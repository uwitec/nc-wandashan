package nc.bs.ic.pub;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nc.bs.pub.SuperDMO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.trade.business.HYPubBO;
import nc.bs.trade.comsave.BillSave;
import nc.bs.wds.tray.lock.LockTrayBO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.SQLParameter;
import nc.ui.scm.util.ObjectUtils;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.trade.voutils.IFilter;
import nc.vo.wds.ic.cargtray.SmallTrayVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class WdsIcInPubBillSave extends BillSave {
	private SuperDMO dmo = new SuperDMO();
	private SuperDMO getSuperDMO(){
		if(dmo == null){
			dmo = new SuperDMO();
		}
		return dmo;
	}
	private IcInPubBO dao = null;
	private IcInPubBO getOutBO(){
		if(dao == null)
			dao = new IcInPubBO();
		return dao;
	}
	
	private TempTableUtil ttutil = null;
	private TempTableUtil getTtutil(){
		if(ttutil == null){
			ttutil = new TempTableUtil();
		}
		return ttutil;
	}
	
	private LockTrayBO lockbo = null;
	private LockTrayBO getLockTrayBO(){
		if(lockbo == null)
			lockbo = new LockTrayBO(getOutBO().getDao(),getOutBO().getStockBO(),getSuperDMO(),getTtutil());
		return lockbo;
	}
	
	class filterDelLine implements IFilter{
		public boolean accept(Object o) {
			// TODO Auto-generated method stub
			if(o == null)
				return false;
			SuperVO vo = (SuperVO)o;
			if(vo.getStatus() == VOStatus.DELETED)
				return false;
			return true;
		}
		
	}
	
	/**
	 * 单据VO保存。 保存 先删除托盘信息  再保存新的托盘信息
	 *    1单据vo保存  2托盘信息保存 3来源单据回写 4生成运输确认单
	 * 创建日期：(2004-2-27 11:15:29)
	 * @return ArrayList
	 * @param vo nc.vo.pub.AggregatedValueObject

	 */
	public java.util.ArrayList saveBill(nc.vo.pub.AggregatedValueObject billVo)
	throws BusinessException {

		if(billVo==null)
			throw new BusinessException("传入数据为空");
		boolean isAdd = false;
		boolean bodyChanged = false;
		
		AggregatedValueObject oldbillVo = null;//备份原始单据信息
		try {
			oldbillVo = (AggregatedValueObject)ObjectUtils.serializableClone(billVo);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new BusinessException(e1);
		}
		
		TbGeneralHVO head = (TbGeneralHVO)billVo.getParentVO();
		
		Map<String, SmallTrayVO[]> lockTrayInfor = (Map<String, SmallTrayVO[]>)((OtherInBillVO)billVo).getOUserObj();
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getPrimaryKey())==null)
			isAdd = true;
		
		
		//////////////////////////////////////////////////////////////////////////////////////////
		
		TbGeneralBVO[] obodys = (TbGeneralBVO[])billVo.getChildrenVO();
		
		//过滤掉删除行  zhf add
		TbGeneralBVO[] bodys = (TbGeneralBVO[])nc.vo.trade.voutils.VOUtil.filter(obodys, new filterDelLine());
		billVo.setChildrenVO(bodys);

		if(null != bodys && bodys.length > 0){
			//yf记录表体是否有改动
			bodyChanged = true;
			for(TbGeneralBVO body:bodys){
				body.validateOnSave();
			}
		}
		
		
//		zhf add  校验  虚拟托盘的绑定关系
		getLockTrayBO().checkInBillOnSave(lockTrayInfor, bodys, head.getGeh_cwarehouseid(), head.getPk_cargdoc());
		
		///////////////////////////////////////////////////////////////////////////////////////////////
		
		if(!isAdd && bodyChanged){//修改保存先删除  已存在的托盘明细子表信息  和 回复托盘存量信息
			getOutBO().deleteOtherInforOnDelBill(head.getPrimaryKey(),(TbGeneralBVO[])oldbillVo.getChildrenVO());
		}
		
		//回写必须在保存之前，保存之后再在同一事务中新数据和旧数据会完全一样，并且保存之后vo的状态不再是修改状态
		try {
			WriteBackTool.writeBack((SuperVO[])(billVo.getChildrenVO()), "tb_outgeneral_b", "general_b_pk", new String[]{"geb_nmny"}, new String[]{"ntagnum"});
		} catch (Exception e) {
			
			throw new BusinessException(e);
		}
		
		getOutBO().writeBackForInBill((OtherInBillVO)oldbillVo,IBDACTION.SAVE,isAdd);
		
		java.util.ArrayList retAry = super.saveBill(billVo);

		if(retAry == null || retAry.size() == 0){
			throw new BusinessException("保存失败");
		}
		OtherInBillVO newBillVo = (OtherInBillVO)retAry.get(1);
		if(newBillVo == null){
			throw new BusinessException("保存失败");
		}	
		if(bodyChanged){
			//插入托盘信息流水表
			insertTrayInfor(newBillVo, (OtherInBillVO)oldbillVo);
			//插入库存存量状态表  更新托盘状态为已占用
			TbGeneralBVO[] newbodys = (TbGeneralBVO[])newBillVo.getChildrenVO();
			getOutBO().insertInvBatchState(newbodys, head.getPk_cargdoc());
		}	
		
//		zhf add
		if(lockTrayInfor != null && lockTrayInfor.size() > 0){
			//如果存在绑定实际托盘信息   保存绑定关系
//			LockTrayBO lockbo = new LockTrayBO();
			getLockTrayBO().doSaveLockTrayInfor(PuPubVO.getString_TrimZeroLenAsNull(retAry.get(0)),newBillVo.getHeaderVo().getGeh_cwarehouseid(),lockTrayInfor);
		}	
		
//		转分仓流程入库时  是否自动调整 入库偏差量
		//yf表体没有改动则不进行判定
		if(head.getGeh_billtype().equalsIgnoreCase(WdsWlPubConst.BILLTYPE_OTHER_IN) && bodyChanged){
			saveBillOnAdjust(newBillVo);
		}
		return retAry;
	}
	
	
	
	
	
	
	
	
	
//	/**
//	 * 
//	 * @作者：zhf
//	 * @说明：完达山物流项目 
//	 * @时间：2011-4-7下午04:57:17
//	 * @param arg1
//	 * @param sLogUser
//	 * @param uLogDate
//	 * @param sLogCorp
//	 * @param itype 0 发运制单 1 销售订单 2 分厂直流 3拆分订单4 合并订单 8 出库自制单据生成的运单
//	 * @throws Exception
//	 */
//	public void insertFyd(AggregatedValueObject arg1) throws Exception {
//		if(arg1 == null)
//			return;
//		MyBillVO billVo = (MyBillVO)arg1;
//		TbGeneralHVO generalh =(TbGeneralHVO) arg1.getParentVO();
//		TbGeneralBVO[] generalb = (TbGeneralBVO[])arg1.getChildrenVO();
//		Object[] o = new Object[3];
//		o[0] = false;
//		TbFydnewVO fydvo = new TbFydnewVO();
//		List<TbFydmxnewVO[]> fydmxList = new ArrayList<TbFydmxnewVO[]>();
//		HYPubBO hybo = new HYPubBO();
//
//		// 进行VO转换/////////////////////////////////////////////
//
//		// ------------转换表头对象-----------------//
//		
//		if (null != generalh && null != generalb && generalb.length > 0) {
//			if (null != generalh.getVdiliveraddress()
//					&& !"".equals(generalh.getVdiliveraddress())) {
//				fydvo.setFyd_shdz(generalh.getVdiliveraddress()); // 收货地址
//			}
//			if (null != generalh.getVnote() && !"".equals(generalh.getVnote())) {
//				fydvo.setFyd_bz(generalh.getVnote()); // 备注
//			}
//			if (null != generalh.getCdptid()
//					&& !"".equals(generalh.getCdptid())) {
//				fydvo.setCdeptid(generalh.getCdptid()); // 部门
//			}
//			// 设置运货方式
//			fydvo.setFyd_yhfs("汽运");
//			// 单据类型 0 发运制单 1 销售订单 2 分厂直流 3拆分订单4 合并订单 8 出库自制单据生成的运单
//			fydvo.setBilltype(billVo.getItype());
//			fydvo.setVbillstatus(1);
//			// 单据号----------------------------------------------------------------
//			fydvo.setVbillno(hybo.getBillNo(WdsWlPubConst.BILLTYPE_SEND_CONFIRM, billVo.getSLogCorp(), null, null));
//			// 制单日期
//			fydvo.setDmakedate(billVo.getULogDate());
//			fydvo.setVoperatorid(billVo.getSLogUser()); // 设置制单人
//			// 设置发货站
//			fydvo.setSrl_pk(generalh.getSrl_pk());
//			// 到货站
//			fydvo.setSrl_pkr(generalh.getSrl_pkr());
//			// --------------转换表头结束---------------//
//			// --------------转换表体----------------//
//			List<TbFydmxnewVO> tbfydmxList = new ArrayList<TbFydmxnewVO>();
//			for (int j = 0; j < generalb.length; j++) {
//				TbFydmxnewVO fydmxnewvo = new TbFydmxnewVO();
//				TbGeneralBVO genb = generalb[j];
//				if (null != genb.getCinventoryid()
//						&& !"".equals(genb.getCinventoryid())) {
//					fydmxnewvo.setPk_invbasdoc(genb.getCinventoryid()); // 单品主键
//				}
//				if (null != genb.getNshouldoutnum()
//						&& !"".equals(genb.getNshouldoutnum())) {
//					fydmxnewvo.setCfd_yfsl(genb.getNshouldoutnum()); // 应发数量
//				}
//				if (null != genb.getNshouldoutassistnum()
//						&& !"".equals(genb.getNshouldoutassistnum())) {
//					fydmxnewvo.setCfd_xs(genb.getNshouldoutassistnum()); // 箱数
//				}
//				if (null != genb.getNoutnum() && !"".equals(genb.getNoutnum())) {
//					fydmxnewvo.setCfd_sfsl(genb.getNoutnum()); // 实发数量
//				}
//				if (null != genb.getNoutassistnum()
//						&& !"".equals(genb.getNoutassistnum())) {
//					fydmxnewvo.setCfd_sffsl(genb.getNoutassistnum()); // 实发辅数量
//				}
//				if (null != genb.getCrowno() && !"".equals(genb.getCrowno())) {
//					fydmxnewvo.setCrowno(genb.getCrowno()); // 行号
//				}
//				if (null != genb.getUnitid() && !"".equals(genb.getUnitid())) {
//					fydmxnewvo.setCfd_dw(genb.getUnitid()); // 单位
//				}
//				fydmxnewvo.setCfd_pc(genb.getVbatchcode()); // 批次		
//				fydmxnewvo.setVsourcebillcode(WdsWlPubConst.BILLTYPE_OTHER_OUT);
//				fydmxnewvo.setCsourcebillbid(genb.getGeneral_b_pk());
//				fydmxnewvo.setCsourcebillhid(genb.getGeneral_pk());
//				tbfydmxList.add(fydmxnewvo);
//			}
//			// ----------------转换表体结束---------------------//
//				TbFydmxnewVO[] fydmxVO = new TbFydmxnewVO[tbfydmxList.size()];
//				tbfydmxList.toArray(fydmxVO);
//				fydmxList.add(fydmxVO);
//				o[0] = true;
//		HYBillVO newBillVo = new HYBillVO();
//		newBillVo.setParentVO(fydvo);
//		newBillVo.setChildrenVO(fydmxVO);
//		saveBD(newBillVo, null);
//	}
//	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 插入库存单据托盘明细流水子表
	 * @时间：2011-4-7下午02:28:26
	 * @param newBillVo  保存后的 库存单据vo
	 * @param oldBillVo  保存前库存单据vo
	 * @throws BusinessException
	 */
	private void insertTrayInfor(OtherInBillVO newBillVo,OtherInBillVO oldBillVo) throws BusinessException{
		if(newBillVo == null || oldBillVo == null){
			return;
		}
		TbGeneralBVO[] newbodys = (TbGeneralBVO[])newBillVo.getChildrenVO();
		TbGeneralBVO[] oldbodys = (TbGeneralBVO[])oldBillVo.getChildrenVO();
		//		分厂 的  入库保存 不受该校验约束
		String cware = newBillVo.getHeaderVo().getGeh_cwarehouseid();
		if(PuPubVO.getString_TrimZeroLenAsNull(cware)==null)
			throw new BusinessException("入库仓库为空");
		if(WdsWlPubTool.isZc(cware))
			checkTrayUsed(oldbodys);
		for(TbGeneralBVO old : oldbodys ){
			String oldno = old.getGeb_crowno();//行号
			for(TbGeneralBVO newbody:newbodys){
				String newno = newbody.getGeb_crowno();//新行号
				if(oldno.equals(newno)){
					List<TbGeneralBBVO> ltraytmp = old.getTrayInfor();
					for(TbGeneralBBVO tray:ltraytmp){
						tray.setGeb_pk(newbody.getPrimaryKey());
						tray.setGeh_pk(newbody.getGeh_pk());
					}
					getSuperDMO().insertList(ltraytmp);
					TbGeneralBBVO[] bvo = (TbGeneralBBVO[])getSuperDMO().queryByWhereClause(TbGeneralBBVO.class, " geb_pk = '"+newbody.getPrimaryKey()+"' and isnull(dr,0) = 0 ");
					//					if(ltraytmp==null||tmps.length==0||tmps.length!=ltraytmp.size()){
					//						throw new BusinessException("保存托盘信息失败");
					//					}
					//					int index2 = 0;
					//					for(String tmp:tmps){
					//						ltraytmp.get(index2).setPrimaryKey(tmp);
					//						index2++;
					//					}

					newbody.setTrayInfor(arrayTolist(bvo));
					//					index++;
				}
			}
		}
	}
	public List<TbGeneralBBVO> arrayTolist(TbGeneralBBVO[] bvo){
		List<TbGeneralBBVO> list  = null;
		if(bvo!=null && bvo.length>0){
			list = new ArrayList<TbGeneralBBVO>();
			for(TbGeneralBBVO b : bvo){
				list.add(b);
			}
		}
		return list;
	}
	/**
	 * 
	 * @throws BusinessException
	 * @作者：zhf
	 * @说明：完达山物流项目 
	 * @时间：2011-4-13下午09:02:49
	 */
	private void checkTrayUsed(TbGeneralBVO[] bodys) throws BusinessException{
		String sql ="select cdt_traycode from bd_cargdoc_tray  " +
				" where cdt_pk=? and cdt_traystatus="+StockInvOnHandVO.stock_state_use;
		SQLParameter para = new SQLParameter();
		for(TbGeneralBVO bvo:bodys){
			List<TbGeneralBBVO> tray = bvo.getTrayInfor();
		if(bvo.getPrimaryKey()==null || bvo.getPrimaryKey().length()==0){
			for(int i=0;i<tray.size();i++){
				String cdp_pk =tray.get(i).getCdt_pk();
				para.addParam(cdp_pk);
				Object o =getOutBO().getDao().executeQuery(sql, para, WdsPubResulSetProcesser.COLUMNPROCESSOR);
				if(o!=null && ! (((String)o).substring(0,2)).equalsIgnoreCase(WdsWlPubConst.XN_CARGDOC_TRAY_NAME)){
					throw new BusinessException("第"+(i+1)+"行，指定的托盘:"+o.toString()+"\n已被抢先使用，请重新指定");
				}
				para.clearParams();
			}
		  }
		}
		
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 
	 * @时间：2011-7-13上午10:10:34
	 * @param billVo
	 * @return
	 * @throws BusinessException
	 */
	public void saveBillOnAdjust(nc.vo.pub.AggregatedValueObject billVo)
	throws BusinessException {

		if(billVo==null)
			throw new BusinessException("传入数据为空");
		
		String cinstoreid = PuPubVO.getString_TrimZeroLenAsNull(((TbGeneralHVO)billVo.getParentVO()).getGeh_cwarehouseid());
		if(cinstoreid == null){
			throw new BusinessException("入库仓库为空");
		}
		boolean flag = WdsWlPubTool.isAutoAdjustStore(cinstoreid); 
		if(!flag)
			return;
		
//		生成调整入库单
		OtherInBillVO inbill = creatAdjustInBill(billVo);
		if(inbill == null)
			return;
//		保存调整入库单
		List lret = super.saveBill(inbill);
		getOutBO().writeBackForInBill(inbill,IBDACTION.SAVE,true);
		OtherInBillVO newin = (OtherInBillVO)lret.get(1);
//		生成调整出库单
		MyBillVO outbillvo = creatAdjustOutBill(newin, inbill.getHeaderVo().getCoperatorid(), inbill.getHeaderVo().getCopetadate().toString(), inbill.getHeaderVo().getPk_corp());
		
		if(outbillvo == null){
			throw new BusinessException("数据异常");
		}
//		保存调整出库单
		super.saveBill(outbillvo);
	}
	
	private HYPubBO hybo = null;
	private HYPubBO getHyBO(){
		if(hybo == null)
			hybo = new HYPubBO();
		return hybo;
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 转分仓入库单保存后  由于实入数量和应入数量不一致  对偏差量进行系统自动调整 （一入 一出）
	 * @时间：2011-7-13上午10:50:08
	 * @param billVo
	 * @return
	 * @throws BusinessException
	 */
	private OtherInBillVO creatAdjustInBill(nc.vo.pub.AggregatedValueObject billVo) throws BusinessException{
		if(billVo == null)
			return null;
		TbGeneralHVO head = (TbGeneralHVO)billVo.getParentVO();
		TbGeneralBVO[] bodys = (TbGeneralBVO[])billVo.getChildrenVO();
		if(head == null || bodys == null||bodys.length ==0){			
			throw new BusinessException("数据为空");			
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(bodys[0].getCsourcetype())==null || !PuPubVO.getString_TrimZeroLenAsNull(bodys[0].getCsourcetype()).equalsIgnoreCase(WdsWlPubConst.BILLTYPE_OTHER_OUT)){
			return null;
		}
		List<TbGeneralBVO> lnewbody = new ArrayList<TbGeneralBVO>();
		UFDouble nnum = null;
		UFDouble nassnum = null;
		TbGeneralBVO newbody = null;
		OtherInBillVO bill = null;
//		HYPubBO hybo = new HYPubBO();
		for(TbGeneralBVO body:bodys){
			if(PuPubVO.getString_TrimZeroLenAsNull(body.getCsourcetype())==null || !PuPubVO.getString_TrimZeroLenAsNull(body.getCsourcetype()).equalsIgnoreCase(WdsWlPubConst.BILLTYPE_OTHER_OUT)){
				continue;
			}
			newbody = (TbGeneralBVO)body.clone();
			newbody.validateOnZdrk(true);
			nnum = PuPubVO.getUFDouble_NullAsZero(newbody.getGeb_snum()).sub(PuPubVO.getUFDouble_NullAsZero(newbody.getGeb_anum()));
			nassnum = PuPubVO.getUFDouble_NullAsZero(newbody.getGeb_bsnum()).sub(PuPubVO.getUFDouble_NullAsZero(newbody.getGeb_banum()));
			if(nnum.doubleValue()<=0)
				continue;
			if(nassnum.doubleValue()<=0)
				continue;
			newbody.setGeb_snum(nnum);
			newbody.setGeb_bsnum(nassnum);
			newbody.setGeb_anum(nnum);
			newbody.setGeb_banum(nassnum);
			newbody.setGeb_nmny(newbody.getGeb_anum().multiply(PuPubVO.getUFDouble_NullAsZero(newbody.getGeb_nprice())));
			
//			来源信息  补录  为了 回写 出库单  的累计出库数量  如此赋值  zhf
			newbody.setCsourcebillbid(body.getCsourcebillbid());
			newbody.setCsourcebillhid(body.getCsourcebillhid());
			newbody.setCsourcetype(body.getCsourcetype());
			
			newbody.setCfirstbillbid(body.getPrimaryKey());
			newbody.setCfirstbillhid(body.getGeh_pk());
			newbody.setCfirsttype(WdsWlPubConst.BILLTYPE_OTHER_IN);
			
			newbody.setGeh_pk(null);
			newbody.setGeb_pk(null);
			
			body.setStatus(VOStatus.NEW);
			lnewbody.add(newbody);
		}
		
		if(lnewbody.size()>0){
			bill = new OtherInBillVO();
			TbGeneralHVO newhead = (TbGeneralHVO)head.clone();
			newhead.setGeh_pk(null);
			newhead.setGeh_customize7("Y");
			newhead.setGeh_billcode(getHyBO().getBillNo(WdsWlPubConst.BILLTYPE_OTHER_IN, head.getPk_corp(), null, null));
			bill.setParentVO(newhead);
			bill.setChildrenVO(lnewbody.toArray(new TbGeneralBVO[0]));
			return bill;
		}
		return null;
	}	
	
	private MyBillVO creatAdjustOutBill(OtherInBillVO inBillVo,
			String coprator, String date, String corp) throws BusinessException {
		if (inBillVo == null)
			return null;
		PfParameterVO paraVo = new PfParameterVO();
		paraVo.m_operator = coprator;
		paraVo.m_currentDate = date;
		paraVo.m_coId = corp;
		MyBillVO outbillvo = (MyBillVO) PfUtilTools.runChangeData(
				WdsWlPubConst.BILLTYPE_OTHER_IN,
				WdsWlPubConst.BILLTYPE_OTHER_OUT, inBillVo, paraVo);
		if(outbillvo == null)
			return null;
		String vbillno = getHyBO().getBillNo(WdsWlPubConst.BILLTYPE_OTHER_OUT, corp, null, null);
		if(vbillno == null){
			throw new BusinessException("获取其他出库单单据号出错");
		}
		outbillvo.getParentVO().setAttributeValue("vbillcode", vbillno);
		return outbillvo;
	}
}

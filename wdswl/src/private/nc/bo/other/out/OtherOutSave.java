package nc.bo.other.out;

import java.util.ArrayList;
import java.util.List;

import nc.bs.pub.SuperDMO;
import nc.bs.trade.business.HYPubBO;
import nc.bs.wds.w8004040204.W8004040204Impl;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.jdbc.framework.SQLParameter;
import nc.vo.dm.confirm.TbFydmxnewVO;
import nc.vo.dm.confirm.TbFydnewVO;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wl.pub.VOTool;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 
 * @author Administrator
 * 其他出库后台类
 */
public class OtherOutSave  extends nc.bs.trade.comsave.BillSave {

	private SuperDMO dmo = new SuperDMO();
	private SuperDMO getSuperDMO(){
		if(dmo == null){
			dmo = new SuperDMO();
		}
		return dmo;
	}
	private OtherOutBO dao = null;
	private OtherOutBO getOutBO(){
		if(dao == null)
			dao = new OtherOutBO();
		return dao;
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
		MyBillVO old_billVo = (MyBillVO)VOTool.aggregateVOClone(billVo);
		boolean isAdd = false;
		boolean bodyChanged = false;
		TbOutgeneralHVO head = (TbOutgeneralHVO)billVo.getParentVO();
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getPrimaryKey())==null)
			isAdd = true;
		TbOutgeneralBVO[] bodys = null;	
		if(billVo.getChildrenVO() != null && billVo.getChildrenVO().length > 0){
			bodyChanged = true;
			bodys = (TbOutgeneralBVO[])billVo.getChildrenVO();
			for(TbOutgeneralBVO body:bodys){
				body.validationOnSave();
			}
		}
		if(!isAdd && bodyChanged){//修改保存先删除  已存在的托盘明细子表信息  和 回复托盘存量信息
			TbOutgeneralBVO[] bb = (TbOutgeneralBVO[])old_billVo.getChildrenVO();
			for(TbOutgeneralBVO b : bb){
				String wheresql = " general_b_pk = '"+b.getPrimaryKey()+"' and isnull(dr,0)=0";
				List<TbOutgeneralTVO> ltray = (List<TbOutgeneralTVO> )getOutBO().getBaseDAO().retrieveByClause(TbOutgeneralTVO.class, wheresql);
				if(ltray!=null && ltray.size()>0)
					getOutBO().deleteOtherInforOnDelBill(ltray);
			}
		}
		//保存后  回写数据来源
		getOutBO().writeBack(old_billVo,IBDACTION.SAVE,isAdd);
		//---------------------------保存前校验结束----------------------------------------	
		java.util.ArrayList retAry = super.saveBill(billVo);

		if(retAry == null || retAry.size() == 0){
			throw new BusinessException("保存失败");
		}
		MyBillVO newBillVo = (MyBillVO)retAry.get(1);
		if(newBillVo == null){
			throw new BusinessException("保存失败");
		}	
		if(bodyChanged){
			//插入托盘信息流水表
			insertTrayInfor(newBillVo, old_billVo);
			//更新库存存量状态表
			W8004040204Impl handbo = new W8004040204Impl();
			try {
				TbOutgeneralBVO[] newbodys = (TbOutgeneralBVO[])newBillVo.getChildrenVO();
				for(TbOutgeneralBVO newbody:newbodys){
					handbo.updateWarehousestock(newbody.getTrayInfor());
				}
//				//生成发运单
//				if(PuPubVO.getUFBoolean_NullAs(((TbOutgeneralHVO)newBillVo.getParentVO()).getIs_yundan(), UFBoolean.FALSE).booleanValue()){
//					insertFyd(newBillVo);
//				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if(e instanceof BusinessException){
					throw (BusinessException)e;
				}
				throw new BusinessException(e);
			}		
		}
			

	
		return retAry;
	}
	
	
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 
	 * @时间：2011-4-7下午04:57:17
	 * @param arg1
	 * @param sLogUser
	 * @param uLogDate
	 * @param sLogCorp	 * @param itype 0 发运制单 1 销售订单 2 分厂直流 3拆分订单4 合并订单 8 出库自制单据生成的运单
	 * @throws Exception
	 */
	public void insertFyd(AggregatedValueObject arg1) throws Exception {
		if(arg1 == null)
			return;
		MyBillVO billVo = (MyBillVO)arg1;
		TbOutgeneralHVO generalh =(TbOutgeneralHVO) arg1.getParentVO();
		TbOutgeneralBVO[] generalb = (TbOutgeneralBVO[])arg1.getChildrenVO();
		Object[] o = new Object[3];
		o[0] = false;
		TbFydnewVO fydvo = new TbFydnewVO();
		List<TbFydmxnewVO[]> fydmxList = new ArrayList<TbFydmxnewVO[]>();
		HYPubBO hybo = new HYPubBO();

		// 进行VO转换/////////////////////////////////////////////

		// ------------转换表头对象-----------------//
		
		if (null != generalh && null != generalb && generalb.length > 0) {
			if (null != generalh.getVdiliveraddress()
					&& !"".equals(generalh.getVdiliveraddress())) {
				fydvo.setFyd_shdz(generalh.getVdiliveraddress()); // 收货地址
			}
			if (null != generalh.getVnote() && !"".equals(generalh.getVnote())) {
				fydvo.setFyd_bz(generalh.getVnote()); // 备注
			}
			if (null != generalh.getCdptid()
					&& !"".equals(generalh.getCdptid())) {
				fydvo.setCdeptid(generalh.getCdptid()); // 部门
			}
			// 设置运货方式
			fydvo.setFyd_yhfs("汽运");
			// 单据类型 0 发运制单 1 销售订单 2 分厂直流 3拆分订单4 合并订单 8 出库自制单据生成的运单
			fydvo.setBilltype(billVo.getItype());
			fydvo.setVbillstatus(1);
			// 单据号----------------------------------------------------------------
			fydvo.setVbillno(hybo.getBillNo(WdsWlPubConst.BILLTYPE_SEND_CONFIRM, billVo.getSLogCorp(), null, null));
			// 制单日期
			fydvo.setDmakedate(billVo.getULogDate());
			fydvo.setVoperatorid(billVo.getSLogUser()); // 设置制单人
			// 设置发货站
			fydvo.setSrl_pk(generalh.getSrl_pk());
			// 到货站
			fydvo.setSrl_pkr(generalh.getSrl_pkr());
			// --------------转换表头结束---------------//
			// --------------转换表体----------------//
			List<TbFydmxnewVO> tbfydmxList = new ArrayList<TbFydmxnewVO>();
			for (int j = 0; j < generalb.length; j++) {
				TbFydmxnewVO fydmxnewvo = new TbFydmxnewVO();
				TbOutgeneralBVO genb = generalb[j];
				if (null != genb.getCinventoryid()
						&& !"".equals(genb.getCinventoryid())) {
					fydmxnewvo.setPk_invbasdoc(genb.getCinventoryid()); // 单品主键
				}
				if (null != genb.getNshouldoutnum()
						&& !"".equals(genb.getNshouldoutnum())) {
					fydmxnewvo.setCfd_yfsl(genb.getNshouldoutnum()); // 应发数量
				}
				if (null != genb.getNshouldoutassistnum()
						&& !"".equals(genb.getNshouldoutassistnum())) {
					fydmxnewvo.setCfd_xs(genb.getNshouldoutassistnum()); // 箱数
				}
				if (null != genb.getNoutnum() && !"".equals(genb.getNoutnum())) {
					fydmxnewvo.setCfd_sfsl(genb.getNoutnum()); // 实发数量
				}
				if (null != genb.getNoutassistnum()
						&& !"".equals(genb.getNoutassistnum())) {
					fydmxnewvo.setCfd_sffsl(genb.getNoutassistnum()); // 实发辅数量
				}
				if (null != genb.getCrowno() && !"".equals(genb.getCrowno())) {
					fydmxnewvo.setCrowno(genb.getCrowno()); // 行号
				}
				if (null != genb.getUnitid() && !"".equals(genb.getUnitid())) {
					fydmxnewvo.setCfd_dw(genb.getUnitid()); // 单位
				}
				fydmxnewvo.setCfd_pc(genb.getVbatchcode()); // 批次		
				fydmxnewvo.setVsourcebillcode(WdsWlPubConst.BILLTYPE_OTHER_OUT);
				fydmxnewvo.setCsourcebillbid(genb.getGeneral_b_pk());
				fydmxnewvo.setCsourcebillhid(genb.getGeneral_pk());
				tbfydmxList.add(fydmxnewvo);
			}
			// ----------------转换表体结束---------------------//
				TbFydmxnewVO[] fydmxVO = new TbFydmxnewVO[tbfydmxList.size()];
				tbfydmxList.toArray(fydmxVO);
				fydmxList.add(fydmxVO);
				o[0] = true;
		HYBillVO newBillVo = new HYBillVO();
		newBillVo.setParentVO(fydvo);
		newBillVo.setChildrenVO(fydmxVO);
		saveBD(newBillVo, null);
	}
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 插入库存单据托盘明细流水子表
	 * @时间：2011-4-7下午02:28:26
	 * @param newBillVo  保存后的 库存单据vo
	 * @param oldBillVo  保存前库存单据vo
	 * @throws BusinessException
	 */
	private void insertTrayInfor(MyBillVO newBillVo,MyBillVO oldBillVo) throws BusinessException{
		if(newBillVo == null || oldBillVo == null){
			return;
		}
		TbOutgeneralBVO[] newbodys = (TbOutgeneralBVO[])newBillVo.getChildrenVO();
		TbOutgeneralBVO[] oldbodys = (TbOutgeneralBVO[])oldBillVo.getChildrenVO();	
		checkTrayUsed(oldbodys);
//		String[] tmps = null;
		for(TbOutgeneralBVO old : oldbodys){
			String oldno = old.getCrowno();
			for(TbOutgeneralBVO newbody:newbodys){
				String newno = newbody.getCrowno();
				if(oldno.equals(newno)){
					List<TbOutgeneralTVO> ltraytmp = old.getTrayInfor();
					if(ltraytmp == null || ltraytmp.size() == 0){
						throw new BusinessException("无托盘信息，保存托盘信息失败！");
					}
					for(TbOutgeneralTVO tray:ltraytmp){
						tray.setGeneral_b_pk(newbody.getPrimaryKey());
						tray.setGeneral_pk(newbody.getGeneral_pk());
					}
					getSuperDMO().insertList(ltraytmp);
					TbOutgeneralTVO[] bvo = (TbOutgeneralTVO[])getSuperDMO().queryByWhereClause(TbOutgeneralTVO.class, " general_b_pk = '"+newbody.getPrimaryKey()+"' and isnull(dr,0) = 0 ");
					newbody.setTrayInfor(arrayTolist(bvo));
					break;
				}
			}
		}
	}
	
	public List<TbOutgeneralTVO> arrayTolist(TbOutgeneralTVO[] bvo){
		List<TbOutgeneralTVO> list  = null;
		if(bvo!=null && bvo.length>0){
			list = new ArrayList<TbOutgeneralTVO>();
			for(TbOutgeneralTVO b : bvo){
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
	private void checkTrayUsed(TbOutgeneralBVO[] bodys) throws BusinessException{
		String sql ="select cdt_traycode from bd_cargdoc_tray  " +
				" join tb_warehousestock on tb_warehousestock.pplpt_pk = bd_cargdoc_tray.cdt_pk " +
				" where bd_cargdoc_tray.cdt_pk=? and (bd_cargdoc_tray.cdt_traystatus=0 or  coalesce(tb_warehousestock.whs_stocktonnage,0)<? and tb_warehousestock.whs_stocktonnage>0)";
		SQLParameter para = new SQLParameter();
		for(TbOutgeneralBVO bvo:bodys){
			List<TbOutgeneralTVO> tray = bvo.getTrayInfor();
			for(int i=0;i<tray.size();i++){
				String cdp_pk =tray.get(i).getCdt_pk();
				UFDouble  noutnum = PuPubVO.getUFDouble_NullAsZero(tray.get(i).getNoutnum());
				para.addParam(cdp_pk);
				para.addParam(noutnum);
				Object o =getOutBO().getBaseDAO().executeQuery(sql, para, WdsPubResulSetProcesser.COLUMNPROCESSOR);
				if(o!=null){
					throw new BusinessException("第"+(i+1)+"行，指定的托盘:"+o.toString()+"\n已被抢先使用，不够出货，请重新指定");
				}
				para.clearParams();
			}
		}
		
	}
	
//	/**
//	 * 根据出库子表信息删除缓存里面数据
//	 * 
//	 * @param generalb
//	 *            出库子表
//	 * @throws BusinessException
//	 */
//	private void deleteTrayInfor(TbOutgeneralBVO[] generalb)
//			throws BusinessException {
//		if (null != generalb && generalb.length > 0) {
//			for (int i = 0; i < generalb.length; i++) {
//				String strWhere = " pk_invbasdoc='"
//						+ generalb[i].getCinventoryid()
//						+ "' and cfirstbillbid ='"
//						+ generalb[i].getCsourcebillbid() + "'";
//				getBaseDao().deleteByClause(TbOutgeneralTVO.class, strWhere);
//			}
//		}
//	}

}

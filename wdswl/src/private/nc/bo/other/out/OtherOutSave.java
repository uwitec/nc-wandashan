package nc.bo.other.out;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nc.bs.pub.SuperDMO;
import nc.bs.wds.tray.lock.LockTrayBO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.jdbc.framework.SQLParameter;
import nc.ui.scm.util.ObjectUtils;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.trade.voutils.IFilter;
import nc.vo.wds.ic.cargtray.SmallTrayVO;

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
		MyBillVO old_billVo = null;
		try {
			old_billVo = (MyBillVO)ObjectUtils.serializableClone(billVo);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new BusinessException(e1);
		}

		//		zhf add
		Map<String, SmallTrayVO[]> trayInfor = (Map<String,SmallTrayVO[]>)((MyBillVO)billVo).getOUserObj();

		//		校验  进行了 绑定实际托盘的虚拟托盘 必须  指定  解除绑定信息
		LockTrayBO lockbo = new LockTrayBO();
		lockbo.checkIsLock((MyBillVO)billVo);
		//		zhf end

		boolean isAdd = false;
		boolean bodyChanged = false;//表体是否修改
		TbOutgeneralHVO head = (TbOutgeneralHVO)billVo.getParentVO();
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getPrimaryKey())==null)
			isAdd = true;


		TbOutgeneralBVO[] bodys = (TbOutgeneralBVO[])billVo.getChildrenVO();
		//过滤掉删除行  zhf add
		bodys = (TbOutgeneralBVO[])nc.vo.trade.voutils.VOUtil.filter(bodys, new filterDelLine());

		if(bodys!=null && bodys.length > 0){
			bodyChanged = true;
			for(TbOutgeneralBVO body:bodys){
				body.validationOnSave();
			}
		}

		if(!isAdd && old_billVo.getChildrenVO().length>0){//修改保存先删除  已存在的托盘明细子表信息  和 回复托盘存量信息
			TbOutgeneralBVO[] bb = (TbOutgeneralBVO[])old_billVo.getChildrenVO();
			for(TbOutgeneralBVO b : bb){
				String wheresql = " general_b_pk = '"+b.getPrimaryKey()+"' and isnull(dr,0)=0";
				List<TbOutgeneralTVO> ltray = (List<TbOutgeneralTVO> )getOutBO().getBaseDAO().retrieveByClause(TbOutgeneralTVO.class, wheresql);
				if(ltray!=null && ltray.size()>0)
					getOutBO().deleteOtherInforOnDelBill(head.getSrl_pk(),ltray);
			}
		}

		//保存后  回写数据来源
		getOutBO().writeBack(old_billVo,IBDACTION.SAVE,isAdd);
		//---------------------------保存前校验结束----------------------------------------	
		java.util.ArrayList retAry = super.saveBill(old_billVo);

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
			try {
				TbOutgeneralBVO[] newbodys = (TbOutgeneralBVO[])newBillVo.getChildrenVO();
				List<TbOutgeneralTVO> ltray = new ArrayList<TbOutgeneralTVO>();
				for(TbOutgeneralBVO newbody:newbodys){
					ltray.addAll(newbody.getTrayInfor());
				}
				if(ltray.size()>0)
					getOutBO().updateStockOnSaveBill(head.getPk_corp(),head.getSrl_pk(),ltray);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if(e instanceof BusinessException){
					throw (BusinessException)e;
				}
				throw new BusinessException(e);
			}		
		}

		if(trayInfor != null && trayInfor.size()>0){
			//			解锁  实际托盘
			lockbo.doDelLockTrayInfor(PuPubVO.getString_TrimZeroLenAsNull(retAry.get(0)), head.getSrl_pk(), trayInfor);
		}

		return retAry;
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
		" where bd_cargdoc_tray.cdt_pk=? and tb_warehousestock.whs_pk=? " +
		" and (bd_cargdoc_tray.cdt_traystatus="+StockInvOnHandVO.stock_state_null+" or  " +
		" coalesce(tb_warehousestock.whs_stocktonnage,0)<? " +
		" and tb_warehousestock.whs_stocktonnage>0)";
		SQLParameter para = new SQLParameter();
		for(TbOutgeneralBVO bvo:bodys){
			List<TbOutgeneralTVO> tray = bvo.getTrayInfor();
			for(int i=0;i<tray.size();i++){
				String cdp_pk =tray.get(i).getCdt_pk();
				String whs_pk=tray.get(i).getWhs_pk();
				UFDouble  noutnum = PuPubVO.getUFDouble_NullAsZero(tray.get(i).getNoutnum());
				para.addParam(cdp_pk);
				para.addParam(whs_pk);
				para.addParam(noutnum);
				Object o =getOutBO().getBaseDAO().executeQuery(sql, para, WdsPubResulSetProcesser.COLUMNPROCESSOR);
				if(o!=null){
					throw new BusinessException("第"+(i+1)+"行，指定的托盘:"+o.toString()+"\n已被抢先使用，不够出货，请重新指定");
				}
				para.clearParams();
			}
		}

	}
}

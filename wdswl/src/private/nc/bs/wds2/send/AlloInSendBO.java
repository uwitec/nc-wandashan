package nc.bs.wds2.send;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.trade.business.HYPubBO;
import nc.vo.dm.order.SendorderBVO;
import nc.vo.dm.order.SendorderVO;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wl.pub.Wds2WlPubConst;
import nc.vo.wl.pub.WdsWlPubConst;

public class AlloInSendBO {
	
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null)
			dao = new BaseDAO();
		return dao;
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 调拨入库单 保存时生成调入运单 
	 * @时间：2012-7-10下午12:51:26
	 * @param bill
	 * @throws BusinessException
	 */
	public void createAlloInSendBill(String headid,PfParameterVO para,boolean isnew) throws Exception{
		if(headid == null)
			return;

		//		查询调拨入库单
		OtherInBillVO bill = (OtherInBillVO)new HYPubBO().queryBillVOByPrimaryKey(
				new String[]{OtherInBillVO.class.getName(),
						TbGeneralHVO.class.getName(),
						TbGeneralBVO.class.getName()}, headid);

		if(bill == null){
			throw new BusinessException("数据异常");
		}
		
		if(!isnew){
			updateAlloInSendBill(bill);
		}
		
		//		转换生成
		HYBillVO tarBill = (HYBillVO)PfUtilTools.runChangeData(WdsWlPubConst.BILLTYPE_ALLO_IN, 
				Wds2WlPubConst.billtype_alloinsendorder, bill,para);

		if(tarBill == null)
			throw new BusinessException("数据异常,未生成调入运单");

		check(tarBill);

		new PfUtilBO().processAction("WRITE", Wds2WlPubConst.billtype_alloinsendorder, 
				para.m_currentDate, null, tarBill, null);
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 生成调入运单数据校验 
	 * @时间：2012-7-10下午06:10:33
	 * @param bill
	 * @throws BusinessException
	 */
	public void check(HYBillVO  bill) throws BusinessException{
		if(bill == null)
			throw new  BusinessException("传入数据为空");
		SendorderVO head = (SendorderVO)bill.getParentVO();
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getVdef1())==null)
			throw new BusinessException("调出公司为空");
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getPk_corp())==null)
			throw new BusinessException("调入公司为空");
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getPk_billtype())==null)
			throw new BusinessException("单据类型为空");
		
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getVbillno())==null){
			head.setVbillno(
					new HYPubBO().getBillNo(Wds2WlPubConst.billtype_alloinsendorder, head.getPk_corp(), null, null));
		}
		
		SendorderBVO[] bodys = (SendorderBVO[])bill.getChildrenVO();
		if(bodys == null || bodys.length == 0)
			throw new BusinessException("数据异常，表体数据为空");
		 for(SendorderBVO body:bodys){
			 body.validate();
		 }
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 入库单调整后 运单做相应调整  主要是入库数量调整后 调整运单的接收数量 
	 * @时间：2012-7-10下午06:09:45
	 * @param bill
	 * @throws BusinessException
	 */
	private void updateAlloInSendBill(OtherInBillVO bill) throws BusinessException{
		
	}
	
	public void deleteAlloInSendBill(String alloinheadid) throws BusinessException{
		
	}
}

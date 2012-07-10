package nc.bs.wds2.send;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
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
	public void createAlloInSendBill(OtherInBillVO bill,PfParameterVO para) throws Exception{
		if(bill == null)
			return;
//		转换生成
		HYBillVO tarBill = (HYBillVO)PfUtilTools.runChangeData(WdsWlPubConst.BILLTYPE_ALLO_IN, 
				Wds2WlPubConst.billtype_alloinsendorder, bill,para);
		
		if(tarBill == null)
			throw new BusinessException("数据异常,未生成调入运单");
		
		check(tarBill);
		
		new PfUtilBO().processAction("WRITE", Wds2WlPubConst.billtype_alloinsendorder, 
				para.m_currentDate, null, tarBill, null);
	}
	
	public void check(HYBillVO  bill) throws BusinessException{
		if(bill == null)
			throw new  BusinessException("传入数据为空");
	}

}

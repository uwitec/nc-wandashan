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
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ������ⵥ ����ʱ���ɵ����˵� 
	 * @ʱ�䣺2012-7-10����12:51:26
	 * @param bill
	 * @throws BusinessException
	 */
	public void createAlloInSendBill(OtherInBillVO bill,PfParameterVO para) throws Exception{
		if(bill == null)
			return;
//		ת������
		HYBillVO tarBill = (HYBillVO)PfUtilTools.runChangeData(WdsWlPubConst.BILLTYPE_ALLO_IN, 
				Wds2WlPubConst.billtype_alloinsendorder, bill,para);
		
		if(tarBill == null)
			throw new BusinessException("�����쳣,δ���ɵ����˵�");
		
		check(tarBill);
		
		new PfUtilBO().processAction("WRITE", Wds2WlPubConst.billtype_alloinsendorder, 
				para.m_currentDate, null, tarBill, null);
	}
	
	public void check(HYBillVO  bill) throws BusinessException{
		if(bill == null)
			throw new  BusinessException("��������Ϊ��");
	}

}

package nc.ui.zb.avnum;

import nc.ui.zb.pub.LongTimeTask;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zb.avnum.AvNumBodyVO;
import nc.vo.zb.avnum.AvVendorVO;

public class AvNumHelper {
    private static String bo = "nc.bs.zb.avnum.AvNumBO";
	
	public static HYBillVO  loadAvNumBillVO(String cbiddingid,ClientLink cl) throws Exception{
		Class[] ParameterTypes = new Class[]{String.class,ClientLink.class};
		Object[] ParameterValues = new Object[]{cbiddingid,cl};
		Object o = LongTimeTask.callRemoteService("pu",bo, "loadAvNumBillVO", ParameterTypes, ParameterValues, 2);
		return o==null?null:(HYBillVO)o;
	}
	
	public static HYBillVO saveHyBillVO(HYBillVO billvo) throws Exception{
		checkZbNum(billvo);
		Class[] ParameterTypes = new Class[]{HYBillVO.class};
		Object[] ParameterValues = new Object[]{billvo};
        Object o =LongTimeTask.callRemoteService("pu",bo, "saveHyBillVO", ParameterTypes, ParameterValues, 2);
        return o==null?null:(HYBillVO)o;
	}
	
	private static void checkZbNum(HYBillVO billvo) throws BusinessException {
		if(billvo==null)
			return;
		AvNumBodyVO[] vos =(AvNumBodyVO[])billvo.getChildrenVO();
		if(vos==null || vos.length==0)
			return;
		
		for(AvNumBodyVO vo:vos){
			AvVendorVO[] bvos = vo.getAvVendorVO();
			if(bvos==null || bvos.length==0)
				return;
			UFDouble sum = UFDouble.ZERO_DBL;
			UFDouble zbnum = PuPubVO.getUFDouble_NullAsZero(vo.getNzbnum());
			for(AvVendorVO bvo:bvos){
				sum = sum.add(PuPubVO.getUFDouble_NullAsZero(bvo.getNzbnum()));
			}
//			if(!sum.equals(zbnum))
//				throw new BusinessException("��"+vo.getCrowno()+"�б�������̯�ܺͲ��������б�����");
			
//			zhf modify   �ͻ�����   �б���������С���б�����  �����ܴ���
			if(sum.equals(UFDouble.ZERO_DBL))
				throw new BusinessException("δ����,Ʒ����Ϣ�к�Ϊ"+vo.getCrowno());
			if(sum.compareTo(zbnum)>0)
				throw new BusinessException("��Ӧ���б�����֮�ͳ������б�����,Ʒ����Ϣ�к�Ϊ"+vo.getCrowno());
			
		}
		
	}
	
}

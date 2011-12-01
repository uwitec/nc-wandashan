package nc.vo.dm.so.deal2;

import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.pub.ValidationException;
import nc.vo.trade.pub.HYBillVO;

public class SoDealBillVO extends HYBillVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public SoDealHeaderVo getHeader(){
		return (SoDealHeaderVo)getParentVO();
	}
	public SoDealVO[] getBodyVos(){
		return (SoDealVO[])getChildrenVO();
	}
	public static void checkData(SoDealBillVO[] bills) throws ValidationException {
		SoDealHeaderVo head = null;
		SoDealVO[] bodys = null;
		if(bills ==  null || bills.length == 0){
			throw new ValidationException("����Ϊ��");
		}
		for(SoDealBillVO bill:bills){
			if(bill == null){
				throw new ValidationException("����Ϊ��");
			}
			head = bill.getHeader();
			if(head == null){
				throw new ValidationException("��ͷΪ��");
			}
			bodys = bill.getBodyVos();
			if(bodys == null || bodys.length == 0)
				throw new ValidationException("���ڿձ�������");
			head.validate();
			for(SoDealVO body:bodys){
				body.validataOnDeal();
			}
		}
	}

}

package nc.vo.zb.price.bill;

import nc.vo.pub.ValidationException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;

public class SubmitPriceBillVO extends HYBillVO {

	public SubmitPriceHeaderVO getHeader(){
		return (SubmitPriceHeaderVO)getParentVO();
	}
	public SubmitPriceBodyVO[] getBodys(){
		return (SubmitPriceBodyVO[])getChildrenVO();
	}
	
	public void validata() throws ValidationException{
		SubmitPriceHeaderVO head = getHeader();
		if(head == null)
			throw new ValidationException("数据为空");
		head.validate();		
		SubmitPriceBodyVO[] bodys = getBodys();
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getPrimaryKey())!=null &&(bodys == null || bodys.length ==0))
			return;
		if(bodys == null || bodys.length ==0)
			throw new ValidationException("数据为空");
		for(SubmitPriceBodyVO body:bodys){
			body.validate();
		}
	}
	
	
}

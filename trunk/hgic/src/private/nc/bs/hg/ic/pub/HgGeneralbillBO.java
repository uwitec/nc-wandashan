package nc.bs.hg.ic.pub;

import nc.bs.ic.pub.bill.GeneralBillBO;
import nc.bs.ic.pub.bill.GeneralBillDMO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.BusinessException;

public class HgGeneralbillBO extends GeneralBillBO {

	public Object updateThisBill2(GeneralBillVO voUpdatedBill) throws BusinessException{
		if(voUpdatedBill == null)
			return null;
	 super.updateThisBill(voUpdatedBill);
	 GeneralBillDMO dmo = null;
	 try{
		 dmo = new GeneralBillDMO();
	 }catch(Exception ee){
		 if(ee instanceof BusinessException)
			 throw (BusinessException)ee;
		 throw new BusinessException(ee);
	 }
	 String headid = voUpdatedBill.getHeaderVO().getPrimaryKey();
	 String headts = dmo.queryBillHead(headid).getTs();
	 GeneralBillItemVO[] items = (GeneralBillItemVO[])dmo.queryAllBodyData(voUpdatedBill.getHeaderVO().getPrimaryKey());
	 return new Object[]{headts,items};
	}
}

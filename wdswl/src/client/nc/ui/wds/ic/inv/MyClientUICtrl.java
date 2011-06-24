package nc.ui.wds.ic.inv;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.bill.ISingleController;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.wds.ic.inv.MyBillVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.ui.trade.button.IBillButton;




public class MyClientUICtrl extends AbstractManageController implements ISingleController{

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {		                                
                return new int[]{ IBillButton.Query,
	                              IBillButton.Edit,
	                              IBillButton.Save,
	                              IBillButton.Cancel,
	 	           	              IBillButton.Return,                                                                                    
	                              IBillButton.Refresh
	                              ,IBillButton.Print
	                              };
  
	}
	
	public int[] getListButtonAry() {		
			        	        return new int[]{
	         	           	             IBillButton.Query,
   	         	           	             IBillButton.Edit,
   	         	           	             IBillButton.Save,
   	         	           	             IBillButton.Cancel,
	           	         	           	 IBillButton.Card,
	           	         	           	 IBillButton.Refresh
	           	         	           	 ,IBillButton.Print
	           	         	        
	        };
	
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return true;
	}

	public String getBillType() {
		return WdsWlPubConst.BILLTYPE_IC_INV_STATUS;
	}

	public String[] getBillVoName() {
		return new String[]{
			MyBillVO.class.getName(),
			StockInvOnHandVO.class.getName(),
			StockInvOnHandVO.class.getName()
		};
	}

	public String getBodyCondition() {
		return null;
	}

	public String getBodyZYXKey() {
		return null;
	}

	public int getBusinessActionType() {
		return IBusinessActionType.BD;
	}

	public String getChildPkField() {
		return null;
	}

	public String getHeadZYXKey() {
		return null;
	}

	public String getPkField() {
		return null;
	}

	public Boolean isEditInGoing() throws Exception {
		return null;
	}

	public boolean isExistBillStatus() {
		return false;
	}

	public boolean isLoadCardFormula() {		
		return true;
	}

	public String[] getListBodyHideCol() {	
		return null;
	}

	public String[] getListHeadHideCol() {		
		return null;
	}

	public boolean isShowListRowNo() {		
		return true;
	}

	public boolean isShowListTotal() {
		return true;
	}
	
	/**
	 * 是否单表
	 * @return boolean true:单表体，false:单表头
	 */ 
	public boolean isSingleDetail() {
		return false; //单表头
	}
}

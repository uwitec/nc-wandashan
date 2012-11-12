package nc.ui.wds.ic.invstore;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.wds.ic.invstore.CargdocVO;
import nc.vo.wds.ic.invstore.MyBillVO;
import nc.vo.wds.ic.invstore.TbSpacegoodsVO;
import nc.vo.wl.pub.WdsWlPubConst;


public class MyClientUICtrl extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
                                
        return new int[]{
        				IBillButton.Add,
        				IBillButton.Query,
        				IBillButton.Edit,
        				IBillButton.Delete,
                        IBillButton.Save,
                        IBillButton.Cancel,
                        IBillButton.Line,
                        IBillButton.Return,
                        IBillButton.Refresh,
                        IBillButton.Copy,
                        IBillButton.Print
                                                         };
  
	}
	
	public int[] getListButtonAry() {		
	    return new int[]{
	    		IBillButton.Add,
	            IBillButton.Query,
	            IBillButton.Edit,
	        	IBillButton.Delete,
	            IBillButton.Save,
	            IBillButton.Cancel,
	            IBillButton.Line,
	            IBillButton.Card,
	            IBillButton.Refresh,
	            IBillButton.Print
	           	         	        
	        };
	
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return WdsWlPubConst.INVSTORE;
	}

	public String[] getBillVoName() {
		return new String[]{
			MyBillVO.class.getName(),
			CargdocVO.class.getName(),
			TbSpacegoodsVO.class.getName()
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
		return false;
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
		return false;
	}
	
}

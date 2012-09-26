package nc.ui.wds.trans;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.trans.TransVO;
public class MyClientUICtrl extends AbstractManageController implements ISingleController{

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {		                                
                return new int[]{
                           IBillButton.Query,
                           IBillButton.Add,
                            IBillButton.Edit,
                            IBillButton.Save,
                            IBillButton.Cancel,
                            IBillButton.Delete,
                            IBillButton.Return,
                            IBillButton.Refresh,
                            IBillButton.Print
                                                         
                };
  
	}
	
	public int[] getListButtonAry() {		
        return new int[]{    IBillButton.Query,
 	           	             IBillButton.Add,
 	           	             IBillButton.Edit,
 	           	             IBillButton.Save,
 	           	             IBillButton.Cancel,
 	           	             IBillButton.Delete,
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
		return "8008010206";
	}

	public String[] getBillVoName() {
		return new String[]{
			HYBillVO.class.getName(),
			TransVO.class.getName(),
			TransVO.class.getName()
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
		return false;
	}
	
	/**
	 * �Ƿ񵥱�
	 * @return boolean true:�����壬false:����ͷ
	 */ 
	public boolean isSingleDetail() {
		return false; //����ͷ
	}
}

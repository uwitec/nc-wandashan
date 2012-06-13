package nc.ui.wds.tranprice.cardoc;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.tranprice.cardoc.CardocBVO;
import nc.vo.wds.tranprice.cardoc.CardocHVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientController extends  AbstractManageController{

	public String[] getCardBodyHideCol() {	
		return null;
	}
	public boolean isShowCardRowNo() {
		
		return true;
	}

	public boolean isShowCardTotal() {
		
		return true;
	}
	public String getBillType() {
		
		return WdsWlPubConst.TRANS_CARDOC_NODECODE;
	}

	
	public String[] getBillVoName() {
		
		return new String[]{
			HYBillVO.class.getName(),
			CardocHVO.class.getName(),			
			CardocBVO.class.getName()				
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
	
		return "pk_wds_cardoc_h";
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
		
		return false;
	}

	public boolean isShowListTotal() {
		
		return false;
	}



	public int[] getCardButtonAry() {
		return new int[]{
				IBillButton.Add,
			    IBillButton.Delete,
				IBillButton.Edit,
				IBillButton.Query, 
				
				IBillButton.Line,
			    
				IBillButton.Save, 
				IBillButton.Cancel,
				
				IBillButton.Return,
		
				IBillButton.Refresh,
				IBillButton.Print,
			};
	}
	public boolean isSingleDetail() {		
		return true;
	}
	public int[] getListButtonAry() {
		
		return new int[]{
				IBillButton.Add,
			    IBillButton.Delete,
				IBillButton.Edit,
				IBillButton.Query, 
				IBillButton.Line,
			    
				IBillButton.Save, 
				IBillButton.Cancel,
				IBillButton.Card,
				
		
				IBillButton.Refresh,
				IBillButton.Print,
			};
	}

}

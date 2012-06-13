package nc.ui.wds.tranprice.specbusi;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.tranprice.specbusi.SpecbusiVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientController implements ICardController,ISingleController {

	public String[] getCardBodyHideCol() {	
		return null;
	}
	public boolean isShowCardRowNo() {
		
		return false;
	}

	public boolean isShowCardTotal() {
		
		return true;
	}
	public String getBillType() {
		
		return WdsWlPubConst.BILLTYPE_SPEC_BUSI_DOC;
	}

	
	public String[] getBillVoName() {
		
		return new String[]{
			HYBillVO.class.getName(),
			SpecbusiVO.class.getName(),			
			SpecbusiVO.class.getName()				
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
	
		return "pk_wds_specbusi";
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
				
			    
				IBillButton.Save, 
				IBillButton.Cancel,
				
		
				IBillButton.Refresh,
				IBillButton.Print,
			};
	}
	public boolean isSingleDetail() {		
		return true;
	}

}

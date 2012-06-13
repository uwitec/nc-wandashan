package nc.ui.wds.tranprice.transcorp;


import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.vo.wds.tranprice.transcorp.ExaggLoadPricVO;
import nc.vo.wds.tranprice.transcorp.TanscorpB1VO;
import nc.vo.wds.tranprice.transcorp.TanscorpB2VO;
import nc.vo.wds.tranprice.transcorp.TanscorpHVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientController extends AbstractManageController {

	public int[] getCardButtonAry() {
		int[] buttonArray = new int[] {
				IBillButton.Add,
				IBillButton.Query, 		
				IBillButton.Delete,
				IBillButton.Edit,
				IBillButton.Save,
				IBillButton.Line,
				IBillButton.Cancel,						 
				IBillButton.Return,
				IBillButton.Brow,
				IBillButton.Refresh,
			
			
				IBillButton.Print
				
		  };
		return buttonArray;
}
	public int[] getListButtonAry() {
		int[] buttonArray = new int[] {
			
				IBillButton.Add,
				IBillButton.Query, 	
				IBillButton.Delete,
				IBillButton.Edit,							 
				IBillButton.Card,
				IBillButton.Brow,
				IBillButton.Refresh,
			
				IBillButton.Print
		};
		return buttonArray;
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public String getBillType() {
		return WdsWlPubConst.TRANS_CORP_NODECODE;
	}

	public String[] getBillVoName() {
		return new String[] {
				ExaggLoadPricVO.class.getName(),
				TanscorpHVO.class.getName(),
				TanscorpB1VO.class.getName(),
				TanscorpB2VO.class.getName()
				};
	}

	public String getBodyCondition() {
		return null;
	}

	public int getBusinessActionType() {
		return nc.ui.trade.businessaction.IBusinessActionType.BD;
	}

	public String getChildPkField() {
		return "";
	}

	public String getPkField() {
		return "";
	}

	public Boolean isEditInGoing() throws Exception {
		return false;
	}

	public boolean isExistBillStatus() {
		return false;
	}

	public boolean isLoadCardFormula() {
		return true;
	}


	public boolean isShowListRowNo() {
		return true;
	}

	public String[] getCardBodyHideCol() {
		return null;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBodyZYXKey() {

		return null;
	}

	public String getHeadZYXKey() {

		return null;
	}

	public String[] getListBodyHideCol() {

		return null;
	}

	public String[] getListHeadHideCol() {

		return null;
	}

	public boolean isShowListTotal() {
		return false;
	}

}

package nc.ui.wds.tranprice.tonkilometre;


import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.tranprice.tonkilometre.TranspriceBVO;
import nc.vo.wds.tranprice.tonkilometre.TranspriceHVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientController extends AbstractManageController {

	public int[] getCardButtonAry() {
		int[] buttonArray = new int[] {
				IBillButton.Query, 		
				IBillButton.Add,
				IBillButton.Del,
				IBillButton.Edit,
				IBillButton.Save,
				IBillButton.Line,
				IBillButton.Cancel,						 
				IBillButton.Return,
				IBillButton.Brow,
				IBillButton.Refresh,
				IBillButton.Action,
				IBillButton.Copy
		       		
		};
		return buttonArray;
}
	public int[] getListButtonAry() {
		int[] buttonArray = new int[] {
				IBillButton.Query, 	
				IBillButton.Add,
				IBillButton.Del,
				IBillButton.Edit,							 
				IBillButton.Card,
				IBillButton.Brow,
				IBillButton.Refresh,
				IBillButton.Action,
				IBillButton.Copy
		};
		return buttonArray;
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public String getBillType() {
		return WdsWlPubConst.WDSI;
	}

	public String[] getBillVoName() {
		return new String[] {
				HYBillVO.class.getName(),
				TranspriceHVO.class.getName(),
				TranspriceBVO.class.getName()
				};
	}
	
	
	public String getBodyCondition() {
		return null;
	}

	public int getBusinessActionType() {
		return nc.ui.trade.businessaction.IBusinessActionType.PLATFORM;
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
		return true;
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
		return true;
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
		return true;
	}

}

package nc.ui.dm.sb;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;

public class ClientController extends AbstractManageController {

	public int[] getCardButtonAry() {
		int[] buttonArray = new int[] {
				IBillButton.Add,
				IBillButton.Del,
				IBillButton.Edit,
				IBillButton.Save,
				IBillButton.Line,
				IBillButton.Cancel,
				IBillButton.Query, 				 
				IBillButton.Return,
				IBillButton.Refresh};
		return buttonArray;
	}

	public int[] getListButtonAry() {
		int[] buttonArray = new int[] {
				IBillButton.Add,
				IBillButton.Del,
				IBillButton.Edit,
				IBillButton.Query, 				 
				IBillButton.Card,
				IBillButton.Refresh
		}
				;
		return buttonArray;
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public String getBillType() {
		return "wds_sbdoc";
	}

	public String[] getBillVoName() {
		return new String[] {
				HYBillVO.class.getName(),
				SbDocVO.class.getName(),
				null
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
		return true;
	}

}

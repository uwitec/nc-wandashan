package nc.ui.dm.so.order;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.vo.dm.order.SendorderBVO;
import nc.vo.dm.order.SendorderVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientController extends AbstractManageController {

	public int[] getCardButtonAry() {
		int[] buttonArray = new int[] {
				IBillButton.Add,
				IBillButton.Delete,
				IBillButton.Edit,
				IBillButton.Save,
				IBillButton.Line,
				IBillButton.Cancel,
				IBillButton.Query, 				 
				IBillButton.Return,
				IBillButton.Brow,
				IBillButton.Refresh,
				IBillButton.Audit,
				IBillButton.CancelAudit};
		return buttonArray;
	}

	public int[] getListButtonAry() {
		int[] buttonArray = new int[] {
				IBillButton.Add,
				IBillButton.Delete,
				IBillButton.Edit,
				IBillButton.Query, 				 
				IBillButton.Card,
				IBillButton.Brow,
				IBillButton.Refresh,
				IBillButton.Audit,
				IBillButton.CancelAudit
		}
				;
		return buttonArray;
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public String getBillType() {
		return WdsWlPubConst.WDS5;
	}

	public String[] getBillVoName() {
		return new String[] {
				HYBillVO.class.getName(),
				SendorderVO.class.getName(),
				SendorderBVO.class.getName()
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

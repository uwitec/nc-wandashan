package nc.ui.dm.db.order;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.vo.dm.order.SendorderBVO;
import nc.vo.dm.order.SendorderVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wl.pub.ButtonCommon;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.pub.consts.ZmpubBtnConst;

public class ClientController extends AbstractManageController {

	public int[] getCardButtonAry() {
		int[] buttonArray = new int[] {
			//	IBillButton.Add,
				IBillButton.Del,
				IBillButton.Edit,
				IBillButton.Line,
				IBillButton.Save,
				IBillButton.Cancel,
				IBillButton.Query, 				 
				IBillButton.Return,
				IBillButton.Brow,
				IBillButton.Refresh,
				IBillButton.Action,
				ZmpubBtnConst.LINKQUERY,
				IBillButton.Print,
			//	ButtonCommon.TRAN_COL,
				ButtonCommon.LOCK,
				ButtonCommon.UNLOCK,
				ButtonCommon.btnopen,
				ButtonCommon.btnclose,
				};
		return buttonArray;
	}

	public int[] getListButtonAry() {
		int[] buttonArray = new int[] {
			//	IBillButton.Add,
				IBillButton.Del,
				IBillButton.Edit,
				IBillButton.Line,
				IBillButton.Query, 				 
				IBillButton.Card,
				IBillButton.Brow,
				IBillButton.Refresh,
				IBillButton.Action,
				ZmpubBtnConst.LINKQUERY,
				IBillButton.Print,
			//	ButtonCommon.TRAN_COL,
				ButtonCommon.LOCK,
				ButtonCommon.UNLOCK,
				ButtonCommon.btnopen,
				ButtonCommon.btnclose,
		}
				;
		return buttonArray;
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public String getBillType() {
		return WdsWlPubConst.WDSG;
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

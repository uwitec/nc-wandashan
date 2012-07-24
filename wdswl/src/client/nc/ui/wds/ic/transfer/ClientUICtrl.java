package nc.ui.wds.ic.transfer;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.wds.transfer.MyBillVO;
import nc.vo.wds.transfer.TransferBVO;
import nc.vo.wds.transfer.TransferVO;
import nc.vo.wl.pub.ButtonCommon;

/**
 * 
 */

public class ClientUICtrl extends AbstractManageController {

	private String billtype = null;

	public ClientUICtrl(String billtype) {
		this.billtype = billtype;
	}

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {

		return new int[] { IBillButton.Add, IBillButton.Edit, IBillButton.Line,
				IBillButton.Save, IBillButton.Query, IBillButton.Del,
				IBillButton.Cancel,
				nc.ui.wds.w8004040204.ssButtun.ISsButtun.fzgn,
				IBillButton.Action, IBillButton.Return, IBillButton.Refresh,
				ButtonCommon.joinup,ButtonCommon.LOCK,ButtonCommon.UNLOCK

		};

	}

	public int[] getListButtonAry() {
		return new int[] { IBillButton.Add, IBillButton.Edit,
				IBillButton.Query, IBillButton.Del, IBillButton.Action,
				IBillButton.Card, IBillButton.Refresh, ButtonCommon.joinup,
				ButtonCommon.LOCK,ButtonCommon.UNLOCK };

	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return true;
	}

	public String getBillType() {
		return billtype;
	}

	public String[] getBillVoName() {
		return new String[] { MyBillVO.class.getName(),
				TransferVO.class.getName(), TransferBVO.class.getName() };
	}

	public String getBodyCondition() {
		return " isnull(dr,0)=0 ";
	}

	public String getBodyZYXKey() {
		return null;
	}

	public int getBusinessActionType() {
		return IBusinessActionType.PLATFORM;
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
		return true;
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

}

package nc.ui.wds.ic.cargtray;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.wds.ic.cargtray.BdCargdocTrayVO;
import nc.vo.wds.ic.cargtray.CargdocVO;
import nc.vo.wds.ic.cargtray.MyBillVO;
import nc.vo.wl.pub.WdsWlPubConst;



public class MyClientUICtrl extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {

		return new int[] { 
				IBillButton.Add,
				IBillButton.Edit, IBillButton.Line, IBillButton.Save,
				IBillButton.Query, 
				IBillButton.Delete,
				IBillButton.Cancel, IBillButton.Return,
				IBillButton.Refresh,IBillButton.Print };

	}

	public int[] getListButtonAry() {
		return new int[] { 
				IBillButton.Add,
				IBillButton.Edit, IBillButton.Line, IBillButton.Save,
				IBillButton.Query, IBillButton.Delete,
				IBillButton.Cancel, IBillButton.Card,
				IBillButton.Refresh,IBillButton.Print

		};

	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return true;
	}

	public String getBillType() {
		return WdsWlPubConst.BILLTYPE_CARG_TARY;
	}

	public String[] getBillVoName() {
		return new String[] { 
				MyBillVO.class.getName(),
				CargdocVO.class.getName(), 
				BdCargdocTrayVO.class.getName() };
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
		return true;
	}

}

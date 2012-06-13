package nc.ui.wds.ie.storepersons;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.wds.ie.storepersons.MyBillVO;
import nc.vo.wds.ie.storepersons.TbStockstaffVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 */

public class MyClientUICtrl extends AbstractManageController implements
		ISingleController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {

		return new int[] { IBillButton.Query, IBillButton.Add,
				IBillButton.Edit, IBillButton.Save, IBillButton.Cancel,
				IBillButton.Delete, IBillButton.Return, IBillButton.Refresh,IBillButton.Print };

	}

	public int[] getListButtonAry() {
		return new int[] { IBillButton.Query, IBillButton.Add,
				IBillButton.Edit, IBillButton.Save, IBillButton.Cancel,
				IBillButton.Delete, IBillButton.Card, IBillButton.Refresh,IBillButton.Print

		};

	}

	public boolean isShowCardRowNo() {
		return false;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return WdsWlPubConst.BILLTYPE_IE_STOR_PERSONS;
	}

	public String[] getBillVoName() {
		return new String[] { MyBillVO.class.getName(),
				TbStockstaffVO.class.getName(), TbStockstaffVO.class.getName() };
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
		return false;
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

	/**
	 * 是否单表
	 * @return boolean true:单表体，false:单表头
	 */
	public boolean isSingleDetail() {
		return false; //单表头
	}
}

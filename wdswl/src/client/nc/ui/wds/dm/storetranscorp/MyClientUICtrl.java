package nc.ui.wds.dm.storetranscorp;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.dm.storetranscorp.BdStordocVO;
import nc.vo.wds.dm.storetranscorp.StortranscorpBVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * Create on 2006-4-6 16:00:51
 * 
 * @author authorName
 * @version tempProject version
 */

public class MyClientUICtrl extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {

		return new int[] { IBillButton.Query, IBillButton.Edit,
				IBillButton.Line, IBillButton.Save, IBillButton.Cancel,
				IBillButton.Return,IBillButton.Brow, IBillButton.Refresh,IBillButton.Print };

	}

	public int[] getListButtonAry() {
		return new int[] { IBillButton.Query, IBillButton.Edit,
				IBillButton.Line, IBillButton.Save, IBillButton.Cancel,
				IBillButton.Card, IBillButton.Brow,IBillButton.Refresh,IBillButton.Print

		};

	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return WdsWlPubConst.DM_STORE_TRANSCORP_NODECODE;
	}

	public String[] getBillVoName() {
		return new String[] { HYBillVO.class.getName(),
				BdStordocVO.class.getName(), StortranscorpBVO.class.getName() };
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
		return false;
	}

}

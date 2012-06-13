package nc.ui.wds.dm.storebing;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.wds.dm.storebing.BdStordocVO;
import nc.vo.wds.dm.storebing.MyBillVO;
import nc.vo.wds.dm.storebing.TbStorcubasdocVO;

/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * 
 * <p>
 * �ڴ˴���Ӵ����������Ϣ
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

		return new int[] {  IBillButton.Add, IBillButton.Edit,
				IBillButton.Copy,
				IBillButton.Query,
				IBillButton.Line, IBillButton.Save, IBillButton.Cancel,IBillButton.Delete,
				IBillButton.Return,IBillButton.Brow, IBillButton.Refresh };

	}

	public int[] getListButtonAry() {
		return new int[] { IBillButton.Add,IBillButton.Edit,
				IBillButton.Copy,IBillButton.Query,
				IBillButton.Line, IBillButton.Save, IBillButton.Cancel,IBillButton.Delete,
				IBillButton.Card, IBillButton.Brow,IBillButton.Refresh

		};

	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "8006080206";
	}

	public String[] getBillVoName() {
		return new String[] { MyBillVO.class.getName(),
				BdStordocVO.class.getName(), TbStorcubasdocVO.class.getName() };
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

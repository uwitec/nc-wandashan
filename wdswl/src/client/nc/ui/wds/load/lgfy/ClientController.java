package nc.ui.wds.load.lgfy;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.load.account.ExaggLoadPricVO;
import nc.vo.wds.load.account.LoadpriceB1VO;
import nc.vo.wds.load.account.LoadpriceB2VO;
import nc.vo.wds.load.account.LoadpriceHVO;
import nc.vo.wl.pub.ButtonCommon;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientController extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getCardButtonAry() {
		int[] buttonArray = new int[] { IBillButton.Add, IBillButton.Edit,
				IBillButton.Save, IBillButton.Line, IBillButton.Cancel,
				IBillButton.Del, IBillButton.Query, IBillButton.Return,
				IBillButton.Brow, IBillButton.Refresh, IBillButton.Action,
				ButtonCommon.WDSVYGXX, IBillButton.Print };
		return buttonArray;
	}

	public int[] getListButtonAry() {
		int[] buttonArray = new int[] { IBillButton.Add, IBillButton.Edit,
				IBillButton.Del, IBillButton.Query, IBillButton.Card,
				IBillButton.Brow, IBillButton.Refresh, IBillButton.Action,
				IBillButton.Print };
		return buttonArray;
	}

	public boolean isShowCardRowNo() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isShowCardTotal() {
		// TODO Auto-generated method stub
		return true;
	}

	public String getBillType() {
		return WdsWlPubConst.WDSV;
	}

	public String[] getBillVoName() {
		return new String[] { HYBillVO.class.getName(),
				LoadpriceHVO.class.getName(), LoadpriceB2VO.class.getName() };
	}

	public String getBodyCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getBodyZYXKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getBusinessActionType() {
		return nc.ui.trade.businessaction.IBusinessActionType.PLATFORM;
	}

	public String getChildPkField() {
		return "pk_loadprice_b2";
	}

	public String getHeadZYXKey() {
		return null;
	}

	public String getPkField() {
		return "pk_loadprice";
	}

	public Boolean isEditInGoing() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isExistBillStatus() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isLoadCardFormula() {
		// TODO Auto-generated method stub
		return true;
	}

	public String[] getListBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getListHeadHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isShowListRowNo() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isShowListTotal() {
		// TODO Auto-generated method stub
		return true;
	}

}

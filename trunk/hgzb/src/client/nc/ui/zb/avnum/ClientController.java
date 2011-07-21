package nc.ui.zb.avnum;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zb.avnum.AvNumBodyVO;
import nc.vo.zb.avnum.AvNumHeadVO;
import nc.vo.zb.pub.ZbPuBtnConst;
import nc.vo.zb.pub.ZbPubConst;

public class ClientController extends AbstractManageController {
	
	
	public int[] getCardButtonAry() {
		int[] buttonArray = new int[] { 
				IBillButton.Add,
				ZbPuBtnConst.Editor, 
				IBillButton.Save,
				IBillButton.Cancel, 
//				IBillButton.Line,
				IBillButton.Query, 
				IBillButton.Del, 
				IBillButton.Print, 
				IBillButton.Return, 
				IBillButton.Brow,
				IBillButton.Commit,
				IBillButton.Audit,
				IBillButton.Refresh,
				ZbPuBtnConst.ASSQUERY,
				IBillButton.Print
				};
		return buttonArray;
	}
	
	public int[] getListButtonAry() {
		int[] buttonArray = new int[] { 
				IBillButton.Add, 
				ZbPuBtnConst.Editor, 
				IBillButton.Query,
				IBillButton.Del, 
				//IBillButton.Line, 
				IBillButton.Card, 
				IBillButton.Brow, 
				IBillButton.Commit, 
				IBillButton.Audit,
				IBillButton.Refresh,
				ZbPuBtnConst.ASSQUERY
				};
		return buttonArray;
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public String getBillType() {
		return ZbPubConst.ZB_AVNUM_BILLTYPE;
	}

	public String[] getBillVoName() {
		return new String[] { 
				HYBillVO.class.getName(),
				AvNumHeadVO.class.getName(),
				AvNumBodyVO.class.getName()
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
		return "cavnumid";
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

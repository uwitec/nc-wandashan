package nc.ui.zb.comments;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zb.comments.BiEvaluationBodyVO;
import nc.vo.zb.comments.BidEvaluationHeaderVO;
import nc.vo.zb.pub.ZbPuBtnConst;
import nc.vo.zb.pub.ZbPubConst;

public class ClientController extends AbstractManageController {
	
	
	public int[] getCardButtonAry() {
		int[] buttonArray = new int[] { 
				//IBillButton.Add,
				ZbPuBtnConst.Editor, 
				IBillButton.Save,
				ZbPuBtnConst.FLBTN,
				IBillButton.Cancel, 
				//IBillButton.Line,
				IBillButton.Query, 
				IBillButton.Del, 
				IBillButton.Return, 
				IBillButton.Brow,
				IBillButton.Action, 
				IBillButton.Audit,
				IBillButton.Refresh,
				ZbPuBtnConst.ASSQUERY,
				IBillButton.Print
				};
		return buttonArray;
	}
	
	public int[] getListButtonAry() {
		int[] buttonArray = new int[] { 
				//IBillButton.Add, 
				ZbPuBtnConst.Editor,
				IBillButton.Query,
				IBillButton.Del, 
				//IBillButton.Line, 
				IBillButton.Card, 
				IBillButton.Brow, 
				IBillButton.Action, 
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
		return ZbPubConst.ZB_EVALUATION_BILLTYPE;
	}

	public String[] getBillVoName() {
		return new String[] { 
				HYBillVO.class.getName(),
				BidEvaluationHeaderVO.class.getName(),
				BiEvaluationBodyVO.class.getName()
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
		return "cevaluationid";
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

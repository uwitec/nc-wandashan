package nc.ui.zb.price.bill;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.zb.price.bill.SubmitPriceBillVO;
import nc.vo.zb.price.bill.SubmitPriceBodyVO;
import nc.vo.zb.price.bill.SubmitPriceHeaderVO;
import nc.vo.zb.pub.ZbPuBtnConst;
import nc.vo.zb.pub.ZbPubConst;


public class ClientController extends AbstractManageController {

	/** 单据VO信息 */
	private String[] m_billVoNames = new String[] { 
			SubmitPriceBillVO.class.getName(),
			SubmitPriceHeaderVO.class.getName(),
			SubmitPriceBodyVO.class.getName() };

	public String[] getListBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		return new int[] { 
				IBillButton.Add,
				ZbPuBtnConst.Editor,
				IBillButton.Save,
				IBillButton.Cancel,
				IBillButton.Query,
//				IBillButton.Line,
				IBillButton.Del,
				IBillButton.Brow,
				IBillButton.Refresh,
				IBillButton.Return,
				IBillButton.Action,
				IBillButton.Audit,
				ZbPuBtnConst.ASSQUERY,
				IBillButton.Print,
				ZbPuBtnConst.REVISED

		};
	}
	
	public int[] getListButtonAry() {
		return new int[] { 
				IBillButton.Add,
				ZbPuBtnConst.Editor, 
				IBillButton.Query,
				IBillButton.Del,
				IBillButton.Brow,
				IBillButton.Refresh,
				IBillButton.Card,
				IBillButton.Action,
				IBillButton.Audit,
				ZbPuBtnConst.ASSQUERY
		};
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

	public String getBillType() {
		return ZbPubConst.ZB_SUBMIT_BILL;
	}

	public String[] getBillVoName() {
		return m_billVoNames;
	}

	public String getBodyCondition() {
		return " isnull(dr,0) = 0 ";
	}

	public String getBodyZYXKey() {
		return null;
	}
	/**
	 * 动作脚本
	 */
	public int getBusinessActionType() {
		return IBusinessActionType.PLATFORM;
	}

	public String getChildPkField() {
		return "";
	}

	public String getHeadZYXKey() {
		return null;
	}

	public String getPkField() {
		return "csubmitbillid";
	}

	public Boolean isEditInGoing() throws Exception {
		return false;
	}
/**
 * 是否存在单据状态
 */
	public boolean isExistBillStatus() {
		return true;
	}

	public boolean isLoadCardFormula() {
		return true;
	}

	public String[] getCardBodyHideCol() {
		return null;
	}

	public boolean isShowCardRowNo() {
		return false;
	}

	public boolean isShowCardTotal() {
		return true;
	}
	
}

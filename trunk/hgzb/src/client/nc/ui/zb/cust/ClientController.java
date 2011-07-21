package nc.ui.zb.cust;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zb.cust.CubasdocHgVO;
import nc.vo.zb.pub.ZbPuBtnConst;
import nc.vo.zb.pub.ZbPubConst;


public class ClientController extends AbstractManageController implements ISingleController{

	//不用实现implements ISingleController这个接口

	/** 单据VO信息 */
	private String[] m_billVoNames = new String[] { 
			HYBillVO.class.getName(),
			CubasdocHgVO.class.getName(),
			CubasdocHgVO.class.getName() };

	public String[] getListBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		return new int[] {
				IBillButton.Add,
				IBillButton.Save,
				ZbPuBtnConst.Editor, 
				IBillButton.Cancel,
				IBillButton.Query,
				IBillButton.Action,
				IBillButton.Audit,
				IBillButton.Del,
				IBillButton.Brow,
				IBillButton.Refresh,
				IBillButton.Return,
				ZbPuBtnConst.MODIFY,
//				ZbPuBtnConst.ASSQUERY,
				IBillButton.Print
		};
	}
	
	public int[] getListButtonAry() {
		return new int[] { 
				IBillButton.Add,
				ZbPuBtnConst.Editor, 
				IBillButton.Query,
				IBillButton.Action,
				IBillButton.Audit,
				IBillButton.Brow,
				IBillButton.Refresh,
				IBillButton.Card,
				ZbPuBtnConst.MODIFY,
//				ZbPuBtnConst.ASSQUERY
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
		return ZbPubConst.ZB_CUSTBAS_BILLTYPE;
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
		return "ccubasdochgid";
	}

	public Boolean isEditInGoing() throws Exception {
		return null;
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
	/**
	 * 单表头
	 */
	public boolean isSingleDetail() {
		return false;
	}

	public String[] getCardBodyHideCol() {
		return null;
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return false;
	}
	
}

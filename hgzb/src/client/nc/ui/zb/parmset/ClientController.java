package nc.ui.zb.parmset;


import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zb.parmset.ParamSetVO;
import nc.vo.zb.pub.ZbPubConst;


public class ClientController  implements ICardController,ISingleController{

	/** 单据类型 */
	private String m_sBillType =ZbPubConst.ZB_PARAMSET_BILLTYPE;
	/** 单据VO信息 */
	private String[] m_billVoNames = new String[] { 
			HYBillVO.class.getName(),
			ParamSetVO.class.getName(),
			ParamSetVO.class.getName() };

	public String[] getListBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		return new int[] { 
//				IBillButton.Add,
				IBillButton.Edit,
				IBillButton.Save,
				IBillButton.Cancel
		};
	}
	
	public int[] getListButtonAry() {
		return new int[] { 
//				IBillButton.Add,
				IBillButton.Edit,
				IBillButton.Cancel
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
		return m_sBillType;
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
		return IBusinessActionType.BD;
	}

	public String getChildPkField() {
		return "";
	}

	public String getHeadZYXKey() {
		return null;
	}

	public String getPkField() {
		return "";
	}

	public Boolean isEditInGoing() throws Exception {
		return null;
	}
/**
 * 是否存在单据状态
 */
	public boolean isExistBillStatus() {
		return false;
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
		return false;
	}

	public boolean isShowCardTotal() {
		return false;
	}
	
}

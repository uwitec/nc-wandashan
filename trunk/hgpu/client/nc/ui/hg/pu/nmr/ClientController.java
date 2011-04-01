package nc.ui.hg.pu.nmr;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.hg.pu.nmr.NewMaterialsVO;
import nc.vo.hg.pu.pub.HgPuBtnConst;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.trade.pub.HYBillVO;


public class ClientController extends AbstractManageController implements ISingleController{

	//不用实现implements ISingleController这个接口

	/** 单据VO信息 */
	private String[] m_billVoNames = new String[] { 
			HYBillVO.class.getName(),
			NewMaterialsVO.class.getName(),
			NewMaterialsVO.class.getName() };

	public String[] getListBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		return new int[] {
				IBillButton.Add,
				HgPuBtnConst.Editor,
				IBillButton.Save,
				HgPuBtnConst.CODING,
				HgPuBtnConst.OK,
				IBillButton.Cancel,
				IBillButton.Query,
				IBillButton.Action,
				IBillButton.Audit,
				IBillButton.Del,
				IBillButton.Brow,
				IBillButton.Refresh,
				IBillButton.Return,
				IBillButton.Print,
				IBillButton.ApproveInfo
		};
	}
	
	public int[] getListButtonAry() {
		return new int[] { 
				IBillButton.Add,
				HgPuBtnConst.Editor,
				HgPuBtnConst.CODING,
				IBillButton.Cancel,
				IBillButton.Query,
				IBillButton.Action,
				IBillButton.Audit,
				IBillButton.Brow,
				IBillButton.Refresh,
				IBillButton.Card,
				IBillButton.Print,
				IBillButton.ApproveInfo
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
		return HgPubConst.NEW_MATERIALS_BILLTYPE;
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
		return "";
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

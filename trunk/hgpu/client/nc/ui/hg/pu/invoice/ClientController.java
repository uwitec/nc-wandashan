package nc.ui.hg.pu.invoice;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.hg.pu.invoice.BzbVO;
import nc.vo.hg.pu.invoice.BzhVO;
import nc.vo.hg.pu.pub.HgPuBtnConst;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.trade.pub.HYBillVO;


public class ClientController extends AbstractManageController {
	//����ʵ��implements ISingleController����ӿ�

	/** ����VO��Ϣ */
	private String[] m_billVoNames = new String[] { 
			HYBillVO.class.getName(),
			BzhVO.class.getName(),
			BzbVO.class.getName() };

	public String[] getListBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		return new int[] {
				IBillButton.Refbill,
//				HgPuBtnConst.Editor,
				IBillButton.Save,
				IBillButton.Cancel,
//				IBillButton.Line,
				IBillButton.Query,
//				IBillButton.Audit,
				IBillButton.Del,
				IBillButton.Brow,
				IBillButton.Refresh,
				IBillButton.Return,
				IBillButton.Print
//				IBillButton.ApproveInfo
		};
	}
	
	public int[] getListButtonAry() {
		return new int[] { 
				IBillButton.Refbill,
//				HgPuBtnConst.Editor,
				IBillButton.Cancel,
//				IBillButton.Line,
				IBillButton.Query,
				IBillButton.Del,
//				IBillButton.Action,
				IBillButton.Brow,
				IBillButton.Refresh,
				IBillButton.Card,
				IBillButton.Print
//				IBillButton.ApproveInfo
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
		return HgPubConst.PLAN_BAOZHANG_BILLTYPE;
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
 * �Ƿ���ڵ���״̬
 */
	public boolean isExistBillStatus() {
		return true;
	}

	public boolean isLoadCardFormula() {
		return true;
	}
//	/**
//	 * ����ͷ
//	 */
//	public boolean isSingleDetail() {
//		return false;
//	}

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
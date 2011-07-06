package nc.ui.wds.invcl;

import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.treecard.ITreeCardController;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.invcl.WdsInvClVO;

public class InvClCtrl implements ITreeCardController,ISingleController {

	public boolean isAutoManageTree() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isChildTree() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isTableTree() {
		// TODO Auto-generated method stub
		return true;
	}

	public String[] getCardBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getCardButtonAry() {
		// TODO Auto-generated method stub
		return new int[]{
				IBillButton.Add,
				IBillButton.Edit,
				IBillButton.Save,
				IBillButton.Cancel,
				IBillButton.Delete,
				IBillButton.Refresh
		};
	}

	public boolean isShowCardRowNo() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isShowCardTotal() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getBillType() {
		// TODO Auto-generated method stub
		return WdsInvClVO.bill_type;
	}

	public String[] getBillVoName() {
		// TODO Auto-generated method stub
		return new String[] { HYBillVO.class.getName(),
				WdsInvClVO.class.getName(), WdsInvClVO.class.getName() };
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
		// TODO Auto-generated method stub
		return IBusinessActionType.BD;
	}

	public String getChildPkField() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getHeadZYXKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPkField() {
		// TODO Auto-generated method stub
		return "pk_invcl";
	}

	public Boolean isEditInGoing() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isExistBillStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isLoadCardFormula() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isSingleDetail() {
		// TODO Auto-generated method stub
		return false;
	}

}

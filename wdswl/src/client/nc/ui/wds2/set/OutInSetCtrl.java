package nc.ui.wds2.set;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds2.set.OutInSetVO;

public class OutInSetCtrl implements ICardController,ISingleController {

	public OutInSetCtrl() {
		super();
	}


	public int[] getCardButtonAry() {
			return new int[]{
					IBillButton.Add,
					IBillButton.Edit,
					IBillButton.Save,
					IBillButton.Cancel,
					IBillButton.Delete,
					IBillButton.Query,
					IBillButton.Refresh
			};
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "80021002";
	}

	public String[] getBillVoName() {
		return new String[]{
				HYBillVO.class.getName(),
				OutInSetVO.class.getName(),
				OutInSetVO.class.getName()	
		};
	}

	public String getBodyCondition() {
		return "";
	}

	public String getBodyZYXKey() {
		return null;
	}

	public int getBusinessActionType() {
		return nc.ui.trade.businessaction.IBusinessActionType.BD;
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
//	µ¥±íÌå
	public boolean isSingleDetail() {
		return true;
	}

	public String[] getCardBodyHideCol() {
		return null;
	}

}

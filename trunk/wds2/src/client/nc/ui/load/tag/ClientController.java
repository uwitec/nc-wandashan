package nc.ui.load.tag;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.vo.load.tag.TagDocBVO;
import nc.vo.load.tag.TagDocVO;

public class ClientController extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getCardButtonAry() {
		int[] buttonArray = new int[] {
				IBillButton.Del,
				IBillButton.Edit,
				IBillButton.Save,
				IBillButton.Cancel,
				IBillButton.Query, 				 
				IBillButton.Return,
				IBillButton.Brow,
				IBillButton.Refresh,
				IBillButton.Audit,
				IBillButton.CancelAudit};
		return buttonArray;
	}

	public int[] getListButtonAry() {
		int[] buttonArray = new int[] {
				IBillButton.Del,
				IBillButton.Edit,
				IBillButton.Query, 				 
				IBillButton.Card,
				IBillButton.Brow,
				IBillButton.Refresh,
				IBillButton.Audit,
				IBillButton.CancelAudit
		}
				;
		return buttonArray;
	}

	public boolean isShowCardRowNo() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isShowCardTotal() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getBillType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getBillVoName() {
		return new String[]{
			TagDocVO.class.getName(),
			TagDocBVO.class.getName()
		};
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
		// TODO Auto-generated method stub
		return "pk_tagbid";
	}

	public String getHeadZYXKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPkField() {
		// TODO Auto-generated method stub
		return "pk_taghid";
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
		return false;
	}

}

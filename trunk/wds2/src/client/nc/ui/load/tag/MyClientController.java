package nc.ui.load.tag;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.vo.load.tag.TagDocBVO;
import nc.vo.load.tag.TagDocVO;
import nc.vo.trade.pub.HYBillVO;

public class MyClientController extends AbstractManageController{

	public String[] getCardBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getCardButtonAry() {
		return new int[]{
				IBillButton.Add,
				IBillButton.Line,
				IBillButton.Save,
				IBillButton.Delete,
				IBillButton.Cancel,
				IBillButton.Edit,
				IBillButton.Query,
				IBillButton.Refresh,
				IBillButton.Return
				
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
		return "wds_tagdoc";
	}

	public String[] getBillVoName() {
		return new String[]{
				HYBillVO.class.getName(),
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
		// TODO Auto-generated method stub
		return 0;
	}

	public String getChildPkField() {
		// TODO Auto-generated method stub
		return "";
	}

	public String getHeadZYXKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPkField() {
		// TODO Auto-generated method stub
		return "";
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
		return false;
	}

	public String[] getListBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getListButtonAry() {
		return new int[]{
				IBillButton.Add,
				IBillButton.Delete,
				IBillButton.Edit,
				IBillButton.Query,
				IBillButton.Save,
				IBillButton.Refresh,
				IBillButton.Card
		};
	}

	public String[] getListHeadHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isShowListRowNo() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isShowListTotal() {
		// TODO Auto-generated method stub
		return false;
	}

}

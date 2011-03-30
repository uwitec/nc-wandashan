package nc.ui.dm.sb;


/*
 * 特殊业务档案
 */
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;

public class ClientController extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getCardButtonAry() {
		return new int[]{
				IBillButton.Add,
				IBillButton.Line,
				IBillButton.Save,
				IBillButton.Query,
				IBillButton.Delete,
				IBillButton.Cancel,
				IBillButton.Edit,
				IBillButton.Refresh,
				IBillButton.Return
				
			};
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
		return "wds_sbdoc";
	}

	public String[] getBillVoName() {
		return new String[]{
				HYBillVO.class.getName(),
				SbDocVO.class.getName()
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
		return nc.ui.trade.businessaction.IBusinessActionType.BD;
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
		return true;
	}

	public String[] getListBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getListButtonAry() {
		return new int[]{
				IBillButton.Add,
				IBillButton.Delete,
				IBillButton.Query,
				IBillButton.Edit,
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
		return true;
	}

	public boolean isShowListTotal() {
		// TODO Auto-generated method stub
		return false;
	}


}

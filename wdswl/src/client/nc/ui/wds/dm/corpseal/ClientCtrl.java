package nc.ui.wds.dm.corpseal;

import nc.ui.trade.bill.IListController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.treecard.ITreeCardController;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.dm.corpseal.CorpsealVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientCtrl implements ITreeCardController, IListController,
		ISingleController {

	public String getBillType() {
		return WdsWlPubConst.DM_CUST_CORPSEAL;
	}
	
	public int[] getCardButtonAry() {

		int[] cardButtonAry = null;
		cardButtonAry = new int[] { 
			IBillButton.Add, 
			IBillButton.Save,
			IBillButton.Cancel, 
			IBillButton.Edit, 
			IBillButton.Delete,
			IBillButton.Refresh, 
			IBillButton.Return 
		};

		return cardButtonAry;
	}
	public int[] getListButtonAry() {
		// TODO Auto-generated method stub
		return new int[]{
				IBillButton.Card
		};
	}
	
	public String[] getBillVoName() {
		return new String[] { 
			HYBillVO.class.getName(),
			CorpsealVO.class.getName(), 
			CorpsealVO.class.getName() 
		};
	}
	
	public String getPkField() {
		return "pk_wds_corpseal";
	}

	public boolean isAutoManageTree() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isChildTree() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isTableTree() {
		// TODO Auto-generated method stub
		return false;
	}

	public String[] getCardBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isShowCardRowNo() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isShowCardTotal() {
		// TODO Auto-generated method stub
		return false;
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

	public boolean isSingleDetail() {
		// TODO Auto-generated method stub
		return false;
	}
}

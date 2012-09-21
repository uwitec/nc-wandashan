package nc.ui.deal.dataset;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.deal.dataset.PlanSetVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.invbasdoc.InvbasdocVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class MyClientController extends AbstractManageController implements ISingleController {

	public String[] getCardBodyHideCol() {
		
		return null;
	}

	public boolean isShowCardRowNo() {
		
		return true;
	}

	public boolean isShowCardTotal() {
		
		return true;
	}

	public String getBillType() {
		
		return WdsWlPubConst.BILLTYPE_PlAN_DATESET;
	}

	public String[] getBillVoName() {
		
		return new String[]{
			HYBillVO.class.getName(),
			PlanSetVO.class.getName(),			
			PlanSetVO.class.getName()				
		};
	}

	public String getBodyCondition() {
		
		return null;
	}

	public String getBodyZYXKey() {
		
		return null;
	}

	public int getBusinessActionType() {
		
		return IBusinessActionType.BD;
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

	public boolean isSingleDetail() {
		
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
			IBillButton.Query,
			IBillButton.Edit,
		 
			IBillButton.Save,
			IBillButton.Cancel,
			IBillButton.Card,
	//		IBillButton.Print
		};
	}

	public String[] getListHeadHideCol() {
		return null;
	}

	public boolean isShowListRowNo() {
		return false;
	}

	public boolean isShowListTotal() {
		return false;
	}



	public int[] getCardButtonAry() {
		return new int[]{
				IBillButton.Add,
				IBillButton.Delete,
				IBillButton.Query,
				IBillButton.Edit,
			
				IBillButton.Save,
				IBillButton.Cancel,
				IBillButton.Return,
		//		IBillButton.Print
			};
	}

}

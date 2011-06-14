package nc.ui.wds.dm.sendinvdoc;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.dm.sendinvdoc.SendinvdocVO;
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
		
		return WdsWlPubConst.DM_PLAN_BASDOC_NODECODE;
	}

	public String[] getBillVoName() {
		
		return new String[]{
			HYBillVO.class.getName(),
			SendinvdocVO.class.getName(),			
			SendinvdocVO.class.getName()				
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
	
		return "pk_wds_sendinvdoc";
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
			IBillButton.Print
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



	public int[] getCardButtonAry() {
		return new int[]{
				IBillButton.Add,
				IBillButton.Delete,
				IBillButton.Query,
				IBillButton.Edit,
			
				IBillButton.Save,
				IBillButton.Cancel,
				IBillButton.Return,
				IBillButton.Print
			};
	}

}

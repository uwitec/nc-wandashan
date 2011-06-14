package nc.ui.wds.load.teamdoc;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.load.teamdoc.TeamdocBVO;
import nc.vo.wds.load.teamdoc.TeamdocHVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class MyClientController extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		
		return null;
	}

	

	public boolean isShowCardRowNo() {
		
		return true;
	}

	public boolean isShowCardTotal() {
		
		return false;
	}

	public String getBillType() {
		
		return WdsWlPubConst.LOAD_TEAM_DOC;
	}

	public String[] getBillVoName() {
		
		return new String[]{
			HYBillVO.class.getName(),
			TeamdocHVO.class.getName(),			
			TeamdocBVO.class.getName()				
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
	
		return "pk_wds_teamdoc_h";
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


	public String[] getListBodyHideCol() {
		
		return null;
	}

	public int[] getListButtonAry() {
		

		return new int[]{
			IBillButton.Add,
			IBillButton.Delete,
			IBillButton.Query,
			IBillButton.Edit,
		    IBillButton.Line ,
			IBillButton.Save,
			IBillButton.Cancel,
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



	public int[] getCardButtonAry() {
		return new int[]{
				IBillButton.Add,
				IBillButton.Delete,
				IBillButton.Query,
				IBillButton.Edit,
				 IBillButton.Line ,
				IBillButton.Save,
				IBillButton.Cancel,
				IBillButton.Return,
				IBillButton.Print
			};
	}

}

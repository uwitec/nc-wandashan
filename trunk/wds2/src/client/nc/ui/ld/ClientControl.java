package nc.ui.ld;

import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.treemanage.AbstractTreeManageController;
import nc.vo.ld.InpriceVO;
import nc.vo.ld.InvclVO;
import nc.vo.trade.pub.HYBillVO;

public class ClientControl extends AbstractTreeManageController{

	public boolean isAutoManageTree() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isTableTree() {
		
		return false;
	}

	public String[] getCardBodyHideCol() {
		
		return null;
	}

	public int[] getCardButtonAry() {
		// TODO Auto-generated method stub
		 return new int[]{
				IBillButton.Add,
				IBillButton.Delete,
				IBillButton.Edit,
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
		return "80060101";
	}

	public String[] getBillVoName() {
		// TODO Auto-generated method stub
		return new String[]{
				HYBillVO.class.getName(),
				InvclVO.class.getName(),
				InpriceVO.class.getName()
				
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
		return IBusinessActionType.BD;
	}

	public String getChildPkField() {
		// TODO Auto-generated method stub
		return "pk_ldprice";
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
		return false;
	}

	public String[] getListBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getListButtonAry() {
		// TODO Auto-generated method stub
		return new int[]{
				IBillButton.Add,
				IBillButton.Delete,
				IBillButton.Edit,
				IBillButton.Refresh
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

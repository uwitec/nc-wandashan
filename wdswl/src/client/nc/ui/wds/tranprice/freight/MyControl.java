package nc.ui.wds.tranprice.freight;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.tranprice.freight.ZhbzBVO;
import nc.vo.wds.tranprice.freight.ZhbzHVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class MyControl extends AbstractManageController{

	public String[] getCardBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getCardButtonAry() {
		int[] buttonArray = new int[]{
				IBillButton.Query,
				IBillButton.Line,
				IBillButton.Add,
				IBillButton.Delete,
				IBillButton.Edit,
				IBillButton.Save,
				IBillButton.Refresh,
				IBillButton.Return
		};
		return buttonArray;
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
		return WdsWlPubConst.ZHBZ;
	}

	public String[] getBillVoName() {
		return new String[]{
				HYBillVO.class.getName(),
				ZhbzHVO.class.getName(),
				ZhbzBVO.class.getName()
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

	public int[] getListButtonAry() {
		int[] buttonArray = new int[]{
				IBillButton.Query,
				IBillButton.Add,
				IBillButton.Delete,
				IBillButton.Edit,
				IBillButton.Card,
				IBillButton.Refresh
		};
		return buttonArray;
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

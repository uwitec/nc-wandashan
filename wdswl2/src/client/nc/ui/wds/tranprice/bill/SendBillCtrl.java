package nc.ui.wds.tranprice.bill;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.vo.wds.tranprice.bill.SendBillBodyVO;
import nc.vo.wds.tranprice.bill.SendBillHeaderVO;
import nc.vo.wds.tranprice.bill.SendBillVO;
import nc.vo.wl.pub.ButtonCommon;
import nc.vo.wl.pub.WdsWlPubConst;

public class SendBillCtrl extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		
		return null;
	}
	public int[] getCardButtonAry() {
		
		return new int[]{
				IBillButton.Refbill,
				ButtonCommon.TRAN_COL,
				IBillButton.Edit,
				IBillButton.Save,
				IBillButton.Cancel,
				IBillButton.Query,
				IBillButton.Line,
				IBillButton.Del,
				IBillButton.Brow,
				IBillButton.Refresh,
				IBillButton.Return,
				IBillButton.Action,
				IBillButton.Print,
				IBillButton.DirectPrint
		};
	}
	
	public int[] getListButtonAry() {
		// TODO Auto-generated method stub
		return new int[]{
				IBillButton.Refbill,
				ButtonCommon.TRAN_COL,
				IBillButton.Edit,
				IBillButton.Cancel,
				IBillButton.Query,
				IBillButton.Del,
				IBillButton.Brow,
				IBillButton.Refresh,
				IBillButton.Card,
				IBillButton.Action,
				IBillButton.Print,
				IBillButton.DirectPrint
		};
	}

	public boolean isShowCardRowNo() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isShowCardTotal() {
		// TODO Auto-generated method stub
		return true;
	}

	public String getBillType() {
		// TODO Auto-generated method stub
		return WdsWlPubConst.WDSM;
	}

	public String[] getBillVoName() {
		// TODO Auto-generated method stub
		return new String[]{
				SendBillVO.class.getName(),
				SendBillHeaderVO.class.getName(),
				SendBillBodyVO.class.getName()
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
		return false;
	}

	public boolean isShowListTotal() {
		// TODO Auto-generated method stub
		return true;
	}

}

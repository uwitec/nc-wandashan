package nc.ui.wds2.inv;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.wds2.inv.StatusUpdateBillVO;
import nc.vo.wds2.inv.StatusUpdateBodyVO;
import nc.vo.wds2.inv.StatusUpdateHeadVO;
import nc.vo.wl.pub.Wds2WlPubConst;
import nc.vo.zmpub.pub.consts.ZmpubBtnConst;

public class StatusUpdateCtrl extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getCardButtonAry() {
        int[] buttonArray = new int[] { IBillButton.Add, IBillButton.Edit, IBillButton.Save,
                IBillButton.Cancel, IBillButton.Query, IBillButton.Action, IBillButton.Del,
                IBillButton.Line, IBillButton.Brow, IBillButton.Refresh, IBillButton.Return,
                IBillButton.Print, IBillButton.DirectPrint, ZmpubBtnConst.LINKQUERY,IBillButton.ApproveInfo };
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
		// TODO Auto-generated method stub
		return Wds2WlPubConst.billtype_statusupdate;
	}

	public String[] getBillVoName() {
		// TODO Auto-generated method stub
		return new String[]{
				StatusUpdateBillVO.class.getName(),
				StatusUpdateHeadVO.class.getName(),
				StatusUpdateBodyVO.class.getName()
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
		return IBusinessActionType.PLATFORM;
	}

	public String getChildPkField() {
		// TODO Auto-generated method stub
		return "cbill_bid";
	}

	public String getHeadZYXKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPkField() {
		// TODO Auto-generated method stub
		return "cbillid";
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

	public int[] getListButtonAry() {
        int[] buttonArray = new int[] { IBillButton.Add, IBillButton.Edit, IBillButton.Save,
                IBillButton.Cancel, IBillButton.Query, IBillButton.Action, IBillButton.Del,
                IBillButton.Line, IBillButton.Brow, IBillButton.Refresh, IBillButton.Card,
                IBillButton.Print, IBillButton.DirectPrint, ZmpubBtnConst.LINKQUERY,IBillButton.ApproveInfo };
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

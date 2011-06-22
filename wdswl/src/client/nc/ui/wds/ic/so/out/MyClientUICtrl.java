package nc.ui.wds.ic.so.out;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.ui.wds.w80060206.buttun0206.ISsButtun;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.wl.pub.ButtonCommon;
import nc.vo.wl.pub.WdsWlPubConst;



public class MyClientUICtrl extends AbstractManageController {

	
	
	
	
	public MyClientUICtrl() {
		super();

	}


	public String[] getCardBodyHideCol() {
		return null;
	}

	
	public int[] getCardButtonAry() {

		return new int[] { IBillButton.Refbill,IBillButton.Query, 
				IBillButton.Edit, IBillButton.Save, IBillButton.Del,IBillButton.Cancel,
				IBillButton.Return, IBillButton.Refresh,ButtonCommon.joinup,
				nc.ui.wds.w8004040204.ssButtun.ISsButtun.fzgn, ISsButtun.Qzqr,
				ISsButtun.Qxqz,IBillButton.Print };

	}

	public int[] getListButtonAry() {
		return new int[] { IBillButton.Refbill,IBillButton.Query, 
				IBillButton.Edit, IBillButton.Save,IBillButton.Del, ButtonCommon.joinup,IBillButton.Cancel,
				IBillButton.Card, IBillButton.Refresh,
				nc.ui.wds.w8004040204.ssButtun.ISsButtun.fzgn, ISsButtun.Qzqr,
				ISsButtun.Qxqz,IBillButton.Print
		};

	}

	public boolean isShowCardRowNo() {
		return true;
	}



	public String getBillType() {
		return WdsWlPubConst.BILLTYPE_SALE_OUT;
	}

	public String[] getBillVoName() {
		return new String[] { MyBillVO.class.getName(),
				TbOutgeneralHVO.class.getName(),
				TbOutgeneralBVO.class.getName() };
	}

	public String getBodyCondition() {
		return null;
	}

	public String getBodyZYXKey() {
		return null;
	}

	public int getBusinessActionType() {
		return IBusinessActionType.PLATFORM;
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
		return true;
	}

	public boolean isLoadCardFormula() {
		return true;
	}

	public String[] getListBodyHideCol() {
		return null;
	}

	public String[] getListHeadHideCol() {
		return null;
	}

	public boolean isShowListRowNo() {
		return true;
	}

	
	public boolean isShowListTotal() {
		return true;
	}

	public boolean isShowCardTotal() {
		return true;
	}
	
	
}

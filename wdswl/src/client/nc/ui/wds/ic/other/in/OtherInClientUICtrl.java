package nc.ui.wds.ic.other.in;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.ic.pub.TbgeneralB2VO;
import nc.vo.wl.pub.ButtonCommon;
import nc.vo.wl.pub.WdsWlPubConst;

public class OtherInClientUICtrl extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {

		return new int[] { 
				IBillButton.Refbill,
				IBillButton.Add,
				IBillButton.Del,
				IBillButton.Edit,
				IBillButton.Query, 
				IBillButton.Line, 
				IBillButton.Save, 
				IBillButton.Cancel,
				IBillButton.Return,
				IBillButton.Refresh,
				IBillButton.Print,
				ButtonCommon.joinup,
			///	nc.ui.wds.w8004040214.buttun0214.ISsButtun.Fzgn,
			//	nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr,
			//	nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz
				
		};

	}

	public int[] getListButtonAry() {
		return new int[] { 
				IBillButton.Refbill,
				IBillButton.Add,
				IBillButton.Del,
				IBillButton.Edit,
				IBillButton.Query, 
				IBillButton.Line, 
				IBillButton.Save, 
				IBillButton.Cancel,
				IBillButton.Card, 
				IBillButton.Refresh,
				IBillButton.Print,
				ButtonCommon.joinup,
			//	nc.ui.wds.w8004040214.buttun0214.ISsButtun.Fzgn,
			//	nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr,
			//	nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz
				
		};

	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return true;
	}

	public String getBillType() {
		return WdsWlPubConst.BILLTYPE_OTHER_IN;
	}

	public String[] getBillVoName() {
		return new String[] { OtherInBillVO.class.getName(),
				TbGeneralHVO.class.getName(), TbGeneralBVO.class.getName(), TbgeneralB2VO.class.getName()};
	}

	public String getBodyCondition() {
		return " isnull(dr,0)=0 ";
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
		return "pk_loadprice";
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

}

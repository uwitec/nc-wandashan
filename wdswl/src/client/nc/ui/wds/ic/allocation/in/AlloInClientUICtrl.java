package nc.ui.wds.ic.allocation.in;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.ic.pub.TbgeneralB2VO;
import nc.vo.wl.pub.ButtonCommon;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 
 * <p>
 *    µ÷²¦Èë¿â
 * </p>
 * 
 * Create on 2006-4-6 16:00:51
 * 
 * @author authorName
 * @version tempProject version
 */

public class AlloInClientUICtrl extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {

		return new int[] {IBillButton.Refbill,
				
				
				IBillButton.Edit,
				IBillButton.Query, 
				IBillButton.Del,
				IBillButton.Save, 
				IBillButton.Line,
//				IBillButton.CopyLine,
//				IBillButton.PasteLine,
				IBillButton.Cancel,
				IBillButton.Return,
				IBillButton.Refresh,
				IBillButton.Print,
				ButtonCommon.joinup,
				nc.ui.wds.w8004040214.buttun0214.ISsButtun.Fzgn,
				nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr,
				nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz
				 };

	}

	public int[] getListButtonAry() {
		return new int[] {
				IBillButton.Refbill,
		    
				
				IBillButton.Edit,
				IBillButton.Query, 
				IBillButton.Del,
				IBillButton.Save, 
				IBillButton.Cancel,
				IBillButton.Line,

				IBillButton.Card,
				IBillButton.Refresh,
				IBillButton.Print,
				ButtonCommon.joinup,
				nc.ui.wds.w8004040214.buttun0214.ISsButtun.Fzgn,
				nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr,
				nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz
				 };

	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return true;
	}

	public String getBillType() {
//		return "8004040210";
		return WdsWlPubConst.BILLTYPE_ALLO_IN;
	}

	public String[] getBillVoName() {
		return new String[] { OtherInBillVO.class.getName(),
				TbGeneralHVO.class.getName(), TbGeneralBVO.class.getName(),TbgeneralB2VO.class.getName() };
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

}

package nc.ui.wds.w8004040208;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
import nc.vo.wds.w8004040208.MyBillVO;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * Create on 2006-4-6 16:00:51
 * 
 * @author authorName
 * @version tempProject version
 */

public class MyClientUICtrl extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {

		return new int[] { IBillButton.Query, ISsButtun.zj,IBillButton.Edit,
				IBillButton.Line, IBillButton.Save, IBillButton.Cancel,
				IBillButton.Return, IBillButton.Refresh,
				nc.ui.wds.w8004040204.ssButtun.ISsButtun.fzgn,
				nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr,
				nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz };

	}

	public int[] getListButtonAry() {
		return new int[] { IBillButton.Query, ISsButtun.zj,IBillButton.Edit,
				IBillButton.Save, IBillButton.Cancel, IBillButton.Card,
				IBillButton.Refresh,
				nc.ui.wds.w8004040204.ssButtun.ISsButtun.fzgn,
				nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr,
				nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz };

	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return true;
	}

	public String getBillType() {
		return "8004040208";
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

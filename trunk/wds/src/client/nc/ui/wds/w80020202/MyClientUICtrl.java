package nc.ui.wds.w80020202;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.bill.ISingleController;

import nc.vo.wds.w80020202.MultiBillVO;
import nc.vo.wds.w80020202.TbHandlecostsVO;
import nc.vo.wds.w80020202.TbHandlecostsBVO;
import nc.vo.wds.w80020202.TbLogoVO;
import nc.vo.wds.w80020202.TbAcquisitionVO;
import nc.ui.trade.button.IBillButton;
import nc.ui.wds.w80020202.buttun0202.ISsButtun;

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

		return new int[] { IBillButton.Query, IBillButton.Edit,
				IBillButton.Save, IBillButton.Cancel, IBillButton.Delete,
				IBillButton.Return, IBillButton.Refresh, IBillButton.Print };

	}

	public int[] getListButtonAry() {
		return new int[] { IBillButton.Query, IBillButton.Edit,
				IBillButton.Save, IBillButton.Cancel, IBillButton.Delete,
				IBillButton.Card, IBillButton.Refresh, IBillButton.Print

		};

	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return true;
	}

	public String getBillType() {
		return "80020202";
	}

	public String[] getBillVoName() {
		return new String[] { MultiBillVO.class.getName(),
				TbHandlecostsVO.class.getName(),
				TbHandlecostsBVO.class.getName(), TbLogoVO.class.getName(),
				TbAcquisitionVO.class.getName() };
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

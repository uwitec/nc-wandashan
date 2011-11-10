package nc.ui.wds.ic.write.back4y;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.vo.wds.ic.write.back4y.MultiBillVO;
import nc.vo.wds.ic.write.back4y.Writeback4yB1VO;
import nc.vo.wds.ic.write.back4y.Writeback4yB2VO;
import nc.vo.wds.ic.write.back4y.Writeback4yHVO;
import nc.vo.wl.pub.WdsWlPubConst;


public class ClientController extends AbstractManageController{

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		int[] buttonArray = new int[]{
				IBillButton.Add,
				IBillButton.Save,
				IBillButton.Line,
				IBillButton.Query, 		
				IBillButton.Return,
				IBillButton.Brow,
				IBillButton.Refresh,
				IBillButton.Action	
		};
		return buttonArray;
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return true;
	}

	public String getBillType() {
		return WdsWlPubConst.WDSP;
	}

	public String[] getBillVoName() {
		return new String[] {
				MultiBillVO.class.getName(),
				Writeback4yHVO.class.getName(),
				Writeback4yB1VO.class.getName(),
				Writeback4yB2VO.class.getName()
			};
	}

	public String getBodyCondition() {
		return null;
	}

	public String getBodyZYXKey() {
		return null;
	}

	public int getBusinessActionType() {
		return nc.ui.trade.businessaction.IBusinessActionType.PLATFORM;
	}

	public String getChildPkField() {
		return "";
	}

	public String getHeadZYXKey() {
		return null;
	}

	public String getPkField() {
		return "";
	}

	public Boolean isEditInGoing() throws Exception {
		return false;
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

	public int[] getListButtonAry() {
		int[] buttonArray = new int[]{
				IBillButton.Add,
				IBillButton.Save,
				IBillButton.Line,
				IBillButton.Query, 		
				IBillButton.Return,
				IBillButton.Brow,
				IBillButton.Refresh,
				IBillButton.Action	
		};
		return buttonArray;
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

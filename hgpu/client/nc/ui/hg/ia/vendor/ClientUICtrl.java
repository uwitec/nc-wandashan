package nc.ui.hg.ia.vendor;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.button.IBillButton;
import nc.vo.hg.pu.pub.HgPuBtnConst;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.trade.pub.HYBillVO;

public class ClientUICtrl implements ICardController,ISingleController {

	public ClientUICtrl() {
		super();
	}
	
	private String pk_corp = null;
	public ClientUICtrl(String corp) {
		super();
		pk_corp=corp;
	}


	public int[] getCardButtonAry() {
			return new int[]{
//					HgPuBtnConst.VIEW,
					IBillButton.Add,
					IBillButton.Edit,
					IBillButton.Save,
					IBillButton.Cancel,
					IBillButton.Query,
					HgPuBtnConst.FREEZE,
					HgPuBtnConst.UNFREEZE,
					IBillButton.Refresh
			};
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return HgPubConst.FREEZE_VENTOR;
	}

	public String[] getBillVoName() {
		return new String[]{
				HYBillVO.class.getName(),
				BalFreezeVO.class.getName(),
				BalFreezeVO.class.getName()	
		};
	}

	public String getBodyCondition() {
		return " 1=1 and pk_corp = '"+pk_corp+"'";
	}

	public String getBodyZYXKey() {
		return null;
	}

	public int getBusinessActionType() {
		return nc.ui.trade.businessaction.IBusinessActionType.BD;
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
//	µ¥±íÌå
	public boolean isSingleDetail() {
		return true;
	}

	public String[] getCardBodyHideCol() {
		return null;
	}

}

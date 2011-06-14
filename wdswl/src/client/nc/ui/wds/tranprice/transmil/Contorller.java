package nc.ui.wds.tranprice.transmil;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.tranprice.transmil.TransmilVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class Contorller implements ICardController,ISingleController{

	public String[] getCardBodyHideCol() {
		
		return null;
	}

	public int[] getCardButtonAry() {
		
		return new int[]{
				IBillButton.Add,
				IBillButton.Delete,
				IBillButton.Save,
				IBillButton.Edit,
				IBillButton.Query,
				IBillButton.ImportBill,
				IBillButton.Cancel,
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
		
		return WdsWlPubConst.TRANS_MIL_NODECODE;
	}

	public String[] getBillVoName() {
	 
		return new String[]{
				HYBillVO.class.getName(),
				TransmilVO.class.getName(),
				TransmilVO.class.getName()
				
				
		};
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

	public boolean isSingleDetail() {
		
		return true;
	}
	
	
	
	
}
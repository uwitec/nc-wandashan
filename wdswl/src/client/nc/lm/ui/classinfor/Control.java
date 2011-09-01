package nc.lm.ui.classinfor;

import nc.lm.vo.classinfor.ClassBVO;
import nc.lm.vo.classinfor.ClassHVO;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class Control extends AbstractManageController
{

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
		   IBillButton.File,
		   IBillButton.Return,
		   IBillButton.Brow,
		   IBillButton.Line		
		};
	}
	public boolean isShowCardRowNo() {
		
		return false;
	}

	public boolean isShowCardTotal() {		
		return false;
	}

	public String getBillType() {		
		return WdsWlPubConst.BILLTYPE_LM_CLASSINFOR;
	}
	public String[] getBillVoName(){		
		return new String[]{HYBillVO.class.getName(),ClassHVO.class.getName(),ClassBVO.class.getName()};
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
	public int[] getListButtonAry() {
		 return new int[]{
				   IBillButton.Add,
				   IBillButton.Delete,
				   IBillButton.Save,
				   IBillButton.File,
				   IBillButton.Edit,
				   IBillButton.Query,
				   IBillButton.Card,
				   IBillButton.Line		
				};
	}
	public String[] getListHeadHideCol() {
		return null;
	}
	public boolean isShowListRowNo() {
		return false;
	}
	public boolean isShowListTotal() {		
		return false;
	}

}

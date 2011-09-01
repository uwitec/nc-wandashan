package nc.ui.lm.chengji;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.lm.chengji.ChengjiBVO;
import nc.vo.lm.chengji.ChengjiHVO;
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
		   IBillButton.Return,
		   IBillButton.FileWrite,
		   IBillButton.FileRead,
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
		return WdsWlPubConst.LM_CHENGJI_BILLTYPE;
	}
	public String[] getBillVoName(){		
		return new String[]{HYBillVO.class.getName(),ChengjiHVO.class.getName(),ChengjiBVO.class.getName()};
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
	public int[] getListButtonAry() {
		 return new int[]{
				   IBillButton.Add,
				   IBillButton.Delete,
				   IBillButton.Save,
				   IBillButton.Edit,
				   IBillButton.FileWrite,
				   IBillButton.FileRead,
				   IBillButton.Query,
				   IBillButton.Card,
				   IBillButton.Line		
				};
	}
	public String[] getListHeadHideCol() {
		return null;
	}
	public boolean isShowListRowNo() {
		return true;
	}
	public boolean isShowListTotal() {		
		return false;
	}

}

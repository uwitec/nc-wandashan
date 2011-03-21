package nc.ui.wds.w8006080806;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.bill.ISingleController;

import nc.vo.wds.w8006080806.MyBillVO;
import nc.vo.wds.w80060804.TbTranscompanyVO;
import nc.vo.wds.w8006080806.TbEvenstoreHVO;
import nc.ui.trade.button.IBillButton;


/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
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
		                                
                return new int[]{
                                                               IBillButton.Query,
                                                                                    IBillButton.Edit,
                                                                                    IBillButton.Line,
                                                                                    IBillButton.Save,
                                                                                    IBillButton.Cancel,
                                                                                    IBillButton.Return,
                                                                                    IBillButton.Refresh,
                                                                                    IBillButton.Print
                                                         };
  
	}
	
	public int[] getListButtonAry() {		
			        	        return new int[]{
	         	           	             IBillButton.Query,
	           	         	           	             IBillButton.Edit,
	           	         	           	             IBillButton.Line,
	           	         	           	             IBillButton.Save,
	           	         	           	             IBillButton.Cancel,
	           	         	           	             IBillButton.Card,
	           	         	           	             IBillButton.Refresh,
	           	         	           	             IBillButton.Print
	           	         	        
	        };
	
	}

	public boolean isShowCardRowNo() {
		return false;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "8006080806";
	}

	public String[] getBillVoName() {
		return new String[]{
			MyBillVO.class.getName(),
			TbTranscompanyVO.class.getName(),
			TbEvenstoreHVO.class.getName()
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

	public String[] getListBodyHideCol() {	
		return null;
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

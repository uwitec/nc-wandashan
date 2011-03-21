package nc.ui.ncblk.yjcb.yjcbjs;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.bill.ISingleController;

import nc.vo.ncblk.yjcb.yjcbjs.MyBillVO;
import nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbVO;
import nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbBVO;
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
                                                                                    IBillButton.Add,
                                                                                    IBillButton.Edit,
                                                                                    IBillButton.Save,
                                                                                    IBillButton.Cancel,
                                                                                    IBillButton.Delete,
                                                                                    IBillButton.Return,
                                                                                    IPrivateButton.SYSTEM_BUTON_JS,
                                                                                    IPrivateButton.SYSTEM_BUTTON_TZ,
                                                                                    IPrivateButton.SYSTEM_BUTON_QR
                };
  
	}
	
	public int[] getListButtonAry() {		
			        	        return new int[]{
	         	           	             IBillButton.Query,
	           	         	           	             IBillButton.Add,
	           	         	           	             IBillButton.Edit,
	           	         	           	             IBillButton.Save,
	           	         	           	             IBillButton.Cancel,
	           	         	           	             IBillButton.Delete,
	           	         	           	             IBillButton.Card,
	           	         	           	             IPrivateButton.SYSTEM_BUTON_JS,
	           	         	           	             IPrivateButton.SYSTEM_BUTTON_TZ,
	           	         	           	             IPrivateButton.SYSTEM_BUTON_QR
	        };
	
	}

	public boolean isShowCardRowNo() {
		return false;
	}

	public boolean isShowCardTotal() {
		return true;
	}

	public String getBillType() {
		return "H1H0H0";
	}

	public String[] getBillVoName() {
		return new String[]{
			MyBillVO.class.getName(),
			NcReportYjcbVO.class.getName(),
			NcReportYjcbBVO.class.getName()
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
		return true;
	}
	
}

package nc.ui.wds.w80060606;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.bill.ISingleController;

import nc.vo.wds.w80060606.MyBillVO;
import nc.vo.wds.w80060606.TbProdwaybillVO;
import nc.vo.wds.w80060606.TbProdwaybillBVO;
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
                                                              
                                                                                    
                                                                                  
                                                                                   
                                                                                  
                                                                                   
                		nc.ui.wds.w80060606.pwButtun.IPwButtun.PwAdd,
                                                                                    IBillButton.Return,
                                                                                    IBillButton.Print
                                                                                    
                                                         };
  
	}
	
	public int[] getListButtonAry() {		
			        	        return new int[]{
	         	           	            
	           	         	           	             
	           	         	           	            
	           	         	           	           
			        	        		  nc.ui.wds.w80060606.pwButtun.IPwButtun.PwAdd,
	           	         	           	            
	           	         	           	             IBillButton.Card,
	           	         	           	             IBillButton.Print
	           	         	           	           
	           	         	        
	        };
	
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "80060606";
	}

	public String[] getBillVoName() {
		return new String[]{
			MyBillVO.class.getName(),
			TbProdwaybillVO.class.getName(),
			TbProdwaybillBVO.class.getName()
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
		return true;
	}

	public boolean isShowListTotal() {
		return false;
	}
	
}

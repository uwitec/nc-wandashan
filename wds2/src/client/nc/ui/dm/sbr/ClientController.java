package nc.ui.dm.sbr;


/*
 * 特殊业务档案
 */
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.button.IBillButton;
import nc.vo.dm.sbr.SbDocRVO;
import nc.vo.trade.pub.HYBillVO;

public class ClientController implements ICardController, ISingleController{

	
	public ClientController() {
		super();
	}
	
	public ClientController(String corp) {
		super();
	}
	
	public int[] getCardButtonAry() {
		return new int[]{
				IBillButton.Add,
//				IBillButton.Line,
				IBillButton.Save,
				IBillButton.Query,
				IBillButton.Delete,
				IBillButton.Cancel,
				IBillButton.Edit,
				IBillButton.Refresh,
//				IBillButton.Return
				
			};
	}

	public boolean isShowCardRowNo() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isShowCardTotal() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getBillType() {
		// TODO Auto-generated method stub
		return "wds_sbdocr";
	}

	public String[] getBillVoName() {
		return new String[]{
				HYBillVO.class.getName(),
				SbDocRVO.class.getName(),
				SbDocRVO.class.getName()
		};
	}

	public String getBodyCondition() {
		// TODO Auto-generated method stub
		return "1=1";
	}

	public String getBodyZYXKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getBusinessActionType() {
		// TODO Auto-generated method stub
		return nc.ui.trade.businessaction.IBusinessActionType.BD;
	}

	public String getChildPkField() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getHeadZYXKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPkField() {
		// TODO Auto-generated method stub
		return "pk_sbdocr";
	}

	public Boolean isEditInGoing() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isExistBillStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isLoadCardFormula() {
		// TODO Auto-generated method stub
		return true;
	}

		
	public boolean isSingleDetail() {
		// TODO Auto-generated method stub
		return true;
	}
	public String[] getCardBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}
}

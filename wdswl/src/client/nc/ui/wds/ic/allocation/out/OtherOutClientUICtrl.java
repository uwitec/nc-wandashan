package nc.ui.wds.ic.allocation.out;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.ic.other.out.TbOutgeneralB2VO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.wl.pub.ButtonCommon;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * µ÷²¦³ö¿â
 */
public class OtherOutClientUICtrl extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {

		return new int[] { IBillButton.Refbill,
				IBillButton.Edit,
				IBillButton.Line, IBillButton.Save, IBillButton.Query,IBillButton.Del,IBillButton.Cancel,
				IBillButton.Return, IBillButton.Refresh,
				IBillButton.Print,
				ButtonCommon.joinup,
				nc.ui.wds.w8004040204.ssButtun.ISsButtun.fzgn,
				nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr,
				nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz 
		  		
		};

	}

	public int[] getListButtonAry() {
		return new int[] { 
				IBillButton.Refbill,IBillButton.Edit,
				IBillButton.Save, IBillButton.Query,IBillButton.Del,IBillButton.Cancel, 
				IBillButton.Card,IBillButton.Refresh,
				ButtonCommon.joinup,
				nc.ui.wds.w8004040204.ssButtun.ISsButtun.fzgn,
				nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr,
				nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz,
				IBillButton.Print};

	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return true;
	}

	public String getBillType() {
		return WdsWlPubConst.BILLTYPE_ALLO_OUT;
	}

	public String[] getBillVoName() {
		return new String[] { MyBillVO.class.getName(),
				TbOutgeneralHVO.class.getName(),
				TbOutgeneralBVO.class.getName(),
				TbOutgeneralB2VO.class.getName()
				};
	}

	public String getBodyCondition() {
		  return " isnull(dr,0)=0 ";
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

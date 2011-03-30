package nc.ui.wds.w80060820;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.bill.ISingleController;

import nc.vo.wds.w80060820.MyBillVO;
import nc.vo.wds.w80060810.TbYslcglVO;
import nc.vo.wds.w80060810.TbYslcglVO;
import nc.ui.trade.button.IBillButton;

/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * 
 * <p>
 * �ڴ˴����Ӵ����������Ϣ
 * </p>
 * 
 * Create on 2006-4-6 16:00:51
 * 
 * @author authorName
 * @version tempProject version
 */

public class MyClientUICtrl extends AbstractManageController implements
	ISingleController {

    public String[] getCardBodyHideCol() {
	return null;
    }

    public int[] getCardButtonAry() {

	return new int[] { IBillButton.Query, IBillButton.Add,
		IBillButton.Edit, IBillButton.Save, IBillButton.Cancel,
		IBillButton.Delete, IBillButton.Return, IBillButton.Refresh,
		IBillButton.Print };

    }

    public int[] getListButtonAry() {
	return new int[] { IBillButton.Query, IBillButton.Add,
		IBillButton.Edit, IBillButton.Save, IBillButton.Cancel,
		IBillButton.Delete, IBillButton.Card, IBillButton.Refresh,
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
	return "80060820";
    }

    public String[] getBillVoName() {
	return new String[] { MyBillVO.class.getName(),
		TbYslcglVO.class.getName(), TbYslcglVO.class.getName() };
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
	return false;
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

    /**
     * �Ƿ񵥱�
     * 
     * @return boolean true:�����壬false:����ͷ
     */
    public boolean isSingleDetail() {
	return false; // ����ͷ
    }
}
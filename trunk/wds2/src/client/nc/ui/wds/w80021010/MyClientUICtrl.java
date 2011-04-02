package nc.ui.wds.w80021010;


import nc.ui.trade.bill.IListController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.w9000.BdInvbasdocVO;

/**
 * <b> �б�UI��������</b><br>
 * 
 * <p>
 * ���ý��水ť�����ݣ��Ƿ�ƽ̨��ص���Ϣ
 * </p>
 * <br>
 * 
 * Create on 2006-4-6 16:00:51
 * 
 * @author authorName
 * @version tempProject version
 */

public class MyClientUICtrl implements IListController, ISingleController {

    public String[] getCardBodyHideCol() {
	return null;
    }

    /**
     * ���ý��水ť
     */
    public int[] getListButtonAry() {

	return new int[] { IBillButton.Query, IBillButton.Edit,
		IBillButton.Save, IBillButton.Cancel, IBillButton.Refresh,
		IBillButton.Print };
    }

    public boolean isShowCardRowNo() {
	return false;
    }

    public boolean isShowCardTotal() {
	return false;
    }

    public String getBillType() {
	return "80021010";
    }

    public String[] getBillVoName() {
	return new String[] { HYBillVO.class.getName(),
		BdInvbasdocVO.class.getName(), BdInvbasdocVO.class.getName() };
    }

    public String getBodyCondition() {
	return null;
    }

    public String getBodyZYXKey() {
	return null;
    }

    /**
     * �Ƿ�ƽ̨�޹�
     */
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

    /**
     * �Ƿ񵥱�
     * 
     * @return boolean true:�����壬false:����ͷ
     */
    public boolean isSingleDetail() {
	return false; // ����ͷ
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

}

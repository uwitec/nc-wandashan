package nc.ui.wds.w80021010;


import nc.ui.trade.bill.IListController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.w9000.BdInvbasdocVO;

/**
 * <b> 列表UI控制器类</b><br>
 * 
 * <p>
 * 设置界面按钮，数据，是否平台相关等信息
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
     * 设置界面按钮
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
     * 是否平台无关
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
     * 是否单表
     * 
     * @return boolean true:单表体，false:单表头
     */
    public boolean isSingleDetail() {
	return false; // 单表头
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

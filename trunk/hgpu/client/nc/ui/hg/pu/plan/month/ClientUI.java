package nc.ui.hg.pu.plan.month;

import nc.ui.hg.pu.pub.PlanPubClientUI;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.hg.pu.pub.HgPuBtnConst;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;

/**
 * 月份领用计划
 * @author zhw
 */
public class ClientUI extends PlanPubClientUI {

    public ClientUI() {
        super();
    }

    @Override
    protected void initSelfData() {
        super.initSelfData();
        getBillCardWrapper().initHeadComboBox("cmonth",
            new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }, false);
    }

    // 添加自定义按钮
    public void initPrivateButton() {
        super.initPrivateButton();
        // 打开
        ButtonVO btnvo1 = new ButtonVO();
        btnvo1.setBtnNo(HgPuBtnConst.OPEN);
        btnvo1.setBtnName("打开");
        btnvo1.setBtnChinaName("打开");
        btnvo1.setBtnCode(null);// code最好设置为空
        btnvo1.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT, IBillOperate.OP_NOADD_NOTEDIT });
        addPrivateButton(btnvo1);
        // 关闭
        ButtonVO btnvo2 = new ButtonVO();
        btnvo2.setBtnNo(HgPuBtnConst.CLOSE);
        btnvo2.setBtnName("关闭");
        btnvo1.setBtnChinaName("关闭");
        btnvo2.setBtnCode(null);// code最好设置为空
        btnvo2.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT, IBillOperate.OP_NOADD_NOTEDIT });
        addPrivateButton(btnvo2);

    }

    @Override
    protected ManageEventHandler createEventHandler() {
        return new ClientEventHandler(this, getUIControl());
    }

    @Override
    protected AbstractManageController createController() {
        return new ClientController();
    }

    @Override
    public void setBodySpecialData(CircularlyAccessibleValueObject[] vos) throws Exception {

    }
    
    @Override
    protected String getBillNo() throws Exception {
        return HYPubBO_Client.getBillNo(HgPubConst.PLAN_MONTH_BILLTYPE, _getCorp().getPrimaryKey(), null,
            null);
    }

    @Override
    protected BusinessDelegator createBusinessDelegator() {
        return new ClientBusinessDelegator(this);
    }

    @Override
    public void setDefaultData() throws Exception {
        setHeadItemValue("pk_billtype", HgPubConst.PLAN_MONTH_BILLTYPE);
        setHeadItemValue("cyear",ClientEnvironment.getInstance().getAccountYear());
        setHeadItemValue("cmonth",ClientEnvironment.getInstance().getAccountMonth());
        super.setDefaultData();
    }
}

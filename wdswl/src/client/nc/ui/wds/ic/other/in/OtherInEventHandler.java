package nc.ui.wds.ic.other.in;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wds.ic.pub.InPubClientUI;
import nc.ui.wds.ic.pub.InPubEventHandler;
import nc.ui.wds.w8004040214.buttun0214.ISsButtun;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.wl.pub.BillRowNo;
import nc.vo.wl.pub.WdsWlPubConst;


public class OtherInEventHandler extends InPubEventHandler {

	public OtherInEventHandler(InPubClientUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		switch (intBtn) {

		case ISsButtun.Zdtp:
		
			onZdtp();
			break;
		case ISsButtun.Ckmx:
			onCkmx();
			break;
		case ISsButtun.Zdrk:
			onZdrk();
			break;
		case ISsButtun.Zzdj:
			onZzdj();
			break;
		case ISsButtun.Fydj:
			onFydj();
			break;
		case nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz:
			onQxqz();
			break;
		case nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr:
			onQzqr();
			break;
		case  nc.ui.wds.w80020206.buttun0206.ISsButtun.Ref4I:
			onBillRef();
			break;
		
		}
	}

	/**
	 * 自制单据
	 */
	public void onZzdj() throws Exception {
		if (getBillManageUI().isListPanelSelected())
			getBillManageUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		getBillUI().setBillOperate(IBillOperate.OP_ADD);
	}

	@Override
	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
		BillRowNo.addLineRowNo(getBillCardPanelWrapper().getBillCardPanel(),
				WdsWlPubConst.BILLTYPE_OTHER_IN, "geb_crowno");
	}

	public BillManageUI getBillManageUI() {
		return (BillManageUI) getBillUI();
	}
	/**
	 * 发运单据
	 */
	
	protected void onFydj() throws Exception {
		onBillRef();
	}
	/**
	 * 货位调整
	 */

	protected void onHwtz() throws Exception {

	}
	@Override
	protected String getHeadCondition() {
		StringBuffer strWhere = new StringBuffer();
		LoginInforHelper lo=new LoginInforHelper();
		try{
			strWhere.append(" geh_billtype ='"+WdsWlPubConst.BILLTYPE_OTHER_IN+"' ");
			String pk_stordoc = lo.getCwhid(_getOperator());
			strWhere.append(" and geh_cwarehouseid='" + pk_stordoc + "' ");
			String cargdocid = lo.getSpaceByLogUserForStore(ui._getOperator());
			if(cargdocid != null){//不是保管员登录 可以查看所有入库单
				strWhere.append(" and pk_cargdoc = '"+cargdocid+"'");
			}
			strWhere.append(" and isnull(dr,0) = 0 and pk_corp = '"+_getCorp().getPrimaryKey()+"' ");
		} catch (Exception e) {
			Logger.info(e);
		}
		return strWhere.toString();

	}

	@Override
	protected UIDialog createQueryUI() {
		return new MyQueryDIG(
				getBillUI(), null, 
				_getCorp().getPk_corp(), getBillUI().getModuleCode()
				, getBillUI()._getOperator(), null		
		);

	}

	

	
}
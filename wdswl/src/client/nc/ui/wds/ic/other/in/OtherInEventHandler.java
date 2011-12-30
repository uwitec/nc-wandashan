package nc.ui.wds.ic.other.in;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wds.ic.pub.InPubEventHandler;
import nc.ui.wds.ic.pub.MutiInPubClientUI;
import nc.ui.wds.w8004040214.buttun0214.ISsButtun;
import nc.ui.wl.pub.BeforeSaveValudate;
import nc.ui.wl.pub.LoginInforHelper;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class OtherInEventHandler extends InPubEventHandler {
	public OtherInEventHandler(MutiInPubClientUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		switch (intBtn) {

		case ISsButtun.Zdtp://手动拣货
			//拣货 存货唯一校验
			valudateWhereYeqian();
			BeforeSaveValudate.beforeSaveBodyUnique(getBillCardPanelWrapper().getBillCardPanel().getBillTable(),
					getBillCardPanelWrapper().getBillCardPanel().getBillModel(),
					new String[]{"invcode","geb_vbatchcode"},
					new String[]{"存货编码","批次号"});
			onZdtp();
			break;
		case ISsButtun.Ckmx:
			onCkmx();
			break;
		case ISsButtun.Zdrk://自动拣货
			valudateWhereYeqian();
			//拣货 存货唯一校验
			BeforeSaveValudate.beforeSaveBodyUnique(getBillCardPanelWrapper().getBillCardPanel().getBillTable(),
					getBillCardPanelWrapper().getBillCardPanel().getBillModel(),
					new String[]{"invcode","geb_vbatchcode"},
					new String[]{"存货编码","批次号"});
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
			((MyClientUI)getBillUI()).setRefBillType("4I");
			onBillRef();
//			zhf  参照新增时 不能增行
			getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
			setInitWarehouse("geh_cwarehouseid");
			break;	
		case  nc.ui.wds.w80020206.buttun0206.ISsButtun.refwds6:
		   ((MyClientUI)getBillUI()).setRefBillType(WdsWlPubConst.BILLTYPE_OTHER_OUT);
			onBillRef();
			setBusidate();//设置业务日期
//			zhf  参照新增时 不能增行
			getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
			getBillUI().updateButtons();
			break;		
		}
	}
    /**
     * 
     * @作者：mlr
     * @说明：完达山物流项目 
     *        表体行设置业务日期 默认为当前日期
     * @时间：2011-8-1下午03:57:14
     */
	private void setBusidate() {
	 int legth=getBillCardPanelWrapper().getBillCardPanel().getRowCount();
	 for(int i=0;i<legth;i++){
		 getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getDate(), i, "geb_dbizdate"); 
	 }
	}
	/**
	 * 拣货对应的页签
	 * 必须在出库子表页签
	 * @作者：zhf
	 * @说明：完达山物流项目 
	 * @时间：2011-9-22下午02:56:45
	 */
    private void valudateWhereYeqian()throws Exception{
	   String tablecode=getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().getTableCode();
	   if(!"tb_general_b".equalsIgnoreCase(tablecode)){
		 throw new Exception("请选择表体存货页签");   
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
			strWhere.append(" and isnull(dr,0) = 0 and pk_corp = '"+_getCorp().getPrimaryKey()+"' ");
			String pk_stordoc = lo.getCwhid(_getOperator());
			strWhere.append(" and geh_cwarehouseid='" + pk_stordoc + "' ");
			String cargdocid = lo.getSpaceByLogUserForStore(ui._getOperator());
			if(cargdocid != null){//不是保管员登录 可以查看所有入库单
				strWhere.append(" and pk_cargdoc = '"+cargdocid+"'");
			}
			
		} catch (Exception e) {
			Logger.info(e);
			System.out.print(e);
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

	protected void onBoEdit() throws Exception {
		if (getBufferData().getCurrentVO() == null)
			return;
		UFBoolean isadjust = PuPubVO.getUFBoolean_NullAs(getBufferData().getCurrentVO().getParentVO().getAttributeValue("geh_customize7"), UFBoolean.FALSE);
		if(isadjust.booleanValue()){
			getBillUI().showHintMessage("用于调整的出库单不能修改");
			return;
		}
		super.onBoEdit();
	}
	
	protected void onBoDel() throws Exception {
		if (getBufferData().getCurrentVO() == null)
			return;
		UFBoolean isadjust = PuPubVO.getUFBoolean_NullAs(getBufferData().getCurrentVO().getParentVO().getAttributeValue("geh_customize7"), UFBoolean.FALSE);
		if(isadjust.booleanValue()){
			getBillUI().showHintMessage("用于调整的出库单不能删除");
			return;
		}
		super.onBoDel();	
	}
	
	@Override    //xjx  add
	protected void onBoPrint() throws Exception {
		//　如果是列表界面，使用ListPanelPRTS数据源
		if( getBillManageUI().isListPanelSelected() ){
			nc.ui.pub.print.IDataSource dataSource = new MyListTPDateSource(getBillUI()
					._getModuleCode(),((BillManageUI) getBillUI()).getBillListPanel());
			nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,
					dataSource);
			print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()
					._getModuleCode(), getBillUI()._getOperator(), getBillUI()
					.getBusinessType(), getBillUI().getNodeKey());
			if (print.selectTemplate() == 1)
				print.preview();
		}else{
		final nc.ui.pub.print.IDataSource dataSource = new MyTpDateSource(
				getBillUI()._getModuleCode(), getBillCardPanelWrapper()
						.getBillCardPanel());
		final nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(
				null, dataSource);
		print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(),
				getBillUI()._getModuleCode(), getBillUI()._getOperator(),
				getBillUI().getBusinessType(), getBillUI().getNodeKey());
		if (print.selectTemplate() == 1)
			print.preview();
		// 更改数据源，支持托盘打印
	//	super.onBoPrint();
		}
		Integer iprintcount = PuPubVO.getInteger_NullAs(getBufferData()
				.getCurrentVO().getParentVO().getAttributeValue(
						"cdt_pk"), 0);
		iprintcount = iprintcount + 1;
		getBufferData().getCurrentVO().getParentVO().setAttributeValue(
				"iprintcount", iprintcount);
		try {
			HYPubBO_Client.update((SuperVO) getBufferData().getCurrentVO()
					.getParentVO());
			onBoRefresh();
		} catch (final UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  
	   }
	}
	@Override
	protected String getBillType() {
		// TODO Auto-generated method stub
		return WdsWlPubConst.BILLTYPE_OTHER_IN;
	}	
}
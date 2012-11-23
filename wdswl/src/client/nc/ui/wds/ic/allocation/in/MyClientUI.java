package nc.ui.wds.ic.allocation.in;

import javax.swing.JComponent;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.BillCardPanelWrapper;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.ButtonVOFactory;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.ic.pub.MutiInPubClientUI;
import nc.ui.wds.w80020206.buttun0206.QxqzBtn;
import nc.ui.wds.w80020206.buttun0206.QzqrBtn;
import nc.ui.wds.w8004040214.buttun0214.ZdrkBtn;
import nc.ui.wds.w80060206.buttun0206.ISsButtun;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.field.IBillField;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 调拨入库
 */
public class MyClientUI extends MutiInPubClientUI implements  BillCardBeforeEditListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	protected AbstractManageController createController() {
		return new AlloInClientUICtrl();
	}
	protected ManageEventHandler createEventHandler() {
		return new AlloInEventHandler(this, getUIControl());
	}

	@Override
	public BillCardPanelWrapper createBillCardPanelWrapper() throws Exception {
		return super.createBillCardPanelWrapper();
	}
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}
	
	protected BusinessDelegator createBusinessDelegator() {
		return new AlloDelegator();
	}
	public void afterUpdate() {
		if (!getBufferData().isVOBufferEmpty()) {
			int row = getBufferData().getCurrentRow();
			if (row < 0) {
				return;
			}
			Object o = getBufferData().getCurrentVO().getParentVO()
					.getAttributeValue(getBillField().getField_BillStatus());
			if (o.equals(IBillStatus.FREE)) {// 自由
				getButtonManager().getButton(ISsButtun.Qxqz).setEnabled(false);
				getButtonManager().getButton(ISsButtun.Qzqr).setEnabled(true);
			} else {// 签字
				getButtonManager().getButton(ISsButtun.Qzqr).setEnabled(false);
				getButtonManager().getButton(ISsButtun.Qxqz).setEnabled(true);
			}
			updateButtons();
		}
	}

	protected void initSelfData() {
		super.initSelfData();
	//	除去行操作多余按钮
		ButtonObject btnobj = getButtonManager().getButton(IBillButton.Line);
		if (btnobj != null) {
		    btnobj.removeChildButton(getButtonManager().getButton(IBillButton.InsLine));
//		    btnobj.removeChildButton(getButtonManager().getButton(IBillButton.CopyLine));
//		    btnobj.removeChildButton(getButtonManager().getButton(IBillButton.PasteLine));
		}
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}	
//	@Override
//	public boolean beforeEdit(BillEditEvent e) {
//		super.beforeEdit(e);
//		return true;
//	}
	
	/**
	 * 注册自定义按钮
	 */
	protected void initPrivateButton() {
		super.initPrivateButton();
//		FzgnBtn customizeButton1=new FzgnBtn();
//		addPrivateButton(customizeButton1.getButtonVO());
//		ZdtpBtn customizeButton2=new ZdtpBtn();
//		addPrivateButton(customizeButton2.getButtonVO());
//		CkmxBtn customizeButton3=new CkmxBtn();
//		addPrivateButton(customizeButton3.getButtonVO());
		ZdrkBtn customizeButton4=new ZdrkBtn();
		addPrivateButton(customizeButton4.getButtonVO());
		QzqrBtn customizeButton9=new QzqrBtn();
		addPrivateButton(customizeButton9.getButtonVO());
		QxqzBtn customizeButton10=new QxqzBtn();
		addPrivateButton(customizeButton10.getButtonVO());
		getButtonManager().getButtonAry(
				new int[] { ISsButtun.Qzqr, ISsButtun.Qxqz});// 取消签字,签字确认
		//对系统注册的参照按钮的更改
		ButtonVO ref4i = new ButtonVO();
		ref4i.setBtnNo(nc.ui.wds.w80020206.buttun0206.ISsButtun.Ref4I);
		ref4i.setBtnCode(null);
		ref4i.setBtnName("供应链调拨出库");
		ref4i.setBtnChinaName("供应链调拨出库");
		addPrivateButton(ref4i);
		ButtonVO refbill =ButtonVOFactory.getInstance().build(IBillButton.Refbill);
		refbill.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT, IBillOperate.OP_INIT });
		refbill.setChildAry(new int[]{ref4i.getBtnNo()});
		
		addPrivateButton(refbill);
	}
	
	@Override
	protected void initEventListener() {
		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}
	
	@Override
	protected IBillField createBillField() {
		return new nc.ui.wds.ic.other.in.BillField();
	}
	@Override
	protected String getBillNo() throws Exception {
		return HYPubBO_Client.getBillNo(WdsWlPubConst.BILLTYPE_ALLO_IN,_getOperator(),null, null);
	}
	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("pk_corp",_getCorp().getPk_corp());//当前登录公司
		getBillCardPanel().setHeadItem("geh_corp", _getCorp().getPk_corp());//入库公司
//		getBillCardPanel().setHeadItem("geh_calbody", WdsWlPubConst.DEFAULT_CALBODY);
		if(getBillOperate() != IBillOperate.OP_REFADD){
			try{		
				getBillCardPanel().setHeadItem("geh_cwarehouseid", getLoginInforHelper().getCwhid(_getOperator()));
				getBillCardPanel().setHeadItem("pk_cargdoc", getLoginInforHelper().getSpaceByLogUserForStore(_getOperator()));
			}catch(Exception e){
				e.printStackTrace();//zhf  异常不处理
			}
		}
		getBillCardPanel().setHeadItem("geh_billtype",WdsWlPubConst.BILLTYPE_ALLO_IN);
		getBillCardPanel().setHeadItem("geh_cbilltypecode",WdsWlPubConst.BILLTYPE_ALLO_IN);//入库单据类型
		getBillCardPanel().setHeadItem("pwb_fbillflag",IBillStatus.FREE);//单据状态
		getBillCardPanel().setHeadItem("geh_billcode", getBillNo());
		getBillCardPanel().setTailItem("copetadate",_getDate());
		getBillCardPanel().setHeadItem("geh_dbilldate",_getDate());
		getBillCardPanel().setTailItem("coperatorid",_getOperator());
		getBillCardPanel().setTailItem("tmaketime",_getServerTime());
		getBillCardPanel().setHeadItem("geh_dbilldate",_getDate());	
		
		
	}

	public boolean beforeEdit(BillItemEvent e) {
		String key=e.getItem().getKey();
		//对入库仓库过滤，过滤只属于物流的仓库
		if("geh_cwarehouseid".equalsIgnoreCase(key)){
			JComponent c =getBillCardPanel().getHeadItem("geh_cwarehouseid").getComponent();
			if( c instanceof UIRefPane){
				UIRefPane ref = (UIRefPane)c;
				ref.getRefModel().addWherePart(" and def1 = '1' and isnull(dr,0) = 0");
			}
			return true;
		}
		//对入库货位过滤，过滤只属于对应的入库仓库下面的货位
		if("pk_cargdoc".equalsIgnoreCase(key)){
			String pk_store=(String) getBillCardPanel().getHeadItem("geh_cwarehouseid").getValueObject();
			if(null==pk_store || "".equalsIgnoreCase(pk_store)){
				showWarningMessage("请选择入库仓库");
				return false;
			}			
			JComponent c =getBillCardPanel().getHeadItem("pk_cargdoc").getComponent();
			if( c instanceof UIRefPane){
				UIRefPane ref = (UIRefPane)c;
				ref.getRefModel().addWherePart("  and bd_cargdoc.pk_stordoc='"+pk_store+"' and isnull(bd_cargdoc.dr,0) = 0");
			}
			return true;			
		}
		//对当前货位下的管理员进行过滤
		if("geh_cwhsmanagerid".equalsIgnoreCase(key)){
			String pk_cargdoc=(String) getBillCardPanel().getHeadItem("pk_cargdoc").getValueObject();
			if(null==pk_cargdoc || "".equalsIgnoreCase(pk_cargdoc)){
				showWarningMessage("前选择入库货位");
				return false;
			}			
			JComponent c =getBillCardPanel().getHeadItem("geh_cwhsmanagerid").getComponent();
			if( c instanceof UIRefPane){
				UIRefPane ref = (UIRefPane)c;
				ref.getRefModel().addWherePart("  and tb_stockstaff.pk_cargdoc='"+pk_cargdoc+"' ");
			}
			return true;		
		}
		return true;
	}
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();				
		if (e.getPos() == BillItem.BODY) {	
			//在班组信息中   通过表头的仓库对班组进行参照过滤
			if("teamcode".equals(key)){//班组
				//仓库id
				Object a = getBillCardPanel().getHeadItem("geh_cwarehouseid").getValueObject();
				if(a==null){
					showWarningMessage("请选择仓库");
					return false;
				}
				UIRefPane panel = (UIRefPane) this.getBillCardPanel().getBodyItem("teamcode").getComponent();
				if (null != a && !"".equals(a)) {
					//修改参照 条件 增加条件 指定仓库id
					panel.getRefModel().addWherePart(" and wds_teamdoc_h.vdef1 = '"+a+"' ");
				}
			}
		}
		return true;
	}
	@Override
	public String getRefBillType() {
		return "4Y";
	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
		String key = e.getKey();				
		if (e.getPos() == BillItem.BODY) {	
//			if("geb_bsnum".equals(key)){
//				UFDouble old=PuPubVO.getUFDouble_NullAsZero(e.getOldValue());
//				UFDouble nv=PuPubVO.getUFDouble_NullAsZero(e.getValue());
//				if(nv.doubleValue()>old.doubleValue()){
//				//	this.showErrorMessage("应出数量  比原先  应出数量大");
//					getBillCardPanel().getBillModel().setValueAt(old, e.getRow(), key);
//					getBillCardPanel().getBillModel().execEditFormulaByKey(e.getRow(), key);					
//				}			
//			}
			
		}
		super.afterEdit(e);
	}

}

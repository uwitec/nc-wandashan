package nc.ui.wds.ic.cargtray;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.WdsBillManagUI;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.wl.pub.LoginInforVO;

/**
 * 	货位托盘信息
 */
public class MyClientUI extends WdsBillManagUI implements
		nc.ui.pub.bill.BillEditListener2,BillCardBeforeEditListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
		getBillCardPanel().addBodyEditListener2(this);
//		 getBillCardPanel().addBillEditListenerHeadTail(billEditListener)
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	
	}

	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}

	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	@Override
	public Object getUserObject() {
		
		return new ClientGetCheckClass();
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void initSelfData() {
		 ButtonObject btn=getButtonManager().getButton(IBillButton.Line);
		 btn.removeChildButton(getButtonManager().getButton(IBillButton.CopyLine));
		 btn.removeChildButton(getButtonManager().getButton(IBillButton.PasteLine));
		 btn.removeChildButton(getButtonManager().getButton(IBillButton.InsLine));
	}

	public void setDefaultData() throws Exception {
		  //按照 当前操作人绑定的仓库和货位赋值
		LoginInforVO login=getLoginInforHelper().getLogInfor(_getOperator());
		getBillCardWrapper().getBillCardPanel().getHeadItem("pk_stordoc").setValue(login.getWhid());
		getBillCardWrapper().getBillCardPanel().getHeadItem("pk_cargdoc").setValue(login.getSpaceid());
		getBillCardWrapper().getBillCardPanel().getHeadItem("pk_corp").setValue(getCorpPrimaryKey());
		
	}

	@Override
	public void afterEdit(BillEditEvent e) {
//		if (e.getKey().equals("invname")) {
//			getBillCardPanel().execHeadEditFormulas();
//		}
		super.afterEdit(e);
	}
	public boolean beforeEdit(BillEditEvent e){
//		if (getBillCardPanel().isEnabled() == false) {
//			return false;
//		}
//		// 这里如果单据上选择了客户，参选合同时，需要按客户过滤经销合同
//		if ("invname".equals(e.getKey())) {
//			String pk_cargdoc = (String) this.getBillCardPanel().getHeadItem(
//					"pk_cargdoc").getValueObject();
//			if (pk_cargdoc != null && pk_cargdoc.length() > 0) {
//				// 得到合同参照
//				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
//						.getBodyItem("invname").getComponent();
//				// this.getBillCardPanel()
//				// .getHeadItem("cif_pk").setDecimalDigits(iDecimalDigits);
//				// 加上客户做为条件去过滤
//				panel
//						.getRefModel()
//						.setWherePart(
//								" pk_invbasdoc in (select pk_invbasdoc from tb_spacegoods where dr = 0 and pk_cargdoc = '"
//										+ pk_cargdoc.toString() + "') and dr=0 ");
//			}
//		}
		//项目主键	invcode
		if("invcode".equalsIgnoreCase(e.getKey())){
		
			String pk_cargdoc=(String) getBillCardPanel().getHeadItem("pk_cargdoc").getValueObject();
			//得到参照
			UIRefPane panel=(UIRefPane) getBillCardPanel().getBodyItem("invcode").getComponent();
			panel.getRefModel().addWherePart(" and tb_spacegoods.pk_cargdoc ='"+pk_cargdoc+"'");
						
		}
		
		return true;
	}

	protected AbstractManageController createController() {
		return new MyClientUICtrl();
	}
	
	/**
	 * 如果单据不走平台时，UI类需要重载此方法，返回不走平台的业务代理类 
	 * @return BusinessDelegator 不走平台的业务代理类
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new nc.ui.wds.ic.cargtray.MyDelegator();
	}
	public boolean beforeEdit(BillItemEvent e) {
//		if (getBillCardPanel().isEnabled() == false) {
//			return false;
//		}
//		// 这里如果单据上选择了客户，参选合同时，需要按客户过滤经销合同
//		if ("invname".equals(e.getItem().getKey())) {
//			String pk_cargdoc = (String) this.getBillCardPanel().getHeadItem(
//					"pk_cargdoc").getValueObject();
//			if (pk_cargdoc != null && pk_cargdoc.length() > 0) {
//				// 得到合同参照
//				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
//						.getHeadItem("invname").getComponent();
//				// this.getBillCardPanel()
//				// .getHeadItem("cif_pk").setDecimalDigits(iDecimalDigits);
//				// 加上客户做为条件去过滤
//				panel
//						.getRefModel()
//						.setWherePart(
//								" pk_invbasdoc in (select pk_invbasdoc from tb_spacestock where dr = 0 and pk_cargdoc = '"
//										+ pk_cargdoc.toString() + "') and dr=0 ");
//			} else {
//
//				this.showErrorMessage("您没有选择货位，将不显示信息");
//				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
//						.getHeadItem("invname").getComponent();
//				// 加上客户做为条件去过滤
//				panel.getRefModel().setWherePart(" dr=0 and 1=2  ");
//			}
//		}
		
		//仓库过滤，只属于物流系统的
		
//		String key = e.getItem().getKey();
//		if(e.getItem().getPos() ==BillItem.HEAD){
//			//仓库过滤，只属于物流系统的
//			if("pk_stordoc".equalsIgnoreCase(key)){
//				JComponent c =getBillCardPanel().getHeadItem("pk_stordoc").getComponent();
//				if( c instanceof UIRefPane){
//					UIRefPane ref = (UIRefPane)c;
//					ref.getRefModel().addWherePart(" and def1 = '1' and isnull(dr,0) = 0");
//				}
//			}
//		}else if(e.getItem().getPos() ==BillItem.BODY){}
		
		return true;
	}

}

package nc.ui.wds.ic.invstore;

import javax.swing.JComponent;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.WdsBillManagUI;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.wl.pub.LoginInforVO;


/**
 *    存货货位绑定
 */
 public class MyClientUI extends WdsBillManagUI implements BillCardBeforeEditListener {
       
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
    
	@Override
	protected void initEventListener() {
		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}



	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

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
		getBillCardWrapper().getBillCardPanel().getHeadItem("pk_psndoc").setValue(_getOperator());		
	}
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		if("sgcode".equalsIgnoreCase(key)){
			Object pk_stordoc = getBillCardPanel().getHeadItem("pk_stordoc").getValueObject();
			if(pk_stordoc == null ){
				showWarningMessage("请先绑定仓库货位");
				return false;
			}
			JComponent jc = getBillCardPanel().getBodyItem("sgcode").getComponent();
			if(jc instanceof UIRefPane){
				UIRefPane ref =(UIRefPane)jc;
				ref.getRefModel().addWherePart(" and wds_invbasdoc.pk_invmandoc not in ( select pk_invmandoc from tb_spacegoods " +
						" where isnull(dr,0)=0 and pk_storedoc ='"+pk_stordoc+" ' and pk_invmandoc is not null)");
			}
		}
		return super.beforeEdit(e);
	}
	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
	}

	public boolean beforeEdit(BillItemEvent e) {
		String key = e.getItem().getKey();
		if(e.getItem().getPos() ==BillItem.HEAD){
			//仓库过滤，只属于物流系统的
			if("pk_stordoc".equalsIgnoreCase(key)){
				JComponent c =getBillCardPanel().getHeadItem("pk_stordoc").getComponent();
				if( c instanceof UIRefPane){
					UIRefPane ref = (UIRefPane)c;
					ref.getRefModel().addWherePart("  and def1 = '1' and isnull(dr,0) = 0");
				}
			}
			if (e.getItem().getKey().equals("pk_cargdoc")) {
				Object a = getBillCardPanel().getHeadItem("pk_stordoc").getValueObject();
				UIRefPane panel = (UIRefPane) this.getBillCardPanel().getHeadItem("pk_cargdoc").getComponent();
				if (null != a && !"".equals(a)) {
					panel.getRefModel().addWherePart(" and bd_cargdoc.pk_stordoc='" + a + "'");
				} else {
					showErrorMessage("请先选择仓库信息!");
					return false;
				}
			}
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
		return new nc.ui.wds.ic.invstore.MyDelegator();
	}
	
	@Override
	public Object getUserObject() {
		// TODO Auto-generated method stub
		return super.getUserObject();
	}
}

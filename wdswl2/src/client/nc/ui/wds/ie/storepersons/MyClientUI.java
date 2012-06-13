package nc.ui.wds.ie.storepersons;

import javax.swing.JComponent;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 *
 * 仓库人员绑定
 * 
 */
public class MyClientUI extends BillManageUI implements BillCardBeforeEditListener {

	
	private static final long serialVersionUID = -2077419730202716540L;

	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
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

	protected void initSelfData() {
		
	}

	public void setDefaultData() throws Exception {
		getBillCardPanel().getHeadItem("st_date").setValue(_getDate());//操作日期
		getBillCardPanel().getHeadItem("st_pot").setValue(_getOperator());//操作人
		getBillCardPanel().getHeadItem("pk_corp").setValue(_getCorp().getPrimaryKey());//公司
	}

	@Override
	protected void initEventListener() {
		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}

	public boolean beforeEdit(BillItemEvent e) {
		String key=e.getItem().getKey();
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
		}else if(e.getItem().getPos() ==BillItem.BODY){}
		
		return true;
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		if (e.getKey().equals("pk_stordoc")) {//仓库
			getBillCardPanel().setHeadItem("pk_cargdoc", null);//货位
		}
	}

	@Override
	protected AbstractManageController createController() {
		return new MyClientUICtrl();
	}

	@Override
	public Object getUserObject() {
		
		return new GetCheck();
	}
	
	
}

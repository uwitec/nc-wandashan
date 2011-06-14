package nc.ui.wds.dm.store;

import javax.swing.JComponent;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;


/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     分仓区域绑定
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
 public class MyClientUI extends AbstractMyClientUI implements BillCardBeforeEditListener{
       
       protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
       
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

	protected void initSelfData() {
	   getBillCardWrapper().getBillCardPanel().addBillEditListenerHeadTail(this);
	 
		
	}

	public void setDefaultData() throws Exception {
	}
	
	
	
	
	



	public boolean beforeEdit(BillItemEvent e) {
		String key = e.getItem().getKey();
		if(e.getItem().getPos() ==BillItem.HEAD){
			//仓库过滤，只属于物流系统的
			if("pk_stordoc".equalsIgnoreCase(key)){
				JComponent c =getBillCardPanel().getHeadItem("pk_stordoc").getComponent();
				if( c instanceof UIRefPane){
					UIRefPane ref = (UIRefPane)c;
					ref.getRefModel().addWherePart(" and def1 = '1' and isnull(dr,0) = 0");
				}
			}
		}else if(e.getItem().getPos() ==BillItem.BODY){};	
		
		
		return true;
	}
	
	
	@Override
	protected void initEventListener() {
		
		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		
	
		return super.beforeEdit(e);
	}

	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		if(e.getKey().equals("pk_areacl")){
			getBillCardPanel().execHeadEditFormulas();
		}
		if(e.getKey().equals("pk_stordoc")){
			getBillCardPanel().execHeadEditFormulas();
		}
		super.afterEdit(e);
	}



}

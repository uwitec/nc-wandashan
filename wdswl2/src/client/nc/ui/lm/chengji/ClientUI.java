package nc.ui.lm.chengji;
import javax.swing.JComponent;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.WdsWlPubConst;
public class ClientUI extends BillManageUI implements BillCardBeforeEditListener{
	private static final long serialVersionUID = 6714332441173414659L;
	@Override
	protected AbstractManageController createController() {
		
		return new Control();
	}
	@Override
	protected ManageEventHandler createEventHandler() {	
		return new EventHandler(this,getUIControl());
	}
	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {		
	}
	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {		
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {		
	}
	/**
	 * 初始化ui类的监听器
	 */
	@Override
	protected void initEventListener() {	
		super.initEventListener();
		//给当前ui类 注册一个   表头编辑前事件的监听器
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}
	@Override
	protected String getBillNo() throws Exception {
		return HYPubBO_Client.getBillNo(WdsWlPubConst.LM_CHENGJI_BILLTYPE,_getOperator(),null, null);
	}
	@Override
	protected void initSelfData() {
		
	}
	/**
	 * 单据新增按钮的时候 给表单赋默认值
	 */
	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().getHeadItem("pk_corp").setValue(getCorpPrimaryKey());	
		getBillCardPanel().getHeadItem("pk_billtype").setValue(WdsWlPubConst.LM_CHENGJI_BILLTYPE);
		getBillCardPanel().setHeadItem("vbillstatus",IBillStatus.FREE);//单据状态
		getBillCardPanel().getHeadItem("dbilldate").setValue(_getDate());
		getBillCardPanel().getTailItem("voperatorid").setValue(_getOperator());
		
	}
	public boolean beforeEdit(BillItemEvent e) {
		String key=e.getItem().getKey();
		if("pk_stu".equalsIgnoreCase(key)){
			Object o=getBillCardPanel().getHeadItem("pk_class").getValueObject();
			if(o==null){
				this.showErrorMessage(" 请选择班级");
				return false;
			}
			String s=(String)o;
			if(s.trim().length()==0){
				this.showErrorMessage(" 请选择班级");
				return false;
			}
			JComponent c =getBillCardPanel().getHeadItem("pk_stu").getComponent();
			if( c instanceof UIRefPane){
				UIRefPane ref = (UIRefPane)c;
				ref.getRefModel().addWherePart(" and lm_class_h.pk_class='"+s+"'");
			}
			return true;		
		}
		if("pk_class".equalsIgnoreCase(key)){
			getBillCardPanel().getHeadItem("pk_stu").setValue(null);		
		}	    
		return false;
	}
}

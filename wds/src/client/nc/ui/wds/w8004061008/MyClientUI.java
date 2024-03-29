package nc.ui.wds.w8004061008;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.trade.bill.IListController;
import nc.ui.trade.list.BillListUI;
import nc.ui.trade.list.ListEventHandler;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */

public class MyClientUI extends AbstractMyClientUI implements
	ListSelectionListener {

    protected ListEventHandler createEventHandler() {
	return new MyEventHandler(this, getUIControl());
    }

    public String getRefBillType() {
	return null;
    }

    @Override
    protected void initEventListener() {
	// TODO Auto-generated method stub
	super.initEventListener();
	getBillListPanel().getHeadTable().getSelectionModel()
		.addListSelectionListener(this);
    }

    @Override
    protected String getInitDataWhere() {
	// TODO Auto-generated method stub
	return " pk_invcl in"
		+ " (select pk_invcl from bd_invcl where invclasscode like '30101%')"
		+ " and def14=0 or def14=1";
    }

    /**
     * 修改此方法初始化模板控件数据
     */

    protected void initSelfData() {

    }

    public void setDefaultData() throws Exception {
    }

    /**
     * 修改此方法增加后台校验
     */
    public Object getUserObject() {
	return null;
    }

    public void valueChanged(ListSelectionEvent arg0) {
	// TODO Auto-generated method stub

    }

}

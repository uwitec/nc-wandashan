package nc.ui.wds.ie.storedetail;


import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.trade.list.ListEventHandler;

/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * 
 * <p>
 * 	�����ϸ
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
		+ " (select pk_invcl from bd_invcl where invclasscode like '30101%') ";
    }

    /**
     * �޸Ĵ˷�����ʼ��ģ��ؼ�����
     */

    protected void initSelfData() {

    }

    public void setDefaultData() throws Exception {
    }

    /**
     * �޸Ĵ˷������Ӻ�̨У��
     */
    public Object getUserObject() {
	return null;
    }

    public void valueChanged(ListSelectionEvent arg0) {
	// TODO Auto-generated method stub

    }

}

package nc.ui.wds.w9000;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.trade.list.ListEventHandler;

/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 *
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */

public class MyClientUI extends AbstractMyClientUI implements ListSelectionListener {
	
	protected ListEventHandler createEventHandler() {
	       return new MyEventHandler(this, getUIControl());
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
		return " invmnecode like '%C%'";
	}
	
	public String getRefBillType() {
		return null;
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

	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

}


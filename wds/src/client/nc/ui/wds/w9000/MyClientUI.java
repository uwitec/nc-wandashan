package nc.ui.wds.w9000;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.trade.list.ListEventHandler;

/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
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

	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

}


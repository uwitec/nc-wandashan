package nc.ui.wl.pub.report;

import nc.ui.pub.*;
import java.util.*;
/**
 * 此处插入类型说明。
 * 创建日期：(2004-8-19 9:31:41)
 * @author：刘建波
 */
abstract public class ReportUIManager extends nc.ui.pub.ToftPanel implements IMsgProxy {
	private java.util.Stack m_stkPanel = new java.util.Stack();
	private nc.ui.pub.ToftPanel  m_pnlCurr=null;
	private Hashtable m_hReceiver=new Hashtable();
	private nc.ui.pub.ButtonObject boReturn = new nc.ui.pub.ButtonObject("返回", "返回", 1);
/**
 * ReportUIManager 构造子注解。
 */
public ReportUIManager() {
	super();
	initialize();
}
private ButtonObject[] getCurrButtons() {
	ButtonObject[] buttons = m_pnlCurr.getButtons();
	if (buttons == null)
		return new ButtonObject[] { boReturn };
	if (m_stkPanel.size() > 0) {
		ButtonObject[] bos = new ButtonObject[buttons.length + 1];
		System.arraycopy(buttons, 0, bos, 0, buttons.length);
		bos[bos.length - 1] = boReturn;
		return bos;
	} else
		return buttons;

}
/**
 * 此处插入方法说明。
 * 创建日期：(2004-11-4 9:32:53)
 * @return nc.ui.pub.ToftPanel
 */
public ToftPanel getCurrUI() {
	return m_pnlCurr;
}
/**
*返回第一个类的名称。
*/
abstract public String getFirstClassName();
private IReceiver getReceiveByClassName(String classname) throws Exception {
	if (m_hReceiver.get(classname) == null) {
		Object o = Class.forName(classname).newInstance();
		if (o instanceof IReceiver)
			m_hReceiver.put(classname, o);
		else
			throw new Exception("接受的对象没有实现IReceiver接口！");
	}
	return (IReceiver) m_hReceiver.get(classname);
}
public String getTitle() {
	return m_pnlCurr.getTitle();
}
private void initialize() {
	try {
		setName("reportManager");
		//*****************
		java.awt.GridLayout layout = new java.awt.GridLayout();
		layout.setColumns(1);
		layout.setRows(1);
		setLayout(layout);
		//*****************
		setSize(774, 419);
		//load the first Panel.
		Object o = Class.forName(getFirstClassName()).newInstance();
		m_pnlCurr = (ToftPanel) o;
		showCurrPanel();
		//register the proxy to Sender.
		if (m_pnlCurr instanceof ISender) {
			ISender sender = (ISender) m_pnlCurr;
			sender.registerMsgProxy(this);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
}
/**
 * 子类实现该方法，响应按钮事件。
 * @version (00-6-1 10:32:59)
 * 
 * @param bo ButtonObject
 */
public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
	if (bo == boReturn)
		onReturn();
	else
		m_pnlCurr.onButtonClicked(bo);
}
private void onReturn() {
	m_pnlCurr = (nc.ui.pub.ToftPanel) m_stkPanel.pop();
	showCurrPanel();
}
/**
 * sendMsg 方法注解。
 */
public Object sendMsg(ISender sender, String receiverClassName, Object msg) throws Exception {
	//send message to receiver.
	IReceiver receiver = getReceiveByClassName(receiverClassName);
	Object o = receiver.receiverMsg(this, sender, msg);
	if (receiver instanceof ISender)
		 ((ISender) receiver).registerMsgProxy(this);
	//put sender to stack.
	m_stkPanel.push(sender);
	//show Receiver.
	if (!(receiver instanceof nc.ui.pub.ToftPanel))
		throw new Exception("receiver应继承ToftPanel!");
	m_pnlCurr = (nc.ui.pub.ToftPanel) receiver;
	showCurrPanel();
	//return message from sender.
	return o;
}
/**
 * sendMsg 方法注解。
 */
public Object sendMsg(ISender sender, IReceiver receiver, Object msg) throws Exception {
	//send message to receiver.
	Object o = receiver.receiverMsg(this, sender, msg);
	if (receiver instanceof ISender)
		 ((ISender) receiver).registerMsgProxy(this);
	//put sender to stack.
	m_stkPanel.push(sender);
	//show Receiver.
	if (!(receiver instanceof nc.ui.pub.ToftPanel))
		throw new Exception("receiver应继承ToftPanel!");
	m_pnlCurr = (nc.ui.pub.ToftPanel) receiver;
	showCurrPanel();
	//return message from sender.
	return o;
}
private void showCurrPanel() {
	try {
		removeAll();
		add(m_pnlCurr, m_pnlCurr.getName());
		m_pnlCurr.setFrame(super.getFrame());
		setButtons(getCurrButtons());
		try {
			setTitleText(m_pnlCurr.getTitle());
		} catch (Exception e){
		}
		this.validate();
		this.repaint();
	} catch (Throwable e) {
		e.printStackTrace();
	}
}
}

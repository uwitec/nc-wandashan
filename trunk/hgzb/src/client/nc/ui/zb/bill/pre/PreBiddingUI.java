package nc.ui.zb.bill.pre;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.IFuncWindow;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIDialog;

public class PreBiddingUI extends ToftPanel{

	private UIDialog m_dlg = null;
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public PreBiddingUI(){
		super();
		init();
	}
	
	 /**
 *     检查打开该节点的前提条件。用于处理“只有满足某一条件时，才能打开该节点”
 * 的情况。
 *     需要判断“只有满足某一条件时，才能打开该节点”的节点，需要实现本方法。
 * 在方法内进行条件判断。
 *     基类根据返回值进行相应处理，如果返回值为一个非空字符串，那么基类不打开
 * 该节点，只在一个对话框中显示返回的字符串；如果返回值为null，那么基类象对待
 * 其他节点一样打开该节点。
 * 
 * return  IFuncWindow.DONTSHOWFRAME 不提示  不打开节点  详见：nc.ui.pub.FuncNodeStarter.openImpl(Bag bag)

	 */
	public String checkPrerequisite(){
		return IFuncWindow.DONTSHOWFRAME;
	}
	
	private UIDialog getDlg(){
		if(m_dlg == null){
			m_dlg = new PreBiddingDLG(this);
		}
		return m_dlg;
	}
	
	private void init(){
		getDlg().showModal();
	}

	@Override
	public void onButtonClicked(ButtonObject arg0) {
		// TODO Auto-generated method stub

	}

}

package nc.ui.zb.bidding.adjust;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.IFuncWindow;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIDialog;

public class AdjustBiddingUI extends ToftPanel {

	private UIDialog m_dlg = null;
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public AdjustBiddingUI(){
		super();
		init();
	}
	
	 /**
 *     ���򿪸ýڵ��ǰ�����������ڴ���ֻ������ĳһ����ʱ�����ܴ򿪸ýڵ㡱
 * �������
 *     ��Ҫ�жϡ�ֻ������ĳһ����ʱ�����ܴ򿪸ýڵ㡱�Ľڵ㣬��Ҫʵ�ֱ�������
 * �ڷ����ڽ��������жϡ�
 *     ������ݷ���ֵ������Ӧ�����������ֵΪһ���ǿ��ַ�������ô���಻��
 * �ýڵ㣬ֻ��һ���Ի�������ʾ���ص��ַ������������ֵΪnull����ô������Դ�
 * �����ڵ�һ���򿪸ýڵ㡣
 * 
 * return  IFuncWindow.DONTSHOWFRAME ����ʾ  ���򿪽ڵ�  �����nc.ui.pub.FuncNodeStarter.openImpl(Bag bag)

	 */
	public String checkPrerequisite(){
		return IFuncWindow.DONTSHOWFRAME;
	}
	
	private UIDialog getDlg(){
		if(m_dlg == null){
			m_dlg = new AdjustBiddingDLG(this);
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

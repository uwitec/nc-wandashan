package nc.ui.zb.bill.pre;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.zb.pub.LongTimeTask;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.zb.pub.ZbPubTool;

/**
 * �������׼���Ի���
 * @author zhf
 * 
 *
 */

public class PreBiddingDLG extends UIDialog implements ActionListener{
	private static final long serialVersionUID = -39986234234258916L;
	
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	private PreBidListToListPanel listPane = null;
	
	private nc.ui.pub.beans.UIPanel ivjPnButton = null;
	private nc.ui.pub.beans.UIButton ivjBnCancel = null;
	private nc.ui.pub.beans.UIButton ivjBnOk = null;
	
	private ToftPanel parent = null;
	
	
	public PreBiddingDLG(ToftPanel tp) {
		super(tp);
		this.parent = tp;
		init();
	}
	
	private void init() {
		setSize(610,400);
		setTitle("�������׼��");
		setContentPane(getUIDialogContentPane());		
		
		getBnOk().addActionListener(this);
		getBnCancel().addActionListener(this);
	}
	
	private javax.swing.JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new javax.swing.JPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new java.awt.BorderLayout());
				getUIDialogContentPane().add(getPnButton(), "South");
				getUIDialogContentPane().add(getBillListPanel(), "Center");
			} catch (java.lang.Throwable ivjExc) {
			}
		}
		return ivjUIDialogContentPane;
	}
	
	public PreBidListToListPanel getBillListPanel() {
		if (listPane == null) {
			listPane = new PreBidListToListPanel();
		}
		return listPane;
	}
	
	
	
	/**
	 * ���� PnButton ����ֵ.
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ����:�˷�������������. */
	private nc.ui.pub.beans.UIPanel getPnButton() {
		if (ivjPnButton == null) {
			try {
				ivjPnButton = new nc.ui.pub.beans.UIPanel();
				ivjPnButton.setName("PnButton");
				getPnButton().add(getBnOk(), getBnOk().getName());	
				getPnButton().add(getBnCancel(), getBnCancel().getName());
			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjPnButton;
	}
	
	

	/**
	 * ���� BnOk ����ֵ.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ����:�˷�������������. */
	nc.ui.pub.beans.UIButton getBnOk() {
		if (ivjBnOk == null) {
			try {
				ivjBnOk = new nc.ui.pub.beans.UIButton();
				ivjBnOk.setName("BnOk");
				ivjBnOk.setText("ȷ��");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnOk;
	}
	
	/**
	 * ���� BnCancel ����ֵ.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ����:�˷�������������. */
	nc.ui.pub.beans.UIButton getBnCancel() {
		if (ivjBnCancel == null) {
			try {
				ivjBnCancel = new nc.ui.pub.beans.UIButton();
				ivjBnCancel.setName("BnCancel");
				ivjBnCancel.setText("ȡ��");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnCancel;
	}	

	
	void showErrorMessage(String msg){
		parent.showErrorMessage(msg);
	}
	void showWarningMessage(String msg){
		parent.showWarningMessage(msg);
	}
	void showHintMessage(String msg){
		parent.showHintMessage(msg);
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try{
			if(e.getSource()==getBnOk()){
				onOK();
			}else if(e.getSource() == getBnCancel()){
				onCancel();
			}
		}catch(Exception ee){
			ee.printStackTrace();
			MessageDialog.showErrorDlg(this, "����", ZbPubTool.getString_NullAsTrimZeroLen(ee.getMessage()));
		}
	}
	
    private void onOK() throws Exception{
//    	����״̬��̨����
    	SuperVO[] vos = getBillListPanel().getRightListItems();
    	if(vos == null || vos.length == 0){
    		throw new ValidationException("��ѡ���������׶εı��");
    	}
    	
    	Class[] ParameterTypes = new Class[]{SuperVO[].class};
		Object[] ParameterValues = new Object[]{vos};
		LongTimeTask.
		calllongTimeService("pu", parent, "���ڻ��б���Ϣ...", 1, "nc.bs.zb.bill.pre.PreBiddingBO", null, "dealBidding", ParameterTypes, ParameterValues);
	
		closeOK();
	}
	private void onCancel() throws Exception{
		closeCancel();
	}
}

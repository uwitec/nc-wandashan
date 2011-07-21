package nc.ui.hg.to.pub;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.vo.hg.pu.pub.HgPubConst;
/**
 * 八大量参数对话框
 * @author zhw
 *
 */
public class EightParametersDialog extends UIDialog implements ActionListener  {
	private static final long serialVersionUID = -39986234234258916L;
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	private BillCardPanel cardPane = null;
	
	private nc.ui.pub.beans.UIPanel ivjPnButton = null;
	private nc.ui.pub.beans.UIButton ivjBnCancel = null;
//	private nc.ui.pub.beans.UIButton ivjBnOk = null;
	
	private ToftPanel parent = null;
	
//	private  ClientEnvironment ce=null;
	
	public EightParametersDialog(ToftPanel tp) {
		super(tp);
		this.parent = tp;
		init();
		initDataPanel();
	}
	
	public void initDataPanel(){
	}
	
	private void init() {
		setSize(500, 500);
		setTitle("六个参数");
		setContentPane(getUIDialogContentPane());
//		getBnOk().addActionListener(this);
		getBnCancel().addActionListener(this);
	}
	
	private javax.swing.JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new javax.swing.JPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new java.awt.BorderLayout());
				getUIDialogContentPane().add(getPnButton(), "South");
				getUIDialogContentPane().add(getBillCardPanel(), "Center");
			} catch (java.lang.Throwable ivjExc) {
			}
		}
		return ivjUIDialogContentPane;
	}
	
	public BillCardPanel getBillCardPanel() {
		if (cardPane == null) {
			cardPane = new BillCardPanel();
			cardPane.loadTemplet(HgPubConst.CHECK_TEMP_ID);
		}
		return cardPane;
	}
	
	/**
	 * 返回 PnButton 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告:此方法将重新生成. */
	private nc.ui.pub.beans.UIPanel getPnButton() {
		if (ivjPnButton == null) {
			try {
				ivjPnButton = new nc.ui.pub.beans.UIPanel();
				ivjPnButton.setName("PnButton");
//				getPnButton().add(getBnOk(), getBnOk().getName());	
				getPnButton().add(getBnCancel(), getBnCancel().getName());

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjPnButton;
	}

	/**
	 * 返回 BnOk 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告:此方法将重新生成. */
//	private nc.ui.pub.beans.UIButton getBnOk() {
//		if (ivjBnOk == null) {
//			try {
//				ivjBnOk = new nc.ui.pub.beans.UIButton();
//				ivjBnOk.setName("BnOk");
//				ivjBnOk.setText("确定");
//
//			} catch (java.lang.Throwable ivjExc) {
//
//			}
//		}
//		return ivjBnOk;
//	}

	/**
	 * 返回 BnCancel 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告:此方法将重新生成. */
	private nc.ui.pub.beans.UIButton getBnCancel() {
		if (ivjBnCancel == null) {
			try {
				ivjBnCancel = new nc.ui.pub.beans.UIButton();
				ivjBnCancel.setName("BnCancel");
				ivjBnCancel.setText("取消");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnCancel;
	}
	
	public void actionPerformed(ActionEvent bo) {
		try{
//			if (bo.getSource() == getBnOk()) {
//				
//				onOK();
//				this.closeOK();
//			} else 
			if (bo.getSource() == getBnCancel()) {
				this.closeCancel();
			}
		}catch(Exception e){
			showErrorMsg(e.getMessage());
			e.printStackTrace();
		}
	}
	
//	private void onOK() throws Exception{
//		
//	}
	private void showErrorMsg(String msg){
		MessageDialog.showErrorDlg(parent, "", msg==null?"操作出现异常，请重新操作":msg);
	}
}

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
 * 议标数据准备对话框
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
		setTitle("议标数据准备");
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
				getPnButton().add(getBnOk(), getBnOk().getName());	
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
	nc.ui.pub.beans.UIButton getBnOk() {
		if (ivjBnOk == null) {
			try {
				ivjBnOk = new nc.ui.pub.beans.UIButton();
				ivjBnOk.setName("BnOk");
				ivjBnOk.setText("确定");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnOk;
	}
	
	/**
	 * 返回 BnCancel 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告:此方法将重新生成. */
	nc.ui.pub.beans.UIButton getBnCancel() {
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
			MessageDialog.showErrorDlg(this, "错误", ZbPubTool.getString_NullAsTrimZeroLen(ee.getMessage()));
		}
	}
	
    private void onOK() throws Exception{
//    	标书状态后台处理
    	SuperVO[] vos = getBillListPanel().getRightListItems();
    	if(vos == null || vos.length == 0){
    		throw new ValidationException("请选择进入评标阶段的标段");
    	}
    	
    	Class[] ParameterTypes = new Class[]{SuperVO[].class};
		Object[] ParameterValues = new Object[]{vos};
		LongTimeTask.
		calllongTimeService("pu", parent, "正在获招标信息...", 1, "nc.bs.zb.bill.pre.PreBiddingBO", null, "dealBidding", ParameterTypes, ParameterValues);
	
		closeOK();
	}
	private void onCancel() throws Exception{
		closeCancel();
	}
}

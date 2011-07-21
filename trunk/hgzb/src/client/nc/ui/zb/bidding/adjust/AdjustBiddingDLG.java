package nc.ui.zb.bidding.adjust;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.zb.pub.LongTimeTask;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.zb.bidding.SmallBiddingBodyVO;
import nc.vo.zb.pub.ZbPubTool;

/**
 * �������׼���Ի���
 * @author zhf
 * 
 *
 */

public class AdjustBiddingDLG extends UIDialog implements ActionListener{
	private static final long serialVersionUID = -39986234234258916L;
	
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	private AdjustBidListToListPanel listPane = null;
	
	private nc.ui.pub.beans.UIPanel ivjPnButton = null;
	private nc.ui.pub.beans.UIButton ivjBnCancel = null;
	private nc.ui.pub.beans.UIButton ivjBnOk = null;
	
	private ToftPanel parent = null;
	
	
	public AdjustBiddingDLG(ToftPanel tp) {
		super(tp);
		this.parent = tp;
		init();
	}
	
	private void init() {
		setSize(610,400);
		setTitle("��ε���");
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
	
	public AdjustBidListToListPanel getBillListPanel() {
		if (listPane == null) {
			listPane = new AdjustBidListToListPanel(parent);
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
//    	�����������
    	SuperVO[] left = getBillListPanel().getLeftListItems();
//    	�Ҳ���������
    	SuperVO[] right = getBillListPanel().getRightListItems();
    	
    	if((left == null || left.length == 0)&&(right == null || right.length == 0)){
    		MessageDialog.showErrorDlg(this, "����", "��ȡ����Ϊ��");
    		return;
    	}
    	
        if(getBillListPanel().getLeftBiddingID().equalsIgnoreCase(getBillListPanel().getRightBiddingID())){
        	MessageDialog.showErrorDlg(this, "����", "ͬһ��μ䲻�ܵ���");
    		return;
    	}
    	
    	if((left == null || left.length == 0)){
    		if(MessageDialog.showOkCancelDlg(this, "ѯ��", "����α��ε��������б�Ʒ��,�Ƿ�ȷ��?")!=UIDialog.ID_OK)
    		return;
    	}
    	
       
    	
    	if((right == null || right.length == 0)){
    		if(MessageDialog.showOkCancelDlg(this, "ѯ��", "�Ҳ��α��ε��������б�Ʒ��,�Ƿ�ȷ��?")!=UIDialog.ID_OK)
    		return;
    	}
    	
    	SmallBiddingBodyVO[] bodysleft = new SmallBiddingBodyVO[left.length];
    	int index = 0;
    	for(SuperVO vo:left){
    		bodysleft[index] = (SmallBiddingBodyVO)vo;
    		index ++;
    	}
    	
    	SmallBiddingBodyVO[] bodysright = new SmallBiddingBodyVO[right.length];
    	index = 0;
    	for(SuperVO vo:right){
    		bodysright[index] = (SmallBiddingBodyVO)vo;
    		index ++;
    	}
    	
    	
    	
//    	SmallBiddingBodyVO[] bodysleft = (SmallBiddingBodyVO[])left;
//    	SmallBiddingBodyVO[] bodysright = (SmallBiddingBodyVO[])right;
    	SmallBiddingBodyVO.checkIsInClass(bodysleft);//У���Ƿ�ͬһ����
    	SmallBiddingBodyVO.checkIsInClass(bodysright);
    	
//    	java.util.List lpara = new ArrayList();
//    	/**
//    	 * 0 -- left new data
//    	 * 1 -- right new data
//    	 * 2 -- operator ����Ա
//    	 */
//    	lpara.add(left);
//    	lpara.add(right);
//    	lpara.add(ClientEnvironment.getInstance().getUser().getPrimaryKey());
//    	lpara.add(ClientEnvironment.getInstance().getDate());
//    	lpara.add(left[0].getPrimaryKey());
//    	lpara.add(right[0].getPrimaryKey());
    	Class[] ParameterTypes = new Class[]{SuperVO[].class,SuperVO[].class,String.class,UFDate.class,String.class,String.class};
		Object[] ParameterValues = new Object[]{left,right,ClientEnvironment.getInstance().getUser().getPrimaryKey()
				,ClientEnvironment.getInstance().getDate(),getBillListPanel().getLeftBiddingID(),getBillListPanel().getRightBiddingID()};
		LongTimeTask.
		calllongTimeService("pu", parent, "���ڻ��б���Ϣ...", 1, "nc.bs.zb.bidding.adjust.AdjustBiddingBO", null, "dealBidding", ParameterTypes, ParameterValues);
	
		closeOK();
	}
	private void onCancel() throws Exception{
		closeCancel();
	}
}

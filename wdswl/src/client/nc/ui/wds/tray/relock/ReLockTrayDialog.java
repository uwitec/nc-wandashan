package nc.ui.wds.tray.relock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillListPanel;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.ic.cargtray.SmallTrayVO;
import nc.vo.wl.pub.WdsWlPubTool;

public class ReLockTrayDialog extends UIDialog implements ActionListener{

//	private static final long serialVersionUID = -39986234234258916L;
	
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	private ReLockTrayListToListPanel listPane = null;
	
	private nc.ui.pub.beans.UIPanel ivjPnButton = null;
	private nc.ui.pub.beans.UIButton ivjBnCancel = null;
	private nc.ui.pub.beans.UIButton ivjBnOk = null;
	
	private ToftPanel parent = null;
	
//	private String cwarehouseid = null;
//	
//	private boolean isedit = false;
	
	private BillListPanel trayPanel = null;
	
	public static String xntrayid = "";//������ϸ��ˮ��ID  ��������ʱʹ��
	public static String invmanid = "";
	public static String batchcode = "";
	
//	public static void setBBID(String id){
//		bbid = id;
//	}
//	public static String getBBID(){
//		return bbid;
//	}

	public ReLockTrayDialog(ToftPanel tp,BillListPanel trayPanel,String cwarehouseid,boolean isedit) {
		super(tp);
		this.parent = tp;
//		this.cwarehouseid = cwarehouseid;
//		this.isedit = isedit;
		this.trayPanel = trayPanel;
		init();
	}
	
//	public void setIsEdit(boolean isedit){
//		this.isedit = isedit;
//	}
	
	public SmallTrayVO[] getRetVos(){
		SuperVO[] vos =  (SuperVO[])this.getBillListPanel().getRightListItems();
		SmallTrayVO[] vos2 = new SmallTrayVO[vos.length];
		int index = 0;
		for(SuperVO vo:vos){
			vos2[index] = (SmallTrayVO)vo;
			index++;
		}
		return vos2;
	}
	
//	public void setWarehouseID(String wareID){
//		this.cwarehouseid = wareID;
//	}
	
	private void init() {
		setSize(610,400);
		setTitle("���̰�");
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
	
	public ReLockTrayListToListPanel getBillListPanel() {
		if (listPane == null) {
			listPane = new ReLockTrayListToListPanel();
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
			MessageDialog.showErrorDlg(this, "����", WdsWlPubTool.getString_NullAsTrimZeroLen(ee.getMessage()));
		}
	}
	
//	private String getkey(int row){
//		return WdsWlPubTool.getString_NullAsTrimZeroLen(getBodyValue(row, "cdt_pk"))+","+
//		WdsWlPubTool.getString_NullAsTrimZeroLen(getBodyValue(row, "vbatchcode"));
//	}
	
	private void onOK() throws Exception{
		SmallTrayVO[] trays = getRetVos();
		if(trays == null || trays.length ==0){
			parent.showWarningMessage("δָ��ʵ������");
			return;
		}
//		//		У����������   Ĭ�� ����ѡ�е�����˳�� ����    ����������ӵ�   ��Ų��±���
//
		checkTrayVolume(trays);
//		if(isedit){
//			closeOK();
//			return;
//		}

//		int row = trayPanel.getBodyTable().getSelectedRow();
//		String key = getkey(row);
//		getl
//		Map<String, SmallTrayVO[]> lockTrayInfor = new HashMap<String, SmallTrayVO[]>();
//		lockTrayInfor.put(key, trays);
//		LockTrayHelper.lockTray(parent,
//				PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("geh_pk")),
//				PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("geh_cwarehouseid")),
//				lockTrayInfor);
		closeOK();
	}
	private String getkey(int row){
		return WdsWlPubTool.getString_NullAsTrimZeroLen(getBodyValue(row, "cdt_pk"))+","+
		WdsWlPubTool.getString_NullAsTrimZeroLen(getBodyValue(row, "vbatchcode"));
	}
    private Object getHeadValue(String fieldname){
		if(PuPubVO.getString_TrimZeroLenAsNull(fieldname)==null)
			return null;
		int row = trayPanel.getHeadTable().getSelectedRow();
		if(row < 0)
			return null;
		return trayPanel.getHeadBillModel().getValueAt(row, fieldname);
	}
    private Object getBodyValue(int row,String fieldname){
		return trayPanel.getBodyBillModel().getValueAt(row, fieldname);
	}
    private void checkTrayVolume(SmallTrayVO[] trays) throws ValidationException{
		int row = trayPanel.getBodyTable().getSelectedRow();
		if(row<0)
			throw new ValidationException("δѡ�б���������Ϣ��");
//		�����λ��������
		UFDouble nunitvolume = PuPubVO.getUFDouble_NullAsZero(getHeadValue("tray_volume"));
		int len = trays.length;
		UFDouble nallvolume = nunitvolume.multiply(len);
		
		UFDouble nactnum = PuPubVO.getUFDouble_NullAsZero(getBodyValue(row, "noutassistnum"));
		if(nactnum.compareTo(nallvolume)>0)
			throw new ValidationException("ָ�����̵���������ʵ�ʳ�������");
	}
    
	private void onCancel() throws Exception{
		closeCancel();
	}
}

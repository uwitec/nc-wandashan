package nc.ui.wds.tray.lock;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

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

public class LockTrayDialog extends UIDialog implements ActionListener{

//	private static final long serialVersionUID = -39986234234258916L;
	
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	private LockTrayListToListPanel listPane = null;
	
	private nc.ui.pub.beans.UIPanel ivjPnButton = null;
	private nc.ui.pub.beans.UIButton ivjBnCancel = null;
	private nc.ui.pub.beans.UIButton ivjBnOk = null;
	
	private ToftPanel parent = null;
	
	private String cwarehouseid = null;
	
	private boolean isedit = false;
	
	private BillListPanel trayPanel = null;
	
	
//	public LockTrayDialog(ToftPanel tp) {
//		super(tp);
//		this.parent = tp;
//		init();
//	}
	public LockTrayDialog(ToftPanel tp,Container parent,BillListPanel trayPanel,String cwarehouseid,boolean isedit) {
		super(parent);
		this.parent = tp;
		this.cwarehouseid = cwarehouseid;
		this.isedit = isedit;
		this.trayPanel = trayPanel;
		init();
	}
	
	public void setIsEdit(boolean isedit){
		this.isedit = isedit;
	}
	
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
	
	public void setWarehouseID(String wareID){
		this.cwarehouseid = wareID;
	}
	
	private void init() {
		setSize(610,400);
		setTitle("托盘绑定");
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
	
	public LockTrayListToListPanel getBillListPanel() {
		if (listPane == null) {
			listPane = new LockTrayListToListPanel(cwarehouseid);
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
			MessageDialog.showErrorDlg(this, "错误", WdsWlPubTool.getString_NullAsTrimZeroLen(ee.getMessage()));
		}
	}
	
	private String getkey(int row){
		return WdsWlPubTool.getString_NullAsTrimZeroLen(getBodyValue(row, "cdt_pk"))+","+
		WdsWlPubTool.getString_NullAsTrimZeroLen(getBodyValue(row, "gebb_vbatchcode"));
	}
	
	private void onOK() throws Exception{
		SmallTrayVO[] trays = getRetVos();
		if(trays == null || trays.length ==0){
			parent.showWarningMessage("未指定实际托盘");
			return;
		}
		//		校验托盘容量   默认 按照选中的托盘顺序 放量    多余的托盘扔掉   存放不下报错

		checkTrayVolume(trays);
		if(isedit){
			closeOK();
			return;
		}

		int row = trayPanel.getBodyTable().getSelectedRow();
		String key = getkey(row);
		Map<String, SmallTrayVO[]> lockTrayInfor = new HashMap<String, SmallTrayVO[]>();
		lockTrayInfor.put(key, trays);
		
		LockTrayHelper.lockTray(parent,
				PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("geh_pk")),
				PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("geh_cwarehouseid")),
				PuPubVO.getString_TrimZeroLenAsNull(getBodyValue(row, "gebb_pk")),
				lockTrayInfor
				);
		closeOK();
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
		if(trays == null || trays.length == 0)
			return;
		int row = trayPanel.getBodyTable().getSelectedRow();
		if(row<0)
			throw new ValidationException("未选中表体托盘信息行");
//		存货单位托盘容量
		UFDouble nunitvolume = PuPubVO.getUFDouble_NullAsZero(getHeadValue("traymax"));
		int len = trays.length;
		UFDouble nallvolume = nunitvolume.multiply(len);
		
		UFDouble nactnum = PuPubVO.getUFDouble_NullAsZero(getBodyValue(row, "ninassistnum"));
		if(nactnum.compareTo(nallvolume)>0)
			throw new ValidationException("实际入库数量超出选择托盘的总容量");
	}
    
	private void onCancel() throws Exception{
		closeCancel();
	}

//	public void actionPerformed(ActionEvent e) {
//		// TODO Auto-generated method stub
//		
//	}

}

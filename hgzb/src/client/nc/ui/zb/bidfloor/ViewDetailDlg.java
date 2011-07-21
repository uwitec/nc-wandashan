package nc.ui.zb.bidfloor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillListPanel;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.pub.ZbPubConst;

/**
 * 
 * @author zhw
 * �鿴��ϸ�Ի���
 *
 */
public class ViewDetailDlg extends UIDialog implements ActionListener {
	private static final long serialVersionUID = -39986234234258916L;
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	private BillListPanel listPane = null;
	private nc.ui.pub.beans.UIPanel ivjPnButton = null;
	private nc.ui.pub.beans.UIButton ivjBnOk = null;

	private  HashMap<String,ViewDetailVO[]> map = new HashMap<String,ViewDetailVO[]>();
	private ClientUI ui = null;
	private String str =null ;
	
	private void init() {
		setSize(450, 400);
		setTitle("�鿴��ϸ");
		setContentPane(getUIDialogContentPane());
		getBnOk().addActionListener(this);
	}

	public BillListPanel getBillListPanel() {
		if (listPane == null) {
			listPane = new BillListPanel();
			listPane.loadTemplet(ZbPubConst.VIEW_DETAIL_DLG_TEMP_ID);
		}
		return listPane;
	}

	public ViewDetailDlg(ClientUI ui,String str) {
		super(ui);
		this.ui = ui;
		this.str=str;
		init();
		try{
		setDatasToUI();
		}catch(Exception e){
			showErrorMsg(e.getMessage());
			e.printStackTrace();
		}
	}

	public void setDatasToUI() throws Exception {
		
		getBillListPanel().getBodyBillModel().clearBodyData();
		ViewDetailVO[] vos =loadDatas();
		getBillListPanel().getBodyBillModel().setBodyDataVO(vos);
		if(vos!=null){
			int len = vos.length;
			for(int i=0;i<len;i++){
				if(PuPubVO.getString_TrimZeroLenAsNull(vos[i].getCcustbasid())==null){
					String[] formulas = new String[]{
							"gysname->getColvalue(bd_cubasdochg,custname,ccubasdochgid,ccustmanid)",
							"gyscode->getColvalue(bd_cubasdochg,vbillno,ccubasdochgid,ccustmanid)"
							};
					getBillListPanel().getBodyBillModel().execFormulas(i, formulas);
				}else{
					String[] formulas = new String[]{
							"gysname->getColvalue(bd_cubasdoc,custname,pk_cubasdoc,ccustbasid)",
							"gyscode->getColvalue(bd_cubasdoc,custcode,pk_cubasdoc,ccustbasid)"
							};
					getBillListPanel().getBodyBillModel().execFormulas(i, formulas);
				}
			}
		}
	}

	/**
	 * ���� UIDialogContentPane ����ֵ.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* ����:�˷�������������. */
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
	private nc.ui.pub.beans.UIButton getBnOk() {
		if (ivjBnOk == null) {
			try {
				ivjBnOk = new nc.ui.pub.beans.UIButton();
				ivjBnOk.setName("BnOk");
				ivjBnOk.setText("�ر�");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnOk;
	}

	private void showErrorMsg(String msg){
		MessageDialog.showErrorDlg(ui, "", msg==null?"���������쳣�������²���":msg);
	}

	public void actionPerformed(ActionEvent bo) {
		try {
			 if (bo.getSource() == getBnOk()) {
				this.closeOK();
			} 
		} catch (Exception e) {
			showErrorMsg(e.getMessage());
			e.printStackTrace();
		}

	}
	private ViewDetailVO[]  loadDatas() throws Exception{
		ViewDetailVO[] vos = null;
		if(str !=null){
			vos = map.get(str);
			if(vos ==null|| vos.length==0){
				String[] strs=str.split("&");
				String cbiddingid = strs[0];//����id
				String cinvmanid = strs[1];//���id
				vos=BidFloorHelper.loadDatas(cbiddingid, cinvmanid);
				map.put(str,vos);
			}
		}
		return vos;
	}
	
}

package nc.ui.hg.pu.plan.balance;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import nc.ui.hg.pu.pub.LongTimeTask;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillListPanel;
import nc.vo.hg.pu.plan.temp.PlanInventoryVO;

/**
 * 
 * @author zhw
 * 查看存货存量显示
 *
 */
public class ViewDetailDlg extends UIDialog implements ActionListener {
	private static final long serialVersionUID = -39986234234258916L;
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	private BillListPanel listPane = null;
	private nc.ui.pub.beans.UIPanel ivjPnButton = null;
	private nc.ui.pub.beans.UIButton ivjBnOk = null;
	
	private BalancePlanUI ui = null;
	private ArrayList<String> al ;

	private void init() {
		setSize(450, 400);
		setTitle("存货存量显示");
		setContentPane(getUIDialogContentPane());
		getBnOk().addActionListener(this);
	}

	public BillListPanel getBillListPanel() {
		if (listPane == null) {
			listPane = new BillListPanel();
			listPane.loadTemplet("1002AA1000000001VU4Q");
		}
		return listPane;
	}

	public ViewDetailDlg(BalancePlanUI ui,ArrayList<String> als) {
		super(ui);
		this.ui = ui;
		al=als;
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
		PlanInventoryVO[] vos =loadDatas();
		getBillListPanel().getBodyBillModel().setBodyDataVO(vos);
		getBillListPanel().getBodyBillModel().execLoadFormula();
	}

	/**
	 * 返回 UIDialogContentPane 特性值.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告:此方法将重新生成. */
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
	private nc.ui.pub.beans.UIButton getBnOk() {
		if (ivjBnOk == null) {
			try {
				ivjBnOk = new nc.ui.pub.beans.UIButton();
				ivjBnOk.setName("BnOk");
				ivjBnOk.setText("关闭");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnOk;
	}

	private void showErrorMsg(String msg){
		MessageDialog.showErrorDlg(ui, "", msg==null?"操作出现异常，请重新操作":msg);
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
	private PlanInventoryVO[]  loadDatas() throws Exception{
		PlanInventoryVO[] vos = null;
		Class[] ParameterTypes = new Class[]{ArrayList.class};
		Object[] ParameterValues = new Object[]{al};
		Object o = LongTimeTask.callRemoteService("pu","nc.bs.hg.pu.plan.temp.StockNumBO", "getOnhandNum1", ParameterTypes, ParameterValues, 2);
	    if(o!=null)
	    	vos= (PlanInventoryVO[])o;
		return vos;
	}
	
}

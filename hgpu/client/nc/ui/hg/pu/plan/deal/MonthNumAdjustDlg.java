package nc.ui.hg.pu.plan.deal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.vo.hg.pu.plan.deal.PlanDealVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.pub.ValidationException;

/**
 * 
 * @author zhf
 * 月份量调整对话框
 *
 */
public class MonthNumAdjustDlg extends UIDialog implements ActionListener {
	private static final long serialVersionUID = -39986234234258916L;
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	private BillCardPanel cardPane = null;
	private nc.ui.pub.beans.UIPanel ivjPnButton = null;

	private nc.ui.pub.beans.UIButton ivjBnCancel = null;
	private nc.ui.pub.beans.UIButton ivjBnOk = null;

	private PlanDealVO[] datas = null;

	private ToftPanel parent = null;

	private void init() {
		setSize(1000, 500);
		setTitle("月份分量调整");
		setContentPane(getUIDialogContentPane());
		getBnOk().addActionListener(this);
		getBnCancel().addActionListener(this);
	}

	public BillCardPanel getDataPanel() {
		if (cardPane == null) {
			cardPane = new BillCardPanel();
			cardPane.loadTemplet(HgPubConst.PLAN_DEAL_MONTHADJUST_TEMPLETID);
			cardPane.setEnabled(true);
		}
		return cardPane;
	}

	public MonthNumAdjustDlg(ToftPanel tp) {
		super(tp);
		this.parent = tp;
		init();
	}

	public void setDatasToUI(PlanDealVO[] datas){
		getDataPanel().getBillModel().clearBodyData();
		getDataPanel().getBillModel().setBodyDataVO(datas);
		getDataPanel().getBillModel().execLoadFormula();
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
				getUIDialogContentPane().add(getDataPanel(), "Center");
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
				//				getPnButton().add(getBnCal(), getBnCal().getName());
				//				getPnButton().add(getBnSave(), getBnSave().getName());
				getPnButton().add(getBnOk(), getBnOk().getName());		
				getPnButton().add(getBnCancel(), getBnCancel().getName());
					

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjPnButton;
	}


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
				ivjBnOk.setText("确定");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnOk;
	}

	private void showErrorMsg(String msg){
		MessageDialog.showErrorDlg(parent, "", msg==null?"操作出现异常，请重新操作":msg);
	}

	public void actionPerformed(ActionEvent bo) {
		try{

			if (bo.getSource() == getBnOk()) {
				onOK();
			} else if (bo.getSource() == getBnCancel()) {
				this.closeCancel();
			}
		}catch(Exception e){
			showErrorMsg(e.getMessage());
			e.printStackTrace();
		}

	}

	public PlanDealVO[] getDatas(){
		return datas;
	}
	private void onOK() throws ValidationException{
		getDataPanel().stopEditing();
		this.datas = (PlanDealVO[])getDataPanel().getBillModel().getBodyValueVOs(PlanDealVO.class.getName());
		for(PlanDealVO data:datas){
			data.validata();
		}
		closeOK();
	}

}

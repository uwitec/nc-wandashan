package nc.ui.hg.pu.pub;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.hg.pu.plan.detail.PlanBBVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.PlanVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;

/**
 * 
 * @author zhw 调整净需求对话框
 * 
 */
public class ModifyNnetNumDlg extends UIDialog implements ActionListener {
	private static final long serialVersionUID = -39986234234258916L;
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	private BillListPanel listPane = null;
	private nc.ui.pub.beans.UIPanel ivjPnButton = null;

	private nc.ui.pub.beans.UIButton ivjBnOk = null;
	private ToftPanel parent = null;
	private String pk = null;

	private void init() {
		setSize(450, 200);
		setTitle("调整净需求信息");
		setContentPane(getUIDialogContentPane());
		getBnOk().addActionListener(this);
//		getBnCancel().addActionListener(this);
	}

	public BillListPanel getBillListPanel() {
		if (listPane == null) {
			listPane = new BillListPanel();
			listPane.loadTemplet(HgPubConst.MODIFY_TEMP_ID);
		}
		return listPane;
	}

	public ModifyNnetNumDlg(ToftPanel tp) {
		super(tp);
		this.parent = tp;
		init();
	}

	public void initBillListPanel() throws Exception {
//		try {
			getBillListPanel().getBodyBillModel().clearBodyData();
			getBillListPanel().getBodyBillModel().setBodyDataVO(getDatas());
			getBillListPanel().getBodyBillModel().execLoadFormula();
//		} catch (Exception e) {
//			showErrorMsg(e.getMessage());
//			e.printStackTrace();
//			return;
//		}
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
//				getPnButton().add(getBnCancel(), getBnCancel().getName());
				getPnButton().add(getBnOk(), getBnOk().getName());

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
//	private nc.ui.pub.beans.UIButton getBnCancel() {
//		if (ivjBnCancel == null) {
//			try {
//				ivjBnCancel = new nc.ui.pub.beans.UIButton();
//				ivjBnCancel.setName("BnCancel");
//				ivjBnCancel.setText("取消");
//
//			} catch (java.lang.Throwable ivjExc) {
//
//			}
//		}
//		return ivjBnCancel;
//	}

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

	private void showErrorMsg(String msg) {
		MessageDialog.showErrorDlg(parent, "", msg == null ? "操作出现异常，请重新操作"
				: msg);
	}

	public void actionPerformed(ActionEvent bo) {
		try {
			if (bo.getSource() == getBnOk()) {
				this.closeOK();
			}
//				else if (bo.getSource() == getBnCancel()) {
//				this.closeCancel();
//			}
		} catch (Exception e) {
			showErrorMsg(e.getMessage());
			e.printStackTrace();
		}
	}

	private PlanBBVO[] getDatas() throws Exception {
		
			PlanBBVO[] yearbvo = null;
			String yearWhere = " pk_planyear_b ='"+pk+"'";
			SuperVO[] yearbvos = HYPubBO_Client.queryByCondition(
					PlanBBVO.class, yearWhere);
			if(yearbvos ==null || yearbvos.length==0)
				throw new BusinessException("该单据还没有调整信息");
			
				yearbvo = (PlanBBVO[]) yearbvos;
			return yearbvo;
//		} else {
//			PlanBBVO[] otherbvo = null;
//			String otherWhere = " pk_planyear_b in (select b.pk_planyear_b from hg_plan h ,hg_planother_b b  "
//					+ "where h.pk_plan = b.pk_plan and  isnull(h.dr,0)=0 and isnull(b.dr,0) = 0 and h.pk_plan = '"
//					+ key + "')";
//			SuperVO[] otherbvos = HYPubBO_Client.queryByCondition(
//					PlanBBVO.class, otherWhere);
//			if (otherbvos != null && otherbvos.length > 0) 
//				throw new BusinessException("该单据还没有调整信息");
//			
//				otherbvo = (PlanBBVO[]) otherbvos;
//			
//			return otherbvo;
//		}
	}


	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}
}

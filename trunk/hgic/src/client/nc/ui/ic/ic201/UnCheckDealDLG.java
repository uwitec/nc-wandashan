package nc.ui.ic.ic201;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import nc.ui.hg.pu.pub.LongTimeTask;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillListPanel;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

/**
 * 不合格处理对话对话框
 * 
 * @author zhw
 * 
 */
public class UnCheckDealDLG extends UIDialog implements ActionListener {
	private static final long serialVersionUID = -39986234234258916L;
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	private BillListPanel listPane = null;

	private nc.ui.pub.beans.UIPanel ivjPnButton = null;
	private nc.ui.pub.beans.UIButton ivjBnCancel = null;
	private nc.ui.pub.beans.UIButton ivjBnOk = null;

	private ToftPanel parent = null;
	private GeneralBillVO gbillvo = null;
	private ClientEnvironment ce = null;

	public UnCheckDealDLG(ToftPanel tp, ClientEnvironment ce) {
		super(tp);
		this.parent = tp;
		this.ce = ce;
		init();
	}

	public void initDataPanel() {
		try {
			getBillListPanel().getBodyBillModel().clearBodyData();
			getBillListPanel().getBodyBillModel().setBodyDataVO(
					gbillvo.getChildrenVO());
			getBillListPanel().getBodyBillModel().setEnabled(true);
			getBillListPanel().getBodyBillModel().execLoadFormula();
		} catch (Exception e) {
			showErrorMsg(e.getMessage());
			e.printStackTrace();
		}
	}

	private void init() {
		setSize(750, 200);
		setTitle("不合格处理");
		setContentPane(getUIDialogContentPane());
		// setEnabled(true);
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

	public BillListPanel getBillListPanel() {
		if (listPane == null) {
			listPane = new BillListPanel();
			listPane.loadTemplet(HgPubConst.UNCHECK_DEAL_TEMP_ID);
			listPane.setEnabled(true);
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
		try {
			if (bo.getSource() == getBnOk()) {

				onOK();
				this.closeOK();
			} else if (bo.getSource() == getBnCancel()) {
				this.closeCancel();
			}
		} catch (Exception e) {
			showErrorMsg(e.getMessage());
			e.printStackTrace();
		}
	}

	private void onOK() throws Exception {
		CircularlyAccessibleValueObject[] vos = getBillListPanel()
				.getBodyBillModel().getBodyValueVOs(
						GeneralBillItemVO.class.getName());
		if (vos == null)
			throw new BusinessException("获取界面信息出错");
		int len = vos.length;
		GeneralBillHeaderVO head = gbillvo.getHeaderVO();
		GeneralBillItemVO[] items = (GeneralBillItemVO[]) gbillvo
				.getChildrenVO();
		head.setAttributeValue(HgPubConst.VUSERDEF[5], "N");// 解封单据
		head.setAttributeValue(HgPubConst.VUSERDEF[6], ce.getDate());
		head.setAttributeValue(HgPubConst.VUSERDEF[7], ce.getUser()
				.getPrimaryKey());
		for (int i = 0; i < len; i++) {
			if(vos[i].getAttributeValue(HgPubConst.NUM_DEF_QUA) ==null){
				throw new BusinessException("第"+(i+1)+"行验收合格数量不能为空");
			}
			
			if(PuPubVO.getUFDouble_NullAsZero(vos[i].getAttributeValue(HgPubConst.NUM_DEF_QUA)).compareTo(UFDouble.ZERO_DBL)<0){
				throw new BusinessException("第"+(i+1)+"行验收合格数量不能小于0");
			}
			
			if(PuPubVO.getUFDouble_NullAsZero(vos[i].getAttributeValue(HgPubConst.NUM_DEF_FAC)).compareTo(PuPubVO.getUFDouble_NullAsZero(vos[i].getAttributeValue(HgPubConst.NUM_DEF_QUA)))<0){
				throw new BusinessException("第"+(i+1)+"行实收数量应该大于验收合格数量");
			}
			
			items[i].setAttributeValue(HgPubConst.NUM_DEF_QUA, PuPubVO.getUFDouble_NullAsZero(vos[i].getAttributeValue(HgPubConst.NUM_DEF_QUA)));
		}
		Class[] ParameterTypes = new Class[] { GeneralBillVO.class };
		Object[] ParameterValues = new Object[] { gbillvo };
		Object o = LongTimeTask.callRemoteService("ic",
				"nc.bs.hg.ic.ic201.SaveGeneral", "onUnDeal", ParameterTypes,
				ParameterValues, 2);
		if (o != null) {
			Map map = (Map) o;
			if (map.containsKey(head.getCgeneralhid())) {
				head.setTs((map.get(head.getCgeneralhid()).toString()));
			}
			for (int i = 0; i < len; i++) {
				if (map.containsKey(items[i].getCgeneralbid())) {
					items[i].setTs((map.get(items[i].getCgeneralbid())
							.toString()));
				}
			}
		}
		setGbillvo(gbillvo);
	}

	private void showErrorMsg(String msg) {
		MessageDialog.showErrorDlg(parent, "", msg == null ? "操作出现异常，请重新操作"
				: msg);
	}

	public GeneralBillVO getGbillvo() {
		return gbillvo;
	}

	public void setGbillvo(GeneralBillVO gbillvo) {
		this.gbillvo = (GeneralBillVO)gbillvo.clone();
	}
}

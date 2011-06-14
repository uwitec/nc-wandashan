package nc.ui.wl.pub.report;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.WindowConstants;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;

/**
 * 多重穿透时弹出对话框让用户选择想穿透到的节点
 * @author 周瑞杰
 */
public class NodeDLG extends UIDialog {

	private static final long serialVersionUID = 1313361267267964957L;
	
	private String[] nodeNames = null;

	private int nodeIndex = -1;

	/** 提示标签 */
	private UILabel lblNodeNames = null;

	private UIComboBox cbNodes = null;

	private UIButton bCancel = null;

	private UIButton bOk = null;

	/** OK按钮 */
	public final static int OKBUTTON = 1;

	/** Cancel按钮 */
	public final static int CANCELBUTTON = 0;

	/** Close按钮 */
	public final static int CLOSEBUTTON = -1;

	private int okAndCancel = CLOSEBUTTON;

	ehDlg ehDlgal = new ehDlg();

	private JPanel jpDlgContent = null;

	private Object publicClass = null;

	class ehDlg implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == NodeDLG.this.getOk())
				connEtoOk(e);
			if (e.getSource() == NodeDLG.this.getCancel())
				connEtoCancle(e);
		};
	};

	public NodeDLG(Container parent, String[] nodes) {
		super(parent);
		nodeNames = nodes;
		initialize();
	}

	private void initialize() {
		try {
			setName("UIListToListDialog");
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setSize(350, 128);
			setContentPane(getUIDialogContentPane());
			initConnections();
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private JPanel getUIDialogContentPane() {

		if (jpDlgContent == null) {
			try {
				jpDlgContent = new JPanel();
				jpDlgContent.setName("UIDialogContentPane");
				jpDlgContent.setLayout(null);

				UILabel uillabel = getUILabelIns();
				UIComboBox uicombobox = getUIComboBoxIns();

				getUIDialogContentPane().add(uillabel,uillabel.getName());
				getUIDialogContentPane().add(uicombobox, uicombobox.getName());
				getUIDialogContentPane().add(getOk(), getOk().getName());
				getUIDialogContentPane().add(getCancel(), getCancel().getName());
				
			} catch (Throwable e) {
				handleException(e);
			}
		}
		return jpDlgContent;
	}

	private UILabel getUILabelIns() {
		try {
			if (lblNodeNames == null)
				lblNodeNames = new UILabel();
			lblNodeNames.setName("UILabel");
			lblNodeNames.setText("请选择您要穿透到的单据:");
			lblNodeNames.setBounds(20, 16, 150, 22);
		} catch (Throwable e) {
			handleException(e);
		}
		return lblNodeNames;
	}

	private UIComboBox getUIComboBoxIns() {
		try {
			if (cbNodes == null)
				cbNodes = new UIComboBox(nodeNames);
			cbNodes.setName("UIComboBox");
			cbNodes.setBounds(180, 16, 150, 22);
		} catch (Throwable e) {
			handleException(e);
		}
		return cbNodes;
	}

	private UIButton getOk() {
		if (bOk == null)
			bOk = new UIButton();
		bOk.setName("Ok");
		bOk.setText("确定");
		bOk.setBounds(105, 55, 50, 21);
		return bOk;
	}

	private UIButton getCancel() {
		if (bCancel == null)
			bCancel = new UIButton();
		bCancel.setName("Cancel");
		bCancel.setText("取消");
		bCancel.setBounds(195, 55, 50, 21);
		return bCancel;
	}

	private void initConnections() throws Exception {
		getOk().addActionListener(ehDlgal);
		getCancel().addActionListener(ehDlgal);
	}

	public void cancel_ActionPerformed(ActionEvent actionEvent) {
		okAndCancel = CANCELBUTTON;
		closeCancel();
	}

	/**
	 * 点击OK按钮触发的方法 ;
	 */
	public void ok_ActionPerformed(ActionEvent actionEvent) {
		okAndCancel = OKBUTTON;
		closeOK();
	}

	private void connEtoCancle(ActionEvent arg1) {
		try {
			nodeIndex=-1;   //modify by zhourj
			this.cancel_ActionPerformed(null);
		} catch (Throwable e) {
			handleException(e);
		}
	}

	private void handleException(Throwable exception) {

	}

	private void connEtoOk(ActionEvent ae) {
		try {
			nodeIndex = cbNodes.getSelectedIndex();
			this.ok_ActionPerformed(ae);
		} catch (Throwable e) {
			handleException(e);
		}
	}

	/**
	 * @return 返回 publicClass。
	 */
	public Object getPublicClass() {
		return publicClass;
	}

	/**
	 * @param publicClass
	 *            要设置的 publicClass。
	 */
	public void setPublicClass(Object publicClass) {
		this.publicClass = publicClass;
	}

	public int getOkAndCancel() {
		return okAndCancel;
	}

	/**
	 * 返回要穿透的节点的序号
	 * @return
	 */
	public int getNodeIndex() {
		return nodeIndex;
	}
	
}

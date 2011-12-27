package nc.ui.wds.pub.report2;
import java.awt.Dimension;
import javax.swing.JPanel;
import nc.bs.logging.Logger;
import nc.ui.iuforeport.businessquery.RotateCrossPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.querymodel.RotateCrossSetDlg;
import nc.ui.pub.querymodel.UIUtil;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.pub.querymodel.RotateCrossVO;
/**
 * 旋转交叉设置框
 * 
 * 创建日期：(2003-11-28 9:38:11)
 * @author：朱俊彬  modify mlr 修复旋转交叉 交叉多列出错的bug
 */
public class CrossDLG extends UIDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1463011405178652008L;
	private RotateCrossVO m_rc = null;
	private UIButton ivjBnCancel = null;
	private UIButton ivjBnOK = null;
	private UIPanel ivjPnSouth = null;
	//private FlowLayout ivjPnSouthFlowLayout = null;
	private JPanel ivjUIDialogContentPane = null;
	private UIPanel ivjPnNorth = null;
	private RotateCrossPanel ivjPnRotateCross = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == CrossDLG.this.getBnOK())
				connEtoC1(e);
			if (e.getSource() == CrossDLG.this.getBnCancel())
				connEtoC2(e);
		};
	};
/**
 * RotateCrossSetDlg 构造子注解。
 * @deprecated
 */
public CrossDLG() {
	super();
	initialize();
}
/**
 * RotateCrossSetDlg 构造子注解。
 * @param parent java.awt.Container
 */
public CrossDLG(java.awt.Container parent) {
	super(parent);
	initialize();
}
/**
 * 取消
 */
public void bnCancel_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	closeCancel();
}
/**
 * 确定
 */
public void bnOK_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	String strErr = getPnRotateCross().checkCrossinfo();
	if (strErr == null)
		m_rc = getPnRotateCross().getRotateCross();
	else
		if (strErr.equals(""))
			m_rc = null;
		else {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000099")/*@res "查询引擎"*/, strErr);
			return;
		}
	closeOK();
}
/**
 * connEtoC1:  (BnOK.action.actionPerformed(java.awt.event.ActionEvent) --> RotateCrossSetDlg.bnOK_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* 警告：此方法将重新生成。 */
private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.bnOK_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (BnCancel.action.actionPerformed(java.awt.event.ActionEvent) --> RotateCrossSetDlg.bnCancel_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* 警告：此方法将重新生成。 */
private void connEtoC2(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.bnCancel_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * 返回 BnCancel 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getBnCancel() {
	if (ivjBnCancel == null) {
		try {
			ivjBnCancel = new nc.ui.pub.beans.UIButton();
			ivjBnCancel.setName("BnCancel");
			ivjBnCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000000")/*@res "取消"*/);
			// user code begin {1}
			ivjBnCancel.setPreferredSize(new Dimension(90, 22));
			UIUtil.autoSizeComp(ivjBnCancel, ivjBnCancel.getText());
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBnCancel;
}
/**
 * 返回 BnOK 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getBnOK() {
	if (ivjBnOK == null) {
		try {
			ivjBnOK = new nc.ui.pub.beans.UIButton();
			ivjBnOK.setName("BnOK");
			ivjBnOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000001")/*@res "确定"*/);
			// user code begin {1}
			ivjBnOK.setPreferredSize(new Dimension(90, 22));
			UIUtil.autoSizeComp(ivjBnOK, ivjBnOK.getText());
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBnOK;
}
/**
 * 返回 PnNorth 特性值。
 * @return nc.ui.pub.beans.UIPanel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIPanel getPnNorth() {
	if (ivjPnNorth == null) {
		try {
			ivjPnNorth = new nc.ui.pub.beans.UIPanel();
			ivjPnNorth.setName("PnNorth");
			ivjPnNorth.setPreferredSize(new java.awt.Dimension(10, 4));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPnNorth;
}
/**
 * 返回 PnRotateCross 特性值。
 * @return nc.ui.iuforeport.businessquery.RotateCrossPanel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.iuforeport.businessquery.RotateCrossPanel getPnRotateCross() {
	if (ivjPnRotateCross == null) {
		try {
			ivjPnRotateCross = new nc.ui.iuforeport.businessquery.RotateCrossPanel();
			ivjPnRotateCross.setName("PnRotateCross");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPnRotateCross;
}
/**
 * 返回 PnSouth 特性值。
 * @return nc.ui.pub.beans.UIPanel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIPanel getPnSouth() {
	if (ivjPnSouth == null) {
		try {
			ivjPnSouth = new nc.ui.pub.beans.UIPanel();
			ivjPnSouth.setName("PnSouth");
			ivjPnSouth.setLayout(getPnSouthFlowLayout());
			getPnSouth().add(getBnOK(), getBnOK().getName());
			getPnSouth().add(getBnCancel(), getBnCancel().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPnSouth;
}
/**
 * 返回 PnSouthFlowLayout 特性值。
 * @return java.awt.FlowLayout
 */
/* 警告：此方法将重新生成。 */
private java.awt.FlowLayout getPnSouthFlowLayout() {
	java.awt.FlowLayout ivjPnSouthFlowLayout = null;
	try {
		/* 创建部件 */
		ivjPnSouthFlowLayout = new java.awt.FlowLayout();
		ivjPnSouthFlowLayout.setVgap(8);
		ivjPnSouthFlowLayout.setHgap(20);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	};
	return ivjPnSouthFlowLayout;
}
/**
 * 获得旋转交叉VO
 * 创建日期：(2003-11-25 10:44:05)
 * @param rcs nc.vo.pub.querymodel.RotateCrossVO[]
 */
public RotateCrossVO getRotateCross() {
	return m_rc;
}
/**
 * 返回 UIDialogContentPane 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JPanel getUIDialogContentPane() {
	if (ivjUIDialogContentPane == null) {
		try {
			ivjUIDialogContentPane = new javax.swing.JPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(new java.awt.BorderLayout());
			getUIDialogContentPane().add(getPnSouth(), "South");
			getUIDialogContentPane().add(getPnRotateCross(), "Center");
			getUIDialogContentPane().add(getPnNorth(), "North");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIDialogContentPane;
}
/**
 * 每当部件抛出异常时被调用
 * @param exception java.lang.Throwable
 * @i18n upp08110600001=--------- 未捕捉到的异常 ---------
 */
private void handleException(java.lang.Throwable exception) {

	/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
	Logger.error("--------- 未捕捉到的异常 ---------", exception);
}
/**
 * 初始化连接
 * @exception java.lang.Exception 异常说明。
 */
/* 警告：此方法将重新生成。 */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	getBnOK().addActionListener(ivjEventHandler);
	getBnCancel().addActionListener(ivjEventHandler);
}
/**
 * 初始化类。
 */
/* 警告：此方法将重新生成。 */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("RotateCrossSetDlg");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(750, 480);
        this.setResizable(true);
		setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-001291")/*@res "旋转交叉设置"*/);
		setContentPane(getUIDialogContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}
/**
 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
 * @param args java.lang.String[]
 * @i18n upp08110600002=nc.ui.pub.beans.UIDialog 的 main() 中发生异常
 */
public static void main(java.lang.String[] args) {
	try {
		RotateCrossSetDlg aRotateCrossSetDlg;
		aRotateCrossSetDlg = new RotateCrossSetDlg();
		aRotateCrossSetDlg.setModal(true);
		aRotateCrossSetDlg.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aRotateCrossSetDlg.setVisible(true);
		java.awt.Insets insets = aRotateCrossSetDlg.getInsets();
		aRotateCrossSetDlg.setSize(aRotateCrossSetDlg.getWidth() + insets.left + insets.right, aRotateCrossSetDlg.getHeight() + insets.top + insets.bottom);
		aRotateCrossSetDlg.setVisible(true);
	} catch (Throwable exception) {
		Logger.error("nc.ui.pub.beans.UIDialog 的 main() 中发生异常", exception);
	}
}
/**
 * 设置旋转交叉VO
 * 创建日期：(2003-11-25 10:44:05)
 * @param rcs nc.vo.pub.querymodel.RotateCrossVO[]
 */
public void setRotateCross(RotateCrossVO rc, SelectFldVO[] sfs) {
	getPnRotateCross().setRotateCross(rc, sfs);
}
}  
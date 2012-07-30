package nc.ui.zmpub.pub.report.buttonaction2;

import java.awt.FlowLayout;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

import nc.ui.iuforeport.businessquery.CrossFhSetDlg;
import nc.ui.iuforeport.businessquery.CrossTotalDlg;
import nc.ui.iuforeport.businessquery.RotateCrossCellRenderer;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.pub.querymodel.ModelUtil;
import nc.vo.pub.querymodel.QueryConst;
import nc.vo.pub.querymodel.RotateCrossVO;

/**
 * 旋转交叉定义面板 创建日期：(2003-11-25 10:17:28)
 * 
 * @author：朱俊彬
 */
public class RotateCrossPanel extends UIPanel {

	private IReportDealListener deal = null;

	public void addReportDealListener(IReportDealListener lis) {
		deal = lis;
	}

	// 交叉合计设置框
	private CrossTotalDlg m_crossTotalDlg = null;

	// 行复合维度设置框
	private CrossFhSetDlg m_crossFhDlg = null;

	// 全部列数组
	private SelectFldVO[] m_allSfs = null;

	// 交叉合计列数组
	private String[] m_strTotalCols = null;

	// 交叉分组列数组
	private String[] m_strGroupCols = null;

	// 行复合维度标志
	private boolean m_bRowFh = true;

	// 列复合维度标志
	private boolean m_bColFh = false;

	// 交叉合计列排序器类名
	private String m_strColComparatorClass = null;

	private UIPanel ivjPnCenter = null;

	private UIPanel ivjPnEast = null;

	private UIScrollPane ivjSclPnAll = null;

	private UIList ivjListAll = null;

	private UIList ivjListCol = null;

	private UIList ivjListRow = null;

	private UIList ivjListVal = null;

	// private GridLayout ivjPnEastGridLayout = null;
	private UIScrollPane ivjSclPnCol = null;

	private UIScrollPane ivjSclPnRow = null;

	private UIScrollPane ivjSclPnVal = null;

	private UIButton ivjBnAddCol = null;

	private UIButton ivjBnAddRow = null;

	private UIButton ivjBnAddVal = null;

	private UIButton ivjBnDelCol = null;

	private UIButton ivjBnDelRow = null;

	private UIButton ivjBnDelVal = null;

	// private GridLayout ivjPnCenterGridLayout = null;
	private UIPanel ivjPnColBn = null;

	private UIPanel ivjPnRowBn = null;

	private UIPanel ivjPnValBn = null;

	private UIButton ivjBnUpRow = null;

	private UIButton ivjBnDownRow = null;

	private UIButton ivjBnDownCol = null;

	private UIButton ivjBnDownVal = null;

	private UIButton ivjBnUpCol = null;

	private UIButton ivjBnUpVal = null;

	private UILabel ivjLabelRow = null;

	private UILabel ivjLabelCol = null;

	private UILabel ivjLabelVal = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements java.awt.event.ActionListener,
			java.awt.event.MouseListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == RotateCrossPanel.this.getBnAddRow())
				connEtoC1(e);
			if (e.getSource() == RotateCrossPanel.this.getBnDelRow())
				connEtoC2(e);
			if (e.getSource() == RotateCrossPanel.this.getBnUpRow())
				connEtoC10(e);
			if (e.getSource() == RotateCrossPanel.this.getBnDownRow())
				connEtoC11(e);
			if (e.getSource() == RotateCrossPanel.this.getBnAddCol())
				connEtoC3(e);
			if (e.getSource() == RotateCrossPanel.this.getBnDelCol())
				connEtoC4(e);
			if (e.getSource() == RotateCrossPanel.this.getBnUpCol())
				connEtoC5(e);
			if (e.getSource() == RotateCrossPanel.this.getBnDownCol())
				connEtoC6(e);
			if (e.getSource() == RotateCrossPanel.this.getBnAddVal())
				connEtoC7(e);
			if (e.getSource() == RotateCrossPanel.this.getBnDelVal())
				connEtoC8(e);
			if (e.getSource() == RotateCrossPanel.this.getBnUpVal())
				connEtoC9(e);
			if (e.getSource() == RotateCrossPanel.this.getBnDownVal())
				connEtoC12(e);
		};

		public void mouseClicked(java.awt.event.MouseEvent e) {
			if (e.getSource() == RotateCrossPanel.this.getLabelVal())
				connEtoC13(e);
			if (e.getSource() == RotateCrossPanel.this.getLabelRow())
				connEtoC14(e);
			if (e.getSource() == RotateCrossPanel.this.getLabelCol())
				labelCol_MouseClicked(e);
		};

		public void mouseEntered(java.awt.event.MouseEvent e) {
		};

		public void mouseExited(java.awt.event.MouseEvent e) {
		};

		public void mousePressed(java.awt.event.MouseEvent e) {
		};

		public void mouseReleased(java.awt.event.MouseEvent e) {
		};
	};

	public String[] getM_strGroupCols() {
		return m_strGroupCols;
	}

	public void setM_strGroupCols(String[] groupCols) {
		m_strGroupCols = groupCols;
	}

	/**
	 * RotateCrossPanel 构造子注解。
	 */
	public RotateCrossPanel() {
		super();
		initialize();
	}

	/**
	 * RotateCrossPanel 构造子注解。
	 * 
	 * @param p0
	 *            boolean
	 */
	public RotateCrossPanel(boolean p0) {
		super(p0);
	}

	/**
	 * add col
	 */
	public void bnAddCol_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		Object[] objs = getListAll().getSelectedValues();
		if (objs != null) {
			int iLen = objs.length;
			int iSize = getLMCol().size();
			int[] iSelIndices = new int[iLen];
			for (int i = 0; i < objs.length; i++) {
				getLMAll().removeElement(objs[i]);
				getLMCol().addElement(objs[i]);
				iSelIndices[i] = iSize++;
			}
			getListCol().setSelectedIndices(iSelIndices);
		}
	}

	/**
	 * add row
	 */
	public void bnAddRow_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		Object[] objs = getListAll().getSelectedValues();
		if (objs != null) {
			int iLen = objs.length;
			int iSize = getLMRow().size();
			int[] iSelIndices = new int[iLen];
			for (int i = 0; i < iLen; i++) {
				getLMAll().removeElement(objs[i]);
				getLMRow().addElement(objs[i]);
				iSelIndices[i] = iSize++;
			}
			getListRow().setSelectedIndices(iSelIndices);
		}
	}

	/**
	 * add value
	 */
	public void bnAddVal_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		Object[] objs = getListAll().getSelectedValues();
		if (objs != null) {
			int iLen = objs.length;
			int iSize = getLMVal().size();
			int[] iSelIndices = new int[iLen];
			for (int i = 0; i < objs.length; i++)
				if (!objs[i].toString().equals(QueryConst.CROSS_WEIGHT)) {
					getLMAll().removeElement(objs[i]);
					getLMVal().addElement(objs[i]);
					iSelIndices[i] = iSize++;
				}
			getListVal().setSelectedIndices(iSelIndices);
		}
	}

	/**
	 * del col
	 */
	public void bnDelCol_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		Object[] objs = getListCol().getSelectedValues();
		if (objs != null) {
			int iLen = objs.length;
			int iSize = getLMAll().size();
			int[] iSelIndices = new int[iLen];
			for (int i = 0; i < objs.length; i++) {
				getLMCol().removeElement(objs[i]);
				getLMAll().addElement(objs[i]);
				iSelIndices[i] = iSize++;
			}
			getListAll().setSelectedIndices(iSelIndices);
		}
	}

	/**
	 * del row
	 */
	public void bnDelRow_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		Object[] objs = getListRow().getSelectedValues();
		if (objs != null) {
			int iLen = objs.length;
			int iSize = getLMAll().size();
			int[] iSelIndices = new int[iLen];
			for (int i = 0; i < objs.length; i++) {
				getLMRow().removeElement(objs[i]);
				getLMAll().addElement(objs[i]);
				iSelIndices[i] = iSize++;
			}
			getListAll().setSelectedIndices(iSelIndices);
		}
	}

	/**
	 * del value
	 */
	public void bnDelVal_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		Object[] objs = getListVal().getSelectedValues();
		if (objs != null) {
			int iLen = objs.length;
			int iSize = getLMAll().size();
			int[] iSelIndices = new int[iLen];
			for (int i = 0; i < objs.length; i++) {
				getLMVal().removeElement(objs[i]);
				getLMAll().addElement(objs[i]);
				iSelIndices[i] = iSize++;
			}
			getListAll().setSelectedIndices(iSelIndices);
		}
	}

	/**
	 * 下移列
	 */
	public void bnDownCol_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		if (getListCol().getSelectedValues().length == 1) {
			int iIndex = getListCol().getSelectedIndex();
			if (iIndex < getLMCol().getSize() - 1) {
				// 获得选中项
				Object obj = getListCol().getSelectedValue();
				// 下移
				getLMCol().removeElement(obj);
				getLMCol().insertElementAt(obj, iIndex + 1);
				getListCol().setSelectedIndex(iIndex + 1);
			}
		}
	}

	/**
	 * 下移行
	 */
	public void bnDownRow_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		if (getListRow().getSelectedValues().length == 1) {
			int iIndex = getListRow().getSelectedIndex();
			if (iIndex < getLMRow().getSize() - 1) {
				// 获得选中项
				Object obj = getListRow().getSelectedValue();
				// 下移
				getLMRow().removeElement(obj);
				getLMRow().insertElementAt(obj, iIndex + 1);
				getListRow().setSelectedIndex(iIndex + 1);
			}
		}
	}

	/**
	 * 下移值
	 */
	public void bnDownVal_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		if (getListVal().getSelectedValues().length == 1) {
			int iIndex = getListVal().getSelectedIndex();
			if (iIndex < getLMVal().getSize() - 1) {
				// 获得选中项
				Object obj = getListVal().getSelectedValue();
				// 下移
				getLMVal().removeElement(obj);
				getLMVal().insertElementAt(obj, iIndex + 1);
				getListVal().setSelectedIndex(iIndex + 1);
			}
		}
	}

	/**
	 * 上移列
	 */
	public void bnUpCol_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		if (getListCol().getSelectedValues().length == 1) {
			int iIndex = getListCol().getSelectedIndex();
			if (iIndex > 0) {
				// 获得选中项
				Object obj = getListCol().getSelectedValue();
				// 上移
				getLMCol().removeElement(obj);
				getLMCol().insertElementAt(obj, iIndex - 1);
				getListCol().setSelectedIndex(iIndex - 1);
			}
		}
	}

	/**
	 * 上移行
	 */
	public void bnUpRow_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		if (getListRow().getSelectedValues().length == 1) {
			int iIndex = getListRow().getSelectedIndex();
			if (iIndex > 0) {
				// 获得选中项
				Object obj = getListRow().getSelectedValue();
				// 上移
				getLMRow().removeElement(obj);
				getLMRow().insertElementAt(obj, iIndex - 1);
				getListRow().setSelectedIndex(iIndex - 1);
			}
		}
	}

	/**
	 * 上移值
	 */
	public void bnUpVal_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		if (getListVal().getSelectedValues().length == 1) {
			int iIndex = getListVal().getSelectedIndex();
			if (iIndex > 0) {
				// 获得选中项
				Object obj = getListVal().getSelectedValue();
				// 上移
				getLMVal().removeElement(obj);
				getLMVal().insertElementAt(obj, iIndex - 1);
				getListVal().setSelectedIndex(iIndex - 1);
			}
		}
	}

	/**
	 * 合法性检查 创建日期：(2003-4-4 14:00:39)
	 * 
	 * @return java.lang.String
	 */
	public String check(boolean bDef) {
		// 空值校验
		int[] iRowCounts = new int[] { getLMRow().size(), getLMCol().size(),
				getLMVal().size() };
		// if (iRowCounts[0] == 0)
		// return "必须设置交叉行";
		// else
		if (iRowCounts[1] == 0)
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000856")/* @res "必须设置交叉列" */;
		else if (iRowCounts[2] == 0)
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000857")/* @res "必须设置交叉值" */;
		if (bDef) {
			// 砝码有效性
			if (!isWeightValid())
				return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000858")/* @res "交叉砝码必须设置于行或列上" */;
		}
		// 交叉合计列有效性
		int iLen = (m_strTotalCols == null) ? 0 : m_strTotalCols.length;
		String[] strVals = getVals();
		for (int i = 0; i < iLen; i++)
			if (!ModelUtil.isElement3(m_strTotalCols[i], strVals)) {
				System.out.println("无效交叉合计列: " + m_strTotalCols[i]);
				return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000859")/* @res "至少有一个交叉合计列不存在于交叉值列中" */;
			}
		// 构造哈希表
		Hashtable hashAliasFld = getHashAliasFldvo(m_allSfs);
		// 交叉分组列有效性
		iLen = (m_strGroupCols == null) ? 0 : m_strGroupCols.length;
		for (int i = 0; i < iLen; i++)
			if (!hashAliasFld.containsKey(m_strGroupCols[i])) {
				System.out.println("无效交叉分组列: " + m_strGroupCols[i]);
				return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000860")/* @res "至少有一个无效的交叉分组列" */;
			}
		return null;
	}

	/**
	 * 检查交叉设置 创建日期：(2003-12-13 14:04:17)
	 * 
	 * @return int
	 */
	public String checkCrossinfo() {
		int iLen = (m_allSfs == null) ? 0 : m_allSfs.length;
		int iAllSize = getLMAll().getSize();
		// int iRowSize = getLMRow().getSize();
		// int iColSize = getLMCol().getSize();
		// int iValSize = getLMVal().getSize();

		if (isWeightValid()) {
			if (iAllSize == iLen)
				return ""; // 交叉还原
			else
				return check(false);
		} else {
			if (iAllSize == iLen + 1)
				return ""; // 交叉还原
			else
				return check(false);
		}
	}

	/**
	 * connEtoC1: (BnAddRow.action.actionPerformed(java.awt.event.ActionEvent)
	 * -->
	 * RotateCrossPanel.bnAddRow_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * 
	 * @param arg1
	 *            java.awt.event.ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC1(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnAddRow_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC10: (BnUpRow.action.actionPerformed(java.awt.event.ActionEvent)
	 * -->
	 * RotateCrossPanel.bnUpRow_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * 
	 * @param arg1
	 *            java.awt.event.ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC10(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnUpRow_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC11: (BnDelRow1.action.actionPerformed(java.awt.event.ActionEvent)
	 * -->
	 * RotateCrossPanel.bnDelRow1_ActionPerformed(Ljava.awt.event.ActionEvent
	 * ;)V)
	 * 
	 * @param arg1
	 *            java.awt.event.ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC11(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnDownRow_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC12: (BnDownVal.action.actionPerformed(java.awt.event.ActionEvent)
	 * -->
	 * RotateCrossPanel.bnDownVal_ActionPerformed(Ljava.awt.event.ActionEvent
	 * ;)V)
	 * 
	 * @param arg1
	 *            java.awt.event.ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC12(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnDownVal_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC13: (LabelVal.mouse.mouseClicked(java.awt.event.MouseEvent) -->
	 * RotateCrossPanel.labelVal_MouseClicked(Ljava.awt.event.MouseEvent;)V)
	 * 
	 * @param arg1
	 *            java.awt.event.MouseEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC13(java.awt.event.MouseEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.labelVal_MouseClicked(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC14: (LabelRow.mouse.mouseClicked(java.awt.event.MouseEvent) -->
	 * RotateCrossPanel.labelRow_MouseClicked(Ljava.awt.event.MouseEvent;)V)
	 * 
	 * @param arg1
	 *            java.awt.event.MouseEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC14(java.awt.event.MouseEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.labelRow_MouseClicked(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC2: (BnDelRow.action.actionPerformed(java.awt.event.ActionEvent)
	 * -->
	 * RotateCrossPanel.bnDelRow_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * 
	 * @param arg1
	 *            java.awt.event.ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC2(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnDelRow_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC3: (BnAddCol.action.actionPerformed(java.awt.event.ActionEvent)
	 * -->
	 * RotateCrossPanel.bnAddCol_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * 
	 * @param arg1
	 *            java.awt.event.ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC3(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnAddCol_ActionPerformed(arg1);
			Object[] objs = getListCol().getSelectedValues();
			if (objs != null && objs.length > 0)
				deal.deal(true, objs.length);

			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC4: (BnDelCol.action.actionPerformed(java.awt.event.ActionEvent)
	 * -->
	 * RotateCrossPanel.bnDelCol_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * 
	 * @param arg1
	 *            java.awt.event.ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC4(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			Object[] objs = getListCol().getSelectedValues();
			if (objs != null && objs.length > 0)
				deal.deal(false, objs.length);
			this.bnDelCol_ActionPerformed(arg1);

			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC5: (BnUpCol.action.actionPerformed(java.awt.event.ActionEvent)
	 * -->
	 * RotateCrossPanel.bnUpCol_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * 
	 * @param arg1
	 *            java.awt.event.ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC5(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnUpCol_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC6: (BnDownCol.action.actionPerformed(java.awt.event.ActionEvent)
	 * -->
	 * RotateCrossPanel.bnDownCol_ActionPerformed(Ljava.awt.event.ActionEvent
	 * ;)V)
	 * 
	 * @param arg1
	 *            java.awt.event.ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC6(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnDownCol_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC7: (BnAddVal.action.actionPerformed(java.awt.event.ActionEvent)
	 * -->
	 * RotateCrossPanel.bnAddVal_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * 
	 * @param arg1
	 *            java.awt.event.ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC7(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnAddVal_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC8: (BnDelVal.action.actionPerformed(java.awt.event.ActionEvent)
	 * -->
	 * RotateCrossPanel.bnDelVal_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * 
	 * @param arg1
	 *            java.awt.event.ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC8(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnDelVal_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC9: (BnUpVal.action.actionPerformed(java.awt.event.ActionEvent)
	 * -->
	 * RotateCrossPanel.bnUpVal_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * 
	 * @param arg1
	 *            java.awt.event.ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC9(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnUpVal_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * 设置行复合维度 创建日期：(2003-12-10 10:21:16)
	 */
	public void defRowFh() {
		// 弹框
		CrossFhSetDlg dlg = getCrossFhDlg();
		dlg.setRowFh(m_bRowFh);
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK)
			m_bRowFh = dlg.isRowFh();
	}

	/**
	 * 设置列复合维度 创建日期：(2003-12-10 10:21:16)
	 */
	public void defColFh() {
		// 弹框
		CrossFhSetDlg dlg = getCrossFhDlg();
		dlg.setRowFh(m_bColFh);
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK)
			m_bColFh = dlg.isRowFh();
	}

	/**
	 * 设置交叉合计信息 创建日期：(2003-12-10 10:21:16)
	 */
	public void defTotal() {
		// 获得交叉值
		SelectFldVO[] sfVals = getSfVals();
		// 弹框
		CrossTotalDlg dlg = getCrossTotalDlg();
		dlg.setSumCols(sfVals, m_strTotalCols);
		dlg.setGroupCols(m_allSfs, m_strGroupCols);
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			m_strTotalCols = dlg.getSumCols();
			m_strGroupCols = dlg.getGroupCols();
		}
	}

	/**
	 * 返回 BnAddCol 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBnAddCol() {
		if (ivjBnAddCol == null) {
			try {
				ivjBnAddCol = new nc.ui.pub.beans.UIButton();
				ivjBnAddCol.setName("BnAddCol");
				ivjBnAddCol.setPreferredSize(new java.awt.Dimension(70, 22));
				ivjBnAddCol.setText("→");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnAddCol;
	}

	/**
	 * 返回 BnAddRow 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBnAddRow() {
		if (ivjBnAddRow == null) {
			try {
				ivjBnAddRow = new nc.ui.pub.beans.UIButton();
				ivjBnAddRow.setName("BnAddRow");
				ivjBnAddRow.setPreferredSize(new java.awt.Dimension(70, 22));
				ivjBnAddRow.setText("→");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnAddRow;
	}

	/**
	 * 返回 BnAddVal 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBnAddVal() {
		if (ivjBnAddVal == null) {
			try {
				ivjBnAddVal = new nc.ui.pub.beans.UIButton();
				ivjBnAddVal.setName("BnAddVal");
				ivjBnAddVal.setPreferredSize(new java.awt.Dimension(70, 22));
				ivjBnAddVal.setText("→");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnAddVal;
	}

	/**
	 * 返回 BnDelCol 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBnDelCol() {
		if (ivjBnDelCol == null) {
			try {
				ivjBnDelCol = new nc.ui.pub.beans.UIButton();
				ivjBnDelCol.setName("BnDelCol");
				ivjBnDelCol.setText("←");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnDelCol;
	}

	/**
	 * 返回 BnDelRow 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBnDelRow() {
		if (ivjBnDelRow == null) {
			try {
				ivjBnDelRow = new nc.ui.pub.beans.UIButton();
				ivjBnDelRow.setName("BnDelRow");
				ivjBnDelRow.setText("←");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnDelRow;
	}

	/**
	 * 返回 BnDelVal 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBnDelVal() {
		if (ivjBnDelVal == null) {
			try {
				ivjBnDelVal = new nc.ui.pub.beans.UIButton();
				ivjBnDelVal.setName("BnDelVal");
				ivjBnDelVal.setText("←");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnDelVal;
	}

	/**
	 * 返回 BnDownCol 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBnDownCol() {
		if (ivjBnDownCol == null) {
			try {
				ivjBnDownCol = new nc.ui.pub.beans.UIButton();
				ivjBnDownCol.setName("BnDownCol");
				ivjBnDownCol.setText("↓");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnDownCol;
	}

	/**
	 * 返回 BnDelRow1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBnDownRow() {
		if (ivjBnDownRow == null) {
			try {
				ivjBnDownRow = new nc.ui.pub.beans.UIButton();
				ivjBnDownRow.setName("BnDownRow");
				ivjBnDownRow.setText("↓");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnDownRow;
	}

	/**
	 * 返回 BnDownVal 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBnDownVal() {
		if (ivjBnDownVal == null) {
			try {
				ivjBnDownVal = new nc.ui.pub.beans.UIButton();
				ivjBnDownVal.setName("BnDownVal");
				ivjBnDownVal.setText("↓");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnDownVal;
	}

	/**
	 * 返回 BnUpCol 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBnUpCol() {
		if (ivjBnUpCol == null) {
			try {
				ivjBnUpCol = new nc.ui.pub.beans.UIButton();
				ivjBnUpCol.setName("BnUpCol");
				ivjBnUpCol.setPreferredSize(new java.awt.Dimension(70, 22));
				ivjBnUpCol.setText("↑");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnUpCol;
	}

	/**
	 * 返回 BnUpRow 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBnUpRow() {
		if (ivjBnUpRow == null) {
			try {
				ivjBnUpRow = new nc.ui.pub.beans.UIButton();
				ivjBnUpRow.setName("BnUpRow");
				ivjBnUpRow.setPreferredSize(new java.awt.Dimension(70, 22));
				ivjBnUpRow.setText("↑");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnUpRow;
	}

	/**
	 * 返回 BnUpVal 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBnUpVal() {
		if (ivjBnUpVal == null) {
			try {
				ivjBnUpVal = new nc.ui.pub.beans.UIButton();
				ivjBnUpVal.setName("BnUpVal");
				ivjBnUpVal.setPreferredSize(new java.awt.Dimension(70, 22));
				ivjBnUpVal.setText("↑");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnUpVal;
	}

	/**
	 * 复合维度设置框 创建日期：(2003-12-10 10:30:06)
	 * 
	 * @return nc.ui.iuforeport.businessquery.CrossTotalDlg
	 */
	public CrossFhSetDlg getCrossFhDlg() {
		if (m_crossFhDlg == null)
			m_crossFhDlg = new CrossFhSetDlg(this);
		return m_crossFhDlg;
	}

	/**
	 * 交叉合计设置框 创建日期：(2003-12-10 10:30:06)
	 * 
	 * @return nc.ui.iuforeport.businessquery.CrossTotalDlg
	 */
	public CrossTotalDlg getCrossTotalDlg() {
		if (m_crossTotalDlg == null)
			m_crossTotalDlg = new CrossTotalDlg(this);
		return m_crossTotalDlg;
	}

	/**
	 * 获得别名-字段VO哈希表 创建日期：(2003-12-10 13:36:59)
	 * 
	 * @return java.util.Hashtable
	 */
	private Hashtable getHashAliasFldvo(SelectFldVO[] sfs) {
		// 构造哈希表
		Hashtable hashAliasFld = new Hashtable();
		int iLen = (sfs == null) ? 0 : sfs.length;
		for (int i = 0; i < iLen; i++)
			hashAliasFld.put(sfs[i].getFldalias().toLowerCase(), sfs[i]);
		return hashAliasFld;
	}

	/**
	 * 返回 LabelCol 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getLabelCol() {
		if (ivjLabelCol == null) {
			try {
				ivjLabelCol = new nc.ui.pub.beans.UILabel();
				ivjLabelCol.setName("LabelCol");
				ivjLabelCol
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000733")/* @res "列" */);
				ivjLabelCol.setToolTipText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("10241201", "UPP10241201-000679")/*
																	 * @res
																	 * "设置复合维度"
																	 */);
				ivjLabelCol.setForeground(java.awt.Color.black);
				ivjLabelCol
						.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
				ivjLabelCol
						.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
				// ivjLabelCol.setILabelType(0/** Java默认(自定义)*/);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLabelCol;
	}

	/**
	 * 返回 LabelRow 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getLabelRow() {
		if (ivjLabelRow == null) {
			try {
				ivjLabelRow = new nc.ui.pub.beans.UILabel();
				ivjLabelRow.setName("LabelRow");
				ivjLabelRow
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000038")/* @res "行" */);
				ivjLabelRow.setToolTipText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("10241201", "UPP10241201-000679")/*
																	 * @res
																	 * "设置复合维度"
																	 */);
				ivjLabelRow.setForeground(java.awt.Color.black);
				ivjLabelRow.setHorizontalAlignment(SwingConstants.CENTER);
				ivjLabelRow
						.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
				ivjLabelRow.setILabelType(0/** Java默认(自定义) */
				);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLabelRow;
	}

	/**
	 * 返回 LabelVal 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getLabelVal() {
		if (ivjLabelVal == null) {
			try {
				ivjLabelVal = new nc.ui.pub.beans.UILabel();
				ivjLabelVal.setName("LabelVal");
				ivjLabelVal
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000268")/* @res "值" */);
				ivjLabelVal.setToolTipText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("10241201", "UPP10241201-000269")/*
																	 * @res
																	 * "计算小计"
																	 */);
				ivjLabelVal.setForeground(java.awt.Color.black);
				ivjLabelVal
						.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
				ivjLabelVal
						.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
				ivjLabelVal.setILabelType(0/** Java默认(自定义) */
				);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLabelVal;
	}

	/**
	 * 返回 ListAll 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIList
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIList getListAll() {
		if (ivjListAll == null) {
			try {
				ivjListAll = new nc.ui.pub.beans.UIList();
				ivjListAll.setName("ListAll");
				ivjListAll.setBounds(0, 0, 160, 120);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjListAll;
	}

	/**
	 * 返回 ListCol 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIList
	 */
	/* 警告：此方法将重新生成。 */
	public nc.ui.pub.beans.UIList getListCol() {
		if (ivjListCol == null) {
			try {
				ivjListCol = new nc.ui.pub.beans.UIList();
				ivjListCol.setName("ListCol");
				ivjListCol.setBounds(0, 0, 160, 120);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjListCol;
	}

	/**
	 * 返回 ListRow 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIList
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIList getListRow() {
		if (ivjListRow == null) {
			try {
				ivjListRow = new nc.ui.pub.beans.UIList();
				ivjListRow.setName("ListRow");
				ivjListRow.setBounds(0, 0, 160, 120);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjListRow;
	}

	/**
	 * 返回 ListVal 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIList
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIList getListVal() {
		if (ivjListVal == null) {
			try {
				ivjListVal = new nc.ui.pub.beans.UIList();
				ivjListVal.setName("ListVal");
				ivjListVal.setBounds(0, 0, 160, 120);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjListVal;
	}

	/**
	 * 创建日期：(2003-10-31 19:28:22)
	 * 
	 * @return javax.swing.DefaultListModel
	 */
	private DefaultListModel getLMAll() {
		return (DefaultListModel) getListAll().getModel();
	}

	/**
	 * 创建日期：(2003-10-31 19:28:22)
	 * 
	 * @return javax.swing.DefaultListModel
	 */
	private DefaultListModel getLMCol() {
		return (DefaultListModel) getListCol().getModel();
	}

	/**
	 * 创建日期：(2003-10-31 19:28:22)
	 * 
	 * @return javax.swing.DefaultListModel
	 */
	private DefaultListModel getLMRow() {
		return (DefaultListModel) getListRow().getModel();
	}

	/**
	 * 创建日期：(2003-10-31 19:28:22)
	 * 
	 * @return javax.swing.DefaultListModel
	 */
	private DefaultListModel getLMVal() {
		return (DefaultListModel) getListVal().getModel();
	}

	/**
	 * 返回 PnCenter 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getPnCenter() {
		if (ivjPnCenter == null) {
			try {
				ivjPnCenter = new nc.ui.pub.beans.UIPanel();
				ivjPnCenter.setName("PnCenter");
				ivjPnCenter.setLayout(getPnCenterGridLayout());
				getPnCenter().add(getPnRowBn(), getPnRowBn().getName());
				getPnCenter().add(getPnColBn(), getPnColBn().getName());
				getPnCenter().add(getPnValBn(), getPnValBn().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnCenter;
	}

	/**
	 * 返回 PnCenterGridLayout 特性值。
	 * 
	 * @return java.awt.GridLayout
	 */
	/* 警告：此方法将重新生成。 */
	private java.awt.GridLayout getPnCenterGridLayout() {
		java.awt.GridLayout ivjPnCenterGridLayout = null;
		try {
			/* 创建部件 */
			ivjPnCenterGridLayout = new java.awt.GridLayout(3, 1);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnCenterGridLayout;
	}

	/**
	 * 返回 PnColBn 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getPnColBn() {
		if (ivjPnColBn == null) {
			try {
				ivjPnColBn = new nc.ui.pub.beans.UIPanel();
				ivjPnColBn.setName("PnColBn");
				ivjPnColBn.setBorder(new javax.swing.border.EtchedBorder());
				ivjPnColBn
						.setLayout(new BoxLayout(ivjPnColBn, BoxLayout.Y_AXIS));

				ivjPnColBn.add(Box.createVerticalGlue());

				UIPanel pnNorth = new UIPanel(new FlowLayout());
				pnNorth.add(getBnAddCol());
				pnNorth.add(getBnUpCol());
				ivjPnColBn.add(pnNorth);

				UIPanel pnCenter = new UIPanel(new FlowLayout(
						FlowLayout.CENTER, 5, 0));
				pnCenter.add(getLabelCol());
				ivjPnColBn.add(pnCenter);

				UIPanel pnSouth = new UIPanel();
				pnSouth.add(getBnDelCol());
				pnSouth.add(getBnDownCol());
				ivjPnColBn.add(pnSouth);

				ivjPnColBn.add(Box.createVerticalGlue());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnColBn;
	}

	/**
	 * 返回 PnEast 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getPnEast() {
		if (ivjPnEast == null) {
			try {
				ivjPnEast = new nc.ui.pub.beans.UIPanel();
				ivjPnEast.setName("PnEast");
				ivjPnEast.setPreferredSize(new java.awt.Dimension(240, 10));
				ivjPnEast.setLayout(getPnEastGridLayout());
				getPnEast().add(getSclPnRow(), getSclPnRow().getName());
				getPnEast().add(getSclPnCol(), getSclPnCol().getName());
				getPnEast().add(getSclPnVal(), getSclPnVal().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnEast;
	}

	/**
	 * 返回 PnEastGridLayout 特性值。
	 * 
	 * @return java.awt.GridLayout
	 */
	/* 警告：此方法将重新生成。 */
	private java.awt.GridLayout getPnEastGridLayout() {
		java.awt.GridLayout ivjPnEastGridLayout = null;
		try {
			/* 创建部件 */
			ivjPnEastGridLayout = new java.awt.GridLayout(3, 1);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnEastGridLayout;
	}

	/**
	 * 返回 PnRowBn 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getPnRowBn() {
		if (ivjPnRowBn == null) {
			try {
				ivjPnRowBn = new nc.ui.pub.beans.UIPanel();
				ivjPnRowBn.setName("PnRowBn");
				ivjPnRowBn.setBorder(new javax.swing.border.EtchedBorder());
				ivjPnRowBn
						.setLayout(new BoxLayout(ivjPnRowBn, BoxLayout.Y_AXIS));

				ivjPnRowBn.add(Box.createVerticalGlue());

				UIPanel pnNorth = new UIPanel(new FlowLayout());
				pnNorth.add(getBnAddRow());
				pnNorth.add(getBnUpRow());
				ivjPnRowBn.add(pnNorth);

				UIPanel pnCenter = new UIPanel(new FlowLayout(
						FlowLayout.CENTER, 5, 0));
				pnCenter.add(getLabelRow());
				ivjPnRowBn.add(pnCenter);

				UIPanel pnSouth = new UIPanel();
				pnSouth.add(getBnDelRow());
				pnSouth.add(getBnDownRow());
				ivjPnRowBn.add(pnSouth);

				ivjPnRowBn.add(Box.createVerticalGlue());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnRowBn;
	}

	/**
	 * 返回 PnValBn 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getPnValBn() {
		if (ivjPnValBn == null) {
			try {
				ivjPnValBn = new nc.ui.pub.beans.UIPanel();
				ivjPnValBn.setName("PnValBn");
				ivjPnValBn.setBorder(new javax.swing.border.EtchedBorder());
				ivjPnValBn
						.setLayout(new BoxLayout(ivjPnValBn, BoxLayout.Y_AXIS));

				ivjPnValBn.add(Box.createVerticalGlue());

				UIPanel pnNorth = new UIPanel(new FlowLayout());
				pnNorth.add(getBnAddVal());
				pnNorth.add(getBnUpVal());
				ivjPnValBn.add(pnNorth);

				UIPanel pnCenter = new UIPanel(new FlowLayout(
						FlowLayout.CENTER, 5, 0));
				pnCenter.add(getLabelVal());
				ivjPnValBn.add(pnCenter);

				UIPanel pnSouth = new UIPanel();
				pnSouth.add(getBnDelVal());
				pnSouth.add(getBnDownVal());
				ivjPnValBn.add(pnSouth);

				ivjPnValBn.add(Box.createVerticalGlue());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnValBn;
	}

	/**
	 * 获得旋转交叉VO 创建日期：(2003-11-25 10:44:05)
	 * 
	 * @param rcs
	 *            nc.vo.pub.querymodel.RotateCrossVO[]
	 */
	public RotateCrossVO getRotateCross() {

		// 交叉行
		int iSize = getLMRow().size();
		String[] strRows = new String[iSize];
		String[] rowsName = new String[iSize];
		for (int i = 0; i < iSize; i++) {
			if (getLMRow().elementAt(i) instanceof SelectFldVO) {
				SelectFldVO sf = (SelectFldVO) getLMRow().elementAt(i);
				strRows[i] = sf.getFldalias();
				rowsName[i] = sf.getFldname();
			} else
				strRows[i] = QueryConst.CROSS_WEIGHT;
		}
		// 交叉列
		iSize = getLMCol().size();
		String[] strCols = new String[iSize];
		String[] colsName = new String[iSize];
		for (int i = 0; i < iSize; i++) {
			if (getLMCol().elementAt(i) instanceof SelectFldVO) {
				SelectFldVO sf = (SelectFldVO) getLMCol().elementAt(i);
				strCols[i] = sf.getFldalias();
				colsName[i] = sf.getFldname();
			} else
				strCols[i] = QueryConst.CROSS_WEIGHT;
		}
		// 交叉值
		String[] strVals = getVals();
		String[] valsName = getvalsName();

		// 组织返回值
		RotateCrossVO rc = new RotateCrossVO();
		rc.setStrRows(strRows);
		rc.setStrCols(strCols);
		rc.setStrVals(strVals);
		rc.setRowsName(rowsName);
		rc.setColsName(colsName);
		rc.setValsName(valsName);
		rc.setValTotal(m_strTotalCols);
		rc.setValGroup(m_strGroupCols);
		rc.setRowFh(m_bRowFh);
		rc.setColFh(m_bColFh);

		// 交叉列排序器类名
		rc.setColComparatorClass(m_strColComparatorClass);
		return rc;
	}

	/**
	 * 返回 SclPnAll 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIScrollPane getSclPnAll() {
		if (ivjSclPnAll == null) {
			try {
				ivjSclPnAll = new nc.ui.pub.beans.UIScrollPane();
				ivjSclPnAll.setName("SclPnAll");
				ivjSclPnAll.setPreferredSize(new java.awt.Dimension(240, 3));
				getSclPnAll().setViewportView(getListAll());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjSclPnAll;
	}

	/**
	 * 返回 SclPnCol 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIScrollPane getSclPnCol() {
		if (ivjSclPnCol == null) {
			try {
				ivjSclPnCol = new nc.ui.pub.beans.UIScrollPane();
				ivjSclPnCol.setName("SclPnCol");
				getSclPnCol().setViewportView(getListCol());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjSclPnCol;
	}

	/**
	 * 返回 SclPnRow 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIScrollPane getSclPnRow() {
		if (ivjSclPnRow == null) {
			try {
				ivjSclPnRow = new nc.ui.pub.beans.UIScrollPane();
				ivjSclPnRow.setName("SclPnRow");
				getSclPnRow().setViewportView(getListRow());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjSclPnRow;
	}

	/**
	 * 返回 SclPnVal 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIScrollPane getSclPnVal() {
		if (ivjSclPnVal == null) {
			try {
				ivjSclPnVal = new nc.ui.pub.beans.UIScrollPane();
				ivjSclPnVal.setName("SclPnVal");
				getSclPnVal().setViewportView(getListVal());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjSclPnVal;
	}

	/**
	 * 获得交叉值数组（SELECTFLDVO） 创建日期：(2003-12-10 11:08:56)
	 * 
	 * @return java.lang.String[]
	 */
	public SelectFldVO[] getSfVals() {

		int iSize = getLMVal().size();
		SelectFldVO[] sfVals = new SelectFldVO[iSize];
		for (int i = 0; i < iSize; i++)
			sfVals[i] = (SelectFldVO) getLMVal().elementAt(i);
		return sfVals;
	}

	/**
	 * 获得交叉值数组 创建日期：(2003-12-10 11:08:56)
	 * 
	 * @return java.lang.String[]
	 */
	public String[] getVals() {

		int iSize = getLMVal().size();
		String[] strVals = new String[iSize];
		for (int i = 0; i < iSize; i++) {
			SelectFldVO sf = (SelectFldVO) getLMVal().elementAt(i);
			strVals[i] = sf.getFldalias();
		}
		return strVals;
	}

	/**
	 * 蒋磊-20050912-增加旋转交叉定义的值名称
	 * 
	 * @return
	 */
	public String[] getvalsName() {
		int iSize = getLMVal().size();
		String[] valsName = new String[iSize];
		for (int i = 0; i < iSize; i++) {
			SelectFldVO sf = (SelectFldVO) getLMVal().elementAt(i);
			valsName[i] = sf.getFldname();
		}
		return valsName;
	}

	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		System.out.println("--------- 未捕捉到的异常 ---------");
		exception.printStackTrace(System.out);
	}

	/**
	 * 初始化连接
	 * 
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	/* 警告：此方法将重新生成。 */
	private void initConnections() throws java.lang.Exception {
		// user code begin {1}
		// user code end
		getBnAddRow().addActionListener(ivjEventHandler);
		getBnDelRow().addActionListener(ivjEventHandler);
		getBnUpRow().addActionListener(ivjEventHandler);
		getBnDownRow().addActionListener(ivjEventHandler);
		getBnAddCol().addActionListener(ivjEventHandler);
		getBnDelCol().addActionListener(ivjEventHandler);
		getBnUpCol().addActionListener(ivjEventHandler);
		getBnDownCol().addActionListener(ivjEventHandler);
		getBnAddVal().addActionListener(ivjEventHandler);
		getBnDelVal().addActionListener(ivjEventHandler);
		getBnUpVal().addActionListener(ivjEventHandler);
		getBnDownVal().addActionListener(ivjEventHandler);
		getLabelVal().addMouseListener(ivjEventHandler);
		getLabelRow().addMouseListener(ivjEventHandler);
		getLabelCol().addMouseListener(ivjEventHandler);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("RotateCrossPanel");
			setLayout(new java.awt.BorderLayout());
			setSize(600, 320);
			add(getSclPnAll(), "West");
			add(getPnEast(), "East");
			add(getPnCenter(), "Center");
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// 列表模型
		getListAll().setModel(new DefaultListModel());
		getListRow().setModel(new DefaultListModel());
		getListCol().setModel(new DefaultListModel());
		getListVal().setModel(new DefaultListModel());
		// 渲染器
		getListAll().setCellRenderer(new RotateCrossCellRenderer());
		getListRow().setCellRenderer(new RotateCrossCellRenderer());
		getListCol().setCellRenderer(new RotateCrossCellRenderer());
		getListVal().setCellRenderer(new RotateCrossCellRenderer());
		// user code end
	}

	/**
	 * 判断交叉砝码是否有效 创建日期：(2003-11-25 10:44:05)
	 * 
	 * @param rcs
	 *            nc.vo.pub.querymodel.RotateCrossVO[]
	 */
	public boolean isWeightValid() {

		// 交叉行
		int iSize = getLMRow().size();
		for (int i = 0; i < iSize; i++)
			if (getLMRow().elementAt(i).toString().equals(
					QueryConst.CROSS_WEIGHT))
				return true;
		// 交叉列
		iSize = getLMCol().size();
		for (int i = 0; i < iSize; i++)
			if (getLMCol().elementAt(i).toString().equals(
					QueryConst.CROSS_WEIGHT))
				return true;

		return false;
	}

	/**
	 * 行复合维度设置
	 */
	public void labelRow_MouseClicked(java.awt.event.MouseEvent mouseEvent) {
		if (mouseEvent.getClickCount() == 1)
			defRowFh();
	}

	/**
	 * 列复合维度设置
	 */
	public void labelCol_MouseClicked(java.awt.event.MouseEvent mouseEvent) {
		if (mouseEvent.getClickCount() == 1) {
			if (mouseEvent.isControlDown()) {
				// 设置列排序器
				defComparator();
			} else {
				defColFh();
			}
		}
	}

	/**
	 * 交叉合计设置
	 */
	public void labelVal_MouseClicked(java.awt.event.MouseEvent mouseEvent) {
		if (mouseEvent.getClickCount() == 1) {
			if (getSfVals().length == 0)
				MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10241201",
								"UPP10241201-000099")/*
													 * @res "查询引擎"
													 */, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10241201",
								"UPP10241201-000861")/*
													 * @res
													 * "只有选择了交叉值，才能够进行交叉合计设置"
													 */);
			else
				defTotal();
		}
	}

	/**
	 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	public static void main(java.lang.String[] args) {
		try {
			JFrame frame = new javax.swing.JFrame();
			RotateCrossPanel aRotateCrossPanel;
			aRotateCrossPanel = new RotateCrossPanel();
			frame.setContentPane(aRotateCrossPanel);
			frame.setSize(aRotateCrossPanel.getSize());
			frame.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				};
			});
			frame.setVisible(true);
			java.awt.Insets insets = frame.getInsets();
			frame.setSize(frame.getWidth() + insets.left + insets.right, frame
					.getHeight()
					+ insets.top + insets.bottom);
			frame.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("nc.ui.pub.beans.UIPanel 的 main() 中发生异常");
			exception.printStackTrace(System.out);
		}
	}

	/**
	 * 设置按钮可用性 创建日期：(2003-11-8 16:30:08)
	 * 
	 * @param bEnable
	 *            boolean
	 */
	public void setBnEnabled(boolean bEnable) {
		getBnAddRow().setEnabled(bEnable);
		getBnDelRow().setEnabled(bEnable);
		getBnAddCol().setEnabled(bEnable);
		getBnDelCol().setEnabled(bEnable);
		getBnAddVal().setEnabled(bEnable);
		getBnDelVal().setEnabled(bEnable);
	}

	/**
	 * 设置旋转交叉VO 创建日期：(2003-11-25 10:44:05)
	 * 
	 * @param rcs
	 *            nc.vo.pub.querymodel.RotateCrossVO[]
	 */
	public void setRotateCross(RotateCrossVO rc, SelectFldVO[] sfs) {

		m_allSfs = sfs;
		int iLen = (sfs == null) ? 0 : sfs.length;
		// 构造哈希表
		Hashtable hashAliasFld = getHashAliasFldvo(sfs);
		// 获得交叉信息
		String[] strRows = null;
		String[] strCols = null;
		String[] strVals = null;
		if (rc == null) {
			// 清空
			getLMRow().removeAllElements();
			getLMCol().removeAllElements();
			getLMVal().removeAllElements();
			// 加交叉砝码
			getLMCol().addElement(QueryConst.CROSS_WEIGHT);
			// 清空交叉小计和分组信息
			m_strTotalCols = null;
			m_strGroupCols = null;
			m_bRowFh = true;
			m_bColFh = false;
			m_strColComparatorClass = null;
			getCrossTotalDlg().setSumCols(null, null);
			getCrossTotalDlg().setGroupCols(sfs, null);
			getCrossFhDlg().setRowFh(true);
		} else {
			strRows = rc.getStrRows();
			strCols = rc.getStrCols();
			strVals = rc.getStrVals();
			m_strTotalCols = rc.getValTotals();
			m_strGroupCols = rc.getValGroups();
			m_bRowFh = rc.isRowFh();
			m_bColFh = rc.isColFh();
			m_strColComparatorClass = rc.getColComparatorClass();
			// 交叉行
			iLen = (strRows == null) ? 0 : strRows.length;
			for (int i = 0; i < iLen; i++)
				if (strRows[i].equals(QueryConst.CROSS_WEIGHT))
					getLMRow().addElement(strRows[i]);
				else {
					strRows[i] = strRows[i].toLowerCase();
					if (hashAliasFld.containsKey(strRows[i])) {
						getLMRow().addElement(hashAliasFld.get(strRows[i]));
						hashAliasFld.remove(strRows[i]);
					}
				}
			// 交叉列
			iLen = (strCols == null) ? 0 : strCols.length;
			for (int i = 0; i < iLen; i++)
				// if (strCols[i] != null)
				if (strCols[i].equals(QueryConst.CROSS_WEIGHT))
					getLMCol().addElement(strCols[i]);
				else {
					strCols[i] = strCols[i].toLowerCase();
					if (hashAliasFld.containsKey(strCols[i])) {
						getLMCol().addElement(
								hashAliasFld.get(strCols[i].toLowerCase()));
						hashAliasFld.remove(strCols[i]);
					}
				}
			// 交叉值
			iLen = (strVals == null) ? 0 : strVals.length;
			for (int i = 0; i < iLen; i++) {
				strVals[i] = strVals[i].toLowerCase();
				if (hashAliasFld.containsKey(strVals[i])) {
					getLMVal().addElement(
							hashAliasFld.get(strVals[i].toLowerCase()));
					hashAliasFld.remove(strVals[i]);
				}
			}
		}

		// 退化项
		getLMAll().removeAllElements();
		Enumeration nodes = hashAliasFld.keys();
		while (nodes.hasMoreElements()) {
			String key = nodes.nextElement().toString();
			getLMAll().addElement(hashAliasFld.get(key));
		}
	}

	/**
	 * 设置列排序器 创建日期：(2003-12-10 10:21:16)
	 */
	public void defComparator() {
		// 设置列排序器
		Object objClassName = MessageDialog.showInputDlg(this,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000099")/* @res "查询引擎" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001340")/* @res "交叉列排序器类名：" */,
				m_strColComparatorClass, 100);
		if (objClassName != null && !objClassName.toString().trim().equals("")) {
			try {
				Class.forName(objClassName.toString());
				m_strColComparatorClass = objClassName.toString();
			} catch (Exception e) {
				e.printStackTrace();
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10241201",
								"UPP10241201-000099")/* @res "查询引擎" */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
								"UPP10241201-001341")/* @res "该类不存在" */);
			}
		} else {
			m_strColComparatorClass = null;
		}
	}
}
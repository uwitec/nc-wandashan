package nc.ui.wds.tranprice.transmil;

/**
 * Excel导入 注意事项： 对Excel文件要求： 每个工作簿的数据结构必须一致 待选列名以第一个工作簿的列名为准，所选列名必须在VO的字段中存在
 * 每个工作簿的列名必须位于第一行，数据行必须从第二行开始（因为数据从第二行开始读） 列名中不能出现合并单元格 列名不能重复 使用Excel导入对话框:
 * 需要设置VO的name，包名＋VO类名 方法：setVOName(String)设置VO类名
 * 导入指定行数的数据setRowCount(int)如果不设置则默认为导入所有数据； 设置时则导入指定行数的数据（主要用于批处理数据）
 * 方法：setRowCount(int)设置每次导出行数 方法：getExportVO()获取导出VO 方法：Next()
 */

import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import nc.bs.logging.Logger;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIListToList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.layout.TableLayout;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.wl.pub.MedTimeLog;

@SuppressWarnings( { "unchecked", "serial", "deprecation", "unused",
		"static-access" })
public class ExcelDialog extends nc.ui.pub.beans.UIDialog implements
		javax.swing.event.ChangeListener,
		javax.swing.event.ListSelectionListener, ActionListener {

	private javax.swing.JPanel pnlContent = null;

	private UIPanel pnlExcel = null;

	private UITablePane ivjUITablePane = null;

	private UIScrollPane spWorkBook = null;

	private UIScrollPane spField = null;

	private UIPanel pnlList = null;

	private UIPanel pnlWorkBook = null;

	private UIPanel pnlField = null;

	private UIList lstWorkBook = null;

	private UIList lstField = null;

	private UILabel lblWorkBook = null;

	private UILabel lblField = null;

	private UICheckBox ckbWorkBook = null;

	private UICheckBox ckbField = null;

	private UIPanel pnlButton = null;

	private UIButton btnOpenFile = null;

	private UIButton btnImport = null;

	private UIButton btnCancel = null;

	private nc.ui.pub.beans.UICheckBox ckbImport = null;

	private UIPanel pnlMiddle = null;

	private UIFileChooser fcFileChooser = null;

	private String sourcefile = null;

	private java.io.File m_fPathFile = null;

	private Workbook m_workbook = null;

	private Sheet[] m_sheet = null;

	private Object[][] m_columns;

	// 导入到VO或数据库标识
	private boolean m_bImportVOFlag = true;

	// Excel中导出数据的当前行
	private int m_iCurrentRow = 1;

	// 每次导出数据的行数
	private int m_iRowCount = 0;

	// 导出到VO
	private CircularlyAccessibleValueObject[] m_voExport = null;

	//
	private String m_sVOName = null;

	// *************使用UIListToList代替UIList和UICheckBox******************
	private UIListToList ltlWorkBook = null;

	// private UIListToList ltlField = null;

	// private HashMap m_hmSheetField = null;
	// private Vector m_vSheetName = null;
	// private Vector m_vcolumn = null;

	private boolean m_bExportMark = false;

	private int m_iCurrentSheetIndex = 0;

	private UICheckBox ckbSingle = null;

	private boolean m_bClose = false;

	private boolean m_bMultiImport = false;

	private boolean m_bSingle;// 是否单列导出

	private UICheckBox m_ckbFirstRow = null;

	private int m_iLen = 0;

	// 工作簿、列
	private InputStream m_is = null;

	private int m_iSingleCols = 0;

	// *************增加Excel表中列名和VO中字段名的对应关系******************
	private nc.ui.pub.bill.BillCardPanel m_pnlField = null;

	private String m_sFileName = null;

	private String m_sSeparator = null;

	private String[][] m_voCHandENnames = null;

	private String[] m_vofields = null;

	// *************增加单行导入，单行数据使用分隔符分开的情况***************
	private UIPanel pnlSingle = null;

	private UITextField txtSeparator = null;

	private java.awt.Container clientUI = null;

	/**
	 * ExcelImportDialog 构造子注解。
	 */
	public ExcelDialog() {
		super();
		init();
	}

	/**
	 * ExcelImportDialog 构造子注解。
	 * 
	 * @param parent
	 *            java.awt.Container
	 */
	public ExcelDialog(java.awt.Container parent) {
		super(parent);
		clientUI = parent;
		init();
	}

	/**
	 * ExcelImportDialog 构造子注解。
	 * 
	 * @param parent
	 *            java.awt.Container
	 * @param title
	 *            java.lang.String
	 */
	public ExcelDialog(java.awt.Container parent, String title) {
		super(parent, title);
		init();
	}

	/**
	 * ExcelImportDialog 构造子注解。
	 * 
	 * @param owner
	 *            java.awt.Frame
	 */
	public ExcelDialog(java.awt.Frame owner) {
		super(owner);
		init();
	}

	/**
	 * ExcelImportDialog 构造子注解。
	 * 
	 * @param owner
	 *            java.awt.Frame
	 * @param title
	 *            java.lang.String
	 */
	public ExcelDialog(java.awt.Frame owner, String title) {
		super(owner, title);
		init();
	}

	/**
	 * Invoked when an action occurs.
	 */
	public void actionPerformed(java.awt.event.ActionEvent e) {
		try {
			if (e.getSource() == this.getUIButtonOpenFile()) {
				m_bExportMark = false;
				m_bClose = true;
				onOpenFile();
			} else if (e.getSource() == this.getUIButtonImport()) {
				onImport();
				m_bExportMark = true;
				if (m_bClose)
					this.closeOK();
			} else if (e.getSource() == this.getUIButtonCancel()) {
				m_bExportMark = false;
				m_bClose = true;
				this.closeCancel();
				return;
			}
		} catch (Exception e1) {
			Logger.info(e1);
			if (clientUI != null) {
				((ToftPanel) clientUI).showWarningMessage(e1.getMessage());
			}
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-23 9:01:52)
	 * 
	 * @return int
	 */
	private int getCurrentRow() {
		return m_iCurrentRow;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-23 2:50:49)
	 * 
	 * @return int
	 */
	private int getCurrentSheetIndex() {
		return m_iCurrentSheetIndex;
	}

	/**
	 * 从Excel中导出数据（非单列导出） 需要过滤空行,要将每行需导出的各个单元格进行空纪录判断 创建日期：(2004-4-23 2:50:49)
	 * 
	 * @return int
	 */
	private CircularlyAccessibleValueObject[] getDataFromExcel()
			throws Exception {
		Workbook rwb = getWorkBook(); // 获取工作簿对象
		Sheet[] sht = getSheet(); // 获取已选工作簿和列

		Object[][] cols = getColumns();
		int curshtindex = getCurrentSheetIndex();
		int currow = getCurrentRow();
		int k = currow;

		int remaincount = 0;
		int newcurrindex = curshtindex;
		int totalcount = 0;
		int iCount = 0;

		boolean bFirst = m_ckbFirstRow.isSelected(); // 是否选择第一行数据为列名称
		int iBegin = bFirst ? 1 : 0; // 数据起始行；如果选择，则从第一行开始导数据，否则从第二行开始导入数据

		boolean bIsNull = true; // 单元格记录是否为空
		String sTmp = "";

		Class c = Class.forName(getVOName());
		CircularlyAccessibleValueObject[] vos = null;
		CircularlyAccessibleValueObject vo = null;
		Vector vt = new Vector();

		if (curshtindex > sht.length - 1) {
			// 越界
			return null;
		}

		iCount = m_iRowCount;
		// 一次性导出
		if (m_iRowCount == 0) {
			m_bMultiImport = false;
			for (int t = 0; t < sht.length; t++) {
				if (sht[t].getRows() != 0) {
					iCount = iCount + sht[t].getRows() - iBegin;
				}
			}
		} else { // 分批导出
			// 分批导入时，判断最后一个工作簿中剩余记录
			// 如果最后一批记录数小于每次导出数，则将iCount置为最后剩余记录数
			if (curshtindex == sht.length - 1) {
				remaincount = sht[curshtindex].getRows() - currow;
				if (m_iRowCount > remaincount) {
					iCount = remaincount;
				}
			}

			totalcount = sht[curshtindex].getRows() - currow;
			if (m_iRowCount < totalcount) {
				newcurrindex = curshtindex;
			} else {
				for (int i = curshtindex + 1; i < sht.length; i++) {
					totalcount = totalcount + sht[i].getRows() - iBegin;
					if ((m_iRowCount - totalcount) < (sht[i].getRows() - iBegin)) {
						newcurrindex = i;
						break;
					}
				}
			}
		}
		if (iCount == 0)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = curshtindex; i < sht.length; i++, k = iBegin) {
			for (int t = 0; t < iCount;) {
				for (; (k < sht[i].getRows() && t < iCount); k++, t++) {
					bIsNull = true;
					sTmp = "";
					//
					vo = (CircularlyAccessibleValueObject) Class.forName(
							getVOName()).newInstance();
					//
					for (int j = 0; j < cols.length; j++) {
						Cell cl = sht[i].getCell((new Integer(cols[j][0]
								.toString())).intValue(), k);
						if (cl.getType() == CellType.DATE) {
							DateCell dc = (DateCell) cl;
							sTmp = sdf.format(dc.getDate());
						} else if (cl.getType() == CellType.NUMBER
								|| cl.getType() == CellType.NUMBER_FORMULA) {
							NumberCell nc = (NumberCell) cl;
							sTmp = String.valueOf(nc.getValue());
						} else {
							sTmp = cl.getContents().trim();
						}
						if (bIsNull && sTmp != null && !"".equals(sTmp)) {
							bIsNull = false;
						}
						Object[] o = (Object[]) cols[j][1];
						vo.setAttributeValue(o[0].toString(), sTmp);

						if (t >= iCount)
							break;
					}
					vt.add(vo);
					if (t >= iCount)
						break;
				}
	
				if (t >= iCount)
					break;
			}
		}

		if (vt.size() > 0) {
			vos = (CircularlyAccessibleValueObject[]) java.lang.reflect.Array
					.newInstance(c, vt.size());
			vt.copyInto(vos);
		}

		// 设置当前工作簿
		// 设置当前行
		if (m_bMultiImport) {
			setCurrentSheetIndex(newcurrindex);
		} else {
			setCurrentSheetIndex(sht.length);
		}
		setCurrentRow(k);
		return (CircularlyAccessibleValueObject[]) vos;
	}

	public CircularlyAccessibleValueObject[] getExportVO() throws Exception {
		return m_voExport;
	}

	/**
	 * 返回 InternalDialogContentPane 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JPanel getInternalDialogContentPane() {

		if (pnlContent == null) {
			try {
				pnlContent = new javax.swing.JPanel();
				pnlContent.setName("Content");

				pnlContent.setLayout(new java.awt.BorderLayout());
				UIPanel m_north = new UIPanel();
				m_north.setPreferredSize(new java.awt.Dimension(100, 10));
				UIPanel m_west = new UIPanel();
				m_west.setPreferredSize(new java.awt.Dimension(10, 100));
				UIPanel m_east = new UIPanel();
				m_east.setPreferredSize(new java.awt.Dimension(10, 100));
				UIPanel m_south = new UIPanel();
				m_south.setPreferredSize(new java.awt.Dimension(100, 10));

				getInternalDialogContentPane().add(m_north, "North");
				getInternalDialogContentPane().add(m_west, "West");
				getInternalDialogContentPane().add(m_east, "East");
				getInternalDialogContentPane().add(m_south, "South");

				getInternalDialogContentPane()
						.add(getUIPanelMiddle(), "Center");
			} catch (Exception e) {
				Logger.info(e);
			}
		}
		return pnlContent;
	}

	private java.io.File getPathFile() {
		return m_fPathFile;
	}

	private Sheet[] getSheet() {
		return m_sheet;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-19 16:02:20)
	 */
	private UIButton getUIButtonCancel() {
		if (btnCancel == null) {
			btnCancel = new UIButton();
			btnCancel.setName("UIButtonCancel");
			btnCancel.setToolTipText(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("scmpub", "UPPscmpub-000467")/*
																 * @res "取消导入"
																 */);
			btnCancel.setFont(new java.awt.Font("dialog", 0, 12));
			btnCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000008")/* @res "取消" */);
			btnCancel.setBounds(130, 0, 60, 22);
			btnCancel.addActionListener(this);
		}
		return btnCancel;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-19 16:02:20)
	 */
	private UIButton getUIButtonImport() {
		if (btnImport == null) {
			btnImport = new UIButton();
			btnImport.setName("UIButtonImport");
			btnImport.setToolTipText(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("scmpub", "UPPscmpub-000468")/* @res "导入" */);
			btnImport.setFont(new java.awt.Font("dialog", 0, 12));
			btnImport.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"scmpub", "UPPscmpub-000468")/* @res "导入" */);
			btnImport.setBounds(70, 0, 60, 22);

			btnImport.addActionListener(this);
		}
		return btnImport;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-19 16:02:20)
	 */
	private UIButton getUIButtonOpenFile() {
		if (btnOpenFile == null) {
			btnOpenFile = new UIButton();
			btnOpenFile.setName("UIButtonOpenFile");
			btnOpenFile.setToolTipText(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("scmpub", "UPPscmpub-000469")/*
																 * @res "打开文件"
																 */);
			btnOpenFile.setFont(new java.awt.Font("dialog", 0, 12));
			btnOpenFile.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"scmpub", "UPPscmpub-000470")/* @res "打开..." */);
			btnOpenFile.setBounds(0, 0, 60, 22);

			btnOpenFile.addActionListener(this);
		}
		return btnOpenFile;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-19 20:44:39)
	 * 
	 * @return javax.swing.JFileChooser
	 */
	private UIFileChooser getUIFileChooser() {
		if (fcFileChooser == null) {
			fcFileChooser = new UIFileChooser();
			MyFileFilter filter = new MyFileFilter();

			filter.addExtension("xls");
			filter.setDescription(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"scmpub", "UPPscmpub-001066")/*
													 * @res "Microsoft Excel 文件 "
													 */);
			fcFileChooser.setFileFilter(filter);
			fcFileChooser.addChoosableFileFilter(filter);

		}
		return fcFileChooser;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-19 16:02:20)
	 */
	public UIListToList getUIltlWorkBook() {
		if (ltlWorkBook == null) {
			ltlWorkBook = new UIListToList();
			ltlWorkBook.setName("UIltlWorkBook");
			ltlWorkBook.setEnabled(true);
			ltlWorkBook.setAutoscrolls(true);
			ltlWorkBook.setDisplayTitle(true);
			ltlWorkBook
					.setLeftText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"scmpub", "UPPscmpub-000472")/* @res "待选工作簿" */);
			ltlWorkBook
					.setRightText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"scmpub", "UPPscmpub-000473")/* @res "已选工作簿" */);
			ltlWorkBook.setPreferredSize(new java.awt.Dimension(500, 130));
		}
		return ltlWorkBook;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-19 16:02:20)
	 */
	private UIScrollPane getUIPaneField() {
		if (spField == null) {
			spField = new UIScrollPane();
			spField.setName("UIPaneField");

			UIPanel pnlField = new UIPanel();
			spField.setViewportView(getUIpnlField());
		}
		return spField;
	}

	/**
	 * 返回 UIPanel 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getUIPanelButton() {
		if (pnlButton == null) {
			pnlButton = new UIPanel();
			pnlButton.setName("UIPanelButton");
			pnlButton.setLayout(new java.awt.BorderLayout());

			UIPanel tmp1 = new UIPanel();
			tmp1.setLayout(new java.awt.BorderLayout());
			tmp1.setPreferredSize(new java.awt.Dimension(200, 22));
			tmp1.add(getUIButtonOpenFile(), java.awt.BorderLayout.WEST);
			tmp1.add(getUIButtonImport(), java.awt.BorderLayout.CENTER);
			tmp1.add(getUIButtonCancel(), java.awt.BorderLayout.EAST);

			UIPanel tmp2 = new UIPanel();
			tmp2.setLayout(new java.awt.BorderLayout());
			tmp2.setPreferredSize(new java.awt.Dimension(100, 22));

			UIPanel tmp3 = new UIPanel();
			tmp3.setLayout(new java.awt.BorderLayout());
			tmp3.setPreferredSize(new java.awt.Dimension(100, 22));

			pnlButton.add(tmp2, java.awt.BorderLayout.WEST);
			pnlButton.add(tmp1, java.awt.BorderLayout.CENTER);
			pnlButton.add(tmp3, java.awt.BorderLayout.EAST);

			pnlButton.setPreferredSize(new java.awt.Dimension(500, 22));

		}
		return pnlButton;
	}

	/**
	 * 返回 UIPanel 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getUIPanelButtonTL() {
		if (pnlButton == null) {
			pnlButton = new UIPanel();
			pnlButton.setName("UIPanelButton");
			double btn = 8;
			double f = TableLayout.FILL;
			double p = TableLayout.PREFERRED;
			double size[][] = { { f, p, btn, p, btn, p, f }, { p } };
			pnlButton.setLayout(new TableLayout(size));
			pnlButton.add(getUIButtonOpenFile(), "1,0");
			pnlButton.add(getUIButtonImport(), "3,0");
			pnlButton.add(getUIButtonCancel(), "5,0");

			pnlButton.setPreferredSize(new java.awt.Dimension(500, 22));

		}
		return pnlButton;
	}

	/**
	 * 返回 UIPanel 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getUIPanelList() {

		if (pnlList == null) {
			pnlList = new UIPanel();
			pnlList.setName("UIPanelList");
			pnlList.setLayout(new java.awt.BorderLayout());

			pnlList.add(getUIPaneWorkBook(), java.awt.BorderLayout.NORTH);
			pnlList.add(getUIPanelSingle(), java.awt.BorderLayout.CENTER);

			pnlList.setPreferredSize(new java.awt.Dimension(500, 520));
		}
		return pnlList;
	}

	/**
	 * 返回 UIPanel 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getUIPanelMiddle() {

		if (pnlMiddle == null) {
			pnlMiddle = new UIPanel();
			pnlMiddle.setName("UIPanelMiddle");
			pnlMiddle.setLayout(new java.awt.BorderLayout());

			pnlMiddle.add(getUIPanelList(), java.awt.BorderLayout.NORTH);
			pnlMiddle.add(getUIPanelButtonTL(), java.awt.BorderLayout.SOUTH);

			pnlMiddle.setPreferredSize(new java.awt.Dimension(500, 550));
		}
		return pnlMiddle;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-19 16:02:20)
	 */
	public UIScrollPane getUIPaneWorkBook() {
		if (spWorkBook == null) {
			spWorkBook = new UIScrollPane();
			spWorkBook.setName("UIPaneWorkBook");
			spWorkBook.setViewportView(getUIltlWorkBook());
		}
		return spWorkBook;
	}

	private String getVOName() {
		return m_sVOName;
	}

	private Workbook getWorkBook() {
		return m_workbook;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-19 14:18:30)
	 */
	private void init() {
		setName("ExcelImportDialog");
		setResizable(false);
		setSize(536, 602);
		setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub",
				"UPPscmpub-000474")/* @res "Excel导入" */);
		setContentPane(getInternalDialogContentPane());
	}

	public boolean isExportOK() {
		return m_bExportMark;
	}

	public void Next() throws Exception {
		if (m_bSingle)
			setExportVO(getDataFromExcelSingle());
		else
			setExportVO(getDataFromExcel());
	}

	private void onImport() throws Exception {
		try {
			// 校验：excelL列名，范例名不能为空
			List<Object> list = new ArrayList<Object>();
			for (int i = 0; i < getUIpnlField().getBillTable().getRowCount(); i++) {
				Object o = getUIpnlField().getBillModel().getValueAt(i, 0);
				if (list.contains(o)) {
					throw new Exception("EXCEL列名/范例数据重复!");
				}
				list.add(o);
			}
			// 校验:要导入的的工作簿
			if ((getUIltlWorkBook().getRightData() == null)
					|| (getUIltlWorkBook().getRightData().length == 0)) {
				throw new Exception("请选择工作簿");
				// throw new Exception(nc.ui.ml.NCLangRes.getInstance()
				// .getStrByID("scmpub", "UPPscmpub-000475")/*
				// * @res
				// * "请选择工作簿！"
				// */);
			}

			// 获得选中的工作簿
			Sheet[] sheets = new Sheet[getUIltlWorkBook().getRightData().length];
			for (int i = 0; i < getUIltlWorkBook().getRightData().length; i++) {
				for (int j = 0; j < getWorkBook().getNumberOfSheets(); j++) {
					if (getWorkBook().getSheet(j).getName().toString().equals(
							getUIltlWorkBook().getRightData()[i].toString())) {
						sheets[i] = getWorkBook().getSheet(j);
					}
				}
			}
			setSheet(sheets);

			// 将当前工作簿和当前行置为初始状态
			setCurrentSheetIndex(0);
			setCurrentRow(1);

			// 获得选中的字段
			int selectedrows = 0;
			getUIpnlField().stopEditing();
			for (int k = 0; k < getUIpnlField().getBillModel().getRowCount(); k++) {
				/* 此处有一个BUG：combo界面值发生改变时，如果focus直接离开billcardpanel，导致combo界面值和保存在billmodel中的对应值不一致 */
				if ((getUIpnlField().getBillModel().getValueAt(k, 1) != null)
						&& getUIpnlField().getBillModel().getValueAt(k, 1)
								.toString().trim().length() > 0) {
					selectedrows++;
				}
			}
			// 获得选中的字段和VO的对应关系
			Object[][] columns = new Object[selectedrows][2];
			selectedrows = 0;
			String[][] names = getCHandENnames();
			String vofieldname = null;
			for (int k = 0; k < getUIpnlField().getBillModel().getRowCount(); k++) {

				if ((getUIpnlField().getBillModel().getValueAt(k, 1) != null)
						&& getUIpnlField().getBillModel().getValueAt(k, 1)
								.toString().trim().length() > 0) {
					vofieldname = findCorrespondString((String) (getUIpnlField()
							.getBillModel().getValueAt(k, 1)));// getValueAt(k,"vocolname"))
					// );

					if (vofieldname != null) {
						Object o = getUIpnlField().getBillModel().getValueAt(k,
								1);
						// columns[selectedrows] = new Object[] { o ,
						// vofieldname };
						Object[] strs = new Object[] { vofieldname, o };
						// zpm
						columns[selectedrows] = new Object[] { new Integer(k),
								strs };
						selectedrows++;
					}
				}

			}

			setColumns(columns);

			if (getColumns().length == 0) {
				throw new Exception("请选择Excel列和导入名称的对应关系");
				//
				// throw new Exception(nc.ui.ml.NCLangRes.getInstance()
				// .getStrByID("scmpub", "UPPscmpub-000476")/*
				// * @res
				// * "请选择Excel列和导入名称的对应关系！"
				// */);
			}

			if (!checkColumns()) {
				throw new Exception("Excel列和导入名称对应关系不唯一！");
				//
				// throw new Exception(nc.ui.ml.NCLangRes.getInstance()
				// .getStrByID("scmpub", "UPPscmpub-000477")/*
				// * @res
				// * "Excel列和导入名称对应关系不唯一！"
				// */);
			}

			if (m_bSingle) {
				setExportVO(getDataFromExcelSingle());
			} else {
				setExportVO(getDataFromExcel());
			}
			m_bClose = true;
			m_workbook.close();
			m_is.close();
		} catch (NumberFormatException eFormatError) {
			m_bClose = false;
			Logger.info(eFormatError);
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon",
							"UPPSCMCommon-000132")/* @res "警告" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon",
							"UPPSCMCommon-000462"));// 格式错误
			throw eFormatError;
		} catch (Exception e) {
			m_bClose = false;
			Logger.info(e);
			throw e;
			// 2005-06-01,wnj,可能显示混乱的提示。这里有异常需要查看JAVA控制台。
			// MessageDialog.showErrorDlg(this,
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000132")/*@res
			// "警告"*/, e.getMessage());
		}
	}

	private void onOpenFile() {
		try {
			boolean bSingle = ckbSingle.isSelected();
			String sSeparator = txtSeparator.getText();
			int iLen = 0; // 分隔符长度
			if (bSingle
					&& ((sSeparator == null) || ("".equals(sSeparator.trim())))) {
				throw new Exception(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("scmpub", "UPPscmpub-000483")/*
																	 * @res
																	 * "请出入分隔符！"
																	 */);
			}
			if (clientUI != null) {
				if (getUIFileChooser().showOpenDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
					// LongTimeTask.calllongTimeService(SdmPubConst.MED_SDM_MODULNAME,
					// clientUI,
					// "正在读取文件，请稍候...", 1, this.getClass().getName(), null,
					// "setWorkBookTask", null, null);

					// zpm.................
					setWorkBookTask();
				}
			} else {
				setWorkBookTask();
			}
		} catch (Exception e) {
			Logger.info(e);
			if (MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("scmcommon",
							"UPPSCMCommon-000132")/* @res "警告" */, e
					.getMessage()) == MessageDialog.ID_OK)
				this.setEnabled(true);
			else
				this.closeCancel();
		}

	}

	public void setWorkBookTask() throws Exception {
		MedTimeLog t = new MedTimeLog();
		// if (checkCHandENnames() == 1) {
		// throw new Exception(nc.ui.ml.NCLangRes.getInstance()
		// .getStrByID("scmpub", "UPPscmpub-000479")/*
		// * @res
		// * "传入中英文对照数组不能为空！"
		// */);
		// } else if (checkCHandENnames() == 2) {
		// throw new Exception(nc.ui.ml.NCLangRes.getInstance()
		// .getStrByID("scmpub", "UPPscmpub-000480")/*
		// * @res
		// * "传入中文和英文对照数组中不能有为空数据！"
		// */);
		// }
		// else if (checkCHandENnames() == 3) {
		// throw new Exception(nc.ui.ml.NCLangRes.getInstance()
		// .getStrByID("scmpub", "UPPscmpub-000481")/*
		// * @res
		// * "传入中文和英文对照数组中不能有重复数据！"
		// */);
		// }

		long s = System.currentTimeMillis();

		setPathFile(getUIFileChooser().getSelectedFile());

		t.showExecuteTime("before setWorkBook:");
		setWorkBook(getPathFile());
		t.showExecuteTime("setWorkBook:");
	}

	private void setCurrentRow(int newCurrentRow) {
		m_iCurrentRow = newCurrentRow;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-23 2:50:49)
	 * 
	 * @param newCurrentSheetIndex
	 *            int
	 */
	private void setCurrentSheetIndex(int newCurrentSheetIndex) {
		m_iCurrentSheetIndex = newCurrentSheetIndex;
	}

	private void setExportVO(CircularlyAccessibleValueObject[] newvos)
			throws Exception {
		m_voExport = newvos;
	}

	public void setImportVOFlag(boolean importvoflag) {
		m_bImportVOFlag = importvoflag;
	}

	private void setPathFile(java.io.File newPathFile) {
		m_fPathFile = newPathFile;
		m_sFileName = newPathFile.getName();
	}

	public void setRowCount(int rownum) {
		if (rownum <= 0)
			m_iRowCount = 0;
		else {
			m_iRowCount = rownum;
			m_bMultiImport = true;
		}
	}

	private void setSheet(Sheet[] newsheet) {
		m_sheet = newsheet;
	}

	public void setVOName(String voName) {
		m_sVOName = voName;
	}

	private void setWorkBook(java.io.File newPathFile) {
		try {
			// 构建Workbook对象, 只读Workbook对象
			// 直接从本地文件创建Workbook
			// 从输入流创建Workbook
			long s = System.currentTimeMillis();
			MedTimeLog t1 = new MedTimeLog();
			m_is = new FileInputStream(newPathFile);
			Workbook rwb = Workbook.getWorkbook(m_is);
			t1.showExecuteTime("File Stream:");

			m_workbook = rwb;

			// 设置工作簿
			loadSheetsData();

		} catch (java.io.FileNotFoundException e1) {
			if (MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("scmcommon",
							"UPPSCMCommon-000132")/* @res "警告" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub",
							"UPPscmpub-000482")/* @res "文件不存在！" */) == MessageDialog.ID_OK)
				this.setEnabled(true);
			else
				// 强行关闭错误提示框
				this.closeCancel();
		} catch (Exception e) {
			Logger.info(e);
			if (MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("scmcommon",
							"UPPSCMCommon-000059")/* @res "错误" */, e
					.getMessage()) == MessageDialog.ID_OK)
				this.setEnabled(true);
		}
	}

	/**
	 * Called whenever the value of the selection changes.
	 * 
	 * @param e
	 *            the event that characterizes the change.
	 */
	public void valueChanged(javax.swing.event.ListSelectionEvent e) {
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-6-2 11:07:59)
	 */
	private void afterInputEnd() {
		String sSeparator = txtSeparator.getText().trim();
		if (sSeparator == null || sSeparator.length() == 0) {
			// 什么都不做
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon",
							"UPPSCMCommon-000132")/* @res "警告" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub",
							"UPPscmpub-000483")/* @res "请输入分隔符" */);
		} else {
			// 重新刷新 VO对照列表
			if (getWorkBook() == null) {
				return;
			} else {
				loadFieldsData();
			}
		}
	}

	/**
	 * 功能：检查传入的中文和英文对照数组是否有效 1.不能为空 2.String[][0]为中文，String[][1]为英文
	 * 3.String[][0]中不能有重复数据和空数据，String[][1]中不能有重复数据和空数据 创建日期：(2004-5-17
	 * 9:19:16)
	 * 
	 * @return java.lang.String[]
	 */
	private int checkCHandENnames() {
		String[][] names = getCHandENnames();

		if ((names == null) || (names.length == 0)) {
			return 1;// 传入数组为空
		}
		for (int i = 0; i < names.length; i++) {
			if ((names[i][0] == null) || ("".equals(names[i][0]))
					|| (names[i][1] == null) || ("".equals(names[i][1]))) {
				return 2;// 检查中文英文是否有空数据
			}
			for (int j = i + 1; j < names.length; j++) {
				if ((names[i][0].equals(names[j][0]))
						|| (names[i][1].equals(names[j][1]))) {
					return 3;// 检查中文英文中是否有重复数据
				}
			}
		}
		return 0;
	}

	/**
	 * 检查Excel列名和VO字段名的对照关系是否为一一对应 创建日期：(2004-5-17 10:53:43)
	 * 
	 * @return boolean
	 */
	private boolean checkColumns() {
		Object[][] objs = getColumns();
		String field = null;
		if (objs.length == 0)
			return false;

		for (int i = 0; i < objs.length; i++) {
			field = objs[i][1].toString();
			for (int j = i + 1; j < objs.length; j++) {
				if ((field != null) && (field.length() > 0)
						&& (field.equals(objs[j][1].toString()))) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 返回选择导入的字段，对应的数据库VO字段
	 * 
	 * @return java.lang.String
	 * @param str1
	 *            java.lang.String
	 */
	private String findCorrespondString(String str1) {
		String[][] strs = getCHandENnames();
		if (str1 == null)
			return null;
		if (strs == null && strs[0] == null && strs[0].length == 0)
			return null;
		int nrow = strs.length;
		int ncol = strs[0].length;
		for (int j = 0; j < nrow; j++) {
			for (int i = 0; i < ncol; i++) {
				if (str1.equals(strs[j][i]))
					return strs[j][1];
				continue;
			}

		}
		return null;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-5-17 9:19:16)
	 * 
	 * @return java.lang.String[]
	 */
	private java.lang.String[][] getCHandENnames() {
		return m_voCHandENnames;
	}

	private Object[][] getColumns() {
		return m_columns;
	}

	/**
	 * 从Excel中导出数据（单列导出） 创建日期：(2004-5-20 13:50:49)
	 * 
	 * @return int
	 */
	private CircularlyAccessibleValueObject[] getDataFromExcelSingle()
			throws Exception {
		// 获取工作簿对象
		Workbook rwb = getWorkBook();
		// 获取已选工作簿和列
		Sheet[] sht = getSheet();
		Object[][] cols = getColumns();
		int curshtindex = getCurrentSheetIndex();
		int currow = getCurrentRow();
		int k = currow;

		int remaincount = 0;
		int newcurrindex = curshtindex;
		int totalcount = 0;
		int iCount = 0;

		boolean bFirst = m_ckbFirstRow.isSelected(); // 是否选择第一行数据为列名称
		int iBegin = bFirst ? 1 : 0;// 数据起始行；如果选择，则从第一行开始导数据，否则从第二行开始导入数据

		boolean bIsNull = true;// 单元格记录是否为空
		String sTmp = "";

		Class c = Class.forName(getVOName());
		CircularlyAccessibleValueObject[] vos = null;
		CircularlyAccessibleValueObject vo = null;
		Vector vt = new Vector();

		if (curshtindex > sht.length - 1) {
			// 越界
			return null;
		}

		iCount = m_iRowCount;
		// 一次性导出
		if (m_iRowCount == 0) {
			m_bMultiImport = false;
			for (int t = 0; t < sht.length; t++) {
				if (sht[t].getRows() != 0) {
					iCount = iCount + sht[t].getRows() - iBegin;
				}
			}
		} else { // 分批导出
			// 分批导入时，判断最后一个工作簿中剩余记录
			// 如果最后一批记录数小于每次导出数，则将iCount置为最后剩余记录数
			if (curshtindex == sht.length - 1) {
				remaincount = sht[curshtindex].getRows() - currow;
				if (m_iRowCount > remaincount) {
					iCount = remaincount;
				}
			}

			totalcount = sht[curshtindex].getRows() - currow;
			if (m_iRowCount < totalcount) {
				newcurrindex = curshtindex;
			} else {
				for (int i = curshtindex + 1; i < sht.length; i++) {
					totalcount = totalcount + sht[i].getRows() - iBegin;
					if ((m_iRowCount - totalcount) < (sht[i].getRows() - iBegin)) {
						newcurrindex = i;
						break;
					}
				}
			}
		}

		if (iCount == 0)
			return null;

		for (int t = 0; t < iCount;) {
			for (int i = curshtindex; i < sht.length; i++, k = iBegin) {
				for (; (k < sht[i].getRows() && t < iCount); k++, t++) {
					vo = (CircularlyAccessibleValueObject) Class.forName(
							getVOName()).newInstance();
					// 获得单列数据并将数据拆分
					String[] datas = new String[m_iSingleCols];
					int column = 0;
					int iBeginIndex = 0;
					String sSingleColName = sht[i].getCell(0, k).getContents()
							.trim();
					// 如果该行没有数据，则将分割的数据均值为空
					if ((sSingleColName == null)
							|| (sSingleColName.length() == 0)) {
						datas[column] = "";
					} else {
						for (int s = 0; s <= sSingleColName.length() - m_iLen; s++) {
							if (m_sSeparator.equals(sSingleColName.substring(s,
									s + m_iLen))) {
								if (column == 0) { // 获取第一列
									datas[column] = sSingleColName.substring(0,
											s);
									iBeginIndex = s + m_iLen;
								} else { // 获取其他列
									// 判断数据是否合理，是否与分割出列名数的相等，
									// 分割的数据的列数如果大于分割的列名数，则将多余的数据舍弃
									// 分割的数据的列数如果小于分割的列名数，则补空
									if (column > m_iSingleCols - 1)
										break;
									else {
										datas[column] = sSingleColName
												.substring(iBeginIndex, s);
										iBeginIndex = s + m_iLen;
									}
								}
								s = s + m_iLen - 1;
								column++;
							}
						}
						if (column == 0)
							datas[column] = sSingleColName;
						else if (column > 0 && column < m_iSingleCols) {
							// 获得合法数据最后一个分割符之后的数据
							datas[column] = sSingleColName.substring(
									iBeginIndex, sSingleColName.length());
						}
					}
					// 对于数据列数不够的，顺序补空
					if (column < m_iSingleCols - 1) {
						for (int d = column + 1; d < m_iSingleCols; d++) {
							datas[d] = "";
						}
					}

					bIsNull = true;
					sTmp = "";
					for (int j = 0; j < cols.length; j++) {
						sTmp = datas[(new Integer(cols[j][0].toString()))
								.intValue()];
						if (bIsNull && sTmp != null && !"".equals(sTmp)) {
							bIsNull = false;
						}

						Object[] o = (Object[]) cols[j][1];
						if ("reserve2".equals(o[0].toString())) {// //////////////////费用类型
																	// zpm
							vo.setAttributeValue(o[0].toString(), o[1]);
						} else {
							vo.setAttributeValue(o[0].toString(), sTmp);
						}
						// if("reserve2".equals(cols[j][1].toString())){////////////////////费用类型
						// zpm
						// vo.setAttributeValue(cols[j][1].toString(),
						// cols[j][0].toString());
						// }else{
						// vo.setAttributeValue(cols[j][1].toString(), sTmp);
						// }
					}

					// 增加行号和工作簿名称
					// 需要vo具有"exlrowno"和"exlsheetname"和"exlfilename"字段
					vo.setAttributeValue("exlrowno", new Integer(k + 1));
					vo.setAttributeValue("exlsheetname", sht[i].getName());
					vo.setAttributeValue("exlfilename", m_sFileName);

					if (!bIsNull) {
						vt.add(vo);
					}

					if (t >= iCount)
						break;
				}
				if (t >= iCount)
					break;
			}
		}

		if (vt.size() > 0) {
			vos = (CircularlyAccessibleValueObject[]) java.lang.reflect.Array
					.newInstance(c, vt.size());
			vt.copyInto(vos);
		}

		// 设置当前工作簿
		// 设置当前行
		if (m_bMultiImport) {
			setCurrentSheetIndex(newcurrindex);
		} else {
			setCurrentSheetIndex(sht.length);
		}
		setCurrentRow(k);
		return (CircularlyAccessibleValueObject[]) vos;
	}

	/**
	 * 返回 UIPanel 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getUIPanelSingle() {

		if (pnlSingle == null) {
			pnlSingle = new UIPanel();
			pnlSingle.setName("UIPanelSingle");
			pnlSingle.setLayout(new java.awt.BorderLayout());
			pnlSingle.setPreferredSize(new java.awt.Dimension(500, 220));

			ckbSingle = new UICheckBox();
			ckbSingle.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"scmpub", "UPPscmpub-000484")/* @res "单列导入" */);
			ckbSingle.setPreferredSize(new java.awt.Dimension(180, 20));
			ckbSingle.addChangeListener(this);

			UILabel lblSeparator = new UILabel(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("scmpub", "UPPscmpub-000483")/* @res "请输入分隔符" */);
			lblSeparator.setPreferredSize(new java.awt.Dimension(80, 20));
			lblSeparator.setAlignmentX(0);
			txtSeparator = new UITextField();
			txtSeparator.setEnabled(false);
			txtSeparator.setPreferredSize(new java.awt.Dimension(100, 20));
			txtSeparator.addKeyListener(this);

			UILabel lbltmp = new UILabel();
			lbltmp.setPreferredSize(new java.awt.Dimension(130, 20));

			UIPanel pnlTmp = new UIPanel();
			pnlTmp.add(ckbSingle, java.awt.BorderLayout.WEST);
			pnlTmp.add(lblSeparator, java.awt.BorderLayout.CENTER);
			pnlTmp.add(txtSeparator);
			pnlTmp.add(lbltmp, java.awt.BorderLayout.EAST);
			// pnlTmp.add(pnlSgl, java.awt.BorderLayout.WEST);
			// pnlTmp.add(pnlSeparator, java.awt.BorderLayout.CENTER);
			pnlTmp.setPreferredSize(new java.awt.Dimension(500, 30));

			// 是否导入第一行数据的复选框
			// 如果选择，则第一行不作为数据导入，否则作为数据导入
			// 主要由于实际用户的来源数据为 SAP导出的数据，不会有列名称
			m_ckbFirstRow = new UICheckBox();
			m_ckbFirstRow.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"scmpub", "UPPscmpub-000485")/* @res "Excel中第一行是否列名称" */);
			m_ckbFirstRow.setPreferredSize(new java.awt.Dimension(180, 20));
			String sDesc = nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"scmpub", "UPPscmpub-000486")/*
													 * @res
													 * "注意：该选项决定第一行数据是否作为数据导入！请确认"
													 */;
			UILabel lblDescription = new UILabel(sDesc);
			lblDescription.setFont(new java.awt.Font("dialog", 0, 12));
			lblDescription.setPreferredSize(new java.awt.Dimension(320, 20));

			UIPanel pnlFirst = new UIPanel();
			pnlFirst.add(m_ckbFirstRow, java.awt.BorderLayout.WEST);
			pnlFirst.add(lblDescription, java.awt.BorderLayout.CENTER);
			pnlFirst.setPreferredSize(new java.awt.Dimension(500, 50));

			pnlSingle.add(pnlTmp, java.awt.BorderLayout.NORTH);
			pnlSingle.add(pnlFirst, java.awt.BorderLayout.CENTER);
			pnlSingle.add(getUIpnlField(), java.awt.BorderLayout.SOUTH);

		}
		return pnlSingle;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-5-13 19:36:24) 获得要导入的字段选择模板
	 * 
	 * @return nc.ui.pub.bill.BillCardPanel
	 */
	public BillCardPanel getUIpnlField() {
		if (m_pnlField == null) {
			m_pnlField = new BillCardPanel();
			m_pnlField.setPreferredSize(new java.awt.Dimension(500, 320));

			BillItem[] items = new BillItem[2];

			items[0] = new BillItem();
			items[0].setName(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"scmpub", "UPPscmpub-000487")/* @res "Excel列名/范例数据" */);
			items[0].setKey("excelcolname");
			items[0].setDataType(0);
			items[0].setShow(true);
			items[0].setWidth(240);
			items[0].setPos(1);
			items[0].setEdit(false);

			// vocolname
			items[1] = new BillItem();
			items[1].setName(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"scmpub", "UPPscmpub-000488")/* @res "含义" */);
			items[1].setKey("vocolname");
			items[1].setDataType(6);
			items[1].setShow(true);
			items[1].setWidth(240);
			items[1].setPos(1);
			items[1].setEdit(true);

			BillData billdata = new BillData();
			billdata.setBodyItems(items);
			m_pnlField.setBillData(billdata);

			m_pnlField.getBodyPanel().setBBodyMenuShow(false);
			m_pnlField.getBodyPanel().setRowNOShow(false);
		}
		return m_pnlField;
	}

	/**
	 * 功能：加载导入字段的数据 1.如果没有选中“单列导入”，则Excel列名显示的是Excel表中的各列名
	 * 2.如果选中“单列导入”，并且输入分隔符，则按照分隔符， 将Excel表中cell(0,0)按分隔符分割后作为Excel列名显示出来
	 * 创建日期：(2004-5-13 20:33:18)
	 */
	private void loadFieldsData() {
		MedTimeLog t = new MedTimeLog();

		try {
			boolean bSingle = ckbSingle.isSelected();
			String sSeparator = txtSeparator.getText();
			int iLen = 0; // 分隔符长度
			if (bSingle
					&& ((sSeparator == null) || ("".equals(sSeparator.trim())))) {
				throw new Exception(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("scmpub", "UPPscmpub-000483")/*
																	 * @res
																	 * "请出入分隔符"
																	 */);
			}
			iLen = sSeparator.trim().length();

			String[][] names = getCHandENnames();

			getUIpnlField().getBillModel().clearBodyData();
			String[] strCols = new String[2];
			strCols[0] = new String("excelcolname");
			strCols[1] = new String("vocolname");

			int column = 0;
			Sheet sheet = getWorkBook().getSheet(0);
			String sSingleColName = null;
			t.showExecuteTime("loadFieldsData 1:");
			if (bSingle) {
				sSingleColName = sheet.getCell(0, 0).getContents().trim();
				if ((sSingleColName == null)
						|| ("".equals(sSingleColName.trim()))) {
					throw new Exception(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("scmpub", "UPPscmpub-000489")/*
																		 * @res
																		 * "请确定Excel中第一行是否有单列列名 /
																		 * 示例数据！"
																		 */);
				}
				if (iLen > sSingleColName.length()) {
					throw new Exception(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("scmpub", "UPPscmpub-000490")/*
																		 * @res
																		 * "分隔符长度不能大于列名长度！"
																		 */);
				}
			}
			String[] tmpcolnames = null;
			String[] m_singlecolnames = null;
			t.showExecuteTime("loadFieldsData 2:");
			if (bSingle) {
				tmpcolnames = new String[sSingleColName.length()];
				int iBeginIndex = 0;
				for (int i = 0; i < sSingleColName.length() - iLen; i++) {
					if (sSeparator
							.equals(sSingleColName.substring(i, i + iLen))) {
						if (column == 0) { // 获取第一列
							tmpcolnames[column] = sSingleColName
									.substring(0, i);
							iBeginIndex = i + iLen;
						} else { // 获取其他列
							tmpcolnames[column] = sSingleColName.substring(
									iBeginIndex, i);
							iBeginIndex = i + iLen;
						}
						i = i + iLen - 1;
						column++;
					}
				}
				// 获取最后一个数据,如果在字符串末尾有分隔符，则将其去掉
				if (sSeparator.equals(sSingleColName.substring(sSingleColName
						.length()
						- iLen, sSingleColName.length()))) {
					tmpcolnames[column] = sSingleColName.substring(iBeginIndex,
							sSingleColName.length() - iLen);
				} else {
					tmpcolnames[column] = sSingleColName.substring(iBeginIndex,
							sSingleColName.length());
				}
				column++;

				m_singlecolnames = new String[column];
				for (int i = 0; i < m_singlecolnames.length; i++) {
					m_singlecolnames[i] = tmpcolnames[i];
				}

				// 检查Excel列名是否有重复
				String colname = null;
				for (int i = 0; i < m_singlecolnames.length; i++) {
					colname = m_singlecolnames[i];
					for (int j = i + 1; j < m_singlecolnames.length; j++) {
						if (colname.equals(m_singlecolnames[j])) {
							throw new Exception(nc.ui.ml.NCLangRes
									.getInstance().getStrByID("scmpub",
											"UPPscmpub-000491")/*
																 * @res
																 * "请确定分隔符是否正确或Excel中被分割的列名是否有重复！"
																 */);
						}
					}
				}
			} else {
				column = sheet.getColumns();
			}
			t.showExecuteTime("loadFieldsData 3:");
			if (column == 0) {
				throw new Exception(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("scmpub", "UPPscmpub-000492")/*
																	 * @res
																	 * "请确定Excel中是否有列名！"
																	 */);
			}

			UIComboBox fcombox = null;
			fcombox = (UIComboBox) getUIpnlField().getBodyItem("vocolname")
					.getComponent();
			getUIpnlField().getBodyItem("vocolname").setWithIndex(true);
			fcombox.removeAllItems();
			fcombox.addItem(" ");
			for (int j = 0; j < names.length; j++) {
				fcombox.addItem(names[j][0]);
			}
			fcombox.setTranslate(true);

			// 设置默认值，如果Excel中列名与含义列字段相同则将该字段显示为默认值，否则不显示默认值
			String sLeft = null;
			String sRight = null;
			t.showExecuteTime("loadFieldsData 4:");
			for (int i = 0; i < column; i++) {
				getUIpnlField().addLine();
				if (bSingle) {
					sLeft = m_singlecolnames[i];
					getUIpnlField().getBillModel().setValueAt(sLeft, i, 0);
				} else {
					sLeft = sheet.getCell(i, 0).getContents();
					getUIpnlField().getBillModel().setValueAt(sLeft, i, 0);
				}

				for (int j = 0; j < names.length; j++) {
					sRight = names[j][0];
					if (sLeft.equals(sRight)) {
						getUIpnlField().getBillModel().setValueAt(sRight, i, 1);
						break;
					}
				}

				// if (i < names.length) {
				// getUIpnlField().getBillModel().setValueAt(names[i][0], i, 1);
				// }
				// else {
				// getUIpnlField().getBillModel().setValueAt("", i, 1);
				// }
			}
			t.showExecuteTime("loadFieldsData 5:");
			// 设置是否单列导出
			m_bSingle = bSingle;

			// 设置单列列数
			if (bSingle) {
				m_iSingleCols = column;
				m_iLen = iLen;
				m_sSeparator = sSeparator;
			}
			//
			getUIpnlField().updateValue();
			getUIpnlField().updateUI();

		} catch (Exception e) {
			Logger.error(e);
			if (MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("scmcommon",
							"UPPSCMCommon-000132")/* @res "警告" */, e
					.getMessage()) == MessageDialog.ID_OK)
				this.setEnabled(true);
			else
				this.closeCancel();
		}

		t.showTime("loadFieldsData 6:");
	}

	/**
	 * 功能：加载导入字段的数据 1.如果没有选中“单行导入”，则Excel列名显示的是Excel表中的各列名
	 * 2.如果选中“单行导入”，并且输入分隔符，则按照分隔符， 将Excel表中cell(0,0)按分隔符分割后作为Excel列名显示出来
	 * 创建日期：(2004-5-13 20:33:18)
	 */
	private void loadSheetsData() {

		MedTimeLog t = new MedTimeLog();
		int sheetno;
		Vector vdata = new Vector();

		Workbook wb = getWorkBook();
		t.showExecuteTime("loadSheetsData getWorkBook:");

		sheetno = wb.getNumberOfSheets();
		Object[] oaData = new Object[sheetno];

		t.showExecuteTime("loadSheetsData getNumberOfSheets:");
		if (sheetno == 0)
			return;

		for (int i = 0; i < sheetno; i++) {
			// vdata.add(wb.getSheet(i).getName());
			oaData[i] = wb.getSheet(i).getName();
			t.showExecuteTime(i + "::::");
		}
		t.showExecuteTime("loadSheetsData loop:");
		getUIltlWorkBook().setRightData(null);
		getUIltlWorkBook().setLeftData(null);
		// getUIltlWorkBook().setLeftData(vdata.toArray());
		getUIltlWorkBook().setLeftData(oaData);

		t.showExecuteTime("loadSheetsData setLeftData,rightData:");
		// 设置第一个工作簿的列为待选列名
		loadFieldsData();
	}

	protected void processKeyEvent(java.awt.event.KeyEvent e) {
		if (e.getKeyCode() == e.VK_ENTER) {
			afterInputEnd();
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-5-17 9:19:16)
	 * 
	 * @return java.lang.String[]
	 */
	public void setCHandENnames(String[][] newnames) {
		m_voCHandENnames = newnames;
	}

	private void setColumns(Object[][] newcolumns) {
		m_columns = newcolumns;
	}

	/**
	 * Invoked when the target of the listener has changed its state.
	 * 
	 * @param e
	 *            a ChangeEvent object
	 */
	public void stateChanged(javax.swing.event.ChangeEvent e) {
		if (e.getSource() == ckbSingle) {
			if (ckbSingle.isSelected()) {
				txtSeparator.setEnabled(true);
				txtSeparator.setText("");
			} else {
				txtSeparator.setEnabled(false);
				txtSeparator.setText("");
			}
		}
	}

	public static void main(String[] a) {
		ExcelDialog e = new ExcelDialog();
		e.showModal();
	}

	/**
	 * 创建人：刘家清 创建日期：2007-8-3下午01:23:56
	 * 创建原因：放开"Excel中第一行是否列名称"checkbox权限，可以导入界面打开时就默认选上
	 * 
	 * @param frselected
	 */
	public void setckbFirstRowSelected(boolean frselected) {
		if (m_ckbFirstRow != null)
			m_ckbFirstRow.setSelected(frselected);
	}
}
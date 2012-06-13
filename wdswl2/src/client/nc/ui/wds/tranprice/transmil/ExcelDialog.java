package nc.ui.wds.tranprice.transmil;

/**
 * Excel���� ע����� ��Excel�ļ�Ҫ�� ÿ�������������ݽṹ����һ�� ��ѡ�����Ե�һ��������������Ϊ׼����ѡ����������VO���ֶ��д���
 * ÿ������������������λ�ڵ�һ�У������б���ӵڶ��п�ʼ����Ϊ���ݴӵڶ��п�ʼ���� �����в��ܳ��ֺϲ���Ԫ�� ���������ظ� ʹ��Excel����Ի���:
 * ��Ҫ����VO��name��������VO���� ������setVOName(String)����VO����
 * ����ָ������������setRowCount(int)�����������Ĭ��Ϊ�����������ݣ� ����ʱ����ָ�����������ݣ���Ҫ�������������ݣ�
 * ������setRowCount(int)����ÿ�ε������� ������getExportVO()��ȡ����VO ������Next()
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

	// ���뵽VO�����ݿ��ʶ
	private boolean m_bImportVOFlag = true;

	// Excel�е������ݵĵ�ǰ��
	private int m_iCurrentRow = 1;

	// ÿ�ε������ݵ�����
	private int m_iRowCount = 0;

	// ������VO
	private CircularlyAccessibleValueObject[] m_voExport = null;

	//
	private String m_sVOName = null;

	// *************ʹ��UIListToList����UIList��UICheckBox******************
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

	private boolean m_bSingle;// �Ƿ��е���

	private UICheckBox m_ckbFirstRow = null;

	private int m_iLen = 0;

	// ����������
	private InputStream m_is = null;

	private int m_iSingleCols = 0;

	// *************����Excel����������VO���ֶ����Ķ�Ӧ��ϵ******************
	private nc.ui.pub.bill.BillCardPanel m_pnlField = null;

	private String m_sFileName = null;

	private String m_sSeparator = null;

	private String[][] m_voCHandENnames = null;

	private String[] m_vofields = null;

	// *************���ӵ��е��룬��������ʹ�÷ָ����ֿ������***************
	private UIPanel pnlSingle = null;

	private UITextField txtSeparator = null;

	private java.awt.Container clientUI = null;

	/**
	 * ExcelImportDialog ������ע�⡣
	 */
	public ExcelDialog() {
		super();
		init();
	}

	/**
	 * ExcelImportDialog ������ע�⡣
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
	 * ExcelImportDialog ������ע�⡣
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
	 * ExcelImportDialog ������ע�⡣
	 * 
	 * @param owner
	 *            java.awt.Frame
	 */
	public ExcelDialog(java.awt.Frame owner) {
		super(owner);
		init();
	}

	/**
	 * ExcelImportDialog ������ע�⡣
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
	 * �˴����뷽��˵���� �������ڣ�(2004-4-23 9:01:52)
	 * 
	 * @return int
	 */
	private int getCurrentRow() {
		return m_iCurrentRow;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-4-23 2:50:49)
	 * 
	 * @return int
	 */
	private int getCurrentSheetIndex() {
		return m_iCurrentSheetIndex;
	}

	/**
	 * ��Excel�е������ݣ��ǵ��е����� ��Ҫ���˿���,Ҫ��ÿ���赼���ĸ�����Ԫ����пռ�¼�ж� �������ڣ�(2004-4-23 2:50:49)
	 * 
	 * @return int
	 */
	private CircularlyAccessibleValueObject[] getDataFromExcel()
			throws Exception {
		Workbook rwb = getWorkBook(); // ��ȡ����������
		Sheet[] sht = getSheet(); // ��ȡ��ѡ����������

		Object[][] cols = getColumns();
		int curshtindex = getCurrentSheetIndex();
		int currow = getCurrentRow();
		int k = currow;

		int remaincount = 0;
		int newcurrindex = curshtindex;
		int totalcount = 0;
		int iCount = 0;

		boolean bFirst = m_ckbFirstRow.isSelected(); // �Ƿ�ѡ���һ������Ϊ������
		int iBegin = bFirst ? 1 : 0; // ������ʼ�У����ѡ����ӵ�һ�п�ʼ�����ݣ�����ӵڶ��п�ʼ��������

		boolean bIsNull = true; // ��Ԫ���¼�Ƿ�Ϊ��
		String sTmp = "";

		Class c = Class.forName(getVOName());
		CircularlyAccessibleValueObject[] vos = null;
		CircularlyAccessibleValueObject vo = null;
		Vector vt = new Vector();

		if (curshtindex > sht.length - 1) {
			// Խ��
			return null;
		}

		iCount = m_iRowCount;
		// һ���Ե���
		if (m_iRowCount == 0) {
			m_bMultiImport = false;
			for (int t = 0; t < sht.length; t++) {
				if (sht[t].getRows() != 0) {
					iCount = iCount + sht[t].getRows() - iBegin;
				}
			}
		} else { // ��������
			// ��������ʱ���ж����һ����������ʣ���¼
			// ������һ����¼��С��ÿ�ε���������iCount��Ϊ���ʣ���¼��
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

		// ���õ�ǰ������
		// ���õ�ǰ��
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
	 * ���� InternalDialogContentPane ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * �˴����뷽��˵���� �������ڣ�(2004-4-19 16:02:20)
	 */
	private UIButton getUIButtonCancel() {
		if (btnCancel == null) {
			btnCancel = new UIButton();
			btnCancel.setName("UIButtonCancel");
			btnCancel.setToolTipText(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("scmpub", "UPPscmpub-000467")/*
																 * @res "ȡ������"
																 */);
			btnCancel.setFont(new java.awt.Font("dialog", 0, 12));
			btnCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000008")/* @res "ȡ��" */);
			btnCancel.setBounds(130, 0, 60, 22);
			btnCancel.addActionListener(this);
		}
		return btnCancel;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-4-19 16:02:20)
	 */
	private UIButton getUIButtonImport() {
		if (btnImport == null) {
			btnImport = new UIButton();
			btnImport.setName("UIButtonImport");
			btnImport.setToolTipText(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("scmpub", "UPPscmpub-000468")/* @res "����" */);
			btnImport.setFont(new java.awt.Font("dialog", 0, 12));
			btnImport.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"scmpub", "UPPscmpub-000468")/* @res "����" */);
			btnImport.setBounds(70, 0, 60, 22);

			btnImport.addActionListener(this);
		}
		return btnImport;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-4-19 16:02:20)
	 */
	private UIButton getUIButtonOpenFile() {
		if (btnOpenFile == null) {
			btnOpenFile = new UIButton();
			btnOpenFile.setName("UIButtonOpenFile");
			btnOpenFile.setToolTipText(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("scmpub", "UPPscmpub-000469")/*
																 * @res "���ļ�"
																 */);
			btnOpenFile.setFont(new java.awt.Font("dialog", 0, 12));
			btnOpenFile.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"scmpub", "UPPscmpub-000470")/* @res "��..." */);
			btnOpenFile.setBounds(0, 0, 60, 22);

			btnOpenFile.addActionListener(this);
		}
		return btnOpenFile;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-4-19 20:44:39)
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
													 * @res "Microsoft Excel �ļ� "
													 */);
			fcFileChooser.setFileFilter(filter);
			fcFileChooser.addChoosableFileFilter(filter);

		}
		return fcFileChooser;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-4-19 16:02:20)
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
							"scmpub", "UPPscmpub-000472")/* @res "��ѡ������" */);
			ltlWorkBook
					.setRightText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"scmpub", "UPPscmpub-000473")/* @res "��ѡ������" */);
			ltlWorkBook.setPreferredSize(new java.awt.Dimension(500, 130));
		}
		return ltlWorkBook;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-4-19 16:02:20)
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
	 * ���� UIPanel ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� UIPanel ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� UIPanel ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� UIPanel ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * �˴����뷽��˵���� �������ڣ�(2004-4-19 16:02:20)
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
	 * �˴����뷽��˵���� �������ڣ�(2004-4-19 14:18:30)
	 */
	private void init() {
		setName("ExcelImportDialog");
		setResizable(false);
		setSize(536, 602);
		setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub",
				"UPPscmpub-000474")/* @res "Excel����" */);
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
			// У�飺excelL����������������Ϊ��
			List<Object> list = new ArrayList<Object>();
			for (int i = 0; i < getUIpnlField().getBillTable().getRowCount(); i++) {
				Object o = getUIpnlField().getBillModel().getValueAt(i, 0);
				if (list.contains(o)) {
					throw new Exception("EXCEL����/���������ظ�!");
				}
				list.add(o);
			}
			// У��:Ҫ����ĵĹ�����
			if ((getUIltlWorkBook().getRightData() == null)
					|| (getUIltlWorkBook().getRightData().length == 0)) {
				throw new Exception("��ѡ������");
				// throw new Exception(nc.ui.ml.NCLangRes.getInstance()
				// .getStrByID("scmpub", "UPPscmpub-000475")/*
				// * @res
				// * "��ѡ��������"
				// */);
			}

			// ���ѡ�еĹ�����
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

			// ����ǰ�������͵�ǰ����Ϊ��ʼ״̬
			setCurrentSheetIndex(0);
			setCurrentRow(1);

			// ���ѡ�е��ֶ�
			int selectedrows = 0;
			getUIpnlField().stopEditing();
			for (int k = 0; k < getUIpnlField().getBillModel().getRowCount(); k++) {
				/* �˴���һ��BUG��combo����ֵ�����ı�ʱ�����focusֱ���뿪billcardpanel������combo����ֵ�ͱ�����billmodel�еĶ�Ӧֵ��һ�� */
				if ((getUIpnlField().getBillModel().getValueAt(k, 1) != null)
						&& getUIpnlField().getBillModel().getValueAt(k, 1)
								.toString().trim().length() > 0) {
					selectedrows++;
				}
			}
			// ���ѡ�е��ֶκ�VO�Ķ�Ӧ��ϵ
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
				throw new Exception("��ѡ��Excel�к͵������ƵĶ�Ӧ��ϵ");
				//
				// throw new Exception(nc.ui.ml.NCLangRes.getInstance()
				// .getStrByID("scmpub", "UPPscmpub-000476")/*
				// * @res
				// * "��ѡ��Excel�к͵������ƵĶ�Ӧ��ϵ��"
				// */);
			}

			if (!checkColumns()) {
				throw new Exception("Excel�к͵������ƶ�Ӧ��ϵ��Ψһ��");
				//
				// throw new Exception(nc.ui.ml.NCLangRes.getInstance()
				// .getStrByID("scmpub", "UPPscmpub-000477")/*
				// * @res
				// * "Excel�к͵������ƶ�Ӧ��ϵ��Ψһ��"
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
							"UPPSCMCommon-000132")/* @res "����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon",
							"UPPSCMCommon-000462"));// ��ʽ����
			throw eFormatError;
		} catch (Exception e) {
			m_bClose = false;
			Logger.info(e);
			throw e;
			// 2005-06-01,wnj,������ʾ���ҵ���ʾ���������쳣��Ҫ�鿴JAVA����̨��
			// MessageDialog.showErrorDlg(this,
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000132")/*@res
			// "����"*/, e.getMessage());
		}
	}

	private void onOpenFile() {
		try {
			boolean bSingle = ckbSingle.isSelected();
			String sSeparator = txtSeparator.getText();
			int iLen = 0; // �ָ�������
			if (bSingle
					&& ((sSeparator == null) || ("".equals(sSeparator.trim())))) {
				throw new Exception(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("scmpub", "UPPscmpub-000483")/*
																	 * @res
																	 * "�����ָ�����"
																	 */);
			}
			if (clientUI != null) {
				if (getUIFileChooser().showOpenDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
					// LongTimeTask.calllongTimeService(SdmPubConst.MED_SDM_MODULNAME,
					// clientUI,
					// "���ڶ�ȡ�ļ������Ժ�...", 1, this.getClass().getName(), null,
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
							"UPPSCMCommon-000132")/* @res "����" */, e
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
		// * "������Ӣ�Ķ������鲻��Ϊ�գ�"
		// */);
		// } else if (checkCHandENnames() == 2) {
		// throw new Exception(nc.ui.ml.NCLangRes.getInstance()
		// .getStrByID("scmpub", "UPPscmpub-000480")/*
		// * @res
		// * "�������ĺ�Ӣ�Ķ��������в�����Ϊ�����ݣ�"
		// */);
		// }
		// else if (checkCHandENnames() == 3) {
		// throw new Exception(nc.ui.ml.NCLangRes.getInstance()
		// .getStrByID("scmpub", "UPPscmpub-000481")/*
		// * @res
		// * "�������ĺ�Ӣ�Ķ��������в������ظ����ݣ�"
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
	 * �˴����뷽��˵���� �������ڣ�(2004-4-23 2:50:49)
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
			// ����Workbook����, ֻ��Workbook����
			// ֱ�Ӵӱ����ļ�����Workbook
			// ������������Workbook
			long s = System.currentTimeMillis();
			MedTimeLog t1 = new MedTimeLog();
			m_is = new FileInputStream(newPathFile);
			Workbook rwb = Workbook.getWorkbook(m_is);
			t1.showExecuteTime("File Stream:");

			m_workbook = rwb;

			// ���ù�����
			loadSheetsData();

		} catch (java.io.FileNotFoundException e1) {
			if (MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("scmcommon",
							"UPPSCMCommon-000132")/* @res "����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub",
							"UPPscmpub-000482")/* @res "�ļ������ڣ�" */) == MessageDialog.ID_OK)
				this.setEnabled(true);
			else
				// ǿ�йرմ�����ʾ��
				this.closeCancel();
		} catch (Exception e) {
			Logger.info(e);
			if (MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("scmcommon",
							"UPPSCMCommon-000059")/* @res "����" */, e
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
	 * �˴����뷽��˵���� �������ڣ�(2004-6-2 11:07:59)
	 */
	private void afterInputEnd() {
		String sSeparator = txtSeparator.getText().trim();
		if (sSeparator == null || sSeparator.length() == 0) {
			// ʲô������
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon",
							"UPPSCMCommon-000132")/* @res "����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub",
							"UPPscmpub-000483")/* @res "������ָ���" */);
		} else {
			// ����ˢ�� VO�����б�
			if (getWorkBook() == null) {
				return;
			} else {
				loadFieldsData();
			}
		}
	}

	/**
	 * ���ܣ���鴫������ĺ�Ӣ�Ķ��������Ƿ���Ч 1.����Ϊ�� 2.String[][0]Ϊ���ģ�String[][1]ΪӢ��
	 * 3.String[][0]�в������ظ����ݺͿ����ݣ�String[][1]�в������ظ����ݺͿ����� �������ڣ�(2004-5-17
	 * 9:19:16)
	 * 
	 * @return java.lang.String[]
	 */
	private int checkCHandENnames() {
		String[][] names = getCHandENnames();

		if ((names == null) || (names.length == 0)) {
			return 1;// ��������Ϊ��
		}
		for (int i = 0; i < names.length; i++) {
			if ((names[i][0] == null) || ("".equals(names[i][0]))
					|| (names[i][1] == null) || ("".equals(names[i][1]))) {
				return 2;// �������Ӣ���Ƿ��п�����
			}
			for (int j = i + 1; j < names.length; j++) {
				if ((names[i][0].equals(names[j][0]))
						|| (names[i][1].equals(names[j][1]))) {
					return 3;// �������Ӣ�����Ƿ����ظ�����
				}
			}
		}
		return 0;
	}

	/**
	 * ���Excel������VO�ֶ����Ķ��չ�ϵ�Ƿ�Ϊһһ��Ӧ �������ڣ�(2004-5-17 10:53:43)
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
	 * ����ѡ������ֶΣ���Ӧ�����ݿ�VO�ֶ�
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
	 * �˴����뷽��˵���� �������ڣ�(2004-5-17 9:19:16)
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
	 * ��Excel�е������ݣ����е����� �������ڣ�(2004-5-20 13:50:49)
	 * 
	 * @return int
	 */
	private CircularlyAccessibleValueObject[] getDataFromExcelSingle()
			throws Exception {
		// ��ȡ����������
		Workbook rwb = getWorkBook();
		// ��ȡ��ѡ����������
		Sheet[] sht = getSheet();
		Object[][] cols = getColumns();
		int curshtindex = getCurrentSheetIndex();
		int currow = getCurrentRow();
		int k = currow;

		int remaincount = 0;
		int newcurrindex = curshtindex;
		int totalcount = 0;
		int iCount = 0;

		boolean bFirst = m_ckbFirstRow.isSelected(); // �Ƿ�ѡ���һ������Ϊ������
		int iBegin = bFirst ? 1 : 0;// ������ʼ�У����ѡ����ӵ�һ�п�ʼ�����ݣ�����ӵڶ��п�ʼ��������

		boolean bIsNull = true;// ��Ԫ���¼�Ƿ�Ϊ��
		String sTmp = "";

		Class c = Class.forName(getVOName());
		CircularlyAccessibleValueObject[] vos = null;
		CircularlyAccessibleValueObject vo = null;
		Vector vt = new Vector();

		if (curshtindex > sht.length - 1) {
			// Խ��
			return null;
		}

		iCount = m_iRowCount;
		// һ���Ե���
		if (m_iRowCount == 0) {
			m_bMultiImport = false;
			for (int t = 0; t < sht.length; t++) {
				if (sht[t].getRows() != 0) {
					iCount = iCount + sht[t].getRows() - iBegin;
				}
			}
		} else { // ��������
			// ��������ʱ���ж����һ����������ʣ���¼
			// ������һ����¼��С��ÿ�ε���������iCount��Ϊ���ʣ���¼��
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
					// ��õ������ݲ������ݲ��
					String[] datas = new String[m_iSingleCols];
					int column = 0;
					int iBeginIndex = 0;
					String sSingleColName = sht[i].getCell(0, k).getContents()
							.trim();
					// �������û�����ݣ��򽫷ָ�����ݾ�ֵΪ��
					if ((sSingleColName == null)
							|| (sSingleColName.length() == 0)) {
						datas[column] = "";
					} else {
						for (int s = 0; s <= sSingleColName.length() - m_iLen; s++) {
							if (m_sSeparator.equals(sSingleColName.substring(s,
									s + m_iLen))) {
								if (column == 0) { // ��ȡ��һ��
									datas[column] = sSingleColName.substring(0,
											s);
									iBeginIndex = s + m_iLen;
								} else { // ��ȡ������
									// �ж������Ƿ�����Ƿ���ָ������������ȣ�
									// �ָ�����ݵ�����������ڷָ�����������򽫶������������
									// �ָ�����ݵ��������С�ڷָ�����������򲹿�
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
							// ��úϷ��������һ���ָ��֮�������
							datas[column] = sSingleColName.substring(
									iBeginIndex, sSingleColName.length());
						}
					}
					// �����������������ģ�˳�򲹿�
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
						if ("reserve2".equals(o[0].toString())) {// //////////////////��������
																	// zpm
							vo.setAttributeValue(o[0].toString(), o[1]);
						} else {
							vo.setAttributeValue(o[0].toString(), sTmp);
						}
						// if("reserve2".equals(cols[j][1].toString())){////////////////////��������
						// zpm
						// vo.setAttributeValue(cols[j][1].toString(),
						// cols[j][0].toString());
						// }else{
						// vo.setAttributeValue(cols[j][1].toString(), sTmp);
						// }
					}

					// �����кź͹���������
					// ��Ҫvo����"exlrowno"��"exlsheetname"��"exlfilename"�ֶ�
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

		// ���õ�ǰ������
		// ���õ�ǰ��
		if (m_bMultiImport) {
			setCurrentSheetIndex(newcurrindex);
		} else {
			setCurrentSheetIndex(sht.length);
		}
		setCurrentRow(k);
		return (CircularlyAccessibleValueObject[]) vos;
	}

	/**
	 * ���� UIPanel ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private UIPanel getUIPanelSingle() {

		if (pnlSingle == null) {
			pnlSingle = new UIPanel();
			pnlSingle.setName("UIPanelSingle");
			pnlSingle.setLayout(new java.awt.BorderLayout());
			pnlSingle.setPreferredSize(new java.awt.Dimension(500, 220));

			ckbSingle = new UICheckBox();
			ckbSingle.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"scmpub", "UPPscmpub-000484")/* @res "���е���" */);
			ckbSingle.setPreferredSize(new java.awt.Dimension(180, 20));
			ckbSingle.addChangeListener(this);

			UILabel lblSeparator = new UILabel(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("scmpub", "UPPscmpub-000483")/* @res "������ָ���" */);
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

			// �Ƿ����һ�����ݵĸ�ѡ��
			// ���ѡ�����һ�в���Ϊ���ݵ��룬������Ϊ���ݵ���
			// ��Ҫ����ʵ���û�����Դ����Ϊ SAP���������ݣ�������������
			m_ckbFirstRow = new UICheckBox();
			m_ckbFirstRow.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"scmpub", "UPPscmpub-000485")/* @res "Excel�е�һ���Ƿ�������" */);
			m_ckbFirstRow.setPreferredSize(new java.awt.Dimension(180, 20));
			String sDesc = nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"scmpub", "UPPscmpub-000486")/*
													 * @res
													 * "ע�⣺��ѡ�������һ�������Ƿ���Ϊ���ݵ��룡��ȷ��"
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
	 * �˴����뷽��˵���� �������ڣ�(2004-5-13 19:36:24) ���Ҫ������ֶ�ѡ��ģ��
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
					"scmpub", "UPPscmpub-000487")/* @res "Excel����/��������" */);
			items[0].setKey("excelcolname");
			items[0].setDataType(0);
			items[0].setShow(true);
			items[0].setWidth(240);
			items[0].setPos(1);
			items[0].setEdit(false);

			// vocolname
			items[1] = new BillItem();
			items[1].setName(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"scmpub", "UPPscmpub-000488")/* @res "����" */);
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
	 * ���ܣ����ص����ֶε����� 1.���û��ѡ�С����е��롱����Excel������ʾ����Excel���еĸ�����
	 * 2.���ѡ�С����е��롱����������ָ��������շָ����� ��Excel����cell(0,0)���ָ����ָ����ΪExcel������ʾ����
	 * �������ڣ�(2004-5-13 20:33:18)
	 */
	private void loadFieldsData() {
		MedTimeLog t = new MedTimeLog();

		try {
			boolean bSingle = ckbSingle.isSelected();
			String sSeparator = txtSeparator.getText();
			int iLen = 0; // �ָ�������
			if (bSingle
					&& ((sSeparator == null) || ("".equals(sSeparator.trim())))) {
				throw new Exception(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("scmpub", "UPPscmpub-000483")/*
																	 * @res
																	 * "�����ָ���"
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
																		 * "��ȷ��Excel�е�һ���Ƿ��е������� /
																		 * ʾ�����ݣ�"
																		 */);
				}
				if (iLen > sSingleColName.length()) {
					throw new Exception(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("scmpub", "UPPscmpub-000490")/*
																		 * @res
																		 * "�ָ������Ȳ��ܴ����������ȣ�"
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
						if (column == 0) { // ��ȡ��һ��
							tmpcolnames[column] = sSingleColName
									.substring(0, i);
							iBeginIndex = i + iLen;
						} else { // ��ȡ������
							tmpcolnames[column] = sSingleColName.substring(
									iBeginIndex, i);
							iBeginIndex = i + iLen;
						}
						i = i + iLen - 1;
						column++;
					}
				}
				// ��ȡ���һ������,������ַ���ĩβ�зָ���������ȥ��
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

				// ���Excel�����Ƿ����ظ�
				String colname = null;
				for (int i = 0; i < m_singlecolnames.length; i++) {
					colname = m_singlecolnames[i];
					for (int j = i + 1; j < m_singlecolnames.length; j++) {
						if (colname.equals(m_singlecolnames[j])) {
							throw new Exception(nc.ui.ml.NCLangRes
									.getInstance().getStrByID("scmpub",
											"UPPscmpub-000491")/*
																 * @res
																 * "��ȷ���ָ����Ƿ���ȷ��Excel�б��ָ�������Ƿ����ظ���"
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
																	 * "��ȷ��Excel���Ƿ���������"
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

			// ����Ĭ��ֵ�����Excel�������뺬�����ֶ���ͬ�򽫸��ֶ���ʾΪĬ��ֵ��������ʾĬ��ֵ
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
			// �����Ƿ��е���
			m_bSingle = bSingle;

			// ���õ�������
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
							"UPPSCMCommon-000132")/* @res "����" */, e
					.getMessage()) == MessageDialog.ID_OK)
				this.setEnabled(true);
			else
				this.closeCancel();
		}

		t.showTime("loadFieldsData 6:");
	}

	/**
	 * ���ܣ����ص����ֶε����� 1.���û��ѡ�С����е��롱����Excel������ʾ����Excel���еĸ�����
	 * 2.���ѡ�С����е��롱����������ָ��������շָ����� ��Excel����cell(0,0)���ָ����ָ����ΪExcel������ʾ����
	 * �������ڣ�(2004-5-13 20:33:18)
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
		// ���õ�һ������������Ϊ��ѡ����
		loadFieldsData();
	}

	protected void processKeyEvent(java.awt.event.KeyEvent e) {
		if (e.getKeyCode() == e.VK_ENTER) {
			afterInputEnd();
		}
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-5-17 9:19:16)
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
	 * �����ˣ������� �������ڣ�2007-8-3����01:23:56
	 * ����ԭ�򣺷ſ�"Excel�е�һ���Ƿ�������"checkboxȨ�ޣ����Ե�������ʱ��Ĭ��ѡ��
	 * 
	 * @param frselected
	 */
	public void setckbFirstRowSelected(boolean frselected) {
		if (m_ckbFirstRow != null)
			m_ckbFirstRow.setSelected(frselected);
	}
}
package nc.ui.zmpub.pub.excel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.JOptionPane;

import nc.ui.pp.pub.ExcelColumnInfo;
import nc.ui.pp.pub.IExcelFileFlag;
import nc.ui.pu.pub.PuTool;
import nc.vo.pp.ask.ExcelFileVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.zmpub.pub.report.ReportBaseVO;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

/**
 * Excel�ļ���ȡ��
 * 
 * @author mlr
 */
@SuppressWarnings("restriction")
public abstract class ExcelReadCtrl {
	private HSSFWorkbook wb = null;// Excel�ļ�����
	private HSSFSheet[] sheet = null;// sheet����,����ҳ��

	// ��ǰ�ļ�����
	private String m_fileName;

	/**
	 * ExcelReadCtrl ������ע�⡣
	 */
	public ExcelReadCtrl() {
		super();
	}

	/**
	 * ExcelReadCtrl ������ע�⡣
	 * 
	 * @param sFileName
	 *            java.lang.String
	 * @param flag
	 *            boolean
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	public ExcelReadCtrl(String sFileName, boolean flag) throws Exception {
		super();
		if (sFileName == null)
			return;
		m_fileName = sFileName;

		FileInputStream fs = new FileInputStream(m_fileName);
		wb = new HSSFWorkbook(fs);

		int sheetSize = wb.getNumberOfSheets();
		if (sheetSize == 0) {
			JOptionPane
					.showMessageDialog(null, nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("40040701", "UPP40040701-000193")/*
																		 * @res
																		 * "Excel�ļ�Ϊ�գ�"
																		 */);
			return;
		}
		sheet = new HSSFSheet[sheetSize];

		for (int i = 0; i < sheetSize; i++) {
			sheet[i] = wb.getSheetAt(i);
		}

		fs.close();
	}

	/**
	 * ?user> ���ܣ��ӵ�һ��sheetָ����Excel��ȡ��VO ������ ���أ� ���⣺ ���ڣ�(2004-8-25 12:18:30)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.vo.ic.pub.barcodeoffline.ExcelFileVO
	 * @param line
	 *            int
	 */
	public ExcelFileVO getVOAtLine(int indexRow) {
		ExcelFileVO vo = new nc.vo.pp.ask.ExcelFileVO();
		HSSFRow row = sheet[0].getRow(indexRow);
		setRowToVO(row, vo);
		return vo;

	}

	/**
	 * ���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-10-15 9:22:40) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param len
	 *            int
	 * @param lenAll
	 *            int
	 */
	private void clearRestLines(int len, int lenAll) {
		if (len >= lenAll)
			return;

		for (int i = len; i <= lenAll; i++) {
			HSSFRow row = sheet[0].getRow(i);
			if (row == null)
				continue;

			for (short j = 0; j < ExcelColumnInfo.saAskVOName.length; j++) {
				HSSFCell cell = row.getCell(j);
				if (cell == null)
					continue;
				cell.setCellValue("");

			}

		}

	}

	/**
	 * Author������ ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-10-16 13:01:30) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param len
	 *            int
	 * @param lenAll
	 *            int
	 */
	private void clearVendorRestLines(int len, int lenAll) {
		if (len >= lenAll)
			return;

		for (int i = len; i <= lenAll; i++) {
			HSSFRow row = sheet[0].getRow(i);
			if (row == null)
				continue;

			for (short j = 0; j < ExcelColumnInfo.saVendorVOName.length; j++) {
				HSSFCell cell = row.getCell(j);
				if (cell == null)
					continue;
				cell.setCellValue("");

			}

		}

	}

	/**
	 * ���� ���ܣ�����EXCEL�ļ��б��⣨ѯ�۵��ã� ������ ���أ� ���⣺ ���ڣ�(2004-10-15 9:21:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param sheet
	 *            org.apache.poi.hssf.usermodel.HSSFSheet
	 */
	private void createExcelCaption(HSSFSheet sheet) {
		if (sheet == null)
			return;
		HSSFRow row = null;
		HSSFCell cell = null;
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// Create a new font and alter it.
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short) 24);
		font.setFontName("Courier New");
		// font.setItalic(true);
		// font.setStrikeout(true);
		cellStyle.setFont(font);

		short creLineForTitle = 6;
		row = sheet.getRow(0);
		if (row == null)
			row = sheet.createRow(0);
		cell = row.getCell(creLineForTitle);
		if (cell == null)
			cell = row.createCell(creLineForTitle);
		cell.setCellStyle(cellStyle);
		sheet.addMergedRegion(new Region(0, (short) 6, 0, (short) 9));
		cell.setCellValue("ѯ�۵�");

		int j = 0;
		short creLine = 0;
		for (short i = 0; i < nc.ui.pp.pub.ExcelColumnInfo.saAskCaptionForHead.length; i++) {

			if (i < 3) {
				row = sheet.getRow(2);
				if (row == null)
					row = sheet.createRow(2);
			} else {
				row = sheet.getRow(3);
				if (row == null)
					row = sheet.createRow(3);
			}
			if (i == 0 || i == 3) {
				j = 0;
			}
			if (i == 1 || i == 4) {
				j = 7;
			}
			if (i == 2 || i == 5) {
				j = 13;
			}
			creLine = new Integer(j).shortValue();
			cell = row.getCell(creLine);

			if (cell == null)
				cell = row.createCell(creLine);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell
					.setCellValue(nc.ui.pp.pub.ExcelColumnInfo.saAskCaptionForHead[i]);

		}

		// Style the cell with borders all around.
		HSSFCellStyle style = wb.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_DASHED);
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setBorderLeft(HSSFCellStyle.BORDER_DASHED);
		style.setLeftBorderColor(HSSFColor.GREEN.index);
		style.setBorderRight(HSSFCellStyle.BORDER_DASHED);
		style.setRightBorderColor(HSSFColor.BLUE.index);
		style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM_DASHED);
		style.setTopBorderColor(HSSFColor.BLACK.index);

		if (sheet == null)
			return;
		HSSFRow rowItem = sheet.getRow(5);
		if (rowItem == null)
			rowItem = sheet.createRow(5);
		for (short i = 0; i < nc.ui.pp.pub.ExcelColumnInfo.saAskCaption.length; i++) {
			cell = rowItem.getCell(i);
			if (cell == null)
				cell = rowItem.createCell(i);
			cell.setCellStyle(style);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(nc.ui.pp.pub.ExcelColumnInfo.saAskCaption[i]);
		}

	}

	/**
	 * Author������ ���ܣ�����EXCEL�ļ��б��⣨���۵��ã� ������ ���أ� ���⣺ ���ڣ�(2004-10-16 13:02:19)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param sheet
	 *            org.apache.poi.hssf.usermodel.HSSFSheet
	 */
	private void createVendorExcelCaption(HSSFSheet sheet) {
		if (sheet == null)
			return;
		HSSFRow row = sheet.getRow(0);
		if (row == null)
			row = sheet.createRow(0);
		for (short i = 0; i < nc.ui.pp.pub.ExcelColumnInfo.saVendorCaption.length; i++) {
			HSSFCell cell = row.getCell(i);
			if (cell == null)
				cell = row.createCell(i);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(nc.ui.pp.pub.ExcelColumnInfo.saVendorCaption[i]);

		}
	}

	/**
	 * Author mlr ���ܣ���õ����ת��ΪReporBaseVO[]��ʽ��EXCEL�ļ���ѯ�۵��ã� ������ ���أ� ���⣺
	 * ���ڣ�(2004-10-15 14:43:44) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.vo.pp.ask.ExcelFileVO[]
	 * @throws Exception
	 */
	public ReportBaseVO[] getAskVOsFromExcel() throws Exception {
		ReportBaseVO[] voaReturn;

		Vector vcSheet = getDataRowVector(0);

		if (vcSheet == null)
			return null;
		if (vcSheet.size() < 1)
			return null;

		int len = vcSheet.size();
		voaReturn = new ReportBaseVO[len];

		for (int i = 0; i < len; i++) {

			ReportBaseVO voTemp = new ReportBaseVO();
			HSSFRow rowTemp = (HSSFRow) vcSheet.get(i);
			setRowToVO(rowTemp, voTemp);
			// //����Excel�ļ��ı�־λ
			// voTemp.setExcelFlag(fileFlag);
			voaReturn[i] = voTemp;
		}
		if (voaReturn.length > 0) {
			return voaReturn;
		}
		return null;

	}

	/**
	 * Author������ ���ܣ�������������� ������ ���أ� ���⣺ ���ڣ�(2004-10-15 14:12:16)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return java.util.Vector
	 * @param sheetID
	 *            int
	 */
	private java.util.Vector getDataRowVector(int sheetID) {
		java.util.Iterator iter = sheet[sheetID].rowIterator();
		java.util.Vector vcRow = new java.util.Vector();

		while (iter.hasNext()) {
			HSSFRow row = (HSSFRow) iter.next();
			if (row == null || row.getCell((short) 16) == null) {
				continue;
			}
			vcRow.add(row);
		}
		return vcRow;
	}

	/**
	 * ���� ���ܣ����EXCEL�ļ�״̬��־��ѯ�۵��ã� ������ ���أ� ���⣺ ���ڣ�(2004-10-15 9:10:46)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return java.lang.String
	 */
	public String getExcelFileFlag() {

		HSSFRow row = sheet[0].getRow(0);
		if (row == null) {
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
					"UPP40040701-000194")/* @res "�ļ���ʽ����" */;
		}
		int iColumn = ExcelColumnInfo.saAskCaption.length;
		HSSFCell cell = row.getCell((short) iColumn);
		if (cell == null || cell.getCellType() != HSSFCell.CELL_TYPE_STRING) {
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
					"UPP40040701-000195")/* @res "�հ�״̬λ" */;
		}
		String value = cell.getStringCellValue().trim();
		if (!value.equals(IExcelFileFlag.F_EDITED)
				&& !value.equals(IExcelFileFlag.F_EDITING)
				&& !value.equals(IExcelFileFlag.F_NEW)
				&& !value.equals(IExcelFileFlag.F_UPLOAD)
				&& !value.equals(IExcelFileFlag.F_UPLOADFAILED))
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
					"UPP40040701-000196")/* @res "״̬λ����" */;

		return value;

	}

	/**
	 * ���� ���ܣ�����в����ˣ�ѯ�۵��ã� ������ ���أ� ���⣺ ���ڣ�(2004-10-15 9:12:17)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return java.lang.String
	 */
	public String getStatusWho() {
		HSSFRow row = sheet[0].getRow(0);
		if (row == null) {
			return IExcelFileFlag.F_NO_BODY;
		}
		int iColumn = ExcelColumnInfo.saAskCaption.length + 1;
		HSSFCell cell = row.getCell((short) iColumn);
		if (cell == null || cell.getCellType() != HSSFCell.CELL_TYPE_STRING) {
			return IExcelFileFlag.F_NO_BODY;
		}
		String value = cell.getStringCellValue();

		if (value == null || value.trim().equals(""))
			return IExcelFileFlag.F_NO_BODY;

		return value.trim();
	}

	/**
	 * Author������ ���ܣ����EXCEL�ļ�״̬��־�����۵��ã� ������ ���أ� ���⣺ ���ڣ�(2004-10-16 13:05:02)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return java.lang.String
	 */
	public String getVendorExcelFileFlag() {

		HSSFRow row = sheet[0].getRow(0);
		if (row == null) {
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
					"UPP40040701-000194")/* @res "�ļ���ʽ����" */;
		}
		int iColumn = ExcelColumnInfo.saVendorCaption.length;
		HSSFCell cell = row.getCell((short) iColumn);
		if (cell == null || cell.getCellType() != HSSFCell.CELL_TYPE_STRING) {
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
					"UPP40040701-000195")/* @res "�հ�״̬λ" */;
		}
		String value = cell.getStringCellValue().trim();
		if (!value.equals(IExcelFileFlag.F_EDITED)
				&& !value.equals(IExcelFileFlag.F_EDITING)
				&& !value.equals(IExcelFileFlag.F_NEW)
				&& !value.equals(IExcelFileFlag.F_UPLOAD)
				&& !value.equals(IExcelFileFlag.F_UPLOADFAILED))
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
					"UPP40040701-000196")/* @res "״̬λ����" */;

		return value;

	}

	/**
	 * Author������ ���ܣ�����в����ˣ����۵��ã� ������ ���أ� ���⣺ ���ڣ�(2004-10-16 16:50:16)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return java.lang.String
	 */
	public String getVendorStatusWho() {
		HSSFRow row = sheet[0].getRow(0);
		if (row == null) {
			return IExcelFileFlag.F_NO_BODY;
		}
		int iColumn = ExcelColumnInfo.saVendorCaption.length + 1;
		HSSFCell cell = row.getCell((short) iColumn);
		if (cell == null || cell.getCellType() != HSSFCell.CELL_TYPE_STRING) {
			return IExcelFileFlag.F_NO_BODY;
		}
		String value = cell.getStringCellValue();

		if (value == null || value.trim().equals(""))
			return IExcelFileFlag.F_NO_BODY;

		return value.trim();
	}

	/**
	 * Author������ ���ܣ���õ����ת��ΪExcelFileVO��ʽ��EXCEL�ļ������۵��ã� ������ ���أ� ���⣺
	 * ���ڣ�(2004-10-16 16:48:47) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.vo.pp.ask.ExcelFileVO[]
	 */
	public ExcelFileVO[] getVendorVOsFromExcel() {
		nc.vo.pp.ask.ExcelFileVO[] voaReturn;

		Vector vcSheet = getDataRowVector(0);

		if (vcSheet == null)
			return null;
		if (vcSheet.size() <= 1)
			return null;

		int len = vcSheet.size() - 1;
		voaReturn = new nc.vo.pp.ask.ExcelFileVO[len];

		for (int i = 1; i <= len; i++) {

			nc.vo.pp.ask.ExcelFileVO voTemp = new nc.vo.pp.ask.ExcelFileVO();
			HSSFRow rowTemp = (HSSFRow) vcSheet.get(i);
			setVendorRowToVO(rowTemp, voTemp);
			// //����Excel�ļ��ı�־λ
			// voTemp.setExcelFlag(fileFlag);
			voaReturn[i - 1] = voTemp;
		}
		if (voaReturn.length > 0) {
			return voaReturn;
		}
		return null;

	}

	/**
	 * ���� ���ܣ�����EXCEL�ļ�״̬��־��ѯ�۵��ã� ������ ���أ� ���⣺ ���ڣ�(2004-10-15 9:16:51)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return java.lang.String
	 * @param strFlag
	 *            java.lang.String
	 */
	public String setExcelFileFlag(String strFlag) {
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(m_fileName);

			HSSFRow row = sheet[0].getRow(0);
			if (row == null) {
				return "error";
			}
			int iColumn = nc.ui.pp.pub.ExcelColumnInfo.saAskCaption.length;
			HSSFCell cell = row.getCell((short) iColumn);

			// ��һ�ε���ʱ���ļ���ʾ��ΪNEW
			if (cell == null) {
				cell = row.createCell((short) iColumn);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(IExcelFileFlag.F_NEW);
			}
			// String value = cell.getStringCellValue().trim();
			// if (!value.equals(IExcelFileFlag.F_EDITED)
			// && !value.equals(IExcelFileFlag.F_EDITING)
			// && !value.equals(IExcelFileFlag.F_NEW)
			// && !value.equals(IExcelFileFlag.F_UPLOAD))
			// return "error";

			cell.setCellValue(strFlag);

			wb.write(fileOut);
			fileOut.close();
		} catch (IOException e) {
			SCMEnv.out(e.getMessage());
			PuTool.outException(e);
			JOptionPane
					.showMessageDialog(null, nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("40040701", "UPP40040701-000211")/*
																		 * @res
																		 * "��д�ļ��쳣��"
																		 */);
		}
		return "setValue success";
	}

	public abstract String[] getFieldNames();

	/**
	 * author mlr ���ܣ������ϴ�����ÿ�е�ֵ��ѯ�۵��ã� ���ڣ�(2004-10-15 9:17:14)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @throws Exception
	 */
	private void setRowToVO(HSSFRow row, ReportBaseVO vo) throws Exception {
		if (row == null || vo == null)
			return;
		SimpleDateFormat formatter = null;
		String dateToString = null;
		String[] fieldNames = getFieldNames();
		if (fieldNames == null || fieldNames.length == 0)
			throw new Exception("û��ע���ӦExcel�ļ����ֶ��б�");
		for (short i = 0; i < fieldNames.length; i++) {
			HSSFCell cellTemp = row.getCell(i);
			// �м䲻���п���
			if (cellTemp == null) {
				continue;
			}
			int type = cellTemp.getCellType();
			Object value = null;

			switch (type) {
			case HSSFCell.CELL_TYPE_STRING:
				value = PuPubVO.getString_TrimZeroLenAsNull(cellTemp
						.getStringCellValue());
				if (value == null) {
					continue;
				}
				value = value.toString().trim();
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				double db = cellTemp.getNumericCellValue();
				// ����������� 2008.1.15 modify by donggq
				String strFormat = // java.text.NumberFormat.getNumberInstance(java.util.Locale.CHINA).format(db);
				new UFDouble(db).toString();
				java.util.StringTokenizer strToken = new java.util.StringTokenizer(
						strFormat, ",");
				StringBuffer sbBuffer = new StringBuffer();
				while (strToken.hasMoreTokens()) {
					String strT = strToken.nextToken();
					sbBuffer.append(strT);

				}
				if (sbBuffer.toString().trim().length() > 0) {
					value = new UFDouble(sbBuffer.toString().trim());
				}
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
			case HSSFCell.CELL_TYPE_BLANK:
			case HSSFCell.CELL_TYPE_ERROR:
			case HSSFCell.CELL_TYPE_FORMULA:
			default:
				break;
			}
			vo.setAttributeValue(fieldNames[i], value);
		}
	}

	/**
	 * ���� ���ܣ������ϴ�����ÿ�е�ֵ��ѯ�۵��ã� ������ ���أ� ���⣺ ���ڣ�(2004-10-15 9:17:14)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param row
	 *            org.apache.poi.hssf.usermodel.HSSFRow
	 * @param vo
	 *            nc.vo.pp.ask.ExcelFileVO
	 */
	private void setRowToVO(HSSFRow row, nc.vo.pp.ask.ExcelFileVO vo) {
		if (row == null || vo == null)
			return;
		SimpleDateFormat formatter = null;
		String dateToString = null;
		for (short i = 0; i < nc.ui.pp.pub.ExcelColumnInfo.saAskVOName.length; i++) {
			HSSFCell cellTemp = row.getCell(i);
			String strValue = null;
			// �м䲻���п���
			if (cellTemp == null) {
				continue;

			}
			if ((i == 11 || i == 13 || i == 14)) {// &&
				// (cellTemp.getDateCellValue()
				// != null &&
				// cellTemp.getDateCellValue().toString().trim().length()
				// > 0 )
				UFDate date = null;
				if (cellTemp.getCellType() == HSSFCell.CELL_TYPE_STRING
						&& PuPubVO.getString_TrimZeroLenAsNull(cellTemp
								.getStringCellValue()) != null) {
					date = new UFDate(cellTemp.getStringCellValue());
				} else if (cellTemp.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
					date = new UFDate(cellTemp.getDateCellValue());
				}
				if (date != null) {
					formatter = new SimpleDateFormat("yyyy-MM-dd");
					dateToString = formatter.format(date.toDate());
				}
			}
			switch (cellTemp.getCellType()) {
			case HSSFCell.CELL_TYPE_STRING:
				String strCell = cellTemp.getStringCellValue();
				if (strCell == null)
					continue;
				if (strCell.trim().equals(""))
					continue;
				if ((i == 11 || i == 13 || i == 14)
						&& (dateToString != null && dateToString.toString()
								.trim().length() > 0)) {
					strValue = dateToString;
				} else {
					strValue = strCell;
				}
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:

				if ((i == 11 || i == 13 || i == 14)
						&& (dateToString != null && dateToString.toString()
								.trim().length() > 0)) {
					strValue = dateToString;
				} else {
					double db = cellTemp.getNumericCellValue();
					// ����������� 2008.1.15 modify by donggq
					String strFormat = // java.text.NumberFormat.getNumberInstance(java.util.Locale.CHINA).format(db);
					new UFDouble(db).toString();
					java.util.StringTokenizer strToken = new java.util.StringTokenizer(
							strFormat, ",");
					StringBuffer sbBuffer = new StringBuffer();
					while (strToken.hasMoreTokens()) {
						String strT = strToken.nextToken();
						sbBuffer.append(strT);

					}
					if (sbBuffer.toString().trim().length() > 0) {
						strValue = sbBuffer.toString().trim();
					}
				}
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
			case HSSFCell.CELL_TYPE_BLANK:
			case HSSFCell.CELL_TYPE_ERROR:
			case HSSFCell.CELL_TYPE_FORMULA:
			default:
				break;

			}

			vo.setAttributeValue(ExcelColumnInfo.saAskVOName[i], strValue);
		}

	}

	/**
	 * Author������ ���ܣ�����EXCEL�ļ�״̬��־�����۵��ã� ������ ���أ� ���⣺ ���ڣ�(2004-10-16 13:05:44)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return java.lang.String
	 * @param strFlag
	 *            java.lang.String
	 */
	public String setVendorExcelFileFlag(String strFlag) {
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(m_fileName);

			HSSFRow row = sheet[0].getRow(0);
			if (row == null) {
				return "error";
			}
			int iColumn = nc.ui.pp.pub.ExcelColumnInfo.saVendorCaption.length;
			HSSFCell cell = row.getCell((short) iColumn);

			// ��һ�ε���ʱ���ļ���ʾ��ΪNEW
			if (cell == null) {
				cell = row.createCell((short) iColumn);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(IExcelFileFlag.F_NEW);
			}
			// String value = cell.getStringCellValue().trim();
			// if (!value.equals(IExcelFileFlag.F_EDITED)
			// && !value.equals(IExcelFileFlag.F_EDITING)
			// && !value.equals(IExcelFileFlag.F_NEW)
			// && !value.equals(IExcelFileFlag.F_UPLOAD))
			// return "error";

			cell.setCellValue(strFlag);

			wb.write(fileOut);
			fileOut.close();
		} catch (IOException e) {
			SCMEnv.out(e.getMessage());
			PuTool.outException(e);
			JOptionPane
					.showMessageDialog(null, nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("40040701", "UPP40040701-000198")/*
																		 * @res
																		 * "setExcelFileFlag��д�ļ��쳣��"
																		 */);
		}
		return "setValue success";
	}

	/**
	 * Author������ ���ܣ������ϴ�����ÿ�е�ֵ�����۵��ã� ������ ���أ� ���⣺ ���ڣ�(2004-10-16 16:47:17)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param row
	 *            org.apache.poi.hssf.usermodel.HSSFRow
	 * @param vo
	 *            nc.vo.pp.ask.ExcelFileVO
	 */
	private void setVendorRowToVO(HSSFRow row, ExcelFileVO vo) {
		if (row == null || vo == null)
			return;
		for (short i = 0; i < nc.ui.pp.pub.ExcelColumnInfo.saVendorVOName.length; i++) {
			HSSFCell cellTemp = row.getCell(i);
			String strValue = null;
			// �м䲻���п���
			if (cellTemp == null) {
				continue;

			}

			switch (cellTemp.getCellType()) {
			case HSSFCell.CELL_TYPE_STRING:
				String strCell = cellTemp.getStringCellValue();
				if (strCell == null)
					continue;
				if (strCell.trim().equals(""))
					continue;
				strValue = strCell;
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:

				double db = cellTemp.getNumericCellValue();

				String strFormat = java.text.NumberFormat.getInstance(
						java.util.Locale.CHINA).format(db);
				java.util.StringTokenizer strToken = new java.util.StringTokenizer(
						strFormat, ",");
				StringBuffer sbBuffer = new StringBuffer();
				while (strToken.hasMoreTokens()) {
					String strT = strToken.nextToken();
					sbBuffer.append(strT);

				}
				if (sbBuffer.toString().trim().length() > 0) {
					strValue = sbBuffer.toString().trim();
				}
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
			case HSSFCell.CELL_TYPE_BLANK:
			case HSSFCell.CELL_TYPE_ERROR:
			case HSSFCell.CELL_TYPE_FORMULA:
			default:
				break;

			}

			vo.setAttributeValue(ExcelColumnInfo.saVendorVOName[i], strValue);
		}

	}

	/**
	 * Author������ ���ܣ��ѵ��������õ�EXCEL�ļ���Ӧ�����У����۵��ã� ������ ���أ� ���⣺ ���ڣ�(2004-10-16
	 * 13:06:38) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param voFile
	 *            nc.vo.pp.ask.ExcelFileVO
	 * @param line
	 *            int
	 */
	public void setVendorVOAtLine(ExcelFileVO voFile, int line) {
		try {
			if (line <= 0)
				return;
			if (voFile == null)
				return;

			HSSFRow row = sheet[0].getRow(line);
			if (row == null)
				row = sheet[0].createRow((short) line);

			for (short i = 0; i < nc.ui.pp.pub.ExcelColumnInfo.saVendorVOName.length; i++) {
				HSSFCell cell = row.getCell(i);
				if (cell == null)
					cell = row.createCell(i);
				// ���֮ǰ�ļ�Ϊ���֣�������������쳣��������Ҫ����
				// cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				String strCellValue = (String) voFile
						.getAttributeValue(nc.ui.pp.pub.ExcelColumnInfo.saVendorVOName[i]);
				if (strCellValue == null)
					cell.setCellValue("");
				else
					cell.setCellValue(strCellValue);

			}
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			SCMEnv.out("filetoExcel err :" + e.getMessage());
			PuTool.outException(e);
		}
	}

	/**
	 * Author������ ���ܣ��ѵ������õ�EXCEL�ļ��У����۵��ã� ������ ���أ� ���⣺ ���ڣ�(2004-10-16 13:07:52)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param voaFile
	 *            nc.vo.pp.ask.ExcelFileVO[]
	 * @param fileName
	 *            java.lang.String
	 * @exception java.io.IOException
	 *                �쳣˵����
	 */
	public void setVendorVOToExcel(ExcelFileVO[] voaFile, String fileName)
			throws java.io.IOException {
		try {
			java.io.File f = new java.io.File(fileName);
			if (!f.exists()) {
				f.createNewFile();
			}
			FileOutputStream fileOut = new FileOutputStream(fileName, false);

			if (wb == null) {
				wb = new HSSFWorkbook();
				sheet = new HSSFSheet[1];
				sheet[0] = wb.createSheet("BillInfo");

			}
			createVendorExcelCaption(sheet[0]);

			if (voaFile == null || voaFile.length <= 0 || voaFile[0] == null) {
				clearVendorRestLines(1, 1000);
			}

			if (voaFile.length != 0) {
				int len = voaFile.length;
				for (int i = 0; i < len; i++) {
					if (voaFile[i].getAttributeValue("vaskbillcode") == null) {
						continue;
					}
					setVendorVOAtLine(voaFile[i], i + 1);
				}
			}

			// Write the output to a file
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			SCMEnv.out("filetoExcel err :" + e.getMessage());
			PuTool.outException(e);
		}
	}

	/**
	 * ���� ���ܣ��ѵ��������õ�EXCEL�ļ���Ӧ�����У�ѯ�۵��ã� ������ ���أ� ���⣺ ���ڣ�(2004-10-15 9:17:30)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param voFile
	 *            nc.vo.pp.ask.ExcelFileVO
	 * @param line
	 *            int
	 */
	public void setVOAtLine(nc.vo.pp.ask.ExcelFileVO voFile, int line) {
		try {
			if (line <= 0)
				return;
			if (voFile == null)
				return;

			HSSFRow row = sheet[0].getRow(line);

			if (row == null)
				row = sheet[0].createRow((short) line);
			// Style the cell with borders all around
			HSSFCellStyle style = wb.createCellStyle();
			style.setBorderBottom(HSSFCellStyle.BORDER_DASHED);
			style.setBottomBorderColor(HSSFColor.BLACK.index);
			style.setBorderLeft(HSSFCellStyle.BORDER_DASHED);
			style.setLeftBorderColor(HSSFColor.GREEN.index);
			style.setBorderRight(HSSFCellStyle.BORDER_DASHED);
			style.setRightBorderColor(HSSFColor.BLUE.index);
			style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM_DASHED);
			style.setTopBorderColor(HSSFColor.BLACK.index);
			HSSFCellStyle styleForHidden = wb.createCellStyle();
			styleForHidden.setHidden(true);
			styleForHidden.setLocked(true);
			UFDate valueOfDate = null;
			UFDouble valueOfDouble = null;
			for (short i = 0; i < nc.ui.pp.pub.ExcelColumnInfo.saAskVOName.length; i++) {
				HSSFCell cell = row.getCell(i);
				if (cell == null)
					cell = row.createCell(i);
				// ���֮ǰ�ļ�Ϊ���֣�������������쳣��������Ҫ����
				// cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				String strCellValue = (String) voFile
						.getAttributeValue(nc.ui.pp.pub.ExcelColumnInfo.saAskVOName[i]);
				if ((i == 7 || i == 8 || i == 9 || i == 10 || i == 12)
						&& (strCellValue != null && strCellValue.toString()
								.trim().length() > 0)) {
					valueOfDouble = new UFDouble(strCellValue);
				} else if ((i == 11 || i == 13 || i == 14)
						&& (strCellValue != null && strCellValue.toString()
								.trim().length() > 0)) {
					valueOfDate = new UFDate(strCellValue);
				}

				if (i == 7 || i == 8 || i == 9 || i == 10 || i == 12) {
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				} else {
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				}
				if (strCellValue == null
						|| (strCellValue != null && strCellValue.toString()
								.trim().length() == 0)) {
					cell.setCellValue("");
				} else if (i == 7 || i == 8 || i == 9 || i == 10 || i == 12) {
					if (valueOfDouble != null
							&& valueOfDouble.toString().trim().length() > 0) {
						cell.setCellValue(valueOfDouble.doubleValue());
					} else {
						cell.setCellValue(strCellValue);
					}
				}
				// else if(i == 11 || i == 13 || i ==14 ){
				// if(valueOfDate != null &&
				// valueOfDate.toString().trim().length() > 0){
				// cell.setCellValue(valueOfDate.toDate());
				// }else{
				// cell.setCellValue(strCellValue);
				// }
				// }
				else {
					cell.setCellValue(strCellValue);
				}

				if (i < 15) {
					cell.setCellStyle(style);
				} else {
					cell.setCellStyle(styleForHidden);
				}
			}
		} catch (Exception e) {
			SCMEnv.out("filetoExcel err :" + e.getMessage());
			PuTool.outException(e);
		}
	}

	/**
	 * ���� ���ܣ��ѵ��������õ�EXCEL�ļ���Ӧ�����У�ѯ�۵��ã� ������ ���أ� ���⣺ ���ڣ�(2004-10-15 9:17:30)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param voFile
	 *            nc.vo.pp.ask.ExcelFileVO
	 * @param line
	 *            int
	 */
	public void setVOAtLineForHead(nc.vo.pp.ask.ExcelFileVO voFile, int line) {
		try {
			if (line <= 0)
				return;
			if (voFile == null)
				return;

			HSSFRow row = null;

			int j = 1;
			short createLine = 0;
			for (short i = 0; i < nc.ui.pp.pub.ExcelColumnInfo.saAskVONameForHead.length; i++) {
				if (i < 3) {
					row = sheet[0].getRow(2);
					if (row == null)
						row = sheet[0].createRow((short) 2);
				} else {
					row = sheet[0].getRow(3);
					if (row == null)
						row = sheet[0].createRow((short) 3);
				}
				if (i == 0 || i == 3) {
					j = 1;
				}
				if (i == 1 || i == 4) {
					j = 8;
				}
				if (i == 2 || i == 5) {
					j = 14;
				}
				createLine = new Integer(j).shortValue();
				HSSFCell cell = row.getCell(createLine);
				if (cell == null)
					cell = row.createCell(createLine);
				// ���֮ǰ�ļ�Ϊ���֣�������������쳣��������Ҫ����
				// cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				String strCellValue = (String) voFile
						.getAttributeValue(nc.ui.pp.pub.ExcelColumnInfo.saAskVONameForHead[i]);
				if (strCellValue == null)
					cell.setCellValue("");
				else
					cell.setCellValue(strCellValue);

			}
			j = 0;
			createLine = 0;
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			SCMEnv.out("filetoExcel err :" + e.getMessage());
			PuTool.outException(e);
		}
	}

	/**
	 * ���� ���ܣ��ѵ������õ�EXCEL�ļ��У�ѯ�۵��ã� ������ ���أ� ���⣺ ���ڣ�(2004-10-15 9:18:19)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param voaFile
	 *            nc.vo.pp.ask.ExcelFileVO[]
	 * @param fileName
	 *            java.lang.String
	 * @exception java.io.IOException
	 *                �쳣˵����
	 */
	public void setVOToExcel(nc.vo.pp.ask.ExcelFileVO[] voaFile, String fileName)
			throws IOException {
		try {
			java.io.File f = new java.io.File(fileName);
			if (!f.exists()) {
				f.createNewFile();
			}
			FileOutputStream fileOut = new FileOutputStream(fileName, false);

			if (wb == null) {
				wb = new HSSFWorkbook();
				sheet = new HSSFSheet[1];
				sheet[0] = wb.createSheet("BillInfo");

			}
			createExcelCaption(sheet[0]);

			if (voaFile == null || voaFile.length <= 0 || voaFile[0] == null) {
				clearRestLines(1, 1000);
			}
			setVOAtLineForHead(voaFile[0], 2);
			if (voaFile.length != 0) {
				int len = voaFile.length;
				for (int i = 1; i < len; i++) {

					setVOAtLine(voaFile[i], i + 5);
				}
			}

			// Write the output to a file
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			SCMEnv.out("filetoExcel err :" + e.getMessage());
			PuTool.outException(e);
		}
	}

	/**
	 * ����Ҫ���ݱ����ѯid���ֶ�
	 * 
	 * @author mlr
	 * @˵�������׸ڿ�ҵ�� 2011-11-4����05:59:23
	 * @return
	 */
	public abstract String[] getQueryIds();

	/**
	 * ����Ҫ���ݱ����ѯid���ֶ� ������� ������ getQueryIds()һһ��Ӧ
	 * 
	 * @author mlr
	 * @˵�������׸ڿ�ҵ�� 2011-11-4����05:59:23
	 * @return
	 */
	public abstract String[] getQueryTables();

	/**
	 * ����Ҫ��ѯ�Ŀ�� ��Ӧ��id ������ getQueryTables()һһ��Ӧ
	 * 
	 * @author mlr
	 * @˵�������׸ڿ�ҵ�� 2011-11-4����05:59:23
	 * @return
	 */
	public abstract String[] getQuerySelectIDs();

	/**
	 * ����Ҫ��ѯ�Ŀ�� ��Ӧ�ı�������� ������ getQueryTables()һһ��Ӧ
	 * 
	 * @author mlr
	 * @˵�������׸ڿ�ҵ�� 2011-11-4����05:59:23
	 * @return
	 */
	public abstract String[] getQueryCodeNames();

	/**
	 * �Ƿ��ѯ��˾ ������ getQueryTables()һһ��Ӧ
	 * 
	 * @author mlr
	 * @˵�������׸ڿ�ҵ�� 2011-11-4����05:59:23
	 * @return
	 */
	public abstract boolean[] getIsQueCorps();

	/**
	 * �������ݽ����󹹽�����
	 * 
	 * @author mlr
	 * @˵�������׸ڿ�ҵ�� 2011-11-5����10:06:33
	 * @return
	 */
	public abstract String getReturnVO();

	/**
	 * �Ƿ������ ������getQueryTables һһ��Ӧ
	 * 
	 * @author mlr
	 * @˵�������׸ڿ�ҵ�� 2011-11-5����01:42:59
	 * @return
	 */
	public abstract boolean[] getIsMutitables();

	/**
	 * ��ȡ��ֵ�������б�
	 * 
	 * @author mlr
	 * @˵�������׸ڿ�ҵ�� 2011-11-5����02:15:47
	 * @return
	 */
	public abstract String[] getSetValueIds();

}

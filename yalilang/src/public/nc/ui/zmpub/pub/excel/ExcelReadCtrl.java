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
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.zmpub.pub.tool.SingleVOChangeDataUiTool;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.pub.report.ReportBaseVO;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * Excel文件读取类
 * 
 * @author mlr
 */
@SuppressWarnings("restriction")
public abstract class ExcelReadCtrl {
	private HSSFWorkbook wb = null;// Excel文件对象
	private HSSFSheet[] sheet = null;// sheet数组,代表页们

	// 当前文件名称
	private String m_fileName;

	/**
	 * ExcelReadCtrl 构造子注解。
	 */
	public ExcelReadCtrl() {
		super();
	}

	/**
	 * ExcelReadCtrl 构造子注解。
	 * 
	 * @param sFileName
	 *            java.lang.String
	 * @param flag
	 *            boolean
	 * @exception java.lang.Exception
	 *                异常说明。
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
					 * "Excel文件为空！"
					 */);
			return;
		}
		sheet = new HSSFSheet[sheetSize];

		for (int i = 0; i < sheetSize; i++) {
			sheet[i] = wb.getSheetAt(i);
		}

		fs.close();
	}

//	/**
//	 * ?user> 功能：从第一个sheet指定的Excel行取出VO 参数： 返回： 例外： 日期：(2004-8-25 12:18:30)
//	 * 修改日期，修改人，修改原因，注释标志：
//	 * 
//	 * @return nc.vo.ic.pub.barcodeoffline.ExcelFileVO
//	 * @param line
//	 *            int
//	 */
//	public ExcelFileVO getVOAtLine(int indexRow) {
//		ExcelFileVO vo = new nc.vo.pp.ask.ExcelFileVO();
//		HSSFRow row = sheet[0].getRow(indexRow);
//		setRowToVO(row, vo);
//		return vo;
//	}

	/**
	 * 周晓 功能： 参数： 返回： 例外： 日期：(2004-10-15 9:22:40) 修改日期，修改人，修改原因，注释标志：
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
	 * Author：周晓 功能： 参数： 返回： 例外： 日期：(2004-10-16 13:01:30) 修改日期，修改人，修改原因，注释标志：
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
	 * Author mlr 功能：获得导入的转换为ReporBaseVO[]格式的EXCEL文件（询价单用） 参数： 返回： 例外：
	 * 日期：(2004-10-15 14:43:44) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.pp.ask.ExcelFileVO[]
	 * @throws Exception
	 */
	public ReportBaseVO[] getAskVOsFromExcel() throws Exception {
		ReportBaseVO[] voaReturn = null;
		Vector vcSheet = getDataRowVector(0);

		if (vcSheet == null)
			return null;
		if (vcSheet.size() < 1)
			return null;

		int len = vcSheet.size();
		voaReturn = new ReportBaseVO[len];

		ReportBaseVO voTemp = null;
		HSSFRow rowTemp = null;
		for (int i = 0; i < len; i++) {
			voTemp = new ReportBaseVO();
			rowTemp = (HSSFRow) vcSheet.get(i);
			setRowToVO(rowTemp, voTemp);
			// //设置Excel文件的标志位
			// voTemp.setExcelFlag(fileFlag);
			voaReturn[i] = voTemp;
		}
		if (voaReturn.length > 0) {
			return voaReturn;
		}
		return null;

	}

	/**
	 * Author：周晓 功能：获得数据行向量 参数： 返回： 例外： 日期：(2004-10-15 14:12:16)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.util.Vector
	 * @param sheetID   excel的页码
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
	 * 周晓 功能：获得EXCEL文件状态标志（询价单用） 参数： 返回： 例外： 日期：(2004-10-15 9:10:46)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String
	 */
	public String getExcelFileFlag() {

		HSSFRow row = sheet[0].getRow(0);
		if (row == null) {
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
			"UPP40040701-000194")/* @res "文件格式不对" */;
		}
		int iColumn = ExcelColumnInfo.saAskCaption.length;
		HSSFCell cell = row.getCell((short) iColumn);
		if (cell == null || cell.getCellType() != HSSFCell.CELL_TYPE_STRING) {
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
			"UPP40040701-000195")/* @res "空白状态位" */;
		}
		String value = cell.getStringCellValue().trim();
		if (!value.equals(IExcelFileFlag.F_EDITED)
				&& !value.equals(IExcelFileFlag.F_EDITING)
				&& !value.equals(IExcelFileFlag.F_NEW)
				&& !value.equals(IExcelFileFlag.F_UPLOAD)
				&& !value.equals(IExcelFileFlag.F_UPLOADFAILED))
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
			"UPP40040701-000196")/* @res "状态位不对" */;

		return value;

	}

	/**
	 * 周晓 功能：获得行操作人（询价单用） 参数： 返回： 例外： 日期：(2004-10-15 9:12:17)
	 * 修改日期，修改人，修改原因，注释标志：
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
	 * Author：周晓 功能：获得EXCEL文件状态标志（报价单用） 参数： 返回： 例外： 日期：(2004-10-16 13:05:02)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String
	 */
	public String getVendorExcelFileFlag() {

		HSSFRow row = sheet[0].getRow(0);
		if (row == null) {
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
			"UPP40040701-000194")/* @res "文件格式不对" */;
		}
		int iColumn = ExcelColumnInfo.saVendorCaption.length;
		HSSFCell cell = row.getCell((short) iColumn);
		if (cell == null || cell.getCellType() != HSSFCell.CELL_TYPE_STRING) {
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
			"UPP40040701-000195")/* @res "空白状态位" */;
		}
		String value = cell.getStringCellValue().trim();
		if (!value.equals(IExcelFileFlag.F_EDITED)
				&& !value.equals(IExcelFileFlag.F_EDITING)
				&& !value.equals(IExcelFileFlag.F_NEW)
				&& !value.equals(IExcelFileFlag.F_UPLOAD)
				&& !value.equals(IExcelFileFlag.F_UPLOADFAILED))
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
			"UPP40040701-000196")/* @res "状态位不对" */;

		return value;

	}

	/**
	 * Author：周晓 功能：获得行操作人（报价单用） 参数： 返回： 例外： 日期：(2004-10-16 16:50:16)
	 * 修改日期，修改人，修改原因，注释标志：
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
	 * 周晓 功能：设置EXCEL文件状态标志（询价单用） 参数： 返回： 例外： 日期：(2004-10-15 9:16:51)
	 * 修改日期，修改人，修改原因，注释标志：
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

			// 第一次调用时将文件标示置为NEW
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
					 * "读写文件异常！"
					 */);
		}
		return "setValue success";
	}

	/**
	 * 	注册对应Excel文件的字段列表   单体模式  ishead  传入为  false
	 * @return
	 */
	public  String[] getFieldNames(boolean ishead){
		if(ishead)
			return getHeadFieldNames();
		else
			return getBodyFieldNames();
	}
	
	/**
	 * 返回表头字段名称
	 * @return
	 */
	public abstract String[] getHeadFieldNames();
	/**
	 * 返回表体字段名称
	 * @return
	 */
	public abstract String[] getBodyFieldNames();
	
	/**
	 * 是否表头
	 * @param headFlagCell
	 * @return
	 */
	protected boolean isHead(HSSFCell headFlagCell){
		if(isSingle())//如果是单体的直接返回false
			return false;
		if(headFlagCell == null)
			return false;
		int type = headFlagCell.getCellType();
		if(type != HSSFCell.CELL_TYPE_STRING)
			return false;
		String value = PuPubVO.getString_TrimZeroLenAsNull(headFlagCell.getStringCellValue());
		if(value == null)
			return false;
		if(value.equalsIgnoreCase("Y") || value.equalsIgnoreCase("TRUE"))
			return true;
		return false;
	}

	/**
	 * author mlr 功能：设置上传单据每行的值（询价单用） 日期：(2004-10-15 9:17:14)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @throws Exception
	 */
	private void setRowToVO(HSSFRow row, ReportBaseVO vo) throws Exception {
		if (row == null || vo == null)
			return;
		SimpleDateFormat formatter = null;
		String dateToString = null;
		String[] fieldNames = getFieldNames(isHead(row.getCell((short)0)));
		if (fieldNames == null || fieldNames.length == 0)
			throw new Exception("没有注册对应Excel文件的字段列表");
		for (short i = 0; i < fieldNames.length; i++) {
			HSSFCell cellTemp = row.getCell(i);
			// 中间不能有空行
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
				// 解决精度问题 2008.1.15 modify by donggq
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
	 * Author：周晓 功能：设置EXCEL文件状态标志（报价单用） 参数： 返回： 例外： 日期：(2004-10-16 13:05:44)
	 * 修改日期，修改人，修改原因，注释标志：
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

			// 第一次调用时将文件标示置为NEW
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
					 * "setExcelFileFlag读写文件异常！"
					 */);
		}
		return "setValue success";
	}


	/**
	 * 周晓 功能：把单据行设置到EXCEL文件相应的行中（询价单用） 参数： 返回： 例外： 日期：(2004-10-15 9:17:30)
	 * 修改日期，修改人，修改原因，注释标志：
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
				// 如果之前文件为数字，下面的语句会抛异常，所以需要屏蔽
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
	 * 周晓 功能：把单据行设置到EXCEL文件相应的行中（询价单用） 参数： 返回： 例外： 日期：(2004-10-15 9:17:30)
	 * 修改日期，修改人，修改原因，注释标志：
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
				// 如果之前文件为数字，下面的语句会抛异常，所以需要屏蔽
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
	 * 返回要根据编码查询id的字段
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-11-4下午05:59:23
	 * @return
	 */
//	public abstract String[] getQueryIds();

	/**
	 * 返回要根据编码查询id的字段 库表名字 必须与 getQueryIds()一一对应
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-11-4下午05:59:23
	 * @return
	 */
//	public abstract String[] getQueryTables();

	/**
	 * 返回要查询的库表 对应的id 必须与 getQueryTables()一一对应
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-11-4下午05:59:23
	 * @return
	 */
//	public abstract String[] getQuerySelectIDs();

	/**
	 * 返回要查询的库表 对应的编码的名字 必须与 getQueryTables()一一对应
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-11-4下午05:59:23
	 * @return
	 */
//	public abstract String[] getQueryCodeNames();

	/**
	 * 是否查询公司 必须与 getQueryTables()一一对应
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-11-4下午05:59:23
	 * @return
	 */
//	public abstract boolean[] getIsQueCorps();

	/**
	 * 返回数据交换后构建的类
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-11-5上午10:06:33
	 * @return
	 */
//	public abstract String getReturnVO();

	/**
	 * 是否多表关联 必须月getQueryTables 一一对应
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-11-5下午01:42:59
	 * @return
	 */
//	public abstract boolean[] getIsMutitables();

	/**
	 * 获取赋值的数组列表
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-11-5下午02:15:47
	 * @return
	 */
//	public abstract String[] getSetValueIds();

	/**
	 * 是否单体  还是 表头表体结构  true：单体  false：单据模式
	 * @return
	 */
	public  abstract boolean isSingle();

	/**
	 * 单据类型
	 * @return
	 */
	public  abstract String getBillType();

	/**
	 * excel 虚拟单据类型
	 * @return
	 */
	public String getTmpBillType(){
		return "EXCEL";
	}

	/**
	 * 单体数据转换时需要提供单vo全路径
	 * @return
	 */
	protected abstract Class getSingleVOClass();

	/**
	 * 获得单体 数据交换类   有reportbasevo 转换成 业务数据
	 * @return
	 */
	protected abstract String getSingleChangeClassName();
	/**
	 * 后台插件处理类  必须继承 nc.bs.zmpub.pub.excel.AbstractExcetBO
	 * @return
	 */
	protected abstract String getDealBOClassName();

	/**
	 * 调用后台插件处理数据
	 */
	public void dealData(ReportBaseVO[] rvos) throws Exception {
		if (rvos == null || rvos.length == 0)
			return;
		// 转换成数据vo
		if (isSingle()) {// 单体数据处理
			CircularlyAccessibleValueObject[] vos = SingleVOChangeDataUiTool
			.runChangeVOAry(rvos, getSingleVOClass(),
					getSingleChangeClassName());
			// 转后台处理
			Class[] ParameterTypes = new Class[] { CircularlyAccessibleValueObject[].class };
			Object[] ParameterValues = new Object[] { vos };
			LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME,
					getDealBOClassName(), "dealSingleImportDatas",
					ParameterTypes, ParameterValues, 2);
		} else {// 单据数据处理 或 具有表头表体 的档案 需要注册单据类型 vo对照
			// 直接转后台处理
			Class[] ParameterTypes = new Class[] { ReportBaseVO[].class,
					String.class, String.class };
			Object[] ParameterValues = new Object[] { rvos, getBillType(),
					getTmpBillType() };
			LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME,
					getDealBOClassName(), "dealBillImportDatas",
					ParameterTypes, ParameterValues, 2);
		}
	}

}

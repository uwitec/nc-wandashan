package nc.ui.hg.pu.pub;

/**
 * liuys
 * 
 * 功能：Excel文件读取
 * 日期：(2010-12-25 9:08:46)
 */
import java.io.FileInputStream;
import java.util.Vector;

import javax.swing.JOptionPane;

import nc.itf.hg.pu.pub.HgExcelColumnInfo;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class HgExcelReadCtrl {
	private HSSFWorkbook wb = null;// Excel文件对象
	private HSSFSheet[] sheet = null;// sheet数组,代表页们

	// 当前文件名称
	private String m_fileName;

	/**
	 * ExcelReadCtrl 构造子注解。
	 */
	public HgExcelReadCtrl() {
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
	public HgExcelReadCtrl(String sFileName, boolean flag) throws Exception {
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

	/**
	 * Author：周晓 功能：获得导入的转换为ExcelFileVO格式的EXCEL文件（年度需求计划用） 参数： 返回： 例外：
	 * 日期：(2004-10-15 14:43:44) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.pp.ask.ExcelFileVO[]
	 */
	public nc.vo.hg.pu.pub.HgYearExcelFileVO[] getAskVOsFromExcel() {
		nc.vo.hg.pu.pub.HgYearExcelFileVO[] voaReturn;

		Vector vcSheet = getDataRowVector(0);

		if (vcSheet == null)
			return null;
		if (vcSheet.size() < 1)
			return null;

		int len = vcSheet.size();
		if(len > 1000){
			JOptionPane.showMessageDialog(null, "导入文件行数不能超过1000行!");
			return null;
		}
		voaReturn = new nc.vo.hg.pu.pub.HgYearExcelFileVO[len];
		
		for (int i = 1; i < len; i++) {

			nc.vo.hg.pu.pub.HgYearExcelFileVO voTemp = new nc.vo.hg.pu.pub.HgYearExcelFileVO();
			HSSFRow rowTemp = (HSSFRow) vcSheet.get(i);
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
	 * liuys 功能：设置上传单据每行的值（年度需求计划用） 参数： 返回： 例外： 日期：(2004-10-15 9:17:14)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param row
	 *            org.apache.poi.hssf.usermodel.HSSFRow
	 * @param vo
	 *            nc.vo.pp.ask.ExcelFileVO
	 */
	private void setRowToVO(HSSFRow row, nc.vo.hg.pu.pub.HgYearExcelFileVO vo) {
		if (row == null || vo == null)
			return;
		for (short i = 0; i < HgExcelColumnInfo.saAskVOName.length; i++) {
			HSSFCell cellTemp = row.getCell(i);
			String strValue = null;
			// 中间不能有空行
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

			vo.setAttributeValue(HgExcelColumnInfo.saAskVOName[i], strValue);
		}

	}
}
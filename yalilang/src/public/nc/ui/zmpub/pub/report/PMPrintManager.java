package nc.ui.zmpub.pub.report;
import java.awt.Font;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.table.TableColumn;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.print.PrintDirectEntry;
import nc.ui.pub.print.datastruct.CellRange;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

// Referenced classes of package nc.ui.ar.printadapter:
//            XDefaultAIL, TemplatePrintAdapter, PrintDataitemBO_Client, AddtionInfoLayouter

public class PMPrintManager extends nc.ui.report.base.PrintManager
{ //static int m_iContentRowCount =0;
private static Vector diStillHeaderInfo(UITable table)
	{
		Vector info = new Vector();
		GroupableTableHeader header = (GroupableTableHeader)table.getTableHeader();
		Vector colInfo;
		for(Enumeration enumeration = header.getColumnModel().getColumns(); enumeration.hasMoreElements(); info.addElement(colInfo))
		{
			colInfo = new Vector();
			TableColumn aColumn = (TableColumn)enumeration.nextElement();
			Enumeration cGroups = header.getColumnGroups(aColumn);
			if(cGroups != null)
			{
				ColumnGroup cGroup;
				for(; cGroups.hasMoreElements(); colInfo.addElement(cGroup.getHeaderValue()))
					cGroup = (ColumnGroup)cGroups.nextElement();

			}
		}

		int len = info.size();
		int mid = 0;
		int max = 0;
		for(int i = 0; i < len; i++)
		{
			Vector v = (Vector)info.elementAt(i);
			mid = v.size();
			if(mid > max)
				max = mid;
		}

		for(int i = 0; i < len; i++)
		{
			((Vector)info.elementAt(i)).setSize(max);
			String colname = table.getColumnName(i);
			((Vector)info.elementAt(i)).addElement(colname);
		}

		return info;
	}
private static int[] getAlignFlag(Object obj[][])
	{
		int alignFlag[] = null;
		if(obj != null && obj.length != 0)
		{
			int width = obj.length;
			int len = obj[0].length;
			int row = 0;
			alignFlag = new int[len];
			for(int i = 0; i < len; i++)
			{
				row = 0;
				do
				{
					if(obj[row][i] == null || obj[row][i].toString().length() == 0)
						continue;
					if((obj[row][i] instanceof UFDouble) || (obj[row][i] instanceof Integer) || (obj[row][i] instanceof Double))
						alignFlag[i] = 2;
					else
					if((obj[row][i] instanceof String) && obj[row][i].toString().equals("√"))
						alignFlag[i] = 1;
					else
						alignFlag[i] = 0;
					break;
				} while(++row < width);
			}

		}
		return alignFlag;
	}
private static CellRange[] getCombinCellRange(String colname[][])
	{
		Vector rangeVector = new Vector();
		CellRange Range[] = null;
		int width = colname.length;
		int len = colname[0].length;
		for(int x = 0; x < width - 1; x++)
		{
			for(int y = 0; y < len; y++)
				if(colname[x][y] != null)
				{
					int xb = x;
					int yb = y;
					int ye;
					for(ye = y + 1; ye < len; ye++)
						if(colname[x][ye] != null)
							break;

					int xe;
					for(xe = x + 1; xe < width; xe++)
						if(colname[xe][y] != null)
							break;

					if(xb != xe || yb != ye)
						rangeVector.addElement(new CellRange(xb, yb, xe - 1, ye - 1));
				}

		}

		int size = rangeVector.size();
		if(size > 0)
		{
			Range = new CellRange[size];
			rangeVector.copyInto(Range);
		}
		return Range;
	}
public static PrintDirectEntry getDirectPrinter(nc.ui.pub.bill.BillCardPanel reportBase, String title) {

	//new PrintDirectEntry
	PrintDirectEntry printer = new PrintDirectEntry();
	//set  Font
	java.awt.Font titleFont = new java.awt.Font("dialog", 1, 16);
	java.awt.Font topFont = new java.awt.Font("dialog", 0, 10);
	Font contentFont = new Font("dialog", 0, 12);
	printer.setTitleFont(titleFont);
	printer.setTopStrFont(topFont);
	printer.setContentFont(contentFont);
	printer.setBottomStrFont(topFont);
	//set Title
	printer.setTitle(title);
	//set Head(Top)	
	setHead(reportBase, printer);
	//set Body
	setBody(reportBase, printer);
	//set Bottom	
	setBottom(reportBase,printer);
	//set Print
	printer.setTopStrFixed(false);
	printer.setBottomStrFixed(false);
	printer.setPageNumAlign(2);
	printer.setPageNumPos(2);
	printer.setPageNumDisp(true);
	printer.setPageNumTotalDisp(false);
	printer.setPrintDirection(true);
	//printer.setMargin(25 ,25, 25, 25);//top ,left ,bottom, right  此处设置有问题跟列宽有关
	//print.setPageNumStyle("页码：", null, null);	
	printer.setPageNumStyle("第", "页 共", "页");
	//return
	return printer;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-5-30 14:20:25)
 */
public static String getItemPrintValue(BillItem item) {
	String strValue = null;
	switch (item.getDataType()) {
		case BillItem.INTEGER :
			{
			}
		case BillItem.STRING :
			{
			}
		case BillItem.TIME :
			{
			}
		case BillItem.DATE :
			{
				if (item.getComponent() instanceof nc.ui.pub.beans.UIRefPane)
					strValue = ((UIRefPane) item.getComponent()).getText();
				break;
			}
		case BillItem.DECIMAL :
			{
				String wb = ((UIRefPane) item.getComponent()).getText();
				nc.vo.pub.lang.UFDouble ufd = new nc.vo.pub.lang.UFDouble(wb, -item.getDecimalDigits());
				strValue = ufd.toString();
				break;
			}
		case BillItem.USERDEF :
			{
				if (item.getComponent() instanceof UIRefPane)
					strValue = ((UIRefPane) item.getComponent()).getRefModel().getRefNameValue();
				//strValue = ((UIRefPane)item.getComponent()).isReturnCode() ? ((UIRefPane)item.getComponent()).getRefCode() : ((UIRefPane)item.getComponent()).getRefName();
				break;
			}
		case BillItem.UFREF :
			{
				if (item.getComponent() instanceof UIRefPane)
					//strValue = ((UIRefPane)item.getComponent()).getRefPK();
					strValue = ((UIRefPane) item.getComponent()).isReturnCode() ? ((UIRefPane) item.getComponent()).getRefCode() : ((UIRefPane) item.getComponent()).getRefName();
				break;
			}
		case BillItem.COMBO :
			{
				if (item.getComponent() instanceof UIComboBox)
					if (((UIComboBox) item.getComponent()).getSelectedItem() != null)
						//if (item.isWithIndex())
						//strValue = ((UIComboBox)item.getComponent()).getSelectedIndex() + "";
						//else
						strValue = ((UIComboBox) item.getComponent()).getSelectedItem().toString();
				break;
			}
		case BillItem.BOOLEAN :
			{
				if (item.getComponent() instanceof UICheckBox)
					if (((UICheckBox) item.getComponent()).isSelected())
						//strValue = "true";
						strValue = "是";
					else
						//strValue = "false";
						strValue = "否";
				break;
			}
	}
	return strValue;
}
private static Object[][] getTableData(UITable table)
	{
		int width = table.getRowCount();
		int length = table.getColumnCount();
		Object obj[][] = new Object[width != 0 ? width : 0][length];
		Object value = null;
		for(int i = 0; i < width; i++)
		{
			for(int k = 0; k < length; k++)
			{
				value = table.getValueAt(i, k);
				if((value instanceof UFBoolean) || (value instanceof Boolean))
				{
					if(value.toString().equals("true") || value.toString().equals("Y"))
						value = "√";
					else
						value = "";
				} else
				if((value instanceof UFDouble) || (value instanceof Integer))
					if(value instanceof UFDouble)
					{
						if(((UFDouble)value).doubleValue() == 0.0D)
							value = "";
					} else
					if((value instanceof Integer) && ((Integer)value).intValue() == 0)
						value = "";
				obj[i][k] = value;
			}

		}

		return obj;
	}
private static String[][] parseInfo(Vector v)
	{
		int width = v.size();
		int len = ((Vector)v.elementAt(0)).size();
		String col[][] = new String[len][width];
		for(int i = 0; i < len; i++)
		{
			for(int k = 0; k < width; k++)
			{
				Vector cv = (Vector)v.elementAt(k);
				col[i][k] = (String)cv.elementAt(i);
			}

		}

		int mid = width;
		width = len;
		len = mid;
		for(int i = 0; i < len; i++)
			if(col[0][i] == null)
			{
				col[0][i] = col[width - 1][i];
				col[width - 1][i] = null;
			}

		for(int i = len - 1; i > 0; i--)
		{
			for(int k = 0; k < width; k++)
				if(col[k][i] == col[k][i - 1])
					col[k][i] = null;

		}

		return col;
	}
/**
 * 此处插入方法说明。
 * 创建日期：(2004-08-26 16:44:14)
 * @param reportBase nc.ui.pub.report.ReportBaseClass
 */
public static void setBody(nc.ui.pub.bill.BillCardPanel reportBase, PrintDirectEntry printer) {
	//get table
	UITable table = reportBase.getBillTable();
	//int height = 20;
	//set colname (include cellRange   set colname is fixed row)
	Vector info = diStillHeaderInfo(table);
	String colname[][] = parseInfo(info);
	printer.setColNames(colname);
	CellRange crg[] = getCombinCellRange(colname);
	printer.setCombinCellRange(crg);
	int iFixRow = colname.length;
	printer.setFixedRows(iFixRow);
	//set data (include dataAlignFlag)
	Object data[][] = getTableData(table);
//	data = getFData(data);
	printer.setData(data);
	printer.setAlignFlag(getAlignFlag(data));

	//set row height
	//int headerRow = colname.length;
	//int bodyRow = data.length;
	//int rowCount = headerRow + bodyRow;

	//int rowHight[] = new int[rowCount];
	//for (int i = 0; i < rowCount; i++)
	//rowHight[i] = height;
	//printer.setRowHeight(rowHight);
	printer.setDefaultRowHeight(15);
	//set col width
	int colWid[] = null;
	if (colWid == null) {
		colWid = new int[table.getColumnCount()];
		for (int i = 0; i < table.getColumnCount(); i++) {
			int iColWidth = table.getColumnModel().getColumn(i).getWidth();
			colWid[i] = iColWidth;
		}

	}
	printer.setColWidth(colWid);

}
/**
 * 此处插入方法说明。
 * 创建日期：(2004-08-26 16:44:14)
 * @param reportBase nc.ui.pub.report.ReportBaseClass
 */
public static void setBottom(
	nc.ui.pub.bill.BillCardPanel reportBase,
	PrintDirectEntry printer) {
	nc.ui.pub.bill.BillItem[] bodyItems = reportBase.getTailShowItems();
	int row = 0;
	String[][] headStr = null;
	int[] headHeight = null;
	if (bodyItems != null) {
		int itemCount = bodyItems.length;
		if (itemCount > 0) {
			row = itemCount / 2 + itemCount % 2;
			headStr = new String[row + 2][2];
			headHeight = new int[row + 2];
			for (int i = 0; i < itemCount; i++) {
				Object value = null;
				if (bodyItems[i].getDataType() == nc.ui.pub.bill.BillCardPanel.UFREF)
					value = ((UIRefPane) bodyItems[i].getComponent()).getText();
				else
					value = bodyItems[i].getValue();

				headStr[i / 2][i % 2] = bodyItems[i].getName() + ": " + value;
				headHeight[i / 2] = 15;
			}

		}
	} else {
		headStr = new String[2][2];
		headHeight = new int[2];
	}
	printer.setBottomStrVerAlign(0);
	ClientEnvironment ce = ClientEnvironment.getInstance();
	String corp = ce.getCorporation().getUnitname();
	String user = ce.getUser().getUserName();
	String time = ce.getBusinessDate().toString();
	//String bottom[][] = new String[2][3];
	headStr[row][0] = "单位: " + corp;
	headStr[row][1] = "操作员: " + user;
	headStr[row + 1][0] = "打印日期: " + time;
	headStr[row + 1][1] = "【用友软件】";
	headHeight[row] = 15;
	headHeight[row + 1] = 15;
	printer.setBottomStr(headStr);
	printer.setBStrColRange(new int[] { 2, 4 });
	printer.setBottomStrHeight(headHeight);
	printer.setBottomStrVerAlign(0);
	printer.setBottomStrAlign(0);
	//int colnum = table.getColumnCount() - 1;
	//printer.setBottomStrAlign(new int[] { 0, 0, 2 });
	//printer.setBStrColRange(new int[] { 1, 2, 4 });
	//printer.setBottomStr(bottom);
}
/**
 * 简单处理：表头只打印两列
 * 创建日期：(2004-08-26 16:44:14)
 * @param reportBase nc.ui.pub.report.ReportBaseClass
 */
public static void setHead(nc.ui.pub.bill.BillCardPanel reportBase,PrintDirectEntry printer) {
	int height = 15;
	nc.ui.pub.bill.BillItem[] headItems = reportBase.getHeadShowItems();
	int headItemCount = headItems.length;
	if (headItemCount > 0) {
		int row = headItemCount / 2 + headItemCount % 2;
		String[][] headStr = new String[row][2];
		int[] headHeight = new int[row];
		for (int i = 0; i < headItemCount; i++) {
			headStr[i / 2][i % 2] = headItems[i].getName() + ": " + headItems[i].getValue();
			headHeight[i / 2] = height;
		}
		printer.setTopStr(headStr);
		printer.setTopStrColRange(new int[] { 2, 4 });
		printer.setTopStrHeight(headHeight);
		printer.setTopStrVerAlign(0);
		printer.setTopStrAlign(0);
	}
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-5-30 14:20:25)
 */
public static void setPrintTopPub(nc.ui.pub.bill.BillCardPanel billcardpanel, nc.ui.pub.print.PrintDirectEntry print) {
	int ie = 0; //debug
	int je = 0; //debug
	try {
		//得到表头信息
		BillItem[] headitems = billcardpanel.getHeadShowItems();
		if (headitems.length == 0) {
			return;
		}
		BillItem[] bodyshowitems = billcardpanel.getBodyShowItems();
		int headcount = headitems.length; //表头项个数
		int rowcount = bodyshowitems.length; //列数
		int headrowcount; //每行表头项个数
		int headlinecount; //表头项行数

		int[] iTops = new int[rowcount];
		for (int i = 0; i < rowcount; i++) {
			iTops[i] = i;
		}

		String[][] strTops;
		if (rowcount % 2 == 0) {
			headrowcount = (rowcount) / 2;
		} else {
			headrowcount = (rowcount - 1) / 2;
		}
		if (headcount % headrowcount == 0) {
			headlinecount = headcount / headrowcount;
		} else {
			headlinecount = ((int) headcount / headrowcount) + 1;
		}
		strTops = new String[headlinecount][rowcount];

		for (int i = 0; i < headlinecount; i++) {
			int j = 0;
			for (j = 0; j < headrowcount * 2; j++) {
				if (j % 2 == 0) {
					strTops[i][j] = headitems[i * headrowcount + j / 2].getName() + ":"+headitems[i * headrowcount + j / 2].getValue();
				}
				//else {
					//strTops[i][j] = getItemPrintValue(headitems[i * headrowcount + (int) j / 2]);
				//}
				if ((i * headrowcount + (int) j / 2) < (headcount - 1)) {
					continue;
				} else {
					if (j % 2 != 0)
						break;
				}
			}
			if ((i * headrowcount + (int) j / 2) < headcount - 1) {
				continue;
			} else {
				if (j % 2 != 0)
					break;
			}
		}
		if (rowcount % 2 != 0) {
			for (int i = 0; i < headlinecount; i++) {
				strTops[i][rowcount - 1] = "";
			}
		}
		//	setString(strTops);
		print.setTopStr(strTops);
		print.setTopStrColRange(new int[]{2,4,6,8});
	} catch (Exception e) {
		System.out.println("错误发生在表头取值时第" + ie + "行第" + je + "列");
		e.printStackTrace();
	}

}
}

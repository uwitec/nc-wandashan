package nc.vo.zmpub.pub.report2;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.table.TableColumnModel;

import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.report.ReportGeneralUtil;
import nc.ui.pub.report.ReportItem;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.cquery.FldgroupVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.rs.MemoryResultSet;
import nc.vo.pub.rs.MemoryResultSetMetaData;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zmpub.pub.report.ReportBaseVO;

/**
 * 报表中经常要用到 将 查询出来的某一列的数据 或 某几列的数据 展开 到行上 形成二维的动态表 什么是一维表？ 所谓的一维表就是 列是固定不变的
 * 而上的数据是动态变化的 也就是 横坐标 不变 纵坐标变
 * 
 * 这种数据 是应该都知道
 * 
 * 那么什么是二维表呢 所谓的二维表就是 行 和 列都是动态变化的
 * 
 * 也就是 会将查询出来的 某一列的数据 动态展开 放到行上面
 * 
 * @author mlr 2011-12-11 ----- 2011-12-16
 */

/**
 *有一个items  要在该items的 4  8   6  11 .......所以位置处 加一个item 如何加呢？
 *
 *首先 用一个int数组 存放 索引  为 indexs
 *
 * 用一个数组来存放  要添加的item 为  newits
 * 
 * 然后 将 items 数组放到一个集合中去 为 oldits
 * 
 * 循环如下：               4 9 8 14
 * 
 * for(int i=0;i<newits.length;i++){
 *    indexs[i]=indexs[i]+i;
 * }
 * 
 * for(int i=0;i<indexs.length;i++){
 *    oldits.add(indexs[i],newits[i]);
 * }
 * 
 * 
 * 
 * 如何构建一颗树
 * 
 * 假设有如下表:
 *  
 *  id  parentid  name code leaval(为第几级)
 *  
 * 封装的vo为  id parentid  name code leaval  list(list用于存放子类) 
 * 
 * 
 * 那么 如何 构建一个树 最终得到一个vo？
 * 
 * 可以这么构建
 * 
 * 首先 将查询的数据封装起来 带到 一个集合 集合中的对象为 vo  vo中的list没有值
 * 
 * 首先根据级次分组  得到各组数据 
 * 将各组数据放到 map 集合中 key为级次
 * 
 * 然后循环如下:
 * 从第二级开始 汇总第一级  第三级 汇总 第二级.......
 * for(int i=map.size()-1-1;i>=0;i--){
 *    //得到下一级的数据
 *    list xl=map.get(i-1);
 *    //得到本级次的数据
 *    list sl=map.get(i);
 *    for(int i=0;i<sl.size();i++){
 *      for(int j=0;j<xl.size();j++){
 *         if(sl.get(i).getid().equal(xl.get(j).getpareantid())){
 *            sl.get(i).addChildren(xl.get(j));
 *         }
 *      }
 *    }
 * }
 *  map.get(0);//就是最终的构建树
 */
public class ReportRowColCrossTool {
	private static ReportItem[] m_crsResultValItems = null;
	// 构建二维表的复合列表头的表头数据
	private static FldgroupVO[] fldgroupVOs = null;
	// 过滤尺寸
	private static int fielterNum = 0;
	// 交叉列展开得初始位置
	private static int itemstart = 0;
	// 过滤级次
	private static int size = 0;
	// 保存多表头列的维度数组
	// 用于在数据交叉完成后
	// 利用该维度 构建 复合多表头列
	/**
	 * 报表数据交叉 构建二维表的工具类
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-12-15上午11:20:17
	 * @param strCrsRows
	 *            要交叉的行
	 * @param strCrsCols
	 *            要交叉的列
	 * @param strCrsVals
	 *            要交叉的值
	 * @throws Exception
	 */
	public static void onCross(ReportBaseUI ui, String[] strCrsRows,
			String[] strCrsCols, String[] strCrsVals) throws Exception {
		// 交叉行 的数据 要看作 一个合并的维度
		// 按交叉行的 联合字段的维度 进行 交叉列的数据合并操作
		if (strCrsRows == null || strCrsRows.length == 0)
			throw new Exception("交叉行不能为空");
		if (strCrsCols == null || strCrsCols.length == 0)
			throw new Exception("交叉列不能为空");
		if (strCrsVals == null || strCrsVals.length == 0)
			throw new Exception("交叉值不能为空");
		// 构建交叉行 联合维度
		StringBuffer comRows = new StringBuffer();
		for (int i = 0; i < strCrsRows.length; i++) {
			comRows.append(strCrsRows[i]);
			if (i != strCrsRows.length - 1) {
				comRows.append(" ");
			}
		}
		StringBuffer comCols = new StringBuffer();
		String[] cols = new String[strCrsCols.length + 1];
		for (int i = 0; i < strCrsCols.length; i++) {
			comCols.append(strCrsCols[i]);
			cols[i] = strCrsCols[i];
			if (i != strCrsCols.length - 1) {
				comCols.append(" ");
			}
		}
		cols[cols.length - 1] = "&type";
		fielterNum = strCrsVals.length;// 过滤维度
		size = strCrsCols.length;
		itemstart = strCrsRows.length;// 记录交叉列生成的初始位置
		String[] strows = new String[] { comRows.toString() };
		drawCrossTable(ui, strows,
				new String[] { comCols.toString(), "&type" }, strCrsVals);
	}
	
	
	/**
	 * 报表数据交叉 构建二维表的工具类
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-12-15上午11:20:17
	 * @param strCrsRows
	 *            要交叉的行
	 * @param strCrsCols
	 *            要交叉的列
	 * @param strCrsVals
	 *            要交叉的值
	 * @throws Exception
	 */
	public static void onCross(ReportBaseUI ui, String[] strCrsRows,
			String[] strCrsCols, String[] strCrsVals,int leavel) throws Exception {
		// 交叉行 的数据 要看作 一个合并的维度
		// 按交叉行的 联合字段的维度 进行 交叉列的数据合并操作
		if (strCrsRows == null || strCrsRows.length == 0)
			throw new Exception("交叉行不能为空");
		if (strCrsCols == null || strCrsCols.length == 0)
			throw new Exception("交叉列不能为空");
		if (strCrsVals == null || strCrsVals.length == 0)
			throw new Exception("交叉值不能为空");
		// 构建交叉行 联合维度
		StringBuffer comRows = new StringBuffer();
		for (int i = 0; i < strCrsRows.length; i++) {
			comRows.append(strCrsRows[i]);
			if (i != strCrsRows.length - 1) {
				comRows.append(" ");
			}
		}
		StringBuffer comCols = new StringBuffer();
		String[] cols = new String[strCrsCols.length + 1];
		for (int i = 0; i < strCrsCols.length; i++) {
			comCols.append(strCrsCols[i]);
			cols[i] = strCrsCols[i];
			if (i != strCrsCols.length - 1) {
				comCols.append(" ");
			}
		}
		cols[cols.length - 1] = "&type";
		fielterNum = strCrsVals.length;// 过滤维度
		size = strCrsCols.length;
		itemstart = strCrsRows.length;// 记录交叉列生成的初始位置
		String[] strows = new String[] { comRows.toString() };
		drawCrossTable(ui, strows,
				new String[] { comCols.toString(), "&type" }, strCrsVals,leavel);
	}


	private static void drawCrossTable(ReportBaseUI ui, String[] strCrsRows,
			String[] strCrsCols, String[] strCrsVals, int leavel) throws Exception {

		// 构造内存结果集元数据
		MemoryResultSetMetaData mrsmd = getReportGeneralUtil(ui)
				.createMeteData();
		// 构造内存结果集
		Vector dataVec = ui.getReportBase().getBillModel().getDataVector();
		MemoryResultSet mrsOrg = getReportGeneralUtil(ui).vector2Mrs(dataVec,
				mrsmd);
		ZmCrossTable ct = new ZmCrossTable();
		ct.setStrSp("_");
		ct.setMrsOrg(mrsOrg);
		ct.setCrsRows(strCrsRows);
		ct.setCrsCols(strCrsCols);
		ct.setCrsVals(strCrsVals);
		ReportItem[] risOrg = ui.getReportBase().getBody_Items();
		String[] strMrsOrgDisNames = new String[risOrg.length];
		for (int i = 0; i < risOrg.length; i++)
			strMrsOrgDisNames[i] = risOrg[i].getName();
		ct.setMrsOrgColDisNames(strMrsOrgDisNames);
		MemoryResultSet mrsCrs = ct.getCrossTableMrs();
		ReportItem[] risNew = getCrossBody_Items(ui, strCrsRows.length, mrsCrs);
		FldgroupVO[] fgvos = ct.getCrossTableFldGrpVOs();
		if (fgvos != null)
			// ui.getReportBase().setFieldGroup(fgvos);
			ui.getReportBase().setBody_Items(risNew);
		Vector vecBodyDataVec = getReportGeneralUtil(ui).mrs2Vector(mrsCrs);
		ui.getReportBase().getBillModel().setDataVector(vecBodyDataVec);
		//增加横向合计
		// onTotal(ui,strCrsRows.length-1,vecBodyDataVec);
		

			try {
				createItem2(risNew, ui, false, leavel, vecBodyDataVec);
				ui.getReportBase().getBillModel().setDataVector(vecBodyDataVec);

				// ui.getReportBase().getBillModel().execLoadFormula();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		ui.getReportBase().getBillModel().updateValue();
		// ui.getReportBase().getBillModel().getBillModelData();
		ReportBaseVO[] vos = new ReportBaseVO[ui.getReportBase().getBillModel()
				.getRowCount()];
		for (int i = 0; i < vos.length; i++) {
			vos[i] = new ReportBaseVO();
		}

		ui.getReportBase().getBillModel().getBodyValueVOs(vos);

		ui.getBodyDataVO();
		ui.setBodyDataVO(vos, true);
		
//		// onTotal(ui,5);
//		createItem(fgvos, ui, false);
//		ui.getBodyDataVO();
		// ui.getReportBase().getBillModel().execLoadFormula();		
	}


	/**
	 * × 报表交叉函数。须传入交叉行，交叉列，及交叉值数组
	 * 
	 * @param rows
	 *            java.lang.String[]
	 * @param columns
	 *            java.lang.String[]
	 */
	protected static void drawCrossTable(ReportBaseUI ui, String[] strCrsRows,
			String[] strCrsCols, String[] strCrsVals) throws Exception {
		// 构造内存结果集元数据
		MemoryResultSetMetaData mrsmd = getReportGeneralUtil(ui)
				.createMeteData();
		// 构造内存结果集
		Vector dataVec = ui.getReportBase().getBillModel().getDataVector();
		MemoryResultSet mrsOrg = getReportGeneralUtil(ui).vector2Mrs(dataVec,
				mrsmd);
		ZmCrossTable ct = new ZmCrossTable();
		ct.setStrSp("_");
		ct.setMrsOrg(mrsOrg);
		ct.setCrsRows(strCrsRows);
		ct.setCrsCols(strCrsCols);
		ct.setCrsVals(strCrsVals);
		ReportItem[] risOrg = ui.getReportBase().getBody_Items();
		String[] strMrsOrgDisNames = new String[risOrg.length];
		for (int i = 0; i < risOrg.length; i++)
			strMrsOrgDisNames[i] = risOrg[i].getName();
		ct.setMrsOrgColDisNames(strMrsOrgDisNames);
		MemoryResultSet mrsCrs = ct.getCrossTableMrs();
		ReportItem[] risNew = getCrossBody_Items(ui, strCrsRows.length, mrsCrs);
		FldgroupVO[] fgvos = ct.getCrossTableFldGrpVOs();
		if (fgvos != null)
			// ui.getReportBase().setFieldGroup(fgvos);
			ui.getReportBase().setBody_Items(risNew);
		Vector vecBodyDataVec = getReportGeneralUtil(ui).mrs2Vector(mrsCrs);
		ui.getReportBase().getBillModel().setDataVector(vecBodyDataVec);
		// onTotal(ui,5);
		createItem(fgvos, ui, false);
		ui.getBodyDataVO();
		// ui.getReportBase().getBillModel().execLoadFormula();
	}

	/**	 
	   不支持合计 构建交叉表的复合多表头 主要用于 交叉列有多列的情况下 构建 复合多表头 该方法 主要采用 数据结构中的 递归运算的的方式 来构建
	 * 多表头序列 比较难以看懂 和 维护 查询引擎当中 有构建复合列表头 的方法 但是 那个方法构建的复合列表头 如果存在多个交叉列的情况下 会导致
	 * 构建的复合列表头的数据出错 ，即构建的复合列表头的item 是完全错误的 所以 ： 写了本方法用于构建复合列表头数据
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-12-16下午03:54:16
	 * @param fgvos
	 * @param ui
	 */
	private static void createItem(FldgroupVO[] fgvos, ReportBaseUI ui,
			boolean istotal) {
		if (fgvos == null || fgvos.length == 0)
			return;
		UITable cardTable = ui.getReportBase().getBillTable();
		GroupableTableHeader cardHeader = (GroupableTableHeader) cardTable
				.getTableHeader();
		TableColumnModel cardTcm = cardTable.getColumnModel();
		// 记录
		List<ZmColumnGroup> list = new ArrayList<ZmColumnGroup>();
		int start = itemstart;
		int cossize = fielterNum;
		int size = 0;// 记录多表头的个数
		FldgroupVO[] fgvos1 = filter(fgvos, cossize - 1);

		if (istotal == false) {
			for (int i = 0; i < fgvos1.length; i++) {
				String[] cbs = fgvos1[i].getGroupname().split("ˉ");
				size = cbs.length;
				String colName = "null";
				String name = "null";
				if (size != 0) {
					colName = cbs[cbs.length - 1];
					for (int k = 0; k < cbs.length - 1; k++) {
						name = name + cbs[k];
					}
				}
				ZmColumnGroup cp = new ZmColumnGroup(colName);
				cp.setParentName(name);
				cp.setName(name + colName);
				for (int j = start; j < start + fielterNum; j++) {
					cp.add(cardTcm.getColumn(j));
				}
				start = start + fielterNum;
				list.add(cp);
			}
		} else {
			for (int i = 0; i < fgvos1.length; i++) {
				String[] cbs = fgvos1[i].getGroupname().split("ˉ");
				size = cbs.length;
				String colName = cbs[cbs.length - 1];
				String name = "";
				for (int k = 0; k < cbs.length - 1; k++) {
					name = name + cbs[k];
				}
				ZmColumnGroup cp = new ZmColumnGroup(colName);
				cp.setParentName(name);
				cp.setName(name + colName);
				for (int j = start; j < start + fielterNum; j++) {
					String st = ui.getReportBase().getBillModel()
							.getColumnName(j);
					if ("合计".equals(st)) {
						continue;
					}
					cp.add(cardTcm.getColumn(j));
				}
				start = start + fielterNum;
				list.add(cp);
			}
		}
		Map<Integer, List<ZmColumnGroup>> map = new HashMap<Integer, List<ZmColumnGroup>>();
		map.put(size - 2, list);
		// 开始递归运算 构建复合列表头
		for (int x = size - 2; x >= 0; x--) {
			// /////////////////
			List<ZmColumnGroup> list1 = map.get(x);
			List<ZmColumnGroup> flist = new ArrayList<ZmColumnGroup>();
			for (int i = 0; i < fgvos1.length; i++) {
				String[] cbs = fgvos1[i].getGroupname().split("ˉ");
				String name = "null";
				String sname = "null";
				if (cbs.length != 0) {
					name = cbs[x];
				}
				for (int k = 0; k < x; k++) {
					sname = sname + cbs[k];
				}
				if (!isExist(sname + name, flist)) {
					ZmColumnGroup cp = new ZmColumnGroup(name);
					cp.setParentName(sname);
					cp.setName(sname + name);
					flist.add(cp);
				}
			}
			for (int i = 0; i < flist.size(); i++) {
				String name = flist.get(i).getName();
				for (int j = 0; j < list1.size(); j++) {
					if (name.equals(list1.get(j).getParentName())) {
						flist.get(i).add(list1.get(j));
					}
				}
			}
			map.put(x - 1, flist);
		}
		List<ZmColumnGroup> flist1 = new ArrayList<ZmColumnGroup>();
		flist1 = map.get(-1);
		for (int i = 0; i < flist1.size(); i++) {
			cardHeader.addColumnGroup(flist1.get(i));
		}
		ui.getReportBase().getBillModel().updateValue();
	}

	private static boolean isExist(String name, List<ZmColumnGroup> flist) {
		if (name == null) {
			return false;
		}
		if (flist == null || flist.size() == 0)
			return false;
		boolean isExist = false;
		for (int i = 0; i < flist.size(); i++) {
			if (flist.get(i).getName().equals(name)) {
				isExist = true;
				break;
			}
		}
		return isExist;
	}
	/**
	 * 按尺寸过滤数据
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-12-16上午09:17:45
	 * @param fgvos
	 * @param cossize
	 * @return
	 */
	private static FldgroupVO[] filter(FldgroupVO[] fgvos, int cossize) {
		if (fgvos == null || fgvos.length == 0)
			return null;
		if (cossize == 0)
			cossize = 1;
		List<FldgroupVO> list = new ArrayList<FldgroupVO>();
		for (int i = 0; i < fgvos.length; i = i + cossize) {
			list.add(fgvos[i]);
		}
		return list.toArray(new FldgroupVO[0]);
	}

	protected static ReportGeneralUtil getReportGeneralUtil(ReportBaseUI ui) {
		return ui.getReportBase().getReportGeneralUtil();
	}

	/**
	 * 通过结果集 获取表体item 创建日期:(2002-11-5 17:34:05)
	 * @author mlr 来自查询引擎 单查询引擎 有错误 本方法已经修正
	 * @return nc.ui.pub.report.ReportItem[]
	 */
	protected static ReportItem[] getCrossBody_Items(int lenRows,
			MemoryResultSet mrsCrs, ReportItem[] items) throws Exception {
		MemoryResultSetMetaData mrsmd = mrsCrs.getMetaData0();
		ReportItem[] riCrs = new ReportItem[mrsmd.getColumnCount()];
		Vector vecResultValueItem = new Vector();
		for (int i = 0; i < mrsmd.getColumnCount(); i++) {
			ReportItem ri = new ReportItem();
			String strKey = mrsmd.getColumnName(i + 1);
			if (i < lenRows) {
				ri.setKey(strKey);
			} else {
				StringTokenizer st = new StringTokenizer(strKey, "_");
				StringBuffer sb = new StringBuffer();
				while (st.hasMoreElements()) {
					String letter = StringUtil.getPYIndexStr(st.nextElement()
							.toString(), true);
					sb.append(letter);
					sb.append("_");
				}
				ri.setKey(sb.toString());
				ri.setNote(strKey);
				vecResultValueItem.add(ri);
			}
			ri.setDataType(sqlType2itemType(mrsmd.getColumnType(i + 1)));
			ReportItem riOrg = getBody_Item(strKey, items);
			if (riOrg != null) {
				ri.setName(riOrg.getName());
				ri.setDataType(riOrg.getDataType());
			} else if (strKey.equals("&type")) {
				ri.setName("");
			} else if (strKey.equals("value")) {
				ri.setName("");
			} else {
				int index = strKey.lastIndexOf("_");
				ri.setName(strKey.substring(index + 1));
				ri.setDataType(IBillItem.DECIMAL);
			}
			ri.setWidth(80);
			riCrs[i] = ri;
		}
		m_crsResultValItems = (ReportItem[]) vecResultValueItem
				.toArray(new ReportItem[0]);
		return riCrs;
	}

	/**
	 * 此处插入方法说明. 创建日期:(2002-12-19 10:32:10)
	 * @return int
	 * @param isqlType
	 */
	public static int sqlType2itemType(int isqlType) {
		int iitemType = BillItem.STRING;
		switch (isqlType) {
		case Types.INTEGER: {
			iitemType = BillItem.INTEGER;
			break;
		}
		case Types.DECIMAL: {
			iitemType = BillItem.DECIMAL;
			break;
		}
		case Types.DOUBLE: {
			iitemType = BillItem.DECIMAL;
			break;
		}
		case Types.DATE: {
			iitemType = BillItem.DATE;
			break;
		}
		case Types.TIME: {
			iitemType = BillItem.TIME;
			break;
		}
		}
		return iitemType;
	}

	/**
	 * 此处插入方法说明.
	 * 
	 * @return nc.ui.pub.report.ReportItem
	 * @param key
	 */
	public static ReportItem getBody_Item(String key, ReportItem[] items) {
		BillItem[] bis = items;
		if (bis == null)
			return null;
		else {
			ReportItem[] riAlls = new ReportItem[bis.length];
			for (int i = 0; i < bis.length; i++)
				riAlls[i] = (ReportItem) bis[i];
			int index = -1;
			for (int i = 0; i < riAlls.length; i++)
				if (riAlls[i].getKey().equals(key)) {
					index = i;
					break;
				}
			if (index == -1)
				return null;
			else
				return riAlls[index];
		}
	}

	/**
	 * 通过结果集 获取表体item 创建日期:(2002-11-5 17:34:05)
	 * @author mlr 来自查询引擎 单查询引擎 有错误 本方法已经修正
	 * @return nc.ui.pub.report.ReportItem[]
	 */
	protected static ReportItem[] getCrossBody_Items(ReportBaseUI ui,
			int lenRows, MemoryResultSet mrsCrs) throws Exception {
		MemoryResultSetMetaData mrsmd = mrsCrs.getMetaData0();
		ReportItem[] riCrs = new ReportItem[mrsmd.getColumnCount()];
		Vector vecResultValueItem = new Vector();
		for (int i = 0; i < mrsmd.getColumnCount(); i++) {
			ReportItem ri = new ReportItem();
			String strKey = mrsmd.getColumnName(i + 1);
			if (i < lenRows) {
				ri.setKey(strKey);
			} else {
				StringTokenizer st = new StringTokenizer(strKey, "_");
				StringBuffer sb = new StringBuffer();
				while (st.hasMoreElements()) {
					String letter = StringUtil.getPYIndexStr(st.nextElement()
							.toString(), true);
					sb.append(letter);
					sb.append("_");
				}
				ri.setKey(sb.toString());
				ri.setNote(strKey);
				vecResultValueItem.add(ri);
			}

			ri.setDataType(getReportGeneralUtil(ui).sqlType2itemType(
					mrsmd.getColumnType(i + 1)));
			ReportItem riOrg = ui.getReportBase().getBody_Item(strKey);
			if (riOrg != null) {
				ri.setName(riOrg.getName());
				ri.setDataType(riOrg.getDataType());
			} else if (strKey.equals("&type")) {
				ri.setName("");
			} else if (strKey.equals("value")) {
				ri.setName("");
			} else {
				int index = strKey.lastIndexOf("_");
				ri.setName(strKey.substring(index + 1));
				ri.setDataType(BillItem.DECIMAL);
			}
			ri.setWidth(80);
			riCrs[i] = ri;
		}
		m_crsResultValItems = (ReportItem[]) vecResultValueItem
				.toArray(new ReportItem[0]);
		return riCrs;
	}

	private class Item {
		private String code = null;
	}

	public static void onCross(ReportBaseUI ui, MemoryResultSetMetaData mrsmd,
			Vector vc, ReportItem[] risOrg, String[] strCrsRows,
			String[] strCrsCols, String[] strCrsVals, boolean istotal,
			int leaval) throws Exception {
		// 交叉行 的数据 要看作 一个合并的维度
		// 按交叉行的 联合字段的维度 进行 交叉列的数据合并操作
		if (strCrsRows == null || strCrsRows.length == 0)
			throw new Exception("交叉行不能为空");
		if (strCrsCols == null || strCrsCols.length == 0)
			throw new Exception("交叉列不能为空");
		if (strCrsVals == null || strCrsVals.length == 0)
			throw new Exception("交叉值不能为空");
		// 构建交叉行 联合维度
		StringBuffer comRows = new StringBuffer();
		for (int i = 0; i < strCrsRows.length; i++) {
			comRows.append(strCrsRows[i]);
			if (i != strCrsRows.length - 1) {
				comRows.append(" ");
			}
		}
		StringBuffer comCols = new StringBuffer();
		String[] cols = new String[strCrsCols.length + 1];
		for (int i = 0; i < strCrsCols.length; i++) {
			comCols.append(strCrsCols[i]);
			cols[i] = strCrsCols[i];
			if (i != strCrsCols.length - 1) {
				comCols.append(" ");
			}
		}
		cols[cols.length - 1] = "&type";
		fielterNum = strCrsVals.length;// 过滤维度
		itemstart = strCrsRows.length;// 记录交叉列生成的初始位置
		size = strCrsCols.length;
		String[] strows = new String[] { comRows.toString() };
		drawCrossTable(ui, mrsmd, vc, risOrg, strows, new String[] {
				comCols.toString(), "&type" }, strCrsVals, istotal, leaval);
	}

	/**
	 * 将表体向量转换为内存结果集
	 * @return nc.vo.pub.cquery.QryResultVO[]
	 * @param headerdata
	 */
	public static MemoryResultSet vector2Mrs(Vector vecData,
			MemoryResultSetMetaData mrsmd) {
		ArrayList al = new ArrayList();
		for (int i = 0; i < vecData.size(); i++) {
			Vector vecRow = (Vector) vecData.elementAt(i);
			ArrayList alRow = new ArrayList();
			for (int j = 0; j < vecRow.size(); j++)
				alRow.add(vecRow.elementAt(j));
			al.add(alRow);
		}
		MemoryResultSet mrs = new MemoryResultSet(al, mrsmd);
		return mrs;
	}

	/**
	 * 将内存结果集转换为表体向量
	 * @return nc.vo.pub.cquery.QryResultVO[]
	 * @param headerdata
	 */
	public static Vector mrs2Vector(MemoryResultSet rs, ReportItem[] items) {
		Vector v = new Vector();
		Object[] objFlds = null;
		Object objTemp = null;
		try {
			MemoryResultSetMetaData rsmd = rs.getMetaData0();
			int iColCount = rsmd.getColumnCount();
			// 获得每列的数据类型(主要因为象“销”这样的字通过getObject()读不出来,所以需要对字符型的字段使用getstring()方法)
			int[] iColTypes = new int[iColCount];
			for (int i = 0; i < iColCount; i++)
				iColTypes[i] = rsmd.getColumnType(i + 1);
			while (rs.next()) {
				objFlds = new Object[iColCount]; // 重要
				for (int i = 1; i <= iColCount; i++) {
					// 为辅助量小计合计特殊处理,辅助量小计合计的结果为ArrayList
					objTemp = rs.getObject(i);
					if (objTemp != null && objTemp instanceof ArrayList) {
						ArrayList valueList = (ArrayList) objTemp;
						BillItem bis = items[i - 1];
						// 格式化
						if (bis.getDataType() == BillItem.DECIMAL) {
							int digits = bis.getDecimalDigits();
							if (digits < 0)
								digits = -digits;
							for (int vi = 0, vn = valueList.size(); vi < vn; vi++) {
								Object o = valueList.get(vi);
								if (o instanceof BigDecimal) {
									valueList.set(vi, ((BigDecimal) o)
											.setScale(digits,
													BigDecimal.ROUND_HALF_UP));
								}
							}
						}
						objFlds[i - 1] = valueList;
						continue;
					}

					if (iColTypes[i - 1] == Types.CHAR
							|| iColTypes[i - 1] == Types.VARCHAR) {
						objTemp = rs.getString(i); // 不同数据库中的字符串类型字段是否仅这两种形式?
						String strColName = rsmd.getColumnName(i);
						ReportItem riCol = getBody_Item(strColName, items);

						if (riCol != null
								&& riCol.getDataType() == BillItem.BOOLEAN)
							objFlds[i - 1] = (objTemp == null) ? new UFBoolean(
									'N') : new UFBoolean(objTemp.toString()
									.trim());
						else
							objFlds[i - 1] = (objTemp == null) ? "" : objTemp
									.toString().trim();
					} else {
						objTemp = rs.getObject(i); // 如果直接用getString,则在读数字类型字段时会出错
						if (iColTypes[i - 1] == Types.DECIMAL
								|| iColTypes[i - 1] == Types.DOUBLE) {
							if (objTemp == null)
								objFlds[i - 1] = null;
							else {
								String strColName = rsmd.getColumnName(i);
								ReportItem riCol = getBody_Item(strColName,
										items);
								;
								if (riCol != null) {
									int iprecision = riCol.getDecimalDigits();
									objFlds[i - 1] = new UFDouble(objTemp
											.toString(), iprecision);
								} else
									objFlds[i - 1] = new UFDouble(objTemp
											.toString());
							}
						} else if (iColTypes[i - 1] == Types.INTEGER) {
							Integer iobj = null;
							if (objTemp == null)
								objFlds[i - 1] = null;
							else {
								String str = objTemp.toString();
								int indexpoint = str.indexOf(".");
								if (indexpoint != -1)
									iobj = new Integer(str.substring(0,
											indexpoint));
								else
									iobj = new Integer(str);
								objFlds[i - 1] = iobj;
							}
						} else
							objFlds[i - 1] = (objTemp == null) ? "" : objTemp;
					}
				}
				v.addElement(objFlds);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 设置表体信息
		Vector vecData = new Vector();
		for (int i = 0; i < v.size(); i++) {
			objFlds = (Object[]) v.elementAt(i);
			Vector vecTemp = new Vector();
			for (int j = 0; j < objFlds.length; j++)
				vecTemp.addElement(objFlds[j]);
			vecData.addElement(vecTemp);
		}
		return vecData;
	}

	/**
	 * × 报表交叉函数。须传入交叉行，交叉列，及交叉值数组
	 * @param leaval
	 * @param istotal
	 * @param rows
	 * @param columns
	 */
	protected static void drawCrossTable(ReportBaseUI ui,
			MemoryResultSetMetaData mrsmd, Vector vc, ReportItem[] risOrg,
			String[] strCrsRows, String[] strCrsCols, String[] strCrsVals,
			boolean istotal, int leaval) throws Exception {
		// 构造内存结果集元数据
		// MemoryResultSetMetaData mrsmd = getReportGeneralUtil(ui)
		// .createMeteData();
		// 构造内存结果集
		Vector dataVec = vc;
		MemoryResultSet mrsOrg = vector2Mrs(dataVec, mrsmd);
		ZmCrossTable ct = new ZmCrossTable();
		ct.setStrSp("_");
		ct.setMrsOrg(mrsOrg);
		ct.setCrsRows(strCrsRows);
		ct.setCrsCols(strCrsCols);
		ct.setCrsVals(strCrsVals);
		// ReportItem[] risOrg = ui.getReportBase().getBody_Items();
		String[] strMrsOrgDisNames = new String[risOrg.length];
		for (int i = 0; i < risOrg.length; i++)
			strMrsOrgDisNames[i] = risOrg[i].getName();
		ct.setMrsOrgColDisNames(strMrsOrgDisNames);
		MemoryResultSet mrsCrs = ct.getCrossTableMrs();
		ReportItem[] risNew = getCrossBody_Items(strCrsRows.length, mrsCrs,
				risOrg);
		FldgroupVO[] fgvos = ct.getCrossTableFldGrpVOs();
		if (fgvos != null)
			// ui.getReportBase().setFieldGroup(fgvos);
			// ui.getReportBase().getbo
			ui.getReportBase().setBody_Items(risNew);
		Vector vecBodyDataVec = mrs2Vector(mrsCrs, risOrg);
		// onTotal(ui,strCrsRows.length-1,vecBodyDataVec);
		if (istotal) {

			try {
				createItem2(risNew, ui, false, leaval, vecBodyDataVec);
				ui.getReportBase().getBillModel().setDataVector(vecBodyDataVec);

				// ui.getReportBase().getBillModel().execLoadFormula();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			createItem(fgvos, ui, istotal);
			ui.getReportBase().getBillModel().setDataVector(vecBodyDataVec);

		}
		ui.getReportBase().getBillModel().updateValue();
		// ui.getReportBase().getBillModel().getBillModelData();
		ReportBaseVO[] vos = new ReportBaseVO[ui.getReportBase().getBillModel()
				.getRowCount()];
		for (int i = 0; i < vos.length; i++) {
			vos[i] = new ReportBaseVO();
		}

		ui.getReportBase().getBillModel().getBodyValueVOs(vos);

		ui.getBodyDataVO();
		ui.setBodyDataVO(vos, true);
		// ui.getReportBase().getBillModel().execLoadFormula();

	}
	/**
	 * 支持横向合计的动态列创建
	 * 
	 * 支持合计 构建交叉表的复合多表头 主要用于 交叉列有多列的情况下 构建 复合多表头 该方法 主要采用 数据结构中的 递归运算的的方式 来构建
	 * 多表头序列 比较难以看懂 和 维护 查询引擎当中 有构建复合列表头 的方法 但是 那个方法构建的复合列表头 如果存在多个交叉列的情况下 会导致
	 * 构建的复合列表头的数据出错 ，即构建的复合列表头的item 是完全错误的 所以 ： 写了本方法用于构建复合列表头数据
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-12-16下午03:54:16
	 * @param fgvos
	 * @param ui
	 */
	private static void createItem2(ReportItem[] items, ReportBaseUI ui,
			boolean istotal, int totalleavl, Vector<Vector> vc) {
		if (items == null || items.length == 0)
			return;
		// 根据合计的级次 创建横向的合计列 items 并重新构建vc数据
		//vc是BillModel的一种数据结构  BillModel有Item 和 Vector 
		//Item 是UI控件   而Vector是控件对应的数据
		List<ReportItem> its = createTotalItem(items, totalleavl, ui, vc);
		// 得到复合列设置工具 该工具用于构建复合列表头
		TableColumnModel ct = getTableColumnModel(ui);
		items = its.toArray(new ReportItem[0]);
		// 构建动态复合列表头
		List<ZmColumnGroup> list = new ArrayList<ZmColumnGroup>();
		//构建第一级的复合列表头 构建的复合列表头数据放到 list中
		createItem1(list, items, ct, totalleavl);
		//用于存放 各个级次的复合列表头
		Map<Integer, List<ZmColumnGroup>> map = new HashMap<Integer, List<ZmColumnGroup>>();
		//将第一级的复合列表头放到map中
		map.put(size - 2, list);
		// 递归运算 创建 2级---最后 一级的复合列表头
		createItem12(map, items, ui, totalleavl);
	}

	private static void createItem12(Map<Integer, List<ZmColumnGroup>> map,
			ReportItem[] items, ReportBaseUI ui, int totalleavl) {
		// 开始递归运算 构建复合列表头
		for (int x = size - 2; x >= 0; x--) {
			List<ZmColumnGroup> list1 = map.get(x);
			List<ZmColumnGroup> flist = new ArrayList<ZmColumnGroup>();
			TableColumnModel ct = getTableColumnModel(ui);
			for (int i = itemstart; i < items.length; i++) {
				if (items[i].getNote() == null
						|| items[i].getNote().length() == 0)
					continue;
				String[] cbs = items[i].getNote().split("ˉ");
				String name = "";
				String sname = "";
				if (cbs.length != 0) {
					name = cbs[x];
				}
				for (int k = 0; k < x; k++) {
					sname = sname + cbs[k];
				}
				//查看当前索引是否 已经达到合计列的复合列表头的添加级次
				if (x == size - totalleavl) {
					//如果达到了 合计列的复合列的级次 那么就要创建合计列的父级
					if (ct.getColumn(i).getHeaderValue().equals("合计")) {
						ZmColumnGroup cp = new ZmColumnGroup(name);
						cp.setParentName(sname);
						cp.setName(sname + name);
						flist.add(cp);
						//将原始合计item向添加到复合列对象中
						cp.add(ct.getColumn(i));
					}
				} else {
					//如果没有达到合计的级次 那就按照正常的复合列创建方式
					//创建复合列
					if (!isExist(sname + name, flist)) {
						ZmColumnGroup cp = new ZmColumnGroup(name);
						cp.setParentName(sname);
						cp.setName(sname + name);
						flist.add(cp);
					}
				}
			}
			//进行上下级次的复合列的关联
			//即 新生产的复合列  要将他们的下级复合列 添加进来 类似树形结构
			//flist为上级复合列
			//list1为下级复合列 ParentName与对应的上级复合列的 name一样
			//所以通过这种关系 就可以构建 上下级之间的关系 即可以构建级次树
			for (int i = 0; i < flist.size(); i++) {
				String name = flist.get(i).getName();
				for (int j = 0; j < list1.size(); j++) {
					if (name.equals(list1.get(j).getParentName())) {
						ZmColumnGroup zm = list1.get(j);
						flist.get(i).add(list1.get(j));
					}
				}
			}
			map.put(x - 1, flist);
		}
		//用来存放最顶级的复合列
		//因为我们构建的是一个树形结构 所以只需取到最顶级的复合列集合就行了
		List<ZmColumnGroup> flist1 = new ArrayList<ZmColumnGroup>();
		flist1 = map.get(-1);//取最顶级的复合列
		//获得复合列ui设置工具
		UITable cardTable = ui.getReportBase().getBillTable();
		GroupableTableHeader cardHeader = (GroupableTableHeader) cardTable
				.getTableHeader();
		//将最顶级的复合列添加到ui中 
		for (int i = 0; i < flist1.size(); i++) {
			cardHeader.addColumnGroup(flist1.get(i));
		}

	}

	private static TableColumnModel getTableColumnModel(ReportBaseUI ui) {
		UITable cardTable = ui.getReportBase().getBillTable();
		GroupableTableHeader cardHeader = (GroupableTableHeader) cardTable
				.getTableHeader();
		TableColumnModel cardTcm = cardTable.getColumnModel();
		return cardTcm;
	}

	/**
	 * 该方法是重写构建 UI控件 即Item
	 * 为什么要重写构建Item呢？
	 * 是因为 要在原先item的基础之上 添加横向合计向  
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-1-5下午07:11:53
	 * @param items
	 * @param ui
	 * @param vc
	 */
	private static List<ReportItem> createTotalItem(ReportItem[] items,
			int totalleavl, ReportBaseUI ui, Vector<Vector> vc) {
		int size = ReportRowColCrossTool.size;
		List<ReportItem> its = new ArrayList<ReportItem>();//
		//将数据交叉后生产的 item 封装到一个List集合中
		for (int i = 0; i < items.length; i++) {
			its.add(items[i]);
		}
		//记录添加合计的位置
		List<String> totals = new ArrayList<String>();// 合计
		for (int i = itemstart; i < items.length; i = i + fielterNum) {
			if (items[i].getNote() == null || items[i].getNote().length() == 0)
				continue;
			String[] cbs = items[i].getNote().split("ˉ");
			size = cbs.length;
			String colName = "";
			String name = "";
			if (size != 0) {
				//获得指定级次的名字
				colName = cbs[cbs.length - totalleavl];
				for (int k = 0; k < cbs.length - totalleavl; k++) {
					name = name + cbs[k];
				}
			}
			//将对应级次的名字 添加到totals
			if (!isExist1(totals, name + colName)) {
				totals.add(name + colName);
			}
		}
		//数据交叉产生的动态列的第一个索引的位置
		int st = itemstart;
		try {
			//开始往  its 集合中添加合计列项
			int totalstart = itemstart;
			for (String key : totals) {
				boolean isEqual = true;
				for (int i = st; i < its.size(); i++) {
					String[] cbs = its.get(i).getNote().split("ˉ");
					String colName = "";
					String name = "";
					if (size != 0) {
						colName = cbs[cbs.length - totalleavl];
						for (int k = 0; k < cbs.length - totalleavl; k++) {
							name = name + cbs[k];
						}
					}
					if (key.equals(name + colName)) {
						isEqual = true;
					} else {
						isEqual = false;
					}
					if (isEqual == false && !its.get(i).getName().equals("合计")) {

						ReportItem item = ReportPubTool.getItem("num" + i,
								"合计", IBillItem.DECIMAL, i, 80);
						item.setNote(its.get(i - 1).getNote());
						its.add(i, item);//追加一个合计列
						createData(i, vc);//追击一个合计值 并设置为0						
						//给刚追击的item计算合计值
						//totalstart为计算合计值的初始索引
						//i-1为计算合计值的结束所以
						//并将从 索引位置 totalstart 开始  到i-1为止的数据求和 
						//加到索引位置为i处
						//即给该合计列 设置对应值
						caltotal(its, totalstart, i, vc);
						totalstart = i + 1;
						st = i + 1;
						break;
					}
				}
			}
			// 添加最后一行的合计
			if (its.size() > 0) {
				ReportItem item = ReportPubTool.getItem("num" + its.size(),
						"合计", IBillItem.DECIMAL, its.size(), 80);
				item.setNote(its.get(its.size() - 1).getNote());
				its.add(its.size(), item);
				createData(its.size() - 1, vc);
				caltotal(its, totalstart, its.size() - 1, vc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
        //因为BillModel的item已经发生了变化 所以要重写设置BillModel
		ui.getReportBase().setBody_Items(its.toArray(new ReportItem[0]));
		return its;
	}

	/**
	 * 计算合计值 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-1-6下午07:49:49
	 * @param its
	 * @param vc
	 * @param totalstart
	 * @param i
	 */
	private static void caltotal(List<ReportItem> its, int startindex,
			int endindex, Vector<Vector> vc) {
		if (vc == null || vc.size() == 0)
			return;
		for (int j = 0; j < vc.size(); j++) {
			UFDouble uf = new UFDouble(0.0);
			Vector vt = vc.get(j);
			for (int i = startindex; i < endindex; i++) {
				uf = uf.add(PuPubVO.getUFDouble_NullAsZero(vt.get(i)));
			}
			vt.set(endindex, uf);
		}
	}

	/**
	 * 由于创建的合计公式在执行时 出现类型转换异常
	 * 所以 暂且不再用此方法
	 * 创建合计公式
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-1-6下午12:47:23
	 * @param its
	 * @param itemstart2
	 * @param i
	 */
	private static void createFormular(List<ReportItem> its, int itemstart2,
			int index) {
		String formu = " num" + (index) + "-> ";
		for (int i = itemstart2; i < index; i++) {
			formu = formu + its.get(i).getKey() + "+";
		}
		its.get(index).setLoadFormula(
				new String[] { formu.substring(0, formu.length() - 1) });
	}

	/**
	 * 指定索引出添加一个合计值 初始值 为 0
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-1-6下午12:13:47
	 * @param i
	 * @param vc
	 */
	private static void createData(int index, Vector<Vector> vc) {
		if (vc == null || vc.size() == 0)
			return;
		for (int i = 0; i < vc.size(); i++) {
			vc.get(i).add(index, new UFDouble(0.0));
		}
	}

	/**
	 *  创建第一级的复合列
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-1-5下午07:10:02
	 * @param list
	 * @param items
	 * @param cardTcm
	 */
	private static void createItem1(List<ZmColumnGroup> list,
			ReportItem[] items, TableColumnModel cardTcm, int leaval) {
		//复合列表头共有多少级
		int size = ReportRowColCrossTool.size;
		//数据交叉产生的动态列的 第一个索引的位置  即开始构建复合列表头的初始位置
		int start = itemstart;
		for (int i = itemstart; i < items.length; i++) {
			if (items[i].getNote() == null || items[i].getNote().length() == 0)
				continue;

			String[] cbs = items[i].getNote().split("ˉ");
			size = cbs.length;
			String colName = "";
			String name = "";
			if (size != 0) {
				colName = cbs[cbs.length - 1];
				for (int k = 0; k < cbs.length - 1; k++) {
					name = name + cbs[k];
				}
			}
			//创建一个新的复合列表头的对象
			ZmColumnGroup cp = new ZmColumnGroup(colName.split("_")[0]);
			//设置该对象上一级的名字
			cp.setParentName(name);
			//设置该对象的名字
			cp.setName(name + colName);
			boolean istotal = false;
			for (int j = start; j < start + fielterNum; j++) {
				cp.add(cardTcm.getColumn(j));
				if (cardTcm.getColumn(j).getHeaderValue().equals("合计")) {
					istotal = true;
				}
			}
			start = start + fielterNum;
			//只对不是合计列的item 创建一级复合列 
			//但是 如果合计列的添加级次是第一级的话 
			//那么 当然要给合计列 创建第一级的复合列
			if (istotal == false || leaval == 1)
				list.add(cp);
		}
	}

	private static boolean isExist1(List<String> totals, String string) {
		boolean isE = false;
		for (int i = 0; i < totals.size(); i++) {
			if (string.equals(totals.get(i))) {
				isE = true;
			} else {
				isE = false;
			}
		}
		return isE;
	}

}

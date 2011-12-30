package nc.ui.wds.pub.report2;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import nc.vo.pub.cquery.FldgroupVO;
import nc.vo.pub.rs.MemoryResultSet;
import nc.vo.pub.rs.MemoryResultSetMetaData;
import nc.vo.pub.util1.MutilWeiClass;

/**
 * 数据交叉表 用于动态构建二维表 取自供应链的查询引擎 但供应链的查询引擎 对存在多个交叉列的 数据交叉 会导致数据混乱 所以 重写了本类 用来支持
 * 本类实现了： 存在多个交叉列的数据交叉 的实现
 * 
 * @author mlr 2011-12-16 modify
 */
public class ZmCrossTable {
	// zjb+
	public static final String CRS_DELIM = "ˉ";

	private static final String CRS_VALUE_COLUMN = "&type";

	private String[] m_crsCols = null;

	private String[] m_crsRows = null;

	private String[] m_crsSumVals = null;

	private String[] m_crsVals = null;

	private boolean m_isTypeAtCol = false;

	private int m_locType = 0;

	private nc.vo.pub.rs.MemoryResultSet m_mrsOrg = null;

	private String[] m_mrsOrgColDisNames = null;

	private MutilWeiClass m_mwc = null;

	private String m_strSp = CRS_DELIM;

	// lyyuan+
	private Comparator m_userDefComparator = null;

	public final static Comparator DEFAULTCOMPARATOR = new java.util.Comparator() {
		public int compare(Object o1, Object o2) {
			if (o1 instanceof Integer || o2 instanceof Integer)
				return ((Integer) o1).compareTo((Integer) o2);
			else if (o1 instanceof Double || o2 instanceof Double)
				return ((Double) o1).compareTo((Double) o2);
			else if (o1 instanceof Short || o2 instanceof Short)
				return ((Short) o1).compareTo((Short) o2);
			else if (o1 instanceof java.math.BigDecimal
					|| o2 instanceof java.math.BigDecimal)
				return ((java.math.BigDecimal) o1)
						.compareTo((java.math.BigDecimal) o2);
			else
				return (o1.toString()).compareTo(o2.toString());
		}
	};

	/**
	 * CrossTable 构造子注解。
	 */
	public ZmCrossTable() {
		super();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-12 13:31:15)
	 * 
	 * @return nc.vo.pub.rs.MemoryResultSet
	 * @param objs
	 *            java.lang.Object[][]
	 */
	private MemoryResultSet arrayToMrs(Object[][] objs,
			MemoryResultSetMetaData mrsmdNew) throws Exception {
		int iTypeAtCol = 0;
		String[] strRows = splitFuHeRows(getCrsRows());
		String[] strCols = splitFuHeRows(getCrsCols());
		int iTypeColNo = 0;
		for (int i = 0; i < strRows.length; i++)
			if (strRows[i].equals("&type")) {
				iTypeAtCol = -1;
				iTypeColNo = i;
				break;
			}
		for (int i = 0; i < strCols.length; i++)
			if (strCols[i].equals("&type")) {
				iTypeAtCol = 1;
				break;
			}
		Vector vIsStringType = new Vector();
		String[] strVals = getCrsVals();
		String[] strMrsOrgDisNames = getMrsOrgDisNames();
		MemoryResultSetMetaData mrsmdOrg = getMrsOrg().getMetaData0();
		for (int i = 0; i < strVals.length; i++) {
			int index = mrsmdOrg.getNameIndex(strVals[i]);
			if (mrsmdOrg.getColumnType(index + 1) == Types.VARCHAR
					|| mrsmdOrg.getColumnType(index + 1) == Types.CHAR) {
				vIsStringType.add(strMrsOrgDisNames[index]);
			}
		}

		// 开始分拆
		int iStart = splitFuHeRows(getCrsRows()).length;
		// &type在列
		if (iTypeAtCol == 1)
			for (int i = iStart; i < mrsmdNew.getColumnCount(); i++) {
				if (mrsmdNew.getColumnType(i + 1) == Types.DOUBLE
						|| mrsmdNew.getColumnType(i + 1) == Types.DECIMAL
						|| mrsmdNew.getColumnType(i + 1) == Types.INTEGER
						|| mrsmdNew.getColumnType(i + 1) == Types.BIGINT
						|| mrsmdNew.getColumnType(i + 1) == Types.SMALLINT
						|| mrsmdNew.getColumnType(i + 1) == Types.FLOAT
						|| mrsmdNew.getColumnType(i + 1) == Types.REAL
						// oracle数据库
						|| mrsmdNew.getColumnType(i + 1) == Types.NUMERIC) {
					for (int j = 0; j < objs.length; j++) {
						// if (i == 17)
						// System.out.println();
						if (objs[j][i] != null) {
							// if (i == 17)
							// System.out.println();
							StringTokenizer st = new StringTokenizer(objs[j][i]
									.toString(), " ");
							double d = 0;
							while (st.hasMoreElements()) {
								// d +=
								// Double.parseDouble(st.nextElement().toString());
								String strTemp = st.nextToken();
								d += (strTemp == null || strTemp.equals("null")) ? 0
										: Double.parseDouble(strTemp);
							}
							if (mrsmdNew.getColumnType(i + 1) == Types.DOUBLE
									|| mrsmdNew.getColumnType(i + 1) == Types.DECIMAL
									|| mrsmdNew.getColumnType(i + 1) == Types.FLOAT
									|| mrsmdNew.getColumnType(i + 1) == Types.REAL
									|| mrsmdNew.getColumnType(i + 1) == Types.NUMERIC
									&& mrsmdNew.getScale(i + 1) != 0)
								objs[j][i] = new Double(d);
							else {
								objs[j][i] = new Integer(new Double(d)
										.intValue());
							}
						}
					}
				} else {
					for (int j = 0; j < objs.length; j++)
						if (objs[j][i] != null) {
							StringTokenizer st = new StringTokenizer(objs[j][i]
									.toString(), " ");
							// Set set = new LinkedHashSet();
							ArrayList al = new ArrayList();
							while (st.hasMoreTokens()) {
								// set.add(st.nextToken());
								al.add(st.nextToken());
							}
							StringBuffer sb = new StringBuffer();
							// Iterator iter = set.iterator();
							Iterator iter = al.iterator();
							while (iter.hasNext()) {
								sb.append(iter.next());
								sb.append(" ");
							}
							objs[j][i] = sb.toString();
						}
				}
			}

		// &type在行：分两种情况
		else if (iTypeAtCol == -1) {
			// a Vals不含字串，全部按Double
			if (vIsStringType.size() == 0) {
				for (int i = iStart; i < mrsmdNew.getColumnCount(); i++)
					for (int j = 0; j < objs.length; j++) {
						if (objs[j][i] != null) {
							StringTokenizer st = new StringTokenizer(objs[j][i]
									.toString(), " ");
							double d = 0;
							while (st.hasMoreTokens()) {
								// d += Double.parseDouble(st.nextToken());
								String strTemp = st.nextToken();
								d += (strTemp == null || strTemp.equals("null")) ? 0
										: Double.parseDouble(strTemp);
							}
							objs[j][i] = new Double(d);
						}
					}
			} else {

				// b 含字串，按行作。
				for (int i = 0; i < objs.length; i++) {
					if (!vIsStringType.contains(objs[i][iTypeColNo].toString()))
						for (int j = iStart; j < mrsmdNew.getColumnCount(); j++) {
							if (objs[i][j] != null) {
								StringTokenizer st = new StringTokenizer(
										objs[i][j].toString(), " ");
								double d = 0;
								while (st.hasMoreTokens()) {
									d += Double.parseDouble(st.nextToken());
								}
								objs[i][j] = new Double(d);
							}
						}
					else {
						for (int j = iStart; j < mrsmdNew.getColumnCount(); j++) {
							if (objs[i][j] != null) {
								StringTokenizer st = new StringTokenizer(
										objs[i][j].toString(), " ");
								// Set set = new LinkedHashSet();
								ArrayList al = new ArrayList();
								while (st.hasMoreTokens()) {
									// set.add(st.nextToken());
									al.add(st.nextToken());
								}
								StringBuffer sb = new StringBuffer();
								// Iterator iter = set.iterator();
								Iterator iter = al.iterator();
								while (iter.hasNext()) {
									sb.append(iter.next());
									sb.append(" ");
								}
								objs[i][j] = sb.toString();
							}
						}

					}
				}
			}
		} else {

			// &type不在行，也不在列，全部按Double作
			for (int i = 0; i < objs.length; i++) {
				for (int j = iStart; j < mrsmdNew.getColumnCount(); j++) {
					if (objs[i][j] != null) {
						StringTokenizer st = new StringTokenizer(objs[i][j]
								.toString(), " ");
						double d = 0;
						while (st.hasMoreTokens()) {
							// d += Double.parseDouble(st.nextToken());
							String strTemp = st.nextToken();
							d += (strTemp == null || strTemp.equals("null")) ? 0
									: Double.parseDouble(strTemp);
						}
						objs[i][j] = new Double(d);
					}
				}
			}
		}
		MemoryResultSet mrs = null;
		if (objs != null) {
			ArrayList al = new ArrayList();
			for (int i = 0; i < objs.length; i++) {
				ArrayList alRow = new ArrayList();
				for (int j = 0; j < objs[0].length; j++)
					alRow.add(objs[i][j]);
				al.add(alRow);
			}
			mrs = new MemoryResultSet(al, mrsmdNew);
		}
		return mrs;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-12 15:38:15)
	 * 
	 * @return nc.vo.pub.rs.MemoryResultSetMetaData
	 */
	private MemoryResultSetMetaData changeCrossTableMrsmd(int iColCount,
			MutilWeiClass mwc) throws Exception {
		String[] strRows = splitFuHeRows(getCrsRows());
		MemoryResultSetMetaData mrsmdOrg = getMrsOrg().getMetaData0();
		MemoryResultSetMetaData mrsmdCrsOrg = getCrsMemoryResultSet()
				.getMetaData0();
		ArrayList al = new ArrayList();
		int[] iDataType = new int[iColCount];
		for (int i = 0; i < strRows.length; i++) {
			al.add(strRows[i]);
			int index = mrsmdCrsOrg.getNameIndex(strRows[i]);
			iDataType[i] = mrsmdCrsOrg.getColumnType(index + 1);
		}
		ArrayList[] lists = mwc.getAlColVal();
		int iLength = iColCount - strRows.length;
		StringBuffer[] sbKeys = new StringBuffer[iLength];
		for (int i = getCrsRows().length; i < lists.length; i++) {
			int nAlColVal = 1;
			if (i != lists.length - 1) {
				nAlColVal = lists[i + 1].size();
				for (int k = i + 2; k < lists.length; k++)
					nAlColVal *= lists[k].size();
			}
			for (int j = 0; j < iLength; j++) {
				String str = lists[i].get((j / nAlColVal) % lists[i].size())
						.toString();
				if (sbKeys[j] == null) {
					sbKeys[j] = new StringBuffer();
					sbKeys[j].append(str);
				} else {
					sbKeys[j].append(m_strSp); // zjb改
					// sbKeys[j].append(".");
					sbKeys[j].append(str);
				}
			}
		}
		if (getCrsCols() == null || getCrsCols().length == 0)
			sbKeys[0] = new StringBuffer("value");

		String[] strVals = getCrsVals();
		boolean bhasStringType = false;
		for (int i = 0; i < strVals.length; i++) {
			int indexValName = mrsmdOrg.getNameIndex(strVals[i]);
			if (mrsmdOrg.getColumnType(indexValName + 1) == Types.VARCHAR
					|| mrsmdOrg.getColumnType(indexValName + 1) == Types.CHAR) {
				bhasStringType = true;
				break;
			}
		}
		// modify mlr 改正纵向的复合维度的合并 无法合并的bug
		String[] strCols = getCrsCols();
		int iTypeLoc = -1;
		for (int i = 0; i < strCols.length; i++)
			if (strCols[i].equals("&type")) {
				iTypeLoc = i;
				break;
			}
		int iRowTypeLoc = -1;
		for (int i = 0; i < strRows.length; i++)
			if (strRows[i].equals("&type")) {
				iRowTypeLoc = i;
				break;
			}
		String[] strMrsOrgDisName = getMrsOrgDisNames();
		for (int i = 0; i < sbKeys.length; i++) {
			al.add(sbKeys[i].toString());
			Vector vecKeys = new Vector();
			if (iTypeLoc != -1) {
				// 对于有空串的情况可能会出错
				// //////////////////////////////////////////////////////////////////////
				// StringTokenizer st = new
				// StringTokenizer(sbKeys[i].toString(), CRS_DELIM); //zjb改
				// //StringTokenizer st = new
				// StringTokenizer(sbKeys[i].toString(), ".");
				// while (st.hasMoreTokens())
				// vecKeys.add(st.nextToken());
				// ///////////////////////////////////////////////////////////////////////
				String str = sbKeys[i].toString();
				int index = str.indexOf(m_strSp);
				while (index != -1) {
					// System.out.println(str.substring(0,index));
					vecKeys.add(str.substring(0, index));
					str = str.substring(index + 1, str.length());
					index = str.indexOf(m_strSp);
				}
				vecKeys.add(str);
				String strValKey = (String) vecKeys.get(iTypeLoc);
				int indexOrg = 0;
				for (int j = 0; j < strMrsOrgDisName.length; j++)
					if (strMrsOrgDisName[j].equals(strValKey)) {
						indexOrg = j;
						break;
					}
				int iColumnType = mrsmdOrg.getColumnType(indexOrg + 1);
				if (iColumnType == Types.CHAR || iColumnType == Types.VARCHAR)
					iDataType[strRows.length + i] = Types.VARCHAR;
				else if (iColumnType == Types.INTEGER)
					iDataType[strRows.length + i] = Types.INTEGER;
				else
					iDataType[strRows.length + i] = Types.DOUBLE;
			} else {
				if (iRowTypeLoc == -1 || !bhasStringType)
					iDataType[strRows.length + i] = Types.DOUBLE;
				else if (iRowTypeLoc == -1 || bhasStringType)
					iDataType[strRows.length + i] = Types.VARCHAR;
			}
		}
		if (iRowTypeLoc != -1 && bhasStringType)
			for (int i = 0; i < iLength; i++)
				iDataType[strRows.length + i] = Types.VARCHAR;
		else if (iRowTypeLoc != -1 && !bhasStringType)
			for (int i = 0; i < iLength; i++)
				iDataType[strRows.length + i] = Types.DOUBLE;

		int[] iNewType = iDataType;
		if (m_isTypeAtCol) {
			iNewType = new int[iDataType.length + getCrsVals().length];
			System.arraycopy(iDataType, 0, iNewType, 0, iDataType.length);
			for (int i = 0; i < getCrsVals().length; i++) {
				String colName = m_mwc.getAlColVal()[m_locType].get(i)
						.toString();
				int index = getColIndexbyTypeVal(colName);
				iNewType[iDataType.length + i] = getMrsOrg().getMetaData0()
						.getColumnType(index + 1);
				if (getMrsOrgDisNames() != null)
					al.add(getMrsOrgDisNames()[index]
							+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
									.getStrByID("102401", "UC000-0001146")/*
																		 * @res
																		 * "合计"
																		 */);
				else
					al.add(colName
							+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
									.getStrByID("102401", "UC000-0001146")/*
																		 * @res
																		 * "合计"
																		 */);
			}
		}
		MemoryResultSetMetaData mrsmdNew = new MemoryResultSetMetaData(
				iNewType, al);
		return mrsmdNew;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-12 10:55:27)
	 * 
	 * @return nc.vo.pub.rs.MemoryResultSet
	 * @param mrsOrg
	 *            nc.vo.pub.rs.MemoryResultSet
	 * @param mrsmdCrs
	 *            nc.vo.pub.rs.MemoryResultSetMetaData
	 */
	private MemoryResultSet changeCrsMrs(MemoryResultSet mrsOrg,
			MemoryResultSetMetaData mrsmdCrs) throws Exception {
		if (mrsOrg == null || mrsmdCrs == null)
			return null;
		ArrayList al = new ArrayList();
		String[] strRows = excludeValsfromRows();
		String[] strCols = excludeValsfromCols();
		String[] strVals = getCrsVals();
		String[] strMrsOrgDisNames = getMrsOrgDisNames();
		MemoryResultSetMetaData mrsmdOrg = mrsOrg.getMetaData0();
		while (mrsOrg.next()) {
			ArrayList alRow = new ArrayList();
			for (int i = 0; i < strRows.length; i++)
				alRow.add(mrsOrg.getObject(strRows[i]));
			for (int i = 0; i < strCols.length; i++)
				alRow.add(mrsOrg.getObject(strCols[i]));
			for (int i = 0; i < strVals.length; i++) {
				ArrayList alRowVal = new ArrayList();
				alRowVal.addAll(alRow);
				int index = mrsmdOrg.getNameIndex(strVals[i]);
				alRowVal.add(strMrsOrgDisNames[index]);
				alRowVal.add(mrsOrg.getObject(strVals[i]));
				al.add(alRowVal);
			}
		}
		MemoryResultSet mrsNew = new MemoryResultSet(al, mrsmdCrs);
		return mrsNew;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-12 10:26:46)
	 * 
	 * @return nc.vo.pub.rs.MemoryResultSetMetaData
	 */
	private MemoryResultSetMetaData createCrsMetaData() throws Exception {
		MemoryResultSet mrsOrg = getMrsOrg();
		MemoryResultSetMetaData mrsmd = mrsOrg.getMetaData0();
		String[] strRowsTemp = splitFuHeRows(getCrsRows());
		String[] strColsTemp = splitFuHeRows(getCrsCols());
		Vector vecRows = new Vector();
		for (int i = 0; i < strRowsTemp.length; i++)
			if (!strRowsTemp[i].equals(CRS_VALUE_COLUMN))
				vecRows.add(strRowsTemp[i]);
		String[] strRows = (String[]) vecRows.toArray(new String[0]);
		Vector vecCols = new Vector();
		for (int i = 0; i < strColsTemp.length; i++)
			if (!strColsTemp[i].equals(CRS_VALUE_COLUMN))
				vecCols.add(strColsTemp[i]);
		String[] strCols = (String[]) vecCols.toArray(new String[0]);
		int[] iDataType = new int[strCols.length + strRows.length + 2];
		ArrayList al = new ArrayList();
		int index = -1;
		for (int i = 0; i < strRows.length; i++) {
			al.add(strRows[i]);
			index = mrsmd.getNameIndex(strRows[i]);
			iDataType[i] = mrsmd.getColumnType(index + 1);
		}
		for (int i = 0; i < strCols.length; i++) {
			al.add(strCols[i]);
			index = mrsmd.getNameIndex(strCols[i]);
			iDataType[strRows.length + i] = mrsmd.getColumnType(index + 1);
		}
		al.add("&type");
		al.add("&value");
		iDataType[strRows.length + strCols.length] = Types.VARCHAR;
		iDataType[iDataType.length - 1] = Types.DOUBLE;
		MemoryResultSetMetaData mrsmdNew = new MemoryResultSetMetaData(
				iDataType, al);
		return mrsmdNew;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-12 10:14:45)
	 * 
	 * @modified by jl on 2006-2-28 -- 用默认的排序器会把用户设置的旋转交叉顺序冲掉，故改成 对于交叉值来说，不排列
	 * 
	 * @return nc.vo.pub.util1.MutilWeiClass
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	private MutilWeiClass createMutilWeiClass() throws java.lang.Exception {
		if (m_mwc != null)
			return m_mwc;
		else {
			MemoryResultSet mrsCrs = getCrsMemoryResultSet();
			String allTitle[] = new String[getCrsRows().length
					+ getCrsCols().length];

			// jl+ 交叉砝码在维度信息中的位置
			int typePos = 0;
			for (int i = 0; i < allTitle.length; i++) {
				if (i < getCrsRows().length)
					allTitle[i] = getCrsRows()[i];
				else
					allTitle[i] = getCrsCols()[i - getCrsRows().length];

				// jl+
				// if (allTitle[i].indexOf("&type") != -1) {
				// zjb改，用此方法在设置列复合维度的情况下导致多表头构造混乱
				if (allTitle[i].equals("&type")) {
					typePos = i;
				}
			}

			MutilWeiClass mwc = new MutilWeiClass();
			mwc.setAlCol(allTitle);
			int weiIndexs[][] = new int[allTitle.length][];
			// for (int i = 0; i < weiIndexs.length; i++)
			// weiIndexs[i] = mrsCrs.getMetaData0().getNameIndex(allTitle[i]);
			weiIndexs = getWeiIndexArray(mrsCrs, allTitle);
			mwc.setIndex(weiIndexs);
			/** 先扫描出来有多少列便于处理 */
			for (int i = 0; i < mrsCrs.getResultArrayList().size(); i++) {
				mrsCrs.skipTo(i);
				ArrayList curAlRow = (ArrayList) mrsCrs.getRowArrayList();
				mwc.appendWeidu(curAlRow);
			}

			for (int i = 0; i < mwc.getAlColVal().length; i++) {
				Vector v = new Vector();
				v.addAll(mwc.getAlColVal()[i]);
				Comparator comparator = m_userDefComparator;
				if (comparator == null) {
					comparator = DEFAULTCOMPARATOR;
				}

				// jl+ 交叉值不排序
				// zjb改
				if (i != typePos) {
					java.util.Collections.sort(v, comparator);
				}

				ArrayList al = new ArrayList(v);
				mwc.getAlColVal()[i] = al;
			}

			// 打印：
			// System.out.println("---------AlColVal()---------");
			// for (int i = 0; i < mwc.getAlColVal().length; i++) {
			// System.out.println(mwc.getAlColVal()[i]);
			// }
			mwc.buildDataSource();
			/** 列扫描完成 */
			/** 添加数据 */
			for (int i = 0; i < mrsCrs.getResultArrayList().size(); i++) {
				mrsCrs.skipTo(i);
				ArrayList curAlRow = (ArrayList) mrsCrs.getRowArrayList();
				int index = mrsCrs.getMetaData0().getColumnCount();
				mwc.append(curAlRow, index - 1);
			}
			m_mwc = mwc;
			return m_mwc;
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-13 10:04:37)
	 * 
	 * @return java.lang.String[]
	 */
	private String[] excludeValsfromCols() throws Exception {
		String[] strColsTemp = splitFuHeRows(getCrsCols());
		Vector vecCols = new Vector();
		for (int i = 0; i < strColsTemp.length; i++)
			if (!strColsTemp[i].equals(CRS_VALUE_COLUMN))
				vecCols.add(strColsTemp[i]);
		String[] strCols = (String[]) vecCols.toArray(new String[0]);
		return strCols;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-13 10:04:09)
	 * 
	 * @return java.lang.String[]
	 */
	private String[] excludeValsfromRows() throws Exception {
		String[] strRowsTemp = splitFuHeRows(getCrsRows());
		Vector vecRows = new Vector();
		for (int i = 0; i < strRowsTemp.length; i++)
			if (!strRowsTemp[i].equals(CRS_VALUE_COLUMN))
				vecRows.add(strRowsTemp[i]);
		String[] strRows = (String[]) vecRows.toArray(new String[0]);
		return strRows;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-12-10 13:43:28)
	 * 
	 * @return int
	 */
	private int getColIndexbyTypeVal(String colName) {
		int index = -1;
		try {
			if (getMrsOrgDisNames() != null) {
				for (int j = 0; j < getMrsOrgDisNames().length; j++) {
					if (getMrsOrgDisNames()[j].equals(colName)) {
						index = j;
						break;
					}
				}
			} else {
				index = getMrsOrg().getMetaData0().getNameIndex(colName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return index;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-12 16:06:10)
	 * 
	 * @return nc.vo.pub.cquery.FldgroupVO[]
	 */
	public FldgroupVO[] getCrossTableFldGrpVOs() {
		FldgroupVO[] fldGrps = null;
		try {
			MutilWeiClass mwc = createMutilWeiClass();
			ArrayList[] listsOrg = mwc.getAlColVal();
			ArrayList[] lists = new ArrayList[listsOrg.length
					- getCrsRows().length];
			System.arraycopy(listsOrg, getCrsRows().length, lists, 0,
					lists.length);
			// FldgroupVO数组的长度
			int iLength = 0;
			for (int i = lists.length - 1; i > 0; i--) {
				int n = lists[0].size();
				for (int j = 1; j < i; j++) {
					n *= lists[j].size();
				}
				// 处理某列取值唯一的情况 yx add
				int dim = (lists[i].size() - 1 > 0) ? (lists[i].size() - 1) : 1;
				iLength += dim * n;
			}

			fldGrps = new FldgroupVO[iLength];

			int iGId = 1;
			// 记录多表头当前行的列数
			int iCurLen = 0;
			int iLen = getCrsValColCount();
			java.util.List lst = new ArrayList();
			java.util.List lstNext = new ArrayList();
			// 从最后一列往上处理，组合多表头
			for (int i = lists.length - 1; i > 0; i--) {
				// 如果是最后一列，初始化lst
				if (i == lists.length - 1) {
					iCurLen = iLen;
					for (int m = 0; m < iLen; m++)
						lst.add(Integer.toString(m
								+ splitFuHeRows(getCrsRows()).length));
				} else
					iCurLen = iCurLen / lists[i + 1].size();

				for (int j = 0; j < iCurLen / lists[i].size(); j++) {
					// 记录lstNext
					String strGrpName = null;
					if (i == 1)
						strGrpName = lists[0].get(j).toString();
					else {
						StringBuffer sb = new StringBuffer(lists[i - 1].get(
								j % lists[i - 1].size()).toString());
						for (int p = i - 2; p >= 0; p--) {
							// zjb改
							int iSize = 1;
							for (int x = p + 1; x < i; x++)
								iSize *= lists[x].size();
							sb.insert(0, lists[p].get((j / iSize)
									% lists[p].size())
									+ m_strSp);
							// sb.insert(0, lists[p].get((j / lists[i -
							// 1].size()) % lists[p].size()) + ".");
						}
						strGrpName = sb.toString();
					}

					lstNext.add(strGrpName);

					// 处理取值唯一的情况 yx add
					if (lists[i].size() == 1) {
						FldgroupVO fldGroup = new FldgroupVO();
						fldGroup.setGroupid(new Integer(iGId));
						fldGroup.setGroupname(strGrpName);
						if (i == lists.length - 1) {
							fldGroup.setGrouptype("0");
						} else {
							fldGroup.setGrouptype("2");
						}
						fldGroup.setItem1(lst.get(j).toString());
						if (i == 1) {
							fldGroup.setToplevelflag("Y");
						} else {
							fldGroup.setToplevelflag("N");
						}

						fldGrps[iGId - 1] = fldGroup;
						iGId++;
					} else {
						for (int k = 0; k < lists[i].size() - 1; k++) {
							FldgroupVO fldGroup = new FldgroupVO();
							fldGroup.setGroupid(new Integer(iGId));
							fldGroup.setGroupname(strGrpName);

							if (k == 0 && (i == lists.length - 1))
								fldGroup.setGrouptype("0");
							else if (k != 0 && (i == lists.length - 1))
								fldGroup.setGrouptype("2");
							else
								fldGroup.setGrouptype("3");
							if (k == 0)
								fldGroup.setItem1(lst.get(
										j * lists[i].size() + k).toString());
							else
								fldGroup.setItem1(strGrpName);
							fldGroup.setItem2(lst.get(
									j * lists[i].size() + k + 1).toString());
							if (i == 1 && k == lists[i].size() - 2)
								fldGroup.setToplevelflag("Y");
							else
								fldGroup.setToplevelflag("N");

							fldGrps[iGId - 1] = fldGroup;
							iGId++;
						}
					}

				}

				lst = new ArrayList();
				lst.addAll(lstNext);
				lstNext = new ArrayList();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fldGrps;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-12 13:24:51)
	 * 
	 * @return nc.vo.pub.rs.MemoryResultSet
	 */
	public MemoryResultSet getCrossTableMrs() {
		if (getCrsVals() == null)
			return null;
		MemoryResultSet mrs = null;
		// MemoryResultSet mrsResult = null;
		try {
			init();
			MutilWeiClass mwc = createMutilWeiClass();
			Object oa[][] = mwc.breakTo2Wei0(getCrsRows().length, m_locType);
			// 生成交叉后的MemoryResultSet
			int nCol = 0;
			if (oa != null) {
				if (m_isTypeAtCol)
					nCol = oa[0].length - mwc.m_AlColValue[m_locType].size();
				else
					nCol = oa[0].length;
			}
			MemoryResultSetMetaData mrsmdNew = changeCrossTableMrsmd(nCol, mwc);
			mrs = arrayToMrs(oa, mrsmdNew);
			// 根据需要合计的列的定义数组组织最终的mrs
			if (m_isTypeAtCol)
				/* mrsResult = */removeCols(mrs);
			// else
			// mrsResult = mrs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mrs;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-12 10:08:27)
	 * 
	 * @return java.lang.String[]
	 */
	public java.lang.String[] getCrsCols() {
		return m_crsCols;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-12 10:22:37)
	 * 
	 * @return nc.vo.pub.rs.MemoryResultSet
	 */
	private MemoryResultSet getCrsMemoryResultSet() throws Exception {
		MemoryResultSetMetaData mrsmdCrs = createCrsMetaData();
		MemoryResultSet mrsCrs = changeCrsMrs(getMrsOrg(), mrsmdCrs);
		return mrsCrs;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-12 10:08:27)
	 * 
	 * @return java.lang.String[]
	 */
	public java.lang.String[] getCrsRows() {
		return m_crsRows;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-12 16:13:59)
	 * 
	 * @return int
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public int getCrsValColCount() throws java.lang.Exception {
		MutilWeiClass mwc = createMutilWeiClass();
		ArrayList[] lists = mwc.getAlColVal();
		String[] strRows = getCrsRows();
		int iLength = lists[strRows.length].size();
		for (int i = strRows.length + 1; i < lists.length; i++)
			iLength *= lists[i].size();
		return iLength;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-12 10:08:27)
	 * 
	 * @return java.lang.String[]
	 */
	public java.lang.String[] getCrsVals() {
		return m_crsVals;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-12 9:32:48)
	 * 
	 * @return nc.vo.pub.rs.MemoryResultSet
	 */
	public nc.vo.pub.rs.MemoryResultSet getMrsOrg() {
		return m_mrsOrg;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-19 14:08:20)
	 * 
	 * @return java.lang.String[]
	 */
	public String[] getMrsOrgDisNames() {
		return m_mrsOrgColDisNames;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-11-27 14:36:52)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getStrSp() {
		return m_strSp;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-12-10 11:09:47)
	 * 
	 * @return java.lang.String[]
	 */
	public java.lang.String[] getSumVals() {
		return m_crsSumVals;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-14 13:12:45)
	 * 
	 * @return int[][]
	 * @param mrs
	 *            nc.vo.pub.rs.MemoryResultSet
	 * @param title
	 *            java.lang.String[]
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	private int[][] getWeiIndexArray(MemoryResultSet mrs, String[] title)
			throws java.lang.Exception {
		int[][] ia = new int[title.length][];
		for (int i = 0; i < title.length; i++) {
			StringTokenizer st = new StringTokenizer(title[i], " ,|");
			ArrayList al = new ArrayList();
			while (st.hasMoreTokens()) {
				al.add(st.nextToken());
			}
			String wa[] = new String[al.size()];
			wa = (String[]) al.toArray(wa);
			/** 一个复合的纬度 */
			int nFhwdIndex[] = new int[wa.length];
			for (int j = 0; j < nFhwdIndex.length; j++) {
				nFhwdIndex[j] = mrs.getMetaData0().getNameIndex(wa[j]);
			}
			ia[i] = nFhwdIndex;
		}
		return ia;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-19 14:24:40)
	 */
	private void init() throws SQLException {
		if (getCrsRows() == null)
			setCrsRows(new String[] {});
		if (getCrsCols() == null)
			setCrsCols(new String[] {});
		MemoryResultSetMetaData mrsmdOrg = getMrsOrg().getMetaData0();
		String[] strDisNameOrgs = new String[mrsmdOrg.getColumnCount()];
		if (getMrsOrgDisNames() == null
				|| getMrsOrgDisNames().length < mrsmdOrg.getColumnCount()) {
			for (int i = 0; i < mrsmdOrg.getColumnCount(); i++)
				strDisNameOrgs[i] = mrsmdOrg.getColumnName(i + 1);
			setMrsOrgColDisNames(strDisNameOrgs);
		}
		for (int i = 0; i < getCrsCols().length; i++) {
			if (getCrsCols()[i].equals("&type")) {
				m_isTypeAtCol = true;
				m_locType = getCrsRows().length + i;
				break;
			}
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-12-10 14:01:48)
	 * 
	 * @return boolean
	 * @param name
	 *            java.lang.String
	 */
	private boolean needSum(String name) {
		boolean needSum = false;
		for (int i = 0; i < getSumVals().length; i++) {
			if (getSumVals()[i].toLowerCase().equals(name.toLowerCase())) {
				needSum = true;
				break;
			}
		}
		return needSum;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-12-10 11:15:13)
	 * 
	 * @return nc.vo.pub.rs.MemoryResultSet
	 */
	private MemoryResultSet removeCols(MemoryResultSet mrs) throws SQLException {
		Vector vec = new Vector();
		MemoryResultSetMetaData mrsmd = mrs.getMetaData0();
		if (getSumVals() == null || getSumVals().length == 0) {
			for (int i = 0; i < getCrsVals().length; i++)
				vec.add(mrsmd.getColumnName(mrsmd.getColumnCount() - i));
		} else {
			for (int i = 0; i < getCrsVals().length; i++) {
				String colName = m_mwc.getAlColVal()[m_locType].get(i)
						.toString();
				int index = getColIndexbyTypeVal(colName);
				String realName = getMrsOrg().getMetaData0().getColumnName(
						index + 1);
				if (!needSum(realName))
					// vec.add("crosssum_" + colName);
					vec.add(colName
							+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
									.getStrByID("102401", "UC000-0001146")/*
																		 * @res
																		 * "合计"
																		 */); // zjb改
			}
		}
		for (int i = 0; i < vec.size(); i++)
			mrs.removeColumn(vec.get(i).toString());
		return mrs;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-12 10:08:27)
	 * 
	 * @param newM_crsCols
	 *            java.lang.String[]
	 */
	public void setCrsCols(java.lang.String[] crsCols) {
		m_crsCols = crsCols;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-12 10:08:27)
	 * 
	 * @param newM_crsRows
	 *            java.lang.String[]
	 */
	public void setCrsRows(java.lang.String[] crsRows) {
		m_crsRows = crsRows;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-12 10:08:27)
	 * 
	 * @param newM_crsVals
	 *            java.lang.String[]
	 */
	public void setCrsVals(java.lang.String[] crsVals) {
		m_crsVals = crsVals;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-12 9:32:48)
	 * 
	 * @param newM_mrsOrg
	 *            nc.vo.pub.rs.MemoryResultSet
	 */
	public void setMrsOrg(nc.vo.pub.rs.MemoryResultSet mrsOrg) {
		m_mrsOrg = mrsOrg;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-19 14:07:09)
	 * 
	 * @param strNames
	 *            java.lang.String[]
	 */
	public void setMrsOrgColDisNames(String[] strNames) {
		m_mrsOrgColDisNames = strNames;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-11-27 14:36:52)
	 * 
	 * @param newStrSp
	 *            java.lang.String
	 */
	public void setStrSp(java.lang.String newStrSp) {
		m_strSp = newStrSp;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-12-10 11:09:47)
	 * 
	 * @param newSumVals
	 *            java.lang.String[]
	 */
	public void setSumVals(java.lang.String[] newSumVals) {
		m_crsSumVals = newSumVals;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-8-25 10:59:35)
	 * 
	 * @param newUserDefComparator
	 *            java.util.Comparator
	 */
	public void setUserDefComparator(java.util.Comparator newUserDefComparator) {
		m_userDefComparator = newUserDefComparator;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-12-12 16:15:57)
	 * 
	 * @return java.lang.String[]
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public String[] splitFuHeRows(String[] strFuHes) throws java.lang.Exception {
		String[] strRowsTemp = strFuHes;
		Vector vec = new Vector();
		for (int i = 0; i < strRowsTemp.length; i++) {
			StringTokenizer st = new StringTokenizer(strRowsTemp[i], " ,|");
			while (st.hasMoreTokens()) {
				vec.add(st.nextToken());
			}
		}
		String[] strRows = (String[]) vec.toArray(new String[0]);
		return strRows;

		// MutilWeiClass mwc = createMutilWeiClass();
		// String[] strRowsTemp = getCrsRows();
		// int iRowCnt = 0;
		// for (int i = 0; i < strRowsTemp.length; i++)
		// if (i == 0)
		// iRowCnt = mwc.getIndex()[i].length;
		// else
		// iRowCnt += mwc.getIndex()[i].length;

		// String[] strRows = new String[iRowCnt];
		// int iCur = 0;
		// for (int i = 0; i < strRowsTemp.length; i++) {
		// if (mwc.getIndex()[i].length > 1) {
		// StringTokenizer st = new StringTokenizer(strRowsTemp[i], " ,|");
		// while (st.hasMoreTokens()) {
		// strRows[iCur] = st.nextToken();
		// iCur++;
		// }
		// } else {
		// strRows[iCur] = strRowsTemp[i];
		// iCur++;
		// }
		// }
		// return strRows;
	}
}

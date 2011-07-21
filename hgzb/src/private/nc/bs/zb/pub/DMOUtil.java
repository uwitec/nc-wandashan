package nc.bs.zb.pub;

import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;

/**
 * <p>工具类。主要用于多表关联查询调用。
 * @author twh
 * @date 2007-3-14 下午04:43:29
 * @version V5.0
 */
public class DMOUtil {
	// table + ( fieldname,sqltype)
	private static final Hashtable hashTableSqlType = new Hashtable();

	// voclass + (attributename,uftype)
	private static final Hashtable hashVoFieldType = new Hashtable();

	private static boolean debug = false;

	// VO的变量类型
	// static
	class UFTypes {
		public static final int INTEGER = 0;

		public static final int STRING = 1;

		public static final int UFDATE = 2;

		public static final int UFDATETIME = 3;

		public static final int UFDOUBLE = 4;

		public static final int UFBOOLEAN = 5;

		public static final int UFTIME = 6;

		public static final int LONG = 7;

		public static final int BYTE = 8;

		public static final int TIMESTAMP = 9;

		public static final int BYTES = 10;

		public static final int DECIMAL = 11;

		public static final int BOOLEAN = 12;

		public static final int OBJECT = 13;
	};

	/**
	 * DMOUtil 构造子注解。
	 */
	private DMOUtil() {
		super();
	}

	public static int convertVoDataTypeToConstant(Class type) {
		int uftype;
		if (type == Integer.class)
			uftype = UFTypes.INTEGER;
		else if (type == String.class)
			uftype = UFTypes.STRING;
		else if (type == Boolean.class)
			uftype = UFTypes.BOOLEAN;
		else if (type == UFDate.class)
			uftype = UFTypes.UFDATE;
		else if (type == UFDateTime.class)
			uftype = UFTypes.UFDATETIME;
		else if (type == UFDouble.class)
			uftype = UFTypes.UFDOUBLE;
		else if (type == UFBoolean.class)
			uftype = UFTypes.UFBOOLEAN;
		else if (type == UFTime.class)
			uftype = UFTypes.UFTIME;
		else if (type == Long.class)
			uftype = UFTypes.LONG;
		else if (type == Byte.class)
			uftype = UFTypes.BYTE;
		else if (type == java.sql.Timestamp.class)
			uftype = UFTypes.TIMESTAMP;
		else if (type == java.math.BigDecimal.class)
			uftype = UFTypes.DECIMAL;
		else
			uftype = UFTypes.OBJECT;
		return uftype;
	}

	/**
	 * return all vo properties but ts。 创建日期：(2002-11-6 13:38:31)
	 */
	public static String[] filterString(String[] ss, String s) {
		if (ss == null || s == null)
			return ss;
		for (int i = 0; i < ss.length; i++) {
			if (ss[i].equals(s)) {
				String[] a = new String[ss.length - 1];
				System.arraycopy(ss, 0, a, 0, i);
				if (i < ss.length - 1)
					System.arraycopy(ss, i + 1, a, i, ss.length - i - 1);
				return a;
			}
		}
		return ss;
	}

	/**
	 * 创建日期：(2003-7-9 15:25:03)
	 * 
	 * @param ss1
	 *            java.lang.String[]
	 * @param ss2
	 *            java.lang.String[]
	 * @return java.lang.String[]
	 */
	public static String[] getShareStringArray(String[] ss1, String[] ss2) {
		if (ss1 == null || ss2 == null)
			return null;
		ArrayList list = new ArrayList();
		HashMap map = new HashMap();
		for (int i = 0; i < ss1.length; i++) {
			map.put(ss1[i], null);
		}
		for (int j = 0; j < ss2.length; j++) {
			if (map.containsKey(ss2[j])) {
				list.add(ss2[j]);
			}
		}
		if (list.size() > 0) {
			return (String[]) list.toArray(new String[list.size()]);
		}
		return null;
	}

	/**
	 * get vo Hashtable[fieldName,ColumnProp[datatype,length]] by voClass。
	 * 创建日期：(2002-8-20 11:22:09)
	 */
	public static Hashtable getTableSqlTypeHash(java.sql.Connection con,
			String schema, Class voClass) throws Exception {
		SuperVO vo = (SuperVO) voClass.newInstance();
		String tablename = vo.getTableName();
		return getTableSqlTypeHash(con, schema, tablename);
	}

	/**
	 * get vo Hashtable[fieldName,ColumnProp[datatype,length]] by voClass。
	 * 创建日期：(2002-8-20 11:22:09)
	 */
	public static Hashtable getTableSqlTypeHash(java.sql.Connection con,
			String schema, String tablename) throws Exception {
		if (tablename == null)
			throw new NullPointerException("tablename is null.");
		synchronized (tablename) {
			Hashtable hs = (Hashtable) hashTableSqlType.get(tablename);
			if (hs != null)
				return hs;
			if (con == null)
				throw new NullPointerException("Connection is null.");
			try {
				java.sql.DatabaseMetaData dbmd = con.getMetaData();
				// 1. put all fields in a Hashtable:
				ResultSet rs = dbmd.getColumns(con.getCatalog(), schema,
						tablename.toUpperCase(), "%");
				hs = new Hashtable();
				while (rs.next()) {
					// Column Name
					String columnName = rs.getString("COLUMN_NAME")
							.toLowerCase();
					// SQL type from java.sql.Types
					int datatype = rs.getShort("DATA_TYPE");
					// //get charlength
					// int columnLength = rs.getInt("COLUMN_SIZE");
					hs.put(columnName.intern(), new Integer(datatype));
					if (debug)
						System.out.println("^^^^^^^^^^^^^fieldname::"
								+ columnName + "::datatype::" + datatype);
				}
				// put to voHash
				rs.close();
				hashTableSqlType.put(tablename.intern(), hs);
				if (hs.size() > 0)
					return hs;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-2-18 16:16:51)
	 */
	public static int[] getTypesFromHash(String[] fieldNames, Hashtable hash) {
		if (fieldNames == null || hash == null)
			return null;
		int[] is = new int[fieldNames.length];
		for (int i = 0; i < fieldNames.length; i++) {
			is[i] = ((Integer) hash.get(fieldNames[i])).intValue();
		}
		return is;
	}

	/**
	 * 创建日期：(2003-7-25 13:13:11)
	 * 
	 * @param voClass
	 *            java.lang.Class
	 * @return java.util.Hashtable
	 */
	public static Hashtable getVoFieldTypeHash(Class voClass) {
		if (voClass == null)
			throw new NullPointerException("voClass is null.");
		synchronized (voClass) {
			Hashtable hash = (Hashtable) hashVoFieldType.get(voClass);
			if (hash == null) {
				hash = new Hashtable();
				CircularlyAccessibleValueObject vo = null;
				try {
					vo = (CircularlyAccessibleValueObject) voClass
							.newInstance();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				if (vo == null)
					throw new NullPointerException("vo is null.");
				String[] fieldnames = vo.getAttributeNames();
				Method[] mts;
				Class type;
				int uftype;
				if (fieldnames != null && vo instanceof SuperVO) {
//					mts = SuperVOGetterSetter.getGetMethods(vo.getClass(),
//							fieldnames);
					mts=BeanHelper.getInstance().getAllGetMethod(vo.getClass(),fieldnames);
					for (int i = 0; i < fieldnames.length; i++) {
						if (mts[i] == null)
							continue;
						if (debug)
							System.out.println("^^^^^^^Fieldname::"
									+ fieldnames[i] + "::Method::"
									+ mts[i].getName()
									+ ":::::mts[i].getReturnType()::"
									+ mts[i].getReturnType());
						type = mts[i].getReturnType();
						uftype = convertVoDataTypeToConstant(type);
						if (uftype != -1)
							hash.put(fieldnames[i], new Integer(uftype));
					}
				} else {
					java.lang.reflect.Field[] fields = voClass
							.getDeclaredFields();
					String fieldname;
					boolean flag;
					// OUTER:
					for (int i = 0; i < fields.length; i++) {
						fieldname = fields[i].getName().toLowerCase();
						if (fieldname.startsWith("m_"))
							fieldname = fieldname.substring(2);
						flag = false;
						if (fieldnames != null) {
							for (int j = 0; j < fieldnames.length; j++) {
								if (fieldname.equals(fieldnames[j])) {
									flag = true;
									break;
								}
							}
						}
						if (!flag)
							continue;
						type = fields[i].getType();
						uftype = convertVoDataTypeToConstant(type);
						if (uftype != -1)
							hash.put(fieldname, new Integer(uftype));
					}
				}
				hashVoFieldType.put(voClass, hash);
			}
			return hash;
		}
	}

	/**
	 * 创建日期：(2003-2-18 16:16:51)
	 */
	public static int[] getVOTypesByClass(String[] fieldNames, Class voClass) {
		if (fieldNames == null || fieldNames.length == 0)
			return null;
		Hashtable hash = getVoFieldTypeHash(voClass);
		int[] is = new int[fieldNames.length];
		for (int i = 0; i < fieldNames.length; i++) {
			// System.out.println(fieldNames[i]);
			is[i] = ((Integer) hash.get(fieldNames[i])).intValue();
		}
		return is;
	}

	/**
	 * all parameters are not allowed be null。 set Vo properties values by
	 * ResultSet 创建日期：(2002-8-27 9:36:29)
	 */
	public static Object getVoValueFromRS(int uftype, ResultSet rs, int index)
			throws Exception {
		Object value = null;
		switch (uftype) {
		case UFTypes.INTEGER:
			int iv = rs.getInt(index);
			if (!rs.wasNull())
				value = new Integer(iv);
			break;
		case UFTypes.LONG:
			long lv = rs.getLong(index);
			if (!rs.wasNull())
				value = new Long(lv);
			break;
		case UFTypes.STRING:
			String s = rs.getString(index);
			if (s != null)
				value = s.trim();
			break;
		case UFTypes.UFDATE:
			String d = rs.getString(index);
			if (d != null && (d = d.trim()).length() > 0)
				value = new UFDate(d);
			break;
		case UFTypes.UFDATETIME:
			String dt = rs.getString(index);
			if (dt != null && (dt = dt.trim()).length() > 0)
				value = new UFDateTime(dt);
			break;
		case UFTypes.UFTIME:
			String t = rs.getString(index);
			if (t != null && (t = t.trim()).length() > 0)
				value = new UFTime(t);
			break;
		case UFTypes.UFDOUBLE:
			value = nc.bs.pub.DataManageObject.getUFDouble(rs, index, 8);
			break;
		case UFTypes.DECIMAL:
			java.math.BigDecimal bd = rs.getBigDecimal(index);
			if (bd != null)
				value = new UFDouble(bd);
			break;
		case UFTypes.UFBOOLEAN:
			String b = rs.getString(index);
			if (b != null && (b = b.trim()).length() > 0)
				value = new UFBoolean(b);
			break;
		case UFTypes.TIMESTAMP:
			value = rs.getTimestamp(index);
			break;
		case UFTypes.BYTE:
			byte bv = rs.getByte(index);
			if (!rs.wasNull())
				value = new Byte(bv);
			break;
		case UFTypes.BYTES:
			value = rs.getBytes(index);
			break;
		case UFTypes.OBJECT:
			/** GET BLOB BYTE */
			byte[] ba = ((nc.jdbc.framework.crossdb.CrossDBResultSet) rs)
					.getBlobBytes(index);
			if (ba == null || ba.length <= 5)
				break;
			java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(
					ba);
			java.io.ObjectInputStream bis = new java.io.ObjectInputStream(
					bais);
			value = bis.readObject();
			break;
		default:
			throw new BusinessException("Not supported type -- " + uftype
					+ "!");
		}
		return value;
	}

	/**
	 * 创建日期：(2003-2-20 15:49:13)
	 * 
	 * @param stmt
	 *            java.sql.Statement 改为下面的方法，但不知道是否正确
	 */

	/*
	 * private static void setBlob(int loc, String name, Object o,
	 * PreparedStatement stmt) throws java.io.IOException,
	 * java.sql.SQLException { java.io.ByteArrayOutputStream baos = new
	 * java.io.ByteArrayOutputStream(); java.io.ObjectOutputStream oos = new
	 * java.io.ObjectOutputStream( baos); oos.writeObject(o); oos.flush();
	 * baos.flush(); ((nc.jdbc.framework.crossdb.CrossDBPreparedStatement)
	 * stmt).setBlob(loc, name, baos .toByteArray()); }
	 */

	private static void setBlob(int loc, String name, Object o,
			PreparedStatement stmt) throws java.io.IOException,
			java.sql.SQLException {
		((nc.jdbc.framework.crossdb.CrossDBPreparedStatement) stmt)
				.setBlob(loc, (Blob) o);
	}

	/**
	 * all parameters are not allowed to be null except o。 set value to
	 * preparedStatement 创建日期：(2002-8-27 9:22:26)
	 */
	public static PreparedStatement setValueToStmt(PreparedStatement stmt,
			int index, Object o, int sqlType, String name) throws Exception {
		// set value to stmt
		if (o == null) {
			switch (sqlType) {
			case java.sql.Types.LONGVARBINARY:
				// case java.sql.Types.BINARY:
				// case java.sql.Types.VARBINARY:
			case java.sql.Types.OTHER:
				System.out.println("java.sql.Types.OTHER");
				setBlob(index + 1, name, o, stmt);
				break;
			default:
				stmt.setNull(index + 1, sqlType);
				break;
			}
		} else {
			switch (sqlType) {
			case java.sql.Types.CHAR:
			case java.sql.Types.VARCHAR:
			case java.sql.Types.LONGVARCHAR:
				stmt.setString(index + 1, o.toString());
				break;
			case java.sql.Types.INTEGER:
			case java.sql.Types.SMALLINT:
			case java.sql.Types.TINYINT:
			case java.sql.Types.BIGINT:
				stmt.setInt(index + 1, Integer.parseInt(o.toString()));
				break;
			case java.sql.Types.BIT:
				stmt.setString((index + 1), o.toString());
				break;
			case java.sql.Types.DOUBLE:
			case java.sql.Types.REAL:
				stmt.setDouble(index + 1, Double.parseDouble(o.toString()));
				break;
			case java.sql.Types.FLOAT:
				stmt.setFloat(index + 1, Float.parseFloat(o.toString()));
				break;
			case java.sql.Types.DECIMAL:
			case java.sql.Types.NUMERIC:
				stmt
						.setBigDecimal(index + 1,
								o instanceof UFDouble ? ((UFDouble) o)
										.toBigDecimal() : new UFDouble(o
										.toString()).toBigDecimal());
				break;
			case java.sql.Types.DATE:
			case java.sql.Types.TIMESTAMP:
				stmt.setLong(index + 1, java.sql.Timestamp.parse(o
						.toString()));
				break;
			case java.sql.Types.LONGVARBINARY:
				// case java.sql.Types.BINARY:
				// case java.sql.Types.VARBINARY:
			case java.sql.Types.OTHER:
			case java.sql.Types.BLOB:
				setBlob(index + 1, name, o, stmt);
				break;
			default:
				throw new BusinessException("Not spported sqlType\""
						+ sqlType + "\"!");
			}
		}
		return stmt;
	}
}

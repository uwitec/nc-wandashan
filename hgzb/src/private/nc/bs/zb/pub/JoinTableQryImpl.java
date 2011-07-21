package nc.bs.zb.pub;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import nc.itf.zb.pub.IJoinTableQry;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.zb.pub.JoinTableInfo;

/**
 * <p>多表联查接口实现。
 * @author twh
 * @date 2007-3-14 下午04:36:22
 * @version V5.0
 * @主要的类使用：
 *  <ul>
 * 		<li><b>如何使用该类：</b></li>
 *      <li><b>是否线程安全：</b></li>
 * 		<li><b>并发性要求：</b></li>
 * 		<li><b>使用约束：</b></li>
 * 		<li><b>其他：</b></li>
 * </ul>
 * </p>
 * <p>
 * @已知的BUG：
 * 	<ul>
 * 		<li></li>
 *  </ul>
 * </p>
 * <p>
 * @修改历史：
 */
public class JoinTableQryImpl implements IJoinTableQry {

	/**
	 * 
	 */
	public JoinTableQryImpl() {
		// TODO Auto-generated constructor stub
	}


	public SuperVO[] queryVOWithJoinTable(Class supervoClass,
			JoinTableInfo[] joinTable, String strWhere, String strOrderBy) {

		return queryVOWithJoinTable(supervoClass, joinTable, strWhere,
				strOrderBy, null, null);

	}

	public SuperVO[] queryVOWithJoinTable(Class supervoClass,
			JoinTableInfo[] joinTable, String strWhere) {

		return queryVOWithJoinTable(supervoClass, joinTable, strWhere, null,
				null, null);

	}

	public SuperVO[] queryVOWithJoinTable(Class supervoClass,
			JoinTableInfo[] joinTable, String strWhere, String strOrderBy,
			Integer maxRecNo, String[] selectedFields) {

		SuperVO[] retvos = null;
		try {

			validateClass(supervoClass, SuperVO.class);
			if (joinTable != null)
				for (int i = 0; i < joinTable.length; i++) {
					JoinTableInfo string = joinTable[i];

					validateClass(string.getJoinSourceTableClass(),
							SuperVO.class);

				}

//			int maxRecordNo = maxRecNo == null ? -1 : maxRecNo.intValue();

			// create supervo instance by Class
			SuperVO supervo = createSuperVOInstance(supervoClass);
			String[] fieldNames = supervo.getAttributeNames();
			String tableName = supervo.getTableName();
			String pkFieldName = supervo.getPKFieldName();
			validateFieldProps(tableName, pkFieldName, fieldNames);
			Connection con = null;
//			PreparedStatement stmt = null;

			//
			if (strWhere != null) {
				strWhere = strWhere
						.replaceAll(" dr", " " + tableName + "\\.dr");
				strWhere = strWhere.replaceAll("\\(dr", "\\(" + tableName
						+ "\\.dr");
				strWhere = strWhere
						.replaceAll(" pk_corp", " " + tableName + "\\.pk_corp");
				strWhere = strWhere.replaceAll("\\(pk_corp", "\\(" + tableName
						+ "\\.pk_corp");
			}
			PersistenceManager sessionManager = null;
			try {
				sessionManager = PersistenceManager.getInstance();
				JdbcSession session = sessionManager.getJdbcSession();
				con = session.getConnection();
				// con = getConnection();
				Hashtable hs = DMOUtil.getTableSqlTypeHash(con, sessionManager
						.getSchema(), tableName);
				fieldNames = getValidFieldNames(con, supervoClass, fieldNames,
						hs, pkFieldName, sessionManager.getSchema());

				if (selectedFields != null && selectedFields.length > 0)
					fieldNames = DMOUtil.getShareStringArray(fieldNames,
							selectedFields);

				// create sql sentence
				StringBuffer sql = new StringBuffer("select distinct ");
				for (int i = 0; i < fieldNames.length; i++) {
					sql.append(tableName).append(".").append(fieldNames[i])
							.append(",");
				}
				sql.append(tableName).append(".").append(pkFieldName).append(
						" from ");

				sql.append(tableName);

				if (joinTable != null)
					for (int i = 0; i < joinTable.length; i++) {
						JoinTableInfo joinTabel = joinTable[i];
	
						String joinWhere = getJoinWhere(supervo, joinTabel,
								joinTable);
						if (joinWhere != null)
							sql.append(joinWhere);
	
					}

				// create where sentence
				if (strWhere != null && strWhere.trim().length() != 0) {
					sql.append(" where ").append(strWhere);
				}
				// create order by sentence
				if (strOrderBy != null && strOrderBy.trim().length() != 0) {
					sql.append(" order by ").append(strOrderBy);
				}

				// create statement
				/*
				 * stmt = con.prepareStatement(new String(sql)); ResultSet rs =
				 * stmt.executeQuery();
				 */
				//retvos = (SuperVO[]) session.executeQuery(sql.toString(),new ArrayListProcessor());
				retvos=(SuperVO[])(((ArrayList)session.executeQuery(sql.toString(),
						new BeanListProcessor(supervoClass))).toArray(new SuperVO[0]));

				return retvos;
			} finally {
				if (sessionManager != null)
					sessionManager.release();// 需要关闭会话
			}

		} catch (Exception e) {
			// TODO: handle exception

			throw new RuntimeException(e);
		}

	}

	private String getJoinWhere(SuperVO mainClass, JoinTableInfo joinInfo,
			JoinTableInfo[] allJoins) throws Exception {

		StringBuffer result = new StringBuffer(" ");

		SuperVO joinVO = createSuperVOInstance(joinInfo
				.getJoinSourceTableClass());
		if (joinInfo.isInnerjoin())
			result.append(" inner join ");
		else
			result.append(" left outer join ");

		result.append(joinVO.getTableName());
		result.append(" ");

		String alias = joinInfo.getAlias();

		if (alias != null) {
			result.append(alias);

			result.append(" ");
		} else {
			alias = joinVO.getTableName();
		}

		result.append("on ");
		result.append(alias);
		result.append(".");
		if (joinInfo.getJoinFieldName() != null)
			result.append(joinInfo.getJoinFieldName());
		else
			result.append(joinVO.getPKFieldName());
		result.append("=");

		//
		SuperVO joinTarget = mainClass;
		if (joinInfo.getJoinTargetTableClass() != null) {
			joinTarget = createSuperVOInstance(joinInfo
					.getJoinTargetTableClass());

			result.append(getJoinTableName(joinTarget, allJoins));
		}

		else {
			result.append(mainClass.getTableName());
		}
		result.append(".");

		String fkName = joinInfo.getFkFieldName() == null ? searchFKName(
				joinTarget, joinVO) : joinInfo.getFkFieldName();
		result.append(fkName);

		result.append(" ");

		result.append("and isnull(");
		result.append(alias);
		result.append(".dr,0)=0");

		if (joinInfo.getJoinWhere() != null) {
			result.append(" and (");
			result.append(joinInfo.getJoinWhere());
			result.append(" ) ");
		}

		return result.toString();
	}

	private Map cache = new HashMap();

	private String searchFKName(SuperVO joinTarget, SuperVO joinVO) {
		if (cache
				.containsKey(joinTarget.getTableName() + joinVO.getTableName()))
			return (String) cache.get(joinTarget.getTableName()
					+ joinVO.getTableName());
		else {
			String[] attrNames = joinTarget.getAttributeNames();
			for (int i = 0; i < attrNames.length; i++) {
				String string = attrNames[i];

				if (string.equalsIgnoreCase(joinTarget.getPKFieldName())) {
					cache.put(
							joinTarget.getTableName() + joinVO.getTableName(),
							string);
					return string;
				}

			}
		}

		return "";
	}

	private String getJoinTableName(SuperVO joinTarget, JoinTableInfo[] allJoins) {

		for (int i = 0; i < allJoins.length; i++) {
			JoinTableInfo info = allJoins[i];

			if (info.getJoinSourceTableClass().isInstance(joinTarget)) {
				return info.getAlias() == null ? joinTarget.getTableName()
						: info.getAlias();
			}

		}

		return null;

	}

	/**
	 * 升级5.0新增方法，从原有superdmo实现中拷出
	 * 
	 * @param class1
	 * @param class2
	 * @throws Exception
	 */
	protected void validateClass(Class class1, Class class2) throws Exception {
		if (class1 == null || class2 == null
				|| !class2.isAssignableFrom(class1))
			throw new BusinessException("voClass is not inheritted from "
					+ class2 + " !");
		else
			return;
	}

	protected void validateFieldProps(String s, String s1, String as[])
			throws Exception {
		validateTableProps(s, s1);
		validateFieldNames(as);
	}

	protected void validatePk(String s) throws Exception {
		if (s == null)
			throw new NullPointerException("Pk is null;");
		else
			return;
	}

	protected void validatePkFieldName(String s) throws Exception {
		if (s == null)
			throw new NullPointerException("PrimaryFieldName is null.");
		else
			return;
	}

	protected void validateTableName(String s) throws Exception {
		if (s == null)
			throw new NullPointerException("TableName is null.");
		else
			return;
	}

	protected void validateTableProps(String s, String s1) throws Exception {
		validateTableName(s);
		validatePkFieldName(s1);
	}

	protected void validateFieldNames(String as[]) throws Exception {
		if (as == null || as.length == 0)
			throw new NullPointerException("FieldNames is null.");
		else
			return;
	}

	protected String[] getValidFieldNames(Connection connection, Class class1,
			String as[], Hashtable hashtable, String schema) throws Exception {
		if (hashtable == null)
			hashtable = DMOUtil.getTableSqlTypeHash(connection, schema, class1);
		if (hashtable == null)
			throw new NullPointerException("Datatype hashtable is null!");
		ArrayList arraylist = new ArrayList();
		for (int i = 0; i < as.length; i++)
			if (hashtable.get(as[i]) != null)
				arraylist.add(as[i]);

		if (arraylist.size() > 0) {
			as = new String[arraylist.size()];
			arraylist.toArray(as);
			return as;
		} else {
			throw new NullPointerException("FieldNames is null!");
		}
	}

	protected String[] getValidFieldNames(Connection connection, Class class1,
			String as[], Hashtable hashtable, String s, String schema)
			throws Exception {
		as = getValidFieldNames(connection, class1, as, hashtable, schema);
		if (as != null)
			as = filterPk(as, s);
		return as;
	}

	protected final String[] filterPk(String as[], String s) {
		return DMOUtil.filterString(as, s);
	}


	/**
	 * 升级5.0新增方法，从原有superdmo实现中拷出
	 * 
	 * @param class1
	 * @return
	 * @throws Exception
	 */
	protected SuperVO createSuperVOInstance(Class class1) throws Exception {
		return (SuperVO) class1.newInstance();
	}

}

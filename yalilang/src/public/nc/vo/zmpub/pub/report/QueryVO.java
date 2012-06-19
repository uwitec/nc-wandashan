package nc.vo.zmpub.pub.report;

/**
 * 创建日期：(2004-5-22 8:58:05)
 * @author：刘建波 
 * 查询条件VO.
 */
public class QueryVO extends nc.vo.pub.ValueObject {

	//选择字段.
	private String[] selectflds = null;
	//from clause.
	private String fromclause = null;
	//查询条件.
	private String whereString = null;
	//汇总字段.
	private String[] groupflds = null;
	//排序字段.
	private String[] orderFlds = null;
	//为了方便扩展。增加附加的字段。
	private java.util.Hashtable m_hMsg = new java.util.Hashtable();
/**
 * QueryVO 构造子注解。
 */
public QueryVO() {
	super();
}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public String getEntityName() {
	return null;
}
	public String getFromClause() {
		return fromclause;
	}
	public String[] getGroupFlds(){
		return groupflds;
	}
	public Object getMsg(String key)
{
		return m_hMsg.get(key);
	}
	public String[] getOrderFlds(){
		return orderFlds;
	}
	public String[] getSelectFlds(){
		return selectflds;
	}
	public String getWhereClause(){
		return whereString;
	}
	public void setFromClause(String from) {
		fromclause = from;
	}
	public void setGourpFlds(String[] flds){
		groupflds=flds;
	}
	public void setMsg(String key, Object o)
{
		if (o != null)
			m_hMsg.put(key, o);
	}
	public void setOrderFlds(String[] orderfld){
		orderFlds=orderfld;
	}
	public void setSelectFlds(String[] flds){
		selectflds=flds;
	}
	public void setWhereString(String where){
		whereString=where;
	}
/**
 * 验证对象各属性之间的数据逻辑正确性。
 * 
 * 创建日期：(2001-2-15 11:47:35)
 * @exception nc.vo.pub.ValidationException 如果验证失败，抛出
 *     ValidationException，对错误进行解释。
 */
public void validate() throws nc.vo.pub.ValidationException {}
}

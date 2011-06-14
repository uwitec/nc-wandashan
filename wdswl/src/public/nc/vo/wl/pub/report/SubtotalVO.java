package nc.vo.wl.pub.report;

/**
 * 此处插入类型说明。
 * 创建日期：(2004-8-18 8:45:57)
 * @author：刘建波  
 */
public class SubtotalVO extends nc.vo.pub.ValueObject {
	private String[] m_groupFlds = null; //分组列。
	private String[] m_valueFlds = null; //求值列。
	private int[] m_valueTypes = null; //求值列数据类型。
	private boolean m_bGroupFldCanNull = false; //分组列的数据是否可以为空。
	private boolean[] m_bAsLeafRs = null; //分组列合并后是否作为末级节点记录。
	private String[] m_subDescOnFlds = null; //小计名称所在列
	private String m_totalDescOnFld = null; //合计所在列
	private String[] m_orderbyFlds = null; //排序列。
	private boolean m_bDesc = true; //升序降序。
	private int m_iGroupDepth = -1;//分组的深度。
/**
 * SubtotalVO 构造子注解。
 */
public SubtotalVO() {
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
	public int getGroupDepth() {
		return m_iGroupDepth;
	}
	public String[] getGroupFlds() {
		return m_groupFlds;
	}
	public String[] getOrderbyFlds() {
		return m_orderbyFlds;
	}
	public String[] getSubDescOnFlds() {
		return m_subDescOnFlds;
	}
	public String getTotalDescOnFld() {
		return m_totalDescOnFld;
	}
	public String[] getValueFlds() {
		return m_valueFlds;
	}
	public int[] getValueFldTypes() {
		return m_valueTypes;
	}
	public boolean[] isAsLeafRs() {
		return m_bAsLeafRs;
	}
	public boolean isDesc() {
		return m_bDesc;
	}
	public boolean isGroupFldCanNUll() {
		return m_bGroupFldCanNull;
	}
	public void setAsLeafRs(boolean[] b) {
		m_bAsLeafRs = b;
	}
	public void setGroupDepth(int iDepth) {
		m_iGroupDepth = iDepth;
	}
	public void setGroupFldCanNUll(boolean b) {
		m_bGroupFldCanNull = b;
	}
	public void setGroupFlds(String[] flds) {
		m_groupFlds = flds;
	}
	public void setOrderbyFlds(String[] flds) {
		m_orderbyFlds = flds;
	}
	public void setSortDesc(boolean b) {
		m_bDesc = b;
	}
	public void setSubDescOnFlds(String[] flds) {
		m_subDescOnFlds = flds;
	}
	public void setTotalDescOnFld(String fld) {
		m_totalDescOnFld = fld;
	}
	public void setValueFlds(String[] flds) {
		m_valueFlds = flds;
	}
	public void  setValueFldTypes(int[] itype) {
		m_valueTypes = itype;
	}
/**
 * 验证对象各属性之间的数据逻辑正确性。
 * 
 * 创建日期：(2001-2-15 11:47:35)
 * @exception nc.vo.pub.ValidationException 如果验证失败，抛出
 *     ValidationException，对错误进行解释。
 */
public void validate() throws nc.vo.pub.ValidationException {
	if (getValueFldTypes() == null)
		throw new nc.vo.pub.ValidationException("在做小计合计时，SubtotalVO的求值列的类型定义不能为空!");
	if (getValueFlds() == null)
		throw new nc.vo.pub.ValidationException("在做小计合计时，SubtotalVO求值列的定义不能为空!");
	//if (isAsLeafRs() == null)
	//throw new nc.vo.pub.ValidationException("在做小计合计时，SubtotalVO分组列分组合并后是否为原始列定义不能为空!");
	if (getValueFlds().length != getValueFldTypes().length)
		throw new nc.vo.pub.ValidationException("在做小计合计时，SubtotalVO的求值列的类型定义不全!");
	//if (getGroupFlds() != null && getGroupFlds().length != isAsLeafRs().length)
	//throw new nc.vo.pub.ValidationException("在做小计合计时，SubtotalVO分组列分组合并后是否为原始列定义不全!");
	//if (getGroupFlds() != null && getGroupFlds().length != getSubDescOnFlds().length)
	//throw new nc.vo.pub.ValidationException("在做小计合计时，SubtotalVO分组列分组合并后小计合计写在哪一列定义不全!");
}
}

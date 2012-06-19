package nc.vo.zmpub.pub.freeitem;


import java.util.ArrayList;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.FieldObject;
import nc.vo.pub.IntegerField;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.StringField;
import nc.vo.pub.UFDateTimeField;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDateTime;

/**
 * 此处插入类型说明。
 * 
 * 创建日期：(2001-11-8)
 * @author：仲瑞庆
 */
public class DefdefItemVO extends CircularlyAccessibleValueObject {

	public String m_pk_defdoc;

	public String m_pk_defdoc1;
	public String m_doccode;
	public String m_docname;
	public UFDateTime m_ts;
	public Integer m_dr;

	/**
	 *     描述上面属性的FieldObjects。主要用于系统工具中，
	 * 业务代码中不会用到下面的FieldObjects。
	 */
	private static StringField m_pk_defdocField;
	private static StringField m_pk_defdefField;
	private static StringField m_pk_defdoc1Field;
	private static StringField m_doccodeField;
	private static StringField m_docnameField;
	private static UFDateTimeField m_tsField;
	private static IntegerField m_drField;
/**
 * 使用主键字段进行初始化的构造子。
 *
 * 创建日期：(2001-11-8)
 */
public DefdefItemVO() {

}
/**
 * 使用主键进行初始化的构造子。
 *
 * 创建日期：(2001-11-8)
 * @param ??fieldNameForMethod?? 主键值
 */
public DefdefItemVO(String newPk_defdoc) {

	// 为主键字段赋值:
	m_pk_defdoc = newPk_defdoc;
}
/**
 * 根类Object的方法,克隆这个VO对象。
 *
 * 创建日期：(2001-11-8)
 */
public Object clone() {

	// 复制基类内容并创建新的VO对象：
	Object o = null;
	try {
		o = super.clone();
	} catch (Exception e) {}
	DefdefItemVO defdoc = (DefdefItemVO)o;

	// 你在下面复制本VO对象的所有属性：

	return defdoc;
}
/**
 * 返回数值对象的显示名称。
 *
 * 创建日期：(2001-11-8)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public String getEntityName() {

	return "Defdoc";
}
/**
 * 返回对象标识，用来唯一定位对象。
 *
 * 创建日期：(2001-11-8)
 * @return String
 */
public String getPrimaryKey() {

	return m_pk_defdoc;
}
/**
 * 设置对象标识，用来唯一定位对象。
 *
 * 创建日期：(2001-11-8)
 * @param m_pk_defdoc String 
 */
public void setPrimaryKey(String newPk_defdoc) {

	m_pk_defdoc = newPk_defdoc;
}
/**
 * 属性m_pk_defdoc的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return String
 */
public String getPk_defdoc() {
	return m_pk_defdoc;
}

/**
 * 属性m_pk_defdoc1的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return String
 */
public String getPk_defdoc1() {
	return m_pk_defdoc1;
}
/**
 * 属性m_doccode的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return String
 */
public String getDoccode() {
	return m_doccode;
}
/**
 * 属性m_docname的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return String
 */
public String getDocname() {
	return m_docname;
}
/**
 * 属性m_ts的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return UFDateTime
 */
public UFDateTime getTs() {
	return m_ts;
}
/**
 * 属性m_dr的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return Integer
 */
public Integer getDr() {
	return m_dr;
}
/**
 * 属性m_pk_defdoc的setter方法。
 *
 * 创建日期：(2001-11-8)
 * @param newM_pk_defdoc String
 */
public void setPk_defdoc(String newPk_defdoc) {

	m_pk_defdoc = newPk_defdoc;
}

/**
 * 属性m_pk_defdoc1的setter方法。
 *
 * 创建日期：(2001-11-8)
 * @param newM_pk_defdoc1 String
 */
public void setPk_defdoc1(String newPk_defdoc1) {

	m_pk_defdoc1 = newPk_defdoc1;
}
/**
 * 属性m_doccode的setter方法。
 *
 * 创建日期：(2001-11-8)
 * @param newM_doccode String
 */
public void setDoccode(String newDoccode) {

	m_doccode = newDoccode;
}
/**
 * 属性m_docname的setter方法。
 *
 * 创建日期：(2001-11-8)
 * @param newM_docname String
 */
public void setDocname(String newDocname) {

	m_docname = newDocname;
}
/**
 * 属性m_ts的setter方法。
 *
 * 创建日期：(2001-11-8)
 * @param newM_ts UFDateTime
 */
public void setTs(UFDateTime newTs) {

	m_ts = newTs;
}
/**
 * 属性m_dr的setter方法。
 *
 * 创建日期：(2001-11-8)
 * @param newM_dr Integer
 */
public void setDr(Integer newDr) {

	m_dr = newDr;
}
/**
 * 验证对象各属性之间的数据逻辑正确性。
 *
 * 创建日期：(2001-11-8)
 * @exception nc.vo.pub.ValidationException 如果验证失败，抛出
 *     ValidationException，对错误进行解释。
 */
public void validate() throws ValidationException {

	ArrayList errFields = new ArrayList(); // errFields record those null fields that cannot be null.
	// 检查是否为不允许空的字段赋了空值，你可能需要修改下面的提示信息：
	if (m_pk_defdoc == null) {
		errFields.add(new String("m_pk_defdoc"));
	}
	if (pk_defdoclist == null) {
		errFields.add(new String("pk_defdoclist"));
	}
	if (m_doccode == null) {
		errFields.add(new String("m_doccode"));
	}
	if (m_docname == null) {
		errFields.add(new String("m_docname"));
	}
	// construct the exception message:
	StringBuffer message = new StringBuffer();
	message.append("下列字段不能为空：");
	if (errFields.size() > 0) {
		String[] temp = (String[]) errFields.toArray(new String[0]);
		message.append(temp[0]);
		for ( int i= 1; i < temp.length; i++ ) {
			message.append("、");
			message.append(temp[i]);
		}
		// throw the exception:
		throw new NullFieldException(message.toString());
	}
}
/**
 * <p>需要在一个循环中访问的属性的名称数组。
 * <p>
 * 创建日期：(??Date??)
 * @return java.lang.String[]
 */
public java.lang.String[] getAttributeNames() {

	return new String[] {
		"docsystype",
		"pk_defdoc1",
		"doccode",
		"docname",
		"ts",
		"dr",
		"pk_defdoclist" };
}
/**
 *  <p>根据一个属性名称字符串该属性的值。
 *  <p>
 * 创建日期：(2001-11-8)
 * @param key java.lang.String
 */
public Object getAttributeValue(String attributeName) {

	if (attributeName.equals("pk_defdoc")) {
		return m_pk_defdoc;
	}
	else if (attributeName.equals("pk_defdoclist")) {
		return pk_defdoclist;
	}
	else if (attributeName.equals("pk_defdoc1")) {
		return m_pk_defdoc1;
	}
	else if (attributeName.equals("doccode")) {
		return m_doccode;
	}
	else if (attributeName.equals("docname")) {
		return m_docname;
	}
	else if (attributeName.equals("ts")) {
		return m_ts;
	}
	else if (attributeName.equals("dr")) {
		return m_dr;
	}
	else if (attributeName.equals("docsystype")) {
		return m_docsystype;
	}
	return null;
}
/**
 *  <p>对参数name对型的属性设置值。
 *  <p>
 * 创建日期：(2001-11-8)
 * @param key java.lang.String
 */
public void setAttributeValue(String name, Object value) {

	try {
		if (name.equals("pk_defdoc")) {
			m_pk_defdoc = (String) value;
		}
		else if (name.equals("pk_defdoclist")) {
			pk_defdoclist = (String) value;
		}
		else if (name.equals("pk_defdoc1")) {
			m_pk_defdoc1 = (String) value;
		}
		else if (name.equals("doccode")) {
			m_doccode = (String) value;
		}
		else if (name.equals("docname")) {
			m_docname = (String) value;
		}
		else if (name.equals("ts")) {
			m_ts = switchObjToUFDateTime(value);
		}
		else if (name.equals("dr")) {
			m_dr = switchObjToInteger(value);
		}
		else if (name.equals("docsystype")) {
			m_docsystype = switchObjToInteger(value);
		}
	}
	catch (ClassCastException e) {
		throw new ClassCastException("setAttributeValue方法中为 " + name + " 赋值时类型转换错误！（值：" + value + "）");
	}
}
public  UFDateTime switchObjToUFDateTime(Object param) {
	UFDateTime ufdatetime = null;
	if (param != null) {
		if (param instanceof UFDateTime)
			ufdatetime = (UFDateTime) param;
		else {
			String sTrimValue = param.toString().trim();
			if (sTrimValue.length() > 0) {
				try {
					ufdatetime = new UFDateTime(sTrimValue,false);
				} catch (Exception e) {
				}
			}
		}
	}
	return ufdatetime;
}
public   Integer switchObjToInteger(Object param){
	Integer integer = null;
	if (param != null)
	{
		if (param instanceof Integer)
			integer = (Integer) param;
		else
		{
			String sTrimValue = param.toString().trim();
			if (sTrimValue.length() > 0)
			{
				try
				{
					integer = new Integer(sTrimValue);
				}
				catch (Exception e)
				{
				}
			}
		}
	}
	return integer;
}
/**
 * FieldObject的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return nc.vo.pub.FieldObject
 */
public static StringField getPk_defdocField() {

	if(m_pk_defdocField == null){
		try{
			m_pk_defdocField = new StringField();
			// 属性的名称
			m_pk_defdocField.setName("pk_defdoc");
			// 属性的描述
			m_pk_defdocField.setLabel("null");
			// 请添加对本属性的期它描述：

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_pk_defdocField;
}
/**
 * FieldObject的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return nc.vo.pub.FieldObject
 */
public static StringField getPk_defdefField() {

	if(m_pk_defdefField == null){
		try{
			m_pk_defdefField = new StringField();
			// 属性的名称
			m_pk_defdefField.setName("pk_defdef");
			// 属性的描述
			m_pk_defdefField.setLabel("null");
			// 请添加对本属性的期它描述：

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_pk_defdefField;
}
/**
 * FieldObject的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return nc.vo.pub.FieldObject
 */
public static StringField getPk_defdoc1Field() {

	if(m_pk_defdoc1Field == null){
		try{
			m_pk_defdoc1Field = new StringField();
			// 属性的名称
			m_pk_defdoc1Field.setName("pk_defdoc1");
			// 属性的描述
			m_pk_defdoc1Field.setLabel("null");
			// 请添加对本属性的期它描述：

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_pk_defdoc1Field;
}
/**
 * FieldObject的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return nc.vo.pub.FieldObject
 */
public static StringField getDoccodeField() {

	if(m_doccodeField == null){
		try{
			m_doccodeField = new StringField();
			// 属性的名称
			m_doccodeField.setName("doccode");
			// 属性的描述
			m_doccodeField.setLabel("null");
			// 请添加对本属性的期它描述：

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_doccodeField;
}
/**
 * FieldObject的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return nc.vo.pub.FieldObject
 */
public static StringField getDocnameField() {

	if(m_docnameField == null){
		try{
			m_docnameField = new StringField();
			// 属性的名称
			m_docnameField.setName("docname");
			// 属性的描述
			m_docnameField.setLabel("null");
			// 请添加对本属性的期它描述：

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_docnameField;
}
/**
 * FieldObject的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return nc.vo.pub.FieldObject
 */
public static UFDateTimeField getTsField() {

	if(m_tsField == null){
		try{
			m_tsField = new UFDateTimeField();
			// 属性的名称
			m_tsField.setName("ts");
			// 属性的描述
			m_tsField.setLabel("null");
			// 请添加对本属性的期它描述：

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_tsField;
}
/**
 * FieldObject的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return nc.vo.pub.FieldObject
 */
public static IntegerField getDrField() {

	if(m_drField == null){
		try{
			m_drField = new IntegerField();
			// 属性的名称
			m_drField.setName("dr");
			// 属性的描述
			m_drField.setLabel("null");
			// 请添加对本属性的期它描述：

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_drField;
}
/**
 * 返回这个ValueObject类的所有FieldObject对象的集合。
 *
 * 创建日期：(2001-11-8)
 * @return nc.vo.pub.FieldObject[]
 */
public FieldObject[] getFields() {

	FieldObject[] fields = {getPk_defdocField(), getPk_defdefField(), getPk_defdoc1Field(), getDoccodeField(), getDocnameField(), getTsField(), getDrField()};

	return fields;
}

	public Integer m_docsystype;
	public String pk_defdoclist;

/**
 * 属性m_dr的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return Integer
 */
public Integer getDocsystype() {
	return m_docsystype;
}

/**
 * 属性m_pk_defdef的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return String
 */
public String getPk_defdoclist() {
	return pk_defdoclist;
}

/**
 * 属性m_pk_defdef的setter方法。
 *
 * 创建日期：(2001-11-8)
 * @param newM_pk_defdef String
 */
public void setDocsystype(Integer newPk_defdef) {

	m_docsystype = newPk_defdef;
}

/**
 * 属性m_pk_defdef的setter方法。
 *
 * 创建日期：(2001-11-8)
 * @param newM_pk_defdef String
 */
public void setPk_defdoclist(String newPk_defdef) {

	pk_defdoclist = newPk_defdef;
}
}
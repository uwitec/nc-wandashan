package nc.vo.hg.pu.pub.freeitem;

import java.util.ArrayList;
import nc.vo.pub.*;
import nc.vo.pub.lang.*;

/**
 * 此处插入类型说明。
 * 
 * 创建日期：(2001-11-8)
 * @author：仲瑞庆
 */
public class DefdefHeaderVO extends CircularlyAccessibleValueObject {

	public String m_pk_defdef;
	public String m_defcode;
	public String m_defname;

	public String m_type;
	public Integer m_lengthnum;
	public Integer m_digitnum;
	public UFDateTime m_ts;
	public Integer m_dr;

	/**
	 *     描述上面属性的FieldObjects。主要用于系统工具中，
	 * 业务代码中不会用到下面的FieldObjects。
	 */
	private static StringField m_pk_defdefField;
	private static StringField m_defcodeField;
	private static StringField m_defnameField;

	private static StringField m_typeField;
	private static IntegerField m_lengthnumField;
	private static IntegerField m_digitnumField;
	private static UFDateTimeField m_tsField;
	private static IntegerField m_drField;
/**
 * 使用主键字段进行初始化的构造子。
 *
 * 创建日期：(2001-11-8)
 */
public DefdefHeaderVO() {

}
/**
 * 使用主键进行初始化的构造子。
 *
 * 创建日期：(2001-11-8)
 * @param ??fieldNameForMethod?? 主键值
 */
public DefdefHeaderVO(String newPk_defdef) {

	// 为主键字段赋值:
	m_pk_defdef = newPk_defdef;
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
	DefdefHeaderVO defdef = (DefdefHeaderVO)o;

	// 你在下面复制本VO对象的所有属性：

	return defdef;
}
/**
 * 返回数值对象的显示名称。
 *
 * 创建日期：(2001-11-8)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public String getEntityName() {

	return "Defdef";
}
/**
 * 返回对象标识，用来唯一定位对象。
 *
 * 创建日期：(2001-11-8)
 * @return String
 */
public String getPrimaryKey() {

	return m_pk_defdef;
}
/**
 * 设置对象标识，用来唯一定位对象。
 *
 * 创建日期：(2001-11-8)
 * @param m_pk_defdef String 
 */
public void setPrimaryKey(String newPk_defdef) {

	m_pk_defdef = newPk_defdef;
}
/**
 * 属性m_pk_defdef的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return String
 */
public String getPk_defdef() {
	return m_pk_defdef;
}
/**
 * 属性m_defcode的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return String
 */
public String getDefcode() {
	return m_defcode;
}
/**
 * 属性m_defname的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return String
 */
public String getDefname() {
	return m_defname;
}

/**
 * 属性m_type的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return String
 */
public String getType() {
	return m_type;
}
/**
 * 属性m_lengthnum的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return Integer
 */
public Integer getLengthnum() {
	return m_lengthnum;
}
/**
 * 属性m_digitnum的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return Integer
 */
public Integer getDigitnum() {
	return m_digitnum;
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
 * 属性m_pk_defdef的setter方法。
 *
 * 创建日期：(2001-11-8)
 * @param newM_pk_defdef String
 */
public void setPk_defdef(String newPk_defdef) {

	m_pk_defdef = newPk_defdef;
}
/**
 * 属性m_defcode的setter方法。
 *
 * 创建日期：(2001-11-8)
 * @param newM_defcode String
 */
public void setDefcode(String newDefcode) {

	m_defcode = newDefcode;
}
/**
 * 属性m_defname的setter方法。
 *
 * 创建日期：(2001-11-8)
 * @param newM_defname String
 */
public void setDefname(String newDefname) {

	m_defname = newDefname;
}

/**
 * 属性m_type的setter方法。
 *
 * 创建日期：(2001-11-8)
 * @param newM_type String
 */
public void setType(String newType) {

	m_type = newType;
}
/**
 * 属性m_lengthnum的setter方法。
 *
 * 创建日期：(2001-11-8)
 * @param newM_lengthnum Integer
 */
public void setLengthnum(Integer newLengthnum) {

	m_lengthnum = newLengthnum;
}
/**
 * 属性m_digitnum的setter方法。
 *
 * 创建日期：(2001-11-8)
 * @param newM_digitnum Integer
 */
public void setDigitnum(Integer newDigitnum) {

	m_digitnum = newDigitnum;
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
	if (m_pk_defdef == null) {
		errFields.add(new String("m_pk_defdef"));
	}
	if (m_defcode == null) {
		errFields.add(new String("m_defcode"));
	}
	if (m_defname == null) {
		errFields.add(new String("m_defname"));
	}
	if (pk_defdoclist == null) {
		errFields.add(new String("pk_defdoclist"));
	}
	if (m_type == null) {
		errFields.add(new String("m_type"));
	}
	if (m_lengthnum == null) {
		errFields.add(new String("m_lengthnum"));
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

	return new String[]{ "defcode", "defname", "pk_defdoclist", "type", "lengthnum", "digitnum", "ts", "dr" };
}
/**
 *  <p>根据一个属性名称字符串该属性的值。
 *  <p>
 * 创建日期：(2001-11-8)
 * @param key java.lang.String
 */
public Object getAttributeValue(String attributeName) {

	if (attributeName.equals("pk_defdef")) {
		return m_pk_defdef;
	}
	else if (attributeName.equals("defcode")) {
		return m_defcode;
	}
	else if (attributeName.equals("defname")) {
		return m_defname;
	}
	else if (attributeName.equals("pk_defdoclist")) {
		return pk_defdoclist;
	}
	else if (attributeName.equals("type")) {
		return m_type;
	}
	else if (attributeName.equals("lengthnum")) {
		return m_lengthnum;
	}
	else if (attributeName.equals("digitnum")) {
		return m_digitnum;
	}
	else if (attributeName.equals("ts")) {
		return m_ts;
	}
	else if (attributeName.equals("dr")) {
		return m_dr;
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
		if (name.equals("pk_defdef")) {
			m_pk_defdef = (String) value;
		}
		else if (name.equals("defcode")) {
			m_defcode = (String) value;
		}
		else if (name.equals("defname")) {
			m_defname = (String) value;
		}
		else if (name.equals("pk_defdoclist")) {
			pk_defdoclist = (String) value;
		}
		else if (name.equals("type")) {
			m_type = (String) value;
		}
		else if (name.equals("lengthnum")) {
			m_lengthnum = switchObjToInteger(value);
		}
		else if (name.equals("digitnum")) {
			m_digitnum = switchObjToInteger(value);
		}
		else if (name.equals("ts")) {
			m_ts = switchObjToUFDateTime(value);
		}
		else if (name.equals("dr")) {
			m_dr = switchObjToInteger(value);
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
public static StringField getDefcodeField() {

	if(m_defcodeField == null){
		try{
			m_defcodeField = new StringField();
			// 属性的名称
			m_defcodeField.setName("defcode");
			// 属性的描述
			m_defcodeField.setLabel("null");
			// 请添加对本属性的期它描述：

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_defcodeField;
}
/**
 * FieldObject的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return nc.vo.pub.FieldObject
 */
public static StringField getDefnameField() {

	if(m_defnameField == null){
		try{
			m_defnameField = new StringField();
			// 属性的名称
			m_defnameField.setName("defname");
			// 属性的描述
			m_defnameField.setLabel("null");
			// 请添加对本属性的期它描述：

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_defnameField;
}

/**
 * FieldObject的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return nc.vo.pub.FieldObject
 */
public static StringField getTypeField() {

	if(m_typeField == null){
		try{
			m_typeField = new StringField();
			// 属性的名称
			m_typeField.setName("type");
			// 属性的描述
			m_typeField.setLabel("null");
			// 请添加对本属性的期它描述：

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_typeField;
}
/**
 * FieldObject的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return nc.vo.pub.FieldObject
 */
public static IntegerField getLengthnumField() {

	if(m_lengthnumField == null){
		try{
			m_lengthnumField = new IntegerField();
			// 属性的名称
			m_lengthnumField.setName("lengthnum");
			// 属性的描述
			m_lengthnumField.setLabel("null");
			// 请添加对本属性的期它描述：

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_lengthnumField;
}
/**
 * FieldObject的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return nc.vo.pub.FieldObject
 */
public static IntegerField getDigitnumField() {

	if(m_digitnumField == null){
		try{
			m_digitnumField = new IntegerField();
			// 属性的名称
			m_digitnumField.setName("digitnum");
			// 属性的描述
			m_digitnumField.setLabel("null");
			// 请添加对本属性的期它描述：

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_digitnumField;
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

	FieldObject[] fields = {getPk_defdefField(), getDefcodeField(), getDefnameField(), getPk_defdoclistField(), getTypeField(), getLengthnumField(), getDigitnumField(), getTsField(), getDrField()};

	return fields;
}

	public String pk_defdoclist;
	private static StringField pk_defdoclistField;

/**
 * 属性m_docislevflag的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return UFBoolean
 */
public String getPk_defdoclist() {
	return pk_defdoclist;
}

/**
 * FieldObject的Getter方法。
 *
 * 创建日期：(2001-11-8)
 * @return nc.vo.pub.FieldObject
 */
public static StringField getPk_defdoclistField() {

	if(pk_defdoclistField == null){
		try{
			pk_defdoclistField = new StringField();
			// 属性的名称
			pk_defdoclistField.setName("docislevflag");
			// 属性的描述
			pk_defdoclistField.setLabel("null");
			// 请添加对本属性的期它描述：

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return pk_defdoclistField;
}

/**
 * 属性m_docislevflag的setter方法。
 *
 * 创建日期：(2001-11-8)
 * @param newM_docislevflag UFBoolean
 */
public void setPk_defdoclist(String newDocislevflag) {

	pk_defdoclist = newDocislevflag;
}
}
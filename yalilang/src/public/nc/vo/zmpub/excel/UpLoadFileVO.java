/*
 * 创建日期 2005-1-24
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.vo.zmpub.excel;

import nc.vo.pub.lang.UFBoolean;

/**
 * @author zhouxiao
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class UpLoadFileVO extends nc.vo.pub.CircularlyAccessibleValueObject {
	private String m_sSequence;//序列号
	private UFBoolean m_bSelect;//选择列
	private String m_sFileName;//文件名称
	private String m_sBillCode;//单据号
	private String m_sFileDate;//文件日期
	private String m_sFileStatus;//文件状态
/**
 * UpLoadVO 构造子注解。
 */
public UpLoadFileVO() {
	super();
}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:26:03)
 * @return java.lang.String[]
 */
public java.lang.String[] getAttributeNames() {
	return null;
}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:24:29)
 * @param key java.lang.String
 */
public Object getAttributeValue(String attributeName) {
	if (attributeName.equals("sequence")) {
		return m_sSequence;
	} else if (attributeName.equals("select")) {
		return m_bSelect;
	} else if (attributeName.equals("filename")) {
		return m_sFileName;
	} else if (attributeName.equals("billcode")) {
		return m_sBillCode;
	}else if (attributeName.equals("filedate")) {
		return m_sFileDate;
	}else if (attributeName.equals("filestatus")) {
		return m_sFileStatus;
	}
	return null;
}
/**
 * ?user>
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2004-9-1 10:00:49)
 * 修改日期，修改人，修改原因，注释标志：
 * 
 * @return java.lang.String
 */
public java.lang.String getBillCode() {
	return m_sBillCode;
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
/**
 * ?user>
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2004-9-1 10:00:49)
 * 修改日期，修改人，修改原因，注释标志：
 * 
 * @return java.lang.String
 */
public java.lang.String getFileDate() {
	return m_sFileDate;
}
/**
 * ?user>
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2004-9-1 10:00:49)
 * 修改日期，修改人，修改原因，注释标志：
 * 
 * @return java.lang.String
 */
public java.lang.String getFileName() {
	return m_sFileName;
}
/**
 * ?user>
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2004-9-1 10:00:49)
 * 修改日期，修改人，修改原因，注释标志：
 * 
 * @return java.lang.String
 */
public java.lang.String getFileStatus() {
	return m_sFileStatus;
}
/**
 * ?user>
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2004-9-1 10:00:49)
 * 修改日期，修改人，修改原因，注释标志：
 * 
 * @return nc.vo.pub.lang.UFBoolean
 */
public nc.vo.pub.lang.UFBoolean getSelect() {
	return m_bSelect;
}
/**
 * ?user>
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2004-9-1 10:00:49)
 * 修改日期，修改人，修改原因，注释标志：
 * 
 * @return java.lang.String
 */
public java.lang.String getSequence() {
	return m_sSequence;
}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:24:29)
 * @param key java.lang.String
 */
public void setAttributeValue(String name, Object value) {
	if (name.equals("sequence")) {
		m_sSequence = (String) value;
	} else if (name.equals("select")) {
		m_bSelect = (UFBoolean) value;
	} else if (name.equals("filename")) {
		m_sFileName = (String) value;
	} else if (name.equals("billcode")) {
		m_sBillCode = (String) value;
	}
	else if (name.equals("filedate")) {
		m_sFileDate = (String) value;
	}
	else if (name.equals("filestatus")) {
		m_sFileStatus = (String) value;
	}
}
/**
 * ?user>
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2004-9-1 10:00:49)
 * 修改日期，修改人，修改原因，注释标志：
 * 
 * @param newBillCode java.lang.String
 */
public void setBillCode(java.lang.String newBillCode) {
	m_sBillCode = newBillCode;
}
/**
 * ?user>
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2004-9-1 10:00:49)
 * 修改日期，修改人，修改原因，注释标志：
 * 
 * @param newFileDate java.lang.String
 */
public void setFileDate(java.lang.String newFileDate) {
	m_sFileDate = newFileDate;
}
/**
 * ?user>
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2004-9-1 10:00:49)
 * 修改日期，修改人，修改原因，注释标志：
 * 
 * @param newFileName java.lang.String
 */
public void setFileName(java.lang.String newFileName) {
	m_sFileName = newFileName;
}
/**
 * ?user>
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2004-9-1 10:00:49)
 * 修改日期，修改人，修改原因，注释标志：
 * 
 * @param newFileStatus java.lang.String
 */
public void setFileStatus(java.lang.String newFileStatus) {
	m_sFileStatus = newFileStatus;
}
/**
 * ?user>
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2004-9-1 10:00:49)
 * 修改日期，修改人，修改原因，注释标志：
 * 
 * @param newSelect nc.vo.pub.lang.UFBoolean
 */
public void setSelect(nc.vo.pub.lang.UFBoolean newSelect) {
	m_bSelect = newSelect;
}
/**
 * ?user>
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2004-9-1 10:00:49)
 * 修改日期，修改人，修改原因，注释标志：
 * 
 * @param newSequence java.lang.String
 */
public void setSequence(java.lang.String newSequence) {
	m_sSequence = newSequence;
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

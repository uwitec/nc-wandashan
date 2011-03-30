package nc.ui.po.oper;

import nc.ui.bd.ref.*;
/**
 * 此处插入类型说明。
 * 创建日期：(2001-9-25 14:40:30)
 * @author：ct
 */
public class TermRefModel extends AbstractRefModel {
	public String m_Pkcorp = null;
/**
 * TermRefModel 构造子注解。
 */
public TermRefModel() {
	super();
}
	/**
 * TermRefModel 构造子注解。
 */
public TermRefModel(String pkCorp) {
	super();
	this.m_Pkcorp=pkCorp;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-9-25 14:41:07)
 * @return int
 */
public int getDefaultFieldCount() {
	return 4;
}
/**
 * 显示字段列表
 * 创建日期：(01-4-4 0:57:23)
 * @return java.lang.String
 */
public java.lang.String[] getFieldCode() {
	return new String[] {"termsetcode","termname","termcontent"};
}
/**
 * 显示字段中文名
 * 创建日期：(01-4-4 0:57:23)
 * @return java.lang.String
 */
public java.lang.String[] getFieldName() {
	return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0002738")/*@res "条款编码"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0002736")/*@res "条款名称"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0002737")/*@res "条款类型"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0002735")/*@res "条款内容"*/};
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-10-28 12:48:37)
 * @return java.lang.String[]
 */
public String[] getHiddenFieldCode() {
	return new String[] {"pk_ct_termset"};
}
/**
 * 主键字段名
 * @return java.lang.String
 */
public String getPkFieldCode() {
	return "pk_ct_termset";
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-10-28 12:40:03)
 * @return java.lang.String
 */
public String getPkValue() {
	String strValue = (String) getValue(getPkFieldCode());
	return strValue;
}
/**
 * 参照标题
 * 创建日期：(01-4-4 0:57:23)
 * @return java.lang.String
 */
public String getRefTitle() {
	return nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000212")/*@res "合同条款档案"*/;
}
/**
 * 参照数据库表或者视图名
 * 创建日期：(01-4-4 0:57:23)
 * @return java.lang.String
 */
public String getTableName() {
	return "ct_termset";
}
	public String getWherePart() {
	return "isnull(dr,0) = 0 and pk_corp='"+m_Pkcorp+"'" ;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2004-4-1 14:23:02)
 * @author：程起伍
 * @param pk_corp java.lang.String
 */
public void setPK_Corp(String pk_corp) {
	m_Pkcorp = pk_corp;
}
}
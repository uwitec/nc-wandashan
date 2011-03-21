package nc.ui.wds.w8004040220.refer;

import nc.ui.bd.ref.AbstractRefModel;

public class CardocRefer extends AbstractRefModel {
    private int m_DefaultFieldCount = 3;
    private String[] m_aryFieldCode = {  "cscode","csname","storname" };
    private String[] m_aryFieldName = {  "货位编码","货位名称","分仓名称" };
    private String m_sPkFieldCode = "pk_cargdoc";
    private String m_sRefTitle = "货位档案";
    private String m_sTableName = "( select (select bd_stordoc.storname from bd_stordoc where bd_cargdoc.pk_stordoc=bd_stordoc.pk_stordoc and def1=1) storname,pk_cargdoc,cscode,csname from bd_cargdoc "+
" where (select bd_stordoc.storname from bd_stordoc where bd_cargdoc.pk_stordoc=bd_stordoc.pk_stordoc and def1=1) is not null and dr=0 )tmp ";
    
    /**
     * RouteRefModel 构造子注解。
     */
    public CardocRefer() {
	super();
    }
    
    /**
     * getDefaultFieldCount 方法注解。
     */
    public int getDefaultFieldCount() {
	return m_DefaultFieldCount;
    }

    /**
     * 显示字段列表 创建日期：(01-4-4 0:57:23)
     * 
     * @return java.lang.String
     */
    public java.lang.String[] getFieldCode() {
	return m_aryFieldCode;
    }

    /**
     * 显示字段中文名 创建日期：(01-4-4 0:57:23)
     * 
     * @return java.lang.String
     */
    public java.lang.String[] getFieldName() {
	return m_aryFieldName;
    }

    /**
     * 此处插入方法说明。 创建日期：(2001-9-6 10:56:48)
     */
    public String[] getHiddenFieldCode() {
	return new String[] { m_sPkFieldCode };
    }

    /**
     * 主键字段名
     * 
     * @return java.lang.String
     */
    public String getPkFieldCode() {
	return m_sPkFieldCode;
    }

    /**
     * 参照标题 创建日期：(01-4-4 0:57:23)
     * 
     * @return java.lang.String
     */
    public String getRefTitle() {
	return m_sRefTitle;
    }

    /**
     * 参照数据库表或者视图名 创建日期：(01-4-4 0:57:23)
     * 
     * @return java.lang.String
     */
    public String getTableName() {
	return m_sTableName;
    }

}

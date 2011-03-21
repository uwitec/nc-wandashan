package nc.ui.wds.w80021030.ppRefer;

import nc.ui.bd.ref.AbstractRefModel;

public class PpRefer extends AbstractRefModel {
    private int m_DefaultFieldCount = 2;
    private String[] m_aryFieldCode = { "invcode", "invname" };
    private String[] m_aryFieldName = { "存货编码", "存货名称" };
    private String m_sPkFieldCode = "pk_invbasdoc";
    private String m_sRefTitle = "存货档案";
    private String m_sTableName = "(select pk_invbasdoc , invcode ,invname from bd_invbasdoc t where dr = 0 and ( def14 ='0' or def14 = '1')and dr=0)tmp ";

    /**
     * RouteRefModel 构造子注解。
     */
    public PpRefer() {
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

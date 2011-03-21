package nc.ui.wds.w8006080806.refer;

import nc.ui.bd.ref.AbstractRefModel;

public class FydRefer extends AbstractRefModel {
    private int m_DefaultFieldCount = 4;
    private String[] m_aryFieldCode = { "ddh", "custcode","custname","tc_comname" };
    private String[] m_aryFieldName = { "存货编码", "存货名称","存货规格","运输公司名称" };
    private String m_sPkFieldCode = "pk";
    private String m_sRefTitle = "已发运单";
    private String m_sTableName = "(select b.fyd_pk pk,case when b.fyd_ddh is not null then b.fyd_ddh "+
			" when b.splitvbillno is not null then b.splitvbillno else b.vbillno  end ddh,"+
			" e.custcode custcode custcode,e.custname custname,f.tc_comname tc_comname"+
			" from tb_fydnew b,bd_cumandoc d,bd_cubasdoc e,tb_transcompany f"+
			" where b.pk_kh=d.pk_cumandoc and d.pk_cubasdoc=e.pk_cubasdoc and b.tc_pk=f.tc_pk "+
			" and b.fyd_constatus='1')tmp ";
    
    /**
     * RouteRefModel 构造子注解。
     */
    public FydRefer() {
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

package nc.ui.wds.w8006080806.refer;

import nc.ui.bd.ref.AbstractRefModel;

public class YslcRefer extends AbstractRefModel {
	private int m_DefaultFieldCount= 3;
	private String[] m_aryFieldCode= {  "quyu","STATION_B","km" };
	private String[] m_aryFieldName= { "区域","到货站","公里数" };
	private String m_sPkFieldCode= "PK_YSLCGL";
	private String m_sRefTitle= "运输里程信息";
	private String m_sTableName= "(select (select bd_areacl.areaclname from bd_areacl where bd_areacl.pk_areacl=tb_yslcgl.quyu and bd_areacl.dr=0) quyu,STATION_B,km,PK_YSLCGL from tb_yslcgl where tb_yslcgl.dr=0 )tmp ";
	/**
	 * RouteRefModel 构造子注解。
	 */
/*	public YslcRefer() {
		super.setPkFieldCode("PK_YSLCGL");
		super.setHiddenFieldCode(new String[]{"PK_YSLCGL"});
		super.setTableName("tb_yslcgl");
		
		super.setFieldCode(new String[]{"STATION_B","KM","PK_YSLCGL"} );
		super.setFieldName(new String[]{"到货站","公里里程"});
		super.setDefaultFieldCount(2);
		super.setRefTitle("运输里程信息");
		
		
			
	}*/
	
	/**
	 * getDefaultFieldCount 方法注解。
	 */
	public int getDefaultFieldCount() {
		return m_DefaultFieldCount;
	}
	/**
	 * 显示字段列表
	 * 创建日期：(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldCode() {
		return m_aryFieldCode;
	}
	/**
	 * 显示字段中文名
	 * 创建日期：(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldName() {
		return m_aryFieldName;
	}
	/**
	 * 此处插入方法说明。
	 * 创建日期：(2001-9-6 10:56:48)
	 */
	public String[] getHiddenFieldCode() {
		return new String[] { m_sPkFieldCode };
	}
	/**
	 * 主键字段名
	 * @return java.lang.String
	 */
	public String getPkFieldCode() {
		return m_sPkFieldCode;
	}
	/**
	 * 参照标题
	 * 创建日期：(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public String getRefTitle() {
		return m_sRefTitle;
	}
	/**
	 * 参照数据库表或者视图名
	 * 创建日期：(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public String getTableName() {
		return m_sTableName;
	}
	
}

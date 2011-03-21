package nc.ui.wds.w8006080806.refer;

import nc.ui.bd.ref.AbstractRefModel;

public class YslcRefer extends AbstractRefModel {
	private int m_DefaultFieldCount= 3;
	private String[] m_aryFieldCode= {  "quyu","STATION_B","km" };
	private String[] m_aryFieldName= { "����","����վ","������" };
	private String m_sPkFieldCode= "PK_YSLCGL";
	private String m_sRefTitle= "���������Ϣ";
	private String m_sTableName= "(select (select bd_areacl.areaclname from bd_areacl where bd_areacl.pk_areacl=tb_yslcgl.quyu and bd_areacl.dr=0) quyu,STATION_B,km,PK_YSLCGL from tb_yslcgl where tb_yslcgl.dr=0 )tmp ";
	/**
	 * RouteRefModel ������ע�⡣
	 */
/*	public YslcRefer() {
		super.setPkFieldCode("PK_YSLCGL");
		super.setHiddenFieldCode(new String[]{"PK_YSLCGL"});
		super.setTableName("tb_yslcgl");
		
		super.setFieldCode(new String[]{"STATION_B","KM","PK_YSLCGL"} );
		super.setFieldName(new String[]{"����վ","�������"});
		super.setDefaultFieldCount(2);
		super.setRefTitle("���������Ϣ");
		
		
			
	}*/
	
	/**
	 * getDefaultFieldCount ����ע�⡣
	 */
	public int getDefaultFieldCount() {
		return m_DefaultFieldCount;
	}
	/**
	 * ��ʾ�ֶ��б�
	 * �������ڣ�(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldCode() {
		return m_aryFieldCode;
	}
	/**
	 * ��ʾ�ֶ�������
	 * �������ڣ�(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldName() {
		return m_aryFieldName;
	}
	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2001-9-6 10:56:48)
	 */
	public String[] getHiddenFieldCode() {
		return new String[] { m_sPkFieldCode };
	}
	/**
	 * �����ֶ���
	 * @return java.lang.String
	 */
	public String getPkFieldCode() {
		return m_sPkFieldCode;
	}
	/**
	 * ���ձ���
	 * �������ڣ�(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public String getRefTitle() {
		return m_sRefTitle;
	}
	/**
	 * �������ݿ�������ͼ��
	 * �������ڣ�(01-4-4 0:57:23)
	 * @return java.lang.String
	 */
	public String getTableName() {
		return m_sTableName;
	}
	
}

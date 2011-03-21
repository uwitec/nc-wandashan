package nc.ui.wds.w80060210;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
import nc.ui.wds.w8000.CommonUnit;
import nc.vo.pub.BusinessException;

public class TC01 extends AbstractRefModel{
	
	
	
	private int m_DefaultFieldCount= 2;
	private String[] m_aryFieldCode= {  "tc_comcode","tc_comname"};
	private String[] m_aryFieldName= { "���乫˾����","���乫˾����" };
	private String m_sPkFieldCode= "tc_pk";
	private String m_sRefTitle= "���乫˾��Ϣ";
	private String m_sTableName= "(select tc_pk,tc_comcode,tc_comname from tb_transcompany where  dr=0 and pp_pk='"+TC01.getStPk()+"' )tmp ";
	/**
	 * RouteRefModel ������ע�⡣
	 */
	public TC01() {
		super();
	}
	//���ݵ�ǰ��¼�˻�òֿ�����
	public static String getStPk(){
		String s ="";
		try {
			 s= CommonUnit.getStordocName(ClientEnvironment.getInstance().getUser().getPrimaryKey());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
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

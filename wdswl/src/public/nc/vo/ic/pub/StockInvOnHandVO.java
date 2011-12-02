package nc.vo.ic.pub;

import java.util.ArrayList;

import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
/**
 * ���״̬��
 * @author Administrator
 *
 */
public class StockInvOnHandVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String pk_customize1;//----------------------�ֿ�ID  zhf add
	public String pk_cargdoc;//��λ
	public String pplpt_pk;//����id
	public String whs_batchcode;// ���κ�
	public String pk_invbasdoc; //�������ID
	public String pk_invmandoc; //�������ID
	public UFDate creadate;//��������	
	public UFDate expdate;//ʧЧ����
	public String pk_corp; //��˾
	public String whs_munit;//����λ
	public String whs_aunit;//����λ
	public UFDouble whs_omnum;//ԭʼ���������
	public UFDouble whs_stocktonnage;//���������
	public UFDouble whs_stockpieces;//��渨����
	public UFDouble whs_oanum;//ԭʼ���ʵ�ո�����
	public Integer whs_type;// ����

	public String ss_pk;//���״̬id
	public Integer whs_status;//����״̬  0�л�  1����Ϊ��
	public String whs_alert;//��澯��
	public UFDouble whs_nprice;//����
	public UFDouble whs_nmny;//���
	public UFDouble whs_hsl;//������
	public String whs_lbatchcode;//ԭʼ���κ� 

	public String pk_headsource;
	public String pk_bodysource; // ��Դ���ݱ��������� ���������
	public String whs_sourcecode;
//	����״̬
	public static int stock_state_use = 1;//ռ��
	public static int stock_state_null = 0;//��
	public static int stock_state_lock = 2;//����

	public UFTime ts;
	public String nvote;

	public String whs_customize3;
	public String whs_pk;

	public String whs_customize9;

	public UFDateTime operatetime;
	public String pk_customize3;
	public String whs_customize5;
	public String whs_customize2;
	public Integer dr;
	public String whs_customize1;
	public String whs_customize8;
	public String pk_customize9;
	public String whs_customize4;
	public String pk_customize6;
	public String whs_customize6;
	public String whs_customize7;
	public String pk_customize8;
	public String pk_customize7;
	public String pk_customize2;
	public String pk_customize5;
	public String pk_customize4;


	/**
	 * ����pk_customize4��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getPk_customize4() {
		return pk_customize4;
	}

	public UFDate getCreadate() {
		return creadate;
	}

	public void setCreadate(UFDate creadate) {
		this.creadate = creadate;
	}

	public UFDate getExpdate() {
		return expdate;
	}

	public void setExpdate(UFDate expdate) {
		this.expdate = expdate;
	}

	/**
	 * ����pk_customize4��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newPk_customize4 String
	 */
	public void setPk_customize4(String newPk_customize4) {

		pk_customize4 = newPk_customize4;
	}

	/**
	 * ����whs_oanum��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return UFDouble
	 */
	public UFDouble getWhs_oanum() {
		return whs_oanum;
	}

	/**
	 * ����whs_oanum��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_oanum UFDouble
	 */
	public void setWhs_oanum(UFDouble newWhs_oanum) {

		whs_oanum = newWhs_oanum;
	}

	/**
	 * ����whs_type��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return Integer
	 */
	public Integer getWhs_type() {
		return whs_type;
	}

	/**
	 * ����whs_type��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_type Integer
	 */
	public void setWhs_type(Integer newWhs_type) {

		whs_type = newWhs_type;
	}

	/**
	 * ����pk_customize3��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getPk_customize3() {
		return pk_customize3;
	}

	/**
	 * ����pk_customize3��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newPk_customize3 String
	 */
	public void setPk_customize3(String newPk_customize3) {

		pk_customize3 = newPk_customize3;
	}

	/**
	 * ����whs_customize5��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getWhs_customize5() {
		return whs_customize5;
	}

	/**
	 * ����whs_customize5��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_customize5 String
	 */
	public void setWhs_customize5(String newWhs_customize5) {

		whs_customize5 = newWhs_customize5;
	}

	/**
	 * ����pk_cargdoc��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getPk_cargdoc() {
		return pk_cargdoc;
	}

	/**
	 * ����pk_cargdoc��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newPk_cargdoc String
	 */
	public void setPk_cargdoc(String newPk_cargdoc) {

		pk_cargdoc = newPk_cargdoc;
	}

	/**
	 * ����whs_alert��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getWhs_alert() {
		return whs_alert;
	}

	/**
	 * ����whs_alert��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_alert String
	 */
	public void setWhs_alert(String newWhs_alert) {

		whs_alert = newWhs_alert;
	}

	/**
	 * ����whs_customize2��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getWhs_customize2() {
		return whs_customize2;
	}

	/**
	 * ����whs_customize2��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_customize2 String
	 */
	public void setWhs_customize2(String newWhs_customize2) {

		whs_customize2 = newWhs_customize2;
	}

	/**
	 * ����dr��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return Integer
	 */
	public Integer getDr() {
		return dr;
	}

	/**
	 * ����dr��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newDr Integer
	 */
	public void setDr(Integer newDr) {

		dr = newDr;
	}

	/**
	 * ����whs_customize8��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getWhs_customize8() {
		return whs_customize8;
	}

	/**
	 * ����whs_customize8��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_customize8 String
	 */
	public void setWhs_customize8(String newWhs_customize8) {

		whs_customize8 = newWhs_customize8;
	}

	/**
	 * ����whs_stockpieces��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return UFDouble
	 */
	public UFDouble getWhs_stockpieces() {
		return whs_stockpieces;
	}

	/**
	 * ����whs_stockpieces��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_stockpieces UFDouble
	 */
	public void setWhs_stockpieces(UFDouble newWhs_stockpieces) {

		whs_stockpieces = newWhs_stockpieces;
	}

	/**
	 * ����pk_bodysource��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getPk_bodysource() {
		return pk_bodysource;
	}

	/**
	 * ����pk_bodysource��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newPk_bodysource String
	 */
	public void setPk_bodysource(String newPk_bodysource) {

		pk_bodysource = newPk_bodysource;
	}

	/**
	 * ����whs_customize1��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getWhs_customize1() {
		return whs_customize1;
	}

	/**
	 * ����whs_customize1��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_customize1 String
	 */
	public void setWhs_customize1(String newWhs_customize1) {

		whs_customize1 = newWhs_customize1;
	}

	/**
	 * ����pk_invbasdoc��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getPk_invbasdoc() {
		return pk_invbasdoc;
	}

	/**
	 * ����pk_invbasdoc��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newPk_invbasdoc String
	 */
	public void setPk_invbasdoc(String newPk_invbasdoc) {

		pk_invbasdoc = newPk_invbasdoc;
	}

	/**
	 * ����pk_customize9��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getPk_customize9() {
		return pk_customize9;
	}

	/**
	 * ����pk_customize9��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newPk_customize9 String
	 */
	public void setPk_customize9(String newPk_customize9) {

		pk_customize9 = newPk_customize9;
	}

	/**
	 * ����whs_customize4��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getWhs_customize4() {
		return whs_customize4;
	}

	/**
	 * ����whs_customize4��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_customize4 String
	 */
	public void setWhs_customize4(String newWhs_customize4) {

		whs_customize4 = newWhs_customize4;
	}

	/**
	 * ����pk_customize6��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getPk_customize6() {
		return pk_customize6;
	}

	/**
	 * ����pk_customize6��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newPk_customize6 String
	 */
	public void setPk_customize6(String newPk_customize6) {

		pk_customize6 = newPk_customize6;
	}

	/**
	 * ����pk_customize1��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getPk_customize1() {
		return pk_customize1;
	}

	/**
	 * ����pk_customize1��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newPk_customize1 String
	 */
	public void setPk_customize1(String newPk_customize1) {

		pk_customize1 = newPk_customize1;
	}

	/**
	 * ����ss_pk��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getSs_pk() {
		return ss_pk;
	}

	/**
	 * ����ss_pk��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newSs_pk String
	 */
	public void setSs_pk(String newSs_pk) {

		ss_pk = newSs_pk;
	}

	/**
	 * ����whs_status��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return Integer
	 */
	public Integer getWhs_status() {
		return whs_status;
	}

	/**
	 * ����whs_status��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_status Integer
	 */
	public void setWhs_status(Integer newWhs_status) {

		whs_status = newWhs_status;
	}

	/**
	 * ����ts��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return UFTime
	 */
	public UFTime getTs() {
		return ts;
	}

	/**
	 * ����ts��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newTs UFTime
	 */
	public void setTs(UFTime newTs) {

		ts = newTs;
	}

	/**
	 * ����nvote��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getNvote() {
		return nvote;
	}

	/**
	 * ����nvote��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newNvote String
	 */
	public void setNvote(String newNvote) {

		nvote = newNvote;
	}

	/**
	 * ����whs_batchcode��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getWhs_batchcode() {
		return whs_batchcode;
	}

	/**
	 * ����whs_batchcode��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_batchcode String
	 */
	public void setWhs_batchcode(String newWhs_batchcode) {

		whs_batchcode = newWhs_batchcode;
	}

	/**
	 * ����whs_aunit��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getWhs_aunit() {
		return whs_aunit;
	}

	/**
	 * ����whs_aunit��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_aunit String
	 */
	public void setWhs_aunit(String newWhs_aunit) {

		whs_aunit = newWhs_aunit;
	}

	/**
	 * ����whs_customize3��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getWhs_customize3() {
		return whs_customize3;
	}

	/**
	 * ����whs_customize3��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_customize3 String
	 */
	public void setWhs_customize3(String newWhs_customize3) {

		whs_customize3 = newWhs_customize3;
	}

	/**
	 * ����whs_pk��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getWhs_pk() {
		return whs_pk;
	}

	/**
	 * ����whs_pk��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_pk String
	 */
	public void setWhs_pk(String newWhs_pk) {

		whs_pk = newWhs_pk;
	}

	/**
	 * ����whs_customize6��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getWhs_customize6() {
		return whs_customize6;
	}

	/**
	 * ����whs_customize6��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_customize6 String
	 */
	public void setWhs_customize6(String newWhs_customize6) {

		whs_customize6 = newWhs_customize6;
	}

	/**
	 * ����whs_customize7��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getWhs_customize7() {
		return whs_customize7;
	}

	/**
	 * ����whs_customize7��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_customize7 String
	 */
	public void setWhs_customize7(String newWhs_customize7) {

		whs_customize7 = newWhs_customize7;
	}

	/**
	 * ����pk_customize8��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getPk_customize8() {
		return pk_customize8;
	}

	/**
	 * ����pk_customize8��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newPk_customize8 String
	 */
	public void setPk_customize8(String newPk_customize8) {

		pk_customize8 = newPk_customize8;
	}

	/**
	 * ����pk_customize7��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getPk_customize7() {
		return pk_customize7;
	}

	/**
	 * ����pk_customize7��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newPk_customize7 String
	 */
	public void setPk_customize7(String newPk_customize7) {

		pk_customize7 = newPk_customize7;
	}

	/**
	 * ����pk_customize2��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getPk_customize2() {
		return pk_customize2;
	}

	/**
	 * ����pk_customize2��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newPk_customize2 String
	 */
	public void setPk_customize2(String newPk_customize2) {

		pk_customize2 = newPk_customize2;
	}

	/**
	 * ����pplpt_pk��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getPplpt_pk() {
		return pplpt_pk;
	}

	/**
	 * ����pplpt_pk��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newPplpt_pk String
	 */
	public void setPplpt_pk(String newPplpt_pk) {

		pplpt_pk = newPplpt_pk;
	}

	/**
	 * ����whs_munit��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getWhs_munit() {
		return whs_munit;
	}

	/**
	 * ����whs_munit��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_munit String
	 */
	public void setWhs_munit(String newWhs_munit) {

		whs_munit = newWhs_munit;
	}

	/**
	 * ����whs_customize9��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getWhs_customize9() {
		return whs_customize9;
	}

	/**
	 * ����whs_customize9��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_customize9 String
	 */
	public void setWhs_customize9(String newWhs_customize9) {

		whs_customize9 = newWhs_customize9;
	}

	/**
	 * ����whs_omnum��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return UFDouble
	 */
	public UFDouble getWhs_omnum() {
		return whs_omnum;
	}

	/**
	 * ����whs_omnum��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_omnum UFDouble
	 */
	public void setWhs_omnum(UFDouble newWhs_omnum) {

		whs_omnum = newWhs_omnum;
	}

	/**
	 * ����whs_sourcecode��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getWhs_sourcecode() {
		return whs_sourcecode;
	}

	/**
	 * ����whs_sourcecode��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_sourcecode String
	 */
	public void setWhs_sourcecode(String newWhs_sourcecode) {

		whs_sourcecode = newWhs_sourcecode;
	}

	/**
	 * ����whs_stocktonnage��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return UFDouble
	 */
	public UFDouble getWhs_stocktonnage() {
		return whs_stocktonnage;
	}

	/**
	 * ����whs_stocktonnage��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_stocktonnage UFDouble
	 */
	public void setWhs_stocktonnage(UFDouble newWhs_stocktonnage) {

		whs_stocktonnage = newWhs_stocktonnage;
	}

	/**
	 * ����pk_headsource��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getPk_headsource() {
		return pk_headsource;
	}

	/**
	 * ����pk_headsource��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newPk_headsource String
	 */
	public void setPk_headsource(String newPk_headsource) {

		pk_headsource = newPk_headsource;
	}

	/**
	 * ����operatetime��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return UFDateTime
	 */
	public UFDateTime getOperatetime() {
		return operatetime;
	}

	/**
	 * ����operatetime��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newOperatetime UFDateTime
	 */
	public void setOperatetime(UFDateTime newOperatetime) {

		operatetime = newOperatetime;
	}

	/**
	 * ����pk_customize5��Getter����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getPk_customize5() {
		return pk_customize5;
	}

	/**
	 * ����pk_customize5��Setter����.
	 *
	 * ��������:2010-8-2
	 * @param newPk_customize5 String
	 */
	public void setPk_customize5(String newPk_customize5) {

		pk_customize5 = newPk_customize5;
	}

	/**
	 * ��֤���������֮��������߼���ȷ��.
	 *
	 * ��������:2010-8-2
	 * @exception nc.vo.pub.ValidationException �����֤ʧ��,�׳�
	 * ValidationException,�Դ�����н���.
	 */
	public void validate() throws ValidationException {

		ArrayList errFields = new ArrayList(); // errFields record those null

		// fields that cannot be null.
		// ����Ƿ�Ϊ������յ��ֶθ��˿�ֵ,�������Ҫ�޸��������ʾ��Ϣ:

		if (whs_pk == null) {
			errFields.add(new String("whs_pk"));
		}

		StringBuffer message = new StringBuffer();
		message.append("�����ֶβ���Ϊ��:");
		if (errFields.size() > 0) {
			String[] temp = (String[]) errFields.toArray(new String[0]);
			message.append(temp[0]);
			for (int i = 1; i < temp.length; i++) {
				message.append(",");
				message.append(temp[i]);
			}
			throw new NullFieldException(message.toString());
		}
	}

	/**
	 * <p>ȡ�ø�VO�����ֶ�.
	 * <p>
	 * ��������:2010-8-2
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {

		return null;

	}

	/**
	 * <p>ȡ�ñ�����.
	 * <p>
	 * ��������:2010-8-2
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "whs_pk";
	}

	/**
	 * <p>���ر�����.
	 * <p>
	 * ��������:2010-8-2
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "tb_warehousestock";
	}

	/**
	 * ����Ĭ�Ϸ�ʽ����������.
	 *
	 * ��������:2010-8-2
	 */
	public StockInvOnHandVO() {

		super();
	}

	/**
	 * ʹ���������г�ʼ���Ĺ�����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_pk ����ֵ
	 */
	public StockInvOnHandVO(String newWhs_pk) {

		// Ϊ�����ֶθ�ֵ:
		whs_pk = newWhs_pk;

	}

	/**
	 * ���ض����ʶ,����Ψһ��λ����.
	 *
	 * ��������:2010-8-2
	 * @return String
	 */
	public String getPrimaryKey() {

		return whs_pk;

	}

	/**
	 * ���ö����ʶ,����Ψһ��λ����.
	 *
	 * ��������:2010-8-2
	 * @param newWhs_pk  String    
	 */
	public void setPrimaryKey(String newWhs_pk) {

		whs_pk = newWhs_pk;

	}

	/**
	 * ������ֵ�������ʾ����.
	 *
	 * ��������:2010-8-2
	 * @return java.lang.String ������ֵ�������ʾ����.
	 */
	public String getEntityName() {

		return "tb_warehousestock";

	}

	public UFDouble getWhs_nprice() {
		return whs_nprice;
	}

	public void setWhs_nprice(UFDouble whs_nprice) {
		this.whs_nprice = whs_nprice;
	}

	public UFDouble getWhs_nmny() {
		return whs_nmny;
	}

	public void setWhs_nmny(UFDouble whs_nmny) {
		this.whs_nmny = whs_nmny;
	}

	public UFDouble getWhs_hsl() {
		return whs_hsl;
	}

	public void setWhs_hsl(UFDouble whs_hsl) {
		this.whs_hsl = whs_hsl;
	}

	public String getWhs_lbatchcode() {
		return whs_lbatchcode;
	}

	public void setWhs_lbatchcode(String whs_lbatchcode) {
		this.whs_lbatchcode = whs_lbatchcode;
	}

	public String getPk_invmandoc() {
		return pk_invmandoc;
	}

	public void setPk_invmandoc(String pk_invmandoc) {
		this.pk_invmandoc = pk_invmandoc;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}
}

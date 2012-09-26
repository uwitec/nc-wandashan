
package nc.vo.wds.trans;
import java.util.ArrayList;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class TransVO extends SuperVO {
	private static final long serialVersionUID = 1809368317928233158L;
	private String pk_corp;// 
	public String ss_custom2;//������˾
	public String ss_remark;
	public String ss_custom6;//����ֿ�;
	public String ss_custom3;
	public String ss_pk;// ����
	public String ss_state;
	public UFDateTime ts;
	public String ss_custom1;
	public String ss_custom5;
	public Integer dr;
	public String ss_custom4;
	public Integer ss_isout;//�������   0-�����  , 1-��ۣ�����,2-���(��)
	public  UFBoolean isok ;//
	
	private UFDouble price;//�˼�()
	
	private UFDouble mail;//������()

	
	
	public UFDouble getPrice() {
		return price;
	}

	public void setPrice(UFDouble price) {
		this.price = price;
	}

	public UFDouble getMail() {
		return mail;
	}

	public void setMail(UFDouble mail) {
		this.mail = mail;
	}

	public UFBoolean getIsok() {
		return isok;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public void setIsok(UFBoolean isok) {
		this.isok = isok;
	}

	/**
	 * ����ss_custom2��Getter����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @return String
	 */
	public String getSs_custom2() {
		return ss_custom2;
	}

	/**
	 * ����ss_custom2��Setter����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @param newSs_custom2
	 *            String
	 */
	public void setSs_custom2(String newSs_custom2) {

		ss_custom2 = newSs_custom2;
	}

	/**
	 * ����ss_remark��Getter����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @return String
	 */
	public String getSs_remark() {
		return ss_remark;
	}

	/**
	 * ����ss_remark��Setter����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @param newSs_remark
	 *            String
	 */
	public void setSs_remark(String newSs_remark) {

		ss_remark = newSs_remark;
	}

	/**
	 * ����ss_custom6��Getter����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @return String
	 */
	public String getSs_custom6() {
		return ss_custom6;
	}

	/**
	 * ����ss_custom6��Setter����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @param newSs_custom6
	 *            String
	 */
	public void setSs_custom6(String newSs_custom6) {

		ss_custom6 = newSs_custom6;
	}

	/**
	 * ����ss_custom3��Getter����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @return String
	 */
	public String getSs_custom3() {
		return ss_custom3;
	}

	/**
	 * ����ss_custom3��Setter����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @param newSs_custom3
	 *            String
	 */
	public void setSs_custom3(String newSs_custom3) {

		ss_custom3 = newSs_custom3;
	}

	/**
	 * ����ss_pk��Getter����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @return String
	 */
	public String getSs_pk() {
		return ss_pk;
	}

	/**
	 * ����ss_pk��Setter����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @param newSs_pk
	 *            String
	 */
	public void setSs_pk(String newSs_pk) {

		ss_pk = newSs_pk;
	}

	/**
	 * ����ss_state��Getter����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @return String
	 */
	public String getSs_state() {
		return ss_state;
	}

	/**
	 * ����ss_state��Setter����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @param newSs_state
	 *            String
	 */
	public void setSs_state(String newSs_state) {

		ss_state = newSs_state;
	}

	/**
	 * ����ts��Getter����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @return UFDateTime
	 */
	public UFDateTime getTs() {
		return ts;
	}

	/**
	 * ����ts��Setter����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @param newTs
	 *            UFDateTime
	 */
	public void setTs(UFDateTime newTs) {

		ts = newTs;
	}

	/**
	 * ����ss_custom1��Getter����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @return String
	 */
	public String getSs_custom1() {
		return ss_custom1;
	}

	/**
	 * ����ss_custom1��Setter����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @param newSs_custom1
	 *            String
	 */
	public void setSs_custom1(String newSs_custom1) {

		ss_custom1 = newSs_custom1;
	}

	/**
	 * ����ss_custom5��Getter����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @return String
	 */
	public String getSs_custom5() {
		return ss_custom5;
	}

	/**
	 * ����ss_custom5��Setter����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @param newSs_custom5
	 *            String
	 */
	public void setSs_custom5(String newSs_custom5) {

		ss_custom5 = newSs_custom5;
	}

	/**
	 * ����dr��Getter����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @return Integer
	 */
	public Integer getDr() {
		return dr;
	}

	/**
	 * ����dr��Setter����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @param newDr
	 *            Integer
	 */
	public void setDr(Integer newDr) {

		dr = newDr;
	}

	/**
	 * ����ss_custom4��Getter����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @return String
	 */
	public String getSs_custom4() {
		return ss_custom4;
	}

	/**
	 * ����ss_custom4��Setter����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @param newSs_custom4
	 *            String
	 */
	public void setSs_custom4(String newSs_custom4) {

		ss_custom4 = newSs_custom4;
	}

	/**
	 * ��֤���������֮��������߼���ȷ��.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @exception nc.vo.pub.ValidationException
	 *                �����֤ʧ��,�׳� ValidationException,�Դ�����н���.
	 */
	public void validate() throws ValidationException {

		ArrayList errFields = new ArrayList(); // errFields record those null

		// fields that cannot be null.
		// ����Ƿ�Ϊ������յ��ֶθ��˿�ֵ,�������Ҫ�޸��������ʾ��Ϣ:

		if (ss_pk == null) {
			errFields.add(new String("ss_pk"));
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
	 * <p>
	 * ȡ�ø�VO�����ֶ�.
	 * <p>
	 * ��������:2010-7-16
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {

		return null;

	}

	/**
	 * <p>
	 * ȡ�ñ�����.
	 * <p>
	 * ��������:2010-7-16
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "ss_pk";
	}

	/**
	 * <p>
	 * ���ر�����.
	 * <p>
	 * ��������:2010-7-16
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "tb_transvo";
	}

	/**
	 * ����Ĭ�Ϸ�ʽ����������.
	 * 
	 * ��������:2010-7-16
	 */
	public TransVO() {

		super();
	}

	/**
	 * ʹ���������г�ʼ���Ĺ�����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @param newSs_pk
	 *            ����ֵ
	 */
	public TransVO(String newSs_pk) {

		// Ϊ�����ֶθ�ֵ:
		ss_pk = newSs_pk;

	}

	/**
	 * ���ض����ʶ,����Ψһ��λ����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @return String
	 */
	public String getPrimaryKey() {

		return ss_pk;

	}

	/**
	 * ���ö����ʶ,����Ψһ��λ����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @param newSs_pk
	 *            String
	 */
	public void setPrimaryKey(String newSs_pk) {

		ss_pk = newSs_pk;

	}

	/**
	 * ������ֵ�������ʾ����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @return java.lang.String ������ֵ�������ʾ����.
	 */
	public String getEntityName() {

		return "tb_stockstate";

	}

	public Integer getSs_isout() {
		return ss_isout;
	}

	public void setSs_isout(Integer ss_isout) {
		this.ss_isout = ss_isout;
	}
}

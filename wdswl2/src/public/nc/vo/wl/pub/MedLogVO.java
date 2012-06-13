package nc.vo.wl.pub;

import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;

/**
 * LogVO�����ˣ���˾���룬ҵ�����ͣ��������ͱ��룬���ݺţ�ҵ����Ϣ 5������ ������vo����ʽ�����ӿ��࣬��ScmTimelog����������ʾ��Ϣ��
 */
@SuppressWarnings( { "unchecked", "serial", "deprecation", "unused",
		"static-access" })
public class MedLogVO extends ValueObject {
	public String Pkcorp = null;

	public String BillCode = null;

	public String BillType = null;

	public String BizType = null;

	public String User = null;

	public String Hint = null;

	public long Time;

	/**
	 * @return ���� billType��
	 */
	public String getBillType() {
		return BillType;
	}

	/**
	 * @param billType
	 *            Ҫ���õ� billType��
	 */
	public void setBillType(String billType) {
		BillType = billType;
	}

	/**
	 * @return ���� bizType��
	 */
	public String getBizType() {
		return BizType;
	}

	/**
	 * @param bizType
	 *            Ҫ���õ� bizType��
	 */
	public void setBizType(String bizType) {
		BizType = bizType;
	}

	/**
	 * @return ���� pkcorp��
	 */
	public String getPkcorp() {
		return Pkcorp;
	}

	/**
	 * @param pkcorp
	 *            Ҫ���õ� pkcorp��
	 */
	public void setPkcorp(String pkcorp) {
		Pkcorp = pkcorp;
	}

	/**
	 * 
	 */
	public MedLogVO() {
		super();
		// FIXME �Զ����ɹ��캯�����
	}

	/**
	 * 
	 * @param sPkcorp
	 * @param sBillCode
	 * @param sBillType
	 * @param sBiz
	 * @param sUser
	 * @param sHint
	 */
	public MedLogVO(String sPkcorp, String sBillCode, String sBillType,
			String sBiz, String sUser, String sHint) {
		super();
		setPkcorp(sPkcorp);
		setBillcode(sBillCode);
		setBillType(sBillType);
		setBizType(sBiz);
		setUser(sUser);
		setHint(sHint);
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.vo.pub.ValueObject#getEntityName()
	 */
	public String getEntityName() {
		// FIXME �Զ����ɷ������
		return "LogVO";
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.vo.pub.ValueObject#validate()
	 */
	public void validate() throws ValidationException {
		// FIXME �Զ����ɷ������
	}

	/**
	 * @return ���� hint��
	 */
	public String getHint() {
		return Hint;
	}

	/**
	 * @param hint
	 *            Ҫ���õ� hint��
	 */
	public void setHint(String hint) {
		Hint = hint;
	}

	/**
	 * @return ���� user��
	 */
	public String getUser() {
		return User;
	}

	/**
	 * @param user
	 *            Ҫ���õ� user��
	 */
	public void setUser(String user) {
		User = user;
	}

	/**
	 * @return ���� billCode��
	 */
	public String getBillcode() {
		return BillCode;
	}

	/**
	 * @param billCode
	 *            Ҫ���õ� billCode��
	 */
	public void setBillcode(String billCode) {
		BillCode = billCode;
	}

	/**
	 * @return ���� time��
	 */
	public long getTime() {
		return Time;
	}

	/**
	 * @param time
	 *            Ҫ���õ� time��
	 */
	public void setTime(long time) {
		Time = time;
	}
}

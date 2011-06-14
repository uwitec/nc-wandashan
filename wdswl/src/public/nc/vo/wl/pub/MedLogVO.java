package nc.vo.wl.pub;

import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;

/**
 * LogVO定义了：公司编码，业务类型，单据类型编码，单据号，业务信息 5个属性 可以用vo的形式传给接口类，由ScmTimelog类来解析提示信息。
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
	 * @return 返回 billType。
	 */
	public String getBillType() {
		return BillType;
	}

	/**
	 * @param billType
	 *            要设置的 billType。
	 */
	public void setBillType(String billType) {
		BillType = billType;
	}

	/**
	 * @return 返回 bizType。
	 */
	public String getBizType() {
		return BizType;
	}

	/**
	 * @param bizType
	 *            要设置的 bizType。
	 */
	public void setBizType(String bizType) {
		BizType = bizType;
	}

	/**
	 * @return 返回 pkcorp。
	 */
	public String getPkcorp() {
		return Pkcorp;
	}

	/**
	 * @param pkcorp
	 *            要设置的 pkcorp。
	 */
	public void setPkcorp(String pkcorp) {
		Pkcorp = pkcorp;
	}

	/**
	 * 
	 */
	public MedLogVO() {
		super();
		// FIXME 自动生成构造函数存根
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
	 * （非 Javadoc）
	 * 
	 * @see nc.vo.pub.ValueObject#getEntityName()
	 */
	public String getEntityName() {
		// FIXME 自动生成方法存根
		return "LogVO";
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.vo.pub.ValueObject#validate()
	 */
	public void validate() throws ValidationException {
		// FIXME 自动生成方法存根
	}

	/**
	 * @return 返回 hint。
	 */
	public String getHint() {
		return Hint;
	}

	/**
	 * @param hint
	 *            要设置的 hint。
	 */
	public void setHint(String hint) {
		Hint = hint;
	}

	/**
	 * @return 返回 user。
	 */
	public String getUser() {
		return User;
	}

	/**
	 * @param user
	 *            要设置的 user。
	 */
	public void setUser(String user) {
		User = user;
	}

	/**
	 * @return 返回 billCode。
	 */
	public String getBillcode() {
		return BillCode;
	}

	/**
	 * @param billCode
	 *            要设置的 billCode。
	 */
	public void setBillcode(String billCode) {
		BillCode = billCode;
	}

	/**
	 * @return 返回 time。
	 */
	public long getTime() {
		return Time;
	}

	/**
	 * @param time
	 *            要设置的 time。
	 */
	public void setTime(long time) {
		Time = time;
	}
}

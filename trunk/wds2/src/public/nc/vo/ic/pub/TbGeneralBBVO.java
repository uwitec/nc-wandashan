package nc.vo.ic.pub;

import java.util.ArrayList;

import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;

public class TbGeneralBBVO extends SuperVO {

	public String gebb_pk;//id	
	public String geb_pk;//入库单子表主键       子表id
	public String cdt_pk;	// 托盘主键
	public String pwb_pk;// 出库单表体主键
	public String pk_invbasdoc;
	public String gebb_vbatchcode;//批次	
	public UFDouble gebb_num;// 托盘实际存放数量


	public String vnote;
	public String gebb_rowno;
	
	public UFDouble gebb_hsl;//换算率
	public UFDouble gebb_nprice;//单价
	public UFDouble gebb_nmny;//金额
	public String gebb_lvbatchcode;//要回写的批次号
	public Integer dr;
	public UFTime ts;
	public String gebb_customize1;
	public String gebb_customize2;
	public static final String GEBB_LVBATCHCODE="gebb_lvbatchcode";
	public static final String GEBB_NMNY="gebb_nmny";
	public static final String GEBB_NPRICE="gebb_nprice";
	public static final String GEBB_HSL="gebb_hsl";
	public static final String GEB_PK="geb_pk";
	public static final String GEBB_PK = "gebb_pk";
	public static final String PWB_PK = "pwb_pk";
	public static final String PK_INVBASDOC = "pk_invbasdoc";
	public static final String GEBB_VBATCHCODE = "gebb_vbatchcode";
	public static final String CDT_PK = "cdt_pk";
	public static final String GEBB_NUM = "gebb_num";
	public static final String DR = "dr";
	public static final String TS = "ts";
	public static final String GEBB_CUSTOMIZE1 = "gebb_customize1";
	public static final String GEBB_CUSTOMIZE2 = "gebb_customize2";
	public static final String VNOTE = "vnote";
	

	public String getGebb_pk() {
		return gebb_pk;
	}

	public void setGebb_pk(String gebb_pk) {
		this.gebb_pk = gebb_pk;
	}

	public String getPwb_pk() {
		return pwb_pk;
	}

	public void setPwb_pk(String pwb_pk) {
		this.pwb_pk = pwb_pk;
	}

	public String getPk_invbasdoc() {
		return pk_invbasdoc;
	}

	public void setPk_invbasdoc(String pk_invbasdoc) {
		this.pk_invbasdoc = pk_invbasdoc;
	}

	public String getGebb_vbatchcode() {
		return gebb_vbatchcode;
	}

	public void setGebb_vbatchcode(String gebb_vbatchcode) {
		this.gebb_vbatchcode = gebb_vbatchcode;
	}

	public String getCdt_pk() {
		return cdt_pk;
	}

	public void setCdt_pk(String cdt_pk) {
		this.cdt_pk = cdt_pk;
	}

	public UFDouble getGebb_num() {
		return gebb_num;
	}

	public void setGebb_num(UFDouble gebb_num) {
		this.gebb_num = gebb_num;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public UFTime getTs() {
		return ts;
	}

	public void setTs(UFTime ts) {
		this.ts = ts;
	}

	public String getGebb_customize1() {
		return gebb_customize1;
	}

	public void setGebb_customize1(String gebb_customize1) {
		this.gebb_customize1 = gebb_customize1;
	}

	public String getGebb_customize2() {
		return gebb_customize2;
	}

	public void setGebb_customize2(String gebb_customize2) {
		this.gebb_customize2 = gebb_customize2;
	}

	public String getVnote() {
		return vnote;
	}

	public void setVnote(String vnote) {
		this.vnote = vnote;
	}

	/**
	 * 验证对象各属性之间的数据逻辑正确性.
	 *
	 * 创建日期:2010-7-19
	 * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
	 * ValidationException,对错误进行解释.
	 */
	public void validate() throws ValidationException {

		ArrayList errFields = new ArrayList(); // errFields record those null

		// fields that cannot be null.
		// 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:

		if (gebb_pk == null) {
			errFields.add(new String("gebb_pk"));
		}

		StringBuffer message = new StringBuffer();
		message.append("下列字段不能为空:");
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
	 * <p>取得父VO主键字段.
	 * <p>
	 * 创建日期:2010-7-19
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {

		return null;

	}
	
	/**
	 * <p>取得表主键.
	 * <p>
	 * 创建日期:2010-7-19
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "gebb_pk";
	}

	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2010-7-19
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "tb_general_b_b";
	}

	/**
	 * 按照默认方式创建构造子.
	 *
	 * 创建日期:2010-7-19
	 */
	public TbGeneralBBVO() {
		super();
	}
	
	/**
	 * 使用主键进行初始化的构造子.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_pk 主键值
	 */
	public TbGeneralBBVO(String newGebb_pk) {
		// 为主键字段赋值:
		gebb_pk = newGebb_pk;
	}
	
	/**
	 * 返回对象标识,用来唯一定位对象.
	 *
	 * 创建日期:2010-7-19
	 * @return String
	 */
	public String getPrimaryKey() {
		return gebb_pk;
	}
	
	/**
	 * 设置对象标识,用来唯一定位对象.
	 *
	 * 创建日期:2010-7-19
	 * @param newGeb_pk  String    
	 */
	public void setPrimaryKey(String newGebb_pk) {

		gebb_pk = newGebb_pk;

	}

	/**
	 * 返回数值对象的显示名称.
	 *
	 * 创建日期:2010-7-19
	 * @return java.lang.String 返回数值对象的显示名称.
	 */
	public String getEntityName() {

		return "tb_general_b_b";

	}

	public String getGebb_rowno() {
		return gebb_rowno;
	}

	public void setGebb_rowno(String gebb_rowno) {
		this.gebb_rowno = gebb_rowno;
	}

	public String getGeb_pk() {
		return geb_pk;
	}

	public void setGeb_pk(String geb_pk) {
		this.geb_pk = geb_pk;
	}

	public UFDouble getGebb_hsl() {
		return gebb_hsl;
	}

	public void setGebb_hsl(UFDouble gebb_hsl) {
		this.gebb_hsl = gebb_hsl;
	}

	public UFDouble getGebb_nprice() {
		return gebb_nprice;
	}

	public void setGebb_nprice(UFDouble gebb_nprice) {
		this.gebb_nprice = gebb_nprice;
	}

	public UFDouble getGebb_nmny() {
		return gebb_nmny;
	}

	public void setGebb_nmny(UFDouble gebb_nmny) {
		this.gebb_nmny = gebb_nmny;
	}

	public String getGebb_lvbatchcode() {
		return gebb_lvbatchcode;
	}

	public void setGebb_lvbatchcode(String gebb_lvbatchcode) {
		this.gebb_lvbatchcode = gebb_lvbatchcode;
	}

}

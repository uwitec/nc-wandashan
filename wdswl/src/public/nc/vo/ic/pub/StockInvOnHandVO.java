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
 * 存货状态表
 * @author Administrator
 *
 */
public class StockInvOnHandVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String pk_customize1;//----------------------仓库ID  zhf add
	public String pk_cargdoc;//货位
	public String pplpt_pk;//托盘id
	public String whs_batchcode;// 批次号
	public String pk_invbasdoc; //存货基本ID
	public String pk_invmandoc; //存货管理ID
	public UFDate creadate;//生成日期	
	public UFDate expdate;//失效日期
	public String pk_corp; //公司
	public String whs_munit;//主单位
	public String whs_aunit;//辅单位
	public UFDouble whs_omnum;//原始入库主数量
	public UFDouble whs_stocktonnage;//库存主数量
	public UFDouble whs_stockpieces;//库存辅数量
	public UFDouble whs_oanum;//原始入库实收辅数量
	public Integer whs_type;// 类型

	public String ss_pk;//库存状态id
	public Integer whs_status;//库存表状态  0有货  1存量为空
	public String whs_alert;//库存警戒
	public UFDouble whs_nprice;//单价
	public UFDouble whs_nmny;//金额
	public UFDouble whs_hsl;//换算率
	public String whs_lbatchcode;//原始批次号 

	public String pk_headsource;
	public String pk_bodysource; // 来源单据表体主键， 缓存表主键
	public String whs_sourcecode;
//	托盘状态
	public static int stock_state_use = 1;//占用
	public static int stock_state_null = 0;//空
	public static int stock_state_lock = 2;//锁定

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
	 * 属性pk_customize4的Getter方法.
	 *
	 * 创建日期:2010-8-2
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
	 * 属性pk_customize4的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newPk_customize4 String
	 */
	public void setPk_customize4(String newPk_customize4) {

		pk_customize4 = newPk_customize4;
	}

	/**
	 * 属性whs_oanum的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return UFDouble
	 */
	public UFDouble getWhs_oanum() {
		return whs_oanum;
	}

	/**
	 * 属性whs_oanum的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_oanum UFDouble
	 */
	public void setWhs_oanum(UFDouble newWhs_oanum) {

		whs_oanum = newWhs_oanum;
	}

	/**
	 * 属性whs_type的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return Integer
	 */
	public Integer getWhs_type() {
		return whs_type;
	}

	/**
	 * 属性whs_type的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_type Integer
	 */
	public void setWhs_type(Integer newWhs_type) {

		whs_type = newWhs_type;
	}

	/**
	 * 属性pk_customize3的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getPk_customize3() {
		return pk_customize3;
	}

	/**
	 * 属性pk_customize3的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newPk_customize3 String
	 */
	public void setPk_customize3(String newPk_customize3) {

		pk_customize3 = newPk_customize3;
	}

	/**
	 * 属性whs_customize5的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getWhs_customize5() {
		return whs_customize5;
	}

	/**
	 * 属性whs_customize5的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_customize5 String
	 */
	public void setWhs_customize5(String newWhs_customize5) {

		whs_customize5 = newWhs_customize5;
	}

	/**
	 * 属性pk_cargdoc的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getPk_cargdoc() {
		return pk_cargdoc;
	}

	/**
	 * 属性pk_cargdoc的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newPk_cargdoc String
	 */
	public void setPk_cargdoc(String newPk_cargdoc) {

		pk_cargdoc = newPk_cargdoc;
	}

	/**
	 * 属性whs_alert的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getWhs_alert() {
		return whs_alert;
	}

	/**
	 * 属性whs_alert的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_alert String
	 */
	public void setWhs_alert(String newWhs_alert) {

		whs_alert = newWhs_alert;
	}

	/**
	 * 属性whs_customize2的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getWhs_customize2() {
		return whs_customize2;
	}

	/**
	 * 属性whs_customize2的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_customize2 String
	 */
	public void setWhs_customize2(String newWhs_customize2) {

		whs_customize2 = newWhs_customize2;
	}

	/**
	 * 属性dr的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return Integer
	 */
	public Integer getDr() {
		return dr;
	}

	/**
	 * 属性dr的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newDr Integer
	 */
	public void setDr(Integer newDr) {

		dr = newDr;
	}

	/**
	 * 属性whs_customize8的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getWhs_customize8() {
		return whs_customize8;
	}

	/**
	 * 属性whs_customize8的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_customize8 String
	 */
	public void setWhs_customize8(String newWhs_customize8) {

		whs_customize8 = newWhs_customize8;
	}

	/**
	 * 属性whs_stockpieces的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return UFDouble
	 */
	public UFDouble getWhs_stockpieces() {
		return whs_stockpieces;
	}

	/**
	 * 属性whs_stockpieces的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_stockpieces UFDouble
	 */
	public void setWhs_stockpieces(UFDouble newWhs_stockpieces) {

		whs_stockpieces = newWhs_stockpieces;
	}

	/**
	 * 属性pk_bodysource的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getPk_bodysource() {
		return pk_bodysource;
	}

	/**
	 * 属性pk_bodysource的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newPk_bodysource String
	 */
	public void setPk_bodysource(String newPk_bodysource) {

		pk_bodysource = newPk_bodysource;
	}

	/**
	 * 属性whs_customize1的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getWhs_customize1() {
		return whs_customize1;
	}

	/**
	 * 属性whs_customize1的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_customize1 String
	 */
	public void setWhs_customize1(String newWhs_customize1) {

		whs_customize1 = newWhs_customize1;
	}

	/**
	 * 属性pk_invbasdoc的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getPk_invbasdoc() {
		return pk_invbasdoc;
	}

	/**
	 * 属性pk_invbasdoc的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newPk_invbasdoc String
	 */
	public void setPk_invbasdoc(String newPk_invbasdoc) {

		pk_invbasdoc = newPk_invbasdoc;
	}

	/**
	 * 属性pk_customize9的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getPk_customize9() {
		return pk_customize9;
	}

	/**
	 * 属性pk_customize9的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newPk_customize9 String
	 */
	public void setPk_customize9(String newPk_customize9) {

		pk_customize9 = newPk_customize9;
	}

	/**
	 * 属性whs_customize4的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getWhs_customize4() {
		return whs_customize4;
	}

	/**
	 * 属性whs_customize4的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_customize4 String
	 */
	public void setWhs_customize4(String newWhs_customize4) {

		whs_customize4 = newWhs_customize4;
	}

	/**
	 * 属性pk_customize6的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getPk_customize6() {
		return pk_customize6;
	}

	/**
	 * 属性pk_customize6的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newPk_customize6 String
	 */
	public void setPk_customize6(String newPk_customize6) {

		pk_customize6 = newPk_customize6;
	}

	/**
	 * 属性pk_customize1的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getPk_customize1() {
		return pk_customize1;
	}

	/**
	 * 属性pk_customize1的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newPk_customize1 String
	 */
	public void setPk_customize1(String newPk_customize1) {

		pk_customize1 = newPk_customize1;
	}

	/**
	 * 属性ss_pk的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getSs_pk() {
		return ss_pk;
	}

	/**
	 * 属性ss_pk的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newSs_pk String
	 */
	public void setSs_pk(String newSs_pk) {

		ss_pk = newSs_pk;
	}

	/**
	 * 属性whs_status的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return Integer
	 */
	public Integer getWhs_status() {
		return whs_status;
	}

	/**
	 * 属性whs_status的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_status Integer
	 */
	public void setWhs_status(Integer newWhs_status) {

		whs_status = newWhs_status;
	}

	/**
	 * 属性ts的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return UFTime
	 */
	public UFTime getTs() {
		return ts;
	}

	/**
	 * 属性ts的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newTs UFTime
	 */
	public void setTs(UFTime newTs) {

		ts = newTs;
	}

	/**
	 * 属性nvote的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getNvote() {
		return nvote;
	}

	/**
	 * 属性nvote的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newNvote String
	 */
	public void setNvote(String newNvote) {

		nvote = newNvote;
	}

	/**
	 * 属性whs_batchcode的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getWhs_batchcode() {
		return whs_batchcode;
	}

	/**
	 * 属性whs_batchcode的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_batchcode String
	 */
	public void setWhs_batchcode(String newWhs_batchcode) {

		whs_batchcode = newWhs_batchcode;
	}

	/**
	 * 属性whs_aunit的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getWhs_aunit() {
		return whs_aunit;
	}

	/**
	 * 属性whs_aunit的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_aunit String
	 */
	public void setWhs_aunit(String newWhs_aunit) {

		whs_aunit = newWhs_aunit;
	}

	/**
	 * 属性whs_customize3的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getWhs_customize3() {
		return whs_customize3;
	}

	/**
	 * 属性whs_customize3的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_customize3 String
	 */
	public void setWhs_customize3(String newWhs_customize3) {

		whs_customize3 = newWhs_customize3;
	}

	/**
	 * 属性whs_pk的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getWhs_pk() {
		return whs_pk;
	}

	/**
	 * 属性whs_pk的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_pk String
	 */
	public void setWhs_pk(String newWhs_pk) {

		whs_pk = newWhs_pk;
	}

	/**
	 * 属性whs_customize6的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getWhs_customize6() {
		return whs_customize6;
	}

	/**
	 * 属性whs_customize6的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_customize6 String
	 */
	public void setWhs_customize6(String newWhs_customize6) {

		whs_customize6 = newWhs_customize6;
	}

	/**
	 * 属性whs_customize7的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getWhs_customize7() {
		return whs_customize7;
	}

	/**
	 * 属性whs_customize7的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_customize7 String
	 */
	public void setWhs_customize7(String newWhs_customize7) {

		whs_customize7 = newWhs_customize7;
	}

	/**
	 * 属性pk_customize8的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getPk_customize8() {
		return pk_customize8;
	}

	/**
	 * 属性pk_customize8的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newPk_customize8 String
	 */
	public void setPk_customize8(String newPk_customize8) {

		pk_customize8 = newPk_customize8;
	}

	/**
	 * 属性pk_customize7的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getPk_customize7() {
		return pk_customize7;
	}

	/**
	 * 属性pk_customize7的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newPk_customize7 String
	 */
	public void setPk_customize7(String newPk_customize7) {

		pk_customize7 = newPk_customize7;
	}

	/**
	 * 属性pk_customize2的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getPk_customize2() {
		return pk_customize2;
	}

	/**
	 * 属性pk_customize2的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newPk_customize2 String
	 */
	public void setPk_customize2(String newPk_customize2) {

		pk_customize2 = newPk_customize2;
	}

	/**
	 * 属性pplpt_pk的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getPplpt_pk() {
		return pplpt_pk;
	}

	/**
	 * 属性pplpt_pk的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newPplpt_pk String
	 */
	public void setPplpt_pk(String newPplpt_pk) {

		pplpt_pk = newPplpt_pk;
	}

	/**
	 * 属性whs_munit的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getWhs_munit() {
		return whs_munit;
	}

	/**
	 * 属性whs_munit的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_munit String
	 */
	public void setWhs_munit(String newWhs_munit) {

		whs_munit = newWhs_munit;
	}

	/**
	 * 属性whs_customize9的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getWhs_customize9() {
		return whs_customize9;
	}

	/**
	 * 属性whs_customize9的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_customize9 String
	 */
	public void setWhs_customize9(String newWhs_customize9) {

		whs_customize9 = newWhs_customize9;
	}

	/**
	 * 属性whs_omnum的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return UFDouble
	 */
	public UFDouble getWhs_omnum() {
		return whs_omnum;
	}

	/**
	 * 属性whs_omnum的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_omnum UFDouble
	 */
	public void setWhs_omnum(UFDouble newWhs_omnum) {

		whs_omnum = newWhs_omnum;
	}

	/**
	 * 属性whs_sourcecode的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getWhs_sourcecode() {
		return whs_sourcecode;
	}

	/**
	 * 属性whs_sourcecode的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_sourcecode String
	 */
	public void setWhs_sourcecode(String newWhs_sourcecode) {

		whs_sourcecode = newWhs_sourcecode;
	}

	/**
	 * 属性whs_stocktonnage的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return UFDouble
	 */
	public UFDouble getWhs_stocktonnage() {
		return whs_stocktonnage;
	}

	/**
	 * 属性whs_stocktonnage的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_stocktonnage UFDouble
	 */
	public void setWhs_stocktonnage(UFDouble newWhs_stocktonnage) {

		whs_stocktonnage = newWhs_stocktonnage;
	}

	/**
	 * 属性pk_headsource的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getPk_headsource() {
		return pk_headsource;
	}

	/**
	 * 属性pk_headsource的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newPk_headsource String
	 */
	public void setPk_headsource(String newPk_headsource) {

		pk_headsource = newPk_headsource;
	}

	/**
	 * 属性operatetime的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return UFDateTime
	 */
	public UFDateTime getOperatetime() {
		return operatetime;
	}

	/**
	 * 属性operatetime的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newOperatetime UFDateTime
	 */
	public void setOperatetime(UFDateTime newOperatetime) {

		operatetime = newOperatetime;
	}

	/**
	 * 属性pk_customize5的Getter方法.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getPk_customize5() {
		return pk_customize5;
	}

	/**
	 * 属性pk_customize5的Setter方法.
	 *
	 * 创建日期:2010-8-2
	 * @param newPk_customize5 String
	 */
	public void setPk_customize5(String newPk_customize5) {

		pk_customize5 = newPk_customize5;
	}

	/**
	 * 验证对象各属性之间的数据逻辑正确性.
	 *
	 * 创建日期:2010-8-2
	 * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
	 * ValidationException,对错误进行解释.
	 */
	public void validate() throws ValidationException {

		ArrayList errFields = new ArrayList(); // errFields record those null

		// fields that cannot be null.
		// 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:

		if (whs_pk == null) {
			errFields.add(new String("whs_pk"));
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
	 * 创建日期:2010-8-2
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {

		return null;

	}

	/**
	 * <p>取得表主键.
	 * <p>
	 * 创建日期:2010-8-2
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "whs_pk";
	}

	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2010-8-2
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "tb_warehousestock";
	}

	/**
	 * 按照默认方式创建构造子.
	 *
	 * 创建日期:2010-8-2
	 */
	public StockInvOnHandVO() {

		super();
	}

	/**
	 * 使用主键进行初始化的构造子.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_pk 主键值
	 */
	public StockInvOnHandVO(String newWhs_pk) {

		// 为主键字段赋值:
		whs_pk = newWhs_pk;

	}

	/**
	 * 返回对象标识,用来唯一定位对象.
	 *
	 * 创建日期:2010-8-2
	 * @return String
	 */
	public String getPrimaryKey() {

		return whs_pk;

	}

	/**
	 * 设置对象标识,用来唯一定位对象.
	 *
	 * 创建日期:2010-8-2
	 * @param newWhs_pk  String    
	 */
	public void setPrimaryKey(String newWhs_pk) {

		whs_pk = newWhs_pk;

	}

	/**
	 * 返回数值对象的显示名称.
	 *
	 * 创建日期:2010-8-2
	 * @return java.lang.String 返回数值对象的显示名称.
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

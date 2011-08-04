/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.wds.dm.storetranscorp;

import java.util.ArrayList;
import nc.vo.pub.*;
import nc.vo.pub.lang.*;

/**
 * <b>分仓承运商绑定子表 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 创建日期:2011-6-9
 * 
 * @author author
 * @version Your Project 1.0
 */
public class StortranscorpBVO extends SuperVO {	
	private String pk_corp;
	public String pk_stordoc;//主键
	public Integer dr;
	public String vmemo;
	public UFDateTime ts;
	public String pk_wds_tanscorp_h;//承运商
	public UFDouble nsmallnum;//零担数量
	public String pk_storetranscorp;//子表主键
	public String careaid;//运输地区id
	public Integer ismalltype;//零担标准
	public Integer ismallprice;//零担运价方式
	//预留字段
	public String reserve1;
	public String reserve2;
	public String reserve3;
	public String reserve4;	
	public String reserve5;
	public UFDouble reserve6;
	public UFDouble reserve7;
	public UFDouble reserve8;
	public UFDouble reserve9;	
	public UFDouble reserve10;
	public UFDate reserve11;
	public UFDate reserve12;
	public UFDate reserve13;	
	public UFBoolean reserve14;
	public UFBoolean reserve15;
	public UFBoolean reserve16;	
	//自定义项
	public String vdef1;
	public String vdef2;
	public String vdef3;
	public String vdef4;	
	public String vdef5;
	public String vdef6;
	public String vdef7;
	public String vdef8;
	public String vdef9;
	public String vdef10;	
	/**
	 * 属性reserve5的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return String
	 */
	public String getReserve5() {
		return reserve5;
	}

	/**
	 * 属性reserve5的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newReserve5
	 *            String
	 */
	public void setReserve5(String newReserve5) {

		reserve5 = newReserve5;
	}

	/**
	 * 属性pk_stordoc的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return String
	 */
	public String getPk_stordoc() {
		return pk_stordoc;
	}

	/**
	 * 属性pk_stordoc的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newPk_stordoc
	 *            String
	 */
	public void setPk_stordoc(String newPk_stordoc) {

		pk_stordoc = newPk_stordoc;
	}

	/**
	 * 属性reserve4的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return String
	 */
	public String getReserve4() {
		return reserve4;
	}

	/**
	 * 属性reserve4的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newReserve4
	 *            String
	 */
	public void setReserve4(String newReserve4) {

		reserve4 = newReserve4;
	}

	/**
	 * 属性vdef4的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return String
	 */
	public String getVdef4() {
		return vdef4;
	}

	/**
	 * 属性vdef4的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newVdef4
	 *            String
	 */
	public void setVdef4(String newVdef4) {

		vdef4 = newVdef4;
	}

	/**
	 * 属性vdef7的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return String
	 */
	public String getVdef7() {
		return vdef7;
	}

	/**
	 * 属性vdef7的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newVdef7
	 *            String
	 */
	public void setVdef7(String newVdef7) {

		vdef7 = newVdef7;
	}

	/**
	 * 属性dr的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return Integer
	 */
	public Integer getDr() {
		return dr;
	}

	/**
	 * 属性dr的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newDr
	 *            Integer
	 */
	public void setDr(Integer newDr) {

		dr = newDr;
	}

	/**
	 * 属性vdef2的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return String
	 */
	public String getVdef2() {
		return vdef2;
	}

	/**
	 * 属性vdef2的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newVdef2
	 *            String
	 */
	public void setVdef2(String newVdef2) {

		vdef2 = newVdef2;
	}

	/**
	 * 属性reserve11的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return UFDate
	 */
	public UFDate getReserve11() {
		return reserve11;
	}

	/**
	 * 属性reserve11的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newReserve11
	 *            UFDate
	 */
	public void setReserve11(UFDate newReserve11) {

		reserve11 = newReserve11;
	}

	/**
	 * 属性reserve12的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return UFDate
	 */
	public UFDate getReserve12() {
		return reserve12;
	}

	/**
	 * 属性reserve12的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newReserve12
	 *            UFDate
	 */
	public void setReserve12(UFDate newReserve12) {

		reserve12 = newReserve12;
	}

	/**
	 * 属性vmemo的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return String
	 */
	public String getVmemo() {
		return vmemo;
	}

	/**
	 * 属性vmemo的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newVmemo
	 *            String
	 */
	public void setVmemo(String newVmemo) {

		vmemo = newVmemo;
	}

	/**
	 * 属性ts的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return UFDateTime
	 */
	public UFDateTime getTs() {
		return ts;
	}

	/**
	 * 属性ts的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newTs
	 *            UFDateTime
	 */
	public void setTs(UFDateTime newTs) {

		ts = newTs;
	}

	/**
	 * 属性reserve6的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return UFDouble
	 */
	public UFDouble getReserve6() {
		return reserve6;
	}

	/**
	 * 属性reserve6的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newReserve6
	 *            UFDouble
	 */
	public void setReserve6(UFDouble newReserve6) {

		reserve6 = newReserve6;
	}

	/**
	 * 属性vdef1的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return String
	 */
	public String getVdef1() {
		return vdef1;
	}

	/**
	 * 属性vdef1的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newVdef1
	 *            String
	 */
	public void setVdef1(String newVdef1) {

		vdef1 = newVdef1;
	}

	/**
	 * 属性pk_wds_tanscorp_h的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return String
	 */
	public String getPk_wds_tanscorp_h() {
		return pk_wds_tanscorp_h;
	}

	/**
	 * 属性pk_wds_tanscorp_h的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newPk_wds_tanscorp_h
	 *            String
	 */
	public void setPk_wds_tanscorp_h(String newPk_wds_tanscorp_h) {

		pk_wds_tanscorp_h = newPk_wds_tanscorp_h;
	}

	/**
	 * 属性reserve3的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return String
	 */
	public String getReserve3() {
		return reserve3;
	}

	/**
	 * 属性reserve3的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newReserve3
	 *            String
	 */
	public void setReserve3(String newReserve3) {

		reserve3 = newReserve3;
	}

	/**
	 * 属性reserve14的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return UFBoolean
	 */
	public UFBoolean getReserve14() {
		return reserve14;
	}

	/**
	 * 属性reserve14的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newReserve14
	 *            UFBoolean
	 */
	public void setReserve14(UFBoolean newReserve14) {

		reserve14 = newReserve14;
	}

	/**
	 * 属性reserve10的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return UFDouble
	 */
	public UFDouble getReserve10() {
		return reserve10;
	}

	/**
	 * 属性reserve10的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newReserve10
	 *            UFDouble
	 */
	public void setReserve10(UFDouble newReserve10) {

		reserve10 = newReserve10;
	}

	/**
	 * 属性reserve13的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return UFDate
	 */
	public UFDate getReserve13() {
		return reserve13;
	}

	/**
	 * 属性reserve13的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newReserve13
	 *            UFDate
	 */
	public void setReserve13(UFDate newReserve13) {

		reserve13 = newReserve13;
	}

	/**
	 * 属性nsmallnum的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return UFDouble
	 */
	public UFDouble getNsmallnum() {
		return nsmallnum;
	}

	/**
	 * 属性nsmallnum的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newNsmallnum
	 *            UFDouble
	 */
	public void setNsmallnum(UFDouble newNsmallnum) {

		nsmallnum = newNsmallnum;
	}

	/**
	 * 属性reserve8的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return UFDouble
	 */
	public UFDouble getReserve8() {
		return reserve8;
	}

	/**
	 * 属性reserve8的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newReserve8
	 *            UFDouble
	 */
	public void setReserve8(UFDouble newReserve8) {

		reserve8 = newReserve8;
	}

	/**
	 * 属性reserve9的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return UFDouble
	 */
	public UFDouble getReserve9() {
		return reserve9;
	}

	/**
	 * 属性reserve9的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newReserve9
	 *            UFDouble
	 */
	public void setReserve9(UFDouble newReserve9) {

		reserve9 = newReserve9;
	}

	/**
	 * 属性vdef3的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return String
	 */
	public String getVdef3() {
		return vdef3;
	}

	/**
	 * 属性vdef3的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newVdef3
	 *            String
	 */
	public void setVdef3(String newVdef3) {

		vdef3 = newVdef3;
	}

	/**
	 * 属性vdef9的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return String
	 */
	public String getVdef9() {
		return vdef9;
	}

	/**
	 * 属性vdef9的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newVdef9
	 *            String
	 */
	public void setVdef9(String newVdef9) {

		vdef9 = newVdef9;
	}

	/**
	 * 属性vdef8的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return String
	 */
	public String getVdef8() {
		return vdef8;
	}

	/**
	 * 属性vdef8的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newVdef8
	 *            String
	 */
	public void setVdef8(String newVdef8) {

		vdef8 = newVdef8;
	}

	/**
	 * 属性reserve1的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return String
	 */
	public String getReserve1() {
		return reserve1;
	}

	/**
	 * 属性reserve1的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newReserve1
	 *            String
	 */
	public void setReserve1(String newReserve1) {

		reserve1 = newReserve1;
	}

	/**
	 * 属性reserve16的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return UFBoolean
	 */
	public UFBoolean getReserve16() {
		return reserve16;
	}

	/**
	 * 属性reserve16的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newReserve16
	 *            UFBoolean
	 */
	public void setReserve16(UFBoolean newReserve16) {

		reserve16 = newReserve16;
	}

	/**
	 * 属性reserve7的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return UFDouble
	 */
	public UFDouble getReserve7() {
		return reserve7;
	}

	/**
	 * 属性reserve7的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newReserve7
	 *            UFDouble
	 */
	public void setReserve7(UFDouble newReserve7) {

		reserve7 = newReserve7;
	}

	/**
	 * 属性reserve15的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return UFBoolean
	 */
	public UFBoolean getReserve15() {
		return reserve15;
	}

	/**
	 * 属性reserve15的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newReserve15
	 *            UFBoolean
	 */
	public void setReserve15(UFBoolean newReserve15) {

		reserve15 = newReserve15;
	}

	/**
	 * 属性vdef6的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return String
	 */
	public String getVdef6() {
		return vdef6;
	}

	/**
	 * 属性vdef6的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newVdef6
	 *            String
	 */
	public void setVdef6(String newVdef6) {

		vdef6 = newVdef6;
	}

	/**
	 * 属性reserve2的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return String
	 */
	public String getReserve2() {
		return reserve2;
	}

	/**
	 * 属性reserve2的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newReserve2
	 *            String
	 */
	public void setReserve2(String newReserve2) {

		reserve2 = newReserve2;
	}

	/**
	 * 属性vdef10的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return String
	 */
	public String getVdef10() {
		return vdef10;
	}

	/**
	 * 属性vdef10的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newVdef10
	 *            String
	 */
	public void setVdef10(String newVdef10) {

		vdef10 = newVdef10;
	}

	/**
	 * 属性vdef5的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return String
	 */
	public String getVdef5() {
		return vdef5;
	}

	/**
	 * 属性vdef5的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newVdef5
	 *            String
	 */
	public void setVdef5(String newVdef5) {

		vdef5 = newVdef5;
	}

	/**
	 * 属性pk_storetranscorp的Getter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return String
	 */
	public String getPk_storetranscorp() {
		return pk_storetranscorp;
	}

	/**
	 * 属性pk_storetranscorp的Setter方法.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newPk_storetranscorp
	 *            String
	 */
	public void setPk_storetranscorp(String newPk_storetranscorp) {

		pk_storetranscorp = newPk_storetranscorp;
	}

	/**
	 * 验证对象各属性之间的数据逻辑正确性.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @exception nc.vo.pub.ValidationException
	 *                如果验证失败,抛出 ValidationException,对错误进行解释.
	 */
	public void validate() throws ValidationException {

		ArrayList errFields = new ArrayList(); // errFields record those null

		// fields that cannot be null.
		// 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:

		if (pk_storetranscorp == null) {
			errFields.add(new String("pk_storetranscorp"));
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
	 * <p>
	 * 取得父VO主键字段.
	 * <p>
	 * 创建日期:2011-6-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {

		return "pk_stordoc";

	}

	/**
	 * <p>
	 * 取得表主键.
	 * <p>
	 * 创建日期:2011-6-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "pk_storetranscorp";
	}

	/**
	 * <p>
	 * 返回表名称.
	 * <p>
	 * 创建日期:2011-6-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "wds_stortranscorp_b";
	}

	/**
	 * 按照默认方式创建构造子.
	 * 
	 * 创建日期:2011-6-9
	 */
	public StortranscorpBVO() {

		super();
	}

	/**
	 * 使用主键进行初始化的构造子.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newPk_storetranscorp
	 *            主键值
	 */
	public StortranscorpBVO(String newPk_storetranscorp) {

		// 为主键字段赋值:
		pk_storetranscorp = newPk_storetranscorp;

	}

	/**
	 * 返回对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return String
	 */
	public String getPrimaryKey() {

		return pk_storetranscorp;

	}

	/**
	 * 设置对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @param newPk_storetranscorp
	 *            String
	 */
	public void setPrimaryKey(String newPk_storetranscorp) {

		pk_storetranscorp = newPk_storetranscorp;

	}

	/**
	 * 返回数值对象的显示名称.
	 * 
	 * 创建日期:2011-6-9
	 * 
	 * @return java.lang.String 返回数值对象的显示名称.
	 */
	public String getEntityName() {

		return "wds_stortranscorp_b";

	}

	public String getCareaid() {
		return careaid;
	}

	public void setCareaid(String careaid) {
		this.careaid = careaid;
	}

	public Integer getIsmalltype() {
		return ismalltype;
	}

	public void setIsmalltype(Integer ismalltype) {
		this.ismalltype = ismalltype;
	}

	public Integer getIsmallprice() {
		return ismallprice;
	}

	public void setIsmallprice(Integer ismallprice) {
		this.ismallprice = ismallprice;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getPk_corp() {
		return pk_corp;
	}
}

package nc.vo.load.tag;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

/**
 * <b> 班组基本档案 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 * 创建日期:2011-3-28
 * @author author
 * @version Your Project 1.0
 */
public class TagDocVO extends SuperVO {

	//主键
	public String pk_taghid;
	//班组名称
	public String vtagname;
	//班组编码
	public String vtagid;
	//班组地址
	public String vtagdress;
	//备注
	public String vmemo;
	//时间戳
    public UFDateTime ts;
    //删除标志位
    public Integer dr;
	public String vdef1;
	public String vdef2;
	public String vdef3;
	public String vdef4;
	public String vdef5;
	public String vdef6;
	public String reserve1;
	public String reserve2;
	public String reserve3;
	public String reserve4;
	public String reserve5;
	public String reserve6;
	public String reserve7;
	public String reserve8;
	public String reserve9;
	public String reserve10;
	public String reserve11;
	public String reserve12;
	public String reserve13;
	public String reserve14;
	public String reserve15;
	public String reserve16;
	
	
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_taghid";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "wds_tagdoc";
	}

	public String getPk_taghid() {
		return pk_taghid;
	}

	public void setPk_taghid(String pk_taghid) {
		this.pk_taghid = pk_taghid;
	}

	public String getVtagname() {
		return vtagname;
	}

	public void setVtagname(String vtagname) {
		this.vtagname = vtagname;
	}

	public String getVtagid() {
		return vtagid;
	}

	public void setVtagid(String vtagid) {
		this.vtagid = vtagid;
	}

	public String getVtagdress() {
		return vtagdress;
	}

	public void setVtagdress(String vtagdress) {
		this.vtagdress = vtagdress;
	}

	public String getVmemo() {
		return vmemo;
	}

	public void setVmemo(String vmemo) {
		this.vmemo = vmemo;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getVdef1() {
		return vdef1;
	}

	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}

	public String getVdef2() {
		return vdef2;
	}

	public void setVdef2(String vdef2) {
		this.vdef2 = vdef2;
	}

	public String getVdef3() {
		return vdef3;
	}

	public void setVdef3(String vdef3) {
		this.vdef3 = vdef3;
	}

	public String getVdef4() {
		return vdef4;
	}

	public void setVdef4(String vdef4) {
		this.vdef4 = vdef4;
	}

	public String getVdef5() {
		return vdef5;
	}

	public void setVdef5(String vdef5) {
		this.vdef5 = vdef5;
	}

	public String getVdef6() {
		return vdef6;
	}

	public void setVdef6(String vdef6) {
		this.vdef6 = vdef6;
	}

	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	public String getReserve3() {
		return reserve3;
	}

	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}

	public String getReserve4() {
		return reserve4;
	}

	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}

	public String getReserve5() {
		return reserve5;
	}

	public void setReserve5(String reserve5) {
		this.reserve5 = reserve5;
	}

	public String getReserve6() {
		return reserve6;
	}

	public void setReserve6(String reserve6) {
		this.reserve6 = reserve6;
	}

	public String getReserve7() {
		return reserve7;
	}

	public void setReserve7(String reserve7) {
		this.reserve7 = reserve7;
	}

	public String getReserve8() {
		return reserve8;
	}

	public void setReserve8(String reserve8) {
		this.reserve8 = reserve8;
	}

	public String getReserve9() {
		return reserve9;
	}

	public void setReserve9(String reserve9) {
		this.reserve9 = reserve9;
	}

	public String getReserve10() {
		return reserve10;
	}

	public void setReserve10(String reserve10) {
		this.reserve10 = reserve10;
	}

	public String getReserve11() {
		return reserve11;
	}

	public void setReserve11(String reserve11) {
		this.reserve11 = reserve11;
	}

	public String getReserve12() {
		return reserve12;
	}

	public void setReserve12(String reserve12) {
		this.reserve12 = reserve12;
	}

	public String getReserve13() {
		return reserve13;
	}

	public void setReserve13(String reserve13) {
		this.reserve13 = reserve13;
	}

	public String getReserve14() {
		return reserve14;
	}

	public void setReserve14(String reserve14) {
		this.reserve14 = reserve14;
	}

	public String getReserve15() {
		return reserve15;
	}

	public void setReserve15(String reserve15) {
		this.reserve15 = reserve15;
	}

	public String getReserve16() {
		return reserve16;
	}

	public void setReserve16(String reserve16) {
		this.reserve16 = reserve16;
	}
	
	public TagDocVO() {
		
		   super();	
}    

     /**
      * 使用主键进行初始化的构造子.
      *
      * 创建日期:2011-3-28
      * @param newPk_sendplanin 主键值
      */
	public TagDocVO(String newPk_taghid) {
	
		// 为主键字段赋值:
		pk_taghid = newPk_taghid;

	}


	/**
	 * 返回对象标识,用来唯一定位对象.
	 *
	 * 创建日期:2011-3-28
	 * @return String
	 */
	public String getPrimaryKey() {
			
		return pk_taghid;

	}

	/**
	 * 设置对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2011-3-28
	 * 
	 * @param newPk_sendplanin
	 *            String
	 */
	public void setPrimaryKey(String newpk_taghid) {

		pk_taghid = newpk_taghid;

	}

	/**
	 * 返回数值对象的显示名称.
	 * 
	 * 创建日期:2011-3-28
	 * 
	 * @return java.lang.String 返回数值对象的显示名称.
	 */
	public String getEntityName() {

		return "wds_tagdoc";

	} 

}





package nc.vo.load.tag;

import java.util.ArrayList;

import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

/**
 * <b>班组基本信息子表 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 * 创建日期:2011-3-23
 * @author author
 * @version Your Project 1.0
 */

public class TagDocBVO extends SuperVO {
	
	public UFBoolean fistag;
	public String vemployeeid;
	public String vemployeename;
	public String pk_taghid;
	public String pk_tagbid;
	public String pk_employeeid;
	public String vaddress;
	public String vcontact1;
	public String vcontact2;
	public String vcontact3;
	public String vmemo;
	public UFDateTime ts;
	public Integer dr;
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
	
	

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_tagbid";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_taghid";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "wds_tagdoc_b";
	}

	public String getPk_taghid() {
		return pk_taghid;
	}

	public void setPk_taghid(String pk_taghid) {
		this.pk_taghid = pk_taghid;
	}

	public String getPk_tagbid() {
		return pk_tagbid;
	}

	public void setPk_tagbid(String pk_tagbid) {
		this.pk_tagbid = pk_tagbid;
	}

	public UFBoolean isFistag() {
		return fistag;
	}

	public void setFistag(UFBoolean fistag) {
		this.fistag = fistag;
	}

	public String getPk_employeeid() {
		return pk_employeeid;
	}

	public void setPk_employeeid(String pk_employeeid) {
		this.pk_employeeid = pk_employeeid;
	}

	public String getVaddress() {
		return vaddress;
	}

	public void setVaddress(String vaddress) {
		this.vaddress = vaddress;
	}

	public String getVcontact1() {
		return vcontact1;
	}

	public void setVcontact1(String vcontact1) {
		this.vcontact1 = vcontact1;
	}

	public String getVcontact2() {
		return vcontact2;
	}

	public void setVcontact2(String vcontact2) {
		this.vcontact2 = vcontact2;
	}

	public String getVcontact3() {
		return vcontact3;
	}

	public void setVcontact3(String vcontact3) {
		this.vcontact3 = vcontact3;
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

	public String getVdef7() {
		return vdef7;
	}

	public void setVdef7(String vdef7) {
		this.vdef7 = vdef7;
	}

	public String getVdef8() {
		return vdef8;
	}

	public void setVdef8(String vdef8) {
		this.vdef8 = vdef8;
	}

	public String getVdef9() {
		return vdef9;
	}

	public void setVdef9(String vdef9) {
		this.vdef9 = vdef9;
	}

	public String getVdef10() {
		return vdef10;
	}

	public void setVdef10(String vdef10) {
		this.vdef10 = vdef10;
	}
	
	
	 public void validate() throws ValidationException {
			
		 	ArrayList errFields = new ArrayList(); // errFields record those null

	                                                      // fields that cannot be null.
	       		  // 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:
		
		   		if (pk_tagbid == null) {
				errFields.add(new String("pk_tagbid"));
				  }	
		   	
		    StringBuffer message = new StringBuffer();
			message.append("下列字段不能为空:");
			if (errFields.size() > 0) {
			String[] temp = (String[]) errFields.toArray(new String[0]);
			message.append(temp[0]);
			for ( int i= 1; i < temp.length; i++ ) {
				message.append(",");
				message.append(temp[i]);
			}
			throw new NullFieldException(message.toString());
			}
		 }
	
	public TagDocBVO() {

		super();
	}

	/**
	 * 使用主键进行初始化的构造子.
	 * 
	 * 创建日期:2011-3-23
	 * 
	 * @param newPk_sendplanin_b
	 *            主键值
	 */
	public TagDocBVO(String newpk_tagbid) {

		// 为主键字段赋值:
		pk_tagbid = newpk_tagbid;

	}

	/**
	 * 返回对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2011-3-23
	 * 
	 * @return String
	 */
	public String getPrimaryKey() {

		return pk_tagbid;

	}

	/**
	 * 设置对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2011-3-23
	 * 
	 * @param newPk_sendplanin_b
	 *            String
	 */
	public void setPrimaryKey(String newPk_tagbid) {

		pk_tagbid = newPk_tagbid;

	}

	/**
	 * 返回数值对象的显示名称.
	 * 
	 * 创建日期:2011-3-23
	 * 
	 * @return java.lang.String 返回数值对象的显示名称.
	 */
	public String getEntityName() {

		return "wds_tagdoc_b";

	}

	public String getVemployeeid() {
		return vemployeeid;
	}

	public void setVemployeeid(String vemployeeid) {
		this.vemployeeid = vemployeeid;
	}

	public String getVemployeename() {
		return vemployeename;
	}

	public void setVemployeename(String vemployeename) {
		this.vemployeename = vemployeename;
	}

	public UFBoolean getFistag() {
		return fistag;
	}


}

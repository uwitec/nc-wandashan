  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.wds.ic.zgjz;
   	
	import java.util.ArrayList;
	import nc.vo.pub.*;
	import nc.vo.pub.lang.*;
	
/**
 * <b> 暂估记账子表vo </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 * 创建日期:2011-12-15
 * @author author
 * @version Your Project 1.0
 */
     public class ZgjzBVO extends SuperVO {
           
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		    public String pk_wds_zgjz_b;//子表主键
		    public String pk_wds_zgjz_h;//父表主键
	         public String pk_invmandoc;//存货管理id
	         public String pk_invbasdoc;//存货基本id
	         public UFDouble nlastnum;//上月欠发主数量
	         public UFDouble nlastassnum;//上月欠发辅数量
	         public UFDouble nreducnum;//本月冲减上月
	         public UFDouble nreducassnum;//本月冲减上月辅数量
	         public UFDouble noutnum;//安排主数量
	         public UFDouble noutassnum;//安排辅数量
	         public String unit;//主计量单位
	         public String assunit;//辅计量单位
	         public UFDouble hsl;//换算率
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
         	public UFDateTime ts;
         	public Integer dr;
         	//备注
         	public String vmemo;
         	//上下游信息字段
         	public String vlastbillcode;
         	public String vlastbilltype;
         	public String vlastbillid;
         	public String vlastbillrowid;
         	public String csourcebillcode;
         	public String csourcetype;
         	public String csourcebillhid;
         	public String csourcebillbid;

      
    
        /**
	   * 属性reserve5的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return String
	   */
	 public String getReserve5() {
		 return reserve5;
	  }   
	  
     /**
	   * 属性reserve5的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newReserve5 String
	   */
	public void setReserve5(String newReserve5) {
		
		reserve5 = newReserve5;
	 } 	  
       
        /**
	   * 属性vdef4的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return String
	   */
	 public String getVdef4() {
		 return vdef4;
	  }   
	  
     /**
	   * 属性vdef4的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newVdef4 String
	   */
	public void setVdef4(String newVdef4) {
		
		vdef4 = newVdef4;
	 } 	  
       
        /**
	   * 属性reserve4的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return String
	   */
	 public String getReserve4() {
		 return reserve4;
	  }   
	  
     /**
	   * 属性reserve4的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newReserve4 String
	   */
	public void setReserve4(String newReserve4) {
		
		reserve4 = newReserve4;
	 } 	  
       
        /**
	   * 属性pk_wds_zgjz_b的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return String
	   */
	 public String getPk_wds_zgjz_b() {
		 return pk_wds_zgjz_b;
	  }   
	  
     /**
	   * 属性pk_wds_zgjz_b的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newPk_wds_zgjz_b String
	   */
	public void setPk_wds_zgjz_b(String newPk_wds_zgjz_b) {
		
		pk_wds_zgjz_b = newPk_wds_zgjz_b;
	 } 	  
       
        /**
	   * 属性vdef7的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return String
	   */
	 public String getVdef7() {
		 return vdef7;
	  }   
	  
     /**
	   * 属性vdef7的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newVdef7 String
	   */
	public void setVdef7(String newVdef7) {
		
		vdef7 = newVdef7;
	 } 	  
       
        /**
	   * 属性dr的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return Integer
	   */
	 public Integer getDr() {
		 return dr;
	  }   
	  
     /**
	   * 属性dr的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newDr Integer
	   */
	public void setDr(Integer newDr) {
		
		dr = newDr;
	 } 	  
       
        /**
	   * 属性pk_invbasdoc的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return String
	   */
	 public String getPk_invbasdoc() {
		 return pk_invbasdoc;
	  }   
	  
     /**
	   * 属性pk_invbasdoc的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newPk_invbasdoc String
	   */
	public void setPk_invbasdoc(String newPk_invbasdoc) {
		
		pk_invbasdoc = newPk_invbasdoc;
	 } 	  
       
        /**
	   * 属性vdef2的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return String
	   */
	 public String getVdef2() {
		 return vdef2;
	  }   
	  
     /**
	   * 属性vdef2的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newVdef2 String
	   */
	public void setVdef2(String newVdef2) {
		
		vdef2 = newVdef2;
	 } 	  
       
        /**
	   * 属性reserve11的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return UFDate
	   */
	 public UFDate getReserve11() {
		 return reserve11;
	  }   
	  
     /**
	   * 属性reserve11的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newReserve11 UFDate
	   */
	public void setReserve11(UFDate newReserve11) {
		
		reserve11 = newReserve11;
	 } 	  
       
        /**
	   * 属性reserve12的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return UFDate
	   */
	 public UFDate getReserve12() {
		 return reserve12;
	  }   
	  
     /**
	   * 属性reserve12的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newReserve12 UFDate
	   */
	public void setReserve12(UFDate newReserve12) {
		
		reserve12 = newReserve12;
	 } 	  
       
        /**
	   * 属性vmemo的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return String
	   */
	 public String getVmemo() {
		 return vmemo;
	  }   
	  
     /**
	   * 属性vmemo的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newVmemo String
	   */
	public void setVmemo(String newVmemo) {
		
		vmemo = newVmemo;
	 } 	  
       
        /**
	   * 属性ts的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return UFDateTime
	   */
	 public UFDateTime getTs() {
		 return ts;
	  }   
	  
     /**
	   * 属性ts的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newTs UFDateTime
	   */
	public void setTs(UFDateTime newTs) {
		
		ts = newTs;
	 } 	  
       
        /**
	   * 属性noutnum的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return UFDouble
	   */
	 public UFDouble getNoutnum() {
		 return noutnum;
	  }   
	  
     /**
	   * 属性noutnum的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newNoutnum UFDouble
	   */
	public void setNoutnum(UFDouble newNoutnum) {
		
		noutnum = newNoutnum;
	 } 	  
       
        /**
	   * 属性vdef1的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return String
	   */
	 public String getVdef1() {
		 return vdef1;
	  }   
	  
     /**
	   * 属性vdef1的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newVdef1 String
	   */
	public void setVdef1(String newVdef1) {
		
		vdef1 = newVdef1;
	 } 	  
       
        /**
	   * 属性reserve6的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return UFDouble
	   */
	 public UFDouble getReserve6() {
		 return reserve6;
	  }   
	  
     /**
	   * 属性reserve6的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newReserve6 UFDouble
	   */
	public void setReserve6(UFDouble newReserve6) {
		
		reserve6 = newReserve6;
	 } 	  
       
        /**
	   * 属性reserve3的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return String
	   */
	 public String getReserve3() {
		 return reserve3;
	  }   
	  
     /**
	   * 属性reserve3的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newReserve3 String
	   */
	public void setReserve3(String newReserve3) {
		
		reserve3 = newReserve3;
	 } 	  
       
        /**
	   * 属性reserve14的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return UFBoolean
	   */
	 public UFBoolean getReserve14() {
		 return reserve14;
	  }   
	  
     /**
	   * 属性reserve14的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newReserve14 UFBoolean
	   */
	public void setReserve14(UFBoolean newReserve14) {
		
		reserve14 = newReserve14;
	 } 	  
       
        /**
	   * 属性nlastassnum的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return UFDouble
	   */
	 public UFDouble getNlastassnum() {
		 return nlastassnum;
	  }   
	  
     /**
	   * 属性nlastassnum的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newNlastassnum UFDouble
	   */
	public void setNlastassnum(UFDouble newNlastassnum) {
		
		nlastassnum = newNlastassnum;
	 } 	  
       
        /**
	   * 属性reserve13的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return UFDate
	   */
	 public UFDate getReserve13() {
		 return reserve13;
	  }   
	  
     /**
	   * 属性reserve13的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newReserve13 UFDate
	   */
	public void setReserve13(UFDate newReserve13) {
		
		reserve13 = newReserve13;
	 } 	  
       
        /**
	   * 属性nreducassnum的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return UFDouble
	   */
	 public UFDouble getNreducassnum() {
		 return nreducassnum;
	  }   
	  
     /**
	   * 属性nreducassnum的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newNreducassnum UFDouble
	   */
	public void setNreducassnum(UFDouble newNreducassnum) {
		
		nreducassnum = newNreducassnum;
	 } 	  
       
        /**
	   * 属性reserve10的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return UFDouble
	   */
	 public UFDouble getReserve10() {
		 return reserve10;
	  }   
	  
     /**
	   * 属性reserve10的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newReserve10 UFDouble
	   */
	public void setReserve10(UFDouble newReserve10) {
		
		reserve10 = newReserve10;
	 } 	  
       
        /**
	   * 属性pk_wds_zgjz_h的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return String
	   */
	 public String getPk_wds_zgjz_h() {
		 return pk_wds_zgjz_h;
	  }   
	  
     /**
	   * 属性pk_wds_zgjz_h的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newPk_wds_zgjz_h String
	   */
	public void setPk_wds_zgjz_h(String newPk_wds_zgjz_h) {
		
		pk_wds_zgjz_h = newPk_wds_zgjz_h;
	 } 	  
       
        /**
	   * 属性vdef3的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return String
	   */
	 public String getVdef3() {
		 return vdef3;
	  }   
	  
     /**
	   * 属性vdef3的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newVdef3 String
	   */
	public void setVdef3(String newVdef3) {
		
		vdef3 = newVdef3;
	 } 	  
       
        /**
	   * 属性reserve8的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return UFDouble
	   */
	 public UFDouble getReserve8() {
		 return reserve8;
	  }   
	  
     /**
	   * 属性reserve8的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newReserve8 UFDouble
	   */
	public void setReserve8(UFDouble newReserve8) {
		
		reserve8 = newReserve8;
	 } 	  
       
        /**
	   * 属性reserve9的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return UFDouble
	   */
	 public UFDouble getReserve9() {
		 return reserve9;
	  }   
	  
     /**
	   * 属性reserve9的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newReserve9 UFDouble
	   */
	public void setReserve9(UFDouble newReserve9) {
		
		reserve9 = newReserve9;
	 } 	  
       
        /**
	   * 属性vdef9的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return String
	   */
	 public String getVdef9() {
		 return vdef9;
	  }   
	  
     /**
	   * 属性vdef9的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newVdef9 String
	   */
	public void setVdef9(String newVdef9) {
		
		vdef9 = newVdef9;
	 } 	  
       
        /**
	   * 属性noutassnum的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return UFDouble
	   */
	 public UFDouble getNoutassnum() {
		 return noutassnum;
	  }   
	  
     /**
	   * 属性noutassnum的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newNoutassnum UFDouble
	   */
	public void setNoutassnum(UFDouble newNoutassnum) {
		
		noutassnum = newNoutassnum;
	 } 	  
       
        /**
	   * 属性vdef8的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return String
	   */
	 public String getVdef8() {
		 return vdef8;
	  }   
	  
     /**
	   * 属性vdef8的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newVdef8 String
	   */
	public void setVdef8(String newVdef8) {
		
		vdef8 = newVdef8;
	 } 	  
       
        /**
	   * 属性reserve1的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return String
	   */
	 public String getReserve1() {
		 return reserve1;
	  }   
	  
     /**
	   * 属性reserve1的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newReserve1 String
	   */
	public void setReserve1(String newReserve1) {
		
		reserve1 = newReserve1;
	 } 	  
       
        /**
	   * 属性nreducnum的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return UFDouble
	   */
	 public UFDouble getNreducnum() {
		 return nreducnum;
	  }   
	  
     /**
	   * 属性nreducnum的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newNreducnum UFDouble
	   */
	public void setNreducnum(UFDouble newNreducnum) {
		
		nreducnum = newNreducnum;
	 } 	  
       
        /**
	   * 属性nlastnum的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return UFDouble
	   */
	 public UFDouble getNlastnum() {
		 return nlastnum;
	  }   
	  
     /**
	   * 属性nlastnum的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newNlastnum UFDouble
	   */
	public void setNlastnum(UFDouble newNlastnum) {
		
		nlastnum = newNlastnum;
	 } 	  
       
        /**
	   * 属性reserve16的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return UFBoolean
	   */
	 public UFBoolean getReserve16() {
		 return reserve16;
	  }   
	  
     /**
	   * 属性reserve16的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newReserve16 UFBoolean
	   */
	public void setReserve16(UFBoolean newReserve16) {
		
		reserve16 = newReserve16;
	 } 	  
       
        /**
	   * 属性reserve7的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return UFDouble
	   */
	 public UFDouble getReserve7() {
		 return reserve7;
	  }   
	  
     /**
	   * 属性reserve7的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newReserve7 UFDouble
	   */
	public void setReserve7(UFDouble newReserve7) {
		
		reserve7 = newReserve7;
	 } 	  
       
        /**
	   * 属性reserve15的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return UFBoolean
	   */
	 public UFBoolean getReserve15() {
		 return reserve15;
	  }   
	  
     /**
	   * 属性reserve15的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newReserve15 UFBoolean
	   */
	public void setReserve15(UFBoolean newReserve15) {
		
		reserve15 = newReserve15;
	 } 	  
       
        /**
	   * 属性vdef6的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return String
	   */
	 public String getVdef6() {
		 return vdef6;
	  }   
	  
     /**
	   * 属性vdef6的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newVdef6 String
	   */
	public void setVdef6(String newVdef6) {
		
		vdef6 = newVdef6;
	 } 	  
       
        /**
	   * 属性pk_invmandoc的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return String
	   */
	 public String getPk_invmandoc() {
		 return pk_invmandoc;
	  }   
	  
     /**
	   * 属性pk_invmandoc的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newPk_invmandoc String
	   */
	public void setPk_invmandoc(String newPk_invmandoc) {
		
		pk_invmandoc = newPk_invmandoc;
	 } 	  
       
        /**
	   * 属性reserve2的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return String
	   */
	 public String getReserve2() {
		 return reserve2;
	  }   
	  
     /**
	   * 属性reserve2的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newReserve2 String
	   */
	public void setReserve2(String newReserve2) {
		
		reserve2 = newReserve2;
	 } 	  
       
        /**
	   * 属性vdef10的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return String
	   */
	 public String getVdef10() {
		 return vdef10;
	  }   
	  
     /**
	   * 属性vdef10的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newVdef10 String
	   */
	public void setVdef10(String newVdef10) {
		
		vdef10 = newVdef10;
	 } 	  
       
        /**
	   * 属性vdef5的Getter方法.
	   *
	   * 创建日期:2011-12-15
	   * @return String
	   */
	 public String getVdef5() {
		 return vdef5;
	  }   
	  
     /**
	   * 属性vdef5的Setter方法.
	   *
	   * 创建日期:2011-12-15
	   * @param newVdef5 String
	   */
	public void setVdef5(String newVdef5) {
		
		vdef5 = newVdef5;
	 } 	  
       
       
    /**
	  * 验证对象各属性之间的数据逻辑正确性.
	  *
	  * 创建日期:2011-12-15
	  * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
	  * ValidationException,对错误进行解释.
	 */
	 public void validate() throws ValidationException {
	
	 	ArrayList errFields = new ArrayList(); // errFields record those null

                                                      // fields that cannot be null.
       		  // 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:
	
	   		if (pk_wds_zgjz_b == null) {
			errFields.add(new String("pk_wds_zgjz_b"));
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
			   
       
   	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2011-12-15
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 		return "pk_wds_zgjz_h";
	 	
	}   
    
    /**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2011-12-15
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pk_wds_zgjz_b";
	 	}
    
	/**
      * <p>返回表名称.
	  * <p>
	  * 创建日期:2011-12-15
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "wds_zgjz_b";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2011-12-15
	  */
	public ZgjzBVO() {
			
			   super();	
	  }    
    
            /**
	 * 使用主键进行初始化的构造子.
	 *
	 * 创建日期:2011-12-15
	 * @param newPk_wds_zgjz_b 主键值
	 */
	 public ZgjzBVO(String newPk_wds_zgjz_b) {
		
		// 为主键字段赋值:
		 pk_wds_zgjz_b = newPk_wds_zgjz_b;
	
    	}
    
     
     /**
	  * 返回对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2011-12-15
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_wds_zgjz_b;
	   
	   }

     /**
	  * 设置对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2011-12-15
	  * @param newPk_wds_zgjz_b  String    
	  */
	 public void setPrimaryKey(String newPk_wds_zgjz_b) {
				
				pk_wds_zgjz_b = newPk_wds_zgjz_b; 
				
	 } 
           
	  /**
       * 返回数值对象的显示名称.
	   *
	   * 创建日期:2011-12-15
	   * @return java.lang.String 返回数值对象的显示名称.
	   */
	 public String getEntityName() {
				
	   return "wds_zgjz_b"; 
				
	 }

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getAssunit() {
		return assunit;
	}

	public void setAssunit(String assunit) {
		this.assunit = assunit;
	}

	public UFDouble getHsl() {
		return hsl;
	}

	public void setHsl(UFDouble hsl) {
		this.hsl = hsl;
	}

	public String getVlastbillcode() {
		return vlastbillcode;
	}

	public void setVlastbillcode(String vlastbillcode) {
		this.vlastbillcode = vlastbillcode;
	}

	public String getVlastbilltype() {
		return vlastbilltype;
	}

	public void setVlastbilltype(String vlastbilltype) {
		this.vlastbilltype = vlastbilltype;
	}

	public String getVlastbillid() {
		return vlastbillid;
	}

	public void setVlastbillid(String vlastbillid) {
		this.vlastbillid = vlastbillid;
	}

	public String getVlastbillrowid() {
		return vlastbillrowid;
	}

	public void setVlastbillrowid(String vlastbillrowid) {
		this.vlastbillrowid = vlastbillrowid;
	}

	public String getCsourcebillcode() {
		return csourcebillcode;
	}

	public void setCsourcebillcode(String csourcebillcode) {
		this.csourcebillcode = csourcebillcode;
	}

	public String getCsourcetype() {
		return csourcetype;
	}

	public void setCsourcetype(String csourcetype) {
		this.csourcetype = csourcetype;
	}

	public String getCsourcebillhid() {
		return csourcebillhid;
	}

	public void setCsourcebillhid(String csourcebillhid) {
		this.csourcebillhid = csourcebillhid;
	}

	public String getCsourcebillbid() {
		return csourcebillbid;
	}

	public void setCsourcebillbid(String csourcebillbid) {
		this.csourcebillbid = csourcebillbid;
	} 
} 

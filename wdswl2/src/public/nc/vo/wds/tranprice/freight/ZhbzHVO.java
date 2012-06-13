  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.wds.tranprice.freight;
   	
	import java.util.ArrayList;
	import nc.vo.pub.*;
	import nc.vo.pub.lang.*;
	
/**
 * <b> 承运商折合标准主表VO </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 * 创建日期:2011-11-23
 * @author author
 * @version Your Project 1.0
 */
     public class ZhbzHVO extends SuperVO {
           
             /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
			public String reserve5;
             public String pk_corp;
             public String reserve4;
             public String vdef4;
             public UFDate dmakedate;
             public Integer dr;
             public String voperatorid;
             public String vdef20;
             public String pk_billtype;
             public String vdef2;
             public UFDate reserve11;
             public UFDate reserve12;
             public String vmemo;
             public UFDateTime ts;
             public String reserve3;
             public UFDouble tuneunits;//调重单位
             public UFDouble standardtune;//调重标准
             public String pk_zhbz_h;//主表主键
             public String carriersid;//承运商
             public String pk_defdoc6;
             public String vdef19;
             public UFBoolean reserve14;
             public UFDate dbilldate;
             public String vbillno;
             public String pk_defdoc4;
             public UFDouble reserve10;
             public UFDate reserve13;
             public String pk_defdoc5;
             public UFDouble reserve8;
             public UFDouble reserve9;
             public String pk_defdoc3;
             public String reserve1;
             public UFBoolean reserve16;
             public String pk_defdoc2;
             public UFBoolean reserve15;
             public String reserve2;
            
             public static final String  RESERVE5="reserve5";   
             public static final String  PK_CORP="pk_corp";   
             public static final String  RESERVE4="reserve4";   
             public static final String  VDEF4="vdef4";   
             public static final String  DMAKEDATE="dmakedate";   
             public static final String  DR="dr";   
             public static final String  PK_ZHBZ_H="pk_zhbz_h";   
             public static final String  VOPERATORID="voperatorid";   
             public static final String  VDEF20="vdef20";   
             public static final String  PK_BILLTYPE="pk_billtype";   
             public static final String  VDEF2="vdef2";   
             public static final String  RESERVE11="reserve11";   
             public static final String  RESERVE12="reserve12";   
             public static final String  VMEMO="vmemo";   
             public static final String  TS="ts";   
             public static final String  RESERVE3="reserve3";   
             public static final String  TUNEUNITS="tuneunits";   
             public static final String  PK_DEFDOC6="pk_defdoc6";   
             public static final String  VDEF19="vdef19";   
             public static final String  RESERVE14="reserve14";   
             public static final String  DBILLDATE="dbilldate";   
             public static final String  VBILLNO="vbillno";   
             public static final String  PK_DEFDOC4="pk_defdoc4";   
             public static final String  RESERVE10="reserve10";   
             public static final String  RESERVE13="reserve13";   
             public static final String  PK_DEFDOC5="pk_defdoc5";   
             public static final String  STANDARDTUNE="standardtune";   
             public static final String  RESERVE8="reserve8";   
             public static final String  RESERVE9="reserve9";   
             public static final String  PK_DEFDOC3="pk_defdoc3";   
             public static final String  CARRIERSID="carriersid";   
             public static final String  RESERVE1="reserve1";   
             public static final String  RESERVE16="reserve16";   
             public static final String  PK_DEFDOC2="pk_defdoc2";   
             public static final String  RESERVE15="reserve15";   
             public static final String  RESERVE2="reserve2";   
      
    
        /**
	   * 属性reserve5的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return String
	   */
	 public String getReserve5() {
		 return reserve5;
	  }   
	  
     /**
	   * 属性reserve5的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newReserve5 String
	   */
	public void setReserve5(String newReserve5) {
		
		reserve5 = newReserve5;
	 } 	  
       
        /**
	   * 属性pk_corp的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return String
	   */
	 public String getPk_corp() {
		 return pk_corp;
	  }   
	  
     /**
	   * 属性pk_corp的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newPk_corp String
	   */
	public void setPk_corp(String newPk_corp) {
		
		pk_corp = newPk_corp;
	 } 	  
       
        /**
	   * 属性reserve4的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return String
	   */
	 public String getReserve4() {
		 return reserve4;
	  }   
	  
     /**
	   * 属性reserve4的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newReserve4 String
	   */
	public void setReserve4(String newReserve4) {
		
		reserve4 = newReserve4;
	 } 	  
       
        /**
	   * 属性vdef4的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return String
	   */
	 public String getVdef4() {
		 return vdef4;
	  }   
	  
     /**
	   * 属性vdef4的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newVdef4 String
	   */
	public void setVdef4(String newVdef4) {
		
		vdef4 = newVdef4;
	 } 	  
       
        /**
	   * 属性dmakedate的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return UFDate
	   */
	 public UFDate getDmakedate() {
		 return dmakedate;
	  }   
	  
     /**
	   * 属性dmakedate的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newDmakedate UFDate
	   */
	public void setDmakedate(UFDate newDmakedate) {
		
		dmakedate = newDmakedate;
	 } 	  
       
        /**
	   * 属性dr的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return Integer
	   */
	 public Integer getDr() {
		 return dr;
	  }   
	  
     /**
	   * 属性dr的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newDr Integer
	   */
	public void setDr(Integer newDr) {
		
		dr = newDr;
	 } 	  
       
        /**
	   * 属性pk_zhbz_h的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return String
	   */
	 public String getPk_zhbz_h() {
		 return pk_zhbz_h;
	  }   
	  
     /**
	   * 属性pk_zhbz_h的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newPk_zhbz_h String
	   */
	public void setPk_zhbz_h(String newPk_zhbz_h) {
		
		pk_zhbz_h = newPk_zhbz_h;
	 } 	  
       
        /**
	   * 属性voperatorid的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return String
	   */
	 public String getVoperatorid() {
		 return voperatorid;
	  }   
	  
     /**
	   * 属性voperatorid的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newVoperatorid String
	   */
	public void setVoperatorid(String newVoperatorid) {
		
		voperatorid = newVoperatorid;
	 } 	  
       
        /**
	   * 属性vdef20的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return String
	   */
	 public String getVdef20() {
		 return vdef20;
	  }   
	  
     /**
	   * 属性vdef20的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newVdef20 String
	   */
	public void setVdef20(String newVdef20) {
		
		vdef20 = newVdef20;
	 } 	  
       
        /**
	   * 属性pk_billtype的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return String
	   */
	 public String getPk_billtype() {
		 return pk_billtype;
	  }   
	  
     /**
	   * 属性pk_billtype的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newPk_billtype String
	   */
	public void setPk_billtype(String newPk_billtype) {
		
		pk_billtype = newPk_billtype;
	 } 	  
       
        /**
	   * 属性vdef2的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return String
	   */
	 public String getVdef2() {
		 return vdef2;
	  }   
	  
     /**
	   * 属性vdef2的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newVdef2 String
	   */
	public void setVdef2(String newVdef2) {
		
		vdef2 = newVdef2;
	 } 	  
       
        /**
	   * 属性reserve11的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return UFDate
	   */
	 public UFDate getReserve11() {
		 return reserve11;
	  }   
	  
     /**
	   * 属性reserve11的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newReserve11 UFDate
	   */
	public void setReserve11(UFDate newReserve11) {
		
		reserve11 = newReserve11;
	 } 	  
       
        /**
	   * 属性reserve12的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return UFDate
	   */
	 public UFDate getReserve12() {
		 return reserve12;
	  }   
	  
     /**
	   * 属性reserve12的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newReserve12 UFDate
	   */
	public void setReserve12(UFDate newReserve12) {
		
		reserve12 = newReserve12;
	 } 	  
       
        /**
	   * 属性vmemo的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return String
	   */
	 public String getVmemo() {
		 return vmemo;
	  }   
	  
     /**
	   * 属性vmemo的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newVmemo String
	   */
	public void setVmemo(String newVmemo) {
		
		vmemo = newVmemo;
	 } 	  
       
        /**
	   * 属性ts的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return UFDateTime
	   */
	 public UFDateTime getTs() {
		 return ts;
	  }   
	  
     /**
	   * 属性ts的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newTs UFDateTime
	   */
	public void setTs(UFDateTime newTs) {
		
		ts = newTs;
	 } 	  
       
        /**
	   * 属性reserve3的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return String
	   */
	 public String getReserve3() {
		 return reserve3;
	  }   
	  
     /**
	   * 属性reserve3的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newReserve3 String
	   */
	public void setReserve3(String newReserve3) {
		
		reserve3 = newReserve3;
	 } 	  
       
        /**
	   * 属性tuneunits的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return UFDouble
	   */
	 public UFDouble getTuneunits() {
		 return tuneunits;
	  }   
	  
     /**
	   * 属性tuneunits的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newTuneunits UFDouble
	   */
	public void setTuneunits(UFDouble newTuneunits) {
		
		tuneunits = newTuneunits;
	 } 	  
       
        /**
	   * 属性pk_defdoc6的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return String
	   */
	 public String getPk_defdoc6() {
		 return pk_defdoc6;
	  }   
	  
     /**
	   * 属性pk_defdoc6的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newPk_defdoc6 String
	   */
	public void setPk_defdoc6(String newPk_defdoc6) {
		
		pk_defdoc6 = newPk_defdoc6;
	 } 	  
       
        /**
	   * 属性vdef19的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return String
	   */
	 public String getVdef19() {
		 return vdef19;
	  }   
	  
     /**
	   * 属性vdef19的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newVdef19 String
	   */
	public void setVdef19(String newVdef19) {
		
		vdef19 = newVdef19;
	 } 	  
       
        /**
	   * 属性reserve14的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return UFBoolean
	   */
	 public UFBoolean getReserve14() {
		 return reserve14;
	  }   
	  
     /**
	   * 属性reserve14的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newReserve14 UFBoolean
	   */
	public void setReserve14(UFBoolean newReserve14) {
		
		reserve14 = newReserve14;
	 } 	  
       
        /**
	   * 属性dbilldate的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return UFDate
	   */
	 public UFDate getDbilldate() {
		 return dbilldate;
	  }   
	  
     /**
	   * 属性dbilldate的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newDbilldate UFDate
	   */
	public void setDbilldate(UFDate newDbilldate) {
		
		dbilldate = newDbilldate;
	 } 	  
       
        /**
	   * 属性vbillno的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return String
	   */
	 public String getVbillno() {
		 return vbillno;
	  }   
	  
     /**
	   * 属性vbillno的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newVbillno String
	   */
	public void setVbillno(String newVbillno) {
		
		vbillno = newVbillno;
	 } 	  
       
        /**
	   * 属性pk_defdoc4的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return String
	   */
	 public String getPk_defdoc4() {
		 return pk_defdoc4;
	  }   
	  
     /**
	   * 属性pk_defdoc4的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newPk_defdoc4 String
	   */
	public void setPk_defdoc4(String newPk_defdoc4) {
		
		pk_defdoc4 = newPk_defdoc4;
	 } 	  
       
        /**
	   * 属性reserve10的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return UFDouble
	   */
	 public UFDouble getReserve10() {
		 return reserve10;
	  }   
	  
     /**
	   * 属性reserve10的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newReserve10 UFDouble
	   */
	public void setReserve10(UFDouble newReserve10) {
		
		reserve10 = newReserve10;
	 } 	  
       
        /**
	   * 属性reserve13的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return UFDate
	   */
	 public UFDate getReserve13() {
		 return reserve13;
	  }   
	  
     /**
	   * 属性reserve13的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newReserve13 UFDate
	   */
	public void setReserve13(UFDate newReserve13) {
		
		reserve13 = newReserve13;
	 } 	  
       
        /**
	   * 属性pk_defdoc5的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return String
	   */
	 public String getPk_defdoc5() {
		 return pk_defdoc5;
	  }   
	  
     /**
	   * 属性pk_defdoc5的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newPk_defdoc5 String
	   */
	public void setPk_defdoc5(String newPk_defdoc5) {
		
		pk_defdoc5 = newPk_defdoc5;
	 } 	  
       
        /**
	   * 属性standardtune的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return UFDouble
	   */
	 public UFDouble getStandardtune() {
		 return standardtune;
	  }   
	  
     /**
	   * 属性standardtune的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newStandardtune UFDouble
	   */
	public void setStandardtune(UFDouble newStandardtune) {
		
		standardtune = newStandardtune;
	 } 	  
       
        /**
	   * 属性reserve8的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return UFDouble
	   */
	 public UFDouble getReserve8() {
		 return reserve8;
	  }   
	  
     /**
	   * 属性reserve8的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newReserve8 UFDouble
	   */
	public void setReserve8(UFDouble newReserve8) {
		
		reserve8 = newReserve8;
	 } 	  
       
        /**
	   * 属性reserve9的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return UFDouble
	   */
	 public UFDouble getReserve9() {
		 return reserve9;
	  }   
	  
     /**
	   * 属性reserve9的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newReserve9 UFDouble
	   */
	public void setReserve9(UFDouble newReserve9) {
		
		reserve9 = newReserve9;
	 } 	  
       
        /**
	   * 属性pk_defdoc3的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return String
	   */
	 public String getPk_defdoc3() {
		 return pk_defdoc3;
	  }   
	  
     /**
	   * 属性pk_defdoc3的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newPk_defdoc3 String
	   */
	public void setPk_defdoc3(String newPk_defdoc3) {
		
		pk_defdoc3 = newPk_defdoc3;
	 } 	  
       
        /**
	   * 属性carriersid的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return String
	   */
	 public String getCarriersid() {
		 return carriersid;
	  }   
	  
     /**
	   * 属性carriersid的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newCarriersid String
	   */
	public void setCarriersid(String newCarriersid) {
		
		carriersid = newCarriersid;
	 } 	  
       
        /**
	   * 属性reserve1的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return String
	   */
	 public String getReserve1() {
		 return reserve1;
	  }   
	  
     /**
	   * 属性reserve1的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newReserve1 String
	   */
	public void setReserve1(String newReserve1) {
		
		reserve1 = newReserve1;
	 } 	  
       
        /**
	   * 属性reserve16的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return UFBoolean
	   */
	 public UFBoolean getReserve16() {
		 return reserve16;
	  }   
	  
     /**
	   * 属性reserve16的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newReserve16 UFBoolean
	   */
	public void setReserve16(UFBoolean newReserve16) {
		
		reserve16 = newReserve16;
	 } 	  
       
        /**
	   * 属性pk_defdoc2的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return String
	   */
	 public String getPk_defdoc2() {
		 return pk_defdoc2;
	  }   
	  
     /**
	   * 属性pk_defdoc2的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newPk_defdoc2 String
	   */
	public void setPk_defdoc2(String newPk_defdoc2) {
		
		pk_defdoc2 = newPk_defdoc2;
	 } 	  
       
        /**
	   * 属性reserve15的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return UFBoolean
	   */
	 public UFBoolean getReserve15() {
		 return reserve15;
	  }   
	  
     /**
	   * 属性reserve15的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newReserve15 UFBoolean
	   */
	public void setReserve15(UFBoolean newReserve15) {
		
		reserve15 = newReserve15;
	 } 	  
       
        /**
	   * 属性reserve2的Getter方法.
	   *
	   * 创建日期:2011-11-23
	   * @return String
	   */
	 public String getReserve2() {
		 return reserve2;
	  }   
	  
     /**
	   * 属性reserve2的Setter方法.
	   *
	   * 创建日期:2011-11-23
	   * @param newReserve2 String
	   */
	public void setReserve2(String newReserve2) {
		
		reserve2 = newReserve2;
	 } 	  
       
       
    /**
	  * 验证对象各属性之间的数据逻辑正确性.
	  *
	  * 创建日期:2011-11-23
	  * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
	  * ValidationException,对错误进行解释.
	 */
	 public void validate() throws ValidationException {
	
	 	ArrayList errFields = new ArrayList(); // errFields record those null

                                                      // fields that cannot be null.
       		  // 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:
	
	   	
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
	  * 创建日期:2011-11-23
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 	    return null;
	 	
	}   
    
    /**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2011-11-23
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pk_zhbz_h";
	 	}
    
	/**
      * <p>返回表名称.
	  * <p>
	  * 创建日期:2011-11-23
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "wds_zhbz_h";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2011-11-23
	  */
	public ZhbzHVO() {
			
			   super();	
	  }    
    
           
	  /**
       * 返回数值对象的显示名称.
	   *
	   * 创建日期:2011-11-23
	   * @return java.lang.String 返回数值对象的显示名称.
	   */
	 public String getEntityName() {
				
	   return "wds_zhbz_h"; 
				
	 } 
} 

  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.wds.w8004061002;
   	
	import java.util.ArrayList;
	import nc.vo.pub.*;
	import nc.vo.pub.lang.*;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 * 创建日期:2010-9-7
 * @author ${vmObject.author}
 * @version Your Project 1.0
 */
     public class BdCargdocVO extends SuperVO {
           
             public UFBoolean isrmplace;
             public String pk_psndoc;
             public String memo;
             public UFDouble volume;
             public String pk_stordoc;
             public Integer inpriority;
             public String pk_cargdoc;
             public Integer dr;
             public Integer csattr;
             public UFBoolean endflag;
             public Integer outpriority;
             public Integer codelev;
             public UFTime ts;
             public String cscode;
             public String csname;
             public UFBoolean sealflag;
             public UFBoolean ischecked;
            
             public static final String  ISRMPLACE="isrmplace";   
             public static final String  PK_PSNDOC="pk_psndoc";   
             public static final String  MEMO="memo";   
             public static final String  VOLUME="volume";   
             public static final String  PK_STORDOC="pk_stordoc";   
             public static final String  INPRIORITY="inpriority";   
             public static final String  PK_CARGDOC="pk_cargdoc";   
             public static final String  DR="dr";   
             public static final String  CSATTR="csattr";   
             public static final String  ENDFLAG="endflag";   
             public static final String  OUTPRIORITY="outpriority";   
             public static final String  CODELEV="codelev";   
             public static final String  TS="ts";   
             public static final String  CSCODE="cscode";   
             public static final String  CSNAME="csname";   
             public static final String  SEALFLAG="sealflag";   
             public static final String  ISCHECKED="ischecked";   
      
    
        /**
	   * 属性isrmplace的Getter方法.
	   *
	   * 创建日期:2010-9-7
	   * @return UFBoolean
	   */
	 public UFBoolean getIsrmplace() {
		 return isrmplace;
	  }   
	  
     /**
	   * 属性isrmplace的Setter方法.
	   *
	   * 创建日期:2010-9-7
	   * @param newIsrmplace UFBoolean
	   */
	public void setIsrmplace(UFBoolean newIsrmplace) {
		
		isrmplace = newIsrmplace;
	 } 	  
       
        /**
	   * 属性pk_psndoc的Getter方法.
	   *
	   * 创建日期:2010-9-7
	   * @return String
	   */
	 public String getPk_psndoc() {
		 return pk_psndoc;
	  }   
	  
     /**
	   * 属性pk_psndoc的Setter方法.
	   *
	   * 创建日期:2010-9-7
	   * @param newPk_psndoc String
	   */
	public void setPk_psndoc(String newPk_psndoc) {
		
		pk_psndoc = newPk_psndoc;
	 } 	  
       
        /**
	   * 属性memo的Getter方法.
	   *
	   * 创建日期:2010-9-7
	   * @return String
	   */
	 public String getMemo() {
		 return memo;
	  }   
	  
     /**
	   * 属性memo的Setter方法.
	   *
	   * 创建日期:2010-9-7
	   * @param newMemo String
	   */
	public void setMemo(String newMemo) {
		
		memo = newMemo;
	 } 	  
       
        /**
	   * 属性volume的Getter方法.
	   *
	   * 创建日期:2010-9-7
	   * @return UFDouble
	   */
	 public UFDouble getVolume() {
		 return volume;
	  }   
	  
     /**
	   * 属性volume的Setter方法.
	   *
	   * 创建日期:2010-9-7
	   * @param newVolume UFDouble
	   */
	public void setVolume(UFDouble newVolume) {
		
		volume = newVolume;
	 } 	  
       
        /**
	   * 属性pk_stordoc的Getter方法.
	   *
	   * 创建日期:2010-9-7
	   * @return String
	   */
	 public String getPk_stordoc() {
		 return pk_stordoc;
	  }   
	  
     /**
	   * 属性pk_stordoc的Setter方法.
	   *
	   * 创建日期:2010-9-7
	   * @param newPk_stordoc String
	   */
	public void setPk_stordoc(String newPk_stordoc) {
		
		pk_stordoc = newPk_stordoc;
	 } 	  
       
        /**
	   * 属性inpriority的Getter方法.
	   *
	   * 创建日期:2010-9-7
	   * @return Integer
	   */
	 public Integer getInpriority() {
		 return inpriority;
	  }   
	  
     /**
	   * 属性inpriority的Setter方法.
	   *
	   * 创建日期:2010-9-7
	   * @param newInpriority Integer
	   */
	public void setInpriority(Integer newInpriority) {
		
		inpriority = newInpriority;
	 } 	  
       
        /**
	   * 属性pk_cargdoc的Getter方法.
	   *
	   * 创建日期:2010-9-7
	   * @return String
	   */
	 public String getPk_cargdoc() {
		 return pk_cargdoc;
	  }   
	  
     /**
	   * 属性pk_cargdoc的Setter方法.
	   *
	   * 创建日期:2010-9-7
	   * @param newPk_cargdoc String
	   */
	public void setPk_cargdoc(String newPk_cargdoc) {
		
		pk_cargdoc = newPk_cargdoc;
	 } 	  
       
        /**
	   * 属性dr的Getter方法.
	   *
	   * 创建日期:2010-9-7
	   * @return Integer
	   */
	 public Integer getDr() {
		 return dr;
	  }   
	  
     /**
	   * 属性dr的Setter方法.
	   *
	   * 创建日期:2010-9-7
	   * @param newDr Integer
	   */
	public void setDr(Integer newDr) {
		
		dr = newDr;
	 } 	  
       
        /**
	   * 属性csattr的Getter方法.
	   *
	   * 创建日期:2010-9-7
	   * @return Integer
	   */
	 public Integer getCsattr() {
		 return csattr;
	  }   
	  
     /**
	   * 属性csattr的Setter方法.
	   *
	   * 创建日期:2010-9-7
	   * @param newCsattr Integer
	   */
	public void setCsattr(Integer newCsattr) {
		
		csattr = newCsattr;
	 } 	  
       
        /**
	   * 属性endflag的Getter方法.
	   *
	   * 创建日期:2010-9-7
	   * @return UFBoolean
	   */
	 public UFBoolean getEndflag() {
		 return endflag;
	  }   
	  
     /**
	   * 属性endflag的Setter方法.
	   *
	   * 创建日期:2010-9-7
	   * @param newEndflag UFBoolean
	   */
	public void setEndflag(UFBoolean newEndflag) {
		
		endflag = newEndflag;
	 } 	  
       
        /**
	   * 属性outpriority的Getter方法.
	   *
	   * 创建日期:2010-9-7
	   * @return Integer
	   */
	 public Integer getOutpriority() {
		 return outpriority;
	  }   
	  
     /**
	   * 属性outpriority的Setter方法.
	   *
	   * 创建日期:2010-9-7
	   * @param newOutpriority Integer
	   */
	public void setOutpriority(Integer newOutpriority) {
		
		outpriority = newOutpriority;
	 } 	  
       
        /**
	   * 属性codelev的Getter方法.
	   *
	   * 创建日期:2010-9-7
	   * @return Integer
	   */
	 public Integer getCodelev() {
		 return codelev;
	  }   
	  
     /**
	   * 属性codelev的Setter方法.
	   *
	   * 创建日期:2010-9-7
	   * @param newCodelev Integer
	   */
	public void setCodelev(Integer newCodelev) {
		
		codelev = newCodelev;
	 } 	  
       
        /**
	   * 属性ts的Getter方法.
	   *
	   * 创建日期:2010-9-7
	   * @return UFDateTime
	   */
	 public UFTime getTs() {
		 return ts;
	  }   
	  
     /**
	   * 属性ts的Setter方法.
	   *
	   * 创建日期:2010-9-7
	   * @param newTs UFDateTime
	   */
	public void setTs(UFTime newTs) {
		
		ts = newTs;
	 } 	  
       
        /**
	   * 属性cscode的Getter方法.
	   *
	   * 创建日期:2010-9-7
	   * @return String
	   */
	 public String getCscode() {
		 return cscode;
	  }   
	  
     /**
	   * 属性cscode的Setter方法.
	   *
	   * 创建日期:2010-9-7
	   * @param newCscode String
	   */
	public void setCscode(String newCscode) {
		
		cscode = newCscode;
	 } 	  
       
        /**
	   * 属性csname的Getter方法.
	   *
	   * 创建日期:2010-9-7
	   * @return String
	   */
	 public String getCsname() {
		 return csname;
	  }   
	  
     /**
	   * 属性csname的Setter方法.
	   *
	   * 创建日期:2010-9-7
	   * @param newCsname String
	   */
	public void setCsname(String newCsname) {
		
		csname = newCsname;
	 } 	  
       
        /**
	   * 属性sealflag的Getter方法.
	   *
	   * 创建日期:2010-9-7
	   * @return UFBoolean
	   */
	 public UFBoolean getSealflag() {
		 return sealflag;
	  }   
	  
     /**
	   * 属性sealflag的Setter方法.
	   *
	   * 创建日期:2010-9-7
	   * @param newSealflag UFBoolean
	   */
	public void setSealflag(UFBoolean newSealflag) {
		
		sealflag = newSealflag;
	 } 	  
       
        /**
	   * 属性ischecked的Getter方法.
	   *
	   * 创建日期:2010-9-7
	   * @return UFBoolean
	   */
	 public UFBoolean getIschecked() {
		 return ischecked;
	  }   
	  
     /**
	   * 属性ischecked的Setter方法.
	   *
	   * 创建日期:2010-9-7
	   * @param newIschecked UFBoolean
	   */
	public void setIschecked(UFBoolean newIschecked) {
		
		ischecked = newIschecked;
	 } 	  
       
       
    /**
	  * 验证对象各属性之间的数据逻辑正确性.
	  *
	  * 创建日期:2010-9-7
	  * @exception nc.vo.pub.ValidationException 如果验证失败,抛出
	  * ValidationException,对错误进行解释.
	 */
	 public void validate() throws ValidationException {
	
	 	ArrayList errFields = new ArrayList(); // errFields record those null

                                                      // fields that cannot be null.
       		  // 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:
	
	   		if (pk_stordoc == null) {
			errFields.add(new String("pk_stordoc"));
			  }	
	   		if (pk_cargdoc == null) {
			errFields.add(new String("pk_cargdoc"));
			  }	
	   		if (cscode == null) {
			errFields.add(new String("cscode"));
			  }	
	   		if (csname == null) {
			errFields.add(new String("csname"));
			  }	
	   		if (sealflag == null) {
			errFields.add(new String("sealflag"));
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
	  * 创建日期:2010-9-7
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 	    return null;
	 	
	}   
    
    /**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2010-9-7
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pk_cargdoc";
	 	}
    
	/**
      * <p>返回表名称.
	  * <p>
	  * 创建日期:2010-9-7
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "bd_cargdoc";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2010-9-7
	  */
	public BdCargdocVO() {
			
			   super();	
	  }    
    
            /**
	 * 使用主键进行初始化的构造子.
	 *
	 * 创建日期:2010-9-7
	 * @param newPk_cargdoc 主键值
	 */
	 public BdCargdocVO(String newPk_cargdoc) {
		
		// 为主键字段赋值:
		 pk_cargdoc = newPk_cargdoc;
	
    	}
    
     
     /**
	  * 返回对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2010-9-7
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_cargdoc;
	   
	   }

     /**
	  * 设置对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2010-9-7
	  * @param newPk_cargdoc  String    
	  */
	 public void setPrimaryKey(String newPk_cargdoc) {
				
				pk_cargdoc = newPk_cargdoc; 
				
	 } 
           
	  /**
       * 返回数值对象的显示名称.
	   *
	   * 创建日期:2010-9-7
	   * @return java.lang.String 返回数值对象的显示名称.
	   */
	 public String getEntityName() {
				
	   return "bd_cargdoc"; 
				
	 } 
} 

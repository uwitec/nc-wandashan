  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.wds.invbasdoc;
   	
	import java.util.ArrayList;

import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
	
/**
 * 
 *
 * <p>
 *     存货常用档案VO
 * </p>
 *
 * 创建日期:2011-4-22
 * @author author
 * @version Your Project 1.0
 */
     public class InvbasdocVO extends SuperVO {
           //公司主键
    	 private String pk_corp;
             public String pk_invbasdoc;
             public String vdef2;//------zhf 20110627  所属存货分类编码
             public String vdef4;
             public String pk_invmandoc;
             public String vdef6;
             public String vdef7;
             public String vdef1;//------zhf 20110627  存货分类ID
             public String vdef3;
             public String vdef5;
             public UFDouble tray_volume;//托盘对该存货的容量
             public Integer 	tray_volume_layers;//层数
             public Integer     so_ywaring_days;//销售预警天数
             public Integer 	so_waring_days;//销售警戒天数
             public Integer 	db_waring_days1;//调拨警戒天数1
             public Integer 	db_waring_dyas2;//调拨警戒天数2
             public Integer  fuesed;//下拉 ：常用，不常用
             public String pk_wds_invbasdoc;
             public UFDouble grossweight;  //毛重
             public UFDouble  volume;     //体积
            
//             public static final String  PK_INVBASDOC="pk_invbasdoc";   
//             public static final String  VDEF2="vdef2";   
//             public static final String  VDEF4="vdef4";   
//             public static final String  PK_INVMANDOC="pk_invmandoc";   
//             public static final String  VDEF6="vdef6";   
//             public static final String  VDEF7="vdef7";   
//             public static final String  VDEF1="vdef1";   
//             public static final String  VDEF3="vdef3";   
//             public static final String  VDEF5="vdef5";   
//             public static final String  TRAY_VOLUME="tray_volume";   
//             public static final String  PK_WDS_INVBASDOC="pk_wds_invbasdoc";  
             
             public void validateOnSave() throws ValidationException{
            	 if(PuPubVO.getString_TrimZeroLenAsNull(pk_invbasdoc)==null)
            		 throw new ValidationException("存货不能为空");
//            	 if(PuPubVO.getString_TrimZeroLenAsNull(vdef1)==null)
//            		 throw new ValidationException("存货分类不能为空");
//            	 if(PuPubVO.getString_TrimZeroLenAsNull(vdef2)==null)
//            		 throw new ValidationException("存货分类编码不能为空");
             }
      
    
        public Integer getTray_volume_layers() {
				return tray_volume_layers;
			}

			public void setTray_volume_layers(Integer tray_volume_layers) {
				this.tray_volume_layers = tray_volume_layers;
			}

			public Integer getSo_ywaring_days() {
				return so_ywaring_days;
			}

			public void setSo_ywaring_days(Integer so_ywaring_days) {
				this.so_ywaring_days = so_ywaring_days;
			}

			public Integer getSo_waring_days() {
				return so_waring_days;
			}

			public void setSo_waring_days(Integer so_waring_days) {
				this.so_waring_days = so_waring_days;
			}

			public Integer getDb_waring_days1() {
				return db_waring_days1;
			}

			public void setDb_waring_days1(Integer db_waring_days1) {
				this.db_waring_days1 = db_waring_days1;
			}

			public Integer getDb_waring_dyas2() {
				return db_waring_dyas2;
			}

			public void setDb_waring_dyas2(Integer db_waring_dyas2) {
				this.db_waring_dyas2 = db_waring_dyas2;
			}

		/**
	   * 属性pk_invbasdoc的Getter方法.
	   *
	   * 创建日期:2011-4-22
	   * @return String
	   */
	 public String getPk_invbasdoc() {
		 return pk_invbasdoc;
	  }   
	  
     /**
	   * 属性pk_invbasdoc的Setter方法.
	   *
	   * 创建日期:2011-4-22
	   * @param newPk_invbasdoc String
	   */
	public void setPk_invbasdoc(String newPk_invbasdoc) {
		
		pk_invbasdoc = newPk_invbasdoc;
	 } 	  
       
        /**
	   * 属性vdef2的Getter方法.
	   *
	   * 创建日期:2011-4-22
	   * @return String
	   */
	 public String getVdef2() {
		 return vdef2;
	  }   
	  
     /**
	   * 属性vdef2的Setter方法.
	   *
	   * 创建日期:2011-4-22
	   * @param newVdef2 String
	   */
	public void setVdef2(String newVdef2) {
		
		vdef2 = newVdef2;
	 } 	  
       
        /**
	   * 属性vdef4的Getter方法.
	   *
	   * 创建日期:2011-4-22
	   * @return String
	   */
	 public String getVdef4() {
		 return vdef4;
	  }   
	  
     /**
	   * 属性vdef4的Setter方法.
	   *
	   * 创建日期:2011-4-22
	   * @param newVdef4 String
	   */
	public void setVdef4(String newVdef4) {
		
		vdef4 = newVdef4;
	 } 	  
       
        /**
	   * 属性pk_invmandoc的Getter方法.
	   *
	   * 创建日期:2011-4-22
	   * @return String
	   */
	 public String getPk_invmandoc() {
		 return pk_invmandoc;
	  }   
	  
     /**
	   * 属性pk_invmandoc的Setter方法.
	   *
	   * 创建日期:2011-4-22
	   * @param newPk_invmandoc String
	   */
	public void setPk_invmandoc(String newPk_invmandoc) {
		
		pk_invmandoc = newPk_invmandoc;
	 } 	  
       
        /**
	   * 属性vdef6的Getter方法.
	   *
	   * 创建日期:2011-4-22
	   * @return String
	   */
	 public String getVdef6() {
		 return vdef6;
	  }   
	  
     /**
	   * 属性vdef6的Setter方法.
	   *
	   * 创建日期:2011-4-22
	   * @param newVdef6 String
	   */
	public void setVdef6(String newVdef6) {
		
		vdef6 = newVdef6;
	 } 	  
       
        /**
	   * 属性vdef7的Getter方法.
	   *
	   * 创建日期:2011-4-22
	   * @return String
	   */
	 public String getVdef7() {
		 return vdef7;
	  }   
	  
     /**
	   * 属性vdef7的Setter方法.
	   *
	   * 创建日期:2011-4-22
	   * @param newVdef7 String
	   */
	public void setVdef7(String newVdef7) {
		
		vdef7 = newVdef7;
	 } 	  
       
        /**
	   * 属性vdef1的Getter方法.
	   *
	   * 创建日期:2011-4-22
	   * @return String
	   */
	 public String getVdef1() {
		 return vdef1;
	  }   
	  
     /**
	   * 属性vdef1的Setter方法.
	   *
	   * 创建日期:2011-4-22
	   * @param newVdef1 String
	   */
	public void setVdef1(String newVdef1) {
		
		vdef1 = newVdef1;
	 } 	  
       
        /**
	   * 属性vdef3的Getter方法.
	   *
	   * 创建日期:2011-4-22
	   * @return String
	   */
	 public String getVdef3() {
		 return vdef3;
	  }   
	  
     /**
	   * 属性vdef3的Setter方法.
	   *
	   * 创建日期:2011-4-22
	   * @param newVdef3 String
	   */
	public void setVdef3(String newVdef3) {
		
		vdef3 = newVdef3;
	 } 	  
       
        /**
	   * 属性vdef5的Getter方法.
	   *
	   * 创建日期:2011-4-22
	   * @return String
	   */
	 public String getVdef5() {
		 return vdef5;
	  }   
	  
     /**
	   * 属性vdef5的Setter方法.
	   *
	   * 创建日期:2011-4-22
	   * @param newVdef5 String
	   */
	public void setVdef5(String newVdef5) {
		
		vdef5 = newVdef5;
	 } 	  
       
        /**
	   * 属性tray_volume的Getter方法.
	   *
	   * 创建日期:2011-4-22
	   * @return UFDouble
	   */
	 public UFDouble getTray_volume() {
		 return tray_volume;
	  }   
	  
     /**
	   * 属性tray_volume的Setter方法.
	   *
	   * 创建日期:2011-4-22
	   * @param newTray_volume UFDouble
	   */
	public void setTray_volume(UFDouble newTray_volume) {
		
		tray_volume = newTray_volume;
	 } 	  
       
        /**
	   * 属性pk_wds_invbasdoc的Getter方法.
	   *
	   * 创建日期:2011-4-22
	   * @return String
	   */
	 public String getPk_wds_invbasdoc() {
		 return pk_wds_invbasdoc;
	  }   
	  
     /**
	   * 属性pk_wds_invbasdoc的Setter方法.
	   *
	   * 创建日期:2011-4-22
	   * @param newPk_wds_invbasdoc String
	   */
	public void setPk_wds_invbasdoc(String newPk_wds_invbasdoc) {
		
		pk_wds_invbasdoc = newPk_wds_invbasdoc;
	 } 	  
       
       
    /**
	  * 验证对象各属性之间的数据逻辑正确性.
	  *
	  * 创建日期:2011-4-22
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
	  * 创建日期:2011-4-22
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 	    return null;
	 	
	}   
    
    /**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2011-4-22
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pk_wds_invbasdoc";
	 	}
    
	/**
      * <p>返回表名称.
	  * <p>
	  * 创建日期:2011-4-22
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "wds_invbasdoc";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2011-4-22
	  */
	public InvbasdocVO() {
			
			   super();	
	  }    
    
            public UFDouble getGrossweight() {
		return grossweight;
	}


	public void setGrossweight(UFDouble grossweight) {
		this.grossweight = grossweight;
	}


	public UFDouble getVolume() {
		return volume;
	}


	public void setVolume(UFDouble volume) {
		this.volume = volume;
	}


			/**
	 * 使用主键进行初始化的构造子.
	 *
	 * 创建日期:2011-4-22
	 * @param newPk_wds_invbasdoc 主键值
	 */
	 public InvbasdocVO(String newPk_wds_invbasdoc) {
		
		// 为主键字段赋值:
		 pk_wds_invbasdoc = newPk_wds_invbasdoc;
	
    	}
    
     
     /**
	  * 返回对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2011-4-22
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_wds_invbasdoc;
	   
	   }

     /**
	  * 设置对象标识,用来唯一定位对象.
	  *
	  * 创建日期:2011-4-22
	  * @param newPk_wds_invbasdoc  String    
	  */
	 public void setPrimaryKey(String newPk_wds_invbasdoc) {
				
				pk_wds_invbasdoc = newPk_wds_invbasdoc; 
				
	 } 
           
	  /**
       * 返回数值对象的显示名称.
	   *
	   * 创建日期:2011-4-22
	   * @return java.lang.String 返回数值对象的显示名称.
	   */
	 public String getEntityName() {
				
	   return "wds_invbasdoc"; 
				
	 }

	public Integer getFuesed() {
		return fuesed;
	}

	public void setFuesed(Integer fuesed) {
		this.fuesed = fuesed;
	}


	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}


	public String getPk_corp() {
		return pk_corp;
	} 
	
	
} 

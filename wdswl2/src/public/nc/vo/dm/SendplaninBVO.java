  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.dm;
   	
	import java.util.ArrayList;

import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubTool;
	
/**
 * <b>���˼ƻ�¼���ӱ� </b>
 *
 * <p>
 *     �ڴ˴����Ӵ����������Ϣ
 * </p>
 *
 * ��������:2011-3-23
 * @author author
 * @version Your Project 1.0
 */
     public class SendplaninBVO extends SuperVO {
           
 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
			public String reserve5;
			public UFDouble hsl;//������
			
             public UFDateTime ts;
             /**�ƻ����� */
             public UFDouble nplannum;
             /**��������λ */
             public String assunit;
             public String reserve4;
             public String vdef4;
             public String vdef7;
             /**������ */
             public UFDouble nassplannum;
             /**�������id */
             public String pk_invbasdoc;
             public String vdef2;
             /**�ۼư������� */
             public UFDouble ndealnum;
             /**�ۼư��Ÿ����� */
         	 public UFDouble nassdealnum;
             public UFDate reserve11;
             public UFDate reserve12;
             /**���� */
             public String pk_sendplanin_b;
             /**��ע */
             public String vmemo;
             /**���� */
             public String pk_sendplanin;
             public UFDouble reserve6;
             public String vdef1;
             public String reserve3;
             public UFBoolean reserve14;//------------------�Ƿ�ر�
             public UFDouble reserve10;
             public UFDate reserve13;
             /** ������λ*/
             public String unit;
             public UFDouble reserve8;
             public UFDouble reserve9;
             public String vdef3;
             public String vdef9;
             public String vdef8;
             public String reserve1;
             public UFBoolean reserve16;
             public UFDouble reserve7;
             public UFBoolean reserve15;
             /**�������id */
             public String pk_invmandoc;
             public String vdef6;
             public String reserve2;
             public Integer dr;
            

			public String vdef10;
             public String vdef5;
             
             public UFBoolean bisdate;//�Ƿ������
            
             public static final String  RESERVE5="reserve5";   
             public static final String  TS="ts";   
             public static final String  NPLANNUM="nplannum";   
             public static final String  ASSUNIT="assunit";   
             public static final String  RESERVE4="reserve4";   
             public static final String  VDEF4="vdef4";   
             public static final String  VDEF7="vdef7";   
             public static final String  NASSPLANNUM="nassplannum";   
             public static final String  PK_INVBASDOC="pk_invbasdoc";   
             public static final String  VDEF2="vdef2";   
             public static final String  NDEALNUM="ndealnum";   
             public static final String  RESERVE11="reserve11";   
             public static final String  RESERVE12="reserve12";   
             public static final String  PK_SENDPLANIN_B="pk_sendplanin_b";   
             public static final String  VMEMO="vmemo";   
             public static final String  PK_SENDPLANIN="pk_sendplanin";   
             public static final String  RESERVE6="reserve6";   
             public static final String  VDEF1="vdef1";   
             public static final String  RESERVE3="reserve3";   
             public static final String  RESERVE14="reserve14";   
             public static final String  RESERVE10="reserve10";   
             public static final String  RESERVE13="reserve13";   
             public static final String  UNIT="unit";   
             public static final String  RESERVE8="reserve8";   
             public static final String  RESERVE9="reserve9";   
             public static final String  VDEF3="vdef3";   
             public static final String  VDEF9="vdef9";   
             public static final String  VDEF8="vdef8";   
             public static final String  RESERVE1="reserve1";   
             public static final String  RESERVE16="reserve16";   
             public static final String  RESERVE7="reserve7";   
             public static final String  RESERVE15="reserve15";   
             public static final String  PK_INVMANDOC="pk_invmandoc";   
             public static final String  VDEF6="vdef6";   
             public static final String  RESERVE2="reserve2";   
             public static final String  DR="dr";   
             public static final String  VDEF10="vdef10";   
             public static final String  VDEF5="vdef5";   
      
    
           public UFDouble getHsl() {
				return hsl;
			}

			public void setHsl(UFDouble hsl) {
				this.hsl = hsl;
			}

			 public UFBoolean getBisdate() {
					return bisdate;
				}

				public void setBisdate(UFBoolean bisdate) {
					this.bisdate = bisdate;
				}
		/**
	   * ����reserve5��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return String
	   */
	 public String getReserve5() {
		 return reserve5;
	  }   
	  
     /**
	   * ����reserve5��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newReserve5 String
	   */
	public void setReserve5(String newReserve5) {
		
		reserve5 = newReserve5;
	 } 	  
       
       
        /**
	   * ����nplannum��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return UFDouble
	   */
	 public UFDouble getNplannum() {
		 return nplannum;
	  }   
	  
     /**
	   * ����nplannum��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newNplannum UFDouble
	   */
	public void setNplannum(UFDouble newNplannum) {
		
		nplannum = newNplannum;
	 } 	  
       
        /**
	   * ����assunit��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return String
	   */
	 public String getAssunit() {
		 return assunit;
	  }   
	  
     /**
	   * ����assunit��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newAssunit String
	   */
	public void setAssunit(String newAssunit) {
		
		assunit = newAssunit;
	 } 	  
       
        /**
	   * ����reserve4��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return String
	   */
	 public String getReserve4() {
		 return reserve4;
	  }   
	  
     /**
	   * ����reserve4��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newReserve4 String
	   */
	public void setReserve4(String newReserve4) {
		
		reserve4 = newReserve4;
	 } 	  
       
        /**
	   * ����vdef4��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return String
	   */
	 public String getVdef4() {
		 return vdef4;
	  }   
	  
     /**
	   * ����vdef4��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newVdef4 String
	   */
	public void setVdef4(String newVdef4) {
		
		vdef4 = newVdef4;
	 } 	  
       
        /**
	   * ����vdef7��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return String
	   */
	 public String getVdef7() {
		 return vdef7;
	  }   
	  
     /**
	   * ����vdef7��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newVdef7 String
	   */
	public void setVdef7(String newVdef7) {
		
		vdef7 = newVdef7;
	 } 	  
       
        /**
	   * ����nassplannum��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return UFDouble
	   */
	 public UFDouble getNassplannum() {
		 return nassplannum;
	  }   
	  
     /**
	   * ����nassplannum��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newNassplannum UFDouble
	   */
	public void setNassplannum(UFDouble newNassplannum) {
		
		nassplannum = newNassplannum;
	 } 	  
       
        /**
	   * ����pk_invbasdoc��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return String
	   */
	 public String getPk_invbasdoc() {
		 return pk_invbasdoc;
	  }   
	  
     /**
	   * ����pk_invbasdoc��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newPk_invbasdoc String
	   */
	public void setPk_invbasdoc(String newPk_invbasdoc) {
		
		pk_invbasdoc = newPk_invbasdoc;
	 } 	  
       
        /**
	   * ����vdef2��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return String
	   */
	 public String getVdef2() {
		 return vdef2;
	  }   
	  
     /**
	   * ����vdef2��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newVdef2 String
	   */
	public void setVdef2(String newVdef2) {
		
		vdef2 = newVdef2;
	 } 	  
       
        /**
	   * ����ndealnum��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return UFDouble
	   */
	 public UFDouble getNdealnum() {
		 return ndealnum;
	  }   
	  
     /**
	   * ����ndealnum��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newNdealnum UFDouble
	   */
	public void setNdealnum(UFDouble newNdealnum) {
		
		ndealnum = newNdealnum;
	 } 	  
       
        /**
	   * ����reserve11��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return UFDate
	   */
	 public UFDate getReserve11() {
		 return reserve11;
	  }   
	  
     /**
	   * ����reserve11��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newReserve11 UFDate
	   */
	public void setReserve11(UFDate newReserve11) {
		
		reserve11 = newReserve11;
	 } 	  
       
        /**
	   * ����reserve12��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return UFDate
	   */
	 public UFDate getReserve12() {
		 return reserve12;
	  }   
	  
     /**
	   * ����reserve12��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newReserve12 UFDate
	   */
	public void setReserve12(UFDate newReserve12) {
		
		reserve12 = newReserve12;
	 } 	  
       
        /**
	   * ����pk_sendplanin_b��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return String
	   */
	 public String getPk_sendplanin_b() {
		 return pk_sendplanin_b;
	  }   
	  
     /**
	   * ����pk_sendplanin_b��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newPk_sendplanin_b String
	   */
	public void setPk_sendplanin_b(String newPk_sendplanin_b) {
		
		pk_sendplanin_b = newPk_sendplanin_b;
	 } 	  
       
        /**
	   * ����vmemo��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return String
	   */
	 public String getVmemo() {
		 return vmemo;
	  }   
	  
     /**
	   * ����vmemo��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newVmemo String
	   */
	public void setVmemo(String newVmemo) {
		
		vmemo = newVmemo;
	 } 	  
       
        /**
	   * ����pk_sendplanin��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return String
	   */
	 public String getPk_sendplanin() {
		 return pk_sendplanin;
	  }   
	  
     /**
	   * ����pk_sendplanin��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newPk_sendplanin String
	   */
	public void setPk_sendplanin(String newPk_sendplanin) {
		
		pk_sendplanin = newPk_sendplanin;
	 } 	  
       
        /**
	   * ����reserve6��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return UFDouble
	   */
	 public UFDouble getReserve6() {
		 return reserve6;
	  }   
	  
     /**
	   * ����reserve6��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newReserve6 UFDouble
	   */
	public void setReserve6(UFDouble newReserve6) {
		
		reserve6 = newReserve6;
	 } 	  
       
        /**
	   * ����vdef1��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return String
	   */
	 public String getVdef1() {
		 return vdef1;
	  }   
	  
     /**
	   * ����vdef1��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newVdef1 String
	   */
	public void setVdef1(String newVdef1) {
		
		vdef1 = newVdef1;
	 } 	  
       
        /**
	   * ����reserve3��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return String
	   */
	 public String getReserve3() {
		 return reserve3;
	  }   
	  
     /**
	   * ����reserve3��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newReserve3 String
	   */
	public void setReserve3(String newReserve3) {
		
		reserve3 = newReserve3;
	 } 	  
       
        /**
	   * ����reserve14��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return UFBoolean
	   */
	 public UFBoolean getReserve14() {
		 return reserve14;
	  }   
	  
     /**
	   * ����reserve14��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newReserve14 UFBoolean
	   */
	public void setReserve14(UFBoolean newReserve14) {
		
		reserve14 = newReserve14;
	 } 	  
       
        /**
	   * ����reserve10��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return UFDouble
	   */
	 public UFDouble getReserve10() {
		 return reserve10;
	  }   
	  
     /**
	   * ����reserve10��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newReserve10 UFDouble
	   */
	public void setReserve10(UFDouble newReserve10) {
		
		reserve10 = newReserve10;
	 } 	  
       
        /**
	   * ����reserve13��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return UFDate
	   */
	 public UFDate getReserve13() {
		 return reserve13;
	  }   
	  
     /**
	   * ����reserve13��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newReserve13 UFDate
	   */
	public void setReserve13(UFDate newReserve13) {
		
		reserve13 = newReserve13;
	 } 	  
       
        /**
	   * ����unit��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return String
	   */
	 public String getUnit() {
		 return unit;
	  }   
	  
     /**
	   * ����unit��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newUnit String
	   */
	public void setUnit(String newUnit) {
		
		unit = newUnit;
	 } 	  
       
        /**
	   * ����reserve8��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return UFDouble
	   */
	 public UFDouble getReserve8() {
		 return reserve8;
	  }   
	  
     /**
	   * ����reserve8��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newReserve8 UFDouble
	   */
	public void setReserve8(UFDouble newReserve8) {
		
		reserve8 = newReserve8;
	 } 	  
       
        /**
	   * ����reserve9��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return UFDouble
	   */
	 public UFDouble getReserve9() {
		 return reserve9;
	  }   
	  
     /**
	   * ����reserve9��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newReserve9 UFDouble
	   */
	public void setReserve9(UFDouble newReserve9) {
		
		reserve9 = newReserve9;
	 } 	  
       
        /**
	   * ����vdef3��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return String
	   */
	 public String getVdef3() {
		 return vdef3;
	  }   
	  
     /**
	   * ����vdef3��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newVdef3 String
	   */
	public void setVdef3(String newVdef3) {
		
		vdef3 = newVdef3;
	 } 	  
       
        /**
	   * ����vdef9��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return String
	   */
	 public String getVdef9() {
		 return vdef9;
	  }   
	  
     /**
	   * ����vdef9��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newVdef9 String
	   */
	public void setVdef9(String newVdef9) {
		
		vdef9 = newVdef9;
	 } 	  
       
        /**
	   * ����vdef8��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return String
	   */
	 public String getVdef8() {
		 return vdef8;
	  }   
	  
     /**
	   * ����vdef8��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newVdef8 String
	   */
	public void setVdef8(String newVdef8) {
		
		vdef8 = newVdef8;
	 } 	  
       
        /**
	   * ����reserve1��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return String
	   */
	 public String getReserve1() {
		 return reserve1;
	  }   
	  
     /**
	   * ����reserve1��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newReserve1 String
	   */
	public void setReserve1(String newReserve1) {
		
		reserve1 = newReserve1;
	 } 	  
       
        /**
	   * ����reserve16��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return UFBoolean
	   */
	 public UFBoolean getReserve16() {
		 return reserve16;
	  }   
	  
     /**
	   * ����reserve16��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newReserve16 UFBoolean
	   */
	public void setReserve16(UFBoolean newReserve16) {
		
		reserve16 = newReserve16;
	 } 	  
       
        /**
	   * ����reserve7��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return UFDouble
	   */
	 public UFDouble getReserve7() {
		 return reserve7;
	  }   
	  
     /**
	   * ����reserve7��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newReserve7 UFDouble
	   */
	public void setReserve7(UFDouble newReserve7) {
		
		reserve7 = newReserve7;
	 } 	  
       
        /**
	   * ����reserve15��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return UFBoolean
	   */
	 public UFBoolean getReserve15() {
		 return reserve15;
	  }   
	  
     /**
	   * ����reserve15��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newReserve15 UFBoolean
	   */
	public void setReserve15(UFBoolean newReserve15) {
		
		reserve15 = newReserve15;
	 } 	  
       
        /**
	   * ����pk_invmandoc��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return String
	   */
	 public String getPk_invmandoc() {
		 return pk_invmandoc;
	  }   
	  
     /**
	   * ����pk_invmandoc��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newPk_invmandoc String
	   */
	public void setPk_invmandoc(String newPk_invmandoc) {
		
		pk_invmandoc = newPk_invmandoc;
	 } 	  
       
        /**
	   * ����vdef6��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return String
	   */
	 public String getVdef6() {
		 return vdef6;
	  }   
	  
     /**
	   * ����vdef6��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newVdef6 String
	   */
	public void setVdef6(String newVdef6) {
		
		vdef6 = newVdef6;
	 } 	  
       
        /**
	   * ����reserve2��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return String
	   */
	 public String getReserve2() {
		 return reserve2;
	  }   
	  
     /**
	   * ����reserve2��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newReserve2 String
	   */
	public void setReserve2(String newReserve2) {
		
		reserve2 = newReserve2;
	 } 	  
       
        /**
	   * ����vdef10��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return String
	   */
	 public String getVdef10() {
		 return vdef10;
	  }   
	  
     /**
	   * ����vdef10��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newVdef10 String
	   */
	public void setVdef10(String newVdef10) {
		
		vdef10 = newVdef10;
	 } 	  
       
        /**
	   * ����vdef5��Getter����.
	   *
	   * ��������:2011-3-23
	   * @return String
	   */
	 public String getVdef5() {
		 return vdef5;
	  }   
	  
     /**
	   * ����vdef5��Setter����.
	   *
	   * ��������:2011-3-23
	   * @param newVdef5 String
	   */
	public void setVdef5(String newVdef5) {
		
		vdef5 = newVdef5;
	 } 	  
	
	
	public void validationOnSave() throws ValidationException{
		if(PuPubVO.getUFDouble_NullAsZero(getNplannum()).equals(WdsWlPubTool.DOUBLE_ZERO)){
			if(!PuPubVO.getUFDouble_NullAsZero(getNassplannum()).equals(WdsWlPubTool.DOUBLE_ZERO))
				throw new ValidationException("��������Ϊ��");
		}
	}
	
       
       
    /**
	  * ��֤���������֮��������߼���ȷ��.
	  *
	  * ��������:2011-3-23
	  * @exception nc.vo.pub.ValidationException �����֤ʧ��,�׳�
	  * ValidationException,�Դ�����н���.
	 */
	 public void validate() throws ValidationException {
	
	 	ArrayList errFields = new ArrayList(); // errFields record those null

                                                      // fields that cannot be null.
       		  // ����Ƿ�Ϊ�������յ��ֶθ��˿�ֵ,�������Ҫ�޸��������ʾ��Ϣ:
	
	   		if (pk_sendplanin_b == null) {
			errFields.add(new String("pk_sendplanin_b"));
			  }	
	   	
	    StringBuffer message = new StringBuffer();
		message.append("�����ֶβ���Ϊ��:");
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
	  * <p>ȡ�ø�VO�����ֶ�.
	  * <p>
	  * ��������:2011-3-23
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 		return "pk_sendplanin";
	 	
	}   
    
    /**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:2011-3-23
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pk_sendplanin_b";
	 	}
    
	/**
      * <p>���ر�����.
	  * <p>
	  * ��������:2011-3-23
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "wds_sendplanin_b";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:2011-3-23
	  */
	public SendplaninBVO() {
			
			   super();	
	  }    
    
            /**
	 * ʹ���������г�ʼ���Ĺ�����.
	 *
	 * ��������:2011-3-23
	 * @param newPk_sendplanin_b ����ֵ
	 */
	 public SendplaninBVO(String newPk_sendplanin_b) {
		
		// Ϊ�����ֶθ�ֵ:
		 pk_sendplanin_b = newPk_sendplanin_b;
	
    	}
    
     
     /**
	  * ���ض����ʶ,����Ψһ��λ����.
	  *
	  * ��������:2011-3-23
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_sendplanin_b;
	   
	   }

     /**
	  * ���ö����ʶ,����Ψһ��λ����.
	  *
	  * ��������:2011-3-23
	  * @param newPk_sendplanin_b  String    
	  */
	 public void setPrimaryKey(String newPk_sendplanin_b) {
				
				pk_sendplanin_b = newPk_sendplanin_b; 
				
	 } 
           
	  /**
       * ������ֵ�������ʾ����.
	   *
	   * ��������:2011-3-23
	   * @return java.lang.String ������ֵ�������ʾ����.
	   */
	 public String getEntityName() {
				
	   return "wds_sendplanin_b"; 
				
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

	public UFDouble getNassdealnum() {
		return nassdealnum;
	}

	public void setNassdealnum(UFDouble nassdealnum) {
		this.nassdealnum = nassdealnum;
	} 
} 
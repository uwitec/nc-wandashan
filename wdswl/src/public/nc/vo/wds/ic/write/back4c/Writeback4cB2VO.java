  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/
      	package nc.vo.wds.ic.write.back4c;
   	
	import java.util.ArrayList;
	import nc.vo.pub.*;
import nc.vo.pub.lang.*;
	
/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 *
 * <p>
 *     �ڴ˴����Ӵ����������Ϣ
 * </p>
 *
 * ��������:2011-10-25
 * @author author
 * @version Your Project 1.0
 */
     public class Writeback4cB2VO extends SuperVO {
           
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 public String crowno; //�к�
	 public String cfirsttype;//���۶���
     public String vfirstbillcode;//���۶�������
     public String cfirstbillhid;//���۶�������
     public String cfirstbillbid;//���۶����ֱ�����
	
     public String pk_invmandoc;//�������id 
	 public String pk_invbasdoc;//�������id           
	 public String unit;//�����������λid
	 public String assunit; //�����������λid
	
	
    public String pk_soorder;//�����˵�����
    public String pk_soorder_b;//�����˵��ֱ�����
    public UFDouble narrangnum;//�����˵�������������
    public UFDouble nassarrangnum;//�����˵����Ÿ�����
	//��������Ϣ�ֶ�
	public String vsourcebillcode;//�������۳��ⵥ���ݺţ�WDS8.....��
	public String csourcetype;//�������۳������ͣ�WDS8��
	public String csourcebillhid;//�������۳��ⵥ����
	public String csourcebillbid;//�������۳��ⵥ�ӱ�����
    public UFDouble noutnum;
    public UFDouble noutassistnum;      
    public String pk_wds_writeback4c_b2;
    public String pk_wds_writeback4c_h;
	//Ԥ���ֶ�
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
	//�Զ�����
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
	//��ע
	public String vmemo;
    
        /**
	   * ����reserve5��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getReserve5() {
		 return reserve5;
	  }   
	  
     /**
	   * ����reserve5��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newReserve5 String
	   */
	public void setReserve5(String newReserve5) {
		
		reserve5 = newReserve5;
	 } 	  
       
        /**
	   * ����assunit��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getAssunit() {
		 return assunit;
	  }   
	  
     /**
	   * ����assunit��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newAssunit String
	   */
	public void setAssunit(String newAssunit) {
		
		assunit = newAssunit;
	 } 	  
       
        /**
	   * ����csourcebillhid��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getCsourcebillhid() {
		 return csourcebillhid;
	  }   
	  
     /**
	   * ����csourcebillhid��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newCsourcebillhid String
	   */
	public void setCsourcebillhid(String newCsourcebillhid) {
		
		csourcebillhid = newCsourcebillhid;
	 } 	  
       
        /**
	   * ����vdef4��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getVdef4() {
		 return vdef4;
	  }   
	  
     /**
	   * ����vdef4��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newVdef4 String
	   */
	public void setVdef4(String newVdef4) {
		
		vdef4 = newVdef4;
	 } 	  
       
        /**
	   * ����vfirstbillcode��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getVfirstbillcode() {
		 return vfirstbillcode;
	  }   
	  
     /**
	   * ����vfirstbillcode��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newVfirstbillcode String
	   */
	public void setVfirstbillcode(String newVfirstbillcode) {
		
		vfirstbillcode = newVfirstbillcode;
	 } 	  
       
        /**
	   * ����csourcetype��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getCsourcetype() {
		 return csourcetype;
	  }   
	  
     /**
	   * ����csourcetype��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newCsourcetype String
	   */
	public void setCsourcetype(String newCsourcetype) {
		
		csourcetype = newCsourcetype;
	 } 	  
       
        /**
	   * ����dr��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return Integer
	   */
	 public Integer getDr() {
		 return dr;
	  }   
	  
     /**
	   * ����dr��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newDr Integer
	   */
	public void setDr(Integer newDr) {
		
		dr = newDr;
	 } 	  
       
        /**
	   * ����narrangnum��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return UFDouble
	   */
	 public UFDouble getNarrangnum() {
		 return narrangnum;
	  }   
	  
     /**
	   * ����narrangnum��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newNarrangnum UFDouble
	   */
	public void setNarrangnum(UFDouble newNarrangnum) {
		
		narrangnum = newNarrangnum;
	 } 	  
       
        /**
	   * ����pk_soorder_b��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getPk_soorder_b() {
		 return pk_soorder_b;
	  }   
	  
     /**
	   * ����pk_soorder_b��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newPk_soorder_b String
	   */
	public void setPk_soorder_b(String newPk_soorder_b) {
		
		pk_soorder_b = newPk_soorder_b;
	 } 	  
       
        /**
	   * ����reserve12��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return UFDate
	   */
	 public UFDate getReserve12() {
		 return reserve12;
	  }   
	  
     /**
	   * ����reserve12��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newReserve12 UFDate
	   */
	public void setReserve12(UFDate newReserve12) {
		
		reserve12 = newReserve12;
	 } 	  
       
        /**
	   * ����ts��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return UFDateTime
	   */
	 public UFDateTime getTs() {
		 return ts;
	  }   
	  
     /**
	   * ����ts��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newTs UFDateTime
	   */
	public void setTs(UFDateTime newTs) {
		
		ts = newTs;
	 } 	  
       
        /**
	   * ����noutnum��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return UFDouble
	   */
	 public UFDouble getNoutnum() {
		 return noutnum;
	  }   
	  
     /**
	   * ����noutnum��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newNoutnum UFDouble
	   */
	public void setNoutnum(UFDouble newNoutnum) {
		
		noutnum = newNoutnum;
	 } 	  
       
        /**
	   * ����pk_soorder��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getPk_soorder() {
		 return pk_soorder;
	  }   
	  
     /**
	   * ����pk_soorder��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newPk_soorder String
	   */
	public void setPk_soorder(String newPk_soorder) {
		
		pk_soorder = newPk_soorder;
	 } 	  
       
        /**
	   * ����reserve6��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return UFDouble
	   */
	 public UFDouble getReserve6() {
		 return reserve6;
	  }   
	  
     /**
	   * ����reserve6��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newReserve6 UFDouble
	   */
	public void setReserve6(UFDouble newReserve6) {
		
		reserve6 = newReserve6;
	 } 	  
       
        /**
	   * ����crowno��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getCrowno() {
		 return crowno;
	  }   
	  
     /**
	   * ����crowno��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newCrowno String
	   */
	public void setCrowno(String newCrowno) {
		
		crowno = newCrowno;
	 } 	  
       
        /**
	   * ����reserve14��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return UFBoolean
	   */
	 public UFBoolean getReserve14() {
		 return reserve14;
	  }   
	  
     /**
	   * ����reserve14��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newReserve14 UFBoolean
	   */
	public void setReserve14(UFBoolean newReserve14) {
		
		reserve14 = newReserve14;
	 } 	  
       
        /**
	   * ����noutassistnum��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return UFDouble
	   */
	 public UFDouble getNoutassistnum() {
		 return noutassistnum;
	  }   
	  
     /**
	   * ����noutassistnum��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newNoutassistnum UFDouble
	   */
	public void setNoutassistnum(UFDouble newNoutassistnum) {
		
		noutassistnum = newNoutassistnum;
	 } 	  
       
        /**
	   * ����reserve10��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return UFDouble
	   */
	 public UFDouble getReserve10() {
		 return reserve10;
	  }   
	  
     /**
	   * ����reserve10��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newReserve10 UFDouble
	   */
	public void setReserve10(UFDouble newReserve10) {
		
		reserve10 = newReserve10;
	 } 	  
       
        /**
	   * ����vdef3��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getVdef3() {
		 return vdef3;
	  }   
	  
     /**
	   * ����vdef3��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newVdef3 String
	   */
	public void setVdef3(String newVdef3) {
		
		vdef3 = newVdef3;
	 } 	  
       
        /**
	   * ����reserve1��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getReserve1() {
		 return reserve1;
	  }   
	  
     /**
	   * ����reserve1��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newReserve1 String
	   */
	public void setReserve1(String newReserve1) {
		
		reserve1 = newReserve1;
	 } 	  
       
        /**
	   * ����reserve16��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return UFBoolean
	   */
	 public UFBoolean getReserve16() {
		 return reserve16;
	  }   
	  
     /**
	   * ����reserve16��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newReserve16 UFBoolean
	   */
	public void setReserve16(UFBoolean newReserve16) {
		
		reserve16 = newReserve16;
	 } 	  
       
        /**
	   * ����reserve15��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return UFBoolean
	   */
	 public UFBoolean getReserve15() {
		 return reserve15;
	  }   
	  
     /**
	   * ����reserve15��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newReserve15 UFBoolean
	   */
	public void setReserve15(UFBoolean newReserve15) {
		
		reserve15 = newReserve15;
	 } 	  
       
        /**
	   * ����pk_invmandoc��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getPk_invmandoc() {
		 return pk_invmandoc;
	  }   
	  
     /**
	   * ����pk_invmandoc��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newPk_invmandoc String
	   */
	public void setPk_invmandoc(String newPk_invmandoc) {
		
		pk_invmandoc = newPk_invmandoc;
	 } 	  
       
        /**
	   * ����reserve2��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getReserve2() {
		 return reserve2;
	  }   
	  
     /**
	   * ����reserve2��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newReserve2 String
	   */
	public void setReserve2(String newReserve2) {
		
		reserve2 = newReserve2;
	 } 	  
       
        /**
	   * ����vdef10��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getVdef10() {
		 return vdef10;
	  }   
	  
     /**
	   * ����vdef10��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newVdef10 String
	   */
	public void setVdef10(String newVdef10) {
		
		vdef10 = newVdef10;
	 } 	  
       
        /**
	   * ����cfirstbillbid��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getCfirstbillbid() {
		 return cfirstbillbid;
	  }   
	  
     /**
	   * ����cfirstbillbid��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newCfirstbillbid String
	   */
	public void setCfirstbillbid(String newCfirstbillbid) {
		
		cfirstbillbid = newCfirstbillbid;
	 } 	  
       
        /**
	   * ����vdef5��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getVdef5() {
		 return vdef5;
	  }   
	  
     /**
	   * ����vdef5��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newVdef5 String
	   */
	public void setVdef5(String newVdef5) {
		
		vdef5 = newVdef5;
	 } 	  
       
        /**
	   * ����nassarrangnum��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return UFDouble
	   */
	 public UFDouble getNassarrangnum() {
		 return nassarrangnum;
	  }   
	  
     /**
	   * ����nassarrangnum��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newNassarrangnum UFDouble
	   */
	public void setNassarrangnum(UFDouble newNassarrangnum) {
		
		nassarrangnum = newNassarrangnum;
	 } 	  
       
       
        /**
	   * ����cfirstbillhid��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getCfirstbillhid() {
		 return cfirstbillhid;
	  }   
	  
     /**
	   * ����cfirstbillhid��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newCfirstbillhid String
	   */
	public void setCfirstbillhid(String newCfirstbillhid) {
		
		cfirstbillhid = newCfirstbillhid;
	 } 	  
       
        /**
	   * ����reserve4��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getReserve4() {
		 return reserve4;
	  }   
	  
     /**
	   * ����reserve4��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newReserve4 String
	   */
	public void setReserve4(String newReserve4) {
		
		reserve4 = newReserve4;
	 } 	  
       
        /**
	   * ����vdef7��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getVdef7() {
		 return vdef7;
	  }   
	  
     /**
	   * ����vdef7��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newVdef7 String
	   */
	public void setVdef7(String newVdef7) {
		
		vdef7 = newVdef7;
	 } 	  
       
        /**
	   * ����pk_invbasdoc��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getPk_invbasdoc() {
		 return pk_invbasdoc;
	  }   
	  
     /**
	   * ����pk_invbasdoc��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newPk_invbasdoc String
	   */
	public void setPk_invbasdoc(String newPk_invbasdoc) {
		
		pk_invbasdoc = newPk_invbasdoc;
	 } 	  
       
        /**
	   * ����vdef2��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getVdef2() {
		 return vdef2;
	  }   
	  
     /**
	   * ����vdef2��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newVdef2 String
	   */
	public void setVdef2(String newVdef2) {
		
		vdef2 = newVdef2;
	 } 	  
       
        /**
	   * ����reserve11��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return UFDate
	   */
	 public UFDate getReserve11() {
		 return reserve11;
	  }   
	  
     /**
	   * ����reserve11��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newReserve11 UFDate
	   */
	public void setReserve11(UFDate newReserve11) {
		
		reserve11 = newReserve11;
	 } 	  
       
        /**
	   * ����vmemo��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getVmemo() {
		 return vmemo;
	  }   
	  
     /**
	   * ����vmemo��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newVmemo String
	   */
	public void setVmemo(String newVmemo) {
		
		vmemo = newVmemo;
	 } 	  
       
        /**
	   * ����vsourcebillcode��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getVsourcebillcode() {
		 return vsourcebillcode;
	  }   
	  
     /**
	   * ����vsourcebillcode��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newVsourcebillcode String
	   */
	public void setVsourcebillcode(String newVsourcebillcode) {
		
		vsourcebillcode = newVsourcebillcode;
	 } 	  
       
        /**
	   * ����vdef1��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getVdef1() {
		 return vdef1;
	  }   
	  
     /**
	   * ����vdef1��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newVdef1 String
	   */
	public void setVdef1(String newVdef1) {
		
		vdef1 = newVdef1;
	 } 	  
       
        /**
	   * ����reserve3��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getReserve3() {
		 return reserve3;
	  }   
	  
     /**
	   * ����reserve3��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newReserve3 String
	   */
	public void setReserve3(String newReserve3) {
		
		reserve3 = newReserve3;
	 } 	  
       
        /**
	   * ����csourcebillbid��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getCsourcebillbid() {
		 return csourcebillbid;
	  }   
	  
     /**
	   * ����csourcebillbid��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newCsourcebillbid String
	   */
	public void setCsourcebillbid(String newCsourcebillbid) {
		
		csourcebillbid = newCsourcebillbid;
	 } 	  
       
        /**
	   * ����reserve13��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return UFDate
	   */
	 public UFDate getReserve13() {
		 return reserve13;
	  }   
	  
     /**
	   * ����reserve13��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newReserve13 UFDate
	   */
	public void setReserve13(UFDate newReserve13) {
		
		reserve13 = newReserve13;
	 } 	  
       
        /**
	   * ����unit��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getUnit() {
		 return unit;
	  }   
	  
     /**
	   * ����unit��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newUnit String
	   */
	public void setUnit(String newUnit) {
		
		unit = newUnit;
	 } 	  
       
        /**
	   * ����reserve8��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return UFDouble
	   */
	 public UFDouble getReserve8() {
		 return reserve8;
	  }   
	  
     /**
	   * ����reserve8��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newReserve8 UFDouble
	   */
	public void setReserve8(UFDouble newReserve8) {
		
		reserve8 = newReserve8;
	 } 	  
       
        /**
	   * ����reserve9��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return UFDouble
	   */
	 public UFDouble getReserve9() {
		 return reserve9;
	  }   
	  
     /**
	   * ����reserve9��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newReserve9 UFDouble
	   */
	public void setReserve9(UFDouble newReserve9) {
		
		reserve9 = newReserve9;
	 } 	  
       
        /**
	   * ����vdef9��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getVdef9() {
		 return vdef9;
	  }   
	  
     /**
	   * ����vdef9��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newVdef9 String
	   */
	public void setVdef9(String newVdef9) {
		
		vdef9 = newVdef9;
	 } 	  
       
        /**
	   * ����vdef8��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getVdef8() {
		 return vdef8;
	  }   
	  
     /**
	   * ����vdef8��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newVdef8 String
	   */
	public void setVdef8(String newVdef8) {
		
		vdef8 = newVdef8;
	 } 	  
       
        /**
	   * ����cfirsttype��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getCfirsttype() {
		 return cfirsttype;
	  }   
	  
     /**
	   * ����cfirsttype��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newCfirsttype String
	   */
	public void setCfirsttype(String newCfirsttype) {
		
		cfirsttype = newCfirsttype;
	 } 	  
       
        /**
	   * ����reserve7��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return UFDouble
	   */
	 public UFDouble getReserve7() {
		 return reserve7;
	  }   
	  
     /**
	   * ����reserve7��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newReserve7 UFDouble
	   */
	public void setReserve7(UFDouble newReserve7) {
		
		reserve7 = newReserve7;
	 } 	  
       
        /**
	   * ����vdef6��Getter����.
	   *
	   * ��������:2011-10-25
	   * @return String
	   */
	 public String getVdef6() {
		 return vdef6;
	  }   
	  
     /**
	   * ����vdef6��Setter����.
	   *
	   * ��������:2011-10-25
	   * @param newVdef6 String
	   */
	public void setVdef6(String newVdef6) {
		
		vdef6 = newVdef6;
	 } 	  
       
       
    /**
	  * ��֤���������֮��������߼���ȷ��.
	  *
	  * ��������:2011-10-25
	  * @exception nc.vo.pub.ValidationException �����֤ʧ��,�׳�
	  * ValidationException,�Դ�����н���.
	 */
	 public void validate() throws ValidationException {
	
	 	ArrayList errFields = new ArrayList(); // errFields record those null

                                                      // fields that cannot be null.
       		  // ����Ƿ�Ϊ�������յ��ֶθ��˿�ֵ,�������Ҫ�޸��������ʾ��Ϣ:
	
	   		if (pk_wds_writeback4c_b2 == null) {
			errFields.add(new String("pk_wds_writeback4c_b2"));
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
	  * ��������:2011-10-25
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	  	 
	 		return "pk_wds_writeback4c_h";
	 	
	}   
    
    /**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:2011-10-25
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	 	  return "pk_wds_writeback4c_b2";
	 	}
    
	/**
      * <p>���ر�����.
	  * <p>
	  * ��������:2011-10-25
	  * @return java.lang.String
	 */
	public java.lang.String getTableName() {
				
		return "wds_writeback4c_b2";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:2011-10-25
	  */
	public Writeback4cB2VO() {
			
			   super();	
	  }    
    
            /**
	 * ʹ���������г�ʼ���Ĺ�����.
	 *
	 * ��������:2011-10-25
	 * @param newpk_wds_writeback4c_b2 ����ֵ
	 */
	 public Writeback4cB2VO(String newpk_wds_writeback4c_b2) {
		
		// Ϊ�����ֶθ�ֵ:
		 pk_wds_writeback4c_b2 = newpk_wds_writeback4c_b2;
	
    	}
    
     
     /**
	  * ���ض����ʶ,����Ψһ��λ����.
	  *
	  * ��������:2011-10-25
	  * @return String
	  */
	   public String getPrimaryKey() {
				
		 return pk_wds_writeback4c_b2;
	   
	   }

     /**
	  * ���ö����ʶ,����Ψһ��λ����.
	  *
	  * ��������:2011-10-25
	  * @param newpk_wds_writeback4c_b2  String    
	  */
	 public void setPrimaryKey(String newpk_wds_writeback4c_b2) {
				
				pk_wds_writeback4c_b2 = newpk_wds_writeback4c_b2; 
				
	 } 
           
	  /**
       * ������ֵ�������ʾ����.
	   *
	   * ��������:2011-10-25
	   * @return java.lang.String ������ֵ�������ʾ����.
	   */
	 public String getEntityName() {
				
	   return "pk_wds_writeback4c_b2"; 
				
	 }

	public String getPk_wds_writeback4c_b2() {
		return pk_wds_writeback4c_b2;
	}

	public void setPk_wds_writeback4c_b2(String pk_wds_writeback4c_b2) {
		this.pk_wds_writeback4c_b2 = pk_wds_writeback4c_b2;
	}

	public String getPk_wds_writeback4c_h() {
		return pk_wds_writeback4c_h;
	}

	public void setPk_wds_writeback4c_h(String pk_wds_writeback4c_h) {
		this.pk_wds_writeback4c_h = pk_wds_writeback4c_h;
	} 
} 
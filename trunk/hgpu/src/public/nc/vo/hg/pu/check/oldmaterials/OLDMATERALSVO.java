/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.hg.pu.check.oldmaterials;
/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
import nc.vo.pub.*;
import nc.vo.pub.lang.*;
	
/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * <p>
 *     �ڴ˴����Ӵ����������Ϣ
 * </p>
 * ��������:2011-11-02 13:17:28
 * @author Administrator
 * @version NCPrj 1.0
 */
@SuppressWarnings("serial")
public class OLDMATERALSVO extends SuperVO {
	private String cnewmanid;
	private UFDateTime ts;
	private String vdef1;
	private String vdef2;
	private String pk_oldmaterials;
	private String vdef5;
	private String coldbasid;
	private String cnewbasid;
	private String vdef3;
	private Integer dr;
	private String vdef4;
	private UFDouble nscale;
	private String coldmanid;
	private String coperatorid;//¼����     
    private UFDate dmakedate;//¼������
	private String  cmodifyman; //����޸���       
	private UFDate  dmodifydate;//����޸�����

	public static final String CNEWMANID = "cnewmanid";
	public static final String VDEF1 = "vdef1";
	public static final String VDEF2 = "vdef2";
	public static final String PK_OLDMATERIALS = "pk_oldmaterials";
	public static final String VDEF5 = "vdef5";
	public static final String COLDBASID = "coldbasid";
	public static final String CNEWBASID = "cnewbasid";
	public static final String VDEF3 = "vdef3";
	public static final String VDEF4 = "vdef4";
	public static final String NSCALE = "nscale";
	public static final String COLDMANID = "coldmanid";
			
	/**
	 * ����cnewmanid��Getter����.
	 * ��������:2011-11-02 13:17:28
	 * @return String
	 */
	public String getCnewmanid () {
		return cnewmanid;
	}   
	/**
	 * ����cnewmanid��Setter����.
	 * ��������:2011-11-02 13:17:28
	 * @param newCnewmanid String
	 */
	public void setCnewmanid (String newCnewmanid ) {
	 	this.cnewmanid = newCnewmanid;
	} 	  
	/**
	 * ����ts��Getter����.
	 * ��������:2011-11-02 13:17:28
	 * @return UFDateTime
	 */
	public UFDateTime getTs () {
		return ts;
	}   
	/**
	 * ����ts��Setter����.
	 * ��������:2011-11-02 13:17:28
	 * @param newTs UFDateTime
	 */
	public void setTs (UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	  
	/**
	 * ����vdef1��Getter����.
	 * ��������:2011-11-02 13:17:28
	 * @return String
	 */
	public String getVdef1 () {
		return vdef1;
	}   
	/**
	 * ����vdef1��Setter����.
	 * ��������:2011-11-02 13:17:28
	 * @param newVdef1 String
	 */
	public void setVdef1 (String newVdef1 ) {
	 	this.vdef1 = newVdef1;
	} 	  
	/**
	 * ����vdef2��Getter����.
	 * ��������:2011-11-02 13:17:28
	 * @return String
	 */
	public String getVdef2 () {
		return vdef2;
	}   
	/**
	 * ����vdef2��Setter����.
	 * ��������:2011-11-02 13:17:28
	 * @param newVdef2 String
	 */
	public void setVdef2 (String newVdef2 ) {
	 	this.vdef2 = newVdef2;
	} 	  
	/**
	 * ����pk_oldmaterials��Getter����.
	 * ��������:2011-11-02 13:17:28
	 * @return String
	 */
	public String getPk_oldmaterials () {
		return pk_oldmaterials;
	}   
	/**
	 * ����pk_oldmaterials��Setter����.
	 * ��������:2011-11-02 13:17:28
	 * @param newPk_oldmaterials String
	 */
	public void setPk_oldmaterials (String newPk_oldmaterials ) {
	 	this.pk_oldmaterials = newPk_oldmaterials;
	} 	  
	/**
	 * ����vdef5��Getter����.
	 * ��������:2011-11-02 13:17:28
	 * @return String
	 */
	public String getVdef5 () {
		return vdef5;
	}   
	/**
	 * ����vdef5��Setter����.
	 * ��������:2011-11-02 13:17:28
	 * @param newVdef5 String
	 */
	public void setVdef5 (String newVdef5 ) {
	 	this.vdef5 = newVdef5;
	} 	  
	/**
	 * ����coldbasid��Getter����.
	 * ��������:2011-11-02 13:17:28
	 * @return String
	 */
	public String getColdbasid () {
		return coldbasid;
	}   
	/**
	 * ����coldbasid��Setter����.
	 * ��������:2011-11-02 13:17:28
	 * @param newColdbasid String
	 */
	public void setColdbasid (String newColdbasid ) {
	 	this.coldbasid = newColdbasid;
	} 	  
	/**
	 * ����cnewbasid��Getter����.
	 * ��������:2011-11-02 13:17:28
	 * @return String
	 */
	public String getCnewbasid () {
		return cnewbasid;
	}   
	/**
	 * ����cnewbasid��Setter����.
	 * ��������:2011-11-02 13:17:28
	 * @param newCnewbasid String
	 */
	public void setCnewbasid (String newCnewbasid ) {
	 	this.cnewbasid = newCnewbasid;
	} 	  
	/**
	 * ����vdef3��Getter����.
	 * ��������:2011-11-02 13:17:28
	 * @return String
	 */
	public String getVdef3 () {
		return vdef3;
	}   
	/**
	 * ����vdef3��Setter����.
	 * ��������:2011-11-02 13:17:28
	 * @param newVdef3 String
	 */
	public void setVdef3 (String newVdef3 ) {
	 	this.vdef3 = newVdef3;
	} 	  
	/**
	 * ����dr��Getter����.
	 * ��������:2011-11-02 13:17:28
	 * @return UFDouble
	 */
	public Integer getDr () {
		return dr;
	}   
	/**
	 * ����dr��Setter����.
	 * ��������:2011-11-02 13:17:28
	 * @param newDr UFDouble
	 */
	public void setDr (Integer newDr ) {
	 	this.dr = newDr;
	} 	  
	/**
	 * ����vdef4��Getter����.
	 * ��������:2011-11-02 13:17:28
	 * @return String
	 */
	public String getVdef4 () {
		return vdef4;
	}   
	/**
	 * ����vdef4��Setter����.
	 * ��������:2011-11-02 13:17:28
	 * @param newVdef4 String
	 */
	public void setVdef4 (String newVdef4 ) {
	 	this.vdef4 = newVdef4;
	} 	  
	/**
	 * ����nscale��Getter����.
	 * ��������:2011-11-02 13:17:28
	 * @return UFDouble
	 */
	public UFDouble getNscale () {
		return nscale;
	}   
	/**
	 * ����nscale��Setter����.
	 * ��������:2011-11-02 13:17:28
	 * @param newNscale UFDouble
	 */
	public void setNscale (UFDouble newNscale ) {
	 	this.nscale = newNscale;
	} 	  
	/**
	 * ����coldmanid��Getter����.
	 * ��������:2011-11-02 13:17:28
	 * @return String
	 */
	public String getColdmanid () {
		return coldmanid;
	}   
	/**
	 * ����coldmanid��Setter����.
	 * ��������:2011-11-02 13:17:28
	 * @param newColdmanid String
	 */
	public void setColdmanid (String newColdmanid ) {
	 	this.coldmanid = newColdmanid;
	} 	  
 
	/**
	  * <p>ȡ�ø�VO�����ֶ�.
	  * <p>
	  * ��������:2011-11-02 13:17:28
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:2011-11-02 13:17:28
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "pk_oldmaterials";
	}
    
	/**
	 * <p>���ر�����.
	 * <p>
	 * ��������:2011-11-02 13:17:28
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "HG_OLDMATERIALS";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:2011-11-02 13:17:28
	  */
     public OLDMATERALSVO() {
		super();	
	}
	public String getCoperatorid() {
		return coperatorid;
	}
	public void setCoperatorid(String coperatorid) {
		this.coperatorid = coperatorid;
	}
	public UFDate getDmakedate() {
		return dmakedate;
	}
	public void setDmakedate(UFDate dmakedate) {
		this.dmakedate = dmakedate;
	}
	public String getCmodifyman() {
		return cmodifyman;
	}
	public void setCmodifyman(String cmodifyman) {
		this.cmodifyman = cmodifyman;
	}
	public UFDate getDmodifydate() {
		return dmodifydate;
	}
	public void setDmodifydate(UFDate dmodifydate) {
		this.dmodifydate = dmodifydate;
	}    
} 

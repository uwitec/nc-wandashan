/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.zb.avnum;
	
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
	
/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * <p>
 *     �ڴ˴����Ӵ����������Ϣ
 * </p>
 * ��������:2011-05-21 13:11:49
 * @author Administrator
 * @version NCPrj 1.0
 */
@SuppressWarnings("serial")
public class AvNumHeadVO extends SuperVO {
	
	private String vemployeeid;//ҵ��Ա
	private String vbillno;//���������
	private UFDate dbilldate;//��������
	private String voperatorid;//�Ƶ���
	private String cbiddingid;//�������
	private String vapproveid;//������
	private String pk_deptdoc;//�б겿��
	private UFDate dmakedate;//�Ƶ�����
	private String pk_billtype;//��������
	private Integer vbillstatus;//����״̬
	private String vmemo;//��ע
	private UFDate dapprovedate;//��������
	
	private String pk_defdoc19;
	private String pk_corp;
	private UFDateTime ts;
	private UFDouble reserve10;
	private String pk_defdoc10;
	private UFBoolean reserve15;
	private String vdef9;
	private String vdef10;
	private String pk_defdoc18;
	private String reserve2;
	private UFDouble reserve8;
	private String vdef15;
	private String pk_defdoc8;
	private String pk_defdoc5;
	private String vdef14;
	private String cavnumid;
	private UFDouble reserve7;
	private String vdef7;
	private String pk_defdoc7;
	private UFDate reserve11;
	private String vdef2;
	private String vdef16;
	private UFDouble reserve9;
	private String vdef5;
	private String pk_defdoc12;
	private String vdef19;
	private String pk_defdoc13;
	private String pk_defdoc6;
	private String vdef4;
	private String vdef18;
	private String vdef17;
	private String pk_defdoc15;
	private String pk_defdoc2;
	private String vdef20;
	private String vdef1;
	private String vdef8;
	private String reserve5;
	private String pk_busitype;
	private String pk_defdoc3;
	private String vapprovenote;
	private String pk_defdoc17;
	private UFDate reserve12;
	private String pk_defdoc16;
	private UFDouble reserve6;
	private String reserve1;
	private String vdef13;
	private UFBoolean reserve16;
	private UFBoolean reserve14;
	private String pk_defdoc11;
	private String pk_defdoc9;
	private UFDate reserve13;
	private String vdef11;
	private String pk_defdoc14;
	private String pk_defdoc20;
	private String pk_defdoc4;
	private String vdef3;
	private String vdef12;
	private String vdef6;
	private String reserve3;
	private Integer dr;
	private String pk_defdoc1;
	private String reserve4;

	public static final String PK_DEFDOC19 = "pk_defdoc19";
	public static final String PK_CORP = "pk_corp";
	public static final String RESERVE10 = "reserve10";
	public static final String PK_DEFDOC10 = "pk_defdoc10";
	public static final String RESERVE15 = "reserve15";
	public static final String VDEF9 = "vdef9";
	public static final String VEMPLOYEEID = "vemployeeid";
	public static final String VDEF10 = "vdef10";
	public static final String PK_DEFDOC18 = "pk_defdoc18";
	public static final String RESERVE2 = "reserve2";
	public static final String RESERVE8 = "reserve8";
	public static final String VDEF15 = "vdef15";
	public static final String PK_DEFDOC8 = "pk_defdoc8";
	public static final String PK_DEFDOC5 = "pk_defdoc5";
	public static final String DBILLDATE = "dbilldate";
	public static final String VDEF14 = "vdef14";
	public static final String CAVNUMID = "cavnumid";
	public static final String RESERVE7 = "reserve7";
	public static final String VDEF7 = "vdef7";
	public static final String PK_DEFDOC7 = "pk_defdoc7";
	public static final String RESERVE11 = "reserve11";
	public static final String VDEF2 = "vdef2";
	public static final String VDEF16 = "vdef16";
	public static final String RESERVE9 = "reserve9";
	public static final String VDEF5 = "vdef5";
	public static final String VMEMO = "vmemo";
	public static final String PK_DEFDOC12 = "pk_defdoc12";
	public static final String VDEF19 = "vdef19";
	public static final String VOPERATORID = "voperatorid";
	public static final String PK_DEFDOC13 = "pk_defdoc13";
	public static final String VBILLNO = "vbillno";
	public static final String PK_DEFDOC6 = "pk_defdoc6";
	public static final String VDEF4 = "vdef4";
	public static final String VDEF18 = "vdef18";
	public static final String VDEF17 = "vdef17";
	public static final String PK_DEFDOC15 = "pk_defdoc15";
	public static final String PK_DEFDOC2 = "pk_defdoc2";
	public static final String VDEF20 = "vdef20";
	public static final String VDEF1 = "vdef1";
	public static final String VDEF8 = "vdef8";
	public static final String CBIDDINGID = "cbiddingid";
	public static final String RESERVE5 = "reserve5";
	public static final String PK_BUSITYPE = "pk_busitype";
	public static final String PK_DEFDOC3 = "pk_defdoc3";
	public static final String VAPPROVEID = "vapproveid";
	public static final String PK_DEPTDOC = "pk_deptdoc";
	public static final String VAPPROVENOTE = "vapprovenote";
	public static final String PK_DEFDOC17 = "pk_defdoc17";
	public static final String RESERVE12 = "reserve12";
	public static final String PK_DEFDOC16 = "pk_defdoc16";
	public static final String RESERVE6 = "reserve6";
	public static final String DAPPROVEDATE = "dapprovedate";
	public static final String RESERVE1 = "reserve1";
	public static final String VDEF13 = "vdef13";
	public static final String RESERVE16 = "reserve16";
	public static final String RESERVE14 = "reserve14";
	public static final String PK_DEFDOC11 = "pk_defdoc11";
	public static final String PK_DEFDOC9 = "pk_defdoc9";
	public static final String RESERVE13 = "reserve13";
	public static final String VDEF11 = "vdef11";
	public static final String PK_DEFDOC14 = "pk_defdoc14";
	public static final String PK_DEFDOC20 = "pk_defdoc20";
	public static final String PK_DEFDOC4 = "pk_defdoc4";
	public static final String PK_BILLTYPE = "pk_billtype";
	public static final String VBILLSTATUS = "vbillstatus";
	public static final String DMAKEDATE = "dmakedate";
	public static final String VDEF3 = "vdef3";
	public static final String VDEF12 = "vdef12";
	public static final String VDEF6 = "vdef6";
	public static final String RESERVE3 = "reserve3";
	public static final String PK_DEFDOC1 = "pk_defdoc1";
	public static final String RESERVE4 = "reserve4";
			
	/**
	 * ����pk_defdoc19��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_defdoc19 () {
		return pk_defdoc19;
	}   
	/**
	 * ����pk_defdoc19��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_defdoc19 String
	 */
	public void setPk_defdoc19 (String newPk_defdoc19 ) {
	 	this.pk_defdoc19 = newPk_defdoc19;
	} 	  
	/**
	 * ����pk_corp��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_corp () {
		return pk_corp;
	}   
	/**
	 * ����pk_corp��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_corp String
	 */
	public void setPk_corp (String newPk_corp ) {
	 	this.pk_corp = newPk_corp;
	} 	  
	/**
	 * ����ts��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return UFDateTime
	 */
	public UFDateTime getTs () {
		return ts;
	}   
	/**
	 * ����ts��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newTs UFDateTime
	 */
	public void setTs (UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	  
	/**
	 * ����reserve10��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return UFDouble
	 */
	public UFDouble getReserve10 () {
		return reserve10;
	}   
	/**
	 * ����reserve10��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newReserve10 UFDouble
	 */
	public void setReserve10 (UFDouble newReserve10 ) {
	 	this.reserve10 = newReserve10;
	} 	  
	/**
	 * ����pk_defdoc10��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_defdoc10 () {
		return pk_defdoc10;
	}   
	/**
	 * ����pk_defdoc10��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_defdoc10 String
	 */
	public void setPk_defdoc10 (String newPk_defdoc10 ) {
	 	this.pk_defdoc10 = newPk_defdoc10;
	} 	  
	/**
	 * ����reserve15��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return UFBoolean
	 */
	public UFBoolean getReserve15 () {
		return reserve15;
	}   
	/**
	 * ����reserve15��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newReserve15 UFBoolean
	 */
	public void setReserve15 (UFBoolean newReserve15 ) {
	 	this.reserve15 = newReserve15;
	} 	  
	/**
	 * ����vdef9��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVdef9 () {
		return vdef9;
	}   
	/**
	 * ����vdef9��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVdef9 String
	 */
	public void setVdef9 (String newVdef9 ) {
	 	this.vdef9 = newVdef9;
	} 	  
	/**
	 * ����vemployeeid��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVemployeeid () {
		return vemployeeid;
	}   
	/**
	 * ����vemployeeid��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVemployeeid String
	 */
	public void setVemployeeid (String newVemployeeid ) {
	 	this.vemployeeid = newVemployeeid;
	} 	  
	/**
	 * ����vdef10��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVdef10 () {
		return vdef10;
	}   
	/**
	 * ����vdef10��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVdef10 String
	 */
	public void setVdef10 (String newVdef10 ) {
	 	this.vdef10 = newVdef10;
	} 	  
	/**
	 * ����pk_defdoc18��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_defdoc18 () {
		return pk_defdoc18;
	}   
	/**
	 * ����pk_defdoc18��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_defdoc18 String
	 */
	public void setPk_defdoc18 (String newPk_defdoc18 ) {
	 	this.pk_defdoc18 = newPk_defdoc18;
	} 	  
	/**
	 * ����reserve2��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getReserve2 () {
		return reserve2;
	}   
	/**
	 * ����reserve2��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newReserve2 String
	 */
	public void setReserve2 (String newReserve2 ) {
	 	this.reserve2 = newReserve2;
	} 	  
	/**
	 * ����reserve8��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return UFDouble
	 */
	public UFDouble getReserve8 () {
		return reserve8;
	}   
	/**
	 * ����reserve8��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newReserve8 UFDouble
	 */
	public void setReserve8 (UFDouble newReserve8 ) {
	 	this.reserve8 = newReserve8;
	} 	  
	/**
	 * ����vdef15��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVdef15 () {
		return vdef15;
	}   
	/**
	 * ����vdef15��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVdef15 String
	 */
	public void setVdef15 (String newVdef15 ) {
	 	this.vdef15 = newVdef15;
	} 	  
	/**
	 * ����pk_defdoc8��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_defdoc8 () {
		return pk_defdoc8;
	}   
	/**
	 * ����pk_defdoc8��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_defdoc8 String
	 */
	public void setPk_defdoc8 (String newPk_defdoc8 ) {
	 	this.pk_defdoc8 = newPk_defdoc8;
	} 	  
	/**
	 * ����pk_defdoc5��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_defdoc5 () {
		return pk_defdoc5;
	}   
	/**
	 * ����pk_defdoc5��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_defdoc5 String
	 */
	public void setPk_defdoc5 (String newPk_defdoc5 ) {
	 	this.pk_defdoc5 = newPk_defdoc5;
	} 	  
	/**
	 * ����dbilldate��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return UFDate
	 */
	public UFDate getDbilldate () {
		return dbilldate;
	}   
	/**
	 * ����dbilldate��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newDbilldate UFDate
	 */
	public void setDbilldate (UFDate newDbilldate ) {
	 	this.dbilldate = newDbilldate;
	} 	  
	/**
	 * ����vdef14��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVdef14 () {
		return vdef14;
	}   
	/**
	 * ����vdef14��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVdef14 String
	 */
	public void setVdef14 (String newVdef14 ) {
	 	this.vdef14 = newVdef14;
	} 	  
	/**
	 * ����cavnumid��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getCavnumid () {
		return cavnumid;
	}   
	/**
	 * ����cavnumid��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newCavnumid String
	 */
	public void setCavnumid (String newCavnumid ) {
	 	this.cavnumid = newCavnumid;
	} 	  
	/**
	 * ����reserve7��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return UFDouble
	 */
	public UFDouble getReserve7 () {
		return reserve7;
	}   
	/**
	 * ����reserve7��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newReserve7 UFDouble
	 */
	public void setReserve7 (UFDouble newReserve7 ) {
	 	this.reserve7 = newReserve7;
	} 	  
	/**
	 * ����vdef7��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVdef7 () {
		return vdef7;
	}   
	/**
	 * ����vdef7��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVdef7 String
	 */
	public void setVdef7 (String newVdef7 ) {
	 	this.vdef7 = newVdef7;
	} 	  
	/**
	 * ����pk_defdoc7��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_defdoc7 () {
		return pk_defdoc7;
	}   
	/**
	 * ����pk_defdoc7��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_defdoc7 String
	 */
	public void setPk_defdoc7 (String newPk_defdoc7 ) {
	 	this.pk_defdoc7 = newPk_defdoc7;
	} 	  
	/**
	 * ����reserve11��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return UFDate
	 */
	public UFDate getReserve11 () {
		return reserve11;
	}   
	/**
	 * ����reserve11��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newReserve11 UFDate
	 */
	public void setReserve11 (UFDate newReserve11 ) {
	 	this.reserve11 = newReserve11;
	} 	  
	/**
	 * ����vdef2��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVdef2 () {
		return vdef2;
	}   
	/**
	 * ����vdef2��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVdef2 String
	 */
	public void setVdef2 (String newVdef2 ) {
	 	this.vdef2 = newVdef2;
	} 	  
	/**
	 * ����vdef16��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVdef16 () {
		return vdef16;
	}   
	/**
	 * ����vdef16��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVdef16 String
	 */
	public void setVdef16 (String newVdef16 ) {
	 	this.vdef16 = newVdef16;
	} 	  
	/**
	 * ����reserve9��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return UFDouble
	 */
	public UFDouble getReserve9 () {
		return reserve9;
	}   
	/**
	 * ����reserve9��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newReserve9 UFDouble
	 */
	public void setReserve9 (UFDouble newReserve9 ) {
	 	this.reserve9 = newReserve9;
	} 	  
	/**
	 * ����vdef5��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVdef5 () {
		return vdef5;
	}   
	/**
	 * ����vdef5��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVdef5 String
	 */
	public void setVdef5 (String newVdef5 ) {
	 	this.vdef5 = newVdef5;
	} 	  
	/**
	 * ����vmemo��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVmemo () {
		return vmemo;
	}   
	/**
	 * ����vmemo��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVmemo String
	 */
	public void setVmemo (String newVmemo ) {
	 	this.vmemo = newVmemo;
	} 	  
	/**
	 * ����pk_defdoc12��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_defdoc12 () {
		return pk_defdoc12;
	}   
	/**
	 * ����pk_defdoc12��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_defdoc12 String
	 */
	public void setPk_defdoc12 (String newPk_defdoc12 ) {
	 	this.pk_defdoc12 = newPk_defdoc12;
	} 	  
	/**
	 * ����vdef19��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVdef19 () {
		return vdef19;
	}   
	/**
	 * ����vdef19��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVdef19 String
	 */
	public void setVdef19 (String newVdef19 ) {
	 	this.vdef19 = newVdef19;
	} 	  
	/**
	 * ����voperatorid��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVoperatorid () {
		return voperatorid;
	}   
	/**
	 * ����voperatorid��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVoperatorid String
	 */
	public void setVoperatorid (String newVoperatorid ) {
	 	this.voperatorid = newVoperatorid;
	} 	  
	/**
	 * ����pk_defdoc13��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_defdoc13 () {
		return pk_defdoc13;
	}   
	/**
	 * ����pk_defdoc13��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_defdoc13 String
	 */
	public void setPk_defdoc13 (String newPk_defdoc13 ) {
	 	this.pk_defdoc13 = newPk_defdoc13;
	} 	  
	/**
	 * ����vbillno��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVbillno () {
		return vbillno;
	}   
	/**
	 * ����vbillno��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVbillno String
	 */
	public void setVbillno (String newVbillno ) {
	 	this.vbillno = newVbillno;
	} 	  
	/**
	 * ����pk_defdoc6��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_defdoc6 () {
		return pk_defdoc6;
	}   
	/**
	 * ����pk_defdoc6��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_defdoc6 String
	 */
	public void setPk_defdoc6 (String newPk_defdoc6 ) {
	 	this.pk_defdoc6 = newPk_defdoc6;
	} 	  
	/**
	 * ����vdef4��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVdef4 () {
		return vdef4;
	}   
	/**
	 * ����vdef4��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVdef4 String
	 */
	public void setVdef4 (String newVdef4 ) {
	 	this.vdef4 = newVdef4;
	} 	  
	/**
	 * ����vdef18��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVdef18 () {
		return vdef18;
	}   
	/**
	 * ����vdef18��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVdef18 String
	 */
	public void setVdef18 (String newVdef18 ) {
	 	this.vdef18 = newVdef18;
	} 	  
	/**
	 * ����vdef17��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVdef17 () {
		return vdef17;
	}   
	/**
	 * ����vdef17��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVdef17 String
	 */
	public void setVdef17 (String newVdef17 ) {
	 	this.vdef17 = newVdef17;
	} 	  
	/**
	 * ����pk_defdoc15��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_defdoc15 () {
		return pk_defdoc15;
	}   
	/**
	 * ����pk_defdoc15��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_defdoc15 String
	 */
	public void setPk_defdoc15 (String newPk_defdoc15 ) {
	 	this.pk_defdoc15 = newPk_defdoc15;
	} 	  
	/**
	 * ����pk_defdoc2��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_defdoc2 () {
		return pk_defdoc2;
	}   
	/**
	 * ����pk_defdoc2��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_defdoc2 String
	 */
	public void setPk_defdoc2 (String newPk_defdoc2 ) {
	 	this.pk_defdoc2 = newPk_defdoc2;
	} 	  
	/**
	 * ����vdef20��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVdef20 () {
		return vdef20;
	}   
	/**
	 * ����vdef20��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVdef20 String
	 */
	public void setVdef20 (String newVdef20 ) {
	 	this.vdef20 = newVdef20;
	} 	  
	/**
	 * ����vdef1��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVdef1 () {
		return vdef1;
	}   
	/**
	 * ����vdef1��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVdef1 String
	 */
	public void setVdef1 (String newVdef1 ) {
	 	this.vdef1 = newVdef1;
	} 	  
	/**
	 * ����vdef8��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVdef8 () {
		return vdef8;
	}   
	/**
	 * ����vdef8��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVdef8 String
	 */
	public void setVdef8 (String newVdef8 ) {
	 	this.vdef8 = newVdef8;
	} 	  
	/**
	 * ����cbiddingid��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getCbiddingid () {
		return cbiddingid;
	}   
	/**
	 * ����cbiddingid��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newCbiddingid String
	 */
	public void setCbiddingid (String newCbiddingid ) {
	 	this.cbiddingid = newCbiddingid;
	} 	  
	/**
	 * ����reserve5��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getReserve5 () {
		return reserve5;
	}   
	/**
	 * ����reserve5��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newReserve5 String
	 */
	public void setReserve5 (String newReserve5 ) {
	 	this.reserve5 = newReserve5;
	} 	  
	/**
	 * ����pk_busitype��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_busitype () {
		return pk_busitype;
	}   
	/**
	 * ����pk_busitype��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_busitype String
	 */
	public void setPk_busitype (String newPk_busitype ) {
	 	this.pk_busitype = newPk_busitype;
	} 	  
	/**
	 * ����pk_defdoc3��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_defdoc3 () {
		return pk_defdoc3;
	}   
	/**
	 * ����pk_defdoc3��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_defdoc3 String
	 */
	public void setPk_defdoc3 (String newPk_defdoc3 ) {
	 	this.pk_defdoc3 = newPk_defdoc3;
	} 	  
	/**
	 * ����vapproveid��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVapproveid () {
		return vapproveid;
	}   
	/**
	 * ����vapproveid��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVapproveid String
	 */
	public void setVapproveid (String newVapproveid ) {
	 	this.vapproveid = newVapproveid;
	} 	  
	/**
	 * ����pk_deptdoc��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_deptdoc () {
		return pk_deptdoc;
	}   
	/**
	 * ����pk_deptdoc��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_deptdoc String
	 */
	public void setPk_deptdoc (String newPk_deptdoc ) {
	 	this.pk_deptdoc = newPk_deptdoc;
	} 	  
	/**
	 * ����vapprovenote��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVapprovenote () {
		return vapprovenote;
	}   
	/**
	 * ����vapprovenote��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVapprovenote String
	 */
	public void setVapprovenote (String newVapprovenote ) {
	 	this.vapprovenote = newVapprovenote;
	} 	  
	/**
	 * ����pk_defdoc17��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_defdoc17 () {
		return pk_defdoc17;
	}   
	/**
	 * ����pk_defdoc17��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_defdoc17 String
	 */
	public void setPk_defdoc17 (String newPk_defdoc17 ) {
	 	this.pk_defdoc17 = newPk_defdoc17;
	} 	  
	/**
	 * ����reserve12��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return UFDate
	 */
	public UFDate getReserve12 () {
		return reserve12;
	}   
	/**
	 * ����reserve12��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newReserve12 UFDate
	 */
	public void setReserve12 (UFDate newReserve12 ) {
	 	this.reserve12 = newReserve12;
	} 	  
	/**
	 * ����pk_defdoc16��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_defdoc16 () {
		return pk_defdoc16;
	}   
	/**
	 * ����pk_defdoc16��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_defdoc16 String
	 */
	public void setPk_defdoc16 (String newPk_defdoc16 ) {
	 	this.pk_defdoc16 = newPk_defdoc16;
	} 	  
	/**
	 * ����reserve6��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return UFDouble
	 */
	public UFDouble getReserve6 () {
		return reserve6;
	}   
	/**
	 * ����reserve6��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newReserve6 UFDouble
	 */
	public void setReserve6 (UFDouble newReserve6 ) {
	 	this.reserve6 = newReserve6;
	} 	  
	/**
	 * ����dapprovedate��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return UFDate
	 */
	public UFDate getDapprovedate () {
		return dapprovedate;
	}   
	/**
	 * ����dapprovedate��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newDapprovedate UFDate
	 */
	public void setDapprovedate (UFDate newDapprovedate ) {
	 	this.dapprovedate = newDapprovedate;
	} 	  
	/**
	 * ����reserve1��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getReserve1 () {
		return reserve1;
	}   
	/**
	 * ����reserve1��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newReserve1 String
	 */
	public void setReserve1 (String newReserve1 ) {
	 	this.reserve1 = newReserve1;
	} 	  
	/**
	 * ����vdef13��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVdef13 () {
		return vdef13;
	}   
	/**
	 * ����vdef13��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVdef13 String
	 */
	public void setVdef13 (String newVdef13 ) {
	 	this.vdef13 = newVdef13;
	} 	  
	/**
	 * ����reserve16��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return UFBoolean
	 */
	public UFBoolean getReserve16 () {
		return reserve16;
	}   
	/**
	 * ����reserve16��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newReserve16 UFBoolean
	 */
	public void setReserve16 (UFBoolean newReserve16 ) {
	 	this.reserve16 = newReserve16;
	} 	  
	/**
	 * ����reserve14��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return UFBoolean
	 */
	public UFBoolean getReserve14 () {
		return reserve14;
	}   
	/**
	 * ����reserve14��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newReserve14 UFBoolean
	 */
	public void setReserve14 (UFBoolean newReserve14 ) {
	 	this.reserve14 = newReserve14;
	} 	  
	/**
	 * ����pk_defdoc11��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_defdoc11 () {
		return pk_defdoc11;
	}   
	/**
	 * ����pk_defdoc11��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_defdoc11 String
	 */
	public void setPk_defdoc11 (String newPk_defdoc11 ) {
	 	this.pk_defdoc11 = newPk_defdoc11;
	} 	  
	/**
	 * ����pk_defdoc9��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_defdoc9 () {
		return pk_defdoc9;
	}   
	/**
	 * ����pk_defdoc9��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_defdoc9 String
	 */
	public void setPk_defdoc9 (String newPk_defdoc9 ) {
	 	this.pk_defdoc9 = newPk_defdoc9;
	} 	  
	/**
	 * ����reserve13��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return UFDate
	 */
	public UFDate getReserve13 () {
		return reserve13;
	}   
	/**
	 * ����reserve13��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newReserve13 UFDate
	 */
	public void setReserve13 (UFDate newReserve13 ) {
	 	this.reserve13 = newReserve13;
	} 	  
	/**
	 * ����vdef11��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVdef11 () {
		return vdef11;
	}   
	/**
	 * ����vdef11��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVdef11 String
	 */
	public void setVdef11 (String newVdef11 ) {
	 	this.vdef11 = newVdef11;
	} 	  
	/**
	 * ����pk_defdoc14��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_defdoc14 () {
		return pk_defdoc14;
	}   
	/**
	 * ����pk_defdoc14��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_defdoc14 String
	 */
	public void setPk_defdoc14 (String newPk_defdoc14 ) {
	 	this.pk_defdoc14 = newPk_defdoc14;
	} 	  
	/**
	 * ����pk_defdoc20��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_defdoc20 () {
		return pk_defdoc20;
	}   
	/**
	 * ����pk_defdoc20��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_defdoc20 String
	 */
	public void setPk_defdoc20 (String newPk_defdoc20 ) {
	 	this.pk_defdoc20 = newPk_defdoc20;
	} 	  
	/**
	 * ����pk_defdoc4��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_defdoc4 () {
		return pk_defdoc4;
	}   
	/**
	 * ����pk_defdoc4��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_defdoc4 String
	 */
	public void setPk_defdoc4 (String newPk_defdoc4 ) {
	 	this.pk_defdoc4 = newPk_defdoc4;
	} 	  
	/**
	 * ����pk_billtype��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_billtype () {
		return pk_billtype;
	}   
	/**
	 * ����pk_billtype��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_billtype String
	 */
	public void setPk_billtype (String newPk_billtype ) {
	 	this.pk_billtype = newPk_billtype;
	} 	  
	/**
	 * ����vbillstatus��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return UFDouble
	 */
	public Integer getVbillstatus () {
		return vbillstatus;
	}   
	/**
	 * ����vbillstatus��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVbillstatus UFDouble
	 */
	public void setVbillstatus (Integer newVbillstatus ) {
	 	this.vbillstatus = newVbillstatus;
	} 	  
	/**
	 * ����dmakedate��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return UFDate
	 */
	public UFDate getDmakedate () {
		return dmakedate;
	}   
	/**
	 * ����dmakedate��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newDmakedate UFDate
	 */
	public void setDmakedate (UFDate newDmakedate ) {
	 	this.dmakedate = newDmakedate;
	} 	  
	/**
	 * ����vdef3��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVdef3 () {
		return vdef3;
	}   
	/**
	 * ����vdef3��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVdef3 String
	 */
	public void setVdef3 (String newVdef3 ) {
	 	this.vdef3 = newVdef3;
	} 	  
	/**
	 * ����vdef12��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVdef12 () {
		return vdef12;
	}   
	/**
	 * ����vdef12��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVdef12 String
	 */
	public void setVdef12 (String newVdef12 ) {
	 	this.vdef12 = newVdef12;
	} 	  
	/**
	 * ����vdef6��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getVdef6 () {
		return vdef6;
	}   
	/**
	 * ����vdef6��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newVdef6 String
	 */
	public void setVdef6 (String newVdef6 ) {
	 	this.vdef6 = newVdef6;
	} 	  
	/**
	 * ����reserve3��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getReserve3 () {
		return reserve3;
	}   
	/**
	 * ����reserve3��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newReserve3 String
	 */
	public void setReserve3 (String newReserve3 ) {
	 	this.reserve3 = newReserve3;
	} 	  
	/**
	 * ����dr��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return UFDouble
	 */
	public Integer getDr () {
		return dr;
	}   
	/**
	 * ����dr��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newDr UFDouble
	 */
	public void setDr (Integer newDr ) {
	 	this.dr = newDr;
	} 	  
	/**
	 * ����pk_defdoc1��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getPk_defdoc1 () {
		return pk_defdoc1;
	}   
	/**
	 * ����pk_defdoc1��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newPk_defdoc1 String
	 */
	public void setPk_defdoc1 (String newPk_defdoc1 ) {
	 	this.pk_defdoc1 = newPk_defdoc1;
	} 	  
	/**
	 * ����reserve4��Getter����.
	 * ��������:2011-05-21 13:11:49
	 * @return String
	 */
	public String getReserve4 () {
		return reserve4;
	}   
	/**
	 * ����reserve4��Setter����.
	 * ��������:2011-05-21 13:11:49
	 * @param newReserve4 String
	 */
	public void setReserve4 (String newReserve4 ) {
	 	this.reserve4 = newReserve4;
	} 	  
 
	/**
	  * <p>ȡ�ø�VO�����ֶ�.
	  * <p>
	  * ��������:2011-05-21 13:11:49
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:2011-05-21 13:11:49
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "cavnumid";
	}
    
	/**
	 * <p>���ر�����.
	 * <p>
	 * ��������:2011-05-21 13:11:49
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "zb_avnum_h";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:2011-05-21 13:11:49
	  */
     public AvNumHeadVO() {
		super();	
	}    
} 
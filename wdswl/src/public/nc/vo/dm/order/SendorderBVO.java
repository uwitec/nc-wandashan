/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.dm.order;

import java.util.ArrayList;

import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * 
 * <p>
 * ���˶����ӱ�
 * </p>
 * 
 * ��������:2011-3-24
 * 
 * @author author
 * @version Your Project 1.0
 */
public class SendorderBVO extends SuperVO {
	private static final long serialVersionUID = 4752008406168776524L;
	public String reserve5;
	public UFDateTime ts;
	//��������λid
	public String assunit;
	public String reserve4;
	//�ƻ�������
	public UFDouble nplannum;

	public String vdef4;
	public String vdef7;
	//�ջ�վ����������
	public UFDouble nonlinenum;
	//�ƻ�������
	public UFDouble nassplannum;
	//�����������
	public String pk_invbasdoc;
	public String vdef2;
	public UFDate reserve11;
	public UFDate reserve12;
	//��ע
	public String vmemo;
	public UFDouble reserve6;
	public String vdef1;
	public String reserve3;
	//�ӱ�����
	public String pk_sendorder_b;
	public UFBoolean reserve14;
	public UFDouble reserve10;
	public UFDate reserve13;
	//����վ�ѷ�������
	public UFDouble noutsendnum;
	//��������λid
	public String unit;
	public UFDouble reserve8;
	public UFDouble reserve9;
	public String vdef3;
	public String vdef9;
	//��������
	public String pk_sendorder;
	public String vdef8;
	public String reserve1;
	//����վ����������
	public UFDouble noutkeepnum;
	public UFBoolean reserve16;
	public UFDouble reserve7;
	public UFBoolean reserve15;
	//�����������
	public String pk_invmandoc;
	public String vdef6;
	public String reserve2;
	public Integer dr;
	//�ջ�վ��������
	public UFDouble ninacceptnum;
	public String vdef10;
	public String vdef5;
	//Դͷ���ݱ���id
	public String cfirstbillbid;
	//��Դ�����к�
	public String vsourcerowno;
	//Դͷ���ݱ�ͷid
	public String cfirstbillhid;
	//Դͷ�����к�
	public String vfirstrowno;
	//��Դ���ݺ�
	public String vsourcebillcode;
	//Դͷ�����Ƶ�����
	public UFDate dfirstbilldate;
	//��Դ���ݱ������к�	
	public String csourcebillbid;
	//Դͷ��������
	public String cfirsttype;
	//��Դ���ݱ�ͷ���к�
	public String csourcebillhid;
	//Դͷ���ݺ�
	public String vfirstbillcode;
	//��Դ��������
	public String csourcetype;
	//��������
	public UFDouble ndealnum;
	//���Ÿ�����
	public UFDouble nassdealnum;
	//�ѳ�������
	public UFDouble noutnum;
	//�ѳ��⸨����
	public UFDouble nassoutnum;
	public UFDouble nhsl;//������

//	public static final String RESERVE5 = "reserve5";
//	public static final String TS = "ts";
//	public static final String ASSUNIT = "assunit";
//	public static final String RESERVE4 = "reserve4";
//	public static final String VDEF4 = "vdef4";
//	public static final String VDEF7 = "vdef7";
//	public static final String NONLINENUM = "nonlinenum";
//	public static final String NASSPLANNUM = "nassplannum";
//	public static final String PK_INVBASDOC = "pk_invbasdoc";
//	public static final String VDEF2 = "vdef2";
//	public static final String RESERVE11 = "reserve11";
//	public static final String RESERVE12 = "reserve12";
//	public static final String VMEMO = "vmemo";
//	public static final String RESERVE6 = "reserve6";
//	public static final String VDEF1 = "vdef1";
//	public static final String RESERVE3 = "reserve3";
//	public static final String PK_SENDORDER_B = "pk_sendorder_b";
//	public static final String RESERVE14 = "reserve14";
//	public static final String RESERVE10 = "reserve10";
//	public static final String RESERVE13 = "reserve13";
//	public static final String NOUTSENDNUM = "noutsendnum";
//	public static final String UNIT = "unit";
//	public static final String RESERVE8 = "reserve8";
//	public static final String RESERVE9 = "reserve9";
//	public static final String VDEF3 = "vdef3";
//	public static final String VDEF9 = "vdef9";
//	public static final String PK_SENDORDER = "pk_sendorder";
//	public static final String VDEF8 = "vdef8";
//	public static final String RESERVE1 = "reserve1";
//	public static final String NOUTKEEPNUM = "noutkeepnum";
//	public static final String RESERVE16 = "reserve16";
//	public static final String RESERVE7 = "reserve7";
//	public static final String RESERVE15 = "reserve15";
//	public static final String PK_INVMANDOC = "pk_invmandoc";
//	public static final String VDEF6 = "vdef6";
//	public static final String RESERVE2 = "reserve2";
//	public static final String DR = "dr";
//	public static final String NINACCEPTNUM = "ninacceptnum";
//	public static final String VDEF10 = "vdef10";
//	public static final String VDEF5 = "vdef5";

	public UFDouble getNplannum() {
		return nplannum;
	}

	public void setNplannum(UFDouble nplannum) {
		this.nplannum = nplannum;
	}

	public String getCfirstbillbid() {
		return cfirstbillbid;
	}

	public void setCfirstbillbid(String cfirstbillbid) {
		this.cfirstbillbid = cfirstbillbid;
	}

	public String getVsourcerowno() {
		return vsourcerowno;
	}

	public void setVsourcerowno(String vsourcerowno) {
		this.vsourcerowno = vsourcerowno;
	}

	public String getCfirstbillhid() {
		return cfirstbillhid;
	}

	public void setCfirstbillhid(String cfirstbillhid) {
		this.cfirstbillhid = cfirstbillhid;
	}

	public String getVfirstrowno() {
		return vfirstrowno;
	}

	public void setVfirstrowno(String vfirstrowno) {
		this.vfirstrowno = vfirstrowno;
	}

	public String getVsourcebillcode() {
		return vsourcebillcode;
	}

	public void setVsourcebillcode(String vsourcebillcode) {
		this.vsourcebillcode = vsourcebillcode;
	}

	public UFDate getDfirstbilldate() {
		return dfirstbilldate;
	}

	public void setDfirstbilldate(UFDate dfirstbilldate) {
		this.dfirstbilldate = dfirstbilldate;
	}

	public String getCsourcebillbid() {
		return csourcebillbid;
	}

	public void setCsourcebillbid(String csourcebillbid) {
		this.csourcebillbid = csourcebillbid;
	}

	public String getCfirsttype() {
		return cfirsttype;
	}

	public void setCfirsttype(String cfirsttype) {
		this.cfirsttype = cfirsttype;
	}

	public String getCsourcebillhid() {
		return csourcebillhid;
	}

	public void setCsourcebillhid(String csourcebillhid) {
		this.csourcebillhid = csourcebillhid;
	}

	public String getVfirstbillcode() {
		return vfirstbillcode;
	}

	public void setVfirstbillcode(String vfirstbillcode) {
		this.vfirstbillcode = vfirstbillcode;
	}

	public String getCsourcetype() {
		return csourcetype;
	}

	public void setCsourcetype(String csourcetype) {
		this.csourcetype = csourcetype;
	}

	/**
	 * ����reserve5��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getReserve5() {
		return reserve5;
	}

	/**
	 * ����reserve5��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newReserve5
	 *            String
	 */
	public void setReserve5(String newReserve5) {

		reserve5 = newReserve5;
	}

	/**
	 * ����assunit��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getAssunit() {
		return assunit;
	}

	/**
	 * ����assunit��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newAssunit
	 *            String
	 */
	public void setAssunit(String newAssunit) {

		assunit = newAssunit;
	}

	/**
	 * ����reserve4��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getReserve4() {
		return reserve4;
	}

	/**
	 * ����reserve4��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newReserve4
	 *            String
	 */
	public void setReserve4(String newReserve4) {

		reserve4 = newReserve4;
	}

	/**
	 * ����vdef4��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getVdef4() {
		return vdef4;
	}

	/**
	 * ����vdef4��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newVdef4
	 *            String
	 */
	public void setVdef4(String newVdef4) {

		vdef4 = newVdef4;
	}

	/**
	 * ����vdef7��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getVdef7() {
		return vdef7;
	}

	/**
	 * ����vdef7��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newVdef7
	 *            String
	 */
	public void setVdef7(String newVdef7) {

		vdef7 = newVdef7;
	}

	/**
	 * ����nonlinenum��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return UFDouble
	 */
	public UFDouble getNonlinenum() {
		return nonlinenum;
	}

	/**
	 * ����nonlinenum��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newNonlinenum
	 *            UFDouble
	 */
	public void setNonlinenum(UFDouble newNonlinenum) {

		nonlinenum = newNonlinenum;
	}

	/**
	 * ����nassplannum��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return UFDouble
	 */
	public UFDouble getNassplannum() {
		return nassplannum;
	}

	/**
	 * ����nassplannum��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newNassplannum
	 *            UFDouble
	 */
	public void setNassplannum(UFDouble newNassplannum) {

		nassplannum = newNassplannum;
	}

	/**
	 * ����pk_invbasdoc��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getPk_invbasdoc() {
		return pk_invbasdoc;
	}

	/**
	 * ����pk_invbasdoc��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newPk_invbasdoc
	 *            String
	 */
	public void setPk_invbasdoc(String newPk_invbasdoc) {

		pk_invbasdoc = newPk_invbasdoc;
	}

	/**
	 * ����vdef2��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getVdef2() {
		return vdef2;
	}

	/**
	 * ����vdef2��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newVdef2
	 *            String
	 */
	public void setVdef2(String newVdef2) {

		vdef2 = newVdef2;
	}

	/**
	 * ����reserve11��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return UFDate
	 */
	public UFDate getReserve11() {
		return reserve11;
	}

	/**
	 * ����reserve11��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newReserve11
	 *            UFDate
	 */
	public void setReserve11(UFDate newReserve11) {

		reserve11 = newReserve11;
	}

	/**
	 * ����reserve12��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return UFDate
	 */
	public UFDate getReserve12() {
		return reserve12;
	}

	/**
	 * ����reserve12��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newReserve12
	 *            UFDate
	 */
	public void setReserve12(UFDate newReserve12) {

		reserve12 = newReserve12;
	}

	/**
	 * ����vmemo��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getVmemo() {
		return vmemo;
	}

	/**
	 * ����vmemo��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newVmemo
	 *            String
	 */
	public void setVmemo(String newVmemo) {

		vmemo = newVmemo;
	}

	/**
	 * ����reserve6��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return UFDouble
	 */
	public UFDouble getReserve6() {
		return reserve6;
	}

	/**
	 * ����reserve6��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newReserve6
	 *            UFDouble
	 */
	public void setReserve6(UFDouble newReserve6) {

		reserve6 = newReserve6;
	}

	/**
	 * ����vdef1��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getVdef1() {
		return vdef1;
	}

	/**
	 * ����vdef1��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newVdef1
	 *            String
	 */
	public void setVdef1(String newVdef1) {

		vdef1 = newVdef1;
	}

	/**
	 * ����reserve3��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getReserve3() {
		return reserve3;
	}

	/**
	 * ����reserve3��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newReserve3
	 *            String
	 */
	public void setReserve3(String newReserve3) {

		reserve3 = newReserve3;
	}

	/**
	 * ����pk_sendorder_b��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getPk_sendorder_b() {
		return pk_sendorder_b;
	}

	/**
	 * ����pk_sendorder_b��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newPk_sendorder_b
	 *            String
	 */
	public void setPk_sendorder_b(String newPk_sendorder_b) {

		pk_sendorder_b = newPk_sendorder_b;
	}

	/**
	 * ����reserve14��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return UFBoolean
	 */
	public UFBoolean getReserve14() {
		return reserve14;
	}

	/**
	 * ����reserve14��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newReserve14
	 *            UFBoolean
	 */
	public void setReserve14(UFBoolean newReserve14) {

		reserve14 = newReserve14;
	}

	/**
	 * ����reserve10��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return UFDouble
	 */
	public UFDouble getReserve10() {
		return reserve10;
	}

	/**
	 * ����reserve10��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newReserve10
	 *            UFDouble
	 */
	public void setReserve10(UFDouble newReserve10) {

		reserve10 = newReserve10;
	}

	/**
	 * ����reserve13��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return UFDate
	 */
	public UFDate getReserve13() {
		return reserve13;
	}

	/**
	 * ����reserve13��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newReserve13
	 *            UFDate
	 */
	public void setReserve13(UFDate newReserve13) {

		reserve13 = newReserve13;
	}

	/**
	 * ����noutsendnum��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return UFDouble
	 */
	public UFDouble getNoutsendnum() {
		return noutsendnum;
	}

	/**
	 * ����noutsendnum��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newNoutsendnum
	 *            UFDouble
	 */
	public void setNoutsendnum(UFDouble newNoutsendnum) {

		noutsendnum = newNoutsendnum;
	}

	/**
	 * ����unit��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * ����unit��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newUnit
	 *            String
	 */
	public void setUnit(String newUnit) {

		unit = newUnit;
	}

	/**
	 * ����reserve8��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return UFDouble
	 */
	public UFDouble getReserve8() {
		return reserve8;
	}

	/**
	 * ����reserve8��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newReserve8
	 *            UFDouble
	 */
	public void setReserve8(UFDouble newReserve8) {

		reserve8 = newReserve8;
	}

	/**
	 * ����reserve9��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return UFDouble
	 */
	public UFDouble getReserve9() {
		return reserve9;
	}

	/**
	 * ����reserve9��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newReserve9
	 *            UFDouble
	 */
	public void setReserve9(UFDouble newReserve9) {

		reserve9 = newReserve9;
	}

	/**
	 * ����vdef3��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getVdef3() {
		return vdef3;
	}

	/**
	 * ����vdef3��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newVdef3
	 *            String
	 */
	public void setVdef3(String newVdef3) {

		vdef3 = newVdef3;
	}

	/**
	 * ����vdef9��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getVdef9() {
		return vdef9;
	}

	/**
	 * ����vdef9��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newVdef9
	 *            String
	 */
	public void setVdef9(String newVdef9) {

		vdef9 = newVdef9;
	}

	/**
	 * ����pk_sendorder��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getPk_sendorder() {
		return pk_sendorder;
	}

	/**
	 * ����pk_sendorder��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newPk_sendorder
	 *            String
	 */
	public void setPk_sendorder(String newPk_sendorder) {

		pk_sendorder = newPk_sendorder;
	}

	/**
	 * ����vdef8��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getVdef8() {
		return vdef8;
	}

	/**
	 * ����vdef8��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newVdef8
	 *            String
	 */
	public void setVdef8(String newVdef8) {

		vdef8 = newVdef8;
	}

	/**
	 * ����reserve1��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getReserve1() {
		return reserve1;
	}

	/**
	 * ����reserve1��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newReserve1
	 *            String
	 */
	public void setReserve1(String newReserve1) {

		reserve1 = newReserve1;
	}

	/**
	 * ����noutkeepnum��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return UFDouble
	 */
	public UFDouble getNoutkeepnum() {
		return noutkeepnum;
	}

	/**
	 * ����noutkeepnum��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newNoutkeepnum
	 *            UFDouble
	 */
	public void setNoutkeepnum(UFDouble newNoutkeepnum) {

		noutkeepnum = newNoutkeepnum;
	}

	/**
	 * ����reserve16��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return UFBoolean
	 */
	public UFBoolean getReserve16() {
		return reserve16;
	}

	/**
	 * ����reserve16��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newReserve16
	 *            UFBoolean
	 */
	public void setReserve16(UFBoolean newReserve16) {

		reserve16 = newReserve16;
	}

	/**
	 * ����reserve7��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return UFDouble
	 */
	public UFDouble getReserve7() {
		return reserve7;
	}

	/**
	 * ����reserve7��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newReserve7
	 *            UFDouble
	 */
	public void setReserve7(UFDouble newReserve7) {

		reserve7 = newReserve7;
	}

	/**
	 * ����reserve15��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return UFBoolean
	 */
	public UFBoolean getReserve15() {
		return reserve15;
	}

	/**
	 * ����reserve15��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newReserve15
	 *            UFBoolean
	 */
	public void setReserve15(UFBoolean newReserve15) {

		reserve15 = newReserve15;
	}

	/**
	 * ����pk_invmandoc��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getPk_invmandoc() {
		return pk_invmandoc;
	}

	/**
	 * ����pk_invmandoc��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newPk_invmandoc
	 *            String
	 */
	public void setPk_invmandoc(String newPk_invmandoc) {

		pk_invmandoc = newPk_invmandoc;
	}

	/**
	 * ����vdef6��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getVdef6() {
		return vdef6;
	}

	/**
	 * ����vdef6��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newVdef6
	 *            String
	 */
	public void setVdef6(String newVdef6) {

		vdef6 = newVdef6;
	}

	/**
	 * ����reserve2��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getReserve2() {
		return reserve2;
	}

	/**
	 * ����reserve2��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newReserve2
	 *            String
	 */
	public void setReserve2(String newReserve2) {

		reserve2 = newReserve2;
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

	/**
	 * ����ninacceptnum��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return UFDouble
	 */
	public UFDouble getNinacceptnum() {
		return ninacceptnum;
	}

	/**
	 * ����ninacceptnum��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newNinacceptnum
	 *            UFDouble
	 */
	public void setNinacceptnum(UFDouble newNinacceptnum) {

		ninacceptnum = newNinacceptnum;
	}

	/**
	 * ����vdef10��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getVdef10() {
		return vdef10;
	}

	/**
	 * ����vdef10��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newVdef10
	 *            String
	 */
	public void setVdef10(String newVdef10) {

		vdef10 = newVdef10;
	}

	/**
	 * ����vdef5��Getter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getVdef5() {
		return vdef5;
	}

	/**
	 * ����vdef5��Setter����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newVdef5
	 *            String
	 */
	public void setVdef5(String newVdef5) {

		vdef5 = newVdef5;
	}

	/**
	 * ��֤���������֮��������߼���ȷ��.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @exception nc.vo.pub.ValidationException
	 *                �����֤ʧ��,�׳� ValidationException,�Դ�����н���.
	 */
	public void validate() throws ValidationException {

		ArrayList errFields = new ArrayList(); // errFields record those null

		// fields that cannot be null.
		// ����Ƿ�Ϊ�������յ��ֶθ��˿�ֵ,�������Ҫ�޸��������ʾ��Ϣ:

		if (pk_sendorder_b == null) {
			errFields.add(new String("pk_sendorder_b"));
		}

		StringBuffer message = new StringBuffer();
		message.append("�����ֶβ���Ϊ��:");
		if (errFields.size() > 0) {
			String[] temp = (String[]) errFields.toArray(new String[0]);
			message.append(temp[0]);
			for (int i = 1; i < temp.length; i++) {
				message.append(",");
				message.append(temp[i]);
			}
			throw new NullFieldException(message.toString());
		}
	}

	/**
	 * <p>
	 * ȡ�ø�VO�����ֶ�.
	 * <p>
	 * ��������:2011-3-24
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {

		return "pk_sendorder";

	}

	/**
	 * <p>
	 * ȡ�ñ�����.
	 * <p>
	 * ��������:2011-3-24
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "pk_sendorder_b";
	}

	/**
	 * <p>
	 * ���ر�����.
	 * <p>
	 * ��������:2011-3-24
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "wds_sendorder_b";
	}

	/**
	 * ����Ĭ�Ϸ�ʽ����������.
	 * 
	 * ��������:2011-3-24
	 */
	public SendorderBVO() {

		super();
	}

	/**
	 * ʹ���������г�ʼ���Ĺ�����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newPk_sendorder_b
	 *            ����ֵ
	 */
	public SendorderBVO(String newPk_sendorder_b) {

		// Ϊ�����ֶθ�ֵ:
		pk_sendorder_b = newPk_sendorder_b;

	}

	/**
	 * ���ض����ʶ,����Ψһ��λ����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return String
	 */
	public String getPrimaryKey() {

		return pk_sendorder_b;

	}

	/**
	 * ���ö����ʶ,����Ψһ��λ����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @param newPk_sendorder_b
	 *            String
	 */
	public void setPrimaryKey(String newPk_sendorder_b) {

		pk_sendorder_b = newPk_sendorder_b;

	}

	/**
	 * ������ֵ�������ʾ����.
	 * 
	 * ��������:2011-3-24
	 * 
	 * @return java.lang.String ������ֵ�������ʾ����.
	 */
	public String getEntityName() {

		return "wds_sendorder_b";

	}

	public UFDouble getNdealnum() {
		return ndealnum;
	}

	public void setNdealnum(UFDouble ndealnum) {
		this.ndealnum = ndealnum;
	}

	public UFDouble getNassdealnum() {
		return nassdealnum;
	}

	public void setNassdealnum(UFDouble nassdealnum) {
		this.nassdealnum = nassdealnum;
	}

	public UFDouble getNoutnum() {
		return noutnum;
	}

	public void setNoutnum(UFDouble noutnum) {
		this.noutnum = noutnum;
	}

	public UFDouble getNassoutnum() {
		return nassoutnum;
	}

	public void setNassoutnum(UFDouble nassoutnum) {
		this.nassoutnum = nassoutnum;
	}

	public UFDouble getNhsl() {
		return nhsl;
	}

	public void setNhsl(UFDouble nhsl) {
		this.nhsl = nhsl;
	}
	
}
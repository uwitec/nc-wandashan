package nc.vo.wds.transfer;

import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import nc.vo.scm.pu.PuPubVO;

/**
 * ת��λ
 * @author yf
 */
public class TransferVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8299919978628715989L;
	/**ҵ������ */
	public String cbiztype;
	public String pk_corp;//��˾
	/**���ݺ� */
	public String vbillcode;
	
	/**�������� */
	public String vbilltype;
	/**�������� */
	public UFDate dbilldate;
	/**����״̬ */
	public Integer vbillstatus;
	
	
	/**����ֿ� */
	public String srl_pk;
	/**���ֿ� */
	public String srl_pkr;
	/**���Ա */
	public String cwhsmanagerid;
	   /**���� */
	public String cdptid;
	/**�շ���� */
	public String cdispatcherid;
	/** �ջ���ַ */
	public String vdiliveraddress;
	
	/**��λ����*/
	public String pk_cargdoc;
	public String pk_fcorp;// �ֳ���˾����
	public String pk_fcalbody;// �ֳ������֯����
	public String pk_fstordoc;// �ֳ��ֿ�����
	public String mileage;// �������
	public String unitprice;// �˷ѵ���
	public String freight;// �˷�
	public String vsourcebillcode; // ��Դ������
	public String csourcebillhid; // ��Դ���ݱ�ͷ
	/**�Ƿ��д������--����ѡ��д�������õ����� */
	public UFBoolean is_yundan; 
	public String creceiptcustomerid;//�ջ���λ
	
	public UFBoolean fisload;//�Ƿ�װж�Ѽ������
	public UFBoolean fistran;//�Ƿ��˷Ѽ������
	public UFBoolean 	iscaltrans;//�Ƿ�����˷�
    public UFDate cshengchanriqi;//��������
    public UFDate cshixiaoriqi;//ʧЧ����
	
	public String vuserdef9;
	public String tmaketime;
	public String ccustomerid;
	public String vuserdef3;
	public String cauditorid;

	public String coperatorid;
	public Integer iprintcount;
	public String vuserdef14;
	
	public String pk_defdoc10;
	public String pk_defdoc6;

	public String pk_defdoc3;
	public String general_pk;
	
	public String vuserdef10;
	public String pk_defdoc2;

	public UFDate qianzidate;
	public String pk_defdoc7;
	public Integer state;
	public String pk_cubasdocc;

	public String vuserdef12;
	public UFDate dauditdate;
	public String vuserdef11;

	public String vuserdef15;//-------zhf ʹ��  �Ƿ���� ����   ת�ֲ�ʱ  ������������������С�ڳ������� �Զ����ɲ�ֵ������ⵥ  ���Զ�����
 
	public String cbizid;
	public String tlastmoditime;
	public String pk_defdoc9;
	public String vuserdef1;
	public String clastmodiid;
	public String pk_calbody;
	public String pk_defdoc1;//�������ⵥ���ջ�λ����������ŵ����λ
	
	public String pk_defdoc4;
	public String pk_defdoc5;
	public String vuserdef2;
	public String vuserdef5;
	public String vnote;//--��ע ���ղɹ�ȡ��ʱ����ȡ����λ  �� ȡ���� ƴ��һ���ַ���  �ŵ���ע����
	
	public String vuserdef8;
	public String vuserdef4;
	public String cregister;
	public String pk_defdoc8;
	public String vuserdef7;
	public String pk_calbodyr;
	public String taccounttime;
	public String vuserdef13;
	public String vuserdef6;
	public UFTime ts;
	public Integer dr;
	public UFBoolean isxnap;//�Ƿ����ⰲ��--ת�ֲ�ʹ��
	
	/******************��λ������TransferVO**************/
	private String pk_cargdoc2;//�����λ
	//��������
	public String pk_sendareal;
	// �Ƿ�����
	public UFBoolean fisbigflour;
	// ���ʱ��
	public UFDate denddate;
	// ����Ա
	public String pk_transer;
	// ���̻���id
	public String pk_cubasdoc;
	// ����վ
	public String pk_outwhouse;
	// ���䷽ʽ
	public Integer itranstype;
	// װ��ʱ��
	public UFDate dbegindate;
	// ҵ�����绰
	public String vyedbtel;
	// ���ι��ˣ�����
	public UFDouble ntotalnum;
	// ���Ա
	public String pk_manageperson;
	// ��ϵ��id
	public String pk_receiveperson;
	// �ջ�վ
	public String pk_inwhouse;
	// ҵ�����
	public String pk_yedb;
	// ���̹���id
	public String pk_cumandoc;
	//������ϵ�˵绰
	public String vtelphone;
	//�Ƿ񶳽�
	public UFBoolean fisended;
	//������:
	public String pk_transcorp;
	//���ţ�
	public String vcardno;
	//˾��ǩ�֣�
	public String vdriver;
	//˾���绰�� 
	public String vdrivertel;
	//���ܼ�����
	public UFDouble nacceptnum;
	//��������
	public UFDouble ngls;
	//�˼ۣ�
	public UFDouble ntranprice;
	//�˷ѵ���ֵ(Ԫ):
	public UFDouble nadjustprice;
	//�˷ѵ��� ��λ:
	public Integer iadjusttype;
	//�˷ѣ�Ԫ����
	public UFDouble ntransmny;
	//�ջ����ڣ�
	public UFDate dacceptdate;
	//������ǩ�֣�
	public String vcolpersonid;
	//�ջ���
	public String custareaid;
	// ------------------------------------------------------
	public String csalecorpid;// ������֯
	public String ccalbodyid;// �����֯
//	public String creceiptcustomerid;// �ջ���λ
	public String vinaddress;// �ջ���ַ

	// Ԥ���ֶ�
	public String reserve1;
	public String reserve2;
	public String reserve3;
	public String reserve4;
	public String reserve5;
	public String reserve6;
	public String reserve7;
	public UFDouble reserve8;
	public UFDouble reserve9;
	public UFDouble reserve10;
	public UFDate reserve11;
	public UFDate reserve12;
	public UFDate reserve13;
	public UFBoolean reserve14;
	public UFBoolean reserve15;
	public UFBoolean reserve16;
	// �Զ�����
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
//	public Integer dr;
	// ��ע
	public String vmemo;

	// ��˾
//	public String pk_corp;
	// ���ݺ�
//	public String vbillno;
	// ��������
//	public UFDate dbilldate;
	// ��������
	public String pk_billtype;
	// ����״̬
//	public Integer vbillstatus;
	// ҵ������
	public String pk_busitype;
	// ҵ��Աid
	public String vemployeeid;
	// ����id
	public String pk_deptdoc;
	// �Ƶ���
	public String voperatorid;
	// �Ƶ�����
	public UFDate dmakedate;
	// ������
	public String vapproveid;
	// ��������
	public UFDate dapprovedate;
	// ��������
	public String vapprovenote;
	//����״̬
	public Integer 	itransstatus;
	//��������
	public Integer icoltype;
	//----�˵�������Ϣ
	public UFDate ddispachdate;//�ɳ�����
	public Integer idispachtime;//�ɳ�ʱ��[0,23]������ֵ�����ɳ�Сʱ
	public UFDate dsenddate;//��������
	public Integer isenttime;//����ʱ��[0,23]������ֵ�����ɳ�Сʱ
	public UFDate dtransferdate;//ת��������
	public Integer itransfertime;//ת������ʱ��[0,23]������ֵ�����ɳ�Сʱ
	public UFDate dreceiptdate;//��ִ����
	public Integer ireceipttime;//��ִʱ��[0,23]������ֵ�����ɳ�Сʱ
	public UFDate darrivedate;//��������
	public Integer iarrivetime;//����ʱ��[0,23]������ֵ�����ɳ�Сʱ
	public UFDouble nruntime;//��������ʱ��
	public UFBoolean fissubstitute;//�Ƿ�ת������
	public String vfirstsendcorp;//��ʼ������
	public UFBoolean fisnormal;//�Ƿ��������
	public String vunnormalreason;//���������ԭ��
	public UFBoolean fiscomplain;//�Ƿ�Ͷ��
	public String vcomplainreason;//Ͷ��ԭ��
	public String vlatearrivereason;//����ԭ��
	public String vlatestartreason;//����ԭ��
	public Integer iquality;//��������
	public UFDouble ndestorynum;//������
	// ����ʱ�䣺
	public UFDateTime vapprovetime;
	//----�˵�������Ϣ
	public static final String ICOLTYPE ="icoltype";
	
	
	public String getCbiztype() {
		return cbiztype;
	}

	public void setCbiztype(String cbiztype) {
		this.cbiztype = cbiztype;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getVbillcode() {
		return vbillcode;
	}

	public void setVbillcode(String vbillcode) {
		this.vbillcode = vbillcode;
	}

	public String getVbilltype() {
		return vbilltype;
	}

	public void setVbilltype(String vbilltype) {
		this.vbilltype = vbilltype;
	}

	public UFDate getDbilldate() {
		return dbilldate;
	}

	public void setDbilldate(UFDate dbilldate) {
		this.dbilldate = dbilldate;
	}

	public Integer getVbillstatus() {
		return vbillstatus;
	}

	public void setVbillstatus(Integer vbillstatus) {
		this.vbillstatus = vbillstatus;
	}

	public String getSrl_pk() {
		return srl_pk;
	}

	public void setSrl_pk(String srl_pk) {
		this.srl_pk = srl_pk;
	}

	public String getSrl_pkr() {
		return srl_pkr;
	}

	public void setSrl_pkr(String srl_pkr) {
		this.srl_pkr = srl_pkr;
	}

	public String getCwhsmanagerid() {
		return cwhsmanagerid;
	}

	public void setCwhsmanagerid(String cwhsmanagerid) {
		this.cwhsmanagerid = cwhsmanagerid;
	}

	public String getCdptid() {
		return cdptid;
	}

	public void setCdptid(String cdptid) {
		this.cdptid = cdptid;
	}

	public String getCdispatcherid() {
		return cdispatcherid;
	}

	public void setCdispatcherid(String cdispatcherid) {
		this.cdispatcherid = cdispatcherid;
	}

	public String getVdiliveraddress() {
		return vdiliveraddress;
	}

	public void setVdiliveraddress(String vdiliveraddress) {
		this.vdiliveraddress = vdiliveraddress;
	}

	public String getPk_cargdoc() {
		return pk_cargdoc;
	}

	public void setPk_cargdoc(String pk_cargdoc) {
		this.pk_cargdoc = pk_cargdoc;
	}

	public String getPk_fcorp() {
		return pk_fcorp;
	}

	public void setPk_fcorp(String pk_fcorp) {
		this.pk_fcorp = pk_fcorp;
	}

	public String getPk_fcalbody() {
		return pk_fcalbody;
	}

	public void setPk_fcalbody(String pk_fcalbody) {
		this.pk_fcalbody = pk_fcalbody;
	}

	public String getPk_fstordoc() {
		return pk_fstordoc;
	}

	public void setPk_fstordoc(String pk_fstordoc) {
		this.pk_fstordoc = pk_fstordoc;
	}

	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}

	public String getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(String unitprice) {
		this.unitprice = unitprice;
	}

	public String getFreight() {
		return freight;
	}

	public void setFreight(String freight) {
		this.freight = freight;
	}

	public String getVsourcebillcode() {
		return vsourcebillcode;
	}

	public void setVsourcebillcode(String vsourcebillcode) {
		this.vsourcebillcode = vsourcebillcode;
	}

	public String getCsourcebillhid() {
		return csourcebillhid;
	}

	public void setCsourcebillhid(String csourcebillhid) {
		this.csourcebillhid = csourcebillhid;
	}

	public UFBoolean getIs_yundan() {
		return is_yundan;
	}

	public void setIs_yundan(UFBoolean is_yundan) {
		this.is_yundan = is_yundan;
	}

	public String getCreceiptcustomerid() {
		return creceiptcustomerid;
	}

	public void setCreceiptcustomerid(String creceiptcustomerid) {
		this.creceiptcustomerid = creceiptcustomerid;
	}

	public UFBoolean getFisload() {
		return fisload;
	}

	public void setFisload(UFBoolean fisload) {
		this.fisload = fisload;
	}

	public UFBoolean getFistran() {
		return fistran;
	}

	public void setFistran(UFBoolean fistran) {
		this.fistran = fistran;
	}

	public UFBoolean getIscaltrans() {
		return iscaltrans;
	}

	public void setIscaltrans(UFBoolean iscaltrans) {
		this.iscaltrans = iscaltrans;
	}

	public UFDate getCshengchanriqi() {
		return cshengchanriqi;
	}

	public void setCshengchanriqi(UFDate cshengchanriqi) {
		this.cshengchanriqi = cshengchanriqi;
	}

	public UFDate getCshixiaoriqi() {
		return cshixiaoriqi;
	}

	public void setCshixiaoriqi(UFDate cshixiaoriqi) {
		this.cshixiaoriqi = cshixiaoriqi;
	}

	public String getVuserdef9() {
		return vuserdef9;
	}

	public void setVuserdef9(String vuserdef9) {
		this.vuserdef9 = vuserdef9;
	}

	public String getTmaketime() {
		return tmaketime;
	}

	public void setTmaketime(String tmaketime) {
		this.tmaketime = tmaketime;
	}

	public String getCcustomerid() {
		return ccustomerid;
	}

	public void setCcustomerid(String ccustomerid) {
		this.ccustomerid = ccustomerid;
	}

	public String getVuserdef3() {
		return vuserdef3;
	}

	public void setVuserdef3(String vuserdef3) {
		this.vuserdef3 = vuserdef3;
	}

	public String getCauditorid() {
		return cauditorid;
	}

	public void setCauditorid(String cauditorid) {
		this.cauditorid = cauditorid;
	}

	public String getCoperatorid() {
		return coperatorid;
	}

	public void setCoperatorid(String coperatorid) {
		this.coperatorid = coperatorid;
	}

	public Integer getIprintcount() {
		return iprintcount;
	}

	public void setIprintcount(Integer iprintcount) {
		this.iprintcount = iprintcount;
	}

	public String getVuserdef14() {
		return vuserdef14;
	}

	public void setVuserdef14(String vuserdef14) {
		this.vuserdef14 = vuserdef14;
	}

	public String getPk_defdoc10() {
		return pk_defdoc10;
	}

	public void setPk_defdoc10(String pk_defdoc10) {
		this.pk_defdoc10 = pk_defdoc10;
	}

	public String getPk_defdoc6() {
		return pk_defdoc6;
	}

	public void setPk_defdoc6(String pk_defdoc6) {
		this.pk_defdoc6 = pk_defdoc6;
	}

	public String getPk_defdoc3() {
		return pk_defdoc3;
	}

	public void setPk_defdoc3(String pk_defdoc3) {
		this.pk_defdoc3 = pk_defdoc3;
	}

	public String getGeneral_pk() {
		return general_pk;
	}

	public void setGeneral_pk(String general_pk) {
		this.general_pk = general_pk;
	}

	public String getVuserdef10() {
		return vuserdef10;
	}

	public void setVuserdef10(String vuserdef10) {
		this.vuserdef10 = vuserdef10;
	}

	public String getPk_defdoc2() {
		return pk_defdoc2;
	}

	public void setPk_defdoc2(String pk_defdoc2) {
		this.pk_defdoc2 = pk_defdoc2;
	}

	public UFDate getQianzidate() {
		return qianzidate;
	}

	public void setQianzidate(UFDate qianzidate) {
		this.qianzidate = qianzidate;
	}

	public String getPk_defdoc7() {
		return pk_defdoc7;
	}

	public void setPk_defdoc7(String pk_defdoc7) {
		this.pk_defdoc7 = pk_defdoc7;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getPk_cubasdocc() {
		return pk_cubasdocc;
	}

	public void setPk_cubasdocc(String pk_cubasdocc) {
		this.pk_cubasdocc = pk_cubasdocc;
	}

	public String getVuserdef12() {
		return vuserdef12;
	}

	public void setVuserdef12(String vuserdef12) {
		this.vuserdef12 = vuserdef12;
	}

	public UFDate getDauditdate() {
		return dauditdate;
	}

	public void setDauditdate(UFDate dauditdate) {
		this.dauditdate = dauditdate;
	}

	public String getVuserdef11() {
		return vuserdef11;
	}

	public void setVuserdef11(String vuserdef11) {
		this.vuserdef11 = vuserdef11;
	}

	public String getVuserdef15() {
		return vuserdef15;
	}

	public void setVuserdef15(String vuserdef15) {
		this.vuserdef15 = vuserdef15;
	}

	public String getCbizid() {
		return cbizid;
	}

	public void setCbizid(String cbizid) {
		this.cbizid = cbizid;
	}

	public String getTlastmoditime() {
		return tlastmoditime;
	}

	public void setTlastmoditime(String tlastmoditime) {
		this.tlastmoditime = tlastmoditime;
	}

	public String getPk_defdoc9() {
		return pk_defdoc9;
	}

	public void setPk_defdoc9(String pk_defdoc9) {
		this.pk_defdoc9 = pk_defdoc9;
	}

	public String getVuserdef1() {
		return vuserdef1;
	}

	public void setVuserdef1(String vuserdef1) {
		this.vuserdef1 = vuserdef1;
	}

	public String getClastmodiid() {
		return clastmodiid;
	}

	public void setClastmodiid(String clastmodiid) {
		this.clastmodiid = clastmodiid;
	}

	public String getPk_calbody() {
		return pk_calbody;
	}

	public void setPk_calbody(String pk_calbody) {
		this.pk_calbody = pk_calbody;
	}

	public String getPk_defdoc1() {
		return pk_defdoc1;
	}

	public void setPk_defdoc1(String pk_defdoc1) {
		this.pk_defdoc1 = pk_defdoc1;
	}

	public String getPk_defdoc4() {
		return pk_defdoc4;
	}

	public void setPk_defdoc4(String pk_defdoc4) {
		this.pk_defdoc4 = pk_defdoc4;
	}

	public String getPk_defdoc5() {
		return pk_defdoc5;
	}

	public void setPk_defdoc5(String pk_defdoc5) {
		this.pk_defdoc5 = pk_defdoc5;
	}

	public String getVuserdef2() {
		return vuserdef2;
	}

	public void setVuserdef2(String vuserdef2) {
		this.vuserdef2 = vuserdef2;
	}

	public String getVuserdef5() {
		return vuserdef5;
	}

	public void setVuserdef5(String vuserdef5) {
		this.vuserdef5 = vuserdef5;
	}

	public String getVnote() {
		return vnote;
	}

	public void setVnote(String vnote) {
		this.vnote = vnote;
	}

	public String getVuserdef8() {
		return vuserdef8;
	}

	public void setVuserdef8(String vuserdef8) {
		this.vuserdef8 = vuserdef8;
	}

	public String getVuserdef4() {
		return vuserdef4;
	}

	public void setVuserdef4(String vuserdef4) {
		this.vuserdef4 = vuserdef4;
	}

	public String getCregister() {
		return cregister;
	}

	public void setCregister(String cregister) {
		this.cregister = cregister;
	}

	public String getPk_defdoc8() {
		return pk_defdoc8;
	}

	public void setPk_defdoc8(String pk_defdoc8) {
		this.pk_defdoc8 = pk_defdoc8;
	}

	public String getVuserdef7() {
		return vuserdef7;
	}

	public void setVuserdef7(String vuserdef7) {
		this.vuserdef7 = vuserdef7;
	}

	public String getPk_calbodyr() {
		return pk_calbodyr;
	}

	public void setPk_calbodyr(String pk_calbodyr) {
		this.pk_calbodyr = pk_calbodyr;
	}

	public String getTaccounttime() {
		return taccounttime;
	}

	public void setTaccounttime(String taccounttime) {
		this.taccounttime = taccounttime;
	}

	public String getVuserdef13() {
		return vuserdef13;
	}

	public void setVuserdef13(String vuserdef13) {
		this.vuserdef13 = vuserdef13;
	}

	public String getVuserdef6() {
		return vuserdef6;
	}

	public void setVuserdef6(String vuserdef6) {
		this.vuserdef6 = vuserdef6;
	}

	public UFTime getTs() {
		return ts;
	}

	public void setTs(UFTime ts) {
		this.ts = ts;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public UFBoolean getIsxnap() {
		return isxnap;
	}

	public void setIsxnap(UFBoolean isxnap) {
		this.isxnap = isxnap;
	}

	public String getPk_cargdoc2() {
		return pk_cargdoc2;
	}

	public void setPk_cargdoc2(String pk_cargdoc2) {
		this.pk_cargdoc2 = pk_cargdoc2;
	}

	public String getPk_sendareal() {
		return pk_sendareal;
	}

	public void setPk_sendareal(String pk_sendareal) {
		this.pk_sendareal = pk_sendareal;
	}

	public UFBoolean getFisbigflour() {
		return fisbigflour;
	}

	public void setFisbigflour(UFBoolean fisbigflour) {
		this.fisbigflour = fisbigflour;
	}

	public UFDate getDenddate() {
		return denddate;
	}

	public void setDenddate(UFDate denddate) {
		this.denddate = denddate;
	}

	public String getPk_transer() {
		return pk_transer;
	}

	public void setPk_transer(String pk_transer) {
		this.pk_transer = pk_transer;
	}

	public String getPk_cubasdoc() {
		return pk_cubasdoc;
	}

	public void setPk_cubasdoc(String pk_cubasdoc) {
		this.pk_cubasdoc = pk_cubasdoc;
	}

	public String getPk_outwhouse() {
		return pk_outwhouse;
	}

	public void setPk_outwhouse(String pk_outwhouse) {
		this.pk_outwhouse = pk_outwhouse;
	}

	public Integer getItranstype() {
		return itranstype;
	}

	public void setItranstype(Integer itranstype) {
		this.itranstype = itranstype;
	}

	public UFDate getDbegindate() {
		return dbegindate;
	}

	public void setDbegindate(UFDate dbegindate) {
		this.dbegindate = dbegindate;
	}

	public String getVyedbtel() {
		return vyedbtel;
	}

	public void setVyedbtel(String vyedbtel) {
		this.vyedbtel = vyedbtel;
	}

	public UFDouble getNtotalnum() {
		return ntotalnum;
	}

	public void setNtotalnum(UFDouble ntotalnum) {
		this.ntotalnum = ntotalnum;
	}

	public String getPk_manageperson() {
		return pk_manageperson;
	}

	public void setPk_manageperson(String pk_manageperson) {
		this.pk_manageperson = pk_manageperson;
	}

	public String getPk_receiveperson() {
		return pk_receiveperson;
	}

	public void setPk_receiveperson(String pk_receiveperson) {
		this.pk_receiveperson = pk_receiveperson;
	}

	public String getPk_inwhouse() {
		return pk_inwhouse;
	}

	public void setPk_inwhouse(String pk_inwhouse) {
		this.pk_inwhouse = pk_inwhouse;
	}

	public String getPk_yedb() {
		return pk_yedb;
	}

	public void setPk_yedb(String pk_yedb) {
		this.pk_yedb = pk_yedb;
	}

	public String getPk_cumandoc() {
		return pk_cumandoc;
	}

	public void setPk_cumandoc(String pk_cumandoc) {
		this.pk_cumandoc = pk_cumandoc;
	}

	public String getVtelphone() {
		return vtelphone;
	}

	public void setVtelphone(String vtelphone) {
		this.vtelphone = vtelphone;
	}

	public UFBoolean getFisended() {
		return fisended;
	}

	public void setFisended(UFBoolean fisended) {
		this.fisended = fisended;
	}

	public String getPk_transcorp() {
		return pk_transcorp;
	}

	public void setPk_transcorp(String pk_transcorp) {
		this.pk_transcorp = pk_transcorp;
	}

	public String getVcardno() {
		return vcardno;
	}

	public void setVcardno(String vcardno) {
		this.vcardno = vcardno;
	}

	public String getVdriver() {
		return vdriver;
	}

	public void setVdriver(String vdriver) {
		this.vdriver = vdriver;
	}

	public String getVdrivertel() {
		return vdrivertel;
	}

	public void setVdrivertel(String vdrivertel) {
		this.vdrivertel = vdrivertel;
	}

	public UFDouble getNacceptnum() {
		return nacceptnum;
	}

	public void setNacceptnum(UFDouble nacceptnum) {
		this.nacceptnum = nacceptnum;
	}

	public UFDouble getNgls() {
		return ngls;
	}

	public void setNgls(UFDouble ngls) {
		this.ngls = ngls;
	}

	public UFDouble getNtranprice() {
		return ntranprice;
	}

	public void setNtranprice(UFDouble ntranprice) {
		this.ntranprice = ntranprice;
	}

	public UFDouble getNadjustprice() {
		return nadjustprice;
	}

	public void setNadjustprice(UFDouble nadjustprice) {
		this.nadjustprice = nadjustprice;
	}

	public Integer getIadjusttype() {
		return iadjusttype;
	}

	public void setIadjusttype(Integer iadjusttype) {
		this.iadjusttype = iadjusttype;
	}

	public UFDouble getNtransmny() {
		return ntransmny;
	}

	public void setNtransmny(UFDouble ntransmny) {
		this.ntransmny = ntransmny;
	}

	public UFDate getDacceptdate() {
		return dacceptdate;
	}

	public void setDacceptdate(UFDate dacceptdate) {
		this.dacceptdate = dacceptdate;
	}

	public String getVcolpersonid() {
		return vcolpersonid;
	}

	public void setVcolpersonid(String vcolpersonid) {
		this.vcolpersonid = vcolpersonid;
	}

	public String getCustareaid() {
		return custareaid;
	}

	public void setCustareaid(String custareaid) {
		this.custareaid = custareaid;
	}

	public String getCsalecorpid() {
		return csalecorpid;
	}

	public void setCsalecorpid(String csalecorpid) {
		this.csalecorpid = csalecorpid;
	}

	public String getCcalbodyid() {
		return ccalbodyid;
	}

	public void setCcalbodyid(String ccalbodyid) {
		this.ccalbodyid = ccalbodyid;
	}

	public String getVinaddress() {
		return vinaddress;
	}

	public void setVinaddress(String vinaddress) {
		this.vinaddress = vinaddress;
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

	public UFDouble getReserve8() {
		return reserve8;
	}

	public void setReserve8(UFDouble reserve8) {
		this.reserve8 = reserve8;
	}

	public UFDouble getReserve9() {
		return reserve9;
	}

	public void setReserve9(UFDouble reserve9) {
		this.reserve9 = reserve9;
	}

	public UFDouble getReserve10() {
		return reserve10;
	}

	public void setReserve10(UFDouble reserve10) {
		this.reserve10 = reserve10;
	}

	public UFDate getReserve11() {
		return reserve11;
	}

	public void setReserve11(UFDate reserve11) {
		this.reserve11 = reserve11;
	}

	public UFDate getReserve12() {
		return reserve12;
	}

	public void setReserve12(UFDate reserve12) {
		this.reserve12 = reserve12;
	}

	public UFDate getReserve13() {
		return reserve13;
	}

	public void setReserve13(UFDate reserve13) {
		this.reserve13 = reserve13;
	}

	public UFBoolean getReserve14() {
		return reserve14;
	}

	public void setReserve14(UFBoolean reserve14) {
		this.reserve14 = reserve14;
	}

	public UFBoolean getReserve15() {
		return reserve15;
	}

	public void setReserve15(UFBoolean reserve15) {
		this.reserve15 = reserve15;
	}

	public UFBoolean getReserve16() {
		return reserve16;
	}

	public void setReserve16(UFBoolean reserve16) {
		this.reserve16 = reserve16;
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

	public String getVmemo() {
		return vmemo;
	}

	public void setVmemo(String vmemo) {
		this.vmemo = vmemo;
	}

	public String getPk_billtype() {
		return pk_billtype;
	}

	public void setPk_billtype(String pk_billtype) {
		this.pk_billtype = pk_billtype;
	}

	public String getPk_busitype() {
		return pk_busitype;
	}

	public void setPk_busitype(String pk_busitype) {
		this.pk_busitype = pk_busitype;
	}

	public String getVemployeeid() {
		return vemployeeid;
	}

	public void setVemployeeid(String vemployeeid) {
		this.vemployeeid = vemployeeid;
	}

	public String getPk_deptdoc() {
		return pk_deptdoc;
	}

	public void setPk_deptdoc(String pk_deptdoc) {
		this.pk_deptdoc = pk_deptdoc;
	}

	public String getVoperatorid() {
		return voperatorid;
	}

	public void setVoperatorid(String voperatorid) {
		this.voperatorid = voperatorid;
	}

	public UFDate getDmakedate() {
		return dmakedate;
	}

	public void setDmakedate(UFDate dmakedate) {
		this.dmakedate = dmakedate;
	}

	public String getVapproveid() {
		return vapproveid;
	}

	public void setVapproveid(String vapproveid) {
		this.vapproveid = vapproveid;
	}

	public UFDate getDapprovedate() {
		return dapprovedate;
	}

	public void setDapprovedate(UFDate dapprovedate) {
		this.dapprovedate = dapprovedate;
	}

	public String getVapprovenote() {
		return vapprovenote;
	}

	public void setVapprovenote(String vapprovenote) {
		this.vapprovenote = vapprovenote;
	}

	public Integer getItransstatus() {
		return itransstatus;
	}

	public void setItransstatus(Integer itransstatus) {
		this.itransstatus = itransstatus;
	}

	public Integer getIcoltype() {
		return icoltype;
	}

	public void setIcoltype(Integer icoltype) {
		this.icoltype = icoltype;
	}

	public UFDate getDdispachdate() {
		return ddispachdate;
	}

	public void setDdispachdate(UFDate ddispachdate) {
		this.ddispachdate = ddispachdate;
	}

	public Integer getIdispachtime() {
		return idispachtime;
	}

	public void setIdispachtime(Integer idispachtime) {
		this.idispachtime = idispachtime;
	}

	public UFDate getDsenddate() {
		return dsenddate;
	}

	public void setDsenddate(UFDate dsenddate) {
		this.dsenddate = dsenddate;
	}

	public Integer getIsenttime() {
		return isenttime;
	}

	public void setIsenttime(Integer isenttime) {
		this.isenttime = isenttime;
	}

	public UFDate getDtransferdate() {
		return dtransferdate;
	}

	public void setDtransferdate(UFDate dtransferdate) {
		this.dtransferdate = dtransferdate;
	}

	public Integer getItransfertime() {
		return itransfertime;
	}

	public void setItransfertime(Integer itransfertime) {
		this.itransfertime = itransfertime;
	}

	public UFDate getDreceiptdate() {
		return dreceiptdate;
	}

	public void setDreceiptdate(UFDate dreceiptdate) {
		this.dreceiptdate = dreceiptdate;
	}

	public Integer getIreceipttime() {
		return ireceipttime;
	}

	public void setIreceipttime(Integer ireceipttime) {
		this.ireceipttime = ireceipttime;
	}

	public UFDate getDarrivedate() {
		return darrivedate;
	}

	public void setDarrivedate(UFDate darrivedate) {
		this.darrivedate = darrivedate;
	}

	public Integer getIarrivetime() {
		return iarrivetime;
	}

	public void setIarrivetime(Integer iarrivetime) {
		this.iarrivetime = iarrivetime;
	}

	public UFDouble getNruntime() {
		return nruntime;
	}

	public void setNruntime(UFDouble nruntime) {
		this.nruntime = nruntime;
	}

	public UFBoolean getFissubstitute() {
		return fissubstitute;
	}

	public void setFissubstitute(UFBoolean fissubstitute) {
		this.fissubstitute = fissubstitute;
	}

	public String getVfirstsendcorp() {
		return vfirstsendcorp;
	}

	public void setVfirstsendcorp(String vfirstsendcorp) {
		this.vfirstsendcorp = vfirstsendcorp;
	}

	public UFBoolean getFisnormal() {
		return fisnormal;
	}

	public void setFisnormal(UFBoolean fisnormal) {
		this.fisnormal = fisnormal;
	}

	public String getVunnormalreason() {
		return vunnormalreason;
	}

	public void setVunnormalreason(String vunnormalreason) {
		this.vunnormalreason = vunnormalreason;
	}

	public UFBoolean getFiscomplain() {
		return fiscomplain;
	}

	public void setFiscomplain(UFBoolean fiscomplain) {
		this.fiscomplain = fiscomplain;
	}

	public String getVcomplainreason() {
		return vcomplainreason;
	}

	public void setVcomplainreason(String vcomplainreason) {
		this.vcomplainreason = vcomplainreason;
	}

	public String getVlatearrivereason() {
		return vlatearrivereason;
	}

	public void setVlatearrivereason(String vlatearrivereason) {
		this.vlatearrivereason = vlatearrivereason;
	}

	public String getVlatestartreason() {
		return vlatestartreason;
	}

	public void setVlatestartreason(String vlatestartreason) {
		this.vlatestartreason = vlatestartreason;
	}

	public Integer getIquality() {
		return iquality;
	}

	public void setIquality(Integer iquality) {
		this.iquality = iquality;
	}

	public UFDouble getNdestorynum() {
		return ndestorynum;
	}

	public void setNdestorynum(UFDouble ndestorynum) {
		this.ndestorynum = ndestorynum;
	}

	public UFDateTime getVapprovetime() {
		return vapprovetime;
	}

	public void setVapprovetime(UFDateTime vapprovetime) {
		this.vapprovetime = vapprovetime;
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

		return "wds_transfer";
	}

	/**
	 * ����Ĭ�Ϸ�ʽ����������.
	 * 
	 * ��������:2011-3-24
	 */
	public TransferVO() {

		super();
	}

	@Override
	public String getPKFieldName() {
		return "general_pk";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void validate() throws ValidationException {
//		if(PuPubVO.getString_TrimZeroLenAsNull(pk_cargdoc)==null)
//			throw new ValidationException("������λΪ��");
//		if(PuPubVO.getString_TrimZeroLenAsNull(pk_cargdoc2)==null)
//			throw new ValidationException("�����λΪ��");
//		if(pk_cargdoc == pk_cargdoc2)
//			throw new ValidationException("������λ�͵����λ������ͬ");
	}

}

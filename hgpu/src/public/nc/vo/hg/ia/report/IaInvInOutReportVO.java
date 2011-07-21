package nc.vo.hg.ia.report;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;


/**
 * 
 * @author zhf  �������������ˮ��ϸ�� vo
 *
 */
public class IaInvInOutReportVO extends SuperVO {

	private String  pk_corp;
	private String  crdcenterid;// �����֯��ʶ    
	private String  cinventoryid;//  �����ʶ  
	private String  vbatch;//���κ�
	private String  caccountyear;//������
	private String  caccountmonth;//�����
	private String  nmonthprice;//ƽ����

	private UFDouble ninnum ;//��������;                
	private UFDouble ninmny ;//������;                
	private UFDouble noutnum;//��������;                
	private UFDouble noutmny;//�������;                
	private UFDouble nabnum ;//�������; ---------------��ĩ����               
	private UFDouble nabprice ;//��浥��            
	private UFDouble nabmny ;//�����  -------------------��ĩ���
	
//	private UFDouble nplanedprice;//�ƻ�����  �ƻ����۵ı仯  �����۲�

	//	ia_generalledger  ���ı�ǩ: �������   �����ֶ�  ȡ�� �ϱ�


	private String cinvclid;//���һ������ID
	//	�ڳ�����
	private String cinvbasid;//���ID
	private UFDouble ninitnum;//�ڳ�����
	private UFDouble ninitmny;//�ڳ����
	private UFDouble ndeparturemny;//���ۼ۲���

	//	��������
	private UFDouble ninvoicenum;//��Ʊ����
	private UFDouble ninvoicemny;//��Ʊ���

	private UFDouble nestimatenum;//�ݹ�δ��������
	private UFDouble nestimatemny;//�ݹ�δ������

	private UFDouble nlastestnum;//��ǰ���ݹ�����
	private UFDouble nlastestmny;//

	private  UFDouble nproductnum;
	private UFDouble nproductmny;//����Ʒ�����

	//	���ڳ�
	private UFDouble nsaleoutnum;
	private UFDouble nsaleoutmny;

	private UFDouble nalloutnum;
	private UFDouble nalloutmny;
	private UFDouble nselfoutnum;//����
	private UFDouble nselfoutmny;


	private String vdef1;
	private String vdef2;
	private String vdef3;
	private String vdef4;
	private String vdef5;
	private String vdef6;

	private UFDouble ndef1;
	private UFDouble ndef2;
	private UFDouble ndef3;
	private UFDouble ndef4;
	private UFDouble ndef5;
	private UFDouble ndef6;


	private String cwarehouseid;//�ֿ�


	public static final String templet_data_ID = "0001A110000000019UIA";
	public static final String templet_data_ID2 = "0001A11000000001A59T";
	public static final String templet_query_ID = "0001A110000000019UKS";
	public static final String templet_modoulecode= "20149901";
	

	public static String[] generalledger_names = new String[]{
		"pk_corp"
		,"crdcenterid"// �����֯��ʶ    
		,"cinventoryid"//  �����ʶ  
//		,"vbatch"//���κ�
//		,"caccountyear"//������
//		,"caccountmonth"//�����
//		,"nmonthprice"//ƽ����

		,"ninnum "//��������"                
		,"ninmny "//������"                
		,"noutnum"//��������"                
		,"noutmny"//�������"                
		,"nabnum "//�������" ---------------��ĩ����               
//		,"nabprice "//��浥��            
		,"nabmny "//�����  -------------------��ĩ���};
//		,"nplanedprice"//�ƻ���
	};
	
//	public static String buildSql(String whereSql){
//		StringBuffer str = new StringBuffer("select 'aaa' ");
//		for(String name:generalledger_names){
//			str.append(","+name);
//		}
//		str.append(" from ia_generalledger ");
//		str.append(" isnull(dr,0) = 0 and coalesce(btryflag,'N') = 'N'");//Ĭ������
//		if(PuPubVO.getString_TrimZeroLenAsNull(whereSql)!=null)
//			str.append(" and "+whereSql);
//		str.append("group by pk_corp,crdcenterid,cinventoryid");
//		return str.toString();
//	}

	public String getCinvclid() {
		return cinvclid;
	}

	public void setCinvclid(String cinvclid) {
		this.cinvclid = cinvclid;
	}

//	public UFDouble getNplanedprice() {
//		return nplanedprice;
//	}
//
//	public void setNplanedprice(UFDouble nplanedprice) {
//		this.nplanedprice = nplanedprice;
//	}

	public String getCwarehouseid() {
		return cwarehouseid;
	}

	public void setCwarehouseid(String cwarehouseid) {
		this.cwarehouseid = cwarehouseid;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getCrdcenterid() {
		return crdcenterid;
	}

	public void setCrdcenterid(String crdcenterid) {
		this.crdcenterid = crdcenterid;
	}

	public String getCinventoryid() {
		return cinventoryid;
	}

	public void setCinventoryid(String cinventoryid) {
		this.cinventoryid = cinventoryid;
	}

	public String getVbatch() {
		return vbatch;
	}

	public void setVbatch(String vbatch) {
		this.vbatch = vbatch;
	}

	public String getCaccountyear() {
		return caccountyear;
	}

	public void setCaccountyear(String caccountyear) {
		this.caccountyear = caccountyear;
	}

	public String getCaccountmonth() {
		return caccountmonth;
	}

	public void setCaccountmonth(String caccountmonth) {
		this.caccountmonth = caccountmonth;
	}

	public String getNmonthprice() {
		return nmonthprice;
	}

	public void setNmonthprice(String nmonthprice) {
		this.nmonthprice = nmonthprice;
	}

	public UFDouble getNinnum() {
		return ninnum;
	}

	public void setNinnum(UFDouble ninnum) {
		this.ninnum = ninnum;
	}

	public UFDouble getNinmny() {
		return ninmny;
	}

	public void setNinmny(UFDouble ninmny) {
		this.ninmny = ninmny;
	}

	public UFDouble getNoutnum() {
		return noutnum;
	}

	public void setNoutnum(UFDouble noutnum) {
		this.noutnum = noutnum;
	}

	public UFDouble getNoutmny() {
		return noutmny;
	}

	public void setNoutmny(UFDouble noutmny) {
		this.noutmny = noutmny;
	}

	public UFDouble getNabnum() {
		return nabnum;
	}

	public void setNabnum(UFDouble nabnum) {
		this.nabnum = nabnum;
	}

	public UFDouble getNabprice() {
		return nabprice;
	}

	public void setNabprice(UFDouble nabprice) {
		this.nabprice = nabprice;
	}

	public UFDouble getNabmny() {
		return nabmny;
	}

	public void setNabmny(UFDouble nabmny) {
		this.nabmny = nabmny;
	}

	public String getCinvbasid() {
		return cinvbasid;
	}

	public void setCinvbasid(String cinvbasid) {
		this.cinvbasid = cinvbasid;
	}

	public UFDouble getNinitnum() {
		return ninitnum;
	}

	public void setNinitnum(UFDouble ninitnum) {
		this.ninitnum = ninitnum;
	}

	public UFDouble getNinitmny() {
		return ninitmny;
	}

	public void setNinitmny(UFDouble ninitmny) {
		this.ninitmny = ninitmny;
	}

	public UFDouble getNdeparturemny() {
		return ndeparturemny;
	}

	public void setNdeparturemny(UFDouble ndeparturemny) {
		this.ndeparturemny = ndeparturemny;
	}

	public UFDouble getNinvoicenum() {
		return ninvoicenum;
	}

	public void setNinvoicenum(UFDouble ninvoicenum) {
		this.ninvoicenum = ninvoicenum;
	}

	public UFDouble getNinvoicemny() {
		return ninvoicemny;
	}

	public void setNinvoicemny(UFDouble ninvoicemny) {
		this.ninvoicemny = ninvoicemny;
	}

	public UFDouble getNestimatenum() {
		return nestimatenum;
	}

	public void setNestimatenum(UFDouble nestimatenum) {
		this.nestimatenum = nestimatenum;
	}

	public UFDouble getNestimatemny() {
		return nestimatemny;
	}

	public void setNestimatemny(UFDouble nestimatemny) {
		this.nestimatemny = nestimatemny;
	}

	public UFDouble getNlastestnum() {
		return nlastestnum;
	}

	public void setNlastestnum(UFDouble nlastestnum) {
		this.nlastestnum = nlastestnum;
	}

	public UFDouble getNlastestmny() {
		return nlastestmny;
	}

	public void setNlastestmny(UFDouble nlastestmny) {
		this.nlastestmny = nlastestmny;
	}

	public UFDouble getNproductnum() {
		return nproductnum;
	}

	public void setNproductnum(UFDouble nproductnum) {
		this.nproductnum = nproductnum;
	}

	public UFDouble getNproductmny() {
		return nproductmny;
	}

	public void setNproductmny(UFDouble nproductmny) {
		this.nproductmny = nproductmny;
	}

	public UFDouble getNsaleoutnum() {
		return nsaleoutnum;
	}

	public void setNsaleoutnum(UFDouble nsaleoutnum) {
		this.nsaleoutnum = nsaleoutnum;
	}

	public UFDouble getNsaleoutmny() {
		return nsaleoutmny;
	}

	public void setNsaleoutmny(UFDouble nsaleoutmny) {
		this.nsaleoutmny = nsaleoutmny;
	}

	public UFDouble getNalloutnum() {
		return nalloutnum;
	}

	public void setNalloutnum(UFDouble nalloutnum) {
		this.nalloutnum = nalloutnum;
	}

	public UFDouble getNalloutmny() {
		return nalloutmny;
	}

	public void setNalloutmny(UFDouble nalloutmny) {
		this.nalloutmny = nalloutmny;
	}

	public UFDouble getNselfoutnum() {
		return nselfoutnum;
	}

	public void setNselfoutnum(UFDouble nselfoutnum) {
		this.nselfoutnum = nselfoutnum;
	}

	public UFDouble getNselfoutmny() {
		return nselfoutmny;
	}

	public void setNselfoutmny(UFDouble nselfoutmny) {
		this.nselfoutmny = nselfoutmny;
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

	public UFDouble getNdef1() {
		return ndef1;
	}

	public void setNdef1(UFDouble ndef1) {
		this.ndef1 = ndef1;
	}

	public UFDouble getNdef2() {
		return ndef2;
	}

	public void setNdef2(UFDouble ndef2) {
		this.ndef2 = ndef2;
	}

	public UFDouble getNdef3() {
		return ndef3;
	}

	public void setNdef3(UFDouble ndef3) {
		this.ndef3 = ndef3;
	}

	public UFDouble getNdef4() {
		return ndef4;
	}

	public void setNdef4(UFDouble ndef4) {
		this.ndef4 = ndef4;
	}

	public UFDouble getNdef5() {
		return ndef5;
	}

	public void setNdef5(UFDouble ndef5) {
		this.ndef5 = ndef5;
	}

	public UFDouble getNdef6() {
		return ndef6;
	}

	public void setNdef6(UFDouble ndef6) {
		this.ndef6 = ndef6;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

}

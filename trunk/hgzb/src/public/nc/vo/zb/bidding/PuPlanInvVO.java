package nc.vo.zb.bidding;

import java.util.HashSet;

import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

/**
 * 
 * @author zhf
 * @˵�������׸ڿ�ҵ�����ղɹ��ƻ����ֱ�� ר�õ� �ɹ��ƻ�vo
 * 2011-5-18����02:04:32
 */

public class PuPlanInvVO extends SuperVO {
//	�빺��ͷ�ֶ�
	 private String cpraybillid;//            �빺��id                
	 private String pk_corp;//             ��˾����                                                          
	 private String vpraycode;//        �빺�����                                                        
	 private UFDate dpraydate;//            �빺����                                                          
	 private String cdeptid;//            �빺����id                                                        
	 private String cpraypsn;//            �빺��id                                                          
	 private String coperator;//            �Ƶ���id                                                          
	 private Integer ibillstatus;//        ����״̬                                                          
	 private UFDate dauditdate;//            ��������                                                          
	 private String cauditpsn ;//            ������id                                                          
	 private String cplannerid;//            �ƻ�Ա                                                            
	 private String vmemo;//       ��ע                                                              
//	�빺�����ֶ�
	 
	 private String cpraybill_bid;//            �빺����id                                                                                                       
	 private String pk_reqcorp;//             ����˾                                                          
	 private String pk_reqstoorg;//            ��������֯                                                      
	 private String pk_purcorp;//             �ɹ���˾                                                          
	 private String cpurorganization;//            �ɹ���֯id                                                        
	 private String cmangid;//            �������id                                                        
	 private String cbaseid;//            �������id                                                        
	 private UFDouble npraynum;//��������                                                          
	 private String cassistunit;//            ��������λid                                                      
	 private UFDouble nassistnum;//����������                                                        
	 private UFDouble nsuggestprice;// ���鵥��                                                          
	 private String cvendormangid;//            ���鹩Ӧ�̹���id                                                  
	 private String cvendorbaseid;//            ���鹩Ӧ�̻���id                                                  
	 private UFDate ddemanddate;//��������                                                          
	 private UFDate dsuggestdate;//���鶩������                                                      
	 private String cwarehouseid ;//            ����ֿ�id                                                        
	 private UFDouble naccumulatenum ;//�ۼƶ�������                                                                                                           
	 private String csourcebilltype ;//          ��Դ��������                                                      
	 private String csourcebillid       ;//            ��Դ����id                                                        
	 private String csourcebillrowid    ;//            ��Դ������id                                                      
	 private String cupsourcebilltype             ;//          �ϲ㵥������                                                      
	 private String cupsourcebillid     ;//            �ϲ���Դ����id                                                    
	 private String cupsourcebillrowid  ;//            �ϲ���Դ������id                                                  
	 private String bodyvmemo;//��ע                                                                                                               
	 private String crowno;//�к�                                                              
	 private String cemployeeid         ;//            �ɹ�Ա                                                                                                                  
	 private Integer npriceauditbill;//���ɼ۸�����������                                      
	 private Integer nquotebill;//����ѯ���۵�����         
	 
	 
	 public static  void checkInvUnique(PuPlanInvVO[] invs) throws ValidationException{
		 if(invs == null || invs.length == 0)
			 return;
		 java.util.Set<String> ss = new HashSet<String>();
		 for(PuPlanInvVO inv:invs){
			 ss.add(inv.getCbaseid());
		 }
		 if(invs.length!=ss.size()){
			 throw new ValidationException("�����ظ�Ʒ��");
		 }
	 }
	 

	public String getCpraybillid() {
		return cpraybillid;
	}

	public void setCpraybillid(String cpraybillid) {
		this.cpraybillid = cpraybillid;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getVpraycode() {
		return vpraycode;
	}

	public void setVpraycode(String vpraycode) {
		this.vpraycode = vpraycode;
	}

	public UFDate getDpraydate() {
		return dpraydate;
	}

	public void setDpraydate(UFDate dpraydate) {
		this.dpraydate = dpraydate;
	}

	public String getCdeptid() {
		return cdeptid;
	}

	public void setCdeptid(String cdeptid) {
		this.cdeptid = cdeptid;
	}

	public String getCpraypsn() {
		return cpraypsn;
	}

	public void setCpraypsn(String cpraypsn) {
		this.cpraypsn = cpraypsn;
	}

	public String getCoperator() {
		return coperator;
	}

	public void setCoperator(String coperator) {
		this.coperator = coperator;
	}

	public Integer getIbillstatus() {
		return ibillstatus;
	}

	public void setIbillstatus(Integer ibillstatus) {
		this.ibillstatus = ibillstatus;
	}

	public UFDate getDauditdate() {
		return dauditdate;
	}

	public void setDauditdate(UFDate dauditdate) {
		this.dauditdate = dauditdate;
	}

	public String getCauditpsn() {
		return cauditpsn;
	}

	public void setCauditpsn(String cauditpsn) {
		this.cauditpsn = cauditpsn;
	}

	public String getCplannerid() {
		return cplannerid;
	}

	public void setCplannerid(String cplannerid) {
		this.cplannerid = cplannerid;
	}

	public String getVmemo() {
		return vmemo;
	}

	public void setVmemo(String vmemo) {
		this.vmemo = vmemo;
	}

	public String getCpraybill_bid() {
		return cpraybill_bid;
	}

	public void setCpraybill_bid(String cpraybill_bid) {
		this.cpraybill_bid = cpraybill_bid;
	}

	public String getPk_reqcorp() {
		return pk_reqcorp;
	}

	public void setPk_reqcorp(String pk_reqcorp) {
		this.pk_reqcorp = pk_reqcorp;
	}

	public String getPk_reqstoorg() {
		return pk_reqstoorg;
	}

	public void setPk_reqstoorg(String pk_reqstoorg) {
		this.pk_reqstoorg = pk_reqstoorg;
	}

	public String getPk_purcorp() {
		return pk_purcorp;
	}

	public void setPk_purcorp(String pk_purcorp) {
		this.pk_purcorp = pk_purcorp;
	}

	public String getCpurorganization() {
		return cpurorganization;
	}

	public void setCpurorganization(String cpurorganization) {
		this.cpurorganization = cpurorganization;
	}

	public String getCmangid() {
		return cmangid;
	}

	public void setCmangid(String cmangid) {
		this.cmangid = cmangid;
	}

	public String getCbaseid() {
		return cbaseid;
	}

	public void setCbaseid(String cbaseid) {
		this.cbaseid = cbaseid;
	}

	public UFDouble getNpraynum() {
		return npraynum;
	}

	public void setNpraynum(UFDouble npraynum) {
		this.npraynum = npraynum;
	}

	public String getCassistunit() {
		return cassistunit;
	}

	public void setCassistunit(String cassistunit) {
		this.cassistunit = cassistunit;
	}

	public UFDouble getNassistnum() {
		return nassistnum;
	}

	public void setNassistnum(UFDouble nassistnum) {
		this.nassistnum = nassistnum;
	}

	public UFDouble getNsuggestprice() {
		return nsuggestprice;
	}

	public void setNsuggestprice(UFDouble nsuggestprice) {
		this.nsuggestprice = nsuggestprice;
	}

	public String getCvendormangid() {
		return cvendormangid;
	}

	public void setCvendormangid(String cvendormangid) {
		this.cvendormangid = cvendormangid;
	}

	public String getCvendorbaseid() {
		return cvendorbaseid;
	}

	public void setCvendorbaseid(String cvendorbaseid) {
		this.cvendorbaseid = cvendorbaseid;
	}

	public UFDate getDdemanddate() {
		return ddemanddate;
	}

	public void setDdemanddate(UFDate ddemanddate) {
		this.ddemanddate = ddemanddate;
	}

	public UFDate getDsuggestdate() {
		return dsuggestdate;
	}

	public void setDsuggestdate(UFDate dsuggestdate) {
		this.dsuggestdate = dsuggestdate;
	}

	public String getCwarehouseid() {
		return cwarehouseid;
	}

	public void setCwarehouseid(String cwarehouseid) {
		this.cwarehouseid = cwarehouseid;
	}

	public UFDouble getNaccumulatenum() {
		return naccumulatenum;
	}

	public void setNaccumulatenum(UFDouble naccumulatenum) {
		this.naccumulatenum = naccumulatenum;
	}

	public String getCsourcebilltype() {
		return csourcebilltype;
	}

	public void setCsourcebilltype(String csourcebilltype) {
		this.csourcebilltype = csourcebilltype;
	}

	public String getCsourcebillid() {
		return csourcebillid;
	}

	public void setCsourcebillid(String csourcebillid) {
		this.csourcebillid = csourcebillid;
	}

	public String getCsourcebillrowid() {
		return csourcebillrowid;
	}

	public void setCsourcebillrowid(String csourcebillrowid) {
		this.csourcebillrowid = csourcebillrowid;
	}

	public String getCupsourcebilltype() {
		return cupsourcebilltype;
	}

	public void setCupsourcebilltype(String cupsourcebilltype) {
		this.cupsourcebilltype = cupsourcebilltype;
	}

	public String getCupsourcebillid() {
		return cupsourcebillid;
	}

	public void setCupsourcebillid(String cupsourcebillid) {
		this.cupsourcebillid = cupsourcebillid;
	}

	public String getCupsourcebillrowid() {
		return cupsourcebillrowid;
	}

	public void setCupsourcebillrowid(String cupsourcebillrowid) {
		this.cupsourcebillrowid = cupsourcebillrowid;
	}

	public String getBodyvmemo() {
		return bodyvmemo;
	}

	public void setBodyvmemo(String bodyvmemo) {
		this.bodyvmemo = bodyvmemo;
	}

	public String getCrowno() {
		return crowno;
	}

	public void setCrowno(String crowno) {
		this.crowno = crowno;
	}

	public String getCemployeeid() {
		return cemployeeid;
	}

	public void setCemployeeid(String cemployeeid) {
		this.cemployeeid = cemployeeid;
	}

	public Integer getNpriceauditbill() {
		return npriceauditbill;
	}

	public void setNpriceauditbill(Integer npriceauditbill) {
		this.npriceauditbill = npriceauditbill;
	}

	public Integer getNquotebill() {
		return nquotebill;
	}

	public void setNquotebill(Integer nquotebill) {
		this.nquotebill = nquotebill;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "cpraybill_bid";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return "cpraybillid";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return getTableName2();
	}
	
	public static String getTableName2(){
		return " po_praybill h inner join po_praybill_b b on h.cpraybillid = b.cpraybillid " 
		       +" inner join bd_invbasdoc inv on inv.pk_invbasdoc = b.cbaseid "
		       +" inner join bd_invcl cl on cl.pk_invcl = inv.pk_invcl ";
	}
	
	public  String[] getAttributeNames(){
		return getAttributeNames2(); 
	}
	
	public static String buildSelectSql(String whereSql){
		StringBuffer strb = new StringBuffer();
		strb.append("select ");
		String[] names = PuPlanInvVO.getAttributeNames2();
		for(String name:names){
			strb.append(name+",");
		}
		strb.append(" 'aaa' ");
		strb.append(" from ");
		strb.append(getTableName2());
		strb.append(" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 ");
		if(PuPubVO.getString_TrimZeroLenAsNull(whereSql)!=null)
			strb.append(" and "+whereSql);
	
		return strb.toString();
	}
	
	/**
	 * @return java.lang.String[]
	 */
	public static String[] getAttributeNames2() {
		return new String[]{
				  "h.cpraybillid"//            �빺��id                
				 ,"h.pk_corp"//             ��˾����                                                          
				 ,"h.vpraycode"//        �빺�����                                                        
				 ,"h.dpraydate"//            �빺����                                                          
				 ,"h.cdeptid"//            �빺����id                                                        
				 ,"h.cpraypsn"//            �빺��id                                                          
				 ,"h.coperator"//            �Ƶ���id                                                          
				 ,"h.ibillstatus"//        ����״̬                                                          
				 ,"h.dauditdate"//            ��������                                                          
				 ,"h.cauditpsn "//            ������id                                                          
				 ,"h.cplannerid"//            �ƻ�Ա                                                            
				 ,"h.vmemo"//       ��ע                                                              
//				�빺�����ֶ�
				 
				 ,"b.cpraybill_bid"//            �빺����id                                                                                                       
				 ,"b.pk_reqcorp"//             ����˾                                                          
				 ,"b.pk_reqstoorg"//            ��������֯                                                      
				 ,"b.pk_purcorp"//             �ɹ���˾                                                          
				 ,"b.cpurorganization"//            �ɹ���֯id                                                        
				 ,"b.cmangid"//            �������id                                                        
				 ,"b.cbaseid"//            �������id                                                        
				 ,"coalesce(b.npraynum,0.0)-coalesce(b.naccumulatenum,0.0) npraynum"//�ɹ�����                                                          
				 ,"b.cassistunit"//            ��������λid                                                      
				 ,"b.nassistnum"//����������                                                        
				 ,"b.nsuggestprice"// ���鵥��                                                          
				 ,"b.cvendormangid"//            ���鹩Ӧ�̹���id                                                  
				 ,"b.cvendorbaseid"//            ���鹩Ӧ�̻���id                                                  
				 ,"b.ddemanddate"//��������                                                          
				 ,"b.dsuggestdate"//���鶩������                                                      
				 ,"b.cwarehouseid"//            ����ֿ�id                                                        
				 ,"b.naccumulatenum"//�ۼƶ�������                                                                                                           
				 ,"b.csourcebilltype"//          ��Դ��������                                                      
				 ,"b.csourcebillid"//            ��Դ����id                                                        
				 ,"b.csourcebillrowid"//            ��Դ������id                                                      
				 ,"b.cupsourcebilltype"//          �ϲ㵥������                                                      
				 ,"b.cupsourcebillid"//            �ϲ���Դ����id                                                    
				 ,"b.cupsourcebillrowid"//            �ϲ���Դ������id                                                  
				 ,"b.vmemo bodyvmemo"//��ע                                                                                                               
				 ,"b.crowno"//�к�                                                              
				 ,"b.cemployeeid"//            �ɹ�Ա                                                                                                                  
				 ,"b.npriceauditbill"//���ɼ۸�����������                                      
				 ,"b.nquotebill"//����ѯ���۵�����   
		};
	}

}

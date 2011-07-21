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
 * @说明：（鹤岗矿业）参照采购计划划分标段 专用的 采购计划vo
 * 2011-5-18下午02:04:32
 */

public class PuPlanInvVO extends SuperVO {
//	请购单头字段
	 private String cpraybillid;//            请购单id                
	 private String pk_corp;//             公司主键                                                          
	 private String vpraycode;//        请购单编号                                                        
	 private UFDate dpraydate;//            请购日期                                                          
	 private String cdeptid;//            请购部门id                                                        
	 private String cpraypsn;//            请购人id                                                          
	 private String coperator;//            制单人id                                                          
	 private Integer ibillstatus;//        单据状态                                                          
	 private UFDate dauditdate;//            审批日期                                                          
	 private String cauditpsn ;//            审批人id                                                          
	 private String cplannerid;//            计划员                                                            
	 private String vmemo;//       备注                                                              
//	请购单体字段
	 
	 private String cpraybill_bid;//            请购单行id                                                                                                       
	 private String pk_reqcorp;//             需求公司                                                          
	 private String pk_reqstoorg;//            需求库存组织                                                      
	 private String pk_purcorp;//             采购公司                                                          
	 private String cpurorganization;//            采购组织id                                                        
	 private String cmangid;//            存货管理id                                                        
	 private String cbaseid;//            存货基础id                                                        
	 private UFDouble npraynum;//需求数量                                                          
	 private String cassistunit;//            辅计量单位id                                                      
	 private UFDouble nassistnum;//辅计量数量                                                        
	 private UFDouble nsuggestprice;// 建议单价                                                          
	 private String cvendormangid;//            建议供应商管理id                                                  
	 private String cvendorbaseid;//            建议供应商基础id                                                  
	 private UFDate ddemanddate;//需求日期                                                          
	 private UFDate dsuggestdate;//建议订货日期                                                      
	 private String cwarehouseid ;//            需求仓库id                                                        
	 private UFDouble naccumulatenum ;//累计订货数量                                                                                                           
	 private String csourcebilltype ;//          来源单据类型                                                      
	 private String csourcebillid       ;//            来源单据id                                                        
	 private String csourcebillrowid    ;//            来源单据行id                                                      
	 private String cupsourcebilltype             ;//          上层单据类型                                                      
	 private String cupsourcebillid     ;//            上层来源单据id                                                    
	 private String cupsourcebillrowid  ;//            上层来源单据行id                                                  
	 private String bodyvmemo;//备注                                                                                                               
	 private String crowno;//行号                                                              
	 private String cemployeeid         ;//            采购员                                                                                                                  
	 private Integer npriceauditbill;//生成价格审批单次数                                      
	 private Integer nquotebill;//生成询报价单次数         
	 
	 
	 public static  void checkInvUnique(PuPlanInvVO[] invs) throws ValidationException{
		 if(invs == null || invs.length == 0)
			 return;
		 java.util.Set<String> ss = new HashSet<String>();
		 for(PuPlanInvVO inv:invs){
			 ss.add(inv.getCbaseid());
		 }
		 if(invs.length!=ss.size()){
			 throw new ValidationException("存在重复品种");
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
				  "h.cpraybillid"//            请购单id                
				 ,"h.pk_corp"//             公司主键                                                          
				 ,"h.vpraycode"//        请购单编号                                                        
				 ,"h.dpraydate"//            请购日期                                                          
				 ,"h.cdeptid"//            请购部门id                                                        
				 ,"h.cpraypsn"//            请购人id                                                          
				 ,"h.coperator"//            制单人id                                                          
				 ,"h.ibillstatus"//        单据状态                                                          
				 ,"h.dauditdate"//            审批日期                                                          
				 ,"h.cauditpsn "//            审批人id                                                          
				 ,"h.cplannerid"//            计划员                                                            
				 ,"h.vmemo"//       备注                                                              
//				请购单体字段
				 
				 ,"b.cpraybill_bid"//            请购单行id                                                                                                       
				 ,"b.pk_reqcorp"//             需求公司                                                          
				 ,"b.pk_reqstoorg"//            需求库存组织                                                      
				 ,"b.pk_purcorp"//             采购公司                                                          
				 ,"b.cpurorganization"//            采购组织id                                                        
				 ,"b.cmangid"//            存货管理id                                                        
				 ,"b.cbaseid"//            存货基础id                                                        
				 ,"coalesce(b.npraynum,0.0)-coalesce(b.naccumulatenum,0.0) npraynum"//采购数量                                                          
				 ,"b.cassistunit"//            辅计量单位id                                                      
				 ,"b.nassistnum"//辅计量数量                                                        
				 ,"b.nsuggestprice"// 建议单价                                                          
				 ,"b.cvendormangid"//            建议供应商管理id                                                  
				 ,"b.cvendorbaseid"//            建议供应商基础id                                                  
				 ,"b.ddemanddate"//需求日期                                                          
				 ,"b.dsuggestdate"//建议订货日期                                                      
				 ,"b.cwarehouseid"//            需求仓库id                                                        
				 ,"b.naccumulatenum"//累计订货数量                                                                                                           
				 ,"b.csourcebilltype"//          来源单据类型                                                      
				 ,"b.csourcebillid"//            来源单据id                                                        
				 ,"b.csourcebillrowid"//            来源单据行id                                                      
				 ,"b.cupsourcebilltype"//          上层单据类型                                                      
				 ,"b.cupsourcebillid"//            上层来源单据id                                                    
				 ,"b.cupsourcebillrowid"//            上层来源单据行id                                                  
				 ,"b.vmemo bodyvmemo"//备注                                                                                                               
				 ,"b.crowno"//行号                                                              
				 ,"b.cemployeeid"//            采购员                                                                                                                  
				 ,"b.npriceauditbill"//生成价格审批单次数                                      
				 ,"b.nquotebill"//生成询报价单次数   
		};
	}

}

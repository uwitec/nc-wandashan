package nc.bs.ia.hg.report;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.hg.ia.report.IaInvInOutReportVO;
import nc.vo.hg.ia.report.InOutSumPubVO;
import nc.vo.scm.pu.PuPubVO;

/**
 *  查询本期开票数量金额 汇总
 * @author zhf
 *
 */

public class InvoiceSumBO extends AbstractInOutSumQryBO {

	public InvoiceSumBO(BaseDAO dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}
	
	

	
	@Override
	public String buildSql(String date1, String date2, String whereCondition) {
		// TODO Auto-generated method stub

		/**
		 * 汇总本期全部采购发票开票数量和金额  （已结算的发票  本期未结算的发票不考虑在本期范围内）
		 */

//		StringBuffer str = new StringBuffer();
//		str.append("select ih.pk_corp,h.pk_calbody crdcenterid");
//		if(InOutSumPubVO.isware.booleanValue())
//			str.append(",h.cwarehouseid");
//		if(InOutSumPubVO.isinvcl.booleanValue()){
//			str.append(",substr(invcode,0,2) cinvclid") ;
//		}else{
//			str.append(", ib.cbaseid cinvbasid,ib.cmangid cinventoryid") ;
//		}
//		
//		if(InOutSumPubVO.isqrybatch.booleanValue())
//			str.append(",b.vbatchcode vbatch"); 
//		str.append(",sum(ib.ninvoicenum) nnum,sum(ib.nmoney) nmny ");
//
//		str.append(" from po_invoice_b ib inner join po_invoice ih on ih.cinvoiceid =  ib.cinvoiceid");
//
//		str.append(" inner join po_settlebill_b s on s.cinvoice_bid  = ib.cinvoice_bid");
//
//		str.append(" inner join ic_general_b b on b.cgeneralbid = s.cstockrow");
//
//		str.append(" inner join ic_general_h h on h.cgeneralhid = b.cgeneralhid");
//		
//		str.append(" inner join bd_invbasdoc inv on inv.pk_invbasdoc = ib.cbaseid");
//
//
//		str.append(" where nvl(ib.dr,0)=0 and nvl(ih.dr,0) = 0 and nvl(s.dr,0)=0 and nvl(h.dr,0)=0 and nvl(b.dr,0)=0");
//
//		str.append(" and ih.dinvoicedate >='"+date1+"' and ih.dinvoicedate<='"+date2+"'");
//		if(PuPubVO.getString_TrimZeroLenAsNull(whereCondition)!=null)
//			str.append(" and " + whereCondition);
//		str.append(" group by ih.pk_corp,h.pk_calbody");
//		if(InOutSumPubVO.isware.booleanValue())
//			str.append(",b.cwarehouseid");
//		
//		
//		if(InOutSumPubVO.isinvcl.booleanValue()){
//			str.append(",substr(invcode,0,2)") ;
//		}else{
//			str.append(", ib.cbaseid,ib.cmangid") ;
//		}
////			str.append(", ib.cbaseid,ib.cmangid");
//		if(InOutSumPubVO.isqrybatch.booleanValue())
//			str.append(" ,b.vbatch");
//		return str.toString();
		
		
		
//		原代码 调整  丢失了      vmi  的发票量
		StringBuffer str = new StringBuffer();
		str.append(" select pk_corp, crdcenterid,");
		if(InOutSumPubVO.isinvcl.booleanValue()){
			str.append("cinvclid") ;
		}else{
			str.append("cinvbasid,cinventoryid") ;
		}
		str.append(",sum(coalesce(nnum, 0)) nnum,sum(coalesce(nmny, 0)) nmny from (");
		str.append("select distinct ih.pk_corp pk_corp,ih.cstoreorganization crdcenterid");
//		if(InOutSumPubVO.isware.booleanValue())----------------不考虑仓库
//			str.append(",h.cwarehouseid");
		if(InOutSumPubVO.isinvcl.booleanValue()){
			str.append(",substr(invcode,0,2) cinvclid") ;
		}else{
			str.append(", ib.cbaseid cinvbasid,ib.cmangid cinventoryid") ;
		}
		
//		if(InOutSumPubVO.isqrybatch.booleanValue())-------------不考虑批次
//			str.append(",b.vbatchcode vbatch"); 
		str.append(",coalesce(ib.ninvoicenum,0) nnum,coalesce(ib.ninvoicenum, 0)*coalesce(h.nplanedprice, 0) nmny,ib.cinvoice_bid");
//sum(coalesce(ib.ninvoicenum,0)*coalesce(h.nplanedprice,0))
		str.append(" from po_invoice_b ib inner join po_invoice ih on ih.cinvoiceid =  ib.cinvoiceid");

		str.append(" inner join po_settlebill_b s on s.cinvoice_bid  = ib.cinvoice_bid");
		
		str.append(" inner join po_settlebill sh on sh.csettlebillid = s.csettlebillid");

//		str.append(" inner join ic_general_b b on b.cgeneralbid = s.cstockrow");
//
		str.append(" inner join bd_invbasdoc inv on inv.pk_invbasdoc = ib.cbaseid");
		
       // str.append(" inner join bd_invmandoc man on inv.pk_invbasdoc = man.pk_invbasdoc");
		
		str.append(" inner join ia_monthledger h on s.cmangid = h.cinventoryid ");

		str.append(" where nvl(ib.dr,0)=0 and nvl(ih.dr,0) = 0 and nvl(s.dr,0)=0 and nvl(sh.dr,0) = 0 ");
		str.append(" and h.frecordtypeflag = 3 and h.pk_corp = '"+SQLHelper.getCorpPk()+"' and isnull(h.dr,0) = 0 and coalesce(btryflag,'N') = 'N' ");
		str.append(" and sh.dsettledate >='"+date1+"' and sh.dsettledate<='"+date2+"'");
		if(PuPubVO.getString_TrimZeroLenAsNull(whereCondition)!=null)
			str.append(" and " + whereCondition);
		str.append(")group by pk_corp,crdcenterid ");
		
		
		if(InOutSumPubVO.isinvcl.booleanValue()){
			str.append(",cinvclid") ;
		}else{
			str.append(",cinvbasid,cinventoryid") ;
		}
//			str.append(", ib.cbaseid,ib.cmangid");
//		if(InOutSumPubVO.isqrybatch.booleanValue())
//			str.append(" ,b.vbatch");
		return str.toString();
	}



	@Override
	protected void appData2(IaInvInOutReportVO data, InOutSumPubVO num) {
		// TODO Auto-generated method stub
		if(data == null || num == null)
			return;
		data.setNinvoicenum(num.getNnum());
		data.setNinvoicemny(num.getNmny());
	}

}

package nc.bs.ia.hg.report;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.hg.ia.report.IaInvInOutReportVO;
import nc.vo.hg.ia.report.InOutSumPubVO;
import nc.vo.scm.pu.PuPubVO;

public class LastEstimateSumBO extends AbstractInOutSumQryBO {

	public LastEstimateSumBO(BaseDAO dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String buildSql(String date1, String date2, String whereCondition) {
		// TODO Auto-generated method stub
		
		/**
		 * 冲以前月份暂估   的  发票数量 和 金额（本期所有的已结算发票：1、和本期的入库结算  2、和前期的入库结算）
		 *   取出 和 前期结算的本期的发票量和金额
		 */
		
		

		// TODO Auto-generated method stub

//		/**
//		 * 汇总本期全部采购发票开票数量和金额  （已结算的发票  本期未结算的发票不考虑在本期范围内）
//		 */

		StringBuffer str = new StringBuffer();
		str.append(" select pk_corp, crdcenterid,");
		if(InOutSumPubVO.isinvcl.booleanValue()){
			str.append("cinvclid") ;
		}else{
			str.append("cinvbasid,cinventoryid") ;
		}
		str.append(",sum(coalesce(nnum, 0)) nnum,sum(coalesce(nmny, 0)) nmny from (");
		str.append("select distinct ih.pk_corp pk_corp,lh.pk_calbody crdcenterid");
		if(InOutSumPubVO.isware.booleanValue())
			str.append(",h.cwarehouseid");
		if(InOutSumPubVO.isinvcl.booleanValue()){
			str.append(",substr(invcode,0,2) cinvclid") ;
		}else{
			str.append(", ib.cbaseid cinvbasid,ib.cmangid cinventoryid") ;
		}
		
		if(InOutSumPubVO.isqrybatch.booleanValue())
			str.append(",b.vbatchcode vbatch"); 
		str.append(",ib.ninvoicenum nnum,coalesce(ib.ninvoicenum, 0)*coalesce(h.nplanedprice, 0) nmny,ib.cinvoice_bid ");

		str.append(" from po_invoice_b ib inner join po_invoice ih on ih.cinvoiceid =  ib.cinvoiceid");

		str.append(" inner join po_settlebill_b s on s.cinvoice_bid  = ib.cinvoice_bid");
		
		str.append(" inner join po_settlebill sh on sh.csettlebillid = s.csettlebillid");

		str.append(" inner join ic_general_b b on b.cgeneralbid = s.cstockrow");

		str.append(" inner join ic_general_h lh on lh.cgeneralhid = b.cgeneralhid");
		
		str.append(" inner join bd_invbasdoc inv on inv.pk_invbasdoc = ib.cbaseid");
		
//		str.append(" inner join ic_general_bb3 bb3 on bb3.cgeneralbid = b.cgeneralbid ");
		str.append(" inner join ia_monthledger h on s.cmangid = h.cinventoryid ");

		str.append(" where nvl(ib.dr,0)=0 and nvl(ih.dr,0) = 0 " +
				"and nvl(s.dr,0)=0 and nvl(lh.dr,0)=0 and nvl(b.dr,0)=0 and nvl(sh.dr,0)=0 ");
		str.append(" and h.frecordtypeflag = 3 and h.pk_corp = '"+SQLHelper.getCorpPk()+"' and isnull(h.dr,0) = 0 and coalesce(btryflag,'N') = 'N' ");
		str.append(" and sh.dsettledate >='"+date1+"' and sh.dsettledate<='"+date2+"'");
		if(PuPubVO.getString_TrimZeroLenAsNull(whereCondition)!=null)
			str.append(" and " + whereCondition);
		str.append(" and  b.dzgdate < '"+date1+"'");
		str.append(") group by pk_corp,crdcenterid");
		if(InOutSumPubVO.isware.booleanValue())
			str.append(",b.cwarehouseid");
		
		
		if(InOutSumPubVO.isinvcl.booleanValue()){
			str.append(",cinvclid") ;
		}else{
			str.append(", cinvbasid,cinventoryid") ;
		}
//			str.append(", ib.cbaseid,ib.cmangid");
		if(InOutSumPubVO.isqrybatch.booleanValue())
			str.append(" ,b.vbatch");
		
		
		
//		查询vmi的暂估----------------------------------------------------
		
		StringBuffer str2 = new StringBuffer();
		str2.append("select ih.pk_corp pk_corp,ih.cstoreorganization crdcenterid");
//		if(InOutSumPubVO.isware.booleanValue())
//			str2.append(",h.cwarehouseid");
		if(InOutSumPubVO.isinvcl.booleanValue()){
			str2.append(",substr(invcode,0,2) cinvclid") ;
		}else{
			str2.append(", ib.cbaseid cinvbasid,ib.cmangid cinventoryid") ;
		}
		
//		if(InOutSumPubVO.isqrybatch.booleanValue())
//			str2.append(",b.vbatchcode vbatch"); 
		str2.append(",sum(ib.ninvoicenum) nnum,sum(s.ngaugemny) nmny ");

		str2.append(" from po_invoice_b ib inner join po_invoice ih on ih.cinvoiceid =  ib.cinvoiceid");

		str2.append(" inner join po_settlebill_b s on s.cinvoice_bid  = ib.cinvoice_bid");

		str2.append(" inner join po_settlebill sh on sh.csettlebillid = s.csettlebillid");
//		str2.append(" inner join ic_general_b b on b.cgeneralbid = s.cstockrow");

		str2.append(" inner join ic_vmi_sum h on h.cvmihid = s.cvmiid");
		
		str2.append(" inner join bd_invbasdoc inv on inv.pk_invbasdoc = ib.cbaseid");
		
//		str2.append(" inner join ic_general_bb3 bb3 on bb3.cgeneralbid = b.cgeneralbid ");


		str2.append(" where nvl(ib.dr,0)=0 and nvl(ih.dr,0) = 0 and nvl(s.dr,0)=0 and nvl(h.dr,0)=0 ");
		str2.append(" and sh.dsettledate >='"+date1+"' and sh.dsettledate<='"+date2+"'");
		//str2.append(" and ih.dinvoicedate >='"+date1+"' and ih.dinvoicedate<='"+date2+"'");
		if(PuPubVO.getString_TrimZeroLenAsNull(whereCondition)!=null)
			str2.append(" and " + whereCondition);
		str2.append(" and  h.dgaugedate < '"+date1+"'");
		str2.append(" group by ih.pk_corp,ih.cstoreorganization");
//		if(InOutSumPubVO.isware.booleanValue())
//			str2.append(",b.cwarehouseid");
		
		
		if(InOutSumPubVO.isinvcl.booleanValue()){
			str2.append(",substr(invcode,0,2)") ;
		}else{
			str2.append(", ib.cbaseid,ib.cmangid") ;
		}
//			str2.append(", ib.cbaseid,ib.cmangid");
//		if(InOutSumPubVO.isqrybatch.booleanValue())
//			str2.append(" ,b.vbatch");
		
		String s=null;
		
		if(InOutSumPubVO.isinvcl.booleanValue()){
			s=" select pk_corp,crdcenterid,cinvclid,sum(coalesce(nnum,0.0)) nnum, sum(coalesce(nmny,0.0)) nmny  from (("+str.toString()+") union all ("+str2.toString()+")) group by pk_corp,crdcenterid,cinvclid";
		}else{
			s=" select pk_corp,crdcenterid,cinvbasid,cinventoryid,sum(coalesce(nnum,0.0)) nnum, sum(coalesce(nmny,0.0)) nmny  from (("+str.toString()+") union all ("+str2.toString()+")) group by pk_corp,crdcenterid, cinvbasid,cinventoryid";
		}
		return s ;
	
		
		
//		InvoiceSumBO invoicebo = new InvoiceSumBO(dao);
//		if(PuPubVO.getString_TrimZeroLenAsNull(whereCondition)==null)
//			whereCondition = " 1=1 ";
//		String sql = invoicebo.buildSql(date1, date2, whereCondition+" and  h.dbilldate < '"+date1+"'");
//		sql = sql + ;
//		return sql;
	}

	@Override
	protected void appData2(IaInvInOutReportVO data, InOutSumPubVO num) {
		// TODO Auto-generated method stub
		if(data == null || num == null)
			return;
		data.setNlastestnum(num.getNnum());
		data.setNlastestmny(num.getNmny());
	}

}

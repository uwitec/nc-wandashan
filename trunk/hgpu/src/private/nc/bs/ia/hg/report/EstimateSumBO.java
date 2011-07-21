package nc.bs.ia.hg.report;

import nc.bs.dao.BaseDAO;
import nc.vo.hg.ia.report.IaInvInOutReportVO;
import nc.vo.hg.ia.report.InOutSumPubVO;
import nc.vo.scm.pu.PuPubVO;

public class EstimateSumBO extends AbstractInOutSumQryBO {

	public EstimateSumBO(BaseDAO dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}
	
//	select  h.pk_corp,h.pk_calbody,h.cwarehouseid , b.cinvbasid,b.cinventoryid ,b.vbatchcode , sum(b.ninnum),sum(bb3.npmoney)
	//
//			from ic_general_b b
//			 
//			 inner join ic_general_h h on h.cgeneralhid = b.cgeneralhid
//			 
//			 inner join ic_general_bb3 bb3 on bb3.cgeneralbid = b.cgeneralbid
	//
	//
//			where nvl(b.dr,0)=0 and nvl(h.dr,0) = 0  and nvl(bb3.dr,0)=0
	//
//			and h.dbilldate >='2011-04-25' and h.dbilldate<='2011-05-26' and b.bzgflag = 'Y' and b.isok = 'N'
//			group by  h.pk_corp,h.pk_calbody,h.cwarehouseid , b.cinvbasid,b.cinventoryid ,b.vbatchcode

	@Override
	public String buildSql(String date1, String date2, String whereCondition) {
		// TODO Auto-generated method stub

		/**
		 * 查询本期 已暂估 未结算的  汇总数量和金额
		 */
		
//		查询非vmi的暂估

		StringBuffer str = new StringBuffer();
		str.append("select  h.pk_corp pk_corp,h.pk_calbody crdcenterid" );
		if(InOutSumPubVO.isware.booleanValue()){
			str.append(",h.cwarehouseid");
		}
		if(InOutSumPubVO.isinvcl.booleanValue()){
			str.append(",substr(invcode,0,2) cinvclid");
		}else{
			str.append(",b.cinvbasid cinvbasid,b.cinventoryid cinventoryid");
		}
		str.append(",sum(b.ninnum-coalesce(bb3.naccountnum1,0.0)) nnum,sum((b.ninnum-coalesce(bb3.naccountnum1,0.0))*bb3.npprice) nmny");//sum(bb3.npmoney) nmny");
		if(InOutSumPubVO.isqrybatch.booleanValue()){
			str.append(" ,b.vbatchcode vbatch ");
		}
		str.append(" from ic_general_b b");
		str.append(" inner join ic_general_h h on h.cgeneralhid = b.cgeneralhid");
		str.append(" inner join ic_general_bb3 bb3 on bb3.cgeneralbid = b.cgeneralbid");
		str.append(" inner join bd_invbasdoc inv on inv.pk_invbasdoc = b.cinvbasid ");
		str.append(" where nvl(b.dr,0)=0 and nvl(h.dr,0) = 0  and nvl(bb3.dr,0)=0");
		str.append(" and b.dzgdate >='"+date1+"' and b.dzgdate<='"+date2+"' and b.bzgflag = 'Y' and coalesce(b.isok,'N') = 'N'");
		if(PuPubVO.getString_TrimZeroLenAsNull(whereCondition)!=null){
			str.append(" and "+whereCondition);
		}
		str.append(" group by  h.pk_corp,h.pk_calbody");
		if(InOutSumPubVO.isware.booleanValue())
			str.append(",h.cwarehouseid");
		if(InOutSumPubVO.isinvcl.booleanValue()){
			str.append(",substr(invcode,0,2)");
		}else{
			str.append(",b.cinvbasid,b.cinventoryid");
		}
		if(InOutSumPubVO.isqrybatch.booleanValue())
			str.append(",b.vbatchcode");
//		return str.toString();
		
//		----------------查询vmi的暂估------------------------------------------------------------
		StringBuffer str2 = new StringBuffer();
		str2.append("select  h.pk_corp pk_corp,h.ccalbodyid crdcenterid" );
//		if(InOutSumPubVO.isware.booleanValue()){
//			str2.append(",h.cwarehouseid");
//		}
		if(InOutSumPubVO.isinvcl.booleanValue()){
			str2.append(",substr(invcode,0,2) cinvclid");
		}else{
			str2.append(",inv.pk_invbasdoc cinvbasid,h.cinventoryid cinventoryid");
		}
		str2.append(",sum(h.nrsvnum1-coalesce(h.naccountnum,0.0)) nnum,sum((h.nrsvnum1-coalesce(h.naccountnum,0.0))*h.nprice) nmny");//sum(bb3.npmoney) nmny");
//		if(InOutSumPubVO.isqrybatch.booleanValue()){
//			str2.append(" ,b.vbatchcode vbatch ");
//		}
		str2.append(" from ic_vmi_sum h");
//		str2.append(" inner join ic_general_h h on h.cgeneralhid = b.cgeneralhid");
		str2.append(" inner join bd_invmandoc man on man.pk_invmandoc = h.cinventoryid");
		str2.append(" inner join bd_invbasdoc inv on inv.pk_invbasdoc = man.pk_invbasdoc ");
		str2.append(" where  nvl(h.dr,0) = 0");
		str2.append(" and h.dgaugedate >='"+date1+"' and h.dgaugedate<='"+date2+"' and h.bgaugeflag = 'Y' and coalesce(h.bsettleendflag,'N') = 'N'");
		if(PuPubVO.getString_TrimZeroLenAsNull(whereCondition)!=null){
			str2.append(" and "+whereCondition);
		}
		str2.append(" group by h.pk_corp,h.ccalbodyid");
//		if(InOutSumPubVO.isware.booleanValue())
//			str2.append(",h.cwarehouseid");
		if(InOutSumPubVO.isinvcl.booleanValue()){
			str2.append(",substr(invcode,0,2)");
		}else{
			str2.append(",inv.pk_invbasdoc,h.cinventoryid");
		}
//		if(InOutSumPubVO.isqrybatch.booleanValue())
//			str2.append(",b.vbatchcode");
		String  s =null;
		if(InOutSumPubVO.isinvcl.booleanValue()){
			s = "select pk_corp,crdcenterid,cinvclid,sum (coalesce(nnum,0.0)) nnum,sum(coalesce(nmny,0.0)) nmny from (("+str.toString()+") union all ("+str2.toString()+")) group by pk_corp,crdcenterid,cinvclid ";
		}else{
			s = "select pk_corp,crdcenterid,cinvbasid,cinventoryid,sum (coalesce(nnum,0.0)) nnum,sum(coalesce(nmny,0.0)) nmny from (("+str.toString()+") union all ("+str2.toString()+")) group by pk_corp,crdcenterid,cinvbasid,cinventoryid ";
		}
		return s;
	}


	@Override
	protected void appData2(IaInvInOutReportVO data, InOutSumPubVO num) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		if(data == null || num == null)
			return;
		data.setNestimatenum(num.getNnum());
		data.setNestimatemny(num.getNmny());
	
	}
}

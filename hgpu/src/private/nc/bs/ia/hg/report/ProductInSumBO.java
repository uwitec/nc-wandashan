package nc.bs.ia.hg.report;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.hg.ia.report.IaInvInOutReportVO;
import nc.vo.hg.ia.report.InOutSumPubVO;
import nc.vo.scm.pu.PuPubVO;

public class ProductInSumBO extends AbstractInOutSumQryBO {

	public ProductInSumBO(BaseDAO dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}


//		select  h.pk_corp,h.pk_calbody,h.cwarehouseid , b.cinvbasid,b.cinventoryid ,b.vbatchcode , sum(b.ninnum),sum(bb3.npmoney)
//
//		from ic_general_b b	 inner join ic_general_h h on h.cgeneralhid = b.cgeneralhid
//		 
//
//
//
//		where nvl(b.dr,0)=0 and nvl(h.dr,0) = 0  and nvl(bb3.dr,0)=0 and h.fbillflag = 4 and h.cbilltypecode = '44'
//
//		and h.dbilldate >='2011-04-25' and h.dbilldate<='2011-05-26' 
//		group by  h.pk_corp,h.pk_calbody,h.cwarehouseid , b.cinvbasid,b.cinventoryid ,b.vbatchcode

	@Override
	public String buildSql(String date1, String date2, String whereCondition) {
		// TODO Auto-generated method stub

		/**
		 * 查询产成品入库单的数量和金额  ： 本期所有的已签字的 产成品入库单
		 */
		StringBuffer str = new StringBuffer();
		str.append("select h.pk_corp,h.pk_calbody crdcenterid");
		if(InOutSumPubVO.isware.booleanValue())
			str.append(",h.cwarehouseid");
		if(InOutSumPubVO.isinvcl.booleanValue()){
			str.append(",substr(invcode,0,2) cinvclid");
		}else{
			str.append(",b.cinvbasid,b.cinventoryid");
		}
		str.append(",sum(b.ninnum) nnum,sum(b.nplannedmny) nmny");
		if(InOutSumPubVO.isqrybatch.booleanValue()){
			str.append(" ,b.vbatchcode vbatch");
		}
		str.append(" from ic_general_b b inner join ic_general_h h on h.cgeneralhid = b.cgeneralhid");
		str.append(" inner join bd_invbasdoc inv on inv.pk_invbasdoc = b.cinvbasid ");
		str.append(" where nvl(b.dr,0)=0 and nvl(h.dr,0) = 0 and h.cregister is not null and h.cbilltypecode = '46'");
		str.append(" and h.dbilldate >='"+date1+"' and h.dbilldate<='"+date2+"'");
		str.append(" and h.pk_corp = '"+SQLHelper.getCorpPk()+"'");
		if(PuPubVO.getString_TrimZeroLenAsNull(whereCondition)!=null){
			str.append(" and "+whereCondition);
		}
		str.append(" group by  h.pk_corp,h.pk_calbody");
		if(InOutSumPubVO.isware.booleanValue())
			str.append(",h.cwarehouseid");
//		str.append(", b.cinvbasid,b.cinventoryid ");
		if(InOutSumPubVO.isinvcl.booleanValue()){
			str.append(",substr(invcode,0,2)");
		}else{
			str.append(",b.cinvbasid,b.cinventoryid");
		}
		if(InOutSumPubVO.isqrybatch.booleanValue()){
			str.append(" ,b.vbatchcode");
		}
		str.append(getOrderBySql());
		return str.toString();
	}

	@Override
	protected void appData2(IaInvInOutReportVO data, InOutSumPubVO num) {
		// TODO Auto-generated method stub
		if(data == null || num == null)
			return;
		data.setNproductnum(num.getNnum());
		data.setNproductmny(num.getNmny());
	}

//	@Override
//	public String dealWhereSql(ConditionVO[] cons) {
//		// TODO Auto-generated method stub
//		if(cons == null)
//		return ;
//	}

}

package nc.bs.ia.hg.report;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.hg.ia.report.IaInvInOutReportVO;
import nc.vo.hg.ia.report.InOutSumPubVO;
import nc.vo.scm.pu.PuPubVO;

public class OtherInSumBO extends AbstractInOutSumQryBO {

	public OtherInSumBO(BaseDAO dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String buildSql(String date1, String date2, String whereCondition) {
		// TODO Auto-generated method stub

		/**
		 * 查询其他入库单的数量和金额  ： 本期所有的已签字的 其他入库单
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
		str.append(" inner join ia_bill_b iab on iab.csourcebillitemid = b.cgeneralbid ");//和存货核算关联 保证传入了存货核算
		str.append(" where nvl(b.dr,0)=0 and nvl(h.dr,0) = 0 and  nvl(iab.dr,0) = 0 and h.cregister is not null and h.cbilltypecode = '4A'");
		str.append(" and h.dbilldate >='"+date1+"' and h.dbilldate<='"+date2+"'");
		str.append(" and h.pk_corp = '"+SQLHelper.getCorpPk()+"'");
//		str.append(" and ");
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
		data.setNdef1(num.getNnum());
		data.setNdef2(num.getNmny());
	}
}

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

public class AlloOutSumBO extends AbstractInOutSumQryBO {

	public AlloOutSumBO(BaseDAO dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public String buildSql(String date1, String date2, String whereCondition) {

		// TODO Auto-generated method stub
		
		/**
		 * 内销出库：调拨出库单    有领料单位的材料出库单
		 */
		StringBuffer str = new StringBuffer();
		str.append("select h.pk_corp,h.pk_calbody crdcenterid, sum(b.noutnum) nnum,sum(coalesce(b.nplannedprice,0)*coalesce(b.noutnum,0)) nmny");
		if(InOutSumPubVO.isware.booleanValue())
			str.append(",h.cwarehouseid ");
		if(InOutSumPubVO.isqrybatch.booleanValue()){
			str.append(" ,b.vbatchcode");
		}
		if(InOutSumPubVO.isinvcl.booleanValue()){
			str.append(",substr(invcode,0,2) cinvclid");
		}else{
			str.append(",b.cinvbasid,b.cinventoryid");
		}
		str.append(" from ic_general_b b	 inner join ic_general_h h on h.cgeneralhid = b.cgeneralhid");
		str.append(" inner join bd_invbasdoc inv on inv.pk_invbasdoc = b.cinvbasid ");
		str.append(" where nvl(b.dr,0)=0 and nvl(h.dr,0) = 0 and h.cregister is not null ");
		str.append(" and h.pk_corp = '"+SQLHelper.getCorpPk()+"'");
		str.append(" and (h.cbilltypecode = '4Y' or h.cbilltypecode = '4D')");
		str.append(" and b.dbizdate >='"+date1+"' and b.dbizdate<='"+date2+"'");
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
		data.setNalloutnum(num.getNnum());
		data.setNalloutmny(num.getNmny());
	}

}

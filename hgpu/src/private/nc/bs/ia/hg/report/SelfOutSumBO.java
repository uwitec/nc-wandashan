package nc.bs.ia.hg.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.hg.ia.report.IaInvInOutReportVO;
import nc.vo.hg.ia.report.InOutSumPubVO;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

/**
 *  查询本期开票数量金额 汇总
 * @author zhf
 *
 */

public class SelfOutSumBO extends AbstractInOutSumQryBO {

	public SelfOutSumBO(BaseDAO dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public String buildSql(String date1, String date2, String whereCondition) {

		// TODO Auto-generated method stub
		
		/**
		 * 自用出库：本期没有领料单位的材料出库单
		 */
		StringBuffer str = new StringBuffer();
//		str.append("select h.pk_corp,h.pk_calbody,h.cwarehouseid , b.cinvbasid,b.cinventoryid , sum(b.noutnum),sum(b.nmny)");
//		if(InOutSumPubVO.isqrybatch.booleanValue()){
//			str.append(" ,b.vbatchcode");
//		}
		
		str.append("select h.pk_corp,h.pk_calbody crdcenterid");
		if(InOutSumPubVO.isware.booleanValue())
			str.append(",h.cwarehouseid");
		if(InOutSumPubVO.isinvcl.booleanValue()){
			str.append(",substr(invcode,0,2) cinvclid");
		}else{
			str.append(",b.cinvbasid,b.cinventoryid");
		}
		str.append(",sum(b.noutnum) nnum,sum(coalesce(b.nplannedprice,0)*coalesce(b.noutnum,0)) nmny");
		if(InOutSumPubVO.isqrybatch.booleanValue()){
			str.append(" ,b.vbatchcode vbatch");
		}
		
		str.append(" from ic_general_b b	 inner join ic_general_h h on h.cgeneralhid = b.cgeneralhid");
		str.append(" inner join bd_invbasdoc inv on inv.pk_invbasdoc = b.cinvbasid ");
		str.append(" where nvl(b.dr,0)=0 and nvl(h.dr,0) = 0 and h.cregister is not null ");
		str.append(" and h.pk_corp = '"+SQLHelper.getCorpPk()+"'");
		str.append(" and  h.cbilltypecode = '4I' and h.cwarehouseid = '"+InOutSumPubVO.MCJGCK+"' ");
		str.append(" and b.dbizdate >='"+date1+"' and b.dbizdate<='"+date2+"'");
		if(PuPubVO.getString_TrimZeroLenAsNull(whereCondition)!=null){
			str.append(" and "+whereCondition);
		}
//		str.append(" group by  h.pk_corp,h.pk_calbody,h.cwarehouseid , b.cinvbasid,b.cinventoryid ");
//		if(InOutSumPubVO.isqrybatch.booleanValue()){
//			str.append(" ,b.vbatchcode");
//		}
		
		str.append(" group by h.pk_corp,h.pk_calbody");
		if(InOutSumPubVO.isware.booleanValue())
			str.append(",h.cwarehouseid");
//		str.append(",b.cinvbasid,b.cinventoryid ");
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
	
//	public  void appData(IaInvInOutReportVO[] datas,InOutSumPubVO[] nums){
//		//		维度：公司 组织  （仓库） 存货 （批次）      数量  金额
//		if(datas == null || datas.length ==0)
//			return;
//		Map<String, InOutSumPubVO> numInfor = InOutSumPubVO.dealData(nums);
//		if(numInfor == null || numInfor.size() == 0)
//			return;
//		StringBuffer keybuf = new StringBuffer();
//		String key = null;
//		List<IaInvInOutReportVO> ldata = new ArrayList<IaInvInOutReportVO>();
//		UFDouble nallnum = UFDouble.ZERO_DBL;
//		for(IaInvInOutReportVO data:datas){
//			keybuf = new StringBuffer();
//			keybuf.append(HgPubTool.getString_NullAsTrimZeroLen(data.getPk_corp()));
//			keybuf.append(HgPubTool.getString_NullAsTrimZeroLen(data.getCrdcenterid()));
//			//    		if(InOutSumPubVO.isware.booleanValue())
//			keybuf.append(HgPubTool.getString_NullAsTrimZeroLen(data.getCwarehouseid()));
//			keybuf.append(HgPubTool.getString_NullAsTrimZeroLen(data.getCinvclid()));
//			keybuf.append(HgPubTool.getString_NullAsTrimZeroLen(data.getCinvbasid()));
//			keybuf.append(HgPubTool.getString_NullAsTrimZeroLen(data.getCinventoryid()));
//			//    		if(InOutSumPubVO.isqrybatch.booleanValue())
//			keybuf.append(HgPubTool.getString_NullAsTrimZeroLen(data.getVbatch()));
//			key = keybuf.toString();
//			appData2(data, numInfor.get(key));
//			nallnum = nallnum.add(PuPubVO.getUFDouble_NullAsZero(data.getNinitnum()))
//			.add(PuPubVO.getUFDouble_NullAsZero(data.getNabnum()))
//			.add(PuPubVO.getUFDouble_NullAsZero(data.getNinvoicenum()))
//			.add(PuPubVO.getUFDouble_NullAsZero(data.getNestimatenum()))
//			.add(PuPubVO.getUFDouble_NullAsZero(data.getNlastestnum()))
//			.add(PuPubVO.getUFDouble_NullAsZero(data.getNproductnum()))
//			.add(PuPubVO.getUFDouble_NullAsZero(data.getNsaleoutnum()))
//			.add(PuPubVO.getUFDouble_NullAsZero(data.getNalloutnum()))
//			.add(PuPubVO.getUFDouble_NullAsZero(data.getNselfoutnum()));
//			if(nallnum.doubleValue()>0)
//				ldata.add(data);
//		}
//		if(ldata.size() == 0)
//			datas = null;
//		ldata.toArray(datas);
//	}

	@Override
	protected void appData2(IaInvInOutReportVO data, InOutSumPubVO num) {
		// TODO Auto-generated method stub
		if(data == null || num == null)
			return;
		data.setNselfoutnum(num.getNnum());
		data.setNselfoutmny(num.getNmny());
	}

}

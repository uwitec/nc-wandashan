package nc.bs.zb.query.pub;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;
import nc.vo.zb.query.ZbDetail.ZbDetailVO;
import nc.vo.zb.query.ZbNmny.ZbNmnyVO;
import nc.vo.zb.query.ZbSitua.ZbSituaVO;

public class ZbQueryPubBO {
	private BaseDAO dao = null;

	private BaseDAO getBaseDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	// 中标金额查询
	public ZbNmnyVO[] queryDatas(String sql, String type) throws BusinessException {
		String str = getSql(sql, type);
		List ldata = (List) getBaseDao().executeQuery(str,
				new BeanListProcessor(ZbNmnyVO.class));

		if (ldata == null || ldata.size() == 0) {
			return null;
		}
		ZbNmnyVO[] datas = (ZbNmnyVO[]) ldata.toArray(new ZbNmnyVO[0]);
		// VOUtil.ascSort(datas, ZbPubConst.DEAL_SORT_FIELDNAMES);
		if (ZbPubConst.ZB_TYPE_DETAIL.equalsIgnoreCase(type))
			ZbNmnyVO.sortZbNmnyVO(datas);
		return datas;
	}

	private String getSql(String sql, String type) throws BusinessException {
		String str = null;
		if (ZbPubConst.ZB_TYPE_VENDOR.equalsIgnoreCase(type)) {
			str = "select sum(coalesce(r.nzbmny,0)) nzbmny,r.ccustmanid,r.ccustbasid  from zb_bidevaluation_h h "
					+ " join zb_evaluation_b b on h.cevaluationid = b.cevaluationid join zb_slvendor r on r.cevaluationbid = b.cevaluationbid"
					+ " where isnull(r.dr,0)=0 and isnull(b.dr,0)=0 and isnull(h.dr,0)=0";
			if (PuPubVO.getString_TrimZeroLenAsNull(sql) != null)
				str = str + " and " + sql;
			String s =ZbPubTool.getParam();
			if(s!=null &&!"".equals(s))
				str =str+" and h.cbiddingid in(select h.cbiddingid from zb_bidding_h h where  nvl(h.dr, 0) = 0 and h.reserve1 = '"+s+"')";
			str = str + " group by r.ccustmanid,r.ccustbasid ";
		} else if (ZbPubConst.ZB_TYPE_CORP.equalsIgnoreCase(type)) {
			str = "select h.pk_deptdoc,r.ccustmanid,r.ccustbasid,sum(coalesce(r.nzbmny,0)) nzbmny from zb_bidevaluation_h h "
					+ " join zb_evaluation_b b on h.cevaluationid = b.cevaluationid join zb_slvendor r on r.cevaluationbid = b.cevaluationbid"
					+ " where nvl(h.dr, 0) = 0 and nvl(b.dr, 0) = 0 and nvl(r.dr, 0) = 0 ";
			if (PuPubVO.getString_TrimZeroLenAsNull(sql) != null)
				str = str + " and " + sql;
			String s =ZbPubTool.getParam();
			if(s!=null &&!"".equals(s))
				str =str+" and h.cbiddingid in(select h.cbiddingid from zb_bidding_h h where  nvl(h.dr, 0) = 0 and h.reserve1 = '"+s+"')";
			str = str + "  group by h.pk_deptdoc, r.ccustmanid,r.ccustbasid ";
		} else if (ZbPubConst.ZB_TYPE_DETAIL.equalsIgnoreCase(type)) {
			str = "select h.pk_deptdoc,r.ccustmanid,r.ccustbasid,b.cinvbasid,b.cinvmanid,"
					+ " b.cunitid,b.nzbprice,r.nzbnum,r.nzbmny,c.invcode from zb_bidevaluation_h h "
					+ " join zb_evaluation_b b on h.cevaluationid = b.cevaluationid join zb_slvendor r "
					+ " on r.cevaluationbid =b.cevaluationbid join bd_invbasdoc c on c.pk_invbasdoc = b.cinvbasid"
					+ " where isnull(h.dr,0)= 0 and isnull(b.dr,0)= 0 and isnull(r.dr,0)= 0 and isnull(c.dr,0)=0 ";
			if (PuPubVO.getString_TrimZeroLenAsNull(sql) != null)
				str = str + " and " + sql;
			String s =ZbPubTool.getParam();
			if(s!=null &&!"".equals(s))
				str =str+" and h.cbiddingid in(select h.cbiddingid from zb_bidding_h h where  nvl(h.dr, 0) = 0 and h.reserve1 = '"+s+"')";
			str = str + " order by h.pk_deptdoc,r.ccustmanid,c.invcode";
		}

		return str;
	}

	// 中标明细查询
	public ZbDetailVO[] queryDatas1(String sql) throws BusinessException {
		String str = "select b.cinvbasid,b.cinvmanid,b.cunitid,b.nzbprice,b.nzbnum,zh.izbtype from "
				+ "zb_evaluation_b b join zb_bidevaluation_h h on h.cevaluationid = b.cevaluationid "
				+ "join zb_bidding_h zh on zh.cbiddingid = h.cbiddingid join bd_invbasdoc c on c.pk_invbasdoc = b.cinvbasid  where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 "
				+ " and isnull(zh.dr,0)=0 and isnull(c.dr,0)=0";
		if (PuPubVO.getString_TrimZeroLenAsNull(sql) != null)
			str = str + " and " + sql;
		String s =ZbPubTool.getParam();
		if(s!=null &&!"".equals(s))
			str =str+" and zh.reserve1 = '" +s+"'";
		str = str + " order by c.invcode";
		List ldata = (List) getBaseDao().executeQuery(str,
				new BeanListProcessor(ZbDetailVO.class));

		if (ldata == null || ldata.size() == 0) {
			return null;
		}
		ZbDetailVO[] datas = (ZbDetailVO[]) ldata.toArray(new ZbDetailVO[0]);
		return datas;
	}

	// 分标段供应商查询
	public ZbSituaVO[] queryDatas2(String sql) throws BusinessException {
		String str = " select a.cbiddingid,a.ccustmanid,a.ccustbasid,a.nprezbnmny,a.pk_deptdoc, a.izbtype,coalesce(s.nquotatpoints, 0)+coalesce(s.nqualipoints,0) ntotalgrad,a.vbillno,a.dbilldate "
				+ " from zb_biddingsuppliers s,(select zh.cbiddingid,r.ccustmanid,r.ccustbasid,sum(coalesce(r.nzbmny, 0)) nprezbnmny,h.pk_deptdoc, zh.izbtype,zh.vbillno,h.dbilldate"
				+ " from zb_evaluation_b b join zb_bidevaluation_h h on h.cevaluationid = b.cevaluationid join zb_bidding_h zh on zh.cbiddingid = h.cbiddingid"
				+ " join zb_slvendor r on r.cevaluationbid = b.cevaluationbid"
				+ " where nvl(h.dr, 0) = 0 and nvl(b.dr, 0) = 0 and nvl(r.dr, 0) = 0  and nvl(zh.dr, 0) = 0  group by zh.cbiddingid, r.ccustmanid,r.ccustbasid, h.pk_deptdoc, zh.izbtype,zh.vbillno,h.dbilldate"
				+ " ) a where s.cbiddingid=a.cbiddingid and s.ccustmanid=a.ccustmanid";
		if (PuPubVO.getString_TrimZeroLenAsNull(sql) != null)
			str = str + " and " + sql;
		String s =ZbPubTool.getParam();
		if(s!=null &&!"".equals(s))
			str =str+" and a.cbiddingid in(select h.cbiddingid from zb_bidding_h h where  nvl(h.dr, 0) = 0 and h.reserve1 = '"+s+"')";
		str = str + " order by a.vbillno,ntotalgrad";

		List ldata = (List) getBaseDao().executeQuery(str,
				new BeanListProcessor(ZbSituaVO.class));

		if (ldata == null || ldata.size() == 0) {
			return null;
		}
		ZbSituaVO[] datas = (ZbSituaVO[]) ldata.toArray(new ZbSituaVO[0]);
		ZbSituaVO.sortZbSituaVO(datas);
		return datas;
	}
}

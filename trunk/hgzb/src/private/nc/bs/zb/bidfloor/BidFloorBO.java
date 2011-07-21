package nc.bs.zb.bidfloor;

import java.util.HashMap;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.trade.business.HYPubBO;
import nc.bs.zb.bidding.make.MakeBiddingBO;
import nc.bs.zb.pub.ZbBsPubTool;
import nc.ui.zb.bidfloor.BidFloorBodyVO;
import nc.ui.zb.bidfloor.BidFloorHeadVO;
import nc.ui.zb.bidfloor.ViewDetailVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.zb.pub.ZbPubConst;

public class BidFloorBO {

	private BaseDAO dao = null;

	public BaseDAO getBaseDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	
	public void checkOnUnApprove(HYBillVO billvo) throws ValidationException,BusinessException{
		if(billvo == null)
			throw new ValidationException("传入参数为空");
		BidFloorHeadVO head = (BidFloorHeadVO)billvo.getParentVO();
		MakeBiddingBO mbo = new MakeBiddingBO();
		int ista = mbo.getBiddingBusiState(head.getCbiddingid());
		if(ista==ZbPubConst.BIDDING_BUSINESS_STATUE_SUBMIT||ista == ZbPubConst.BIDDING_BUSINESS_STATUE_INIT){
			mbo.updateBiddingBusiState(head.getCbiddingid(), ZbPubConst.BIDDING_BUSINESS_STATUE_INIT);
			mbo.updateBiddingMarkPrice(head.getCbiddingid());
			return;
		}else if(ista == ZbPubConst.BIDDING_BUSINESS_STATUE_BILL)
			throw new ValidationException("标书已进入评标流程");
		else if(ista==ZbPubConst.BIDDING_BUSINESS_STATUE_RESULT)
			throw new ValidationException("标书已中标");
		else if(ista == ZbPubConst.BIDDING_BUSINESS_STATUE_CLOSE)
			throw new ValidationException("标书招标已结束");
	}

	public ViewDetailVO[] loadDatas(String cbiddingid, String cinvmanid)
			throws DAOException {
		String sql = " select e.cvendorid,c.pk_cubasdoc,e.nprice,e.ccircalnoid,bas.custcode,s.vname from zb_submitprice e " 
				+"join bd_cumandoc c on c.pk_cumandoc=e.cvendorid  join bd_cubasdoc bas on c.pk_cubasdoc =bas.pk_cubasdoc " +
			     " join zb_biddingtimes s on s.cbiddingtimesid=e.ccircalnoid where "
				+" e.cbiddingid ='"+ cbiddingid + "' and e.cinvmanid='"+ cinvmanid+ "'"+ " and isnull(e.dr,0)=0 and isnull(c.dr,0)=0 ";
		//取临时供应商的标底价
		String tempsql  ="select e.cvendorid,'aa',e.nprice,e.ccircalnoid,g.vbillno,s.vname from zb_submitprice e "
				+ "join bd_cubasdochg g on g.ccubasdochgid=e.cvendorid join zb_biddingtimes s on s.cbiddingtimesid=e.ccircalnoid where "
				+ " e.cbiddingid ='"+ cbiddingid + "' and e.cinvmanid='"+ cinvmanid+ "'"+ " and isnull(e.dr,0)=0 and isnull(g.dr,0)=0";

		List ldata = (List) getBaseDao().executeQuery(sql,ZbBsPubTool.ARRAYLISTPROCESSOR);

		List temp  =(List)getBaseDao().executeQuery(tempsql,ZbBsPubTool.ARRAYLISTPROCESSOR);
		if (ldata == null || ldata.size() == 0) {
			if (temp == null || temp.size() == 0)
				return null;
		}
		ldata.addAll(temp);
		int size = ldata.size();

		ViewDetailVO[] datas = new ViewDetailVO[size];

		for (int i = 0; i < size; i++) {
			Object[] os = (Object[]) ldata.get(i);
			ViewDetailVO vo = new ViewDetailVO();
			vo.setCcustmanid(PuPubVO.getString_TrimZeroLenAsNull(os[0]));
			String  custbasid=PuPubVO.getString_TrimZeroLenAsNull(os[1]);
			if("aa".equalsIgnoreCase(PuPubVO.getString_TrimZeroLenAsNull(os[1])))
				custbasid=null;
			vo.setCcustbasid(custbasid);
			vo.setNprice(PuPubVO.getUFDouble_NullAsZero(os[2]));
			vo.setCcircalnoid(PuPubVO.getString_TrimZeroLenAsNull(os[3]));
			vo.setCustcode(PuPubVO.getString_TrimZeroLenAsNull(os[4]));
			vo.setVname(PuPubVO.getString_TrimZeroLenAsNull(os[5]));
			datas[i] = vo;
		}
		VOUtil.ascSort(datas, ZbPubConst.VIEW_SORT_FIELDNAMES);
		return datas;

	}

	public SuperVO[] loadBodyData(String key, String name) throws Exception {

		SuperVO[] supervos = new HYPubBO().queryByCondition(Class
				.forName(name), "cbiddingid='" + key + "' and isnull(dr,0)=0");// 查出标段
		if (supervos == null && supervos.length == 0)
			return null;
		int len = supervos.length;
		HashMap map = new HashMap();
		for(SuperVO vo:supervos){
			String sql = " select  min(nprice) from  (select e.nprice,e.cinvmanid from zb_submitprice e join zb_biddingtimes s "
				+ " on e.ccircalnoid = s.cbiddingtimesid where "
				+ " s.crowno = (select min(s.crowno) from zb_bidding_b b join zb_biddingtimes s on b.cbiddingid = s.cbiddingid"
				+ " where b.cbiddingid = '"+ key+ "'and isnull(b.dr, 0) = 0 and isnull(s.dr, 0) = 0 and b.cinvmanid = '"+vo.getAttributeValue("cinvmanid")+"') and e.cbiddingid ='"
				+ key+ "'" + " and isnull(e.dr,0)=0 and isnull(s.dr,0)=0 and e.cinvmanid = '"+vo.getAttributeValue("cinvmanid")+"')";// 查出一次报价的最小值
			Object ldata =  getBaseDao().executeQuery(sql,ZbBsPubTool.COLUMNPROCESSOR);
			map.put(vo.getAttributeValue("cinvmanid"),ldata);
		}
		BidFloorBodyVO[] bodyvos = dealVO(supervos, map);
		return bodyvos;
	}

	private BidFloorBodyVO[] dealVO(SuperVO[] supervos, HashMap map) {
		int length = supervos.length;
		BidFloorBodyVO[] vos = new BidFloorBodyVO[length];
		for (int i = 0; i < length; i++) {
			BidFloorBodyVO vo = new BidFloorBodyVO();
			vo.setCinvbasid(PuPubVO.getString_TrimZeroLenAsNull(supervos[i]
					.getAttributeValue("cinvbasid")));
			vo.setCinvclid(PuPubVO.getString_TrimZeroLenAsNull(supervos[i]
					.getAttributeValue("cinvclid")));
			vo.setCinvmanid(PuPubVO.getString_TrimZeroLenAsNull(supervos[i]
					.getAttributeValue("cinvmanid")));
			vo.setCrowno(PuPubVO.getString_TrimZeroLenAsNull(supervos[i]
					.getAttributeValue("crowno")));
			vo.setCunitid(PuPubVO.getString_TrimZeroLenAsNull(supervos[i]
					.getAttributeValue("cunitid")));
			vo.setNaverageprice(PuPubVO.getUFDouble_NullAsZero(supervos[i]
					.getAttributeValue("naverageprice")));
			vo.setNmarketprice(PuPubVO.getUFDouble_NullAsZero(supervos[i]
					.getAttributeValue("nmarketprice")));
			vo.setNmarkprice(PuPubVO.getUFDouble_NullAsZero(supervos[i]
					.getAttributeValue("nmarkprice")));
			vo.setNminprice(PuPubVO.getUFDouble_NullAsZero(supervos[i]
					.getAttributeValue("nminprice")));
			vo.setNplanprice(PuPubVO.getUFDouble_NullAsZero(supervos[i]
					.getAttributeValue("nplanprice")));
			vo.setNzbnum(PuPubVO.getUFDouble_NullAsZero(supervos[i]
					.getAttributeValue("nzbnum")));
			vo.setCsourcebillbid(PuPubVO
					.getString_TrimZeroLenAsNull(supervos[i]
							.getAttributeValue("cbiddingbid")));
			vo.setCsourcebillhid(PuPubVO
					.getString_TrimZeroLenAsNull(supervos[i]
							.getAttributeValue("cbiddingid")));
			vo.setCsourcetype("ZB01");
			vo.setCupsourcebillid(PuPubVO
					.getString_TrimZeroLenAsNull(supervos[i]
							.getAttributeValue("cbiddingid")));
			vo.setCupsourcebillrowid(PuPubVO
					.getString_TrimZeroLenAsNull(supervos[i]
							.getAttributeValue("cbiddingbid")));
			vo.setCupsourcebilltype("ZB01");
			vos[i] = vo;
			vos[i].setNminprice(PuPubVO.getUFDouble_NullAsZero(map.get(vos[i]
					.getAttributeValue("cinvmanid"))));
		}
		return vos;
	}

	public void reWriteBidding(BidFloorBodyVO[] vos) throws DAOException {
		if (vos == null || vos.length == 0)
			return;

		int len = vos.length;
		for (int i = 0; i < len; i++) {
			String cbiddingbid = PuPubVO.getString_TrimZeroLenAsNull(vos[i]
					.getCsourcebillbid());
			UFDouble nmarkprice = PuPubVO.getUFDouble_NullAsZero(vos[i]
					.getNmarkprice());
			String sql = "update zb_bidding_b set nmarkprice = '" + nmarkprice
					+ "' where cbiddingbid = '" + cbiddingbid + "'";
			getBaseDao().executeUpdate(sql);
		}
	}

}

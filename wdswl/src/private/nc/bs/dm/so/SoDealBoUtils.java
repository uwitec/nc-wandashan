package nc.bs.dm.so;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.bs.wds.ic.stock.StockInvOnHandBO;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.dm.so.deal2.StoreInvNumVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * 发运计划安排 辅助类
 * 
 * @author Administrator
 * 
 */
public class SoDealBoUtils {

	private BaseDAO dao = null;

	private BaseDAO getDao() {
		if (dao == null)
			dao = new BaseDAO();
		return dao;
	}

	private StockInvOnHandBO stockbo = null;

	private StockInvOnHandBO getStockBO() {
		if (stockbo == null) {
			stockbo = new StockInvOnHandBO(getDao());
		}
		return stockbo;
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目
	 * @时间：2011-11-28下午08:29:20
	 * @param fisdate
	 *            :是否大日期
	 * @param pk_corp
	 * @param pk_stordoc:出库仓库
	 * @param bodys
	 * @return
	 * @throws BusinessException
	 */
	protected Map<String, StoreInvNumVO> initInvNumInfor(boolean fisdate,
			String pk_corp, String pk_stordoc, List<SoDealVO> bodys)
			throws BusinessException {
		Map<String, StoreInvNumVO> invNumInfor = new HashMap<String, StoreInvNumVO>();
		if (bodys == null || bodys.size() == 0) {
			return invNumInfor;
		}
		Set<String> cinvids = new HashSet<String>();// 本次安排的所有存货id
		StoreInvNumVO tmpNumVO = null;
		String key = null;
		for (SoDealVO body : bodys) {
			key = WdsWlPubTool.getString_NullAsTrimZeroLen(body
					.getCinvbasdocid());
			cinvids.add(body.getCinvbasdocid());
			if (invNumInfor.containsKey(key)) {
				tmpNumVO = invNumInfor.get(key);
			} else {
				tmpNumVO = new StoreInvNumVO();
				tmpNumVO.setPk_corp(pk_corp);
				tmpNumVO.setCstoreid(pk_stordoc);
				tmpNumVO.setCinvbasid(body.getCinvbasdocid());
				tmpNumVO.setCinvmanid(body.getCinventoryid());
				String strWhere = null;
				// 查询库存量
				if (fisdate) {
					strWhere = " ss_pk='" + WdsWlPubConst.WDS_STORSTATE_PK
							+ "'";
				} else {
					strWhere = " ss_pk in('"
							+ WdsWlPubConst.WDS_STORSTATE_PK_hg + "')";
					// strWhere= " ss_pk
					// in('"+WdsWlPubConst.WDS_STORSTATE_PK_hg+"','"+WdsWlPubConst.WDS_STORSTATE_PK_dj+"')";
				}
				UFDouble[] stocknums = getStockBO().getInvStockNum(pk_corp,
						tmpNumVO.getCstoreid(), null, tmpNumVO.getCinvbasid(),
						null, null, strWhere);
				if (stocknums == null || stocknums.length == 0) {
					String reason = " 存货:"
							+ WdsWlPubTool.getInvCodeByInvid(tmpNumVO
									.getCinvbasid()) + " 无库存量";
					if (fisdate) {
						reason = "大日期状态：" + reason;
					} else {
						reason = "合格，待检状态：" + reason;
					}
					Logger.info(reason);
					throw new BusinessException(reason);
				}
				tmpNumVO.setNstocknum(stocknums[0]);
				tmpNumVO.setNstockassnum(stocknums[1]);
			}
			// 本次需要安排数量
			tmpNumVO.setNplannum(PuPubVO.getUFDouble_NullAsZero(
					tmpNumVO.getNplannum()).add(
					PuPubVO.getUFDouble_NullAsZero(body.getNnum())));
			tmpNumVO.setNplanassnum(PuPubVO.getUFDouble_NullAsZero(
					tmpNumVO.getNplanassnum()).add(
					PuPubVO.getUFDouble_NullAsZero(body.getNassnum())));
			invNumInfor.put(key, tmpNumVO);
		}
		if (invNumInfor.size() == 0) {
			Logger.info("本次待安排存货库存均为空，无法安排，退出");
			return invNumInfor;
		}
		// 2.获得销售运单占有量和发运订单占有量
		Logger.info("获取存货已安排未出库量...");
		TempTableUtil tt = new TempTableUtil();
		String[] invs = cinvids.toArray(new String[0]);
		String strWhereSO = null;
		if (fisdate) {
			strWhereSO = " coalesce(b.bisdate,'N')='Y' and b.pk_invbasdoc in "
					+ tt.getSubSql(invs);
		} else {
			strWhereSO = " coalesce(b.bisdate,'N')='N' and b.pk_invbasdoc in "
					+ tt.getSubSql(invs);
		}
		Map<String, UFDouble[]> invNumInfor1 = getStockBO()
				.getSoOrderNdealNumInfor(pk_corp, pk_stordoc, strWhereSO);
		if (invNumInfor1 == null || invNumInfor1.size() == 0) {
			Logger.info("本次安排的存货不存在已安排未出库量");
			if (invNumInfor1 == null)
				invNumInfor1 = new HashMap<String, UFDouble[]>();
		}
		String strWherePlan = null;
		if (fisdate) {
			strWherePlan = "  coalesce(b.bisdate,'N')='Y' and b.pk_invbasdoc in "
					+ tt.getSubSql(invs);
		} else {
			strWherePlan = "  coalesce(b.bisdate,'N')='N' and b.pk_invbasdoc in "
					+ tt.getSubSql(invs);
		}
		Map<String, UFDouble[]> invNumInfor2 = getStockBO()
				.getPlanOrderNdealNumInfor(pk_corp, pk_stordoc, strWherePlan);
		if (invNumInfor2 == null || invNumInfor2.size() == 0) {
			Logger.info("本次安排的存货不存在已安排未出库量");
			if (invNumInfor2 == null)
				invNumInfor2 = new HashMap<String, UFDouble[]>();
		}
		for (String key2 : invNumInfor.keySet()) {
			tmpNumVO = invNumInfor.get(key2);
			UFDouble[] stocknums = invNumInfor1.get(key2);
			UFDouble[] stocknum2 = invNumInfor2.get(key2);
			if (tmpNumVO == null)
				continue;
			// 1.已安排运单占用量
			UFDouble nplannum = PuPubVO.getUFDouble_NullAsZero(null);
			UFDouble nplanassnum = PuPubVO.getUFDouble_NullAsZero(null);
			if (stocknums != null) {
				nplannum = nplannum.add(PuPubVO
						.getUFDouble_NullAsZero(stocknums[0]));
				nplanassnum = nplanassnum.add(PuPubVO
						.getUFDouble_NullAsZero(stocknums[1]));

			}
			if (stocknum2 != null) {
				nplannum = nplannum.add(PuPubVO
						.getUFDouble_NullAsZero(stocknum2[0]));
				nplanassnum = nplanassnum.add(PuPubVO
						.getUFDouble_NullAsZero(stocknum2[1]));

			}
			tmpNumVO.setNdealnum(nplannum);
			tmpNumVO.setNdealassnum(nplanassnum);
			// 2. 当前可用量=库存量-已经安排的运单占用量
			tmpNumVO.setNnum(tmpNumVO.getNstocknum()
					.sub(tmpNumVO.getNdealnum()));
			tmpNumVO.setNassnum(tmpNumVO.getNstockassnum().sub(
					tmpNumVO.getNdealassnum()));
			// 3. 如果可用量 > 本次安排量 ，标记为可安排
			if (tmpNumVO.getNassnum().doubleValue() >= tmpNumVO
					.getNplanassnum().doubleValue()) {
				tmpNumVO.setBisok(UFBoolean.TRUE);
			} else {
				tmpNumVO.setBisok(UFBoolean.FALSE);
				String reason = " 存货"
						+ WdsWlPubTool.getInvCodeByInvid(tmpNumVO
								.getCinvbasid()) + " 当前存量："
						+ tmpNumVO.getNstockassnum() + " 已安排未出库量："
						+ tmpNumVO.getNdealassnum() + " 本次可用量："
						+ tmpNumVO.getNassnum() + " 本次待安排总量："
						+ tmpNumVO.getNplanassnum();
				if (fisdate) {
					reason = "大日期状态" + reason;
				} else {
					reason = "合格，待检状态" + reason;
				}
				Logger.info(reason);
				throw new BusinessException(reason);
			}

		}
		return invNumInfor;
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目：发货站库存量和安排后库存量处理
	 * @时间：2011-11-4下午10:26:39
	 * @param pk_storedoc:发货仓库
	 * @param datas
	 * @throws BusinessException
	 */
	void arrangStornumout(String pk_corp, String pk_storedoc, SoDealVO[] datas)
			throws BusinessException {
		if (datas == null || datas.length == 0) {
			return;
		}
		if (pk_corp == null || "".equalsIgnoreCase(pk_corp)) {
			pk_corp = SQLHelper.getCorpPk();
		}
		String pk_outwhous = PuPubVO.getString_TrimZeroLenAsNull(pk_storedoc);
		if (pk_outwhous == null) {
			throw new BusinessException("发货仓库不能为空，请检查当前操作人员绑定仓库");
		}
		// 1.一次查询当前仓库下所有存货的库存量，逐个比较分配
		ArrayList<String> pk_invbasdocs = new ArrayList<String>();
		for (int j = 0; j < datas.length; j++) {
			String pk_invbasdoc = PuPubVO.getString_TrimZeroLenAsNull(datas[j]
					.getAttributeValue("cinvbasdocid"));
			if (pk_invbasdoc == null || pk_invbasdocs.contains(pk_invbasdoc)) {// 如果存货主键为空，或者已经包含，则继续
				continue;
			}
			pk_invbasdocs.add(pk_invbasdoc);
		}
		if (pk_invbasdocs.size() == 0) {
			return;
		}
		// 2 库存量查询
		// 2.1查库存量：区分正常品和大日期
		Map<String, UFDouble[]> invStornum_drq = getStockNum(true, pk_corp,
				pk_outwhous, pk_invbasdocs);// 大日期库存量
		Map<String, UFDouble[]> invStornum_hg = getStockNum(false, pk_corp,
				pk_outwhous, pk_invbasdocs);// 合格品库存量
		// 3 查询销售运单占有量：区分正常品和大日期
		Map<String, UFDouble[]> soordernum_drq = getSoOrderNdealNumInfor(true,
				pk_corp, pk_outwhous, pk_invbasdocs);
		Map<String, UFDouble[]> soordernum_hg = getSoOrderNdealNumInfor(false,
				pk_corp, pk_outwhous, pk_invbasdocs);
		// 4 查询发运订单占有量：区分正常品和大日期
		Map<String, UFDouble[]> sendordernum_drq = getPlanOrderNdealNumInfor(
				true, pk_corp, pk_outwhous, pk_invbasdocs);
		Map<String, UFDouble[]> sendordernum_hg = getPlanOrderNdealNumInfor(
				false, pk_corp, pk_outwhous, pk_invbasdocs);
		dealNum(true, invStornum_drq, soordernum_drq, sendordernum_drq, datas);
		dealNum(false, invStornum_hg, soordernum_hg, sendordernum_hg, datas);
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目
	 * @时间：2012-1-6下午08:51:05
	 * @param fisdate
	 * @param invStornum:库存量
	 * @param invNumInfor1：销售运单占有量
	 * @param invNumInfor2:发运订单占有量
	 * @param datas
	 */
	protected void dealNum(boolean fisdate, Map<String, UFDouble[]> invStornum,
			Map<String, UFDouble[]> invNumInfor1,
			Map<String, UFDouble[]> invNumInfor2, SoDealVO[] datas) {
		if(datas == null || datas.length ==0){
			return;
		}
		if (invStornum == null || invStornum.size() ==0) {//无库存量，则不进行分配
			return;
		}
		// 3.根据存货分配 库存数量和 可用量
		for (String key : invStornum.keySet()) {
			UFDouble stornum = PuPubVO.getUFDouble_NullAsZero(0);// 发货站库存主数量
			UFDouble nuesfulnum = PuPubVO.getUFDouble_NullAsZero(0);// 可用量==库存量-销售运单占有量-发运订单占有量
			
			UFDouble[] stornums = invStornum.get(key);
			if (stornums !=null && stornums.length > 0) {
				stornum = PuPubVO.getUFDouble_NullAsZero(stornums[0]);
			}
			//销售运单占有量
			UFDouble nsoordernum = PuPubVO.getUFDouble_NullAsZero(0);
			if (invNumInfor1 != null && invNumInfor1.size() > 0) {
				UFDouble[] nsoordernums = invNumInfor1.get(key);
				if (nsoordernums != null && nsoordernums.length > 0) {
					nsoordernum = PuPubVO
							.getUFDouble_NullAsZero(nsoordernums[0]);
				}
			}
			//发运订单占有量
			UFDouble nsendordernum = PuPubVO.getUFDouble_NullAsZero(0);
			if (invNumInfor2 != null && invNumInfor2.size() > 0) {
				UFDouble[] nsendordernums = invNumInfor2.get(key);
				if (nsendordernums !=null && nsendordernums.length > 0) {
					nsendordernum = PuPubVO
							.getUFDouble_NullAsZero(nsendordernums[0]);
				}
				nuesfulnum = stornum.sub(nsoordernum).sub(nsendordernum);
			}
			for (int j = 0; j < datas.length; j++) {
				String pk_invbasdoc = PuPubVO
						.getString_TrimZeroLenAsNull(datas[j]
								.getAttributeValue("cinvbasdocid"));
				if (pk_invbasdoc == null) {// 如果存货主键为空则继续
					continue;
				}
				UFDouble ndealnum = PuPubVO.getUFDouble_NullAsZero(datas[j].getNnumber()).sub(PuPubVO.getUFDouble_NullAsZero(datas[j].getNtaldcnum()));
				if (pk_invbasdoc.equalsIgnoreCase(key)) {
					if (fisdate) {
						datas[j].setNdrqstorenumout(stornum);
						datas[j].setNdrqarrstorenumout(stornum.sub(ndealnum));
						datas[j].setNdrqusefulnumout(nuesfulnum);
						datas[j].setNdrqarrusefulnumout(nuesfulnum
								.sub(ndealnum));
					} else {
						datas[j].setNstorenumout(stornum);
						datas[j].setNarrstorenumout(stornum.sub(ndealnum));
						datas[j].setNusefulnumout(nuesfulnum);
						datas[j].setNarrusefulnumout(nuesfulnum.sub(ndealnum));

					}
				}
			}
		}

	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目
	 * @时间：2012-1-6下午08:09:28
	 * @param fisdate:是否大日期
	 * @param pk_corp
	 * @param pk_outwhous
	 * @param pk_invbasdocs
	 * @return
	 * @throws BusinessException
	 */
	protected Map<String, UFDouble[]> getStockNum(boolean fisdate,
			String pk_corp, String pk_outwhous, ArrayList<String> pk_invbasdocs)
			throws BusinessException {
		String strWhere = null;
		if (fisdate) {
			strWhere = " ss_pk='" + WdsWlPubConst.WDS_STORSTATE_PK + "'";// ss_pk
																			// 存货状态id

		} else {
			strWhere = " ss_pk in('" + WdsWlPubConst.WDS_STORSTATE_PK_hg + "')";
		}
		return getStockBO().getStockNum(pk_corp, pk_outwhous, null,
				pk_invbasdocs, null, null, strWhere);
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目
	 * @时间：2012-1-6下午08:13:13
	 * @param fisdate:是否大日期
	 * @param pk_corp
	 * @param pk_outwhous
	 * @param pk_invbasdocs
	 * @return
	 * @throws BusinessException
	 */
	protected Map<String, UFDouble[]> getSoOrderNdealNumInfor(boolean fisdate,
			String pk_corp, String pk_outwhous, ArrayList<String> pk_invbasdocs)
			throws BusinessException {
		String strWhere = null;
		TempTableUtil tt = new TempTableUtil();
		if (fisdate) {
			strWhere = "  coalesce(b.bisdate,'N')='Y' and b.pk_invbasdoc in "
					+ tt.getSubSql(pk_invbasdocs);
		} else {
			strWhere = "  coalesce(b.bisdate,'N')='N' and b.pk_invbasdoc in "
					+ tt.getSubSql(pk_invbasdocs);
		}
		Map<String, UFDouble[]> invNumInfor = getStockBO()
				.getSoOrderNdealNumInfor(pk_corp, pk_outwhous, strWhere);

		if (invNumInfor == null || invNumInfor.size() == 0) {
			Logger.info("本次查询的存货发运订单不存在已安排未出库量");
			if (invNumInfor == null)
				invNumInfor = new HashMap<String, UFDouble[]>();
		}
		return invNumInfor;

	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目
	 * @时间：2012-1-6下午08:13:13
	 * @param fisdate:是否大日期
	 * @param pk_corp
	 * @param pk_outwhous
	 * @param pk_invbasdocs
	 * @return
	 * @throws BusinessException
	 */
	protected Map<String, UFDouble[]> getPlanOrderNdealNumInfor(
			boolean fisdate, String pk_corp, String pk_outwhous,
			ArrayList<String> pk_invbasdocs) throws BusinessException {
		TempTableUtil tt = new TempTableUtil();
		String strWherePlan = null;
		if (fisdate) {
			strWherePlan = "  coalesce(b.bisdate,'N')='Y' and b.pk_invbasdoc in "
					+ tt.getSubSql(pk_invbasdocs);
		} else {
			strWherePlan = "  coalesce(b.bisdate,'N')='N' and b.pk_invbasdoc in "
					+ tt.getSubSql(pk_invbasdocs);
		}
		Map<String, UFDouble[]> invNumInfor = getStockBO()
				.getPlanOrderNdealNumInfor(pk_corp, pk_outwhous, strWherePlan);
		if (invNumInfor == null || invNumInfor.size() == 0) {
			Logger.info("本次查询的存货发运订单不存在已安排未出库量");
			if (invNumInfor == null)
				invNumInfor = new HashMap<String, UFDouble[]>();
		}
		return invNumInfor;
	}
}

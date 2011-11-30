package nc.bs.dm.so;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.bs.wds.ic.stock.StockInvOnHandBO;
import nc.itf.scm.cenpur.service.TempTableUtil;
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
	 * @param fisdate :是否大日期
	 * @param pk_corp
	 * @param pk_stordoc:出库仓库
	 * @param bodys
	 * @return
	 * @throws BusinessException
	 */
	protected Map<String, StoreInvNumVO> initInvNumInfor(boolean fisdate ,String pk_corp,String pk_stordoc,List<SoDealVO> bodys) throws BusinessException{
		Map<String, StoreInvNumVO> invNumInfor=  new HashMap<String, StoreInvNumVO>();
		if(bodys == null || bodys.size()==0){
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
				String strWhere= null;
				//查询库存量
				if(fisdate){
					strWhere= "ss_pk in('"+WdsWlPubConst.WDS_STORSTATE_PK_hg+"','"+WdsWlPubConst.WDS_STORSTATE_PK_dj+"')";
				}else{
					strWhere= " ss_pk='"+WdsWlPubConst.WDS_STORSTATE_PK+"'";
				}
				UFDouble[]stocknums = getStockBO().getInvStockNum(pk_corp,
						tmpNumVO.getCstoreid(), null,
						tmpNumVO.getCinvbasid(), null, null,strWhere);
				if (stocknums == null || stocknums.length == 0){
					String reason=" 存货"
						+ WdsWlPubTool.getInvCodeByInvid(tmpNumVO.getCinvbasid())
						+ " 无库存量";
					if(fisdate){
						reason="大日期："+reason;
					}else{
						reason="合格，待检："+reason;
					}
					Logger.info(reason);
					throw new BusinessException(reason);
				}
				tmpNumVO.setNstocknum(stocknums[0]);
				tmpNumVO.setNstockassnum(stocknums[1]);
			}
			//本次需要安排数量
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
		//2.获得销售运单占有量和发运订单占有量
		Logger.info("获取存货已安排未出库量...");
		TempTableUtil tt = new TempTableUtil();
		String[] invs= cinvids.toArray(new String[0]);
		String  strWhereSO= null;
		if(fisdate){
			strWhereSO= " h.pk_outwhouse='"+pk_stordoc+"' and coalesce(b.bisdate,'N')='Y' and b.pk_invbasdoc in "+tt.getSubSql(invs);
		}else{
			strWhereSO= " h.pk_outwhouse='"+pk_stordoc+"' and coalesce(b.bisdate,'N')='N' and b.pk_invbasdoc in "+tt.getSubSql(invs);
		}
		Map<String, UFDouble[]> invNumInfor1 = getStockBO().getSoOrderNdealNumInfor(pk_corp, strWhereSO);
		if (invNumInfor1 == null || invNumInfor1.size() == 0) {
			Logger.info("本次安排的存货不存在已安排未出库量");
			if (invNumInfor1 == null)
				invNumInfor1 = new HashMap<String, UFDouble[]>();
		}
		String  strWherePlan= null;
		if(fisdate){
			strWherePlan= " h.pk_outwhouse='"+pk_stordoc+"' and coalesce(b.bisdate,'N')='Y' and b.pk_invbasdoc in "+tt.getSubSql(invs);
		}else{
			strWherePlan= " h.pk_outwhouse='"+pk_stordoc+"' and coalesce(b.bisdate,'N')='N' and b.pk_invbasdoc in "+tt.getSubSql(invs);
		}
		Map<String, UFDouble[]> invNumInfor2 = getStockBO().getPlanOrderNdealNumInfor(pk_corp, strWherePlan);
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
			// 已安排运单占用量
			UFDouble nplannum= PuPubVO.getUFDouble_NullAsZero(null);
			UFDouble nplanassnum= PuPubVO.getUFDouble_NullAsZero(null);
			if(stocknums != null){
				nplannum= nplannum.add(PuPubVO.getUFDouble_NullAsZero(stocknums[0]));
				nplanassnum= nplanassnum.add(PuPubVO.getUFDouble_NullAsZero(stocknums[1]));

			}
			if(stocknum2 != null){
				nplannum= nplannum.add(PuPubVO.getUFDouble_NullAsZero(stocknum2[0]));
				nplanassnum= nplanassnum.add(PuPubVO.getUFDouble_NullAsZero(stocknum2[1]));

			}
			tmpNumVO.setNdealnum(stocknums == null ? WdsWlPubTool.DOUBLE_ZERO
					: stocknums[0]);
			tmpNumVO
					.setNdealassnum(stocknums == null ? WdsWlPubTool.DOUBLE_ZERO
							: stocknums[1]);
			tmpNumVO.setNplannum(nplannum);
			tmpNumVO.setNplanassnum(nplanassnum);
			// 当前可用量=库存量-已经安排的运单占用量
			tmpNumVO.setNnum(tmpNumVO.getNstocknum()
					.sub(tmpNumVO.getNdealnum()));
			tmpNumVO.setNassnum(tmpNumVO.getNstockassnum().sub(
					tmpNumVO.getNdealassnum()));
			// 如果可用量 > 本次安排量 ，标记为可安排
			if (tmpNumVO.getNassnum().doubleValue() > tmpNumVO.getNplanassnum()
					.doubleValue()){
				tmpNumVO.setBisok(UFBoolean.TRUE);
			}else{
				tmpNumVO.setBisok(UFBoolean.FALSE);
				String reason=" 存货"
					+ WdsWlPubTool.getInvCodeByInvid(tmpNumVO.getCinvbasid())
					+ " 当前存量：" + tmpNumVO.getNstockassnum() + " 已安排未出库量："
					+ tmpNumVO.getNdealassnum() + " 本次可用量："
					+ tmpNumVO.getNassnum() + " 本次待安排总量："
					+ tmpNumVO.getNplanassnum();
				if(fisdate){
					reason="大日期"+reason;
				}else{
					reason="合格，待检"+reason;
				}
				Logger.info(reason);
				throw new BusinessException(reason);
			}
				
		}
		return invNumInfor;
			
	}
	

}

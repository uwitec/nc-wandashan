package nc.bs.dm.so.deal2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.dm.so.SoDealBO;
import nc.bs.logging.Logger;
import nc.bs.wds.ic.stock.StockInvOnHandBO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.dm.so.deal2.SoDealBillVO;
import nc.vo.dm.so.deal2.SoDealHeaderVo;
import nc.vo.dm.so.deal2.StoreInvNumVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * 
 * @author zhf 销售计划安排算法类
 * 
 * 输入 待安排的数据 进行安排处理 返回需要手工安排的数据
 * 
 */
public class SoDealCol {

	private SoDealBillVO[] bills = null;

	private List lpara = null;

	public SoDealCol() {
		super();
	}

	public SoDealCol(SoDealBillVO[] bills, List lpara) {
		super();
		this.bills = bills;
		this.lpara = lpara;
	}

	public void setData(SoDealBillVO[] bills, List lpara) {
		this.bills = bills;
		this.lpara = lpara;
	}

	private StockInvOnHandBO stockbo = null;
	private BaseDAO dao = null;

	private BaseDAO getDao() {
		if (dao == null)
			dao = new BaseDAO();
		return dao;
	}

	private StockInvOnHandBO getStockBO() {
		if (stockbo == null) {
			stockbo = new StockInvOnHandBO(getDao());
		}
		return stockbo;
	}

	private Map<String, UFDouble> custMinNumInfor = null;

	public void clearCustMinNumInfor() {
		if (custMinNumInfor != null)
			custMinNumInfor.clear();
	}

	/**
	 * 
	 * @作者：zhf
	 * @说明 完达山物流项目 通过分仓客商绑定获取 分仓客商的最小发货量设置
	 * @时间：2011-7-7下午04:09:28
	 * @param ccustid
	 * @param pk_store
	 * @return
	 * @throws BusinessException
	 */
	public UFDouble getMinSendNumForCust(String ccustid, String pk_store)
			throws BusinessException {

		if (PuPubVO.getString_TrimZeroLenAsNull(ccustid) == null)
			return null;
		String key = WdsWlPubTool.getString_NullAsTrimZeroLen(pk_store)
				+ WdsWlPubTool.getString_NullAsTrimZeroLen(ccustid);
		if (custMinNumInfor == null) {
			custMinNumInfor = new HashMap<String, UFDouble>();
		}
		if (!custMinNumInfor.containsKey(key)) {
			String sql = " select ndef1 from tb_storcubasdoc where isnull(dr,0)=0 and "
					+ " pk_cumandoc = '"
					+ ccustid
					+ "' and pk_stordoc = '"
					+ pk_store + "'";// (select pk_stordoc from bd_stordoc
										// where " +
			// "isnull(dr,0)=0 and )";
			custMinNumInfor.put(key, PuPubVO
					.getUFDouble_NullAsZero(getDao().executeQuery(sql,
							WdsPubResulSetProcesser.COLUMNPROCESSOR)));
		}
		return custMinNumInfor.get(key);
	}

	/**
	 * 
	 * @作者：zhf
	 * @说明：查询 存货库存量
	 * @时间：2011-11-8下午07:59:53
	 * @param invNumInfor
	 * @throws BusinessException
	 */
	private void initInvNumInfor(Map<String, StoreInvNumVO> invNumInfor)
			throws BusinessException {
		// invNumInfor <key,StoreInvNumVO> =
		// <存货基本档案id，StoreInvNumVO（包含当前存货库存量，本次需要安排的数量,已经安排的运单占用量）>
		Set<String> invs = new HashSet<String>();// 本次安排的所有存货id
		SoDealHeaderVo head = null;
		SoDealVO[] bodys = null;
		StoreInvNumVO tmpNumVO = null;
		// 1.获取库存量
		UFDouble[] stocknums = null;
		String pk_corp = SQLHelper.getCorpPk();
		Logger.info("获取库存当前存量...");
		String key = null;
		for (SoDealBillVO bill : bills) {
			head = bill.getHeader();
			bodys = bill.getBodyVos();
			if (bodys == null || bodys.length == 0)
				continue;
			for (SoDealVO body : bodys) {
				key = WdsWlPubTool.getString_NullAsTrimZeroLen(body
						.getCinvbasdocid());
				invs.add(body.getCinvbasdocid());
				if (invNumInfor.containsKey(key)) {
					tmpNumVO = invNumInfor.get(key);
				} else {
					tmpNumVO = new StoreInvNumVO();
					tmpNumVO.setPk_corp(pk_corp);
					tmpNumVO.setCstoreid(head.getCbodywarehouseid());
					tmpNumVO.setCinvbasid(body.getCinvbasdocid());
					tmpNumVO.setCinvmanid(body.getCinventoryid());
					stocknums = getStockBO().getInvStockNum(pk_corp,
							tmpNumVO.getCstoreid(), null,
							tmpNumVO.getCinvbasid(), null, null);
					if (stocknums == null || stocknums.length == 0)
						continue;
					tmpNumVO.setNstocknum(stocknums[0]);
					tmpNumVO.setNstockassnum(stocknums[1]);
				}
				tmpNumVO.setNplannum(PuPubVO.getUFDouble_NullAsZero(
						tmpNumVO.getNplannum()).add(
						PuPubVO.getUFDouble_NullAsZero(body.getNnum())));
				tmpNumVO.setNplanassnum(PuPubVO.getUFDouble_NullAsZero(
						tmpNumVO.getNplanassnum()).add(
						PuPubVO.getUFDouble_NullAsZero(body.getNassnum())));
				invNumInfor.put(key, tmpNumVO);
			}
		}
		if (invNumInfor.size() == 0) {
			Logger.info("本次待安排存货库存均为空，无法安排，退出");
		}
		// 2.获取占用量
		Logger.info("获取存货已安排未出库量...");
		Map<String, UFDouble[]> invNumInfor2 = getStockBO().getNdealNumInfor(
				pk_corp, head.getCbodywarehouseid(),
				invs.toArray(new String[0]), new TempTableUtil());
		if (invNumInfor2 == null || invNumInfor2.size() == 0) {
			Logger.info("本次安排的存货不存在已安排未出库量");
			if (invNumInfor2 == null)
				invNumInfor2 = new HashMap<String, UFDouble[]>();
		}
		Logger.info("本次待安排存货库存状况：");
		for (String key2 : invNumInfor.keySet()) {
			tmpNumVO = invNumInfor.get(key2);
			stocknums = invNumInfor2.get(key2);
			if (tmpNumVO == null)
				continue;
			// 已安排运单占用量
			tmpNumVO.setNdealnum(stocknums == null ? WdsWlPubTool.DOUBLE_ZERO
					: stocknums[0]);
			tmpNumVO
					.setNdealassnum(stocknums == null ? WdsWlPubTool.DOUBLE_ZERO
							: stocknums[1]);
			// 当前可用量=库存量-已经安排的运单占用量
			tmpNumVO.setNnum(tmpNumVO.getNstocknum()
					.sub(tmpNumVO.getNdealnum()));
			tmpNumVO.setNassnum(tmpNumVO.getNstockassnum().sub(
					tmpNumVO.getNdealassnum()));
			// 如果可用量 > 本次安排量 ，标记为可安排
			if (tmpNumVO.getNassnum().doubleValue() > tmpNumVO.getNplanassnum()
					.doubleValue())
				tmpNumVO.setBisok(UFBoolean.TRUE);
			else
				tmpNumVO.setBisok(UFBoolean.FALSE);
			Logger.info(" 存货"
					+ WdsWlPubTool.getInvCodeByInvid(tmpNumVO.getCinvbasid())
					+ " 当前存量：" + tmpNumVO.getNstockassnum() + " 已安排未出库量："
					+ tmpNumVO.getNdealassnum() + " 本次可用量："
					+ tmpNumVO.getNassnum() + " 本次待安排总量："
					+ tmpNumVO.getNplanassnum());
		}
	}

	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 算法描述： 
	 * 1、本次安排的各个品种的库存存量
	 *  2、按品种汇总各个品种的需求量 
	 *  3、将品种分为两类：1、存量满足类* 2、存量不足类 
	 *  4、将客户分为两类：1、可直接安排类 2、存在存量不足的品种的客户类 
	 *  5、可直接安排的客户	直接安排生成运单 
	 *  6、不足的客户根据客户最小发货量过滤库存存量，如果库存存量比客户的最小发货量还小 抛弃该客户
	 *  7、将剩余的客户数据返回输出 等待 用户手工安排
	 * @时间：2011-7-7下午04:58:32 返回不够安排的客户 和 不够安排的品种库存状态
	 */
	public Object col() throws Exception {
		if (bills == null || bills.length == 0)
			return null;
		// 1.获得库存量，可用量信息 
		// invNumInfor里面封装<本次需要安排的存货基本id，StoreInvNumVO(存货当前库存量，已经安排的运单（销售运单+发运订单）占有量，可用量（=库存量-占有量）)>
		Map<String, StoreInvNumVO> invNumInfor = new HashMap<String, StoreInvNumVO>();
		initInvNumInfor(invNumInfor);
		if (invNumInfor.size() == 0)
			throw new BusinessException("所有存货的当前库存量均为空,无法安排,退出");
		// 2. 根据最小发货量过滤库存可用量偏低的存货"
		Logger.info("根据最小发货量过滤库存可用量偏低的存货");
		List<SoDealVO> ldeal = null;
		List<SoDealVO> lnodeal = null;
		List<SoDealBillVO> lcust = new ArrayList<SoDealBillVO>();// 因为可用量不足，不能直接安排发货的客户信息
		List<String> reasons = new ArrayList<String>();// 客户本次不能安排的原因,返回前台做提示用
		for (SoDealBillVO bill : bills) {
			SoDealVO[] bodys = bill.getBodyVos();
			if (bodys == null || bodys.length == 0)
				continue;
			boolean pass = false;//是否跳过该客户
			boolean isdeal = true;//默认 该客户表体 货可用量都满足
			for (SoDealVO body : bodys) {
				StoreInvNumVO tmpNumVO = invNumInfor.get(body.getCinvbasdocid());
				// 2.1存在存货的可用量<=0的 涉及该存货的客户 均不可安排 本次 该客户直接丢弃
				if(tmpNumVO == null){
					String num = "0";
					String reason = "存货"+ WdsWlPubTool.getInvCodeByInvid(body.getCinvbasdocid())
					+ "可用量为:"+num+"客户["
					+ WdsWlPubTool.getCustNameByid(bill.getHeader()
							.getCcustomerid()) + "]本次无法安排;\n";
					Logger.info(reason);
					reasons.add(reason);
					pass = true;
					break;
				}
				double nnum = PuPubVO.getUFDouble_NullAsZero(tmpNumVO.getNnum()).doubleValue();//可用量主数量
				double nassnum = PuPubVO.getUFDouble_NullAsZero(tmpNumVO.getNassnum()).doubleValue();//可用量辅数量
				//2.2存在存货的可用量<=0的 涉及该存货的客户 均不可安排 本次 该客户直接丢弃
				if (nnum<=0) {
					String reason = "存货"+ WdsWlPubTool.getInvCodeByInvid(body.getCinvbasdocid())
					+ "可用量:主数量="+nnum+",辅数量="+nassnum+"\n 客户["
					+ WdsWlPubTool.getCustNameByid(bill.getHeader()
							.getCcustomerid()) + "]本次无法安排;\n";
					Logger.info(reason);
					reasons.add(reason);
					pass = true;
					break;
				}
				// 2.3如果可用量 低于该客户的最小发货量 过滤 掉该客户 
				double nminSendNum = getMinSendNumForCust(bill.getHeader().getCcustomerid(), bill.getHeader().getCbodywarehouseid()).doubleValue();	
				if (nassnum -nassnum<0) {
					String reason = "存货"
						+ WdsWlPubTool.getInvCodeByInvid(body
								.getCinvbasdocid())
						+ "可用量:主数量="+nnum+",辅数量="+nassnum+"\n 低于客户的最小发货量"+nminSendNum+",客户["
						+ WdsWlPubTool.getCustNameByid(bill.getHeader()
								.getCcustomerid()) + "]本次无法安排;\n";
					Logger.info(reason);
					reasons.add(reason);
					pass = true;
					break;
				}
				//2.4比较可用量与本次安排数量，判断是否可以直接安排(如果可用量 > 本次安排数量,bisok = true)
				// 即使该客户表体存货有一个可用量不满足，也标记为不能直接安排
				boolean bisok = PuPubVO.getUFBoolean_NullAs(invNumInfor.get(body.getCinvbasdocid()).getBisok(),UFBoolean.FALSE).booleanValue();
				if (!bisok) {
					isdeal = false;
					continue;
				}
			}
			//3. 经过 上 4步 的判断，对客户分3种情况进行安排：
			//pass=true 直接跳过，本次不进行安排;
			//isdeal=true 可以直接安排生成运单，放入ldeal,一会直接安排生成运单;
			//isdeal = false 不能直接安排，放入lnodeal 经过数据处理，返回前台进行手动安排
			if (pass)
				continue;
			if (isdeal) {
				if (ldeal == null) {
					ldeal = new ArrayList<SoDealVO>();
				}
				ldeal.addAll(Arrays.asList(bodys));
				Logger.info("##客户["
						+ WdsWlPubTool.getCustNameByid(bill.getHeader()
								.getCcustomerid()) + "]本次可直接安排");
			} else {
				if (lnodeal == null) {
					lnodeal = new ArrayList<SoDealVO>();
				}
				lnodeal.addAll(Arrays.asList(bodys));
				lcust.add(bill);
			}
		}
		// 4.将过滤后可以安排的是数据进行安排
		// 4.1 将可以直接安排的数据，直接安排生成运单，将isauto标记为true
		UFBoolean isauto = UFBoolean.FALSE;// 本次安排待安排的数据中，是否有数据满足自动安排，进行了自动安排
		if (ldeal == null || ldeal.size() == 0) {
			Logger.info("本次安排未存在可直接安排的客户");
		} else {// 直接安排
			SoDealBO dealbo = new SoDealBO();
			dealbo.doDeal(ldeal, lpara);
			isauto = UFBoolean.TRUE;
			Logger.info("系统直接安排成功");
		}
		//4.2 将需要手动安排的数据，进行封装，返回前台处理
		//????如果存在自动安排，则当前存货可用量已经不准确，是否应该重新计算？？
		if (lnodeal != null && lnodeal.size() > 0) {
			Collection<StoreInvNumVO> c = invNumInfor.values();
			Iterator<StoreInvNumVO> it = c.iterator();
			StoreInvNumVO tmp = null;
			List<StoreInvNumVO> ltmp = new ArrayList<StoreInvNumVO>();
			while (it.hasNext()) {
				tmp = it.next();
				for (SoDealVO deal : lnodeal) {
					if (deal.getCinvbasdocid().equalsIgnoreCase(
							tmp.getCinvbasid())) {
						tmp.getLdeal().add(deal);
					}
				}
				if (tmp.getLdeal().size() > 0)
					ltmp.add(tmp);
			}
			if (lcust.size() > 0)
				Logger.info("存在" + lcust.size() + "个客户由于库存不足需要手工进行安排");
			// UFDateTime time2 = new UFDateTime(System.currentTimeMillis());
			Logger.info("本次安排处理结束,返回界面手工安排");
			Logger.info("#####################################################");
			return new Object[] { isauto, lcust, ltmp,reasons };
		} else {
			Logger.info("本次安排未存在需要用户手工安排的数据");
			Logger.info("本次安排处理结束");
			Logger.info("#####################################################");
		}
		return new Object[] { isauto, null, null ,reasons};
	}

}

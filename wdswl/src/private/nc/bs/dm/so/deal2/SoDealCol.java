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
import java.util.Map.Entry;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.wds.ic.stock.StockInvOnHandBO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.dm.so.deal2.SoDealBillVO;
import nc.vo.dm.so.deal2.SoDealHeaderVo;
import nc.vo.dm.so.deal2.SoDealVO;
import nc.vo.dm.so.deal2.StoreInvNumVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.wl.pub.WdsWlPubConst;
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
					//根据仓库+存货 获得 合格和待检状态的现存量
					String strWhere= " ss_pk in('"+WdsWlPubConst.WDS_STORSTATE_PK_hg+"','"+WdsWlPubConst.WDS_STORSTATE_PK_dj+"')";
					stocknums = getStockBO().getInvStockNum(pk_corp,
							tmpNumVO.getCstoreid(), null,
							tmpNumVO.getCinvbasid(), null, null,strWhere);
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
		// 2.获取运单占用量,根据存货现存量和待安排数量，将存货分为两类：够安排和不够安排
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
			throw new BusinessException("所有存货的当前库存量均为空,无法自动安排");
		// 2.根据可用量过滤客户，将客户分为本次可以直接安排的客户，存量不足需要手工安排的客户
		Logger.info("根据最小发货量过滤库存可用量偏低的存货");
		List<SoDealVO> ldeal = null;
		List<SoDealVO> lnodeal = null;
		List<SoDealBillVO> lcust = new ArrayList<SoDealBillVO>();// 因为可用量不足，不能直接安排发货的客户信息
		List<String> reasons = new ArrayList<String>();// 客户本次不能安排的原因,返回前台做提示用
		List<String> reasons2 = new ArrayList<String>();//本次直接安排的客户
		for (SoDealBillVO bill : bills) {
			SoDealVO[] bodys = bill.getBodyVos();
			if (bodys == null || bodys.length == 0)
				continue;
			boolean pass = false;//是否跳过该客户
			boolean isGift = false;//判断是否赠品单：赠品单不允许拆单。即赠品单存在两种情况：可用量都满足，直接安排;有可用量不满足，跳过;
			boolean isdeal = true;//默认 该客户表体 货可用量都满足
			for (SoDealVO body : bodys) {
				StoreInvNumVO tmpNumVO = invNumInfor.get(body.getCinvbasdocid());
				boolean blargessflag = PuPubVO.getUFBoolean_NullAs(body.getBlargessflag(), UFBoolean.FALSE).booleanValue();
				if(blargessflag){
					isGift = true;
				}
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
				reasons2.add(WdsWlPubTool.getCustNameByid(bill.getHeader()
						.getCcustomerid()));
				Logger.info("##客户["
						+ WdsWlPubTool.getCustNameByid(bill.getHeader()
								.getCcustomerid()) + "]本次可直接安排");
			} else {
				if (lnodeal == null) {
					lnodeal = new ArrayList<SoDealVO>();
				}
				if(isGift){
					String reason = "客户["+ WdsWlPubTool.getCustNameByid(bill.getHeader()
								.getCcustomerid()) + "包含赠品单，但又可用量不足的存货，不能安排，可将赠品单先单独安排";
					Logger.info(reason);
					reasons.add(reason);
				}else{
					lnodeal.addAll(Arrays.asList(bodys));
					lcust.add(bill);
				}
				
			}
		}
		// 4.将过滤后可以安排的是数据进行安排
		// 4.1 将可以直接安排的数据，直接安排生成运单，将isauto标记为true
		UFBoolean isauto = UFBoolean.FALSE;// 本次安排待安排的数据中，是否有数据满足自动安排，进行了自动安排
		if (ldeal == null || ldeal.size() == 0) {
			Logger.info("本次安排未存在可直接安排的客户");
		} else {// 直接安排
			doDeal(ldeal, lpara);
			isauto = UFBoolean.TRUE;
			Logger.info("系统直接安排成功");
		}
		//4.2 将需要手动安排的数据，进行封装，返回前台处理
		//????如果存在自动安排，自动安排之后，则当前存货可用量已经不准确，是否应该重新计算？？
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
			return new Object[] { isauto, lcust, ltmp,reasons,reasons2};
		} else {
			Logger.info("本次安排未存在需要用户手工安排的数据");
			Logger.info("本次安排处理结束");
			Logger.info("#####################################################");
		}
		return new Object[] { isauto, null, null ,reasons,reasons2};
	}
	
	
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目:执行安排，生成销售运单
	 * @时间：2011-3-25下午03:58:14
	 * @param ldata
	 * @param infor
	 *            :登录人，登录公司，登录日期
	 * @throws Exception
	 */
	public void doDeal(List<SoDealVO> ldata, List<String> infor)
			throws Exception {
		if (ldata == null || ldata.size() == 0)
			return;
		/**
		 * 安排：生成发运单 发运计划安排生成发运订单
		 * 
		 * 计划单号 计划行号 不合并计划行 计划和订单为1对多关系 分单规则： 发货站 收货站不同 不考虑计划类型
		 * */
		// 回写计划累计安排数量
		// 销售计划安排vo---》销售订单
		Map<String, UFDouble> map = new HashMap<String, UFDouble>();
		for (int i = 0; i < ldata.size(); i++) {
			String key = ldata.get(i).getCorder_bid();
			UFDouble num = PuPubVO.getUFDouble_NullAsZero(ldata.get(i)
					.getNnum());
			if (map.containsKey(key)) {
				UFDouble oldValue = PuPubVO
						.getUFDouble_NullAsZero(map.get(key));
				map.put(key, oldValue.add(num));
			}
			map.put(key, num);
		}
		reWriteDealNumForPlan(map);
		// 按 计划号 发货站 客户 分单
		CircularlyAccessibleValueObject[][] datas = SplitBillVOs.getSplitVOs(
				(CircularlyAccessibleValueObject[]) (ldata
						.toArray(new SoDealVO[0])),
				WdsWlPubConst.SO_PLAN_DEAL_SPLIT_FIELDS);
		if (datas == null || datas.length == 0)
			return;
		int len = datas.length;
		SoDealVO[] tmpVOs = null;
		SoDealBillVO[] planBillVos = new SoDealBillVO[len];
		for (int i = 0; i < len; i++) {
			tmpVOs = (SoDealVO[]) datas[i];
			planBillVos[i] = new SoDealBillVO();
			planBillVos[i].setParentVO(getPlanHead(tmpVOs[0]));
			planBillVos[i].setChildrenVO(tmpVOs);
		}
		// // 销售订单--》销售运单
		PfParameterVO paraVo = new PfParameterVO();
		paraVo.m_operator = infor.get(0);
		paraVo.m_coId = infor.get(1);
		paraVo.m_currentDate = infor.get(2);
		// // 参量上 设置 日期 操作人
		AggregatedValueObject[] orderVos = (AggregatedValueObject[]) PfUtilTools
				.runChangeDataAry(WdsWlPubConst.WDS4, WdsWlPubConst.WDS5,
						planBillVos, paraVo);
		// // 分单---》保存订单
		if (orderVos == null || orderVos.length == 0) {
			return;
		}
		PfUtilBO pfbo = new PfUtilBO();
		for (AggregatedValueObject bill : orderVos) {
			pfbo.processAction(WdsWlPubConst.DM_PLAN_TO_ORDER_SAVE,
					WdsWlPubConst.WDS5, infor.get(2), null, bill, null);
		}
	}
	
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 将本次安排数量，回写到销售订单安排累计发运数量
	 * @时间：2011-3-25下午04:44:08
	 * @param dealnumInfor
	 * @throws BusinessException
	 */
	private void reWriteDealNumForPlan(Map<String, UFDouble> map)
			throws BusinessException {

		if (map == null || map.size() == 0)
			return;
		for (Entry<String, UFDouble> entry : map.entrySet()) {
			String sql = "update so_saleorder_b set "
					+ WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME
					+ " = coalesce("
					+ WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME
					+ ",0)+"
					+ PuPubVO.getUFDouble_NullAsZero(entry.getValue())
							.doubleValue() + " where corder_bid='"
					+ entry.getKey() + "'";
			if (getDao().executeUpdate(sql) == 0) {
				throw new BusinessException("数据异常：该发运计划可能已被删除，请重新查询数据");
			}
			;

			// 将计划数量（nplannum）和累计安排数量(ndealnum)比较

			// 如果累计安排数量大于计划数量将抛出异常

			String sql1 = "select count(0) from so_saleorder_b where corder_bid='"
					+ entry.getKey()
					+ "'and (coalesce(nnumber,0)-coalesce("
					+ WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME + ",0))>=0";
			Object o = getDao().executeQuery(sql1,
					WdsPubResulSetProcesser.COLUMNPROCESSOR);
			if (o == null) {
				throw new BusinessException("超计划量安排");
			}
		}
	}
	
	private SoDealHeaderVo getPlanHead(SoDealVO dealVo) {
		if (dealVo == null)
			return null;
		SoDealHeaderVo head = new SoDealHeaderVo();
		String[] names = head.getAttributeNames();
		for (String name : names) {
			head.setAttributeValue(name, dealVo.getAttributeValue(name));
		}
		return head;
	}

}

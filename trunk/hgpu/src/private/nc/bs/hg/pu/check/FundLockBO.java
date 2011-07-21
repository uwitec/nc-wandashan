package nc.bs.hg.pu.check;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.dao.DAOException;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.itf.hg.pu.pub.IFundCheck;
import nc.vo.hg.pu.check.fund.FUNDSETVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;

/**
 * 
 * @author zhf
 * 
 * 资金 台帐设计： 维度 总资金 预扣 实扣 预留（计划）
 * 
 * 原始总资金=总资金+预留 只有产生预留时 调整总资金 产生预扣合实扣时 总资金不随之变动
 * 
 * 剩余可用资金=总资金+该计划的预留-预扣-实扣
 * 
 * 维度： 上级为下级设置每个月可用资金总量 供应处设置各个矿 矿为下属各个采区设置
 * 
 * 矿和供应处之间的物资领用走调拨 采区和矿之间 因为采区没有库存 走 材料出库单 外销走销售订单 销售出库
 * 
 */

public class FundLockBO implements IFundCheck {

	private nc.bs.dao.BaseDAO dao = null;

	private nc.bs.dao.BaseDAO getDao() {
		if (dao == null)
			dao = new nc.bs.dao.BaseDAO();
		return dao;
	}
	
	private FundPrepareBO prepareBO = null;
	private FundPrepareBO getPrepareBO(){
		if(prepareBO == null){
			prepareBO = new FundPrepareBO(getDao());
		}
		return prepareBO;
	}



//	public void reLockFund(String pk_plan, int ifundtype, String scorpid,
//			String sdeptid, UFDate uDate, String loginCorp)
//			throws BusinessException {
//
//		FUNDSETVO vo = getFundSet(scorpid, sdeptid, null, uDate, ifundtype,
//				false, loginCorp);
//		if (vo == null)
//			return;
//		reLockFund(vo, pk_plan);
//	}

//	public void reLockFund(FUNDSETVO vo, String pk_plan)
//			throws BusinessException {
//		String sql = " update hg_fundset_b set dr = 1 where pk_plan = '"
//				+ pk_plan + "' and pk_fundset = '" + vo.getPrimaryKey()
//				+ "' and isnull(dr,0)=0";
//		getDao().executeUpdate(sql);
//	}

	

	public FUNDSETVO getFundSet(String scorpid, String sdeptid,
			String ccustmanid, UFDate uDate, int ifundtype, boolean flag,
			String loginCorp) throws BusinessException {
		String corp = PuPubVO.getString_TrimZeroLenAsNull(scorpid);
		String deptid = PuPubVO.getString_TrimZeroLenAsNull(sdeptid);
		String scustid = PuPubVO.getString_TrimZeroLenAsNull(ccustmanid);

		if (corp == null && deptid == null && scustid == null)
			return null;
		StringBuffer strb = new StringBuffer();
		// 客户优先
		if (scustid != null) {
			strb.append(" vdef1 = '" + scustid + "' and ");
		} else {
			if (corp != null) {
				strb.append("  pk_corp = '" + corp + "' and ");
			}
			if (deptid != null) {
				strb.append(" cdeptid = '" + deptid + "' and ");
			} else
				strb.append(" (cdeptid = '' or cdeptid is null ) and ");
		}

		strb.append(" isnull(dr,0)=0 ");
		// add by zhw 2010-12-28 获取当前日期对应的会计年 会计月
		// 得到基准期间方案的日历实例
		AccountCalendar calendar = AccountCalendar.getInstance();
		AccountCalendar calendar1 = AccountCalendar.getInstance();
		calendar.setDate(uDate);
		// 设置日期
		String iyear = calendar.getYearVO().getPeriodyear();
		String imonth = calendar.getMonthVO().getMonth();
		String imonth1 = calendar1.getMonthVO().getMonth();

		// zhw end
		strb.append(" and cyear = '" + String.valueOf(iyear)
				+ "' and imonth = '" + String.valueOf(imonth) + "'");
		strb.append(" and coalesce(fcontrol,'N')='Y' and ifundtype = "
				+ ifundtype);
		strb.append(" and vdef2 = '" + loginCorp + "'");
		java.util.Collection c = getDao().retrieveByClause(FUNDSETVO.class,
				strb.toString());
		// getDMO().queryByWhereClause(FUNDSETVO.class, strb.toString());
		if ((c == null || c.size() == 0))
			if (flag) {
				// modify by zhw 2010-03-11
				if (imonth1.equalsIgnoreCase(imonth))// 如果当月没有进行专项资金设置
					throw new BusinessException("未进行有效的专项资金设置，无法预留");
				// modify by zhw 2010-03-11
				return null;
			} else
				return null;
		// }
		if (c.size() > 1) {
			throw new BusinessException("资金设置异常");
		}
		java.util.Iterator it = c.iterator();
		FUNDSETVO vo = (FUNDSETVO) it.next();
		return vo;
	}


//	public void reLockFundForPlan(HYBillVO planvo) throws BusinessException {
//		if (planvo == null)
//			return;
//		PlanVO head = (PlanVO) planvo.getParentVO();
//		PlanOtherBVO[] bodys = (PlanOtherBVO[]) planvo.getChildrenVO();
//		// 按日期进行汇总金额
//		java.util.Map<String, UFDate> dateMap = new HashMap<String, UFDate>();
//		String key = null;
//		UFDate date = null;
//		for (PlanOtherBVO body : bodys) {
//			date = body.getDreqdate();// 供货日期 为 有效的 日期
//			key = HgPubTool.getString_NullAsTrimZeroLen(date.getYear())
//					+ HgPubTool.getMonth(date.getMonth());
//			if (!dateMap.containsKey(key))
//				dateMap.put(key, date);
//		}
//
//		if (dateMap.size() == 0)
//			return;
//
//		int iplantype = HgPubTool.getIFundTypeByPlanType(head.getPk_billtype());
//
//		if (iplantype == HgPubConst.FUND_CHECK_SPECIALFUND) {// 专项资金
//			for (UFDate uDate : dateMap.values()) {
//				reLockFund(head.getPrimaryKey(),
//						HgPubConst.FUND_CHECK_SPECIALFUND, head.getPk_corp(),
//						head.getCapplydeptid(), uDate, "1002");
//			}
//			return;
//		}
//	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）资金 限额 专项资金 的 预扣 2011-2-2下午01:01:39  无预留情况下的预扣
	 *                   目前系统仅专项资金计划存在预留问题
	 * @param pk_plan
	 * @param cplanbilltype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nmny
	 * @throws BusinessException
	 */
	public void useFund_Before(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nmny, String pk,
			String billtype, String loginCorp) throws BusinessException {
		FUNDSETVO vo = null;
		vo = getFundSet(scorpid, sdeptid, scorpid == null ? sdeptid : null,
				uDate, ifundtype, false, loginCorp);

		if (vo == null) {
			return;
		}

		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// 总资金
		UFDouble oldnmny = PuPubVO.getUFDouble_NullAsZero(getNMY(pk, billtype));// 修改前金额
		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// 预扣
		UFDouble nsubmny = nmny.sub(oldnmny);//预扣增加值
		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());// 实扣
//		UFDouble nlockmny = UFDouble.ZERO_DBL;// 预留------------------------------zhf 资金和限额不考虑预留 注释掉 2011-04-13

		// 校验是否可以预扣 总资金-预扣-实扣-预扣增加值>0?
		UFDouble nsubfund = nfund.sub(nlockfund).sub(nactfund)
				.sub(nsubmny);

		if (nsubfund.doubleValue() < 0) {
			throw new BusinessException("超" + HgPubTool.getSFundType(ifundtype)
					+ "控制");
		}

		vo.setNlockfund(nlockfund.add(nsubmny,HgPubConst.MNY_DIGIT));
		getDao().updateVO(vo, HgBsPubTool.CHECK_LOCkFUND_NAME);		
		
		//数据校验
		checkFundAfterUse(vo.getPrimaryKey(), ifundtype, null, uDate, loginCorp);
	}

//	public void clearLockFund(String pk_plan, String pk_fundset)
//			throws BusinessException {
//		String sql = "update hg_fundset_b set dr = 1 where pk_plan = '"
//				+ pk_plan + "' and pk_fundset = '" + pk_fundset
//				+ "' and isnull(dr,0)=0";
//		getDao().executeUpdate(sql);
//	}

//	public void updateLockFund(String pk_plan, String pk_fundset,
//			UFDouble nmny, UFDouble noldmny, boolean isAct)
//			throws BusinessException {
//		StringBuffer str = new StringBuffer();
//		str.append("update hg_fundset_b set nlockmny = "
//				+ PuPubVO.getUFDouble_NullAsZero(nmny));
//		if (!isAct)
//			str.append(" and noldlockmny = "
//					+ PuPubVO.getUFDouble_NullAsZero(noldmny));
//		str.append("where pk_plan = '" + pk_plan + "' and pk_fundset = '"
//				+ pk_fundset + "' and isnull(dr,0)=0");
//		// String sql = "update hg_fundset_b set nlockmny =
//		// "+nmny.doubleValue()+" and noldlockmny = " +noldmny.doubleValue() +
//		// "where pk_plan = '"+pk_plan+"' and pk_fundset = '"+pk_fundset+"' and
//		// isnull(dr,0)=0";
//		getDao().executeUpdate(str.toString());
//	}
	
	
	

//	/**
//	 * 
//	 * @author zhw
//	 * @说明：（鹤岗矿业） 2011-3-21下午03:23:53
//	 * @param pk_plan
//	 * @param pk_fundset
//	 * @param nmny
//	 * @param noldmny
//	 * @param isAct
//	 * @throws BusinessException
//	 */
//	public void updateLockSpecalFund(String pk_planother_b, UFDouble locknmny)
//			throws BusinessException {
//		StringBuffer str = new StringBuffer();
//		str.append("update hg_fundset_b set nmny = "
//				+ PuPubVO.getUFDouble_NullAsZero(locknmny));
//		str.append(" where pk_planother_b ='" + pk_planother_b + "'");
//		getDao().executeUpdate(str.toString());
//	}

	public UFDouble getPlanLockFund(String pk_plan, String pk_fundset)
			throws BusinessException {
		String sql = "select nlockmny from hg_fundset_b where  pk_plan = '"
				+ pk_plan + "' and pk_fundset = '" + pk_fundset
				+ "' and isnull(dr,0)=0";
		Object o = getDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
		return o == null ? null : PuPubVO.getUFDouble_NullAsZero(o);
		// return nmny;
	}
	
	
	

//	public UFDouble getPlanLockFundByPk(String pk_plan, String pk_fundset)
//			throws BusinessException {
//		String sql = "select nlockmny from hg_fundset_b where  pk_plan = '"
//				+ pk_plan + "' and pk_fundset = '" + pk_fundset
//				+ "' and isnull(dr,0)=0";
//		Object o = getDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
//		return o == null ? null : PuPubVO.getUFDouble_NullAsZero(o);
//	}

//	public UFDouble getLockFundForPlan(String pk_plan, String pk_fundset)
//			throws BusinessException {
//		String sql = "select nlockmny from hg_fundset_b where  pk_plan = '"
//				+ pk_plan + "' and pk_fundset = '" + pk_fundset
//				+ "' and isnull(dr,0)=0";
//		Object o = getDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
//		return o == null ? null : PuPubVO.getUFDouble_NullAsZero(o);
//	}

	public UFDouble getPlanOldLockFund(String pk_plan, String pk_fundset)
			throws BusinessException {
		String sql = "select noldlockmny from hg_fundset_b where  pk_plan = '"
				+ pk_plan + "' and pk_fundset = '" + pk_fundset
				+ "' and isnull(dr,0)=0";
		UFDouble nmny = PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(
				sql, HgBsPubTool.COLUMNPROCESSOR));
		return nmny;
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）实扣 根据实际出库数量计算金额 扣除 资金 限额 或专项资金 2011-2-2下午01:49:26
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nbeforemny
	 *            预扣
	 * @param nmny
	 *            实扣
	 * @throws BusinessException
	 */
//	public void useFund(String pk_plan, int ifundtype, String scorpid,
//			String sdeptid, UFDate uDate, UFDouble nbeforemny, UFDouble nmny,
//			String pk, String billtype, String loginCorp)
//			throws BusinessException {
//		// 扣减顺序 预留 预扣 总资金
//		if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
//				.doubleValue())
//			return;
//		// add by zhw 2010-12-28
//		FUNDSETVO vo = getFundSet(scorpid, sdeptid, null, uDate, ifundtype,
//				false, loginCorp);
//		if (vo == null)
//			return;
//		// add by zhw 2010-12-28 end
//
//		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// 总资金
//		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// 预扣
//		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());
//		// modify by zhw 2010-12-28 begin
//		UFDouble oldnmny = PuPubVO.getUFDouble_NullAsZero(getNMY(pk, billtype));// 修改前金额
//		nactfund = nactfund.sub(oldnmny);
//		// modify by zhw 2010-12-28 end
//		UFDouble nlockmny = null;// 计划预留
//
//		if (PuPubVO.getString_TrimZeroLenAsNull(pk_plan) != null) {
//			// 获取计划预留资金
//			nlockmny = getPlanLockFund(pk_plan, vo.getPk_fundset());
//		}
//
//		boolean islock = nlockmny == null ? false : true;
//
//		nlockmny = PuPubVO.getUFDouble_NullAsZero(nlockmny);
//
//		// 实扣 和 预扣得大小关系
//		UFDouble nsubmny = nmny.sub(nbeforemny);
//
//		if (nfund.add(nlockmny).sub(nlockfund).sub(nactfund).sub(nsubmny)
//				.doubleValue() < 0)
//			throw new BusinessException("超" + HgPubTool.getSFundType(ifundtype)
//					+ "控制");
//
//		// nlockfund = nlockfund.sub(nbeforemny);
//		nlockfund = nlockfund.sub(nmny.sub(oldnmny));
//		nactfund = nactfund.add(nmny);
//		vo.setNlockfund(nlockfund);
//		vo.setNactfund(nactfund);
//		getDao().updateVO(vo, HgBsPubTool.CHECK_FUND_USENAMES);
//
//		if (!islock)
//			return;
//
//		// 调整预留
//		nlockmny = nlockmny.sub(nsubmny);
//		if (nlockmny.doubleValue() < 0)
//			nlockmny = UFDouble.ZERO_DBL;
//		UFDouble noldlockmny = PuPubVO
//				.getUFDouble_NullAsZero(getPlanOldLockFund(pk_plan, vo
//						.getPk_fundset()));
//		if (nlockmny.doubleValue() > noldlockmny.doubleValue())
//			nlockmny = noldlockmny;
//
//		updateLockFund(pk_plan, vo.getPrimaryKey(), nlockmny, null, true);
//	}
	

//	public void reUseFund(String pk_plan, int ifundtype, String scorpid,
//			String sdeptid, UFDate uDate, UFDouble nbeforemny, UFDouble nmny,
//			String loginCorp) throws BusinessException {
//
//		if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
//				.doubleValue())
//			return;
//		FUNDSETVO vo = getFundSet(scorpid, sdeptid, null, uDate, ifundtype,
//				false, loginCorp);
//		if (vo == null) {
//			return;
//		}
//
//		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// 预扣
//		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());// 实扣
//		UFDouble nlockmny = null;// 预留
//		if (PuPubVO.getString_TrimZeroLenAsNull(pk_plan) != null) {
//			// 获取计划预留资金
//			nlockmny = getPlanLockFund(pk_plan, vo.getPk_fundset());
//		}
//		boolean isLock = nlockmny == null ? false : true;
//
//		if (nactfund.doubleValue() < nmny.doubleValue())
//			throw new BusinessException("数据错误，超实扣金额");
//
//		nactfund = nactfund.sub(nmny);
//		// nlockfund = nlockfund.add(nbeforemny);
//		nlockfund = nlockfund.add(nmny);
//		UFDouble nsubmny = nbeforemny.sub(nmny);
//
//		if (isLock) {//
//			UFDouble noldlockmny = PuPubVO
//					.getUFDouble_NullAsZero(getPlanOldLockFund(pk_plan, vo
//							.getPk_fundset()));
//			nlockmny = nlockmny.sub(nsubmny);
//			if (nlockmny.doubleValue() > noldlockmny.doubleValue()) {
//				nlockmny = noldlockmny;
//			}
//		}
//		vo.setNactfund(nactfund);
//		vo.setNlockfund(nlockfund);
//		getDao().updateVO(vo, HgBsPubTool.CHECK_FUND_USENAMES);
//
//		updateLockFund(pk_plan, vo.getPrimaryKey(), nlockmny, null, true);
//	}

	/**
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业）实扣 根据实际出库数量计算金额 扣除 资金 限额 2011-2-2下午01:49:26
	 * @param pk_plan
	 *            用于材料出库
	 * @param ifundtype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nbeforemny
	 *            预扣
	 * @param nmny
	 *            实扣
	 * @throws BusinessException
	 */
	public void useFundMater(String pk_plan, int ifundtype, String sdeptid,
			String cust, UFDate uDate, UFDouble nmny,
			String pk, String billtype, String loginCorp)
			throws BusinessException {
		// 扣减顺序 预留 预扣 总资金
//		if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
//				.doubleValue())
//			return;
		// add by zhw 2010-12-28
		FUNDSETVO vo = getFundSet(pk_plan, sdeptid, cust, uDate, ifundtype,
				false, loginCorp);
		if (vo == null)
			return;
		// add by zhw 2010-12-28 end

		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// 总资金
		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());
		UFDouble oldnmny = PuPubVO.getUFDouble_NullAsZero(getNMY(pk, billtype));// 修改前金额
		nmny = nmny.sub(oldnmny);//实扣本次增加
		if (nfund.sub(nactfund).sub(nmny)
				.doubleValue() < 0)
			throw new BusinessException("超" + HgPubTool.getSFundType(ifundtype)
					+ "控制");
		nactfund = nactfund.add(nmny,HgPubConst.MNY_DIGIT);
		vo.setNactfund(nactfund);
		getDao().updateVO(vo, HgBsPubTool.CHECK_FUND_USENAMES);
		
		//数据校验
		checkFundAfterUse(vo.getPrimaryKey(), ifundtype, null, uDate, loginCorp);
	}

	/**
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业）实扣 根据实际出库数量计算金额 扣除 资金 限额 2011-2-2下午01:49:26
	 * @param pk_plan
	 *            用于材料出库
	 * @param ifundtype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nbeforemny
	 *            预扣
	 * @param nmny
	 *            实扣
	 * @throws BusinessException
	 */
//	public void reUseFundMater(String pk_plan, int ifundtype, String sdeptid,
//			String cust, UFDate uDate, UFDouble nbeforemny, UFDouble nmny,
//			String loginCorp) throws BusinessException {
//
//		if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
//				.doubleValue())
//			return;
//		FUNDSETVO vo = getFundSet(null, sdeptid, cust, uDate, ifundtype, false,
//				loginCorp);
//		if (vo == null) {
//			return;
//		}
//
//		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// 预扣
//		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());// 实扣
//
//		UFDouble nlockmny = null;// 预留
//		if (PuPubVO.getString_TrimZeroLenAsNull(pk_plan) != null) {
//			// 获取计划预留资金
//			nlockmny = getPlanLockFund(pk_plan, vo.getPk_fundset());
//		}
//		boolean isLock = nlockmny == null ? false : true;
//
//		if (nactfund.doubleValue() < nmny.doubleValue())
//			throw new BusinessException("数据错误，超实扣金额");
//
//		nactfund = nactfund.sub(nmny);
//		nlockfund = nlockfund.add(nbeforemny);
//
//		UFDouble nsubmny = nbeforemny.sub(nmny);
//
//		if (isLock) {//
//			UFDouble noldlockmny = PuPubVO
//					.getUFDouble_NullAsZero(getPlanOldLockFund(pk_plan, vo
//							.getPk_fundset()));
//			nlockmny = nlockmny.sub(nsubmny);
//			if (nlockmny.doubleValue() > noldlockmny.doubleValue()) {
//				nlockmny = noldlockmny;
//			}
//		}
//
//		vo.setNactfund(nactfund);
//		vo.setNlockfund(nlockfund);
//		getDao().updateVO(vo, HgBsPubTool.CHECK_FUND_USENAMES);
//
//		updateLockFund(pk_plan, vo.getPrimaryKey(), nlockmny, null, true);
//	}
	
	private UFDouble getOldActMny(String pk, String billType) throws BusinessException{
		if (billType.equals(ScmConst.m_materialOut)||billType.equals(ScmConst.m_allocationOut)) {
			String sql = "select nplannedmny,nmny from  ic_general_b where cgeneralbid = '"+pk
					+ "' and isnull(dr,0)=0";
		     // 0  计划价   1  合同价
			Object o = getDao().executeQuery(sql, HgBsPubTool.ARRAYPROCESSOR);
			if(o!=null){
				Object[] os = (Object[])o;
				if(PuPubVO.getUFDouble_ZeroAsNull(os[1])==null)
					return PuPubVO.getUFDouble_NullAsZero(os[0]);
				return PuPubVO.getUFDouble_NullAsZero(os[1]);
			}
			
		} else if (billType.equals(ScmConst.m_sBillDBDD)) {
//			String sql = "select sum(b.nnotaxmny) from to_bill h join to_bill_b b on h.cbillid=b.cbillid where h.cbillid= '"
//					+ pk
//					+ "' and h.cbilltype = '"
//					+ ScmConst.m_sBillDBDD
//					+ "' and isnull(h.dr,0)=0 and isnull(b.dr,0)=0";
//			Object o = getDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
//			return o == null ? null : PuPubVO.getUFDouble_NullAsZero(o);
		} else if (billType.equals(ScmConst.SO_Order)) {
//			String sql = "select sum(b.noriginalcurmny) from so_sale h join so_saleorder_b b on h.csaleid =b.csaleid  where h.csaleid  = '"
//					+ pk
//					+ "' and h.creceipttype  = '"
//					+ ScmConst.SO_Order
//					+ "' and isnull(h.dr,0)=0 and isnull(b.dr,0)=0";
//			Object o = getDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
//			return o == null ? null : PuPubVO.getUFDouble_NullAsZero(o);
		}
			return UFDouble.ZERO_DBL;
		
	}

	/**
	 * 获取修改前的金额
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-1-8下午06:26:15
	 * @param pk
	 * @param billType
	 * @return
	 * @throws DAOException
	 */
	public UFDouble getNMY(String pk, String billType) throws DAOException {
		if (billType.equals(ScmConst.m_materialOut)) {
			String sql = "select sum(b.nplannedmny) from ic_general_h h join ic_general_b b on h.cgeneralhid = b.cgeneralhid  where h.cgeneralhid= '"
					+ pk
					+ "' and h.cbilltypecode = '"
					+ ScmConst.m_materialOut
					+ "' and isnull(h.dr,0)=0 and isnull(b.dr,0)=0";
			Object o = getDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
			return o == null ? null : PuPubVO.getUFDouble_NullAsZero(o);
		} else if (billType.equals(ScmConst.m_allocationOut)) {
			String sql = "select sum(b.nplannedmny) from ic_general_h h join ic_general_b b on h.cgeneralhid = b.cgeneralhid  where h.cgeneralhid= '"
					+ pk
					+ "' and h.cbilltypecode = '"
					+ ScmConst.m_allocationOut
					+ "' and isnull(h.dr,0)=0 and isnull(b.dr,0)=0";
			Object o = getDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
			return o == null ? null : PuPubVO.getUFDouble_NullAsZero(o);
		} else if (billType.equals(ScmConst.m_sBillDBDD)) {
			String sql = "select sum(nnotaxmny) from to_bill_b  where cbillid= '"
					+ pk
					+ "' and  isnull(dr,0)=0";
			Object o = getDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
			return PuPubVO.getUFDouble_NullAsZero(o);
		} else if (billType.equals(ScmConst.SO_Order)) {
			String sql = "select sum(b.noriginalcurmny) from so_sale h join so_saleorder_b b on h.csaleid =b.csaleid  where h.csaleid  = '"
					+ pk
					+ "' and h.creceipttype  = '"
					+ ScmConst.SO_Order
					+ "' and isnull(h.dr,0)=0 and isnull(b.dr,0)=0";
			Object o = getDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
			return o == null ? null : PuPubVO.getUFDouble_NullAsZero(o);
		} else {
			return UFDouble.ZERO_DBL;
		}
	}
	

//	public void useSpeacialFund_Before(String pk_plan, int ifundtype,
//			String scorpid, String sdeptid, UFDate date, UFDouble nmny,
//			String pk, String billtype, Object[][] o, String loginCorp)
//			throws BusinessException {
//		if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
//				.doubleValue())
//			return;
//		FUNDSETVO vo = getFundSet(scorpid, sdeptid, scorpid == null ? sdeptid
//				: null, date, ifundtype, false, loginCorp);
//		if (vo == null) {
//			return;
//		}
//
//		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// 总资金
//		UFDouble oldnmny = PuPubVO.getUFDouble_NullAsZero(getNMY(pk, billtype));// 修改前金额
//		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// 预扣
//		nlockfund = nlockfund.sub(oldnmny);
//		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());// 实扣
//		UFDouble nlockmny = null;// 预留
//		// if (PuPubVO.getString_TrimZeroLenAsNull(pk_plan) != null) {
//		// // 获取计划预留资金
//		// nlockmny = getPlanLockFund(pk_plan, vo.getPk_fundset());
//		// }
//
//		if (ifundtype == HgPubConst.FUND_CHECK_SPECIALFUND) {
//			nlockmny = getPlanLockFundByCorp(scorpid, sdeptid, date);
//		}
//		boolean islock = nlockmny == null ? false : true;
//
//		nlockmny = PuPubVO.getUFDouble_NullAsZero(nlockmny);
//
//		// 校验是否可以预扣 总资金-预留-预扣-实扣-本次预扣
//		UFDouble nsubfund = nfund.sub(nlockmny).sub(nlockfund).sub(nactfund)
//				.sub(nmny);
//
//		if (nsubfund.doubleValue() < 0) {
//			throw new BusinessException("超" + HgPubTool.getSFundType(ifundtype)
//					+ "控制");
//		}
//
//		vo.setNlockfund(nlockfund.add(nmny));
//		getDao().updateVO(vo, HgBsPubTool.CHECK_LOCkFUND_NAME);
//
//		// 调整预留
//		if (!islock)
//			return;
//		int len = o.length;
//		for (int i = 0; i < len; i++) {
//			UFDouble locknmny = PuPubVO.getUFDouble_NullAsZero(o[i][1]);
//			String pk_planother_b = PuPubVO
//					.getString_TrimZeroLenAsNull(o[i][0]);
//			updateLockSpecalFund(pk_planother_b, locknmny);
//		}
//		// UFDouble noldlockmny = nlockmny;
//		// nlockmny = nlockmny.sub(nmny);
//		// if (nlockmny.doubleValue() < 0)
//		// nlockmny = UFDouble.ZERO_DBL;
//		// updateLockFund(pk_plan, vo.getPrimaryKey(), nlockmny, noldlockmny,
//		// false);
//
//		// updateLockSpecalFund()
//
//	}
	/**
	 * 专项资金计划预扣
	 */
	public void useSpeacialFund_Before(String pk_plan_b,
			String scorpid, String sdeptid, UFDate date, UFDouble nmny,
			String pk , String loginCorp)
	throws BusinessException {
		FUNDSETVO vo = getFundSet(scorpid, sdeptid, scorpid == null ? sdeptid
				: null, date, HgPubConst.FUND_CHECK_SPECIALFUND, false, loginCorp);
		if (vo == null) {
			return;
		}

		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// 总资金
		UFDouble oldnmny = PuPubVO.getUFDouble_NullAsZero(getNMY(pk, ScmConst.m_sBillDBDD));// 修改前金额
		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// 预扣
		UFDouble nsubmny = nmny.sub(oldnmny);//预扣增加值
		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());// 实扣	

		UFDouble nlockmny = null;// 计划可用预留
		UFDouble nalllockmny = null;//该单位全部预留

		boolean islock = false;

		islock = getPrepareBO().isYL(pk_plan_b,date);
		if(islock){
			nlockmny = PuPubVO.getUFDouble_NullAsZero(getPrepareBO().getPlanLockEnableMny(pk_plan_b, date));
			nalllockmny = PuPubVO.getUFDouble_NullAsZero(getPrepareBO().getFundPrepareMny(null, scorpid, sdeptid, null, date, HgPubConst.FUND_CHECK_SPECIALFUND, loginCorp));

		}


		// 校验是否可以预扣 总资金-全部预留+可用预留-预扣-实扣-本次预扣增加值>0
		UFDouble nsubfund = nfund.sub(PuPubVO.getUFDouble_NullAsZero(nalllockmny)).add(PuPubVO.getUFDouble_NullAsZero(nlockmny)).sub(nlockfund).sub(nactfund)
		.sub(nsubmny);

		if (nsubfund.doubleValue() < 0) {
			throw new BusinessException("超" + HgPubTool.getSFundType(HgPubConst.FUND_CHECK_SPECIALFUND)
					+ "控制");
		}

		vo.setNlockfund(nlockfund.add(nsubmny,HgPubConst.MNY_DIGIT));
		getDao().updateVO(vo, HgBsPubTool.CHECK_LOCkFUND_NAME);

		// 调整预留
		if (!islock)
			return;
		//		将预扣增加值 调整到 预留上  校验调整结果  累计预留不能大于原始预留  累计预留不能小于0
		getPrepareBO().adjustPrepareMnyOnBefore(pk_plan_b, nsubmny);
		
		//数据校验
		checkFundAfterUse(vo.getPrimaryKey(), HgPubConst.FUND_CHECK_SPECIALFUND, pk_plan_b, date, loginCorp);
	}

	/**
	 * 
	 * @author zhw 用于调拨出库
	 * @说明：（鹤岗矿业）实扣 根据实际出库数量计算金额 扣除 资金 限额 或专项资金 2011-2-2下午01:49:26
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nordermny 来源订单行原始金额  原始预扣
	 * @param nbeforemny
	 *            本次可用预扣    总预扣-累计已使用预扣 = 订单总无税金额-（订单累计入库数量*无税单价）
	 * @param nmny
	 *            本次实扣值
	 * @throws BusinessException
	 */
	public void useFundForAllOut(String pk_plan_b, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate,UFDouble nordermny, UFDouble nbeforemny, UFDouble nmny,
			String pk, String billtype, String loginCorp)
			throws BusinessException {
		
		/**
		 * zhf 20110413  算法调整：预扣后 只能该品种实扣时使用该预扣  其他不能使用 调整为按行明细进行实扣
		 * 新增实扣保存时   扣减顺序为：  预扣  预留 总资金    回复是的次序同
		 * 调整实扣保存时   具体逻辑看代码
		 * 删行时          等同于调整实扣  将实扣调为0
		 * 整单删除时       同删行
		 * 
		 */
		
		// 扣减顺序 预留 预扣 总资金
//		if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
//				.doubleValue())
//			return;
		// add by zhw 2010-12-28
		FUNDSETVO vo = getFundSet(scorpid, sdeptid, null, uDate, ifundtype,
				false, loginCorp);
		if (vo == null)
			return;
		// add by zhw 2010-12-28 end

		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// 总资金
		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// 预扣
		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());//实扣
		// modify by zhw 2010-12-28 begin
		UFDouble oldnmny = PuPubVO.getUFDouble_NullAsZero(getOldActMny(pk, billtype));// 原实扣值
		UFDouble nsubmny = nmny.sub(oldnmny);//实扣增加值
		if(nsubmny.equals(UFDouble.ZERO_DBL))//实扣值未发生变化
			return;
		// modify by zhw 2010-12-28 end
		UFDouble nlockmny = UFDouble.ZERO_DBL;// 计划可用预留
		UFDouble nalllockmny = UFDouble.ZERO_DBL;//该单位全部预留

		boolean islock = false;
		if (ifundtype == HgPubConst.FUND_CHECK_SPECIALFUND) {
			islock = getPrepareBO().isYL(pk_plan_b,uDate);
			if(islock){
				nlockmny = PuPubVO.getUFDouble_NullAsZero(getPrepareBO().getPlanLockEnableMny(pk_plan_b, uDate));
				nalllockmny = PuPubVO.getUFDouble_NullAsZero(getPrepareBO().getFundPrepareMny(null, scorpid, sdeptid, null, uDate, ifundtype, loginCorp));
				
			}
		}
		
//		可用资金=总资金-全部预留+可用预留-全部预扣+可用预扣-全部实扣
		UFDouble nallmny = nfund.sub(nalllockmny).add(nlockmny).sub(nlockfund).add(nbeforemny).sub(nactfund);
		
		if(oldnmny.doubleValue()==0){//第一次实扣
			boolean isadjust = nmny.doubleValue()>nbeforemny.doubleValue();
//			if(isadjust){
//				校验是否可以实扣出库 
				if(nallmny.doubleValue()<nmny.doubleValue()){
					throw new BusinessException("超" + HgPubTool.getSFundType(ifundtype)
							+ "控制。超出额："+String.valueOf(nmny.sub(nallmny,HgPubConst.MNY_DIGIT).doubleValue()));
				}
			
//			计算新实扣
			nactfund = nactfund.add(nmny,HgPubConst.MNY_DIGIT);
//			计算新预扣
			nlockfund = nlockfund.sub(nbeforemny).add(HgPubTool.getNBeforeMnyWhenAct(nbeforemny, nmny),HgPubConst.MNY_DIGIT);			
			if(islock && isadjust){//调整预留
//				//如果有预留 超额部分 需要先从预留上扣除
				getPrepareBO().updateFundSetBNumOnUseFund(pk_plan_b, nlockmny,nmny.sub(nbeforemny),false);
			}
		}else if(oldnmny.doubleValue()>0){//实扣修改
			if(nsubmny.doubleValue()>0){//实扣增加
//				校验是否可以实扣出库 
				if(nallmny.doubleValue()<nsubmny.doubleValue()){
					throw new BusinessException("超" + HgPubTool.getSFundType(ifundtype)
							+ "控制。超出额："+String.valueOf(nsubmny.sub(nallmny,HgPubConst.MNY_DIGIT).doubleValue()));
				}			
//				设置新的实扣
				nactfund = nactfund.add(nsubmny,HgPubConst.MNY_DIGIT);				
//				按 预扣  预留  总资金 次序  继续扣
//				1、考虑是否还有预扣可用
				boolean isYK = nbeforemny.doubleValue()>0;
				if(isYK){
					if(nbeforemny.doubleValue()>nsubmny.doubleValue()){
						nlockfund = nlockfund.sub(nsubmny,HgPubConst.MNY_DIGIT);
					}else{
						nlockfund = nlockfund.sub(nbeforemny,HgPubConst.MNY_DIGIT);
						//如果有预留需要调整预留
						if(islock){
							getPrepareBO().updateFundSetBNumOnUseFund(pk_plan_b, nlockmny,nsubmny.sub(nbeforemny),false);
						}
					}				
				}else{//没有剩余预扣了 调整预留
					if(islock){
						getPrepareBO().updateFundSetBNumOnUseFund(pk_plan_b, nlockmny,nsubmny,false);				
					}	
				}
			}else if(nsubmny.doubleValue()<0){//实扣减小
				//				减少的实扣要 按次序进行 回复 预扣  预留  总资金			
				//				设置新的实扣
				nactfund = nactfund.add(nsubmny,HgPubConst.MNY_DIGIT);
				boolean isYK = nbeforemny.doubleValue()>=0;
				if(isYK){
					nlockfund = nlockfund.add(nsubmny.abs(),HgPubConst.MNY_DIGIT);
				}else{// 调整预留
					//先回复给预扣  需要用 nsubmny  和  订单金额-累计出库金额 nsub2
					/**
					 * nsubmny>nsub2   预扣+nsubmny-nsub2
					 * else  预扣= 预扣
					 */
					if(nsubmny.abs().doubleValue()>=nbeforemny.abs().doubleValue()){
						//全部返回给预扣
						nlockfund = nlockfund.add(nsubmny.abs().sub(nbeforemny.abs()),HgPubConst.MNY_DIGIT);
						//剩余的调整预留
						if(islock){
							getPrepareBO().updateFundSetBNumOnUseFund(pk_plan_b, nlockmny,nbeforemny.abs(),true);	
						}
					}
					//					else{
					//						nlockfund = nlockfund.add(nordermny);												
					//					}		
				}				
			}
		}		
		
		vo.setNactfund(nactfund);
		vo.setNlockfund(nlockfund);
		getDao().updateVO(vo, HgBsPubTool.CHECK_FUND_USENAMES);
		//数据校验
		checkFundAfterUse(vo.getPrimaryKey(), ifundtype, pk_plan_b, uDate, loginCorp);
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）实扣后校验资金台账
	 * 2011-4-22下午05:59:05
	 * @param fundid
	 * @param ifundtype
	 * @param pk_plan_b
	 * @param uDate
	 * @param logincorp
	 * @throws BusinessException
	 */
	private void checkFundAfterUse(String fundid,int ifundtype,String pk_plan_b,UFDate uDate,String logincorp) throws BusinessException{
		// add by zhw 2010-12-28 end
		if(PuPubVO.getString_TrimZeroLenAsNull(fundid)==null)
			throw new BusinessException("资金设置数据异常");
		
		FUNDSETVO vo = getPrepareBO().getFundSetVoByID(fundid);
		if(vo == null)
			throw new BusinessException("资金设置数据异常");
		
		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// 总资金
		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// 预扣
		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());//实扣
		// modify by zhw 2010-12-28 end
//		UFDouble nlockmny = UFDouble.ZERO_DBL;// 计划可用预留
		UFDouble nalllockmny = UFDouble.ZERO_DBL;//该单位全部预留

		boolean islock = false;
		if (ifundtype == HgPubConst.FUND_CHECK_SPECIALFUND||pk_plan_b!=null) {
			islock = getPrepareBO().isYL(pk_plan_b,uDate);
			if(islock){
//				nlockmny = PuPubVO.getUFDouble_NullAsZero(getPrepareBO().getPlanLockEnableMny(pk_plan_b, uDate));
				nalllockmny = PuPubVO.getUFDouble_NullAsZero(getPrepareBO().getFundPrepareMny(null, vo.getPk_corp(), vo.getCdeptid(), null, uDate, ifundtype, logincorp));				
			}
		}
		
		if(nlockfund.doubleValue()<0)
			throw new BusinessException("资金扣减异常,预扣值出现红字");
		if(nactfund.doubleValue()<0)
			throw new BusinessException("资金扣减异常,实扣值出现红字");
		
//		可用资金=总资金-全部预留-全部预扣-全部实扣
		UFDouble nallmny = nfund.sub(nalllockmny).sub(nlockfund).sub(nactfund);
		if(nallmny.doubleValue()<0)
			throw new BusinessException("资金扣减异常,实扣+预扣超总资金");
	}
	
	private UFDouble getSaleOutOldActMny(String id,UFDouble nprice) throws BusinessException{
		String sql = "select noutnum from ic_general_b where cgeneralbid = '"+id+"'";
		return PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR)).multiply(nprice, 8);
		
	}
	
	public void useFundForSaleOut(String customid,
			 UFDate uDate,UFDouble nallyk, UFDouble nbeforemny, UFDouble nmny,
			String pk, String loginCorp,int ifundtype,UFDouble nprice)
			throws BusinessException {
		
		/**
		 * zhf 20110413  算法调整：预扣后 只能该品种实扣时使用该预扣  其他不能使用 调整为按行明细进行实扣
		 * 新增实扣保存时   扣减顺序为：  预扣  预留 总资金    回复是的次序同
		 * 调整实扣保存时   具体逻辑看代码
		 * 删行时          等同于调整实扣  将实扣调为0
		 * 整单删除时       同删行
		 * 
		 */
		
		// 扣减顺序 预留 预扣 总资金
//		if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
//				.doubleValue())
//			return;
		// 外销的资金获取方式
		String vdef1 = null;
		FUNDSETVO vo = null;

		vdef1 = PuPubVO.getString_TrimZeroLenAsNull(getCustomer(customid));
		if (vdef1 == null)
			throw new BusinessException("客商档案与领料单位之间的关系不存在,请查看是否维护");
		vo = getFundSet(null, null, vdef1, uDate, ifundtype, false, loginCorp);
		
		if (vo == null) {
			return;
		}
		// add by zhw 2010-12-28 end

		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// 总资金
		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// 预扣
		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());//实扣
		// modify by zhw 2010-12-28 begin
		UFDouble oldnmny = PuPubVO.getUFDouble_NullAsZero(getSaleOutOldActMny(pk, nprice));// 原实扣值
		UFDouble nsubmny = nmny.sub(oldnmny);//实扣增加值
		if(nsubmny.equals(UFDouble.ZERO_DBL))//实扣值未发生变化
			return;	
//		可用资金=总资金-全部预扣+可用预扣-全部实扣
		UFDouble nallmny = nfund.sub(nlockfund).add(nbeforemny).sub(nactfund,HgPubConst.MNY_DIGIT);	
		if(oldnmny.doubleValue()==0){//第一次实扣
//				校验是否可以实扣出库 
				if(nallmny.doubleValue()<nmny.doubleValue()){
					throw new BusinessException("资金或限额不足.超出额："+String.valueOf(nmny.sub(nallmny,HgPubConst.MNY_DIGIT).doubleValue()));
				}
//			计算新实扣
			nactfund = nactfund.add(nmny,HgPubConst.MNY_DIGIT);
//			计算新预扣
			nlockfund = nlockfund.sub(nbeforemny).add(HgPubTool.getNBeforeMnyWhenAct(nbeforemny, nmny),HgPubConst.MNY_DIGIT);
		}else if(oldnmny.doubleValue()>0){//实扣修改
			if(nsubmny.doubleValue()>0){//实扣增加
//				校验是否可以实扣出库 
				if(nallmny.doubleValue()<nsubmny.doubleValue()){
					throw new BusinessException("资金或限额不足.超出额："+String.valueOf(nsubmny.sub(nallmny,HgPubConst.MNY_DIGIT).doubleValue()));
				}	
//				设置新的实扣
				nactfund = nactfund.add(nsubmny,HgPubConst.MNY_DIGIT);
				
//				按 预扣   总资金 次序  继续扣
//				1、考虑是否还有预扣可用
					if(nbeforemny.doubleValue()>nsubmny.doubleValue()){
						nlockfund = nlockfund.sub(nsubmny,HgPubConst.MNY_DIGIT);
					}else{
						nlockfund = nlockfund.sub(nbeforemny,HgPubConst.MNY_DIGIT);
					}
			}else if(nsubmny.doubleValue()<0){//实扣减小
//				减少的实扣要 按次序进行 回复 预扣  预留  总资金				
//				设置新的实扣
				nactfund = nactfund.add(nsubmny,HgPubConst.MNY_DIGIT);
				boolean isYK = nbeforemny.doubleValue()>=0;
				if(isYK){
					nlockfund = nlockfund.add(nsubmny.abs(),HgPubConst.MNY_DIGIT);
//					getDao().updateVO(vo, HgBsPubTool.CHECK_FUND_USENAMES);		
				}else{// 调整预留
					//先回复给预扣
					if(nsubmny.abs().doubleValue()>=nbeforemny.abs().doubleValue()){
						//全部返回给预扣
						nlockfund = nlockfund.add(nsubmny.abs().sub(nbeforemny.abs(),HgPubConst.MNY_DIGIT));
					}
//					else{
//						nlockfund = nlockfund.add(nallyk);						
//					}						
				}
			}
		}
		
		vo.setNactfund(nactfund);
		vo.setNlockfund(nlockfund);
		getDao().updateVO(vo, HgBsPubTool.CHECK_FUND_USENAMES);	
		
		//数据校验
		checkFundAfterUse(vo.getPrimaryKey(), ifundtype, null, uDate, loginCorp);
	}
	
	



	/**
	 * 获取存货管理档案与领料单位之间的关系
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-4-13下午05:26:12
	 * @param customerid
	 * @return
	 * @throws BusinessException
	 */
	public String getCustomer(String customerid) throws BusinessException {
		String sql = " select def1 from bd_cumandoc where pk_cumandoc ='"
				+ customerid + "'";
		Object o = getDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
		return o == null ? null : PuPubVO.getString_TrimZeroLenAsNull(o);
	}

	/**
	 *用于外销预扣
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-4-13下午05:26:12
	 * @param customerid
	 * @return
	 * @throws BusinessException
	 */
	public void useFund_Before_SoOrder(String pk_plan, int ifundtype,
			String scorpid, String sdeptid, UFDate uDate, UFDouble nmny,
			UFDouble beforeNmny, String loginCorp) throws BusinessException {
		// TODO Auto-generated method stub
		if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
				.doubleValue())
			return;
		String vdef1 = null;
		FUNDSETVO vo = null;

		vdef1 = PuPubVO.getString_TrimZeroLenAsNull(getCustomer(scorpid));
		if (vdef1 == null)
			throw new BusinessException("客商档案与领料单位之间的关系不存在,请查看是否维护");
		vo = getFundSet(null, null, vdef1, uDate, ifundtype, false, loginCorp);

		if (vo == null) {
			return;
		}

		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// 总资金
		UFDouble oldnmny = PuPubVO.getUFDouble_NullAsZero(beforeNmny);// 修改前金额
		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// 预扣
		nlockfund = nlockfund.sub(oldnmny);
		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());// 实扣
		
		UFDouble nsubfund = nfund.sub(nlockfund).sub(nactfund)
				.sub(nmny);

		if (nsubfund.doubleValue() < 0) {
			throw new BusinessException("超" + HgPubTool.getSFundType(ifundtype)
					+ "控制");
		}

		vo.setNlockfund(nlockfund.add(nmny,HgPubConst.MNY_DIGIT));
		getDao().updateVO(vo, HgBsPubTool.CHECK_LOCkFUND_NAME);		

		//数据校验
		checkFundAfterUse(vo.getPrimaryKey(), ifundtype, null, uDate, loginCorp);
	}
	
	/**
	 *用于外销预扣(删除)
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-4-13下午05:26:12
	 * @param customerid
	 * @return
	 * @throws BusinessException
	 */
	public void reUseFund_before_SoOrder(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nmny, String loginCorp)
			throws BusinessException {
		// TODO Auto-generated method stub

		if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
				.doubleValue())
			return;
		
		String vdef1 = null;
		FUNDSETVO vo = null;

		vdef1 = PuPubVO.getString_TrimZeroLenAsNull(getCustomer(scorpid));
		if (vdef1 == null)
			throw new BusinessException("客商档案与领料单位之间的关系不存在,请查看是否维护");
		vo = getFundSet(null, null, vdef1, uDate, ifundtype, false, loginCorp);
		
		if (vo == null) {
			return;
		}

		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// 预扣

		if (nlockfund.doubleValue() < nmny.doubleValue())
			throw new BusinessException("数据错误，超预扣金额");

		nlockfund = nlockfund.sub(nmny);
		vo.setNlockfund(nlockfund);
		getDao().updateVO(vo, HgBsPubTool.CHECK_FUND_USENAMES);
	}
}

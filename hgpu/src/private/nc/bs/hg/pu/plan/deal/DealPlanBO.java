package nc.bs.hg.pu.plan.deal;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.hg.pu.check.FundLockBO;
import nc.bs.hg.pu.plan.pub.HgInvNumQueryBO;
import nc.bs.hg.pu.plan.pub.PlanPubBO;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.bs.hg.scm.pub.HgScmPubBO;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.hg.pu.plan.deal.PlanDealVO;
import nc.vo.hg.pu.plan.month.PlanOtherBVO;
import nc.vo.hg.pu.plan.year.PlanYearBVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.hg.pu.pub.PlanApplyInforVO;
import nc.vo.hg.pu.pub.PlanBVO;
import nc.vo.hg.pu.pub.PlanVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.trade.voutils.VOUtil;

public class DealPlanBO {

	private PlanPubBO bo = null;

	private PlanPubBO getPubBO() {
		if (bo == null) {
			bo = new PlanPubBO();
		}
		return bo;
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）计划处理查询功能 上级查询下级已上报的计划进行处理 2010-11-22下午05:00:00
	 * @param whereSql
	 * @param cl
	 * @return 下级上报的计划
	 * @throws BusinessException
	 */
	public PlanDealVO[] queryPlanForDeal(String whereSql, ClientLink cl,
			String splantype) throws BusinessException {

		// 获取查询的默认级次条件 以当前单位和部门 为 供货单位和供货部门的 计划
		String deWhereSql = getDefaultPlanWhereSql(cl.getCorp(), cl.getUser());
		// whereSql = whereSql+deWhereSql;
		if (PuPubVO.getString_TrimZeroLenAsNull(deWhereSql) == null)
			return null;
		StringBuffer strb = new StringBuffer();
		StringBuffer strb1 = new StringBuffer();
		StringBuffer strbc = new StringBuffer();
		StringBuffer strbs = new StringBuffer();

		strb
				.append(" select h.vbillno,h.pk_billtype,h.dbilldate,h.capplydeptid,h.capplypsnid,h.csupplydeptid"
						+ ",h.csupplycorpid,h.pk_corp,h.cyear,h.cmonth,h.caccperiodschemeid,h.cplanprojectid,h.cinvclassid,h.voperatorid ,h.dmakedate,h.vapproveid,h.dapprovedate ");

		SuperVO bodyvo = getBodyVO(splantype);

		String[] barrnames = bodyvo.getAttributeNames();// yearb vo 信息容量最大
														// 所以用yearb vo

		for (String name : barrnames) {
			if (name.equalsIgnoreCase("lsourceid")
					|| name.equalsIgnoreCase(bodyvo.getPKFieldName())
					|| "invcode".equalsIgnoreCase(name))
				continue;
			strb.append(",b." + name);
		}

		strb.append("," + bodyvo.getPKFieldName() + " pk_plan_b ");

		strb.append(" from hg_plan h inner join " + bodyvo.getTableName()
				+ " b on h.pk_plan = b.pk_plan ");
		String str = "inner join hg_new_materials inv on inv.pk_materials = b.vreserve1";
		String str1 = " inner join bd_invbasdoc inv on inv.pk_invbasdoc = b.pk_invbasdoc ";
		strb1.append(" inner join bd_invcl cl on cl.pk_invcl = inv.pk_invcl ");
		strb1.append(" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 ");
		if (PuPubVO.getString_TrimZeroLenAsNull(whereSql) != null)
			strb1.append(" and " + whereSql);
		strb1.append(deWhereSql);
		strb1.append(" and h.vbillstatus = " + IBillStatus.CHECKPASS);
		strb1.append(" and isnull(b.irowstatus,0) = "
				+ HgPubConst.PLAN_ROW_STATUS_FREE + "");// 未提交的物资行
		strb1.append(" and h.pk_billtype = '" + splantype + "'");

		// 用户角色存货分类权限过滤
		// String powersql = getPubBO().queryClassPowerSql("bd_invcl",
		// cl.getCorp(), cl.getUser());
		//		
		// if(PuPubVO.getString_TrimZeroLenAsNull(powersql)!=null)
		// strb1.append(" and cl.pk_invcl in ("+powersql+")");
		strbc.append(strb.toString() + str1 + strb1.toString());
		strbs.append(strb.toString() + str + strb1.toString());
		List ldata = (List) getPubBO().executeQuery(strbc.toString(),
				new BeanListProcessor(PlanDealVO.class));
		List ldata1 = (List) getPubBO().executeQuery(strbs.toString(),
				new BeanListProcessor(PlanDealVO.class));

		if (ldata == null || ldata.size() == 0) {
			if (ldata1 == null || ldata1.size() == 0)
				return null;
		}
		ldata.addAll(ldata1);
		PlanDealVO[] datas = (PlanDealVO[]) ldata.toArray(new PlanDealVO[0]);

		VOUtil.ascSort(datas, HgPubTool.DEAL_SORT_FIELDNAMES);
		return datas;
	}

	/**
	 * 
	 * @author lyf
	 * @说明：（鹤岗矿业）回写需求日期 2010-11-22下午01:29:46
	 * @param planinfor
	 * @param cl
	 * @throws BusinessException
	 */
	public void writeBack(List planinfor, Map map) throws BusinessException {
		if (planinfor == null || planinfor.size() == 0) {
			throw new BusinessException("传入计划数据为空");
		}
		PlanDealVO planvo = null;
		int len = planinfor.size();
		for (int i = 0; i < len; i++) {
			planvo = (PlanDealVO) planinfor.get(i);
			// pk_plan_b
			String sql = "update hg_planother_b set dreqdate='"
					+ planvo.getDreqdate().toString() + "',ts = '"
					+ map.get(planvo.getPk_plan_b())
					+ "' where pk_planother_b='" + planvo.getPk_plan_b() + "'";

			getPubBO().executeUpdate(sql);
		}
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）计划处理 计划上报 生成上级需求计划 2010-11-22下午01:29:46
	 * @param planinfor
	 * @param cl
	 * @throws BusinessException
	 */
	public void commitPlan(List planinfor, PlanApplyInforVO infor,
			String splantype,List lseldata) throws BusinessException {
		if (planinfor == null || planinfor.size() == 0) {
			throw new BusinessException("传入计划数据为空");
		}
		HgScmPubBO scmbo = new HgScmPubBO();
		// 并发控制并加锁
		String tablename, pkname;
		if (splantype.equalsIgnoreCase(HgPubConst.PLAN_YEAR_BILLTYPE)) {
			tablename = "hg_planyear_b";
			pkname = "pk_planyear_b";
		} else {
			tablename = "hg_planother_b";
			pkname = "pk_planother_b";
		}
		if (infor.getM_useObj() != null && (infor.getM_useObj() instanceof Map)) {
			try {
				scmbo.checkTime(tablename, pkname,
						(Map<String, UFDateTime>) infor.getM_useObj());
			} catch (SQLException se) {
				se.printStackTrace();
				throw new BusinessException(se);
			}
		}

		UFBoolean isLock = UFBoolean.FALSE;
		List<String> lids = new ArrayList<String>();
		PlanDealVO temp = null;
		int len = planinfor.size();
		for (int i = 0; i < len; i++) {
			temp = (PlanDealVO) planinfor.get(i);
			lids.addAll(temp.getLsourceid());
		}
		// add by zhw 2011-03-07
		
		if (HgPubConst.PLAN_MNY_BILLTYPE.equals(splantype)) {
			// 为专项资金计划资金定额预留
			FundLockBO bo = new FundLockBO();
				bo.LockFundForPlan(planinfor,lseldata);
		}
		// zhw end
		// PubDMO dmo = null;
		try {
			// dmo = new PubDMO();
			isLock = scmbo.lockPkForKey(infor.getM_sLogUser(), lids
					.toArray(new String[0]));

			// 校验 净需求数量=月份分量总和

			// 生成 计划单价
			HYBillVO[] billvos = null;
			try {
				billvos = splitAndGenPlan(planinfor, infor, splantype);
			} catch (Exception e) {
				if (e instanceof BusinessException) {
					e.printStackTrace();
					throw (BusinessException) e;
				} else {
					e.printStackTrace();
					throw new BusinessException(e);
				}
			}
			// 保存计划
			if (billvos == null || billvos.length == 0)
				return;
			getPubBO().pushSavePlans(billvos);

		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			throw new BusinessException(e);
		} finally {
			if (isLock.booleanValue()) {
				try {
					scmbo.freePkForKey(infor.getM_sLogUser(), lids
							.toArray(new String[0]));
				} catch (Exception e) {

				}
			}
		}
	}

	private HYBillVO[] splitAndGenPlan(List planinfor, PlanApplyInforVO infor,
			String splantype) throws Exception {
		if (planinfor == null || planinfor.size() == 0)
			return null;
		HashMap<String, ArrayList<SuperVO>> billInfor = new HashMap<String, ArrayList<SuperVO>>();
		ArrayList<SuperVO> ldata = new ArrayList<SuperVO>();

		PlanDealVO dealTmp = null;
		String stmp = null;
		int len = planinfor.size();

		// boolean isTranCorp = false;

		for (int i = 0; i < len; i++) {
			dealTmp = (PlanDealVO) planinfor.get(i);
			// 分单依据：上报公司+上报部门 + 存货分类+年+月+会计期间方案
			stmp = HgPubTool.getString_NullAsTrimZeroLen(dealTmp
					.getCsupplycorpid())
					+ HgPubTool.getString_NullAsTrimZeroLen(dealTmp
							.getCsupplydeptid())
					+ HgPubTool.getString_NullAsTrimZeroLen(dealTmp
							.getCinvclassid())
					+ HgPubTool.getString_NullAsTrimZeroLen(dealTmp.getCyear())
					+ HgPubTool
							.getString_NullAsTrimZeroLen(dealTmp.getCmonth())
					+ HgPubTool.getString_NullAsTrimZeroLen(dealTmp
							.getCaccperiodschemeid());
			if (billInfor.containsKey(stmp)) {
				ldata = billInfor.get(stmp);
			} else {
				ldata = new ArrayList<SuperVO>();
				ldata.add(getHeadVo(dealTmp, infor));
			}

			// isTranCorp =
			// dealTmp.getPk_corp().equalsIgnoreCase(dealTmp.getCsupplycorpid());

			ldata.add(getBodyVo(dealTmp, splantype, infor));
			billInfor.put(stmp, ldata);
		}

		if (billInfor.size() == 0)
			return null;

		HYBillVO[] billvos = new HYBillVO[billInfor.size()];
		HYBillVO tmpbill = null;
		int index = 0;
		for (String key : billInfor.keySet()) {
			ldata = billInfor.get(key);
			tmpbill = new HYBillVO();
			tmpbill.setParentVO(ldata.get(0));
			ldata.remove(0);
			SuperVO[] vos = ldata.toArray(new SuperVO[0]);
			HgPubTool.setVOsRowNoByRule(vos, HgPubConst.PLAN_BODY_ROWNO);
			tmpbill.setChildrenVO(vos);
			billvos[index] = tmpbill;
			index++;
		}

		return billvos;
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）计划平衡处理 2010-11-22下午05:25:27
	 * @param ldata
	 * @param cl
	 * @throws BusinessException
	 */
	public PlanDealVO[] balancePlan(PlanDealVO[] datas, ClientLink cl,
			String splantype) throws BusinessException {

		if (datas == null || datas.length == 0)
			return datas;

		// 五大量 当前库存 剩余资源 余月预耗 安全库存 库存不合用 前期消耗 上年年度消耗
		String plantype = datas[0].getPk_billtype();
		HgInvNumQueryBO bo = new HgInvNumQueryBO(getPubBO());
		String corpid = datas[0].getPk_corp();
		String sCalbodyid = datas[0].getCreqcalbodyid();
		AccountCalendar ac = AccountCalendar.getInstance();
		ac.setDate(cl.getLogonDate());
		UFDate uDate = ac.getLastMonthOfCurrentYear().getEnddate();
		ac.setDate(uDate);

		List<String> linvmanid = new ArrayList<String>();
		for (PlanDealVO data : datas) {
			if (linvmanid.contains(data.getCinventoryid()))
				continue;
			linvmanid.add(data.getCinventoryid());
		}
		if (linvmanid.size() == 0)
			return datas;
		String[] invmanids = linvmanid.toArray(new String[0]);

		Map<String, UFDouble> currentStoInfor = bo.getCurrentStock(corpid,
				sCalbodyid, ac, invmanids);

		Map<String, UFDouble> remainNumInfor = null;
		Map<String, UFDouble> preUseInfor = null;
		Map<String, UFDouble> useInfor = null;
		Map<String, UFDouble> lastYearUseInfor = null;

		if (!plantype.equalsIgnoreCase(HgPubConst.PLAN_YEAR_BILLTYPE)) {
			remainNumInfor = bo.getRemainNum(corpid, sCalbodyid, ac, invmanids);
			preUseInfor = bo.getMoreMonthsPreUseNum(corpid, sCalbodyid, ac,
					invmanids);
			useInfor = bo.getSumOutNum(corpid, sCalbodyid, invmanids, ac
					.getYearVO().getPeriodyear(), ac
					.getFirstMonthOfCurrentYear().getMonth(), ac.getMonthVO()
					.getMonth());
			lastYearUseInfor = bo.getSumOutNum(corpid, sCalbodyid, invmanids,
					ac.getYearVO().getPeriodyear(), null, null);
		}

		String key = null;
		for (PlanDealVO data : datas) {
			key = data.getCinventoryid();
			if (currentStoInfor != null) {
				data.setNonhandnum(currentStoInfor.get(key));
			}
			if (remainNumInfor != null) {
				data.setNreinnum(remainNumInfor.get(key));
			}
			if (preUseInfor != null) {
				data.setNreoutnum(preUseInfor.get(key));
			}
			if (useInfor != null) {
				data.setNusenum(useInfor.get(key));
			}
			if (lastYearUseInfor != null) {
				data.setNallusenum(lastYearUseInfor.get(key));
			}
		}
		return datas;
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）获取计划处理查询时 的默认条件 （级次过滤条件） 2010-11-22下午01:26:39
	 * @param sLogCorp
	 * @param sLogUser
	 * @return
	 * @throws BusinessException
	 */
	public String getDefaultPlanWhereSql(String sLogCorp, String sLogUser)
			throws BusinessException {
		String str = null;
		if (PuPubVO.getString_TrimZeroLenAsNull(sLogCorp) == null
				|| PuPubVO.getString_TrimZeroLenAsNull(sLogUser) == null)
			return null;
		// 获取系统集采公司设置
		String spocorp = getPubBO().getGatherPoCorp();
		if (PuPubVO.getString_TrimZeroLenAsNull(spocorp) == null)
			throw new BusinessException("系统未指定集采公司");
		// 判断当前登录公司=集采公司
		if (sLogCorp.equalsIgnoreCase(spocorp)) {
			// 获取以该集采公司为供货公司的单位
			// List lcorp = getReqCorp(spocorp);
			// List ldept = getPurDeptBycorp(lcorp);
			// str = " and h.pk_corp in
			// "+getPubBO().getSubSql((String[])lcorp.toArray(new String[0]))
			// +" and h.capplydeptid in
			// "+getPubBO().getSubSql((String[])ldept.toArray(new String[0]));

			// 按供货单位=当前公司的计划查询出来
			str = " and h.csupplycorpid = '" + sLogCorp + "'";

		} else {
			PlanApplyInforVO infor = getPubBO().getPlanAppInfor(sLogCorp,
					sLogUser);

			if (infor == null
					|| PuPubVO.getString_TrimZeroLenAsNull(infor
							.getCapplydeptid()) == null)
				throw new BusinessException("当前操作员未关联业务员");
			//
			// //判断当前登录公司<>集采公司 直接查询下级部门的需求计划 获取下级部门
			// List ldept = getNextDepts(infor);
			// if(ldept == null||ldept.size() == 0){//没有下级部门的 不能再次操作 查询结果应为空
			// return null;
			// }
			// str = " and h.pk_corp = '"+sLogCorp+"' and h.capplydeptid in
			// "+getPubBO().getSubSql((String[])ldept.toArray(new String[0]));
			str = " and h.csupplycorpid = '" + sLogCorp
					+ "' and h.csupplydeptid = '" + infor.getCapplydeptid()
					+ "'";
		}
		return str;
	}

	private List getPurDeptBycorp(List lcorp) throws BusinessException {
		// 获取各个公司下设置的采购部门
		String sql = "select pk_deptdoc from bd_deptdoc where pk_corp in "
				+ getPubBO().getSubSql((String[]) lcorp.toArray(new String[0]))
				+ " and pk_fathedept is null ";
		Object o = getPubBO()
				.executeQuery(sql, HgBsPubTool.COLUMNLISTPROCESSOR);
		return o == null ? null : (List) o;
	}

	private List getNextDepts(PlanApplyInforVO infor) throws BusinessException {
		String sql = "select pk_deptdoc from bd_deptdoc where pk_fathedept = '"
				+ infor.getCapplydeptid() + "'";
		List l = (List) getPubBO().executeQuery(sql,
				HgBsPubTool.COLUMNLISTPROCESSOR);
		return l;
	}

	private List getReqCorp(String upcorp) throws BusinessException {
		List lcorp = null;
		String sql = "select distinct creqcorp from scm_invsourcelist where isnull(dr,0)=0 and cbalancecorp = '"
				+ upcorp + "'";
		lcorp = (List) getPubBO().executeQuery(sql,
				HgBsPubTool.COLUMNLISTPROCESSOR);
		return lcorp;
	}

	private SuperVO getBodyVo(PlanDealVO dealvo, String splantype,
			PlanApplyInforVO infor) throws Exception {
		PlanBVO retVo = null;
		if (splantype.equalsIgnoreCase(HgPubConst.PLAN_YEAR_BILLTYPE))
			retVo = new PlanYearBVO();
		else
			retVo = new PlanOtherBVO();

		String[] arrnames = retVo.getAttributeNames();
		for (String arrname : arrnames) {
			retVo.setAttributeValue(arrname, dealvo.getAttributeValue(arrname));
		}

		if (infor.getM_pocorp().equalsIgnoreCase(infor.getM_sLogCorp())) {// 需要跨公司转换
																			// 存货id
			dealvo.setCinventoryid(getPubBO().transIDs(infor.getM_sLogCorp(),
					dealvo.getCinventoryid(), "bd_invmandoc", "pk_invbasdoc",
					"pk_invmandoc", "存货管理档案"));
		}

		retVo.setLsourceid(dealvo.getLsourceid());// 记录下来源信息

		retVo.setIrowstatus(HgPubConst.PLAN_ROW_STATUS_FREE);

		retVo.setStatus(VOStatus.NEW);
		retVo.setPrimaryKey(null);
		return retVo;
	}

	private PlanVO getHeadVo(PlanDealVO dealvo, PlanApplyInforVO infor)
			throws BusinessException {
		PlanVO head = new PlanVO();

		// 生成 上级的 计划表头
		head.setPk_corp(infor.getM_sLogCorp());
		head.setCreqcalbodyid(dealvo.getCreqcalbodyid());
		head.setCreqwarehouseid(dealvo.getCreqwarehouseid());
		head.setCapplydeptid(dealvo.getCapplydeptid());
		head.setCapplypsnid(dealvo.getCapplypsnid());
		head.setCaccperiodschemeid(dealvo.getCaccperiodschemeid());
		if (infor.getM_pocorp().equalsIgnoreCase(infor.getM_sLogCorp()))
			head.setCsupplycorpid(infor.getM_pocorp());
		else
			head.setCsupplycorpid(infor.getCsupplycorpid());
		head.setCsupplydeptid(dealvo.getCsupplycalbodyid());
		head.setStatus(VOStatus.NEW);
		head.setVbillstatus(IBillStatus.FREE);
		head.setDbilldate(infor.getM_uLogDate());
		head.setVbillno(HgPubTool.getBillNo(dealvo.getPk_billtype(), infor
				.getM_sLogCorp(), null, null));

		head.setCyear(dealvo.getCyear());
		head.setCmonth(dealvo.getCmonth());
		head.setCinvclassid(dealvo.getCinvclassid());
		head.setPk_billtype(dealvo.getPk_billtype());

		head.setCplanprojectid(dealvo.getCplanprojectid());

		head.setFisself(UFBoolean.FALSE);

		head.setDmakedate(infor.getM_uLogDate());
		head.setVoperatorid(infor.getM_sLogUser());
		return head;
	}

	private SuperVO getBodyVO(String splantype) {
		SuperVO retvo = null;
		if (splantype.equalsIgnoreCase(HgPubConst.PLAN_YEAR_BILLTYPE)) {
			retvo = new PlanYearBVO();
		} else
			retvo = new PlanOtherBVO();

		return retvo;
	}

	/**
	 * 如果没有到货日期 不能处理
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-2-24下午04:05:37
	 * @param lseldata
	 */
	public void valDreqdate(List<PlanDealVO> ldata) throws BusinessException {
		String sql = null;
		String sql1 = null;
		Object ob = null;
		Object[] o = null;
		int size = ldata.size();
		if (ldata == null || ldata.size() == 0)
			throw new BusinessException("传入数据出错");
		for (int i = 0; i < size; i++) {
			if (!ldata.get(i).getPk_billtype().equalsIgnoreCase(HgPubConst.PLAN_YEAR_BILLTYPE)) {
				sql = "select b.dreqdate,c.invcode,b.bisuse from hg_planother_b b join bd_invbasdoc c on c.pk_invbasdoc = b.pk_invbasdoc where b.pk_planother_b ='"
						+ ldata.get(i).getPk_plan_b()+ "' and isnull(b.dr,0)=0 ";
				sql1 = "select count(*) from hg_planother_b b inner join hg_new_materials inv on inv.pk_materials = b.vreserve1 where b.pk_planother_b ='"
						+ ldata.get(i).getPk_plan_b()+ "' and isnull(b.dr,0)=0 and isnull(inv.dr,0)=0 ";
				ob = getPubBO().executeQuery(sql, HgBsPubTool.ARRAYPROCESSOR);
				String len = PuPubVO.getString_TrimZeroLenAsNull(getPubBO().executeQuery(sql1, HgBsPubTool.COLUMNPROCESSOR));
				if (Integer.parseInt(len) > 0)
					throw new BusinessException("存在着新物资编码没有审批");
				if (ob == null)
					throw new BusinessException("该数据已不存,请重新查询");
				o = (Object[]) ob;

				if (o != null && o.length > 0) {
					String dreqdate = PuPubVO.getString_TrimZeroLenAsNull(o[0]);
					String vcode = PuPubVO.getString_TrimZeroLenAsNull(o[1]);
					if (dreqdate == null)
						throw new BusinessException("存货" + vcode
								+ "没有到货日期,请先回写到货日期");
				}
			}
		}
	}

}

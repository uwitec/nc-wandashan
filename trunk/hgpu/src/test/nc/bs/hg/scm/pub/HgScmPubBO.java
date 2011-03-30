package nc.bs.hg.scm.pub;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.itf.hg.pu.pub.IFundCheck;
import nc.itf.pu.pub.fw.LockTool;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.hg.pu.pub.LongTimeTask;
import nc.vo.hg.pu.check.allowance.ALLOWANCEVO;
import nc.vo.hg.pu.check.fund.FUNDSETVO;
import nc.vo.hg.pu.plan.month.PlanOtherBVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.hg.pu.pub.PlanVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pi.InvoiceHeaderVO;
import nc.vo.pi.InvoiceItemVO;
import nc.vo.pr.pray.PraybillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.to.pub.BillHeaderVO;
import nc.vo.to.pub.BillItemVO;
import nc.vo.to.pub.BillVO;
import nc.vo.trade.pub.IBillStatus;

public class HgScmPubBO {

	private IFundCheck fundbo = null;

	private IFundCheck getFundBO() {
		if (fundbo == null) {
			fundbo = (IFundCheck) NCLocator.getInstance().lookup(
					IFundCheck.class.getName());
		}
		return fundbo;
	}

	/**
	 * 作者：zhf 功能：锁单据 参数：AggregatedValueObject 返回：UFBoolean 例外： 创建日期：(2002-4-15
	 * 10:27:42) 修改日期，修改人，修改原因，注释标志： 2004-02-19 WYF 修改bean.remove()的处理
	 */
	public UFBoolean lockPkForKey(String sCuser, String[] saPk)
			throws Exception {

		boolean isLockSucc = false;
		// nc.bs.pub.lock.LockBOAccess boLock = new
		// nc.bs.pub.lock.LockBOAccess();
		try {

			// 操作员
			// String sCuser = null;
			// sCuser = (String) vo.getParentVO().getAttributeValue("cuserid");
			SCMEnv.out("正在为操作员[ID:" + sCuser + "]申请NC业务锁...");
			if (sCuser == null || sCuser.trim().equals(""))
				throw new nc.vo.pub.BusinessException(
						nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
								"4004pub", "UPP4004pub-000119")/*
																 * @res
																 * "获取操作员出错，不能进行相关操作！"
																 */);
			// String[] saPk = getPksForVo(vo);
			if (saPk != null) {
				// isLockSucc = boLock.lockPKArray(saPk, sCuser, "");
				isLockSucc = LockTool.setLockForPks(saPk, sCuser);
				if (isLockSucc) {
					SCMEnv.out("为操作员[ID:" + sCuser + "]申请NC业务锁成功。");
				} else {
					throw new nc.vo.pub.BusinessException(
							nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID("4004pub", "UPP4004pub-000129")/*
																				 * @res
																				 * "存在并发操作，请稍后再试"
																				 */);
				}
			}
			// 不需要枷锁的情况
			else {
				SCMEnv.out("为操作员[ID:" + sCuser + "]申请NC业务锁失败。失败原因：不需要枷锁的情况!");
			}
		} catch (Exception e) {
			SCMEnv.out(e);
			throw e;
		}
		return new UFBoolean(isLockSucc);
	}

	/**
	 * 作者：zhf 功能：释放单据锁 参数：AggregatedValueObject 返回：void 例外： 创建日期：(2002-4-15
	 * 10:27:42) 修改日期，修改人，修改原因，注释标志： 2004-02-19 WYF 修改bean.remove()的处理
	 */
	public void freePkForKey(String sCuser, String[] saPk) throws Exception {
		// 操作员
		// String sCuser = (String)
		// vo.getParentVO().getAttributeValue("cuserid");
		SCMEnv.out("正在为操作员[ID:" + sCuser + "]释放NC业务锁...");
		if (sCuser == null || sCuser.trim().equals(""))
			throw new nc.vo.pub.BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("4004pub", "UPP4004pub-000119")/*
																				 * @res
																				 * "获取操作员出错，不能进行相关操作！"
																				 */);
		// String[] saPk = getPksForVoFree(vo);

		// nc.bs.pub.lock.LockBOAccess boLock = new
		// nc.bs.pub.lock.LockBOAccess() ;
		try {
			if (saPk != null) {
				// boLock.freePKArrayByUser(saPk, sCuser, "");
				LockTool.releaseLockForPks(saPk, sCuser);
			}
			// lock.remove();
			SCMEnv.out("为操作员[ID:" + sCuser + "]释放NC业务锁成功结束。");
		} catch (Exception e) {
			SCMEnv.out("为操作员[ID:" + sCuser + "]释放NC业务锁异常结束。");
			// reportException(e);
			throw e;
		}
	}

	/**
	 * 作者：zhf 功能：锁单据 参数：AggregatedValueObject 返回：UFBoolean 例外： 创建日期：(2002-4-15
	 * 10:27:42) 修改日期，修改人，修改原因，注释标志： 2004-02-19 WYF 修改bean.remove()的处理
	 */
	public UFBoolean lockPkForVo(nc.vo.pub.AggregatedValueObject vo)
			throws Exception {

		boolean isLockSucc = false;
		// nc.bs.pub.lock.LockBOAccess boLock = new
		// nc.bs.pub.lock.LockBOAccess();
		try {

			// 操作员
			String sCuser = null;
			sCuser = (String) vo.getParentVO().getAttributeValue("cuserid");
			SCMEnv.out("正在为操作员[ID:" + sCuser + "]申请NC业务锁...");
			if (sCuser == null || sCuser.trim().equals(""))
				throw new nc.vo.pub.BusinessException(
						nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
								"4004pub", "UPP4004pub-000119")/*
																 * @res
																 * "获取操作员出错，不能进行相关操作！"
																 */);
			String[] saPk = getPksForVo(vo);
			if (saPk != null) {
				// isLockSucc = boLock.lockPKArray(saPk, sCuser, "");
				isLockSucc = LockTool.setLockForPks(saPk, sCuser);
				if (isLockSucc) {
					SCMEnv.out("为操作员[ID:" + sCuser + "]申请NC业务锁成功。");
				} else {
					throw new nc.vo.pub.BusinessException(
							nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID("4004pub", "UPP4004pub-000129")/*
																				 * @res
																				 * "存在并发操作，请稍后再试"
																				 */);
				}
			}
			// 不需要枷锁的情况
			else {
				SCMEnv.out("为操作员[ID:" + sCuser + "]申请NC业务锁失败。失败原因：不需要枷锁的情况!");
			}
		} catch (Exception e) {
			SCMEnv.out(e);
			throw e;
		}
		return new UFBoolean(isLockSucc);
	}

	/**
	 * 作者：zhf 功能：释放单据锁 参数：AggregatedValueObject 返回：void 例外： 创建日期：(2002-4-15
	 * 10:27:42) 修改日期，修改人，修改原因，注释标志： 2004-02-19 WYF 修改bean.remove()的处理
	 */
	public void freePkForVo(nc.vo.pub.AggregatedValueObject vo)
			throws Exception {
		// 操作员
		String sCuser = (String) vo.getParentVO().getAttributeValue("cuserid");
		SCMEnv.out("正在为操作员[ID:" + sCuser + "]释放NC业务锁...");
		if (sCuser == null || sCuser.trim().equals(""))
			throw new nc.vo.pub.BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("4004pub", "UPP4004pub-000119")/*
																				 * @res
																				 * "获取操作员出错，不能进行相关操作！"
																				 */);
		String[] saPk = getPksForVoFree(vo);

		// nc.bs.pub.lock.LockBOAccess boLock = new
		// nc.bs.pub.lock.LockBOAccess() ;
		try {
			if (saPk != null) {
				// boLock.freePKArrayByUser(saPk, sCuser, "");
				LockTool.releaseLockForPks(saPk, sCuser);
			}
			// lock.remove();
			SCMEnv.out("为操作员[ID:" + sCuser + "]释放NC业务锁成功结束。");
		} catch (Exception e) {
			SCMEnv.out("为操作员[ID:" + sCuser + "]释放NC业务锁异常结束。");
			// reportException(e);
			throw e;
		}
	}

	/**
	 * 作者：zhf 功能：公用算法,锁单据和释放单据锁时用于取所有需要加锁的ID(新增或修改单张单据) 参数：AggregatedValueObject
	 * 返回：String[] 例外： 创建日期：(2002-4-15 10:27:42) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return String[]
	 * @param vo
	 *            nc.vo.pub.AggregatedValueObject
	 */
	private String[] getPksForVo(nc.vo.pub.AggregatedValueObject vo)
			throws BusinessException, BusinessException, SQLException {
		String[] saRslt = null;
		// 合法性检查
		if (vo == null)
			return null;
		if (vo.getParentVO() == null
				&& (vo.getChildrenVO() == null || vo.getChildrenVO().length <= 0))
			return null;
		// Vector vTmp = new Vector();
		if (vo.getParentVO() == null) {
			SCMEnv.out("数据错误：无单据表头数据,加锁失败");
			return null;
		}
		// 单据ID向量
		Vector vPk = new Vector();
		// 单据新增标志
		boolean bIsNew = (vo.getParentVO().getPrimaryKey() == null || vo
				.getParentVO().getPrimaryKey().trim().equals(""));
		String sTmp = null;
		// 如果是新增单据
		if (bIsNew) {
			// 是否有参照的上层数据
			boolean bIsRef = false;
			if (vo.getChildrenVO() == null || vo.getChildrenVO().length <= 0) {
				throw new nc.vo.pub.BusinessException(
						nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
								"4004pub", "UPP4004pub-000122")/*
																 * @res
																 * "数据错误：新增的单据表体为空,加锁失败"
																 */);
			}
			for (int i = 0; i < vo.getChildrenVO().length; i++) {
				sTmp = (String) vo.getChildrenVO()[i]
						.getAttributeValue("cupsourcebillrowid");
				if (sTmp != null && !sTmp.trim().equals("")) {
					bIsRef = true;
					break;
				}
				sTmp = (String) vo.getChildrenVO()[i]
						.getAttributeValue("cupsourcebillid");
				if (sTmp != null && !sTmp.trim().equals("")) {
					bIsRef = true;
					break;
				}
			}
			// 如果所有单据行均为自制
			if (!bIsRef) {
				SCMEnv.out("单据行全部自制，不必加锁");
				return null;
			}
			for (int i = 0; i < vo.getChildrenVO().length; i++) {
				sTmp = (String) vo.getChildrenVO()[i]
						.getAttributeValue("cupsourcebillrowid");
				if (sTmp != null && !sTmp.trim().equals("")) {
					if (!vPk.contains(sTmp))
						vPk.addElement(sTmp);
				}
				sTmp = (String) vo.getChildrenVO()[i]
						.getAttributeValue("cupsourcebillid");

				if (sTmp == null || sTmp.trim().equals("")) {
					SCMEnv.out("存在数据错误：参照的单据上层行ID找不到匹配的上层头ID,加锁失败");
					return null;
				}
				if (!vPk.contains(sTmp)) {
					vPk.addElement(sTmp);
				}
			}
		}
		// 如果是修改单据
		else {
			sTmp = vo.getParentVO().getPrimaryKey();
			if (sTmp == null || sTmp.trim().equals("")) {
				SCMEnv.out("数据错误：找不到表头ID,加锁失败");
				return null;
			}
			vPk.addElement(sTmp);
			if (vo.getChildrenVO().length > 0) {
				for (int i = 0; i < vo.getChildrenVO().length; i++) {
					sTmp = vo.getChildrenVO()[i].getPrimaryKey();
					if (sTmp != null && sTmp.trim().length() > 0
							&& !vPk.contains(sTmp))
						vPk.addElement(sTmp);
				}
			}
		}
		if (vPk != null && vPk.size() > 0) {
			saRslt = new String[vPk.size()];
			vPk.copyInto(saRslt);
		}
		// 如果单据是新增
		return saRslt;

	}

	/**
	 * 作者：zhf 功能：公用算法,锁单据和释放单据锁时用于取所有需要加锁的ID(新增单张单据) 参数：AggregatedValueObject
	 * 返回：String[] 例外： 创建日期：(2002-4-15 10:27:42) 修改日期，修改人，修改原因，注释标志： 2002-05-17
	 * 王印芬 单据保存后不能解锁上层来源单据，修改为最大化集合
	 */
	private String[] getPksForVoFree(nc.vo.pub.AggregatedValueObject vo)
			throws BusinessException, BusinessException, SQLException {
		String[] saRslt = null;

		// 合法性检查
		if (vo == null)
			return null;

		// Vector vTmp = new Vector();
		if (vo.getParentVO() == null) {
			SCMEnv.out("数据错误：无单据表头数据,加锁失败");
			return null;
		}

		// 单据ID向量
		Vector vPk = new Vector();
		String sTmp = null;

		sTmp = vo.getParentVO().getPrimaryKey();
		if (sTmp != null && !sTmp.trim().equals("")) {
			vPk.addElement(sTmp);
		}

		if (vo.getChildrenVO() != null && vo.getChildrenVO().length > 0) {
			for (int i = 0; i < vo.getChildrenVO().length; i++) {
				sTmp = (String) vo.getChildrenVO()[i]
						.getAttributeValue("cupsourcebillrowid");
				if (sTmp != null && !sTmp.trim().equals("")) {
					if (!vPk.contains(sTmp))
						vPk.addElement(sTmp);
					sTmp = (String) vo.getChildrenVO()[i]
							.getAttributeValue("cupsourcebillid");
					if (sTmp == null || sTmp.trim().equals("")) {
						if (!(vo instanceof PraybillVO)) {
							throw new nc.vo.pub.BusinessException(
									nc.bs.ml.NCLangResOnserver.getInstance()
											.getStrByID("4004pub",
													"UPP4004pub-000123")/*
																		 * @res
																		 * "存在数据错误：参照的单据上层行ID找不到匹配的上层头ID,加锁失败"
																		 */);
						}
					}
					if (sTmp != null && !vPk.contains(sTmp)) {
						vPk.addElement(sTmp);
					}
				}

				sTmp = vo.getChildrenVO()[i].getPrimaryKey();
				if (sTmp != null && sTmp.trim().length() > 0
						&& !vPk.contains(sTmp))
					vPk.addElement(sTmp);

			}
		}

		if (vPk != null && vPk.size() > 0) {
			saRslt = new String[vPk.size()];
			vPk.copyInto(saRslt);
		}
		return saRslt;

	}

	// public void
	/**
	 * @author zhf
	 * @说明： 校验时间戳是否同步 不同步返回false
	 * @日期： 2009-11-12
	 */
	public void checkTime(String tablename, String pkname,
			java.util.Map<String, UFDateTime> tsInfor) throws SQLException,
			BusinessException {

		String sql = "select "
				+ pkname
				+ ",ts  from "
				+ tablename
				+ " where "
				+ pkname
				+ " in  "
				+ new TempTableUtil().getSubSql(tsInfor.keySet().toArray(
						new String[0]));

		Object o = getBaseDao().executeQuery(sql,
				HgBsPubTool.ARRAYLISTPROCESSOR);
		if (o == null) {
			throw new BusinessException("数据异常");
		}
		List l = (List) o;
		Iterator it = l.iterator();
		Object[] os = null;
		String tmpPK = null;
		String tmpTs = null;
		while (it.hasNext()) {
			os = (Object[]) it.next();
			tmpPK = PuPubVO.getString_TrimZeroLenAsNull(os[0]);
			tmpTs = PuPubVO.getString_TrimZeroLenAsNull(os[1]);
			if (!tmpTs.equalsIgnoreCase(PuPubVO
					.getString_TrimZeroLenAsNull(tsInfor.get(tmpPK)))) {
				throw new BusinessException("发生并发操作，请刷新界面再试");
			}
		}

	}

	/**
	 * @author liuys
	 * @说明：（鹤岗矿业）调拨订单与需求计划容差校验
	 * 
	 * 计划上数量 与订单上的数量 最后一次有容差 2011-1-10 下午01:45:20
	 * @param gbillvo
	 * @throws BusinessException
	 */
	public void checkAllowanceForTOOrder(BillVO inCurVOs)
			throws BusinessException {
		// 获取 容差设置 递归的 校验容差
		if (inCurVOs == null)
			return;
		BillItemVO[] items = (BillItemVO[]) inCurVOs.getChildrenVO();
		if (items == null || items.length == 0)
			return;

		// 订单可能出现多行相同存货情况, 故按存货 进行行 数量合并
		Map<String, BillItemVO> infor = new HashMap<String, BillItemVO>();
		BillItemVO tmpitem = null;
		for (BillItemVO item : items) {
			if (infor.containsKey(item.getCinvbasid())) {
				tmpitem = infor.get(item.getCinvbasid());
				tmpitem.setNnum(PuPubVO.getUFDouble_NullAsZero(
						tmpitem.getNnum()).add(
						PuPubVO.getUFDouble_NullAsZero(item.getNnum())));
			} else {
				tmpitem = (BillItemVO) item.clone();
			}
			infor.put(item.getCinvbasid(), tmpitem);
		}

		if (infor.size() == 0)
			return;

		UFDouble tmpNmny = null;
		UFDouble nnum = null;
		for (BillItemVO item : infor.values()) {
			// 得到计划容差比例
			tmpNmny = PuPubVO.getUFDouble_NullAsZero(getAllowanceSet(item
					.getCinvbasid(), ALLOWANCEVO.NPLAN, null));
			tmpNmny = tmpNmny.div(new UFDouble(100), 8);
			// 得到相应(临时计划,专项资金计划,月领用计划)计划的数量
			UFDouble plannum = PuPubVO
					.getUFDouble_NullAsZero(getAllPlanwanceSet(item
							.getCfirstbid()));
			nnum = PuPubVO.getUFDouble_NullAsZero(item.getNnum());
			// add by zhw 2011-01-12 21:15
			UFDouble oldnum = PuPubVO.getUFDouble_NullAsZero(getOldNumByPk(item
					.getCbill_bid()));

			// 容差比例未设置或=0 不进行容差控制
			if (tmpNmny.equals(UFDouble.ZERO_DBL)) {
				if (!(plannum.add(oldnum)).equals(nnum)) {
					// add by zhw 如果修改前的量大于当前量 不需要控制
					if (nnum.doubleValue() > (plannum.add(oldnum))
							.doubleValue())
						throw new BusinessException("存货" + item.getCinvcode()
								+ ",订单上数量超计划容差控制");
				}
			} else {
				if (nnum.doubleValue() < (plannum.add(oldnum)).doubleValue())// 不进行向下容差控制
					return;
				plannum = (plannum.add(oldnum));
				UFDouble nshouldnum = plannum.multiply(tmpNmny, 8);
				if (nnum.doubleValue() > nshouldnum.doubleValue()) {
					throw new BusinessException("存货" + item.getCinvcode()
							+ ",订单上数量超计划容差控制");
				}
			}
		}
	}

	/**
	 * 查询计划未领用的数量 zhf modify nnum-->nreserve10 20110211 月计划添加平衡数量
	 * 
	 * @param planpk
	 * @return
	 * @throws BusinessException
	 */
	private UFDouble getAllPlanwanceSet(String planpk) throws BusinessException {
		UFDouble tmpNnum = null;
		String sql = "select nreserve10,nnum,nouttotalnum from hg_planother_b hgb where hgb.dr=0 and hgb.pk_planother_b = '"
				+ planpk + "'";
		Object o = getBaseDao().executeQuery(sql,
				HgBsPubTool.ARRAYLISTPROCESSOR);
		if (o == null)
			return null;
		List<?> ldata = (List<?>) o;
		if (ldata == null || ldata.size() == 0)
			return null;
		Object ob = ldata.get(0);
		if (ob == null)
			return null;
		Object[] os = (Object[]) ldata.get(0);
		if (os.length == 0)
			return null;
		// 可领用数量 = 计划数量 - 累计领用数量
		UFDouble nnum1 = PuPubVO.getUFDouble_NullAsZero(os[0]);
		UFDouble nnum = nnum1.equals(UFDouble.ZERO_DBL) ? PuPubVO
				.getUFDouble_NullAsZero(os[1]) : nnum1;
		UFDouble nallnum = PuPubVO.getUFDouble_NullAsZero(os[2]);
		tmpNnum = nnum.sub(nallnum);
		return tmpNnum;
	}

	/**
	 * 查询出修改前的数量
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-1-12下午09:08:01
	 * @param pk
	 * @return
	 * @throws BusinessException
	 */
	private Object getOldNumByPk(String pk) throws BusinessException {
		String sql = "select nnum from to_bill_b where cbill_bid = '" + pk
				+ "' and isnull(dr,0)=0";
		Object o = getBaseDao().executeQuery(sql,
				HgBsPubTool.ARRAYLISTPROCESSOR);
		if (o == null)
			return null;
		ArrayList al = (ArrayList) o;
		if (al == null || al.size() == 0)
			return null;
		Object[] o1 = (Object[]) al.get(0);
		return o1[0];
	}

	/**
	 * 
	 * @author liuys
	 * @说明：（鹤岗矿业）入库容差校验 2011-4-8下午01:45:20
	 * @param gbillvo
	 * @throws BusinessException
	 */
	public void checkAllowanceForICin(GeneralBillVO gbillvo)
			throws BusinessException {
		// 获取 容差设置 递归的 校验容差
		if (gbillvo == null)
			return;
		GeneralBillItemVO[] items = gbillvo.getItemVOs();
		if (items == null || items.length == 0)
			return;

		// 按存货 进行行 数量合并
		Map<String, GeneralBillItemVO> infor = new HashMap<String, GeneralBillItemVO>();
		GeneralBillItemVO tmpitem = null;
		for (GeneralBillItemVO item : items) {
			if (infor.containsKey(item.getCinvbasid())) {
				tmpitem = infor.get(item.getCinvbasid());
				tmpitem
						.setNshouldinnum(PuPubVO.getUFDouble_NullAsZero(
								tmpitem.getNshouldinnum()).add(
								PuPubVO.getUFDouble_NullAsZero(item
										.getNshouldinnum())));
				tmpitem.setNinnum(PuPubVO.getUFDouble_NullAsZero(
						tmpitem.getNinnum()).add(
						PuPubVO.getUFDouble_NullAsZero(item.getNinnum())));
			} else {
				tmpitem = (GeneralBillItemVO) item.clone();
			}
			infor.put(item.getCinvbasid(), tmpitem);
		}

		if (infor.size() == 0)
			return;

		UFDouble tmpNmny = null;
		UFDouble nshouldnum = null;
		UFDouble ninnum = null;
		for (GeneralBillItemVO item : infor.values()) {
			tmpNmny = PuPubVO.getUFDouble_NullAsZero(getAllowanceSet(item
					.getCinvbasid(), ALLOWANCEVO.NARRIVAL, null));
			tmpNmny = tmpNmny.div(new UFDouble(100), 8);
			nshouldnum = PuPubVO.getUFDouble_NullAsZero(item.getNshouldinnum());
			if(nshouldnum.compareTo(UFDouble.ZERO_DBL)<0)//如果数量小于零 则为退库
				return;
			ninnum = PuPubVO.getUFDouble_NullAsZero(item
					.getAttributeValue(HgPubConst.NUM_DEF_FAC));
			// ==0 不进行容差控制
			if (tmpNmny.equals(UFDouble.ZERO_DBL)) {
				if (!nshouldnum.equals(ninnum))
					if (ninnum.doubleValue() > nshouldnum.doubleValue())
						throw new BusinessException("存货"
								+ item.getCinventorycode() + ",实际到货超容差控制");
			} else {
				if (ninnum.doubleValue() < nshouldnum.doubleValue())// 不进行向下容差控制
					return;
				nshouldnum = nshouldnum.multiply(tmpNmny, 8);
				if (ninnum.doubleValue() > nshouldnum.doubleValue()) {
					throw new BusinessException("存货" + item.getCinventorycode()
							+ ",实际到货超容差控制");
				}
			}
		}
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）出库容差校验 2011-4-8下午01:45:20
	 * @param gbillvo
	 * @throws BusinessException
	 */
	public void checkAllowanceForICout(GeneralBillVO gbillvo)
			throws BusinessException {
		// 获取 容差设置 递归的 校验容差
		if (gbillvo == null)
			return;
		GeneralBillItemVO[] items = gbillvo.getItemVOs();
		if (items == null || items.length == 0)
			return;

		// 按存货 进行行 数量合并
		Map<String, GeneralBillItemVO> infor = new HashMap<String, GeneralBillItemVO>();
		GeneralBillItemVO tmpitem = null;
		for (GeneralBillItemVO item : items) {
			if (infor.containsKey(item.getCinvbasid())) {
				tmpitem = infor.get(item.getCinvbasid());
				tmpitem.setNshouldoutnum(PuPubVO.getUFDouble_NullAsZero(
						tmpitem.getNshouldoutnum())
						.add(
								PuPubVO.getUFDouble_NullAsZero(item
										.getNshouldoutnum())));
				tmpitem.setNoutnum(PuPubVO.getUFDouble_NullAsZero(
						tmpitem.getNoutnum()).add(
						PuPubVO.getUFDouble_NullAsZero(item.getNoutnum())));
			} else {
				tmpitem = (GeneralBillItemVO) item.clone();
			}
			infor.put(item.getCinvbasid(), tmpitem);
		}

		if (infor.size() == 0)
			return;

		UFDouble tmpNmny = null;
		UFDouble nshouldnum = null;
		UFDouble noutnum = null;
		for (GeneralBillItemVO item : infor.values()) {
			tmpNmny = PuPubVO.getUFDouble_NullAsZero(getAllowanceSet(item
					.getCinvbasid(), ALLOWANCEVO.NOUTBOUND, null));
			tmpNmny = tmpNmny.div(new UFDouble(100), 8);
			nshouldnum = PuPubVO
					.getUFDouble_NullAsZero(item.getNshouldoutnum());
			noutnum = PuPubVO.getUFDouble_NullAsZero(item.getNoutnum());
			// ==0 不进行容差控制
			if (tmpNmny.equals(UFDouble.ZERO_DBL)) {
				if (!nshouldnum.equals(noutnum))
					if (noutnum.doubleValue() > nshouldnum.doubleValue())
						throw new BusinessException("存货"
								+ item.getCinventorycode() + "实际出库超容差控制");
			} else {
				if (noutnum.doubleValue() < nshouldnum.doubleValue())// 不进行向下容差控制
					return;
				nshouldnum = nshouldnum.multiply(tmpNmny, 8);
				if (noutnum.doubleValue() > nshouldnum.doubleValue()) {
					throw new BusinessException("存货" + item.getCinventorycode()
							+ "实际出库超容差控制");
				}
			}
		}
	}

	private String getAllotInCorp(String cbillhid) throws BusinessException {
		String sql = " select cincorpid from to_bill were cbillid = '"
				+ cbillhid + "'";
		return PuPubVO.getString_TrimZeroLenAsNull(getBaseDao().executeQuery(
				sql, HgBsPubTool.COLUMNPROCESSOR));
	}

	public Map<String, String> getAlloOrderSoubid(String[] cbillbids)
			throws BusinessException {
		if (cbillbids == null || cbillbids.length == 0)
			return null;
		String sql = "select cbill_bid,csourcebid from to_bill_b where isnull(dr,0)=0 "
				+ " and (csourcetypecode='HG02' or csourcetypecode='HG03' or csourcetypecode='HG04') and cbill_bid in "
				+ HgPubTool.getSubSql(cbillbids);
		Object o = getBaseDao().executeQuery(sql,
				HgBsPubTool.ARRAYLISTPROCESSOR);
		if (o == null)
			return null;
		List ldata = (List) o;
		if (ldata.size() == 0)
			return null;
		Iterator it = ldata.iterator();
		Object[] os = null;
		Map<String, String> retMap = new HashMap<String, String>();
		while (it.hasNext()) {
			os = (Object[]) it.next();
			retMap.put(HgPubTool.getString_NullAsTrimZeroLen(os[0]), HgPubTool
					.getString_NullAsTrimZeroLen(os[1]));
		}
		return retMap;
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业） 2011-4-8下午03:37:24
	 * @param l5xbids
	 * @return Map<String, String[]> planidInfor
	 * @throws BusinessException
	 */
	private Object getPlanTypeFor4Y(List<String> l5xbids)
			throws BusinessException {

		String sql = " select cbill_bid,csourceid,cincorpid,csourcetypecode from to_bill_b where cbill_bid  in "
				+ HgPubTool.getSubSql(l5xbids.toArray(new String[0]));

		Object o = getBaseDao().executeQuery(sql,
				HgBsPubTool.ARRAYLISTPROCESSOR);
		if (o == null)
			return null;
		List l = (List) o;
		Object[] os = null;
		Map<String, String[]> planInfor = new HashMap<String, String[]>();
		String[] tmpS = null;
		String splantypecode = PuPubVO
				.getString_TrimZeroLenAsNull(((Object[]) (l.get(0)))[3]);
		if (splantypecode == null
				|| HgPubTool.getIFundTypeByPlanType(splantypecode) == HgPubConst.FUND_CHECK_ERRORTYPE)
			return null;
		for (int i = 0; i < l.size(); i++) {
			os = (Object[]) l.get(i);
			tmpS = new String[] { os[1].toString(), os[2].toString() };
			planInfor.put(os[0].toString(), tmpS);
		}
		return new Object[] { planInfor,
				HgPubTool.getIFundTypeByPlanType(splantypecode) };
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）销售出库单签字 实扣 2012-2-19下午09:36:20
	 * @param gbillvo
	 * @param flag
	 * @throws BusinessException
	 */
	public void checkAndUseFundForSaleOut(GeneralBillVO gbillvo, boolean flag)
			throws BusinessException {
		if (gbillvo == null)
			return;
		GeneralBillHeaderVO head = gbillvo.getHeaderVO();
		GeneralBillItemVO[] items = gbillvo.getItemVOs();
		if (items == null
				|| items.length == 0
				|| PuPubVO.getString_TrimZeroLenAsNull(items[0]
						.getCsourcetype()) == null)
			return; // 不支持自制销售出库单 正常 不会 自制销售出库单

		// 从来源销售订单 获取 含税单价 和 含税金额 一销售订单对应一张销售出库单 1:1
		List<String> lsoorderid = new ArrayList<String>();
		for (GeneralBillItemVO item : items) {
			if (!HgPubTool.getString_NullAsTrimZeroLen(item.getCsourcetype())
					.equalsIgnoreCase(ScmConst.SO_Order))
				continue;
			if (lsoorderid.contains(item.getCsourcebillbid()))
				continue;
			lsoorderid.add(item.getCsourcebillbid());
		}
		if (lsoorderid.size() == 0)
			return;

		Map<String, UFDouble[]> soOrderInfor = getSoOrderInfor(lsoorderid);

		if (soOrderInfor.size() == 0)
			return;

		UFDate uDate = head.getDauditdate();
		if (HgPubConst.IS_DBILLDATE_WHEN_USE) {
			uDate = head.getDbilldate();
		}

		UFDouble nmny = UFDouble.ZERO_DBL;
		UFDouble nbeforemny = UFDouble.ZERO_DBL;

		UFDouble[] tmpMny = null;
		for (GeneralBillItemVO item : items) {
			tmpMny = soOrderInfor.get(item.getCsourcebillbid());
			nbeforemny = nbeforemny.add(PuPubVO
					.getUFDouble_NullAsZero(tmpMny[1]));
			nmny = nmny.add(PuPubVO.getUFDouble_NullAsZero(tmpMny[0]).multiply(
					item.getNoutnum(), HgPubConst.MNY_DIGIT));
		}

		if (flag) {
			getFundBO().useFund(null, HgPubConst.FUND_CHECK_FUND, null,
					head.getCcustomerid(), uDate, nbeforemny, nmny,
					head.getPrimaryKey(), head.getCbilltypecode());
			getFundBO().useFund(null, HgPubConst.FUND_CHECK_QUATO, null,
					head.getCcustomerid(), uDate, nbeforemny, nmny,
					head.getPrimaryKey(), head.getCbilltypecode());
		} else {
			getFundBO().reUseFund(null, HgPubConst.FUND_CHECK_FUND, null,
					head.getCcustomerid(), uDate, nbeforemny, nmny);
			getFundBO().reUseFund(null, HgPubConst.FUND_CHECK_QUATO, null,
					head.getCcustomerid(), uDate, nbeforemny, nmny);
		}
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）从来源销售订单 获取 含税单价 和 含税金额 一销售订单对应一张销售出库单 1:1 2012-2-20上午10:29:20
	 * @param lsoorderid
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, UFDouble[]> getSoOrderInfor(List<String> lsoorderid)
			throws BusinessException {
		String sql = " select corder_bid,ntaxnetprice,nsummny from so_saleorder_b where corder_bid in "
				+ HgPubTool.getSubSql(lsoorderid.toArray(new String[0]));
		Object o = getBaseDao().executeQuery(sql,
				HgBsPubTool.ARRAYLISTPROCESSOR);
		if (o == null)
			return null;
		List ldata = (List) o;
		if (ldata.size() == 0)
			return null;
		int len = ldata.size();
		Iterator it = ldata.iterator();
		Map<String, UFDouble[]> retMap = new HashMap<String, UFDouble[]>();
		UFDouble[] tmps = null;
		String key = null;
		Object[] os = null;
		while (it.hasNext()) {
			os = (Object[]) it.next();
			key = HgPubTool.getString_NullAsTrimZeroLen(os[0]);
			tmps = new UFDouble[] { PuPubVO.getUFDouble_NullAsZero(os[1]),
					PuPubVO.getUFDouble_NullAsZero(os[2]) };
			retMap.put(key, tmps);
		}
		return retMap;
	}

	public void checkAndUseFundForMaterialOut(GeneralBillVO gbillvo,
			boolean flag) throws BusinessException {
		if (gbillvo == null)
			return;
		GeneralBillHeaderVO head = gbillvo.getHeaderVO();
		GeneralBillItemVO[] items = gbillvo.getItemVOs();
		if (items == null || items.length == 0)
			return;
		// add by zhw 2010-12-28
		String cdptid = PuPubVO.getString_TrimZeroLenAsNull(head
				.getAttributeValue("pk_defdoc5"));// 领料部门
		// add by zhw 2010-12-28 end
		UFDate uDate = head.getDauditdate();
		if (HgPubConst.IS_DBILLDATE_WHEN_USE) {
			uDate = head.getDbilldate();
		}

		// 实扣 没有预扣 考虑是否来源于计划 如果来源于计划 考虑计划的预留问题
		String pk_plan = PuPubVO.getString_TrimZeroLenAsNull(items[0]
				.getCsourcebillhid());// 暂时认为一个 出库单 来源于一种计划 且是 一个计划单

		UFDouble nmny = UFDouble.ZERO_DBL;
		for (GeneralBillItemVO item : items) {
			nmny = nmny.add(PuPubVO.getUFDouble_NullAsZero(item
					.getNplannedmny()));
		}

		int ifundtype = HgPubTool.getIFundTypeByPlanType(items[0]
				.getCsourcetype());

		if (ifundtype == HgPubConst.FUND_CHECK_ERRORTYPE) {// 无计划扣
			// add by zhw 2010-12-28 用于材料出库单自制
			if (flag) {
				getFundBO().useFundMater(null, HgPubConst.FUND_CHECK_FUND,
						null, cdptid, uDate, UFDouble.ZERO_DBL, nmny,
						head.getPrimaryKey(), head.getCbilltypecode());
				getFundBO().useFundMater(null, HgPubConst.FUND_CHECK_QUATO,
						null, cdptid, uDate, UFDouble.ZERO_DBL, nmny,
						head.getPrimaryKey(), head.getCbilltypecode());
			} else {
				getFundBO().reUseFundMater(null, HgPubConst.FUND_CHECK_FUND,
						null, cdptid, uDate, UFDouble.ZERO_DBL, nmny);
				getFundBO().reUseFundMater(null, HgPubConst.FUND_CHECK_QUATO,
						null, cdptid, uDate, UFDouble.ZERO_DBL, nmny);
			}
			// add by zhw 2010-12-28 用于材料出库单自制 end
		} else if (ifundtype == HgPubConst.FUND_CHECK_SPECIALFUND) {// 专项资金 扣
			if (flag)
				getFundBO().useFund(pk_plan, ifundtype, head.getPk_corp(),
						cdptid, uDate, UFDouble.ZERO_DBL, nmny,
						head.getPrimaryKey(), head.getCbilltypecode());
			else
				getFundBO().reUseFund(pk_plan, ifundtype, head.getPk_corp(),
						cdptid, uDate, UFDouble.ZERO_DBL, nmny);
		} else if (ifundtype == HgPubConst.FUND_CHECK_FUND_QUATO) {// 资金 限额 扣
			if (flag) {
				getFundBO().useFund(pk_plan, HgPubConst.FUND_CHECK_FUND,
						head.getPk_corp(), cdptid, uDate, UFDouble.ZERO_DBL,
						nmny, head.getPrimaryKey(), head.getCbilltypecode());
				getFundBO().useFund(pk_plan, HgPubConst.FUND_CHECK_QUATO,
						head.getPk_corp(), cdptid, uDate, UFDouble.ZERO_DBL,
						nmny, head.getPrimaryKey(), head.getCbilltypecode());
			} else {
				getFundBO().reUseFund(pk_plan, HgPubConst.FUND_CHECK_FUND,
						head.getPk_corp(), cdptid, uDate, UFDouble.ZERO_DBL,
						nmny);
				getFundBO().reUseFund(pk_plan, HgPubConst.FUND_CHECK_QUATO,
						head.getPk_corp(), cdptid, uDate, UFDouble.ZERO_DBL,
						nmny);
			}
		}
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）实扣/反实扣 用于来源于计划的内销出库 2011-4-8下午01:44:15
	 * @param gbillvo
	 * @param flag
	 * @param isYK
	 * @throws BusinessException
	 */
	public void checkAndUseFund(GeneralBillVO gbillvo, boolean flag,
			boolean isYK) throws BusinessException {

		if (gbillvo == null)
			return;
		GeneralBillHeaderVO head = gbillvo.getHeaderVO();
		GeneralBillItemVO[] items = gbillvo.getItemVOs();
		if (items == null
				|| items.length == 0
				|| PuPubVO.getString_TrimZeroLenAsNull(items[0]
						.getCsourcetype()) == null)
			return;
		List<String> l5xbids = new ArrayList<String>();
		// String tmp =null;
		// modify by zhw 2011-01-08 调拨订单拉式生成调拨出库
		for (GeneralBillItemVO item : items) {
			// tmp = PuPubVO.getString_TrimZeroLenAsNull(item.getCsourcetype());
			// if(!(tmp.equalsIgnoreCase(HgPubConst.PLAN_MNY_BILLTYPE)||tmp.equalsIgnoreCase(HgPubConst.PLAN_TEMP_BILLTYPE)
			// ||tmp.equalsIgnoreCase(HgPubConst.PLAN_MONTH_BILLTYPE)))
			// continue;
			if (l5xbids.contains(item.getCsourcebillbid()))
				continue;
			l5xbids.add(item.getCsourcebillbid());
		}

		if (l5xbids.size() == 0)
			return;

		// 根据来源调拨订单id 获取 来源 计划头id 和 来源 计划类型
		/**
		 * 注意 订单和计划不是一一对应 1:N 但同一订单的来源计划类型是相同的
		 */
		Object o = getPlanTypeFor4Y(l5xbids);

		if (o == null)
			return;
		int ifundtype = Integer.parseInt((((Object[]) o)[1]).toString());
		// int ifundtype = HgPubTool.getIFundTypeByPlanType(splantype);
		if (ifundtype == HgPubConst.FUND_CHECK_ERRORTYPE) {
			checkAndUseFund_NoPlan(gbillvo, flag, isYK);// 非来源于计划的 调拨出库
			return;
		}

		Map<String, String[]> planidInfor = (Map<String, String[]>) ((Object[]) o)[0];

		UFDate uDate = head.getDauditdate();
		if (HgPubConst.IS_DBILLDATE_WHEN_USE) {
			uDate = head.getDbilldate();
		}
		String soubid = null;
		String[] inInfor = null;
		String newkey = null;
		UFDouble nmny = UFDouble.ZERO_DBL;
		UFDouble nbeforemny = UFDouble.ZERO_DBL;
		Map<String, UFDouble> planmny = new HashMap<String, UFDouble>();
		Map<String, UFDouble> planbeforemny = null;

		planbeforemny = new HashMap<String, UFDouble>();

		for (GeneralBillItemVO item : items) {
			soubid = item.getCsourcebillbid();
			inInfor = planidInfor.get(soubid);
			newkey = inInfor[0] + "," + inInfor[1];
			if (planmny.containsKey(newkey)) {
				nmny = planmny.get(newkey);
			}
			if (isYK) {
				if (planbeforemny.containsKey(newkey)) {
					nbeforemny = planbeforemny.get(newkey);
				}
			}
			nmny = nmny.add(PuPubVO.getUFDouble_NullAsZero(item
					.getNplannedmny()));
			if (isYK) {
				nbeforemny = nbeforemny.add(PuPubVO.getUFDouble_NullAsZero(
						item.getNplannedprice())
						.multiply(
								PuPubVO.getUFDouble_NullAsZero(item
										.getNshouldoutnum()), 8));
			}
			planmny.put(newkey, nmny);
			planbeforemny.put(newkey, nbeforemny);
		}

		if (planmny.size() == 0)
			return;
		if (isYK && planbeforemny.size() == 0)
			return;

		for (String plankey : planmny.keySet()) {
			nmny = planmny.get(plankey);
			nbeforemny = planbeforemny.get(plankey);
			inInfor = plankey.split(",");
			if (ifundtype == HgPubConst.FUND_CHECK_SPECIALFUND) {
				if (flag) {
					getFundBO().useFund(inInfor[0], ifundtype, inInfor[1],
							null, uDate, nbeforemny, nmny,
							head.getPrimaryKey(), head.getCbilltypecode());
				} else {
					getFundBO().reUseFund(inInfor[0], ifundtype, inInfor[1],
							null, uDate, nbeforemny, nmny);
				}
			} else if (ifundtype == HgPubConst.FUND_CHECK_FUND_QUATO) {
				if (flag) {
					getFundBO().useFund(inInfor[0], HgPubConst.FUND_CHECK_FUND,
							inInfor[1], null, uDate, nbeforemny, nmny,
							head.getPrimaryKey(), head.getCbilltypecode());
					getFundBO().useFund(inInfor[0],
							HgPubConst.FUND_CHECK_QUATO, inInfor[1], null,
							uDate, nbeforemny, nmny, head.getPrimaryKey(),
							head.getCbilltypecode());
				} else {
					getFundBO().reUseFund(inInfor[0],
							HgPubConst.FUND_CHECK_FUND, inInfor[1], null,
							uDate, nbeforemny, nmny);
					getFundBO().reUseFund(inInfor[0],
							HgPubConst.FUND_CHECK_QUATO, inInfor[1], null,
							uDate, nbeforemny, nmny);
				}
			}
		}
	}

	public void checkAndUseFund_NoPlan(GeneralBillVO gbillvo, boolean flag,
			boolean isYK) throws BusinessException {

		if (gbillvo == null)
			return;
		GeneralBillHeaderVO head = gbillvo.getHeaderVO();
		GeneralBillItemVO[] items = gbillvo.getItemVOs();
		if (items == null || items.length == 0)
			return;
		UFDouble nmny = UFDouble.ZERO_DBL;
		UFDouble nbeforemny = UFDouble.ZERO_DBL;
		for (GeneralBillItemVO item : items) {
			nmny = nmny.add(PuPubVO.getUFDouble_NullAsZero(item
					.getNplannedmny()));
			nbeforemny = nbeforemny
					.add(PuPubVO.getUFDouble_NullAsZero(item.getNshouldinnum())
							.multiply(
									PuPubVO.getUFDouble_NullAsZero(item
											.getNplannedprice()), 8));
		}

		if (!isYK)
			nbeforemny = UFDouble.ZERO_DBL;

		UFDate uDate = head.getDauditdate();
		if (HgPubConst.IS_DBILLDATE_WHEN_USE) {
			uDate = head.getDbilldate();
		}
		String cincorp = getAllotInCorp(items[0].getCsourcebillhid());

		if (flag) {
			getFundBO().useFund(null, HgPubConst.FUND_CHECK_FUND, cincorp,
					null, uDate, nbeforemny, nmny, head.getPrimaryKey(),
					head.getCbilltypecode());
			getFundBO().useFund(null, HgPubConst.FUND_CHECK_QUATO, cincorp,
					null, uDate, nbeforemny, nmny, head.getPrimaryKey(),
					head.getCbilltypecode());
		} else {
			getFundBO().reUseFund(null, HgPubConst.FUND_CHECK_FUND, cincorp,
					null, uDate, nbeforemny, nmny);
			getFundBO().reUseFund(null, HgPubConst.FUND_CHECK_QUATO, cincorp,
					null, uDate, nbeforemny, nmny);
		}
	}

	public void checkAndUseFund_before_NoPlan(BillVO billvo, boolean flag)
			throws BusinessException {
		if (billvo == null) {
			return;
		}
		BillHeaderVO head = billvo.getHeaderVO();
		BillItemVO[] items = billvo.getItemVOs();
		if (items == null || items.length == 0)
			return;

		UFDouble nmny = UFDouble.ZERO_DBL;
		for (BillItemVO item : items) {
			nmny = nmny
					.add(PuPubVO.getUFDouble_NullAsZero(item.getNnotaxmny()));
		}

		UFDate uDate = head.getDauditdate();
		if (HgPubConst.IS_DBILLDATE_WHEN_USE) {
			uDate = head.getDbilldate();
		}

		if (nmny.doubleValue() == UFDouble.ZERO_DBL.doubleValue())
			return;

		if (flag) {
			getFundBO().useFund_Before(null, HgPubConst.FUND_CHECK_FUND,
					head.getCincorpid(), null, uDate, nmny,
					head.getPrimaryKey(), head.getCbilltype());
			getFundBO().useFund_Before(null, HgPubConst.FUND_CHECK_QUATO,
					head.getCincorpid(), null, uDate, nmny,
					head.getPrimaryKey(), head.getCbilltype());
		} else {
			getFundBO().reUseFund_before(null, HgPubConst.FUND_CHECK_FUND,
					head.getCincorpid(), null, uDate, nmny);
			getFundBO().reUseFund_before(null, HgPubConst.FUND_CHECK_QUATO,
					head.getCincorpid(), null, uDate, nmny);
		}
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）预扣 2011-4-8下午01:44:31
	 * @param billvo
	 * @param flag
	 *            预扣/反预扣
	 * @throws BusinessException
	 */
	public void checkAndUseFund_before(BillVO billvo, boolean flag)
			throws BusinessException {
		if (billvo == null)
			return;
		BillHeaderVO head = billvo.getHeaderVO();
		BillItemVO[] items = billvo.getItemVOs();
		if (items == null || items.length == 0)
			return;

		if (PuPubVO.getString_TrimZeroLenAsNull(items[0].getCsourcetypecode()) == null) {
			// 自制单据 无计划 预拨
			checkAndUseFund_before_NoPlan(billvo, flag);
			return;
		}

		int ifundtype = HgPubTool.getIFundTypeByPlanType(items[0]
				.getCsourcetypecode());
		if (ifundtype == HgPubConst.FUND_CHECK_ERRORTYPE)
			return;

		String scorpid = head.getCincorpid();

		UFDate uDate = head.getDauditdate();
		if (HgPubConst.IS_DBILLDATE_WHEN_USE) {
			uDate = head.getDbilldate();// 这取的日期 是当前日期 而不是会计时间的日期
		}

		String pk_plan = null;
		UFDouble nmny = UFDouble.ZERO_DBL;
		Map<String, UFDouble> planmny = new HashMap<String, UFDouble>();
		int len = items.length;
		Object[][] o = new Object[len][2];

		for (int i = 0; i < len; i++) {
			BillItemVO item = items[i];
			pk_plan = item.getCsourceid();
			if (planmny.containsKey(pk_plan)) {
				nmny = planmny.get(pk_plan);
			}
			nmny = nmny
					.add(PuPubVO.getUFDouble_NullAsZero(item.getNnotaxmny()));
			o[i][0] = item.getCsourcebid();
			o[i][1] = item.getNnotaxmny();
			planmny.put(pk_plan, nmny);
		}

		if (planmny.size() == 0)
			return;

		for (String plankey : planmny.keySet()) {
			nmny = planmny.get(plankey);
			if (ifundtype == HgPubConst.FUND_CHECK_SPECIALFUND) {
				if (flag) {
					getFundBO().useSpeacialFund_Before(plankey, ifundtype,
							scorpid, null, uDate, nmny, head.getPrimaryKey(),
							head.getCbilltype(), o);
				} else {
					getFundBO().reUseFund_before(plankey, ifundtype, scorpid,
							null, uDate, nmny);
				}
			} else if (ifundtype == HgPubConst.FUND_CHECK_FUND_QUATO) {
				if (flag) {
					getFundBO().useFund_Before(plankey,
							HgPubConst.FUND_CHECK_FUND, scorpid, null, uDate,
							nmny, head.getPrimaryKey(), head.getCbilltype());
					getFundBO().useFund_Before(plankey,
							HgPubConst.FUND_CHECK_QUATO, scorpid, null, uDate,
							nmny, head.getPrimaryKey(), head.getCbilltype());
				} else {
					getFundBO().reUseFund_before(plankey,
							HgPubConst.FUND_CHECK_FUND, scorpid, null, uDate,
							nmny);
					getFundBO().reUseFund_before(plankey,
							HgPubConst.FUND_CHECK_QUATO, scorpid, null, uDate,
							nmny);
				}
			}
		}
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）外销预扣 2012-2-19下午07:55:26
	 * @param billvo
	 * @param flag
	 * @throws BusinessException
	 */
	public void checkAndUseFund_before(SaleOrderVO billvo, boolean flag)
			throws BusinessException {
		if (billvo == null) {
			return;
		}

		SaleorderHVO head = billvo.getHeadVO();
		SaleorderBVO[] items = billvo.getBodyVOs();
		if (items == null || items.length == 0)
			return;

		UFDouble nmny = UFDouble.ZERO_DBL;
		for (SaleorderBVO item : items) {
			nmny = nmny.add(PuPubVO.getUFDouble_NullAsZero(item.getNsummny()));
		}

		if (nmny.doubleValue() == UFDouble.ZERO_DBL.doubleValue())
			return;

		UFDate uDate = head.getDapprovedate();
		if (HgPubConst.IS_DBILLDATE_WHEN_USE) {
			uDate = head.getDbilldate();
		}

		if (flag) {
			getFundBO().useFund_Before(null, HgPubConst.FUND_CHECK_FUND, null,
					head.getCcustomerid(), uDate, nmny, head.getPrimaryKey(),
					null);
			getFundBO().useFund_Before(null, HgPubConst.FUND_CHECK_QUATO, null,
					head.getCcustomerid(), uDate, nmny, head.getPrimaryKey(),
					null);
		} else {
			getFundBO().reUseFund_before(null, HgPubConst.FUND_CHECK_FUND,
					null, head.getCcustomerid(), uDate, nmny);
			getFundBO().reUseFund_before(null, HgPubConst.FUND_CHECK_QUATO,
					null, head.getCcustomerid(), uDate, nmny);
		}
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）订单保存后回写累计领用数量 2011-4-8下午01:44:52
	 * @param billvo
	 *            修改后数据
	 * @param preBillvo
	 *            修改前数据
	 * @param flag
	 *            保存/删除
	 * @throws BusinessException
	 */
	public void reWriteToPlanFor5X(Object o1, Object o2, boolean flag)
			throws BusinessException {

		if (o1 == null)
			return;

		Map<String, UFDouble> newItemInfor = null;
		Map<String, UFDouble> oldItemInfor = null;
		List al = new ArrayList<String>();
		BillItemVO[] items = null;
		if (o1 instanceof BillItemVO[]) {
			items = (BillItemVO[]) o1;
			newItemInfor = new HashMap<String, UFDouble>();
			for (BillItemVO newitem : items) {
				if (flag) {// 保存
					// add by zhw 删除行时传过来的VO并没有删掉 因此 需要根据DR来判断是否已经删除 如果删除 则回写0
					String dr = PuPubVO
							.getString_TrimZeroLenAsNull(getDrForPK(newitem
									.getCbill_bid()));
					if (String.valueOf(1).equals(dr)) {
						newItemInfor.put(newitem.getCsourcebid(),
								UFDouble.ZERO_DBL);
						al.add(newitem.getCsourcebid());//
						// add by zhw end
					} else {
						newItemInfor.put(newitem.getCsourcebid(), PuPubVO
								.getUFDouble_NullAsZero(newitem.getNnum()));
					}
				} else {
					newItemInfor.put(newitem.getCsourcebid(), PuPubVO
							.getUFDouble_NullAsZero(newitem.getNnum()));
				}

			}
		} else {
			newItemInfor = (Map<String, UFDouble>) o1;
		}
		if (o2 != null) {
			if (o2 instanceof BillItemVO[]) {
				items = (BillItemVO[]) o2;
				oldItemInfor = new HashMap<String, UFDouble>();
				for (BillItemVO olditem : items) {
					oldItemInfor.put(olditem.getCsourcebid(), PuPubVO
							.getUFDouble_NullAsZero(olditem.getNnum()));
				}
			} else {
				oldItemInfor = (Map<String, UFDouble>) o2;
			}
		}

		String sql = "update hg_planother_b set nouttotalnum = coalesce(nouttotalnum,0.0)+? where pk_planother_b = ?";

		UFDouble nnum = UFDouble.ZERO_DBL;
		SQLParameter parameter = null;

		// BillItemVO[] newItems = billvo.getItemVOs();
		// BillItemVO[] oldItems = preBillvo ==
		// null?null:preBillvo.getItemVOs();
		List<String> lplanbid = new ArrayList<String>();
		if (flag) {
			// int index = 0;
			for (String key : newItemInfor.keySet()) {
				parameter = new SQLParameter();
				// parameter.add
				nnum = PuPubVO.getUFDouble_NullAsZero(newItemInfor.get(key));
				if (oldItemInfor != null) {
					nnum = nnum.sub(PuPubVO.getUFDouble_NullAsZero(oldItemInfor
							.get(key)));
				}
				parameter.addParam(nnum);
				parameter.addParam(key);
				if (!lplanbid.contains(key))
					lplanbid.add(key);
				getBaseDao().executeUpdate(sql, parameter);
				String query = " select nouttotalnum from hg_planother_b where pk_planother_b = '"
						+ key + "' and isnull(dr,0)=0 ";
				Object o = getBaseDao().executeQuery(query,
						HgBsPubTool.COLUMNPROCESSOR);
				if (o == null)
					return;
				if (BigDecimal.ZERO.compareTo((BigDecimal) o) > 0)
					throw new BusinessException("累计领用数量小于零,回写出错");
			}
			// if(lplanbid.size()>0){
			// validationPlanOut(lplanbid.toArray(new String[0]));
			// }
			return;
		}

		for (String key : newItemInfor.keySet()) {
			parameter = new SQLParameter();
			nnum = PuPubVO.getUFDouble_NullAsZero(newItemInfor.get(key))
					.multiply(-1);
			parameter.addParam(nnum);
			parameter.addParam(key);
			if (!lplanbid.contains(key))
				lplanbid.add(key);
			getBaseDao().executeUpdate(sql, parameter);
		}

		if (al != null) {// 回写预留资金表中的预扣为0
			int size = al.size();
			for (int i = 0; i < size; i++) {
				String pk = PuPubVO.getString_TrimZeroLenAsNull(al.get(i));
				String sql1 = " update hg_fundset_b set nmny = '0' where pk_planother_b  = '"
						+ pk + "'";
				getBaseDao().executeUpdate(sql1);
			}
		}

		// 校验累计领用数量不能大于计划量
		// if(lplanbid.size()>0){
		// validationPlanOut(lplanbid.toArray(new String[0]));
		// }
	}

	// 根据PK查处该单据的DR 判断是否删除 add by zhw 2011-01-12 20:41
	public String getDrForPK(String pk) throws BusinessException {
		String sql = " select dr from to_bill_b where cbill_bid = '" + pk + "'";
		Object o = getBaseDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
		return PuPubVO.getString_TrimZeroLenAsNull(o);
	}

	public void validationPlanOut(String[] lplanbid) throws BusinessException {
		if (lplanbid == null || lplanbid.length == 0)
			return;
		String sql = " select count(*) from hg_planother_b where coalesce(nnum,0.0) - coalesce(nouttotalnum,0.0) < 0 and pk_planother_b in "
				+ HgPubTool.getSubSql(lplanbid);

		int count = PuPubVO.getInteger_NullAs(getBaseDao().executeQuery(sql,
				HgBsPubTool.COLUMNPROCESSOR), HgPubTool.INTEGER_ZERO_VALUE);
		if (count > 0)
			throw new BusinessException("超计划出库，请检查出库数量");
	}

	public BaseDAO dao = null;

	public BaseDAO getBaseDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）查询容差设置 2011-4-8下午02:07:10
	 * @param pk_invbasid
	 * @param fieldname
	 * @return
	 * @throws BusinessException
	 */
	public UFDouble getAllowanceSet(String pk_invbasid, String fieldname,
			String where) throws BusinessException {
		if (PuPubVO.getString_TrimZeroLenAsNull(pk_invbasid) == null)
			return null;
		String sql = " select "
				+ fieldname
				+ " from HG_ALLOWANCE where isnull(dr,0)=0 and pk_invbasdoc = '"
				+ pk_invbasid + "'";
		if (where != null && !"".equals(where)) {
			sql = sql + where;
		}

		Object o = getBaseDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
		if (o != null)
			return new UFDouble((BigDecimal) o);
		sql = " select invclasscode from bd_invbasdoc inv inner join bd_invcl cl"
				+ " on inv.pk_invcl = cl.pk_invcl "
				+ " where  pk_invbasdoc = '" + pk_invbasid + "'";
		String invcode = PuPubVO.getString_TrimZeroLenAsNull(getBaseDao()
				.executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR));
		if (invcode == null)
			return null;
		return getAllowanceSetByInvcl(invcode, fieldname, where);
	}

	public UFDouble getAllowanceSetByInvcl(String invclcode, String fieldname,
			String where) throws BusinessException {
		if (invclcode.length() < 2)
			return null;
		String sql = "select " + fieldname
				+ " from HG_ALLOWANCE where isnull(dr,0)=0 and pk_invcl = "
				+ " (select pk_invcl from bd_invcl where invclasscode = '"
				+ invclcode + "')";
		if (where != null && !"".equals(where)) {
			sql = sql + where;
		}
		Object o = getBaseDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
		if (o != null) {
			return PuPubVO.getUFDouble_NullAsZero(o);
		}
		if (invclcode.length() == 2)
			return null;
		String invcl = invclcode.substring(0, invclcode.length() - 2);
		return getAllowanceSetByInvcl(invcl, fieldname, where);
	}

	/**
	 * 根据存货校验该存货对应分类下是否存在着该存货
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-1-8下午07:07:58
	 * @param invclcode
	 * @param fieldname
	 * @param where
	 * @return
	 * @throws BusinessException
	 */
	public void checkAllowanceSetByInvcl(String pk_invbasdoc, String pk_invcl,
			String pk) throws BusinessException {

		String sqlcode = "select l.invclasscode from bd_invbasdoc c join HG_ALLOWANCE e on e.pk_invbasdoc =c.pk_invbasdoc "
				+ " join bd_invcl l on l.pk_invcl =c.pk_invcl where isnull(e.dr,0)=0 ";
		if (pk != null)
			sqlcode = sqlcode + (" and e.pk_allowance <>'" + pk + "' ");

		String sqlclasscode = "select l.invclasscode from bd_invcl l join HG_ALLOWANCE e on e.pk_invcl = l.pk_invcl where isnull(e.dr,0)=0 ";
		if (pk != null)
			sqlclasscode = sqlclasscode
					+ (" and e.pk_allowance <>'" + pk + "' ");

		Object o2 = getBaseDao().executeQuery(sqlcode,
				HgBsPubTool.COLUMNLISTPROCESSOR);
		Object o4 = getBaseDao().executeQuery(sqlclasscode,
				HgBsPubTool.COLUMNLISTPROCESSOR);
		String invcode = null;
		String invclasscode = null;

		ArrayList al = new ArrayList();
		if (o2 == null)
			return;
		ArrayList<String> alcode = (ArrayList<String>) o2;
		int size1 = alcode.size();
		for (int i = 0; i < size1; i++) {
			al.add(alcode.get(i));
		}

		if (o4 == null)
			return;
		ArrayList<String> alclasscode = (ArrayList<String>) o4;
		int size = alclasscode.size();
		for (int i = 0; i < size; i++) {
			al.add(alclasscode.get(i));
		}
		int size2 = al.size();
		if (pk_invbasdoc != null) {
			String sql1 = "select count(*) from HG_ALLOWANCE where pk_invbasdoc = '"
					+ pk_invbasdoc + "' and isnull(dr,0)=0 ";
			if (pk != null)
				sql1 = sql1 + (" and pk_allowance <>'" + pk + "' ");
			Object on = getBaseDao().executeQuery(sql1,
					HgBsPubTool.COLUMNPROCESSOR);
			if (PuPubVO.getInteger_NullAs(on, new Integer(0)) > 0)
				throw new BusinessException("该存货存在容差设置");
			String sql = "select l.invclasscode from bd_invbasdoc c join bd_invcl l on l.pk_invcl=c.pk_invcl  where c.pk_invbasdoc = '"
					+ pk_invbasdoc + "'";
			Object o1 = getBaseDao().executeQuery(sql,
					HgBsPubTool.COLUMNPROCESSOR);
			invcode = PuPubVO.getString_TrimZeroLenAsNull(o1);
			if (alclasscode.contains(invcode)) {
				throw new BusinessException("该存货存在容差设置");
			}
		} else {
			String sqlclass = "select l.invclasscode from bd_invcl l where l.pk_invcl='"
					+ pk_invcl + "'";
			Object o3 = getBaseDao().executeQuery(sqlclass,
					HgBsPubTool.COLUMNPROCESSOR);
			if (o3 != null) {
				invclasscode = HgPubTool.getString_NullAsTrimZeroLen(o3);
				if (al.contains(invclasscode)) {
					throw new BusinessException("该存货存在容差设置");
				} else {
					for (int i = 0; i < size2; i++) {
						String cl = HgPubTool.getString_NullAsTrimZeroLen(al
								.get(i));
						int lencl = cl.length();
						int leninv = invclasscode.length();
						if (lencl > leninv) {
							String ll = cl.substring(0, leninv);
							if (ll.equalsIgnoreCase(invclasscode)) {
								throw new BusinessException("该存货分类存在容差设置");
							}
						} else {
							String ll = invclasscode.substring(0, lencl);
							if (ll.equalsIgnoreCase(cl)) {
								throw new BusinessException("该存货分类存在容差设置");
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）参照查询计划 调拨订单 库存材料出库单 2011-2-16上午09:53:40
	 * @param swhere
	 * @return
	 * @throws BusinessException
	 */
	public Object queryPlanBillForTO5X(String swhere, String s,String billtype)
			throws BusinessException {
		// 表头 表体
		if (PuPubVO.getString_TrimZeroLenAsNull(swhere) == null)
			return null;
		StringBuffer str = new StringBuffer();
		PlanVO head = new PlanVO();
		String[] names = head.getAttributeNames();
		// modify by zhw 2010-12-28 过滤掉重复的数据
		str.append("select distinct ");
		// modify by zhw 2010-12-28 过滤掉重复的数据 end
		for (String name : names) {
			str.append("h." + name + ",");
		}
		str.append("'aa'");// 无用为了去除 最后一个逗号
		str.append(" from " + head.getTableName());
		str
				.append(" h inner join hg_planother_b b on h.pk_plan = b.pk_plan  join bd_invbasdoc c on c.pk_invbasdoc = b.pk_invbasdoc where ");
		str.append(swhere);
		Object o = getBaseDao().executeQuery(str.toString(),
				new BeanListProcessor(PlanVO.class));
		if (o == null)
			return null;
		List lhead = (List) o;

		if (lhead.size() == 0)
			return null;

		// 获取表体
		List<String> lheadid = new ArrayList<String>();
		int len = lhead.size();
		String tmp = null;
		for (int i = 0; i < len; i++) {
			tmp = ((PlanVO) lhead.get(i)).getPrimaryKey();
			if (lheadid.contains(tmp))
				continue;
			lheadid.add(tmp);
		}
		// modify by zhw 2010-12-30 过滤掉表体 数量大于0 按存货分类过滤表体行 数量-累计领用数量大于零
		PlanOtherBVO bvo = new PlanOtherBVO();
		String[] bnames = bvo.getAttributeNames();
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct ");
		for (String bname : bnames) {
			if (!bname.equalsIgnoreCase("lsourceid"))
				buffer.append("b." + bname + ",");
		}
		buffer.append("'aa'");// 无用为了去除 最后一个逗号
		buffer.append(" from " + bvo.getTableName());
		buffer
				.append(" b join bd_invbasdoc c on c.pk_invbasdoc = b.pk_invbasdoc where ");
		buffer.append(" b.pk_plan  in "
				+ HgPubTool.getSubSql(lheadid.toArray(new String[0])));
		buffer.append(" and coalesce(b.nnum,0.0)>0 and coalesce(b.nnum,0.0) - coalesce(b.nouttotalnum,0.0)>0 and isnull(b.dr,0)=0 and isnull(c.dr,0)=0 ");
		if(billtype.equalsIgnoreCase(HgPubConst.PLAN_MONTH_BILLTYPE))
			buffer.append(" and (coalesce(b.nreserve10,0.0)-coalesce(b.nouttotalnum,0.0))>0.0 ");
		buffer.append(" and b.vreserve2 =='Y' ");
		buffer.append(s);
		// Collection c = getBaseDao().retrieveByClause(PlanOtherBVO.class,
		// bodywhere);
		Object ob = getBaseDao().executeQuery(buffer.toString(),
				new BeanListProcessor(PlanOtherBVO.class));
		if (ob == null)
			return null;
		List lbody = (List) ob;
		if (lbody == null || lbody.size() == 0)
			return null;
		Object os = new Object[] { lhead.toArray(new PlanVO[0]),
				lbody.toArray(new PlanOtherBVO[0]) };
		return os;
		// modify by zhw 2010-12-30 过滤掉表体 数量大于0 end
	}

	/**
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业）调拨订单关闭后 把未出库数量回写到来源的计划中 2011-01-13下午01:44:52
	 * @throws BusinessException
	 */
	public void reWriteToPlanFor5XEnd(UFDouble back, String csourceid)
			throws BusinessException {
		if (csourceid == null)
			return;
		String sql = "update hg_planother_b set nouttotalnum = coalesce(nouttotalnum,0.0)-"
				+ back + " where pk_planother_b = '" + csourceid + "'";
		getBaseDao().executeUpdate(sql);

	}

	/**
	 * 调拨出库单签字后 把调拨订单的剩余量回写 把调拨订单行关闭 不可以弃审
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-1-20下午12:43:02
	 */
	public void reWtriteToPlanFor4YClose(GeneralBillVO gbillvo)
			throws BusinessException {
		if (gbillvo == null)
			return;
		GeneralBillItemVO[] items = gbillvo.getItemVOs();
		Object[] str = new Object[4];
		UFDouble temp = null;
		for (GeneralBillItemVO item : items) {
			str = getItemFromOut(item.getCsourcebillbid()); // 调拨订单数量 已经调出数量
			// 源头ID
			if (str != null && str.length > 0) {
				temp = PuPubVO.getUFDouble_NullAsZero(str[0]).sub(
						PuPubVO.getUFDouble_NullAsZero(str[1]));
				if (UFDouble.ZERO_DBL.compareTo(temp) < 0) {
					if (!HgPubConst.TO_FROWSTATUS.equalsIgnoreCase(str[3]
							.toString())) {
						String sql = " update hg_planother_b set nouttotalnum = coalesce(nouttotalnum,0.0) - "
								+ temp
								+ " where pk_planother_b = '"
								+ str[2]
								+ "'";
						getBaseDao().executeUpdate(sql);
						String sql1 = " update to_bill_b set frowstatuflag = '7',bsendendflag='Y',boutendflag='Y' where cbill_bid  = '"
								+ item.getCsourcebillbid() + "'";
						getBaseDao().executeUpdate(sql1);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）订单保存后回写累计领用数量 2011-4-8下午01:44:52
	 * @param billvo
	 *            修改后数据
	 * @param preBillvo
	 *            修改前数据
	 * @param flag
	 *            保存/删除
	 * @throws BusinessException
	 */
	public void reWriteToPlanFor5X1(Object o1, Object o2, boolean flag)
			throws BusinessException {

		if (o1 == null)
			return;

		Map<String, UFDouble> newItemInfor = null;
		Map<String, UFDouble> oldItemInfor = null;
		BillItemVO[] items = null;
		if (o1 instanceof BillItemVO[]) {
			items = (BillItemVO[]) o1;
			newItemInfor = new HashMap<String, UFDouble>();
			for (BillItemVO newitem : items) {
				newItemInfor.put(newitem.getCsourcebid(), PuPubVO
						.getUFDouble_NullAsZero(newitem.getNnum()));
			}
		} else {
			newItemInfor = (Map<String, UFDouble>) o1;
		}
		if (o2 != null) {
			if (o2 instanceof BillItemVO[]) {
				items = (BillItemVO[]) o2;
				oldItemInfor = new HashMap<String, UFDouble>();
				for (BillItemVO olditem : items) {
					oldItemInfor.put(olditem.getCsourcebid(), PuPubVO
							.getUFDouble_NullAsZero(olditem.getNnum()));
				}
			} else {
				oldItemInfor = (Map<String, UFDouble>) o2;
			}
		}

		String sql = "update hg_planother_b set nouttotalnum = coalesce(nouttotalnum,0.0)+? where pk_planother_b = ?";

		UFDouble nnum = UFDouble.ZERO_DBL;
		SQLParameter parameter = null;

		// BillItemVO[] newItems = billvo.getItemVOs();
		// BillItemVO[] oldItems = preBillvo ==
		// null?null:preBillvo.getItemVOs();
		List<String> lplanbid = new ArrayList<String>();
		if (flag) {
			// int index = 0;
			for (String key : newItemInfor.keySet()) {
				parameter = new SQLParameter();
				// parameter.add
				nnum = PuPubVO.getUFDouble_NullAsZero(newItemInfor.get(key));
				if (oldItemInfor != null) {
					nnum = nnum.sub(PuPubVO.getUFDouble_NullAsZero(oldItemInfor
							.get(key)));
				}
				parameter.addParam(nnum);
				parameter.addParam(key);
				if (!lplanbid.contains(key))
					lplanbid.add(key);
				getBaseDao().executeUpdate(sql, parameter);
			}
			// if(lplanbid.size()>0){
			// validationPlanOut(lplanbid.toArray(new String[0]));
			// }
			return;
		}

		for (String key : newItemInfor.keySet()) {
			parameter = new SQLParameter();
			nnum = PuPubVO.getUFDouble_NullAsZero(newItemInfor.get(key))
					.multiply(-1);
			parameter.addParam(nnum);
			parameter.addParam(key);
			if (!lplanbid.contains(key))
				lplanbid.add(key);
			getBaseDao().executeUpdate(sql, parameter);
		}

		// 校验累计领用数量不能大于计划量
		// if(lplanbid.size()>0){
		// validationPlanOut(lplanbid.toArray(new String[0]));
		// }
	}

	/**
	 * 根据调拨出库单的来源ID 找出 调拨订单数量 已经调出数量 源头ID
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-1-21下午02:55:07
	 * @param csourcebid
	 * @return
	 * @throws BusinessException
	 */
	public Object[] getItemFromOut(String csourcebid) throws BusinessException {
		String sql = " select nnum,norderoutnum,csourcebid,frowstatuflag from to_bill_b where cbill_bid = '"
				+ csourcebid + "' and isnull(dr,0)=0";
		Object o = getBaseDao().executeQuery(sql, HgBsPubTool.ARRAYPROCESSOR);
		if (o == null)
			return null;
		Object[] str = (Object[]) o;
		return str;
	}

	/**
	 * 出库签字 回写订单行关闭上的预扣
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-1-24下午06:05:19
	 * @param gbillvo
	 * @throws BusinessException
	 */
	public void reWriteUseFundBefore(GeneralBillVO gbillvo)
			throws BusinessException {
		// TODO Auto-generated method stub
		if (gbillvo == null)
			return;
		if (gbillvo.getHeaderVO().getCbilltypecode().equalsIgnoreCase(
				ScmConst.m_allocationOut)) {
			GeneralBillItemVO[] items = gbillvo.getItemVOs();
			UFDouble temp = UFDouble.ZERO_DBL;
			UFDouble nmny = UFDouble.ZERO_DBL;

			for (GeneralBillItemVO item : items) {
				Object[] obj = getValueFromOut(item.getCsourcebillbid());
				if (obj != null && obj.length > 0) {
					String corp = HgPubTool.getString_NullAsTrimZeroLen(obj[0]);// 调入公司
					UFDate date = PuPubVO.getUFDate(obj[1]);// 制单日期
					temp = PuPubVO.getUFDouble_NullAsZero(obj[3]).sub(
							PuPubVO.getUFDouble_NullAsZero(obj[4]));
					nmny = temp.multiply(
							PuPubVO.getUFDouble_NullAsZero(obj[5]),
							HgPubConst.MNY_DIGIT);// 还原金额
					if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
							.doubleValue())
						return;

					int ifundtype = HgPubTool.getIFundTypeByPlanType(PuPubVO.getString_TrimZeroLenAsNull(obj[2]));// 获取计划的类型
					if (ifundtype == HgPubConst.FUND_CHECK_ERRORTYPE)
						return;
					if (ifundtype == HgPubConst.FUND_CHECK_SPECIALFUND) {
						updateVO(corp, date, HgPubConst.FUND_CHECK_SPECIALFUND,
								nmny);
					} else if (ifundtype == HgPubConst.FUND_CHECK_FUND_QUATO) {
						updateVO(corp, date, HgPubConst.FUND_CHECK_FUND, nmny);
						updateVO(corp, date, HgPubConst.FUND_CHECK_QUATO, nmny);
					}
				}
			}
		}
	}

	/**
	 * 更新预扣
	 * 
	 * @author zhw
	 * @throws BusinessException
	 * @说明：（鹤岗矿业） 2011-1-24下午06:01:08
	 */
	public void updateVO(String corp, UFDate date, int ifundtype, UFDouble nmny)
			throws BusinessException {
		FUNDSETVO vo = getFundSet(corp, date, ifundtype);
		if (vo == null) {
			return;
		}
		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// 预扣
		if (nlockfund.doubleValue() < nmny.doubleValue())
			throw new BusinessException("数据错误，超预扣金额");

		nlockfund = nlockfund.sub(nmny);// 回写预扣
		vo.setNlockfund(nlockfund);
		getBaseDao().updateVO(vo, HgBsPubTool.CHECK_LOCkFUND_NAME);
	}

	/**
	 * 根据调拨出库单的来源ID 找出 调入公司 制单日期 来源单据类型 调拨订单数量 已经调出数量 无税单价
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-1-21下午02:55:07
	 * @param csourcebid
	 * @return
	 * @throws BusinessException
	 */
	public Object[] getValueFromOut(String csourcebid) throws BusinessException {
		String sql = " select l.cincorpid,l.dbilldate,b.csourcetypecode,b.nnum,b.norderoutnum,b.nnotaxprice "
				+ " from to_bill l join to_bill_b b on l.cbillid = b.cbillid where b.cbill_bid = '"
				+ csourcebid + "' and isnull(l.dr,0)=0 and isnull(b.dr,0)=0";
		Object o = getBaseDao().executeQuery(sql, HgBsPubTool.ARRAYPROCESSOR);
		if (o == null)
			return null;
		Object[] str = (Object[]) o;
		return str;
	}

	/**
	 * 获取fundVO
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-1-24下午05:40:22
	 * @param scorpid
	 * @param uDate
	 * @param ifundtype
	 * @return
	 * @throws BusinessException
	 */
	public FUNDSETVO getFundSet(String scorpid, UFDate uDate, int ifundtype)
			throws BusinessException {
		String corp = PuPubVO.getString_TrimZeroLenAsNull(scorpid);
		if (corp == null)
			return null;
		StringBuffer strb = new StringBuffer();
		if (corp != null) {
			strb.append("  pk_corp = '" + corp + "' and ");
		}
		strb.append(" isnull(dr,0)=0 ");
		// add by zhw 2010-12-28 获取当前日期对应的会计年 会计月
		// 得到基准期间方案的日历实例
		AccountCalendar calendar = AccountCalendar.getInstance();
		calendar.setDate(uDate);
		// 设置日期
		String iyear = calendar.getYearVO().getPeriodyear();
		String imonth = calendar.getMonthVO().getMonth();
		// zhw end
		strb.append(" and cyear = '" + String.valueOf(iyear)
				+ "' and imonth = '" + String.valueOf(imonth) + "'");
		strb.append(" and coalesce(fcontrol,'N')='Y' and ifundtype = "
				+ ifundtype);

		java.util.Collection c = getBaseDao().retrieveByClause(FUNDSETVO.class,
				strb.toString());
		// getDMO().queryByWhereClause(FUNDSETVO.class, strb.toString());
		if ((c == null || c.size() == 0))
			return null;
		if (c.size() > 1) {
			throw new BusinessException("资金设置异常");
		}
		java.util.Iterator it = c.iterator();
		FUNDSETVO vo = (FUNDSETVO) it.next();
		return vo;
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）参照查询计划 调拨订单 库存材料出库单 2011-2-16上午09:53:40
	 * @param swhere
	 * @return
	 * @throws BusinessException
	 */
	public Object queryPlanBillForTOHG10(String swhere)
			throws BusinessException {
		// 表头 表体
		if (PuPubVO.getString_TrimZeroLenAsNull(swhere) == null)
			return null;
		StringBuffer str = new StringBuffer();
		InvoiceHeaderVO head = new InvoiceHeaderVO();
		String[] names = head.getDbFields();
		// modify by zhw 2010-12-28 过滤掉重复的数据
		str.append("select distinct ");
		// modify by zhw 2010-12-28 过滤掉重复的数据 end
		for (String name : names) {
			str.append("h." + name + ",");
		}
		str.append("'aa'");// 无用为了去除 最后一个逗号
		str.append(" from po_invoice");
		str
				.append(" h inner join po_invoice_b b on h.cinvoiceid = b.cinvoiceid where ");
		// 得到基准期间方案的日历实例
		AccountCalendar calendar = AccountCalendar.getInstance();
		// 设置日期
		str.append(" h.dinvoicedate >= '"
				+ calendar.getMonthVO().getBegindate().toString() + "'");
		str.append(" and h.dinvoicedate <= '"
				+ calendar.getMonthVO().getEnddate().toString() + "'");
		str.append(" and h.ibillstatus = '0' and h.vdef20  is null and");
		str.append(swhere);
		Object o = getBaseDao().executeQuery(str.toString(),
				new BeanListProcessor(InvoiceHeaderVO.class));
		if (o == null)
			return null;
		List lhead = (List) o;

		if (lhead.size() == 0)
			return null;

		// 获取表体
		List<String> lheadid = new ArrayList<String>();
		int len = lhead.size();
		String tmp = null;
		for (int i = 0; i < len; i++) {
			tmp = ((InvoiceHeaderVO) lhead.get(i)).getPrimaryKey();
			if (lheadid.contains(tmp))
				continue;
			lheadid.add(tmp);
		}
		// modify by zhw 2010-12-30 过滤掉表体 数量大于0 按存货分类过滤表体行 数量-累计领用数量大于零
		InvoiceItemVO bvo = new InvoiceItemVO();
		String[] bnames = bvo.getDbFields();
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct ");
		for (String bname : bnames) {
			buffer.append("b." + bname + ",");
		}
		buffer.append("'aa'");// 无用为了去除 最后一个逗号
		buffer.append(" from po_invoice_b ");
		buffer.append(" b where b.cinvoiceid  in "
				+ HgPubTool.getSubSql(lheadid.toArray(new String[0])));
		buffer.append("  and isnull(b.dr,0)=0 ");
		Object ob = getBaseDao().executeQuery(buffer.toString(),
				new BeanListProcessor(InvoiceItemVO.class));
		if (ob == null)
			return null;
		List lbody = (List) ob;
		if (lbody == null || lbody.size() == 0)
			return null;
		Object os = new Object[] { lhead.toArray(new InvoiceHeaderVO[0]),
				lbody.toArray(new InvoiceItemVO[0]) };
		return os;
		// modify by zhw 2010-12-30 过滤掉表体 数量大于0 end
	}

	/**
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业）采购订单订单询采购合同价销售 2012-2-21下午02:27:22
	 * @param cinvbasid
	 * @param cbatchid
	 * @throws BusinessException
	 */
	public UFDouble callPurchasePactPrice(String invcode, String pk_corp,String vbatch)
			throws Exception {
		if (PuPubVO.getString_TrimZeroLenAsNull(invcode) == null
				|| PuPubVO.getString_TrimZeroLenAsNull(pk_corp) == null || PuPubVO.getString_TrimZeroLenAsNull(vbatch)==null)
			return null;

		String sql = "select ord.noriginalcurprice from po_order_b ord inner join ic_general_b pi on pi.csourcebillbid = ord.corder_bid "
				+ " inner join bd_invmandoc man on pi.cinventoryid = man.pk_invmandoc inner join bd_invbasdoc bas on bas.pk_invbasdoc = man.pk_invbasdoc "
				+ " where isnull(ord.dr, 0) = 0 and isnull(pi.dr, 0) = 0 and isnull(bas.dr,0)=0 and isnull(man.dr,0)=0" 
				+" and bas.invcode='"+invcode+"' and man.pk_corp ='"+pk_corp+"' and pi.vbatchcode = '"+vbatch+"'";
		Object o = getBaseDao().executeQuery(sql,
				HgBsPubTool.COLUMNLISTPROCESSOR);

		if (o == null)
			return null;
		UFDouble prices = null;
		List ldata = (List) o;
		if (ldata.size() == 0)
			return null;
		Object os = (Object) ldata.get(0);
		prices = PuPubVO.getUFDouble_NullAsZero(os);
		return prices;
	}

	/**
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业）根据计划项目找出库方式 2012-2-21下午02:27:22
	 * @param cinvbasid
	 * @param cbatchid
	 * @throws BusinessException
	 */
	public String getIOutWay(String pk_planproject) throws Exception {
		if (PuPubVO.getString_TrimZeroLenAsNull(pk_planproject) == null)
			return null;
		String sql = "select ioutway from hg_planproject where pk_planproject = '"
				+ pk_planproject + "' and isnull(dr,0)=0";
		Object o = getBaseDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);

		if (o == null)
			return null;
		String string = null;
		String str = PuPubVO.getString_TrimZeroLenAsNull(o);
		if ("0".equalsIgnoreCase(str))
			string = "计划价";
		if ("1".equalsIgnoreCase(str))
			string = "合同价";
		return string;
	}
}

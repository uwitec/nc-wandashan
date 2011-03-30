package nc.bs.hg.pu.check;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.dao.DAOException;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.itf.hg.pu.pub.IFundCheck;
import nc.vo.hg.pu.check.fund.FUNDSETVO;
import nc.vo.hg.pu.check.fund.FundSetBVO;
import nc.vo.hg.pu.plan.deal.PlanDealVO;
import nc.vo.hg.pu.plan.month.PlanOtherBVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.hg.pu.pub.PlanVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;

/**
 * 
 * @author zhf
 * 
 * �ʽ� ̨����ƣ� ά�� ���ʽ� Ԥ�� ʵ�� Ԥ�����ƻ���
 * 
 * ԭʼ���ʽ�=���ʽ�+Ԥ�� ֻ�в���Ԥ��ʱ �������ʽ� ����Ԥ�ۺ�ʵ��ʱ ���ʽ���֮�䶯
 * 
 * ʣ������ʽ�=���ʽ�+�üƻ���Ԥ��-Ԥ��-ʵ��
 * 
 * ά�ȣ� �ϼ�Ϊ�¼�����ÿ���¿����ʽ����� ��Ӧ�����ø����� ��Ϊ����������������
 * 
 * ��͹�Ӧ��֮������������ߵ��� �����Ϳ�֮�� ��Ϊ����û�п�� �� ���ϳ��ⵥ ���������۶��� ���۳���
 * 
 */

public class FundLockBO implements IFundCheck {

	private nc.bs.dao.BaseDAO dao = null;

	private nc.bs.dao.BaseDAO getDao() {
		if (dao == null)
			dao = new nc.bs.dao.BaseDAO();
		return dao;
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��Ϊ�ƻ�Ԥ���ʽ��޶��ר���ʽ� ���ʽ𲻱� Ԥ�� Ԥ�� ʵ�� ������̬�仯 2011-2-2����10:51:59
	 * @param vo
	 * @param nmny
	 *            Ԥ����� �����Ԥ�����µ��ʽ� ��ҪУ�鵱���Ƿ����㹻���ʽ� ���Ԥ�������Ժ���ʽ�ֱ��
	 * @param ifundtype
	 *            �ʽ����� ���ʽ� �޶� ר���ʽ�
	 * @param pk_plan
	 *            �ƻ�id
	 * @throws BusinessException
	 */
	public void LockFundExitFund(FUNDSETVO vo, UFDouble nmny, int ifundtype,
			String pk_plan, String pk_planother_b, String scorpid,
			String sdeptid, UFDate uDate) throws BusinessException {
		if (vo == null
				|| PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
						.doubleValue())
			return;
		// У���Ƿ� ���� ���ʽ�-Ԥ��-ʵ��-Ԥ��
		AccountCalendar calendar = AccountCalendar.getInstance();
		calendar.setDate(uDate);
		String iyear = calendar.getYearVO().getPeriodyear();
		String imonth = calendar.getMonthVO().getMonth();
		FundSetBVO bvo = getFundSetBVO(scorpid, sdeptid, iyear, imonth,pk_planother_b);
		if(bvo !=null){
			UFDouble lock = bvo.getNlockmny().sub(bvo.getNmny()).add(nmny);// Ԥ���ʽ�

			UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund()).sub(
					PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund())).sub(
					PuPubVO.getUFDouble_NullAsZero(vo.getNactfund())).sub(lock);
			if (nfund.doubleValue() < 0) {
				throw new BusinessException("��" + HgPubTool.getSFundType(ifundtype)
						+ "����");
			}
		}
		
		LockFund(vo, nmny, scorpid, sdeptid, uDate, pk_plan, pk_planother_b);

		vo.setNfund(PuPubVO.getUFDouble_NullAsZero(vo.getNfund()).sub(nmny));
		getDao().updateVO(vo, HgBsPubTool.CHECK_FUND_NAME);
	}

	/**
	 * ���������Ժ��Ԥ��
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-3-11����10:13:39
	 * @param vo
	 * @param nmny
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param pk_plan
	 * @throws BusinessException
	 */
	public void LockFund(FUNDSETVO vo, UFDouble nmny, String scorpid,
			String sdeptid, UFDate uDate, String pk_plan, String pk_planother_b)
			throws BusinessException {
		AccountCalendar calendar = AccountCalendar.getInstance();
		calendar.setDate(uDate);
		String iyear = calendar.getYearVO().getPeriodyear();
		String imonth = calendar.getMonthVO().getMonth();

		FundSetBVO bSetVO = getFundSetBVO(scorpid, sdeptid, iyear, imonth,pk_planother_b);

		FundSetBVO bvo = new FundSetBVO();
		if (bSetVO != null) {
			bvo = bSetVO;
			nmny = nmny.add(bSetVO.getNlockmny());
			bvo.setNlockmny(nmny);
			getDao().updateVO(bvo);
		} else {
			bvo.setImonth(imonth);
			bvo.setCdeptid(sdeptid);
			bvo.setIyear(iyear);
			bvo.setPk_corp(scorpid);
			bvo.setPk_plan(pk_plan);
			bvo.setPk_planother_b(pk_planother_b);
			bvo.setNlockmny(nmny);
			bvo.setStatus(VOStatus.NEW);
			getDao().insertVO(bvo);
		}

	}

	/**
	 * ��ȡԤ���ʽ�
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-2-11����02:43:10
	 * @param scorpid
	 * @param sdeptid
	 * @param iyear
	 * @param imonth
	 * @return
	 * @throws BusinessException
	 */
	public FundSetBVO getFundSetBVO(String scorpid, String sdeptid,
			String iyear, String imonth,String pk_planother_b) throws BusinessException {
		String corp = PuPubVO.getString_TrimZeroLenAsNull(scorpid);
		String deptid = PuPubVO.getString_TrimZeroLenAsNull(sdeptid);
		if (corp == null && deptid == null)
			return null;
		StringBuffer strb = new StringBuffer();
		if (corp != null) {
			strb.append("  pk_corp = '" + corp + "' and ");
		}
		if (deptid != null) {
			strb.append(" cdeptid = '" + deptid + "' and ");
		} else
			strb.append(" (cdeptid = '' or cdeptid is null ) and ");

		strb.append(" isnull(dr,0)=0 ");
		strb.append(" and iyear = '" + String.valueOf(iyear)
				+ "' and imonth = '" + String.valueOf(imonth) + "'");
		strb.append(" and pk_planother_b = '"+pk_planother_b+"'");
		java.util.Collection c = getDao().retrieveByClause(FundSetBVO.class,
				strb.toString());
		if ((c == null || c.size() == 0))
			return null;
		if (c.size() > 1) {
			throw new BusinessException("ר���ʽ������쳣");
		}
		java.util.Iterator it = c.iterator();
		FundSetBVO vo = (FundSetBVO) it.next();
		return vo;
	}

	public void reLockFund(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate) throws BusinessException {

		FUNDSETVO vo = getFundSet(scorpid, sdeptid, null, uDate, ifundtype,
				false);
		if (vo == null)
			return;
		reLockFund(vo, pk_plan);
	}

	public void reLockFund(FUNDSETVO vo, String pk_plan)
			throws BusinessException {
		String sql = " update hg_fundset_b set dr = 1 where pk_plan = '"
				+ pk_plan + "' and pk_fundset = '" + vo.getPrimaryKey()
				+ "' and isnull(dr,0)=0";
		getDao().executeUpdate(sql);
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ������ά�� Ϊ�ƻ� ���� �ʽ� 2011-2-2����10:54:43
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nmny
	 * @throws BusinessException
	 */
	public void LockFund(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nmny, String pk_planother_b,boolean flag)
			throws BusinessException {
		if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
				.doubleValue())
			return;
		FUNDSETVO vo =null;
		if(flag){
			 vo = getFundSet(scorpid, null, null, uDate, ifundtype,
					true);
		}else{
			 vo = getFundSet(null, sdeptid, null, uDate, ifundtype,
						true);
		}
		
		if (vo == null) {// ����������ʽ����
			LockFund(vo, nmny, scorpid, sdeptid, uDate, pk_plan, pk_planother_b);
		} else {
			LockFundExitFund(vo, nmny, ifundtype, pk_plan, pk_planother_b,
					scorpid, sdeptid, uDate);
		}

	}

	public FUNDSETVO getFundSet(String scorpid, String sdeptid,
			String ccustmanid, UFDate uDate, int ifundtype, boolean flag)
			throws BusinessException {
		String corp = PuPubVO.getString_TrimZeroLenAsNull(scorpid);
		String deptid = PuPubVO.getString_TrimZeroLenAsNull(sdeptid);
		String scustid = PuPubVO.getString_TrimZeroLenAsNull(ccustmanid);

		if (corp == null && deptid == null && scustid == null)
			return null;
		StringBuffer strb = new StringBuffer();
		// �ͻ�����
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
		// add by zhw 2010-12-28 ��ȡ��ǰ���ڶ�Ӧ�Ļ���� �����
		// �õ���׼�ڼ䷽��������ʵ��
		AccountCalendar calendar = AccountCalendar.getInstance();
		AccountCalendar calendar1 = AccountCalendar.getInstance();
		calendar.setDate(uDate);
		// ��������
		String iyear = calendar.getYearVO().getPeriodyear();
		String imonth = calendar.getMonthVO().getMonth();
		String imonth1 = calendar1.getMonthVO().getMonth();

		// zhw end
		strb.append(" and cyear = '" + String.valueOf(iyear)
				+ "' and imonth = '" + String.valueOf(imonth) + "'");
		strb.append(" and coalesce(fcontrol,'N')='Y' and ifundtype = "
				+ ifundtype);

		java.util.Collection c = getDao().retrieveByClause(FUNDSETVO.class,
				strb.toString());
		// getDMO().queryByWhereClause(FUNDSETVO.class, strb.toString());
		if ((c == null || c.size() == 0))
			if (flag) {
				// modify by zhw 2010-03-11
				if (imonth1.equalsIgnoreCase(imonth))// �������û�н���ר���ʽ�����
					throw new BusinessException("δ������Ч��ר���ʽ����ã��޷�Ԥ��");
				// modify by zhw 2010-03-11
				return null;
			} else
				return null;
		// }
		if (c.size() > 1) {
			throw new BusinessException("�ʽ������쳣");
		}
		java.util.Iterator it = c.iterator();
		FUNDSETVO vo = (FUNDSETVO) it.next();
		return vo;
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ) Ϊר��ƻ�Ԥ���ʽ� ���� 2011-1-31����05:03:31
	 * @param planvo
	 * @throws BusinessException
	 */
	// public void LockFundForPlan(HYBillVO planvo) throws BusinessException {
	// if (planvo == null)
	// return;
	// PlanVO head = (PlanVO) planvo.getParentVO();
	// // if (!PuPubVO.getUFBoolean_NullAs(head.getFisclose(), UFBoolean.FALSE)
	// // .booleanValue()) {
	// // return;
	// // }
	// PlanOtherBVO[] bodys = (PlanOtherBVO[]) planvo.getChildrenVO();
	// // �����ڽ��л��ܽ��
	// java.util.Map<String, UFDouble> mnyInfor = new java.util.HashMap<String,
	// UFDouble>();
	// UFDate date = null;
	// UFDouble nmny = null;
	// String key = null;
	// for (PlanOtherBVO body : bodys) {
	// if(!PuPubVO.getUFBoolean_NullAs(body.getFislimited(),UFBoolean.FALSE).booleanValue()){
	// continue;
	// }
	// date = body.getDreqdate();// �������� Ϊ ��Ч�� ����
	// key = HgPubTool.getString_NullAsTrimZeroLen(date.getYear()) + "-"
	// + HgPubTool.getMonth(date.getMonth());
	// if (mnyInfor.containsKey(key)) {
	// nmny = mnyInfor.get(key).add(
	// PuPubVO.getUFDouble_NullAsZero(body.getNmny()));
	// } else {
	// nmny = PuPubVO.getUFDouble_NullAsZero(body.getNmny());
	// }
	// mnyInfor.put(key, nmny);
	// }
	//
	// if (mnyInfor.size() == 0)
	// return;
	//
	// int iplantype = HgPubTool.getIFundTypeByPlanType(head.getPk_billtype());
	//
	// if (iplantype == HgPubConst.FUND_CHECK_SPECIALFUND) {// ר���ʽ�
	// for (String tmp : mnyInfor.keySet()) {
	// LockFund(head.getPrimaryKey(),
	// HgPubConst.FUND_CHECK_SPECIALFUND, head.getPk_corp(),
	// head.getCapplydeptid(), new UFDate(tmp + "-01"),
	// mnyInfor.get(tmp));
	// }
	// return;
	// }
	//
	// // try {// һ�� ���� ��һ�� ����У��
	// // UFDate uDate = null;
	// // for (String tmp : mnyInfor.keySet()) {
	// // uDate = new UFDate(tmp + "-01");
	// // // �����ʽ�
	// // LockFund(head.getPrimaryKey(),
	// // iplantype == -1 ? HgPubConst.FUND_CHECK_FUND
	// // : iplantype, head.getPk_corp(), head
	// // .getCapplydeptid(), uDate, mnyInfor.get(tmp));
	// // // �����޶�
	// // LockFund(head.getPrimaryKey(),
	// // iplantype == -1 ? HgPubConst.FUND_CHECK_QUATO
	// // : iplantype, head.getPk_corp(), head
	// // .getCapplydeptid(), uDate, mnyInfor.get(tmp));
	// // }
	// // } catch (BusinessException e) {
	// // throw e;
	// // }
	// }
	/**
	 * @author zhw
	 * @˵�������׸ڿ�ҵ) Ϊר��ƻ�Ԥ���ʽ� ���� 2011-03-11����05:03:31
	 */
	// public void LockFundForPlan(List planinfor, List lseldata)
	// throws BusinessException {
	//
	// if (planinfor == null)
	// return;
	// PlanDealVO dealTmp = null;
	// PlanDealVO dealTmp1 = null;
	// int len = planinfor.size();
	// UFDate date = null;
	// // �����ڽ��л��ܽ��
	// java.util.Map<String, UFDouble> mnyInfor = new java.util.HashMap<String,
	// UFDouble>();
	// for (int i = 0; i < len; i++) {
	//
	// UFDouble nmny = null;
	// String key = null;
	// dealTmp = (PlanDealVO) planinfor.get(i);
	// dealTmp1 = (PlanDealVO) lseldata.get(i);
	// if (!PuPubVO.getUFBoolean_NullAs(dealTmp.getFislimited(),
	// UFBoolean.FALSE).booleanValue()) {
	// continue;
	// }
	// date = dealTmp.getDreqdate();// �������� Ϊ ��Ч�� ����
	// key = HgPubTool.getString_NullAsTrimZeroLen(date.getYear()) + "-"
	// + HgPubTool.getMonth(date.getMonth())
	// + dealTmp1.getPk_corp() + dealTmp1.getCapplydeptid();
	// if (mnyInfor.containsKey(key)) {
	// nmny = mnyInfor.get(key).add(
	// PuPubVO.getUFDouble_NullAsZero(dealTmp.getNmny()));
	// } else {
	// nmny = PuPubVO.getUFDouble_NullAsZero(dealTmp.getNmny());
	// }
	// mnyInfor.put(key, nmny);
	// }
	//
	// if (mnyInfor.size() == 0)
	// return;
	// int iplantype = HgPubTool.getIFundTypeByPlanType(dealTmp
	// .getPk_billtype());
	//
	// if (iplantype == HgPubConst.FUND_CHECK_SPECIALFUND) {// ר���ʽ�
	// for (String tmp : mnyInfor.keySet()) {
	// LockFund(dealTmp.getPk_plan(),
	// HgPubConst.FUND_CHECK_SPECIALFUND, dealTmp1
	// .getPk_corp(), dealTmp1.getCapplydeptid(),
	// new UFDate(tmp + "-01"),mnyInfor.get(tmp), dealTmp1.getPk_plan_b());
	// }
	// }
	// }
	/**
	 * @author zhw
	 * @˵�������׸ڿ�ҵ) Ϊר��ƻ�Ԥ���ʽ� ���� 2011-03-11����05:03:31
	 */
	public void LockFundForPlan(List planinfor, List lseldata)
			throws BusinessException {

		if (planinfor == null)
			return;
		PlanDealVO dealTmp = null;
		PlanDealVO tmp = null;
		PlanDealVO dealTmp1 = null;
		int len = planinfor.size();
		int len1 = lseldata.size();
		UFDate date = null;
		for (int i = 0; i < len; i++) {
			dealTmp = (PlanDealVO) planinfor.get(i);
			for (int j = 0; j < len1; j++) {
				tmp = (PlanDealVO) lseldata.get(j);
				if (tmp.getPk_plan_b().equalsIgnoreCase(dealTmp.getPk_plan_b())) {
					dealTmp1 = tmp;
					break;
				}
			}
			if (!PuPubVO.getUFBoolean_NullAs(dealTmp.getFislimited(),
					UFBoolean.FALSE).booleanValue()) {
				continue;
			}
			date = dealTmp.getDreqdate();// �������� Ϊ ��Ч�� ����
			int iplantype = HgPubTool.getIFundTypeByPlanType(dealTmp
					.getPk_billtype());

			if (iplantype == HgPubConst.FUND_CHECK_SPECIALFUND) {// ר���ʽ�
				if("1002".equals(dealTmp.getPk_corp())){
					LockFund(dealTmp.getPk_plan(),
							HgPubConst.FUND_CHECK_SPECIALFUND, dealTmp1
									.getPk_corp(), dealTmp1.getCapplydeptid(),
							date, dealTmp1.getNmny(), dealTmp1.getPk_plan_b(),true);
				}else{
					LockFund(dealTmp.getPk_plan(),
							HgPubConst.FUND_CHECK_SPECIALFUND, dealTmp1
							.getPk_corp(), dealTmp1.getCapplydeptid(),
							date, dealTmp1.getNmny(), dealTmp1.getPk_plan_b(),false);
				}
			}
		}

	}

	public void reLockFundForPlan(HYBillVO planvo) throws BusinessException {
		if (planvo == null)
			return;
		PlanVO head = (PlanVO) planvo.getParentVO();
		PlanOtherBVO[] bodys = (PlanOtherBVO[]) planvo.getChildrenVO();
		// �����ڽ��л��ܽ��
		java.util.Map<String, UFDate> dateMap = new HashMap<String, UFDate>();
		String key = null;
		UFDate date = null;
		for (PlanOtherBVO body : bodys) {
			date = body.getDreqdate();// �������� Ϊ ��Ч�� ����
			key = HgPubTool.getString_NullAsTrimZeroLen(date.getYear())
					+ HgPubTool.getMonth(date.getMonth());
			if (!dateMap.containsKey(key))
				dateMap.put(key, date);
		}

		if (dateMap.size() == 0)
			return;

		int iplantype = HgPubTool.getIFundTypeByPlanType(head.getPk_billtype());

		if (iplantype == HgPubConst.FUND_CHECK_SPECIALFUND) {// ר���ʽ�
			for (UFDate uDate : dateMap.values()) {
				reLockFund(head.getPrimaryKey(),
						HgPubConst.FUND_CHECK_SPECIALFUND, head.getPk_corp(),
						head.getCapplydeptid(), uDate);
			}
			return;
		}

		// try {// һ�� ���� ��һ�� ����У��
		//
		// for (UFDate uDate : dateMap.values()) {
		// // �����ʽ�
		// reLockFund(head.getPrimaryKey(),
		// iplantype == -1 ? HgPubConst.FUND_CHECK_FUND
		// : iplantype, head.getPk_corp(), head
		// .getCapplydeptid(), uDate);
		// // �����޶�
		// reLockFund(head.getPrimaryKey(),
		// iplantype == -1 ? HgPubConst.FUND_CHECK_QUATO
		// : iplantype, head.getPk_corp(), head
		// .getCapplydeptid(), uDate);
		// }
		// } catch (BusinessException e) {
		// throw e;
		// }
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��У���Ƿ���Խ���Ԥ�� 2011-2-3����04:02:30
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nmny
	 * @throws BusinessException
	 */
	public void checkFund_Before(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nmny)
			throws BusinessException {
		if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
				.doubleValue())
			return;
		FUNDSETVO vo = getFundSet(scorpid, sdeptid, null, uDate, ifundtype,
				false);
		if (vo == null) {
			return;
		}

		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// ���ʽ�
		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// Ԥ��
		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());// ʵ��
		UFDouble nlockmny = UFDouble.ZERO_DBL;// Ԥ��
		if (PuPubVO.getString_TrimZeroLenAsNull(pk_plan) != null) {
			// ��ȡ�ƻ�Ԥ���ʽ�
			nlockmny = getPlanLockFund(pk_plan, vo.getPk_fundset());
		}
//		if (ifundtype == HgPubConst.FUND_CHECK_SPECIALFUND) {
//			nlockmny =getPlanLockFundByCorp(scorpid, sdeptid,uDate);
//		}
		
		// У���Ƿ����Ԥ�� ���ʽ�+Ԥ��-Ԥ��-ʵ��-����Ԥ��
		UFDouble nsubfund = nfund.add(nlockmny).sub(nlockfund).sub(nactfund)
				.sub(nmny);

		if (nsubfund.doubleValue() < 0) {
			throw new BusinessException("��" + HgPubTool.getSFundType(ifundtype)
					+ "����");
		}
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��У���Ƿ�����ʵ������ 2011-2-3����04:06:24
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nbeforemny
	 * @param nmny
	 * @throws BusinessException
	 */
	public void checkFund(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nbeforemny, UFDouble nmny)
			throws BusinessException {
		// �ۼ�˳�� Ԥ�� Ԥ�� ���ʽ�
		// if(PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue()==UFDouble.ZERO_DBL.doubleValue())
		// return;
		FUNDSETVO vo = getFundSet(scorpid, sdeptid, null, uDate, ifundtype,
				false);
		if (vo == null)
			return;

		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// ���ʽ�
		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// Ԥ��
		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());
		UFDouble nlockmny = UFDouble.ZERO_DBL;// �ƻ�Ԥ��

		if (PuPubVO.getString_TrimZeroLenAsNull(pk_plan) != null) {
			// ��ȡ�ƻ�Ԥ���ʽ�
			nlockmny = getPlanLockFund(pk_plan, vo.getPk_fundset());
		}

		// ʵ�� �� Ԥ�۵ô�С��ϵ
		UFDouble nsubmny = nmny.sub(nbeforemny);

		if (nfund.add(nlockmny).sub(nlockfund).sub(nactfund).sub(nsubmny)
				.doubleValue() < 0)
			throw new BusinessException("��" + HgPubTool.getSFundType(ifundtype)
					+ "����");
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ���ʽ� Ԥ�� 2011-2-2����01:01:39
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
			String billtype) throws BusinessException {
		if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
				.doubleValue())
			return;
		FUNDSETVO vo = getFundSet(scorpid, sdeptid, scorpid == null ? sdeptid
				: null, uDate, ifundtype, false);
		if (vo == null) {
			return;
		}

		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// ���ʽ�
		UFDouble oldnmny = PuPubVO.getUFDouble_NullAsZero(getNMY(pk, billtype));// �޸�ǰ���
		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// Ԥ��
		nlockfund = nlockfund.sub(oldnmny);
		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());// ʵ��
		UFDouble nlockmny = null;// Ԥ��
		if (PuPubVO.getString_TrimZeroLenAsNull(pk_plan) != null) {
			// ��ȡ�ƻ�Ԥ���ʽ�
			nlockmny = getPlanLockFund(pk_plan, vo.getPk_fundset());
		}
		
//		if (ifundtype == HgPubConst.FUND_CHECK_SPECIALFUND) {
//			nlockmny =getPlanLockFundByCorp(scorpid, sdeptid,uDate);
//		}
		boolean islock = nlockmny == null ? false : true;

		nlockmny = PuPubVO.getUFDouble_NullAsZero(nlockmny);

		// У���Ƿ����Ԥ�� ���ʽ�+Ԥ��-Ԥ��-ʵ��-����Ԥ��
		UFDouble nsubfund = nfund.add(nlockmny).sub(nlockfund).sub(nactfund)
				.sub(nmny);

		if (nsubfund.doubleValue() < 0) {
			throw new BusinessException("��" + HgPubTool.getSFundType(ifundtype)
					+ "����");
		}

		vo.setNlockfund(nlockfund.add(nmny));
		getDao().updateVO(vo, HgBsPubTool.CHECK_LOCkFUND_NAME);

		// ����Ԥ��
		if (!islock)
			return;
		UFDouble noldlockmny = nlockmny;
		nlockmny = nlockmny.sub(nmny);
		if (nlockmny.doubleValue() < 0)
			nlockmny = UFDouble.ZERO_DBL;
		updateLockFund(pk_plan, vo.getPrimaryKey(), nlockmny, noldlockmny,
				false);
	}

	public void clearLockFund(String pk_plan, String pk_fundset)
			throws BusinessException {
		String sql = "update hg_fundset_b set dr = 1 where pk_plan = '"
				+ pk_plan + "' and pk_fundset = '" + pk_fundset
				+ "' and isnull(dr,0)=0";
		getDao().executeUpdate(sql);
	}

	public void updateLockFund(String pk_plan, String pk_fundset,
			UFDouble nmny, UFDouble noldmny, boolean isAct)
			throws BusinessException {
		StringBuffer str = new StringBuffer();
		str.append("update hg_fundset_b set nlockmny = "
				+ PuPubVO.getUFDouble_NullAsZero(nmny));
		if (!isAct)
			str.append(" and noldlockmny = "
					+ PuPubVO.getUFDouble_NullAsZero(noldmny));
		str.append("where pk_plan = '" + pk_plan + "' and pk_fundset = '"
				+ pk_fundset + "' and isnull(dr,0)=0");
		// String sql = "update hg_fundset_b set nlockmny =
		// "+nmny.doubleValue()+" and noldlockmny = " +noldmny.doubleValue() +
		// "where pk_plan = '"+pk_plan+"' and pk_fundset = '"+pk_fundset+"' and
		// isnull(dr,0)=0";
		getDao().executeUpdate(str.toString());
	}
	/**
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-3-21����03:23:53
	 * @param pk_plan
	 * @param pk_fundset
	 * @param nmny
	 * @param noldmny
	 * @param isAct
	 * @throws BusinessException
	 */
	public void updateLockSpecalFund(String pk_planother_b,UFDouble locknmny)
			throws BusinessException {
		StringBuffer str = new StringBuffer();
		str.append("update hg_fundset_b set nmny = "+ PuPubVO.getUFDouble_NullAsZero(locknmny));
		str.append(" where pk_planother_b ='"+pk_planother_b+"'");
		getDao().executeUpdate(str.toString());
	}

	public UFDouble getPlanLockFund(String pk_plan, String pk_fundset)
			throws BusinessException {
		String sql = "select nlockmny from hg_fundset_b where  pk_plan = '"
				+ pk_plan + "' and pk_fundset = '" + pk_fundset
				+ "' and isnull(dr,0)=0";
		Object o = getDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
		return o == null ? null : PuPubVO.getUFDouble_NullAsZero(o);
		// return nmny;
	}
/**
 * ��ȡԤ���ʽ�
 * @author zhw
 * @˵�������׸ڿ�ҵ��
 * 2011-3-21����02:32:21
 * @param scorpid
 * @param sdeptid
 * @param date
 * @return
 * @throws BusinessException
 */
	public UFDouble getPlanLockFundByCorp(String scorpid,String sdeptid,UFDate date)
			throws BusinessException {
		String corp = PuPubVO.getString_TrimZeroLenAsNull(scorpid);
		String deptid = PuPubVO.getString_TrimZeroLenAsNull(sdeptid);

		if (corp == null && deptid == null )
			return null;
		StringBuffer strb = new StringBuffer();
		strb.append("select sum(nlockmny-(coalesce(nmny,0.0))) from hg_fundset_b where");
		if (corp != null) {
			strb.append("  pk_corp = '" + corp + "' and ");
		}
		if (deptid != null) {
			strb.append(" cdeptid = '" + deptid + "' and ");
		} 
		
		// �õ���׼�ڼ䷽��������ʵ��
		AccountCalendar calendar = AccountCalendar.getInstance();
		calendar.setDate(date);
		// ��������
		String iyear = calendar.getYearVO().getPeriodyear();
		String imonth = calendar.getMonthVO().getMonth();

		strb.append(" iyear = '" + String.valueOf(iyear)
				+ "' and imonth = '" + String.valueOf(imonth) + "'");
		
		strb.append(" and isnull(dr,0)=0");
		Object o = getDao().executeQuery(strb.toString(), HgBsPubTool.COLUMNPROCESSOR);
		return o == null ? null : PuPubVO.getUFDouble_NullAsZero(o);
	}

	public UFDouble getPlanLockFundByPk(String pk_plan, String pk_fundset)
			throws BusinessException {
		String sql = "select nlockmny from hg_fundset_b where  pk_plan = '"
				+ pk_plan + "' and pk_fundset = '" + pk_fundset
				+ "' and isnull(dr,0)=0";
		Object o = getDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
		return o == null ? null : PuPubVO.getUFDouble_NullAsZero(o);
	}

	public UFDouble getLockFundForPlan(String pk_plan, String pk_fundset)
			throws BusinessException {
		String sql = "select nlockmny from hg_fundset_b where  pk_plan = '"
				+ pk_plan + "' and pk_fundset = '" + pk_fundset
				+ "' and isnull(dr,0)=0";
		Object o = getDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
		return o == null ? null : PuPubVO.getUFDouble_NullAsZero(o);
	}

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
	 * @˵�������׸ڿ�ҵ��ʵ�� ����ʵ�ʳ������������� �۳� �ʽ� �޶� ��ר���ʽ� 2011-2-2����01:49:26
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nbeforemny
	 *            Ԥ��
	 * @param nmny
	 *            ʵ��
	 * @throws BusinessException
	 */
	public void useFund(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nbeforemny, UFDouble nmny,
			String pk, String billtype) throws BusinessException {
		// �ۼ�˳�� Ԥ�� Ԥ�� ���ʽ�
		if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
				.doubleValue())
			return;
		// add by zhw 2010-12-28
		FUNDSETVO vo = getFundSet(scorpid, sdeptid, null, uDate, ifundtype,
				false);
		if (vo == null)
			return;
		// add by zhw 2010-12-28 end

		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// ���ʽ�
		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// Ԥ��
		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());
		// modify by zhw 2010-12-28 begin
		UFDouble oldnmny = PuPubVO.getUFDouble_NullAsZero(getNMY(pk, billtype));// �޸�ǰ���
		nactfund = nactfund.sub(oldnmny);
		// modify by zhw 2010-12-28 end
		UFDouble nlockmny = null;// �ƻ�Ԥ��

		if (PuPubVO.getString_TrimZeroLenAsNull(pk_plan) != null) {
			// ��ȡ�ƻ�Ԥ���ʽ�
			nlockmny = getPlanLockFund(pk_plan, vo.getPk_fundset());
		}

		boolean islock = nlockmny == null ? false : true;

		nlockmny = PuPubVO.getUFDouble_NullAsZero(nlockmny);

		// ʵ�� �� Ԥ�۵ô�С��ϵ
		UFDouble nsubmny = nmny.sub(nbeforemny);

		if (nfund.add(nlockmny).sub(nlockfund).sub(nactfund).sub(nsubmny)
				.doubleValue() < 0)
			throw new BusinessException("��" + HgPubTool.getSFundType(ifundtype)
					+ "����");

		// nlockfund = nlockfund.sub(nbeforemny);
		nlockfund = nlockfund.sub(nmny.sub(oldnmny));
		nactfund = nactfund.add(nmny);
		vo.setNlockfund(nlockfund);
		vo.setNactfund(nactfund);
		getDao().updateVO(vo, HgBsPubTool.CHECK_FUND_USENAMES);

		if (!islock)
			return;

		// ����Ԥ��
		nlockmny = nlockmny.sub(nsubmny);
		if (nlockmny.doubleValue() < 0)
			nlockmny = UFDouble.ZERO_DBL;
		UFDouble noldlockmny = PuPubVO
				.getUFDouble_NullAsZero(getPlanOldLockFund(pk_plan, vo
						.getPk_fundset()));
		if (nlockmny.doubleValue() > noldlockmny.doubleValue())
			nlockmny = noldlockmny;

		updateLockFund(pk_plan, vo.getPrimaryKey(), nlockmny, null, true);
	}

	public void reUseFund(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nbeforemny, UFDouble nmny)
			throws BusinessException {

		if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
				.doubleValue())
			return;
		FUNDSETVO vo = getFundSet(scorpid, sdeptid, null, uDate, ifundtype,
				false);
		if (vo == null) {
			return;
		}

		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// Ԥ��
		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());// ʵ��

		UFDouble nlockmny = null;// Ԥ��
		if (PuPubVO.getString_TrimZeroLenAsNull(pk_plan) != null) {
			// ��ȡ�ƻ�Ԥ���ʽ�
			nlockmny = getPlanLockFund(pk_plan, vo.getPk_fundset());
		}
		boolean isLock = nlockmny == null ? false : true;

		if (nactfund.doubleValue() < nmny.doubleValue())
			throw new BusinessException("���ݴ��󣬳�ʵ�۽��");

		nactfund = nactfund.sub(nmny);
		// nlockfund = nlockfund.add(nbeforemny);
		nlockfund = nlockfund.add(nmny);
		UFDouble nsubmny = nbeforemny.sub(nmny);

		if (isLock) {//
			UFDouble noldlockmny = PuPubVO
					.getUFDouble_NullAsZero(getPlanOldLockFund(pk_plan, vo
							.getPk_fundset()));
			nlockmny = nlockmny.sub(nsubmny);
			if (nlockmny.doubleValue() > noldlockmny.doubleValue()) {
				nlockmny = noldlockmny;
			}
		}

		vo.setNactfund(nactfund);
		vo.setNlockfund(nlockfund);
		getDao().updateVO(vo, HgBsPubTool.CHECK_FUND_USENAMES);

		updateLockFund(pk_plan, vo.getPrimaryKey(), nlockmny, null, true);
	}

	public void reUseFund_before(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nmny)
			throws BusinessException {
		// TODO Auto-generated method stub

		if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
				.doubleValue())
			return;
		FUNDSETVO vo = getFundSet(scorpid, sdeptid, scorpid == null ? sdeptid
				: null, uDate, ifundtype, false);
		if (vo == null) {
			return;
		}

		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// Ԥ��

		UFDouble nlockmny = null;// Ԥ��
		if (PuPubVO.getString_TrimZeroLenAsNull(pk_plan) != null) {
			// ��ȡ�ƻ�Ԥ���ʽ�
			nlockmny = getPlanLockFund(pk_plan, vo.getPk_fundset());
		}
		boolean isLock = nlockmny == null ? false : true;

		if (nlockfund.doubleValue() < nmny.doubleValue())
			throw new BusinessException("���ݴ��󣬳�Ԥ�۽��");

		nlockfund = nlockfund.sub(nmny);

		if (!isLock) {
			vo.setNlockfund(nlockfund);
			getDao().updateVO(vo, HgBsPubTool.CHECK_FUND_USENAMES);
			return;
		}

		nlockmny = nlockmny.add(nmny);// �س�Ԥ�۸� Ԥ���ʽ�

		// ԭԤ�����
		UFDouble noldlockmny = PuPubVO
				.getUFDouble_NullAsZero(getPlanOldLockFund(pk_plan, vo
						.getPk_fundset()));
		if (nlockmny.doubleValue() > noldlockmny.doubleValue())
			nlockmny = noldlockmny;

		vo.setNlockfund(nlockfund);
		getDao().updateVO(vo, HgBsPubTool.CHECK_FUND_USENAMES);
		updateLockFund(pk_plan, vo.getPrimaryKey(), nmny, UFDouble.ZERO_DBL,
				false);
	}

	/**
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��ʵ�� ����ʵ�ʳ������������� �۳� �ʽ� �޶� 2011-2-2����01:49:26
	 * @param pk_plan
	 *            ���ڲ��ϳ���
	 * @param ifundtype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nbeforemny
	 *            Ԥ��
	 * @param nmny
	 *            ʵ��
	 * @throws BusinessException
	 */
	public void useFundMater(String pk_plan, int ifundtype, String sdeptid,
			String cust, UFDate uDate, UFDouble nbeforemny, UFDouble nmny,
			String pk, String billtype) throws BusinessException {
		// �ۼ�˳�� Ԥ�� Ԥ�� ���ʽ�
		if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
				.doubleValue())
			return;
		// add by zhw 2010-12-28
		FUNDSETVO vo = getFundSet(null, sdeptid, cust, uDate, ifundtype, false);
		if (vo == null)
			return;
		// add by zhw 2010-12-28 end

		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// ���ʽ�
		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// Ԥ��
		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());
		UFDouble oldnmny = PuPubVO.getUFDouble_NullAsZero(getNMY(pk, billtype));// �޸�ǰ���
		nactfund = nactfund.sub(oldnmny);
		UFDouble nlockmny = null;// �ƻ�Ԥ��

		if (PuPubVO.getString_TrimZeroLenAsNull(pk_plan) != null) {
			// ��ȡ�ƻ�Ԥ���ʽ�
			nlockmny = getPlanLockFund(pk_plan, vo.getPk_fundset());
		}

		boolean islock = nlockmny == null ? false : true;

		nlockmny = PuPubVO.getUFDouble_NullAsZero(nlockmny);

		// ʵ�� �� Ԥ�۵ô�С��ϵ
		UFDouble nsubmny = nmny.sub(nbeforemny);

		if (nfund.add(nlockmny).sub(nlockfund).sub(nactfund).sub(nsubmny)
				.doubleValue() < 0)
			throw new BusinessException("��" + HgPubTool.getSFundType(ifundtype)
					+ "����");

		nlockfund = nlockfund.sub(nbeforemny);
		nactfund = nactfund.add(nmny);
		vo.setNlockfund(nlockfund);
		vo.setNactfund(nactfund);
		getDao().updateVO(vo, HgBsPubTool.CHECK_FUND_USENAMES);

		if (!islock)
			return;

		// ����Ԥ��
		nlockmny = nlockmny.sub(nsubmny);
		if (nlockmny.doubleValue() < 0)
			nlockmny = UFDouble.ZERO_DBL;
		UFDouble noldlockmny = PuPubVO
				.getUFDouble_NullAsZero(getPlanOldLockFund(pk_plan, vo
						.getPk_fundset()));
		if (nlockmny.doubleValue() > noldlockmny.doubleValue())
			nlockmny = noldlockmny;

		updateLockFund(pk_plan, vo.getPrimaryKey(), nlockmny, null, true);
	}

	/**
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��ʵ�� ����ʵ�ʳ������������� �۳� �ʽ� �޶� 2011-2-2����01:49:26
	 * @param pk_plan
	 *            ���ڲ��ϳ���
	 * @param ifundtype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nbeforemny
	 *            Ԥ��
	 * @param nmny
	 *            ʵ��
	 * @throws BusinessException
	 */
	public void reUseFundMater(String pk_plan, int ifundtype, String sdeptid,
			String cust, UFDate uDate, UFDouble nbeforemny, UFDouble nmny)
			throws BusinessException {

		if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
				.doubleValue())
			return;
		FUNDSETVO vo = getFundSet(null, sdeptid, cust, uDate, ifundtype, false);
		if (vo == null) {
			return;
		}

		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// Ԥ��
		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());// ʵ��

		UFDouble nlockmny = null;// Ԥ��
		if (PuPubVO.getString_TrimZeroLenAsNull(pk_plan) != null) {
			// ��ȡ�ƻ�Ԥ���ʽ�
			nlockmny = getPlanLockFund(pk_plan, vo.getPk_fundset());
		}
		boolean isLock = nlockmny == null ? false : true;

		if (nactfund.doubleValue() < nmny.doubleValue())
			throw new BusinessException("���ݴ��󣬳�ʵ�۽��");

		nactfund = nactfund.sub(nmny);
		nlockfund = nlockfund.add(nbeforemny);

		UFDouble nsubmny = nbeforemny.sub(nmny);

		if (isLock) {//
			UFDouble noldlockmny = PuPubVO
					.getUFDouble_NullAsZero(getPlanOldLockFund(pk_plan, vo
							.getPk_fundset()));
			nlockmny = nlockmny.sub(nsubmny);
			if (nlockmny.doubleValue() > noldlockmny.doubleValue()) {
				nlockmny = noldlockmny;
			}
		}

		vo.setNactfund(nactfund);
		vo.setNlockfund(nlockfund);
		getDao().updateVO(vo, HgBsPubTool.CHECK_FUND_USENAMES);

		updateLockFund(pk_plan, vo.getPrimaryKey(), nlockmny, null, true);
	}

	/**
	 * ��ȡ�޸�ǰ�Ľ��
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-1-8����06:26:15
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
			String sql = "select sum(b.nnotaxmny) from to_bill h join to_bill_b b on h.cbillid=b.cbillid where h.cbillid= '"
					+ pk
					+ "' and h.cbilltype = '"
					+ ScmConst.m_sBillDBDD
					+ "' and isnull(h.dr,0)=0 and isnull(b.dr,0)=0";
			Object o = getDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
			return o == null ? null : PuPubVO.getUFDouble_NullAsZero(o);
		} else {
			return UFDouble.ZERO_DBL;
		}
	}

	public void useSpeacialFund_Before(String pk_plan, int ifundtype,
			String scorpid, String sdeptid, UFDate date, UFDouble nmny,
			String pk, String billtype, Object[][] o)
			throws BusinessException {
		if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
				.doubleValue())
			return;
		FUNDSETVO vo = getFundSet(scorpid, sdeptid, scorpid == null ? sdeptid
				: null, date, ifundtype, false);
		if (vo == null) {
			return;
		}

		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// ���ʽ�
		UFDouble oldnmny = PuPubVO.getUFDouble_NullAsZero(getNMY(pk, billtype));// �޸�ǰ���
		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// Ԥ��
		nlockfund = nlockfund.sub(oldnmny);
		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());// ʵ��
		UFDouble nlockmny = null;// Ԥ��
//		if (PuPubVO.getString_TrimZeroLenAsNull(pk_plan) != null) {
//			// ��ȡ�ƻ�Ԥ���ʽ�
//			nlockmny = getPlanLockFund(pk_plan, vo.getPk_fundset());
//		}
		
		if (ifundtype == HgPubConst.FUND_CHECK_SPECIALFUND) {
			nlockmny =getPlanLockFundByCorp(scorpid, sdeptid,date);
		}
		boolean islock = nlockmny == null ? false : true;

		nlockmny = PuPubVO.getUFDouble_NullAsZero(nlockmny);

		// У���Ƿ����Ԥ�� ���ʽ�-Ԥ��-Ԥ��-ʵ��-����Ԥ��
		UFDouble nsubfund = nfund.sub(nlockmny).sub(nlockfund).sub(nactfund)
				.sub(nmny);

		if (nsubfund.doubleValue() < 0) {
			throw new BusinessException("��" + HgPubTool.getSFundType(ifundtype)
					+ "����");
		}

		vo.setNlockfund(nlockfund.add(nmny));
		getDao().updateVO(vo, HgBsPubTool.CHECK_LOCkFUND_NAME);

		// ����Ԥ��
		if (!islock)
			return;
		int len = o.length;
		for(int i=0;i<len;i++){
			UFDouble locknmny= PuPubVO.getUFDouble_NullAsZero(o[i][1]);
			String pk_planother_b = PuPubVO.getString_TrimZeroLenAsNull(o[i][0]);
			updateLockSpecalFund(pk_planother_b,locknmny);
		}
//		UFDouble noldlockmny = nlockmny;
//		nlockmny = nlockmny.sub(nmny);
//		if (nlockmny.doubleValue() < 0)
//			nlockmny = UFDouble.ZERO_DBL;
//		updateLockFund(pk_plan, vo.getPrimaryKey(), nlockmny, noldlockmny,
//				false);
		
//		updateLockSpecalFund()
		
	}
}

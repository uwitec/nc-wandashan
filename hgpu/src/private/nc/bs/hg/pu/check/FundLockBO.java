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
		strb.append(" and vdef2 = '" + loginCorp + "'");
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


//	public void reLockFundForPlan(HYBillVO planvo) throws BusinessException {
//		if (planvo == null)
//			return;
//		PlanVO head = (PlanVO) planvo.getParentVO();
//		PlanOtherBVO[] bodys = (PlanOtherBVO[]) planvo.getChildrenVO();
//		// �����ڽ��л��ܽ��
//		java.util.Map<String, UFDate> dateMap = new HashMap<String, UFDate>();
//		String key = null;
//		UFDate date = null;
//		for (PlanOtherBVO body : bodys) {
//			date = body.getDreqdate();// �������� Ϊ ��Ч�� ����
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
//		if (iplantype == HgPubConst.FUND_CHECK_SPECIALFUND) {// ר���ʽ�
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
	 * @˵�������׸ڿ�ҵ���ʽ� �޶� ר���ʽ� �� Ԥ�� 2011-2-2����01:01:39  ��Ԥ������µ�Ԥ��
	 *                   Ŀǰϵͳ��ר���ʽ�ƻ�����Ԥ������
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

		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// ���ʽ�
		UFDouble oldnmny = PuPubVO.getUFDouble_NullAsZero(getNMY(pk, billtype));// �޸�ǰ���
		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// Ԥ��
		UFDouble nsubmny = nmny.sub(oldnmny);//Ԥ������ֵ
		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());// ʵ��
//		UFDouble nlockmny = UFDouble.ZERO_DBL;// Ԥ��------------------------------zhf �ʽ���޶����Ԥ�� ע�͵� 2011-04-13

		// У���Ƿ����Ԥ�� ���ʽ�-Ԥ��-ʵ��-Ԥ������ֵ>0?
		UFDouble nsubfund = nfund.sub(nlockfund).sub(nactfund)
				.sub(nsubmny);

		if (nsubfund.doubleValue() < 0) {
			throw new BusinessException("��" + HgPubTool.getSFundType(ifundtype)
					+ "����");
		}

		vo.setNlockfund(nlockfund.add(nsubmny,HgPubConst.MNY_DIGIT));
		getDao().updateVO(vo, HgBsPubTool.CHECK_LOCkFUND_NAME);		
		
		//����У��
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
//	 * @˵�������׸ڿ�ҵ�� 2011-3-21����03:23:53
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
//	public void useFund(String pk_plan, int ifundtype, String scorpid,
//			String sdeptid, UFDate uDate, UFDouble nbeforemny, UFDouble nmny,
//			String pk, String billtype, String loginCorp)
//			throws BusinessException {
//		// �ۼ�˳�� Ԥ�� Ԥ�� ���ʽ�
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
//		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// ���ʽ�
//		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// Ԥ��
//		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());
//		// modify by zhw 2010-12-28 begin
//		UFDouble oldnmny = PuPubVO.getUFDouble_NullAsZero(getNMY(pk, billtype));// �޸�ǰ���
//		nactfund = nactfund.sub(oldnmny);
//		// modify by zhw 2010-12-28 end
//		UFDouble nlockmny = null;// �ƻ�Ԥ��
//
//		if (PuPubVO.getString_TrimZeroLenAsNull(pk_plan) != null) {
//			// ��ȡ�ƻ�Ԥ���ʽ�
//			nlockmny = getPlanLockFund(pk_plan, vo.getPk_fundset());
//		}
//
//		boolean islock = nlockmny == null ? false : true;
//
//		nlockmny = PuPubVO.getUFDouble_NullAsZero(nlockmny);
//
//		// ʵ�� �� Ԥ�۵ô�С��ϵ
//		UFDouble nsubmny = nmny.sub(nbeforemny);
//
//		if (nfund.add(nlockmny).sub(nlockfund).sub(nactfund).sub(nsubmny)
//				.doubleValue() < 0)
//			throw new BusinessException("��" + HgPubTool.getSFundType(ifundtype)
//					+ "����");
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
//		// ����Ԥ��
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
//		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// Ԥ��
//		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());// ʵ��
//		UFDouble nlockmny = null;// Ԥ��
//		if (PuPubVO.getString_TrimZeroLenAsNull(pk_plan) != null) {
//			// ��ȡ�ƻ�Ԥ���ʽ�
//			nlockmny = getPlanLockFund(pk_plan, vo.getPk_fundset());
//		}
//		boolean isLock = nlockmny == null ? false : true;
//
//		if (nactfund.doubleValue() < nmny.doubleValue())
//			throw new BusinessException("���ݴ��󣬳�ʵ�۽��");
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
			String cust, UFDate uDate, UFDouble nmny,
			String pk, String billtype, String loginCorp)
			throws BusinessException {
		// �ۼ�˳�� Ԥ�� Ԥ�� ���ʽ�
//		if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
//				.doubleValue())
//			return;
		// add by zhw 2010-12-28
		FUNDSETVO vo = getFundSet(pk_plan, sdeptid, cust, uDate, ifundtype,
				false, loginCorp);
		if (vo == null)
			return;
		// add by zhw 2010-12-28 end

		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// ���ʽ�
		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());
		UFDouble oldnmny = PuPubVO.getUFDouble_NullAsZero(getNMY(pk, billtype));// �޸�ǰ���
		nmny = nmny.sub(oldnmny);//ʵ�۱�������
		if (nfund.sub(nactfund).sub(nmny)
				.doubleValue() < 0)
			throw new BusinessException("��" + HgPubTool.getSFundType(ifundtype)
					+ "����");
		nactfund = nactfund.add(nmny,HgPubConst.MNY_DIGIT);
		vo.setNactfund(nactfund);
		getDao().updateVO(vo, HgBsPubTool.CHECK_FUND_USENAMES);
		
		//����У��
		checkFundAfterUse(vo.getPrimaryKey(), ifundtype, null, uDate, loginCorp);
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
//		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// Ԥ��
//		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());// ʵ��
//
//		UFDouble nlockmny = null;// Ԥ��
//		if (PuPubVO.getString_TrimZeroLenAsNull(pk_plan) != null) {
//			// ��ȡ�ƻ�Ԥ���ʽ�
//			nlockmny = getPlanLockFund(pk_plan, vo.getPk_fundset());
//		}
//		boolean isLock = nlockmny == null ? false : true;
//
//		if (nactfund.doubleValue() < nmny.doubleValue())
//			throw new BusinessException("���ݴ��󣬳�ʵ�۽��");
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
		     // 0  �ƻ���   1  ��ͬ��
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
//		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// ���ʽ�
//		UFDouble oldnmny = PuPubVO.getUFDouble_NullAsZero(getNMY(pk, billtype));// �޸�ǰ���
//		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// Ԥ��
//		nlockfund = nlockfund.sub(oldnmny);
//		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());// ʵ��
//		UFDouble nlockmny = null;// Ԥ��
//		// if (PuPubVO.getString_TrimZeroLenAsNull(pk_plan) != null) {
//		// // ��ȡ�ƻ�Ԥ���ʽ�
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
//		// У���Ƿ����Ԥ�� ���ʽ�-Ԥ��-Ԥ��-ʵ��-����Ԥ��
//		UFDouble nsubfund = nfund.sub(nlockmny).sub(nlockfund).sub(nactfund)
//				.sub(nmny);
//
//		if (nsubfund.doubleValue() < 0) {
//			throw new BusinessException("��" + HgPubTool.getSFundType(ifundtype)
//					+ "����");
//		}
//
//		vo.setNlockfund(nlockfund.add(nmny));
//		getDao().updateVO(vo, HgBsPubTool.CHECK_LOCkFUND_NAME);
//
//		// ����Ԥ��
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
	 * ר���ʽ�ƻ�Ԥ��
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

		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// ���ʽ�
		UFDouble oldnmny = PuPubVO.getUFDouble_NullAsZero(getNMY(pk, ScmConst.m_sBillDBDD));// �޸�ǰ���
		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// Ԥ��
		UFDouble nsubmny = nmny.sub(oldnmny);//Ԥ������ֵ
		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());// ʵ��	

		UFDouble nlockmny = null;// �ƻ�����Ԥ��
		UFDouble nalllockmny = null;//�õ�λȫ��Ԥ��

		boolean islock = false;

		islock = getPrepareBO().isYL(pk_plan_b,date);
		if(islock){
			nlockmny = PuPubVO.getUFDouble_NullAsZero(getPrepareBO().getPlanLockEnableMny(pk_plan_b, date));
			nalllockmny = PuPubVO.getUFDouble_NullAsZero(getPrepareBO().getFundPrepareMny(null, scorpid, sdeptid, null, date, HgPubConst.FUND_CHECK_SPECIALFUND, loginCorp));

		}


		// У���Ƿ����Ԥ�� ���ʽ�-ȫ��Ԥ��+����Ԥ��-Ԥ��-ʵ��-����Ԥ������ֵ>0
		UFDouble nsubfund = nfund.sub(PuPubVO.getUFDouble_NullAsZero(nalllockmny)).add(PuPubVO.getUFDouble_NullAsZero(nlockmny)).sub(nlockfund).sub(nactfund)
		.sub(nsubmny);

		if (nsubfund.doubleValue() < 0) {
			throw new BusinessException("��" + HgPubTool.getSFundType(HgPubConst.FUND_CHECK_SPECIALFUND)
					+ "����");
		}

		vo.setNlockfund(nlockfund.add(nsubmny,HgPubConst.MNY_DIGIT));
		getDao().updateVO(vo, HgBsPubTool.CHECK_LOCkFUND_NAME);

		// ����Ԥ��
		if (!islock)
			return;
		//		��Ԥ������ֵ ������ Ԥ����  У��������  �ۼ�Ԥ�����ܴ���ԭʼԤ��  �ۼ�Ԥ������С��0
		getPrepareBO().adjustPrepareMnyOnBefore(pk_plan_b, nsubmny);
		
		//����У��
		checkFundAfterUse(vo.getPrimaryKey(), HgPubConst.FUND_CHECK_SPECIALFUND, pk_plan_b, date, loginCorp);
	}

	/**
	 * 
	 * @author zhw ���ڵ�������
	 * @˵�������׸ڿ�ҵ��ʵ�� ����ʵ�ʳ������������� �۳� �ʽ� �޶� ��ר���ʽ� 2011-2-2����01:49:26
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nordermny ��Դ������ԭʼ���  ԭʼԤ��
	 * @param nbeforemny
	 *            ���ο���Ԥ��    ��Ԥ��-�ۼ���ʹ��Ԥ�� = ��������˰���-�������ۼ��������*��˰���ۣ�
	 * @param nmny
	 *            ����ʵ��ֵ
	 * @throws BusinessException
	 */
	public void useFundForAllOut(String pk_plan_b, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate,UFDouble nordermny, UFDouble nbeforemny, UFDouble nmny,
			String pk, String billtype, String loginCorp)
			throws BusinessException {
		
		/**
		 * zhf 20110413  �㷨������Ԥ�ۺ� ֻ�ܸ�Ʒ��ʵ��ʱʹ�ø�Ԥ��  ��������ʹ�� ����Ϊ������ϸ����ʵ��
		 * ����ʵ�۱���ʱ   �ۼ�˳��Ϊ��  Ԥ��  Ԥ�� ���ʽ�    �ظ��ǵĴ���ͬ
		 * ����ʵ�۱���ʱ   �����߼�������
		 * ɾ��ʱ          ��ͬ�ڵ���ʵ��  ��ʵ�۵�Ϊ0
		 * ����ɾ��ʱ       ͬɾ��
		 * 
		 */
		
		// �ۼ�˳�� Ԥ�� Ԥ�� ���ʽ�
//		if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
//				.doubleValue())
//			return;
		// add by zhw 2010-12-28
		FUNDSETVO vo = getFundSet(scorpid, sdeptid, null, uDate, ifundtype,
				false, loginCorp);
		if (vo == null)
			return;
		// add by zhw 2010-12-28 end

		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// ���ʽ�
		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// Ԥ��
		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());//ʵ��
		// modify by zhw 2010-12-28 begin
		UFDouble oldnmny = PuPubVO.getUFDouble_NullAsZero(getOldActMny(pk, billtype));// ԭʵ��ֵ
		UFDouble nsubmny = nmny.sub(oldnmny);//ʵ������ֵ
		if(nsubmny.equals(UFDouble.ZERO_DBL))//ʵ��ֵδ�����仯
			return;
		// modify by zhw 2010-12-28 end
		UFDouble nlockmny = UFDouble.ZERO_DBL;// �ƻ�����Ԥ��
		UFDouble nalllockmny = UFDouble.ZERO_DBL;//�õ�λȫ��Ԥ��

		boolean islock = false;
		if (ifundtype == HgPubConst.FUND_CHECK_SPECIALFUND) {
			islock = getPrepareBO().isYL(pk_plan_b,uDate);
			if(islock){
				nlockmny = PuPubVO.getUFDouble_NullAsZero(getPrepareBO().getPlanLockEnableMny(pk_plan_b, uDate));
				nalllockmny = PuPubVO.getUFDouble_NullAsZero(getPrepareBO().getFundPrepareMny(null, scorpid, sdeptid, null, uDate, ifundtype, loginCorp));
				
			}
		}
		
//		�����ʽ�=���ʽ�-ȫ��Ԥ��+����Ԥ��-ȫ��Ԥ��+����Ԥ��-ȫ��ʵ��
		UFDouble nallmny = nfund.sub(nalllockmny).add(nlockmny).sub(nlockfund).add(nbeforemny).sub(nactfund);
		
		if(oldnmny.doubleValue()==0){//��һ��ʵ��
			boolean isadjust = nmny.doubleValue()>nbeforemny.doubleValue();
//			if(isadjust){
//				У���Ƿ����ʵ�۳��� 
				if(nallmny.doubleValue()<nmny.doubleValue()){
					throw new BusinessException("��" + HgPubTool.getSFundType(ifundtype)
							+ "���ơ������"+String.valueOf(nmny.sub(nallmny,HgPubConst.MNY_DIGIT).doubleValue()));
				}
			
//			������ʵ��
			nactfund = nactfund.add(nmny,HgPubConst.MNY_DIGIT);
//			������Ԥ��
			nlockfund = nlockfund.sub(nbeforemny).add(HgPubTool.getNBeforeMnyWhenAct(nbeforemny, nmny),HgPubConst.MNY_DIGIT);			
			if(islock && isadjust){//����Ԥ��
//				//�����Ԥ�� ����� ��Ҫ�ȴ�Ԥ���Ͽ۳�
				getPrepareBO().updateFundSetBNumOnUseFund(pk_plan_b, nlockmny,nmny.sub(nbeforemny),false);
			}
		}else if(oldnmny.doubleValue()>0){//ʵ���޸�
			if(nsubmny.doubleValue()>0){//ʵ������
//				У���Ƿ����ʵ�۳��� 
				if(nallmny.doubleValue()<nsubmny.doubleValue()){
					throw new BusinessException("��" + HgPubTool.getSFundType(ifundtype)
							+ "���ơ������"+String.valueOf(nsubmny.sub(nallmny,HgPubConst.MNY_DIGIT).doubleValue()));
				}			
//				�����µ�ʵ��
				nactfund = nactfund.add(nsubmny,HgPubConst.MNY_DIGIT);				
//				�� Ԥ��  Ԥ��  ���ʽ� ����  ������
//				1�������Ƿ���Ԥ�ۿ���
				boolean isYK = nbeforemny.doubleValue()>0;
				if(isYK){
					if(nbeforemny.doubleValue()>nsubmny.doubleValue()){
						nlockfund = nlockfund.sub(nsubmny,HgPubConst.MNY_DIGIT);
					}else{
						nlockfund = nlockfund.sub(nbeforemny,HgPubConst.MNY_DIGIT);
						//�����Ԥ����Ҫ����Ԥ��
						if(islock){
							getPrepareBO().updateFundSetBNumOnUseFund(pk_plan_b, nlockmny,nsubmny.sub(nbeforemny),false);
						}
					}				
				}else{//û��ʣ��Ԥ���� ����Ԥ��
					if(islock){
						getPrepareBO().updateFundSetBNumOnUseFund(pk_plan_b, nlockmny,nsubmny,false);				
					}	
				}
			}else if(nsubmny.doubleValue()<0){//ʵ�ۼ�С
				//				���ٵ�ʵ��Ҫ ��������� �ظ� Ԥ��  Ԥ��  ���ʽ�			
				//				�����µ�ʵ��
				nactfund = nactfund.add(nsubmny,HgPubConst.MNY_DIGIT);
				boolean isYK = nbeforemny.doubleValue()>=0;
				if(isYK){
					nlockfund = nlockfund.add(nsubmny.abs(),HgPubConst.MNY_DIGIT);
				}else{// ����Ԥ��
					//�Ȼظ���Ԥ��  ��Ҫ�� nsubmny  ��  �������-�ۼƳ����� nsub2
					/**
					 * nsubmny>nsub2   Ԥ��+nsubmny-nsub2
					 * else  Ԥ��= Ԥ��
					 */
					if(nsubmny.abs().doubleValue()>=nbeforemny.abs().doubleValue()){
						//ȫ�����ظ�Ԥ��
						nlockfund = nlockfund.add(nsubmny.abs().sub(nbeforemny.abs()),HgPubConst.MNY_DIGIT);
						//ʣ��ĵ���Ԥ��
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
		//����У��
		checkFundAfterUse(vo.getPrimaryKey(), ifundtype, pk_plan_b, uDate, loginCorp);
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��ʵ�ۺ�У���ʽ�̨��
	 * 2011-4-22����05:59:05
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
			throw new BusinessException("�ʽ����������쳣");
		
		FUNDSETVO vo = getPrepareBO().getFundSetVoByID(fundid);
		if(vo == null)
			throw new BusinessException("�ʽ����������쳣");
		
		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// ���ʽ�
		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// Ԥ��
		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());//ʵ��
		// modify by zhw 2010-12-28 end
//		UFDouble nlockmny = UFDouble.ZERO_DBL;// �ƻ�����Ԥ��
		UFDouble nalllockmny = UFDouble.ZERO_DBL;//�õ�λȫ��Ԥ��

		boolean islock = false;
		if (ifundtype == HgPubConst.FUND_CHECK_SPECIALFUND||pk_plan_b!=null) {
			islock = getPrepareBO().isYL(pk_plan_b,uDate);
			if(islock){
//				nlockmny = PuPubVO.getUFDouble_NullAsZero(getPrepareBO().getPlanLockEnableMny(pk_plan_b, uDate));
				nalllockmny = PuPubVO.getUFDouble_NullAsZero(getPrepareBO().getFundPrepareMny(null, vo.getPk_corp(), vo.getCdeptid(), null, uDate, ifundtype, logincorp));				
			}
		}
		
		if(nlockfund.doubleValue()<0)
			throw new BusinessException("�ʽ�ۼ��쳣,Ԥ��ֵ���ֺ���");
		if(nactfund.doubleValue()<0)
			throw new BusinessException("�ʽ�ۼ��쳣,ʵ��ֵ���ֺ���");
		
//		�����ʽ�=���ʽ�-ȫ��Ԥ��-ȫ��Ԥ��-ȫ��ʵ��
		UFDouble nallmny = nfund.sub(nalllockmny).sub(nlockfund).sub(nactfund);
		if(nallmny.doubleValue()<0)
			throw new BusinessException("�ʽ�ۼ��쳣,ʵ��+Ԥ�۳����ʽ�");
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
		 * zhf 20110413  �㷨������Ԥ�ۺ� ֻ�ܸ�Ʒ��ʵ��ʱʹ�ø�Ԥ��  ��������ʹ�� ����Ϊ������ϸ����ʵ��
		 * ����ʵ�۱���ʱ   �ۼ�˳��Ϊ��  Ԥ��  Ԥ�� ���ʽ�    �ظ��ǵĴ���ͬ
		 * ����ʵ�۱���ʱ   �����߼�������
		 * ɾ��ʱ          ��ͬ�ڵ���ʵ��  ��ʵ�۵�Ϊ0
		 * ����ɾ��ʱ       ͬɾ��
		 * 
		 */
		
		// �ۼ�˳�� Ԥ�� Ԥ�� ���ʽ�
//		if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
//				.doubleValue())
//			return;
		// �������ʽ��ȡ��ʽ
		String vdef1 = null;
		FUNDSETVO vo = null;

		vdef1 = PuPubVO.getString_TrimZeroLenAsNull(getCustomer(customid));
		if (vdef1 == null)
			throw new BusinessException("���̵��������ϵ�λ֮��Ĺ�ϵ������,��鿴�Ƿ�ά��");
		vo = getFundSet(null, null, vdef1, uDate, ifundtype, false, loginCorp);
		
		if (vo == null) {
			return;
		}
		// add by zhw 2010-12-28 end

		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// ���ʽ�
		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// Ԥ��
		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());//ʵ��
		// modify by zhw 2010-12-28 begin
		UFDouble oldnmny = PuPubVO.getUFDouble_NullAsZero(getSaleOutOldActMny(pk, nprice));// ԭʵ��ֵ
		UFDouble nsubmny = nmny.sub(oldnmny);//ʵ������ֵ
		if(nsubmny.equals(UFDouble.ZERO_DBL))//ʵ��ֵδ�����仯
			return;	
//		�����ʽ�=���ʽ�-ȫ��Ԥ��+����Ԥ��-ȫ��ʵ��
		UFDouble nallmny = nfund.sub(nlockfund).add(nbeforemny).sub(nactfund,HgPubConst.MNY_DIGIT);	
		if(oldnmny.doubleValue()==0){//��һ��ʵ��
//				У���Ƿ����ʵ�۳��� 
				if(nallmny.doubleValue()<nmny.doubleValue()){
					throw new BusinessException("�ʽ���޶��.�����"+String.valueOf(nmny.sub(nallmny,HgPubConst.MNY_DIGIT).doubleValue()));
				}
//			������ʵ��
			nactfund = nactfund.add(nmny,HgPubConst.MNY_DIGIT);
//			������Ԥ��
			nlockfund = nlockfund.sub(nbeforemny).add(HgPubTool.getNBeforeMnyWhenAct(nbeforemny, nmny),HgPubConst.MNY_DIGIT);
		}else if(oldnmny.doubleValue()>0){//ʵ���޸�
			if(nsubmny.doubleValue()>0){//ʵ������
//				У���Ƿ����ʵ�۳��� 
				if(nallmny.doubleValue()<nsubmny.doubleValue()){
					throw new BusinessException("�ʽ���޶��.�����"+String.valueOf(nsubmny.sub(nallmny,HgPubConst.MNY_DIGIT).doubleValue()));
				}	
//				�����µ�ʵ��
				nactfund = nactfund.add(nsubmny,HgPubConst.MNY_DIGIT);
				
//				�� Ԥ��   ���ʽ� ����  ������
//				1�������Ƿ���Ԥ�ۿ���
					if(nbeforemny.doubleValue()>nsubmny.doubleValue()){
						nlockfund = nlockfund.sub(nsubmny,HgPubConst.MNY_DIGIT);
					}else{
						nlockfund = nlockfund.sub(nbeforemny,HgPubConst.MNY_DIGIT);
					}
			}else if(nsubmny.doubleValue()<0){//ʵ�ۼ�С
//				���ٵ�ʵ��Ҫ ��������� �ظ� Ԥ��  Ԥ��  ���ʽ�				
//				�����µ�ʵ��
				nactfund = nactfund.add(nsubmny,HgPubConst.MNY_DIGIT);
				boolean isYK = nbeforemny.doubleValue()>=0;
				if(isYK){
					nlockfund = nlockfund.add(nsubmny.abs(),HgPubConst.MNY_DIGIT);
//					getDao().updateVO(vo, HgBsPubTool.CHECK_FUND_USENAMES);		
				}else{// ����Ԥ��
					//�Ȼظ���Ԥ��
					if(nsubmny.abs().doubleValue()>=nbeforemny.abs().doubleValue()){
						//ȫ�����ظ�Ԥ��
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
		
		//����У��
		checkFundAfterUse(vo.getPrimaryKey(), ifundtype, null, uDate, loginCorp);
	}
	
	



	/**
	 * ��ȡ��������������ϵ�λ֮��Ĺ�ϵ
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-4-13����05:26:12
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
	 *��������Ԥ��
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-4-13����05:26:12
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
			throw new BusinessException("���̵��������ϵ�λ֮��Ĺ�ϵ������,��鿴�Ƿ�ά��");
		vo = getFundSet(null, null, vdef1, uDate, ifundtype, false, loginCorp);

		if (vo == null) {
			return;
		}

		UFDouble nfund = PuPubVO.getUFDouble_NullAsZero(vo.getNfund());// ���ʽ�
		UFDouble oldnmny = PuPubVO.getUFDouble_NullAsZero(beforeNmny);// �޸�ǰ���
		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// Ԥ��
		nlockfund = nlockfund.sub(oldnmny);
		UFDouble nactfund = PuPubVO.getUFDouble_NullAsZero(vo.getNactfund());// ʵ��
		
		UFDouble nsubfund = nfund.sub(nlockfund).sub(nactfund)
				.sub(nmny);

		if (nsubfund.doubleValue() < 0) {
			throw new BusinessException("��" + HgPubTool.getSFundType(ifundtype)
					+ "����");
		}

		vo.setNlockfund(nlockfund.add(nmny,HgPubConst.MNY_DIGIT));
		getDao().updateVO(vo, HgBsPubTool.CHECK_LOCkFUND_NAME);		

		//����У��
		checkFundAfterUse(vo.getPrimaryKey(), ifundtype, null, uDate, loginCorp);
	}
	
	/**
	 *��������Ԥ��(ɾ��)
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-4-13����05:26:12
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
			throw new BusinessException("���̵��������ϵ�λ֮��Ĺ�ϵ������,��鿴�Ƿ�ά��");
		vo = getFundSet(null, null, vdef1, uDate, ifundtype, false, loginCorp);
		
		if (vo == null) {
			return;
		}

		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// Ԥ��

		if (nlockfund.doubleValue() < nmny.doubleValue())
			throw new BusinessException("���ݴ��󣬳�Ԥ�۽��");

		nlockfund = nlockfund.sub(nmny);
		vo.setNlockfund(nlockfund);
		getDao().updateVO(vo, HgBsPubTool.CHECK_FUND_USENAMES);
	}
}

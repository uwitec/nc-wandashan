package nc.bs.hg.pu.check;

import java.util.List;
import nc.bd.accperiod.AccountCalendar;
import nc.bs.dao.BaseDAO;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.vo.hg.pu.check.fund.FUNDSETVO;
import nc.vo.hg.pu.check.fund.FundSetBVO;
import nc.vo.hg.pu.plan.deal.PlanDealVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

/**
 * 
 * @author zhf
 * �ƻ����ʽ�Ԥ��  
 * ���ʽ�Ԥ���������������ʽ������½���Ԥ�� ���Ԥ�����������ʽ��ܶ� ��Ϊ����Ԥ�� ��Ԥ��ʧ��
 * ���ʽ�Ԥ�������ʽ�ʱ����Ϊ����Ԥ�� Ϊ�����·ݿ���Ԥ��
 *
 */
public class FundPrepareBO {
	
	private nc.bs.dao.BaseDAO dao = null;

	private nc.bs.dao.BaseDAO getDao() {
		if (dao == null)
			dao = new nc.bs.dao.BaseDAO();
		return dao;
	}
	
	public FundPrepareBO(BaseDAO dao){
		this.dao = dao;
	}
	
	/**
	 * @author zhw
	 * @˵�������׸ڿ�ҵ) Ϊר��ƻ�Ԥ���ʽ� ���� 2011-03-11����05:03:31
	 */
	public void LockFundForPlan(List planinfor, List lseldata,String sLogcorp)
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
				if (HgPubConst.GYC_CORPID.equals(sLogcorp)) {
					LockFund(dealTmp.getPk_plan(),
							HgPubConst.FUND_CHECK_SPECIALFUND, dealTmp1
									.getPk_corp(), dealTmp1.getCapplydeptid(),null,
							date, dealTmp1.getNmny(), dealTmp1.getPk_plan_b(),
							true,sLogcorp);
				} else {
					LockFund(dealTmp.getPk_plan(),
							HgPubConst.FUND_CHECK_SPECIALFUND, dealTmp1
									.getPk_corp(), dealTmp1.getCapplydeptid(),null,
							date, dealTmp1.getNmny(), dealTmp1.getPk_plan_b(),
							false,sLogcorp);
				}
			}
		}

	}
	
	/**
	 * ���ʽ�Ԥ��
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
	public void LockFundNoExitMny(UFDouble nmny, String scorpid,
			String sdeptid,String custid,String logcorp,int ifundtype, UFDate uDate, String pk_plan, String pk_planother_b)
	throws BusinessException {
		AccountCalendar calendar = AccountCalendar.getInstance();
		calendar.setDate(uDate);
		String iyear = calendar.getYearVO().getPeriodyear();
		String imonth = calendar.getMonthVO().getMonth();

		FundSetBVO bvo = new FundSetBVO();
		bvo.setImonth(imonth);
		bvo.setCdeptid(sdeptid);
		bvo.setIyear(iyear);
		bvo.setPk_corp(scorpid);
		bvo.setPk_plan(pk_plan);
		bvo.setPk_planother_b(pk_planother_b);
		bvo.setNlockmny(nmny);
		bvo.setVdef1(custid);//�ͻ�
		bvo.setVdef2(logcorp);//��½��˾
		bvo.setVdef3(String.valueOf(ifundtype));//�ʽ�����
		bvo.setStatus(VOStatus.NEW);
		getDao().insertVO(bvo);
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
	public void LockFundExitFund(FUNDSETVO vo, UFDouble nmny,UFDate uDate,String pk_plan,String pk_plan_b) throws BusinessException {
		if (vo == null
				|| PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
				.doubleValue())
			return;


		//		�����Ԥ������ ��ҪУ�����ʽ��Ƿ�����Ԥ�����
		AccountCalendar calendar = AccountCalendar.getInstance();
		AccountCalendar calendar1 = AccountCalendar.getInstance();
		calendar.setDate(uDate);
		// ��������
//		String iyear = calendar.getYearVO().getPeriodyear();
		String imonth = calendar.getMonthVO().getMonth();
		String imonth1 = calendar1.getMonthVO().getMonth();

		UFDouble npreparemny = UFDouble.ZERO_DBL;
		if(imonth == imonth1){
			//��ȡ��ǰ����Ԥ��
			npreparemny = getFundPrepareMny(null,vo.getPk_corp(), vo.getCdeptid(), vo.getVdef1(), uDate, vo.getIfundtype(), vo.getVdef2());

			//			�����ʽ� = ���ʽ�-��Ԥ��-Ԥ��-ʵ��
			UFDouble nmaymny = PuPubVO.getUFDouble_NullAsZero(vo.getNfund()).sub(PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund())).sub(PuPubVO.getUFDouble_NullAsZero(vo.getNactfund()))
			.sub(npreparemny);
			if(nmaymny.doubleValue()<nmny.doubleValue()){
				throw new BusinessException("��ר���ʽ��ܽ������,�޷�Ԥ��");
			}
		}
		
//		����Ԥ��
		LockFundNoExitMny(nmny, vo.getPk_corp(), vo.getCdeptid(), vo.getVdef1(), vo.getVdef2(), vo.getIfundtype(), uDate, pk_plan, pk_plan_b);
	}
	
	
//	/**
//	 * ��ȡԤ���ʽ�
//	 * 
//	 * @author zhw
//	 * @˵�������׸ڿ�ҵ�� 2011-2-11����02:43:10
//	 * @param scorpid
//	 * @param sdeptid
//	 * @param iyear
//	 * @param imonth
//	 * @return
//	 * @throws BusinessException
//	 */
//	public FundSetBVO getFundSetBVO(String scorpid, String sdeptid,
//			String iyear, String imonth, String pk_planother_b)
//			throws BusinessException {
//		String corp = PuPubVO.getString_TrimZeroLenAsNull(scorpid);
//		String deptid = PuPubVO.getString_TrimZeroLenAsNull(sdeptid);
//		if (corp == null && deptid == null)
//			return null;
//		StringBuffer strb = new StringBuffer();
//		if (corp != null) {
//			strb.append("  pk_corp = '" + corp + "' and ");
//		}
//		if (deptid != null) {
//			strb.append(" cdeptid = '" + deptid + "' and ");
//		} else
//			strb.append(" (cdeptid = '' or cdeptid is null ) and ");
//
//		strb.append(" isnull(dr,0)=0 ");
//		strb.append(" and iyear = '" + String.valueOf(iyear)
//				+ "' and imonth = '" + String.valueOf(imonth) + "'");
//		strb.append(" and pk_planother_b = '" + pk_planother_b + "'");
//		java.util.Collection c = getDao().retrieveByClause(FundSetBVO.class,
//				strb.toString());
//		if ((c == null || c.size() == 0))
//			return null;
//		if (c.size() > 1) {
//			throw new BusinessException("ר���ʽ������쳣");
//		}
//		java.util.Iterator it = c.iterator();
//		FundSetBVO vo = (FundSetBVO) it.next();
//		return vo;
//	}

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
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��Ϊ�ƻ�Ԥ���ʽ��޶��ר���ʽ� ���ʽ𲻱� Ԥ�� Ԥ�� ʵ�� ������̬�仯 2011-2-2����10:51:59
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nmny
	 * @throws BusinessException
	 */
	public void LockFund(String pk_plan, int ifundtype, String scorpid,
			String sdeptid,String custid, UFDate uDate, UFDouble nmny, String pk_planother_b,
			boolean flag, String loginCorp) throws BusinessException {
		if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
				.doubleValue())
			return;
		FUNDSETVO vo = null;
		if (flag) {
			vo = getFundSet(scorpid, null, null, uDate, ifundtype, true,
					loginCorp);
		} else {
			vo = getFundSet(null, sdeptid, null, uDate, ifundtype, true,
					loginCorp);
		}

		if (vo == null) {// ����������ʽ����
			LockFundNoExitMny( nmny, scorpid, sdeptid,custid,loginCorp,ifundtype, uDate, pk_plan, pk_planother_b);
		} else {
			LockFundExitFund(vo, nmny, uDate,pk_plan,pk_planother_b);
		}

	}
	
	public FUNDSETVO getFundSetVoByID(String fundid) throws BusinessException{
		return (FUNDSETVO)getDao().retrieveByPK(FUNDSETVO.class, fundid);
	}
	
	/**
	 * �Ƿ�Ԥ��
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-3-21����02:32:21
	 * @param scorpid
	 * @param sdeptid
	 * @param date
	 * @return
	 * @throws BusinessException
	 */
	public boolean isYL(String pk_plan_b,
			UFDate date) throws BusinessException {
//		String corp = PuPubVO.getString_TrimZeroLenAsNull(scorpid);
//		String deptid = PuPubVO.getString_TrimZeroLenAsNull(sdeptid);
//
//		if (corp == null && deptid == null)
//			return false;
		StringBuffer strb = new StringBuffer();
		strb
				.append("select count(0) from hg_fundset_b where");
//		if (corp != null) {
//			strb.append("  pk_corp = '" + corp + "' and ");
//		}
//		if (deptid != null) {
			strb.append(" pk_planother_b = '" + pk_plan_b + "' and ");
//		}

		// �õ���׼�ڼ䷽��������ʵ��
		AccountCalendar calendar = AccountCalendar.getInstance();
		calendar.setDate(date);
		// ��������
		String iyear = calendar.getYearVO().getPeriodyear();
		String imonth = calendar.getMonthVO().getMonth();

		strb.append(" iyear = '" + String.valueOf(iyear) + "' and imonth = '"
				+ String.valueOf(imonth) + "'");

		strb.append(" and isnull(dr,0)=0");
		int i = PuPubVO.getInteger_NullAs(getDao().executeQuery(strb.toString(),
				HgBsPubTool.COLUMNPROCESSOR),0);
		return i>0;
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�������ƻ�Ԥ����� �ۼ�Ԥ���ֶ�
	 * 2011-4-15����06:04:03
	 * @param pk_plan
	 * @param pk_fundset
	 * @param nlocknum ��ǰ����Ԥ��
	 * @param nsubnum ����Ԥ��
	 * @throws BusinessException
	 */
	public void updateFundSetBNumOnUseFund(String pk_plan_b, 
			UFDouble nlocknum,UFDouble nsubnum,boolean isadjust)
			throws BusinessException {
		UFDouble naddnum = nlocknum.sub(nsubnum);//����Ԥ��-����Ԥ��
		if (naddnum.doubleValue() > 0)//����
			naddnum = nsubnum;
		else //������
			naddnum = nlocknum;
		
		
		StringBuffer str = new StringBuffer();
		str.append("update hg_fundset_b set nmny = coalesce(nmny,0.0)) + "
				+ PuPubVO.getUFDouble_NullAsZero(naddnum));
		str.append("where pk_planother_b = '" + pk_plan_b + "' and isnull(dr,0)=0");
		getDao().executeUpdate(str.toString());
		
		if(!isadjust)
			return;
		String sql = "update hg_fundset_b set nmny = 0.0 where nmny < 0.0 and isnull(dr,0)=0 and pk_planother_b = '" + pk_plan_b + "'  and isnull(dr,0)=0";
		
		getDao().executeUpdate(sql);
	}

	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ȡ�ƻ�����Ԥ��
	 * 2011-4-16����04:27:07
	 * @param pk_plan_b
	 * @param date
	 * @return
	 * @throws BusinessException
	 */
	public UFDouble getPlanLockEnableMny(String pk_plan_b,
			UFDate date) throws BusinessException {
//		String corp = PuPubVO.getString_TrimZeroLenAsNull(scorpid);
//		String deptid = PuPubVO.getString_TrimZeroLenAsNull(sdeptid);

//		if (corp == null && deptid == null)
//			return null;
		StringBuffer strb = new StringBuffer();
		strb
				.append("select sum(nlockmny-(coalesce(nmny,0.0))) from hg_fundset_b where");
//		if (corp != null) {
//			strb.append("  pk_corp = '" + corp + "' and ");
//		}
//		if (deptid != null) {
			strb.append(" pk_planother_b = '" + pk_plan_b + "' and ");
//		}

		// �õ���׼�ڼ䷽��������ʵ��
		AccountCalendar calendar = AccountCalendar.getInstance();
		calendar.setDate(date);
		// ��������
		String iyear = calendar.getYearVO().getPeriodyear();
		String imonth = calendar.getMonthVO().getMonth();

		strb.append(" iyear = '" + String.valueOf(iyear) + "' and imonth = '"
				+ String.valueOf(imonth) + "'");

		strb.append(" and isnull(dr,0)=0");
		Object o = getDao().executeQuery(strb.toString(),
				HgBsPubTool.COLUMNPROCESSOR);
		return o == null ? null : PuPubVO.getUFDouble_NullAsZero(o);
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��
	 * 2011-4-16����03:43:19
	 * @param scorpid
	 * @param sdeptid
	 * @param ccustmanid
	 * @param uDate
	 * @param ifundtype
	 * @param flag
	 * @param loginCorp
	 * @return
	 * @throws BusinessException
	 */
	public UFDouble getFundPrepareMny(String pk_fundset,String scorpid, String sdeptid,
			String ccustmanid, UFDate uDate, int ifundtype,
			String loginCorp) throws BusinessException {
		
		if(PuPubVO.getString_TrimZeroLenAsNull(pk_fundset)!=null){
			String sql = "select sum(nlockmny) from hg_fundset_b where pk_fundset = '"+pk_fundset+"' and isnull(dr,0)=0";
			return PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR));
		}

		String corp = PuPubVO.getString_TrimZeroLenAsNull(scorpid);
		String deptid = PuPubVO.getString_TrimZeroLenAsNull(sdeptid);
		String scustid = PuPubVO.getString_TrimZeroLenAsNull(ccustmanid);

		if (corp == null && deptid == null && scustid == null)
			return null;
		StringBuffer strb = new StringBuffer();
		strb.append("select sum(nlockmny) from hg_fundset_b where ");
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
//		AccountCalendar calendar1 = AccountCalendar.getInstance();
		calendar.setDate(uDate);
		// ��������
		String iyear = calendar.getYearVO().getPeriodyear();
		String imonth = calendar.getMonthVO().getMonth();
//		String imonth1 = calendar1.getMonthVO().getMonth();

		// zhw end
		strb.append(" and iyear = '" + String.valueOf(iyear)
				+ "' and imonth = '" + String.valueOf(imonth) + "'");
//		strb.append(" and coalesce(fcontrol,'N')='Y' and ifundtype = "
//				+ ifundtype);
		strb.append(" and vdef2 = '" + loginCorp + "'");
		strb.append(" and vdef3 = '"+String.valueOf(ifundtype)+"'");
		
		return PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(strb.toString(), HgBsPubTool.COLUMNPROCESSOR));
//		return vo;
	
	}
	

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
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��ר���ʽ�ƻ�Ԥ���ǵ���Ԥ��
	 * 2011-4-16����05:57:31
	 * @param pk_plan_b
	 * @param nsubmny
	 * @throws BusinessException
	 */
	public void adjustPrepareMnyOnBefore(String pk_plan_b,UFDouble nsubmny) throws BusinessException{
		String sql = "update hg_fundset_b set nmny = (coalesce(nmny,0.0) - "
				+ PuPubVO.getUFDouble_NullAsZero(nsubmny)+
		" )where pk_planother_b = '" + pk_plan_b + "' and isnull(dr,0)=0";
		getDao().executeUpdate(sql);
		
		List l = (List)getDao().retrieveByClause(FundSetBVO.class, " pk_planother_b = '" + pk_plan_b + "' and isnull(dr,0)=0");
		if(l==null||l.size()==0)
			return;
		FundSetBVO fund = (FundSetBVO)l.get(0);
		if(PuPubVO.getUFDouble_NullAsZero(fund.getNmny()).doubleValue()<0)
			fund.setNmny(UFDouble.ZERO_DBL);
		else if(PuPubVO.getUFDouble_NullAsZero(fund.getNmny()).doubleValue()>PuPubVO.getUFDouble_NullAsZero(fund.getNlockmny()).doubleValue())
			fund.setNmny(fund.getNlockmny());
		getDao().updateVO(fund, new String[]{"nmny"});
		
	}

}

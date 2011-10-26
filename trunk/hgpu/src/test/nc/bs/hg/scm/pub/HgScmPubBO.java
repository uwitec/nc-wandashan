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
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.itf.hg.pu.pub.IFundCheck;
import nc.itf.pu.pub.fw.LockTool;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
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
import nc.vo.pub.VOStatus;
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
	 * ���ߣ�zhf ���ܣ������� ������AggregatedValueObject ���أ�UFBoolean ���⣺ �������ڣ�(2002-4-15
	 * 10:27:42) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2004-02-19 WYF �޸�bean.remove()�Ĵ���
	 */
	public UFBoolean lockPkForKey(String sCuser, String[] saPk)
			throws Exception {

		boolean isLockSucc = false;
		// nc.bs.pub.lock.LockBOAccess boLock = new
		// nc.bs.pub.lock.LockBOAccess();
		try {

			// ����Ա
			// String sCuser = null;
			// sCuser = (String) vo.getParentVO().getAttributeValue("cuserid");
			SCMEnv.out("����Ϊ����Ա[ID:" + sCuser + "]����NCҵ����...");
			if (sCuser == null || sCuser.trim().equals(""))
				throw new nc.vo.pub.BusinessException(
						nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
								"4004pub", "UPP4004pub-000119")/*
																 * @res
																 * "��ȡ����Ա�������ܽ�����ز�����"
																 */);
			// String[] saPk = getPksForVo(vo);
			if (saPk != null) {
				// isLockSucc = boLock.lockPKArray(saPk, sCuser, "");
				isLockSucc = LockTool.setLockForPks(saPk, sCuser);
				if (isLockSucc) {
					SCMEnv.out("Ϊ����Ա[ID:" + sCuser + "]����NCҵ�����ɹ���");
				} else {
					throw new nc.vo.pub.BusinessException(
							nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID("4004pub", "UPP4004pub-000129")/*
																				 * @res
																				 * "���ڲ������������Ժ�����"
																				 */);
				}
			}
			// ����Ҫ���������
			else {
				SCMEnv.out("Ϊ����Ա[ID:" + sCuser + "]����NCҵ����ʧ�ܡ�ʧ��ԭ�򣺲���Ҫ���������!");
			}
		} catch (Exception e) {
			SCMEnv.out(e);
			throw e;
		}
		return new UFBoolean(isLockSucc);
	}

	/**
	 * ���ߣ�zhf ���ܣ��ͷŵ����� ������AggregatedValueObject ���أ�void ���⣺ �������ڣ�(2002-4-15
	 * 10:27:42) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2004-02-19 WYF �޸�bean.remove()�Ĵ���
	 */
	public void freePkForKey(String sCuser, String[] saPk) throws Exception {
		// ����Ա
		// String sCuser = (String)
		// vo.getParentVO().getAttributeValue("cuserid");
		SCMEnv.out("����Ϊ����Ա[ID:" + sCuser + "]�ͷ�NCҵ����...");
		if (sCuser == null || sCuser.trim().equals(""))
			throw new nc.vo.pub.BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("4004pub", "UPP4004pub-000119")/*
																				 * @res
																				 * "��ȡ����Ա�������ܽ�����ز�����"
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
			SCMEnv.out("Ϊ����Ա[ID:" + sCuser + "]�ͷ�NCҵ�����ɹ�������");
		} catch (Exception e) {
			SCMEnv.out("Ϊ����Ա[ID:" + sCuser + "]�ͷ�NCҵ�����쳣������");
			// reportException(e);
			throw e;
		}
	}

	/**
	 * ���ߣ�zhf ���ܣ������� ������AggregatedValueObject ���أ�UFBoolean ���⣺ �������ڣ�(2002-4-15
	 * 10:27:42) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2004-02-19 WYF �޸�bean.remove()�Ĵ���
	 */
	public UFBoolean lockPkForVo(nc.vo.pub.AggregatedValueObject vo)
			throws Exception {

		boolean isLockSucc = false;
		// nc.bs.pub.lock.LockBOAccess boLock = new
		// nc.bs.pub.lock.LockBOAccess();
		try {

			// ����Ա
			String sCuser = null;
			sCuser = (String) vo.getParentVO().getAttributeValue("cuserid");
			SCMEnv.out("����Ϊ����Ա[ID:" + sCuser + "]����NCҵ����...");
			if (sCuser == null || sCuser.trim().equals(""))
				throw new nc.vo.pub.BusinessException(
						nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
								"4004pub", "UPP4004pub-000119")/*
																 * @res
																 * "��ȡ����Ա�������ܽ�����ز�����"
																 */);
			String[] saPk = getPksForVo(vo);
			if (saPk != null) {
				// isLockSucc = boLock.lockPKArray(saPk, sCuser, "");
				isLockSucc = LockTool.setLockForPks(saPk, sCuser);
				if (isLockSucc) {
					SCMEnv.out("Ϊ����Ա[ID:" + sCuser + "]����NCҵ�����ɹ���");
				} else {
					throw new nc.vo.pub.BusinessException(
							nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID("4004pub", "UPP4004pub-000129")/*
																				 * @res
																				 * "���ڲ������������Ժ�����"
																				 */);
				}
			}
			// ����Ҫ���������
			else {
				SCMEnv.out("Ϊ����Ա[ID:" + sCuser + "]����NCҵ����ʧ�ܡ�ʧ��ԭ�򣺲���Ҫ���������!");
			}
		} catch (Exception e) {
			SCMEnv.out(e);
			throw e;
		}
		return new UFBoolean(isLockSucc);
	}

	/**
	 * ���ߣ�zhf ���ܣ��ͷŵ����� ������AggregatedValueObject ���أ�void ���⣺ �������ڣ�(2002-4-15
	 * 10:27:42) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2004-02-19 WYF �޸�bean.remove()�Ĵ���
	 */
	public void freePkForVo(nc.vo.pub.AggregatedValueObject vo)
			throws Exception {
		// ����Ա
		String sCuser = (String) vo.getParentVO().getAttributeValue("cuserid");
		SCMEnv.out("����Ϊ����Ա[ID:" + sCuser + "]�ͷ�NCҵ����...");
		if (sCuser == null || sCuser.trim().equals(""))
			throw new nc.vo.pub.BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("4004pub", "UPP4004pub-000119")/*
																				 * @res
																				 * "��ȡ����Ա�������ܽ�����ز�����"
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
			SCMEnv.out("Ϊ����Ա[ID:" + sCuser + "]�ͷ�NCҵ�����ɹ�������");
		} catch (Exception e) {
			SCMEnv.out("Ϊ����Ա[ID:" + sCuser + "]�ͷ�NCҵ�����쳣������");
			// reportException(e);
			throw e;
		}
	}

	/**
	 * ���ߣ�zhf ���ܣ������㷨,�����ݺ��ͷŵ�����ʱ����ȡ������Ҫ������ID(�������޸ĵ��ŵ���) ������AggregatedValueObject
	 * ���أ�String[] ���⣺ �������ڣ�(2002-4-15 10:27:42) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return String[]
	 * @param vo
	 *            nc.vo.pub.AggregatedValueObject
	 */
	private String[] getPksForVo(nc.vo.pub.AggregatedValueObject vo)
			throws BusinessException, BusinessException, SQLException {
		String[] saRslt = null;
		// �Ϸ��Լ��
		if (vo == null)
			return null;
		if (vo.getParentVO() == null
				&& (vo.getChildrenVO() == null || vo.getChildrenVO().length <= 0))
			return null;
		// Vector vTmp = new Vector();
		if (vo.getParentVO() == null) {
			SCMEnv.out("���ݴ����޵��ݱ�ͷ����,����ʧ��");
			return null;
		}
		// ����ID����
		Vector vPk = new Vector();
		// ����������־
		boolean bIsNew = (vo.getParentVO().getPrimaryKey() == null || vo
				.getParentVO().getPrimaryKey().trim().equals(""));
		String sTmp = null;
		// �������������
		if (bIsNew) {
			// �Ƿ��в��յ��ϲ�����
			boolean bIsRef = false;
			if (vo.getChildrenVO() == null || vo.getChildrenVO().length <= 0) {
				throw new nc.vo.pub.BusinessException(
						nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
								"4004pub", "UPP4004pub-000122")/*
																 * @res
																 * "���ݴ��������ĵ��ݱ���Ϊ��,����ʧ��"
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
			// ������е����о�Ϊ����
			if (!bIsRef) {
				SCMEnv.out("������ȫ�����ƣ����ؼ���");
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
					SCMEnv.out("�������ݴ��󣺲��յĵ����ϲ���ID�Ҳ���ƥ����ϲ�ͷID,����ʧ��");
					return null;
				}
				if (!vPk.contains(sTmp)) {
					vPk.addElement(sTmp);
				}
			}
		}
		// ������޸ĵ���
		else {
			sTmp = vo.getParentVO().getPrimaryKey();
			if (sTmp == null || sTmp.trim().equals("")) {
				SCMEnv.out("���ݴ����Ҳ�����ͷID,����ʧ��");
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
		// �������������
		return saRslt;

	}

	/**
	 * ���ߣ�zhf ���ܣ������㷨,�����ݺ��ͷŵ�����ʱ����ȡ������Ҫ������ID(�������ŵ���) ������AggregatedValueObject
	 * ���أ�String[] ���⣺ �������ڣ�(2002-4-15 10:27:42) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-05-17
	 * ��ӡ�� ���ݱ�����ܽ����ϲ���Դ���ݣ��޸�Ϊ��󻯼���
	 */
	private String[] getPksForVoFree(nc.vo.pub.AggregatedValueObject vo)
			throws BusinessException, BusinessException, SQLException {
		String[] saRslt = null;

		// �Ϸ��Լ��
		if (vo == null)
			return null;

		// Vector vTmp = new Vector();
		if (vo.getParentVO() == null) {
			SCMEnv.out("���ݴ����޵��ݱ�ͷ����,����ʧ��");
			return null;
		}

		// ����ID����
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
																		 * "�������ݴ��󣺲��յĵ����ϲ���ID�Ҳ���ƥ����ϲ�ͷID,����ʧ��"
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
	 * @˵���� У��ʱ����Ƿ�ͬ�� ��ͬ������false
	 * @���ڣ� 2009-11-12
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
			throw new BusinessException("�����쳣");
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
				throw new BusinessException("����������������ˢ�½�������");
			}
		}

	}

	/**
	 * @author liuys
	 * @˵�������׸ڿ�ҵ����������������ƻ��ݲ�У��
	 * 
	 * �ƻ������� �붩���ϵ����� ���һ�����ݲ� 2011-1-10 ����01:45:20
	 * @param gbillvo
	 * @throws BusinessException
	 */
	public void checkAllowanceForTOOrder(BillVO inCurVOs)
			throws BusinessException {
		// ��ȡ �ݲ����� �ݹ�� У���ݲ�
		if (inCurVOs == null)
			return;
		BillItemVO[] items = (BillItemVO[]) inCurVOs.getChildrenVO();
		if (items == null || items.length == 0)
			return;

		// �������ܳ��ֶ�����ͬ������, �ʰ���� ������ �����ϲ�
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
			// �õ��ƻ��ݲ����
			tmpNmny = PuPubVO.getUFDouble_NullAsZero(getAllowanceSet(item
					.getCinvbasid(), ALLOWANCEVO.NPLAN, null));
			tmpNmny = tmpNmny.div(new UFDouble(100), 8);
			// �õ���Ӧ(��ʱ�ƻ�,ר���ʽ�ƻ�,�����üƻ�)�ƻ�������
			UFDouble plannum = PuPubVO
					.getUFDouble_NullAsZero(getAllPlanwanceSet(item
							.getCfirstbid()));
			nnum = PuPubVO.getUFDouble_NullAsZero(item.getNnum());
			// add by zhw 2011-01-12 21:15
			UFDouble oldnum = PuPubVO.getUFDouble_NullAsZero(getOldNumByPk(item
					.getCbill_bid()));

			// �ݲ����δ���û�=0 �������ݲ����
			if (tmpNmny.equals(UFDouble.ZERO_DBL)) {
				if (!(plannum.add(oldnum)).equals(nnum)) {
					// add by zhw ����޸�ǰ�������ڵ�ǰ�� ����Ҫ����
					if (nnum.doubleValue() > (plannum.add(oldnum))
							.doubleValue())
						throw new BusinessException("���" + item.getCinvcode()
								+ ",�������������ƻ��ݲ����");
				}
			} else {
				if (nnum.doubleValue() < (plannum.add(oldnum)).doubleValue())// �����������ݲ����
					return;
				plannum = (plannum.add(oldnum));
				UFDouble nshouldnum = plannum.multiply(tmpNmny, 8);
				if (nnum.doubleValue() > nshouldnum.doubleValue()) {
					throw new BusinessException("���" + item.getCinvcode()
							+ ",�������������ƻ��ݲ����");
				}
			}
		}
	}

	/**
	 * ��ѯ�ƻ�δ���õ����� zhf modify nnum-->nreserve10 20110211 �¼ƻ����ƽ������
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
		// ���������� = �ƻ����� - �ۼ���������
		UFDouble nnum1 = PuPubVO.getUFDouble_NullAsZero(os[0]);
		UFDouble nnum = nnum1.equals(UFDouble.ZERO_DBL) ? PuPubVO
				.getUFDouble_NullAsZero(os[1]) : nnum1;
		UFDouble nallnum = PuPubVO.getUFDouble_NullAsZero(os[2]);
		tmpNnum = nnum.sub(nallnum);
		return tmpNnum;
	}

	/**
	 * ��ѯ���޸�ǰ������
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-1-12����09:08:01
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
	 * @˵�������׸ڿ�ҵ������ݲ�У�� 2011-4-8����01:45:20
	 * @param gbillvo
	 * @throws BusinessException
	 */
	public void checkAllowanceForICin(GeneralBillVO gbillvo)
			throws BusinessException {
		//  У��ʵ�ʵ����������ܴ��ں�ͬ����
		GeneralBillItemVO[] items = (GeneralBillItemVO[])gbillvo.getChildrenVO();
		for (GeneralBillItemVO item :items ) {
			UFDouble ninnum = PuPubVO.getUFDouble_NullAsZero(item.getAttributeValue(HgPubConst.NUM_DEF_FAC));
			UFDouble nshouldnum = PuPubVO.getUFDouble_NullAsZero(item.getNshouldinnum());
			if(nshouldnum.abs().compareTo(ninnum.abs())<0)
				throw new BusinessException("���" + item.getCinventorycode()+ ",���ܳ�����ͬ����");
		}
//		// ��ȡ �ݲ����� �ݹ�� У���ݲ�
//		if (gbillvo == null)
//			return;
//		GeneralBillItemVO[] items = gbillvo.getItemVOs();
//		if (items == null || items.length == 0)
//			return;
//
//		// ����� ������ �����ϲ�
//		Map<String, GeneralBillItemVO> infor = new HashMap<String, GeneralBillItemVO>();
//		GeneralBillItemVO tmpitem = null;
//		for (GeneralBillItemVO item : items) {
//			if (infor.containsKey(item.getCinvbasid())) {
//				tmpitem = infor.get(item.getCinvbasid());
//				tmpitem
//						.setNshouldinnum(PuPubVO.getUFDouble_NullAsZero(
//								tmpitem.getNshouldinnum()).add(
//								PuPubVO.getUFDouble_NullAsZero(item
//										.getNshouldinnum())));
//				tmpitem.setNinnum(PuPubVO.getUFDouble_NullAsZero(
//						tmpitem.getNinnum()).add(
//						PuPubVO.getUFDouble_NullAsZero(item.getNinnum())));
//			} else {
//				tmpitem = (GeneralBillItemVO) item.clone();
//			}
//			infor.put(item.getCinvbasid(), tmpitem);
//		}
//
//		if (infor.size() == 0)
//			return;
//
//		UFDouble tmpNmny = null;
//		UFDouble nshouldnum = null;
//		UFDouble ninnum = null;
//		for (GeneralBillItemVO item : infor.values()) {
//			tmpNmny = PuPubVO.getUFDouble_NullAsZero(getAllowanceSet(item
//					.getCinvbasid(), ALLOWANCEVO.NARRIVAL, null));
//			tmpNmny = tmpNmny.div(new UFDouble(100), 8);
//			nshouldnum = PuPubVO.getUFDouble_NullAsZero(item.getNshouldinnum());
//			if(nshouldnum.compareTo(UFDouble.ZERO_DBL)<0)//�������С���� ��Ϊ�˿�
//				return;
//			ninnum = PuPubVO.getUFDouble_NullAsZero(item
//					.getAttributeValue(HgPubConst.NUM_DEF_FAC));
//			// ==0 �������ݲ����
//			if (tmpNmny.equals(UFDouble.ZERO_DBL)) {
//				if (!nshouldnum.equals(ninnum))
//					if (ninnum.doubleValue() > nshouldnum.doubleValue())
//						throw new BusinessException("���"
//								+ item.getCinventorycode() + ",ʵ�ʵ������ݲ����");
//			} else {
//				if (ninnum.doubleValue() < nshouldnum.doubleValue())// �����������ݲ����
//					return;
//				nshouldnum = nshouldnum.multiply(tmpNmny, 8);
//				if (ninnum.doubleValue() > nshouldnum.doubleValue()) {
//					throw new BusinessException("���" + item.getCinventorycode()
//							+ ",ʵ�ʵ������ݲ����");
//				}
//			}
//		}
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ���������������ݲ�У�� 2011-4-8����01:45:20
	 * @param gbillvo
	 * @throws BusinessException
	 */
	public void checkAllowanceForICout(GeneralBillVO gbillvo)
			throws BusinessException {
		// ��ȡ �ݲ����� �ݹ�� У���ݲ�
		if (gbillvo == null)
			return;
		GeneralBillItemVO[] items = gbillvo.getItemVOs();
		if (items == null || items.length == 0)
			return;

		// ����� ������ �����ϲ�
		Map<String, GeneralBillItemVO> infor = new HashMap<String, GeneralBillItemVO>();
		GeneralBillItemVO tmpitem = null;
		for (GeneralBillItemVO item : items) {
			if (infor.containsKey(item.getCinvbasid())) {
				tmpitem = infor.get(item.getCinvbasid());
				//Ӧ�ð��������� - �ۼ����������ݲ�У��  modufy by zhw  2011-04-20
				tmpitem.setNshouldoutnum(PuPubVO.getUFDouble_NullAsZero(
						tmpitem.getNshouldoutnum()).add(PuPubVO.getUFDouble_NullAsZero(getRemainNum(item.getCsourcebillbid()))));
				tmpitem.setNoutnum(PuPubVO.getUFDouble_NullAsZero(
						tmpitem.getNoutnum()).add(PuPubVO.getUFDouble_NullAsZero(item.getNoutnum())));
			} else {
				tmpitem = (GeneralBillItemVO) item.clone();
				//Ӧ�ð��������� - �ۼ����������ݲ�У��  modufy by zhw  2011-04-20
				tmpitem.setNshouldoutnum(PuPubVO.getUFDouble_NullAsZero(PuPubVO.getUFDouble_NullAsZero(getOldOutNum(item.getCgeneralbid())))
						.add(PuPubVO.getUFDouble_NullAsZero(getRemainNum(item.getCsourcebillbid()))));
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
			// ==0 �������ݲ����
			if (tmpNmny.equals(UFDouble.ZERO_DBL)) {
				if (!nshouldnum.equals(noutnum))
					if (noutnum.doubleValue() > nshouldnum.doubleValue())
						throw new BusinessException("���"
								+ item.getCinventorycode() + "ʵ�ʳ��ⳬ�ݲ����");
			} else {
				if (noutnum.doubleValue() < nshouldnum.doubleValue())// �����������ݲ����
					return;
				nshouldnum = nshouldnum.multiply(tmpNmny, 8);
				if (noutnum.doubleValue() > nshouldnum.doubleValue()) {
					throw new BusinessException("���" + item.getCinventorycode()
							+ "ʵ�ʳ��ⳬ�ݲ����");
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
	 * @˵�������׸ڿ�ҵ�� 2011-4-8����03:37:24
	 * @param l5xbids
	 * @return Map<String, String[]> planidInfor
	 * @throws BusinessException
	 */
	private Object getPlanTypeFor4Y(List<String> l5xbids)
			throws BusinessException {

		String sql = " select cbill_bid,csourcebid,cincorpid,csourcetypecode,nnum,nnotaxprice,norderoutnum,nnotaxmny from to_bill_b where cbill_bid  in "
				+ HgPubTool.getSubSql(l5xbids.toArray(new String[0]));

		Object o = getBaseDao().executeQuery(sql,
				new BeanListProcessor(BillItemVO.class));
		if (o == null)
			return null;
		List l = (List) o;
		BillItemVO item = null;
		Map<String, BillItemVO> planInfor = new HashMap<String, BillItemVO>();
//		String[] tmpS = null;
		String splantypecode = PuPubVO
				.getString_TrimZeroLenAsNull(((BillItemVO) (l.get(0))).getCsourcetypecode());
		if (splantypecode == null
				|| HgPubTool.getIFundTypeByPlanType(splantypecode) == HgPubConst.FUND_CHECK_ERRORTYPE)
			return null;
		for (int i = 0; i < l.size(); i++) {
			item = (BillItemVO) l.get(i);
			planInfor.put(item.getCbill_bid(),item);
		}
		return new Object[] { planInfor,
				HgPubTool.getIFundTypeByPlanType(splantypecode) };
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�����۳��ⵥǩ�� ʵ�� 2012-2-19����09:36:20
	 * @param gbillvo
	 * @param flag
	 * @throws BusinessException
	 */
	public void checkAndUseFundForSaleOut(GeneralBillVO gbillvo, boolean isDel,String loginCorp)
	throws BusinessException {
		if (gbillvo == null)
			return;
		GeneralBillHeaderVO head = gbillvo.getHeaderVO();
		GeneralBillItemVO[] items = gbillvo.getItemVOs();
		if (items == null
				|| items.length == 0
				|| PuPubVO.getString_TrimZeroLenAsNull(items[0]
				                                             .getCsourcetype()) == null)
			return; // ��֧���������۳��ⵥ ���� ���� �������۳��ⵥ

		// ����Դ���۶��� ��ȡ ��˰���� �� ��˰��� һ���۶�����Ӧһ�����۳��ⵥ 1:1
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

		Map<String, SaleorderBVO> soOrderInfor = getSoOrderInfor(lsoorderid);

		if (soOrderInfor.size() == 0)
			return;

		UFDate uDate = head.getDauditdate();
		if (HgPubConst.IS_DBILLDATE_WHEN_USE) {
			uDate = head.getDbilldate();
		}

		UFDouble nmny = null;//����ʵ�۽��
		UFDouble nbeforemny = null;//���ο���Ԥ�۽��
		UFDouble nprice = null;

		SaleorderBVO itemorder = null;
		for (GeneralBillItemVO item : items) {
			if(item.getStatus() == VOStatus.UNCHANGED&&!isDel)
				continue;			
			
			if(!soOrderInfor.containsKey(item.getCsourcebillbid())){
				throw new BusinessException("��ȡ��Դ������ϢΪ��");
			}
			itemorder = soOrderInfor.get(item.getCsourcebillbid());
			if(itemorder == null){
				throw new BusinessException("��ȡ��Դ������ϢΪ��");			
			}
			
			nprice = PuPubVO.getUFDouble_NullAsZero(itemorder.getNoriginalcurprice());
			nbeforemny = nprice.multiply(PuPubVO.getUFDouble_NullAsZero(itemorder.getNnumber()).
					add(PuPubVO.getUFDouble_NullAsZero(itemorder.getNtotalinventorynumber())),8);
			nmny = nprice.multiply(
					item.getNoutnum(), HgPubConst.MNY_DIGIT);
			
			if(item.getStatus() == VOStatus.DELETED||isDel){
				nmny = UFDouble.ZERO_DBL;
			}

			getFundBO().useFundForSaleOut(head.getCcustomerid(),  uDate,itemorder.getNmny(), nbeforemny, nmny, item.getPrimaryKey(), loginCorp, HgPubConst.FUND_CHECK_FUND,nprice);
			getFundBO().useFundForSaleOut(head.getCcustomerid(), uDate, itemorder.getNmny(),nbeforemny, nmny, item.getPrimaryKey(),  loginCorp, HgPubConst.FUND_CHECK_QUATO,nprice);
		}
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ������Դ���۶��� ��ȡ ��˰���� �� ��˰���
	 * @param lsoorderid
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, SaleorderBVO> getSoOrderInfor(List<String> lsoorderid)
			throws BusinessException {
		String sql = " select a.corder_bid,a.noriginalcurprice,a.noriginalcurmny,a.nnumber,b.ntotalinventorynumber from so_saleorder_b a inner join so_saleexecute b on a.corder_bid = b.csale_bid where corder_bid in "
				+ HgPubTool.getSubSql(lsoorderid.toArray(new String[0]));
		Object o = getBaseDao().executeQuery(sql,
				new BeanListProcessor(SaleorderBVO.class));
		if (o == null)
			return null;
		List ldata = (List) o;
		if (ldata.size() == 0)
			return null;
//		int len = ldata.size();
		Iterator it = ldata.iterator();
		Map<String, SaleorderBVO> retMap = new HashMap<String, SaleorderBVO>();
		SaleorderBVO item = null;
		String key = null;
//		Object[] os = null;
		while (it.hasNext()) {
			item = (SaleorderBVO) it.next();
			key = item.getCorder_bid();
			retMap.put(key, item);
		}
		return retMap;
	}

	public void checkAndUseFundForMaterialOut(GeneralBillVO gbillvo,String loginCorp,
			boolean isDel) throws BusinessException {
		if (gbillvo == null)
			return;
		GeneralBillHeaderVO head = gbillvo.getHeaderVO();
		GeneralBillItemVO[] items = gbillvo.getItemVOs();
		if (items == null || items.length == 0)
			return;
		// modify by zhw 2011-04-20
		String cust = null;
		
		String pk_corp = null;
		String pk_dept =null;
		UFDouble nmny = UFDouble.ZERO_DBL;
		if(!HgPubConst.GYC_CORPID.equalsIgnoreCase(loginCorp)){//����Ǿſ�  ��Ҫ�����ⷽʽ����
			pk_corp = PuPubVO.getString_TrimZeroLenAsNull(head.getAttributeValue("pk_corp"));// ��˾
			pk_dept = PuPubVO.getString_TrimZeroLenAsNull(head.getAttributeValue("cdptid"));// ����
			for (GeneralBillItemVO item : items) {
				if(item.getStatus() == VOStatus.DELETED)//--------------ȥ��ɾ�е�����
					continue;
				if(PuPubVO.getUFDouble_NullAsZero(item.getNmny()).compareTo(UFDouble.ZERO_DBL)>0){
					nmny = nmny.add(PuPubVO.getUFDouble_NullAsZero(item.getNmny()));
				}else{
					nmny = nmny.add(PuPubVO.getUFDouble_NullAsZero(item
							.getNplannedmny()));
				}
				
			}
		}else{
			cust = PuPubVO.getString_TrimZeroLenAsNull(head.getAttributeValue("pk_defdoc5"));// ���ϲ���
			for (GeneralBillItemVO item : items) {
				if(item.getStatus() == VOStatus.DELETED)//--------------ȥ��ɾ�е�����
					continue;
				nmny = nmny.add(PuPubVO.getUFDouble_NullAsZero(item
						.getNplannedmny()));
			}
		}
		// modify by zhw 2011-04-20 end
		
		UFDate uDate = head.getDauditdate();
		if (HgPubConst.IS_DBILLDATE_WHEN_USE) {
			uDate = head.getDbilldate();
		}

		if(isDel)
			nmny = UFDouble.ZERO_DBL;
		
		getFundBO().useFundMater(pk_corp, HgPubConst.FUND_CHECK_FUND,
				pk_dept, cust, uDate, nmny,
				head.getPrimaryKey(), head.getCbilltypecode(),loginCorp);
		getFundBO().useFundMater(pk_corp, HgPubConst.FUND_CHECK_QUATO,
				pk_dept, cust, uDate, nmny,
				head.getPrimaryKey(), head.getCbilltypecode(),loginCorp);

	}
	
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��ʵ��/��ʵ�� ������Դ�ڼƻ����������� 2011-4-8����01:44:15
	 * @param gbillvo
	 * @param isDel  �Ƿ�ɾ��
	 * @param isYK  �Ƿ�Ԥ��
	 * @throws BusinessException
	 */
	public void checkAndUseFundForAllOut(GeneralBillVO gbillvo,
			boolean isDel,String loginCorp) throws BusinessException {

		if (gbillvo == null)
			return;
		GeneralBillHeaderVO head = gbillvo.getHeaderVO();
		GeneralBillItemVO[] items = gbillvo.getItemVOs();
		if (items == null
				|| items.length == 0
				|| PuPubVO.getString_TrimZeroLenAsNull(items[0].getCsourcetype()) == null)
			return;

		List<String> l5xbids = new ArrayList<String>();
		// modify by zhw 2011-01-08 ����������ʽ���ɵ�������
		for (GeneralBillItemVO item : items) {
			if (l5xbids.contains(item.getCsourcebillbid()))
				continue;
			l5xbids.add(item.getCsourcebillbid());
		}

		if (l5xbids.size() == 0)
			return;

		// ������Դ��������id ��ȡ ��Դ �ƻ�ͷid �� ��Դ �ƻ�����
		/**
		 * ע�� �����ͼƻ�����һһ��Ӧ 1:N ��ͬһ��������Դ�ƻ���������ͬ��
		 */
		Object o = getPlanTypeFor4Y(l5xbids);

		if (o == null)
			return;
		int ifundtype = Integer.parseInt((((Object[]) o)[1]).toString());
		if (ifundtype == HgPubConst.FUND_CHECK_ERRORTYPE) {
			//			checkAndUseFundForAllOut_NoPlan(gbillvo, isDel,loginCorp);// ����Դ�ڼƻ��� ��������
			return;
		}

		Map<String, BillItemVO> planidInfor = (Map<String, BillItemVO>) ((Object[]) o)[0];

		UFDate uDate = head.getDauditdate();
		if (HgPubConst.IS_DBILLDATE_WHEN_USE) {
			uDate = head.getDbilldate();
		}

		UFDouble nbeforemny = null;//��ǰ����Ԥ��
		UFDouble nmny = null;
		UFDouble nplanMny = null;
		UFDouble norderMny = null;		
		UFDouble nordernum = null;
		UFDouble nljordernum = null;

		BillItemVO itemorder = null;
		for(GeneralBillItemVO item:items){//����������н���ʵ��	
			if(item.getStatus() == VOStatus.UNCHANGED&&!isDel)
				continue;

			if(!planidInfor.containsKey(item.getCsourcebillbid())){
				throw new BusinessException("��ȡ��ԴԤ������Ϣ�쳣");
			}
			itemorder = planidInfor.get(item.getCsourcebillbid());

			nordernum = PuPubVO.getUFDouble_NullAsZero(itemorder.getNnum());//Ԥ��������
			nljordernum = PuPubVO.getUFDouble_NullAsZero(itemorder.getNorderoutnum());//Ԥ�����ۼ��������
			norderMny = PuPubVO.getUFDouble_NullAsZero(item.getNmny());//����������
			nplanMny = PuPubVO.getUFDouble_NullAsZero(item.getNplannedmny());//��������ƻ����
			//���û�к�ͬ�ϵļ۸�   ȡ�ƻ���  ����  ȡ��ͬ��  modify  by zhw  2011-04-19  
			if(UFDouble.ZERO_DBL.equals(norderMny)){
				nmny=nplanMny;
			}else{
				nmny=norderMny;
			}

			nbeforemny = itemorder.getNnotaxprice().multiply(nordernum.sub(nljordernum), 8);

			if(item.getStatus() == VOStatus.DELETED||isDel){
				nmny = UFDouble.ZERO_DBL;
			}

			if (ifundtype == HgPubConst.FUND_CHECK_SPECIALFUND) {
				getFundBO().useFundForAllOut(itemorder.getPrimaryKey(), ifundtype, itemorder.getCincorpid(),
						null, uDate,itemorder.getNnotaxmny(), nbeforemny, nmny,
						item.getPrimaryKey(), head.getCbilltypecode(),loginCorp);
			} else if (ifundtype == HgPubConst.FUND_CHECK_FUND_QUATO) {
				getFundBO().useFundForAllOut(itemorder.getPrimaryKey(), HgPubConst.FUND_CHECK_FUND,
						itemorder.getCincorpid(), null, uDate, itemorder.getNnotaxmny(),nbeforemny, nmny,
						item.getPrimaryKey(), head.getCbilltypecode(),loginCorp);
				getFundBO().useFundForAllOut(itemorder.getPrimaryKey(),
						HgPubConst.FUND_CHECK_QUATO, itemorder.getCincorpid(), null,
						uDate, itemorder.getNnotaxmny(),nbeforemny, nmny, item.getPrimaryKey(),
						head.getCbilltypecode(),loginCorp);
			}
		}
	}

//	/**
//	 * 
//	 * @author zhf
//	 * @˵�������׸ڿ�ҵ��ʵ��/��ʵ�� ������Դ�ڼƻ����������� 2011-4-8����01:44:15
//	 * @param gbillvo
//	 * @param flag
//	 * @param isYK
//	 * @throws BusinessException
//	 */
//	public void checkAndUseFund(GeneralBillVO gbillvo, boolean flag,
//			boolean isYK,String loginCorp) throws BusinessException {
//
//		if (gbillvo == null)
//			return;
//		GeneralBillHeaderVO head = gbillvo.getHeaderVO();
//		GeneralBillItemVO[] items = gbillvo.getItemVOs();
//		if (items == null
//				|| items.length == 0
//				|| PuPubVO.getString_TrimZeroLenAsNull(items[0]
//						.getCsourcetype()) == null)
//			return;
//		List<String> l5xbids = new ArrayList<String>();
//		// String tmp =null;
//		// modify by zhw 2011-01-08 ����������ʽ���ɵ�������
//		for (GeneralBillItemVO item : items) {
//			// tmp = PuPubVO.getString_TrimZeroLenAsNull(item.getCsourcetype());
//			// if(!(tmp.equalsIgnoreCase(HgPubConst.PLAN_MNY_BILLTYPE)||tmp.equalsIgnoreCase(HgPubConst.PLAN_TEMP_BILLTYPE)
//			// ||tmp.equalsIgnoreCase(HgPubConst.PLAN_MONTH_BILLTYPE)))
//			// continue;
//			if (l5xbids.contains(item.getCsourcebillbid()))
//				continue;
//			l5xbids.add(item.getCsourcebillbid());
//		}
//
//		if (l5xbids.size() == 0)
//			return;
//
//		// ������Դ��������id ��ȡ ��Դ �ƻ�ͷid �� ��Դ �ƻ�����
//		/**
//		 * ע�� �����ͼƻ�����һһ��Ӧ 1:N ��ͬһ��������Դ�ƻ���������ͬ��
//		 */
//		Object o = getPlanTypeFor4Y(l5xbids);
//
//		if (o == null)
//			return;
//		int ifundtype = Integer.parseInt((((Object[]) o)[1]).toString());
//		// int ifundtype = HgPubTool.getIFundTypeByPlanType(splantype);
//		if (ifundtype == HgPubConst.FUND_CHECK_ERRORTYPE) {
//			checkAndUseFund_NoPlan(gbillvo, flag, isYK,loginCorp);// ����Դ�ڼƻ��� ��������
//			return;
//		}
//
//		Map<String, String[]> planidInfor = (Map<String, String[]>) ((Object[]) o)[0];
//
//		UFDate uDate = head.getDauditdate();
//		if (HgPubConst.IS_DBILLDATE_WHEN_USE) {
//			uDate = head.getDbilldate();
//		}
//		String soubid = null;
//		String[] inInfor = null;
//		String newkey = null;
//		UFDouble nmny = UFDouble.ZERO_DBL;
//		UFDouble nmny1 = UFDouble.ZERO_DBL;//ʵ�۵�ʱ�����Ҫ��дԤ�۵Ľ��
//		UFDouble nbeforemny = UFDouble.ZERO_DBL;
//		Map<String, UFDouble> planmny = new HashMap<String, UFDouble>();
//		Map<String, UFDouble> planmny1 = new HashMap<String, UFDouble>();
//		Map<String, UFDouble> planbeforemny = null;
//
//		planbeforemny = new HashMap<String, UFDouble>();
//
//		for (GeneralBillItemVO item : items) {
//			soubid = item.getCsourcebillbid();
//			inInfor = planidInfor.get(soubid);
//			newkey = inInfor[0] + "," + inInfor[1];
//			if (planmny.containsKey(newkey)) {
//				nmny = planmny.get(newkey);
//			}
//			
//			if(planmny1.containsKey(newkey)){
//				nmny1 = planmny1.get(newkey);
//			}
//			if (isYK) {
//				if (planbeforemny.containsKey(newkey)) {
//					nbeforemny = planbeforemny.get(newkey);
//				}
//			}
//			UFDouble nshouldNum = item.getNshouldoutnum();
//			UFDouble noutNum = PuPubVO.getUFDouble_NullAsZero(item.getNoutnum());
//			if(nshouldNum.compareTo(noutNum)>0){
//				nmny1 = nmny1.add(PuPubVO.getUFDouble_NullAsZero(item.getNplannedmny()));
//			}else{
//				nmny1 =nmny1.add(nshouldNum.multiply(item.getNplannedprice()));
//			}
//			nmny = nmny.add(PuPubVO.getUFDouble_NullAsZero(item.getNplannedmny()));
//			if (isYK) {
//				nbeforemny = nbeforemny.add(PuPubVO.getUFDouble_NullAsZero(
//						item.getNplannedprice()).multiply(PuPubVO.getUFDouble_NullAsZero(item.getNshouldoutnum()), 8));
//			}
//			planmny1.put(newkey, nmny1);
//			planmny.put(newkey, nmny);
//			planbeforemny.put(newkey, nbeforemny);
//		}
//
//		if (planmny.size() == 0)
//			return;
//		if (planmny1.size() == 0)
//			return;
//		if (isYK && planbeforemny.size() == 0)
//			return;
//
//		for (String plankey : planmny.keySet()) {
//			nmny = planmny.get(plankey);
//			nmny1 = planmny1.get(plankey);
//			nbeforemny = planbeforemny.get(plankey);
//			inInfor = plankey.split(",");
//			if (ifundtype == HgPubConst.FUND_CHECK_SPECIALFUND) {
//				if (flag) {
//					getFundBO().useFund1(inInfor[0], ifundtype, inInfor[1],
//							null, uDate, nbeforemny, nmny,
//							head.getPrimaryKey(), head.getCbilltypecode(),nmny1,loginCorp);
//				} else {
//					getFundBO().reUseFund1(inInfor[0], ifundtype, inInfor[1],
//							null, uDate, nbeforemny, nmny,nmny1,loginCorp);
//				}
//			} else if (ifundtype == HgPubConst.FUND_CHECK_FUND_QUATO) {
//				if (flag) {
//					getFundBO().useFund1(inInfor[0], HgPubConst.FUND_CHECK_FUND,
//							inInfor[1], null, uDate, nbeforemny, nmny,
//							head.getPrimaryKey(), head.getCbilltypecode(),nmny1,loginCorp);
//					getFundBO().useFund1(inInfor[0],
//							HgPubConst.FUND_CHECK_QUATO, inInfor[1], null,
//							uDate, nbeforemny, nmny, head.getPrimaryKey(),
//							head.getCbilltypecode(),nmny1,loginCorp);
//				} else {
//					getFundBO().reUseFund1(inInfor[0],
//							HgPubConst.FUND_CHECK_FUND, inInfor[1], null,
//							uDate, nbeforemny, nmny,nmny1,loginCorp);
//					getFundBO().reUseFund1(inInfor[0],
//							HgPubConst.FUND_CHECK_QUATO, inInfor[1], null,
//							uDate, nbeforemny, nmny,nmny1,loginCorp);
//				}
//			}
//		}
//	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ���޼ƻ��������ⵥ ʵ�� 
	 * 2011-4-16����10:25:38
	 * @param gbillvo
	 * @param isDel
	 * @param isYK
	 * @param loginCorp
	 * @throws BusinessException
	 */
	public void checkAndUseFundForAllOut_NoPlan(GeneralBillVO gbillvo, boolean isDel,
			String loginCorp) throws BusinessException {

//		if (gbillvo == null)
//			return;
//		GeneralBillHeaderVO head = gbillvo.getHeaderVO();
//		GeneralBillItemVO[] items = gbillvo.getItemVOs();
//		if (items == null || items.length == 0)
//			return;
//		UFDouble nmny = UFDouble.ZERO_DBL;
//		UFDouble nbeforemny = UFDouble.ZERO_DBL;
//		UFDate uDate = head.getDauditdate();
//		if (HgPubConst.IS_DBILLDATE_WHEN_USE) {
//			uDate = head.getDbilldate();
//		}
//		String cincorp = getAllotInCorp(items[0].getCsourcebillhid());
//		for(GeneralBillItemVO item:items){//����������н���ʵ��			
//			nmny = nmny.add(PuPubVO.getUFDouble_NullAsZero(item.getNplannedmny()));
//			nbeforemny = nbeforemny.add(PuPubVO.getUFDouble_NullAsZero(
//					item.getNplannedprice()).multiply(PuPubVO.getUFDouble_NullAsZero(item.getNshouldoutnum()), 8));
//			if(item.getStatus() == VOStatus.DELETED||isDel){
//				nmny = UFDouble.ZERO_DBL;
//			}
//			getFundBO().useFundForAllOut(null, HgPubConst.FUND_CHECK_FUND,
//					cincorp, null, uDate, nbeforemny, nmny,
//					item.getPrimaryKey(), head.getCbilltypecode(),loginCorp);
//			getFundBO().useFundForAllOut(null,
//					HgPubConst.FUND_CHECK_QUATO, cincorp, null,
//					uDate, nbeforemny, nmny, item.getPrimaryKey(),
//					head.getCbilltypecode(),loginCorp);
//		}
	}

//	public void checkAndUseFund_before_NoPlan(BillVO billvo, boolean flag,String loginCorp)
//			throws BusinessException {
//		if (billvo == null) {
//			return;
//		}
//		BillHeaderVO head = billvo.getHeaderVO();
//		BillItemVO[] items = billvo.getItemVOs();
//		if (items == null || items.length == 0)
//			return;
//
//		UFDouble nmny = UFDouble.ZERO_DBL;
//		for (BillItemVO item : items) {
//			if(item.getStatus() == VOStatus.DELETED)
//				continue;//zhf  ɾ�����в�����
//			nmny = nmny.add(PuPubVO.getUFDouble_NullAsZero(item.getNnotaxmny()));
//		}
//
//		UFDate uDate = head.getDauditdate();
//		if (HgPubConst.IS_DBILLDATE_WHEN_USE) {
//			uDate = head.getDbilldate();
//		}
//
//		if (nmny.doubleValue() == UFDouble.ZERO_DBL.doubleValue())
//			return;
//
//		if (flag) {
//			getFundBO().useFund_Before(null, HgPubConst.FUND_CHECK_FUND,
//					head.getCincorpid(), null, uDate, nmny,
//					head.getPrimaryKey(), head.getCbilltype(),loginCorp);
//			getFundBO().useFund_Before(null, HgPubConst.FUND_CHECK_QUATO,
//					head.getCincorpid(), null, uDate, nmny,
//					head.getPrimaryKey(), head.getCbilltype(),loginCorp);
//		} else {
//			getFundBO().reUseFund_before(null, HgPubConst.FUND_CHECK_FUND,
//					head.getCincorpid(), null, uDate, nmny,loginCorp);
//			getFundBO().reUseFund_before(null, HgPubConst.FUND_CHECK_QUATO,
//					head.getCincorpid(), null, uDate, nmny,loginCorp);
//		}
//	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ������Ԥ�� 2011-4-8����01:44:31
	 * @param billvo
	 * @param flag
	 *            Ԥ��/��Ԥ��
	 * @throws BusinessException
	 */
	public void checkAndUseFund_before(BillVO billvo, boolean isDel,String loginCorp)
	throws BusinessException {
		if (billvo == null)
			return;
		BillHeaderVO head = billvo.getHeaderVO();
		BillItemVO[] items = billvo.getItemVOs();
		if (items == null || items.length == 0)
			return;

		if (PuPubVO.getString_TrimZeroLenAsNull(items[0].getCsourcetypecode()) == null) {
			// ���Ƶ��� �޼ƻ� Ԥ��               ��ʱϵͳû�� ����Ԥ�۵������  �ݲ�����  zhf 20110416
			//			checkAndUseFund_before_NoPlan(billvo, flag,loginCorp);
			return;
		}

		int ifundtype = HgPubTool.getIFundTypeByPlanType(items[0]
		                                                       .getCsourcetypecode());
		if (ifundtype == HgPubConst.FUND_CHECK_ERRORTYPE)
			return;

		String scorpid = head.getCincorpid();

		UFDate uDate = head.getDauditdate();
		if (HgPubConst.IS_DBILLDATE_WHEN_USE) {
			uDate = head.getDbilldate();// ��ȡ������ �ǵ�ǰ���� �����ǻ��ʱ�������
		}

		UFDouble nmny = UFDouble.ZERO_DBL;

		boolean isspecial = ifundtype == HgPubConst.FUND_CHECK_SPECIALFUND;
		for (BillItemVO item:items) {
			if(isspecial){
				if(item.getStatus() == VOStatus.UNCHANGED)
					continue;
				nmny = PuPubVO.getUFDouble_NullAsZero(item.getNnotaxmny());
				if(isDel||item.getStatus() == VOStatus.DELETED)
					nmny = UFDouble.ZERO_DBL;
				getFundBO().useSpeacialFund_Before(item.getCsourcebid(),					
						scorpid, null, uDate, nmny, head.getPrimaryKey(),loginCorp);
				continue;
			}

			if(item.getStatus()==VOStatus.DELETED)//���ǵ��޸�ɾ�����  ���������� zhf2011-04-14
				continue;

			nmny = nmny
			.add(PuPubVO.getUFDouble_NullAsZero(item.getNnotaxmny()));
		}		
		if(isDel){//ɾ�����
			nmny = UFDouble.ZERO_DBL;
		}
		if (ifundtype == HgPubConst.FUND_CHECK_FUND_QUATO) {
			getFundBO().useFund_Before(null,
					HgPubConst.FUND_CHECK_FUND, scorpid, null, uDate,
					nmny, head.getPrimaryKey(), head.getCbilltype(),loginCorp);
			getFundBO().useFund_Before(null,
					HgPubConst.FUND_CHECK_QUATO, scorpid, null, uDate,
					nmny, head.getPrimaryKey(), head.getCbilltype(),loginCorp);
		}
	}


	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ������������д�ۼ��������� 2011-4-8����01:44:52
	 * @param billvo
	 *            �޸ĺ�����
	 * @param preBillvo
	 *            �޸�ǰ����
	 * @param flag
	 *            ����/ɾ��
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
				if(PuPubVO.getString_TrimZeroLenAsNull(newitem.getCsourcebid())==null)
					continue;
				if (flag) {// ����
					// add by zhw ɾ����ʱ��������VO��û��ɾ�� ��� ��Ҫ����DR���ж��Ƿ��Ѿ�ɾ�� ���ɾ�� ���д0
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
					throw new BusinessException("�ۼ���������С����,��д����");
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

		if (al != null) {// ��дԤ���ʽ���е�Ԥ��Ϊ0
			int size = al.size();
			for (int i = 0; i < size; i++) {
				String pk = PuPubVO.getString_TrimZeroLenAsNull(al.get(i));
				String sql1 = " update hg_fundset_b set nmny = '0' where pk_planother_b  = '"
						+ pk + "'";
				getBaseDao().executeUpdate(sql1);
			}
		}

		// У���ۼ������������ܴ��ڼƻ���
		// if(lplanbid.size()>0){
		// validationPlanOut(lplanbid.toArray(new String[0]));
		// }
	}

	// ����PK�鴦�õ��ݵ�DR �ж��Ƿ�ɾ�� add by zhw 2011-01-12 20:41
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
			throw new BusinessException("���ƻ����⣬�����������");
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
	 * @˵�������׸ڿ�ҵ����ѯ�ݲ����� 2011-4-8����02:07:10
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
	 * ���ݴ��У��ô����Ӧ�������Ƿ�����Ÿô��
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-1-8����07:07:58
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
				throw new BusinessException("�ô�������ݲ�����");
			String sql = "select l.invclasscode from bd_invbasdoc c join bd_invcl l on l.pk_invcl=c.pk_invcl  where c.pk_invbasdoc = '"
					+ pk_invbasdoc + "'";
			Object o1 = getBaseDao().executeQuery(sql,
					HgBsPubTool.COLUMNPROCESSOR);
			invcode = PuPubVO.getString_TrimZeroLenAsNull(o1);
			if (alclasscode.contains(invcode)) {
				throw new BusinessException("�ô�������ݲ�����");
			}
		} else {
			String sqlclass = "select l.invclasscode from bd_invcl l where l.pk_invcl='"
					+ pk_invcl + "'";
			Object o3 = getBaseDao().executeQuery(sqlclass,
					HgBsPubTool.COLUMNPROCESSOR);
			if (o3 != null) {
				invclasscode = HgPubTool.getString_NullAsTrimZeroLen(o3);
				if (al.contains(invclasscode)) {
					throw new BusinessException("�ô�������ݲ�����");
				} else {
					for (int i = 0; i < size2; i++) {
						String cl = HgPubTool.getString_NullAsTrimZeroLen(al
								.get(i));
						int lencl = cl.length();
						int leninv = invclasscode.length();
						if (lencl > leninv) {
							String ll = cl.substring(0, leninv);
							if (ll.equalsIgnoreCase(invclasscode)) {
								throw new BusinessException("�ô����������ݲ�����");
							}
						} else {
							String ll = invclasscode.substring(0, lencl);
							if (ll.equalsIgnoreCase(cl)) {
								throw new BusinessException("�ô����������ݲ�����");
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
	 * @˵�������׸ڿ�ҵ�����ղ�ѯ�ƻ� �������� �����ϳ��ⵥ 2011-2-16����09:53:40
	 * @param swhere
	 * @return
	 * @throws BusinessException
	 */
	public Object queryPlanBillForTO5X(String swhere, String s,String billtype)
			throws BusinessException {
		// ��ͷ ����
		if (PuPubVO.getString_TrimZeroLenAsNull(swhere) == null)
			return null;
		StringBuffer str = new StringBuffer();
		PlanVO head = new PlanVO();
		String[] names = head.getAttributeNames();
		// modify by zhw 2010-12-28 ���˵��ظ�������
		str.append("select distinct ");
		// modify by zhw 2010-12-28 ���˵��ظ������� end
		for (String name : names) {
			str.append("h." + name + ",");
		}
		str.append("'aa'");// ����Ϊ��ȥ�� ���һ������
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

		// ��ȡ����
		List<String> lheadid = new ArrayList<String>();
		int len = lhead.size();
		String tmp = null;
		for (int i = 0; i < len; i++) {
			tmp = ((PlanVO) lhead.get(i)).getPrimaryKey();
			if (lheadid.contains(tmp))
				continue;
			lheadid.add(tmp);
		}
		// modify by zhw 2010-12-30 ���˵����� ��������0 �����������˱����� ����-�ۼ���������������
		PlanOtherBVO bvo = new PlanOtherBVO();
		String[] bnames = bvo.getAttributeNames();
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct ");
		for (String bname : bnames) {
			if (!bname.equalsIgnoreCase("lsourceid"))
				buffer.append("b." + bname + ",");
		}
		buffer.append("'aa'");// ����Ϊ��ȥ�� ���һ������
		buffer.append(" from " + bvo.getTableName());
		buffer
				.append(" b join bd_invbasdoc c on c.pk_invbasdoc = b.pk_invbasdoc where ");
		buffer.append(" b.pk_plan  in "
				+ HgPubTool.getSubSql(lheadid.toArray(new String[0])));
		buffer.append(" and coalesce(b.nnum,0.0)>0 and coalesce(b.nnum,0.0) - coalesce(b.nouttotalnum,0.0)>0 and isnull(b.dr,0)=0 and isnull(c.dr,0)=0 ");
		if(billtype.equalsIgnoreCase(HgPubConst.PLAN_MONTH_BILLTYPE)){
			buffer.append(" and (coalesce(b.nreserve10,0.0)-coalesce(b.nouttotalnum,0.0))>0.0 ");
			buffer.append(" and b.vreserve2 ='Y' ");
			// modify by zhw  ƽ���    �ϸ��¼ƻ���������
			buffer.append(" and b.vreserve3 ='N' ");
		}
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
		// modify by zhw 2010-12-30 ���˵����� ��������0 end
	}

	/**
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�����������رպ� ��δ����������д����Դ�ļƻ���   ��ԭԤ�۽�� 2011-01-13����01:44:52
	 * @throws BusinessException
	 */
	public void reWriteToPlanFor5XEnd(BillVO[] vos,String loginCorp)throws BusinessException {
		
		if(vos == null || vos.length==0)
			return;
		
		int len= vos.length;
		
		for(int i= 0;i<len;i++){
			
			BillVO vo = vos[i];
			if(vo !=null){
				UFDate date = vo.getHeaderVO().getDbilldate();
				String corp = vo.getHeaderVO().getCincorpid();
				BillItemVO[] items =(BillItemVO[])vo.getItemVOs();
				for(BillItemVO item:items){
					
					UFDouble nnum = PuPubVO.getUFDouble_NullAsZero(item.getNnum());
					UFDouble nusenum= PuPubVO.getUFDouble_NullAsZero(item.getNorderoutnum());
					UFDouble price = PuPubVO.getUFDouble_NullAsZero(item.getNprice());
					UFDouble temp =nnum.sub(nusenum);
					String csourceid= PuPubVO.getString_TrimZeroLenAsNull(item.getCsourcebid());
					
					if(UFDouble.ZERO_DBL.compareTo(temp)<0){//��������������ڳ������� ��Ҫ��ԭ
						
						UFDouble nmny=price.multiply(temp);
						int ifundtype = HgPubTool.getIFundTypeByPlanType(PuPubVO.getString_TrimZeroLenAsNull(item.getCsourcetypecode()));// ��ȡ�ƻ�������
						
						if (ifundtype == HgPubConst.FUND_CHECK_ERRORTYPE)
							return;
						
						if (ifundtype == HgPubConst.FUND_CHECK_SPECIALFUND) {
							updateVO(corp, date, HgPubConst.FUND_CHECK_SPECIALFUND,nmny,loginCorp);
						} else if (ifundtype == HgPubConst.FUND_CHECK_FUND_QUATO) {
							updateVO(corp, date, HgPubConst.FUND_CHECK_FUND, nmny,loginCorp);
							updateVO(corp, date, HgPubConst.FUND_CHECK_QUATO, nmny,loginCorp);
						}
						
						String sql = "update hg_planother_b set nouttotalnum = coalesce(nouttotalnum,0.0)-"
							+ temp + " where pk_planother_b = '" + csourceid + "'";
					    getBaseDao().executeUpdate(sql);
					}
				}
			}
			
		}
	}

	/**
	 * �������ⵥǩ�ֺ� �ѵ���������ʣ������д �ѵ��������йر� ����������
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-1-20����12:43:02
	 */
	public void reWtriteToPlanFor4YClose(GeneralBillVO gbillvo)
			throws BusinessException {
		if (gbillvo == null)
			return;
		GeneralBillItemVO[] items = gbillvo.getItemVOs();
		Object[] str = new Object[4];
		UFDouble temp = null;
		for (GeneralBillItemVO item : items) {
			str = getItemFromOut(item.getCsourcebillbid()); // ������������ �Ѿ���������
			// ԴͷID
			if (str != null && str.length > 0) {
				temp = PuPubVO.getUFDouble_NullAsZero(str[0]).sub(
						PuPubVO.getUFDouble_NullAsZero(str[1]));
				if (UFDouble.ZERO_DBL.compareTo(temp) < 0) {
					if (!HgPubConst.TO_FROWSTATUS.equalsIgnoreCase(str[3]
							.toString())) {
						String sql = " update hg_planother_b set nouttotalnum = coalesce(nouttotalnum,0.0) - "
								+ temp + " where pk_planother_b = '" + str[2] + "'";
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
	 * @˵�������׸ڿ�ҵ������������д�ۼ��������� 2011-4-8����01:44:52
	 * @param billvo
	 *            �޸ĺ�����
	 * @param preBillvo
	 *            �޸�ǰ����
	 * @param flag
	 *            ����/ɾ��
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

		// У���ۼ������������ܴ��ڼƻ���
		// if(lplanbid.size()>0){
		// validationPlanOut(lplanbid.toArray(new String[0]));
		// }
	}

	/**
	 * ���ݵ������ⵥ����ԴID �ҳ� ������������ �Ѿ��������� ԴͷID
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-1-21����02:55:07
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
	 * ����ǩ�� ��д�����йر��ϵ�Ԥ��
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-1-24����06:05:19
	 * @param gbillvo
	 * @throws BusinessException
	 */
	public void reWriteUseFundBefore(GeneralBillVO gbillvo,String loginCorp)
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
					String corp = HgPubTool.getString_NullAsTrimZeroLen(obj[0]);// ���빫˾
					UFDate date = PuPubVO.getUFDate(obj[1]);// �Ƶ�����
					temp = PuPubVO.getUFDouble_NullAsZero(obj[3]).sub(PuPubVO.getUFDouble_NullAsZero(obj[4]));
					
					if(UFDouble.ZERO_DBL.compareTo(temp)<0){//�ۼ��������ڶ������� ����Ҫ��ԭ
						nmny = temp.multiply(PuPubVO.getUFDouble_NullAsZero(obj[5]),HgPubConst.MNY_DIGIT);// ��ԭ���
						if (PuPubVO.getUFDouble_NullAsZero(nmny).doubleValue() == UFDouble.ZERO_DBL
								.doubleValue())
							return;

						int ifundtype = HgPubTool.getIFundTypeByPlanType(PuPubVO.getString_TrimZeroLenAsNull(obj[2]));// ��ȡ�ƻ�������
						if (ifundtype == HgPubConst.FUND_CHECK_ERRORTYPE)
							return;
						if (ifundtype == HgPubConst.FUND_CHECK_SPECIALFUND) {
							updateVO(corp, date, HgPubConst.FUND_CHECK_SPECIALFUND,nmny,loginCorp);
						} else if (ifundtype == HgPubConst.FUND_CHECK_FUND_QUATO) {
							updateVO(corp, date, HgPubConst.FUND_CHECK_FUND, nmny,loginCorp);
							updateVO(corp, date, HgPubConst.FUND_CHECK_QUATO, nmny,loginCorp);
						}
					}
				}
			}
		}
	}

	/**
	 * ����Ԥ��
	 * 
	 * @author zhw
	 * @throws BusinessException
	 * @˵�������׸ڿ�ҵ�� 2011-1-24����06:01:08
	 */
	public void updateVO(String corp, UFDate date, int ifundtype, UFDouble nmny,String loginCorp)
			throws BusinessException {
		FUNDSETVO vo = getFundSet(corp, date, ifundtype,loginCorp);
		if (vo == null) {
			return;
		}
		UFDouble nlockfund = PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund());// Ԥ��
		if (nlockfund.doubleValue() < nmny.doubleValue())
			throw new BusinessException("���ݴ��󣬳�Ԥ�۽��");

		nlockfund = nlockfund.sub(nmny);// ��дԤ��
		vo.setNlockfund(nlockfund);
		getBaseDao().updateVO(vo, HgBsPubTool.CHECK_LOCkFUND_NAME);
	}

	/**
	 * ���ݵ������ⵥ����ԴID �ҳ� ���빫˾ �Ƶ����� ��Դ�������� ������������ �Ѿ��������� ��˰����
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-1-21����02:55:07
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
	 * ��ȡfundVO
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-1-24����05:40:22
	 * @param scorpid
	 * @param uDate
	 * @param ifundtype
	 * @return
	 * @throws BusinessException
	 */
	public FUNDSETVO getFundSet(String scorpid, UFDate uDate, int ifundtype,String loginCorp)
			throws BusinessException {
		String corp = PuPubVO.getString_TrimZeroLenAsNull(scorpid);
		if (corp == null)
			return null;
		StringBuffer strb = new StringBuffer();
		if (corp != null) {
			strb.append("  pk_corp = '" + corp + "' and ");
		}
		strb.append(" isnull(dr,0)=0 ");
		// add by zhw 2010-12-28 ��ȡ��ǰ���ڶ�Ӧ�Ļ���� �����
		// �õ���׼�ڼ䷽��������ʵ��
		AccountCalendar calendar = AccountCalendar.getInstance();
		calendar.setDate(uDate);
		// ��������
		String iyear = calendar.getYearVO().getPeriodyear();
		String imonth = calendar.getMonthVO().getMonth();
		// zhw end
		strb.append(" and cyear = '" + String.valueOf(iyear)
				+ "' and imonth = '" + String.valueOf(imonth) + "'");
		strb.append(" and coalesce(fcontrol,'N')='Y' and ifundtype = "
				+ ifundtype);
		strb.append(" and vdef2 = '" + loginCorp + "'");

		java.util.Collection c = getBaseDao().retrieveByClause(FUNDSETVO.class,
				strb.toString());
		// getDMO().queryByWhereClause(FUNDSETVO.class, strb.toString());
		if ((c == null || c.size() == 0))
			return null;
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
	 * @˵�������׸ڿ�ҵ�����շ�Ʊ ���ɱ��˵� 2011-2-16����09:53:40
	 * @param swhere
	 * @return
	 * @throws BusinessException
	 */
	public Object queryPlanBillForTOHG10(String swhere,UFDate date)
			throws BusinessException {
		// ��ͷ ����
		if (PuPubVO.getString_TrimZeroLenAsNull(swhere) == null)
			return null;
		StringBuffer str = new StringBuffer();
		InvoiceHeaderVO head = new InvoiceHeaderVO();
		String[] names = head.getDbFields();
		// modify by zhw 2010-12-28 ���˵��ظ�������
		str.append("select distinct ");
		// modify by zhw 2010-12-28 ���˵��ظ������� end
		for (String name : names) {
			str.append("h." + name + ",");
		}
		str.append("'aa'");// ����Ϊ��ȥ�� ���һ������
		str.append(" from po_invoice");
		str
				.append(" h inner join po_invoice_b b on h.cinvoiceid = b.cinvoiceid where ");
		// �õ���׼�ڼ䷽��������ʵ��
//		AccountCalendar calendar = AccountCalendar.getInstance();
//		calendar.setDate(date);
//		// ��������  ����Ʊ������
//		str.append(" h.darrivedate >= '"
//				+ calendar.getMonthVO().getBegindate().toString() + "'");
//		str.append(" and h.darrivedate <= '"
//				+ calendar.getMonthVO().getEnddate().toString() + "'");
		str.append(" h.ibillstatus = '0' and h.vdef20  is null and");
		str.append(swhere);
		Object o = getBaseDao().executeQuery(str.toString(),
				new BeanListProcessor(InvoiceHeaderVO.class));
		if (o == null)
			return null;
		List lhead = (List) o;

		if (lhead.size() == 0)
			return null;

		// ��ȡ����
		List<String> lheadid = new ArrayList<String>();
		int len = lhead.size();
		String tmp = null;
		for (int i = 0; i < len; i++) {
			tmp = ((InvoiceHeaderVO) lhead.get(i)).getPrimaryKey();
			if (lheadid.contains(tmp))
				continue;
			lheadid.add(tmp);
		}
		// modify by zhw 2010-12-30 ���˵����� ��������0 �����������˱����� ����-�ۼ���������������
		InvoiceItemVO bvo = new InvoiceItemVO();
		String[] bnames = bvo.getDbFields();
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct ");
		for (String bname : bnames) {
			buffer.append("b." + bname + ",");
		}
		buffer.append("'aa'");// ����Ϊ��ȥ�� ���һ������
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
		// modify by zhw 2010-12-30 ���˵����� ��������0 end
	}

	/**
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ���ɹ���������ѯ�ɹ���ͬ������ 2012-2-21����02:27:22
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
	 * @˵�������׸ڿ�ҵ�����ݼƻ���Ŀ�ҳ��ⷽʽ 2012-2-21����02:27:22
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
			string = "�ƻ���";
		if ("1".equalsIgnoreCase(str))
			string = "��ͬ��";
		return string;
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
			Object o = getBaseDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
			return o == null ? null : PuPubVO.getUFDouble_NullAsZero(o);
		} else if (billType.equals(ScmConst.m_allocationOut)) {
			String sql = "select sum(b.nplannedmny) from ic_general_h h join ic_general_b b on h.cgeneralhid = b.cgeneralhid  where h.cgeneralhid= '"
					+ pk
					+ "' and h.cbilltypecode = '"
					+ ScmConst.m_allocationOut
					+ "' and isnull(h.dr,0)=0 and isnull(b.dr,0)=0";
			Object o = getBaseDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
			return o == null ? null : PuPubVO.getUFDouble_NullAsZero(o);
		} else if (billType.equals(ScmConst.m_sBillDBDD)) {
			String sql = "select sum(b.nnotaxmny) from to_bill h join to_bill_b b on h.cbillid=b.cbillid where h.cbillid= '"
					+ pk
					+ "' and h.cbilltype = '"
					+ ScmConst.m_sBillDBDD
					+ "' and isnull(h.dr,0)=0 and isnull(b.dr,0)=0";
			Object o = getBaseDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
			return o == null ? null : PuPubVO.getUFDouble_NullAsZero(o);
		} else if (billType.equals(ScmConst.SO_Order)) {
			String sql = "select sum(b.noriginalcurmny) from so_sale h join so_saleorder_b b on h.csaleid =b.csaleid  where h.csaleid  = '"
					+ pk+ "' and h.creceipttype  = '"+ ScmConst.SO_Order + "' and isnull(h.dr,0)=0 and isnull(b.dr,0)=0";
			Object o = getBaseDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
			return o == null ? null : PuPubVO.getUFDouble_NullAsZero(o);
		} else {
			return UFDouble.ZERO_DBL;
		}
	}
	
	public void checkAndUseFund_before(SaleOrderVO billvo, boolean isDel,String loginCorp,UFDouble beforeMny)
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
			nmny = nmny.add(PuPubVO.getUFDouble_NullAsZero(item.getNoriginalcurmny()));
		}

		if (nmny.doubleValue() == UFDouble.ZERO_DBL.doubleValue())
			return;

		UFDate uDate = head.getDapprovedate();
		if (HgPubConst.IS_DBILLDATE_WHEN_USE) {
			uDate = head.getDbilldate();
		}

		if (!isDel) {
			getFundBO().useFund_Before_SoOrder(null, HgPubConst.FUND_CHECK_FUND,
					head.getCcustomerid(), null, uDate, nmny,beforeMny,loginCorp);
			getFundBO().useFund_Before_SoOrder(null, HgPubConst.FUND_CHECK_QUATO,
					head.getCcustomerid(), null, uDate, nmny,beforeMny,loginCorp);
		} else {
			getFundBO().reUseFund_before_SoOrder(null, HgPubConst.FUND_CHECK_FUND,
					head.getCcustomerid(),null, uDate, nmny,loginCorp);
			getFundBO().reUseFund_before_SoOrder(null, HgPubConst.FUND_CHECK_QUATO,
					head.getCcustomerid(),null, uDate, nmny,loginCorp);
		}
     }
	
	/**
	 * ��ȡʣ�������
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-4-20����05:23:41
	 * @param cbillid
	 * @return
	 * @throws DAOException
	 */
	private UFDouble  getRemainNum(String cbillid) throws DAOException{
		if(PuPubVO.getString_TrimZeroLenAsNull(cbillid)==null)
			return UFDouble.ZERO_DBL;
		String  sql = " select coalesce(b.nnum,0)-coalesce(b.norderoutnum,0)  from to_bill_b b  where b.cbill_bid='"+cbillid+"' and isnull(b.dr,0)=0 ";
		return PuPubVO.getUFDouble_NullAsZero(getBaseDao().executeQuery(sql,HgBsPubTool.COLUMNPROCESSOR));
	}
	
	/**
	 * ��ȡ���ⵥ�޸�ǰ����
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-4-20����05:23:41
	 * @param cbillid
	 * @return
	 * @throws DAOException
	 */
	private UFDouble  getOldOutNum(String cgeneralbid) throws DAOException{
		if(PuPubVO.getString_TrimZeroLenAsNull(cgeneralbid)==null)
			return UFDouble.ZERO_DBL;
		String  sql = " select b.noutnum from ic_general_b b  where b.cgeneralbid='"+cgeneralbid+"' and isnull(b.dr,0)=0 ";
		return PuPubVO.getUFDouble_NullAsZero(getBaseDao().executeQuery(sql,HgBsPubTool.COLUMNPROCESSOR));
	}
}

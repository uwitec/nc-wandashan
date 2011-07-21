package nc.bs.hg.pu.check.allowance;

import java.util.ArrayList;

import nc.bs.dao.BaseDAO;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.bs.hg.scm.pub.HgScmPubBO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.trade.comchkref.CheckRef;
import nc.vo.hg.pu.check.allowance.ALLOWANCEVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBDACTION;

public class AllowanceCheckCHK implements IBDBusiCheck, IBDACTION {

	CheckRef check = null;
	private BaseDAO dao = null;

	private BaseDAO getBaseDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	/**
	 * CostProjectBsBackCHK ������ע��
	 */
	public AllowanceCheckCHK() {
		super();

	}

	/**
	 * У��
	 */
	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
			throws Exception {
		switch (intBdAction) {
		case DELETE:
			onCheckDelete((HYBillVO) vo);
			break;
		case SAVE:
			onCheckSave((HYBillVO) vo);
			break;
		}

	}

	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {

	}

	private void onCheckSave(HYBillVO vo) throws Exception {
		// nplan �ƻ��ݲ���� narrival�����ݲ���� noutbound�����ݲ����
		UFDouble nplan = UFDouble.ZERO_DBL;
		UFDouble narrival = UFDouble.ZERO_DBL;
		UFDouble noutbound = UFDouble.ZERO_DBL;
		ALLOWANCEVO[] bvos = (ALLOWANCEVO[]) vo.getChildrenVO();
		if (bvos == null || bvos.length == 0)
			return;

		ALLOWANCEVO bvo = (ALLOWANCEVO) bvos[0];
		String invbasdoc = PuPubVO.getString_TrimZeroLenAsNull(bvo
				.getPk_invbasdoc());
		if(PuPubVO.getUFBoolean_NullAs(bvo.getVdef1(),UFBoolean.FALSE).booleanValue())
			return;
		String invcl = PuPubVO.getString_TrimZeroLenAsNull(bvo.getPk_invcl());

		HgScmPubBO bo = new HgScmPubBO();
		String pk_allowance = bvo.getPk_allowance();
		String where = null;
		if (pk_allowance != null && !"".equals(pk_allowance)) {
			where = " and pk_allowance <> '" + pk_allowance + "' ";
		}
		
		bo.checkAllowanceSetByInvcl(invbasdoc,invcl,bvo.getPrimaryKey());
//		if (invbasdoc != null) {//��������Ϊ��  
//			
//			nplan = bo.getAllowanceSet(invbasdoc, "nplan", where);
//			if (PuPubVO.getUFDouble_ZeroAsNull(nplan) != null) {
//				throw new BusinessException("�ô���ļƻ��ݲ�������ڣ�");
//			}
//			
//			narrival = bo.getAllowanceSet(invbasdoc, "narrival", where);
//			if (PuPubVO.getUFDouble_ZeroAsNull(narrival) != null) {
//				throw new BusinessException("�ô���ĵ����ݲ�������ڣ�");
//			}
//			
//			noutbound = bo.getAllowanceSet(invbasdoc, "noutbound",where);
//			if (PuPubVO.getUFDouble_ZeroAsNull(noutbound) != null) {
//				throw new BusinessException("�ô���ĳ����ݲ�������ڣ�");
//			}
//		}else if (invcl != null) {//���������಻Ϊ��
//			String sql = " select invclasscode from bd_invcl where pk_invcl =  '"+ invcl + "'";
//		    String invcode = PuPubVO.getString_TrimZeroLenAsNull(getBaseDao()
//				.executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR));
//		    bo.checkAllowanceSetByInvcl(invbasdoc,invcl,bvo.getPrimaryKey());
//			nplan = bo.getAllowanceSetByInvcl(invcode, "nplan", where);// �÷�����ݲ�
//			if (nplan != null)
//				throw new BusinessException("�����ʷ���ļƻ��ݲ�������ڣ�");
//			bo.checkAllowanceSetByInvcl(invbasdoc,invcl,bvo.getPrimaryKey());
//			narrival = bo.getAllowanceSetByInvcl(invcode, "narrival",
//					where);// �÷�����ݲ�
//			if (narrival != null)
//				throw new BusinessException("�����ʷ���ĵ����ݲ�������ڣ�");
//			
//			noutbound = bo.getAllowanceSetByInvcl(invcode, "noutbound",
//					where);// �÷�����ݲ�
//			if (noutbound != null)
//				throw new BusinessException("�����ʷ���ĳ����ݲ��������ڣ�");
//		}
//			
	}

	private void onCheckDelete(HYBillVO vo) throws Exception {

	}

	private void checkTS(String pk, String tsOld, Class className)
			throws Exception {

	}
}

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
	 * CostProjectBsBackCHK 构造子注释
	 */
	public AllowanceCheckCHK() {
		super();

	}

	/**
	 * 校验
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
		// nplan 计划容差比例 narrival到货容差比例 noutbound出库容差比例
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
//		if (invbasdoc != null) {//如果存货不为空  
//			
//			nplan = bo.getAllowanceSet(invbasdoc, "nplan", where);
//			if (PuPubVO.getUFDouble_ZeroAsNull(nplan) != null) {
//				throw new BusinessException("该存货的计划容差比例存在！");
//			}
//			
//			narrival = bo.getAllowanceSet(invbasdoc, "narrival", where);
//			if (PuPubVO.getUFDouble_ZeroAsNull(narrival) != null) {
//				throw new BusinessException("该存货的到货容差比例存在！");
//			}
//			
//			noutbound = bo.getAllowanceSet(invbasdoc, "noutbound",where);
//			if (PuPubVO.getUFDouble_ZeroAsNull(noutbound) != null) {
//				throw new BusinessException("该存货的出库容差比例存在！");
//			}
//		}else if (invcl != null) {//如果存货分类不为空
//			String sql = " select invclasscode from bd_invcl where pk_invcl =  '"+ invcl + "'";
//		    String invcode = PuPubVO.getString_TrimZeroLenAsNull(getBaseDao()
//				.executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR));
//		    bo.checkAllowanceSetByInvcl(invbasdoc,invcl,bvo.getPrimaryKey());
//			nplan = bo.getAllowanceSetByInvcl(invcode, "nplan", where);// 该分类的容差
//			if (nplan != null)
//				throw new BusinessException("该物资分类的计划容差比例存在！");
//			bo.checkAllowanceSetByInvcl(invbasdoc,invcl,bvo.getPrimaryKey());
//			narrival = bo.getAllowanceSetByInvcl(invcode, "narrival",
//					where);// 该分类的容差
//			if (narrival != null)
//				throw new BusinessException("该物资分类的到货容差比例存在！");
//			
//			noutbound = bo.getAllowanceSetByInvcl(invcode, "noutbound",
//					where);// 该分类的容差
//			if (noutbound != null)
//				throw new BusinessException("该物资分类的出库容差比例存存在！");
//		}
//			
	}

	private void onCheckDelete(HYBillVO vo) throws Exception {

	}

	private void checkTS(String pk, String tsOld, Class className)
			throws Exception {

	}
}

package nc.bs.pub.action;


import java.util.Hashtable;

import nc.bs.hg.pu.plan.pub.PlanPubBO;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.uap.pf.PFBusinessException;

/**
 * ��������ƻ�
 * @author Administrator
 *
 */
public class N_HG01_UNAPPROVE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;

	public N_HG01_UNAPPROVE() {
		super();
	}

	/*
	 * ��ע��ƽ̨��д������ �ӿ�ִ����
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			
			AggregatedValueObject billvo = getVo();
			if(billvo == null){
				throw new BusinessException("��������Ϊ��");
			}
			
			HYBillVO planvo = (HYBillVO)billvo;
			
			//У���Ƿ��������
			PlanPubBO bo = new PlanPubBO();
			bo.checkPlanOnUnApprove(PuPubVO.getString_TrimZeroLenAsNull(getUserObj()), vo.m_coId, new UFDate(vo.m_currentDate), billvo);
			
			// ####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####
			procUnApproveFlow(vo);
			Object retObj = runClass("nc.bs.hg.pu.pub.HYBillUnApprove",
					"unApproveHYBill", "nc.vo.pub.AggregatedValueObject:01",
					vo, m_keyHas, m_methodReturnHas);
			
			//������ɺ��д   ɾ�����ɵĶ�Ӧ�¼ƻ�
			bo.reWriteNextBillOnUnapprove(planvo);
			return retObj;
		} catch (Exception ex) {
			if (ex instanceof BusinessException)
				throw (BusinessException) ex;
			else
				throw new PFBusinessException(ex.getMessage(), ex);
		}
	}

	/*
	 * ��ע��ƽ̨��дԭʼ�ű�
	 */
	public String getCodeRemark() {
		return "	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\n procUnApproveFlow (vo); \nObject retObj=runClassCom@ \"nc.bs.pp.pub.comstatus.HYBillUnApprove\", \"unApproveHYBill\", \"nc.vo.pub.AggregatedValueObject:01\"@;\nreturn retObj;\n";
	}

	/*
	 * ��ע�����ýű�������HAS
	 */
	private void setParameter(String key, Object val) {
		if (m_keyHas == null) {
			m_keyHas = new Hashtable();
		}
		if (val != null) {
			m_keyHas.put(key, val);
		}
	}
}

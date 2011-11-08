package nc.bs.pub.action;


import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.wds.ic.write.back4c.Writeback4cHVO;

/**
 *   * ���۳���ش�

 * @author Administrator
 *
 */
public class N_WDSO_UNAPPROVE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;

	public N_WDSO_UNAPPROVE() {
		super();
	}

	/*
	 * ��ע��ƽ̨��д������ �ӿ�ִ����
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			procUnApproveFlow(vo);
			setParameter("currentVo", vo.m_preValueVo);
			setParameter("date", vo.m_currentDate);
			setParameter("operator", vo.m_operator);
			setParameter("pk_corp",vo.m_coId);
			//
			AggregatedValueObject[] icBillVO = (AggregatedValueObject[]) runClass("nc.bs.wds.ic.so.out.ChangeTo4C", "canelSignQueryGenBillVO",
					"&currentVo:nc.vo.pub.AggregatedValueObject,&operator:String,&date:String", vo, m_keyHas,m_methodReturnHas);
			setParameter("AggObject",icBillVO);
			runClass("nc.bs.wds.ic.so.out.SoOutBO", "canelPushSign4C","&date:String,&AggObject:nc.vo.pub.AggregatedValueObject[]", vo, m_keyHas,m_methodReturnHas);
			//���ı�����������Ϣ
			Writeback4cHVO head = (Writeback4cHVO)vo.m_preValueVo.getParentVO();
			head.setDapprovedate(null);
			head.setVapproveid(null);
			Object retObj = runClass("nc.bs.wl.pub.HYBillUnApprove",
					"unApproveHYBill", "nc.vo.pub.AggregatedValueObject:01",
					vo, m_keyHas, m_methodReturnHas);
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

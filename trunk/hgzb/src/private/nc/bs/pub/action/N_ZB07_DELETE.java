package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.zb.avnum.AvNumBO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.zb.avnum.AvNumBodyVO;


/**
 * ��������������
 * @author Administrator
 *
 */
public class N_ZB07_DELETE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;


	public N_ZB07_DELETE() {
		super();
	}

	/*
	 * ��ע��ƽ̨��д������ �ӿ�ִ����
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			// ####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####
			HYBillVO billvo = (HYBillVO)getVo();
            if(billvo ==null)
          	  throw new BusinessException("��������Ϊ��");
            AvNumBodyVO[] bodys = (AvNumBodyVO[])billvo.getChildrenVO();
            if(bodys ==null ||bodys.length==0)
          	  throw new BusinessException("�����������Ϊ��");
			Object retObj = null;
			// ����˵��:��ҵ����ɾ��
			retObj = runClass("nc.bs.trade.comdelete.BillDelete", "deleteBill",
					"nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,
					m_methodReturnHas);
			AvNumBO bo =new AvNumBO();
			bo.deleteBill(bodys);
			// ##################################################
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
		return "	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\nObject retObj  =null;\n//����˵��:��ҵ����ɾ��\nretObj  =runClassCom@ \"nc.bs.trade.comdelete.BillDelete\", \"deleteBill\", \"nc.vo.pub.AggregatedValueObject:01\"@;\n//##################################################\nreturn retObj;\n";
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

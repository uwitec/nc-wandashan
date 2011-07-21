package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.pf.PfUtilActionVO;
import nc.vo.uap.pf.PFBusinessException;

/**
 *ר���ʽ�ƻ����
 * @author Administrator
 *
 */
public class N_HG04_APPROVECOND extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;


	public N_HG04_APPROVECOND() {
		super();
	}

	/*
	 * ��ע��ƽ̨��д������ �ӿ�ִ����
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			// ####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####
			Object retObj = null;
			// ####��Ҫ˵���������Ϊ����PNL�Ľ��淽�����ܽ����޸�####
			String[] infoAry = new String[3];
			infoAry[0] = (String) getVo().getParentVO().getAttributeValue(
					"pk_billtype");
			infoAry[1] = getVo().getParentVO().getPrimaryKey();
			infoAry[2] = (String) getVo().getParentVO().getAttributeValue(
					"pk_busitype");
			setUserObj(infoAry);
			// ########�÷���������ű��в��������return���#########
			PfUtilActionVO actionVo = new PfUtilActionVO();
			actionVo.setIsDLG(true);
			actionVo.setClassName("nc.ui.pub.workflownote.FlowStateDlg");
			actionVo.setMethod("");
			actionVo.setParameter("");
			actionVo.setUIObj(m_tmpVo.m_userObj);
			return actionVo;
			// ##################################################
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
		return "	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\n Object retObj  =null;\n //####��Ҫ˵���������Ϊ����PNL�Ľ��淽�����ܽ����޸�####\n String[] infoAry=new String[3];\n infoAry[0]=(String)getVo().getParentVO().getAttributeValue(\"pk_billtype\");\n infoAry[1]=getVo().getParentVO().getPrimaryKey();\n infoAry[2]=(String)getVo().getParentVO().getAttributeValue(\"pk_busitype\");\n setUserObj(infoAry);\n //########�÷���������ű��в��������return���#########\n PfUtilActionVO actionVo=new PfUtilActionVO();\n actionVo.setIsDLG(true);\n actionVo.setClassName(\"nc.ui.pub.workflownote.FlowStateDlg\");\n actionVo.setMethod( \"\");\n actionVo.setParameter( \"\");\n actionVo.setUIObj(m_tmpVo.m_userObj);\n return actionVo;\n //##################################################\n";
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

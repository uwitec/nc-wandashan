package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;

/**
 * 调入运单
 * @author Administrator
 *
 */
public class N_WS21_APPROVE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;

	public N_WS21_APPROVE() {
		super();
	}

	/*
	 * 备注：平台编写规则类 接口执行类
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			// ####该组件为单动作工作流处理开始...不能进行修改####
			Object m_sysflowObj = procActionFlow(vo);
			if (m_sysflowObj != null) {
				return m_sysflowObj;
			}
			// ####该组件为单动作工作流处理结束...不能进行修改####
			Object retObj = null;
			setParameter("currentVo", vo.m_preValueVo);
//			vo.m_preValueVo.getParentVO().setAttributeValue("itransstatus", 2);
			if(vo.m_preValueVo.getParentVO().getAttributeValue("ntransmny")==null)
				throw new BusinessException("请录入运费");
			retObj = runClass("nc.bs.wl.pub.HYBillApproveForwds3", "approveHYBill",
					"&currentVo:nc.vo.pub.AggregatedValueObject", vo, m_keyHas,
					m_methodReturnHas);
			return retObj;
		} catch (Exception ex) {
			if (ex instanceof BusinessException)
				throw (BusinessException) ex;
			else
				throw new PFBusinessException(ex.getMessage(), ex);
		}
	}

	/*
	 * 备注：平台编写原始脚本
	 */
	public String getCodeRemark() {
		return "	//####该组件为单动作工作流处理开始...不能进行修改####\nprocActionFlow@@;\n//####该组件为单动作工作流处理结束...不能进行修改####\nObject  retObj  =null;\n setParameter(\"currentVo\",vo.m_preValueVo);           \nretObj =runClassCom@ \"nc.bs.pp.pp0201.ApproveAction\", \"approveHYBill\", \"nc.vo.pub.AggregatedValueObject:01\"@;\n            ArrayList ls = (ArrayList)getUserObj();\n       \n        setParameter(\"userOpt\",ls.get(1));               \n            runClassCom@ \"nc.bs.pp.pp0201.ApproveAction\", \"afterApprove\", \"&userOpt:java.lang.Integer,nc.vo.pub.AggregatedValueObject:01\"@;               \nreturn retObj;\n";
	}

	/*
	 * 备注：设置脚本变量的HAS
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

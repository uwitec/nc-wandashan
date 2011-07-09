package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;

/**
 * 发运计划录入
 * @author Administrator
 *
 */
public class N_WDS1_APPROVE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;

	public N_WDS1_APPROVE() {
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
			Object retObj = null;
			setParameter("currentVo", vo.m_preValueVo);
			//审批前的校验 计划数量 和计划辅数量不能都为 空  只对月计划校验
			Object iplantype =vo.m_preValueVo.getParentVO().getAttributeValue("iplantype");
			if(iplantype!=null && 0==(Integer)iplantype){
				runClass("nc.bs.wl.plan.PlanCheckinBO","checkNotAllNull","&currentVo:nc.vo.pub.AggregatedValueObject",vo, m_keyHas,m_methodReturnHas);		
			}
			// ####该组件为单动作工作流处理结束...不能进行修改####	
			retObj = runClass("nc.bs.wl.pub.HYBillApprove", "approveHYBill",
					"&currentVo:nc.vo.pub.AggregatedValueObject", vo, m_keyHas,
					m_methodReturnHas);			
			//审批后，将审批后的追加计划，合并到月计划
//			Object iplantype =vo.m_preValueVo.getParentVO().getAttributeValue("iplantype");
//			if(iplantype!=null && 1==(Integer)iplantype){
//				runClass("nc.bs.wl.plan.PlanCheckinBO","planStats","&currentVo:nc.vo.pub.AggregatedValueObject",vo, m_keyHas,m_methodReturnHas);		
//			}			
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

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
 * 销售出库回传审批
 * @author Administrator
 *
 */
public class N_WDSO_APPROVE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;

	public N_WDSO_APPROVE() {
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
			setParameter("date", vo.m_currentDate);
			setParameter("operator", vo.m_operator);
			setParameter("pk_corp",vo.m_coId);
			//签字生成ERP销售出库单
			AggregatedValueObject icBillVO = (AggregatedValueObject) runClass("nc.bs.wds.ic.so.out.ChangeTo4C", "signQueryGenBillVO",
					"&currentVo:nc.vo.pub.AggregatedValueObject,&operator:String,&date:String", vo, m_keyHas,m_methodReturnHas);
			setParameter("AggObject",icBillVO);
			runClass("nc.bs.wds.ic.so.out.SoOutBO", "pushSign4C","&date:String,&AggObject:nc.vo.pub.AggregatedValueObject", vo, m_keyHas,m_methodReturnHas);
			//
			Writeback4cHVO head = (Writeback4cHVO)vo.m_preValueVo.getParentVO();
			head.setDapprovedate(new UFDate(vo.m_currentDate));
			head.setVapproveid(vo.m_operator);
			retObj = runClass("nc.bs.wl.pub.HYBillApprove", "approveHYBill",
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

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
 * 物资需求计划
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
	 * 备注：平台编写规则类 接口执行类
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			
			AggregatedValueObject billvo = getVo();
			if(billvo == null){
				throw new BusinessException("传入数据为空");
			}
			
			HYBillVO planvo = (HYBillVO)billvo;
			
			//校验是否可以弃审
			PlanPubBO bo = new PlanPubBO();
			bo.checkPlanOnUnApprove(PuPubVO.getString_TrimZeroLenAsNull(getUserObj()), vo.m_coId, new UFDate(vo.m_currentDate), billvo);
			
			// ####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####
			procUnApproveFlow(vo);
			Object retObj = runClass("nc.bs.hg.pu.pub.HYBillUnApprove",
					"unApproveHYBill", "nc.vo.pub.AggregatedValueObject:01",
					vo, m_keyHas, m_methodReturnHas);
			
			//弃审完成后回写   删除生成的对应月计划
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
	 * 备注：平台编写原始脚本
	 */
	public String getCodeRemark() {
		return "	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n procUnApproveFlow (vo); \nObject retObj=runClassCom@ \"nc.bs.pp.pub.comstatus.HYBillUnApprove\", \"unApproveHYBill\", \"nc.vo.pub.AggregatedValueObject:01\"@;\nreturn retObj;\n";
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

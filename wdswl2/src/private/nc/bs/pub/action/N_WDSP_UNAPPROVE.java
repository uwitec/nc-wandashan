package nc.bs.pub.action;


import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.wds.ic.write.back4y.Writeback4yHVO;

/**
 *  调拨入库回传
 * @author Administrator
 *
 */
public class N_WDSP_UNAPPROVE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;

	public N_WDSP_UNAPPROVE() {
		super();
	}

	/*
	 * 备注：平台编写规则类 接口执行类
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
			AggregatedValueObject[] icBillVO = (AggregatedValueObject[]) runClass("nc.bs.wds.ic.allocation.in.ChangeTo4E", "canelSignQueryGenBillVO",
					"&currentVo:nc.vo.pub.AggregatedValueObject,&operator:String,&date:String", vo, m_keyHas,m_methodReturnHas);
			setParameter("AggObject",icBillVO);
			runClass("nc.bs.wds.ic.allocation.in.AllocationInBO", "canelPushSign4E","&date:String,&AggObject:nc.vo.pub.AggregatedValueObject[]", vo, m_keyHas,m_methodReturnHas);
			//更改本单据审批信息
			Writeback4yHVO head = (Writeback4yHVO)vo.m_preValueVo.getParentVO();
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

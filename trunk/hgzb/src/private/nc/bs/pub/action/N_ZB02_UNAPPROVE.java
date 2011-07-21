package nc.bs.pub.action;


import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.zb.bidding.make.MakeBiddingBO;
import nc.bs.zb.comments.CommentsBO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.zb.comments.BidEvaluationHeaderVO;
import nc.vo.zb.pub.ZbPubConst;

/**
 *中标审批表弃审
 * @author Administrator
 *
 */
public class N_ZB02_UNAPPROVE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;

	public N_ZB02_UNAPPROVE() {
		super();
	}

	/*
	 * 备注：平台编写规则类 接口执行类
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			// ####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####
             HYBillVO newbill = (HYBillVO)getVo();
			if(newbill==null)
				throw new BusinessException("传入数据为空");
             CommentsBO bo = new CommentsBO();
			 bo.isHaveBill(newbill.getParentVO().getPrimaryKey(),((BidEvaluationHeaderVO)newbill.getParentVO()).getCbiddingid());	
			procUnApproveFlow(vo);
			Object retObj = runClass("nc.bs.zb.pub.HYBillUnApprove",
					"unApproveHYBill", "nc.vo.pub.AggregatedValueObject:01",
					vo, m_keyHas, m_methodReturnHas);
			//删除下游单据
			bo.deleteDownBill(newbill.getParentVO().getPrimaryKey());
			
			//回写业务状态 中标
			MakeBiddingBO biddingbo = new MakeBiddingBO();
			biddingbo.updateBiddingBusiState(((BidEvaluationHeaderVO)newbill.getParentVO()).getCbiddingid(), ZbPubConst.BIDDING_BUSINESS_STATUE_RESULT);
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

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
 *�б�����������
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
	 * ��ע��ƽ̨��д������ �ӿ�ִ����
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			// ####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####
             HYBillVO newbill = (HYBillVO)getVo();
			if(newbill==null)
				throw new BusinessException("��������Ϊ��");
             CommentsBO bo = new CommentsBO();
			 bo.isHaveBill(newbill.getParentVO().getPrimaryKey(),((BidEvaluationHeaderVO)newbill.getParentVO()).getCbiddingid());	
			procUnApproveFlow(vo);
			Object retObj = runClass("nc.bs.zb.pub.HYBillUnApprove",
					"unApproveHYBill", "nc.vo.pub.AggregatedValueObject:01",
					vo, m_keyHas, m_methodReturnHas);
			//ɾ�����ε���
			bo.deleteDownBill(newbill.getParentVO().getPrimaryKey());
			
			//��дҵ��״̬ �б�
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

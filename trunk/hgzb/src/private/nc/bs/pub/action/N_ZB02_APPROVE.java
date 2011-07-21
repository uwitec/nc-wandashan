package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.zb.bidding.make.MakeBiddingBO;
import nc.bs.zb.comments.CommentsBO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.zb.comments.BidEvaluationHeaderVO;
import nc.vo.zb.pub.ZbPubConst;

/**
 * �б�����������
 * @author Administrator
 *
 */
public class N_ZB02_APPROVE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;

	public N_ZB02_APPROVE() {
		super();
	}

	/*
	 * ��ע��ƽ̨��д������ �ӿ�ִ����
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			
			AggregatedValueObject abillvo = getVo();
			Object o =getUserObj();
			if(abillvo == null){
				throw new BusinessException("��������Ϊ��");
			}
			
			HYBillVO billvo = (HYBillVO)abillvo;
			// ####�����Ϊ����������������ʼ...���ܽ����޸�####
			Object m_sysflowObj = procActionFlow(vo);
			if (m_sysflowObj != null) {
				return m_sysflowObj;
			}
			// ####�����Ϊ�������������������...���ܽ����޸�####
			Object retObj = null;
			setParameter("currentVo", vo.m_preValueVo);
			retObj = runClass("nc.bs.zb.pub.HYBillApprove", "approveHYBill",
					"nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,
					m_methodReturnHas);
			HYBillVO newbill = (HYBillVO)retObj;
			BidEvaluationHeaderVO head = (BidEvaluationHeaderVO)newbill.getParentVO();
			if(head.getVbillstatus()==IBillStatus.CHECKPASS){
				CommentsBO bo =new CommentsBO();
				bo.pushSaveEntry(billvo,o);
				
//				������ҵ��״̬�޸�Ϊ  ���
				MakeBiddingBO biddingbo = new MakeBiddingBO();
				biddingbo.updateBiddingBusiState(head.getCbiddingid(), ZbPubConst.BIDDING_BUSINESS_STATUE_CLOSE);
			}
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
		return "	//####�����Ϊ����������������ʼ...���ܽ����޸�####\nprocActionFlow@@;\n//####�����Ϊ�������������������...���ܽ����޸�####\nObject  retObj  =null;\n setParameter(\"currentVo\",vo.m_preValueVo);           \nretObj =runClassCom@ \"nc.bs.pp.pp0201.ApproveAction\", \"approveHYBill\", \"nc.vo.pub.AggregatedValueObject:01\"@;\n            ArrayList ls = (ArrayList)getUserObj();\n       \n        setParameter(\"userOpt\",ls.get(1));               \n            runClassCom@ \"nc.bs.pp.pp0201.ApproveAction\", \"afterApprove\", \"&userOpt:java.lang.Integer,nc.vo.pub.AggregatedValueObject:01\"@;               \nreturn retObj;\n";
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

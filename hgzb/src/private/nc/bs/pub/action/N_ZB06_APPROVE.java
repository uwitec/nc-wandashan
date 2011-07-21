package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.zb.bidding.make.MakeBiddingBO;
import nc.bs.zb.bidfloor.BidFloorBO;
import nc.ui.zb.bidfloor.BidFloorBodyVO;
import nc.ui.zb.bidfloor.BidFloorHeadVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.zb.pub.ZbPubConst;

/**
 * ��׼�ά������
 * @author Administrator
 *
 */
public class N_ZB06_APPROVE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;

	public N_ZB06_APPROVE() {
		super();
	}

	/*
	 * ��ע��ƽ̨��д������ �ӿ�ִ����
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			
			AggregatedValueObject billvo = getVo();
			if(billvo == null){
				throw new BusinessException("��������Ϊ��");
			}
			
			HYBillVO biddingvo = (HYBillVO)billvo;
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
			BidFloorHeadVO head = (BidFloorHeadVO)newbill.getParentVO();
			if(head.getVbillstatus()==IBillStatus.CHECKPASS){
				//��д�����ϵı�׼� �Ƿ����Ϊ�����ı�׼�
				BidFloorBO bo = new BidFloorBO();
				bo.reWriteBidding((BidFloorBodyVO[])biddingvo.getChildrenVO());
//				��д����ҵ��״̬Ϊ  Ͷ��
				MakeBiddingBO biddbo = new MakeBiddingBO();
				biddbo.updateBiddingBusiState(head.getCbiddingid(), ZbPubConst.BIDDING_BUSINESS_STATUE_SUBMIT);
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

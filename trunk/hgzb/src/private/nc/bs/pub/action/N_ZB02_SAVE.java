package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.zb.comments.BiEvaluationBodyVO;
import nc.vo.zb.comments.BidSlvendorVO;

/**
 * �б��������ύ
 * @author Administrator
 *
 */
public class N_ZB02_SAVE extends AbstractCompiler2 {
	
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();

	private Hashtable m_keyHas = null;

	public N_ZB02_SAVE() {
		super();
	}


	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			// ####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####
                  HYBillVO billvo = (HYBillVO)getVo();
                  if(billvo ==null)
                	  throw new BusinessException("��������Ϊ��");
                  BiEvaluationBodyVO[] bodys = (BiEvaluationBodyVO[])billvo.getChildrenVO();
                  if(bodys ==null ||bodys.length==0)
                	  throw new BusinessException("�����������Ϊ��");
                  for(BiEvaluationBodyVO body:bodys){
                	  
                	  BidSlvendorVO[] svos = body.getBidSlvendorVOs();
                	  if(svos ==null ||svos.length==0)
                    	  throw new BusinessException("���빩Ӧ������Ϊ��");
                	  UFDouble zbnum =UFDouble.ZERO_DBL;//�б�����
                	  for(BidSlvendorVO svo:svos){
                		  zbnum= zbnum.add(PuPubVO.getUFDouble_NullAsZero(svo.getNzbnum()));
                		  svo.validataOnSaveSplitNum(body.getNzbprice());
                	  }
                	  if(zbnum.compareTo(UFDouble.ZERO_DBL)==0)
                		  throw new BusinessException("���ȷ��������ύ");
                  }

			Object retObj = runClass("nc.bs.zb.pub.HYBillCommit",
					"commitHYBill", "nc.vo.pub.AggregatedValueObject:01", vo,
					m_keyHas, m_methodReturnHas);
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
		return "	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\nrunClassCom@ \"nc.bs.pp.pp0201.BillCommit\", \"beforeCommitCheck\", \"nc.vo.pub.AggregatedValueObject:01\"@;\nObject retObj =runClassCom@ \"nc.bs.pp.pub.comstatus.HYBillCommit\", \"commitHYBill\", \"nc.vo.pub.AggregatedValueObject:01\"@;\nreturn retObj;\n";
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

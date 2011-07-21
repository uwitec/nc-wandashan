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
 * 中标审批表提交
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
			// ####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####
                  HYBillVO billvo = (HYBillVO)getVo();
                  if(billvo ==null)
                	  throw new BusinessException("传入数据为空");
                  BiEvaluationBodyVO[] bodys = (BiEvaluationBodyVO[])billvo.getChildrenVO();
                  if(bodys ==null ||bodys.length==0)
                	  throw new BusinessException("传入表体数据为空");
                  for(BiEvaluationBodyVO body:bodys){
                	  
                	  BidSlvendorVO[] svos = body.getBidSlvendorVOs();
                	  if(svos ==null ||svos.length==0)
                    	  throw new BusinessException("传入供应商数据为空");
                	  UFDouble zbnum =UFDouble.ZERO_DBL;//中标数量
                	  for(BidSlvendorVO svo:svos){
                		  zbnum= zbnum.add(PuPubVO.getUFDouble_NullAsZero(svo.getNzbnum()));
                		  svo.validataOnSaveSplitNum(body.getNzbprice());
                	  }
                	  if(zbnum.compareTo(UFDouble.ZERO_DBL)==0)
                		  throw new BusinessException("请先分量后在提交");
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
	 * 备注：平台编写原始脚本
	 */
	public String getCodeRemark() {
		return "	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\nrunClassCom@ \"nc.bs.pp.pp0201.BillCommit\", \"beforeCommitCheck\", \"nc.vo.pub.AggregatedValueObject:01\"@;\nObject retObj =runClassCom@ \"nc.bs.pp.pub.comstatus.HYBillCommit\", \"commitHYBill\", \"nc.vo.pub.AggregatedValueObject:01\"@;\nreturn retObj;\n";
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

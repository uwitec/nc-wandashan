package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 暂估入库保存提交
 * @author Administrator
 *
 */
public class N_WDSQ_SAVE extends AbstractCompiler2 {
	
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();

	private Hashtable m_keyHas = null;

	public N_WDSQ_SAVE() {
		super();
	}


	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
		    setParameter ( "INCURVO",vo.m_preValueVo);
		    setParameter ( "BillType", WdsWlPubConst.WDSQ);
		    setParameter ( "BillDate",getUserDate ().toString ());
		    setParameter ( "ActionName", "WRITE");
		    setParameter ( "P3",null);
		    setParameter ( "P5",null); //getUserObj ());
		    Object obj1 = runClass( "nc.bs.pub.pf.PfUtilBO", "processAction", "&ActionName:String,&BillType:String,&BillDate:String,&P3:nc.vo.pub.pf.PfUtilWorkFlowVO,&INCURVO:nc.vo.pub.AggregatedValueObject,&P5:Object",vo,m_keyHas,m_methodReturnHas);
			return obj1;
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

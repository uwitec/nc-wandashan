package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.hg.pu.mondeal.MonDealBo;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.hg.pu.plan.mondeal.MonUpdateBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.uap.pf.PFBusinessException;

public class N_HG05_WRITE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * 
 *  月需求计划调整
 *
 */
public N_HG05_WRITE() {
	super();
}
/*
* 备注：平台编写规则类
* 接口执行类
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	try {
			super.m_tmpVo = vo;
			Object retObj = null;
			AggregatedValueObject billvo = getVo();
			if(billvo ==null)
				throw new BusinessException("传入数据为空");
			MonUpdateBVO[] bvo = (MonUpdateBVO[])billvo.getChildrenVO();
			MonDealBo bo = new MonDealBo();
			int len = bvo.length;
			for(int i=0;i<len;i++){
				String cnextbillid =PuPubVO.getString_TrimZeroLenAsNull(bvo[i].getCnextbillid());
				String cnextbillbid =PuPubVO.getString_TrimZeroLenAsNull(bvo[i].getCnextbillbid());
				bo.checkUpdateMon(cnextbillid, cnextbillbid);
			}
			//只是执行了保存操作
			retObj = runClass("nc.bs.hg.pu.pub.HYBillSave", "saveBill","nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,	m_methodReturnHas);
			
			    return retObj;
		} catch (Exception ex) {
			if (ex instanceof BusinessException)
				throw (BusinessException) ex;
			else
				throw new PFBusinessException(ex.getMessage(), ex);
		}
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
public String getCodeRemark(){
	return "	try {\n			super.m_tmpVo = vo;\n			Object retObj = null;\n			// 保存即提交\n			retObj = runClass(\"nc.bs.gt.pub.HYBillSave\", \"saveBill\",\"nc.vo.pub.AggregatedValueObject:01\", vo, m_keyHas,	m_methodReturnHas);\n			return retObj;\n		} catch (Exception ex) {\n			if (ex instanceof BusinessException)\n				throw (BusinessException) ex;\n			else\n				throw new PFBusinessException(ex.getMessage(), ex);\n		}\n";}
/*
* 备注：设置脚本变量的HAS
*/
private void setParameter(String key,Object val)	{
	if (m_keyHas==null){
		m_keyHas=new Hashtable();
	}
	if (val!=null)	{
		m_keyHas.put(key,val);
	}
}
}

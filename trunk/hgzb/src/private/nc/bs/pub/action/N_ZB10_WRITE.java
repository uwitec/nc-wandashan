package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.zb.bidding.BiddingHeaderVO;
import nc.vo.zb.cust.CubasdocHgVO;
import nc.vo.zb.pub.ResultSetProcessorTool;

public class N_ZB10_WRITE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * 
 *  供应商注册保存
 *
 */
public N_ZB10_WRITE() {
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
			HYBillVO billvo = (HYBillVO)getVo();
	             if(billvo ==null)
	            	 throw new ValidationException("数据异常");
	             CubasdocHgVO head =(CubasdocHgVO)billvo.getParentVO();
	             if(head ==null)
	            	 throw new ValidationException("数据异常");
	             
			//只是执行了保存操作
			//单据号唯一性校验
	             if(PuPubVO.getString_TrimZeroLenAsNull(head.getVbillno())==null)
	            	   throw new ValidationException("供应商编码不能为空");
			String sql = "select count(0) from bd_cubasdochg where isnull(dr,0) = 0 and vbillno = '"+head.getVbillno().trim()+"' " +
					"and pk_corp = '"+SQLHelper.getCorpPk()+"' and ccubasdochgid != '"+head.getPrimaryKey()+"'";
			BaseDAO dao = new  BaseDAO();
			if(PuPubVO.getInteger_NullAs(dao.executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR), 0)>0)
				throw new BusinessException("供应商编码重复");
			
			retObj = runClass("nc.bs.zb.pub.HYBillSave", "saveBill","nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,	m_methodReturnHas);
			
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
	return null;
}
//	return "	try {\n			super.m_tmpVo = vo;\n			Object retObj = null;\n			// 保存即提交\n			retObj = runClass(\"nc.bs.gt.pub.HYBillSave\", \"saveBill\",\"nc.vo.pub.AggregatedValueObject:01\", vo, m_keyHas,	m_methodReturnHas);\n			return retObj;\n		} catch (Exception ex) {\n			if (ex instanceof BusinessException)\n				throw (BusinessException) ex;\n			else\n				throw new PFBusinessException(ex.getMessage(), ex);\n		}\n";}
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

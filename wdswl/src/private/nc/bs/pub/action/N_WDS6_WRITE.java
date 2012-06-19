package nc.bs.pub.action;
import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.wdsnew.pub.BillStockBO1;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 *  其他出库单
 * @author Administrator
 *
 */
public class N_WDS6_WRITE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
private BillStockBO1 stock=null;
private BillStockBO1 getStock(){
	if(stock==null){
		stock=new BillStockBO1();
	}
	return stock;
}

public N_WDS6_WRITE() {
	super();
}
/*
* 备注：平台编写规则类
* 接口执行类
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {

	try {
		super.m_tmpVo = vo;
		MyBillVO billVo = (MyBillVO)getVo();
		if(billVo == null){
			throw new BusinessException("传入数据为空");
		}
		billVo.setSLogUser(getOperator());
		billVo.setSLogCorp(vo.m_coId);
		billVo.setULogDate(new UFDate(System.currentTimeMillis()));
		Object retObj = null;
		retObj = runClass("nc.bo.other.out.OtherOutSave", "saveBill","nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,	m_methodReturnHas);
		//更新现存量		 
		getStock().updateStockByBill(vo.m_preValueVo, WdsWlPubConst.BILLTYPE_OTHER_OUT);
		
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

package nc.bs.pub.action;

import java.util.Hashtable;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.uap.pf.PFBusinessException;
/**
 *  发运计划录入
 * @author Administrator
 *
 */
public class N_WDS1_WRITE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;

public N_WDS1_WRITE() {
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
			setParameter("currentVo", vo.m_preValueVo);
			/**begin-------如果是月计划，则校验当前调入仓库在当前月是否已经有月计划---------begin */
			Object iplantype =vo.m_preValueVo.getParentVO().getAttributeValue("iplantype");
			if(iplantype !=null && 0==(Integer)iplantype){
				setParameter("InWhouse", vo.m_preValueVo.getParentVO().getAttributeValue("pk_inwhouse"));
				setParameter("Pk", vo.m_preValueVo.getParentVO().getAttributeValue("pk_sendplanin"));
				setParameter("Date", vo.m_currentDate);//SPF ADD
				runClass("nc.bs.wl.plan.PlanCheckinBO", "beforeCheck","&InWhouse:String,&Pk:String,&Date:String", vo, m_keyHas,	m_methodReturnHas);
			}
			//保存前的校验 追加计划保存时,如果没有月计划,追加计划不允许保存  追加计划没有存量不允许保存 
			if(iplantype!=null && 1==(Integer)iplantype){
				runClass("nc.bs.wl.plan.PlanCheckinBO","checkForBplan","&currentVo:nc.vo.pub.AggregatedValueObject",vo, m_keyHas,m_methodReturnHas);		
			}		
			/**begin-------如果是月计划，则校验当前调入仓库在当前月是否已经有月计划---------end */
			
			/**begin-------如果是追加计划，则校验当前调入仓库在当前月是否已经追加计划---------begin */
//			Object iplantype1 =vo.m_preValueVo.getParentVO().getAttributeValue("iplantype");
//			if(iplantype !=null && 1==(Integer)iplantype){
//				setParameter("InWhouse", vo.m_preValueVo.getParentVO().getAttributeValue("pk_inwhouse"));
//				setParameter("Pk", vo.m_preValueVo.getParentVO().getAttributeValue("pk_sendplanin"));
//				runClass("nc.bs.wl.plan.PlanCheckinBO", "beforeCheck1","&InWhouse:String,&Pk:String", vo, m_keyHas,	m_methodReturnHas);
//			}
			/**begin-------如果是追加计划，则校验当前调入仓库在当前月是否已经追加计划---------end */
			
			
			
			retObj = runClass("nc.bs.trade.comsave.BillSave", "saveBill","nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,	m_methodReturnHas);
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

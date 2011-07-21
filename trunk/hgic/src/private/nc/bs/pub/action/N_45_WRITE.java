package nc.bs.pub.action;

import java.util.ArrayList;
import java.util.Hashtable;

import nc.bs.hg.ic.pub.HgICPubBO;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;
/**
 * 备注：库存采购入库单的保存
单据动作执行中的动态执行类的动态执行类。
 *
 * 创建日期：(2006-4-10)
 * @author 平台脚本生成
 */
public class N_45_WRITE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_45_SAVE 构造子注解。
 */
public N_45_WRITE() {
	super();
}
/*
* 备注：平台编写规则类
* 接口执行类
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####
//*************从平台取得由该动作传入的入口参数。本方法取得需要保存的VO。***********
nc.vo.pub.AggregatedValueObject invo=(nc.vo.pub.AggregatedValueObject)getVo();
setParameter("INCURVO",invo);
Object retObj=null;
Object alLockedPK=null;
try{
//####重要说明：生成的业务组件方法尽量不要进行修改####
//方法说明:库存出入库单据加业务锁
alLockedPK=runClass("nc.bs.ic.pub.bill.ICLockBO","lockBill","&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("lockBill",retObj);
	}
//该方法<可配置>
//方法说明:检查来源单据时间戳
runClass("nc.bs.ic.pub.check.CheckDMO","checkSourceBillTimeStamp_new","&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("checkSourceBillTimeStamp_new",retObj);
	}
//##################################################
	setParameter("BillType","45");
	setParameter("BillDate",getUserDate().toString());
	setParameter("ActionName","SAVEBASE");
	setParameter("P3",null);
	setParameter("P5",getUserObj());
	retObj=runClass("nc.bs.pub.pf.PfUtilBO","processAction","&ActionName:String,&BillType:String,&BillDate:String,&P3:nc.vo.pub.pf.PfUtilWorkFlowVO,&INCURVO:nc.vo.pub.AggregatedValueObject,&P5:Object",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("processAction",retObj);
	}
	HgICPubBO bo = new HgICPubBO();
	bo.check(invo);
}catch(Exception e){
//############################
//插入业务日志，该方法可以配置
setParameter("EXC",e.getMessage());
setParameter("FUN","保存");
runClass("nc.bs.ic.pub.check.CheckBO","insertBusinessExceptionlog","&INCURVO:nc.vo.pub.AggregatedValueObject,&EXC:String,&FUN:String",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("insertBusinessExceptionlog",retObj);
	}
//###########################
     if (e instanceof BusinessException)
			throw (BusinessException) e;
		else
			throw new BusinessException("Remote Call", e);
}
finally{
//####重要说明：生成的业务组件方法尽量不要进行修改####
//方法说明:库存出入库单据解业务锁
setParameter("ALLPK",(ArrayList)alLockedPK);
if(alLockedPK!=null)
runClass("nc.bs.ic.pub.bill.ICLockBO","unlockBill","&INCURVO:nc.vo.pub.AggregatedValueObject,&ALLPK:ArrayList",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("unlockBill",retObj);
	}
//##################################################
}
return retObj;
//************************************************************************
} catch (Exception ex) {
  throw new PFBusinessException(ex.getMessage(), ex);
}
}
/*
* 备注：平台编写原始脚本
*/
public String getCodeRemark(){
	return "	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n//*************从平台取得由该动作传入的入口参数。本方法取得需要保存的VO。***********\nnc.vo.pub.AggregatedValueObject invo=(nc.vo.pub.AggregatedValueObject)getVo();\nsetParameter(\"INCURVO\",invo);\nObject retObj=null;\nObject alLockedPK=null;\ntry{\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//方法说明:库存出入库单据加业务锁\nalLockedPK=runClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"lockBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//该方法<可配置>\n//方法说明:检查来源单据时间戳\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkSourceBillTimeStamp_new\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//##################################################\n	setParameter(\"BillType\",\"45\");\n	setParameter(\"BillDate\",getUserDate().toString());\n	setParameter(\"ActionName\",\"SAVEBASE\");\n	setParameter(\"P3\",null);\n	setParameter(\"P5\",getUserObj());\n	retObj=runClassCom@\"nc.bs.pub.pf.PfUtilBO\",\"processAction\",\"&ActionName:String,&BillType:String,&BillDate:String,&P3:nc.vo.pub.pf.PfUtilWorkFlowVO,&INCURVO:nc.vo.pub.AggregatedValueObject,&P5:Object\"@;\n}catch(Exception e){\n//############################\n//插入业务日志，该方法可以配置\nsetParameter(\"EXC\",e.getMessage());\nsetParameter(\"FUN\",\"保存\");\nrunClassCom@\"nc.bs.ic.pub.check.CheckBO\",\"insertBusinessExceptionlog\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&EXC:String,&FUN:String\"@;\n//###########################\n     if (e instanceof BusinessException)\n			throw (BusinessException) e;\n		else\n			throw new BusinessException(\"Remote Call\", e);\n}\nfinally{\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//方法说明:库存出入库单据解业务锁\nsetParameter(\"ALLPK\",(ArrayList)alLockedPK);\nif(alLockedPK!=null)\nrunClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"unlockBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&ALLPK:ArrayList\"@;\n//##################################################\n}\nreturn retObj;\n//************************************************************************\n";}
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


package nc.bs.pub.action;

import java.util.Hashtable;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.wl.pub.BsBeforeSaveValudate;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.ui.wl.pub.BeforeSaveValudate;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;
/**
 *  箱数运价表
 * @author Administrator
 *
 */
public class N_WDSJ_WRITE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
public N_WDSJ_WRITE() {
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
		   //保存前的统一校验
			beforeSaveValute(vo);
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
/**
 * 
 * @作者：mlr
 * @说明：完达山物流项目 
 *       保存前的统一校验
 * @时间：2011-7-6下午07:33:03
 * @param vo
 * @throws Exception
 */
private void beforeSaveValute(PfParameterVO vo)throws Exception {
	//必输项 ：    单据号  单据类型  发货仓库  承运商  最小数  最大数 
	//  表体：    收货地区  最小距离 最大距离   运价
    BsBeforeSaveValudate.FieldNotNull(new SuperVO[]{(SuperVO) vo.m_preValueVo.getParentVO()},new String[]{"vbillno","pk_billtype","reserve1","carriersid","nmincase","nmaxcase"},new String[]{"单据号","单据类型","发货仓库","承运商","最小数","最大数"}); 
    BsBeforeSaveValudate.FieldNotNull((SuperVO[])vo.m_preValueVo.getChildrenVO(),new String[]{"pk_replace","nmindistance","nmaxdistance"},new String[]{"收获地区","最小距离","最大距离"});	
	//单据类型  编码公司级唯一  
	BsUniqueCheck.FieldUniqueCheck((SuperVO)vo.m_preValueVo.getParentVO(),new String[]{"vbillno","pk_billtype"},"数据库中存在重复的单据号");
	//发货仓库 单据类型 承运商   这个维度下 不能出现    最小数 到  最大数  之间的交叉
	BsUniqueCheck.FieldUniqueCheckInment((SuperVO)vo.m_preValueVo.getParentVO(),new String[]{"reserve1", "pk_billtype","carriersid"},"nmincase","nmaxcase"," 已经定义 了该 [发货仓库]  [承运商] 下的这个箱数区间段的运价表 ");		
	//校验 收获地区相同的情况下   在最小距离和最大距离不能出现交叉
	BsBeforeSaveValudate.beforeSaveBodyUniqueInmet((SuperVO[])vo.m_preValueVo.getChildrenVO(),new String[]{"pk_replace"}, "nmindistance", "nmaxdistance",new String[]{"收获地区"}, "最小距离","最大距离");
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

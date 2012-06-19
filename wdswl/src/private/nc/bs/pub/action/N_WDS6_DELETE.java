package nc.bs.pub.action;
import java.util.Hashtable;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.wdsnew.pub.BillStockBO1;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * @author mlr
 */
public class N_WDS6_DELETE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;
	private BillStockBO1 stock=null;
	private BillStockBO1 getStock(){
		if(stock==null){
			stock=new BillStockBO1();
		}
		return stock;
	}

	public N_WDS6_DELETE() {
		super();
	}

	/*
	 * 备注：平台编写规则类 接口执行类
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			// ####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####
			Object retObj = null;
			// 方法说明:行业公共删除
			
			MyBillVO billVo = (MyBillVO)getVo();
			if(billVo == null){
				throw new BusinessException("传入数据为空");
			}
			//更新现存量		 
			getStock().updateStockByBill(billVo, WdsWlPubConst.BILLTYPE_OTHER_OUT_1);

//			MyBillVO billVo2 = (MyBillVO)nc.ui.scm.util.ObjectUtils.serializableClone(billVo);
//			
//			//删除后需要处理的逻辑
//			/**
//			 * 删除单据
//			 * 删除存量缓存表
//			 * 回撤托盘存量表   
//			 *  
//			 */			
//			retObj = runClass("nc.bs.trade.comdelete.BillDelete", "deleteBill",
//					"nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,
//					m_methodReturnHas);
//			OtherOutBO bo = new OtherOutBO();
//			bo.deleteOutBill(billVo2);
			
			// ##################################################
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
		return "	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\nObject retObj  =null;\n//方法说明:行业公共删除\nretObj  =runClassCom@ \"nc.bs.trade.comdelete.BillDelete\", \"deleteBill\", \"nc.vo.pub.AggregatedValueObject:01\"@;\n//##################################################\nreturn retObj;\n";
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

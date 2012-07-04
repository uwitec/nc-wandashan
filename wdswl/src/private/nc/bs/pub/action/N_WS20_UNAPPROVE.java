package nc.bs.pub.action;


import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.wdsnew.pub.BillStockBO1;
import nc.vo.wl.pub.Wds2WlPubConst;

/**
 *  状态变更
 * @author Administrator
 *
 */
public class N_WS20_UNAPPROVE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;

	public N_WS20_UNAPPROVE() {
		super();
	}
	private BillStockBO1 stock=null;
	private BillStockBO1 getStock(){
		if(stock==null){
			stock=new BillStockBO1();
		}
		return stock;
	}

	/*
	 * 备注：平台编写规则类 接口执行类
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			// ####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####
			procUnApproveFlow(vo);
			
			//更新现存量
			getStock().updateStockByBill(vo.m_preValueVo, Wds2WlPubConst.billtype_statusupdate_2);//处理状态变更单删除  状态变化前减少量
			getStock().updateStockByBill(vo.m_preValueVo, Wds2WlPubConst.billtype_statusupdate_3);//处理状态变更单删除  状态变化后 新增量
			
			Object retObj = runClass("nc.bs.wl.pub.HYBillUnApprove",
					"unApproveHYBill", "nc.vo.pub.AggregatedValueObject:01",
					vo, m_keyHas, m_methodReturnHas);
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
		return "	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n procUnApproveFlow (vo); \nObject retObj=runClassCom@ \"nc.bs.pp.pub.comstatus.HYBillUnApprove\", \"unApproveHYBill\", \"nc.vo.pub.AggregatedValueObject:01\"@;\nreturn retObj;\n";
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

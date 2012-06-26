package nc.bs.pub.action;

import java.util.Hashtable;
import nc.bs.ic.pub.IcInPubBO;
import nc.bs.ic.pub.WriteBackTool;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;


/**
 *  退货入库
 * @author Administrator
 *
 */
public class N_WDSZ_DELETE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;


	public N_WDSZ_DELETE() {
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
			AggregatedValueObject  bill = getVo();
			if(bill == null || bill.getParentVO() == null){
				throw new BusinessException("传入数据为空");
			}
			
			TbGeneralBVO[] bodys = (TbGeneralBVO[])bill.getChildrenVO();
			if(bodys == null || bodys.length ==0){
				throw new BusinessException("传入数据为空");
			}
			
			// ##################################################
			IcInPubBO bo = new IcInPubBO();
//			bo.deleteAdjustBill((OtherInBillVO)bill);
			
//			修订来源单据累计量
			for(TbGeneralBVO body:bodys){
				body.setStatus(VOStatus.DELETED);
			}
			WriteBackTool.setVsourcebillrowid("cfirstbillbid");
			WriteBackTool.setVsourcebillid("cfirstbillhid");
			WriteBackTool.setVsourcebilltype("cfirsttype");
			WriteBackTool.writeBack(bodys, "so_saleorder_b", "corder_bid", new String[]{"geb_anum"}, new String[]{"ntaldcnum"},new String[]{"nnumber"});
//			修订结束
			
			// ##################################################
			retObj = runClass("nc.bs.trade.comdelete.BillDelete", "deleteBill",
					"nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,
					m_methodReturnHas);// 方法说明:行业公共删除
			// ##################################################
		//	bo.deleteOtherInforOnDelBill(bill.getParentVO().getPrimaryKey(),bodys);//回写托盘，删除孙表
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

package nc.bs.pub.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.zb.bidding.make.MakeBiddingBO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.zb.bidding.BiddingBillVO;
import nc.vo.zb.bidding.BiddingBodyVO;


/**
 * 制作标书作废
 * @author Administrator
 *
 */
public class N_ZB01_DELETE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;


	public N_ZB01_DELETE() {
		super();
	}

	/*
	 * 备注：平台编写规则类 接口执行类
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			
			BiddingBillVO bill = (BiddingBillVO)getVo();
			if(bill == null || bill.getChildrenVO()==null)
				throw new BusinessException("数据异常");
			BiddingBodyVO[] bodys = (BiddingBodyVO[])bill.getChildrenVO();
			if(bodys ==null ||bodys.length == 0)
				throw new BusinessException("数据为空");
			// ####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####
			Object retObj = null;
			
//			MakeBiddingBO bo = new MakeBiddingBO();
//			BiddingBillVO bill = (BiddingBillVO)getVo();
//			if(bill.getHeader().getFisself().booleanValue())
//				return retObj;
			
			if(!PuPubVO.getUFBoolean_NullAs(bill.getHeader().getFisself().booleanValue(),UFBoolean.FALSE).booleanValue()){
				List<String> lids = new ArrayList<String>();
				for(BiddingBodyVO body:bodys){
					if(PuPubVO.getString_TrimZeroLenAsNull(body.getCupsourcebilltype())==null||!PuPubVO.getString_TrimZeroLenAsNull(body.getCupsourcebilltype()).equalsIgnoreCase(ScmConst.PO_Pray))
						continue;
					if(PuPubVO.getString_TrimZeroLenAsNull(body.getCupsourcebillrowid())==null)
						continue;
					
					if(lids.contains(body.getCupsourcebillrowid()))
						continue;
					lids.add(body.getPrimaryKey());
				}
				if(lids.size()>0){
					MakeBiddingBO bo = new MakeBiddingBO();
					bo.reWritePONumOnDel(lids.toArray(new String[0]),null,false);
				}		
			}
			
			// 方法说明:行业公共删除
			retObj = runClass("nc.bs.trade.comdelete.BillDelete", "deleteBill",
					"nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,
					m_methodReturnHas);
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

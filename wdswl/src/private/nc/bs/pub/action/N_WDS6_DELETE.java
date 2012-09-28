package nc.bs.pub.action;
import java.util.Hashtable;
import java.util.List;

import nc.bo.other.out.OtherOutBO;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.dm.order.SendorderVO;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.wdsnew.pub.BillStockBO1;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * @author mlr
 */
public class N_WDS6_DELETE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;
	private BillStockBO1 stock=null;
	private BaseDAO dao=new BaseDAO();
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
			valueOrder(billVo);
			if(billVo == null){
				throw new BusinessException("传入数据为空");
			}
			//更新现存量		 
			getStock().updateStockByBill(billVo, WdsWlPubConst.BILLTYPE_OTHER_OUT_1);
		    //进行数据会写
			OtherOutBO bo = new OtherOutBO();
			bo.writeBack(billVo, IBDACTION.DELETE);
			//进行单据的删除操作
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
    /**
     * 出库单作废 校验 上游的运单是否已经冻结
     * @作者：mlr
     * @说明：完达山物流项目 
     * @时间：2012-9-22上午11:02:29
     * @param billVo
     * @throws Exception 
     */
	private void valueOrder(MyBillVO billVo) throws Exception {
		
	 if(billVo==null || billVo.getChildrenVO()==null || billVo.getAllChildrenVO().length==0)
		 return;
	  SuperVO vo=(SuperVO) billVo.getChildrenVO()[0];
	  String csid=PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue("csourcebillhid"));
	  if(csid==null){
		  return;
	  }
	  //查询发运订单看是否已经冻结
	 List list=  (List) dao.retrieveByClause(SendorderVO.class, " isnull(dr,0)=0 and pk_sendorder='"+csid+"'");
	 if(list==null || list.size()==0)
		 return;
	 SendorderVO head=(SendorderVO) list.get(0);
	 UFBoolean isdj=PuPubVO.getUFBoolean_NullAs(head.getFisended(), new UFBoolean(false));
	 if(isdj.booleanValue()==true){
		 throw new Exception("上游运单已经冻结 ,不能作废");
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

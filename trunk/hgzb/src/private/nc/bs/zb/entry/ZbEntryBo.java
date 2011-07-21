package nc.bs.zb.entry;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.zb.pub.ZbBsPubTool;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.entry.ZbResultBodyVO;
import nc.vo.zb.entry.ZbResultHeadVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class ZbEntryBo {
	
	private BaseDAO dao = null;

	private BaseDAO getBaseDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	
	//  获取税率
	public Object getnOrderTaxRate(String custbasid) throws DAOException{
		
	
		if(custbasid==null)
			return null;
		
		String sql = " select def1 from bd_cubasdoc where pk_cubasdoc ='"+custbasid+"' and isnull(dr,0)=0";
		Object object = (Object)getBaseDao().executeQuery(sql, ZbBsPubTool.COLUMNPROCESSOR);
		return object;
	}
	
	//获取采购组织
	public Object getpk_calbody(String ccorpid) throws DAOException{
		String sql = "select pk_purorg from bd_purorg  where ownercorp ='"+ccorpid+"' and isnull(dr,0)=0";
		Object o = getBaseDao().executeQuery(sql, ZbBsPubTool.COLUMNPROCESSOR);
		return o;
	
	}
	
	//是否存在下游单据
	public void isExitDownBill (ZbResultBodyVO bodyvo) throws BusinessException{
		String sql = " select count(0) from  po_order_b b join po_order r on r.corderid = b.corderid " +
				     " where b.cupsourcebillrowid = '"+bodyvo.getPrimaryKey()+"' and isnull(b.dr,0)=0 and isnull(r.dr,0)=0 ";
		Object o = getBaseDao().executeQuery(sql, new ColumnProcessor());
		if(PuPubVO.getInteger_NullAs(o, ZbPubConst.IZBRESULTTYPE).intValue()>0)
			throw new BusinessException("存在着已经下游采购合同");
	
	}
	
	//是否存在下游单据
	public void isExitDownBillApprove (ZbResultHeadVO headvo) throws BusinessException{
		String sql = " select count(0) from  po_order_b b join po_order r on r.corderid = b.corderid " +
				     " where b.cupsourcebillid = '"+headvo.getPrimaryKey()+"' and isnull(b.dr,0)=0 and isnull(r.dr,0)=0";
		Object o = getBaseDao().executeQuery(sql, new ColumnProcessor());
		if(PuPubVO.getInteger_NullAs(o, ZbPubConst.IZBRESULTTYPE).intValue()>0)
			throw new BusinessException("存在着已经下游采购合同");
	
	}
	
	//临时供应商是否存在着正式供应商ID
	public Object isExitCcustid (ZbResultHeadVO headvo) throws BusinessException{
		String sql = "select count(0) from  bd_cumandoc where pk_cumandoc = '"+headvo.getCcustmanid()+"' and isnull(dr,0)=0 ";
		String sql1 = "select ccustbasid,ccustmanid from  bd_cubasdochg where ccubasdochgid = '"+headvo.getCcustmanid()+"' and isnull(dr,0)=0 and ccustbasid is not  null and ccustmanid is not null";
		Object o = getBaseDao().executeQuery(sql,ZbBsPubTool.COLUMNPROCESSOR);
		Object o1 = getBaseDao().executeQuery(sql1,ZbBsPubTool.ARRAYPROCESSOR);
		Object[] os = null;
		if(o1!=null)
			os = (Object[])o1;
		if(PuPubVO.getInteger_NullAs(o,new Integer(-1)).intValue()==0 &&(os==null || os.length==0))
			throw new BusinessException("临时供应商对应的正式供应商不存在");
		return os;
	}
	
//	public void isComplete(String[] als) throws BusinessException{
//		String sql ="select count(0) from zb_result_b b where b.czbresultbid in "+ZbPubTool.getSubSql(als)+" and isnull(b.dr,0)=0 and coalesce(b.reserve10,0)>0";
//		Object o = getBaseDao().executeQuery(sql,ZbBsPubTool.COLUMNPROCESSOR);
//		
//		if(PuPubVO.getInteger_NullAs(o, ZbPubConst.IZBRESULTTYPE).intValue()>0)
//			throw new BusinessException("已经生成合同");
//		
//	}
	
	public void isComplete(String str) throws BusinessException{
		String sql ="select count(0) from po_order_b b where b.cupsourcebillid  = '"+str+"' and isnull(dr,0)=0 ";
		Object o = getBaseDao().executeQuery(sql,new ColumnProcessor());
		
		if(PuPubVO.getInteger_NullAs(o, new Integer(-1)).intValue()>0)
			throw new BusinessException("已经生成合同");
		
	}
}

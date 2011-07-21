package nc.bs.pp.ask;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;

public class HgPriceAuditBO {
	
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	public void isComplete(String str) throws BusinessException{
		String sql ="select count(0) from po_order_b b where b.cpriceauditid  = '"+str+"' and isnull(dr,0)=0 ";
		Object o = getDao().executeQuery(sql,new ColumnProcessor());
		
		if(PuPubVO.getInteger_NullAs(o, new Integer(-1)).intValue()>0)
			throw new BusinessException("已经生成合同");
		
	}
}

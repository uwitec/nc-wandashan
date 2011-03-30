package nc.bs.hg.pu.invoice;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.vo.hg.pu.invoice.BzbVO;
import nc.vo.hg.pu.invoice.BzhVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;

public class InvoiceBo {
	
	private BaseDAO dao = null;
	private BaseDAO getBaseDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	public void updateVdef20(AggregatedValueObject vo) throws DAOException{
		BzhVO  hvo = (BzhVO)vo.getParentVO();
		BzbVO[] bvos=(BzbVO[])vo.getChildrenVO();
		for(BzbVO bvo:bvos){
			String sql =" update po_invoice set vdef20 = '"+hvo.getVbillno()+"' where cinvoiceid = '"+bvo.getCsourcebillhid()+"'";
			getBaseDao().executeUpdate(sql);
		}
		
	}
	
	public void checkVdef20(AggregatedValueObject vo) throws BusinessException{
		BzhVO  hvo = (BzhVO)vo.getParentVO();
			String  query =" select count(*) from po_invoice where vdef20 = '"+hvo.getVbillno()+"' and ibillstatus = 3 and isnull(dr,0)=0 ";
			String len = PuPubVO.getString_TrimZeroLenAsNull( getBaseDao().executeQuery(query, HgBsPubTool.COLUMNPROCESSOR));
			
			if(Integer.parseInt(len)>0){
				throw new BusinessException("该发票已经存在审批");
			}
			BzbVO[] bvos=(BzbVO[])vo.getChildrenVO();
			for(BzbVO bvo:bvos){
				String sql =" update po_invoice set vdef20 = '' where cinvoiceid = '"+bvo.getCsourcebillhid()+"'";
				getBaseDao().executeUpdate(sql);
			}
		
	}

}

package nc.bs.hg.pu.invoice;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.vo.hg.pu.invoice.BzbVO;
import nc.vo.hg.pu.invoice.BzhVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public class InvoiceBo {

	private BaseDAO dao = null;

	private BaseDAO getBaseDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	public void updateVdef20(AggregatedValueObject vo) throws DAOException {
		BzhVO hvo = (BzhVO) vo.getParentVO();
		BzbVO[] bvos = (BzbVO[]) vo.getChildrenVO();
		for (BzbVO bvo : bvos) {
			String sql = " update po_invoice set vdef20 = '" + hvo.getVbillno()
					+ "' where cinvoiceid = '" + bvo.getCsourcebillhid() + "'";
			getBaseDao().executeUpdate(sql);
		}

	}

	public void checkVdef20(AggregatedValueObject vo) throws BusinessException {
		BzhVO hvo = (BzhVO) vo.getParentVO();
		String query = " select count(*) from po_invoice where vdef20 = '"
				+ hvo.getVbillno()
				+ "' and ibillstatus = 3 and isnull(dr,0)=0 ";
		String len = PuPubVO.getString_TrimZeroLenAsNull(getBaseDao()
				.executeQuery(query, HgBsPubTool.COLUMNPROCESSOR));

		if (Integer.parseInt(len) > 0) {
			throw new BusinessException("该发票已经存在审批");
		}
		BzbVO[] bvos = (BzbVO[]) vo.getChildrenVO();
		for (BzbVO bvo : bvos) {
			String sql = " update po_invoice set vdef20 = '' where cinvoiceid = '"
					+ bvo.getCsourcebillhid() + "'";
			getBaseDao().executeUpdate(sql);
		}

	}

	public UFDouble getThreeParameters(String ccustmanid,String corp) throws DAOException {
		String sql1 = " select sum(f.dfybje) from arap_djzb z join arap_djfb f on z.vouchid = f.vouchid " +
				     " where z.djlxbm in ('D1','2348') and isnull(z.dr, 0) = 0 and isnull(f.dr, 0) = 0 " +
				     " and f.jsfsbm ='25'  and z.dwbm='"+corp+"' and z.zgyf=0 and f.hbbm= '"+ccustmanid+"' ";
		UFDouble d1 =PuPubVO.getUFDouble_NullAsZero(getBaseDao().executeQuery(sql1, HgBsPubTool.COLUMNPROCESSOR));
		
		String sql2 = " select sum(z.bbje) from arap_djzb z join arap_djfb f on z.vouchid = f.vouchid " +
	                  " where z.djlxbm in ('D3','2346') and isnull(z.dr, 0) = 0 and isnull(f.dr, 0) = 0 " +
	                  " and z.dwbm='"+corp+"' and z.prepay = 'Y' and f.hbbm= '"+ccustmanid+"' ";
        UFDouble d2 =PuPubVO.getUFDouble_NullAsZero(getBaseDao().executeQuery(sql2, HgBsPubTool.COLUMNPROCESSOR));
		 
        UFDouble d3 = UFDouble.ZERO_DBL;
        
        if(d2.sub(d1).compareTo(d3)>0){
        	d3 =d2.sub(d1);
        }
		return d3;
	}
}

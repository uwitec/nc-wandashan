package nc.bs.wds.ic.inv;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.pub.IBDACTION;
/**
 * 
 * 基本档案 后台校验类 

 * author:mlr
 * */

public class BSCheck implements IBDBusiCheck {
	private BaseDAO dao;

	private BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
			throws Exception {
		if (intBdAction != IBDACTION.SAVE) {
			return;
		}
		if (vo == null || vo.getParentVO() == null) {
			return;
		}
		StockInvOnHandVO sk=(StockInvOnHandVO) vo.getParentVO();
		String sql=" update tb_warehousestock set ss_pk='"+sk.getSs_pk()+"' where " +
				" pk_cargdoc='"+sk.getPk_cargdoc()+"' " +
				" and pk_invbasdoc='"+sk.getPk_invbasdoc()+"'" +
				" and pk_invmandoc='"+sk.getPk_invmandoc()+"'" +
				" and whs_batchcode='"+sk.getWhs_batchcode()+"'";
        getDao().executeUpdate(sql);
		
	}
	
	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		
	}

}

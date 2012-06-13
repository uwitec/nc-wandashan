package nc.bs.wds.ic.inv;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
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
		StockInvOnHandVO stock=(StockInvOnHandVO) vo.getParentVO();
		String ID = PuPubVO.getString_TrimZeroLenAsNull(stock.getPrimaryKey());
		if(ID==null)
			return;//不支持新增时的调整
		
//		zhf   调整 判断  库存状态 
/**
 * 		1、若是正常状态向正常状态调整    同一个批次同一状态全部调整
 *      2、非正常状态向正常状态调整   向非正常状态调整   均  只调整当前托盘  不影响 同批次 其他托盘状态
 *      3、正常状态向分正常状态调整   同2
 */
		
//		取出当前库存状态  判断是否正常态
		String state = PuPubVO.getString_TrimZeroLenAsNull(stock.getSs_pk());
		if(state == null)
			throw new BusinessException("库存状态为空");
		boolean isok = getIsOk(state);
		if(!isok)
			return;
		
		String sql = "select ss_pk from tb_warehousestock where whs_pk = '"+ID+"'";
		
		String oldstate = PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
		if(oldstate == null){
			throw new BusinessException("数据异常，原库存状态为空");
		}
//		
		boolean isoldok = getIsOk(oldstate);
		if(!isoldok)
			return;		
		
		sql=" update tb_warehousestock set ss_pk='"+stock.getSs_pk()+"' where " +
				" pk_cargdoc='"+stock.getPk_cargdoc()+"' " +
				" and pk_invbasdoc='"+stock.getPk_invbasdoc()+"'" +
				" and pk_invmandoc='"+stock.getPk_invmandoc()+"'" +
				" and whs_batchcode='"+stock.getWhs_batchcode()+"'" +
						" and ss_pk = '"+oldstate+"'";
        getDao().executeUpdate(sql);
		
	}
	
	private boolean getIsOk(String ss_pk) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(ss_pk)==null)
			throw new BusinessException("传入参数为空");
		String sql = "select isok from tb_stockstate where isnull(dr,0) = 0 and ss_pk = '"+ss_pk+"'";
		return PuPubVO.getUFBoolean_NullAs(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR), UFBoolean.FALSE).booleanValue();
	}
	
	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		
	}

}

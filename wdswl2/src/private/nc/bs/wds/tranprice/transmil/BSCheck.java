package nc.bs.wds.tranprice.transmil;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
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
		if (vo == null || vo.getChildrenVO() == null) {
			return;
		}		
		//收发货地点唯一性校验
		BsUniqueCheck.FieldUniqueCheck((SuperVO[])(vo.getChildrenVO()),new String[]{"pk_relocation","pk_delocation"}, "收发货地点必须唯一");
		
	}
	
	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		
	}

}

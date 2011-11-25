package nc.bs.wds.tranprice.freight;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.tranprice.freight.ZhbzBVO;

public class ZhbzBSCheck implements IBDBusiCheck {
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

		// 判断是修改后的保存还是新增后的保存
		ZhbzBVO ivo = (ZhbzBVO) vo.getParentVO();
		//更换 唯一校验
		BsUniqueCheck.FieldUniqueCheck(ivo, new String[]{"pk_invbasdoc","pk_corp"}, " 该存货在数据库中已经存在 ");
		BsUniqueCheck.FieldUniqueCheck(ivo, new String[]{"irownumber","pk_corp"}, " 该行号在数据库中已经存在 ");
	}
	
	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		
	}

}

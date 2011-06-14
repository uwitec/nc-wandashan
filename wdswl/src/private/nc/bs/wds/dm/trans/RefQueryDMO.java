package nc.bs.wds.dm.trans;

import nc.bs.wl.pub.HYPubDMOWithQuery;
import nc.vo.wds.dm.trans.FydBVO;
import nc.vo.wds.dm.trans.FydHVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 运单确认对应的（WDS5）后台参照查询类
 *
 */
public class RefQueryDMO extends HYPubDMOWithQuery{

	@Override
	protected String getBillType() {
		return WdsWlPubConst.BILLTYPE_SEND_CONFIRM;
	}

	@Override
	protected Class getBodyVOClass() {
		return FydBVO.class;
	}

	@Override
	protected Class getHeadVOClass() {
		return FydHVO.class;
	}
}

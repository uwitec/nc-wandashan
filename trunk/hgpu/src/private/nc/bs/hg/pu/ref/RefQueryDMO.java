package nc.bs.hg.pu.ref;

import nc.bs.hg.pu.pub.HYPubDMOWithQuery;
import nc.vo.hg.pu.invoice.BzbVO;
import nc.vo.hg.pu.invoice.BzhVO;
import nc.vo.hg.pu.pub.HgPubConst;

public class RefQueryDMO extends HYPubDMOWithQuery{

	@Override
	protected String getBillType() {
		return HgPubConst.PLAN_BAOZHANG_BILLTYPE;
	}

	@Override
	protected Class getBodyVOClass() {
		return BzbVO.class;
	}

	@Override
	protected Class getHeadVOClass() {
		return BzhVO.class;
	}
}

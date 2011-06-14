package nc.bs.wds.ic.allocation.in;

import nc.bs.wl.pub.HYPubDMOWithQuery;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class RefQueryDMO extends HYPubDMOWithQuery {

	@Override
	protected String getBillType() {
		// TODO Auto-generated method stub
		return  WdsWlPubConst.BILLTYPE_ALLO_IN;
	}

	@Override
	protected Class getBodyVOClass() {
		// TODO Auto-generated method stub
		return TbGeneralBVO.class;
	}

	@Override
	protected Class getHeadVOClass() {
		// TODO Auto-generated method stub
		return TbGeneralHVO.class;
	}

}

package nc.bs.wds.ic.so.out;

import nc.bs.wl.pub.HYPubDMOWithQuery;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class RefQueryDMO extends HYPubDMOWithQuery {

	@Override
	protected String getBillType() {
		// TODO Auto-generated method stub
		return WdsWlPubConst.BILLTYPE_SALE_OUT;
	}

	@Override
	protected Class getBodyVOClass() {
		// TODO Auto-generated method stub
		return TbOutgeneralBVO.class;
	}

	@Override
	protected Class getHeadVOClass() {
		// TODO Auto-generated method stub
		return TbOutgeneralHVO.class;
	}

}

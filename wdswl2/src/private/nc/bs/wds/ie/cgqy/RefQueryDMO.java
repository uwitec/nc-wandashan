package nc.bs.wds.ie.cgqy;

import nc.bs.wl.pub.HYPubDMOWithQuery;
import nc.vo.wds.ie.cgqy.CgqyBVO;
import nc.vo.wds.ie.cgqy.CgqyHVO;
import nc.vo.wl.pub.WdsWlPubConst;

/*
 * 参照采购取样查询DMO
 */
public class RefQueryDMO extends HYPubDMOWithQuery {

	@Override
	protected String getBillType() {
		// TODO Auto-generated method stub
		return WdsWlPubConst.WDSC;
	}

	@Override
	protected Class getBodyVOClass() {
		// TODO Auto-generated method stub
		return CgqyBVO.class;
	}

	@Override
	protected Class getHeadVOClass() {
		// TODO Auto-generated method stub
		return CgqyHVO.class;
	}

}

package nc.bs.wds.transfer;

import nc.bs.wl.pub.HYPubDMOWithQuery;
import nc.vo.wds.transfer.TransferBVO;
import nc.vo.wds.transfer.TransferVO;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 
 * @author Administrator
 *参照发运订单查询DMO
 */
public class RefQueryDMO extends HYPubDMOWithQuery {

	@Override
	protected String getBillType() {
		// TODO Auto-generated method stub
		return WdsWlPubConst.HWTZ;
	}

	@Override
	protected Class getBodyVOClass() {
		return  TransferBVO.class;
	}

	@Override
	protected Class getHeadVOClass() {
		return TransferVO.class;
	}

}

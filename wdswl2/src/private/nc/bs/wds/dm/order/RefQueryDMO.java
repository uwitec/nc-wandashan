package nc.bs.wds.dm.order;

import nc.bs.wl.pub.HYPubDMOWithQuery;
import nc.vo.dm.order.SendorderBVO;
import nc.vo.dm.order.SendorderVO;
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
		return WdsWlPubConst.WDS3;
	}

	@Override
	protected Class getBodyVOClass() {
		// TODO Auto-generated method stub
		return  SendorderBVO.class;
	}

	@Override
	protected Class getHeadVOClass() {
		// TODO Auto-generated method stub
		return SendorderVO.class;
	}

}

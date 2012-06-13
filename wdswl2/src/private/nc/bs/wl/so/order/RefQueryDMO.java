package nc.bs.wl.so.order;

import nc.bs.wl.pub.HYPubDMOWithQuery;
import nc.vo.dm.so.order.SoorderBVO;
import nc.vo.dm.so.order.SoorderVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * �����˵���WDS5����̨���ղ�ѯ��
 *
 */
public class RefQueryDMO extends HYPubDMOWithQuery{

	@Override
	protected String getBillType() {
		return WdsWlPubConst.WDS5;
	}

	@Override
	protected Class getBodyVOClass() {
		return SoorderBVO.class;
	}

	@Override
	protected Class getHeadVOClass() {
		return SoorderVO.class;
	}
}

package nc.ui.dm.plan;

import java.awt.Container;

import nc.ui.dm.PlanDealHealper;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.dm.PlanDealVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientBusinessDelegator extends BusinessDelegator {


	private Container parent = null;

	public ClientBusinessDelegator(Container parent ){
		this.parent = parent;
	}

	@Override
	public SuperVO[] queryHeadAllData(Class headClass, String strBillType,
			String strWhere) throws Exception {
		// TODO Auto-generated method stub
		return super.queryHeadAllData(headClass, strBillType, strWhere);
	}

}

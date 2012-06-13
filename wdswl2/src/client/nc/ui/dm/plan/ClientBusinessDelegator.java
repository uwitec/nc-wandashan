package nc.ui.dm.plan;

import java.awt.Container;

import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.vo.pub.SuperVO;

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

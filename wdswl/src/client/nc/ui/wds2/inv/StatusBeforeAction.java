package nc.ui.wds2.inv;

import java.awt.Container;

import nc.ui.pub.pf.IUIBeforeProcAction;
import nc.ui.trade.businessaction.IPFACTION;
import nc.vo.pub.AggregatedValueObject;

public class StatusBeforeAction implements IUIBeforeProcAction {

	public void runBatchClass(Container parent, String billType,
			String actionName, AggregatedValueObject[] vos, Object[] obj)
			throws Exception {
		// TODO Auto-generated method stub

	}

	public void runClass(Container parent, String billType, String actionName,
			AggregatedValueObject vo, Object obj) throws Exception {
		// TODO Auto-generated method stub

		if(actionName.equalsIgnoreCase(IPFACTION.COMMIT)){
//			保存前数据校验  表体：存货 批次 调整前状态  调整后状态  不能重复  调整数量不能为0
		}
	}

}

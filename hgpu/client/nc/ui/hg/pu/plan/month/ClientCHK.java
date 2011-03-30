package nc.ui.hg.pu.plan.month;

import java.awt.Container;
import nc.ui.trade.businessaction.IPFACTION;
import nc.ui.trade.check.BeforeActionCHK;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.checkrule.VOChecker;


public class ClientCHK extends BeforeActionCHK{

	public void runBatchClass(Container parent, String billType,
			String actionName, AggregatedValueObject[] vos, Object[] obj)
			throws Exception {
	}

	public void runClass(Container parent, String billType, String actionName,
			AggregatedValueObject vo, Object obj) throws Exception {
		if (IPFACTION.SAVE.equalsIgnoreCase(actionName)
				&& !VOChecker.check(vo, ClientCheckRules.getInstance())){
				throw new nc.vo.pub.BusinessException(VOChecker.getErrorMessage());
		}
	}

}

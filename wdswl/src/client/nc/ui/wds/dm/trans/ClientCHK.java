package nc.ui.wds.dm.trans;

import java.awt.Container;
import nc.ui.trade.businessaction.IPFACTION;
import nc.ui.trade.check.BeforeActionCHK;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.checkrule.VOChecker;

public class ClientCHK extends BeforeActionCHK {

	public ClientCHK() {
	}

	public void runBatchClass(Container parent, String billType,
			String actionName, AggregatedValueObject[] vos, Object[] obj)
			throws Exception {

	}

	public void runClass(Container parent, String billType, String actionName,
			AggregatedValueObject vo, Object obj) throws Exception {
		
		if (IPFACTION.COMMIT.equals(actionName) && !VOChecker.check(vo, new ClientCheckRules()))
			throw new nc.vo.pub.BusinessException(VOChecker.getErrorMessage());

		VOChecker.checkBack(vo, ClientCheckRules.getInstance());

	}

}

package nc.ui.zb.entry;

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
		//因为本单据保存即提交，所以这里使用commit
		if (IPFACTION.SAVE.equalsIgnoreCase(actionName)
				&& !VOChecker.check(vo, ClientCheckRules.getInstance())){
				throw new nc.vo.pub.BusinessException(VOChecker.getErrorMessage());
		}
	}

}

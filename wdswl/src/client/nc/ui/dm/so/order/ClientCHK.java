package nc.ui.dm.so.order;

import java.awt.Container;

import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.businessaction.IPFACTION;
import nc.ui.trade.check.BeforeActionCHK;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
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

		if (IPFACTION.COMMIT.equals(actionName)
				&& !VOChecker.check(vo, new ClientCheckRules()))
			throw new nc.vo.pub.BusinessException(VOChecker.getErrorMessage());

		VOChecker.checkBack(vo, ClientCheckRules.getInstance());
		// add by yf 2012-07-27 销售运单 保存时校验 如果销售运单 自提标志位reserve16 为true 时 校验 承运商
		// 的是否自提标志位 必须是true
		CircularlyAccessibleValueObject head = vo.getParentVO();
		if (head == null) {
			return;
		}
		UFBoolean iszt = PuPubVO.getUFBoolean_NullAs(head
				.getAttributeValue("reserve16"), UFBoolean.FALSE);
		if (iszt.booleanValue()) {
			String pk_transcorp = PuPubVO.getString_TrimZeroLenAsNull(head
					.getAttributeValue("pk_transcorp"));
			checkTranscorp(pk_transcorp, true);
		}

	}

	private void checkTranscorp(String pk_transcorp, boolean b) throws BusinessException {
		Object o = HYPubBO_Client.findColValue("wds_tanscorp_h", "reserve16",
				"pk_wds_tanscorp_h = '" + pk_transcorp + "'");
		if(PuPubVO.getUFBoolean_NullAs(o, UFBoolean.FALSE).booleanValue()){
			throw new BusinessException("承运商不符合自提标准");
		}
	}

}

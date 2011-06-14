package nc.ui.wds.ic.other.in;

import nc.ui.wds.ic.pub.WdsIcPubHelper;


/**
  *
  *抽象业务代理类的缺省实现
  *@author author
  *@version tempProject version
  */
public class OtherInDelegator extends nc.ui.trade.bsdelegate.BusinessDelegator{

	/**
	 * queryBodyVOs 方法注解。
	 */
	public nc.vo.pub.CircularlyAccessibleValueObject[] queryBodyAllData(
			Class voClass, String billType, String key, String strWhere)
			throws Exception {
		return WdsIcPubHelper.queryIcInBillBodyAllData(key, strWhere);
	}
}
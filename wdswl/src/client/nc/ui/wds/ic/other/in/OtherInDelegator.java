package nc.ui.wds.ic.other.in;

import nc.ui.wds.ic.pub.WdsIcPubHelper;


/**
  *
  *����ҵ��������ȱʡʵ��
  *@author author
  *@version tempProject version
  */
public class OtherInDelegator extends nc.ui.trade.bsdelegate.BusinessDelegator{

	/**
	 * queryBodyVOs ����ע�⡣
	 */
	public nc.vo.pub.CircularlyAccessibleValueObject[] queryBodyAllData(
			Class voClass, String billType, String key, String strWhere)
			throws Exception {
		return WdsIcPubHelper.queryIcInBillBodyAllData(key, strWhere);
	}
}
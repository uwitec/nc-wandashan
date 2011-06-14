package nc.ui.wds.ic.so.out;

import nc.ui.wl.pub.LongTimeTask;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;


/**
  *
  *抽象业务代理类的缺省实现
  *@author author
  *@version tempProject version
  */
public class MyDelegator extends nc.ui.trade.bsdelegate.BusinessDelegator{

	/**
	 * queryBodyVOs 方法注解。
	 */
	public nc.vo.pub.CircularlyAccessibleValueObject[] queryBodyAllData(
			Class voClass, String billType, String key, String strWhere)
	throws Exception {
		if(PuPubVO.getString_TrimZeroLenAsNull(strWhere)==null)
			strWhere = " general_pk = '"+key+"'";
		else
			strWhere = strWhere +=" and general_pk = '"+key+"'";
		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{strWhere};
		SuperVO[] os = (SuperVO[])LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, "nc.bs.ic.pub.WdsIcPubBO", "queryIcOutBodyInfor", ParameterTypes, ParameterValues, 2);

		return os;
	}
}
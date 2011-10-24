package nc.ui.wds.ic.invstore;

import nc.ui.trade.bsdelegate.BDBusinessDelegator;

/**
 *
 *抽象业务代理类的缺省实现
 *@author author
 *@version tempProject version
 */
public class MyDelegator extends BDBusinessDelegator {

	/**
	 *
	 *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
	 *
	 */
//	public String getBodyCondition(Class bodyClass, String key) {
//		if (bodyClass == TbSpacegoodsVO.class)
//			return "pk_cargdoc = '" + key + "' and isnull(dr,0)=0 ";
//		return null;
//	}

}
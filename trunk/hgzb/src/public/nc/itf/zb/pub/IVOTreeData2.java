package nc.itf.zb.pub;

import nc.ui.trade.pub.IVOTreeData;

/**
 * 
 * @author zhf
 *
 */

public interface IVOTreeData2 extends IVOTreeData {

//	public String getWherePart();
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业） 加载树数据时允许用户注入过滤条件
	 * 2011-6-7下午01:34:05
	 */
	public void setWherePart(String whereSql);

}

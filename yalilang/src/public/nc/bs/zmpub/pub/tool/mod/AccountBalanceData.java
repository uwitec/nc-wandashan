package nc.bs.zmpub.pub.tool.mod;

import nc.vo.pub.SuperVO;

/**
 * 结存信息注册类
 * 
 * @author mlr
 */
public abstract class AccountBalanceData {
	/**
	 * 获取月结 结存表类全路径
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-10-31上午10:53:54
	 * @return
	 * @throws Exception
	 */
	public abstract String getMonthAccountClass() throws Exception;

	/**
	 * 注册月结 表存放 会计月id的字段名字
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-10-31上午11:03:55
	 * @return
	 * @throws Exception
	 */
	public abstract String getAcMonID() throws Exception;

	/**
	 * 鹤岗矿业）返回查询最后的结账月 会计月主键的 id
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-10-31上午11:11:15
	 * @return
	 * @throws Exception
	 */
	public abstract String getLastAccountMonthQuerySql(String corp)
			throws Exception;

	/**
	 * 获取月结表--->现存量的单表交换类
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-10-31下午02:05:26
	 * @return
	 * @throws Exception
	 */
	public abstract String getChangeClass() throws Exception;

	/**
	 * 加载期初数据 在什么情况下加载期初数据呢？
	 * 
	 * 月份结账时 如果一月份 已经结账 一月份的结账表中 肯定已经加载了期初数据
	 * 
	 * 如果一月份已经结账的情况下进行台账修复 就不会加载期初数据了 因为 结账时 ： 是从结存表中取已结账的数据 转化成现存量 加上
	 * 最后一个结账月往后的从业务 单据上汇总来得数据
	 * 
	 * 如果一月份未结账 那么期初数据 在修复的时候 就加载不到现存量中所以必须手动加载
	 * 
	 * 
	 * @author mlr
	 * @param whereSql4
	 * @说明：（鹤岗矿业） 2011-11-1下午12:47:10
	 * @return
	 * @throws Exception
	 */
	public abstract SuperVO[] loadPeridData(String whereSql4) throws Exception;
}

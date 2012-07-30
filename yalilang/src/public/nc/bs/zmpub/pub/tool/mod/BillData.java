package nc.bs.zmpub.pub.tool.mod;

import nc.vo.pub.lang.UFBoolean;

/**
 * 业务单据信息注册类
 * 
 * @author mlr
 */
public abstract class BillData {
	/**
	 * 注册变化规则
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-10-31上午09:25:17
	 * @return
	 * @throws Exception
	 */
	public abstract UFBoolean[] getIsChangeNum() throws Exception;

	/**
	 * 注册那些单据状态的单据可以查询 目前只支持保存态和审批态 0---为保存态 1---为审批态
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-10-31上午09:26:24
	 * @return
	 * @throws Exception
	 */
	public abstract boolean[] getBillStatus() throws Exception;

	/**
	 * 注册单据->现存量的单表交换类
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-10-31上午09:29:04
	 * @return
	 * @throws Exception
	 */
	public abstract String getChangeClass() throws Exception;
}

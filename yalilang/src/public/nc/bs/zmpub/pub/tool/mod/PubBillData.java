package nc.bs.zmpub.pub.tool.mod;

/**
 * 业务单据公共信息注册类
 * 
 * @author mlr
 */
public abstract class PubBillData {
	/**
	 * 根据某个维度查业务单据
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract String getQuerySql(String whereSql) throws Exception;

	/**
	 * 根据某个维度查询业务单据 各业务单据 的wheresql 必须字段一致 如果不一致的话 则工具类无法支持按某个维度进行数据修复
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract String[] getQuerySql1(String whereSql) throws Exception;

	/**
	 * 获得查询语句
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract String getQuerySql() throws Exception;

	/**
	 * 获得查询语句
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract String[] getQuerySql1() throws Exception;

	/**
	 * 获得单据类型字段名字 各业务单据必须保持统一
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract String getBillTypeName() throws Exception;

	/**
	 * 获得单据状态字段名字 各业务单据必须保持统一
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract String getBillTypeStatusName() throws Exception;

	/**
	 * 获取单据日期注册字段的名字 各业务单据必须保持统一
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-10-31上午11:17:44
	 * @return
	 * @throws Exception
	 */
	public abstract String getBillDateName() throws Exception;

}

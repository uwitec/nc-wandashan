package nc.bs.zmpub.pub.tool.mod;

/**
 * 现存量信息注册类
 * 
 * @author mlr
 */
public abstract class AccountData {
	/**
	 * 获得影响现存量的 需要调整设置的字段的数据类型 注册的接口为 IUFTypes
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-10-31下午12:26:04
	 * @return
	 * @throws Exception
	 */
	public abstract int[] getSetNumFieldsType() throws Exception;

	/**
	 * 根据某个维度清空现存量
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract String getClearSql(String whereSql) throws Exception;

	/**
	 * 获得影响现存量的 需要调整设置的字段名字数组
	 * 
	 * @return Map<String,boolean[]>
	 * @throws Exception
	 */
	public abstract String[] getSetNumFields() throws Exception;

	/**
	 * 获得现存量类名全路径
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract String getNumClass() throws Exception;

	/**
	 * 获得现存量最小维度数组
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract String[] getUnpk() throws Exception;
}

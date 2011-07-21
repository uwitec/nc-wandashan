package nc.ui.zb.pub;

/**
 * <p>多子表查询（主子表关联查询）相关信息。
 * @author twh
 * @date 2007-1-22 上午10:04:41
 * @version V5.0
 * @主要的类使用：
 *  <ul>
 * 		<li><b>如何使用该类：</b></li>
 *      <li><b>是否线程安全：</b></li>
 * 		<li><b>并发性要求：</b></li>
 * 		<li><b>使用约束：</b></li>
 * 		<li><b>其他：</b></li>
 * </ul>
 * </p>
 * <p>
 * @已知的BUG：
 * 	<ul>
 * 		<li></li>
 *  </ul>
 * </p>
 * <p>
 * @修改历史：
 */
public interface IMultiChildQueryInfo {

	/**
	 * 根据（单据模板对应）子表编码返回查询模板中对应
	 * @param tblCode
	 * @return
	 * twh (2007-1-22 上午10:27:37)<br>
	 */
	public abstract String getAliasByTableCode(String tblCode);
}

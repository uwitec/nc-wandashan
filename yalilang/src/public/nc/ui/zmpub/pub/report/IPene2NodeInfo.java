/**
 * 
 */
package nc.ui.zmpub.pub.report;

import nc.vo.zmpub.pub.report.IPeneExtendInfo;




/**
 * <p>
 * 报表穿透业务单据信息接口
 * 
 * @author xkf
 * @date 2007-6-21 下午06:58:13
 * @version V5.0
 * @主要的类使用：
 *          <ul>
 *          <li><b>如何使用该类：</b></li>
 *          <li><b>是否线程安全：</b></li>
 *          <li><b>并发性要求：</b></li>
 *          <li><b>使用约束：</b></li>
 *          <li><b>其他：</b></li>
 *          </ul>
 *          </p>
 *          <p>
 * @已知的BUG：
 *          <ul>
 *          <li></li>
 *          </ul>
 *          </p>
 *          <p>
 * @修改历史：
 */
public interface IPene2NodeInfo {

	/**
	 * @return 当前报表节点号
	 */
	public String getPene2NodeInfo();

	/**
	 * 获得额外穿透信息 
	 * 
	 * @return
	 */
	public IPeneExtendInfo getExtendInfo();
}

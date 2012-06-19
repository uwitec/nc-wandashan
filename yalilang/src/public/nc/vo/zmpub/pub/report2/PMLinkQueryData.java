/**
 * 
 */
package nc.vo.zmpub.pub.report2;

import nc.ui.pub.linkoperate.ILinkQueryData;

/**
 * <p>
 * @author xkf
 * @date 2007-6-21 下午08:40:49
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
public class PMLinkQueryData implements ILinkQueryData {

	/**
	 * 
	 */
	public PMLinkQueryData() {
		// TODO 自动生成构造函数存根
	}

	private String billID = null;
	private String billType = null;
	private String pkOrg = null;
	private Object userObject = null;
	public String getBillID() {
		// TODO 自动生成方法存根
		return billID;
	}

	public String getBillType() {
		// TODO 自动生成方法存根
		return billType;
	}

	public String getPkOrg() {
		// TODO 自动生成方法存根
		return pkOrg;
	}

	public Object getUserObject() {
		// TODO 自动生成方法存根
		return userObject;
	}

	public void setBillID(String billID) {
		this.billID = billID;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

}

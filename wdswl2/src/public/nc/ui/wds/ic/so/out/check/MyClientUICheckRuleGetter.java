package nc.ui.wds.ic.so.out.check;

import java.io.Serializable;
import nc.vo.trade.pub.IBDGetCheckClass2;

/**
 * <b> 前台校验类的Getter类 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject 1.0
 */

public class MyClientUICheckRuleGetter implements IBDGetCheckClass2,Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5273980673617565351L;

	/**
	 * 前台校验类
	 */
	public String getUICheckClass() {
		return "nc.ui.wds.ic.so.out.MyClientUICheckRule";
	}

	/**
	 * 后台校验类
	 */
	public String getCheckClass() {
		return "nc.bs.wds.ic.so.out.SoOutBO";
	}

}
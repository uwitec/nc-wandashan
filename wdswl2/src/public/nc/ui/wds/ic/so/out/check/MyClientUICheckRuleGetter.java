package nc.ui.wds.ic.so.out.check;

import java.io.Serializable;
import nc.vo.trade.pub.IBDGetCheckClass2;

/**
 * <b> ǰ̨У�����Getter�� </b>
 *
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
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
	 * ǰ̨У����
	 */
	public String getUICheckClass() {
		return "nc.ui.wds.ic.so.out.MyClientUICheckRule";
	}

	/**
	 * ��̨У����
	 */
	public String getCheckClass() {
		return "nc.bs.wds.ic.so.out.SoOutBO";
	}

}
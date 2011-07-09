package nc.ui.wds.dm.storetranscorp;


import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass;
/**
 * <p>
 *     保存前获取后台校验类
 * </p>
 *
 *
 * @author mlr
 * @version tempProject 1.0
 */
public class GetCheckClass implements IBDGetCheckClass,Serializable {
	private static final long serialVersionUID = -5323147651241531762L;
	/**
	 * 后台校验类
	 */
	public String getCheckClass() {
		return "nc.bs.wds.dm.storetranscorp.BSCheck";
	}
}
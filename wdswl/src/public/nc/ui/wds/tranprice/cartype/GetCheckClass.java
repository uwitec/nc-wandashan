package nc.ui.wds.tranprice.cartype;



import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass;
import nc.vo.trade.pub.IBDGetCheckClass2;

/**
 * 
 *
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
		return "nc.bs.wds.tranprice.cartype.BSCheck";
	}

}
package nc.ui.wds.load.price;

import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass2;

/**
 * 装卸费价格设置
 * @author Administrator
 *
 */
public class GetCheckClasses  implements IBDGetCheckClass2, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getCheckClass() {
		// TODO Auto-generated method stub
		return "nc.bs.wds.load.price.LoadPriceBO";
	}

	public String getUICheckClass() {
		// TODO Auto-generated method stub
		return null;
	}

}

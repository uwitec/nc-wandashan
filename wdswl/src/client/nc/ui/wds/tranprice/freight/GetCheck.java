package nc.ui.wds.tranprice.freight;

import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass2;

public class GetCheck implements Serializable, IBDGetCheckClass2 {
	private static final long serialVersionUID = 428965024363387549L;
	/**
	 * 后台校验类路径
	 */
	public String getCheckClass() {
		return "nc.bs.wds.tranprice.freight.ZhbzBSCheck";
	}
	/**
	 * 前台校验类路径
	 */
	public String getUICheckClass() {		
	//	return "nc.ui.wds.tranprice.freight.UICheck";
		return "";
	}
}
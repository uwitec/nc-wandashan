package nc.vo.wds.dm.storebing;

import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass2;

public class GetCheck implements Serializable, IBDGetCheckClass2 {
	private static final long serialVersionUID = 428965024363387549L;
	/**
	 * ��̨У����·��
	 */
	public String getCheckClass() {
		return "nc.bs.wds.dm.storebing.BSCheck";
	}
	/**
	 * ǰ̨У����·��
	 */
	public String getUICheckClass() {		
	//	return "nc.ui.wds.tranprice.freight.UICheck";
		return "";
	}
}
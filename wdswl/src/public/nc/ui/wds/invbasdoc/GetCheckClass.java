package nc.ui.wds.invbasdoc;

import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass2;

public class GetCheckClass implements Serializable,IBDGetCheckClass2{

	/**
	 * 
	 */
	private static final long serialVersionUID = -857937586420598785L;

	public String getUICheckClass() {
		
		return null;
	}

	public String getCheckClass() {
		
		return "nc.bs.wds.invbasdoc.BSCheck";
	}
	

}

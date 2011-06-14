package nc.ui.wds.dm.store;

import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass2;

public class MyClientUICheckRuleGetter implements IBDGetCheckClass2,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getCheckClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUICheckClass() {
		
		return "nc.ui.wds.dm.store.MyClientUICheckRule";
	}

}

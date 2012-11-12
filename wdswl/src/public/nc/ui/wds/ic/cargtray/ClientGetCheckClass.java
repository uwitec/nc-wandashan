package nc.ui.wds.ic.cargtray;

import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass2;

public class ClientGetCheckClass implements IBDGetCheckClass2,Serializable{


	private static final long serialVersionUID = 1L;

	public String getUICheckClass() {
		
		return "nc.ui.wds.ic.cargtray.ClientCheckCHK";
	}

	public String getCheckClass() {
		
		return null;
	}

}

package nc.vo.wds.invcl;

import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass;

public class checkClassInterface implements IBDGetCheckClass,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8251436265601954259L;

	public String getCheckClass() {
		// TODO Auto-generated method stub
		return "nc.bs.wds.invcl.InvClBO";
	}

}

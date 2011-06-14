package nc.ui.wds.ie.storepersons;

import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass;

public class GetCheck implements Serializable, IBDGetCheckClass {


//	public GetCheck() {
//		super();
//	}

	private static final long serialVersionUID = 428965024363387549L;

	public String getCheckClass() {

		return "nc.bs.wds.ie.storepersons.BSCheck";
	}









}

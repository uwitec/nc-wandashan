package nc.vo.wds.dm.wljsq;

import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass2;

public class PeriodCheckClass implements Serializable, IBDGetCheckClass2 {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getUICheckClass() {
		return null;
	//	return "nc.ui.wds.dm.corpseal.BeforeActionCheck" ;   //ǰ̨У��
	}

	public String getCheckClass() {
		// TODO Auto-generated method stub
//		return ConstFor12Yunfei.route_bs_checkclass;      //��̨У��
		return "nc.bs.wds.dm.wljsq.BdbusiCheck";
	}

}
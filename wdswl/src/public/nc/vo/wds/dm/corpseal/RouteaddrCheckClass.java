package nc.vo.wds.dm.corpseal;

import java.io.Serializable;

import nc.vo.trade.pub.IBDGetCheckClass2;
//xjx   add
public class RouteaddrCheckClass implements Serializable, IBDGetCheckClass2 {

	public String getUICheckClass() {
		return "nc.ui.wds.dm.corpseal.BeforeActionCheck" ;   //ǰ̨У��
	}

	public String getCheckClass() {
		// TODO Auto-generated method stub
//		return ConstFor12Yunfei.route_bs_checkclass;      //��̨У��
		return "nc.bs.wds.dm.corpseal.BdbusiCheck";
	}

}

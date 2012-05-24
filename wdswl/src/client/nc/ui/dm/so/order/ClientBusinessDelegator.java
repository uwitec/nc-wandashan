package nc.ui.dm.so.order;

import nc.ui.pub.ToftPanel;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;

public class ClientBusinessDelegator extends BusinessDelegator {

	
	private ToftPanel parent = null;
	
	public ClientBusinessDelegator(ToftPanel parent ){
		this.parent = parent;
	}
	
	@Override
	public SuperVO[] queryHeadAllData(Class headClass, String strBillType,
			String strWhere) throws Exception {
		// TODO Auto-generated method stub
//		String str = strWhere;
		if(PuPubVO.getString_TrimZeroLenAsNull(strWhere)!=null){
			if(strWhere.indexOf("dr")>1)
				strWhere = strWhere.replaceAll("dr", "wds_soorder.dr");
		}
		return TranOrderHelper.queryOrders(parent, strWhere);
	}	
}

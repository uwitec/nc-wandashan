package nc.ui.hg.pu.plan.mondeal;

import java.awt.Container;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.pub.SuperVO;

public class ClientBusinessDelegator extends BusinessDelegator {

	private Container parent = null;
	
	public ClientBusinessDelegator(Container parent ){
		this.parent = parent;
	}
	@Override
	public SuperVO[] queryHeadAllData(Class headClass, String strBillType,
			String strWhere) throws Exception {	
		String where = "";
		if(strWhere!=null && strWhere.length()>0){
			where =  " hg_plan.pk_plan in (select distinct h.pk_plan from hg_plan h join hg_monupdate_b b " +
			" on h.pk_plan = b.pk_plan join bd_invbasdoc c on c.pk_invbasdoc = b.pk_invbasdoc "+
	        " where isnull(h.dr,0)=0 and isnull(b.dr,0) = 0  and isnull(c.dr,0) = 0 "+
			" and  "+strWhere+") ";
		}
		SuperVO[] vo = HYPubBO_Client.queryByCondition(headClass, where);
		return vo;
	}
}

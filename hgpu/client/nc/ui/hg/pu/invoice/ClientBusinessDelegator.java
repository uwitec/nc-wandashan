package nc.ui.hg.pu.invoice;

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
			where =  " hg_bz_h.pk_bz_h in (select distinct h.pk_bz_h from hg_bz_h h join hg_bz_b b " +
			" on h.pk_bz_h = b.pk_bz_h where isnull(h.dr,0)=0 and isnull(b.dr,0) = 0"+
			" and  "+strWhere+") ";
		}
		SuperVO[] vo = HYPubBO_Client.queryByCondition(headClass, where);
		return vo;
	}
}

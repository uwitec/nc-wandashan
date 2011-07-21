package nc.ui.zb.bidfloor;

import java.awt.Container;

import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.pub.SuperVO;
import nc.vo.zb.pub.ZbPubTool;

public class ClientBusinessDelegator extends BusinessDelegator {

	private Container parent = null;
	
	public ClientBusinessDelegator(Container parent ){
		this.parent = parent;
	}
	@Override
	public SuperVO[] queryHeadAllData(Class headClass, String strBillType,
			String strWhere) throws Exception {	
		String where =null;
		
	   
		if(strWhere!=null && strWhere.length()>0){
			where =  " cbidfloorid in (select distinct zb_bidfloor_h.cbidfloorid from  zb_bidfloor_h  ,zb_bidfloor_b " +
					" where zb_bidfloor_h.cbidfloorid =  zb_bidfloor_b.cbidfloorid and  isnull( zb_bidfloor_h.dr,0)=0 and isnull(zb_bidfloor_b.dr,0) = 0 "+
					" and  "+strWhere+") ";
			String s = ZbPubTool.getParam();
			 if(s!=null &&!"".equals(s))
				 where = where+ " and zb_bidfloor_h.cbiddingid in(select h.cbiddingid from zb_bidding_h h where  nvl(h.dr, 0) = 0 and h.reserve1 = '"+s+"')";
		}
		SuperVO[] vo = HYPubBO_Client.queryByCondition(headClass, where);
		return vo;
	}
}

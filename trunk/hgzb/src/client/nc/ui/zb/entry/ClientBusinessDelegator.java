package nc.ui.zb.entry;

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
		String where = "";
		if(strWhere!=null && strWhere.length()>0){
			where =  " czbresultid in (select distinct zb_result_h.czbresultid from  zb_result_h  ,zb_result_b " +
					" where zb_result_h.czbresultid =  zb_result_b.czbresultid and  isnull( zb_result_h.dr,0)=0 and isnull(zb_result_b.dr,0) = 0 "+
					" and  "+strWhere+" and zb_result_b.nzbnum > 0) ";
			String s = ZbPubTool.getParam();
			 if(s!=null &&!"".equals(s))
				 where = where+ " and zb_result_h.cbiddingid in(select h.cbiddingid from zb_bidding_h h where  nvl(h.dr, 0) = 0 and h.reserve1 = '"+s+"')";
		}
		SuperVO[] vo = HYPubBO_Client.queryByCondition(headClass, where);
		return vo;
	}
	
	/**
	 * queryBodyVOs ·½·¨×¢½â¡£
	 */
	public nc.vo.pub.CircularlyAccessibleValueObject[] queryBodyAllData(
			Class voClass, String billType, String key, String strWhere)
			throws Exception {
		if (billType == null || billType.trim().length() == 0)
			return null;
		else{
			if(strWhere ==null)
				strWhere =" nzbnum > 0 ";
			else 
			   strWhere = strWhere+" and nzbnum > 0 ";
			return HYPubBO_Client.queryAllBodyData(billType, voClass, key,
					strWhere);
		}
			
	}
}

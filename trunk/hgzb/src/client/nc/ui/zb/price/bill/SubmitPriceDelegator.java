package nc.ui.zb.price.bill;

import nc.ui.pub.ToftPanel;
import nc.ui.self.changedir.CHGBiBodyTOPriceBody;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.zb.pub.SingleVOChangeDataUiTool;
import nc.vo.pub.SuperVO;
import nc.vo.zb.bidding.BiddingBodyVO;
import nc.vo.zb.price.bill.SubmitPriceBodyVO;
import nc.vo.zb.pub.ZbPubTool;

public class SubmitPriceDelegator extends BusinessDelegator {
	
//	private String bo = "nc.bs.zb.price.SubmitPriceBO";
	
	@Override
	public SuperVO[] queryHeadAllData(Class headClass, String strBillType,
			String strWhere) throws Exception {	
		String where = "";
		if(strWhere!=null && strWhere.length()>0){
			where =  " csubmitbillid in (select distinct zb_submitbill.csubmitbillid from  zb_submitbill  ,zb_submitbill_b " +
					" where zb_submitbill.csubmitbillid =  zb_submitbill_b.csubmitbillid and  isnull( zb_submitbill.dr,0)=0 and isnull(zb_submitbill_b.dr,0) = 0 "+
					" and  "+strWhere+") ";
			String s = ZbPubTool.getParam();
			 if(s!=null &&!"".equals(s))
				 where = where+ " and zb_submitbill.cbiddingid in(select h.cbiddingid from zb_bidding_h h where  nvl(h.dr, 0) = 0 and h.reserve1 = '"+s+"')";
		}
		SuperVO[] vo = HYPubBO_Client.queryByCondition(headClass, where);
		return vo;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）加载标段表体数据
	 * 2011-5-25下午01:47:05
	 * @param cbiddingid
	 * @param cvendorid
	 * @return
	 * @throws Exception
	 */
	public SubmitPriceBodyVO[] loadBiddingInvInfor(String cbiddingid,ToftPanel tp) throws Exception{
		
		String whereSql = "isnull(dr,0) = 0 and cbiddingid = '"+cbiddingid+"'";
		BiddingBodyVO[] bodys = (BiddingBodyVO[])HYPubBO_Client.queryByCondition(BiddingBodyVO.class, whereSql);
		if(bodys == null || bodys.length == 0)
			return null;
		
		return (SubmitPriceBodyVO[])SingleVOChangeDataUiTool.runChangeVOAry(bodys, SubmitPriceBodyVO.class, CHGBiBodyTOPriceBody.class.getName());
		
	}

}

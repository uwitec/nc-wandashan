package nc.bs.zb.bidding.view;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.HYPubBO;
import nc.bs.zb.pub.ZbBsPubTool;
import nc.bs.zb.pub.ZbPubBO;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.zb.bidding.BidViewBillVO;
import nc.vo.zb.bidding.BiddingBillVO;
import nc.vo.zb.bidding.BiddingBodyVO;
import nc.vo.zb.bidding.BiddingHeaderVO;
import nc.vo.zb.bidding.BiddingTimesVO;
import nc.vo.zb.pub.ZbPubTool;

public class BidViewBO {
	
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	private HYPubBO bo =null;
	private HYPubBO getBo(){
		if(bo==null){
			bo= new HYPubBO();
		}
		return bo;
	}
	
	public BidViewBillVO[] loadDatas(String userid) throws BusinessException{
		ZbPubBO bo =new ZbPubBO();
		
		String cvendorid =bo.getCvendoridByLogUser(userid);
		if(PuPubVO.getString_TrimZeroLenAsNull(cvendorid)==null)	
		    return null;
		String bidsql =" select s.cbiddingid from zb_biddingsuppliers s join zb_bidding_h h on h.cbiddingid = s.cbiddingid where s.ccustmanid = '"+cvendorid+"'" +
				" and isnull(s.dr,0)=0 and isnull(h.dr,0)=0 and h.vbillstatus = "+IBillStatus.CHECKPASS+" and h.ibusstatus in(0,1)";
		
			String s = ZbPubTool.getParam();
		    if(s!=null &&!"".equals(s))
		    	bidsql = bidsql+" and h.reserve1 = '"+s+"'";
		List<String> l = (List<String>)getDao().executeQuery(bidsql,ZbBsPubTool.COLUMNLISTPROCESSOR);
		if(l==null || l.size()==0)
			return null;
		int size =l.size();
		BidViewBillVO[] bills = new BidViewBillVO[size];
		for(int i=0;i<size;i++){
			String cbiddingid = l.get(i);
			
			BiddingHeaderVO[] head =(BiddingHeaderVO[])getBo().queryByCondition(BiddingHeaderVO.class,"cbiddingid ='"+cbiddingid+"' and isnull(dr,0)=0 ");
			BiddingBodyVO[] bodys =(BiddingBodyVO[])getBo().queryByCondition(BiddingBodyVO.class,"cbiddingid ='"+cbiddingid+"' and isnull(dr,0)=0 ");
			BiddingTimesVO[] times=(BiddingTimesVO[])getBo().queryByCondition(BiddingTimesVO.class,"cbiddingid ='"+cbiddingid+"' and isnull(dr,0)=0 ");
			
			BidViewBillVO bill = new BidViewBillVO();
			bill.setParentVO(head[0]);
			bill.setTableVO(BiddingBillVO.tablecode_body, bodys);
			bill.setTableVO(BiddingBillVO.tablecode_times, times);
			bills[i]=bill;
		}
		return bills;
	}
}

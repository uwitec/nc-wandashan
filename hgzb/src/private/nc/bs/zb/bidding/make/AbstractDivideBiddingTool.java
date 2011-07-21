package nc.bs.zb.bidding.make;

import java.util.Map;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.zb.bidding.BiddingBillVO;
import nc.vo.zb.bidding.HistoryPriceVO;

public abstract class AbstractDivideBiddingTool {

	public abstract Object divid(HistoryPriceVO[] prices) throws Exception;
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��������ʷƷ����
	 * 2011-5-18����06:06:51
	 * @param prices
	 * @return
	 * @throws BusinessException
	 */
	protected Map<String,UFDouble> colHistoryPrice(HistoryPriceVO[] prices) throws BusinessException{
		return BiddingBillVO.colHistoryPrice(prices);
	}
}

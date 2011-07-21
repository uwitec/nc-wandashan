package nc.vo.zb.comments;

import nc.vo.trade.pub.HYBillVO;


/**
 * 
 * @author zhf  该接口存在没什么实质意义   611 时分量算法调整下的产物  调整后  支持两种算法
 * 耦合度第一点了
 *
 */
public interface ISplitNumPara {
	public void clear();
	public void refresh(String cbiddingid,HYBillVO bill) throws Exception;
	public HYBillVO getBill();	
}

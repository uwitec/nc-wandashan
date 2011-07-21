package nc.itf.zb.price;

import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.zb.bidding.BiddingTimesVO;

public interface IPriceTimerEventHandler {

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）计时开始
	 * 2011-4-29上午10:00:08
	 * @throws BusinessException
	 */
	public void doTimerBegin();
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）计时结束
	 * 2011-4-29上午10:00:24
	 * @throws BusinessException
	 */
	public void doTimerEnd();
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业） 判断计时器计时是否到某个时间
	 * 2011-4-29上午10:12:27
	 * @param uTime 指定时间
	 * @param subtime 计时器计时数（以服务器时间为计时开始时间的计时数）
	 * @return
	 */
	public boolean isTimeEquals(UFDateTime uTime,long m_timer);
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）获取当前报价轮次
	 * 2011-4-29上午10:20:12
	 * @return
	 */
	public BiddingTimesVO getCurrSubmitPriceCircal();
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）初始化当前轮次
	 * 2011-6-5下午05:42:03
	 */
	public void initCurrSubmitPriceCircal();
    
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）
	 * 2011-5-12下午12:14:28
	 * @param m_timer 距离初始时间的毫秒数
	 */
	public void setSubTime(long m_timer);
}

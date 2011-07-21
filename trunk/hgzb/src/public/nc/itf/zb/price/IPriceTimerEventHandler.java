package nc.itf.zb.price;

import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.zb.bidding.BiddingTimesVO;

public interface IPriceTimerEventHandler {

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ʱ��ʼ
	 * 2011-4-29����10:00:08
	 * @throws BusinessException
	 */
	public void doTimerBegin();
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ʱ����
	 * 2011-4-29����10:00:24
	 * @throws BusinessException
	 */
	public void doTimerEnd();
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�� �жϼ�ʱ����ʱ�Ƿ�ĳ��ʱ��
	 * 2011-4-29����10:12:27
	 * @param uTime ָ��ʱ��
	 * @param subtime ��ʱ����ʱ�����Է�����ʱ��Ϊ��ʱ��ʼʱ��ļ�ʱ����
	 * @return
	 */
	public boolean isTimeEquals(UFDateTime uTime,long m_timer);
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ȡ��ǰ�����ִ�
	 * 2011-4-29����10:20:12
	 * @return
	 */
	public BiddingTimesVO getCurrSubmitPriceCircal();
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ʼ����ǰ�ִ�
	 * 2011-6-5����05:42:03
	 */
	public void initCurrSubmitPriceCircal();
    
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��
	 * 2011-5-12����12:14:28
	 * @param m_timer �����ʼʱ��ĺ�����
	 */
	public void setSubTime(long m_timer);
}

package nc.ui.zb.price.web;

import java.util.TimerTask;
import nc.itf.zb.price.IPriceTimerEventHandler;
import nc.vo.zb.bidding.BiddingTimesVO;

/**
 * 
 * @author zhf
 * ���ϱ��۶�ʱ�������
 *
 */

public class WebPriceTimerTask extends TimerTask {
	private long m_timer = 0;//��ʱ��
	
	private IPriceTimerEventHandler m_handler = null;//�¼�������
	
	public WebPriceTimerTask(IPriceTimerEventHandler handler){
		super();
		m_handler = handler;
	}
	
	/**
	 * �ִν׶� ��ʼ  ˢ�½�������  ���ᱨ��ť  ׼�����ܹ�Ӧ�̵ı�����Ϣ  
	 * ���� �¸��ִν׶�  ��������һ���׶ν���
	 * ֹͣ����
	 */
	private void doTimeBegin(){
		m_handler.doTimerBegin();
	}
	
	/**
	 * �ִν׶� ����  �رս��������  �ر��ᱨ��ť
	 */
	private void doTimeEnd(){
		m_handler.doTimerEnd();
	}

	@Override
	public void run() {//Ҫ��1s����һ��  �̶�Ƶ��
		// TODO Auto-generated method stub
		m_timer = m_timer+1000;
//		System.out.println(m_timer);
		BiddingTimesVO circal = m_handler.getCurrSubmitPriceCircal();

		if(circal == null){
			m_handler.initCurrSubmitPriceCircal();
			circal = m_handler.getCurrSubmitPriceCircal();
			if(circal == null)
				return;
		}
			
		if(m_handler.isTimeEquals(circal.getTbigintime(), m_timer)){
			doTimeBegin();
//			��ʾ����ʱ�Ի���
		}
		if(m_handler.isTimeEquals(circal.getTendtime(),m_timer)){
			doTimeEnd();
//			ע������ʱ�Ի���
		}
		m_handler.setSubTime(m_timer);
	}

}

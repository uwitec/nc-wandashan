package nc.ui.zb.price.web;

import java.util.TimerTask;
import nc.itf.zb.price.IPriceTimerEventHandler;
import nc.vo.zb.bidding.BiddingTimesVO;

/**
 * 
 * @author zhf
 * 网上报价定时服务组件
 *
 */

public class WebPriceTimerTask extends TimerTask {
	private long m_timer = 0;//计时器
	
	private IPriceTimerEventHandler m_handler = null;//事件处理器
	
	public WebPriceTimerTask(IPriceTimerEventHandler handler){
		super();
		m_handler = handler;
	}
	
	/**
	 * 轮次阶段 开始  刷新界面数据  打开提报按钮  准备接受供应商的报价信息  
	 * 设置 下个轮次阶段  如果是最后一个阶段结束
	 * 停止任务
	 */
	private void doTimeBegin(){
		m_handler.doTimerBegin();
	}
	
	/**
	 * 轮次阶段 结束  关闭界面操作性  关闭提报按钮
	 */
	private void doTimeEnd(){
		m_handler.doTimerEnd();
	}

	@Override
	public void run() {//要求1s触发一次  固定频率
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
//			显示倒计时对话框
		}
		if(m_handler.isTimeEquals(circal.getTendtime(),m_timer)){
			doTimeEnd();
//			注销倒计时对话框
		}
		m_handler.setSubTime(m_timer);
	}

}

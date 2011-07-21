package nc.ui.zb.price.web;

import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.zb.price.pub.AbstractPriceDataBuffer;
import nc.ui.zb.price.pub.SubmitPriceHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.bidding.BiddingTimesVO;
import nc.vo.zb.price.SubmitPriceVO;
import nc.vo.zb.pub.ZbPubConst;

public class WebSubmitPriceBuffer extends AbstractPriceDataBuffer{	
	private BiddingTimesVO[] m_times = null; //标书设置的招标轮次	
	private BiddingTimesVO m_time = null;//当前招标轮次
	private UFDateTime m_servertime = null;//服务器初始时间
//	private boolean bisend = false;
	private boolean isbegin = false;	
	

	
	public WebSubmitPriceBuffer(WebSubmitPriceUI tp){
		super(tp);
	}
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）是否跟标轮次
	 * 2011-5-12下午05:10:18
	 * @return
	 */
	public boolean isBisfollow() {
		return m_time==null?false:PuPubVO.getUFBoolean_NullAs(m_time.getBisfollow(),UFBoolean.FALSE).booleanValue();
	}

	public void setBeging(boolean isb){
		isbegin = isb;
	}
	public boolean isBegin(){
		return isbegin;
	}
	
	public void clear(){
		isbegin = false;
		m_times = null;
		m_time = null;
		m_servertime = null;
//		bisend = false;
		super.clear();
	}
	
	public void clearWhenRefresh(){
		isbegin = false;
		m_times = null;
		m_time = null;
		m_servertime = null;
		
		m_prices = null;		
//		bisinv = true;
		if(invPriceInfor != null)
		invPriceInfor.clear();	
	}
	
	public void clearWhenTimerEnd(){
		m_time = null;
	    isbegin = false;
		m_prices = null;	
	}
	
	public void initData(String cbiddString,boolean bisself) throws Exception{
		if(PuPubVO.getString_TrimZeroLenAsNull(cbiddString)==null){
			return;
		}
		cbiddingid = cbiddString; 
//		bisinv = !bisself;
		initDataForWeb(cbiddString);
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）网上报价专业数据初始化
	 * 2011-4-29下午02:45:20
	 * @param cbiddString
	 * @throws Exception
	 */
	private void initDataForWeb(String cbiddString) throws Exception{
		m_servertime = ClientEnvironment.getServerTime();
		m_times = (BiddingTimesVO[])HYPubBO_Client.queryByCondition(BiddingTimesVO.class, " isnull(dr,0)=0 " +
				" and cbiddingid = '"+cbiddString+"' and coalesce(bisfollow,'N')='Y'");//增加支持   是否网上报价
		if(m_times == null||m_times.length == 0){
			throw new BusinessException("选中标书的招标轮次没有设置,系统无法开展网上招标");
		}
		SubmitPriceHelper.dealBiddingTimes(m_times);
		m_time = SubmitPriceHelper.getCurrentTime(m_servertime, m_times);
		if(m_time!=null){//轮次时间中间进入
			setBeging(true);
			SubmitPriceVO[] prices = SubmitPriceHelper.getPriceVos(ui,cbiddingid, m_time.getCprecircalnoid(), cvendorid, bisinv,ZbPubConst.WEB_SUBMIT_PRICE);
//			List<SubmitPriceVO> lprice = null;
//			if(isBisfollow()){//是否跟标阶段      后续讨论  客户放弃跟标阶段   代码预留吧zhf20110521  是否跟标已被占用：是否网上报价
//				lprice = new ArrayList<SubmitPriceVO>();
//				for(SubmitPriceVO price:prices){//过滤报价数据  不需要跟标的品种 过滤掉
//					if(PuPubVO.getUFDouble_NullAsZero(price.getNllowerprice()).doubleValue()<PuPubVO.getUFDouble_NullAsZero(price.getNlastprice()).doubleValue()){
//						lprice.add(price);
//					}
//				}
//				
//				ui.showHintMessage("进入跟标阶段");
//				if(lprice.size()>0)
//					prices = lprice.toArray(new SubmitPriceVO[0]);
//				else
//					prices = null;
//			}
			setM_prices(prices);
			ui.setCircalInforToHead(false);
			ui.setButtonState(true);
		}else{
//			两种情况  过时了   未到时间    还有一种情况 轮次之间的空挡
			BiddingTimesVO first = SubmitPriceHelper.getFirstTime(m_times);
			BiddingTimesVO last = SubmitPriceHelper.getLastTime(m_times);
			if(m_servertime.getMillis()<first.getTbigintime().getMillis()-ZbPubConst.TIME_DIFFERRENCE){
				m_time = first;
				ui.setCircalInforToHead(false);
//				setBeging(true);
				ui.showHintMessage("没到该轮次的开始时间");
				return;
			}else if(m_servertime.getMillis()>last.getTendtime().getMillis()+ZbPubConst.TIME_DIFFERRENCE){
//				bisend = true;
				throw new BusinessException("该标书报价已结束");
			}else{
				m_time = SubmitPriceHelper.getNextTime(m_servertime, m_times);
				ui.setCircalInforToHead(false);
				ui.showHintMessage("没到该轮次的开始时间");
				return;
			}
			
		}
//		
	}
	
//	public boolean isBisend() {
//		return bisend;
//	}
//
//	public void setBisend(boolean bisend) {
//		this.bisend = bisend;
//	}
	
	public BiddingTimesVO[] getM_times() {
		return m_times;
	}
	public void setM_times(BiddingTimesVO[] m_times) {
		this.m_times = m_times;
	}
	public BiddingTimesVO getM_time() {
		return m_time;
	}
	public void setM_time(BiddingTimesVO m_time) {
		this.m_time = m_time;
	}
	public UFDateTime getM_servertime() {
		return m_servertime;
	}
	public void setM_servertime(UFDateTime m_servertime) {
		this.m_servertime = m_servertime;
	}
	@Override
	public String getCurrentCircalID() {
		// TODO Auto-generated method stub
		return getM_time() == null?null:getM_time().getPrimaryKey();
	}
}

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
	private BiddingTimesVO[] m_times = null; //�������õ��б��ִ�	
	private BiddingTimesVO m_time = null;//��ǰ�б��ִ�
	private UFDateTime m_servertime = null;//��������ʼʱ��
//	private boolean bisend = false;
	private boolean isbegin = false;	
	

	
	public WebSubmitPriceBuffer(WebSubmitPriceUI tp){
		super(tp);
	}
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ���Ƿ�����ִ�
	 * 2011-5-12����05:10:18
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
	 * @˵�������׸ڿ�ҵ�����ϱ���רҵ���ݳ�ʼ��
	 * 2011-4-29����02:45:20
	 * @param cbiddString
	 * @throws Exception
	 */
	private void initDataForWeb(String cbiddString) throws Exception{
		m_servertime = ClientEnvironment.getServerTime();
		m_times = (BiddingTimesVO[])HYPubBO_Client.queryByCondition(BiddingTimesVO.class, " isnull(dr,0)=0 " +
				" and cbiddingid = '"+cbiddString+"' and coalesce(bisfollow,'N')='Y'");//����֧��   �Ƿ����ϱ���
		if(m_times == null||m_times.length == 0){
			throw new BusinessException("ѡ�б�����б��ִ�û������,ϵͳ�޷���չ�����б�");
		}
		SubmitPriceHelper.dealBiddingTimes(m_times);
		m_time = SubmitPriceHelper.getCurrentTime(m_servertime, m_times);
		if(m_time!=null){//�ִ�ʱ���м����
			setBeging(true);
			SubmitPriceVO[] prices = SubmitPriceHelper.getPriceVos(ui,cbiddingid, m_time.getCprecircalnoid(), cvendorid, bisinv,ZbPubConst.WEB_SUBMIT_PRICE);
//			List<SubmitPriceVO> lprice = null;
//			if(isBisfollow()){//�Ƿ����׶�      ��������  �ͻ���������׶�   ����Ԥ����zhf20110521  �Ƿ�����ѱ�ռ�ã��Ƿ����ϱ���
//				lprice = new ArrayList<SubmitPriceVO>();
//				for(SubmitPriceVO price:prices){//���˱�������  ����Ҫ�����Ʒ�� ���˵�
//					if(PuPubVO.getUFDouble_NullAsZero(price.getNllowerprice()).doubleValue()<PuPubVO.getUFDouble_NullAsZero(price.getNlastprice()).doubleValue()){
//						lprice.add(price);
//					}
//				}
//				
//				ui.showHintMessage("�������׶�");
//				if(lprice.size()>0)
//					prices = lprice.toArray(new SubmitPriceVO[0]);
//				else
//					prices = null;
//			}
			setM_prices(prices);
			ui.setCircalInforToHead(false);
			ui.setButtonState(true);
		}else{
//			�������  ��ʱ��   δ��ʱ��    ����һ����� �ִ�֮��Ŀյ�
			BiddingTimesVO first = SubmitPriceHelper.getFirstTime(m_times);
			BiddingTimesVO last = SubmitPriceHelper.getLastTime(m_times);
			if(m_servertime.getMillis()<first.getTbigintime().getMillis()-ZbPubConst.TIME_DIFFERRENCE){
				m_time = first;
				ui.setCircalInforToHead(false);
//				setBeging(true);
				ui.showHintMessage("û�����ִεĿ�ʼʱ��");
				return;
			}else if(m_servertime.getMillis()>last.getTendtime().getMillis()+ZbPubConst.TIME_DIFFERRENCE){
//				bisend = true;
				throw new BusinessException("�ñ��鱨���ѽ���");
			}else{
				m_time = SubmitPriceHelper.getNextTime(m_servertime, m_times);
				ui.setCircalInforToHead(false);
				ui.showHintMessage("û�����ִεĿ�ʼʱ��");
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

package nc.ui.zb.price.web;

import java.util.Map;
import java.util.Timer;

import nc.itf.zb.price.IPriceTimerEventHandler;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.zb.price.pub.PricePubEventHandler;
import nc.ui.zb.price.pub.SubmitPriceHelper;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.bidding.BiddingTimesVO;
import nc.vo.zb.parmset.ParamSetVO;
import nc.vo.zb.price.SubmitPriceVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class WebPriceEventHandler extends PricePubEventHandler implements IPriceTimerEventHandler{
	
	public WebPriceEventHandler(WebSubmitPriceUI ui) {
		super(ui);
	}
	
	private WebSubmitPriceUI getUI(){
		return (WebSubmitPriceUI)ui;
	}
	
	public void onFollow(){
		if(ui.getBillType().equalsIgnoreCase(ZbPubConst.ZB_LOCAL_BILLTYPE)){
			return;
		}
//		У�鵱ǰ�Ƿ�Ϊ����׶�
		boolean isfollow = getUI().getWebDataBuffer().isBisfollow();
		if(!isfollow){
			ui.showErrorMessage("��ǰ���۽׶β��Ǹ���׶�");
			return;
		}
			
//		����ͱ������õ����α����ֶ�
		SubmitPriceVO[] prices = ui.getPriceDatas();
		if(prices == null || prices.length == 0)
			return;
		for(SubmitPriceVO price:prices){
			price.setNprice(price.getNllowerprice());
			price.setIsubmittype(ZbPubConst.WEB_SUBMIT_PRICE);
		}
		ui.setDataToUI();
	}

	public void doTimerBegin(){
		//�ִμ�ʱ��ʼ		
		//��ѯ������Ϣ   ˢ�½���   ��ť����   ����ʱ����
//		System.out.println("���ۿ�ʼ");
		ui.showHintMessage("���ۿ�ʼ");
		try{
			SubmitPriceVO[] prices = SubmitPriceHelper.getPriceVos(ui,ui.getDataBuffer().getCbiddingid(), getUI().getWebDataBuffer().getM_time().getCprecircalnoid(), 
					ui.getDataBuffer().getCvendorid(), ui.getDataBuffer().isBisinv(),getIsubMitType());
//			List<SubmitPriceVO> lprice = null;
//			boolean isfollow = getUI().getWebDataBuffer().isBisfollow();
//			if(isfollow){//�Ƿ����׶�
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
			ui.getDataBuffer().setM_prices(prices);
			ui.setDataToUI();
			ui.setCircalInforToHead(false);
			getUI().getWebDataBuffer().setBeging(true);
			ui.setButtonState(true);
		}catch(Exception e){
			e.printStackTrace();
			ui.showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
		}		
	}

	public void doTimerEnd(){
		
//		System.out.println("���۽���");
		
//		�ִμ�ʱ����
		String cnextcircalid = SubmitPriceHelper.isEnd(getUI().getWebDataBuffer().getM_time());
		getUI().getWebDataBuffer().setBeging(false);
		if(cnextcircalid ==  null){//���۽���			
			ui.clearData();
			cancelTimer();
			//���ñ���ҵ��״̬
			ui.showWarningMessage("�������б����");
			ui.showHintMessage("�������б����");			
			return;
		}
		
		ui.showHintMessage("���ִα��۽���");
		ui.setCircalInforToHead(true);
		ui.getDataPanel().clearBodyData();
		getUI().getWebDataBuffer().clearWhenTimerEnd();
		ui.setButtonState(false);
		
//		ȷ���±�����һ�ִ�
		BiddingTimesVO newtime = SubmitPriceHelper.getBiddingTimeByID(cnextcircalid,getUI().getWebDataBuffer().getM_times());
		if(newtime == null){
			cancelTimer();
			ui.showErrorMessage("δ��ȡ����һ�������ִ���Ϣ");
			return;
		}
		getUI().getWebDataBuffer().setM_time(newtime);
		ui.setCircalInforToHead(false);
	}

	public boolean isTimeEquals(UFDateTime time, long m_timer) {//��ʱ�Ƿ�
		long timeno = time.getMillisAfter(getUI().getWebDataBuffer().getM_servertime());
		if(timeno>=m_timer-ZbPubConst.TIME_DIFFERRENCE && timeno<=m_timer+ZbPubConst.TIME_DIFFERRENCE)
			return true;
		return false;
	}

	public BiddingTimesVO getCurrSubmitPriceCircal() {
		// TODO Auto-generated method stub
		return getUI().getWebDataBuffer().getM_time();
	}
	public void afterEdit(BillEditEvent e) {
		if(e.getPos() == BillItem.HEAD && "cbiddingid".equalsIgnoreCase(e.getKey()))
			afterEditWhenBidCode(e);
		super.afterEdit(e);
	}
	
	public void afterEditWhenBidCode(BillEditEvent e){
		//�����ִ�ʱ��  ˢ�±�������   ȷ����ǰ�ִ�   ȷ����ͱ���
		String key = getHeadValue("cbiddingid");
		UFBoolean bisinvcl = PuPubVO.getUFBoolean_NullAs(getHeadValue("fisinvcl"), UFBoolean.FALSE);		
//		�����л� ֹͣ��ʱ������
		cancelTimer();
		if(key == null){
//			��ձ�������  ��ջ���
			ui.clearData();
			
			ui.setButtonState(false);
			return;
		}
		
		try {
			getUI().getWebDataBuffer().initData(key,!bisinvcl.booleanValue());	
			ui.setDataToUI();
			if(getUI().getWebDataBuffer().isBegin()){
				ui.setButtonState(true);
			}
		} catch (Exception e1) {
			e1.printStackTrace();	
			ui.showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e1.getMessage()));
			return;
		}		
	
//		������ʱ����
		getTimer().scheduleAtFixedRate(getTimerTask(), 0, 1000);	
	}
	
	private Timer timeServer = null;
	private WebPriceTimerTask timeTask = null;
	private Timer getTimer(){
//		if(timeServer == null){
			timeServer = new Timer();
//		}
		return timeServer;
	}
	
	private void cancelTimer(){
		if(timeServer!=null)
			timeServer.cancel();
	}
	private WebPriceTimerTask getTimerTask(){//ÿ����һ������һ���µ������߳�
		//		if(timeTask == null)
		 timeTask = new WebPriceTimerTask(this);
				return timeTask;
	}
	
	@Override
	public Integer getIsubMitType() {
		// TODO Auto-generated method stub
		return ZbPubConst.WEB_SUBMIT_PRICE;
	}

	public void setSubTime(long m_timer) {
		// TODO Auto-generated method stub
		getUI().setSubTime(m_timer);
	}

	private BadSubmitCtrl m_badctrl = null;
	private BadSubmitCtrl getBadCtrl(){
		if(m_badctrl == null)
			m_badctrl = new BadSubmitCtrl();
		return m_badctrl;
	}
	@Override
	public boolean controlBadPrice()  throws Exception{
		// TODO Auto-generated method stub
		ParamSetVO para = ui.getDataBuffer().getPara();
		boolean iscontrol = PuPubVO.getUFBoolean_NullAs(para.getFisbadquotation(),UFBoolean.FALSE).booleanValue();
		if(!iscontrol )//|| para.getIbadquotanum() == null
			return true;
		SubmitPriceVO[] prices = ui.getDataBuffer().getM_prices();
		
		SubmitPriceHelper.validationDataOnSubmit(prices, ui.getDataBuffer().isBisinv(), ZbPubConst.WEB_SUBMIT_PRICE);
//		List<SubmitPriceVO> lprice = new ArrayList<SubmitPriceVO>();
		UFDouble nminprice = null;//��ͼ�
		UFDouble nbiddingprice = null;//��׼�
		Map<String, UFDouble> minPriceInfor = ui.getDataBuffer().getInvPriceInfor();
		if(minPriceInfor == null || minPriceInfor.size() == 0)
			throw new ValidationException("�ñ��Ʒ�ֵı�׼���Ϣ������");
		for(SubmitPriceVO price:prices){
//			����������= ��׼�*��1-������ƫ��ϵ����
			nbiddingprice = PuPubVO.getUFDouble_NullAsZero(
					minPriceInfor.get(price.getCinvbasid()));

			nminprice = SubmitPriceVO.getMinPrice(nbiddingprice, PuPubVO.getUFDouble_NullAsZero(para.getNquotationlower()));
			
			if(price.getNprice().doubleValue()<nminprice.doubleValue()){
				getBadCtrl().addBadSubmit(price);
				if(getBadCtrl().getBadSubNum() >= PuPubVO.getInteger_NullAs(para.getIbadquotanum(),0)){
					getBadCtrl().dealBadSubmit();
					getBadCtrl().clear();
					ui.clearData();
					ui.showWarningMessage("���ⱨ�۴���������ߴ���");
				}else
					ui.showWarningMessage("���ڶ��ⱨ��");
				return false;
			}
		}
		
		return true;
	}
	
	public  void onRefresh(){		
		/**
		 * һ���������¼���   �������  ������  ����ʱ��
		 */
		

//		У�� 
		String cbiddingid = ui.getDataBuffer().getCbiddingid();
		String cvendorid = ui.getDataBuffer().getCvendorid();
		if(cbiddingid == null){
			ui.showHintMessage("��ѡ�����");
			return;
		}
		if(cvendorid == null){
			ui.showHintMessage("��ѡ��Ӧ��");
			return;
		}
		cancelTimer();
		getUI().clearOnRefresh();
		

		try {
			getUI().getWebDataBuffer().initData(cbiddingid, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ui.showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
		}
		
		
		
		
//		SubmitPriceVO[] prices = null;
//		try{
//			prices = SubmitPriceHelper.getPriceVos(ui, cbiddingid, null, cvendorid, ui.getDataBuffer().isBisinv(),ZbPubConst.LOCAL_SUBMIT_PRICE);
//		}catch(Exception e){
//			e.printStackTrace();
//			ui.showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
//		}
//		ui.getDataBuffer().setM_prices(prices);
//		if(prices == null || prices.length == 0){
//			return;
//		}
		ui.setDataToUI();
//	
//		������ʱ����
		getTimer().scheduleAtFixedRate(getTimerTask(), 0, 1000);	
		
	}

	@Override
	public Object getUserObject() {
		// TODO Auto-generated method stub
		
//		�����б� ���� �ִε�ʱ�����Ϣ  ��  �ִ�����
		return new Object[]{getUI().getWebDataBuffer().getM_time().getTs(),getUI().getWebDataBuffer().getM_times().length};
	}

	public void initCurrSubmitPriceCircal() {
		// TODO Auto-generated method stub
		WebSubmitPriceBuffer buffer = getUI().getWebDataBuffer();
		buffer.setM_time(SubmitPriceHelper.getCurrentTime(buffer.getM_servertime(), buffer.getM_times()));
	}
}

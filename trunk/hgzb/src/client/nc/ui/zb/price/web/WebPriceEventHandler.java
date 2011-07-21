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
//		校验当前是否为跟标阶段
		boolean isfollow = getUI().getWebDataBuffer().isBisfollow();
		if(!isfollow){
			ui.showErrorMessage("当前报价阶段不是跟标阶段");
			return;
		}
			
//		将最低报价设置到本次报价字段
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
		//轮次计时开始		
		//查询报价信息   刷新界面   按钮可用   倒计时窗口
//		System.out.println("报价开始");
		ui.showHintMessage("报价开始");
		try{
			SubmitPriceVO[] prices = SubmitPriceHelper.getPriceVos(ui,ui.getDataBuffer().getCbiddingid(), getUI().getWebDataBuffer().getM_time().getCprecircalnoid(), 
					ui.getDataBuffer().getCvendorid(), ui.getDataBuffer().isBisinv(),getIsubMitType());
//			List<SubmitPriceVO> lprice = null;
//			boolean isfollow = getUI().getWebDataBuffer().isBisfollow();
//			if(isfollow){//是否跟标阶段
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
		
//		System.out.println("报价结束");
		
//		轮次计时结束
		String cnextcircalid = SubmitPriceHelper.isEnd(getUI().getWebDataBuffer().getM_time());
		getUI().getWebDataBuffer().setBeging(false);
		if(cnextcircalid ==  null){//报价结束			
			ui.clearData();
			cancelTimer();
			//设置标书业务状态
			ui.showWarningMessage("本标书招标结束");
			ui.showHintMessage("本标书招标结束");			
			return;
		}
		
		ui.showHintMessage("本轮次报价结束");
		ui.setCircalInforToHead(true);
		ui.getDataPanel().clearBodyData();
		getUI().getWebDataBuffer().clearWhenTimerEnd();
		ui.setButtonState(false);
		
//		确定下报价下一轮次
		BiddingTimesVO newtime = SubmitPriceHelper.getBiddingTimeByID(cnextcircalid,getUI().getWebDataBuffer().getM_times());
		if(newtime == null){
			cancelTimer();
			ui.showErrorMessage("未获取到下一个报价轮次信息");
			return;
		}
		getUI().getWebDataBuffer().setM_time(newtime);
		ui.setCircalInforToHead(false);
	}

	public boolean isTimeEquals(UFDateTime time, long m_timer) {//计时是否到
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
		//加载轮次时间  刷新表体数据   确定当前轮次   确定最低报价
		String key = getHeadValue("cbiddingid");
		UFBoolean bisinvcl = PuPubVO.getUFBoolean_NullAs(getHeadValue("fisinvcl"), UFBoolean.FALSE);		
//		标书切换 停止计时器服务
		cancelTimer();
		if(key == null){
//			清空表体数据  清空缓存
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
	
//		启动定时服务
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
	private WebPriceTimerTask getTimerTask(){//每调用一次启用一个新的任务线程
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
		UFDouble nminprice = null;//最低价
		UFDouble nbiddingprice = null;//标底价
		Map<String, UFDouble> minPriceInfor = ui.getDataBuffer().getInvPriceInfor();
		if(minPriceInfor == null || minPriceInfor.size() == 0)
			throw new ValidationException("该标段品种的标底价信息不存在");
		for(SubmitPriceVO price:prices){
//			合理报价下限= 标底价*（1-合理报价偏差系数）
			nbiddingprice = PuPubVO.getUFDouble_NullAsZero(
					minPriceInfor.get(price.getCinvbasid()));

			nminprice = SubmitPriceVO.getMinPrice(nbiddingprice, PuPubVO.getUFDouble_NullAsZero(para.getNquotationlower()));
			
			if(price.getNprice().doubleValue()<nminprice.doubleValue()){
				getBadCtrl().addBadSubmit(price);
				if(getBadCtrl().getBadSubNum() >= PuPubVO.getInteger_NullAs(para.getIbadquotanum(),0)){
					getBadCtrl().dealBadSubmit();
					getBadCtrl().clear();
					ui.clearData();
					ui.showWarningMessage("恶意报价次数超过最高次数");
				}else
					ui.showWarningMessage("存在恶意报价");
				return false;
			}
		}
		
		return true;
	}
	
	public  void onRefresh(){		
		/**
		 * 一切数据重新计算   清理界面  清理缓存  清理定时器
		 */
		

//		校验 
		String cbiddingid = ui.getDataBuffer().getCbiddingid();
		String cvendorid = ui.getDataBuffer().getCvendorid();
		if(cbiddingid == null){
			ui.showHintMessage("请选择标书");
			return;
		}
		if(cvendorid == null){
			ui.showHintMessage("请选择供应商");
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
//		启动定时服务
		getTimer().scheduleAtFixedRate(getTimerTask(), 0, 1000);	
		
	}

	@Override
	public Object getUserObject() {
		// TODO Auto-generated method stub
		
//		网上招标 返回 轮次的时间戳信息  和  轮次数量
		return new Object[]{getUI().getWebDataBuffer().getM_time().getTs(),getUI().getWebDataBuffer().getM_times().length};
	}

	public void initCurrSubmitPriceCircal() {
		// TODO Auto-generated method stub
		WebSubmitPriceBuffer buffer = getUI().getWebDataBuffer();
		buffer.setM_time(SubmitPriceHelper.getCurrentTime(buffer.getM_servertime(), buffer.getM_times()));
	}
}

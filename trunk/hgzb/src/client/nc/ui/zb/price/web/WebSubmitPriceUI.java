package nc.ui.zb.price.web;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.zb.price.pub.AbstractPricePubUI;
import nc.ui.zb.price.pub.PricePubEventHandler;
import nc.ui.zb.pub.ZbPubHelper;
import nc.ui.zb.pub.refmodel.BiddingRefModelForWeb;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.bidding.BiddingTimesVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class WebSubmitPriceUI extends AbstractPricePubUI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public WebSubmitPriceBuffer getWebDataBuffer(){
		return (WebSubmitPriceBuffer)getDataBuffer();
	}
	
	public WebSubmitPriceUI(){
		super();
		initRef();
	}
	private void initRef(){
		BiddingRefModelForWeb model =  new BiddingRefModelForWeb(getDataBuffer().getCvendorid());
		UIRefPane refpane = (UIRefPane)getBillCardPanel().getHeadItem("cbiddingid").getComponent();
		refpane.setRefModel(model);
	}

	protected void  createDataBuffer(){
		m_dataBuffer = new WebSubmitPriceBuffer(this);	
		String cvendorid = null;
		try {
			cvendorid = PuPubVO.getString_TrimZeroLenAsNull(ZbPubHelper.getCvendoridByLogUser(cl.getUser()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cvendorid = null;
		}
		m_dataBuffer.setCvendorid(cvendorid);
	}

	@Override
	public String getBillType() {
		// TODO Auto-generated method stub
		return ZbPubConst.ZB_WEB_BILLTYPE;
	}

	@Override
	public PricePubEventHandler getEventHandler() {
		if(m_handler == null){
			m_handler = new WebPriceEventHandler(this);
		}
		return m_handler;
	}

	
//	设置倒计时  时：分：秒
	public void setSubTime(long m_timer){
		if(!getWebDataBuffer().isBegin()){
			return;
		}
		BiddingTimesVO circal = getWebDataBuffer().getM_time();
		if(circal == null){
			setHeadValue("tsubtime", "00:00:00");
			return;
		}
		UFDateTime endtime = circal.getTendtime();
		long endMill = endtime.getMillisAfter(getWebDataBuffer().getM_servertime());
		long sub = endMill - m_timer;
		setHeadValue("tsubtime", ZbPubTool.tranMillsToTime(sub));
	}
	public void setCircalInforToHead(boolean isclear){
		if(isclear){
			setHeadValue("ccircalnoid", null);
			setHeadValue("tbegintime", null);
			setHeadValue("tendtime", null);
			setHeadValue("tsubtime", "00:00:00");
			return;
		
		}
		BiddingTimesVO time = getWebDataBuffer().getM_time();
		if(time == null){
			setHeadValue("ccircalnoid", null);
			setHeadValue("tbegintime", null);
			setHeadValue("tendtime", null);
			return;
		}
		
//		setHeadValue("ccircalnoid", ZbPubTool.getString_NullAsTrimZeroLen(time.getCrowno())
//				+":"+ZbPubTool.getString_NullAsTrimZeroLen(time.getVname()));
		setHeadValue("ccircalnoid", ZbPubTool.getString_NullAsTrimZeroLen(time.getVname()));
		setHeadValue("tbegintime", ZbPubTool.getString_NullAsTrimZeroLen(time.getTbigintime()));
		setHeadValue("tendtime", ZbPubTool.getString_NullAsTrimZeroLen(time.getTendtime()));
	}
	
	public boolean beforeEdit(BillEditEvent e) {
		if(e.getKey().equalsIgnoreCase("nprice"))
			if(getWebDataBuffer().isBisfollow())
				return false;
		return true;
	}
	
	public void clearOnRefresh(){
//		清理界面  清理缓存  清理定时器
		getDataPanel().clearBodyData();
		getWebDataBuffer().clearWhenRefresh();
	}

	@Override
	public ButtonObject[] getButtonAry() {
		// TODO Auto-generated method stub
		return new ButtonObject[]{m_btnCommit,m_btnRefresh};
	}
}

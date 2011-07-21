package nc.ui.zb.price.pub;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.price.SubmitPriceVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public abstract class PricePubEventHandler {
	
	protected AbstractPricePubUI ui = null;
	public PricePubEventHandler(AbstractPricePubUI ui){
		super();
		this.ui = ui;
	}
	
	public void onButtonClicked(String btntag) {
		if(btntag.equalsIgnoreCase(ZbPubConst.BTN_TAB_CANCEL)){
			onCancelSubmit();
		}else if(btntag.equalsIgnoreCase(ZbPubConst.BTN_TAB_COMMIT)){
			onCommit();
		}else if(btntag.equalsIgnoreCase(ZbPubConst.BTN_TAB_REFRESH)){
			onRefresh();
		}else if(btntag.equalsIgnoreCase(ZbPubConst.BTN_TAB_FOLLOW)){
			onFollow();
		}
	}
	public void onFollow(){
		
	}
	
	public  void onRefresh(){
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
		ui.getDataPanel().clearBodyData();
		SubmitPriceVO[] prices = null;
		try{
			prices = SubmitPriceHelper.getPriceVos(ui, cbiddingid, null, cvendorid, ui.getDataBuffer().isBisinv(),ZbPubConst.LOCAL_SUBMIT_PRICE);
		}catch(Exception e){
			e.printStackTrace();
			ui.showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
		}
		ui.getDataBuffer().setM_prices(prices);
		if(prices == null || prices.length == 0){
			return;
		}
		ui.setDataToUI();
	
	}
	public abstract Integer getIsubMitType();
	public abstract boolean controlBadPrice() throws Exception;//控制恶意报价
	public abstract Object getUserObject();
	
	//提报
	public boolean onCommit(){
		ui.getBillCardPanel().stopEditing();
		//获取数据 
		try {			
			if(!controlBadPrice())
				return false;
			SubmitPriceHelper.submitPrice(ui,ui.getPriceDatas(),ui.getDataBuffer().isBisinv(),getIsubMitType(),getUserObject());
		} catch (Exception e) {
			e.printStackTrace();
			ui.showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return false;
		}
		ui.getDataPanel().clearBodyData();
		ui.showHintMessage("报价完成");
		return true;
	}
	public void onCancelSubmit(){
		SubmitPriceVO[] datas =  ui.getPriceDatas();
		if(datas == null || datas.length == 0){
			ui.showHintMessage("无数据");
			return;
		}
			
		if (MessageDialog.showYesNoDlg(ui, "取消报价", "是否确认取消本轮报价?") != UIDialog.ID_YES) {
			return;
		}
		try {
			SubmitPriceHelper.cancelSubmitPrice(ui, datas[0].getCbiddingid(),ui.getDataBuffer().getCvendorid(), datas[0].getCcircalnoid(),getIsubMitType());
		} catch (Exception e) {
			e.printStackTrace();
			ui.showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
		}
 	}
	

	public void afterEdit(BillEditEvent e) {
		if(e.getPos() == BillItem.BODY){
			int row = e.getRow();
			String key = e.getKey();
			if("nprice".equalsIgnoreCase(key)){
				SubmitPriceVO price = ui.getPriceDatas()[row];
				if(!(PuPubVO.getUFDouble_NullAsZero(price.getNlastprice()).equals(UFDouble.ZERO_DBL))){
					//					本轮报价必须低于上轮的报价
					if(PuPubVO.getUFDouble_NullAsZero(price.getNlastprice()).doubleValue()<PuPubVO.getUFDouble_NullAsZero(e.getValue()).doubleValue()){
						ui.getDataPanel().setValueAt(null, row, "nprice");
						ui.showErrorMessage("本轮报价不能高于上轮报价");
						return;
					}
				}			

				//将报价同步到缓存
				price.setNprice(PuPubVO.getUFDouble_NullAsZero(e.getValue()));
			}
		}
	}
	
	public String getHeadValue(String headkey){
		return PuPubVO.getString_TrimZeroLenAsNull(ui.getBillCardPanel().getHeadItem(headkey).getValueObject());
	}
	
	public void afterEditWhenBidCode(BillEditEvent e){
		
	}
	
	

	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		
	}
}

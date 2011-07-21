package nc.ui.zb.price.local;

import java.util.Map;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.zb.price.pub.AbstractPriceDataBuffer;
import nc.ui.zb.price.pub.AbstractPricePubUI;
import nc.ui.zb.price.pub.PricePubEventHandler;
import nc.ui.zb.price.pub.SubmitPriceHelper;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.parmset.ParamSetVO;
import nc.vo.zb.price.SubmitPriceVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class LocalPriceEventHandler extends PricePubEventHandler {
	
	public LocalPriceEventHandler(AbstractPricePubUI ui) {
		super(ui);
	}	
	
	public boolean onCommit(){
		boolean flag = super.onCommit();
		if(flag)
		ui.getDataBuffer().setM_prices(null);
		return flag;
	}
	
	public void bodyRowChange(BillEditEvent e) {
//		清空表尾数据
//		clearSel("");
	}
	
	private void afterEditWhenBidding(BillEditEvent e){
//        校验数据  		
		clearTailItem();
		String cbiddingid = PuPubVO.getString_TrimZeroLenAsNull(e.getValue());
		if(cbiddingid == null){//清空标书
			ui.clearData();//清空数据
//			ui.setButtonState(false,false);//封闭按钮
			return;
		}
//		ui.setButtonState(true,false);	
		UIRefPane refpane = (UIRefPane)ui.getBillCardPanel().getHeadItem("cbiddingid").getComponent();
		cbiddingid = refpane.getRefPK();
		
//		UFBoolean bisinvcl = PuPubVO.getUFBoolean_NullAs(getHeadValue("fisinvcl"), UFBoolean.FALSE);

//		设置缓存
		AbstractPriceDataBuffer databuffer = ui.getDataBuffer();
		databuffer.setCbiddingid(cbiddingid);
//		databuffer.setBisinv(!bisinvcl.booleanValue());
		
		String cvendorid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("cvendorid"));
		if(cvendorid == null){
			ui.showHintMessage("请选择本次报价的供应商");
			return;
		}
		
//		设置界面数据
		try {
			SubmitPriceVO[] prices = SubmitPriceHelper.getPriceVos(ui,cbiddingid, databuffer.getCurrentCircalID(),cvendorid, databuffer.isBisinv(),ZbPubConst.LOCAL_SUBMIT_PRICE);
			databuffer.setM_prices(prices);
			ui.setDataToUI();
		} catch (Exception e1) {
			ui.clearData();
			e1.printStackTrace();
			ui.showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e1.getMessage()));
		}
	}
	
	private void afterEditWhenVendor(BillEditEvent e){
//		供应商选择后   刷新表体
		clearTailItem();
		
		UIRefPane refpane = (UIRefPane)ui.getBillCardPanel().getHeadItem("cvendorid").getComponent();
		String cvendorid = PuPubVO.getString_TrimZeroLenAsNull(refpane.getRefPK());
		if(cvendorid == null){
			//清空标书
			ui.clearData();//清空数据
//			ui.setButtonState(false);//封闭按钮
			return;
		}
		
		String cbiddingid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("cbiddingid"));
		AbstractPriceDataBuffer databuffer = ui.getDataBuffer();
//		清空价格信息  界面 及 缓存
		databuffer.setCvendorid(cvendorid);
		
		if(cbiddingid == null)
			return;		
		
//		设置界面数据
		try {
			SubmitPriceVO[] prices = SubmitPriceHelper.getPriceVos(ui,cbiddingid, databuffer.getCurrentCircalID(),cvendorid, databuffer.isBisinv(),ZbPubConst.LOCAL_SUBMIT_PRICE);
			for(SubmitPriceVO price:prices){
				price.setCvendorid(cvendorid);
				price.setIsubmittype(ZbPubConst.LOCAL_SUBMIT_PRICE);
			}
			databuffer.setM_prices(prices);
			ui.setDataToUI();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			ui.setHeadValue("cvendorid",null);
		    ui.getDataBuffer().setCvendorid(null);
		    ui.getDataPanel().clearBodyData();
			e1.printStackTrace();
			ui.showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e1.getMessage()));
		}	
	}
	
	private Object getTailItemValue(String key){
		return ui.getBillCardPanel().getTailItem(key).getValueObject();
	}
	
	private void clearSel(String key){
		BillItem[] items = ui.getBillCardPanel().getTailItems();
		for(BillItem item:items){
			if(item.getKey().equalsIgnoreCase(ZbPubTool.getString_NullAsTrimZeroLen(key))||item.getKey().equalsIgnoreCase("nadjust"))
				continue;
			item.setValue(UFBoolean.FALSE);
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(key)==null)
			ui.getBillCardPanel().getTailItem("nadjust").setValue(null);
	}
	
	private String getTailSelKey(){
		BillItem[] items = ui.getBillCardPanel().getTailItems();
		for(BillItem item:items){
			if(item.getKey().equalsIgnoreCase("nadjust")){
				continue;
			}
			if(PuPubVO.getUFBoolean_NullAs(item.getValueObject(), UFBoolean.FALSE).booleanValue())
				return item.getKey();
		}
		return null;
	}

	private void afterEditOnTail(BillEditEvent e){
		flag=true;
		int rowCount = ui.getBillCardPanel().getBillTable().getRowCount();
		if(rowCount < 0){
//			clearSel(null);//选择全部清空
			ui.showWarningMessage("表体数据不存在");			
			return;
		}
		String key = e.getKey();
		
        if("nadjust".equalsIgnoreCase(key)){
        	String selKey = getTailSelKey();
        	if(selKey == null)
        		return;
        	UFBoolean isSel = PuPubVO.getUFBoolean_NullAs(getTailItemValue(selKey), UFBoolean.FALSE);
    		if(!isSel.booleanValue())
    			return;	
    		key = selKey;
		}else{
			UFBoolean isSel = PuPubVO.getUFBoolean_NullAs(getTailItemValue(key), UFBoolean.FALSE);
			if(!isSel.booleanValue())
				return;		
			
			clearSel(key);
		}
		
		
		UFDouble nadjust = PuPubVO.getUFDouble_NullAsZero(getTailItemValue("nadjust"));
//		if(nadjust.equals(UFDouble.ZERO_DBL))
//			nadjust = new UFDouble(100);
		BillEditEvent e2 = null;
		
		UFDouble nprice = UFDouble.ZERO_DBL;
		for(int i=0;i<rowCount;i++){
			SubmitPriceVO price  = ui.getDataBuffer().getM_prices()[i];
			if("isplan".equalsIgnoreCase(key)){
				nprice = PuPubVO.getUFDouble_NullAsZero(price.getNplanprice()).multiply(nadjust.div(100).add(1));
			}else if("ismarket".equalsIgnoreCase(key)){
				nprice = PuPubVO.getUFDouble_NullAsZero(price.getNmarketprice()).multiply(nadjust.div(100).add(1));
			}else if("ishistory".equalsIgnoreCase(key)){
				nprice = PuPubVO.getUFDouble_NullAsZero(price.getNaverageprice()).multiply(nadjust.div(100).add(1));
			}else if("islow".equalsIgnoreCase(key)){
				nprice = PuPubVO.getUFDouble_NullAsZero(price.getNllowerprice()).multiply(nadjust.div(100).add(1));
			}else if("issta".equalsIgnoreCase(key)){
				nprice = PuPubVO.getUFDouble_NullAsZero(price.getNmarkprice()).multiply(nadjust.div(100).add(1));
			}
			ui.getBillCardPanel().getBillModel().setValueAt(nprice, i, "nprice");		
			e2 = new BillEditEvent(ui.getBillCardPanel().getBillModel(),null,nprice,"nprice",i,BillItem.BODY);
			afterEdit(e2);
		}
		
	}
	private boolean flag = true;
	public void afterEdit(BillEditEvent e) {
		String key = e.getKey();
		if(e.getPos() == BillItem.HEAD){
			if("bistemp".equalsIgnoreCase(key)){
				ui.getBillCardPanel().getHeadItem("cvendorid").setValue(null);
				ui.getDataPanel().clearBodyData();//清空数据
			}else if(key.equalsIgnoreCase("cbiddingid")){//标书选择后
				afterEditWhenBidding(e);
			}else if("cvendorid".equalsIgnoreCase(key)){//供应商选择后
				afterEditWhenVendor(e);
			}else 
			afterEditOnTail(e);
		}else if(e.getPos() == BillItem.BODY){
			int row = e.getRow();
			if("nprice".equalsIgnoreCase(key)){
				SubmitPriceVO price = ui.getPriceDatas()[row];
				if(!(PuPubVO.getUFDouble_NullAsZero(price.getNlastprice()).equals(UFDouble.ZERO_DBL))){
					//					本轮报价必须低于上轮的报价
					if(PuPubVO.getUFDouble_NullAsZero(price.getNlastprice()).doubleValue()<PuPubVO.getUFDouble_NullAsZero(e.getValue()).doubleValue()){
						ui.getDataPanel().setValueAt(null, row, "nprice");
						if(flag)
						  ui.showErrorMessage("本轮报价不能高于上轮报价");
						flag = false;
						return;
					}
				}			

				//将报价同步到缓存
				price.setNprice(PuPubVO.getUFDouble_NullAsZero(e.getValue()));
			}
		}
//		super.afterEdit(e);
		if(PuPubVO.getString_TrimZeroLenAsNull(ui.getDataBuffer().getCbiddingid())==null&&PuPubVO.getString_TrimZeroLenAsNull(ui.getDataBuffer().getCvendorid())==null){
			ui.setButtonState(false);
		}else
			ui.setButtonState(true);
	}

	@Override
	public Integer getIsubMitType() {
		// TODO Auto-generated method stub
		return ZbPubConst.LOCAL_SUBMIT_PRICE;
	}



	@Override
	public boolean controlBadPrice() throws Exception {
		// TODO Auto-generated method stub
		ParamSetVO para = ui.getDataBuffer().getPara();
		boolean iscontrol = PuPubVO.getUFBoolean_NullAs(para.getFisbadquotation(),UFBoolean.FALSE).booleanValue();
		if(!iscontrol )//|| para.getIbadquotanum() == null
			return true;
		else{
			SubmitPriceVO[] prices = ui.getDataBuffer().getM_prices();
			
			SubmitPriceHelper.validationDataOnSubmit(prices, ui.getDataBuffer().isBisinv(), ZbPubConst.LOCAL_SUBMIT_PRICE);
//			List<SubmitPriceVO> lprice = new ArrayList<SubmitPriceVO>();
			UFDouble nminprice = null;//最低价
			UFDouble nbiddingprice = null;//标底价
			Map<String, UFDouble> minPriceInfor = ui.getDataBuffer().getInvPriceInfor();
			if(minPriceInfor == null || minPriceInfor.size() == 0)
				throw new ValidationException("该标段品种的标底价信息不存在");
			
			for(SubmitPriceVO price:prices){
				nbiddingprice = PuPubVO.getUFDouble_NullAsZero(
						minPriceInfor.get(price.getCinvbasid()));

				nminprice = SubmitPriceVO.getMinPrice(nbiddingprice, PuPubVO.getUFDouble_NullAsZero(para.getNquotationlower()));
				if(price.getNprice().doubleValue()<nminprice.doubleValue()){
					ui.showErrorMessage("恶意报价");
						return false;
				}
			}
		}
		
		
		return true;
	}



	@Override
	public Object getUserObject() {
		// TODO Auto-generated method stub
		return null;
	}

	//清空表尾
	private void clearTailItem(){
		BillItem[] items = ui.getBillCardPanel().getTailItems();
		for(BillItem item:items){
			item.setValue(null);
		}
	}

}

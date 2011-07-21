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
	public abstract boolean controlBadPrice() throws Exception;//���ƶ��ⱨ��
	public abstract Object getUserObject();
	
	//�ᱨ
	public boolean onCommit(){
		ui.getBillCardPanel().stopEditing();
		//��ȡ���� 
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
		ui.showHintMessage("�������");
		return true;
	}
	public void onCancelSubmit(){
		SubmitPriceVO[] datas =  ui.getPriceDatas();
		if(datas == null || datas.length == 0){
			ui.showHintMessage("������");
			return;
		}
			
		if (MessageDialog.showYesNoDlg(ui, "ȡ������", "�Ƿ�ȷ��ȡ�����ֱ���?") != UIDialog.ID_YES) {
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
					//					���ֱ��۱���������ֵı���
					if(PuPubVO.getUFDouble_NullAsZero(price.getNlastprice()).doubleValue()<PuPubVO.getUFDouble_NullAsZero(e.getValue()).doubleValue()){
						ui.getDataPanel().setValueAt(null, row, "nprice");
						ui.showErrorMessage("���ֱ��۲��ܸ������ֱ���");
						return;
					}
				}			

				//������ͬ��������
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

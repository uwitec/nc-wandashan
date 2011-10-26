package nc.ui.hg.so.plugin;

/**
 * zhf 2010-12-16 销售订单二次开发ui扩展插件
 */

import java.awt.event.ActionEvent;

import nc.ui.hg.so.pub.HgSoPubHealper;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.scm.plugin.IScmUIPlugin;
import nc.ui.scm.plugin.SCMUIContext;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.plugin.Action;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;

public class SaleOrderUIPlugin implements IScmUIPlugin {

	public void afterAction(Action action, AggregatedValueObject[] billvos,
			SCMUIContext conx) throws BusinessException {
		// TODO Auto-generated method stub

	}

	public void afterButtonClicked(ButtonObject bo, SCMUIContext ctx)
			throws BusinessException {
		// TODO Auto-generated method stub

	}

	public void afterEdit(BillEditEvent e, SCMUIContext ctx) {
		// TODO Auto-generated method stub
		
		//销售订单批次号编辑后进行询价处理
		if(e.getPos() == BillItem.HEAD)
			return;
		
		//表体编辑后事件处理
		int row = e.getRow();
		String key = e.getKey();
		BillModel bm = ctx.getBillCardPanel().getBillModel();
		if(e.getKey().equals("cbatchid")){//批次
			String cinvmanid = PuPubVO.getString_TrimZeroLenAsNull(bm.getValueAt(row, "cinventoryid"));
			String cbatchid = PuPubVO.getString_TrimZeroLenAsNull(bm.getValueAt(row, "cbatchid"));
			if(cbatchid == null){
				return;
			}
			if(cinvmanid == null){
				return;
			}
			UFDouble[] pricedouble = null;
			try{
				pricedouble = HgSoPubHealper.callPurchasePactPrice(cinvmanid, cbatchid);
			}catch(Exception ee){
				ee.printStackTrace();
				ctx.getToftPanel().showErrorMessage(HgPubTool.getString_NullAsTrimZeroLen(ee.getMessage()));
				return;
			}
			if(pricedouble == null||pricedouble.length==0)
				return;
			
//			setCardPanelBodyValue(bm, "nexchangeotobrate", 1, row);//汇率设置为1
			
			//UFDouble nrate = pricedouble[0];
			UFDouble ntaxprice = pricedouble[1].multiply(HgSoPubHealper.getSaleOrderPriceAddRate(), HgPubConst.PRICE_DIGIT);
			
			//setCardPanelBodyValue(bm, "ntaxrate", nrate,row);
			setCardPanelBodyValue(bm, "noriginalcurprice", ntaxprice,row);	//原币无税单价
			
			//联动运算
//			String key = (uipanel.SA_02.booleanValue() ? "noriginalcurtaxprice" : "noriginalcurprice");
			int[] para = SaleorderBVO.getCalculatePara(key, null, UFBoolean.FALSE, "调整单价");			
			RelationsCal.calculate(row, null, ctx.getBillCardPanel(), para, "noriginalcurprice", SaleorderBVO.getKeys(), SaleorderBVO
					.getField(), SaleorderBVO.class.getName(), SaleorderHVO.class.getName(), null);
			
		}		
	}
	
	private void setCardPanelBodyValue(BillModel bm,String itemkey,Object oValue,int row){
		bm.setValueAt(oValue, row,itemkey);
	}

	public void afterSetBillVOToCard(AggregatedValueObject billvo,
			SCMUIContext conx) {
		// TODO Auto-generated method stub

	}

	public void afterSetBillVOsToListBody(
			CircularlyAccessibleValueObject[] bodyvos, SCMUIContext conx) {
		// TODO Auto-generated method stub

	}

	public void afterSetBillVOsToListHead(
			CircularlyAccessibleValueObject[] headvos, SCMUIContext conx) {
		// TODO Auto-generated method stub

	}

	public void beforeAction(Action action, AggregatedValueObject[] billvos,
			SCMUIContext conx) throws BusinessException {
		// TODO Auto-generated method stub

	}

	public void beforeButtonClicked(ButtonObject bo, SCMUIContext ctx)
			throws BusinessException {
		// TODO Auto-generated method stub

	}

	public boolean beforeEdit(BillEditEvent e, SCMUIContext ctx) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean beforeEdit(BillItemEvent e, SCMUIContext conx) {
		// TODO Auto-generated method stub
		return false;
	}

	public AggregatedValueObject[] beforePrint(
			AggregatedValueObject[] printVOs, SCMUIContext conx) {
		// TODO Auto-generated method stub
		return null;
	}

	public void beforeSetBillVOToCard(AggregatedValueObject billvo,
			SCMUIContext conx) {
		// TODO Auto-generated method stub

	}

	public void beforeSetBillVOsToListBody(
			CircularlyAccessibleValueObject[] bodyvos, SCMUIContext conx) {
		// TODO Auto-generated method stub

	}

	public void beforeSetBillVOsToListHead(
			CircularlyAccessibleValueObject[] headvos, SCMUIContext conx) {
		// TODO Auto-generated method stub

	}

	public void bodyRowChange(BillEditEvent e, SCMUIContext ctx) {
		// TODO Auto-generated method stub

	}

	public boolean init(SCMUIContext ctx) {
		// TODO Auto-generated method stub
		return false;
	}

	public void mouse_doubleclick(BillMouseEnent e, SCMUIContext ctx) {
		// TODO Auto-generated method stub

	}

	public void onAddLine(SCMUIContext conx) throws BusinessException {
		// TODO Auto-generated method stub

	}

	public void onMenuItemClick(ActionEvent e, SCMUIContext ctx) {
		// TODO Auto-generated method stub

	}

	public void onPastLine(SCMUIContext conx) throws BusinessException {
		// TODO Auto-generated method stub

	}

	public String onQuery(String swhere, SCMUIContext conx)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object[] retBillToBillRefVOs(
			CircularlyAccessibleValueObject[] headVos,
			CircularlyAccessibleValueObject[] bodyVos) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public AggregatedValueObject[] retChangeBusiVOs(
			AggregatedValueObject[] preVo, AggregatedValueObject[] nowVo)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setButtonStatus(SCMUIContext conx) {
		// TODO Auto-generated method stub

	}
	
	

}

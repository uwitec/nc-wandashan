package nc.ui.zb.pu.plugin;

import java.awt.event.ActionEvent;

import nc.ui.pi.invoice.IButtonConstInv;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.scm.plugin.IScmUIPlugin;
import nc.ui.scm.plugin.SCMUIContext;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.plugin.Action;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.pub.ZbPubConst;

public class PoOrderUIPlugin  implements IScmUIPlugin {

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
		// TODO Auto-generated method stub\getPoCardPanel().setBodyMenuShow("table",false);

			String sourceType = PuPubVO.getString_TrimZeroLenAsNull(
					ctx.getBillCardPanel().getBillModel().getValueAt(0,"cupsourcebilltype"));
			if(sourceType==null)
				sourceType= PuPubVO.getString_TrimZeroLenAsNull(ctx.getBillListPanel().getBodyBillModel().getValueAt(0, "cupsourcebilltype"));
				
			if (bo.getCode().equals(IButtonConstInv.BTN_LINE_ADD)||bo.getCode().equals(IButtonConstInv.BTN_LINE_DELETE)
					||bo.getCode().equals(IButtonConstInv.BTN_LINE_COPY)||bo.getCode().equals(IButtonConstInv.BTN_LINE_INSERT)
					||bo.getCode().equals(IButtonConstInv.BTN_LINE_PASTE)|| bo.getCode().equals(IButtonConstInv.BTN_LINE_PASTE_TAIL)
					||bo.getCode().equals(IButtonConstInv.BTN_ADD_NEWROWNO) || bo.getCode().equals(IButtonConstInv.BTN_CARDEDIT)) {
				
				if(ZbPubConst.ZB_Result_BILLTYPE.equalsIgnoreCase(sourceType))
					throw new BusinessException("不能进行行操作");
			}else if(bo.getCode().equalsIgnoreCase(IButtonConstInv.BTN_BILL_EDIT)){
				if(ZbPubConst.ZB_Result_BILLTYPE.equalsIgnoreCase(sourceType))
					ctx.getBillCardPanel().getHeadItem("cvendormangid").setEnabled(false);
			}
	}

	public boolean beforeEdit(BillEditEvent e, SCMUIContext ctx) {
		// TODO Auto-generated method stub
		String key = e.getKey();
//		UFDouble nversion = PuPubVO.getUFDouble_NullAsZero((ctx.getBillCardPanel().getHeadItem("nversion").getValueObject()));
		String sourceType = PuPubVO.getString_TrimZeroLenAsNull(
				ctx.getBillCardPanel().getBillModel().getValueAt(0,"cupsourcebilltype"));
		if(sourceType==null)
			sourceType= PuPubVO.getString_TrimZeroLenAsNull(ctx.getBillListPanel().getBodyBillModel().getValueAt(0, "cupsourcebilltype"));
		
		if(e.getPos()==BillItem.BODY){
//			if(nversion.compareTo(UFDouble.ONE_DBL)!=0)
//				return true;
			if("nordernum".equalsIgnoreCase(key)||"cinventorycode".equalsIgnoreCase(key)
					||"noriginalcurmny".equalsIgnoreCase(key)||"idiscounttaxtype".equalsIgnoreCase(key)
					||"ntaxrate".equalsIgnoreCase(key)||"noriginaltaxmny".equalsIgnoreCase(key)||"noriginaltaxpricemny".equalsIgnoreCase(key)){
				if(ZbPubConst.ZB_Result_BILLTYPE.equalsIgnoreCase(sourceType))
					return false;
			}
		}
		return true;
	}

	public boolean beforeEdit(BillItemEvent e, SCMUIContext conx) {
		
		return true;
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
		ctx.getBillCardPanel().setBodyMenuShow(false);
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

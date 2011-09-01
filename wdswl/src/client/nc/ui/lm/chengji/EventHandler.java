package nc.ui.lm.chengji;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.wl.pub.WdsWlPubConst;
public class EventHandler extends ManageEventHandler
{
	public EventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);		
	}
	@Override
	protected UIDialog createQueryUI() {
		return new QueryDig(getBillUI(), null, getBillUI()._getCorp().getPrimaryKey(), WdsWlPubConst.LM_CHENGJI_NODECODE, getBillUI()._getOperator(), null);
	}
}

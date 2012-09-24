package nc.ui.wds.invcl;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.trade.treecard.TreeCardEventHandler;
import nc.ui.zmpub.pub.tool.LongTimeTask;
import nc.vo.pub.AggregatedValueObject;
/**
 * @author mlr
 */
public class Event extends TreeCardEventHandler{

	public Event(BillTreeCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	
	@Override
    public void onBoDelete() throws Exception{
		AggregatedValueObject modelVo = getBufferData().getCurrentVO();
		Class[] ParameterTypes = new Class[] { AggregatedValueObject.class };
		Object[] ParameterValues = new Object[] { modelVo };
		Object o = LongTimeTask.calllongTimeService("wds", null,
				"ÕýÔÚ²éÑ¯...", 1, "nc.vo.wdsnew.pub.BaseDocValuteTool", null, "valuteInvclDocDelete",
				ParameterTypes, ParameterValues);
	
    	super.onBoDelete();
    }
}

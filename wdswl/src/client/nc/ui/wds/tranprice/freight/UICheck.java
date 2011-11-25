package nc.ui.wds.tranprice.freight;

import java.awt.Container;

import nc.lm.ui.classinfor.ClientUI;
import nc.ui.pub.pf.IUIBeforeProcAction;
import nc.vo.pub.AggregatedValueObject;

public class UICheck implements IUIBeforeProcAction{
	public void runBatchClass(Container parent, String billType,
			String actionName, AggregatedValueObject[] vos, Object[] obj)
			throws Exception {		
	}
	public void runClass(Container parent, String billType, String actionName,
			AggregatedValueObject vo, Object obj) throws Exception {
	  if("SAVE".equalsIgnoreCase(actionName)){	   
	    if(parent==null)
	    	return;	  
		ClientUI ui=(ClientUI) parent;
	    nc.ui.wl.pub.BeforeSaveValudate.beforeSaveBodyUnique(ui.getBillCardPanel().
	    		getBillTable(),ui.getBillCardPanel().getBillModel(),
	    		new String[]{"invcode"},new String[]{"´æ»õ±àÂë"});  
	 }
	}
}


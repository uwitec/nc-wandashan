package nc.lm.ui.classinfor;
import java.awt.Container;
import nc.ui.pub.pf.IUIBeforeProcAction;
import nc.vo.pub.AggregatedValueObject;
/**
 * 
 * @author mlr 
 * 前台校验类 动作执行前 运行runClass方法
 * 传来的 AggregatedValueObject 对象  是通过
 * getBillUI().getVOFromUI() 得到的聚合vo
 * 
 */
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
		 //校验必须项
		nc.ui.wl.pub.BeforeSaveValudate.dataNotNullValidate(ui.getBillCardPanel());					
		//表体不为空
		nc.ui.wl.pub.BeforeSaveValudate.BodyNotNULL(ui.getBillCardPanel().getBillTable());
	    //表体学号不允许重复	 
	    nc.ui.wl.pub.BeforeSaveValudate.beforeSaveBodyUnique(ui.getBillCardPanel().getBillTable(),ui.getBillCardPanel().getBillModel(),new String[]{"ccstucode"},new String[]{"学号"});  
	 }
	}
}

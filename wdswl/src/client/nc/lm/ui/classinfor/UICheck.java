package nc.lm.ui.classinfor;
import java.awt.Container;
import nc.ui.pub.pf.IUIBeforeProcAction;
import nc.vo.pub.AggregatedValueObject;
/**
 * 
 * @author mlr 
 * ǰ̨У���� ����ִ��ǰ ����runClass����
 * ������ AggregatedValueObject ����  ��ͨ��
 * getBillUI().getVOFromUI() �õ��ľۺ�vo
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
		 //У�������
		nc.ui.wl.pub.BeforeSaveValudate.dataNotNullValidate(ui.getBillCardPanel());					
		//���岻Ϊ��
		nc.ui.wl.pub.BeforeSaveValudate.BodyNotNULL(ui.getBillCardPanel().getBillTable());
	    //����ѧ�Ų������ظ�	 
	    nc.ui.wl.pub.BeforeSaveValudate.beforeSaveBodyUnique(ui.getBillCardPanel().getBillTable(),ui.getBillCardPanel().getBillModel(),new String[]{"ccstucode"},new String[]{"ѧ��"});  
	 }
	}
}

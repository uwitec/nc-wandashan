package nc.ui.wds.ie.storepersons;

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
	
		  vo.getParentVO();
	}

}

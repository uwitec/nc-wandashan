package nc.ui.wds.w80060604;

import nc.ui.ic.pub.bill.query.ICheckCondition;
import nc.ui.pub.query.QueryConditionClient;
import nc.vo.pub.query.ConditionVO;

public class Test extends QueryConditionClient{
private ICheckCondition m_iCheck=null;
	
	public void addICheckCondition(ICheckCondition check){
		m_iCheck=check;
		
	}
	public String checkCondition(ConditionVO[] conditions) {
		String err=super.checkCondition(conditions);
		if(err!=null)
			return err;
		
		if(m_iCheck!=null)
			return m_iCheck.checkICCondition(conditions);
		
	return null;
		
	}

}

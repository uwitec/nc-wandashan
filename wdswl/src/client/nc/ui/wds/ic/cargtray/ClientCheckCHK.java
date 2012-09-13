package nc.ui.wds.ic.cargtray;

import java.awt.Container;

import nc.ui.trade.check.BeforeActionCHK;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.trade.checkrule.IUniqueRule;
import nc.vo.trade.checkrule.VOChecker;


public class ClientCheckCHK extends BeforeActionCHK {

	public void runBatchClass(Container parent, String billType,
			String actionName, AggregatedValueObject[] vos, Object[] obj)
			throws Exception {
		
		
	}

	public void runClass(Container parent, String billType, String actionName,
			AggregatedValueObject vo, Object obj) throws Exception {
		
		if(actionName.equalsIgnoreCase("WRITE")){
			if(vo==null){
				return;
			}
			if(vo.getChildrenVO()==null || vo.getChildrenVO().length==0){
				return;
			}			
		boolean isCheckPass= VOChecker.checkUniqueRule(vo.getChildrenVO(), new IUniqueRule(){

			public String[] getFields() {
				
				return new String[]{"cdt_traycode"};
			}

			public String getHint() {
				
				return "货架位置编码 不允许重复";
			}			   
		   });
		if(!isCheckPass){
			
			throw new BusinessException("货架位置编码 不允许重复");
		}
		
		}
		
	}

}

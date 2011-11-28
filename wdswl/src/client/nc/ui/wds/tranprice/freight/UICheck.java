package nc.ui.wds.tranprice.freight;

import java.awt.Container;

import nc.lm.ui.classinfor.ClientUI;
import nc.ui.pub.pf.IUIBeforeProcAction;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.wds.tranprice.freight.ZhbzBVO;

public class UICheck implements IUIBeforeProcAction{
	public void runBatchClass(Container parent, String billType,
			String actionName, AggregatedValueObject[] vos, Object[] obj)
			throws Exception {		
	}
	public void runClass(Container parent, String billType, String actionName,
			AggregatedValueObject vo, Object obj) throws Exception {
	  if("WRITE".equalsIgnoreCase(actionName)){	   
	    if(parent==null)
	    	return;	  
		
	   }
	  ZhbzBVO[] body= (ZhbzBVO[]) vo.getChildrenVO();
	  if(body==null||body.length==0){
		  throw new BusinessException("表体信息不能为空");
	  }
	  for(int i=0;i<body.length;i++){
		String invmandoc = body[i].getPk_invmandoc();
		for(int j=i+1;j<body.length;j++){
			String invmandoc1 = body[j].getPk_invmandoc();
			if(invmandoc.equalsIgnoreCase(invmandoc1)){
				 throw new BusinessException("表体存货信息不能重复");
			}
		}
	  }
	}
}


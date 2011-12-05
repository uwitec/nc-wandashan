package nc.ui.wds.dm.storebing;

import java.awt.Container;

import nc.ui.pub.pf.IUIBeforeProcAction;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.wds.dm.storebing.TbStorcubasdocVO;

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
	  TbStorcubasdocVO[] body= (TbStorcubasdocVO[]) vo.getChildrenVO();
	  if(body==null||body.length==0){
		  throw new BusinessException("表体信息不能为空");
	  }
	  for(int i=0;i<body.length;i++){
		String cumandoc = body[i].getPk_cumandoc();
		String pk_tordoc1 =body[i].getPk_stordoc1();
		if(cumandoc==null || cumandoc.length()==0){
			if(pk_tordoc1==null || pk_tordoc1.length()==0){
				 throw new BusinessException("表体中客商或者分仓信息不能为空");
			}
		}
		for(int j=i+1;j<body.length;j++){
			String cumandoc2 = body[j].getPk_cumandoc();
			String pk_tordoc2 =body[j].getPk_stordoc1();
			if(cumandoc!=null&&cumandoc.length()>0){
				if(cumandoc2!=null&&cumandoc2.length()>0){
					if(cumandoc.equalsIgnoreCase(cumandoc2)){
						 throw new BusinessException("表体客商信息不能重复");
					}
				}
			}
			if(pk_tordoc1!=null&&pk_tordoc1.length()>0){
				if(pk_tordoc2!=null&&pk_tordoc2.length()>0){
					if(pk_tordoc1.equalsIgnoreCase(pk_tordoc2)){
						 throw new BusinessException("表体分仓信息不能重复");
					}
				}
			}
			
		}
	  }
	}
}


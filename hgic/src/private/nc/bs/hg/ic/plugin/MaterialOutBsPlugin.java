package nc.bs.hg.ic.plugin;

import nc.bs.hg.scm.pub.HgScmPubBO;
import nc.bs.scm.plugin.IScmBSPlugin;
import nc.bs.scm.plugin.SCMBsContext;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.plugin.Action;

public class MaterialOutBsPlugin implements IScmBSPlugin {
	
	public void beforeAction(Action action, AggregatedValueObject[] billvos,
			Object otherparam, SCMBsContext conx) throws BusinessException {
		// TODO Auto-generated method stub
		if(billvos == null||billvos.length==0)
			return;
		String loginCorp =conx.getLoginCorp();//登录公司
		GeneralBillVO[] gbillvos = (GeneralBillVO[])billvos;
		HgScmPubBO checkbo = new HgScmPubBO();
		if(action == Action.SAVE){
			//zhf add   资金控制
			for(GeneralBillVO gbillvo:gbillvos){
				checkbo.checkAndUseFundForMaterialOut(gbillvo,loginCorp, false);
			}
			//zhf end
		}else if(action == Action.DELETE){
			for(GeneralBillVO gbillvo:gbillvos){
				checkbo.checkAndUseFundForMaterialOut(gbillvo,loginCorp,true);
			}
		}
			// modify  by zhw  2010-12-28 end
	}

	public void afterAction(Action action, AggregatedValueObject[] billvos,
			Object otherparam, SCMBsContext conx) throws BusinessException {
		
	}

	public AggregatedValueObject[] checkOutVO(AggregatedValueObject[] avos)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public AggregatedValueObject[] retChangeBusiVOs(
			AggregatedValueObject[] preVo, AggregatedValueObject[] nowVo)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

}

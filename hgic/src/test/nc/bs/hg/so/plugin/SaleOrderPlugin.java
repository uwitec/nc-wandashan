package nc.bs.hg.so.plugin;

import nc.bs.hg.scm.pub.HgScmPubBO;
import nc.bs.scm.plugin.IScmBSPlugin;
import nc.bs.scm.plugin.SCMBsContext;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.plugin.Action;
import nc.vo.so.so001.SaleOrderVO;

public class SaleOrderPlugin implements IScmBSPlugin {

	public void afterAction(Action action, AggregatedValueObject[] billvos,
			Object otherparam, SCMBsContext conx) throws BusinessException {

		if(billvos==null||billvos.length==0)
			return;
		SaleOrderVO[] svos = new SaleOrderVO[billvos.length];
		int index = 0;
		for(AggregatedValueObject billvo:billvos){
			svos[index] =  (SaleOrderVO)billvo;
			index ++;
		}
		// modify zhw  2011-01-10   需求变更  外销不需要预扣
		HgScmPubBO bo = new HgScmPubBO();
		if(action == Action.AUDIT){//审批通过  进行预扣  含税金额
//			for(SaleOrderVO svo:svos){
//				bo.checkAndUseFund_before(svo, true);
//			}
		}else if(action == Action.UNAUDIT){//取消预扣
//			for(SaleOrderVO svo:svos){
//				bo.checkAndUseFund_before(svo, false);
//			}
		}
	}

	public void beforeAction(Action action, AggregatedValueObject[] billvos,
			Object otherparam, SCMBsContext conx) throws BusinessException {
		// TODO Auto-generated method stub

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

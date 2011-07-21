package nc.bs.hg.so.plugin;

import nc.bs.hg.scm.pub.HgScmPubBO;
import nc.bs.scm.plugin.IScmBSPlugin;
import nc.bs.scm.plugin.SCMBsContext;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.plugin.Action;
import nc.vo.so.so001.SaleOrderVO;

public class SaleOrderPlugin implements IScmBSPlugin {

	UFDouble  beforeMny= UFDouble.ZERO_DBL;
	public void afterAction(Action action, AggregatedValueObject[] billvos,
			Object otherparam, SCMBsContext conx) throws BusinessException {

	}

	public void beforeAction(Action action, AggregatedValueObject[] billvos,
			Object otherparam, SCMBsContext conx) throws BusinessException {
		// TODO Auto-generated method stub
		if(billvos==null||billvos.length==0)
			return;
		String loginCorp = conx.getLoginCorp();
		SaleOrderVO[] svos = new SaleOrderVO[billvos.length];
		int index = 0;
		for(AggregatedValueObject billvo:billvos){
			svos[index] =  (SaleOrderVO)billvo;
			index ++;
		}
		// modify zhw  2011-01-10   需求变更  外销不需要预扣
		HgScmPubBO bo = new HgScmPubBO();
		if(action == Action.SAVE){//获取保存前的修改金额
			for(SaleOrderVO svo:svos){
				beforeMny=bo.getNMY(svo.getPrimaryKey(),svo.getCbilltypecode());
				bo.checkAndUseFund_before(svo, false,loginCorp,beforeMny);
			}
		}else if(action == Action.DELETE){//取消预扣
			for(SaleOrderVO svo:svos){
				bo.checkAndUseFund_before(svo, true,loginCorp,beforeMny);
			}
		}
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

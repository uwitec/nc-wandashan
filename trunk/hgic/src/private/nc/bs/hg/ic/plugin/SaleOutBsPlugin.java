package nc.bs.hg.ic.plugin;

import nc.bs.hg.scm.pub.HgScmPubBO;
import nc.bs.scm.plugin.IScmBSPlugin;
import nc.bs.scm.plugin.SCMBsContext;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.plugin.Action;

public class SaleOutBsPlugin implements IScmBSPlugin {
	
	public void beforeAction(Action action, AggregatedValueObject[] billvos,
			Object otherparam, SCMBsContext conx) throws BusinessException {
		// TODO Auto-generated method stub
		if(billvos == null||billvos.length==0)
			return;
		String loginCorp =conx.getLoginCorp();//��¼��˾
		HgScmPubBO checkbo = new HgScmPubBO();
		if(action == Action.SAVE){
			//zhf add   �����ݲ����   ֻ���Ͽ���
			checkbo.checkAndUseFundForSaleOut((GeneralBillVO)billvos[0], false, loginCorp);
			//zhf end
		}else if(action == Action.DELETE){
			checkbo.checkAndUseFundForSaleOut((GeneralBillVO)billvos[0], true, loginCorp);
		}
//zhw end 2011-01-08
	}

	public void afterAction(Action action, AggregatedValueObject[] billvos,
			Object otherparam, SCMBsContext conx) throws BusinessException {
		// TODO Auto-generated method stub
		if(action == Action.AUDIT){
			//�ر����۶���
		}else if(action == Action.UNAUDIT){
			//�����۶���
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

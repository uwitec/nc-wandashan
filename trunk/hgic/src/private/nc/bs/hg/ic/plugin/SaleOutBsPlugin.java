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
		GeneralBillVO[] gbillvos = (GeneralBillVO[])billvos;
		HgScmPubBO bo = new HgScmPubBO();
		//modify  by zhw 2010-01-08  ���۳��ⲻ��Ҫ�ʽ� �޶����
//		if(action==Action.AUDIT){//���ǩ��		
//			for(GeneralBillVO gbillvo:gbillvos){
//				bo.checkAndUseFundForSaleOut(gbillvo, true);
//			}
//		}else if(action == Action.UNAUDIT){//���ȡ��ǩ��
//			for(GeneralBillVO gbillvo:gbillvos){
//				bo.checkAndUseFundForSaleOut(gbillvo, false);
//			}
//		}else 
		if(action == Action.SAVE){
			//zhf add   �����ݲ����   ֻ���Ͽ���
			HgScmPubBO checkbo = new HgScmPubBO();
			checkbo.checkAllowanceForICout((GeneralBillVO)billvos[0]);
			//zhf end
		}else if(action == Action.DELETE){

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

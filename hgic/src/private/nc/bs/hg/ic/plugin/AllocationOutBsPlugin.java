package nc.bs.hg.ic.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.hg.scm.pub.HgScmPubBO;
import nc.bs.scm.plugin.IScmBSPlugin;
import nc.bs.scm.plugin.SCMBsContext;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.plugin.Action;
import nc.vo.scm.pu.PuPubVO;

public class AllocationOutBsPlugin implements IScmBSPlugin {
	
	HgScmPubBO bo = null;
	private HgScmPubBO getScmPubBO(){
		if(bo == null){
			bo = new HgScmPubBO();
		}
		return bo;
	}
	public void beforeAction(Action action, AggregatedValueObject[] billvos,
			Object otherparam, SCMBsContext conx) throws BusinessException {
		// TODO Auto-generated method stub
		if(billvos == null||billvos.length==0)
			return;
		GeneralBillVO[] gbillvos = (GeneralBillVO[])billvos;
		String loginCorp =conx.getLoginCorp();//��¼��˾
		// modify  by zhw  2010-12-28  �������� ��ǩ�ֵ�ʱ�� ��дԤ���ʽ�
		if(action==Action.AUDIT){//���ǩ��  ʵ��  
			for(GeneralBillVO gbillvo:gbillvos){		
				getScmPubBO().reWriteUseFundBefore(gbillvo,loginCorp);		
			}			
		}
		if(action == Action.SAVE){
			//zhf add   �����ݲ����   ֻ���Ͽ���
			for(GeneralBillVO gbillvo:gbillvos){		
				getScmPubBO().checkAllowanceForICout(gbillvo);	
				getScmPubBO().checkAndUseFundForAllOut(gbillvo,false,loginCorp);
			}	
			//zhf end
		}else if(action == Action.DELETE){
			for(GeneralBillVO gbillvo:gbillvos){		
				getScmPubBO().checkAndUseFundForAllOut(gbillvo,true,loginCorp);
			}
		}
//  zhw end 2011-01-08
	}

	public void afterAction(Action action, AggregatedValueObject[] billvos,
			Object otherparam, SCMBsContext conx) throws BusinessException {
		// TODO Auto-generated method stub
		if(billvos == null||billvos.length==0)
			return;
		GeneralBillVO[] gbillvos = (GeneralBillVO[])billvos;

		if(action == Action.AUDIT){
			//���ǩ��  ��Դ�ƻ�  ����  �ƻ��ۼ���������  
			//modify by zhw  2011-01-21  �����������ʱ����������ʵ���йرգ���ԭ�������ͼƻ����ۼ������������������ⲻ������
			for(GeneralBillVO gbillvo:gbillvos){
//				adjustPlanOuttotalnum(gbillvo,true);
				
//				getScmPubBO().reWtriteToPlanFor4YClose(gbillvo);
			}
			
		}else if(action == Action.UNAUDIT){
			for(GeneralBillVO gbillvo:gbillvos){
//				adjustPlanOuttotalnum(gbillvo,false);
			}
			//zhw end
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

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����Դ�ڼƻ��ĵ��������ĵ������ⵥ ǩ��ʱ ���� �ƻ����ۼ���������������������ʱ�Ѹ��¹���
	 * 2012-2-20����04:19:21
	 * @param gbillvo ��ǰ���ⵥvo
	 * @param flag ����/����
	 * @throws BusinessException
	 * 
	 * liuys modify �ͼ�����,�Ҳ���˵
	 * 
	 */
	private void adjustPlanOuttotalnum(GeneralBillVO gbillvo,boolean flag) throws BusinessException{
		
		if(gbillvo == null)
			return;
		
		GeneralBillItemVO[] items = gbillvo.getItemVOs();

		Map<String,UFDouble > newItemMap = new HashMap<String, UFDouble>();
		Map<String,UFDouble> oldItemMap = new HashMap<String, UFDouble>();
		Map<String,UFDouble > newItemMap1 = new HashMap<String, UFDouble>();
		Map<String,UFDouble> oldItemMap1 = new HashMap<String, UFDouble>();
		List<String> lsoid = new ArrayList<String>();

		String key = null;
		UFDouble tmpNum = null;
		for(GeneralBillItemVO item:items){//����Դ��������   �������ⵥ��������λ��λ���з���
			key = item.getCsourcebillbid();
			if(!lsoid.contains(key))
				lsoid.add(key);
			if(newItemMap.containsKey(key))
				tmpNum = newItemMap.get(key);
			else
				tmpNum = UFDouble.ZERO_DBL;
			if(flag)
				tmpNum = tmpNum.add(PuPubVO.getUFDouble_NullAsZero(item.getNoutnum()));
			else
				tmpNum = tmpNum.add(PuPubVO.getUFDouble_NullAsZero(item.getNshouldoutnum()));
			newItemMap.put(key, tmpNum);

			if(oldItemMap.containsKey(key))
				tmpNum = oldItemMap.get(key);
			else
				tmpNum = UFDouble.ZERO_DBL;
			if(flag)
				tmpNum = tmpNum.add(PuPubVO.getUFDouble_NullAsZero(item.getNshouldoutnum()));
			else
				tmpNum = tmpNum.add(PuPubVO.getUFDouble_NullAsZero(item.getNoutnum()));
			oldItemMap.put(key, tmpNum);
		}
		if(lsoid.size()==0)
			return;
		Object o = getScmPubBO().getAlloOrderSoubid(lsoid.toArray(new String[0]));
		if(o == null)
			return;
		Map<String,String> idMap = (Map<String,String>)o;
		for(String key2:newItemMap.keySet()){
			newItemMap1.put(idMap.get(key2), newItemMap.get(key2));
		}
		for(String key2:oldItemMap.keySet()){
			oldItemMap1.put(idMap.get(key2), oldItemMap.get(key2));
		}

		getScmPubBO().reWriteToPlanFor5X1(newItemMap1, oldItemMap1, true);

	}
}

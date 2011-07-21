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
		String loginCorp =conx.getLoginCorp();//登录公司
		// modify  by zhw  2010-12-28  调拨出库 在签字的时候 回写预扣资金
		if(action==Action.AUDIT){//库存签字  实扣  
			for(GeneralBillVO gbillvo:gbillvos){		
				getScmPubBO().reWriteUseFundBefore(gbillvo,loginCorp);		
			}			
		}
		if(action == Action.SAVE){
			//zhf add   出库容差控制   只向上控制
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
			//库存签字  来源计划  调整  计划累计领用数量  
			//modify by zhw  2011-01-21  调拨出库审核时，调拨订单实现行关闭，还原可用量和计划的累计领用数量，调拨出库不能弃审
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
	 * @说明：（鹤岗矿业）来源于计划的调拨订单的调拨出库单 签字时 调整 计划的累计领用量（调拨订单保存时已更新过）
	 * 2012-2-20下午04:19:21
	 * @param gbillvo 当前出库单vo
	 * @param flag 正向/逆向
	 * @throws BusinessException
	 * 
	 * liuys modify 低级错误,我不想说
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
		for(GeneralBillItemVO item:items){//按来源汇总数量   调拨出库单会根据批次或货位进行分行
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

package nc.ui.hg.pu.plan.deal;

import java.util.HashMap;
import java.util.List;
import nc.vo.hg.pu.plan.deal.PlanDealVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.hg.pu.pub.PlanApplyInforVO;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.voutils.VOUtil;

//计划汇总合并算法类     封装变化

public class PlanCombinTool {

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）计划处理---汇总计划
	 * 2010-11-23下午05:41:47
	 * @param lseldata
	 * @param cl
	 * @return
	 * @throws Exception
	 */
	public static PlanDealVO[] combinPlanData(List<PlanDealVO> lseldata,PlanApplyInforVO infor,boolean isCombin) throws Exception{
		HashMap<String, PlanDealVO> dataMap= new HashMap<String, PlanDealVO>();
		String keytmp = null;
		PlanDealVO tmpvo = null;
		//		String combintag = isCombin?"Y":"N";
		int index = 0;
		//如果没有到货日期  不能处理
		DealPlanHealper.valDreqdate(lseldata);
		
		for(PlanDealVO data:lseldata){
			//合并依据：存货 caccperiodschemeid  年   月   主计量   辅计量 +供货单位+供货部门  是否考虑组织
			
			keytmp = String.valueOf(index) + HgPubTool.getString_NullAsTrimZeroLen(data.getCsupplycorpid())+
			HgPubTool.getString_NullAsTrimZeroLen(data.getCsupplydeptid())+
			HgPubTool.getString_NullAsTrimZeroLen(data.getPk_invbasdoc())+
			HgPubTool.getString_NullAsTrimZeroLen(data.getCyear())+
			HgPubTool.getString_NullAsTrimZeroLen(data.getCaccperiodschemeid())+
			HgPubTool.getString_NullAsTrimZeroLen(data.getCmonth())+
			HgPubTool.getString_NullAsTrimZeroLen(data.getCastunitid())+
			HgPubTool.getString_NullAsTrimZeroLen(PuPubVO.getUFDouble_NullAsZero(data.getNprice()))+
			HgPubTool.getString_NullAsTrimZeroLen(data.getPk_measdoc());

			if(dataMap.containsKey(keytmp)){
				tmpvo = dataMap.get(keytmp);
				//合并数量  需求部门=供货部门  需求单位=供货单位  申请日期=当前日期   计算出新供货单位   计算出新供货部门
				doCombin(tmpvo,(PlanDealVO)data.clone());				
			}else{
				tmpvo = (PlanDealVO)data.clone();//这里应复制一份  来使用  不影响 原数据来源  以便 取消操作时能恢复
				tmpvo.getLsourceid().add(tmpvo.getPk_plan_b());//记录汇总行来源
			}

			dataMap.put(keytmp, tmpvo);
			if(isCombin)
				continue;
			index++;
		}

		if(dataMap.size()==0)
			throw new BusinessException("数据出来异常");

		//		合并后数据处理
		PlanDealVO[] datas = dataMap.values().toArray(new PlanDealVO[0]);
		//填补信息
		dealCombinDatas(datas,infor);
		VOUtil.ascSort(datas, HgPubTool.DEAL_SORT_FIELDNAMES);
		return datas;
	}

	private static void dealCombinDatas(PlanDealVO[] datas,PlanApplyInforVO infor){
		for(PlanDealVO data:datas){
			data.setVbillno(null);
			data.setPk_corp(data.getCsupplycorpid());
			
			data.setCapplydeptid(data.getCsupplydeptid()==null?infor.getCapplydeptid():data.getCsupplydeptid());
			
			data.setCapplypsnid(infor.getCapplypsnid());
			data.setCreqcalbodyid(data.getCsupplycalbodyid()==null?infor.getCreqcalbodyid():data.getCsupplycalbodyid());
			data.setDbilldate(infor.getM_uLogDate());
			//重置供货方信息
			data.setCsupplycorpid(infor.getCsupplycorpid());
			data.setCsupplydeptid(infor.getCsupplydeptid());
			data.setCsupplycalbodyid(infor.getCsupplycalbodyid()==null?infor.getM_sLogCorp()==infor.getM_pocorp()?data.getCreqcalbodyid():null:infor.getCsupplycalbodyid());
			data.setNnum(data.getNnetnum());//下级的净需求即为上级的毛需求
			if(PuPubVO.getUFDouble_NullAsZero(data.getHsl()).doubleValue()>0)
			data.setNassistnum(data.getNnum().div(data.getHsl(), HgPubConst.NUM_DIGIT));//重算毛需求辅数量
			data.setNmny(data.getNnum().multiply(data.getNprice(), HgPubConst.MNY_DIGIT));
			//清空   库存  数量
			for(String name:HgPubTool.PLAN_IC_NUMS){
				data.setAttributeValue(name, null);
			}
			//			data.setCsupplywarehouseid("");
			//处理临时计划和资金计划  没有年度  取  需求日期的  年 作为年度
			if(PuPubVO.getString_TrimZeroLenAsNull(data.getCyear())==null){
				data.setCyear(String.valueOf(data.getDreqdate().getYear()));
			}
			if((!data.getPk_billtype().equalsIgnoreCase(HgPubConst.PLAN_YEAR_BILLTYPE))&&PuPubVO.getString_TrimZeroLenAsNull(data.getCmonth())==null){
				data.setCmonth(HgPubTool.getMonth(data.getDreqdate().getMonth()));
			}
		}
	}

	private static void doCombin(PlanDealVO vo1,PlanDealVO vo2){
		if(vo1==null||vo2==null)
			return;

		String[] arrnames = HgPubTool.PLAN_YEAR_NUMKEYS;
		for(String name:arrnames){			
			vo1.setAttributeValue(name, 
					PuPubVO.getUFDouble_NullAsZero(vo1.getAttributeValue(name))
					.add(PuPubVO.getUFDouble_NullAsZero(vo2.getAttributeValue(name))));

		}

		vo1.setAttributeValue("nmny", 
				PuPubVO.getUFDouble_NullAsZero(vo1.getAttributeValue("nmny"))
				.add(PuPubVO.getUFDouble_NullAsZero(vo2.getAttributeValue("nmny"))));

		vo1.getLsourceid().add(vo2.getPrimaryKey());
	}
}

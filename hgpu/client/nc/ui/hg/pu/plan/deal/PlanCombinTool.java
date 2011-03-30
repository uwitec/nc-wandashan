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

//�ƻ����ܺϲ��㷨��     ��װ�仯

public class PlanCombinTool {

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ���ƻ�����---���ܼƻ�
	 * 2010-11-23����05:41:47
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
		//���û�е�������  ���ܴ���
		DealPlanHealper.valDreqdate(lseldata);
		
		for(PlanDealVO data:lseldata){
			//�ϲ����ݣ���� caccperiodschemeid  ��   ��   ������   ������ +������λ+��������  �Ƿ�����֯
			
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
				//�ϲ�����  ������=��������  ����λ=������λ  ��������=��ǰ����   ������¹�����λ   ������¹�������
				doCombin(tmpvo,(PlanDealVO)data.clone());				
			}else{
				tmpvo = (PlanDealVO)data.clone();//����Ӧ����һ��  ��ʹ��  ��Ӱ�� ԭ������Դ  �Ա� ȡ������ʱ�ָܻ�
				tmpvo.getLsourceid().add(tmpvo.getPk_plan_b());//��¼��������Դ
			}

			dataMap.put(keytmp, tmpvo);
			if(isCombin)
				continue;
			index++;
		}

		if(dataMap.size()==0)
			throw new BusinessException("���ݳ����쳣");

		//		�ϲ������ݴ���
		PlanDealVO[] datas = dataMap.values().toArray(new PlanDealVO[0]);
		//���Ϣ
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
			//���ù�������Ϣ
			data.setCsupplycorpid(infor.getCsupplycorpid());
			data.setCsupplydeptid(infor.getCsupplydeptid());
			data.setCsupplycalbodyid(infor.getCsupplycalbodyid()==null?infor.getM_sLogCorp()==infor.getM_pocorp()?data.getCreqcalbodyid():null:infor.getCsupplycalbodyid());
			data.setNnum(data.getNnetnum());//�¼��ľ�����Ϊ�ϼ���ë����
			if(PuPubVO.getUFDouble_NullAsZero(data.getHsl()).doubleValue()>0)
			data.setNassistnum(data.getNnum().div(data.getHsl(), HgPubConst.NUM_DIGIT));//����ë��������
			data.setNmny(data.getNnum().multiply(data.getNprice(), HgPubConst.MNY_DIGIT));
			//���   ���  ����
			for(String name:HgPubTool.PLAN_IC_NUMS){
				data.setAttributeValue(name, null);
			}
			//			data.setCsupplywarehouseid("");
			//������ʱ�ƻ����ʽ�ƻ�  û�����  ȡ  �������ڵ�  �� ��Ϊ���
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

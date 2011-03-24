package nc.bs.wl.dm;

import java.util.List;
import nc.bs.dao.BaseDAO;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.vo.dm.PlanDealVO;
import nc.vo.dm.SendplaninVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class PlanDealBO {

	private BaseDAO m_dao = null;
	private BaseDAO getDao(){
		if(m_dao == null){
			m_dao = new BaseDAO();
		}
		return m_dao;
	}
	
	public PlanDealVO[] doQuery(String whereSql) throws Exception{
		PlanDealVO[] datas = null;
		//实现查询发运计划的逻辑   
		
		return datas;
	}
	
	public void doDeal(List ldata, String uLogDate, String sLogUser)
			throws Exception {
		if (ldata == null || ldata.size() == 0)
			return;
		/**
		 * 安排：生成发运订单 发运计划安排生成发运订单
		 * 
		 * 计划单号 计划行号 不合并计划行 计划和订单为1对多关系 分单规则： 发货站 收货站不同 不考虑计划类型
		 */

		// 发运安排vo---》发运计划vo
		// 按 计划号 发货站 收货站 分单
		CircularlyAccessibleValueObject[][] datas = SplitBillVOs.getSplitVOs(
				(CircularlyAccessibleValueObject[]) (ldata
						.toArray(new PlanDealVO[0])),
				WdsWlPubConst.DM_PLAN_DEAL_SPLIT_FIELDS);
		if (datas == null || datas.length == 0)
			return;
		int len = datas.length;
		PlanDealVO[] tmpVOs = null;
		HYBillVO[] planBillVos = new HYBillVO[len];
		for (int i = 0; i < len; i++) {
			tmpVOs = (PlanDealVO[]) datas[i];
			planBillVos[i] = new HYBillVO();
			planBillVos[i].setParentVO(getPlanHead(tmpVOs[0]));
			planBillVos[i].setChildrenVO(tmpVOs);
		}
		// 发运计划vo---》发运订单vo
		PfParameterVO paraVo = new PfParameterVO();
		// 参量上 设置 日期 操作人
		HYBillVO[] orderVos = (HYBillVO[]) PfUtilTools.runChangeDataAry(
				WdsWlPubConst.DM_PLAN_LURU_BILLTYPE,
				WdsWlPubConst.DM_ORDER_BILLTYPE, planBillVos, paraVo);
		// 分单---》保存订单
		new PfUtilBO()
				.processBatch(WdsWlPubConst.DM_PLAN_TO_ORDER_PUSHSAVE,
						WdsWlPubConst.DM_ORDER_BILLTYPE, uLogDate, orderVos,
						null, null);
	}
//	private SendplaninBVO[] getPlanBodyVOs(PlanDealVO[] dealVos){
//		if(dealVos == null||dealVos.length==0){
//			return null;
//		}
//		SendplaninBVO[] bodys = new SendplaninBVO[dealVos.length];
//		SendplaninBVO tmp = null;
//		String[] names = null;
//		int index = 0;
//		for(PlanDealVO deal:dealVos){
//			tmp = new SendplaninBVO();
//			if(names == null){
//				names = tmp.getAttributeNames();
//			}
//			for(String name:names){
//				tmp.setAttributeValue(name, deal.getAttributeValue(name));
//			}
//			bodys[index] = tmp;
//			index ++;
//		}
//		return bodys;
//	}
	private SendplaninVO getPlanHead(PlanDealVO dealVo){
		if(dealVo == null)
			return null;
		SendplaninVO head  = new SendplaninVO();
		String[] names  = head.getAttributeNames();
		for(String name:names){
			head.setAttributeValue(name, dealVo.getAttributeValue(name));
		}
		return head;
	}
	
}

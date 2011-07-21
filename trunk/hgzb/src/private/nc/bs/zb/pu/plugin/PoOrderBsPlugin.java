package nc.bs.zb.pu.plugin;

import java.util.HashMap;
import java.util.Map;

import nc.bs.scm.plugin.IScmBSPlugin;
import nc.bs.scm.plugin.SCMBsContext;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.bs.zb.bidding.make.MakeBiddingBO;
import nc.bs.zb.gen.GenOrderBO;
import nc.vo.po.OrderItemVO;
import nc.vo.po.OrderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.plugin.Action;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.pub.ZbPubConst;

public class PoOrderBsPlugin implements IScmBSPlugin {
	
	private Map<String, OrderItemVO[]> m_oldItemsInfor = null;
	

	private Map<String,UFDouble> getPrayNumInfor(OrderVO[] orders) throws BusinessException{
		if(orders == null || orders.length == 0)
			return null;
		Map<String, UFDouble> prayNumInfor = null;
		OrderItemVO[] items = null;
		for(OrderVO order:orders){
//			if(PuPubVO.getString_TrimZeroLenAsNull(order.getHeadVO().getPrimaryKey())!=null)
//				continue;//不处理修改保存情况
			items = order.getBodyVO();
			if(PuPubVO.getString_TrimZeroLenAsNull(items[0].getCsourcebilltype())==null)
				continue;
			if(!PuPubVO.getString_TrimZeroLenAsNull(items[0].getCsourcebilltype()).equalsIgnoreCase(ZbPubConst.ZB_BIDDING_BILLTYPE))
				continue;
			if(prayNumInfor == null)
				prayNumInfor = new HashMap<String, UFDouble>();
			for(OrderItemVO item:items){
				if(PuPubVO.getString_TrimZeroLenAsNull(item.getCsourcerowid())==null)
					continue;
				if(PuPubVO.getString_TrimZeroLenAsNull(item.getCsourcebilltype())==null)
					continue;
				if(!PuPubVO.getString_TrimZeroLenAsNull(item.getCsourcebilltype()).equalsIgnoreCase(ZbPubConst.ZB_BIDDING_BILLTYPE))
					continue;
//				if(prayNumInfor.contains(item.getCupsourcebillrowid()))
//					continue;
				prayNumInfor.put(item.getCsourcerowid(), item.getNordernum());
			}
		}
		return prayNumInfor;
	}
	public void beforeAction(Action action, AggregatedValueObject[] billvos,
			Object otherparam, SCMBsContext conx) throws BusinessException {
		// TODO Auto-generated method stub
		if(billvos == null||billvos.length==0)
			return;
		OrderVO[] orders = (OrderVO[])billvos;
		// modify  by zhw  记录修改前的数据
		if (action == Action.SAVE) {
			GenOrderBO bo = new GenOrderBO();
			bo.checkOrderNum(orders[0]);
			if (PuPubVO.getString_TrimZeroLenAsNull(orders[0].getParentVO().getPrimaryKey()) == null)
				return;
		  
			try {
				SmartDMO dmo = new SmartDMO();
				String headpk = null;
				OrderItemVO[] tmpitems = null;

				if (m_oldItemsInfor == null)
					m_oldItemsInfor = new HashMap<String, OrderItemVO[]>();
				else
					m_oldItemsInfor.clear();

				for (OrderVO order : orders) {
					headpk = PuPubVO.getString_TrimZeroLenAsNull(order.getParentVO().getPrimaryKey());
				
					if (headpk == null)
						continue;
					tmpitems = (OrderItemVO[]) dmo.selectBy(OrderItemVO.class,
							null, " corderid = '" + headpk + "' ");

					if (tmpitems == null || tmpitems.length == 0)
						continue;
					
					m_oldItemsInfor.put(headpk, tmpitems);
				}
			} catch (Exception e) {
				if (e instanceof BusinessException)
					throw (BusinessException) e;
				throw new BusinessException(e);
			}
		}
	}

	public void afterAction(Action action, AggregatedValueObject[] billvos,
			Object otherparam, SCMBsContext conx) throws BusinessException {
		if(billvos == null||billvos.length==0)
			return;
		OrderVO[] orders = (OrderVO[])billvos;
		GenOrderBO bo = new GenOrderBO();
		// modify  by zhw  2011-05-18 回写中标录入结果累计合同数量
		if(action == Action.SAVE){
			

//			OrderVO[] orders = (OrderVO[])billvos;
			//一次保存应该只有一张   zhf 
			Map<String,UFDouble> prayNumInfor = getPrayNumInfor(orders);			
			
			if(prayNumInfor != null && prayNumInfor.size()>0){//清除  对应年计划的累计  订货数量
				MakeBiddingBO biddingbo = new MakeBiddingBO();
				biddingbo.reWritePONumOnDel(null,prayNumInfor,false);//根据合同数量 修订 采购计划 累计订货量
			}
		
			
			
			
			
			for(OrderVO order:orders){		//zhw
				bo.reWriteToResultForOrder(order.getBodyVO(), 
						m_oldItemsInfor==null?null:m_oldItemsInfor.get(order.getHeadVO().getPrimaryKey()),true);
			}	
		}else if(action == Action.DELETE){
			
			
			//删除合同时  来源于招标系统的 调整 累计订货量

//			OrderVO[] orders = (OrderVO[])billvos;   zhf
			//一次保存应该只有一张
			Map<String,UFDouble> prayNumInfor = getPrayNumInfor(orders);
			
//			for(String key:prayNumInfor.keySet()){
//				prayNumInfor.put(key, UFDouble.ZERO_DBL);
//			}
					
			if(prayNumInfor != null && prayNumInfor.size()>0){//清除  对应年计划的累计  订货数量
				MakeBiddingBO bbo = new MakeBiddingBO();
//				bbo.adjustPlanAccountNum(prayNumInfor);//根据合同数量 修订 采购计划 累计订货量
				
				bbo.reWritePONumOnDel(null, prayNumInfor,true);
			}
			
			for(OrderVO order:orders){	//zhw	
				bo.reWriteToResultForOrder(order.getBodyVO(), null, false);
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

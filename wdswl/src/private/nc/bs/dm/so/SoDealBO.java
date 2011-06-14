package nc.bs.dm.so;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.dm.so.deal.SoDealBillVO;
import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class SoDealBO {

	private BaseDAO m_dao = null;
	private BaseDAO getDao(){
		if(m_dao == null){
			m_dao = new BaseDAO();
		}
//		int a = 0;
		return m_dao;
	}
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 
	 * @时间：2011-3-29下午02:08:02
	 * @param whereSql
	 * @return
	 * @throws Exception
	 */
	public SoDealVO[] doQuery(String whereSql) throws Exception{
		SoDealVO[] datas = null;
		//实现查询发运计划的逻辑 
		StringBuffer sql = new StringBuffer();
		sql.append("select  ");
		String[] names = SoDealVO.m_headNames;
		
		for(String name:names){
			sql.append(name+", ");
		}
		names = SoDealVO.m_bodyNames;
		for(String name:names){
			sql.append(name+", ");
		}
		sql.append(" 'aaa' ");
		sql.append(" from so_sale h inner join so_saleorder_b b on h.csaleid = b.csaleid " +
				" inner join so_saleexecute c on b.corder_bid = c.csale_bid ");
		
		sql.append(" where");
		sql.append("  isnull(h.dr,0)=0  and isnull(b.dr,0)=0  and isnull(c.dr,0)=0  ");
		if( whereSql !=null && whereSql.length()>0){
		sql.append(" and "+whereSql);
		}
		
		Object o = getDao().executeQuery(sql.toString(), new BeanListProcessor(SoDealVO.class));
		if( o != null){
			ArrayList<SoDealVO> list = (ArrayList<SoDealVO>)o;
			datas = list.toArray(new SoDealVO[0]);
		}
	
		return datas;
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 将本次安排数量，回写到发运计划安排累计发运数量
	 * @时间：2011-3-25下午04:44:08
	 * @param dealnumInfor
	 * @throws BusinessException
	 */
	private void reWriteDealNumForPlan(Map<String,UFDouble> map) throws BusinessException{

		if(map == null || map.size()==0)
			return;
		for(Entry<String, UFDouble> entry:map.entrySet()){
			String sql = "update so_saleorder_b set "+WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME+" = coalesce("+WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME+",0)+"
				+PuPubVO.getUFDouble_NullAsZero(entry.getValue()).doubleValue()+" where corder_bid='"+entry.getKey()+"'";
			if(getDao().executeUpdate(sql)==0){
				throw new BusinessException("数据异常：该发运计划可能已被删除，请重新查询数据");
			};

			//将计划数量（nplannum）和累计安排数量(ndealnum)比较

			//如果累计安排数量大于计划数量将抛出异常

			String sql1="select count(0) from so_saleorder_b where corder_bid='"+entry.getKey()+ "'and (coalesce(nnumber,0)-coalesce("+WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME+",0))>=0";			
			Object o=getDao().executeQuery(sql1,WdsPubResulSetProcesser.COLUMNPROCESSOR);
			if(o==null){
				throw new BusinessException("超计划量安排");
			}
		}
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * @时间：2011-3-25下午03:58:14
	 * @param ldata
	 * @param infor :登录人，登录公司，登录日期
	 * @throws Exception
	 */
	public void doDeal(List<SoDealVO> ldata, List<String> infor)
			throws Exception {
		if (ldata == null || ldata.size() == 0)
			return;
		/**
		 * 安排：生成发运单 发运计划安排生成发运订单
		 * 
		 * 计划单号 计划行号 不合并计划行 
		 * 计划和订单为1对多关系 
		 * 分单规则： 发货站 收货站不同 不考虑计划类型
		 * 		 */
		//回写计划累计安排数量
		// 发运安排vo---》发运计划vo
		Map<String,UFDouble> map = new HashMap<String, UFDouble>();
		for(int i=0;i<ldata.size();i++){
			String key = ldata.get(i).getCorder_bid();
			UFDouble num= PuPubVO.getUFDouble_NullAsZero(ldata.get(i).getNnum());
			if(map.containsKey(key)){
				UFDouble oldValue =PuPubVO.getUFDouble_NullAsZero(map.get(key));
				map.put(key, oldValue.add(num));
			}
			map.put(key, num);
		}
		reWriteDealNumForPlan(map);
		// 按 计划号 发货站 客户 分单
		CircularlyAccessibleValueObject[][] datas = SplitBillVOs.getSplitVOs(
				(CircularlyAccessibleValueObject[]) (ldata
						.toArray(new SoDealVO[0])),
				WdsWlPubConst.DM_PLAN_DEAL_SPLIT_FIELDS);
		if (datas == null || datas.length == 0)
			return;
		int len = datas.length;
		SoDealVO[] tmpVOs = null;
		SoDealBillVO[] planBillVos = new SoDealBillVO[len];
		for (int i = 0; i < len; i++) {
			tmpVOs = (SoDealVO[]) datas[i];
			planBillVos[i] = new SoDealBillVO();
			planBillVos[i].setParentVO(getPlanHead(tmpVOs[0]));
			planBillVos[i].setChildrenVO(tmpVOs);
		}
//		// 销售订单--》销售运单
		PfParameterVO paraVo = new PfParameterVO();
		paraVo.m_operator = infor.get(0);
		paraVo.m_coId = infor.get(1);
		paraVo.m_currentDate = infor.get(2);
//		// 参量上 设置 日期 操作人
		AggregatedValueObject[] orderVos = (AggregatedValueObject[]) PfUtilTools.runChangeDataAry(
				WdsWlPubConst.WDS4,
				WdsWlPubConst.WDS5, planBillVos, paraVo);
	    	
		
		
		
//		// 分单---》保存订单
		if(orderVos ==null || orderVos.length==0){
			return;
		}
		PfUtilBO pfbo = new PfUtilBO();
		for(AggregatedValueObject bill: orderVos){
			pfbo.processAction(WdsWlPubConst.DM_PLAN_TO_ORDER_SAVE, WdsWlPubConst.WDS5, infor.get(2), null, bill, null);
		}
	}
//	private SendplaninBVO[] getPlanBodyVOs(SoDealVO[] dealVos){
//		if(dealVos == null||dealVos.length==0){
//			return null;
//		}
//		SendplaninBVO[] bodys = new SendplaninBVO[dealVos.length];
//		SendplaninBVO tmp = null;
//		String[] names = null;
//		int index = 0;
//		for(SoDealVO deal:dealVos){
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
	private SaleorderHVO getPlanHead(SoDealVO dealVo){
		if(dealVo == null)
			return null;
		SaleorderHVO head  = new SaleorderHVO();
		String[] names  = head.getAttributeNames();
		for(String name:names){
			head.setAttributeValue(name, dealVo.getAttributeValue(name));
		}
		return head;
	}
//	


}

package nc.bs.wl.dm;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.vo.dm.PlanDealVO;
import nc.vo.pub.BusinessException;

public class PlanDealBO {

	private BaseDAO m_dao = null;
	private BaseDAO getDao(){
		if(m_dao == null){
			m_dao = new BaseDAO();
		}
		return m_dao;
	}
	
	public PlanDealVO[] doQuery(String whereSql) throws BusinessException{
		PlanDealVO[] datas = null;
		//实现查询发运计划的逻辑   
		
		return datas;
	}
	
	public void doDeal(List ldata) throws BusinessException{
		if(ldata == null || ldata.size() ==0)
			return;
		/**
		 * 安排：生成发运订单   发运计划安排生成发运订单
		 * 
		 *  计划单号  计划行号   不合并计划行  计划和订单为1对多关系
		 * 分单规则： 发货站  收货站不同 不考虑计划类型
		 */
		
		
		
		
		
	}
	
}

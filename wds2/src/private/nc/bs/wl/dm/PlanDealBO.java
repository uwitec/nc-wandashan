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
		//ʵ�ֲ�ѯ���˼ƻ����߼�   
		
		return datas;
	}
	
	public void doDeal(List ldata) throws BusinessException{
		if(ldata == null || ldata.size() ==0)
			return;
		/**
		 * ���ţ����ɷ��˶���   ���˼ƻ��������ɷ��˶���
		 * 
		 *  �ƻ�����  �ƻ��к�   ���ϲ��ƻ���  �ƻ��Ͷ���Ϊ1�Զ��ϵ
		 * �ֵ����� ����վ  �ջ�վ��ͬ �����Ǽƻ�����
		 */
		
		
		
		
		
	}
	
}

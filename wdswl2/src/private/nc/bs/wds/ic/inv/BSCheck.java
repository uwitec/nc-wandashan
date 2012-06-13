package nc.bs.wds.ic.inv;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
/**
 * 
 * �������� ��̨У���� 

 * author:mlr
 * */

public class BSCheck implements IBDBusiCheck {
	private BaseDAO dao;

	private BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
			throws Exception {
		if (intBdAction != IBDACTION.SAVE) {
			return;
		}
		if (vo == null || vo.getParentVO() == null) {
			return;
		}
		StockInvOnHandVO stock=(StockInvOnHandVO) vo.getParentVO();
		String ID = PuPubVO.getString_TrimZeroLenAsNull(stock.getPrimaryKey());
		if(ID==null)
			return;//��֧������ʱ�ĵ���
		
//		zhf   ���� �ж�  ���״̬ 
/**
 * 		1����������״̬������״̬����    ͬһ������ͬһ״̬ȫ������
 *      2��������״̬������״̬����   �������״̬����   ��  ֻ������ǰ����  ��Ӱ�� ͬ���� ��������״̬
 *      3������״̬�������״̬����   ͬ2
 */
		
//		ȡ����ǰ���״̬  �ж��Ƿ�����̬
		String state = PuPubVO.getString_TrimZeroLenAsNull(stock.getSs_pk());
		if(state == null)
			throw new BusinessException("���״̬Ϊ��");
		boolean isok = getIsOk(state);
		if(!isok)
			return;
		
		String sql = "select ss_pk from tb_warehousestock where whs_pk = '"+ID+"'";
		
		String oldstate = PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
		if(oldstate == null){
			throw new BusinessException("�����쳣��ԭ���״̬Ϊ��");
		}
//		
		boolean isoldok = getIsOk(oldstate);
		if(!isoldok)
			return;		
		
		sql=" update tb_warehousestock set ss_pk='"+stock.getSs_pk()+"' where " +
				" pk_cargdoc='"+stock.getPk_cargdoc()+"' " +
				" and pk_invbasdoc='"+stock.getPk_invbasdoc()+"'" +
				" and pk_invmandoc='"+stock.getPk_invmandoc()+"'" +
				" and whs_batchcode='"+stock.getWhs_batchcode()+"'" +
						" and ss_pk = '"+oldstate+"'";
        getDao().executeUpdate(sql);
		
	}
	
	private boolean getIsOk(String ss_pk) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(ss_pk)==null)
			throw new BusinessException("�������Ϊ��");
		String sql = "select isok from tb_stockstate where isnull(dr,0) = 0 and ss_pk = '"+ss_pk+"'";
		return PuPubVO.getUFBoolean_NullAs(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR), UFBoolean.FALSE).booleanValue();
	}
	
	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		
	}

}

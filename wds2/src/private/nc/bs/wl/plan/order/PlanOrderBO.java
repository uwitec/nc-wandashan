package nc.bs.wl.plan.order;

import nc.bs.dao.BaseDAO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.dm.SendplaninVO;
import nc.vo.dm.order.SendorderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
/**
 * ���˶�����WDS3����̨��
 * @author Administrator
 *
 */
public class PlanOrderBO {
	

	BaseDAO dao = null;
	
	private BaseDAO getBaseDAO(){
		if(dao==null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 *   ����ǰУ�� �Ƿ��Ѿ������������η��˶���
	 * @ʱ�䣺2011-3-27����09:44:46
	 * @param Ҫ����� ���˼ƻ�����
	 * @throws BusinessException 
	 */
	public void beforeUnApprove(AggregatedValueObject obj) throws BusinessException{
		if(obj ==null){
			return;
		}
		SendorderVO parent =(SendorderVO) obj.getParentVO();
		String pk_sendorder = parent.getPk_sendorder();
		StringBuffer sql = new StringBuffer();	
		sql.append(" select count(*) ");
		sql.append(" from tb_outgeneral_h ");
		sql.append(" jion tb_outgeneral_b ");
		sql.append(" on tb_outgeneral_h.general_pk= tb_outgeneral_b.general_pk");
		sql.append(" where isnull(tb_outgeneral_h.dr,0)=0 and isnull(tb_outgeneral_b.dr,0)=0 ");
		sql.append(" tb_outgeneral_b.csourcebillhid ='"+pk_sendorder+"'");
		int i = PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
		if( i>0){
			throw new BusinessException("���������������ⵥ������ɾ�����˶��������˲���");
		}
		
	}

}

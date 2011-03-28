package nc.bs.wl.plan.order;

import nc.bs.dao.BaseDAO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.dm.order.SendorderBVO;
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
	 *   ����ǰУ�� �Ƿ��Ѿ��������������������ⵥ
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
	/**
	 * 
	 * @throws BusinessException 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * ���˶������ϵ�ʱ����Ҫ�ؼ� ���˼ƻ��ۼư�������
	 * @ʱ�䣺2011-3-28����11:35:11
	 */
	public void reWriteSendPlan(AggregatedValueObject obj) throws BusinessException{
		if(obj == null ||obj.getChildrenVO()==null ||obj.getChildrenVO().length==0 ){
			return ;
		}
		SendorderBVO[] bodys= (SendorderBVO[])obj.getChildrenVO();
		StringBuffer sql = new StringBuffer();
		sql.append(" update wds_sendplanin_b set ndealnum=coalesce(ndealnum,0)-");
		for(SendorderBVO body:bodys){
			sql.append(PuPubVO.getUFDouble_NullAsZero(body.getNdealnum()));
			sql.append(" where pk_sendplanin_b='"+body.getCsourcebillbid()+"'");
			sql.append(" and pk_sendplanin='"+body.getCsourcebillhid()+"'");
			if(getBaseDAO().executeUpdate(sql.toString())==0){
				throw new BusinessException("�����쳣���÷��˼ƻ�����������");
			};
		}
	}

}

package nc.bs.wl.so.order;

import nc.bs.dao.BaseDAO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.dm.so.order.SoorderBVO;
import nc.vo.dm.so.order.SoorderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * �����˵���WDS5����̨��ѯ��
 * @author Administrator
 *
 */
public class SoorderBO {

	
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
	 *   ����ǰУ�� �Ƿ��Ѿ������������۳��ⵥ
	 * @ʱ�䣺2011-3-27����09:44:46
	 * @param Ҫ����� ���˼ƻ�����
	 * @throws BusinessException 
	 */
	public void beforeUnApprove(AggregatedValueObject obj) throws BusinessException{
		if(obj ==null){
			return;
		}
		SoorderVO parent =(SoorderVO) obj.getParentVO();
		String pk_soorder = parent.getPk_soorder();
		StringBuffer sql = new StringBuffer();	
		sql.append(" select count(*) ");
		sql.append(" from tb_outgeneral_h ");
		sql.append(" join tb_outgeneral_b ");
		sql.append(" on tb_outgeneral_h.general_pk= tb_outgeneral_b.general_pk");
		sql.append(" where isnull(tb_outgeneral_h.dr,0)=0 and isnull(tb_outgeneral_b.dr,0)=0 ");
		sql.append(" and tb_outgeneral_b.csourcebillhid ='"+pk_soorder+"'");
		int i = PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
		if( i>0){
			throw new BusinessException("�����������۳��ⵥ������ɾ�������˲���");
		}
		
	}
	
	//�����˵�����,��д���۶���
	public void backSoleOrder(AggregatedValueObject obj)throws BusinessException{
		if(obj ==null){
			return;
		}
		try{
			SoorderBVO[] bvo = (SoorderBVO[])obj.getChildrenVO();
			if(bvo!=null && bvo.length>0){
				for(int i = 0 ;i< bvo.length; i++){
					String sql = "update so_saleorder_b set "+WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME+" = coalesce("+WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME+",0)-"+
					PuPubVO.getUFDouble_NullAsZero(bvo[i].getNarrangnmu()).doubleValue()+" where corder_bid='"+bvo[i].getCsourcebillbid()+"' ";
					if(getBaseDAO().executeUpdate(sql)==0){
						throw new BusinessException("�����쳣,��д���۶������������²�ѯ����");
					};
				}
			}
		}catch(Exception e){
			throw new BusinessException("�����쳣,��д���۶������������²�ѯ����");
		}

	}
}
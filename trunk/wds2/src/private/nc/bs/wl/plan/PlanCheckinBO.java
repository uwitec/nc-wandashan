package nc.bs.wl.plan;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.dao.BaseDAO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.dm.SendplaninVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;

/**
 * @���ߣ�lyf
 * @˵�������ɽ������Ŀ 
 * ���˼ƻ�¼�루WDS1����̨��
 */
public class PlanCheckinBO {
	
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
	 * 	����ǰУ��
	 * ��ǰ��¼�˵Ĺ���Ĳֿ��ڵ�ǰ������Ƿ��Ѿ����¼ƻ�
	 * @ʱ�䣺2011-3-23����09:14:56
	 * @param o
	 */
	public void beforeCheck(String  pk_inwhouse) throws BusinessException{
		AccountCalendar calendar = AccountCalendar.getInstance();
		UFDate beginDate = calendar.getMonthVO().getBegindate();
		UFDate endDate = calendar.getMonthVO().getEnddate();
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*) from wds_sendplanin ");
		sql.append(" where dmakedate between '");
		sql.append(beginDate+"' and '" + endDate);
		sql.append("' and pk_inwhouse ='"+pk_inwhouse+"' ");
		int i = PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
		if( i>0){
			throw new BusinessException("����ֿ⣬��ǰ������Ѿ����¼ƻ�,ֻ������׷�Ӽƻ�");
		}
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
		SendplaninVO parent =(SendplaninVO) obj.getParentVO();
		String pk_sendplanin = parent.getPk_sendplanin();
		StringBuffer sql = new StringBuffer();	
		sql.append(" select count(*) ");
		sql.append(" from wds_sendorder ");
		sql.append(" jion wds_sendorder_b ");
		sql.append(" on wds_sendorder.pk_sendorder= wds_sendorder_b.pk_sendorder");
		sql.append(" where isnull(wds_sendorder.dr,0)=0 and isnull(wds_sendorder_b.dr,0)=0 ");
		sql.append(" wds_sendorder_b.csourcebillhid ='"+pk_sendplanin+"'");
		int i = PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
		if( i>0){
			throw new BusinessException("�������η��˶���������ɾ�����˶��������˲���");
		}
		
	}
}

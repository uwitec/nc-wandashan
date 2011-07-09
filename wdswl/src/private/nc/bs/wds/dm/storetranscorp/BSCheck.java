package nc.bs.wds.dm.storetranscorp;
import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
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
	/**
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *        ����������̨У�鷽��
	 * @ʱ�䣺2011-4-10����06:59:34
	 */
	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
			throws Exception {
		if (intBdAction != IBDACTION.SAVE) {
			return;
		}
		if (vo == null || vo.getParentVO() == null) {
			return;
		}
		if(vo.getChildrenVO()==null || vo.getChildrenVO().length==0){
			return;
		}
		SuperVO[] vos=(SuperVO[]) vo.getChildrenVO();		
		BsUniqueCheck.FieldUniqueChecks(vos,new String[]{"pk_wds_tanscorp_h"}," and pk_stordoc <> '"+vo.getParentVO().getAttributeValue("pk_stordoc")+"'", "��[������]�������ֿ��Ѿ�����");	
	}
	/**
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *        ����������̨У�����������
	 * @ʱ�䣺2011-4-10����06:59:34
	 */
	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		
	}

}

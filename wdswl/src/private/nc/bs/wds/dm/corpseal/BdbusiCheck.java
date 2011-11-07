package nc.bs.wds.dm.corpseal;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.dm.corpseal.CorpsealVO;
import nc.vo.wds.dm.sendinvdoc.SendinvdocVO;
/**
 * �ͻ���˾ͼ�� ��̨У����
 * @author Administrator
 *xjx  add
 */
public class BdbusiCheck implements IBDBusiCheck {
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

		// �ж����޸ĺ�ı��滹��������ı���
		CorpsealVO ivo = (CorpsealVO) vo.getParentVO();
		// ΨһУ��
		BsUniqueCheck.FieldUniqueCheck(ivo, new String[]{"pk_cubasdoc","pk_corp"}, " �ÿͻ������ݿ����Ѿ����� ");
	}

	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub

	}

}

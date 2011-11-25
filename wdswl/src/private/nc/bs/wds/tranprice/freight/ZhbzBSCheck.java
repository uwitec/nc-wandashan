package nc.bs.wds.tranprice.freight;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.tranprice.freight.ZhbzBVO;

public class ZhbzBSCheck implements IBDBusiCheck {
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
		ZhbzBVO ivo = (ZhbzBVO) vo.getParentVO();
		//���� ΨһУ��
		BsUniqueCheck.FieldUniqueCheck(ivo, new String[]{"pk_invbasdoc","pk_corp"}, " �ô�������ݿ����Ѿ����� ");
		BsUniqueCheck.FieldUniqueCheck(ivo, new String[]{"irownumber","pk_corp"}, " ���к������ݿ����Ѿ����� ");
	}
	
	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		
	}

}

package nc.bs.wds.ic.invstore;

import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.ic.invstore.CargdocVO;
import nc.vo.wds.ic.invstore.TbSpacegoodsVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 
 * @author yf �����λ�� ��̨У��
 * 
 */
public class BSCheck implements IBDBusiCheck {

	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
			throws Exception {
		if (intBdAction != IBDACTION.SAVE) {
			return;
		}
		if (vo == null || vo.getParentVO() == null) {
			return;
		}

		// �ж����޸ĺ�ı��滹��������ı���

		CargdocVO hvo = (CargdocVO) vo.getParentVO();

		TbSpacegoodsVO[] bvos = (TbSpacegoodsVO[]) vo.getChildrenVO();
		// bd_cargdoc��λ����30 pk = pk_cargdoc_30
		// У��������Ƿ��ڵ�ǰ��λ�ͷּ�ֻ�λ�����λ����Ψһ��
		BsUniqueCheck.FieldUniqueChecks(bvos, new String[] { "pk_invmandoc" },
				" and pk_cargdoc not in ( '" + WdsWlPubConst.pk_cargdoc_30
						+ "' ,'" + hvo.getPk_cargdoc()
						+ "') and pk_storedoc = '" + hvo.getPk_stordoc() + "'",
				"�д���Ѱ�������λ");
	}

	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub

	}

}

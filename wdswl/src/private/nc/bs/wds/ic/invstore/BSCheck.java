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
 * @author yf 存货货位绑定 后台校验
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

		// 判断是修改后的保存还是新增后的保存

		CargdocVO hvo = (CargdocVO) vo.getParentVO();

		TbSpacegoodsVO[] bvos = (TbSpacegoodsVO[]) vo.getChildrenVO();
		// bd_cargdoc货位编码30 pk = pk_cargdoc_30
		// 校验表体存货是否在当前货位和分拣仓货位以外货位中是唯一的
		BsUniqueCheck.FieldUniqueChecks(bvos, new String[] { "pk_invmandoc" },
				" and pk_cargdoc not in ( '" + WdsWlPubConst.pk_cargdoc_30
						+ "' ,'" + hvo.getPk_cargdoc()
						+ "') and pk_storedoc = '" + hvo.getPk_stordoc() + "'",
				"有存货已绑定其他货位");
	}

	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub

	}

}

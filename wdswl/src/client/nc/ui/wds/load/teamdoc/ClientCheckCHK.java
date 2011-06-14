package nc.ui.wds.load.teamdoc;

import java.awt.Container;

import nc.ui.trade.check.BeforeActionCHK;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.checkrule.IUniqueRule;
import nc.vo.trade.checkrule.VOChecker;
import nc.vo.wds.load.teamdoc.TeamdocBVO;

public class ClientCheckCHK extends BeforeActionCHK {

	public void runBatchClass(Container parent, String billType,
			String actionName, AggregatedValueObject[] vos, Object[] obj)
			throws Exception {

	}

	public void runClass(Container parent, String billType, String actionName,
			AggregatedValueObject vo, Object obj) throws Exception {

		if (actionName.equalsIgnoreCase("WRITE")) {
			if (vo == null) {
				return;
			}
			if (vo.getChildrenVO() == null || vo.getChildrenVO().length == 0) {

				throw new BusinessException("表体不允许为空");

			}
			boolean isCheckPass = VOChecker.checkUniqueRule(vo.getChildrenVO(),
					new IUniqueRule() {

						public String[] getFields() {

							return new String[] { "psncode" };
						}

						public String getHint() {

							return "人员编码 不允许重复";
						}
					});
			if (!isCheckPass) {

				throw new BusinessException("人员编码 不允许重复");
			}

		}
		TeamdocBVO[] vos = (TeamdocBVO[]) vo.getChildrenVO();
		int team = 0;
		for (int i = 0; i < vos.length; i++) {
			if (PuPubVO.getUFBoolean_NullAs(vos[i].getIsteam(), UFBoolean.FALSE).booleanValue()) {
				team++;
			}
		}
		if (team <= 0) {
			throw new BusinessException("必须指定一个组长");
		}
		if (team > 1) {
			throw new BusinessException("一个班组只能有一个组长");
		}
	}

}

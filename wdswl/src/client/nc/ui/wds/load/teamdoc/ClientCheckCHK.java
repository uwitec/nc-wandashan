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

				throw new BusinessException("���岻����Ϊ��");

			}
			boolean isCheckPass = VOChecker.checkUniqueRule(vo.getChildrenVO(),
					new IUniqueRule() {

						public String[] getFields() {

							return new String[] { "psncode" };
						}

						public String getHint() {

							return "��Ա���� �������ظ�";
						}
					});
			if (!isCheckPass) {

				throw new BusinessException("��Ա���� �������ظ�");
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
			throw new BusinessException("����ָ��һ���鳤");
		}
		if (team > 1) {
			throw new BusinessException("һ������ֻ����һ���鳤");
		}
	}

}

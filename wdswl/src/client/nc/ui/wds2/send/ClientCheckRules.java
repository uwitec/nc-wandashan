package nc.ui.wds2.send;
import nc.vo.trade.checkrule.ICheckRule;
import nc.vo.trade.checkrule.ICheckRules;
import nc.vo.trade.checkrule.ICheckRules2;
import nc.vo.trade.checkrule.ICompareRule;
import nc.vo.trade.checkrule.ISpecialChecker;
import nc.vo.trade.checkrule.IUniqueRule;
import nc.vo.trade.checkrule.IUniqueRules;
import nc.vo.trade.checkrule.UniqueRule;

public class ClientCheckRules implements ICheckRules, ICheckRules2, IUniqueRules {

	private static ClientCheckRules m_Rules = null;

	public ICheckRule[] getHeadCheckRules() {
		return null;
	}

	public ICompareRule[] getHeadCompareRules() {
		return null;
	}

	public String[] getHeadIntegerField() {
		return null;
	}

	public String[] getHeadUFDoubleField() {
		return null;
	}

	public ICheckRule[] getItemCheckRules(String tablecode) {
		return null;
	}

	public ICompareRule[] getItemCompareRules(String tablecode) {
		return null;
	}

	public String[] getItemIntegerField(String tablecode) {
		return null;
	}

	public String[] getItemUFDoubleField(String tablecode) {
		return null;
	}

	public ISpecialChecker getSpecialChecker() {
		return null;
	}

	public boolean isAllowEmptyBody(String tablecode) {
		return true;
	}

	public IUniqueRule[] getHeadUniqueRules() {
		return null;
	}

	// 判断表体不准重复
	public IUniqueRule[] getItemUniqueRules(String tablecode) {
		if (tablecode.equals("pp_sm_supcheck_class")) {
			return new IUniqueRule[] { new UniqueRule("表体供方分类不可重复", new String[] { "pk_supclass" }) };
		} else if (tablecode.equals("pp_sm_supcheck_qualif")) {
			return new IUniqueRule[] { new UniqueRule("表体业务资质不可重复", new String[] { "pk_majorbusiqulif" }) };
		} else {
			return null;
		}
	}

	public static ClientCheckRules getInstance() {
		if (m_Rules == null) {
			m_Rules = new ClientCheckRules();
		}
		return m_Rules;
	}

}

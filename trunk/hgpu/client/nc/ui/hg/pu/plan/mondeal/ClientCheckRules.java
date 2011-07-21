package nc.ui.hg.pu.plan.mondeal;

import nc.vo.trade.checkrule.CheckRule;
import nc.vo.trade.checkrule.ICheckRule;
import nc.vo.trade.checkrule.ICheckRules;
import nc.vo.trade.checkrule.ICheckRules2;
import nc.vo.trade.checkrule.ICompareRule;
import nc.vo.trade.checkrule.ISpecialChecker;
import nc.vo.trade.checkrule.IUniqueRule;
import nc.vo.trade.checkrule.IUniqueRules;
import nc.vo.trade.checkrule.UniqueRule;


public class ClientCheckRules implements ICheckRules, ICheckRules2,IUniqueRules{

	private static ClientCheckRules m_Rules=null;
	
	public static ClientCheckRules getInstance(){
		if(m_Rules==null){
			m_Rules=new ClientCheckRules();
		}
		return m_Rules;
	}
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
	public ICompareRule[] getItemCompareRules(String tablecode) {
		
		return null;
	}

	public String[] getItemIntegerField(String tablecode) {
		
		return null;
	}

	public String[] getItemUFDoubleField(String tablecode) {
		return null;
//		new String[]{
//				"nnum","nmonnum1", "nmonnum2", "nmonnum3", "nmonnum4",
//				"nmonnum5", "nmonnum6", "nmonnum7", "nmonnum8",
//				"nmonnum9", "nmonnum10", "nmonnum11", "nmonnum12",
//				"naftenum1", "naftenum2", "naftenum3", "naftenum4",
//				"naftenum5", "naftenum6", "naftenum7", "naftenum8",
//				"naftenum9", "naftenum10", "nafternum11", "nafternum12"
//		};
	}

	public ISpecialChecker getSpecialChecker() {
		
		return null;
	}
	// 不允许表体为空
	public boolean isAllowEmptyBody(String tablecode) {
		
		return false;
	}

	public IUniqueRule[] getHeadUniqueRules() {
		
		return null;
	}

	public IUniqueRule[] getItemUniqueRules(String tablecode) {
		return new UniqueRule[]{
				new UniqueRule("存货不可重复",new String[]{"cinventoryid"})
		};
	}
	
	public ICheckRule[] getItemCheckRules(String tablecode) {
		// 对表体的校验是否为空
		return new CheckRule[]{
			new CheckRule("存货", "cinventoryid", false, null, null)
		};
	}

}

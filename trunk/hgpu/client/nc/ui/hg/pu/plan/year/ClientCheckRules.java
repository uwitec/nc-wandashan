package nc.ui.hg.pu.plan.year;

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
		return new CheckRule[]{
				new CheckRule("公司", "pk_corp", false, null, null),
				new CheckRule("需求组织", "creqcalbodyid", false, null, null),
				new CheckRule("申请部门", "capplydeptid", false, null, null),
				new CheckRule("年度", "cyear", false, null, null),
				new CheckRule("会计期间方案", "caccperiodschemeid", false, null, null),
			};
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
		return  null;
//		new UniqueRule[]{
//				new UniqueRule("存货不可重复",new String[]{"cinventoryid"})
//		};
	}
	public ICheckRule[] getItemCheckRules(String tablecode) {
		// 对表体的校验是否为空
		return new CheckRule[]{
//			new CheckRule("存货", "cinventoryid", false, null, null),
			new CheckRule("毛需求", "nnum", false, null, null)
		};
	}
}

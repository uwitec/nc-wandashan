package nc.ui.hg.pu.nmr;

import nc.vo.trade.checkrule.CheckRule;
import nc.vo.trade.checkrule.ICheckRule;
import nc.vo.trade.checkrule.ICheckRules;
import nc.vo.trade.checkrule.ICheckRules2;
import nc.vo.trade.checkrule.ICompareRule;
import nc.vo.trade.checkrule.ISpecialChecker;
import nc.vo.trade.checkrule.IUniqueRule;
import nc.vo.trade.checkrule.IUniqueRules;


public class ClientCheckRules implements ICheckRules, ICheckRules2,IUniqueRules{

	private static ClientCheckRules m_Rules=null;
	
	public static ClientCheckRules getInstance(){
		if(m_Rules==null){
			m_Rules=new ClientCheckRules();
		}
		return m_Rules;
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
		
		return true;
	}

	public IUniqueRule[] getHeadUniqueRules() {
		
		return null;
	}

	public IUniqueRule[] getItemUniqueRules(String tablecode) {
		return null;
	}
	
	public ICheckRule[] getItemCheckRules(String tablecode) {
		// 对表体的校验是否为空
		return null;
	}
	
	public ICheckRule[] getHeadCheckRules() {
		return new CheckRule[]{
				
				new CheckRule("存货分类", "pk_invcl", false, null, null),
				new CheckRule("临时物资编码","cinvcode", false, null, null),
				new CheckRule("存货名称", "vinvname", false, null, null),
				new CheckRule("单位", "pk_measdoc", false, null, null)
				
			};
	}

}

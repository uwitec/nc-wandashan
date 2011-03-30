package nc.ui.hg.pu.check.fund;

import nc.vo.trade.checkrule.CheckRule;
import nc.vo.trade.checkrule.ICheckRule;
import nc.vo.trade.checkrule.ICheckRules;
import nc.vo.trade.checkrule.ICheckRules2;
import nc.vo.trade.checkrule.ICompareRule;
import nc.vo.trade.checkrule.ISpecialChecker;
import nc.vo.trade.checkrule.IUniqueRule;
import nc.vo.trade.checkrule.IUniqueRules;

public class ClientCHKRules implements ICheckRules,ICheckRules2, IUniqueRules {
	
	private static ClientCHKRules m_Rules=null;
	
	public ClientCHKRules() {
		// TODO 自动生成构造函数存根
	}
	
	public static ClientCHKRules getInstance(){
		if(m_Rules==null){
			m_Rules=new ClientCHKRules();
		}
		return m_Rules;
	}

	public IUniqueRule[] getHeadUniqueRules() {
		// TODO 自动生成方法存根

		return null;
	}

	public IUniqueRule[] getItemUniqueRules(String tablecode) {
		// 表体数据唯一性校验
		return null;
	}

	public ICheckRule[] getHeadCheckRules() {
		// 对表头的校验
		return null;
	}

	public ICompareRule[] getHeadCompareRules() {
		// TODO 自动生成方法存根
		return null;
	}

	public String[] getHeadIntegerField() {
		// TODO 自动生成方法存根
		return null;
	}

	public String[] getHeadUFDoubleField() {
		// TODO 自动生成方法存根
		return null;
	}

	public ICheckRule[] getItemCheckRules(String tablecode) {
		// 对表体的校验是否为空
		return new CheckRule[]{
			new CheckRule("年度", "cyear", false, null, null),
			new CheckRule("月份", "imonth", false, null, null),
			new CheckRule("资金", "nfund", false, null, null)
			
		};
	}

	public ICompareRule[] getItemCompareRules(String tablecode) {
		// TODO 自动生成方法存根
		return null;
	}

	public String[] getItemIntegerField(String tablecode) {
		// TODO 自动生成方法存根
		return null;
	}

	public String[] getItemUFDoubleField(String tablecode) {
		// TODO 自动生成方法存根
		return null;
	}

	public boolean canNull() {
		// TODO 自动生成方法存根
		return false;
	}

	public String getDisplayName() {
		// TODO 自动生成方法存根
		return null;
	}

	public Object getFieldMax() {
		// TODO 自动生成方法存根
		return null;
	}

	public Object getFieldMin() {
		// TODO 自动生成方法存根
		return null;
	}

	public String getFieldName() {
		// TODO 自动生成方法存根
		return null;
	}

	public ISpecialChecker getSpecialChecker() {
		// TODO 自动生成方法存根
		return null;
	}

	public boolean isAllowEmptyBody(String tablecode) {
		// TODO 自动生成方法存根
		return true;
	}

}

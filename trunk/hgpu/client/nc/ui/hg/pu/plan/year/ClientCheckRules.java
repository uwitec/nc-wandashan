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
				new CheckRule("��˾", "pk_corp", false, null, null),
				new CheckRule("������֯", "creqcalbodyid", false, null, null),
				new CheckRule("���벿��", "capplydeptid", false, null, null),
				new CheckRule("���", "cyear", false, null, null),
				new CheckRule("����ڼ䷽��", "caccperiodschemeid", false, null, null),
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
	// ���������Ϊ��
	public boolean isAllowEmptyBody(String tablecode) {
		
		return false;
	}

	public IUniqueRule[] getHeadUniqueRules() {
		
		return null;
	}

	public IUniqueRule[] getItemUniqueRules(String tablecode) {
		return  null;
//		new UniqueRule[]{
//				new UniqueRule("��������ظ�",new String[]{"cinventoryid"})
//		};
	}
	public ICheckRule[] getItemCheckRules(String tablecode) {
		// �Ա����У���Ƿ�Ϊ��
		return new CheckRule[]{
//			new CheckRule("���", "cinventoryid", false, null, null),
			new CheckRule("ë����", "nnum", false, null, null)
		};
	}
}

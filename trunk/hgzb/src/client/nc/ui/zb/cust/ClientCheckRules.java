package nc.ui.zb.cust;

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
	// ���������Ϊ��
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
		// �Ա����У���Ƿ�Ϊ��
		return null;
	}
	
	public ICheckRule[] getHeadCheckRules() {
		return new CheckRule[]{
				
				new CheckRule("��Ӧ�̱��", "vbillno", false, null, null),
				new CheckRule("��Ӧ������","custname", false, null, null),
				new CheckRule("��Ӧ�̼��", "custshortname", false, null, null),
				new CheckRule("��������", "pk_areacl", false, null, null)
				
			};
	}

}

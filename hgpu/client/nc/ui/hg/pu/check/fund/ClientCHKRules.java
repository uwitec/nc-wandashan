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
		// TODO �Զ����ɹ��캯�����
	}
	
	public static ClientCHKRules getInstance(){
		if(m_Rules==null){
			m_Rules=new ClientCHKRules();
		}
		return m_Rules;
	}

	public IUniqueRule[] getHeadUniqueRules() {
		// TODO �Զ����ɷ������

		return null;
	}

	public IUniqueRule[] getItemUniqueRules(String tablecode) {
		// ��������Ψһ��У��
		return null;
	}

	public ICheckRule[] getHeadCheckRules() {
		// �Ա�ͷ��У��
		return null;
	}

	public ICompareRule[] getHeadCompareRules() {
		// TODO �Զ����ɷ������
		return null;
	}

	public String[] getHeadIntegerField() {
		// TODO �Զ����ɷ������
		return null;
	}

	public String[] getHeadUFDoubleField() {
		// TODO �Զ����ɷ������
		return null;
	}

	public ICheckRule[] getItemCheckRules(String tablecode) {
		// �Ա����У���Ƿ�Ϊ��
		return new CheckRule[]{
			new CheckRule("���", "cyear", false, null, null),
			new CheckRule("�·�", "imonth", false, null, null),
			new CheckRule("�ʽ�", "nfund", false, null, null)
			
		};
	}

	public ICompareRule[] getItemCompareRules(String tablecode) {
		// TODO �Զ����ɷ������
		return null;
	}

	public String[] getItemIntegerField(String tablecode) {
		// TODO �Զ����ɷ������
		return null;
	}

	public String[] getItemUFDoubleField(String tablecode) {
		// TODO �Զ����ɷ������
		return null;
	}

	public boolean canNull() {
		// TODO �Զ����ɷ������
		return false;
	}

	public String getDisplayName() {
		// TODO �Զ����ɷ������
		return null;
	}

	public Object getFieldMax() {
		// TODO �Զ����ɷ������
		return null;
	}

	public Object getFieldMin() {
		// TODO �Զ����ɷ������
		return null;
	}

	public String getFieldName() {
		// TODO �Զ����ɷ������
		return null;
	}

	public ISpecialChecker getSpecialChecker() {
		// TODO �Զ����ɷ������
		return null;
	}

	public boolean isAllowEmptyBody(String tablecode) {
		// TODO �Զ����ɷ������
		return true;
	}

}

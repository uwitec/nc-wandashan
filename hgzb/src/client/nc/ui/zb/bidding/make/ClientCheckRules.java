package nc.ui.zb.bidding.make;

import nc.vo.trade.checkrule.CheckRule;
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
		if(tablecode.equalsIgnoreCase("zb_bidding_b")){
			return new CheckRule[]{
					new CheckRule("�����Ϣ", "cinvmanid", false, null, null),
					new CheckRule("�б�����","nzbnum", false, null, null)
					};
		}else if( tablecode.equalsIgnoreCase("zb_biddingsuppliers")){
			return new CheckRule[]{new CheckRule("��Ӧ����Ϣ", "ccustmanid", false, null, null)};
		}else{
			return null;
		}
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
		if(tablecode.equalsIgnoreCase("zb_bidding_b")){
			return false;
		}else if( tablecode.equalsIgnoreCase("zb_biddingsuppliers")){
			return false;
		}
		return true;
	}

	public IUniqueRule[] getHeadUniqueRules() {
		return null;
	}

//	// �жϱ��岻׼�ظ�
	public IUniqueRule[] getItemUniqueRules(String tablecode) {
		if (tablecode.equals("zb_bidding_b")) {
			return new IUniqueRule[] { new UniqueRule("�����Ϣ�����ظ�", new String[] { "cinvbasid","cinvmanid"}) };
		}else if (tablecode.equals("zb_biddingsuppliers")) {
			return new IUniqueRule[] { new UniqueRule("��Ӧ����Ϣ�����ظ�", new String[] { "ccustmanid","ccustbasid"}) };
		} else {
			return null;
		}
	}
	
	public ICheckRule[] getHeadCheckRules() {
		return new CheckRule[]{
				
				new CheckRule("������", "vbillno", false, null, null),
				new CheckRule("��������","cname", false, null, null),
				new CheckRule("�б�����", "izbtype", false, null, null),
				new CheckRule("ҵ��״̬", "ibusstatus", false, null, null)
				
			};
	}

	public static ClientCheckRules getInstance() {
		if (m_Rules == null) {
			m_Rules = new ClientCheckRules();
		}
		return m_Rules;
	}
}

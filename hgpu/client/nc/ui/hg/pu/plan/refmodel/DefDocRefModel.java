package nc.ui.hg.pu.plan.refmodel;

import nc.ui.bd.ref.AbstractRefModel;
import nc.vo.hg.pu.pub.HgPubConst;

public class DefDocRefModel extends AbstractRefModel{

	public DefDocRefModel() {
		super();
	}

	@Override
	public String getWherePart() {
		return " isnull(c.dr,0)= 0 and isnull(t.dr,0)= 0 and t.doclistname='"+HgPubConst.DEFDOC_NAME+"' ";
	}

	@Override
	public String[] getFieldCode() {
		return new String[] { 
				"c.doccode",
				"c.docname",
				"c.pk_defdoc"
				};
	}

	@Override
	public String[] getFieldName() {
		return new String[] { "��������","��������"};
	}

	@Override
	public String[] getHiddenFieldCode() {
		return new String[] {"c.pk_defdoc"};
	}

	@Override
	public int getDefaultFieldCount() {
		return 2;
	}

	@Override
	public String getPkFieldCode() {
		return "c.pk_defdoc";
	}

	@Override
	public String getRefTitle() {
		return "��ú�ڲ��������յ���";
	}

	@Override
	public String getTableName() {
		return " bd_defdoclist t join bd_defdoc c on t.pk_defdoclist=c.pk_defdoclist ";
	}
	
	@Override
	protected String getRefCacheSqlKey() {
		//���ò�����
		setCacheEnabled(false);
		return "";
	}

}


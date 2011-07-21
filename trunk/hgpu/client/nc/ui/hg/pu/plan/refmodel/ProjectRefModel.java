package nc.ui.hg.pu.plan.refmodel;

import nc.ui.bd.ref.AbstractRefModel;

//�ƻ���Ŀ��������

public class ProjectRefModel extends AbstractRefModel {
	
	public ProjectRefModel() {
		super();
	}

	@Override
	public String getWherePart() {
		return " isnull(hg_planproject.dr,0)= 0  ";
	}

	@Override
	public String[] getFieldCode() {
		return new String[] { "hg_planproject.vprojectcode",
				"hg_planproject.vprojectname",
				"hg_planproject.pk_planproject"
				};
	}

	@Override
	public String[] getFieldName() {
		return new String[] { "��Ŀ����", "��Ŀ����"};
	}

	@Override
	public String[] getHiddenFieldCode() {
		return new String[] {"hg_planproject.pk_planproject" };
	}

	@Override
	public int getDefaultFieldCount() {
		return 3;
	}

	@Override
	public String getPkFieldCode() {
		return "hg_planproject.pk_planproject";
	}

	@Override
	public String getRefTitle() {
		return "�ƻ���Ŀ����";
	}

	@Override
	public String getTableName() {
		return "hg_planproject";
	}
	
	@Override
	protected String getRefCacheSqlKey() {
		//���ò�����
		setCacheEnabled(false);
		return "";
	}

}

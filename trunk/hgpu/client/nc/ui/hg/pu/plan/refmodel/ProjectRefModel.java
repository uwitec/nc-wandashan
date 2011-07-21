package nc.ui.hg.pu.plan.refmodel;

import nc.ui.bd.ref.AbstractRefModel;

//计划项目档案参照

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
		return new String[] { "项目编码", "项目名称"};
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
		return "计划项目档案";
	}

	@Override
	public String getTableName() {
		return "hg_planproject";
	}
	
	@Override
	protected String getRefCacheSqlKey() {
		//设置不缓存
		setCacheEnabled(false);
		return "";
	}

}

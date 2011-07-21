package nc.ui.hg.pu.plan.refmodel;

import nc.ui.bd.ref.AbstractRefModel;
import nc.vo.trade.pub.IBillStatus;

public class InvbasdocRefModel extends AbstractRefModel{
	public InvbasdocRefModel() {
		super();
	}

	@Override
	public String getWherePart() {
		return " isnull(hg_new_materials.dr,0)= 0 and (hg_new_materials.vbillstatus != "
				+ IBillStatus.CHECKPASS+")";
	}

	@Override
	public String[] getFieldCode() {
		return new String[] { 
				
				"hg_new_materials.cinvcode",
				"hg_new_materials.vinvname",
				"hg_new_materials.vinvspec",
				"hg_new_materials.vinvtype",
				"bd_invcl.invclassname",
				"hg_new_materials.pk_materials"
				};
	}

	@Override
	public String[] getFieldName() {
		return new String[] { "存货编码", "存货名称","存货规格","存货类型","存货分类"};
	}

	@Override
	public String[] getHiddenFieldCode() {
		return new String[] {"hg_new_materials.pk_materials"};
	}

	@Override
	public int getDefaultFieldCount() {
		return 5;
	}

	@Override
	public String getPkFieldCode() {
		return "hg_new_materials.pk_materials";
	}

	@Override
	public String getRefTitle() {
		return "临时存货参照档案";
	}

	@Override
	public String getTableName() {
		return "hg_new_materials join bd_invcl on hg_new_materials.pk_invcl=bd_invcl.pk_invcl ";
	}
	
	@Override
	protected String getRefCacheSqlKey() {
		//设置不缓存
		setCacheEnabled(false);
		return "";
	}

}


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
		return new String[] { "�������", "�������","������","�������","�������"};
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
		return "��ʱ������յ���";
	}

	@Override
	public String getTableName() {
		return "hg_new_materials join bd_invcl on hg_new_materials.pk_invcl=bd_invcl.pk_invcl ";
	}
	
	@Override
	protected String getRefCacheSqlKey() {
		//���ò�����
		setCacheEnabled(false);
		return "";
	}

}


package nc.ui.wds.w80060206.refer;

import java.util.Hashtable;
import nc.ui.bd.ref.AbstractRefModel;

public class CfRefer extends AbstractRefModel {

	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return super.getPkFieldCode();
	}

	public CfRefer() {
		setDefaultFieldCount(2);
		setFieldCode(new String[]{  "cif_carnum","cif_carowner" });
		setFieldName(new String[] { "车牌号","车主姓名" });
		setPkFieldCode("cif_pk");
		setRefTitle("车辆信息");
		setTableName("tb_carinf");
		setWherePart(" dr = 0");
	}

	
}

package nc.ui.wds.w80061006.refer;

import java.util.Hashtable;
import nc.ui.bd.ref.AbstractRefModel;

public class CfRefer extends AbstractRefModel {

	public CfRefer() {
		setDefaultFieldCount(2);
		setFieldCode(new String[]{  "cif_carnum ","cif_carowner" });
		setFieldName(new String[] { "车牌号","车主姓名" });
		setPkFieldCode("cif_pk");
		setRefTitle("车辆信息");
		setTableName("tb_carinf");
		setWherePart(" dr = 0");

	}

	
}

package nc.ui.wds.w80061006.refer;

import java.util.Hashtable;
import nc.ui.bd.ref.AbstractRefModel;

public class CfRefer extends AbstractRefModel {

	public CfRefer() {
		setDefaultFieldCount(2);
		setFieldCode(new String[]{  "cif_carnum ","cif_carowner" });
		setFieldName(new String[] { "���ƺ�","��������" });
		setPkFieldCode("cif_pk");
		setRefTitle("������Ϣ");
		setTableName("tb_carinf");
		setWherePart(" dr = 0");

	}

	
}

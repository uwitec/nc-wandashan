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
		setFieldName(new String[] { "���ƺ�","��������" });
		setPkFieldCode("cif_pk");
		setRefTitle("������Ϣ");
		setTableName("tb_carinf");
		setWherePart(" dr = 0");
	}

	
}

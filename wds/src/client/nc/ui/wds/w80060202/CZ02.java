package nc.ui.wds.w80060202;

import java.util.Hashtable;
import nc.ui.bd.ref.AbstractRefGridTreeModel;
import nc.ui.bd.ref.AbstractRefModel;

public class CZ02 extends AbstractRefModel {

	/**
	 * RouteRefModel ������ע�⡣
	 * ˾������
	 */
	public CZ02() {
		setDefaultFieldCount(2);
		setFieldCode(new String[]{  "cifb_drivername","cifb_drivermobile"});
		setFieldName(new String[] { "˾��","��ϵ�绰" });
		setPkFieldCode("cifb_drivermobile");
		setRefTitle("˾����Ϣ");
		setTableName("tb_carinf_b");
		setWherePart(" dr = 0");
		
		
		
	}

}

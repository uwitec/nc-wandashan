package nc.ui.wds.w80060202;

import java.util.Hashtable;
import nc.ui.bd.ref.AbstractRefGridTreeModel;
import nc.ui.bd.ref.AbstractRefModel;

public class CZ02 extends AbstractRefModel {

	/**
	 * RouteRefModel 构造子注解。
	 * 司机参照
	 */
	public CZ02() {
		setDefaultFieldCount(2);
		setFieldCode(new String[]{  "cifb_drivername","cifb_drivermobile"});
		setFieldName(new String[] { "司机","联系电话" });
		setPkFieldCode("cifb_drivermobile");
		setRefTitle("司机信息");
		setTableName("tb_carinf_b");
		setWherePart(" dr = 0");
		
		
		
	}

}

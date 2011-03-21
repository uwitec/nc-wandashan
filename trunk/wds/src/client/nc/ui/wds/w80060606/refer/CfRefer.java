package nc.ui.wds.w80060606.refer;

import java.util.Hashtable;
import nc.ui.bd.ref.AbstractRefGridTreeModel;

public class CfRefer extends AbstractRefGridTreeModel {

	public CfRefer() {
		setRootName("运输公司信息"); // 根名
		setClassFieldCode(new String[] { "tc_comcode","tc_comname","tc_pk"}); // 字段名数组
		setFieldName(new String[]{"车主","车牌号"});
		setClassJoinField("tc_pk"); // 要和表关联的字段名
		
		setClassTableName("tb_transcompany"); // 运输公司表
		
		setClassWherePart(" tb_transcompany.dr = 0 ");
		
		setClassDefaultFieldCount(2); // 数值
		
		setFieldCode(new String[] {"tb_carinf.cif_carowner", "tb_carinf.cif_carnum" });
		// 设置不可显示的字段
		setHiddenFieldCode(new String[] {"tb_carinf.cif_pk" });
//		// 38项
		setDocJoinField("tb_carinf.tc_pk"); // i. 设定和树节点数据关联的字段
		setPkFieldCode("tb_carinf.cif_pk"); // 设定主键
		// 设定select 语句 表
		setTableName("tb_carinf ");
		// 设定Where条件  car left join tb_transcompany comy on car.tc_pk=comy.tc_pk
		setWherePart(" tb_carinf.dr = 0");
	}


}

package nc.ui.wds.w80040602.refer;

import java.util.Hashtable;
import nc.ui.bd.ref.AbstractRefGridTreeModel ;

public class STRefer extends AbstractRefGridTreeModel  {

	public STRefer() {
		setRefTitle("仓库货位信息");
		setRootName("仓库信息"); // 根名
		setClassFieldCode(new String[] { "storcode","storname","pk_stordoc"}); // 字段名数组
		
		setClassJoinField("pk_stordoc"); // 要和表关联的字段名
		
		setClassTableName("bd_stordoc"); // 运输公司表
		
		setClassWherePart(" bd_stordoc.pk_stordoc in ('1021A91000000004YZ0P','1021A91000000004UXCH'," +
				"'1021A91000000004UWVU','1021A91000000004UXCM','1021A91000000004UXCN','1021A91000000004UWVO'," +
				"'1021A91000000004UWVT') and bd_stordoc.dr=0 ");
		
		setClassDefaultFieldCount(2); // 数值
		setFieldName(new String[]{"货位编码","货位名称"});
		setFieldCode(new String[] {"bd_cargdoc.cscode", "bd_cargdoc.csname" });
		// 设置不可显示的字段
		setHiddenFieldCode(new String[] {"bd_cargdoc.pk_cargdoc" });
//		// 38项
		setDocJoinField("bd_cargdoc.pk_stordoc"); // i. 设定和树节点数据关联的字段
		setPkFieldCode("bd_cargdoc.pk_cargdoc"); // 设定主键
		// 设定select 语句 表
		setTableName("bd_cargdoc ");
		// 设定Where条件  car left join tb_transcompany comy on car.tc_pk=comy.tc_pk
		setWherePart(" bd_cargdoc.dr = 0");
	}

	
}

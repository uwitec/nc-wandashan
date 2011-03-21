package nc.ui.wds.w80060604;

import nc.ui.bd.ref.AbstractRefModel;

public class CZ01  extends AbstractRefModel{
	
	/**
	 * RouteRefModel 构造子注解。
	 * 销售主表
	 */
	public CZ01() {
		setDefaultFieldCount(2);
		setFieldCode(new String[]{  "dapprovedate ","vreceiptcode" });
		setFieldName(new String[] { "审批日期","订单号" });
		setPkFieldCode("csaleid");
		setRefTitle("销售订单信息");
		setTableName("so_sale");
		setWherePart(" dr = 0 and fstatus =2 and boutendflag ='N' and vreceiptcode like '%CO%'");
	}
}

package nc.ui.wds.w80060604;

import nc.ui.bd.ref.AbstractRefModel;

public class CZ01  extends AbstractRefModel{
	
	/**
	 * RouteRefModel ������ע�⡣
	 * ��������
	 */
	public CZ01() {
		setDefaultFieldCount(2);
		setFieldCode(new String[]{  "dapprovedate ","vreceiptcode" });
		setFieldName(new String[] { "��������","������" });
		setPkFieldCode("csaleid");
		setRefTitle("���۶�����Ϣ");
		setTableName("so_sale");
		setWherePart(" dr = 0 and fstatus =2 and boutendflag ='N' and vreceiptcode like '%CO%'");
	}
}

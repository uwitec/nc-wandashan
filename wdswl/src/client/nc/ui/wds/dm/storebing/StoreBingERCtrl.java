package nc.ui.wds.dm.storebing;

import nc.ui.zmpub.pub.excel.ExcelReadCtrl;
import nc.vo.wl.pub.Wds2WlPubConst;
import nc.vo.zmpub.excel.ExcelToBillConst;

public class StoreBingERCtrl extends ExcelReadCtrl {
	
	public StoreBingERCtrl(String sFileName, boolean flag) throws Exception {
		super(sFileName,flag);
	}

	private String[] headFieldNames = new String[]{ExcelToBillConst.excel_head_flag_field,"corp","outwh","outarea"};
	private String[] bodyFieldNames = new String[]{ExcelToBillConst.excel_head_flag_field,"corp","cust","inwh","gls","area","saleareaname","outnum","memo"};
	
	@Override
	public String getBillType() {
		return Wds2WlPubConst.store_cus_billtype;
	}

	@Override
	public String[] getBodyFieldNames() {
		return bodyFieldNames;
	}

	@Override
	protected String getDealBOClassName() {
		return "nc.bs.wds.dm.storebing.StoreBingExcelBO";
	}

	@Override
	public String[] getHeadFieldNames() {
		return headFieldNames;
	}

	@Override
	protected String getSingleChangeClassName() {
		return null;
	}

	@Override
	protected Class getSingleVOClass() {
		return null;
	}

	@Override
	public boolean isSingle() {
		return false;
	}
}

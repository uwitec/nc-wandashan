package nc.ui.wds.tranprice.fencang;

import nc.ui.zmpub.pub.excel.ExcelReadCtrl;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.excel.ExcelToBillConst;

public class StoreCusERCtrl extends ExcelReadCtrl{
	
	public StoreCusERCtrl(String sFileName, boolean flag) throws Exception {
		super(sFileName,flag);
	}

	private String[] headFieldNames = new String[]{ExcelToBillConst.excel_head_flag_field,"storcode","transcode","min","max"};
	private String[] bodyFieldNames = new String[]{ExcelToBillConst.excel_head_flag_field,"storcode","transcode","min","max","areaname","fee"};
	
	@Override
	public String getBillType() {
		return WdsWlPubConst.WDSK;
	}

	@Override
	public String[] getBodyFieldNames() {
		return bodyFieldNames;
	}

	@Override
	protected String getDealBOClassName() {
		return "nc.bs.wds.tranprice.fencang.StorBingExcelBO";
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

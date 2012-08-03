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
		// TODO Auto-generated method stub
		return Wds2WlPubConst.store_cus_billtype;
	}

	@Override
	public String[] getBodyFieldNames() {
		// TODO Auto-generated method stub
		return bodyFieldNames;
	}

	@Override
	protected String getDealBOClassName() {
		// TODO Auto-generated method stub
		return "nc.bs.wds.dm.storebing.StoreBingExcelBO";
	}

	@Override
	public String[] getHeadFieldNames() {
		// TODO Auto-generated method stub
		return headFieldNames;
	}

	@Override
	protected String getSingleChangeClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class getSingleVOClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSingle() {
		// TODO Auto-generated method stub
		return false;
	}
}

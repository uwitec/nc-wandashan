package nc.itf.hg.pu.pub;

public interface HgExcelColumnInfo {
	
	/** 对应ExcelFileVO的名称 */
	public static final String[] saAskVONameForHead = new String[] {
			"dbilldate", "pk_corp", "capplydeptid", "capplypsnid",
			"hcreqcalbodyid", "fisself", "csupplycorpid", "csupplydeptid",
			"cyear", "hvmemo" };
	/** 对应Excel表的列 */
	public static final String[] saAskCaptionForHead = new String[] { "单据日期",
			"需求组织", "申请部门", "申请人", "需求组织", "是否自制", "供货单位", "供货部门", "年度", "备注" };
	
	
	public static final String[] saAskVOName = new String[] { "dbilldate", "pk_corp", "capplydeptid", "capplypsnid",
		"hcreqcalbodyid", "fisself", "csupplycorpid", "csupplydeptid",
		"cyear", "hvmemo","invcode",
			"bcreqcalbodyid", "creqwarehouseid", "csupplycalbodyid",
			"csupplywarehouseid", "pk_measdoc", "nnum", "nnetnum", "nprice",
			"nmny", "bvmemo" ,"nmonnum1" ,"nmonnum2" ,"nmonnum3" ,"nmonnum4" ,"nmonnum5"
			 ,"nmonnum6" ,"nmonnum7" ,"nmonnum8" ,"nmonnum9" ,"nmonnum10" ,"nmonnum11" ,"nmonnum12"};
	/** 对应Excel表的列 */
	public static final String[] saAskCaption = new String[] {"单据日期",
		"需求组织", "申请部门", "申请人", "需求组织", "是否自制", "供货单位", "供货部门", "年度", "备注", "存货编码", "需求组织",
			"需求仓库", "供货组织", "供货仓库", "主单位", "毛需求", "净需求数量", "计划单价", "计划金额", "备注" , "1月份分量" , "2月份分量" 
			, "3月份分量" , "4月份分量" , "5月份分量" , "6月份分量" , "7月份分量" , "8月份分量" , "9月份分量" , "10月份分量" , "11月份分量" , "12月份分量" };
}
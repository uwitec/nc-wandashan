package nc.itf.hg.pu.pub;

public interface HgExcelColumnInfo {
	
	/** ��ӦExcelFileVO������ */
	public static final String[] saAskVONameForHead = new String[] {
			"dbilldate", "pk_corp", "capplydeptid", "capplypsnid",
			"hcreqcalbodyid", "fisself", "csupplycorpid", "csupplydeptid",
			"cyear", "hvmemo" };
	/** ��ӦExcel����� */
	public static final String[] saAskCaptionForHead = new String[] { "��������",
			"������֯", "���벿��", "������", "������֯", "�Ƿ�����", "������λ", "��������", "���", "��ע" };
	
	
	public static final String[] saAskVOName = new String[] { "dbilldate", "pk_corp", "capplydeptid", "capplypsnid",
		"hcreqcalbodyid", "fisself", "csupplycorpid", "csupplydeptid",
		"cyear", "hvmemo","invcode",
			"bcreqcalbodyid", "creqwarehouseid", "csupplycalbodyid",
			"csupplywarehouseid", "pk_measdoc", "nnum", "nnetnum", "nprice",
			"nmny", "bvmemo" ,"nmonnum1" ,"nmonnum2" ,"nmonnum3" ,"nmonnum4" ,"nmonnum5"
			 ,"nmonnum6" ,"nmonnum7" ,"nmonnum8" ,"nmonnum9" ,"nmonnum10" ,"nmonnum11" ,"nmonnum12"};
	/** ��ӦExcel����� */
	public static final String[] saAskCaption = new String[] {"��������",
		"������֯", "���벿��", "������", "������֯", "�Ƿ�����", "������λ", "��������", "���", "��ע", "�������", "������֯",
			"����ֿ�", "������֯", "�����ֿ�", "����λ", "ë����", "����������", "�ƻ�����", "�ƻ����", "��ע" , "1�·ݷ���" , "2�·ݷ���" 
			, "3�·ݷ���" , "4�·ݷ���" , "5�·ݷ���" , "6�·ݷ���" , "7�·ݷ���" , "8�·ݷ���" , "9�·ݷ���" , "10�·ݷ���" , "11�·ݷ���" , "12�·ݷ���" };
}
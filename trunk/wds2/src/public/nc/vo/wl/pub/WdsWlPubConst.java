package nc.vo.wl.pub;

public class WdsWlPubConst {
	 //1 ���ܽڵ�
	public static String DM_PLAN_LURU_NODECODE="80060405";
	// 2. ��������
    /**-----------����ֵ---------*/
	public static int DM_PLAN_LURU_IPLANTYPE0=0;
	public static int DM_PLAN_LURU_IPLANTYPE1=1;
	/** ----------����ֵ---------*/
	public static String DM_ORDER_NODECODE="80060415";
	
	public static String DM_SO_ORDER_NODECODE="80060425";

	public static String DM_SO_DEAL_NODECODE = "80060201";

	public static String DM_PLAN_DEAL_NODECODE = "80060410";

	// ��ťtag
	public static String DM_PLANDEAL_BTNTAG_QRY = "��ѯ";
	public static String DM_PLANDEAL_BTNTAG_DEAL = "����";
	public static String DM_PLANDEAL_BTNTAG_SELALL = "ȫѡ";
	public static String DM_PLANDEAL_BTNTAG_SELNO = "ȫ��";
	
	public static String WDS_WL_MODULENAME = "wdswl";
	public static String WDS_WL_ZC = "1021A91000000004YZ0P";//�ܲ���˫��
	
	public static String[] DM_PLAN_DEAL_SPLIT_FIELDS = new String[]{"vbillno","pk_outwhouse","pk_inwhouse"};//"vbillno",

	public static String[] SO_PLAN_DEAL_SPLIT_FIELDS = new String[]{"vreceiptcode","cbodywarehouseid","ccustomerid"};//"vbillno",
	
	public static String DM_PLAN_TO_ORDER_PUSHSAVE = "PUSHSAVE";
	public static String DM_PLAN_TO_ORDER_SAVE="SAVE";
	
	public static String DM_SO_DEALNUM_FIELD_NAME = "ntaldcnum";//����ϵͳ���۶���  �Ѳ���۱����� ��Ϊ  �ۼƷ�������
	
	/**�������ⵥ�ڵ�� */
	public static String  OTHER_OUT_FUNCODE="8004040208";
	/**���շ��˶����Ľڵ��ʾ */
	public static String  OTHER_OUT_REFWDS3_NODECODE="8004040210";
	public static String DEFAULT_CALBODY = "1021B1100000000001JL";
	
	/**���˼ƻ�¼�� */
	public static String WDS1="WDS1";
	/**���˼ƻ����� */
	public static String DM_PLAN_DEAL_BILLTYPE="WDS2";
	/**���˶��� */
	public static String WDS3="WDS3";
	/**�����˵����� */
	public static String WDS4 = "WDS4";
	/**�����˵� */
	public static String WDS5="WDS5";
	/**�������� */
	public static String BILLTYPE_OTHER_OUT = "WDS6";
	/**�������*/
	public static String BILLTYPE_OTHER_IN = "WDS7";
	/** ���۳��� */
	public static String BILLTYPE_SALE_OUT = "WDS8";
	/** �������*/
	public static String BILLTYPE_ALLO_OUT = "WDS9";
	/** �˻����*/
	public static String BILLTYPE_BACK_IN = "WDSA";
}

package nc.vo.hg.pu.pub;

public class HgPubConst {
	
	//�׸ھ���
	public static int NUM_DIGIT = 4;
	public static int PRICE_DIGIT = 6;
	public static int MNY_DIGIT = 2;
	
	public static int PLAN_YEAR_IMPORT_BILLSTATUS = 9;
	
	//����   ���պϸ�   ���ϸ�   �޺�ͬ   ���
	
	public static final int purchaseIn_arr = 0;
	public static final int purchaseIn_ok = 1;
	public static final int purchaseIn_no = 2;
	public static final int purchaseIn_nopact = 3;
	public static final int purchaseIn_in = 4;
	
	//ϵͳ�빺����---�ɹ�    2
	
	public static final int DEFAULT_NUM_DIGIT = 8;
	public final static int IPRAYTYPE = 2;
	
	public final static String PLAN_DEAL_MONTHADJUST_TEMPLETID = "0001AZ100000000034O1";
	
    /*��������*/
	public final static String ZJSZ = "ZJSZ";//�ʽ�����
	public final static String DESZ = "DESZ";//��������
	public final static String ZXSZ = "ZXSZ";//ר������
	public final static String XJWZ = "XJWZ";//�¾����ʱ�������
	public final static String RCBL = "RCBL";//�ݲ��������
	
    /*�ƻ�����*/
	public static String PLAN_YEAR_BILLTYPE = "HG01";//��������ƻ�
	public static String PLAN_MONTH_BILLTYPE = "HG02";//������ƻ�
	public static String PLAN_TEMP_BILLTYPE = "HG03";//��ʱ�ƻ�
	public static String PLAN_MNY_BILLTYPE = "HG04";//ר���ʽ�ƻ�
	public static String JHXM = "JHXM";//�ƻ���Ŀ
	public static String PLAN_MONDEAL_BILLTYPE = "HG05";//�¼ƻ�����
	public static String NEW_MATERIALS_BILLTYPE = "HG06";//����������
	public static String USER_AND_CUST = "HG07";//��¼�û��빩Ӧ��֮��Ĺ�ϵ
	public static String PLAN_BALANCE_BILLTYPE = "HG08";//����ƻ�ƽ��
	public static String PLAN_BAOZHANG_BILLTYPE = "HG10";//���˵� 40040407
	
	public static final int PLAN_ROW_STATUS_FREE = 0;//δ�ϱ�
	public static final int PLAN_ROW_STATUS_COMMIT = 1;//�ƻ����������ϱ�
	
	
	public static final String PLAN_BODY_ROWNO = "crowno";
	
	
	
	public static String PLAN_DEAL_BILLTYPE = "DEAL";//�ƻ��������ⵥ������
	public static String PLAN_DEAL_BILLTYPE2 = "DEAL2";//�ƻ��������ⵥ������  ģ��ʹ�� ��ģ��û�� ѡ�п�
	
//	public static int PLAN_TYPE_YEAR = 0;//��ƻ�
//	public static int PLAN_TYPE_TEMP = 1;//��ʱ�ƻ�
//	public static int PLAN_TYPE_MNY = 2;//ר���ʽ�ƻ�
	
	public static String PLAN_DEAL_FUNCODE = "40050401";
	public static String PLAN_DEAL_FUNCODE2 = "0001A310000000006C0W";
	public static String PLAN_MONDEAL_NODEKEY = "PLANBYYEAR";
	
	public static final int FUND_CHECK_FUND = 0;//����---�ʽ�
	public static final int FUND_CHECK_QUATO = 1;//����---�޶�
	public static final int FUND_CHECK_SPECIALFUND = 2;//����---ר���ʽ�
	public static final int FUND_CHECK_ERRORTYPE = -2;
	public static final int FUND_CHECK_FUND_QUATO = -1;//�ʽ�+�޶�
	
	public static String[] NAFTERNUM={ "naftenum1", "naftenum2", "naftenum3","naftenum4", "naftenum5", "naftenum6",
			"naftenum7","naftenum8", "naftenum9", "naftenum10", "nafternum11","nafternum12"};//12�·ݵ����������ֶ�
	public static String[] NMONTHNUM={"nmonnum1", "nmonnum2", "nmonnum3", "nmonnum4","nmonnum5", "nmonnum6",
			"nmonnum7", "nmonnum8","nmonnum9", "nmonnum10", "nmonnum11", "nmonnum12"};//12�·ݳ�ʼ�����ֶ�
	public static String[] NTOTAILNUM={"ntotailnum1", "ntotailnum2", "ntotailnum3", "ntotailnum4","ntotailnum5", "ntotailnum6",
			"ntotailnum7", "ntotailnum8","ntotailnum9", "ntotailnum10", "ntotailnum11", "ntotailnum12"};//12�·��ۼ������ֶ�
	public static String[] NADJUSTNUM={"nadjustnum1", "nadjustnum2", "nadjustnum3", "nadjustnum4","nadjustnum5", "nadjustnum6",
		"nadjustnum7", "nadjustnum8","nadjustnum9", "nadjustnum10", "nadjustnum11", "nadjustnum12"};//12�·��ۼ������ֶ�


	public static  String[] VUSERDEF={"vuserdef13","vuserdef14","vuserdef15","vuserdef16","vuserdef17","vuserdef18",
		"vuserdef19","vuserdef20"};//��ͷ�Զ�����13-20 �Ƿ�ϸ�   ��������  ��������ԭ�� ������ ����ʱ�� ����־  �������� ������
	public static String F_IS_SELF = "vuserdef12";
	public static String SELF ="����";
	public static final String CHECK_TEMP_ID = "0001AA1000000000480H";//������ϢPK_TEMP_ID  CHEC
	public static final String UNCHECK_DEAL_TEMP_ID = "0001AA10000000004UH1";//���ϸ���PK_TEMP_ID  quality

	public static final String MODIFY_TEMP_ID = "0001AA10000000005MXS";//������ϸPK_TEMP_ID  TZMX

	public static final String SUPPLY_PACT_DLG_TEMPLET_ID= "0001AZ10000000006KDB";//���ϸ���PK_TEMP_ID


	public static String PLAN_PROJECT_DEF = "vdef11";//����ר���ʽ�ƻ��ļƻ���Ŀ(��ͷ)
	public static String NUM_DEF_QUA = "vuserdef20";//�������պϸ����������壩
	public static String NUM_DEF_ARR = "vuserdef18";//���ڵ������������壩
	public static String NUM_DEF_FAC= "vuserdef19";//����ʵ�����������壩
	public static String HG_TO_PARA_01 = "HGTO0101";
	public static String HG_SO_PARA_01 = "HGSO01";
	
	public static String[] Plan_Head_EditItems = new String[]{"creqwarehouseid","capplydeptid","capplypsnid","cinvclassid","caccperiodschemeid","cplanprojectid","creqcalbodyid","csupplycorpid","csupplydeptid"};

	
	public static String[] PurchaseIn_ButtonName = {"��������","��Ӧ�̼Ĵ�","��ͨ�ɹ�","���д���","����ת�ɹ�","��Ӧ�̼Ĵ����ɹ�","���Ƶ���","�ɹ�����","��ѯ","ˢ��","��ҳ",
			"��ҳ","��ҳ","ĩҳ","�б���ʾ","�����˿�","�ɹ������˿�","���ϸ���","��Ƭ��ʾ"};
	
	public static String[] PurchaseIn_HeadItems ={"dbilldate","cwarehouseid","cbiztype","cdispatcherid","cwhsmanagerid","cdptid","cbizid","cproviderid","vnote"};

	public static String[] MATERIALS_HEAD_EDITITEMS = {"invcode","pk_taxitems","ijjway","biszywz","bisjjwz","bisdcdx","nplanprice","invmnecode","vmaterial","vtechstan"};
	
	public static String VENDOR_FREEZE_EWASON = "�����ʼ첻�ϸ�";
	public static String VENDOR_UNFREEZE_EWASON = "�����ʼ첻�ϸ������";
	
	public static String PRAY_BILL_VMI_FIELDNAME = "vdef2";//���������������
	public static String PRAY_BILL_PURTYPE_FIELDNAME = "vdef1";//����Բ�/���� ����
	public static String PRAY_BILL_PURTYPE_FIELDNAME2 = "pk_defdoc1";//����Բ�/���� ����
	
	public static String INVBAS_PURTYPE_FIELDNAME = "def1";//����Բ�/���� ����
	public static String INVBAS_VMI_FIELDNAME = "def2";//���������������
	public static String INVBAS_IMPROT_FIELDNAME = "def3";//��Ҫ����
	public static String INVBAS_OLD_FIELDNAME = "def4";//��������
	public static String INVBAS_VTS_FIELDNAME = "def5";//������׼
	public static String INVBAS_VMT_FIELDNAME = "def6";//����
	public static String INVBAS_NPP_FIELDNAME = "def20";//�ƻ���
	
	
	public static String INVBAS_WAY = "�ɹ���ʽ";//�ɹ���ʽ
	public static String INVBAS_WAY_SELF = "�Խ�";//
	public static String INVBAS_WAY_CENTRAL = "ͳ��";//
	public static String PK_CORP = "0001";//
	
	public static final boolean IS_DBILLDATE_WHEN_USE = true;
	
	public static final String[] TO_BILLITME_SORT= new String[]{"cbillbid"};
	
	public static final String PU_PACT_ITEM_TABLECODE = "pact";

	public static final String PLAN_VBILLSTATUS_SELT="����";
	
	// �û��ĵ�ǰ������״̬:��Ƭ���,�༭,��ͨ�б�ë��Ԥ��
	public static final int PO_STATE_BILL_BROWSE = 0;

	public static final int PO_STATE_BILL_EDIT = 1;

	public static final int PO_STATE_BILL_GROSS_EVALUATE = 3;

	public static final int PO_STATE_LIST_BROWSE = 2;


	//liuys add 2010-12-21 ��������ҵ������ ҵ�����Ͷ��� busitype 
	public static final String PI_INVOICE_BUSITYPE_EDIT = "CG02";

	public static final int PO_PACT_TABLECODE_INDEX = 4;
	
	public static final String DEFDOC_NAME="��ú�ڲ�����";
	
	public static final String TO_FROWSTATUS = "7";
	public static final String IC_CBIZTYPE ="0001A11000000000CLQG"; //�������ⵥ ҵ������PK  ����ʵ��
//	public static final String Balance_Flag_Deal = "deal";
//	public static final String Balance_Flag_UnDeal ="undeal";
	public static String PLAN_Balance_BILLTYPE = "BALANCE";//�ƻ�ƽ��
//	public static String PLAN_Balance_BILLTYPE1 = "BALANCE1";//�ƻ�ƽ�ⵥ������  ģ��ʹ�� ��ģ��û�� ѡ�п�
	public static String PLAN_Balance_ID = "zhw1aa10000000007JMP"; //��ѯ�Ի���
	
	
	//lyf add 2011-02-21
	/** ��Ӧ��˰��Ĭ��ֵ =17	 */
	public static final int DEFAULT_TAXRATE=17;



}

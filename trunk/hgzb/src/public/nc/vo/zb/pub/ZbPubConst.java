package nc.vo.zb.pub;

public class ZbPubConst {
	
	//�׸ھ���
	public static int grade_digit = 2;
	public static int NUM_DIGIT = 4;
	public static int PRICE_DIGIT = 6;
	public static int MNY_DIGIT = 2;

    /*�ƻ�����*/
	public static String ZB_WEB_BILLTYPE = "SPWE";//���ϱ���
	public static String ZB_LOCAL_BILLTYPE = "SPLO";//�ֳ�����
//	public static String ZB_SUBMITPRICE_BILLTYPE = "SPBI";//���۵�
	public static String ZB_BIDDING_BILLTYPE = "ZB01";//����
	public static String ZB_BIDDING_BILLTYPE_REF = "ZB0101";
	public static String ZB_EVALUATION_BILLTYPE = "ZB02";//�б������
	public static String ZB_BILL_BILLTYPE_VENDOR = "ZB03";//������� vendor
	public static String ZB_BILL_BILLTYPE_INV = "ZB04";//������� inv
	public static String ZB_FLGN_BILLTYPE = "FLMB";//��������ģ��
	public static String ZB_Result_BILLTYPE = "ZB05";//�б���¼��
	public static String ZB_BIDFLOOR_BILLTYPE = "ZB06";//��׼�ά��
	public static String ZB_PARAMSET_BILLTYPE = "PSET";//���۲�������
	public static String ZB_AVNUM_BILLTYPE = "ZB07";//	����������
	public static String ZB_CUSTBAS_BILLTYPE = "ZB10";//��Ӧ��ע��
	
	public static String ZB_PRICE_GRADE = "ZB08";//���۷�ά��
	public static String ZB_SUBMIT_BILL = "ZB09";//���۵�
	public static String ZB_SUBMIT_VIEW = "ZB11";//�鿴������Ϣ
	public static String ZB_SUBMIT_VIEW_FUNDCODE ="4004090420";//������Ϣ�ڵ���
	public static String ZB_BIDVIEW_BILLTYPE = "ZB12";//�������
	
	
	public static String zb_historyprice = "history";//���鹩����ʷ��Ϣ�鿴ģ��
	public static String zb_view = "view";//��׼�ά���Ĳ鿴��ϸģ��
	public static String zb_flmb = "flmb";//�б�������ķ���ģ��
	
	
//	����ҵ��״̬     0 --��ʼ  1--Ͷ��  2--����  3--�б�  4--��� 5--����
	public static int BIDDING_BUSINESS_STATUE_INIT = 0;
	public static int BIDDING_BUSINESS_STATUE_SUBMIT = 1;
	public static int BIDDING_BUSINESS_STATUE_BILL = 2;
	public static int BIDDING_BUSINESS_STATUE_RESULT = 3;
	public static int BIDDING_BUSINESS_STATUE_CLOSE = 4;
	public static int BIDDING_BUSINESS_STATUE_MISS = 5;
	
	
	public static String YEAR_PLAN_BILLTYPE = "HG03";
	
	public static boolean div_bidding_by_inv = true;//����Ʒ�ֻ��ֱ�� ���Ǹ�����ʷ��Ӧ�̻��ֱ��
	
	/**
	 * �������׼��
	 */
	public static String ADJ_BID_LEFT_TITLE = "���";
	public static String ADJ_BID_RIGHT_TITLE = "���";
	
	/**
	 * �������׼��
	 */
	public static String PRE_BID_LEFT_TITLE = "��������";
	public static String PRE_BID_RIGHT_TITLE = "����׶�";

	public static String TREE_ROOT_TAG = "root";
	
//	 * btntag
	 public static String BTN_TAB_COMMIT = "�ᱨ";
	 public static String BTN_TAB_CANCEL = "����";
	 public static String BTN_TAB_REFRESH = "ˢ��";
	 public static String BTN_TAB_FOLLOW = "����";
	 
	 public static int WEB_SUBMIT_PRICE = 0;//�б�����---�����б�
	 public static int LOCAL_SUBMIT_PRICE = 1;//�б�����---�ֳ��б�
	 public static int SELF_SUBMIT_PRICE = 2;//�б�����---�ֹ�ҵ��Ա¼��
	 public static int BAD_SUBMIT_PRICE = 3;//���ⱨ��
	 
	 
	 public static final String[] TIME_ASC_SORT_FILEDS = new String[]{"",""};
	 public static final long TIME_DIFFERRENCE = 0;//��ʱ���������ʱ��ƫ��  ms
	 
	 public static final String[] SUBMIT_PRICE_UPDATE_FIELD = new String[]{"nprice","cmodifyid","tmodifytime"};
	 
	 public static String Evaluation_TableCode1 = "zb_evaluation_b";//�б�������ӱ�ҵǩ
	 public static String DATE_CAL_DLG_TEMP_ID ="0001A11000000000XOMU";//��������ģ��ID
	 public static String VIEW_DETAIL_DLG_TEMP_ID ="0001A110000000010YBJ";//�鿴��ϸģ��ID
	 
	 public static String pk_currtype ="00010000000000000001";//��������
	 public final static int IZBRESULTTYPE = 2;
	 public static String GENORDER_BILLTYPE = "ORDE";//���ɺ�ͬ���ⵥ������ 
	 public static String GENORDER_MODLUECODE = "4004090603";//���ɺ�ͬ���ⵥ������
	 
	 public static final String[] DEAL_SORT_FIELDNAMES = new String[] {"vbillno", "invcode" };
	 public static final String[] VIEW_SORT_FIELDNAMES = new String[] {"custcode","vname" };
	 public static final String[] SUB_SORT_FIELDNAMES = new String[] {"vbillno","custcode","invcode","vname" };
	 public static String GENORDER_DIALOG_ID = "0001A110000000010M44";//���ɺ�ͬ�Ի�ID
	 public static String LOAD_DIALOG_ID = "9991A110000000011H0O";//���������Ի���
	 
	 public static int inv_class_coderule = 2;//�������������  2λ
	 
	 public static int bidding_inv_unique_time = 60;//��λ  �죺һ���·�Χ�ڲ����ظ��б�һ��Ʒ��
	 //po_order_b  NACCUMDAYPLNUM NUMBER(20,8) CUSERID CHAR(20)
	 public static boolean comment_split_num_flag = true;//true  ���б�����  ��  false ���б��ܽ���
	 
	 public static String corp = "1002";//��Ӧ�� ��������
	 
	 public static String ZB_TYPE_VENDOR="VEND";//��Ӧ�̻���
	 public static String ZB_TYPE_CORP="CORP";//��˾+��Ӧ�̻���
	 public static String ZB_TYPE_DETAIL="DETA";//Ʒ����ϸ
	 public static String ZB_TYPE_ZBDETAIL="ZBDE";//Ʒ����ϸ
	 public static String ZB_TYPE_ZBBID="ZBBI";//�ֱ�ι�Ӧ���б�
	 
	 
}

package nc.vo.wl.pub;

import nc.vo.pub.lang.UFDouble;

public class WdsWlPubConst {
	
	public static UFDouble ufdouble_zero = new UFDouble(0.0);
	
	
	public static String WDS_WL_MODULENAME = "wds";
	/**
	 *���״̬�������״̬����
	 */
	public static String WDS_STORSTATE_PK="1021S31000000009FS9A";
	/**
	 *���״̬��ϸ�״̬����
	 */
	public static String WDS_STORSTATE_PK_hg="1021S3100000000B8LVE";
	/**
	 *���״̬�����״̬����
	 */
	public static String WDS_STORSTATE_PK_dj="1021S31000000009FS99";
	
	/**
	 * ERP ����ⵥ��ʾ �Ƿ�������Զ�����
	 */
	public static String WDS_IC_ZG_DEF="pk_defdoc11";
	
	/**
	 * ERP����ⵥ��������������ĵ����Ƴ������ø��ֶα�������HID
	 */
	public static String csourcehid_wds="pk_defdoc11";
	/**
	 * ERP����ⵥ��������������ĵ����Ƴ������ø��ֶα�������BID
	 */
	public static String csourcebid_wds="pk_defdoc12";
	
	/**
	 *�ݹ�������Ӧ�����ⵥ���Զ�����11��������ʾ��:�ޱ�ʾ��������
	 */
	public static String WDS_IC_FLAG_wu="0001S3100000000MPNIE";	
	/**
	 *�ݹ�������Ӧ�����ⵥ���Զ�����11��������ʾ��:���������ⵥ����
	 */
	public static String WDS_IC_FLAG_you="0001S3100000000MPNIF";
	/**���˼ƻ�¼��ڵ�� */
	public static String DM_PLAN_LURU_NODECODE="80060405";
	
	/**���˼ƻ�����ڵ��  */
	public static String DM_PLAN_DEAL_NODECODE = "80060410";
	/**������������ڵ��  */
	public static String DB_PLAN_DEAL_NODECODE = "80060207";
	/**���˶����ڵ��  */
	public static String DM_ORDER_NODECODE="80060415";
	/**�������ⵥ�ڵ�� */
	public static String  OTHER_OUT_FUNCODE="8004040208";	
	/**�������ⵥ�ڵ�� */
	public static String  ALLO_OUT_FUNCODE="8004040217";	
	/**���շ��˶����Ľڵ��ʾ */
   public static String  OTHER_OUT_REFWDS3_NODECODE="800404028WDS3";
	/**�������ⶩ���Ľڵ��ʾ */
   public static String  OTHER_OUT_REFWDSS_NODECODE="800404028WDSS";
	/**���յ��������Ľڵ��ʾ */
   public static String  ALLO_OUT_REFWDSG_NODECODE="8004040217WDSG";
   /**���շ��˶����Ľڵ��ʾ */
   public static String  OTHER_OUT_REFWDSC_NODECODE="800404028WDSC";
   /**���ջ�λ�������Ľڵ��ʾ */
   public static String  OTHER_OUT_REFHWTZ_NODECODE="800404028HWTZ";//add by yf 2012-06-29
	/**�����˵��ڵ�� */
	public static String DM_SO_ORDER_NODECODE="80060425";
	/**���۶������Žڵ�� */
	public static String DM_SO_DEAL_NODECODE = "80060201";
	
	/**���۳��ⵥ�ڵ�� */
	public static String  SO_OUT_FUNCODE="8004040204";
	/**���������˵���NODEKEY */
	public static String  SO_OUT_REFWDS5_NODECODE="80060425";
	/**���պ������۶�����NODEKEY */
	public static String  SO_OUT_REF30_NODECODE="800404020430";
	/**������� �ڵ��*/
	public static String  IC_OTHER_IN_NODECODE= "8004040214";
	/**���������� [��Ӧ��] �������� ���ղ�ѯ NODEKEY  **/
	public static String  IC_OTHER_IN_REF4I_NODECODE = "80040402144I";
	/**���������� �����˵� ���ղ�ѯ NODEKEY  **/
	public static String  IC_OTHER_IN_REFWDSS_NODECODE = "8004040214WDSS";

	/**����������  �������� ���ղ�ѯ NODEKEY  **/
	public static String  IC_OTHER_IN_REFWDS7_NODECODE = "8004040214WDS6";
	/**������� ��λ���� ����ģ��ڵ��ʾ*/
	public static String  IC_OTHER_IN_REFHWTZ_NODECODE= "02140288";
	/**������� �˵�ȷ�� ����ģ��ڵ��ʾ*/
	public static String  IC_OTHER_IN_REFFYDJ_NODECODE= "2011040801";
	
	/**�˻���� �ڵ��*/
	public static String  IC_OUT_IN_NODECODE= "8004040220";
	/**�˻������� [��Ӧ��] ���۶��� ���ղ�ѯ NODEKEY  **/
	public static String  IC_OUT_IN_REF4I_NODECODE = "800404021430";
	
//	/**�˵�ȷ�� �ڵ�� */
//	public static String IC_TRANS_CONFIRM_NODECOED="80060210";
	/**�ɹ�ȡ�� �ڵ��*/
	public static String IE_CGQY_NODECODE="80021040";
	/**�����ƶ� �ڵ��*/
	public static String IC_TPYD_NODECODE="8004040212";
	/** ��λ��� �ڵ��*/
	public static String INVSTORE_NODECODE="80040602";
	/** ������� �ڵ��*/
	public static String IC_TRANSIN_NODECODE="8004040210";
	/** ����������ERP�������� �ڵ��ʾ*/
	public static String IC_TRANSIN_REF4Y_NODECODE="80040402104Y";	
	/** ���˴������*/
	public static String DM_PLAN_BASDOC_NODECODE="80060801";
	/** �ֲֳ����̰�*/
	public static String DM_STORE_TRANSCORP_NODECODE="8006080202";	
	/** ѧ���ɼ�*/
	public static String LM_CHENGJI_NODECODE="802005";
	/**
	   ��������
	 */
	public static String LM_CHENGJI_BILLTYPE="CHJI";
	/**
	/**���鵵��*/
    public static String LOAD_TEAM_DOC="8008010101";
   /**װж�Ѽ۸�����*/
   public static String  LOAD_SET_PRICE="8008010101";
	/** װж�Ѻ��㵥*/
	public static String LOAD_ACCOUNT="80080201";
	/** װж�Ѻ��㵥 ������������ڵ��ʾ*/
	public static String LOAD_ACCOUNT_REFWDS6="80080201WDS6";
	/** װж�Ѻ��㵥 �����������ڵ��ʾ*/
	public static String LOAD_ACCOUNT_REFWDS7="80080201WDS7";
	/** װж�Ѻ��㵥 �������۳���ڵ��ʾ*/
	public static String LOAD_ACCOUNT_REFWDS8="80080201WDS8";
	/** װж�Ѻ��㵥 ���յ����˿�ڵ��ʾ*/
	public static String LOAD_ACCOUNT_REFWDS9="80080201WDS9";
	
	
	

	/** ������̱��� �ڵ��*/
	public static String TRANS_MIL_NODECODE="8008010201";
	/**�����˼۱� �ڵ��*/
	public static String TRANS_SPECPRICE_NODECODE = "8008010216";
	/** ���乫˾����*/
	public static String TRANS_CORP_NODECODE="8008010202";
	/**����ҵ�񵵰��ڵ�� */
	public static String TRANS_SPECBUSI_NODECODE = "800801022001";
	/**����ҵ���˼۱� �ڵ�� */
	public static String TRANS_SPECBUSIPRICE_NODECODE = "800801022002";
	/**�ۺϱ�׼����ģ������ */
	public static String ZHBZ ="ZHBZ";
	/**���������ڵ�� */
	public static String TRANS_CARDOC_NODECODE = "8008010203";
	/**���͵����ڵ�� */
	public static String TRANS_CARTYPE_NODECODE = "8008010204";
	/**���ú��㵥�ڵ�� */
	public static String TRANS_PRICE_NODECODE = "80080202";
	/**���ú��㵥 ���շ��˶����ڵ��ʾ */
	public static String TRANS_PRICE_NODECODEWDS3 = "REFWDS3";
	/**���ú��㵥 ���������˵������ڵ��ʾ */
	public static String TRANS_PRICE_NODECODEWDS5 = "REFWDS5";
	/** �ͻ���˾ͼ�� �ڵ�� */
	public static String DM_CUST_CORPSEAL="80060804";
	
	
	/**������ú��㵥�ڵ�� */
	public static String TRANS_SEPCLPRICE_NODECODE = "80080203";
	/**���ʵʱ״̬--����*/
	public static String  REPORT02="80100202";
	/**ԭ�Ϸ��շ�����ܱ�--����*/
	public static String  REPORT04="80100204";
	/**��������۱���--����*/
	public static String  REPORT06="80100205";
	/**�������ˮ��--����*/
	public static String  REPORT08="80100208";
	/**������۸��ֿⵥƷ�����ϸ--����*/
	public static String  REPORT10="80100210";
	/**��������ܿ��--����*/
	public static String  REPORT15="80100215";
	/**�˷��»��ܱ�--����*/
	public static String  REPORT11="80080501";
	/**�˷���ϸ��--����*/
	public static String  REPORT12="80080503";
	/**װж���»��ܱ�--����*/
	public static String  REPORT13="80080502";
	/**װж�˷���ϸ��--����*/
	public static String  REPORT14="80080504";
	/**��ۿ����ϸ--����*/
	public static String  REPORT16="80100211";
	/**��ۿ����ϸ--����*/
	public static String  REPORT17="80100212";
	/**��۷���̨��������ϸ��--����*/
	public static String  REPORT30="80100230";
	
	public static String report_crklsz = "80100220";//yf add�������ˮ��  ����
	
	public static String report_unusenum_node = "80061010";//yf add���˹���-����ͳ��-��������ѯ����
	
	public static String pk_cargdoc_30 = "1021S3100000000AGE95";//yf add����󶨻�λ �ּ��  ��λ����30��pkֵ,ע���������Ҫ���»�ȡbd_cargdoc  
	
	public static String def_soorder_30 = "reserve14";//yf add �����˵� ������ʶ ���Ƿ�ָ���ּ�ֳ��⡷
	
	public static String IC_INV_SALE_ALERT_DAYNO = "def16";//������۾��������ֶ�
	/**�������� ��ͷ������ʽ*/
	public static String XN_CARGDOC_TRAY_NAME="XN";
	/**�������� Ĭ�ϴ��������*/
	public static Integer XN_CARGDOC_TRAY_VO=100000000;
	
	/**Ĭ�ϻ�дERP������*/
	public static String ERP_BANCHCODE="2009";
	
	public static String[] out_split_names = new String[]{"vbatchcode"};
	
    /**-----------����ֵ---------*/
	public static int DM_PLAN_LURU_IPLANTYPE0=0;
	public static int DM_PLAN_LURU_IPLANTYPE1=1;
	/** ----------����ֵ---------*/

	// ��ťtag
	public static String DM_PLANDEAL_BTNTAG_QRY = "��ѯ";
	public static String DM_PLANDEAL_BTNTAG_DEAL = "����";
	public static String DM_PLANDEAL_BTNTAG_SELALL = "ȫѡ";
	public static String DM_PLANDEAL_BTNTAG_SELNO = "ȫ��";
	public static String DM_PLANDEAL_BTNTAG_XNDEAL="ģ�ⰲ��";

	public static String WDS_WL_ZC = "1021A91000000004YZ0P";//�ܲ���˫��------------select * from BD_STORDOC aa where aa.PK_STORDOC ='1021A91000000004YZ0P' 
	
	public static String[] DM_PLAN_DEAL_SPLIT_FIELDS = new String[]{"pk_outwhouse","pk_inwhouse"};//"vbillno",

	public static String[] SO_PLAN_DEAL_SPLIT_FIELDS = new String[]{"cbodywarehouseid","ccustomerid","bdericttrans"};//"vbillno",
	
	public static String[] DB_PLAN_DEAL_SPLIT_FIELDS = new String[]{"coutwhid","cincorpid","cincbid","cinwhid"};//"vbillno",
	
	public static String DM_PLAN_TO_ORDER_PUSHSAVE = "PUSHSAVE";
	public static String DM_PLAN_TO_ORDER_SAVE="SAVE";
	
	public static String DM_SO_DEALNUM_FIELD_NAME = "nwdsnum";//����ϵͳ���۶���  �Ѳ���۱����� ��Ϊ  �ۼƷ�������
	
	public static String DM_DB_DEALNUM_FIELD_NAME = "ndealnum";//���������ۼư�������

	public static String DEFAULT_CALBODY = "1021B1100000000001JL";//Ĭ�Ͽ����֯---------------------select * from BD_CALBODY where PK_CALBODY ='1021B1100000000001JL'
	
	/**���˼ƻ�¼�� */
	public static String WDS1="WDS1";
	/**���˼ƻ����� */
	public static String DM_PLAN_DEAL_BILLTYPE="WDS2";
	/**���˶��� */
	public static String WDS3="WDS3";
	/**���ⶩ�� */
	public static String WDSS="WDSS";
	/**�������� */
	public static String WDSG="WDSG";
	/**�����˵����� */
	public static String WDS4 = "WDS4";//���ۼƻ�����  ԭ��
	public static String WDS4_2 = "WDS42";//���ۼƻ�����2
	public static String WDS4_2_1 = "cust";//���ۼƻ�����2
	public static String WDS4_2_2 = "deal";//���ۼƻ�����2
	public static String WDSB = "WDSB";//������������  
	public static String WDS30 = "30";//���۶���
	/**�����˵� */
	public static String WDS5="WDS5";
	/**�������� */
	public static String BILLTYPE_OTHER_OUT = "WDS6";
	/**�������� */
	public static String BILLTYPE_ALLO_OUT = "WDSH";
	public static String BILLTYPE_ALLO_OUT_1 = "WDSH_1";
	
	public static String BILLTYPE_OTHER_OUT_1 = "WDS6_1";
	/**�������*/
	public static String BILLTYPE_OTHER_IN = "WDS7";
	public static String BILLTYPE_OTHER_IN_1 = "WDS7_1";//�����ִ��� �����������ɾ��
	/** ���۳��� */
	public static String BILLTYPE_SALE_OUT = "WDS8";
	public static String BILLTYPE_SALE_OUT_1 = "WDS8_1";

	/** �������*/
	public static String BILLTYPE_ALLO_IN = "WDS9";
	public static String BILLTYPE_ALLO_IN_1 = "WDS9_1";//�����ִ��� ����������ɾ��
	/** �������ر�*/
	public static String BILLTYPE_ALLO_IN_CLOSE = "WDS9C";
	/**�������*/
	public static String BILLTYPE_OUT_IN = "WDSZ";
	
	public static String BILLTYPE_OUT_IN_1 = "WDSZ_1";
	/** ��Ӧ���������� */
	public static String GYL4Y = "4Y";
	/** ��Ӧ���������� */
	public static String GYL5D = "5D";
	/** ��Ӧ��������� */
	public static String GYL4E = "4E";
	/** ��Ӧ���������� */
	public static String GYL4I = "4I";
	/** ��Ӧ��������� */
	public static String GYL4A = "4A";
	
//	/** �˻����*/
//	public static String BILLTYPE_BACK_IN = "WDSA";
	/**����ȷ�ϵ�*/
	public static String BILLTYPE_SEND_CONFIRM = "WDSB";
	/**���״̬������Ϣ */
	public static String BILLTYPE_STORE_STATE="80040606";
	/**��λ���� */
	public static String BILLTYPE_CARG_TARY="8004061002";
	/**�ֿ���Ա��*/
	public static String BILLTYPE_IE_STOR_PERSONS="80021008";
	/**�����ϸ*/
	public static String BILLTYPE_IE_STOR_DETAIL="80021010";
	/**�������*/
	public static String BILLTYPE_IC_INV_DOC="8004061008";	
	
	/**�����˼ƻ����� �ϸ�״̬�ڼ�����*/
	public static String BILLTYPE_PlAN_DATESET="80060805";	
	/**���״̬*/
	public static String BILLTYPE_IC_INV_STATUS="8004040602";
	/**�ɹ�ȡ�� */
	public static String BILLTYPE_LM_CLASSINFOR="802003";
	public static String WDSC="WDSC";

	/**�����ƶ� */
	public static String WDSD="WDSD";//zhf ------����
	/** ��λ���*/
	public static String INVSTORE="80040602";
	/**װж�Ѽ۸� */
	public static String WDSE="WDSE";
	/**װж�ѽ��� */
	public static String WDSF="WDSF";
	/**�۸����䶨�� */
	public static String WDSH="WDSH";
	/**����ҵ�񵵰�*/
    public static String BILLTYPE_SPEC_BUSI_DOC="8008010220";	
    /**����ҵ���˼۱�*/
	public static final String BILLTYPE_SPEC_BUSIPRICE_DOC = "8008010221";
	
	/**�ֹ����˼۱� */
	public static String WDSI="WDSI";

	/**�����˼۱� */
	public static String WDSJ="WDSJ";
	
	/**�ֲ��˼۱� */
	public static String WDSK="WDSK";
	
	/**����ҵ���˼۱� */
	public static String WDSL="WDSL";
	/**�˷Ѻ��㵥 */
	public static String WDSM="WDSM";	
	/**�����˷Ѻ��㵥 */
	public static String WDSN="WDSN";	
	/**���۳���ش� */
	public static String WDSO="WDSO";
	/**��������ش� */
	public static String WDSX="WDSX";
	/**�������ش� */
	public static String WDSP="WDSP";
	/**�ݹ����� */
	public static String WDSQ="WDSQ";
	/**��λ������**/
	public static String HWTZ="HWTZ";//add by yf 2012-06-29
	/**�㹤���õ� */
	public static String WDSV="WDSV";//add by yf 2012-07-17
	
	
	//---------------�ִ��������漰�� �����Ի���ע��ģ������
//	���ⵥ��  ��������ָ��
	public static String DLG_OUT_TRAY_APPOINT = "OTA";
//	��ⵥ��  �������ָ��
	public static String DLG_IN_TRAY_APPOINT = "ITA";
//	���˶��� �������˵� ����
	public static String XNAP = "XNAP";
//	zhd   Ϊ��ӡ����ĵ���ģ��
//	������ⵥ 
	public static String PRINT_BILL_TEMPLET = "0001S3100000000KPO7G";
	public static String default_inv_state = "1021S31000000009FS98";//���ʱ����Ĭ�Ͽ��״̬=====+++++select * from tb_stockstate
	//wdsģ����־��¼
	public static String wds_logger_name = "wds";
//	���۰���ʱ  ���� �ͻ�����С������  �Ǹ����� ����  ������
	public static boolean sale_send_isass = true; 
//	zhf   �ֿ⵵���Զ�����2   ת�ֲ����ʱ   ʵ�������� ���� ʵ�ʳ� ����  �Ƿ��Զ� ���� ��¼
	public static String wds_warehouse_sytz = "def2";
	
//	zhf ���״̬ ����󶨹�ϵ �Ի��� ģ������
	public static String INV_VIEW_LOCK_TEMPLET_TYPE = "lock";
	
//	���״̬  ���� ��ѯ�Ի���  ģ��ID
	public static String INV_STATE_QRY_TEMPLET_ID = "0001S3100000000L231C";
	
	
	//mlr ���ɽ��������  �������ܽڵ�
	public static String report1="80100201";//���ֲ�Ʒ��� ��Ʒ��ϸ��
	public static String report2="80100203";//���ֲ�Ʒ��� �ܱ�
    public static String report3="80100234";//������»���
    public static String report4="80100245";//������»���(�ϼ�)
    public static String report5="80100240";//������۷���̨��
    public static String report6="80100243";//������۷���̨��(����)
    public static String report7="80100255";//������۴�����̨��
    public static String report8="80100260";//������۴�����̨��(����)
    public static String report9="80100237";//
    //liuys ����˶Ա�
    public static String zwhdb = "80100209";//����˶Ա�
    //liuys �ּ�ֶ��˱�
    public static String fjcdzb = "80100214";
    
    
//    ����������ⵥ��дerp�������ⵥ�ۼ�ת����������  �ֶ�
    public static String erp_allo_outnum_fieldname = "nkdnum";

//    ������ⵥ�ر� �ڵ��
    public static String allo_in_close_node = "8004040225";
	public static String sendorder_close = "reserve14";//add by yf 2012-07-26���˹���ر��ֶΣ���Y���رա�N���򿪣�null=��N��(nc.vo.dm.order.SendorderVO)
	public static String soorder_close = "reserve15";//add by yf 2012-07-26�����˵��ر��ֶΣ���Y���رա�N���򿪣�null=��N��(nc.vo.dm.so.order.SoorderVO;)

	public static String dmplan_xn = "reserve16";//add by yf 2012-07-26���˼ƻ������ֶΣ���Y������ƻ���N��������ƻ� null=��N��(nc.vo.dm.SendplaninVO)
	
	public static String sendorder_xn = "reserve16";//add by yf 2012-07-26���˶��������ֶ�(nc.vo.dm.order.SendorderBVO)
	
	public static String report_cldbfx_node = "80041001";///add by yf 2012-07-27�����Աȷ����ڵ�
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

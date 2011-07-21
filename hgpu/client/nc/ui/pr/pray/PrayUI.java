package nc.ui.pr.pray;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.scm.cenpurchase.IScmPosInv;
import nc.itf.scm.invbusitype.IInvBusiService;
import nc.itf.scm.invsourclist.IInvSourceService;
import nc.itf.uap.pf.IPFWorkflowQry;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.hg.pu.pub.TransletFactory;
import nc.ui.ic.pub.QueryOnHandInfoPanel;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.ic.service.IQueryOnHandInfoPanel;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.query.ICheckRetVO;
import nc.ui.po.pub.InvAttrCellRenderer;
import nc.ui.po.pub.PoEditTool;
import nc.ui.po.pub.PoPublicUIClass;
import nc.ui.po.ref.OtherRefModel;
import nc.ui.pp.pub.StringUtil;
import nc.ui.pr.pub.PrTool;
import nc.ui.pr.pub.ProjectPhase;
import nc.ui.pu.pub.ATPForOneInvMulCorpUI;
import nc.ui.pu.pub.PuGetUIValueTool;
import nc.ui.pu.pub.PuTool;
import nc.ui.pu.pub.PubHelper;
import nc.ui.pu.pub.PurPsnRefModel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillActionListener;
import nc.ui.pub.bill.BillBodyMenuListener;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillSortListener;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.bill.BillUIUtil;
import nc.ui.pub.bill.IBillModelSortPrepareListener;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.rc.pub.PurchasePrintDS;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.pu.ParaVOForBatch;
import nc.ui.scm.pub.BillTools;
import nc.ui.scm.pub.BusiBillManageTool;
import nc.ui.scm.pub.CollectSettingDlg;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.bill.IBillExtendFun;
import nc.ui.scm.pub.cache.CacheTool;
import nc.ui.scm.pub.ctrl.BillLineInfoListener;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.print.ISetBillVO;
import nc.ui.scm.pub.print.ScmPrintTool;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.ref.WarehouseRefModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.hg.pu.plan.year.PlanYearBVO;
import nc.vo.po.pub.Operlog;
import nc.vo.pr.pray.AdDayVO;
import nc.vo.pr.pray.PraybillHeaderVO;
import nc.vo.pr.pray.PraybillItemVO;
import nc.vo.pr.pray.PraybillVO;
import nc.vo.pr.pray.PriceInfosVO;
import nc.vo.pu.exception.RwtPoToPrException;
import nc.vo.pu.pr.PrayPubVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.datapower.BtnPowerVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.invsourcelist.InvSourceVO;
import nc.vo.scm.pu.BillStatus;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.VariableConst;
import nc.vo.scm.pub.OnHandRefreshVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.service.ServcallVO;

/**
 * ��������:�빺��ά������
 */
public class PrayUI extends nc.ui.pub.ToftPanel implements BillEditListener,
		BillTableMouseListener, ListSelectionListener, BillBodyMenuListener,
		ICheckRetVO, BillEditListener2, IBillModelSortPrepareListener,
		ISetBillVO, IBillExtendFun, BillCardBeforeEditListener,
		IBillRelaSortListener2, ILinkMaintain,// �����޸�
		ILinkAdd,// ��������
		ILinkApprove,// ������
		ILinkQuery,// ������
		BillSortListener,// �������
		BillLineInfoListener, BillActionListener {
	// since v53,�����к�
	UIMenuItem m_miReSortRowNo = null;

	UIMenuItem m_miCardEdit = null;

	String m_sModelcode = null;

	private nc.itf.uap.pf.IPFWorkflowQry ipflowqry = ((IPFWorkflowQry) NCLocator
			.getInstance().lookup(IPFWorkflowQry.class.getName()));

	// since v55,�����кż���
	class IMenuItemListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			PrayUI.this.onMenuItemClick(event);
		};
	}

	// �б��Ƿ���ع�
	private boolean m_bListPanelLoaded = false;

	// ��ť��ʵ��,since v51
	private ButtonTree m_btnTree = null;

	// �Ż�
	private BillTempletVO m_billTempletVO = null;

	/**
	 * ������ư�ť����
	 */
	// private ButtonObject m_btnBusiTypes = null;//ҵ������
	private ButtonObject m_btnAdds = null;// ����

	// �в���
	private ButtonObject m_btnLines = null;

	private ButtonObject m_btnAddLine = null;

	private ButtonObject m_btnDelLine = null;

	private ButtonObject m_btnInsLine = null;

	private ButtonObject m_btnCpyLine = null;

	private ButtonObject m_btnPstLine = null;

	// since v55
	private ButtonObject m_btnPstLineTail = null;

	// since v55
	private ButtonObject m_btnReSortRowNo = null;

	private ButtonObject m_btnCardEdit = null;

	// ά��
	private ButtonObject m_btnMaintains = null;

	private ButtonObject m_btnModify = null;

	private ButtonObject m_btnOUT = null;// ����

	private ButtonObject m_btnSave = null;

	private ButtonObject m_btnCancel = null;

	private ButtonObject m_btnDiscard = null;

	private ButtonObject m_btnCopy = null;

	private ButtonObject m_btnSendAudit = null;

	// ִ��/
	private ButtonObject m_btnActions = null;

	private ButtonObject m_btnApprove = null;

	private ButtonObject m_btnUnApprove = null;

	private ButtonObject m_btnClose = null;

	private ButtonObject m_btnOpen = null;

	// ���
	private ButtonObject m_btnBrowses = null;

	private ButtonObject m_btnQuery = null;

	private ButtonObject m_btnFirst = null;

	private ButtonObject m_btnPrev = null;

	private ButtonObject m_btnNext = null;

	private ButtonObject m_btnLast = null;

	private ButtonObject m_btnRefresh = null;

	private ButtonObject m_btnRefreshList = null;

	//
	private ButtonObject m_btnList = null;

	private ButtonObject boOnHandShowHidden = null;

	/* ��Ƭ����Ϣ���Ĺ��� */
	private ButtonObject m_btnOthersFuncs = null;// "��������"

	private ButtonObject m_btnDocument = null;// "�ĵ�����"

	private ButtonObject m_btnOthersQry = null;// "������ѯ"

	private ButtonObject m_btnPriceInfo = null;// "�۸���֤��"

	private ButtonObject m_btnWorkFlowBrowse = null; // ״̬��ѯ

	private ButtonObject m_btnUsable = null;// "������ѯ"

	public ButtonObject m_btnCombin = null;// "�ϲ���ʾ"

	private ButtonObject m_btnLinkBillsBrowse = null;// "����"

	// ��ӡ
	private ButtonObject m_btnPrints = null;// "��ӡ"

	private ButtonObject m_btnPrint = null;// "��ӡ"

	private ButtonObject m_btnPrintPreview = null;// "Ԥ��"

	private ButtonObject m_btnPrintList = null;// "�б��ӡ"

	private ButtonObject m_btnPrintListPreview = null;// "Ԥ��"

	// �б�ť����
	private ButtonObject m_btnSelectAll = null;// "ȫѡ"

	private ButtonObject m_btnSelectNo = null;// "ȫ��"

	private ButtonObject m_btnModifyList = null;// "�б��޸�"

	private ButtonObject m_btnDiscardList = null;// "�б�����"

	// �б��ѯ
	private ButtonObject m_btnQueryList = null;// "�б��ѯ"

	private ButtonObject m_btnCard = null;// "�л�"

	private ButtonObject m_btnQueryForAuditList = null;// "�б�״̬��ѯ"

	private ButtonObject m_btnUsableList = null;// "�б������ѯ"

	private ButtonObject m_btnDocumentList = null;// "�б��ĵ�����"

	// ��Ϣ���İ�ť��(����,״̬��ѯ,�ĵ�����)
	private ButtonObject m_btnAudit = null;// "����"

	private ButtonObject m_btnUnAudit = null;// "����"

	private ButtonObject m_btnOthersAuditCenter = null;// "����"

	private ButtonObject[] m_btnsAuditCenter = null;// ��Ϣ���İ�ť

	private ButtonObject m_btnRevise = null;// "�޶�"

	private boolean isRevise = false;

	private boolean reviseSave = false;

	// �ɹ���˾(��¼�༭ǰֵ)---�����ڲ����ϸߵ�����»�������
	private static String m_strPurCorpIdOld = null;

	// ��¼�Ƿ��ѯ������ѯ�������á�ˢ�¡�����
	private boolean m_bQueried = false;

	// ��ѯģ���Ƿ��ʼ��
	private boolean m_butnQuery = false;

	// �Ƿ�����Զ�����(����Ϣ���ĵ��빺�������Զ�����)
	private boolean isCanAutoAddLine = true;

	/** ����ʱ�����⴦�� */
	private boolean isFrmCopy = false;

	private ATPForOneInvMulCorpUI m_atpDlg = null;

	// �Ƿ������빺��(���������빺��)
	private boolean m_bAdd = false;

	// ȡ����ť�Ƿ��Ѿ�����
	private boolean m_bCancel = false;

	private boolean m_bEdit = false;

	private boolean m_bEditBodyInv = false;

	// �Ƿ�������Ƶ���
	private boolean isAllowedModifyByOther = false;

	// ���ݼ�������
	private BillCardPanel m_billPanel = null;

	// �Ƿ��¸����˵�
	private boolean m_bIsSubMenuPressed = false;

	// ��ǰ������ҵ������ v55ɾ��
	// ��ǰ������ҵ������
	// private ButtonObject m_bizButton = null;

	// ���﷭�빤����
	private static NCLangRes m_lanResTool = NCLangRes.getInstance();

	// �����������
	class IBillRelaSortListener2Body implements IBillRelaSortListener2 {
		public Object[] getRelaSortObjectArray() {
			return PrayUI.this.getRelaSortObjectArrayBody();
		}
	}

	/** �������õ�����ID */
	private String m_cauditid = null;

	private UIComboBox m_comPraySource = null;

	private UIComboBox m_comPraySource1 = null;

	private UIComboBox m_comPrayType = null;

	private UIComboBox m_comPrayType1 = null;

	// ��ѯģ��
	private PrayUIQueryDlg m_condClient = null;

	// ������С��λ
	// private nc.vo.pub.para.SysInitVO m_exchangeInitVO = null;
	// ������
	private FreeItemRefPane m_freeItem = null;

	/* �������ݳ�ʼ״̬������ */
	// private int m_iInitRowCount = 20;
	/* ��¼ִ�а�ť���ڿ�Ƭ��ť���е�λ�� */
	private final int m_iPOS_m_btnAction = 5;

	// �Ƿ�Ϊ�۸���֤��
	private BillListPanel m_listPanel = null;

	// ��ǰ���빺����ţ�Ϊ���·�ҳ����
	private int m_nPresentRecord = 0;

	// �����б�/��Ƭ״̬
	private int m_nUIState = 0;

	// �۸���֤���ѯ�Ի���
	private QueryConditionClient m_priceDlg = null;

	private PrintEntry m_print = null;

	// ������ӡ�����б��ͷ�����к�
	protected ArrayList listSelectBillsPos = null;

	// �빺����ID����
	private String m_sDeptID = null;

	// ����������ֶ���
	private final String m_sInvMngIDItemKey = "cinventorycode";

	// ��λ���룬ϵͳӦ�ṩ������ȡ
	private String m_sLoginCorpId = getCorpPrimaryKey();

	// ���棬����빺������
	private PraybillVO[] m_VOs = null;

	private int nAssistUnitDecimal = 2;

	private int nExchangeDecimal = 2;

	/** ���ݾ��� */
	private int nMeasDecimal = 2;

	private int nMoneyDecimal = 2;

	private int nPriceDecimal = 2;

	/* ������ӡ���� */
	private ScmPrintTool printList = null; // �����

	private ScmPrintTool printCard = null;

	/* ��ǰ��¼����Ա��Ȩ�޵Ĺ�˾[] */
	private String[] saPkCorp = null;

	// �Ƿ���Ҫ����
	private PraybillVO m_SaveVOs = null;

	private boolean isAlreadySendToAudit = false;

	// ��ǰ��¼�˺͵�½����
	private String m_sLoginDate = getClientEnvironment().getDate().toString();

	// ������д���ҵ������
	Hashtable m_hashBizType = new Hashtable();

	Hashtable isWorkFlow = new Hashtable();

	Hashtable m_hashInvbasIds = new Hashtable();

	// ���Ĭ�ϲ���
	UIRefPane invrefpane = null;

	// ��ʾ���������ִ������
	protected boolean m_bOnhandShowHidden = false;

	protected UISplitPane pnlCardAndBc = null;

	protected UIPanel m_pnlOnHand = null;

	// ���л�ʱ�ִ�����ʾ��ڲ���
	protected OnHandRefreshVO m_voLineOnHand = new OnHandRefreshVO();

	// ����Ƿ񼯲�
	private static boolean bIsCentralPur = false;

	// �ɹ���ǰ�ڲ���
	private HashMap m_hmapPurAdvanceInfo = null;

	private static HashMap h_InvSource = new HashMap();

	private static String sReqCorpTemp = null;
	// ���������ϵ����������Ϣ:�Ƿ�ɹ���,�ɹ���֯
	private static HashMap<String, String[]> h_ProduceInfo = new HashMap<String, String[]>();

	private static ArrayList<String> al_BizInfo = new ArrayList<String>();

	private static HashMap m_hBiztype = new HashMap();

	/**
	 * PraybillClient ������ע�⡣
	 */
	public PrayUI() {
		super();
		init();
	}

	public PrayUI(FramePanel pl) {
		super();
		m_sModelcode = pl.getModuleCode();
		init();
	}

	/**
	 * PrayUI ������ע�⡣
	 */
	public PrayUI(String pk_corp, String billType, String businessType,
			String operator, String billID) {

		super();

		initi();

		PraybillVO vo = null;

		try {
			vo = PraybillHelper.queryPrayVoByHid(billID);
			if (vo != null) {
				// Logger.debug("��ѯ������");
				m_VOs = new PraybillVO[] { vo };
				m_nPresentRecord = 0;
				setVoToBillCard(m_nPresentRecord, "");
				Logger.debug("�ɹ���ʾ����");
			}
		} catch (Exception e) {
			SCMEnv.out(e);
		}
	}

	/**
	 * ��������:��Ӧ�¼�����
	 * 
	 * @param event
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent event) {
	}

	/**
	 * ��������:�Զ������PK(��ͷ)
	 */
	public static void afterEditWhenHeadDefPK(BillCardPanel bcp, BillEditEvent e) {
		DefSetTool.afterEditHead(bcp.getBillData(), e.getKey(), "pk_defdoc"
				+ e.getKey().substring("vdef".length(), e.getKey().length()));
	}

	/**
	 * ��������:�Զ������PK(����)
	 */
	public static void afterEditWhenBodyDefPK(BillCardPanel bcp, BillEditEvent e) {

		DefSetTool.afterEditBody(bcp.getBillModel(), e.getRow(), e.getKey(),
				"pk_defdoc"
						+ e.getKey().substring("vdef".length(),
								e.getKey().length()));
	}

	/**
	 * ��������:�༭���¼�
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void afterEdit(BillEditEvent e) {

		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000075")/* @res"���ڱ༭" */);

		if (m_nUIState == 0 && m_bEdit) {
			if (e.getPos() == BillItem.HEAD) {
				// �Զ������PK
				afterEditWhenHeadDefPK(getBillCardPanel(), e);

				if (e.getKey().equals("dpraydate")) {
					/*
					 * ��ǰ��:�ݲ�֧�֣����賡�����û���¼�����빺����Ȼ����¼���������
					 * ���Ҫ֧�֣��޸ı�ͷ�������¼�������������ڼ����鶩�����ڣ�Ҫ����ע��Ч������
					 * calculateAdvDaysHead(this, getBillCardPanel(), e);
					 */
				}
				// ��Ա
				else if (e.getKey().equals("cpraypsn")) {
					afterEditWhenHeadPsn(getBillCardPanel(), m_sDeptID, e);
				}
				// ��Ŀ
				else if (e.getKey().equals("cprojectidhead")) {
					afterEditHeadCproject(getBillCardPanel(), e);
				} else if (e.getKey().equals("pk_defdoc7")
						|| e.getKey().equals("vdef7")) {
					checkDef7();
				}
				if (e.getKey().indexOf("pk_defdoc") >= 0
						|| e.getKey().indexOf("vdef") >= 0) {
					checkDef(0, e);
				}
				// ũ�ʼ���-200803271100034128-֧�ֱ�ͷ�Զ�����༭��ʽִ��
				getBillCardPanel().execHeadEditFormulas();
			} else if (e.getPos() == BillItem.BODY) {
				// �Զ������PK
				afterEditWhenBodyDefPK(getBillCardPanel(), e);
				// �ɹ�Ա
				if ("cemployeename".equals(e.getKey())) {
					afterEditWhenBodyEmployee(this, getBillCardPanel(),
							m_sLoginCorpId, e);
				}
				// ��Ŀ
				else if ("cprojectname".equals(e.getKey())) {
					afterEditWhenBodyProj(getBillCardPanel(), e);
				}
				// ������
				else if (e.getKey().trim().equals("vfree")) {
					PoEditTool.afterEditWhenBodyFree(getBillCardPanel()
							.getBillModel(), e.getRow(), "vfree");
					// afterEditWhenBodyFree(getBillCardPanel(), e);
				}
				// �������
				else if (e.getKey().trim().equals("cinventorycode")) {
					m_bEditBodyInv = true;
					afterEditWhenBodyInventory(this, getBillCardPanel(),
							m_sLoginCorpId, getClientEnvironment()
									.getCorporation().getUnitname(), e);
					m_bEditBodyInv = false;
				}
				// ��ע
				else if (e.getKey().trim().equals("vmemo")) {
					afterEditWhenBodyMemo(getBillCardPanel(), e);
				}
				// �к�
				else if (e.getKey().equals("crowno")) {
					afterEditWhenBodyRowNo(getBillCardPanel(), e, "crowno");
				}
				// �ɹ���֯
				else if (e.getKey().equals("cpurorganizationname")) {
					afterEditWhenBodyPurOrg(this, getBillCardPanel(),
							m_sLoginCorpId, e);
				}
				// �ɹ���˾
				else if (e.getKey().equals("pk_purcorp")) {
					afterEditWhenBodyPurCorp(getBillCardPanel(), e);
				}
				// ����˾
				else if (e.getKey().equals("reqcorpname")) {
					afterEditWhenBodyReqcorp(this, getBillCardPanel(), e,
							m_sLoginCorpId);
				}
				// ��������֯
				else if (e.getKey().equals("reqstoorgname")) {
					afterEditWhenBodyReqStoOrg(this, getBillCardPanel(), e,
							m_sLoginCorpId);
				}
				// ����ֿ�
				else if (e.getKey().equals("cwarehousename")) {
					afterEditWhenBodyReqWareHouse(this, getBillCardPanel(), e,
							m_sLoginCorpId);
				}
				// ���鹩Ӧ��
				else if (e.getKey().equals("cvendorname")) {
					afterEditWhenBodyVendor(getBillCardPanel(), e);
				}
				// ��������
				else if (e.getKey().trim().equals("ddemanddate")) {
					afterEditWhenBodyDemandDate(this, getBillCardPanel(), e);
				}
				// ������
				else if (e.getKey().trim().equals("npraynum")) {
					afterEditWhenBodyNum(this, getBillCardPanel(), e);
					// ����빺����
					afterEditWhenBodyPrayNum(this, getBillCardPanel(), e);
				}
				// ������
				else if (e.getKey().trim().equals("nassistnum")) {
					afterEditWhenBodyAssNum(this, getBillCardPanel(), e);
				}
				// ������
				else if (e.getKey().trim().equals("nexchangerate")) {
					afterEditWhenBodyRate(this, getBillCardPanel(), e);
				}
				// ������
				else if (e.getKey().trim().equals("cassistunitname")) {
					afterEditWhenBodyAssist(this, getBillCardPanel(), e);
					// ����빺����
					afterEditWhenBodyPrayNum(this, getBillCardPanel(), e);
				}
				// ���鵥��
				else if (e.getKey().trim().equals("nsuggestprice")) {
					afterEditWhenBodySuggPrice(this, getBillCardPanel(), e);
				}
				// ���
				else if (e.getKey().trim().equals("nmoney")) {
					afterEditWhenBodyMoney(this, getBillCardPanel(), e);
				} else if (e.getKey().indexOf("pk_defdoc") >= 0
						|| e.getKey().indexOf("vdef") >= 0) {
					checkDef(1, e);
				}
			}
		}
		// �ޱ�����
		if (nc.ui.pu.pub.PuTool.isLastCom(getBillCardPanel(), e)
				&& getBillCardPanel().getBillModel().getRowCount() <= 0) {
			onAppendLine(getBillCardPanel(), this);
		}
		PuTool.setFocusOnLastCom(getBillCardPanel(), e);
		// ��������������ٴ�ѡ����ִ���ԭ���������
		// UIRefPane refpane = (UIRefPane) getBillCardPanel().getBodyItem(
		// "cinventorycode").getComponent();
		// refpane.setPK(null);
		// UIRefPane refpaneM = (UIRefPane) getBillCardPanel().getBodyItem(
		// "cvendorname").getComponent();
		// refpaneM.setPK(null);
	}

	/**
	 * ���������������༭����˾����������֯Ӧ��Ϊ��
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param bcp
	 * @param e
	 *            <p>
	 * @author liyc
	 * @time 2007-8-20 ����04:46:10
	 */
	public static void afterEditWhenBodyReqcorp(ToftPanel uiPanel,
			BillCardPanel bcp, BillEditEvent e, String strCorp) {
		if (!bcp.getBillData().getBillModel()
				.isCellEditable(
						e.getRow(),
						bcp.getBillData().getBillModel().getBodyColByKey(
								"reqcorpname"))) {
			// �������Ϊ���ɱ༭�����ûؾ�ֵ
			bcp.setBodyValueAt(sReqCorpTemp, e.getRow(), "pk_reqcorp");
			bcp.setBodyValueAt(e.getOldValue(), e.getRow(), "reqcorpname");
		} else {
			// �༭����˾����������֯��ҵ������Ӧ��Ϊ��
			bcp.setBodyValueAt(null, e.getRow(), "reqstoorgname");
			bcp.setBodyValueAt(null, e.getRow(), "pk_reqstoorg");
			bcp.setBodyValueAt(null, e.getRow(), "cbiztype");
			bcp.setBodyValueAt(null, e.getRow(), "cbiztypename");
			// // ����˾PK
			// String strPkRegCorp =
			// PuPubVO.getString_TrimZeroLenAsNull(bcp.getBodyValueAt(e.getRow(),
			// "pk_reqcorp"));
			// // ���PK
			// String strPkInv =
			// PuPubVO.getString_TrimZeroLenAsNull(bcp.getBodyValueAt(e.getRow(),
			// "cbaseid"));

			try {
				setCpurorganization(bcp, e.getRow(), e.getRow());
			} catch (BusinessException e1) {
				// ��־�쳣
				nc.vo.scm.pub.SCMEnv.out(e1);
			}

			// �繫˾������������֯
			String strReq = (String) bcp.getBodyValueAt(e.getRow(),
					"pk_reqcorp");
			UIRefPane refPane = (UIRefPane) bcp.getBodyItem("reqstoorgname")
					.getComponent();
			refPane.setPk_corp(strReq);
		}
	}

	/**
	 * �༭���¼�--��������ֿ�
	 * 
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyReqWareHouse(ToftPanel ui,
			BillCardPanel bcp, BillEditEvent e, String strCorp) {
		String strPkCalBody = PuPubVO.getString_TrimZeroLenAsNull(bcp
				.getBodyValueAt(e.getRow(), "pk_reqstoorg"));
		String strPkWare = PuPubVO.getString_TrimZeroLenAsNull(bcp
				.getBodyValueAt(e.getRow(), "cwarehouseid"));
		if (strPkWare == null) {
			SCMEnv.out("�޸�����ֿ�ʱ��������������֯������˾Ĭ��ֵ,�ֿ�Ϊ�գ�ֱ�ӷ��ء�");
			return;
		}
		Object[] oaRet = null;
		try {
			// ������������֯
			if (strPkCalBody == null) {
				oaRet = (Object[]) CacheTool.getCellValue("bd_stordoc",
						"pk_stordoc", "pk_calbody", strPkWare);
				if (oaRet == null || oaRet.length == 0 || oaRet[0] == null
						|| oaRet[0].toString().trim().length() == 0) {
					SCMEnv
							.out("���ݲֿ⵵��ID[IDֵ����" + strPkWare
									+ "��]���ܻ�ȡ���������֯ID!");
				} else {
					strPkCalBody = oaRet[0].toString().trim();
					// -----------------
					bcp
							.setBodyValueAt(strPkCalBody, e.getRow(),
									"pk_reqstoorg");
					oaRet = (Object[]) CacheTool.getCellValue("bd_calbody",
							"pk_calbody", "bodyname", strPkCalBody);
					if (oaRet == null || oaRet.length == 0 || oaRet[0] == null
							|| oaRet[0].toString().trim().length() == 0) {
						SCMEnv.out("�������������֯ID[IDֵ����" + strPkCalBody
								+ "��]���ܻ�ȡ���������֯����!");
					} else {
						// --------------
						bcp.setBodyValueAt(oaRet[0], e.getRow(),
								"reqstoorgname");
					}
				}

				((UIRefPane) bcp.getBodyItem("reqstoorgname").getComponent())
						.setPK(strPkCalBody);
				if (((UIRefPane) bcp.getBodyItem("reqstoorgname")
						.getComponent()).getRefModel().getPkValue() == null) {
					bcp.setBodyValueAt(null, e.getRow(), "pk_reqstoorg");
					bcp.setBodyValueAt(null, e.getRow(), "reqstoorgname");
				}
				// since V56, ֧���޸Ŀ����֯�����¼�����ǰ��
				afterEditWhenBodyReqStoOrg(ui, bcp, e, strCorp);
			}
		} catch (BusinessException be) {
			SCMEnv.out(be.getMessage());
		}
	}

	/**
	 * �༭���¼�--������������֯
	 * 
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyReqStoOrg(ToftPanel ui,
			BillCardPanel bcp, BillEditEvent event, String strCorp) {
		if (event.getKey().equals("reqstoorgname")) {
			bcp.getBillModel().setValueAt(null, event.getRow(),
					"cwarehousename");
			bcp.getBillModel().setValueAt(null, event.getRow(), "cwarehouseid");
		}
		BillModel bm = bcp.getBillModel();
		String pk_reqstoorg = (String) bm.getValueAt(event.getRow(),
				"pk_reqstoorg");
		if (!PuTool.isPKMatchRefModel("pk_reqstoorg", "reqstoorgname", bcp,
				event.getRow())) {
			bm.setValueAt("", event.getRow(), "reqstoorgname");
			bm.setValueAt("", event.getRow(), "pk_reqstoorg");
			return;
		}
		Object oTemp = bcp.getBodyValueAt(event.getRow(), "pk_reqcorp");

		if (pk_reqstoorg != null && oTemp == null) {
			try {
				// ��������˾
				Object[] oaRet = null;
				oaRet = (Object[]) CacheTool.getCellValue("bd_calbody",
						"pk_calbody", "pk_corp", pk_reqstoorg);
				if (oaRet != null && oaRet.length > 0 && oaRet[0] != null) {
					String pk_reqcorp = oaRet[0].toString();
					bcp
							.setBodyValueAt(pk_reqcorp, event.getRow(),
									"pk_reqcorp");
					UIRefPane reqcorpname = ((UIRefPane) bcp.getBodyItem(
							"reqcorpname").getComponent());
					reqcorpname.setPK(pk_reqcorp);
					oaRet = (Object[]) CacheTool.getCellValue("bd_corp",
							"pk_corp", "unitname", pk_reqcorp);
					if (oaRet != null && oaRet.length > 0 && oaRet[0] != null)
						bcp.setBodyValueAt(oaRet[0].toString(), event.getRow(),
								"reqcorpname");

				}
			} catch (Exception e) {
				SCMEnv.out("��ȡ��������֯��Ӧ������˾����...");
				SCMEnv.out(e.getMessage());
			}
		}

		String cpurcorp = bcp.getBodyValueAt(event.getRow(), "pk_purcorp") == null ? null
				: bcp.getBodyValueAt(event.getRow(), "pk_purcorp").toString();
		String strBaseId = bcp.getBodyValueAt(event.getRow(), "cbaseid") == null ? null
				: bcp.getBodyValueAt(event.getRow(), "cbaseid").toString();
		// ���Ĭ�ϲɹ���֯�͹�Ӧ������
		HashMap hVendorNo = null;
		try {
			hVendorNo = PraybillHelper
					.queryPurOrgAndVendor(null, new String[] { strBaseId },
							null, new String[] { cpurcorp });
		} catch (Exception e) {
		}
		UFDouble dPrices[] = null;
		if (hVendorNo != null && hVendorNo.containsKey(strBaseId + cpurcorp)) {
			// ���ù�Ӧ��
			ArrayList listValVendor = (ArrayList) hVendorNo.get(strBaseId
					+ cpurcorp);
			if (listValVendor != null && listValVendor.size() >= 3) {
				bm.setValueAt(listValVendor.get(0), event.getRow(),
						"cvendormangid");
				bm.setValueAt(listValVendor.get(1), event.getRow(),
						"cvendorbaseid");
				bm.setValueAt(listValVendor.get(2), event.getRow(),
						"cvendorname");
			} else {
				bm.setValueAt(null, event.getRow(), "cvendormangid");
				bm.setValueAt(null, event.getRow(), "cvendorbaseid");
				bm.setValueAt(null, event.getRow(), "cvendorname");
			}
		}
		try {
			dPrices = PraybillHelper.getRulePrice(null,
					new String[] { strBaseId }, pk_reqstoorg, strCorp);
		} catch (Exception e) {
		}
		if (dPrices != null) {
			bcp.setBodyValueAt(dPrices[0], event.getRow(), "nsuggestprice");
		}
		if (dPrices != null && dPrices.length > 0) {
			// ��Ҫ����ı�����
			UFDouble nSuggestPrice = dPrices[0];
			UFDouble nPrayNum = PuPubVO.getUFDouble_NullAsZero(bcp
					.getBodyValueAt(event.getRow(), "npraynum"));
			bcp.setBodyValueAt(nPrayNum.multiply(nSuggestPrice),
					event.getRow(), "nmoney");
		}

		try {
			// ���òɹ���֯
			setCpurorganization(bcp, event.getRow(), event.getRow());
			// ����ҵ������
			setBiztype(bcp, event.getRow(), event.getRow() + 1);
		} catch (BusinessException e) {
			// ��־�쳣
			SCMEnv.out(e);
		}
		// �޸���������֯,���鶩�������Զ��仯
		calculateAdvDays(ui, bcp, event);

	}

	/**
	 * �༭���¼�--���屸ע
	 * 
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyMemo(BillCardPanel bcp, BillEditEvent e) {
		UIRefPane nRefPanel = (UIRefPane) bcp.getBodyItem("vmemo")
				.getComponent();
		bcp.setBodyValueAt(nRefPanel.getText(), e.getRow(), "vmemo");
	}

	/**
	 * �༭���¼�--�к�
	 * 
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyRowNo(BillCardPanel bcp,
			BillEditEvent e, String strRowNoKey) {
		BillRowNo.afterEditWhenRowNo(bcp, e, strRowNoKey);
	}

	/**
	 * �༭���¼�--�ɹ���˾
	 * 
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyPurCorp(BillCardPanel bcp,
			BillEditEvent e) {
		bcp.getBillModel().setValueAt(null, e.getRow(), "cvendormangid");
		bcp.getBillModel().setValueAt(null, e.getRow(), "cvendorbaseid");
	}

	/**
	 * �༭���¼�--������
	 * 
	 * @param ui
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyRate(ToftPanel ui, BillCardPanel bcp,
			BillEditEvent e) {
		UFDouble nPrayNum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e
				.getRow(), "npraynum"));
		UFDouble nSuggestPrice = PuPubVO.getUFDouble_NullAsZero(bcp
				.getBodyValueAt(e.getRow(), "nsuggestprice"));
		String sBaseID = (String) bcp.getBillModel().getValueAt(e.getRow(),
				"cbaseid");
		String sCassId = (String) bcp.getBillModel().getValueAt(e.getRow(),
				"cassistunit");
		UFDouble nExchangeRate = nc.ui.pu.pub.PuTool.getInvConvRateValue(
				sBaseID, sCassId);
		// �޸ļ���������,�빺�����仯
		Object oTemp = bcp.getBodyValueAt(e.getRow(), "nexchangerate");
		if (oTemp != null && oTemp.toString().length() > 0)
			nExchangeRate = (UFDouble) oTemp;
		else
			nExchangeRate = null;
		if (nExchangeRate != null) {
			if (nExchangeRate.doubleValue() < 0) {
				MessageDialog.showErrorDlg(ui, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000040")
				/*
				 * @res "��������"
				 */, m_lanResTool.getStrByID("40040101", "UPP40040101-000045")
				/*
				 * @res "���������ʲ���Ϊ����"
				 */);
				return;
			}
			UFDouble nAssistNum = PuPubVO.getUFDouble_NullAsZero(bcp
					.getBodyValueAt(e.getRow(), "nassistnum"));
			if (nPrayNum.doubleValue() != nc.vo.scm.pu.VariableConst.ZERO
					.doubleValue()) {
				bcp.setBodyValueAt(nPrayNum.div(nExchangeRate), e.getRow(),
						"nassistnum");
			} else {
				if (nAssistNum.doubleValue() != nc.vo.scm.pu.VariableConst.ZERO
						.doubleValue()) {
					bcp.setBodyValueAt(nAssistNum.multiply(nExchangeRate), e
							.getRow(), "npraynum");
				}
			}
			// �빺�����仯,����Զ��仯
			nPrayNum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e
					.getRow(), "npraynum"));
			if (nSuggestPrice != null) {
				final double d = nPrayNum.doubleValue()
						* nSuggestPrice.doubleValue();
				bcp.setBodyValueAt(new UFDouble(d), e.getRow(), "nmoney");
			}
			// �����빺�����Ͳɹ���ǰ�ڣ��Զ��޸��������ںͽ��鶩������
			// ������������Ѿ�����,���������ڲ��޸�.
			calculateAdvDays(ui, bcp, e);
		} else {
			bcp.setBodyValueAt(null, e.getRow(), "nmoney");
			bcp.setBodyValueAt(null, e.getRow(), "ddemanddate");
			bcp.setBodyValueAt(null, e.getRow(), "dsuggestdate");
		}
	}

	/**
	 * �༭���¼�--��������
	 * 
	 * @param bcp
	 * @param e
	 *            <p>
	 *            since v56, �����㷨����
	 */
	public static void afterEditWhenBodyDemandDate(ToftPanel ui,
			BillCardPanel bcp, BillEditEvent e) {
		// ��ͷ�빺����
		UFDate dPrayDate = PuPubVO.getUFDate(bcp.getHeadItem("dpraydate")
				.getValueObject());
		if (dPrayDate == null) {
			return;
		}
		// ��������ʱ��
		UFDate objDate = PuPubVO.getUFDate(bcp.getBodyValueAt(e.getRow(),
				"ddemanddate"));
		if (objDate != null && dPrayDate.after(objDate)) {
			MessageDialog.showErrorDlg(ui, m_lanResTool.getStrByID("40040101",
					"UPP40040101-000081")/* @res"�빺������" */, "�������ڱ��������빺����");
			return;
		}
		// �޸���������,���鶩�������Զ��仯
		final int nDays = getAdvanceDays(ui, bcp, e);
		if (nDays >= 0) {
			UFDate dateDemand = PuPubVO.getUFDate(bcp.getBodyValueAt(
					e.getRow(), "ddemanddate"));
			if (dateDemand != null) {
				UFDate d2 = dateDemand.getDateAfter(-nDays);
				if (!AdDayVO.isDateOverflow(d2)) {
					bcp.setBodyValueAt(d2, e.getRow(), "dsuggestdate");
				}
			}
		}
	}

	/**
	 * �༭���¼�--������
	 * 
	 * @param ui
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyAssNum(ToftPanel ui, BillCardPanel bcp,
			BillEditEvent e) {
		UFDouble nPrayNum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e
				.getRow(), "npraynum"));
		UFDouble nSuggestPrice = PuPubVO.getUFDouble_NullAsZero(bcp
				.getBodyValueAt(e.getRow(), "nsuggestprice"));
		String sBaseID = (String) bcp.getBillModel().getValueAt(e.getRow(),
				"cbaseid");
		String sCassId = (String) bcp.getBillModel().getValueAt(e.getRow(),
				"cassistunit");
		UFDouble nExchangeRate = nc.ui.pu.pub.PuTool.getInvConvRateValue(
				sBaseID, sCassId);
		if (nExchangeRate != null)
			nExchangeRate = nExchangeRate
					.setScale(bcp.getBodyItem("nexchangerate")
							.getDecimalDigits(), UFDouble.ROUND_HALF_UP);
		// �޸ĸ�����,�빺�����仯
		Object oTemp = bcp.getBodyValueAt(e.getRow(), "nassistnum");
		UFDouble nAssistNum = null;
		if (oTemp != null && oTemp.toString().length() > 0)
			nAssistNum = (UFDouble) oTemp;
		if (nAssistNum != null) {
			if (nAssistNum.doubleValue() < 0) {
				MessageDialog.showErrorDlg(ui, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000040")
				/*
				 * @res "��������"
				 */, m_lanResTool.getStrByID("40040101", "UPP40040101-000044")/*
																				 * @res
																				 * "����������Ϊ����"
																				 */);
				return;
			}
			// �����Ƿǹ̶������ʣ����Բ����� m_nExchangeRate, Ҫ��ģ����ȡ
			Object exc = bcp.getBillModel().getValueAt(e.getRow(),
					"nexchangerate");
			if (exc != null && !exc.toString().trim().equals(""))
				nExchangeRate = new UFDouble(exc.toString().trim());
			if (nExchangeRate != null) {
				final double d = nAssistNum.doubleValue()
						* nExchangeRate.doubleValue();
				bcp.setBodyValueAt(new UFDouble(d), e.getRow(), "npraynum");
			}
			// �빺�����仯,����Զ��仯
			nPrayNum = (UFDouble) bcp.getBodyValueAt(e.getRow(), "npraynum");
			if (nSuggestPrice != null && nPrayNum != null) {
				final double d = nPrayNum.doubleValue()
						* nSuggestPrice.doubleValue();
				bcp.setBodyValueAt(new UFDouble(d), e.getRow(), "nmoney");
			}
			// �����빺�����Ͳɹ���ǰ�ڣ��Զ��޸��������ںͽ��鶩������
			// ������������Ѿ�����,���������ڲ��޸�.
			calculateAdvDays(ui, bcp, e);
			//
			bcp.setBodyValueAt(nPrayNum, e.getRow(), "npraynum0");

		} else {
			bcp.setBodyValueAt(null, e.getRow(), "nmoney");
			bcp.setBodyValueAt(null, e.getRow(), "ddemanddate");
			bcp.setBodyValueAt(null, e.getRow(), "dsuggestdate");
			bcp.setBodyValueAt(null, e.getRow(), "npraynum");
		}
	}

	/**
	 * �༭���¼�--���
	 * 
	 * @param ui
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyMoney(ToftPanel ui, BillCardPanel bcp,
			BillEditEvent e) {
		UFDouble nPrayNum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e
				.getRow(), "npraynum"));
		UFDouble nSuggestPrice = PuPubVO.getUFDouble_NullAsZero(bcp
				.getBodyValueAt(e.getRow(), "nsuggestprice"));
		UFDouble nMoney = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e
				.getRow(), "nmoney"));
		String sBaseID = (String) bcp.getBillModel().getValueAt(e.getRow(),
				"cbaseid");
		String sCassId = (String) bcp.getBillModel().getValueAt(e.getRow(),
				"cassistunit");
		UFDouble nExchangeRate = nc.ui.pu.pub.PuTool.getInvConvRateValue(
				sBaseID, sCassId);
		boolean bFixedFlag = PuTool.isFixedConvertRate(sBaseID, sCassId);
		// ���仯�����鵥���Զ��仯
		if (nMoney != null) {
			if (nMoney.doubleValue() < 0) {
				MessageDialog.showErrorDlg(ui, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000040")/* "��������" */,
						m_lanResTool.getStrByID("40040101",
								"UPP40040101-000043")/* "����Ϊ����" */);
				return;
			}
			if (nPrayNum != null && nPrayNum.doubleValue() != 0.0) {
				final double d = nMoney.doubleValue() / nPrayNum.doubleValue();
				bcp
						.setBodyValueAt(new UFDouble(d), e.getRow(),
								"nsuggestprice");
			} else if (nSuggestPrice != null
					&& nSuggestPrice.doubleValue() != 0.0) {
				final double d = nMoney.doubleValue()
						/ nSuggestPrice.doubleValue();
				bcp.setBodyValueAt(new UFDouble(d), e.getRow(), "npraynum");

				// �̶�������,���������빺�����仯;����,���������빺�����仯
				if (bFixedFlag) {
					if (nExchangeRate != null
							&& nExchangeRate.doubleValue() != 0.0) {
						final double d0 = nPrayNum.doubleValue()
								/ nExchangeRate.doubleValue();
						bcp.setBodyValueAt(new UFDouble(d0), e.getRow(),
								"nassistnum");
					}
				} else {
					UFDouble nAssistNum = (UFDouble) bcp.getBodyValueAt(e
							.getRow(), "nassistnum");
					if (nAssistNum != null && nAssistNum.doubleValue() != 0.0) {
						final double d0 = nPrayNum.doubleValue()
								/ nAssistNum.doubleValue();
						bcp.setBodyValueAt(new UFDouble(d0), e.getRow(),
								"nexchangerate");
					} else {
						bcp.setBodyValueAt(null, e.getRow(), "nexchangerate");
					}
				}
			}
		} else {
			bcp.setBodyValueAt(null, e.getRow(), "npraynum");
			bcp.setBodyValueAt(null, e.getRow(), "nassistnum");
		}
	}

	/**
	 * �༭���¼�--���鵥��
	 * 
	 * @param ui
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodySuggPrice(ToftPanel ui,
			BillCardPanel bcp, BillEditEvent e) {
		UFDouble nPrayNum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e
				.getRow(), "npraynum"));
		UFDouble nSuggestPrice = PuPubVO.getUFDouble_NullAsZero(bcp
				.getBodyValueAt(e.getRow(), "nsuggestprice"));
		UFDouble nMoney = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e
				.getRow(), "nmoney"));
		String sBaseID = (String) bcp.getBillModel().getValueAt(e.getRow(),
				"cbaseid");
		String sCassId = (String) bcp.getBillModel().getValueAt(e.getRow(),
				"cassistunit");
		Object[] info = nc.ui.scm.inv.InvTool.getInvConvRateInfo(sBaseID,
				sCassId);
		UFDouble nExchangeRate = null;
		boolean bFixedFlag = false;
		if (info != null) {
			nExchangeRate = (UFDouble) info[0];
			bFixedFlag = ((UFBoolean) info[1]).booleanValue();
		}

		// ���鵥�۱仯������Զ��仯
		if (nSuggestPrice != null) {
			if (nSuggestPrice.doubleValue() < 0) {
				MessageDialog.showErrorDlg(ui, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000040")/* "��������" */,
						m_lanResTool.getStrByID("40040101",
								"UPP40040101-000042")/* "���鵥�۲���Ϊ����" */);
				return;
			}
			if (nPrayNum != null) {
				final double d = nPrayNum.doubleValue()
						* nSuggestPrice.doubleValue();
				bcp.setBodyValueAt(new UFDouble(d), e.getRow(), "nmoney");
			} else if (nMoney != null && nSuggestPrice.doubleValue() != 0.0) {
				final double d = nMoney.doubleValue()
						/ nSuggestPrice.doubleValue();
				bcp.setBodyValueAt(new UFDouble(d), e.getRow(), "npraynum");
				// �̶�������,���������빺�����仯;����,���������빺�����仯
				if (bFixedFlag) {
					if (nExchangeRate != null
							&& nExchangeRate.doubleValue() != 0.0) {
						final double d0 = nPrayNum.doubleValue()
								/ nExchangeRate.doubleValue();
						bcp.setBodyValueAt(new UFDouble(d0), e.getRow(),
								"nassistnum");
					}
				} else {
					UFDouble nAssistNum = (UFDouble) bcp.getBodyValueAt(e
							.getRow(), "nassistnum");
					if (nAssistNum != null && nAssistNum.doubleValue() != 0.0) {
						final double d0 = nPrayNum.doubleValue()
								/ nAssistNum.doubleValue();
						bcp.setBodyValueAt(new UFDouble(d0), e.getRow(),
								"nexchangerate");
					} else {
						bcp.setBodyValueAt(null, e.getRow(), "nexchangerate");
					}
				}
			}
		} else
			bcp.setBodyValueAt(null, e.getRow(), "nmoney");
	}

	/**
	 * �༭���¼�--���鵥�ۣ������� linsf
	 * 
	 * @param ui
	 * @param bcp
	 * @param beginRow
	 * @param endRow
	 */
	public static void afterEditWhenBodySuggPrice(ToftPanel ui,
			BillCardPanel bcp, int beginRow, int endRow) {
		int len = endRow - beginRow;
		UFDouble[] nPrayNum = new UFDouble[len];
		UFDouble[] nSuggestPrice = new UFDouble[len];
		UFDouble[] nMoney = new UFDouble[len];
		String[] sBaseID = new String[len];
		String[] sCassId = new String[len];
		int iCounter = 0;// ������
		for (int row = beginRow; row < endRow; row++) {
			nPrayNum[iCounter] = PuPubVO.getUFDouble_NullAsZero(bcp
					.getBodyValueAt(row, "npraynum"));
			nSuggestPrice[iCounter] = PuPubVO.getUFDouble_NullAsZero(bcp
					.getBodyValueAt(row, "nsuggestprice"));
			nMoney[iCounter] = PuPubVO.getUFDouble_NullAsZero(bcp
					.getBodyValueAt(row, "nmoney"));
			sBaseID[iCounter] = (String) bcp.getBillModel().getValueAt(row,
					"cbaseid");
			sCassId[iCounter] = (String) bcp.getBillModel().getValueAt(row,
					"cassistunit");
			iCounter++;
		}
		// info��һ����ά����
		Object[][] info = nc.ui.scm.inv.InvTool.getInvConvRateInfo(sBaseID,
				sCassId);

		UFDouble nExchangeRate = null;
		boolean bFixedFlag = false;
		iCounter = 0;
		for (int row = beginRow; row < endRow; row++) {

			if (info[iCounter] != null) {
				nExchangeRate = (UFDouble) info[iCounter][0];
				bFixedFlag = ((UFBoolean) info[iCounter][1]).booleanValue();
			}
			// ���鵥�۱仯������Զ��仯
			if (nSuggestPrice[iCounter] != null) {
				if (nSuggestPrice[iCounter].doubleValue() < 0) {
					MessageDialog.showErrorDlg(ui, m_lanResTool.getStrByID(
							"40040101", "UPP40040101-000040")/* "��������" */,
							m_lanResTool.getStrByID("40040101",
									"UPP40040101-000042")/* "���鵥�۲���Ϊ����" */);
					return;
				}
				if (nPrayNum != null && nPrayNum[iCounter] != null) {
					final double d = nPrayNum[iCounter].doubleValue()
							* nSuggestPrice[iCounter].doubleValue();
					bcp.setBodyValueAt(new UFDouble(d), row, "nmoney");
				} else if (nMoney != null
						&& nSuggestPrice[iCounter].doubleValue() != 0.0) {
					final double d = nMoney[iCounter].doubleValue()
							/ nSuggestPrice[iCounter].doubleValue();
					bcp.setBodyValueAt(new UFDouble(d), row, "npraynum");
					// �̶�������,���������빺�����仯;����,���������빺�����仯
					if (bFixedFlag) {
						if (nExchangeRate != null
								&& nExchangeRate.doubleValue() != 0.0) {
							final double d0 = nPrayNum[iCounter].doubleValue()
									/ nExchangeRate.doubleValue();
							bcp.setBodyValueAt(new UFDouble(d0), row,
									"nassistnum");
						}
					} else {
						UFDouble nAssistNum = (UFDouble) bcp.getBodyValueAt(
								row, "nassistnum");
						if (nAssistNum != null
								&& nAssistNum.doubleValue() != 0.0) {
							final double d0 = nPrayNum[iCounter].doubleValue()
									/ nAssistNum.doubleValue();
							bcp.setBodyValueAt(new UFDouble(d0), row,
									"nexchangerate");
						} else {
							bcp.setBodyValueAt(null, row, "nexchangerate");
						}
					}
				}
			} else
				bcp.setBodyValueAt(null, row, "nmoney");

			iCounter++;
		}
	}

	/**
	 * �༭���¼�--������
	 * 
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyAssist(ToftPanel uiPanel,
			BillCardPanel bcp, BillEditEvent e) {
		int iRow = e.getRow();
		// �����������ID
		String sBaseID = (String) bcp.getBillModel()
				.getValueAt(iRow, "cbaseid");
		// ����������
		String sCassId = (String) bcp.getBillModel().getValueAt(iRow,
				"cassistunit");
		//
		if (e.getValue() == null
				|| e.getValue().toString().trim().length() == 0) {
			bcp.getBillModel().setValueAt(null, iRow, "nassistnum");
			bcp.getBillModel().setValueAt(null, iRow, "nexchangerate");
			bcp.getBillModel().setValueAt(null, iRow, "cassistunitname");
			return;
		}
		// ��ȡ������
		UFDouble nExchangeRate = PuTool.getInvConvRateValue(sBaseID, sCassId);
		// //////////////////
		// UIRefPane ref = (UIRefPane)
		// bcp.getBodyItem("cassistunitname").getComponent();
		// String pk_measdoc = ref.getRefPK();
		// String name = ref.getRefName();
		String pk_measdoc = (String) ((DefaultConstEnum) e.getValue())
				.getValue();
		String name = (String) ((DefaultConstEnum) e.getValue()).getName();
		bcp.getBillModel().setValueAt(pk_measdoc, iRow, "cassistunit");
		bcp.getBillModel().setValueAt(name, iRow, "cassistunitname");
		sCassId = (String) bcp.getBillModel().getValueAt(iRow, "cassistunit");
		nExchangeRate = PuTool.getInvConvRateValue(sBaseID, sCassId);
		bcp.getBillModel().setValueAt(nExchangeRate, iRow, "nexchangerate");

		// �����ʸı䣬���¼���
		BillEditEvent tempE = new BillEditEvent(bcp
				.getBodyItem("nexchangerate"), bcp.getBodyValueAt(iRow,
				"nexchangerate"), "nexchangerate", iRow);
		afterEditWhenBodyRate(uiPanel, bcp, tempE);

		// �˴��ſ����ڿɱ༭���ڱ༭ǰ���������
		bcp.setCellEditable(iRow, "npraynum", true);
		bcp.setCellEditable(iRow, "nmoney", true);
		bcp.setCellEditable(iRow, "nexchangerate", true);
		bcp.setCellEditable(iRow, "nassistnum", true);
		bcp.setCellEditable(iRow, "cassistunitname", true);
	}

	/**
	 * �༭���¼�--����
	 * 
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyNum(ToftPanel ui, BillCardPanel bcp,
			BillEditEvent e) {
		// ��Ҫ����ı�����
		UFDouble nSuggestPrice = PuPubVO.getUFDouble_NullAsZero(bcp
				.getBodyValueAt(e.getRow(), "nsuggestprice"));
		UFDouble nPrayNum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e
				.getRow(), "npraynum"));
		// �����������ID
		String sBaseID = (String) bcp.getBillModel().getValueAt(e.getRow(),
				"cbaseid");
		// ����������
		String sCassId = (String) bcp.getBillModel().getValueAt(e.getRow(),
				"cassistunit");
		// ��ȡ������
		UFDouble nExchangeRate = PuTool.getInvConvRateValue(sBaseID, sCassId);
		if (nExchangeRate != null)
			nExchangeRate = nExchangeRate
					.setScale(bcp.getBodyItem("nexchangerate")
							.getDecimalDigits(), UFDouble.ROUND_HALF_UP);
		// �Ƿ�̶�������
		boolean bFixedFlag = PuTool.isFixedConvertRate(sBaseID, sCassId);
		// �޸ı������������Ӧ�仯
		int iRow = e.getRow();
		if (PuTool.isAssUnitManaged(sBaseID)) {
			Object cassistunit = bcp.getBillModel().getValueAt(iRow,
					"cassistunit");
			if ((cassistunit == null)) {
				bcp.getBillModel().setValueAt(null, iRow, "nassistnum");
				calculateAdvDays(ui, bcp, e);
				return;
			}
		}
		// �빺�����仯������Զ��仯
		if (nPrayNum != null) {
			if (nPrayNum.doubleValue() < 0) {
				MessageDialog.showErrorDlg(ui, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000040")/* "��������" */,
						m_lanResTool.getStrByID("40040101",
								"UPP40040101-000041")/* "�빺��������Ϊ����" */);
				bcp.getBillModel().setValueAt(null, iRow, e.getKey());
				return;
			}
			if (nSuggestPrice != null) {
				bcp.setBodyValueAt(nPrayNum.multiply(nSuggestPrice),
						e.getRow(), "nmoney");
			}
			// �̶�������,���������빺�����仯;����,���������빺�����仯
			if (bFixedFlag) {
				if (nExchangeRate != null && nExchangeRate.doubleValue() != 0.0) {
					bcp.setBodyValueAt(nPrayNum.div(nExchangeRate), e.getRow(),
							"nassistnum");
				}
			} else {
				// �ǹ̶�������
				UFDouble nAssistNum = null;
				Object oTemp = bcp.getBodyValueAt(e.getRow(), "nassistnum");
				if (oTemp != null && oTemp.toString().length() > 0)
					nAssistNum = (UFDouble) oTemp;
				if (nAssistNum != null) {
					if (nAssistNum.doubleValue() != 0.0) {
						bcp.setBodyValueAt(nPrayNum.div(nAssistNum),
								e.getRow(), "nexchangerate");
					} else {
						// ������Ϊ0,����޸Ļ�����,�����������/������!=0,��������Ϊ0��ì��
						// Ϊ��,�޸ĸ�����,���ı任����
						if (nExchangeRate != null
								&& nExchangeRate.doubleValue() != 0.0) {
							bcp.setBodyValueAt(nPrayNum.div(nExchangeRate), e
									.getRow(), "nassistnum");
						}
					}
				} else {
					Object objTmp = bcp.getBodyValueAt(e.getRow(),
							"nexchangerate");
					if (objTmp != null && !objTmp.toString().trim().equals("")) {
						nExchangeRate = new UFDouble(objTmp.toString());
						bcp.setBodyValueAt(nPrayNum.div(nExchangeRate), e
								.getRow(), "nassistnum");
					} else {
						bcp.setBodyValueAt(null, e.getRow(), "nassistnum");
						bcp.setBodyValueAt(null, e.getRow(), "nexchangerate");
					}
				}
			}
		} else {
			bcp.setBodyValueAt(null, e.getRow(), "nmoney");
			bcp.setBodyValueAt(null, e.getRow(), "ddemanddate");
			bcp.setBodyValueAt(null, e.getRow(), "dsuggestdate");
			if (bFixedFlag)
				bcp.setBodyValueAt(null, e.getRow(), "nassistnum");
			else
				bcp.setBodyValueAt(null, e.getRow(), "nexchangerate");
		}
		calculateAdvDays(ui, bcp, e);
	}

	/**
	 * 
	 */
	private void afterEditWhenBodyPrayNum(ToftPanel ui, BillCardPanel bcp,
			BillEditEvent event) {
		// �õ�������
		UFDouble nPrayNum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(
				event.getRow(), "npraynum"));
		if (m_VOs == null) {
			bcp.setBodyValueAt(nPrayNum, event.getRow(), "npraynum0");
		} else {
			boolean bCardShowing = getBillCardPanel().isVisible();
			if (bCardShowing) {
				int iSelected = m_nPresentRecord;
				if (m_VOs[iSelected].getHeadVO().getIbillstatus() == 0
						|| isFrmCopy) {
					bcp.setBodyValueAt(nPrayNum, event.getRow(), "npraynum0");
				}
			}
		}
	}

	/**
	 * �����빺�����Ͳɹ���ǰ�ڣ��Զ��޸��������ںͽ��鶩������
	 * 
	 * <p>
	 * since v56, �ع�����һ��ʵ��
	 * <p>
	 * ��������=�빺����+��ǰ��
	 * <p>
	 * ���鶩������=�빺����-��ǰ��
	 */
	private static void calculateAdvDays(ToftPanel ui, BillCardPanel bcp,
			BillEditEvent e) {
		// �빺����Ϊ�գ��򲻼��㣬ֱ�ӷ���
		UFDate dpraydate = PuPubVO.getUFDate(bcp.getHeadItem("dpraydate")
				.getValueObject());
		if (dpraydate == null) {
			return;
		}
		// ������������Ѿ�����,���������ڲ��޸�.
		int nDays = getAdvanceDays(ui, bcp, e);
		//
		UFDate ddemanddate = null;
		UFDate dsuggestdate = null;
		if (nDays >= 0) {
			ddemanddate = dpraydate.getDateAfter(nDays);
			if (!AdDayVO.isDateOverflow(ddemanddate)) {
				bcp.setBodyValueAt(ddemanddate, e.getRow(), "ddemanddate");
			}
			dsuggestdate = ddemanddate.getDateBefore(nDays);
			if (!AdDayVO.isDateOverflow(dsuggestdate)) {
				bcp.setBodyValueAt(dsuggestdate, e.getRow(), "dsuggestdate");
			}
		}
	}

	/**
	 * �༭���¼�-������Ŀ
	 * 
	 * @param bcp
	 * @param e
	 */
	public static void afterEditWhenBodyProj(BillCardPanel bcp, BillEditEvent e) {

		int n = e.getRow();
		Object oTemp = bcp.getBodyValueAt(n, "cprojectname");
		if (oTemp == null || oTemp.toString().length() == 0) {
			bcp.getBillModel().setCellEditable(n, "cprojectphasename", false);
			bcp.setBodyValueAt(null, n, "cprojectphasename");
			bcp.setBodyValueAt(null, n, "cprojectphaseid");
		} else {
			bcp.getBillModel().setCellEditable(
					n,
					"cprojectphasename",
					bcp.getBillModel().getItemByKey("cprojectphasename")
							.isEdit());
			oTemp = bcp.getBodyValueAt(n, "cprojectid");
			if (oTemp != null && oTemp.toString().length() > 0) {
				UIRefPane nRefPanel = (UIRefPane) bcp.getBodyItem(
						"cprojectphasename").getComponent();
				nRefPanel.setIsCustomDefined(true);
				nRefPanel.setRefModel(new ProjectPhase((String) oTemp));
				bcp.setBodyValueAt(null, n, "cprojectphasename");
				bcp.setBodyValueAt(null, n, "cprojectphaseid");
			}
		}

	}

	/**
	 * �༭���¼�--������
	 * 
	 * @param e
	 */
	// public static void afterEditWhenBodyFree(BillCardPanel bcp, BillEditEvent
	// e) {
	//
	// FreeVO freeVO = ((FreeItemRefPane) bcp.getBodyItem("vfree")
	// .getComponent()).getFreeVO();
	// String sVfree = (String) bcp.getBodyValueAt(e.getRow(), "vfree");
	// if (sVfree == null || sVfree.trim().length() <= 0) {
	// String str = null;
	// for (int i = 0; i < 5; i++) {
	// str = "vfree" + new Integer(i + 1).toString();
	// bcp.setBodyValueAt(null, e.getRow(), str);
	// }
	// } else {
	// String strName = null;
	// String str = null;
	// Object ob = null;
	// for (int i = 0; i < 5; i++) {
	// strName = "vfreename" + new Integer(i + 1).toString();
	//
	// if (freeVO.getAttributeValue(strName) != null) {
	// str = "vfree" + new Integer(i + 1).toString();
	// ob = freeVO.getAttributeValue(str);
	// bcp.setBodyValueAt(ob, e.getRow(), str);
	// }
	// }
	// }
	// // ���⵱ǰֵ��������һ�����������
	// InvVO invVO = new InvVO();
	// ((FreeItemRefPane) bcp.getBodyItem("vfree").getComponent())
	// .setFreeItemParam(invVO);
	// }
	/**
	 * ��ͷ�༭���¼�--�빺��
	 * 
	 * @param e
	 */
	public static void afterEditWhenHeadPsn(BillCardPanel bcp,
			String sDeptIdOld, BillEditEvent e) {
		String sPsnID = bcp.getHeadItem("cpraypsn").getValue();
		if (sPsnID != null && sPsnID.length() > 0) {
			// ��ò���ID����������
			UIRefPane nRefPanel = (UIRefPane) bcp.getHeadItem("cpraypsn")
					.getComponent();
			sDeptIdOld = (String) nRefPanel.getRefValue("bd_psndoc.pk_deptdoc");
			nRefPanel = (UIRefPane) bcp.getHeadItem("cdeptid").getComponent();
			nRefPanel.setPK(sDeptIdOld);
		}
	}

	/**
	 * ��ͷ�༭���¼�
	 * 
	 * @param e
	 */
	public static void afterEditWhenHeadDept(BillCardPanel bcp,
			String sDeptIdOld, String sLoginCorpId, BillEditEvent e) {

		// String sDeptID = bcp.getHeadItem("cdeptid").getValue();
		// UIRefPane nRefPanel = (UIRefPane)
		// bcp.getHeadItem("cpraypsn").getComponent();
		// String sWhere = " bd_psndoc.pk_deptdoc = '" + sDeptID + "'";
		//
		// if (sDeptID != null && sDeptID.length() > 0) {
		// nRefPanel.setWhereString(sWhere);
		//
		// if (!sDeptID.equals(sDeptIdOld)) {
		// // ����빺�˲������빺����,����빺��
		// UIRefPane nRefPanel0 = (UIRefPane)
		// bcp.getHeadItem("cpraypsn").getComponent();
		// nRefPanel0.setValue(null);
		// nRefPanel0.setPK(null);
		// }
		// }
		// else {
		// nRefPanel.setWhereString("bd_deptdoc.pk_corp = '" + sLoginCorpId
		// + "' and bd_deptdoc.pk_deptdoc = bd_psndoc.pk_deptdoc");
		// }
	}

	/**
	 * �빺���ͱ���ǰ���
	 */
	private boolean checkPraytype(PraybillItemVO bodyVO[]) {
		// ���������������㡰����˾=�빺��������˾=�ɹ���˾��ʱ���빺���Ͳ���ѡ��ǣ��ɹ������_�����ϣ��е�����һ��
		boolean b = true;
		if (m_comPrayType.getSelectedIndex() == 0
				|| m_comPrayType.getSelectedIndex() == 3) {
			for (int i = 0; i < bodyVO.length; i++) {
				if (!bodyVO[i].getPk_purcorp().equals(m_sLoginCorpId)) {
					b = false;
					break;
				}
				if (!bodyVO[i].getPk_reqcorp().equals(m_sLoginCorpId)) {
					b = false;
					break;
				}
			}
		}

		if (!b) {
			MessageDialog
					.showWarningDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000270")/* @res"��ʾ" */, m_lanResTool
							.getStrByID("40040101", "UPP40040101-000528", null,
									null)/* @res"ֻ�б�������������:����˾=�빺��������˾=�ɹ���˾ʱ,��������ί�ⶩ����" */);
		}
		return b;
	}

	/**
	 * ��������:��Ŀ�༭���¼����������Ŀ
	 */
	public static void afterEditHeadCproject(BillCardPanel bcp, BillEditEvent e) {
		UIRefPane ref = (UIRefPane) bcp.getHeadItem("cprojectidhead")
				.getComponent();
		String sPkCalBody = ref.getRefPK();

		final int size = bcp.getRowCount();
		for (int i = 0; i < size; i++) {
			bcp.getBillModel().setValueAt(sPkCalBody, i, "cprojectid");
			bcp.getBillModel().setValueAt(ref.getRefName(), i, "cprojectname");
			bcp.getBillModel().setRowState(i, BillModel.MODIFICATION);
		}
	}

	/**
	 * ˢ�±�ͷ��Ŀ�ֶΣ����ֶ�ֻ������ʾ����̨���洢 linsf 2009-10-28
	 * 
	 */
	private void freshHeadCproject(BillCardPanel bcp) {
		if (bcp.getHeadItem("cprojectidhead").getValue() == null
				|| bcp.getHeadItem("cprojectidhead").getValue().trim().length() == 0) {
			String pkProject = (String) bcp.getBillModel().getValueAt(0,
					"cprojectid");
			String nameProject = (String) bcp.getBillModel().getValueAt(0,
					"cprojectname");

			bcp.getHeadItem("cprojectidhead").setValue(nameProject);
			// bcp.getHeadItem("cprojectheadname").setValue(nameProject);
			UIRefPane ref = (UIRefPane) bcp.getHeadItem("cprojectidhead")
					.getComponent();
			ref.setPK(pkProject);
		}
	}

	/**
	 * �ѱ�����Ŀֵ������ͷ
	 * 
	 * @param billVOS
	 */
	private void freshHeadCproject(PraybillVO[] billVOS) {
		PraybillHeaderVO header = null;
		for (PraybillVO vo : billVOS) {
			header = null;
			header = vo.getHeadVO();
			if (header.getAttributeValue("cprojectidhead") == null
					&& vo.getChildrenVO()[0].getAttributeValue("cprojectid") != null) {
				header.setAttributeValue("cprojectidhead",
						vo.getChildrenVO()[0].getAttributeValue("cprojectid"));
			}
		}
	}

	// ���鹩Ӧ�̸ı�, ����ɹ�Ա������, ȡ��Ӧ��ר��ҵ��Ա
	public static void afterEditWhenBodyVendor(BillCardPanel bcp,
			BillEditEvent event) {
		try {
			int nCurRow = event.getRow();
			Object oTemp = bcp.getBodyValueAt(nCurRow, "cvendormangid");
			if (oTemp != null) {
				Object oTemp1 = bcp.getBodyValueAt(nCurRow, "cemployeename");
				if (oTemp1 == null) {
					oTemp1 = CacheTool.getCellValue("bd_cumandoc",
							"pk_cumandoc", "pk_resppsn1", oTemp.toString());
					if (oTemp1 != null) {
						Object o[] = (Object[]) oTemp1;
						if (o[0] != null) {
							bcp.setBodyValueAt(o[0], nCurRow, "cemployeeid");
							oTemp1 = CacheTool.getCellValue("bd_psndoc",
									"pk_psndoc", "psnname", o[0].toString());
							o = (Object[]) oTemp1;
							bcp.setBodyValueAt(o[0], nCurRow, "cemployeename");
						}
					}
				}
			}
		} catch (Exception e) {
			SCMEnv.out(e);
		}
	}

	/**
	 * �ɹ�Ա �༭��
	 */
	public static void afterEditWhenBodyEmployee(ToftPanel uiPanel,
			BillCardPanel bcp, String sLoginCorpId, BillEditEvent event) {

		String sPsnId = PuPubVO.getString_TrimZeroLenAsNull(bcp.getBodyValueAt(
				event.getRow(), "cemployeeid"));
		if (sPsnId == null) {
			return;
		}
		// ����ҵ��ԱĬ�ϲɹ���֯
		String strPurId = PuTool.getPurIdByPsnId(sPsnId);
		if (strPurId != null) {
			if (!bIsCentralPur) {
				bcp
						.setBodyValueAt(strPurId, event.getRow(),
								"cpurorganization");
			}
			m_strPurCorpIdOld = PuPubVO.getString_TrimZeroLenAsNull(bcp
					.getBodyValueAt(event.getRow(), "pk_purcorp"));
			if (bcp.getBodyItem("cpurorganizationname").getEditFormulas() != null) {
				bcp.getBillModel().execFormulas(
						event.getRow(),
						bcp.getBodyItem("cpurorganizationname")
								.getEditFormulas());
			} else {
				bcp
						.getBillModel()
						.execFormulas(
								event.getRow(),
								new String[] {
										"cpurorganization->getColValue(bd_purorg,pk_purorg,pk_purorg,cpurorganization)",
										"cpurorganizationname->getColValue(bd_purorg,name,pk_purorg,cpurorganization)",
										"pk_purcorp->getColValue(bd_purorg,ownercorp,pk_purorg,cpurorganization)",
										"purcorpname->getColValue(bd_corp,unitname,pk_corp,pk_purcorp)" });
			}
			// bcp.setBodyValueAt(ClientEnvironment.getInstance().getCorporation()
			// .getUnitname(), event.getRow()+1, "purcorpname");
			BillEditEvent eNew = new BillEditEvent(bcp
					.getBodyItem("cpurorganizationname"), strPurId,
					"cpurorganizationname", event.getRow(), BillItem.BODY);
			afterEditWhenBodyPurOrg(uiPanel, bcp, sLoginCorpId, eNew);
		}
	}

	/**
	 * �ɹ���֯ �༭��
	 */
	public static void afterEditWhenBodyPurOrg(ToftPanel uiPanel,
			BillCardPanel bcp, String sLoginCorpId, BillEditEvent event) {
		int currow = event.getRow();

		BillModel bm = bcp.getBillModel();
		String oldPurCorp = PuPubVO.getString_TrimZeroLenAsNull(bm.getValueAt(
				currow, "pk_purcorp"));

		String strPurOrgId = PuPubVO.getString_TrimZeroLenAsNull(bm.getValueAt(
				currow, "cpurorganization"));
		// ����ɹ���֯����գ���ȡ��¼��˾Ϊ�ɹ���˾
		if (strPurOrgId == null) {
			bcp.setBodyValueAt(sLoginCorpId, currow, "pk_purcorp");
			bcp.setBodyValueAt(ClientEnvironment.getInstance().getCorporation()
					.getUnitname(), currow, "purcorpname");
		}
		String strPurCorpCurr = (String) bm.getValueAt(event.getRow(),
				"pk_purcorp");
		// �ɹ���˾�仯����ս��鹩Ӧ�̣��ɹ�Ա
		if (!strPurCorpCurr.equals(m_strPurCorpIdOld)) {
			bm.setValueAt(null, currow, "cvendormangid");
			bm.setValueAt(null, currow, "cvendorbaseid");
			bm.setValueAt(null, currow, "cvendorname");
			bm.setValueAt(null, currow, "cemployeeid");
			bm.setValueAt(null, currow, "cemployeename");
		}
		// �빺��˾
		String strCurrCorpId = PuPubVO.getString_TrimZeroLenAsNull(bm
				.getValueAt(event.getRow(), "pk_corp"));
		if (strCurrCorpId == null) {
			strCurrCorpId = ClientEnvironment.getInstance().getCorporation()
					.getPrimaryKey();
		}
		String newPurCorp = PuPubVO.getString_TrimZeroLenAsNull(bm.getValueAt(
				currow, "pk_purcorp"));
		// ����ɹ���˾�б仯��������ѯ��
		if (newPurCorp != null && oldPurCorp != null
				&& !oldPurCorp.equals(newPurCorp)) {
			// ѯ��
			setRulePrice(uiPanel, bcp, new String[] { (String) bm.getValueAt(
					event.getRow(), "pk_purcorp") }, new String[] { (String) bm
					.getValueAt(event.getRow(), "cbaseid") }, PuPubVO
					.getString_TrimZeroLenAsNull(bm.getValueAt(event.getRow(),
							"pk_reqstoorg")), strCurrCorpId, event.getRow(),
					event.getRow() + 1);
		}
		try {
			setBiztype(bcp, currow, currow + 1);
		} catch (BusinessException e) {
			uiPanel.showErrorMessage("����ҵ������ʧ��.");
		}
	}

	/**
	 * ������룺���յ�ǰ��¼��˾������������ǿգ��ɱ༭��Ĭ����ʾ a) �༭�� i. �������գ������Ŀ�԰���V31SP1���� ii.
	 * ����ı�ֵ������ɹ���֯��ֵ�������ɹ���֯Ĭ��ֵ�� 1.
	 * ������Բɣ�ȡ��¼��˾����ǰ����ɹ��Ĳɹ�Ա�����ݲɹ�Ա�����ϵ������Ӧ�Ĳɹ���֯�����ݲɹ���֯����Ĳɹ�Ա��,����ȡһ�� 2.
	 * ����Ǽ��ɣ�ȡ���ɹ����ж���Ĳɹ���֯������˾���������ID�����ɹ���֯�� iii. ����ı�ֵ������ɹ�Ա��ֵ�������ɹ�ԱĬ��ֵ�� 1.
	 * ���������Բɣ���ȡ�ɹ���˾����ǰ����ɹ��Ĳɹ�Ա�����ݲɹ�Ա�����ϵ�� 2.
	 * �������Ǽ��ɣ�ȡ���ɹ����ж���Ĳɹ���֯������˾���������ID�����ɹ���֯�������ҵ��Ա������ȡһ�� iv.
	 * ����ı�ֵ��������鹩Ӧ����ֵ���������鹩Ӧ��Ĭ��ֵ�� 1.
	 * �����������������䵽�ɹ���֯������˾��ȡ���������ID+�ɹ���֯������˾����Ӧ������Ӧ�� !!!!!!!
	 * ע�⣺����������Ч�����⣬��Ҫ��һ��Ч���Ż�
	 */
	public static void afterEditWhenBodyInventory(ToftPanel uiPanel,
			BillCardPanel bcp, String strReqCorpId, String strCorpName,
			BillEditEvent event) {

		BillModel bm = bcp.getBillModel();
		// �رպϼƿ���
		boolean bOldNeedCalc = bm.isNeedCalculate();
		bm.setNeedCalculate(false);

		UIRefPane refpane = (UIRefPane) bcp.getBodyItem("cinventorycode")
				.getComponent();
		Object[] oaMangId = ((Object[]) refpane
				.getRefValues("bd_invmandoc.pk_invmandoc"));
		Object[] oaBaseId = ((Object[]) refpane
				.getRefValues("bd_invmandoc.pk_invbasdoc"));

		Object[] saCode = ((Object[]) refpane
				.getRefValues("bd_invbasdoc.invcode"));
		Object[] saName = ((Object[]) refpane
				.getRefValues("bd_invbasdoc.invname"));
		Object[] saSpec = ((Object[]) refpane
				.getRefValues("bd_invbasdoc.invspec"));
		Object[] saType = ((Object[]) refpane
				.getRefValues("bd_invbasdoc.invtype"));
		Object[] saMeasUnitRef = ((Object[]) refpane
				.getRefValues("bd_invbasdoc.pk_measdoc"));

		String pk_reqstoorg = (String) bm.getValueAt(event.getRow(),
				"pk_reqstoorg");
		String pk_reqcorp = (String) bm
				.getValueAt(event.getRow(), "pk_reqcorp");

		if (oaMangId == null || oaBaseId == null
				|| oaBaseId.length != oaMangId.length) {
			int selectedRow = event.getRow();

			bm.setValueAt(null, selectedRow, "cinventorycode");
			bm.setValueAt(null, selectedRow, "cinventoryname");
			bm.setValueAt(null, selectedRow, "cbaseid");
			bm.setValueAt(null, selectedRow, "cmangid");
			bm.setValueAt(null, selectedRow, "cinventoryspec");
			bm.setValueAt(null, selectedRow, "cinventorytype");
			bm.setValueAt(null, selectedRow, "cassistunitname");
			bm.setValueAt(null, selectedRow, "cinventoryunit");
			bm.setValueAt(null, selectedRow, "cproductname");

			bm.setValueAt(null, selectedRow, "nassistnum");
			bm.setValueAt(null, selectedRow, "nexchangerate");
			// �빺��ά�����޶����޸Ĵ��ʱ���������
			// bm.setValueAt(null, selectedRow, "npraynum");
			bm.setValueAt(null, selectedRow, "nsuggestprice");
			bm.setValueAt(null, selectedRow, "nmoney");
			bm.setValueAt(null, selectedRow, "ddemanddate");
			bm.setValueAt(null, selectedRow, "dsuggestdate");
			bm.setValueAt(null, selectedRow, "vfree");
			bm.setValueAt(null, selectedRow, "vfree1");
			bm.setValueAt(null, selectedRow, "vfree2");
			bm.setValueAt(null, selectedRow, "vfree3");
			bm.setValueAt(null, selectedRow, "vfree4");
			bm.setValueAt(null, selectedRow, "vfree5");
			bm.setValueAt(null, selectedRow, "cvendormangid");
			bm.setValueAt(null, selectedRow, "cvendorbaseid");
			bm.setValueAt(null, selectedRow, "cvendorname");

			return;
		}
		int iLen = ((oaMangId == null) ? 0 : oaMangId.length);
		String[] saMangId = new String[iLen];
		String[] saBaseId = new String[iLen];
		Object[] saMeasUnit = new Object[iLen];
		for (int i = 0; i < iLen; i++) {
			saMangId[i] = oaMangId[i] + "";
			saBaseId[i] = oaBaseId[i] + "";
			saMeasUnit[i] = saMeasUnitRef[i] + "";
		}
		int nRow = bcp.getRowCount();

		// ѡ�е����������һ�������з������
		int iBeginRow = event.getRow();
		int iEndRow = iBeginRow + iLen;
		int selectedCount = iBeginRow;
		if (iBeginRow == nRow - 1) {
			// ����
			for (int i = 0; i < iLen; i++) {
				bcp.getBillModel().addLine();
				// ���в������Զ����뵱ǰ��¼��˾
				bcp.setBodyValueAt(strReqCorpId, bcp.getRowCount() - 1,
						"pk_reqcorp");
				bcp.setBodyValueAt(strCorpName, bcp.getRowCount() - 1,
						"reqcorpname");
				bcp.setBodyValueAt(strReqCorpId, bcp.getRowCount() - 1,
						"pk_purcorp");
				bcp.setBodyValueAt(strCorpName, bcp.getRowCount() - 1,
						"purcorpname");
			}
			/* ������Ŀ�Զ�Э�� */
			PuTool.setBodyProjectByHeadProject(bcp, "cprojectidhead",
					"cprojectid", "cprojectname", iBeginRow, iEndRow);
			/* �����к� */
			BillRowNo.addLineRowNos(bcp, BillTypeConst.PO_PRAY, "crowno", iLen);
		} else {// ������
			if (selectedCount < 0) {
				uiPanel.showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000354")/* @res"����ǰ����ѡ������У�" */);
				return;
			}

			for (int i = iBeginRow + 1; i < iBeginRow + iLen; i++) {
				bcp.getBillModel().insertRow(i);
			}
			for (int i = iBeginRow; i < iBeginRow + iLen; i++) {
				// ���в������Զ����뵱ǰ��¼��˾
				bcp.setBodyValueAt(strReqCorpId, i, "pk_reqcorp");
				bcp.setBodyValueAt(strCorpName, i, "reqcorpname");
				bcp.setBodyValueAt(strReqCorpId, i, "pk_purcorp");
				bcp.setBodyValueAt(strCorpName, i, "purcorpname");
				// ����ղɹ���֯������������
				bcp.setBodyValueAt(null, i, "cpurorganization");
				bcp.setBodyValueAt(null, i, "cpurorganizationname");
			}
			int iFinalEndRow = iBeginRow + iLen;

			// �����к�
			BillRowNo.insertLineRowNos(bcp, BillTypeConst.PO_PRAY, "crowno",
					iFinalEndRow, iLen - 1);

		}
		// ִ�б��幫ʽ��������
		setInvEditFormulaInfo(bcp, refpane, iBeginRow, iEndRow, strReqCorpId,
				strCorpName, saCode, saName, saSpec, saType, saMangId,
				saBaseId, saMeasUnit);

		// �����������Զ������
		// setEnabled_BodyFree(bcp, refpane, iBeginRow, iEndRow);

		// ArrayList listValAll = null;
		// ArrayList listValVendor = null;
		String[] saPurCorpId = new String[saMangId.length];
		for (int i = iBeginRow; i < iEndRow; i++) {
			// �ı���ʱ,���������,������,����,����,���,��������,���鶩������
			bm.setValueAt(null, i, "nassistnum");
			bm.setValueAt(null, i, "nexchangerate");
			// bm.setValueAt(null, i, "npraynum");
			bm.setValueAt(null, i, "nsuggestprice");
			bm.setValueAt(null, i, "nmoney");
			bm.setValueAt(null, i, "ddemanddate");
			bm.setValueAt(null, i, "dsuggestdate");
			bm.setValueAt(null, i, "vfree");
			bm.setValueAt(null, i, "vfree1");
			bm.setValueAt(null, i, "vfree2");
			bm.setValueAt(null, i, "vfree3");
			bm.setValueAt(null, i, "vfree4");
			bm.setValueAt(null, i, "vfree5");
			// ��˾����
			bcp.setBodyValueAt(strReqCorpId, i, "pk_corp");
			// ������Ŀ�Զ�Э��
			// PuTool.setBodyProjectByHeadPro(bcp, "cprojectidhead",
			// "cprojectid", "cprojectname", i);
			saPurCorpId[i - iBeginRow] = (String) bcp.getBodyValueAt(i,
					"pk_purcorp");
			// ���κ����
			bm.setValueAt(null, i, "vproducenum");

			// ��������˾
			bm.setValueAt(pk_reqcorp, i, "pk_reqcorp");
			// ������������֯
			bm.setValueAt(pk_reqstoorg, i, "pk_reqstoorg");
			//
		}

		Vector<String> formulas = new Vector<String>();
		// ����ִ�д���༭��ʽ
		BillItem it = bcp.getBodyItem(event.getKey());
		if (it.getEditFormulas() != null && it.getEditFormulas().length > 0) {
			// bcp.getBillModel().execFormulas(it.getEditFormulas(), iBeginRow,
			// iEndRow);
			for (String formula : it.getEditFormulas())
				formulas.add(formula);
		}
		// ִ�б���༭��ʽ
		BillItem itemEmployee = bm.getItemByKey("cemployeename");
		if (itemEmployee.getEditFormulas() != null
				&& itemEmployee.getEditFormulas().length > 0) {
			// bcp.getBillModel().execFormulas(itemEmployee.getEditFormulas(),
			// iBeginRow, iEndRow);
			for (String formula : itemEmployee.getEditFormulas())
				formulas.add(formula);
		}
		BillItem itemRepstoorgname = bm.getItemByKey("reqstoorgname");
		if (itemRepstoorgname.getEditFormulas() != null
				&& itemRepstoorgname.getEditFormulas().length > 0) {
			// bcp.getBillModel().execFormulas(itemRepstoorgname.getEditFormulas(),
			// iBeginRow, iEndRow);
			for (String formula : itemRepstoorgname.getEditFormulas())
				formulas.add(formula);
		}
		BillItem itemPurCorp = bm.getItemByKey("purcorpname");
		if (itemPurCorp.getEditFormulas() != null
				&& itemPurCorp.getEditFormulas().length > 0) {
			// bcp.getBillModel().execFormulas(itemPurCorp.getEditFormulas(),
			// iBeginRow, iEndRow);
			for (String formula : itemPurCorp.getEditFormulas())
				formulas.add(formula);
		}
		BillItem itemReqCorp = bm.getItemByKey("reqcorpname");
		if (itemReqCorp.getEditFormulas() != null
				&& itemReqCorp.getEditFormulas().length > 0) {
			// bcp.getBillModel().execFormulas(itemReqCorp.getEditFormulas(),
			// iBeginRow, iEndRow);
			for (String formula : itemReqCorp.getEditFormulas())
				formulas.add(formula);
		}
		formulas
				.add("cpurorganizationname->getColValue(bd_purorg,name,pk_purorg,cpurorganization)");

		bcp.getBillModel().execFormulas(
				formulas.toArray(new String[formulas.size()]), iBeginRow,
				iEndRow);
		// �����빺��
		setRulePrice(uiPanel, bcp, saPurCorpId, saBaseId, pk_reqstoorg,
				strReqCorpId, iBeginRow, iEndRow);
		// �Ƿ񸨼���������������
		PuTool.loadBatchAssistManaged(saBaseId);
		// �Ƿ����κŹ�����������
		PuTool.loadBatchProdNumMngt(saMangId);
		// ������ʾ czp 20050303 ����

		/*
		 * PraybillVO voCurr = (PraybillVO)
		 * bcp.getBillValueVO(PraybillVO.class.getName(),
		 * PraybillHeaderVO.class.getName(), PraybillItemVO.class.getName());
		 */
		nRow = bcp.getRowCount();
		PraybillVO voCurr = new PraybillVO(nRow);
		bcp.getBillValueVO(voCurr);

		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(bcp, voCurr);
		// �򿪺ϼƿ���
		bm.setNeedCalculate(bOldNeedCalc);
		// refpane.setPK(null);
		// �˴��ſ����ڿɱ༭���ڱ༭ǰ���������

		for (int iRow = iBeginRow; iRow < iEndRow; iRow++) {
			bcp.setCellEditable(iRow, "npraynum", true);
			bcp.setCellEditable(iRow, "nmoney", true);
			bcp.setCellEditable(iRow, "nexchangerate", true);
			bcp.setCellEditable(iRow, "nassistnum", true);
			bcp.setCellEditable(iRow, "cassistunitname", true);
		}
		// ���ø������������������Ϣ
		setRelated_AssistUnit(bcp, uiPanel, iBeginRow, iEndRow);

		try {
			saPurCorpId = setCpurorganization(bcp, iBeginRow, iEndRow);
			// ����ҵ������
			setBiztype(bcp, iBeginRow, iEndRow);
		} catch (BusinessException e) {
			// ��־�쳣
			nc.vo.scm.pub.SCMEnv.out(e);
		}
		HashMap hVendorNo = null;
		/*
		 * <p>Arraylist(�������������IDΪ��) <p> |--�ɹ���֯ <p> |--�ɹ���˾ <p> |--�ɹ�Ա <p>
		 * |--���鹩Ӧ�̼������Ϣ(ArrayList)
		 */
		try {
			// ���Ĭ�Ϲ�Ӧ������
			hVendorNo = PraybillHelper.queryPurOrgAndVendor(null, saBaseId,
					null, saPurCorpId);
		} catch (Exception e) {
			// MessageDialog.showErrorDlg(uiPanel,
			// m_lanResTool.getStrByID("40040101", "UPP40040101-000442")/*
			// "Ĭ�ϲɹ���֯�͹�Ӧ��" */,
			// e.getMessage());
			// return;
		}
		if (hVendorNo != null) {
			for (int i = iBeginRow; i < iEndRow; i++) {
				if (bcp.getBodyValueAt(i, "cbaseid") != null) {
					String cbasid = (String) bcp.getBodyValueAt(i, "cbaseid");
					String sPk_purcorp = (String) bcp.getBodyValueAt(i,
							"pk_purcorp");
					if (!hVendorNo.containsKey(cbasid + sPk_purcorp)) {
						continue;
					}
					// ���ù�Ӧ��
					ArrayList listValVendor = (ArrayList) hVendorNo.get(cbasid
							+ sPk_purcorp);
					if (listValVendor != null && listValVendor.size() >= 3) {
						bm.setValueAt(listValVendor.get(0), i, "cvendormangid");
						bm.setValueAt(listValVendor.get(1), i, "cvendorbaseid");
						bm.setValueAt(listValVendor.get(2), i, "cvendorname");
					} else {
						bm.setValueAt(null, i, "cvendormangid");
						bm.setValueAt(null, i, "cvendorbaseid");
						bm.setValueAt(null, i, "cvendorname");
					}
				}
			}
		}
	}

	/**
	 * ���ߣ���ӡ�� ���ܣ�������������ڡ������޸ĺ���Ӧ������޼۱仯 �ú�����afterEdit������������ڡ����ָı����á� ������ int
	 * iBeginRow ������ͬ�����Ϣ�ı��忪ʼ�� int iEndRow ������ͬ�����Ϣ�ı�������� ���أ��� ���⣺��
	 * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2003-11-14 wyf ��������
	 */
	private static void setRelated_AssistUnit(BillCardPanel bcp,
			ToftPanel uiPanel, int iBeginRow, int iEndRow) {
		BillModel bm = bcp.getBillModel();
		// ��������
		String[] saBaseId = (String[]) PuGetUIValueTool.getArrayNotNull(bm,
				"cbaseid", String.class, iBeginRow, iEndRow);
		PuTool.loadBatchAssistManaged(saBaseId);

		// ����������
		Vector vecAssistUnitIndex = new Vector();
		Vector vecBaseId = new Vector();
		Vector vecAssistId = new Vector();

		// ����ֵ

		// ����Ĭ�ϸ�����
		String[] aryAssistunit = new String[] {
				"<formulaset><cachetype>FOREDBCACHE</cachetype></formulaset>cassistunit->getColValue(bd_invbasdoc,pk_measdoc2,pk_invbasdoc,cbaseid)",
				"cassistunitname->getColValue(bd_measdoc,measname,pk_measdoc,cassistunit)" };
		bm.execFormulas(aryAssistunit, iBeginRow, iEndRow);
		//
		String sBaseId = null;
		String sAssistUnit = null;
		for (int iRow = iBeginRow; iRow < iEndRow; iRow++) {
			sBaseId = (String) bcp.getBodyValueAt(iRow, "cbaseid");
			if (PuTool.isAssUnitManaged(sBaseId)) {
				sAssistUnit = (String) bcp.getBodyValueAt(iRow, "cassistunit");
				// Ϊ����������׼��
				if (PuPubVO.getString_TrimZeroLenAsNull(sAssistUnit) != null) {
					vecAssistUnitIndex.add(new Integer(iRow));
					vecBaseId.add(sBaseId);
					vecAssistId.add(sAssistUnit);
				}
			} else {
				bm.setValueAt(null, iRow, "cassistunitname");
				bm.setValueAt(null, iRow, "cassistunit");
				bm.setValueAt(null, iRow, "nassistnum");
				bm.setValueAt(null, iRow, "nexchangerate");
			}
		}

		// �������ø���������
		int iAssistUnitLen = vecAssistUnitIndex.size();
		if (iAssistUnitLen > 0) {

			// ��������
			PuTool.loadBatchInvConvRateInfo((String[]) vecBaseId
					.toArray(new String[iAssistUnitLen]),
					(String[]) vecAssistId.toArray(new String[iAssistUnitLen]));

			// String[] saCurrId =
			// getAllCurrIdFromCard(getRowCount(),getCorp(),getHeadItem("ccurrencytypeid").getValue(),bm);
			// HashMap mapRateMny =
			// m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(saCurrId);
			// BusinessCurrencyRateUtil bca =
			// m_cardPoPubSetUI2.getCurrArith_Busi(getCorp());

			// ѭ��ִ��
			int iRow = 0;
			for (int i = 0; i < iAssistUnitLen; i++) {
				iRow = ((Integer) vecAssistUnitIndex.get(i)).intValue();

				Object[] oConvRate = PuTool.getInvConvRateInfo(
						(String) vecBaseId.get(i), (String) vecAssistId.get(i));
				if (oConvRate == null) {
					bm.setValueAt(null, iRow, "nexchangerate");
				} else {
					bm.setValueAt((UFDouble) oConvRate[0], iRow,
							"nexchangerate");
				}

				// �����ʸı䣬���¼���
				BillEditEvent tempE = new BillEditEvent(bcp
						.getBodyItem("nexchangerate"), bcp.getBodyValueAt(iRow,
						"nexchangerate"), "nexchangerate", iRow);
				afterEditWhenBodyRate(uiPanel, bcp, tempE);
			}
		}

		// ���ÿɱ༭��
		// setEnabled_BodyAssistUnitRelated(iBeginRow,iEndRow) ;

	}

	/**
	 * ��������PO29�����ý��鵥��
	 * 
	 * @param uiPanel
	 * @param bcp
	 * @param saMangId
	 * @param saBaseId
	 * @param pk_reqstoorg
	 * @param strPk_corp
	 * @param iBeginRow
	 * @param iEndRow
	 * @since V50
	 */
	private static void setRulePrice(ToftPanel uiPanel, BillCardPanel bcp,
			String[] saPurCorpId, String[] saBaseId, String pk_reqstoorg,
			String strPk_corp, int iBeginRow, int iEndRow) {

		// ��ȡ���ݹ������ļ۸�
		UFDouble[] uaPrice = null;
		int iLen = iEndRow - iBeginRow;
		try {
			uaPrice = PraybillHelper.getRulePrice(saPurCorpId, saBaseId,
					pk_reqstoorg, strPk_corp);
		} catch (Exception e) {
			// MessageDialog.showErrorDlg(uiPanel,
			// m_lanResTool.getStrByID("40040101", "UPP40040101-000442")/*
			// "Ĭ�ϲɹ���֯�͹�Ӧ��" */,
			// e.getMessage());
			// return;
		}
		if (uaPrice == null || uaPrice.length != iLen) {
			return;
		}
		BillEditEvent e = null;

		for (int i = iBeginRow; i < iEndRow; i++) {
			// ԭ����ֵ������ԭ����ֵ
			if (uaPrice[i - iBeginRow] == null) {
				continue;
			}
			bcp.getBillModel().setValueAt(uaPrice[i - iBeginRow], i,
					"nsuggestprice");
		}
		afterEditWhenBodySuggPrice(uiPanel, bcp, iBeginRow, iEndRow);
	}

	/**
	 * �༭ǰ����:������ �������ڣ�(2001-3-23 2:02:27)
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public boolean beforeEdit(BillEditEvent e) {

		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000075")/*
															 * @res "���ڱ༭"
															 */);

		boolean editable = true;

		// ����û�ģ�嶨�����Ŀ���ɱ༭����ֱ�ӷ���false
		switch (e.getPos()) {
		case 0:
			if (!getBillCardPanel().getHeadItem(e.getKey()).isEdit()) {
				return false;
			}
			break;
		case 1:
			if (!getBillCardPanel().getBodyItem(e.getKey()).isEdit()) {
				return false;
			}
			break;
		case 2:
			if (!getBillCardPanel().getTailItem(e.getKey()).isEdit()) {
				return false;
			}
			break;
		default:
			return false;
		}

		// �û�ģ�嶨����޸�����µĴ���
		if (e.getKey().equals("vfree")) {
			return PuTool.beforeEditInvBillBodyFree(getBillCardPanel(), e,
					new String[] { "cmangid", "cinventorycode",
							"cinventoryname", "cinventoryspec",
							"cinventorytype" }, new String[] { "vfree",
							"vfree1", "vfree2", "vfree3", "vfree4", "vfree5" });
		}
		// �ɹ���֯
		else if (e.getKey().equals("cpurorganizationname")) {
			return beforeEditWhenBodyPurOrg(this, getBillCardPanel(),
					m_sLoginCorpId, e);
		}
		// �������
		else if (e.getKey().equals("cinventorycode")) {
			// beforeEditWhenBodyInventory(getBillCardPanel(), m_hashBizType,
			// m_hashInvbasIds, m_sLoginCorpId, PoPublicUIClass
			// .getLoginDate()
			// + "", e);
		}
		// �����Ʒ
		else if (e.getKey().equals("cproductname")) {
			beforeEditWhenBodyProduct(getBillCardPanel(), m_sLoginCorpId,
					m_sLoginDate, e);
		}
		// ����ֿ�
		else if (e.getKey().equals("cwarehousename")) {
			beforeEditWhenBodyReqWare(getBillCardPanel(), e);
		}
		// ��������֯
		else if (e.getKey().equals("reqstoorgname")) {
			beforeEditWhenBodyReqStore(getBillCardPanel(), e);
		}
		// ���鹩Ӧ��
		else if (e.getKey().equals("cvendorname")) {
			return beforeEditWhenBodyVendor(getBillCardPanel(), e);
		}
		// �ɹ�Ա
		else if (e.getKey().equals("cemployeename")) {
			beforeEditWhenBodyEmployer(getBillCardPanel(), e);
		}
		// ��Ŀ
		else if (e.getKey().equals("cprojectname")) {
			beforeEditWhenBodyProject(getBillCardPanel(), e);
		}
		// ��Ŀ�׶�
		else if (e.getKey().equals("cprojectphasename")) {
			beforeEditWhenBodyProjectPhase(getBillCardPanel(), e);
		}
		// ���κ�
		else if (e.getKey().equals("vproducenum")) {
			return beforeEditWhenBodyProduceNum(this, getBillCardPanel(),
					m_sLoginCorpId, e);
		}
		// ����˾
		else if (e.getKey().equals("reqcorpname")) {
			beforeEditWhenBodyReqcorp(getBillCardPanel(), e);
		}
		// �ɹ���˾
		else if (e.getKey().equals("purcorpname")) {
			beforeEditWhenBodyPurcorp(getBillCardPanel(), e);
		}
		// �������������ʡ���������������
		else if ("npraynum".equals(e.getKey()) || "nmoney".equals(e.getKey())
				|| "nexchangerate".equals(e.getKey())
				|| "nassistnum".equals(e.getKey())
				|| "cassistunitname".equals(e.getKey())) {
			beforeEditBodyAssistUnitNumber(getBillCardPanel(), e.getRow());
			if ("nexchangerate".equals(e.getKey())) {
				int col = getBillCardPanel().getBodyColByKey("nexchangerate");
				editable = getBillCardPanel().getBillModel().isCellEditable(
						e.getRow(), col);
			}
		} else if (e.getKey().startsWith("vdef")) {
			if (getBillCardPanel().getBodyItem(e.getKey()).getComponent() instanceof UIRefPane) {
				((UIRefPane) getBillCardPanel().getBodyItem(e.getKey())
						.getComponent()).getUITextField().setEditable(true);
			}
		} else if (e.getKey().equals("cbiztypename")) {
			beforeEditWhenBodyBiztype(getBillCardPanel(), e);
		}
		return editable;
	}

	/**
	 * <p>
	 * �ɹ���֯���༭�ɱ༭�Լ�������������
	 * <p>
	 * 1)��ҵ�������ǹ�˾ҵ�����ͣ��ɹ���֯��������ȡ�ɹ���֯������˾Ϊ�빺��������˾�Ĳɹ���֯�������ɱ༭
	 * <p>
	 * 2)��ҵ�������Ǽ���ҵ�����ͣ�
	 * <p>
	 * i.����˾δ¼�룬��������ȡȫ�������вɹ���֯�������ɱ༭
	 * <p>
	 * ii.����˾=�빺��������˾�����δ¼�룬���ɱ༭
	 * <p>
	 * iii.����˾=�빺��������˾���Բɹ������������ȡȫ�������вɹ���֯�������ɱ༭�����ɲ��ɱ༭(XY\WYF\CZP:�ݲ����ֹ�˾���ƻ���������������Ż����������)
	 * <p>
	 * iv.����˾���빺��������˾�����۴���Ƿ�¼�룬�ɹ���֯��������Ϊ���빺��������˾������˾�����ɱ༭
	 * 
	 * @��ϸ����������
	 * <p>
	 * ���������Բɣ���ȡ����˾����ǰ����ɹ��Ĳɹ�Ա�����ݲɹ�Ա�����ϵ������Ӧ�Ĳɹ���֯�����ݲɹ���֯����Ĳɹ�Ա�������Բ���ѡ��ɹ���֯�����޸ģ�
	 * <p>
	 * �������Ǽ��ɣ�ȡ���ɹ����ж���Ĳɹ���֯����ǰ��˾����ǲɹ���֯��������˾������Բ���ѡ��������˾��Ϊ��ǰ��˾������˾�Ĳɹ���֯�޸ģ��������޸ġ�
	 * <p>
	 * 6.1 ��Դ��ֱ�����۶������빺�� 6.1.1
	 * �����빺������ġ�����˾����������֯���������Ϣ��ƥ���Դ�嵥���Զ�����Ĭ��ֵ�������Դ�嵥ƥ�䵽�Ĳɹ���֯�����빺��������˾������Ϊ�˴����ǰ��˾��Ȩ�Բɣ���Ҳ��ί�б�Ĺ�˾�ɹ�����ʱ�ɹ���֯�ɲ��ռ��ŷ�Χ�ڵĲɹ���֯�޸ģ��޸ĺ������޸Ĳɹ���˾��Ŀ��
	 * 6.1.2 ���Դ�嵥ƥ�䵽�Ĳɹ���֯�����ڵ�ǰ��˾����ɹ���֯���ɸģ���Ϊ��ǰ��˾�޲ɹ�Ȩ��ֻ�ܼ��ɣ� 6.1.3
	 * �����Ϊ�գ���ȡ��������֯��Ӧ�������������ϵĲɹ���֯��Ϊ�빺������ɹ���֯Ĭ��ֵ�����ɲ��ռ��ŷ�Χ�Ĳɹ���֯�����޸ġ� 6.1.4
	 * ���û��ƥ�䵽��Ӧ�Ĳɹ���֯���ɲ��ռ��ŷ�Χ�ڵĲɹ���֯����ѡ�� 6.2
	 * Դ��ֱ�����۶������빺����Ҫ��֤���۹�˾�Ͳɹ���˾��ͬ���ɹ���֯�������ڵ�ǰ�빺��˾������ϵͳ�߼��޷����� 6.2.1
	 * ƥ���Դ�嵥��>������֯�������������ϵĲɹ���֯����ΪĬ��ֵ�����Բ��յ�ǰ��˾�Ĳɹ���֯�����޸ġ� 6.2.2
	 * ���ƥ�䵽�Ĳɹ���֯�����ڵ�ǰ��˾������Ϊû��ƥ�䵽��Ӧ�Ĳɹ���֯�����Բ��յ�ǰ��˾�Ĳɹ���֯����ѡ��
	 */
	public static boolean beforeEditWhenBodyPurOrg(ToftPanel uiPanel,
			BillCardPanel bcp, String strPk_corp, BillEditEvent e) {
		int currow = e.getRow();
		// String pk_reqcorp = null;
		UIRefPane refpane = null;

		m_strPurCorpIdOld = PuPubVO.getString_TrimZeroLenAsNull(bcp
				.getBodyValueAt(e.getRow(), "pk_purcorp"));

		// ����˾Ϊ��
		// pk_reqcorp =
		// PuPubVO.getString_TrimZeroLenAsNull(bcp.getBillModel().getValueAt(currow,
		// "pk_reqcorp"));
		// if (pk_reqcorp == null) {
		// Logger.debug("pk_reqcorp = null, �ɹ���˾����ȡֵ");/*-=notranslate=-*/
		// refpane = (UIRefPane)
		// bcp.getBillData().getBodyItem("cpurorganizationname").getComponent();
		// AbstractRefModel refModel = refpane.getRefModel();
		// refModel.addWherePart(" and 1>0 ");
		// return true;
		// }
		// // ����˾���빺��������˾
		// if (!pk_reqcorp.equals(strPk_corp)) {
		// // �빺��������˾������˾
		// // ���۴���Ƿ�¼�룬�ɹ���֯��������Ϊ���빺��������˾������˾���ɱ༭
		// refpane = (UIRefPane)
		// bcp.getBillData().getBodyItem("cpurorganizationname").getComponent();
		// AbstractRefModel refModel = refpane.getRefModel();
		// refModel.addWherePart(" and bd_purorg.ownercorp in ('" + strPk_corp +
		// "','" + pk_reqcorp + "') ");
		//
		// return true;
		// }
		// ����˾=�빺��������˾
		// if (pk_reqcorp.equals(strPk_corp)) {
		// ���δ¼��
		if (PuPubVO.getString_TrimZeroLenAsNull(bcp.getBillModel().getValueAt(
				currow, "cmangid")) == null) {
			// ���δ¼�롢���ɱ༭
			SCMEnv.out("���δ¼�롢���ɱ༭");/*-=notranslate=-*/
			return false;
		}
		// ���¼��
		// ����ֱ�˲ɹ� �ɹ���֯Ϊ����˾���ɱ༭
		if (bcp.getHeadItem("isdirecttransit").getValue().equals("true")) {
			refpane = (UIRefPane) bcp.getBillData().getBodyItem(
					"cpurorganizationname").getComponent();
			AbstractRefModel refModel = refpane.getRefModel();
			refModel.addWherePart(" and bd_purorg.ownercorp ='" + strPk_corp
					+ "' ");
			return true;
		}
		try {
			String cbaseid = PuPubVO.getString_TrimZeroLenAsNull(bcp
					.getBillModel().getValueAt(e.getRow(), "cbaseid"));
			String reqstoorg = PuPubVO.getString_TrimZeroLenAsNull(bcp
					.getBillModel().getValueAt(e.getRow(), "pk_reqstoorg"));
			queryProduceInfo(new String[] { cbaseid },
					new String[] { reqstoorg });

			// setCpurorganization(bcp, e.getRow(), e.getRow());
			InvSourceVO invSourceVO = getCpurorganization(bcp, e.getRow());
			if ((!h_ProduceInfo.containsKey(cbaseid + reqstoorg) || !h_ProduceInfo
					.get(cbaseid + reqstoorg)[0].equals("MR"))
					&& invSourceVO != null
					&& invSourceVO.getPurOrgan() != null
					&& invSourceVO.getPurOrgan().trim().length() != 0
					&& !invSourceVO.getPurCorp().equals(strPk_corp)) {
				// ���ɴ��,���������õĲɹ���˾ ���� �빺��������˾�������Ա༭
				return false;
			} else {
				// ѯ����Դ�嵥���ɹ���˾�����빺��������˾||û��ѯ����Դ�嵥
				// �ɹ���֯����ȫ���ſɼ�
				refpane = (UIRefPane) bcp.getBillData().getBodyItem(
						"cpurorganizationname").getComponent();
				AbstractRefModel refModel = refpane.getRefModel();
				refModel.addWherePart("");
				refModel.setWherePart(" bd_purorg.sealdate is null ");
				// refpane.setRefModel(refModel);
				// addWherePart(" and bd_purorg.ownercorp ='"
				// + strPk_corp + "' ");

				// String strPurCorp = bcp.getBodyValueAt(e.getRow(),
				// "pk_purcorp") + "";
				return true;
			}
		} catch (BusinessException e1) {
			// ��־�쳣
			nc.vo.scm.pub.SCMEnv.out(e1);
		}

		// }
		// // ������Բ�
		// refpane = (UIRefPane) bcp.getBillData().getBodyItem(
		// "cpurorganizationname").getComponent();
		// AbstractRefModel refModel = refpane.getRefModel();
		// refModel.addWherePart(" and 1>0 ");
		// if(m_strPurCorpIdOld!=null){
		// refModel.addWherePart(" and bd_purorg.ownercorp ='"
		// + strPk_corp + "' ");
		// }
		// return true;
		// }
		// }
		return true;
	}

	/**
	 * ����ֿ⣺�ɿգ��ɱ༭��Ĭ�ϲ���ʾ a) �༭ǰ��飺�������˾Ϊ�ջ��ϲ���Դ����ID�ǿգ��򲻿ɱ༭ b)
	 * �༭ǰ�������ã�������������֯�µĲֿ⵵��
	 */
	public static void beforeEditWhenBodyReqWare(BillCardPanel bcp,
			BillEditEvent e) {
		String strReqCorp = PuPubVO.getString_TrimZeroLenAsNull(bcp
				.getBodyValueAt(e.getRow(), "pk_reqcorp"));
		if (strReqCorp == null) {
			Logger.debug("����˾δ¼��,���塰����ֿ⡱��Ŀ���ɱ༭");
			bcp.getBillData().getBillModel().setCellEditable(e.getRow(),
					"reqstoorgname", false);
			return;
		}
		String strUpSrcBillId = PuPubVO.getString_TrimZeroLenAsNull(bcp
				.getBodyValueAt(e.getRow(), "cupsourcebillid"));
		if (strUpSrcBillId != null) {
			Logger.debug("����Դ�ĵ�����,���塰����ֿ⡱��Ŀ���ɱ༭");
			bcp.getBillData().getBillModel().setCellEditable(e.getRow(),
					"reqstoorgname", false);
			return;
		}
		bcp.getBillData().getBillModel().setCellEditable(
				e.getRow(),
				"reqstoorgname",
				bcp.getBillData().getBillModel().getItemByKey("reqstoorgname")
						.isEdit());
		String strReqStoOrg = PuPubVO.getString_TrimZeroLenAsNull(bcp
				.getBodyValueAt(e.getRow(), "pk_reqstoorg"));
		PuTool.restrictWarehouseRefByStoreOrg(bcp, strReqCorp, strReqStoOrg,
				"cwarehousename");
	}

	/*
	 * ����˾���ж��Ƿ���Ա༭
	 */
	public static void beforeEditWhenBodyReqcorp(BillCardPanel bcp,
			BillEditEvent e) {
		String strUpSrcBillId = PuPubVO.getString_TrimZeroLenAsNull(bcp
				.getBodyValueAt(e.getRow(), "csourcebillid"));
		if (strUpSrcBillId != null) {
			Logger.debug("����Դ�ĵ�����,���塰����˾����Ŀ���ɱ༭");
			setReqCorpEditable(bcp, e, false);
			return;
		} else if ((Integer) bcp.getHeadItem("ipraysource").getValueObject() == 8) {
			Logger.debug("��Դ����������,���塰����˾����Ŀ���ɱ༭");
			setReqCorpEditable(bcp, e, false);
			return;
		}
		// else{
		// String strCurrentCorp =
		// getClientEnvironment().getCorporation().getUnitname();
		// bcp.setBodyValueAt(strCurrentCorp, e.getRow(), "reqcorpname");
		// bcp.getBillData().getBillModel().setCellEditable(e.getRow(),
		// "reqcorpname", true);
		// }

	}

	/*
	 * �ɹ���˾�����ɱ༭
	 */
	public static void beforeEditWhenBodyPurcorp(BillCardPanel bcp,
			BillEditEvent e) {
		bcp.getBillData().getBillModel().setCellEditable(e.getRow(),
				"purcorpname", false);
	}

	/**
	 * ��������֯����������˾�µĿ����֯�������ɿ�,�ɱ༭��Ĭ�ϲ���ʾ a) �༭ǰ��飺����ϲ���Դ����ID�ǿգ����ɱ༭ b)
	 * �༭ǰ�������ã���������˾�����֯
	 */
	public static void beforeEditWhenBodyReqStore(BillCardPanel bcp,
			BillEditEvent e) {
		// �༭ǰ��飺����ϲ���Դ����ID�ǿգ����ɱ༭
		String strUpSrcBillId = PuPubVO.getString_TrimZeroLenAsNull(bcp
				.getBodyValueAt(e.getRow(), "cupsourcebillid"));
		if (strUpSrcBillId != null) {
			Logger.debug("����Դ�ĵ�����,���塰��������֯����Ŀ���ɱ༭");
			bcp.getBillData().getBillModel().setCellEditable(e.getRow(),
					"reqstoorgname", false);
			return;
		} else {
			bcp.getBillData().getBillModel().setCellEditable(
					e.getRow(),
					"reqstoorgname",
					bcp.getBillData().getBillModel().getItemByKey(
							"reqstoorgname").isEdit());
		}

		// �༭ǰ�������ã���������˾�����֯
		UIRefPane paneReqStoOrg = ((UIRefPane) bcp.getBodyItem("reqstoorgname")
				.getComponent());
		paneReqStoOrg.getRefModel().setSealedDataShow(false);
		Object oTemp = bcp.getBodyValueAt(e.getRow(), "pk_reqcorp");
		if (oTemp != null) {
			paneReqStoOrg.getRefModel().setPk_corp(oTemp.toString());
		}
		paneReqStoOrg.getRefModel().getRefSql();
	}

	/**
	 * ���鹩Ӧ�� i. ����ɹ���֯δ¼�룬���ɱ༭ ii. ��������ȡ�ɹ���֯�ġ�������˾���µĿ��̵���
	 */
	public static boolean beforeEditWhenBodyVendor(BillCardPanel bcp,
			BillEditEvent e) {
		String strUpSrcBillId = PuPubVO.getString_TrimZeroLenAsNull(bcp
				.getBodyValueAt(e.getRow(), "pk_purcorp"));
		if (strUpSrcBillId == null) {
			Logger.debug("�ɹ���˾δ¼�룬���ɱ༭");
			return false;
		}
		UIRefPane paneReqStoOrg = ((UIRefPane) bcp.getBodyItem("cvendorname")
				.getComponent());
		paneReqStoOrg.getRefModel().setSealedDataShow(false);
		Object oTemp = bcp.getBodyValueAt(e.getRow(), "pk_purcorp");
		if (paneReqStoOrg.getRefModel().getPk_corp() != null
				&& paneReqStoOrg.getRefModel().getPk_corp().equals(oTemp)) {
			return true;
		}
		if (oTemp != null) {
			paneReqStoOrg.getRefModel().setPk_corp(oTemp.toString());
			paneReqStoOrg.getRefModel().reloadData();
		}
		return true;
	}

	/**
	 * ��Ŀ�׶� �༭ǰ���ò��գ����յ�ǰ��˾��Ŀ�׶ε���
	 */
	public static void beforeEditWhenBodyProjectPhase(BillCardPanel bcp,
			BillEditEvent e) {
		// �༭ǰ��������
		UIRefPane paneReqStoOrg = ((UIRefPane) bcp.getBodyItem(
				"cprojectphasename").getComponent());
		int iRow = e.getRow();
		Object oTemp = bcp.getBodyValueAt(iRow, "cprojectid");
		if (oTemp != null) {
			((ProjectPhase) paneReqStoOrg.getRefModel()).setJobmngfilId(oTemp
					.toString());
			// ProjectPhase rmArrStoOrg = new ProjectPhase(oTemp.toString());
			// paneReqStoOrg.setRefModel(rmArrStoOrg);
			bcp.getBillData().getBillModel().setCellEditable(
					iRow,
					"cprojectphasename",
					bcp.getBillData().getBillModel().getItemByKey(
							"cprojectphasename").isEdit());
		} else {
			bcp.getBillData().getBillModel().setCellEditable(iRow,
					"cprojectphasename", false);
		}
	}

	/**
	 * ��Ŀ �༭ǰ���ò��գ����յ�ǰ��˾��Ŀ����
	 */
	public static void beforeEditWhenBodyProject(BillCardPanel bcp,
			BillEditEvent e) {
		// �༭ǰ�������ã���������˾�����֯
		UIRefPane paneReqStoOrg = ((UIRefPane) bcp.getBodyItem("cprojectname")
				.getComponent());
		// JobmngfilDefaultRefModel rmArrStoOrg = new
		// JobmngfilDefaultRefModel("��Ŀ������");
		AbstractRefModel rmArrStoOrg = paneReqStoOrg.getRefModel();
		rmArrStoOrg.setSealedDataShow(false);
		// paneReqStoOrg.setRefModel(rmArrStoOrg);

	}

	/**
	 * <p>
	 * ���κţ��༭ǰ��飺�������˾Ϊ�ջ��ϲ���Դ����ID�ǿգ��򲻿ɱ༭(+V31SP1�߼�)
	 * <p>
	 * 
	 * @�޸ģ� V5
	 */
	public static boolean beforeEditWhenBodyProduceNum(ToftPanel ui,
			BillCardPanel bcp, String sLoginCorpId, BillEditEvent e) {

		int iRow = e.getRow();

		// ��������ι���ֱ�ӷ���
		if (!PuTool
				.isBatchManaged((String) bcp.getBodyValueAt(iRow, "cmangid"))) {
			ui.showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000531")/*
											 * @res "����������ι������ɱ༭"
											 */);
			return false;
		}
		//
		ParaVOForBatch vo = new ParaVOForBatch();

		// ����FieldName
		vo.setMangIdField("cmangid");
		vo.setInvCodeField("cinventorycode");
		vo.setInvNameField("cinventoryname");
		vo.setSpecificationField("cinventoryspec");
		vo.setInvTypeField("cinventorytype");
		vo.setMainMeasureNameField("cinventoryunit");
		vo.setAssistUnitIDField("cassistunit");
		vo.setIsAstMg(new UFBoolean(PuTool.isAssUnitManaged((String) bcp
				.getBodyValueAt(iRow, "cbaseid"))));
		vo.setWarehouseIDField("cwarehouseid");
		vo.setFreePrefix("vfree");

		// ���ÿ�Ƭģ��,��˾��
		vo.setCardPanel(bcp);
		vo.setPk_corp(sLoginCorpId);
		vo.setEvent(e);

		try {
			PuTool.beforeEditWhenBodyBatch(vo);
		} catch (Exception ex) {
			Logger.debug("�������κų���" + ex.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * a) �ɹ�Ա �༭ǰ: ��������ȡ���ɹ���˾���µ���Ա����
	 */
	public static void beforeEditWhenBodyEmployer(BillCardPanel bcp,
			BillEditEvent e) {

		UIRefPane paneEmployee = ((UIRefPane) bcp.getBodyItem("cemployeename")
				.getComponent());
		AbstractRefModel rmArrStoOrg = paneEmployee.getRefModel();
		rmArrStoOrg.setSealedDataShow(false);// ����ʾ�������
		Object oTemp = bcp.getBodyValueAt(e.getRow(), "pk_purcorp");
		if (oTemp != null) {
			rmArrStoOrg.setPk_corp(oTemp.toString());
			rmArrStoOrg.reloadData();
		}
		paneEmployee.setRefModel(rmArrStoOrg);
	}

	/**
	 * ���ߣ����� ���ܣ��޸Ĳ�Ʒǰ���� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
	 * ���ڣ�(2005-6-23 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public static void beforeEditWhenBodyProduct(BillCardPanel bcPanel,
			String sLoginCorpId, String sLoginDate, BillEditEvent e) {

		bcPanel.stopEditing();

		final int n = e.getRow();
		UIRefPane refpane = null;

		String[] invmandoc = null;
		// String condition = null;
		StringBuffer con = new StringBuffer();
		refpane = (UIRefPane) bcPanel.getBodyItem("cproductname")
				.getComponent();
		// refpane = new UIRefPane();
		// Object cproductmanid = bcPanel.getBodyValueAt(n, "cproductmanid");
		// Object cproductname = bcPanel.getBodyValueAt(n, "cproductname");
		Object cbaseid = bcPanel.getBodyValueAt(n, "cbaseid");
		String basid = null;
		if (cbaseid != null && cbaseid.toString().trim().length() > 0) {
			basid = cbaseid.toString().trim();
		}
		// ���ù����������ѯ�������-��Ʒ

		try {
			invmandoc = PraybillHelper.queryBackMultiple(sLoginCorpId, null,
					basid, sLoginDate);
		} catch (Exception e1) {
			SCMEnv.out(e);
		}

		if (invmandoc != null && invmandoc.length > 0) {
			for (int i = 0; i < invmandoc.length; i++) {
				if (i < invmandoc.length - 1) {
					con.append(" '");
					con.append(invmandoc[i] + "',");
				} else if (i == invmandoc.length - 1) {
					con.append(" '");
					con.append(invmandoc[i] + "' ");
				}
			}

		}

		String wherePart = " AND bd_invmandoc.pk_invbasdoc=bd_invbasdoc.pk_invbasdoc   and bd_invmandoc.pk_corp ='"
				+ sLoginCorpId + "'";
		if (con != null && con.length() > 1) {
			wherePart = "and bd_invmandoc.pk_invmandoc in(" + con.toString()
					+ ")" + wherePart;

		} else if (con.length() == 0) {
			wherePart = "and bd_invmandoc.pk_invmandoc is null " + wherePart;
		}
		// NewInventoryRef refModel = new NewInventoryRef();
		nc.ui.bd.ref.DefaultRefModel refModel = new nc.ui.bd.ref.DefaultRefModel();
		refModel.setPk_corp(sLoginCorpId);
		refModel.setRefNodeName("�������");
		refModel.addWherePart(wherePart);
		refpane.setRefModel(refModel);
		// refModel.setPk_corp(getCorpId());

		// ��治��ʾ
		// refModel.addWherePart(wherePart);
		refpane.setRefModel(refModel);
	}

	/**
	 * ��������:�б任�¼���Ӧ
	 * 
	 * @param event
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void bodyRowChange(BillEditEvent event) {
		if (event.getSource().getClass().getName().equals(
				nc.ui.pub.bill.BillModelRowEditPanel.class.getName())) {
			// ����˾������������֯_������ÿ����֯Ȩ�޺󣬿������繫˾�Ŀ����֯
			Object oTemp = getBillCardPanel().getBodyValueAt(event.getRow(),
					"pk_reqcorp");
			if (oTemp != null) {
				UIRefPane paneReqStoOrg = ((UIRefPane) getBillCardPanel()
						.getBodyItem("reqstoorgname").getComponent());
				paneReqStoOrg.getRefModel().setPk_corp(oTemp.toString());
			}
			return;
		}
		if ((UITable) event.getSource() == getBillCardPanel().getBillTable()) {
			// ������Ŀ�׶οɱ༭��
			setProjPhaseEditable(event);
			// ˢ�´�����ʾ/���ؽ���
			freshOnhandnum(event.getRow());
		} else if ((UITable) event.getSource() == getBillListPanel()
				.getBodyTable()) {
			// ���ø��������ռ�����Ϣ�ɱ༭��
			// setAssisUnitEditState2(event);
			// ��ͬ����������������Ӧ����Ŀ�׶β���
			Object oTemp = getBillCardPanel().getBodyValueAt(event.getRow(),
					"cprojectid");
			if (oTemp != null) {
				UIRefPane nRefPanel = (UIRefPane) getBillCardPanel()
						.getBodyItem("cprojectphasename").getComponent();
				nRefPanel.setIsCustomDefined(true);
				nRefPanel.setRefModel(new ProjectPhase((String) oTemp));
			}
			// ��ͬ����������������Ӧ���������Զ������
			InvVO invVO = getVOForFreeItem(event);
			invVO.setIsFreeItemMgt(new Integer(1));
			m_freeItem.setFreeItemParam(invVO);
		}
	}

	/**
	 * ��������:�ı���水ť״̬
	 */
	private void updateButtonsAll() {
		int iLen = getBtnTree().getButtonArray().length;
		for (int i = 0; i < iLen; i++) {
			update(getBtnTree().getButtonArray()[i]);
		}
	}

	/**
	 * �������ڣ� 2005-9-20 ���������� ���°�ť״̬���ݹ鷽ʽ��
	 */
	private void update(ButtonObject bo) {
		updateButton(bo);
		if (bo.getChildCount() > 0) {
			for (int i = 0, len = bo.getChildCount(); i < len; i++)
				update(bo.getChildButtonGroup()[i]);
		}
	}

	/**
	 * �빺������ʱ����齨�鶩�������Ƿ���ڣ���������-�̶���ǰ�ڣ������ǣ�����ʾ�������н��鶩����������Ӧ�ɹ����ڣ��Ƿ񱣴桱��
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param PraybillItemVO
	 *            bodyVO
	 * @return HashMap:
	 *         <p>
	 *         KEY:��˾ID�������֯ID���������ID+�빺����;
	 *         <p>
	 *         <p>
	 *         VALUE:�ɹ���ǰ������=�̶���ǰ��+(��������-��ǰ������)*��ǰ��ϵ��/��ǰ������
	 *         <p>
	 * @author lixiaodong
	 * @time 2008-02-27 ����13:44:12
	 */
	private boolean checkAdvanceDaysBatch(PraybillItemVO[] items,
			UFDate dpraydate) {
		m_hmapPurAdvanceInfo = loadAdvancedate(items);
		if (m_hmapPurAdvanceInfo != null) {
			StringBuffer errorLins = new StringBuffer();
			UFDate ddemanddate = null;
			UFDate dsuggestdate = null;

			for (int i = 0; i < items.length; i++) {
				ddemanddate = items[i].getDdemanddate();
				dsuggestdate = items[i].getDsuggestdate();
				if (dpraydate != null && ddemanddate != null
						&& dsuggestdate != null) {
					Object key = m_hmapPurAdvanceInfo.get(items[i].getPk_corp()
							+ items[i].getPk_reqstoorg()
							+ items[i].getCbaseid() + items[i].getNpraynum());
					if (PuPubVO.getString_TrimZeroLenAsNull(key) == null)
						continue;
					int intAdvanceValue = Integer.valueOf(key.toString());
					if (PuPubVO.getString_TrimZeroLenAsNull(intAdvanceValue) != null) {
						if (!(ddemanddate.after(dsuggestdate
								.getDateAfter(intAdvanceValue)) || dsuggestdate
								.getDateAfter(intAdvanceValue).equals(
										ddemanddate))) {
							errorLins.append(m_lanResTool
									.getStrByID("40040101",
											"UPP40040101-000543", null,
											new String[] {
													items[i].getRowno() + "",
													dsuggestdate + "",
													ddemanddate.getDateBefore(
															intAdvanceValue)
															.toString() })/* {0}�н��鶩������{1}����Ӧ�ɹ�����{2}" */
									+ "\n");
						}
					}
				}
			}

			if (errorLins.length() > 0) {
				int ret = MessageDialog.showYesNoDlg(this, NCLangRes
						.getInstance().getStrByID("40040401",
								"UPPSCMCommon-000270")/*
														 * @res "��ʾ"
														 */, errorLins
						+ NCLangRes.getInstance().getStrByID("40040401",
								"UPP40040401-000159")/*
														 * @res "���Ƿ�������棿"
														 */);
				if (ret == MessageDialog.ID_NO) {
					showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000010")/* "����ʧ��" */);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * ��������:���Ϊ����������ʱ,��鸨�����Ƿ����
	 * 
	 * @return java.lang.String
	 */
	private String checkAssistUnit() {
		String sMessage = "";
		final int nRow = getBillCardPanel().getRowCount();
		Object temp = null;
		Object oAssNum = null;
		Object oAssUnit = null;
		UFDouble ufdAssNum = null;
		Vector vLines = new Vector();
		for (int i = 0; i < nRow; i++) {
			temp = getBillCardPanel().getBodyValueAt(i, "cbaseid");
			if (temp == null || temp.toString().trim().equals(""))
				continue;
			if (nc.ui.pu.pub.PuTool.isAssUnitManaged(temp.toString().trim())) {
				oAssNum = getBillCardPanel().getBodyValueAt(i, "nassistnum");
				oAssUnit = getBillCardPanel().getBodyValueAt(i,
						"cassistunitname");
				ufdAssNum = (oAssNum == null || oAssNum.toString().trim()
						.equals("")) ? null : new UFDouble(oAssNum.toString()
						.trim());
				if (oAssUnit == null || oAssUnit.toString().trim().equals("")
						|| ufdAssNum == null || ufdAssNum.doubleValue() == 0) {
					if (vLines.size() > 0) {
						vLines.addElement(m_lanResTool.getStrByID("40040101",
								"UPPSCMCommon-000000")/*
														 * @res "��"
														 */);
					}
					vLines.addElement(new Integer(i + 1));
				}
			}
		}
		int vLinesSize = vLines.size();
		if (vLinesSize > 0) {
			sMessage += vLines.elementAt(0);
			for (int i = 1; i < vLinesSize; i++) {
				sMessage += ",";
				sMessage += vLines.elementAt(i);
			}
		}
		String[] value = new String[] { sMessage };
		if (value != null && value.length > 0 && value[0] != null
				&& !value[0].equals("")) {
			sMessage = (m_lanResTool.getStrByID("40040101",
					"UPP40040101-000437", null, value));
		}
		return sMessage;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-11-20 17:09:22)
	 */
	private boolean checkBeforeSave(PraybillVO VO) {

		/* ��鵥���к� */
		if (!BillRowNo.verifyRowNosCorrect(getBillCardPanel(), "crowno")) {
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000010")/*
											 * @res "����ʧ��"
											 */);
			return false;
		}
		/* ��鵥��ģ��ǿ��� */
		try {
			nc.ui.pu.pub.PuTool.validateNotNullField(getBillCardPanel());
		} catch (Exception e) {
			MessageDialog.showWarningDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000035")/* "����ģ��ǿ�����" */, e
					.getMessage());
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000010")/* @res"����ʧ��" */);
			return false;
		}

		// �Ϸ��Լ��
		try {
			if (getBillCardPanel().getRowCount() < 1) {
				MessageDialog
						.showErrorDlg(this, m_lanResTool.getStrByID("common",
								"UC001-0000001")/* "����" */, m_lanResTool
								.getStrByID("40040101", "UPP40040101-000036")/* "�빺������Ϊ�գ����ܱ��棡" */);
				showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000010")/* "����ʧ��" */);
				return false;
			}
			VO.validate();
		} catch (ValidationException e) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000037")/* "�Ϸ��Լ��" */, e
					.getMessage());
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000010")/* "����ʧ��" */);
			return false;
		}
		PraybillItemVO[] items = VO.getBodyVO();
		UFDate ddemanddate = null;
		UFDate dsuggestdate = null;
		UFDate dpraydate = null;
		if (VO.getHeadVO() != null) {
			dpraydate = VO.getHeadVO().getDpraydate();
		}
		if (items != null && items.length > 0) {
			String errorRows = "";
			for (int i = 0; i < items.length; i++) {
				ddemanddate = items[i].getDdemanddate();
				dsuggestdate = items[i].getDsuggestdate();
				if (PuPubVO.getString_TrimZeroLenAsNull(items[i]
						.getPk_purcorp()) == null) {
					MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
							"common", "UC001-0000001")/* "����" */, m_lanResTool
							.getStrByID("common", "4004COMMON000000015")/* @res*�ɹ���˾" */
							+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
									.getStrByID("smcomm", "UPP1005-000239")/*
																			 * @res
																			 * "����Ϊ�ա�"
																			 */);
					return false;
				}
				if (dpraydate != null && ddemanddate != null
						&& dsuggestdate != null) {
					if (!((dpraydate.before(ddemanddate) || dpraydate
							.equals(ddemanddate)) && (dpraydate
							.before(dsuggestdate) || dpraydate
							.equals(dsuggestdate)))) {
						errorRows += (items[i].getRowno() + ",");
					}
				}
			}
			if (errorRows.trim().length() > 0) {
				int ret = MessageDialog.showYesNoDlg(this, NCLangRes
						.getInstance().getStrByID("40040401",
								"UPPSCMCommon-000270")/*
														 * @res "��ʾ"
														 */, m_lanResTool.getStrByID("40040101", "UPT40040101-001123")
						+ errorRows
						+ m_lanResTool.getStrByID("40040101",
								"UPP40040101-001108")
						+ m_lanResTool.getStrByID("40040101",
								"UPP40040101-001109")
						+ " "
						+ dpraydate
						+ m_lanResTool.getStrByID("40040101",
								"UPP40040101-001110")
						+ ddemanddate
						+ m_lanResTool.getStrByID("40040101",
								"UPP40040101-001111")
						+ dsuggestdate
						+ "\n"
						+ NCLangRes.getInstance().getStrByID("40040401",
								"UPP40040401-000159")/*
														 * @res "���Ƿ�������棿"
														 */);
				if (ret == MessageDialog.ID_NO) {
					showHintMessage(m_lanResTool.getStrByID("40040101",
							"UPP40040101-000524", null, new String[] {
									dpraydate + "", dsuggestdate + "",
									ddemanddate + "" })/* �빺����{0}����С�ڽ��鶩������{1}����������{2}!" */);
					return false;
				}
			}
			for (int i = 0; i < items.length; i++) {
				ddemanddate = items[i].getDdemanddate();
				dsuggestdate = items[i].getDsuggestdate();
				if (dsuggestdate != null
						&& ddemanddate != null
						&& !(ddemanddate.after(dsuggestdate) || dsuggestdate
								.equals(ddemanddate))) {
					MessageDialog
							.showErrorDlg(this, m_lanResTool.getStrByID(
									"common", "UC001-0000001")/* "����" */,
									m_lanResTool.getStrByID("40040101",
											"UPP40040101-000525", null,
											new String[] { dsuggestdate + "",
													ddemanddate + "" })/* ����ɹ�����{0}����С���빺����{1}!" */);
					showHintMessage(m_lanResTool.getStrByID("40040101",
							"UPP40040101-000525", null, new String[] {
									dsuggestdate + "", ddemanddate + "" })/* ����ɹ�����{0}����С����������{1}!" */);
					return false;
				}
				// }
				else if (dpraydate != null && ddemanddate != null
						&& dsuggestdate == null) {
					if (!(dpraydate.before(ddemanddate) || dpraydate
							.equals(ddemanddate))) {
						MessageDialog
								.showErrorDlg(this, m_lanResTool.getStrByID(
										"common", "UC001-0000001")/* "����" */,
										m_lanResTool.getStrByID("40040101",
												"UPP40040101-000526", null,
												new String[] { dpraydate + "",
														ddemanddate + "" })/* �빺����{0}����С����������{1}!" */);
						showHintMessage(m_lanResTool.getStrByID("40040101",
								"UPP40040101-000526", null, new String[] {
										dpraydate + "", ddemanddate + "" })/* �빺����{0}����С����������{1}!" */);
						return false;
					}
				}
			}
			if (!checkAdvanceDaysBatch(items, dpraydate)) {
				return false;
			}
		}

		String sMessage = checkAssistUnit();
		if (sMessage.length() > 0) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000037")/* "�Ϸ��Լ��" */, sMessage);
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000010")/* "����ʧ��" */);
			return false;
		}
		return true;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-11-20 18:05:17)
	 */
	private void dispAfterSave(PraybillVO VO, ArrayList keys) {
		try {
			// ����������
			new nc.ui.scm.pub.FreeVOParse().setFreeVO(VO.getBodyVO(), "vfree",
					"vfree", "cbaseid", "cmangid", true);

		} catch (Exception e) {
			reportException(e);
		}
		Vector vTemp = new Vector();
		if (m_VOs != null) {
			// �Ѿ������빺��(����)
			if (m_bAdd) {
				for (int i = 0; i < m_VOs.length; i++) {
					vTemp.addElement(m_VOs[i]);
				}
				vTemp.addElement(VO);
				m_VOs = new PraybillVO[vTemp.size()];
				vTemp.copyInto(m_VOs);
				m_nPresentRecord = m_VOs.length - 1;
				// �Ѿ������빺��(�޸�)
			} else {
				m_VOs[m_nPresentRecord] = VO;
			}
		} else {
			/* �������빺�� */
			m_VOs = new PraybillVO[1];
			m_VOs[0] = VO;
			m_nPresentRecord = 0;
		}
		/* ���ñ��棬�в���������Ϊ�ң��������� */
		getBillCardPanel().setEnabled(false);
		/* ��������װ�����ݣ������½��� */
		/*
		 * for (int i = 0; i < 1; i++) { try {
		 * getBillCardPanel().setBillValueVO(m_VOs[m_nPresentRecord]); } catch
		 * (Exception e) { continue; } }
		 */
		// PraybillHeaderVO headerVO = m_VOs[m_nPresentRecord].getParentVO();
		// ��������ˢ�½���,����ˢ�²�������
		String[] needRefreshHeadKey = new String[] { "ibillstatus", "ts",
				"vpraycode", "cpraybillid" };
		String[] needRefreshTailKey = new String[] { "dauditdate", "cauditpsn",
				"tmaketime", "tlastmaketime", "taudittime" };
		String[] needRefreshBodyKey = new String[] { "cpraybillid",
				"cpraybill_bid" };
		for (String key : needRefreshHeadKey) {
			getBillCardPanel().getHeadItem(key).setValue(
					m_VOs[m_nPresentRecord].getParentVO()
							.getAttributeValue(key));
		}

		for (String key : needRefreshTailKey) {
			getBillCardPanel().getTailItem(key).setValue(
					m_VOs[m_nPresentRecord].getParentVO()
							.getAttributeValue(key));
		}
		int status = -1, iPos = 0;
		for (PraybillItemVO item : (PraybillItemVO[]) m_VOs[m_nPresentRecord]
				.getChildrenVO()) {
			status = item.getBodyEditStatus();
			if (!String.valueOf(status).equals("3")) {
				for (String key : needRefreshBodyKey) {
					getBillCardPanel().getBillModel().setValueAt(
							m_VOs[m_nPresentRecord].getChildrenVO()[iPos]
									.getAttributeValue(key), iPos, key);
				}
			}
			getBillCardPanel().getBillModel().setValueAt(
					m_VOs[m_nPresentRecord].getChildrenVO()[iPos]
							.getAttributeValue("ts"), iPos, "ts");
			iPos++;
		}

		nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillCardPanel());
		/* ��ʾ״̬ͼƬ */
		// V5 Del : setImageTypeMy(m_nPresentRecord);
		getBillCardPanel().getBillModel().execLoadFormula();
		getBillCardPanel().updateValue();
		getBillCardPanel().setEnabled(false);
		m_bEdit = false;
		/* ��ʾ��ע */
		if (getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane) {
			UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel().getHeadItem(
					"vmemo").getComponent();
			nRefPanel3.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVmemo());
		}
		// ˢ�±�ͷ�Զ��������
		displayHeadVdef();
		if (getBillCardPanel().getBodyItem("csourcebillcode").isShow()
				|| getBillCardPanel().getBodyItem("csourcebillrowno").isShow()) {//
			// ��ʾ������Դ��Ϣ
			nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanel(),
					nc.vo.scm.pu.BillTypeConst.PO_PRAY);
		}
		// ����������־
		m_bAdd = false;
		setButtonsCard();
		m_nUIState = 0;
	}

	/**
	 * ˢ���Զ��������
	 * 
	 */
	private void displayHeadVdef() {
		try {
			if (m_VOs != null && m_VOs[m_nPresentRecord] != null) {
				for (int i = 1; i < 21; i++) {
					if (getBillCardPanel().getHeadItem("vdef" + i).isShow()
							&& getBillCardPanel().getHeadItem("vdef" + i)
									.getComponent() instanceof UIRefPane
							&& m_VOs[m_nPresentRecord].getHeadVO()
									.getAttributeValue("vdef" + i) != null) {
						UIRefPane defRefPanel = (UIRefPane) getBillCardPanel()
								.getHeadItem("vdef" + i).getComponent();
						defRefPanel.setValue(m_VOs[m_nPresentRecord]
								.getHeadVO().getAttributeValue("vdef" + i)
								.toString());
					}
				}
			}
		} catch (Exception e) {
			SCMEnv.out("displayHeadVdef() error:" + e.getMessage());
		}

	}

	/**
	 * ���ߣ���ά�� ���ܣ�����ͷ��ʽ���ݣ����ⲻ�ɲ��ճ������� ������ ���أ� ���⣺ ���ڣ�(2004-5-20 14:18:41)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param vo
	 *            nc.vo.pr.pray.PraybillVO V5.5ɾ��
	 */
	// public void execHeadTailFormula(PraybillVO vo) {
	// if (vo == null) {
	// return;
	// }
	// // ������ʽ
	// PraybillHeaderVO voHead = vo.getHeadVO();
	// UIRefPane refpaneA = null;
	//
	// String[] saFormulaA = new String[] {
	// "getColValue(bd_busitype,businame,pk_busitype,cbiztype)" };
	// PuTool.getFormulaParse().setExpressArray(saFormulaA);
	//
	// // PuTool.getFormulaParse().addVariable("cbiztype",
	// // new String[] { voHead.getCbiztype() });
	// String[][] saRetA = PuTool.getFormulaParse().getValueSArray();
	//
	// refpaneA = (UIRefPane) getBillCardPanel().getHeadItem("cbiztype")
	// .getComponent();
	//
	// if (nc.vo.scm.pu.PuPubVO.getString_TrimZeroLenAsNull(refpaneA
	// .getUITextField().getText()) == null) {
	// if (saRetA != null && saRetA[0] != null)
	// refpaneA.getUITextField().setText(saRetA[0][0]);
	// }
	// }
	/**
	 * �˴����뷽��˵���� ��������:���ǵ����� ���ߣ���ά�� �������: ����ֵ: �쳣����: ����:
	 */
	private void filterNullLine() {
		// �����ֵ�ݴ�
		Object oTempValue = null;
		// ����model
		nc.ui.pub.bill.BillModel bmBill = getBillCardPanel().getBillModel();
		// ����кţ�Ч�ʸ�һЩ��
		final int iInvCol = bmBill.getBodyColByKey(m_sInvMngIDItemKey);

		// �����д����
		if (bmBill != null && iInvCol >= 0 && iInvCol < bmBill.getColumnCount()) {
			// ����
			final int iRowCount = getBillCardPanel().getRowCount();
			// �Ӻ���ǰɾ
			for (int line = iRowCount - 1; line >= 0; line--) {
				// ���δ��
				oTempValue = bmBill.getValueAt(line, iInvCol);
				if (oTempValue == null
						|| oTempValue.toString().trim().length() == 0)
					// ɾ��
					getBillCardPanel().getBillModel().delLine(
							new int[] { line });
			}
		}
		if (bmBill.getRowCount() <= 0)
			onAppendLine(getBillCardPanel(), this);
	}

	/**
	 * ��������:��ô���Ĳɹ���ǰ��
	 */
	private static int getAdvanceDays(ToftPanel ui, BillCardPanel bcp,
			BillEditEvent event) {
		final int n = event.getRow();
		Vector v = new Vector();
		try {
			String strReqCorpId = bcp.getBillModel()
					.getValueAt(n, "pk_reqcorp") == null ? null : bcp
					.getBillModel().getValueAt(n, "pk_reqcorp").toString();// bodyVO[n].getPk_reqcorp();
			String strReqStorId = bcp.getBillModel().getValueAt(n,
					"pk_reqstoorg") == null ? null : bcp.getBillModel()
					.getValueAt(n, "pk_reqstoorg").toString();// bodyVO[n].getPk_reqstoorg();
			String strBaseId = bcp.getBillModel().getValueAt(n, "cbaseid") == null ? null
					: bcp.getBillModel().getValueAt(n, "cbaseid").toString();// bodyVO[n].getCbaseid();
			if (strReqStorId != null && strReqStorId.length() > 0
					&& strBaseId != null && strBaseId.length() > 0)
				v = PraybillHelper.queryAdvanceDays(strReqCorpId, strReqStorId,
						strBaseId);
			else
				return 0;
		} catch (java.sql.SQLException e) {
			MessageDialog
					.showErrorDlg(ui, m_lanResTool.getStrByID("40040101",
							"UPP40040101-000047")/* "�ɹ���ǰ��" */, m_lanResTool
							.getStrByID("SCMCOMMON", "UPPSCMCommon-000412")/* "SQL������" */);
			return -1;
		} catch (ArrayIndexOutOfBoundsException e) {
			MessageDialog
					.showErrorDlg(ui, m_lanResTool.getStrByID("40040101",
							"UPP40040101-000047")/* "�ɹ���ǰ��" */, m_lanResTool
							.getStrByID("SCMCOMMON", "UPPSCMCommon-000426")/* "����Խ�����" */);
			return -1;
		} catch (NullPointerException e) {
			MessageDialog
					.showErrorDlg(ui, m_lanResTool.getStrByID("40040101",
							"UPP40040101-000047")/* "�ɹ���ǰ��" */, m_lanResTool
							.getStrByID("SCMCOMMON", "UPPSCMCommon-000427")/* "��ָ�����" */);
			return -1;
		} catch (Exception e) {
			MessageDialog.showErrorDlg(ui, m_lanResTool.getStrByID("40040101",
					"UPP40040101-000047")/* "�ɹ���ǰ��" */, e.getMessage());
			return -1;
		}
		if (v.size() == 0)
			return 0;
		UFDouble dFixedahead = (UFDouble) v.elementAt(0);
		UFDouble dAheadcoff = (UFDouble) v.elementAt(1);
		UFDouble dAheadbatch = (UFDouble) v.elementAt(2);
		UFDouble d = bcp.getBillModel().getValueAt(n, "npraynum") == null ? null
				: new UFDouble(bcp.getBillModel().getValueAt(n, "npraynum")
						.toString());// bodyVO[n].getNpraynum();

		// V5,Czp,�ع�����֤��ǰ���㷨ֻ��һ��ʵ��
		return AdDayVO.getAdDaysArith(d, dFixedahead, dAheadcoff, dAheadbatch);
	}

	/**
	 * ���ܣ���ȡ������ѯ�Ի���
	 */
	private ATPForOneInvMulCorpUI getAtpDlg() {
		if (null == m_atpDlg) {
			m_atpDlg = new ATPForOneInvMulCorpUI(this);
		}
		return m_atpDlg;
	}

	/**
	 * ��������:��ÿ�Ƭ���ݿ���
	 */
	private BillCardPanel getBillCardPanel() {
		if (m_billPanel == null) {
			try {
				m_billPanel = new BillCardPanel();
				BillData bd = new BillData(getBillTempletVO());
				initBillBeforeLoad(bd);
				m_billPanel.setBillData(bd);
				// ����ǧ��λ
				/* m_billPanel.setBodyShowThMark(true);ȡ��ǧ��λӲ���� */
				// �ϼ�����ʾ
				m_billPanel.setTatolRowShow(true);
				// �����к�
				BillRowNo.loadRowNoItem(m_billPanel, "crowno");

				// ���ʻ�
				nc.ui.pu.pub.PuTool.setTranslateRender(m_billPanel);

				// �����Զ�����
				nc.ui.scm.pub.def.DefSetTool.updateBillCardPanelUserDef(
						m_billPanel, getClientEnvironment().getCorporation()
								.getPk_corp(), ScmConst.PO_Pray, // ��������
						"vdef", "vdef");

				// �汾��:С��λ��ʾһλ
				m_billPanel.getHeadItem("nversion").setDecimalDigits(1);

				// since v56, ���ò��������ĵ���Ŀ(ԭ���ǣ�������Ҫ����Ӧ�á������Լ���������Ϊ
				// Ŀǰ������������֧�ֲ�������)
				PuTool.setBatchModifyForbid(m_billPanel,
						new String[] { "vfree" });
			} catch (Exception e) {
				SCMEnv.out(e);
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000048")/* "����ģ��" */,
						m_lanResTool.getStrByID("40040101",
								"UPP40040101-000049")/* "ģ�岻���ڣ�" */);
				return null;
			}
		}
		return m_billPanel;
	}

	/**
	 * ��������:����б��ݿ���
	 */
	private BillListPanel getBillListPanel() {
		if (m_listPanel == null) {
			try {
				m_listPanel = new BillListPanel();
				m_listPanel.setName("ListPanel");
				BillListData bd = null;
				try {
					bd = new BillListData(getBillTempletVO());
					m_listPanel.setListData(bd);
				} catch (Exception e) {
					SCMEnv.out(e);
					m_listPanel.loadTemplet("40040101010000000000");
				}
				// ���ع�˾
				if (m_listPanel.getHeadItem("pk_corpname") != null)
					m_listPanel.hideHeadTableCol("pk_corpname");

				// ����ǧ��λ
				/*
				 * m_listPanel.getParentListPanel().setShowThMark(true);
				 * m_listPanel.getChildListPanel().setShowThMark(true);
				 */
				// ��ʼ������
				bd = initListDecimal(bd);

				// �����б�����
				/* m_listPanel.setListData(bd); */

				m_listPanel.getHeadTable().setSelectionMode(
						ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

				m_listPanel.addEditListener(this);
				m_listPanel.addMouseListener(this);

				// �����Զ�����
				nc.ui.scm.pub.def.DefSetTool.updateBillListPanelUserDef(
						m_listPanel, getClientEnvironment().getCorporation()
								.getPk_corp(), ScmConst.PO_Pray, // ��������
						"vdef", "vdef");
				getBillListPanel().setListData(
						getBillListPanel().getBillListData());
				m_listPanel.getHeadTable().setCellSelectionEnabled(false);
				// ���Ӻϼ���
				m_listPanel.getChildListPanel().setTotalRowShow(true);

				// �����к��������
				m_listPanel.getBodyBillModel().setSortPrepareListener(this);

				// �������
				m_listPanel.getHeadBillModel().addSortRelaObjectListener2(this);
				PuTool.setLineSelectedList(m_listPanel);
				//
				// m_listPanel.setMultiSelect(true);
				//
				//
				m_listPanel.getHeadTable().getSelectionModel()
						.addListSelectionListener(this);
				m_listPanel.updateUI();
			} catch (Exception e) {
				SCMEnv.out(e);
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000048")/* "����ģ��" */,
						m_lanResTool.getStrByID("40040101",
								"UPP40040101-000049")/* "ģ�岻���ڣ�" */);
				return null;
			}
		}

		return m_listPanel;
	}

	/**
	 * ����ʵ�ָ÷���������ҵ�����ı���
	 */
	private String getCauditid() {

		return m_cauditid;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-10-28 14:53:42)
	 */
	private int getCurVoPos() {
		return m_nPresentRecord;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-10-28 15:02:52)
	 */
	private PraybillVO[] getPraybillVOs() {
		return m_VOs;
	}

	/**
	 * ���ܣ����ز�ѯ�Ի��� ������ ���أ� ���⣺ ���ڣ�(2002-9-13 9:47:11) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.ui.pub.query.QueryConditionClient
	 */
	private PrayUIQueryDlg getQueryDlg() {

		if (m_condClient == null) {
			m_condClient = new PrayUIQueryDlg(this, m_sLoginCorpId);
			/*
			 * DefSetTool.updateQueryConditionClientUserDef(m_condClient,
			 * m_sLoginCorpId, ScmConst.PO_Pray, // �������� "po_praybill.def",
			 * "po_praybill_b.def");
			 */
			m_butnQuery = true;
			m_condClient.addExtraDate("po_praybill.dpraydate",
					getClientEnvironment().getDate(), getClientEnvironment()
							.getDate());
		}

		return m_condClient;
	}

	/**
	 * �޸��ˣ������ �� �ܣ�ȥ�����ռ������ݣ����Ч�� �� �ڣ�2002-05-30
	 */

	private UIRefPane getRefWherePart(UIRefPane refpane, String pk_corp,
			String key) {

		String wherepart = refpane.getRefModel().getWherePart();
		if ((wherepart != null) && (!(wherepart.trim().equals("")))
				&& (wherepart.indexOf("pk_corp") >= 0)) {
			// ��λ����
			final int index = wherepart.indexOf("pk_corp");
			final int indexbegin = wherepart.indexOf("'", index + 1);
			int indexend = -1;
			if (indexbegin >= 0) {
				indexend = wherepart.indexOf("'", indexbegin + 1);
			}
			if ((indexbegin >= 0) && (indexend >= 0)) {
				String str1 = wherepart.substring(0, indexbegin + 1);
				String str2 = wherepart.substring(indexend);
				wherepart = str1 + pk_corp + str2;
				refpane.getRefModel().clearData();
				refpane.getRefModel().setWherePart(wherepart);
			}
			// } else {
			// if (key.trim().equals("cbiztype")) {
			// refpane.getRefModel().clearData();
			// refpane.setWhereString("bd_busitype.pk_corp = '" + pk_corp
			// + "'");
			// }
		}

		return refpane;

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-11-4 19:59:42)
	 */
	private ArrayList getSelectedBills() {
		Vector vAll = new Vector();
		PraybillVO[] allvos = null;
		// ȫ��ѡ��ѯ�۵�
		int iPos = 0;
		for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
			if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
				iPos = i;
				iPos = nc.ui.pu.pub.PuTool.getIndexBeforeSort(
						getBillListPanel(), iPos);
				vAll.add(getPraybillVOs()[iPos]);
			}
		}
		allvos = new PraybillVO[vAll.size()];
		vAll.copyInto(allvos);

		// ��ѯδ��������ĵ�����
		try {
			allvos = PrTool.getRefreshedVOs(allvos);
		} catch (BusinessException b) {
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000444")/* "���ִ���:" */
					+ b.getMessage());
		} catch (Exception e) {
			SCMEnv.out(e);
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000445")/* "����δ֪����" */);
		}
		ArrayList aryRslt = new ArrayList();
		if (allvos != null) {
			for (int i = 0; i < allvos.length; i++) {
				aryRslt.add(allvos[i]);
			}
		}
		return aryRslt;
	}

	/**
	 * ���ߣ���ά�� ���ܣ��ӿ�IBillModelSortPrepareListener ��ʵ�ַ��� ������String sItemKey
	 * ITEMKEY ���أ��� ���⣺�� ���ڣ�(2004-03-24 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public int getSortTypeByBillItemKey(String sItemKey) {

		if ("crowno".equals(sItemKey)) {
			return BillItem.DECIMAL;
		} else if ("csourcebillrowno".equals(sItemKey)) {
			return BillItem.DECIMAL;
		} else if ("cancestorbillrowno".equals(sItemKey)) {
			return BillItem.DECIMAL;
		}

		return getBillCardPanel().getBillModel().getItemByKey(sItemKey)
				.getDataType();
	}

	/**
	 * ����ʵ�ָ÷���������ҵ�����ı��⡣
	 * 
	 * @version (00-6-6 13:33:25)
	 * @return java.lang.String
	 */
	public String getTitle() {
		// "ά���빺��"*/;
		String title = m_lanResTool
				.getStrByID("40040101", "UPP40040101-000453")/* "ά���빺��" */;
		if (m_billPanel != null
				&& !getClientEnvironment().getCorporation().getPk_corp()
						.equals("@@@@")) {
			title = m_billPanel.getTitle();
		}
		return title;
	}

	/**
	 * ���ܣ��÷�����ʵ�ֽӿ�ICheckRetVO�ķ��� �ýӿ�Ϊ��������ƣ���ʵ���������˵������ʱ���������ŷ�Ʊ
	 * �벻Ҫ����ɾ�����޸ĸ÷������Ա������ �������� ���أ�nc.vo.pub.AggregatedValueObject Ϊ�빺��VO ���⣺��
	 * ���ڣ�(2001-5-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public nc.vo.pub.AggregatedValueObject getVo() throws Exception {
		PraybillVO vo = null;
		try {
			vo = PraybillHelper.queryPrayVoByHid(getCauditid());
		} catch (java.lang.Exception e) {
			SCMEnv.out(e);
			throw e;
		}
		// û�з��������ĵ���
		if (vo == null) {
			return null;
		}
		return vo;
	}

	/**
	 * ���ߣ��ܺ��� ���ܣ���ô�����룬������ƣ������񣬴���ͺţ�10�����������ƣ�10��������ֵ Ϊ��������� ������BillEditEvent
	 * e ��׽����BillEditEvent�¼� ���أ������������VO ���⣺�� ���ڣ�(2002-3-13 11:39:21)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-08-08 wyf �޸Ķ��ڿմ������ID�Ĵ���
	 */
	private InvVO getVOForFreeItem(BillEditEvent event) {
		final int iRow = event.getRow();

		// ���ڷ��ص�VO
		InvVO tempVO = new InvVO();
		String sBaseId = (String) getBillCardPanel().getBodyValueAt(iRow,
				"cbaseid");
		if (sBaseId == null) {
			return tempVO;
		}

		// ��ô�����룬������ƣ������񣬴���ͺż��������ID
		tempVO.setCinventorycode((String) getBillCardPanel().getBodyValueAt(
				iRow, "cinventorycode"));
		tempVO.setInvname((String) getBillCardPanel().getBodyValueAt(iRow,
				"cinventoryname"));
		tempVO.setInvspec((String) getBillCardPanel().getBodyValueAt(iRow,
				"cinventoryspec"));
		tempVO.setInvtype((String) getBillCardPanel().getBodyValueAt(iRow,
				"cinventorytype"));
		tempVO.setCinventoryid((String) getBillCardPanel().getBodyValueAt(iRow,
				"cbaseid"));

		try {
			FreeVO freeVO = PraybillHelper.queryVOForFreeItem(tempVO
					.getCinventoryid());
			tempVO.setFreeItemVO(freeVO);
		} catch (Exception e) {
			SCMEnv.out(e);
			// return null;
		}

		return tempVO;
	}

	/**
	 * ��������:��ʼ��
	 */
	private void init() {

		// ��ʼ������
		initPara();

		// ��ʾ����
		setLayout(new java.awt.BorderLayout());
		add(getBillCardPanel(), java.awt.BorderLayout.CENTER);

		// ---------����ģ����غ�ĳ�ʼ����������������

		// ��ʼ����ť
		initButton();

		// ���Ӽ���
		initListener();

		getBillCardPanel().setEnabled(false);
		getBillCardPanel().setBodyMenuShow(true);

		getBillCardPanel().setBillData(getBillCardPanel().getBillData());

		// since v55,ѡ��ģʽ����
		PuTool.setLineSelected(getBillCardPanel());
		// �����̬����
		clearCache();
	}

	/**
	 * ��������:���ص���ģ��֮ǰ�ĳ�ʼ��
	 */

	private void initBillBeforeLoad(BillData bd) {

		// ---------����ģ�����ǰ�ĳ�ʼ����������������

		// ��ʼ������
		initRefpane(bd);
		// ��ʼ��ComboBox
		initComboBox(bd);
		// ��ʼ������
		initDecimal(bd);
		// ��ʼ����������
		initiEnabledFalseItems(bd);

	}

	/**
	 * ���ߣ���ӡ�� ���ܣ��Բ��ɱ༭��������� �������� ���أ��� ���⣺�� ���ڣ�(2002-8-26 11:39:21)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-10-23 WYF ����Կ��������������
	 */
	private void initiEnabledFalseItems(BillData bd) {
		// ��ͷ
		// // ҵ������
		// bd.getHeadItem("cbiztype").setEnabled(false);
		//
		// // ����
		// //����˾ ���ɱ༭ Ĭ�ϲ���ʾ
		// v5.02 ��ͨ��ʢ �빺���ϱ����е�����˾���趨Ϊ�ɸ��Ļ�����ѡ�����ṩ��Ӧ������
		if (bd.getBodyItem("pk_reqcorp") != null) {
			bd.getBodyItem("pk_reqcorp").setEnabled(false);
		}
		if (bd.getBodyItem("reqcorpname") != null) {
			bd.getBodyItem("reqcorpname").setEnabled(
					bd.getBodyItem("reqcorpname").isEdit());
		}
		return;
	}

	/**
	 * V51�ع���Ҫ��ƥ��,��ťʵ����������
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-13 ����01:15:06
	 */
	private void createBtnInstances() {
		// //ҵ������
		ButtonObject m_btnBusiTypes = getBtnTree().getButton(
				IButtonConstPr.BTN_BUSINESS_TYPE);
		ButtonObject m_btnBusiTypes2 = getBtnTree().getButton("ҵ������");
		if (m_btnBusiTypes != null) {
			m_btnBusiTypes.setVisible(false);
		}
		if (m_btnBusiTypes2 != null) {
			m_btnBusiTypes2.setVisible(false);
		}

		// ����
		m_btnAdds = getBtnTree().getButton(IButtonConstPr.BTN_ADD);
		// ����
		m_btnSave = getBtnTree().getButton(IButtonConstPr.BTN_SAVE);
		// ����
		m_btnMaintains = getBtnTree().getButton(IButtonConstPr.BTN_BILL);
		m_btnModify = getBtnTree().getButton(IButtonConstPr.BTN_BILL_EDIT);
		m_btnOUT = getBtnTree().getButton("����");
		m_btnModifyList = m_btnModify;
		m_btnCancel = getBtnTree().getButton(IButtonConstPr.BTN_BILL_CANCEL);
		m_btnDiscard = getBtnTree().getButton(IButtonConstPr.BTN_BILL_DELETE);
		m_btnDiscardList = m_btnDiscard;
		m_btnCopy = getBtnTree().getButton(IButtonConstPr.BTN_BILL_COPY);
		// �в���
		m_btnLines = getBtnTree().getButton(IButtonConstPr.BTN_LINE);
		m_btnAddLine = getBtnTree().getButton(IButtonConstPr.BTN_LINE_ADD);
		m_btnDelLine = getBtnTree().getButton(IButtonConstPr.BTN_LINE_DELETE);
		m_btnInsLine = getBtnTree().getButton(IButtonConstPr.BTN_LINE_INSERT);
		m_btnCpyLine = getBtnTree().getButton(IButtonConstPr.BTN_LINE_COPY);
		m_btnPstLine = getBtnTree().getButton(IButtonConstPr.BTN_LINE_PASTE);
		m_btnPstLineTail = getBtnTree().getButton(
				IButtonConstPr.BTN_LINE_PASTE_TAIL);
		m_btnReSortRowNo = getBtnTree().getButton(
				IButtonConstPr.BTN_ADD_NEWROWNO);
		m_btnCardEdit = getBtnTree().getButton(IButtonConstPr.BTN_CARDEDIT);
		// ����
		m_btnAudit = getBtnTree().getButton(IButtonConstPr.BTN_AUDIT);
		m_btnApprove = m_btnAudit;
		m_btnApprove.setTag("APPROVE");
		// ִ��
		m_btnActions = getBtnTree().getButton(IButtonConstPr.BTN_EXECUTE);
		m_btnUnAudit = getBtnTree().getButton(
				IButtonConstPr.BTN_EXECUTE_AUDIT_CANCEL);
		m_btnUnApprove = m_btnUnAudit;
		m_btnUnApprove.setTag("UNAPPROVE");
		m_btnSendAudit = getBtnTree().getButton(
				IButtonConstPr.BTN_EXECUTE_AUDIT);
		m_btnClose = getBtnTree().getButton(IButtonConstPr.BTN_EXECUTE_CLOSE);
		m_btnClose.setTag("CLOSE");
		m_btnOpen = getBtnTree().getButton(IButtonConstPr.BTN_EXECUTE_OPEN);
		m_btnOpen.setTag("OPEN");
		// ��ѯ
		m_btnQuery = getBtnTree().getButton(IButtonConstPr.BTN_QUERY);
		m_btnQueryList = m_btnQuery;
		// ���
		m_btnBrowses = getBtnTree().getButton(IButtonConstPr.BTN_BROWSE);
		m_btnRefresh = getBtnTree()
				.getButton(IButtonConstPr.BTN_BROWSE_REFRESH);
		m_btnRefreshList = m_btnRefresh;
		m_btnFirst = getBtnTree().getButton(IButtonConstPr.BTN_BROWSE_TOP);
		m_btnPrev = getBtnTree().getButton(IButtonConstPr.BTN_BROWSE_PREVIOUS);
		m_btnNext = getBtnTree().getButton(IButtonConstPr.BTN_BROWSE_NEXT);
		m_btnLast = getBtnTree().getButton(IButtonConstPr.BTN_BROWSE_BOTTOM);
		m_btnSelectAll = getBtnTree().getButton(
				IButtonConstPr.BTN_BROWSE_SELECT_ALL);
		m_btnSelectNo = getBtnTree().getButton(
				IButtonConstPr.BTN_BROWSE_SELECT_NONE);
		// ��Ƭ��ʾ/�б���ʾ
		m_btnCard = getBtnTree().getButton(IButtonConstPr.BTN_SWITCH);
		m_btnList = m_btnCard;
		// ��ӡ
		m_btnPrints = getBtnTree().getButton(IButtonConstPr.BTN_PRINT);
		m_btnPrint = getBtnTree().getButton(IButtonConstPr.BTN_PRINT_PRINT);
		m_btnPrintList = m_btnPrint;
		m_btnPrintPreview = getBtnTree().getButton(
				IButtonConstPr.BTN_PRINT_PREVIEW);
		m_btnPrintListPreview = m_btnPrintPreview;
		m_btnCombin = getBtnTree().getButton(IButtonConstPr.BTN_PRINT_DISTINCT);
		// ������ѯ
		m_btnOthersQry = getBtnTree()
				.getButton(IButtonConstPr.BTN_ASSIST_QUERY);
		m_btnLinkBillsBrowse = getBtnTree().getButton(
				IButtonConstPr.BTN_ASSIST_QUERY_RELATED);
		m_btnUsable = getBtnTree().getButton(
				IButtonConstPr.BTN_ASSIST_QUERY_ONHAND);
		m_btnUsableList = m_btnUsable;
		m_btnWorkFlowBrowse = getBtnTree().getButton(
				IButtonConstPr.BTN_ASSIST_QUERY_WORKFLOW);
		m_btnQueryForAuditList = m_btnWorkFlowBrowse;
		m_btnPriceInfo = getBtnTree().getButton(
				IButtonConstPr.BTN_ASSIST_QUERY_PRICE_REPORT);
		// ��������
		m_btnOthersFuncs = getBtnTree().getButton(
				IButtonConstPr.BTN_ASSIST_FUNC);
		boOnHandShowHidden = getBtnTree().getButton(
				IButtonConstPr.BTN_ASSIST_FUNC_ONHAND);
		m_btnDocument = getBtnTree().getButton(
				IButtonConstPr.BTN_ASSIST_FUNC_DOCUMENT);
		m_btnDocumentList = m_btnDocument;
		// ��Ϣ���ĸ�����ť��
		m_btnOthersAuditCenter = getBtnTree().getButton(
				IButtonConstPr.BTN_ASSIST_FUNC);
		m_btnOthersAuditCenter.removeAllChildren();
		// m_btnOthersAuditCenter.addChildButton(m_btnWorkFlowBrowse);
		m_btnOthersAuditCenter.addChildButton(m_btnDocument);
		m_btnOthersAuditCenter.addChildButton(boOnHandShowHidden);

		// ��Ϣ���Ľ���˵�
		m_btnsAuditCenter = new ButtonObject[] { m_btnAudit, m_btnUnAudit,
				m_btnOthersAuditCenter };

		m_btnRevise = getBtnTree().getButton("�޶�");

		// ֧�ֵ����˵��С������кš�����
		m_miReSortRowNo = BillTools.addBodyMenuItem(getBillCardPanel(),
				m_btnReSortRowNo, null);
		m_miCardEdit = BillTools.addBodyMenuItem(getBillCardPanel(),
				m_btnCardEdit, null);
	}

	/**
	 * ��ȡ��ť������Ψһʵ����
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @return
	 * <p>
	 * @author czp
	 * @time 2007-3-13 ����01:16:48
	 */
	private ButtonTree getBtnTree() {
		if (m_btnTree == null) {
			try {
				m_btnTree = new ButtonTree("40040101");
			} catch (BusinessException be) {
				showHintMessage(be.getMessage());
				return null;
			}
		}
		return m_btnTree;
	}

	/**
	 * ��Ƭ��ť��ʾǰ�����⴦��
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-13 ����04:17:42
	 */
	private void dealBtnsBeforeCardShowing() {
		// ���⹦��
		m_btnList.setName(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000464")/*
										 * @res "�б���ʾ"
										 */);
		// �б����û�
		m_btnSelectAll.setEnabled(false);
		m_btnSelectNo.setEnabled(false);
	}

	/**
	 * ���ߣ���ά�� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-9-8 10:18:55) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private void initButton() {
		// V51�ع���Ҫ��ƥ��,��ťʵ��������
		createBtnInstances();

		// ����ҵ�����Ͱ�ť
		PfUtilClient.retBusinessBtn(m_btnTree
				.getButton(IButtonConstPr.BTN_BUSINESS_TYPE), m_sLoginCorpId,
				"20");

		// ҵ�����Ͱ�ť�򹳴���
		// PuTool.initBusiAddBtns(m_btnBusiTypes, m_btnAdds,
		// "20",m_sLoginCorpId);
		// if(m_btnBusiTypes != null && m_btnBusiTypes.getChildCount() > 0){
		// m_bizButton = m_btnBusiTypes.getChildButtonGroup()[0];
		// }

		// ������չ��ť
		addExtendBtns();

		// ���ؿ�Ƭ��ť
		setButtons(m_btnTree.getButtonArray());

		// ��ť״̬�߼�
		setButtonsCard();

		// ��չ��ť��ʼ��
		setExtendBtnsStat(0);
		//
		m_nUIState = 0;
	}

	/**
	 * ���ߣ���ά�� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-9-8 10:18:55) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private void initComboBox(BillData bd) {
		// �빺����
		bd.getHeadItem("ipraytype").setWithIndex(true);
		m_comPrayType = (UIComboBox) bd.getHeadItem("ipraytype").getComponent();
		m_comPrayType.setTranslate(true);
		m_comPrayType.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000454")/*
										 * @res "����ߴ���"
										 */);
		m_comPrayType.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000455")/*
										 * @res "����߲�����"
										 */);
		m_comPrayType.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000456")/*
										 * @res "�ɹ�"
										 */);
		m_comPrayType.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000457")/*
										 * @res "��Э"
										 */);

		// �빺��Դ
		bd.getHeadItem("ipraysource").setWithIndex(true);
		m_comPraySource = (UIComboBox) bd.getHeadItem("ipraysource")
				.getComponent();
		m_comPraySource.setTranslate(true);
		m_comPraySource.addItem("MRP");
		m_comPraySource.addItem("MO");
		m_comPraySource.addItem("SCF");
		m_comPraySource.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000458")/*
										 * @res "���۶���"
										 */);
		m_comPraySource.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000459")/*
										 * @res "��涩����"
										 */);
		m_comPraySource.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000460")/*
										 * @res "�ֹ�¼��"
										 */);
		m_comPraySource.addItem("DRP");
		m_comPraySource.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000461")/*
										 * @res "��������"
										 */);
		m_comPraySource.addItem(m_lanResTool.getStrByID("4004pub",
				"UPP4004pub-000204") /*
										 * @res "������������"
										 */);
		m_comPraySource.addItem(m_lanResTool.getStrByID("40040101",
				"UPT40040101-001114") /*
										 * @res "��������"
										 */);
	}

	/**
	 * ��������:��ʼ��С��λ
	 */
	private void initDecimal(BillData bd) {

		// �汾��:С��λ��ʾһλ
		bd.getHeadItem("nversion").setDecimalDigits(1);

		BillItem item1 = bd.getBodyItem("nsuggestprice");
		item1.setDecimalDigits(nPriceDecimal);

		BillItem item11 = bd.getBodyItem("nmaxprice");
		item11.setDecimalDigits(nPriceDecimal);

		BillItem item2 = bd.getBodyItem("npraynum");
		item2.setDecimalDigits(nMeasDecimal);

		BillItem item3 = bd.getBodyItem("nassistnum");
		item3.setDecimalDigits(nAssistUnitDecimal);

		BillItem item4 = bd.getBodyItem("nexchangerate");
		item4.setDecimalDigits(nExchangeDecimal);

		BillItem item5 = bd.getBodyItem("nmoney");
		item5.setDecimalDigits(nMoneyDecimal);

		BillItem item6 = bd.getBodyItem("npraynum0");
		item6.setDecimalDigits(nMeasDecimal);

	}

	/**
	 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
	 * 
	 * @param ����˵��
	 * @return ����ֵ
	 * @exception �쳣����
	 * @see ��Ҫ�μ�����������
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 */
	private void initi() {

		// ��ʼ������
		initPara();

		// ��ʾ����
		setLayout(new java.awt.BorderLayout());
		add(getBillCardPanel(), java.awt.BorderLayout.CENTER);
		getBillCardPanel().setEnabled(false);

		// ---------����ģ����غ�ĳ�ʼ����������������

		// ����ǧ��λ
		/* getBillCardPanel().setBodyShowThMark(true);ȡ��ǧ��λӲ���� */

		// �ϼ�����ʾ
		getBillCardPanel().getBodyPanel().setTotalRowShow(true);

		// �����к�
		BillRowNo.loadRowNoItem(getBillCardPanel(), "crowno");

		// ���ʻ�
		nc.ui.pu.pub.PuTool.setTranslateRender(getBillCardPanel());
		// �����Զ�����
		// nc.ui.scm.pub.def.DefSetTool.updateBillCardPanelUserDef(getBillCardPanel(),
		// getClientEnvironment().getCorporation()
		// .getPk_corp(), ScmConst.PO_Pray, // ��������
		// "vdef", "vdef");
		// �ػ�ģ��
		getBillCardPanel().setBillData(getBillCardPanel().getBillData());
		// since v55,ѡ��ģʽ����
		PuTool.setLineSelected(getBillCardPanel());
		// �����̬����
		clearCache();
	}

	/**
	 * ���ߣ���ά�� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-9-8 10:18:55) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private void initListComboBox() {

		// �빺����
		getBillListPanel().getBillListData().getHeadItem("ipraytype")
				.setWithIndex(true);
		m_comPrayType1 = (UIComboBox) getBillListPanel().getBillListData()
				.getHeadItem("ipraytype").getComponent();
		m_comPrayType1.setTranslate(true);
		m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000454")/*
										 * @res "����ߴ���"
										 */);
		m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000455")/*
										 * @res "����߲�����"
										 */);
		m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000456")/*
										 * @res "�ɹ�"
										 */);
		m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000457")/*
										 * @res "��Э"
										 */);

		// �빺��Դ
		getBillListPanel().getBillListData().getHeadItem("ipraysource")
				.setWithIndex(true);
		m_comPraySource1 = (UIComboBox) getBillListPanel().getBillListData()
				.getHeadItem("ipraysource").getComponent();
		m_comPraySource1.setTranslate(true);
		m_comPraySource1.addItem("MRP");
		m_comPraySource1.addItem("MO");
		m_comPraySource1.addItem("SFC");
		m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000458")/*
										 * @res "���۶���"
										 */);
		m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000459")/*
										 * @res "��涩����"
										 */);
		m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000460")/*
										 * @res "�ֹ�¼��"
										 */);
		m_comPraySource1.addItem("DRP");
		m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000461")/*
										 * @res "��������"
										 */);
		m_comPraySource1.addItem(m_lanResTool.getStrByID("4004pub",
				"UPP4004pub-000204") /*
										 * @res "������������"
										 */);
	}

	/**
	 * ��������:��ʼ��С��λ
	 */
	private BillListData initListDecimal(BillListData bd) {

		// �汾��:С��λ��ʾһλ
		bd.getHeadItem("nversion").setDecimalDigits(1);

		BillItem item1 = bd.getBodyItem("nsuggestprice");
		item1.setDecimalDigits(nPriceDecimal);

		BillItem item11 = bd.getBodyItem("nmaxprice");
		item11.setDecimalDigits(nPriceDecimal);

		BillItem item2 = bd.getBodyItem("npraynum");
		item2.setDecimalDigits(nMeasDecimal);

		BillItem item3 = bd.getBodyItem("nassistnum");
		item3.setDecimalDigits(nAssistUnitDecimal);

		BillItem item4 = bd.getBodyItem("nexchangerate");
		item4.setDecimalDigits(nExchangeDecimal);

		BillItem item5 = bd.getBodyItem("nmoney");
		item5.setDecimalDigits(nMoneyDecimal);

		// �汾��:С��λ��ʾһλ
		bd.getHeadItem("nversion").setDecimalDigits(1);

		BillItem item6 = bd.getBodyItem("npraynum0");
		item6.setDecimalDigits(nMeasDecimal);

		return bd;
	}

	/**
	 * ��������:��ʼ��
	 */
	public void initListener() {

		// �����������
		getBillCardPanel().getBodyPanel().addTableSortListener();
		getBillCardPanel().getBillModel().setRowSort(true);
		// �����к��������
		getBillCardPanel().getBillModel().setSortPrepareListener(this);
		// ���Ӳֿ����
		((UIRefPane) getBillCardPanel().getBodyItem("cwarehousename")
				.getComponent()).getUIButton().addActionListener(this);

		// ���ӵ��ݱ༭����
		getBillCardPanel().addEditListener(this);
		getBillCardPanel().addBodyMenuListener(this);

		// ���������
		getBillCardPanel().addBodyEditListener2(this);
		// ������
		((UIRefPane) getBillCardPanel().getBodyItem("vfree").getComponent())
				.getUIButton().addActionListener(this);
		// ��ͷ�༭ǰ�¼�����
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
		// �������
		getBillCardPanel().getBillModel().addSortListener(this);
		// ��Ƭ�����������
		getBillCardPanel().getBillModel().addSortRelaObjectListener2(
				new IBillRelaSortListener2Body());
		//
		m_miReSortRowNo.addActionListener(new IMenuItemListener());
		m_miCardEdit.addActionListener(new IMenuItemListener());
		getBillCardPanel().addActionListener(this);
	}

	/**
	 * ��������:��ʼ������
	 */
	private void initPara() {
		try {
			// ��ʼ�����ȣ����������ۣ�
			// int[] iDigits = nc.ui.pu.pub.PuTool.getDigitBatch(m_sUnitCode,
			// new String[] { "BD502", "BD503", "BD501", "BD505" });
			// if (iDigits != null && iDigits.length == 4) {
			// nAssistUnitDecimal = iDigits[0];
			// nExchangeDecimal = iDigits[1];
			// nMeasDecimal = iDigits[2];
			// nPriceDecimal = iDigits[3];
			// }
			// ���ҽ���
			// nMoneyDecimal =
			// nc.ui.rc.pub.CPurchseMethods.getCurrDecimal(getCorpId())[0];

			// ����Ƿ�����
			// m_bICStartUp = nc.ui.sm.user.UserPowerUI.isEnabled(m_sUnitCode,
			// "IC");

			ServcallVO[] scDisc = new ServcallVO[2];
			// ��ʼ�����ȣ����������ۣ�
			scDisc[0] = new ServcallVO();
			scDisc[0].setBeanName("nc.itf.pu.pub.IPub");
			scDisc[0].setMethodName("getDigitBatch");
			scDisc[0].setParameter(new Object[] { m_sLoginCorpId,
					new String[] { "BD502", "BD503", "BD501", "BD505" } });
			scDisc[0].setParameterTypes(new Class[] { String.class,
					String[].class });

			scDisc[1] = new ServcallVO();
			scDisc[1].setBeanName("nc.itf.rc.receive.IArriveorder");
			scDisc[1].setMethodName("getCurrDecimal");
			scDisc[1].setParameter(new Object[] { m_sLoginCorpId });
			scDisc[1].setParameterTypes(new Class[] { String.class });

			// //�Զ�����Զ�̵���������
			// ServcallVO[] scdsDef =
			// nc.ui.scm.pub.def.DefSetTool.getTwoSCDs(m_sUnitCode);
			// scDisc[3] = scdsDef[0];
			// scDisc[4] = scdsDef[1];

			// ��̨һ�ε���
			Object[] oParaValue = nc.ui.scm.service.LocalCallService
					.callService(scDisc);
			if (oParaValue != null && oParaValue.length == scDisc.length) {
				// ���������ݾ���
				int[] iDigits = (int[]) oParaValue[0];
				if (iDigits != null && iDigits.length == 4) {
					nAssistUnitDecimal = iDigits[0];
					nExchangeDecimal = iDigits[1];
					nMeasDecimal = iDigits[2];
					nPriceDecimal = iDigits[3];
				}
				// ���ҽ���
				nMoneyDecimal = ((Integer) oParaValue[1]).intValue();

				// ����Ƿ�����
				// m_bICStartUp = ((Boolean) oParaValue[2]).booleanValue();

				// �Զ�����Ԥ����
				// nc.ui.scm.pub.def.DefSetTool.setTwoOBJs(new Object[] {
				// oParaValue[3], oParaValue[4] });

				// �����Ƿ������޸�ɾ�����˵ĵ��ݵĲ���
				String sRet = SysInitBO_Client.getParaString(m_sLoginCorpId,
						"PO060");

				isAllowedModifyByOther = (sRet == null || sRet.equals("N")) ? false
						: true;
			}
		} catch (Exception e) {
			reportException(e);
		}
		/*
		 * �����Ż������δʹ�õ�
		 *//** ICģ�������жϲ�������Ľӿڷ�ʽ */
		/*
		 * ICreateCorpQueryService tt = (ICreateCorpQueryService)
		 * NCLocator.getInstance().lookup(
		 * ICreateCorpQueryService.class.getName()); boolean bEnable = false;
		 * try { tt.isEnabled("1001", "IC"); } catch (Exception e) {
		 * Logger.debug(e.getMessage()); } Logger.debug(bEnable + "");
		 */

	}

	// ��ջ���
	private void clearCache() {
		h_ProduceInfo = new HashMap<String, String[]>();
		h_InvSource = new HashMap();
		al_BizInfo = null;
		m_hBiztype = new HashMap();
	}

	/**
	 * ���ߣ���ά�� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-9-8 10:18:55) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private void initRefpane(BillData bd) {
		UIRefPane refpane = null;

		// ��������������������ͷ������������������

		// ҵ��Ա(�ɹ����ŵ�)
		refpane = (UIRefPane) bd.getBodyItem("cemployeename").getComponent();
		refpane.setRefModel(new PurPsnRefModel(m_sLoginCorpId, bd.getHeadItem(
				"cdeptid").getValue()));

		// ��Ŀ
		refpane = (UIRefPane) bd.getBodyItem("cprojectname").getComponent();
		refpane
				.setWhereString(" bd_jobmngfil.pk_corp = '"
						+ m_sLoginCorpId
						+ "' and bd_jobmngfil.pk_jobbasfil = bd_jobbasfil.pk_jobbasfil and upper(isnull(bd_jobmngfil.sealflag,'N')) = 'N'");
		refpane.setCacheEnabled(false);

		// ��ע
		Object obj = bd.getHeadItem("vmemo").getComponent();
		if (obj instanceof UIRefPane) {
			refpane = (UIRefPane) obj;
			refpane.setReturnCode(false);
			refpane.setAutoCheck(false);
		}

		// ���������������������壭����������������

		// �������
		refpane = (UIRefPane) bd.getBodyItem("cinventorycode").getComponent();
		String sWhere = " discountflag = 'N' and bd_invmandoc.sealflag = 'N' and bd_invmandoc.pk_corp = '"
				+ m_sLoginCorpId
				+ "' and bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc";

		refpane.setWhereString(sWhere);
		refpane.setTreeGridNodeMultiSelected(true);
		refpane.setMultiSelectedEnabled(true);
		refpane.setCacheEnabled(false);
		invrefpane = refpane;

		// �������
		refpane = (UIRefPane) bd.getBodyItem("cvendorname").getComponent();
		sWhere = " bd_cumandoc.pk_corp='"
				+ m_sLoginCorpId
				+ "' and (bd_cumandoc.custflag='1' or bd_cumandoc.custflag='3') and bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc and upper(frozenflag) = 'N'";
		refpane.setWhereString(sWhere);
		refpane.setCacheEnabled(false);

		// ����������
		refpane = (UIRefPane) bd.getBodyItem("nassistnum").getComponent();
		UITextField nAssistNumUI = refpane.getUITextField();
		nAssistNumUI.setDelStr("-");

		// ����������
		refpane = ((UIRefPane) (bd.getBodyItem("cassistunitname")
				.getComponent()));
		refpane.setIsCustomDefined(true);
		refpane.setRefModel(new OtherRefModel("��������λ"));
		refpane.setReturnCode(false);
		refpane.setRefInputType(1);
		refpane.setCacheEnabled(false);

		// ������
		refpane = (UIRefPane) bd.getBodyItem("nexchangerate").getComponent();
		UITextField nExchangeRateUI = refpane.getUITextField();
		nExchangeRateUI.setDelStr("-");

		// ���κŲ���
		if (bd.getBodyItem("vproducenum").isShow()) {
			LotNumbRefPane lotRef = new LotNumbRefPane();
			lotRef.setMaxLength(bd.getBodyItem("vproducenum").getLength());
			lotRef.setIsCustomDefined(true);
			lotRef.setMaxLength(bd.getBodyItem("vproducenum").getLength());
			bd.getBodyItem("vproducenum").setComponent((JComponent) lotRef);
		}
		// ���������
		m_freeItem = new FreeItemRefPane();
		m_freeItem.setMaxLength(bd.getBodyItem("vfree").getLength());
		bd.getBodyItem("vfree").setComponent(m_freeItem);

		// ����۸�
		refpane = (UIRefPane) bd.getBodyItem("nsuggestprice").getComponent();
		UITextField nSuggestPriceUI = refpane.getUITextField();
		nSuggestPriceUI.setMaxLength(16);
		nSuggestPriceUI.setDelStr("-");

		// �빺����
		refpane = (UIRefPane) bd.getBodyItem("npraynum").getComponent();
		UITextField nPrayNumUI = refpane.getUITextField();
		nPrayNumUI.setMaxLength(16);
		nPrayNumUI.setDelStr("-");

		// ���
		refpane = (UIRefPane) bd.getBodyItem("nmoney").getComponent();
		UITextField nMoneyUI = refpane.getUITextField();
		nMoneyUI.setMaxLength(16);
		nMoneyUI.setDelStr("-");

		// �ֿ���
		refpane = (UIRefPane) bd.getBodyItem("cwarehousename").getComponent();
		WarehouseRefModel warehouseModel = new WarehouseRefModel();
		refpane.setRefModel(warehouseModel);
		refpane.getRefModel().addWherePart(" and upper(gubflag) = 'N' ");
		refpane.setCacheEnabled(false);

		// ���屸ע��������ͬ�ڱ�ͷ
		refpane = (UIRefPane) bd.getBodyItem("vmemo").getComponent();
		// refpane.setTable(bd.getBillTable());
		refpane.getRefModel().setRefCodeField(
				refpane.getRefModel().getRefNameField());
		refpane.getRefModel().setBlurFields(
				new String[] { refpane.getRefModel().getRefNameField() });
		refpane.setAutoCheck(false);

		// ҵ������
		refpane = (UIRefPane) bd.getBodyItem("cbiztypename").getComponent();
		refpane.getRefModel().setWherePart(
				"( pk_corp = '@@@@')  "
						+ "and (encapsulate = 'N' or encapsulate is null) ");
		refpane
				.getRefModel()
				.addWherePart(
						"and pk_busitype IN (SELECT pk_businesstype FROM pub_billbusiness WHERE pub_billbusiness.dr = 0 "
								+ " and pk_billtype ='"
								+ ScmConst.PO_Order
								+ "')");
		refpane.setReturnCode(false);
		refpane.setCacheEnabled(false);

	}

	/**
	 * �Ƿ������
	 */
	private boolean isCanAudit(PraybillVO vo) {
		if (vo == null)
			return false;
		if (vo.getParentVO() == null)
			return false;
		Integer status = null;
		status = ((PraybillHeaderVO) vo.getParentVO()).getIbillstatus();
		if (status == null)
			return false;
		if (status.intValue() == 0)
			return true;
		return false;
	}

	/**
	 * �Ƿ�ɹر�
	 */
	private boolean isCanClose(PraybillVO vo) {
		if (vo == null)
			return false;
		if (vo.getParentVO() == null)
			return false;
		Integer status = null;
		status = ((PraybillHeaderVO) vo.getParentVO()).getIbillstatus();
		if (status == null)
			return false;
		if (status.intValue() == 3)
			return true;
		return false;
	}

	/**
	 * �Ƿ�ɴ�
	 */
	private boolean isCanOpen(PraybillVO vo) {
		if (vo == null)
			return false;
		if (vo.getParentVO() == null)
			return false;
		Integer status = null;
		status = ((PraybillHeaderVO) vo.getParentVO()).getIbillstatus();
		if (status == null)
			return false;
		// if (status.intValue() == 3 && status.intValue() == 1)
		if (status.intValue() == 1)
			return true;
		return false;
	}

	/**
	 * �Ƿ������
	 */
	private boolean isCanUnAudit(PraybillVO vo) {
		if (vo == null)
			return false;
		if (vo.getParentVO() == null)
			return false;
		Integer status = null;
		PraybillItemVO[] items = (PraybillItemVO[]) vo.getChildrenVO();
		//
		if (items == null || items.length <= 0)
			return false;
		/* �ж�������,�������� */
		UFDouble ufdNaccumulatenum = null;
		for (int i = 0; i < items.length; i++) {
			ufdNaccumulatenum = nc.vo.scm.pu.PuPubVO
					.getUFDouble_NullAsZero(items[i].getNaccumulatenum());
			if (!ufdNaccumulatenum.equals(nc.vo.scm.pu.VariableConst.ZERO))
				return false;

			if (PuPubVO.getUFDouble_NullAsZero(items[i].getNgenct()).compareTo(
					VariableConst.ZERO) != 0)// �Ѿ����ɲɹ���ͬ��������
				return false;
			if (PuPubVO.getUFDouble_NullAsZero(items[i].getNpriceauditbill())
					.compareTo(VariableConst.ZERO) != 0)// �Ѿ����ɼ۸���������������
				return false;
		}
		status = ((PraybillHeaderVO) vo.getParentVO()).getIbillstatus();
		if (status == null)
			return false;
		if (status.intValue() == 3/** ����ͨ�� */
		|| status.intValue() == 2/** ���������� */
		)
			return true;
		return false;
	}

	/**
	 * ��������:���˫���¼���Ӧ
	 */
	public void mouse_doubleclick(BillMouseEnent event) {
		if (event.getPos() == BillItem.HEAD) {
			onCard();
			// /* ���û�е����壬����Ϊ������ֱ�ӷ��� */
			// PraybillItemVO[] items = (PraybillItemVO[]) getBillListPanel()
			// .getBodyBillModel().getBodyValueVOs(
			// PraybillItemVO.class.getName());
			// if (items == null || items.length <= 0)
			// return;
			// /* ֧������ */
			// m_nPresentRecord = event.getRow();
			// m_nPresentRecord = nc.ui.pu.pub.PuTool.getIndexBeforeSort(
			// getBillListPanel(), m_nPresentRecord);
			// if (m_nPresentRecord >= 0) {
			// /* ���ص��ݿ�Ƭ�ؼ� */
			// getBillListPanel().setVisible(false);
			// getBillCardPanel().setVisible(true);
			// m_nUIState = 0;
			// /* ���õ���VO����Ƭ */
			// setVoToBillCard(m_nPresentRecord, null);
			// /* ���ð�ť�߼� */
			// setButtonsCard();
			// }
		}
	}

	/**
	 * ��������:����
	 */
	private void onAppend() {
		showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000050")/* @res"��������..." */);
		m_bAdd = true;
		m_bEdit = true;

		getBillCardPanel().addNew();
		getBillCardPanel().setEnabled(true);
		setPartNoEditable(getBillCardPanel());
		UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel().getHeadItem(
				"dpraydate").getComponent();
		nRefPanel1.setValue(getClientEnvironment().getDate().toString());

		// UIRefPane nRefPanel2 = (UIRefPane)
		// getBillCardPanel().getHeadItem("cbiztype").getComponent();
		// nRefPanel2.setPK(m_bizButton.getTag());
		// nRefPanel2.setEnabled(false);

		UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel().getTailItem(
				"cauditpsn").getComponent();
		nRefPanel3.setEnabled(false);

		// ���ò���Ա
		String strUserId = getClientEnvironment().getUser().getPrimaryKey();
		UIRefPane nRefPanel6 = (UIRefPane) getBillCardPanel().getTailItem(
				"coperator").getComponent();
		nRefPanel6.setPK(strUserId);
		nRefPanel6.setEnabled(false);

		// ȡ����Ա��Ӧҵ��Ա�������빺��(�빺����ֵʱ������)
		if (PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem(
				"cpraypsn").getValueObject()) == null) {
			PsndocVO voPsnDoc = PuTool.getPsnByUser(strUserId,
					getCorpPrimaryKey());
			if (voPsnDoc != null) {
				UIRefPane refPanePrayPsn = (UIRefPane) getBillCardPanel()
						.getHeadItem("cpraypsn").getComponent();
				refPanePrayPsn.setPK(voPsnDoc.getPrimaryKey());
				// ���빺�˴����빺����(����빺������ֵʱ�Ŵ���)
				if (PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
						.getHeadItem("cdeptid").getValueObject()) == null) {
					afterEditWhenHeadPsn(getBillCardPanel(), null, null);
				}
			}
		}
		m_comPraySource.setSelectedIndex(5);
		m_comPraySource.setEnabled(false);
		m_comPrayType.setSelectedIndex(2);
		m_bCancel = false;
		setButtonsCardModify();
		m_nUIState = 0;
		updateButtonsAll();

		/* �����Ҽ��˵��밴ť�顰�в�����Ȩ����ͬ */
		setPopMenuBtnsEnable();

		// �������޸�
		onAppendLine(getBillCardPanel(), this);

		// �ù�굽��ͷ��һ���ɱ༭��Ŀ
		getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);

		// �Ƿ�ֱ��
		if (getBillCardPanel().getHeadItem("isdirecttransit") != null) {
			getBillCardPanel().getHeadItem("isdirecttransit").setEnabled(false);
		}
		// ���ɺ�ͬ����
		if (getBillCardPanel().getBodyItem("ngenct") != null) {
			getBillCardPanel().getBodyItem("ngenct").setEnabled(false);
		}

		//
		showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000533")/*
										 * @res "���ӡ��༭����"
										 */);
	}

	/**
	 * ��������:����
	 */
	private static void onAppendLine(BillCardPanel bcp, ToftPanel uiPanel) {
		uiPanel.showHintMessage("");
		bcp.addLine();
		bcp.setEnabled(true);
		/* ������Ŀ�Զ�Э�� */
		nc.ui.pu.pub.PuTool.setBodyProjectByHeadProject(bcp, "cprojectidhead",
				"cprojectid", "cprojectname",
				nc.vo.scm.pu.PuBillLineOprType.ADD);
		/* �����к� */
		BillRowNo.addLineRowNo(bcp, BillTypeConst.PO_PRAY, "crowno");
		// ���в������Զ����뵱ǰ��¼��˾
		bcp.setBodyValueAt(PoPublicUIClass.getLoginPk_corp(),
				bcp.getRowCount() - 1, "pk_reqcorp");
		bcp.execBodyFormula(bcp.getRowCount() - 1, "reqcorpname");
		bcp.setBodyValueAt(PoPublicUIClass.getLoginPk_corp(),
				bcp.getRowCount() - 1, "pk_purcorp");
		bcp.execBodyFormula(bcp.getRowCount() - 1, "purcorpname");

		setPartNoEditable(bcp);
		//
		uiPanel.showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000535")/*
										 * @res "��������һ��"
										 */);
	}

	/**
	 * ��ȡ�����������VO
	 * 
	 * @param voAudit
	 *            �����µ�VO
	 * @return
	 * @since v50
	 * @author czp
	 */
	private Integer setLastestInfosToVoAfterAuditted(PraybillVO voAudit)
			throws Exception {

		/* �����ɹ���ˢ�� */
		ArrayList arrRet = PraybillHelper.queryPrayForSaveAudit(voAudit
				.getParentVO().getPrimaryKey());
		((PraybillHeaderVO) voAudit.getParentVO())
				.setDauditdate((UFDate) arrRet.get(0));
		((PraybillHeaderVO) voAudit.getParentVO()).setCauditpsn((String) arrRet
				.get(1));
		((PraybillHeaderVO) voAudit.getParentVO())
				.setIbillstatus((Integer) arrRet.get(2));
		((PraybillHeaderVO) voAudit.getParentVO())
				.setTs((String) arrRet.get(3));
		((PraybillHeaderVO) voAudit.getParentVO()).setTmaketime((String) arrRet
				.get(4));
		((PraybillHeaderVO) voAudit.getParentVO())
				.setTlastmaketime((String) arrRet.get(5));
		((PraybillHeaderVO) voAudit.getParentVO())
				.setTaudittime((String) arrRet.get(6));
		// ֧�������޸�������ͬ������ʱ���
		ArrayList ts = (ArrayList) arrRet.get(7);
		HashMap<String, String> mapBidTs = new HashMap<String, String>();
		if (ts != null && ts.size() > 0) {
			ArrayList<String> listBidTs = null;
			for (int i = 0; i < ts.size(); i++) {
				listBidTs = (ArrayList) ts.get(i);
				if (listBidTs == null || listBidTs.size() < 2) {
					continue;
				}
				mapBidTs.put(listBidTs.get(1), listBidTs.get(0));
			}
		}
		PraybillItemVO[] items = voAudit.getBodyVO();
		int itemsLength = items.length;
		for (int i = 0; i < itemsLength; i++) {
			items[i].setTs((String) mapBidTs.get(items[i].getCpraybill_bid()));
		}
		voAudit.setChildrenVO(items);

		Integer iBillStatus = (Integer) arrRet.get(2);

		return iBillStatus;
	}

	/**
	 * �������ڣ� 2005-9-28 ���������� ��ð汾�Ŵ���1�ĵ��ݺ� ����ʱ���� ����
	 */
	private String getVersionOfCantUnaudit(PraybillVO[] vos) {
		String versions = "";

		for (int i = 0, len = vos.length; i < len; i++) {
			if (vos[i].getHeadVO().getNversion().intValue() > 1)
				versions += vos[i].getHeadVO().getVpraycode() + ", ";
		}

		return versions;
	}

	/**
	 * ��������:ȡ������
	 */
	protected void onUnAuditList() {
		int iCnt = 0;
		showHintMessage(NCLangRes.getInstance().getStrByID("40040102",
				"UPP40040102-000018")/* @res"�������󵥾�..." */);
		Integer nSelected[] = null;
		Vector v = new Vector();
		Vector vv = new Vector();
		int nRow = getBillListPanel().getHeadBillModel().getRowCount();
		for (int i = 0; i < nRow; i++) {
			int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
			if (nStatus == BillModel.SELECTED)
				v.addElement(new Integer(i));
			else
				vv.addElement(new Integer(i));
		}
		nSelected = new Integer[v.size()];
		v.copyInto(nSelected);
		if (nSelected == null || nSelected.length == 0) {
			MessageDialog.showErrorDlg(this,
					NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000019")/* @res"�빺��ȡ������" */,
					NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000003")/* @res"δѡ���빺����" */);
			return;
		}
		long tTime = System.currentTimeMillis();
		Vector vTemp = new Vector();
		// ���������˼��������ڹ�ϣ������ʧ��ʱ�õ�
		HashMap<String, ArrayList<Object>> mapAuditInfo = new HashMap<String, ArrayList<Object>>();
		ArrayList<Object> listAuditInfo = null;
		PraybillHeaderVO headVO = null;
		ArrayList<PraybillVO> alReQueryVO = new ArrayList<PraybillVO>();
		ArrayList<PraybillVO> alProcessVO = new ArrayList<PraybillVO>();
		for (int i = 0; i < nSelected.length; i++) {
			// ������
			PraybillVO vo = m_VOs[nSelected[i].intValue()];
			if (vo.getBodyVO() == null || vo.getBodyVO().length == 0) {
				alReQueryVO.add(vo);
			} else {
				alProcessVO.add(vo);
			}
		}
		try {
			PraybillVO[] reQueryVO = null;
			// �������û�б���,�����²�ѯ����
			if (alReQueryVO.size() > 0) {
				reQueryVO = PrTool.getRefreshedVOs(alReQueryVO
						.toArray(new PraybillVO[alReQueryVO.size()]));
				for (PraybillVO curvo : reQueryVO) {
					alProcessVO.add(curvo);
				}
			}
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this,
					NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000020")/* @res"ȡ���빺������" */,
					NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000020")/* @res"ȡ���빺������" */);
			SCMEnv.out(e);
			return;
		}
		for (int i = 0; i < alProcessVO.size(); i++) {
			// ������
			PraybillVO vo = alProcessVO.get(i);

			// ����ʧ��ʱ�õ������������˼��������ڹ�ϣ��
			headVO = vo.getHeadVO();
			if (!isCanUnAudit(vo)) {
				showErrorMessage(headVO.getVpraycode()
						+ NCLangRes.getInstance().getStrByID("40040101",
								"UPT40040101-001124")
				/* @res"�Ѿ����ɲɹ�������ɹ���ͬ��۸�������,��������" */);
				return;
			}

			if (PuPubVO.getString_TrimZeroLenAsNull(headVO.getCauditpsn()) != null) {
				listAuditInfo = new ArrayList<Object>();
				listAuditInfo.add(headVO.getCauditpsn());
				listAuditInfo.add(headVO.getDauditdate());
				mapAuditInfo.put(headVO.getPrimaryKey(), listAuditInfo);
			}
			//
			vo.getHeadVO().setAttributeValue("cauditpsnold",
					vo.getHeadVO().getCauditpsn());// Ϊ�ж��Ƿ������������˵ĵ���
			vo.getHeadVO().setCuserid(
					getClientEnvironment().getUser().getPrimaryKey());
			vo.getHeadVO().setCauditpsn(
					getClientEnvironment().getUser().getPrimaryKey());
			vTemp.addElement(vo);
		}
		PraybillVO VOs[] = new PraybillVO[vTemp.size()];
		vTemp.copyInto(VOs);
		//
		try {
			// �������빺��������Ա
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			for (int i = 0; i < VOs.length; i++) {
				VOs[i].setCurrOperator(strOpr);
				VOs[i].getHeadVO().setCuserid(strOpr);
				VOs[i].getHeadVO().setTaudittime(null);

				// Ϊ�ж��Ƿ������������˵ĵ���
				VOs[i].getHeadVO().setCoperatoridnow(strOpr);
				VOs[i].getHeadVO().setAttributeValue("cauditpsnold",
						VOs[i].getHeadVO().getCauditpsnold());
			}
			// ������������ʱ�ż��ر���
			VOs = PrTool.getRefreshedVOs(VOs);

			PfUtilClient.processBatch(this, "UNAPPROVE", "20",
					getClientEnvironment().getDate().toString(), VOs, null);

			//
			if (!PfUtilClient.isSuccess()) {
				// ����ʧ�ܣ����������˼���������
				rollbackAuditInfos(VOs, mapAuditInfo);
				return;
			}
			iCnt = VOs.length;
		} catch (java.sql.SQLException e) {
			// ����ʧ�ܣ����������˼���������
			rollbackAuditInfos(VOs, mapAuditInfo);
			//
			MessageDialog.showErrorDlg(this,
					NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000020")/* @res"ȡ���빺������" */,
					NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000412")/* @res"SQL������" */);
			SCMEnv.out(e);
			return;
		} catch (ArrayIndexOutOfBoundsException e) {
			// ����ʧ�ܣ����������˼���������
			rollbackAuditInfos(VOs, mapAuditInfo);
			//
			MessageDialog.showErrorDlg(this,
					NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000020")/* @res"ȡ���빺������" */,
					NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000426")/* @res"����Խ�����" */);
			SCMEnv.out(e);
			return;
		} catch (NullPointerException e) {
			// ����ʧ�ܣ����������˼���������
			rollbackAuditInfos(VOs, mapAuditInfo);
			//
			MessageDialog.showErrorDlg(this,
					NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000020")/* @res"ȡ���빺������" */,
					NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000427")/* @res"��ָ�����" */);
			SCMEnv.out(e);
			return;
		} catch (nc.vo.pub.BusinessException e) {
			// ����ʧ�ܣ����������˼���������
			rollbackAuditInfos(VOs, mapAuditInfo);
			//
			MessageDialog.showErrorDlg(this,
					NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000020")/* @res"ȡ���빺������" */, e
							.getMessage());
			SCMEnv.out(e);
			return;
		} catch (Exception e) {
			// ����ʧ�ܣ����������˼���������
			rollbackAuditInfos(VOs, mapAuditInfo);
			//
			MessageDialog.showErrorDlg(this,
					NCLangRes.getInstance().getStrByID("40040102",
							"UPP40040102-000020")/* @res"ȡ���빺������" */, e
							.getMessage());
			SCMEnv.out(e);
			return;
		}
		Operlog operlog = new Operlog();
		for (int i = 0; i < VOs.length; i++) {
			VOs[i].getOperatelogVO().setOpratorname(
					nc.ui.pub.ClientEnvironment.getInstance().getUser()
							.getUserName());
			VOs[i].getOperatelogVO().setCompanyname(
					nc.ui.pub.ClientEnvironment.getInstance().getCorporation()
							.getUnitname());
			VOs[i].getOperatelogVO().setOperatorId(
					nc.ui.pub.ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
			operlog.insertBusinessExceptionlog(VOs[i], "����", "����",
					nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
					nc.vo.scm.pu.BillTypeConst.PO_PRAY, nc.ui.sm.cmenu.Desktop
							.getApplet().getParameter("USER_IP"));
		}
		tTime = System.currentTimeMillis() - tTime;
		SCMEnv.out("ȡ������ʱ�䣺" + tTime + " ms!");
		showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH011")/* @res"����ɹ�" */);
		//
		if (m_butnQuery) {
			onListRefresh();
		} else {
			// �������
			getBillListPanel().getHeadBillModel().clearBodyData();
			getBillListPanel().getBodyBillModel().clearBodyData();
			m_VOs = null;
			getBillListPanel().updateUI();
		}
	}

	/*
	 * ����ϣ��������ˡ������������������ˡ���������
	 */
	private void rollbackAuditInfos(PraybillVO[] VOs,
			HashMap<String, ArrayList<Object>> mapAuditInfo) {

		if (VOs == null || mapAuditInfo == null || mapAuditInfo.size() == 0) {
			return;
		}

		PraybillHeaderVO headVO = null;
		ArrayList<Object> listAuditInfo = null;

		// ���������˼���������
		if (mapAuditInfo.size() > 0) {
			for (int i = 0; i < VOs.length; i++) {
				headVO = VOs[i].getHeadVO();
				if (!mapAuditInfo.containsKey(headVO.getPrimaryKey())) {
					headVO.setCauditpsn(null);
					headVO.setDauditdate(null);
					continue;
				}
				listAuditInfo = (ArrayList<Object>) mapAuditInfo.get(headVO
						.getPrimaryKey());
				headVO.setCauditpsn((String) listAuditInfo.get(0));
				headVO.setDauditdate((UFDate) listAuditInfo.get(1));
			}
		}
	}

	/**
	 * ��������:����--�б�
	 */
	public void onAuditList() {
		int iCnt = 0;
		showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
				"UPPSCMCommon-000403")/*
										 * @res "������������..."
										 */);
		Integer nSelected[] = null;
		Vector v = new Vector();
		Vector vv = new Vector();
		int nRow = getBillListPanel().getHeadBillModel().getRowCount();
		for (int i = 0; i < nRow; i++) {
			int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
			if (nStatus == BillModel.SELECTED)
				v.addElement(new Integer(i));
			else
				vv.addElement(new Integer(i));
		}
		nSelected = new Integer[v.size()];
		v.copyInto(nSelected);

		if (nSelected == null || nSelected.length == 0) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000002")/*
																	 * @res
																	 * "�빺������"
																	 */, NCLangRes.getInstance().getStrByID("40040102",
					"UPP40040102-000003")/*
											 * @res "δѡ���빺����"
											 */);
			return;
		}
		long tTime = System.currentTimeMillis();
		Vector vTemp = new Vector();
		for (int i = 0; i < nSelected.length; i++) {
			// ������
			PraybillVO vo = m_VOs[nSelected[i].intValue()];
			vo.getHeadVO().setCuserid(
					getClientEnvironment().getUser().getPrimaryKey());
			vTemp.addElement(vo);
		}

		PraybillVO VOs[] = new PraybillVO[vTemp.size()];
		vTemp.copyInto(VOs);

		/*
		 * �޸ģ�zhongwei v31-sp1-���� �������ڲ���С���빺����
		 */
		Vector v_ids = new Vector();
		// ���������˼��������ڹ�ϣ������ʧ��ʱ�õ�
		HashMap<String, ArrayList<Object>> mapAuditInfo = new HashMap<String, ArrayList<Object>>();
		ArrayList<Object> listAuditInfo = null;
		PraybillHeaderVO headVO = null;
		for (int i = 0; i < VOs.length; i++) {
			headVO = VOs[i].getHeadVO();

			if (headVO.getDpraydate().toString().compareTo(
					getClientEnvironment().getDate().toString()) > 0)
				// ���治����Ҫ����빺�����
				v_ids.add(headVO.getVpraycode());

			// ����ʧ��ʱ�õ������������˼��������ڹ�ϣ��
			if (PuPubVO.getString_TrimZeroLenAsNull(headVO.getCauditpsn()) != null) {
				listAuditInfo = new ArrayList<Object>();
				listAuditInfo.add(headVO.getCauditpsn());
				listAuditInfo.add(headVO.getDauditdate());
				mapAuditInfo.put(headVO.getPrimaryKey(), listAuditInfo);
			}
			headVO.setCauditpsn(getClientEnvironment().getUser()
					.getPrimaryKey());
			headVO.setDauditdate(getClientEnvironment().getDate());
		}

		/*
		 * ��ʾ������Ϣ������ V5.02 �ع�
		 */
		ClientLink cl = new ClientLink(getClientEnvironment());
		String strErr = PuTool.getAuditLessThanMakeMsg(VOs, "dpraydate",
				"vpraycode", cl.getLogonDate(), ScmConst.PO_Pray);
		if (strErr != null && strErr.trim().length() > 0) {
			showErrorMessage(strErr);

			// ���������˼���������
			rollbackAuditInfos(VOs, mapAuditInfo);

			return;
		}

		try {
			// �������빺��������Ա
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			for (int i = 0; i < VOs.length; i++) {
				VOs[i].setCurrOperator(strOpr);
				VOs[i].getHeadVO().setCuserid(strOpr);
				VOs[i].getHeadVO().setTaudittime(
						(new UFDateTime(new Date())).toString());
			}
			// ������������ʱ�ż��ر���
			VOs = PrTool.getRefreshedVOs(VOs);
			PfUtilClient.processBatchFlow(this, "APPROVE", "20",
					getClientEnvironment().getDate().toString(), VOs, null);

			if (!PfUtilClient.isSuccess()) {
				// ���������˼���������
				rollbackAuditInfos(VOs, mapAuditInfo);
				// �������빺��
				return;
			}
			iCnt = VOs.length;
		} catch (java.sql.SQLException e) {
			// ���������˼���������
			rollbackAuditInfos(VOs, mapAuditInfo);
			//
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000002")/*
																	 * @res
																	 * "�빺������"
																	 */, NCLangRes.getInstance().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000412")/*
											 * @res "SQL������"
											 */);
			SCMEnv.out(e);
			return;
		} catch (ArrayIndexOutOfBoundsException e) {
			// ���������˼���������
			rollbackAuditInfos(VOs, mapAuditInfo);
			//
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000002")/*
																	 * @res
																	 * "�빺������"
																	 */, NCLangRes.getInstance().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000426")/*
											 * @res "����Խ�����"
											 */);
			SCMEnv.out(e);
			return;
		} catch (NullPointerException e) {
			// ���������˼���������
			rollbackAuditInfos(VOs, mapAuditInfo);
			//
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000002")/*
																	 * @res
																	 * "�빺������"
																	 */, NCLangRes.getInstance().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000427")/*
											 * @res "��ָ�����"
											 */);
			SCMEnv.out(e);
			return;
		} catch (nc.vo.pub.BusinessException e) {
			// ���������˼���������
			rollbackAuditInfos(VOs, mapAuditInfo);
			//
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000002")/*
																	 * @res
																	 * "�빺������"
																	 */, e.getMessage());
			SCMEnv.out(e);
			return;
		} catch (Exception e) {
			// ���������˼���������
			rollbackAuditInfos(VOs, mapAuditInfo);
			//
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance()
					.getStrByID("40040102", "UPP40040102-000002")/*
																	 * @res
																	 * "�빺������"
																	 */, e.getMessage());
			SCMEnv.out(e);
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		SCMEnv.out("�빺������ʱ�䣺" + tTime + " ms!");
		Operlog operlog = new Operlog();
		for (int i = 0; i < VOs.length; i++) {
			VOs[i].getOperatelogVO().setOpratorname(
					nc.ui.pub.ClientEnvironment.getInstance().getUser()
							.getUserName());
			VOs[i].getOperatelogVO().setCompanyname(
					nc.ui.pub.ClientEnvironment.getInstance().getCorporation()
							.getUnitname());
			VOs[i].getOperatelogVO().setOperatorId(
					nc.ui.pub.ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
			operlog.insertBusinessExceptionlog(VOs[i], "����", "����",
					nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
					nc.vo.scm.pu.BillTypeConst.PO_PRAY, nc.ui.sm.cmenu.Desktop
							.getApplet().getParameter("USER_IP"));
		}
		showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
				"UPPSCMCommon-000236")/*
										 * @res "��˳ɹ�"
										 */);
		if (m_bQueried || m_bAdd) {
			onListRefresh();
		} else {
			m_VOs[m_nPresentRecord].getHeadVO().setIbillstatus(3);
			// �б�ť�߼�
			setButtonsList();
		}
	}

	/**
	 * ����:ִ������--��Ƭ
	 */
	private void onAudit() {
		PraybillVO voAudit = null;
		boolean isCycle = true;

		// ���������˼��������ڹ�ϣ������ʧ��ʱ�õ�
		String strPsnOld = m_VOs[m_nPresentRecord].getHeadVO().getCauditpsn();
		UFDate dateAuditOld = m_VOs[m_nPresentRecord].getHeadVO()
				.getDauditdate();

		while (isCycle) {
			isCycle = false;
			try {
				PraybillVO voCloned = null;
				if (isCanAutoAddLine) {
					voAudit = m_VOs[m_nPresentRecord];
					voCloned = (PraybillVO) m_VOs[m_nPresentRecord].clone();
					PraybillItemVO[] items = voAudit.getBodyVO();
					int itemsLength = items.length;
					for (int i = 0; i < itemsLength; i++) {

						String cmangid = items[i].getCmangid();
						String cbaseid = items[i].getCbaseid();
						if (cbaseid != null
								&& cbaseid.equals('"' + cbaseid.substring(1,
										cbaseid.length() - 1) + '"')) {
							items[i].setCbaseid(cbaseid.substring(1, cbaseid
									.length() - 1));
						}
						if (cmangid != null
								&& cmangid.equals('"' + cmangid.substring(1,
										cmangid.length() - 1) + '"')) {
							items[i].setCmangid(cmangid.substring(1, cmangid
									.length() - 1));
						}
					}
				} else {
					voCloned = m_VOs[m_nPresentRecord];
					voAudit = (PraybillVO) getBillCardPanel().getBillValueVO(
							PraybillVO.class.getName(),
							PraybillHeaderVO.class.getName(),
							PraybillItemVO.class.getName());
				}
				/*
				 * �޸ģ�zhongwei v31-sp1-���� �������ڲ���С���Ƶ�����(�빺����) V5.02 �ع�
				 */
				String strErr = PuTool.getAuditLessThanMakeMsg(
						new AggregatedValueObject[] { voAudit }, "dpraydate",
						"vpraycode", ClientEnvironment.getInstance().getDate(),
						ScmConst.PO_Pray);
				if (strErr != null && strErr.trim().length() > 0) {
					throw new BusinessException(strErr);
				}
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000051")/* "��������..." */);
				nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
				timer.start("�빺����������onAudit������ʼ");

				voAudit.getHeadVO().setCauditpsn(
						getClientEnvironment().getUser().getPrimaryKey());
				voAudit.getHeadVO().setDauditdate(
						getClientEnvironment().getDate());
				voAudit.setCurrOperator(getClientEnvironment().getUser()
						.getPrimaryKey());
				voAudit.getHeadVO().setCuserid(
						getClientEnvironment().getUser().getPrimaryKey());
				voAudit.getHeadVO().setTaudittime(
						(new UFDateTime(new Date())).toString());
				if (voAudit.getBodyVO() != null
						&& voAudit.getBodyVO().length > 0) {
					for (int i = 0; i < voAudit.getBodyVO().length; i++) {
						PraybillItemVO item = voAudit.getBodyVO()[i];
						int status = item.getStatus();
						if (status == 2) {
							voAudit.getBodyVO()[i].setStatus(0);
						}
					}
				}
				timer.addExecutePhase("����ǰ׼������");
				/* ���� */
				PraybillVO[] oaUserObj = new PraybillVO[] { voCloned };
				PfUtilClient.processBatchFlow(null, "APPROVE",
						nc.vo.scm.pu.BillTypeConst.PO_PRAY,
						getClientEnvironment().getDate().toString(),
						new PraybillVO[] { voAudit }, oaUserObj);

				if (!PfUtilClient.isSuccess()) {
					// ����ʧ�ܣ��ָ������˼���������
					voAudit.getHeadVO().setCauditpsn(strPsnOld);
					voAudit.getHeadVO().setDauditdate(dateAuditOld);

					showHintMessage(m_lanResTool.getStrByID("40040101",
							"UPP40040101-000052")/* "����δ�ɹ�" */);
					return;
				}
				/*
				 * �������Ż�������־�����Ƶ���̨ // ҵ����־ Operlog operlog = new Operlog();
				 * voAudit.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
				 * voAudit.getOperatelogVO().setCompanyname(
				 * nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
				 * voAudit.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
				 * operlog.insertBusinessExceptionlog(voAudit, "����", "����",
				 * nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
				 * nc.vo.scm.pu.BillTypeConst.PO_PRAY,
				 * nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
				 */

				timer.addExecutePhase("ִ��APPROVE�ű�����");

				// �����ɹ���ˢ��
				Integer iBillStatus = setLastestInfosToVoAfterAuditted(voAudit);
				timer.addExecutePhase("�����ɹ���ˢ��");
				m_VOs[m_nPresentRecord] = voAudit;

				/* ���ص��� */
				setVoToBillCard(m_nPresentRecord, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000447")/* "������" */);
				/* ˢ�°�ť״̬ */
				setButtonsCard();
				timer.addExecutePhase("��������ʾ");
				timer.showAllExecutePhase("�빺����������onAudit��������");
				getBillCardPanel().setEnabled(false);
				if (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.FREE) == 0
						|| iBillStatus
								.compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) == 0) {
					showHintMessage(m_lanResTool.getStrByID("40040101",
							"UPP40040101-000052")/* "����δ�ɹ�" */);

				} else if (iBillStatus
						.compareTo(nc.vo.scm.pu.BillStatus.AUDITED) == 0
						|| iBillStatus
								.compareTo(nc.vo.scm.pu.BillStatus.AUDITING) == 0) {
					showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000236")/* "�����ɹ�" */);
				}
			} catch (Exception e) {
				// ����ʧ�ܣ��ָ������˼���������
				voAudit.getHeadVO().setCauditpsn(strPsnOld);
				voAudit.getHeadVO().setDauditdate(dateAuditOld);

				// ������ʽ���ɲɹ��������ݲ���ʾ���
				if (e instanceof RwtPoToPrException) {
					// �빺���ۼ�����������ʾ
					int iRet = showYesNoMessage(e.getMessage());
					if (iRet == MessageDialog.ID_YES) {
						// ����ѭ��
						isCycle = true;
						// �Ƿ��û�ȷ��
						voAudit.setUserConfirm(true);
					}
				} else {
					PuTool.outException(this, e);
				}
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000054")/* "�����쳣,����ʧ��" */);
			}
		}
	}

	/**
	 * getBatchPrintEntry()�� �������ڣ�(2004-12-09) ��ô�ӡ���
	 * 
	 * @author������
	 * @return PrintDataInterface
	 * @param null
	 */
	protected nc.ui.pub.print.PrintEntry getNewBatchPrintEntry() {
		if (null == m_print) {
			m_print = new nc.ui.pub.print.PrintEntry(null, null);
			m_print.setTemplateID(getCorpPrimaryKey(), "4004070101",
					getClientEnvironment().getUser().getUserCode(), null);
		}
		return m_print;
	}

	/**
	 * ҵ������--����¼���Ӧ
	 * 
	 * @param bo
	 */
	// private void onBusiType(ButtonObject bo) {
	//
	// PfUtilClient.retAddBtn(m_btnAdds, m_sLoginCorpId, "20", bo);
	//    
	// // m_bizButton = bo;
	//    
	// //���ؿ�Ƭ��ť
	// setButtons(m_btnTree.getButtonArray());
	//    
	// bo.setSelected(true);
	//    
	// setButtonsCard();
	// }
	/**
	 * ����
	 * 
	 * @modified by czp v50, �����߼��ع�
	 */
	private void onSendAudit() {

		boolean bSaveFlag = getBillCardPanel().getBillData().getEnabled();
		PraybillVO vo = null;
		try {
			// �༭״̬���󣽡����桱��������
			if (bSaveFlag) {
				boolean bContinue = onSave();
				if (!bContinue) {
					return;
				}
			}
			// ��ȡˢ�º��VO����
			vo = getPraybillVOs()[getCurVoPos()];
			if (vo == null) {
				setButtonsCard();
				SCMEnv.out("�빺��VOΪ�գ�����ɹ��������ܼ�������");/*-=notranslate=-*/
				showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000408")/* @res"����ʧ�ܣ�" */);
				return;
			}

			PfUtilClient.processAction("SAVE", BillTypeConst.PO_PRAY,
					ClientEnvironment.getInstance().getDate().toString(), vo);

			// ˢ�µ��ݣ�֧�������������
			setLastestInfosToVoAfterAuditted(vo);

			// ˢ�°�ť״̬
			setButtonsCard();

			/* ���ص��� */
			setVoToBillCard(m_nPresentRecord, m_lanResTool.getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000265")/* "����" */);
			//
			updateButtonsAll();
			// ���������־
			isAlreadySendToAudit = false;
			setButtonsRevise(isCurAuditOP(vo));

			showHintMessage(m_lanResTool.getStrByID("common", "UCH023")/* @res"������" */);
		} catch (Exception e) {
			SCMEnv.out(e);
			if (e instanceof BusinessException || e instanceof RuntimeException) {
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000081")/*
															 * @res "�빺������"
															 */, e.getMessage());
			} else {
				MessageDialog
						.showErrorDlg(this, m_lanResTool.getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000270")/*
																	 * @res "��ʾ"
																	 */, m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000408")/* @res"����ʧ�ܣ�" */);
			}
		}

	}

	/**
	 * ��Ƭ��ť��Ӧ��
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param bo
	 *            <p>
	 * @author czp
	 * @time 2007-3-14 ����10:47:22
	 */
	private void onButtonClickedCard(ButtonObject bo) {
		if (bo == m_btnReSortRowNo) {
			onReSortRowNo();
		} else if (bo == m_btnCardEdit) {
			onCardEdit();
		} else if (bo == m_btnPstLineTail) {
			onPasteLineToTail();
		} else if (bo == m_btnAdds) {
			onAppend();
		} else if (bo == m_btnSave) {
			onSave();
		} else if (bo == m_btnModify) {
			onModify(false, false);
		} else if (bo == m_btnCancel) {
			onCancel();
		} else if (bo == m_btnDiscard) {
			onDiscard();
		} else if (bo == m_btnCopy) {
			onCopy();
		} else if (bo == m_btnAddLine) {
			onAppendLine(getBillCardPanel(), this);
		} else if (bo == m_btnDelLine) {
			onDeleteLine();
		} else if (bo == m_btnInsLine) {
			onInsertLine();
		} else if (bo == m_btnCpyLine) {
			onCopyLine();
		} else if (bo == m_btnPstLine) {
			onPasteLine();
		} else if (bo == m_btnSendAudit) {
			onSendAudit();
		} else if ("APPROVE".equals(bo.getTag())) {
			onAudit();
		} else if ("UNAPPROVE".equals(bo.getTag())) {
			onUnAudit();
		} else if ("CLOSE".equals(bo.getTag())) {
			onCloseCard();
		} else if ("OPEN".equals(bo.getTag())) {
			onOpenCard();
		} else if (bo == m_btnQuery) {
			onCardQuery();
		} else if (bo == m_btnFirst) {
			onFirst();
		} else if (bo == m_btnPrev) {
			onPrevious();
		} else if (bo == m_btnNext) {
			onNext();
		} else if (bo == m_btnLast) {
			onLast();
		} else if (bo == m_btnCard) {
			onList();
		}
		/* ����״̬��ѯ������Ϣ���Ĺ��� */
		else if (bo == m_btnWorkFlowBrowse) {
			onQueryForAudit();
		} else if (bo == m_btnUsable) {
			onQueryInvOnHand();
		} else if (bo == m_btnPriceInfo) {
			onPriceInfos();
		}
		/* �ĵ���������Ϣ���Ĺ��� */
		else if (bo == m_btnDocument) {
			onDocument();
		}
		/* ������ */
		else if (bo == m_btnLinkBillsBrowse) {
			onLinkQuery();
		}
		/* ��Ϣ���İ�ť */
		else if (bo == m_btnAudit) {
			onAudit();
		} else if (bo == m_btnUnAudit) {
			onUnAudit();
		} else if (bo == m_btnPrint) {
			onPrint();
		} else if (bo == m_btnPrintPreview) {
			onPrintPreview();
		}
		// �ϲ���ʾ����ӡ
		else if (bo == m_btnCombin) {
			onCombin();
		}
		// ˢ��
		else if (bo == m_btnRefresh) {
			onCardRefresh();
		}
		// ������ʾ/����
		else if (bo == boOnHandShowHidden) {
			onCardOnHandShowHidden();
		} else if (bo == m_btnRevise) {
			onRevise();
		}
	}

	/**
	 * �б�ť��Ӧ��
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param bo
	 *            <p>
	 * @author czp
	 * @time 2007-3-14 ����10:47:42
	 */
	private void onButtonClickedList(ButtonObject bo) {
		if (bo == m_btnModify) {
			onModify(true, false);
		} else if (bo == m_btnDiscardList) {
			onDiscard();
		} else if (bo == m_btnCopy) {
			onCopyList();
		}
		// ����
		else if (bo == m_btnSendAudit) {
			onSendAudit();
		} else if ("APPROVE".equals(bo.getTag())) {
			onAuditList();
		} else if ("UNAPPROVE".equals(bo.getTag())) {
			onUnAuditList();
		} else if ("CLOSE".equals(bo.getTag())) {
			onCloseList();
		} else if ("OPEN".equals(bo.getTag())) {
			onOpenList();
		}
		/* �л�����Ƭ */
		else if (bo == m_btnCard) {
			onCard();
		} else if (bo == m_btnWorkFlowBrowse) {
			onQueryForAudit();
		} else if (bo == m_btnUsableList) {
			onQueryInvOnHand();
		} else if (bo == m_btnDocumentList) {
			onDocument();
		}
		/* ������ */
		else if (bo == m_btnLinkBillsBrowse) {
			onLinkQuery();
		}
		/* ȫѡ */
		else if (bo == m_btnSelectAll) {
			onSelectAll();
		}
		/* ȫ�� */
		else if (bo == m_btnSelectNo) {
			onSelectNo();
		}
		/* ��ѯ-�б� */
		else if (bo == m_btnQueryList) {
			onListQuery();
		}
		// �б��ӡ
		else if (bo == m_btnPrintList) {
			onPrintList();
		}
		// �б��ӡԤ��
		else if (bo == m_btnPrintListPreview) {
			onPrintListPreview();
		}
		// ˢ��
		else if (bo == m_btnRefreshList) {
			onListRefresh();
		}
		// ������ʾ/����
		else if (bo == boOnHandShowHidden) {
			onCardOnHandShowHidden();
		} else if (bo == m_btnRevise) {
			onRevise();
		}
	}

	/**
	 * ����ʵ�ָ÷�������Ӧ��ť�¼���
	 * 
	 * @version (00-6-1 10:32:59)
	 * @param bo
	 *            ButtonObject
	 */
	public void onButtonClicked(ButtonObject bo) {
		boolean bCardShowing = getBillCardPanel().isShowing();
		if (bCardShowing) {
			onButtonClickedCard(bo);
		} else {
			onButtonClickedList(bo);
		}
		// add by zhw 2011-06-27 �׸���Ŀ ����
		if (bo == m_btnOUT) {
			onboexport();
		}
		// ������չ��ť�¼�
		onExtendBtnsClick(bo);

		// ������չ��ť״̬
		if (m_bEdit) {
			setExtendBtnsStat(2);
		} else {
			setExtendBtnsStat(1);
		}
		//
		updateButtonsAll();

	}

	/**
	 * ��Ŧ������ʾ/����
	 */
	private void onCardOnHandShowHidden() {
		// ������ѯ����ֻ�ڿ�Ƭ������ʾ
		if (getBillListPanel().isShowing()) {
			onCard();
		}

		m_bOnhandShowHidden = !m_bOnhandShowHidden;
		// ��ʾ/����
		showPanel(true, m_bOnhandShowHidden);
		if (m_bOnhandShowHidden) {
			freshOnhandnum(getBillCardPanel().getBillTable().getSelectedRow());
		}
		updateUI();
	}

	/**
	 * ������ʾ/����
	 */
	private void showPanel(boolean bCardShow, boolean bSouthPanelShow) {

		getBillCardPanel().setVisible(bCardShow);
		getSplitPanelBc().setVisible(bCardShow);
		if (bCardShow) {
			if (bSouthPanelShow) {
				if (getSplitPanelBc().getBottomComponent() == null) {
					getSplitPanelBc().add(getPnlSouth(this),
							nc.ui.pub.beans.UISplitPane.BOTTOM);

					getSplitPanelBc().add(getBillCardPanel(),
							nc.ui.pub.beans.UISplitPane.TOP);
				}
				getSplitPanelBc().setDividerLocation(
						(int) (getBillCardPanel().getHeight() * 0.68));
				// remove(getSplitPanelBc());
				add(getSplitPanelBc(), java.awt.BorderLayout.CENTER);

			} else {
				if (getSplitPanelBc().getTopComponent() == null)
					getSplitPanelBc().add(getBillCardPanel(),
							nc.ui.pub.beans.UISplitPane.TOP);
				if (getSplitPanelBc().getBottomComponent() != null)
					getSplitPanelBc().remove(getPnlSouth(this));
				getSplitPanelBc().setDividerLocation(
						(int) (getSplitPanelBc().getHeight() * 0.95));
				remove(getSplitPanelBc());
				add(getBillCardPanel(), java.awt.BorderLayout.CENTER);
				// add(getSplitPanelBc(),java.awt.BorderLayout.CENTER);
			}
		}

	}

	/**
	 * �ָ����ʵ��
	 */
	private UISplitPane getSplitPanelBc() {
		if (pnlCardAndBc == null) {

			pnlCardAndBc = new nc.ui.pub.beans.UISplitPane(
					nc.ui.pub.beans.UISplitPane.VERTICAL_SPLIT);
			pnlCardAndBc.setName("card2");
			pnlCardAndBc.add(getBillCardPanel(),
					nc.ui.pub.beans.UISplitPane.TOP);
		}

		return pnlCardAndBc;

	}

	/**
	 * ����������
	 */
	public void freshOnhandnum(int row) {
		if (!m_bOnhandShowHidden)
			return;
		if (row == -1) {
			row = 1;
		}

		if (row < 0) {
			((IQueryOnHandInfoPanel) getPnlSouth(this)).showData(null);
			setButtonsCardModify();
			return;
		}
		OnHandRefreshVO onhandnumvo = getSelectedItemHandInfo();

		// �����ִ�����ѯ���������������֯������������������Ρ������ֿ�
		((IQueryOnHandInfoPanel) getPnlSouth(this)).showData(onhandnumvo);
	}

	/**
	 * �������
	 */
	protected UIPanel getPnlSouth(BillLineInfoListener listener) {

		if (m_pnlOnHand == null) {

			Object obj = null;
			try {
				obj = new QueryOnHandInfoPanel(listener, false);
			} catch (Exception e) {
				SCMEnv.out(e);
			}

			if (obj != null) {
				try {
					m_pnlOnHand = (UIPanel) ((IQueryOnHandInfoPanel) obj);
				} catch (Exception e) {
					SCMEnv.out(e);
				}
			}

		}
		return m_pnlOnHand;
	}

	/**
	 * ��ӡԤ��-�б�
	 */
	private void onPrintListPreview() {
		try {
			if (printList == null) {
				printList = new ScmPrintTool(this, getBillCardPanel(),
						getSelectedBills(), getModuleCode());
			} else {
				printList.setData(getSelectedBills());
			}
			printList.onBatchPrintPreview(getBillListPanel(),
					getBillCardPanel(), "20");
			if (PuPubVO
					.getString_TrimZeroLenAsNull(printList.getPrintMessage()) != null) {
				showHintMessage(printCard.getPrintMessage());

			}
		} catch (BusinessException e) {
			PuTool.outException(this, e);
		}
	}

	/**
	 * ��ӡ-�б�
	 */
	private void onPrintList() {

		try {
			if (printList == null) {
				printList = new ScmPrintTool(this, getBillCardPanel(),
						getSelectedBills(), getModuleCode());
			} else {
				printList.setData(getSelectedBills());
			}
			printList
					.onBatchPrint(getBillListPanel(), getBillCardPanel(), "20");
			if (PuPubVO
					.getString_TrimZeroLenAsNull(printList.getPrintMessage()) != null) {
				showHintMessage(printList.getPrintMessage());
			}
		} catch (BusinessException e) {
			PuTool.outException(this, e);
		}
	}

	/**
	 * ��ӡԤ��--��Ƭ
	 */
	private void onPrintPreview() {

		Vector vAll = new Vector();
		PraybillVO[] oneBill = null;
		vAll.add(m_VOs[m_nPresentRecord]);
		oneBill = new PraybillVO[vAll.size()];
		// oneBill[0] = new PraybillVO();
		vAll.copyInto(oneBill);
		ArrayList aryRslt = new ArrayList();
		aryRslt.add(oneBill[0]);

		try {
			if (printCard == null) {
				// Ŀǰ�����Ͼ������벹����
				if (nc.vo.scm.pub.CustomerConfigVO
						.getCustomerName()
						.equalsIgnoreCase(
								nc.vo.scm.pub.CustomerConfigVO.NAME_NANJINGPUZHEN)) {
					PurchasePrintDS printData = new PurchasePrintDS(
							getModuleCode(), getBillCardPanel());
					printCard = new ScmPrintTool(getBillCardPanel(), printData,
							aryRslt, getModuleCode());
				} else {
					printCard = new ScmPrintTool(this, getBillCardPanel(),
							aryRslt, getModuleCode());
				}
				printCard = new ScmPrintTool(this, getBillCardPanel(), aryRslt,
						getModuleCode());
			} else {
				printCard.setData(aryRslt);
			}
			printCard.onCardPrintPreview(getBillCardPanel(),
					getBillListPanel(), "20");
			if (PuPubVO
					.getString_TrimZeroLenAsNull(printCard.getPrintMessage()) != null) {
				showHintMessage(printCard.getPrintMessage());

			}
		} catch (Exception e1) {
			PuTool.outException(this, e1);
		}
		// setVoToBillCard(m_nPresentRecord, "");
	}

	/**
	 * ��ӡ--��Ƭ
	 */
	private void onPrint() {
		Vector vAll = new Vector();
		PraybillVO[] oneBill = null;
		vAll.add(m_VOs[m_nPresentRecord]);
		oneBill = new PraybillVO[vAll.size()];
		// oneBill[0] = new PraybillVO();
		vAll.copyInto(oneBill);
		ArrayList aryRslt = new ArrayList();
		aryRslt.add(oneBill[0]);

		try {
			if (printCard == null) {
				// Ŀǰ�����Ͼ������벹����
				if (nc.vo.scm.pub.CustomerConfigVO
						.getCustomerName()
						.equalsIgnoreCase(
								nc.vo.scm.pub.CustomerConfigVO.NAME_NANJINGPUZHEN)) {
					PurchasePrintDS printData = new PurchasePrintDS(
							getModuleCode(), getBillCardPanel());
					printCard = new ScmPrintTool(getBillCardPanel(), printData,
							aryRslt, getModuleCode());
				} else {
					printCard = new ScmPrintTool(this, getBillCardPanel(),
							aryRslt, getModuleCode());
				}
			} else {
				printCard.setData(aryRslt);
			}
			printCard.onCardPrint(getBillCardPanel(), getBillListPanel(), "20");
			if (PuPubVO
					.getString_TrimZeroLenAsNull(printCard.getPrintMessage()) != null) {
				showHintMessage(printCard.getPrintMessage());
				// MessageDialog.showHintDlg(this,
				// m_lanResTool.getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
				// "��ʾ" */,
				// printCard.getPrintMessage());
			}
		} catch (Exception e1) {
			PuTool.outException(this, e1);
		}
	}

	/**
	 * ��������:����
	 */
	public void onCancel() {

		isFrmCopy = false;
		if (m_bOnhandShowHidden) {
			onCardOnHandShowHidden();
		}
		/* ��������ȡ�� */
		if (m_bCancel)
			return;
		else
			m_bCancel = true;
		// ��ֹ�༭
		if (getBillCardPanel().getBillTable().getEditingRow() >= 0) {
			getBillCardPanel().getBillTable().editingStopped(
					new ChangeEvent(getBillCardPanel().getBillTable()));
		}
		/*
		 * ���ӱ�־ΪFALSE,�༭��־ΪFALSE,���ݴ��ڲ��ɱ༭״̬ ������״̬�·������������ǰ���ݴ���,��ʾ��Ӧ�ĵ���;����,��ʾΪ��
		 * ���޸�״̬�·�������ʾ����ǰ�ĵ���
		 */
		if (m_bAdd) {
			if (m_VOs != null && m_VOs.length > 0) {
				/* �������ǰ���ݴ���,����ʾ��Ӧ�ĵ��� */
				if (m_VOs != null
						&& m_VOs[m_nPresentRecord] != null
						&& m_VOs[m_nPresentRecord].getHeadVO().getCpraybillid() != null) {
					getBillCardPanel().setBillValueVO(m_VOs[m_nPresentRecord]);
					nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillCardPanel());
					/* ��ʾ��Դ��Ϣ */
					PuTool.loadSourceInfoAll(getBillCardPanel(),
							BillTypeConst.PO_PRAY);
					getBillCardPanel().getBillModel().execLoadFormula();
					getBillCardPanel().updateValue();
					getBillCardPanel().updateUI();
					/* ��ʾ��ע */
					if (getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane) {
						UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel()
								.getHeadItem("vmemo").getComponent();
						nRefPanel3.setValue(m_VOs[m_nPresentRecord].getHeadVO()
								.getVmemo());
					}
				}
				if (m_VOs != null
						&& m_VOs.length == 1
						&& m_VOs[m_nPresentRecord] != null
						&& m_VOs[m_nPresentRecord].getHeadVO().getCpraybillid() == null) {
					getBillCardPanel().getBillData().clearViewData();
					getBillCardPanel().updateUI();
				}
			} else {
				/* �������ǰ���ݲ�����,��������� */
				getBillCardPanel().getBillData().clearViewData();
				getBillCardPanel().updateUI();
			}
		} else {
			getBillCardPanel().setBillValueVO(m_VOs[m_nPresentRecord]);
			nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillCardPanel());
			getBillCardPanel().getBillModel().execLoadFormula();
			/* ��ʾ��Դ��Ϣ */
			PuTool.loadSourceInfoAll(getBillCardPanel(), BillTypeConst.PO_PRAY);
			getBillCardPanel().updateValue();
			getBillCardPanel().updateUI();
			/* ��ʾ��ע */
			if (getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane) {
				UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel()
						.getHeadItem("vmemo").getComponent();
				nRefPanel3.setValue(m_VOs[m_nPresentRecord].getHeadVO()
						.getVmemo());
			}
		}
		displayHeadVdef();
		setButtonsCard();
		/* �ָ���ť״̬ */
		m_nUIState = 0;
		m_bAdd = false;
		m_bEdit = false;
		getBillCardPanel().setEnabled(false);
		if (m_VOs != null)
			setButtonsRevise(isCurAuditOP(m_VOs[m_nPresentRecord]));
		else
			setButtonsRevise(false);

		// ��־ƽ ������������������������ʾ����
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), null);

		showHintMessage(m_lanResTool.getStrByID("common", "UCH008")/* "ȡ���ɹ�" */);
	}

	private String[] headNeedDisplayFields = new String[] { "vmemo", "vdef1",
			"vdef2", "vdef3", "vdef4", "vdef5", "vdef6", "vdef7", "vdef8",
			"vdef9", "vdef10", "vdef11", "vdef12", "vdef13", "vdef14",
			"vdef15", "vdef16", "vdef17", "vdef18", "vdef19", "vdef20" };

	/**
	 * ��������:��Ƭ״̬��ѯ
	 */
	private void onCardQuery() {

		getQueryDlg().showModal();

		if (getQueryDlg().isCloseOK()) {

			m_bQueried = true;

			// //��������Ȩ��, czp ,since v50O
			// getQueryDlg().setRefsDataPowerConVOs(
			// PoPublicUIClass.getLoginUser(),
			// new String[]{PoPublicUIClass.getLoginPk_corp()},
			// PrTool.getPowerNodeNames(),
			// PrTool.getPowerCodes(),
			// PrTool.getPowerReturnTypes());

			onCardRefresh();
		}

		showHintMessage(m_lanResTool.getStrByID("common", "UCH009")/*
																	 * @res
																	 * "��ѯ���"
																	 */);
	}

	/**
	 * ��Ƭˢ��
	 */
	private void onCardRefresh() {
		showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000360")/* "��ʼ��ѯ..." */);
		// �Զ�������
		ConditionVO[] conditionVO = getQueryDlg().getConditionVO();
		// ��Դ��������
		String strSubSql = getQueryDlg().getSubSQL();
		// ��ѯ�빺��
		try {
			m_VOs = PraybillHelper.queryAll(getQueryDlg()
					.getSelectedCorpIdString(), conditionVO, getQueryDlg()
					.getStatusCndStr(), null, strSubSql, getClientEnvironment()
					.getUser().getPrimaryKey());
		} catch (BusinessException e) {
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000038")/* "��ѯʧ��" */);
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000055")/* "�빺����ѯ" */, e
					.getMessage());
			return;
		} catch (Exception e) {
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000038")/* "��ѯʧ��" */);
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000055")/* "�빺����ѯ" */, e
					.getMessage());
			return;
		}
		// �����ز�ѯ���
		if (m_VOs != null && m_VOs.length > 0) {

			// �����ͷ�����б�
			Integer nTemp1 = (Integer) m_VOs[0].getHeadVO().getAttributeValue(
					"ipraysource");
			Integer nTemp2 = (Integer) m_VOs[0].getHeadVO().getAttributeValue(
					"ipraytype");

			if (nTemp1 != null)
				m_comPraySource.setSelectedIndex(nTemp1.intValue());
			if (nTemp2 != null)
				m_comPrayType.setSelectedIndex(nTemp2.intValue());

			Vector v = new Vector();
			for (int i = 0; i < m_VOs.length; i++) {
				v.addElement(m_VOs[i].getHeadVO());
			}
			PraybillHeaderVO[] headVO = new PraybillHeaderVO[v.size()];
			v.copyInto(headVO);

			// ���ص��ݵ���Ƭ
			m_nPresentRecord = 0;
			setVoToBillCard(m_nPresentRecord, null);
		} else {
			m_VOs = null;
			// �޷��ز�ѯ���
			MessageDialog
					.showHintDlg(this, m_lanResTool.getStrByID("40040101",
							"UPP40040101-000055")/* "�빺����ѯ" */, m_lanResTool
							.getStrByID("40040101", "UPP40040101-000056")/* "û�з����������빺����" */);
			// �������
			getBillCardPanel().getBillData().clearViewData();
		}
		// ���ð�ť�߼�
		setButtonsCard();
		//
		m_nUIState = 0;
		// �����½���ʾ��Ϣ
		int iCnt = 0;
		if (m_VOs != null && m_VOs.length > 0)
			iCnt = m_VOs.length;
		if (iCnt > 0) {
			String[] value = new String[] { String.valueOf(iCnt) };
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000478", null, value)/*
														 * "��ѯ��ϣ����� " +iCnt +
														 * "�ŵ���"
														 */);
		} else
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000361")/* "��ѯ��ϣ�û�в鵽����" */);
		m_bAdd = false;
		m_bEdit = false;
		getBillCardPanel().setEnabled(false);
		showHintMessage(m_lanResTool.getStrByID("common", "UCH007")/*
																	 * @res
																	 * "ˢ�³ɹ�"
																	 */);
		freshHeadCproject(getBillCardPanel());
	}

	/**
	 * ��������:�رյ�ǰ���빺��
	 */
	private void onCloseList() {
		this.showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000390")/* "���ڹرյ���..." */);
		m_VOs[m_nPresentRecord].getHeadVO().setCuserid(
				getClientEnvironment().getUser().getPrimaryKey());
		long tTime = System.currentTimeMillis();
		try {
			// ������Ա
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			m_VOs[m_nPresentRecord].setCurrOperator(strOpr);
			ArrayList keys = (ArrayList) PfUtilClient
					.processActionNoSendMessage(this, "CLOSE",
							nc.vo.scm.pu.BillTypeConst.PO_PRAY,
							getClientEnvironment().getDate().toString(),
							m_VOs[m_nPresentRecord], null, null, null);
			m_VOs[m_nPresentRecord] = (PraybillVO) keys.get(0);

			m_VOs[m_nPresentRecord].getHeadVO().setIbillstatus(new Integer(1));
			// ���ݹرպ�����
			if (m_VOs[m_nPresentRecord] != null
					&& m_VOs[m_nPresentRecord].getBodyVO() != null) {
				int itemsLength = m_VOs[m_nPresentRecord].getBodyVO().length;
				UFDouble nMoney = null;
				UFDouble nPrayNum = null;
				UFDouble nSuggestPrice = null;

				for (int i = 0; i < itemsLength; i++) {
					nMoney = m_VOs[m_nPresentRecord].getBodyVO()[i].getNmoney();
					nPrayNum = m_VOs[m_nPresentRecord].getBodyVO()[i]
							.getNpraynum();
					nSuggestPrice = m_VOs[m_nPresentRecord].getBodyVO()[i]
							.getNsuggestprice();
					if (nMoney == null && nPrayNum != null
							&& nSuggestPrice != null) {
						nMoney = new UFDouble(nPrayNum.doubleValue()
								* nSuggestPrice.doubleValue());
					} else {
						nMoney = new UFDouble(0);
					}
					m_VOs[m_nPresentRecord].getBodyVO()[i].setNmoney(nMoney);
				}

			}
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000057")/* "�빺���ر�" */, e
					.getMessage());
			SCMEnv.out(e);
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000391")/* "�ر�ʧ��" */);
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		Logger.debug("�빺���ر�ʱ�䣺" + tTime + " ms!");
		m_nUIState = 0;
		getBillListPanel().getHeadBillModel().setBodyRowVO(
				m_VOs[m_nPresentRecord].getHeadVO(), m_nPresentRecord);
		getBillListPanel().getHeadBillModel().execLoadFormula();
		getBillListPanel().getBodyBillModel().setBodyDataVO(
				m_VOs[m_nPresentRecord].getBodyVO());
		getBillListPanel().getBodyBillModel().execLoadFormula();
		updateUI();
		/* ˢ�°�ť״̬ */
		setButtonsList();
		//
		showHintMessage(m_lanResTool.getStrByID("common", "UCH013")/*
																	 * @res
																	 * "�ѹر�"
																	 */);
		return;
	}

	/**
	 * ��������:�رյ�ǰ���빺��
	 */
	private void onCloseCard() {
		this.showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000390")/* "���ڹرյ���..." */);
		m_VOs[m_nPresentRecord].getHeadVO().setCuserid(
				getClientEnvironment().getUser().getPrimaryKey());
		long tTime = System.currentTimeMillis();
		try {
			// ������Ա
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			m_VOs[m_nPresentRecord].setCurrOperator(strOpr);
			ArrayList keys = (ArrayList) PfUtilClient
					.processActionNoSendMessage(this, "CLOSE",
							nc.vo.scm.pu.BillTypeConst.PO_PRAY,
							getClientEnvironment().getDate().toString(),
							m_VOs[m_nPresentRecord], null, null, null);
			m_VOs[m_nPresentRecord] = (PraybillVO) keys.get(0);

			m_VOs[m_nPresentRecord].getHeadVO().setIbillstatus(new Integer(1));
			if (m_VOs[m_nPresentRecord] != null
					&& m_VOs[m_nPresentRecord].getBodyVO() != null) {
				int itemsLength = m_VOs[m_nPresentRecord].getBodyVO().length;
				UFDouble nMoney = null;
				UFDouble nPrayNum = null;
				UFDouble nSuggestPrice = null;

				for (int i = 0; i < itemsLength; i++) {
					nMoney = m_VOs[m_nPresentRecord].getBodyVO()[i].getNmoney();
					if (nMoney != null)
						continue;
					nPrayNum = m_VOs[m_nPresentRecord].getBodyVO()[i]
							.getNpraynum();
					nSuggestPrice = m_VOs[m_nPresentRecord].getBodyVO()[i]
							.getNsuggestprice();
					if (nMoney == null && nPrayNum != null
							&& nSuggestPrice != null) {
						nMoney = new UFDouble(nPrayNum.doubleValue()
								* nSuggestPrice.doubleValue());
						m_VOs[m_nPresentRecord].getBodyVO()[i]
								.setNmoney(nMoney);
					}
				}
			}
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000057")/* "�빺���ر�" */, e
					.getMessage());
			SCMEnv.out(e);
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000391")/* "�ر�ʧ��" */);
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		Logger.debug("�빺���ر�ʱ�䣺" + tTime + " ms!");
		m_nUIState = 0;
		/* ���ص��� */
		setVoToBillCard(m_nPresentRecord, m_lanResTool.getStrByID("40040101",
				"UPP40040101-000448")/* "���ر�" */);
		/* ˢ�°�ť״̬ */
		setButtonsCard();
		//
		showHintMessage(m_lanResTool.getStrByID("common", "UCH013")/*
																	 * @res
																	 * "�ѹر�"
																	 */);
		return;
	}

	/**
	 * ��������:�˳�ϵͳ
	 */
	public boolean onClosing() {
		// ���ڱ༭����ʱ�˳���ʾ
		if (m_bEdit) {
			int iRet = MessageDialog.showYesNoCancelDlg(this, m_lanResTool
					.getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* "��ʾ" */,
					m_lanResTool.getStrByID("common", "UCH001")/*
																 * @res
																 * "�Ƿ񱣴����޸ĵ����ݣ�"
																 */);
			// ����ɹ�����˳�
			if (iRet == MessageDialog.ID_YES) {
				return onSave();
			}
			// �˳�
			else if (iRet == MessageDialog.ID_NO) {
				return true;
			}
			// ȡ���ر�
			else {
				return false;
			}
		}
		return true;
	}

	/**
	 * �б������ܡ�
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-14 ����11:02:12
	 */
	private void onCopyList() {
		// ���л�����Ƭ
		onCard();
		// ���߿�Ƭ��������
		onCopy();
	}

	/**
	 * ��������:����
	 */
	private void onCopy() {
		showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000350")/* "�༭����..." */);

		nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
		timer.start("�빺�����Ʋ�����ʼonCopy");

		isFrmCopy = true;
		// ��������޼�
		nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillCardPanel());

		timer.addExecutePhase("��������޼�loadInvMaxPrice");

		// �����빺��ԴΪ�ֹ�¼��,��������Ϊ��ǰ����(�������۶����⣬��Ϊ�л�д��ϵ)

		m_comPraySource.setEnabled(false);
		UIRefPane nRefPane = (UIRefPane) getBillCardPanel().getHeadItem(
				"dpraydate").getComponent();
		nRefPane.setValue(getClientEnvironment().getDate().toString());

		nRefPane = (UIRefPane) getBillCardPanel().getTailItem("tmaketime")
				.getComponent();
		nRefPane.setValue(null);
		nRefPane = (UIRefPane) getBillCardPanel().getTailItem("taudittime")
				.getComponent();
		nRefPane.setValue(null);
		nRefPane = (UIRefPane) getBillCardPanel().getTailItem("tlastmaketime")
				.getComponent();
		nRefPane.setValue(null);

		// ��������˴���,�����������
		nRefPane = (UIRefPane) getBillCardPanel().getTailItem("cauditpsn")
				.getComponent();
		nRefPane.setValue(null);
		// ����������ڴ���,�����������
		nRefPane = (UIRefPane) getBillCardPanel().getTailItem("dauditdate")
				.getComponent();
		nRefPane.setValue(null);
		nRefPane = (UIRefPane) getBillCardPanel().getTailItem("coperator")
				.getComponent();
		nRefPane.setPK(getClientEnvironment().getUser().getPrimaryKey());
		getBillCardPanel().setEnabled(true);
		setPartNoEditable(getBillCardPanel());

		timer.addExecutePhase("һЩ��ֵ����");

		// ���ñ��帨����״̬
		// setAssisUnitEditState();

		timer.addExecutePhase("setAssisUnitEditState����");

		m_bAdd = true;
		m_bEdit = true;
		m_bCancel = false;
		setButtonsCardCopy();
		m_nUIState = 0;
		setButtonsCardModify();
		updateButtonsAll();
		// ����ʱ,���ݺ�Ϊ��
		UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel().getHeadItem(
				"vpraycode").getComponent();
		nRefPanel3.setValue(null);
		// ��ձ�ͷ����
		getBillCardPanel().getHeadItem("cpraybillid").setValue(null);
		getBillCardPanel().getHeadItem("nversion").setValue(new Integer(1));
		getBillCardPanel().getTailItem("iprintcount").setValue(null);
		getBillCardPanel().getHeadItem("ibillstatus").setValue(
				new Integer(BillStatus.FREE));
		getBillCardPanel().getHeadItem("ts").setValue(null);
		// ��ձ�������
		BillModel bm = getBillCardPanel().getBillModel();
		int bmCount = bm.getRowCount();
		String sMangId;
		for (int i = 0; i < bmCount; i++) {
			bm.setRowState(i, BillModel.ADD);
			bm.setValueAt(null, i, "cpraybill_bid");
			bm.setValueAt(null, i, "cpraybillid");
			bm.setValueAt(null, i, "naccumulatenum");

			bm.setValueAt(null, i, "ts");
			// ���κ�
			sMangId = (String) bm.getValueAt(i, "cmangid");
			bm.setCellEditable(i, "vproducenum", PuTool.isBatchManaged(sMangId)
					&& bm.getItemByKey("vproducenum").isEdit());
		}
		// �˴��޸�Ϊ�ֹ������뱣��������Դ��Ϣ��ͻ������빺��Դ
		// if(m_comPraySource.getSelectedIndex() == 8 ){
		m_comPraySource.setSelectedIndex(5);
		for (int i = 0; i < bmCount; i++) {
			bm.setValueAt(null, i, "cupsourcebilltype");
			bm.setValueAt(null, i, "cupsourcebillid");
			bm.setValueAt(null, i, "cupsourcebillrowid");
			bm.setValueAt(null, i, "csourcebilltype");
			bm.setValueAt(null, i, "csourcebillid");
			bm.setValueAt(null, i, "csourcebillrowid");

			bm.setValueAt(null, i, "csourcebillname");
			bm.setValueAt(null, i, "csourcebillcode");
			bm.setValueAt(null, i, "csourcebillrowno");
			bm.setValueAt(null, i, "cancestorbillname");
			bm.setValueAt(null, i, "cancestorbillcode");
			bm.setValueAt(null, i, "cancestorbillrowno");
			bm.setValueAt(null, i, "ngenct");// ���ɺ�ͬ����Ҳ��Ҫ���
		}
		// }

		// ��־ƽ ������������������������ʾ����
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), m_VOs[m_nPresentRecord]);

		// �Ƿ�ֱ��
		if (getBillCardPanel().getHeadItem("isdirecttransit") != null) {
			// �����ֱ�˵ģ���Ҫ��ҵ��������գ�����ƥ��
			if (getBillCardPanel().getHeadItem("isdirecttransit").getValue()
					.equals("true")) {
				for (int i = 0; i < bmCount; i++) {
					bm.setValueAt(null, i, "cbiztypename");
				}
				getBillCardPanel().getHeadItem("isdirecttransit").setValue(
						UFBoolean.FALSE);
				try {
					// ����ҵ������
					setBiztype(getBillCardPanel(), 0, bmCount);
				} catch (BusinessException e) {
					// ��־�쳣
					nc.vo.scm.pub.SCMEnv.out(e);
				}
			}
			getBillCardPanel().getHeadItem("isdirecttransit").setEnabled(false);
		}
		// ���ɺ�ͬ����
		if (getBillCardPanel().getBodyItem("ngenct") != null) {
			getBillCardPanel().getBodyItem("ngenct").setEnabled(false);
		}

		timer.showAllExecutePhase("�빺�����Ʋ�������onCopy");

		showHintMessage(m_lanResTool.getStrByID("common", "UCH029")/*
																	 * @res
																	 * "�Ѹ���"
																	 */);
	}

	/**
	 * ��������:�п���
	 */
	private void onCopyLine() {
		if (!m_bIsSubMenuPressed) {
			int[] nSelected = getBillCardPanel().getBillTable()
					.getSelectedRows();
			if (nSelected == null || nSelected.length == 0) {
				MessageDialog
						.showHintDlg(this, m_lanResTool.getStrByID("common",
								"UC001-0000015")/*
												 * @res "������"
												 */, m_lanResTool.getStrByID("40040101",
								"UPP40040101-000059")/* "û��ѡ���빺�������У�" */);
				return;
			}
		}
		getBillCardPanel().copyLine();
		m_bCancel = false;
		m_nUIState = 0;
		updateButtonsAll();
		showHintMessage(m_lanResTool.getStrByID("common", "UCH039")/*
																	 * @res
																	 * "�����гɹ�"
																	 */);
	}

	/**
	 * ��������:ɾ��
	 */
	private void onDeleteLine() {
		if (!getBillCardPanel().delLine()) {
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("common",
					"UC001-0000013")/* "ɾ��" */, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000059")/* "û��ѡ���빺�������У�" */);
			return;
		}
		m_bCancel = false;
		m_nUIState = 0;
		updateButtonsAll();
		// ������ʾ czp 20050303 ����
		PraybillVO voCurr = (PraybillVO) getBillCardPanel().getBillValueVO(
				PraybillVO.class.getName(), PraybillHeaderVO.class.getName(),
				PraybillItemVO.class.getName());
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), voCurr);
		showHintMessage(m_lanResTool.getStrByID("common", "UCH037")/*
																	 * @res
																	 * "ɾ�гɹ�"
																	 */);
	}

	/**
	 * ��������:�빺������(�б�Ƭ����)
	 */
	private void onDiscard() {
		showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000060")/* "���ϵ���..." */);
		int ret = MessageDialog.showYesNoDlg(this, m_lanResTool.getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000219")/* @res "ȷ��" */,
				m_lanResTool.getStrByID("common", "4004COMMON000000069")/*
																		 * @res
																		 * "�Ƿ�ȷ��Ҫɾ����"
																		 */, UIDialog.ID_NO);
		if (ret != MessageDialog.ID_YES) {
			return;
		}
		Integer[] nSelected = null;
		final boolean bCardShowing = getBillCardPanel().isVisible();
		Vector vv = new Vector();
		if (bCardShowing) {
			nSelected = new Integer[1];
			nSelected[0] = new Integer(m_nPresentRecord);
		} else {
			/* �б� */
			Vector v = new Vector();
			final int nRow = getBillListPanel().getHeadBillModel()
					.getRowCount();
			int nStatus;
			for (int i = 0; i < nRow; i++) {
				nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
				if (nStatus == BillModel.SELECTED) {
					v.addElement(new Integer(nc.ui.pu.pub.PuTool
							.getIndexBeforeSort(getBillListPanel(), i)));
				} else {
					vv.addElement(new Integer(nc.ui.pu.pub.PuTool
							.getIndexBeforeSort(getBillListPanel(), i)));
				}
			}
			nSelected = new Integer[v.size()];
			v.copyInto(nSelected);
		}

		if (nSelected == null || nSelected.length == 0) {
			MessageDialog
					.showErrorDlg(this, m_lanResTool.getStrByID("40040101",
							"UPP40040101-000061")/* "�빺������" */, m_lanResTool
							.getStrByID("40040101", "UPP40040101-000062")/* "δѡ���빺����" */);
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000063")/* "���ϵ���ʧ��" */);
			return;
		}
		long tTime = System.currentTimeMillis();
		Vector v = new Vector();
		/*
		 * ����빺��״̬,���������֮һ����������: �رա���������������ͨ��������
		 */
		StringBuffer sMessage = new StringBuffer();
		PraybillVO vo = null;
		int nBillStatus = 0;
		String[] sPraybillIds = new String[nSelected.length];
		// int nSelectedLength = nSelected.length;
		ArrayList<String> alForMMPrayids = new ArrayList<String>();
		for (int i = 0; i < nSelected.length; i++) {
			vo = m_VOs[nSelected[i].intValue()];
			sPraybillIds[i] = vo.getParentVO().getAttributeValue("cpraybillid")
					.toString();
			// ��Դ 0:�ƻ����� 1:��������
			if (vo.getHeadVO().getIpraysource() == 0
					|| vo.getHeadVO().getIpraysource() == 1) {
				alForMMPrayids.add(vo.getParentVO().getAttributeValue(
						"cpraybillid").toString());
			}
			vo.getHeadVO().setCuserid(
					getClientEnvironment().getUser().getPrimaryKey());
			v.addElement(vo);
			nBillStatus = vo.getHeadVO().getIbillstatus().intValue();
			if (nBillStatus == 1) {
				sMessage.append(vo.getHeadVO().getVpraycode()
						+ m_lanResTool.getStrByID("40040101",
								"UPP40040101-000463")/* ���빺���Ѿ��رգ��������ϣ�\n" */);
			} else if (nBillStatus == 2) {
				sMessage.append(vo.getHeadVO().getVpraycode()
						+ m_lanResTool.getStrByID("40040101",
								"UPP40040101-000464")/* ���빺�������������������ϣ�\n" */);
			} else if (nBillStatus == 3) {
				sMessage.append(vo.getHeadVO().getVpraycode()
						+ m_lanResTool.getStrByID("40040101",
								"UPP40040101-000465")/* ���빺���Ѿ��������������ϣ�\n" */);
			}
			nBillStatus = vo.getHeadVO().getDr();
			if (nBillStatus > 0) {
				sMessage.append(vo.getHeadVO().getVpraycode()
						+ m_lanResTool.getStrByID("40040101",
								"UPP40040101-000466")/* ���빺���Ѿ����ϣ�\n" */);
			}
		}
		if (sMessage.length() > 0) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000061")/* "�빺������" */, sMessage
					.toString());
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000063")/* "���ϵ���ʧ��" */);
			return;
		}

		PraybillVO[] tempVOs = new PraybillVO[v.size()];
		v.copyInto(tempVOs);
		try {
			/*
			 * ����ƻ�����ά�����ӡ�ȡ���´���ܣ� Ŀǰ�����빺����ɾ���õ�����ȡ���´� ��������ģ�����òŵ��ô˷���
			 */
			if (alForMMPrayids.size() > 0
					&& PuTool.isProductEnabled(nc.ui.po.pub.PoPublicUIClass
							.getLoginPk_corp(), ScmConst.m_sModuleCodeMM)) {
				nc.ui.pr.pray.PraybillHelper.onRearOrderDelete(alForMMPrayids
						.toArray(new String[alForMMPrayids.size()]));
			}

			/* ������Ա */
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			for (int i = 0; i < m_VOs.length; i++) {
				m_VOs[i].setCurrOperator(strOpr);
				// Ϊ�ж��Ƿ���޸ġ����������˵���
				m_VOs[i].getHeadVO().setCoperatoridnow(strOpr);
				Logger.debug("Coperator = "
						+ m_VOs[i].getHeadVO().getCoperator());
				Logger.debug("Coperatoridnow = "
						+ m_VOs[i].getHeadVO().getCoperatoridnow());
				m_VOs[i].getHeadVO().setCuserid(strOpr);
			}
			/* ��������������ǰ�ż��ر��� */
			tempVOs = PrTool.getRefreshedVOs(tempVOs);

			// ��ɽ�̹�԰:��¼ҵ����־
			Operlog operlog = new Operlog();
			for (int i = 0; i < tempVOs.length; i++) {
				tempVOs[i].getOperatelogVO().setOpratorname(
						nc.ui.pub.ClientEnvironment.getInstance().getUser()
								.getUserName());
				tempVOs[i].getOperatelogVO().setCompanyname(
						nc.ui.pub.ClientEnvironment.getInstance()
								.getCorporation().getUnitname());
				tempVOs[i].getOperatelogVO().setOperatorId(
						nc.ui.pub.ClientEnvironment.getInstance().getUser()
								.getPrimaryKey());
				operlog.insertBusinessExceptionlog(tempVOs[i], "ɾ��", "ɾ��",
						nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
						nc.vo.scm.pu.BillTypeConst.PO_PRAY,
						nc.ui.sm.cmenu.Desktop.getApplet().getParameter(
								"USER_IP"));
			}

			/* ƽ̨���ϵ��� */
			PfUtilClient.processBatch("DISCARD", "20", getClientEnvironment()
					.getDate().toString(), tempVOs);
			if (PfUtilClient.isSuccess()) {
				PraybillItemVO[] bodyVO;
				/* ���ϳɹ����޸Ļ���ĵ���״̬ */
				for (int i = 0; i < nSelected.length; i++) {
					m_VOs[nSelected[i].intValue()].getHeadVO().setDr(1);
					bodyVO = m_VOs[nSelected[i].intValue()].getBodyVO();
					for (int j = 0; j < bodyVO.length; j++)
						bodyVO[j].setDr(1);
				}
			} else {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000063")/* "���ϵ���ʧ��" */);
				return;
			}
		} catch (nc.vo.pub.BusinessException e) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000061")/* "�빺������" */, e
					.getMessage());
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000063")/* "���ϵ���ʧ��" */);
			return;
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000061")/* "�빺������" */, e
					.getMessage());
			SCMEnv.out(e);
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000063")/* "���ϵ���ʧ��" */);
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		Logger.debug("�빺������ʱ�䣺" + tTime + " ms!");
		/* �빺�����Ϻ�,�����ڽ�����ʾ */
		Vector vTemp = new Vector();
		int vvSize = vv.size();
		int m_VOsLength = m_VOs.length;
		if (bCardShowing) {
			/* ��Ƭ */
			for (int i = 0; i < m_VOsLength; i++) {
				if (m_VOs[i].getHeadVO().getDr() == 0) {
					vTemp.addElement(m_VOs[i]);
				}
			}
		} else {
			/* �б� */
			int n;
			for (int i = 0; i < vvSize; i++) {
				n = ((Integer) vv.elementAt(i)).intValue();
				vTemp.addElement(m_VOs[n]);
			}
		}
		int vTempSize = vTemp.size();
		if (vTempSize > 0) {
			if (m_nUIState == 0) {
				if (m_nPresentRecord == m_VOs.length - 1)
					m_nPresentRecord--;
			}
			m_VOs = new PraybillVO[vTempSize];
			vTemp.copyInto(m_VOs);
		} else {
			/* ���е��������� */
			m_VOs = null;
			if (m_nUIState == 0) {
				getBillCardPanel().getBillData().clearViewData();
				getBillCardPanel().updateUI();
				setButtonsCard();
			} else {
				setButtonsList();
				getBillListPanel().getHeadBillModel().clearBodyData();
				getBillListPanel().getBodyBillModel().clearBodyData();
				getBillListPanel().updateUI();
			}
			showHintMessage(m_lanResTool.getStrByID("common", "UCH006")/*
																		 * @res
																		 * "ɾ���ɹ�"
																		 */);
			return;
		}
		/* ���ֵ��������� */
		if (m_nUIState == 0) {
			/* ���ص��ݵ���Ƭ */
			setVoToBillCard(m_nPresentRecord, null);
			/* ���ð�ť�߼� */
			setButtonsCard();
		} else {
			/* �������б��ͷ��ʾ����λ�� */
			m_nPresentRecord = 0;
			/* ��ø��ŵ��ݵı��� */
			try {
				m_VOs[m_nPresentRecord] = PrTool
						.getRefreshedVO(m_VOs[m_nPresentRecord]);
			} catch (Exception be) {
				if (be instanceof BusinessException)
					MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000422")/* "ҵ���쳣" */,
							be.getMessage());
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000065")/* "���ϵ��ݳɹ���ɺ���ص���ʱ���ִ���" */);
				return;
			}
			/* �����б����б�ͷ����һ�ŵ��ݱ��� */
			Vector vv0 = new Vector();
			m_VOsLength = m_VOs.length;
			for (int i = 0; i < m_VOsLength; i++)
				vv0.addElement(m_VOs[i].getHeadVO());
			if (vv0.size() > 0) {
				PraybillHeaderVO[] headVO = new PraybillHeaderVO[vv.size()];
				vv0.copyInto(headVO);
				getBillListPanel().getHeadBillModel().setBodyDataVO(headVO);
				getBillListPanel().getHeadBillModel().execLoadFormula();
				getBillListPanel().getBodyBillModel().setBodyDataVO(
						m_VOs[m_nPresentRecord].getBodyVO());
				nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillListPanel());
				getBillListPanel().getBodyBillModel().execLoadFormula();
				getBillListPanel().updateUI();
				/* ��Ĭ����ʾΪ��һ�� */
				getBillListPanel().getHeadBillModel().setRowState(
						m_nPresentRecord, BillModel.SELECTED);
				getBillListPanel().getHeadTable().setRowSelectionInterval(
						m_nPresentRecord, m_nPresentRecord);
				setButtonsList();
				int headVOLength = headVO.length;
				/* ��ʾ��ע */
				for (int i = 0; i < headVOLength; i++) {
					getBillListPanel().getHeadBillModel().setValueAt(
							headVO[i].getVmemo(), i, "vmemo");
				}
				/* ��ʾ������Դ��Ϣ */
				PuTool.loadSourceInfoAll(getBillListPanel(),
						BillTypeConst.PO_PRAY);
			}
		}
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000068")/*
															 * @res "���ϳɹ�"
															 */);
	}

	/**
	 * ���� ���Ĺܹ��� ���õ���״̬����������������������б�
	 */
	private void onDocument() {
		String[] strPks = null;
		String[] strCodes = null;
		HashMap mapBtnPowerVo = new HashMap();
		Integer iBillStatus = null;
		//
		boolean isCard = getBillCardPanel().isShowing();
		/* ��Ϣ������ͬ���ݿ�Ƭ */
		if (!(getBillCardPanel().isShowing() || getBillListPanel().isShowing())) {
			isCard = true;
		}
		// ��Ƭ
		if (isCard) {
			if (m_VOs != null && m_VOs.length > 0
					&& m_VOs[m_nPresentRecord] != null
					&& m_VOs[m_nPresentRecord].getParentVO() != null) {
				strPks = new String[] { (String) m_VOs[m_nPresentRecord]
						.getParentVO().getAttributeValue("cpraybillid") };
				strCodes = new String[] { (String) m_VOs[m_nPresentRecord]
						.getParentVO().getAttributeValue("vpraycode") };
				// �����ĵ������ɾ����ť�Ƿ����
				BtnPowerVO pVo = new BtnPowerVO(strCodes[0]);
				iBillStatus = PuPubVO.getInteger_NullAs(m_VOs[m_nPresentRecord]
						.getParentVO().getAttributeValue("ibillstatus"),
						new Integer(BillStatus.FREE));
				if (iBillStatus.intValue() == 1 || iBillStatus.intValue() == 2
						|| iBillStatus.intValue() == 3) {
					pVo.setFileDelEnable("false");
				}
				mapBtnPowerVo.put(strCodes[0], pVo);
			}
		}
		// �б�
		final boolean isList = getBillListPanel().isShowing();
		if (isList) {
			if (m_VOs != null && m_VOs.length > 0) {
				PraybillHeaderVO[] headers = null;
				headers = (PraybillHeaderVO[]) getBillListPanel()
						.getHeadBillModel().getBodySelectedVOs(
								PraybillHeaderVO.class.getName());
				if (headers == null || headers.length <= 0) {
					showHintMessage(m_lanResTool.getStrByID("40040101",
							"UPP40040101-000066")/* "û����ȷ��ȡ���ݺ�,���ܽ����ĵ�����" */);
					return;
				}
				strPks = new String[headers.length];
				strCodes = new String[headers.length];
				BtnPowerVO pVo = null;
				for (int i = 0; i < headers.length; i++) {
					strPks[i] = headers[i].getPrimaryKey();
					strCodes[i] = headers[i].getVpraycode();
					// �����ĵ������ɾ����ť�Ƿ����
					pVo = new BtnPowerVO(strCodes[i]);
					iBillStatus = PuPubVO.getInteger_NullAs(headers[i]
							.getIbillstatus(), new Integer(0));
					if (iBillStatus.intValue() == 1
							|| iBillStatus.intValue() == 2
							|| iBillStatus.intValue() == 3) {
						pVo.setFileDelEnable("false");
					}
					mapBtnPowerVo.put(strCodes[i], pVo);
				}
			}
		}
		if (strPks == null || strPks.length <= 0) {
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000066")/* "û����ȷ��ȡ���ݺ�,���ܽ����ĵ�����" */);
			return;
		}
		// �����ĵ�����Ի���
		nc.ui.scm.file.DocumentManager.showDM(this, ScmConst.PO_Pray, strPks,
				mapBtnPowerVo);
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000025")/*
															 * @res "�ĵ�����ɹ�"
															 */);
	}

	/**
	 * ��������:��ҳ
	 */
	private void onFirst() {
		/* ���ص��� */
		final int iRollBackPos = m_nPresentRecord;
		m_nPresentRecord = 0;
		setVoToBillCard(iRollBackPos, m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000248")/* "����" */);
		/* ���ð�ť�߼� */
		setButtonsCard();
		getBillCardPanel().setEnabled(false);
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000026")/*
															 * @res "�ɹ���ʾ��ҳ"
															 */);
	}

	/**
	 * ��������:����
	 */
	private void onInsertLine() {
		int nSelectedRow = getBillCardPanel().getBillTable().getSelectedRow();
		if (!getBillCardPanel().insertLine()) {
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("40040101",
					"UPP40040101-000068")/* "����" */, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000059")/* "û��ѡ���빺�������У�" */);
			return;
		}
		/* ������Ŀ�Զ�Э�� */
		nc.ui.pu.pub.PuTool.setBodyProjectByHeadProject(getBillCardPanel(),
				"cprojectidhead", "cprojectid", "cprojectname",
				nc.vo.scm.pu.PuBillLineOprType.INSERT);
		/* �����к� */
		BillRowNo.insertLineRowNo(getBillCardPanel(),
				nc.vo.scm.pu.BillTypeConst.PO_PRAY, "crowno");

		if (nSelectedRow >= 0) {
			getBillCardPanel().setBodyValueAt(m_sLoginCorpId, nSelectedRow,
					"pk_reqcorp");
			getBillCardPanel().setBodyValueAt(
					getClientEnvironment().getCorporation().getUnitname(),
					nSelectedRow, "reqcorpname");
			getBillCardPanel().setBodyValueAt(m_sLoginCorpId, nSelectedRow,
					"pk_purcorp");
			getBillCardPanel().setBodyValueAt(
					getClientEnvironment().getCorporation().getUnitname(),
					nSelectedRow, "purcorpname");
		}

		// setAssisUnitEditState();
		m_bCancel = false;
		m_nUIState = 0;
		updateButtonsAll();

		// ������ʾ czp 20050303 ����
		PraybillVO voCurr = (PraybillVO) getBillCardPanel().getBillValueVO(
				PraybillVO.class.getName(), PraybillHeaderVO.class.getName(),
				PraybillItemVO.class.getName());
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), voCurr);
		showHintMessage(m_lanResTool.getStrByID("common", "UCH038")/*
																	 * @res
																	 * "�����гɹ�"
																	 */);

	}

	/**
	 * ��������:ĩҳ
	 */
	private void onLast() {
		/* ���ص��� */
		final int iRollBackPos = m_nPresentRecord;
		m_nPresentRecord = m_VOs.length - 1;
		setVoToBillCard(iRollBackPos, m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000177")/* "ĩ��" */);
		/* ���ð�ť�߼� */
		setButtonsCard();
		getBillCardPanel().setEnabled(false);
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000029")/*
															 * @res "�ɹ���ʾĩҳ"
															 */);
	}

	/**
	 * ��������:��Ƭ���б�
	 */
	private void onList() {
		showHintMessage("");
		int iSortCol = getBillCardPanel().getBillModel().getSortColumn();
		boolean bSortAsc = getBillCardPanel().getBillModel().isSortAscending();
		getBillCardPanel().setVisible(false);
		if (!m_bListPanelLoaded) {
			add(getBillListPanel(), java.awt.BorderLayout.CENTER);
			m_bListPanelLoaded = true;
		}
		getBillListPanel().setVisible(true);
		if (m_bOnhandShowHidden) {
			remove(getSplitPanelBc());
			add(getBillListPanel(), java.awt.BorderLayout.CENTER);
		} else {
			add(getBillListPanel(), java.awt.BorderLayout.CENTER);
		}
		/* ���ز����ֶ� */
		/**
		 * v5 �����֯����ͷ��Ŀ����������ش���ɾ��
		 * getBillListPanel().hideHeadTableCol("cstoreorganization");
		 */
		// getBillListPanel().hideHeadTableCol("cbiztype");
		getBillListPanel().hideHeadTableCol("cdeptid");
		getBillListPanel().hideHeadTableCol("cpraypsn");
		getBillListPanel().hideHeadTableCol("coperator");
		getBillListPanel().hideHeadTableCol("cauditpsn");
		getBillListPanel().hideHeadTableCol("ccustomerid");
		if (m_VOs != null && m_VOs.length > 0) {
			/* ��ʾ��Ƭ״̬�ĵ�ǰ���� */
			Vector v = new Vector();
			for (int i = 0; i < m_VOs.length; i++) {
				v.addElement(m_VOs[i].getHeadVO());
			}
			PraybillHeaderVO[] hVO = new PraybillHeaderVO[v.size()];
			v.copyInto(hVO);

			PraybillItemVO[] bVO = m_VOs[m_nPresentRecord].getBodyVO();

			getBillListPanel().getBillListData().getHeadItem("ipraysource")
					.setWithIndex(true);
			m_comPraySource1 = (UIComboBox) getBillListPanel()
					.getBillListData().getHeadItem("ipraysource")
					.getComponent();
			m_comPraySource1.removeAllItems();
			m_comPraySource1.setTranslate(true);
			m_comPraySource1.addItem("MRP");
			m_comPraySource1.addItem("MO");
			m_comPraySource1.addItem("SFC");
			m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000458")/* "���۶���" */);
			m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000459")/* "��涩����" */);
			m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000460")/* "�ֹ�¼��" */);
			m_comPraySource1.addItem("DRP");
			m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000461")/* "��������" */);
			m_comPraySource1.addItem(m_lanResTool.getStrByID("4004pub",
					"UPP4004pub-000204") /* "������������" */);
			m_comPraySource1.addItem(m_lanResTool.getStrByID("40040101",
					"UPT40040101-001114") /* "��������" */);

			getBillListPanel().getBillListData().getHeadItem("ipraytype")
					.setWithIndex(true);
			m_comPrayType1 = (UIComboBox) getBillListPanel().getBillListData()
					.getHeadItem("ipraytype").getComponent();
			m_comPrayType1.removeAllItems();
			m_comPrayType1.setTranslate(true);
			m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000454")/* "����ߴ���" */);
			m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000455")/* "����߲�����" */);
			m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000456")/* "�ɹ�" */);
			m_comPrayType1.addItem(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000457")/* "��Э" */);

			Integer nTemp1 = (Integer) m_VOs[m_nPresentRecord].getParentVO()
					.getAttributeValue("ipraysource");
			Integer nTemp2 = (Integer) m_VOs[m_nPresentRecord].getParentVO()
					.getAttributeValue("ipraytype");
			if (nTemp1 != null)
				m_comPraySource1.setSelectedIndex(nTemp1.intValue());
			if (nTemp2 != null)
				m_comPrayType1.setSelectedIndex(nTemp2.intValue());

			getBillListPanel().getHeadBillModel().setBodyDataVO(hVO);
			getBillListPanel().getHeadBillModel().execLoadFormula();
			getBillListPanel().getBodyBillModel().setBodyDataVO(bVO);
			nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillListPanel());
			getBillListPanel().getBodyBillModel().execLoadFormula();
			getBillListPanel().getHeadBillModel().updateValue();
			getBillListPanel().getBodyBillModel().updateValue();
			getBillListPanel().updateUI();
			/* �б���ʾ���ݴ��� */
			getBillListPanel().getHeadBillModel().setRowState(m_nPresentRecord,
					BillModel.SELECTED);
			getBillListPanel().getHeadTable().setRowSelectionInterval(
					m_nPresentRecord, m_nPresentRecord);
			/* ��ʾ��Դ��Ϣ���б� */
			PuTool.loadSourceInfoAll(getBillListPanel(), BillTypeConst.PO_PRAY);
			/* ��ʾ��ע */
			int mVOsLength = m_VOs.length;
			for (int i = 0; i < mVOsLength; i++) {
				getBillListPanel().getHeadBillModel().setValueAt(
						hVO[i].getVmemo(), i, "vmemo");
			}
			if (iSortCol >= 0) {
				getBillListPanel().getBodyBillModel().sortByColumn(iSortCol,
						bSortAsc);
			}
		} else {
			getBillListPanel().getHeadBillModel().clearBodyData();
			getBillListPanel().getBodyBillModel().clearBodyData();
			getBillListPanel().updateUI();
		}
		/* �б�ť�߼� */
		setButtonsList();
		m_nUIState = 1;
		getBillListPanel().setEnabled(false);
		if (m_VOs != null && m_VOs.length > 0)
			setButtonsRevise(isCurAuditOP(m_VOs[m_nPresentRecord]));
		else
			setButtonsRevise(false);
		showHintMessage(m_lanResTool.getStrByID("common", "UCH022")/*
																	 * @res
																	 * "�б���ʾ"
																	 */);
	}

	/**
	 * ��������:�б�״̬��ѯ
	 */
	private void onListQuery() {
		showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000360")/* @res"��ʼ��ѯ..." */);
		// ��ʾ�Ի���
		getQueryDlg().showModal();
		if (getQueryDlg().isCloseOK()) {
			m_bQueried = true;
			onListRefresh();
		}

		showHintMessage(m_lanResTool.getStrByID("common", "UCH009")/*
																	 * @res
																	 * "��ѯ���"
																	 */);
	}

	/* �б�ˢ�� */
	private void onListRefresh() {
		// ��ȡ�빺����ѯ����
		ConditionVO[] conditionVO = getQueryDlg().getConditionVO();
		String strSubSql = getQueryDlg().getSubSQL();
		try {
			m_VOs = PraybillHelper.queryAll(getQueryDlg()
					.getSelectedCorpIdString(), conditionVO, getQueryDlg()
					.getStatusCndStr(), null, strSubSql, getClientEnvironment()
					.getUser().getPrimaryKey());
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000061")/* @res"�빺������" */, e
					.getMessage());
			return;
		}
		if (m_VOs != null && m_VOs.length > 0) {
			// ���û���λ��
			m_nPresentRecord = 0;
			try {
				m_VOs[m_nPresentRecord] = PrTool
						.getRefreshedVO(m_VOs[m_nPresentRecord]);
				freshHeadCproject(new PraybillVO[] { m_VOs[m_nPresentRecord] });
				// ����������
				new nc.ui.scm.pub.FreeVOParse().setFreeVO(
						m_VOs[m_nPresentRecord].getBodyVO(), "vfree", "vfree",
						"cbaseid", "cmangid", true);
			} catch (Exception be) {
				if (be instanceof BusinessException)
					MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000422")/* "ҵ���쳣" */,
							be.getMessage());
				return;
			}
			// ���ز�ѯ���
			Vector v = new Vector();
			for (int i = 0; i < m_VOs.length; i++)
				v.addElement(m_VOs[i].getHeadVO());
			PraybillHeaderVO[] hVO = new PraybillHeaderVO[v.size()];
			v.copyInto(hVO);

			initListComboBox();

			Integer nTemp1 = (Integer) m_VOs[0].getParentVO()
					.getAttributeValue("ipraysource");
			Integer nTemp2 = (Integer) m_VOs[0].getParentVO()
					.getAttributeValue("ipraytype");
			m_comPraySource1.setSelectedIndex(nTemp1.intValue());
			m_comPrayType1.setSelectedIndex(nTemp2.intValue());

			// ѡ���һ��
			getBillListPanel().getHeadBillModel().setBodyDataVO(hVO);
			getBillListPanel().getHeadBillModel().execLoadFormula();
			getBillListPanel().getHeadBillModel().updateValue();

			// �б���ʾ���ݴ���
			getBillListPanel().getHeadBillModel().setRowState(m_nPresentRecord,
					BillModel.SELECTED);
			getBillListPanel().getHeadTable().setRowSelectionInterval(
					m_nPresentRecord, m_nPresentRecord);
			Logger.debug("$$---$---$$:" + m_nPresentRecord);
			getBillListPanel().updateUI();

			// // ��ʾ��ע
			// for (int i = 0; i < hVO.length; i++) {
			// getBillListPanel().getHeadBillModel().setValueAt(hVO[i].getVmemo(),
			// i,
			// "vmemo");
			// }
		} else {
			m_VOs = null;
			// �޷��ز�ѯ���
			MessageDialog
					.showHintDlg(this, m_lanResTool.getStrByID("40040101",
							"UPP40040101-000055")/* "�빺����ѯ" */, m_lanResTool
							.getStrByID("40040101", "UPP40040101-000056")/* "û�з����������빺����" */);
			// �������
			getBillListPanel().getHeadBillModel().clearBodyData();
			getBillListPanel().getBodyBillModel().clearBodyData();
			getBillListPanel().updateUI();
		}
		// �б�ť�߼�
		setButtonsList();
		m_nUIState = 1;
		updateButtonsAll();
		getBillCardPanel().setEnabled(false);

		showHintMessage(m_lanResTool.getStrByID("common", "UCH007")/*
																	 * @res
																	 * "ˢ�³ɹ�"
																	 */);
	}

	/**
	 * ����������
	 */
	private void onLinkQuery() {
		PraybillVO vo = m_VOs[m_nPresentRecord];
		if (vo == null || vo.getParentVO() == null)
			return;
		nc.ui.scm.sourcebill.SourceBillFlowDlg soureDlg = new nc.ui.scm.sourcebill.SourceBillFlowDlg(
				this, nc.vo.scm.pu.BillTypeConst.PO_PRAY,
				((PraybillHeaderVO) vo.getParentVO()).getPrimaryKey(), null,
				getClientEnvironment().getUser().getPrimaryKey(),
				((PraybillHeaderVO) vo.getParentVO()).getVpraycode());
		soureDlg.showModal();
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000019")/*
															 * @res "����ɹ�"
															 */);
	}

	/**
	 * ��������:�������˵�
	 * 
	 * @param event
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void onMenuItemClick(java.awt.event.ActionEvent event) {
		UIMenuItem menuItem = (UIMenuItem) event.getSource();

		if (menuItem.equals(getBillCardPanel().getAddLineMenuItem()))
			onAppendLine(getBillCardPanel(), this);

		if (menuItem.equals(getBillCardPanel().getCopyLineMenuItem())) {
			m_bIsSubMenuPressed = true;
			onCopyLine();
			m_bIsSubMenuPressed = false;
		}

		if (menuItem.equals(getBillCardPanel().getDelLineMenuItem()))
			onDeleteLine();

		if (menuItem.equals(getBillCardPanel().getInsertLineMenuItem()))
			onInsertLine();

		if (menuItem.equals(getBillCardPanel().getPasteLineMenuItem()))
			onPasteLine();
		if (menuItem.equals(getBillCardPanel().getPasteLineToTailMenuItem()))
			onPasteLineToTail();

		// �����к�
		if (menuItem.equals(m_miReSortRowNo)) {
			onReSortRowNo();
		}
		if (menuItem.equals(m_miCardEdit)) {
			onCardEdit();
		}
	}

	/*
	 * �����к�
	 */
	private void onReSortRowNo() {
		PuTool.resortRowNo(getBillCardPanel(), ScmConst.PO_Pray, "crowno");
		showHintMessage(NCLangRes.getInstance().getStrByID("common",
				"SCMCOMMON000000284")/*
										 * @res "�����кųɹ�"
										 */);
	}

	private void onCardEdit() {
		// ����˴�����ΪURREF,��ô��Ƭ�༭ʱ,��ʾ���������յĽ��,
		// ��ʱע�͵��˴�����,���ܻᵼ��δ֪���⣬һ�����֣�����donggq��ϵ��
		// by donggq v56
		for (int i = 1; i <= 20; i++) {
			if (getBillCardPanel().getBodyItem("vdef" + i) != null
					&& getBillCardPanel().getBodyItem("vdef" + i).getDataType() == BillItem.UFREF) {
				getBillCardPanel().getBodyItem("vdef" + i).setDataType(
						BillItem.USERDEF);
			}
		}
		// ������ÿ����֯Ȩ�޺󣬿������繫˾�Ŀ����֯
		int curRow = getBillCardPanel().getBillTable().getSelectedRow();
		if (curRow == -1)
			curRow = 0;// ��ʼΪѡ���һ��
		Object oTemp = getBillCardPanel().getBodyValueAt(curRow, "pk_reqcorp");
		if (oTemp != null) {
			UIRefPane paneReqStoOrg = ((UIRefPane) getBillCardPanel()
					.getBodyItem("reqstoorgname").getComponent());
			paneReqStoOrg.getRefModel().setPk_corp(oTemp.toString());
		}
		// ���ñ�ע
		boolean isRef = false;
		int dataType = -1;
		if (getBillCardPanel().getBodyItem("vmemo").getComponent() instanceof UIRefPane) {
			isRef = true;
			dataType = getBillCardPanel().getBodyItem("vmemo").getDataType();
			getBillCardPanel().getBodyItem("vmemo")
					.setDataType(BillItem.STRING);
		}
		getBillCardPanel().startRowCardEdit();
		if (isRef)
			getBillCardPanel().getBodyItem("vmemo").setDataType(dataType);
	}

	/**
	 * ��������:����
	 */
	private void onModify(boolean bListShowing, boolean bRevise) {
		reviseSave = bRevise;

		if (bListShowing) {
			// ���б�תΪ��Ƭ
			int rowCount = getBillListPanel().getHeadBillModel().getRowCount();
			int iList;
			int iCard;
			for (int i = 0; i < rowCount; i++) {
				if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
					m_nPresentRecord = i;
					if (getBillListPanel().getHeadBillModel() != null
							&& getBillListPanel().getHeadBillModel()
									.getSortIndex() != null) {
						iList = m_nPresentRecord;
						iCard = getBillListPanel().getHeadBillModel()
								.getSortIndex()[iList];
						m_nPresentRecord = iCard;
					}
					break;
				}
			}
			if (m_nPresentRecord < 0) {
				MessageDialog
						.showHintDlg(this, m_lanResTool.getStrByID("common",
								"UC001-0000045")/* @res"�޸�" */, m_lanResTool
								.getStrByID("40040101", "UPP40040101-000446")/* @res"û��ѡ���빺����ͷ�У�" */);
				return;
			}

		}
		// �Ѿ��������빺�������޸�
		int nBillStatus = 0;
		if (m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus() != null) {
			nBillStatus = m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus()
					.intValue();
		}
		if (nBillStatus == 3) {
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("40040101",
					"UPP40040101-000467")/* "�빺���޸�" */,
					m_VOs[m_nPresentRecord].getHeadVO().getVpraycode()
							+ m_lanResTool.getStrByID("40040101",
									"UPP40040101-000468")/*
															 * @res "
															 * ���빺���Ѿ������������޸ģ���ˢ�½�������"
															 */);
			return;
		}
		if (nBillStatus == 1) {
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("40040101",
					"UPP40040101-000467")/* "�빺���޸�" */,
					m_VOs[m_nPresentRecord].getHeadVO().getVpraycode()
							+ m_lanResTool.getStrByID("40040101",
									"UPP40040101-000469")/*
															 * @res "
															 * ���빺���Ѿ��رգ������޸ģ���ˢ�½�������"
															 */);
			return;
		}
		if (m_bOnhandShowHidden) {
			onCardOnHandShowHidden();
		}
		getBillListPanel().setVisible(false);
		getBillCardPanel().setVisible(true);

		getBillCardPanel().setEnabled(true);
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000030")/*
															 * @res "�����޸�"
															 */);
		// ***************fangy add 2002-12-04 begin****************/
		int rowCount = getBillCardPanel().getRowCount();
		for (int i = 0; i < rowCount; i++) {
			setVProduceNumEditState(i);
		}
		Integer nTemp1 = (Integer) m_VOs[m_nPresentRecord].getParentVO()
				.getAttributeValue("ipraysource");
		Integer nTemp2 = (Integer) m_VOs[m_nPresentRecord].getParentVO()
				.getAttributeValue("ipraytype");
		m_comPraySource.setSelectedIndex(nTemp1.intValue());
		m_comPrayType.setSelectedIndex(PuPubVO
				.getString_TrimZeroLenAsNull(nTemp2) == null ? 2 : nTemp2
				.intValue());
		// ��ʾ��Ƭ����
		getBillCardPanel().setBillValueVO(m_VOs[m_nPresentRecord]);
		nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillCardPanel());
		getBillCardPanel().getBillModel().execLoadFormula();
		// ������Դ������Ϣ
		nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanel(),
				nc.vo.scm.pu.BillTypeConst.PO_PRAY);
		getBillCardPanel().updateValue();
		getBillCardPanel().updateUI();
		// ��ʾ��ע
		if (getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane) {
			UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel().getHeadItem(
					"vmemo").getComponent();
			nRefPanel3.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVmemo());
		}
		// ***************fangy add 2002-12-04 end******************/

		setBillItemStatus();

		setPartNoEditable(getBillCardPanel());
		// setAssisUnitEditState();
		m_bEdit = true;
		m_bCancel = false;
		setButtonsCardModify();
		m_nUIState = 0;
		updateButtonsAll();

		/** �����Ҽ��˵��밴ť�顰�в�����Ȩ����ͬ */
		setPopMenuBtnsEnable();
		// ���˴������,��֤��ҵ�����������д���ҵ������ʱ�滻�����������д�������
		String sql = " and ( 1 =1 )";
		UIRefPane refCinventorycode = (UIRefPane) (getBillCardPanel()
				.getBodyItem("cinventorycode").getComponent());
		refCinventorycode.getRefModel().addWherePart(sql);
		// �ù�굽��ͷ��һ���ɱ༭��Ŀ
		getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000030")/*
															 * @res "�����޸�"
															 */);

		// ��־ƽ ������������������������ʾ����
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		// �ӵ�ǰ����ȡֵ����Ϊ������������뻺�治һ�£�ficr.setFreeItemRenderer(getBillCardPanel(),
		// m_VOs[m_nPresentRecord]);
		PraybillVO voCurr = (PraybillVO) getBillCardPanel().getBillValueVO(
				PraybillVO.class.getName(), PraybillHeaderVO.class.getName(),
				PraybillItemVO.class.getName());
		ficr.setFreeItemRenderer(getBillCardPanel(), voCurr);
		getBillCardPanel().updateUI();
		// �ù�굽��ͷ��һ���ɱ༭��Ŀ
		getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);

		// �Ƿ�ֱ��
		if (getBillCardPanel().getHeadItem("isdirecttransit") != null) {
			getBillCardPanel().getHeadItem("isdirecttransit").setEnabled(false);
		}
		// ���ɺ�ͬ����
		if (getBillCardPanel().getBodyItem("ngenct") != null) {
			getBillCardPanel().getBodyItem("ngenct").setEnabled(false);
		}
		// ��������޶�(���޸Ĳ���),�����������Ѿ�����,��������������Ϣ v56
		if (!isRevise
				&& !reviseSave
				&& getBillCardPanel().getHeadItem("cauditpsn") != null
				&& PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
						.getHeadItem("cauditpsn").getValue()) != null) {
			getBillCardPanel().getTailItem("cauditpsn").setValue(null);
			getBillCardPanel().getTailItem("dauditdate").setValue(null);
			getBillCardPanel().getTailItem("taudittime").setValue(null);
		}
		//
		showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000534")/*
										 * @res "�༭����"
										 */);
	}

	/**
	 * ���õ�Ԫ��༭״̬
	 */
	private void setBillItemStatus() {
		// �޸��빺��ʱ����������Ӧ�ÿ����޸ġ�
		BillItem ddemanddate = getBillCardPanel().getBodyItem("ddemanddate");
		if (ddemanddate != null) {
			ddemanddate.setEnabled(true);
		}
		// yux ��Ŀ�����޸�
		BillItem cprojectname = getBillCardPanel().getBodyItem("cprojectname");
		if (cprojectname != null) {
			cprojectname.setEnabled(true);
		}
		BillItem cwarehousename = getBillCardPanel().getBodyItem(
				"cwarehousename");
		if (cwarehousename != null) {
			cwarehousename.setEnabled(true);
		}
		BillItem reqstoorgname = getBillCardPanel()
				.getBodyItem("reqstoorgname");
		if (reqstoorgname != null) {
			reqstoorgname.setEnabled(true);
		}
	}

	/**
	 * ���ߣ���־ƽ ���ܣ����õ��ݿ�Ƭ�Ҽ��˵��в����밴ť�顰�в�����Ȩ����ͬ �������� ���أ��� ���⣺�� ���ڣ�(2004-11-26
	 * 10:06:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */

	private void setPopMenuBtnsEnable() {
		// û�з����в���Ȩ��
		if (m_btnLines == null || m_btnLines.getChildCount() == 0) {
			getBillCardPanel().getBodyPanel().getMiAddLine().setEnabled(false);
			getBillCardPanel().getBodyPanel().getMiCopyLine().setEnabled(false);
			getBillCardPanel().getBodyPanel().getMiDelLine().setEnabled(false);
			getBillCardPanel().getBodyPanel().getMiInsertLine().setEnabled(
					false);
			getBillCardPanel().getBodyPanel().getMiPasteLine()
					.setEnabled(false);
			getBillCardPanel().getBodyPanel().getMiPasteLineToTail()
					.setEnabled(false);
			m_miReSortRowNo.setEnabled(false);
			m_btnCardEdit.setEnabled(false);
		}
		// �����в���Ȩ��
		else {
			getBillCardPanel().getBodyPanel().getMiAddLine().setEnabled(
					isPowerBtn(m_btnAddLine.getCode()));
			getBillCardPanel().getBodyPanel().getMiCopyLine().setEnabled(
					isPowerBtn(m_btnCpyLine.getCode()));
			getBillCardPanel().getBodyPanel().getMiDelLine().setEnabled(
					isPowerBtn(m_btnDelLine.getCode()));
			getBillCardPanel().getBodyPanel().getMiInsertLine().setEnabled(
					isPowerBtn(m_btnInsLine.getCode()));
			getBillCardPanel().getBodyPanel().getMiPasteLine().setEnabled(
					isPowerBtn(m_btnPstLine.getCode()));
			// ճ������β��ճ���������߼���ͬ
			getBillCardPanel().getBodyPanel().getMiPasteLineToTail()
					.setEnabled(isPowerBtn(m_btnPstLineTail.getCode()));
			//
			m_miReSortRowNo.setEnabled(isPowerBtn(m_btnReSortRowNo.getCode()));
			m_btnCardEdit.setEnabled(isPowerBtn(m_btnCardEdit.getCode()));
			m_miCardEdit.setEnabled(isPowerBtn(m_btnCardEdit.getCode()));
		}
	}

	/**
	 * ��������:��ҳ
	 */
	private void onNext() {
		/* ���ص��� */
		final int iRollBackPos = m_nPresentRecord;
		m_nPresentRecord++;
		setVoToBillCard(iRollBackPos, m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000281")/* "����" */);
		/* ���ð�ť�߼� */
		setButtonsCard();
		getBillCardPanel().setEnabled(false);
		//
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000028")/*
															 * @res "�ɹ���ʾ��һҳ"
															 */);

	}

	/**
	 * ��������:�򿪵�ǰ���빺��--�б�
	 */
	private void onOpenList() {
		this.showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000069")/* "���ڴ򿪵���..." */);
		m_VOs[m_nPresentRecord].getHeadVO().setCuserid(
				getClientEnvironment().getUser().getPrimaryKey());
		long tTime = System.currentTimeMillis();
		try {
			// ������Ա
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			m_VOs[m_nPresentRecord].setCurrOperator(strOpr);
			ArrayList keys = (ArrayList) PfUtilClient
					.processActionNoSendMessage(this, "OPEN",
							nc.vo.scm.pu.BillTypeConst.PO_PRAY,
							getClientEnvironment().getDate().toString(),
							m_VOs[m_nPresentRecord], null, null, null);
			m_VOs[m_nPresentRecord] = (PraybillVO) keys.get(0);
			// �򿪵���ʱ�ָ�������״̬
			m_VOs[m_nPresentRecord].getHeadVO().setIbillstatus(new Integer(3));
			// ���ݹرպ�����
			if (m_VOs[m_nPresentRecord] != null
					&& m_VOs[m_nPresentRecord].getBodyVO() != null) {
				int itemsLength = m_VOs[m_nPresentRecord].getBodyVO().length;
				UFDouble nMoney = null;
				UFDouble nPrayNum = null;
				UFDouble nSuggestPrice = null;

				for (int i = 0; i < itemsLength; i++) {
					nMoney = m_VOs[m_nPresentRecord].getBodyVO()[i].getNmoney();
					nPrayNum = m_VOs[m_nPresentRecord].getBodyVO()[i]
							.getNpraynum();
					nSuggestPrice = m_VOs[m_nPresentRecord].getBodyVO()[i]
							.getNsuggestprice();
					if (nMoney == null && nPrayNum != null
							&& nSuggestPrice != null) {
						nMoney = new UFDouble(nPrayNum.doubleValue()
								* nSuggestPrice.doubleValue());
					} else {
						nMoney = new UFDouble(0);
					}
					m_VOs[m_nPresentRecord].getBodyVO()[i].setNmoney(nMoney);
				}
			}
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000070")/* "�빺����" */, e
					.getMessage());
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000071")/* "��ʧ��" */);
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		Logger.debug("�빺����ʱ�䣺" + tTime + " ms!");
		m_nUIState = 0;
		//
		getBillListPanel().getHeadBillModel().setBodyRowVO(
				m_VOs[m_nPresentRecord].getHeadVO(), m_nPresentRecord);
		getBillListPanel().getHeadBillModel().execLoadFormula();
		getBillListPanel().getBodyBillModel().setBodyDataVO(
				m_VOs[m_nPresentRecord].getBodyVO());
		getBillListPanel().getBodyBillModel().execLoadFormula();
		updateUI();
		/* ˢ�°�ť״̬ */
		setButtonsList();
		//
		showHintMessage(m_lanResTool.getStrByID("common", "UCH012")/*
																	 * @res
																	 * "�Ѵ�"
																	 */);
		return;
	}

	/**
	 * ��������:�򿪵�ǰ���빺��
	 */
	private void onOpenCard() {
		this.showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000069")/* "���ڴ򿪵���..." */);
		m_VOs[m_nPresentRecord].getHeadVO().setCuserid(
				getClientEnvironment().getUser().getPrimaryKey());
		long tTime = System.currentTimeMillis();
		try {
			// ������Ա
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			m_VOs[m_nPresentRecord].setCurrOperator(strOpr);
			ArrayList keys = (ArrayList) PfUtilClient
					.processActionNoSendMessage(this, "OPEN",
							nc.vo.scm.pu.BillTypeConst.PO_PRAY,
							getClientEnvironment().getDate().toString(),
							m_VOs[m_nPresentRecord], null, null, null);
			m_VOs[m_nPresentRecord] = (PraybillVO) keys.get(0);
			// �򿪵���ʱ�ָ�������״̬
			m_VOs[m_nPresentRecord].getHeadVO().setIbillstatus(new Integer(3));
			// ���ݹرպ�����
			if (m_VOs[m_nPresentRecord] != null
					&& m_VOs[m_nPresentRecord].getBodyVO() != null) {
				int itemsLength = m_VOs[m_nPresentRecord].getBodyVO().length;
				UFDouble nMoney = null;
				UFDouble nPrayNum = null;
				UFDouble nSuggestPrice = null;

				for (int i = 0; i < itemsLength; i++) {
					nMoney = m_VOs[m_nPresentRecord].getBodyVO()[i].getNmoney();
					if (nMoney != null)
						continue;
					nPrayNum = m_VOs[m_nPresentRecord].getBodyVO()[i]
							.getNpraynum();
					nSuggestPrice = m_VOs[m_nPresentRecord].getBodyVO()[i]
							.getNsuggestprice();
					if (nMoney == null && nPrayNum != null
							&& nSuggestPrice != null) {
						nMoney = new UFDouble(nPrayNum.doubleValue()
								* nSuggestPrice.doubleValue());
					}
					m_VOs[m_nPresentRecord].getBodyVO()[i].setNmoney(nMoney);
				}
			}
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"40040101", "UPP40040101-000070")/* "�빺����" */, e
					.getMessage());
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000071")/* "��ʧ��" */);
			return;
		}
		tTime = System.currentTimeMillis() - tTime;
		Logger.debug("�빺����ʱ�䣺" + tTime + " ms!");
		m_nUIState = 0;
		/* ˢ�°�ť״̬ */
		setButtonsCard();
		/* ���ص��� */
		setVoToBillCard(m_nPresentRecord, m_lanResTool.getStrByID("40040101",
				"UPP40040101-000449")/* "����" */);
		//
		showHintMessage(m_lanResTool.getStrByID("common", "UCH012")/*
																	 * @res
																	 * "�Ѵ�"
																	 */);
		return;
	}

	/**
	 * ��������:ճ����
	 */
	private void onPasteLine() {
		/* ճ��ǰ������ */
		final int iOrgRowCount = getBillCardPanel().getRowCount();
		getBillCardPanel().pasteLine();

		/* ���ӵ����� */
		final int iPastedRowCount = getBillCardPanel().getRowCount()
				- iOrgRowCount;

		/* �����к� */
		BillRowNo.pasteLineRowNo(getBillCardPanel(),
				nc.vo.scm.pu.BillTypeConst.PO_PRAY, "crowno", iPastedRowCount);

		final int iEndRow = getBillCardPanel().getBillTable().getSelectedRow() - 1;
		final int iBeginRow = iEndRow - iPastedRowCount + 1;
		/* �������ʱ����Ҫ����Ϣ */
		for (int iRow = iBeginRow; iRow <= iEndRow; iRow++) {
			getBillCardPanel().getBillModel().setRowState(iRow, BillModel.ADD);
			getBillCardPanel().setBodyValueAt(null, iRow, "cpraybill_bid");
			getBillCardPanel().setBodyValueAt(null, iRow, "cpraybillid");
			getBillCardPanel().setBodyValueAt(null, iRow, "naccumulatenum");

			getBillCardPanel().setBodyValueAt(null, iRow, "ts");
		}
		// setAssisUnitEditState();
		m_bCancel = false;
		m_nUIState = 0;
		updateButtonsAll();
		// ������ʾ czp 20050303 ����
		PraybillVO voCurr = (PraybillVO) getBillCardPanel().getBillValueVO(
				PraybillVO.class.getName(), PraybillHeaderVO.class.getName(),
				PraybillItemVO.class.getName());
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), voCurr);
		showHintMessage(m_lanResTool.getStrByID("common", "UCH040")/*
																	 * @res
																	 * "ճ���гɹ�"
																	 */);

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-11-10 11:19:18)
	 */
	private void onPasteLineToTail() {
		final int iOldRowCnt = getBillCardPanel().getRowCount();
		getBillCardPanel().pasteLineToTail();
		final int iNewRowCnt = getBillCardPanel().getRowCount();
		if (iOldRowCnt > 0 && iNewRowCnt > 0 && iOldRowCnt == iNewRowCnt)
			showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
					"UPPSCMCommon-000424")/* "ճ���е���βδ�ɹ�,����ԭ��û�п������ݻ�δȷ��Ҫճ����λ��" */);
		else {
			for (int iRow = iOldRowCnt; iRow < iNewRowCnt; iRow++) {
				getBillCardPanel().getBillModel().setRowState(iRow,
						BillModel.ADD);
				getBillCardPanel().setBodyValueAt(null, iRow, "cpraybill_bid");
				getBillCardPanel().setBodyValueAt(null, iRow, "cpraybillid");
				getBillCardPanel().setBodyValueAt(null, iRow, "naccumulatenum");

				getBillCardPanel().setBodyValueAt(null, iRow, "ts");
			}
			String[] value = new String[] { String.valueOf(iNewRowCnt
					- iOldRowCnt) };
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000439", null, value));
			// �����к�
			BillRowNo.addLineRowNos(getBillCardPanel(),
					nc.vo.scm.pu.BillTypeConst.PO_PRAY, "crowno", iNewRowCnt
							- iOldRowCnt);
		}
		// ������ʾ czp 20050303 ����
		PraybillVO voCurr = (PraybillVO) getBillCardPanel().getBillValueVO(
				PraybillVO.class.getName(), PraybillHeaderVO.class.getName(),
				PraybillItemVO.class.getName());
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), voCurr);
		showHintMessage(m_lanResTool.getStrByID("common", "UCH040")/*
																	 * @res
																	 * "ճ���гɹ�"
																	 */);
	}

	/**
	 * ��������:��ҳ
	 */
	private void onPrevious() {
		/* ���ص��� */
		final int iRollBackPos = m_nPresentRecord;
		m_nPresentRecord--;
		setVoToBillCard(iRollBackPos, m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000232")/* "����" */);
		/* ���ð�ť�߼� */
		setButtonsCard();
		getBillCardPanel().setEnabled(false);
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000027")/*
															 * @res "�ɹ���ʾ��һҳ"
															 */);
	}

	/**
	 * ���ܣ��۸���֤�� ������ ���أ� ���⣺ ���ڣ�(2002-9-23 11:49:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private void onPriceInfos() {
		// ��ʾ��ѯ�Ի���
		if (m_priceDlg == null) {
			m_priceDlg = new QueryConditionClient(this);
			m_priceDlg.setTempletID("40040103000000000000");
			m_priceDlg.hideNormal();
		}
		m_priceDlg.showModal();
		// ��ѯ
		if (!m_priceDlg.isCloseOK()) {
			return;
		}
		String wherePart = m_priceDlg.getWhereSQL();
		// ȡ�ô����Ϣ
		ArrayList<String> listPurCorpId = new ArrayList<String>();
		ArrayList<String> listInvBaseId = new ArrayList<String>();
		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
			if (PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBodyValueAt(i, "cbaseid")) == null
					|| PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
							.getBodyValueAt(i, "pk_purcorp")) == null) {
				continue;
			}
			listPurCorpId.add(PuPubVO
					.getString_TrimZeroLenAsNull(getBillCardPanel()
							.getBodyValueAt(i, "pk_purcorp")));
			listInvBaseId.add(PuPubVO
					.getString_TrimZeroLenAsNull(getBillCardPanel()
							.getBodyValueAt(i, "cbaseid")));
		}
		String[] saPurCorpId = null;
		String[] saInvBaseId = null;
		PriceInfosVO[] vos = null;
		if (listPurCorpId.size() > 0) {
			saPurCorpId = (String[]) listPurCorpId
					.toArray(new String[listPurCorpId.size()]);
			saInvBaseId = (String[]) listInvBaseId
					.toArray(new String[listPurCorpId.size()]);
			try {
				vos = PraybillHelper.queryPriceInfos(saInvBaseId,
						getClientEnvironment().getDate(), saPurCorpId,
						wherePart);
			} catch (Exception e) {
				SCMEnv.out(e);
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000270")/* "��ʾ" */, e
						.getMessage());
			}
		}
		Hashtable<String, PriceInfosVO> hashPriceVos = new Hashtable<String, PriceInfosVO>();
		if (vos != null && vos.length > 0) {
			// ����۸�Ϊ�ջ�С�ڵ���0���򽫹�Ӧ�����
			int vosLength = vos.length;
			for (int i = 0; i < vosLength; i++) {
				if (vos[i].getQuota1() == null
						|| vos[i].getQuota1().doubleValue() <= 0) {
					vos[i].setVendor1(null);
					vos[i].setQuota1(null);
				}
			}
		}
		// ����ǰ���ݴ��ڶ����ͬ�������۸���֤Ҳ�ֶ�����ʾ��
		String strKey = null;
		if (vos != null && vos.length <= saInvBaseId.length) {
			int vosLength = vos.length;
			for (int i = 0; i < vosLength; i++) {
				strKey = vos[i].getpk_purcorp() + vos[i].getcbaseid();
				hashPriceVos.put(strKey, vos[i]);
			}
			vos = new PriceInfosVO[saInvBaseId.length];
			int cmangidsLength = saInvBaseId.length;
			for (int j = 0; j < cmangidsLength; j++) {
				strKey = saPurCorpId[j] + saInvBaseId[j];
				if (hashPriceVos.containsKey(strKey)) {
					vos[j] = (PriceInfosVO) hashPriceVos.get(strKey);
				} else {
					vos[j] = new PriceInfosVO();
					vos[j].setcmangid((String) getBillCardPanel()
							.getBodyValueAt(j, "cmangid"));
					vos[j].setpk_purcorp((String) getBillCardPanel()
							.getBodyValueAt(j, "pk_purcorp"));
					vos[j].setcbaseid((String) getBillCardPanel()
							.getBodyValueAt(j, "cbaseid"));
					vos[j].setInvcode((String) getBillCardPanel()
							.getBodyValueAt(j, "cinventorycode"));
					vos[j].setInvname((String) getBillCardPanel()
							.getBodyValueAt(j, "cinventoryname"));
					vos[j].setInvspec((String) getBillCardPanel()
							.getBodyValueAt(j, "cinventoryspec"));
					vos[j].setInvtype((String) getBillCardPanel()
							.getBodyValueAt(j, "cinventorytype"));
				}
			}
		}
		// �������¼�
		UFDouble[] newPrices = null;
		try {
			newPrices = PraybillHelper.queryNewPriceArray(saPurCorpId,
					saInvBaseId);
		} catch (Exception e) {
			SCMEnv.out(e);
			return;
		}
		if (newPrices != null && newPrices.length > 0) {
			Hashtable<String, UFDouble> hashPrices = new Hashtable<String, UFDouble>();
			for (int i = 0; i < saInvBaseId.length; i++) {
				strKey = saPurCorpId[i] + saInvBaseId[i];
				if (hashPrices.containsKey(strKey))
					continue;
				else {
					if (newPrices[i] == null) {
						newPrices[i] = new UFDouble(-1);
					}
					hashPrices.put(strKey, newPrices[i]);
				}
			}
			UFDouble newPrice;
			if (vos != null) {
				for (int j = 0; j < vos.length; j++) {
					strKey = saPurCorpId[j] + saInvBaseId[j];
					if (hashPrices.containsKey(strKey)) {
						newPrice = (UFDouble) hashPrices.get(strKey);
						if (newPrice.doubleValue() <= 0) {
							newPrice = null;
						}
						vos[j].setNewprice(newPrice);
					}
				}
			}
		}
		/* ��ʾ�۸���֤�� */
		PriceInfoDlg dlgPriceInfo = new PriceInfoDlg(this, vos, nPriceDecimal,
				getClientEnvironment());
		dlgPriceInfo.showModal();
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000031")/*
															 * @res "�ɹ���ʾ�۸���֤��"
															 */);
	}

	/**
	 * ��ѯ��ǰ��������״̬
	 */

	private void onQueryForAudit() {

		PraybillVO[] vos = m_VOs;
		// ��ǰ������ڵ���
		if (vos != null && vos.length > 0) {
			// ����õ��ݴ�����������״̬��ִ��������䣺
			nc.ui.pub.workflownote.FlowStateDlg approvestatedlg = new nc.ui.pub.workflownote.FlowStateDlg(
					this, "20", vos[m_nPresentRecord].getHeadVO()
							.getPrimaryKey());
			approvestatedlg.showModal();

			showHintMessage(m_lanResTool.getStrByID("common", "UCH035")/*
																		 * @res
																		 * "����״̬��ѯ�ɹ�"
																		 */);
		} else {
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000539")/*
											 * @res "����������"
											 */);
		}
	}

	/**
	 * ���ܣ�������ѯ ������(2002-10-31 19:45:39) �޸ģ�2003-04-21/czp/ͳһ�����۶Ի���
	 */
	private void onQueryInvOnHand() {
		PraybillVO voPara = null;
		PraybillItemVO item = null;
		PraybillItemVO[] items = null;
		/* ��Ƭ */
		if (getBillCardPanel().isVisible()) {
			voPara = (PraybillVO) getBillCardPanel().getBillValueVO(
					PraybillVO.class.getName(),
					PraybillHeaderVO.class.getName(),
					PraybillItemVO.class.getName());
			if (voPara == null || voPara.getParentVO() == null) {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000073")/* "δѡȡ����,���ܲ�ѯ����" */);
				return;
			}
			/* ��������ʱ���õ�ǰ��¼��˾ */
			if (voPara.getParentVO().getAttributeValue("pk_corp") == null
					|| voPara.getParentVO().getAttributeValue("pk_corp")
							.toString().trim().equals("")) {
				voPara.getParentVO().setAttributeValue("pk_corp",
						m_sLoginCorpId);
			}
			/* ������Ϣ�����Լ�� */
			int[] iSelRows = getBillCardPanel().getBillTable()
					.getSelectedRows();
			if (iSelRows != null && iSelRows.length > 0) {
				/* �õ��û�ѡȡ�ĵ�һ�� */
				item = (PraybillItemVO) getBillCardPanel().getBillModel()
						.getBodyValueRowVO(iSelRows[0],
								PraybillItemVO.class.getName());
			} else {
				/* �û�δѡ��ʱ��ȡ���ݵ�һ�� */
				items = (PraybillItemVO[]) getBillCardPanel().getBillModel()
						.getBodyValueVOs(PraybillItemVO.class.getName());
				if (items == null || items.length <= 0) {
					showHintMessage(m_lanResTool.getStrByID("40040101",
							"UPP40040101-000074")/* "��˾�����������������Ϣ������,���ܲ�ѯ����" */);
					return;
				}
				item = items[0];
			}
			/* ��Ϣ�����Լ�� */
			if (voPara.getParentVO().getAttributeValue("pk_corp") == null
					|| voPara.getParentVO().getAttributeValue("pk_corp")
							.toString().trim().equals("")
					|| item.getAttributeValue("cinventoryid") == null
					|| item.getAttributeValue("cinventoryid").toString().trim()
							.equals("")
					|| item.getAttributeValue("ddemanddate") == null
					|| item.getAttributeValue("ddemanddate").toString().trim()
							.equals("")) {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000074")/* "��˾�����������������Ϣ������,���ܲ�ѯ����" */);
				return;
			}
			/* �����VO��ʼ�������ô�����ѯ�Ի��� */
			voPara.setChildrenVO(new PraybillItemVO[] { item });
			if (saPkCorp == null) {
				try {
					IUserManageQuery myService = (IUserManageQuery) nc.bs.framework.common.NCLocator
							.getInstance().lookup(
									IUserManageQuery.class.getName());
					nc.vo.bd.CorpVO[] vos = myService
							.queryAllCorpsByUserPK(getClientEnvironment()
									.getUser().getPrimaryKey());
					if (vos == null || vos.length == 0) {
						Logger.debug("δ��ѯ����Ȩ�޹�˾��ֱ�ӷ���!");
						return;
					}
					final int iLen = vos.length;
					saPkCorp = new String[iLen];
					for (int i = 0; i < iLen; i++) {
						saPkCorp[i] = vos[i].getPrimaryKey();
					}
				} catch (Exception e) {
					MessageDialog
							.showErrorDlg(
									this,
									m_lanResTool.getStrByID("40040101",
											"UPP40040101-000075")/* "��ȡ��Ȩ�޹�˾�쳣" */,
									m_lanResTool.getStrByID("40040101",
											"UPP40040101-000076")/* "��ȡ��Ȩ�޹�˾ʱ�����쳣(��ϸ��Ϣ�μ�����̨��־)!" */);
					return;
				}
			}
			getAtpDlg().setPkCorps(saPkCorp);
			getAtpDlg().initData(voPara);
			getAtpDlg().showModal();
		}
		/* �б� */
		else if (getBillListPanel().isVisible()) {
			/* ��ͷ��Ϣ�����Լ�� */
			PraybillHeaderVO head = null;
			if (getBillListPanel().getHeadBillModel().getBodySelectedVOs(
					PraybillHeaderVO.class.getName()) == null
					|| getBillListPanel().getHeadBillModel()
							.getBodySelectedVOs(
									PraybillHeaderVO.class.getName()).length <= 0) {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000073")/* "δѡȡ����,���ܲ�ѯ����" */);
				return;
			}
			head = (PraybillHeaderVO) getBillListPanel().getHeadBillModel()
					.getBodySelectedVOs(PraybillHeaderVO.class.getName())[0];
			if (head == null
					|| head.getAttributeValue("pk_corp") == null
					|| head.getAttributeValue("pk_corp").toString().trim()
							.equals("")) {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000077")/* "δ��ȷ��˾,���ܲ�ѯ����" */);
				return;
			}
			/* ������Ϣ�����Լ�� */
			int[] iSelRows = getBillListPanel().getBodyTable()
					.getSelectedRows();
			if (iSelRows != null && iSelRows.length > 0) {
				/* �õ��û�ѡȡ�ĵ�һ�� */
				item = (PraybillItemVO) getBillListPanel().getBodyBillModel()
						.getBodyValueRowVO(iSelRows[0],
								PraybillItemVO.class.getName());
			} else {
				/* �û�δѡ��ʱ��ȡ���ݵ�һ�� */
				items = (PraybillItemVO[]) getBillListPanel()
						.getBodyBillModel().getBodyValueVOs(
								PraybillItemVO.class.getName());
				if (items == null || items.length <= 0) {
					showHintMessage(m_lanResTool.getStrByID("40040101",
							"UPP40040101-000074")/* "��˾�����������������Ϣ������,���ܲ�ѯ����" */);
					return;
				}
				item = items[0];
			}
			/* ��Ϣ�����Լ�� */
			if (item.getAttributeValue("cinventoryid") == null
					|| item.getAttributeValue("cinventoryid").toString().trim()
							.equals("")
					|| item.getAttributeValue("ddemanddate") == null
					|| item.getAttributeValue("ddemanddate").toString().trim()
							.equals("")) {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000074")/* "��˾�����������������Ϣ������,���ܲ�ѯ����" */);
				return;
			}
			/* �����VO��ʼ�������ô�����ѯ�Ի��� */
			voPara = new PraybillVO();
			voPara.setParentVO(head);
			voPara.setChildrenVO(new PraybillItemVO[] { item });
			if (saPkCorp == null) {
				try {
					IUserManageQuery myService = (IUserManageQuery) nc.bs.framework.common.NCLocator
							.getInstance().lookup(
									IUserManageQuery.class.getName());
					nc.vo.bd.CorpVO[] vos = myService
							.queryAllCorpsByUserPK(getClientEnvironment()
									.getUser().getPrimaryKey());
					if (vos == null || vos.length == 0) {
						Logger.debug("δ��ѯ����Ȩ�޹�˾��ֱ�ӷ���!");
						return;
					}
					final int iLen = vos.length;
					saPkCorp = new String[iLen];
					for (int i = 0; i < iLen; i++) {
						saPkCorp[i] = vos[i].getPrimaryKey();
					}
				} catch (Exception e) {
					MessageDialog
							.showErrorDlg(
									this,
									m_lanResTool.getStrByID("40040101",
											"UPP40040101-000075")/* "��ȡ��Ȩ�޹�˾�쳣" */,
									m_lanResTool.getStrByID("40040101",
											"UPP40040101-000076")/* "��ȡ��Ȩ�޹�˾ʱ�����쳣(��ϸ��Ϣ�μ�����̨��־)!" */);
					Logger.debug(e.getMessage());
					return;
				}
			}
			getAtpDlg().setPkCorps(saPkCorp);
			getAtpDlg().initData(voPara);
			getAtpDlg().showModal();
			showHintMessage(m_lanResTool.getStrByID("common",
					"4004COMMON000000032")/*
											 * @res "������ѯ���"
											 */);
		}
	}

	/*
	 * ��������:����
	 */
	private boolean onSave() {
		showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000078")/* "���濪ʼ..." */);
		nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
		timer.start("�빺���������onSave������ʼ");

		// ��ֹ�༭
		getBillCardPanel().stopEditing();
		// ���ǿ���
		filterNullLine();
		if (!checkDef7()) {
			return false;
		}

		// ���Ӷ�У�鹫ʽ��֧��,������ʾ��UAP���� since v501
		if (!getBillCardPanel().getBillData().execValidateFormulas()) {
			return false;
		}

		final int nRow = getBillCardPanel().getRowCount();
		PraybillVO VO = new PraybillVO(nRow);
		m_SaveVOs = new PraybillVO(nRow);
		getBillCardPanel().getBillValueVO(VO);
		// // Ϊ�������͸�ֵ
		// String Cbiztype = VO.getHeadVO().getCbiztype();
		// if (Cbiztype == null) {
		// if (m_bizButton.getTag() != null
		// && m_bizButton.getTag().trim() != "") {
		// VO.getHeadVO().setCbiztype(m_bizButton.getTag());
		// }
		// }
		// ����ǰ��Ч�Լ��
		if (!checkBeforeSave(VO))
			return false;
		if (!checkPraytype(VO.getBodyVO()))
			return false;

		timer.addExecutePhase("����ǰ��Ч�Լ��");

		if (m_bAdd) {

			// �����빺��(���������빺��)
			PraybillHeaderVO headVO = VO.getHeadVO();
			// ��ͷ����
			headVO.setNversion(new Integer(1));
			// ֧�ֽ�����ҵ�繫˾�����޸������󱣴棬��˾�����޸ĵ������޸�
			if (PuPubVO.getString_TrimZeroLenAsNull(headVO.getPk_corp()) == null) {
				headVO.setPk_corp(m_sLoginCorpId);
			}
			headVO.setIpraysource(new Integer(m_comPraySource
					.getSelectedIndex()));
			headVO.setIpraytype(new Integer(m_comPrayType.getSelectedIndex()));
			headVO.setCaccountyear(getClientEnvironment().getAccountYear());
			headVO.setIbillstatus(new Integer(0));

			headVO.setCauditpsn(null);
			headVO.setDauditdate(null);

			// �Ƶ���

			headVO.setCoperator(getClientEnvironment().getUser()
					.getPrimaryKey());
			// if (m_bizButton != null)
			// headVO.setCbiztype(m_bizButton.getTag());
			// // ����ʱ�����⴦��
			// if (isFrmCopy) {
			// headVO.setCbiztype(((UIRefPane) getBillCardPanel().getHeadItem(
			// "cbiztype").getComponent()).getRefModel().getPkValue());
			// }
			try {
				// ����ʱ�õ�ǰ����ԱΪ�Ƶ���
				headVO.setCoperator(getClientEnvironment().getUser()
						.getPrimaryKey());
			} catch (Exception e) {
				Logger.debug(e.getMessage());
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000355")/* "��ͷ����" */, e
						.getMessage());
				showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000010")/* "����ʧ��" */);
				return false;
			}
			if (getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane) {
				UIRefPane nRefPanel = (UIRefPane) getBillCardPanel()
						.getHeadItem("vmemo").getComponent();
				UITextField vMemoField = nRefPanel.getUITextField();
				headVO.setVmemo(vMemoField.getText());
			}
			// ��������
			PraybillItemVO[] bodyVO = VO.getBodyVO();
			Object oTemp;
			for (int i = 0; i < bodyVO.length; i++) {
				bodyVO[i].setPk_corp(m_sLoginCorpId);

				oTemp = getBillCardPanel().getBodyValueAt(i, "vmemo");
				if (oTemp != null && oTemp.toString().length() > 0)
					bodyVO[i].setVmemo((String) oTemp);
				else
					bodyVO[i].setVmemo(null);
			}
			// ������ʱVO
			m_SaveVOs.setChildrenVO(bodyVO);

		} else {
			// �޸��빺��
			PraybillHeaderVO headVO = VO.getHeadVO();
			Logger.debug("BCoperator=" + headVO.getCoperator());
			// �ӻ����������Ƶ���
			if (m_VOs != null && m_VOs[m_nPresentRecord] != null
					&& m_VOs[m_nPresentRecord].getHeadVO() != null
					&& !isAllowedModifyByOther && !isRevise) {
				headVO.setCoperator(getClientEnvironment().getUser()
						.getPrimaryKey());
			}
			Logger.debug("Coperator=" + headVO.getCoperator());
			Logger.debug("isAllowedModifyByOther=" + isAllowedModifyByOther);
			// ��ͷ����
			headVO.setPk_corp(m_sLoginCorpId);
			headVO.setIpraysource(new Integer(m_comPraySource
					.getSelectedIndex()));
			headVO.setIpraytype(new Integer(m_comPrayType.getSelectedIndex()));

			// ������޸ı��棬״̬���䣬Ӧ�����������У�billstatus=2
			if (!isRevise && headVO.getIbillstatus() != null
					&& headVO.getIbillstatus().intValue() != 2) {
				headVO.setIbillstatus(new Integer(0));
			}
			// else��δ�������ȱ�ݣ�V60���Կ����Ż����߼�
			else {
				headVO.setIbillstatus(new Integer(2));
			}
			headVO.setPrimaryKey((String) m_VOs[m_nPresentRecord].getHeadVO()
					.getAttributeValue("cpraybillid"));
			headVO.setCaccountyear(getClientEnvironment().getAccountYear());
			headVO.setHeadEditStatus(2);

			if (!isRevise) {
				headVO.setCauditpsn(null);
				headVO.setDauditdate(null);
			}
			headVO.setCoperatoridnow(getClientEnvironment().getUser()
					.getPrimaryKey());
			if (getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane) {
				UIRefPane nRefPanel = (UIRefPane) getBillCardPanel()
						.getHeadItem("vmemo").getComponent();
				UITextField vMemoField = nRefPanel.getUITextField();
				headVO.setVmemo(vMemoField.getText());
			}
			// ��������
			PraybillItemVO[] bodyVO = VO.getBodyVO();
			UFDouble d;
			Object oTemp;
			for (int i = 0; i < bodyVO.length; i++) {
				bodyVO[i].setPk_corp(m_sLoginCorpId);
				if (bodyVO[i].getStatus() == VOStatus.NEW) {
					bodyVO[i].setBodyEditStatus(1);
				} else if (bodyVO[i].getStatus() == VOStatus.UPDATED) {
					bodyVO[i].setBodyEditStatus(2);
				} else if (bodyVO[i].getStatus() == VOStatus.DELETED) {
					bodyVO[i].setBodyEditStatus(3);
				}

				d = bodyVO[i].getNsuggestprice();
				if (d == null || d.toString().length() == 0)
					bodyVO[i].setNsuggestprice(new UFDouble(0.0));
				d = bodyVO[i].getNaccumulatenum();
				if (d == null || d.toString().length() == 0)
					bodyVO[i].setNaccumulatenum(new UFDouble(0.0));
				oTemp = getBillCardPanel().getBodyValueAt(i, "vmemo");
				if (oTemp != null && oTemp.toString().length() > 0)
					bodyVO[i].setVmemo((String) oTemp);
				else
					bodyVO[i].setVmemo(null);

			}
			// ���Ʊ���
			Vector vData = new Vector();
			for (int i = 0; i < bodyVO.length; i++) {
				vData.addElement(bodyVO[i]);
			}
			// ����ɾ���ı���
			if (getBillCardPanel().getBillModel().getBodyValueChangeVOs(
					PraybillItemVO.class.getName()) != null) {
				int changeVOsLength = getBillCardPanel().getBillModel()
						.getBodyValueChangeVOs(PraybillItemVO.class.getName()).length;
				for (int i = 0; i < changeVOsLength; i++) {
					if (getBillCardPanel().getBillModel()
							.getBodyValueChangeVOs(
									PraybillItemVO.class.getName())[i]
							.getStatus() == nc.ui.pub.bill.BillModel.DELETE) {
						vData.addElement(getBillCardPanel().getBillModel()
								.getBodyValueChangeVOs(
										PraybillItemVO.class.getName())[i]);
					}
				}
			}
			PraybillItemVO[] v = new PraybillItemVO[vData.size()];
			vData.copyInto(v);
			for (int i = vData.size() - 1; i >= nRow; i--) {
				v[i].setBodyEditStatus(3); // ����ɾ��״̬
			}
			// ����ɾ�������б�������Ϊ��ǰ�ı���
			VO.setChildrenVO(v);
			// ������ʱVO
			m_SaveVOs.setChildrenVO(VO.getChildrenVO());
		}

		ArrayList arrReturnFromBs = null;
		try {
			/* �Ƿ���Ҫ���˵��ݺ�:�������ֹ�¼�뵥�ݺ� */
			if (m_bAdd) {
				Object sPraybillCode = VO.getParentVO().getAttributeValue(
						"vpraycode");
				if (VO.getParentVO() != null && sPraybillCode != null
						&& sPraybillCode.toString().trim().length() > 0) {
				}
			}
			/* ���� */
			/* ������Ա(�������������޸�) */
			String strOpr = getClientEnvironment().getUser().getPrimaryKey();
			VO.setCurrOperator(strOpr);
			VO.getHeadVO().setCuserid(strOpr);
			m_SaveVOs.setParentVO(VO.getParentVO());
			/* ֧�ֹ�Ӧ�̺�׼��� */
			ArrayList aryUserObj = new ArrayList();
			aryUserObj.add(new Integer(1));
			aryUserObj.add("cvendormangid");
			timer.addExecutePhase("����ǰ��׼������");

			if (!reviseSave) {
				arrReturnFromBs = (ArrayList) PfUtilClient
						.processActionNoSendMessage(this, "SAVEBASE",
								nc.vo.scm.pu.BillTypeConst.PO_PRAY,
								getClientEnvironment().getDate().toString(),
								VO, aryUserObj, null, null);

				timer.addExecutePhase("ִ��SAVEBASE�Ų�����");

				// �õ�����
				if (arrReturnFromBs == null || arrReturnFromBs.size() == 0) {
					showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000010")/* "����ʧ��" */);
					return false;
				}

				timer.addExecutePhase("�������");

				// ��־ƽ ������������������������ʾ����
				nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
				ficr.setFreeItemRenderer(getBillCardPanel(), null);

				// V50������ɹ������ô˱�־
				isFrmCopy = false;
				//
				showHintMessage(m_lanResTool.getStrByID("common", "UCH005")/* @res"����ɹ�" */);
				// ��̨����ֵ����
				Vector vDataTemp = new Vector();
				int itemsLength = 0;
				PraybillItemVO[] items = null;
				PraybillItemVO[] itemsTemp = null;
				PraybillItemVO item = null;
				int status = 0;
				String[] ts = null;
				ArrayList arr = new ArrayList();
				if (arrReturnFromBs != null && arrReturnFromBs.size() >= 3) {
					ts = (String[]) arrReturnFromBs
							.get(arrReturnFromBs.size() - 3);
					m_SaveVOs.getHeadVO().setTs(ts[0]);
					m_SaveVOs.getHeadVO().setVpraycode(
							(String) arrReturnFromBs
									.get(arrReturnFromBs.size() - 1));
					m_SaveVOs.getHeadVO().setCpraybillid(
							(String) arrReturnFromBs.get(0));
					items = m_SaveVOs.getBodyVO();
					itemsLength = items.length;
					// ����ID��TS��Ӧ��
					HashMap<String, String> mapBidBts = new HashMap<String, String>();
					for (int i = 1; i < ts.length; i++) {
						mapBidBts.put(ts[i].substring(0, 20), ts[i].substring(
								20, 39));
					}
					int iPos = 1;
					// ��BS������Э�飺��֤�빺�������ı����������� arrReturn �������ڶ�������...��Ԫ��
					for (int i = 0; i < itemsLength; i++) {
						item = items[i];
						status = item.getBodyEditStatus();
						if (item != null && !String.valueOf(status).equals("3")) {
							if (item.getCpraybillid() == null)
								item.setCpraybillid((String) arrReturnFromBs
										.get(0));
							if (item.getCpraybill_bid() == null) {
								item.setCpraybill_bid((String) arrReturnFromBs
										.get(iPos));
								iPos++;
							}
							item.setTs(mapBidBts.get(item.getCpraybill_bid()));
							vDataTemp.add(item);
						}
						items[i] = item;
					}
					itemsTemp = new PraybillItemVO[vDataTemp.size()];
					vDataTemp.copyInto(itemsTemp);
					m_SaveVOs.setChildrenVO(itemsTemp);
					// ��������������������������������������ʱ�Ƶ��˿���ֱ���������ݣ���ʱ��Ҫˢ����ʾ
					arr = (ArrayList) arrReturnFromBs.get(arrReturnFromBs
							.size() - 2);
					m_SaveVOs.getHeadVO().setDauditdate((UFDate) arr.get(0));
					m_SaveVOs.getHeadVO().setCauditpsn((String) arr.get(1));
					m_SaveVOs.getHeadVO().setIbillstatus((Integer) arr.get(2));
					m_SaveVOs.getHeadVO().setTs((String) arr.get(3));
					m_SaveVOs.getHeadVO().setTmaketime((String) arr.get(4));
					m_SaveVOs.getHeadVO().setTlastmaketime((String) arr.get(5));
					m_SaveVOs.getHeadVO().setTaudittime((String) arr.get(6));

					/*
					 * try { m_SaveVOs =
					 * PraybillHelper.queryPrayVoByHid(m_SaveVOs.getHeadVO().getPrimaryKey());
					 * if (m_SaveVOs != null) { Logger.debug("�ɹ���ʾ����"); } }
					 * catch (Exception e) { SCMEnv.out(e); }
					 */

					dispAfterSave(m_SaveVOs, arrReturnFromBs);
				}
				/** **********��¼ҵ����־************ */
				/*
				 * Operlog operlog = new Operlog();
				 * m_SaveVOs.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
				 * m_SaveVOs.getOperatelogVO().setCompanyname(
				 * nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
				 * m_SaveVOs.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
				 * operlog.insertBusinessExceptionlog(m_SaveVOs, "����", "����",
				 * nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
				 * nc.vo.scm.pu.BillTypeConst.PO_PRAY,
				 * nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
				 */
				/** **********��¼ҵ����־* end *********** */
			} else {
				// �޶�����ʱ���°汾��
				VO.getHeadVO().setNversion(
						PuPubVO.getInteger_NullAs(String.valueOf(VO.getHeadVO()
								.getNversion() + 1), 1));

				PraybillVO result = (PraybillVO) PfUtilClient
						.processActionNoSendMessage(this, "SAVEREVISE",
								nc.vo.scm.pu.BillTypeConst.PO_PRAY,
								getClientEnvironment().getDate().toString(),
								VO, getCorpPrimaryKey(), null, null);

				timer.addExecutePhase("ִ��SAVEREVISE�Ų�����");

				// �õ�����
				if (result == null || result.getBodyLen() < 1) {
					showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000010")/* "����ʧ��" */);
					return false;
				}

				try {
					result = PraybillHelper.queryPrayVoByHid(result.getHeadVO()
							.getPrimaryKey());
					if (result != null) {
						Logger.debug("�ɹ���ʾ����");
					}
				} catch (Exception e) {
					SCMEnv.out(e);
				}

				// ��־ƽ ������������������������ʾ����
				nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
				ficr.setFreeItemRenderer(getBillCardPanel(), null);

				/** ����Ϣ���Ƶ��˺�ǰ���������������� */
				String makeMan = result.getHeadVO().getCoperator();
				String auditMan = result.getHeadVO().getCauditpsn();
				String billid = result.getHeadVO().getPrimaryKey();
				String billcode = result.getHeadVO().getVpraycode();
				PuTool.sendMessageToMen(makeMan, auditMan, billid, billcode,
						"20");

				// V50������ɹ������ô˱�־
				isFrmCopy = false;
				//
				showHintMessage(m_lanResTool.getStrByID("common", "UCH005")/* @res"����ɹ�" */);

				dispAfterSave(result, null);

			}
		} catch (Exception e) {
			SCMEnv.out(e);
			if (e instanceof BusinessException) {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000080")/*
												 * @res "����ʧ�ܣ�"
												 */);
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000081")/*
															 * @res "�빺������"
															 */, e.getMessage());
			} else {
				showHintMessage(m_lanResTool.getStrByID("40040101",
						"UPP40040101-000080")/*
												 * @res "����ʧ�ܣ�"
												 */);
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"40040101", "UPP40040101-000081")/*
															 * @res "�빺������"
															 */, e.getMessage());
			}
			return false;
		}
		// // �ֹ�����Ƭ����(�ָ������������������Ϊ������)
		// if (iSortCol >= 0) {
		// getBillCardPanel().getBillModel().sortByColumn(iSortCol, bSortAsc);
		// }
		timer.addExecutePhase("��������ʾ����");
		timer.showAllExecutePhase("�빺�������������");

		showHintMessage(m_lanResTool.getStrByID("common", "UCH005")/*
																	 * @res
																	 * "����ɹ�"
																	 */);

		return true;
	}

	/**
	 * ��������:ȫѡ
	 */
	private void onSelectAll() {
		final int iLen = getBillListPanel().getHeadBillModel().getRowCount();
		if (iLen <= 0) {
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000541")/*
											 * @res "������ѡ��"
											 */);
			return;
		}
		getBillListPanel().getHeadTable().setRowSelectionInterval(0, iLen - 1);
		for (int i = 0; i < iLen; i++) {
			getBillListPanel().getHeadBillModel().setRowState(i,
					BillModel.SELECTED);
		}
		/* ���ð�ť�߼� */
		setButtonsList();
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000033")/*
															 * @res "ȫ��ѡ�гɹ�"
															 */);
	}

	/**
	 * ��������:ȫ��
	 */
	private void onSelectNo() {
		final int iLen = getBillListPanel().getHeadBillModel().getRowCount();
		if (iLen <= 0) {
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000541")/*
											 * @res "������ѡ��"
											 */);
			return;
		}
		getBillListPanel().getHeadTable().removeRowSelectionInterval(0,
				iLen - 1);
		for (int i = 0; i < iLen; i++) {
			getBillListPanel().getHeadBillModel().setRowState(i,
					BillModel.UNSTATE);
		}
		/* ���ð�ť�߼� */
		setButtonsList();
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000034")/*
															 * @res "ȫ��ȡ���ɹ�"
															 */);
	}

	/**
	 * ����:�б���Ƭ
	 */
	private void onCard() {

		// ���빺��
		if (m_VOs == null || m_VOs.length == 0) {
			getBillListPanel().setVisible(false);
			getBillCardPanel().setVisible(true);
			getBillCardPanel().getBillData().clearViewData();
			setButtonsCard();
			m_nUIState = 0;
			showHintMessage(m_lanResTool.getStrByID("common", "UCH021")/*
																		 * @res
																		 * "��Ƭ��ʾ"
																		 */);
			return;
		}
		// ���빺��
		if (m_nPresentRecord >= 0) {
			int iSortCol = getBillListPanel().getBodyBillModel()
					.getSortColumn();
			boolean bSortAsc = getBillListPanel().getBodyBillModel()
					.isSortAscending();
			/* ���ص��ݿ�Ƭ�ؼ� */
			getBillListPanel().setVisible(false);
			getBillCardPanel().setVisible(true);
			if (m_bOnhandShowHidden) {
				add(getSplitPanelBc(), java.awt.BorderLayout.CENTER);
			} else {
				add(getBillCardPanel(), java.awt.BorderLayout.CENTER);
			}
			m_nUIState = 0;
			/* ���ص���VO����Ƭ */
			setVoToBillCard(m_nPresentRecord, null);
			// �ֹ�����Ƭ����(ͬ���б����������)
			if (iSortCol >= 0) {
				getBillCardPanel().getBillModel().sortByColumn(iSortCol,
						bSortAsc);
			}
			/* ���ð�ť�߼� */
			setButtonsCard();
		}
		getBillCardPanel().execHeadTailLoadFormulas();
		getBillCardPanel().setEnabled(false);
		setButtonsRevise(isCurAuditOP(m_VOs[m_nPresentRecord]));
		showHintMessage(m_lanResTool.getStrByID("common", "UCH021")/*
																	 * @res
																	 * "��Ƭ��ʾ"
																	 */);
	}

	/**
	 * �������ڣ� 2005-9-20 ���������� ������չ��ť����Ƭ����
	 */
	private void addExtendBtns() {
		ButtonObject[] btnsExtend = getExtendBtns();
		if (btnsExtend == null || btnsExtend.length <= 0) {
			return;
		}
		ButtonObject boExtTop = getBtnTree().getExtTopButton();
		getBtnTree().addMenu(boExtTop);
		int iLen = btnsExtend.length;
		try {
			for (int j = 0; j < iLen; j++) {
				getBtnTree().addChildMenu(boExtTop, btnsExtend[j]);
			}
		} catch (BusinessException be) {
			showHintMessage(be.getMessage());
			return;
		}
	}

	/**
	 * ����:ִ������
	 */
	private void onUnAudit() {

		// ���������˼��������ڹ�ϣ������ʧ��ʱ�õ�
		String strPsnOld = m_VOs[m_nPresentRecord].getHeadVO().getCauditpsn();
		UFDate dateAuditOld = m_VOs[m_nPresentRecord].getHeadVO()
				.getDauditdate();
		//
		try {
			nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
			timer.start("�빺���������onUnAudit������ʼ");
			String[] sPraybillIds = new String[1];
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000082")/* @res"��������..." */);
			PraybillVO vo = m_VOs[m_nPresentRecord];
			if (!isCanUnAudit(vo)) {
				showErrorMessage(vo.getHeadVO().getVpraycode()
						+ NCLangRes.getInstance().getStrByID("40040101",
								"UPT40040101-001124")
				/* @res"�Ѿ����ɲɹ�������ɹ���ͬ��۸�������,��������" */);
				return;
			}

			// ������Ա��ԭ������ID��Ϊ�ж��Ƿ������������˵ĵ���
			vo.getHeadVO().setCoperatoridnow(
					getClientEnvironment().getUser().getPrimaryKey());
			vo.getHeadVO().setAttributeValue("cauditpsnold", strPsnOld);
			//
			vo.getHeadVO().setCuserid(
					getClientEnvironment().getUser().getPrimaryKey());
			sPraybillIds[0] = vo.getHeadVO().getCpraybillid();
			// ���������
			vo.getHeadVO().setCauditpsn(
					getClientEnvironment().getUser().getPrimaryKey());

			timer.addExecutePhase("����ǰ׼������");
			/* ���� */
			PfUtilClient.processBatchFlow(null, "UNAPPROVE"
					+ getClientEnvironment().getUser().getPrimaryKey(),
					nc.vo.scm.pu.BillTypeConst.PO_PRAY, getClientEnvironment()
							.getDate().toString(), new PraybillVO[] { vo },
					null);

			if (!PfUtilClient.isSuccess()) {
				// ����ʧ�ܣ��ָ������˼���������
				m_VOs[m_nPresentRecord].getHeadVO().setCauditpsn(strPsnOld);
				m_VOs[m_nPresentRecord].getHeadVO().setDauditdate(dateAuditOld);
				//
				showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
						"UPPSCMCommon-000409")/* @res"����δ�ɹ�" */);
				return;
			}
			timer.addExecutePhase("ִ��UNAPPROVE�ű�����");

			/* ����ɹ���ˢ�� */
			ArrayList arrRet = PraybillHelper.queryPrayForSaveAudit(vo
					.getParentVO().getPrimaryKey());
			((PraybillHeaderVO) vo.getParentVO()).setDauditdate((UFDate) arrRet
					.get(0));
			((PraybillHeaderVO) vo.getParentVO()).setCauditpsn((String) arrRet
					.get(1));
			((PraybillHeaderVO) vo.getParentVO())
					.setIbillstatus((Integer) arrRet.get(2));
			((PraybillHeaderVO) vo.getParentVO()).setTs((String) arrRet.get(3));
			((PraybillHeaderVO) vo.getParentVO()).setTaudittime(null);

			timer.addExecutePhase("����ɹ���ˢ��");
			m_VOs[m_nPresentRecord] = vo;
			/* ˢ�°�ť״̬ */
			setButtonsCard();
			/* ���ص��� */
			setVoToBillCard(m_nPresentRecord, m_lanResTool.getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000410")/* @res"������" */);

			timer.addExecutePhase("�������ʾ");
			timer.showAllExecutePhase("�빺���������onUnAudit��������");
			getBillCardPanel().setEnabled(false);

			/*
			 * �������Ż���������־�Ƶ���̨���� Operlog operlog = new Operlog();
			 * vo.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
			 * vo.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
			 * vo.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
			 * operlog.insertBusinessExceptionlog(vo, "����", "����",
			 * nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
			 * nc.vo.scm.pu.BillTypeConst.PO_PRAY,
			 * nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
			 */

			showHintMessage(m_lanResTool.getStrByID("common", "UCH011")/*
																		 * @res
																		 * "����ɹ�"
																		 */);
		} catch (Exception e) {
			// ����ʧ�ܣ��ָ������˼���������
			m_VOs[m_nPresentRecord].getHeadVO().setCauditpsn(strPsnOld);
			m_VOs[m_nPresentRecord].getHeadVO().setDauditdate(dateAuditOld);
			//
			PuTool.outException(this, e);
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000083")/* @res"�����쳣,����ʧ��" */);
			SCMEnv.out(e);
		}
	}

	/**
	 * �������а�ť�Ƿ���á�
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param bEnabled
	 *            <p>
	 * @author czp
	 * @time 2007-3-13 ����01:04:45
	 */
	private void setButtonsEnabled(boolean bEnabled) {
		ButtonObject[] bos = m_btnTree.getButtonArray();
		int iLen = bos == null ? 0 : bos.length;
		int jLen = 0;
		for (int i = 0; i < iLen; i++) {
			bos[i].setEnabled(bEnabled);
			if (bos[i].getChildCount() > 0) {
				jLen = bos[i].getChildCount();
				for (int j = 0; j < jLen; j++) {
					bos[i].getChildButtonGroup()[j].setEnabled(bEnabled);
				}
			}
		}
		updateButtonsAll();
	}

	/**
	 * ���ܣ���Ƭ�����ť�߼�
	 */
	private void setButtonsCard() {
		//
		setButtonsEnabled(true);
		//
		dealBtnsBeforeCardShowing();

		/* ���桢ȡ�����в����� */
		m_btnSave.setEnabled(false);
		m_btnCancel.setEnabled(false);
		m_btnLines.setEnabled(false);
		if (m_btnLines.getChildCount() > 0) {
			for (int i = 0; i < m_btnLines.getChildButtonGroup().length; i++) {
				m_btnLines.getChildButtonGroup()[i].setEnabled(false);
			}
		}
		boolean bHaveRecs = (m_VOs != null && m_VOs.length > 0);
		// ����
		m_btnCopy.setEnabled(bHaveRecs);
		/* ��ҳ */
		if (!bHaveRecs) {
			m_btnFirst.setEnabled(false);
			m_btnPrev.setEnabled(false);
			m_btnNext.setEnabled(false);
			m_btnLast.setEnabled(false);
		} else if (m_VOs.length == 1) {
			m_btnFirst.setEnabled(false);
			m_btnPrev.setEnabled(false);
			m_btnNext.setEnabled(false);
			m_btnLast.setEnabled(false);
		} else if (m_VOs != null && m_nPresentRecord == m_VOs.length - 1) {
			m_btnNext.setEnabled(false);
			m_btnLast.setEnabled(false);
		} else if (m_nPresentRecord == 0) {
			m_btnFirst.setEnabled(false);
			m_btnPrev.setEnabled(false);
		}
		// ��������
		m_btnDocument.setEnabled(bHaveRecs);
		boOnHandShowHidden.setEnabled(bHaveRecs);
		// ������ѯ
		m_btnWorkFlowBrowse.setEnabled(bHaveRecs);
		m_btnUsable.setEnabled(bHaveRecs);
		m_btnPrint.setEnabled(bHaveRecs);
		m_btnPrintPreview.setEnabled(bHaveRecs);
		m_btnCombin.setEnabled(bHaveRecs);
		m_btnLinkBillsBrowse.setEnabled(bHaveRecs);
		m_btnPriceInfo.setEnabled(bHaveRecs);

		/* ���󡢹رա��򿪡��޸ġ����ϡ����������� */
		int iBillStatus = -1;
		if (bHaveRecs && m_VOs[m_nPresentRecord] != null
				&& m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus() != null) {
			iBillStatus = m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus()
					.intValue();
		}
		// ����
		boolean isNeedSendToAuditQ = isNeedSendAudit(iBillStatus);
		//
		if (iBillStatus == -1) {
			m_btnSendAudit.setEnabled(false);
			m_btnClose.setEnabled(false);
			m_btnOpen.setEnabled(false);
			m_btnModify.setEnabled(false);
			m_btnDiscard.setEnabled(false);
			m_btnApprove.setEnabled(false);
			m_btnUnApprove.setEnabled(false);
			//m_btnOUT.setEnabled(false);
		} else if (iBillStatus == 0) {
			m_btnSendAudit.setEnabled(isNeedSendToAuditQ);
			m_btnClose.setEnabled(false);
			m_btnOpen.setEnabled(false);
			m_btnUnApprove.setEnabled(false);
			//m_btnOUT.setEnabled(false);
		} else if (iBillStatus == 1) {// �Ѿ��ر�
			m_btnSendAudit.setEnabled(false);
			m_btnClose.setEnabled(false);
			m_btnModify.setEnabled(false);
			m_btnDiscard.setEnabled(false);
			m_btnApprove.setEnabled(false);
			m_btnUnApprove.setEnabled(false);
			//m_btnOUT.setEnabled(false);
		} else if (iBillStatus == 2) {/* �빺���������� */
			m_btnSendAudit.setEnabled(false);
			m_btnClose.setEnabled(false);
			m_btnOpen.setEnabled(false);
			m_btnModify.setEnabled(false);
			m_btnDiscard.setEnabled(false);
			m_btnUnApprove.setEnabled(true);
			//m_btnOUT.setEnabled(false);
		} else if (iBillStatus == 3) {/* �빺��������ͨ�� */
			m_btnSendAudit.setEnabled(false);
			m_btnOpen.setEnabled(false);
			m_btnModify.setEnabled(false);
			m_btnDiscard.setEnabled(false);
			m_btnApprove.setEnabled(false);
			//m_btnOUT.setEnabled(true);
		} else if (iBillStatus == 4) {/* �빺��������ͨ�� */
			m_btnSendAudit.setEnabled(false);
			m_btnClose.setEnabled(false);
			m_btnOpen.setEnabled(false);
			m_btnUnApprove.setEnabled(false);
			//m_btnOUT.setEnabled(false);
		}
		// ˢ��
		m_btnRefresh.setEnabled(m_bQueried);
		// �������Ż����޶�״̬����ȥ�жϵ�ǰ���Ƿ������
		if (bHaveRecs && isRevise)
			setButtonsRevise(isCurAuditOP(m_VOs[m_nPresentRecord]));
		else
			setButtonsRevise(false);
		//
		updateButtonsAll();
	}

	/**
	 * ���ܣ�������ʱ��ʼ����Ƭ��ť,���ó����棬�в��������������а�ťΪ���� ���أ� ���⣺ ���ڣ�(2002-5-14 11:24:40)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private void setButtonsCardModify() {
		//
		setButtonsEnabled(false);
		//
		dealBtnsBeforeCardShowing();
		// �в���
		m_btnLines.setEnabled(true);
		if (m_btnLines.getChildCount() > 0) {
			for (int i = 0; i < m_btnLines.getChildButtonGroup().length; i++) {
				m_btnLines.getChildButtonGroup()[i].setEnabled(true);
			}
		}
		// ��������
		m_btnOthersFuncs.setEnabled(true);
		// ������ѯ
		m_btnOthersQry.setEnabled(true);
		// ������ѯ
		m_btnUsable.setEnabled(true);
		// ��ʾ/���ش���
		boOnHandShowHidden.setEnabled(true);
		// ����ά��
		m_btnMaintains.setEnabled(true);
		// ����
		m_btnSave.setEnabled(true);
		// ȡ��
		m_btnCancel.setEnabled(true);
		// ִ����
		m_btnActions.setEnabled(true);
		// ����
		int iBillStatus = -1;
		if (!m_bAdd && m_VOs != null && m_VOs.length > 0
				&& m_VOs[m_nPresentRecord] != null
				&& m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus() != null) {
			iBillStatus = m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus()
					.intValue();
		}
		m_btnSendAudit.setEnabled(isNeedSendAudit(iBillStatus));
		//
		updateButtonsAll();
	}

	/**
	 * ����ť�߼�����
	 * 
	 * @author czp
	 * @since v50
	 * @date 2006-09-23
	 * @ע�⣺��������Լ������������ʱ���ô˷�����Ҫ����״ֵ̬Ϊ -1
	 */
	private boolean isNeedSendAudit(int iBillStatus) {

		// ����δͨ��
		boolean isNeedSendToAuditQ = (iBillStatus != BillStatus.AUDITFAIL);

		if (getPraybillVOs() == null)
			return false;
		String billid = getPraybillVOs()[m_nPresentRecord].getHeadVO()
				.getCpraybillid();
		isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit("20",
				m_sLoginCorpId, null, billid, getClientEnvironment().getUser()
						.getPrimaryKey());
		m_btnSendAudit.setEnabled(isNeedSendToAuditQ);
		updateButton(m_btnSendAudit);

		return isNeedSendToAuditQ;
	}

	/**
	 * ���ܣ�������ʱ��ʼ����Ƭ��ť,���ó����棬�в��������������а�ťΪ���� ���أ� ���⣺ ���ڣ�(2002-5-14 11:24:40)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private void setButtonsCardCopy() {
		//
		setButtonsEnabled(false);

		// ���⹦��
		m_btnList.setName(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000464")/*
										 * @res "�б���ʾ"
										 */);
		// �б���
		m_btnSelectAll.setEnabled(false);
		m_btnSelectNo.setEnabled(false);
		//
		m_btnLines.setEnabled(true);
		if (m_btnLines.getChildCount() > 0) {
			for (int i = 0; i < m_btnLines.getChildButtonGroup().length; i++) {
				m_btnLines.getChildButtonGroup()[i].setEnabled(true);
			}
		}
		/* �޸�ʱ����:����{������ѯ} */
		m_btnOthersFuncs.setEnabled(true);
		m_btnUsable.setEnabled(true);

		m_btnMaintains.setEnabled(true);
		m_btnSave.setEnabled(true);
		m_btnCancel.setEnabled(true);
		m_btnBrowses.setEnabled(true);
		int iBillStatus = 88;
		// String cbizType = null;

		if (m_VOs != null && m_VOs.length > 0
				&& m_VOs[m_nPresentRecord] != null
				&& m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus() != null) {
			iBillStatus = m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus()
					.intValue();
			// cbizType = m_VOs[m_nPresentRecord].getHeadVO().getCbiztype();
		}
		if (iBillStatus == 0) {
			setSendAuditBtnState();
		}

		boolean isNeedSendToAuditQ = false;
		if ((m_bAdd || ((new Integer(iBillStatus))
				.compareTo(nc.vo.scm.pu.BillStatus.FREE) == 0 && (new Integer(
				iBillStatus)).compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) == 0))) {
			// �ò������Ƿ���������
			// if (cbizType != null && cbizType.toString().trim().length() > 0)
			// {
			// isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit("20",
			// m_sLoginCorpId, cbizType, null, getClientEnvironment()
			// .getUser().getPrimaryKey());
			// } else
			// if (m_bizButton != null && m_bizButton.getTag() != null
			// && m_bizButton.getTag() != null) {
			// isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit("20",
			// m_sLoginCorpId, m_bizButton.getTag(), null,
			// getClientEnvironment().getUser().getPrimaryKey());
			// }
			m_btnSendAudit.setEnabled(isNeedSendToAuditQ);
		}
		// ˢ��
		m_btnRefresh.setEnabled(false);
	}

	/**
	 * ����ʵ�ָ÷���������ҵ�����ı��⡣
	 * 
	 * @version (00-6-6 13:33:25)
	 * @return java.lang.String
	 */
	private void setCauditid(String newId) {

		m_cauditid = newId;
	}

	/**
	 * ���ܣ����þ��� 2002-10-29 czp ��������޼۾�������
	 */
	private void setDecimalDigits(String pk_corp, int iflag) {
		int[] iaDigit = PoPublicUIClass.getShowDigits(pk_corp);
		final int nMoney = nc.ui.rc.pub.CPurchseMethods.getCurrDecimal(pk_corp);
		if (iflag == 0) {
			// CARD
			getBillCardPanel().getBodyItem("npraynum").setDecimalDigits(
					iaDigit[0]);
			getBillCardPanel().getBodyItem("nassistnum").setDecimalDigits(
					iaDigit[1]);
			getBillCardPanel().getBodyItem("nsuggestprice").setDecimalDigits(
					iaDigit[2]);
			getBillCardPanel().getBodyItem("nmaxprice").setDecimalDigits(
					iaDigit[2]);
			getBillCardPanel().getBodyItem("nmoney").setDecimalDigits(nMoney);
			getBillCardPanel().getBodyItem("nexchangerate").setDecimalDigits(
					iaDigit[3]);
		} else {
			// LIST
			getBillListPanel().getBodyItem("npraynum").setDecimalDigits(
					iaDigit[0]);
			getBillListPanel().getBodyItem("nassistnum").setDecimalDigits(
					iaDigit[1]);
			getBillListPanel().getBodyItem("nsuggestprice").setDecimalDigits(
					iaDigit[2]);
			getBillListPanel().getBodyItem("nmaxprice").setDecimalDigits(
					iaDigit[2]);
			getBillListPanel().getBodyItem("nmoney").setDecimalDigits(nMoney);
			getBillListPanel().getBodyItem("nexchangerate").setDecimalDigits(
					iaDigit[3]);
		}
	}

	/**
	 * �����빺����Ƭ�ɱ༭�� ���ñ���ɱ༭�ԣ���Ҫ��������ɱ༭��Ȼ�������ñ���ɱ༭��
	 */
	private void setEdit(boolean b) {
		// ���ŵ��ݲ��ɱ༭
		getBillCardPanel().setEnabled(false);
		// �Ƿ�ɱ༭
		m_bEdit = b;
		// �����Ƿ�ɱ༭
		getBillCardPanel().getBodyItem("npraynum").setEnabled(b);
		getBillCardPanel().getBodyItem("npraynum").setEdit(b);
	}

	/**
	 * ���ߣ���ά�� ���ܣ���������������Ŀɱ༭�� ������iBeginRow��iEndRow �����к� ���أ��� ���⣺�� ���ڣ�(2001-4-22
	 * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2003-02-26 wyf �޸�Ϊȡ���������ķ����������Ч��
	 */
	private static void setEnabled_BodyFree(BillCardPanel pnlBillCard,
			UIRefPane refpaneInv, int iBeginRow, int iEndRow) {
		if (pnlBillCard == null || refpaneInv == null) {
			SCMEnv.out("�����������ȷ��");
			return;
		}
		Object[] saMangId = ((Object[]) refpaneInv
				.getRefValues("bd_invmandoc.pk_invmandoc"));
		final int size = saMangId.length;
		String[] sMangIds = new String[size];
		for (int i = 0; i < size; i++) {
			sMangIds[i] = saMangId[i].toString();
		}
		// ����װ��
		PuTool.loadBatchFreeVO(sMangIds);
		String sMangId;
		for (int i = iBeginRow; i <= iEndRow; i++) {
			sMangId = (String) pnlBillCard.getBillModel().getValueAt(i,
					"cmangid");

			// sMangId = sMangId.substring(1,sMangId.length()-1);
			pnlBillCard.getBillModel().setCellEditable(
					i,
					"vfree",
					PuTool.isFreeMngt(sMangId)
							&& pnlBillCard.getBillModel().getItemByKey("vfree")
									.isEdit());
		}

	}

	/**
	 * ���ߣ���ά�� ���ܣ��������ô���Ĺ��������Ϣ ����: BillCardPanel pnlBillCard ����ģ�� UIRefPane
	 * refpaneInv ������� int iBeginRow ��ʼλ�� int iEndRow ����λ�� ���أ��� ���⣺��
	 * ���ڣ�(2004-02-11 13:45:10) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private static void setInvEditFormulaInfo(BillCardPanel pnlBillCard,
			UIRefPane refpaneInv, int iBeginRow, int iEndRow,
			String sLoginCorpId, String sLoginCorpName, Object[] saCode,
			Object[] saName, Object[] saSpec, Object[] saType,
			Object[] saMangId, Object[] saBaseId, Object[] saMeasUnit) {
		if (pnlBillCard == null || refpaneInv == null) {
			SCMEnv.out("�����������ȷ��");
			return;
		}
		try {
			// Object[] saMangIdRef = ((Object[])
			// refpaneInv.getRefValues("bd_invmandoc.pk_invmandoc"));
			// Object[] saBaseIdRef = ((Object[])
			// refpaneInv.getRefValues("bd_invmandoc.pk_invbasdoc"));
			// Object[] saMeasUnitRef = ((Object[])
			// refpaneInv.getRefValues("bd_invbasdoc.pk_measdoc"));
			//
			//     
			// Object[] saMangId = new Object[saMangIdRef.length];
			// Object[] saBaseId = new Object[saBaseIdRef.length];
			// Object[] saMeasUnit = new Object[saMeasUnitRef.length];
			if (saMangId == null || saBaseId == null
					|| saMangId.length != saBaseId.length) {
				Logger.debug("���ݴ��󣺴��������IDΪ�ջ�������IDΪ�ջ���߳��Ȳ��ȣ�ֱ�ӷ���");
				return;
			}

			// Object[] saPurOrg = new Object[saMangId.length];
			// Object[] saEmployee = new Object[saMangId.length];
			// for (int i = 0; i < saMangId.length; i++) {
			// saMangId[i] = saMangIdRef[i];
			// saBaseId[i] = saBaseIdRef[i];
			// saMeasUnit[i] = ((RefValueVO)saMeasUnitRef[i]).getOriginValue();
			// // saPurOrg[i] = pnlBillCard.getBodyValueAt(iBeginRow + i,
			// // "cpurorganization");
			// // saEmployee[i] = pnlBillCard.getBodyValueAt(iBeginRow + i,
			// // "cemployeeid");
			// }
			final int iLen = saMangId.length;

			// ================����������������������,����������
			String[] saFormula = new String[] {
					"getColValue(bd_measdoc,measname,pk_measdoc,cmessureunit)",
					"getColValue(bd_invbasdoc,pk_measdoc2,pk_invbasdoc,cbaseid)"
			// "getColValue(bd_purorg,name,pk_purorg,cpurorganization)",
			// "getColValue(bd_purorg,ownercorp,pk_purorg,cpurorganization)",
			// "getColValue(bd_psndoc,psnname,pk_psndoc,cemployeeid)"
			};
			PuTool.getFormulaParse().setExpressArray(saFormula);
			int iFormulaLen = saFormula.length;
			for (int i = 0; i < iFormulaLen; i++) {
				PuTool.getFormulaParse()
						.addVariable("cmessureunit", saMeasUnit);
				PuTool.getFormulaParse().addVariable("cbaseid", saBaseId);// saBaseIdRef);
				// PuTool.getFormulaParse().addVariable("cpurorganization",
				// saPurOrg);
				// PuTool.getFormulaParse().addVariable("cemployeeid",
				// saEmployee);
			}

			String[][] saRet = PuTool.getFormulaParse().getValueSArray();
			String[] saUnitName = new String[iLen];
			// ����������
			String[] sAssisUnit = new String[iLen];
			String[] sAssisUnitRef = new String[iLen];
			// String[] sPurOrgName = new String[iLen];
			// String[] sOwnerCorp = new String[iLen];
			// String[] sEmployeeName = new String[iLen];
			if (saRet != null) {
				for (int i = 0; i < iLen; i++) {
					if (saRet[0] != null) {
						saUnitName[i] = saRet[0][i];
					}
					if (saRet[1] != null) {
						sAssisUnit[i] = saRet[1][i].toString();
						sAssisUnitRef[i] = saRet[1][i].toString();
					}
					// if (saRet[2] != null)
					// sPurOrgName[i] = saRet[2][i].toString();
					// if (saRet[3] != null) {
					// sOwnerCorp[i] = saRet[3][i].toString();
					// if (sOwnerCorp[i] == null ||
					// sOwnerCorp[i].trim().length() == 0)
					// sOwnerCorp[i] = sLoginCorpId;
					// }
					// if (saRet[4] != null)
					// sEmployeeName[i] = saRet[4][i].toString();
				}
			}
			// // ================�Ա����������ֵ
			// Object[] saCode = ((Object[])
			// refpaneInv.getRefValues("bd_invbasdoc.invcode"));
			// Object[] saName = ((Object[])
			// refpaneInv.getRefValues("bd_invbasdoc.invname"));
			// Object[] saSpec = ((Object[])
			// refpaneInv.getRefValues("bd_invbasdoc.invspec"));
			// Object[] saType = ((Object[])
			// refpaneInv.getRefValues("bd_invbasdoc.invtype"));

			// ================����������:���ء�����޼�
			saFormula = new String[] {
					"getColValue(bd_invmandoc,prodarea,pk_invmandoc,cmangid)",
					"getColValue(bd_invmandoc,maxprice,pk_invmandoc,cmangid)",
					"getColValue(bd_measdoc,measname,pk_measdoc,cassistunit)" };
			iFormulaLen = saFormula.length;
			PuTool.getFormulaParse().setExpressArray(saFormula);

			for (int i = 0; i < iFormulaLen; i++) {
				PuTool.getFormulaParse().addVariable("cmangid", saMangId);// saMangIdRef);
				PuTool.getFormulaParse().addVariable("cassistunit",
						sAssisUnitRef);
				// PuTool.getFormulaParse().addVariable("ownercorp",
				// sOwnerCorp);
			}

			saRet = PuTool.getFormulaParse().getValueSArray();
			// ����
			String[] saArea = new String[iLen];
			// ����޼�
			UFDouble[] uMaxPrice = new UFDouble[iLen];
			// ��������λ
			String[] sAssisUnitName = new String[iLen];
			// String[] sPurCorpName = new String[iLen];
			if (saRet != null) {
				for (int i = 0; i < iLen; i++) {
					if (saRet[0] != null) {
						saArea[i] = saRet[0][i];
					}
					if (saRet[1] != null) {
						uMaxPrice[i] = new UFDouble(saRet[1][i]);
					}
					if (saRet[2] != null) {
						sAssisUnitName[i] = saRet[2][i];
					}
					// if (saRet[3] != null)
					// sPurCorpName[i] = saRet[3][i].trim();
				}
			}

			// ִ�б��幫ʽ
			int iPkIndex = 0;
			for (int i = iBeginRow; i < iEndRow; i++) {
				iPkIndex = i - iBeginRow;
				// ����ID
				pnlBillCard.setBodyValueAt(saMangId[iPkIndex], i, "cmangid");
				// ����ID
				pnlBillCard.setBodyValueAt(saBaseId[iPkIndex], i, "cbaseid");
				// ����
				pnlBillCard.setBodyValueAt(saCode[iPkIndex], i,
						"cinventorycode");
				// ����
				pnlBillCard.setBodyValueAt(saName[iPkIndex], i,
						"cinventoryname");
				// ���
				pnlBillCard.setBodyValueAt(saSpec[iPkIndex], i,
						"cinventoryspec");
				// �ͺ�
				pnlBillCard.setBodyValueAt(saType[iPkIndex], i,
						"cinventorytype");
				// ������λNAME
				pnlBillCard.setBodyValueAt(saUnitName[iPkIndex], i,
						"cinventoryunit");
				// ����
				pnlBillCard.setBodyValueAt(saArea[iPkIndex], i, "cprodarea");
				// ����޼�
				pnlBillCard.setBodyValueAt(uMaxPrice[iPkIndex], i, "nmaxprice");
				// ��������λ
				pnlBillCard.setBodyValueAt(sAssisUnitName[iPkIndex], i,
						"cassistunitname");
				// ������ID
				pnlBillCard.setBodyValueAt(sAssisUnit[iPkIndex], i,
						"cassistunit");
			}
		} catch (Exception e) {
			Logger.debug("¼�����ʱ���ó���");
		}
	}

	/**
	 * �����б�ť�߼�
	 */
	private void setButtonsList() {

		// �б�����
		m_btnCard.setName(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000463")/*
										 * @res "��Ƭ��ʾ"
										 */);
		// //��Ƭ����
		// m_btnBusiTypes.setEnabled(false);
		//
		m_btnAdds.setEnabled(false);
		//
		m_btnLines.setEnabled(false);
		m_btnAddLine.setEnabled(false);
		m_btnDelLine.setEnabled(false);
		m_btnPstLine.setEnabled(false);
		m_btnCpyLine.setEnabled(false);
		//
		m_btnBrowses.setEnabled(false);
		m_btnFirst.setEnabled(false);
		m_btnPrev.setEnabled(false);
		m_btnNext.setEnabled(false);
		m_btnLast.setEnabled(false);
		//
		m_btnSave.setEnabled(false);
		m_btnCancel.setEnabled(false);

		/* �ݲ��ṩ�б�۸���֤���� */
		m_btnPriceInfo.setEnabled(false);
		/* �ݲ�֧�ֵ��б��ܣ������ؼ�û���б��� */
		m_btnCombin.setEnabled(false);

		// ����Ч��
		m_btnMaintains.setEnabled(true);
		m_btnActions.setEnabled(true);
		m_btnQuery.setEnabled(true);
		m_btnBrowses.setEnabled(true);
		m_btnPrints.setEnabled(true);
		m_btnOthersQry.setEnabled(true);
		m_btnOthersFuncs.setEnabled(true);

		// ��ѯ������Ч
		m_btnRefresh.setEnabled(m_bQueried);

		/*
		 * ����������ذ�ť�������� ȫ����Ԥ������ӡ���ĵ�����
		 * ȫѡ����Ƭ��ʾ���޸ġ����ϡ����ơ��������������󡢹رա��򿪡��ϲ���ʾ�����顢������ѯ��������״̬
		 */
		int iSelectedCnt = getBillListPanel().getHeadTable()
				.getSelectedRowCount();
		int iCacheDataCnt = getBillListPanel().getHeadBillModel().getRowCount();
		if (m_VOs == null || m_VOs.length <= 0) {
			iCacheDataCnt = 0;
		}
		/* û������ */
		if (iCacheDataCnt <= 0) {
			m_btnCard.setEnabled(true);
			m_btnSelectNo.setEnabled(false);
			m_btnPrintPreview.setEnabled(false);
			m_btnPrint.setEnabled(false);
			m_btnDocument.setEnabled(false);
			m_btnSelectAll.setEnabled(false);
			m_btnModify.setEnabled(false);
			m_btnDiscard.setEnabled(false);
			m_btnCopy.setEnabled(false);
			m_btnSendAudit.setEnabled(false);
			m_btnApprove.setEnabled(false);
			m_btnUnApprove.setEnabled(false);
			m_btnClose.setEnabled(false);
			m_btnOpen.setEnabled(false);
			m_btnCombin.setEnabled(false);
			m_btnLinkBillsBrowse.setEnabled(false);
			m_btnUsable.setEnabled(false);
			m_btnWorkFlowBrowse.setEnabled(false);
			//
			updateButtonsAll();
			return;
		}
		/* �����ݵ�û��ѡȡ�κε��� */
		if (iSelectedCnt <= 0) {
			m_btnSelectAll.setEnabled(true);
			m_btnCard.setEnabled(false);
			m_btnSelectNo.setEnabled(false);
			m_btnPrintPreview.setEnabled(false);
			m_btnPrint.setEnabled(false);
			m_btnDocument.setEnabled(false);
			m_btnModify.setEnabled(false);
			m_btnDiscard.setEnabled(false);
			m_btnCopy.setEnabled(false);
			m_btnSendAudit.setEnabled(false);
			m_btnApprove.setEnabled(false);
			m_btnUnApprove.setEnabled(false);
			m_btnClose.setEnabled(false);
			m_btnOpen.setEnabled(false);
			m_btnCombin.setEnabled(false);
			m_btnLinkBillsBrowse.setEnabled(false);
			m_btnUsable.setEnabled(false);
			m_btnWorkFlowBrowse.setEnabled(false);
			//
			updateButtonsAll();
			return;
		}

		/* ������ �� ѡȡ���������ڵ���1 */
		m_btnSelectNo.setEnabled(true);
		m_btnPrintPreview.setEnabled(true);
		m_btnPrint.setEnabled(true);
		m_btnDocument.setEnabled(true);

		// ȫѡ
		if (iSelectedCnt != iCacheDataCnt) {
			m_btnSelectAll.setEnabled(true);
		} else {
			m_btnSelectAll.setEnabled(false);
		}

		/* ��Ƭ��ʾ�����ơ��ϲ���ʾ�����顢������ѯ��������״̬ */
		boolean bOnlyOneSelected = (iSelectedCnt == 1);
		m_btnCard.setEnabled(bOnlyOneSelected);
		m_btnCopy.setEnabled(bOnlyOneSelected);
		// �ݲ�֧�ֵ��б��ܣ������ؼ�û���б���
		// m_btnCombin.setEnabled(bOnlyOneSelected);
		m_btnLinkBillsBrowse.setEnabled(bOnlyOneSelected);
		m_btnUsable.setEnabled(bOnlyOneSelected);
		m_btnWorkFlowBrowse.setEnabled(bOnlyOneSelected);

		/* �޸ġ����ϡ��������������󡢹رա��� */
		if (bOnlyOneSelected) {
			/* ֻ��һ�ŵ���ѡ�е���� */
			int iBillStatus = -1;
			if (m_nPresentRecord >= 0
					&& m_VOs[m_nPresentRecord] != null
					&& m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus() != null) {
				iBillStatus = m_VOs[m_nPresentRecord].getHeadVO()
						.getIbillstatus().intValue();
			}
			if (iBillStatus == 0) {
				m_btnModify.setEnabled(true);
				m_btnDiscard.setEnabled(true);
				m_btnSendAudit.setEnabled(isNeedSendAudit(iBillStatus));
				m_btnApprove.setEnabled(true);
				m_btnUnApprove.setEnabled(false);
				m_btnClose.setEnabled(false);
				m_btnOpen.setEnabled(false);
			} else if (iBillStatus == 1) {
				m_btnModify.setEnabled(false);
				m_btnDiscard.setEnabled(false);
				m_btnSendAudit.setEnabled(isNeedSendAudit(iBillStatus));
				m_btnApprove.setEnabled(false);
				m_btnUnApprove.setEnabled(false);
				m_btnClose.setEnabled(false);
				m_btnOpen.setEnabled(true);
			} else if (iBillStatus == 2) {
				m_btnModify.setEnabled(false);
				m_btnDiscard.setEnabled(false);
				m_btnSendAudit.setEnabled(isNeedSendAudit(iBillStatus));
				m_btnApprove.setEnabled(true);
				m_btnUnApprove.setEnabled(false);
				m_btnClose.setEnabled(false);
				m_btnOpen.setEnabled(false);
			} else if (iBillStatus == 3) {
				m_btnModify.setEnabled(false);
				m_btnDiscard.setEnabled(false);
				// m_btnSendAudit.setEnabled(isNeedSendAudit(iBillStatus));
				m_btnSendAudit.setEnabled(false);
				m_btnApprove.setEnabled(false);
				m_btnUnApprove.setEnabled(true);
				m_btnClose.setEnabled(true);
				m_btnOpen.setEnabled(false);
			} else if (iBillStatus == 4) {
				m_btnModify.setEnabled(true);
				m_btnDiscard.setEnabled(true);
				m_btnSendAudit.setEnabled(isNeedSendAudit(iBillStatus));
				m_btnApprove.setEnabled(false);
				m_btnUnApprove.setEnabled(false);
				m_btnClose.setEnabled(false);
				m_btnOpen.setEnabled(false);
			}
		} else {
			m_btnModify.setEnabled(true);
			m_btnDiscard.setEnabled(true);
			boolean bSel = true;
			int[] iSel = getBillListPanel().getHeadTable().getSelectedRows();
			for (int i = 0; i < iSelectedCnt; i++) {
				bSel = m_VOs[iSel[i]].isCanAudit();
				if (!bSel)
					break;
			}
			m_btnApprove.setEnabled(bSel);
			boolean bSelect = true;
			int[] iSelect = getBillListPanel().getHeadTable().getSelectedRows();
			for (int i = 0; i < iSelectedCnt; i++) {
				bSelect = m_VOs[iSelect[i]].isCanUnAuditPrayUI();
				if (!bSelect)
					break;
			}
			m_btnUnApprove.setEnabled(bSelect);
			// �ݲ�֧�������رա���
			m_btnClose.setEnabled(false);
			m_btnOpen.setEnabled(false);
		}
		// ˢ�°�ť״̬
		updateButtonsAll();
	}

	/**
	 * �б���ʱ�����������б�ť�߼����á�
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author czp
	 * @time 2007-3-14 ����10:21:56
	 */
	private void setButtonsListWhenErr() {
		setButtonsEnabled(false);
		/* ����:��ѯ,ˢ�� */
		m_btnBrowses.setEnabled(true);
		m_btnQuery.setEnabled(true);
		m_btnRefresh.setEnabled(m_bQueried);
		/* ˢ�°�ť״̬ */
		updateButtonsAll();
	}

	/**
	 * ��������:�����빺�����壺������ƣ������񣬴���ͺţ����أ���������������ַ���ɱ༭
	 * �����빺����ͷ��ҵ�����ͣ��빺��Դ�������ˣ��Ƶ��ˣ��������ڲ��ɱ༭
	 */
	private static void setPartNoEditable(BillCardPanel bcp) {
		BillItem item = bcp.getBodyItem("cinventoryname");
		item.setEnabled(false);
		item = bcp.getBodyItem("cinventoryspec");
		item.setEnabled(false);
		item = bcp.getBodyItem("cinventorytype");
		item.setEnabled(false);
		item = bcp.getBodyItem("cinventoryunit");
		item.setEnabled(false);
		item = bcp.getBodyItem("caddress");
		item.setEnabled(false);
		item = bcp.getBodyItem("cprodarea");
		item.setEnabled(false);
		item = bcp.getBodyItem("npraynum0");
		if (item != null)
			item.setEnabled(false);

		// item = bcp.getHeadItem("cbiztype");
		// item.setEnabled(false);
		item = bcp.getHeadItem("ipraysource");
		item.setEnabled(false);

		item = bcp.getTailItem("cauditpsn");
		item.setEnabled(false);
		item = bcp.getTailItem("coperator");
		item.setEnabled(false);
		item = bcp.getTailItem("dauditdate");
		item.setEnabled(false);

		bcp.getBodyItem("pk_reqcorp").isEdit();
	}

	/**
	 * ��Ŀ�׶οɱ༭��
	 */
	private void setProjPhaseEditable(BillEditEvent e) {
		final int n = e.getRow();
		if (n < 0)
			return;
		// �ж���Ŀ�Ƿ�Ϊ�ա���Ϊ�գ�����Ŀ�׶β��ɱ༭
		Object oTemp = getBillCardPanel().getBodyValueAt(n, "cprojectname");
		if ("cprojectname".equals(e.getKey())) {
			if (oTemp == null || oTemp.toString().length() == 0) {
				getBillCardPanel().getBillModel().setCellEditable(n,
						"cprojectphasename", false);
				getBillCardPanel().setBodyValueAt(null, n, "cprojectphasename");
			} else {
				getBillCardPanel().getBillModel().setCellEditable(
						n,
						"cprojectphasename",
						getBillCardPanel().getBillModel().getItemByKey(
								"cprojectphasename").isEdit());
				oTemp = getBillCardPanel().getBodyValueAt(n, "cprojectid");
				if (oTemp != null && oTemp.toString().length() > 0) {
					UIRefPane nRefPanel = (UIRefPane) getBillCardPanel()
							.getBodyItem("cprojectphasename").getComponent();
					nRefPanel.setIsCustomDefined(true);
					nRefPanel.setRefModel(new ProjectPhase((String) oTemp));

					getBillCardPanel().setBodyValueAt(null, n,
							"cprojectphasename");
				}
			}
		}
	}

	/**
	 * ���ø���������
	 * 
	 * @param bcp
	 * @param row
	 * @since V50
	 */
	private static void setRefPaneAssistunit(BillCardPanel bcp, int row) {
		Object cbaseid = bcp.getBillModel().getValueAt(row, "cbaseid");
		bcp.getBillModel().getValueAt(row, "cinventoryunit");

		UIRefPane ref = (UIRefPane) bcp.getBodyItem("cassistunitname")
				.getComponent();
		String wherePart = "bd_convert.pk_invbasdoc='" + cbaseid + "' ";
		ref.setWhereString(wherePart);
		StringBuffer unionPart = new StringBuffer();
		unionPart.append(" union all \n");
		unionPart
				.append("(select bd_measdoc.shortname,bd_measdoc.measname,bd_invbasdoc.pk_measdoc \n");
		unionPart.append("from bd_invbasdoc \n");
		unionPart.append("left join bd_measdoc  \n");
		unionPart.append("on bd_invbasdoc.pk_measdoc=bd_measdoc.pk_measdoc \n");
		unionPart.append("where bd_invbasdoc.pk_invbasdoc='" + cbaseid
				+ "') \n");
		ref.getRefModel().setGroupPart(unionPart.toString());
	}

	/**
	 * ���ò��յ���������
	 * 
	 * @return void
	 * @since 2001-04-28
	 */
	private void setRefPaneByPkcorp(String pk_corp) {

		if (pk_corp == null)
			return;

		BillData bd = getBillCardPanel().getBillData();
		int headItemsLength = bd.getHeadItems().length;
		String key;
		UIRefPane refpane;
		for (int i = 0; i < headItemsLength; i++) {
			if (bd.getHeadItems()[i].getDataType() == BillItem.UFREF) {
				key = bd.getHeadItems()[i].getKey();
				refpane = (UIRefPane) (bd.getHeadItem(key).getComponent());
				bd.getHeadItem(key).setComponent(
						getRefWherePart(refpane, pk_corp, key));
			}
		}
		int bodyItemsLength = bd.getBodyItems().length;
		for (int i = 0; i < bodyItemsLength; i++) {
			if (bd.getBodyItems()[i].getDataType() == BillItem.UFREF) {
				key = bd.getBodyItems()[i].getKey();
				refpane = (UIRefPane) (bd.getBodyItem(key).getComponent());
				bd.getBodyItem(key).setComponent(
						getRefWherePart(refpane, pk_corp, key));
			}
		}
		getBillCardPanel().setBillData(bd);
		nc.ui.pu.pub.PuTool.setTranslateRender(getBillCardPanel());
	}

	/**
	 * ����:�������������ʡ��������ɱ༭�ԡ��������������ݼ��ɱ༭��
	 * 
	 * @since V50
	 */
	public static void beforeEditBodyAssistUnitNumber(BillCardPanel bcp,
			int iRow) {
		try {
			String strCbaseid;
			String cassistunit;
			Object oTmp;
			String ass;
			String main;
			UFDouble ufdConv;
			// �Ƿ���и���������
			strCbaseid = (String) bcp.getBillModel()
					.getValueAt(iRow, "cbaseid");
			// �д��
			if (strCbaseid != null && !strCbaseid.trim().equals("")) {
				if (PuTool.isAssUnitManaged(strCbaseid)) {
					// ���ø���������
					setRefPaneAssistunit(bcp, iRow);
					cassistunit = (String) bcp.getBillModel().getValueAt(iRow,
							"cassistunit");
					// ��������Ϊ��
					oTmp = bcp.getBillModel().getValueAt(iRow, "nexchangerate");
					if (oTmp == null || oTmp.toString().trim().equals("")) {
						// ���û�����(���ԭ�����ڻ����������ã���Ϊ�������Ѿ��ı��˵ķǹ̶�������)
						ufdConv = PuTool.getInvConvRateValue(strCbaseid,
								cassistunit);
						bcp.getBillModel().setValueAt(ufdConv, iRow,
								"nexchangerate");
					}
					// ���ÿɱ༭��
					bcp.setCellEditable(iRow, "cassistunitname", true);
					bcp.setCellEditable(iRow, "npraynum", true);
					bcp.setCellEditable(iRow, "nassistnum", true);
					bcp.setCellEditable(iRow, "nmoney", true);
					bcp.getBodyItem("nexchangerate").setEdit(true);
					bcp.setCellEditable(iRow, "nexchangerate", true);
					// ����������ǹ̶�������
					if (PuTool.isFixedConvertRate(strCbaseid, cassistunit)) {
						bcp.setCellEditable(iRow, "nexchangerate", false);
					} else {
						bcp.setCellEditable(iRow, "nexchangerate", true);
					}
					// ���������������ͬ,�����ʲ��ɱ༭
					ass = (String) bcp.getBillModel().getValueAt(iRow,
							"cassistunitname");
					main = (String) bcp.getBillModel().getValueAt(iRow,
							"cinventoryunit");
					if (ass != null && ass.equals(main)) {
						bcp.getBillModel().setValueAt(new UFDouble(1), iRow,
								"nexchangerate");
						bcp.setCellEditable(iRow, "nexchangerate", false);
					}
				} else {
					bcp.setCellEditable(iRow, "npraynum", true);
					bcp.setCellEditable(iRow, "nmoney", true);
					bcp.setCellEditable(iRow, "nexchangerate", false);
					bcp.setCellEditable(iRow, "nassistnum", false);
					bcp.setCellEditable(iRow, "cassistunitname", false);
				}
			}
			// �޴��
			else {
				bcp.setCellEditable(iRow, "npraynum", false);
				bcp.setCellEditable(iRow, "nmoney", false);
				bcp.getBodyItem("nexchangerate").setEdit(false);
				bcp.setCellEditable(iRow, "nassistnum", false);
				bcp.setCellEditable(iRow, "cassistunitname", false);
			}
		} catch (Exception e) {
			Logger.debug("¼�����ʱ���ó���");
		}
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-10-28 14:42:54)
	 */
	private void setSendAuditBtnState() {
		if (getPraybillVOs() == null || getPraybillVOs().length <= 0) {
			m_btnSendAudit.setEnabled(false);
			return;
		}
		PraybillVO curVO = getPraybillVOs()[getCurVoPos()];
		if (curVO == null)
			return;
		// ����
		if (PuTool.isNeedSendToAudit(nc.vo.scm.pu.BillTypeConst.PO_PRAY, curVO)
				&& !isAlreadySendToAudit) {
			m_btnSendAudit.setEnabled(true);
		} else {
			m_btnSendAudit.setEnabled(false);
		}
	}

	/**
	 * ���ܣ��򵥾�ģ���м��뻺��VO(�빺��ά�����) ���ߣ���־ƽ ���ڣ�(2003-3-12 10:27:34)
	 */
	private void setVoToBillCard(int iRollBackPos, String strmsg) {
		String strMsg = null;
		if (strmsg == null || strmsg.trim().equals("")) {
			strMsg = "";
		} else {
			strMsg = strmsg.trim();
		}
		if (m_VOs == null || m_VOs.length <= 0) {
			Logger.debug("������������");
			m_nPresentRecord = iRollBackPos;
			String[] value = new String[] { strMsg };
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000440", null, value));
			return;
		}
		try {
			m_VOs[m_nPresentRecord] = PrTool
					.getRefreshedVO(m_VOs[m_nPresentRecord]);
			freshHeadCproject(new PraybillVO[] { m_VOs[m_nPresentRecord] });
			// ����������
			new nc.ui.scm.pub.FreeVOParse().setFreeVO(m_VOs[m_nPresentRecord]
					.getBodyVO(), "vfree", "vfree", "cbaseid", "cmangid", true);
			getBillCardPanel().getBillModel().clearBodyData();
		} catch (Exception be) {
			if (be instanceof BusinessException)
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000422")/* "ҵ���쳣" */, be
						.getMessage());
			m_nPresentRecord = iRollBackPos;
			String[] value = new String[] { strMsg };
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000440", null, value));
			return;
		}

		// ��ʾ���ŵ���
		for (int i = 0; i < 1; i++) {
			try {
				getBillCardPanel().setBillValueVO(m_VOs[m_nPresentRecord]);
			} catch (Exception e) {
				continue;
			}
		}
		nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillCardPanel());
		getBillCardPanel().getBillModel().execLoadFormula();
		// ��ʾ��Դ��Ϣ
		nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanel(),
				nc.vo.scm.pu.BillTypeConst.PO_PRAY);
		getBillCardPanel().updateValue();
		// ��ʾ�����б���Ϣ
		Integer nTemp1 = (Integer) m_VOs[m_nPresentRecord].getParentVO()
				.getAttributeValue("ipraysource");
		m_comPraySource.setSelectedIndex(nTemp1.intValue());
		if (m_VOs[m_nPresentRecord].getParentVO()
				.getAttributeValue("ipraytype") != null) {
			Integer nTemp2 = (Integer) m_VOs[m_nPresentRecord].getParentVO()
					.getAttributeValue("ipraytype");
			m_comPrayType.setSelectedIndex(nTemp2.intValue());
		}
		getBillCardPanel().updateUI();
		// ��ʾ��ע
		if (getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane) {
			UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel().getHeadItem(
					"vmemo").getComponent();
			nRefPanel3.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVmemo());
		}
		String[] value = new String[] { strMsg };
		showHintMessage(m_lanResTool.getStrByID("40040101",
				"UPP40040101-000085", null, value));
		// ����ͷ��ʽ���ݣ����ⲻ�ɲ��ճ�������
		// execHeadTailFormula(m_VOs[m_nPresentRecord]);
	}

	/**
	 * ���ܣ��򵥾ݽ�����ʾ�빺��(����������) ע�⣺��ʱ�� m_VOs �Ѿ�������������Ϣ ������ ���أ� ���⣺ ���ڣ�(2002-6-27
	 * 14:06:10) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private void setVoToBillPanel() {
		if (m_VOs != null && m_VOs.length > 0) {
			// Ĭ����ʾ��һ��
			m_nPresentRecord = 0;
			// ���ر���
			try {
				m_VOs[m_nPresentRecord] = PrTool
						.getRefreshedVO(m_VOs[m_nPresentRecord]);
				// ����������
				new nc.ui.scm.pub.FreeVOParse().setFreeVO(
						m_VOs[m_nPresentRecord].getBodyVO(), "vfree", "vfree",
						"cbaseid", "cmangid", true);
			} catch (Exception be) {
				if (be instanceof BusinessException)
					MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000422")/* "ҵ���쳣" */,
							be.getMessage());
				return;
			}
			if (!(m_VOs[m_nPresentRecord].getHeadVO().getPk_corp()
					.equals(m_sLoginCorpId))) {
				setRefPaneByPkcorp(m_VOs[m_nPresentRecord].getHeadVO()
						.getPk_corp());
				setDecimalDigits(m_VOs[m_nPresentRecord].getHeadVO()
						.getPk_corp(), 0);
			}
			// ���ز�ѯ���
			Integer nTemp1 = (Integer) m_VOs[m_nPresentRecord].getHeadVO()
					.getAttributeValue("ipraysource");
			Integer nTemp2 = (Integer) m_VOs[m_nPresentRecord].getHeadVO()
					.getAttributeValue("ipraytype");
			m_comPraySource.setSelectedIndex(nTemp1.intValue());
			m_comPrayType.setSelectedIndex(nTemp2.intValue());
			Vector v = new Vector();
			for (int i = 0; i < m_VOs.length; i++)
				v.addElement(m_VOs[i].getHeadVO());
			PraybillHeaderVO[] headVO = new PraybillHeaderVO[v.size()];
			v.copyInto(headVO);
			getBillCardPanel().setBillValueVO(m_VOs[m_nPresentRecord]);
			nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillCardPanel());
			getBillCardPanel().getBillModel().execLoadFormula();
			// ��ʾ��Դ��Ϣ
			nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanel(),
					nc.vo.scm.pu.BillTypeConst.PO_PRAY);
			// ����ͷ��ʽ���ݣ����ⲻ�ɲ��ճ�������
			// execHeadTailFormula(m_VOs[m_nPresentRecord]);
			getBillCardPanel().updateValue();
			// ��ʾ��ע
			if (getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane) {
				UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel()
						.getHeadItem("vmemo").getComponent();
				nRefPanel3.setValue(m_VOs[m_nPresentRecord].getHeadVO()
						.getVmemo());
			}
			m_btnWorkFlowBrowse.setEnabled(true);
			m_btnAudit.setEnabled(true);
			updateButtons();
		} else {
			m_VOs = null;
			// �޷��ز�ѯ���
			MessageDialog
					.showHintDlg(this, m_lanResTool.getStrByID("40040101",
							"UPP40040101-000055")/* "�빺����ѯ" */, m_lanResTool
							.getStrByID("40040101", "UPP40040101-000056")/* "û�з����������빺����" */);
			// �������
			getBillCardPanel().getBillData().clearViewData();
			getBillCardPanel().updateUI();
			m_btnWorkFlowBrowse.setEnabled(false);
			m_btnAudit.setEnabled(false);
			updateButtons();
		}
	}

	/**
	 * ���ߣ����� ���ܣ��趨ĳ�е����κŵĿɱ༭�ԣ� ������iRow int ���κ����ڵ��У� ���أ��� ���⣺�� ���ڣ�(2002-6-25
	 * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private void setVProduceNumEditState(int iRow) {

		String sMangId = (String) getBillCardPanel().getBodyValueAt(iRow,
				"cmangid");
		if (sMangId == null || sMangId.trim().length() < 1) {
			// =====�ɱ༭
			getBillCardPanel().setCellEditable(iRow, "vproducenum", true);

		} else {
			// =====�Ƿ�ɱ༭
			getBillCardPanel().setCellEditable(iRow, "vproducenum",
					nc.ui.pu.pub.PuTool.isBatchManaged(sMangId));
		}
	}

	/**
	 * ���ߣ���־ƽ ���ܣ�ʵ��ListSelectionListener�ļ������� ������ListSelectionEvent e �����¼� ���أ���
	 * ���⣺�� ���ڣ�(2002-5-23 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void valueChanged(ListSelectionEvent e) {
		/**/
		boolean isErr = false;
		/* ѡȡ������ */
		int iSelCnt = -1;
		int iSelFirstRow = -1;
		/* �������г�ʼΪδѡ�� */
		final int iCount = getBillListPanel().getHeadTable().getRowCount();
		for (int i = 0; i < iCount; i++) {
			getBillListPanel().getHeadBillModel().setRowState(i,
					BillModel.UNSTATE);
		}
		/* �õ���ѡ�е��� */
		int[] selectedRows = getBillListPanel().getHeadTable()
				.getSelectedRows();
		if (selectedRows != null && selectedRows.length > 0) {
			iSelCnt = selectedRows.length;
			iSelFirstRow = selectedRows[0];
		}
		/* ��ѡ�е��б�ʾΪ�򣪺� */
		if (iSelCnt > 0) {
			for (int i = 0; i < iSelCnt; i++) {
				getBillListPanel().getHeadBillModel().setRowState(
						selectedRows[i], BillModel.SELECTED);
			}
		}
		/* ��ʾ��ѡ�е��кͱ��� */
		if (iSelCnt != 1) {
			getBillListPanel().getBodyBillModel().setBodyDataVO(null);
		} else {
			/* ���ҽ�����ͷֻ��һ�б�ѡ��ʱ:���ñ���λ��(֧������) */
			m_nPresentRecord = nc.ui.pu.pub.PuTool.getIndexBeforeSort(
					getBillListPanel(), iSelFirstRow);
			/* �����ʱ�ż��ر��� */
			try {
				if (m_VOs != null && m_VOs.length > 0
						&& m_VOs[m_nPresentRecord].getBodyLen() == 0) {
					m_VOs[m_nPresentRecord] = PrTool
							.getRefreshedVO(m_VOs[m_nPresentRecord]);
					// ����������
					new nc.ui.scm.pub.FreeVOParse().setFreeVO(
							m_VOs[m_nPresentRecord].getBodyVO(), "vfree",
							"vfree", "cbaseid", "cmangid", true);

				}
			} catch (Exception be) {
				getBillListPanel().getBodyBillModel().clearBodyData();
				if (be instanceof BusinessException)
					MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000422")/* "ҵ���쳣" */,
							be.getMessage());
				isErr = true;
			}
			/* ������� */
			PraybillItemVO[] bodyVO = m_VOs[m_nPresentRecord].getBodyVO();
			getBillListPanel().getBodyBillModel().setBodyDataVO(bodyVO);
			nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillListPanel());
			getBillListPanel().getBodyBillModel().execLoadFormula();
			/* ���ص�����Դ��Ϣ */
			nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillListPanel(),
					nc.vo.scm.pu.BillTypeConst.PO_PRAY);
			getBillListPanel().getBodyBillModel().updateValue();
			getBillListPanel().updateUI();
		}
		/* �����б�ť�߼� */
		setButtonsList();
		/* ���б��־ */
		m_nUIState = 1;
		showHintMessage(m_lanResTool.getStrByID("SCMCOMMON",
				"UPPSCMCommon-000393")/* "�������" */);
		if (isErr) {
			setButtonsListWhenErr();
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000086")/* "����ʱ���ر�����ֲ���" */);
		}
	}

	/**
	 * ������ӡ�ӿڣ�ʵ����Ƭ�������ݣ�����ɿ�Ƭ��˾���������õ�
	 * 
	 * @see nc.ui.scm.pub.print.ISetBillVO#setBillVO(nc.vo.pub.AggregatedValueObject)
	 */
	public void setBillVO(AggregatedValueObject vo) {
		if (vo == null) {
			Logger.debug("������");
			return;
		}
		try {
			vo = PrTool.getRefreshedVO((PraybillVO) vo);
			// ����������
			new nc.ui.scm.pub.FreeVOParse().setFreeVO(((PraybillVO) vo)
					.getBodyVO(), "vfree", "vfree", "cbaseid", "cmangid", true);
			getBillCardPanel().getBillModel().clearBodyData();
		} catch (Exception be) {
			if (be instanceof BusinessException)
				MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000422")/* "ҵ���쳣" */, be
						.getMessage());
			return;
		}

		// ��ʾ���ŵ���
		for (int i = 0; i < 1; i++) {
			try {
				getBillCardPanel().setBillValueVO(vo);
			} catch (Exception e) {
				continue;
			}
		}
		nc.ui.pu.pub.PuTool.loadInvMaxPrice(getBillCardPanel());
		getBillCardPanel().getBillModel().execLoadFormula();
		// ��ʾ��Դ��Ϣ
		nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanel(),
				nc.vo.scm.pu.BillTypeConst.PO_PRAY);
		getBillCardPanel().updateValue();
		// ��ʾ�����б���Ϣ
		Integer nTemp1 = (Integer) vo.getParentVO().getAttributeValue(
				"ipraysource");
		Integer nTemp2 = (Integer) vo.getParentVO().getAttributeValue(
				"ipraytype");
		m_comPraySource.setSelectedIndex(nTemp1.intValue());
		m_comPrayType.setSelectedIndex(nTemp2.intValue());
		getBillCardPanel().updateUI();
		// ��ʾ��ע
		if (getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane) {
			UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel().getHeadItem(
					"vmemo").getComponent();
			nRefPanel3.setValue(((PraybillHeaderVO) ((PraybillVO) vo)
					.getParentVO()).getVmemo());
		}
		// execHeadTailFormula((PraybillVO) vo);
	}

	/** Ϊ���ο����ṩ�ӿ� ********************************** */
	/**
	 * ���Ƿ�����nc.ui.pr.pray.PrayUI.java.getExtendBtns �������ڣ�2005-9-22
	 * ������������ȡ��չ��ť���飨ֻ�ṩ��Ƭ���水ť��
	 */
	public ButtonObject[] getExtendBtns() {
		return null;
	}

	/**
	 * ���Ƿ�����nc.ui.pr.pray.PrayUI.java.onExtendBtnsClick �������ڣ�2005-9-22
	 * ����������������չ��ť���¼�
	 */
	public void onExtendBtnsClick(ButtonObject bo) {
		// ����ʵ��
	}

	/**
	 * ���Ƿ�����nc.ui.pr.pray.PrayUI.java.setExtendBtnsStat �������ڣ�2005-9-22
	 * ����������������չ��ť״̬ ״ֵ̬������0---��ʼ�� 1---��Ƭ��� 2---��Ƭ�༭ 3---�б�������ݲ�֧�֣�
	 */
	public void setExtendBtnsStat(int iState) {
		// ����ʵ��
	}

	/**
	 * @���ܣ�����༭�����òɹ�Ա���ɹ���֯���ɹ���˾
	 */
	public static String[] setCpurorganization(BillCardPanel bcp, int iBgn,
			int iEnd) throws BusinessException {
		int iLen = iEnd - iBgn + 1;
		String[] cpk_purcorp = new String[iLen];
		try {
			String beanNameA = IScmPosInv.class.getName();
			IScmPosInv boA = (IScmPosInv) nc.bs.framework.common.NCLocator
					.getInstance().lookup(beanNameA);
			BillModel bm = bcp.getBillModel();

			String[] sInvbasIDs = new String[iLen];
			String[] sCalbodyIDs = new String[iLen];
			for (int i = iBgn; i <= iEnd; i++) {
				if (PuPubVO.getString_TrimZeroLenAsNull(bcp.getBillModel()
						.getValueAt(i, "cbaseid")) != null
						&& PuPubVO.getString_TrimZeroLenAsNull(bcp
								.getBillModel().getValueAt(i, "pk_reqstoorg")) != null) {
					sInvbasIDs[i - iBgn] = PuPubVO
							.getString_TrimZeroLenAsNull(bcp.getBillModel()
									.getValueAt(i, "cbaseid"));
					sCalbodyIDs[i - iBgn] = PuPubVO
							.getString_TrimZeroLenAsNull(bcp.getBillModel()
									.getValueAt(i, "pk_reqstoorg"));
				}
				// ��ղɹ���֯,����������,�ɹ���˾���óɵ�ǰ��˾
				bm.setValueAt(null, i, "cpurorganization");
				// ��������������¼�Ĳɹ���ֻ֯����Ϊ��ǰ��˾
				bm.setValueAt(ClientEnvironment.getInstance().getCorporation()
						.getPk_corp(), i, "pk_purcorp");
			}
			queryProduceInfo(sInvbasIDs, sCalbodyIDs);
			// ����Ǵ��������������л�ȡ�Ĳɹ���֯,��ô���Ϊtrue.
			boolean[] bIsProduce = new boolean[iLen];
			for (int i = iBgn; i <= iEnd; i++) {
				String cbasid = PuPubVO.getString_TrimZeroLenAsNull(bcp
						.getBillModel().getValueAt(i, "cbaseid"));
				String cCalbodyid = PuPubVO.getString_TrimZeroLenAsNull(bcp
						.getBillModel().getValueAt(i, "pk_reqstoorg"));
				if (cbasid != null
						&& cCalbodyid != null
						&& h_ProduceInfo.containsKey(cbasid + cCalbodyid)
						&& h_ProduceInfo.get(cbasid + cCalbodyid)[0]
								.equals("MR")) {
					// ���òɹ���֯Ϊ��������������¼�Ĳɹ���֯
					bm.setValueAt(h_ProduceInfo.get(cbasid + cCalbodyid)[1], i,
							"cpurorganization");
					// ��������������¼�Ĳɹ���ֻ֯����Ϊ��ǰ��˾
					bm.setValueAt(ClientEnvironment.getInstance()
							.getCorporation().getPk_corp(), i, "pk_purcorp");
					bIsProduce[i - iBgn] = true;
				}
			}

			// ���òɹ�Ա���ɹ���֯ID���ɹ���˾
			ArrayList<String> alWhere = new ArrayList<String>();
			queryCpurorganization(bcp, 0, bcp.getBillModel().getRowCount());
			Object cbaseid = null;
			Object reqstoorg = null;
			InvSourceVO invSourceVO = null;
			for (int i = iBgn; i <= iEnd; i++) {
				invSourceVO = null;
				cbaseid = null;
				reqstoorg = null;
				cbaseid = bm.getValueAt(i, "cbaseid");
				reqstoorg = bm.getValueAt(i, "pk_reqstoorg");
				// ����ӻ��������ҵ��������ֱ�ӷ��أ�������Ҫȫ���ٴμ���һ��
				if (cbaseid != null && reqstoorg != null) {
					if (h_InvSource.containsKey(cbaseid.toString()
							+ reqstoorg.toString())) {
						invSourceVO = (InvSourceVO) h_InvSource.get(cbaseid
								.toString()
								+ reqstoorg.toString());
					}
				}
				if (!bIsProduce[i - iBgn] && invSourceVO != null
						&& invSourceVO.getPurOrgan() != null
						&& invSourceVO.getPurOrgan().trim().length() > 0) {
					String Pk_corp = invSourceVO.getPurCorp();
					bm.setValueAt(boA.getPurchaser(Pk_corp,
							cbaseid == null ? null : cbaseid.toString()), i,
							"cemployeeid");
					// �ɹ���֯ID
					bm.setValueAt(invSourceVO.getPurOrgan(), i,
							"cpurorganization");
					// �ɹ���˾
					bm.setValueAt(Pk_corp, i, "pk_purcorp");

					String directtransit = bcp.getHeadItem("isdirecttransit")
							.getValue();
					if ("Y".equalsIgnoreCase(directtransit)) {// Դ��ֱ�����۶������빺��Ҫ��֤�ɹ���֯�������ڵ�ǰ�빺��˾
						if (Pk_corp.equalsIgnoreCase(invSourceVO.getPurCorp())) {
							bcp.getBillModel().setValueAt(
									invSourceVO.getPurOrgan(), i,
									"cpurorganization");
						}
					} else {
						if (!Pk_corp.equalsIgnoreCase(invSourceVO.getPurCorp())) {// ����ԭ�嵥ƥ�䵽�Ĳɹ��鲻���ڵ�ǰ��˾����ɹ���֯�ɲ����޸ģ���Ϊ��ǰ��˾�޲ɹ�Ȩ��ֻ�ܼ���
							UIRefPane nRefPanel = (UIRefPane) bcp.getBodyItem(
									"cpurorganization").getComponent();
							if (nRefPanel != null) {
								nRefPanel.setEnabled(false);
							}
						}
					}
				}
			}
			for (int i = iBgn; i < iEnd; i++) {
				// ��ûƥ�䵽�ɹ���֯������������������ȡ�ɹ���֯
				if (PuPubVO.getString_TrimZeroLenAsNull(bcp.getBodyValueAt(i,
						"cpurorganization")) == null) {
					String cbasid = PuPubVO.getString_TrimZeroLenAsNull(bcp
							.getBillModel().getValueAt(i, "cbaseid"));
					String cCalbodyid = PuPubVO.getString_TrimZeroLenAsNull(bcp
							.getBillModel().getValueAt(i, "pk_reqstoorg"));
					if (h_ProduceInfo.containsKey(cbasid + cCalbodyid)) {
						bcp.getBillModel().setValueAt(
								h_ProduceInfo.get(cbasid + cCalbodyid)[1], i,
								"cpurorganization");
					}
				}
				cpk_purcorp[i - iBgn] = (String) bcp.getBodyValueAt(i,
						"pk_purcorp");
			}
			/** ************************************************ */
			bcp.getBillModel().execLoadFormulasByKey("cpurorganizationname");
			bcp.getBillModel().execLoadFormulasByKey("purcorpname");
		} catch (Exception e) {
			PuTool.outException(e);
		}
		return cpk_purcorp;
	}

	/**
	 * V5 ,֧���û����岻�ɱ༭��Ŀ�޸�
	 */
	public boolean beforeEdit(BillItemEvent e) {
		if (!e.getItem().isEdit()) {
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * ���򷽷�������Ҫ����Ļ���VO����
	 * 
	 * @since V50
	 */
	public Object[] getRelaSortObjectArray() {
		return m_VOs;
	}

	/**
	 * <p>
	 * ���򷽷�������Ҫ����ĵ�ǰVO����VO����
	 * 
	 * @since V50
	 */
	public Object[] getRelaSortObjectArrayBody() {
		if (m_VOs != null && m_VOs.length > 0 && m_nPresentRecord >= 0
				&& m_VOs[m_nPresentRecord] != null
				&& m_VOs[m_nPresentRecord].getBodyVO() != null) {
			return m_VOs[m_nPresentRecord].getBodyVO();
		}
		return null;
	}

	/**
	 * ��������ӿڷ���ʵ�� -- ά��
	 */
	public void doMaintainAction(ILinkMaintainData maintaindata) {
		SCMEnv.out("����ά���ӿ�...");

		String billID = maintaindata.getBillID();

		initi();

		PraybillVO vo = null;
		m_VOs = null;
		try {
			vo = PraybillHelper.queryPrayVoByHid(billID);
		} catch (Exception e) {
			SCMEnv.out(e);
		}
		// �����ز�ѯ���

		// �����ǰ��¼��˾���ǲ���Ա�Ƶ����ڹ�˾��������޲�����ť�����ṩ������ܣ�by chao , xy , 2006-11-07
		String strLoginCorpId = PoPublicUIClass.getLoginPk_corp();
		String strPrayCorpId = vo == null ? null : vo.getHeadVO().getPk_corp();
		boolean bSameCorpFlag = strLoginCorpId.equals(strPrayCorpId);

		// �����ݴ���
		if (vo == null) {
			MessageDialog
					.showHintDlg(this, m_lanResTool.getStrByID("40040101",
							"UPP40040101-000055")/* "�빺����ѯ" */, m_lanResTool
							.getStrByID("40040101", "UPP40040101-000056")/* "û�з����������빺����" */);
			// �������
			getBillCardPanel().getBillData().clearViewData();
			// ���ð�ť�߼�
			if (bSameCorpFlag) {
				setButtonsCard();
			} else {
				setButtonsNull();
			}
			return;
		}

		// �����ݴ���
		m_VOs = new PraybillVO[] { vo };
		m_nPresentRecord = 0;

		// �����ͷ�����б�
		Integer nTemp1 = (Integer) m_VOs[0].getHeadVO().getAttributeValue(
				"ipraysource");
		Integer nTemp2 = (Integer) m_VOs[0].getHeadVO().getAttributeValue(
				"ipraytype");
		m_comPraySource.setSelectedIndex(nTemp1.intValue());
		m_comPrayType.setSelectedIndex(nTemp2.intValue());

		// ���ص��ݵ���Ƭ
		m_nPresentRecord = 0;
		setVoToBillCard(m_nPresentRecord, null);

		// ���ð�ť�߼�
		setButtonsCard();

		//
		m_nUIState = 0;
		// ˢ�°�ť״̬
		updateButtonsAll();
		//
		m_bAdd = false;
		m_bEdit = false;
		getBillCardPanel().setEnabled(false);

		// ��������ڱ���˾�Ƶ�����������а�ť
		if (bSameCorpFlag) {
			// �Ѿ��������빺�������޸�
			int iBillStatus = -1;
			if (m_VOs[m_nPresentRecord].getHeadVO().getIbillstatus() != null) {
				iBillStatus = m_VOs[m_nPresentRecord].getHeadVO()
						.getIbillstatus().intValue();
			}
			//
			// if (iBillStatus == 0 || iBillStatus == 2) {
			// onModify(false, false);
			// }
		} else {
			setButtonsNull();
		}
	}

	/**
	 * ��������ӿڷ���ʵ�� -- ����
	 */
	public void doAddAction(ILinkAddData adddata) {
		SCMEnv.out("���������ӿ�...");

	}

	/**
	 * ��������ӿڷ���ʵ�� -- ����
	 */
	public void doApproveAction(ILinkApproveData approvedata) {
		SCMEnv.out("���������ӿ�...");
		if (approvedata == null)
			return;
		isRevise = true;
		String billID = approvedata.getBillID();
		/* ��ʼ�� */
		initi();
		isCanAutoAddLine = false;
		/* ƽ̨��Ҫ */
		setCauditid(billID);
		/* ��ѯ�빺�� */
		PraybillVO vo = null;
		try {
			vo = (PraybillVO) getVo();
		} catch (Exception e) {
			SCMEnv.out(e);
			MessageDialog
					.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
							"UPPSCMCommon-000270")/* "��ʾ" */, m_lanResTool
							.getStrByID("SCMCOMMON", "UPPSCMCommon-000428")/* "ϵͳ���ϣ�" */);
		}

		/* �޵��ݵĴ��� */
		if (vo == null) {
			m_VOs = null;
			/* ����ť������ */
			for (int i = 0; i < m_btnsAuditCenter.length; i++) {
				m_btnsAuditCenter[i].setEnabled(false);
			}
			for (int i = 0; i < m_btnsAuditCenter.length; i++) {
				updateButton(m_btnsAuditCenter[i]);
			}
			setButtonsNull();
			return;
		}
		/* �����빺��VO���� */
		m_VOs = (new PraybillVO[] { vo });
		m_nPresentRecord = 0;
		/* ���ÿ�Ƭ�������� */
		setVoToBillPanel();
		//
		// setEdit(false);
		// ���ð�ť��
		boolean bCorpSameFlag = getCorpPrimaryKey().equals(vo.getPk_corp());
		if (bCorpSameFlag) {
			setButtons(getBtnTree().getButtonArray());
			setButtonsCard();
		} else {
			setButtons(m_btnsAuditCenter);
			/* ����ť���� */
			for (int i = 0; i < m_btnsAuditCenter.length; i++) {
				if (m_btnsAuditCenter[i] == m_btnUnAudit
						&& 2 == vo.getHeadVO().getIbillstatus()) {// �����в�������
					m_btnsAuditCenter[i].setEnabled(false);
				} else {
					m_btnsAuditCenter[i].setEnabled(true);
				}
				if (m_btnsAuditCenter[i].getChildCount() > 0) {
					for (int j = 0; j < m_btnsAuditCenter[i].getChildCount(); j++) {
						m_btnsAuditCenter[i].getChildButtonGroup()[j]
								.setEnabled(true);
						updateButton(m_btnsAuditCenter[i].getChildButtonGroup()[j]);
					}
				}
			}
			for (int i = 0; i < m_btnsAuditCenter.length; i++) {
				updateButton(m_btnsAuditCenter[i]);
			}
		}

		getBillCardPanel().setEnabled(false);
		setButtonsRevise(isCurAuditOP(vo));
	}

	/**
	 * �жϵ�ǰ����Ա�Ƿ�Ϊ��ǰ������
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param vo
	 * @return
	 *            <p>
	 * @author donggq
	 * @time 2008-10-24 ����10:33:20
	 */
	private boolean isCurAuditOP(PraybillVO vo) {
		boolean isCurAuditOPER = false;
		try {
			if (vo != null
					&& ipflowqry.isCheckman(vo.getHeadVO().getPrimaryKey(),
							ScmConst.PO_Pray, getClientEnvironment().getUser()
									.getPrimaryKey())) {
				isCurAuditOPER = true;
			}
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
		}
		return isCurAuditOPER;
	}

	/**
	 * ����������������Ҫ�����������Ĺ��ܡ�
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param isCurAuditOPER
	 *            �Ƿ�ǰ������
	 *            <p>
	 * @author donggq
	 * @time 2008-10-24 ����10:26:37
	 */
	private void setButtonsRevise(boolean isCurAuditOPER) {
		if (isRevise) {
			if (isCurAuditOPER) {
				m_btnRevise.setVisible(true);
			} else {
				m_btnRevise.setVisible(false);
			}
			m_btnAdds.setEnabled(false);
			m_btnCopy.setEnabled(false);
			m_btnDiscard.setEnabled(false);
			m_btnDiscardList.setEnabled(false);
			updateButton(m_btnDiscard);
			updateButton(m_btnDiscardList);
			updateButton(m_btnCopy);
			updateButton(m_btnAdds);
			updateButton(m_btnModify);
			updateButton(m_btnModifyList);
			updateButton(m_btnRevise);
		} else {
			m_btnRevise.setVisible(false);
		}

		if (m_VOs != null) {
			Integer iBillStatus = PuPubVO.getInteger_NullAs(
					m_VOs[m_nPresentRecord].getParentVO().getAttributeValue(
							"ibillstatus"), new Integer(BillStatus.FREE));
			String strPsnOld = m_VOs[m_nPresentRecord].getHeadVO()
					.getCauditpsn();
			if (iBillStatus.intValue() == 2
					&& PuPubVO.getString_TrimZeroLenAsNull(strPsnOld) == null) {
				m_btnModify.setEnabled(true);
				m_btnModifyList.setEnabled(true);
			}
		}
	}

	/**
	 * ��������ӿڷ���ʵ�� -- ������
	 * 
	 * @modified by czp since v51, ��������Ȩ��
	 */
	public void doQueryAction(ILinkQueryData querydata) {
		SCMEnv.out("����������ӿ�...");

		String billID = querydata.getBillID();

		initi();

		PraybillVO vo = null;

		try {
			// �Ȱ��յ���PK��ѯ���������Ĺ�˾corpvalue
			vo = PraybillHelper.queryPrayVoByHid(billID);
			if (vo == null) {
				MessageDialog
						.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000270")/* "��ʾ" */, m_lanResTool
								.getStrByID("common", "SCMCOMMON000000161")/* "û�в쿴���ݵ�Ȩ��" */);
				return;
			}
			//
			String strPkCorp = vo.getPk_corp();
			// ���յ���������˾���ز�ѯģ��
			PrayUIQueryDlg queryDlg = new PrayUIQueryDlg(this, strPkCorp);// ��ѯģ����û�й�˾ʱ��Ҫ�������⹫˾
			queryDlg.setDefaultValue("po_praybill.dpraydate",
					"po_praybill.dpraydate", "");
			queryDlg.initCorpRefs();
			// ���ù���������ȡ�ù�˾�п���Ȩ�޵ĵ�������VO����
			QueryConditionVO[] condVOs = queryDlg.getConditionDatas();
			PuTool.switchReturnType(PrayPubVO._Hash_PrayUI,
					PrayPubVO._Hash_PrayUI_Code_Flag, condVOs, true);
			ConditionVO[] voaCond = queryDlg.getDataPowerConVOs(strPkCorp,
					PrayUIQueryDlg.REFKEYS);
			PuTool.switchReturnType(PrayPubVO._Hash_PrayUI,
					PrayPubVO._Hash_PrayUI_Code_Flag, condVOs, false);
			// ��֯�ڶ��β�ѯ���ݣ�����Ȩ�޺͵���PK����
			PraybillVO[] voaRet = PraybillHelper.queryAll(null, voaCond, null,
					null, "po_praybill.cpraybillid = '" + billID + "' ",
					getClientEnvironment().getUser().getPrimaryKey());
			if (voaRet == null || voaRet.length <= 0 || voaRet[0] == null) {
				MessageDialog
						.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON",
								"UPPSCMCommon-000270")/* "��ʾ" */, m_lanResTool
								.getStrByID("common", "SCMCOMMON000000161")/* "û�в쿴���ݵ�Ȩ��" */);
				setButtonsNull();
				return;
			}
			m_VOs = new PraybillVO[] { vo };
			m_nPresentRecord = 0;
			setVoToBillCard(m_nPresentRecord, "");
			getBillCardPanel().setEnabled(false);
			Logger.debug("�ɹ���ʾ����");
		} catch (Exception e) {
			SCMEnv.out(e);
			MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000270")/* "��ʾ" */, e
					.getMessage());
			setButtonsNull();
			return;
		}
		boolean bCorpSameFlag = getCorpPrimaryKey().equals(vo.getPk_corp());
		// ���ð�ť��
		if (bCorpSameFlag) {
			setButtons(getBtnTree().getButtonArray());
			setButtonsCard();
		} else {
			setButtonsNull();
		}
	}

	/**
	 * ��յ�ǰ���水ť
	 */
	private void setButtonsNull() {
		ButtonObject[] objs = getButtons();
		int iLen = objs == null ? 0 : objs.length;
		for (int i = 0; i < iLen; i++) {
			if (objs[i] == null) {
				continue;
			}
			objs[i].setVisible(false);
			updateButton(objs[i]);
		}
	}

	/**
	 * �ϲ���ʾ����ӡ����
	 * 
	 * @since v50
	 */
	private void onCombin() {
		CollectSettingDlg dlg = new CollectSettingDlg(this,
				NCLangRes.getInstance().getStrByID("common",
						"4004COMMON000000089")/* @res "�ϲ���ʾ" */,
				ScmConst.PO_Pray, "40040101", getCorpPrimaryKey(),
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				PraybillVO.class.getName(), PraybillHeaderVO.class.getName(),
				PraybillItemVO.class.getName());
		//
		JComponent tempCom = null;
		if (getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane) {
			tempCom = getBillCardPanel().getHeadItem("vmemo").getComponent();
			getBillCardPanel().getHeadItem("vmemo")
					.setDataType(BillItem.STRING);
		}
		dlg.initData(getBillCardPanel(),
		// �̶�������
				new String[] { "cinventorycode", "cinventoryname",
						"cinventoryspec", "cinventorytype", "cprodarea" },
				// ȱʡ������
				null,
				// �����
				new String[] { "nmoney", "npraynum" },
				// ��ƽ����
				null,
				// ���Ȩƽ����
				new String[] { "nsuggestprice" },
				// ������
				"npraynum");
		dlg.showModal();
		if (tempCom != null) {
			getBillCardPanel().getHeadItem("vmemo").setDataType(BillItem.UFREF);
			getBillCardPanel().getHeadItem("vmemo").setComponent(tempCom);
		}
		showHintMessage(m_lanResTool
				.getStrByID("common", "4004COMMON000000039")/*
															 * @res "�ϲ���ʾ���"
															 */);
	}

	/**
	 * ������¼�,��Ҫ��Ϊ�˴����BillModel���ݵ��������Զ���Ļ������ؼ�
	 * 
	 * @author czp
	 * @since v50
	 */
	public void afterSort(String key) {
		// ������ʾ
		if (getBillCardPanel().getBillData().getEnabled()) {
			PraybillVO voCurr = (PraybillVO) getBillCardPanel().getBillValueVO(
					PraybillVO.class.getName(),
					PraybillHeaderVO.class.getName(),
					PraybillItemVO.class.getName());
			InvAttrCellRenderer ficr = new InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanel(), voCurr);
		}
	}

	public OnHandRefreshVO getSelectedItemHandInfo() {
		int row = getBillCardPanel().getBillTable().getSelectedRow();
		PraybillVO billVO = (PraybillVO) getBillCardPanel().getBillValueVO(
				PraybillVO.class.getName(), PraybillHeaderVO.class.getName(),
				PraybillItemVO.class.getName());
		if (billVO == null || billVO.getChildrenVO() == null
				|| billVO.getChildrenVO().length == 0)
			return null;
		if (row < 0 || row >= billVO.getChildrenVO().length)
			return null;
		PraybillItemVO billitem = (billVO.getBodyVO())[row];

		String[] saParam = new String[] { "ccorpid", "ccalbodyid", "cwhid",
				"cinvbasdoc", "cinvid", "cspaceid", "vfree1", "vfree2",
				"vfree3", "vfree4", "vfree5" };
		String[] saOrg = new String[] { "pk_corp", "pk_reqstoorg",
				"cwarehouseid", "cbaseid", "cmangid", "vproducenum", "vfree1",
				"vfree2", "vfree3", "vfree4", "vfree5" };
		if ((billitem.getAttributeValue("pk_corp") == null || billitem
				.getAttributeValue("pk_corp").toString().trim().length() == 0)
				|| ((billitem.getAttributeValue("cmangid") == null || billitem
						.getAttributeValue("cmangid").toString().trim()
						.length() == 0))) {
			return null;
		}
		for (int i = 0; i < saParam.length; i++) {
			m_voLineOnHand.setAttributeValue(saParam[i], billitem
					.getAttributeValue(saOrg[i]));
		}

		return m_voLineOnHand;

	}

	/**
	 * ����ǵ�һ�Σ����в�ѯ�빺���ж�Ӧ�̶���ǰ�ڣ������Hashtable�л�ȡ����Ϣ���ڹ�ϣ����
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param PraybillItemVO
	 *            bodyVO
	 * @return HashMap:
	 *         <p>
	 *         KEY:��˾ID�������֯ID���������ID+�빺����;
	 *         <p>
	 *         <p>
	 *         VALUE:�ɹ���ǰ������=�̶���ǰ��+(��������-��ǰ������)*��ǰ��ϵ��/��ǰ������
	 *         <p>
	 * @author lixiaodong
	 * @time 2008-02-27 ����13:44:12
	 */
	private HashMap loadAdvancedate(PraybillItemVO bodyVO[]) {
		if (bodyVO == null || bodyVO.length < 1) {
			return null;
		}

		Vector<String> vecBaseId = new Vector<String>();
		Vector<String> vecStorId = new Vector<String>();
		Vector<String> vecCorp = new Vector<String>();
		Vector<UFDouble> vecOrderNum = new Vector<UFDouble>();
		// ��ϣ��Ϊ�գ���������
		if (m_hmapPurAdvanceInfo == null) {
			m_hmapPurAdvanceInfo = new HashMap();
		}

		for (int i = 0; i < bodyVO.length; i++) {
			if (m_hmapPurAdvanceInfo.containsKey(bodyVO[i].getPk_corp()
					+ bodyVO[i].getPk_reqstoorg() + bodyVO[i].getCbaseid()
					+ bodyVO[i].getNpraynum())) {
				continue;
			}

			UFDate dOrderDate = (UFDate) bodyVO[i]
					.getAttributeValue("ddemanddate");// ��������
			if (dOrderDate == null) {
				continue;
			}
			String sBaseId = PuPubVO
					.getString_TrimZeroLenAsNull((String) bodyVO[i]
							.getAttributeValue("cbaseid"));// �������id
			if (sBaseId == null) {
				continue;
			}
			String sStoreId = PuPubVO
					.getString_TrimZeroLenAsNull((String) bodyVO[i]
							.getAttributeValue("pk_reqstoorg"));// ��������֯
			if (sStoreId == null) {
				continue;
			}
			String sCorp = PuPubVO
					.getString_TrimZeroLenAsNull((String) bodyVO[i]
							.getAttributeValue("pk_corp"));// �ɹ���˾
			if (sCorp == null) {
				continue;
			}
			UFDouble dOrderNum = PuPubVO
					.getUFDouble_ZeroAsNull((UFDouble) bodyVO[i]
							.getAttributeValue("npraynum"));// ����������Ϊ0
			if (dOrderNum == null) {
				continue;
			}

			if (!(vecBaseId.contains(sBaseId) && vecStorId.contains(sStoreId)
					&& vecCorp.contains(sCorp) && vecOrderNum
					.contains(dOrderNum))) {
				vecBaseId.add(sBaseId);
				vecStorId.add(sStoreId);
				vecCorp.add(sCorp);
				vecOrderNum.add(dOrderNum);
			}
		}
		int iReSetLen = vecBaseId.size();
		if (iReSetLen == 0) {
			return m_hmapPurAdvanceInfo;
		}

		String[] arrStorId = (String[]) vecStorId
				.toArray(new String[iReSetLen]);
		String[] arrBaseId = (String[]) vecBaseId
				.toArray(new String[iReSetLen]);
		String[] arrCorp = (String[]) vecCorp.toArray(new String[iReSetLen]);
		UFDouble[] arrOrderNum = (UFDouble[]) vecOrderNum
				.toArray(new UFDouble[iReSetLen]);

		ArrayList arrayList = new ArrayList();
		arrayList.add(arrCorp);
		arrayList.add(arrStorId);
		arrayList.add(arrBaseId);
		arrayList.add(arrOrderNum);
		try {
			HashMap da2Ret = PraybillHelper.getAdvanceDaysBatch(arrayList);
			if (da2Ret != null) {
				String sKey = null;
				for (int i = 0; i < da2Ret.size(); i++) {
					sKey = vecCorp.get(i) + vecStorId.get(i) + vecBaseId.get(i)
							+ vecOrderNum.get(i);
					// ���뵽��ϣ����
					m_hmapPurAdvanceInfo.put(sKey, da2Ret.get(sKey));
				}
			}

		} catch (Exception e) {
			SCMEnv.out(e);
		}
		return m_hmapPurAdvanceInfo;
	}

	/**
	 * �����빺��������+��������֯ƥ���Դ�嵥����ȡ����ɹ���֯��
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param bcp
	 * @param iBgn
	 * @param iEnd
	 * @return
	 * @throws BusinessException
	 *             <p>
	 * @author lixiaodong
	 * @time 2008-6-30 ����10:00:48
	 */
	private static void queryCpurorganization(BillCardPanel bcp, int iBgn,
			int iEnd) throws BusinessException {

		String cbaseid = "";// �������id
		String reqstoorg = "";// ��������֯

		// String cpurorganization = "";// �ɹ���֯id
		ArrayList<String> listCbaseid = new ArrayList<String>();
		ArrayList<String> listReqstoorg = new ArrayList<String>();

		for (int i = iBgn; i < iEnd; i++) {
			cbaseid = PuPubVO.getString_TrimZeroLenAsNull(bcp.getBillModel()
					.getValueAt(i, "cbaseid"));
			reqstoorg = PuPubVO.getString_TrimZeroLenAsNull(bcp.getBillModel()
					.getValueAt(i, "pk_reqstoorg"));
			if (cbaseid != null && reqstoorg != null) {
				listCbaseid.add(cbaseid);
				listReqstoorg.add(reqstoorg);
			}
		}
		if (listCbaseid == null || listCbaseid.size() < 1) {
			return;
		}

		// �Ѳ�ѯ���Ĳ��ٲ�ѯ
		ArrayList<String> listCbaseidTemp = new ArrayList<String>();
		ArrayList<String> listReqstoorgTemp = new ArrayList<String>();

		for (int i = 0; i < listCbaseid.size(); i++) {
			if (!h_InvSource.containsKey(listCbaseid.get(i).toString()
					+ listReqstoorg.get(i).toString())) {
				listCbaseidTemp.add(listCbaseid.get(i));
				listReqstoorgTemp.add(listReqstoorg.get(i));
			}
		}
		if (listCbaseidTemp == null || listCbaseidTemp.size() < 1) {
			return;
		}

		String[] arrcbaseid = new String[listCbaseidTemp.size()];// �������id
		String[] arrReqstoorg = new String[listReqstoorgTemp.size()];// ��������֯

		listCbaseidTemp.toArray(arrcbaseid);
		listReqstoorgTemp.toArray(arrReqstoorg);
		try {
			IInvSourceService s = (IInvSourceService) NCLocator.getInstance()
					.lookup(IInvSourceService.class.getName());
			HashMap h = s.getCpurOrg(arrcbaseid, arrReqstoorg);

			if (h != null) {
				Iterator keyValuePairs = h.entrySet().iterator();
				for (Iterator iter = keyValuePairs; iter.hasNext();) {
					Map.Entry entry = (Map.Entry) iter.next();
					if (!h_InvSource.containsKey(entry.getKey())) {
						h_InvSource.put(entry.getKey(), entry.getValue());
					}
				}
			}
		} catch (BusinessException e) {
			nc.vo.scm.pub.SCMEnv.out(e);
			MessageDialog.showErrorDlg(bcp, m_lanResTool.getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000270")/* @res"��ʾ" */, e
					.getMessage());
		}
	}

	/**
	 * �����빺��������+��������֯ƥ���Դ�嵥��ȷ������ɹ���֯��
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param bcp
	 * @param iRow
	 * @return �ɹ���֯id
	 * @throws BusinessException
	 *             <p>
	 * @author lixiaodong
	 * @time 2008-6-30 ����01:19:40
	 */
	private static InvSourceVO getCpurorganization(BillCardPanel bcp, int iRow)
			throws BusinessException {

		String cbaseid = "";// �������id
		String reqstoorg = "";// ��������֯
		cbaseid = PuPubVO.getString_TrimZeroLenAsNull(bcp.getBillModel()
				.getValueAt(iRow, "cbaseid"));
		reqstoorg = PuPubVO.getString_TrimZeroLenAsNull(bcp.getBillModel()
				.getValueAt(iRow, "pk_reqstoorg"));
		if (reqstoorg == null) {
			reqstoorg = "";
		}

		queryCpurorganization(bcp, 0, bcp.getBillModel().getRowCount());

		if (cbaseid != null && reqstoorg != null) {
			if (h_InvSource.containsKey(cbaseid + reqstoorg)) {
				return (InvSourceVO) h_InvSource.get(cbaseid + reqstoorg);
			}
		}
		return null;
	}

	/**
	 * �빺�����������н����޶���
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author lixiaodong
	 * @time 2008-7-2 ����10:10:54
	 */
	private void onRevise() {

		onModify(false, true);
		getBillCardPanel().getBodyItem("crowno").setEnabled(false);
		getBillCardPanel().getHeadItem("vpraycode").setEnabled(false);
		getBillCardPanel().getHeadItem("ipraytype").setEnabled(false);
		// getBillCardPanel().getBillModel().getItemByKey("cinventorycode").setEnabled(false);
		// �Ƿ�ֱ��
		if (getBillCardPanel().getHeadItem("isdirecttransit") != null) {
			getBillCardPanel().getHeadItem("isdirecttransit").setEnabled(false);
		}
		// ���ɺ�ͬ����
		if (getBillCardPanel().getBodyItem("ngenct") != null) {
			getBillCardPanel().getBodyItem("ngenct").setEnabled(false);
		}

		showHintMessage(NCLangRes.getInstance().getStrByID("common",
				"4004COMMON000000030")/*
										 * @res "�����޸�"
										 */);
		isRevise = true;
	}

	public boolean onEditAction(int action) {
		if (action == BillScrollPane.ADDLINE && !m_bEditBodyInv) {
			showHintMessage("");
			getBillCardPanel().getBillModel().addLine();
			getBillCardPanel().setEnabled(true);
			/* ������Ŀ�Զ�Э�� */
			nc.ui.pu.pub.PuTool.setBodyProjectByHeadProject(getBillCardPanel(),
					"cprojectidhead", "cprojectid", "cprojectname",
					nc.vo.scm.pu.PuBillLineOprType.ADD);
			/* �����к� */
			BillRowNo.addLineRowNo(getBillCardPanel(), BillTypeConst.PO_PRAY,
					"crowno");
			// ���в������Զ����뵱ǰ��¼��˾
			getBillCardPanel().setBodyValueAt(
					PoPublicUIClass.getLoginPk_corp(),
					getBillCardPanel().getRowCount() - 1, "pk_reqcorp");
			Vector<String> formulas = new Vector<String>();

			String[] formula1 = getBillCardPanel().getBodyItem("reqcorpname")
					.getEditFormulas();
			String[] formula2 = getBillCardPanel().getBodyItem("purcorpname")
					.getEditFormulas();

			if (formula1 != null) {
				for (String formula : formula1)
					formulas.add(formula);
			}
			if (formula2 != null) {
				for (String formula : formula2)
					formulas.add(formula);
			}

			if (formulas != null && formulas.size() > 0) {
				getBillCardPanel().execBodyFormulas(
						getBillCardPanel().getRowCount() - 1,
						formulas.toArray(new String[formulas.size()]));
			}

			getBillCardPanel().setBodyValueAt(
					PoPublicUIClass.getLoginPk_corp(),
					getBillCardPanel().getRowCount() - 1, "pk_purcorp");
			setPartNoEditable(getBillCardPanel());
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000535")/*
											 * @res "��������һ��"
											 */);
			return false;
		}
		return true;
	}

	private static void setReqCorpEditable(BillCardPanel bcp, BillEditEvent e,
			boolean editable) {
		bcp.getBillData().getBillModel().setCellEditable(e.getRow(),
				"reqcorpname", editable);
		if (!editable) {
			sReqCorpTemp = PuPubVO.getString_TrimZeroLenAsNull(bcp
					.getBodyValueAt(e.getRow(), "pk_reqcorp"));
		}
	}

	/**
	 * 
	 * �̹�԰ר��Զ�����7ֻ����ѡ����ĩ��¼��
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author lixiaodong
	 * @modify linsf
	 * @time 2009-3-3 ����09:32:54
	 */
	private boolean checkDef7() {
		// �������Ż���������̹�԰ר���Ҫȥ��
		if (true)
			return true;
		//
		try {
			if (!nc.vo.scm.pub.CustomerConfigVO.getCustomerName()
					.equalsIgnoreCase(
							nc.vo.scm.pub.CustomerConfigVO.NAME_FSBIGUIYUAN)) {
				return true;
			}
		} catch (Exception e) {
			SCMEnv.out(e);
			showHintMessage(m_lanResTool.getStrByID("40040101",
					"UPP40040101-000444")/* "���ִ���:" */
					+ e.getMessage());
		}

		// �޸��ˣ�����Ⱥ �޸����ڣ�2008-6-03 ����04:19:16
		// �޸�ԭ��Ҫ���빺����ͷ�Զ�����7���ƣ�ֻ����ѡ����ĩ��¼�롣�Զ�����7��������Ŀ������
		UIRefPane def7 = (UIRefPane) getBillCardPanel().getHeadItem("vdef7")
				.getComponent();// vdef7

		if (null != def7 && null != def7.getRefModel()) {

			String[] refCodeValues = def7.getRefModel().getRefCodeValues();
			if (null != refCodeValues
					&& 0 < refCodeValues.length
					&& null != refCodeValues[0]
					&& (4 == refCodeValues[0].length() || 7 == refCodeValues[0]
							.length())) {
				def7.getRefModel().setSelectedData(null);
				def7.setValue(null);
				showErrorMessage("��ͷ��Ŀ������ѡ����ĩ����");
				return false;
			}

		}

		UIRefPane pkdef7 = (UIRefPane) getBillCardPanel().getHeadItem(
				"pk_defdoc7").getComponent();// pk_defdoc7

		if (null != pkdef7 && null != pkdef7.getRefModel()) {

			String[] refCodeValues = pkdef7.getRefModel().getRefCodeValues();
			if (null != refCodeValues
					&& 0 < refCodeValues.length
					&& null != refCodeValues[0]
					&& (4 == refCodeValues[0].length() || 7 == refCodeValues[0]
							.length())) {
				pkdef7.getRefModel().setSelectedData(null);
				pkdef7.setValue(null);
				showErrorMessage("��ͷ��Ŀ������ѡ����ĩ����");
				return false;
			}

		}
		return true;
	}

	public static void setBiztype(BillCardPanel bcp, int iBgn, int iEnd)
			throws BusinessException {
		String[] cpurorgids = new String[iEnd - iBgn];
		try {
			BillModel bm = bcp.getBillModel();
			Vector<String> vInvbasid = new Vector();// �������id
			Vector<String> vCpurorgid = new Vector();// �ɹ���֯id
			Vector<String> vCpurcorp = new Vector();// �ɹ���˾id
			HashMap hBiztype = new HashMap();
			initBizInfo();
			// ��֯����
			ArrayList<String> alWhere = new ArrayList<String>();
			for (int i = iBgn; i < iEnd; i++) {
				if (PuPubVO.getString_TrimZeroLenAsNull(bcp.getBodyValueAt(i,
						"cbaseid")) != null
						|| PuPubVO.getString_TrimZeroLenAsNull(bcp
								.getBodyValueAt(i, "cpurorganization")) != null) {
					String sinvbas = PuPubVO.getString_TrimZeroLenAsNull(bcp
							.getBodyValueAt(i, "cbaseid"));
					String sPurorg = PuPubVO.getString_TrimZeroLenAsNull(bcp
							.getBodyValueAt(i, "cpurorganization"));
					if (!m_hBiztype
							.containsKey((PuPubVO
									.getString_TrimZeroLenAsNull(sinvbas) == null ? ""
									: sinvbas)
									+ (PuPubVO
											.getString_TrimZeroLenAsNull(sPurorg) == null ? "null"
											: sPurorg))) {
						vInvbasid.add(sinvbas);
						vCpurorgid.add(sPurorg);
						vCpurcorp.add(PuPubVO.getString_TrimZeroLenAsNull(bcp
								.getBodyValueAt(i, "pk_purcorp")));
					}
				}
			}
			/** ******************** */
			if (vCpurorgid != null && vInvbasid != null && vCpurcorp != null
					&& vCpurorgid.size() > 0 && vInvbasid.size() > 0
					&& vCpurcorp.size() > 0) {
				String[] arrInvbasid = new String[vInvbasid.size()];// �������id
				String[] arrCpurorgid = new String[vCpurorgid.size()];// �ɹ���֯id
				String[] arrCpurcorp = new String[vCpurcorp.size()];// �ɹ���֯id

				vInvbasid.copyInto(arrInvbasid);
				vCpurorgid.copyInto(arrCpurorgid);
				vCpurcorp.copyInto(arrCpurcorp);
				// ���û�Դ�嵥�����ҵ���������ýӿڣ�
				IInvBusiService s = (IInvBusiService) NCLocator.getInstance()
						.lookup(IInvBusiService.class.getName());
				hBiztype = s.getInvBusiType(arrInvbasid, arrCpurorgid,
						arrCpurcorp);
				m_hBiztype.putAll(hBiztype);
			}
			for (int i = iBgn; i < iEnd; i++) {
				String cbaseid = PuPubVO.getString_TrimZeroLenAsNull(bcp
						.getBodyValueAt(i, "cbaseid"));
				String cpurorgid = PuPubVO.getString_TrimZeroLenAsNull(bcp
						.getBodyValueAt(i, "cpurorganization"));
				if ((cbaseid != null || cpurorgid != null)
						&& m_hBiztype
								.containsKey((PuPubVO
										.getString_TrimZeroLenAsNull(cbaseid) == null ? ""
										: cbaseid)
										+ (PuPubVO
												.getString_TrimZeroLenAsNull(cpurorgid) == null ? "null"
												: cpurorgid))) {
					// �����ͷ��ֱ��,��ôҵ�����ͱ�����ֱ��;�����ͷ����ֱ��,ҵ�����Ͳ�����Ϊֱ��.
					boolean isDirect = ((nc.ui.pub.beans.UICheckBox) bcp
							.getHeadItem("isdirecttransit").getComponent())
							.isSelected();
					String cbiztype = m_hBiztype
							.get(
									(PuPubVO
											.getString_TrimZeroLenAsNull(cbaseid) == null ? ""
											: cbaseid)
											+ (PuPubVO
													.getString_TrimZeroLenAsNull(cpurorgid) == null ? "null"
													: cpurorgid)).toString();
					if ((!al_BizInfo.contains(cbiztype) && !isDirect)
							|| (al_BizInfo.contains(cbiztype) && isDirect)) {
						bcp
								.setBodyValueAt(
										m_hBiztype
												.get((PuPubVO
														.getString_TrimZeroLenAsNull(cbaseid) == null ? ""
														: cbaseid)
														+ (PuPubVO
																.getString_TrimZeroLenAsNull(cpurorgid) == null ? "null"
																: cpurorgid)),
										i, "cbiztype");
					}
				} else {
					bcp.setBodyValueAt(null, i, "cbiztype");
				}
			}
			bcp
					.getBillModel()
					.execFormulas(
							new String[] { "cbiztypename->getColValue(bd_busitype,businame,pk_busitype,cbiztype)" });
		} catch (Exception e) {
			PuTool.outException(e);
		}
	}

	private static void beforeEditWhenBodyBiztype(BillCardPanel bcp,
			BillEditEvent e) {
		// ҵ������
		UIRefPane refpane = (UIRefPane) bcp.getBodyItem("cbiztypename")
				.getComponent();

		BillModel bm = bcp.getBillModel();
		String newPurCorp = PuPubVO.getString_TrimZeroLenAsNull(bm.getValueAt(e
				.getRow(), "pk_purcorp"));

		refpane.getRefModel().setWherePart(
				"( pk_corp = '" + newPurCorp + "' or pk_corp = '@@@@')  "
						+ "and (encapsulate = 'N' or encapsulate is null) ");
		refpane
				.getRefModel()
				.addWherePart(
						"and pk_busitype IN (SELECT pk_businesstype FROM pub_billbusiness WHERE pub_billbusiness.dr = 0 "
								+ " and pk_billtype ='"
								+ ScmConst.PO_Order
								+ "')");
		refpane.setReturnCode(false);
		refpane.setCacheEnabled(false);

		if (((UIComboBox) bcp.getHeadItem("ipraytype").getComponent())
				.getSelectedIndex() == 0
				|| ((UIComboBox) bcp.getHeadItem("ipraytype").getComponent())
						.getSelectedIndex() == 3) {
			// �����к���ί������ҵ������
			refpane
					.getRefModel()
					.addWherePart(
							" and pk_busitype IN (SELECT pk_businesstype FROM pub_billbusiness WHERE pub_billbusiness.dr = 0 "
									+ " and pk_billtype ='"
									+ ScmConst.SC_Order
									+ "')"
									+ "  and pk_busitype IN (SELECT pk_businesstype FROM pub_billbusiness WHERE pub_billbusiness.dr = 0 "
									+ " and pk_billtype ='"
									+ ScmConst.PO_Pray
									+ "')");
		} else {
			// �����к���ί������ҵ������
			refpane
					.getRefModel()
					.addWherePart(
							" and pk_busitype IN (SELECT pk_businesstype FROM pub_billbusiness WHERE pub_billbusiness.dr = 0 "
									+ " and pk_billtype ='"
									+ ScmConst.PO_Order
									+ "')"
									+ "  and pk_busitype IN (SELECT pk_businesstype FROM pub_billbusiness WHERE pub_billbusiness.dr = 0 "
									+ " and pk_billtype ='"
									+ ScmConst.PO_Pray
									+ "')");
		}
		if (bcp.getHeadItem("isdirecttransit").getValue().equals("true")) {
			// ҵ������
			refpane.getRefModel().addWherePart(
					" and verifyrule = 'Z' "
							+ refpane.getRefModel().getAddWherePart());
		} else {
			if (refpane.getRefModel().getAddWherePart() != null
					&& refpane.getRefModel().getAddWherePart().indexOf(
							"and verifyrule = 'Z'") != -1) {
				refpane.getRefModel().addWherePart(
						StringUtil
								.replaceAllString(refpane.getRefModel()
										.getAddWherePart(),
										"and verifyrule = 'Z'", " "));
			}
		}
	}

	/**
	 * ��ѯ������������,�����浽ǰ̨
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param sbaseid
	 * @param sReqstoorg
	 *            <p>
	 * @author donggq
	 * @time 2009-3-28 ����03:02:25
	 */
	private static void queryProduceInfo(String[] sbaseid, String[] sReqstoorg) {
		// ���ҳ�滺���в����ڴ�����������������¼,�򵽺�̨��ѯ
		ArrayList<String> alInv = new ArrayList<String>();
		ArrayList<String> alStororg = new ArrayList<String>();
		for (int i = 0; i < sbaseid.length; i++) {
			if (sbaseid[i] != null && sReqstoorg[i] != null
					&& !h_ProduceInfo.containsKey(sbaseid[i] + sReqstoorg[i])) {
				alInv.add(sbaseid[i]);
				alStororg.add(sReqstoorg[i]);
			}
		}
		try {
			if (alInv.size() > 0 && alInv.size() == alStororg.size()) {
				HashMap<String, String[]> hmTemp = PubHelper.queryProductInfo(
						alInv.toArray(new String[alInv.size()]), alStororg
								.toArray(new String[alStororg.size()]));
				h_ProduceInfo.putAll(hmTemp);
			}
		} catch (BusinessException e) {
			PuTool.outException(e);
		}
	}

	private static void initBizInfo() throws Exception {
		if (al_BizInfo == null) {
			al_BizInfo = new ArrayList<String>();
			Object[][] oRule = PubHelper.queryResultsFromAnyTable(
					"bd_busitype", new String[] { "pk_busitype" },
					"verifyrule = 'Z' and pk_corp = '"
							+ ClientEnvironment.getInstance().getCorporation()
									.getPk_corp() + "'");
			if (oRule != null && oRule.length > 0) {
				for (Object[] objects : oRule) {
					if (objects != null
							&& objects.length == 1
							&& PuPubVO.getString_TrimZeroLenAsNull(objects[0]) != null) {
						al_BizInfo.add(objects[0].toString());
					}
				}
			}
		}
	}

	private boolean checkDef(int pos, BillEditEvent e) {
		UIRefPane def = null;
		if (pos == 1) {
			def = (UIRefPane) getBillCardPanel().getBodyItem(e.getKey())
					.getComponent();
		} else if (pos == 0) {
			def = (UIRefPane) getBillCardPanel().getHeadItem(e.getKey())
					.getComponent();
		}
		if (null != def && null != def.getRefModel()) {
			String[] refCodeValues = def.getRefModel().getRefCodeValues();
			if (null == refCodeValues) {
				def.getRefModel().setSelectedData(null);
				def.setValue(null);

				if (pos == 1) {
					getBillCardPanel().getBillModel().setValueAt(null,
							e.getRow(), e.getKey());
				} else if (pos == 0) {
					getBillCardPanel().getHeadItem(e.getKey()).setValue(null);
				}
				return false;
			}
		}

		return true;
	}

	/**
	 * ��ȡ�ýڵ��ģ������
	 */
	private BillTempletVO getBillTempletVO() {
		if (m_billTempletVO == null) {
			m_billTempletVO = BillUIUtil.getDefaultTempletStatic(
					BillTypeConst.PO_PRAY, null, getClientEnvironment()
							.getUser().getPrimaryKey(), m_sLoginCorpId, null,
					null);
		}
		return m_billTempletVO;
	}

	// ---------------------------------------------------------------
	// ���� add by zhw 2011-06-27
	public void onboexport() {// ��ť��Ӧ�¼�
		showHintMessage("");
		try {
			if (getBillListPanel().isShowing() == true) {
				onListOut();
			}else{
				onCardOut();
			}
		} catch (Exception e) {
			e.printStackTrace();
			showErrorMessage(e.getMessage());
		}
	}

	private void onListOut() throws Exception {
		String def1 = null;
		OutputStream os;
		String filename = null;
		String filepath = null;
		String billtype = null;
		String headid = null;
		ArrayList list = (ArrayList) getSelectedBills();
		if (list == null || list.size() == 0)
			throw new BusinessException("��ѡ�����ݣ�");

		int size = list.size();
		int m=0;
		for (int i = 0; i < size; i++) {
			
			PraybillVO billvo = (PraybillVO) list.get(i);
			PraybillHeaderVO head = billvo.getHeadVO();
			if (head == null)
				throw new BusinessException("��ͷ����Ϊ�գ�");
			
			PraybillItemVO[] items = billvo.getBodyVO();
			if (items == null || items.length == 0)
				throw new BusinessException("��������Ϊ�գ�");
			
			int len =items.length;
			billtype = items[0].getCsourcebilltype();
			headid = items[0].getCsourcebillid();
			
			String pk_invbasdoc = items[0].getCbaseid();
			def1 = head.getVdef1();
			
			if("HG01".equalsIgnoreCase(billtype) ||"HG03".equalsIgnoreCase(billtype)){
				if (!"ͳ��".equalsIgnoreCase(def1))
					continue;
			}
            int ibillstatus = head.getIbillstatus();
			
			if(ibillstatus!=3)
				continue;
			File file = null;
			File[] files = null;
			if (getUIFileChooser().showSaveDialog(this) != javax.swing.JFileChooser.APPROVE_OPTION) {
				return ;
			}else{
				files = getUIFileChooser().getSelectedFiles();

				if (files == null || "".equals(files)) 
				return ;
				file = files[0];
			}
			
			TransletFactory tf = new TransletFactory();
			if ("HG01".equalsIgnoreCase(billtype)) {// ��ƻ�
				
				String[] strs =null;
				String s = file.getPath();
				
				strs = tf.splitCode(s);
			
                if(strs.length>0){
            	  
                	filepath = strs[0]+"("+PuPubVO.getString_TrimZeroLenAsNull(head.getDpraydate()).substring(0, 4)+")"+".xls";
                }
				os = new FileOutputStream(filepath);
				WritableWorkbook wwb = Workbook.createWorkbook(os);
				WritableSheet sheet = wwb.createSheet("sheet1", 0);
				
				ArrayList<Object> al = yearHead(head,tf,headid);
				
				ArrayList alt = yearBody(headid,tf) ;
				
				YearExExpot(al, alt,len, sheet);
				m++;
				wwb.write();
				wwb.close();
				os.close();
			} else if ("HG03".equalsIgnoreCase(billtype) ||"".equalsIgnoreCase(billtype) || billtype == null) {// ��ʱ�ƻ�
				ArrayList<Object> al = onBoLSHeadExport(head, tf,headid);
				ArrayList alt = new ArrayList();
				String [] st = {"ddemanddate,vdef19,vdef20"};
				for(int f = 0 ; f < items.length ; f++ ){
					UFDate s  = items[f].getDdemanddate();
					if(s == null || "".equals(s)){
						throw new BusinessException("�������ڲ���Ϊ��");
					}
					int year = s.getYear();
					int month = s.getMonth();
					UFDate date = new UFDate(""+year+"-"+month+"-01");
					items[f].setDdemanddate(date);					
				}
				int month = 0;
				CircularlyAccessibleValueObject[][] vos = nc.vo.scm.pub.vosplit.SplitBillVOs.getSplitVOs(items, st);
				for(int obj = 0 ; obj < vos.length ; obj++){
					alt.clear();
					PraybillItemVO[] vo = (PraybillItemVO[]) vos[obj]; 
					
					len = vo.length;
					for(PraybillItemVO v:vo){
						String stg = PuPubVO.getString_TrimZeroLenAsNull(v.getCmangid());
						if(billtype==null || "".equalsIgnoreCase(billtype)){
							String pk =tf.TFactory(stg,"�ɹ���ʽ");
							if(!"0001Q110000000000R18".equalsIgnoreCase(pk)){
								len--;
								continue;
							}
						}
						alt.add(tf.TFactory(stg,"�������"));
						alt.add(tf.getNplanPrice(stg));// ����
						alt.add(v.getNpraynum());// ����
						alt.add(v.getVmemo());// ��ע
						month = v.getDdemanddate().getMonth();
						al.set(3, PuPubVO.getString_TrimZeroLenAsNull(String.valueOf(month).toString()));
						al.set(7, v.getVdef19().toString());//spf add �ɹ�����
					}
					
					if(len ==0){
						showHintMessage("����ͳ�ɼƻ�����Ҫ����");
						continue;
					}
					String[] strs =null;
					String s = file.getPath();
					
					strs = tf.splitCode(s);
				
	                if(strs.length>0){
	                	//spf edit 
	                	filepath = strs[0]+"("+PuPubVO.getString_TrimZeroLenAsNull(head.getDpraydate()).substring(0, 4)+"("+
	                	PuPubVO.getString_TrimZeroLenAsNull(al.get(7))+"))"+obj+""+".xls";
	                	//spf edit end
	                }
					
					os = new FileOutputStream(filepath);
					WritableWorkbook wwbs = Workbook.createWorkbook(os);
					WritableSheet sheetl = wwbs.createSheet("sheet1", 0);
					LSExExpot(al, alt, len, sheetl);
					alt.clear();
					wwbs.write();
					wwbs.close();
					os.close();
					m++;
				}
			} 
		}
		if(m==0)
			showHintMessage("����ͳ�ɼƻ�����Ҫ����");
		else
			showHintMessage("�����ɹ���");
	}
	
	private void onCardOut() throws Exception {
		String def1 = null;
		String billtype = null;
		String headid = null;
		File file = null;
		File[] files = null;
		//String filepath = "C:\\Documents and Settings\\Administrator\\����\\";
		
		final int nRow = getBillCardPanel().getRowCount();
		PraybillVO VO = new PraybillVO(nRow);
		m_SaveVOs = new PraybillVO(nRow);
		getBillCardPanel().getBillValueVO(VO);
		
			PraybillHeaderVO head = VO.getHeadVO();
			if (head == null)
				throw new BusinessException("��ͷ����Ϊ�գ�");
			
			PraybillItemVO[] items = VO.getBodyVO();
			if (items == null || items.length == 0)
				throw new BusinessException("��������Ϊ�գ�");
			
			int len =items.length;
			billtype = items[0].getCsourcebilltype();
			headid = items[0].getCsourcebillid();
			String pk_invbasdoc = items[0].getCbaseid();
			def1 = head.getVdef1();
			int ibillstatus = head.getIbillstatus();
			
			if(ibillstatus!=3){
				showHintMessage("�ƻ�δ����");
				return;
			}
			
			if("HG01".equalsIgnoreCase(billtype) ||"HG03".equalsIgnoreCase(billtype)){
				if (!"ͳ��".equalsIgnoreCase(def1)){
					showHintMessage("����ͳ�ɼƻ�����Ҫ����");
					return;
				}
			}
			
			
			if (getUIFileChooser().showSaveDialog(this) != javax.swing.JFileChooser.APPROVE_OPTION) {
				return ;
			}else{
				files = getUIFileChooser().getSelectedFiles();

				if (files == null || "".equals(files)) 
				return ;
				file = files[0];
			}
			TransletFactory tf = new TransletFactory();
			if("HG01".equalsIgnoreCase(billtype)) {// ��ƻ�
			
                ArrayList<Object> al = yearHead(head,tf,headid);
				
				ArrayList alt = yearBody(headid,tf) ;
				
				if(alt.size()==0){
					showHintMessage("��ƻ����岻���ڣ�");
					return;
				}
				String[] strs =null;
				String s = file.getPath();
				
				strs = tf.splitCode(s);
			
                if(strs.length>0){
            	  
            	    file = new File(strs[0]+"("+PuPubVO.getString_TrimZeroLenAsNull(head.getDpraydate()).substring(0, 4)+")"+".xls");
                }
				WritableWorkbook book = Workbook.createWorkbook(file);
				WritableSheet sheet = book.createSheet("sheet1", 0);
				
				YearExExpot(al, alt,len, sheet);
				book.write();
				book.close();
				showHintMessage("�����ɹ���");
			}else if ("HG03".equalsIgnoreCase(billtype) ||"".equalsIgnoreCase(billtype) || billtype == null) {// ��ʱ�ƻ�
				ArrayList<Object> al = onBoLSHeadExport(head, tf,headid);
				ArrayList alt = new ArrayList();
				String [] st = {"ddemanddate"};
				for(int f = 0 ; f < items.length ; f++ ){
					UFDate s  = items[f].getDdemanddate();
					if(s == null || "".equals(s)){
						throw new BusinessException("�������ڲ���Ϊ��");
					}
					int year = s.getYear();
					int month = s.getMonth();
					UFDate date = new UFDate(""+year+"-"+month+"-01");
					items[f].setDdemanddate(date);					
				}
				
				CircularlyAccessibleValueObject[][] vos = nc.vo.scm.pub.vosplit.SplitBillVOs.getSplitVOs(items, st);
				File st11 = file.getParentFile();
				int i=0;
				String[] strs =null;
				int lens = vos.length;
				int m=0;
				for(int obj = 0 ; obj < lens ; obj++){
					PraybillItemVO[] vo = (PraybillItemVO[]) vos[obj]; 
					len = vos[obj].length;
					for(PraybillItemVO v:vo){
						String stg = PuPubVO.getString_TrimZeroLenAsNull(v.getCmangid());
						if(billtype==null || "".equalsIgnoreCase(billtype)){
							String pk =tf.TFactory(stg,"�ɹ���ʽ");
							if(!"0001Q110000000000R18".equalsIgnoreCase(pk)){
								len--;
								continue;
							}
						}
						alt.add(tf.TFactory(stg,"�������"));
						alt.add(tf.getNplanPrice(stg));// ����
						alt.add(v.getNpraynum());// ����
						alt.add(v.getVmemo());// ��ע
						al.set(7, v.getVdef19().toString());//spf add �ɹ�����
					}
					if(len ==0){
						if(m==0)
						     showHintMessage("û��ͳ�����ݣ�");
						else
							 showHintMessage("�����ɹ���");
						continue;
					}
					
					if(lens>0){
						
						if(i==0){
							String s = file.getPath();
							strs = tf.splitCode(s);
						}
						
	                    if(strs.length>0){
	                    	i++;
	                    	file = new File(strs[0]+"("+PuPubVO.getString_TrimZeroLenAsNull(vo[0].getDdemanddate().getMonth())+"("+
	        	                	PuPubVO.getString_TrimZeroLenAsNull(al.get(7))+"))"+obj+""+".xls");
	                    }
					}
					
					
					if(file == null || "".equals(file)){
						throw new BusinessException("û��ͳ�����ݣ�");
					}
					WritableWorkbook books = Workbook.createWorkbook(file);
					WritableSheet sheet = books.createSheet(String.valueOf(vos[obj][0].getAttributeValue("ddemanddate")), 0);
					LSExExpot(al, alt, len, sheet);
					alt.clear();
					books.write();
					books.close();
					m++;
					
				}
				if(m==0)
				     showHintMessage("û��ͳ�����ݣ�");
				else
					 showHintMessage("�����ɹ���");
			} 
	}
	
	public ArrayList yearHead(PraybillHeaderVO head,TransletFactory tf,String headid) throws Exception{
		ArrayList<Object> al = new ArrayList<Object>();
		
		al.add(PuPubVO.getString_TrimZeroLenAsNull(head.getDpraydate()).substring(0, 4));// �ɹ����
		al.add(PuPubVO.getString_TrimZeroLenAsNull(tf.TFactory(ClientEnvironment.getInstance().getUser().getPrimaryKey(), "���벿��")));// ���벿��
		al.add(tf.TFactory(headid,"�ɹ�����"));// �ɹ�����
		al.add(tf.TFactory(PuPubVO.getString_TrimZeroLenAsNull(head.getCoperator()),"����Ա"));// �Ƶ���
		
		al.add(PuPubVO.getString_TrimZeroLenAsNull(head.getTlastmaketime()).substring(0, 10));// �Ƶ�����
		al.add(tf.TFactory(PuPubVO.getString_TrimZeroLenAsNull(head.getCauditpsn()),"����Ա"));// ������
		al.add(PuPubVO.getString_TrimZeroLenAsNull(head.getDauditdate()));// ��������
		al.add(null);// ��������
		al.add(head.getVmemo());// ��ע
		return al;
	}
	
	public ArrayList yearBody(String id,TransletFactory tf) throws Exception {
		PlanYearBVO[] ybvos = (PlanYearBVO[]) HYPubBO_Client.queryByCondition(
				PlanYearBVO.class, " pk_plan ='" + id + "' and isnull(dr,0)=0 ");

		if (ybvos == null || ybvos.length == 0)
			throw new BusinessException("��ƻ���������Ϊ��");

		int rowcount = ybvos.length;
		ArrayList alt = new ArrayList();
		for (PlanYearBVO yvbo : ybvos) {
			
			alt.add(tf.TFactory(PuPubVO.getString_TrimZeroLenAsNull(yvbo.getAttributeValue("cinventoryid")),"�������"));// ���id
			alt.add(yvbo.getAttributeValue("nprice"));// �۸�
			
			UFDouble num = UFDouble.ZERO_DBL;
			for (int x = 1; x <= 12; x++) {
				// alt.add("nmonnum"+ x);
				alt.add(PuPubVO.getUFDouble_NullAsZero(yvbo.getAttributeValue("nmonnum" + x)));
				num = num.add(PuPubVO.getUFDouble_NullAsZero(yvbo.getAttributeValue("nmonnum" + x)));
			}
			
            if("����".equalsIgnoreCase(tf.TFactory(yvbo.getPk_measdoc(),"��������"))){
            	num= num.div(1000);
			}
			
			alt.add(num);
			UFDouble mny = PuPubVO.getUFDouble_NullAsZero(yvbo.getAttributeValue("nprice"));
			alt.add(mny.multiply(num));
			alt.add(yvbo.getAttributeValue("vmemo"));
		}
		return alt;
	}

	public void YearExExpot(ArrayList<Object> al, ArrayList<Object> alt,
			int num, WritableSheet sheet) throws BusinessException {
		try {
			Label label = null;
			label = new Label(0, 0, "�ɹ����");
			sheet.addCell(label);

			label = new Label(1, 0, PuPubVO.getString_TrimZeroLenAsNull(al
					.get(0)));
			sheet.addCell(label);

			label = new Label(2, 0, "���벿��");
			sheet.addCell(label);
			label = new Label(3, 0, PuPubVO.getString_TrimZeroLenAsNull(al
					.get(1)));
			sheet.addCell(label);
			label = new Label(4, 0, "�ɹ�����");
			sheet.addCell(label);
			label = new Label(5, 0, PuPubVO.getString_TrimZeroLenAsNull(al
					.get(2)));
			sheet.addCell(label);

			label = new Label(0, 1, "�Ƶ���");
			sheet.addCell(label);
			label = new Label(1, 1, PuPubVO.getString_TrimZeroLenAsNull(al
					.get(3)));
			sheet.addCell(label);
			label = new Label(2, 1, "�Ƶ�����");
			sheet.addCell(label);
			label = new Label(3, 1, PuPubVO.getString_TrimZeroLenAsNull(al
					.get(4)));
			sheet.addCell(label);
			label = new Label(4, 1, "������");
			sheet.addCell(label);
			label = new Label(5, 1, PuPubVO.getString_TrimZeroLenAsNull(al
					.get(5)));
			sheet.addCell(label);

			label = new Label(0, 2, "��������");
			sheet.addCell(label);
			if (al.get(6) == "null") {
				al.set(6, "");
			}
			label = new Label(1, 2, PuPubVO.getString_TrimZeroLenAsNull(al
					.get(6)));
			sheet.addCell(label);
			label = new Label(2, 2, "��������");
			sheet.addCell(label);
			label = new Label(3, 2, PuPubVO.getString_TrimZeroLenAsNull(al
					.get(7)));
			sheet.addCell(label);
			label = new Label(4, 2, "��ע");
			sheet.addCell(label);
			label = new Label(5, 2, PuPubVO.getString_TrimZeroLenAsNull(al
					.get(8)));
			sheet.addCell(label);

			label = new Label(0, 3, "�������");
			sheet.addCell(label);
			label = new Label(1, 3, "����");
			sheet.addCell(label);
			label = new Label(2, 3, "һ��");
			sheet.addCell(label);
			label = new Label(3, 3, "����");
			sheet.addCell(label);
			label = new Label(4, 3, "����");
			sheet.addCell(label);
			label = new Label(5, 3, "����");
			sheet.addCell(label);
			label = new Label(6, 3, "����");
			sheet.addCell(label);
			label = new Label(7, 3, "����");
			sheet.addCell(label);
			label = new Label(8, 3, "����");
			sheet.addCell(label);
			label = new Label(9, 3, "����");
			sheet.addCell(label);
			label = new Label(10, 3, "����");
			sheet.addCell(label);
			label = new Label(11, 3, "ʮ��");
			sheet.addCell(label);
			label = new Label(12, 3, "ʮһ��");
			sheet.addCell(label);
			label = new Label(13, 3, "ʮ����");
			sheet.addCell(label);
			label = new Label(14, 3, "������");
			sheet.addCell(label);
			label = new Label(15, 3, "�ܽ��");
			sheet.addCell(label);
			label = new Label(16, 3, "��ע");
			sheet.addCell(label);
			int sum = 0;
			for (int i = 4; i < num + 4; i++) {
				if (PuPubVO.getString_TrimZeroLenAsNull(alt.get(sum)) == null) {
					alt.set(sum, "");
				}
				label = new Label(0, i, PuPubVO.getString_TrimZeroLenAsNull(alt
						.get(sum++)));
				sheet.addCell(label);
				String st = PuPubVO.getString_TrimZeroLenAsNull(alt.get(sum++));
				if (st == null) {
					st = "";
				}
				label = new Label(1, i, st);
				sheet.addCell(label);
				for (int x = 2; x < 14; x++) {
					if (PuPubVO.getString_TrimZeroLenAsNull(alt.get(sum)) == null) {
						alt.set(sum, "");
						sheet.addCell(new Label(x, i, PuPubVO
								.getString_TrimZeroLenAsNull(alt.get(sum++))));
						continue;
					}
					st = PuPubVO.getString_TrimZeroLenAsNull(alt.get(sum++));
					label = new Label(x, i, st);
					sheet.addCell(label);
				}

				st = PuPubVO.getString_TrimZeroLenAsNull(alt.get(sum++));
				label = new Label(14, i, st);
				sheet.addCell(label);

				st = PuPubVO.getString_TrimZeroLenAsNull(alt.get(sum++));
				label = new Label(15, i, st);
				sheet.addCell(label);
				label = new Label(16, i, (String) alt.get(sum++));
				sheet.addCell(label);
			}
		} catch (WriteException e) {
			e.printStackTrace();
			throw new BusinessException("��EXCELд���ݴ���!");
		}
	}

	UIFileChooser fcFileChooser = null;

	private UIFileChooser getUIFileChooser() {
		if (fcFileChooser == null) {
			fcFileChooser = new UIFileChooser();
			fcFileChooser.setMultiSelectionEnabled(true);
		}
		return fcFileChooser;

	}


	 ArrayList<Object> al = new ArrayList<Object>();
	 private ArrayList onBoLSHeadExport(PraybillHeaderVO head,TransletFactory tf,String headid ) throws Exception {
		 al.add("�ɹ����");
		 al.add(PuPubVO.getString_TrimZeroLenAsNull(head.getDpraydate()).substring(0, 4));
		 al.add("�ɹ��·�");
		 al.add(PuPubVO.getString_TrimZeroLenAsNull(head.getDpraydate()).substring(5, 7));
		 al.add("���벿��");
		 al.add(PuPubVO.getString_TrimZeroLenAsNull(tf.TFactory(ClientEnvironment.getInstance().getUser().getPrimaryKey(), "���벿��")));// ���벿��
		 al.add("�ɹ�����");
		 al.add(tf.TFactory(headid,"�ɹ�����"));// �ɹ�����
		 al.add("�Ƶ���");
		 al.add(tf.TFactory(PuPubVO.getString_TrimZeroLenAsNull(head.getCoperator()),"����Ա"));// �Ƶ���
		 al.add("�Ƶ�����");
		 al.add(PuPubVO.getString_TrimZeroLenAsNull(head.getTlastmaketime()).substring(0, 10));// �Ƶ�����
		 al.add("������");
		 al.add(tf.TFactory(PuPubVO.getString_TrimZeroLenAsNull(head.getCauditpsn()),"����Ա"));// ������
		 al.add("��������");
		 al.add(PuPubVO.getString_TrimZeroLenAsNull(head.getDauditdate()));// ��������
		 al.add("��������");
		 al.add(null);// ��������
		 al.add("���");
		 al.add("����");
		 al.add("����");
		 al.add("��ע");
		 al.add("��ע");
		 al.add(head.getVmemo());// ��ע
		 return al;
	 
	 }

	private void LSExExpot(ArrayList<Object> al, ArrayList<Object> alt,
			int num, WritableSheet sheet) throws BusinessException {
		try {
			Label label = null;
			int sum = 0;
			for (int x = 0; x < 3; x++) {
				for (int y = 0; y < 6; y++) {
					if ((String) al.get(sum) == "null") {
						al.set(sum, "");
					}
					label = new Label(y, x, (String) al.get(sum++));
					sheet.addCell(label);
				}
			}
			label = new Label(0, 3, (String) al.get(sum++));
			sheet.addCell(label);
			label = new Label(1, 3, (String) al.get(sum++));
			sheet.addCell(label);
			label = new Label(2, 3, (String) al.get(sum++));
			sheet.addCell(label);
			label = new Label(3, 3, (String) al.get(sum++));
			sheet.addCell(label);

			label = new Label(6, 2, (String) al.get(sum++));
			sheet.addCell(label);
			label = new Label(7, 2, (String) al.get(sum++));
			sheet.addCell(label);

			sum = 0;
			for (int i = 4; i < num + 4; i++) {
				label = new Label(0, i, String.valueOf(alt.get(sum++)));
				sheet.addCell(label);

				if (String.valueOf(alt.get(sum)) == "null") {
					alt.set(sum, "");
				}
				String st = new String().valueOf(alt.get(sum++));
				label = new Label(1, i, st);
				sheet.addCell(label);

				if (String.valueOf(alt.get(sum)) == "null") {
					alt.set(sum, "");
				}
				st = new String().valueOf(alt.get(sum++));
				label = new Label(2, i, st);
				sheet.addCell(label);
				label = new Label(3, i, (String) alt.get(sum++));
				sheet.addCell(label);
			}
		} catch (WriteException e) {
			e.printStackTrace();
			throw new BusinessException("��EXCELд���ݴ���!");
		}

	}
}
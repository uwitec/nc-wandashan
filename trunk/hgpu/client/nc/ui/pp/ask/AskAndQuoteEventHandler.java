package nc.ui.pp.ask;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JFileChooser;

import nc.bs.uap.sf.facility.SFServiceFacility;
import nc.ui.bd.ref.UFRefManage;
import nc.ui.bd.ref.busi.PurorgDefaultRefModel;
import nc.ui.hg.pu.pub.LongTimeTask;
import nc.ui.ml.NCLangRes;
import nc.ui.po.pub.InvAttrCellRenderer;
import nc.ui.pp.pub.CustRefModelForAskQuery;
import nc.ui.pp.pub.CustRefModelForAskSend;
import nc.ui.pp.pub.InvCodeRefModelForAskQue;
import nc.ui.pp.pub.PpTool;
import nc.ui.pp.pub.UpLoadCtrl;
import nc.ui.pu.pub.PuTool;
import nc.ui.pu.pub.PurDeptRefModel;
import nc.ui.pu.pub.PurPsnRefModel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.mail.AccountManageDialog;
import nc.ui.pub.mail.QuickMail;
import nc.ui.pub.mail.SendMailDialog;
import nc.ui.pub.print.PrintEntry;
import nc.ui.scm.pub.cache.CacheTool;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.print.ISetBillVO;
import nc.ui.scm.pub.print.ScmPrintTool;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.pub.sourceref.SourceRefDlg;
import nc.ui.trade.adapter.IBillBufferAdapter;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.bsdelegate.IBusinessSplit;
import nc.ui.trade.businessaction.IBusinessController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.bd.access.BddataVO;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pfxx.pub.Filter;
import nc.vo.pp.ask.AskPubVO;
import nc.vo.pp.ask.AskbillHeaderVO;
import nc.vo.pp.ask.AskbillItemBVO;
import nc.vo.pp.ask.AskbillItemMergeVO;
import nc.vo.pp.ask.AskbillItemVO;
import nc.vo.pp.ask.AskbillItemVendorVO;
import nc.vo.pp.ask.AskbillMergeVO;
import nc.vo.pp.ask.ExcelFileVO;
import nc.vo.pp.ask.IAskAndQuote;
import nc.vo.pp.ask.IAskBillStatus;
import nc.vo.pp.ask.IDataPowerForPrice;
import nc.vo.pp.ask.IIsGenOrder;
import nc.vo.pp.ask.StatusConvert;
import nc.vo.pp.ask.UpLoadFileVO;
import nc.vo.pp.ask.VendorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.mail.AttachmentVO;
import nc.vo.pub.mail.MailInfo;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.trade.summarize.Hashlize;
import nc.vo.trade.summarize.VOHashPrimaryKeyAdapter;

/**
 * @author zhouxiao
 * 
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת�� ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public class AskAndQuoteEventHandler extends
		nc.ui.trade.splitmanage.SplitManageEventHandler {
	private static String bo = "nc.bs.hg.so.pub.HgSoPubBO";
	// �빺������ѯ�۵��½���
//	private PrayToAskDLG billReferUI = null;
//
//	// ѡ��Ӧ���½���
//	private VendorSelectUI vendorSelectUI = null;

	
	// ��������ѡ��Ĺ�Ӧ��
	private VendorVO[] m_vendorVOs = null;

	// ��������ѡ��Ĺ�Ӧ��
	private VendorVO[] m_vendorVOsForSend = null;

	// �����ӱ�������
	private AskbillItemVO[] m_itemVOs = null;

	// ѯ���۵���ѯ��������Ի���
	private SCMQueryConditionDlg m_dlgIqQueryCondition = null;
//	private AskQueryDlg m_askQueryDlg = null; 

	// ״̬��ѯ����
	UICheckBox m_chkFree = null;

	UICheckBox m_chkSend = null;

	UICheckBox m_chkQuote = null;

	UICheckBox m_chkEnd = null;

	// ʹ��������������ԭ����ctrl��delegator����������� dengjt
	protected IBillBufferAdapter bufferAdapter;

	// �Ƿ�ѡ�в�ѯ��ˢ�°�ť
	private boolean isQueryORRefresh = false;

	// �Ƿ��б�ɾ��
	//private boolean isListDel = false;

	// Email
	private QuickMail dlgMail = null;

	/* ��ǰ��¼����Ա��Ȩ�޵Ĺ�˾[] */
	private String[] saPkCorp = null;

	// ������ѯ�Ի���
	nc.ui.pu.pub.ATPForOneInvMulCorpUI m_atpDlg = null;

	/* ��ӡ�ؼ������ */
	private PrintEntry m_printEntry = null;

	private ScmPrintTool printList = null;

	private ScmPrintTool printListForVendor = null;

//	private boolean isAlreadyPrice = false;

	protected javax.swing.JFileChooser m_chooser = null;

	private int state = 0;

	/** �ļ�ѡȡ�� */
	private UIFileChooser m_filechooser = null;

	private UITextField ivjtfDirectory = null;

	/** ��ǰѡ���·�� */
	private String m_currentPath = null;

	/** ��ǰĿ¼��XLS�ļ����� */
	private File[] m_allcurrfiles = null;

	// ��ǰ�ļ�
	private File m_fFilePath = null;

	private Hashtable m_ht = null;

	public UpLoadCtrl m_UploadCtrl;

	private Vector excelTOBill = null;

	//��ť
	public ButtonObject commit = null;

	public ButtonObject edit = null;

	public ButtonObject save = null;

	public ButtonObject cancel = null;

	public ButtonObject del = null;

	public ButtonObject delList = null;

	public ButtonObject copy = null;

	public ButtonObject billQuery = null;

	public ButtonObject docManager = null;

	public ButtonObject print = null;

	public ButtonObject printpre = null;

	public ButtonObject docManagerlist = null;

	public ButtonObject printlist = null;

	public ButtonObject printprelist = null;

	public ButtonObject editList = null;

	public ButtonObject mainList = null;
	
	public ButtonObject mainCard = null;

	public ButtonObject selAllList = null;

	public ButtonObject selNoneList = null;

	public ButtonObject toCard = null;

	public ButtonObject line = null;

	public ButtonObject queryList = null;

	public ButtonObject reFreshList = null;

	public ButtonObject assistList = null;

	public ButtonObject listCancelTranform = null;

	public ButtonObject first = null;

	public ButtonObject next = null;

	public ButtonObject prev = null;

	public ButtonObject Last = null;

	public ButtonObject billToExcelDefault = null;

	public ButtonObject billToExcelDef = null;

	public ButtonObject excelToBill = null;

	public ButtonObject billToExcelDefaultList = null;

	public ButtonObject billToExcelDeflist = null;

	public ButtonObject excelToBillList = null;
	
	public ButtonObject  vendorSelect = null;
	
	public ButtonObject stockQuyList = null;
	
	public ButtonObject stockQuy = null;
	
	//ѡ��Ӧ�̲���
	private UIRefPane refpaneVendor = null;
	
    //���﷭�빤����
	private static NCLangRes m_lanResTool = NCLangRes.getInstance();
	


	/**
	 * @param billUI
	 * @param control
	 */
	public AskAndQuoteEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		// TODO �Զ����ɹ��캯�����
	}

	/**
	 * ���е��ݶ����Ĵ���, ������е��ݶ���������Ҫ���ظ÷��� �������ڣ�(2004-1-3 18:13:36)
	 */
	protected IBusinessController createBusinessAction() {
		return new AskPuPubBusiAction(getBillUI());
	}

	/**
	 * ʵ����������в�ֵ���VO�Ĵ���, ������в�ֵ��ݴ�����Ҫ���ظ÷��� �������ڣ�(2004-1-3 18:13:36)
	 */
	protected IBusinessSplit createBusinessSplit() {
		return new PrayToAskBusiSplit();
	}

	public void onActionAppendLine(int iAppendCount) {

		// showHintMessage( CommonConstant.SPACE_MARK + "���Ӷ�����" +
		// CommonConstant.SPACE_MARK ) ;

		if (iAppendCount <= 0) {
			return;
		}
//		int iRow = getBillCardPanelWrapper().getBillCardPanel().getBillModel()
//				.getRowCount() - 1;
		// ����
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().addLine(
				iAppendCount);

		// �账�����
//		int[] iaRow = nc.ui.pu.pub.PuTool.getRowsAfterMultiSelect(iRow,
//				iAppendCount + 1);

		// ��ʼ��������
//		int iLen = iaRow.length;
//		int iBeginRow = iaRow[1];
//		int iEndRow = iaRow[iLen - 1];

	}

	public void onActionInsertLines(int iCurRow, int iInsertCount) {

		if (iInsertCount == 0) {
			return;
		}

		// ����
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().insertLine(
				iCurRow, iInsertCount);

//		int iLen = iInsertCount + 1;
		// ���ӵ���
//		int[] iaRow = nc.ui.pu.pub.PuTool
//				.getRowsAfterMultiSelect(iCurRow, iLen);

		// ��ʼ��������
//		int iBeginRow = iaRow[0];
//		int iEndRow = iaRow[iLen - 2];

	}

	/**
	 * ��ťm_boCancel���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoCancel() throws Exception {

		
		
		ArrayList refVOs = ((BillUIRefCacheForPP) ((AskAndQuoteUI) getBillUI())
				.getCacheData()).getCurRefCache();
	
		if (refVOs.size() <= 1) {
			refVOs = null;
		} else {
			refVOs.remove(((BillUIRefCacheForPP) ((AskAndQuoteUI) getBillUI())
					.getCacheData()).getSelectedRow());
		}
		
		AggregatedValueObject[] vos = null;
		if (refVOs != null && refVOs.size() > 0) {
			vos = new AggregatedValueObject[refVOs.size()];
			refVOs.toArray(vos);
		}
		setRefDataForSave(vos);
    //������ʾ 
		AskbillMergeVO voCurr = (AskbillMergeVO) getBillCardPanelWrapper().getBillCardPanel().getBillValueVO(AskbillMergeVO.class.getName(), AskbillHeaderVO.class.getName(), AskbillItemMergeVO.class.getName());
		AggregatedValueObject billVO = null;
		try {
			if(voCurr.getParentVO().getPrimaryKey() == null 
					|| voCurr.getParentVO().getPrimaryKey().trim().length() == 0){
				getBufferData().removeCurrentRow();
				return;
			}
		} catch (Exception e2) {
      SCMEnv.out(e2);
		}
		if(getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vfree").isEnabled()){//2009/10/20 ����� - Ϊ��������ʾ����ж�����
			InvAttrCellRenderer ficr = new InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanelWrapper().getBillCardPanel(), voCurr);
		}
		((BillManageUI)getBillUI()).showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH008")/*@res "ȡ���ɹ�"*/);

	}
	/**
	 * ��ťm_boCancelAudit���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoCancelAudit() throws Exception {

		AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
		try {
			for (int i = 0; i < modelVo.getChildrenVO().length; i++) {
				if ((!(modelVo.getChildrenVO()[i]
						.getAttributeValue("closeflag") == null))
						&& ("Y".equalsIgnoreCase(modelVo.getChildrenVO()[i].getAttributeValue(
								"closeflag").toString())))
					throw new BusinessException("�����йرյı������޷�����");
			}
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			if (e instanceof BusinessException)
				throw e;
		}
		super.onBoCancelAudit();

	}

	/**
	 * ��ťm_boEdit���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoEdit() throws Exception {
		AggregatedValueObject billVO = null;
		String status = null;
		if (((BillUIRefCacheForPP) ((AskAndQuoteUI) getBillUI()).getCacheData())
				.isEmpty()) {
			billVO = getBufferData().getCurrentVO();
			if (billVO != null && billVO.getParentVO() != null) {

				status = ((AskbillHeaderVO) billVO.getParentVO())
						.getIbillstatus();
				int ibillstatus = new Integer(StatusConvert
						.getStatusIndexName().get(status).toString())
						.intValue();
				if (ibillstatus == IAskBillStatus.CONFIRM) {
					MessageDialog.showErrorDlg((AskAndQuoteUI) getBillUI(),
							"�޸�", "���״̬��ѯ���۵������޸�");
					return;
				}
			}
		}else{
			billVO = ((BillUIRefCacheForPP) ((AskAndQuoteUI) getBillUI()).getCacheData()).getCurrentCacheVO();
		}
		// �޸�ѡ�е�ѯ���۵����������ɡ�����������״̬��ѯ���۵��������޸ģ������ڷ���������״̬��ѯ���۵��ı�ͷ��Ŀ�ͱ����кš�������롢�����˰�ʡ���װ��ʽ���������������ڡ�������ַ�����޸ģ����ڴ������״̬��ѯ���۵��������޸�
		String ibillStatus = ((AskbillHeaderVO)billVO.getParentVO()).getIbillstatus();
//			getBillCardPanelWrapper().getBillCardPanel()
//				.getTailItem("ibillstatus").getValue();
		if (ibillStatus == null
				|| (ibillStatus != null && ibillStatus.trim()
						.length() == 0)) {
			ibillStatus = status;
		}
//		String cvendmangid = null;
//		if (getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
//				"cvendormangid") != null
//				&& getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
//						"cvendormangid").getValue() != null
//				&& getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
//						"cvendormangid").getValue().trim().length() > 0) {
//
//			cvendmangid = getBillCardPanelWrapper().getBillCardPanel()
//					.getBodyItem("cvendormangid").getValue();
//		}

		if (((BillManageUI) getBillUI()).isListPanelSelected()) {
		  ((BillManageUI)getBillUI()).setCardUIData(billVO);
		}
		((AskAndQuoteUI) getBillUI()).getManagePane().remove(((AskAndQuoteUI) getBillUI()).getBillListPanel());
		((AskAndQuoteUI) getBillUI()).getManagePane().add(((AskAndQuoteUI) getBillUI()).getBillCardWrapper().getBillCardPanel(),BorderLayout.CENTER);
		((BillManageUI) getBillUI()).setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		super.onBoEdit();
        //��ʾ������Դ��Ϣ
		nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanelWrapper().getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_ASK);
		
		if (ibillStatus != null && ibillStatus.trim() != null) {
			if (ibillStatus.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000340")/* @res "����" */)) {
				// ѯ���۵��ı�ͷ��Ŀ���ɱ༭
				BillItem[] headItems = getBillCardPanelWrapper()
						.getBillCardPanel().getHeadItems();
				if (headItems != null && headItems.length > 0) {
					BillItem headItem = null;
					for (int i = 0; i < headItems.length; i++) {
						headItem = headItems[i];
						headItem.setEnabled(true);
					}
				}
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"crowno").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"cinventorycode").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"cvendorname").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"vfree").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"dreceivedate").setEnabled(true);
				if (getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"vreveiveaddress") != null) {
					getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
							"vreveiveaddress").setEnabled(true);
				}
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"vmemo").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"ntaxrate").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"nasknum").setEnabled(true);
				// ��˰���ۡ���˰���ۡ������ڡ������ˡ�������Ч���ڡ�����ʧЧ���ڲ��ɱ༭
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"cvendorname").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"nquoteprice").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"nquotetaxprice").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"deliverdays").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"dvaliddate").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"dinvaliddate").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getTailItem(
						"cquotepsn").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getTailItem(
						"dquotedate").setEnabled(false);
			} else if (ibillStatus
					.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"4004070101", "UPT4004070101-000022")/* @res "����" */)
					|| ibillStatus
							.equals(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4004070101",
											"UPP4004070101-000050")/* @res "����" */)) {// ѯ���۵�״̬Ϊ�����򱨼�
				// ѯ���۵��ı�ͷ��Ŀ���ɱ༭
				BillItem[] headItems = getBillCardPanelWrapper()
						.getBillCardPanel().getHeadItems();
				if (headItems != null && headItems.length > 0) {
					BillItem headItem = null;
					for (int i = 0; i < headItems.length; i++) {
						headItem = headItems[i];
						headItem.setEnabled(false);
					}
				}
				// ѯ���۵��ı����кš�������롢�����˰�ʡ���װ��ʽ���������������ڡ�������ַ���ɱ༭
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"crowno").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"cinventorycode").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"cvendorname").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"vfree").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"dreceivedate").setEnabled(false);
				if (getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"vreveiveaddress") != null) {
					getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
							"vreveiveaddress").setEnabled(false);
				}
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"vmemo").setEnabled(false);
				//since V5.3 ˰�ʿ����޸� modify by donggq
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"ntaxrate").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"nasknum").setEnabled(false);

				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"cvendorname").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"nquoteprice").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"nquotetaxprice").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"deliverdays").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"dvaliddate").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"dinvaliddate").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getTailItem(
						"cquotepsn").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getTailItem(
						"dquotedate").setEnabled(false);

			}

		}
		String pk = ((AskbillHeaderVO) billVO.getParentVO()).getCaskbillid();
		
		if (pk != null && pk.trim().length() > 0) {
			if(ibillStatus != null && ibillStatus.trim() != null
					&& ibillStatus.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000340")/* @res "����" */)){
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"vaskbillcode").setEnabled(true);
			}else{
			 getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"vaskbillcode").setEnabled(false);
			}
		} else {
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"vaskbillcode").setEnabled(true);
		}
		if (getBillUI().getBillOperate() == IBillOperate.OP_REFADD)// ��������
		{
			setOperatorID();
		}
        //������������ʾ����
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanelWrapper().getBillCardPanel(), getBufferData().getCurrentVO());
		setButtonsStateEdit();
	}
	/**
	 * ��ťm_boEdit���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoEditForVendorSelected(String ibillStatus) throws Exception {
//		AggregatedValueObject billVO = null;
//		String status = null;
//		if (((BillUIRefCacheForPP) ((AskAndQuoteUI) getBillUI()).getCacheData())
//				.isEmpty()) {
//			billVO = getBufferData().getCurrentVO();
//			if (billVO != null && billVO.getParentVO() != null) {
//
//				status = ((AskbillHeaderVO) billVO.getParentVO())
//						.getIbillstatus();
//				int ibillstatus = new Integer(StatusConvert
//						.getStatusIndexName().get(status).toString())
//						.intValue();
//				if (ibillstatus == IAskBillStatus.CONFIRM) {
//					MessageDialog.showErrorDlg((AskAndQuoteUI) getBillUI(),
//							"�޸�", "���״̬��ѯ���۵������޸�");
//					return;
//				}
//			}
//		}else{
//			billVO = ((BillUIRefCacheForPP) ((AskAndQuoteUI) getBillUI()).getCacheData()).getCurrentCacheVO();
//		}
		// �޸�ѡ�е�ѯ���۵����������ɡ�����������״̬��ѯ���۵��������޸ģ������ڷ���������״̬��ѯ���۵��ı�ͷ��Ŀ�ͱ����кš�������롢�����˰�ʡ���װ��ʽ���������������ڡ�������ַ�����޸ģ����ڴ������״̬��ѯ���۵��������޸�
//		String ibillStatus = ((AskbillHeaderVO)billVO.getParentVO()).getIbillstatus();
//			getBillCardPanelWrapper().getBillCardPanel()
//				.getTailItem("ibillstatus").getValue();
		if (ibillStatus == null
				|| (ibillStatus != null && ibillStatus.trim()
						.length() == 0)) {
			ibillStatus = nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000340")/* @res "����" */;
		}
//		String cvendmangid = null;
//		if (getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
//				"cvendormangid") != null
//				&& getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
//						"cvendormangid").getValue() != null
//				&& getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
//						"cvendormangid").getValue().trim().length() > 0) {
//
//			cvendmangid = getBillCardPanelWrapper().getBillCardPanel()
//					.getBodyItem("cvendormangid").getValue();
//		}

		
		super.onBoEdit();
        //��ʾ������Դ��Ϣ
		nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanelWrapper().getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_ASK);
		
		if (ibillStatus != null && ibillStatus.trim() != null) {
			if (ibillStatus.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000340")/* @res "����" */)) {
				// ѯ���۵��ı�ͷ��Ŀ���ɱ༭
				BillItem[] headItems = getBillCardPanelWrapper()
						.getBillCardPanel().getHeadItems();
				if (headItems != null && headItems.length > 0) {
					BillItem headItem = null;
					for (int i = 0; i < headItems.length; i++) {
						headItem = headItems[i];
						headItem.setEnabled(true);
					}
				}
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"crowno").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"cinventorycode").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"cvendorname").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"vfree").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"dreceivedate").setEnabled(true);
				if (getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"vreveiveaddress") != null) {
					getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
							"vreveiveaddress").setEnabled(true);
				}
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"vmemo").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"ntaxrate").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"nasknum").setEnabled(true);
				// ��˰���ۡ���˰���ۡ������ڡ������ˡ�������Ч���ڡ�����ʧЧ���ڲ��ɱ༭
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"cvendorname").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"nquoteprice").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"nquotetaxprice").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"deliverdays").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"dvaliddate").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"dinvaliddate").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getTailItem(
						"cquotepsn").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getTailItem(
						"dquotedate").setEnabled(false);
			} else if (ibillStatus
					.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"4004070101", "UPT4004070101-000022")/* @res "����" */)
					|| ibillStatus
							.equals(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4004070101",
											"UPP4004070101-000050")/* @res "����" */)) {// ѯ���۵�״̬Ϊ�����򱨼�
				// ѯ���۵��ı�ͷ��Ŀ���ɱ༭
				BillItem[] headItems = getBillCardPanelWrapper()
						.getBillCardPanel().getHeadItems();
				if (headItems != null && headItems.length > 0) {
					BillItem headItem = null;
					for (int i = 0; i < headItems.length; i++) {
						headItem = headItems[i];
						headItem.setEnabled(false);
					}
				}
				// ѯ���۵��ı����кš�������롢�����˰�ʡ���װ��ʽ���������������ڡ�������ַ���ɱ༭
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"crowno").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"cinventorycode").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"cvendorname").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"vfree").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"dreceivedate").setEnabled(false);
				if (getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"vreveiveaddress") != null) {
					getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
							"vreveiveaddress").setEnabled(false);
				}
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"vmemo").setEnabled(false);
				//since V5.3 ˰�ʿ����޸� modify by donggq
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"ntaxrate").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"nasknum").setEnabled(false);

				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"cvendorname").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"nquoteprice").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"nquotetaxprice").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"deliverdays").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"dvaliddate").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"dinvaliddate").setEnabled(true);
				getBillCardPanelWrapper().getBillCardPanel().getTailItem(
						"cquotepsn").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getTailItem(
						"dquotedate").setEnabled(false);

			}

		}
		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		String pk = ((AskbillHeaderVO) billVO.getParentVO()).getCaskbillid();
		
		if (pk != null && pk.trim().length() > 0) {
			if(ibillStatus != null && ibillStatus.trim() != null
					&& ibillStatus.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000340")/* @res "����" */)){
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"vaskbillcode").setEnabled(true);
			}else{
			 getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"vaskbillcode").setEnabled(false);
			}
		} else {
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"vaskbillcode").setEnabled(true);
		}
		if (getBillUI().getBillOperate() == IBillOperate.OP_REFADD)// ��������
		{
			setOperatorID();
		}
    //������������ʾ����
		if(getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vfree").isEnabled()){//2009/10/20  ����� - ����ж��������ɱ༭ʱ��Ÿ�����ʾ
			nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanelWrapper().getBillCardPanel(), getBufferData().getCurrentVO());
		}
		setButtonsStateEdit();
	}
	/**
	 * �˴����뷽��˵���� ��������:���ǵ����� ���ߣ���ά�� �������: ����ֵ: �쳣����: ����:
	 * 
	 * @throws Exception
	 */
	public void filterNullLine() throws Exception {
		// �����ֵ�ݴ�
		Object oTempValueForInv = null;
		Object oTempValueForCVen = null;
		// ����model
		nc.ui.pub.bill.BillModel bmBill = getBillCardPanelWrapper()
				.getBillCardPanel().getBillModel();
		// ����кţ�Ч�ʸ�һЩ��
		final int iInvCol = bmBill.getBodyColByKey("cmangid");
		final int iCvenCol = bmBill.getBodyColByKey("cvendormangid");

		// �����д����
		if (bmBill != null && iInvCol >= 0 && iInvCol < bmBill.getColumnCount()) {
			// ����
			final int iRowCount = getBillCardPanelWrapper().getBillCardPanel()
					.getRowCount();
			// �Ӻ���ǰɾ
			for (int line = iRowCount - 1; line >= 0; line--) {
				// ���δ��
				oTempValueForInv = bmBill.getValueAt(line, iInvCol);
				oTempValueForCVen = bmBill.getValueAt(line, iCvenCol);
				if (filterNullLineCondition(oTempValueForInv, oTempValueForCVen))
					// ɾ��
					getBillCardPanelWrapper().getBillCardPanel().getBillModel()
							.delLine(new int[] { line });
			}
		}
		if (bmBill.getRowCount() <= 0)
			onBoLineAdd();
	}

	private boolean filterNullLineCondition(Object oTempValueForInv, Object oTempValueForCVen) {
		return (oTempValueForInv == null || oTempValueForInv.toString()
				.trim().length() == 0)
				&& (oTempValueForCVen == null || oTempValueForCVen
						.toString().trim().length() == 0);
	}
	/**
	 * �˴����뷽��˵���� ��������:���ǵ����� ���ߣ���ά�� �������: ����ֵ: �쳣����: ����:
	 * 
	 * @throws Exception
	 */
	public void filterNullLineForAfterEditInv() throws Exception {
		// �����ֵ�ݴ�
		Object oTempValueForInv = null;
		Object oTempValueForCVen = null;
		// ����model
		nc.ui.pub.bill.BillModel bmBill = getBillCardPanelWrapper()
				.getBillCardPanel().getBillModel();
		// ����кţ�Ч�ʸ�һЩ��
		final int iInvCol = bmBill.getBodyColByKey("cmangid");
		final int iCvenCol = bmBill.getBodyColByKey("cvendormangid");

		// �����д����
		if (bmBill != null && iInvCol >= 0 && iInvCol < bmBill.getColumnCount()) {
			// ����
			final int iRowCount = getBillCardPanelWrapper().getBillCardPanel()
					.getRowCount();
			// �Ӻ���ǰɾ
			for (int line = iRowCount - 2; line >= 0; line--) {
				// ���δ��
				oTempValueForInv = bmBill.getValueAt(line, iInvCol);
				oTempValueForCVen = bmBill.getValueAt(line, iCvenCol);
				if (filterNullLineCondition(oTempValueForInv, oTempValueForCVen))
					// ɾ��
					getBillCardPanelWrapper().getBillCardPanel().getBillModel()
							.delLine(new int[] { line });
			}
		}
		if (bmBill.getRowCount() <= 0)
			onBoLineAdd();
	}
	/**
	 * �˴����뷽��˵���� ��������:���ǵ����� ���ߣ���ά�� �������: ����ֵ: �쳣����: ����:
	 * 
	 * @throws Exception
	 */
	public void filterNullLineForAfterEditInvNew() throws Exception {
		// �����ֵ�ݴ�
		Object oTempValueForInv = null;
		Object oTempValueForCVen = null;
		// ����model
		nc.ui.pub.bill.BillModel bmBill = getBillCardPanelWrapper()
				.getBillCardPanel().getBillModel();
		// ����кţ�Ч�ʸ�һЩ��
		final int iInvCol = bmBill.getBodyColByKey("cmangid");
		final int iCvenCol = bmBill.getBodyColByKey("cvendormangid");

		// �����д����
		if (bmBill != null && iInvCol >= 0 && iInvCol < bmBill.getColumnCount()) {
			// ����
			final int iRowCount = getBillCardPanelWrapper().getBillCardPanel()
					.getRowCount();
			// �Ӻ���ǰɾ
			for (int line = iRowCount -1; line >= 0; line--) {
				// ���δ��
				oTempValueForInv = bmBill.getValueAt(line, iInvCol);
				oTempValueForCVen = bmBill.getValueAt(line, iCvenCol);
				if (filterNullLineCondition(oTempValueForInv, oTempValueForCVen))
					// ɾ��
					getBillCardPanelWrapper().getBillCardPanel().getBillModel()
							.delLine(new int[] { line });
			}
		}
			onBoLineAdd();
	}
	/**
	 * ��ťm_boSave���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoSave() throws Exception {
		// ��ֹ�༭
		getBillCardPanelWrapper().getBillCardPanel().stopEditing();
		// ȡģ������
		filterNullLine();
		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(billVO);
        //���ֵ���
        AskbillItemMergeVO.validate((AskbillItemMergeVO[])billVO.getChildrenVO());
		convertIBillStatusToBack(billVO);
		//���۱任�����ñ����˺ͱ�������
		boolean isQuotePriceCHG = ((AskAndQuoteUI)getBillUI()).isQuotePriceCHG;
		if(isQuotePriceCHG){
			((AskbillHeaderVO)billVO.getParentVO()).setCquotepsn(getUserId());
			((AskbillHeaderVO)billVO.getParentVO()).setDquotedate(new UFDate(getLogDate()));
		}
		((AskAndQuoteUI)getBillUI()).isQuotePriceCHG = false;
		((AskAndQuoteUI)getBillUI()).nquotepriceBeforeEdit = null;
		((AskAndQuoteUI)getBillUI()).nquotetaxpriceBeforeEdit = null;
		// �����������
//		Object o = null;
		Vector v = new Vector();
		boolean isSave = true;
		// �ж��Ƿ��д�������
		if (onBoSaveCondition(billVO)) {
			isSave = false;
			return;
		} else {
			// ���ǿ��ֶ�
			if (!checkBeforeSave((AskbillMergeVO) billVO)) {
				return;
			}
			//���ñ�ע
			if( getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane ){
				UIRefPane nRefPanel = (UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmemo").getComponent();
				UITextField vMemoField = nRefPanel.getUITextField();
				((AskbillHeaderVO)billVO.getParentVO()).setVmemo(vMemoField.getText());
			}
			if (getBillUI().getBillOperate() == IBillOperate.OP_ADD )//��������
			{
			   ((AskbillHeaderVO)billVO.getParentVO()).setBpurchase(new UFBoolean(IIsGenOrder.SELECTED));
			}
			// �Ƶ�ʱ��\����޸�ʱ�䴦��
			if (getBillUI().getBillOperate() == IBillOperate.OP_ADD
					|| getBillUI().getBillOperate() == IBillOperate.OP_REFADD)// ��������
			{
				((AskbillHeaderVO) billVO.getParentVO())
						.setTmaketime(getDateForTime());
				((AskbillHeaderVO) billVO.getParentVO())
				.setTlastmaketime(getDateForTime());
			} else if (getBillUI().getBillOperate() == IBillOperate.OP_EDIT)// �޸ı���
			{
				String currentTime = getDateForTime();
				((AskbillHeaderVO) billVO.getParentVO())
						.setTlastmaketime(currentTime);
				//2009/12/10 tianft
				if(((AskbillHeaderVO) billVO.getParentVO()).getTmaketime() == null)
					((AskbillHeaderVO) billVO.getParentVO()).setTmaketime(currentTime);
			}
      setInfoForConvertForBack(billVO);
			v = AskbillMergeVO.convertVOTOBack(billVO);
			v = AskHelper.doSaveForAskBill(v);
            //������������ʾ����
			nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanelWrapper().getBillCardPanel(), null);
      setInfoForConvertForFront(v);
			billVO = AskbillMergeVO.convertVOTOFront(v);
			 //����������
			setVfree((AskbillItemMergeVO[])billVO.getChildrenVO());
		}
		if (isSave) {
			if (isEditing()) {
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(billVO);
					getBufferData().setCurrentRow(0);
				} else {
					getBufferData().setCurrentVO(billVO);
					getBufferData().setCurrentRow(
							getBufferData().getCurrentRow());
				}
			} else if (isAdding()) {
				ArrayList refVOs = ((BillUIRefCacheForPP) (((AskAndQuoteUI) getBillUI())
						.getCacheData())).getCurRefCache();
				if (refVOs != null && refVOs.size() > 0) {// ��ת��
					if (refVOs.size() == 1) {
						refVOs.remove(0);
					} else {
						refVOs
								.remove(((BillUIRefCacheForPP) (((AskAndQuoteUI) getBillUI())
										.getCacheData())).getSelectedRow());
					}
					if (refVOs.size() >= 1) {
						AggregatedValueObject[] vos = new AggregatedValueObject[refVOs
								.size()];
						refVOs.toArray(vos);
						if (vos != null && vos.length > 0) {
							setRefDataForSave(vos);
						}
					}
				}
				// �������������
				getBufferData().addVOsToBuffer(
						new AggregatedValueObject[] { billVO });
				if( 
						((BillUIRefCacheForPP)(((AskAndQuoteUI)getBillUI()).getCacheData())).getCurRefCache() != null 
						&& ((BillUIRefCacheForPP)(((AskAndQuoteUI)getBillUI()).getCacheData())).getCurRefCache().size() == 1 )
				   {
						((BillUIRefCacheForPP)(((AskAndQuoteUI)getBillUI()).getCacheData())).setSelectedRow(0);
					}
				if(getBufferData() != null && getBufferData().getVOBufferSize() >= 1)
				if (getBufferData().getVOBufferSize() == 1) {
					
					getBufferData().setCurrentRow(0);
				} else {
					getBufferData().setCurrentRow(getBufferData().getVOBufferSize() - 1);
				}
			}
		}
		getBufferData().updateView();
		ArrayList refVOs = ((BillUIRefCacheForPP) (((AskAndQuoteUI) getBillUI())
				.getCacheData())).getCurRefCache();
		if(refVOs == null || (refVOs != null && refVOs.size() == 0)){//�����ظ�����loadSourceInfoAll
          //��ʾ������Դ��Ϣ
		  nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanelWrapper().getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_ASK);
		}
		String cdeptid = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cdeptid").getValue();
		boolean isExecFormula = false;
		if(cdeptid == null ||(cdeptid != null && cdeptid.length() == 0)){
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cdeptid").setValue(billVO.getParentVO().getAttributeValue("cdeptid"));
			isExecFormula = true;
		}
		String cemployeeid = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cemployeeid").getValue();
        if(cemployeeid == null ||(cemployeeid != null && cemployeeid.length() == 0)){
        	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cemployeeid").setValue(billVO.getParentVO().getAttributeValue("cemployeeid"));
        	isExecFormula = true;
        }
        if(isExecFormula){
         getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
         getBillCardPanelWrapper().getBillCardPanel().updateValue();
        }
        if (getBufferData().getCurrentVO() != null ){
	        /* ��ʾ��ע */
			if(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane){
				UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmemo").getComponent();
				nRefPanel3.setValue(((AskbillHeaderVO)getBufferData().getCurrentVO().getParentVO()).getVmemo());
			}
        }
		// ���ñ����״̬
		setSaveOperateState();
		setButtonsStateBrowse();
		((BillManageUI)getBillUI()).showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH005")/*@res "����ɹ�"*/);

	}

	private boolean onBoSaveCondition(AggregatedValueObject billVO) {
		return billVO.getParentVO() == null
				|| (billVO.getChildrenVO() == null || (billVO.getChildrenVO() != null && billVO
						.getChildrenVO().length == 0));
	}

	/**
	 * ����ǰ�ǿ�����
	 */
	private boolean checkBeforeSave(AskbillMergeVO VO) {
		if (VO == null || (VO != null && VO.getParentVO() == null)) {
			return false;
		}
		 /* ��鵥��ģ��ǿ��� */
		try {
			nc.ui.pu.pub.PuTool.validateNotNullField(getBillCardPanelWrapper().getBillCardPanel());
		}
	    catch (Exception e) {
	    	PuTool.outException(getBillCardPanelWrapper().getBillCardPanel(), e);
	    	return false;
	    }
		// ��ͷ�ǿ�����
		AskbillHeaderVO headVO = (AskbillHeaderVO) VO.getParentVO();
//		String vaskbillcode = headVO.getVaskbillcode();
		String pk_purorg = headVO.getPk_purorg();
		String ccurrencytypeid = headVO.getCcurrencytypeid();
//		String caskpsn = headVO.getCaskpsn();
//		UFDate daskdate = headVO.getDaskdate();
//		String cquotepsn = headVO.getCquotepsn();
//		UFDate dquotedate = headVO.getDquotedate();
//		String ibillstatus = headVO.getIbillstatus();
		String  message = " ";
		String  messageHead = " ";
		Vector errorHead = new Vector();
		/* ���ɹ���֯ */
		if (pk_purorg == null
				|| (pk_purorg != null && pk_purorg.trim().length() == 0)) {
			errorHead.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101",
			"UPP4004070101-000092"));
		}
		/* ������ */
		if (ccurrencytypeid == null
				|| (ccurrencytypeid != null && ccurrencytypeid.
						trim().length() == 0)) {
			errorHead.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101",
			"UPP4004070101-000093"));
		}
		AskbillItemMergeVO[] itemVOs = (AskbillItemMergeVO[]) VO
				.getChildrenVO();
		AskbillItemMergeVO itemVO = null;
		String crownum = null;
		String cmangid = null;
//		UFDouble asknum = null;
		UFDate dvaliddate = null;
		UFDate dinvaliddate = null;
		String messageForBody = "";
		Vector errorItems = new Vector();
		String errorForDate = null;
		int errorRow = 0;
		for (int i = 0; i < itemVOs.length; i++) {
			itemVO = new AskbillItemMergeVO();
			itemVO = itemVOs[i];
			crownum = itemVO.getCrowno();
			cmangid = itemVO.getCmangid();
//			asknum = itemVO.getNasknum();
			dvaliddate = itemVO.getDvaliddate();
			dinvaliddate = itemVO.getDinvaliddate();
			if (cmangid != null && cmangid.trim().length() > 0) {
				/* ����к� */
				if (crownum == null
						|| (crownum != null && crownum.trim()
								.length() == 0)) {
					errorItems.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101",
			"UPP4004070101-000089"));
					
				}
				/* ������� */
//				if (asknum == null
//						|| (asknum != null && asknum.toString().trim().length() == 0)) {
//					errorItems.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101",
//					"UPP4004070101-000090"));
//				}
			} else if (crownum != null
					&& crownum.trim().length() > 0) {
				/* ����� */
				if (cmangid == null
						|| (cmangid != null && cmangid.trim()
								.length() == 0)) {
					errorItems.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101",
					"UPP4004070101-000091"));
				}
				/* ������� */
//				if (asknum == null
//						|| (asknum != null && asknum.toString().trim().length() == 0)) {
//					errorItems.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101",
//					"UPP4004070101-000090"));
//				}
			}
			if(dvaliddate != null && dvaliddate.toString().trim().length() > 0 && dinvaliddate != null && dinvaliddate.toString().trim().length() > 0){
				if(dinvaliddate.before(dvaliddate)){
					errorForDate = nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101",
					"UPP4004070101-000111");
				}
			}
			if((errorItems != null && errorItems.size() > 0) || (errorForDate != null && errorForDate.trim().length() > 0)){
				errorRow = i+1;
				break;
			}
		}
        if(errorHead != null && errorHead.size() > 0){
        	for(int i = 0 ; i <errorHead.size();i ++ ){
        		if(i < errorHead.size() - 1){
        			messageHead += errorHead.get(i).toString() + " ��";
        		}else{
        			messageHead += errorHead.get(i).toString() + " ";
        		}
        	}
        }
        if(errorItems != null && errorItems.size() > 0){
        	for(int i = 0 ; i <errorItems.size();i ++ ){
        		if(i < errorHead.size() - 1){
        			messageForBody += errorItems.get(i).toString() + " ��";
        		}else{
        			messageForBody += errorItems.get(i).toString() + " ";
        		}
        	}
        }
        if(messageHead != null && messageHead.trim().length() > 0){
        	message += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004070101", "UPP4004070101-000094",
					null, 
					new String[] { messageHead });/*
																 * @res
																 * "��ͷ:{0}����Ϊ��!"
																 */;
        }
        if(messageForBody != null && messageForBody.trim().length() > 0){
        	if(errorForDate != null && errorForDate.trim().length() > 0){
        		message += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004070101", "UPP4004070101-000112",
    					null, 
    					new String[] { new Integer(errorRow).toString() , messageForBody,errorForDate });/*
    																 * @res
    																 * "����:��{0}��{1}����Ϊ��,{2}!"
    																 */;
        	}else{
        	  message += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004070101", "UPP4004070101-000095",
					null, 
					new String[] { new Integer(errorRow).toString() , messageForBody });/*
																 * @res
																 * "����:��{0}��{1}����Ϊ��!"
																 */;
        	}
        }else if(errorForDate != null && errorForDate.trim().length() > 0){
    		message += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004070101", "UPP4004070101-000113",
					null, 
					new String[] { new Integer(errorRow).toString() ,errorForDate });/*
																 * @res
																 * "����:��{0}��{1}!"
																 */;
    	}
        if(message != null && message.trim().length() > 0){
        	 MessageDialog.showWarningDlg((AskAndQuoteUI)getBillUI(),
    		 nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101",
    	 "UPP4004070101-000069")/* @res "����ģ��ǿ�����" */,
    	                     message);
        	 ((BillManageUI)getBillUI()).showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
     		 "SCMCOMMON", "UPPSCMCommon-000010")/* @res "����ʧ��" */);
     		 return false;
        }
		return true;
	}

	/**
	 * ����״̬ת��
	 */
	private void convertIBillStatusToBack(AggregatedValueObject billVO) {
		// ״̬ת��(���ɡ�����������)--����
		if (((AskbillHeaderVO) billVO.getParentVO()).getIbillstatus().equals(
        StatusConvert.getStatusIndexNum().get(
						new Integer(IAskBillStatus.FREE)))) {
			((AskbillHeaderVO) billVO.getParentVO())
					.setIbillstatus(new Integer(IAskBillStatus.FREE).toString());
		} else if (((AskbillHeaderVO) billVO.getParentVO()).getIbillstatus()
				.equals(
            StatusConvert.getStatusIndexNum().get(
								new Integer(IAskBillStatus.SENDING)))) {
			((AskbillHeaderVO) billVO.getParentVO())
					.setIbillstatus(new Integer(IAskBillStatus.SENDING)
							.toString());
		} else if (((AskbillHeaderVO) billVO.getParentVO()).getIbillstatus()
				.equals(
            StatusConvert.getStatusIndexNum().get(
								new Integer(IAskBillStatus.QUOTED)))) {
			((AskbillHeaderVO) billVO.getParentVO())
					.setIbillstatus(new Integer(IAskBillStatus.QUOTED)
							.toString());
		} else if (((AskbillHeaderVO) billVO.getParentVO()).getIbillstatus()
				.equals(
            StatusConvert.getStatusIndexNum().get(
								new Integer(IAskBillStatus.CONFIRM)))) {
			((AskbillHeaderVO) billVO.getParentVO())
					.setIbillstatus(new Integer(IAskBillStatus.CONFIRM)
							.toString());
		}
	}

	/**
	 * ȱʡ��ѯ�Ի��� ������ӳ����������滻��ѯģ��������ظ÷��� �������ڣ�(2004-1-3 18:13:36)
	 */
	protected UIDialog createQueryUI() {
		return getM_dlgIqQueryCondition();
//		return getQueryDlg();
	}

	/**
	 * ��ȡ���ڵ㹦�ܽڵ�ID �������ڣ�(2001-10-20 17:29:24)
	 * 
	 * @return java.lang.String
	 */
	private String getFuncId() {
		String funId = null;
		if (funId == null || "".equals(funId.trim())) {
			funId = "4004070101";
		}
		return funId;
	}

	/**
	 * ���ڵ��ӱ�Ľ��洦���������ش˷���������ݣ�ͬʱ���л������� ���� getBufferDataForAsk().clear();
	 * getBufferData().adddVOToBuffer(aVo); //����б�setListHeadData(queryVos);
	 * //���õ��ݲ���״̬ getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
	 */
	protected void onBoQuery() throws Exception {
		// ��ȡ��ѯ����
//		int rettype = getQueryDlg().showModal();
		int rettype = getM_dlgIqQueryCondition().showModal();
		if (rettype == UIDialog.ID_OK) {
			// ���ǵ�ɾ������
			getBufferData().setCurrentRow(0);
			// ���ø�������
			isQueryORRefresh = true;
			getAskbillVOsFromDB(-1);
			isQueryORRefresh = false;

			if (((BillManageUI) getBillUI()).isListPanelSelected()) {// �б�
				updateBufferForAsk();
				if (!(getBufferData().getVOBufferSize() == 0 || getBufferData()
						.getCurrentRow() == -1)) {
					convertIbillstatusToString();
					((BillManageUI) getBillUI()).getBillListPanel()
							.setHeaderValueVO(
									getBufferData().getAllHeadVOsFromBuffer());
					((BillManageUI) getBillUI()).getBillListPanel()
							.getHeadBillModel().execLoadFormula();
					((BillManageUI) getBillUI()).getBillListPanel()
							.getBodyBillModel().setBodyDataVO(
									getBufferData().getCurrentVO()
											.getChildrenVO());
					((BillManageUI) getBillUI()).getBillListPanel()
							.getBodyBillModel().execLoadFormula();
					((BillManageUI) getBillUI()).getBillListPanel()
							.getHeadBillModel().updateValue();
					((BillManageUI) getBillUI()).getBillListPanel()
							.getBodyBillModel().updateValue();

					// �б���ʾ���ݴ���
					((BillManageUI) getBillUI()).getBillListPanel()
							.getHeadBillModel().setRowState(
									getBufferData().getCurrentRow(),
									BillModel.SELECTED);
					((BillManageUI) getBillUI()).getBillListPanel()
							.getHeadTable().setRowSelectionInterval(
									getBufferData().getCurrentRow(),
									getBufferData().getCurrentRow());
					((BillManageUI) getBillUI())
							.setBillOperate(IBillOperate.OP_NOTEDIT);
					((BillManageUI) getBillUI()).getBillListPanel().updateUI();
				} else {

					((BillManageUI) getBillUI()).setListHeadData(null);
					((BillManageUI) getBillUI())
							.setBillOperate(IBillOperate.OP_INIT);
					getBufferData().setCurrentRow(-1);
					((BillManageUI) getBillUI())
							.showHintMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("uifactory",
											"UPPuifactory-000066")/*
																	 * @res
																	 * "û�в鵽�κ���������������!"
																	 */);
				}
				setButtonsStateListNormal();
				try {
					((AskAndQuoteUI) getBillUI()).updateButtonUI();
				} catch (Exception e) {
					// TODO �Զ����� catch ��
					SCMEnv.out(e.getMessage());
					throw e;
				}
			} else {// ��Ƭ
				setBillVOToCard();
			}

		}
	}

	/**
	 * ��ťm_boRefresh���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoRefresh() throws Exception {
		
		if(getBufferData().getVOBufferSize() == 0){
			return;
		}
        //���ǵ�ɾ������
		getBufferData().setCurrentRow(0);
		// ���ø�������
		isQueryORRefresh = true;
		getAskbillVOsFromDB(-1);
		isQueryORRefresh = false;
		updateBufferForAsk();
		if (((BillManageUI) getBillUI()).isListPanelSelected()) {// �б�
			if (!(getBufferData().getVOBufferSize() == 0 || getBufferData()
					.getCurrentRow() == -1)) {
				convertIbillstatusToString();
				setButtonsStateListNormal();
				((BillManageUI) getBillUI()).getBillListPanel()
						.setHeaderValueVO(
								getBufferData().getAllHeadVOsFromBuffer());
				((BillManageUI) getBillUI()).getBillListPanel()
						.getHeadBillModel().execLoadFormula();
				((BillManageUI) getBillUI()).getBillListPanel()
						.getHeadBillModel().updateValue();
			}
		} else {// ��Ƭ
			setBillVOToCard();
			setButtonsStateBrowse();
		}
	}

	/**
	 * @���ܣ�����ѯ�۵�VO
	 * @���ߣ����� �������ڣ�(2001-6-8 21:24:36)
	 * @param: <|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 * @return nc.vo.pp.ask.AskbillVO[]
	 * @param strQueryCondition
	 *            java.lang.String
	 * @throws Exception
	 */
	protected void getAskbillVOsFromDB(int currentRow) throws Exception {
		UFBoolean[] status = {
				new UFBoolean(m_chkFree == null ? true : m_chkFree.isSelected()),
				new UFBoolean(m_chkSend == null ? true : m_chkSend.isSelected()),
				new UFBoolean(m_chkQuote == null ? true : m_chkQuote
						.isSelected()),
				new UFBoolean(m_chkEnd == null ? true : m_chkEnd.isSelected()) };
		AskbillMergeVO[] mergeVOs = null;
		AskbillHeaderVO[] headerVOs = null;
		ConditionVO[] conds = null;
		Vector v = new Vector();
		String headerPK = null;
		try {
			if (isQueryORRefresh) {// ��ѯ��ˢ��
				conds = getM_dlgIqQueryCondition().getConditionVO();
				//liuys
				v = AskHelper.queryAllInquireMy(conds, getCorpId(), status,getUserId());
//				new UFBoolean(getQueryDlg().isQryFree()),
//				new UFBoolean(getQueryDlg().isQryFree()),
//				new UFBoolean(getQueryDlg().isQryQuote()),
//				new UFBoolean(getQueryDlg().isQryEnd()) };
//		AskbillMergeVO[] mergeVOs = null;
//		AskbillHeaderVO[] headerVOs = null;
////		ConditionVO[] conds = null;
//		Vector v = new Vector();
//		String headerPK = null;
//		try {
//			if (isQueryORRefresh) {// ��ѯ��ˢ��
//				String strSql = null;
////				while(strSql == null ){
//					String wherePart = getQueryDlg().getWhereSQL();
//					if(wherePart != null){
//						strSql = PpTool.getSQLForInquireHeaderVOsMy(wherePart, getCorpId(), status);
//						v = AskHelper.queryAllInquireMy(strSql);
//					}
////					if(strSql == null){
////						int iContinue = getBillUI().showOkCancelMessage(nc.ui.ml.NCLangRes
////								.getInstance().getStrByID("scmpub", "UPPscmpub-000779")/*
////								 * @res
////								 * "������ҵ��δ�����κε�Լ������,��ӱ�ϵͳ��ȡ����ҵ�����ݲ����кܳ�ʱ��Ĳ���,\n������Ҫ��ʱ����ܵõ���ϣ����ҵ�����ݲ��ᵼ��ϵͳ����Ч�ʼ����½�,\n���ϵͳ��æ.���Ƿ񷵻�ָ��Լ������?"
////								 */);
////						if (iContinue == UIDialog.ID_CANCEL)
////							return;
////						else if (iContinue == UIDialog.ID_OK) {
////							getQueryDlg().showModal();
////						}
////				}
////				}
			} else {// ��ѡ�����š����š����š�ĩ�Ű�ťʱ
				// �������
				AggregatedValueObject modelVo = getBufferData().getVOByRowNo(
						currentRow);
				if (modelVo != null
						&& modelVo.getParentVO() != null
						&& (modelVo.getChildrenVO() == null || (modelVo
								.getChildrenVO() != null && modelVo
								.getChildrenVO().length == 0))) {
					AskbillHeaderVO headerVO = (AskbillHeaderVO) modelVo
							.getParentVO();
					headerPK = headerVO.getCaskbillid();
//					v = AskHelper.findByPrimaryKeyForDataPower(headerPK,null);
					v = AskHelper.findByPrimaryKeyForDataPower(headerPK,conds);

				} else {
					return;
				}
			}

			long tTime = System.currentTimeMillis();
			AskbillHeaderVO headerVO = null;
			AskbillItemVO[] itemVOs = null;
			AskbillItemBVO[] itemBVOs = null;
			AskbillItemVendorVO[] itemVendorVOs = null;
			AskbillMergeVO askbillMergeVO = null;
			Vector vvvv = new Vector();
			Vector vvv = null;
			if (v != null && v.size() > 0) {
				if (isQueryORRefresh) {// ��ѯ��ˢ��
					headerVOs = (AskbillHeaderVO[]) v.get(0);
					if (headerVOs != null && headerVOs.length > 0) {
						for (int i = 0; i < headerVOs.length; i++) {
							// �����ѯ����ĵ�һ���б����
							if (i == 0) {
								headerVO = headerVOs[0];
								itemVOs = (AskbillItemVO[]) v.get(1);
								itemBVOs = (AskbillItemBVO[]) v.get(2);
								itemVendorVOs = (AskbillItemVendorVO[]) v
										.get(3);
								vvv = new Vector();
								vvv.add(headerVO);
								vvv.add(itemVOs);
								vvv.add(itemBVOs);
								vvv.add(itemVendorVOs);
                                setInfoForConvertForFront(vvv);
								askbillMergeVO = AskbillMergeVO.convertVOTOFront(vvv);
								setVfree(askbillMergeVO);
								vvvv.add(askbillMergeVO);
							}
							// �����ѯ�������û�б����
							else {
								headerVO = headerVOs[i];
								vvv = new Vector();
								vvv.add(headerVO);
								vvv.add(null);
								vvv.add(null);
								vvv.add(null);
                                setInfoForConvertForFront(vvv);
								askbillMergeVO = AskbillMergeVO.convertVOTOFront(vvv);
                                 //����������
								setVfree(askbillMergeVO);
								vvvv.add(askbillMergeVO);
							}

						}

					}
				} else {// ��ѡ�����š����š����š�ĩ�Ű�ťʱ
					headerVO = (AskbillHeaderVO) v.get(0);
					itemVOs = (AskbillItemVO[]) v.get(1);
					itemBVOs = (AskbillItemBVO[]) v.get(2);
					itemVendorVOs = (AskbillItemVendorVO[]) v.get(3);
					vvv = new Vector();
					vvv.add(headerVO);
					vvv.add(itemVOs);
					vvv.add(itemBVOs);
					vvv.add(itemVendorVOs);
                    setInfoForConvertForFront(vvv);
					askbillMergeVO = AskbillMergeVO.convertVOTOFront(vvv);
					 //����������
					setVfree(askbillMergeVO);
					vvvv.add(askbillMergeVO);
				}

			}
			if (vvvv.size() > 0) {
				mergeVOs = new AskbillMergeVO[vvvv.size()];
				vvvv.copyInto(mergeVOs);
				if (isQueryORRefresh) {// ��ѯ��ˢ��
					getBufferData().clear();
					getBufferData().addVOsToBuffer(mergeVOs);
				} else {// ��ѡ�����š����š����š�ĩ�Ű�ťʱ
					getBufferData().setVOAt(currentRow, mergeVOs[0]);
				}
			} else {
				getBufferData().clear();
			}

			tTime = System.currentTimeMillis() - tTime;
			SCMEnv.out("��ѯ����ʱ�䣺" + tTime + " ms!");
		} catch (Exception e) {
			throw e;
		}
			getBufferData().updateView();
	}

	private void setVfree(AskbillMergeVO askbillMergeVO) {
		AskbillItemMergeVO[] itemTempMergeVOs = (AskbillItemMergeVO[])askbillMergeVO.getChildrenVO();
		  // ����������
		Vector vTemp = new Vector();
		if(itemTempMergeVOs != null && itemTempMergeVOs.length > 0){
		    for(int j = 0; j < itemTempMergeVOs.length; j++){
		        if(itemTempMergeVOs[j].getVfree1() != null || itemTempMergeVOs[j].getVfree2() != null || 
		            itemTempMergeVOs[j].getVfree3() != null || itemTempMergeVOs[j].getVfree4() != null ||
		            itemTempMergeVOs[j].getVfree5() != null) {
		          vTemp.addElement(itemTempMergeVOs[j]);}
		        }
		    }
   if(vTemp.size() > 0){
     AskbillItemMergeVO[] askItems = new AskbillItemMergeVO[vTemp.size()];
		vTemp.copyInto(askItems);
		new nc.ui.scm.pub.FreeVOParse().setFreeVO(askItems, "vfree","vfree","cbaseid","cmangid",true);
   }
	}

	public void setVfree(AskbillItemMergeVO[] itemTempMergeVOs) {
		Vector vTemp = new Vector();
		        if(itemTempMergeVOs != null && itemTempMergeVOs.length > 0){
		            for(int j = 0; j < itemTempMergeVOs.length; j++){
		                if(itemTempMergeVOs[j].getVfree1() != null || itemTempMergeVOs[j].getVfree2() != null || 
		                    itemTempMergeVOs[j].getVfree3() != null || itemTempMergeVOs[j].getVfree4() != null ||
		                    itemTempMergeVOs[j].getVfree5() != null) {
		                  vTemp.addElement(itemTempMergeVOs[j]);}
		                }
		            }
		    if(vTemp.size() > 0){
		      AskbillItemMergeVO[] askItems = new AskbillItemMergeVO[vTemp.size()];
		        vTemp.copyInto(askItems);
		        new nc.ui.scm.pub.FreeVOParse().setFreeVO(askItems, "vfree","vfree","cbaseid","cmangid",true);
		    }
	}

	/**
	 * @���ܣ�����ѯ�۵�VO
	 * @���ߣ����� �������ڣ�(2001-6-8 21:24:36)
	 * @param: <|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 * @return nc.vo.pp.ask.AskbillVO[]
	 * @param strQueryCondition
	 *            java.lang.String
	 */
	protected void getAskbillVOsFromDBForListCHG(int nCurIndex) {
		AskbillMergeVO[] mergeVOs = null;
		Vector v = new Vector();
		String headerPK = null;
		try {
			// �������
			AggregatedValueObject modelVo = getBufferData().getVOByRowNo(
					nCurIndex);

			if (modelVo != null && modelVo.getParentVO() != null) {
				// ��������Ϊ�ղŲ�ѯ
				if (modelVo.getChildrenVO() == null
						|| (modelVo.getChildrenVO() != null && modelVo
								.getChildrenVO().length == 0)) {
					AskbillHeaderVO headerVO = (AskbillHeaderVO) modelVo
							.getParentVO();
					headerPK = headerVO.getCaskbillid();
					v = AskHelper.findByPrimaryKey(headerPK);
				} else {
					return;
				}
			}
			// ��֯���ݷ���
			AskbillHeaderVO headerVO = null;
			AskbillItemVO[] itemVOs = null;
			AskbillItemBVO[] itemBVOs = null;
			AskbillItemVendorVO[] itemVendorVOs = null;
			AskbillMergeVO askbillMergeVO = null;
			Vector vvvv = new Vector();
			Vector vvv = null;
			if (v != null && v.size() > 0) {
				headerVO = (AskbillHeaderVO) v.get(0);
				itemVOs = (AskbillItemVO[]) v.get(1);
				itemBVOs = (AskbillItemBVO[]) v.get(2);
				itemVendorVOs = (AskbillItemVendorVO[]) v.get(3);
				vvv = new Vector();
				vvv.add(headerVO);
				vvv.add(itemVOs);
				vvv.add(itemBVOs);
				vvv.add(itemVendorVOs);
                setInfoForConvertForFront(vvv);
				askbillMergeVO = AskbillMergeVO.convertVOTOFront(vvv);
				 //����������
				setVfree(askbillMergeVO);
				vvvv.add(askbillMergeVO);
			}
			if (vvvv.size() > 0) {
				mergeVOs = new AskbillMergeVO[vvvv.size()];
				vvvv.copyInto(mergeVOs);
				getBufferData().setVOAt(nCurIndex, mergeVOs[0]);
				getBufferData().setCurrentRow(nCurIndex);
				updateBufferForAskListCHG();
			}

		} catch (Exception e) {
			PuTool.outException(e);
		}

	}

	/**
	 * ��ָ����VO���� <I>resultVOs </I>ȥ����BillUIBuffer.����������Ȱ�Buffer��ԭ�е�������ա�
	 * ���ָ��resultVOsΪ��Buffer�����������CurrentRow������Ϊ-1 ����CurrentRow����Ϊ��0��
	 * 
	 * @throws Exception
	 */
	protected void updateBufferForAsk() throws Exception // ��ʱ�ĳ�final,ȷ��Ŀǰ��û���˼̳й���
	{

		if (getBufferData().getVOBufferSize() != 0) {

			getBillUI().setListHeadData(
					getBufferData().getAllHeadVOsFromBuffer());
			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		} else {
			getBillUI().setListHeadData(null);
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
			getBufferData().setCurrentRow(-1);
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000066")/* @res "û�в鵽�κ���������������!" */);
		}
	}
	
	/**
	 * ��ָ����VO���� <I>resultVOs </I>ȥ����BillUIBuffer.����������Ȱ�Buffer��ԭ�е�������ա�
	 * ���ָ��resultVOsΪ��Buffer�����������CurrentRow������Ϊ-1 ����CurrentRow����Ϊ��0��
	 * 
	 * @throws Exception
	 */
	protected void updateBufferForAskListCHG() throws Exception // ��ʱ�ĳ�final,ȷ��Ŀǰ��û���˼̳й���
	{

		if (getBufferData().getVOBufferSize() != 0) {
			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
		} else {
			getBillUI().setListHeadData(null);
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
			getBufferData().setCurrentRow(-1);
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000066")/* @res "û�в鵽�κ���������������!" */);
		}
	}

	/**
	 * Action��ť���ʱִ�еĶ���,���б�Ҫ���븲��.����ѡ�����š����š����š�ĩ�Ű�ťʱ��
	 */
	public void onBoActionElse(ButtonObject bo) throws Exception {
		// �������
//		AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
		int intBtn = 0;
		if (bo.getData() != null)
			intBtn = ((ButtonVO) bo.getData()).getBtnNo();
//		beforeOnBoAction(intBtn, modelVo);
		int currentRow = getBufferData().getCurrentRow();
		if (intBtn == 21) {// ����
			currentRow = 0;
		} else if (intBtn == 22) {// ����
			currentRow = currentRow + 1;
			if (currentRow >= getBufferData().getVOBufferSize()) {
				return;
			}
		} else if (intBtn == 23) {// ����
			if (currentRow >= 1) {
				currentRow = currentRow - 1;
			} else {
				return;
			}
		} else if (intBtn == 24) {// ĩ��
			currentRow = getBufferData().getVOBufferSize() - 1;
			if (currentRow < 0) {
				return;
			}
		}
		getAskbillVOsFromDB(currentRow);
		if (intBtn == 21 || intBtn == 22 || intBtn == 23 || intBtn == 24) {
			getBufferData().setCurrentRow(currentRow);
		} else {
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		}
		// *******************
		AggregatedValueObject modelVo = getBufferData().getCurrentVO();
//		afterOnBoAction(intBtn, modelVo);
		if (((BillManageUI) getBillUI()).isListPanelSelected()) {// �б�
			convertIbillstatusToString();
		} else {// ��Ƭ
			setBillVOToCard();
			setButtonsStateBrowse();
		}
		// �����б�
		getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
		getBufferData().updateView();
        //��ʾ������Դ��Ϣ
		nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanelWrapper().getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_ASK);
	}
	/**
	 * ��ȡ��ѯ�Ի���ʵ��
	 */
	public SCMQueryConditionDlg getM_dlgIqQueryCondition() {
		if (m_dlgIqQueryCondition == null) {
			m_dlgIqQueryCondition = new SCMQueryConditionDlg();
			m_dlgIqQueryCondition.setShowPrintStatusPanel(false);
			m_dlgIqQueryCondition.setNormalShow(true);
			// ��Ļ�๫˾ѡ����
			m_dlgIqQueryCondition.hideUnitButton();
			try {
				m_dlgIqQueryCondition.setTempletID(getCorpId(), getFuncId(), getUserId(), null);
			} catch (Exception e) {
				PuTool.outException(e);
			}
			// ���볣������ - UIRadioButton
			m_chkFree = new UICheckBox();
			m_chkFree.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000340")/* @res "����" */);
			m_chkFree.setBackground(getBillCardPanelWrapper().getBillCardPanel().getBackground());
			m_chkFree.setForeground(java.awt.Color.black);
			m_chkFree.setSize(80, m_chkFree.getHeight());
			m_chkFree.setSelected(true);
			//
			m_chkSend = new UICheckBox();
			m_chkSend.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101", "UPT4004070101-000022")/* @res "����" */);
			m_chkSend.setBackground(m_chkFree.getBackground());
			m_chkSend.setForeground(m_chkFree.getForeground());
			m_chkSend.setSize(m_chkFree.getSize());

			m_chkQuote = new UICheckBox();
			m_chkQuote.setText(NCLangRes.getInstance().getStrByID("4004070101", "UPP4004070101-000050")/* @res "����" */);
			m_chkQuote.setBackground(m_chkFree.getBackground());
			m_chkQuote.setForeground(m_chkFree.getForeground());
			m_chkQuote.setSize(m_chkFree.getSize());

			m_chkEnd = new UICheckBox();
			m_chkEnd.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101", "UPP4004070101-000051")/* @res "���" */);
			m_chkEnd.setBackground(m_chkFree.getBackground());
			m_chkEnd.setForeground(m_chkFree.getForeground());
			m_chkEnd.setSize(m_chkFree.getSize());

			m_chkFree.setLocation(50, 30);
			m_chkSend.setLocation(m_chkFree.getX(), m_chkFree.getY() + m_chkFree.getHeight() + 20);
			m_chkQuote.setLocation(m_chkSend.getX(), m_chkSend.getY() + m_chkSend.getHeight() + 20);
			m_chkEnd.setLocation(m_chkQuote.getX(), m_chkQuote.getY() + m_chkQuote.getHeight() + 20);
			//
			m_chkFree.setSelected(true);
			m_chkSend.setSelected(true);
			m_chkQuote.setSelected(true);
			m_chkEnd.setSelected(true);
			m_dlgIqQueryCondition.getUIPanelNormal().setLayout(null);
			//
			m_dlgIqQueryCondition.getUIPanelNormal().add(m_chkFree);
			m_dlgIqQueryCondition.getUIPanelNormal().add(m_chkSend);
			m_dlgIqQueryCondition.getUIPanelNormal().add(m_chkQuote);
			m_dlgIqQueryCondition.getUIPanelNormal().add(m_chkEnd);
			// ��Ӧ��
			UIRefPane refCustcode = new UIRefPane();
			refCustcode.setRefModel(new CustRefModelForAskQuery(getCorpId()));
			// wyf 2002-10-17 modify end
			m_dlgIqQueryCondition.setValueRef("bd_cubasdoc.custcode", refCustcode);
			
			// �������
			UIRefPane refInvcode = new UIRefPane();
			refInvcode.setRefModel(new InvCodeRefModelForAskQue(getCorpId()));
			// wyf 2002-10-17 modify end
			m_dlgIqQueryCondition.setValueRef("bd_invbasdoc.invcode", refInvcode);
			
			/* ҵ��Ա */
			UIRefPane refPrayPsn = new UIRefPane();
			PurPsnRefModel refPsnModel = new PurPsnRefModel(getCorpId());
			refPrayPsn.setRefModel(refPsnModel);
			m_dlgIqQueryCondition.setValueRef("bd_psndoc.psncode", refPrayPsn);
			/* ���� */
			UIRefPane refPrayDept = new UIRefPane();
			PurDeptRefModel refDeptModel = new PurDeptRefModel();
			refPrayDept.setRefModel(refDeptModel);
			m_dlgIqQueryCondition.setValueRef("bd_deptdoc.deptcode", refPrayDept);
			/* �ɹ���֯ */
			UIRefPane refPurOrg = new UIRefPane();
			PurorgDefaultRefModel refPurOrgModel = new PurorgDefaultRefModel("�ɹ���֯");
			refPurOrgModel.setPk_corp(getCorpId());
			refPurOrgModel.addWherePart(" and bd_purorg.ownercorp = '"
					+ _getCorp().getPrimaryKey()
					+ "' and bd_purorg.sealdate is null ");
			refPurOrg.setRefModel(refPurOrgModel);
			refPurOrg.setPk_corp(_getCorp().getPrimaryKey());
			m_dlgIqQueryCondition.setValueRef("bd_purorg.code", refPurOrg);
			// ѯ�۵�״̬
			String askStatusStr[] = {
					NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000340")/* @res "����" */,
					NCLangRes.getInstance().getStrByID("4004070101", "UPT4004070101-000008") /* @res "����" */};
			UIComboBox askStatusBox = new UIComboBox(askStatusStr);
			//v5.1����Ȩ��
		    ArrayList<String> alcorp=new ArrayList<String>();
		    alcorp.add(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
		    m_dlgIqQueryCondition.initCorpRef(IDataPowerForPrice.CORPKEYFORASKBILL,ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),alcorp);   
		    m_dlgIqQueryCondition.setCorpRefs(IDataPowerForPrice.CORPKEYFORASKBILL, AskPubVO.REFKEYS);   
			// �Զ�����
			askStatusBox.setTranslate(true);
			m_dlgIqQueryCondition.setValueRef("po_askbill.ibillstatus", askStatusBox);
			/* �����Զ��������� */
			DefSetTool.updateQueryConditionClientUserDef(
					m_dlgIqQueryCondition, getCorpId(), ScmConst.PO_AskBill, "po_askbill.vdef", "po_askbill_bb1.vbdef");
			/* Ĭ������ */
			m_dlgIqQueryCondition.setValueRef("po_askbill.daskdate", "����");
			m_dlgIqQueryCondition.setDefaultValue("po_askbill.daskdate", "po_askbill.daskdate", ClientEnvironment.getInstance().getDate().toString());
			/* ����������ʾ���� */
			m_dlgIqQueryCondition.setIsWarningWithNoInput(true);
			/* ���Ļ��������ܱ����� */
			m_dlgIqQueryCondition.setSealedDataShow(true);
			
			//2009/09/24 ����� v56��ѯ����������
			m_dlgIqQueryCondition.addCurToCurDate("po_askbill.daskdate");
			
		}
		return m_dlgIqQueryCondition;
	}
	/**
	 * ����Buffer�е�TS����ǰ����VO�� �������ڣ�(2004-5-14 18:04:59)
	 * 
	 * @param setVo
	 *            nc.vo.pub.AggregatedValueObject
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	public void setTSFormBufferToVO(AggregatedValueObject setVo)
			throws java.lang.Exception {
		if (setVo == null)
			return;
		AggregatedValueObject vo = getBufferData().getCurrentVO();
		if (vo == null)
			return;
		if (getBillUI().getBillOperate() == IBillOperate.OP_EDIT) {

			if (vo.getParentVO() != null && setVo.getParentVO() != null)
				setVo.getParentVO().setAttributeValue("ts",
						vo.getParentVO().getAttributeValue("ts"));
			// SuperVO[] changedvos = (SuperVO[]) setVo.getChildrenVO();
			SuperVO[] changedvos = (SuperVO[]) getChildVO(setVo);
//			HashMap changedVOMap = null;
			if (changedvos != null && changedvos.length != 0) {
				// �����������е��ֱ�����
				HashMap bufferedVOMap = null;
				// SuperVO[] bufferedVOs = (SuperVO[]) vo.getChildrenVO();
				SuperVO[] bufferedVOs = (SuperVO[]) getChildVO(vo);
        
				setUpSourceTs(changedvos,bufferedVOs);	
				
				if (bufferedVOs != null && bufferedVOs.length != 0) {
					bufferedVOMap = Hashlize.hashlizeObjects(bufferedVOs,
							new VOHashPrimaryKeyAdapter());
					for (int i = 0; i < changedvos.length; i++) {
						if (changedvos[i].getPrimaryKey() != null) {
							ArrayList bufferedAl = (ArrayList) bufferedVOMap
									.get(changedvos[i].getPrimaryKey());

							if (bufferedAl != null) {
								SuperVO bufferedVO = (SuperVO) bufferedAl
										.get(0);
								changedvos[i].setAttributeValue("ts",
										bufferedVO.getAttributeValue("ts"));
							}
						}
					}
				}
			}
		}
	}

	/**
	 * ����ӱ����ݡ� �������ڣ�(2004-3-11 17:44:14)
	 * 
	 * @return nc.vo.pub.CircularlyAccessibleValueObject[]
	 */
	private CircularlyAccessibleValueObject[] getChildVO(
			AggregatedValueObject retVo) {
		CircularlyAccessibleValueObject[] childVos = null;
		childVos = retVo.getChildrenVO();
		return childVos;
	}

	/**
	 * ��ťm_boLineAdd���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoLineAdd() throws Exception {
		// super.onBoLineAdd();
		// ������֮ǰ���õĳ��󷽷�
		getBillCardPanelWrapper().addLine();
		/* �����к� */
		int rowCount = getBillCardPanelWrapper().getBillCardPanel()
				.getRowCount();
		Object crowno = null;
		int row = 0;
		for (int i = 0; i < rowCount - 1; i++) {
			crowno = getBillCardPanelWrapper().getBillCardPanel()
					.getBodyValueAt(i, "crowno");
			if (crowno != null && crowno.toString().trim().length() > 0) {
				row = i;
			}
		}
		BillRowNo.addLineRowNo(getBillCardPanelWrapper().getBillCardPanel(),
		/* nc.vo.scm.pu.BillTypeConst.PO_ASK */"28", "crowno", row);

		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"crowno",true);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"cinventorycode",true);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"cvendorname",true);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"vfree",true);
		if (getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
				"vreveiveaddress") != null) {
			getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"vreveiveaddress",true);
		}
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"vmemo",true);
	
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"ntaxrate",true);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"nasknum",true);
		// ���������Ŀ���Ա༭
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"cvendorname",false);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"nquoteprice",false);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"nquotetaxprice",false);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"deliverdays",false);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"dvaliddate",false);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"dinvaliddate",false);
	}
	/**
	 * ��ťm_boLineAdd���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoLineAddForEasyUse() throws Exception {
		// ������֮ǰ���õĳ��󷽷�
		getBillCardPanelWrapper().addLine();
		/* �����к� */
		int rowCount = getBillCardPanelWrapper().getBillCardPanel()
				.getRowCount();
		int row = rowCount-1;
		BillRowNo.addLineRowNo(getBillCardPanelWrapper().getBillCardPanel(),
		/* nc.vo.scm.pu.BillTypeConst.PO_ASK */"28", "crowno", row);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"crowno",true);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"cinventorycode",true);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"cvendorname",true);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"vfree",true);
		if (getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
				"vreveiveaddress") != null) {
			getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"vreveiveaddress",true);
		}
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"vmemo",true);
	
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"ntaxrate",true);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"nasknum",true);
		// ���������Ŀ���Ա༭
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"cvendorname",false);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"nquoteprice",false);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"nquotetaxprice",false);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"deliverdays",false);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"dvaliddate",false);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"dinvaliddate",false);
	}

	/**
	 * ��ťm_boLineIns���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoLineIns() throws Exception {
		getBillCardPanelWrapper().insertLine();
		/* �����к� */
		BillRowNo.insertLineRowNo(getBillCardPanelWrapper().getBillCardPanel(),
		/* nc.vo.scm.pu.BillTypeConst.PO_ASK */"28", "crowno");
    //������ʾ 
		AskbillMergeVO voCurr = (AskbillMergeVO) getBillCardPanelWrapper().getBillCardPanel().getBillValueVO(AskbillMergeVO.class.getName(), AskbillHeaderVO.class.getName(), AskbillItemMergeVO.class.getName());
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanelWrapper().getBillCardPanel(), voCurr);

	}

	/**
	 * ��ťm_boLineCopy���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoLineCopy() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().copyLine();	
		getBillCardPanelWrapper().copySelectedLines();
	}

	/**
	 * ��ťm_boLinePaste���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoLinePaste() throws Exception {
		// ճ��ǰ�Ը��Ƶ�VO��Щҵ����صĴ���
		processCopyedBodyVOsBeforePaste(getBillCardPanelWrapper()
				.getCopyedBodyVOs());
		int iOrgRowCount = getBillCardPanelWrapper().getBillCardPanel()
				.getRowCount();
		int iNextRow = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getSelectedRow();
		getBillCardPanelWrapper().pasteLines();
		// ���ӵ�����
		int iPastedRowCount = getBillCardPanelWrapper().getBillCardPanel()
				.getRowCount()
				- iOrgRowCount;
		int[] crowNoa = new int[iPastedRowCount];
		for (int i = 0; i < iPastedRowCount; i++) {
			crowNoa[i] = iNextRow + i;
		}
		// �����к�
		BillRowNo.pasteLineRowNo(getBillCardPanelWrapper().getBillCardPanel(),
				BillTypeConst.PO_ASK, "crowno", iNextRow + iPastedRowCount,
				crowNoa);
        //������ʾ 

		AskbillMergeVO voCurr = (AskbillMergeVO) getBillCardPanelWrapper().getBillCardPanel().getBillValueVO(AskbillMergeVO.class.getName(), AskbillHeaderVO.class.getName(), AskbillItemMergeVO.class.getName());
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanelWrapper().getBillCardPanel(), voCurr);
	}
	/**
	 * ��ťm_boLinePaste���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoLinePasteToTail() throws Exception {
		if (getBillCardPanelWrapper().getCopyedBodyVOs() == null
				|| getBillCardPanelWrapper().getCopyedBodyVOs().length == 0){
			getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000424")/* "ճ���е���βδ�ɹ�,����ԭ��û�п������ݻ�δȷ��Ҫճ����λ��" */);
			return ;
		}else{
			getBillUI().showHintMessage("");
		}
		// ճ��ǰ�Ը��Ƶ�VO��Щҵ����صĴ���
		processCopyedBodyVOsBeforePaste(getBillCardPanelWrapper().getCopyedBodyVOs());
		getBillCardPanelWrapper().getBillCardPanel().pasteLineToTail();
			// �����к�
			BillRowNo.addLineRowNos(getBillCardPanelWrapper().getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_ASK, "crowno",getBillCardPanelWrapper()
					.getCopyedBodyVOs().length);
	    //������ʾ 
		AskbillMergeVO voCurr = (AskbillMergeVO) getBillCardPanelWrapper().getBillCardPanel().getBillValueVO(AskbillMergeVO.class.getName(), AskbillHeaderVO.class.getName(), AskbillItemMergeVO.class.getName());
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanelWrapper().getBillCardPanel(), voCurr);
	
	}
	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-8 16:33:05)
	 * 
	 * @param vos
	 *            nc.vo.pub.CircularlyAccessibleValueObject[]
	 */
	protected void processCopyedBodyVOsBeforePaste(
			CircularlyAccessibleValueObject[] vos) {
		if (vos == null)
			return;
        int rowCount = getBillCardPanelWrapper().getBillCardPanel().getRowCount();
        int invCount = 0;
        for(int i = 0 ; i < rowCount ; i ++){
        	Object cmangid = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"cmangid");
        	if(cmangid != null && cmangid.toString().trim().length() > 0){
        		invCount ++;
        	}
        }
		for (int i = 0; i < vos.length; i++) {
			vos[i].setAttributeValue("crowno", null);
			vos[i].setAttributeValue("caskbill_bid", null);
			vos[i].setAttributeValue("caskbill_b1id", null);
			vos[i].setAttributeValue("caskbill_bb1id", null);
			vos[i].setAttributeValue("ts", null);
			if(vos[i].getAttributeValue("specialnum") != null && vos[i].getAttributeValue("specialnum").toString().trim().length() > 0){
			  vos[i].setAttributeValue("specialnum",vos[i].getAttributeValue("specialnum").toString().substring(0, 20)+ new Integer(++invCount));
			}
		}
	}

	/**
	 * ���ߣ���ӡ�� ���ܣ���ճ�� �������� ���أ��� ���⣺�� ���ڣ�(2001-5-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 2002-09-20 wyf �ָ��ԣ��ϲ㣩��ԴID����Դ��ID�ĸ��� 2002-11-25 wyf ��TS���� 2003-02-27 wyf
	 * �޸�Ϊ�������и��Ƶ��н�����Ŀ��ռ��༭�Կ���
	 */
//	private void setValueToPastedLines(int iBeginRow, int iEndRow) {
//
//		for (int iRow = iBeginRow; iRow <= iEndRow; iRow++) {
//			// ��������ȱʡֵ
//			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(null,
//					iRow, "caskbill_bid");
//			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(null,
//					iRow, "caskbill_b1id");
//			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(null,
//					iRow, "caskbill_bb1id");
//			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(null,
//					iRow, "ts");
//		}
//
//	}

	/**
	 * ���ܣ�������ѯ ������(2002-10-31 19:45:39) �޸ģ�2003-04-21/czp/ͳһ�����۶Ի���
	 * ����״̬:��ʼ����ѯ�۵������ѯ�۵��޸ģ�ѯ�۵�������ѯ�۵��б�
	 */
	private void onQueryInvOnHand() {
		AskbillMergeVO voPara = null;
		AskbillItemMergeVO item = null;
		AskbillItemMergeVO[] items = null;
		/* ��Ƭ */
		if ((!((BillManageUI) getBillUI()).isListPanelSelected())
				&& (getBillUI().getBillOperate() == IBillOperate.OP_NOTEDIT
						|| getBillUI().getBillOperate() == IBillOperate.OP_EDIT || getBillUI()
						.getBillOperate() == IBillOperate.OP_ADD)) {

			voPara = (AskbillMergeVO) getBillCardPanelWrapper()
					.getBillCardPanel().getBillValueVO(
							AskbillMergeVO.class.getName(),
							AskbillHeaderVO.class.getName(),
							AskbillItemMergeVO.class.getName());
			if (voPara == null) {
				getBillUI().showHintMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
								"UPP40040701-000082")/* @res "δѡȡ����,���ܲ�ѯ����" */);
				return;
			}
			/* ������Ϣ�����Լ�� */
			int[] iSelRows = getBillCardPanelWrapper().getBillCardPanel()
					.getBillTable().getSelectedRows();
			if (iSelRows != null && iSelRows.length > 0) {
				/* �õ��û�ѡȡ�ĵ�һ�� */
				item = (AskbillItemMergeVO) getBillCardPanelWrapper()
						.getBillCardPanel().getBillModel()
						.getBodyValueRowVO(iSelRows[0],
								AskbillItemMergeVO.class.getName());
			} else {
				/* �û�δѡ��ʱ��ȡ���ݵ�һ�� */
				items = (AskbillItemMergeVO[]) getBillCardPanelWrapper()
						.getBillCardPanel().getBillModel().getBodyValueVOs(
								AskbillItemMergeVO.class.getName());
				if (items == null || items.length <= 0) {
					getBillUI()
							.showHintMessage(
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("40040701",
													"UPP40040701-000083")/*
																			 * @res
																			 * "��˾��������Ƶ�������Ϣ������,���ܲ�ѯ����"
																			 */);
					return;
				}
				item = items[0];
			}
			/* �ƻ�ִ������=�Ƶ����� */
			item.setDaskdate((UFDate) voPara.getParentVO().getAttributeValue(
					"daskdate"));
			/* ��Ϣ�����Լ�� */
			if (voPara.getParentVO().getAttributeValue("pk_corp") == null
					|| "".equals(voPara.getParentVO().getAttributeValue("pk_corp")
							.toString().trim())
					|| item.getAttributeValue("cinventorycode") == null
					|| "".equals(item.getAttributeValue("cinventorycode").toString()
							.trim())
					|| item.getAttributeValue("daskdate") == null
					|| ""
							.equals(item.getAttributeValue("daskdate").toString().trim())) {
				getBillUI()
						.showHintMessage(
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"40040701", "UPP40040701-000083")/*
																			 * @res
																			 * "��˾��������Ƶ�������Ϣ������,���ܲ�ѯ����"
																			 */);
				return;
			}
			/* �����VO��ʼ�������ô�����ѯ�Ի��� */
			voPara.setChildrenVO(new AskbillItemMergeVO[] { item });
			if (saPkCorp == null) {
				try {
					nc.vo.bd.CorpVO[] vos = nc.ui.sm.user.UserBO_Client
							.queryAllCorpsByUserPK(getUserId());
					if (vos == null || vos.length == 0) {
						SCMEnv.out("δ��ѯ����Ȩ�޹�˾��ֱ�ӷ���!");
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
									getBillUI(),
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("40040101",
													"UPP40040101-000075")/*
																			 * @res
																			 * "��ȡ��Ȩ�޹�˾�쳣"
																			 */,
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("40040101",
													"UPP40040101-000076")/*
																			 * @res
																			 * "��ȡ��Ȩ�޹�˾ʱ�����쳣(��ϸ��Ϣ�μ�����̨��־)!"
																			 */);
					SCMEnv.out(e.getMessage());
					return;
				}
			}
			getAtpDlg().setPkCorps(saPkCorp);
			getAtpDlg().initData(voPara);
			getAtpDlg().showModal();
		}
		/* �б� */
		else if (((BillManageUI) getBillUI()).isListPanelSelected()) {
			/* ��ͷ��Ϣ�����Լ�� */
			// ȡģ������ǰ�Ĵ���
			AskbillHeaderVO head = null;
			((BillManageUI) getBillUI()).getBillListWrapper()
					.getBillListPanel().getHeadItem("ibillstatus")
					.setWithIndex(false);
			if (((BillManageUI) getBillUI()).getBillListWrapper()
					.getBillListPanel().getHeadBillModel().getBodySelectedVOs(
							AskbillHeaderVO.class.getName()) == null
					|| ((BillManageUI) getBillUI())
							.getBillListWrapper()
							.getBillListPanel()
							.getHeadBillModel()
							.getBodySelectedVOs(AskbillHeaderVO.class.getName()).length <= 0) {
				getBillUI().showHintMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
								"UPP40040701-000082")/* @res "δѡȡ����,���ܲ�ѯ����" */);
				return;
			}
			head = (AskbillHeaderVO) ((BillManageUI) getBillUI())
					.getBillListWrapper().getBillListPanel().getHeadBillModel()
					.getBodySelectedVOs(AskbillHeaderVO.class.getName())[0];
			((BillManageUI) getBillUI()).getBillListWrapper()
					.getBillListPanel().getHeadItem("ibillstatus")
					.setWithIndex(true);
			if (head == null
					|| head.getAttributeValue("pk_corp") == null
					|| ""
							.equals(head.getAttributeValue("pk_corp").toString().trim())) {
				getBillUI().showHintMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
								"UPP40040701-000084")/* @res "δ��ȷ��˾,���ܲ�ѯ����" */);
				return;
			}
			/* ������Ϣ�����Լ�� */
			int[] iSelRows = ((BillManageUI) getBillUI()).getBillListWrapper()
					.getBillListPanel().getBodyTable().getSelectedRows();
			if (iSelRows != null && iSelRows.length > 0) {
				/* �õ��û�ѡȡ�ĵ�һ�� */
				item = (AskbillItemMergeVO) ((BillManageUI) getBillUI())
						.getBillListWrapper().getBillListPanel()
						.getBodyBillModel().getBodyValueRowVO(iSelRows[0],
								AskbillItemMergeVO.class.getName());
			} else {
				/* �û�δѡ��ʱ��ȡ���ݵ�һ�� */
				items = (AskbillItemMergeVO[]) ((BillManageUI) getBillUI())
						.getBillListWrapper().getBillListPanel()
						.getBodyBillModel().getBodyValueVOs(
								AskbillItemMergeVO.class.getName());
				if (items == null || items.length <= 0) {
					getBillUI()
							.showHintMessage(
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("40040701",
													"UPP40040701-000083")/*
																			 * @res
																			 * "��˾��������Ƶ�������Ϣ������,���ܲ�ѯ����"
																			 */);
					return;
				}
				item = items[0];
			}
			/* �ƻ�ִ������=�Ƶ����� */
			item.setDaskdate(head.getDaskdate());
			/* ��Ϣ�����Լ�� */
			if (item.getAttributeValue("cinventoryid") == null
					|| ""
							.equals(item.getAttributeValue("cinventoryid").toString().trim())
					|| item.getAttributeValue("daskdate") == null
					|| ""
							.equals(item.getAttributeValue("daskdate").toString().trim())) {
				getBillUI()
						.showHintMessage(
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"40040701", "UPP40040701-000083")/*
																			 * @res
																			 * "��˾��������Ƶ�������Ϣ������,���ܲ�ѯ����"
																			 */);
				return;
			}
			/* �����VO��ʼ�������ô�����ѯ�Ի��� */
			voPara = new AskbillMergeVO();
			voPara.setParentVO(head);
			voPara.setChildrenVO(new AskbillItemMergeVO[] { item });
			if (saPkCorp == null) {
				try {
					nc.vo.bd.CorpVO[] vos = nc.ui.sm.user.UserBO_Client
							.queryAllCorpsByUserPK(getUserId());
					if (vos == null || vos.length == 0) {
						SCMEnv.out("δ��ѯ����Ȩ�޹�˾��ֱ�ӷ���!");
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
									getBillUI(),
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("40040101",
													"UPP40040101-000075")/*
																			 * @res
																			 * "��ȡ��Ȩ�޹�˾�쳣"
																			 */,
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("40040101",
													"UPP40040101-000076")/*
																			 * @res
																			 * "��ȡ��Ȩ�޹�˾ʱ�����쳣(��ϸ��Ϣ�μ�����̨��־)!"
																			 */);
					SCMEnv.out(e.getMessage());
					return;
				}
			}
			getAtpDlg().setPkCorps(saPkCorp);
			getAtpDlg().initData(voPara);
			getAtpDlg().showModal();
		}
	}

	/**
	 * ���� ���Ĺܹ��� ���õ���״̬����ѯ�۵����������ѯ�۵��б�
	 */
	private void onDocument() {
		String[] strPks = null;
		String[] strCodes = null;
		if (!(((!((BillManageUI) getBillUI()).isListPanelSelected()) && (getBillUI()
				.getBillOperate() == IBillOperate.OP_NOTEDIT)) || ((((BillManageUI) getBillUI())
				.isListPanelSelected())))) {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
							"UPP40040701-000069")/* @res "��ȡ�������ݺ�,�ĵ������ܲ�����" */);
			return;
		}
		// ��Ƭ
		if (((!((BillManageUI) getBillUI()).isListPanelSelected()) && (getBillUI()
				.getBillOperate() == IBillOperate.OP_NOTEDIT))) {
			if (getBufferData().getVOBufferSize() > 0
					&& getBufferData().getCurrentVO() != null
					&& getBufferData().getCurrentVO().getParentVO() != null) {
				strPks = new String[] { (String) getBufferData().getCurrentVO()
						.getParentVO().getAttributeValue("caskbillid") };
				strCodes = new String[] { (String) getBufferData()
						.getCurrentVO().getParentVO().getAttributeValue(
								"vaskbillcode") };
			}
		}
		// �б�
		if (((BillManageUI) getBillUI()).isListPanelSelected()) {
			if (getBufferData().getVOBufferSize() > 0) {
				int[] iaRows = ((BillManageUI) getBillUI())
						.getBillListWrapper().getBillListPanel().getHeadTable()
						.getSelectedRows();
				if (iaRows == null || iaRows.length <= 0)
					return;
				strPks = new String[iaRows.length];
				strCodes = new String[iaRows.length];
				for (int i = 0; i < iaRows.length; i++) {
					strPks[i] = (String) ((BillManageUI) getBillUI())
							.getBillListWrapper().getBillListPanel()
							.getHeadBillModel().getValueAt(iaRows[i],
									"caskbillid");
					strCodes[i] = (String) ((BillManageUI) getBillUI())
							.getBillListWrapper().getBillListPanel()
							.getHeadBillModel().getValueAt(iaRows[i],
									"vaskbillcode");
				}
			}
		}
		if (strPks == null || strPks.length <= 0)
			return;
		// �����ĵ�����Ի���
		nc.ui.scm.file.DocumentManager.showDM((BillManageUI) getBillUI(),
				ScmConst.PO_AskBill,strPks);
	}

	/**
	 * ���ܣ���ȡ������ѯ�Ի���
	 * 
	 * @return nc.ui.so.so033.ATPForOneInvUI
	 */
	private nc.ui.pu.pub.ATPForOneInvMulCorpUI getAtpDlg() {
		if (null == m_atpDlg) {
			m_atpDlg = new nc.ui.pu.pub.ATPForOneInvMulCorpUI(
					(BillManageUI) getBillUI());
		}
		return m_atpDlg;
	}

	/**
	 * ������ť�������¼���������(�����)
	 */
	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {
		case IAskAndQuote.VendorSelect:
			onVendorSelect();
			break;
		case IAskAndQuote.FromPrayBill:
			onGenFromPrayBill();
			break;
		case IAskAndQuote.SEND:
			onSend();
			break;
		case IAskAndQuote.RECEIVE:
			onReceive();
			break;
		case IAskAndQuote.DOCMANGER:
			onDocument();
			break;
		case IAskAndQuote.DOCMANGERLIST:
			onDocument();
			break;	
		case IAskAndQuote.STOCKQUY:
			onQueryInvOnHand();
			break;
		case IAskAndQuote.STOCKQUYLIST:
			onQueryInvOnHand();
			break;	
		case IAskAndQuote.PRINT:
			onPrintCard();
			break;
		case IAskAndQuote.PRINTPRE:
			onPrintPreviewCard();
			break;
		case IAskAndQuote.PRINTLIST:
			onPrintList();
			break;
		case IAskAndQuote.PRINTPRELIST:
			onPrintPreviewList();
			break;
		case IAskAndQuote.LISTPRINT:
			onPrintList();
			break;
		case IAskAndQuote.LISTPRINTPRE:
			onPrintPreviewList();
			break;
		case IAskAndQuote.LISTCANCELTRANSFORM:
			onListCancelTransform();
			break;
		case IAskAndQuote.BILLTOEXCELDEFAULT:
			onBillExcel(0);
			break;
		case IAskAndQuote.BILLTOEXCELDEF:
			onBillExcel(1);
			break;
		case IAskAndQuote.EXCELTOBILL:
			onExcelBill();
			break;
		case IAskAndQuote.PRINTBYVENDOR:
			onPrintByVendor();
			break;
		case IAskAndQuote.EDITLIST:
			onBoEdit();
			break;
		case IAskAndQuote.DELLIST:
			onBoDel();
			break;
		case IAskAndQuote.QueryList:
			onBoQuery();
			break;
		case IAskAndQuote.RefeshList:
			onBoRefresh();
			break;	
		case IAskAndQuote.PreAndAfterBillQuery:
			onLnkQuery();
			break;
		case IAskAndQuote.CARDLINEPasteToTail:
			onBoLinePasteToTail();
			break;
		case IAskAndQuote.MailMaintain:
		  onBoMailMaintain();
		default: {
			// SCMEnv.out("���ȼ���δ����");
			break;
		}

		}
	}
	/**
	 * ����������
	 */
	private void onLnkQuery() {
		AskbillMergeVO vo = null;
		try {
			vo = (AskbillMergeVO)getBillUI().getVOFromUI();
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PuTool.outException(e) ;
		}
		if (vo == null || vo.getParentVO() == null)
			return;
		nc.ui.scm.sourcebill.SourceBillFlowDlg soureDlg = new nc.ui.scm.sourcebill.SourceBillFlowDlg(
				(AskAndQuoteUI)getBillUI(), nc.vo.scm.pu.BillTypeConst.PO_ASK,
				((AskbillHeaderVO) vo.getParentVO()).getPrimaryKey(), null,
				getUserId(), getCorpId());
		soureDlg.showModal();
	}
	/**
	 * ѡ��Ӧ��
	 * 
	 * @throws Exception
	 */
	private void onVendorSelect() throws Exception {
		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		AskbillHeaderVO headVO = (AskbillHeaderVO) billVO.getParentVO();
		//���ñ�ע
		if( getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane ){
			UIRefPane nRefPanel = (UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmemo").getComponent();
			UITextField vMemoField = nRefPanel.getUITextField();
			headVO.setVmemo(vMemoField.getText());
		}
		AskbillItemMergeVO[] itemMergeVOs = (AskbillItemMergeVO[]) billVO
				.getChildrenVO();
		AskbillItemVO[] itemVOs = null;
		AskbillItemVO itemVO = new AskbillItemVO();
		AskbillItemMergeVO itemMergeVO = new AskbillItemMergeVO();
		AskbillItemMergeVO itemMergeVOTemp = new AskbillItemMergeVO();
		Vector v = new Vector();
		Vector retV = new Vector();
		Hashtable pricedVendorH = new Hashtable();
		Hashtable VendorNoPriceH = new Hashtable();
		Hashtable itemH = new Hashtable();
		String cmangid = null;
		String vmangid = null;
		Hashtable vmangidH = new Hashtable();
		Hashtable vendorH = new Hashtable();
		Hashtable vendorHAll = new Hashtable();
		Hashtable vendorTempH = new Hashtable();
		Hashtable vendorTempHAll = new Hashtable();
		UFDouble nquoteprice = null;
		UFDouble nquotetaxprice = null;
		String specialNum = null;
		// �ǿ�����
		String ibillStatusTemp = headVO.getIbillstatus();
		if (ibillStatusTemp.equals(StatusConvert
				.getStatusIndexNum().get(new Integer(IAskBillStatus.FREE))
				.toString())) {
			// ��ͷ�ǿ�����
			String pk_purorg = headVO.getPk_purorg();
			String ccurrencytypeid = headVO.getCcurrencytypeid();
			/* ���ɹ���֯ */
			if (pk_purorg == null
					|| (pk_purorg != null && pk_purorg.trim()
							.length() == 0)) {
				MessageDialog
						.showWarningDlg(
								(AskAndQuoteUI) getBillUI(),
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"4004070101", "UPP4004070101-000069")/*
																				 * @res
																				 * "����ģ��ǿ�����"
																				 */,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"4004070101", "UPP4004070101-000072")/*
																				 * @res
																				 * "��ͷ:�ɹ���֯����Ϊ��"
																				 */);
				((BillManageUI) getBillUI()).showHintMessage(nc.ui.ml.NCLangRes
						.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000010")/* @res "����ʧ��" */);
				return;
			}
			/* ������ */
			if (ccurrencytypeid == null
					|| (ccurrencytypeid != null && ccurrencytypeid
							.trim().length() == 0)) {
				MessageDialog
						.showWarningDlg(
								(AskAndQuoteUI) getBillUI(),
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"4004070101", "UPP4004070101-000069")/*
																				 * @res
																				 * "����ģ��ǿ�����"
																				 */,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"4004070101", "UPP4004070101-000073")/*
																				 * @res
																				 * "��ͷ:���ֲ���Ϊ��"
																				 */);
				((BillManageUI) getBillUI()).showHintMessage(nc.ui.ml.NCLangRes
						.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000010")/* @res "����ʧ��" */);
				return;
			}
			// ����ǿ�����
			int iCount = getBillCardPanelWrapper().getBillCardPanel()
					.getBillTable().getRowCount();
			Object cmangidTemp = null;
//			Object nasknumTemp = null;
			boolean isInvNull = false;
			for (int i = 0; i < iCount; i++) {
				cmangidTemp = getBillCardPanelWrapper().getBillCardPanel()
						.getBodyValueAt(i, "cmangid");
//				nasknumTemp = getBillCardPanelWrapper().getBillCardPanel()
//						.getBodyValueAt(i, "nasknum");
				if (cmangidTemp != null
						&& cmangidTemp.toString().trim().length() > 0) {
					isInvNull = true;
					// �������Ƿǿ��� since 5.6 modify by donggq
//					if (nasknumTemp == null
//							|| (nasknumTemp != null && nasknumTemp.toString()
//									.trim().length() == 0)) {
//						MessageDialog.showWarningDlg(getBillUI(),
//								nc.ui.ml.NCLangRes.getInstance().getStrByID(
//										"SCMCOMMON", "UPPSCMCommon-000270")/*
//																			 * @res
//																			 * "��ʾ"
//																			 */,
//																			 nc.ui.ml.NCLangRes.getInstance()
//																				.getStrByID("4004070101",
//																						"UPP4004070101-000095", null,
//																						new String[] { (i + 1) + "",nc.ui.ml.NCLangRes.getInstance().getStrByID(
//																								"4004070101", "UPP4004070101-000090") }))/*
//																														 * @res
//																														 * "{0}�У���������Ϊ�գ�"
//																														 */;
//						return;
//					}
				}
			}
			if(!isInvNull){
				MessageDialog.showWarningDlg(getBillUI(),
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000270")/*
																	 * @res
																	 * "��ʾ"
																	 */,nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"4004070101", "UPP4004070101-000096")/*
																				 * @res
																				 * "����Ӧ������һ�д���������޷�ѡ��Ӧ��!"
																				 */);
				return;
			}
		}
		if (m_itemVOs == null
				|| getBillUI().getBillOperate() == IBillOperate.OP_NOTEDIT) {
			// �ӽ���ȡ���ӱ�����
			for (int i = 0; i < itemMergeVOs.length; i++) {
				itemMergeVO = itemMergeVOs[i];
				cmangid = itemMergeVO.getCmangid();
				specialNum = itemMergeVO.getSpecialNum();
				if (specialNum != null
						&& specialNum.trim().length() > 0
						&& itemMergeVO != null
						&& !vendorTempH.containsKey(specialNum)) {
					vendorTempH.put(specialNum, itemMergeVO);
				}
				if (specialNum != null
						&& specialNum.trim().length() > 0
						&& cmangid != null && cmangid.trim().length() > 0
						&& itemMergeVO != null && !itemH.containsKey(specialNum)) {
					itemVO = new AskbillItemVO();
					itemVO.setCaskbill_bid(itemMergeVO.getCaskbill_bid());
					itemVO.setCaskbillid(itemMergeVO.getCaskbillid());
					itemVO.setCbaseid(itemMergeVO.getCbaseid());
					itemVO.setCmangid(itemMergeVO.getCmangid());
					itemVO.setCrowno(itemMergeVO.getCrowno());
					itemVO.setNasknum(itemMergeVO.getNasknum());
//					itemVO.setNtaxrate(itemMergeVO.getNtaxrate() == null ? PuTool.getInvTaxRate(itemMergeVO.getCbaseid().toString()):itemMergeVO.getNtaxrate());
					itemVO.setVfree1(itemMergeVO.getVfree1());
					itemVO.setVfree2(itemMergeVO.getVfree2());
					itemVO.setVfree3(itemMergeVO.getVfree3());
					itemVO.setVfree4(itemMergeVO.getVfree4());
					itemVO.setVfree5(itemMergeVO.getVfree5());
					itemVO.setDreceivedate(itemMergeVO.getDreceivedate());
					itemVO.setCsourcebillid(itemMergeVO.getCsourcebillid());
					itemVO.setCsourcebillrowid(itemMergeVO
							.getCsourcebillrowid());
					itemVO.setCsourcebilltype(itemMergeVO.getCsourcebilltype());
					itemVO.setCupsourcebillid(itemMergeVO.getCupsourcebillid());
					itemVO.setCupsourcebillrowid(itemMergeVO
							.getCupsourcebillrowid());
					itemVO.setCupsourcebilltype(itemMergeVO
							.getCupsourcebilltype());
					itemVO.setPk_srccorp(itemMergeVO.getPk_srccorp());
					itemVO.setUpsrccorp(itemMergeVO.getPk_upsrccorp());
					if ((itemMergeVO.getSpecialNum() != null && itemMergeVO
							.getSpecialNum().trim().length() == 0)
							|| (itemMergeVO.getSpecialNum() == null)) {
						itemVO.setSpecialNum(itemVO.getCmangid()
								+ itemVO.getCrowno());
					} else {
						itemVO.setSpecialNum(itemMergeVO.getSpecialNum());
					}
					itemVO.setVmemo(itemMergeVO.getVmemo());
					itemVO.setVdef1(itemMergeVO.getVdef1());
					itemVO.setVdef2(itemMergeVO.getVdef2());
					itemVO.setVdef3(itemMergeVO.getVdef3());
					itemVO.setVdef4(itemMergeVO.getVdef4());
					itemVO.setVdef5(itemMergeVO.getVdef5());
					itemVO.setVdef6(itemMergeVO.getVdef6());
					itemVO.setVdef7(itemMergeVO.getVdef7());
					itemVO.setVdef8(itemMergeVO.getVdef8());
					itemVO.setVdef9(itemMergeVO.getVdef9());
					itemVO.setVdef10(itemMergeVO.getVdef10());
					itemVO.setVdef11(itemMergeVO.getVdef11());
					itemVO.setVdef12(itemMergeVO.getVdef12());
					itemVO.setVdef13(itemMergeVO.getVdef13());
					itemVO.setVdef14(itemMergeVO.getVdef14());
					itemVO.setVdef15(itemMergeVO.getVdef15());
					itemVO.setVdef16(itemMergeVO.getVdef16());
					itemVO.setVdef17(itemMergeVO.getVdef17());
					itemVO.setVdef18(itemMergeVO.getVdef18());
					itemVO.setVdef19(itemMergeVO.getVdef19());
					itemVO.setVdef20(itemMergeVO.getVdef20());
					
					itemVO.setPk_defdoc1(itemMergeVO.getPk_defdoc1());
					itemVO.setPk_defdoc2(itemMergeVO.getPk_defdoc2());
					itemVO.setPk_defdoc3(itemMergeVO.getPk_defdoc3());
					itemVO.setPk_defdoc4(itemMergeVO.getPk_defdoc4());
					itemVO.setPk_defdoc5(itemMergeVO.getPk_defdoc5());
					itemVO.setPk_defdoc6(itemMergeVO.getPk_defdoc6());
					itemVO.setPk_defdoc7(itemMergeVO.getPk_defdoc7());
					itemVO.setPk_defdoc8(itemMergeVO.getPk_defdoc8());
					itemVO.setPk_defdoc9(itemMergeVO.getPk_defdoc9());
					itemVO.setPk_defdoc10(itemMergeVO.getPk_defdoc10());
					itemVO.setPk_defdoc11(itemMergeVO.getPk_defdoc11());
					itemVO.setPk_defdoc12(itemMergeVO.getPk_defdoc12());
					itemVO.setPk_defdoc13(itemMergeVO.getPk_defdoc13());
					itemVO.setPk_defdoc14(itemMergeVO.getPk_defdoc14());
					itemVO.setPk_defdoc15(itemMergeVO.getPk_defdoc15());
					itemVO.setPk_defdoc16(itemMergeVO.getPk_defdoc16());
					itemVO.setPk_defdoc17(itemMergeVO.getPk_defdoc17());
					itemVO.setPk_defdoc18(itemMergeVO.getPk_defdoc18());
					itemVO.setPk_defdoc19(itemMergeVO.getPk_defdoc19());
					itemVO.setPk_defdoc20(itemMergeVO.getPk_defdoc20());

					v.add(itemVO);
					itemH.put(specialNum, specialNum);
				}

			}
			if (v.size() > 0) {
				itemVOs = new AskbillItemVO[v.size()];
				v.copyInto(itemVOs);
				m_itemVOs = itemVOs;
			}
		}
		// ȡ���Ѿ�����/�����Ĺ�Ӧ��
		Vector vendorSelected = new Vector();
		for (int i = 0; i < itemMergeVOs.length; i++) {
			itemMergeVO = itemMergeVOs[i];
			vmangid = itemMergeVO.getCvendormangid();
			nquoteprice = itemMergeVO.getNquoteprice();
			nquotetaxprice = itemMergeVO.getNquotetaxprice();
			specialNum = itemMergeVO.getSpecialNum();
			if (itemMergeVO != null
					&& (nquoteprice != null || nquotetaxprice != null)) {// �б���
				if (specialNum != null && vmangid != null
						&& vmangid.trim() != null) {// ����ʱ
					vendorTempH = (Hashtable) vendorH.get(specialNum);
					if (vendorTempH == null) {
						vendorTempH = new Hashtable();
					}
					vendorTempH.put(vmangid, itemMergeVO);
					vendorH.put(specialNum, vendorTempH);
				}
				if (vmangid != null && vmangid.trim() != null
						&& !vmangidH.containsKey(vmangid)) {
					vmangidH.put(vmangid, vmangid);
					pricedVendorH.put(vmangid, vmangid);
					vendorSelected.add(vmangid);
				}
			} else if (itemMergeVO != null && nquoteprice == null
					&& nquotetaxprice == null) {// �ޱ���

				if (specialNum != null && vmangid != null
						&& vmangid.trim() != null) {// ����ʱ
					vendorTempHAll = (Hashtable) vendorHAll.get(specialNum);
					if (vendorTempHAll == null) {
						vendorTempHAll = new Hashtable();
					}
					vendorTempHAll.put(vmangid, itemMergeVO);
					vendorHAll.put(specialNum, vendorTempHAll);
				}

				if (vmangid != null && vmangid.trim() != null
						&& VendorNoPriceH != null && VendorNoPriceH.size() >= 0
						&& !VendorNoPriceH.containsKey(vmangid)) {
					VendorNoPriceH.put(vmangid, vmangid);
					vendorSelected.add(vmangid);
				}
			}
		}

		// ��ʾѡ��Ӧ�̽���
		String[] vmangIDs = null;
		if(vendorSelected.size() > 0){
			vmangIDs = new String[vendorSelected.size()];
			vendorSelected.copyInto(vmangIDs);
		}
        //��ʾ��������Ϊ��ʾ��Ӧ�̲��� V51����������Ŀ�����޸�
		getVendorRef();
		refpaneVendor.getRefModel().setUserParameter(getCorpId());
		
		if(vmangIDs != null && vmangIDs.length > 0){
		 refpaneVendor.setPKs(vmangIDs);
		}else{
	     refpaneVendor.setPKs(null);	
		}
   	    refpaneVendor.onButtonClicked();
   	    
   	//����ȷ��ֱ�ӷ���
   	if(refpaneVendor.getReturnButtonCode() != UFRefManage.ID_OK)
   		return;
		
		VendorVO[] vendorVOs = null;
		VendorVO vendorVO = new VendorVO();
		int vendorVOsLength = 0;
		//���״̬������������ ֱ�ӷ���
		if (ibillStatusTemp.equals(StatusConvert
				.getStatusIndexNum().get(new Integer(IAskBillStatus.CONFIRM))
				.toString())) {
			return;
		}
		
		// ȡ��ѡ��Ӧ�̽������
		if (!refpaneVendor.isShowing()) {
			String[] saMangIds = refpaneVendor.getRefPKs();
			if(saMangIds != null && saMangIds.length > 0){
			BddataVO[] saBaseIds = refpaneVendor.getRefBDdats();
			vendorVOs = new VendorVO[saMangIds.length];
			for(int i = 0 ; i < vendorVOs.length ; i ++){
				vendorVOs[i] = new VendorVO();
			}
			for(int i = 0 ; i < saMangIds.length ; i ++){
				vendorVOs[i].setCubasdoc(saBaseIds[i].getBasedocPK());
				vendorVOs[i].setCumandoc(saMangIds[i]);
				vendorVOs[i].setCustname(saBaseIds[i].getName());
				vendorVOs[i].setCustcode(saBaseIds[i].getCode());
			}
			if (vendorVOs != null && vendorVOs.length > 0) {
				vendorVOsLength = vendorVOs.length;
				m_vendorVOs = vendorVOs;
				m_vendorVOsForSend = vendorVOs;
			} else {
				m_vendorVOs = null;
				m_vendorVOsForSend = null;
			}
		  }else {
				m_vendorVOs = null;
				m_vendorVOsForSend = null;
			}
		} else {
			m_vendorVOs = null;
			m_vendorVOsForSend = null;
			return;
		}
		String ibillStatus = null;
		if (m_vendorVOs != null && m_vendorVOs.length > 0) {// ѡ���˹�Ӧ��

			if (pricedVendorH != null && pricedVendorH.size() > 0) {// �Ѿ��б�����Ϣ
				ibillStatus = StatusConvert.getStatusIndexNum()
						.get(new Integer(IAskBillStatus.QUOTED)).toString();
				((AskbillHeaderVO) billVO.getParentVO())
						.setIbillstatus(ibillStatus);
			}
			else {// û�б�����Ϣ
				ibillStatus = StatusConvert.getStatusIndexNum()
						.get(new Integer(IAskBillStatus.SENDING)).toString();
				((AskbillHeaderVO) billVO.getParentVO())
						.setIbillstatus(ibillStatus);
			}
		} else {// δѡ���˹�Ӧ��
			ibillStatus = StatusConvert.getStatusIndexNum()
					.get(new Integer(IAskBillStatus.FREE)).toString();
			((AskbillHeaderVO) billVO.getParentVO())
					.setIbillstatus(ibillStatus);
		}

		
		// ����ӱ�����ӱ������
		int[] iaGivenSetRow = null;
		int lastRowNum = 0;
		if (m_itemVOs != null && m_itemVOs.length > 0) {

			for (int i = 0; i < m_itemVOs.length; i++) {
				itemVO = m_itemVOs[i];
				specialNum = itemVO.getSpecialNum();
				if (m_vendorVOs != null && m_vendorVOs.length > 0) {// �й�Ӧ������
					for (int j = 0; j < m_vendorVOs.length; j++) {
						vendorVO = m_vendorVOs[j];
						vmangid = vendorVO.getCumandoc();
						itemMergeVO = new AskbillItemMergeVO();
						itemMergeVO.setCaskbill_bid(itemVO.getCaskbill_bid());
						itemMergeVO.setCaskbillid(itemVO.getCaskbillid());
						if (j == 0) {// ��Ӧÿ������ĵ�һ�����õ���Ϣ
							itemMergeVO.setCbaseid(itemVO.getCbaseid());
							itemMergeVO.setCmangid(itemVO.getCmangid());
							if (itemVO.getCrowno() != null
									&& itemVO.getCrowno().trim()
											.length() > 0) {
								itemMergeVO.setCrowno(itemVO.getCrowno());
								lastRowNum = i;
							}
							
							itemMergeVO.setNasknum(itemVO.getNasknum());
							itemMergeVO.setDreceivedate(itemVO
									.getDreceivedate());
							itemMergeVO.setVfree1(itemVO.getVfree1());
							itemMergeVO.setVfree2(itemVO.getVfree2());
							itemMergeVO.setVfree3(itemVO.getVfree3());
							itemMergeVO.setVfree4(itemVO.getVfree4());
							itemMergeVO.setVfree5(itemVO.getVfree5());
							

						}
						//since 5.3 ˰�ʡ��Զ������������ӱ� 
//						itemMergeVO.setNtaxrate(itemVO.getNtaxrate());
			            itemMergeVO.setVdef1(itemVO.getVdef1());
			            itemMergeVO.setVdef2(itemVO.getVdef2());
			            itemMergeVO.setVdef3(itemVO.getVdef3());
			            itemMergeVO.setVdef4(itemVO.getVdef4());
			            itemMergeVO.setVdef5(itemVO.getVdef5());
			            itemMergeVO.setVdef6(itemVO.getVdef6());
			            itemMergeVO.setVdef7(itemVO.getVdef7());
			            itemMergeVO.setVdef8(itemVO.getVdef8());
			            itemMergeVO.setVdef9(itemVO.getVdef9());
			            itemMergeVO.setVdef10(itemVO.getVdef10());
			            itemMergeVO.setVdef11(itemVO.getVdef11());
			            itemMergeVO.setVdef12(itemVO.getVdef12());
			            itemMergeVO.setVdef13(itemVO.getVdef13());
			            itemMergeVO.setVdef14(itemVO.getVdef14());
			            itemMergeVO.setVdef15(itemVO.getVdef15());
			            itemMergeVO.setVdef16(itemVO.getVdef16());
			            itemMergeVO.setVdef17(itemVO.getVdef17());
			            itemMergeVO.setVdef18(itemVO.getVdef18());
			            itemMergeVO.setVdef19(itemVO.getVdef19());
			            itemMergeVO.setVdef20(itemVO.getVdef20());
			            
			            itemMergeVO.setPk_defdoc1(itemVO.getPk_defdoc1());
			            itemMergeVO.setPk_defdoc2(itemVO.getPk_defdoc2());
			            itemMergeVO.setPk_defdoc3(itemVO.getPk_defdoc3());
			            itemMergeVO.setPk_defdoc4(itemVO.getPk_defdoc4());
			            itemMergeVO.setPk_defdoc5(itemVO.getPk_defdoc5());
			            itemMergeVO.setPk_defdoc6(itemVO.getPk_defdoc6());
			            itemMergeVO.setPk_defdoc7(itemVO.getPk_defdoc7());
			            itemMergeVO.setPk_defdoc8(itemVO.getPk_defdoc8());
			            itemMergeVO.setPk_defdoc9(itemVO.getPk_defdoc9());
			            itemMergeVO.setPk_defdoc10(itemVO.getPk_defdoc10());
			            itemMergeVO.setPk_defdoc11(itemVO.getPk_defdoc11());
			            itemMergeVO.setPk_defdoc12(itemVO.getPk_defdoc12());
			            itemMergeVO.setPk_defdoc13(itemVO.getPk_defdoc13());
			            itemMergeVO.setPk_defdoc14(itemVO.getPk_defdoc14());
			            itemMergeVO.setPk_defdoc15(itemVO.getPk_defdoc15());
			            itemMergeVO.setPk_defdoc16(itemVO.getPk_defdoc16());
			            itemMergeVO.setPk_defdoc17(itemVO.getPk_defdoc17());
			            itemMergeVO.setPk_defdoc18(itemVO.getPk_defdoc18());
			            itemMergeVO.setPk_defdoc19(itemVO.getPk_defdoc19());
			            itemMergeVO.setPk_defdoc20(itemVO.getPk_defdoc20());
						itemMergeVO.setPk_srccorp(itemVO.getPk_srccorp());
						itemMergeVO.setPk_upsrccorp(itemVO.getUpsrccorp());
						itemMergeVO.setCsourcebillid(itemVO.getCsourcebillid());
						itemMergeVO.setCsourcebillrowid(itemVO
								.getCsourcebillrowid());
						itemMergeVO.setCsourcebilltype(itemVO
								.getCsourcebilltype());
						itemMergeVO.setCupsourcebillid(itemVO
								.getCupsourcebillid());
						itemMergeVO.setCupsourcebillrowid(itemVO
								.getCupsourcebillrowid());
						itemMergeVO.setCupsourcebilltype(itemVO
								.getCupsourcebilltype());
						itemMergeVO.setVmemo(itemVO.getVmemo());
						if (vmangid != null
								&& vmangid.trim().length() > 0
								&& vmangidH.containsKey(vmangid)) {// �˹�Ӧ���ѱ���
							if (specialNum != null && specialNum.trim() != null) {// ����ʱ
								vendorTempH = (Hashtable) vendorH
										.get(specialNum);
								if (vendorTempH != null) {
									itemMergeVOTemp = (AskbillItemMergeVO) vendorTempH
											.get(vmangid);
								}
								if (itemMergeVOTemp != null) {// �б�������
									itemMergeVO
											.setCvendorbaseid(itemMergeVOTemp
													.getCvendorbaseid());
									itemMergeVO
											.setCvendormangid(itemMergeVOTemp
													.getCvendormangid());
									itemMergeVO.setNquoteprice(itemMergeVOTemp
											.getNquoteprice());
									itemMergeVO
											.setNquotetaxprice(itemMergeVOTemp
													.getNquotetaxprice());
									itemMergeVO
											.setCaskbill_b1id(itemMergeVOTemp
													.getCaskbill_b1id());
									itemMergeVO
											.setCaskbill_bb1id(itemMergeVOTemp
													.getCaskbill_bb1id());
									itemMergeVO.setSpecialNum(itemMergeVOTemp
											.getSpecialNum());
									// ���������Ч����Ϊ�գ�������Ĭ��ֵΪά������ʱ�ĵ�¼����
									if (itemMergeVOTemp.getDvaliddate() == null) {
										itemMergeVO
												.setDvaliddate(((AskAndQuoteUI) getBillUI())
														.getLogDate());
									} else {
										itemMergeVO
												.setDvaliddate(itemMergeVOTemp
														.getDvaliddate());
									}
									itemMergeVO.setDinvaliddate(itemMergeVOTemp
											.getDinvaliddate());
									itemMergeVO.setDeliverdays(itemMergeVOTemp
											.getDeliverdays());
									itemMergeVO.setNtaxrate(itemMergeVOTemp.getNtaxrate());
									itemMergeVO.setCsendtypeid(itemMergeVOTemp.getCsendtypeid());
									itemMergeVO.setCreceiptareaid(itemMergeVOTemp.getCreceiptareaid());
								} else {// �ޱ�������
//									itemMergeVO
//											.setCaskbill_b1id(itemMergeVOTemp
//													.getCaskbill_b1id());
//									itemMergeVO
//											.setCaskbill_bb1id(itemMergeVOTemp
//													.getCaskbill_bb1id());
									itemMergeVO.setCvendorbaseid(vendorVO
											.getCubasdoc());
									itemMergeVO.setCvendormangid(vendorVO
											.getCumandoc());
//									itemMergeVO.setNtaxrate(itemMergeVOTemp.getNtaxrate());
								}
							}
						} else {// �˹�Ӧ��δ����
							vendorTempHAll = (Hashtable) vendorHAll
									.get(specialNum);
							if (vmangid != null
									&& vmangid.trim().length() > 0) {
								if (vendorTempHAll != null
										&& vendorTempHAll.get(vmangid) != null) {
									itemMergeVOTemp = (AskbillItemMergeVO) vendorTempHAll
											.get(vmangid);
									itemMergeVO
											.setCaskbill_b1id(itemMergeVOTemp
													.getCaskbill_b1id());
									itemMergeVO
											.setCaskbill_bb1id(itemMergeVOTemp
													.getCaskbill_bb1id());
								}
							}
							itemMergeVO.setNtaxrate(itemMergeVOTemp.getNtaxrate());
							itemMergeVO
									.setCvendorbaseid(vendorVO.getCubasdoc());
							itemMergeVO
									.setCvendormangid(vendorVO.getCumandoc());
							// ���������Ч����Ϊ�գ�������Ĭ��ֵΪά������ʱ�ĵ�¼����
							itemMergeVO
									.setDvaliddate(((AskAndQuoteUI) getBillUI())
											.getLogDate());
						}
						// �����������,ָΪͷ��ѡ��Ӧ��ʱָ������빩Ӧ�̵�λ����
						itemMergeVO.setSpecialNum(itemVO.getSpecialNum());
						if (itemMergeVO != null) {
							retV.add(itemMergeVO);
						}
					}
				} else {// �޹�Ӧ������
					itemMergeVO = new AskbillItemMergeVO();
					itemMergeVO.setCaskbill_bid(itemVO.getCaskbill_bid());
					itemMergeVO.setCaskbillid(itemVO.getCaskbillid());
					itemMergeVO.setSpecialNum(itemVO.getSpecialNum());
					itemMergeVO.setCbaseid(itemVO.getCbaseid());
					itemMergeVO.setCmangid(itemVO.getCmangid());
					if (itemVO.getCrowno() != null
							&& itemVO.getCrowno().trim().length() > 0) {
						itemMergeVO.setCrowno(itemVO.getCrowno());
						lastRowNum = i;
					}
//					itemMergeVO.setNtaxrate(itemVO.getNtaxrate());
					itemMergeVO.setNasknum(itemVO.getNasknum());
					itemMergeVO.setVfree1(itemVO.getVfree1());
					itemMergeVO.setVfree2(itemVO.getVfree2());
					itemMergeVO.setVfree3(itemVO.getVfree3());
					itemMergeVO.setVfree4(itemVO.getVfree4());
					itemMergeVO.setVfree5(itemVO.getVfree5());
					itemMergeVO.setDreceivedate(itemVO.getDreceivedate());
					itemMergeVO.setCsourcebillid(itemVO.getCsourcebillid());
					itemMergeVO.setCsourcebillrowid(itemVO
							.getCsourcebillrowid());
					itemMergeVO.setCsourcebilltype(itemVO.getCsourcebilltype());
					itemMergeVO.setCupsourcebillid(itemVO.getCupsourcebillid());
					itemMergeVO.setCupsourcebillrowid(itemVO
							.getCupsourcebillrowid());
					itemMergeVO.setCupsourcebilltype(itemVO
							.getCupsourcebilltype());
					itemMergeVO.setPk_srccorp(itemVO.getPk_srccorp());
					itemMergeVO.setPk_upsrccorp(itemVO.getUpsrccorp());
					itemMergeVO.setVmemo(itemVO.getVmemo());
					if (itemMergeVO != null) {
						retV.add(itemMergeVO);
					}

				}
			}

			// �����к�
			int lastRowNumTemp = lastRowNum;
			if (m_vendorVOs != null && m_vendorVOs.length > 0) {// �й�Ӧ������
				if (m_itemVOs.length - (lastRowNumTemp + 1) > 0) {
					iaGivenSetRow = new int[m_itemVOs.length
							- (lastRowNumTemp + 1)];
					for (int i = lastRowNumTemp / m_vendorVOs.length + 1; i < m_itemVOs.length; i++) {
						if(iaGivenSetRow.length == 1){
						  iaGivenSetRow[0] = i * m_vendorVOs.length;
						}else{
						   iaGivenSetRow[i] = i * m_vendorVOs.length;
						}
					}
				}
			} else {
				if (m_itemVOs.length - (lastRowNumTemp + 1) > 0) {
					iaGivenSetRow = new int[m_itemVOs.length
							- (lastRowNumTemp + 1)];
					for (int i = lastRowNumTemp + 1; i < m_itemVOs.length; i++) {
						iaGivenSetRow[i] = i;
					}
				}
			}
		}
		if (retV.size() > 0) {
			itemMergeVOs = new AskbillItemMergeVO[retV.size()];
			retV.copyInto(itemMergeVOs);
		}
		// ����������
		Vector vTemp = new Vector();
	        if(itemMergeVOs != null && itemMergeVOs.length > 0){
	            for(int j = 0; j < itemMergeVOs.length; j++){
	                if(itemMergeVOs[j].getVfree1() != null || itemMergeVOs[j].getVfree2() != null || 
	                		itemMergeVOs[j].getVfree3() != null || itemMergeVOs[j].getVfree4() != null ||
	                		itemMergeVOs[j].getVfree5() != null) {
	                	vTemp.addElement(itemMergeVOs[j]);}
	                }
	            }
	    if(vTemp.size() > 0){
	    	AskbillItemMergeVO[] askItems = new AskbillItemMergeVO[vTemp.size()];
	        vTemp.copyInto(askItems);
	        new nc.ui.scm.pub.FreeVOParse().setFreeVO(askItems, "vfree","vfree","cbaseid","cmangid",true);
	    }
		
		if (iaGivenSetRow != null && iaGivenSetRow.length > 0) {
			BillRowNo.addLineRowNos(getBillCardPanelWrapper()
					.getBillCardPanel(), "29", "crowno", lastRowNum,
					iaGivenSetRow);
		}
		// ����
		billVO.setChildrenVO(itemMergeVOs);
		getBillCardPanelWrapper().getBillCardPanel().setBillValueVO(billVO);
		/* ��ʾ��ע */
		if(billVO != null && getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane){
			UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmemo").getComponent();
			nRefPanel3.setValue(((AskbillHeaderVO)billVO.getParentVO()).getVmemo());
		}
		String isMod = null;
		for (int i = 0; i < itemMergeVOs.length; i++) {
			if (vendorVOsLength > 0) {
				isMod = (new UFDouble(i).mod(new UFDouble(vendorVOsLength)))
						.toString();
				if (i == 0 || "0.00000000".equals(isMod)) {
					getBillCardPanelWrapper().getBillCardPanel()
							.execBodyFormula(i, "cinventorycode");
				} else {
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(null, i, "crowno");
				}
				getBillCardPanelWrapper().getBillCardPanel().execBodyFormula(i,
						"cvendorname");
			} else {
				getBillCardPanelWrapper().getBillCardPanel().execBodyFormula(i,
						"cinventorycode");
				;
			}
			getBillCardPanelWrapper().getBillCardPanel().execBodyFormula(i,"sendtypename");
      getBillCardPanelWrapper().getBillCardPanel().execBodyFormula(i,"receiptareaname");
		}
		pricedVendorH = null;
		VendorNoPriceH = null;
		// ֻҪѡ����һ�ι�Ӧ��,����Ͳ����ٽ��б༭
		if (m_vendorVOs != null && m_vendorVOs.length > 0) {
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("crowno")
					.setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"cinventorycode").setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"cvendorname").setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vfree")
					.setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"dreceivedate").setEnabled(false);
			if (getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"vreveiveaddress") != null) {
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"vreveiveaddress").setEnabled(false);
			}
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vmemo")
					.setEnabled(false);
//			getBillCardPanelWrapper().getBillCardPanel()
//					.getBodyItem("ntaxrate").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("nasknum")
					.setEnabled(false);
			// ���������Ŀ���Ա༭
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"cvendorname").setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"nquoteprice").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"nquotetaxprice").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"deliverdays").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"dvaliddate").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"dinvaliddate").setEnabled(true);
		} else {// û��ѡ���κι�Ӧ��
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("crowno")
					.setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"cinventorycode").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"cvendorname").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vfree")
					.setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"dreceivedate").setEnabled(true);
			if (getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"vreveiveaddress") != null) {
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
						"vreveiveaddress").setEnabled(true);
			}
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vmemo")
					.setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel()
					.getBodyItem("ntaxrate").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem("nasknum")
					.setEnabled(true);
			// ���������Ŀ���Ա༭
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"cvendorname").setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"nquoteprice").setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"nquotetaxprice").setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"deliverdays").setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"dvaliddate").setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"dinvaliddate").setEnabled(false);

		}
		// ��ͷ�ɱ༭��
		if (getBillUI().getBillOperate() == IBillOperate.OP_ADD
				|| getBillUI().getBillOperate() == IBillOperate.OP_EDIT) {
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cdeptid")
					.setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"cemployeeid").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"ccurrencytypeid").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"ctermprotocolid").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"pk_purorg").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"vaskbillcode").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"dclosedate").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmemo")
					.setEnabled(true);
		}else{
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cdeptid")
			.setEnabled(false);
	        getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
			"cemployeeid").setEnabled(false);
	        getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
			"ccurrencytypeid").setEnabled(false);
	        getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
			"ctermprotocolid").setEnabled(false);
	        getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
			"pk_purorg").setEnabled(false);
	        getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
			"vaskbillcode").setEnabled(false);
	        getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
			"dclosedate").setEnabled(false);
	        getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmemo")
			.setEnabled(false);
		}
		getBillCardPanelWrapper().getBillCardPanel().updateValue();
		getBillCardPanelWrapper().getBillCardPanel().getBillModel()
				.updateValue();
		getBillCardPanelWrapper().getBillCardPanel().updateUI();
		String finish = nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"4004070101", "UPP4004070101-000051")/* @res "���" */;
		// �Ǳ༭���ҷ����״̬ѡ��Ӧ�̺��Ϊ�༭״̬
		if (getBillUI().getBillOperate() == IBillOperate.OP_NOTEDIT
				&& ibillStatus != null
				&& ibillStatus.trim().length() > 0
				&& !ibillStatus.equals(finish) 
//				&& m_vendorVOs != null && m_vendorVOs.length > 0
				) {
			onBoEditForVendorSelected(ibillStatus);
		}else{
			 //������������ʾ����
			nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanelWrapper().getBillCardPanel(), billVO);
			 //��ʾ������Դ��Ϣ
			nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanelWrapper().getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_ASK);
		}
		// ��ջ���
		m_itemVOs = null;

		// ѡ��Ӧ�̺󷢳����ȱ����ٷ���  since 5.6 ѡ��Ӧ�̺󲻷����ʼ�
//		if (m_vendorVOsForSend != null && m_vendorVOsForSend.length > 0) {
//			if(isSending()){
//			 onBoSave();
//			 onSendFromSelectVendor(m_vendorVOsForSend);
//			}
//		}
		line = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IBillButton.Line);

		// ѡ��Ӧ�̺��в�����ť������
		if (m_vendorVOs != null && m_vendorVOs.length > 0) {
			line.setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().setBBodyMenuShow(false);
			// ѯ���۵��ı�ͷ��Ŀ���ɱ༭
			BillItem[] headItems = getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItems();
			if (headItems != null && headItems.length > 0) {
				BillItem headItem = null;
				for (int i = 0; i < headItems.length; i++) {
					headItem = headItems[i];
					headItem.setEnabled(false);
				}
			}
		} else if(m_vendorVOs == null && getBillUI().getBillOperate() == IBillOperate.OP_NOTEDIT){
			line.setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().setBBodyMenuShow(false);
			// ѯ���۵��ı�ͷ��Ŀ���ɱ༭
			BillItem[] headItems = getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItems();
			if (headItems != null && headItems.length > 0) {
				BillItem headItem = null;
				for (int i = 0; i < headItems.length; i++) {
					headItem = headItems[i];
					headItem.setEnabled(false);
				}
			}
		}else {
			line.setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().setBBodyMenuShow(true);
			// ѯ���۵��ı�ͷ��Ŀ���ɱ༭
			BillItem[] headItems = getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItems();
			if (headItems != null && headItems.length > 0) {
				BillItem headItem = null;
				for (int i = 0; i < headItems.length; i++) {
					headItem = headItems[i];
					headItem.setEnabled(true);
				}
			}
		}
		try {
			((AskAndQuoteUI) getBillUI()).updateButtonUI();
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PuTool.outException(e);
		}
	}
	/**
	 * ��������:���ѡ��Ӧ�̰�ť��Ҫʹ�õ��Ĺ�Ӧ�̲���
	 */
	private UIRefPane getVendorRef() {
		if(refpaneVendor == null){
  		refpaneVendor = new UIRefPane("��Ӧ�̵���");
  		refpaneVendor.setTreeGridNodeMultiSelected(true);
  		refpaneVendor.setMultiSelectedEnabled(true);
  		refpaneVendor.setFocusable(true);
  		refpaneVendor.setCacheEnabled(false);
  		refpaneVendor.getRefModel().setIsDynamicCol(true);
  		refpaneVendor.getRefModel().setDynamicColClassName("nc.ui.pp.ask.RefDynamicForSelectVendors");
  		// xy & zhounl, since v56, ��۸�������ͳһ,���ݲ�֧��ɢ��
  		refpaneVendor.getRefModel().addWherePart(" and coalesce(bd_cubasdoc.freecustflag, 'N') = 'N' ") ;
		}
		return refpaneVendor;
	}
	/**
	 * ��������:�Ƿ���з�������
	 */
	public boolean isSending() {
		// ���ڱ༭����ʱ�˳���ʾ
			int iRet = MessageDialog
					.showYesNoDlg(getBillCardPanelWrapper().getBillCardPanel(), m_lanResTool.getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000270")/* "��ʾ" */,
							m_lanResTool.getStrByID("4004070101", "UPP4004070101-000114")/*
																		 * @res
																		 * "�Ƿ�ѯ���۵����͸���Ӧ�̣�"
																		 */);
			// ����ɹ�����˳�
			if (iRet == MessageDialog.ID_YES) {
				return true;
			}
			// �˳�
			else if (iRet == MessageDialog.ID_NO) {
				return false;
			}
			// ȡ���ر�
			else {
				return false;
			}
	}
	/**
	 * @���ܣ�����Ӧ�̷�������Email
	 * @���ߣ����� �������ڣ�(2001-9-3 16:27:21) 2002-10-17 wyf
	 *         �޸Ĺ�Ӧ�̲���Ϊ��״̬���ò��ղ������ò���refCustcode.setIsCustomDefined(true)�������Գ�ƽ�����
	 */
	private void onSend() throws Exception {
		String strVendors = nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"4004070101", "UPP4004070101-000045")/* @res "��Ӧ���б�:" */;
//		AskbillMergeVO[] cloneVOs = null;
//		boolean stop = false;
//		AskbillVO[] newVOs = null;
		AggregatedValueObject billVO = null;
		ExcelFileVO[] vos = null;
		String sFilePath = null;
		String sFilePathDir = getDefaultDir();
//		CacheTool cacheTool = new CacheTool();
		try {
			billVO = getBillUI().getVOFromUI();
		} catch (Exception e1) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e1.getMessage());
			PuTool.outException(e1);
		}
		getBillCardPanelWrapper().getBillCardPanel().getTailItem("caskpsnname");
		AskbillMergeVO currVO = (AskbillMergeVO) billVO;
		AskbillItemMergeVO[] itemVOs = (AskbillItemMergeVO[]) currVO
				.getChildrenVO();
		if(billVO == null || (billVO != null && (currVO == null || itemVOs == null))){
			return;
		}
		String cvendormangid = null;
		Hashtable vendorH = new Hashtable();
		Vector v = new Vector();
		String[] existIDs = null;
		if (itemVOs != null && itemVOs.toString().trim().length() > 0) {
			for (int i = 0; i < itemVOs.length; i++) {
				cvendormangid = itemVOs[i].getCvendormangid();
				if (cvendormangid != null
						&& cvendormangid.trim().length() > 0) {
					if (!vendorH.containsKey(cvendormangid)) {
						vendorH.put(cvendormangid, cvendormangid);
						v.add(cvendormangid);
					}
				}
			}
			if (v.size() > 0) {
				existIDs = new String[v.size()];
				v.copyInto(existIDs);
			}
		} else {
			return;
		}
		UIRefPane refVendors = new UIRefPane();

		refVendors.setMultiSelectedEnabled(true);
		refVendors.setRefModel(new CustRefModelForAskSend(getCorpId()));

//		String askcode = null;
//		askcode = ((AskbillHeaderVO) currVO.getParentVO()).getVaskbillcode();
		if (!(existIDs == null)) {
			String strWhere = "";
			for (int i = 0; i < existIDs.length; i++) {
                if(existIDs.length == 1){
                	strWhere += " and a.pk_cumandoc in ('" + existIDs[i] + "')  ";
                }else{
				if (i < existIDs.length - 1 ) {
					if (i == 0) {
						strWhere += " and a.pk_cumandoc in ('" + existIDs[i]
								+ "', ";
					} else {
						strWhere += " '" + existIDs[i] + "', ";
					}
				} else if (i == existIDs.length - 1) {
					strWhere += " '" + existIDs[i] + "') ";
				}
                }

			}
			refVendors.getRefModel().addWherePart(strWhere);
		}
		refVendors.onButtonClicked();

		int returntype = UIDialog.ID_CANCEL;
		// ȷ����������
		returntype = refVendors.getReturnButtonCode();
		Vector vendorV = new Vector();
		Vector vendorMangIdsV = new Vector();
		Hashtable resultH = new Hashtable();
		if (returntype == UIDialog.ID_OK) {
			String[] pkValues = refVendors.getRefModel().getPkValues();
			if (pkValues == null || (pkValues != null && pkValues.length == 0)) {
				return;
			}
			try {
				resultH = AskHelper.queryEmailAddrForAskSend(pkValues);
			} catch (Exception e1) {
				// TODO �Զ����� catch ��
				SCMEnv.out(e1.getMessage());
				PuTool.outException(e1);
			}

			for (int i = 0; i < refVendors.getRefModel().getRefNameValues().length; i++) {
				// ��Ӧ�������б�
				if (strVendors.indexOf(":") + 1 < strVendors.length()) {
					strVendors += nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000000")/* @res "��" */;
				}
				strVendors += refVendors.getRefModel().getRefNameValues()[i];

				vendorV.add(refVendors.getRefModel().getRefNameValues()[i]);
				vendorMangIdsV.add(refVendors.getRefModel().getPkValues()[i]);
			}

			if (!(refVendors.getRefModel().getRefNameValues() == null || refVendors
					.getRefModel().getRefNameValues().length == 0)) {

				if (currVO == null) {
					getBillUI()
							.showHintMessage(
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("40040701",
													"UPP40040701-000060")/*
																			 * @res
																			 * "���Ȳ�ѯ��¼�뵥�ݡ�"
																			 */);
					return;
				}
				if (currVO.getParentVO() == null) {
					currVO.setParentVO(new AskbillHeaderVO());
				}
				if ((currVO.getChildrenVO() == null)
						|| (currVO.getChildrenVO().length == 0)
						|| (currVO.getChildrenVO()[0] == null)) {
					getBillUI()
							.showHintMessage(
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("40040701",
													"UPP40040701-000061")/*
																			 * @res
																			 * "����ѡ�е��ݺ����ԡ�"
																			 */);
					return;
				}

				// ��Ҫ���������ݴ���VO
//				Hashtable result = new Hashtable();
				v = new Vector();
//				nc.vo.pp.ask.ExcelFileVO vo = null;
				// �õ���Ҫ������VO
				v = saveOutputVOsForSend(vendorV, vendorMangIdsV,
						new AskbillMergeVO[] { currVO });

				// �Ѿ��õ�vo��Ȼ�󵼳�vo
				if (v != null && v.size() > 0) {
					nc.ui.pp.pub.ExcelReadCtrl erc = null;
					File da = null;
					FileInputStream dsa = null;
					MailInfo mailInfo = null;
					AccountManageDialog dlg = null;
					nc.vo.pub.mail.AttachmentVO newAttachment = null;
					SendMailDialog mailTool = null;
					try {
					for (int i = 0; i < v.size(); i++) {
						vos = new ExcelFileVO[((Vector) v.get(i)).size()];
						((Vector) v.get(i)).copyInto(vos);

						// ���õ����ӿ�
						// �ļ����ƹ���Askbill+��˾PK+���ݺ�+"_"+��Ӧ��".xls"

						sFilePath = sFilePathDir + "\\" + "ѯ�۵���("
								+ vos[0].getVaskbillcode() + ")_��Ӧ��("
								+ vos[0].getCvendorname() + ").xls";
						erc = new nc.ui.pp.pub.ExcelReadCtrl();
						//try {
							erc.setVOToExcel(vos, sFilePath);
//						} catch (IOException e) {
//							// TODO �Զ����� catch ��
//							SCMEnv.out(e.getMessage());
//							PuTool.outException(e);
//						}
						da = new File(sFilePath);
						long fileLength = da.length();
						
						byte[] buff = null;
						try {
							dsa = new FileInputStream(sFilePath);
							buff = new byte[(int) fileLength];
							dsa.read(buff);// ʱ�����Ĵ�ԼΪ0.03��
						} catch (FileNotFoundException e) {
							// TODO �Զ����� catch ��
							SCMEnv.out(e.getMessage());
							PuTool.outException(e);
						} catch (IOException e) {
							// TODO �Զ����� catch ��
							SCMEnv.out(e.getMessage());
							PuTool.outException(e);
						}

						mailInfo = new MailInfo();
						String currentUser = nc.ui.pub.ClientEnvironment
								.getInstance().getUser().getPrimaryKey();
						MailInfo m_MailInfo = null;
						try {
							m_MailInfo = SFServiceFacility
									.getMailQueryService().getInitMailInfo(
											currentUser);
							if (m_MailInfo == null) {
								dlg=new AccountManageDialog((AskAndQuoteUI)getBillUI(),nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm","UPPsmcomm-000053")/*@res "�˺Ź���Ի���"*/,currentUser);
								dlg.showModal();
								((AskAndQuoteUI)getBillUI()).repaint();
								m_MailInfo = SFServiceFacility
										.getMailQueryService().getInitMailInfo(
												currentUser);
								if(m_MailInfo == null){
									return;
								}
							}
						} catch (BusinessException e) {
							// TODO �Զ����� catch ��
							SCMEnv.out(e.getMessage());
							PuTool.outException(e);
						}
						mailInfo.setReceiver((String)resultH.get(vos[0].getCvendormangid()));
						mailInfo.setCc("");
						mailInfo.setSubject("ѯ���۵�");
						mailInfo.setMessage("");
						mailInfo.setCurrentUser(currentUser);
						mailInfo.setUserName(m_MailInfo.getUserName());
						mailInfo.setPassword(m_MailInfo.getPassword());
						mailInfo.setPopHost(m_MailInfo.getPopHost());
						mailInfo.setSmtpHost(m_MailInfo.getSmtpHost());
						mailInfo.setSender(m_MailInfo.getSender());

						 newAttachment = new
						 nc.vo.pub.mail.AttachmentVO();
						 newAttachment.setFileName("ѯ���۵���(" +
								 vos[0].getVaskbillcode()
								 +")_��Ӧ��("+vos[0].getCvendorname()+ ").xls");
						 newAttachment.setFileContent(buff);
						 mailInfo.setAttachments(new AttachmentVO[]
						 {newAttachment});
						mailTool = new SendMailDialog(
								(AskAndQuoteUI) getBillUI(), "ѯ���۵�����", mailInfo);
						mailTool.showDialog(mailInfo);
						getBillUI()
								.showHintMessage(
										nc.ui.ml.NCLangRes.getInstance()
												.getStrByID("40040701",
														"UPP40040701-000062")/*
																				 * @res
																				 * "�������"
																				 */);
					}
					
					} catch (IOException e) {
						// TODO �Զ����� catch ��
						SCMEnv.out(e.getMessage());
						PuTool.outException(e);
					}finally{
						dsa.close();
					}
				}

				// ���ɶ��ŵ���
				cvendormangid = null;
//				cloneVOs = new AskbillMergeVO[refVendors.getRefModel()
//						.getPkValues().length];
				// �÷���״̬
				((AskbillHeaderVO) currVO.getParentVO())
						.setIbillstatus(StatusConvert
								.getStatusIndexNum().get(
										new Integer(IAskBillStatus.SENDING))
								.toString());
				getBufferData().setCurrentVO(currVO);
				try {
					updateBufferForAsk();
				} catch (Exception e) {
					// TODO �Զ����� catch ��
					SCMEnv.out(e.getMessage());
					PuTool.outException(e);
				}
				getBillCardPanelWrapper().getBillCardPanel().setBillValueVO(
						currVO);
			} else {
//				stop = true;
				strVendors += nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4004070101", "UPP4004070101-000046")/* @res "û��ѡȡ" */;
			}
		} else {
//			stop = true;
			dlgMail = null;
		}
	}

	/**
	 * @throws IOException 
	 * @���ܣ�����Ӧ�̷�������Email
	 * @���ߣ����� �������ڣ�(2001-9-3 16:27:21) 2002-10-17 wyf
	 *         �޸Ĺ�Ӧ�̲���Ϊ��״̬���ò��ղ������ò���refCustcode.setIsCustomDefined(true)�������Գ�ƽ�����
	 */
//	private void onSendFromSelectVendor(VendorVO[] vendorVOs) throws IOException {
//		String strVendors = nc.ui.ml.NCLangRes.getInstance().getStrByID(
//				"4004070101", "UPP4004070101-000045")/* @res "��Ӧ���б�:" */;
////		boolean stop = false;
////		AskbillVO[] newVOs = null;
//		AggregatedValueObject billVO = null;
//		ExcelFileVO[] vos = null;
//		String sFilePath = null;
//		String sFilePathDir = getDefaultDir();
////		CacheTool cacheTool = new CacheTool();
//		try {
//			billVO = getBillUI().getVOFromUI();
//		} catch (Exception e1) {
//			// TODO �Զ����� catch ��
//			SCMEnv.out(e1.getMessage());
//			PuTool.outException(e1);
//		}
//		AskbillMergeVO currVO = (AskbillMergeVO) billVO;
////		AskbillItemMergeVO[] itemVOs = (AskbillItemMergeVO[]) currVO
////				.getChildrenVO();
//		UIRefPane refVendors = new UIRefPane();
//
//		refVendors.setMultiSelectedEnabled(true);
//		refVendors.setRefModel(new CustRefModelForAskSend(getCorpId()));
//
////		String askcode = null;
////		askcode = ((AskbillHeaderVO) currVO.getParentVO()).getVaskbillcode();
////		String[] existIDs = null;
//		Vector vendorV = new Vector();
//		Vector vendorMangIdsV = new Vector();
//		Hashtable resultH = new Hashtable();
//		if (m_vendorVOsForSend != null && m_vendorVOsForSend.length > 0) {
//
//			for (int i = 0; i < m_vendorVOsForSend.length; i++) {
//				// ��Ӧ�������б�
//				if (strVendors.indexOf(":") + 1 < strVendors.length()) {
//					strVendors += nc.ui.ml.NCLangRes.getInstance().getStrByID(
//							"SCMCOMMON", "UPPSCMCommon-000000")/* @res "��" */;
//				}
//				strVendors += m_vendorVOsForSend[i].getCustname();
//
//				vendorV.add(m_vendorVOsForSend[i].getCustname());
//				vendorMangIdsV.add(m_vendorVOsForSend[i].getCumandoc());
//			}
//
//			if (!(vendorV == null || (vendorV != null && vendorV.size() == 0))) {
//
//				if (currVO == null) {
//					getBillUI()
//							.showHintMessage(
//									nc.ui.ml.NCLangRes.getInstance()
//											.getStrByID("40040701",
//													"UPP40040701-000060")/*
//																			 * @res
//																			 * "���Ȳ�ѯ��¼�뵥�ݡ�"
//																			 */);
//					return;
//				}
//				if (currVO.getParentVO() == null) {
//					currVO.setParentVO(new AskbillHeaderVO());
//				}
//				if ((currVO.getChildrenVO() == null)
//						|| (currVO.getChildrenVO().length == 0)
//						|| (currVO.getChildrenVO()[0] == null)) {
//					getBillUI()
//							.showHintMessage(
//									nc.ui.ml.NCLangRes.getInstance()
//											.getStrByID("40040701",
//													"UPP40040701-000061")/*
//																			 * @res
//																			 * "����ѡ�е��ݺ����ԡ�"
//																			 */);
//					return;
//				}
//
//				// ��Ҫ���������ݴ���VO
//				String[] pkValues = new String[vendorMangIdsV.size()];
//				vendorMangIdsV.copyInto(pkValues);
//				try {
//					resultH = AskHelper.queryEmailAddrForAskSend(pkValues);
//				} catch (Exception e1) {
//					// TODO �Զ����� catch ��
//					SCMEnv.out(e1.getMessage());
//					PuTool.outException(e1);
//				}
////				Hashtable result = new Hashtable();
//				Vector v = new Vector();
////				nc.vo.pp.ask.ExcelFileVO vo = null;
//				// �õ���Ҫ������VO
//				v = saveOutputVOsForSend(vendorV, vendorMangIdsV,
//						new AskbillMergeVO[] { currVO });
//
//				// �Ѿ��õ�vo��Ȼ�󵼳�vo
//				if (v != null && v.size() > 0) {
//					nc.ui.pp.pub.ExcelReadCtrl erc = null;
//					File da = null;
//					FileInputStream dsa = null;
//					MailInfo mailInfo = null;
//					AccountManageDialog dlg = null;
//					nc.vo.pub.mail.AttachmentVO newAttachment = null;
//					SendMailDialog mailTool = null;
//					for (int i = 0; i < v.size(); i++) {
//						vos = new ExcelFileVO[((Vector) v.get(i)).size()];
//						((Vector) v.get(i)).copyInto(vos);
//
//						// ���õ����ӿ�
//						// �ļ����ƹ���Askbill+��˾PK+���ݺ�+"_"+��Ӧ��".xls"
//
//						sFilePath = sFilePathDir + "\\" + "ѯ�۵���("
//								+ vos[0].getVaskbillcode() + ")_��Ӧ��("
//								+ vos[0].getCvendorname() + ").xls";
//						erc = new nc.ui.pp.pub.ExcelReadCtrl();
//						try {
//							erc.setVOToExcel(vos, sFilePath);
//						} catch (IOException e) {
//							// TODO �Զ����� catch ��
//							SCMEnv.out(e.getMessage());
//							PuTool.outException(e);
//						}
//
//					    da = new File(sFilePath);
//						long fileLength = da.length();
//						byte[] buff = null;
//						try {
//							dsa = new FileInputStream(sFilePath);
//							buff = new byte[(int) fileLength];
//							dsa.read(buff);// ʱ�����Ĵ�ԼΪ0.03��
//						} catch (FileNotFoundException e) {
//							// TODO �Զ����� catch ��
////							SCMEnv.out(e.getMessage());
////							PuTool.outException(e);
//							if(e.getMessage().indexOf("*") != -1){
//								getBillUI().showErrorMessage("���ݺ��к��С�*�������ܴ���ѯ�۵�excel�ļ������͹�Ӧ���ʼ�ʧ�ܡ�");
//							}else {
//								getBillUI().showErrorMessage(e.getMessage());
//							}
//						} catch (IOException e) {
//							// TODO �Զ����� catch ��
////							SCMEnv.out(e.getMessage());
//							PuTool.outException(e);
//						}finally{
//							if(dsa!=null)
//								dsa.close();
//							else{
//								return;
//							}
//						}
//
//						mailInfo = new MailInfo();
//						String currentUser = nc.ui.pub.ClientEnvironment
//								.getInstance().getUser().getPrimaryKey();
//						MailInfo m_MailInfo = null;
//						try {
//							m_MailInfo = SFServiceFacility
//									.getMailQueryService().getInitMailInfo(
//											currentUser);
//						if (m_MailInfo == null) {
//								dlg=new AccountManageDialog((AskAndQuoteUI)getBillUI(),nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm","UPPsmcomm-000053")/*@res "�˺Ź���Ի���"*/,currentUser);
//								dlg.showModal();
//								((AskAndQuoteUI)getBillUI()).repaint();
//								m_MailInfo = SFServiceFacility
//										.getMailQueryService().getInitMailInfo(
//												currentUser);
//								if(m_MailInfo == null){
//									return;
//								}
//							}
//						} catch (BusinessException e) {
//							// TODO �Զ����� catch ��
//							SCMEnv.out(e.getMessage());
//							PuTool.outException(e);
//						}
//						mailInfo.setReceiver((String)resultH.get(vos[0].getCvendormangid()));
//						mailInfo.setCc("");
//						mailInfo.setSubject("ѯ���۵�");
//						mailInfo.setMessage("");
//						mailInfo.setCurrentUser(currentUser);
//						mailInfo.setUserName(m_MailInfo.getUserName());
//						mailInfo.setPassword(m_MailInfo.getPassword());
//						mailInfo.setPopHost(m_MailInfo.getPopHost());
//						mailInfo.setSmtpHost(m_MailInfo.getSmtpHost());
//						mailInfo.setSender(m_MailInfo.getSender());
//
//						 newAttachment = new
//						 nc.vo.pub.mail.AttachmentVO();
//						 newAttachment.setFileName("ѯ���۵���(" +
//						 vos[0].getVaskbillcode()
//						 +")_��Ӧ��("+vos[0].getCvendorname()+ ").xls");
//						 newAttachment.setFileContent(buff);
//						 mailInfo.setAttachments(new AttachmentVO[]
//						 {newAttachment});
//						 mailTool = new SendMailDialog(
//								(AskAndQuoteUI) getBillUI(), "ѯ���۵�����", mailInfo);
//						mailTool.showDialog(mailInfo);
//						getBillUI()
//								.showHintMessage(
//										nc.ui.ml.NCLangRes.getInstance()
//												.getStrByID("40040701",
//														"UPP40040701-000062")/*
//																				 * @res
//																				 * "�������"
//																				 */);
//					}
//				}
//				// �÷���״̬
//				((AskbillHeaderVO) currVO.getParentVO())
//						.setIbillstatus(StatusConvert
//								.getStatusIndexNum().get(
//										new Integer(IAskBillStatus.SENDING))
//								.toString());
//				getBufferData().setCurrentVO(currVO);
//				try {
////					updateBufferForAsk();
////					onBoRefresh();
//					setSaveOperateState();
//				} catch (Exception e) {
//					// TODO �Զ����� catch ��
//					SCMEnv.out(e.getMessage());
//					PuTool.outException(e);
//				}
////				getBillCardPanelWrapper().getBillCardPanel().setBillValueVO(
////						currVO);
//			} else {
////				stop = true;
//				strVendors += nc.ui.ml.NCLangRes.getInstance().getStrByID(
//						"4004070101", "UPP4004070101-000046")/* @res "û��ѡȡ" */;
//			}
//		} else {
////			stop = true;
//		  
//			dlgMail = null;
//		}
//		
//	}

	/**
	 * @���ܣ���ȡEmail�Ի���
	 * @���ߣ����� �������ڣ�(2001-9-3 16:29:50)
	 */
	private QuickMail getEmailDlg() {
		if (dlgMail == null) {
			dlgMail = new QuickMail((BillManageUI) getBillUI(), "quickmail");
		}
		return dlgMail;
	}

	/**
	 * @���ܣ�����Email
	 * @���ߣ����� �������ڣ�(2001-9-3 16:27:41)
	 * @param: <|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void onReceive() {
		int iRet = getEmailDlg().showModal();
		if (iRet == UIDialog.ID_CANCEL)
			dlgMail = null;
	}

	/**
	 * ��ȡ��ӡ���
	 */
	private PrintEntry getPrintEntry1() {
		if (m_printEntry == null) {
			m_printEntry = new PrintEntry(null, null);
			m_printEntry.setTemplateID(getCorpId(), "4004070101", getUserId(),
					null);
		}
		return m_printEntry;
	}

	/**
	 * @���ܣ�����Email
	 * @���ߣ����� �������ڣ�(2001-9-3 16:27:41)
	 * @param: <|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void onPrintCard() {
		nc.ui.pu.pub.PuTool.onPrint(getPrintEntry1(),
				new nc.ui.rc.pub.PurchasePrintDS("4004070101",
						getBillCardPanelWrapper().getBillCardPanel()));
	}

	/**
	 * @���ܣ�����Email
	 * @���ߣ����� �������ڣ�(2001-9-3 16:27:41)
	 * @param: <|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void onPrintPreviewCard() {
		nc.ui.pu.pub.PuTool.onPreview(getPrintEntry1(),
				new nc.ui.rc.pub.PurchasePrintDS("4004070101",
						getBillCardPanelWrapper().getBillCardPanel()));

	}

	/**
	 * @���ܣ�����Email
	 * @���ߣ����� �������ڣ�(2001-9-3 16:27:41)
	 * @param: <|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void onPrintList() {
		if (printList == null) {
			printList = new ScmPrintTool((ISetBillVO) ((AskAndQuoteUI)getBillUI()),
					getBillCardPanelWrapper().getBillCardPanel(),
					getSelectedBills(), ((AskAndQuoteUI)getBillUI()).getModuleCode());
		} else {
			try {
				printList.setData(getSelectedBills());
			} catch (BusinessException e) {
				// TODO �Զ����� catch ��
				SCMEnv.out(e.getMessage());
				PuTool.outException(e);
			}
		}
		printList.print();
	}

	/**
	 * @���ܣ�����Email
	 * @���ߣ����� �������ڣ�(2001-9-3 16:27:41)
	 * @param: <|>
	 * @return:
	 * @throws Exception
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void onPrintByVendor() throws Exception {
		//��ʱ����ԭ����VO
//		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		//����Ӧ�̷ֵ���ӡ��VOs
		ArrayList printDatas = getDataForPrintByVendor();
		if (printDatas == null || (printDatas != null && printDatas.size() == 0)) {
			return;
		}
		if (printListForVendor == null) {
			printListForVendor = new ScmPrintTool((AskAndQuoteUI) getBillUI(),
					getBillCardPanelWrapper().getBillCardPanel(),
					printDatas, "4004070101");
		} else {
			try {

				printListForVendor.setData(printDatas);
			} catch (BusinessException e) {
				// TODO �Զ����� catch ��
				SCMEnv.out(e.getMessage());
				PuTool.outException(e);
			} catch (Exception e) {
				// TODO �Զ����� catch ��
				SCMEnv.out(e.getMessage());
				PuTool.outException(e);
			}
		}
		printListForVendor.onBatchPrint(((AskAndQuoteUI) getBillUI())
				.getBillListPanel(), getBillCardPanelWrapper()
				.getBillCardPanel(), "29",false);
		setBillVOToCard();
	}

	private ArrayList getSelectedBills() {

		Vector vAll = new Vector();
//		Vector vTemp = new Vector();
		AskbillMergeVO[] allvos = null;
		AskbillMergeVO vo = null;
		// ȫ��ѡ��ѯ�۵�
		int iPos = 0;
		for (int i = 0; i < ((BillManageUI) getBillUI()).getBillListWrapper()
				.getBillListPanel().getHeadBillModel().getRowCount(); i++) {
			if (((BillManageUI) getBillUI()).getBillListWrapper()
					.getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
				iPos = i;
				iPos = nc.ui.pu.pub.PuTool.getIndexBeforeSort(
						((BillManageUI) getBillUI()).getBillListWrapper()
								.getBillListPanel(), iPos);
				vAll.add(getBufferData().getVOByRowNo(iPos));
			}
		}

		allvos = new AskbillMergeVO[vAll.size()];
		vAll.copyInto(allvos);

		// ��ѯδ��������ĵ�����
		try {
			allvos = getRefreshedVOs(allvos);
			nc.ui.scm.pub.FreeVOParse freeParse = null;
			String[] saKey = null;
			for (int k = 0; k < allvos.length; k++) {
				vo = new AskbillMergeVO();
				vo = allvos[k];
				// ����������
				freeParse = new nc.ui.scm.pub.FreeVOParse();
				
				freeParse.setFreeVO(vo.getChildrenVO(), "vfree", "vfree",
						"cbaseid", "cmangid", true);
				getBillCardPanelWrapper().getBillCardPanel().setBillValueVO(vo);

				// ������������򶳽�ȱ����������ʾ����
				PpTool.loadBDData(getBillCardPanelWrapper().getBillCardPanel());
				// �����Զ�����
				AskbillHeaderVO voHead = (AskbillHeaderVO) vo.getParentVO();
				saKey = new String[] { "vdef1", "vdef2", "vdef3",
						"vdef4", "vdef5", "vdef6", "vdef7", "vdef8", "vdef9",
						"vdef10", "vdef11", "vdef12", "vdef13", "vdef14",
						"vdef15", "vdef16", "vdef17", "vdef18", "vdef19",
						"vdef20" };
				int iLen = saKey.length;
				for (int i = 0; i < iLen; i++) {
					javax.swing.JComponent component = getBillCardPanelWrapper()
							.getBillCardPanel().getHeadItem(saKey[i])
							.getComponent();
					if (component instanceof UIRefPane) {
						voHead.setAttributeValue(saKey[i],
								((UIRefPane) component).getUITextField()
										.getText());
					}
				}
				getBillCardPanelWrapper().getBillCardPanel().updateValue();
				getBillCardPanelWrapper().getBillCardPanel().getBillModel()
						.updateValue();
				getBillCardPanelWrapper().getBillCardPanel().getBillModel()
						.execLoadFormula();

				getBillCardPanelWrapper().getBillCardPanel()
						.execHeadLoadFormulas();

				getBillCardPanelWrapper().getBillCardPanel().getBillModel()
						.updateValue();

				String NAME_HEADVO = "nc.vo.pp.ask.AskbillHeaderVO";
				String NAME_ITEMVOs = "nc.vo.pp.ask.AskbillItemMergeVO";
				// ��ͷ
				AskbillHeaderVO voHeader = (AskbillHeaderVO) getBillCardPanelWrapper()
						.getBillCardPanel().getBillData().getHeaderValueVO(
								NAME_HEADVO);
				AskbillItemMergeVO[] voItems = (AskbillItemMergeVO[]) getBillCardPanelWrapper()
						.getBillCardPanel().getBillData().getBodyValueVOs(
								NAME_ITEMVOs);
				vo.setParentVO(voHeader);
				vo.setChildrenVO(voItems);
				allvos[k] = vo;
			}

		} catch (BusinessException b) {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
							"UPP40040701-000055")/* @res "���ִ���:" */
							+ b.getMessage());
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
							"UPP40040701-000056")/* @res "����δ֪����" */);
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
	 * @���ܣ�����Email
	 * @���ߣ����� �������ڣ�(2001-9-3 16:27:41)
	 * @param: <|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void onPrintPreviewList() {
		if (printList == null) {
			printList = new ScmPrintTool((ISetBillVO)((AskAndQuoteUI)getBillUI()),
					getBillCardPanelWrapper().getBillCardPanel(),
					getSelectedBills(), ((AskAndQuoteUI)getBillUI()).getModuleCode());
		} else {
			try {
				printList.setData(getSelectedBills());
			} catch (BusinessException e) {
				// TODO �Զ����� catch ��
				SCMEnv.out(e.getMessage());
				PuTool.outException(e);
			}
		}
		printList.preview();
	}

	/**
	 * @���ܣ������빺������ѯ�۵�����
	 * @���ߣ����� �������ڣ�(2001-8-11 14:56:58)
	 * @param: <|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 * @return nc.ui.pp.ask.GenAskbillFromPray
	 */
//	private VendorSelectUI getVendorSelectUI() {
//		String pkField = PfUIDataCache.getBillPK("@4004010");
//		// if (vendorSelectUI == null) {
//		vendorSelectUI = new VendorSelectUI(pkField, getCorpId(), getUserId(),
//				"400407010101", "1=1", "@4004010", null,
//				"40040407010000000005", "29", getBillCardPanelWrapper()
//						.getBillCardPanel());
//		// }
//		return vendorSelectUI;
//	}

	/**
	 * @���ܣ���ȡ��˾ID
	 */
	private String getCorpId() {
		String corpid = null;
		if (corpid == null || "".equals(corpid.trim())) {
			corpid = nc.ui.pub.ClientEnvironment.getInstance().getCorporation()
					.getPrimaryKey();
		}
		return corpid;
	}

	/**
	 * @���ܣ���ȡ��½����
	 */
	private String getLogDate() {
		UFDate logDate = nc.ui.pub.ClientEnvironment.getInstance().getDate();
		return logDate.toString();
	}

	/**
	 * @���ܣ���ȡ������ʱ��
	 */
	private String getDateForTime() {
		UFDateTime serverTime = new UFDateTime(new Date());
		;
		return serverTime.toString();
	}
	/**
	 * @���ܣ���ȡ��½��ID
	 */
	public String getUserId() {
		String userid = nc.ui.pub.ClientEnvironment.getInstance().getUser()
				.getPrimaryKey();
		return userid;
	}

	/**
	 * �����빺������ѯ���۵�����
	 */
	private void onGenFromPrayBill() {
//		PrayToAskDLG billReferUI = getNewGeneratePanel();
//		// ******2003-05-03***�����ѯ������DLG
//		billReferUI.onQuery();
//		if (billReferUI.getIsIDOK()) {
//			// ����ģ��
//			billReferUI.addBillUI();
//			// ��������
//			billReferUI.loadHeadData();����
//			billReferUI.showModal();
//		}
////		AskbillVO[] newVOs = null;
////		newVOs = billReferUI.getM_askbillVOs();
//		returnBack();
		
		nc.ui.pub.ButtonObject bo = new nc.ui.pub.ButtonObject("ѯ����ת��");
	    bo.setTag("20:111111111111111");
	    SourceRefDlg.childButtonClicked(bo, getCorpId(), "4004070101", 
	        getUserId(), nc.vo.scm.pu.BillTypeConst.PO_ASK, getBillUI());
	    if (SourceRefDlg.isCloseOK()) {
	    setChangeUI();
	    }
	}
	
	private void setChangeUI(){
		AggregatedValueObject[] arySourceVOs = null;
		  if (SourceRefDlg.isCloseOK()) {
		    arySourceVOs = SourceRefDlg.getRetsVos();
		  }
		  ((AskAndQuoteUI) getBillUI()).getManagePane().remove(((AskAndQuoteUI) getBillUI()).getBillCardWrapper().getBillCardPanel());
		  ((AskAndQuoteUI) getBillUI()).getManagePane().add(((AskAndQuoteUI) getBillUI()).getBillListPanel(),BorderLayout.CENTER);
		  ((AskAndQuoteUI) getBillUI()).setCurrentPanel(BillTemplateWrapper.LISTPANEL);
		  if(arySourceVOs == null){
		    return;
		  }
		  AskbillHeaderVO[] headerVOs = new AskbillHeaderVO[arySourceVOs.length];
		  Vector<AskbillHeaderVO> vecheadVOs = new Vector<AskbillHeaderVO>();
		  for(int i = 0 ; i < arySourceVOs.length ; i ++){
			  vecheadVOs.add((AskbillHeaderVO)arySourceVOs[i].getParentVO());
		  }
		  vecheadVOs.copyInto(headerVOs);
		  Vector<AskbillMergeVO> vecMergeVO = new Vector<AskbillMergeVO>();
		  for(int i = 0 ; i < arySourceVOs.length ; i ++){
			  Vector vecTemp = new Vector();
			  vecTemp.add(arySourceVOs[i].getParentVO());
			  vecTemp.add(arySourceVOs[i].getChildrenVO());
			  vecTemp.add(null);
			  AskbillMergeVO vo = AskbillMergeVO.convertVOTOFront(vecTemp);
			  new nc.ui.scm.pub.FreeVOParse().setFreeVO(vo.getChildrenVO(), "vfree","vfree","cbaseid","cmangid",true);
			  vecMergeVO.add(vo);
		  }
		  AskbillMergeVO[] vos = new AskbillMergeVO[vecMergeVO.size()];
		  vecMergeVO.copyInto(vos);
		  getBufferData().clear();
//		  ((BillUIRefCacheForPP) ((AskAndQuoteUI) getBillUI())
//					.getCacheData()).addVOsToCache(vos);
		  getBufferData().addVOsToBuffer(vos);
		  ((AskAndQuoteUI) getBillUI()).getBillListPanel().setHeaderValueVO(headerVOs);
		  ((BillManageUI) getBillUI()).getBillListPanel().getHeadBillModel().execLoadFormula();
		  ((AskAndQuoteUI) getBillUI()).getBillListPanel().setBodyValueVO(arySourceVOs[0].getChildrenVO());
		  ((BillManageUI)getBillUI()).getBillListPanel().getBodyBillModel().execLoadFormula();
		  ((AskAndQuoteUI) getBillUI()).getBillListPanel().updateUI();
	}
	
	/**
	 * @���ܣ����빺����������ѯ�۵��󷵻� �������ɵ�ѯ�۵�ֱ�Ӳ������ݿ�ͬʱ׷�ӵ�ǰ��
	 * @���ߣ����� �������ڣ�(2001-8-15 16:12:13)
	 */
//	private boolean returnBack() {
//		Vector newVOs = new Vector();
//		newVOs = getNewGeneratePanel().getM_askbillVector();
//		if (newVOs != null && newVOs.size() > 0) {
//			try {
//				getBillCardPanelWrapper().getBillCardPanel().remove(
//						getNewGeneratePanel());
//				AskbillMergeVO[] mergeVOs = null;
//				AskbillMergeVO mergeVO = null;
//				Vector v = null;
//				Vector vTemp = new Vector();
//				;
//				for (int i = 0; i < newVOs.size(); i++) {
//					mergeVO = new AskbillMergeVO();
//					v = new Vector();
//					v = (Vector) newVOs.get(i);
//                    setInfoForConvertForFront(v);
//					mergeVO = AskbillMergeVO.convertVOTOFront(v);
//                     //����������
//					setVfree((AskbillItemMergeVO[])mergeVO.getChildrenVO());
//					vTemp.add(mergeVO);
//				}
//				if (vTemp.size() > 0) {
//					mergeVOs = new AskbillMergeVO[vTemp.size()];
//					vTemp.copyInto(mergeVOs);
//				}
//				setRefData(mergeVOs);
//				getNewGeneratePanel().setM_askbillVector();
//				setColumnEditable();
//                setOperatorID();
//
//				return true;
//			} catch (Exception e) {
//				SCMEnv
//						.out("���빺�����ɵ�ѯ���۵�д�����ݿ�ʱ����[nc.ui.pp.ask.InquireUI.onSend()->AskbillBO_Client.insertVOsFromPrayMy(newVOs,getClientEnvironment().isGroup())]");
//				getNewGeneratePanel().setM_askbillVector();
//				PuTool.outException(e);
//				return false;
//			}
//		} else {
//
//		}
//		return true;
//	}

	protected void setColumnEditable() {
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("nasknum")
		.setEdit(true);
	}
	/**
	* ����ת����Ƭ���浥����Ŀ�ɱ༭��
	*/
	protected void setColumnEditableForConvertBill() {
//	 ѯ���۵��ı�ͷ��Ŀ�ɱ༭
	BillItem[] headItems = getBillCardPanelWrapper()
			.getBillCardPanel().getHeadItems();
	if (headItems != null && headItems.length > 0) {
		BillItem headItem = null;
		for (int i = 0; i < headItems.length; i++) {
			headItem = headItems[i];
			headItem.setEnabled(true);
		}
	}
	getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
			"crowno").setEnabled(true);
	getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
			"cinventorycode").setEnabled(true);
	getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
			"cvendorname").setEnabled(true);
	getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
			"vfree").setEnabled(true);
	getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
			"dreceivedate").setEnabled(true);
	if (getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
			"vreveiveaddress") != null) {
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
				"vreveiveaddress").setEnabled(true);
	}
	getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
			"vmemo").setEnabled(true);
	getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
			"ntaxrate").setEnabled(true);
	getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
			"nasknum").setEnabled(true);
	// ��˰���ۡ���˰���ۡ������ڡ������ˡ�������Ч���ڡ�����ʧЧ���ڲ��ɱ༭
	getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
			"cvendorname").setEnabled(false);
	getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
			"nquoteprice").setEnabled(false);
	getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
			"nquotetaxprice").setEnabled(false);
	getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
			"deliverdays").setEnabled(false);
	getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
			"dvaliddate").setEnabled(false);
	getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
			"dinvaliddate").setEnabled(false);
	getBillCardPanelWrapper().getBillCardPanel().getTailItem(
			"cquotepsn").setEnabled(false);
	getBillCardPanelWrapper().getBillCardPanel().getTailItem(
			"dquotedate").setEnabled(false);
	}
	/**
	* ����ҵ��ԱĬ��ֵ ���ݲ���Ա����ҵ��Ա
	*/
	private void setOperatorID() throws BusinessException {
		//v5.1 Added Begin
		//����ҵ��ԱĬ��ֵ ���ݲ���Ա����ҵ��Ա
		
		UIRefPane cemployeeid = (UIRefPane) getBillCardPanelWrapper()
		.getBillCardPanel().getHeadItem("cemployeeid")
		.getComponent();
		//���ģ���Ѿ�����ҵ��Ա�����Ա���ɹ�Ա������ҵ��Ա����Ĭ��ֵ��������
		//���ģ��û�����ã����ݲ���Ա����ҵ��Ա�����Ա���ɹ�Ա������ҵ��Ա����Ĭ��ֵ�����ݣ��û�������ѡ����ҵ��Ա��
		if(cemployeeid != null && cemployeeid.getRefPK() == null){
		 PsndocVO voPsnDoc = PuTool.getPsnByUser(getUserId(),getCorpId());
		 if(voPsnDoc != null ){
		  cemployeeid.setPK(voPsnDoc.getPk_psndoc());
		
		  UIRefPane cdeptid = (UIRefPane) getBillCardPanelWrapper()
		  .getBillCardPanel().getHeadItem("cdeptid")
		  .getComponent();
		  cdeptid.setPK(voPsnDoc.getPk_deptdoc());
		 }
		}
		//v5.1 Added End
	}

	/**
	 * ���ý������ݣ���ִ��������ظ��෽�� �������ڣ�(2004-4-1 8:20:25)
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	protected void setRefData(AggregatedValueObject[] vos)
			throws java.lang.Exception {
		if (vos == null || vos.length == 0)
			throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("uifactory", "UPPuifactory-000084")/*
																	 * @res
																	 * "δѡ����յ���"
																	 */);
		AggregatedValueObject[] splitVos = vos;

		if (splitVos.length == 1) {
			// ���õ���״̬
			getBillUI().setCardUIState();
			// ������
			getBillUI().setCardUIData(splitVos[0]);
			// ����Ϊ��������
			getBillUI().setBillOperate(IBillOperate.OP_REFADD);
			 //��ʾ��Դ��Ϣ
			nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanelWrapper().getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_ASK);
			// ѯ���۵��ı�ͷ��Ŀ���ɱ༭
			BillItem[] headItems = getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItems();
			if (headItems != null && headItems.length > 0) {
				BillItem headItem = null;
				for (int i = 0; i < headItems.length; i++) {
					headItem = headItems[i];
					headItem.setEdit(true);
				}
			}
      //������
			try {
				   onBoLineAdd();
			} catch (Exception e) {
				// TODO �Զ����� catch ��
				SCMEnv.out(e.getMessage());
				PuTool.outException(e);
			}
			setButtonsStateCardMaintain();
			setButtonsStateEdit();
			setColumnEditable();
			((AskAndQuoteUI) getBillUI()).getBillCardPanel().updateUI();
			
		} else {
			// ���ò�������
			((AskAndQuoteUI) getBillUI()).getBillListPanel()
					.getParentListPanel().removeTableSortListener();
			((AskAndQuoteUI) getBillUI())
					.setCurrentPanel(BillTemplateWrapper.LISTPANEL);
			((BillUIRefCacheForPP) ((AskAndQuoteUI) getBillUI()).getCacheData())
					.setCurRefCacheClear();
			((AskAndQuoteUI) getBillUI()).setListRefCacheData(splitVos);
			((AskAndQuoteUI) getBillUI()).getBillListPanel().setBodyValueVO(
					splitVos[0].getChildrenVO());
			// ����Ϊ��������
			(((AskAndQuoteUI) getBillUI()))
					.setSplitBillOperate(IBillOperate.OP_REFADD);
			(((AskAndQuoteUI) getBillUI())).getBillListPanel()
					.getHeadBillModel().setRowState(0, BillModel.SELECTED);
			((BillUIRefCacheForPP) ((AskAndQuoteUI) getBillUI()).getCacheData())
					.setSelectedRow(0);

			(((AskAndQuoteUI) getBillUI())).getBillListPanel().getHeadTable()
					.setRowSelectionInterval(
							(((AskAndQuoteUI) getBillUI())).getCacheData()
									.getSelectedRow(),
							(((AskAndQuoteUI) getBillUI())).getCacheData()
									.getSelectedRow());
			((AskAndQuoteUI) getBillUI()).getBillListPanel().getHeadBillModel()
					.execLoadFormula();
			((AskAndQuoteUI) getBillUI()).getBillListPanel().getBodyBillModel()
					.execLoadFormula();
			setButtonsStateList();
			((AskAndQuoteUI) getBillUI()).getBillListPanel().updateUI();
		}
	}

	/**
	 * @���ܣ������빺������ѯ�۵�����
	 * @���ߣ����� �������ڣ�(2001-8-11 14:56:58)
	 * @param: <|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 * @return nc.ui.pp.ask.GenAskbillFromPray
	 */
//	private PrayToAskDLG getNewGeneratePanel() {
//		String pkField = PfUIDataCache.getBillPK(IBillType.PRAYBILL);
//		if (billReferUI == null) {
//			billReferUI = new PrayToAskDLG(pkField, getCorpId(), getUserId(),
//					"400407010101", "1=1", IBillType.PRAYBILL, null, "40040704010000000000",
//					IBillType.ASKBILL, getBillCardPanelWrapper().getBillCardPanel());
//		}
//		return billReferUI;
//	}
	/**
	 * ��ťm_boLineDel���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoLineDel() throws Exception {
		super.onBoLineDel();
        //������ʾ 
		AskbillMergeVO voCurr = (AskbillMergeVO) getBillCardPanelWrapper().getBillCardPanel().getBillValueVO(AskbillMergeVO.class.getName(), AskbillHeaderVO.class.getName(), AskbillItemMergeVO.class.getName());
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanelWrapper().getBillCardPanel(), voCurr);
	}
	/**
	 * ��ťm_boDel���ʱִ�еĶ���,���б�Ҫ���븲��. ���ݵ����ϴ���
	 */
	protected void onBoDel() throws Exception {
		int ret = MessageDialog.showYesNoDlg((AskAndQuoteUI) getBillUI(),
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101",
						"UPP4004070101-000070")/* @res "ѯ��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101",
						"UPP4004070101-000071")/*
												 * @res "����ѯ���۵���\n���ϲ������ɻָ���"
												 */);
		if (ret == MessageDialog.ID_NO || ret == MessageDialog.ID_CANCEL) {
			return;
		}
		Vector vDiscardIndex = new Vector();
		Vector vv = new Vector();
		AggregatedValueObject modelVo = null;
		Vector v = new Vector();
		AskbillItemVendorVO[] vendorVOs = null;
		int ibillstatus = 0;
		int nCurIndex = 0;
		String status = null;
		if (((BillManageUI) getBillUI()).isListPanelSelected()) {// �б�ɾ��
			//isListDel = true;
			String ibillST = null;
			((BillManageUI) getBillUI()).getBillListWrapper()
			.getBillListPanel().getBodyBillModel().clearBodyData();
			// �õ���ѡ�е�VO
			for (int i = 0; i < getBufferData().getVOBufferSize(); i++) {
				if (((BillManageUI) getBillUI()).getBillListPanel()
						.getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
					// �п��ܾ��������򣬵õ������ı�ͷINDEX
					nCurIndex = i;
					ibillST = ((AskbillHeaderVO) getBufferData().getVOByRowNo(
							nCurIndex).getParentVO()).getIbillstatus();
					if (ibillST != null
							&& ibillST.trim().length() > 0
							&& (!ibillST.trim()
									.equals(
											new Integer(IAskBillStatus.FREE)
													.toString())
									&& !ibillST.trim().equals(
											new Integer(IAskBillStatus.SENDING)
													.toString())
									&& !ibillST.trim().equals(
											new Integer(IAskBillStatus.DEPOSED)
													.toString())
									&& !ibillST.trim().equals(
											new Integer(IAskBillStatus.CONFIRM)
													.toString()) && !ibillST
									.trim().equals(
											new Integer(IAskBillStatus.QUOTED)
													.toString()))) {
						status = StatusConvert
								.getStatusIndexName().get(
										((AskbillHeaderVO) getBufferData()
												.getVOByRowNo(nCurIndex)
												.getParentVO())
												.getIbillstatus()).toString();
					} else {
						status = ibillST;
					}
					ibillstatus = new Integer(status).intValue();
					if (ibillstatus != IAskBillStatus.CONFIRM) {// ���״̬��ѯ���۵���������
						vDiscardIndex.add(new Integer(nCurIndex));
						if (getBufferData() != null
								&& getBufferData().getVOBufferSize() > 0) {
							modelVo = getBufferData().getVOByRowNo(nCurIndex);
							if (modelVo != null
									&& (modelVo.getChildrenVO() == null || (modelVo
											.getChildrenVO() != null && modelVo
											.getChildrenVO().length == 0))) {
								getAskbillVOsFromDB(nCurIndex);
							}
							getBufferData().setCurrentRow(nCurIndex);
							modelVo = getBufferData().getVOByRowNo(nCurIndex);
							convertIBillStatusToBack(modelVo);
              setInfoForConvertForBack(modelVo);
							v = AskbillMergeVO.convertVOTOBack(modelVo);
							vv.add(v);
						}
					}
				}
			}
		} else {// ��Ƭɾ��
			modelVo = getBillUI().getVOFromUI();
			if (((AskbillHeaderVO) modelVo.getParentVO()).getIbillstatus() != null
					&& ((AskbillHeaderVO) modelVo.getParentVO())
							.getIbillstatus().trim().length() > 0) {
				if (((AskbillHeaderVO) modelVo.getParentVO()).getIbillstatus()
						.trim().equals(new Integer(IAskBillStatus.FREE).toString())
						|| ((AskbillHeaderVO) modelVo.getParentVO())
								.getIbillstatus().trim().equals(new Integer(IAskBillStatus.SENDING).toString())
						|| ((AskbillHeaderVO) modelVo.getParentVO())
								.getIbillstatus().trim().equals(new Integer(IAskBillStatus.QUOTED).toString())
						|| ((AskbillHeaderVO) modelVo.getParentVO())
								.getIbillstatus().trim().equals(new Integer(IAskBillStatus.CONFIRM).toString())) {
					status = ((AskbillHeaderVO) modelVo.getParentVO())
							.getIbillstatus().trim();
				} else {
					status = StatusConvert.getStatusIndexName()
							.get(
									((AskbillHeaderVO) modelVo.getParentVO())
											.getIbillstatus()).toString();
				}
			}
			ibillstatus = new Integer(status).intValue();
			if (ibillstatus != IAskBillStatus.CONFIRM) {// ���״̬��ѯ���۵���������
				convertIBillStatusToBack(modelVo);
        setInfoForConvertForBack(modelVo);
				v = AskbillMergeVO.convertVOTOBack(modelVo);
				vendorVOs = (AskbillItemVendorVO[]) v.get(3);
				vv.add(v);
			} else {
				MessageDialog.showErrorDlg((AskAndQuoteUI) getBillUI(), "ɾ��",
						"���״̬��ѯ���۵���������");
				return;
			}
		}
		int vendorVOsLength = 0;
		if (!((BillManageUI) getBillUI()).isListPanelSelected()) {// ��Ƭɾ��
			if (vendorVOs != null && vendorVOs.length > 0) {
				vendorVOsLength = vendorVOs.length;
			}
		}
		
		if (vv == null || (vv != null && vv.size() == 0)) {
			MessageDialog.showErrorDlg((AskAndQuoteUI) getBillUI(), "ɾ��",
			"���״̬��ѯ���۵���������");
			return;
		}
		boolean isSuccess = AskHelper.deleteMy(vv);
		if (isSuccess) {
			// ����״̬
			if (!((BillManageUI) getBillUI()).isListPanelSelected()) {// ��Ƭɾ��
				modelVo.getParentVO().setAttributeValue(
						getBillField().getField_BillStatus(),
						new Integer(IBillStatus.DELETE));
				getBufferData().removeCurrentRow();
			} else {// �б�ɾ��
				// �ӻ����г�ȥ
				Integer[] iaIndex = new Integer[vDiscardIndex.size()];
				vDiscardIndex.copyInto(iaIndex);
				for (int i = 0; i < iaIndex.length; i++) {
					if (iaIndex[i].intValue() == 0) {
						getBufferData().setCurrentRow(iaIndex[i].intValue());
					} else if (iaIndex[i].intValue() > 0) {
						getBufferData()
								.setCurrentRow(iaIndex[i].intValue() - i);
					}
					getBufferData().removeCurrentRow();
				}
				getBufferData().updateView();
				updateBufferForAskListCHG();
			}
//			onBoRefresh();
			if (getBufferData().getVOBufferSize() == 0) {

				getBillUI().setBillOperate(IBillOperate.OP_INIT);
			} else {
				getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
				getBufferData().setCurrentRow(0);
				getBufferData().updateView();
				AggregatedValueObject billVO = getBufferData().getCurrentVO();
				if(billVO.getParentVO().getPrimaryKey() != null){
  				billVO.setChildrenVO(null);
  				getBufferData().setCurrentVO(billVO);
  				getAskbillVOsFromDB(getBufferData().getCurrentRow());
  				billVO = getBufferData().getCurrentVO();
				}
				AskbillItemMergeVO[] itemMergeVOs = (AskbillItemMergeVO[]) billVO
						.getChildrenVO();
				// }
				if (!((BillManageUI) getBillUI()).isListPanelSelected()) {// ��Ƭɾ������
					getBillCardPanelWrapper().getBillCardPanel()
							.setBillValueVO(billVO);

					String isMod = null;
					for (int i = 0; i < itemMergeVOs.length; i++) {
						isMod = (new UFDouble(i).mod(new UFDouble(
								vendorVOsLength))).toString();
						if (i == 0 || "0.00000000".equals(isMod)) {
							getBillCardPanelWrapper().getBillCardPanel()
									.execBodyFormula(i, "cinventorycode");
						} else {
							getBillCardPanelWrapper().getBillCardPanel()
									.setBodyValueAt(null, i, "crowno");
						}
						getBillCardPanelWrapper().getBillCardPanel()
								.execBodyFormula(i, "cvendorname");
					}
					// ֻҪѡ����һ�ι�Ӧ��,����Ͳ����ٽ��б༭
					getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
							"crowno").setEdit(false);
					getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
							"cinventorycode").setEdit(false);
					getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
							"cvendorname").setEdit(false);
					getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
							"vfree").setEdit(false);
					getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
							"dreceivedate").setEdit(false);
					if (getBillCardPanelWrapper().getBillCardPanel()
							.getBodyItem("vreveiveaddress") != null) {
						getBillCardPanelWrapper().getBillCardPanel()
								.getBodyItem("vreveiveaddress").setEdit(false);
					}
					getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
							"vmemo").setEdit(false);
//					getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
//							"ntaxrate").setEdit(false);
					getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
							"nasknum").setEdit(false);

					setBillVOToCard();
					setButtonsStateBrowse();
				} else {// �б�ɾ������
					Hashtable cvendorMangids = new Hashtable();
					if (itemMergeVOs != null && itemMergeVOs.length > 0) {
						String cvendorMangid = null;
						for (int i = 0; i < itemMergeVOs.length; i++) {
							cvendorMangid = itemMergeVOs[i].getCvendormangid();
							if (cvendorMangid != null
									&& cvendorMangid.trim().length() > 0) {
								if (!cvendorMangids.containsKey(cvendorMangid)) {
									cvendorMangids.put(cvendorMangid,
											cvendorMangid);
								}

							}
						}

						if (cvendorMangids != null && cvendorMangids.size() > 0) {
							vendorVOsLength = cvendorMangids.size();
						}
					}
					String isMod = null;
					String formulaA = "cbaseid -> getColValue(bd_invmandoc, pk_invbasdoc, pk_invmandoc, cmangid);cinventorycode->getColValue(bd_invbasdoc,invcode,pk_invbasdoc,cbaseid);cinventoryname -> getColValue(bd_invbasdoc, invname, pk_invbasdoc, cbaseid); cinventorytype -> getColValue(bd_invbasdoc, invtype, pk_invbasdoc, cbaseid); cinventoryspec -> getColValue(bd_invbasdoc, invspec, pk_invbasdoc, cbaseid); cmainmeasid -> getColValue(bd_invbasdoc, pk_measdoc, pk_invbasdoc, cbaseid); cmainmeasname -> getColValue(bd_measdoc, measname, pk_measdoc, cmainmeasid); prodarea -> getColValue(bd_invmandoc, prodarea, pk_invmandoc,cmangid);pk_cubasdoc->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cvendormangid);cvendorname->getColvalue(bd_cubasdoc,custname,pk_cubasdoc,pk_cubasdoc)";
					String formulaB = "pk_cubasdoc->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cvendormangid);cvendorname->getColvalue(bd_cubasdoc,custname,pk_cubasdoc,pk_cubasdoc)";
					if (itemMergeVOs != null && itemMergeVOs.length > 0) {
					for (int i = 0; i < itemMergeVOs.length; i++) {
						isMod = (new UFDouble(i).mod(new UFDouble(
								vendorVOsLength))).toString();
						if (i == 0 || "0.00000000".equals(isMod)) {
							((BillManageUI) getBillUI()).getBillListPanel()
									.getBodyBillModel().execFormula(i,
											new String[] { formulaA });
						} else {
							((BillManageUI) getBillUI()).getBillListPanel()
									.getBodyBillModel().setValueAt(null, i,
											"crowno");
						}
						((BillManageUI) getBillUI()).getBillListPanel()
								.getBodyBillModel().execFormula(i,
										new String[] { formulaB });
					}
					}
					// ֻҪѡ����һ�ι�Ӧ��,����Ͳ����ٽ��б༭
					convertIbillstatusToString();
					updateListVo();
					((BillManageUI) getBillUI()).getBillListPanel().updateUI();
				}
			}

		}

		// �б���ʾ���ݴ���
		if (((BillManageUI) getBillUI()).isListPanelSelected() && getBufferData().getVOBufferSize() > 0) {
		  updateBufferForAsk();
			((BillManageUI) getBillUI()).getBillListPanel().getHeadBillModel()
					.setRowState(getBufferData().getCurrentRow(),
							BillModel.SELECTED);
			((BillManageUI) getBillUI()).getBillListPanel().getHeadTable()
					.setRowSelectionInterval(getBufferData().getCurrentRow(),
							getBufferData().getCurrentRow());
			((BillManageUI) getBillUI()).getBillListPanel().updateUI();
		}

	}

  /**
   * ǰ̨���ݵ���̨ʱ��Ĭ�ϵ�½����ͱ���
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param modelVo
   * <p>
   * @author zhouxiao
   * @time 2007-2-2 ����08:59:38
   */
  protected void setInfoForConvertForBack(AggregatedValueObject modelVo) {
    ((AskbillHeaderVO) modelVo.getParentVO()).setLogdate(((AskAndQuoteUI) getBillUI())
        .getLogDate());
    ((AskbillHeaderVO) modelVo.getParentVO()).setCcurrencytypeiddefault(((AskAndQuoteUI) getBillUI())
        .getCurrtypeID());
  }

	/**
	 * �������ӵĴ��� �������ڣ�(2002-12-23 12:43:15)
	 */
	public void onBoAdd(ButtonObject bo) throws Exception {
		// �ָ����������ֶεĿɱ༭
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("crowno")
				.setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
				"cinventorycode").setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("cvendorname")
				.setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vfree")
				.setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel()
				.getBodyItem("dreceivedate").setEdit(true);
		if (getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
				"vreveiveaddress") != null) {
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"vreveiveaddress").setEdit(true);
		}
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vmemo")
				.setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("ntaxrate")
				.setEdit(true);
		setColumnEditable();
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cdeptid")
				.setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cemployeeid")
				.setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"ccurrencytypeid").setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"ctermprotocolid").setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_purorg")
				.setEdit(true);
		
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("dclosedate")
				.setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmemo")
				.setEdit(true);
		// ״̬��Ϊ����
		String ibillStatus = StatusConvert.getStatusIndexNum()
				.get(new Integer(IAskBillStatus.FREE)).toString();
		getBillCardPanelWrapper().getBillCardPanel().getTailItem("ibillstatus")
				.setValue(ibillStatus);
		// ѯ����Ĭ��Ϊ��ǰ��½��
		UIRefPane nRefPanel = (UIRefPane) getBillCardPanelWrapper()
				.getBillCardPanel().getTailItem("caskpsn").getComponent();
		nRefPanel.setPK(getUserId());
		nRefPanel.setEnabled(false);
		// ���ñ���Ĭ��ֵΪ��λ��
		UIRefPane currtype = (UIRefPane) getBillCardPanelWrapper()
				.getBillCardPanel().getHeadItem("ccurrencytypeid")
				.getComponent();
		currtype.setPK(((AskAndQuoteUI) getBillUI()).getCurrtypeID());
		
		// ��Ӧ�̼��䱨����Ϣ������
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("cvendorname")
				.setEnabled(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("nquoteprice")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
				"nquotetaxprice").setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("deliverdays")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("dvaliddate")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel()
				.getBodyItem("dinvaliddate").setEdit(false);
		
		super.onBoAdd(bo);
		onBoLineAddForEasyUse();
		getBillCardPanelWrapper().getBillCardPanel()
		.getHeadItem("vaskbillcode").setEnabled(true); 
		setOperatorID();
		setButtonsStateEdit();
	}

	/**
	 * ����ָ�������ݡ� �������ڣ�(2004-1-11 11:23:25)
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	private void updateListVo() throws java.lang.Exception {
		CircularlyAccessibleValueObject vo = null;
		if (getBufferData().getCurrentVO() != null) {
			vo = getBufferData().getCurrentVO().getParentVO();
			((BillManageUI) getBillUI()).getBillListWrapper().updateListVo(vo,
					getBufferData().getCurrentRow());

		}
	}

	/**
	 * ���ܣ�ˢ���빺������(������)������,������岻Ϊ����ˢ�� ���ߣ����� ���ڣ�(2003-2-13 9:10:31)
	 * 
	 * @return nc.vo.pr.pray.nc.vo.pp.ask.AskbillVO[]
	 * @param item
	 *            nc.vo.pr.pray.nc.vo.pp.ask.AskbillVO[]
	 */
	public nc.vo.pp.ask.AskbillMergeVO[] getRefreshedVOs(
			nc.vo.pp.ask.AskbillMergeVO[] vos) throws Exception {
		if (vos == null || vos.length <= 0)
			return vos;
		Hashtable hBody = new Hashtable();
		Hashtable header = new Hashtable();
		ArrayList aryPara = new ArrayList();
		Vector vHid = new Vector(), vHts = new Vector();
		String[] saHid = null, saHts = null;
		String key = null;
		AskbillMergeVO askbillMergeVO = null;
		AskbillItemMergeVO[] allItems = null;
		try {
			for (int i = 0; i < vos.length; i++) {
				if (vos[i] == null || vos[i].getParentVO() == null
						|| vos[i].getParentVO().getPrimaryKey() == null
						|| vos[i].getParentVO().getAttributeValue("ts") == null)
					continue;
				// �����VO�����Ѿ����ص�UI�˹��������²�ѯ
				if (vos[i].getChildrenVO() != null
						&& vos[i].getChildrenVO().length > 0)
					continue;
				vHid.addElement(vos[i].getParentVO().getPrimaryKey());
				vHts.addElement((String) vos[i].getParentVO()
						.getAttributeValue("ts"));
			}
			//
			if (vHid.size() > 0) {
				saHid = new String[vHid.size()];
				vHid.copyInto(saHid);
				saHts = new String[vHts.size()];
				vHts.copyInto(saHts);
				aryPara.add(saHid);
				aryPara.add(saHts);
				Vector aryQueryRslt = AskHelper.queryAllBodys(aryPara);
				Vector v = new Vector();
				Vector bodyV = null;
				if (aryQueryRslt != null && aryQueryRslt.size() > 0) {
          Vector temp = null;
					for (int i = 0; i < aryQueryRslt.size(); i++) {
            temp = (Vector)aryQueryRslt.get(i);
            setInfoForConvertForFront(temp);
						askbillMergeVO = AskbillMergeVO.convertVOTOFront((Vector) aryQueryRslt
								.get(i));
						 //����������
						setVfree(askbillMergeVO);
						bodyV = new Vector();
						header.put(askbillMergeVO.getParentVO()
								.getPrimaryKey(), askbillMergeVO.getParentVO());
						if (askbillMergeVO != null
								&& askbillMergeVO.getChildrenVO() != null
								&& askbillMergeVO.getChildrenVO().length > 0) {
							for (int j = 0; j < askbillMergeVO.getChildrenVO().length; j++) {
								v.add(askbillMergeVO.getChildrenVO()[j]);
								if (hBody.containsKey(askbillMergeVO
										.getParentVO().getPrimaryKey())) {
									bodyV = (Vector) hBody.get(askbillMergeVO
											.getParentVO().getPrimaryKey());
									bodyV
											.add(askbillMergeVO.getChildrenVO()[j]);
									hBody.put(askbillMergeVO.getParentVO()
											.getPrimaryKey(), bodyV);
								} else {
									bodyV
											.add(askbillMergeVO.getChildrenVO()[j]);
									hBody.put(askbillMergeVO.getParentVO()
											.getPrimaryKey(), bodyV);
								}
							}
						}
					}
					if (v.size() > 0) {
						allItems = new AskbillItemMergeVO[v.size()];
						v.copyInto(allItems);
					}
					// �������л���
					AskbillItemMergeVO[] items = null;
					for (int i = 0; i < vos.length; i++) {
						if (vos[i].getParentVO() == null
								|| vos[i].getParentVO().getPrimaryKey() == null
								|| ""
										.equals(vos[i].getParentVO().getPrimaryKey().trim()))
							continue;
						// �����VO�����Ѿ����ص�UI�˹��������²�ѯ
						if (vos[i].getChildrenVO() != null
								&& vos[i].getChildrenVO().length > 0)
							continue;
						key = vos[i].getParentVO().getPrimaryKey();
						bodyV = (Vector) hBody.get(key);

						if (bodyV != null && bodyV.size() > 0) {
							items = new AskbillItemMergeVO[bodyV.size()];
							bodyV.copyInto(items);
						}
						vos[i].setChildrenVO(items);
						vos[i].setParentVO((CircularlyAccessibleValueObject)header.get(key));
					}
				}
			}
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			throw e;
		}
		return vos;
	}

  /**
   * ��̨���ݲ�ѯ��ǰ̨ʱ��Ĭ�ϵ�½����ͱ���
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param temp
   * <p>
   * @author zhouxiao
   * @time 2007-2-2 ����09:27:48
   */
  protected void setInfoForConvertForFront(Vector temp) {
    ((AskbillHeaderVO)(temp.get(0))).setLogdate(((AskAndQuoteUI)getBillUI()).getLogDate());
    ((AskbillHeaderVO)(temp.get(0))).setCcurrencytypeiddefault(((AskAndQuoteUI) getBillUI()).getCurrtypeID());
  }

	/**
	 * ��ťm_boReturn���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoReturn() throws Exception {
		((AskAndQuoteUI) getBillUI()).getManagePane().remove(((AskAndQuoteUI) getBillUI()).getBillCardWrapper().getBillCardPanel());
		  ((AskAndQuoteUI) getBillUI()).getManagePane().add(((AskAndQuoteUI) getBillUI()).getBillListPanel(),BorderLayout.CENTER);
		  ((AskAndQuoteUI) getBillUI()).setCurrentPanel(BillTemplateWrapper.LISTPANEL);
		((BillManageUI) getBillUI()).getBillCardPanel().setVisible(false);
		((BillManageUI) getBillUI()).getBillCardPanel().setEnabled(false);
		((BillManageUI) getBillUI()).getBillListPanel().setVisible(true);
		((BillManageUI) getBillUI()).getBillListPanel().setEnabled(false);
		if (getBufferData() != null && getBufferData().getVOBufferSize() > 0) {
			getBufferData().updateView();
		}
		ArrayList refVOs = null;
		if (((BillUIRefCacheForPP) (((AskAndQuoteUI) getBillUI())
				.getCacheData())) != null
				&& ((BillUIRefCacheForPP) (((AskAndQuoteUI) getBillUI())
						.getCacheData())) != null) {
			refVOs = ((BillUIRefCacheForPP) (((AskAndQuoteUI) getBillUI())
					.getCacheData())).getCurRefCache();
		}
		if (refVOs != null && refVOs.size() > 0) {
			((BillUIRefCacheForPP) (((AskAndQuoteUI) getBillUI())
					.getCacheData())).setCurRefCacheClear();
			((BillManageUI) getBillUI()).getBillListPanel().setHeaderValueVO(
					null);
			((BillManageUI) getBillUI()).getBillListPanel().getBodyBillModel()
					.setBodyDataVO(null);

		}
		if (getBufferData() == null
				|| (getBufferData() != null && getBufferData()
						.getVOBufferSize() == 0)) {// ��ת��

			if (getBufferData().isVOBufferEmpty()) {
				getBillUI().setBillOperate(IBillOperate.OP_INIT);

			} else {
				getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
				getBufferData().setCurrentRow(getBufferData().getCurrentRow());
			}
			setButtonsStateBrowse();
			((BillManageUI) getBillUI()).getBillListPanel().getHeadBillModel()
					.clearBodyData();
			((BillManageUI) getBillUI()).getBillListPanel().getBodyBillModel()
					.clearBodyData();
			((BillManageUI) getBillUI()).getBillListPanel().updateUI();
		} else {
			((BillManageUI) getBillUI()).getBillListPanel().setHeaderValueVO(
					getBufferData().getAllHeadVOsFromBuffer());
			((BillManageUI) getBillUI()).getBillListPanel().getHeadBillModel()
					.execLoadFormula();
			((BillManageUI) getBillUI()).getBillListPanel().getBodyBillModel()
					.setBodyDataVO(
							getBufferData().getCurrentVO().getChildrenVO());
             //��ʾ������Դ��Ϣ
			nc.ui.pu.pub.PuTool.loadSourceInfoAll(((AskAndQuoteUI)getBillUI()).getBillListPanel(), nc.vo.scm.pu.BillTypeConst.PO_ASK);
			
			((BillManageUI) getBillUI()).getBillListPanel().getBodyBillModel()
					.execLoadFormula();
			((BillManageUI) getBillUI()).getBillListPanel().getHeadBillModel()
					.updateValue();
			((BillManageUI) getBillUI()).getBillListPanel().getBodyBillModel()
					.updateValue();

			// �б���ʾ���ݴ���
			((BillManageUI) getBillUI()).getBillListPanel().getHeadBillModel()
					.setRowState(getBufferData().getCurrentRow(),
							BillModel.SELECTED);
			((BillManageUI) getBillUI()).getBillListPanel().getHeadTable()
					.setRowSelectionInterval(getBufferData().getCurrentRow(),
							getBufferData().getCurrentRow());
			((BillManageUI) getBillUI())
					.setBillOperate(IBillOperate.OP_NOTEDIT);
			((BillManageUI) getBillUI()).getBillListPanel().updateUI();
		}

		// �ֶ�������ť�߼�
		if (getBillUI().getBillOperate() == IBillOperate.OP_REFADD) {
			setButtonsStateListFromBills();
		} else {
			setButtonsStateListNormal();
		}
		((BillManageUI) getBillUI())
		.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH022")/*@res "�б���ʾ"*/);
	}

	/**
	 * ��ťm_boCard���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoCard() throws Exception {
		((AskAndQuoteUI) getBillUI()).getManagePane().remove(((AskAndQuoteUI) getBillUI()).getBillListWrapper().getBillListPanel());
		  ((AskAndQuoteUI) getBillUI()).getManagePane().add(((AskAndQuoteUI) getBillUI()).getBillCardPanel(),BorderLayout.CENTER);
		((AskAndQuoteUI) getBillUI())
				.setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		if(getBufferData() != null && getBufferData().getVOBufferSize() > 0){
			getBufferData().updateView();
		}
		setBillVOToCard();
		((BillManageUI) getBillUI())
		.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH021")/*@res "��Ƭ��ʾ"*/);
	}

	/**
	 * @���ܣ�1.�ѵ���״̬����ת���ɴ� ���� 0 - >SENDING ����; ���� 1 - >QUOTE ����. 2.���б�״̬�ֶθ�ֵ
	 * @���ߣ����� �������ڣ�(2001-6-14 20:53:41)
	 */
	private void convertIbillstatusToString() {

		if (getBufferData().getVOBufferSize() == 0)
			return;
		for (int i = 0; i < getBufferData().getVOBufferSize(); i++) {
			if (((AskbillHeaderVO) getBufferData().getVOByRowNo(i)
					.getParentVO()).getIbillstatus().equals(
					new Integer(IAskBillStatus.FREE).toString())) {
				((BillManageUI) getBillUI()).getBillListWrapper()
						.getBillListPanel().getHeadBillModel().setValueAt(
                StatusConvert
										.getStatusIndexNum()
										.get(new Integer(IAskBillStatus.FREE)),
								i, "ibillstatus");
			} else if (((AskbillHeaderVO) getBufferData().getVOByRowNo(i)
					.getParentVO()).getIbillstatus().equals(
					new Integer(IAskBillStatus.SENDING).toString())) {
				((BillManageUI) getBillUI())
						.getBillListWrapper()
						.getBillListPanel()
						.getHeadBillModel()
						.setValueAt(
                StatusConvert
										.getStatusIndexNum()
										.get(
												new Integer(
														IAskBillStatus.SENDING)),
								i, "ibillstatus");
			} else if (((AskbillHeaderVO) getBufferData().getVOByRowNo(i)
					.getParentVO()).getIbillstatus().equals(
					new Integer(IAskBillStatus.QUOTED).toString())) {
				((BillManageUI) getBillUI())
						.getBillListWrapper()
						.getBillListPanel()
						.getHeadBillModel()
						.setValueAt(
                StatusConvert
										.getStatusIndexNum().get(
												new Integer(
														IAskBillStatus.QUOTED)),
								i, "ibillstatus");
			} else if (((AskbillHeaderVO) getBufferData().getVOByRowNo(i)
					.getParentVO()).getIbillstatus().equals(
					new Integer(IAskBillStatus.CONFIRM).toString())) {
				((BillManageUI) getBillUI())
						.getBillListWrapper()
						.getBillListPanel()
						.getHeadBillModel()
						.setValueAt(
                StatusConvert
										.getStatusIndexNum()
										.get(
												new Integer(
														IAskBillStatus.CONFIRM)),
								i, "ibillstatus");
			}
		}
	}
	/**
	 * @���ܣ����б�ǰѡ�е���д�뿨Ƭ
	 * @���ߣ����� �������ڣ�(2001-6-8 15:12:34)
	 */
	public void setBillVOToCard() throws BusinessException {
		// �ر�״̬�ı��������
		AskbillMergeVO billvo = null;
		if (getBufferData().getVOBufferSize() > 0) {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
							"UPP40040701-000046")/* @res "���ѯ�۵�" */);
			try {
				// ��������
				try {
//					((AskbillMergeVO) getBufferData()
//					.getCurrentVO()).setChildrenVO(null);
					billvo = getRefreshedVO((AskbillMergeVO) getBufferData()
							.getCurrentVO());
					getBufferData().setCurrentVO(billvo);
				} catch (Exception e) {
					if (e instanceof BusinessException)
						MessageDialog
								.showErrorDlg(
										getBillUI(),
										nc.ui.ml.NCLangRes.getInstance()
												.getStrByID("SCMCOMMON",
														"UPPSCMCommon-000422")/*
																				 * @res
																				 * "ҵ���쳣"
																				 */,
										e.getMessage());
					throw e;
				}
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().clearBodyData();
				// ����������
				Vector vTemp = new Vector();
				AskbillItemMergeVO[] itemMergeVOs = (AskbillItemMergeVO[])getBufferData().getCurrentVO()
				.getChildrenVO(); 
		        if(itemMergeVOs != null && itemMergeVOs.length > 0){
		            for(int j = 0; j < itemMergeVOs.length; j++){
		                if(itemMergeVOs[j].getVfree1() != null || itemMergeVOs[j].getVfree2() != null || 
		                		itemMergeVOs[j].getVfree3() != null || itemMergeVOs[j].getVfree4() != null ||
		                		itemMergeVOs[j].getVfree5() != null) {
		                	vTemp.addElement(itemMergeVOs[j]);}
		                }
		            }
		    if(vTemp.size() > 0){
		    	AskbillItemMergeVO[] askItems = new AskbillItemMergeVO[vTemp.size()];
		        vTemp.copyInto(askItems);
		        new nc.ui.scm.pub.FreeVOParse().setFreeVO(askItems, "vfree","vfree","cbaseid","cmangid",true);
		    }
				
				getBillCardPanelWrapper().getBillCardPanel().setBillValueVO(
						getBufferData().getCurrentVO());

				// ������������򶳽�ȱ����������ʾ����
				PpTool.loadBDData(getBillCardPanelWrapper().getBillCardPanel());
                // �����Զ�����
				AskbillHeaderVO voHead = (AskbillHeaderVO) getBufferData()
						.getCurrentVO().getParentVO();
				String[] saKey = new String[] { "vdef1", "vdef2", "vdef3",
						"vdef4", "vdef5", "vdef6", "vdef7", "vdef8", "vdef9",
						"vdef10", "vdef11", "vdef12", "vdef13", "vdef14",
						"vdef15", "vdef16", "vdef17", "vdef18", "vdef19",
						"vdef20" };
				int iLen = saKey.length;
				for (int i = 0; i < iLen; i++) {
					javax.swing.JComponent component = getBillCardPanelWrapper()
							.getBillCardPanel().getHeadItem(saKey[i])
							.getComponent();
					if (component instanceof UIRefPane) {
						voHead.setAttributeValue(saKey[i],
								((UIRefPane) component).getUITextField()
										.getText());
					}
				}
				getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
				/* �����Զ��������� */
				nc.ui.scm.pub.def.DefSetTool.updateQueryConditionClientUserDef(
						m_dlgIqQueryCondition, getCorpId(),ScmConst.PO_AskBill, "po_askbill.vdef",
						"po_askbill_bb1.vbdef");
				PuTool.loadSourceInfoAll(getBillCardPanelWrapper()
						.getBillCardPanel(), BillTypeConst.PO_ASK);
				/* ��ʾ��ע */
				if(billvo != null && getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane){
					UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmemo").getComponent();
					nRefPanel3.setValue(((AskbillHeaderVO)billvo.getParentVO()).getVmemo());
				}				
				getBillCardPanelWrapper().getBillCardPanel().getBillModel()
				.execLoadFormula();
				getBillCardPanelWrapper().getBillCardPanel().updateValue();
				getBillCardPanelWrapper().getBillCardPanel().updateUI();
				
               			
			} catch (Exception e) {
				SCMEnv.out(e.getMessage());
				if (e instanceof BusinessException)
					throw (BusinessException) e;
			}
		} else {
			try {
				if (getBillCardPanelWrapper().getBillCardPanel().getBillData() != null) {
					getBillCardPanelWrapper().getBillCardPanel().getBillData()
							.clearViewData();
					clearHeadTailItemsValue(getBillCardPanelWrapper()
							.getBillCardPanel());
				}
				getBillCardPanelWrapper().getBillCardPanel().getTailItem(
						"ibillstatus").setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getTailItem(
						"ibillstatus").setEdit(false);
			} catch (Exception e) {
				SCMEnv.out("���ñ�ͷ��βΪ��ʱ����");
				SCMEnv.out(e.getMessage());
				PuTool.outException(e);
			}
		}
		setButtonsStateBrowse();

	    
	}
	/**
	 * @���ܣ����б�ǰѡ�е���д�뿨Ƭ
	 * @���ߣ����� �������ڣ�(2001-6-8 15:12:34)
	 */
	public void dispCurrentVO(AskbillMergeVO billvo) throws BusinessException {
			try {
				// ����������
				Vector vTemp = new Vector();
				AskbillItemMergeVO[] itemMergeVOs = (AskbillItemMergeVO[])billvo.getChildrenVO(); 
		        if(itemMergeVOs != null && itemMergeVOs.length > 0){
		            for(int j = 0; j < itemMergeVOs.length; j++){
		                if(itemMergeVOs[j].getVfree1() != null || itemMergeVOs[j].getVfree2() != null || 
		                		itemMergeVOs[j].getVfree3() != null || itemMergeVOs[j].getVfree4() != null ||
		                		itemMergeVOs[j].getVfree5() != null) {
		                	vTemp.addElement(itemMergeVOs[j]);}
		                }
		            }
		    if(vTemp.size() > 0){
		    	AskbillItemMergeVO[] askItems = new AskbillItemMergeVO[vTemp.size()];
		        vTemp.copyInto(askItems);
		        new nc.ui.scm.pub.FreeVOParse().setFreeVO(askItems, "vfree","vfree","cbaseid","cmangid",true);
		    }
				getBillCardPanelWrapper().getBillCardPanel().setBillValueVO(
						billvo);

				// ������������򶳽�ȱ����������ʾ����
				PpTool.loadBDData(getBillCardPanelWrapper().getBillCardPanel());
                //��ʾ��Դ��Ϣ
				nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanelWrapper().getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_ASK);
				// �����Զ�����
				AskbillHeaderVO voHead = (AskbillHeaderVO)billvo.getParentVO();
				String[] saKey = new String[] { "vdef1", "vdef2", "vdef3",
						"vdef4", "vdef5", "vdef6", "vdef7", "vdef8", "vdef9",
						"vdef10", "vdef11", "vdef12", "vdef13", "vdef14",
						"vdef15", "vdef16", "vdef17", "vdef18", "vdef19",
						"vdef20" };
				int iLen = saKey.length;
				for (int i = 0; i < iLen; i++) {
					javax.swing.JComponent component = getBillCardPanelWrapper()
							.getBillCardPanel().getHeadItem(saKey[i])
							.getComponent();
					if (component instanceof UIRefPane) {
						voHead.setAttributeValue(saKey[i],
								((UIRefPane) component).getUITextField()
										.getText());
					}
				}
				getBillCardPanelWrapper().getBillCardPanel()
						.execHeadLoadFormulas();

				PuTool.loadSourceInfoAll(getBillCardPanelWrapper()
						.getBillCardPanel(), BillTypeConst.PO_ASK);
				/* �����Զ��������� */
				nc.ui.scm.pub.def.DefSetTool.updateQueryConditionClientUserDef(
						m_dlgIqQueryCondition, getCorpId(),ScmConst.PO_Invoice,"po_askbill.vdef",
						"po_askbill_b.vdef");
			} catch (Exception e) {
				SCMEnv.out(e.getMessage());
				if (e instanceof BusinessException)
					throw (BusinessException) e;
			}
			getBillCardPanelWrapper().getBillCardPanel().updateValue();
			getBillCardPanelWrapper().getBillCardPanel().getBillModel()
					.updateValue();
		    getBillCardPanelWrapper().getBillCardPanel().updateUI();
	}
	/**
	 * ���ܣ���յ��ݿ�Ƭ��ͷ������Ŀ
	 */
	private void clearHeadTailItemsValue(BillCardPanel cardPnl) {

		if (cardPnl == null)
			return;
		// ��ձ�ͷ��Ŀ(��������)
		BillItem[] billItems = cardPnl.getHeadItems();
		int iLen = 0;
		if (billItems != null) {
			iLen = billItems.length;
			for (int i = 0; i < iLen; i++) {
				if (billItems[i] != null && billItems[i].isShow()
						&& !"ccurrencytypeid".equals(billItems[i].getKey())) {
					billItems[i].setValue(null);
				}
			}
		}
		// ��ձ�����Ŀ(������״̬��)
		billItems = cardPnl.getTailItems();
		if (billItems != null) {
			iLen = billItems.length;
			for (int i = 0; i < iLen; i++) {
				if (billItems[i] != null && billItems[i].isShow()
						&& !"ibillstatus".equals(billItems[i].getKey())) {
					billItems[i].setValue(null);
				}
			}
		}

	}

	/**
	 * ���ܣ���ȡ���б�����빺��(���ŵ���) ���ߣ����� ���ڣ�(2003-2-13 9:10:31)
	 * 
	 * @return AskbillVO
	 * @param AskbillVO
	 */
	public AskbillMergeVO getRefreshedVO(AskbillMergeVO vo) throws Exception {
		AskbillMergeVO[] vos = null;
		try {
			vos = getRefreshedVOs(new AskbillMergeVO[] { vo });
		} catch (Exception e) {
			throw e;
		}
		if (vos != null && vos.length > 0)
			return vos[0];
		return vo;
	}

	/**
	 * ��ťm_boCopy���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoCopy() throws Exception {
		((BillManageUI) getBillUI())
				.setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		// �������
		AggregatedValueObject copyVo = getBillUI().getVOFromUI();
		//���ñ�ע
		if( getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane ){
			UIRefPane nRefPanel = (UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmemo").getComponent();
			UITextField vMemoField = nRefPanel.getUITextField();
			((AskbillHeaderVO)copyVo.getParentVO()).setVmemo(vMemoField.getText());
		}
		// ����������մ���
		copyVo.getParentVO().setPrimaryKey(null);
		((AskbillHeaderVO) copyVo.getParentVO()).setVaskbillcode(null);
		((AskbillHeaderVO) copyVo.getParentVO()).setTs(null);
		((AskbillHeaderVO) copyVo.getParentVO()).setCquotepsn(null);
		((AskbillHeaderVO) copyVo.getParentVO()).setDquotedate(null);
		((AskbillHeaderVO) copyVo.getParentVO()).setTmaketime(null);
		((AskbillHeaderVO) copyVo.getParentVO()).setTlastmaketime(null);
		((AskbillHeaderVO) copyVo.getParentVO())
				.setIbillstatus(StatusConvert
						.getStatusIndexNum().get(
								new Integer(IAskBillStatus.FREE)).toString());
		((AskbillHeaderVO) copyVo.getParentVO()).setDaskdate(new UFDate(getLogDate()));
		((AskbillHeaderVO) copyVo.getParentVO()).setCaskpsn(getUserId());
		AskbillItemMergeVO[] items = clearChildPk(copyVo.getChildrenVO());
		copyVo.setChildrenVO(items);
		// ����Ϊ��������
		getBillUI().setBillOperate(IBillOperate.OP_ADD);
		// ���ý�������
		getBillUI().setCardUIData(copyVo);
		setButtonsStateEdit();
		// ѯ���۵��ı�ͷ��Ŀ���ɱ༭
		BillItem[] headItems = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItems();
		if (headItems != null && headItems.length > 0) {
			BillItem headItem = null;
			for (int i = 0; i < headItems.length; i++) {
				headItem = headItems[i];
				headItem.setEdit(true);
			}
		}
			   getBillCardPanelWrapper().getBillCardPanel().getBodyItem("cinventorycode").setEdit(true);
			   getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vfree").setEdit(true);
			   getBillCardPanelWrapper().getBillCardPanel().getBodyItem("ntaxrate").setEdit(true);
			   setColumnEditable();
			   getBillCardPanelWrapper().getBillCardPanel().getBodyItem("dreceivedate").setEdit(true);
		  
		 
        //������������ʾ����
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanelWrapper().getBillCardPanel(), copyVo);
	}

	/**
	 * ����ӱ������� �������ڣ�(2004-2-25 19:59:34)
	 * 
	 * @param vos
	 *            nc.vo.pub.CircularlyAccessibleValueObject[]
	 */
	private AskbillItemMergeVO[] clearChildPk(
			CircularlyAccessibleValueObject[] vos) throws Exception {
		Vector v = new Vector();
		AskbillItemMergeVO[] items = null;
		if (vos == null || vos.length == 0)
			return null;
		for (int i = 0; i < vos.length; i++) {
			vos[i].setPrimaryKey(null);
			// ��Ӧ�̼��䱨����Ϣ������
			((AskbillItemMergeVO) vos[i]).setCaskbill_b1id(null);
			((AskbillItemMergeVO) vos[i]).setCaskbill_bb1id(null);
			((AskbillItemMergeVO) vos[i]).setCvendorbaseid(null);
			((AskbillItemMergeVO) vos[i]).setCvendormangid(null);
			((AskbillItemMergeVO) vos[i]).setNquoteprice(null);
			((AskbillItemMergeVO) vos[i]).setNquotetaxprice(null);
			((AskbillItemMergeVO) vos[i]).setDvaliddate(null);
			((AskbillItemMergeVO) vos[i]).setDinvaliddate(null);
			((AskbillItemMergeVO) vos[i]).setDeliverdays(null);
			((AskbillItemMergeVO) vos[i]).setBTs(null);
			((AskbillItemMergeVO) vos[i]).setHTs(null);
			((AskbillItemMergeVO) vos[i]).setTs(null);
			//��Դ��Ϣ������
			((AskbillItemMergeVO) vos[i]).setCupsourcebillid(null);
			((AskbillItemMergeVO) vos[i]).setCupsourcebillrowid(null);
			((AskbillItemMergeVO) vos[i]).setCupsourcebilltype(null);
			((AskbillItemMergeVO) vos[i]).setCsourcebillid(null);
			((AskbillItemMergeVO) vos[i]).setCsourcebillrowid(null);
			((AskbillItemMergeVO) vos[i]).setCsourcebilltype(null);
			
			if (((AskbillItemMergeVO) vos[i]).getCinventorycode() != null
					&& ((AskbillItemMergeVO) vos[i]).getCinventorycode()
							.trim().length() > 0) {
				v.add(vos[i]);
			}
		}
		if (v.size() > 0) {
			items = new AskbillItemMergeVO[v.size()];
			v.copyInto(items);
		}
		return items;
	}

	/**
	 * ��ťm_boSelAll���ʱִ�еĶ���, ���б�Ҫ���븲��.
	 */
	protected void onBoSelAll() throws Exception {
		int iLen = ((BillManageUI) getBillUI()).getBillListWrapper()
				.getBillListPanel().getHeadBillModel().getRowCount();
		((BillManageUI) getBillUI()).getBillListWrapper().getBillListPanel()
				.getHeadTable().setRowSelectionInterval(0, iLen - 1);
		for (int i = 0; i < iLen; i++) {
			((BillManageUI) getBillUI()).getBillListWrapper()
					.getBillListPanel().getHeadBillModel().setRowState(i,
							BillModel.SELECTED);
		}
		setButtonsStateList();
		((BillManageUI) getBillUI()).getBillListWrapper()
		.getBillListPanel().getBodyBillModel().clearBodyData();
	}

	/**
	 * ��ťm_boSelNone���ʱִ�еĶ���, ���б�Ҫ���븲��.
	 */
	protected void onBoSelNone() throws Exception {
		int iLen = ((BillManageUI) getBillUI()).getBillListWrapper()
				.getBillListPanel().getHeadTable().getRowCount();
		((BillManageUI) getBillUI()).getBillListWrapper().getBillListPanel()
				.getHeadTable().removeRowSelectionInterval(0, iLen - 1);
		for (int i = 0; i < iLen; i++) {
			((BillManageUI) getBillUI()).getBillListWrapper()
					.getBillListPanel().getHeadBillModel().setRowState(i,
							BillModel.NORMAL);
		}
		setButtonsStateList();
		((BillManageUI) getBillUI()).getBillListWrapper()
		.getBillListPanel().getBodyBillModel().clearBodyData();
	}

	/**
	 * ���ߣ���ӡ�� ���ܣ��������״̬�İ�ť������ �������� ���أ��� ���⣺�� ���ڣ�(2002-5-15 11:39:21)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-10-31 wyf ������ļ�������ť�Ŀ��� 2002-11-01 wyf
	 * �޸ĵ�ǰ�޵���ʱ�Ŀ�ָ�����
	 */
	protected void setButtonsStateBrowse() {
		// ά����ť���߼�
		vendorSelect = ((AskAndQuoteUI) getBillUI()).getButtonManager()
		.getButton(IAskAndQuote.VendorSelect);
		try {
			AggregatedValueObject billVO = getBillUI().getVOFromUI();
			AskbillItemMergeVO[] itemMergeVOs = (AskbillItemMergeVO[]) billVO
			.getChildrenVO();
			String cmangidTemp = null;
			for(int i = 0 ; i < itemMergeVOs.length; i ++){
				cmangidTemp = itemMergeVOs[i].getCmangid();
				if(cmangidTemp != null && cmangidTemp.trim().length() > 0){
					break;
				}
			}
			if(cmangidTemp != null && cmangidTemp.trim().length() > 0){
				vendorSelect.setEnabled(true);
			}else{
				vendorSelect.setEnabled(false);
			}
		} catch (Exception e1) {
      SCMEnv.out(e1);
		}
		
		
		billToExcelDef = ((AskAndQuoteUI) getBillUI()).getButtonManager()
				.getButton(IAskAndQuote.BILLTOEXCELDEF);
		billToExcelDefault = ((AskAndQuoteUI) getBillUI()).getButtonManager()
				.getButton(IAskAndQuote.BILLTOEXCELDEFAULT);
		excelToBill = ((AskAndQuoteUI) getBillUI()).getButtonManager()
				.getButton(IAskAndQuote.EXCELTOBILL);
		del = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IBillButton.Del);
		first = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IBillButton.First);
		next = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IBillButton.Next);
		prev = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IBillButton.Prev);
		Last = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IBillButton.Last);
		setButtonsStateCardMaintain();
		String NAME_HEADVO = "nc.vo.pp.ask.AskbillHeaderVO";
		AskbillHeaderVO voHeader = (AskbillHeaderVO) getBillCardPanelWrapper()
				.getBillCardPanel().getBillData().getHeaderValueVO(NAME_HEADVO);
		String ibillstatus = voHeader.getIbillstatus();
		if (ibillstatus != null
				&& ibillstatus.trim().length() > 0
				&& StatusConvert.getStatusIndexNum().get(
						new Integer(IAskBillStatus.CONFIRM)).toString().equals(
						ibillstatus)) {

			billToExcelDef.setEnabled(false);
			billToExcelDefault.setEnabled(false);
			excelToBill.setEnabled(false);
		} else if(ibillstatus == null
				|| ibillstatus.trim().length() == 0){
			billToExcelDef.setEnabled(false);
			billToExcelDefault.setEnabled(false);
			excelToBill.setEnabled(false);
		} else {
			billToExcelDef.setEnabled(true);
			billToExcelDefault.setEnabled(true);
			excelToBill.setEnabled(true);
		}
		if (getBufferData() == null
				|| (getBufferData() != null && getBufferData()
						.getVOBufferSize() == 0)) {
			first.setEnabled(false);
			next.setEnabled(false);
			prev.setEnabled(false);
			Last.setEnabled(false);
		} else if (getBufferData() != null
				&& getBufferData().getVOBufferSize() == 1) {
			first.setEnabled(false);
			next.setEnabled(false);
			prev.setEnabled(false);
			Last.setEnabled(false);
		} else if (getBufferData() != null) {
			if (getBufferData().getVOBufferSize() > 1) {
				if (getBufferData().getCurrentRow() == getBufferData()
						.getVOBufferSize() - 1) {
					first.setEnabled(true);
					next.setEnabled(false);
					prev.setEnabled(true);
					Last.setEnabled(true);
				} else if (getBufferData().getCurrentRow() < getBufferData()
						.getVOBufferSize() - 1) {
					first.setEnabled(true);
					next.setEnabled(true);
					prev.setEnabled(true);
					Last.setEnabled(true);
				} else if (getBufferData().getCurrentRow() == 0) {
					first.setEnabled(true);
					next.setEnabled(true);
					prev.setEnabled(false);
					Last.setEnabled(true);
				}

			}
		}
		try {
			((AskAndQuoteUI) getBillUI()).updateButtonUI();
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PuTool.outException(e);
		}
	}

	/**
	 * ���ߣ����� ���ܣ����ñ༭ʱ�İ�ť״̬ �������� ���أ��� ���⣺�� ���ڣ�(2002-6-5 11:39:21)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-10-31 wyf ������ļ�������ť�Ŀ���
	 */
	protected void setButtonsStateEdit() {
		line = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IBillButton.Line);

		String ibillStatus = getBillCardPanelWrapper().getBillCardPanel()
				.getTailItem("ibillstatus").getValue();
		if (ibillStatus != null
				&& ibillStatus
						.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000340")/* @res "����" */)) {
			line.setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().setBBodyMenuShow(true);
		} else if (ibillStatus != null
				&& (ibillStatus
						.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4004070101", "UPT4004070101-000022")/*
																		 * @res
																		 * "����"
																		 */)
				|| ibillStatus
						.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4004070101", "UPP4004070101-000050")/*
																		 * @res
																		 * "����"
																		 */))) {// ѯ���۵�״̬Ϊ�����򱨼�
			line.setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().setBBodyMenuShow(false);
		}
		try {
			((AskAndQuoteUI) getBillUI()).updateButtonUI();
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PuTool.outException(e);
		}

	}

	/**
	 * ���ߣ����� ���ܣ����ö����б�İ�ť״̬ �������� ���أ��� ���⣺�� ���ڣ�(2002-6-5 11:39:21)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void setButtonsStateList() {

		if (getBillUI().getBillOperate() == IBillOperate.OP_REFADD) {
			setButtonsStateListFromBills();
		} else {
			setButtonsStateListNormal();
		}

		try {
			((BillManageUI) getBillUI()).updateButtonUI();
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PuTool.outException(e);
		}
	}

	/**
	 * ���ߣ����� ���ܣ�������������ת��ʱ���б�ť״̬ �������� ���أ��� ���⣺�� ���ڣ�(2002-6-5 11:39:21)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private void setButtonsStateListFromBills() {

		int iSelectedCount = getHeadSelectedCount();
		selAllList = ((AskAndQuoteUI) getBillUI()).getButtonManager()
				.getButton(IBillButton.SelAll);
		selNoneList = ((AskAndQuoteUI) getBillUI()).getButtonManager()
				.getButton(IBillButton.SelNone);
		listCancelTranform = ((AskAndQuoteUI) getBillUI()).getButtonManager()
				.getButton(IAskAndQuote.LISTCANCELTRANSFORM);
		editList = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IAskAndQuote.EDITLIST);
		delList = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IAskAndQuote.DELLIST);
		queryList = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IBillButton.Query);
		reFreshList = ((AskAndQuoteUI) getBillUI()).getButtonManager()
				.getButton(IBillButton.Refresh);
		toCard = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IBillButton.Card);
		docManagerlist = ((AskAndQuoteUI) getBillUI()).getButtonManager()
				.getButton(IAskAndQuote.DOCMANGERLIST);
		printlist = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IAskAndQuote.PRINTLIST);
		printprelist = ((AskAndQuoteUI) getBillUI()).getButtonManager()
				.getButton(IAskAndQuote.PRINTPRELIST);

		if (iSelectedCount == 0
				|| ((AskAndQuoteUI) getBillUI()).getCacheData().isEmpty()) {
			editList.setEnabled(false);

			// ֻ��һ�ŵ���ʱ��ȫѡ����
			if (getBufferData().getVOBufferSize() == 1) {
				selAllList.setEnabled(true);
				selNoneList.setEnabled(false);
			} else {
				selNoneList.setEnabled(true);
				selAllList.setEnabled(false);
			}
			// ����ת��
			listCancelTranform.setVisible(false);
			listCancelTranform.setEnabled(false);
		} else {
			if (iSelectedCount == 1) {
				editList.setEnabled(true);
				// conQuery.setEnabled(true);
			} else {
				editList.setEnabled(false);
				// conQuery.setEnabled(false);
			}

			selAllList.setEnabled(true);
			selNoneList.setEnabled(false);
			// ����ת��
			listCancelTranform.setVisible(true);
			listCancelTranform.setEnabled(true);
		}
		// ��ѯ
		queryList.setEnabled(false);
		// ����
		delList.setEnabled(false);
		// delList.setVisible(false);
		// mainList.setEnabled(false);
		// ˢ��
		reFreshList.setEnabled(false);
		// �л�
		toCard.setEnabled(false);
		// �ĵ�����
		docManagerlist.setEnabled(false);

		// ��ӡ
		printlist.setEnabled(false);
		printprelist.setEnabled(false);
	}

	/**
	 * ���ߣ����� ���ܣ����ö����б�İ�ť״̬ �������� ���أ��� ���⣺�� ���ڣ�(2002-6-5 11:39:21)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-10-17 wyf �޸Ķ���Ķ��������޸ĵ����� 2002-02-24 wyf
	 * ����Դ��ڲ����ĵ��ݵİ�ť����
	 */
	private void setButtonsStateListNormal() {

		// ѡ�е���
		int iSelectedCount = getHeadSelectedCount();
		ButtonObject mantain = ((AskAndQuoteUI) getBillUI()).getButtonManager()
		.getButton(IAskAndQuote.MAINTENANCEList);
		selAllList = ((AskAndQuoteUI) getBillUI()).getButtonManager()
				.getButton(IBillButton.SelAll);
		selNoneList = ((AskAndQuoteUI) getBillUI()).getButtonManager()
				.getButton(IBillButton.SelNone);
		listCancelTranform = ((AskAndQuoteUI) getBillUI()).getButtonManager()
				.getButton(IAskAndQuote.LISTCANCELTRANSFORM);
		editList = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IAskAndQuote.EDITLIST);
		delList = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IAskAndQuote.DELLIST);
		queryList = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IBillButton.Query);
		reFreshList = ((AskAndQuoteUI) getBillUI()).getButtonManager()
				.getButton(IBillButton.Refresh);
		toCard = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IBillButton.Card);
		docManagerlist = ((AskAndQuoteUI) getBillUI()).getButtonManager()
				.getButton(IAskAndQuote.DOCMANGERLIST);
		printlist = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IAskAndQuote.PRINTLIST);
		printprelist = ((AskAndQuoteUI) getBillUI()).getButtonManager()
				.getButton(IAskAndQuote.PRINTPRELIST);
		billToExcelDefaultList = ((AskAndQuoteUI) getBillUI())
				.getButtonManager().getButton(
						IAskAndQuote.BILLTOEXCELDEFAULTLIST);
		billToExcelDeflist = ((AskAndQuoteUI) getBillUI()).getButtonManager()
				.getButton(IAskAndQuote.BILLTOEXCELDEFLIST);
		excelToBillList = ((AskAndQuoteUI) getBillUI()).getButtonManager()
				.getButton(IAskAndQuote.EXCELTOBILLLIST);
		stockQuyList = ((AskAndQuoteUI) getBillUI()).getButtonManager()
		.getButton(IAskAndQuote.STOCKQUYLIST);
		mantain.setEnabled(true);
		delList.setVisible(true);
		if (getBufferData().getVOBufferSize() == 0) {
			// û�ж���
			selAllList.setEnabled(false);
			selNoneList.setEnabled(false);
			delList.setEnabled(false);
			editList.setEnabled(false);
			toCard.setEnabled(true);
			docManagerlist.setEnabled(false);
			stockQuyList.setEnabled(false);
			// ��ӡ
			printlist.setEnabled(false);
			printprelist.setEnabled(false);
			// ���뵼��
			billToExcelDefaultList.setEnabled(false);
			billToExcelDeflist.setEnabled(false);
			excelToBillList.setEnabled(false);
		} else {
			// �ж���
			if (iSelectedCount == 0) {
				// δѡ�ж���
				selAllList.setEnabled(true);
				selNoneList.setEnabled(false);
				delList.setEnabled(false);
				editList.setEnabled(false);
				toCard.setEnabled(false);
				// ���뵼��
				billToExcelDefaultList.setEnabled(false);
				billToExcelDeflist.setEnabled(false);
				excelToBillList.setEnabled(false);
			} else {
				// ȫ������
				delList.setEnabled(true);
				// ȫѡ
				if (iSelectedCount == getBufferData().getVOBufferSize()) {
					selAllList.setEnabled(false);
					selNoneList.setEnabled(true);
				} else {
					selAllList.setEnabled(true);
					selNoneList.setEnabled(false);
				}

				// ѡ��һ�Ŷ���
				if (iSelectedCount == 1) {
					// ��˷������ڼ��ر������У�������֪�Ƿ���ڲ�����ֻ��ֱ�ӵ���VO�������ټ���
					// �Ƿ�����л�
					AskbillMergeVO voCur = (AskbillMergeVO) getBufferData()
							.getVOByRowNo(
									getHeadSelectedVOPoses()[0].intValue());
					if (voCur.getChildrenVO() == null
							|| (voCur.getChildrenVO() != null && voCur
									.getChildrenVO().length == 0)) {
						toCard.setEnabled(false);
					} else {
						toCard.setEnabled(true);
					}
					// �Ƿ�����޸ġ�����
					String iBillStatus = ((AskbillHeaderVO) voCur.getParentVO())
							.getIbillstatus();
					if (iBillStatus != null
							&& StatusConvert
									.getStatusIndexNum()
									.get(new Integer(IAskBillStatus.CONFIRM)) != null
							&& StatusConvert
									.getStatusIndexNum()
									.get(new Integer(IAskBillStatus.CONFIRM))
									.toString().trim().length() > 0
							&& iBillStatus.equals(StatusConvert
									.getStatusIndexNum()
									.get(new Integer(IAskBillStatus.CONFIRM))
									.toString())) {
						// ����,����δͨ��
						editList.setEnabled(false);
						delList.setEnabled(false);
					} else {
						if (voCur.getChildrenVO() != null
								&& voCur.getChildrenVO().length > 0) {
							if (voCur.getParentVO().getAttributeValue("vaskbillcode") == null
									|| voCur.getParentVO().getAttributeValue("vaskbillcode")
									.toString().trim().length() == 0){
								delList.setEnabled(false);        
							} else{
								delList.setEnabled(true);
							}
							editList.setEnabled(true);
						} else {
							editList.setEnabled(false);
							delList.setEnabled(false);
						}
					}
				} else {
					AskbillMergeVO voCur = (AskbillMergeVO) getBufferData()
					.getVOByRowNo(
							getHeadSelectedVOPoses()[0].intValue());
					if (voCur.getChildrenVO() != null
							&& voCur.getChildrenVO().length > 0) {
						if (voCur.getParentVO().getAttributeValue("vaskbillcode") == null
								|| voCur.getParentVO().getAttributeValue("vaskbillcode")
								.toString().trim().length() == 0){
							delList.setEnabled(false);        
						} else{
							delList.setEnabled(true);
						}
					}else {
						delList.setEnabled(true);
					}
					editList.setEnabled(false);
					toCard.setEnabled(false);
				}
				// ��ӡ
				printlist.setEnabled(true);
				printprelist.setEnabled(true);
				// ���뵼��
				billToExcelDefaultList.setEnabled(false);
				billToExcelDeflist.setEnabled(false);
				excelToBillList.setEnabled(false);
			}

			// �ĵ�����
			if (iSelectedCount == 0) {
				docManagerlist.setEnabled(false);
				stockQuyList.setEnabled(false);
			} else {
				docManagerlist.setEnabled(true);
				stockQuyList.setEnabled(true);
			}
		}
		// ��ѯ
		queryList.setEnabled(true);
		// ����ת��
		if (listCancelTranform != null) {
			listCancelTranform.setVisible(false);
		}
	}

	/**
	 * ���ߣ����� ���ܣ��õ��б�����±�ͷѡ�е��� �������� ���أ��� ���⣺�� ���ڣ�(2002-4-22 11:39:21)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2003-03-10 wyf �޸�ȡѡ�е��еķ���
	 */
	public int getHeadSelectedCount() {
		if (((AskAndQuoteUI) getBillUI()).getBillListPanel().getHeadBillModel()
				.getRowCount() == 0) {
			return 0;
		}

		int[] iaSelectedRow = ((AskAndQuoteUI) getBillUI()).getBillListPanel()
				.getHeadTable().getSelectedRows();
		if (iaSelectedRow == null) {
			return 0;
		} else {
			return iaSelectedRow.length;
		}
	}

	/**
	 * ���ߣ����� ���ܣ��õ��б�����±�ͷѡ�е�������Ӧ������VO��λ�ã����Ѱ�VO�е�λ�ô�С���� �������� ���أ��� ���⣺��
	 * ���ڣ�(2003-11-05 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public Integer[] getHeadSelectedVOPoses() {

		// �����ϵĶ���
		HashMap htmapIndex = new HashMap();

		int[] iaSelectedRowCount = ((AskAndQuoteUI) getBillUI())
				.getBillListPanel().getHeadTable().getSelectedRows();
		int iLen = iaSelectedRowCount.length;
		for (int i = 0; i < iLen; i++) {
			// int iVOPos = getVOPos(iaSelectedRowCount[i]);
			int iVOPos = iaSelectedRowCount[i];
			htmapIndex.put(new Integer(iVOPos), "");
		}

		TreeMap trmapIndex = new TreeMap(htmapIndex);
		Integer[] iaIndex = (Integer[]) trmapIndex.keySet().toArray(
				new Integer[iLen]);

		return iaIndex;
	}

	/**
	 * ���ߣ����� ���ܣ����ݸ��������� ����VO�������±� ������int iUIRow ������ ���أ�int ������Ӧ��VO�±꣬�༴����ǰ���±�
	 * ���⣺�� ���ڣ�(2001-05-22 13:24:16) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public int getVOPos(int iUIRow) {

		return PuTool.getIndexBeforeSort(((AskAndQuoteUI) getBillUI())
				.getBillCardPanel(), iUIRow);
	}

	/**
	 * ���ߣ����� ���ܣ�����ת��δ��ɵĵ��� �������� ���أ��� ���⣺�� ���ڣ�(2003-03-31 13:24:16)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private void onListCancelTransform() {
		try {
			
			((BillUIRefCacheForPP) (((AskAndQuoteUI) getBillUI())
					.getCacheData())).setCurRefCacheClear();
			((AskAndQuoteUI) getBillUI()).setListRefCacheData(null);
			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
			((AskAndQuoteUI) getBillUI()).getBillListPanel()
			.getHeadBillModel().clearBodyData();
	        ((AskAndQuoteUI) getBillUI()).getBillListPanel()
			.getBodyBillModel().clearBodyData();
			// ��ʾԭ�л����һ�ŵ���
			if (getBufferData().getVOBufferSize() > 0) {
				getBufferData().setCurrentRow(
						getBufferData().getVOBufferSize() - 1);
			} else {
				((AskAndQuoteUI) getBillUI()).getBillListPanel()
						.getHeadBillModel().clearBodyData();
				((AskAndQuoteUI) getBillUI()).getBillListPanel()
						.getBodyBillModel().clearBodyData();
			}
			if (((BillManageUI) getBillUI()).isListPanelSelected()) {// �б�

				if (getBufferData().getVOBufferSize() > 0) {
					getBufferData().updateView();
					((BillManageUI) getBillUI()).getBillListPanel().setHeaderValueVO(
							getBufferData().getAllHeadVOsFromBuffer());
					((BillManageUI) getBillUI()).getBillListPanel().getHeadBillModel()
							.execLoadFormula();
					((BillManageUI) getBillUI()).getBillListPanel().getBodyBillModel()
							.setBodyDataVO(
									getBufferData().getCurrentVO().getChildrenVO());
		             //��ʾ������Դ��Ϣ
					nc.ui.pu.pub.PuTool.loadSourceInfoAll(((AskAndQuoteUI)getBillUI()).getBillListPanel(), nc.vo.scm.pu.BillTypeConst.PO_ASK);
					
					((BillManageUI) getBillUI()).getBillListPanel().getBodyBillModel()
							.execLoadFormula();
					((BillManageUI) getBillUI()).getBillListPanel().getHeadBillModel()
							.updateValue();
					((BillManageUI) getBillUI()).getBillListPanel().getBodyBillModel()
							.updateValue();

					// �б���ʾ���ݴ���
					((BillManageUI) getBillUI()).getBillListPanel().getHeadBillModel()
							.setRowState(getBufferData().getCurrentRow(),
									BillModel.SELECTED);
					((BillManageUI) getBillUI()).getBillListPanel().getHeadTable()
							.setRowSelectionInterval(getBufferData().getCurrentRow(),
									getBufferData().getCurrentRow());
					((BillManageUI) getBillUI())
							.setBillOperate(IBillOperate.OP_NOTEDIT);
				}
				setButtonsStateListNormal();
			} else {
				setButtonsStateBrowse();
			}
			onBoRefresh();
			((AskAndQuoteUI) getBillUI()).updateButtonUI();
			((AskAndQuoteUI) getBillUI()).updateUI();
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PuTool.outException(e);
		}
	}

	/**
	 * ���ý������ݣ���ִ��������ظ��෽�� �������ڣ�(2004-4-1 8:20:25)
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	protected void setRefDataForSave(AggregatedValueObject[] vos)
			throws java.lang.Exception {
		AggregatedValueObject[] splitVos = vos;
		if (splitVos == null || (splitVos != null && splitVos.length == 0)) {// ��ת��
			((BillUIRefCacheForPP) ((AskAndQuoteUI) getBillUI()).getCacheData())
					.setCurRefCacheClear();
			((AskAndQuoteUI) getBillUI())
					.setCurrentPanel(BillTemplateWrapper.CARDPANEL);
			getBillCardPanelWrapper().getBillCardPanel().setVisible(true);
			getBillCardPanelWrapper().getBillCardPanel().setEnabled(false);
			if (getBufferData().isVOBufferEmpty()) {
				getBillUI().setBillOperate(IBillOperate.OP_INIT);
			} else {
				getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
				getBufferData().setCurrentRow(getBufferData().getCurrentRow());
				getBufferData().updateView();
			}
             //��ʾ������Դ��Ϣ
			nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanelWrapper().getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_ASK);
			getBillCardPanelWrapper().getBillCardPanel().updateUI();
			setButtonsStateBrowse();
			
		} else {// ��ת��
			if (splitVos.length == 1) {
				// ���õ���״̬
				getBillUI().setCardUIState();
				// ������
				getBillUI().setCardUIData(splitVos[0]);
				// �����û�����
				// getBillUI().setUserObject(getBusiSplit().getUserObj(splitVos[0]));
				// ����Ϊ��������
				getBillUI().setBillOperate(IBillOperate.OP_REFADD);
				setColumnEditableForConvertBill();
				setButtonsStateEdit();
			} else {
				// ���ò�������
				((AskAndQuoteUI) getBillUI()).getBillListPanel()
						.getParentListPanel().removeTableSortListener();
				((AskAndQuoteUI) getBillUI())
						.setCurrentPanel(BillTemplateWrapper.LISTPANEL);
				((BillUIRefCacheForPP) ((AskAndQuoteUI) getBillUI())
						.getCacheData()).setCurRefCacheClear();
				((AskAndQuoteUI) getBillUI()).setListRefCacheData(splitVos);
				((AskAndQuoteUI) getBillUI()).getBillListPanel()
						.getHeadBillModel().setRowState(0, BillModel.SELECTED);
				((BillUIRefCacheForPP) ((AskAndQuoteUI) getBillUI())
						.getCacheData()).setSelectedRow(0);
				((AskAndQuoteUI) getBillUI())
						.getBillListPanel()
						.getBodyBillModel()
						.setBodyDataVO(
								((BillUIRefCacheForPP) ((AskAndQuoteUI) getBillUI())
										.getCacheData()).getCurrentCacheVO()
										.getChildrenVO());
				((AskAndQuoteUI) getBillUI())
						.getBillListPanel()
						.getHeadTable()
						.setRowSelectionInterval(
								((BillUIRefCacheForPP) ((AskAndQuoteUI) getBillUI())
										.getCacheData()).getSelectedRow(),
								((BillUIRefCacheForPP) ((AskAndQuoteUI) getBillUI())
										.getCacheData()).getSelectedRow());
				((AskAndQuoteUI) getBillUI())
				.getBillListPanel().updateUI();
				// ����Ϊ��������
				(((AskAndQuoteUI) getBillUI()))
						.setSplitBillOperate(IBillOperate.OP_REFADD);
				setButtonsStateList();
			}
		}
	}

	/**
	 * �õ��ļ�ѡ���
	 * 
	 * @return javax.swing.JFileChooser
	 */
	protected javax.swing.JFileChooser getChooser() {
		if (m_chooser == null) {
			m_chooser = new UIFileChooser();
			// ��ȥ��ǰ���ļ�������
			m_chooser.removeChoosableFileFilter(m_chooser.getFileFilter());
			// ��ʾ��ѡȡ�İ����ļ���Ŀ¼
			m_chooser.setFileSelectionMode(m_chooser.DIRECTORIES_ONLY);
		}
		return m_chooser;
	}

	/**
	 * �õ�ѡ�е�Ŀ¼
	 * 
	 * @return java.lang.String
	 */
	public String getDefaultDir() {

		String sDir = null;
		try {
			nc.ui.pp.pub.PropertyCtrl ctrl = new nc.ui.pp.pub.PropertyCtrl();
			sDir = ctrl
					.getItemValueByKey(nc.ui.pp.pub.IPropertyFile.ExcelBarcode_FileName_Param2);
			if (sDir == null) {
				java.io.File file = new java.io.File(
						nc.ui.pp.pub.IPropertyFile.ExcelBarcode_FileName_Param2_Value);
				if (!file.exists())
					file.mkdir();
				sDir = file.getAbsolutePath();
			}
		} catch (Exception ex) {
			SCMEnv.out(ex.getMessage());
			PuTool.outException(ex);
		}
		return sDir;

	}

	/**
	 * ���ݵ���Excel �������ڣ�(2003-09-28 9:51:50)
	 */
	public void onBillExcel(int iFlag) {
		ExcelFileVO[] vos = null;
//		AskbillItemMergeVO[] tvos = null;
//		AskbillHeaderVO tvosHead = null;
		AskbillMergeVO voBill = new AskbillMergeVO();
		String sBillCode = null;
//		String sPKCorp = null;
		String sFilePath = null;
		String sFilePathDir = null;
//		String cVendorMangid = null;
		String ibillstatus = null;
		if (iFlag == 1) {
			// ���ļ�
			if (getChooser().showDialog(
					getBillUI(),
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC001-0000044")/* @res "ȷ��" */) == javax.swing.JFileChooser.CANCEL_OPTION)
				return;
			sFilePathDir = getChooser().getSelectedFile().toString();
		} else {
			sFilePathDir = getDefaultDir();
		}

		try {
			// ����ǰ���б��Ǳ�������������
			if (getBillUI().getBillOperate() == IBillOperate.OP_NOTEDIT
					&& !((BillManageUI) getBillUI()).isListPanelSelected()) { // ���
				getBillUI().showHintMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
								"UPP40040701-000059")/* @res "���ڵ��������Ժ�..." */);
				String NAME_HEADVO = "nc.vo.pp.ask.AskbillHeaderVO";
				String NAME_ITEMVOs = "nc.vo.pp.ask.AskbillItemMergeVO";
				// ��ͷ
				AskbillHeaderVO voHeader = (AskbillHeaderVO) getBillCardPanelWrapper()
						.getBillCardPanel().getBillData().getHeaderValueVO(
								NAME_HEADVO);
				AskbillItemMergeVO[] voItems = (AskbillItemMergeVO[]) getBillCardPanelWrapper()
						.getBillCardPanel().getBillData().getBodyValueVOs(
								NAME_ITEMVOs);
				voBill.setParentVO(voHeader);
				voBill.setChildrenVO(voItems);
				ibillstatus = voHeader.getIbillstatus();
				if (ibillstatus != null
						&& ibillstatus.trim().length() > 0
						&& StatusConvert.getStatusIndexNum()
								.get(new Integer(IAskBillStatus.CONFIRM))
								.toString().equals(ibillstatus)) {

				}

				if (voBill == null) {
					getBillUI()
							.showHintMessage(
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("40040701",
													"UPP40040701-000060")/*
																			 * @res
																			 * "���Ȳ�ѯ��¼�뵥�ݡ�"
																			 */);
					return;
				}
				if (voBill.getParentVO() == null) {
					voBill.setParentVO(new AskbillHeaderVO());
				}
				if ((voBill.getChildrenVO() == null)
						|| (voBill.getChildrenVO().length == 0)
						|| (voBill.getChildrenVO()[0] == null)) {
					getBillUI()
							.showHintMessage(
									nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("40040701",
											"UPP40040701-000061")/*
																	 * @res
																	 * "����ѡ�е��ݺ����ԡ�"
																	 */);
					return;
				}
				for (AskbillItemMergeVO itemMergVO : (AskbillItemMergeVO[])voBill.getChildrenVO()) {
					if(PuPubVO.getString_TrimZeroLenAsNull(itemMergVO.getCvendorbaseid()) == null 
							|| PuPubVO.getString_TrimZeroLenAsNull(itemMergVO.getCvendormangid()) == null){
						getBillUI()
						.showHintMessage(
								nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("40040701",
										"UPP40040701-000219")/*
																 * @res
																 * "�����޹�Ӧ�̣���ѡ��Ӧ�̺��ٵ���excel��"
																 */);
						return;
					}
				}

				// �õ����ݺţ���˾
				sBillCode = voBill.getParentVO().getAttributeValue(
						"vaskbillcode") == null ? "" : voBill.getParentVO()
						.getAttributeValue("vaskbillcode").toString();
//				sPKCorp = voBill.getParentVO().getAttributeValue("pk_corp") == null ? ""
//						: voBill.getParentVO().getAttributeValue("pk_corp")
//								.toString();
//				tvos = (AskbillItemMergeVO[]) voBill.getChildrenVO();
//				tvosHead = (AskbillHeaderVO) voBill.getParentVO();
//
//				// ��Ҫ���������ݴ���VO
//				Hashtable result = new Hashtable();
				Vector v = new Vector();
//				nc.vo.pp.ask.ExcelFileVO vo = null;

				// ����ѯ����
				Object oTemp = CacheTool.getCellValue("sm_user", "cUserId",
						"user_name", voHeader.getCaskpsn());
				if (oTemp != null) {
					Object data[] = (Object[]) oTemp;
					voBill.getParentVO().setAttributeValue("caskpsnname",
							data[0].toString());
				}
				// ���ñ�����
				oTemp = CacheTool.getCellValue("sm_user", "cUserId",
						"user_name", voHeader.getCquotepsn());
				if (oTemp != null) {
					Object data[] = (Object[]) oTemp;
					voBill.getParentVO().setAttributeValue("cquotepsnname",
							data[0].toString());
				}

				// �õ���Ҫ������VO
				v = saveOutputVOs(sBillCode, new AskbillMergeVO[] { voBill });
				if (v != null && v.size() > 0) {
					nc.ui.pp.pub.ExcelReadCtrl erc = null;
					for (int k = 0; k < v.size(); k++) {
						vos = new nc.vo.pp.ask.ExcelFileVO[((Vector) v.get(k))
								.size()];
						((Vector) v.get(k)).copyInto(vos);
						// ���õ����ӿ�
						// �ļ����ƹ���Askbill+��˾PK+���ݺ�+"_"+��Ӧ��".xls"
						sFilePath = sFilePathDir + "\\" + "ѯ�۵���("
								+ vos[0].getVaskbillcode() + ")_��Ӧ��("
								+ vos[0].getCvendorname() + ").xls";
						erc = new nc.ui.pp.pub.ExcelReadCtrl();
						erc.setVOToExcel(vos, sFilePath);
					}
				}
				getBillUI().showHintMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
								"UPP40040701-000062")/* @res "�������" */);
				getBillUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
								"UPP40040701-000062")/* @res "�������" */);
			} else if (getBillUI().getBillOperate() == IBillOperate.OP_NOTEDIT
					&& ((BillManageUI) getBillUI()).isListPanelSelected()) { // �б�
				if (getBufferData() == null
						|| (getBufferData() != null && getBufferData()
								.getVOBufferSize() == 0)) {
					getBillUI()
							.showHintMessage(
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("40040701",
													"UPP40040701-000060")/*
																			 * @res
																			 * "���Ȳ�ѯ��¼�뵥�ݡ�"
																			 */);
					return;
				}

				ArrayList alBill = getSelectedBills();

				if (alBill == null || alBill.size() <= 0
						|| alBill.get(0) == null) {
					getBillUI()
							.showHintMessage(
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("40040701",
													"UPP40040701-000061")/*
																			 * @res
																			 * "����ѡ�е��ݺ����ԡ�"
																			 */);
					return;
				}
				AskbillMergeVO[] allBill = new AskbillMergeVO[alBill.size()];
				alBill.toArray(allBill);
				// ��Ҫ���������ݴ���VO
				Vector v = new Vector();
//				nc.vo.pp.ask.ExcelFileVO vo = null;
				// �õ���Ҫ������VO
				v = saveOutputVOs(sBillCode, allBill);
				// ÿ�ŵ��ݽ�����һ���ļ�
				// �Ѿ��õ�vo��Ȼ�󵼳�vo
				if (v != null && v.size() > 0) {
					nc.ui.pp.pub.ExcelReadCtrl erc = null;
					for (int k = 0; k < v.size(); k++) {
						vos = new nc.vo.pp.ask.ExcelFileVO[((Vector) v.get(k))
								.size()];
						((Vector) v.get(k)).copyInto(vos);
						// ���õ����ӿ�
						// �ļ����ƹ���Askbill+��˾PK+���ݺ�+"_"+��Ӧ��".xls"
						sFilePath = sFilePathDir + "\\" + "ѯ�۵���("
								+ vos[0].getVaskbillcode() + ")_��Ӧ��("
								+ vos[0].getCvendorname() + ").xls";
						erc = new nc.ui.pp.pub.ExcelReadCtrl();
						erc.setVOToExcel(vos, sFilePath);

					}
				}
				getBillUI().showHintMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
								"UPP40040701-000062")/* @res "�������" */);
				getBillUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
								"UPP40040701-000062")/* @res "�������" */);
			}
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
							"UPP40040701-000063")/* @res "��������" */);
			String[] value = new String[] { e.getMessage(), sFilePath };
			getBillUI().showWarningMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101",
							"UPP4004070101-000038", null, value))/*
																	 * @res
																	 * "��������"+
																	 * e.getMessage()+",�ļ�·����"+sFilePath)
																	 */;
		}
	}

	/**
	 * ����Ҫ���������� �������ڣ�(2003-09-28 9:51:50)
	 */
	private Vector saveOutputVOs(String sBillCode, AskbillMergeVO[] vos) throws Exception{
		// ��Ҫ���������ݴ���VO
		Vector v = new Vector();
		Vector vendorV = null;
		Vector tempV = new Vector();
		nc.vo.pp.ask.ExcelFileVO vo = null;
		Hashtable itemResult = null;
		Hashtable itemResultBySpecialNum = new Hashtable();
		Vector tempVForItem = new Vector();
		nc.vo.pp.ask.ExcelFileVO voForHead = null;
		AskbillItemMergeVO[] tvos = null;
		AskbillHeaderVO tvosHead = null;
		String ibillstatus = null;
		// ����Ҫ���������ݵ�vo��
		for (int j = 0; j < vos.length; j++) {
			tvosHead = (AskbillHeaderVO) vos[j].getParentVO();
			ibillstatus = tvosHead.getIbillstatus();
			if (ibillstatus != null
					&& ibillstatus.trim().length() > 0
					&& StatusConvert.getStatusIndexNum().get(
							new Integer(IAskBillStatus.CONFIRM)).toString()
							.equals(ibillstatus)) {
				continue;
			}
			tvos = (AskbillItemMergeVO[]) vos[j].getChildrenVO();
			itemResult = new Hashtable();
			vendorV = new Vector();
			for (int i = 0; i < tvos.length; i++) {

				vo = new nc.vo.pp.ask.ExcelFileVO();
				if (tvos[i].getCinventorycode() != null
						&& tvos[i].getCinventorycode().trim().length() > 0) {
					// ��������Ϣ
					vo.setAttributeValue("crowno", tvos[i].getCrowno()); // �к�
					vo.setAttributeValue("cmangid", tvos[i].getCmangid()); // �������������
					vo
							.setAttributeValue("invcode", tvos[i]
									.getCinventorycode()); // �������
					vo
							.setAttributeValue("invname", tvos[i]
									.getCinventoryname()); // �������
					vo
							.setAttributeValue("invspec", tvos[i]
									.getCinventoryspec()); // ���
					vo
							.setAttributeValue("invtype", tvos[i]
									.getCinventorytype()); // �ͺ�
					vo.setAttributeValue("cmainmeasname", tvos[i]
							.getCmainmeasname()); // ��λ
					vo.setAttributeValue("vfree0", tvos[i].getVfree()); // ������Ŀ
					// ����
					UFDouble dshouldin = null;
					UFDouble dnum = null; // Ҫ����������
					dshouldin = tvos[i].getNasknum();
					dnum = dshouldin == null ? new UFDouble(0) : dshouldin;
					vo.setAttributeValue("nasknum", dnum.toString()); // ����
					vo.setAttributeValue("ntaxrate",
							(tvos[i].getNtaxrate() == null ? "" : tvos[i]
									.getNtaxrate().toString())); // ˰��
					vo.setAttributeValue("dreceivedate", (tvos[i]
							.getDreceivedate() == null ? "" : tvos[i]
							.getDreceivedate().toString())); // ��������
					if (tvos[i].getSpecialNum() != null
							&& tvos[i].getSpecialNum().trim()
									.length() > 0 && vo != null) {
						itemResultBySpecialNum.put(tvos[i].getSpecialNum(), vo);
					}
				}

				//
				if (tvos[i].getCvendormangid() != null
						&& tvos[i].getCvendormangid().trim()
								.length() > 0) {
					if (itemResult.containsKey(tvos[i].getCvendormangid())) {
						tempVForItem = (Vector) itemResult.get(tvos[i].getCvendormangid());
						vo = new nc.vo.pp.ask.ExcelFileVO();
						ExcelFileVO voTempForItem = (ExcelFileVO) itemResultBySpecialNum
								.get(tvos[i].getSpecialNum());

						vo
								.setAttributeValue("crowno", voTempForItem
										.getRowNo()); // �к�
						vo.setAttributeValue("cmangid", voTempForItem
								.getCmangid()); // �������������
						vo.setAttributeValue("invcode", voTempForItem
								.getInvcode()); // �������
						vo.setAttributeValue("invname", voTempForItem
								.getInvname()); // �������
						vo.setAttributeValue("invspec", voTempForItem
								.getInvspec()); // ���
						vo.setAttributeValue("invtype", voTempForItem
								.getInvtype()); // �ͺ�
						vo.setAttributeValue("cmainmeasname", voTempForItem
								.getCmainmeasname()); // ��λ
						vo.setAttributeValue("vfree", voTempForItem
								.getVfree0()); // ������Ŀ
                        //����
						UFDouble dnum = null; // Ҫ����������
						String dshouldin = voTempForItem.getNasknum();
						dnum = dshouldin == null ? new UFDouble(0) : new UFDouble(dshouldin);
						vo.setAttributeValue("nasknum", dnum.toString()); // ����
						vo.setAttributeValue("ntaxrate",
								(voTempForItem.getTaxratio() == null ? "" : voTempForItem.getTaxratio().trim())); // ˰��
						vo.setAttributeValue("dreceivedate", (tvos[i]
								.getDreceivedate() == null ? "" : tvos[i]
								.getDreceivedate().toString())); // ��������

						// ������Ϣ
						vo.setAttributeValue("nquoteprice", (tvos[i]
								.getNquoteprice() == null ? "" : tvos[i]
								.getNquoteprice().toString())); // ��˰����
						vo.setAttributeValue("nquotetaxprice", (tvos[i]
								.getNquotetaxprice() == null ? "" : tvos[i]
								.getNquotetaxprice().toString())); // ��˰����
						vo.setAttributeValue("deliverdays", (tvos[i]
								.getDeliverdays() == null ? "" : tvos[i]
								.getDeliverdays().toString())); // ������
						vo.setAttributeValue("dvaliddate", (tvos[i]
								.getDvaliddate() == null ? "" : tvos[i]
								.getDvaliddate().toString())); // ������Ч����
						vo.setAttributeValue("dinvaliddate", (tvos[i]
								.getDinvaliddate() == null ? "" : tvos[i]
								.getDinvaliddate().toString())); // ����ʧЧ����
						vo.setAttributeValue("caskbillid", tvos[i]
								.getCaskbillid()); // ��ͷpk
						vo.setAttributeValue("caskbill_bid", tvos[i]
								.getCaskbill_bid()); // �ӱ�pk
						vo.setAttributeValue("caskbill_bb1id", tvos[i]
								.getCaskbill_bb1id()); // ���ӱ�pk

						tempVForItem.add(vo);
					} else {
						tempVForItem = new Vector();

						if (tvosHead != null) {
							voForHead = new nc.vo.pp.ask.ExcelFileVO();

							voForHead.setAttributeValue("vaskbillcode",
									tvosHead.getVaskbillcode()); // ���ݺ�

							voForHead.setAttributeValue("cvendorname", tvos[i]
									.getCvendorname()); // ��Ӧ��
							voForHead.setAttributeValue("cvendorbaseid",
									tvos[i].getCvendorbaseid()); // ��Ӧ�̻�������ID
							voForHead.setAttributeValue("cvendormangid",
									tvos[i].getCvendormangid()); // ��Ӧ�̹�����ID

							voForHead.setAttributeValue("ccurrencytypeid",
									tvosHead.getCcurrencytypeid()); // ԭ�ұ���ID
							voForHead.setAttributeValue("ccurrencytypename",
									tvosHead.getCcurrencytypename()); // ԭ�ұ���

							voForHead.setAttributeValue("dclosedate", (tvosHead
									.getDclosedate() == null ? "" : tvosHead
									.getDclosedate().toString())); // ���۽�������

							voForHead.setAttributeValue("caskpsn", tvosHead
									.getCaskpsn()); // ѯ����ID
							voForHead.setAttributeValue("caskpsnname", tvosHead
									.getCaskpsnname()); // ѯ����ID
							voForHead.setAttributeValue("daskdate", (tvosHead
									.getDaskdate() == null ? "" : tvosHead
									.getDaskdate().toString())); // ѯ������

						}
						tempVForItem.add(voForHead);
						vo = new nc.vo.pp.ask.ExcelFileVO();
						ExcelFileVO voTempForItem = (ExcelFileVO) itemResultBySpecialNum
								.get(tvos[i].getSpecialNum());

						vo
								.setAttributeValue("crowno", voTempForItem
										.getRowNo()); // �к�
						vo.setAttributeValue("cmangid", voTempForItem
								.getCmangid()); // �������������
						vo.setAttributeValue("invcode", voTempForItem
								.getInvcode()); // �������
						vo.setAttributeValue("invname", voTempForItem
								.getInvname()); // �������
						vo.setAttributeValue("invspec", voTempForItem
								.getInvspec()); // ���
						vo.setAttributeValue("invtype", voTempForItem
								.getInvtype()); // �ͺ�
						vo.setAttributeValue("cmainmeasname", voTempForItem
								.getCmainmeasname()); // ��λ
						vo.setAttributeValue("vfree", voTempForItem
								.getVfree0()); // ������Ŀ
						vo.setAttributeValue("nasknum", voTempForItem
								.getNasknum()); // ����
						vo.setAttributeValue("ntaxrate", voTempForItem
								.getTaxratio()); // ˰��
						vo.setAttributeValue("dreceivedate", voTempForItem
								.getDreceivedate()); // ��������

						// ������Ϣ
						vo.setAttributeValue("nquoteprice", (tvos[i]
								.getNquoteprice() == null ? "" : tvos[i]
								.getNquoteprice().toString())); // ��˰����
						vo.setAttributeValue("nquotetaxprice", (tvos[i]
								.getNquotetaxprice() == null ? "" : tvos[i]
								.getNquotetaxprice().toString())); // ��˰����
						vo.setAttributeValue("deliverdays", (tvos[i]
								.getDeliverdays() == null ? "" : tvos[i]
								.getDeliverdays().toString())); // ������
						vo.setAttributeValue("dvaliddate", (tvos[i]
								.getDvaliddate() == null ? "" : tvos[i]
								.getDvaliddate().toString())); // ������Ч����
						vo.setAttributeValue("dinvaliddate", (tvos[i]
								.getDinvaliddate() == null ? "" : tvos[i]
								.getDinvaliddate().toString())); // ����ʧЧ����

						// vo.setAttributeValue("billtype", "a"); //��������
						vo.setAttributeValue("caskbillid", tvos[i]
								.getCaskbillid()); // ��ͷpk
						vo.setAttributeValue("caskbill_bid", tvos[i]
								.getCaskbill_bid()); // �ӱ�pk
						vo.setAttributeValue("caskbill_bb1id", tvos[i]
								.getCaskbill_bb1id()); // ���ӱ�pk
						tempVForItem.add(vo);

						vendorV.add(tvos[i].getCvendormangid());
					}
					itemResult.put(tvos[i].getCvendormangid(), tempVForItem);
				}

			}
			if (vendorV != null && vendorV.size() > 0) {
				for (int k = 0; k < vendorV.size(); k++) {
					tempV = new Vector();
					tempV = (Vector) itemResult.get(vendorV.get(k));
					if (tempV != null && tempV.size() > 0) {
						v.add(tempV);
					}
				}
			}
		}

		return v;
	}

	/**
	 * �ֹ�Ӧ�̴�ӡ���ݲ�� �������ڣ�(2003-09-28 9:51:50)
	 * 
	 * @throws Exception
	 */
	private ArrayList getDataForPrintByVendor() throws Exception {
		AskbillMergeVO vo = (AskbillMergeVO) getBillUI().getVOFromUI();
		// ��Ҫ���������ݴ���VO
		Vector tempV = null;

		AskbillItemMergeVO[] tvosItems = null;
		AskbillHeaderVO tvosHead = null;
		tvosHead = (AskbillHeaderVO) vo.getParentVO();
		tvosItems = (AskbillItemMergeVO[]) vo.getChildrenVO();

		Hashtable invInfo = new Hashtable();
		Hashtable vendorInfo = new Hashtable();
		Hashtable result = new Hashtable();
		Vector vendorV = new Vector();
		AskbillItemMergeVO item = null;
		AskbillItemMergeVO itemT = null;

		// �ֹ�Ӧ�̴�ӡ���ݲ��
		for (int i = 0; i < tvosItems.length; i++) {

			item = new AskbillItemMergeVO();
			if (tvosItems[i].getCinventorycode() != null
					&& tvosItems[i].getCinventorycode().length() > 0) {
				if (tvosItems[i].getSpecialNum() != null
						&& tvosItems[i].getSpecialNum().trim().length() > 0
						&& !invInfo.containsKey(tvosItems[i].getSpecialNum())) {
					invInfo.put(tvosItems[i].getSpecialNum(), tvosItems[i]);
				}
			}
			if (tvosItems[i].getCvendormangid() != null
					&& tvosItems[i].getCvendormangid().length() > 0) {
				if (!vendorInfo.containsKey(tvosItems[i].getCvendormangid())) {
					vendorV.add(tvosItems[i].getCvendormangid());
					vendorInfo.put(tvosItems[i].getCvendormangid(),
							tvosItems[i].getCvendormangid());
				}
			}
			// �����Ϣ
			if (tvosItems[i].getCinventorycode() != null
					&& tvosItems[i].getCinventorycode().length() > 0) {
				itemT = new AskbillItemMergeVO();
				itemT = tvosItems[i];
			} else {
				itemT = new AskbillItemMergeVO();
				itemT = (AskbillItemMergeVO) invInfo.get(tvosItems[i]
						.getSpecialNum());
			}
			item.setAttributeValue("crowno", itemT.getCrowno()); // �к�
			item.setAttributeValue("cmangid", itemT.getCmangid()); // �������������
			item.setAttributeValue("cinventorycode", itemT.getCinventorycode()); // �������
			item.setAttributeValue("cinventoryname", itemT.getCinventoryname()); // �������
			item.setAttributeValue("cinventoryspec", itemT.getCinventoryspec()); // ���
			item.setAttributeValue("cinventorytype", itemT.getCinventorytype()); // �ͺ�
			item.setAttributeValue("cmainmeasname", itemT.getCmainmeasname()); // ��λ
			item.setAttributeValue("vfree", itemT.getVfree()); // ������Ŀ
			// ����
			UFDouble dshouldin = null;
			UFDouble dnum = null; // Ҫ����������
			dshouldin = itemT.getNasknum();
			dnum = dshouldin == null ? new UFDouble(0) : dshouldin;
			item.setNasknum(dnum);// ����
			item.setNtaxrate(itemT.getNtaxrate());// ˰��
			item.setDreceivedate(itemT.getDreceivedate());// ��������

			// ������Ϣ
			if (tvosItems[i].getCvendormangid() != null
					&& tvosItems[i].getCvendormangid().length() > 0) {
				if (!result.containsKey(tvosItems[i].getCvendormangid())) {
					tempV = new Vector();

				} else {
					tempV = (Vector) result
							.get(tvosItems[i].getCvendormangid());
				}

				tempV.add(item);
				result.put(tvosItems[i].getCvendormangid(), tempV);
                item.setCvendormangid(tvosItems[i].getCvendormangid());
                item.setCvendorbaseid(tvosItems[i].getCvendorbaseid());
                item.setCvendorname(tvosItems[i].getCvendorname());
				item.setNquoteprice(tvosItems[i].getNquoteprice());
				item.setNquotetaxprice(tvosItems[i].getNquotetaxprice());
				item.setDeliverdays(tvosItems[i].getDeliverdays());
				item.setDvaliddate(tvosItems[i].getDvaliddate());
				item.setDinvaliddate(tvosItems[i].getDinvaliddate());

			}

		}
		// ��֯���ݷ���
		AskbillMergeVO[] mergeVOs = null;
		if (vendorV != null && vendorV.size() > 0) {
			AskbillMergeVO mergeVO = null;
			AskbillItemMergeVO[] mergeItems = null;
			Vector temp = new Vector();
			for (int i = 0; i < vendorV.size(); i++) {
				tempV = new Vector();
				if (vendorV.get(i) != null) {
					tempV = (Vector) result.get((String) vendorV.get(i));
					if (tempV.size() > 0) {
						mergeItems = new AskbillItemMergeVO[tempV.size()];
						tempV.copyInto(mergeItems);
						mergeVO = new AskbillMergeVO();
						mergeVO.setParentVO(tvosHead);
						mergeVO.setChildrenVO(mergeItems);
						temp.add(mergeVO);
					}
				}
			}
			if (temp.size() > 0) {
				mergeVOs = new AskbillMergeVO[temp.size()];
				temp.copyInto(mergeVOs);
			}
		}

		ArrayList aryRslt = new ArrayList();
		if (mergeVOs != null) {
			for (int i = 0; i < mergeVOs.length; i++) {
				aryRslt.add(mergeVOs[i]);
			}
		}

		return aryRslt;
	}

	/**
	 * ����Ҫ���������� �������ڣ�(2003-09-28 9:51:50)
	 */
	private Vector saveOutputVOsForSend(Vector vendorV, Vector vendorMangIdsV,
			AskbillMergeVO[] vos) {
		// ��Ҫ���������ݴ���VO
		Vector v = new Vector();
		Vector vendorVForHead = new Vector();
		Vector tempV = new Vector();
		nc.vo.pp.ask.ExcelFileVO vo = null;
		Vector tempVForItem = new Vector();
		nc.vo.pp.ask.ExcelFileVO voForHead = null;
		AskbillItemMergeVO[] tvos = null;
		AskbillHeaderVO tvosHead = null;
		tvosHead = (AskbillHeaderVO) vos[0].getParentVO();
		HashMap<String, ExcelFileVO> itemResultBySpecialNum = new HashMap<String, ExcelFileVO>();
		HashMap<String, ExcelFileVO> itemVendorSprcial = new HashMap<String, ExcelFileVO>();
		if (tvosHead != null) {
			
			for (int i = 0; i < vendorV.size(); i++) {
				voForHead = new nc.vo.pp.ask.ExcelFileVO();
				voForHead.setAttributeValue("vaskbillcode", tvosHead
						.getVaskbillcode()); // ���ݺ�
				voForHead.setAttributeValue("cvendorname", vendorV.get(i)); // ��Ӧ��
				voForHead.setAttributeValue("ccurrencytypename", tvosHead
						.getCcurrencytypename()); // ԭ�ұ���
				voForHead.setAttributeValue("dclosedate", (tvosHead
						.getDclosedate() == null ? "" : tvosHead
								.getDclosedate().toString())); // ���۽�������
				voForHead.setAttributeValue("caskpsn", tvosHead.getCaskpsn()); // ѯ����ID
				// ����ѯ����
				if(tvosHead.getCaskpsn() != null && tvosHead.getCaskpsn().trim().length() > 0){
					Object oTemp = null;
					try {
						oTemp = CacheTool.getCellValue("sm_user", "cUserId",
								"user_name", tvosHead.getCaskpsn());
					} catch (BusinessException e) {
						// TODO �Զ����� catch ��
						e.printStackTrace();
					}
					if (oTemp != null) {
						Object data[] = (Object[]) oTemp;
						voForHead.setAttributeValue("caskpsnname",
								data[0].toString());
					}
				}else{
					voForHead.setAttributeValue("caskpsnname", tvosHead
							.getCaskpsnname()); // ѯ����ID
				}
				voForHead.setAttributeValue("daskdate",
						(tvosHead.getDaskdate() == null ? "" : tvosHead
								.getDaskdate().toString())); // ѯ������
				voForHead.setAttributeValue("cvendormangid", vendorMangIdsV
						.get(i)); // ��Ӧ��ID
				vendorVForHead.add(voForHead);
			}
		}
		// ����Ҫ���������ݵ�vo��
		for (int j = 0; j < vos.length; j++) {
			tvosHead = (AskbillHeaderVO) vos[j].getParentVO();
			tvos = (AskbillItemMergeVO[]) vos[j].getChildrenVO();
			for (int i = 0; i < tvos.length; i++) {
				
				vo = new nc.vo.pp.ask.ExcelFileVO();
				if(tvos[i].getCvendormangid() != null
						&& tvos[i].getCvendormangid().trim()
						.length() > 0){
					if (tvos[i].getCinventorycode() != null
							&& tvos[i].getCinventorycode().length() > 0) {
						ExcelFileVO invVO = new nc.vo.pp.ask.ExcelFileVO();
						// ��������Ϣ
						invVO.setAttributeValue("crowno", tvos[i].getCrowno()); // �к�
						invVO.setAttributeValue("cmangid", tvos[i].getCmangid()); // �������������
						invVO
						.setAttributeValue("invcode", tvos[i]
						                                   .getCinventorycode()); // �������
						invVO
						.setAttributeValue("invname", tvos[i]
						                                   .getCinventoryname()); // �������
						invVO
						.setAttributeValue("invspec", tvos[i]
						                                   .getCinventoryspec()); // ���
						invVO
						.setAttributeValue("invtype", tvos[i]
						                                   .getCinventorytype()); // �ͺ�
						invVO.setAttributeValue("cmainmeasname", tvos[i]
						                                              .getCmainmeasname()); // ��λ
						invVO.setAttributeValue("vfree", tvos[i].getVfree()); // ������Ŀ
						// ����
						UFDouble dshouldin = null;
						UFDouble dnum = null; // Ҫ����������
						dshouldin = tvos[i].getNasknum();
						dnum = dshouldin == null ? new UFDouble(0) : dshouldin;
						invVO.setAttributeValue("nasknum", dnum.toString()); // ����
						
						itemResultBySpecialNum.put(tvos[i].getSpecialNum(), invVO);
					}
					//��������Ϣ
					vo.setAttributeValue("crowno", itemResultBySpecialNum.
							get(tvos[i].getSpecialNum()).getRowno()); // �к�
					vo.setAttributeValue("cmangid", itemResultBySpecialNum.
							get(tvos[i].getSpecialNum()).getCmangid()); // �������������
					vo.setAttributeValue("invcode", itemResultBySpecialNum.
							get(tvos[i].getSpecialNum()).getInvcode()); // �������
					vo.setAttributeValue("invname", itemResultBySpecialNum.
							get(tvos[i].getSpecialNum()).getInvname()); // �������
					vo.setAttributeValue("invspec", itemResultBySpecialNum.
							get(tvos[i].getSpecialNum()).getInvspec()); // ���
					vo.setAttributeValue("invtype", itemResultBySpecialNum.
							get(tvos[i].getSpecialNum()).getInvtype()); // �ͺ�
					vo.setAttributeValue("cmainmeasname", itemResultBySpecialNum.
							get(tvos[i].getSpecialNum()).getCmainmeasname()); // ��λ
					vo.setAttributeValue("vfree", itemResultBySpecialNum.
							get(tvos[i].getSpecialNum()).getAttributeValue("vfree")); // ������Ŀ
					vo.setAttributeValue("nasknum", itemResultBySpecialNum.
							get(tvos[i].getSpecialNum()).getNasknum()); // ����
					
					vo.setAttributeValue("ntaxrate",
							(tvos[i].getNtaxrate() == null ? "" : 
								tvos[i].getNtaxrate().toString())); // ˰��
					vo.setAttributeValue("dreceivedate", 
							(tvos[i].getDreceivedate() == null ? "" : 
								tvos[i].getDreceivedate().toString())); // ��������
					vo.setAttributeValue("caskbillid", tvos[i].getCaskbillid()); // ��ͷpk
					vo.setAttributeValue("caskbill_bid", tvos[i].getCaskbill_bid()); // �ӱ�pk
					vo.setAttributeValue("caskbill_bb1id", tvos[i].getCaskbill_bb1id()); // ���ӱ�pk
					vo.setCvendormangid(tvos[i].getCvendormangid());
					
					tempVForItem.add(vo);
					
				}
			}
			
			if (vendorV != null && vendorV.size() > 0) {
				for (int i = 0; i < vendorVForHead.size(); i++) {
					tempV = new Vector();
					if (vendorVForHead.get(i) != null) {
						tempV.add(vendorVForHead.get(i));
					}
					for (int k = 0; k < tempVForItem.size(); k++) {
						vo = new nc.vo.pp.ask.ExcelFileVO();
						vo = (ExcelFileVO) tempVForItem.get(k);
						if (vo != null && vo.getCvendormangid().equals(((ExcelFileVO)vendorVForHead.get(i)).getCvendormangid())) {
							tempV.add(vo);
						}
					}
					v.add(tempV);
				}
			}
			
		}
		
		return v;
	}

	/**
	 * ��ʾѡ���ļ�Ŀ¼
	 * 
	 */
	private void doShowDir() {
		state = getFileChooser().showOpenDialog(getBillUI());
		File f = getFileChooser().getSelectedFile();
		if (f != null && state == UIFileChooser.APPROVE_OPTION) {
			{ 
				// ��¼��ǰ�ļ���Ŀ¼
				m_currentPath = f.toString();
				// ���õ�ǰ�ļ�Ŀ¼
				m_fFilePath = f;
				
				if(m_fFilePath.isFile()){
					int pos = f.toString().lastIndexOf("\\");
					String sFilePath = null;
//					String[] s=null;
					if (pos > 0 && pos < f.toString().length() - 1)
						sFilePath = f.toString().substring(0,pos);
					gettfDirectory().setText(sFilePath);
				}else{
					gettfDirectory().setText(f.toString());
				}
				// ������ǰĿ¼�����е��ļ�
				readAllFileList();
			}
		}
	}

	/**
	 * ��ȡĿ¼�������ļ�
	 * 
	 * @return boolean
	 */
	private boolean readAllFileList() {
		//select directory or file
		if (m_fFilePath.isDirectory()){
		 m_allcurrfiles = m_fFilePath.listFiles(new Filter("xls"));
		}else if(m_fFilePath.isFile()){
		 String fileName = m_currentPath;
		 File f = new File(fileName);
		
		 m_allcurrfiles = new File[1];
		 m_allcurrfiles[0] = f;
		}
		if (m_allcurrfiles == null)
			return false;

		// ��ձ��
		getBillCardPanelWrapper().getBillCardPanel().getBillModel()
				.clearBodyData();
		
		// �Ѷ�����XLS�ļ����뵽�����
		UpLoadFileVO vo = null;
		int k = 0;
//		UpLoadFileVO[] vos = null;
		excelTOBill = new Vector();
		if (m_ht == null || m_ht.size() == 0) { // �ǹ���ʱ
			for (int i = 0; i < m_allcurrfiles.length; i++) {
				if(m_fFilePath.isDirectory() && !m_allcurrfiles[i].exists()){
					continue;
				}
				vo = getUploadCtrl().createFileVOs(m_allcurrfiles[i], i + 1);
				excelTOBill.add(vo);
			}
		} else { // �������
			String sfiletype = null;
			String sfilename = null;
			String sfiledate = null;

			if (m_ht.containsKey("filetype")) {
				sfiletype = m_ht.get("filetype").toString();
			}
			if (m_ht.containsKey("filename")) {
				sfilename = m_ht.get("filename").toString();
			}
			if (m_ht.containsKey("filedate")) {
				sfiledate = m_ht.get("filedate").toString();
			}

			for (int i = 0; i < m_allcurrfiles.length; i++) {
				if(!m_allcurrfiles[i].exists()){
					continue;
				}
				vo = getUploadCtrl().createFileVOs(m_allcurrfiles[i], k + 1);
				String sType = null;
				String sName = null;
				String sDate = null;
				sType = vo.getFileStatus();
				sName = vo.getFileName();
				sDate = vo.getFileDate();
				// �����ļ�����
				if (sfiletype != null && sfiletype.length() > 0) {
					if (!sfiletype.equals(sType)) {
						continue;
					}
				}
				// �����ļ�����
				if (sfilename != null && sfilename.length() > 0) {
					if (sName.indexOf(sfilename) == -1) {
						continue;
					}
				}
				// �����ļ�����
				if (sfiledate != null && sfiledate.length() > 0) {
					if (sDate.indexOf(sfiletype) == -1) {
						continue;
					}
				}
				excelTOBill.add(vo);
			}
		}
		return true;
	}

	/**
	 * ���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-10-11 19:40:03) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.ui.ic.pub.barcodeoffline.UpLoadCtrl
	 */
	public UpLoadCtrl getUploadCtrl() {
		if (m_UploadCtrl == null)
			m_UploadCtrl = new UpLoadCtrl(getBillUI());
		return m_UploadCtrl;
	}

	/**
	 * ���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-10-11 19:35:20) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 */
	private nc.ui.pub.beans.UITextField gettfDirectory() {
		if (ivjtfDirectory == null) {
			try {
				ivjtfDirectory = new nc.ui.pub.beans.UITextField();
				ivjtfDirectory.setName("tfDirectory");
				ivjtfDirectory.setBounds(92, 9, 634, 22);
				ivjtfDirectory.setMaxLength(500);
			} catch (java.lang.Throwable ivjExc) {
			}
		}
		return ivjtfDirectory;
	}

	/**
	 * Excel���뵽���� �������ڣ�(2003-09-28 9:51:50)
	 */
	public void onExcelBill() {
		// ����ѡ�е��ϴ����ļ�
		String[] sFileNames = null;
//		ExcelReadCtrl erc = null;
		// �����ϴ��ɹ��ĵ���
//		ArrayList askBillVOs = new ArrayList();
		// �ϴ�����VOת��ʱʹ��
//		AskbillVO[] askbillvos = null;
		// ���ļ�
		doShowDir();
		// ���ݰ�ť�����ж�
		if (state == javax.swing.JFileChooser.CANCEL_OPTION) {
			getBillUI().showWarningMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
							"UPP40040701-000074")/* @res "�ϴ�������ȡ����" */);
			return;
		} else {
			// ��ȡѡ�е��ϴ��ļ�
			sFileNames = getSelectedFiles();
			if (sFileNames == null || sFileNames.length <= 0) {
				getBillUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
								"UPP40040701-000075")/* @res "��ѡ��Ҫ�ϴ��ĵ����ļ���" */);
				if(!((BillManageUI) getBillUI()).isListPanelSelected()){
				// ��ʾ���ݡ�����ť״̬
			    getBufferData().clear();
			    getBufferData().updateView();
				}
				return;
			}
		}

		try {

			// ��ȡ�ϴ��ļ���·��
			String sPath = gettfDirectory().getText().trim();
			// �����ϴ�
			ArrayList alresult = getUploadCtrl().UpLoadFiles(getCorpId(),
					sFileNames, sPath, getUserId(), getLogDate());
			// ������Ϣ
//			String sError = (String) alresult.get(0);
			// �����ϴ����ļ�
//			ArrayList alUploadFile = (ArrayList) alresult.get(1);
			// �ϴ�ʧ�ܵ��ļ�
//			ArrayList alUploadFailFile = (ArrayList) alresult.get(2);
			// �ϴ��ɹ����ļ�
//			askBillVOs = (ArrayList) alresult.get(4);
			// �ϴ�����VOת��
//			if (askBillVOs != null && askBillVOs.size() > 0) {
//				AskbillVO askbilltempvo = new AskbillVO();
//				askbillvos = new AskbillVO[askBillVOs.size()];
//				for (int i = 0; i < askBillVOs.size(); i++) {
//					askbilltempvo = (AskbillVO) askBillVOs.get(i);
//					askbillvos[i] = askbilltempvo;
//				}
//			}
			// �ж��ļ��Ƿ��ϴ��ɹ�
			Vector isUpLoadSuccess = (Vector) alresult.get(6);
			String isSuccess = null;
			if (isUpLoadSuccess != null && isUpLoadSuccess.size() > 0) {
				isSuccess = (String) isUpLoadSuccess.get(0);
			}

			isUpLoadFileSuccessNew(isSuccess);

		} catch (Exception e) {
//			if (e instanceof java.io.FileNotFoundException)
//				MessageDialog
//						.showErrorDlg(getBillUI(), nc.ui.ml.NCLangRes
//								.getInstance().getStrByID("SCMCOMMON",
//										"UPPSCMCommon-000132")/* @res "����" */,
//								nc.ui.ml.NCLangRes.getInstance().getStrByID(
//										"40040701", "UPP40040701-000072")/*
//																			 * @res
//																			 * "��ȷ���ļ�Ŀ¼���ļ��Ƿ���ڣ�"
//																			 */);
//			else
				MessageDialog.showErrorDlg(getBillUI(), nc.ui.ml.NCLangRes
						.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000132")/* @res "����" */, e
						.getMessage());
				return;
		}

		// ��ʾ���ݡ�����ť״̬
		getBillCardPanelWrapper().getBillCardPanel().getBillModel()
				.clearBodyData();
		try {
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			SCMEnv.out(e.getMessage());
			PuTool.outException(e) ;
		}
		getBufferData().clear();
		getBufferData().updateView();
	}

	/**
	 * @���ܣ��жϵ����Ƿ��ϴ��ɹ�
	 * @���ߣ����� �������ڣ�(2004-12-8 15:48:14)
	 * @param:alUploadFile--�����ϴ����ļ� alUploadFailFile--�����ϴ�ʧ�ܵ��ļ�
	 *                              askBillVOs--�����ϴ��ɹ����ļ� sPath--�ϴ��ļ���·��
	 *                              erc---EXCEL�ļ��ӿ�
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 * @param newM_strState
	 *            java.lang.String
	 */
	private void isUpLoadFileSuccessNew(String isSuccess) {
		// �ж��ļ��Ƿ��ϴ��ɹ�
		if (isSuccess != null && isSuccess.length() > 0
				&& "success".equals(isSuccess)) {
			MessageDialog.showWarningDlg(getBillUI(), "��ʾ",
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
							"UPP40040701-000070")/* @res "�����ļ��ϴ��ɹ���" */);

		}
		else if ((isSuccess != null && isSuccess.length() > 0 && "false"
				.equals(isSuccess))
				|| isSuccess == null) {
			MessageDialog.showErrorDlg(getBillUI(), nc.ui.ml.NCLangRes
					.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000132")/* @res "����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
							"UPP40040701-000073")/* @res "�����ļ��ϴ�ʧ��!" */);
		}
	}

	/**
	 * @���ܣ��жϵ����Ƿ��ϴ��ɹ�
	 * @���ߣ����� �������ڣ�(2004-12-8 15:48:14)
	 * @param:alUploadFile--�����ϴ����ļ� alUploadFailFile--�����ϴ�ʧ�ܵ��ļ�
	 *                              askBillVOs--�����ϴ��ɹ����ļ� sPath--�ϴ��ļ���·��
	 *                              erc---EXCEL�ļ��ӿ�
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 * @param newM_strState
	 *            java.lang.String
	 */
//	private void isUpLoadFileSuccess(ArrayList alUploadFile,
//			ArrayList alUploadFailFile, ArrayList askBillVOs, String sPath,
//			ExcelReadCtrl erc) {
//		// �ж��ļ��Ƿ��ϴ��ɹ�
//		Hashtable htbUploadFile = new Hashtable();
//		String sFilename = null;
//		for (int i = 0; i < alUploadFile.size(); i++) {
//			sFilename = (String) alUploadFile.get(i);
//			if (sFilename != null && sFilename.length() > 0)
//				htbUploadFile.put(sFilename, sFilename);
//
//		}
//
//		if (askBillVOs != null
//				&& askBillVOs.size() > 0
//				&& ((alUploadFailFile == null) || (alUploadFailFile != null && alUploadFailFile
//						.size() == 0))) {
//			MessageDialog.showErrorDlg(getBillUI(), nc.ui.ml.NCLangRes
//					.getInstance().getStrByID("SCMCOMMON",
//							"UPPSCMCommon-000132")/* @res "����" */,
//					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
//							"UPP40040701-000070")/* @res "�����ļ��ϴ��ɹ���" */);
//
//		}
//
//		if (askBillVOs != null && askBillVOs.size() > 0
//				&& alUploadFailFile != null && alUploadFailFile.size() > 0) {
//			MessageDialog.showErrorDlg(getBillUI(), nc.ui.ml.NCLangRes
//					.getInstance().getStrByID("SCMCOMMON",
//							"UPPSCMCommon-000132")/* @res "����" */,
//					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
//							"UPP40040701-000071")/* @res ",�����ļ��ϴ�ʧ��" */);
//
//			for (int i = 0; i < alUploadFailFile.size(); i++) {
//				try {
//					erc = new ExcelReadCtrl(sPath + "\\"
//							+ (String) alUploadFailFile.get(i), true);
//					erc.setExcelFileFlag(IExcelFileFlag.F_UPLOADFAILED);
//				} catch (Exception e) {
//					if (e instanceof java.io.FileNotFoundException)
//						MessageDialog
//								.showErrorDlg(
//										getBillUI(),
//										nc.ui.ml.NCLangRes.getInstance()
//												.getStrByID("SCMCOMMON",
//														"UPPSCMCommon-000132")/*
//																				 * @res
//																				 * "����"
//																				 */,
//										nc.ui.ml.NCLangRes.getInstance()
//												.getStrByID("40040701",
//														"UPP40040701-000072")/*
//																				 * @res
//																				 * "��ȷ���ļ�Ŀ¼���ļ��Ƿ���ڣ�"
//																				 */);
//					else
//						MessageDialog
//								.showErrorDlg(
//										getBillUI(),
//										nc.ui.ml.NCLangRes.getInstance()
//												.getStrByID("SCMCOMMON",
//														"UPPSCMCommon-000132")/*
//																				 * @res
//																				 * "����"
//																				 */,
//										e.getMessage());
//				}
//			}
//		}
//
//		if (askBillVOs == null
//				|| (askBillVOs != null && askBillVOs.size() == 0)) {
//			MessageDialog.showErrorDlg(getBillUI(), nc.ui.ml.NCLangRes
//					.getInstance().getStrByID("SCMCOMMON",
//							"UPPSCMCommon-000132")/* @res "����" */,
//					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
//							"UPP40040701-000073")/* @res "�����ļ��ϴ�ʧ��!" */);
//
//		}
//	}

	/**
	 * �õ�ѡ�е��ļ�
	 * 
	 * @return java.lang.String[]
	 */
	private String[] getSelectedFiles() {
		String[] ss = null;
		java.util.Vector v = new java.util.Vector();
//		String sPath = null;
		String sFilePath = null;
		UpLoadFileVO temp = null;
		if (null != excelTOBill && excelTOBill.size() > 0) {
      for (int i = 0; i < excelTOBill.size(); i++) {
        temp = new UpLoadFileVO();
        temp = (UpLoadFileVO) excelTOBill.get(i);
        if (temp != null && temp.getFileName() != null
            && temp.getFileName().trim().length() > 0) {
          sFilePath = temp.getFileName().trim();
          v.add(sFilePath);
        }
      }
    }else{
     return null; 
    }
		ss = new String[v.size()];
		v.copyInto(ss);
		return ss;
	}

	/**
	 * ����ѡ�е��ļ�����
	 * 
	 * @return javax.swing.JFileChooser
	 */
	private JFileChooser getFileChooser() {
		if (m_filechooser == null) {
			m_filechooser = new UIFileChooser();
			// ��ȥ��ǰ���ļ�������
			m_filechooser.removeChoosableFileFilter(m_filechooser
					.getFileFilter());
			// ����ļ�ѡ�������
			 m_filechooser.addChoosableFileFilter(new
					 ExcelFileFilter("xls"));//Excel�ļ�
			// ��ʾ��ѡȡ�İ����ļ���Ŀ¼
			m_filechooser.setFileSelectionMode(m_filechooser.FILES_AND_DIRECTORIES);
		}
		return m_filechooser;
	}

	/**
	 * ���ߣ����� ���ܣ�����ά��״̬�İ�ť������ �������� ���أ��� ���⣺�� ���ڣ�(2002-5-15 11:39:21)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-10-31 zx ������ļ�������ť�Ŀ��� 2002-11-01 zx
	 * �޸ĵ�ǰ�޵���ʱ�Ŀ�ָ�����
	 */
	protected void setButtonsStateCardMaintain() {
		mainCard = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IAskAndQuote.MAINTENANCEBill);
		mainCard.setEnabled(true);
		edit = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IBillButton.Edit);
		save = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IBillButton.Save);
		cancel = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IBillButton.Cancel);
		del = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IBillButton.Del);
		copy = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IBillButton.Copy);
		del.setVisible(true);
		if (getBillUI().getBillOperate() == IBillOperate.OP_NOTEDIT) {
			if (getBillCardPanelWrapper().getBillCardPanel().getTailItem(
					"ibillstatus") != null
					&& getBillCardPanelWrapper().getBillCardPanel()
							.getTailItem("ibillstatus").getValue() != null) {
				if (StatusConvert.getStatusIndexNum().get(
						new Integer(IAskBillStatus.CONFIRM)) != null
						&& StatusConvert.getStatusIndexNum()
								.get(new Integer(IAskBillStatus.CONFIRM))
								.toString().trim().length() > 0
						&& getBillCardPanelWrapper()
								.getBillCardPanel()
								.getTailItem("ibillstatus")
								.getValue()
								.equals(
                    StatusConvert
												.getStatusIndexNum()
												.get(
														new Integer(
																IAskBillStatus.CONFIRM))
												.toString())) {
					edit.setEnabled(false);
					save.setEnabled(false);
					cancel.setEnabled(false);
					del.setEnabled(false);
					copy.setEnabled(true);

				} else {
					edit.setEnabled(true);
					save.setEnabled(false);
					cancel.setEnabled(false);
					if(getBillCardPanelWrapper().getBillCardPanel()
              .getHeadItem("vaskbillcode").getValue()== null ||getBillCardPanelWrapper().getBillCardPanel()
              .getHeadItem("vaskbillcode").getValue().toString().trim().length() == 0){
					  del.setEnabled(false);
					}else{
					  del.setEnabled(true);
					}
					copy.setEnabled(true);
				}
			} else {
				edit.setEnabled(false);
				save.setEnabled(false);
				cancel.setEnabled(false);
				del.setEnabled(false);
				copy.setEnabled(false);
			}
		}else if (getBillUI().getBillOperate() == IBillOperate.OP_REFADD) {
			edit.setEnabled(false);
			save.setEnabled(true);
			cancel.setEnabled(true);
			del.setEnabled(false);
			copy.setEnabled(false);
		}
		else if (getBillUI().getBillOperate() == IBillOperate.OP_INIT) {
			edit.setEnabled(false);
			save.setEnabled(false);
			cancel.setEnabled(false);
			del.setEnabled(false);
			copy.setEnabled(false);
		}
	}
	private void onBoMailMaintain(){
	  String currentUser = nc.ui.pub.ClientEnvironment
      .getInstance().getUser().getPrimaryKey();
	  AccountManageDialog dlg=new AccountManageDialog((AskAndQuoteUI)getBillUI(),
	      nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm","UPPsmcomm-000053")/*@res "�˺Ź���Ի���"*/,currentUser);
    dlg.showModal();
	}
	
	/**
	 * 
	 * ���ߣ������
	 * ���ܣ�����Ŀ�����ݵ�cupsourcebts��cupsourcehts������Դ���ݵ�cupsourcebts��cupsourcehts
	 * ������ sourceItems - Դ���� (AskBillMergeVO)
	 *        targetItems - Ŀ������ (AskBillMergeVO)
	 * ���أ�
	 * ���⣺
	 * ���ڣ�2009-10-19
	 * �޸����ڣ� �޸��ˣ��޸�ԭ��ע�ͱ�־
	 */
	private void setUpSourceTs(SuperVO[] sourceItems,SuperVO[] targetItems){
		for (int i = 0; i < sourceItems.length; i++) {
			SuperVO sItem=sourceItems[i];
			
			//û�в��յ���ֱ�ӷ���
			if(sItem.getAttributeValue("cupsourcebillrowid") == null
					|| StringUtil.isEmptyWithTrim(sItem.getAttributeValue("cupsourcebillrowid").toString())) 
				continue;
			
			//����ts
			for (int j = 0; j < targetItems.length; j++) {
				SuperVO tItem=targetItems[j];
				if(sItem.getAttributeValue("cupsourcebillrowid").equals(tItem.getAttributeValue("cupsourcebillrowid"))){
					if(tItem.getAttributeValue("cupsourcebts") != null)
						sItem.setAttributeValue("cupsourcebts", tItem.getAttributeValue("cupsourcebts").toString());
					if(tItem.getAttributeValue("cupsourcehts") != null)
						sItem.setAttributeValue("cupsourcehts", tItem.getAttributeValue("cupsourcehts").toString());
				}
			}
		}
	}
	
}

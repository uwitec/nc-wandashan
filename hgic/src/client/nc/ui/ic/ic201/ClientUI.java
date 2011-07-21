package nc.ui.ic.ic201;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.pps.IPricStl;
import nc.ui.hg.ic.pub.HgICPubHealper;
import nc.ui.hg.pu.pub.LongTimeTask;
import nc.ui.ic.auditdlg.ClientUIInAndOut;
import nc.ui.ic.ic211.GeneralHHelper;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ic.pub.bill.GeneralBillUICtl;
import nc.ui.ic.pub.bill.IButtonManager;
import nc.ui.ic.pub.bill.ICButtonConst;
import nc.ui.ic.pub.bill.QueryDlgHelp;
import nc.ui.ic.pub.bill.initref.RefFilter;
import nc.ui.ic.pub.tools.GenMethod;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.vo.hg.pu.pub.HgPuBtnConst;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.IItemKey;
import nc.vo.ic.pub.bill.QryInfoConst;
import nc.vo.ic.pub.billtype.BillTypeFactory;
import nc.vo.ic.pub.billtype.IBillType;
import nc.vo.ic.pub.lang.ResBase;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.constant.ic.BillMode;
import nc.vo.scm.constant.ic.InOutFlag;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;

/**
 * �˴���������˵���� �������ڣ�(2001-11-23 15:39:43)
 * 
 * @author�����˾�
 * 
 */
public class ClientUI extends nc.ui.ic.pub.bill.GeneralBillClientUI {
	// protected ButtonObject m_boRatioOut = new ButtonObject(
	// nc.ui.ml.NCLangRes.getInstance().getStrByID("40080602",
	// "UPT40080804-000004")/* @res "��ȳ���" */,
	// nc.ui.ml.NCLangRes.getInstance().getStrByID("40080602",
	// "UPT40080804-000004")/* @res "��ȳ���" */, 0, "��ȳ���");/*-=notranslate=-*/

	// zhw begin
	/** ������Ϣ�Ի��� */
	private CkeckDLG m_CkeckDLG = null;
	private UnCheckDealDLG m_UnCheckDealDLG = null;
	boolean m_isAdjust = false; // ����ʵ��༭̬����
	// end

	// ���׽���Ի���
	private nc.ui.ic.auditdlg.ClientUIInAndOut m_dlgInOut = null;

	// �����תҵ������id
	private ArrayList m_alBrwLendBusitype = null;

	// ҵ������itemkey
	private final String m_sBusiTypeItemKey = "cbiztype";

	private IButtonManager m_buttonManager;

	/**
	 * ClientUI2 ������ע�⡣
	 */
	public ClientUI() {
		super();
		initialize();
	}

	/**
	 * ClientUI ������ע�⡣ add by liuzy 2007-12-18 ���ݽڵ�����ʼ������ģ��
	 */
	public ClientUI(FramePanel fp) {
		super(fp);
		initialize();
	}

	/**
	 * ClientUI ������ע�⡣ nc 2.2 �ṩ�ĵ������鹦�ܹ����ӡ�
	 * 
	 */
	public ClientUI(String pk_corp, String billType, String businessType,
			String operator, String billID) {
		super(pk_corp, billType, businessType, operator, billID);

	}

	/**
	 * ����¼�뼯�вɹ��ı����ֶ�
	 */

	public void setBodyDefaultData(int istartrow, int count) {

		// �鿴�Ƿ��������
		if (getBillCardPanel().getRowCount() > 0) {
			Object obj = getBillCardPanel().getBodyValueAt(0,
					IItemKey.CSOURCEBILLBID);
			if (obj != null && obj.toString().trim().length() > 0)
				return;
		}

		String pk_corp = getBillCardPanel().getHeadItem(IItemKey.PKCORP)
				.getValue();
		String calid = getBillCardPanel().getHeadItem("pk_calbody").getValue();
		String whid = getBillCardPanel().getHeadItem("cwarehouseid").getValue();

		for (int i = istartrow; i < count; i++) {
			// �������˾��Ϊ��,���ܴ����ͷ��ֵ
			String reqCorp = (String) getBillCardPanel().getBodyValueAt(i,
					IItemKey.REQ_CORP);
			if (reqCorp == null || reqCorp.trim().length() == 0) {
				getBillCardPanel()
						.setBodyValueAt(pk_corp, i, IItemKey.REQ_CORP);
				getBillCardPanel().getBillModel().execEditFormulaByKey(i,
						IItemKey.REQ_CORPNAME);
			}
			// req_cal
			String reqCal = (String) getBillCardPanel().getBodyValueAt(i,
					IItemKey.REQ_CAL);
			if (reqCal == null) {
				getBillCardPanel().setBodyValueAt(calid, i, IItemKey.REQ_CAL);
				getBillCardPanel().getBillModel().execEditFormulaByKey(i,
						IItemKey.REQ_CALNAME);
			}
			// req_wh
			String reqWh = (String) getBillCardPanel().getBodyValueAt(i,
					IItemKey.REQ_WH);
			if (reqWh == null) {
				getBillCardPanel().setBodyValueAt(whid, i, IItemKey.REQ_WH);
				getBillCardPanel().getBillModel().execEditFormulaByKey(i,
						IItemKey.REQ_WHNAME);
			}

			// inv_corp
			String invCorp = (String) getBillCardPanel().getBodyValueAt(i,
					IItemKey.INV_CORP);
			if (invCorp == null) {
				getBillCardPanel()
						.setBodyValueAt(pk_corp, i, IItemKey.INV_CORP);
				getBillCardPanel().getBillModel().execEditFormulaByKey(i,
						IItemKey.INV_CORPNAME);
			}

		}
	}

	protected void afterBillEdit(nc.ui.pub.bill.BillEditEvent e) {
		String sItemKey = e.getKey();
		int row = e.getRow();
		if (sItemKey.equals("cproviderid")) {
			// ��Ӧ��
			afterProviderEdit(e);
		}else if ("vbodynote2".equalsIgnoreCase(sItemKey)) {
			nc.ui.pub.beans.UIRefPane refPaneReasonBody = (nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getBodyItem("vbodynote2").getComponent();
			String sBodyNoteCode = refPaneReasonBody.getText();
			String sBodyNoteCodeContent = (String) refPaneReasonBody
					.getRefValue("cbackreasonname");

			String sReturnResult = null;
			// ����ж�Ӧ������
			if (sBodyNoteCodeContent != null)
				sReturnResult = sBodyNoteCodeContent;
			else if (sBodyNoteCode != null) // ����ı���û�ж�Ӧ���˿����ɣ�����Ϊֱ����������
				sReturnResult = sBodyNoteCode;
			// ���Ϸ��ؽ��
			if (sReturnResult != null) {
				getBillCardPanel().setBodyValueAt(sReturnResult, row,
						"vbodynote2");
			}

		}

	}

	/**
	 * ���ߣ�� ���ܣ���Ӧ�̱༭���¼���������� ������ ���أ� ���⣺ ���ڣ�(2004-6-21 10:36:30)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void afterProviderEdit(nc.ui.pub.bill.BillEditEvent e) {

		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cproviderid").getComponent()).getRefName();
		String sRefPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cproviderid").getComponent()).getRefPK();

		// �������������б���ʽ����ʾ��
		if (getM_voBill() != null)
			getM_voBill().setHeaderValue("cprovidername", sName);

		// ���ݿͻ���Ӧ�̹��˷��˵�ַ�Ĳ���
		if (getBillCardPanel().getHeadItem("vdiliveraddress") != null) {
			((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(
					"vdiliveraddress").getComponent())
					.setWhereString("pk_cubasdoc = (select pk_cubasdoc from bd_cumandoc where pk_cumandoc = '"
							+ sRefPK + "')");
		}

		BillItem iProvidername = getBillCardPanel().getHeadItem(
				"cprovidershortname");
		BillItem iPk_cubasdoc = getBillCardPanel().getHeadItem("pk_cubasdoc");
		try {
			// ���ݲ��մ�����Ӧ�̼��
			nc.ui.scm.pub.TwoTableCacheFind twoTable = new nc.ui.scm.pub.TwoTableCacheFind();
			String sProviderShortName = twoTable.getJoinTableFieldValue(
					"bd_cumandoc", sRefPK, "custshortname");
			if (iProvidername != null) {
				iProvidername.setValue(sProviderShortName);
			}

			// ��ÿ��̻�������ID
			String sPk_cubasdoc = getPk_cubasdoc(sRefPK);
			if (iPk_cubasdoc != null)
				iPk_cubasdoc.setValue(sPk_cubasdoc);

			if (getM_voBill() != null) {
				getM_voBill().setHeaderValue("cprovidershortname",
						sProviderShortName);
				getM_voBill().setHeaderValue("pk_cubasdoc", sPk_cubasdoc);
			}
			// modify by zhw ����ѡ��Ӧ��  �����Զ�����  �ɹ���ⵥ
				int row = getBillCardPanel().getRowCount();
				setBodyCelValue(row,"cvendorid",sRefPK);
				setBodyCelValue(row,"pk_cubasdoc",sPk_cubasdoc);
				getBillCardPanel().getBillModel().execLoadFormulaByKey(
				"vvendorname");
		} catch (BusinessException be) {
			Logger.error(be.getMessage(), be);
			MessageDialog.showErrorDlg(this, null, be.getMessage());
		} catch (BusinessRuntimeException bre) {
			Logger.error(bre.getMessage(), bre);
			MessageDialog.showErrorDlg(this, null, bre.getMessage());
		} catch (Exception ee) {
			MessageDialog.showUnknownErrorDlg(this, ee);
		}

	}
	
	/**
	 * �����ߣ����˾� ���ܣ������塢�б��ϱ�༭�¼����� ������e ���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	@Override
	public void bodyRowChange(nc.ui.pub.bill.BillEditEvent e) {
		super.bodyRowChange(e);

		// // ����������Խ�һ�б����У�����ѡ������жϣ����£����״̬��δǩ�֣�δ�������׵ĵ���,����Դ��������Դ��������ģ�鵥��
		// // ���װ�ť���á�
		// if (e.getSource() == getBillCardPanel().getBillTable()
		// && m_boDispense != null) {
		// int rownum = e.getRow();
		// if (BillMode.Browse == m_iMode && isSigned() != SIGNED
		// && isSetInv(m_voBill, rownum)
		// && !isDispensedBill(m_voBill, rownum)
		// && getSourBillTypeCode() != null
		// && !getSourBillTypeCode().startsWith("4")
		// ) {
		// m_boDispense.setEnabled(true);
		//
		// } else {
		// m_boDispense.setEnabled(false);
		// }
		// updateButton(m_boDispense);
		// }
	}

	/**
	 * �����ߣ����˾� ���ܣ����󷽷�������ǰ��VO��� �����������浥�� ���أ� ���⣺ ���ڣ�(2001-5-24 ���� 5:17)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected boolean checkVO(nc.vo.ic.pub.bill.GeneralBillVO voBill) {
		return this.checkVO();
	}

	/**
	 * �����ߣ����˾� ���ܣ��õ���ѯ�Ի��� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	// protected QueryConditionDlgForBill getConditionDlg() {
	// if (ivjQueryConditionDlg == null) {
	// ivjQueryConditionDlg = super.getConditionDlg();
	// ivjQueryConditionDlg.setCombox("freplenishflag", new String[][] {
	// {
	// nc.vo.ic.pub.BillTypeConst.BILLNORMAL,
	// nc.ui.ml.NCLangRes.getInstance().getStrByID(
	// "4008busi", "UPP4008busi-000367") /*
	// * @res
	// * "���"
	// */},
	// {
	// nc.vo.ic.pub.BillTypeConst.BILLSENDBACK,
	// nc.ui.ml.NCLangRes.getInstance().getStrByID(
	// "4008busi", "UPT40080602-000014") /*
	// * @res
	// * "�˿�"
	// */},
	// {
	// nc.vo.ic.pub.BillTypeConst.BILLALL,
	// nc.ui.ml.NCLangRes.getInstance().getStrByID(
	// "4008busi", "UPPSCMCommon-000217") /*
	// * @res
	// * "ȫ��"
	// */} });
	// // zhy2005-04-23�ɹ���ⵥ��Ҫ���˹�Ӧ��Ȩ��
	// // �Ȼ�����һ����Ӧ��
	// ivjQueryConditionDlg.setCorpRefs("head.pk_corp",
	// new String[] { "head.cproviderid" });
	// }
	//
	// return ivjQueryConditionDlg;
	// }
	public QueryDlgHelp getQryDlgHelp() {
		if (m_qryDlgHelp == null) {
			m_qryDlgHelp = new QueryDlgHelp(this) {
				protected void init() {
					super.init();
					getQueryDlg()
							.setCombox(
									"freplenishflag",
									new String[][] {
											{
													nc.vo.ic.pub.BillTypeConst.BILLNORMAL,
													nc.ui.ml.NCLangRes
															.getInstance()
															.getStrByID(
																	"4008busi",
																	"UPP4008busi-000367") /** @res* "���" */
											},
											{
													nc.vo.ic.pub.BillTypeConst.BILLSENDBACK,
													nc.ui.ml.NCLangRes
															.getInstance()
															.getStrByID(
																	"4008busi",
																	"UPT40080602-000014") /** @res* "�˿�" */
											},
											{
													nc.vo.ic.pub.BillTypeConst.BILLALL,
													nc.ui.ml.NCLangRes
															.getInstance()
															.getStrByID(
																	"4008busi",
																	"UPPSCMCommon-000217") /**
																							 * @res *
																							 *      "ȫ��"
																							 */
											} });
					getQueryDlg().setInitDate("freplenishflag",
							nc.vo.ic.pub.BillTypeConst.BILLNORMAL);
					// zhy2005-04-23�ɹ���ⵥ��Ҫ���˹�Ӧ��Ȩ��
					// �Ȼ�����һ����Ӧ��
					getQueryDlg().setCorpRefs("head.pk_corp",
							new String[] { "head.cproviderid" });
				}
			};
		}
		return m_qryDlgHelp;
	}

	/**
	 * ���� ReturnDlg1 ����ֵ��
	 * 
	 * @return nc.ui.ic.auditdlg.ClientUIInAndOut
	 */
	/* ���棺�˷������������ɡ� */
	protected nc.ui.ic.auditdlg.ClientUIInAndOut getDispenseDlg(String sTitle,
			ArrayList alInVO, ArrayList alOutVO) {
		if (m_dlgInOut == null) {
			try {
				// user code begin {1}
				m_dlgInOut = new ClientUIInAndOut(this, sTitle);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		// if (m_voBill == null)
		setM_voBill((GeneralBillVO) ((GeneralBillVO) getM_alListData().get(
				getM_iLastSelListHeadRow())).clone());
		m_dlgInOut.setVO(getM_voBill(), alInVO, alOutVO, getBillType(),
				getM_voBill().getPrimaryKey().trim(), getEnvironment()
						.getCorpID(), getEnvironment().getUserID());
		m_dlgInOut.setName("BillDlg");
		// m_dlgInOut.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
		return m_dlgInOut;
	}

	/**
	 * ����ʵ�ָ÷���������ҵ�����ı��⡣
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTitle() {
		return super.getTitle();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-11-19 9:10:04)
	 */
	public void initialize() {
		super.initialize();
		long lTime = System.currentTimeMillis();

		getBillCardPanel().setBodyMenuShow(false);
		getBillCardPanel().addBodyEditListener2(this);

		SCMEnv.showTime(lTime, "initialize:addBodyEditListener2:");

		lTime = System.currentTimeMillis();
		nc.ui.pub.beans.UIRefPane refPaneReason = (nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("vheadnote2").getComponent();
		refPaneReason.setAutoCheck(false);
		refPaneReason.setReturnCode(false);
		nc.ui.pub.beans.UIRefPane refPaneReasonBody = (nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getBodyItem("vbodynote2").getComponent();
		refPaneReasonBody.setAutoCheck(false);
		refPaneReasonBody.setReturnCode(true);
		SCMEnv.showTime(lTime, "initialize:init�˻�����:");/*-=notranslate=-*/
	}

	/**
	 * �����ߣ����˾� ���ܣ���ʼ��ϵͳ���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void initPanel() {
		// ��Ҫ���ݲ���
		super.setNeedBillRef(true);
	}

	public String getBillType() {
		return nc.vo.ic.pub.BillTypeConst.m_purchaseIn;
	}

	public String getFunctionNode() {
		return "40080602";
	}

	public int getInOutFlag() {
		return InOutFlag.IN;
	}

	/**
	 * �����ߣ����˾� ���ܣ��Ƿ��ת����
	 * 
	 * ��һ����Ҫ���⡣
	 * 
	 * ������ ���أ� ���⣺ ���ڣ�(2001-11-24 12:15:42) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected boolean isBrwLendBiztype() {
		try {
			GeneralBillVO voMyBill = null;
			// ҵ������
			String sBusitypeid = null;
			if (getM_iCurPanel() == BillMode.List) { // �б���ʽ��
				if (getM_alListData() != null
						&& getM_iLastSelListHeadRow() >= 0
						&& getM_alListData().size() > getM_iLastSelListHeadRow()
						&& getM_alListData().get(getM_iLastSelListHeadRow()) != null) {
					voMyBill = ((GeneralBillVO) getM_alListData().get(
							getM_iLastSelListHeadRow()));
					sBusitypeid = (String) voMyBill
							.getHeaderValue(m_sBusiTypeItemKey);
				}
			} else { // ��
				if (getBillCardPanel().getHeadItem(m_sBusiTypeItemKey) != null
						&& getBillCardPanel().getHeadItem(m_sBusiTypeItemKey)
								.getComponent() != null) {
					nc.ui.pub.beans.UIRefPane ref = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
							.getHeadItem(m_sBusiTypeItemKey).getComponent());
					// ��pk
					sBusitypeid = ref.getRefPK();
				}
			}
			// ҵ�����Ͳ�Ϊ��ʱ
			// ��һ����Ҫ���⡣
			if (sBusitypeid != null && m_alBrwLendBusitype == null) {
				/*
				 * ArrayList alParam = new ArrayList();
				 * alParam.add(getEnvironment().getCorpID());
				 * m_alBrwLendBusitype = (ArrayList) GeneralBillHelper
				 * .queryInfo(new Integer( QryInfoConst.QRY_BRW_LEND_BIZTYPE),
				 * alParam);
				 */
				m_alBrwLendBusitype = GenMethod
						.getBrwLendBiztypes(getEnvironment().getCorpID());

				// ������ؿգ���ʼ��֮����־�Ѿ������ˣ���û�н�ת���Ͱ�!
				if (m_alBrwLendBusitype == null)
					m_alBrwLendBusitype = new ArrayList();
			}
			// �ǽ�ת���͵ģ����ء��ǡ�
			if (sBusitypeid != null && m_alBrwLendBusitype != null
					&& m_alBrwLendBusitype.contains(sBusitypeid))
				return true;
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
		return false;
	}

	protected class ButtonManager201 extends
			nc.ui.ic.pub.bill.GeneralButtonManager {
		public ButtonManager201(GeneralBillClientUI clientUI)
				throws BusinessException {
			super(clientUI);
		}

		/**
		 * ����ʵ�ָ÷�������Ӧ��ť�¼���
		 * 
		 * @version (00-6-1 10:32:59)
		 * 
		 * @param bo
		 *            ButtonObject
		 */
		public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
			showHintMessage(bo.getName());

			// add by zhw �Ѿ����ĵ��ݲ��������²���
			if (unButtonClicked(bo)) {
				// zhw end

				if (bo == getButtonManager().getButton(
						ICButtonConst.BTN_ASSIST_FUNC_DISPENSE))
					onDispense();
				else if (bo == getButtonManager().getButton(
						ICButtonConst.BTN_ASSIST_FUNC_MANUAL_RETURN))
					onNewReplenishInvBill();
				else if (bo == getButtonManager().getButton(
						ICButtonConst.BTN_ASSIST_FUNC_PO_RETURN))
					onNewReplenishInvBillByOrder();

				// v5
				else if (bo == getButtonManager().getButton(
						ICButtonConst.BTN_LINE_KD_CALCULATE))
					onKDJS();
				else if (bo == getButtonTree().getButton(
						ICButtonConst.BTN_ASSIST_GEN_ASSET_CARD))
					onGenerateAssetCard();
				else if (bo == getButtonTree().getButton(
						ICButtonConst.BTN_ASSIST_CGEN_ASSET_CARD))
					onCancelGenerateAssetCard();
				// add by zhw begin
				else if (!PuPubVO.getUFBoolean_NullAs(
						getBillCardPanel().getHeadItem("freplenishflag")
								.getValueObject(), UFBoolean.FALSE)
						.booleanValue()) {

					if (bo == getButtonTree().getButton(HgPuBtnConst.ACPT))// ����
						onCheck();
					else if (bo == getButtonTree().getButton(
							HgPuBtnConst.UNACPT))// ȡ������
						onUNCheck();
					else if (bo == getButtonTree().getButton(
							HgPuBtnConst.UNDEAL))// ���ϸ���
						onDeal();
					else if (bo == getButtonTree().getButton(HgPuBtnConst.WARE))// ���
						onBillIn();
					else if (bo == getButtonTree()
							.getButton(HgPuBtnConst.MATCH))
						onMatchPact();
					else if(bo == getButtonTree().getButton(HgPuBtnConst.CANCELMATCH)){
						onCancelMatchPact();
					}else if(bo == getButtonTree().getButton(HgPuBtnConst.deriver)){
						onExportBtnsClick(bo);
					}
					else
						super.onButtonClicked(bo);
				}
				// zhw end
				else
					super.onButtonClicked(bo);
			}
		}

		/**
		 * �Ѿ����ĵ��ݲ�����ִ�еĲ���
		 * 
		 * @author zhw
		 * @˵�������׸ڿ�ҵ�� 2011-12-15����06:31:10
		 * @param bo
		 */
		private boolean unButtonClicked(nc.ui.pub.ButtonObject bo) {
			
			GeneralBillVO vo = null;
			vo = getCurVO();
			
			if (vo != null) {
				
				if (PuPubVO.getUFBoolean_NullAs(
						vo.getHeaderVO().getAttributeValue(HgPubConst.VUSERDEF[5]), UFBoolean.FALSE).booleanValue()) {
					
					String name = bo.getName();
					String[] str = HgPubConst.PurchaseIn_ButtonName;
					int len = str.length;
					
					ArrayList al = new ArrayList();
					
					for (int i = 0; i < len; i++) {
						al.add(str[i]);
					}
					
					if (!al.contains(name)) {
						showErrorMsg("�õ����Ѿ���棬���ɲ���");
						return false;
					}
				}
			}
			return true;
		}

		/**
		 * ���մ���
		 * 
		 * @author zhw
		 * @˵�������׸ڿ�ҵ�� 2011-12-8����06:23:10
		 */
		private void onCheck() {
			GeneralBillVO vo = null;
			try {
				vo = getCurVO();

				if (vo == null)
					return;

				if (HgPubConst.purchaseIn_in == HgPubTool.getPurchaseInBusiStatus(vo)) {
					showErrorMsg("�õ����Ѿ����,��������");
					return;
				}

				if (HgPubConst.purchaseIn_ok == HgPubTool.getPurchaseInBusiStatus(vo)
						|| HgPubConst.purchaseIn_no == HgPubTool.getPurchaseInBusiStatus(vo)
						|| HgPubConst.purchaseIn_nopact == HgPubTool.getPurchaseInBusiStatus(vo)) {
					showErrorMsg("�õ����Ѿ����գ������ظ�����");
					return;
				}
			    
				vo.getHeaderVO().setAttributeValue("cuserid",getClientEnvironment().getUser().getPrimaryKey());
				getCkeckDLG().setGbillvo(vo);// ��Ի����д�VO
				getCkeckDLG().initDataPanel();// ��ʼ���Ի����е�PANEL
				
				if (getCkeckDLG().showModal() != UIDialog.ID_OK) {
					return;
				}
				
				GeneralBillVO billVO = getCkeckDLG().getGbillvo();
				
				if (HgPubConst.purchaseIn_ok == HgPubTool.getPurchaseInBusiStatus(billVO)) {
					showHintMessage("���պϸ�");
				} else if (HgPubConst.purchaseIn_no == HgPubTool.getPurchaseInBusiStatus(billVO)) {
					showHintMessage("���ղ��ϸ�");
				}
				
				setDataToUI(billVO);// �������������
			
				// ���û���VO
				getM_alListData().set(getM_iLastSelListHeadRow(), billVO);
				setM_voBill(billVO);
				
			} catch (Exception e) {
				showErrorMessage(HgPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
				e.printStackTrace();
			}
		}

		/**
		 * ���նԻ���
		 * 
		 * @author zhw
		 * @˵�������׸ڿ�ҵ�� 2011-12-8����08:51:37
		 * @return
		 * @throws Exception
		 */
		private CkeckDLG getCkeckDLG() throws Exception {
		  
			if (m_CkeckDLG == null) {
				
		    	m_CkeckDLG = new CkeckDLG(this.getClientUI(),getClientEnvironment());
			}
			return m_CkeckDLG;
		}

		/**
		 * ������ɺ� �������������
		 * 
		 * @author zhw
		 * @˵�������׸ڿ�ҵ�� 2011-12-8����08:52:50
		 * @param billVO
		 */
		private void setDataToUI(GeneralBillVO billVO) {
			
			GeneralBillHeaderVO head = billVO.getHeaderVO();
			GeneralBillItemVO[] items = (GeneralBillItemVO[]) billVO.getChildrenVO();
			
			int len = items.length;
			
			if (BillMode.List == getM_iCurPanel()) {
				int selrow = getBillListPanel().getHeadTable().getSelectedRow();
				
				for (int i = 0; i < 6; i++) {
					setListHeadItem(head.getAttributeValue(HgPubConst.VUSERDEF[i]), selrow,HgPubConst.VUSERDEF[i]);
				}
				
				setListHeadItem(head.getTs(), selrow, "ts");

				String[] aryAssistunit = new String[] {
						"fis->" + HgPubConst.VUSERDEF[0],
						"dealid->" + HgPubConst.VUSERDEF[3],
						"dealname->getColValue(sm_user,user_name,cuserid,"+ HgPubConst.VUSERDEF[3] + ")",
						"fisclose->" + HgPubConst.VUSERDEF[5]};

				getBillListPanel().getHeadBillModel().execFormulas(selrow,aryAssistunit);
				
				for (int i = 0; i < len; i++) {
					getBillListPanel().getBodyBillModel().setValueAt(
							PuPubVO.getUFDouble_NullAsZero(items[i].getAttributeValue(HgPubConst.NUM_DEF_QUA)),
								i, HgPubConst.NUM_DEF_QUA);
					getBillListPanel().getBodyBillModel().setValueAt(items[i].getTs(), i, "ts");
				}
				
			} else {
				
				for (int i = 0; i < 6; i++) {
					setCardHeadItem(HgPubConst.VUSERDEF[i], head.getAttributeValue(HgPubConst.VUSERDEF[i]));
				}
				
				setCardHeadItem("ts", head.getTs());

				String[] aryAssistunit = new String[] {
						"fis->" + HgPubConst.VUSERDEF[0],
						"dealid->" + HgPubConst.VUSERDEF[3],
						"dealname->getColValue(sm_user,user_name,cuserid,"+ HgPubConst.VUSERDEF[3] + ")",
						"fisclose->" + HgPubConst.VUSERDEF[5], };
				
				getBillCardPanel().execHeadFormulas(aryAssistunit);
			
				for (int i = 0; i < len; i++) {
					getBillCardPanel().getBillModel().setValueAt(
							PuPubVO.getUFDouble_NullAsZero(items[i].getAttributeValue(HgPubConst.NUM_DEF_QUA)),
									i, HgPubConst.NUM_DEF_QUA);
					getBillCardPanel().getBillModel().setValueAt(items[i].getTs(), i, "ts");
				}
			}
		}

		private void setCardHeadItem(String filde, Object value) {
			getBillCardPanel().getHeadItem(filde).setValue(value);
		}

		private void setListHeadItem(Object value, int row, String filde) {
			getBillListPanel().getHeadBillModel().setValueAt(value, row, filde);
		}

		// ִ�й�ʽ ��ʾ ������ ������
		protected void onSwitch() {
			
			super.onSwitch();
			
			GeneralBillVO vo = getCurVO();
			if (vo == null)
				return;
			
			if (BillMode.List == getM_iCurPanel()) {
				
				int selrow = getBillListPanel().getHeadTable().getSelectedRow();
				
				String[] aryAssistunit = new String[] {
						"fis->" + HgPubConst.VUSERDEF[0],
						"dealid->" + HgPubConst.VUSERDEF[3],
						"dealname->getColValue(sm_user,user_name,cuserid,"+ HgPubConst.VUSERDEF[3] + ")",
						"fisclose->" + HgPubConst.VUSERDEF[5],
						"updateid->" + HgPubConst.VUSERDEF[7],
						"updatename->getColValue(sm_user,user_name,cuserid,"+ HgPubConst.VUSERDEF[7] + ")" };
			
				getBillListPanel().getHeadBillModel().execFormulas(selrow,aryAssistunit);
			
			} else {
				
				String[] aryAssistunit = new String[] {
						"fis->" + HgPubConst.VUSERDEF[0],
						"dealid->" + HgPubConst.VUSERDEF[3],
						"dealname->getColValue(sm_user,user_name,cuserid,"+ HgPubConst.VUSERDEF[3] + ")",
						"fisclose->" + HgPubConst.VUSERDEF[5],
						"updateid->" + HgPubConst.VUSERDEF[7],
						"updatename->getColValue(sm_user,user_name,cuserid,"+ HgPubConst.VUSERDEF[7] + ")" };
				
				getBillCardPanel().execHeadFormulas(aryAssistunit);
			}
		}

		/**
		 * ������ɵĵ��ݲ����޸�
		 * 
		 * @author zhw
		 * @˵�������׸ڿ�ҵ�� 2011-12-8����08:52:50
		 * @param billVO
		 */
		protected void onUpdate() {
			GeneralBillVO vo = getCurVO();
			if (vo == null)
				return;
			if (HgPubConst.purchaseIn_in != HgPubTool.getPurchaseInBusiStatus(vo)) {
				
				if (HgPubConst.purchaseIn_ok == HgPubTool.getPurchaseInBusiStatus(vo)
						|| HgPubConst.purchaseIn_no == HgPubTool.getPurchaseInBusiStatus(vo)
						|| HgPubConst.purchaseIn_nopact == HgPubTool.getPurchaseInBusiStatus(vo)) {
					
					showErrorMsg("�õ����Ѿ����գ������޸�");
					return;
				}
			}
			super.onUpdate();
			
			//�����֮������޸�һЩ�ֶ�
			if(HgPubConst.purchaseIn_in == HgPubTool.getPurchaseInBusiStatus(vo)){
				
				setAfterIn(false);
			
			}else{
				
				setHeadEditEnableWhenBillIn(true);// ���ñ�ͷ�ɱ༭Ԫ��
			}
			
			setButtonsEnable(false);// �����ʼ찴ť�Ƿ����

		}

		private void showErrorMsg(String msg) {
			
			MessageDialog.showErrorDlg(this.getClientUI(), "",msg == null ? "���������쳣�������²���" : msg);
		}

		/**
		 * ȡ������
		 * 
		 * @author zhw
		 * @˵�������׸ڿ�ҵ�� 2011-12-8����08:55:38
		 */
		private void onUNCheck() {
			
			GeneralBillVO vo = null;
			
			try {
				vo = getCurVO();
				
				if (vo == null)
					return;

				GeneralBillHeaderVO head = vo.getHeaderVO();
				GeneralBillItemVO[] items = (GeneralBillItemVO[]) vo.getChildrenVO();
				
				int len = items.length;

				if (HgPubConst.purchaseIn_in == HgPubTool.getPurchaseInBusiStatus(vo)) {
					
					showErrorMsg("�õ����Ѿ����,����ȡ������");
					return;
				}
				
				boolean isPact = false;
				
				for (GeneralBillItemVO item : items) {
					
					if (PuPubVO.getString_TrimZeroLenAsNull(item.getCsourcetype()) != null)
						isPact = true;
				}
				String self = HgPubTool.getString_NullAsTrimZeroLen(getBillCardPanel()
								.getHeadItem(HgPubConst.F_IS_SELF).getValueObject());
				
				// ��������� �����к�ͬƥ��У��
				if ((HgPubConst.SELF).equalsIgnoreCase(self)) {
					if (isPact) {
						showErrorMsg("�õ����Ѿ���ͬƥ��,����ȡ������");
						return;
					}
				}

				if (HgPubConst.purchaseIn_arr == HgPubTool.getPurchaseInBusiStatus(vo)) {
					showErrorMsg("�õ���δ����,����ȡ������");
					return;
				}

				Class[] ParameterTypes = new Class[] { String.class };
				Object[] ParameterValues = new Object[] { head.getCgeneralhid() };
				Object o = LongTimeTask.callRemoteService("ic","nc.bs.hg.ic.ic201.SaveGeneral", "unCheck",ParameterTypes, ParameterValues, 2);
				
				if (o != null) {
					
					Map map = (Map) o;
					
					if (map.containsKey(head.getCgeneralhid())) 
						head.setTs((map.get(head.getCgeneralhid()).toString()));
					
					for (int i = 0; i < len; i++) {
						
						if (map.containsKey(items[i].getCgeneralbid())) 
							items[i].setTs((map.get(items[i].getCgeneralbid()).toString()));		
					}
				}
				
				changeVO(vo);// VOת��
				setDataToUI(vo);// �������������
				setDataToUIForUnCheckDeal(vo);
				
				// ���û���VO
				getM_alListData().set(getM_iLastSelListHeadRow(), vo);
				setM_voBill(vo);

			} catch (Exception e) {
				showErrorMsg(HgPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
				e.printStackTrace();
			}
		}

		/**
		 * ȡ�����յ�vo���
		 * 
		 * @author zhw
		 * @˵�������׸ڿ�ҵ�� 2011-12-8����08:56:13
		 * @param vo
		 */
		private void changeVO(GeneralBillVO vo) {
			
			GeneralBillHeaderVO head = vo.getHeaderVO();
			
			for (int i = 0; i < (HgPubConst.VUSERDEF).length; i++) {
				head.setAttributeValue(HgPubConst.VUSERDEF[i], null);
			}
			
			GeneralBillItemVO[] items = (GeneralBillItemVO[]) vo.getChildrenVO();
			
			int len = items.length;
			
			for (int i = 0; i < len; i++) {
				items[i].setAttributeValue(HgPubConst.NUM_DEF_QUA, PuPubVO.getUFDouble_NullAsZero(null));
			}
		}

		/**
		 * ���ϸ���
		 * 
		 * @author zhw
		 * @˵�������׸ڿ�ҵ�� 2011-12-8����08:56:47
		 */
		private void onDeal() {
			
			GeneralBillVO vo = null;
			
			try {
				vo = getCurVO();
				
				if (vo == null)
					return;

				if (HgPubConst.purchaseIn_in == HgPubTool.getPurchaseInBusiStatus(vo)) {
					showErrorMsg("�õ����Ѿ����,���ܴ���");
					return;
				}
				
				if (HgPubConst.purchaseIn_ok == HgPubTool.getPurchaseInBusiStatus(vo)
						|| HgPubConst.purchaseIn_nopact == HgPubTool.getPurchaseInBusiStatus(vo)) {
					
					showErrorMsg("�õ��ݺϸ�,���ô���");
					return;
				}

				if (HgPubConst.purchaseIn_arr == HgPubTool.getPurchaseInBusiStatus(vo)) {
					showErrorMsg("�õ���û������,���ܴ���");
					return;
				}
				
				vo.getHeaderVO().setAttributeValue("cuserid",getClientEnvironment().getUser().getPrimaryKey());
				getUnCheckDealDLG().setGbillvo(vo);
				getUnCheckDealDLG().initDataPanel();
				
				if (getUnCheckDealDLG().showModal() != UIDialog.ID_OK) {
					return;
				}

				GeneralBillVO billVO = getUnCheckDealDLG().getGbillvo();
				setDataToUIForUnCheckDeal(billVO);
				
				// ���û���VO
				getM_alListData().set(getM_iLastSelListHeadRow(), billVO);
				setM_voBill(billVO);

			} catch (Exception e) {
				showErrorMsg(HgPubTool.getString_NullAsTrimZeroLen(e
						.getMessage()));
				e.printStackTrace();
			}
		}

		/**
		 * ���ϸ���Ľ�����������
		 * 
		 * @author zhw
		 * @˵�������׸ڿ�ҵ�� 2011-12-8����08:57:55
		 * @param billVO
		 */
		private void setDataToUIForUnCheckDeal(GeneralBillVO billVO) {

			GeneralBillHeaderVO head = billVO.getHeaderVO();
			GeneralBillItemVO[] items = (GeneralBillItemVO[]) billVO.getChildrenVO();
			
			int len = items.length;
			
			if (BillMode.List == getM_iCurPanel()) {
				int selrow = getBillListPanel().getHeadTable().getSelectedRow();
				for (int i = 5; i < 8; i++) {
					setListHeadItem(head.getAttributeValue(HgPubConst.VUSERDEF[i]), selrow,HgPubConst.VUSERDEF[i]);
				}
			
				setListHeadItem(head.getTs(), selrow, "ts");
				
				String[] aryAssistunit = new String[] {
						"fisclose->" + HgPubConst.VUSERDEF[5],
						"updateid->" + HgPubConst.VUSERDEF[7],
						"updatename->getColValue(sm_user,user_name,cuserid,"+ HgPubConst.VUSERDEF[7] + ")" };
				
				getBillListPanel().getHeadBillModel().execFormulas(selrow,aryAssistunit);
				
				for (int i = 0; i < len; i++) {
					getBillListPanel().getBodyBillModel().setValueAt(
									PuPubVO.getUFDouble_NullAsZero(items[i].getAttributeValue(HgPubConst.NUM_DEF_QUA)),i, HgPubConst.NUM_DEF_QUA);
					
					getBillListPanel().getBodyBillModel().setValueAt(items[i].getTs(), i, "ts");
				}
			} else {
			    
				for (int i = 5; i < 8; i++) {
					setCardHeadItem(HgPubConst.VUSERDEF[i], head
							.getAttributeValue(HgPubConst.VUSERDEF[i]));
					setCardHeadItem("ts", head.getTs());
				}

				String[] aryAssistunit = new String[] {
						"fisclose->" + HgPubConst.VUSERDEF[5],
						"updateid->" + HgPubConst.VUSERDEF[7],
						"updatename->getColValue(sm_user,user_name,cuserid,"+ HgPubConst.VUSERDEF[7] + ")" };
				
				getBillCardPanel().execHeadFormulas(aryAssistunit);
				
				for (int i = 0; i < len; i++) {
					getBillCardPanel().getBillModel().setValueAt(
									PuPubVO.getUFDouble_NullAsZero(items[i].getAttributeValue(HgPubConst.NUM_DEF_QUA)),i, HgPubConst.NUM_DEF_QUA);
					
					getBillCardPanel().getBillModel().setValueAt(items[i].getTs(), i, "ts");
				}
			}
		}

		/**
		 * ���ϸ���Ի���
		 * 
		 * @author zhw
		 * @˵�������׸ڿ�ҵ�� 2011-12-8����08:57:30
		 * @return
		 * @throws Exception
		 */
		private UnCheckDealDLG getUnCheckDealDLG() throws Exception {
			if (m_UnCheckDealDLG == null) {
				m_UnCheckDealDLG = new UnCheckDealDLG(this.getClientUI(),
						getClientEnvironment());
			}
			return m_UnCheckDealDLG;
		}

		ArrayList<GeneralBillVO> al = new ArrayList<GeneralBillVO>();

		/**
		 * ������⴦�� �������ʱ�������������µ��������
		 * 
		 * @author zhw
		 * @˵�������׸ڿ�ҵ�� 2011-12-8����09:13:09
		 */
		private void onBillIn() {
		   
			GeneralBillVO vo = null;
			
		    try {
				
		    	vo = getCurVO();
				
		    	if (vo == null) {
					showErrorMsg("��ѡ��Ҫ�����ĵ���");
					return;
				}
				
		    	GeneralBillHeaderVO head = vo.getHeaderVO();
				GeneralBillItemVO[] items = (GeneralBillItemVO[]) vo.getChildrenVO();
				
				int len = items.length;
				
				if (HgPubConst.purchaseIn_in == HgPubTool.getPurchaseInBusiStatus(vo)) {
					showErrorMsg("�õ����Ѿ����,�����ظ����");
					return;
				}

				if (HgPubConst.purchaseIn_arr == HgPubTool.getPurchaseInBusiStatus(vo)) {
					showErrorMsg("�õ���û������,�������");
					return;
				}

				if (HgPubConst.purchaseIn_no == HgPubTool.getPurchaseInBusiStatus(vo)) {// ���ϸ�
					showErrorMsg("�õ��ݲ��ϸ�,�������");
					return;
				}

				if (HgPubConst.purchaseIn_nopact == HgPubTool.getPurchaseInBusiStatus(vo)) {
					showErrorMsg("�õ���û�к�ͬ��Ϣ,����ƥ���ͬ��Ϣ,Ȼ�����");
					return;
				}

				super.onUpdate();
				
				setAdjustFlag(true);
				setHeadEditEnableWhenBillIn(false);
				setButtonEnable(ICButtonConst.BTN_LINE, false);// �����в�����ť������
				setButtonsEnable(false);// �����ʼ찴ť�Ƿ����
			
				// �����������
				for (int i = 0; i < len; i++) {
					
					UFDouble nkdnum = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel().getValueAt(i,HgPubConst.NUM_DEF_QUA));
					getBillCardPanel().getBillModel().setValueAt(nkdnum, i,"ninnum");
					getBillCardPanel().getBillModel().setRowState(i,BillModel.MODIFICATION);// ���ñ����޸�̬
					
					// ���岻�޸�ʱҲ���Խ��Զ��������ݱ���
					items[i].setNinnum(nkdnum);
					getBillCardPanel().getBillModel().setValueAt(ClientEnvironment.getInstance().getDate(), i,"dbizdate");
					items[i].setDbizdate(ClientEnvironment.getInstance().getDate());
					UFDouble hsl = PuPubVO.getUFDouble_ZeroAsNull(getBillCardPanel().getBillModel().getValueAt(i, "hsl"));
					
					if (hsl != null) {
						getBillCardPanel().getBillModel().setValueAt(nkdnum.div(hsl, HgPubConst.NUM_DIGIT), i,"ninassistnum");
						items[i].setNinassistnum(nkdnum.div(hsl,HgPubConst.NUM_DIGIT));
					}
					
					UFDouble nprice = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel().getValueAt(i, "nprice"));
					UFDouble nplanprice = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel().getValueAt(i,"nplannedprice"));

					getBillCardPanel().getBillModel().setValueAt(nkdnum.multiply(nprice, HgPubConst.MNY_DIGIT), i,"nmny");
					getBillCardPanel().getBillModel().setValueAt(nkdnum.multiply(nplanprice, HgPubConst.MNY_DIGIT),i, "nplannedmny");

					items[i].setNmny(nkdnum.multiply(nprice,HgPubConst.MNY_DIGIT));
					items[i].setNplannedmny(nkdnum.multiply(nplanprice,HgPubConst.MNY_DIGIT));

				}
				
				// ���û���VO
				getM_alListData().set(getM_iLastSelListHeadRow(), vo);
				setM_voBill(vo);

			} catch (Exception e) {
				showErrorMsg(HgPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
				e.printStackTrace();
			}
		}

		/**
		 * ���ñ���Ԫ�ز��ɱ༭��ʵ���������⣩
		 * 
		 * @author zhw
		 * @˵�������׸ڿ�ҵ�� 2011-12-14����05:10:35
		 * @param flag
		 */
		private void setAdjustFlag(boolean flag) {
			m_isAdjust = flag;
		}

		/**
		 * ���ò�����ť������
		 * 
		 * @author zhw
		 * @˵�������׸ڿ�ҵ�� 2011-12-15����10:53:46
		 * @param buttonName
		 * @param flag
		 */
		private void setButtonEnable(String buttonName, boolean flag) {
		    
			getButtonManager().getButtonTree().getButton(buttonName).setEnabled(flag);
			getClientUI().updateButton(getButtonManager().getButton(buttonName));// ���ò�����ť������
		}

		// ���� �׸ڿ�ҵ���Ӱ�ť�ı༭��
		private void setButtonsEnable(boolean flag) {
			
			setButtonEnable(HgPuBtnConst.ACPT, flag);// �����ʼ찴ť����
			setButtonEnable(HgPuBtnConst.UNACPT, flag);
			setButtonEnable(HgPuBtnConst.UNDEAL, flag);
			setButtonEnable(HgPuBtnConst.WARE, flag);
			setButtonEnable(HgPuBtnConst.MATCH, flag);
		}

		/**
		 * ���ñ�ͷԪ�ز��ɱ༭
		 * 
		 * @author zhw
		 * @˵�������׸ڿ�ҵ�� 2011-12-14����05:10:35
		 * @param flag
		 */
		private void setHeadEditEnableWhenBillIn(boolean flag) {
			
			String[] headItems = HgPubConst.PurchaseIn_HeadItems;
			for (String key : headItems) {
				getBillCardPanel().getHeadItem(key).setEdit(flag);
			}
		}
		
		/**
		 * 
		 * @author zhw
		 * @˵�������׸ڿ�ҵ��
		 * 2011-4-14����05:02:26
		 * @param msg
		 */
		public void setAfterIn(boolean flag){
			
			String[] head={"dbilldate","cwarehouseid","cbiztype","cdispatcherid","cwhsmanagerid","cbizid","cdptid","cproviderid"};
			String[]  body={"vbatchcode","vuserdef18","vuserdef19","vnotebody","nprice","nmny","dbizdate"};
		
			for (String key : head) {
				getBillCardPanel().getHeadItem(key).setEdit(flag);
			}
//		     getBillCardPanel().getBillModel().setEnabled(false);
			for (String key : body) {
				getBillCardPanel().getBillModel().getItemByKey(key).setEnabled(flag);
			}
			
		}

		protected void onCancel() {
			
			GeneralBillVO vo = null;
			
			vo = getCurVO();
			
			if (HgPubConst.purchaseIn_in == HgPubTool.getPurchaseInBusiStatus(vo)) {
				
				super.onCancel();
			} else {
				
				super.onCancel();
				
				if (!PuPubVO.getUFBoolean_NullAs(getBillCardPanel().getHeadItem("freplenishflag").getValueObject(), UFBoolean.FALSE).booleanValue()) {
					
					setAdjustFlag(false);
					setButtonsEnable(true);// �����ʼ찴ť����
					
					if (vo == null) {
						showErrorMsg("��ѡ��Ҫȡ���ĵ���");
						return;
					}
					
					GeneralBillItemVO[] items = (GeneralBillItemVO[]) vo.getChildrenVO();
					
					int len = items.length;
					
					// �����������
					for (int i = 0; i < len; i++) {
						
						if (!UFDouble.ZERO_DBL.equals(PuPubVO.getUFDouble_NullAsZero(items[i].getNinnum()))) {
							
							getBillCardPanel().getBillModel().setValueAt(UFDouble.ZERO_DBL, i, "ninnum");
							items[i].setNinnum(UFDouble.ZERO_DBL);
							getBillCardPanel().getBillModel().setValueAt(null,i, "dbizdate");
							items[i].setDbizdate(null);
						    
							UFDouble hsl = PuPubVO.getUFDouble_ZeroAsNull(getBillCardPanel().getBillModel().getValueAt(i, "hsl"));
							
							if (hsl != null) {
								
								getBillCardPanel().getBillModel().setValueAt(UFDouble.ZERO_DBL.div(hsl,HgPubConst.NUM_DIGIT), i,"ninassistnum");
								items[i].setNinassistnum(UFDouble.ZERO_DBL.div(hsl, HgPubConst.NUM_DIGIT));
							}
							
							UFDouble nprice = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel().getValueAt(i,"nprice"));
							getBillCardPanel().getBillModel().setValueAt(UFDouble.ZERO_DBL.multiply(nprice,HgPubConst.MNY_DIGIT), i, "nmny");
							items[i].setNmny(UFDouble.ZERO_DBL.multiply(nprice,HgPubConst.MNY_DIGIT));
						}
					}
					
					setM_voBill(vo);
				}
			}
		}

		/**
		 * ��ѯ�� ����ⰴť״̬��ԭ
		 */
		protected void onQuery(boolean flag) {
			super.onQuery(flag);
			setButtonsEnable(true);
		}

		// zhw end

		/**
		 * 
		 * �����������������ɹ̶��ʲ���Ƭ��
		 * <p>
		 * <b>����˵��</b>
		 * <p>
		 * 
		 * @author duy
		 * @time 2008-5-22 ����10:14:45
		 */
		private void onGenerateAssetCard() {
			try {
				// ���ú�̨�ӿ����ɹ̶��ʲ���Ƭ
				String ts = (String) nc.ui.ic.pub.tools.GenMethod
						.callICService("nc.bs.ic.ic201.GeneralHBO",
								"generateAssetCard", new Class[] {
										GeneralBillVO.class, String.class },
								new Object[] {
										getM_voBill(),
										getClientEnvironment().getUser()
												.getPrimaryKey() });
				getM_voBill().getHeaderVO().setTs(ts);
				getM_voBill().getHeaderVO().setBassetcard(UFBoolean.TRUE);
				setBillVO(getM_voBill());
				updateBillToList(getM_voBill());
				setButtonStatus(true);
			} catch (Exception e) {
				// ��־�쳣
				nc.vo.scm.pub.SCMEnv.out(e);
				showErrorMessage(e.getMessage());
			}
		}

		/**
		 * 
		 * ��������������ȡ�����ɹ̶��ʲ���Ƭ��
		 * <p>
		 * <b>����˵��</b>
		 * <p>
		 * 
		 * @author duy
		 * @time 2008-5-22 ����10:26:35
		 */
		private void onCancelGenerateAssetCard() {
			try {
				// ���ú�̨�ӿ����ɹ̶��ʲ���Ƭ
				String ts = (String) nc.ui.ic.pub.tools.GenMethod
						.callICService("nc.bs.ic.ic201.GeneralHBO",
								"cancelGenerateAssetCard", new Class[] {
										GeneralBillVO.class, String.class },
								new Object[] {
										getM_voBill(),
										getClientEnvironment().getUser()
												.getPrimaryKey() });
				getM_voBill().getHeaderVO().setTs(ts);
				getM_voBill().getHeaderVO().setBassetcard(UFBoolean.FALSE);
				setBillVO(getM_voBill());
				updateBillToList(getM_voBill());
				setButtonStatus(true);
			} catch (Exception e) {
				// ��־�쳣
				nc.vo.scm.pub.SCMEnv.out(e);
				showErrorMessage(e.getMessage());
			}
		}

		/**
		 * v5:����ʱ����ͷ�Ĺ�˾����Ϊ���������˾�Ͳɹ���˾
		 * 
		 */
		protected void onAddLine() {
			super.onAddLine();
			int sel = getBillCardPanel().getBillTable().getSelectedRow();
			if (sel < 0)
				return;

			setReqAndInvField(sel);
		}

		/**
		 * v5:����ʱ����ͷ�Ĺ�˾����Ϊ���������˾�Ͳɹ���˾
		 * 
		 */
		protected void onInsertLine() {
			super.onInsertLine();
			int sel = getBillCardPanel().getBillTable().getSelectedRow();
			if (sel < 0)
				return;
			setReqAndInvField(sel);
		}

		/**
		 * �����ߣ����˾� ���ܣ����棬����ǽ�ת���͵ģ����λ�����кš� ������ ���أ� ���⣺ ���ڣ�(2001-11-24 12:15:42)
		 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
		 * 
		 */
		protected boolean onSave() {
			// add by zhw У������֮��Ĺ�ϵ
			int rowCount = getBillCardPanel().getBillModel().getRowCount();

			// zhf add ��ⵥ���ܴ�����ͬ������� ԭ�� ͬһ����ͬһ�����������ͬ������
			List<String> linv = new ArrayList<String>();
			for (int i = 0; i < rowCount; i++) {
				String invid = PuPubVO
						.getString_TrimZeroLenAsNull(getBillCardPanel()
								.getBodyValueAt(i, "cinvbasid"));
				if (invid == null) {
					showErrorMessage("��" + (i + 1) + "�д������Ϊ��");
					return false;
				}
				if (linv.contains(invid)) {
					showErrorMessage("ͬһ���ݶ��б��岻�ܴ�����ͬ���");
					return false;
				}
				linv.add(invid);
				// zhf end20110211
				UFDouble nkdnum = PuPubVO
						.getUFDouble_NullAsZero(getBillCardPanel()
								.getBillModel().getValueAt(i,
										HgPubConst.NUM_DEF_QUA));// ���պϸ�����
				UFDouble arr = PuPubVO
						.getUFDouble_NullAsZero(getBillCardPanel()
								.getBillModel().getValueAt(i,
										HgPubConst.NUM_DEF_ARR));// ��������
				UFDouble fac = PuPubVO
						.getUFDouble_NullAsZero(getBillCardPanel()
								.getBillModel().getValueAt(i,
										HgPubConst.NUM_DEF_FAC));// ʵ������

				UFDouble shouldInNum = PuPubVO
						.getUFDouble_NullAsZero(getBillCardPanel()
								.getBillModel().getValueAt(i, "nshouldinnum"));// ��ͬ����

				//
				UFDouble ninnum = PuPubVO
						.getUFDouble_NullAsZero(getBillCardPanel()
								.getBillModel().getValueAt(i, "ninnum"));// ʵ������
				// ������������ �ҽ��л�λ���� ���λ����
//				String cwarehouseid = PuPubVO
//						.getString_TrimZeroLenAsNull(getBillCardPanel()
//								.getHeadItem("cwarehouseid").getValueObject());

//				try {
//					if (getBillCardPanel().getBillModel().getValueAt(i,
//							"ninnum") != null
//							&& HgICPubHealper.getCsflag(cwarehouseid)) {
//						if (getBillCardPanel().getBillModel().getValueAt(i,
//								"cspaceid") == null ||"_________N/A________".equals(getBillCardPanel().getBillModel().getValueAt(i,
//								"cspaceid"))) {
//							showErrorMessage("��" + (i + 1) + "�л�λ����Ϊ��");
//							return false;
//						}
//					}
//				} catch (BusinessException e) {
//					e.printStackTrace();
//					showErrorMessage("��ȡ�òֿ��Ƿ���л�λ�������");
//					return false;
//				}
				if (!PuPubVO.getUFBoolean_NullAs(
						getBillCardPanel().getHeadItem("freplenishflag")
								.getValueObject(), UFBoolean.FALSE)
						.booleanValue()) {
					if (UFDouble.ZERO_DBL.compareTo(arr) >= 0) {
						showErrorMessage("��" + (i + 1) + "�е����������������");
						return false;
					}

					if (UFDouble.ZERO_DBL.compareTo(fac) >= 0) {
						showErrorMessage("��" + (i + 1) + "��ʵ���������������");
						return false;
					}
					if (arr.compareTo(fac) < 0) {
						showErrorMessage("��" + (i + 1) + "��ʵ���������ܴ��ڵ�������");
						return false;
					}

					if (!UFDouble.ZERO_DBL.equals(nkdnum)) {
						if (UFDouble.ZERO_DBL.compareTo(ninnum) >= 0) {
							showErrorMessage("��" + (i + 1) + "������������������");
							return false;
						} else if (ninnum.compareTo(nkdnum) > 0) {
							showErrorMessage("��" + (i + 1) + "������������ܴ�����������");
							return false;
						}
					}
				}
			}
			// add by zhw end
			if (isBrwLendBiztype()) {
				// ����ǰ���λ����
				m_alLocatorDataBackup = getM_alLocatorData();
				setM_alLocatorData(null);
				// ����ǰ�����к�����
				m_alSerialDataBackup = getM_alSerialData();
				setM_alSerialData(null);
			}
			// ������Դͷ���ݺŵ���Ʒ�����Ƿ����0

			// �޸��ˣ������� �޸����ڣ�2007-8-13����04:51:06
			// �޸�ԭ�򣺽����������,�����˻�����ⵥ��¼�����������Ϊ���ģ�Ȼ����������ʱ��¼�����룬���ӵ�����Ҳ��Ϊ���ġ�
			if (super.onSave()) {
				setFixBarcodenegative(false);// ��������Ϊ����
				// add by zhw 2010-12-14 ���ñ���༭��
				setAdjustFlag(false);// ���ñ���༭��
				setButtonsEnable(true);// �����ʼ찴ť�Ƿ����
				//�����֮������޸�һЩ�ֶ�
				// zhw end
				return true;
			}
			return false;
		}

		/**
		 * �����ߣ����˾� ���ܣ����кŷ��� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
		 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
		 * 
		 * 
		 * 
		 * 
		 */
		public void onSNAssign() {
			// �����ģʽ�£�����ǽ�ת���͵ģ�����Ҫ�˲�����
			if (BillMode.Browse != getM_iMode() && isBrwLendBiztype()) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000273")/*
															 * @res
															 * "����ҵ�����ͣ�����Ҫִ�д˲������������״̬�²鿴��"
															 */);
				return;
			} else {
				if (isBrwLendBiztype()) {

					GeneralBillVO voMyBill = null;
					if (getM_alListData() != null
							&& getM_iLastSelListHeadRow() >= 0
							&& getM_alListData().size() > getM_iLastSelListHeadRow()
							&& getM_alListData()
									.get(getM_iLastSelListHeadRow()) != null) {
						voMyBill = ((GeneralBillVO) getM_alListData().get(
								getM_iLastSelListHeadRow()));
						String sBillPK = (String) voMyBill.getItemValue(0,
								"cfirstbillhid");
						if (sBillPK == null || sBillPK.trim().length() == 0) {
							showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008busi",
											"UPP4008busi-000274")/*
																	 * @res
																	 * "û�ж�Ӧ�Ľ���/�������޷���ѯ����λ�����к����ݡ����鵥����Դ��"
																	 */);
							return;
						} else
							showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008busi",
											"UPP4008busi-000275")/*
																	 * @res
																	 * "��쿴��ؽ���/����������ݡ�"
																	 */);
					}
					return;
				}
			}
			super.onSNAssign();
		}

		/**
		 * �����ߣ����˾� ���ܣ���λ���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
		 * 
		 * 
		 * 
		 * 
		 */
		public void onSpaceAssign() {
			// �����ģʽ�£�����ǽ�ת���͵ģ�����Ҫ�˲�����
			if (BillMode.Browse != getM_iMode() && isBrwLendBiztype()) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000273")/*
															 * @res
															 * "����ҵ�����ͣ�����Ҫִ�д˲������������״̬�²鿴��"
															 */);
				return;
			} else {
				if (isBrwLendBiztype()) {
					GeneralBillVO voMyBill = null;
					if (getM_alListData() != null
							&& getM_iLastSelListHeadRow() >= 0
							&& getM_alListData().size() > getM_iLastSelListHeadRow()
							&& getM_alListData()
									.get(getM_iLastSelListHeadRow()) != null) {
						voMyBill = ((GeneralBillVO) getM_alListData().get(
								getM_iLastSelListHeadRow()));
						String sBillPK = (String) voMyBill.getItemValue(0,
								"cfirstbillhid");
						if (sBillPK == null || sBillPK.trim().length() == 0) {
							showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008busi",
											"UPP4008busi-000274")/*
																	 * @res
																	 * "û�ж�Ӧ�Ľ���/�������޷���ѯ����λ�����к����ݡ����鵥����Դ��"
																	 */);
							return;
						} else
							showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008busi",
											"UPP4008busi-000275")/*
																	 * @res
																	 * "��쿴��ؽ���/����������ݡ�"
																	 */);
					}
					return;
				}
			}
			super.onSpaceAssign();
		}

		/**
		 * ���׹��ܰ�ť������ ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-04-18 10:43:46)
		 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
		 */
		private void onDispense() {
			IBillType billType = BillTypeFactory.getInstance().getBillType(
					getSourBillTypeCode());
			if (BillMode.Browse == getM_iMode() && isSigned() != SIGNED
					&& getSourBillTypeCode() != null
					&& !billType.typeOf(nc.vo.ic.pub.billtype.ModuleCode.IC)) {

			} else
				return;

			if (getBillCardPanel().getBillTable().getSelectedRows().length >= 1) {

				if (nc.ui.pub.beans.UIDialog.ID_CANCEL == nc.ui.pub.beans.MessageDialog
						.showOkCancelDlg(getClientUI(), null,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"4008busi", "UPP4008busi-000268")/*
																			 * @res
																			 * "�Ƿ�Ա�����ѡ���������г��׼��Զ��������״�����������������ⵥ��?"
																			 */)) {
					return;

				}

				// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				// "4008busi", "UPT40080602-000007")/* @res "����" */);

				GeneralBillVO voBill = (GeneralBillVO) ((GeneralBillVO) getM_alListData()
						.get(getM_iLastSelListHeadRow())).clone();
				GeneralBillVO voBillclone = (GeneralBillVO) voBill.clone();

				ArrayList alOutGeneralVO = new ArrayList();
				ArrayList alInGeneralVO = new ArrayList();

				ArrayList aloutitem = new ArrayList();
				ArrayList alinitem = new ArrayList();
				int[] rownums = getBillCardPanel().getBillTable()
						.getSelectedRows();

				for (int i = 0; i < rownums.length; i++) {

					if (!isSetInv(voBill, rownums[i])
							|| isDispensedBill(voBill, rownums[i]))
						continue;

					// ������
					GeneralBillItemVO voParts = voBill.getItemVOs()[rownums[i]];// searchInvKit(voBill.getItemVOs()[rownums[i]]);
					UFDouble ufSetNum = null;
					// if (voParts != null) {
					// outnum to innum
					ufSetNum = voParts.getNinnum();
					voParts.setAttributeValue("nshouldoutnum", voParts
							.getNinnum());
					voParts.setAttributeValue("nshouldoutassistnum", voParts
							.getNinassistnum());
					voParts.setAttributeValue("noutnum", voParts.getNinnum());
					voParts.setAttributeValue("noutassistnum", voParts
							.getNinassistnum());
					// after set null to noutnum and noutassistnum zhx 030616
					voParts.setAttributeValue("ninnum", null);
					voParts.setAttributeValue("ninassistnum", null);
					voParts.setAttributeValue("nshouldinnum", null);
					voParts.setAttributeValue("nneedinassistnum", null);
					voParts.setDbizdate(new nc.vo.pub.lang.UFDate(
							getEnvironment().getLogDate()));

					// soucebill
					voParts.setAttributeValue("csourcetype", voBill
							.getHeaderVO().getCbilltypecode());
					voParts.setAttributeValue("csourcebillhid", voBill
							.getHeaderVO().getPrimaryKey());
					voParts.setAttributeValue("csourcebillbid", voBill
							.getItemVOs()[rownums[i]].getPrimaryKey());
					voParts.setAttributeValue("vsourcebillcode", voBill
							.getHeaderVO().getVbillcode());
					// �޸���:������ �޸�����:2007-04-12
					// �޸�ԭ��:���ڳ���������Ĵ������Ӧ��ⵥ�źͶ�Ӧ�������к��Զ�Я�������ɳ��ⵥ��
					voParts.setAttributeValue("ccorrespondcode", voBill
							.getHeaderVO().getVbillcode());
					voParts.setAttributeValue("ccorrespondbid", voBill
							.getItemVOs()[rownums[i]].getCgeneralbid());

					voParts.setCgeneralbid(null);
					voParts.setCgeneralbb3(null);
					voParts.setCsourceheadts(null);
					voParts.setCsourcebodyts(null);

					aloutitem.add(voParts);
					// alOutGeneralVO.add(gbvoOUT);

					// ���������VO��Ӧ�ǲɹ���ⵥ�ݵ���������

					voParts.setLocator(null);// zhy
					GeneralBillItemVO[] tempItemVO = splitInvKit(voParts,
							voBillclone.getHeaderVO(), ufSetNum);
					if (tempItemVO != null && tempItemVO.length > 0) {
						for (int j = 0; j < tempItemVO.length; j++) {
							alinitem.add(tempItemVO[j]);

						}

					} else {
						showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("4008busi", "UPP4008busi-000270")/*
																				 * @res
																				 * "�������׼�û�ж���������붨��������ٽ������ף�"
																				 */);
						return;
					}
				}

				if (aloutitem.size() == 0 || alinitem.size() == 0)

					return;

				GeneralBillVO gbvoOUT = new GeneralBillVO();
				voBill.getHeaderVO().setCoperatorid(
						getEnvironment().getUserID());
				voBill.getHeaderVO()
						.setDbilldate(
								new nc.vo.pub.lang.UFDate(getEnvironment()
										.getLogDate()));
				gbvoOUT.setParentVO(voBill.getParentVO());
				gbvoOUT.getHeaderVO().setPrimaryKey(null);
				gbvoOUT.getHeaderVO().setVbillcode(null);
				gbvoOUT.getHeaderVO().setCbilltypecode(
						nc.vo.ic.pub.BillTypeConst.m_otherOut);
				gbvoOUT.getHeaderVO().setStatus(nc.vo.pub.VOStatus.NEW);
				gbvoOUT.getHeaderVO().setAttributeValue("bdispenseflag", "Y");

				GeneralBillItemVO[] outbodys = new GeneralBillItemVO[aloutitem
						.size()];
				aloutitem.toArray(outbodys);

				gbvoOUT.setChildrenVO(outbodys);

				nc.ui.scm.pub.report.BillRowNo.setVORowNoByRule(gbvoOUT,
						nc.vo.ic.pub.BillTypeConst.m_otherOut, "crowno");

				alOutGeneralVO.add(gbvoOUT);

				GeneralBillVO gbvoIn = new GeneralBillVO();
				gbvoIn.setParentVO(voBillclone.getParentVO());
				gbvoIn.getHeaderVO().setPrimaryKey(null);
				gbvoIn.getHeaderVO().setVbillcode(null);
				gbvoIn.getHeaderVO().setCbilltypecode(
						nc.vo.ic.pub.BillTypeConst.m_otherIn);
				gbvoIn.getHeaderVO().setStatus(nc.vo.pub.VOStatus.NEW);
				gbvoIn.getHeaderVO().setAttributeValue("bdispenseflag", "Y");

				GeneralBillItemVO[] inbodys = new GeneralBillItemVO[alinitem
						.size()];
				alinitem.toArray(inbodys);

				gbvoIn.setChildrenVO(inbodys);

				// // �ɹ�������ɵ������ⵥ��Ҫ���õ��ݺ�
				nc.ui.scm.pub.report.BillRowNo.setVORowNoByRule(gbvoIn,
						nc.vo.ic.pub.BillTypeConst.m_otherIn, "crowno");

				alInGeneralVO.add(gbvoIn);

				getDispenseDlg(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
								"UPP4008busi-000269")/*
														 * @res "�������ɣ�������/�������ⵥ"
														 */, alInGeneralVO, alOutGeneralVO).showModal();
				if (m_dlgInOut.isOK()) {
					try { // ���±�β
						// setAuditBillFlag();
						filterNullLine();

						setDispenseFlag(
								(GeneralBillVO) ((GeneralBillVO) getM_alListData()
										.get(getM_iLastSelListHeadRow())),
								rownums);
						// super.freshStatusTs(voBill.getHeaderVO().getPrimaryKey());

						setM_voBill((GeneralBillVO) ((GeneralBillVO) getM_alListData()
								.get(getM_iLastSelListHeadRow())).clone());
						super.setButtonStatus(false);
						// ���׳ɹ�����Ҫ���õ��ݵİ�ť��ɾ�����޸ģ����ư�ť�����ã���

						// setBillState();
						// can not dispense the inv more over, after create
						// the other in and out bill!
						// m_boDispense.setEnabled(false);
						ctrlSourceBillButtons(true);
						/*
						 * for(int i=0;i<rownums.length;i++)
						 * super.refreshSelBill(rownums[i]);
						 */
						// �޸�bug-NCdp200017720-���ȱ��:NCdp200016800-������-200603191641--����Ts
						String billCardPrimaryKey = getBillCardPanel()
								.getHeadItem("cgeneralhid").getValue()
								.toString().trim();
						ArrayList alFreshRet = (ArrayList) GeneralBillHelper
								.queryInfo(new Integer(
										QryInfoConst.BILL_STATUS_TS),
										billCardPrimaryKey);
						if (alFreshRet == null || alFreshRet.get(0) == null) {
							SCMEnv.out("Err,ret");
						}
						// set
						// ts
						if (alFreshRet != null && alFreshRet.size() >= 2
								&& alFreshRet.get(1) != null) {
							ArrayList alTs = (ArrayList) alFreshRet.get(1);
							GeneralBillUICtl.updateDataAfterDispense(
									getClientUI(), getM_voBill());
							// freshTs(alTs);
						}
						// �޸�bug-NCdp200017720-���ȱ��:NCdp200016800-������-200603191641

					} catch (Exception e) {
						handleException(e);
						showErrorMessage(e.getMessage());
					}
				}
			}
		}

		/**
		 * �ɹ�����ʱ������������۶ּ���ķ��������� ë��-Ƥ�� ���ص����� ���������õ���������
		 * 
		 * @author ljun
		 * @since v5 �߲ɹ�->�ʼ�->�������̲��п۶ּ���
		 */
		private void onKDJS() {

			// ��ǰ����״̬Ϊ�༭
			if (getM_iMode() == BillMode.Browse) {
				showWarningMessage(ResBase.get201KD01());
				return;
			}
			int rows = getBillCardPanel().getBillTable().getRowCount();
			if (rows <= 0) {
				return;
			}
			// ��ǰ��˾�Ͳɹ���˾�����ʱ�������۶�
			String purcorp = getBillCardPanel().getHeadItem(IItemKey.PUR_CORP) == null ? null
					: getBillCardPanel().getHeadItem(IItemKey.PUR_CORP)
							.getValue();
			if (purcorp == null) {
				return;
			} else {
				if (!purcorp.equals(getEnvironment().getCorpID())) {
					showWarningMessage(ResBase.get201KD03());
					return;
				}
			}
			// ���������鵽���ĲŽ��п۶�
			if (getBillCardPanel().getBodyValueAt(0, "csourcebillbid") == null
					|| getBillCardPanel().getBodyValueAt(0, "csourcetype") == null
					|| !getBillCardPanel().getBodyValueAt(0, "csourcetype")
							.toString().equalsIgnoreCase("23")) {
				showWarningMessage(ResBase.get201KD02());
				return;
			}

			clearBillBodyItem(getBillCardPanel(), "nkdnum");

			GeneralBillItemVO[] voaItem = getM_voBill().getItemVOs();
			if (voaItem == null)
				return;

			UFDouble[] ufdArray = null;
			try {
				IPricStl obj = (IPricStl) NCLocator.getInstance().lookup(
						IPricStl.class.getName());
				ufdArray = obj.servForQnty(getM_voBill().getItemVOs(),
						new ClientLink(getClientEnvironment()));
			} catch (BusinessException exx) {
				nc.vo.scm.pub.SCMEnv.error(exx);
				showHintMessage(exx.getMessage());
			}

			// ���ÿ۶ֵ�����
			HashMap map = new HashMap();
			if (ufdArray == null)
				return;
			for (int i = 0; i < ufdArray.length; i++) {
				Integer iI = new Integer(i);

				UFDouble ufdGrossNum = (UFDouble) voaItem[i]
						.getAttributeValue("ningrossnum");
				if (ufdGrossNum == null || ufdArray[i] == null)
					map.put(iI, null);
				else {
					map.put(iI, ufdArray[i]);// �۶�����
				}
			}

			// ���ÿ۶�����������۶������ֶ� //����ʵ������: ʵ������������ë��-Ƥ��-koudun
			if (getBillCardPanel().getBodyItem("nkdnum") != null) {

				for (int i = 0; i < rows; i++) {
					if (getBillCardPanel().getBodyValueAt(i, "cinventoryid") == null)
						continue;
					Integer iX = new Integer(i);

					UFDouble ufd = (UFDouble) map.get(iX);
					getBillCardPanel().setBodyValueAt(ufd, i, "nkdnum");

					//
					// exec num's editformula
					getBillCardPanel().execBodyFormula(i, "ninnum");
					// ���������ȱ仯
					Object vl = getBillCardPanel().getBodyValueAt(i, "ninnum");

					getBillCardPanel().execBodyFormula(i, "nkdnum");

					getEditCtrl().afterNumEdit(
							new BillEditEvent(this, null, vl, "ninnum", i,
									BillItem.BODY));

					getBillCardPanel().getBillModel().setRowState(i,
							BillModel.MODIFICATION);

				}
			}
		}

		/**
		 * �˴����뷽��˵���� �������ڣ�(2003-10-9 14:43:10) �����˿ⵥ �˻���־Ϊ"Y"�� �˻����ɿɱ༭��
		 * ����������˻����ɲ��ɱ༭�� ��Ҫ����onNew(),���˻�������Ϊ���ɱ༭��
		 */
		public void onNewReplenishInvBill() {
			super.onAdd();
			// ���ñ�����˻����ɺͱ�ͷ���˻����ɿ��Ա༭
			// ������������Ǹ���
			nc.ui.ic.pub.bill.GeneralBillUICtl.setSendBackBillState(
					getClientUI(), true);
			getM_voBill().getHeaderVO().setFreplenishflag(new UFBoolean(true));

			setFixBarcodenegative(true);// ��������Ϊ����

			getBillCardPanel().getHeadItem("cproviderid").setEdit(true);
			getBillCardPanel().getHeadItem("cproviderid").setEnabled(true);
		}

		/**
		 * �˴����뷽��˵���� �������ڣ�(2003-10-9 14:43:10) ���ղɹ��˻����������˿ⵥ��by hanwei
		 * 2003-10-14
		 * 
		 * 
		 */
		public void onNewReplenishInvBillByOrder() {

			IFromPoUI ui = null;
			try {
				ui = (IFromPoUI) Class.forName("nc.ui.ic.ic201.FromPoUI")
						.newInstance();
				setBillEdit();// add by zhw �˿�ʱ���õ����ֶεĿɱ༭��
			} catch (Exception e) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000272")/*
															 * @res
															 * "�ù��ܲ����ã�ԭ�򣺲ɹ�����ϵͳû�а�װ����"
															 */);
				return;
			}
			ui.onNewReplenishInvBillByOrder((ClientUI) getClientUI(),
					getClientUI().getEnvironment().getCorpID());

		}
	}

	/**
	 * �˿��ʱ�� ���ñ�ͷ����Ŀɱ༭��
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-3-26����10:07:07
	 */
	private void setBillEdit() {
		String[] strs = { "vuserdef18", "vuserdef19"};
		for (String str : strs) {
			getBillCardPanel().getBillModel().getItemByKey(str).setEnabled(
					false);
		}

	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�� ȡ����ͬƥ��  ���ǰ  ��ͬƥ���
	 * 2011-4-23����01:18:20
	 */
	private void onCancelMatchPact(){
		try {
			GeneralBillVO gbillvo = getCurVO();
			if(gbillvo == null){
				showHintMessage("��ǰ����Ϊ��");
				return;
			}		
			
			if (HgPubConst.purchaseIn_in == HgPubTool
					.getPurchaseInBusiStatus(gbillvo)) {
				showWarningMessage("�õ����Ѿ����,����ȡ����ͬƥ��");
				return;
			}
			GeneralBillHeaderVO head = gbillvo.getHeaderVO();
			if(!HgPubTool.getString_NullAsTrimZeroLen(head.getAttributeValue(HgPubConst.F_IS_SELF)).equalsIgnoreCase(HgPubConst.SELF)){
				showWarningMessage("��Դ�ں�ͬ��������յ�����ȡ��ƥ���ͬ");
				return;
			}
			GeneralBillItemVO[] gitems = gbillvo.getItemVOs();
			for(GeneralBillItemVO gitem:gitems){
				if(PuPubVO.getString_TrimZeroLenAsNull(gitem.getCsourcebillbid())==null||!HgPubTool.getString_NullAsTrimZeroLen(gitem.getCsourcetype()).equalsIgnoreCase(ScmConst.PO_Order)){
					showWarningMessage("δƥ���ͬ");
					return;
				}
			}
			
			gbillvo.getHeaderVO().setAttributeValue("cuserid", ClientEnvironment.getInstance().getUser().getPrimaryKey());
			
//			�����̨ȡ����ͬƥ�����
			Map<String,String> tsInfor = (Map<String,String>)HgICPubHealper.cancelPactMatch(gbillvo);
			for(GeneralBillItemVO gitem:gitems){
				gitem.setTs(tsInfor.get(gitem.getPrimaryKey()));
				gitem.setCsourcebillbid(null);
				gitem.setCsourcebillhid(null);
				gitem.setCsourcetype(null);
				gitem.setNshouldinnum(null);
			}
			
			if (getCurPanel() == BillMode.Card) {
				getBillCardPanel().setBillValueVO(gbillvo);
				getBillCardPanel().getBillModel().execLoadFormula();
			} else {
				getBillListPanel().getBodyBillModel().setBodyDataVO(gbillvo.getItemVOs());
				getBillListPanel().getBodyBillModel().execLoadFormula();
			}

			gbillvo.setChildrenVO(getBodyVOs());
			getM_alListData().set(getM_iLastSelListHeadRow(),
					gbillvo);
			setM_voBill(gbillvo);
			// }

		} catch (Exception e) {
			e.printStackTrace();
			showErrorMessage(HgPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
		}
	
	}
	
	/**
	 * spf excel����
	 */
	public void onExportBtnsClick(ButtonObject bo) {
			try {
				showHintMessage("");
				if (getBillListPanel().isShowing() == false) {
					onBoCardDerived();
				} else {
					onBoListDerived();
				}
			} catch (Exception e) {
				this.showWarningMessage(e.getMessage());
			}
	}
	
	private void onBoListDerived() throws BusinessException {
		File file = null;
		File[] files = null;
		ArrayList alBill = getSelectedBills();
		if (alBill == null || alBill.size() == 0){
			showHintMessage("���Ȳ�ѯ��¼�뵥��");
			return;
		}
		if (getUIFileChooser().showSaveDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
			files = getUIFileChooser().getSelectedFiles();
			
		}
		if (files == null || "".equals(files)) {
			return;
		}
		file = files[0];

		TransletFactory tf = new TransletFactory();
		int len = alBill.size();
          int m=0;
		for (int x = 0; x < len; x++) {
//			String filename = PuPubVO
//					.getString_TrimZeroLenAsNull(((GeneralBillVO) alBill.get(x)).getHeaderValue("vbillcode"));
			
			if(!"3".equalsIgnoreCase(PuPubVO.getString_TrimZeroLenAsNull(((GeneralBillVO) alBill.get(x)).getHeaderValue("fbillflag")))){
				showHintMessage("����δǩ��,���ܵ���");
				continue;
			}
			String[] strs =null;
			String s = file.getPath();
			
			strs = tf.splitCode(s);
		
			String filepath = strs[0]+"("+PuPubVO.getString_TrimZeroLenAsNull(((GeneralBillVO) alBill.get(x)).getHeaderValue("vbillcode"))+")"+".xls";
			
			try {
				
				// int m = i[x];
				 m=onBoListDerived(m,filepath,(GeneralBillVO) alBill.get(x));
				
			} catch (Exception e) {
				e.printStackTrace();
				showErrorMessage(PuPubVO.getString_TrimZeroLenAsNull(e.getMessage()));
			}
		}
		
		if(m==0)
			showHintMessage("������Բɣ����赼����");
		else
		  showHintMessage("�����ɹ���");
} 
	private int onBoListDerived(int m,String path,GeneralBillVO billvo) throws Exception {
		//int m=0;
		ArrayList<Object> al = new ArrayList<Object>();
		ArrayList<Object> alt = new ArrayList<Object>();
		GeneralBillHeaderVO head = billvo.getHeaderVO();
		al.add(PuPubVO.getString_TrimZeroLenAsNull(head.getAttributeValue("vuserdef17")));// ��������
		al.add(head.getAttributeValue("cbizname"));// �ɹ�Ա
		al.add(head.getAttributeValue("cdptname"));// �ɹ�����
		al.add(null);// ���䷽ʽ
		al.add(head.getAttributeValue("dealname"));// �ջ���
		al.add(head.getAttributeValue("vcalbodyname"));// �����֯
		al.add(head.getAttributeValue("coperatorname"));// �Ƶ���
		al.add(PuPubVO.getString_TrimZeroLenAsNull(head.getAttributeValue("tmaketime")));// �Ƶ�ʱ��
		al.add(head.getAttributeValue("vuserdef9"));// �ɹ���ͬ��
	
		CircularlyAccessibleValueObject[] a = billvo.getChildrenVO();
//		BillModel bm = getBillCardPanel().getBillModel();
		// /**��ȡ��ǰ�༭������VO -- item ,ע�⣺�����ڳ�ʼ��ʱ�ɷ������˴��ݹ�������Ϊ�����Ƕ���ת��ĵ���*/
		int rowcount = a.length;
	//	StringBuffer sb = null;
		String stg = null;
		String pk = null;
		TransletFactory tf = new TransletFactory();
		for (int i = 0; i < rowcount; i++) {
			stg = PuPubVO.getString_TrimZeroLenAsNull(a[i].getAttributeValue("cinventoryid"));
			pk =tf.TFactory1(stg,"�ɹ���ʽ");
			if(!"0001Q110000000000R18".equalsIgnoreCase(pk)){
				continue;
			}
			alt.add(tf.TFactory(PuPubVO.getString_TrimZeroLenAsNull(a[i].getAttributeValue("cinventoryid"))));// ����
			alt.add(PuPubVO.getString_TrimZeroLenAsNull(head.getAttributeValue("vuserdef17")));//�ƻ���������
			alt.add(PuPubVO.getString_TrimZeroLenAsNull(a[i].getAttributeValue("vuserdef18")));//����
			alt.add(PuPubVO.getString_TrimZeroLenAsNull(a[i].getAttributeValue("vuserdef20")));//�ϸ�����
			String num = PuPubVO.getString_TrimZeroLenAsNull(PuPubVO.getUFDouble_NullAsZero(a[i].getAttributeValue("vuserdef18"))
					.sub(PuPubVO.getUFDouble_NullAsZero(a[i].getAttributeValue("vuserdef20"))));
			alt.add(PuPubVO.getString_TrimZeroLenAsNull(num));//���ϸ�����
			//alt.add(PuPubVO.getString_TrimZeroLenAsNull(a[i].getAttributeValue("nnotelignum")));//���ϸ�����
			alt.add("0.0000");//;������
			alt.add(PuPubVO.getString_TrimZeroLenAsNull(a[i].getAttributeValue("vuserdef18")));//�ۼƱ�������
			alt.add(tf.TFactory(PuPubVO.getString_TrimZeroLenAsNull(head.getAttributeValue("cwarehouseid"))));//�ջ��ֿ�ID
			alt.add(null);//��������
			alt.add(null);//ʧЧ����
			alt.add(null);//���κ�
			alt.add(a[i].getAttributeValue("vmemo"));
		}
		if(alt.size()==0){
//			showHintMessage("������Բɣ����赼����");
			return m;
		}
			m++;
		OutputStream os;
		os = new FileOutputStream(path);
		WritableWorkbook wwb = Workbook.createWorkbook(os);
		WritableSheet sheet = wwb.createSheet("sheet1", 0);
		ExcelDeriver(al, alt, rowcount, sheet);
		wwb.write();
		wwb.close();
		os.close();
		//if(sb.length() == 0 || sb.toString() == null || "".equals(sb.toString())){
		//}else{
		//	showErrorMessage(sb.toString());
		//}
		return m;
	}

	private void onBoCardDerived() throws Exception {
		File file = null;
		File[] files = null;
		ArrayList<Object> al = new ArrayList<Object>();
		ArrayList<Object> alt = new ArrayList<Object>();
		if (getUIFileChooser().showSaveDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
			files = getUIFileChooser().getSelectedFiles();
			
		}
		if (files == null || "".equals(files)) {
			return;
		}
		file = files[0];

		if(!"3".equalsIgnoreCase(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("fbillflag").getValueObject()))){
			showHintMessage("����δǩ��,���ܵ���");
			return;
		}
		al.add(getBillCardPanel().getHeadItem("vuserdef17").getValueObject());// ��������
		al.add(((UIRefPane) (JComponent) getBillCardPanel().getHeadItem("cbizid").getComponent()).getRefName());// �ɹ�Ա
		al.add(((UIRefPane) (JComponent) getBillCardPanel().getHeadItem("cdptid").getComponent()).getRefName());// �ɹ�����
		al.add(null);// ���䷽ʽ
		al.add(((UIRefPane) (JComponent) getBillCardPanel().getHeadItem("cbizid").getComponent()).getRefName());// �ջ���
		al.add(((UIRefPane) (JComponent) getBillCardPanel().getHeadItem("pk_calbody").getComponent()).getRefName());// �����֯
		al.add(getBillCardPanel().getTailItem("coperatorname").getValueObject());// �Ƶ���
		al.add(getBillCardPanel().getTailItem("tmaketime").getValueObject());// �Ƶ�ʱ��
		al.add(getBillCardPanel().getHeadItem("vuserdef9").getValueObject());// �ɹ���ͬ��
       // StringBuffer sb = null;
		TransletFactory tf = new TransletFactory();
		int rowcount = getBillCardPanel().getBillModel().getRowCount();
		String stg = null;
		String pk = null;
		for (int i = 0; i < rowcount; i++) {
			
			stg = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(i, "cinventoryid"));
			pk =tf.TFactory1(stg,"�ɹ���ʽ");
			if(!"0001Q110000000000R18".equalsIgnoreCase(pk)){
				rowcount--;
				//int j =i +1;
				//sb.append("��'"+j+"'�д�����Բɣ����赼����");
				continue;
			}
			alt.add(getBillCardPanel().getBillModel().getValueAt(i,"cinventorycode"));// ����
			alt.add(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("vuserdef17").getValueObject()));//�ƻ���������
			alt.add(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(i, "vuserdef18")));//����
			alt.add(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(i, "vuserdef20")));//�ϸ�����
			String num = PuPubVO.getString_TrimZeroLenAsNull(PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel().getValueAt(i, "vuserdef18"))
					.sub(PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel().getValueAt(i, "vuserdef20"))));
			alt.add(PuPubVO.getString_TrimZeroLenAsNull(num));//���ϸ�����
			alt.add("0.0000");//;������
			alt.add(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(i, "vuserdef18")));// naccumchecknum �ۼƱ�������
			alt.add(((UIRefPane) (JComponent) getBillCardPanel().getHeadItem("cwarehouseid").getComponent()).getRefName());//�ջ��ֿ�ID
			alt.add(null);// dproducedate ��������
			alt.add(null);// dvaliddate ʧЧ����
			alt.add(null);//���κ�
			alt.add(getBillCardPanel().getBillModel().getValueAt(i, "vmemo"));
		}
		if(alt.size()==0){
			showHintMessage("������Բɣ����赼����");
			return;
		}
			
		if((al==null||al.size()==0) &&(alt==null||alt.size()==0)){
			showHintMessage("���Ȳ�ѯ��¼�뵥��");
			return;
		}
			
		    WritableWorkbook book;
			try {
				String[] strs=null;
				String s = file.getPath();
				strs = tf.splitCode(s);
			
            	file = new File(strs[0]+"("+PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("vbillcode").getValueObject())+")"+".xls");
            	
				book = Workbook.createWorkbook(file);
				WritableSheet sheet = book.createSheet("abc", 0);
				ExcelDeriver(al, alt, rowcount, sheet);
				//if(sb.length() == 0 || sb.toString() == null || "".equals(sb.toString())){
				//}else{
				//	showErrorMessage(sb.toString());
				//}
				book.write();
				book.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				showErrorMessage(PuPubVO.getString_TrimZeroLenAsNull(e.getMessage()));
			}
		
		showHintMessage("�����ɹ���");
	}
	
	private void ExcelDeriver(ArrayList<Object> al, ArrayList<Object> alt,int num, WritableSheet sheet) throws Exception {
		
			Label label = null;
			label = new Label(0, 0, "��������");
			sheet.addCell(label);
			label = new Label(1, 0, (String) al.get(0));
			sheet.addCell(label);
			label = new Label(2, 0, "�ɹ�Ա");
			sheet.addCell(label);
			label = new Label(3, 0, (String) al.get(1));
			sheet.addCell(label);
			label = new Label(4, 0, "�ɹ�����");
			sheet.addCell(label);
			label = new Label(5, 0, (String) al.get(2));
			sheet.addCell(label);
			label = new Label(6, 0, "���䷽ʽ");
			sheet.addCell(label);
			label = new Label(7, 0, (String) al.get(3));
			sheet.addCell(label);

			label = new Label(0, 1, "�ջ���");
			sheet.addCell(label);
			label = new Label(1, 1, (String) al.get(4));
			sheet.addCell(label);
			label = new Label(2, 1, "�����֯");
			sheet.addCell(label);
			label = new Label(3, 1, (String) al.get(5));
			sheet.addCell(label);
			label = new Label(4, 1, "�Ƶ���");
			sheet.addCell(label);
			label = new Label(5, 1, (String) al.get(6));
			sheet.addCell(label);
			label = new Label(6, 1, "�Ƶ�����");
			sheet.addCell(label);
			label = new Label(7, 1, (String) al.get(7));
			sheet.addCell(label);
			label = new Label(0, 2, "��Ӧ�ɹ�����");
			sheet.addCell(label);
			label = new Label(1, 2, (String) al.get(8));
			sheet.addCell(label);

			sheet.addCell(new Label(0, 3, "�������"));
			sheet.addCell(new Label(1, 3, "�ƻ���������"));
			sheet.addCell(new Label(2, 3, "��������"));
			sheet.addCell(new Label(3, 3, "�ϸ�����"));
			sheet.addCell(new Label(4, 3, "���ϸ�����"));
			sheet.addCell(new Label(5, 3, ";������"));
			sheet.addCell(new Label(6, 3, "��������"));
			sheet.addCell(new Label(7, 3, "�ջ��ֿ�"));
			sheet.addCell(new Label(8, 3, "��������"));
			sheet.addCell(new Label(9, 3, "ʧЧ����"));
			sheet.addCell(new Label(10, 3, "����"));
			sheet.addCell(new Label(11, 3, "��ע"));

			int sum = 0;
			for (int y = 1; y <= num; y++) {
				for (int i = 0; i < 12; i++) {
					if ((String) alt.get(sum) == "null") {
						alt.set(sum, "");
					}
					sheet.addCell(new Label(i, y + 3, (String) alt.get(sum)));
					sum++;
				}
			}
	}
	
	UIFileChooser fcFileChooser = null;

	private UIFileChooser getUIFileChooser() {
		if (fcFileChooser == null) {
			fcFileChooser = new UIFileChooser();
			fcFileChooser.setMultiSelectionEnabled(true);
//			fcFileChooser.set
		}
		return fcFileChooser;
	}


	private void onMatchPact() {
		try {

			GeneralBillVO gbillvo = getCurVO();
			if(gbillvo==null){
				showHintMessage("��ǰ��������Ϊ��");
				return;
			}
			if (HgPubConst.purchaseIn_in == HgPubTool
					.getPurchaseInBusiStatus(gbillvo)) {
				showWarningMessage("�õ����Ѿ����,���ܺ�ͬƥ��");
				return;
			}

			if (HgPubConst.purchaseIn_arr == HgPubTool
					.getPurchaseInBusiStatus(gbillvo)) {
				showWarningMessage("�õ���û������,���ܺ�ͬƥ��");
				return;
			}

			if (HgPubConst.purchaseIn_no == HgPubTool
					.getPurchaseInBusiStatus(gbillvo)) {// ���ϸ�
				showWarningMessage("�õ��ݲ��ϸ�,���ܺ�ͬƥ��");
				return;
			}

			boolean flag = getPactDlg().initData(gbillvo);
			if (flag) {
				showWarningMessage("���ں�ͬ��Ϣ������Ҫƥ��");
				return;
			}
			if (getPactDlg().showModal() != UIDialog.ID_OK)
				return;

			if (getCurPanel() == BillMode.Card) {
				getBillCardPanel().setBillValueVO(getPactDlg().getNewGbillvo());
				getBillCardPanel().getBillModel().execLoadFormula();
			} else {
				getBillListPanel().getBodyBillModel().setBodyDataVO(getPactDlg().getNewGbillvo().getItemVOs());
				getBillListPanel().getBodyBillModel().execLoadFormula();
			}

//			getPactDlg().getNewGbillvo().setChildrenVO(getBodyVOs());
			getM_alListData().set(getM_iLastSelListHeadRow(),getPactDlg().getNewGbillvo());
			setM_voBill(getPactDlg().getNewGbillvo());

		} catch (Exception e) {
			e.printStackTrace();
			showErrorMessage(e.getMessage());
		}
	}

	private MatchPactDLG pactDlg = null;

	private MatchPactDLG getPactDlg() {
		if (pactDlg == null)
			pactDlg = new MatchPactDLG(this);
		return pactDlg;
	}

	/**
	 * ���մ���
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-12-8����06:23:10
	 */

	public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
		String key = e.getKey();
		if (!PuPubVO.getUFBoolean_NullAs(getBillCardPanel().getHeadItem("freplenishflag")
						.getValueObject(), UFBoolean.FALSE).booleanValue()) {
			 if (m_isAdjust) {// true ����ʱ�� ����ʵ������ ��λ�ɱ༭
					if (key.equalsIgnoreCase("ninnum")
							|| key.equalsIgnoreCase("ninassistnum")
							|| key.equalsIgnoreCase("castunitname")
							|| key.equalsIgnoreCase("vspacename"))
						return true;
					else
						return false;
				}
				if (key.equalsIgnoreCase("ninnum")
						|| key.equalsIgnoreCase("ninassistnum")
						|| key.equalsIgnoreCase("vspacename")) {
					return false;
				}
		}
		return super.beforeEdit(e);
	}

	public IButtonManager getButtonManager() {
		if (m_buttonManager == null) {
			try {
				m_buttonManager = new ButtonManager201(this);
			} catch (BusinessException e) {
				// ��־�쳣
				nc.vo.scm.pub.SCMEnv.error(e);
				showErrorMessage(e.getMessage());
			}
		}
		return m_buttonManager;
	}

	private void setFixBarcodenegative(boolean value) {
		super.m_bFixBarcodeNegative = value;
	}

	public static void clearBillBodyItem(BillCardPanel bcp, String itemkey) {
		int rows = bcp.getBillTable().getRowCount();
		for (int i = 0; i < rows; i++) {
			bcp.setBodyValueAt(null, i, itemkey);
		}
	}

	/**
	 * ����ʱ,����ͷ�Ƿ��й�˾����Ϣ, �ѹ�˾�����֯�ֿ������������
	 * 
	 * @see afterWHEdit, afterCalbodyEdit: �����֯�Ͳֿ�༭��,Ҫ����ͷ�Ĳֿ�Ϳ����֯�������ڵı�������
	 * 
	 */
	public void addRowNums(int rownums) {
		super.addRowNums(rownums);
		// v5 lj ���вɹ�Ĭ����������
		setBodyDefaultData(0, rownums);// ����
		// ��ͷ�ɹ���˾����Ĭ��Ϊ��½��˾
		// ���ղɹ���������ʱ,�˷����������ö�������֮ǰ����,�������ﲻ���ִ�����պ�����
		String pk_corp = getClientEnvironment().getCorporation().getPk_corp();
		getBillCardPanel().setHeadItem(IItemKey.PUR_CORP, pk_corp);
		BillItem it = getBillCardPanel().getHeadItem(IItemKey.PUR_CORP);
		// ((UIRefPane)getBillCardPanel().getHeadItem(IItemKey.PUR_CORP).getComponent()).

		getBillCardPanel().execHeadTailEditFormulas(it);
	}

	/**
	 * �ӵ��ݱ����У����ҳ����ǳ��׼��Ĵ�������ع�һ���µı�����VO[] ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-04-18
	 * 11:29:23) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	GeneralBillItemVO[] searchInvKit(GeneralBillItemVO[] cvos) {
		ArrayList alInvKit = null;
		GeneralBillItemVO[] resultvos = null;
		if (cvos != null) {
			alInvKit = new ArrayList();
			for (int i = 0; i < cvos.length; i++) {
				if (cvos[i].getIsSet() != null
						&& cvos[i].getIsSet().intValue() == 1)
					alInvKit.add(cvos[i]);
			}
			if (alInvKit.size() > 0) {
				resultvos = new GeneralBillItemVO[alInvKit.size()];
				alInvKit.toArray(resultvos);
			}
			return resultvos;
		}
		return null;
	}

	/**
	 * �ӵ��ݱ����У����ҳ����ǳ��׼��Ĵ�������ع�һ���µı�����VO[] ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-04-18
	 * 11:29:23) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	GeneralBillItemVO searchInvKit(GeneralBillItemVO cvos) {
		if (cvos != null) {
			if (cvos.getIsSet() != null && cvos.getIsSet().intValue() == 1)
				return cvos;
		}
		return null;
	}

	/**
	 * �����ߣ����˾� ���ܣ����б�ʽ��ѡ��һ�ŵ��� ������ ������alListData�е����� ���أ��� ���⣺ ���ڣ�(2001-11-23
	 * 18:11:18) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void selectBillOnListPanel(int iBillIndex) {
	}

	/**
	 * �˴����뷽��˵���� �Ѷ��ŵ���VO�ϲ���һ�� �Բɹ���ⵥ�ݣ���Ҫ���ر����� �������ڣ�(2004-3-17 15:35:51)
	 * 
	 * @return nc.vo.ic.pub.bill.GeneralBillVO
	 * @param vos
	 *            nc.vo.pub.AggregatedValueObject[]
	 */
	// protected GeneralBillVO setBillRefResultCombinVo(
	// nc.vo.pub.AggregatedValueObject[] vos) throws Exception {
	//
	// nc.ui.ic.pub.pfconv.VoHandle handle = new nc.ui.ic.pub.pfconv.VoHandle();
	// String sCsourcetype = null;
	//
	// // ͨ����ͷ��ÿ����֯������Դ��������Ϊ�ɹ�������ҵ�񣬴ӱ���ȡ�����֯
	// // ����Դ��������Ϊ�ɹ�������ҵ�񣬴ӱ�ͷȡ�����֯
	// boolean bhead = true;
	// if (vos != null && vos.length > 0) {
	// GeneralBillItemVO[] itemVOs = (GeneralBillItemVO[]) vos[0]
	// .getChildrenVO();
	// if (itemVOs != null && itemVOs.length > 0) {
	// sCsourcetype = itemVOs[0].getCsourcetype();
	// // /����Դ��������Ϊ�ɹ�������ҵ�񣬴ӱ���ȡ�����֯
	// if (sCsourcetype != null && "21".equalsIgnoreCase(sCsourcetype)) {
	// bhead = false;
	// }
	// }
	//
	// }
	// // Ĭ�ϼ������֯����ͷ�ķ�ʽ������
	// GeneralBillVO voRet = handle.combinVo(vos, bhead);
	//
	// return voRet;
	// }
	/**
	 * �˴����뷽��˵���� ���ò��չ�����������Ʒ�����Ա༭ �÷������˿�����Ĳ��ն����������������� �������ڣ�(2003-10-14
	 * 14:29:30)
	 * 
	 * @param BusiTypeID
	 *            java.lang.String
	 * @param vos
	 *            nc.vo.pub.AggregatedValueObject[]
	 */
	protected void setBillRefResultVO(String sBusiTypeID,
			nc.vo.pub.AggregatedValueObject[] vos) throws Exception {
		// ���չ�����������Ʒ�����Ա༭
		nc.vo.ic.pub.GenMethod.setFlargessEdit(vos, false);
		nc.vo.ic.pub.GenMethod.processFlargessLine((GeneralBillVO[]) vos);
		// �����෽��
		super.setBillRefResultVO(sBusiTypeID, vos);

	}

	/**
	 * �����ߣ����˾� ���ܣ����ݵ�ǰ���ݵĴ���״̬����ǩ��/ȡ��ǩ���Ǹ����� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setBtnStatusSign(boolean bUpdateButtons) {
		// ֻ�����״̬�²��ҽ������е���ʱ����
		if (BillMode.Browse != getM_iMode() || getM_iLastSelListHeadRow() < 0
				|| m_iBillQty <= 0) {
			getButtonManager().getButton(ICButtonConst.BTN_SIGN).setEnabled(
					false);
			getButtonManager().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL)
					.setEnabled(false);
			return;
		}
		int iSignFlag = isSigned();
		if (SIGNED == iSignFlag) {
			// ��ǩ�֣��������ð�ť״̬,ǩ�ֲ����ã�ȡ��ǩ�ֿ���
			getButtonManager().getButton(ICButtonConst.BTN_SIGN).setEnabled(
					false);
			getButtonManager().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL)
					.setEnabled(true);
			// ����ɾ����
			getButtonManager().getButton(ICButtonConst.BTN_BILL_EDIT)
					.setEnabled(false);
			getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE)
					.setEnabled(false);
			getButtonManager()
					.getButton(ICButtonConst.BTN_ASSIST_FUNC_DISPENSE)
					.setEnabled(false);

			// zhw begin
			getButtonManager().getButton(HgPuBtnConst.ACPT).setEnabled(false);
			getButtonManager().getButton(HgPuBtnConst.UNACPT).setEnabled(false);
			getButtonManager().getButton(HgPuBtnConst.UNDEAL).setEnabled(false);
			getButtonManager().getButton(HgPuBtnConst.WARE).setEnabled(false);
			// zhw end

		} else if (NOTSIGNED == iSignFlag) {
			// δǩ�֣��������ð�ť״̬,ǩ�ֿ��ã�ȡ��ǩ�ֲ�����
			// �ж��Ƿ���������������Ϊ�����������ģ�����ֻҪ����һ�о����ˡ�

			getButtonManager().getButton(ICButtonConst.BTN_SIGN).setEnabled(
					true);
			getButtonManager().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL)
					.setEnabled(false);

			// zhw begin
			getButtonManager().getButton(HgPuBtnConst.ACPT).setEnabled(true);
			getButtonManager().getButton(HgPuBtnConst.UNACPT).setEnabled(true);
			getButtonManager().getButton(HgPuBtnConst.UNDEAL).setEnabled(true);
			getButtonManager().getButton(HgPuBtnConst.WARE).setEnabled(true);
			// zhw end

			// ��ɾ����
			if (isCurrentTypeBill()) {

				getButtonManager().getButton(ICButtonConst.BTN_BILL_EDIT)
						.setEnabled(true);
				getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE)
						.setEnabled(true);
			} else {

				getButtonManager().getButton(ICButtonConst.BTN_BILL_EDIT)
						.setEnabled(false);
				getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE)
						.setEnabled(false);
			}
			// if (isSetInv(m_voBill, m_iFirstSelectRow) &&
			// !isDispensedBill(null))
			if (BillMode.Card == getM_iCurPanel()) {
				getButtonManager().getButton(
						ICButtonConst.BTN_ASSIST_FUNC_DISPENSE)
						.setEnabled(true);
			}
			// else
			// getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_DISPENSE).setEnabled(false);

		} else { // ����ǩ�ֲ���
			getButtonManager().getButton(ICButtonConst.BTN_SIGN).setEnabled(
					false);
			getButtonManager().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL)
					.setEnabled(false);
			// ��ɾ����
			if (isCurrentTypeBill()) {
				getButtonManager().getButton(ICButtonConst.BTN_BILL_EDIT)
						.setEnabled(true);
				getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE)
						.setEnabled(true);
			} else {
				getButtonManager().getButton(ICButtonConst.BTN_BILL_EDIT)
						.setEnabled(false);
				getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE)
						.setEnabled(false);
			}
		}
		// ʹ������Ч
		if (bUpdateButtons)
			updateButtons();

	}

	/**
	 * ���ǻ��෽��
	 */
	public void setBillVO(GeneralBillVO bvo, boolean bUpdateBotton,
			boolean bExeFormule) {

		// ���вɹ��繫˾���ݴ���
		// v5:���ı�ͷ�ɹ�Ա�Ͳɹ�����,��Ӧ�̵Ĳ���Ϊ�����Բɹ���˾
		String purcorp = (String) ((GeneralBillVO) bvo)
				.getHeaderValue(IItemKey.PUR_CORP);
		if (purcorp != null && !purcorp.equals(getEnvironment().getCorpID())) {
			BillItem it = getBillCardPanel().getHeadItem("cbizid");
			if (it != null) {
				// RefFilter.filterPsnByDept(it, purcorp, null);// ���ϲ��Ź�˾����
				((UIRefPane) it.getComponent()).setPk_corp(purcorp);
			}

			BillItem it1 = getBillCardPanel().getHeadItem("cdptid");
			if (it1 != null) {
				((UIRefPane) it1.getComponent()).getRefModel().setWherePart(
						null);
				((UIRefPane) it1.getComponent()).setPk_corp(purcorp);
			}

			BillItem it2 = getBillCardPanel().getHeadItem("cproviderid");
			if (it2 != null) {
				((UIRefPane) it2.getComponent()).getRefModel().setWherePart(
						null);
				((UIRefPane) it2.getComponent()).setPk_corp(purcorp);
			}
		}

		// �繫˾���δ���
		if (purcorp != null && !purcorp.equals(getEnvironment().getCorpID())) {
			if (bvo != null) {
				GeneralBillItemVO[] voa = bvo.getItemVOs();
				if (voa != null) {
					for (int i = 0; i < voa.length; i++) {
						if (voa[i] == null)
							continue;
						Integer isBatch = voa[i].getInv().getIsLotMgt();
						if (isBatch == null || isBatch.intValue() == 1)
							continue;
						if (isBatch.intValue() == 0) { // �����ι���clear batchcode
							voa[i].setVbatchcode(null);
						}
					}
				}
			}
		}

		super.setBillVO(bvo, bUpdateBotton, bExeFormule);
	}

	/**
	 * �����ߣ����˾� ���ܣ����󷽷������ð�ť״̬����setButtonStatus�е��á� ������ ���أ� ���⣺ ���ڣ�(2001-5-9
	 * 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void setButtonsStatus(int iBillMode) {
		// ���ģʽ�£��е��ݲ����Ѿ�ǩ�ֲſ���
		long lTime = System.currentTimeMillis();
		// in card browser status, can use dispense button.
		if (getButtonManager()
				.getButton(ICButtonConst.BTN_ASSIST_FUNC_DISPENSE) != null) {
			if (getM_iCurPanel() == BillMode.Card
					&& iBillMode == BillMode.Browse && m_iBillQty > 0
					&& isSigned() != SIGNED)
				getButtonManager().getButton(
						ICButtonConst.BTN_ASSIST_FUNC_DISPENSE)
						.setEnabled(true);
			else
				getButtonManager().getButton(
						ICButtonConst.BTN_ASSIST_FUNC_DISPENSE).setEnabled(
						false);
		}

		// �����ʲ���Ƭ/ȡ�������ʲ���Ƭ�İ�ť״������
		if (getM_iCurPanel() == BillMode.Card
				&& iBillMode == BillMode.Browse
				&& m_iBillQty > 0
				&& isSigned() == SIGNED
				&& getM_voBill().getWh().getIsCapitalStor().booleanValue()
				&& !getM_voBill().getHeaderVO().getFreplenishflag()
						.booleanValue()
				&& nc.ui.ic.pub.tools.GenMethod.isProductEnabled(
						getCorpPrimaryKey(), "AIM")) {
			// ��ǩ�֡��ʲ��֡����˻���־���ʲ�ģ���Ѿ�������Щ���������ϣ��Ž�����������ť������
			if (getM_voBill().getHeaderVO().getBassetcard().booleanValue()) {
				getButtonManager().getButton(
						ICButtonConst.BTN_ASSIST_GEN_ASSET_CARD).setEnabled(
						false);
				getButtonManager().getButton(
						ICButtonConst.BTN_ASSIST_CGEN_ASSET_CARD).setEnabled(
						true);
			} else {
				getButtonManager().getButton(
						ICButtonConst.BTN_ASSIST_GEN_ASSET_CARD).setEnabled(
						true);
				getButtonManager().getButton(
						ICButtonConst.BTN_ASSIST_CGEN_ASSET_CARD).setEnabled(
						false);
			}
		} else {
			getButtonManager().getButton(
					ICButtonConst.BTN_ASSIST_GEN_ASSET_CARD).setEnabled(false);
			getButtonManager().getButton(
					ICButtonConst.BTN_ASSIST_CGEN_ASSET_CARD).setEnabled(false);
		}
		updateButton(getButtonManager().getButton(
				ICButtonConst.BTN_ASSIST_GEN_ASSET_CARD));
		updateButton(getButtonManager().getButton(
				ICButtonConst.BTN_ASSIST_CGEN_ASSET_CARD));

		// //��Ҫ�������ð�ť��ˢ�����ఴť��״̬��
		// ���ﲻ�ܵ��ø��Ǹ��෽��
		initButtonsData();

		// �жϲɹ���Ʒģ���Ƿ�����
		lTime = System.currentTimeMillis();
		if (nc.ui.ic.pub.tools.GenMethod.isProductEnabled(getCorpPrimaryKey(),
				nc.vo.pub.ProductCode.PROD_PO)) {
			// ֻҪ�����˲ɹ�ģ�飬���˿�˵����κ�ʱ���ǿ��õģ����Ҫ��

			if (getM_iMode() == BillMode.Browse) {
				// v5
				getButtonManager().getButton(
						ICButtonConst.BTN_LINE_KD_CALCULATE).setEnabled(false);
				// �ڱ༭״̬�£��˿ⰴť������ ������ 2009-08-10
				// getButtonManager().getButton(
				// ICButtonConst.BTN_ASSIST_FUNC_MANUAL_RETURN)
				// .setEnabled(true);
				// getButtonManager().getButton(
				// ICButtonConst.BTN_ASSIST_FUNC_PO_RETURN).setEnabled(
				// true);
			} else {
				// v5
				getButtonManager().getButton(
						ICButtonConst.BTN_LINE_KD_CALCULATE).setEnabled(true);
			}
		} else {
			getButtonManager().getButton(
					ICButtonConst.BTN_ASSIST_FUNC_MANUAL_RETURN).setEnabled(
					false);
			getButtonManager().getButton(
					ICButtonConst.BTN_ASSIST_FUNC_PO_RETURN).setEnabled(false);

			// v5
			getButtonManager().getButton(ICButtonConst.BTN_LINE_KD_CALCULATE)
					.setEnabled(false);
		}

		lTime = System.currentTimeMillis();

		updateButtons();

		SCMEnv.showTime(lTime, "setButtonsStatus(int)_6:");

	}

	/**
	 * ��������֮����Ҫ�������׵ı�־�û�VO�� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-06-03 14:39:46)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	void setDispenseFlag(GeneralBillVO gvo) {
		if (gvo == null || gvo.getItemCount() == 0)
			return;
		ArrayList alBid = null;
		GeneralBillItemVO[] resultvos = gvo.getItemVOs();
		if (resultvos != null) {
			alBid = new ArrayList();
			for (int i = 0; i < resultvos.length; i++) {
				if (resultvos[i].getIsSet() != null
						&& resultvos[i].getIsSet().intValue() == 1) {
					resultvos[i].setFbillrowflag(new Integer(
							nc.vo.ic.pub.BillRowType.afterConvert));
					alBid.add(resultvos[i].getPrimaryKey());
				}
			}
			if (alBid.size() > 0) {
				try {
					GeneralHHelper.setDispense(alBid);
				} catch (Exception e) {
					nc.vo.scm.pub.SCMEnv.error(e);

				}

			}

		}

	}

	/**
	 * ��������֮����Ҫ�������׵ı�־�û�VO�� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-06-03 14:39:46)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	void setDispenseFlag(GeneralBillVO gvo, int[] rownums) {
		if (gvo == null || gvo.getItemCount() == 0)
			return;
		ArrayList alBid = null;
		GeneralBillItemVO[] resultvos = gvo.getItemVOs();
		if (resultvos != null) {
			alBid = new ArrayList();

			for (int i = 0; i < rownums.length; i++) {
				if (!isSetInv(gvo, rownums[i]))
					continue;
				resultvos[rownums[i]].setFbillrowflag(new Integer(
						nc.vo.ic.pub.BillRowType.afterConvert));
				alBid.add(resultvos[rownums[i]].getPrimaryKey());

			}

		}

	}

	/**
	 * �����ߣ�zhx ���ܣ����׼��������Ĵ������� ���������׼��Ĵ������ID�����׼����������ڼ������������ ���أ� ���⣺
	 * ���ڣ�(2001-5-8 19:08:05) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	public GeneralBillItemVO[] splitInvKit(GeneralBillItemVO itemvo,
			GeneralBillHeaderVO headervo, UFDouble nsetnum) {

		if (itemvo == null)
			return null;
		String sInvSetID = itemvo.getCinventoryid();

		if (sInvSetID != null) {
			ArrayList alInvvo = new ArrayList();
			try {
				alInvvo = GeneralBillHelper.queryPartbySetInfo(sInvSetID);
			} catch (Exception e2) {
				nc.vo.scm.pub.SCMEnv.error(e2);
			}
			if (alInvvo == null) {
				nc.vo.scm.pub.SCMEnv.out("�ó��׼�û��������������ݿ�...");
				return null;
			}
			int rowcount = alInvvo.size();

			GeneralBillItemVO[] voParts = new GeneralBillItemVO[rowcount];
			nc.vo.pub.lang.UFDate db = new nc.vo.pub.lang.UFDate(
					getEnvironment().getLogDate());
			for (int i = 0; i < rowcount; i++) {
				voParts[i] = new GeneralBillItemVO();
				voParts[i].setInv((InvVO) alInvvo.get(i));
				voParts[i].setDbizdate(db);
				UFDouble nchildnum = ((InvVO) alInvvo.get(i))
						.getAttributeValue("childsnum") == null ? new UFDouble(
						0) : new UFDouble(((InvVO) alInvvo.get(i))
						.getAttributeValue("childsnum").toString());
				UFDouble ntotalnum = null;
				if (nsetnum != null)
					ntotalnum = nchildnum.multiply(nsetnum);
				else
					ntotalnum = nchildnum;
				UFDouble hsl = ((InvVO) alInvvo.get(i)).getHsl() == null ? null
						: new UFDouble(((InvVO) alInvvo.get(i)).getHsl()
								.toString());
				UFDouble ntotalastnum = null;
				if (hsl != null && hsl.doubleValue() != 0) {
					ntotalastnum = ntotalnum.div(hsl);
				}

				voParts[i].setNinnum(ntotalnum);
				voParts[i].setNinassistnum(ntotalastnum);
				voParts[i].setNshouldinnum(ntotalnum);
				voParts[i].setNneedinassistnum(ntotalastnum);
				voParts[i].setDbizdate(new nc.vo.pub.lang.UFDate(
						getEnvironment().getLogDate()));
				voParts[i].setCsourceheadts(null);
				voParts[i].setCsourcebodyts(null);
				voParts[i].setCsourcetype(headervo.getCbilltypecode());
				voParts[i].setCsourcebillhid(headervo.getPrimaryKey());
				voParts[i].setCsourcebillbid(itemvo.getPrimaryKey());
				voParts[i].setVsourcebillcode(headervo.getVbillcode());
				voParts[i].setAttributeValue("creceieveid", itemvo
						.getCreceieveid());
				voParts[i].setAttributeValue("cprojectid", itemvo
						.getCprojectid());
				String s = "vuserdef";
				String ss = "pk_defdoc";
				for (int j = 0; j < 20; j++) {

					voParts[i]
							.setAttributeValue(s + String.valueOf(j + 1),
									itemvo.getAttributeValue(s
											+ String.valueOf(j + 1)));
					voParts[i].setAttributeValue(ss + String.valueOf(j + 1),
							itemvo
									.getAttributeValue(ss
											+ String.valueOf(j + 1)));
				}
				voParts[i].setCgeneralhid(null);
				voParts[i].setCgeneralbid(null);
				voParts[i].setStatus(nc.vo.pub.VOStatus.NEW);
			}
			return voParts;
		}
		return null;

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-10-28 8:47:55)
	 * 
	 * @param iRow
	 *            int
	 * @param m_voBill
	 *            nc.vo.ic.pub.bill.GeneralBillVO ճ���к�����ճ���е����ԣ���������ʹ��
	 *            ����Ʒ�еĸ����У����Ա༭����Ʒ�� ��Ʒ�еĸ����У������Ա༭����Ʒ��
	 */
	public void voBillPastLineSetAttribe(int iRow, GeneralBillVO voBill) {
		Object oTemp = voBill.getItemVOs()[iRow].getAttributeValue("flargess");
		boolean bFlarg = false; // �Ƿ���Ʒ
		if (oTemp != null) {
			UFBoolean ufbflargess = (UFBoolean) oTemp;
			bFlarg = ufbflargess.booleanValue();
		}
		if (!bFlarg) // ����Ʒ�еĸ����У����Ա༭����Ʒ��
			voBill.getItemVOs()[iRow].setAttributeValue("isflargessedit", "Y");
		else
			voBill.getItemVOs()[iRow].setAttributeValue("isflargessedit", "N");
		return;
	}

	// test
	// public void onBillExcel(int iflag) {
	//	      
	// getBillCardPanel().setBodyValueAt(Boolean.FALSE,0,"bsourcelargess");
	//		
	// m_voBill.setItemValue(0,"bsourcelargess",new UFBoolean(false));
	//
	// getBillCardPanel().setBodyValueAt(Boolean.TRUE,0,"bsourcelargess");
	//
	// m_voBill.setItemValue(1,"bsourcelargess",new UFBoolean(true));
	//		
	// getBillCardPanel().setBodyValueAt("21",0,IItemKey.CSOURCETYPE);
	// getBillCardPanel().setBodyValueAt("21",0,IItemKey.CSOURCEBILLBID);
	// getBillCardPanel().setBodyValueAt("21",0,IItemKey.CSOURCEBILLHID);
	//		
	//
	// getBillCardPanel().setBodyValueAt("21",1,IItemKey.CSOURCETYPE);
	// getBillCardPanel().setBodyValueAt("21",1,IItemKey.CSOURCEBILLBID);
	// getBillCardPanel().setBodyValueAt("21",1,IItemKey.CSOURCEBILLHID);
	//		
	//		
	// m_voBill.setItemValue(0,IItemKey.CSOURCETYPE,"21");
	// m_voBill.setItemValue(0,IItemKey.CSOURCEBILLBID,"21");
	// m_voBill.setItemValue(0,IItemKey.CSOURCEBILLHID,"21");
	//
	// m_voBill.setItemValue(1,IItemKey.CSOURCETYPE,"21");
	// m_voBill.setItemValue(1,IItemKey.CSOURCEBILLBID,"21");
	// m_voBill.setItemValue(1,IItemKey.CSOURCEBILLHID,"21");
	//		
	// }

	/**
	 * v5:����ʱ����ͷ�Ĺ�˾����Ϊ���������˾�Ͳɹ���˾
	 * 
	 */
	private void setReqAndInvField(int row) {
		String pk_corp = getBillCardPanel().getHeadItem(IItemKey.PKCORP)
				.getValue();
		String calid = getBillCardPanel().getHeadItem("pk_calbody").getValue();
		String whid = getBillCardPanel().getHeadItem("cwarehouseid").getValue();

		if (getBillCardPanel().getBodyValueAt(row, IItemKey.REQ_CORP) == null) {
			getBillCardPanel().setBodyValueAt(pk_corp, row, IItemKey.REQ_CORP);
			getBillCardPanel().getBillModel().execEditFormulaByKey(row,
					IItemKey.REQ_CORPNAME);
		}

		if (getBillCardPanel().getBodyValueAt(row, IItemKey.REQ_CAL) == null) {
			getBillCardPanel().setBodyValueAt(calid, row, IItemKey.REQ_CAL);
			getBillCardPanel().getBillModel().execEditFormulaByKey(row,
					IItemKey.REQ_CALNAME);
		}

		if (getBillCardPanel().getBodyValueAt(row, IItemKey.REQ_WH) == null) {
			getBillCardPanel().setBodyValueAt(whid, row, IItemKey.REQ_WH);
			getBillCardPanel().getBillModel().execEditFormulaByKey(row,
					IItemKey.REQ_WHNAME);
		}

		if (getBillCardPanel().getBodyValueAt(row, IItemKey.INV_CORP) == null) {
			getBillCardPanel().setBodyValueAt(pk_corp, row, IItemKey.INV_CORP);
			getBillCardPanel().getBillModel().execEditFormulaByKey(row,
					IItemKey.INV_CORPNAME);
		}
	}

	protected void afterBillItemSelChg(int iRow, int iCol) {

	}

	/**
	 * �ֿ�Ϳ����֯�༭��,���ñ���ļ��вɹ��ֶ�, ��������,û�������
	 */
	@Override
	public void afterCalbodyEdit(BillEditEvent e) {
		super.afterCalbodyEdit(e);
		setDefaultDataByHead();
	}

	/**
	 * �ֿ�Ϳ����֯�༭��,���ñ���ļ��вɹ��ֶ�, ��������,û�������
	 */
	@Override
	public void afterWhEdit(BillEditEvent e) {
		super.afterWhEdit(e);
		setDefaultDataByHead();
	}

	private void setBodyData(int irow, String key, String pk, String keyName) {
		if (getBillCardPanel().getBodyValueAt(irow, key) == null
				|| getBillCardPanel().getBodyValueAt(irow, key).toString()
						.trim().length() == 0) {

			getBillCardPanel().setBodyValueAt(pk, irow, key);
			getBillCardPanel().getBillModel().execEditFormulaByKey(irow,
					keyName);
		}
	}

	public void setDefaultDataByHead() {
		int row = getBillCardPanel().getRowCount();
		if (row <= 0)
			return;

		String pk_corp = getBillCardPanel().getHeadItem(IItemKey.PKCORP)
				.getValue();
		String calid = getBillCardPanel().getHeadItem("pk_calbody").getValue();
		String whid = getBillCardPanel().getHeadItem("cwarehouseid").getValue();

		for (int i = 0; i < row; i++) {
			// �鿴�Ƿ��������

			Object obj = getBillCardPanel().getBodyValueAt(i,
					IItemKey.CSOURCEBILLBID);
			if (obj != null && obj.toString().trim().length() > 0)
				continue;

			setBodyData(i, IItemKey.REQ_CORP, pk_corp, IItemKey.REQ_CORPNAME);
			setBodyData(i, IItemKey.REQ_CAL, calid, IItemKey.REQ_CALNAME);
			setBodyData(i, IItemKey.REQ_WH, whid, IItemKey.REQ_WHNAME);

		}

	}

	public boolean beforeBillItemEdit(BillEditEvent e) {
		// TODO �Զ����ɷ������
		return false;
	}

	/**
	 * ��ͷ�ɹ�˾������ʱ����ͷҵ��ԱӦ���ݲɹ���˾����
	 * 
	 * @since v5
	 * @author ljun
	 * 
	 */
	// v5
	public boolean beforeEdit(nc.ui.pub.bill.BillItemEvent e) {

		boolean bret = super.beforeEdit(e);
		if (bret == false)
			return false;

		String sItemKey = e.getItem().getKey();
		// �����ͷ�Ĳɹ���˾��ֵ�����ղɹ���˾���˲ɹ�Ա

		/*
		 * if (sItemKey.equals("cbizid")) {
		 * 
		 * nc.ui.pub.bill.BillItem bi2 = getBillCardPanel().getHeadItem(
		 * "cbizid");
		 * 
		 * UIRefPane purcorp = (UIRefPane) getBillCardPanel().getHeadItem(
		 * IItemKey.PUR_CORP).getComponent();
		 * 
		 * String pkcorpValue = purcorp.getRefPK(); if (pkcorpValue != null &&
		 * pkcorpValue.trim().length() != 0) RefFilter.filterPsnByDept(bi2,
		 * pkcorpValue, null); }
		 */

		// �ֿ�༭ǰ��Ҫ���տ����֯����
		if (sItemKey.equals(IItemKey.WAREHOUSE)) {
			String sCalID = getBillCardPanel().getHeadItem("pk_calbody") == null ? null
					: (String) getBillCardPanel().getHeadItem("pk_calbody")
							.getValueObject();
			if (sCalID != null && sCalID.length() > 0) {

				RefFilter.filtWh(getBillCardPanel().getHeadItem(
						IItemKey.WAREHOUSE), getEnvironment().getCorpID(),
						new String[] { " AND pk_calbody='" + sCalID + "'" });
			}
		}

		return true;

	}

	/**
	 * �����ߣ����˾� ���ܣ�����¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	@Override
	public void afterInvMutiEdit(nc.ui.pub.bill.BillEditEvent e) {
		super.afterInvMutiEdit(e);
		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
			setReqAndInvField(i);
		}
	}

	protected void beforeBillItemSelChg(int iRow, int iCol) {
		// TODO �Զ����ɷ������

	}

	/**
	 * �����ˣ������� ����ʱ�䣺2009-3-5 ����09:33:22 ����ԭ��л���뿪��������һ�£�����ȷ��V55�޸ķ���Ϊ��
	 * 
	 * 1�����ɹ���ⵥ�Ĳɹ����š��ɹ�Ա�����в��š���ԱȨ�޿��ƣ�
	 * 2�����ɹ���ⵥ�������δ������ɹ���ⵥ��������ⵥ��Ӧ����Ӧ����ʱ������ɹ���ⵥ�Ĳ��š���Ա���ǵ�ǰ��˾�Ĳ��š���Ա����������ε��ݵĲ��š���Ա��
	 * 
	 * ��ʷ���ݲ���������������⣬ͨ��ר������
	 */
	public void filterRef(String sCorpID) {
		super.filterRef(sCorpID);
		nc.ui.pub.bill.BillItem bi = getBillCardPanel().getHeadItem("cbizid");
		RefFilter.filterPsnByDept(bi, null, null);
		bi = getBillCardPanel().getHeadItem("cdptid");
		RefFilter.filterDept(bi, null, null);
	}

	/**
	 * ��������� �������Ʊ��
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-1-17����04:10:12
	 * @param flag
	 * @param o
	 */
	public void onAdd(boolean bUpataBotton, GeneralBillVO voBill) {
		bUpataBotton = true;

		super.onAdd(bUpataBotton, null);
		getBillCardPanel().getHeadItem(HgPubConst.F_IS_SELF).setValue(
				HgPubConst.SELF);
		setHeadEditEnableWhenBillIn(true);// ���ñ�ͷ�ɱ༭Ԫ��
		setAfterIn(true);
		setButtonsEnable(false);// �����ʼ찴ť�Ƿ����
		setButtonEnable(ICButtonConst.BTN_LINE, true);// �����в�����ť����
	}

	/**
	 * ���ñ���Ԫ�ز��ɱ༭��ʵ���������⣩
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-12-14����05:10:35
	 * @param flag
	 */
	private void setAdjustFlag(boolean flag) {
		m_isAdjust = flag;
	}

	/**
	 * ���ò�����ť������
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-12-15����10:53:46
	 * @param buttonName
	 * @param flag
	 */
	private void setButtonEnable(String buttonName, boolean flag) {
		getButtonManager().getButtonTree().getButton(buttonName).setEnabled(
				flag);
		this.updateButton(getButtonManager().getButton(buttonName));// ���ò�����ť������
	}

	// ���� �׸ڿ�ҵ���Ӱ�ť�ı༭��
	private void setButtonsEnable(boolean flag) {
		setButtonEnable(HgPuBtnConst.ACPT, flag);// �����ʼ찴ť����
		setButtonEnable(HgPuBtnConst.UNACPT, flag);
		setButtonEnable(HgPuBtnConst.UNDEAL, flag);
		setButtonEnable(HgPuBtnConst.WARE, flag);
		setButtonEnable(HgPuBtnConst.MATCH, flag);
//		setButtonEnable(HgPuBtnConst.CANCELMATCH, flag);
	}

	/**
	 * ���ñ�ͷԪ�ز��ɱ༭
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-12-14����05:10:35
	 * @param flag
	 */
	private void setHeadEditEnableWhenBillIn(boolean flag) {
		String[] headItems = HgPubConst.PurchaseIn_HeadItems;
		for (String key : headItems) {
			getBillCardPanel().getHeadItem(key).setEdit(flag);
		}
	}
	/**
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-4-14����05:02:26
	 * @param msg
	 */
	public void setAfterIn(boolean flag){
		String[] head={"dbilldate","cwarehouseid","cbiztype","cdispatcherid","cwhsmanagerid","cbizid","cdptid","cproviderid"};
		String[]  body={"vbatchcode","vuserdef18","vuserdef19"};
		for (String key : head) {
			getBillCardPanel().getHeadItem(key).setEdit(flag);
		}
		for (String key : body) {
			getBillCardPanel().getBillModel().getItemByKey(key).setEnabled(flag);
		}
		
	}
	
	/**
	 * 
	 * �����������������ݿ��̹�������ÿ��̵Ļ���������
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param pk_cumangid
	 *            ���̹�������ID
	 * @return ���̻�������ID
	 *         <p>
	 * @author duy
	 * @time 2007-3-20 ����10:50:26
	 */
	private String getPk_cubasdoc(String pk_cumandoc) {
		if (pk_cumandoc == null)
			return null;
		try {
			Object[] pks = (Object[]) nc.ui.scm.pub.CacheTool.getColumnValue(
					"bd_cumandoc", "pk_cumandoc", "pk_cubasdoc",
					new String[] { pk_cumandoc });
			if (pks != null)
				return (String) pks[0];
		} catch (BusinessException e) {
			nc.ui.ic.pub.tools.GenMethod
					.handleException(this, null, e);
		}
		return null;
	}
	
	private void setBodyCelValue(int rowCount, String filedname, Object oValue) {
		for (int i = 0; i < rowCount; i++) {
			getBillCardPanel().setBodyValueAt(oValue, i, filedname);
		}
	}
}
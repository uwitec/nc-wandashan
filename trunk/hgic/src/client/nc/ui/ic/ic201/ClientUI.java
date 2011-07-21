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
 * 此处插入类型说明。 创建日期：(2001-11-23 15:39:43)
 * 
 * @author：王乃军
 * 
 */
public class ClientUI extends nc.ui.ic.pub.bill.GeneralBillClientUI {
	// protected ButtonObject m_boRatioOut = new ButtonObject(
	// nc.ui.ml.NCLangRes.getInstance().getStrByID("40080602",
	// "UPT40080804-000004")/* @res "配比出库" */,
	// nc.ui.ml.NCLangRes.getInstance().getStrByID("40080602",
	// "UPT40080804-000004")/* @res "配比出库" */, 0, "配比出库");/*-=notranslate=-*/

	// zhw begin
	/** 验收信息对话框 */
	private CkeckDLG m_CkeckDLG = null;
	private UnCheckDealDLG m_UnCheckDealDLG = null;
	boolean m_isAdjust = false; // 表体实入编辑态控制
	// end

	// 配套界面对话框
	private nc.ui.ic.auditdlg.ClientUIInAndOut m_dlgInOut = null;

	// 保存借转业务类型id
	private ArrayList m_alBrwLendBusitype = null;

	// 业务类型itemkey
	private final String m_sBusiTypeItemKey = "cbiztype";

	private IButtonManager m_buttonManager;

	/**
	 * ClientUI2 构造子注解。
	 */
	public ClientUI() {
		super();
		initialize();
	}

	/**
	 * ClientUI 构造子注解。 add by liuzy 2007-12-18 根据节点编码初始化单据模版
	 */
	public ClientUI(FramePanel fp) {
		super(fp);
		initialize();
	}

	/**
	 * ClientUI 构造子注解。 nc 2.2 提供的单据联查功能构造子。
	 * 
	 */
	public ClientUI(String pk_corp, String billType, String businessType,
			String operator, String billID) {
		super(pk_corp, billType, businessType, operator, billID);

	}

	/**
	 * 快速录入集中采购的表体字段
	 */

	public void setBodyDefaultData(int istartrow, int count) {

		// 查看是否参照上游
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
			// 如果需求公司不为空,则不能带入表头的值
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
			// 供应商
			afterProviderEdit(e);
		}else if ("vbodynote2".equalsIgnoreCase(sItemKey)) {
			nc.ui.pub.beans.UIRefPane refPaneReasonBody = (nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getBodyItem("vbodynote2").getComponent();
			String sBodyNoteCode = refPaneReasonBody.getText();
			String sBodyNoteCodeContent = (String) refPaneReasonBody
					.getRefValue("cbackreasonname");

			String sReturnResult = null;
			// 如果有对应的内容
			if (sBodyNoteCodeContent != null)
				sReturnResult = sBodyNoteCodeContent;
			else if (sBodyNoteCode != null) // 输入的编码没有对应的退库理由，则认为直接输入理由
				sReturnResult = sBodyNoteCode;
			// 置上返回结果
			if (sReturnResult != null) {
				getBillCardPanel().setBodyValueAt(sReturnResult, row,
						"vbodynote2");
			}

		}

	}

	/**
	 * 作者：李俊 功能：供应商编辑后事件：带出简称 参数： 返回： 例外： 日期：(2004-6-21 10:36:30)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void afterProviderEdit(nc.ui.pub.bill.BillEditEvent e) {

		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cproviderid").getComponent()).getRefName();
		String sRefPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cproviderid").getComponent()).getRefPK();

		// 保存名称以在列表形式下显示。
		if (getM_voBill() != null)
			getM_voBill().setHeaderValue("cprovidername", sName);

		// 根据客户或供应商过滤发运地址的参照
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
			// 根据参照带出供应商简称
			nc.ui.scm.pub.TwoTableCacheFind twoTable = new nc.ui.scm.pub.TwoTableCacheFind();
			String sProviderShortName = twoTable.getJoinTableFieldValue(
					"bd_cumandoc", sRefPK, "custshortname");
			if (iProvidername != null) {
				iProvidername.setValue(sProviderShortName);
			}

			// 获得客商基本档案ID
			String sPk_cubasdoc = getPk_cubasdoc(sRefPK);
			if (iPk_cubasdoc != null)
				iPk_cubasdoc.setValue(sPk_cubasdoc);

			if (getM_voBill() != null) {
				getM_voBill().setHeaderValue("cprovidershortname",
						sProviderShortName);
				getM_voBill().setHeaderValue("pk_cubasdoc", sPk_cubasdoc);
			}
			// modify by zhw 表体选择供应商  表体自动带出  采购入库单
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
	 * 创建者：王乃军 功能：单据体、列表上表编辑事件处理 参数：e 单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	@Override
	public void bodyRowChange(nc.ui.pub.bill.BillEditEvent e) {
		super.bodyRowChange(e);

		// // 由于配套针对仅一行表体行，根据选择的行判断，表单下，浏览状态，未签字，未做过配套的单据,有来源单据且来源单据是外模块单据
		// // 配套按钮可用。
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
	 * 创建者：王乃军 功能：抽象方法：保存前的VO检查 参数：待保存单据 返回： 例外： 日期：(2001-5-24 下午 5:17)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected boolean checkVO(nc.vo.ic.pub.bill.GeneralBillVO voBill) {
		return this.checkVO();
	}

	/**
	 * 创建者：王乃军 功能：得到查询对话框 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
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
	// * "入库"
	// */},
	// {
	// nc.vo.ic.pub.BillTypeConst.BILLSENDBACK,
	// nc.ui.ml.NCLangRes.getInstance().getStrByID(
	// "4008busi", "UPT40080602-000014") /*
	// * @res
	// * "退库"
	// */},
	// {
	// nc.vo.ic.pub.BillTypeConst.BILLALL,
	// nc.ui.ml.NCLangRes.getInstance().getStrByID(
	// "4008busi", "UPPSCMCommon-000217") /*
	// * @res
	// * "全部"
	// */} });
	// // zhy2005-04-23采购入库单需要过滤供应商权限
	// // 比基类多出一个供应商
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
																	"UPP4008busi-000367") /** @res* "入库" */
											},
											{
													nc.vo.ic.pub.BillTypeConst.BILLSENDBACK,
													nc.ui.ml.NCLangRes
															.getInstance()
															.getStrByID(
																	"4008busi",
																	"UPT40080602-000014") /** @res* "退库" */
											},
											{
													nc.vo.ic.pub.BillTypeConst.BILLALL,
													nc.ui.ml.NCLangRes
															.getInstance()
															.getStrByID(
																	"4008busi",
																	"UPPSCMCommon-000217") /**
																							 * @res *
																							 *      "全部"
																							 */
											} });
					getQueryDlg().setInitDate("freplenishflag",
							nc.vo.ic.pub.BillTypeConst.BILLNORMAL);
					// zhy2005-04-23采购入库单需要过滤供应商权限
					// 比基类多出一个供应商
					getQueryDlg().setCorpRefs("head.pk_corp",
							new String[] { "head.cproviderid" });
				}
			};
		}
		return m_qryDlgHelp;
	}

	/**
	 * 返回 ReturnDlg1 特性值。
	 * 
	 * @return nc.ui.ic.auditdlg.ClientUIInAndOut
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 子类实现该方法，返回业务界面的标题。
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTitle() {
		return super.getTitle();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-11-19 9:10:04)
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
		SCMEnv.showTime(lTime, "initialize:init退货理由:");/*-=notranslate=-*/
	}

	/**
	 * 创建者：王乃军 功能：初始化系统参数 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void initPanel() {
		// 需要单据参照
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
	 * 创建者：王乃军 功能：是否借转类型
	 * 
	 * 第一次需要读库。
	 * 
	 * 参数： 返回： 例外： 日期：(2001-11-24 12:15:42) 修改日期，修改人，修改原因，注释标志：
	 */
	protected boolean isBrwLendBiztype() {
		try {
			GeneralBillVO voMyBill = null;
			// 业务类型
			String sBusitypeid = null;
			if (getM_iCurPanel() == BillMode.List) { // 列表形式下
				if (getM_alListData() != null
						&& getM_iLastSelListHeadRow() >= 0
						&& getM_alListData().size() > getM_iLastSelListHeadRow()
						&& getM_alListData().get(getM_iLastSelListHeadRow()) != null) {
					voMyBill = ((GeneralBillVO) getM_alListData().get(
							getM_iLastSelListHeadRow()));
					sBusitypeid = (String) voMyBill
							.getHeaderValue(m_sBusiTypeItemKey);
				}
			} else { // 表单
				if (getBillCardPanel().getHeadItem(m_sBusiTypeItemKey) != null
						&& getBillCardPanel().getHeadItem(m_sBusiTypeItemKey)
								.getComponent() != null) {
					nc.ui.pub.beans.UIRefPane ref = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
							.getHeadItem(m_sBusiTypeItemKey).getComponent());
					// 置pk
					sBusitypeid = ref.getRefPK();
				}
			}
			// 业务类型不为空时
			// 第一次需要读库。
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

				// 如果返回空，初始化之，标志已经读过了，但没有借转类型啊!
				if (m_alBrwLendBusitype == null)
					m_alBrwLendBusitype = new ArrayList();
			}
			// 是借转类型的，返回“是”
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
		 * 子类实现该方法，响应按钮事件。
		 * 
		 * @version (00-6-1 10:32:59)
		 * 
		 * @param bo
		 *            ButtonObject
		 */
		public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
			showHintMessage(bo.getName());

			// add by zhw 已经封存的单据不能做以下操作
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

					if (bo == getButtonTree().getButton(HgPuBtnConst.ACPT))// 验收
						onCheck();
					else if (bo == getButtonTree().getButton(
							HgPuBtnConst.UNACPT))// 取消验收
						onUNCheck();
					else if (bo == getButtonTree().getButton(
							HgPuBtnConst.UNDEAL))// 不合格处理
						onDeal();
					else if (bo == getButtonTree().getButton(HgPuBtnConst.WARE))// 入库
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
		 * 已经封存的单据不可以执行的操作
		 * 
		 * @author zhw
		 * @说明：（鹤岗矿业） 2011-12-15下午06:31:10
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
						showErrorMsg("该单据已经封存，不可操作");
						return false;
					}
				}
			}
			return true;
		}

		/**
		 * 验收处理
		 * 
		 * @author zhw
		 * @说明：（鹤岗矿业） 2011-12-8下午06:23:10
		 */
		private void onCheck() {
			GeneralBillVO vo = null;
			try {
				vo = getCurVO();

				if (vo == null)
					return;

				if (HgPubConst.purchaseIn_in == HgPubTool.getPurchaseInBusiStatus(vo)) {
					showErrorMsg("该单据已经入库,不能验收");
					return;
				}

				if (HgPubConst.purchaseIn_ok == HgPubTool.getPurchaseInBusiStatus(vo)
						|| HgPubConst.purchaseIn_no == HgPubTool.getPurchaseInBusiStatus(vo)
						|| HgPubConst.purchaseIn_nopact == HgPubTool.getPurchaseInBusiStatus(vo)) {
					showErrorMsg("该单据已经验收，不可重复验收");
					return;
				}
			    
				vo.getHeaderVO().setAttributeValue("cuserid",getClientEnvironment().getUser().getPrimaryKey());
				getCkeckDLG().setGbillvo(vo);// 向对话框中传VO
				getCkeckDLG().initDataPanel();// 初始化对话框中的PANEL
				
				if (getCkeckDLG().showModal() != UIDialog.ID_OK) {
					return;
				}
				
				GeneralBillVO billVO = getCkeckDLG().getGbillvo();
				
				if (HgPubConst.purchaseIn_ok == HgPubTool.getPurchaseInBusiStatus(billVO)) {
					showHintMessage("验收合格");
				} else if (HgPubConst.purchaseIn_no == HgPubTool.getPurchaseInBusiStatus(billVO)) {
					showHintMessage("验收不合格");
				}
				
				setDataToUI(billVO);// 向界面设置数据
			
				// 设置缓存VO
				getM_alListData().set(getM_iLastSelListHeadRow(), billVO);
				setM_voBill(billVO);
				
			} catch (Exception e) {
				showErrorMessage(HgPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
				e.printStackTrace();
			}
		}

		/**
		 * 验收对话框
		 * 
		 * @author zhw
		 * @说明：（鹤岗矿业） 2011-12-8下午08:51:37
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
		 * 验收完成后 向界面设置数据
		 * 
		 * @author zhw
		 * @说明：（鹤岗矿业） 2011-12-8下午08:52:50
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

		// 执行公式 显示 处理人 验收人
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
		 * 验收完成的单据不能修改
		 * 
		 * @author zhw
		 * @说明：（鹤岗矿业） 2011-12-8下午08:52:50
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
					
					showErrorMsg("该单据已经验收，不能修改");
					return;
				}
			}
			super.onUpdate();
			
			//在入库之后可以修改一些字段
			if(HgPubConst.purchaseIn_in == HgPubTool.getPurchaseInBusiStatus(vo)){
				
				setAfterIn(false);
			
			}else{
				
				setHeadEditEnableWhenBillIn(true);// 设置表头可编辑元素
			}
			
			setButtonsEnable(false);// 设置质检按钮是否可用

		}

		private void showErrorMsg(String msg) {
			
			MessageDialog.showErrorDlg(this.getClientUI(), "",msg == null ? "操作出现异常，请重新操作" : msg);
		}

		/**
		 * 取消验收
		 * 
		 * @author zhw
		 * @说明：（鹤岗矿业） 2011-12-8下午08:55:38
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
					
					showErrorMsg("该单据已经入库,不能取消验收");
					return;
				}
				
				boolean isPact = false;
				
				for (GeneralBillItemVO item : items) {
					
					if (PuPubVO.getString_TrimZeroLenAsNull(item.getCsourcetype()) != null)
						isPact = true;
				}
				String self = HgPubTool.getString_NullAsTrimZeroLen(getBillCardPanel()
								.getHeadItem(HgPubConst.F_IS_SELF).getValueObject());
				
				// 如果是自制 不进行合同匹配校验
				if ((HgPubConst.SELF).equalsIgnoreCase(self)) {
					if (isPact) {
						showErrorMsg("该单据已经合同匹配,不能取消验收");
						return;
					}
				}

				if (HgPubConst.purchaseIn_arr == HgPubTool.getPurchaseInBusiStatus(vo)) {
					showErrorMsg("该单据未验收,不能取消验收");
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
				
				changeVO(vo);// VO转换
				setDataToUI(vo);// 向界面设置数据
				setDataToUIForUnCheckDeal(vo);
				
				// 设置缓存VO
				getM_alListData().set(getM_iLastSelListHeadRow(), vo);
				setM_voBill(vo);

			} catch (Exception e) {
				showErrorMsg(HgPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
				e.printStackTrace();
			}
		}

		/**
		 * 取消验收的vo清空
		 * 
		 * @author zhw
		 * @说明：（鹤岗矿业） 2011-12-8下午08:56:13
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
		 * 不合格处理
		 * 
		 * @author zhw
		 * @说明：（鹤岗矿业） 2011-12-8下午08:56:47
		 */
		private void onDeal() {
			
			GeneralBillVO vo = null;
			
			try {
				vo = getCurVO();
				
				if (vo == null)
					return;

				if (HgPubConst.purchaseIn_in == HgPubTool.getPurchaseInBusiStatus(vo)) {
					showErrorMsg("该单据已经入库,不能处理");
					return;
				}
				
				if (HgPubConst.purchaseIn_ok == HgPubTool.getPurchaseInBusiStatus(vo)
						|| HgPubConst.purchaseIn_nopact == HgPubTool.getPurchaseInBusiStatus(vo)) {
					
					showErrorMsg("该单据合格,不用处理");
					return;
				}

				if (HgPubConst.purchaseIn_arr == HgPubTool.getPurchaseInBusiStatus(vo)) {
					showErrorMsg("该单据没有验收,不能处理");
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
				
				// 设置缓存VO
				getM_alListData().set(getM_iLastSelListHeadRow(), billVO);
				setM_voBill(billVO);

			} catch (Exception e) {
				showErrorMsg(HgPubTool.getString_NullAsTrimZeroLen(e
						.getMessage()));
				e.printStackTrace();
			}
		}

		/**
		 * 不合格处理的界面数据设置
		 * 
		 * @author zhw
		 * @说明：（鹤岗矿业） 2011-12-8下午08:57:55
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
		 * 不合格处理对话框
		 * 
		 * @author zhw
		 * @说明：（鹤岗矿业） 2011-12-8下午08:57:30
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
		 * 单据入库处理 可以入库时将验收数量更新到入库数量
		 * 
		 * @author zhw
		 * @说明：（鹤岗矿业） 2011-12-8下午09:13:09
		 */
		private void onBillIn() {
		   
			GeneralBillVO vo = null;
			
		    try {
				
		    	vo = getCurVO();
				
		    	if (vo == null) {
					showErrorMsg("请选择要调整的单据");
					return;
				}
				
		    	GeneralBillHeaderVO head = vo.getHeaderVO();
				GeneralBillItemVO[] items = (GeneralBillItemVO[]) vo.getChildrenVO();
				
				int len = items.length;
				
				if (HgPubConst.purchaseIn_in == HgPubTool.getPurchaseInBusiStatus(vo)) {
					showErrorMsg("该单据已经入库,不能重复入库");
					return;
				}

				if (HgPubConst.purchaseIn_arr == HgPubTool.getPurchaseInBusiStatus(vo)) {
					showErrorMsg("该单据没有验收,不用入库");
					return;
				}

				if (HgPubConst.purchaseIn_no == HgPubTool.getPurchaseInBusiStatus(vo)) {// 不合格
					showErrorMsg("该单据不合格,不用入库");
					return;
				}

				if (HgPubConst.purchaseIn_nopact == HgPubTool.getPurchaseInBusiStatus(vo)) {
					showErrorMsg("该单据没有合同信息,请先匹配合同信息,然后入库");
					return;
				}

				super.onUpdate();
				
				setAdjustFlag(true);
				setHeadEditEnableWhenBillIn(false);
				setButtonEnable(ICButtonConst.BTN_LINE, false);// 设置行操作按钮不可用
				setButtonsEnable(false);// 设置质检按钮是否可用
			
				// 更新入库数量
				for (int i = 0; i < len; i++) {
					
					UFDouble nkdnum = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel().getValueAt(i,HgPubConst.NUM_DEF_QUA));
					getBillCardPanel().getBillModel().setValueAt(nkdnum, i,"ninnum");
					getBillCardPanel().getBillModel().setRowState(i,BillModel.MODIFICATION);// 设置表体修改态
					
					// 表体不修改时也可以将自动带出数据保存
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
				
				// 设置缓存VO
				getM_alListData().set(getM_iLastSelListHeadRow(), vo);
				setM_voBill(vo);

			} catch (Exception e) {
				showErrorMsg(HgPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
				e.printStackTrace();
			}
		}

		/**
		 * 设置表体元素不可编辑（实入数量除外）
		 * 
		 * @author zhw
		 * @说明：（鹤岗矿业） 2011-12-14下午05:10:35
		 * @param flag
		 */
		private void setAdjustFlag(boolean flag) {
			m_isAdjust = flag;
		}

		/**
		 * 设置操作按钮不可用
		 * 
		 * @author zhw
		 * @说明：（鹤岗矿业） 2011-12-15上午10:53:46
		 * @param buttonName
		 * @param flag
		 */
		private void setButtonEnable(String buttonName, boolean flag) {
		    
			getButtonManager().getButtonTree().getButton(buttonName).setEnabled(flag);
			getClientUI().updateButton(getButtonManager().getButton(buttonName));// 设置操作按钮不可用
		}

		// 设置 鹤岗矿业增加按钮的编辑性
		private void setButtonsEnable(boolean flag) {
			
			setButtonEnable(HgPuBtnConst.ACPT, flag);// 设置质检按钮可用
			setButtonEnable(HgPuBtnConst.UNACPT, flag);
			setButtonEnable(HgPuBtnConst.UNDEAL, flag);
			setButtonEnable(HgPuBtnConst.WARE, flag);
			setButtonEnable(HgPuBtnConst.MATCH, flag);
		}

		/**
		 * 设置表头元素不可编辑
		 * 
		 * @author zhw
		 * @说明：（鹤岗矿业） 2011-12-14下午05:10:35
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
		 * @说明：（鹤岗矿业）
		 * 2011-4-14下午05:02:26
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
					setButtonsEnable(true);// 设置质检按钮可用
					
					if (vo == null) {
						showErrorMsg("请选择要取消的单据");
						return;
					}
					
					GeneralBillItemVO[] items = (GeneralBillItemVO[]) vo.getChildrenVO();
					
					int len = items.length;
					
					// 更新入库数量
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
		 * 查询后 把入库按钮状态还原
		 */
		protected void onQuery(boolean flag) {
			super.onQuery(flag);
			setButtonsEnable(true);
		}

		// zhw end

		/**
		 * 
		 * 方法功能描述：生成固定资产卡片。
		 * <p>
		 * <b>参数说明</b>
		 * <p>
		 * 
		 * @author duy
		 * @time 2008-5-22 上午10:14:45
		 */
		private void onGenerateAssetCard() {
			try {
				// 调用后台接口生成固定资产卡片
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
				// 日志异常
				nc.vo.scm.pub.SCMEnv.out(e);
				showErrorMessage(e.getMessage());
			}
		}

		/**
		 * 
		 * 方法功能描述：取消生成固定资产卡片。
		 * <p>
		 * <b>参数说明</b>
		 * <p>
		 * 
		 * @author duy
		 * @time 2008-5-22 上午10:26:35
		 */
		private void onCancelGenerateAssetCard() {
			try {
				// 调用后台接口生成固定资产卡片
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
				// 日志异常
				nc.vo.scm.pub.SCMEnv.out(e);
				showErrorMessage(e.getMessage());
			}
		}

		/**
		 * v5:增行时将表头的公司设置为表体的需求公司和采购公司
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
		 * v5:增行时将表头的公司设置为表体的需求公司和采购公司
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
		 * 创建者：王乃军 功能：保存，如果是借转类型的，清货位、序列号。 参数： 返回： 例外： 日期：(2001-11-24 12:15:42)
		 * 修改日期，修改人，修改原因，注释标志：
		 * 
		 */
		protected boolean onSave() {
			// add by zhw 校验数量之间的关系
			int rowCount = getBillCardPanel().getBillModel().getRowCount();

			// zhf add 入库单不能存在相同存货的行 原因 同一单据同一存货会生成相同的批次
			List<String> linv = new ArrayList<String>();
			for (int i = 0; i < rowCount; i++) {
				String invid = PuPubVO
						.getString_TrimZeroLenAsNull(getBillCardPanel()
								.getBodyValueAt(i, "cinvbasid"));
				if (invid == null) {
					showErrorMessage("第" + (i + 1) + "行存货不能为空");
					return false;
				}
				if (linv.contains(invid)) {
					showErrorMessage("同一单据多行表体不能存在相同存货");
					return false;
				}
				linv.add(invid);
				// zhf end20110211
				UFDouble nkdnum = PuPubVO
						.getUFDouble_NullAsZero(getBillCardPanel()
								.getBillModel().getValueAt(i,
										HgPubConst.NUM_DEF_QUA));// 验收合格数量
				UFDouble arr = PuPubVO
						.getUFDouble_NullAsZero(getBillCardPanel()
								.getBillModel().getValueAt(i,
										HgPubConst.NUM_DEF_ARR));// 到货数量
				UFDouble fac = PuPubVO
						.getUFDouble_NullAsZero(getBillCardPanel()
								.getBillModel().getValueAt(i,
										HgPubConst.NUM_DEF_FAC));// 实收数量

				UFDouble shouldInNum = PuPubVO
						.getUFDouble_NullAsZero(getBillCardPanel()
								.getBillModel().getValueAt(i, "nshouldinnum"));// 合同数量

				//
				UFDouble ninnum = PuPubVO
						.getUFDouble_NullAsZero(getBillCardPanel()
								.getBillModel().getValueAt(i, "ninnum"));// 实入数量
				// 如果有入库数量 且进行货位管理 则货位必填
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
//							showErrorMessage("第" + (i + 1) + "行货位不能为空");
//							return false;
//						}
//					}
//				} catch (BusinessException e) {
//					e.printStackTrace();
//					showErrorMessage("获取该仓库是否进行货位管理出错");
//					return false;
//				}
				if (!PuPubVO.getUFBoolean_NullAs(
						getBillCardPanel().getHeadItem("freplenishflag")
								.getValueObject(), UFBoolean.FALSE)
						.booleanValue()) {
					if (UFDouble.ZERO_DBL.compareTo(arr) >= 0) {
						showErrorMessage("第" + (i + 1) + "行到货数量必须大于零");
						return false;
					}

					if (UFDouble.ZERO_DBL.compareTo(fac) >= 0) {
						showErrorMessage("第" + (i + 1) + "行实收数量必须大于零");
						return false;
					}
					if (arr.compareTo(fac) < 0) {
						showErrorMessage("第" + (i + 1) + "行实收数量不能大于到货数量");
						return false;
					}

					if (!UFDouble.ZERO_DBL.equals(nkdnum)) {
						if (UFDouble.ZERO_DBL.compareTo(ninnum) >= 0) {
							showErrorMessage("第" + (i + 1) + "行入库数量必须大于零");
							return false;
						} else if (ninnum.compareTo(nkdnum) > 0) {
							showErrorMessage("第" + (i + 1) + "行入库数量不能大于验收数量");
							return false;
						}
					}
				}
			}
			// add by zhw end
			if (isBrwLendBiztype()) {
				// 保存前清货位数据
				m_alLocatorDataBackup = getM_alLocatorData();
				setM_alLocatorData(null);
				// 保存前清序列号数据
				m_alSerialDataBackup = getM_alSerialData();
				setM_alSerialData(null);
			}
			// 检查存在源头单据号的赠品存量是否大于0

			// 修改人：刘家清 修改日期：2007-8-13下午04:51:06
			// 修改原因：解决如下问题,先做退货的入库单，录入条码后，数量为负的；然后再做正的时候，录入条码，增加的数量也是为负的。
			if (super.onSave()) {
				setFixBarcodenegative(false);// 条码数量为正数
				// add by zhw 2010-12-14 设置表体编辑性
				setAdjustFlag(false);// 设置表体编辑性
				setButtonsEnable(true);// 设置质检按钮是否可用
				//在入库之后可以修改一些字段
				// zhw end
				return true;
			}
			return false;
		}

		/**
		 * 创建者：王乃军 功能：序列号分配 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
		 * 修改日期，修改人，修改原因，注释标志：
		 * 
		 * 
		 * 
		 * 
		 */
		public void onSNAssign() {
			// 非浏览模式下，如果是借转类型的，不需要此操作。
			if (BillMode.Browse != getM_iMode() && isBrwLendBiztype()) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000273")/*
															 * @res
															 * "特殊业务类型，不需要执行此操作。请在浏览状态下查看。"
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
																	 * "没有对应的借入/出单，无法查询到货位、序列号数据。请检查单据来源！"
																	 */);
							return;
						} else
							showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008busi",
											"UPP4008busi-000275")/*
																	 * @res
																	 * "请察看相关借入/借出单的数据。"
																	 */);
					}
					return;
				}
			}
			super.onSNAssign();
		}

		/**
		 * 创建者：王乃军 功能：货位分配 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
		 * 
		 * 
		 * 
		 * 
		 */
		public void onSpaceAssign() {
			// 非浏览模式下，如果是借转类型的，不需要此操作。
			if (BillMode.Browse != getM_iMode() && isBrwLendBiztype()) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000273")/*
															 * @res
															 * "特殊业务类型，不需要执行此操作。请在浏览状态下查看。"
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
																	 * "没有对应的借入/出单，无法查询到货位、序列号数据。请检查单据来源！"
																	 */);
							return;
						} else
							showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008busi",
											"UPP4008busi-000275")/*
																	 * @res
																	 * "请察看相关借入/借出单的数据。"
																	 */);
					}
					return;
				}
			}
			super.onSpaceAssign();
		}

		/**
		 * 配套功能按钮方法。 功能： 参数： 返回： 例外： 日期：(2002-04-18 10:43:46)
		 * 修改日期，修改人，修改原因，注释标志：
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
																			 * "是否对本单据选中行中所有成套件自动进行配套处理，生成其它出、入库单据?"
																			 */)) {
					return;

				}

				// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				// "4008busi", "UPT40080602-000007")/* @res "配套" */);

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

					// 表体行
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
					// 修改人:刘家清 修改日期:2007-04-12
					// 修改原因:对于出库跟踪入库的存货将对应入库单号和对应表体序列号自动携带到生成出库单上
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

					// 置其它入库VO，应是采购入库单据的子项存货。

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
																				 * "配套中套件没有定义配件！请定义配件后再进行配套！"
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

				// // 采购入库生成的配件入库单需要重置单据号
				nc.ui.scm.pub.report.BillRowNo.setVORowNoByRule(gbvoIn,
						nc.vo.ic.pub.BillTypeConst.m_otherIn, "crowno");

				alInGeneralVO.add(gbvoIn);

				getDispenseDlg(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
								"UPP4008busi-000269")/*
														 * @res "配套生成：其它入/其它出库单"
														 */, alInGeneralVO, alOutGeneralVO).showModal();
				if (m_dlgInOut.isOK()) {
					try { // 更新表尾
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
						// 配套成功后，需要重置单据的按钮（删除，修改，复制按钮不可用，）

						// setBillState();
						// can not dispense the inv more over, after create
						// the other in and out bill!
						// m_boDispense.setEnabled(false);
						ctrlSourceBillButtons(true);
						/*
						 * for(int i=0;i<rownums.length;i++)
						 * super.refreshSelBill(rownums[i]);
						 */
						// 修复bug-NCdp200017720-相关缺陷:NCdp200016800-刘家清-200603191641--更新Ts
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
						// 修复bug-NCdp200017720-相关缺陷:NCdp200016800-刘家清-200603191641

					} catch (Exception e) {
						handleException(e);
						showErrorMessage(e.getMessage());
					}
				}
			}
		}

		/**
		 * 采购启用时调用质量管理扣吨计算的方法，计算 毛重-皮重 返回的数， 将此数设置到表体行上
		 * 
		 * @author ljun
		 * @since v5 走采购->质检->入库的流程才有扣吨计算
		 */
		private void onKDJS() {

			// 当前单据状态为编辑
			if (getM_iMode() == BillMode.Browse) {
				showWarningMessage(ResBase.get201KD01());
				return;
			}
			int rows = getBillCardPanel().getBillTable().getRowCount();
			if (rows <= 0) {
				return;
			}
			// 当前公司和采购公司不相等时不能做扣吨
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
			// 走质量检验到货的才进行扣吨
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

			// 设置扣吨到界面
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
					map.put(iI, ufdArray[i]);// 扣吨数量
				}
			}

			// 设置扣顿数量到界面扣顿数量字段 //反算实际数量: 实际输入数量＝毛重-皮重-koudun
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
					// 辅助数量等变化
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
		 * 此处插入方法说明。 创建日期：(2003-10-9 14:43:10) 自制退库单 退货标志为"Y"。 退货理由可编辑，
		 * 其他情况下退货理由不可编辑， 需要重载onNew(),把退货理由置为不可编辑。
		 */
		public void onNewReplenishInvBill() {
			super.onAdd();
			// 设置表体的退货理由和表头的退货理由可以编辑
			// 检查数量必须是负数
			nc.ui.ic.pub.bill.GeneralBillUICtl.setSendBackBillState(
					getClientUI(), true);
			getM_voBill().getHeaderVO().setFreplenishflag(new UFBoolean(true));

			setFixBarcodenegative(true);// 条码数量为负数

			getBillCardPanel().getHeadItem("cproviderid").setEdit(true);
			getBillCardPanel().getHeadItem("cproviderid").setEnabled(true);
		}

		/**
		 * 此处插入方法说明。 创建日期：(2003-10-9 14:43:10) 参照采购退货订单新增退库单。by hanwei
		 * 2003-10-14
		 * 
		 * 
		 */
		public void onNewReplenishInvBillByOrder() {

			IFromPoUI ui = null;
			try {
				ui = (IFromPoUI) Class.forName("nc.ui.ic.ic201.FromPoUI")
						.newInstance();
				setBillEdit();// add by zhw 退库时设置单据字段的可编辑性
			} catch (Exception e) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000272")/*
															 * @res
															 * "该功能不可用（原因：采购管理系统没有安装）。"
															 */);
				return;
			}
			ui.onNewReplenishInvBillByOrder((ClientUI) getClientUI(),
					getClientUI().getEnvironment().getCorpID());

		}
	}

	/**
	 * 退库的时候 设置表头表体的可编辑性
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-3-26上午10:07:07
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
	 * @说明：（鹤岗矿业） 取消合同匹配  入库前  合同匹配后
	 * 2011-4-23下午01:18:20
	 */
	private void onCancelMatchPact(){
		try {
			GeneralBillVO gbillvo = getCurVO();
			if(gbillvo == null){
				showHintMessage("当前单据为空");
				return;
			}		
			
			if (HgPubConst.purchaseIn_in == HgPubTool
					.getPurchaseInBusiStatus(gbillvo)) {
				showWarningMessage("该单据已经入库,不能取消合同匹配");
				return;
			}
			GeneralBillHeaderVO head = gbillvo.getHeaderVO();
			if(!HgPubTool.getString_NullAsTrimZeroLen(head.getAttributeValue(HgPubConst.F_IS_SELF)).equalsIgnoreCase(HgPubConst.SELF)){
				showWarningMessage("来源于合同的入库验收单不能取消匹配合同");
				return;
			}
			GeneralBillItemVO[] gitems = gbillvo.getItemVOs();
			for(GeneralBillItemVO gitem:gitems){
				if(PuPubVO.getString_TrimZeroLenAsNull(gitem.getCsourcebillbid())==null||!HgPubTool.getString_NullAsTrimZeroLen(gitem.getCsourcetype()).equalsIgnoreCase(ScmConst.PO_Order)){
					showWarningMessage("未匹配合同");
					return;
				}
			}
			
			gbillvo.getHeaderVO().setAttributeValue("cuserid", ClientEnvironment.getInstance().getUser().getPrimaryKey());
			
//			进入后台取消合同匹配服务
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
	 * spf excel导出
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
			showHintMessage("请先查询或录入单据");
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
				showHintMessage("单据未签字,不能导出");
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
			showHintMessage("存货是自采！不予导出！");
		else
		  showHintMessage("导出成功！");
} 
	private int onBoListDerived(int m,String path,GeneralBillVO billvo) throws Exception {
		//int m=0;
		ArrayList<Object> al = new ArrayList<Object>();
		ArrayList<Object> alt = new ArrayList<Object>();
		GeneralBillHeaderVO head = billvo.getHeaderVO();
		al.add(PuPubVO.getString_TrimZeroLenAsNull(head.getAttributeValue("vuserdef17")));// 到货日期
		al.add(head.getAttributeValue("cbizname"));// 采购员
		al.add(head.getAttributeValue("cdptname"));// 采购部门
		al.add(null);// 运输方式
		al.add(head.getAttributeValue("dealname"));// 收货人
		al.add(head.getAttributeValue("vcalbodyname"));// 库存组织
		al.add(head.getAttributeValue("coperatorname"));// 制单人
		al.add(PuPubVO.getString_TrimZeroLenAsNull(head.getAttributeValue("tmaketime")));// 制单时间
		al.add(head.getAttributeValue("vuserdef9"));// 采购合同号
	
		CircularlyAccessibleValueObject[] a = billvo.getChildrenVO();
//		BillModel bm = getBillCardPanel().getBillModel();
		// /**获取当前编辑表体行VO -- item ,注意：不能在初始化时由服务器端传递过来，因为可能是订单转入的单据*/
		int rowcount = a.length;
	//	StringBuffer sb = null;
		String stg = null;
		String pk = null;
		TransletFactory tf = new TransletFactory();
		for (int i = 0; i < rowcount; i++) {
			stg = PuPubVO.getString_TrimZeroLenAsNull(a[i].getAttributeValue("cinventoryid"));
			pk =tf.TFactory1(stg,"采购方式");
			if(!"0001Q110000000000R18".equalsIgnoreCase(pk)){
				continue;
			}
			alt.add(tf.TFactory(PuPubVO.getString_TrimZeroLenAsNull(a[i].getAttributeValue("cinventoryid"))));// 编码
			alt.add(PuPubVO.getString_TrimZeroLenAsNull(head.getAttributeValue("vuserdef17")));//计划到货日期
			alt.add(PuPubVO.getString_TrimZeroLenAsNull(a[i].getAttributeValue("vuserdef18")));//数量
			alt.add(PuPubVO.getString_TrimZeroLenAsNull(a[i].getAttributeValue("vuserdef20")));//合格数量
			String num = PuPubVO.getString_TrimZeroLenAsNull(PuPubVO.getUFDouble_NullAsZero(a[i].getAttributeValue("vuserdef18"))
					.sub(PuPubVO.getUFDouble_NullAsZero(a[i].getAttributeValue("vuserdef20"))));
			alt.add(PuPubVO.getString_TrimZeroLenAsNull(num));//不合格数量
			//alt.add(PuPubVO.getString_TrimZeroLenAsNull(a[i].getAttributeValue("nnotelignum")));//不合格数量
			alt.add("0.0000");//途耗数量
			alt.add(PuPubVO.getString_TrimZeroLenAsNull(a[i].getAttributeValue("vuserdef18")));//累计报检数量
			alt.add(tf.TFactory(PuPubVO.getString_TrimZeroLenAsNull(head.getAttributeValue("cwarehouseid"))));//收货仓库ID
			alt.add(null);//生产日期
			alt.add(null);//失效日期
			alt.add(null);//批次号
			alt.add(a[i].getAttributeValue("vmemo"));
		}
		if(alt.size()==0){
//			showHintMessage("存货是自采！不予导出！");
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
			showHintMessage("单据未签字,不能导出");
			return;
		}
		al.add(getBillCardPanel().getHeadItem("vuserdef17").getValueObject());// 到货日期
		al.add(((UIRefPane) (JComponent) getBillCardPanel().getHeadItem("cbizid").getComponent()).getRefName());// 采购员
		al.add(((UIRefPane) (JComponent) getBillCardPanel().getHeadItem("cdptid").getComponent()).getRefName());// 采购部门
		al.add(null);// 运输方式
		al.add(((UIRefPane) (JComponent) getBillCardPanel().getHeadItem("cbizid").getComponent()).getRefName());// 收货人
		al.add(((UIRefPane) (JComponent) getBillCardPanel().getHeadItem("pk_calbody").getComponent()).getRefName());// 库存组织
		al.add(getBillCardPanel().getTailItem("coperatorname").getValueObject());// 制单人
		al.add(getBillCardPanel().getTailItem("tmaketime").getValueObject());// 制单时间
		al.add(getBillCardPanel().getHeadItem("vuserdef9").getValueObject());// 采购合同号
       // StringBuffer sb = null;
		TransletFactory tf = new TransletFactory();
		int rowcount = getBillCardPanel().getBillModel().getRowCount();
		String stg = null;
		String pk = null;
		for (int i = 0; i < rowcount; i++) {
			
			stg = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(i, "cinventoryid"));
			pk =tf.TFactory1(stg,"采购方式");
			if(!"0001Q110000000000R18".equalsIgnoreCase(pk)){
				rowcount--;
				//int j =i +1;
				//sb.append("第'"+j+"'行存货是自采！不予导出！");
				continue;
			}
			alt.add(getBillCardPanel().getBillModel().getValueAt(i,"cinventorycode"));// 编码
			alt.add(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("vuserdef17").getValueObject()));//计划到货日期
			alt.add(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(i, "vuserdef18")));//数量
			alt.add(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(i, "vuserdef20")));//合格数量
			String num = PuPubVO.getString_TrimZeroLenAsNull(PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel().getValueAt(i, "vuserdef18"))
					.sub(PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel().getValueAt(i, "vuserdef20"))));
			alt.add(PuPubVO.getString_TrimZeroLenAsNull(num));//不合格数量
			alt.add("0.0000");//途耗数量
			alt.add(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(i, "vuserdef18")));// naccumchecknum 累计报检数量
			alt.add(((UIRefPane) (JComponent) getBillCardPanel().getHeadItem("cwarehouseid").getComponent()).getRefName());//收货仓库ID
			alt.add(null);// dproducedate 生产日期
			alt.add(null);// dvaliddate 失效日期
			alt.add(null);//批次号
			alt.add(getBillCardPanel().getBillModel().getValueAt(i, "vmemo"));
		}
		if(alt.size()==0){
			showHintMessage("存货是自采！不予导出！");
			return;
		}
			
		if((al==null||al.size()==0) &&(alt==null||alt.size()==0)){
			showHintMessage("请先查询或录入单据");
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
		
		showHintMessage("导出成功！");
	}
	
	private void ExcelDeriver(ArrayList<Object> al, ArrayList<Object> alt,int num, WritableSheet sheet) throws Exception {
		
			Label label = null;
			label = new Label(0, 0, "到货日期");
			sheet.addCell(label);
			label = new Label(1, 0, (String) al.get(0));
			sheet.addCell(label);
			label = new Label(2, 0, "采购员");
			sheet.addCell(label);
			label = new Label(3, 0, (String) al.get(1));
			sheet.addCell(label);
			label = new Label(4, 0, "采购部门");
			sheet.addCell(label);
			label = new Label(5, 0, (String) al.get(2));
			sheet.addCell(label);
			label = new Label(6, 0, "运输方式");
			sheet.addCell(label);
			label = new Label(7, 0, (String) al.get(3));
			sheet.addCell(label);

			label = new Label(0, 1, "收货人");
			sheet.addCell(label);
			label = new Label(1, 1, (String) al.get(4));
			sheet.addCell(label);
			label = new Label(2, 1, "库存组织");
			sheet.addCell(label);
			label = new Label(3, 1, (String) al.get(5));
			sheet.addCell(label);
			label = new Label(4, 1, "制单人");
			sheet.addCell(label);
			label = new Label(5, 1, (String) al.get(6));
			sheet.addCell(label);
			label = new Label(6, 1, "制单日期");
			sheet.addCell(label);
			label = new Label(7, 1, (String) al.get(7));
			sheet.addCell(label);
			label = new Label(0, 2, "对应采购单号");
			sheet.addCell(label);
			label = new Label(1, 2, (String) al.get(8));
			sheet.addCell(label);

			sheet.addCell(new Label(0, 3, "存货编码"));
			sheet.addCell(new Label(1, 3, "计划到货日期"));
			sheet.addCell(new Label(2, 3, "到货数量"));
			sheet.addCell(new Label(3, 3, "合格数量"));
			sheet.addCell(new Label(4, 3, "不合格数量"));
			sheet.addCell(new Label(5, 3, "途耗数量"));
			sheet.addCell(new Label(6, 3, "报检数量"));
			sheet.addCell(new Label(7, 3, "收货仓库"));
			sheet.addCell(new Label(8, 3, "生产日期"));
			sheet.addCell(new Label(9, 3, "失效日期"));
			sheet.addCell(new Label(10, 3, "批次"));
			sheet.addCell(new Label(11, 3, "备注"));

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
				showHintMessage("当前单据数据为空");
				return;
			}
			if (HgPubConst.purchaseIn_in == HgPubTool
					.getPurchaseInBusiStatus(gbillvo)) {
				showWarningMessage("该单据已经入库,不能合同匹配");
				return;
			}

			if (HgPubConst.purchaseIn_arr == HgPubTool
					.getPurchaseInBusiStatus(gbillvo)) {
				showWarningMessage("该单据没有验收,不能合同匹配");
				return;
			}

			if (HgPubConst.purchaseIn_no == HgPubTool
					.getPurchaseInBusiStatus(gbillvo)) {// 不合格
				showWarningMessage("该单据不合格,不能合同匹配");
				return;
			}

			boolean flag = getPactDlg().initData(gbillvo);
			if (flag) {
				showWarningMessage("存在合同信息，不需要匹配");
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
	 * 验收处理
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-12-8下午06:23:10
	 */

	public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
		String key = e.getKey();
		if (!PuPubVO.getUFBoolean_NullAs(getBillCardPanel().getHeadItem("freplenishflag")
						.getValueObject(), UFBoolean.FALSE).booleanValue()) {
			 if (m_isAdjust) {// true 入库的时候 表体实入数量 货位可编辑
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
				// 日志异常
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
	 * 增行时,检查表头是否有公司等信息, 把公司库存组织仓库带到增行中来
	 * 
	 * @see afterWHEdit, afterCalbodyEdit: 库存组织和仓库编辑后,要将表头的仓库和库存组织带到存在的表体行上
	 * 
	 */
	public void addRowNums(int rownums) {
		super.addRowNums(rownums);
		// v5 lj 集中采购默认新增处理
		setBodyDefaultData(0, rownums);// 表体
		// 表头采购公司自制默认为登陆公司
		// 参照采购单据生成时,此方法会在设置对照数据之前调用,所以这里不区分处理参照和自制
		String pk_corp = getClientEnvironment().getCorporation().getPk_corp();
		getBillCardPanel().setHeadItem(IItemKey.PUR_CORP, pk_corp);
		BillItem it = getBillCardPanel().getHeadItem(IItemKey.PUR_CORP);
		// ((UIRefPane)getBillCardPanel().getHeadItem(IItemKey.PUR_CORP).getComponent()).

		getBillCardPanel().execHeadTailEditFormulas(it);
	}

	/**
	 * 从单据表体行，查找出，是成套件的存货，并重构一个新的表体行VO[] 功能： 参数： 返回： 例外： 日期：(2002-04-18
	 * 11:29:23) 修改日期，修改人，修改原因，注释标志：
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
	 * 从单据表体行，查找出，是成套件的存货，并重构一个新的表体行VO[] 功能： 参数： 返回： 例外： 日期：(2002-04-18
	 * 11:29:23) 修改日期，修改人，修改原因，注释标志：
	 */
	GeneralBillItemVO searchInvKit(GeneralBillItemVO cvos) {
		if (cvos != null) {
			if (cvos.getIsSet() != null && cvos.getIsSet().intValue() == 1)
				return cvos;
		}
		return null;
	}

	/**
	 * 创建者：王乃军 功能：在列表方式下选择一张单据 参数： 单据在alListData中的索引 返回：无 例外： 日期：(2001-11-23
	 * 18:11:18) 修改日期，修改人，修改原因，注释标志：
	 */
	protected void selectBillOnListPanel(int iBillIndex) {
	}

	/**
	 * 此处插入方法说明。 把多张单据VO合并成一张 对采购入库单据，需要重载本方法 创建日期：(2004-3-17 15:35:51)
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
	// // 通过表头获得库存组织，对来源单据类型为采购订单的业务，从表体取库存组织
	// // 对来源单据类型为采购到货的业务，从表头取库存组织
	// boolean bhead = true;
	// if (vos != null && vos.length > 0) {
	// GeneralBillItemVO[] itemVOs = (GeneralBillItemVO[]) vos[0]
	// .getChildrenVO();
	// if (itemVOs != null && itemVOs.length > 0) {
	// sCsourcetype = itemVOs[0].getCsourcetype();
	// // /对来源单据类型为采购订单的业务，从表体取库存组织
	// if (sCsourcetype != null && "21".equalsIgnoreCase(sCsourcetype)) {
	// bhead = false;
	// }
	// }
	//
	// }
	// // 默认检查库存组织按表头的方式来进行
	// GeneralBillVO voRet = handle.combinVo(vos, bhead);
	//
	// return voRet;
	// }
	/**
	 * 此处插入方法说明。 设置参照过来的数据赠品不可以编辑 该方法被退库和入库的参照订单返回数据所引用 创建日期：(2003-10-14
	 * 14:29:30)
	 * 
	 * @param BusiTypeID
	 *            java.lang.String
	 * @param vos
	 *            nc.vo.pub.AggregatedValueObject[]
	 */
	protected void setBillRefResultVO(String sBusiTypeID,
			nc.vo.pub.AggregatedValueObject[] vos) throws Exception {
		// 参照过来的数据赠品不可以编辑
		nc.vo.ic.pub.GenMethod.setFlargessEdit(vos, false);
		nc.vo.ic.pub.GenMethod.processFlargessLine((GeneralBillVO[]) vos);
		// 调父类方法
		super.setBillRefResultVO(sBusiTypeID, vos);

	}

	/**
	 * 创建者：王乃军 功能：根据当前单据的待审状态决定签字/取消签字那个可用 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setBtnStatusSign(boolean bUpdateButtons) {
		// 只在浏览状态下并且界面上有单据时控制
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
			// 已签字，所以设置按钮状态,签字不可用，取消签字可用
			getButtonManager().getButton(ICButtonConst.BTN_SIGN).setEnabled(
					false);
			getButtonManager().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL)
					.setEnabled(true);
			// 不可删、改
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
			// 未签字，所以设置按钮状态,签字可用，取消签字不可用
			// 判断是否已填了数量，因为数量是完整的，所以只要检查第一行就行了。

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

			// 可删、改
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

		} else { // 不可签字操作
			getButtonManager().getButton(ICButtonConst.BTN_SIGN).setEnabled(
					false);
			getButtonManager().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL)
					.setEnabled(false);
			// 可删、改
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
		// 使设置生效
		if (bUpdateButtons)
			updateButtons();

	}

	/**
	 * 覆盖基类方法
	 */
	public void setBillVO(GeneralBillVO bvo, boolean bUpdateBotton,
			boolean bExeFormule) {

		// 集中采购跨公司数据处理
		// v5:更改表头采购员和采购部门,供应商的参照为参照自采购公司
		String purcorp = (String) ((GeneralBillVO) bvo)
				.getHeaderValue(IItemKey.PUR_CORP);
		if (purcorp != null && !purcorp.equals(getEnvironment().getCorpID())) {
			BillItem it = getBillCardPanel().getHeadItem("cbizid");
			if (it != null) {
				// RefFilter.filterPsnByDept(it, purcorp, null);// 加上部门公司条件
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

		// 跨公司批次处理
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
						if (isBatch.intValue() == 0) { // 非批次管理clear batchcode
							voa[i].setVbatchcode(null);
						}
					}
				}
			}
		}

		super.setBillVO(bvo, bUpdateBotton, bExeFormule);
	}

	/**
	 * 创建者：王乃军 功能：抽象方法：设置按钮状态，在setButtonStatus中调用。 参数： 返回： 例外： 日期：(2001-5-9
	 * 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 */
	protected void setButtonsStatus(int iBillMode) {
		// 浏览模式下，有单据并且已经签字才可用
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

		// 生成资产卡片/取消生成资产卡片的按钮状况控制
		if (getM_iCurPanel() == BillMode.Card
				&& iBillMode == BillMode.Browse
				&& m_iBillQty > 0
				&& isSigned() == SIGNED
				&& getM_voBill().getWh().getIsCapitalStor().booleanValue()
				&& !getM_voBill().getHeaderVO().getFreplenishflag()
						.booleanValue()
				&& nc.ui.ic.pub.tools.GenMethod.isProductEnabled(
						getCorpPrimaryKey(), "AIM")) {
			// 已签字、资产仓、无退货标志、资产模块已经启用这些条件都符合，才进行这两个按钮的启用
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

		// //需要重新设置按钮以刷新子类按钮的状态。
		// 这里不能调用覆盖父类方法
		initButtonsData();

		// 判断采购产品模块是否启用
		lTime = System.currentTimeMillis();
		if (nc.ui.ic.pub.tools.GenMethod.isProductEnabled(getCorpPrimaryKey(),
				nc.vo.pub.ProductCode.PROD_PO)) {
			// 只要起用了采购模块，则退库菜单在任何时候都是可用的（杨春林要求）

			if (getM_iMode() == BillMode.Browse) {
				// v5
				getButtonManager().getButton(
						ICButtonConst.BTN_LINE_KD_CALCULATE).setEnabled(false);
				// 在编辑状态下，退库按钮不可用 陈倪娜 2009-08-10
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
	 * 单据配套之后，需要将已配套的标志置回VO中 功能： 参数： 返回： 例外： 日期：(2002-06-03 14:39:46)
	 * 修改日期，修改人，修改原因，注释标志：
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
	 * 单据配套之后，需要将已配套的标志置回VO中 功能： 参数： 返回： 例外： 日期：(2002-06-03 14:39:46)
	 * 修改日期，修改人，修改原因，注释标志：
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
	 * 创建者：zhx 功能：成套件拆分配件的处理方法。 参数：成套件的存货管理ID，成套件数量（用于计算配件数量） 返回： 例外：
	 * 日期：(2001-5-8 19:08:05) 修改日期，修改人，修改原因，注释标志：
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
				nc.vo.scm.pub.SCMEnv.out("该成套件没有配件，请检查数据库...");
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
	 * 此处插入方法说明。 创建日期：(2003-10-28 8:47:55)
	 * 
	 * @param iRow
	 *            int
	 * @param m_voBill
	 *            nc.vo.ic.pub.bill.GeneralBillVO 粘贴行后，设置粘贴行的属性，子类重载使用
	 *            非赠品行的复制行，可以编辑成赠品行 赠品行的复制行，不可以编辑成赠品行
	 */
	public void voBillPastLineSetAttribe(int iRow, GeneralBillVO voBill) {
		Object oTemp = voBill.getItemVOs()[iRow].getAttributeValue("flargess");
		boolean bFlarg = false; // 是否赠品
		if (oTemp != null) {
			UFBoolean ufbflargess = (UFBoolean) oTemp;
			bFlarg = ufbflargess.booleanValue();
		}
		if (!bFlarg) // 非赠品行的复制行，可以编辑成赠品行
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
	 * v5:增行时将表头的公司设置为表体的需求公司和采购公司
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
	 * 仓库和库存组织编辑口,设置表体的集中采购字段, 有则设置,没有则放弃
	 */
	@Override
	public void afterCalbodyEdit(BillEditEvent e) {
		super.afterCalbodyEdit(e);
		setDefaultDataByHead();
	}

	/**
	 * 仓库和库存组织编辑口,设置表体的集中采购字段, 有则设置,没有则放弃
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
			// 查看是否参照上游

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
		// TODO 自动生成方法存根
		return false;
	}

	/**
	 * 表头采公司有数据时，表头业务员应根据采购公司过滤
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
		// 如果表头的采购公司有值，则按照采购公司过滤采购员

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

		// 仓库编辑前需要按照库存组织过滤
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
	 * 创建者：王乃军 功能：存货事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
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
		// TODO 自动生成方法存根

	}

	/**
	 * 创建人：刘家清 创建时间：2009-3-5 上午09:33:22 创建原因：谢阳与开发讨论了一下，初步确定V55修改方案为：
	 * 
	 * 1、库存采购入库单的采购部门、采购员不进行部门、人员权限控制；
	 * 2、库存采购入库单生成下游存货核算采购入库单、调拨入库单、应付的应付单时，如果采购入库单的部门、人员不是当前公司的部门、人员，则清空下游单据的部门、人员。
	 * 
	 * 历史数据不升级，如果有问题，通过专项补丁解决
	 */
	public void filterRef(String sCorpID) {
		super.filterRef(sCorpID);
		nc.ui.pub.bill.BillItem bi = getBillCardPanel().getHeadItem("cbizid");
		RefFilter.filterPsnByDept(bi, null, null);
		bi = getBillCardPanel().getHeadItem("cdptid");
		RefFilter.filterDept(bi, null, null);
	}

	/**
	 * 如果是自制 设置自制标记
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-1-17下午04:10:12
	 * @param flag
	 * @param o
	 */
	public void onAdd(boolean bUpataBotton, GeneralBillVO voBill) {
		bUpataBotton = true;

		super.onAdd(bUpataBotton, null);
		getBillCardPanel().getHeadItem(HgPubConst.F_IS_SELF).setValue(
				HgPubConst.SELF);
		setHeadEditEnableWhenBillIn(true);// 设置表头可编辑元素
		setAfterIn(true);
		setButtonsEnable(false);// 设置质检按钮是否可用
		setButtonEnable(ICButtonConst.BTN_LINE, true);// 设置行操作按钮可用
	}

	/**
	 * 设置表体元素不可编辑（实入数量除外）
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-12-14下午05:10:35
	 * @param flag
	 */
	private void setAdjustFlag(boolean flag) {
		m_isAdjust = flag;
	}

	/**
	 * 设置操作按钮不可用
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-12-15上午10:53:46
	 * @param buttonName
	 * @param flag
	 */
	private void setButtonEnable(String buttonName, boolean flag) {
		getButtonManager().getButtonTree().getButton(buttonName).setEnabled(
				flag);
		this.updateButton(getButtonManager().getButton(buttonName));// 设置操作按钮不可用
	}

	// 设置 鹤岗矿业增加按钮的编辑性
	private void setButtonsEnable(boolean flag) {
		setButtonEnable(HgPuBtnConst.ACPT, flag);// 设置质检按钮可用
		setButtonEnable(HgPuBtnConst.UNACPT, flag);
		setButtonEnable(HgPuBtnConst.UNDEAL, flag);
		setButtonEnable(HgPuBtnConst.WARE, flag);
		setButtonEnable(HgPuBtnConst.MATCH, flag);
//		setButtonEnable(HgPuBtnConst.CANCELMATCH, flag);
	}

	/**
	 * 设置表头元素不可编辑
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-12-14下午05:10:35
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
	 * @说明：（鹤岗矿业）
	 * 2011-4-14下午05:02:26
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
	 * 方法功能描述：根据客商管理档案获得客商的基本档案。
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param pk_cumangid
	 *            客商管理档案的ID
	 * @return 客商基本档案ID
	 *         <p>
	 * @author duy
	 * @time 2007-3-20 上午10:50:26
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
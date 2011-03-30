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
 * TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class AskAndQuoteEventHandler extends
		nc.ui.trade.splitmanage.SplitManageEventHandler {
	private static String bo = "nc.bs.hg.so.pub.HgSoPubBO";
	// 请购单生成询价单新界面
//	private PrayToAskDLG billReferUI = null;
//
//	// 选择供应商新界面
//	private VendorSelectUI vendorSelectUI = null;

	
	// 保存最新选择的供应商
	private VendorVO[] m_vendorVOs = null;

	// 保存最新选择的供应商
	private VendorVO[] m_vendorVOsForSend = null;

	// 保存子表存货数据
	private AskbillItemVO[] m_itemVOs = null;

	// 询报价单查询条件输入对话框
	private SCMQueryConditionDlg m_dlgIqQueryCondition = null;
//	private AskQueryDlg m_askQueryDlg = null; 

	// 状态查询条件
	UICheckBox m_chkFree = null;

	UICheckBox m_chkSend = null;

	UICheckBox m_chkQuote = null;

	UICheckBox m_chkEnd = null;

	// 使用适配器来代替原来的ctrl与delegator，降低耦合性 dengjt
	protected IBillBufferAdapter bufferAdapter;

	// 是否选中查询或刷新按钮
	private boolean isQueryORRefresh = false;

	// 是否列表删除
	//private boolean isListDel = false;

	// Email
	private QuickMail dlgMail = null;

	/* 当前登录操作员有权限的公司[] */
	private String[] saPkCorp = null;

	// 存量查询对话框
	nc.ui.pu.pub.ATPForOneInvMulCorpUI m_atpDlg = null;

	/* 打印控件的入口 */
	private PrintEntry m_printEntry = null;

	private ScmPrintTool printList = null;

	private ScmPrintTool printListForVendor = null;

//	private boolean isAlreadyPrice = false;

	protected javax.swing.JFileChooser m_chooser = null;

	private int state = 0;

	/** 文件选取器 */
	private UIFileChooser m_filechooser = null;

	private UITextField ivjtfDirectory = null;

	/** 当前选择的路径 */
	private String m_currentPath = null;

	/** 当前目录的XLS文件数组 */
	private File[] m_allcurrfiles = null;

	// 当前文件
	private File m_fFilePath = null;

	private Hashtable m_ht = null;

	public UpLoadCtrl m_UploadCtrl;

	private Vector excelTOBill = null;

	//按钮
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
	
	//选择供应商参照
	private UIRefPane refpaneVendor = null;
	
    //多语翻译工具类
	private static NCLangRes m_lanResTool = NCLangRes.getInstance();
	


	/**
	 * @param billUI
	 * @param control
	 */
	public AskAndQuoteEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		// TODO 自动生成构造函数存根
	}

	/**
	 * 进行单据动作的处理, 如果进行单据动作处理需要重载该方法 创建日期：(2004-1-3 18:13:36)
	 */
	protected IBusinessController createBusinessAction() {
		return new AskPuPubBusiAction(getBillUI());
	}

	/**
	 * 实例化界面进行拆分单据VO的处理, 如果进行拆分单据处理需要重载该方法 创建日期：(2004-1-3 18:13:36)
	 */
	protected IBusinessSplit createBusinessSplit() {
		return new PrayToAskBusiSplit();
	}

	public void onActionAppendLine(int iAppendCount) {

		// showHintMessage( CommonConstant.SPACE_MARK + "增加订单行" +
		// CommonConstant.SPACE_MARK ) ;

		if (iAppendCount <= 0) {
			return;
		}
//		int iRow = getBillCardPanelWrapper().getBillCardPanel().getBillModel()
//				.getRowCount() - 1;
		// 增行
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().addLine(
				iAppendCount);

		// 需处理的行
//		int[] iaRow = nc.ui.pu.pub.PuTool.getRowsAfterMultiSelect(iRow,
//				iAppendCount + 1);

		// 开始、结束行
//		int iLen = iaRow.length;
//		int iBeginRow = iaRow[1];
//		int iEndRow = iaRow[iLen - 1];

	}

	public void onActionInsertLines(int iCurRow, int iInsertCount) {

		if (iInsertCount == 0) {
			return;
		}

		// 增行
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().insertLine(
				iCurRow, iInsertCount);

//		int iLen = iInsertCount + 1;
		// 增加的行
//		int[] iaRow = nc.ui.pu.pub.PuTool
//				.getRowsAfterMultiSelect(iCurRow, iLen);

		// 开始、结束行
//		int iBeginRow = iaRow[0];
//		int iEndRow = iaRow[iLen - 2];

	}

	/**
	 * 按钮m_boCancel点击时执行的动作,如有必要，请覆盖.
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
    //输入提示 
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
		if(getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vfree").isEnabled()){//2009/10/20 田锋涛 - 为自由项提示添加判断条件
			InvAttrCellRenderer ficr = new InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanelWrapper().getBillCardPanel(), voCurr);
		}
		((BillManageUI)getBillUI()).showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH008")/*@res "取消成功"*/);

	}
	/**
	 * 按钮m_boCancelAudit点击时执行的动作,如有必要，请覆盖.
	 */
	protected void onBoCancelAudit() throws Exception {

		AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
		try {
			for (int i = 0; i < modelVo.getChildrenVO().length; i++) {
				if ((!(modelVo.getChildrenVO()[i]
						.getAttributeValue("closeflag") == null))
						&& ("Y".equalsIgnoreCase(modelVo.getChildrenVO()[i].getAttributeValue(
								"closeflag").toString())))
					throw new BusinessException("存在行关闭的表体行无法弃审");
			}
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			if (e instanceof BusinessException)
				throw e;
		}
		super.onBoCancelAudit();

	}

	/**
	 * 按钮m_boEdit点击时执行的动作,如有必要，请覆盖.
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
							"修改", "完成状态的询报价单不能修改");
					return;
				}
			}
		}else{
			billVO = ((BillUIRefCacheForPP) ((AskAndQuoteUI) getBillUI()).getCacheData()).getCurrentCacheVO();
		}
		// 修改选中的询报价单，处于自由、发出、报价状态的询报价单均可以修改，但处于发出、报价状态的询报价单的表头栏目和表体行号、存货编码、自由项、税率、包装方式、数量、到货日期、到货地址不可修改；处于处于完成状态的询报价单不可以修改
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
        //显示单据来源信息
		nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanelWrapper().getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_ASK);
		
		if (ibillStatus != null && ibillStatus.trim() != null) {
			if (ibillStatus.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000340")/* @res "自由" */)) {
				// 询报价单的表头栏目不可编辑
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
				// 无税报价、含税报价、交货期、报价人、报价生效日期、报价失效日期不可编辑
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
							"4004070101", "UPT4004070101-000022")/* @res "发出" */)
					|| ibillStatus
							.equals(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4004070101",
											"UPP4004070101-000050")/* @res "报价" */)) {// 询报价单状态为发出或报价
				// 询报价单的表头栏目不可编辑
				BillItem[] headItems = getBillCardPanelWrapper()
						.getBillCardPanel().getHeadItems();
				if (headItems != null && headItems.length > 0) {
					BillItem headItem = null;
					for (int i = 0; i < headItems.length; i++) {
						headItem = headItems[i];
						headItem.setEnabled(false);
					}
				}
				// 询报价单的表体行号、存货编码、自由项、税率、包装方式、数量、到货日期、到货地址不可编辑
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
				//since V5.3 税率可以修改 modify by donggq
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
							"SCMCOMMON", "UPPSCMCommon-000340")/* @res "自由" */)){
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
		if (getBillUI().getBillOperate() == IBillOperate.OP_REFADD)// 新增保存
		{
			setOperatorID();
		}
        //自由项输入提示功能
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanelWrapper().getBillCardPanel(), getBufferData().getCurrentVO());
		setButtonsStateEdit();
	}
	/**
	 * 按钮m_boEdit点击时执行的动作,如有必要，请覆盖.
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
//							"修改", "完成状态的询报价单不能修改");
//					return;
//				}
//			}
//		}else{
//			billVO = ((BillUIRefCacheForPP) ((AskAndQuoteUI) getBillUI()).getCacheData()).getCurrentCacheVO();
//		}
		// 修改选中的询报价单，处于自由、发出、报价状态的询报价单均可以修改，但处于发出、报价状态的询报价单的表头栏目和表体行号、存货编码、自由项、税率、包装方式、数量、到货日期、到货地址不可修改；处于处于完成状态的询报价单不可以修改
//		String ibillStatus = ((AskbillHeaderVO)billVO.getParentVO()).getIbillstatus();
//			getBillCardPanelWrapper().getBillCardPanel()
//				.getTailItem("ibillstatus").getValue();
		if (ibillStatus == null
				|| (ibillStatus != null && ibillStatus.trim()
						.length() == 0)) {
			ibillStatus = nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000340")/* @res "自由" */;
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
        //显示单据来源信息
		nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanelWrapper().getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_ASK);
		
		if (ibillStatus != null && ibillStatus.trim() != null) {
			if (ibillStatus.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000340")/* @res "自由" */)) {
				// 询报价单的表头栏目不可编辑
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
				// 无税报价、含税报价、交货期、报价人、报价生效日期、报价失效日期不可编辑
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
							"4004070101", "UPT4004070101-000022")/* @res "发出" */)
					|| ibillStatus
							.equals(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4004070101",
											"UPP4004070101-000050")/* @res "报价" */)) {// 询报价单状态为发出或报价
				// 询报价单的表头栏目不可编辑
				BillItem[] headItems = getBillCardPanelWrapper()
						.getBillCardPanel().getHeadItems();
				if (headItems != null && headItems.length > 0) {
					BillItem headItem = null;
					for (int i = 0; i < headItems.length; i++) {
						headItem = headItems[i];
						headItem.setEnabled(false);
					}
				}
				// 询报价单的表体行号、存货编码、自由项、税率、包装方式、数量、到货日期、到货地址不可编辑
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
				//since V5.3 税率可以修改 modify by donggq
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
							"SCMCOMMON", "UPPSCMCommon-000340")/* @res "自由" */)){
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
		if (getBillUI().getBillOperate() == IBillOperate.OP_REFADD)// 新增保存
		{
			setOperatorID();
		}
    //自由项输入提示功能
		if(getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vfree").isEnabled()){//2009/10/20  田锋涛 - 添加判断条件，可编辑时候才给出提示
			nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanelWrapper().getBillCardPanel(), getBufferData().getCurrentVO());
		}
		setButtonsStateEdit();
	}
	/**
	 * 此处插入方法说明。 功能描述:过虑掉空行 作者：汪维敏 输入参数: 返回值: 异常处理: 日期:
	 * 
	 * @throws Exception
	 */
	public void filterNullLine() throws Exception {
		// 存货列值暂存
		Object oTempValueForInv = null;
		Object oTempValueForCVen = null;
		// 表体model
		nc.ui.pub.bill.BillModel bmBill = getBillCardPanelWrapper()
				.getBillCardPanel().getBillModel();
		// 存货列号，效率高一些。
		final int iInvCol = bmBill.getBodyColByKey("cmangid");
		final int iCvenCol = bmBill.getBodyColByKey("cvendormangid");

		// 必须有存货列
		if (bmBill != null && iInvCol >= 0 && iInvCol < bmBill.getColumnCount()) {
			// 行数
			final int iRowCount = getBillCardPanelWrapper().getBillCardPanel()
					.getRowCount();
			// 从后向前删
			for (int line = iRowCount - 1; line >= 0; line--) {
				// 存货未填
				oTempValueForInv = bmBill.getValueAt(line, iInvCol);
				oTempValueForCVen = bmBill.getValueAt(line, iCvenCol);
				if (filterNullLineCondition(oTempValueForInv, oTempValueForCVen))
					// 删行
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
	 * 此处插入方法说明。 功能描述:过虑掉空行 作者：汪维敏 输入参数: 返回值: 异常处理: 日期:
	 * 
	 * @throws Exception
	 */
	public void filterNullLineForAfterEditInv() throws Exception {
		// 存货列值暂存
		Object oTempValueForInv = null;
		Object oTempValueForCVen = null;
		// 表体model
		nc.ui.pub.bill.BillModel bmBill = getBillCardPanelWrapper()
				.getBillCardPanel().getBillModel();
		// 存货列号，效率高一些。
		final int iInvCol = bmBill.getBodyColByKey("cmangid");
		final int iCvenCol = bmBill.getBodyColByKey("cvendormangid");

		// 必须有存货列
		if (bmBill != null && iInvCol >= 0 && iInvCol < bmBill.getColumnCount()) {
			// 行数
			final int iRowCount = getBillCardPanelWrapper().getBillCardPanel()
					.getRowCount();
			// 从后向前删
			for (int line = iRowCount - 2; line >= 0; line--) {
				// 存货未填
				oTempValueForInv = bmBill.getValueAt(line, iInvCol);
				oTempValueForCVen = bmBill.getValueAt(line, iCvenCol);
				if (filterNullLineCondition(oTempValueForInv, oTempValueForCVen))
					// 删行
					getBillCardPanelWrapper().getBillCardPanel().getBillModel()
							.delLine(new int[] { line });
			}
		}
		if (bmBill.getRowCount() <= 0)
			onBoLineAdd();
	}
	/**
	 * 此处插入方法说明。 功能描述:过虑掉空行 作者：汪维敏 输入参数: 返回值: 异常处理: 日期:
	 * 
	 * @throws Exception
	 */
	public void filterNullLineForAfterEditInvNew() throws Exception {
		// 存货列值暂存
		Object oTempValueForInv = null;
		Object oTempValueForCVen = null;
		// 表体model
		nc.ui.pub.bill.BillModel bmBill = getBillCardPanelWrapper()
				.getBillCardPanel().getBillModel();
		// 存货列号，效率高一些。
		final int iInvCol = bmBill.getBodyColByKey("cmangid");
		final int iCvenCol = bmBill.getBodyColByKey("cvendormangid");

		// 必须有存货列
		if (bmBill != null && iInvCol >= 0 && iInvCol < bmBill.getColumnCount()) {
			// 行数
			final int iRowCount = getBillCardPanelWrapper().getBillCardPanel()
					.getRowCount();
			// 从后向前删
			for (int line = iRowCount -1; line >= 0; line--) {
				// 存货未填
				oTempValueForInv = bmBill.getValueAt(line, iInvCol);
				oTempValueForCVen = bmBill.getValueAt(line, iCvenCol);
				if (filterNullLineCondition(oTempValueForInv, oTempValueForCVen))
					// 删行
					getBillCardPanelWrapper().getBillCardPanel().getBillModel()
							.delLine(new int[] { line });
			}
		}
			onBoLineAdd();
	}
	/**
	 * 按钮m_boSave点击时执行的动作,如有必要，请覆盖.
	 */
	protected void onBoSave() throws Exception {
		// 终止编辑
		getBillCardPanelWrapper().getBillCardPanel().stopEditing();
		// 取模板数据
		filterNullLine();
		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(billVO);
        //最大值检查
        AskbillItemMergeVO.validate((AskbillItemMergeVO[])billVO.getChildrenVO());
		convertIBillStatusToBack(billVO);
		//报价变换重新置报价人和报价日期
		boolean isQuotePriceCHG = ((AskAndQuoteUI)getBillUI()).isQuotePriceCHG;
		if(isQuotePriceCHG){
			((AskbillHeaderVO)billVO.getParentVO()).setCquotepsn(getUserId());
			((AskbillHeaderVO)billVO.getParentVO()).setDquotedate(new UFDate(getLogDate()));
		}
		((AskAndQuoteUI)getBillUI()).isQuotePriceCHG = false;
		((AskAndQuoteUI)getBillUI()).nquotepriceBeforeEdit = null;
		((AskAndQuoteUI)getBillUI()).nquotetaxpriceBeforeEdit = null;
		// 进行数据清空
//		Object o = null;
		Vector v = new Vector();
		boolean isSave = true;
		// 判断是否有存盘数据
		if (onBoSaveCondition(billVO)) {
			isSave = false;
			return;
		} else {
			// 检查非空字段
			if (!checkBeforeSave((AskbillMergeVO) billVO)) {
				return;
			}
			//设置备注
			if( getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane ){
				UIRefPane nRefPanel = (UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmemo").getComponent();
				UITextField vMemoField = nRefPanel.getUITextField();
				((AskbillHeaderVO)billVO.getParentVO()).setVmemo(vMemoField.getText());
			}
			if (getBillUI().getBillOperate() == IBillOperate.OP_ADD )//新增保存
			{
			   ((AskbillHeaderVO)billVO.getParentVO()).setBpurchase(new UFBoolean(IIsGenOrder.SELECTED));
			}
			// 制单时间\最后修改时间处理
			if (getBillUI().getBillOperate() == IBillOperate.OP_ADD
					|| getBillUI().getBillOperate() == IBillOperate.OP_REFADD)// 新增保存
			{
				((AskbillHeaderVO) billVO.getParentVO())
						.setTmaketime(getDateForTime());
				((AskbillHeaderVO) billVO.getParentVO())
				.setTlastmaketime(getDateForTime());
			} else if (getBillUI().getBillOperate() == IBillOperate.OP_EDIT)// 修改保存
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
            //自由项输入提示功能
			nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanelWrapper().getBillCardPanel(), null);
      setInfoForConvertForFront(v);
			billVO = AskbillMergeVO.convertVOTOFront(v);
			 //计算自由项
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
				if (refVOs != null && refVOs.size() > 0) {// 有转单
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
				// 新增后操作处理
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
		if(refVOs == null || (refVOs != null && refVOs.size() == 0)){//避免重复调用loadSourceInfoAll
          //显示单据来源信息
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
	        /* 显示备注 */
			if(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane){
				UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmemo").getComponent();
				nRefPanel3.setValue(((AskbillHeaderVO)getBufferData().getCurrentVO().getParentVO()).getVmemo());
			}
        }
		// 设置保存后状态
		setSaveOperateState();
		setButtonsStateBrowse();
		((BillManageUI)getBillUI()).showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH005")/*@res "保存成功"*/);

	}

	private boolean onBoSaveCondition(AggregatedValueObject billVO) {
		return billVO.getParentVO() == null
				|| (billVO.getChildrenVO() == null || (billVO.getChildrenVO() != null && billVO
						.getChildrenVO().length == 0));
	}

	/**
	 * 保存前非空项检查
	 */
	private boolean checkBeforeSave(AskbillMergeVO VO) {
		if (VO == null || (VO != null && VO.getParentVO() == null)) {
			return false;
		}
		 /* 检查单据模板非空项 */
		try {
			nc.ui.pu.pub.PuTool.validateNotNullField(getBillCardPanelWrapper().getBillCardPanel());
		}
	    catch (Exception e) {
	    	PuTool.outException(getBillCardPanelWrapper().getBillCardPanel(), e);
	    	return false;
	    }
		// 表头非空项检查
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
		/* 检查采购组织 */
		if (pk_purorg == null
				|| (pk_purorg != null && pk_purorg.trim().length() == 0)) {
			errorHead.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101",
			"UPP4004070101-000092"));
		}
		/* 检查币种 */
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
				/* 检查行号 */
				if (crownum == null
						|| (crownum != null && crownum.trim()
								.length() == 0)) {
					errorItems.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101",
			"UPP4004070101-000089"));
					
				}
				/* 检查数量 */
//				if (asknum == null
//						|| (asknum != null && asknum.toString().trim().length() == 0)) {
//					errorItems.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101",
//					"UPP4004070101-000090"));
//				}
			} else if (crownum != null
					&& crownum.trim().length() > 0) {
				/* 检查存货 */
				if (cmangid == null
						|| (cmangid != null && cmangid.trim()
								.length() == 0)) {
					errorItems.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101",
					"UPP4004070101-000091"));
				}
				/* 检查数量 */
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
        			messageHead += errorHead.get(i).toString() + " 、";
        		}else{
        			messageHead += errorHead.get(i).toString() + " ";
        		}
        	}
        }
        if(errorItems != null && errorItems.size() > 0){
        	for(int i = 0 ; i <errorItems.size();i ++ ){
        		if(i < errorHead.size() - 1){
        			messageForBody += errorItems.get(i).toString() + " 、";
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
																 * "表头:{0}不能为空!"
																 */;
        }
        if(messageForBody != null && messageForBody.trim().length() > 0){
        	if(errorForDate != null && errorForDate.trim().length() > 0){
        		message += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004070101", "UPP4004070101-000112",
    					null, 
    					new String[] { new Integer(errorRow).toString() , messageForBody,errorForDate });/*
    																 * @res
    																 * "表体:第{0}行{1}不能为空,{2}!"
    																 */;
        	}else{
        	  message += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004070101", "UPP4004070101-000095",
					null, 
					new String[] { new Integer(errorRow).toString() , messageForBody });/*
																 * @res
																 * "表体:第{0}行{1}不能为空!"
																 */;
        	}
        }else if(errorForDate != null && errorForDate.trim().length() > 0){
    		message += nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004070101", "UPP4004070101-000113",
					null, 
					new String[] { new Integer(errorRow).toString() ,errorForDate });/*
																 * @res
																 * "表体:第{0}行{1}!"
																 */;
    	}
        if(message != null && message.trim().length() > 0){
        	 MessageDialog.showWarningDlg((AskAndQuoteUI)getBillUI(),
    		 nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101",
    	 "UPP4004070101-000069")/* @res "单据模板非空项检查" */,
    	                     message);
        	 ((BillManageUI)getBillUI()).showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
     		 "SCMCOMMON", "UPPSCMCommon-000010")/* @res "保存失败" */);
     		 return false;
        }
		return true;
	}

	/**
	 * 单据状态转换
	 */
	private void convertIBillStatusToBack(AggregatedValueObject billVO) {
		// 状态转换(自由、发出、报价)--保存
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
	 * 缺省查询对话框 如果增加常用条件或替换查询模版必须重载该方法 创建日期：(2004-1-3 18:13:36)
	 */
	protected UIDialog createQueryUI() {
		return getM_dlgIqQueryCondition();
//		return getQueryDlg();
	}

	/**
	 * 获取本节点功能节点ID 创建日期：(2001-10-20 17:29:24)
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
	 * 对于单子表的界面处理，必须重载此方法获得数据，同时进行缓存数据 加载 getBufferDataForAsk().clear();
	 * getBufferData().adddVOToBuffer(aVo); //如果列表setListHeadData(queryVos);
	 * //设置单据操作状态 getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
	 */
	protected void onBoQuery() throws Exception {
		// 获取查询条件
//		int rettype = getQueryDlg().showModal();
		int rettype = getM_dlgIqQueryCondition().showModal();
		if (rettype == UIDialog.ID_OK) {
			// 考虑到删除数据
			getBufferData().setCurrentRow(0);
			// 设置更新数据
			isQueryORRefresh = true;
			getAskbillVOsFromDB(-1);
			isQueryORRefresh = false;

			if (((BillManageUI) getBillUI()).isListPanelSelected()) {// 列表
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

					// 列表显示单据处理
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
																	 * "没有查到任何满足条件的数据!"
																	 */);
				}
				setButtonsStateListNormal();
				try {
					((AskAndQuoteUI) getBillUI()).updateButtonUI();
				} catch (Exception e) {
					// TODO 自动生成 catch 块
					SCMEnv.out(e.getMessage());
					throw e;
				}
			} else {// 卡片
				setBillVOToCard();
			}

		}
	}

	/**
	 * 按钮m_boRefresh点击时执行的动作,如有必要，请覆盖.
	 */
	protected void onBoRefresh() throws Exception {
		
		if(getBufferData().getVOBufferSize() == 0){
			return;
		}
        //考虑到删除数据
		getBufferData().setCurrentRow(0);
		// 设置更新数据
		isQueryORRefresh = true;
		getAskbillVOsFromDB(-1);
		isQueryORRefresh = false;
		updateBufferForAsk();
		if (((BillManageUI) getBillUI()).isListPanelSelected()) {// 列表
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
		} else {// 卡片
			setBillVOToCard();
			setButtonsStateBrowse();
		}
	}

	/**
	 * @功能：设置询价单VO
	 * @作者：周晓 创建日期：(2001-6-8 21:24:36)
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
			if (isQueryORRefresh) {// 查询或刷新
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
//			if (isQueryORRefresh) {// 查询或刷新
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
////								 * "您本次业务未输入任何的约束条件,会从本系统提取大量业务数据并进行很长时间的操作,\n可能需要长时间才能得到您希望的业务数据并会导致系统运行效率急剧下降,\n造成系统繁忙.您是否返回指定约束条件?"
////								 */);
////						if (iContinue == UIDialog.ID_CANCEL)
////							return;
////						else if (iContinue == UIDialog.ID_OK) {
////							getQueryDlg().showModal();
////						}
////				}
////				}
			} else {// 当选中首张、上张、下张、末张按钮时
				// 获得数据
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
				if (isQueryORRefresh) {// 查询或刷新
					headerVOs = (AskbillHeaderVO[]) v.get(0);
					if (headerVOs != null && headerVOs.length > 0) {
						for (int i = 0; i < headerVOs.length; i++) {
							// 处理查询结果的第一个有表体的
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
							// 处理查询结果其它没有表体的
							else {
								headerVO = headerVOs[i];
								vvv = new Vector();
								vvv.add(headerVO);
								vvv.add(null);
								vvv.add(null);
								vvv.add(null);
                                setInfoForConvertForFront(vvv);
								askbillMergeVO = AskbillMergeVO.convertVOTOFront(vvv);
                                 //计算自由项
								setVfree(askbillMergeVO);
								vvvv.add(askbillMergeVO);
							}

						}

					}
				} else {// 当选中首张、上张、下张、末张按钮时
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
					 //计算自由项
					setVfree(askbillMergeVO);
					vvvv.add(askbillMergeVO);
				}

			}
			if (vvvv.size() > 0) {
				mergeVOs = new AskbillMergeVO[vvvv.size()];
				vvvv.copyInto(mergeVOs);
				if (isQueryORRefresh) {// 查询或刷新
					getBufferData().clear();
					getBufferData().addVOsToBuffer(mergeVOs);
				} else {// 当选中首张、上张、下张、末张按钮时
					getBufferData().setVOAt(currentRow, mergeVOs[0]);
				}
			} else {
				getBufferData().clear();
			}

			tTime = System.currentTimeMillis() - tTime;
			SCMEnv.out("查询单据时间：" + tTime + " ms!");
		} catch (Exception e) {
			throw e;
		}
			getBufferData().updateView();
	}

	private void setVfree(AskbillMergeVO askbillMergeVO) {
		AskbillItemMergeVO[] itemTempMergeVOs = (AskbillItemMergeVO[])askbillMergeVO.getChildrenVO();
		  // 计算自由项
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
	 * @功能：设置询价单VO
	 * @作者：周晓 创建日期：(2001-6-8 21:24:36)
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
			// 获得数据
			AggregatedValueObject modelVo = getBufferData().getVOByRowNo(
					nCurIndex);

			if (modelVo != null && modelVo.getParentVO() != null) {
				// 表体数据为空才查询
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
			// 组织数据返回
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
				 //计算自由项
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
	 * 用指定的VO数组 <I>resultVOs </I>去更新BillUIBuffer.这个操作会先把Buffer中原有的数据清空。
	 * 如果指定resultVOs为空Buffer将被情况，且CurrentRow被设置为-1 否则CurrentRow设置为第0行
	 * 
	 * @throws Exception
	 */
	protected void updateBufferForAsk() throws Exception // 暂时改成final,确保目前还没有人继承过它
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
							"UPPuifactory-000066")/* @res "没有查到任何满足条件的数据!" */);
		}
	}
	
	/**
	 * 用指定的VO数组 <I>resultVOs </I>去更新BillUIBuffer.这个操作会先把Buffer中原有的数据清空。
	 * 如果指定resultVOs为空Buffer将被情况，且CurrentRow被设置为-1 否则CurrentRow设置为第0行
	 * 
	 * @throws Exception
	 */
	protected void updateBufferForAskListCHG() throws Exception // 暂时改成final,确保目前还没有人继承过它
	{

		if (getBufferData().getVOBufferSize() != 0) {
			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
		} else {
			getBillUI().setListHeadData(null);
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
			getBufferData().setCurrentRow(-1);
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000066")/* @res "没有查到任何满足条件的数据!" */);
		}
	}

	/**
	 * Action按钮点击时执行的动作,如有必要，请覆盖.（当选中首张、上张、下张、末张按钮时）
	 */
	public void onBoActionElse(ButtonObject bo) throws Exception {
		// 获得数据
//		AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
		int intBtn = 0;
		if (bo.getData() != null)
			intBtn = ((ButtonVO) bo.getData()).getBtnNo();
//		beforeOnBoAction(intBtn, modelVo);
		int currentRow = getBufferData().getCurrentRow();
		if (intBtn == 21) {// 首张
			currentRow = 0;
		} else if (intBtn == 22) {// 下张
			currentRow = currentRow + 1;
			if (currentRow >= getBufferData().getVOBufferSize()) {
				return;
			}
		} else if (intBtn == 23) {// 上张
			if (currentRow >= 1) {
				currentRow = currentRow - 1;
			} else {
				return;
			}
		} else if (intBtn == 24) {// 末张
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
		if (((BillManageUI) getBillUI()).isListPanelSelected()) {// 列表
			convertIbillstatusToString();
		} else {// 卡片
			setBillVOToCard();
			setButtonsStateBrowse();
		}
		// 更新列表
		getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
		getBufferData().updateView();
        //显示单据来源信息
		nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanelWrapper().getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_ASK);
	}
	/**
	 * 获取查询对话框实例
	 */
	public SCMQueryConditionDlg getM_dlgIqQueryCondition() {
		if (m_dlgIqQueryCondition == null) {
			m_dlgIqQueryCondition = new SCMQueryConditionDlg();
			m_dlgIqQueryCondition.setShowPrintStatusPanel(false);
			m_dlgIqQueryCondition.setNormalShow(true);
			// 屏幕多公司选择功能
			m_dlgIqQueryCondition.hideUnitButton();
			try {
				m_dlgIqQueryCondition.setTempletID(getCorpId(), getFuncId(), getUserId(), null);
			} catch (Exception e) {
				PuTool.outException(e);
			}
			// 加入常用条件 - UIRadioButton
			m_chkFree = new UICheckBox();
			m_chkFree.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000340")/* @res "自由" */);
			m_chkFree.setBackground(getBillCardPanelWrapper().getBillCardPanel().getBackground());
			m_chkFree.setForeground(java.awt.Color.black);
			m_chkFree.setSize(80, m_chkFree.getHeight());
			m_chkFree.setSelected(true);
			//
			m_chkSend = new UICheckBox();
			m_chkSend.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101", "UPT4004070101-000022")/* @res "发出" */);
			m_chkSend.setBackground(m_chkFree.getBackground());
			m_chkSend.setForeground(m_chkFree.getForeground());
			m_chkSend.setSize(m_chkFree.getSize());

			m_chkQuote = new UICheckBox();
			m_chkQuote.setText(NCLangRes.getInstance().getStrByID("4004070101", "UPP4004070101-000050")/* @res "报价" */);
			m_chkQuote.setBackground(m_chkFree.getBackground());
			m_chkQuote.setForeground(m_chkFree.getForeground());
			m_chkQuote.setSize(m_chkFree.getSize());

			m_chkEnd = new UICheckBox();
			m_chkEnd.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101", "UPP4004070101-000051")/* @res "完成" */);
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
			// 供应商
			UIRefPane refCustcode = new UIRefPane();
			refCustcode.setRefModel(new CustRefModelForAskQuery(getCorpId()));
			// wyf 2002-10-17 modify end
			m_dlgIqQueryCondition.setValueRef("bd_cubasdoc.custcode", refCustcode);
			
			// 存货编码
			UIRefPane refInvcode = new UIRefPane();
			refInvcode.setRefModel(new InvCodeRefModelForAskQue(getCorpId()));
			// wyf 2002-10-17 modify end
			m_dlgIqQueryCondition.setValueRef("bd_invbasdoc.invcode", refInvcode);
			
			/* 业务员 */
			UIRefPane refPrayPsn = new UIRefPane();
			PurPsnRefModel refPsnModel = new PurPsnRefModel(getCorpId());
			refPrayPsn.setRefModel(refPsnModel);
			m_dlgIqQueryCondition.setValueRef("bd_psndoc.psncode", refPrayPsn);
			/* 部门 */
			UIRefPane refPrayDept = new UIRefPane();
			PurDeptRefModel refDeptModel = new PurDeptRefModel();
			refPrayDept.setRefModel(refDeptModel);
			m_dlgIqQueryCondition.setValueRef("bd_deptdoc.deptcode", refPrayDept);
			/* 采购组织 */
			UIRefPane refPurOrg = new UIRefPane();
			PurorgDefaultRefModel refPurOrgModel = new PurorgDefaultRefModel("采购组织");
			refPurOrgModel.setPk_corp(getCorpId());
			refPurOrgModel.addWherePart(" and bd_purorg.ownercorp = '"
					+ _getCorp().getPrimaryKey()
					+ "' and bd_purorg.sealdate is null ");
			refPurOrg.setRefModel(refPurOrgModel);
			refPurOrg.setPk_corp(_getCorp().getPrimaryKey());
			m_dlgIqQueryCondition.setValueRef("bd_purorg.code", refPurOrg);
			// 询价单状态
			String askStatusStr[] = {
					NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000340")/* @res "自由" */,
					NCLangRes.getInstance().getStrByID("4004070101", "UPT4004070101-000008") /* @res "发出" */};
			UIComboBox askStatusBox = new UIComboBox(askStatusStr);
			//v5.1数据权限
		    ArrayList<String> alcorp=new ArrayList<String>();
		    alcorp.add(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
		    m_dlgIqQueryCondition.initCorpRef(IDataPowerForPrice.CORPKEYFORASKBILL,ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),alcorp);   
		    m_dlgIqQueryCondition.setCorpRefs(IDataPowerForPrice.CORPKEYFORASKBILL, AskPubVO.REFKEYS);   
			// 自动翻译
			askStatusBox.setTranslate(true);
			m_dlgIqQueryCondition.setValueRef("po_askbill.ibillstatus", askStatusBox);
			/* 加载自定义项名称 */
			DefSetTool.updateQueryConditionClientUserDef(
					m_dlgIqQueryCondition, getCorpId(), ScmConst.PO_AskBill, "po_askbill.vdef", "po_askbill_bb1.vbdef");
			/* 默认条件 */
			m_dlgIqQueryCondition.setValueRef("po_askbill.daskdate", "日历");
			m_dlgIqQueryCondition.setDefaultValue("po_askbill.daskdate", "po_askbill.daskdate", ClientEnvironment.getInstance().getDate().toString());
			/* 大数据量提示开关 */
			m_dlgIqQueryCondition.setIsWarningWithNoInput(true);
			/* 封存的基础数据能被参照 */
			m_dlgIqQueryCondition.setSealedDataShow(true);
			
			//2009/09/24 田锋涛 v56查询日期新需求
			m_dlgIqQueryCondition.addCurToCurDate("po_askbill.daskdate");
			
		}
		return m_dlgIqQueryCondition;
	}
	/**
	 * 设置Buffer中的TS到当前设置VO。 创建日期：(2004-5-14 18:04:59)
	 * 
	 * @param setVo
	 *            nc.vo.pub.AggregatedValueObject
	 * @exception java.lang.Exception
	 *                异常说明。
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
				// 哈西化缓存中的字表数据
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
	 * 获得子表数据。 创建日期：(2004-3-11 17:44:14)
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
	 * 按钮m_boLineAdd点击时执行的动作,如有必要，请覆盖.
	 */
	protected void onBoLineAdd() throws Exception {
		// super.onBoLineAdd();
		// 增加行之前调用的抽象方法
		getBillCardPanelWrapper().addLine();
		/* 处理行号 */
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
		// 报价相关项目可以编辑
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"cvendorname",false);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"nquoteprice",false);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"nquotetaxprice",false);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"deliverdays",false);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"dvaliddate",false);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"dinvaliddate",false);
	}
	/**
	 * 按钮m_boLineAdd点击时执行的动作,如有必要，请覆盖.
	 */
	protected void onBoLineAddForEasyUse() throws Exception {
		// 增加行之前调用的抽象方法
		getBillCardPanelWrapper().addLine();
		/* 处理行号 */
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
		// 报价相关项目可以编辑
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"cvendorname",false);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"nquoteprice",false);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"nquotetaxprice",false);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"deliverdays",false);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"dvaliddate",false);
		getBillCardPanelWrapper().getBillCardPanel().setCellEditable(rowCount-1,"dinvaliddate",false);
	}

	/**
	 * 按钮m_boLineIns点击时执行的动作,如有必要，请覆盖.
	 */
	protected void onBoLineIns() throws Exception {
		getBillCardPanelWrapper().insertLine();
		/* 处理行号 */
		BillRowNo.insertLineRowNo(getBillCardPanelWrapper().getBillCardPanel(),
		/* nc.vo.scm.pu.BillTypeConst.PO_ASK */"28", "crowno");
    //输入提示 
		AskbillMergeVO voCurr = (AskbillMergeVO) getBillCardPanelWrapper().getBillCardPanel().getBillValueVO(AskbillMergeVO.class.getName(), AskbillHeaderVO.class.getName(), AskbillItemMergeVO.class.getName());
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanelWrapper().getBillCardPanel(), voCurr);

	}

	/**
	 * 按钮m_boLineCopy点击时执行的动作,如有必要，请覆盖.
	 */
	protected void onBoLineCopy() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().copyLine();	
		getBillCardPanelWrapper().copySelectedLines();
	}

	/**
	 * 按钮m_boLinePaste点击时执行的动作,如有必要，请覆盖.
	 */
	protected void onBoLinePaste() throws Exception {
		// 粘贴前对复制的VO做些业务相关的处理
		processCopyedBodyVOsBeforePaste(getBillCardPanelWrapper()
				.getCopyedBodyVOs());
		int iOrgRowCount = getBillCardPanelWrapper().getBillCardPanel()
				.getRowCount();
		int iNextRow = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getSelectedRow();
		getBillCardPanelWrapper().pasteLines();
		// 增加的行数
		int iPastedRowCount = getBillCardPanelWrapper().getBillCardPanel()
				.getRowCount()
				- iOrgRowCount;
		int[] crowNoa = new int[iPastedRowCount];
		for (int i = 0; i < iPastedRowCount; i++) {
			crowNoa[i] = iNextRow + i;
		}
		// 设置行号
		BillRowNo.pasteLineRowNo(getBillCardPanelWrapper().getBillCardPanel(),
				BillTypeConst.PO_ASK, "crowno", iNextRow + iPastedRowCount,
				crowNoa);
        //输入提示 

		AskbillMergeVO voCurr = (AskbillMergeVO) getBillCardPanelWrapper().getBillCardPanel().getBillValueVO(AskbillMergeVO.class.getName(), AskbillHeaderVO.class.getName(), AskbillItemMergeVO.class.getName());
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanelWrapper().getBillCardPanel(), voCurr);
	}
	/**
	 * 按钮m_boLinePaste点击时执行的动作,如有必要，请覆盖.
	 */
	protected void onBoLinePasteToTail() throws Exception {
		if (getBillCardPanelWrapper().getCopyedBodyVOs() == null
				|| getBillCardPanelWrapper().getCopyedBodyVOs().length == 0){
			getBillUI().showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000424")/* "粘贴行到行尾未成功,可能原因：没有拷贝内容或未确定要粘贴的位置" */);
			return ;
		}else{
			getBillUI().showHintMessage("");
		}
		// 粘贴前对复制的VO做些业务相关的处理
		processCopyedBodyVOsBeforePaste(getBillCardPanelWrapper().getCopyedBodyVOs());
		getBillCardPanelWrapper().getBillCardPanel().pasteLineToTail();
			// 单据行号
			BillRowNo.addLineRowNos(getBillCardPanelWrapper().getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_ASK, "crowno",getBillCardPanelWrapper()
					.getCopyedBodyVOs().length);
	    //输入提示 
		AskbillMergeVO voCurr = (AskbillMergeVO) getBillCardPanelWrapper().getBillCardPanel().getBillValueVO(AskbillMergeVO.class.getName(), AskbillHeaderVO.class.getName(), AskbillItemMergeVO.class.getName());
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanelWrapper().getBillCardPanel(), voCurr);
	
	}
	/**
	 * 此处插入方法说明。 创建日期：(2004-1-8 16:33:05)
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
	 * 作者：王印芬 功能：行粘贴 参数：无 返回：无 例外：无 日期：(2001-5-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
	 * 2002-09-20 wyf 恢复对（上层）来源ID及来源行ID的复制 2002-11-25 wyf 对TS赋空 2003-02-27 wyf
	 * 修改为：对所有复制的行进行项目清空及编辑性控制
	 */
//	private void setValueToPastedLines(int iBeginRow, int iEndRow) {
//
//		for (int iRow = iBeginRow; iRow <= iEndRow; iRow++) {
//			// 复制新行缺省值
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
	 * 功能：存量查询 创建：(2002-10-31 19:45:39) 修改：2003-04-21/czp/统一走销售对话框
	 * 单据状态:初始化；询价单浏览；询价单修改；询价单新增；询价单列表
	 */
	private void onQueryInvOnHand() {
		AskbillMergeVO voPara = null;
		AskbillItemMergeVO item = null;
		AskbillItemMergeVO[] items = null;
		/* 卡片 */
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
								"UPP40040701-000082")/* @res "未选取单据,不能查询存量" */);
				return;
			}
			/* 表体信息完整性检查 */
			int[] iSelRows = getBillCardPanelWrapper().getBillCardPanel()
					.getBillTable().getSelectedRows();
			if (iSelRows != null && iSelRows.length > 0) {
				/* 得到用户选取的第一行 */
				item = (AskbillItemMergeVO) getBillCardPanelWrapper()
						.getBillCardPanel().getBillModel()
						.getBodyValueRowVO(iSelRows[0],
								AskbillItemMergeVO.class.getName());
			} else {
				/* 用户未选择时，取单据第一行 */
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
																			 * "公司、存货、制单日期信息不完整,不能查询存量"
																			 */);
					return;
				}
				item = items[0];
			}
			/* 计划执行日期=制单日期 */
			item.setDaskdate((UFDate) voPara.getParentVO().getAttributeValue(
					"daskdate"));
			/* 信息完整性检查 */
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
																			 * "公司、存货、制单日期信息不完整,不能查询存量"
																			 */);
				return;
			}
			/* 组合新VO初始化并调用存量查询对话框 */
			voPara.setChildrenVO(new AskbillItemMergeVO[] { item });
			if (saPkCorp == null) {
				try {
					nc.vo.bd.CorpVO[] vos = nc.ui.sm.user.UserBO_Client
							.queryAllCorpsByUserPK(getUserId());
					if (vos == null || vos.length == 0) {
						SCMEnv.out("未查询到有权限公司，直接返回!");
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
																			 * "获取有权限公司异常"
																			 */,
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("40040101",
													"UPP40040101-000076")/*
																			 * @res
																			 * "获取有权限公司时出现异常(详细信息参见控制台日志)!"
																			 */);
					SCMEnv.out(e.getMessage());
					return;
				}
			}
			getAtpDlg().setPkCorps(saPkCorp);
			getAtpDlg().initData(voPara);
			getAtpDlg().showModal();
		}
		/* 列表 */
		else if (((BillManageUI) getBillUI()).isListPanelSelected()) {
			/* 表头信息完整性检查 */
			// 取模板数据前的处理
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
								"UPP40040701-000082")/* @res "未选取单据,不能查询存量" */);
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
								"UPP40040701-000084")/* @res "未明确公司,不能查询存量" */);
				return;
			}
			/* 表体信息完整性检查 */
			int[] iSelRows = ((BillManageUI) getBillUI()).getBillListWrapper()
					.getBillListPanel().getBodyTable().getSelectedRows();
			if (iSelRows != null && iSelRows.length > 0) {
				/* 得到用户选取的第一行 */
				item = (AskbillItemMergeVO) ((BillManageUI) getBillUI())
						.getBillListWrapper().getBillListPanel()
						.getBodyBillModel().getBodyValueRowVO(iSelRows[0],
								AskbillItemMergeVO.class.getName());
			} else {
				/* 用户未选择时，取单据第一行 */
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
																			 * "公司、存货、制单日期信息不完整,不能查询存量"
																			 */);
					return;
				}
				item = items[0];
			}
			/* 计划执行日期=制单日期 */
			item.setDaskdate(head.getDaskdate());
			/* 信息完整性检查 */
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
																			 * "公司、存货、制单日期信息不完整,不能查询存量"
																			 */);
				return;
			}
			/* 组合新VO初始化并调用存量查询对话框 */
			voPara = new AskbillMergeVO();
			voPara.setParentVO(head);
			voPara.setChildrenVO(new AskbillItemMergeVO[] { item });
			if (saPkCorp == null) {
				try {
					nc.vo.bd.CorpVO[] vos = nc.ui.sm.user.UserBO_Client
							.queryAllCorpsByUserPK(getUserId());
					if (vos == null || vos.length == 0) {
						SCMEnv.out("未查询到有权限公司，直接返回!");
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
																			 * "获取有权限公司异常"
																			 */,
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("40040101",
													"UPP40040101-000076")/*
																			 * @res
																			 * "获取有权限公司时出现异常(详细信息参见控制台日志)!"
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
	 * 功能 ：文管管理 适用单据状态：“询价单浏览”、“询价单列表”
	 */
	private void onDocument() {
		String[] strPks = null;
		String[] strCodes = null;
		if (!(((!((BillManageUI) getBillUI()).isListPanelSelected()) && (getBillUI()
				.getBillOperate() == IBillOperate.OP_NOTEDIT)) || ((((BillManageUI) getBillUI())
				.isListPanelSelected())))) {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
							"UPP40040701-000069")/* @res "获取不到单据号,文档管理功能不可用" */);
			return;
		}
		// 卡片
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
		// 列表
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
		// 调用文档管理对话框
		nc.ui.scm.file.DocumentManager.showDM((BillManageUI) getBillUI(),
				ScmConst.PO_AskBill,strPks);
	}

	/**
	 * 功能：获取存量查询对话框
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
	 * 其它按钮动作的事件处理，比如(导入等)
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
			// SCMEnv.out("本热键暂未处理");
			break;
		}

		}
	}
	/**
	 * 单据逐级联查
	 */
	private void onLnkQuery() {
		AskbillMergeVO vo = null;
		try {
			vo = (AskbillMergeVO)getBillUI().getVOFromUI();
		} catch (Exception e) {
			// TODO 自动生成 catch 块
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
	 * 选择供应商
	 * 
	 * @throws Exception
	 */
	private void onVendorSelect() throws Exception {
		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		AskbillHeaderVO headVO = (AskbillHeaderVO) billVO.getParentVO();
		//设置备注
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
		// 非空项检查
		String ibillStatusTemp = headVO.getIbillstatus();
		if (ibillStatusTemp.equals(StatusConvert
				.getStatusIndexNum().get(new Integer(IAskBillStatus.FREE))
				.toString())) {
			// 表头非空项检查
			String pk_purorg = headVO.getPk_purorg();
			String ccurrencytypeid = headVO.getCcurrencytypeid();
			/* 检查采购组织 */
			if (pk_purorg == null
					|| (pk_purorg != null && pk_purorg.trim()
							.length() == 0)) {
				MessageDialog
						.showWarningDlg(
								(AskAndQuoteUI) getBillUI(),
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"4004070101", "UPP4004070101-000069")/*
																				 * @res
																				 * "单据模板非空项检查"
																				 */,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"4004070101", "UPP4004070101-000072")/*
																				 * @res
																				 * "表头:采购组织不能为空"
																				 */);
				((BillManageUI) getBillUI()).showHintMessage(nc.ui.ml.NCLangRes
						.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000010")/* @res "保存失败" */);
				return;
			}
			/* 检查币种 */
			if (ccurrencytypeid == null
					|| (ccurrencytypeid != null && ccurrencytypeid
							.trim().length() == 0)) {
				MessageDialog
						.showWarningDlg(
								(AskAndQuoteUI) getBillUI(),
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"4004070101", "UPP4004070101-000069")/*
																				 * @res
																				 * "单据模板非空项检查"
																				 */,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"4004070101", "UPP4004070101-000073")/*
																				 * @res
																				 * "表头:币种不能为空"
																				 */);
				((BillManageUI) getBillUI()).showHintMessage(nc.ui.ml.NCLangRes
						.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000010")/* @res "保存失败" */);
				return;
			}
			// 表体非空项检查
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
					// 数量不是非空项 since 5.6 modify by donggq
//					if (nasknumTemp == null
//							|| (nasknumTemp != null && nasknumTemp.toString()
//									.trim().length() == 0)) {
//						MessageDialog.showWarningDlg(getBillUI(),
//								nc.ui.ml.NCLangRes.getInstance().getStrByID(
//										"SCMCOMMON", "UPPSCMCommon-000270")/*
//																			 * @res
//																			 * "提示"
//																			 */,
//																			 nc.ui.ml.NCLangRes.getInstance()
//																				.getStrByID("4004070101",
//																						"UPP4004070101-000095", null,
//																						new String[] { (i + 1) + "",nc.ui.ml.NCLangRes.getInstance().getStrByID(
//																								"4004070101", "UPP4004070101-000090") }))/*
//																														 * @res
//																														 * "{0}行：数量不能为空！"
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
																	 * "提示"
																	 */,nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"4004070101", "UPP4004070101-000096")/*
																				 * @res
																				 * "表体应至少有一行存货，否则无法选择供应商!"
																				 */);
				return;
			}
		}
		if (m_itemVOs == null
				|| getBillUI().getBillOperate() == IBillOperate.OP_NOTEDIT) {
			// 从界面取得子表数据
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
		// 取得已经报价/发出的供应商
		Vector vendorSelected = new Vector();
		for (int i = 0; i < itemMergeVOs.length; i++) {
			itemMergeVO = itemMergeVOs[i];
			vmangid = itemMergeVO.getCvendormangid();
			nquoteprice = itemMergeVO.getNquoteprice();
			nquotetaxprice = itemMergeVO.getNquotetaxprice();
			specialNum = itemMergeVO.getSpecialNum();
			if (itemMergeVO != null
					&& (nquoteprice != null || nquotetaxprice != null)) {// 有报价
				if (specialNum != null && vmangid != null
						&& vmangid.trim() != null) {// 新增时
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
					&& nquotetaxprice == null) {// 无报价

				if (specialNum != null && vmangid != null
						&& vmangid.trim() != null) {// 新增时
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

		// 显示选择供应商界面
		String[] vmangIDs = null;
		if(vendorSelected.size() > 0){
			vmangIDs = new String[vendorSelected.size()];
			vendorSelected.copyInto(vmangIDs);
		}
        //显示处理设置为显示供应商参照 V51根据蓝力项目需求修改
		getVendorRef();
		refpaneVendor.getRefModel().setUserParameter(getCorpId());
		
		if(vmangIDs != null && vmangIDs.length > 0){
		 refpaneVendor.setPKs(vmangIDs);
		}else{
	     refpaneVendor.setPKs(null);	
		}
   	    refpaneVendor.onButtonClicked();
   	    
   	//不点确定直接返回
   	if(refpaneVendor.getReturnButtonCode() != UFRefManage.ID_OK)
   		return;
		
		VendorVO[] vendorVOs = null;
		VendorVO vendorVO = new VendorVO();
		int vendorVOsLength = 0;
		//完成状态不做后续操作 直接返回
		if (ibillStatusTemp.equals(StatusConvert
				.getStatusIndexNum().get(new Integer(IAskBillStatus.CONFIRM))
				.toString())) {
			return;
		}
		
		// 取得选择供应商结果数据
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
		if (m_vendorVOs != null && m_vendorVOs.length > 0) {// 选择了供应商

			if (pricedVendorH != null && pricedVendorH.size() > 0) {// 已经有报价信息
				ibillStatus = StatusConvert.getStatusIndexNum()
						.get(new Integer(IAskBillStatus.QUOTED)).toString();
				((AskbillHeaderVO) billVO.getParentVO())
						.setIbillstatus(ibillStatus);
			}
			else {// 没有报价信息
				ibillStatus = StatusConvert.getStatusIndexNum()
						.get(new Integer(IAskBillStatus.SENDING)).toString();
				((AskbillHeaderVO) billVO.getParentVO())
						.setIbillstatus(ibillStatus);
			}
		} else {// 未选择了供应商
			ibillStatus = StatusConvert.getStatusIndexNum()
					.get(new Integer(IAskBillStatus.FREE)).toString();
			((AskbillHeaderVO) billVO.getParentVO())
					.setIbillstatus(ibillStatus);
		}

		
		// 组合子表和子子表的数据
		int[] iaGivenSetRow = null;
		int lastRowNum = 0;
		if (m_itemVOs != null && m_itemVOs.length > 0) {

			for (int i = 0; i < m_itemVOs.length; i++) {
				itemVO = m_itemVOs[i];
				specialNum = itemVO.getSpecialNum();
				if (m_vendorVOs != null && m_vendorVOs.length > 0) {// 有供应商数据
					for (int j = 0; j < m_vendorVOs.length; j++) {
						vendorVO = m_vendorVOs[j];
						vmangid = vendorVO.getCumandoc();
						itemMergeVO = new AskbillItemMergeVO();
						itemMergeVO.setCaskbill_bid(itemVO.getCaskbill_bid());
						itemMergeVO.setCaskbillid(itemVO.getCaskbillid());
						if (j == 0) {// 对应每个存货的第一行设置的信息
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
						//since 5.3 税率、自定义项移至子子表 
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
								&& vmangidH.containsKey(vmangid)) {// 此供应商已报价
							if (specialNum != null && specialNum.trim() != null) {// 新增时
								vendorTempH = (Hashtable) vendorH
										.get(specialNum);
								if (vendorTempH != null) {
									itemMergeVOTemp = (AskbillItemMergeVO) vendorTempH
											.get(vmangid);
								}
								if (itemMergeVOTemp != null) {// 有报价数据
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
									// 如果报价生效日期为空，则设置默认值为维护报价时的登录日期
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
								} else {// 无报价数据
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
						} else {// 此供应商未报价
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
							// 如果报价生效日期为空，则设置默认值为维护报价时的登录日期
							itemMergeVO
									.setDvaliddate(((AskAndQuoteUI) getBillUI())
											.getLogDate());
						}
						// 设置特殊序号,指为头次选择供应商时指定存货与供应商的位置用
						itemMergeVO.setSpecialNum(itemVO.getSpecialNum());
						if (itemMergeVO != null) {
							retV.add(itemMergeVO);
						}
					}
				} else {// 无供应商数据
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

			// 设置行号
			int lastRowNumTemp = lastRowNum;
			if (m_vendorVOs != null && m_vendorVOs.length > 0) {// 有供应商数据
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
		// 计算自由项
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
		// 返回
		billVO.setChildrenVO(itemMergeVOs);
		getBillCardPanelWrapper().getBillCardPanel().setBillValueVO(billVO);
		/* 显示备注 */
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
		// 只要选择了一次供应商,存货就不能再进行编辑
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
			// 报价相关项目可以编辑
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
		} else {// 没有选择任何供应商
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
			// 报价相关项目可以编辑
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
		// 表头可编辑性
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
				"4004070101", "UPP4004070101-000051")/* @res "完成" */;
		// 非编辑并且非完成状态选择供应商后变为编辑状态
		if (getBillUI().getBillOperate() == IBillOperate.OP_NOTEDIT
				&& ibillStatus != null
				&& ibillStatus.trim().length() > 0
				&& !ibillStatus.equals(finish) 
//				&& m_vendorVOs != null && m_vendorVOs.length > 0
				) {
			onBoEditForVendorSelected(ibillStatus);
		}else{
			 //自由项输入提示功能
			nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanelWrapper().getBillCardPanel(), billVO);
			 //显示单据来源信息
			nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanelWrapper().getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_ASK);
		}
		// 清空缓存
		m_itemVOs = null;

		// 选择供应商后发出，先保存再发出  since 5.6 选择供应商后不发送邮件
//		if (m_vendorVOsForSend != null && m_vendorVOsForSend.length > 0) {
//			if(isSending()){
//			 onBoSave();
//			 onSendFromSelectVendor(m_vendorVOsForSend);
//			}
//		}
		line = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IBillButton.Line);

		// 选择供应商后行操作按钮不可用
		if (m_vendorVOs != null && m_vendorVOs.length > 0) {
			line.setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().setBBodyMenuShow(false);
			// 询报价单的表头栏目不可编辑
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
			// 询报价单的表头栏目不可编辑
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
			// 询报价单的表头栏目不可编辑
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
			// TODO 自动生成 catch 块
			SCMEnv.out(e.getMessage());
			PuTool.outException(e);
		}
	}
	/**
	 * 功能描述:点击选择供应商按钮需要使用到的供应商参照
	 */
	private UIRefPane getVendorRef() {
		if(refpaneVendor == null){
  		refpaneVendor = new UIRefPane("供应商档案");
  		refpaneVendor.setTreeGridNodeMultiSelected(true);
  		refpaneVendor.setMultiSelectedEnabled(true);
  		refpaneVendor.setFocusable(true);
  		refpaneVendor.setCacheEnabled(false);
  		refpaneVendor.getRefModel().setIsDynamicCol(true);
  		refpaneVendor.getRefModel().setDynamicColClassName("nc.ui.pp.ask.RefDynamicForSelectVendors");
  		// xy & zhounl, since v56, 与价格审批单统一,　暂不支持散户
  		refpaneVendor.getRefModel().addWherePart(" and coalesce(bd_cubasdoc.freecustflag, 'N') = 'N' ") ;
		}
		return refpaneVendor;
	}
	/**
	 * 功能描述:是否进行发出操作
	 */
	public boolean isSending() {
		// 正在编辑单据时退出提示
			int iRet = MessageDialog
					.showYesNoDlg(getBillCardPanelWrapper().getBillCardPanel(), m_lanResTool.getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000270")/* "提示" */,
							m_lanResTool.getStrByID("4004070101", "UPP4004070101-000114")/*
																		 * @res
																		 * "是否将询报价单发送给供应商？"
																		 */);
			// 保存成功后才退出
			if (iRet == MessageDialog.ID_YES) {
				return true;
			}
			// 退出
			else if (iRet == MessageDialog.ID_NO) {
				return false;
			}
			// 取消关闭
			else {
				return false;
			}
	}
	/**
	 * @功能：给供应商发送内容Email
	 * @作者：周晓 创建日期：(2001-9-3 16:27:21) 2002-10-17 wyf
	 *         修改供应商参照为树状态，该参照不能设置参数refCustcode.setIsCustomDefined(true)，否则仍出平面参照
	 */
	private void onSend() throws Exception {
		String strVendors = nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"4004070101", "UPP4004070101-000045")/* @res "供应商列表:" */;
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
			// TODO 自动生成 catch 块
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
		// 确定返回类型
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
				// TODO 自动生成 catch 块
				SCMEnv.out(e1.getMessage());
				PuTool.outException(e1);
			}

			for (int i = 0; i < refVendors.getRefModel().getRefNameValues().length; i++) {
				// 供应商名称列表
				if (strVendors.indexOf(":") + 1 < strVendors.length()) {
					strVendors += nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000000")/* @res "、" */;
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
																			 * "请先查询或录入单据。"
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
																			 * "请先选中单据后再试。"
																			 */);
					return;
				}

				// 需要导出的数据存入VO
//				Hashtable result = new Hashtable();
				v = new Vector();
//				nc.vo.pp.ask.ExcelFileVO vo = null;
				// 得到需要导出的VO
				v = saveOutputVOsForSend(vendorV, vendorMangIdsV,
						new AskbillMergeVO[] { currVO });

				// 已经得到vo，然后导出vo
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

						// 调用导出接口
						// 文件名称规则：Askbill+公司PK+单据号+"_"+供应商".xls"

						sFilePath = sFilePathDir + "\\" + "询价单号("
								+ vos[0].getVaskbillcode() + ")_供应商("
								+ vos[0].getCvendorname() + ").xls";
						erc = new nc.ui.pp.pub.ExcelReadCtrl();
						//try {
							erc.setVOToExcel(vos, sFilePath);
//						} catch (IOException e) {
//							// TODO 自动生成 catch 块
//							SCMEnv.out(e.getMessage());
//							PuTool.outException(e);
//						}
						da = new File(sFilePath);
						long fileLength = da.length();
						
						byte[] buff = null;
						try {
							dsa = new FileInputStream(sFilePath);
							buff = new byte[(int) fileLength];
							dsa.read(buff);// 时间消耗大约为0.03秒
						} catch (FileNotFoundException e) {
							// TODO 自动生成 catch 块
							SCMEnv.out(e.getMessage());
							PuTool.outException(e);
						} catch (IOException e) {
							// TODO 自动生成 catch 块
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
								dlg=new AccountManageDialog((AskAndQuoteUI)getBillUI(),nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm","UPPsmcomm-000053")/*@res "账号管理对话框"*/,currentUser);
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
							// TODO 自动生成 catch 块
							SCMEnv.out(e.getMessage());
							PuTool.outException(e);
						}
						mailInfo.setReceiver((String)resultH.get(vos[0].getCvendormangid()));
						mailInfo.setCc("");
						mailInfo.setSubject("询报价单");
						mailInfo.setMessage("");
						mailInfo.setCurrentUser(currentUser);
						mailInfo.setUserName(m_MailInfo.getUserName());
						mailInfo.setPassword(m_MailInfo.getPassword());
						mailInfo.setPopHost(m_MailInfo.getPopHost());
						mailInfo.setSmtpHost(m_MailInfo.getSmtpHost());
						mailInfo.setSender(m_MailInfo.getSender());

						 newAttachment = new
						 nc.vo.pub.mail.AttachmentVO();
						 newAttachment.setFileName("询报价单号(" +
								 vos[0].getVaskbillcode()
								 +")_供应商("+vos[0].getCvendorname()+ ").xls");
						 newAttachment.setFileContent(buff);
						 mailInfo.setAttachments(new AttachmentVO[]
						 {newAttachment});
						mailTool = new SendMailDialog(
								(AskAndQuoteUI) getBillUI(), "询报价单发出", mailInfo);
						mailTool.showDialog(mailInfo);
						getBillUI()
								.showHintMessage(
										nc.ui.ml.NCLangRes.getInstance()
												.getStrByID("40040701",
														"UPP40040701-000062")/*
																				 * @res
																				 * "导出完成"
																				 */);
					}
					
					} catch (IOException e) {
						// TODO 自动生成 catch 块
						SCMEnv.out(e.getMessage());
						PuTool.outException(e);
					}finally{
						dsa.close();
					}
				}

				// 生成多张单据
				cvendormangid = null;
//				cloneVOs = new AskbillMergeVO[refVendors.getRefModel()
//						.getPkValues().length];
				// 置发出状态
				((AskbillHeaderVO) currVO.getParentVO())
						.setIbillstatus(StatusConvert
								.getStatusIndexNum().get(
										new Integer(IAskBillStatus.SENDING))
								.toString());
				getBufferData().setCurrentVO(currVO);
				try {
					updateBufferForAsk();
				} catch (Exception e) {
					// TODO 自动生成 catch 块
					SCMEnv.out(e.getMessage());
					PuTool.outException(e);
				}
				getBillCardPanelWrapper().getBillCardPanel().setBillValueVO(
						currVO);
			} else {
//				stop = true;
				strVendors += nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4004070101", "UPP4004070101-000046")/* @res "没有选取" */;
			}
		} else {
//			stop = true;
			dlgMail = null;
		}
	}

	/**
	 * @throws IOException 
	 * @功能：给供应商发送内容Email
	 * @作者：周晓 创建日期：(2001-9-3 16:27:21) 2002-10-17 wyf
	 *         修改供应商参照为树状态，该参照不能设置参数refCustcode.setIsCustomDefined(true)，否则仍出平面参照
	 */
//	private void onSendFromSelectVendor(VendorVO[] vendorVOs) throws IOException {
//		String strVendors = nc.ui.ml.NCLangRes.getInstance().getStrByID(
//				"4004070101", "UPP4004070101-000045")/* @res "供应商列表:" */;
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
//			// TODO 自动生成 catch 块
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
//				// 供应商名称列表
//				if (strVendors.indexOf(":") + 1 < strVendors.length()) {
//					strVendors += nc.ui.ml.NCLangRes.getInstance().getStrByID(
//							"SCMCOMMON", "UPPSCMCommon-000000")/* @res "、" */;
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
//																			 * "请先查询或录入单据。"
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
//																			 * "请先选中单据后再试。"
//																			 */);
//					return;
//				}
//
//				// 需要导出的数据存入VO
//				String[] pkValues = new String[vendorMangIdsV.size()];
//				vendorMangIdsV.copyInto(pkValues);
//				try {
//					resultH = AskHelper.queryEmailAddrForAskSend(pkValues);
//				} catch (Exception e1) {
//					// TODO 自动生成 catch 块
//					SCMEnv.out(e1.getMessage());
//					PuTool.outException(e1);
//				}
////				Hashtable result = new Hashtable();
//				Vector v = new Vector();
////				nc.vo.pp.ask.ExcelFileVO vo = null;
//				// 得到需要导出的VO
//				v = saveOutputVOsForSend(vendorV, vendorMangIdsV,
//						new AskbillMergeVO[] { currVO });
//
//				// 已经得到vo，然后导出vo
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
//						// 调用导出接口
//						// 文件名称规则：Askbill+公司PK+单据号+"_"+供应商".xls"
//
//						sFilePath = sFilePathDir + "\\" + "询价单号("
//								+ vos[0].getVaskbillcode() + ")_供应商("
//								+ vos[0].getCvendorname() + ").xls";
//						erc = new nc.ui.pp.pub.ExcelReadCtrl();
//						try {
//							erc.setVOToExcel(vos, sFilePath);
//						} catch (IOException e) {
//							// TODO 自动生成 catch 块
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
//							dsa.read(buff);// 时间消耗大约为0.03秒
//						} catch (FileNotFoundException e) {
//							// TODO 自动生成 catch 块
////							SCMEnv.out(e.getMessage());
////							PuTool.outException(e);
//							if(e.getMessage().indexOf("*") != -1){
//								getBillUI().showErrorMessage("单据号中含有“*”，不能创建询价单excel文件，发送供应商邮件失败。");
//							}else {
//								getBillUI().showErrorMessage(e.getMessage());
//							}
//						} catch (IOException e) {
//							// TODO 自动生成 catch 块
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
//								dlg=new AccountManageDialog((AskAndQuoteUI)getBillUI(),nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm","UPPsmcomm-000053")/*@res "账号管理对话框"*/,currentUser);
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
//							// TODO 自动生成 catch 块
//							SCMEnv.out(e.getMessage());
//							PuTool.outException(e);
//						}
//						mailInfo.setReceiver((String)resultH.get(vos[0].getCvendormangid()));
//						mailInfo.setCc("");
//						mailInfo.setSubject("询报价单");
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
//						 newAttachment.setFileName("询报价单号(" +
//						 vos[0].getVaskbillcode()
//						 +")_供应商("+vos[0].getCvendorname()+ ").xls");
//						 newAttachment.setFileContent(buff);
//						 mailInfo.setAttachments(new AttachmentVO[]
//						 {newAttachment});
//						 mailTool = new SendMailDialog(
//								(AskAndQuoteUI) getBillUI(), "询报价单发出", mailInfo);
//						mailTool.showDialog(mailInfo);
//						getBillUI()
//								.showHintMessage(
//										nc.ui.ml.NCLangRes.getInstance()
//												.getStrByID("40040701",
//														"UPP40040701-000062")/*
//																				 * @res
//																				 * "导出完成"
//																				 */);
//					}
//				}
//				// 置发出状态
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
//					// TODO 自动生成 catch 块
//					SCMEnv.out(e.getMessage());
//					PuTool.outException(e);
//				}
////				getBillCardPanelWrapper().getBillCardPanel().setBillValueVO(
////						currVO);
//			} else {
////				stop = true;
//				strVendors += nc.ui.ml.NCLangRes.getInstance().getStrByID(
//						"4004070101", "UPP4004070101-000046")/* @res "没有选取" */;
//			}
//		} else {
////			stop = true;
//		  
//			dlgMail = null;
//		}
//		
//	}

	/**
	 * @功能：获取Email对话框
	 * @作者：周晓 创建日期：(2001-9-3 16:29:50)
	 */
	private QuickMail getEmailDlg() {
		if (dlgMail == null) {
			dlgMail = new QuickMail((BillManageUI) getBillUI(), "quickmail");
		}
		return dlgMail;
	}

	/**
	 * @功能：接收Email
	 * @作者：周晓 创建日期：(2001-9-3 16:27:41)
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
	 * 获取打印入口
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
	 * @功能：接收Email
	 * @作者：周晓 创建日期：(2001-9-3 16:27:41)
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
	 * @功能：接收Email
	 * @作者：周晓 创建日期：(2001-9-3 16:27:41)
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
	 * @功能：接收Email
	 * @作者：周晓 创建日期：(2001-9-3 16:27:41)
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
				// TODO 自动生成 catch 块
				SCMEnv.out(e.getMessage());
				PuTool.outException(e);
			}
		}
		printList.print();
	}

	/**
	 * @功能：接收Email
	 * @作者：周晓 创建日期：(2001-9-3 16:27:41)
	 * @param: <|>
	 * @return:
	 * @throws Exception
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 */
	private void onPrintByVendor() throws Exception {
		//暂时保存原来的VO
//		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		//按供应商分单打印的VOs
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
				// TODO 自动生成 catch 块
				SCMEnv.out(e.getMessage());
				PuTool.outException(e);
			} catch (Exception e) {
				// TODO 自动生成 catch 块
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
		// 全部选中询价单
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

		// 查询未被浏览过的单据体
		try {
			allvos = getRefreshedVOs(allvos);
			nc.ui.scm.pub.FreeVOParse freeParse = null;
			String[] saKey = null;
			for (int k = 0; k < allvos.length; k++) {
				vo = new AskbillMergeVO();
				vo = allvos[k];
				// 加载自由项
				freeParse = new nc.ui.scm.pub.FreeVOParse();
				
				freeParse.setFreeVO(vo.getChildrenVO(), "vfree", "vfree",
						"cbaseid", "cmangid", true);
				getBillCardPanelWrapper().getBillCardPanel().setBillValueVO(vo);

				// 处理基础数据因冻结等变更而不能显示问题
				PpTool.loadBDData(getBillCardPanelWrapper().getBillCardPanel());
				// 处理自定义项
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
				// 表头
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
							"UPP40040701-000055")/* @res "发现错误:" */
							+ b.getMessage());
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
							"UPP40040701-000056")/* @res "发现未知错误" */);
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
	 * @功能：接收Email
	 * @作者：周晓 创建日期：(2001-9-3 16:27:41)
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
				// TODO 自动生成 catch 块
				SCMEnv.out(e.getMessage());
				PuTool.outException(e);
			}
		}
		printList.preview();
	}

	/**
	 * @功能：返回请购单生成询价单界面
	 * @作者：周晓 创建日期：(2001-8-11 14:56:58)
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
	 * @功能：获取公司ID
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
	 * @功能：获取登陆日期
	 */
	private String getLogDate() {
		UFDate logDate = nc.ui.pub.ClientEnvironment.getInstance().getDate();
		return logDate.toString();
	}

	/**
	 * @功能：获取服务器时间
	 */
	private String getDateForTime() {
		UFDateTime serverTime = new UFDateTime(new Date());
		;
		return serverTime.toString();
	}
	/**
	 * @功能：获取登陆人ID
	 */
	public String getUserId() {
		String userid = nc.ui.pub.ClientEnvironment.getInstance().getUser()
				.getPrimaryKey();
		return userid;
	}

	/**
	 * 参照请购单生成询报价单处理
	 */
	private void onGenFromPrayBill() {
//		PrayToAskDLG billReferUI = getNewGeneratePanel();
//		// ******2003-05-03***放入查询条件的DLG
//		billReferUI.onQuery();
//		if (billReferUI.getIsIDOK()) {
//			// 加载模版
//			billReferUI.addBillUI();
//			// 加载数据
//			billReferUI.loadHeadData();艘日
//			billReferUI.showModal();
//		}
////		AskbillVO[] newVOs = null;
////		newVOs = billReferUI.getM_askbillVOs();
//		returnBack();
		
		nc.ui.pub.ButtonObject bo = new nc.ui.pub.ButtonObject("询报价转单");
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
	 * @功能：从请购单界面生成询价单后返回 把新生成的询价单直接插入数据库同时追加到前端
	 * @作者：周晓 创建日期：(2001-8-15 16:12:13)
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
//                     //计算自由项
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
//						.out("由请购单生成的询报价单写入数据库时出错[nc.ui.pp.ask.InquireUI.onSend()->AskbillBO_Client.insertVOsFromPrayMy(newVOs,getClientEnvironment().isGroup())]");
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
	* 设置转单后卡片界面单据项目可编辑性
	*/
	protected void setColumnEditableForConvertBill() {
//	 询报价单的表头栏目可编辑
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
	// 无税报价、含税报价、交货期、报价人、报价生效日期、报价失效日期不可编辑
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
	* 设置业务员默认值 根据操作员带出业务员
	*/
	private void setOperatorID() throws BusinessException {
		//v5.1 Added Begin
		//设置业务员默认值 根据操作员带出业务员
		
		UIRefPane cemployeeid = (UIRefPane) getBillCardPanelWrapper()
		.getBillCardPanel().getHeadItem("cemployeeid")
		.getComponent();
		//如果模版已经配置业务员（库管员、采购员、调出业务员）的默认值，不处理；
		//如果模版没有配置，根据操作员带出业务员（库管员、采购员、调出业务员）的默认值（依据：用户管理中选定的业务员）
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
	 * 设置界面数据，拆分处理器重载父类方法 创建日期：(2004-4-1 8:20:25)
	 * 
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	protected void setRefData(AggregatedValueObject[] vos)
			throws java.lang.Exception {
		if (vos == null || vos.length == 0)
			throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("uifactory", "UPPuifactory-000084")/*
																	 * @res
																	 * "未选择参照单据"
																	 */);
		AggregatedValueObject[] splitVos = vos;

		if (splitVos.length == 1) {
			// 设置单据状态
			getBillUI().setCardUIState();
			// 填充界面
			getBillUI().setCardUIData(splitVos[0]);
			// 设置为新增处理
			getBillUI().setBillOperate(IBillOperate.OP_REFADD);
			 //显示来源信息
			nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanelWrapper().getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_ASK);
			// 询报价单的表头栏目不可编辑
			BillItem[] headItems = getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItems();
			if (headItems != null && headItems.length > 0) {
				BillItem headItem = null;
				for (int i = 0; i < headItems.length; i++) {
					headItem = headItems[i];
					headItem.setEdit(true);
				}
			}
      //易用性
			try {
				   onBoLineAdd();
			} catch (Exception e) {
				// TODO 自动生成 catch 块
				SCMEnv.out(e.getMessage());
				PuTool.outException(e);
			}
			setButtonsStateCardMaintain();
			setButtonsStateEdit();
			setColumnEditable();
			((AskAndQuoteUI) getBillUI()).getBillCardPanel().updateUI();
			
		} else {
			// 设置不可排序
			((AskAndQuoteUI) getBillUI()).getBillListPanel()
					.getParentListPanel().removeTableSortListener();
			((AskAndQuoteUI) getBillUI())
					.setCurrentPanel(BillTemplateWrapper.LISTPANEL);
			((BillUIRefCacheForPP) ((AskAndQuoteUI) getBillUI()).getCacheData())
					.setCurRefCacheClear();
			((AskAndQuoteUI) getBillUI()).setListRefCacheData(splitVos);
			((AskAndQuoteUI) getBillUI()).getBillListPanel().setBodyValueVO(
					splitVos[0].getChildrenVO());
			// 设置为新增处理
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
	 * @功能：返回请购单生成询价单界面
	 * @作者：周晓 创建日期：(2001-8-11 14:56:58)
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
	 * 按钮m_boLineDel点击时执行的动作,如有必要，请覆盖.
	 */
	protected void onBoLineDel() throws Exception {
		super.onBoLineDel();
        //输入提示 
		AskbillMergeVO voCurr = (AskbillMergeVO) getBillCardPanelWrapper().getBillCardPanel().getBillValueVO(AskbillMergeVO.class.getName(), AskbillHeaderVO.class.getName(), AskbillItemMergeVO.class.getName());
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanelWrapper().getBillCardPanel(), voCurr);
	}
	/**
	 * 按钮m_boDel点击时执行的动作,如有必要，请覆盖. 单据的作废处理
	 */
	protected void onBoDel() throws Exception {
		int ret = MessageDialog.showYesNoDlg((AskAndQuoteUI) getBillUI(),
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101",
						"UPP4004070101-000070")/* @res "询问" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101",
						"UPP4004070101-000071")/*
												 * @res "作废询报价单吗？\n作废操作不可恢复！"
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
		if (((BillManageUI) getBillUI()).isListPanelSelected()) {// 列表删除
			//isListDel = true;
			String ibillST = null;
			((BillManageUI) getBillUI()).getBillListWrapper()
			.getBillListPanel().getBodyBillModel().clearBodyData();
			// 得到被选中的VO
			for (int i = 0; i < getBufferData().getVOBufferSize(); i++) {
				if (((BillManageUI) getBillUI()).getBillListPanel()
						.getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
					// 有可能经过了排序，得到真正的表头INDEX
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
					if (ibillstatus != IAskBillStatus.CONFIRM) {// 完成状态的询报价单不能作废
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
		} else {// 卡片删除
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
			if (ibillstatus != IAskBillStatus.CONFIRM) {// 完成状态的询报价单不能作废
				convertIBillStatusToBack(modelVo);
        setInfoForConvertForBack(modelVo);
				v = AskbillMergeVO.convertVOTOBack(modelVo);
				vendorVOs = (AskbillItemVendorVO[]) v.get(3);
				vv.add(v);
			} else {
				MessageDialog.showErrorDlg((AskAndQuoteUI) getBillUI(), "删除",
						"完成状态的询报价单不能作废");
				return;
			}
		}
		int vendorVOsLength = 0;
		if (!((BillManageUI) getBillUI()).isListPanelSelected()) {// 卡片删除
			if (vendorVOs != null && vendorVOs.length > 0) {
				vendorVOsLength = vendorVOs.length;
			}
		}
		
		if (vv == null || (vv != null && vv.size() == 0)) {
			MessageDialog.showErrorDlg((AskAndQuoteUI) getBillUI(), "删除",
			"完成状态的询报价单不能作废");
			return;
		}
		boolean isSuccess = AskHelper.deleteMy(vv);
		if (isSuccess) {
			// 更新状态
			if (!((BillManageUI) getBillUI()).isListPanelSelected()) {// 卡片删除
				modelVo.getParentVO().setAttributeValue(
						getBillField().getField_BillStatus(),
						new Integer(IBillStatus.DELETE));
				getBufferData().removeCurrentRow();
			} else {// 列表删除
				// 从缓存中除去
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
				if (!((BillManageUI) getBillUI()).isListPanelSelected()) {// 卡片删除后处理
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
					// 只要选择了一次供应商,存货就不能再进行编辑
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
				} else {// 列表删除后处理
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
					// 只要选择了一次供应商,存货就不能再进行编辑
					convertIbillstatusToString();
					updateListVo();
					((BillManageUI) getBillUI()).getBillListPanel().updateUI();
				}
			}

		}

		// 列表显示单据处理
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
   * 前台数据到后台时赋默认登陆日起和币种
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param modelVo
   * <p>
   * @author zhouxiao
   * @time 2007-2-2 上午08:59:38
   */
  protected void setInfoForConvertForBack(AggregatedValueObject modelVo) {
    ((AskbillHeaderVO) modelVo.getParentVO()).setLogdate(((AskAndQuoteUI) getBillUI())
        .getLogDate());
    ((AskbillHeaderVO) modelVo.getParentVO()).setCcurrencytypeiddefault(((AskAndQuoteUI) getBillUI())
        .getCurrtypeID());
  }

	/**
	 * 单据增加的处理 创建日期：(2002-12-23 12:43:15)
	 */
	public void onBoAdd(ButtonObject bo) throws Exception {
		// 恢复表体以下字段的可编辑
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
		// 状态设为自由
		String ibillStatus = StatusConvert.getStatusIndexNum()
				.get(new Integer(IAskBillStatus.FREE)).toString();
		getBillCardPanelWrapper().getBillCardPanel().getTailItem("ibillstatus")
				.setValue(ibillStatus);
		// 询价人默认为当前登陆人
		UIRefPane nRefPanel = (UIRefPane) getBillCardPanelWrapper()
				.getBillCardPanel().getTailItem("caskpsn").getComponent();
		nRefPanel.setPK(getUserId());
		nRefPanel.setEnabled(false);
		// 设置币种默认值为本位币
		UIRefPane currtype = (UIRefPane) getBillCardPanelWrapper()
				.getBillCardPanel().getHeadItem("ccurrencytypeid")
				.getComponent();
		currtype.setPK(((AskAndQuoteUI) getBillUI()).getCurrtypeID());
		
		// 供应商及其报价信息不可用
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
	 * 更新指定行数据。 创建日期：(2004-1-11 11:23:25)
	 * 
	 * @exception java.lang.Exception
	 *                异常说明。
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
	 * 功能：刷新请购单表体(批处理)并返回,如果表体不为空则不刷新 作者：周晓 日期：(2003-2-13 9:10:31)
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
				// 如果该VO表体已经加载到UI端过则不再重新查询
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
						 //计算自由项
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
					// 更新现有缓存
					AskbillItemMergeVO[] items = null;
					for (int i = 0; i < vos.length; i++) {
						if (vos[i].getParentVO() == null
								|| vos[i].getParentVO().getPrimaryKey() == null
								|| ""
										.equals(vos[i].getParentVO().getPrimaryKey().trim()))
							continue;
						// 如果该VO表体已经加载到UI端过则不再重新查询
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
   * 后台数据查询到前台时赋默认登陆日起和币种
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param temp
   * <p>
   * @author zhouxiao
   * @time 2007-2-2 上午09:27:48
   */
  protected void setInfoForConvertForFront(Vector temp) {
    ((AskbillHeaderVO)(temp.get(0))).setLogdate(((AskAndQuoteUI)getBillUI()).getLogDate());
    ((AskbillHeaderVO)(temp.get(0))).setCcurrencytypeiddefault(((AskAndQuoteUI) getBillUI()).getCurrtypeID());
  }

	/**
	 * 按钮m_boReturn点击时执行的动作,如有必要，请覆盖.
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
						.getVOBufferSize() == 0)) {// 无转单

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
             //显示单据来源信息
			nc.ui.pu.pub.PuTool.loadSourceInfoAll(((AskAndQuoteUI)getBillUI()).getBillListPanel(), nc.vo.scm.pu.BillTypeConst.PO_ASK);
			
			((BillManageUI) getBillUI()).getBillListPanel().getBodyBillModel()
					.execLoadFormula();
			((BillManageUI) getBillUI()).getBillListPanel().getHeadBillModel()
					.updateValue();
			((BillManageUI) getBillUI()).getBillListPanel().getBodyBillModel()
					.updateValue();

			// 列表显示单据处理
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

		// 手动触发按钮逻辑
		if (getBillUI().getBillOperate() == IBillOperate.OP_REFADD) {
			setButtonsStateListFromBills();
		} else {
			setButtonsStateListNormal();
		}
		((BillManageUI) getBillUI())
		.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH022")/*@res "列表显示"*/);
	}

	/**
	 * 按钮m_boCard点击时执行的动作,如有必要，请覆盖.
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
		.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH021")/*@res "卡片显示"*/);
	}

	/**
	 * @功能：1.把单据状态索引转换成串 索引 0 - >SENDING 发出; 索引 1 - >QUOTE 报价. 2.给列表状态字段赋值
	 * @作者：周晓 创建日期：(2001-6-14 20:53:41)
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
	 * @功能：把列表当前选中的行写入卡片
	 * @作者：周晓 创建日期：(2001-6-8 15:12:34)
	 */
	public void setBillVOToCard() throws BusinessException {
		// 关闭状态改变监听开关
		AskbillMergeVO billvo = null;
		if (getBufferData().getVOBufferSize() > 0) {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
							"UPP40040701-000046")/* @res "浏览询价单" */);
			try {
				// 处理单据体
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
																				 * "业务异常"
																				 */,
										e.getMessage());
					throw e;
				}
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().clearBodyData();
				// 加载自由项
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

				// 处理基础数据因冻结等变更而不能显示问题
				PpTool.loadBDData(getBillCardPanelWrapper().getBillCardPanel());
                // 处理自定义项
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
				/* 加载自定义项名称 */
				nc.ui.scm.pub.def.DefSetTool.updateQueryConditionClientUserDef(
						m_dlgIqQueryCondition, getCorpId(),ScmConst.PO_AskBill, "po_askbill.vdef",
						"po_askbill_bb1.vbdef");
				PuTool.loadSourceInfoAll(getBillCardPanelWrapper()
						.getBillCardPanel(), BillTypeConst.PO_ASK);
				/* 显示备注 */
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
				SCMEnv.out("设置表头表尾为空时出错");
				SCMEnv.out(e.getMessage());
				PuTool.outException(e);
			}
		}
		setButtonsStateBrowse();

	    
	}
	/**
	 * @功能：把列表当前选中的行写入卡片
	 * @作者：周晓 创建日期：(2001-6-8 15:12:34)
	 */
	public void dispCurrentVO(AskbillMergeVO billvo) throws BusinessException {
			try {
				// 加载自由项
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

				// 处理基础数据因冻结等变更而不能显示问题
				PpTool.loadBDData(getBillCardPanelWrapper().getBillCardPanel());
                //显示来源信息
				nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanelWrapper().getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_ASK);
				// 处理自定义项
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
				/* 加载自定义项名称 */
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
	 * 功能：清空单据卡片表头表体项目
	 */
	private void clearHeadTailItemsValue(BillCardPanel cardPnl) {

		if (cardPnl == null)
			return;
		// 清空表头项目(除币种外)
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
		// 清空表体项目(除单据状态外)
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
	 * 功能：获取含有表体的请购单(单张单据) 作者：周晓 日期：(2003-2-13 9:10:31)
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
	 * 按钮m_boCopy点击时执行的动作,如有必要，请覆盖.
	 */
	protected void onBoCopy() throws Exception {
		((BillManageUI) getBillUI())
				.setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		// 获得数据
		AggregatedValueObject copyVo = getBillUI().getVOFromUI();
		//设置备注
		if( getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane ){
			UIRefPane nRefPanel = (UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmemo").getComponent();
			UITextField vMemoField = nRefPanel.getUITextField();
			((AskbillHeaderVO)copyVo.getParentVO()).setVmemo(vMemoField.getText());
		}
		// 进行主键清空处理
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
		// 设置为新增处理
		getBillUI().setBillOperate(IBillOperate.OP_ADD);
		// 设置界面数据
		getBillUI().setCardUIData(copyVo);
		setButtonsStateEdit();
		// 询报价单的表头栏目不可编辑
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
		  
		 
        //自由项输入提示功能
		nc.ui.po.pub.InvAttrCellRenderer ficr = new nc.ui.po.pub.InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanelWrapper().getBillCardPanel(), copyVo);
	}

	/**
	 * 清空子表主键。 创建日期：(2004-2-25 19:59:34)
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
			// 供应商及其报价信息不复制
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
			//来源信息不复制
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
	 * 按钮m_boSelAll点击时执行的动作, 如有必要，请覆盖.
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
	 * 按钮m_boSelNone点击时执行的动作, 如有必要，请覆盖.
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
	 * 作者：王印芬 功能：设置浏览状态的按钮可用性 参数：无 返回：无 例外：无 日期：(2002-5-15 11:39:21)
	 * 修改日期，修改人，修改原因，注释标志： 2002-10-31 wyf 加入对文件操作按钮的控制 2002-11-01 wyf
	 * 修改当前无单据时的空指针错误
	 */
	protected void setButtonsStateBrowse() {
		// 维护按钮组逻辑
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
			// TODO 自动生成 catch 块
			SCMEnv.out(e.getMessage());
			PuTool.outException(e);
		}
	}

	/**
	 * 作者：周晓 功能：设置编辑时的按钮状态 参数：无 返回：无 例外：无 日期：(2002-6-5 11:39:21)
	 * 修改日期，修改人，修改原因，注释标志： 2002-10-31 wyf 加入对文件操作按钮的控制
	 */
	protected void setButtonsStateEdit() {
		line = ((AskAndQuoteUI) getBillUI()).getButtonManager().getButton(
				IBillButton.Line);

		String ibillStatus = getBillCardPanelWrapper().getBillCardPanel()
				.getTailItem("ibillstatus").getValue();
		if (ibillStatus != null
				&& ibillStatus
						.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000340")/* @res "自由" */)) {
			line.setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().setBBodyMenuShow(true);
		} else if (ibillStatus != null
				&& (ibillStatus
						.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4004070101", "UPT4004070101-000022")/*
																		 * @res
																		 * "发出"
																		 */)
				|| ibillStatus
						.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4004070101", "UPP4004070101-000050")/*
																		 * @res
																		 * "报价"
																		 */))) {// 询报价单状态为发出或报价
			line.setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().setBBodyMenuShow(false);
		}
		try {
			((AskAndQuoteUI) getBillUI()).updateButtonUI();
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			SCMEnv.out(e.getMessage());
			PuTool.outException(e);
		}

	}

	/**
	 * 作者：周晓 功能：设置订单列表的按钮状态 参数：无 返回：无 例外：无 日期：(2002-6-5 11:39:21)
	 * 修改日期，修改人，修改原因，注释标志：
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
			// TODO 自动生成 catch 块
			SCMEnv.out(e.getMessage());
			PuTool.outException(e);
		}
	}

	/**
	 * 作者：周晓 功能：设置其他单据转入时的列表按钮状态 参数：无 返回：无 例外：无 日期：(2002-6-5 11:39:21)
	 * 修改日期，修改人，修改原因，注释标志：
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

			// 只有一张单据时，全选可用
			if (getBufferData().getVOBufferSize() == 1) {
				selAllList.setEnabled(true);
				selNoneList.setEnabled(false);
			} else {
				selNoneList.setEnabled(true);
				selAllList.setEnabled(false);
			}
			// 放弃转单
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
			// 放弃转单
			listCancelTranform.setVisible(true);
			listCancelTranform.setEnabled(true);
		}
		// 查询
		queryList.setEnabled(false);
		// 作废
		delList.setEnabled(false);
		// delList.setVisible(false);
		// mainList.setEnabled(false);
		// 刷新
		reFreshList.setEnabled(false);
		// 切换
		toCard.setEnabled(false);
		// 文档管理
		docManagerlist.setEnabled(false);

		// 打印
		printlist.setEnabled(false);
		printprelist.setEnabled(false);
	}

	/**
	 * 作者：周晓 功能：设置订单列表的按钮状态 参数：无 返回：无 例外：无 日期：(2002-6-5 11:39:21)
	 * 修改日期，修改人，修改原因，注释标志： 2002-10-17 wyf 修改冻结的订单可以修改的问题 2002-02-24 wyf
	 * 加入对存在并发的单据的按钮处理
	 */
	private void setButtonsStateListNormal() {

		// 选中的行
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
			// 没有订单
			selAllList.setEnabled(false);
			selNoneList.setEnabled(false);
			delList.setEnabled(false);
			editList.setEnabled(false);
			toCard.setEnabled(true);
			docManagerlist.setEnabled(false);
			stockQuyList.setEnabled(false);
			// 打印
			printlist.setEnabled(false);
			printprelist.setEnabled(false);
			// 导入导出
			billToExcelDefaultList.setEnabled(false);
			billToExcelDeflist.setEnabled(false);
			excelToBillList.setEnabled(false);
		} else {
			// 有订单
			if (iSelectedCount == 0) {
				// 未选中订单
				selAllList.setEnabled(true);
				selNoneList.setEnabled(false);
				delList.setEnabled(false);
				editList.setEnabled(false);
				toCard.setEnabled(false);
				// 导入导出
				billToExcelDefaultList.setEnabled(false);
				billToExcelDeflist.setEnabled(false);
				excelToBillList.setEnabled(false);
			} else {
				// 全消可用
				delList.setEnabled(true);
				// 全选
				if (iSelectedCount == getBufferData().getVOBufferSize()) {
					selAllList.setEnabled(false);
					selNoneList.setEnabled(true);
				} else {
					selAllList.setEnabled(true);
					selNoneList.setEnabled(false);
				}

				// 选中一张订单
				if (iSelectedCount == 1) {
					// 因此方法必在加载表体后进行，所以已知是否存在并发，只需直接调用VO，不需再加载
					// 是否可以切换
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
					// 是否可以修改、作废
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
						// 自由,审批未通过
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
				// 打印
				printlist.setEnabled(true);
				printprelist.setEnabled(true);
				// 导入导出
				billToExcelDefaultList.setEnabled(false);
				billToExcelDeflist.setEnabled(false);
				excelToBillList.setEnabled(false);
			}

			// 文档管理
			if (iSelectedCount == 0) {
				docManagerlist.setEnabled(false);
				stockQuyList.setEnabled(false);
			} else {
				docManagerlist.setEnabled(true);
				stockQuyList.setEnabled(true);
			}
		}
		// 查询
		queryList.setEnabled(true);
		// 放弃转单
		if (listCancelTranform != null) {
			listCancelTranform.setVisible(false);
		}
	}

	/**
	 * 作者：周晓 功能：得到列表界面下表头选中的行 参数：无 返回：无 例外：无 日期：(2002-4-22 11:39:21)
	 * 修改日期，修改人，修改原因，注释标志： 2003-03-10 wyf 修改取选中的行的方法
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
	 * 作者：周晓 功能：得到列表界面下表头选中的行所对应的所有VO的位置，并已按VO中的位置大小排序 参数：无 返回：无 例外：无
	 * 日期：(2003-11-05 11:39:21) 修改日期，修改人，修改原因，注释标志：
	 */
	public Integer[] getHeadSelectedVOPoses() {

		// 需作废的订单
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
	 * 作者：周晓 功能：根据给定界面行 返回VO的真正下标 参数：int iUIRow 界面行 返回：int 真正对应的VO下标，亦即排序前的下标
	 * 例外：无 日期：(2001-05-22 13:24:16) 修改日期，修改人，修改原因，注释标志：
	 */
	public int getVOPos(int iUIRow) {

		return PuTool.getIndexBeforeSort(((AskAndQuoteUI) getBillUI())
				.getBillCardPanel(), iUIRow);
	}

	/**
	 * 作者：周晓 功能：放弃转入未完成的单据 参数：无 返回：无 例外：无 日期：(2003-03-31 13:24:16)
	 * 修改日期，修改人，修改原因，注释标志：
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
			// 显示原有或最后一张单据
			if (getBufferData().getVOBufferSize() > 0) {
				getBufferData().setCurrentRow(
						getBufferData().getVOBufferSize() - 1);
			} else {
				((AskAndQuoteUI) getBillUI()).getBillListPanel()
						.getHeadBillModel().clearBodyData();
				((AskAndQuoteUI) getBillUI()).getBillListPanel()
						.getBodyBillModel().clearBodyData();
			}
			if (((BillManageUI) getBillUI()).isListPanelSelected()) {// 列表

				if (getBufferData().getVOBufferSize() > 0) {
					getBufferData().updateView();
					((BillManageUI) getBillUI()).getBillListPanel().setHeaderValueVO(
							getBufferData().getAllHeadVOsFromBuffer());
					((BillManageUI) getBillUI()).getBillListPanel().getHeadBillModel()
							.execLoadFormula();
					((BillManageUI) getBillUI()).getBillListPanel().getBodyBillModel()
							.setBodyDataVO(
									getBufferData().getCurrentVO().getChildrenVO());
		             //显示单据来源信息
					nc.ui.pu.pub.PuTool.loadSourceInfoAll(((AskAndQuoteUI)getBillUI()).getBillListPanel(), nc.vo.scm.pu.BillTypeConst.PO_ASK);
					
					((BillManageUI) getBillUI()).getBillListPanel().getBodyBillModel()
							.execLoadFormula();
					((BillManageUI) getBillUI()).getBillListPanel().getHeadBillModel()
							.updateValue();
					((BillManageUI) getBillUI()).getBillListPanel().getBodyBillModel()
							.updateValue();

					// 列表显示单据处理
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
			// TODO 自动生成 catch 块
			SCMEnv.out(e.getMessage());
			PuTool.outException(e);
		}
	}

	/**
	 * 设置界面数据，拆分处理器重载父类方法 创建日期：(2004-4-1 8:20:25)
	 * 
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	protected void setRefDataForSave(AggregatedValueObject[] vos)
			throws java.lang.Exception {
		AggregatedValueObject[] splitVos = vos;
		if (splitVos == null || (splitVos != null && splitVos.length == 0)) {// 无转单
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
             //显示单据来源信息
			nc.ui.pu.pub.PuTool.loadSourceInfoAll(getBillCardPanelWrapper().getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_ASK);
			getBillCardPanelWrapper().getBillCardPanel().updateUI();
			setButtonsStateBrowse();
			
		} else {// 有转单
			if (splitVos.length == 1) {
				// 设置单据状态
				getBillUI().setCardUIState();
				// 填充界面
				getBillUI().setCardUIData(splitVos[0]);
				// 设置用户对象
				// getBillUI().setUserObject(getBusiSplit().getUserObj(splitVos[0]));
				// 设置为新增处理
				getBillUI().setBillOperate(IBillOperate.OP_REFADD);
				setColumnEditableForConvertBill();
				setButtonsStateEdit();
			} else {
				// 设置不可排序
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
				// 设置为新增处理
				(((AskAndQuoteUI) getBillUI()))
						.setSplitBillOperate(IBillOperate.OP_REFADD);
				setButtonsStateList();
			}
		}
	}

	/**
	 * 得到文件选择框
	 * 
	 * @return javax.swing.JFileChooser
	 */
	protected javax.swing.JFileChooser getChooser() {
		if (m_chooser == null) {
			m_chooser = new UIFileChooser();
			// 移去当前的文件过滤器
			m_chooser.removeChoosableFileFilter(m_chooser.getFileFilter());
			// 表示可选取的包括文件和目录
			m_chooser.setFileSelectionMode(m_chooser.DIRECTORIES_ONLY);
		}
		return m_chooser;
	}

	/**
	 * 得到选中的目录
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
	 * 单据导出Excel 创建日期：(2003-09-28 9:51:50)
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
			// 打开文件
			if (getChooser().showDialog(
					getBillUI(),
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC001-0000044")/* @res "确定" */) == javax.swing.JFileChooser.CANCEL_OPTION)
				return;
			sFilePathDir = getChooser().getSelectedFile().toString();
		} else {
			sFilePathDir = getDefaultDir();
		}

		try {
			// 依当前是列表还是表单而定导出内容
			if (getBillUI().getBillOperate() == IBillOperate.OP_NOTEDIT
					&& !((BillManageUI) getBillUI()).isListPanelSelected()) { // 浏览
				getBillUI().showHintMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
								"UPP40040701-000059")/* @res "正在导出，请稍候..." */);
				String NAME_HEADVO = "nc.vo.pp.ask.AskbillHeaderVO";
				String NAME_ITEMVOs = "nc.vo.pp.ask.AskbillItemMergeVO";
				// 表头
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
																			 * "请先查询或录入单据。"
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
																	 * "请先选中单据后再试。"
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
																 * "单据无供应商，请选择供应商后再导出excel。"
																 */);
						return;
					}
				}

				// 得到单据号，公司
				sBillCode = voBill.getParentVO().getAttributeValue(
						"vaskbillcode") == null ? "" : voBill.getParentVO()
						.getAttributeValue("vaskbillcode").toString();
//				sPKCorp = voBill.getParentVO().getAttributeValue("pk_corp") == null ? ""
//						: voBill.getParentVO().getAttributeValue("pk_corp")
//								.toString();
//				tvos = (AskbillItemMergeVO[]) voBill.getChildrenVO();
//				tvosHead = (AskbillHeaderVO) voBill.getParentVO();
//
//				// 需要导出的数据存入VO
//				Hashtable result = new Hashtable();
				Vector v = new Vector();
//				nc.vo.pp.ask.ExcelFileVO vo = null;

				// 设置询价人
				Object oTemp = CacheTool.getCellValue("sm_user", "cUserId",
						"user_name", voHeader.getCaskpsn());
				if (oTemp != null) {
					Object data[] = (Object[]) oTemp;
					voBill.getParentVO().setAttributeValue("caskpsnname",
							data[0].toString());
				}
				// 设置报价人
				oTemp = CacheTool.getCellValue("sm_user", "cUserId",
						"user_name", voHeader.getCquotepsn());
				if (oTemp != null) {
					Object data[] = (Object[]) oTemp;
					voBill.getParentVO().setAttributeValue("cquotepsnname",
							data[0].toString());
				}

				// 得到需要导出的VO
				v = saveOutputVOs(sBillCode, new AskbillMergeVO[] { voBill });
				if (v != null && v.size() > 0) {
					nc.ui.pp.pub.ExcelReadCtrl erc = null;
					for (int k = 0; k < v.size(); k++) {
						vos = new nc.vo.pp.ask.ExcelFileVO[((Vector) v.get(k))
								.size()];
						((Vector) v.get(k)).copyInto(vos);
						// 调用导出接口
						// 文件名称规则：Askbill+公司PK+单据号+"_"+供应商".xls"
						sFilePath = sFilePathDir + "\\" + "询价单号("
								+ vos[0].getVaskbillcode() + ")_供应商("
								+ vos[0].getCvendorname() + ").xls";
						erc = new nc.ui.pp.pub.ExcelReadCtrl();
						erc.setVOToExcel(vos, sFilePath);
					}
				}
				getBillUI().showHintMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
								"UPP40040701-000062")/* @res "导出完成" */);
				getBillUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
								"UPP40040701-000062")/* @res "导出完成" */);
			} else if (getBillUI().getBillOperate() == IBillOperate.OP_NOTEDIT
					&& ((BillManageUI) getBillUI()).isListPanelSelected()) { // 列表
				if (getBufferData() == null
						|| (getBufferData() != null && getBufferData()
								.getVOBufferSize() == 0)) {
					getBillUI()
							.showHintMessage(
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("40040701",
													"UPP40040701-000060")/*
																			 * @res
																			 * "请先查询或录入单据。"
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
																			 * "请先选中单据后再试。"
																			 */);
					return;
				}
				AskbillMergeVO[] allBill = new AskbillMergeVO[alBill.size()];
				alBill.toArray(allBill);
				// 需要导出的数据存入VO
				Vector v = new Vector();
//				nc.vo.pp.ask.ExcelFileVO vo = null;
				// 得到需要导出的VO
				v = saveOutputVOs(sBillCode, allBill);
				// 每张单据将生成一个文件
				// 已经得到vo，然后导出vo
				if (v != null && v.size() > 0) {
					nc.ui.pp.pub.ExcelReadCtrl erc = null;
					for (int k = 0; k < v.size(); k++) {
						vos = new nc.vo.pp.ask.ExcelFileVO[((Vector) v.get(k))
								.size()];
						((Vector) v.get(k)).copyInto(vos);
						// 调用导出接口
						// 文件名称规则：Askbill+公司PK+单据号+"_"+供应商".xls"
						sFilePath = sFilePathDir + "\\" + "询价单号("
								+ vos[0].getVaskbillcode() + ")_供应商("
								+ vos[0].getCvendorname() + ").xls";
						erc = new nc.ui.pp.pub.ExcelReadCtrl();
						erc.setVOToExcel(vos, sFilePath);

					}
				}
				getBillUI().showHintMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
								"UPP40040701-000062")/* @res "导出完成" */);
				getBillUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
								"UPP40040701-000062")/* @res "导出完成" */);
			}
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
							"UPP40040701-000063")/* @res "导出出错" */);
			String[] value = new String[] { e.getMessage(), sFilePath };
			getBillUI().showWarningMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4004070101",
							"UPP4004070101-000038", null, value))/*
																	 * @res
																	 * "导出出错："+
																	 * e.getMessage()+",文件路径："+sFilePath)
																	 */;
		}
	}

	/**
	 * 保存要导出的数据 创建日期：(2003-09-28 9:51:50)
	 */
	private Vector saveOutputVOs(String sBillCode, AskbillMergeVO[] vos) throws Exception{
		// 需要导出的数据存入VO
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
		// 保存要导出的数据到vo中
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
					// 单据行信息
					vo.setAttributeValue("crowno", tvos[i].getCrowno()); // 行号
					vo.setAttributeValue("cmangid", tvos[i].getCmangid()); // 存货管理档案编码
					vo
							.setAttributeValue("invcode", tvos[i]
									.getCinventorycode()); // 存货编码
					vo
							.setAttributeValue("invname", tvos[i]
									.getCinventoryname()); // 存货名称
					vo
							.setAttributeValue("invspec", tvos[i]
									.getCinventoryspec()); // 规格
					vo
							.setAttributeValue("invtype", tvos[i]
									.getCinventorytype()); // 型号
					vo.setAttributeValue("cmainmeasname", tvos[i]
							.getCmainmeasname()); // 单位
					vo.setAttributeValue("vfree0", tvos[i].getVfree()); // 自由项目
					// 数量
					UFDouble dshouldin = null;
					UFDouble dnum = null; // 要导出的数量
					dshouldin = tvos[i].getNasknum();
					dnum = dshouldin == null ? new UFDouble(0) : dshouldin;
					vo.setAttributeValue("nasknum", dnum.toString()); // 数量
					vo.setAttributeValue("ntaxrate",
							(tvos[i].getNtaxrate() == null ? "" : tvos[i]
									.getNtaxrate().toString())); // 税率
					vo.setAttributeValue("dreceivedate", (tvos[i]
							.getDreceivedate() == null ? "" : tvos[i]
							.getDreceivedate().toString())); // 到货日期
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
										.getRowNo()); // 行号
						vo.setAttributeValue("cmangid", voTempForItem
								.getCmangid()); // 存货管理档案编码
						vo.setAttributeValue("invcode", voTempForItem
								.getInvcode()); // 存货编码
						vo.setAttributeValue("invname", voTempForItem
								.getInvname()); // 存货名称
						vo.setAttributeValue("invspec", voTempForItem
								.getInvspec()); // 规格
						vo.setAttributeValue("invtype", voTempForItem
								.getInvtype()); // 型号
						vo.setAttributeValue("cmainmeasname", voTempForItem
								.getCmainmeasname()); // 单位
						vo.setAttributeValue("vfree", voTempForItem
								.getVfree0()); // 自由项目
                        //数量
						UFDouble dnum = null; // 要导出的数量
						String dshouldin = voTempForItem.getNasknum();
						dnum = dshouldin == null ? new UFDouble(0) : new UFDouble(dshouldin);
						vo.setAttributeValue("nasknum", dnum.toString()); // 数量
						vo.setAttributeValue("ntaxrate",
								(voTempForItem.getTaxratio() == null ? "" : voTempForItem.getTaxratio().trim())); // 税率
						vo.setAttributeValue("dreceivedate", (tvos[i]
								.getDreceivedate() == null ? "" : tvos[i]
								.getDreceivedate().toString())); // 到货日期

						// 报价信息
						vo.setAttributeValue("nquoteprice", (tvos[i]
								.getNquoteprice() == null ? "" : tvos[i]
								.getNquoteprice().toString())); // 无税报价
						vo.setAttributeValue("nquotetaxprice", (tvos[i]
								.getNquotetaxprice() == null ? "" : tvos[i]
								.getNquotetaxprice().toString())); // 含税报价
						vo.setAttributeValue("deliverdays", (tvos[i]
								.getDeliverdays() == null ? "" : tvos[i]
								.getDeliverdays().toString())); // 交货期
						vo.setAttributeValue("dvaliddate", (tvos[i]
								.getDvaliddate() == null ? "" : tvos[i]
								.getDvaliddate().toString())); // 报价生效日期
						vo.setAttributeValue("dinvaliddate", (tvos[i]
								.getDinvaliddate() == null ? "" : tvos[i]
								.getDinvaliddate().toString())); // 报价失效日期
						vo.setAttributeValue("caskbillid", tvos[i]
								.getCaskbillid()); // 表头pk
						vo.setAttributeValue("caskbill_bid", tvos[i]
								.getCaskbill_bid()); // 子表pk
						vo.setAttributeValue("caskbill_bb1id", tvos[i]
								.getCaskbill_bb1id()); // 子子表pk

						tempVForItem.add(vo);
					} else {
						tempVForItem = new Vector();

						if (tvosHead != null) {
							voForHead = new nc.vo.pp.ask.ExcelFileVO();

							voForHead.setAttributeValue("vaskbillcode",
									tvosHead.getVaskbillcode()); // 单据号

							voForHead.setAttributeValue("cvendorname", tvos[i]
									.getCvendorname()); // 供应商
							voForHead.setAttributeValue("cvendorbaseid",
									tvos[i].getCvendorbaseid()); // 供应商基础档案ID
							voForHead.setAttributeValue("cvendormangid",
									tvos[i].getCvendormangid()); // 供应商管理档案ID

							voForHead.setAttributeValue("ccurrencytypeid",
									tvosHead.getCcurrencytypeid()); // 原币币种ID
							voForHead.setAttributeValue("ccurrencytypename",
									tvosHead.getCcurrencytypename()); // 原币币种

							voForHead.setAttributeValue("dclosedate", (tvosHead
									.getDclosedate() == null ? "" : tvosHead
									.getDclosedate().toString())); // 报价截至日期

							voForHead.setAttributeValue("caskpsn", tvosHead
									.getCaskpsn()); // 询价人ID
							voForHead.setAttributeValue("caskpsnname", tvosHead
									.getCaskpsnname()); // 询价人ID
							voForHead.setAttributeValue("daskdate", (tvosHead
									.getDaskdate() == null ? "" : tvosHead
									.getDaskdate().toString())); // 询价日期

						}
						tempVForItem.add(voForHead);
						vo = new nc.vo.pp.ask.ExcelFileVO();
						ExcelFileVO voTempForItem = (ExcelFileVO) itemResultBySpecialNum
								.get(tvos[i].getSpecialNum());

						vo
								.setAttributeValue("crowno", voTempForItem
										.getRowNo()); // 行号
						vo.setAttributeValue("cmangid", voTempForItem
								.getCmangid()); // 存货管理档案编码
						vo.setAttributeValue("invcode", voTempForItem
								.getInvcode()); // 存货编码
						vo.setAttributeValue("invname", voTempForItem
								.getInvname()); // 存货名称
						vo.setAttributeValue("invspec", voTempForItem
								.getInvspec()); // 规格
						vo.setAttributeValue("invtype", voTempForItem
								.getInvtype()); // 型号
						vo.setAttributeValue("cmainmeasname", voTempForItem
								.getCmainmeasname()); // 单位
						vo.setAttributeValue("vfree", voTempForItem
								.getVfree0()); // 自由项目
						vo.setAttributeValue("nasknum", voTempForItem
								.getNasknum()); // 数量
						vo.setAttributeValue("ntaxrate", voTempForItem
								.getTaxratio()); // 税率
						vo.setAttributeValue("dreceivedate", voTempForItem
								.getDreceivedate()); // 到货日期

						// 报价信息
						vo.setAttributeValue("nquoteprice", (tvos[i]
								.getNquoteprice() == null ? "" : tvos[i]
								.getNquoteprice().toString())); // 无税报价
						vo.setAttributeValue("nquotetaxprice", (tvos[i]
								.getNquotetaxprice() == null ? "" : tvos[i]
								.getNquotetaxprice().toString())); // 含税报价
						vo.setAttributeValue("deliverdays", (tvos[i]
								.getDeliverdays() == null ? "" : tvos[i]
								.getDeliverdays().toString())); // 交货期
						vo.setAttributeValue("dvaliddate", (tvos[i]
								.getDvaliddate() == null ? "" : tvos[i]
								.getDvaliddate().toString())); // 报价生效日期
						vo.setAttributeValue("dinvaliddate", (tvos[i]
								.getDinvaliddate() == null ? "" : tvos[i]
								.getDinvaliddate().toString())); // 报价失效日期

						// vo.setAttributeValue("billtype", "a"); //单据类型
						vo.setAttributeValue("caskbillid", tvos[i]
								.getCaskbillid()); // 表头pk
						vo.setAttributeValue("caskbill_bid", tvos[i]
								.getCaskbill_bid()); // 子表pk
						vo.setAttributeValue("caskbill_bb1id", tvos[i]
								.getCaskbill_bb1id()); // 子子表pk
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
	 * 分供应商打印数据拆分 创建日期：(2003-09-28 9:51:50)
	 * 
	 * @throws Exception
	 */
	private ArrayList getDataForPrintByVendor() throws Exception {
		AskbillMergeVO vo = (AskbillMergeVO) getBillUI().getVOFromUI();
		// 需要导出的数据存入VO
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

		// 分供应商打印数据拆分
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
			// 存货信息
			if (tvosItems[i].getCinventorycode() != null
					&& tvosItems[i].getCinventorycode().length() > 0) {
				itemT = new AskbillItemMergeVO();
				itemT = tvosItems[i];
			} else {
				itemT = new AskbillItemMergeVO();
				itemT = (AskbillItemMergeVO) invInfo.get(tvosItems[i]
						.getSpecialNum());
			}
			item.setAttributeValue("crowno", itemT.getCrowno()); // 行号
			item.setAttributeValue("cmangid", itemT.getCmangid()); // 存货管理档案编码
			item.setAttributeValue("cinventorycode", itemT.getCinventorycode()); // 存货编码
			item.setAttributeValue("cinventoryname", itemT.getCinventoryname()); // 存货名称
			item.setAttributeValue("cinventoryspec", itemT.getCinventoryspec()); // 规格
			item.setAttributeValue("cinventorytype", itemT.getCinventorytype()); // 型号
			item.setAttributeValue("cmainmeasname", itemT.getCmainmeasname()); // 单位
			item.setAttributeValue("vfree", itemT.getVfree()); // 自由项目
			// 数量
			UFDouble dshouldin = null;
			UFDouble dnum = null; // 要导出的数量
			dshouldin = itemT.getNasknum();
			dnum = dshouldin == null ? new UFDouble(0) : dshouldin;
			item.setNasknum(dnum);// 数量
			item.setNtaxrate(itemT.getNtaxrate());// 税率
			item.setDreceivedate(itemT.getDreceivedate());// 到货日期

			// 报价信息
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
		// 组织数据返回
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
	 * 保存要导出的数据 创建日期：(2003-09-28 9:51:50)
	 */
	private Vector saveOutputVOsForSend(Vector vendorV, Vector vendorMangIdsV,
			AskbillMergeVO[] vos) {
		// 需要导出的数据存入VO
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
						.getVaskbillcode()); // 单据号
				voForHead.setAttributeValue("cvendorname", vendorV.get(i)); // 供应商
				voForHead.setAttributeValue("ccurrencytypename", tvosHead
						.getCcurrencytypename()); // 原币币种
				voForHead.setAttributeValue("dclosedate", (tvosHead
						.getDclosedate() == null ? "" : tvosHead
								.getDclosedate().toString())); // 报价截至日期
				voForHead.setAttributeValue("caskpsn", tvosHead.getCaskpsn()); // 询价人ID
				// 设置询价人
				if(tvosHead.getCaskpsn() != null && tvosHead.getCaskpsn().trim().length() > 0){
					Object oTemp = null;
					try {
						oTemp = CacheTool.getCellValue("sm_user", "cUserId",
								"user_name", tvosHead.getCaskpsn());
					} catch (BusinessException e) {
						// TODO 自动生成 catch 块
						e.printStackTrace();
					}
					if (oTemp != null) {
						Object data[] = (Object[]) oTemp;
						voForHead.setAttributeValue("caskpsnname",
								data[0].toString());
					}
				}else{
					voForHead.setAttributeValue("caskpsnname", tvosHead
							.getCaskpsnname()); // 询价人ID
				}
				voForHead.setAttributeValue("daskdate",
						(tvosHead.getDaskdate() == null ? "" : tvosHead
								.getDaskdate().toString())); // 询价日期
				voForHead.setAttributeValue("cvendormangid", vendorMangIdsV
						.get(i)); // 供应商ID
				vendorVForHead.add(voForHead);
			}
		}
		// 保存要导出的数据到vo中
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
						// 单据行信息
						invVO.setAttributeValue("crowno", tvos[i].getCrowno()); // 行号
						invVO.setAttributeValue("cmangid", tvos[i].getCmangid()); // 存货管理档案编码
						invVO
						.setAttributeValue("invcode", tvos[i]
						                                   .getCinventorycode()); // 存货编码
						invVO
						.setAttributeValue("invname", tvos[i]
						                                   .getCinventoryname()); // 存货名称
						invVO
						.setAttributeValue("invspec", tvos[i]
						                                   .getCinventoryspec()); // 规格
						invVO
						.setAttributeValue("invtype", tvos[i]
						                                   .getCinventorytype()); // 型号
						invVO.setAttributeValue("cmainmeasname", tvos[i]
						                                              .getCmainmeasname()); // 单位
						invVO.setAttributeValue("vfree", tvos[i].getVfree()); // 自由项目
						// 数量
						UFDouble dshouldin = null;
						UFDouble dnum = null; // 要导出的数量
						dshouldin = tvos[i].getNasknum();
						dnum = dshouldin == null ? new UFDouble(0) : dshouldin;
						invVO.setAttributeValue("nasknum", dnum.toString()); // 数量
						
						itemResultBySpecialNum.put(tvos[i].getSpecialNum(), invVO);
					}
					//单据行信息
					vo.setAttributeValue("crowno", itemResultBySpecialNum.
							get(tvos[i].getSpecialNum()).getRowno()); // 行号
					vo.setAttributeValue("cmangid", itemResultBySpecialNum.
							get(tvos[i].getSpecialNum()).getCmangid()); // 存货管理档案编码
					vo.setAttributeValue("invcode", itemResultBySpecialNum.
							get(tvos[i].getSpecialNum()).getInvcode()); // 存货编码
					vo.setAttributeValue("invname", itemResultBySpecialNum.
							get(tvos[i].getSpecialNum()).getInvname()); // 存货名称
					vo.setAttributeValue("invspec", itemResultBySpecialNum.
							get(tvos[i].getSpecialNum()).getInvspec()); // 规格
					vo.setAttributeValue("invtype", itemResultBySpecialNum.
							get(tvos[i].getSpecialNum()).getInvtype()); // 型号
					vo.setAttributeValue("cmainmeasname", itemResultBySpecialNum.
							get(tvos[i].getSpecialNum()).getCmainmeasname()); // 单位
					vo.setAttributeValue("vfree", itemResultBySpecialNum.
							get(tvos[i].getSpecialNum()).getAttributeValue("vfree")); // 自由项目
					vo.setAttributeValue("nasknum", itemResultBySpecialNum.
							get(tvos[i].getSpecialNum()).getNasknum()); // 数量
					
					vo.setAttributeValue("ntaxrate",
							(tvos[i].getNtaxrate() == null ? "" : 
								tvos[i].getNtaxrate().toString())); // 税率
					vo.setAttributeValue("dreceivedate", 
							(tvos[i].getDreceivedate() == null ? "" : 
								tvos[i].getDreceivedate().toString())); // 到货日期
					vo.setAttributeValue("caskbillid", tvos[i].getCaskbillid()); // 表头pk
					vo.setAttributeValue("caskbill_bid", tvos[i].getCaskbill_bid()); // 子表pk
					vo.setAttributeValue("caskbill_bb1id", tvos[i].getCaskbill_bb1id()); // 子子表pk
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
	 * 显示选择文件目录
	 * 
	 */
	private void doShowDir() {
		state = getFileChooser().showOpenDialog(getBillUI());
		File f = getFileChooser().getSelectedFile();
		if (f != null && state == UIFileChooser.APPROVE_OPTION) {
			{ 
				// 记录当前文件的目录
				m_currentPath = f.toString();
				// 设置当前文件目录
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
				// 读出当前目录下所有的文件
				readAllFileList();
			}
		}
	}

	/**
	 * 读取目录下所有文件
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

		// 清空表格
		getBillCardPanelWrapper().getBillCardPanel().getBillModel()
				.clearBodyData();
		
		// 把读出的XLS文件加入到表格中
		UpLoadFileVO vo = null;
		int k = 0;
//		UpLoadFileVO[] vos = null;
		excelTOBill = new Vector();
		if (m_ht == null || m_ht.size() == 0) { // 非过滤时
			for (int i = 0; i < m_allcurrfiles.length; i++) {
				if(m_fFilePath.isDirectory() && !m_allcurrfiles[i].exists()){
					continue;
				}
				vo = getUploadCtrl().createFileVOs(m_allcurrfiles[i], i + 1);
				excelTOBill.add(vo);
			}
		} else { // 处理过滤
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
				// 过滤文件类型
				if (sfiletype != null && sfiletype.length() > 0) {
					if (!sfiletype.equals(sType)) {
						continue;
					}
				}
				// 过滤文件名称
				if (sfilename != null && sfilename.length() > 0) {
					if (sName.indexOf(sfilename) == -1) {
						continue;
					}
				}
				// 过滤文件日期
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
	 * 周晓 功能： 参数： 返回： 例外： 日期：(2004-10-11 19:40:03) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.ui.ic.pub.barcodeoffline.UpLoadCtrl
	 */
	public UpLoadCtrl getUploadCtrl() {
		if (m_UploadCtrl == null)
			m_UploadCtrl = new UpLoadCtrl(getBillUI());
		return m_UploadCtrl;
	}

	/**
	 * 周晓 功能： 参数： 返回： 例外： 日期：(2004-10-11 19:35:20) 修改日期，修改人，修改原因，注释标志：
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
	 * Excel导入到单据 创建日期：(2003-09-28 9:51:50)
	 */
	public void onExcelBill() {
		// 保存选中的上传的文件
		String[] sFileNames = null;
//		ExcelReadCtrl erc = null;
		// 保存上传成功的单据
//		ArrayList askBillVOs = new ArrayList();
		// 上传单据VO转换时使用
//		AskbillVO[] askbillvos = null;
		// 打开文件
		doShowDir();
		// 根据按钮做出判断
		if (state == javax.swing.JFileChooser.CANCEL_OPTION) {
			getBillUI().showWarningMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
							"UPP40040701-000074")/* @res "上传操作被取消！" */);
			return;
		} else {
			// 获取选中的上传文件
			sFileNames = getSelectedFiles();
			if (sFileNames == null || sFileNames.length <= 0) {
				getBillUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
								"UPP40040701-000075")/* @res "请选择要上传的单据文件！" */);
				if(!((BillManageUI) getBillUI()).isListPanelSelected()){
				// 显示数据、处理按钮状态
			    getBufferData().clear();
			    getBufferData().updateView();
				}
				return;
			}
		}

		try {

			// 获取上传文件的路径
			String sPath = gettfDirectory().getText().trim();
			// 批量上传
			ArrayList alresult = getUploadCtrl().UpLoadFiles(getCorpId(),
					sFileNames, sPath, getUserId(), getLogDate());
			// 错误信息
//			String sError = (String) alresult.get(0);
			// 所有上传的文件
//			ArrayList alUploadFile = (ArrayList) alresult.get(1);
			// 上传失败的文件
//			ArrayList alUploadFailFile = (ArrayList) alresult.get(2);
			// 上传成功的文件
//			askBillVOs = (ArrayList) alresult.get(4);
			// 上传单据VO转换
//			if (askBillVOs != null && askBillVOs.size() > 0) {
//				AskbillVO askbilltempvo = new AskbillVO();
//				askbillvos = new AskbillVO[askBillVOs.size()];
//				for (int i = 0; i < askBillVOs.size(); i++) {
//					askbilltempvo = (AskbillVO) askBillVOs.get(i);
//					askbillvos[i] = askbilltempvo;
//				}
//			}
			// 判断文件是否上传成功
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
//										"UPPSCMCommon-000132")/* @res "警告" */,
//								nc.ui.ml.NCLangRes.getInstance().getStrByID(
//										"40040701", "UPP40040701-000072")/*
//																			 * @res
//																			 * "请确定文件目录和文件是否存在！"
//																			 */);
//			else
				MessageDialog.showErrorDlg(getBillUI(), nc.ui.ml.NCLangRes
						.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000132")/* @res "警告" */, e
						.getMessage());
				return;
		}

		// 显示数据、处理按钮状态
		getBillCardPanelWrapper().getBillCardPanel().getBillModel()
				.clearBodyData();
		try {
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			SCMEnv.out(e.getMessage());
			PuTool.outException(e) ;
		}
		getBufferData().clear();
		getBufferData().updateView();
	}

	/**
	 * @功能：判断单据是否上传成功
	 * @作者：周晓 创建日期：(2004-12-8 15:48:14)
	 * @param:alUploadFile--所有上传的文件 alUploadFailFile--所有上传失败的文件
	 *                              askBillVOs--所有上传成功的文件 sPath--上传文件的路径
	 *                              erc---EXCEL文件接口
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 * @param newM_strState
	 *            java.lang.String
	 */
	private void isUpLoadFileSuccessNew(String isSuccess) {
		// 判断文件是否上传成功
		if (isSuccess != null && isSuccess.length() > 0
				&& "success".equals(isSuccess)) {
			MessageDialog.showWarningDlg(getBillUI(), "提示",
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
							"UPP40040701-000070")/* @res "所有文件上传成功！" */);

		}
		else if ((isSuccess != null && isSuccess.length() > 0 && "false"
				.equals(isSuccess))
				|| isSuccess == null) {
			MessageDialog.showErrorDlg(getBillUI(), nc.ui.ml.NCLangRes
					.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000132")/* @res "警告" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
							"UPP40040701-000073")/* @res "所有文件上传失败!" */);
		}
	}

	/**
	 * @功能：判断单据是否上传成功
	 * @作者：周晓 创建日期：(2004-12-8 15:48:14)
	 * @param:alUploadFile--所有上传的文件 alUploadFailFile--所有上传失败的文件
	 *                              askBillVOs--所有上传成功的文件 sPath--上传文件的路径
	 *                              erc---EXCEL文件接口
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
//		// 判断文件是否上传成功
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
//							"UPPSCMCommon-000132")/* @res "警告" */,
//					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
//							"UPP40040701-000070")/* @res "所有文件上传成功！" */);
//
//		}
//
//		if (askBillVOs != null && askBillVOs.size() > 0
//				&& alUploadFailFile != null && alUploadFailFile.size() > 0) {
//			MessageDialog.showErrorDlg(getBillUI(), nc.ui.ml.NCLangRes
//					.getInstance().getStrByID("SCMCOMMON",
//							"UPPSCMCommon-000132")/* @res "警告" */,
//					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
//							"UPP40040701-000071")/* @res ",部分文件上传失败" */);
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
//																				 * "警告"
//																				 */,
//										nc.ui.ml.NCLangRes.getInstance()
//												.getStrByID("40040701",
//														"UPP40040701-000072")/*
//																				 * @res
//																				 * "请确定文件目录和文件是否存在！"
//																				 */);
//					else
//						MessageDialog
//								.showErrorDlg(
//										getBillUI(),
//										nc.ui.ml.NCLangRes.getInstance()
//												.getStrByID("SCMCOMMON",
//														"UPPSCMCommon-000132")/*
//																				 * @res
//																				 * "警告"
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
//							"UPPSCMCommon-000132")/* @res "警告" */,
//					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
//							"UPP40040701-000073")/* @res "所有文件上传失败!" */);
//
//		}
//	}

	/**
	 * 得到选中的文件
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
	 * 过滤选中的文件类型
	 * 
	 * @return javax.swing.JFileChooser
	 */
	private JFileChooser getFileChooser() {
		if (m_filechooser == null) {
			m_filechooser = new UIFileChooser();
			// 移去当前的文件过滤器
			m_filechooser.removeChoosableFileFilter(m_filechooser
					.getFileFilter());
			// 添加文件选择过滤器
			 m_filechooser.addChoosableFileFilter(new
					 ExcelFileFilter("xls"));//Excel文件
			// 表示可选取的包括文件和目录
			m_filechooser.setFileSelectionMode(m_filechooser.FILES_AND_DIRECTORIES);
		}
		return m_filechooser;
	}

	/**
	 * 作者：周晓 功能：设置维护状态的按钮可用性 参数：无 返回：无 例外：无 日期：(2002-5-15 11:39:21)
	 * 修改日期，修改人，修改原因，注释标志： 2002-10-31 zx 加入对文件操作按钮的控制 2002-11-01 zx
	 * 修改当前无单据时的空指针错误
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
	      nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm","UPPsmcomm-000053")/*@res "账号管理对话框"*/,currentUser);
    dlg.showModal();
	}
	
	/**
	 * 
	 * 作者：田锋涛
	 * 功能：根据目标数据的cupsourcebts，cupsourcehts，设置源数据的cupsourcebts，cupsourcehts
	 * 参数： sourceItems - 源数据 (AskBillMergeVO)
	 *        targetItems - 目标数据 (AskBillMergeVO)
	 * 返回：
	 * 例外：
	 * 日期：2009-10-19
	 * 修改日期， 修改人，修改原因，注释标志
	 */
	private void setUpSourceTs(SuperVO[] sourceItems,SuperVO[] targetItems){
		for (int i = 0; i < sourceItems.length; i++) {
			SuperVO sItem=sourceItems[i];
			
			//没有参照单据直接返回
			if(sItem.getAttributeValue("cupsourcebillrowid") == null
					|| StringUtil.isEmptyWithTrim(sItem.getAttributeValue("cupsourcebillrowid").toString())) 
				continue;
			
			//设置ts
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

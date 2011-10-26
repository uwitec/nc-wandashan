package nc.ui.ic.ic212;


import java.util.ArrayList;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ic.pub.bill.IButtonManager;
import nc.ui.ic.pub.bill.ICButtonConst;
import nc.ui.ic.pub.pf.ICSourceRefBaseDlg;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillData;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.constant.ic.BillMode;
import nc.vo.scm.constant.ic.InOutFlag;

/**
 * 材料出库单
 *
 *
 * 创建日期：(2001-11-23 15:39:43)
 * @author：张欣
 */
public class ClientUI extends nc.ui.ic.pub.bill.GeneralBillClientUI {
//	private ButtonObject m_boRatioOut ;
//	private ButtonObject m_boRefMR ;
	//配比出库对话框
	private RatioOUTStructureDlg ivjRatioOUTStructureDlg = null;

	private IButtonManager m_buttonManager;

/**
 * ClientUI2 构造子注解。
 */
public ClientUI() {
	super();
	initialize();
	//initRefWork();
}

/**
 * ClientUI 构造子注解。
 * add by liuzy 2007-12-18 根据节点编码初始化单据模版
 */
public ClientUI(FramePanel fp) {
 super(fp);
 initialize();
}

/**
 * ClientUI 构造子注解。
 * nc 2.2 提供的单据联查功能构造子。
 *
 */
public ClientUI(
	String pk_corp,
	String billType,
	String businessType,
	String operator,
	String billID) {
	super(pk_corp, billType, businessType, operator, billID);

}
/**
 * 李俊
 * 过滤工作中心和投料点参照
 *
 */
private void initRefWork(){

	String pkCalbody = ((UIRefPane)getBillCardPanel().getHeadItem("pk_calbody").getComponent()).getRefPK();
	UIRefPane uiRef = (UIRefPane)getBillCardPanel().getBodyItem("cworkcentername").getComponent();
	UIRefPane uiRefTL = (UIRefPane)getBillCardPanel().getBodyItem("cworksitename").getComponent();

	String whereWork = null;
	String whereTL = null;
	if (pkCalbody==null) whereWork = " and pd_wk.pk_corp="+"'"+getEnvironment().getCorpID()+"'";
	else whereWork = " and pd_wk.pk_corp="+"'"+getEnvironment().getCorpID()+"'"+" and pd_wk.gcbm="+"'"+pkCalbody+"'";

	if (pkCalbody==null) whereTL = " and pd_tld.pk_corp="+"'"+getEnvironment().getCorpID()+"'";
	else whereTL = " and pd_tld.pk_corp="+"'"+getEnvironment().getCorpID()+"'"+" and pd_tld.gcbm="+"'"+pkCalbody+"'";

	uiRef.getRefModel().setWherePart(uiRef.getRefModel().getWherePart()+whereWork);
	uiRefTL.getRefModel().setWherePart(uiRefTL.getRefModel().getWherePart()+whereTL);

}

/**
 * 创建者：王乃军
 * 功能：单据编辑后处理
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
protected void afterBillEdit(nc.ui.pub.bill.BillEditEvent e) {
//	nc.vo.scm.pub.SCMEnv.out("haha,bill edit/.");
}
/**
 * 创建者：王乃军
 * 功能：表体行列选择改变
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
protected void afterBillItemSelChg(int iRow, int iCol) {
	//nc.vo.scm.pub.SCMEnv.out("haha,sel chged!");

}
/**
 * 创建者：王乃军
 * 功能：单据编辑事件处理
 * 参数：e单据编辑事件
 * 返回：
 * 例外：
 * 日期：(2001-5-8 19:08:05)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */

public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
	//行，选中表头字段时为-1
	int row = e.getRow();
	//字段itemkey
	String sItemKey = e.getKey();
	//字段，位置 0: head     1:table
	int pos = e.getPos();

	if (pos == nc.ui.pub.bill.BillItem.BODY
		&& row < 0
		|| sItemKey == null
		|| sItemKey.length() == 0)
		return;
	//成本对象
	if (sItemKey.equals("ccostobjectname")) {
		String costobjectname = null;
		String costobjectid = null;
		if (getBillCardPanel().getBodyItem("ccostobjectname").getComponent() != null) {

			costobjectname =
				((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getBodyItem("ccostobjectname")
					.getComponent())
					.getRefName();
			costobjectid =
				((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getBodyItem("ccostobjectname")
					.getComponent())
					.getRefPK();
			//zhy 2005-03-31
			((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getBodyItem("ccostobjectname")
					.getComponent()).setPK(null);
		}

		//保存名称以在列表形式下显示。
		if (getM_voBill() != null) {
			getM_voBill().setItemValue(e.getRow(), "ccostobjectname", costobjectname);
			getM_voBill().setItemValue(e.getRow(), "ccostobject", costobjectid);
			getBillCardPanel().setBodyValueAt(
				costobjectname,
				e.getRow(),
				"ccostobjectname");
			getBillCardPanel().setBodyValueAt(costobjectid, e.getRow(), "ccostobject");

		}
	} else if (sItemKey.equals("cworkcentername")) {
		String cworkcentername = null;
		String cpkworkcenter = null;

		if (getBillCardPanel().getBodyItem("cworkcentername").getComponent() != null) {

			cworkcentername =
				((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getBodyItem("cworkcentername")
					.getComponent())
					.getRefName();
			cpkworkcenter =
				((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getBodyItem("cworkcentername")
					.getComponent())
					.getRefPK();
		}

		//保存名称以在列表形式下显示。
		if (getM_voBill() != null) {
			getM_voBill().setItemValue(e.getRow(), "cworkcentername", cworkcentername);
			getM_voBill().setItemValue(e.getRow(), "cworkcenterid", cpkworkcenter);
			getBillCardPanel().setBodyValueAt(
				cpkworkcenter,
				e.getRow(),
				"cpkworkcenter");
			getBillCardPanel().setBodyValueAt(
					cpkworkcenter,
					e.getRow(),
					"cworkcenterid");


		}
	} else if (sItemKey.equals("cworksitename")) {
		String cworksitename = null;
		String cpkworksite = null;

		if (getBillCardPanel().getBodyItem("cworksitename").getComponent() != null) {

			cworksitename =
				((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getBodyItem("cworksitename")
					.getComponent())
					.getRefName();
			cpkworksite  =
				((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getBodyItem("cpkworksite")
					.getComponent())
					.getRefPK();

		}

		//保存名称以在列表形式下显示。
		if (getM_voBill() != null) {
			getM_voBill().setItemValue(e.getRow(), "cworksitename", cworksitename);
			getM_voBill().setItemValue(e.getRow(), "cpkworksite", cpkworksite);
			getBillCardPanel().setBodyValueAt(
				cpkworksite,
				e.getRow(),
				"cpkworksite");

		}
	}
	else {
		super.afterEdit(e);
	}
	//过滤表体工作中心
	if (sItemKey.equals("pk_calbody")||sItemKey.equals("cwarehouseid")){
		initRefWork();
	}
}
/**
 * 创建者：王乃军
 * 功能：单据编辑事件处理
 * 参数：e单据编辑事件
 * 返回：
 * 例外：
 * 日期：(2001-5-8 19:08:05)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
public boolean beforeBillItemEdit(nc.ui.pub.bill.BillEditEvent e) {
	return true;
}
/**
 * 创建者：王乃军
 * 功能：表体行列选择改变
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
protected void beforeBillItemSelChg(int iRow, int iCol) {
	//nc.vo.scm.pub.SCMEnv.out("haha,before sel");

}
/**
  * 创建者：王乃军
  * 功能：抽象方法：保存前的VO检查
  * 参数：待保存单据
  * 返回：
  * 例外：
  * 日期：(2001-5-24 下午 5:17)
  * 修改日期，修改人，修改原因，注释标志：
  */
protected boolean checkVO(nc.vo.ic.pub.bill.GeneralBillVO voBill) {
	return super.checkVO();
}
/**
 * 返回 BillCardPanel1 特性值。
 * @return nc.ui.pub.bill.BillCardPanel
 */
/* 警告：此方法将重新生成。 */
protected nc.ui.pub.bill.BillCardPanel getBillCardPanel() {
	if (ivjBillCardPanel == null) {
		try {
			ivjBillCardPanel=super.getBillCardPanel();
			BillData bd=ivjBillCardPanel.getBillData();
				//表体工作中心参照
			if (bd.getBodyItem("cworkcentername") != null) {
				nc.ui.pub.beans.UIRefPane ref1 = new nc.ui.pub.beans.UIRefPane();
				ref1.setIsCustomDefined(true);
//				修改人：刘家清 修改时间：2008-6-3 上午11:07:21 修改原因：根据公司过滤工作中心。
				//WkRefModel model1 = new WkRefModel();
				WkRefModel model1 = new WkRefModel(getEnvironment().getCorpID());

				ref1.setRefModel(model1);


				bd.getBodyItem("cworkcentername").setComponent(ref1); //

			}
			//表体投料点参照
			if (bd.getBodyItem("cworksitename") != null) {
				nc.ui.pub.beans.UIRefPane ref1 = new nc.ui.pub.beans.UIRefPane();
				ref1.setIsCustomDefined(true);

				TldRefModel model1 = new TldRefModel();

				ref1.setRefModel(model1);
				bd.getBodyItem("cworksitename").setComponent(ref1); //

			}
			ivjBillCardPanel.setBillData(bd);
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBillCardPanel;
}
/**
 * 此处插入方法说明。
 * 功能描述:
 * 作者：王乃军
 * 输入参数:
 * 返回值:
 * 异常处理:
 * 日期:(2003-6-25 20:43:17)
 * @return java.util.ArrayList
 */
protected ArrayList getFormulaItemBody() {
	ArrayList arylistItemField = super.getFormulaItemBody();
	if (arylistItemField != null) {
		//工作中心 new added by zhx
		String[] aryItemField20 = new String[] { "gzzxmc", "cworkcentername", "cworkcenterid" };
		arylistItemField.add(aryItemField20);
		//投料点 new added by zhx
		String[] aryItemField21 = new String[] { "tldmc", "cworksitename", "cworksiteid" };
		arylistItemField.add(aryItemField21);
	}

	//对应入库单类型 ccorrespondtype
	//String[] aryItemField20 =
	//new String[] { "billtypename", "cfirsttypeName", "cfirsttype" };
	//arylistItemField.add(aryItemField20);
	return arylistItemField;
}
/**
 * 返回 FormMemoDlg1 特性值。
 * @return nc.ui.ic.ic211.FormMemoDlg
 */
/* 警告：此方法将重新生成。 */
private RatioOUTStructureDlg getRatioOUTDlg() {
	if (ivjRatioOUTStructureDlg == null) {
		try {
			ivjRatioOUTStructureDlg = new nc.ui.ic.ic212.RatioOUTStructureDlg(this,super.m_ScaleValue.getNumScale());
			ivjRatioOUTStructureDlg.setName("RatioOUTStructureDlg");
			//ivjFormMemoDlg1.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			// user code begin {1}
			//ivjFormMemoDlg1.setParent(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRatioOUTStructureDlg;
}

/**
 * 创建者：王乃军
 * 功能：初始化系统参数
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
protected void initPanel() {
	//需要单据参照
	super.setNeedBillRef(false);
}

public String getBillType() {
	return nc.vo.ic.pub.BillTypeConst.m_materialOut;
}

public String getFunctionNode() {
	return "40080804";
}

public int getInOutFlag() {
	return InOutFlag.OUT;
}

protected class ButtonManager212 extends nc.ui.ic.pub.bill.GeneralButtonManager {

	public ButtonManager212(GeneralBillClientUI clientUI) throws BusinessException {
		super(clientUI);
	}

	/**
	 * 子类实现该方法，响应按钮事件。
	 * @version (00-6-1 10:32:59)
	 *
	 * @param bo ButtonObject
	 */
	public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
		if (bo == getButtonManager().getButton(ICButtonConst.BTN_BILL_RATIO_OUT)){
			//处理"形成待管"
			onRatioOut();
		}else if(bo == getButtonManager().getButton(ICButtonConst.BTN_BILL_REF_MR)){
			bo.setTag("422X:");
      ICSourceRefBaseDlg.childButtonClicked(bo, getEnvironment().getCorpID(),getFunctionNode(), getEnvironment().getUserID(), getBillType(), getClientUI());
			if (ICSourceRefBaseDlg.isCloseOK()) {
				nc.vo.pub.AggregatedValueObject[] vos = ICSourceRefBaseDlg.getRetsVos();
				onAddToOrder(vos);
			}
		}else{
			super.onButtonClicked(bo);
		}
	}
	
	protected void onUpdate() {
		super.onUpdate();
		if(!"1002".equalsIgnoreCase(getCorpPrimaryKey())){
			getBillCardPanel().getHeadItem("cdptid").setEnabled(false);
		}else{
			getBillCardPanel().getHeadItem("vuserdef5").setEnabled(false);
		}
	}

	/**
	 * 配比出库按钮点击事件的响应方法
	 * 创建者：张欣
	 * 功能：
	 * 参数：Void
	 * 返回：Void
	 * 例外：
	 * 日期：(2001-5-10 11:04:00)
	 * 修改日期，修改人，修改原因，注释标志：
	 *
	 */
	private void onRatioOut() {

		if (getEnvironment().getCorpID() == null || getEnvironment().getUserID() == null || getEnvironment().getLogDate() == null) {

			nc.vo.scm.pub.SCMEnv.out("公司ID，操作员ID，登陆日期为空！");
			return;
		}
			ivjRatioOUTStructureDlg=null;

			//getRatioOUTDlg().initialize();
			getRatioOUTDlg().setParams(getEnvironment().getCorpID(), getEnvironment().getCorpID(), getEnvironment().getUserID(), getEnvironment().getUserName(), getEnvironment().getLogDate());
			getRatioOUTDlg().showModal();
	}

	private void onAddToOrder(AggregatedValueObject[] vos) {
		
		if (vos == null) {
			return;
		}
		try {
			if(vos.length<=1){
        setRefBillsFlag(false);
				setBillRefResultVO(null, vos);
			}else{
//      修改人：刘家清 修改日期：2007-04-29 修改原因：参照资需求申请生成材料出库单后，表单状态不对，应该把是否参照生成多张单据设置成是
        setRefBillsFlag(true);
				setBillRefMultiVOs(null,(GeneralBillVO[])vos);
			}
		} catch (Exception e) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000297")/*@res "发生错误:"*/ + e.getMessage());
		}
	}
}

public IButtonManager getButtonManager() {
	if (m_buttonManager == null) {
		try {
			m_buttonManager = new ButtonManager212(this);
		} catch (BusinessException e) {
			//日志异常
			nc.vo.scm.pub.SCMEnv.error(e);
			showErrorMessage(e.getMessage());
		}
	}
	return m_buttonManager;
}

/**
 * 李俊
 * 功能：查询一次配比出库的单据
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2005-1-4 17:56:45)
 * 修改日期，修改人，修改原因，注释标志：
 *
 */
public void qryRationOutBill(ArrayList alHIDs) {
	try {
		nc.vo.ic.pub.bill.Timer timer = new nc.vo.ic.pub.bill.Timer();
		timer.start(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000277")/*@res "@@查询开始："*/);
		StringBuffer sbSql = new StringBuffer("head.cgeneralhid in (");
		if (alHIDs==null||alHIDs.size()<=0) return;
		int size = alHIDs.size();
		for (int i=0;i<size;i++){
			sbSql.append("'");
	        sbSql.append(alHIDs.get(i));
	        sbSql.append("'");
	        if (i!=size-1)
	        sbSql.append(",");
	     }
		sbSql.append(")");
		//获得qrycontionVO的构造
		QryConditionVO voCond = new QryConditionVO(sbSql.toString());
		if (m_bIsByFormula) {
			voCond.setIntParam(0, GeneralBillVO.QRY_HEAD_ONLY_PURE);
		} else {
			voCond.setIntParam(0, GeneralBillVO.QRY_HEAD_ONLY);
		}
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000250")/*@res "正在查询，请稍候..."*/);
		timer.showExecuteTime("Before 查询：：");/*-=notranslate=-*/
		ArrayList<GeneralBillVO> alListData = GeneralBillHelper.queryBills(getBillType(),voCond);
		timer.showExecuteTime("查询时间：");/*-=notranslate=-*/

		if (m_bIsByFormula) {
			setAlistDataByFormula(GeneralBillVO.QRY_FIRST_ITEM_NUM, alListData);
		 }

		if (alListData != null && alListData.size() > 0) {
			setM_alListData(alListData);
			setListHeadData();
			if (nc.vo.scm.constant.ic.BillMode.Card == getM_iCurPanel())
				getButtonManager().onButtonClicked(
						getButtonManager().getButton(
								ICButtonConst.BTN_SWITCH));

			selectListBill(0);
			//设置当前的单据数量/序号，用于按钮控制
			setM_iLastSelListHeadRow(0);
			//初始化当前单据序号，切换时用到！！！不宜主动设置表单的数据。
			m_iCurDispBillNum = -1;
			//当前单据数
			m_iBillQty = getM_alListData().size();

			if (m_iBillQty > 0)
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000290",null,new String[]{(new Integer(m_iBillQty)).toString()})/*@res "共查到{0}张单据！"*/);
			else
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000243")/*@res "未查到符合条件的单据。"*/);

		} else {
			dealNoData();
		}
		setM_iMode(BillMode.Browse);
		setButtonStatus(true);
	} catch (Exception e) {
		handleException(e);
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000251")/*@res "查询出错。"*/);
		showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000252")/*@res "查询出错："*/ + e.getMessage());
	}

}
/**
 * 创建者：王乃军
 * 功能：在列表方式下选择一张单据
 * 参数： 单据在alListData中的索引
 * 返回：无
 * 例外：
 * 日期：(2001-11-23 18:11:18)
 *  修改日期，修改人，修改原因，注释标志：
 */
protected void selectBillOnListPanel(int iBillIndex) {}
/**
 * 创建者：王乃军
 * 功能：抽象方法：设置按钮状态，在setButtonStatus中调用。
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
protected void setButtonsStatus(int iBillMode) {
  //modified by liuzy 2007-11-23 “配比出库”按钮在编辑状态下不可用
  switch(iBillMode){
    case BillMode.New:
    case BillMode.Update:
      getButtonManager().getButton(ICButtonConst.BTN_BILL_RATIO_OUT).setEnabled(false);
      break;
      default:
        getButtonManager().getButton(ICButtonConst.BTN_BILL_RATIO_OUT).setEnabled(true);
  }
//modified by liuzy 2007-11-23 “配比出库”按钮在编辑状态下不可用
//	//浏览模式下，有单据并且已经签字才可用
//  getButtonManager().getButton(ICButtonConst.BTN_BILL_RATIO_OUT).setEnabled(true);
	//需要重新设置按钮以刷新子类按钮的状态。
	//super.initButtonsData();
	//m_vBillMngMenu.addElement(m_boRatioOut);
	//super.setButtons();
}
}
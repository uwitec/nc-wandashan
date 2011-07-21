package nc.ui.ic.pub.lot;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.hg.ic.pub.HgICPubHealper;
import nc.ui.ic.ic001.BatchCodeDefSetTool;
import nc.ui.ic.ic001.BatchCodeDlg;
import nc.ui.ic.pub.ICCommonBusi;
import nc.ui.ic.pub.bill.ClientUISortCtl;
import nc.ui.ic.pub.bill.GeneralBillUICtl;
import nc.ui.ic.pub.bill.SortClientUI;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.vo.ic.ic700.IntList;
import nc.vo.ic.pub.SmartVOUtilExt;
import nc.vo.ic.pub.check.CheckTools;
import nc.vo.ic.pub.lot.LotNumbRefVO;
import nc.vo.pub.bill.BillTempletBodyVO;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.formulaset.util.StringUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.pub.smart.SmartFieldMeta;

/**
 * 此处插入类型说明。
 * 创建者：Zhang Xin
 * 创建日期：2001-05-08
 * 功能：
 * 修改日期，修改人，修改原因，注释标志：
 */
public class LotNumbDlg
  extends UIDialog
  implements BillEditListener,nc.ui.pub.bill.BillTableMouseListener,SortClientUI {
  private javax.swing.JPanel ivjUIDialogContentPane = null;
  private UIButton ivjBtnCancel = null;
  private UIButton ivjBtnOK = null;
  private UIButton ivjBtnQueryAll = null;
  private UIButton ivjBtnRefresh = null;
  private UIButton ivjBtnNew = null;
  private UIButton ivjBtnEdit = null;
  
  //private UILabel ivjLbFreeItem1 = null;
  //private UITextField ivjTfFreeItem1 = null;
  IvjEventHandler ivjEventHandler = new IvjEventHandler();
  
  //
  private ArrayList m_alAllData = null;
  protected ArrayList m_alFreeItemValue = null; //自由项值
  //是否多选，目前只应用库存
  private boolean m_bIsMutiSel = false;
  
  
  //修改人：刘家清 修改日期：2007-9-26下午01:45:36 修改原因：批次参照界面表体是否能多选
  private boolean m_bIsBodyMutiSel = true;
  //1:先入后出，2:按保质期顺序。

  private boolean m_bIsQureyZeroLot = false; //是否查询零结存批次号。
  protected boolean m_bIsTrackedBill = false; //出库是否跟踪入库
  //使用者传入的参数
  /** 是否废品库管理 */
  protected boolean m_bisWasteWH = false;
  private Hashtable m_hashLotNumbVO = null; //存放批次号对应VO的哈希表
  private nc.vo.scm.ic.bill.InvVO m_invvo = null;
  protected Integer m_iOutPriority = null; //出库优先顺序0：先入先出
  protected Integer m_isTrackedBillflag = null; //是否跟踪到单据
  private LotNumbRefVO m_lnrvoSelVO = null; //存放用户选择批次号的LotNumbRefVO
  private Object m_params[]= null;
  protected String m_strAssistMeaUnitID = null; //辅计量ID
  private String m_strBillBodyID = null; //单据表体ID
  private String m_strBillCode = null; //单据号
  private String m_strBillHeaderID = null; //单据表头ID
  private String m_strBillType = null; //单据类型
  protected String m_strCalbodyName = null; //库存组织名称
  protected String m_strCorpID = null; //公司ID
  protected String m_strFreeItem = null;
  protected String m_strInvCode = null; //存货编码
  protected String m_strInvID = null; //存货ID
  protected String m_strInvName = null; //存货名称
  protected String m_strInvSpec = null; //规格
  protected String m_strInvType = null; //型号
  //
  //传出的参数
  private String m_strLotNumb = null; //批次号
  protected String m_strMeasUnit = null; //主计量单位
  protected String m_strNowBillHid = null;
  protected String m_strPk_calbody = null; //库存组织ID
  protected String m_strWareHouseCode = null; //仓库编码
  protected String m_strWareHouseID = null; //仓库ID
  protected String m_strWareHouseName = null; //仓库名称
  private UFDouble m_udInvAssistQty = null; //本批次结存辅数量
  private UFDouble m_udInvQty = null; //本批次结存实数量
  private UFDate m_ufdValidate = null; //失效日期
  
  private BatchCodeDlg m_BatchCodeDlg = null;
  //
  private String m_strNowSrcBid = null;
  
  private ClientLink m_cl=null;
  
  private BillCardPanel m_card=null;
  private String m_sRNodeName="400120";
  private String m_sRNodeKey="batchcode";
  
  private ClientUISortCtl m_cardBodySortCtl;//卡片表体排序控制
  
  /**
   * get
   * 创建日期：(2001-10-26 14:31:14)
   * @param key java.lang.String
   */
  protected ClientUISortCtl getCardBodySortCtl() {
    return m_cardBodySortCtl;
  }
  
    
  public BillListPanel getBillListPanel(){
    return null;
  }
  
  public void beforeSortEvent(boolean iscard,boolean ishead,String key){
    if(m_alAllData!=null && m_alAllData.size()>0){
      getCardBodySortCtl().addRelaSortData(m_alAllData);
    }
  }
  
  public void afterSortEvent(boolean iscard,boolean ishead,String key){
    if(m_alAllData!=null && m_alAllData.size()>0){
      m_alAllData = (ArrayList)getCardBodySortCtl().getRelaSortData(0);
    }
  }
  
  class IvjEventHandler implements java.awt.event.ActionListener {
    public void actionPerformed(java.awt.event.ActionEvent e) {
      if (e.getSource() == LotNumbDlg.this.getBtnOK())
        onOK();
      if (e.getSource() == LotNumbDlg.this.getBtnCancel())
        onCancel();
      if (e.getSource() == LotNumbDlg.this.getBtnRefresh())
        onRefresh();
      if (e.getSource() == LotNumbDlg.this.getBtnQueryAll())
        onQueryAll();
      if (e.getSource() == LotNumbDlg.this.getBtnNew())
        onNew();
      if (e.getSource() == LotNumbDlg.this.getBtnEdit())
        onEdit();
    };
  };
/**
 * LotNumbDlg 构造子注解。
 */
public LotNumbDlg() {
  super();
  initialize();
}
/**
 * LotNumbDlg 构造子注解。
 * @param parent java.awt.Container
 */
public LotNumbDlg(java.awt.Container parent) {
  super(parent);
  initialize();
}
/**
 * LotNumbDlg 构造子注解。
 * @param parent java.awt.Container
 * @param title java.lang.String
 */
public LotNumbDlg(java.awt.Container parent, String title) {
  super(parent, title);
  initialize();
}
/**
 * LotNumbDlg 构造子注解。
 * @param owner java.awt.Frame
 */
public LotNumbDlg(java.awt.Frame owner) {
  super(owner);
  initialize();
}
/**
 * LotNumbDlg 构造子注解。
 * @param owner java.awt.Frame
 * @param title java.lang.String
 */
public LotNumbDlg(java.awt.Frame owner, String title) {
  super(owner, title);
  initialize();
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-5-8 20:47:36)
 * @return java.lang.String
 */
public String getAssQuant() {
  return null;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-5-8 20:48:20)
 * @return java.lang.String
 */
public String getAssUnit() {
  if(getSelVOs() != null && getSelVOs()[0] != null)
    return getSelVOs()[0].getCastunitid();
  else
    return null;
}






/**
 * 返回 BtnCancel 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getBtnCancel() {
  if (ivjBtnCancel == null) {
    try {
      ivjBtnCancel = new nc.ui.pub.beans.UIButton();
      ivjBtnCancel.setName("BtnCancel");
      ivjBtnCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000008")/*@res "取消"*/);
      ivjBtnCancel.setSize(70, 21);
    } catch (java.lang.Throwable ivjExc) {
      handleException(ivjExc);
    }
  }
  return ivjBtnCancel;
}


/**
 * 返回 BtnOK 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getBtnOK() {
  if (ivjBtnOK == null) {
    try {
      ivjBtnOK = new nc.ui.pub.beans.UIButton();
      ivjBtnOK.setName("BtnOK");
      ivjBtnOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000044")/*@res "确定"*/);
      ivjBtnOK.setSize(70, 21);
    } catch (java.lang.Throwable ivjExc) {
      handleException(ivjExc);
    }
  }
  return ivjBtnOK;
}



/**
 * 返回 LbFreeItem1 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
//private nc.ui.pub.beans.UILabel getLbFreeItem1() {
//  if (ivjLbFreeItem1 == null) {
//    try {
//      ivjLbFreeItem1 = new nc.ui.pub.beans.UILabel();
//      ivjLbFreeItem1.setName("LbFreeItem1");
//      ivjLbFreeItem1.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008other","UPP4008other-000447")/*@res "自  由  项"*/);
//      ivjLbFreeItem1.setBounds(350, 30, 80, 22);
//      ivjLbFreeItem1.setVisible(!m_bIsMutiSel);
//    } catch (java.lang.Throwable ivjExc) {
//      handleException(ivjExc);
//    }
//  }
//  return ivjLbFreeItem1;
//}


/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public String getLotNumb() {

  m_strLotNumb = m_lnrvoSelVO==null? null:m_lnrvoSelVO.getVbatchcode();
  return m_strLotNumb;

}



/**
 * 返回 TfFreeItem1 特性值。
 * @return nc.ui.pub.beans.UITextField
 */
/* 警告：此方法将重新生成。 */
//private nc.ui.pub.beans.UITextField getTfFreeItem1() {
//  if (ivjTfFreeItem1 == null) {
//    try {
//      ivjTfFreeItem1 = new nc.ui.pub.beans.UITextField();
//      ivjTfFreeItem1.setName("TfFreeItem1");
//      ivjTfFreeItem1.setPreferredSize(new java.awt.Dimension(100, 20));
//      ivjTfFreeItem1.setBounds(450, 30, 250, 20);
//      ivjTfFreeItem1.setMaxLength(250);
//      ivjTfFreeItem1.setVisible(!m_bIsMutiSel);
//    } catch (java.lang.Throwable ivjExc) {
//      handleException(ivjExc);
//    }
//  }
//  return ivjTfFreeItem1;
//}


/**
 * 返回 UIDialogContentPane 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JPanel getUIDialogContentPane() {
  if (ivjUIDialogContentPane == null) {
    try {
      ivjUIDialogContentPane = new javax.swing.JPanel();
      ivjUIDialogContentPane.setName("UIDialogContentPane");
      ivjUIDialogContentPane.setLayout(new BorderLayout());
      UIPanel cmdpane = new UIPanel();
      cmdpane.setLayout(new FlowLayout());
      
      cmdpane.add(getBtnQueryAll(), getBtnQueryAll().getName());
      cmdpane.add(getBtnOK(), getBtnOK().getName());
      cmdpane.add(getBtnCancel(), getBtnCancel().getName());
      cmdpane.add(getBtnRefresh(), getBtnRefresh().getName());
      cmdpane.add(getBtnNew(), getBtnNew().getName());
      cmdpane.add(getBtnEdit(), getBtnEdit().getName());
      
      getUIDialogContentPane().add(getBillCardPanel(), BorderLayout.CENTER);
      getUIDialogContentPane().add(cmdpane, BorderLayout.SOUTH);
      
    } catch (java.lang.Throwable ivjExc) {
      handleException(ivjExc);
    }
  }
  return ivjUIDialogContentPane;
}


/**
 * 每当部件抛出异常时被调用
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

  /* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
  // nc.vo.scm.pub.SCMEnv.out("--------- 未捕捉到的异常 ---------");
  // nc.vo.scm.pub.SCMEnv.error(exception);
}
/**
 * 初始化连接
 * @exception java.lang.Exception 异常说明。
 */
/* 警告：此方法将重新生成。 */
private void initConnections() throws java.lang.Exception {
  getBtnQueryAll().addActionListener(ivjEventHandler);
  getBtnOK().addActionListener(ivjEventHandler);
  getBtnCancel().addActionListener(ivjEventHandler);
  getBtnRefresh().addActionListener(ivjEventHandler);
  getBtnNew().addActionListener(ivjEventHandler);
  getBtnEdit().addActionListener(ivjEventHandler);
}

/**
 * 初始化类。
 */
/* 警告：此方法将重新生成。 */
private void initialize() {
  try {
    setModal(true);
    setName("LotNumbDlg");
    setSize(880, 394);
//    setResizable(false);
    setContentPane(getUIDialogContentPane());
    initConnections();
    m_cardBodySortCtl = new ClientUISortCtl(this,true,BillItem.BODY);
    GeneralBillUICtl.setBillCardPaneSelectMode(getBillCardPanel());
  } catch (java.lang.Throwable ivjExc) {
    handleException(ivjExc);
  }
}


/**
 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
  try {
    LotNumbDlg aLotNumbDlg = null;
    aLotNumbDlg = new LotNumbDlg();
    aLotNumbDlg.setModal(true);
    aLotNumbDlg.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent e) {
        System.exit(0);
      };
    });
    aLotNumbDlg.show();
    java.awt.Insets insets = aLotNumbDlg.getInsets();
    aLotNumbDlg.setSize(aLotNumbDlg.getWidth() + insets.left + insets.right, aLotNumbDlg.getHeight() + insets.top + insets.bottom);
    aLotNumbDlg.setVisible(true);
  } catch (Throwable exception) {
    nc.vo.scm.pub.SCMEnv.out("nc.ui.pub.beans.UIDialog 的 main() 中发生异常");
    nc.vo.scm.pub.SCMEnv.error(exception);
  }
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-5-8 18:27:15)
 */
public void onCancel() {
  //this.setVisible(false);
  //aLotNumbDlg.closeCancel();
  m_lnrvoSelVO = null;
  closeCancel();


  }
/**
 * 此处插入方法说明。
 * 创建日期：(2001-5-8 18:27:04)
 */
public void onOK() {
  //this.setVisible(false);
  int[] selrows = getSelectRows();
  if (selrows == null || selrows.length<=0) {
    onCancel();
  } else {
    closeOK();
    getSelVO();
    getLotNumb();
    getValidate();

    getBillBodyID();
    getBillHeaderID();
    getBillCode();
    getBillType();
  }
}

private void onNew(){
  String pk_invmandoc = getInvID();
  getBatchCodeDlg().setData(pk_invmandoc,null);
  if(getBatchCodeDlg().showModal()==UIDialog.ID_CANCEL)
    return;
  
  //重新刷新界面
  onRefresh();
}

private void onEdit(){
  int[] selrows = getSelectRows(); 
  if(selrows==null || selrows.length<=0)
    return;
  String pk_invmandoc = getInvID();
  LotNumbRefVO vosel = (LotNumbRefVO)m_alAllData.get(selrows[0]);
  String vbatchcode = vosel.getVbatchcode();
  getBatchCodeDlg().setData(pk_invmandoc,vbatchcode);
  if(getBatchCodeDlg().showModal()==UIDialog.ID_CANCEL)
    return;
  onRefresh();
}

/**
 * 在表格中显示数据。
 * 创建日期：(2001-5-8 20:50:59)
 */
public void setVOtoBody() {
//  LotNumbRefVO[] voaAllData = null;

//  if (isTrackedBill()) {
//    getUITablePane1().getTable().setModel(getTrackedTableModel());
//    voaAllData = getTrackedTableModel().getAllData();
//
//  } else {
//    getUITablePane1().getTable().setModel(getNotTrackedTableModel());
//    voaAllData = getNotTrackedTableModel().getAllData();
//  }
//  getUITablePane1().getTable().setModel(getBillCardPanel().getBillModel());
  
  //test注释
//  voaAllData = (LotNumbRefVO[])getBillCardPanel().getBillModel().getBodyValueVOs(LotNumbRefVO.class.getName());
//  
//  if (voaAllData != null && voaAllData.length > 0) {
//    getUITablePane1().getTable().setRowSelectionInterval(0, 0);
//    setSelVO(voaAllData[0]);
//  }

}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-5-8 20:50:45)
 */
public void setVOtoHeader() {
  BillItem bi = getBillCardPanel().getHeadItem("cwarehousename");
  if(bi!=null){
    if (m_strWareHouseID != null) {
      bi.getCaptionLabel().setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008other","UPP4008other-000448")/*@res "仓    库"*/);
      bi.setValue(
          getWareHouseCode() == null
            ? getWareHouseName()
            : (getWareHouseCode() + ", " + getWareHouseName()));
    } else {
      bi.getCaptionLabel().setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0001825")/*@res "库存组织"*/);
      bi.setValue(getCalbodyName());
    }
  }
  
  bi = getBillCardPanel().getHeadItem("invname");
  if(bi!=null){
    bi.setValue(
      (getInvCode() == null? "": (getInvCode() + ", ")) +
      (getInvName() == null? "": (getInvName() + ", ")) +
      (getInvSpec() == null? "": (getInvSpec() + ", ")) +
      (getInvType() == null? "": getInvType()));
  }
  bi = getBillCardPanel().getHeadItem("cunitname");
  if(bi!=null)
    bi.setValue(getMeasUnit() == null ? "" : getMeasUnit());
  
  bi = getBillCardPanel().getHeadItem("vfree0");
  if(bi!=null){
    if (m_strFreeItem != null && m_strFreeItem.trim().length() > 0) {
      bi.setValue(m_strFreeItem.trim());
    } else {
      bi.setValue("");
    }
  }
  return;

}

  //private UILabel ivjLbWareHouseID = null;




/**
 * 返回 LbWareHouseID 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
//private nc.ui.pub.beans.UILabel getLbWareHouseID() {
//  if (ivjLbWareHouseID == null) {
//    try {
//      ivjLbWareHouseID = new nc.ui.pub.beans.UILabel();
//      ivjLbWareHouseID.setName("LbWareHouseID");
//      ivjLbWareHouseID.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0000153")/*@res "仓库"*/);
//      ivjLbWareHouseID.setBounds(0, 4, 80, 22);
//    } catch (java.lang.Throwable ivjExc) {
//      handleException(ivjExc);
//    }
//  }
//  return ivjLbWareHouseID;
//}


/**
 * 创建人：刘家清
创建日期：2007-9-26下午02:10:22
创建原因：
 * LotNumbDlg 构造子注解。
 * @param parent java.awt.Container
 */
public LotNumbDlg(java.awt.Container parent,boolean isMutiSel,boolean IsBodyMutiSel) {
  super(parent);
  m_bIsMutiSel = isMutiSel;
  m_bIsBodyMutiSel = IsBodyMutiSel;
  initialize();
}

/**
 * LotNumbDlg 构造子注解。
 * @param parent java.awt.Container
 */
public LotNumbDlg(java.awt.Container parent,boolean isMutiSel) {
  super(parent);
  m_bIsMutiSel = isMutiSel;
  initialize();
}

/**
 * LotNumbDlg 构造子注解。
 */
public LotNumbDlg(boolean isMutiSel) {
  super();
  m_bIsMutiSel = isMutiSel;
  initialize();
}

/**
 * 根据传入本张单据表头主键,从alAllData中将入库单据表头主键与之相同的记录行过滤.
 * 创建日期：(2003-01-05 9:43:01)
 */
private ArrayList filterNowBill(ArrayList alAllData, String sNowBillHid) {
  int place = -1;
  if (isTrackedBill() && m_strNowBillHid != null && m_strNowBillHid.trim().length() > 0) {
    for (int i = 0; i < alAllData.size(); i++) {
      String sHid = ((nc.vo.ic.pub.lot.LotNumbRefVO) alAllData.get(i)).getCgeneralhid();
      if (sNowBillHid.equalsIgnoreCase(sHid)) {
        place = i;
        break;

      }
    }
    if (place != -1 && (place >= 0 || place < alAllData.size())) {
      alAllData.remove(place);
    }
  }
  return alAllData;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-7-12 22:27:08)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getAssistMeaUnitID() {
  return m_strAssistMeaUnitID;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getBillBodyID() {

  m_strBillBodyID = m_lnrvoSelVO==null? null:m_lnrvoSelVO.getCgeneralbid();
  return m_strBillBodyID;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getBillCode() {

  m_strBillCode = m_lnrvoSelVO==null? null:m_lnrvoSelVO.getVbillcode();
  return m_strBillCode;

}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getBillHeaderID() {

  m_strBillHeaderID = m_lnrvoSelVO==null? null:m_lnrvoSelVO.getCgeneralhid();
  return m_strBillHeaderID;

}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getBillType() {

  m_strBillType = m_lnrvoSelVO==null? null:m_lnrvoSelVO.getCbilltypecode();
  return m_strBillType;

}

/**
 * 返回 BtnRefresh 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getBtnQueryAll() {
  if (ivjBtnQueryAll == null) {
    try {
      ivjBtnQueryAll = new nc.ui.pub.beans.UIButton();
      ivjBtnQueryAll.setName("BtnQueryAll");
      ivjBtnQueryAll.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008other","UPP4008other-000449")/*@res "所有批次"*/);
      ivjBtnQueryAll.setSize(70, 21);
      ivjBtnQueryAll.setVisible(m_bIsMutiSel);
    } catch (java.lang.Throwable ivjExc) {
      handleException(ivjExc);
    }
  }

  return ivjBtnQueryAll;
}

private nc.ui.pub.beans.UIButton getBtnNew() {
  if (ivjBtnNew == null) {
    try {
      ivjBtnNew = new nc.ui.pub.beans.UIButton();
      ivjBtnNew.setName("BtnNew");
      ivjBtnNew.setText("新增");
      ivjBtnNew.setSize(70, 21);
      ivjBtnNew.setVisible(m_bIsMutiSel);
    } catch (java.lang.Throwable ivjExc) {
      handleException(ivjExc);
    }
  }

  return ivjBtnNew;
}
private nc.ui.pub.beans.UIButton getBtnEdit() {
  if (ivjBtnEdit == null) {
    try {
      ivjBtnEdit = new nc.ui.pub.beans.UIButton();
      ivjBtnEdit.setName("BtnEdit");
      ivjBtnEdit.setText("修改");
      ivjBtnEdit.setSize(70, 21);
      ivjBtnEdit.setVisible(m_bIsMutiSel);
    } catch (java.lang.Throwable ivjExc) {
      handleException(ivjExc);
    }
  }

  return ivjBtnEdit;
}

/**
 * 返回 BtnRefresh 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getBtnRefresh() {
  if (ivjBtnRefresh == null) {
    try {
      ivjBtnRefresh = new nc.ui.pub.beans.UIButton();
      ivjBtnRefresh.setName("BtnRefresh");
      ivjBtnRefresh.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000009")/*@res "刷新"*/);
      ivjBtnRefresh.setSize(70, 21);
    } catch (java.lang.Throwable ivjExc) {
      handleException(ivjExc);
    }
  }
  return ivjBtnRefresh;
}

/**
 * 此处插入方法说明。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-06-11 15:24:57)
 * 修改日期，修改人，修改原因，注释标志：
 * @return java.lang.String
 */
public java.lang.String getCalbodyName() {
  return m_strCalbodyName;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-7-12 22:27:08)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getCorpID() {
  if(m_strCorpID==null)
    m_strCorpID = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
  return m_strCorpID;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-12 11:21:14)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.util.ArrayList
 */
public ArrayList getFreeItemValue() {
  return m_alFreeItemValue;
}

/**
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2005-1-5 9:25:48)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return nc.vo.scm.ic.bill.FreeVO
 */
public nc.vo.scm.ic.bill.FreeVO getFreeVO() {
  if(getSelVOs() != null && getSelVOs()[0] != null)
    return getSelVOs()[0].getFreeVO();
  else
    return null;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-13 20:25:05)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.util.Hashtable
 */
public java.util.Hashtable getHTLotNumbVO() {
  return m_hashLotNumbVO;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-15 16:43:27)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return nc.vo.pub.lang.UFDouble
 */
public UFDouble getInvAssistQty() {

    m_udInvAssistQty = m_lnrvoSelVO==null? null:m_lnrvoSelVO.getNinassistnum();
    return m_udInvAssistQty;

}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getInvCode() {
  return m_strInvCode;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getInvID() {
  return m_strInvID;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getInvName() {
  return m_strInvName;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-15 16:39:35)
 * 修改日期，修改人，修改原因，注释标志：
 *
 */
public UFDouble getInvQty() {

  m_udInvQty = m_lnrvoSelVO==null? null:m_lnrvoSelVO.getNinnum();
  return m_udInvQty;

  }

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getInvSpec() {
  return m_strInvSpec;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getInvType() {
  return m_strInvType;
}


/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getMeasUnit() {
  return m_strMeasUnit;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-4 16:40:05)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return javax.swing.table.TableModel
 */
//public NotTrackedTableModel getNotTrackedTableModel() {
//  if (m_tmNotTracked == null)
//    m_tmNotTracked = new NotTrackedTableModel(getBCBillData());
//  return m_tmNotTracked;
//
//}

/**
 * 此处插入方法说明。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-05-21 11:35:07)
 * 修改日期，修改人，修改原因，注释标志：
 * @return java.lang.Integer
 */
protected java.lang.Integer getOutPriority() {
  return m_iOutPriority;
}

/**
 * 此处插入方法说明。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-06-11 15:24:57)
 * 修改日期，修改人，修改原因，注释标志：
 * @return java.lang.String
 */
public java.lang.String getPk_calbody() {
  return m_strPk_calbody;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-22 8:59:58)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return nc.vo.ic.pub.lot.LotNumbRefVO
 */
public LotNumbRefVO getSelVO() {
  if(getResult()!=UIDialog.ID_OK)
    return null;
  int[] sels = getSelectRows();
  if(sels==null || sels.length<=0){
    m_lnrvoSelVO = null;
    return m_lnrvoSelVO;
  }
  int selrow = sels[0];
  if (selrow >= 0 ) {
    m_lnrvoSelVO=(LotNumbRefVO)getBillCardPanel().getBillModel().getBodyValueRowVO(selrow, LotNumbRefVO.class.getName());
  }
  return m_lnrvoSelVO;
}

/**
 * 功能：返回选中的批次VO
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2005-1-4 18:26:40)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return nc.vo.ic.pub.lot.LotNumbRefVO[]
 */
public LotNumbRefVO[] getSelVOs() {
  if(getResult()!=UIDialog.ID_OK)
    return null;
  LotNumbRefVO[] voRet = null;
  int[] isel = getSelectRows();
  voRet = new LotNumbRefVO[isel.length];
  for(int i=0;i<isel.length;i++){
    voRet[i] = (LotNumbRefVO)m_alAllData.get(isel[i]);
  }
  return voRet;
}

public void setIsOk(boolean bok) {
  setResult(bok?UIDialog.ID_OK:UIDialog.ID_CANCEL);
}



/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-11 11:31:45)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public String getTitle() {
  return nc.ui.ml.NCLangRes.getInstance().getStrByID("4008other","UPP4008other-000452")/*@res "批次号参照"*/;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-4 16:40:05)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return javax.swing.table.TableModel
 */
//public TrackedTableModel getTrackedTableModel() {
//  if (m_tmTracked == null)
//    m_tmTracked = new TrackedTableModel();
//  return m_tmTracked;
//
//}

/**
 * 返回 UITablePane1 特性值。
 * @return nc.ui.pub.beans.UITablePane
 */
/* 警告：此方法将重新生成。 */
//private nc.ui.pub.beans.UITablePane getUITablePane1() {
//  if (ivjUITablePane1 == null) {
//    try {
//      ivjUITablePane1 = new nc.ui.pub.beans.UITablePane();
//      ivjUITablePane1.setName("UITablePane1");
//      ivjUITablePane1.setBounds(10, 84, 880, 221);
//      if(m_bIsMutiSel){
//        ivjUITablePane1.getTable().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//      }else{
//        ivjUITablePane1.getTable().getSelectionModel().setSelectionMode(javax.swing.DefaultListSelectionModel.SINGLE_SELECTION);
//      }
//      ivjUITablePane1.getTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
//      ivjUITablePane1.getTable().addMouseListener(this);
//      ivjUITablePane1.getTable().addSortListener();
//    } catch (java.lang.Throwable ivjExc) {
//      handleException(ivjExc);
//    }
//  }
//  return ivjUITablePane1;
//}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return nc.vo.pub.lang.UFDate
 */
public UFDate getValidate() {

  m_ufdValidate = m_lnrvoSelVO==null?null:m_lnrvoSelVO.getDvalidate();
  return m_ufdValidate;

}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getWareHouseCode() {
  return m_strWareHouseCode;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getWareHouseID() {
  return m_strWareHouseID;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getWareHouseName() {
  return m_strWareHouseName;
}

/**
 * 由VO传入库存管理中的该存货的批次是否按照先进先出显示
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：false 表明该存货是后进先出。
 * 例外：
 * 日期：(2001-6-18 14:04:36)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return boolean
 */
public boolean isFIFO1() {
  if (getOutPriority() != null && getOutPriority().intValue() == 1) { // 1=LIFO
    return false;
  } else {
    return true;
  }
}

/**
 *  * 功能：得到是否跟踪入库单。

 * 创建日期：(2001-5-8 18:35:15)
 * @return boolean
 */
public boolean isTrackedBill() {

  //if(getIsTrackedBill()){

  //return true;
  //}else{

  ////1、 出库批次参照依据初始参数设置，按先进先出或后进先出显示，用户可任意选择批次出库。
  ////2、 批次跟踪是否到单据号：1）按批号汇总结存，不跟踪到入库单据。
  ////2）按单据号+批号显示明细结存，跟踪到入库单据。

  //return false;
  //}
  //return true; //for test
  return m_bIsTrackedBill;
}

/**
 * 此处插入方法说明。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-8-1 15:01:21)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return boolean
 */
public boolean isWasteWH() {
  return m_bisWasteWH;
}

/**
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2005-1-4 17:19:19)
 * 修改日期，修改人，修改原因，注释标志：
 *
 */
public void onQueryAll() {
  m_params = new Object[]{
      m_strCorpID,
      m_strWareHouseID,
      m_strInvID,
      null,
      new Boolean(m_bisWasteWH),
      new Boolean(isTrackedBill()),
      m_iOutPriority,
      m_strPk_calbody,
      new Boolean(m_bIsQureyZeroLot),
      new Boolean(m_bIsMutiSel),
      getStrNowSrcBid()};
  ArrayList alBack = m_alFreeItemValue;
  m_alFreeItemValue = null;
  setData(); //
  m_alFreeItemValue = alBack;
  //setStrNowSrcBid(null);
  setVOtoBody(); //重新将数据放入表体
//  getUITablePane1().getTable().repaint();
//  getUITablePane1().getTable().updateUI();
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-12 16:55:10)
 * 修改日期，修改人，修改原因，注释标志：
 *
 */
public void onRefresh() {
  setData(); //
  setVOtoBody(); //重新将数据放入表体
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-7-12 22:27:08)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strAssistMeaUnitID java.lang.String
 */
public void setAssistMeaUnitID(java.lang.String newM_strAssistMeaUnitID) {
  m_strAssistMeaUnitID = newM_strAssistMeaUnitID;
}

/**
 * 此处插入方法说明。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-06-11 15:24:57)
 * 修改日期，修改人，修改原因，注释标志：
 * @param newCalbodyName java.lang.String
 */
public void setCalbodyName(java.lang.String newCalbodyName) {
  m_strCalbodyName = newCalbodyName;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-7-12 22:27:08)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strCorpID java.lang.String
 */
public void setCorpID(java.lang.String newM_strCorpID) {
  m_strCorpID = newM_strCorpID;
}

/**
 * 根据传入的仓库和存货ID从数据库表头、表体中查出相关数据
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:38:16)
 * 修改日期，修改人，修改原因，注释标志：
 *
 */
@SuppressWarnings("unchecked")
public void setData() {
  if (((m_strWareHouseID == null || m_strWareHouseID.trim().length() <= 0)
    && (m_strPk_calbody == null || m_strPk_calbody.trim().length() <= 0))
    || m_strInvID == null
    || m_strInvID.trim().length() <= 0) {
    return;

  }
  String vlot = null;
  BillItem bi = getBillCardPanel().getHeadItem("vbatchcode");
  if(bi!=null)
    vlot = bi.getValueObject()==null?null:bi.getValueObject().toString();
  UFDate dstartdate = null,denddate = null;
  bi = getBillCardPanel().getHeadItem("dbatchtimestart");
  if(bi!=null)
    dstartdate = (UFDate)SmartVOUtilExt.convertType(SmartFieldMeta.JAVATYPE_UFDATE, bi.getValueObject());
  bi = getBillCardPanel().getHeadItem("dbatchtimeend");
  if(bi!=null)
    denddate = (UFDate)SmartVOUtilExt.convertType(SmartFieldMeta.JAVATYPE_UFDATE, bi.getValueObject());
  
  UFBoolean isfilterbyatp = null;
  bi = getBillCardPanel().getHeadItem("isfilterbyatp");
  if(bi!=null)
    isfilterbyatp = (UFBoolean)SmartVOUtilExt.convertType(SmartFieldMeta.JAVATYPE_UFBOOLEAN, bi.getValueObject());
  
  getBillCardPanel().setHeadItem("cinventoryid", m_strInvID);
  
  Object params[] =
    {
      m_strCorpID,
      m_strWareHouseID,
      m_strInvID,
      m_strAssistMeaUnitID,
      new Boolean(m_bisWasteWH),
      new Boolean(isTrackedBill()),
      m_iOutPriority,
      m_strPk_calbody,
      new Boolean(m_bIsQureyZeroLot),
      new Boolean(m_bIsMutiSel),
      getStrNowSrcBid(),
      vlot,
      dstartdate,
      denddate,
      isfilterbyatp
      };
  if(m_params != null)
    params = m_params;
  try {
    
    getBillCardPanel().execHeadLoadFormulas();
    
    m_alAllData = LotNumbRefHelper.queryAllLotNum(params, m_alFreeItemValue);
    //nc.vo.scm.ic.bill.InvVO voInv = Inv
    //应该将传入的参数带到Server端，如: 仓库编码，存货编码
    LotNumbRefVO[] voaAllData = null;
    if (m_alAllData != null && m_alAllData.size() > 0) {
      //m_isTrackedBillflag = (Integer) m_alAllData.get(0);
      //m_alAllData.remove(0);
      //如果存货是跟踪到入库单的存货且传入的本单据表体主键有值,则调用过滤本单据主键的方法.
      if (isTrackedBill() && m_strNowBillHid != null && m_strNowBillHid.trim().length() > 0) {
        m_alAllData = filterNowBill(m_alAllData, m_strNowBillHid);
      }
      voaAllData = new LotNumbRefVO[m_alAllData.size()];
      m_alAllData.toArray(voaAllData);

      m_hashLotNumbVO = new Hashtable();
      ArrayList al = new ArrayList();
      
      for (int i = 0; i < voaAllData.length; i++) {
    	//liuys add for 鹤岗矿业 begin,根据批次与存货追溯到入库的供应商
    	String cinventoryid = voaAllData[i].getCinventoryid();
    	String vbatchcode = voaAllData[i].getVbatchcode();
    	String infor = vbatchcode +"&"+cinventoryid;
    	al.add(infor);
      }
      Object  cvendor = HgICPubHealper.getcvendor(al);
		if(cvendor != null){
			ArrayList alc =(ArrayList)cvendor;
			int  size = alc.size();
			for(int i=0;i<size; i++){
			     String pk_cubasdoc = execFormular("getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cvendorid)",PuPubVO.getString_TrimZeroLenAsNull(alc.get(i)));
				 voaAllData[i].setAttributeValue("pk_cubasdoc",pk_cubasdoc);
				 voaAllData[i].setAttributeValue("cinventoryid", m_strInvID);
				 m_hashLotNumbVO.put(voaAllData[i].getVbatchcode() == null ? "" : voaAllData[i].getVbatchcode(), voaAllData[i]);
			     if(m_invvo != null)
			          voaAllData[i].setFreeVO(m_invvo.getFreeItemVO());
			}
		}
		//liuys add for 鹤岗矿业 end 
      //BatchCodeDefSetTool.execFormulaBatchCode(voaAllData);
    } else {
      //add by zss 原因：期初余额录入批次号存货单据后，期初借出可参照出此批次号，
      //但这时期初余额的存货单据立刻删除，在期初借出中不能参照出此批次号存货，
      //但手工输入批次号能带出被删掉的存货信息(哈希表中数据未更新）
      m_hashLotNumbVO = null;
      voaAllData = null;
    }
    getBillCardPanel().getBillModel().setBodyDataVO(voaAllData);
    
    getBillCardPanel().getBillModel().execLoadFormula();
    
    getBillCardPanel().updateValue();

  } catch (Exception e) {
    m_params = null;
    nc.vo.scm.pub.SCMEnv.error(e);
    nc.vo.scm.pub.SCMEnv.out("Can't get Data from server!");
  }
  m_params = null;
}

/**
 * 调用公式 功能： 参数： 返回： 例外： 日期：(2001-11-12 16:47:04) 修改日期，修改人，修改原因，注释标志：
 */
private String execFormular(String formula, String value) {
	nc.ui.pub.formulaparse.FormulaParse f = new nc.ui.pub.formulaparse.FormulaParse();

	if (formula != null && !formula.equals("")) {
		// 设置表达式
		f.setExpress(formula);
		// 获得变量
		nc.vo.pub.formulaset.VarryVO varry = f.getVarry();
		// 给变量付值
		Hashtable h = new Hashtable();
		for (int j = 0; j < varry.getVarry().length; j++) {
			String key = varry.getVarry()[j];

			String[] vs = new String[1];
			vs[0] = value;
			h.put(key, StringUtil.toString(vs));
		}

		f.setDataS(h);
		// 设置结果
		if (varry.getFormulaName() != null
				&& !varry.getFormulaName().trim().equals(""))
			return f.getValueS()[0];
		else
			return f.getValueS()[0];

	} else {
		return null;
	}
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strFreeItem java.lang.String
 */
public void setFreeItem(String FreeItem) {
  m_strFreeItem = FreeItem;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-12 11:25:53)
 * 修改日期，修改人，修改原因，注释标志：
 *
 */
public void setFreeItemValue(ArrayList FreeItemValue) {

  m_alFreeItemValue = FreeItemValue;
  }

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strInventoryCode java.lang.String
 */
public void setInvCode(java.lang.String newInvCode) {
  m_strInvCode = newInvCode;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strInventoryID java.lang.String
 */
public void setInvID(String newInvID) {
  m_strInvID = newInvID;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strInventoryName java.lang.String
 */
public void setInvName(String newInvName) {
  m_strInvName = newInvName;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strSpec java.lang.String
 */
public void setInvSpec(String newstrSpec) {
  m_strInvSpec = newstrSpec;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strType java.lang.String
 */
public void setInvType(String newstrType) {
  m_strInvType = newstrType;
}

/**
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2005-1-4 16:50:29)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_invvo nc.vo.scm.ic.bill.InvVO
 */
public void setInvvo(nc.vo.scm.ic.bill.InvVO newM_invvo) {
  m_invvo = newM_invvo;
}

/**
 * 此处插入方法说明。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-05-21 11:35:07)
 * 修改日期，修改人，修改原因，注释标志：
 * @param newIsTrackedBill boolean
 */
protected void setIsTrackedBill(boolean newIsTrackedBill) {
  m_bIsTrackedBill = newIsTrackedBill;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strMeasUnit java.lang.String
 */
public void setMeasUnit(java.lang.String newM_strMeasUnit) {
  m_strMeasUnit = newM_strMeasUnit;
}

/**
 * 此处插入方法说明。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-05-21 11:35:07)
 * 修改日期，修改人，修改原因，注释标志：
 * @param newOutPriority java.lang.Integer
 */
protected void setOutPriority(java.lang.Integer newOutPriority) {
  m_iOutPriority = newOutPriority;
}

/**
 * 此处插入方法说明。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-06-11 15:24:57)
 * 修改日期，修改人，修改原因，注释标志：
 * @param newPk_calbody java.lang.String
 */
public void setPk_calbody(java.lang.String newPk_calbody) {
  m_strPk_calbody = newPk_calbody;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-14 1:35:57)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_lnrvoSelVO nc.vo.ic.pub.lot.LotNumbRefVO
 */
public void setSelVO(nc.vo.ic.pub.lot.LotNumbRefVO newM_lnrvoSelVO) {
  m_lnrvoSelVO = newM_lnrvoSelVO;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-15 14:19:09)
 * 修改日期，修改人，修改原因，注释标志：
 *
 */
public void setVOtoUI() {
  setVOtoHeader();
  //将数据放入表头、表体
  setVOtoBody();




}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strWareHouseCode java.lang.String
 */
public void setWareHouseCode(java.lang.String newM_strWareHouseCode) {
  m_strWareHouseCode = newM_strWareHouseCode;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strWareHouseID java.lang.String
 */
public void setWareHouseID(java.lang.String newM_strWareHouseID) {
  m_strWareHouseID = newM_strWareHouseID;
}

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strWareHouseName java.lang.String
 */
public void setWareHouseName(java.lang.String newM_strWareHouseName) {
  m_strWareHouseName = newM_strWareHouseName;
}

/**
 * 此处插入方法说明。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-8-1 15:01:21)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newWasteWH boolean
 */
public void setWasteWH(boolean newWasteWH) {
  m_bisWasteWH = newWasteWH;
}

private BatchCodeDlg getBatchCodeDlg(){
  if(m_BatchCodeDlg==null){
    m_BatchCodeDlg=new BatchCodeDlg();
  }
  return m_BatchCodeDlg;
}

public String getStrNowSrcBid() {
  return m_strNowSrcBid;
}
public void setStrNowSrcBid(String nowSrcBid) {
  m_strNowSrcBid = nowSrcBid;
}

/**
 * 取得系统信息。
 */
private ClientLink getCEnvInfo() {
  if(m_cl==null){
    nc.ui.pub.ClientEnvironment ce = nc.ui.pub.ClientEnvironment.getInstance();
    m_cl=new ClientLink(ce);
  }
  return m_cl;
  
}

private void addSelectBox(BillTempletVO tempvo) {
  if (!m_bIsBodyMutiSel)
    return;
  if(tempvo==null || tempvo.getBodyVO()==null || tempvo.getBodyVO().length<=0)
    return;
  BillTempletBodyVO[] bodyvos = new  BillTempletBodyVO[tempvo.getBodyVO().length+1];
  bodyvos[0] = new BillTempletBodyVO();
  bodyvos[0].setItemkey("select_state");
  bodyvos[0].setDefaultshowname("  ");
  bodyvos[0].setPos(1);
  bodyvos[0].setCardflag(Boolean.TRUE);
  bodyvos[0].setShowflag(Boolean.TRUE);
  bodyvos[0].setDatatype(new Integer(BillItem.BOOLEAN));
  bodyvos[0].setEditflag(Boolean.TRUE);
  bodyvos[0].setWidth(new Integer(50));
  bodyvos[0].setShoworder(new Integer(0));
  
  HashSet<String> hskeys = new HashSet<String>(Arrays.asList(new String[]{
      "ninnum","noutnum","ngrossnum",
      "ninassistnum","noutassistnum",
      "hsl"
  }));
  
  for(int i=0;i<tempvo.getBodyVO().length;i++){
    bodyvos[i+1] = tempvo.getBodyVO()[i];
    if(bodyvos[i+1].getPos()!=null && bodyvos[i+1].getPos().intValue()==BillItem.BODY)
      bodyvos[i+1].setEditflag(Boolean.FALSE);
    if(hskeys.contains(bodyvos[i+1].getItemkey()))
      bodyvos[i+1].setDatatype(BillItem.DECIMAL);
  }
//  System.arraycopy(tempvo.getBodyVO(), 0, bodyvos, 1, tempvo.getBodyVO().length);
  tempvo.setChildrenVO(bodyvos);
          
}

private boolean isRowSelected(int row) {
  if(row<0)
    return false;
  if (!m_bIsBodyMutiSel){
    int[] sel = getBillCardPanel().getBillTable().getSelectedRows();
    if(sel==null || sel.length<=0)
      return false;
    for(int i=0;i<sel.length;i++)
      if(sel[i]==row)
        return true;
    return false;
  }
  UFBoolean bsel = CheckTools.toUFBoolean(getBillCardPanel().getBodyValueAt(row, "select_state"));
  return bsel==null?false:bsel.booleanValue();
}

private int[] getSelectRows() {
  if (!m_bIsBodyMutiSel){
    return getBillCardPanel().getBillTable().getSelectedRows();
  }
  IntList intlist = new IntList();
  for(int i=0;i<getBillCardPanel().getRowCount();i++){
    if(isRowSelected(i))
      intlist.add(i);
  }
  if(intlist.size()<=0)
    return null;
  return intlist.toIntArray();
}

private void addBodySelListenerEvent() {
  
  if (!m_bIsBodyMutiSel)
    return;
  
  //表头的列表行切换事件处理器
  ListSelectionListener lsl = new ListSelectionListener() {
    public void valueChanged(ListSelectionEvent e) {
      int icol = getBillCardPanel().getBillTable().getSelectedColumn();
      if(icol>=0){
        String key = getBillCardPanel().getBodyPanel().getBodyKeyByCol(icol);
        if(key!=null && key.equals("select_state"))
          return;
      }
      int[] selrows = getBillCardPanel().getBillTable().getSelectedRows();
      if(selrows!=null && selrows.length>0){
        for(int i=0,loop=selrows.length;i<loop;i++){
          if(selrows[i]>=0)
            getBillCardPanel().getBillModel().setValueAt(UFBoolean.TRUE, selrows[i], "select_state");
        }
      }
    }
  };
  getBillCardPanel().getBillTable().getSelectionModel().addListSelectionListener(lsl);
}

public BillCardPanel getBillCardPanel(){
  if(m_card==null){
    try {
      m_card = new nc.ui.pub.bill.BillCardPanel();
      m_card.setName("BillCardPanel");
      
      BillTempletVO tempvo = m_card.getTempletData(m_sRNodeName, null, getCEnvInfo().getUser(), getCEnvInfo().getCorp(),m_sRNodeKey);
      BillTempletVO tempvonew  = (BillTempletVO)tempvo.clone();
      
       
      addSelectBox(tempvonew);
      BillData bd = new BillData(tempvonew);
      BillItem bi = bd.getHeadItem("vfree0");
      if(bi!=null){
        bi.setShow(!m_bIsMutiSel);
        bi.getCaptionLabel().setVisible(!m_bIsMutiSel);
        bi.getComponent().setVisible(!m_bIsMutiSel);
      }
      if (bd == null) {
        nc.vo.scm.pub.SCMEnv.out("--> billdata null.");
        return m_card;
      }
      //自定义项
      BatchCodeDefSetTool.changeBillDataByBCDef(m_cl.getCorp(), bd);
  
      m_card.setBillData(bd);
      
      m_card.getBodyPanel().setBBodyMenuShow(false);
      m_card.getBodyPanel().setRowNOShow(false);
      m_card.addNew();
      //m_card.setEnabled(m_bIsBodyMutiSel);
      m_card.getBillData().setEnabled(m_bIsBodyMutiSel);
      //m_card.getHeaderPanel().setEnabled(true);
      m_card.setBodyMenuShow(false);
      m_card.addEditListener(this);
      m_card.addBodyMouseListener(this);
      addBodySelListenerEvent();
      m_card.setRowNOShow(false);
      //m_card.getBodyPanel().getTable().setRowSelectionAllowed(true);
      if (!m_bIsBodyMutiSel)
        m_card.getBodyPanel().getTable().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
      
      GeneralBillUICtl.setItemEdit(m_card, BillItem.BODY, new String[]{"tchecktime","binqc"}, false);
      GeneralBillUICtl.showItem(m_card.getBodyPanel(), false,new String[]{"invname","invcode"});
      
      bi = m_card.getHeadItem("isqueryzerolot");
      if(bi!=null){
        UFBoolean ufb = (UFBoolean)SmartVOUtilExt.convertType(SmartFieldMeta.JAVATYPE_UFBOOLEAN, 
          getBillCardPanel().getHeadItem("isqueryzerolot").getValueObject());
        m_bIsQureyZeroLot = ufb==null?false:ufb.booleanValue();
      }
      
      
      bi = getBillCardPanel().getHeadItem("isfilterbyatp");
      if(bi!=null){
        UFBoolean ufb = (UFBoolean)SmartVOUtilExt.convertType(SmartFieldMeta.JAVATYPE_UFBOOLEAN, 
            getBillCardPanel().getHeadItem("isfilterbyatp").getValueObject());
        bi = getBillCardPanel().getHeadItem("isqueryzerolot");
        if(bi!=null && ufb!=null && ufb.booleanValue()){
          bi.setValue(UFBoolean.FALSE);
          bi.setEdit(false);
          m_bIsQureyZeroLot = false;
        }else{
          bi.setEdit(true);
        }
      }
      
//    BD501 数量小数位     2
      //BD502 辅计量数量小数位    2
      //BD503 换算率       2
      HashMap<String,String[]> scaleParameter = new HashMap<String,String[]>();
      
        
      scaleParameter.put("BD501", new String[]{"ninnum","noutnum","ngrossnum"});
      scaleParameter.put("BD502", new String[]{"ninassistnum","noutassistnum"});
      scaleParameter.put("BD503", new String[]{"hsl"});
      ICCommonBusi.setCardPanelScale(getCorpID(), 
          m_card, BillItem.BODY, scaleParameter);
      
      
  } catch (java.lang.Throwable ivjExc) {
    handleException(ivjExc);
  }
  
  }
  return m_card;
}
public void afterEdit(BillEditEvent e) {
  // TODO Auto-generated method stub
  if(e.getKey().equals("isqueryzerolot")){
    UFBoolean ufb = (UFBoolean)SmartVOUtilExt.convertType(SmartFieldMeta.JAVATYPE_UFBOOLEAN, 
        getBillCardPanel().getHeadItem("isqueryzerolot").getValueObject());
    m_bIsQureyZeroLot = ufb==null?false:ufb.booleanValue();
    onRefresh();
  }else if(e.getKey().equals("isfilterbyatp")){
    UFBoolean ufb = (UFBoolean)SmartVOUtilExt.convertType(SmartFieldMeta.JAVATYPE_UFBOOLEAN, 
        getBillCardPanel().getHeadItem("isfilterbyatp").getValueObject());
    BillItem bi = getBillCardPanel().getHeadItem("isqueryzerolot");
    if(bi!=null && ufb!=null && ufb.booleanValue()){
      bi.setValue(UFBoolean.FALSE);
      bi.setEdit(false);
      m_bIsQureyZeroLot = false;
    }else{
      bi.setEdit(true);
    }
    onRefresh();
  }
  
}
public void bodyRowChange(BillEditEvent arg0) {
  int selrow = getBillCardPanel().getBillTable().getSelectedRow();

  if (selrow != -1 && isRowSelected(selrow)) {
    m_lnrvoSelVO=(LotNumbRefVO)getBillCardPanel().getBillModel().getBodyValueRowVO(selrow, LotNumbRefVO.class.getName());
  }
}
public void mouse_doubleclick(BillMouseEnent arg0) {
  onOK();
  
}

}
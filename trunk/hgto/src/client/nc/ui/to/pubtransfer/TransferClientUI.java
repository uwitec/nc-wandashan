package nc.ui.to.pubtransfer;

/**
 * 创建日期：(2004-2-9 11:45:59) 作者：王乃军 说明：** 代码模版 **业务界面。
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.bd.corp.ICorpQry;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.FramePanel;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillActionListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.action.BillTableLineAction;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.query.EditComponentFacotry;
import nc.ui.scm.plugin.InvokeEventProxy;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.bill.IBillExtendFun;
import nc.ui.scm.pub.ctrl.GenEditConst;
import nc.ui.scm.pub.ctrl.GenPanelEvent;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.redunmulti.IBillToBillFor5X;
import nc.ui.scm.pub.redunmulti.IUseSupplyTrans;
import nc.ui.to.pub.ClientBillHelper;
import nc.ui.to.pub.ClientCommonDataHelper;
import nc.ui.to.pub.ConstHintMsg;
import nc.ui.to.pub.ExceptionUITools;
import nc.ui.to.pub.TOBillTool;
import nc.ui.to.pub.TOEnvironment;
import nc.ui.to.pub.TOQueryDlgListener;
import nc.ui.to.pub.btn.ITOButtonConst;
import nc.ui.to.pub.plugin.TOPluginUI;
import nc.ui.to.pub.power.TOQueryPrivilegeTool;
import nc.vo.bd.CorpVO;
import nc.vo.logging.Debug;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pub.ctrl.GenMsgCtrl;
import nc.vo.scm.pub.ctrl.GenTimer;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.to.pub.BillHeaderVO;
import nc.vo.to.pub.BillItemVO;
import nc.vo.to.pub.BillVO;
import nc.vo.to.pub.ConstVO;
import nc.vo.to.pub.Log;
import nc.vo.to.pub.SqlBuilder;
import nc.vo.to.pub.tools.ExceptionUtils;
import nc.vo.to.to201.ConstVO201;
import nc.vo.trade.checkrule.VOChecker;

/**
 * 创建日期：(2004-2-9 11:45:59) 作者：王乃军 说明：** 代码模版 **业务界面。
 */

public class TransferClientUI extends nc.ui.to.pub.TOToftPanel implements
    nc.ui.scm.pub.ctrl.GenPanelEventListener, IBillExtendFun,
    nc.ui.pf.query.ICheckRetVO, ILinkQuery, ILinkApprove, ILinkMaintain,
    IUseSupplyTrans, BillActionListener {
  // 缓存参照拉单时的上游单据表体行id
  private HashSet refBillIds = new HashSet();

  /**
   * 补货直运界面
   */
  private IBillToBillFor5X m_BillToBillUI;

  // 卡片控制
  public CardPanelCtrl m_CardCtrl;

  // 列表控制
  public ListPanelCtrl m_ListCtrl;

  // 按钮动作处理
  protected ButtonCtrl m_ButtonCtrl;

  public ButtonTree m_boBtnTree;

  private ButtonEventHandler m_btnEventHandler;
  
  private boolean iqueryStatus = false;
  
  boolean isWaitAudit = false;
  
  String querysql = null;

  // //数据
  protected Model m_Model;

  protected Model m_ModelFrom5A;

  // 节点共享信息的引用。
  protected NodeInfo m_NodeInfo = null;

  // 当前显示的panel
  private int m_iCurPanel;

  // 查询对话框
  private nc.ui.scm.pub.query.SCMQueryConditionDlg m_QueryCondDlg;

  // 常用条件面板
  private QueryNormalPane m_panelQuery = null;
  
//二次开发扩展
  private InvokeEventProxy pluginproxy;
  
  public InvokeEventProxy getPluginProxy() {
    if(this.pluginproxy==null)
      this.pluginproxy = new InvokeEventProxy(ConstVO.m_sModule,"5X",new TOPluginUI(this,"5X"));
      return this.pluginproxy;
  }
  

  /**
   * ClientUI 构造子注解。
   */
  public TransferClientUI() {
    super();
    // 读取环境变量
    initNodeInfo();
    // 初始化Toftpanel
    initialize();
    // 设置按钮
    initbutton();
  }
  
  /**
   * ClientUI 构造子注解。
   */
  public TransferClientUI(FramePanel frame_pane) {
    super();
    // 读取环境变量
    initNodeInfo(frame_pane);
    // 初始化Toftpanel
    initialize();
    // 设置按钮
    initbutton();
  }
  
  public TransferClientUI(String billID) {
    // 读取环境变量
    initNodeInfo();
    // 初始化Toftpanel
    initialize();
    BillVO vo = qryBillForLinkOperate(billID);
    // 状态栏显示查询结果条数的信息
    if (vo == null) {
      String msg = nc.ui.ml.NCLangRes.getInstance().getStrByID("topub",
      "UPPtopub-000036")/* @res "单据可能已经被删除！" */;
      MessageDialog.showWarningDlg(this,null,msg);
      return;
    }
    
    ArrayList<BillVO> alListData = new ArrayList<BillVO>();
    alListData.add(vo);
    
    m_Model.setData(alListData);
    m_CardCtrl.showPanel();
    m_ListCtrl.hidePanel();
    getBillCtrl().displayBill(0);
  }
  
  /**
   * 此处插入方法说明。 创建日期：(2001-4-25 16:15:06)
   */
  private QueryNormalPane getQueryNormalPanel() {

    if (m_panelQuery == null) {
      m_panelQuery = new QueryNormalPane();
    }

    return m_panelQuery;
  }

  /**
   * 创建者：王乃军 功能：得到查询对话框 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
   */
  protected nc.ui.scm.pub.query.SCMQueryConditionDlg getConditionDlg() {
    if (m_QueryCondDlg == null) {
      try {
        m_QueryCondDlg = new nc.ui.scm.pub.query.SCMQueryConditionDlg(this);
        
        m_QueryCondDlg.setTempletID(m_NodeInfo.getCorpID(), m_NodeInfo
            .getNodeCode(), m_NodeInfo.getUserID(), null, null);

        // 以下为对参照的初始化
        m_QueryCondDlg.initQueryDlgRef();

        // 隐藏常用条件
        m_QueryCondDlg.hideUnitButton();
        m_QueryCondDlg.setNormalShow(true);
        m_QueryCondDlg.getUIPanelNormal().add(getQueryNormalPanel());
        
        // 调出公司默认为当前登陆公司
        m_QueryCondDlg.setDefaultValue("to_bill.coutcorpid", m_NodeInfo
            .getCorpID(), m_NodeInfo.getCorpID());

        // 显示打印状态查询Panel
        m_QueryCondDlg.setShowPrintStatusPanel(true);
        
        
        // 设置下拉框显示,旧查询模板不支持“=”
        m_QueryCondDlg.setCombox("to_bill.ctypecode", new Object[][] {
            {
              "-99",
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009",
              "UPT40093009-000054")
            },
            {
              "5C",
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009",
              "UPT40093009-000050")
            },
            {
              "5D",
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009",
              "UPT40093009-000051")
            },
            {
              "5E",
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009",
              "UPT40093009-000052")
            },
            {
              "5I",
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009",
              "UPT40093009-000053")
            }
        });
        
        
        
        // 处理查询模板上自定义项
        DefSetTool.updateQueryConditionClientUserDef(m_QueryCondDlg, m_NodeInfo
            .getCorpID(), Const.USERDEF_TYPE_TOBILL, "to_bill.vdef",
            "to_bill_b.vbdef");

        nc.vo.pub.query.QueryConditionVO[] voaConData = m_QueryCondDlg
            .getConditionDatas();

        // 以下为对参照的初始化

        String sbilltype = getNodeInfo().getBillTypeCode();
        if (!sbilltype.startsWith("5")) {
          sbilltype = ConstVO.m_sBillSFDBDD;
        }
        TOQueryDlgListener listener = new TOQueryDlgListener(m_QueryCondDlg,
            sbilltype);
        HashMap<String, String> hmFieldCode = new HashMap<String, String>();
        nc.ui.pub.beans.UIRefPane ref = null;
        // 固定条件
        HashMap hmFixedData = TOBillTool.getImmobilityQueryData(m_QueryCondDlg,
            m_NodeInfo.getCorpID());
        String sFieldCode = "";
        for (int i = 0; i < voaConData.length; i++) {
          sFieldCode = voaConData[i].getFieldCode();
          EditComponentFacotry factry = new EditComponentFacotry(voaConData[i]);
          if (voaConData[i].getDataType().intValue() == nc.vo.pub.query.IQueryConstants.UFREF) {
            ref = (nc.ui.pub.beans.UIRefPane) factry.getEditComponent(null);
            if(sFieldCode.equals("to_bill.cbiztypeid")){
              ref.setName("QueryBusitype");
              ref.setWhereString("busiprop=7");
            }
            else if (sFieldCode.equals("to_bill.cincorpid")) {
              ref.setName(TOQueryDlgListener.REFNAME_INCORP);
              ref.addValueChangedListener(listener);// 调入公司
              ref.setRefNodeName("公司目录(集团)");
            }
            else if (sFieldCode.equals("to_bill_b.cindeptid")) {
              ref.setName("QueryInDept");
              // 查询模板上有固定条件
              if (hmFixedData.get("to_bill.cincorpid") != null) {
                ref.setWhereString(" pk_corp = '"
                    + hmFixedData.get("to_bill.cincorpid") + "'");
              }
              else {
                // 没有设默认值时，初始化为空
                ref.setWhereString("1<0");
              }
            }
            else if (sFieldCode.equals("to_bill_b.cinpsnid")) {
              ref.setName("QueryInPsn");
              // 查询模板上有固定条件
              if (hmFixedData.get("to_bill.cincorpid") != null) {
                ref.setWhereString(" pk_corp = '"
                    + hmFixedData.get("to_bill.cincorpid") + "'");
              }
              else {
                // 没有设默认值时，初始化为空
                ref.setWhereString("1<0");
              }
            }
            else if (sFieldCode.equals("to_bill.cincbid")) {
              ref.setName(TOQueryDlgListener.REFNAME_INCB);
              ref.addValueChangedListener(listener);// 调入组织

              ref.getRefModel().setUseDataPower(false);

              // 查询模板上有固定条件
              if (hmFixedData.get("to_bill.cincorpid") != null) {
                ref.setWhereString(" pk_corp = '"
                    + hmFixedData.get("to_bill.cincorpid")
                    + "'and property <> "
                    + String.valueOf(ConstVO.ICalbodyType_COST));
              }
              else {
                // 没有设默认值时，初始化为空
                ref.setWhereString("1<0");
              }

            }
            else if (sFieldCode.equals("to_bill.cinwhid"))// 调入仓库
            {
              ref.setName(TOQueryDlgListener.REFNAME_INWH);
              ref.setWhereString(" pk_corp = null");
              ref.getRefModel().setUseDataPower(false);
            }
            else if (sFieldCode.equals("to_bill_b.cindeptid")) {
              ref.setName(TOQueryDlgListener.REFNAME_INDEPT);
              ref.addValueChangedListener(listener);// 调入部门
            }
            else if (sFieldCode.equals("to_bill_b.cinpsnid")) {
              ref.setName(TOQueryDlgListener.REFNAME_INPSN);
              ref.setButtonFireEvent(true);
            }
            else if (sFieldCode.equals("to_bill_b.creceiverbasid")) {
              ref.setName("QueryReceiverbasid");
              ref.setButtonFireEvent(true);
            }
            else if (sFieldCode.equals("to_bill.coutcorpid")) {
              ref.setName(TOQueryDlgListener.REFNAME_OUTCORP);
              ref.addValueChangedListener(listener); // 调出公司
              ref.setRefNodeName("权限公司目录(集团)");
            }
            else if (sFieldCode.equals("to_bill.coutcbid")) {
              ref.setName(TOQueryDlgListener.REFNAME_OUTCB);
              // //查询模板上有固定条件
              if (hmFixedData.get("to_bill.coutcorpid") != null) {
                ref.setWhereString(" pk_corp = '"
                    + hmFixedData.get("to_bill.coutcorpid")
                    + "'and property <> "
                    + String.valueOf(ConstVO.ICalbodyType_COST));
              }
              else {
                // //没有设默认值时，默认为当前登录公司
                ref.setWhereString(" pk_corp = '" + m_NodeInfo.getCorpID()
                    + "' and property <> "
                    + String.valueOf(ConstVO.ICalbodyType_COST));
              }

              ref.addValueChangedListener(listener);// 调出组织
              // 调出组织查询不受权限控制
              // ref.getRefModel().setUseDataPower(false);
            }
            else if (sFieldCode.equals("to_bill.coutwhid"))// 调出仓库
            {
              ref.setName(TOQueryDlgListener.REFNAME_OUTWH);
              ref.setWhereString(" pk_corp = null");
              // ref.getRefModel().setUseDataPower(false);
            }
            else if (sFieldCode.equals("to_bill.ctakeoutcorpid")) {
              ref.setName(TOQueryDlgListener.REFNAME_TAKEOUTCORP);
              ref.addValueChangedListener(listener); // 出货公司
            }
            else if (sFieldCode.equals("to_bill.ctakeoutcbid")) {
              ref.setName(TOQueryDlgListener.REFNAME_TAKEOUTCB);
              ref.addValueChangedListener(listener); // 出货组织
              // 查询模板上有固定条件
              if (hmFixedData.get("to_bill.ctakeoutcorpid") != null) {
                ref.setWhereString(" pk_corp = '"
                    + hmFixedData.get("to_bill.ctakeoutcorpid")
                    + "'and property <> "
                    + String.valueOf(ConstVO.ICalbodyType_COST));
              }
              else {
                // 没有设默认值时，初始化为空
                ref.setWhereString("1<0");
              }
            }
            else if (sFieldCode.equals("to_bill_b.btakeoutwhid"))// 调出仓库
            {
              ref.setName(TOQueryDlgListener.REFNAME_TAKEOUTWH);
              ref.setWhereString(" pk_corp = null");
              ref.getRefModel().setUseDataPower(false);

            }
            hmFieldCode.put(ref.getName(), sFieldCode);
            m_QueryCondDlg.setValueRef(sFieldCode, ref);
          }
        }
        listener.setFieldCode(hmFieldCode);

      }
      catch (Exception e) {
        nc.vo.scm.pub.ctrl.GenMsgCtrl.handleException(e);
      }
      
      //单据日期：默认当天（大于等于）,单据日期：默认当天（小于等于）
      UFDate date = new UFDate(m_NodeInfo.getLogDate());
      m_QueryCondDlg.addExtraDate(NodeInfo.NAME_HEAD_TABLE + ".dbilldate", date, date);
    }
    return m_QueryCondDlg;
  }

  public void doApproveAction(ILinkApproveData data) {
    if (data == null)
      return;
    BillVO vo = qryBillForLinkOperate(data.getBillID());
    initForLinkApprove(vo);
    setButtonStatusBrowse(0);
  }

  private BillVO qryBillForLinkOperate(String billID) {
    m_sBillID = billID;
    /* 查询调拨订单 */
    BillVO vo = null;
    try {
      vo = (BillVO) getVo();
    }
    catch (Exception e) {
      Log.error(e);
      nc.ui.pub.beans.MessageDialog
          .showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "SCMCOMMON", "UPPSCMCommon-000270")/* @res "提示" */,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                  "UPPSCMCommon-000428")/* @res "系统故障！" */);
    }
    return vo;
  }
  
  

  /**
   * 子类实现该方法，返回业务界面的标题。
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  public String getTitle() {
    if (m_CardCtrl != null)
      return m_CardCtrl.getTitle();
    return null;
  }

  /**
   * 创建日期：(2004-2-9 13:49:30) 作者：王乃军 参数： 返回： 说明：初始化界面
   */
  public void initialize() {
    try {
      // 数据
      m_Model = new Model();
      initCardCtrl();
      initListCtrl();
      // 加载界面
      this.removeAll();
      setLayout(new java.awt.CardLayout());
      m_CardCtrl.addPanel(this);
      m_ListCtrl.addPanel(this);
      m_btnEventHandler = new ButtonEventHandler(this);

      // 显示卡片
      m_iCurPanel = GenEditConst.CARD;
      m_CardCtrl.showPanel();

//      // 在“卡片面板”的表体弹出式菜单中添加“卡片编辑”项
//      addCardEditBodyMenu("base");
//      addCardEditBodyMenu("relation");
//      addCardEditBodyMenu("exec2");
//      addCardEditBodyMenu("free");
//
//      // 在“卡片面板”的表体弹出式菜单中添加“重排行号”项
//      addNewRowNoBodyMenu("base");
//      addNewRowNoBodyMenu("relation");
//      addNewRowNoBodyMenu("exec2");
//      addNewRowNoBodyMenu("free");
//      
//      // 在“卡片面板”的表体弹出式菜单中添加“询价”项
//      addAskPriceBodyMenu("base");
//      addAskPriceBodyMenu("relation");
//      addAskPriceBodyMenu("exec2");
//      addAskPriceBodyMenu("free");

      m_CardCtrl.getCardPanel().getBillCardPanel().addActionListener("base",
          this);
      m_CardCtrl.getCardPanel().getBillCardPanel().addActionListener(
          "relation", this);
      m_CardCtrl.getCardPanel().getBillCardPanel().addActionListener("exec2",
          this);
      m_CardCtrl.getCardPanel().getBillCardPanel().addActionListener("free",
          this);
      
    }
    catch (Exception e) {
      Debug.error(e.getMessage(), e);
    }
  }

  /**
   * 在指定的表体页签上增加“卡片编辑”弹出式菜单项
   * 
   * @author songhy 2008-08-14
   * @param strTableCode
   *          具有多页签卡片面板的表体页签编码
   */
  private void addCardEditBodyMenu(String strTableCode) {
    BillCardPanel billCardPanel = m_CardCtrl.getCardPanel().getBillCardPanel();
    // 在“卡片面板”的表体弹出式菜单中添加“卡片编辑”项
    nc.ui.scm.pub.BillTools.addCardEditToBodyMenus(billCardPanel, strTableCode);
    // “卡片编辑”项注册监听器
    UIMenuItem[] bodyMenuItems = billCardPanel.getBodyMenuItems(strTableCode);

    bodyMenuItems[bodyMenuItems.length - 1]
        .addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            m_btnEventHandler.onCardEdit();
          }
        });
  }

  /**
   * 在指定的表体页签上增加“重排行号”弹出式菜单项
   * 
   * @author songhy 2008-08-14
   * @param strTableCode
   *          具有多页签卡片面板的表体页签编码
   */
  private void addNewRowNoBodyMenu(String strTableCode) {
    BillCardPanel billCardPanel = m_CardCtrl.getCardPanel().getBillCardPanel();
    // 在“卡片面板”的表体弹出式菜单中添加“重排行号”项
    TOBillTool.addNewRowNoToBodyMenus(billCardPanel, strTableCode);
    // “重排行号”项注册监听器
    UIMenuItem[] bodyMenuItems = billCardPanel.getBodyMenuItems(strTableCode);
    bodyMenuItems[bodyMenuItems.length - 1]
        .addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            m_btnEventHandler.onNewRowNo();
          }
        });
  }
  
  /**
   * 在指定的表体页签上增加“询价”弹出式菜单项
   * 
   * @author songhy 2008-08-14
   * @param strTableCode
   *          具有多页签卡片面板的表体页签编码
   */
  private void addAskPriceBodyMenu(String strTableCode) {
    BillCardPanel billCardPanel = m_CardCtrl.getCardPanel().getBillCardPanel();
    // 在“卡片面板”的表体弹出式菜单中添加“重排行号”项
    TOBillTool.addAskPriceToBodyMenus(billCardPanel, strTableCode);
    // “重排行号”项注册监听器
    UIMenuItem[] bodyMenuItems = billCardPanel.getBodyMenuItems(strTableCode);
    bodyMenuItems[bodyMenuItems.length - 1]
        .addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            m_CardCtrl.askPrice();
          }
        });
  }

  private void initForLinkApprove(BillVO vo) {
    initForLinkQuery(vo);
    /* 设置按钮组 */
    ButtonObject boAst = new ButtonObject(ITOButtonConst.BTN_AUXILIARY/*
                                                                       * @res
                                                                       * "辅助"
                                                                       */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
        "UPPSCMCommon-000066")/*
                               * @res "单据的各种辅助功能"
                               */, 2, "辅助"); /*-=notranslate=-*/
    boAst.addChildButton(m_ButtonCtrl.m_boAuditStatus);// 状态查询
    boAst.addChildButton(m_ButtonCtrl.m_boDocument);// 文档查询
    boAst.addChildButton(m_ButtonCtrl.m_boJointCheck);// 联查

    nc.ui.pub.ButtonObject[] aryForAudit = new nc.ui.pub.ButtonObject[] {
        m_ButtonCtrl.m_boAudit, m_ButtonCtrl.m_boCancelAudit, boAst,
        m_ButtonCtrl.m_boPrintMng
    };
    setButtons(aryForAudit);
  }

  private void initForLinkQuery(BillVO vo) {
    initNodeInfo();
    String pk_corp = vo.getPk_corp();
    boolean bAnotherCorp = false;
    // 根据调出公司公司不等于当前登录公司,根据调出公司主键查找公司信息VO
    if (pk_corp != null && !pk_corp.equals(m_NodeInfo.getCorpID())) {
      bAnotherCorp = true;
    }
    if (bAnotherCorp) {
      try {
        ICorpQry qryCorp = (ICorpQry) NCLocator.getInstance().lookup(
            ICorpQry.class.getName());
        CorpVO corpvo = qryCorp.findCorpVOByPK(pk_corp);
        m_NodeInfo.setCorpID(corpvo.getPk_corp());
        m_NodeInfo.setCorpCode(corpvo.getUnitcode());
        m_NodeInfo.setCorpName(corpvo.getUnitname());
      }
      catch (Exception e) {
        ExceptionUITools.showMessage(e, this);
        return;
      }
    }
    // 按调出公司来加载单据模板
    initialize();
    // 显示标题
    setTitleText(m_CardCtrl.getTitle());
    // 如果是跨公司联查,则不加载按钮
    if (bAnotherCorp) {
      // 不控制公司的参照权限
      // --表头调出公司
      UIRefPane ref = (UIRefPane) m_CardCtrl.getCardPanel().getBillCardPanel()
          .getHeadItem("coutcorpid").getComponent();
      ref.setRefNodeName("公司目录(集团)");
      // --表头出货公司
      ref = (UIRefPane) m_CardCtrl.getCardPanel().getBillCardPanel()
          .getHeadItem("ctakeoutcorpid").getComponent();
      ref.setRefNodeName("公司目录(集团)");
      m_CardCtrl.getCardPanel().getBillCardPanel().setEnabled(false);// ?
      setButtons(new ButtonObject[] {});
    }

    m_Model.setCurRow(0);
    m_Model.setData(0, vo);
    getBillCtrl().displayBill(0);
  }

  private void initCardCtrl() {
    // 卡片控制
    m_CardCtrl = new CardPanelCtrl(m_NodeInfo,this);
    m_CardCtrl.setModel(m_Model);
    m_CardCtrl.addPanelEventListener(this);
  }

  private void initListCtrl() {
    // 列表控制
    m_ListCtrl = new ListPanelCtrl(m_NodeInfo,this);
    m_ListCtrl.setModel(m_Model);
    m_ListCtrl.addPanelEventListener(this);
  }

  public AbstractBillCtrl getBillCtrl() {
    if (m_iCurPanel == GenEditConst.CARD) {
      return m_CardCtrl;
    }
    if (m_iCurPanel == GenEditConst.LIST) {
      return m_ListCtrl;
    }
    return null;
  }

  public NodeInfo getNodeInfo() {
    return m_NodeInfo;
  }

  /**
   * 创建者：王乃军 功能：得到环境初始数据，如制单人等。 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
   * 修改日期，修改人，修改原因，注释标志：
   */
  protected void initNodeInfo() {
    try {
      m_NodeInfo = new NodeInfo();
      TOEnvironment te = new TOEnvironment();
      m_NodeInfo.setTOEnv(te);
      nc.ui.pub.ClientEnvironment env = te.getCe();
      m_clientLink = new ClientLink(env);

      Hashtable hReturnValue = TOEnvironment.getParaValues(
          getCorpPrimaryKey(),
          new String[] { ConstVO.m_sPk_Para[ConstVO.m_iPara_ZDSPSFXT], // 是否允许制单和审批人为同一人
          });
      m_NodeInfo.setM_paravalue(hReturnValue);
    }
    catch (Exception e) {
      Debug.error(e.getMessage(), e);
    }
  }

  /**
   * 创建者：王乃军 功能：得到环境初始数据，如制单人等。 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
   * 修改日期，修改人，修改原因，注释标志：
   */
  protected void initNodeInfo(FramePanel frame_pane) {
    try {
      m_NodeInfo = new NodeInfo();
      TOEnvironment te = new TOEnvironment();
      m_NodeInfo.setTOEnv(te);
      nc.ui.pub.ClientEnvironment env = te.getCe();
      m_clientLink = new ClientLink(env);

      Hashtable hReturnValue = TOEnvironment.getParaValues(
          getCorpPrimaryKey(),
          new String[] { ConstVO.m_sPk_Para[ConstVO.m_iPara_ZDSPSFXT], // 是否允许制单和审批人为同一人
          });
      m_NodeInfo.setM_paravalue(hReturnValue);
    }
    catch (Exception e) {
      Debug.error(e.getMessage(), e);
    }
  }
  /**
   * 获取扩展按钮数组
   */
  public nc.ui.pub.ButtonObject[] getExtendBtns() {
    return null;
  }

  /**
   * 控制扩展按钮的事件
   */
  public void onExtendBtnsClick(nc.ui.pub.ButtonObject bo) {

  }
/**
 * add:LIYU 增加调拨订单刷新操作
 */
  protected void refresh(){
	  if(querysql==null) return;
	   try {
	  ArrayList alListData = ClientBillHelper.queryBillsBy(
	          ConstVO201.saQueryHead, ConstVO201.saQueryBody, querysql,
	          "5X", this.m_NodeInfo.getUserID(), isWaitAudit); // 5X: 调拨订单
	  if (alListData != null && alListData.size() > 0) {
	        // 需要解析所有的表头数据
	        m_ListCtrl.setListHeadData(alListData);
	        // 需要解析第一条的表体数据
	        m_ListCtrl.setlistBodyData(1, alListData);
	      }

	      // 状态栏显示查询结果条数的信息
	      if (alListData.size() > 0) {
	        String[] value = new String[] {
	          String.valueOf(alListData.size())
	        };
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
	            "UPP40093010-000122", null, value));/* @res "共查到{0}"张单据！" */
	        m_Model.setCurRow(0);

	      }
	      else {
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
	            "UPP40093010-000071")/* @res "没有查到您所需要的记录！" */);
	        m_Model.setCurRow(-1);
	      }
	      m_Model.setData(alListData);
	      // 当前是表单形式时，首先切换到列表形式
	      if (getCurPanel() == GenEditConst.CARD)
	        switchPanel();
	      else {
	        // 显示查询结果:缺省选中第一张
	        m_ListCtrl.displayData();
	        setButtonStatusBrowse(0);
	      }

	  
	   }
	    catch (Exception e) {
	      Debug.debug(e.getMessage(), e);
	      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
	          "UPP40093010-000072")/* @res "查询出错。" */);
	      String[] value = new String[] {
	        e.getMessage()
	      };
	      showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	          "scmcommon", "UPPSCMCommon-000033", null, value));// "查询出错："
	      // +
	      // e.getMessage());
	    }
  }
  
  
  /**
   * 创建日期：(2004-2-10 18:38:37) 作者：王乃军 参数： 返回： 说明：查询
   */
  protected void query() {
    try {
      getDefaultCalbodyID();

      getConditionDlg().showModal();

      if (getConditionDlg().getResult() != nc.ui.pub.beans.UIDialog.ID_OK)
        // 取消返回
        return;
      StringBuffer headWhere = new StringBuffer();
      processQuerySQL(headWhere);

      // added by songhy, 2008-08-07, start
      nc.vo.pub.query.ConditionVO[] voCond = m_QueryCondDlg.getConditionVO();
     
      // 如果查询条件中输入了公司没有输入组织，则查询该公司的所有有权限的组织。
      // 如果公司组织都为空，则为所有公司的所有有权限的组织
      for (int i = 0; i < voCond.length; i++) {
        String sFieldCode = voCond[i].getFieldCode();
        // 得到“是否待审批”的查询条件对查询结果进行进一步过滤，
        // 因为改条件特殊，需要作为独立的参数
        if (sFieldCode.equals("iswaitaudit")) {
          String iswaitauditStr = voCond[i].getValue(); // 返回是否待审批的字符串 N Y
          if (iswaitauditStr.equals("Y")) {
            // 是否待审批用户选择 “是”
            // 进行查询结果的过滤处理
            isWaitAudit = true;
          }
          else{
            isWaitAudit = false;
          }
        }
      }
      // added by songhy, 2008-08-07, end

      showHintMessage(ConstHintMsg.S_ON_BILL_QUERY/* @res "正在查询" */);

      // modified by songhy, 2008-08-07, start
      // 查表头
      ArrayList alListData = ClientBillHelper.queryBillsBy(
          ConstVO201.saQueryHead, ConstVO201.saQueryBody, headWhere.toString(),
          "5X", this.m_NodeInfo.getUserID(), isWaitAudit); // 5X: 调拨订单
      // modified by songhy, 2008-08-07, end
      querysql = headWhere.toString();
      iqueryStatus = true;
      if (alListData != null && alListData.size() > 0) {
        // 需要解析所有的表头数据
        m_ListCtrl.setListHeadData(alListData);
        // 需要解析第一条的表体数据
        m_ListCtrl.setlistBodyData(1, alListData);
      }

      // 状态栏显示查询结果条数的信息
      if (alListData.size() > 0) {
        String[] value = new String[] {
          String.valueOf(alListData.size())
        };
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
            "UPP40093010-000122", null, value));/* @res "共查到{0}"张单据！" */
        m_Model.setCurRow(0);

      }
      else {
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
            "UPP40093010-000071")/* @res "没有查到您所需要的记录！" */);
        m_Model.setCurRow(-1);
      }
      m_Model.setData(alListData);
      // 当前是表单形式时，首先切换到列表形式
      if (getCurPanel() == GenEditConst.CARD)
        switchPanel();
      else {
        // 显示查询结果:缺省选中第一张
        m_ListCtrl.displayData();
        setButtonStatusBrowse(0);
      }

    }
    catch (Exception e) {
      Debug.debug(e.getMessage(), e);
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
          "UPP40093010-000072")/* @res "查询出错。" */);
      String[] value = new String[] {
        e.getMessage()
      };
      showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "scmcommon", "UPPSCMCommon-000033", null, value));// "查询出错："
      // +
      // e.getMessage());
    }
  }

  /*
   * 处理查询模板上得到的where语句
   */
  private void processQuerySQL(StringBuffer headWhere) throws Exception {
    // 表头where条件
    headWhere.append(" blatest='Y' and ");

    // 单据类型为固定查询条件
    String sbilltype = getNodeInfo().getBillTypeCode();
    if (!sbilltype.startsWith("5")) {
      headWhere.append(" ctypecode in(").append("'").append(
          ConstVO.m_sBillSFDBDD).append("',").append("'").append(
          ConstVO.m_sBillGSJDBDD).append("',").append("'").append(
          ConstVO.m_sBillZZJDBDD).append("',").append("'").append(
          ConstVO.m_sBillZZNDBDD).append("')");
    }
    else {
      headWhere.append(NodeInfo.NAME_HEAD_TABLE).append(".cbilltype='").append(
          sbilltype).append("'");
      /*
       * if (sbilltype.equals(ConstVO.m_sBillZZJDBDD) ||
       * sbilltype.equals(ConstVO.m_sBillZZNDBDD)) { headWhere.append(" and
       * coutcorpid='") .append(m_NodeInfo.getCorpID()) .append("'"); }
       */
    }
    // 表体where条件
    StringBuffer bodyWhere = new StringBuffer("");
    // 查询条件
    ConditionVO[] voaCond = getConditionDlg().getConditionVO();
    // 得到拼成的sql
    TOBillTool.getQueryStr(NodeInfo.NAME_HEAD_TABLE, NodeInfo.NAME_BODY_TABLE,
        headWhere, bodyWhere, voaCond);

    // 查询条件
    if (!sbilltype.startsWith("5")) {
      sbilltype = ConstVO.m_sBillDBDD;
    }
    new TOQueryPrivilegeTool(sbilltype).getQueryStrForOrder40093009(bodyWhere,getNodeInfo().getUserID(),getConditionDlg().getConditionDatas());
    // 单据状态的查询条件
    String sNormalSql = getQueryNormalPanel().getSQL();
    if (sNormalSql != null && sNormalSql.trim().length() > 0) {
      headWhere.append(" and ").append(sNormalSql);
    }
    // 有表体where条件时
    if (bodyWhere.length() > 0) {
      headWhere.append("AND EXISTS (SELECT 1 FROM ").append(
          NodeInfo.NAME_BODY_TABLE).append(" WHERE (").append(
          NodeInfo.NAME_HEAD_TABLE).append(".cbillid = ").append(
          NodeInfo.NAME_BODY_TABLE).append(".cbillid ) ").append(
          bodyWhere.toString()).append(" and ")
          .append(NodeInfo.NAME_BODY_TABLE).append(".dr=0 ) ").append(
              "ORDER BY ").append(NodeInfo.NAME_HEAD_TABLE).append(".vcode");
    }
  }

  protected void switchPanel() {
    int iIndex = m_Model.getCurRow();
    // 显示卡片
    if (getCurPanel() == GenEditConst.LIST) {
      m_CardCtrl.showPanel();
      m_ListCtrl.hidePanel();
      m_iCurPanel = GenEditConst.CARD;

    }
    else { // 显示列表
      m_ListCtrl.showPanel();
      m_CardCtrl.hidePanel();
      m_iCurPanel = GenEditConst.LIST;
    }
    getBillCtrl().displayBill(iIndex);
    showBtnSwitch();
    setButtonStatusBrowse(iIndex);
  }

  /**
   * ****************************************** 功能： <|> 创建日期：(2004-3-10
   * 22:14:28)
   * 
   * @param：
   * @return： ******************************************
   */
  public List<BillVO> approveBill(List<BillVO> lstAuditVOs)
  throws BusinessException {
    GenTimer timer = new GenTimer();

    if (lstAuditVOs == null || lstAuditVOs.size() <= 0) {
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPP40093010-000073")/* @res "单据为空！" */);
    }
    List<BillVO> lstRet = null;
    // 保存下来单据的顺序，以便返回时按此顺序整理数据
    List<String> lstID = new ArrayList<String>();
    for (int i = 0, len = lstAuditVOs.size(); i < len; i++) {
      lstID.add(lstAuditVOs.get(i).getHeaderVO().getCbillid());
    }
    // 把单据VO按照单据类型分开，以便调用各自的动作脚本
    timer.start("@@审批开始：");/*-=notranslate=-*/
    BillVO[] voaAudit = new BillVO[lstAuditVOs.size()];
    for(int i=0,len=voaAudit.length;i<len;i++){
      voaAudit[i] = new BillVO();
      BillHeaderVO header = new BillHeaderVO();
      header.setTs(lstAuditVOs.get(i).getHeaderVO().getTs());
      header.setCbillid(lstAuditVOs.get(i).getHeaderVO().getCbillid());
      voaAudit[i].setParentVO(header);
    }
    //voaAudit = lstAuditVOs.toArray(voaAudit);

    BillVO[] vosRet = null;
    try {
      ArrayList param = new ArrayList();
      param.add(this.m_clientLink);
      Object[] userAry = new Object[voaAudit.length];
      userAry[0] = param;

      //新审批调用，必须保证传递的VO中包含主表VO的单据ID和ts属性，其他属性可忽略，因为在后台还会重新查询出该VO
      HashMap hmPfExParams = new HashMap();
      hmPfExParams.put(PfUtilBaseTools.PARAM_RELOAD_VO, PfUtilBaseTools.PARAM_RELOAD_VO);
      //actionName后面加当前操作员
      vosRet = (BillVO[]) nc.ui.pub.pf.PfUtilClient.runBatch(this,
          "APPROVE"+m_clientLink.getUser(),ConstVO.m_sBillDBDD,getNodeInfo().getLogDate(),voaAudit,userAry,null,hmPfExParams); 
      //原审批调用，
      /*vosRet = (BillVO[]) nc.ui.pub.pf.PfUtilClient.processBatchFlow(this,
            "APPROVE", "5X", getNodeInfo().getLogDate(), voaAudit, userAry);*/
    }
    catch (Exception ex) {
      ExceptionUtils.marsh(ex);
    }
    // 正常审批返回的结果
    if (vosRet != null && vosRet.length > 0) {
      if (lstRet == null) {
        lstRet = new ArrayList<BillVO>(Arrays.asList(vosRet));
      }
      else {
        lstRet.addAll(new ArrayList<BillVO>(Arrays.asList(vosRet)));
      }
    }

    // 按传进来的顺序整理返回数据
    if (lstRet != null)
      resumeVOsAfterApprove(lstRet, lstID);
    timer.showExecTime("@@走平台审批：：");/*-=notranslate=-*/
    return lstRet;
  }

  protected List<BillVO> unApproveBill(List<BillVO> lstUnAuditVOs) throws BusinessException {
    List<BillVO> lstRet = null;
    // 保存下来单据的顺序，以便返回时按此顺序整理数据
    List<String> lstID = new ArrayList<String>();
    for (int i = 0, len = lstUnAuditVOs.size(); i < len; i++) {
      lstID.add(lstUnAuditVOs.get(i).getHeaderVO().getCbillid());
    }
    GenTimer timer = new GenTimer();
    timer.start("@@弃审开始：");/*-=notranslate=-*/

    BillVO[] voaUnAudit = new BillVO[lstUnAuditVOs.size()];
    for(int i=0,len=voaUnAudit.length;i<len;i++){
      voaUnAudit[i] = new BillVO();
      BillHeaderVO header = new BillHeaderVO();
      header.setTs(lstUnAuditVOs.get(i).getHeaderVO().getTs());
      header.setCbillid(lstUnAuditVOs.get(i).getHeaderVO().getCbillid());
      voaUnAudit[i].setParentVO(header);
    }

    BillVO[] vosRet = null;
    ArrayList alParam = new ArrayList();
    try {
      alParam.add(this.m_clientLink);
      Object[] userAry = new Object[voaUnAudit.length];
      userAry[0] = alParam;

      //新审批调用，必须保证传递的VO中包含主表VO的单据ID和ts属性，其他属性可忽略，因为在后台还会重新查询出该VO
      HashMap hmPfExParams = new HashMap();
      hmPfExParams.put(PfUtilBaseTools.PARAM_RELOAD_VO, PfUtilBaseTools.PARAM_RELOAD_VO);
      //actionName后面加当前操作员
      vosRet = (BillVO[]) nc.ui.pub.pf.PfUtilClient.runBatch(this,
          "UNAPPROVE"+m_clientLink.getUser(),ConstVO.m_sBillDBDD,getNodeInfo().getLogDate(),voaUnAudit,userAry,null,hmPfExParams);
      /*原弃审调用方法
        vosRet = (BillVO[]) nc.ui.pub.pf.PfUtilClient.processBatchFlow(this,
            "UNAPPROVE", billtype, getNodeInfo().getLogDate(), voaUnAudit, userAry);*/
    }
    catch (Exception ex) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
      "UPP40093010-000094")/*
       * @res "取消弃审成功!"
       */);
      ExceptionUtils.marsh(ex);
    }
    if (vosRet != null && vosRet.length > 0) {
      // 解析单据状态名称
      BillHeaderVO[] heads = new BillHeaderVO[vosRet.length];
      for (int i = 0, len = vosRet.length; i < len; i++) {
        heads[i] = vosRet[i].getHeaderVO();
      }
      TOBillTool.setNameValueByFlag(new String[] {
          "fstatusflag"
      }, heads);
      if (lstRet == null) {
        lstRet = new ArrayList<BillVO>(Arrays.asList(vosRet));
      }
      else {
        lstRet.addAll(new ArrayList<BillVO>(Arrays.asList(vosRet)));
      }
    }
    // 按传进来的顺序整理返回数据
    if (lstRet != null) {
      resumeVOsAfterApprove(lstRet, lstID);
    }
    timer.showExecTime("@@走平台弃审：：");/*-=notranslate=-*/
    return lstRet;
  }

  /*
   * 按照主表主键字段的顺序重新整理单据VO链表的顺序
   */
  private void resumeVOsAfterApprove(List<BillVO> lstVOs, List<String> lstID) {
    HashMap<String, BillVO> map = new HashMap<String, BillVO>();
    for (int i = 0, len = lstVOs.size(); i < len; i++) {
      map.put(lstVOs.get(i).getHeaderVO().getCbillid(), lstVOs.get(i));
    }
    lstVOs.clear();
    for (int i = 0, len = lstID.size(); i < len; i++) {
      lstVOs.add(map.get(lstID.get(i)));
    }
  }

  /**
   * 把单据VO组合按照单据类型划分开来 return HashMap:key(billtypecode),value(该单据类型的VO链表)
   */
  protected HashMap<String, List<BillVO>> getVOsForBillTypes(
      List<BillVO> lstOrders) {
    HashMap<String, List<BillVO>> hmAuditVOs = new HashMap<String, List<BillVO>>();
    for (int i = 0, len = lstOrders.size(); i < len; i++) {
      String billtype = lstOrders.get(i).getBillTypeCode();
      List<BillVO> lstVOs = hmAuditVOs.get(billtype);
      if (lstVOs == null) {
        lstVOs = new ArrayList<BillVO>();
      }
      lstVOs.add(lstOrders.get(i));
      hmAuditVOs.put(billtype, lstVOs);
    }
    return hmAuditVOs;
  }

  /**
   * 创建者：王乃军 功能： 送审 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
   */
  public BillVO sendAudit(BillVO voBill) throws BusinessException {
    showHintMessage(ConstHintMsg.S_ON_BILL_SEND_AUDIT/* @res "正在送审" */);
    BillHeaderVO voHeader = voBill.getHeaderVO();

    try {
      // 检查送审的单据
      BusinessCheck.checkForSendAudit(voBill);

      // 补充操作员
      voBill.getHeaderVO().setCauditorid(voHeader.getCoperatorid());
      voBill.setOperator(getNodeInfo().getUserID());
      // 放入客户端IP地址,写业务日志用
      voBill.setIPAdress(java.net.InetAddress.getLocalHost().getHostAddress());

      ArrayList alRet = (ArrayList) nc.ui.pub.pf.PfUtilClient.processAction(this, "SAVE", voBill
          .getBillTypeCode(), getNodeInfo().getLogDate(), voBill,
          this.m_clientLink);

      voBill = (BillVO) alRet.get(1);
      voBill.getHeaderVO().setCauditorid(null);
      voBill.getHeaderVO().setDauditdate(null);
      voBill.getHeaderVO().setFstatusflag(ConstVO.IBillStatus_CHECKING);

      if (!nc.ui.pub.pf.PfUtilClient.isSuccess()) {
        throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40093010", "UPP40093010-000090")/*
                                                           * @res "送审操作已经被用户取消！"
                                                           */);
      }
    }
    catch (Exception e) {
      // 还原为自由状态
      voHeader.setFstatusflag(new Integer(ConstVO.IBillStatus_FREE));
      ExceptionUtils.marsh(e);
    }
    return voBill;
  }

  /**
   * 控制扩展按钮状态
   */
  public void setExtendBtnsStat(int iState) {

  }

  private void initbutton() {
    // 设置按钮
    try {
      m_boBtnTree = new ButtonTree(m_NodeInfo.getNodeCode());
      
    }
    catch (BusinessException e) {
      Debug.error(e.getMessage(), e);
    }
    
    m_ButtonCtrl = new ButtonCtrl(m_boBtnTree, m_NodeInfo.getFuncExtend()); 
    //如果是复制调拨订单的节点，它的多语是UPT40093009打头的，但是初始化ButtonTree的时候,按钮的名称缺省是多语，需要把多语翻译成名称
     //但是翻译的时候是按照当前节点号，查找相应的多语资源，然而新增节点是没有对应的多语文件的，因此将nodecode转换成调拨订单的节点号40093009
    if(m_NodeInfo.getNodeCode()==Const.NODECODE_DBDD)
    {
      setButtons(m_boBtnTree.getButtonArray());
    }
    else{
      ButtonObject[] buttons = m_boBtnTree.getButtonArray();
      for(ButtonObject button:buttons){
        String funname =button.getName();
        if(funname.startsWith("UPT40093009")||funname.startsWith("UPT4009"))
        {
          funname = nc.ui.ml.NCLangRes.getInstance().getStrByID(Const.NODECODE_DBDD, funname);
          button.setName(funname);
        }
        if(button.getChildren().size()>0){
          ButtonObject[] ChildButtons = button.getChildButtonGroup();
          for(ButtonObject childbutton:ChildButtons){
            if(!VOChecker.isEmpty(childbutton.getName())&&childbutton.getName().startsWith("UPT40093009"))
            {
              childbutton.setName(nc.ui.ml.NCLangRes.getInstance().getStrByID(Const.NODECODE_DBDD, childbutton.getName()));
            }
          }
        }

      }
      setButtons(buttons);
    }
  

    // 设置业务流程相关的按钮
    initFlowBtn();

    showBtnSwitch();
    setButtonStatusBrowse(-1);
  }


  private void initFlowBtn() {
    PfUtilClient.retBusiAddBtn(m_ButtonCtrl.m_boBusiType, m_ButtonCtrl.m_boNew, getCorpPrimaryKey(), ConstVO.m_sBillDBDD);

    if (m_ButtonCtrl.m_boBusiType.getChildButtonGroup() != null
        && m_ButtonCtrl.m_boBusiType.getChildButtonGroup().length > 0) {
      m_ButtonCtrl.m_boBusiType.setTag(m_ButtonCtrl.m_boBusiType.getChildButtonGroup()[0].getTag());
      m_ButtonCtrl.m_boBusiType.getChildButtonGroup()[0].setSelected(true);
      m_ButtonCtrl.m_boBusiType.setCheckboxGroup(true);
    }
  }

  /**
   * 子类实现该方法，响应按钮事件。
   * 
   * @version (00-6-1 10:32:59)
   * @param bo
   *          ButtonObject
   */
  public void onButtonClicked(nc.ui.pub.ButtonObject bo) {

    try {
      // 二次开发扩展
      getPluginProxy().beforeButtonClicked(bo);
      m_btnEventHandler.onBtnClick(bo);
      if (bo.getParent() == m_ButtonCtrl.m_boBusiType) {
        setButtons(m_boBtnTree.getButtonArray());
        bo.setSelected(true);
      }
      updateButtons();
      // 二次开发扩展
      getPluginProxy().afterButtonClicked(bo);
    }
    catch (Exception ex) {
      ExceptionUITools.showMessage(ex, this);
    }

  }

  public boolean onClosing() {
    if (m_CardCtrl.getEditMode() == GenEditConst.UPDATE
        || m_CardCtrl.getEditMode() == GenEditConst.NEW) {
      int iflag = showYesNoCancelMessage(ConstHintMsg.S_IS_SAVE_UPDATE);// 是否保存已修改的数据？
      try {
        // 如果保存,保存退出
        if (iflag == MessageDialog.ID_YES) {
          m_btnEventHandler.onSave();
          return true;
          // 不保存退出
        }
        else if (iflag == MessageDialog.ID_NO) {
          return true;
          // 不退出
        }
        else if (iflag == MessageDialog.ID_CANCEL) {
          return false;
        }
        return false;
      }
      catch (Exception e) {
        ExceptionUITools.showMessage(e, this);
        return false;
      }
    }
    return true;

  }

  /**
   * showBtnSwitch 符合界面规范
   * 
   * @author leijun 2006-5-24
   */
  public void showBtnSwitch() {
    if (m_iCurPanel == GenEditConst.CARD)
      m_ButtonCtrl.m_boSwitch.setName(nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("common", "UCH022")/* @res "列表显示" */);
    else
      m_ButtonCtrl.m_boSwitch.setName(nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("common", "UCH021")/* @res "卡片显示" */);
    updateButton(m_ButtonCtrl.m_boSwitch);

  }

  /**
   * 创建日期：(2004-2-12 10:22:31) 作者：王乃军 参数：int iNum:业务单序号 返回： 说明：设置按钮状态
   */
  protected void setButtonStatusBrowse(int iNum) {
    // 当前在列表选中的单据。
    // 业务单状态。
    int iStatus = ConstVO.IBillStatus_FREE;
    // 能否进行退回
    boolean bReject = false;
    // 能否做补货安排
    boolean bPlan3Z = false;
    // 能否做发货安排
    boolean bPlan4331 = false;
    // 取消结算路径是否可用
    boolean bDelSettlePath = false;
    // 审批人Id
    String auditorId = null;
    BillVO voThis = (BillVO) m_Model.getData(iNum);    

    if (voThis != null && voThis.getParentVO() != null) {
      //排序时有可能表体为空
      if(voThis.getItemVOs()==null||voThis.getItemVOs().length==0){
        StringBuffer sql = new StringBuffer(" ");
        sql.append(" cbillid = '");
        sql.append(voThis.getHeaderVO().getPrimaryKey());
        sql.append("' and dr=0 ");
        try {
          BillItemVO[] itemVOs = nc.ui.to.pub.ClientBillHelper.queryBodyBySQL(null,sql.toString());

          //解析表体数据
          m_ListCtrl.parseBodyData(itemVOs);
          
          voThis.setChildrenVO(itemVOs);
        }
        catch (Exception e) {
          ExceptionUITools.showMessage(e, this);
        }
      }
      
      BillHeaderVO voTemp = (BillHeaderVO) voThis.getParentVO();
      iStatus = voTemp.getFstatusflag();
      
      // 可以退回状态的单据
      if (iStatus == ConstVO.IBillStatus_PASSCHECK
          || iStatus == ConstVO.IBillStatus_CLOSED) {
        // 是否有来源单据的标志
        String sSourceType = ((BillVO) voThis).getItemVOs()[0]
            .getCsourcetypecode();
        UFBoolean bHasReturned = ((BillVO) voThis).getItemVOs()[0]
            .getBretractflag();
        // 如果已经退回的单据，不能再次退回
        if ((sSourceType == null || !sSourceType.equals(voThis
            .getBillTypeCode()))
            && (bHasReturned != null && !bHasReturned.booleanValue()))
          bReject = true;
      }

      // 出货公司等于当前登陆公司，审批通过
      if (voTemp.getCtakeoutcorpid() != null
          && voTemp.getCtakeoutcorpid().equals(getCorpPrimaryKey())
          && iStatus == ConstVO.IBillStatus_PASSCHECK) {
        bPlan3Z = true;
      }
      
      // 出货公司等于当前登陆公司，审批通过
      if (voTemp.getCtakeoutcorpid() != null
          && voTemp.getCtakeoutcorpid().equals(getCorpPrimaryKey())
          && iStatus == ConstVO.IBillStatus_PASSCHECK) {
        bPlan4331 = true;
      }

      if (voTemp.getCsettlepathid() != null) {
        bDelSettlePath = true;
      }

      // added by songhy, 2008-08-07, start
      // V55SCM公共需求：12 审批流相关调整
      // 1、 单据送审后，由自由态变为审批中状态。审批中状态的单据不允许删除
      // 2、 无审批人并且处于“审批中”状态的单据可以修改
      // h.cauditorid
      auditorId = (String) voTemp.getAttributeValue("cauditorid");
      // added by songhy, 2008-08-07, end
    }
    m_ButtonCtrl.setButtonStatusOfBrowse(m_iCurPanel, m_Model.getCount(), iNum,
        iStatus, bReject, bPlan3Z,bPlan4331, bDelSettlePath, auditorId,iqueryStatus);
    if (voThis != null) {
      // 判断单据是否需要送审(可以多次送审)
      boolean bSendAudit = TOBillTool.isNeedSendAudit(
          iStatus, voThis.getBillTypeCode(), 
          voThis.getHeaderVO().getCoutcorpid(), 
          voThis.getHeaderVO().getCbiztypeid(),
          voThis.getHeaderVO().getCbillid(), 
          voThis.getHeaderVO().getCoperatorid());
      m_ButtonCtrl.m_boSendAudit.setEnabled(bSendAudit);
      
      if (iStatus == ConstVO.IBillStatus_FREE)
        m_ButtonCtrl.m_boAudit.setEnabled(true);
      // 调拨类型为直运，不能复制，不能退回
      Integer fallocflag = voThis.getHeaderVO().getFallocflag();
      if (fallocflag != null
          && ConstVO.ITransferType_DIRECT == fallocflag.intValue()) {
        m_ButtonCtrl.m_boCopy.setEnabled(false);
        m_ButtonCtrl.m_boReturn.setEnabled(false);
      }
    }

    // 使设置生效,必须！
    super.updateButtons();
    
    //二次开发扩展
    getPluginProxy().setButtonStatus();

  }

  public void getDefaultCalbodyID() {
    // 获取当前登录库存组织
    if (m_NodeInfo.getCalbodyID() == null) {
      m_NodeInfo.setCalbodyID(getCalbodyPK(m_NodeInfo.getCorpID(), m_NodeInfo
          .getUserID()));
      // 通知其他的Ctrl
      getBillCtrl().setNodeInfo(m_NodeInfo);
    }
  }

  protected ClientLink m_clientLink;

  protected PrintEntry m_print;

  // 用于联查的单据id
  protected String m_sBillID;

  /**
   * 创建日期：(2004-2-11 14:31:51) 作者：王乃军 参数： 返回： 说明：panel事件响应方法。
   * 
   * @param pe
   *          nc.ui.scm.pub.ctrl.PanelEvent
   */
  public Object action(GenPanelEvent pe) {
    // 来源
    switch (pe.getSourceID()) {
      // 列表
      case GenPanelEvent.ID_FIRST_LIST:
        // 类型
        switch (pe.getType()) {
          case GenPanelEvent.SWITCH: // 切换
            // 如果是拉式参照则进入拉式编辑状态
            if (getIsComeFromOther()) {
              try {
                m_btnEventHandler.onUpdate();
              }
              catch (Exception ex) {
                ExceptionUITools.showMessage( ex ,this);
              }
            }
            else {
              m_btnEventHandler.onSwitch();
            }
            break;

          case GenPanelEvent.SEL_HEAD: // 表头
            if (getIsComeFromOther()) {
              setButtonStatusOfRef();
            }
            else {
              setButtonStatusBrowse(m_Model.getCurRow());
              doFuncExtendBtn(nc.ui.scm.extend.IFuncExtend.LIST,
                  nc.ui.pub.bill.BillItem.HEAD);
            }
            break;
          case GenPanelEvent.SEL_BODY: // 表体
            doFuncExtendBtn(nc.ui.scm.extend.IFuncExtend.LIST,
                nc.ui.pub.bill.BillItem.BODY);
            break;

          case GenPanelEvent.HINT: // 显示提示信息
            showHintMessage(pe.getValue().toString());
            break;

        }
        break;

      case GenPanelEvent.ID_FIRST_CARD:
        switch (pe.getType()) {
          case GenPanelEvent.HINT: // 显示提示信息
            if (pe.getValue() != null) {
              showHintMessage(pe.getValue().toString());
            }
            break;
          case GenPanelEvent.ERR:// 显示提示信息
            showErrorMessage(pe.getValue().toString());
            return null;
          case GenPanelEvent.ASK:
            if (pe.getValue() instanceof Integer) {
              if (((Integer) pe.getValue()).intValue() == Const.IASK_CARD_EDITABLE) {
                // 卡片是否可编辑
                if (m_CardCtrl.getEditMode() != GenEditConst.NEW
                    && m_CardCtrl.getEditMode() != GenEditConst.UPDATE) {
                  return Boolean.FALSE;
                }
                else {
                  return Boolean.TRUE;
                }
              }
              else if (((Integer) pe.getValue()).intValue() == Const.IASK_CARD_FRESHSCALE) {
                m_CardCtrl.getCardPanel().setScaleOfCardPanel(
                    m_CardCtrl.getCardPanel().getBillCardPanel().getBillData());
                m_ListCtrl.getListPanel().setScaleOfListPanel(
                    m_ListCtrl.getListPanel().getBillListPanel()
                        .getBillListData());
              }
              else if (((Integer) pe.getValue()).intValue() == Const.IASK_CARD_BODYROWCHANGE) {
                doFuncExtendBtn(nc.ui.scm.extend.IFuncExtend.CARD,
                    nc.ui.pub.bill.BillItem.BODY);
                if (m_CardCtrl.isOnhandShowHidden())
                  m_CardCtrl.freshOnhandnum(m_CardCtrl.getSelectedRow());
              }
            }
            break;
        }
        break;
    }
    return null;
  }

  // 支持功能扩展
  private void doFuncExtendBtn(int iCurPanel, int iPos) {
/*    if (m_NodeInfo.getFuncExtend() != null) {
      m_NodeInfo.getFuncExtend().rowchange(this,
          m_CardCtrl.getCardPanel().getBillCardPanel(),
          m_ListCtrl.getListPanel().getBillListPanel(), iCurPanel, iPos);
    }*/
  }

  /**
   * 返回VO 创建日期：(2001-12-18 16:59:11)
   * 
   * @return nc.vo.pub.AggregatedValueObject
   */
  public nc.vo.pub.AggregatedValueObject getVo() throws java.lang.Exception {
    BillVO voBill = null;
    BillHeaderVO[] voaHeader = null;
    BillItemVO[] voaBody = null;
    try {
      StringBuffer sbHeadWhere = new StringBuffer();
      sbHeadWhere.append(NodeInfo.NAME_HEAD_TABLE);
      sbHeadWhere.append(".cbillid='" + m_sBillID + "'");
      sbHeadWhere.append(" and dr=0 ");
      voaHeader = nc.ui.to.pub.ClientBillHelper.queryHeadBySQL(
          nc.vo.to.to201.ConstVO201.saQueryHead, sbHeadWhere.toString());
      // 再用表头第一张单据去查表体记录
      if (voaHeader != null && voaHeader.length > 0) {
        StringBuffer sBodyWhere = new StringBuffer();
        sBodyWhere.append(NodeInfo.NAME_BODY_TABLE + ".cbillid='"
            + voaHeader[0].getCbillid() + "' and dr=0 ");
        // 判断调拨订单的调出公司是否和当前登录公司相同,如果相同,则加入权限控制的语句,否则不考虑权限
        if (m_NodeInfo.getCorpID().equals(voaHeader[0].getCoutcorpid())) {
          // 加入权限控制的语句
          new TOQueryPrivilegeTool(getNodeInfo().getBillTypeCode())
              .getQueryStrForOrder(sBodyWhere, getNodeInfo().getUserID());
        }
        voaBody = nc.ui.to.pub.ClientBillHelper.queryBodyBySQL(
            nc.vo.to.to201.ConstVO201.saQueryBody, sBodyWhere.toString());

        if (voaBody != null && voaBody.length > 0) {
          voBill = new BillVO();
          voBill.setParentVO(voaHeader[0]);
          voBill.setChildrenVO(voaBody);
          voBill.setOperator(m_NodeInfo.getUserID());
          // 执行加载公式
          ArrayList alData = new ArrayList();
          alData.add(voBill);
          m_ListCtrl.setListHeadData(alData);
          m_ListCtrl.setlistBodyData(1, alData);
          // 解析自由项
          TOBillTool.getFreeInfo(voBill.getItemVOs());
        }
      }
    }
    catch (java.lang.Exception e) {
      Log.error(e);
      throw e;
    }
    // 没有符合条件的单据
    if (voBill == null) {
      return null;
    }
    return voBill;
  }

  /**
   * 函数功能: 参数: 返回值: 异常:
   * 
   * @param newNodeInfo
   *          nc.ui.to.pubtransfer.NodeInfo
   */
  public void setNodeInfo(NodeInfo newNodeInfo) {
    m_NodeInfo = newNodeInfo;
  }

  /**
   * 此处插入方法说明。 创建日期：(2005-11-22 10:29:00)
   */
  public void setButtonStatusEdit() {
    if (m_CardCtrl.getEditMode() == GenEditConst.UPDATE) {
      BillVO voThis = (BillVO) m_Model.getCurVO();
      if (voThis != null) {
        // 是否来源直运销售订单
        boolean bDrictFlag = ((BillVO) voThis).getHeaderVO().getBdrictflag()
            .booleanValue();

        String sbiztypeid = ((BillVO) voThis).getHeaderVO().getCbiztypeid();
        // 得到当前单据所在业务流程是否可以自制
        boolean bSelfMakeFlag = canSelfMake(sbiztypeid);
        // 物资需求汇总平衡只有一条表体
        String csourceTypeCode = voThis.getItemVOs()[0].getCsourcetypecode();
        // 是否可增行
        boolean bCanAddLine = true;
        // 来源直运、物资需求汇总平衡或调拨订单不可自制的，单据不可增行
        if ("422Y".equals(csourceTypeCode) || bDrictFlag || !bSelfMakeFlag) {
          bCanAddLine = false;
        }
        // 更新按钮状态
        m_ButtonCtrl.setEditStatus(m_CardCtrl.getEditMode(), bCanAddLine);
        // 处理右键菜单
        m_CardCtrl.setBodyMenuEdit(bCanAddLine);
      }
    }
    else if (m_CardCtrl.getEditMode() == GenEditConst.NEW) {
      m_ButtonCtrl.setEditStatus(GenEditConst.NEW, true);
      
      m_CardCtrl.setBodyMenuEdit(true);
    }
    // 使设置生效,必须！
    super.updateButtons();
    //二次开发扩展
    getPluginProxy().setButtonStatus();
  }

  // 得到当前单据所在业务流程是否可以自制
  private boolean canSelfMake(String sbiztypeid) {
    boolean selfMake = true;
    String makebillflag = "";
    SqlBuilder sql = new SqlBuilder();
    sql
        .append("select makebillflag from pub_billbusiness where pk_businesstype='");
    sql.append(sbiztypeid);
    sql.append("' and pk_billtype='");
    sql.append(m_NodeInfo.getBillTypeCode());
    sql.append("'");
    try {
      String[][] ret = ClientCommonDataHelper.queryData(sql.toString());
      if (ret != null && ret.length != 0 && ret[0] != null
          && ret[0].length != 0 && ret[0][0] != null) {
        makebillflag = ret[0][0];
        if (makebillflag.equals("N")) {
          selfMake = false;
        }
      }
    }
    catch (BusinessException e) {
      Debug.debug(e.getMessage(), e);
    }
    return selfMake;
  }

  public void doQueryAction(ILinkQueryData querydata) {
    // TODO 自动生成方法存根
    if (querydata == null)
      return;
    BillVO vo = qryBillForLinkOperate(querydata.getBillID());
    initForLinkQuery(vo);
    showBtnSwitch();
    setButtonStatusBrowse(0);
  }

  public int getCurPanel() {
    return m_iCurPanel;
  }

  public void doMaintainAction(ILinkMaintainData maintaindata) {
    // TODO 自动生成方法存根
    if (maintaindata == null)
      return;
    BillVO vo = qryBillForLinkOperate(maintaindata.getBillID());
    initForLinkQuery(vo);
    showBtnSwitch();
    setButtonStatusBrowse(0);
  }

  // 设置参照单据到列表
  public void setReftoListPanel() {
    if (m_Model.getData().size() > 0) {
      m_Model.setCurRow(0);
    }
    setIsComeFromOther(true);
    // 当前是表单形式时，首先切换到列表形式
    if (getCurPanel() == GenEditConst.CARD) {
      m_ListCtrl.showPanel();
      m_CardCtrl.hidePanel();
      m_iCurPanel = GenEditConst.LIST;
    }
    if (m_Model.getData() != null && m_Model.getData().size() > 0) {
      // 需要解析所有的表头数据
      m_ListCtrl.setListHeadData(m_Model.getData());
      // 需要解析第一条的表体数据
      BillVO billVo = null;
      BillItemVO[] itemVos = null;
      ArrayList<BillItemVO> alItemVos = new ArrayList<BillItemVO>();
      for (int i = 0; i < m_Model.getData().size(); i++) {
        billVo = (BillVO) m_Model.getData().get(i);
        itemVos = billVo.getItemVOs();
        // 解析自由项
        TOBillTool.getFreeInfo(itemVos);
        alItemVos = new ArrayList<BillItemVO>(Arrays.asList(itemVos));
        // 行状态标志
        TOBillTool.setNameValueByFlag(new String[] {
          "frowstatuflag"
        }, itemVos);

        if (alItemVos != null && alItemVos.size() > 0) {
          // 通过单据公式容器类执行有关公式解析的方法
          m_ListCtrl.getFormulaContainer().formulaBodys(new ArrayList(),
              alItemVos);
        }
      }
    }
    // 显示查询结果:缺省选中第一张
    m_ListCtrl.displayData();

    // 设置按钮状态为参照保存
    setButtonStatusOfRef();
  }

  // 点击取消时调用
  public void setReftoListPanelForCancel() {
    if (m_Model.getData().size() > 0) {
      m_Model.setCurRow(0);
    }
    setIsComeFromOther(true);
    // 当前是表单形式时，首先切换到列表形式
    if (getCurPanel() == GenEditConst.CARD) {
      m_ListCtrl.showPanel();
      m_CardCtrl.hidePanel();
      m_iCurPanel = GenEditConst.LIST;
    }
    // 显示查询结果:缺省选中第一张
    m_ListCtrl.displayData();

    // 设置按钮状态为参照保存
    setButtonStatusOfRef();
  }

  // 设置按钮状态为参照保存
  public void setButtonStatusOfRef() {
    m_ButtonCtrl.setButtonStatusOfRef();
    // 使设置生效,必须！
    super.updateButtons();
    //二次开发扩展
    getPluginProxy().setButtonStatus();
  }

  // 是否外来单据
  private boolean isComeFromOther = false;

  public void setIsComeFromOther(boolean b) {
    isComeFromOther = b;
  }

  public boolean getIsComeFromOther() {
    return isComeFromOther;
  }

  public void onSwitchForRef() {
    int iIndex = m_Model.getCurRow();
    // 显示卡片
    if (getCurPanel() == GenEditConst.LIST) {
      m_CardCtrl.showPanel();
      m_ListCtrl.hidePanel();
      m_iCurPanel = GenEditConst.CARD;

    }
    getBillCtrl().displayBill(iIndex);
    // setButtonStatusBrowse(iIndex);
  }

  public Model getModelFrom5A() {
    if (m_ModelFrom5A == null) {
      m_ModelFrom5A = new Model();
    }
    return m_ModelFrom5A;
  }

  public HashSet getRefBillIds() {
    return refBillIds;
  }

  // 补货直运安排节点使用
  public void returnToMainUI() {
    removeAll();

    m_CardCtrl.addPanel(this);
    m_ListCtrl.addPanel(this);

    switchPanel();
    
    //当从补货直运界面返回时，需要回复原有调拨订单的按钮权限
    setButtons(m_boBtnTree.getButtonArray(),"40093009");

    // 设置业务流程相关的按钮
    initFlowBtn();

    updateUI();

  }

  /**
   * 加载补货直运界面
   * 
   * @param newVO
   *          需要放到表头的数据 IUseSupplyTrans UI, String
   *          billType,AggregatedValueObject[] vos
   */
  public void addBHZYCardUI(BillVO newVO) throws Exception {
    this.m_BillToBillUI = (IBillToBillFor5X) Class.forName(
        "nc.ui.so.so003.BillToBillUI").getConstructor(new Class[] {
        IUseSupplyTrans.class, String.class, AggregatedValueObject[].class
    }).newInstance(this, "5X", new BillVO[] {
      newVO
    });

    // 加载补货直运安排界面
    removeAll();
    add((ToftPanel) this.m_BillToBillUI, "Center");
    // GenEditConst.SOURCE代表补货直运界面
    m_iCurPanel = GenEditConst.SOURCE;
    this.m_BillToBillUI.setVisible(true);
    updateUI();

    //打开补货直运界面，直接采用补货直运界面的按钮权限替代调拨订单界面的按钮权限
    ButtonObject[] btns = this.m_BillToBillUI.setButtenForUse("5X");
    super.setButtons(btns,"40060402");
    updateButtons();
  }

  /**
   * 补货直运按钮响应调用
   */
  public void onBHZYBtnClick(ButtonObject bo) {
    ButtonObject[] btns = this.m_BillToBillUI.getButtons();
    for (int i = 0, loop = btns.length; i < loop; i++) {
      if (btns[i] == bo || btns[i] == bo.getParent()) {
        // 循环走补货直运界面的按钮响应。
        this.m_BillToBillUI.onButtonClicked(bo);
        updateButtons();
        return;
      }
    }

  }

  public boolean onEditAction(int action) {
    try {
      // m_CardCtrl.getCardPanel().getBillCardPanel().removeActionListener();
      m_CardCtrl.getCardPanel().getBillCardPanel().removeActionListener("base");
      m_CardCtrl.getCardPanel().getBillCardPanel().removeActionListener(
          "relation");
      m_CardCtrl.getCardPanel().getBillCardPanel()
          .removeActionListener("exec2");
      m_CardCtrl.getCardPanel().getBillCardPanel().removeActionListener("free");
      m_CardCtrl.getCardPanel().getBillCardPanel().getBillModel()
          .setNeedCalculate(false);

      switch (action) {
        case BillTableLineAction.ADDLINE:
          m_btnEventHandler.onAddLine();
          break;
        case BillTableLineAction.DELLINE:
          m_btnEventHandler.onDeleteLine();
          break;
        default:
          return true;
      }

    }
    catch (BusinessException e) {
      GenMsgCtrl.printErr(e.getMessage());
    }
    finally {
      // m_CardCtrl.getCardPanel().getBillCardPanel().addActionListener(this);
      m_CardCtrl.getCardPanel().getBillCardPanel().addActionListener("base",
          this);
      m_CardCtrl.getCardPanel().getBillCardPanel().addActionListener(
          "relation", this);
      m_CardCtrl.getCardPanel().getBillCardPanel().addActionListener("exec2",
          this);
      m_CardCtrl.getCardPanel().getBillCardPanel().addActionListener("free",
          this);
      m_CardCtrl.getCardPanel().getBillCardPanel().getBillModel()
          .setNeedCalculate(true);
    }

    return false;
  }
}
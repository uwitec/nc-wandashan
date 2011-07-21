package nc.ui.to.pubtransfer;

/**
 * �������ڣ�(2004-2-9 11:45:59) ���ߣ����˾� ˵����** ����ģ�� **ҵ����档
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
 * �������ڣ�(2004-2-9 11:45:59) ���ߣ����˾� ˵����** ����ģ�� **ҵ����档
 */

public class TransferClientUI extends nc.ui.to.pub.TOToftPanel implements
    nc.ui.scm.pub.ctrl.GenPanelEventListener, IBillExtendFun,
    nc.ui.pf.query.ICheckRetVO, ILinkQuery, ILinkApprove, ILinkMaintain,
    IUseSupplyTrans, BillActionListener {
  // �����������ʱ�����ε��ݱ�����id
  private HashSet refBillIds = new HashSet();

  /**
   * ����ֱ�˽���
   */
  private IBillToBillFor5X m_BillToBillUI;

  // ��Ƭ����
  public CardPanelCtrl m_CardCtrl;

  // �б����
  public ListPanelCtrl m_ListCtrl;

  // ��ť��������
  protected ButtonCtrl m_ButtonCtrl;

  public ButtonTree m_boBtnTree;

  private ButtonEventHandler m_btnEventHandler;
  
  private boolean iqueryStatus = false;
  
  boolean isWaitAudit = false;
  
  String querysql = null;

  // //����
  protected Model m_Model;

  protected Model m_ModelFrom5A;

  // �ڵ㹲����Ϣ�����á�
  protected NodeInfo m_NodeInfo = null;

  // ��ǰ��ʾ��panel
  private int m_iCurPanel;

  // ��ѯ�Ի���
  private nc.ui.scm.pub.query.SCMQueryConditionDlg m_QueryCondDlg;

  // �����������
  private QueryNormalPane m_panelQuery = null;
  
//���ο�����չ
  private InvokeEventProxy pluginproxy;
  
  public InvokeEventProxy getPluginProxy() {
    if(this.pluginproxy==null)
      this.pluginproxy = new InvokeEventProxy(ConstVO.m_sModule,"5X",new TOPluginUI(this,"5X"));
      return this.pluginproxy;
  }
  

  /**
   * ClientUI ������ע�⡣
   */
  public TransferClientUI() {
    super();
    // ��ȡ��������
    initNodeInfo();
    // ��ʼ��Toftpanel
    initialize();
    // ���ð�ť
    initbutton();
  }
  
  /**
   * ClientUI ������ע�⡣
   */
  public TransferClientUI(FramePanel frame_pane) {
    super();
    // ��ȡ��������
    initNodeInfo(frame_pane);
    // ��ʼ��Toftpanel
    initialize();
    // ���ð�ť
    initbutton();
  }
  
  public TransferClientUI(String billID) {
    // ��ȡ��������
    initNodeInfo();
    // ��ʼ��Toftpanel
    initialize();
    BillVO vo = qryBillForLinkOperate(billID);
    // ״̬����ʾ��ѯ�����������Ϣ
    if (vo == null) {
      String msg = nc.ui.ml.NCLangRes.getInstance().getStrByID("topub",
      "UPPtopub-000036")/* @res "���ݿ����Ѿ���ɾ����" */;
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
   * �˴����뷽��˵���� �������ڣ�(2001-4-25 16:15:06)
   */
  private QueryNormalPane getQueryNormalPanel() {

    if (m_panelQuery == null) {
      m_panelQuery = new QueryNormalPane();
    }

    return m_panelQuery;
  }

  /**
   * �����ߣ����˾� ���ܣ��õ���ѯ�Ի��� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  protected nc.ui.scm.pub.query.SCMQueryConditionDlg getConditionDlg() {
    if (m_QueryCondDlg == null) {
      try {
        m_QueryCondDlg = new nc.ui.scm.pub.query.SCMQueryConditionDlg(this);
        
        m_QueryCondDlg.setTempletID(m_NodeInfo.getCorpID(), m_NodeInfo
            .getNodeCode(), m_NodeInfo.getUserID(), null, null);

        // ����Ϊ�Բ��յĳ�ʼ��
        m_QueryCondDlg.initQueryDlgRef();

        // ���س�������
        m_QueryCondDlg.hideUnitButton();
        m_QueryCondDlg.setNormalShow(true);
        m_QueryCondDlg.getUIPanelNormal().add(getQueryNormalPanel());
        
        // ������˾Ĭ��Ϊ��ǰ��½��˾
        m_QueryCondDlg.setDefaultValue("to_bill.coutcorpid", m_NodeInfo
            .getCorpID(), m_NodeInfo.getCorpID());

        // ��ʾ��ӡ״̬��ѯPanel
        m_QueryCondDlg.setShowPrintStatusPanel(true);
        
        
        // ������������ʾ,�ɲ�ѯģ�岻֧�֡�=��
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
        
        
        
        // �����ѯģ�����Զ�����
        DefSetTool.updateQueryConditionClientUserDef(m_QueryCondDlg, m_NodeInfo
            .getCorpID(), Const.USERDEF_TYPE_TOBILL, "to_bill.vdef",
            "to_bill_b.vbdef");

        nc.vo.pub.query.QueryConditionVO[] voaConData = m_QueryCondDlg
            .getConditionDatas();

        // ����Ϊ�Բ��յĳ�ʼ��

        String sbilltype = getNodeInfo().getBillTypeCode();
        if (!sbilltype.startsWith("5")) {
          sbilltype = ConstVO.m_sBillSFDBDD;
        }
        TOQueryDlgListener listener = new TOQueryDlgListener(m_QueryCondDlg,
            sbilltype);
        HashMap<String, String> hmFieldCode = new HashMap<String, String>();
        nc.ui.pub.beans.UIRefPane ref = null;
        // �̶�����
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
              ref.addValueChangedListener(listener);// ���빫˾
              ref.setRefNodeName("��˾Ŀ¼(����)");
            }
            else if (sFieldCode.equals("to_bill_b.cindeptid")) {
              ref.setName("QueryInDept");
              // ��ѯģ�����й̶�����
              if (hmFixedData.get("to_bill.cincorpid") != null) {
                ref.setWhereString(" pk_corp = '"
                    + hmFixedData.get("to_bill.cincorpid") + "'");
              }
              else {
                // û����Ĭ��ֵʱ����ʼ��Ϊ��
                ref.setWhereString("1<0");
              }
            }
            else if (sFieldCode.equals("to_bill_b.cinpsnid")) {
              ref.setName("QueryInPsn");
              // ��ѯģ�����й̶�����
              if (hmFixedData.get("to_bill.cincorpid") != null) {
                ref.setWhereString(" pk_corp = '"
                    + hmFixedData.get("to_bill.cincorpid") + "'");
              }
              else {
                // û����Ĭ��ֵʱ����ʼ��Ϊ��
                ref.setWhereString("1<0");
              }
            }
            else if (sFieldCode.equals("to_bill.cincbid")) {
              ref.setName(TOQueryDlgListener.REFNAME_INCB);
              ref.addValueChangedListener(listener);// ������֯

              ref.getRefModel().setUseDataPower(false);

              // ��ѯģ�����й̶�����
              if (hmFixedData.get("to_bill.cincorpid") != null) {
                ref.setWhereString(" pk_corp = '"
                    + hmFixedData.get("to_bill.cincorpid")
                    + "'and property <> "
                    + String.valueOf(ConstVO.ICalbodyType_COST));
              }
              else {
                // û����Ĭ��ֵʱ����ʼ��Ϊ��
                ref.setWhereString("1<0");
              }

            }
            else if (sFieldCode.equals("to_bill.cinwhid"))// ����ֿ�
            {
              ref.setName(TOQueryDlgListener.REFNAME_INWH);
              ref.setWhereString(" pk_corp = null");
              ref.getRefModel().setUseDataPower(false);
            }
            else if (sFieldCode.equals("to_bill_b.cindeptid")) {
              ref.setName(TOQueryDlgListener.REFNAME_INDEPT);
              ref.addValueChangedListener(listener);// ���벿��
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
              ref.addValueChangedListener(listener); // ������˾
              ref.setRefNodeName("Ȩ�޹�˾Ŀ¼(����)");
            }
            else if (sFieldCode.equals("to_bill.coutcbid")) {
              ref.setName(TOQueryDlgListener.REFNAME_OUTCB);
              // //��ѯģ�����й̶�����
              if (hmFixedData.get("to_bill.coutcorpid") != null) {
                ref.setWhereString(" pk_corp = '"
                    + hmFixedData.get("to_bill.coutcorpid")
                    + "'and property <> "
                    + String.valueOf(ConstVO.ICalbodyType_COST));
              }
              else {
                // //û����Ĭ��ֵʱ��Ĭ��Ϊ��ǰ��¼��˾
                ref.setWhereString(" pk_corp = '" + m_NodeInfo.getCorpID()
                    + "' and property <> "
                    + String.valueOf(ConstVO.ICalbodyType_COST));
              }

              ref.addValueChangedListener(listener);// ������֯
              // ������֯��ѯ����Ȩ�޿���
              // ref.getRefModel().setUseDataPower(false);
            }
            else if (sFieldCode.equals("to_bill.coutwhid"))// �����ֿ�
            {
              ref.setName(TOQueryDlgListener.REFNAME_OUTWH);
              ref.setWhereString(" pk_corp = null");
              // ref.getRefModel().setUseDataPower(false);
            }
            else if (sFieldCode.equals("to_bill.ctakeoutcorpid")) {
              ref.setName(TOQueryDlgListener.REFNAME_TAKEOUTCORP);
              ref.addValueChangedListener(listener); // ������˾
            }
            else if (sFieldCode.equals("to_bill.ctakeoutcbid")) {
              ref.setName(TOQueryDlgListener.REFNAME_TAKEOUTCB);
              ref.addValueChangedListener(listener); // ������֯
              // ��ѯģ�����й̶�����
              if (hmFixedData.get("to_bill.ctakeoutcorpid") != null) {
                ref.setWhereString(" pk_corp = '"
                    + hmFixedData.get("to_bill.ctakeoutcorpid")
                    + "'and property <> "
                    + String.valueOf(ConstVO.ICalbodyType_COST));
              }
              else {
                // û����Ĭ��ֵʱ����ʼ��Ϊ��
                ref.setWhereString("1<0");
              }
            }
            else if (sFieldCode.equals("to_bill_b.btakeoutwhid"))// �����ֿ�
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
      
      //�������ڣ�Ĭ�ϵ��죨���ڵ��ڣ�,�������ڣ�Ĭ�ϵ��죨С�ڵ��ڣ�
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
    /* ��ѯ�������� */
    BillVO vo = null;
    try {
      vo = (BillVO) getVo();
    }
    catch (Exception e) {
      Log.error(e);
      nc.ui.pub.beans.MessageDialog
          .showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                  "UPPSCMCommon-000428")/* @res "ϵͳ���ϣ�" */);
    }
    return vo;
  }
  
  

  /**
   * ����ʵ�ָ÷���������ҵ�����ı��⡣
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
   * �������ڣ�(2004-2-9 13:49:30) ���ߣ����˾� ������ ���أ� ˵������ʼ������
   */
  public void initialize() {
    try {
      // ����
      m_Model = new Model();
      initCardCtrl();
      initListCtrl();
      // ���ؽ���
      this.removeAll();
      setLayout(new java.awt.CardLayout());
      m_CardCtrl.addPanel(this);
      m_ListCtrl.addPanel(this);
      m_btnEventHandler = new ButtonEventHandler(this);

      // ��ʾ��Ƭ
      m_iCurPanel = GenEditConst.CARD;
      m_CardCtrl.showPanel();

//      // �ڡ���Ƭ��塱�ı��嵯��ʽ�˵�����ӡ���Ƭ�༭����
//      addCardEditBodyMenu("base");
//      addCardEditBodyMenu("relation");
//      addCardEditBodyMenu("exec2");
//      addCardEditBodyMenu("free");
//
//      // �ڡ���Ƭ��塱�ı��嵯��ʽ�˵�����ӡ������кš���
//      addNewRowNoBodyMenu("base");
//      addNewRowNoBodyMenu("relation");
//      addNewRowNoBodyMenu("exec2");
//      addNewRowNoBodyMenu("free");
//      
//      // �ڡ���Ƭ��塱�ı��嵯��ʽ�˵�����ӡ�ѯ�ۡ���
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
   * ��ָ���ı���ҳǩ�����ӡ���Ƭ�༭������ʽ�˵���
   * 
   * @author songhy 2008-08-14
   * @param strTableCode
   *          ���ж�ҳǩ��Ƭ���ı���ҳǩ����
   */
  private void addCardEditBodyMenu(String strTableCode) {
    BillCardPanel billCardPanel = m_CardCtrl.getCardPanel().getBillCardPanel();
    // �ڡ���Ƭ��塱�ı��嵯��ʽ�˵�����ӡ���Ƭ�༭����
    nc.ui.scm.pub.BillTools.addCardEditToBodyMenus(billCardPanel, strTableCode);
    // ����Ƭ�༭����ע�������
    UIMenuItem[] bodyMenuItems = billCardPanel.getBodyMenuItems(strTableCode);

    bodyMenuItems[bodyMenuItems.length - 1]
        .addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            m_btnEventHandler.onCardEdit();
          }
        });
  }

  /**
   * ��ָ���ı���ҳǩ�����ӡ������кš�����ʽ�˵���
   * 
   * @author songhy 2008-08-14
   * @param strTableCode
   *          ���ж�ҳǩ��Ƭ���ı���ҳǩ����
   */
  private void addNewRowNoBodyMenu(String strTableCode) {
    BillCardPanel billCardPanel = m_CardCtrl.getCardPanel().getBillCardPanel();
    // �ڡ���Ƭ��塱�ı��嵯��ʽ�˵�����ӡ������кš���
    TOBillTool.addNewRowNoToBodyMenus(billCardPanel, strTableCode);
    // �������кš���ע�������
    UIMenuItem[] bodyMenuItems = billCardPanel.getBodyMenuItems(strTableCode);
    bodyMenuItems[bodyMenuItems.length - 1]
        .addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            m_btnEventHandler.onNewRowNo();
          }
        });
  }
  
  /**
   * ��ָ���ı���ҳǩ�����ӡ�ѯ�ۡ�����ʽ�˵���
   * 
   * @author songhy 2008-08-14
   * @param strTableCode
   *          ���ж�ҳǩ��Ƭ���ı���ҳǩ����
   */
  private void addAskPriceBodyMenu(String strTableCode) {
    BillCardPanel billCardPanel = m_CardCtrl.getCardPanel().getBillCardPanel();
    // �ڡ���Ƭ��塱�ı��嵯��ʽ�˵�����ӡ������кš���
    TOBillTool.addAskPriceToBodyMenus(billCardPanel, strTableCode);
    // �������кš���ע�������
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
    /* ���ð�ť�� */
    ButtonObject boAst = new ButtonObject(ITOButtonConst.BTN_AUXILIARY/*
                                                                       * @res
                                                                       * "����"
                                                                       */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
        "UPPSCMCommon-000066")/*
                               * @res "���ݵĸ��ָ�������"
                               */, 2, "����"); /*-=notranslate=-*/
    boAst.addChildButton(m_ButtonCtrl.m_boAuditStatus);// ״̬��ѯ
    boAst.addChildButton(m_ButtonCtrl.m_boDocument);// �ĵ���ѯ
    boAst.addChildButton(m_ButtonCtrl.m_boJointCheck);// ����

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
    // ���ݵ�����˾��˾�����ڵ�ǰ��¼��˾,���ݵ�����˾�������ҹ�˾��ϢVO
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
    // ��������˾�����ص���ģ��
    initialize();
    // ��ʾ����
    setTitleText(m_CardCtrl.getTitle());
    // ����ǿ繫˾����,�򲻼��ذ�ť
    if (bAnotherCorp) {
      // �����ƹ�˾�Ĳ���Ȩ��
      // --��ͷ������˾
      UIRefPane ref = (UIRefPane) m_CardCtrl.getCardPanel().getBillCardPanel()
          .getHeadItem("coutcorpid").getComponent();
      ref.setRefNodeName("��˾Ŀ¼(����)");
      // --��ͷ������˾
      ref = (UIRefPane) m_CardCtrl.getCardPanel().getBillCardPanel()
          .getHeadItem("ctakeoutcorpid").getComponent();
      ref.setRefNodeName("��˾Ŀ¼(����)");
      m_CardCtrl.getCardPanel().getBillCardPanel().setEnabled(false);// ?
      setButtons(new ButtonObject[] {});
    }

    m_Model.setCurRow(0);
    m_Model.setData(0, vo);
    getBillCtrl().displayBill(0);
  }

  private void initCardCtrl() {
    // ��Ƭ����
    m_CardCtrl = new CardPanelCtrl(m_NodeInfo,this);
    m_CardCtrl.setModel(m_Model);
    m_CardCtrl.addPanelEventListener(this);
  }

  private void initListCtrl() {
    // �б����
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
   * �����ߣ����˾� ���ܣ��õ�������ʼ���ݣ����Ƶ��˵ȡ� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
          new String[] { ConstVO.m_sPk_Para[ConstVO.m_iPara_ZDSPSFXT], // �Ƿ������Ƶ���������Ϊͬһ��
          });
      m_NodeInfo.setM_paravalue(hReturnValue);
    }
    catch (Exception e) {
      Debug.error(e.getMessage(), e);
    }
  }

  /**
   * �����ߣ����˾� ���ܣ��õ�������ʼ���ݣ����Ƶ��˵ȡ� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
          new String[] { ConstVO.m_sPk_Para[ConstVO.m_iPara_ZDSPSFXT], // �Ƿ������Ƶ���������Ϊͬһ��
          });
      m_NodeInfo.setM_paravalue(hReturnValue);
    }
    catch (Exception e) {
      Debug.error(e.getMessage(), e);
    }
  }
  /**
   * ��ȡ��չ��ť����
   */
  public nc.ui.pub.ButtonObject[] getExtendBtns() {
    return null;
  }

  /**
   * ������չ��ť���¼�
   */
  public void onExtendBtnsClick(nc.ui.pub.ButtonObject bo) {

  }
/**
 * add:LIYU ���ӵ�������ˢ�²���
 */
  protected void refresh(){
	  if(querysql==null) return;
	   try {
	  ArrayList alListData = ClientBillHelper.queryBillsBy(
	          ConstVO201.saQueryHead, ConstVO201.saQueryBody, querysql,
	          "5X", this.m_NodeInfo.getUserID(), isWaitAudit); // 5X: ��������
	  if (alListData != null && alListData.size() > 0) {
	        // ��Ҫ�������еı�ͷ����
	        m_ListCtrl.setListHeadData(alListData);
	        // ��Ҫ������һ���ı�������
	        m_ListCtrl.setlistBodyData(1, alListData);
	      }

	      // ״̬����ʾ��ѯ�����������Ϣ
	      if (alListData.size() > 0) {
	        String[] value = new String[] {
	          String.valueOf(alListData.size())
	        };
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
	            "UPP40093010-000122", null, value));/* @res "���鵽{0}"�ŵ��ݣ�" */
	        m_Model.setCurRow(0);

	      }
	      else {
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
	            "UPP40093010-000071")/* @res "û�в鵽������Ҫ�ļ�¼��" */);
	        m_Model.setCurRow(-1);
	      }
	      m_Model.setData(alListData);
	      // ��ǰ�Ǳ���ʽʱ�������л����б���ʽ
	      if (getCurPanel() == GenEditConst.CARD)
	        switchPanel();
	      else {
	        // ��ʾ��ѯ���:ȱʡѡ�е�һ��
	        m_ListCtrl.displayData();
	        setButtonStatusBrowse(0);
	      }

	  
	   }
	    catch (Exception e) {
	      Debug.debug(e.getMessage(), e);
	      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
	          "UPP40093010-000072")/* @res "��ѯ����" */);
	      String[] value = new String[] {
	        e.getMessage()
	      };
	      showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	          "scmcommon", "UPPSCMCommon-000033", null, value));// "��ѯ����"
	      // +
	      // e.getMessage());
	    }
  }
  
  
  /**
   * �������ڣ�(2004-2-10 18:38:37) ���ߣ����˾� ������ ���أ� ˵������ѯ
   */
  protected void query() {
    try {
      getDefaultCalbodyID();

      getConditionDlg().showModal();

      if (getConditionDlg().getResult() != nc.ui.pub.beans.UIDialog.ID_OK)
        // ȡ������
        return;
      StringBuffer headWhere = new StringBuffer();
      processQuerySQL(headWhere);

      // added by songhy, 2008-08-07, start
      nc.vo.pub.query.ConditionVO[] voCond = m_QueryCondDlg.getConditionVO();
     
      // �����ѯ�����������˹�˾û��������֯�����ѯ�ù�˾��������Ȩ�޵���֯��
      // �����˾��֯��Ϊ�գ���Ϊ���й�˾��������Ȩ�޵���֯
      for (int i = 0; i < voCond.length; i++) {
        String sFieldCode = voCond[i].getFieldCode();
        // �õ����Ƿ���������Ĳ�ѯ�����Բ�ѯ������н�һ�����ˣ�
        // ��Ϊ���������⣬��Ҫ��Ϊ�����Ĳ���
        if (sFieldCode.equals("iswaitaudit")) {
          String iswaitauditStr = voCond[i].getValue(); // �����Ƿ���������ַ��� N Y
          if (iswaitauditStr.equals("Y")) {
            // �Ƿ�������û�ѡ�� ���ǡ�
            // ���в�ѯ����Ĺ��˴���
            isWaitAudit = true;
          }
          else{
            isWaitAudit = false;
          }
        }
      }
      // added by songhy, 2008-08-07, end

      showHintMessage(ConstHintMsg.S_ON_BILL_QUERY/* @res "���ڲ�ѯ" */);

      // modified by songhy, 2008-08-07, start
      // ���ͷ
      ArrayList alListData = ClientBillHelper.queryBillsBy(
          ConstVO201.saQueryHead, ConstVO201.saQueryBody, headWhere.toString(),
          "5X", this.m_NodeInfo.getUserID(), isWaitAudit); // 5X: ��������
      // modified by songhy, 2008-08-07, end
      querysql = headWhere.toString();
      iqueryStatus = true;
      if (alListData != null && alListData.size() > 0) {
        // ��Ҫ�������еı�ͷ����
        m_ListCtrl.setListHeadData(alListData);
        // ��Ҫ������һ���ı�������
        m_ListCtrl.setlistBodyData(1, alListData);
      }

      // ״̬����ʾ��ѯ�����������Ϣ
      if (alListData.size() > 0) {
        String[] value = new String[] {
          String.valueOf(alListData.size())
        };
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
            "UPP40093010-000122", null, value));/* @res "���鵽{0}"�ŵ��ݣ�" */
        m_Model.setCurRow(0);

      }
      else {
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
            "UPP40093010-000071")/* @res "û�в鵽������Ҫ�ļ�¼��" */);
        m_Model.setCurRow(-1);
      }
      m_Model.setData(alListData);
      // ��ǰ�Ǳ���ʽʱ�������л����б���ʽ
      if (getCurPanel() == GenEditConst.CARD)
        switchPanel();
      else {
        // ��ʾ��ѯ���:ȱʡѡ�е�һ��
        m_ListCtrl.displayData();
        setButtonStatusBrowse(0);
      }

    }
    catch (Exception e) {
      Debug.debug(e.getMessage(), e);
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
          "UPP40093010-000072")/* @res "��ѯ����" */);
      String[] value = new String[] {
        e.getMessage()
      };
      showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "scmcommon", "UPPSCMCommon-000033", null, value));// "��ѯ����"
      // +
      // e.getMessage());
    }
  }

  /*
   * �����ѯģ���ϵõ���where���
   */
  private void processQuerySQL(StringBuffer headWhere) throws Exception {
    // ��ͷwhere����
    headWhere.append(" blatest='Y' and ");

    // ��������Ϊ�̶���ѯ����
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
    // ����where����
    StringBuffer bodyWhere = new StringBuffer("");
    // ��ѯ����
    ConditionVO[] voaCond = getConditionDlg().getConditionVO();
    // �õ�ƴ�ɵ�sql
    TOBillTool.getQueryStr(NodeInfo.NAME_HEAD_TABLE, NodeInfo.NAME_BODY_TABLE,
        headWhere, bodyWhere, voaCond);

    // ��ѯ����
    if (!sbilltype.startsWith("5")) {
      sbilltype = ConstVO.m_sBillDBDD;
    }
    new TOQueryPrivilegeTool(sbilltype).getQueryStrForOrder40093009(bodyWhere,getNodeInfo().getUserID(),getConditionDlg().getConditionDatas());
    // ����״̬�Ĳ�ѯ����
    String sNormalSql = getQueryNormalPanel().getSQL();
    if (sNormalSql != null && sNormalSql.trim().length() > 0) {
      headWhere.append(" and ").append(sNormalSql);
    }
    // �б���where����ʱ
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
    // ��ʾ��Ƭ
    if (getCurPanel() == GenEditConst.LIST) {
      m_CardCtrl.showPanel();
      m_ListCtrl.hidePanel();
      m_iCurPanel = GenEditConst.CARD;

    }
    else { // ��ʾ�б�
      m_ListCtrl.showPanel();
      m_CardCtrl.hidePanel();
      m_iCurPanel = GenEditConst.LIST;
    }
    getBillCtrl().displayBill(iIndex);
    showBtnSwitch();
    setButtonStatusBrowse(iIndex);
  }

  /**
   * ****************************************** ���ܣ� <|> �������ڣ�(2004-3-10
   * 22:14:28)
   * 
   * @param��
   * @return�� ******************************************
   */
  public List<BillVO> approveBill(List<BillVO> lstAuditVOs)
  throws BusinessException {
    GenTimer timer = new GenTimer();

    if (lstAuditVOs == null || lstAuditVOs.size() <= 0) {
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPP40093010-000073")/* @res "����Ϊ�գ�" */);
    }
    List<BillVO> lstRet = null;
    // �����������ݵ�˳���Ա㷵��ʱ����˳����������
    List<String> lstID = new ArrayList<String>();
    for (int i = 0, len = lstAuditVOs.size(); i < len; i++) {
      lstID.add(lstAuditVOs.get(i).getHeaderVO().getCbillid());
    }
    // �ѵ���VO���յ������ͷֿ����Ա���ø��ԵĶ����ű�
    timer.start("@@������ʼ��");/*-=notranslate=-*/
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

      //���������ã����뱣֤���ݵ�VO�а�������VO�ĵ���ID��ts���ԣ��������Կɺ��ԣ���Ϊ�ں�̨�������²�ѯ����VO
      HashMap hmPfExParams = new HashMap();
      hmPfExParams.put(PfUtilBaseTools.PARAM_RELOAD_VO, PfUtilBaseTools.PARAM_RELOAD_VO);
      //actionName����ӵ�ǰ����Ա
      vosRet = (BillVO[]) nc.ui.pub.pf.PfUtilClient.runBatch(this,
          "APPROVE"+m_clientLink.getUser(),ConstVO.m_sBillDBDD,getNodeInfo().getLogDate(),voaAudit,userAry,null,hmPfExParams); 
      //ԭ�������ã�
      /*vosRet = (BillVO[]) nc.ui.pub.pf.PfUtilClient.processBatchFlow(this,
            "APPROVE", "5X", getNodeInfo().getLogDate(), voaAudit, userAry);*/
    }
    catch (Exception ex) {
      ExceptionUtils.marsh(ex);
    }
    // �����������صĽ��
    if (vosRet != null && vosRet.length > 0) {
      if (lstRet == null) {
        lstRet = new ArrayList<BillVO>(Arrays.asList(vosRet));
      }
      else {
        lstRet.addAll(new ArrayList<BillVO>(Arrays.asList(vosRet)));
      }
    }

    // ����������˳������������
    if (lstRet != null)
      resumeVOsAfterApprove(lstRet, lstID);
    timer.showExecTime("@@��ƽ̨��������");/*-=notranslate=-*/
    return lstRet;
  }

  protected List<BillVO> unApproveBill(List<BillVO> lstUnAuditVOs) throws BusinessException {
    List<BillVO> lstRet = null;
    // �����������ݵ�˳���Ա㷵��ʱ����˳����������
    List<String> lstID = new ArrayList<String>();
    for (int i = 0, len = lstUnAuditVOs.size(); i < len; i++) {
      lstID.add(lstUnAuditVOs.get(i).getHeaderVO().getCbillid());
    }
    GenTimer timer = new GenTimer();
    timer.start("@@����ʼ��");/*-=notranslate=-*/

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

      //���������ã����뱣֤���ݵ�VO�а�������VO�ĵ���ID��ts���ԣ��������Կɺ��ԣ���Ϊ�ں�̨�������²�ѯ����VO
      HashMap hmPfExParams = new HashMap();
      hmPfExParams.put(PfUtilBaseTools.PARAM_RELOAD_VO, PfUtilBaseTools.PARAM_RELOAD_VO);
      //actionName����ӵ�ǰ����Ա
      vosRet = (BillVO[]) nc.ui.pub.pf.PfUtilClient.runBatch(this,
          "UNAPPROVE"+m_clientLink.getUser(),ConstVO.m_sBillDBDD,getNodeInfo().getLogDate(),voaUnAudit,userAry,null,hmPfExParams);
      /*ԭ������÷���
        vosRet = (BillVO[]) nc.ui.pub.pf.PfUtilClient.processBatchFlow(this,
            "UNAPPROVE", billtype, getNodeInfo().getLogDate(), voaUnAudit, userAry);*/
    }
    catch (Exception ex) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
      "UPP40093010-000094")/*
       * @res "ȡ������ɹ�!"
       */);
      ExceptionUtils.marsh(ex);
    }
    if (vosRet != null && vosRet.length > 0) {
      // ��������״̬����
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
    // ����������˳������������
    if (lstRet != null) {
      resumeVOsAfterApprove(lstRet, lstID);
    }
    timer.showExecTime("@@��ƽ̨���󣺣�");/*-=notranslate=-*/
    return lstRet;
  }

  /*
   * �������������ֶε�˳������������VO�����˳��
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
   * �ѵ���VO��ϰ��յ������ͻ��ֿ��� return HashMap:key(billtypecode),value(�õ������͵�VO����)
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
   * �����ߣ����˾� ���ܣ� ���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public BillVO sendAudit(BillVO voBill) throws BusinessException {
    showHintMessage(ConstHintMsg.S_ON_BILL_SEND_AUDIT/* @res "��������" */);
    BillHeaderVO voHeader = voBill.getHeaderVO();

    try {
      // �������ĵ���
      BusinessCheck.checkForSendAudit(voBill);

      // �������Ա
      voBill.getHeaderVO().setCauditorid(voHeader.getCoperatorid());
      voBill.setOperator(getNodeInfo().getUserID());
      // ����ͻ���IP��ַ,дҵ����־��
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
                                                           * @res "��������Ѿ����û�ȡ����"
                                                           */);
      }
    }
    catch (Exception e) {
      // ��ԭΪ����״̬
      voHeader.setFstatusflag(new Integer(ConstVO.IBillStatus_FREE));
      ExceptionUtils.marsh(e);
    }
    return voBill;
  }

  /**
   * ������չ��ť״̬
   */
  public void setExtendBtnsStat(int iState) {

  }

  private void initbutton() {
    // ���ð�ť
    try {
      m_boBtnTree = new ButtonTree(m_NodeInfo.getNodeCode());
      
    }
    catch (BusinessException e) {
      Debug.error(e.getMessage(), e);
    }
    
    m_ButtonCtrl = new ButtonCtrl(m_boBtnTree, m_NodeInfo.getFuncExtend()); 
    //����Ǹ��Ƶ��������Ľڵ㣬���Ķ�����UPT40093009��ͷ�ģ����ǳ�ʼ��ButtonTree��ʱ��,��ť������ȱʡ�Ƕ����Ҫ�Ѷ��﷭�������
     //���Ƿ����ʱ���ǰ��յ�ǰ�ڵ�ţ�������Ӧ�Ķ�����Դ��Ȼ�������ڵ���û�ж�Ӧ�Ķ����ļ��ģ���˽�nodecodeת���ɵ��������Ľڵ��40093009
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
  

    // ����ҵ��������صİ�ť
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
   * ����ʵ�ָ÷�������Ӧ��ť�¼���
   * 
   * @version (00-6-1 10:32:59)
   * @param bo
   *          ButtonObject
   */
  public void onButtonClicked(nc.ui.pub.ButtonObject bo) {

    try {
      // ���ο�����չ
      getPluginProxy().beforeButtonClicked(bo);
      m_btnEventHandler.onBtnClick(bo);
      if (bo.getParent() == m_ButtonCtrl.m_boBusiType) {
        setButtons(m_boBtnTree.getButtonArray());
        bo.setSelected(true);
      }
      updateButtons();
      // ���ο�����չ
      getPluginProxy().afterButtonClicked(bo);
    }
    catch (Exception ex) {
      ExceptionUITools.showMessage(ex, this);
    }

  }

  public boolean onClosing() {
    if (m_CardCtrl.getEditMode() == GenEditConst.UPDATE
        || m_CardCtrl.getEditMode() == GenEditConst.NEW) {
      int iflag = showYesNoCancelMessage(ConstHintMsg.S_IS_SAVE_UPDATE);// �Ƿ񱣴����޸ĵ����ݣ�
      try {
        // �������,�����˳�
        if (iflag == MessageDialog.ID_YES) {
          m_btnEventHandler.onSave();
          return true;
          // �������˳�
        }
        else if (iflag == MessageDialog.ID_NO) {
          return true;
          // ���˳�
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
   * showBtnSwitch ���Ͻ���淶
   * 
   * @author leijun 2006-5-24
   */
  public void showBtnSwitch() {
    if (m_iCurPanel == GenEditConst.CARD)
      m_ButtonCtrl.m_boSwitch.setName(nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("common", "UCH022")/* @res "�б���ʾ" */);
    else
      m_ButtonCtrl.m_boSwitch.setName(nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("common", "UCH021")/* @res "��Ƭ��ʾ" */);
    updateButton(m_ButtonCtrl.m_boSwitch);

  }

  /**
   * �������ڣ�(2004-2-12 10:22:31) ���ߣ����˾� ������int iNum:ҵ����� ���أ� ˵�������ð�ť״̬
   */
  protected void setButtonStatusBrowse(int iNum) {
    // ��ǰ���б�ѡ�еĵ��ݡ�
    // ҵ��״̬��
    int iStatus = ConstVO.IBillStatus_FREE;
    // �ܷ�����˻�
    boolean bReject = false;
    // �ܷ�����������
    boolean bPlan3Z = false;
    // �ܷ�����������
    boolean bPlan4331 = false;
    // ȡ������·���Ƿ����
    boolean bDelSettlePath = false;
    // ������Id
    String auditorId = null;
    BillVO voThis = (BillVO) m_Model.getData(iNum);    

    if (voThis != null && voThis.getParentVO() != null) {
      //����ʱ�п��ܱ���Ϊ��
      if(voThis.getItemVOs()==null||voThis.getItemVOs().length==0){
        StringBuffer sql = new StringBuffer(" ");
        sql.append(" cbillid = '");
        sql.append(voThis.getHeaderVO().getPrimaryKey());
        sql.append("' and dr=0 ");
        try {
          BillItemVO[] itemVOs = nc.ui.to.pub.ClientBillHelper.queryBodyBySQL(null,sql.toString());

          //������������
          m_ListCtrl.parseBodyData(itemVOs);
          
          voThis.setChildrenVO(itemVOs);
        }
        catch (Exception e) {
          ExceptionUITools.showMessage(e, this);
        }
      }
      
      BillHeaderVO voTemp = (BillHeaderVO) voThis.getParentVO();
      iStatus = voTemp.getFstatusflag();
      
      // �����˻�״̬�ĵ���
      if (iStatus == ConstVO.IBillStatus_PASSCHECK
          || iStatus == ConstVO.IBillStatus_CLOSED) {
        // �Ƿ�����Դ���ݵı�־
        String sSourceType = ((BillVO) voThis).getItemVOs()[0]
            .getCsourcetypecode();
        UFBoolean bHasReturned = ((BillVO) voThis).getItemVOs()[0]
            .getBretractflag();
        // ����Ѿ��˻صĵ��ݣ������ٴ��˻�
        if ((sSourceType == null || !sSourceType.equals(voThis
            .getBillTypeCode()))
            && (bHasReturned != null && !bHasReturned.booleanValue()))
          bReject = true;
      }

      // ������˾���ڵ�ǰ��½��˾������ͨ��
      if (voTemp.getCtakeoutcorpid() != null
          && voTemp.getCtakeoutcorpid().equals(getCorpPrimaryKey())
          && iStatus == ConstVO.IBillStatus_PASSCHECK) {
        bPlan3Z = true;
      }
      
      // ������˾���ڵ�ǰ��½��˾������ͨ��
      if (voTemp.getCtakeoutcorpid() != null
          && voTemp.getCtakeoutcorpid().equals(getCorpPrimaryKey())
          && iStatus == ConstVO.IBillStatus_PASSCHECK) {
        bPlan4331 = true;
      }

      if (voTemp.getCsettlepathid() != null) {
        bDelSettlePath = true;
      }

      // added by songhy, 2008-08-07, start
      // V55SCM��������12 ��������ص���
      // 1�� ���������������̬��Ϊ������״̬��������״̬�ĵ��ݲ�����ɾ��
      // 2�� �������˲��Ҵ��ڡ������С�״̬�ĵ��ݿ����޸�
      // h.cauditorid
      auditorId = (String) voTemp.getAttributeValue("cauditorid");
      // added by songhy, 2008-08-07, end
    }
    m_ButtonCtrl.setButtonStatusOfBrowse(m_iCurPanel, m_Model.getCount(), iNum,
        iStatus, bReject, bPlan3Z,bPlan4331, bDelSettlePath, auditorId,iqueryStatus);
    if (voThis != null) {
      // �жϵ����Ƿ���Ҫ����(���Զ������)
      boolean bSendAudit = TOBillTool.isNeedSendAudit(
          iStatus, voThis.getBillTypeCode(), 
          voThis.getHeaderVO().getCoutcorpid(), 
          voThis.getHeaderVO().getCbiztypeid(),
          voThis.getHeaderVO().getCbillid(), 
          voThis.getHeaderVO().getCoperatorid());
      m_ButtonCtrl.m_boSendAudit.setEnabled(bSendAudit);
      
      if (iStatus == ConstVO.IBillStatus_FREE)
        m_ButtonCtrl.m_boAudit.setEnabled(true);
      // ��������Ϊֱ�ˣ����ܸ��ƣ������˻�
      Integer fallocflag = voThis.getHeaderVO().getFallocflag();
      if (fallocflag != null
          && ConstVO.ITransferType_DIRECT == fallocflag.intValue()) {
        m_ButtonCtrl.m_boCopy.setEnabled(false);
        m_ButtonCtrl.m_boReturn.setEnabled(false);
      }
    }

    // ʹ������Ч,���룡
    super.updateButtons();
    
    //���ο�����չ
    getPluginProxy().setButtonStatus();

  }

  public void getDefaultCalbodyID() {
    // ��ȡ��ǰ��¼�����֯
    if (m_NodeInfo.getCalbodyID() == null) {
      m_NodeInfo.setCalbodyID(getCalbodyPK(m_NodeInfo.getCorpID(), m_NodeInfo
          .getUserID()));
      // ֪ͨ������Ctrl
      getBillCtrl().setNodeInfo(m_NodeInfo);
    }
  }

  protected ClientLink m_clientLink;

  protected PrintEntry m_print;

  // ��������ĵ���id
  protected String m_sBillID;

  /**
   * �������ڣ�(2004-2-11 14:31:51) ���ߣ����˾� ������ ���أ� ˵����panel�¼���Ӧ������
   * 
   * @param pe
   *          nc.ui.scm.pub.ctrl.PanelEvent
   */
  public Object action(GenPanelEvent pe) {
    // ��Դ
    switch (pe.getSourceID()) {
      // �б�
      case GenPanelEvent.ID_FIRST_LIST:
        // ����
        switch (pe.getType()) {
          case GenPanelEvent.SWITCH: // �л�
            // �������ʽ�����������ʽ�༭״̬
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

          case GenPanelEvent.SEL_HEAD: // ��ͷ
            if (getIsComeFromOther()) {
              setButtonStatusOfRef();
            }
            else {
              setButtonStatusBrowse(m_Model.getCurRow());
              doFuncExtendBtn(nc.ui.scm.extend.IFuncExtend.LIST,
                  nc.ui.pub.bill.BillItem.HEAD);
            }
            break;
          case GenPanelEvent.SEL_BODY: // ����
            doFuncExtendBtn(nc.ui.scm.extend.IFuncExtend.LIST,
                nc.ui.pub.bill.BillItem.BODY);
            break;

          case GenPanelEvent.HINT: // ��ʾ��ʾ��Ϣ
            showHintMessage(pe.getValue().toString());
            break;

        }
        break;

      case GenPanelEvent.ID_FIRST_CARD:
        switch (pe.getType()) {
          case GenPanelEvent.HINT: // ��ʾ��ʾ��Ϣ
            if (pe.getValue() != null) {
              showHintMessage(pe.getValue().toString());
            }
            break;
          case GenPanelEvent.ERR:// ��ʾ��ʾ��Ϣ
            showErrorMessage(pe.getValue().toString());
            return null;
          case GenPanelEvent.ASK:
            if (pe.getValue() instanceof Integer) {
              if (((Integer) pe.getValue()).intValue() == Const.IASK_CARD_EDITABLE) {
                // ��Ƭ�Ƿ�ɱ༭
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

  // ֧�ֹ�����չ
  private void doFuncExtendBtn(int iCurPanel, int iPos) {
/*    if (m_NodeInfo.getFuncExtend() != null) {
      m_NodeInfo.getFuncExtend().rowchange(this,
          m_CardCtrl.getCardPanel().getBillCardPanel(),
          m_ListCtrl.getListPanel().getBillListPanel(), iCurPanel, iPos);
    }*/
  }

  /**
   * ����VO �������ڣ�(2001-12-18 16:59:11)
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
      // ���ñ�ͷ��һ�ŵ���ȥ������¼
      if (voaHeader != null && voaHeader.length > 0) {
        StringBuffer sBodyWhere = new StringBuffer();
        sBodyWhere.append(NodeInfo.NAME_BODY_TABLE + ".cbillid='"
            + voaHeader[0].getCbillid() + "' and dr=0 ");
        // �жϵ��������ĵ�����˾�Ƿ�͵�ǰ��¼��˾��ͬ,�����ͬ,�����Ȩ�޿��Ƶ����,���򲻿���Ȩ��
        if (m_NodeInfo.getCorpID().equals(voaHeader[0].getCoutcorpid())) {
          // ����Ȩ�޿��Ƶ����
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
          // ִ�м��ع�ʽ
          ArrayList alData = new ArrayList();
          alData.add(voBill);
          m_ListCtrl.setListHeadData(alData);
          m_ListCtrl.setlistBodyData(1, alData);
          // ����������
          TOBillTool.getFreeInfo(voBill.getItemVOs());
        }
      }
    }
    catch (java.lang.Exception e) {
      Log.error(e);
      throw e;
    }
    // û�з��������ĵ���
    if (voBill == null) {
      return null;
    }
    return voBill;
  }

  /**
   * ��������: ����: ����ֵ: �쳣:
   * 
   * @param newNodeInfo
   *          nc.ui.to.pubtransfer.NodeInfo
   */
  public void setNodeInfo(NodeInfo newNodeInfo) {
    m_NodeInfo = newNodeInfo;
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2005-11-22 10:29:00)
   */
  public void setButtonStatusEdit() {
    if (m_CardCtrl.getEditMode() == GenEditConst.UPDATE) {
      BillVO voThis = (BillVO) m_Model.getCurVO();
      if (voThis != null) {
        // �Ƿ���Դֱ�����۶���
        boolean bDrictFlag = ((BillVO) voThis).getHeaderVO().getBdrictflag()
            .booleanValue();

        String sbiztypeid = ((BillVO) voThis).getHeaderVO().getCbiztypeid();
        // �õ���ǰ��������ҵ�������Ƿ��������
        boolean bSelfMakeFlag = canSelfMake(sbiztypeid);
        // �����������ƽ��ֻ��һ������
        String csourceTypeCode = voThis.getItemVOs()[0].getCsourcetypecode();
        // �Ƿ������
        boolean bCanAddLine = true;
        // ��Դֱ�ˡ������������ƽ�����������������Ƶģ����ݲ�������
        if ("422Y".equals(csourceTypeCode) || bDrictFlag || !bSelfMakeFlag) {
          bCanAddLine = false;
        }
        // ���°�ť״̬
        m_ButtonCtrl.setEditStatus(m_CardCtrl.getEditMode(), bCanAddLine);
        // �����Ҽ��˵�
        m_CardCtrl.setBodyMenuEdit(bCanAddLine);
      }
    }
    else if (m_CardCtrl.getEditMode() == GenEditConst.NEW) {
      m_ButtonCtrl.setEditStatus(GenEditConst.NEW, true);
      
      m_CardCtrl.setBodyMenuEdit(true);
    }
    // ʹ������Ч,���룡
    super.updateButtons();
    //���ο�����չ
    getPluginProxy().setButtonStatus();
  }

  // �õ���ǰ��������ҵ�������Ƿ��������
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
    // TODO �Զ����ɷ������
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
    // TODO �Զ����ɷ������
    if (maintaindata == null)
      return;
    BillVO vo = qryBillForLinkOperate(maintaindata.getBillID());
    initForLinkQuery(vo);
    showBtnSwitch();
    setButtonStatusBrowse(0);
  }

  // ���ò��յ��ݵ��б�
  public void setReftoListPanel() {
    if (m_Model.getData().size() > 0) {
      m_Model.setCurRow(0);
    }
    setIsComeFromOther(true);
    // ��ǰ�Ǳ���ʽʱ�������л����б���ʽ
    if (getCurPanel() == GenEditConst.CARD) {
      m_ListCtrl.showPanel();
      m_CardCtrl.hidePanel();
      m_iCurPanel = GenEditConst.LIST;
    }
    if (m_Model.getData() != null && m_Model.getData().size() > 0) {
      // ��Ҫ�������еı�ͷ����
      m_ListCtrl.setListHeadData(m_Model.getData());
      // ��Ҫ������һ���ı�������
      BillVO billVo = null;
      BillItemVO[] itemVos = null;
      ArrayList<BillItemVO> alItemVos = new ArrayList<BillItemVO>();
      for (int i = 0; i < m_Model.getData().size(); i++) {
        billVo = (BillVO) m_Model.getData().get(i);
        itemVos = billVo.getItemVOs();
        // ����������
        TOBillTool.getFreeInfo(itemVos);
        alItemVos = new ArrayList<BillItemVO>(Arrays.asList(itemVos));
        // ��״̬��־
        TOBillTool.setNameValueByFlag(new String[] {
          "frowstatuflag"
        }, itemVos);

        if (alItemVos != null && alItemVos.size() > 0) {
          // ͨ�����ݹ�ʽ������ִ���йع�ʽ�����ķ���
          m_ListCtrl.getFormulaContainer().formulaBodys(new ArrayList(),
              alItemVos);
        }
      }
    }
    // ��ʾ��ѯ���:ȱʡѡ�е�һ��
    m_ListCtrl.displayData();

    // ���ð�ť״̬Ϊ���ձ���
    setButtonStatusOfRef();
  }

  // ���ȡ��ʱ����
  public void setReftoListPanelForCancel() {
    if (m_Model.getData().size() > 0) {
      m_Model.setCurRow(0);
    }
    setIsComeFromOther(true);
    // ��ǰ�Ǳ���ʽʱ�������л����б���ʽ
    if (getCurPanel() == GenEditConst.CARD) {
      m_ListCtrl.showPanel();
      m_CardCtrl.hidePanel();
      m_iCurPanel = GenEditConst.LIST;
    }
    // ��ʾ��ѯ���:ȱʡѡ�е�һ��
    m_ListCtrl.displayData();

    // ���ð�ť״̬Ϊ���ձ���
    setButtonStatusOfRef();
  }

  // ���ð�ť״̬Ϊ���ձ���
  public void setButtonStatusOfRef() {
    m_ButtonCtrl.setButtonStatusOfRef();
    // ʹ������Ч,���룡
    super.updateButtons();
    //���ο�����չ
    getPluginProxy().setButtonStatus();
  }

  // �Ƿ���������
  private boolean isComeFromOther = false;

  public void setIsComeFromOther(boolean b) {
    isComeFromOther = b;
  }

  public boolean getIsComeFromOther() {
    return isComeFromOther;
  }

  public void onSwitchForRef() {
    int iIndex = m_Model.getCurRow();
    // ��ʾ��Ƭ
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

  // ����ֱ�˰��Žڵ�ʹ��
  public void returnToMainUI() {
    removeAll();

    m_CardCtrl.addPanel(this);
    m_ListCtrl.addPanel(this);

    switchPanel();
    
    //���Ӳ���ֱ�˽��淵��ʱ����Ҫ�ظ�ԭ�е��������İ�ťȨ��
    setButtons(m_boBtnTree.getButtonArray(),"40093009");

    // ����ҵ��������صİ�ť
    initFlowBtn();

    updateUI();

  }

  /**
   * ���ز���ֱ�˽���
   * 
   * @param newVO
   *          ��Ҫ�ŵ���ͷ������ IUseSupplyTrans UI, String
   *          billType,AggregatedValueObject[] vos
   */
  public void addBHZYCardUI(BillVO newVO) throws Exception {
    this.m_BillToBillUI = (IBillToBillFor5X) Class.forName(
        "nc.ui.so.so003.BillToBillUI").getConstructor(new Class[] {
        IUseSupplyTrans.class, String.class, AggregatedValueObject[].class
    }).newInstance(this, "5X", new BillVO[] {
      newVO
    });

    // ���ز���ֱ�˰��Ž���
    removeAll();
    add((ToftPanel) this.m_BillToBillUI, "Center");
    // GenEditConst.SOURCE������ֱ�˽���
    m_iCurPanel = GenEditConst.SOURCE;
    this.m_BillToBillUI.setVisible(true);
    updateUI();

    //�򿪲���ֱ�˽��棬ֱ�Ӳ��ò���ֱ�˽���İ�ťȨ�����������������İ�ťȨ��
    ButtonObject[] btns = this.m_BillToBillUI.setButtenForUse("5X");
    super.setButtons(btns,"40060402");
    updateButtons();
  }

  /**
   * ����ֱ�˰�ť��Ӧ����
   */
  public void onBHZYBtnClick(ButtonObject bo) {
    ButtonObject[] btns = this.m_BillToBillUI.getButtons();
    for (int i = 0, loop = btns.length; i < loop; i++) {
      if (btns[i] == bo || btns[i] == bo.getParent()) {
        // ѭ���߲���ֱ�˽���İ�ť��Ӧ��
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
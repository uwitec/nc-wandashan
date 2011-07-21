package nc.ui.to.pubtransfer;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.swing.table.TableCellEditor;

import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.framework.common.NCLocator;
import nc.itf.scm.to.service.IOrderAskPrice;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.ui.bd.b21.CurrParamQuery;
import nc.ui.bd.b21.CurrtypeQuery;
import nc.ui.ic.ic001.BatchCodeDefSetTool;
import nc.ui.ic.pub.IOnHandEventListener;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.IBillItem;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.ic.measurerate.InvMeasRate;
import nc.ui.scm.pub.CacheTool;
import nc.ui.scm.pub.ctrl.GenEditConst;
import nc.ui.scm.pub.ctrl.GenPanelEvent;
import nc.ui.scm.pub.ctrl.GenPanelEventListener;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.ui.scm.pub.panel.SetColor;
import nc.ui.to.outer.ClientOuterHelper;
import nc.ui.to.pub.ClientCommonDataHelper;
import nc.ui.to.pub.InvAttrCellRenderer;
import nc.ui.to.pub.InvInfo;
import nc.ui.to.pub.InvoInfoBYFormula;
import nc.ui.to.pub.TOBillTool;
import nc.ui.to.pub.TOEnvironment;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.bd.b21.CurrinfoVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.ic.ic001.BatchcodeVO;
import nc.vo.ic.pub.lot.LotNumbRefVO;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.RelationsCalVO;
import nc.vo.scm.pub.OnHandResultVO;
import nc.vo.scm.pub.ctrl.GenMsgCtrl;
import nc.vo.scm.relacal.SCMRelationsCal;
import nc.vo.scm.transrela.ITransrelaConst;
import nc.vo.to.outer.QryTransrelaVO;
import nc.vo.to.pub.BillHeaderVO;
import nc.vo.to.pub.BillItemVO;
import nc.vo.to.pub.BillVO;
import nc.vo.to.pub.ConstVO;
import nc.vo.to.pub.Log;
import nc.vo.to.pub.tools.ExceptionUtils;
import nc.vo.to.to201.ConstVO201;
import nc.vo.to.to201.ParamVO;
import nc.vo.trade.checkrule.VOChecker;

public class EditCtrl implements nc.ui.pub.bill.BillEditListener,
    nc.ui.pub.bill.BillEditListener2, nc.ui.pub.bill.BillTotalListener,
    nc.ui.pub.bill.BillTabbedPaneTabChangeListener, BillCardBeforeEditListener,
    IOnHandEventListener {

  // ����card�����á� Ϊ�˼��ٲ����Ĵ��ݶ��ӡ�
  private BillCardPanel m_card;

  private InvMeasRate m_invMeas = new InvMeasRate();

  private InvMeasRate m_invQuoteMeas = new InvMeasRate();

  // panel�¼������ߡ�
  private GenPanelEventListener m_pelistener;

  private NodeInfo m_NodeInfo;
 
  private TransferClientUI ui;
  //
  private BillFillCtrl m_billFillCtrl;

  /**
   * EditCtrl ������ע�⡣
   */
  public EditCtrl(
      nc.ui.pub.bill.BillCardPanel card,TransferClientUI ui) {
    super();
    m_card = card;
    this.ui =ui;
    if (null != m_card) {
      m_billFillCtrl = new BillFillCtrl(this, m_card);
    }
  }

  public boolean beforeEdit(BillItemEvent e) {
    try {
      //����������˾�Ƿ���ͬ�����Ƶ�����˾�ı������޸ĳ�����˾
      setIsSameOutTakeOut();
      
      boolean bEdit = true;
      String sKey = e.getItem().getKey();
      // �༭��ͷ�Ŀ����֯����
      if (sKey.endsWith("cbid")) {
        BillItem btCurrent = CardPanelTool.getBillItemByPos(m_card,
            IBillItem.HEAD, sKey);
        String sCorpid = getHeadTailItemValue(getCorpRole(sKey) + "corpid");
        if (sCorpid == null || sCorpid.length() <= 0) {
          sCorpid = m_NodeInfo.getCorpID();
        }
        String sCondition = " and property <> "
            + String.valueOf(ConstVO.ICalbodyType_COST);
        // ִ�й���
        CardPanelTool.filterRef(btCurrent, sCondition, sCorpid);
      }
      else if (sKey.endsWith("whid")) {
        bEdit = beforeWarehouseEdit(IBillItem.HEAD, sKey);
      }
      else if (sKey.endsWith("foiwastpartflag")) {
        String typecode = "";
        try{
          typecode = m_NodeInfo.getOrderTypeCode(m_card);
        }
        catch(BusinessException ex){
          
        }
        if(typecode.equals("5E")||typecode.equals("5I"))
          bEdit=true;
        else
          bEdit=false;
      }
      else if (sKey.startsWith("vdef")) {
        if (m_card.getHeadItem(sKey).getDataType() == IBillItem.USERDEF) {
          String sPk = (String) m_card.getHeadItem(
              "pk_defdoc" + sKey.substring("vdef".length())).getValueObject();

          if (!isNull(sPk) && e.getItem().getValueObject() != null) {
            ((UIRefPane) e.getItem().getComponent()).setPK(sPk);
          }
          else {
            ((UIRefPane) e.getItem().getComponent()).setPK(null);
          }
        }
      }
      if (!bEdit) {
        e.getItem().setEnabled(false);
        ((UIRefPane) e.getItem().getComponent()).setPK(null);
      }
      //���ο�����չ
      if(ui!=null)
      { if(!ui.getPluginProxy().beforeEdit(e)){
        bEdit = false;
      }
      }
      return bEdit;
    }
    catch (Exception ex) {
      if (ex instanceof BusinessException) {
        sendHintMsg(ex.getMessage());
      }
      else {
        Debug.error(ex.getMessage(), ex);
      }
      return false;
    }
  }

  //����������˾�Ƿ���ͬ�����Ƶ�����˾�ı������޸ĳ�����˾
  private boolean isSameOutTakeOut = false;
  
  /**
   * ������������������isSameOutTakeOut
   * //����������˾�Ƿ���ͬ�����Ƶ�����˾�ı������޸ĳ�����˾
   * <b>����˵��</b>
   * @author sunwei
   * @time 2009-1-12 ����10:21:48
   */
  private void setIsSameOutTakeOut() {
    isSameOutTakeOut = false;
    String sTakeOutCorpid = getHeadTailItemValue("ctakeoutcorpid");
    String sOutCorpid = getHeadTailItemValue("coutcorpid");
    if(!isNull(sTakeOutCorpid)&&!isNull(sOutCorpid)&&sOutCorpid.equals(sTakeOutCorpid)){
      isSameOutTakeOut = true;
    }
  }

  /**
   * �༭���¼��� �������ڣ�(2001-3-23 2:02:27)
   * 
   * @param e
   *          ufbill.BillEditEvent
   */
  public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
    Object editable = firePanelEvent(GenPanelEvent.ASK, new Integer(
        Const.IASK_CARD_EDITABLE));
    // ��ǰ״̬�������ӻ����޸ģ�Ӧ�ò����Ա༭
    if (!((Boolean) editable).booleanValue())
      return;

    sendHintMsg("");
    String sKey = e.getKey();
    int rowNumber = e.getRow();

    try {
      if (e.getKey().equals("crowno")) {
        // �к�
        nc.ui.scm.pub.report.BillRowNo.afterEditWhenRowNo(m_card, e, "crowno");
      }
      else if ("ntaxrate".equals(sKey)) {
        afterNTaxRateEdit(e);
        // afterTaxRateEdit(e);

        // ���ñ�������ɫ
        setModifiedRowColor(rowNumber, Color.YELLOW);
      }
      else if (sKey.indexOf("num") != -1 || sKey.indexOf("rate") != -1
          || sKey.indexOf("astnum") != -1 || sKey.indexOf("quoteunitnum") != -1) {
        chkNegativeNum(e);
      }
      if ("cinvcode".equals(sKey)) {
        afterInvEdit(e);
      }
      // �༭��˾������
      else if (sKey.endsWith("corpid")) {
        afterCorpUnitEdit(e);
      }
      // �༭����/����/���������֯
      else if (sKey.endsWith("cbid")) {
        afterCalbodyEidt(e.getKey());
      }
      // �༭�ֿ�
      else if (sKey.endsWith("whname") || sKey.endsWith("whid")) {
        afterWhEdit(e);
      }
      // �༭ҵ��Ա
      else if (sKey.endsWith("psnname")) {
        afterPsnEdit(e);
      }
      else if (sKey.equals("nexchangeotobrate")) {
        afterRateEdit(e); // �༭����
      }
      else if ("nnum".equals(sKey)) {// �༭������
        afterNumEdit(e);
      }
      // �༭������
      else if ("nassistnum".equals(sKey) || "nquoteunitnum".equals(sKey)) {
        afterAstNumEdit(e);
      }
      // �༭����λ
      else if ("castunitname".equals(sKey)) {
        afterAstunitEdit(e);
      }
      // �༭����λ
      else if ("cquoteunitname".equals(sKey)) {
        afterQuoteUnitEdit(e);
      }
      // �༭������
      else if ("nchangerate".equals(sKey)) {
        afterChangeRateEdit(e);
      }
      // �༭������
      else if ("vfree0".equals(sKey)) {
        afterFreeItemEdit(e);
      }
      // �༭ʧЧ����
      else if ("dvalidate".equals(sKey)) {
        afterValidateEdit(e);
      }
      // �༭��������
      else if ("dproducedate".equals(sKey)) {
        afterProducedateEdit(e);
      }
      else if ("creceievename".equals(sKey)) {
        afterCustEdit(e);
      }
      else if ("nprice".equals(sKey)) {
        afterPriceTaxEdit(e);
        // afterPriceWTaxEdit(e);

        // ���ñ�������ɫ
        setModifiedRowColor(rowNumber, Color.YELLOW);
      }
      else if ("nnotaxprice".equals(sKey)) {
        afterPriceNoTaxEdit(e);
        // afterPriceWOTaxEdit(e);

        // ���ñ�������ɫ
        setModifiedRowColor(rowNumber, Color.YELLOW);
      }
      else if ("nnotaxmny".equals(sKey)) {
        afterMnyNoTaxEdit(e);

        // ���ñ�������ɫ
        setModifiedRowColor(rowNumber, Color.YELLOW);
      }
      else if ("nmny".equals(sKey)) {
        afterMnyTaxEdit(e);

        // ���ñ�������ɫ
        setModifiedRowColor(rowNumber, Color.YELLOW);
      }
      else if ("norgqtnetprc".equals(sKey)) {
        afterQuotePriceWOTaxEdit(e);
      }
      else if ("norgqttaxnetprc".equals(sKey)) {
        afterQuotePriceWTaxEdit(e);
      }
      else if ("coutcurrtype".equals(sKey)) {
        afterCurrTypeEdit(e);
      }
      else if ("cprojectname".equals(sKey)) // ��Ŀ��Ϣ�༭�Ժ󴥷�
      {
        afterProjectEdit(e);
      }
      else if ("cprojectphasename".equals(sKey)) // ��Ŀ�׶���Ϣ�༭�Ժ󴥷�
      {
        afterProjectphaseEdit(e);
      }
      else if ("vbatch".equals(sKey)) {
        afterBatchEdit(e.getRow());
      }
      else if ("flargess".equals(sKey)) {
        afterFlargessEdit(e);
      }
      else if (sKey.startsWith("vdef"))// ��ͷ�Զ�����༭�Ժ󴥷�
      {
        DefSetTool.afterEditHead(m_card.getBillData(), sKey, "pk_defdoc"
            + sKey.substring(sKey.indexOf("vdef") + "vdef".length(), sKey
                .length()));
      }
      else if (sKey.startsWith("vbdef"))// �����Զ�����༭�Ժ󴥷�
      {
        DefSetTool.batchDefEdit(m_card,e,"pk_defdoc","vbdef");
        
        DefSetTool.afterEditBody(m_card.getBillModel(), e.getRow(), sKey,
            "pk_defdoc"
                + sKey.substring(sKey.indexOf("vbdef") + "vbdef".length(), sKey
                    .length()));
      }
      // ���༭���Ƿ���Ȼ����༭ǰ�ļ������! �������ʱ�����˱༭ǰ���!
      m_billFillCtrl.checkAfterBodyFill(e);
      //���ο�����չ
      if(ui!=null)
      {
        ui.getPluginProxy().afterEdit(e);
      }
    }
    catch (BusinessException be) {
      Debug.error(be.getMessage(), be);
      sendErrMsg(be.getMessage());
    }
    catch (Exception ex) {
      Debug.error(ex.getMessage(), ex);
      sendErrMsg(ex.getMessage());
    }
  }

  private void setModifiedRowColor(int rowNumber, Color color) {
    UFDouble nprice = (UFDouble) m_card.getBillModel().getValueAt(rowNumber,
        "nprice");
    UFDouble naskprice = (UFDouble) m_card.getBillModel().getValueAt(rowNumber,
        "naskprice");
    // ��ʾ��ɫΪ��ɫ����������֣�
    // 1, δѯ���۸����û��޸��˼۸�
    // 2, �Ѿ�ѯ���۸����û��޸���ѯ����Ĭ�ϼ۸�
    if ((nprice != null) && (!nprice.equals(naskprice))) {
      ArrayList<Integer> selectedRow = new ArrayList<Integer>();
      selectedRow.add(rowNumber);
      SetColor.setRowColor(m_card.getBodyPanel(), selectedRow, Color.YELLOW);
    }
  }

  /**
   * �۱����ʱ༭�¼�
   * 
   * @param e
   * @throws BusinessException
   */
  private void afterRateEdit(BillEditEvent e) throws BusinessException {
    // nexchangeotobrate
    String sOutCorpID = getHeadTailItemValue("coutcorpid");
    String sCurrType = getHeadTailItemValue("coutcurrtype");
    if (sCurrType.equals(CurrParamQuery.getInstance()
        .getLocalCurrPK(sOutCorpID))) {
      m_card.setHeadItem("nexchangeotobrate", UFDouble.ONE_DBL);
      return;
    }
    // �����Ƿ������޸Ļ��ʣ����������Ҫ���¼��������ȣ���ѯ�ۻ�ȡĬ�ϻ��ʲ�ת��Ϊ��Ӧ��Ҽ۸񡢽��ȣ��Ƿ������⣿
  }

  /**
   * �����ߣ� ���ܣ������ı��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void afterNumEdit(nc.ui.pub.bill.BillEditEvent e) throws Exception {
    int iRow = e.getRow();
    String sMainNum = null;
    Double dMainNum = null;
    String sInvBasID = getCellValue(iRow, "cinvbasid");
    String sOutInvID = getCellValue(iRow, "coutinvid");
    if (isNull(sInvBasID) || isNull(sOutInvID)) {
      return;
    }

    // ������
    sMainNum = getCellValue(iRow, "nnum");
    if (isNull(sMainNum) || Double.valueOf(sMainNum).doubleValue() == 0d) {
      TOBillTool.clearRowValues(m_card, iRow, new String[] {
          "nnum", "nassistnum", "nmny", "nnotaxmny"
      });
      return;
    }
    dMainNum = Double.valueOf(sMainNum);

    // �������������Ϊ�п��ܲ�ѯ�ۣ�����Ҫ����������
    calculateNumPriceMny(e);
    
    // ���ü۸�
    	setPrice(new int[] {iRow});
    
    setBretractFlag(dMainNum, iRow);
  }

  /**
   * �������������㷨
   */
  private void calculateNumPriceMny(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {
    int iRow = e.getRow();

    // �����Ƿ񱨼ۼ�����λ�̶������ʣ��������۽�������ʹ��
    setBquotefixrate(iRow);

    ClientCommonDataHelper cbo = new ClientCommonDataHelper();
    // ��˰���ȣ��Ǻ�˰����"TO0017";
    String sTaxFirst = cbo.getParaValue(getHeadTailItemValue("coutcorpid"),
        ConstVO.m_iPara_SFHSYX);

    int iTaxFirstFlag = RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;
    if (sTaxFirst.equals("Y")) {
      iTaxFirstFlag = RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;
    }
    else {
      iTaxFirstFlag = RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE;
    }
    int[] iaPrior = {
      iTaxFirstFlag
    };
    String flag = CheckChangeRate(e);
    if (flag.equalsIgnoreCase("N")) {
      // ���ü�˰���
      String[] strKeys = getnoKeys();
      try {
        strKeys[0] = getTaxtypeflag(iRow);
      }
      catch (Exception ex) {
        // ȡ������˰���ȡĬ��
        Log.error(ex);
      }
      RelationsCal.calculate(iRow, m_card, iaPrior, e.getKey(), nDescriptions,
          strKeys, BillItemVO.class.getName(), BillHeaderVO.class.getName());
    }
    else {
      String[] strKeys = getKeys();
      try {
        strKeys[0] = getTaxtypeflag(iRow);
      }
      catch (Exception ex) {
        // ȡ������˰���ȡĬ��
        Log.error(ex);
      }
      RelationsCal.calculate(iRow, m_card, iaPrior, e.getKey(), nDescriptions,
          strKeys, BillItemVO.class.getName(), BillHeaderVO.class.getName());
    }

  }

  // �����Ƿ񱨼ۼ�����λ�̶������ʣ��������۽�������ʹ��
  private void setBquotefixrate(int row) throws BusinessException {
    String sInvBasID = getCellValue(row, "cinvbasid");  
    String sQuoteMeasID = getCellValue(row, "cquoteunitid");//���ۼ�����λ
    String sFixedFlag = InvInfo.getInvConvert(sInvBasID, sQuoteMeasID)[1];
    // �������Ϸ���
    m_card.getBillModel().setValueAt(sFixedFlag, row, "bquotefixrate");
  }

  private void setBretractFlag(Double dMainNum, int iRow) {
    // �����Ƿ��˻ر��
    UFBoolean bRetractflag = UFBoolean.FALSE;
    if (dMainNum != null && dMainNum.doubleValue() < 0d) {
      bRetractflag = UFBoolean.TRUE;
    }
    m_card.getBillModel().setValueAt(bRetractflag, iRow, "bretractflag");
    if (bRetractflag.booleanValue()) {
      TOBillTool.clearAllRowValues(m_card, new String[] {
          "sendtypename", "pk_sendtype"
      });
    }
  }

  /**
   * �༭ǰ���� �������ڣ�(2001-3-23 2:02:27) ��ͷ�ı༭ǰ�¼����ᴥ���˷���
   * 
   * @param e
   *          ufbill.BillEditEvent
   */
  public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
    try {
      // ֪ͨ������
      Object editable = firePanelEvent(GenPanelEvent.ASK, new Integer(
          Const.IASK_CARD_EDITABLE));
      // ��ǰ״̬�������ӻ����޸ģ�Ӧ�ò����Ա༭
      if (!((Boolean) editable).booleanValue())
        return false;
      m_card.stopEditing();
      String sKey = e.getKey();

      if (isRetBill(e.getRow())) {
        // �˻�ʱֻ�������ɱ༭
        if (!sKey.equals("nassistnum") && !sKey.equals("nnum")) {
          return false;
        }
      }
      else if ("ntaxmny".equals(sKey)) {
        return false;
      }
      // �༭������
      else if ("vfree0".equals(e.getKey())) {
        return beforeVfree0Edit(e);
      }
      /*
       * else if ("castunitname".equals(e.getKey())) { return
       * beforeAstUnitEdit(e); }
       */
      // ������Ŀ�׶�
      else if ("cprojectphasename".equals(sKey)) {
        return beforeProjectPhaseEdit(e);
      }
      // ������Ŀ
      else if ("cprojectname".equals(sKey)) {
        return beforeProjectEdit(e);
      }
      else if (sKey.endsWith("whname")) {
        return beforeWarehouseEdit(IBillItem.BODY, sKey);
      }
      else if (sKey.endsWith("deptname")) {
        return beforeDeptEdit(e);
      }
      else if (sKey.endsWith("spacename")) {
        return beforeSpacenameEdit(e);
      }
      else if (sKey.endsWith("psnname")) {
        return beforePsnnameEdit(e);
      }
      // �༭���
      else if ("cinvcode".equals(sKey)) {
        return beforeInvEdit(e);
      }
      // �༭����λ
      else if ("castunitname".equals(sKey) || "cquoteunitname".equals(sKey)) {
        return beforeAstUnitEdit(e);
      }
      // �༭������
      else if ("nassistnum".equals(sKey) || "nquoteunitnum".equals(sKey)) {
        return beforeAstNumEdit(e);
      }
      // �༭������
      else if ("nchangerate".equals(sKey)) {
        return beforeChangeRateEdit(e);
      }
      // �༭����
      else if ("nnum".equals(sKey)) {
        return beforeNumEdit(e);
      }
      // �༭���κ�
      else if ("vbatch".equals(sKey)) {
        return beforeBatchEdit(e);
      }
      else if ("dvalidate".equals(sKey)) {
        return beforeValidateEdit(e);
      }
      else if ("dproducedate".equals(sKey)) {
        return beforeProduceDateEdit(e);
      }
      // �༭���˷�ʽ
      else if ("sendtypename".equals(sKey)) {
        return beforeSendTypeEdit(e);
      }
      // �༭�ջ��ͻ�
      else if ("creceievename".equals(sKey)) {
        return beforeCustEdit(e);
      }
      else if ("arriveareaname".equals(sKey)) {
        return beforeCustEdit(e);
      }
      else if ("vreceiveaddress".equals(sKey)) {
        return beforeCustEdit(e);
      }
      else if ("areaclname".equals(sKey)) {
        return beforeCustEdit(e);
      }
      else if ("cvendorname".equals(sKey)) {
        return beforeVendorEdit(e);
      }
      // �༭˰��
      else if ("ntaxrate".equals(sKey)) {
        return beforeTaxRateEdit(e);
      }
      // �Ӽ���ֻ���ڵ�����ϵ���޸�
      else if ("naddpricerate".equals(sKey)) {
        return false;
      }
      else if (sKey.indexOf("price") >= 0
          && !(sKey.equals("naskprice") || sKey.equals("nasknotaxprice"))) {
        return beforePriceEdit(e);
      }
      else if (sKey.equals("naskprice") || sKey.equals("nasknotaxprice")) {
        return false;// ѯ�ۺ�˰����������ѯ�ۼ�¼�����ɱ༭
      }
      else if (sKey.equals("nmny") || sKey.equals("nnotaxmny")) {
        return beforePriceEdit(e);
      }
      else if (sKey.startsWith("vbdef"))// �����Զ�����༭��ǰ����
      {
        if (getBodyItem(sKey).getDataType() == IBillItem.USERDEF) {
          UIRefPane ref = (UIRefPane) getBodyItem(sKey).getComponent();

          if (ref == null) {
            return true;
          }
          // ��ֹͳ�����͵��Զ�����ղ��ܱ��༭��ǿ�������ý���
          ref.getUITextField().setEditable(true);

          String sPk = getCellValue(e.getRow(), "pk_defdoc"
              + sKey.substring(sKey.indexOf("vbdef") + "vbdef".length(), sKey
                  .length()));

          if (!isNull(sPk)
              && m_card.getBillModel().getValueAt(e.getRow(), sKey) != null) {
            ((UIRefPane) getBodyItem(sKey).getComponent()).setPK(sPk);
          }
          else {
            ((UIRefPane) getBodyItem(sKey).getComponent()).setPK(null);
          }
        }
      }
      // ��֯�䡢��֯�ڣ������޸�
      else if (sKey.indexOf("norgqttaxnetprc") >= 0
          || sKey.indexOf("norgqtnetprc") >= 0) {
        return !isInSameCorp();
      }
      //���ο�����չ
      if(ui!=null)
      {
        if(!ui.getPluginProxy().beforeEdit(e)){
          return false;
        }
      }
    }
    catch (Exception ex) {
      if (ex instanceof BusinessException) {
        sendHintMsg(ex.getMessage());
      }
      else {
        GenMsgCtrl.handleException(ex);
      }
      return false;
    }
    return true;
  }

  /**
   * �иı��¼��� �������ڣ�(2001-3-23 2:02:27)
   * 
   * @param e
   *          ufbill.BillEditEvent
   */
  public void bodyRowChange(nc.ui.pub.bill.BillEditEvent e) {
    int iRow = e.getRow();
    showInvAmount(iRow);
    firePanelEvent(GenPanelEvent.ASK,
        new Integer(Const.IASK_CARD_BODYROWCHANGE));
    //���ο�����չ
    ui.getPluginProxy().bodyRowChange(e);
  }

  /**
   * ����ϼơ� �������ڣ�(2001-10-24 16:33:58)
   * 
   * @return nc.vo.pub.lang.UFDouble
   * @param key
   *          java.lang.String
   */
  public nc.vo.pub.lang.UFDouble calcurateTotal(java.lang.String key) {
    UFDouble dTotal = new UFDouble(0.0);
    for (int i = 0; i < m_card.getRowCount(); i++) {
      String sValue = getCellValue(i, key);
      if (isNull(sValue))
        sValue = "0";
      dTotal = dTotal.add(new UFDouble(sValue));
    }

    return dTotal;
  }

  /**
   * �������ڣ�(2004-2-12 16:10:58) ���ߣ����˾� ������ ���أ� ˵�������ýڵ���Ϣ
   * 
   * @param newNodeInfo
   *          nc.ui.scm.pub.templet.NodeInfo
   */
  public void setNodeInfo(NodeInfo newNodeInfo) {
    m_NodeInfo = newNodeInfo;
  }

  private nc.ui.ic.pub.lot.LotNumbRefPane m_LotNumbRefPane = null; // ���β���

  private nc.ui.scm.ic.freeitem.FreeItemRefPane m_refFreeItem = null; // �Զ��������

  /**
   * �����ߣ� ���ܣ��������ı��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void afterAstunitEdit(nc.ui.pub.bill.BillEditEvent e)
      throws BusinessException {

    int iRow = e.getRow();
    Double dMainNum = null;
    String sAstNum = null;
    Double dAstNum = null;
    Double dQuoteNum = null;
    UFDouble dRate = null;
    // ���ø�������ʶ
    Object oTemp1 = e.getValue();
    // ȡ����λ
    BillItem bt = getBodyItem("castunitname");
    UIRefPane refAstUnit = (UIRefPane) bt.getComponent();
    Object oMeaPK = refAstUnit.getRefPK();
    if (isNull(oTemp1) || isNull(oMeaPK)) {
      // ���������������
      TOBillTool.clearRowValues(m_card, iRow, new String[] {
          "castunitid", "nchangerate", "cquoteunitid", "cquoteunitname",
          "nquoteunitnum", "nquoteunitrate", "nnum", "nmny", "nnotaxmny"
      });
      return;
    }

    nc.vo.bd.b15.MeasureRateVO voMeas = null;
    voMeas = m_invMeas.getMeasureRate(getCellValue(iRow, "coutinvid"), oMeaPK
        .toString());
    if (voMeas != null) {
      dRate = voMeas.getMainmeasrate();
    }
    m_card.getBillModel().setValueAt(oMeaPK, iRow, "castunitid");
    m_card.getBillModel().setValueAt(dRate, iRow, "nchangerate");
    if (isNull(dRate)) {
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPP40093010-000022")/*
                                             * @res
                                             * "�뵽���������ȷ��:���д��������������λ�������Ƿ�Ϊ��"
                                             */);

    }
    sAstNum = getCellValue(iRow, "nassistnum");
    String sMainNum = getCellValue(iRow, "nnum");
    if (!isNull(sAstNum)) {
      /*
       * dAstNum = Double.valueOf(sAstNum); // ������=����������x������ dMainNum = new
       * Double(dAstNum.doubleValue() * dRate.doubleValue());
       */
      dMainNum = Double.valueOf(sMainNum);
      dAstNum = new Double(dMainNum.doubleValue() / dRate.doubleValue());
      m_card.getBillModel().setValueAt(dAstNum, iRow, "nassistnum");
      // �������仯�����۵�λ����ҲҪ�仯(���۵�λ����=������/���ۼ�����λ������)
      String sQuoteRate = getCellValue(iRow, "nquoteunitrate");// ���ۼ�����λ������
      if (sQuoteRate != null && sQuoteRate.length() > 0) {
        dQuoteNum = new Double(dMainNum.doubleValue()
            / Double.parseDouble(sQuoteRate));
      }
      // �����ܽ��
      HashMap hmValue = new HashMap(4);
      hmValue.put("nnum", dMainNum.toString());
      hmValue.put("nquoteunitnum", dQuoteNum == null ? "" : dQuoteNum
          .toString());
/*      hmValue.put("nmny", getCellValue(iRow, "nprice") == null ? "" : String
          .valueOf(Double.parseDouble(getCellValue(iRow, "nprice"))
              * dMainNum.doubleValue()));
      hmValue.put("nnotaxmny", getCellValue(iRow, "nnotaxprice") == null ? ""
          : String.valueOf(Double
              .parseDouble(getCellValue(iRow, "nnotaxprice"))
              * dMainNum.doubleValue()));*/
      TOBillTool.setRowValue(m_card, hmValue, iRow);
    }
    String MainNum = getCellValue(iRow, "nnum");
    if (!isNull(MainNum) && isNull(sAstNum)) {
      dMainNum = Double.valueOf(MainNum);
      dAstNum = new Double(dMainNum.doubleValue() / dRate.doubleValue());
      m_card.getBillModel().setValueAt(dAstNum, iRow, "nassistnum");
    }
  }

  /*
   * ���۵�λ�仯���������仯���Ӷ����������仯
   */
  private void afterQuoteUnitEdit(nc.ui.pub.bill.BillEditEvent e)
      throws BusinessException {
    //
    int iRow = e.getRow();
    BillItem bt = getBodyItem("cquoteunitname");
    UIRefPane ref = (UIRefPane) bt.getComponent();
    String sQuoteUnitID = ref.getRefPK();
    m_card.getBillModel().setValueAt(sQuoteUnitID, iRow, "cquoteunitid");
    if (isNull(sQuoteUnitID)) {
      TOBillTool.clearRowValues(m_card, iRow, new String[] {
          "nquoteunitrate", "nnum", "nmny", "nnotaxmny"
      });
      return;
    }
    nc.vo.bd.b15.MeasureRateVO voMeas = null;
    UFDouble dRate = null;
    voMeas = m_invQuoteMeas.getMeasureRate(getCellValue(iRow, "coutinvid"),
        sQuoteUnitID);
    if (voMeas != null) {
      dRate = voMeas.getMainmeasrate();
    }
    if (isNull(dRate)) {
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPP40093010-000022")/*
                                             * @res
                                             * "�뵽���������ȷ��:���д��������������λ�������Ƿ�Ϊ��"
                                             */);

    }
    m_card.getBillModel().setValueAt(dRate, iRow, "nquoteunitrate");
    String sNum = getCellValue(iRow, "nnum");
    if(!isNull(sNum)){
      Double dQuoteunitNum = new Double(Double.parseDouble(sNum)
                / dRate.doubleValue());
       m_card.getBillModel().setValueAt(dQuoteunitNum.toString(), iRow, "nquoteunitnum");
    }
    
//    // �����ʱ��ˣ����¼���������
//    String sQuoteunitNum = getCellValue(iRow, "nquoteunitnum");
//    if (!isNull(sQuoteunitNum)) {
//      Double dMainNum = new Double(Double.parseDouble(sQuoteunitNum)
//          * dRate.doubleValue());
//      m_card.getBillModel().setValueAt(dMainNum.toString(), iRow, "nnum");
//
//      // ����������,���¼��㸨����
//      String sPK_AstUnit = getCellValue(iRow, "castunitid");
//      if (!isNull(sPK_AstUnit)) {
//        String sChangeRate = getCellValue(iRow, "nchangerate");
//        String sAstNum = getCellValue(iRow, "nassistnum");
//        String sInvBasID = getCellValue(iRow, "cinvbasid");
//        String sMeasID = InvInfo.getPk_measdoc(sInvBasID);
//        String sFixedFlag = sMeasID.equals(sPK_AstUnit) ? "Y" : InvInfo
//            .getInvConvert(sInvBasID, sPK_AstUnit)[1];
//        // ��������Ϊ�̶�ʱ
//        if (sFixedFlag.equals("Y")) {
//          Double dAstNum = new Double(dMainNum.doubleValue()
//              / Double.parseDouble(sChangeRate));
//          m_card.getBillModel().setValueAt(dAstNum.toString(), iRow,
//              "nassistnum");
//        }
//        // �ǹ̶������ʣ��䶯�����ʣ�����������
//        else {
//          if (!isNull(sAstNum)) {
//            Double dChangeRate = new Double(dMainNum.doubleValue()
//                / Double.parseDouble(sAstNum));
//            m_card.getBillModel().setValueAt(dChangeRate.toString(), iRow,
//                "nchangerate");
//          }
//        }
//      }
//
//    }
    
    // �޸ı��ۼ�����λ����ѯ��
    setPrice(new int[] {
      iRow
    });
  }

  /**
   * �����ߣ����˾� ���ܣ�������ı��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void afterFreeItemEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {
    int iRow = e.getRow();

    FreeVO voFree = getRefFreeItem().getFreeVO();
    if (isNull(voFree))
      return;

    HashMap hmValue = new HashMap(5);
    for (int i = 0; i <= 5; i++) {
      hmValue.put("vfree" + i, voFree.getAttributeValue("vfree" + i));
    }
    TOBillTool.setRowValue(m_card, hmValue, iRow);

    showInvAmount(iRow);
  }

  private void afterFlargessEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {
    int iRow = e.getRow();
    String sOutInvid = getCellValue(iRow, "coutinvid");
    if (isNull(sOutInvid)) {
      return;
    }
    String sFlargess = getCellValue(iRow, "flargess");

    if (Boolean.valueOf(sFlargess).booleanValue()) {
      // ��ѡ����Ʒ���󣬴���ļ۸���Ϣ���Ӽ�����գ��Ҳ�����༭�������ֻ��Ϊ0
      clearPrice(new int[] {
        iRow
      });
    }
    else {
      // �������Ʒ��������ѯ��
      setPrice(new int[] {
        iRow
      });
    }
  }

  /**
   * ����༭���¼����� �������ڣ�(2004-3-4 16:13:17)
   * 
   * @param paramBillEditEvent
   */
  private void afterInvEdit(nc.ui.pub.bill.BillEditEvent e) throws Exception {

    // GenTimer time = new GenTimer();
    // GenTimer time2 = new GenTimer();
    // time2.start("���������ʼ��");
    // time.start();
    // ���ⷴ������ϼ�Ӱ��Ч��
    m_card.getBillModel().setNeedCalculate(false);
    int[] iaRow = null;

    int iRow = e.getRow();
    // �������
    BillItem bt = getBodyItem("cinvcode");
    UIRefPane refInv = (UIRefPane) bt.getComponent();
    // �������id
    String[] saOutInvID = refInv.getRefPKs();
    if (isNull(saOutInvID)
        || (e.getOldValue()!=null&&!saOutInvID[0].equals(((DefaultConstEnum)e.getOldValue()).getValue()))) {
      // ������Ϊ�ջ򲻵���ǰ�δ�������ǰ�εĴ����Ϣ
      clearInvInfo(iRow);
    }
    if (isNull(saOutInvID)) {
      return;
    }

    // ��˾id�������֯id
    HashMap hmCorp = getTransCorpID();
    String sInCorpID = hmCorp.get("cincorpid").toString();
    String sInCbID = hmCorp.get("cincbid").toString();
    String sOutCorpID = hmCorp.get("coutcorpid").toString();
    String sOutCbID = hmCorp.get("coutcbid").toString();
    String sTakeOutCorpID = hmCorp.get("ctakeoutcorpid").toString();
    String sTakeOutCbID = hmCorp.get("ctakeoutcbid").toString();
    String orderTypeCode = NodeInfo.getOrderTypeCode(m_card);
    if (orderTypeCode != null && (orderTypeCode.equals(ConstVO.m_sBillSFDBDD))) {
      sTakeOutCorpID = getHeadTailItemValue("ctakeoutcorpid");
      sTakeOutCbID = getHeadTailItemValue("ctakeoutcbid");
    }
    else {
      sTakeOutCorpID = getHeadTailItemValue("coutcorpid");
      sTakeOutCbID = getHeadTailItemValue("coutcbid");
    }
    // �����
    String[] saInvBasID = InvInfo.getInvBasID(saOutInvID);
    // ѡȡ�˶��д��ʱ
    boolean isLastRow = false;
    // �жϱ༭���Ƿ����һ��
    if (m_card.getRowCount() - 1 == e.getRow()) {
      isLastRow = true;
    }
    else {
      // ����༭�Ĳ������һ�У�ѡ�е�ǰ�е���һ�У��Ա�����ǰ�������
      m_card.getBillTable().setRowSelectionInterval(iRow + 1, iRow + 1);
    }
    iaRow = new int[saOutInvID.length];
    // �����ѡ�������1�У���Ҫ����n-1��
    for (int i = 0, len = saOutInvID.length; i < len; i++) {
      if (i > 0) {
        // ����༭�������һ�У���ҪaddLine
        if (isLastRow) {
          /*nc.ui.pub.bill.BillCardPanel.addLine()�����ᴥ��nc.ui.to.pubtransfer.TransferClientUI.onEditAction(int action)����ѭ������
          �˴���ȥ�������������У��ټ��ؼ����������Ͳ������nc.ui.to.pubtransfer.TransferClientUI.onEditAction(int action)
          ��Ϊnc.ui.to.pubtransfer.CardPanelCtrl.addLine()�е�nc.ui.to.pubtransfer.EditCtrl.initMultiNewLine(int[] iaRow)���ò���ȥ��
          ����ѭ�����ã�ÿ��һ��initMultiNewLine(int[] iaRow)���������Զ�����ӣ��˴�����ִ��initMultiNewLine(int[] iaRow)����Զ������*/
          m_card.removeActionListener("base");
          m_card.removeActionListener("relation");
          m_card.removeActionListener("exec2");
          m_card.removeActionListener("free");
          m_card.getBillModel().setNeedCalculate(false);
          try{
            m_card.addLine();
          }
          finally {
            m_card.addActionListener("base",ui);
            m_card.addActionListener("relation", ui);
            m_card.addActionListener("exec2",ui);
            m_card.addActionListener("free",ui);
            m_card.getBillModel().setNeedCalculate(true);
          }
        }
        // ����༭�����м��У���Ҫ������
        else {
          m_card.insertLine();
        }
      }
      iaRow[i] = iRow + i;
    }
    initMultiNewLine(iaRow,true);
    // Ϊ�����в����к�
    if (isLastRow) {
      nc.ui.scm.pub.report.BillRowNo.addLineRowNos(m_card, m_NodeInfo
          .getOrderTypeCode(m_card), "crowno", saOutInvID.length);
    }
    else {
      // iEndRow = iRow+sPKs.length,��˼�ǵ�ǰ�е���һ�������Ժ��λ��
      nc.ui.scm.pub.report.BillRowNo.insertLineRowNos(m_card, m_NodeInfo
          .getOrderTypeCode(m_card), "crowno", iRow + saOutInvID.length,
          saOutInvID.length - 1);
    }

    HashMap hmInvManID = null;
    try {
      // ������Ƿ���䵽��Ӧ�Ĺ�˾
      hmInvManID = BusinessCheck.invManDocChk(saInvBasID, new String[] {
          sInCorpID, sOutCorpID, sTakeOutCorpID
      });
    }
    catch (Exception ex) {
      // ������Ϊ�ջ򲻵���ǰ�δ�������ǰ�εĴ����Ϣ
      clearInvInfo(iRow);
      throw ex;
    }

    // time.showExecTime("У����������˾����֯");
    // �����Ϣ�ĳ���ȡ
    HashMap hmParam = InvInfo.getMngInfo(saInvBasID, saOutInvID);
    // time.showExecTime("ȡ�����Ϣ");
    StringBuffer sbKey = new StringBuffer();
    ParamVO param = null;

    for (int ctr = 0; ctr < saOutInvID.length; ctr++) {
      // �������id
      String sOutInvID = saOutInvID[ctr];
      // �������id
      String sInvBasID = saInvBasID[ctr];
      sbKey.setLength(0);
      sbKey = sbKey.append(sInvBasID).append(sOutInvID);
      param = (ParamVO) hmParam.get(sbKey.toString());
      if (param.bLaborFlag || param.bDiscountFlag)
        throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40093010", "UPP40093010-000023")/*
                                                           * @res
                                                           * "����ѡȡΪ�����۸��ۿ۵Ĵ��"
                                                           */);

      // �������id
      String sInInvID = hmInvManID.get(sInvBasID + sInCorpID).toString();
      String sTakeOutInvID = hmInvManID.get(sInvBasID + sTakeOutCorpID)
          .toString();

      // ���ñ��еĴ����Ϣ
      HashMap hmValue = new HashMap();
      hmValue.put("cincorpid", sInCorpID);
      hmValue.put("cincbid", sInCbID);
      hmValue.put("coutcorpid", sOutCorpID);
      hmValue.put("coutcbid", sOutCbID);
      hmValue.put("ctakeoutcorpid", sTakeOutCorpID);
      hmValue.put("ctakeoutcbid", sTakeOutCbID);
      hmValue.put("cinvbasid", sInvBasID);
      hmValue.put("cininvid", sInInvID);
      hmValue.put("coutinvid", sOutInvID);
      hmValue.put("ctakeoutinvid", sTakeOutInvID);
      hmValue.put("coutinvclid", param.sInInvCls);
      hmValue.put("cinvcode", param.sInvcode);
      hmValue.put("cinvname", param.sInvname);
      hmValue.put("cinvspec", param.sInvspec);
      hmValue.put("cinvtype", param.sInvtype);
      hmValue.put("cinvaddress", param.sProdarea);
      hmValue.put("measname", param.sMeasName);
      hmValue.put("castunitid", param.sDefaultMeasID);
      hmValue.put("castunitname", param.sDefaultMeasName);
      hmValue.put("cquoteunitid", param.sMeasID);
      hmValue.put("cquoteunitname", param.sMeasName);
      hmValue.put("nchangerate", param.dRate == null ? "" : param.dRate
          .toString());
      hmValue.put("nquoteunitrate", param.dQuoteUnitRate.toString());

      String sMeasID = InvInfo.getPk_measdoc(sInvBasID);
      String sFixedFlag = sMeasID.equals(param.sMeasID) ? "Y" : InvInfo
          .getInvConvert(sInvBasID, param.sMeasID)[1];
      hmValue.put("bquotefixrate", sFixedFlag);// �����Ƿ񱨼ۼ�����λ�̶������ʣ��������۽�������ʹ��
      String[] saFreeItem = InvInfo.getFreeItem(sInvBasID);
      if (saFreeItem[saFreeItem.length - 1].equals("N")) {
        hmValue.put("vfree0", "");
      }
      // �������Ϸ���
      TOBillTool.setRowValue(m_card, hmValue, iRow + ctr);
    }
    // time.showExecTime("���������");
    // ����ÿ�еļ۸�
    try {
      // �޸Ļ������������Զ����ѯ��
      getPrice(iaRow);

      // time.showExecTime("ѯ�ۣ�");
      // ��ѯatp��sp
      BillEditEvent event = new BillEditEvent(m_card, e.getOldRow(), iRow);
      bodyRowChange(event);
      // time.showExecTime("��ѯatp��");

      // time2.showExecTime("�������������");
    }
    catch (Exception ex) {
      throw ex;
    }
    finally {
      // �ѺϼƵĿ��ش�
      m_card.getBillModel().setNeedCalculate(true);
    }

    /*
     * ��Ϊ�༭��ʽ����afterEdit����֮ǰִ�еģ���cinvbasid�ȴ����ص���Ϣ����afterEdit�����и�ֵ�ġ�
     * �����Ҫʹ��cinvbasid����Ϣ���幫ʽ��cinvbasid�ǵò���ֵ�á� ������Ҫ��afterEdit֮��ǿ�Ƶ��ñ༭��ʽ��
     * Ŀǰֻ���������������ֶ�(�������)��Ҫ������ӦafterEdit�����н��в��䡣
     */
    for (int row : iaRow) {
      BillItem item = m_card.getBodyItem(e.getKey());
      String[] formulas = item.getEditFormulas();
      m_card.execBodyFormulas(row, formulas);
    }
  }

  /**
   * ****************************************** ���ܣ� <|> �������ڣ�(2004-3-11
   * 20:58:45)
   * 
   * @param��
   * @return�� ******************************************
   */
  public boolean beforeAstUnitEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    int iRow = e.getRow();

    Object oInvID = getCellValue(iRow, "cinvbasid");
    if (isNull(oInvID)) {
      return false;
    }
    // �Ƿ񸨼�������
    UFBoolean bAstMng = InvInfo.getAstMngInfo(new String[] {
      oInvID.toString()
    })[0];
    if (bAstMng.booleanValue() == false) {
      sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
          "UPP40093010-000024")/* @res "��ǰ������Ǹ���������" */);
      return false;
    }
    // ȡ����λ
    BillItem bt = getBodyItem(e.getKey());
    if (bt != null) {
      UIRefPane refAstUnit = (UIRefPane) bt.getComponent();
      refAstUnit.setReturnCode(false);
      m_invMeas.filterMeas(getHeadTailItemValue("coutcorpid"), getCellValue(
          iRow, "coutinvid"), refAstUnit);
      if (e.getKey().indexOf("quote") >= 0) {
        m_invQuoteMeas.filterMeas(getHeadTailItemValue("coutcorpid"),
            getCellValue(iRow, "coutinvid"), refAstUnit);
      }
      else {
        // Object ject=e.getValue();
        m_invMeas.filterMeas(getHeadTailItemValue("coutcorpid"), getCellValue(
            iRow, "coutinvid"), refAstUnit);
      }
      refAstUnit.setBlurValue(null);
      // refAstUnit.setValue(null);
      //
      // //���ø�������λ����
      // ((nc.ui.to.pub.AssunitRefmodel) refAstUnit.getRefModel())
      // .setInvs(oInvID.toString());
    }
    return true;

  }

  /**
   * �����ߣ� ���ܣ��������κ�¼��֮ǰ�Ĵ��� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public boolean beforeBatchEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    sendHintMsg("");
    int iRow = e.getRow();
    String sInvBasID = getCellValue(iRow, "cinvbasid");
    // �����������id
    String sOutInvID = getCellValue(iRow, "ctakeoutinvid");
    // �����ֿ�
    String orderTypeCode = NodeInfo.getOrderTypeCode(m_card);
    String sWhID = null;
    if (orderTypeCode != null && (orderTypeCode.equals(ConstVO.m_sBillSFDBDD))) {
      sWhID = getCellValue(iRow, "ctakeoutwhid");
    }
    else {
      sWhID = getCellValue(iRow, "coutwhid");
    }
    BillItem bt = getBodyItem("vbatch");
    if (m_LotNumbRefPane == null) {
      m_LotNumbRefPane = ((nc.ui.ic.pub.lot.LotNumbRefPane) bt.getComponent());
      m_LotNumbRefPane.setAutoCheck(true);
      
      //��ȡ�����ļ�..modules\scmpub\scmcustomer\customerconfig.xml
      if (m_NodeInfo.getProjectName()
          .equalsIgnoreCase(nc.vo.scm.pub.CustomerConfigVO.NAME_FSBIGUIYUAN)) {
        m_LotNumbRefPane.setIsMutiSel(true);//zlq ��ɽ�̹�԰
      }      
    }
    // �Ƿ�������
    if (isNull(sInvBasID) || isNull(sWhID) || isNull(sOutInvID)) {
      if (orderTypeCode.equals(ConstVO.m_sBillSFDBDD)) {
        throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40093010", "UPP40093010-000025")/*
                                                           * @res
                                                           * "�����������������ֿ⣬���������Σ�"
                                                           */);
      }
      else {
        throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40093010", "UPP40093010-000414")/*
                                                           * @res
                                                           * "�����������������ֿ⣬���������Σ�"
                                                           */);
      }
    }

    String sOutCbID = getHeadTailItemValue("ctakeoutcbid");
    String sOutCorpID = getHeadTailItemValue("ctakeoutcorpid");
    ParamVO paramResult = InvInfo.getBatchMngInfo(sOutInvID);
    // �Ƿ����ι���
    if (!paramResult.bLotMngFlag.booleanValue()) {
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "SCMCOMMON", "UPPSCMCommon-000102")/*
                                               * @res "�����ι�����!"
                                               */);
    }

    // ���ù�ʽ�����������
    InvoInfoBYFormula f = new InvoInfoBYFormula();
    // �����β��մ�������Ϣ
    InvVO voInv = f.getInvVO(sOutInvID, sOutCbID, sWhID, sOutCorpID);
    // �������ι����־
    voInv.setIsLotMgt(new Integer(1));
    // ���ø�����
    voInv.setCastunitid(getCellValue(iRow, "castunitid"));
    voInv.setCastunitname(getCellValue(iRow, "castunitname"));
    // ���ü�����λ
    voInv.setMeasdocname(getCellValue(iRow, "measname"));
    setBodyFreeValue(iRow, voInv);
    // �õ�WhVO
    WhVO voWh = new WhVO();
    Object[][] obj2 = nc.ui.scm.pub.CacheTool.getMultiColValue("bd_stordoc",
        "pk_stordoc", new String[] {
            "storcode", "storname", // ����
            "gubflag", // �Ƿ��Ʒ��
            "csflag" // �Ƿ��λ����
        }, new String[] {
          sWhID
        });
    voWh.setCwarehouseid(sWhID);
    voWh.setCwarehousecode(obj2[0][0].toString());
    voWh.setCwarehousename(obj2[0][1].toString());
    if (obj2[0][2].toString().equalsIgnoreCase("N")) {
      voWh.setIsWasteWh(new Integer(0));
    }
    else {
      voWh.setIsWasteWh(new Integer(1));
    }
    if (obj2[0][3].toString().equalsIgnoreCase("N")) {
      voWh.setIsLocatorMgt(new Integer(0));
    }
    else {
      voWh.setIsLocatorMgt(new Integer(1));
    }
    voWh.setPk_corp(sOutCorpID);
    voWh.setPk_calbody(sOutCbID);
    // ���ÿ����֯����
    BillItem bt2 = m_card.getHeadItem("coutcbid");
    UIRefPane pane = (UIRefPane) bt2.getComponent();
    voWh.setVcalbodyname(pane.getRefName());
    // �������β��յĲֿ���Ϣ
    m_LotNumbRefPane.setParameter(voWh, voInv);
    return true;
  }

  /**
   * �������ڣ�(2004-2-18 11:40:15) ���ߣ����˾� ������ ���أ� ˵���� �������޸�ǰ�Ŀ��ơ� Ϊtrueʱ�ſɱ༭��
   * 
   * @param eBillEditEvent
   */
  protected boolean beforeVfree0Edit(nc.ui.pub.bill.BillEditEvent e)
      throws BusinessException {

    int iRow = e.getRow();
    String sInvBasID = getCellValue(iRow, "cinvbasid");
    String sOutInvID = getCellValue(iRow, "coutinvid");

    if (isNull(sInvBasID) || isNull(sOutInvID)) {
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPP40093010-000026")/*
                                             * @res "������������Ϣ����������������Ϣ��"
                                             */);
    }

    // �Ƿ�Ϊ���������Ĵ����
    String[] saFreeItem = InvInfo.getFreeItem(sInvBasID);
    if (saFreeItem[saFreeItem.length - 1].equals("Y")) {
      // ����������մ�������Ϣ
      InvVO voInv = new InvVO();
      voInv.setCinventoryid(sOutInvID);
      voInv.setCinvmanid((String) getCellValue(iRow, "cinvbasid"));
      voInv.setIsFreeItemMgt(new Integer(1));
      voInv.setCinventorycode((String) getCellValue(iRow, "cinvcode"));
      voInv.setInvname((String) getCellValue(iRow, "cinvname"));
      voInv.setInvspec((String) getCellValue(iRow, "cinvspec"));
      voInv.setInvtype((String) getCellValue(iRow, "cinvtype"));
      int iFreeLen = 5;
      for (int i = 0; i < iFreeLen; i++) {
        voInv.setAttributeValue("vfreeid" + (i + 1), saFreeItem[i]);
        voInv
            .setAttributeValue("vfreename" + (i + 1), saFreeItem[i + iFreeLen]);
        voInv.setAttributeValue("vfree" + (i + 1), m_card.getBillModel()
            .getValueAt(iRow, "vfree" + (i + 1)));
      }

      getRefFreeItem().setFreeItemParam(voInv);
      return true;

    }
    else {
      // ������ɱ༭
      // �����������Ϣ
      for (int i = 1; i <= 5; i++) {
        m_card.getBillModel().setValueAt(null, iRow, "vfreeid" + i);
        m_card.getBillModel().setValueAt(null, iRow, "vfree" + i);
      }
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "SCMCOMMON", "UPPSCMCommon-000103")/*
                                               * @res "�������������!"
                                               */);
    }
  }

  /*****************************************************************************
   * ���ܣ���Ƭģ����ClientUI������Ϣ �������ڣ�(2004-3-13 10:50:10)
   * 
   * @param��
   * @return�� ******************************************
   */
  protected void sendHintMsg(String sMsg) {
    firePanelEvent(GenPanelEvent.HINT, sMsg);
  }

  /*****************************************************************************
   * ���ܣ���Ƭģ����ClientUI������Ϣ �������ڣ�(2004-3-13 10:50:10)
   * 
   * @param��
   * @return�� ******************************************
   */
  protected void sendErrMsg(String sMsg) {
    firePanelEvent(GenPanelEvent.ERR, sMsg);
  }

  private Object firePanelEvent(int iType, Object oValue) {
    GenPanelEvent pe = new GenPanelEvent(m_card, iType, oValue);
    pe.setSourceID(GenPanelEvent.ID_FIRST_CARD);
    return m_pelistener.action(pe);
  }

  protected BusinessCheck m_bizCheck;

  /**
   * �������ڣ�(2004-2-10 18:20:00) ���ߣ����˾� ������ ���أ� ˵���� ����panel event ����
   */
  public void addPanelEventListener(GenPanelEventListener l) {
    m_pelistener = l;
  }

  /**
   * ���ܣ�������/���۵�λ�����ı��¼����� ������/���۵�λ�����ı�,�����������ı�,�Ӷ����𱨼۵�λ����/�������ı� ������e���ݱ༭�¼� ���أ�
   * ���⣺ ���ڣ�(2001-5-8 19:08:05) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void afterAstNumEdit(nc.ui.pub.bill.BillEditEvent e) throws Exception {

    int iRow = e.getRow();
    String sInvBasID = getCellValue(iRow, "cinvbasid");
    String sOutInvID = getCellValue(iRow, "coutinvid");
    if (isNull(sInvBasID) || isNull(sOutInvID)) {
      return;
    }

    // �������������Ϊ�п��ܲ�ѯ�ۣ�����Ҫ����������
    calculateNumPriceMny(e);
    
    // ���ü۸�
    setPrice(new int[] {
      iRow
    });


    Double dMainNum = new Double(m_card.getBillModel().getValueAt( iRow, "nnum").toString());
    setBretractFlag(dMainNum, iRow);
  }

  protected String getCellValue(int iRow, String sKey) {
    return CardPanelTool.getCellValue(m_card, iRow, sKey);
  }

  private String getHeadTailItemValue(String sKey) {
    return CardPanelTool.getHeadTailItemValue(m_card, sKey);
  }

  /**
   * �����ߣ� ���ܣ��������κű༭�Ĵ��� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void afterBatchEdit(int iRow) throws Exception {
    // ������κŵ����ֶ�
    String[] sBatchFields = new String[] {
        "tbatchtime", "dproducedate", "dvalidate", "vvendbatchcode",
        "cqualitylevelid", "vnote", "tchecktime", "pk_batchcode",
        "cqualitylevelname", "vdef1", "vdef2", "vdef3", "vdef4", "vdef5",
        "vdef6", "vdef7", "vdef8", "vdef9", "vdef10", "vdef11", "vdef12",
        "vdef13", "vdef14", "vdef15", "vdef16", "vdef17", "vdef18", "vdef19",
        "vdef20", "pk_defdoc1", "pk_defdoc2", "pk_defdoc3", "pk_defdoc4",
        "pk_defdoc5", "pk_defdoc6", "pk_defdoc7", "pk_defdoc8", "pk_defdoc9",
        "pk_defdoc10", "pk_defdoc11", "pk_defdoc12", "pk_defdoc13",
        "pk_defdoc14", "pk_defdoc15", "pk_defdoc16", "pk_defdoc17",
        "pk_defdoc18", "pk_defdoc19", "pk_defdoc20"
    };
    // ���κŵ����ֶ��ڵ�����������ģ���ϵ�itemkey
    String[] sTargetFields = new String[] {
        "ic_tbatchtime", "dproducedate", "dvalidate", "ic_vvendbatchcode",
        "ic_cqualitylevelid", "ic_vnote", "ic_tchecktime", "ic_pk_batchcode",
        "ic_cqualitylevelname", "ic_vdef1", "ic_vdef2", "ic_vdef3", "ic_vdef4",
        "ic_vdef5", "ic_vdef6", "ic_vdef7", "ic_vdef8", "ic_vdef9",
        "ic_vdef10", "ic_vdef11", "ic_vdef12", "ic_vdef13", "ic_vdef14",
        "ic_vdef15", "ic_vdef16", "ic_vdef17", "ic_vdef18", "ic_vdef19",
        "ic_vdef20", "ic_pk_defdoc1", "ic_pk_defdoc2", "ic_pk_defdoc3",
        "ic_pk_defdoc4", "ic_pk_defdoc5", "ic_pk_defdoc6", "ic_pk_defdoc7",
        "ic_pk_defdoc8", "ic_pk_defdoc9", "ic_pk_defdoc10", "ic_pk_defdoc11",
        "ic_pk_defdoc12", "ic_pk_defdoc13", "ic_pk_defdoc14", "ic_pk_defdoc15",
        "ic_pk_defdoc16", "ic_pk_defdoc17", "ic_pk_defdoc18", "ic_pk_defdoc19",
        "ic_pk_defdoc20"
    };

    String sTemp = getCellValue(iRow, "vbatch");
    String sInvID = getCellValue(iRow, "cinvbasid");
    String sOutInvID = getCellValue(iRow, "ctakeoutinvid");
    String sOutCorpID = getCellValue(iRow, "ctakeoutcorpid");

    ////�������
    if (isNull(sTemp) || isNull(sInvID) || isNull(sOutInvID)) {
      // ������κ�����ֶ�
      TOBillTool.clearRowValues(m_card, iRow, sTargetFields);

      // ����ѯ��
      setPrice(new int[] {
        iRow
      });
      
      return;
    }

    // ���β���
    LotNumbRefPane ref = (LotNumbRefPane) getBodyItem("vbatch").getComponent();
    ref.setAutoCheck(true);
    
    //��ȡ�����ļ�..modules\scmpub\scmcustomer\customerconfig.xml
    if (m_NodeInfo.getProjectName()
        .equalsIgnoreCase(nc.vo.scm.pub.CustomerConfigVO.NAME_FSBIGUIYUAN)) {
      //����Ǳ̹�Բ��Ŀ���༭���Σ��Զ�����������
      setFreeAfterBatchEdit(iRow,ref);
    }
  
    boolean bExist = ref.checkData();
    if (!bExist) {
      // ������κ�����ֶ�
      TOBillTool.clearRowValues(m_card, iRow, sTargetFields);
      // ��ռ۸�
      clearPrice(new int[] {
        iRow
      });
      return;
    }
    // ��ȡ���ε�����Ϣ
    BatchcodeVO bcvo = BatchCodeDefSetTool.getBatchCodeInfo(sOutInvID, sTemp,
        sOutCorpID);
    if (bcvo != null) {
      for (int i = 0; i < sBatchFields.length; i++) {
        m_card.getBillModel().setValueAt(
            bcvo.getAttributeValue(sBatchFields[i]), iRow, sTargetFields[i]);
      }
    }

    // ����ѯ��
    setPrice(new int[] {
      iRow
    });
  }

  private void setFreeAfterBatchEdit(int iRow, LotNumbRefPane ref) {
    String sOutInvID = getCellValue(iRow, "ctakeoutinvid");
    String sOutCorpID = getCellValue(iRow, "ctakeoutcorpid");
    //  zlq ��ɽ�̹�԰ ��������¼������ʱ��ͬ�����ϳ��ⵥ��������һ��������ͨ��ѡ�����κţ������������
    ////////////////////////////////////////////////////////////////////////////////////////////
    // �����ֿ�
    String sWhID = getCellValue(iRow, "ctakeoutwhid");
    if(VOChecker.isEmpty(sWhID)){
      sWhID = getCellValue(iRow, "coutwhid");
    }
    String sOutCbID = getHeadTailItemValue("ctakeoutcbid");
    
    LotNumbRefVO[] lotRefVO = ref.getLotNumbRefVOs();
    if (lotRefVO.length>0&&lotRefVO[0] != null) {
      FreeVO voFree = lotRefVO[0].getFreeVO();
      if (isNull(voFree))
        return;
      HashMap hmValue = new HashMap(5);
      for (int i = 0; i <= 5; i++) {
        hmValue.put("vfree" + i, voFree.getAttributeValue("vfree" + i));
      }
      TOBillTool.setRowValue(m_card, hmValue, iRow);

      InvVO voInv = new InvVO();
      voInv.setCinventoryid(sOutInvID);
      voInv.setCinvmanid((String) getCellValue(iRow, "cinvbasid"));
      voInv.setCinventorycode((String) getCellValue(iRow, "cinvcode"));
      voInv.setInvname((String) getCellValue(iRow, "cinvname"));
      voInv.setInvspec((String) getCellValue(iRow, "cinvspec"));
      voInv.setInvtype((String) getCellValue(iRow, "cinvtype"));
      
      voInv.setAttributeValue("vfree0", voFree.getAttributeValue("vfree0"));
      getRefFreeItem().setFreeItemParam(voInv);

      // �����β���ʱ��ѯ����������ϢȻ�����õ����β�����
      WhVO voWh = new WhVO();
      Object[][] obj2 = nc.ui.scm.pub.CacheTool.getMultiColValue("bd_stordoc",
          "pk_stordoc", new String[] {
          "storcode", "storname", // ����
          "gubflag", // �Ƿ��Ʒ��
          "csflag" // �Ƿ��λ����
      }, new String[] {
          sWhID
      });
      voWh.setCwarehouseid(sWhID);
      
      voWh.setCwarehousecode(obj2[0][0].toString());
      voWh.setCwarehousename(obj2[0][1].toString());
      if (obj2[0][2].toString().equalsIgnoreCase("N")) {
        voWh.setIsWasteWh(new Integer(0));
      }
      else {
        voWh.setIsWasteWh(new Integer(1));
      }
      if (obj2[0][3].toString().equalsIgnoreCase("N")) {
        voWh.setIsLocatorMgt(new Integer(0));
      }
      else {
        voWh.setIsLocatorMgt(new Integer(1));
      }
      
      voWh.setPk_corp(sOutCorpID);
      voWh.setPk_calbody(sOutCbID);
      //���ÿ����֯����
      BillItem bt2 = m_card.getHeadItem("coutcbid");
      UIRefPane pane = (UIRefPane) bt2.getComponent();
      voWh.setVcalbodyname(pane.getRefName());
      //�������β��յĲֿ���Ϣ
      m_LotNumbRefPane.setParameter(voWh, voInv);

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////

  
  }

  /**
   * �����ߣ� ���ܣ��������ı��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void afterChangeRateEdit(nc.ui.pub.bill.BillEditEvent e) {
    int iRow = e.getRow();
    UFDouble dMainNum = null;
    Object oMainNum = null;
    Object oAstNum = null;
    UFDouble dAstNum = null;
    UFDouble dQuoteNum = null;
    Object oRate = null;
    UFDouble dRate = null;
    String sQuoterate = null;

    oRate = getCellValue(iRow, "nchangerate");

    if (isNull(oRate)) {
      // ���������������
      TOBillTool.clearRowValues(m_card, iRow, new String[] {
          "nassistnum", "nnum"
      });
      return;
    }
    oMainNum = getCellValue(iRow, "nnum");
    if (isNull(oMainNum))
      return;

    dMainNum = new UFDouble(oMainNum.toString().trim());
    dRate = new UFDouble(oRate.toString().trim());

    if (dRate.doubleValue() == 0.0) {
      sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
          "UPP40093010-000028")/* @res "�����ʲ���Ϊ�ջ�0" */);
      return;
    }

    // ����������=������/������
    dAstNum = dMainNum.div(dRate);
    // ���۵�λ����=������/���۵�λ������
    sQuoterate = getCellValue(iRow, "nquoteunitrate");
    if (!isNull(sQuoterate)) {
      dQuoteNum = dMainNum.div(new UFDouble(sQuoterate));
    }

    // �����ܽ��
    HashMap hmValue = new HashMap(4);

    hmValue.put("nquoteunitnum", isNull(dQuoteNum) ? "" : dQuoteNum.toString());
    hmValue.put("nassistnum", isNull(dAstNum) ? "" : dAstNum.toString());
    TOBillTool.setRowValue(m_card, hmValue, iRow);

  }

  /**
   * ****************************************** ���ܣ� <|> �������ڣ�(2004-3-15
   * 21:57:54)
   * 
   * @param��
   * @return�� ******************************************
   */
  private void afterCurrTypeEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    String sOutCorpID = getHeadTailItemValue("coutcorpid");
    String sCurrType = getHeadTailItemValue("coutcurrtype");
    freshScale(sOutCorpID, sCurrType);
    setRate(sCurrType, sOutCorpID);
    BusinessCurrencyRateUtil currArith = getBizCheck().getCurrArith(sOutCorpID);
    setRateEdit(false, sOutCorpID, sCurrType);

    // ����ȡ�����еļ۸�
    int iCount = m_card.getBillModel().getRowCount();
    if (iCount > 0) {
      int[] iaRow = new int[iCount];
      boolean bHasBody = false;
      for (int i = 0; i < iCount; i++) {
        // �����Ƿ������˴��
        if (isNull(getCellValue(i, "cinvcode"))) {
          continue;
        }
        iaRow[i] = i;
        bHasBody = true;
      }
      // ��ȡ��
      if (bHasBody) {
        setPrice(iaRow);
      }
    }

  }

  /**
   * ****************************************** ���ܣ� <|> �������ڣ�(2004-3-15
   * 21:57:54)
   * 
   * @param��
   * @return�� ******************************************
   */
  private void afterCustEdit(nc.ui.pub.bill.BillEditEvent e) throws Exception {
    int iRow = e.getRow();
    // (UIRefPane)getBodyItem(
    // "creceievename").getComponent();
    String sCustManID = getCellValue(iRow, "creceieveid");
    if (isNull(sCustManID)) {
      // setCellValue(new String[][]{new String[]{null,null},new
      // String[]{"vreceiveaddress","arriveareaname"}},iRow,null);
      return;
    }

    // ����������ַ���ջ��������ջ��ص�
    // ������ַ
    UIRefPane refAddress = (UIRefPane) getBodyItem("vreceiveaddress")
        .getComponent();
    if (refAddress != null) {
      refAddress.setAutoCheck(false);
      refAddress.setReturnCode(false);
      // ������ַ����
      ((nc.ui.scm.ref.prm.CustAddrRefModel) refAddress.getRefModel())
          .setCustId(sCustManID);
      // �ͻ���������ID
      Object[] obj0 = (Object[]) CacheTool.getCellValue("bd_cumandoc",
          "pk_cumandoc", "pk_cubasdoc", sCustManID);
      String sPk_cubasdoc = obj0[0].toString();
      m_card.getBillModel().setValueAt(sPk_cubasdoc, iRow, "creceiverbasid");
      // ��ǰ�ջ��ͻ���Ĭ�ϵ�����ַ
      Object[][] obj1 = CacheTool.getAnyValue2("bd_custaddr", "pk_custaddr",
          " defaddrflag='Y' and pk_cubasdoc='" + sPk_cubasdoc + "'");
      if (obj1 != null && obj1[0][0] != null) {
        refAddress.setPK(obj1[0][0].toString());
      }
      else {
        refAddress.setPK(null);
      }
      if (refAddress.getRefPK() != null) {
        // ��ǰ�ջ��ͻ���Ĭ�ϵ�����ַ
        HashMap hmValue = new HashMap(5);
        hmValue.put("vreceiveaddress", refAddress.getRefCode());
        hmValue.put("pk_arrivearea", (String) refAddress
            .getRefValue("bd_custaddr.pk_areacl"));
        hmValue.put("arriveareaname", (String) refAddress
            .getRefValue("bd_areacl.areaclname"));
        hmValue.put("pk_areacl", (String) refAddress
            .getRefValue("bd_custaddr.pk_address"));
        hmValue.put("areaclname", (String) refAddress
            .getRefValue("bd_address.addrname"));
        TOBillTool.setRowValue(m_card, hmValue, iRow);
      }
      else {
        String sAddress = (String) refAddress.getUITextField().getText();
        m_card.getBillModel().setValueAt(sAddress, iRow, "vreceiveaddress");
        TOBillTool.clearRowValues(m_card, iRow, new String[] {
            "", "pk_arrivearea", "pk_areacl", "arriveareaname", "areaclname"
        });
      }
    }
  }

  public static int[] nDescriptions = {
      // ��˰���ơ����
      SCMRelationsCal.DISCOUNT_TAX_TYPE_NAME,
      // �Ƿ�̶�������
      SCMRelationsCal.IS_FIXED_CONVERT,
      // ˰��
      SCMRelationsCal.TAXRATE,

      // ����
      SCMRelationsCal.NUM,
      SCMRelationsCal.NUM_ASSIST,
      SCMRelationsCal.CONVERT_RATE,

      // ������ԭ�Ҽ۸񡢽��
      SCMRelationsCal.NET_PRICE_ORIGINAL,// �ڲ����׵ĺ�˰����ӳ�䵽�����㷨�ĺ�˰�����ϣ�Ĭ���ۿ�Ϊ100��
      SCMRelationsCal.NET_TAXPRICE_ORIGINAL, SCMRelationsCal.MONEY_ORIGINAL,
      SCMRelationsCal.SUMMNY_ORIGINAL, SCMRelationsCal.TAX_ORIGINAL,

      // ���۵�λʹ��
      SCMRelationsCal.QT_NET_PRICE_ORIGINAL,
      SCMRelationsCal.QT_NET_TAXPRICE_ORIGINAL,
      // SCMRelationsCal.QT_NET_PRICE_LOCAL,//���۵�λ��˰����
      // SCMRelationsCal.QT_NET_TAXPRICE_LOCAL,//���۵�λ��˰����
      SCMRelationsCal.QT_NUM, SCMRelationsCal.QT_CONVERT_RATE,// ���۵�λ������
      SCMRelationsCal.QT_IS_FIXED_CONVERT,// �Ƿ񱨼۹̶�������
      SCMRelationsCal.QUOTEUNITID,// ���ۼ�����λ

      // ���㾫��ʹ��
      SCMRelationsCal.CURRTYPEPk,// ԭ��PK
      SCMRelationsCal.PK_CORP,
  };

  public static String[] getKeys() {
    return new String[] {
        // ��˰���ơ����
        // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009","UPP40093009-000046")/*@res
        // "Ӧ˰���"*/,// TODO ���ݼ�˰�������
        "Ӧ˰���", /*-=notranslate=-*/
        "Y",
        // ˰�ʡ�
        "ntaxrate",

        // ����
        "nnum", "nassistnum", "nchangerate",

        // ������ԭ�Ҽ۸񡢽��
        "nnotaxprice", "nprice", "nnotaxmny", "nmny", "ntaxmny",

        "norgqtnetprc",// ���۵�λ��˰����
        "norgqttaxnetprc",// ���۵�λ��˰����
        "nquoteunitnum", "nquoteunitrate", "bquotefixrate", "cquoteunitid",

        "coutcurrtype", "coutcorpid",
    };
  }

  public static String[] getnoKeys() {
    return new String[] {
        // ��˰���ơ����
        // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009","UPP40093009-000046")/*@res
        // "Ӧ˰���"*/,// TODO ���ݼ�˰�������
        "Ӧ˰���", /*-=notranslate=-*/
        "N",
        // ˰�ʡ�
        "ntaxrate",

        // ����
        "nnum", "nassistnum", "nchangerate",

        // ������ԭ�Ҽ۸񡢽��
        "nnotaxprice", "nprice", "nnotaxmny", "nmny", "ntaxmny",

        "norgqtnetprc",// ���۵�λ��˰����
        "norgqttaxnetprc",// ���۵�λ��˰����
        "nquoteunitnum", "nquoteunitrate", "bquotefixrate", "cquoteunitid",

        "coutcurrtype", "coutcorpid",
    };
  }

  /**
   * ****************************************** ���ܣ� <|> �������ڣ�(2004-3-15
   * 21:57:54)
   * 
   * @param��
   * @return�� ******************************************
   */

  private void afterNTaxRateEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    int iRow = e.getRow();
    UFDouble[] daPrices = null;
    UFDouble dTaxRate = null;
    String sTemp = getCellValue(iRow, "ntaxrate");
    if (isNull(sTemp) || !checkNumInput(e)) {
      freshPrice(iRow, daPrices);
      return;
    }

    dTaxRate = new UFDouble(sTemp).div(100);
    sTemp = getCellValue(iRow, "nprice");
    if (isNull(sTemp))
      return;

    UFDouble dPriceWTax = new UFDouble(sTemp);
    sTemp = getCellValue(iRow, "nnotaxprice");
    if (isNull(sTemp))
      return;

    ClientCommonDataHelper cbo = new ClientCommonDataHelper();
    // ��˰���ȣ��Ǻ�˰����"TO0017";
    String sTaxFirst = cbo.getParaValue(getHeadTailItemValue("coutcorpid"),
        ConstVO.m_iPara_SFHSYX);

    int iTaxFirstFlag = RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;
    if (sTaxFirst.equals("Y")) {
      iTaxFirstFlag = RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;
    }
    else {
      iTaxFirstFlag = RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE;
    }
    int[] iaPrior = {
      iTaxFirstFlag
    };

    // ���ü�˰���
    String[] strKeys = getKeys();
    strKeys[0] = getTaxtypeflag(iRow);
    RelationsCal.calculate(iRow, m_card, iaPrior, e.getKey(), nDescriptions,
        strKeys, BillItemVO.class.getName(), BillHeaderVO.class.getName());

  }

  private void afterPriceTaxEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    int iRow = e.getRow();
    UFDouble dPriceWTax = null;
    Object oTemp = getCellValue(iRow, e.getKey());
    if (isNull(oTemp) || !checkNumInput(e)) {
      TOBillTool.clearRowValues(m_card, iRow, new String[] {
          "nnotaxprice", "nnotaxmny", "nprice", "nmny", "norgqttaxnetprc",
          "norgqtnetprc"
      });
      return;
    }

    int iTaxFirstFlag = RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;
    int[] iaPrior = {
      iTaxFirstFlag
    };

    // ���ü�˰���
    String[] strKeys = getKeys();
    strKeys[0] = getTaxtypeflag(iRow);
    RelationsCal.calculate(iRow, m_card, iaPrior, e.getKey(), nDescriptions,
        strKeys, BillItemVO.class.getName(), BillHeaderVO.class.getName());

  }

  /**
   * ****************************************** ���ܣ� <|> �������ڣ�(2004-3-15
   * 21:57:54)
   * 
   * @param��
   * @return�� ******************************************
   */
  private void afterPriceNoTaxEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    int iRow = e.getRow();
    UFDouble dPriceWTax = null;
    Object oTemp = getCellValue(iRow, e.getKey());
    if (isNull(oTemp) || !checkNumInput(e)) {
      TOBillTool.clearRowValues(m_card, iRow, new String[] {
          "nnotaxprice", "nnotaxmny", "nprice", "nmny", "norgqttaxnetprc",
          "norgqtnetprc"
      });
      return;
    }

    int iTaxFirstFlag = RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE;
    int[] iaPrior = {
      iTaxFirstFlag
    };

    // ���ü�˰���
    String[] strKeys = getKeys();
    strKeys[0] = getTaxtypeflag(iRow);
    RelationsCal.calculate(iRow, m_card, iaPrior, e.getKey(), nDescriptions,
        strKeys, BillItemVO.class.getName(), BillHeaderVO.class.getName());

  }

  private boolean isInSameCorp(){
    String orderTypeCode = null;
    try {
      orderTypeCode = NodeInfo.getOrderTypeCode(m_card);
    }
    catch (BusinessException e) {
      return true;
    }
    if (orderTypeCode != null
        && (orderTypeCode.equals(ConstVO.m_sBillZZJDBDD) || orderTypeCode
            .equals(ConstVO.m_sBillZZNDBDD))) {
      return true;
    }
    else {
      return false;
    }

  }

  private String getTaxtypeflag(int iRow) throws BusinessException {
    QryTransrelaVO[] voTransItem = getTransItem(new int[] {
      iRow
    });
    if (isNull(voTransItem)) {
      //5I��5E��ƥ�������ϵ����ʱҪ���˰���Ĭ��Ӧ˰���
      return "Ӧ˰���";
    }
    Integer iTemp = voTransItem[0].getTaxtypeflag();
    switch (iTemp.intValue()) {
      case ConstVO.TAX_TYPE_IN:
        return "Ӧ˰�ں�"; /*-=notranslate=-*/
        // return
        // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009","UPP40093009-000258")/*@res
        // "Ӧ˰�ں�"*/;
      case ConstVO.TAX_TYPE_OUT:

        return "Ӧ˰���"; /*-=notranslate=-*/
        // return
        // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009","UPP40093009-000257")/*@res
        // "Ӧ˰���"*/;
      case ConstVO.TAX_TYPE_NULL:
        String flag = getHeadTailItemValue("bdrictflag");
        if (flag != null && flag.length() > 0 && flag.equals("true")) {
          return "Ӧ˰���";
        }
        return "����˰"; /*-=notranslate=-*/
        // return
        // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009","UPP40093009-000259")/*@res
        // "����˰"*/;
      default:
        return "����˰"; /*-=notranslate=-*/
        // return
        // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009","UPP40093009-000259")/*@res
        // "����˰"*/;
    }
  }

  /**
   * ****************************************** ���ܣ� <|> �������ڣ�(2004-3-15
   * 21:57:54)
   * 
   * @param��
   * @return�� ******************************************
   */
  private void afterMnyNoTaxEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    int iRow = e.getRow();
    UFDouble dPriceWTax = null;
    Object oTemp = getCellValue(iRow, e.getKey());
    if (isNull(oTemp) || !checkNumInput(e)) {
      TOBillTool.clearRowValues(m_card, iRow, new String[] {
          "nnotaxprice", "nnotaxmny", "nprice", "nmny", "norgqttaxnetprc",
          "norgqtnetprc"
      });
      return;
    }

    int iTaxFirstFlag = RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE;
    int[] iaPrior = {
      iTaxFirstFlag
    };

    // ���ü�˰���
    String[] strKeys = getKeys();
    strKeys[0] = getTaxtypeflag(iRow);

    RelationsCal.calculate(iRow, m_card, iaPrior, e.getKey(), nDescriptions,
        strKeys, BillItemVO.class.getName(), BillHeaderVO.class.getName());

  }

  /**
   * ****************************************** ���ܣ� <|> �������ڣ�(2004-3-15
   * 21:57:54)
   * 
   * @param��
   * @return�� ******************************************
   */
  private void afterMnyTaxEdit(nc.ui.pub.bill.BillEditEvent e) throws Exception {

    int iRow = e.getRow();
    UFDouble dPriceWTax = null;
    Object oTemp = getCellValue(iRow, e.getKey());
    if (isNull(oTemp) || !checkNumInput(e)) {
      TOBillTool.clearRowValues(m_card, iRow, new String[] {
          "nnotaxprice", "nnotaxmny", "nprice", "nmny", "norgqttaxnetprc",
          "norgqtnetprc"
      });
      return;
    }

    int iTaxFirstFlag = RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;
    int[] iaPrior = {
      iTaxFirstFlag
    };

    // ���ü�˰���
    String[] strKeys = getKeys();
    strKeys[0] = getTaxtypeflag(iRow);
    RelationsCal.calculate(iRow, m_card, iaPrior, e.getKey(), nDescriptions,
        strKeys, BillItemVO.class.getName(), BillHeaderVO.class.getName());

  }

  /**
   * ���ܣ� <|>���ۼ�����λ��˰���۱༭�� �������ڣ�(2004-3-15 21:57:54)
   * 
   * @param��
   * @return�� ******************************************
   */
  private void afterQuotePriceWOTaxEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    int iRow = e.getRow();
    UFDouble dPriceWTax = null, dPriceWOTax = null;
    Object oTemp = getCellValue(iRow, "norgqtnetprc");
    if (isNull(oTemp) || !checkNumInput(e)) {
      TOBillTool.clearRowValues(m_card, iRow, new String[] {
          "norgqtnetprc", "norgqttaxnetprc", "nnotaxprice", "nnotaxmny",
          "nprice", "nmny"
      });
      return;
    }

    UFDouble dQuotePriceWOTax = new UFDouble(oTemp.toString());
    UFDouble dQuotePriceWTax = null;
    QryTransrelaVO[] voTransItems = getTransItem(new int[] {
      iRow
    });
    if (isNull(voTransItems)) {
      return;
    }
    QryTransrelaVO voTransItem = voTransItems[0];
    String sTemp = getCellValue(iRow, "ntaxrate");
    UFDouble dTaxRate = null;
    String sQuoteRate = getCellValue(iRow, "nquoteunitrate");
    Integer iTemp = voTransItem.getTaxtypeflag();

    // ���ݡ���˰��������㡰��˰���ۡ���
    switch (iTemp.intValue()) {
      // Ӧ˰�ں�--- ����˰���ۡ�= ���Ǻ�˰���ۡ�/��1-˰�ʣ�
      case ConstVO.TAX_TYPE_IN:
        dTaxRate = sTemp == null ? voTransItem.getTaxrate() : new UFDouble(
            sTemp);
        dTaxRate = dTaxRate.div(100);
        dQuotePriceWTax = dQuotePriceWOTax.div(new UFDouble(1).sub(dTaxRate));
        break;
      // Ӧ˰���--- ����˰���ۡ�= ���Ǻ�˰���ۡ�*��1+˰�ʣ�
      case ConstVO.TAX_TYPE_OUT:
        dTaxRate = sTemp == null ? voTransItem.getTaxrate() : new UFDouble(
            sTemp);
        dTaxRate = dTaxRate.div(100);
        dQuotePriceWTax = dQuotePriceWOTax.multiply(new UFDouble(1)
            .add(dTaxRate));
        break;
      // ����˰--- ����˰���ۡ�= ���Ǻ�˰���ۡ�
      case ConstVO.TAX_TYPE_NULL:
        dQuotePriceWTax = dQuotePriceWOTax;
        break;
    }
    if (!isNull(sQuoteRate)) {
      dPriceWTax = dQuotePriceWTax.div(Double.parseDouble(sQuoteRate));
      dPriceWOTax = dQuotePriceWOTax.div(Double.parseDouble(sQuoteRate));
    }
    freshPrice(iRow, new UFDouble[] {
        dQuotePriceWTax, dQuotePriceWOTax, dPriceWTax, dPriceWOTax,
        dTaxRate == null ? null : dTaxRate.multiply(100),
        voTransItem.getAddpricerate()
    });
  }

  /**
   * ****************************************** ���ܣ� <|> �������ڣ�(2004-3-15
   * 21:57:54)
   * 
   * @param��
   * @return�� ******************************************
   */
  private void afterQuotePriceWTaxEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    int iRow = e.getRow();
    Object oTemp = getCellValue(iRow, "norgqttaxnetprc");
    if (isNull(oTemp) || !checkNumInput(e)) {
      TOBillTool.clearRowValues(m_card, iRow, new String[] {
          "nprice", "nnotaxprice", "nnotaxmny", "nprice", "nmny",
          "norgqttaxnetprc", "norgqtnetprc"
      });
      return;
    }

    UFDouble dQuotePriceWTax = new UFDouble(oTemp.toString());
    UFDouble dPriceWTax = null;
    UFDouble dQuotePriceWOTax = null;
    UFDouble dPriceWOTax = null;
    QryTransrelaVO[] voTransItems = getTransItem(new int[] {
      iRow
    });
    if (isNull(voTransItems)) {
      return;
    }
    QryTransrelaVO voTransItem = voTransItems[0];
    String sTaxrate = getCellValue(iRow, "ntaxrate");
    UFDouble dTaxRate = null;
    String sQuoteRate = getCellValue(iRow, "nquoteunitrate");
    Integer iTemp = voTransItem.getTaxtypeflag();

    // ���ݡ���˰��������㡰�Ǻ�˰���ۡ���
    switch (iTemp.intValue()) {
      // Ӧ˰�ں�--- ����˰���ۡ�= ���Ǻ�˰���ۡ�/��1-˰�ʣ�
      case ConstVO.TAX_TYPE_IN:
        dTaxRate = sTaxrate == null ? voTransItem.getTaxrate() : new UFDouble(
            sTaxrate);
        dTaxRate = dTaxRate.div(100);
        dQuotePriceWOTax = dQuotePriceWTax.multiply(new UFDouble(1)
            .sub(dTaxRate));
        break;
      // Ӧ˰���--- ����˰���ۡ�= ���Ǻ�˰���ۡ�*��1+˰�ʣ�
      case ConstVO.TAX_TYPE_OUT:
        dTaxRate = sQuoteRate == null ? voTransItem.getTaxrate()
            : new UFDouble(sQuoteRate);
        dTaxRate = dTaxRate.div(100);
        dQuotePriceWOTax = dQuotePriceWTax.div(new UFDouble(1).add(dTaxRate));
        break;
      // ����˰--- ����˰���ۡ�= ���Ǻ�˰���ۡ�
      case ConstVO.TAX_TYPE_NULL:
        dQuotePriceWOTax = dQuotePriceWTax;
    }
    if (!isNull(sQuoteRate)) {
      dPriceWTax = dQuotePriceWTax.div(Double.parseDouble(sQuoteRate));
      dPriceWOTax = dQuotePriceWOTax.div(Double.parseDouble(sQuoteRate));
    }

    freshPrice(iRow, new UFDouble[] {
        dQuotePriceWTax, dQuotePriceWOTax, dPriceWTax, dPriceWOTax,
        dTaxRate == null ? null : dTaxRate.multiply(100),
        voTransItem.getAddpricerate()
    });
  }

  /**
   * �����ߣ�lisb ���ܣ�������������ʧЧ���� ������ ���أ� ���⣺ ���ڣ�(2004-4-8 ���� 10:50)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void afterProducedateEdit(BillEditEvent e) throws Exception {

    int iRow = e.getRow();
    Object oTemp = getCellValue(iRow, "dproducedate");
    Object oInvID = getCellValue(iRow, "cinvbasid");
    Object oOutInvID = getCellValue(iRow, "coutinvid");

    if (isNull(oTemp) || isNull(oInvID) || isNull(oOutInvID)) {
      // ��ʧЧ����
      TOBillTool.clearRowValues(m_card, iRow, new String[] {
        "dvalidate"
      });
      return;

    }
    // ����ʧЧ����
    ParamVO param = InvInfo.getBatchMngInfo(oOutInvID.toString());

    if (param.iQualityDays != -1) {
      UFDate dtProductDate = new UFDate(oTemp.toString());
      UFDate dtValidateDate = dtProductDate.getDateAfter(param.iQualityDays);
      m_card.getBillModel().setValueAt(dtValidateDate, iRow, "dvalidate");
    }

  }

  /**
   * ����ҳǩ�ı�󴥷��ķ��� �������ڣ�(2003-8-7 9:47:24)
   * 
   * @param e
   *          nc.ui.pub.bill.BillTabbedPaneTabChangeEvent
   */
  public void afterTabChanged(nc.ui.pub.bill.BillTabbedPaneTabChangeEvent e) {
    nc.vo.pub.bill.BillTabVO btvo = e.getBtvo();
    if (btvo.getPos().intValue() == nc.ui.pub.bill.IBillItem.BODY) // ����ҳǩ
    {
      // ��������ҳǩ��tableCode
      String[] saCode = m_card.getBillData().getTableCodes(
          nc.ui.pub.bill.IBillItem.BODY);
      for (int i = 0; i < saCode.length; i++) {
        // ֹͣ���б���ҳǩ�ı༭���Ա㴥��afterEdit()����
        TableCellEditor editor = m_card.getBillTable(saCode[i]).getCellEditor();
        if (editor != null) {
          editor.stopCellEditing();
        }
      }
      
      
      //zhf add   ����  �˴�������ģ��
    if(btvo.getTabcode().equalsIgnoreCase(HgPubConst.ALLO_ORDER_TABLE_STORE)){
        BillVO bill = (BillVO)ui.m_Model.getCurVO();
        if(bill == null){
        	return;
        }
        m_card.getBillModel(HgPubConst.ALLO_ORDER_TABLE_STORE).setBodyDataVO(bill.getStockNumInfor());
        m_card.getBillModel(HgPubConst.ALLO_ORDER_TABLE_STORE).execLoadFormula();
//        if(ui.getCurPanel()==GenEditConst.CARD){
//        	get
//        }
    }
//      zhf end
      
    }

  }

  /**
   * �����ߣ�lisb ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-4-8 ���� 10:50) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void afterValidateEdit(BillEditEvent e) throws Exception {

    int iRow = e.getRow();
    String sTemp = getCellValue(iRow, "dvalidate");
    String sInvID = getCellValue(iRow, "cinvbasid");
    String sOutInvID = getCellValue(iRow, "coutinvid");

    if (isNull(sTemp) || isNull(sInvID) || isNull(sOutInvID)) {
      // ��ʧЧ����
      TOBillTool.clearRowValues(m_card, iRow, new String[] {
        "dproducedate"
      });
      return;

    }

    ParamVO param = InvInfo.getBatchMngInfo(sOutInvID);

    if (param.iQualityDays == -1) {
      // ��ձ���ʧЧ����,��������
      TOBillTool.clearRowValues(m_card, iRow, new String[] {
          "dvalidate", "cinwhid"
      });
      return;

    }

    // ������������
    UFDate validateDate = new UFDate(sTemp);
    UFDate dtProductDate = validateDate.getDateBefore(param.iQualityDays);
    m_card.getBillModel().setValueAt(dtProductDate, iRow, "dproducedate");

  }

  /**
   * ****************************************** ���ܣ� <|> �������ڣ�(2004-3-11
   * 20:58:45)
   * 
   * @param��
   * @return�� ******************************************
   */
  public boolean beforeAstNumEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {
    int iRow = e.getRow();
    Object oInvID = getCellValue(iRow, "cinvbasid");
    Object oOutInvID = getCellValue(iRow, "coutinvid");
    if (isNull(oInvID) || isNull(oOutInvID)) {
      return false;
    }

    UFBoolean bAstMng = InvInfo.getAstMngInfo(new String[] {
      oInvID.toString()
    })[0];

    String sKey = e.getKey();
    // ����������ʱ
    if (sKey.equalsIgnoreCase("nassistnum")) {
      if (bAstMng.booleanValue() && isNull(getCellValue(iRow, "castunitname"))) {
        sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
            "UPP40093010-000029")/*
                                   * @res "���еĴ��Ϊ�����������������븨��λ"
                                   */);
        return false;
      }
      if (bAstMng.booleanValue() && isNull(getCellValue(iRow, "nchangerate"))) {
        sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
            "UPP40093010-000028")/* @res "�����ʲ���Ϊ�ջ�0" */);
        return false;
      }
    }
    else if (sKey.equalsIgnoreCase("nquoteunitnum")) {
      if (isNull(getCellValue(iRow, "cquoteunitname"))) {
        sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
            "UPP40093010-000199")/*
                                   * @res "�������뱨�ۼ�����λ"
                                   */);
        return false;
      }
      if (bAstMng.booleanValue() && isNull(getCellValue(iRow, "nchangerate"))) {
        sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
            "UPP40093010-000028")/* @res "�����ʲ���Ϊ�ջ�0" */);
        return false;
      }
    }

    return bAstMng.booleanValue();

  }

  private String CheckChangeRate(BillEditEvent e) throws Exception {
    String flag = "N";
    int iRow = e.getRow();
    String sInvBasID = getCellValue(iRow, "cinvbasid");
    String sOutInvID = getCellValue(iRow, "coutinvid");
    if (isNull(sInvBasID) || isNull(sOutInvID)) {
      return flag;
    }

    // ȡ����λ
    String sPK_AstUnit = getCellValue(iRow, "castunitid");
    if (isNull(sPK_AstUnit)) {
      return flag;
    }

    String sMeasID = InvInfo.getPk_measdoc(sInvBasID);
    String sFixedflag = sMeasID.equals(sPK_AstUnit) ? "Y" : InvInfo
        .getInvConvert(sInvBasID, sPK_AstUnit)[1];
    // �̶�������
    if (isNull(sFixedflag)) {
      return flag;
    }
    if (sFixedflag.equals("N")) {
      return flag;
    }
    // �䶯������
    else if (sFixedflag.equals("Y")) {
      flag = "Y";
      return flag;
    }
    return flag;
  }

  /**
   * �����ߣ� ���ܣ����廻����¼��֮ǰ�Ĵ��� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public boolean beforeChangeRateEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    int iRow = e.getRow();
    String sInvBasID = getCellValue(iRow, "cinvbasid");
    String sOutInvID = getCellValue(iRow, "coutinvid");
    if (isNull(sInvBasID) || isNull(sOutInvID)) {
      return false;
    }

    // ȡ����λ
    String sPK_AstUnit = getCellValue(iRow, "castunitid");
    if (isNull(sPK_AstUnit)) {
      sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000155")/*
                                 * @res "û��¼�븨������λ���������뻻����"
                                 */);
      return false;
    }

    String sMeasID = InvInfo.getPk_measdoc(sInvBasID);
    String sFixedflag = sMeasID.equals(sPK_AstUnit) ? "Y" : InvInfo
        .getInvConvert(sInvBasID, sPK_AstUnit)[1];
    // �̶�������
    if (isNull(sFixedflag)) {
      sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
          "UPP40093010-000030")/* @res "��ȷ�ϸô���Ƿ����˻�����" */);
      return false;
    }
    if (sFixedflag.equals("Y")) {
      sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000082")/*
                                 * @res "��ǰ����������븨����֮���ǹ̶������ʣ������޸Ļ�����"
                                 */);
      return false;
    }
    // �䶯������
    else if (sFixedflag.equals("N")) {
      return true;
    }
    return true;
  }

  /**
   * ****************************************** ���ܣ� <|> �������ڣ�(2004-3-15
   * 21:57:54)
   * 
   * @param��
   * @return�� ******************************************
   */
  protected boolean beforeDeptEdit(BillEditEvent e) throws Exception {
    // �༭�ĵ�λ����
    String sKey = e.getKey().trim();
    // ������˾
    String sPk_corp = getHeadTailItemValue(getCorpRole(sKey) + "corpid");
    // �ɱ�ͷ�Ĺ�˾���˱���Ĳ��Ų���
    BillItem btCurrent = getBodyItem(sKey);
    // ����Ƿ��������ϼ���λ
    if (isNull(sPk_corp)) {
      String[] value = new String[] {
        nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0000404")
      /* @res "��˾" */};
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPP40093010-000116", null, value));/* res������ѡ���ϼ��Ĺ�˾ */
    }
    // ִ�й���
    CardPanelTool.filterRef(btCurrent, null, sPk_corp);
    return true;
  }

  /**
   * ****************************************** ���ܣ� <|> �������ڣ�(2004-3-15
   * 21:57:54)
   * 
   * @param��
   * @return�� ******************************************
   */
  protected boolean beforeSpacenameEdit(BillEditEvent e) throws Exception {

    int iRow = e.getRow();
    // �༭�ĵ�λ����
    String sKey = e.getKey();
    // ���뵥λ�Ľ�ɫ:cin,cout or ctakeout
    String sRole = getCorpRole(sKey);
    // �����ֿ�
    BillItem btCurrent = getBodyItem(sKey);
    String sWhid = getCellValue(iRow, sRole + "whid");
    // ����Ƿ��������ϼ���λ
    if (isNull(sWhid)) {
      String[] value = new String[] {
        nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0000153")
      /* @res "�ֿ�" */};
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPP40093010-000116", null, value));// "������ѡ���ϼ��Ĳֿ�"
    }

    Object[] oCsFlag = (Object[]) CacheTool.getCellValue("bd_stordoc",
        "pk_stordoc", "csflag", (getCellValue(iRow, sRole + "whid")).toString()
            .trim());
    // �ǻ�λ����ʱ,���ɱ༭
    if (!isNull(oCsFlag)
        && !(new UFBoolean(oCsFlag[0].toString()).booleanValue())) {
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPP40093010-000033")/*
                                             * @res "�˲ֿ�Ϊ�ǻ�λ����,��������"
                                             */);

    }
    String sCorpid = getHeadTailItemValue(sRole + "corpid");
    String sCondition = "and endflag = 'Y' and bd_cargdoc.pk_stordoc ='"
        + sWhid + "'";
    // ִ�й���
    CardPanelTool.filterRef(btCurrent, sCondition, sCorpid);
    return true;
  }

  /**
   * ****************************************** ���ܣ� <|> �������ڣ�(2004-3-15
   * 21:57:54)
   * 
   * @param��
   * @return�� ******************************************
   */
  protected boolean beforePsnnameEdit(BillEditEvent e) throws Exception {

    int iRow = e.getRow();
    // �༭�ĵ�λ����
    String sKey = e.getKey().trim();
    // ���뵥λ�Ľ�ɫ:cin,cout or ctakeout
    String sRole = getCorpRole(sKey);
    // �Ե�ǰ���ս��й��˵�����
    String sCondition = null;
    String sPk_corp = getHeadTailItemValue(sRole + "corpid");

    // ����Ƿ��������ϼ���λ
    if (isNull(sPk_corp)) {
      String[] value = new String[] {
        nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0000404")
      /* @res "��˾" */};
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPP40093010-000116", null, value));/*
                                                           * res "������ѡ���ϼ��Ĺ�˾
                                                           */
    }
    // ��������
    if (getCellValue(iRow, sRole + "deptid") != null
        && getCellValue(iRow, sRole + "deptid").toString().trim().length() > 0) {
      sCondition = " and bd_psndoc.pk_deptdoc = '"
          + getCellValue(iRow, sRole + "deptid") + "'";
    }
    BillItem btCurrent = getBodyItem(sKey);
    // ִ�й���
    CardPanelTool.filterRef(btCurrent, sCondition, sPk_corp);
    return true;
  }

  /**
   * ****************************************** ���ܣ� <|> �������ڣ�(2004-3-15
   * 21:57:54)
   * 
   * @param��
   * @return�� ******************************************
   */
  protected boolean beforeWarehouseEdit(int pos, String sKey) throws Exception {
    if (pos == IBillItem.BODY) {
      // �Բ�������Ĺ�˾�ĺϷ��Լ���
      String sInCorpID = getHeadTailItemValue("cincorpid");
      String sInCbID = getHeadTailItemValue("cincbid");
      String sOutCorpID = getHeadTailItemValue("coutcorpid");
      String sOutCbID = getHeadTailItemValue("coutcbid");
      String sTakeoutCorpID = getHeadTailItemValue("ctakeoutcorpid");
      String sTakeoutCbID = getHeadTailItemValue("ctakeoutcbid");
      BusinessCheck.tranCorpChk(sInCorpID, sInCbID, sOutCorpID, sOutCbID,
          sTakeoutCorpID, sTakeoutCbID, m_NodeInfo.getOrderTypeCode(m_card));

      // �����������������ֿ⣽�����ֿ�,�����ֿⲻ�ɱ༭
      if (!m_NodeInfo.getOrderTypeCode(m_card).equals(ConstVO.m_sBillSFDBDD)
          && sKey.equals("ctakeoutwhname")) {
        throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40093010", "UPP40093010-000215"));
        // �����������������ֿⲻ�ɱ༭!
      }
    }

    // ���뵥λ�Ľ�ɫ:cin,cout or ctakeout
    String sRole = getCorpRole(sKey);
    // ��������
    BillItem btTemp = m_card.getHeadItem("fallocflag");
    if (btTemp == null || btTemp.getValueObject() == null) {
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPP40093010-000031")/*
                                             * @res "��������Ϊ��!"
                                             */);
    }
    // ֱ�˵���
    if ((sKey.equals("cinwhname") || sKey.equals("cinwhid"))
        && Integer.parseInt(btTemp.getValueObject().toString()) == ConstVO.ITransferType_DIRECT) {
      sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
          "UPP40093010-000032")/*
                                 * @res "ֱ�˵��������µĵ���ֿⲻ������"
                                 */);
      return false;
    }
    BillItem btCurrent = CardPanelTool.getBillItemByPos(m_card, pos, sKey);
    String sCorpid = getHeadTailItemValue(sRole + "corpid");
    if (sCorpid == null || sCorpid.length() <= 0) {
      sCorpid = m_NodeInfo.getCorpID();
    }
    // ����iscapitalstor='N'��Ϊ�˹��˵��ʲ��ֲֿ�
    String sCondition = " and  isdirectstore='N' and gubflag = 'N'";
    String sCbid = getHeadTailItemValue(sRole + "cbid");
    // ִ�й���
    CardPanelTool.filterRef(btCurrent, sCondition, sCorpid);
    // �����֯�Ĺ�������ֻ��ͨ��setWherePart()������,������ͨ��addWherePart(),
    // ����๫˾���յ�ʱ���ʼ�ռ��������������,���������ݴ���
    if (sCbid != null && sCbid.length() > 0) {
      ((UIRefPane) btCurrent.getComponent()).getRefModel().setWherePart(
          " pk_calbody='" + sCbid + "'");
    }

    return true;
  }

  /**
   * ****************************************** ���ܣ� <|> �������ڣ�(2004-3-15
   * 21:57:54)
   * 
   * @param��
   * @return�� ******************************************
   */
  protected boolean beforeInvEdit(BillEditEvent e) throws Exception {

    // �������
    BillItem btInvCode = getBodyItem("cinvcode");
    UIRefPane refInv = (UIRefPane) btInvCode.getComponent();
    // refInv.setPK(null);
    int iRow = e.getRow();
    if (!isSelfMadeBill(iRow)) {
      sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009",
          "UPP40093009-000260")/* @res "�������ε��ݣ������޸Ĵ����" */);
      return false;
    }

    String sInCorpID = getHeadTailItemValue("cincorpid");
    String sInCbID = getHeadTailItemValue("cincbid");
    String sOutCorpID = getHeadTailItemValue("coutcorpid");
    String sOutCbID = getHeadTailItemValue("coutcbid");
    String sTakeoutCorpID = getHeadTailItemValue("ctakeoutcorpid");
    String sTakeoutCbID = getHeadTailItemValue("ctakeoutcbid");

    // �Բ�������Ĺ�˾�ĺϷ��Լ���
    BusinessCheck.tranCorpChk(sInCorpID, sInCbID, sOutCorpID, sOutCbID,
        sTakeoutCorpID, sTakeoutCbID, m_NodeInfo.getOrderTypeCode(m_card));

    if (isNull(sTakeoutCorpID)
        && !ConstVO.m_sBillSFDBDD.equals(m_NodeInfo.getOrderTypeCode(m_card))) {
      sTakeoutCorpID = sOutCorpID;
      sTakeoutCbID = sOutCbID;
    }
    // ���ÿ���������
    nc.ui.bd.ref.AbstractRefModel m = refInv.getRefModel();

    // ��������˾���ô��������Ȩ��,������趨���ջ�Ĭ�ϵ�ǰ��½��˾
    m.setPk_corp(sOutCorpID);

    // m.addWherePart(" and asset='N'");
    String[] o = new String[] {
        sTakeoutCorpID, sTakeoutCbID
    };
    m.setUserParameter(o);
    return true;
  }

  /**
   * �����ߣ� ���ܣ������ı��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  protected boolean beforeNumEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    int iRow = e.getRow();
    String sInvID = getCellValue(iRow, "cinvbasid");
    if (isNull(sInvID)) {
      return false;
    }
    // �Ƿ񸨼�������
    UFBoolean bAstMng = InvInfo.getAstMngInfo(new String[] {
      sInvID
    })[0];

    if (bAstMng.booleanValue() && (isNull(getCellValue(iRow, "castunitname")))) {
      sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000109")/*
                                 * @res "����������Ĵ�����������븨��λ"
                                 */);
      return false;
    }
    return true;
  }

  /**
   * �����ߣ� ���ܣ��������κ�¼��֮ǰ�Ĵ��� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  protected boolean beforeProduceDateEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    int iRow = e.getRow();
    String sInvID = getCellValue(iRow, "cinvbasid");
    // �����������id
    String sOutInvID = getCellValue(iRow, "coutinvid");
    String sBatch = getCellValue(iRow, "vbatch");
    // �Ƿ�������
    if (isNull(sInvID) || isNull(sOutInvID) || isNull(sBatch)) {
      return false;
    }

    ParamVO paramResult = InvInfo.getBatchMngInfo(sOutInvID);
    // ���ι��� AND ��ֵ�ڹ��������������ڿɱ༭
    return (paramResult.bLotMngFlag.booleanValue() && paramResult.bValidateMngFlag);

  }

  /**
   * ****************************************** ���ܣ� <|> �������ڣ�(2004-3-11
   * 20:58:45)
   * 
   * @param��
   * @return�� ******************************************
   */
  protected boolean beforeSendTypeEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    // �˻�ʱ���˷�ʽ���ɱ༭
    String sRetFlag = getCellValue(e.getRow(), "bretractflag");
    if (sRetFlag.equals("true")) {
      return false;
    }
    Object oOutCorpID = getHeadTailItemValue("coutcorpid");
    BillItem bt = getBodyItem(e.getKey());
    UIRefPane ref = (UIRefPane) bt.getComponent();
    ref.setWhereString("pk_corp = '0001'");

    if (!isNull(oOutCorpID)) {

      ref.setWhereString("pk_corp = '0001' or pk_corp = '" + oOutCorpID + "'");
    }

    return true;

  }

  /**
   * �����ߣ� ���ܣ��������κ�¼��֮ǰ�Ĵ��� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  protected boolean beforeValidateEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    int iRow = e.getRow();
    String sInvID = getCellValue(iRow, "cinvbasid");
    // �����������id
    String sOutInvID = getCellValue(iRow, "coutinvid");
    String sBatch = getCellValue(iRow, "vbatch");
    // �Ƿ�������
    if (isNull(sInvID) || isNull(sOutInvID) || isNull(sBatch)) {
      return false;
    }

    ParamVO paramResult = InvInfo.getBatchMngInfo(sOutInvID);

    // ���ι��� AND ��ֵ�ڹ�������ʧЧ���ڿɱ༭
    return (paramResult.bLotMngFlag.booleanValue() && paramResult.bValidateMngFlag);

  }

  /**
   *
   */
  private boolean checkNumInput(BillEditEvent e) {
    String sKey = e.getKey();
    int iRow = e.getRow();
    String sValue = getCellValue(iRow, sKey);
    if (!isNull(sValue))
      return new UFDouble(sValue).doubleValue() >= 0;
    return false;

  }

  /**
   * ��������:����ֱ�˲���Ϣ ����: ����ֵ: �쳣:
   */
  private void chkDirectStore() throws BusinessException {

    // ��������
    String sAllocType = getHeadTailItemValue("bdrictflag");
    if (sAllocType.equals("false")) {
      sAllocType = "1";
    }
    else {
      sAllocType = "0";
    }

    int iCount = m_card.getBillModel().getRowCount();
    if (Integer.parseInt(sAllocType) != ConstVO.ITransferType_DIRECT) {
      return;
    }
    // ֱ�˵���
    String sCInCbID = getHeadTailItemValue("cincbid");
    if (isNull(sCInCbID)) {
      // ����Ĭ��ֵ
      BillItem bt = m_card.getHeadItem("fallocname");
      if (bt != null) {
        bt.setValue(nc.ui.ml.NCLangRes.getInstance().getStrByID("topub",
            ConstVO.ITransferTypeName_INSTORESID));
      }
      bt = m_card.getHeadItem("fallocflag");
      if (bt != null) {
        bt.setValue(new Integer(ConstVO.ITransferType_INSTORE));
      }

      TOBillTool.clearAllRowValues(m_card, new String[] {
          "cinwhname", "cinwhid"
      });

      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPP40093010-000043")/*
                                             * @res "������ѡ�������֯"
                                             */);
    }

    Object[][] oStoreValue = CacheTool.getMultiColValue2("bd_stordoc",
        "pk_calbody", "isdirectstore", new String[] {
            "pk_stordoc", "storname"
        }, new String[] {
          sCInCbID
        }, new String[] {
          "Y"
        });
    if (isNull(oStoreValue) || isNull(oStoreValue[0][0])) {
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPP40093010-000044")/*
                                             * @res "��ȷ���Ƿ�����ֱ�˲ֿ�"
                                             */);
    }

    // ���ñ����еĵ���ֿ�
    TOBillTool.clearAllRowValues(m_card, new String[] {
        "cinspacename", "cinspaceid", "pk_arrivearea", "arriveareaname",
        "pk_areacl", "areaclname", "creceieveid", "creceievename"
    });
    for (int i = 0; i < iCount; i++) {
      m_card.getBillModel().setValueAt(oStoreValue[0][0], i, "cinwhid");
      m_card.getBillModel().setValueAt(oStoreValue[0][1], i, "cinwhname");
    }
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2004-5-8 10:10:53)
   * 
   * @return nc.ui.to.pubtransfer.BusinessCtrl
   */
  private BusinessCheck getBizCheck() {
    if (m_bizCheck == null) {
      m_bizCheck = new BusinessCheck();
    }
    return m_bizCheck;
  }

  /**
   * ��ȡ���빫˾�Ľ�ɫ �������ڣ�(2001-10-24 16:33:58)
   * 
   * @return ��������˾:"ctakeout";
   * @param ��sColumnName="ctakeoutwhname";
   */
  private String getCorpRole(String sColumnName) {
    if (sColumnName.indexOf("cin") >= 0) {
      return "cin";
    }
    if (sColumnName.indexOf("cout") >= 0) {
      return "cout";
    }
    if (sColumnName.indexOf("ctakeout") >= 0) {
      return "ctakeout";
    }

    return null;
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2004-5-9 19:09:18)
   * 
   * @return java.lang.String
   */
  private String getTaxIncludeFlag() throws Exception {

    return InvInfo.getTaxIncludeFlag(getHeadTailItemValue("coutcorpid"));
  }

  private String getFwastpartName(Integer iFlag) {
    if (iFlag == null)
      return null;
    switch (iFlag.intValue()) {
      case ITransrelaConst.ONWAY_OWER_FLAF_OUT:
        return nc.ui.ml.NCLangRes.getInstance().getStrByID("topub",
            "UPP-000139"/*
                         * @res "�������"
                         */);
      case ITransrelaConst.ONWAY_OWER_FLAF_IN:
        return nc.ui.ml.NCLangRes.getInstance().getStrByID("topub",
            "UPP-000140"/*
                         * @res "����뷽"
                         */);

    }
    return null;
  }

  /**
   * ��ȡ��������Ĺ�˾id �������ڣ�(2004-5-9 19:09:18)
   * 
   * @return
   */
  private HashMap getTransCorpID() {

    HashMap hmID = new HashMap();
    // ����corp ID
    String sInCorpID = null;
    String sInCbID = null;
    String sInWhID = null;

    String sOutCorpID = null;
    String sOutCbID = null;
    String sOutWhID = null;

    // ����corp ID
    String sTakeOutCorpID = null;
    String sTakeOutCbID = null;
    String sTakeOutWhID = null;

    sInWhID = getHeadTailItemValue("cinwhid");
    sOutWhID = getHeadTailItemValue("coutwhid");
    sTakeOutWhID = getHeadTailItemValue("ctakeoutwhid");

    // ������֯
    sInCbID = getHeadTailItemValue("cincbid");
    // ������֯
    sOutCbID = getHeadTailItemValue("coutcbid");
    // ������֯
    sTakeOutCbID = getHeadTailItemValue("ctakeoutcbid");
    // ���빫˾
    sInCorpID = getHeadTailItemValue("cincorpid");
    // ������˾
    sOutCorpID = getHeadTailItemValue("coutcorpid");
    // ������˾
    sTakeOutCorpID = getHeadTailItemValue("ctakeoutcorpid");

    // ��������Ϣ���Ϊ�գ�Ĭ��Ϊ��������Ϣ
    if (isNull(sTakeOutCorpID)) {
      sTakeOutCorpID = sOutCorpID;
      sTakeOutCbID = sOutCbID;
      sTakeOutWhID = sOutWhID;
    }
    if (sInCorpID != null) {
      hmID.put("cincorpid", sInCorpID);
    }
    if (sInCbID != null) {
      hmID.put("cincbid", sInCbID);
    }
    if (sInWhID != null) {
      hmID.put("cinwhid", sInWhID);
    }
    if (sOutCorpID != null) {
      hmID.put("coutcorpid", sOutCorpID);
    }
    if (sOutCbID != null) {
      hmID.put("coutcbid", sOutCbID);
    }
    if (sInWhID != null) {
      hmID.put("coutwhid", sOutWhID);
    }
    if (sTakeOutCorpID != null) {
      hmID.put("ctakeoutcorpid", sTakeOutCorpID);
    }
    if (sTakeOutCbID != null) {
      hmID.put("ctakeoutcbid", sTakeOutCbID);
    }
    if (sInWhID != null) {
      hmID.put("ctakeoutwhid", sTakeOutWhID);
    }

    return hmID;
  }

  private void clearWastpart(int[] iaRow) {
    for (int i = 0; i < iaRow.length; i++) {
      TOBillTool.clearRowValues(m_card, iaRow[i], new String[] {
          "crelationid", "crelation_bid"
      });
      //����յ���������;������5E��5I��ѯ������ϵ��ȡ�ֹ�¼����;����
      TOBillTool.clearHeadTailValue(m_card, new String[] {
          "fotwastpartflag",
          "fotwastpartname"
      });
    }
  }

  /**
   * �˴����뷽��˵��:��ȡ������ϵ �������ڣ�(2004-5-9 19:00:29)
   * 
   * @return nc.vo.bd.b204.TransrelaItemVO
   */
  private QryTransrelaVO[] getTransItem(int[] iaRow) throws BusinessException {
    if (isInSameCorp()) {
      clearWastpart(iaRow);
      return null;
    }

    HashMap hmCorp = getTransCorpID();
    String sOutCorpid = (String) hmCorp.get("coutcorpid");
    String sTakeOutCorpid = (String) hmCorp.get("ctakeoutcorpid");
    boolean bSFDB = !sOutCorpid.equals(sTakeOutCorpid);
    ArrayList[] alParam = null;
    if (bSFDB) {
      alParam = new ArrayList[2];
      // ���ҵ������뷽�ĵ�����ϵ����
      alParam[0] = getParam4QryTransItem(iaRow, false);
      // ���ҵ������뷽�ĵ�����ϵ��ͷ
      // alParam[1] = getParam4QryTransHead(false);
      // ���ҳ����������ı��������ϵ
      alParam[1] = getParam4QryTransItem(iaRow, true);
      // ���ҳ����������ı�ͷ������ϵ
      // alParam[3] = getParam4QryTransHead(true);
    }
    else {
      alParam = new ArrayList[1];
      // ���ҵ������뷽�ĵ�����ϵ����
      alParam[0] = getParam4QryTransItem(iaRow, false);
      // ���ҵ������뷽�ĵ�����ϵ��ͷ
      // alParam[1] = getParam4QryTransHead(false);
    }
    Hashtable[] htRet = null;
    try {
      htRet = InvInfo.getTransRela(alParam);
    }
    catch (Exception ex) {
      if (ex != null)
        sendErrMsg(ex.getMessage());
    }

    HashMap<String, Object> hmValue = new HashMap<String, Object>();
    QryTransrelaVO[] voaTransItem = new QryTransrelaVO[iaRow.length];
    QryTransrelaVO[] voaTransItem2 = new QryTransrelaVO[iaRow.length];

    for (int i = 0; i < iaRow.length; i++) {
      voaTransItem[i] = (QryTransrelaVO) htRet[0].get(new Integer(i));
      if (bSFDB) {
        voaTransItem2[i] = (QryTransrelaVO) htRet[1].get(new Integer(i));
      }
      if (voaTransItem[i] == null) {
        TOBillTool.clearRowValues(m_card, iaRow[i], new String[] {
            "crelationid", "crelation_bid"
        });
        TOBillTool.clearHeadTailValue(m_card, new String[] {
            "foiwastpartflag", "fotwastpartflag", "foiwastpartname",
            "fotwastpartname"
        });
        throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40093009", "UPP40093009-000047", null, new String[] {
              getCellValue(iaRow[i], "crowno")
            })/*
               * @res "��i��δ�ҵ�������ϵ����"
               */);
      }
      hmValue.put("crelationid", voaTransItem[i].getPk_transrela());// ������ϵ����pk
      hmValue.put("crelation_bid", voaTransItem[i].getPk_transrela_b());// ������ϵ�ӱ�pk

      TOBillTool.setRowValue(m_card, hmValue, iaRow[i]);
    }
    // ����ͷ����;�����ֶθ�ֵ(��Ϊ��;�����������ڵ�����ϵ��ͷ��ȡ��һ���Ϳ�����)
    Integer iFoiflag = voaTransItem[0].getOnwayownershipflag();
    Integer iFotflag = isNull(voaTransItem2[0]) ? null : voaTransItem2[0]
        .getOnwayownershipflag();
    m_card.setHeadItem("foiwastpartflag", iFoiflag);
    m_card.setHeadItem("fotwastpartflag", iFotflag);
    m_card.setHeadItem("foiwastpartname", getFwastpartName(iFoiflag));
    m_card.setHeadItem("fotwastpartname", getFwastpartName(iFotflag));
    return voaTransItem;
  }

  private ArrayList getParam4QryTransItem(int[] iaRow, boolean bSFDB)
      throws BusinessException {
    HashMap hmCorp = getTransCorpID();
    // ��˾id
    String sInCorpID = hmCorp.get("cincorpid").toString();
    String sInCbID = hmCorp.get("cincbid").toString();
    String sInWhID = hmCorp.get("cinwhid").toString();
    String sOutCorpID = hmCorp.get("coutcorpid").toString();
    String sOutCbID = hmCorp.get("coutcbid").toString();
    String sOutWhID = hmCorp.get("coutwhid").toString();
    String sTakeOutCorpID = hmCorp.get("ctakeoutcorpid").toString();
    String sTakeOutCbID = hmCorp.get("ctakeoutcbid").toString();
    String sTakeoutWhID = hmCorp.get("ctakeoutwhid").toString();

    // ��������
    String sFallocFlag = getHeadTailItemValue("bdrictflag");
    if (sFallocFlag.equals("false")) {
      sFallocFlag = "1";
    }
    else {
      sFallocFlag = "0";
    }
    Integer iFallocflag = Integer.valueOf(sFallocFlag);

    // ҵ������
    String cbiztypeid = getHeadTailItemValue("cbiztypeid");

    if (isNull(sInCorpID) || isNull(sOutCorpID) || isNull(sInCbID)
        || isNull(sOutCbID) || isNull(iFallocflag)) {
      return null;
    }
    String[] saInvBasID = new String[iaRow.length];
    String[] saOutInvCl = new String[iaRow.length];
    for (int i = 0; i < iaRow.length; i++) {
      saInvBasID[i] = getCellValue(iaRow[i], "cinvbasid");
    }
    saOutInvCl = InvInfo.getInvClID(saInvBasID);

    ArrayList<ArrayList> alBodyParams = new ArrayList<ArrayList>(iaRow.length);
    ArrayList<Object> alBodyParam = new ArrayList<Object>(13);

    if (bSFDB) {
      for (int i = 0; i < iaRow.length; i++) {
        // coutcorpid,ctakeoutcorpid,coutcbid,ctakeoutcbid,null,null,invclin,invclout,invin,invout,falloc,id
        alBodyParam.clear();
        alBodyParam.add(sOutCorpID);// 0
        alBodyParam.add(sTakeOutCorpID);// 1
        alBodyParam.add(sOutCbID);// 2
        alBodyParam.add(sTakeOutCbID);// 3
        alBodyParam.add(sOutWhID);// 4
        alBodyParam.add(sTakeoutWhID);// 5
        alBodyParam.add(saOutInvCl[i]);// 6
        alBodyParam.add(saOutInvCl[i]);// 7
        alBodyParam.add(saInvBasID[i]);// 8
        alBodyParam.add(saInvBasID[i]);// 9
        alBodyParam.add(iFallocflag);// 10
        alBodyParam.add(new Integer(i));// 11
        alBodyParam.add(cbiztypeid);// 12
        alBodyParams.add((ArrayList) alBodyParam.clone());
      }

    }
    else {
      for (int i = 0; i < iaRow.length; i++) {
        // cincorpid,coutcorpid,cincbid,coutcbid,cinwhid,coutwhid,invclin,invclout,invin,invout,falloc,id
        alBodyParam.clear();
        alBodyParam.add(sInCorpID);// 0
        alBodyParam.add(sOutCorpID);// 1
        alBodyParam.add(sInCbID);// 2
        alBodyParam.add(sOutCbID);// 3
        // if (sInCbID.equals(sOutCbID)) {
        // alBodyParam.add(getCellValue(iaRow[i], "cinwhid"));// 4
        // alBodyParam.add(getCellValue(iaRow[i], "coutwhid"));// 5
        // }
        // else {
        alBodyParam.add(sInWhID);// 4
        alBodyParam.add(sOutWhID);// 5
        // }
        alBodyParam.add(saOutInvCl[i]);// 6
        alBodyParam.add(saOutInvCl[i]);// 7
        alBodyParam.add(saInvBasID[i]);// 8
        alBodyParam.add(saInvBasID[i]);// 9
        alBodyParam.add(iFallocflag);// 10
        alBodyParam.add(new Integer(i));// 11
        alBodyParam.add(cbiztypeid);// 12
        alBodyParams.add((ArrayList) alBodyParam.clone());
      }
    }
    return alBodyParams;
  }

  private ArrayList getParam4QryTransHead(boolean bSFDB) {
    HashMap hmCorp = getTransCorpID();
    // ��˾id
    String sInCorpID = hmCorp.get("cincorpid").toString();
    String sInCbID = hmCorp.get("cincbid").toString();
    String sOutCorpID = hmCorp.get("coutcorpid").toString();
    String sOutCbID = hmCorp.get("coutcbid").toString();
    String sTakeOutCorpID = hmCorp.get("ctakeoutcorpid").toString();
    String sTakeOutCbID = hmCorp.get("ctakeoutcbid").toString();

    // ��������
    String sAllocType = getHeadTailItemValue("bdrictflag");
    if (sAllocType.equals("false")) {
      sAllocType = "1";
    }
    else {
      sAllocType = "0";
    }
    Integer iFallocflag = Integer.valueOf(sAllocType);

    if (isNull(sInCorpID) || isNull(sOutCorpID) || isNull(sInCbID)
        || isNull(sOutCbID) || isNull(iFallocflag)) {
      return null;
    }

    ArrayList<Object> alHeadParam = new ArrayList<Object>(12);
    // coutcorpid,ctakeoutcorpid,coutcbid,ctakeoutcbid,null,null,invclin,invclout,invin,invout,falloc,id
    if (bSFDB) {
      alHeadParam.add(sOutCorpID);// 0
      alHeadParam.add(sTakeOutCorpID);// 1
      alHeadParam.add(sOutCbID);// 2
      alHeadParam.add(sTakeOutCbID);// 3
      alHeadParam.add(null);// 4
      alHeadParam.add(null);// 5
      alHeadParam.add(null);// 6
      alHeadParam.add(null);// 7
      alHeadParam.add(null);// 8
      alHeadParam.add(null);// 9
      alHeadParam.add(iFallocflag);// 10
      alHeadParam.add(new Integer(0));// 11
    }
    else {
      alHeadParam.add(sInCorpID);// 0
      alHeadParam.add(sOutCorpID);// 1
      alHeadParam.add(sInCbID);// 2
      alHeadParam.add(sOutCbID);// 3
      alHeadParam.add(null);// 4
      alHeadParam.add(null);// 5
      alHeadParam.add(null);// 6
      alHeadParam.add(null);// 7
      alHeadParam.add(null);// 8
      alHeadParam.add(null);// 9
      alHeadParam.add(iFallocflag);// 10
      alHeadParam.add(new Integer(0));// 11
    }
    ArrayList alRet = new ArrayList(1);
    alRet.add(alHeadParam);
    return alRet;
  }

  /**
   * ****************************************** ���ܣ� <|>��ѯ������˾�Ŀ��������ִ���
   * �������ڣ�(2004-3-11 20:58:45)
   * 
   * @param��
   * @return�� ******************************************
   */
  private void showInvAmount(int iRow) {
    try {
      String sCorpID = getHeadTailItemValue("ctakeoutcorpid");
      String sCalBodyID = getHeadTailItemValue("ctakeoutcbid");
      String sOutInvID = getCellValue(iRow, "ctakeoutinvid");
      String sWhId = getCellValue(iRow, "ctakeoutwhid");
      String sPlanOutDate = getCellValue(iRow, "dplanoutdate");

      if (sCorpID != null && sCorpID.length() > 0 && sCalBodyID != null
          && sCalBodyID.length() > 0 && sOutInvID != null
          && sOutInvID.length() > 0 && sPlanOutDate != null
          && sPlanOutDate.length() > 0) {
        UFDouble[] data = InvInfo.getInvAmount(sCorpID, sCalBodyID, sWhId,
            sOutInvID, sPlanOutDate, m_NodeInfo.getDataPrecision()[0]
                .intValue());
        m_card.setTailItem("ATP", data[0]);
        m_card.setTailItem("curamount", data[1]);
      }
    }
    catch (Exception e) {
      GenMsgCtrl.handleException(e);
    }
  }


  /**
   * �����ߣ� ���ܣ����廻����¼��֮ǰ�Ĵ��� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  protected boolean beforeTaxRateEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {
    if (!isCanUpdatePrice(e.getRow())) {
      return false;
    }
    // ������ϵ
    QryTransrelaVO[] voTransItems = getTransItem(new int[] {
      e.getRow()
    });
    if (isNull(voTransItems)) {
      //5I��5E��ƥ�������ϵ����ʱҪ��˰�ʿɱ༭
      return true;
    }
    QryTransrelaVO voTransItem = voTransItems[0];
    Integer iTemp = voTransItem.getTaxtypeflag();
    boolean bEdit = false;

    if (iTemp.intValue() == ConstVO.TAX_TYPE_NULL)
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", ConstVO201.sINF_TRAN_TAX_NULL_SID));

    else {
      // ˰�ʿɱ༭
      bEdit = true;
    }

    return bEdit;

  }

  protected boolean beforePriceEdit(BillEditEvent e) throws Exception {
    if (!isCanUpdatePrice(e.getRow())) {
      return false;
    }
    // ������ۼ�����λ������ʾ,�������޸�����λ����
    if (e.getKey().indexOf("price") >= 0) {
      BillItem bt = getBodyItem("norgqttaxnetprc");
      if (bt != null && bt.isShow()) {
        return false;
      }
    }
    return getBodyItem(e.getKey()).isEdit();
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2004-5-18 11:10:39)
   */
  private void freshPrice(int iRow, UFDouble[] daPrices) {

    HashMap hmValue = new HashMap(10);
    ;
    String sNum = getCellValue(iRow, "nnum");

    if (!isNull(daPrices)) {
      Integer[] iaDataPrecision = null;
      try {
        iaDataPrecision = new TOEnvironment()
            .getDataPrecision(getHeadTailItemValue("coutcorpid"));
      }
      catch (Exception e) {
        sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009",
            "UPP40093009-000261")/* @res "δ�ܻ�����ݾ��ȣ�" */);
        return;
      }
      for (int i = 0; i < daPrices.length; i++) {
        if (daPrices[i] != null)
          daPrices[i].setScale(iaDataPrecision[1], UFDouble.ROUND_HALF_UP);
      }
      // ���۵�λ��˰����
      hmValue.put("norgqttaxnetprc", isNull(daPrices[0]) ? "" : daPrices[0]
          .toString());
      // ���۵�λ�Ǻ�˰����
      hmValue.put("norgqtnetprc", isNull(daPrices[1]) ? "" : daPrices[1]
          .toString());
      // ����λ��˰����
      hmValue.put("nprice", isNull(daPrices[2]) ? "" : daPrices[2].toString());
      // ����λ�Ǻ�˰����
      hmValue.put("nnotaxprice", isNull(daPrices[3]) ? "" : daPrices[3]
          .toString());
      // ˰��
      hmValue
          .put("ntaxrate", isNull(daPrices[4]) ? "" : daPrices[4].toString());
      // ��˰���
      hmValue.put("nmny", isNull(daPrices[2]) || isNull(sNum) ? ""
          : daPrices[2].multiply(new UFDouble(sNum)).toString());
      // �Ǻ�˰���
      hmValue.put("nnotaxmny", isNull(daPrices[3]) || isNull(sNum) ? ""
          : daPrices[3].multiply(new UFDouble(sNum)).toString());
      // �Ӽ���
      hmValue.put("naddpricerate", isNull(daPrices[5]) ? "" : daPrices[5]
          .toString());
      // ѯ�ۺ�˰����
      // hmValue.put("naskprice",
      // isNull(daPrices) || isNull(daPrices[2]) ? "" : daPrices[2]
      // .toString());
      // ѯ�۷Ǻ�˰����
      // hmValue.put("nasknotaxprice", isNull(daPrices)
      // || isNull(daPrices[3]) ? "" : daPrices[3].toString());
      TOBillTool.setRowValue(m_card, hmValue, iRow);

    }
    else {
      // ��ռ۸�
      TOBillTool.clearRowValues(m_card, iRow, new String[] {
          "norgqttaxnetprc", "norgqtnetprc", "nprice", "nnotaxprice",
          "ntaxrate", "nmny", "nnotaxmny", "naddpricerate"
      });
    }
  }

  /*
   * ���ָ���еļ۸���Ϣ
   */
  private void clearPrice(int[] iaRow) {
    final String[] saPriceKey = new String[] {
        "norgqttaxnetprc", "norgqtnetprc", "nprice", "nnotaxprice", "ntaxrate",
        "nmny", "nnotaxmny", "naddpricerate", "naskprice", "nasknotaxprice"
    };
    if (iaRow == null || iaRow.length <= 0) {
      TOBillTool.clearAllRowValues(m_card, saPriceKey);
    }
    else {
      for (int i = 0; i < iaRow.length; i++)
        TOBillTool.clearRowValues(m_card, iaRow[i], saPriceKey);
    }
  }

  /**
   * ��������������ȫ��������ѯ�۷��������ô˷����϶���ѯ�ۡ����������Ҫѯ�۵ı����У������setPrice()��setPrice(int[]
   * iaRow) <b>����˵��</b>
   * 
   * @throws BusinessException
   * @author sunwei
   * @time 2008-11-28 ����09:39:48
   */
  public void getPrice() throws BusinessException {
    int iLen = m_card.getRowCount();
    if (iLen <= 0) {
      return;
    }
    int[] iaRow = new int[iLen];
    for (int i = 0; i < iLen; i++) {
      iaRow[i] = i;
    }
    // ȡ��
    getPrice(iaRow);
  }

  /**
   * ����������������ͷѯ�������Ƿ����
   * �жϱ�ͷ�Ĺ�˾����֯�������Ƿ�ǿ�
   * @return
   * @author sunwei
   * @time 2009-1-13 ����07:00:38
   */
  private boolean isAskPriceHeadConExist() {
    if(TOBillTool.isNull(m_card.getHeadItem("cincorpid").getValueObject())||
        TOBillTool.isNull(m_card.getHeadItem("coutcorpid").getValueObject())||
        //TOBillTool.isNull(m_card.getHeadItem("ctakeoutcorpid").getValueObject())||
        TOBillTool.isNull(m_card.getHeadItem("cincbid").getValueObject())||
        TOBillTool.isNull(m_card.getHeadItem("coutcbid").getValueObject())||
        //TOBillTool.isNull(m_card.getHeadItem("ctakeoutcbid").getValueObject())||
        TOBillTool.isNull(m_card.getHeadItem("coutcurrtype").getValueObject())){
      return false;
    }
    return true;
  }
  
  /**
   * ****************************************** ���ܣ� <|> ���ô˷����϶���ѯ��
   * ͨ������µ���setPrice(int[] iaRow)�������ȹ��˳���Ҫѯ�۵���, �������ڣ�(2004-3-15 21:57:54)
   * 
   * @param��
   * @return�� ******************************************
   */
  protected void getPrice(int[] iaRow) throws BusinessException {
    //5E��5I������ѯ��
    if (isInSameCorp()) {
      return;
    }
    //���ѯ��������������������ѯ��
    if(!isAskPriceHeadConExist()){
      return;
    }

    BillVO vo4Query = getQueryPriceVO(iaRow);
    if(vo4Query.getItemVOs().length==0){
      return;
    }
    ArrayList alResult = null;
    try {
      IOrderAskPrice service = (IOrderAskPrice)nc.bs.framework.common.NCLocator.getInstance().lookup(IOrderAskPrice.class.getName()); 
      alResult = service.getTransRelaAndPrices(vo4Query);
      vo4Query = (BillVO) alResult.get(0);
    }
    catch (BusinessException e) {
      if (alResult != null) {
        String sHintMsg = (String) alResult.get(1);
        if (sHintMsg != null && sHintMsg.length() > 0) {
          throw new BusinessException(sHintMsg);
        }
      }
      else {
        throw e;
      }
    }

    String[] keys = new String[] {
        "norgqttaxnetprc", "norgqtnetprc", "nprice", "nnotaxprice", "ntaxrate",
        "nmny", "nnotaxmny", "naddpricerate", "naskprice", "nasknotaxprice","ntaxmny",
        "crelationid", "crelation_bid"
    };
    for (int i = 0, len = iaRow.length, k = 0; i < len; i++) {
      // ������Ϊ��Ʒ����ȡ��
      String sInvbasid = getCellValue(iaRow[i], "cinvbasid");
      String sFlargess = getCellValue(iaRow[i], "flargess");
      if (isNull(sInvbasid) || Boolean.valueOf(sFlargess).booleanValue()) {
        k++;
        continue;
      }

      for (int j = 0; j < keys.length; j++) {
    	  // modify by zhw 2011-04-11  ������ڼƻ���Ŀ���ⷽʽΪ��ͬ��  �Ͳ������ü۸�
    	  String project = PuPubVO.getString_TrimZeroLenAsNull(getCellValue(iaRow[i], "vbdef12"));
    	  if("nnotaxprice".equalsIgnoreCase(keys[j])){
      	    if(project == null){
      	    	m_card.getBillModel().setValueAt(vo4Query.getItemVOs()[i - k].getAttributeValue(keys[j]), iaRow[i], keys[j]);
      	    }
    	  }else if("nnotaxmny".equalsIgnoreCase(keys[j])){
    		  if(project == null){
        	    	m_card.getBillModel().setValueAt(vo4Query.getItemVOs()[i - k].getAttributeValue(keys[j]), iaRow[i], keys[j]);
        	    }else{
        	    	 UFDouble price = PuPubVO.getUFDouble_NullAsZero(getCellValue(iaRow[i], "nnotaxprice"));
        	    	 UFDouble num = PuPubVO.getUFDouble_NullAsZero(getCellValue(iaRow[i], "nnum"));
        	    	 m_card.getBillModel().setValueAt(price.multiply(num), iaRow[i],"nnotaxmny");
        	    }
    	  }else{
    		  m_card.getBillModel().setValueAt(vo4Query.getItemVOs()[i - k].getAttributeValue(keys[j]), iaRow[i], keys[j]);
    	  }
      }
      
      //�Ҽ�ѯ�۲��ᴥ����״̬�仯���ֶ�������״̬Ϊ���޸ġ�.
      if(BillModel.NORMAL==m_card.getBillModel().getRowState(iaRow[i])){//����������״̬��NEW ��ΪMODIFICATION
        m_card.getBillModel().setRowState(iaRow[i], BillModel.MODIFICATION);
      }
    }
    String[] keys_h = new String[]{"foiwastpartflag", "fotwastpartflag", "foiwastpartname","fotwastpartname"};
    for(int i=0;i<keys_h.length;i++){
      m_card.setHeadItem(keys_h[i], vo4Query.getHeaderVO().getAttributeValue(keys_h[i]));
    }
    
    String sHintMsg = (String) alResult.get(1);
    if (sHintMsg != null && sHintMsg.length() > 0) {
      // ���������ָ���еļ۸���Ϣ
      final String[] saPriceKey = new String[] {
          "norgqttaxnetprc", "norgqtnetprc", "nprice", "nnotaxprice", "nmny",
          "nnotaxmny", "naskprice", "nasknotaxprice"
      };

      ArrayList errorRowNum = (ArrayList) alResult.get(2);
      if (errorRowNum != null && errorRowNum.size() > 0) {
        for (int i = 0; i < iaRow.length; i++) {
          if (errorRowNum.contains(m_card.getBillModel().getValueAt(iaRow[i],
              "crowno"))) {
            TOBillTool.clearRowValues(m_card, iaRow[i], saPriceKey);
          }
        }
      }

      throw new BusinessException(sHintMsg);
    }
  }

  private BillVO getQueryPriceVO(int[] iaRow) throws BusinessException {
    String sInvBasID = "";
    // �Ƿ���Ʒ��־
    String sFlargess = "";
    ArrayList alItem4Query = new ArrayList();

    boolean bAllFlargess = true;
    for (int i = 0; i < iaRow.length; i++) {
      //
      sInvBasID = getCellValue(iaRow[i], "cinvbasid");
      sFlargess = getCellValue(iaRow[i], "flargess");
      if(!Boolean.valueOf(sFlargess).booleanValue()){
        bAllFlargess =false;
      }
      // û�д����������Ʒ�д�����μ�ѯ��
      if (isNull(sInvBasID)
          || (!isNull(sFlargess) && Boolean.valueOf(sFlargess).booleanValue())) {
        continue;
      }
      
      //V56���ۼ۸���Ҫ������VO��Ϊ����
      BillItemVO itemTemp = (BillItemVO) m_card.getBillModel().getBodyValueRowVO(iaRow[i],NodeInfo.NAME_BODYVO);

      alItem4Query.add(itemTemp);
    }
    BillVO vo4Query = new BillVO();
    
    //V56���ۼ۸���Ҫ������VO��Ϊ����
    BillHeaderVO head4Query = (BillHeaderVO) m_card.getBillData().getHeaderValueVO(NodeInfo.NAME_HEADVO);

    BillItemVO[] items4Query = new BillItemVO[alItem4Query.size()];
    items4Query = (BillItemVO[]) alItem4Query.toArray(items4Query);
    vo4Query.setParentVO(head4Query);
    vo4Query.setChildrenVO(items4Query);
    
    if(bAllFlargess&&items4Query.length==0&&ui.getIsComeFromOther()){
      //���б����ж�Ϊ��Ʒ�У���Ϊ5A��������ʱ��Ҫѯ������ϵ
      getTransItem(iaRow);
    }
    return vo4Query;
  }

  /**
   * ****************************************** ���ܣ� <|> �������ڣ�(2004-3-18
   * 15:04:02)
   * 
   * @param��
   * @return�� ******************************************
   */
  private void afterCalbodyEidt(String sKey) throws Exception {
    // ��������֯�µĲֿ⣨��ͷ�ͱ��壩����λ��Ϣ
    String sRole = getCorpRole(sKey);
    TOBillTool.clearHeadTailValue(m_card, new String[] {
        sRole + "whname", sRole + "whid"
    });
    TOBillTool.clearAllRowValues(m_card, new String[] {
        sRole + "whname", sRole + "whid", sRole + "spacename",
        sRole + "spaceid"
    });
    // ���������֯������˾
    String sPk = (String) m_card.getHeadItem(sKey).getValueObject();
    Object[] oValue = null;
    String oldcorp = null;
    if (sPk != null && sPk.length() > 0) {
       oValue = (Object[]) nc.ui.scm.pub.cache.CacheTool.getCellValue(
          "bd_calbody", "pk_calbody", "pk_corp", sPk);
    }
    
    oldcorp = (String)m_card.getHeadItem(sRole + "corpid").getValueObject();
    // ��˾ȷʵ�仯�˲ŵ���afterCorpChanged
    if (oValue != null && !isSameString(oldcorp, (String) oValue[0])) {
      
      m_card.setHeadItem(sRole + "corpid", (String) oValue[0]);
      // �ı乫˾����ı仯��������幫˾�ͱ�λ��
      afterCorpChanged(sRole + "corpid");
    }
    afterCalbodyChanged(sKey);
    
    //�ж���;�����Ƿ���Ա༭
    setEditableOfWastpart();
  }
  
  private boolean isSameString(String a, String b) {
    if (a == null && b == null) {
      return true;
    }
    if (a != null && a.equals(b)) {
      return true;
    }
    else {
      return false;
    }
  }
  

  /**
   * ��������������������;�����Ƿ���Ա༭����Ϊ5E��5I��ѯ������ϵ�����Ե���������;�������Ա༭
   * <b>����˵��</b>
   * @author sunwei
   * @time 2009-3-16 ����10:01:07
   */
  private void setEditableOfWastpart() {
    BillItem bt = null;
    bt = m_card.getHeadItem("foiwastpartflag");
    String typecode = "";
    try{
      typecode = m_NodeInfo.getOrderTypeCode(m_card);
    }
    catch(BusinessException ex){
      //�޷��жϵ�������ʱ����;�������ɱ༭
    }
    if(typecode.equals("5E")||typecode.equals("5I"))
      bt.setEdit(true);
    else
      bt.setEdit(false);
  }
  
  /*
   * ��������֯��ֵ�ı��Ժ�����ı仯
   */
  private void afterCalbodyChanged(String sKey) throws Exception {
    String sRole = getCorpRole(sKey);
    BillItem bt = m_card.getHeadItem(sKey);
    UIRefPane ref = (UIRefPane) bt.getComponent();
    m_card.setHeadItem(sRole + "cbname", ref.getRefName());

    // �������ʷ��Ϣ
    TOBillTool.clearAllRowValues(m_card, new String[] {
        "nprice", "nnotaxprice", "ntaxrate", "nmny", "nnotaxmny",
        "naddpricerate", "naskprice", "nasknotaxprice"
    });

    // ����༭������֯
    if (sRole.equals("cout")) {
      // ����ˢ��atp
      int iRow = m_card.getBillTable().getSelectedRow();
      if (iRow >= 0) {
        showInvAmount(iRow);
      }
      String sOutCbID = getHeadTailItemValue("coutcbid");
      String sOutCorpID = getHeadTailItemValue("coutcorpid");
      String sTakeOutCorpID = getHeadTailItemValue("ctakeoutcorpid");

      if (sOutCorpID != null && sOutCorpID.equals(sTakeOutCorpID)) {
        m_card.setHeadItem("ctakeoutcbid", sOutCbID);
      }
    }
    // ����༭������֯
    else if (sRole.equals("cin")) {
      // v5 ����������ֱ�˵�������������ϵͳ�����ֱ�˵��������������޸ĵ��뷽��Ϣ���ʲ����ܽ�������ķ���
      // chkDirectStore();
      setWhInfo(null);
    }

    // ������ϵ���ˣ���Ҫ����ȡ�����еļ۸�
    int iCount = m_card.getBillModel().getRowCount();
    ArrayList<Integer> alRow = new ArrayList<Integer>(iCount);
    for (int i = 0; i < iCount; i++) {
      m_card.getBillModel().setValueAt(ref.getRefPK(), i, sRole + "cbid");
      // ����޸��˱�ͷ��Ҳ��Ϊ�޸��˱���
      if (m_card.getBillModel().getRowState(i) == BillModel.NORMAL) {
        m_card.getBillModel().setRowState(i, BillModel.MODIFICATION);
      }
      // �����Ƿ������˴��
      if (!isNull(getCellValue(i, "cinvcode"))) {
        alRow.add(i);
      }
    }
    if (alRow.size() > 0) {
      int[] iaRow = new int[alRow.size()];
      for (int i = 0; i < iaRow.length; i++) {
        iaRow[i] = ((Integer) alRow.get(i)).intValue();
      }

      // ����б����У�����Ƿ���䵽�����֯��ˢ�±�������Ϣ
      checkInvInfo(iaRow);

      // ��ȡ��
      setPrice(iaRow);
    }
  }

  /**
   * ����Ƿ���䵽��˾��ˢ�±�������Ϣ
   * <b>����˵��</b>
   * @param iaRow
   * @throws BusinessException
   * @author sunwei
   * @time 2009-1-19 ����11:07:13
   */
  protected void checkInvInfo(int[] iaRow) throws BusinessException {
    // �������id
    String[] saInvBasID = new String[iaRow.length];
    for (int i = 0; i < iaRow.length; i++) {
      saInvBasID[i] = (String) m_card.getBillModel().getValueAt(iaRow[i],
          "cinvbasid");
    }
    // ��˾id�������֯id
    HashMap hmCorp = getTransCorpID();
    String sInCorpID = hmCorp.get("cincorpid").toString();
    String sInCbID = hmCorp.get("cincbid").toString();
    String sOutCorpID = hmCorp.get("coutcorpid").toString();
    String sOutCbID = hmCorp.get("coutcbid").toString();
    String sTakeOutCorpID = hmCorp.get("ctakeoutcorpid").toString();
    String sTakeOutCbID = hmCorp.get("ctakeoutcbid").toString();
    
    //�����֯Ϊ�գ������м��
    if(VOChecker.isEmpty(sInCbID)||VOChecker.isEmpty(sOutCbID)||VOChecker.isEmpty(sTakeOutCbID)){
      return;
    }
    
    // ������Ƿ���䵽��Ӧ�Ĺ�˾
    HashMap hmInvManID = BusinessCheck.invManDocChk(saInvBasID, new String[] {
        sInCorpID, sOutCorpID, sTakeOutCorpID
    });
    String[] saOutInvID = new String[iaRow.length];
    for (int i = 0; i < iaRow.length; i++) {
      saOutInvID[i] = (String) hmInvManID.get(saInvBasID[i] + sOutCorpID);
    }
    // �����Ϣ�ĳ���ȡ
    HashMap hmParam = InvInfo.getMngInfo(saInvBasID, saOutInvID);
    // time.showExecTime("ȡ�����Ϣ");
    StringBuffer sbKey = new StringBuffer();
    ParamVO param = null;

    for (int ctr = 0; ctr < iaRow.length; ctr++) {
      // �������id
      String sOutInvID = saOutInvID[ctr];
      // �������id
      String sInvBasID = saInvBasID[ctr];
      sbKey.setLength(0);
      sbKey = sbKey.append(sInvBasID).append(sOutInvID);
      param = (ParamVO) hmParam.get(sbKey.toString());
      if (param.bLaborFlag || param.bDiscountFlag)
        throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40093010", "UPP40093010-000023")/*
                                                           * @res
                                                           * "����ѡȡΪ�����۸��ۿ۵Ĵ��"
                                                           */);

      // �������id
      String sInInvID = hmInvManID.get(sInvBasID + sInCorpID).toString();
      String sTakeOutInvID = hmInvManID.get(sInvBasID + sTakeOutCorpID)
          .toString();

      // ���ñ��еĴ����Ϣ
      HashMap hmValue = new HashMap();
      hmValue.put("cincorpid", sInCorpID);
      hmValue.put("cincbid", sInCbID);
      hmValue.put("coutcorpid", sOutCorpID);
      hmValue.put("coutcbid", sOutCbID);
      hmValue.put("ctakeoutcorpid", sTakeOutCorpID);
      hmValue.put("ctakeoutcbid", sTakeOutCbID);
      hmValue.put("cinvbasid", sInvBasID);
      hmValue.put("cininvid", sInInvID);
      hmValue.put("coutinvid", sOutInvID);
      hmValue.put("ctakeoutinvid", sTakeOutInvID);
      hmValue.put("coutinvclid", param.sInInvCls);
      hmValue.put("cinvcode", param.sInvcode);
      hmValue.put("cinvname", param.sInvname);
      hmValue.put("cinvspec", param.sInvspec);
      hmValue.put("cinvtype", param.sInvtype);
      hmValue.put("cinvaddress", param.sProdarea);
      hmValue.put("measname", param.sMeasName);
      
      //������и���λ�������ʵȣ��򲻴�����ֵ.��Ϊ����λ����Ϣ�Ǽ��ż��ģ�����˾����֯�޹أ�����䶯
      if(VOChecker.isEmpty(m_card.getBillModel().getValueAt(iaRow[ctr], "castunitid"))){
        hmValue.put("castunitid", param.sDefaultMeasID);
        hmValue.put("castunitname", param.sDefaultMeasName);
        if (param.sDefaultMeasName.equalsIgnoreCase("")) {
          hmValue.put("nassistnum", "");
        }
        hmValue.put("nchangerate", param.dRate == null ? "" : param.dRate.toString());
      }
      
      hmValue.put("cquoteunitid", param.sMeasID);
      hmValue.put("cquoteunitname", param.sMeasName);
      hmValue.put("nquoteunitrate", param.dQuoteUnitRate.toString());

      hmValue.put("bquotefixrate", param.bFixRate);// �����Ƿ񱨼ۼ�����λ�̶������ʣ��������۽�������ʹ��
      // �������Ϸ���
      TOBillTool.setRowValue(m_card, hmValue, iaRow[ctr]);
    }
  }

  protected BillItem getBodyItem(String sKey) {
    return m_card.getBodyItem(NodeInfo.NAME_TABLECODE_BASE, sKey);
  }

  private void afterPsnEdit(BillEditEvent e) {
    int iRow = e.getRow();
    String sKey = e.getKey();
    String sDeptIDField = "", sDeptNameField = "";
    if (sKey.indexOf("in") >= 0) {
      sDeptIDField = "cindeptid";
      sDeptNameField = "cindeptname";
    }
    else if (sKey.indexOf("takeout") >= 0) {
      sDeptIDField = "ctakeoutdeptid";
      sDeptNameField = "ctakeoutdeptname";
    }
    else if (sKey.indexOf("out") >= 0) {
      sDeptIDField = "coutdeptid";
      sDeptNameField = "coutdeptname";
    }
    // �Զ�������Ӧ������Ϣ
    BillItem bt = m_card.getBillModel().getItemByKey(sKey);
    Object oDeptName = ((UIRefPane) bt.getComponent())
        .getRefValue("bd_deptdoc.deptname");
    Object oDeptPK = ((UIRefPane) bt.getComponent())
        .getRefValue("bd_psndoc.pk_deptdoc");
    if (oDeptPK != null) {
      m_card.getBillModel().setValueAt(oDeptPK, iRow, sDeptIDField);
      m_card.getBillModel().setValueAt(oDeptName, iRow, sDeptNameField);
    }
  }

  /**
   * ****************************************** ���ܣ� <|> �������ڣ�(2004-3-11
   * 20:58:45)
   * 
   * @param��
   * @return�� ******************************************
   */
  public boolean beforeCustEdit(BillEditEvent e) throws Exception {

    String sKey = e.getKey();
    boolean bEditFlag = false;

    // ��������
    String sAllocFlag = getHeadTailItemValue("bdrictflag");
    if (sAllocFlag.equals("false")) {
      sAllocFlag = "1";
    }
    else {
      sAllocFlag = "0";
    }
    // ���������ջ���ַ���ջ�����Ӧ���༭
    if (getSourceType(e) == null
        && Integer.parseInt(sAllocFlag) == ConstVO.ITransferType_DIRECT) {
      bEditFlag = true;
    }
    // �����ջ��ͻ�
    if (sKey.equals("creceievename")) {

      // �ջ��ͻ�Ϊ���빫˾�Ŀͻ�
      if (Integer.parseInt(sAllocFlag) == ConstVO.ITransferType_DIRECT) {
        String sPk_corp = (String) getHeadTailItemValue("cincorpid");
        String sCondition = "(  frozenflag  =  'N'  or  frozenflag  is  null  ) AND  (  bd_cumandoc.custflag  =  '0'  or  bd_cumandoc.custflag  =  '2'  ) ";
        BillItem btCurrent = getBodyItem("creceievename");
        CardPanelTool.filterRef(btCurrent, sCondition, sPk_corp);
      }
    }
    return bEditFlag;
  }

  // ���ݵ�����˾���˹�Ӧ��
  protected boolean beforeVendorEdit(BillEditEvent e) throws Exception {

    BillItem btCurrent = getBodyItem(e.getKey());
    String sPk_corp = getHeadTailItemValue("coutcorpid");
    String sCondition = " bd_cumandoc.custflag in('1','3') ";
    CardPanelTool.filterRef(btCurrent, sCondition, sPk_corp);

    return getBodyItem(e.getKey()).isEdit();
  }

  /**
   * ****************************************** ���ܣ� <|>��Դ�������� �������ڣ�(2004-3-11
   * 20:58:45)
   * 
   * @param��
   * @return�� ******************************************
   */
  private String getSourceType(BillEditEvent e) throws BusinessException {

    Object oTemp = getCellValue(e.getRow(), "csourcetypecode");
    return oTemp == null ? null : oTemp.toString();

  }

  /**
   * ��������������ֵ �������ڣ�(01-2-26 13:29:17)
   */
  private void setBodyFreeValue(int row, InvVO voInv) {
    if (!isNull(voInv)) {
      voInv.setFreeItemValue("vfree1", getCellValue(row, "vfree1"));
      voInv.setFreeItemValue("vfree2", getCellValue(row, "vfree2"));
      voInv.setFreeItemValue("vfree3", getCellValue(row, "vfree3"));
      voInv.setFreeItemValue("vfree4", getCellValue(row, "vfree4"));
      voInv.setFreeItemValue("vfree5", getCellValue(row, "vfree5"));
    }
  }

  /**
   * ���û��� �������ڣ�(2004-5-9 16:57:38)
   */
  protected void setRate(String sCurrType, String sOutCorp)
      throws BusinessException {

    if (isNull(sOutCorp) || isNull(sCurrType)) {
      return;
    }
    BusinessCurrencyRateUtil currArith = getBizCheck().getCurrArith(sOutCorp);
    String sBasCurrType = CurrParamQuery.getInstance().getLocalCurrPK(sOutCorp);
    String sDate = getHeadTailItemValue("dbilldate");
    sDate = isNull(sDate) ? m_NodeInfo.getLogDate() : sDate;
    boolean bBD302 = false;
    // �۱�����
    m_card.setHeadItem("nexchangeotobrate", null);

    UFDouble daRate = null;

    // �Ƿ������Һ���
    if (!bBD302) {
      // ��������Һ���,ԭ���۱���
      UFDouble dRate = currArith.getRate(sCurrType, sBasCurrType, sDate);
      if (isNull(dRate)) {
        sendErrMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
            "UPP40093010-000204")/* "û�л���۱����ʣ�" */);
      }
      daRate = dRate;
    }
    m_card.setHeadItem("nexchangeotobrate", daRate);

    // �Ƿ�ɱ༭
    setRateEdit(false, sOutCorp, sCurrType);
  }

  /**
   * �˴����뷽��˵��:�����������ļ�� �������ڣ�(2004-6-9 11:47:17)
   */
  private void chkNegativeNum(BillEditEvent e) throws BusinessException {

    int iRow = e.getRow();
    String sKey = e.getKey();
    Object oValue = e.getValue();
    try {
      if (sKey.indexOf("rate") != -1
          && oValue != null
          && oValue.toString().length() > 0
          && (oValue.toString().indexOf("-") != -1 || Double.parseDouble(oValue
              .toString()) == 0d)) {
        throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40093010", "UPP40093010-000050")/*
                                                           * @res "��������������"
                                                           */);
      }
      if (isRetBill(iRow) && sKey.indexOf("num") != -1 && oValue != null
          && oValue.toString().length() > 0
          && Double.parseDouble(e.getValue().toString()) >= 0d) {
        throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40093010", "UPP40093010-000052")/*
                                                           * @res
                                                           * "���������˻�ʱ�������븺��"
                                                           */);
      }
    }
    catch (BusinessException be) {
      if (e.getPos() == Const.TABLE_BODY)
        TOBillTool.clearRowValues(m_card, iRow, new String[] {
          sKey
        });
      else
        TOBillTool.clearHeadTailValue(m_card, new String[] {
          sKey
        });
      throw be;
    }

  }

  /**
   * ���ÿ�Ƭ����ľ��� sOutCorpID ������˾ bNewCurrType :�Ƿ�Ϊ������˾�ı�λ�ң� �������ڣ�(2004-6-14
   * 19:43:36) int[0] ----- �����ľ��� int[1] ----- ���۵ľ��� int[2] ----- ���ľ��� int[3]
   * ----- �����������ľ��� int[4] ----- �����ʵľ��� int[5] -----
   * �۱����ʵľ��ȣ��˴��Ļ���ʵ���Ǵ���ģ���Ϊ����Ҫ֪���������֣�����Ĭ�Ϸ���5λ�� int[6] -----
   * �۸����ʵľ��ȣ��˴��Ļ���ʵ���Ǵ���ģ���Ϊ����Ҫ֪���������֣�����Ĭ�Ϸ���5λ
   */
  public void freshScale(String sOutCorpID, String sCurrType) {

    if (isNull(sCurrType)) {
      return;
    }

    try {
      Integer[] iaDataPrecision = m_NodeInfo.getDataPrecision();

      BusinessCurrencyRateUtil currArith = BusinessCheck
          .getCurrArith(sOutCorpID);

      String sBasCurrType = m_NodeInfo.getCurrType(sOutCorpID);// ��λ��

      // ���ý���
      // ��ȡ�۱�����(��������Һ��㣬��Դ�����Ǹ��ң�Ŀ�ı����Ǳ��ң�������ԭ���۱���)
      CurrinfoVO voCurr = null;

      voCurr = currArith.getCurrinfoVO(sCurrType, sBasCurrType);

      // ����۱����ʵľ���
      /*
       * л��Ի�� ��û�ж�����ʷ���ʱ���۱����ʵ�Ĭ��С��λ���ڹ�Ӧ����ģ�鴦��һ�£������п��ܻᵼ�µ��ݿ�ģ��ת��ʱ�۱����ʱ��ı�����⡣
       * ����ͳһ��Ӧ����ģ����۱�����Ĭ��С��λ����ͳһ�����5λ����Ϊ���ڲ������5λ�����Ǳ���������һ�¡�
       */
      iaDataPrecision[5] = isNull(voCurr) || isNull(voCurr.getRatedigit()) ? 5
          : voCurr.getRatedigit();

      Integer currBusiDigit = CurrtypeQuery.getInstance().getCurrtypeVO(
          sCurrType).getCurrbusidigit();

      iaDataPrecision[2] = isNull(currBusiDigit) ? iaDataPrecision[2]
          : currBusiDigit;

      // ����������۾���,������˾�п���Ϊ������˾
      TOEnvironment toEnv = new TOEnvironment();
      Integer[] ret = toEnv.getDataPrecision(sOutCorpID);
      iaDataPrecision[0] = isNull(ret[0]) ? iaDataPrecision[0] : ret[0];
      iaDataPrecision[1] = isNull(ret[1]) ? iaDataPrecision[0] : ret[1];
      iaDataPrecision[3] = isNull(ret[3]) ? iaDataPrecision[0] : ret[3];
      iaDataPrecision[4] = isNull(ret[4]) ? iaDataPrecision[0] : ret[4];
      m_NodeInfo.setDataPrecision(iaDataPrecision);
      firePanelEvent(GenPanelEvent.ASK, new Integer(Const.IASK_CARD_FRESHSCALE));

    }
    catch (Exception ex) {
      GenMsgCtrl.printHint(ex.getMessage());
      sendErrMsg(ex.getMessage());
    }
  }

  /**
   * �����ߣ� ���ܣ���Ŀ��Ϣ�༭�Ժ󴥷� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void afterProjectEdit(nc.ui.pub.bill.BillEditEvent e) {
    Object oTemp = e.getValue();
    int iRow = e.getRow();

    TOBillTool.clearRowValues(m_card, iRow, new String[] {
        "cprojectphasename", "cprojectphase"
    });
  }

  /**
   * �����ߣ� ���ܣ���Ŀ�׶���Ϣ�༭�Ժ󴥷� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void afterProjectphaseEdit(nc.ui.pub.bill.BillEditEvent e) {
    Object oTemp = e.getValue();
    int iRow = e.getRow();
    if (!isNull(oTemp)) {
      nc.ui.pub.bill.BillItem bt = getBodyItem("cprojectphasename");
      UIRefPane ufpane = (UIRefPane) bt.getComponent();
      m_card.getBillModel()
          .setValueAt(ufpane.getRefPK(), iRow, "cprojectphase");
    }
    else {
      TOBillTool.clearRowValues(m_card, iRow, new String[] {
          "cprojectphase", "cprojectphasename"
      });
    }
  }

  private void afterWhEdit(BillEditEvent e) throws Exception {
    if (e.getPos() == BillItem.HEAD) {
      afterHeadWhEdit(e);
    }
    else if (e.getPos() == BillItem.BODY) {
      afterBodyWhEdit(e);
    }
  }

  // �༭��ͷ�ֿⴥ��
  private void afterHeadWhEdit(BillEditEvent e) throws Exception {
    String sKey = e.getKey();
    String sRole = getCorpRole(sKey);
    String sPk = ((nc.ui.pub.beans.UIRefPane) e.getSource()).getRefPK();
    String sName = ((nc.ui.pub.beans.UIRefPane) e.getSource()).getRefName();
    m_card.setHeadItem(sRole + "whname", sName);
    // �������
    int iRowCount = m_card.getBillModel().getRowCount();
    if (iRowCount > 0) {
      // �����λ
      TOBillTool.clearAllRowValues(m_card, new String[] {
          sRole + "spacename", sRole + "spaceid"
      });

      for (int i = 0; i < iRowCount; i++) {
        m_card.getBillModel().setValueAt(sPk, i, sKey);
        m_card.getBillModel().setValueAt(sName, i, sRole + "whname");

        if (m_card.getBillModel().getRowState(i) == BillModel.NORMAL) {
          m_card.getBillModel().setRowState(i, BillModel.MODIFICATION);
        }
      }
    }

    // �ֿ�Ϊ�๫˾���գ��п�����ѡ�ֿ�������˾����֯��ı䡣����һ��Ҫˢ��Ӧ�Ĺ�˾����֯
    if (sPk != null) {
      // ���ݲֿ���������֯�͹�˾
      Object[][] oValue = nc.ui.scm.pub.cache.CacheTool.getMultiColValue(
          "bd_stordoc", "pk_stordoc", new String[] {
              "pk_corp", "pk_calbody"
          }, new String[] {
            sPk
          });
      String pk_corp = (String) oValue[0][0];
      String pk_calbody = (String) oValue[0][1];
      BillItem btcorp = m_card.getHeadItem(sRole + "corpid");
      BillItem btcb = m_card.getHeadItem(sRole + "cbid");
      if (btcorp != null) {
        String oldCorp = (String) btcorp.getValueObject();
        if (!isSameString(pk_corp, oldCorp)) {
          m_card.setHeadItem(sRole + "corpid", pk_corp);
          if (btcb != null) {
            ((UIRefPane) btcb.getComponent()).setPk_corp(pk_corp);
          }
          // �ı乫˾����ı仯��������幫˾�ͱ�λ��
          afterCorpChanged(sRole + "corpid");
        }
      }
      if (btcb != null) {

        String oldCB = (String) btcb.getValueObject();
        if (!isSameString(pk_calbody, oldCB))

          m_card.setHeadItem(sRole + "cbid", pk_calbody);

        // �ı�����֯����ı仯�������������֯������ѯ��
        afterCalbodyChanged(sRole + "cbid");
      }
    }
  }

  /*
   * ��˾�ı��Ժ�����ı仯
   */
  private void afterCorpChanged(String sKey) throws Exception {
    String sRole = getCorpRole(sKey);
    BillItem bt = m_card.getHeadItem(sKey);
    UIRefPane ref = (UIRefPane) bt.getComponent();
    m_card.setHeadItem(sRole + "corpname", ref.getRefName());
    String sPK = ref.getRefPK();

    int iCount = m_card.getBillModel().getRowCount();
    for (int i = 0; i < iCount; i++) {
      m_card.getBillModel().setValueAt(sPK, i, sRole + "corpid");
      // ����Ĺ�˾ҲҪ��֮�ı�
      if (m_card.getBillModel().getRowState(i) == BillModel.NORMAL) {
        m_card.getBillModel().setRowState(i, BillModel.MODIFICATION);
      }
    }

    String sTakeOutCorpid = getHeadTailItemValue("ctakeoutcorpid");
    String sOutCorpid = getHeadTailItemValue("coutcorpid");

    // �༭������˾�������Ӧ�ı�λ��
    if (sRole.equals("cout")) {
      String sCurrType = m_NodeInfo.getCurrType(sPK);
      m_card.setHeadItem("coutcurrtype", sCurrType);
      freshScale(sPK, sCurrType);
      setRate(sCurrType, sPK);

      // ������˾Ϊ�ջ������˾�������˾��ͬ��������������˾��ֵ
      if (isNull(getHeadTailItemValue("ctakeoutcorpid"))||isSameOutTakeOut) {
        m_card.setHeadItem("ctakeoutcorpid", sPK);
        // �����˾�µĿ����֯
        TOBillTool.clearHeadTailValue(m_card, new String[] {
            "ctakeoutcbid", "ctakeoutcbname"
        });
        // �����˾�µĲֿ�
        TOBillTool.clearHeadTailValue(m_card, new String[] {
            "ctakeoutwhid", "ctakeoutwhname"
        });

        // �����˾�µĲ��š���Ա���ֿ⡢��λ��Ϣ
        TOBillTool.clearAllRowValues(m_card, new String[] {
            "ctakeoutdeptname", "ctakeoutdeptid", "ctakeoutpsnname",
            "ctakeoutpsnid", "ctakeoutwhname", "ctakeoutwhid", "ctakeoutspacename",
            "ctakeoutspaceid"
        });
      }
      /*
       * ����ȡ�����еļ۸���Ϊ�����֯Ϊ��������Դ˴������²��ҵ�����ϵ������ ��������֯�Ժ������²���
       */

      TOBillTool.clearAllRowValues(m_card, new String[] {
          "cprojectid", "cprojectname", "cprojectphase", "cprojectphasename",
          "nprice", "nnotaxprice", "ntaxrate", "nmny", "nnotaxmny",
          "naddpricerate", "naskprice", "nasknotaxprice"
      });

    }

    String sInCorpid = getHeadTailItemValue("cincorpid");
    // ѯĬ�Ͻ���·��
    if (sOutCorpid != null && sInCorpid != null && sInCorpid.length() != 0
        && sOutCorpid.length() != 0) {
      if (sTakeOutCorpid != null && sTakeOutCorpid.equals(sOutCorpid)) {
        String[] ret = ClientOuterHelper.getSettlePath(sOutCorpid, sInCorpid);
        if (ret != null) {
          m_card.setHeadItem("csettlepathid", ret[0]);
          m_card.setHeadItem("csettlepathname", ret[1]);
        }
        else {
          m_card.setHeadItem("csettlepathid", null);
          m_card.setHeadItem("csettlepathname", null);
        }
      }
      else {
        // �������������ý���·��
        m_card.setHeadItem("csettlepathid", null);
        m_card.setHeadItem("csettlepathname", null);
      }
    }
    else {
      m_card.setHeadItem("csettlepathid", null);
      m_card.setHeadItem("csettlepathname", null);
    }

    //����˾�ı�ʱ���ж��Ƿ���Ƶ��뷽Ȩ��
    if (sRole.equals("cin")||sRole.equals("cout")) {
      setRefUserPower();
    }
  }
  
  /*
   * ���ò��յ�����Ȩ��
   * ����˾�ı�ʱ���ж��Ƿ���Ƶ��뷽Ȩ��
   * */
  private void setRefUserPower()
  {
    String sInCorpid = getHeadTailItemValue("cincorpid");
    String sOutCorpid = getHeadTailItemValue("coutcorpid");
    if(!VOChecker.isEmpty(sInCorpid)
        &&!VOChecker.isEmpty(sOutCorpid)
        &&!sOutCorpid.equals(sInCorpid)){
      //�����Ƶ��뷽������Ȩ��
      //--��ͷ��������֯
      CardPanelTool.setRefUserPower(m_card,IBillItem.HEAD, "cincbid", false);
      //--��ͷ����ֿ�
      CardPanelTool.setRefUserPower(m_card,IBillItem.HEAD, "cinwhid", false);
      //--�������ֿ�
      CardPanelTool.setRefUserPower(m_card,IBillItem.BODY, "cinwhname", false);
      //--������벿��
      CardPanelTool.setRefUserPower(m_card,IBillItem.BODY, "cindeptname", false);
      //--������벿��ҵ��Ա
      CardPanelTool.setRefUserPower(m_card,IBillItem.BODY, "cinpsnname", false);
    }
    else{
      //���Ƶ��뷽������Ȩ��
      //--��ͷ��������֯
      CardPanelTool.setRefUserPower(m_card,IBillItem.HEAD, "cincbid", true);
      //--��ͷ����ֿ�
      CardPanelTool.setRefUserPower(m_card,IBillItem.HEAD, "cinwhid", true);
      //--�������ֿ�
      CardPanelTool.setRefUserPower(m_card,IBillItem.BODY, "cinwhname", true);
      //--������벿��
      CardPanelTool.setRefUserPower(m_card,IBillItem.BODY, "cindeptname", true);
      //--������벿��ҵ��Ա
      CardPanelTool.setRefUserPower(m_card,IBillItem.BODY, "cinpsnname", true);
    }
  }

  /**
   * ****************************************** ���ܣ� <|> �������ڣ�(2004-3-15
   * 21:57:54)
   * 
   * @param��
   * @return�� ******************************************
   */
  private void afterCorpUnitEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    // ����Ĺ�˾
    String sKey = e.getKey().trim();
    String sRole = getCorpRole(sKey);

    // �����˾�µĿ����֯
    TOBillTool.clearHeadTailValue(m_card, new String[] {
        sRole + "cbid", sRole + "cbname"
    });
    // �����˾�µĲֿ�
    TOBillTool.clearHeadTailValue(m_card, new String[] {
        sRole + "whid", sRole + "whname"
    });

    // �����˾�µĲ��š���Ա���ֿ⡢��λ��Ϣ
    TOBillTool.clearAllRowValues(m_card, new String[] {
        sRole + "deptname", sRole + "deptid", sRole + "psnname",
        sRole + "psnid", sRole + "whname", sRole + "whid", sRole + "spacename",
        sRole + "spaceid"
    });

    afterCorpChanged(sKey);
  }

  /**
   * �˴����뷽��˵��: �������ڣ�(2004-6-24 10:27:21)
   * 
   * @param eBillEditEvent
   */
  private void afterBodyWhEdit(BillEditEvent e) throws Exception {
    String sKey = e.getKey();
    String sRole = getCorpRole(sKey);
    int iRow = e.getRow();

    // �����λ
    TOBillTool.clearRowValues(m_card, iRow, new String[] {
        sRole + "spacename", sRole + "spaceid"
    });

    // ����������ֿ�
    if (sKey.endsWith("outwhname")) {
      // �����������������ֿ⣽�����ֿ�
      if (!m_NodeInfo.getOrderTypeCode(m_card).equals(ConstVO.m_sBillSFDBDD)) {
        m_card.getBillModel().setValueAt(getCellValue(iRow, "coutwhid"), iRow,
            "ctakeoutwhid");
        m_card.getBillModel().execEditFormulasByKey(iRow, "ctakeoutwhname");
      }
      // ��ѯ������
      showInvAmount(iRow);
    }
    else if (sKey.endsWith("inwhname")) {
      setWhInfo(new int[] {
        iRow
      });
    }
    if (m_card.getBillModel().getRowState(iRow) == BillModel.NORMAL) {
      m_card.getBillModel().setRowState(iRow, BillModel.MODIFICATION);
    }
    // �༭�ֿ����֯�ڵ���Ҫ����ѯ������ϵ�ͼ۸���Ϊ����֯�ڵ������ֿⲻ��Ϊƥ�������ϵ��������
    if (ConstVO.m_sBillZZNDBDD.equals(m_NodeInfo.getOrderTypeCode(m_card))) {
      // �����Ƿ������˴��
      if (!isNull(getCellValue(iRow, "cinvcode"))) {
        // ��ȡ��
        setPrice(new int[] {
          iRow
        });
      }
    }
  }

  /**
   * �����ߣ� ���ܣ�������Ŀ�׶�¼��֮ǰ�Ĵ��� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  protected boolean beforeProjectEdit(nc.ui.pub.bill.BillEditEvent e)
      throws BusinessException {

    String sOutCorpID = getHeadTailItemValue("coutcorpid");
    if (isNull(sOutCorpID))
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPP40093010-000053")/*
                                             * @res "����ѡ�������˾"
                                             */);

    BillItem bt = getBodyItem(e.getKey());
    UIRefPane ref = (UIRefPane) bt.getComponent();
    ref.setRefModel(new nc.ui.bd.b39.JobRefTreeModel(ConstVO201.sGROUPID,
        sOutCorpID, null));
    return true;
  }

  /**
   * �����ߣ� ���ܣ�������Ŀ�׶�¼��֮ǰ�Ĵ��� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  protected boolean beforeProjectPhaseEdit(nc.ui.pub.bill.BillEditEvent e) {
    String sProjectID = getCellValue(e.getRow(), "cprojectid");
    if (isNull(sProjectID)) {
      sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
          "UPP40093010-000054")/* @res "����������Ŀ��Ϣ" */);
      return false;
    }

    UIRefPane cprojectphasename = (UIRefPane) getBodyItem(e.getKey())
        .getComponent();
    cprojectphasename.setRefModel(new nc.ui.bd.b39.PhaseRefModel(sProjectID));

    return true;
  }

  /**
   * �˴����뷽��˵��:���ǰ�εĴ����Ϣ �������ڣ�(2004-6-21 19:25:50)
   */
  private void clearInvInfo(int iRow) {

    TOBillTool.clearRowValues(m_card, iRow, new String[] {
        "cinvcode", "cinvname", "cinvspec", "cinvtype", "measname",
        "cinvbasid", "cininvid", "coutinvid", "ctakeoutinvid", "castunitid",
        "castunitname", "nassistnum", "nnum", "nchangerate", "dproducedate",
        "dvalidate", "vbatch", "nprice",
        "nnotaxprice",
        "nmny",
        "nnotaxmny",
        "ntaxrate",
        // "csourcetypecode",
        // "csourcetypecodename",
        // "vsourcecode",
        // "vsourcerowno",
        "cinvaddress", "vfree0", "vfree1", "vfree2", "vfree3", "vfree4",
        "vfree5", "cininvclid", "coutinvclid"
    });

    TOBillTool.clearHeadTailValue(m_card, new String[] {
        "ATP", "curamount"
    });
  }

  /**
   * ��ȡ���д������������Ϣ �������ڣ�(2004-6-17 9:13:20)
   * 
   * @return java.lang.String[]
   */
  private String[] getFreeItem(int iRow) {
    String[] saFreeItem = new String[5];
    // �������Ƿ�ȫΪ��
    boolean bRetValue = false;
    for (int i = 1; i <= saFreeItem.length; i++) {
      Object oTemp = getCellValue(iRow, "vfree" + i);
      if (isNull(oTemp)) {
        saFreeItem[i - 1] = null;
      }
      else {
        saFreeItem[i - 1] = oTemp.toString();
        bRetValue = true;
      }
    }

    return bRetValue ? saFreeItem : null;
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2004-6-17 9:13:20)
   * 
   * @return FreeItemRefPane
   */
  private nc.ui.scm.ic.freeitem.FreeItemRefPane getRefFreeItem() {

    if (isNull(m_refFreeItem)) {
      nc.ui.pub.bill.BillItem bt = getBodyItem("vfree0");
      m_refFreeItem = (FreeItemRefPane) bt.getComponent();
    }

    return m_refFreeItem;
  }

  /**
   * �˴����뷽��˵��:�Ƿ�Ϊ�˻ص������� �������ڣ�(2004-6-14 16:05:30)
   * 
   * @return boolean
   */
  protected boolean isRetBill(int iRow) {
    String sRetFlag = getCellValue(iRow, "bretractflag");
    String sSourceType = (String) m_card.getBillModel().getValueAt(iRow,
        "csourcetypecode");
    return sRetFlag != null && sRetFlag.equals("true") && !isNull(sSourceType)
        && sSourceType.equals(getHeadTailItemValue("ctypecode"));

  }

  /**
   * �˴����뷽��˵��:�Ƿ����Ƶ����л���ʽ���ɵĵ����� �������ڣ�(2004-6-18 15:05:31)
   * 
   * @param row
   * @return : true:���Ƶ���; false:��ʽ���ɻ��˻صĵ���
   */
  private boolean isSelfMadeBill(int row) {
    return isNull(getCellValue(row, "vsourcecode"));
  }

  /**
   * �˴����뷽��˵��:���ݵ�����ϵ�ϵ�ѯ�ۺ�۸��Ƿ�ɸ��ж� �Ƿ���Ա༭���������ϵļ۸�˰�ʵ�
   * 
   * @return :
   */
  private boolean isCanUpdatePrice(int iRow) throws Exception {

    boolean bEdit = false;
    // ������Ϊ��Ʒ���۸��ܱ༭
    String sFlargess = getCellValue(iRow, "flargess");

    if (Boolean.valueOf(sFlargess).booleanValue()) {
      return bEdit;
    }
    // ���ݵ�����ϵ�ϵļ۸��Ƿ�ɸĹ�������Ƿ���Ա༭
    QryTransrelaVO[] voTransItem = getTransItem(new int[] {
      iRow
    });

    if (voTransItem != null && voTransItem[0] != null) {
      // �۸����
      String priceruleflag = voTransItem[0].getPriceruleflag();
      // ����۸����Ϊ��ȡ�ۣ�������༭�۸�
      if (priceruleflag == null
          || priceruleflag.equals(String.valueOf(ConstVO.PRICE_RULE_NULL))) {
        bEdit = true;
      }
      else {
        // ���ѯ�ۣ����ݵ�����ϵ�ϵĲ�����ѯ�ۺ�۸��Ƿ�ɸ�
        if (voTransItem[0].getBmodifyprice() != null
            && voTransItem[0].getBmodifyprice().booleanValue()) {
          bEdit = true;
        }
      }
    }
    else {
      bEdit = true;
    }
    return bEdit;
  }

  /**
   * �������ڣ�(2003-6-9 14:49:40)
   */
  protected void setItemEdit(BillVO voCurSel) throws BusinessException {

    String[] saKey = new String[] {
        "coutcurrtype", "coutwhid", "coutcbid", "coutcorpid", "cinwhid",
        "cincbid", "cincorpid", "bretractflag", "fallocname"
    };
    int[] iPosArray = new int[saKey.length];
    Arrays.fill(iPosArray, Const.TABLE_HEAD);
    boolean[] bEditArray = new boolean[saKey.length];

    boolean bEditFlag = false;
    if (isRetBill(0))// �����˻ص���
    {
      Arrays.fill(bEditArray, bEditFlag);
      CardPanelTool.setItemEdit(m_card, bEditArray, iPosArray, saKey);
      return;
    }

    // �۱����������Ƿ�ɱ༭
    if (!TOBillTool.isNull(voCurSel)) {
      BillHeaderVO voHead = voCurSel.getHeaderVO();
      BusinessCurrencyRateUtil currArith = getBizCheck().getCurrArith(
          voHead.getCoutcorpid());
      setRateEdit(false, voHead.getCoutcorpid(), voHead.getCoutcurrtype());
    }
    final int iPosCanEdit = 5;
    Arrays.fill(bEditArray, 0, iPosCanEdit, true);

    // �ֻ����γɵĵ������������޸ĵ��뷽��Ϣ,���������γɵĵ������������޸ĵ��뷽��Ϣ����������
    if (!isNull(voCurSel)
        && !isNull(voCurSel.getItemVOs()[0].getCsourcetypecode())
        && !voCurSel.getItemVOs()[0].getCsourcetypecode().equals(
            ConstVO.m_sBillFHD)) {
      Arrays.fill(bEditArray, iPosCanEdit, saKey.length, false);
    }
    else {
      Arrays.fill(bEditArray, iPosCanEdit, saKey.length, true);
    }
    if (!isNull(voCurSel) && !isNull(voCurSel.getHeaderVO().getFallocflag())) {
      // ֱ�˵�������ֿⲻ�ܱ༭
      if (voCurSel.getHeaderVO().getFallocflag().intValue() == ConstVO.ITransferType_DIRECT)
        bEditArray[4] = false;
      else
        bEditArray[4] = true;
    }
    CardPanelTool.setItemEdit(m_card, bEditArray, iPosArray, saKey);
  }

  protected boolean isNull(Object oValue) {
    return TOBillTool.isNull(oValue);
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2004-6-11 11:13:02)
   */
  public void initMultiNewLine(int[] iaRow,boolean bFillWhByHead) {
    if (iaRow == null || iaRow.length <= 0) {
      return;
    }
    // ����Ҫ�󵽻�����
    UFDate dtTemp = new UFDate(m_NodeInfo.getLogDate(), false);
    for (int i = 0; i < iaRow.length; i++) {
      m_card.getBillModel().setValueAt(dtTemp, iaRow[i], "dplanoutdate");
      m_card.getBillModel().setValueAt(dtTemp, iaRow[i], "dplanarrivedate");
    }
    if(bFillWhByHead){
      // ���õ����ֿ⣨�ɱ�ͷ���룩
      TOBillTool.copyItemValueFromHead(m_card, "coutwhname", "coutwhid", iaRow);
      // ���õ���ֿ⣨�ɱ�ͷ���룩
      TOBillTool.copyItemValueFromHead(m_card, "cinwhname", "cinwhid", iaRow);
      // ���ó����ֿ⣨�ɱ�ͷ���룩
      TOBillTool.copyItemValueFromHead(m_card, "ctakeoutwhname", "ctakeoutwhid",
          iaRow);
    }

    for (int i = 0; i < iaRow.length; i++) {
      Object coutcbid = m_card.getBillModel().getValueAt(iaRow[i], "coutcbid");
      Object ctakeoutcbid = m_card.getBillModel().getValueAt(iaRow[i],
          "ctakeoutcbid");
      if (ctakeoutcbid != null && coutcbid != null
          && ctakeoutcbid.equals(coutcbid)
          && m_card.getBillModel().getValueAt(iaRow[i], "ctakeoutwhid") == null) {
        m_card.getBillModel().setValueAt(
            m_card.getBillModel().getValueAt(iaRow[i], "coutwhname"), iaRow[i],
            "ctakeoutwhname");
        m_card.getBillModel().setValueAt(
            m_card.getBillModel().getValueAt(iaRow[i], "coutwhid"), iaRow[i],
            "ctakeoutwhid");
      }
    }
    // ���òֿ������Ϣ
    setWhInfo(iaRow);
    // ���ݲ���Ա����ҵ��Ա
    setDefaultPsnValue(iaRow);
  }

  /*
   * ���ݲ���Ա����ҵ��Ա
   */
  private void setDefaultPsnValue(int[] iaRow) {
    // ģ�������������ҵ��Ա��Ĭ��ֵ���򲻽�������Ĵ���
    if (getBodyItem("coutpsnid") != null
        && getBodyItem("coutpsnid").getDefaultValue() != null) {
      return;
    }
    IUserManageQuery iQuery = (IUserManageQuery) NCLocator.getInstance()
        .lookup(IUserManageQuery.class.getName());
    String sOutCorpid = getHeadTailItemValue("coutcorpid");
    // ���ҵ��Ա��Ϣvo
    PsndocVO voPsn = null;
    try {
      voPsn = iQuery.getPsndocByUserid(sOutCorpid, m_NodeInfo.getUserID());
    }
    catch (BusinessException ex) {
      ExceptionUtils.wrappException(ex);
    }
    if (voPsn != null) {
      String pk_psndoc = voPsn.getPk_psndoc();
      String psnname = voPsn.getPsnname();
      String pk_deptdoc = voPsn.getPk_deptdoc();
      String deptdocname = "";
      // ��ò�������
      Object[] obj = null;
      try {
        obj = (Object[]) CacheTool.getCellValue("bd_deptdoc", "pk_deptdoc",
            "deptname", pk_deptdoc);
      }
      catch (BusinessException ex) {
        ExceptionUtils.wrappException(ex);
      }

      if (obj != null && obj[0] != null) {
        deptdocname = obj[0].toString();
      }
      for (int i = 0; i < iaRow.length; i++) {
        m_card.getBillModel().setValueAt(pk_psndoc, iaRow[i], "coutpsnid");
        m_card.getBillModel().setValueAt(psnname, iaRow[i], "coutpsnname");
        // ����ҵ��Ա������������
        m_card.getBillModel().setValueAt(pk_deptdoc, iaRow[i], "coutdeptid");
        m_card.getBillModel().setValueAt(deptdocname, iaRow[i], "coutdeptname");
      }
    }
  }

  /**
   * ����ÿ�еļ۸� �������ڣ�(2004-7-22 14:20:29)
   * 
   * @param iRow
   *          �к�����
   */
  protected void setPrice(int[] iaRow) throws BusinessException {
    try {
      int[] iaNewRow = getNeedAskPriceLine(iaRow);

      if (iaNewRow.length > 0) {
        getPrice(iaNewRow);
        SetColor.resetColor(m_card);
        // �����������ֶεı༭��,SetColor.resetColor()������ɫ������������ɫ���������ˣ�������Ҫ������������ɫ��
        new InvAttrCellRenderer().setFreeItemRenderer(m_card);
      }
    }
    catch (BusinessException e) {
      sendErrMsg(e.getMessage());
    }
  }

  /**
   * ��ѯ��֮ǰ���ȵ��ô˷������˵������ֹ��޸Ĺ��ı����У��õ���Ҫѯ�۵ı�����
   * 
   * @param iaRow
   *          ѡ��ı�����
   * @return ��Ҫѯ�۵ı�����
   */
  private int[] getNeedAskPriceLine(int[] iaRow) {
    List<Integer> askPriceLineList = new ArrayList<Integer>();
    for (int i = 0; i < iaRow.length; i++) {
      UFDouble nprice = (UFDouble) m_card.getBillModel().getValueAt(iaRow[i],
          "nprice");
      UFDouble naskprice = (UFDouble) m_card.getBillModel().getValueAt(
          iaRow[i], "naskprice");
      if (nprice == null || nprice.equals(naskprice)) {
        askPriceLineList.add(iaRow[i]);
      }
    }

    int[] needAskPriceLines = new int[askPriceLineList.size()];
    for (int i = 0, len = askPriceLineList.size(); i < len; i++) {
      needAskPriceLines[i] = askPriceLineList.get(i);
    }

    return needAskPriceLines;
  }

  public void setPrice() throws Exception {
    int iLen = m_card.getRowCount();
    if (iLen <= 0) {
      return;
    }
    int[] iaRow = new int[iLen];
    for (int i = 0; i < iLen; i++) {
      iaRow[i] = i;
    }
    // ȡ��
    setPrice(iaRow);
  }

  /**
   * �����۱��۸��Ƿ�ɱ༭ �������ڣ�(2004-5-9 16:57:38) bBD302:�Ƿ������Һ��� sOutCorp:������˾
   * sCurrType:����
   */
  protected void setRateEdit(boolean bBD302, String sOutCorp, String sCurrType)
      throws BusinessException {

    BusinessCurrencyRateUtil currArith = getBizCheck().getCurrArith(sOutCorp);

    boolean bZBrateEdit = false;

    bBD302 = false;
    // �Ƿ������Һ���
    if (!bBD302) {
      // ��������Һ���
      // ������ֵ��ڱ�λ�������۱����ʲ����޸�
      if (!sCurrType.equals(CurrParamQuery.getInstance().getLocalCurrPK(
          sOutCorp))) {
        bZBrateEdit = true;
      }

      CardPanelTool.setItemEdit(m_card, new boolean[] {
        bZBrateEdit
      }, new int[] {
        Const.TABLE_HEAD
      }, new String[] {
        "nexchangeotobrate"
      });
    }

  }

  /**
   * ������ʱ,�Զ���������ֿ�ĵ�ַ,����,�ص㣬û�������������֯�� �������ڣ�(2004-8-31 10:58:44)
   */
  public void setWhInfo(int[] iRows) {
    if (iRows == null || iRows.length <= 0) {
      iRows = new int[m_card.getBillModel().getRowCount()];
      for (int i = 0; i < iRows.length; i++) {
        iRows[i] = i;
      }
    }
    ArrayList<String> alInWhID = new ArrayList<String>(iRows.length);
    for (int i = 0; i < iRows.length; i++) {
      alInWhID.add((String) m_card.getBillModel().getValueAt(iRows[i],
          "cinwhid"));
    }
    String[] saInWhID = new String[iRows.length];
    saInWhID = (String[]) alInWhID.toArray(saInWhID);
    // ��ѯ����ֿ�ĵص㣬��ַ
    Object[][] objsStor = null;
    // ��ѯ����ֿ�ĵص㣬��ַ
    if (!isNull(saInWhID)) {
      objsStor = nc.ui.scm.pub.CacheTool.getMultiColValue("bd_stordoc",
          "pk_stordoc", new String[] {
              "pk_address", "storaddr"
          }, (String[]) alInWhID.toArray(new String[iRows.length]));
    }
    // ������֯
    String sInCbID = (String) m_card.getHeadItem("cincbid").getValueObject();
    if (sInCbID == null || sInCbID.length() <= 0) {
      return;
    }
    Object[][] objsCb = nc.ui.scm.pub.CacheTool.getMultiColValue("bd_calbody",
        "pk_calbody", new String[] {
            "pk_address", "pk_areacl", "area"
        }, new String[] {
          sInCbID
        });
    String sPk_areacl = ""; // �ص�
    String sAddress = ""; // ��ַ
    ArrayList alPk_areal = new ArrayList();
    ArrayList alArrivearea = new ArrayList();
    // ��ͷ��������
    String sFallocFlag = getHeadTailItemValue("bdrictflag");
    if (sFallocFlag.equals("false")) {
      sFallocFlag = "1";
    }
    else {
      sFallocFlag = "0";
    }
    for (int i = 0; i < iRows.length; i++) {
      // ��������Ϊ������ʱ
      if (sFallocFlag != null
          && Integer.parseInt(sFallocFlag) == ConstVO.ITransferType_INSTORE) {
        sPk_areacl = "";
        sAddress = "";
        if (objsStor != null && objsStor[i] != null) {
          if (objsStor[i][0] != null)
            sPk_areacl = objsStor[i][0].toString();
          // ��ַ
          if (objsStor[i][1] != null)
            sAddress = objsStor[i][1].toString();
        }

        // ����ֿ�ĵ�ַ�ص���ϢΪ�գ�ȡ������֯��
        if (objsCb != null && objsCb[0] != null) {
          if ((sPk_areacl == null || sPk_areacl.trim().length() <= 0)
              && objsCb[0][0] != null)
            sPk_areacl = objsCb[0][0].toString();

          if ((sAddress == null || sAddress.trim().length() <= 0)
              && objsCb[0][2] != null)
            sAddress = objsCb[0][2].toString();
        }
        m_card.getBillModel().setValueAt(sPk_areacl, iRows[i], "pk_areacl");
        if(!VOChecker.isEmpty(sPk_areacl)){
          alPk_areal.add(sPk_areacl);
        }

        if (objsCb != null && objsCb[0] != null && objsCb[0][1] != null) {
          m_card.getBillModel().setValueAt(objsCb[0][1].toString(), iRows[i],
              "pk_arrivearea");
          alArrivearea.add(objsCb[0][1].toString());
        }

        m_card.getBillModel().setValueAt(sAddress, iRows[i], "vreceiveaddress");
      }
      else {
        // ֱ�˵���ʱ,�����û�����
      }
    }
    // ȡ�����ص��������Ϣ
    Object[] objAreaclname = null;
    String[] saArrivearea = null;
    Object[] objArriveareaname = null;
    try {
      objAreaclname = (Object[]) nc.ui.scm.pub.CacheTool.getColumnValue(
          "bd_address", "pk_address", "addrname", (String[]) alPk_areal
              .toArray(new String[alPk_areal.size()]));
      objArriveareaname = (Object[]) nc.ui.scm.pub.CacheTool.getColumnValue(
          "bd_areacl", "pk_areacl", "areaclname", (String[]) alArrivearea
              .toArray(new String[alArrivearea.size()]));
    }
    catch (BusinessException be) {
      Debug.debug(be.getMessage(), be);
    }

    int index1 = 0, index2 = 0;
    // ��������Ϊ������ʱ
    if (Integer.parseInt(sFallocFlag) == nc.vo.to.pub.ConstVO.ITransferType_INSTORE) {
      for (int i = 0; i < iRows.length; i++) {
        if (m_card.getBillModel().getValueAt(iRows[i], "pk_areacl") != null
            && m_card.getBillModel().getValueAt(iRows[i], "pk_areacl")
                .toString().length() > 0 && objAreaclname[index1] != null) {
          m_card.getBillModel().setValueAt(objAreaclname[index1].toString(),
              iRows[i], "areaclname");
          index1++;
        }
        if (m_card.getBillModel().getValueAt(iRows[i], "pk_arrivearea") != null
            && m_card.getBillModel().getValueAt(iRows[i], "pk_arrivearea")
                .toString().length() > 0 && objArriveareaname[index2] != null) {
          m_card.getBillModel().setValueAt(
              objArriveareaname[index2].toString(), iRows[i], "arriveareaname");
          index2++;
        }
      }
    }
  }

  /*
   * ����ִ�����崥���¼�
   */
  public void onOnHandClicked(ActionEvent e, OnHandResultVO[] vos) {
  }

  /*
   * �������˫����ʱ�򴥷�
   */
  public void onOnHandClicked(BillMouseEnent e, OnHandResultVO vo) {
    // TODO �Զ����ɷ������
    if (e == null || vo == null) {
      return;
    }
    int iRow = e.getRow();
    if (vo.getCcalbodyid() != null
        && vo.getCcalbodyid().equals(getCellValue(iRow, "ctakeoutcbid"))) {
      // �Ѳֿ���Ϣ����
      m_card.getBillModel().setValueAt(vo.getCwhid(), iRow, "ctakeoutwhid");
      m_card.getBillModel().setValueAt(vo.getAttributeValue("warehousename"),
          iRow, "ctakeoutwhname");
      // ��������Ϣ����
      m_card.getBillModel().setValueAt(vo.getVbatch(), iRow, "vbatch");
      try {
        afterBatchEdit(iRow);
      }
      catch (BusinessException be) {
        Debug.error(be.getMessage(), be);
        sendErrMsg(be.getMessage());
      }
      catch (Exception ex) {
        Debug.error(ex.getMessage(), ex);
      }
      // �ѻ�λ��Ϣ����
      m_card.getBillModel().setValueAt(vo.getAttributeValue("cspaceid"), iRow,
          "ctakeoutspaceid");
      m_card.getBillModel().setValueAt(vo.getAttributeValue("vspacename"),
          iRow, "ctakeoutspacename");
      // �����������������������Ҫ�Ѳֿ�ͻ�λ��Ϣ���Ƶ�������
      if (getHeadTailItemValue("ctakeoutcorpid") != null
          && getHeadTailItemValue("ctakeoutcorpid").equals(
              getHeadTailItemValue("coutcorpid"))) {
        TOBillTool.copyValueFromItem2Item(m_card, iRow, "ctakeoutwhid",
            "coutwhid");
        TOBillTool.copyValueFromItem2Item(m_card, iRow, "ctakeoutwhname",
            "coutwhname");
        TOBillTool.copyValueFromItem2Item(m_card, iRow, "ctakeoutspaceid",
            "coutspaceid");
        TOBillTool.copyValueFromItem2Item(m_card, iRow, "ctakeoutspacename",
            "coutspacename");
      }
    }
  }
}
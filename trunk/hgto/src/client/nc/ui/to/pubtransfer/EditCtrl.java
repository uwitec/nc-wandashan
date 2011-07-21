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

  // 控制card的引用。 为了减少参数的传递而加。
  private BillCardPanel m_card;

  private InvMeasRate m_invMeas = new InvMeasRate();

  private InvMeasRate m_invQuoteMeas = new InvMeasRate();

  // panel事件监听者。
  private GenPanelEventListener m_pelistener;

  private NodeInfo m_NodeInfo;
 
  private TransferClientUI ui;
  //
  private BillFillCtrl m_billFillCtrl;

  /**
   * EditCtrl 构造子注解。
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
      //调出出货公司是否相同，控制调出公司改变连带修改出货公司
      setIsSameOutTakeOut();
      
      boolean bEdit = true;
      String sKey = e.getItem().getKey();
      // 编辑表头的库存组织触发
      if (sKey.endsWith("cbid")) {
        BillItem btCurrent = CardPanelTool.getBillItemByPos(m_card,
            IBillItem.HEAD, sKey);
        String sCorpid = getHeadTailItemValue(getCorpRole(sKey) + "corpid");
        if (sCorpid == null || sCorpid.length() <= 0) {
          sCorpid = m_NodeInfo.getCorpID();
        }
        String sCondition = " and property <> "
            + String.valueOf(ConstVO.ICalbodyType_COST);
        // 执行过滤
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
      //二次开发扩展
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

  //调出出货公司是否相同，控制调出公司改变连带修改出货公司
  private boolean isSameOutTakeOut = false;
  
  /**
   * 方法功能描述：设置isSameOutTakeOut
   * //调出出货公司是否相同，控制调出公司改变连带修改出货公司
   * <b>参数说明</b>
   * @author sunwei
   * @time 2009-1-12 上午10:21:48
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
   * 编辑后事件。 创建日期：(2001-3-23 2:02:27)
   * 
   * @param e
   *          ufbill.BillEditEvent
   */
  public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
    Object editable = firePanelEvent(GenPanelEvent.ASK, new Integer(
        Const.IASK_CARD_EDITABLE));
    // 当前状态不是增加或者修改，应该不可以编辑
    if (!((Boolean) editable).booleanValue())
      return;

    sendHintMsg("");
    String sKey = e.getKey();
    int rowNumber = e.getRow();

    try {
      if (e.getKey().equals("crowno")) {
        // 行号
        nc.ui.scm.pub.report.BillRowNo.afterEditWhenRowNo(m_card, e, "crowno");
      }
      else if ("ntaxrate".equals(sKey)) {
        afterNTaxRateEdit(e);
        // afterTaxRateEdit(e);

        // 设置表体行颜色
        setModifiedRowColor(rowNumber, Color.YELLOW);
      }
      else if (sKey.indexOf("num") != -1 || sKey.indexOf("rate") != -1
          || sKey.indexOf("astnum") != -1 || sKey.indexOf("quoteunitnum") != -1) {
        chkNegativeNum(e);
      }
      if ("cinvcode".equals(sKey)) {
        afterInvEdit(e);
      }
      // 编辑公司、部门
      else if (sKey.endsWith("corpid")) {
        afterCorpUnitEdit(e);
      }
      // 编辑调入/调出/出货库存组织
      else if (sKey.endsWith("cbid")) {
        afterCalbodyEidt(e.getKey());
      }
      // 编辑仓库
      else if (sKey.endsWith("whname") || sKey.endsWith("whid")) {
        afterWhEdit(e);
      }
      // 编辑业务员
      else if (sKey.endsWith("psnname")) {
        afterPsnEdit(e);
      }
      else if (sKey.equals("nexchangeotobrate")) {
        afterRateEdit(e); // 编辑汇率
      }
      else if ("nnum".equals(sKey)) {// 编辑主数量
        afterNumEdit(e);
      }
      // 编辑辅数量
      else if ("nassistnum".equals(sKey) || "nquoteunitnum".equals(sKey)) {
        afterAstNumEdit(e);
      }
      // 编辑辅单位
      else if ("castunitname".equals(sKey)) {
        afterAstunitEdit(e);
      }
      // 编辑主单位
      else if ("cquoteunitname".equals(sKey)) {
        afterQuoteUnitEdit(e);
      }
      // 编辑换算率
      else if ("nchangerate".equals(sKey)) {
        afterChangeRateEdit(e);
      }
      // 编辑自由项
      else if ("vfree0".equals(sKey)) {
        afterFreeItemEdit(e);
      }
      // 编辑失效日期
      else if ("dvalidate".equals(sKey)) {
        afterValidateEdit(e);
      }
      // 编辑生产日期
      else if ("dproducedate".equals(sKey)) {
        afterProducedateEdit(e);
      }
      else if ("creceievename".equals(sKey)) {
        afterCustEdit(e);
      }
      else if ("nprice".equals(sKey)) {
        afterPriceTaxEdit(e);
        // afterPriceWTaxEdit(e);

        // 设置表体行颜色
        setModifiedRowColor(rowNumber, Color.YELLOW);
      }
      else if ("nnotaxprice".equals(sKey)) {
        afterPriceNoTaxEdit(e);
        // afterPriceWOTaxEdit(e);

        // 设置表体行颜色
        setModifiedRowColor(rowNumber, Color.YELLOW);
      }
      else if ("nnotaxmny".equals(sKey)) {
        afterMnyNoTaxEdit(e);

        // 设置表体行颜色
        setModifiedRowColor(rowNumber, Color.YELLOW);
      }
      else if ("nmny".equals(sKey)) {
        afterMnyTaxEdit(e);

        // 设置表体行颜色
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
      else if ("cprojectname".equals(sKey)) // 项目信息编辑以后触发
      {
        afterProjectEdit(e);
      }
      else if ("cprojectphasename".equals(sKey)) // 项目阶段信息编辑以后触发
      {
        afterProjectphaseEdit(e);
      }
      else if ("vbatch".equals(sKey)) {
        afterBatchEdit(e.getRow());
      }
      else if ("flargess".equals(sKey)) {
        afterFlargessEdit(e);
      }
      else if (sKey.startsWith("vdef"))// 表头自定义项编辑以后触发
      {
        DefSetTool.afterEditHead(m_card.getBillData(), sKey, "pk_defdoc"
            + sKey.substring(sKey.indexOf("vdef") + "vdef".length(), sKey
                .length()));
      }
      else if (sKey.startsWith("vbdef"))// 表体自定义项编辑以后触发
      {
        DefSetTool.batchDefEdit(m_card,e,"pk_defdoc","vbdef");
        
        DefSetTool.afterEditBody(m_card.getBillModel(), e.getRow(), sKey,
            "pk_defdoc"
                + sKey.substring(sKey.indexOf("vbdef") + "vbdef".length(), sKey
                    .length()));
      }
      // 检查编辑后是否仍然满足编辑前的检查条件! 针对批改时跳过了编辑前检查!
      m_billFillCtrl.checkAfterBodyFill(e);
      //二次开发扩展
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
    // 显示颜色为黄色的情况有两种：
    // 1, 未询到价格，且用户修改了价格
    // 2, 已经询出价格，且用户修改了询出的默认价格
    if ((nprice != null) && (!nprice.equals(naskprice))) {
      ArrayList<Integer> selectedRow = new ArrayList<Integer>();
      selectedRow.add(rowNumber);
      SetColor.setRowColor(m_card.getBodyPanel(), selectedRow, Color.YELLOW);
    }
  }

  /**
   * 折本汇率编辑事件
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
    // 考虑是否允许修改汇率，如果允许需要重新计算表体金额等，而询价会取默认汇率并转换为对应外币价格、金额等，是否有问题？
  }

  /**
   * 创建者： 功能：数量改变事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
   * 修改日期，修改人，修改原因，注释标志：
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

    // 主数量
    sMainNum = getCellValue(iRow, "nnum");
    if (isNull(sMainNum) || Double.valueOf(sMainNum).doubleValue() == 0d) {
      TOBillTool.clearRowValues(m_card, iRow, new String[] {
          "nnum", "nassistnum", "nmny", "nnotaxmny"
      });
      return;
    }
    dMainNum = Double.valueOf(sMainNum);

    // 改数量，算金额。因为有可能不询价，所以要主动换算金额
    calculateNumPriceMny(e);
    
    // 设置价格
    	setPrice(new int[] {iRow});
    
    setBretractFlag(dMainNum, iRow);
  }

  /**
   * 单价数量金额公共算法
   */
  private void calculateNumPriceMny(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {
    int iRow = e.getRow();

    // 设置是否报价计量单位固定换算率，数量单价金额公共方法使用
    setBquotefixrate(iRow);

    ClientCommonDataHelper cbo = new ClientCommonDataHelper();
    // 含税优先？非含税优先"TO0017";
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
      // 设置记税类别
      String[] strKeys = getnoKeys();
      try {
        strKeys[0] = getTaxtypeflag(iRow);
      }
      catch (Exception ex) {
        // 取不到计税类别取默认
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
        // 取不到计税类别取默认
        Log.error(ex);
      }
      RelationsCal.calculate(iRow, m_card, iaPrior, e.getKey(), nDescriptions,
          strKeys, BillItemVO.class.getName(), BillHeaderVO.class.getName());
    }

  }

  // 设置是否报价计量单位固定换算率，数量单价金额公共方法使用
  private void setBquotefixrate(int row) throws BusinessException {
    String sInvBasID = getCellValue(row, "cinvbasid");  
    String sQuoteMeasID = getCellValue(row, "cquoteunitid");//报价计量单位
    String sFixedFlag = InvInfo.getInvConvert(sInvBasID, sQuoteMeasID)[1];
    // 往界面上放数
    m_card.getBillModel().setValueAt(sFixedFlag, row, "bquotefixrate");
  }

  private void setBretractFlag(Double dMainNum, int iRow) {
    // 设置是否退回标记
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
   * 编辑前处理。 创建日期：(2001-3-23 2:02:27) 表头的编辑前事件不会触发此方法
   * 
   * @param e
   *          ufbill.BillEditEvent
   */
  public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
    try {
      // 通知监听者
      Object editable = firePanelEvent(GenPanelEvent.ASK, new Integer(
          Const.IASK_CARD_EDITABLE));
      // 当前状态不是增加或者修改，应该不可以编辑
      if (!((Boolean) editable).booleanValue())
        return false;
      m_card.stopEditing();
      String sKey = e.getKey();

      if (isRetBill(e.getRow())) {
        // 退回时只有数量可编辑
        if (!sKey.equals("nassistnum") && !sKey.equals("nnum")) {
          return false;
        }
      }
      else if ("ntaxmny".equals(sKey)) {
        return false;
      }
      // 编辑自由项
      else if ("vfree0".equals(e.getKey())) {
        return beforeVfree0Edit(e);
      }
      /*
       * else if ("castunitname".equals(e.getKey())) { return
       * beforeAstUnitEdit(e); }
       */
      // 设置项目阶段
      else if ("cprojectphasename".equals(sKey)) {
        return beforeProjectPhaseEdit(e);
      }
      // 设置项目
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
      // 编辑存货
      else if ("cinvcode".equals(sKey)) {
        return beforeInvEdit(e);
      }
      // 编辑辅单位
      else if ("castunitname".equals(sKey) || "cquoteunitname".equals(sKey)) {
        return beforeAstUnitEdit(e);
      }
      // 编辑辅数量
      else if ("nassistnum".equals(sKey) || "nquoteunitnum".equals(sKey)) {
        return beforeAstNumEdit(e);
      }
      // 编辑换算率
      else if ("nchangerate".equals(sKey)) {
        return beforeChangeRateEdit(e);
      }
      // 编辑数量
      else if ("nnum".equals(sKey)) {
        return beforeNumEdit(e);
      }
      // 编辑批次号
      else if ("vbatch".equals(sKey)) {
        return beforeBatchEdit(e);
      }
      else if ("dvalidate".equals(sKey)) {
        return beforeValidateEdit(e);
      }
      else if ("dproducedate".equals(sKey)) {
        return beforeProduceDateEdit(e);
      }
      // 编辑发运方式
      else if ("sendtypename".equals(sKey)) {
        return beforeSendTypeEdit(e);
      }
      // 编辑收货客户
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
      // 编辑税率
      else if ("ntaxrate".equals(sKey)) {
        return beforeTaxRateEdit(e);
      }
      // 加价率只能在调拨关系中修改
      else if ("naddpricerate".equals(sKey)) {
        return false;
      }
      else if (sKey.indexOf("price") >= 0
          && !(sKey.equals("naskprice") || sKey.equals("nasknotaxprice"))) {
        return beforePriceEdit(e);
      }
      else if (sKey.equals("naskprice") || sKey.equals("nasknotaxprice")) {
        return false;// 询价含税价用来保存询价记录，不可编辑
      }
      else if (sKey.equals("nmny") || sKey.equals("nnotaxmny")) {
        return beforePriceEdit(e);
      }
      else if (sKey.startsWith("vbdef"))// 表体自定义项编辑以前触发
      {
        if (getBodyItem(sKey).getDataType() == IBillItem.USERDEF) {
          UIRefPane ref = (UIRefPane) getBodyItem(sKey).getComponent();

          if (ref == null) {
            return true;
          }
          // 防止统计类型的自定义参照不能被编辑，强制让其获得焦点
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
      // 组织间、组织内，不可修改
      else if (sKey.indexOf("norgqttaxnetprc") >= 0
          || sKey.indexOf("norgqtnetprc") >= 0) {
        return !isInSameCorp();
      }
      //二次开发扩展
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
   * 行改变事件。 创建日期：(2001-3-23 2:02:27)
   * 
   * @param e
   *          ufbill.BillEditEvent
   */
  public void bodyRowChange(nc.ui.pub.bill.BillEditEvent e) {
    int iRow = e.getRow();
    showInvAmount(iRow);
    firePanelEvent(GenPanelEvent.ASK,
        new Integer(Const.IASK_CARD_BODYROWCHANGE));
    //二次开发扩展
    ui.getPluginProxy().bodyRowChange(e);
  }

  /**
   * 计算合计。 创建日期：(2001-10-24 16:33:58)
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
   * 创建日期：(2004-2-12 16:10:58) 作者：王乃军 参数： 返回： 说明：设置节点信息
   * 
   * @param newNodeInfo
   *          nc.ui.scm.pub.templet.NodeInfo
   */
  public void setNodeInfo(NodeInfo newNodeInfo) {
    m_NodeInfo = newNodeInfo;
  }

  private nc.ui.ic.pub.lot.LotNumbRefPane m_LotNumbRefPane = null; // 批次参照

  private nc.ui.scm.ic.freeitem.FreeItemRefPane m_refFreeItem = null; // 自定义项参照

  /**
   * 创建者： 功能：辅计量改变事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
   * 修改日期，修改人，修改原因，注释标志：
   */
  private void afterAstunitEdit(nc.ui.pub.bill.BillEditEvent e)
      throws BusinessException {

    int iRow = e.getRow();
    Double dMainNum = null;
    String sAstNum = null;
    Double dAstNum = null;
    Double dQuoteNum = null;
    UFDouble dRate = null;
    // 设置辅计量标识
    Object oTemp1 = e.getValue();
    // 取辅单位
    BillItem bt = getBodyItem("castunitname");
    UIRefPane refAstUnit = (UIRefPane) bt.getComponent();
    Object oMeaPK = refAstUnit.getRefPK();
    if (isNull(oTemp1) || isNull(oMeaPK)) {
      // 将辅计量数据清空
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
                                             * "请到存货管理档案确认:本行存货的主辅计量单位换算率是否为空"
                                             */);

    }
    sAstNum = getCellValue(iRow, "nassistnum");
    String sMainNum = getCellValue(iRow, "nnum");
    if (!isNull(sAstNum)) {
      /*
       * dAstNum = Double.valueOf(sAstNum); // 主数量=辅计量数量x换算率 dMainNum = new
       * Double(dAstNum.doubleValue() * dRate.doubleValue());
       */
      dMainNum = Double.valueOf(sMainNum);
      dAstNum = new Double(dMainNum.doubleValue() / dRate.doubleValue());
      m_card.getBillModel().setValueAt(dAstNum, iRow, "nassistnum");
      // 主数量变化，报价单位数量也要变化(报价单位数量=主数量/报价计量单位换算率)
      String sQuoteRate = getCellValue(iRow, "nquoteunitrate");// 报价计量单位换算率
      if (sQuoteRate != null && sQuoteRate.length() > 0) {
        dQuoteNum = new Double(dMainNum.doubleValue()
            / Double.parseDouble(sQuoteRate));
      }
      // 计算总金额
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
   * 报价单位变化，主数量变化，从而引起辅数量变化
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
                                             * "请到存货管理档案确认:本行存货的主辅计量单位换算率是否为空"
                                             */);

    }
    m_card.getBillModel().setValueAt(dRate, iRow, "nquoteunitrate");
    String sNum = getCellValue(iRow, "nnum");
    if(!isNull(sNum)){
      Double dQuoteunitNum = new Double(Double.parseDouble(sNum)
                / dRate.doubleValue());
       m_card.getBillModel().setValueAt(dQuoteunitNum.toString(), iRow, "nquoteunitnum");
    }
    
//    // 换算率变了，重新计算主数量
//    String sQuoteunitNum = getCellValue(iRow, "nquoteunitnum");
//    if (!isNull(sQuoteunitNum)) {
//      Double dMainNum = new Double(Double.parseDouble(sQuoteunitNum)
//          * dRate.doubleValue());
//      m_card.getBillModel().setValueAt(dMainNum.toString(), iRow, "nnum");
//
//      // 主数量变了,重新计算辅数量
//      String sPK_AstUnit = getCellValue(iRow, "castunitid");
//      if (!isNull(sPK_AstUnit)) {
//        String sChangeRate = getCellValue(iRow, "nchangerate");
//        String sAstNum = getCellValue(iRow, "nassistnum");
//        String sInvBasID = getCellValue(iRow, "cinvbasid");
//        String sMeasID = InvInfo.getPk_measdoc(sInvBasID);
//        String sFixedFlag = sMeasID.equals(sPK_AstUnit) ? "Y" : InvInfo
//            .getInvConvert(sInvBasID, sPK_AstUnit)[1];
//        // 当换算率为固定时
//        if (sFixedFlag.equals("Y")) {
//          Double dAstNum = new Double(dMainNum.doubleValue()
//              / Double.parseDouble(sChangeRate));
//          m_card.getBillModel().setValueAt(dAstNum.toString(), iRow,
//              "nassistnum");
//        }
//        // 非固定换算率，变动换算率，辅数量不变
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
    
    // 修改报价计量单位触发询价
    setPrice(new int[] {
      iRow
    });
  }

  /**
   * 创建者：王乃军 功能：自由项改变事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
   * 修改日期，修改人，修改原因，注释标志：
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
      // 勾选“赠品”后，存货的价格信息、加价率清空，且不允许编辑，即金额只会为0
      clearPrice(new int[] {
        iRow
      });
    }
    else {
      // 清除“赠品”后，重新询价
      setPrice(new int[] {
        iRow
      });
    }
  }

  /**
   * 存货编辑后事件处理 创建日期：(2004-3-4 16:13:17)
   * 
   * @param paramBillEditEvent
   */
  private void afterInvEdit(nc.ui.pub.bill.BillEditEvent e) throws Exception {

    // GenTimer time = new GenTimer();
    // GenTimer time2 = new GenTimer();
    // time2.start("带出存货开始：");
    // time.start();
    // 避免反复计算合计影响效率
    m_card.getBillModel().setNeedCalculate(false);
    int[] iaRow = null;

    int iRow = e.getRow();
    // 存货编码
    BillItem bt = getBodyItem("cinvcode");
    UIRefPane refInv = (UIRefPane) bt.getComponent();
    // 存货管理id
    String[] saOutInvID = refInv.getRefPKs();
    if (isNull(saOutInvID)
        || (e.getOldValue()!=null&&!saOutInvID[0].equals(((DefaultConstEnum)e.getOldValue()).getValue()))) {
      // 如果存货为空或不等于前次存货，清空前次的存货信息
      clearInvInfo(iRow);
    }
    if (isNull(saOutInvID)) {
      return;
    }

    // 公司id、库存组织id
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
    // 检查存货
    String[] saInvBasID = InvInfo.getInvBasID(saOutInvID);
    // 选取了多行存货时
    boolean isLastRow = false;
    // 判断编辑的是否最后一行
    if (m_card.getRowCount() - 1 == e.getRow()) {
      isLastRow = true;
    }
    else {
      // 如果编辑的不是最后一行，选中当前行的下一行，以便在它前面插入行
      m_card.getBillTable().setRowSelectionInterval(iRow + 1, iRow + 1);
    }
    iaRow = new int[saOutInvID.length];
    // 如果所选存货多于1行，需要增加n-1行
    for (int i = 0, len = saOutInvID.length; i < len; i++) {
      if (i > 0) {
        // 如果编辑的是最后一行，需要addLine
        if (isLastRow) {
          /*nc.ui.pub.bill.BillCardPanel.addLine()方法会触发nc.ui.to.pubtransfer.TransferClientUI.onEditAction(int action)产生循环调用
          此处先去掉监听，再增行，再加载监听，这样就不会调用nc.ui.to.pubtransfer.TransferClientUI.onEditAction(int action)
          因为nc.ui.to.pubtransfer.CardPanelCtrl.addLine()中的nc.ui.to.pubtransfer.EditCtrl.initMultiNewLine(int[] iaRow)调用不可去掉
          如有循环调用，每增一行initMultiNewLine(int[] iaRow)会调用两次远程连接；此处批量执行initMultiNewLine(int[] iaRow)减少远程连接*/
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
        // 如果编辑的是中间行，需要插入行
        else {
          m_card.insertLine();
        }
      }
      iaRow[i] = iRow + i;
    }
    initMultiNewLine(iaRow,true);
    // 为新增行补上行号
    if (isLastRow) {
      nc.ui.scm.pub.report.BillRowNo.addLineRowNos(m_card, m_NodeInfo
          .getOrderTypeCode(m_card), "crowno", saOutInvID.length);
    }
    else {
      // iEndRow = iRow+sPKs.length,意思是当前行的下一行增行以后的位置
      nc.ui.scm.pub.report.BillRowNo.insertLineRowNos(m_card, m_NodeInfo
          .getOrderTypeCode(m_card), "crowno", iRow + saOutInvID.length,
          saOutInvID.length - 1);
    }

    HashMap hmInvManID = null;
    try {
      // 检查存货是否分配到相应的公司
      hmInvManID = BusinessCheck.invManDocChk(saInvBasID, new String[] {
          sInCorpID, sOutCorpID, sTakeOutCorpID
      });
    }
    catch (Exception ex) {
      // 如果存货为空或不等于前次存货，清空前次的存货信息
      clearInvInfo(iRow);
      throw ex;
    }

    // time.showExecTime("校验存货所属公司，组织");
    // 存货信息改成批取
    HashMap hmParam = InvInfo.getMngInfo(saInvBasID, saOutInvID);
    // time.showExecTime("取存货信息");
    StringBuffer sbKey = new StringBuffer();
    ParamVO param = null;

    for (int ctr = 0; ctr < saOutInvID.length; ctr++) {
      // 存货管理id
      String sOutInvID = saOutInvID[ctr];
      // 存货基本id
      String sInvBasID = saInvBasID[ctr];
      sbKey.setLength(0);
      sbKey = sbKey.append(sInvBasID).append(sOutInvID);
      param = (ParamVO) hmParam.get(sbKey.toString());
      if (param.bLaborFlag || param.bDiscountFlag)
        throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40093010", "UPP40093010-000023")/*
                                                           * @res
                                                           * "不可选取为劳务或价格折扣的存货"
                                                           */);

      // 存货管理id
      String sInInvID = hmInvManID.get(sInvBasID + sInCorpID).toString();
      String sTakeOutInvID = hmInvManID.get(sInvBasID + sTakeOutCorpID)
          .toString();

      // 设置本行的存货信息
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
      hmValue.put("bquotefixrate", sFixedFlag);// 设置是否报价计量单位固定换算率，数量单价金额公共方法使用
      String[] saFreeItem = InvInfo.getFreeItem(sInvBasID);
      if (saFreeItem[saFreeItem.length - 1].equals("N")) {
        hmValue.put("vfree0", "");
      }
      // 往界面上放数
      TOBillTool.setRowValue(m_card, hmValue, iRow + ctr);
    }
    // time.showExecTime("往界面放数");
    // 设置每行的价格
    try {
      // 修改或新增存货，永远触发询价
      getPrice(iaRow);

      // time.showExecTime("询价：");
      // 查询atp和sp
      BillEditEvent event = new BillEditEvent(m_card, e.getOldRow(), iRow);
      bodyRowChange(event);
      // time.showExecTime("查询atp：");

      // time2.showExecTime("带出存货结束：");
    }
    catch (Exception ex) {
      throw ex;
    }
    finally {
      // 把合计的开关打开
      m_card.getBillModel().setNeedCalculate(true);
    }

    /*
     * 因为编辑公式是在afterEdit方法之前执行的，而cinvbasid等存货相关的信息是在afterEdit方法中赋值的。
     * 如果需要使用cinvbasid等信息定义公式，cinvbasid是得不到值得。 所以需要在afterEdit之后强制调用编辑公式。
     * 目前只处理存货，如其它字段(自由项等)需要则再相应afterEdit方法中进行补充。
     */
    for (int row : iaRow) {
      BillItem item = m_card.getBodyItem(e.getKey());
      String[] formulas = item.getEditFormulas();
      m_card.execBodyFormulas(row, formulas);
    }
  }

  /**
   * ****************************************** 功能： <|> 创建日期：(2004-3-11
   * 20:58:45)
   * 
   * @param：
   * @return： ******************************************
   */
  public boolean beforeAstUnitEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    int iRow = e.getRow();

    Object oInvID = getCellValue(iRow, "cinvbasid");
    if (isNull(oInvID)) {
      return false;
    }
    // 是否辅计量管理
    UFBoolean bAstMng = InvInfo.getAstMngInfo(new String[] {
      oInvID.toString()
    })[0];
    if (bAstMng.booleanValue() == false) {
      sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
          "UPP40093010-000024")/* @res "当前存货不是辅计量管理" */);
      return false;
    }
    // 取辅单位
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
      // //设置辅计量单位参照
      // ((nc.ui.to.pub.AssunitRefmodel) refAstUnit.getRefModel())
      // .setInvs(oInvID.toString());
    }
    return true;

  }

  /**
   * 创建者： 功能：表体批次号录入之前的处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
   * 修改日期，修改人，修改原因，注释标志：
   */
  public boolean beforeBatchEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    sendHintMsg("");
    int iRow = e.getRow();
    String sInvBasID = getCellValue(iRow, "cinvbasid");
    // 出货存货管理id
    String sOutInvID = getCellValue(iRow, "ctakeoutinvid");
    // 出货仓库
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
      
      //读取配置文件..modules\scmpub\scmcustomer\customerconfig.xml
      if (m_NodeInfo.getProjectName()
          .equalsIgnoreCase(nc.vo.scm.pub.CustomerConfigVO.NAME_FSBIGUIYUAN)) {
        m_LotNumbRefPane.setIsMutiSel(true);//zlq 佛山碧桂园
      }      
    }
    // 是否输入存货
    if (isNull(sInvBasID) || isNull(sWhID) || isNull(sOutInvID)) {
      if (orderTypeCode.equals(ConstVO.m_sBillSFDBDD)) {
        throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40093010", "UPP40093010-000025")/*
                                                           * @res
                                                           * "请先输入存货及出货仓库，再输入批次！"
                                                           */);
      }
      else {
        throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40093010", "UPP40093010-000414")/*
                                                           * @res
                                                           * "请先输入存货及调出仓库，再输入批次！"
                                                           */);
      }
    }

    String sOutCbID = getHeadTailItemValue("ctakeoutcbid");
    String sOutCorpID = getHeadTailItemValue("ctakeoutcorpid");
    ParamVO paramResult = InvInfo.getBatchMngInfo(sOutInvID);
    // 是否批次管理
    if (!paramResult.bLotMngFlag.booleanValue()) {
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "SCMCOMMON", "UPPSCMCommon-000102")/*
                                               * @res "非批次管理存货!"
                                               */);
    }

    // 调用公式解析存货属性
    InvoInfoBYFormula f = new InvoInfoBYFormula();
    // 向批次参照传入存货信息
    InvVO voInv = f.getInvVO(sOutInvID, sOutCbID, sWhID, sOutCorpID);
    // 设置批次管理标志
    voInv.setIsLotMgt(new Integer(1));
    // 设置辅计量
    voInv.setCastunitid(getCellValue(iRow, "castunitid"));
    voInv.setCastunitname(getCellValue(iRow, "castunitname"));
    // 设置计量单位
    voInv.setMeasdocname(getCellValue(iRow, "measname"));
    setBodyFreeValue(iRow, voInv);
    // 得到WhVO
    WhVO voWh = new WhVO();
    Object[][] obj2 = nc.ui.scm.pub.CacheTool.getMultiColValue("bd_stordoc",
        "pk_stordoc", new String[] {
            "storcode", "storname", // 名称
            "gubflag", // 是否废品库
            "csflag" // 是否货位管理
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
    // 设置库存组织名称
    BillItem bt2 = m_card.getHeadItem("coutcbid");
    UIRefPane pane = (UIRefPane) bt2.getComponent();
    voWh.setVcalbodyname(pane.getRefName());
    // 设置批次参照的仓库信息
    m_LotNumbRefPane.setParameter(voWh, voInv);
    return true;
  }

  /**
   * 创建日期：(2004-2-18 11:40:15) 作者：王乃军 参数： 返回： 说明： 自由项修改前的控制。 为true时才可编辑。
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
                                             * @res "请先输入存货信息，再输入自由项信息！"
                                             */);
    }

    // 是否为自由项管理的存货。
    String[] saFreeItem = InvInfo.getFreeItem(sInvBasID);
    if (saFreeItem[saFreeItem.length - 1].equals("Y")) {
      // 向自由项参照传入存货信息
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
      // 自由项不可编辑
      // 清空自由项信息
      for (int i = 1; i <= 5; i++) {
        m_card.getBillModel().setValueAt(null, iRow, "vfreeid" + i);
        m_card.getBillModel().setValueAt(null, iRow, "vfree" + i);
      }
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "SCMCOMMON", "UPPSCMCommon-000103")/*
                                               * @res "非自由项管理存货!"
                                               */);
    }
  }

  /*****************************************************************************
   * 功能：卡片模板向ClientUI发送消息 创建日期：(2004-3-13 10:50:10)
   * 
   * @param：
   * @return： ******************************************
   */
  protected void sendHintMsg(String sMsg) {
    firePanelEvent(GenPanelEvent.HINT, sMsg);
  }

  /*****************************************************************************
   * 功能：卡片模板向ClientUI发送消息 创建日期：(2004-3-13 10:50:10)
   * 
   * @param：
   * @return： ******************************************
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
   * 创建日期：(2004-2-10 18:20:00) 作者：王乃军 参数： 返回： 说明： 配置panel event 监听
   */
  public void addPanelEventListener(GenPanelEventListener l) {
    m_pelistener = l;
  }

  /**
   * 功能：辅数量/报价单位数量改变事件处理 辅数量/报价单位数量改变,引起主数量改变,从而引起报价单位数量/辅数量改变 参数：e单据编辑事件 返回：
   * 例外： 日期：(2001-5-8 19:08:05) 修改日期，修改人，修改原因，注释标志：
   */
  private void afterAstNumEdit(nc.ui.pub.bill.BillEditEvent e) throws Exception {

    int iRow = e.getRow();
    String sInvBasID = getCellValue(iRow, "cinvbasid");
    String sOutInvID = getCellValue(iRow, "coutinvid");
    if (isNull(sInvBasID) || isNull(sOutInvID)) {
      return;
    }

    // 改数量，算金额。因为有可能不询价，所以要主动换算金额
    calculateNumPriceMny(e);
    
    // 设置价格
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
   * 创建者： 功能：表体批次号编辑的处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
   * 修改日期，修改人，修改原因，注释标志：
   */
  private void afterBatchEdit(int iRow) throws Exception {
    // 库存批次号档案字段
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
    // 批次号档案字段在调拨订单单据模板上的itemkey
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

    ////清空批次
    if (isNull(sTemp) || isNull(sInvID) || isNull(sOutInvID)) {
      // 清空批次号相关字段
      TOBillTool.clearRowValues(m_card, iRow, sTargetFields);

      // 重新询价
      setPrice(new int[] {
        iRow
      });
      
      return;
    }

    // 批次参照
    LotNumbRefPane ref = (LotNumbRefPane) getBodyItem("vbatch").getComponent();
    ref.setAutoCheck(true);
    
    //读取配置文件..modules\scmpub\scmcustomer\customerconfig.xml
    if (m_NodeInfo.getProjectName()
        .equalsIgnoreCase(nc.vo.scm.pub.CustomerConfigVO.NAME_FSBIGUIYUAN)) {
      //如果是碧桂圆项目，编辑批次，自动带出自由项
      setFreeAfterBatchEdit(iRow,ref);
    }
  
    boolean bExist = ref.checkData();
    if (!bExist) {
      // 清空批次号相关字段
      TOBillTool.clearRowValues(m_card, iRow, sTargetFields);
      // 清空价格
      clearPrice(new int[] {
        iRow
      });
      return;
    }
    // 获取批次档案信息
    BatchcodeVO bcvo = BatchCodeDefSetTool.getBatchCodeInfo(sOutInvID, sTemp,
        sOutCorpID);
    if (bcvo != null) {
      for (int i = 0; i < sBatchFields.length; i++) {
        m_card.getBillModel().setValueAt(
            bcvo.getAttributeValue(sBatchFields[i]), iRow, sTargetFields[i]);
      }
    }

    // 重新询价
    setPrice(new int[] {
      iRow
    });
  }

  private void setFreeAfterBatchEdit(int iRow, LotNumbRefPane ref) {
    String sOutInvID = getCellValue(iRow, "ctakeoutinvid");
    String sOutCorpID = getCellValue(iRow, "ctakeoutcorpid");
    //  zlq 佛山碧桂园 调拨订单录入批次时，同库存材料出库单操作界面一样，可以通过选择批次号，带出自由项档案
    ////////////////////////////////////////////////////////////////////////////////////////////
    // 出货仓库
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

      // 在批次参照时查询出自由项信息然后设置到批次参照中
      WhVO voWh = new WhVO();
      Object[][] obj2 = nc.ui.scm.pub.CacheTool.getMultiColValue("bd_stordoc",
          "pk_stordoc", new String[] {
          "storcode", "storname", // 名称
          "gubflag", // 是否废品库
          "csflag" // 是否货位管理
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
      //设置库存组织名称
      BillItem bt2 = m_card.getHeadItem("coutcbid");
      UIRefPane pane = (UIRefPane) bt2.getComponent();
      voWh.setVcalbodyname(pane.getRefName());
      //设置批次参照的仓库信息
      m_LotNumbRefPane.setParameter(voWh, voInv);

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////

  
  }

  /**
   * 创建者： 功能：辅计量改变事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
   * 修改日期，修改人，修改原因，注释标志：
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
      // 将辅计量数据清空
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
          "UPP40093010-000028")/* @res "换算率不能为空或0" */);
      return;
    }

    // 辅计量数量=主数量/换算率
    dAstNum = dMainNum.div(dRate);
    // 报价单位数量=主数量/报价单位换算率
    sQuoterate = getCellValue(iRow, "nquoteunitrate");
    if (!isNull(sQuoterate)) {
      dQuoteNum = dMainNum.div(new UFDouble(sQuoterate));
    }

    // 计算总金额
    HashMap hmValue = new HashMap(4);

    hmValue.put("nquoteunitnum", isNull(dQuoteNum) ? "" : dQuoteNum.toString());
    hmValue.put("nassistnum", isNull(dAstNum) ? "" : dAstNum.toString());
    TOBillTool.setRowValue(m_card, hmValue, iRow);

  }

  /**
   * ****************************************** 功能： <|> 创建日期：(2004-3-15
   * 21:57:54)
   * 
   * @param：
   * @return： ******************************************
   */
  private void afterCurrTypeEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    String sOutCorpID = getHeadTailItemValue("coutcorpid");
    String sCurrType = getHeadTailItemValue("coutcurrtype");
    freshScale(sOutCorpID, sCurrType);
    setRate(sCurrType, sOutCorpID);
    BusinessCurrencyRateUtil currArith = getBizCheck().getCurrArith(sOutCorpID);
    setRateEdit(false, sOutCorpID, sCurrType);

    // 重新取表体行的价格
    int iCount = m_card.getBillModel().getRowCount();
    if (iCount > 0) {
      int[] iaRow = new int[iCount];
      boolean bHasBody = false;
      for (int i = 0; i < iCount; i++) {
        // 表体是否输入了存货
        if (isNull(getCellValue(i, "cinvcode"))) {
          continue;
        }
        iaRow[i] = i;
        bHasBody = true;
      }
      // 再取价
      if (bHasBody) {
        setPrice(iaRow);
      }
    }

  }

  /**
   * ****************************************** 功能： <|> 创建日期：(2004-3-15
   * 21:57:54)
   * 
   * @param：
   * @return： ******************************************
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

    // 带出到货地址，收货地区，收货地点
    // 到货地址
    UIRefPane refAddress = (UIRefPane) getBodyItem("vreceiveaddress")
        .getComponent();
    if (refAddress != null) {
      refAddress.setAutoCheck(false);
      refAddress.setReturnCode(false);
      // 到货地址参照
      ((nc.ui.scm.ref.prm.CustAddrRefModel) refAddress.getRefModel())
          .setCustId(sCustManID);
      // 客户基本档案ID
      Object[] obj0 = (Object[]) CacheTool.getCellValue("bd_cumandoc",
          "pk_cumandoc", "pk_cubasdoc", sCustManID);
      String sPk_cubasdoc = obj0[0].toString();
      m_card.getBillModel().setValueAt(sPk_cubasdoc, iRow, "creceiverbasid");
      // 当前收货客户的默认到货地址
      Object[][] obj1 = CacheTool.getAnyValue2("bd_custaddr", "pk_custaddr",
          " defaddrflag='Y' and pk_cubasdoc='" + sPk_cubasdoc + "'");
      if (obj1 != null && obj1[0][0] != null) {
        refAddress.setPK(obj1[0][0].toString());
      }
      else {
        refAddress.setPK(null);
      }
      if (refAddress.getRefPK() != null) {
        // 当前收货客户的默认到货地址
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
      // 扣税名称、类别
      SCMRelationsCal.DISCOUNT_TAX_TYPE_NAME,
      // 是否固定换算率
      SCMRelationsCal.IS_FIXED_CONVERT,
      // 税率
      SCMRelationsCal.TAXRATE,

      // 数量
      SCMRelationsCal.NUM,
      SCMRelationsCal.NUM_ASSIST,
      SCMRelationsCal.CONVERT_RATE,

      // 主计量原币价格、金额
      SCMRelationsCal.NET_PRICE_ORIGINAL,// 内部交易的含税单价映射到公共算法的含税净价上，默认折扣为100％
      SCMRelationsCal.NET_TAXPRICE_ORIGINAL, SCMRelationsCal.MONEY_ORIGINAL,
      SCMRelationsCal.SUMMNY_ORIGINAL, SCMRelationsCal.TAX_ORIGINAL,

      // 报价单位使用
      SCMRelationsCal.QT_NET_PRICE_ORIGINAL,
      SCMRelationsCal.QT_NET_TAXPRICE_ORIGINAL,
      // SCMRelationsCal.QT_NET_PRICE_LOCAL,//报价单位无税净价
      // SCMRelationsCal.QT_NET_TAXPRICE_LOCAL,//报价单位含税净价
      SCMRelationsCal.QT_NUM, SCMRelationsCal.QT_CONVERT_RATE,// 报价单位换算率
      SCMRelationsCal.QT_IS_FIXED_CONVERT,// 是否报价固定换算率
      SCMRelationsCal.QUOTEUNITID,// 报价计量单位

      // 计算精度使用
      SCMRelationsCal.CURRTYPEPk,// 原币PK
      SCMRelationsCal.PK_CORP,
  };

  public static String[] getKeys() {
    return new String[] {
        // 扣税名称、类别
        // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009","UPP40093009-000046")/*@res
        // "应税外加"*/,// TODO 根据记税类别设置
        "应税外加", /*-=notranslate=-*/
        "Y",
        // 税率、
        "ntaxrate",

        // 数量
        "nnum", "nassistnum", "nchangerate",

        // 主计量原币价格、金额
        "nnotaxprice", "nprice", "nnotaxmny", "nmny", "ntaxmny",

        "norgqtnetprc",// 报价单位无税净价
        "norgqttaxnetprc",// 报价单位含税净价
        "nquoteunitnum", "nquoteunitrate", "bquotefixrate", "cquoteunitid",

        "coutcurrtype", "coutcorpid",
    };
  }

  public static String[] getnoKeys() {
    return new String[] {
        // 扣税名称、类别
        // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009","UPP40093009-000046")/*@res
        // "应税外加"*/,// TODO 根据记税类别设置
        "应税外加", /*-=notranslate=-*/
        "N",
        // 税率、
        "ntaxrate",

        // 数量
        "nnum", "nassistnum", "nchangerate",

        // 主计量原币价格、金额
        "nnotaxprice", "nprice", "nnotaxmny", "nmny", "ntaxmny",

        "norgqtnetprc",// 报价单位无税净价
        "norgqttaxnetprc",// 报价单位含税净价
        "nquoteunitnum", "nquoteunitrate", "bquotefixrate", "cquoteunitid",

        "coutcurrtype", "coutcorpid",
    };
  }

  /**
   * ****************************************** 功能： <|> 创建日期：(2004-3-15
   * 21:57:54)
   * 
   * @param：
   * @return： ******************************************
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
    // 含税优先？非含税优先"TO0017";
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

    // 设置记税类别
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

    // 设置记税类别
    String[] strKeys = getKeys();
    strKeys[0] = getTaxtypeflag(iRow);
    RelationsCal.calculate(iRow, m_card, iaPrior, e.getKey(), nDescriptions,
        strKeys, BillItemVO.class.getName(), BillHeaderVO.class.getName());

  }

  /**
   * ****************************************** 功能： <|> 创建日期：(2004-3-15
   * 21:57:54)
   * 
   * @param：
   * @return： ******************************************
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

    // 设置记税类别
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
      //5I、5E不匹配调拨关系，这时要求计税类别默认应税外加
      return "应税外加";
    }
    Integer iTemp = voTransItem[0].getTaxtypeflag();
    switch (iTemp.intValue()) {
      case ConstVO.TAX_TYPE_IN:
        return "应税内含"; /*-=notranslate=-*/
        // return
        // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009","UPP40093009-000258")/*@res
        // "应税内含"*/;
      case ConstVO.TAX_TYPE_OUT:

        return "应税外加"; /*-=notranslate=-*/
        // return
        // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009","UPP40093009-000257")/*@res
        // "应税外加"*/;
      case ConstVO.TAX_TYPE_NULL:
        String flag = getHeadTailItemValue("bdrictflag");
        if (flag != null && flag.length() > 0 && flag.equals("true")) {
          return "应税外加";
        }
        return "不计税"; /*-=notranslate=-*/
        // return
        // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009","UPP40093009-000259")/*@res
        // "不计税"*/;
      default:
        return "不计税"; /*-=notranslate=-*/
        // return
        // nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009","UPP40093009-000259")/*@res
        // "不计税"*/;
    }
  }

  /**
   * ****************************************** 功能： <|> 创建日期：(2004-3-15
   * 21:57:54)
   * 
   * @param：
   * @return： ******************************************
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

    // 设置记税类别
    String[] strKeys = getKeys();
    strKeys[0] = getTaxtypeflag(iRow);

    RelationsCal.calculate(iRow, m_card, iaPrior, e.getKey(), nDescriptions,
        strKeys, BillItemVO.class.getName(), BillHeaderVO.class.getName());

  }

  /**
   * ****************************************** 功能： <|> 创建日期：(2004-3-15
   * 21:57:54)
   * 
   * @param：
   * @return： ******************************************
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

    // 设置记税类别
    String[] strKeys = getKeys();
    strKeys[0] = getTaxtypeflag(iRow);
    RelationsCal.calculate(iRow, m_card, iaPrior, e.getKey(), nDescriptions,
        strKeys, BillItemVO.class.getName(), BillHeaderVO.class.getName());

  }

  /**
   * 功能： <|>报价计量单位无税净价编辑后 创建日期：(2004-3-15 21:57:54)
   * 
   * @param：
   * @return： ******************************************
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

    // 根据“计税类别”来计算“含税单价”：
    switch (iTemp.intValue()) {
      // 应税内含--- “含税单价”= “非含税单价”/（1-税率）
      case ConstVO.TAX_TYPE_IN:
        dTaxRate = sTemp == null ? voTransItem.getTaxrate() : new UFDouble(
            sTemp);
        dTaxRate = dTaxRate.div(100);
        dQuotePriceWTax = dQuotePriceWOTax.div(new UFDouble(1).sub(dTaxRate));
        break;
      // 应税外加--- “含税单价”= “非含税单价”*（1+税率）
      case ConstVO.TAX_TYPE_OUT:
        dTaxRate = sTemp == null ? voTransItem.getTaxrate() : new UFDouble(
            sTemp);
        dTaxRate = dTaxRate.div(100);
        dQuotePriceWTax = dQuotePriceWOTax.multiply(new UFDouble(1)
            .add(dTaxRate));
        break;
      // 不计税--- “含税单价”= “非含税单价”
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
   * ****************************************** 功能： <|> 创建日期：(2004-3-15
   * 21:57:54)
   * 
   * @param：
   * @return： ******************************************
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

    // 根据“计税类别”来计算“非含税单价”：
    switch (iTemp.intValue()) {
      // 应税内含--- “含税单价”= “非含税单价”/（1-税率）
      case ConstVO.TAX_TYPE_IN:
        dTaxRate = sTaxrate == null ? voTransItem.getTaxrate() : new UFDouble(
            sTaxrate);
        dTaxRate = dTaxRate.div(100);
        dQuotePriceWOTax = dQuotePriceWTax.multiply(new UFDouble(1)
            .sub(dTaxRate));
        break;
      // 应税外加--- “含税单价”= “非含税单价”*（1+税率）
      case ConstVO.TAX_TYPE_OUT:
        dTaxRate = sQuoteRate == null ? voTransItem.getTaxrate()
            : new UFDouble(sQuoteRate);
        dTaxRate = dTaxRate.div(100);
        dQuotePriceWOTax = dQuotePriceWTax.div(new UFDouble(1).add(dTaxRate));
        break;
      // 不计税--- “含税单价”= “非含税单价”
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
   * 创建者：lisb 功能：由生产日期算失效日期 参数： 返回： 例外： 日期：(2004-4-8 上午 10:50)
   * 修改日期，修改人，修改原因，注释标志：
   */
  private void afterProducedateEdit(BillEditEvent e) throws Exception {

    int iRow = e.getRow();
    Object oTemp = getCellValue(iRow, "dproducedate");
    Object oInvID = getCellValue(iRow, "cinvbasid");
    Object oOutInvID = getCellValue(iRow, "coutinvid");

    if (isNull(oTemp) || isNull(oInvID) || isNull(oOutInvID)) {
      // 清失效日期
      TOBillTool.clearRowValues(m_card, iRow, new String[] {
        "dvalidate"
      });
      return;

    }
    // 计算失效日期
    ParamVO param = InvInfo.getBatchMngInfo(oOutInvID.toString());

    if (param.iQualityDays != -1) {
      UFDate dtProductDate = new UFDate(oTemp.toString());
      UFDate dtValidateDate = dtProductDate.getDateAfter(param.iQualityDays);
      m_card.getBillModel().setValueAt(dtValidateDate, iRow, "dvalidate");
    }

  }

  /**
   * 表体页签改变后触发的方法 创建日期：(2003-8-7 9:47:24)
   * 
   * @param e
   *          nc.ui.pub.bill.BillTabbedPaneTabChangeEvent
   */
  public void afterTabChanged(nc.ui.pub.bill.BillTabbedPaneTabChangeEvent e) {
    nc.vo.pub.bill.BillTabVO btvo = e.getBtvo();
    if (btvo.getPos().intValue() == nc.ui.pub.bill.IBillItem.BODY) // 表体页签
    {
      // 表体所有页签的tableCode
      String[] saCode = m_card.getBillData().getTableCodes(
          nc.ui.pub.bill.IBillItem.BODY);
      for (int i = 0; i < saCode.length; i++) {
        // 停止所有表体页签的编辑，以便触发afterEdit()方法
        TableCellEditor editor = m_card.getBillTable(saCode[i]).getCellEditor();
        if (editor != null) {
          editor.stopCellEditing();
        }
      }
      
      
      //zhf add   增加  八大量参数模板
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
   * 创建者：lisb 功能： 参数： 返回： 例外： 日期：(2004-4-8 上午 10:50) 修改日期，修改人，修改原因，注释标志：
   */
  private void afterValidateEdit(BillEditEvent e) throws Exception {

    int iRow = e.getRow();
    String sTemp = getCellValue(iRow, "dvalidate");
    String sInvID = getCellValue(iRow, "cinvbasid");
    String sOutInvID = getCellValue(iRow, "coutinvid");

    if (isNull(sTemp) || isNull(sInvID) || isNull(sOutInvID)) {
      // 清失效日期
      TOBillTool.clearRowValues(m_card, iRow, new String[] {
        "dproducedate"
      });
      return;

    }

    ParamVO param = InvInfo.getBatchMngInfo(sOutInvID);

    if (param.iQualityDays == -1) {
      // 清空表体失效日期,生产日期
      TOBillTool.clearRowValues(m_card, iRow, new String[] {
          "dvalidate", "cinwhid"
      });
      return;

    }

    // 计算生产日期
    UFDate validateDate = new UFDate(sTemp);
    UFDate dtProductDate = validateDate.getDateBefore(param.iQualityDays);
    m_card.getBillModel().setValueAt(dtProductDate, iRow, "dproducedate");

  }

  /**
   * ****************************************** 功能： <|> 创建日期：(2004-3-11
   * 20:58:45)
   * 
   * @param：
   * @return： ******************************************
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
    // 辅计量管理时
    if (sKey.equalsIgnoreCase("nassistnum")) {
      if (bAstMng.booleanValue() && isNull(getCellValue(iRow, "castunitname"))) {
        sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
            "UPP40093010-000029")/*
                                   * @res "本行的存货为辅计量管理，请先输入辅单位"
                                   */);
        return false;
      }
      if (bAstMng.booleanValue() && isNull(getCellValue(iRow, "nchangerate"))) {
        sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
            "UPP40093010-000028")/* @res "换算率不能为空或0" */);
        return false;
      }
    }
    else if (sKey.equalsIgnoreCase("nquoteunitnum")) {
      if (isNull(getCellValue(iRow, "cquoteunitname"))) {
        sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
            "UPP40093010-000199")/*
                                   * @res "请先输入报价计量单位"
                                   */);
        return false;
      }
      if (bAstMng.booleanValue() && isNull(getCellValue(iRow, "nchangerate"))) {
        sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
            "UPP40093010-000028")/* @res "换算率不能为空或0" */);
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

    // 取辅单位
    String sPK_AstUnit = getCellValue(iRow, "castunitid");
    if (isNull(sPK_AstUnit)) {
      return flag;
    }

    String sMeasID = InvInfo.getPk_measdoc(sInvBasID);
    String sFixedflag = sMeasID.equals(sPK_AstUnit) ? "Y" : InvInfo
        .getInvConvert(sInvBasID, sPK_AstUnit)[1];
    // 固定换算率
    if (isNull(sFixedflag)) {
      return flag;
    }
    if (sFixedflag.equals("N")) {
      return flag;
    }
    // 变动换算率
    else if (sFixedflag.equals("Y")) {
      flag = "Y";
      return flag;
    }
    return flag;
  }

  /**
   * 创建者： 功能：表体换算率录入之前的处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
   * 修改日期，修改人，修改原因，注释标志：
   */
  public boolean beforeChangeRateEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    int iRow = e.getRow();
    String sInvBasID = getCellValue(iRow, "cinvbasid");
    String sOutInvID = getCellValue(iRow, "coutinvid");
    if (isNull(sInvBasID) || isNull(sOutInvID)) {
      return false;
    }

    // 取辅单位
    String sPK_AstUnit = getCellValue(iRow, "castunitid");
    if (isNull(sPK_AstUnit)) {
      sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000155")/*
                                 * @res "没有录入辅计量单位，不能输入换算率"
                                 */);
      return false;
    }

    String sMeasID = InvInfo.getPk_measdoc(sInvBasID);
    String sFixedflag = sMeasID.equals(sPK_AstUnit) ? "Y" : InvInfo
        .getInvConvert(sInvBasID, sPK_AstUnit)[1];
    // 固定换算率
    if (isNull(sFixedflag)) {
      sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
          "UPP40093010-000030")/* @res "请确认该存货是否定义了换算率" */);
      return false;
    }
    if (sFixedflag.equals("Y")) {
      sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000082")/*
                                 * @res "当前存货主计量与辅计量之间是固定换算率，不能修改换算率"
                                 */);
      return false;
    }
    // 变动换算率
    else if (sFixedflag.equals("N")) {
      return true;
    }
    return true;
  }

  /**
   * ****************************************** 功能： <|> 创建日期：(2004-3-15
   * 21:57:54)
   * 
   * @param：
   * @return： ******************************************
   */
  protected boolean beforeDeptEdit(BillEditEvent e) throws Exception {
    // 编辑的单位名称
    String sKey = e.getKey().trim();
    // 所属公司
    String sPk_corp = getHeadTailItemValue(getCorpRole(sKey) + "corpid");
    // 由表头的公司过滤表体的部门参照
    BillItem btCurrent = getBodyItem(sKey);
    // 检查是否输入了上级单位
    if (isNull(sPk_corp)) {
      String[] value = new String[] {
        nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0000404")
      /* @res "公司" */};
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPP40093010-000116", null, value));/* res请您先选择上级的公司 */
    }
    // 执行过滤
    CardPanelTool.filterRef(btCurrent, null, sPk_corp);
    return true;
  }

  /**
   * ****************************************** 功能： <|> 创建日期：(2004-3-15
   * 21:57:54)
   * 
   * @param：
   * @return： ******************************************
   */
  protected boolean beforeSpacenameEdit(BillEditEvent e) throws Exception {

    int iRow = e.getRow();
    // 编辑的单位名称
    String sKey = e.getKey();
    // 参与单位的角色:cin,cout or ctakeout
    String sRole = getCorpRole(sKey);
    // 所属仓库
    BillItem btCurrent = getBodyItem(sKey);
    String sWhid = getCellValue(iRow, sRole + "whid");
    // 检查是否输入了上级单位
    if (isNull(sWhid)) {
      String[] value = new String[] {
        nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0000153")
      /* @res "仓库" */};
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPP40093010-000116", null, value));// "请您先选择上级的仓库"
    }

    Object[] oCsFlag = (Object[]) CacheTool.getCellValue("bd_stordoc",
        "pk_stordoc", "csflag", (getCellValue(iRow, sRole + "whid")).toString()
            .trim());
    // 非货位管理时,不可编辑
    if (!isNull(oCsFlag)
        && !(new UFBoolean(oCsFlag[0].toString()).booleanValue())) {
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPP40093010-000033")/*
                                             * @res "此仓库为非货位管理,不可输入"
                                             */);

    }
    String sCorpid = getHeadTailItemValue(sRole + "corpid");
    String sCondition = "and endflag = 'Y' and bd_cargdoc.pk_stordoc ='"
        + sWhid + "'";
    // 执行过滤
    CardPanelTool.filterRef(btCurrent, sCondition, sCorpid);
    return true;
  }

  /**
   * ****************************************** 功能： <|> 创建日期：(2004-3-15
   * 21:57:54)
   * 
   * @param：
   * @return： ******************************************
   */
  protected boolean beforePsnnameEdit(BillEditEvent e) throws Exception {

    int iRow = e.getRow();
    // 编辑的单位名称
    String sKey = e.getKey().trim();
    // 参与单位的角色:cin,cout or ctakeout
    String sRole = getCorpRole(sKey);
    // 对当前参照进行过滤的条件
    String sCondition = null;
    String sPk_corp = getHeadTailItemValue(sRole + "corpid");

    // 检查是否输入了上级单位
    if (isNull(sPk_corp)) {
      String[] value = new String[] {
        nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0000404")
      /* @res "公司" */};
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPP40093010-000116", null, value));/*
                                                           * res "请您先选择上级的公司
                                                           */
    }
    // 所属部门
    if (getCellValue(iRow, sRole + "deptid") != null
        && getCellValue(iRow, sRole + "deptid").toString().trim().length() > 0) {
      sCondition = " and bd_psndoc.pk_deptdoc = '"
          + getCellValue(iRow, sRole + "deptid") + "'";
    }
    BillItem btCurrent = getBodyItem(sKey);
    // 执行过滤
    CardPanelTool.filterRef(btCurrent, sCondition, sPk_corp);
    return true;
  }

  /**
   * ****************************************** 功能： <|> 创建日期：(2004-3-15
   * 21:57:54)
   * 
   * @param：
   * @return： ******************************************
   */
  protected boolean beforeWarehouseEdit(int pos, String sKey) throws Exception {
    if (pos == IBillItem.BODY) {
      // 对参与调拨的公司的合法性检验
      String sInCorpID = getHeadTailItemValue("cincorpid");
      String sInCbID = getHeadTailItemValue("cincbid");
      String sOutCorpID = getHeadTailItemValue("coutcorpid");
      String sOutCbID = getHeadTailItemValue("coutcbid");
      String sTakeoutCorpID = getHeadTailItemValue("ctakeoutcorpid");
      String sTakeoutCbID = getHeadTailItemValue("ctakeoutcbid");
      BusinessCheck.tranCorpChk(sInCorpID, sInCbID, sOutCorpID, sOutCbID,
          sTakeoutCorpID, sTakeoutCbID, m_NodeInfo.getOrderTypeCode(m_card));

      // 非三方调拨，出货仓库＝调出仓库,出货仓库不可编辑
      if (!m_NodeInfo.getOrderTypeCode(m_card).equals(ConstVO.m_sBillSFDBDD)
          && sKey.equals("ctakeoutwhname")) {
        throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40093010", "UPP40093010-000215"));
        // 非三方调拨，出货仓库不可编辑!
      }
    }

    // 参与单位的角色:cin,cout or ctakeout
    String sRole = getCorpRole(sKey);
    // 调拨类型
    BillItem btTemp = m_card.getHeadItem("fallocflag");
    if (btTemp == null || btTemp.getValueObject() == null) {
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPP40093010-000031")/*
                                             * @res "调拨类型为空!"
                                             */);
    }
    // 直运调拨
    if ((sKey.equals("cinwhname") || sKey.equals("cinwhid"))
        && Integer.parseInt(btTemp.getValueObject().toString()) == ConstVO.ITransferType_DIRECT) {
      sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
          "UPP40093010-000032")/*
                                 * @res "直运调拨类型下的调入仓库不可输入"
                                 */);
      return false;
    }
    BillItem btCurrent = CardPanelTool.getBillItemByPos(m_card, pos, sKey);
    String sCorpid = getHeadTailItemValue(sRole + "corpid");
    if (sCorpid == null || sCorpid.length() <= 0) {
      sCorpid = m_NodeInfo.getCorpID();
    }
    // 加上iscapitalstor='N'，为了过滤到资产仓仓库
    String sCondition = " and  isdirectstore='N' and gubflag = 'N'";
    String sCbid = getHeadTailItemValue(sRole + "cbid");
    // 执行过滤
    CardPanelTool.filterRef(btCurrent, sCondition, sCorpid);
    // 库存组织的过滤条件只能通过setWherePart()来加载,而不能通过addWherePart(),
    // 否则多公司参照的时候会始终加载这个过滤条件,而导致数据错误
    if (sCbid != null && sCbid.length() > 0) {
      ((UIRefPane) btCurrent.getComponent()).getRefModel().setWherePart(
          " pk_calbody='" + sCbid + "'");
    }

    return true;
  }

  /**
   * ****************************************** 功能： <|> 创建日期：(2004-3-15
   * 21:57:54)
   * 
   * @param：
   * @return： ******************************************
   */
  protected boolean beforeInvEdit(BillEditEvent e) throws Exception {

    // 存货编码
    BillItem btInvCode = getBodyItem("cinvcode");
    UIRefPane refInv = (UIRefPane) btInvCode.getComponent();
    // refInv.setPK(null);
    int iRow = e.getRow();
    if (!isSelfMadeBill(iRow)) {
      sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009",
          "UPP40093009-000260")/* @res "存在上游单据，不可修改存货！" */);
      return false;
    }

    String sInCorpID = getHeadTailItemValue("cincorpid");
    String sInCbID = getHeadTailItemValue("cincbid");
    String sOutCorpID = getHeadTailItemValue("coutcorpid");
    String sOutCbID = getHeadTailItemValue("coutcbid");
    String sTakeoutCorpID = getHeadTailItemValue("ctakeoutcorpid");
    String sTakeoutCbID = getHeadTailItemValue("ctakeoutcbid");

    // 对参与调拨的公司的合法性检验
    BusinessCheck.tranCorpChk(sInCorpID, sInCbID, sOutCorpID, sOutCbID,
        sTakeoutCorpID, sTakeoutCbID, m_NodeInfo.getOrderTypeCode(m_card));

    if (isNull(sTakeoutCorpID)
        && !ConstVO.m_sBillSFDBDD.equals(m_NodeInfo.getOrderTypeCode(m_card))) {
      sTakeoutCorpID = sOutCorpID;
      sTakeoutCbID = sOutCbID;
    }
    // 设置可用量参照
    nc.ui.bd.ref.AbstractRefModel m = refInv.getRefModel();

    // 按调出公司设置存货的数据权限,如果不设定参照会默认当前登陆公司
    m.setPk_corp(sOutCorpID);

    // m.addWherePart(" and asset='N'");
    String[] o = new String[] {
        sTakeoutCorpID, sTakeoutCbID
    };
    m.setUserParameter(o);
    return true;
  }

  /**
   * 创建者： 功能：数量改变事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
   * 修改日期，修改人，修改原因，注释标志：
   */
  protected boolean beforeNumEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    int iRow = e.getRow();
    String sInvID = getCellValue(iRow, "cinvbasid");
    if (isNull(sInvID)) {
      return false;
    }
    // 是否辅计量管理
    UFBoolean bAstMng = InvInfo.getAstMngInfo(new String[] {
      sInvID
    })[0];

    if (bAstMng.booleanValue() && (isNull(getCellValue(iRow, "castunitname")))) {
      sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000109")/*
                                 * @res "辅计量管理的存货，请先输入辅单位"
                                 */);
      return false;
    }
    return true;
  }

  /**
   * 创建者： 功能：表体批次号录入之前的处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
   * 修改日期，修改人，修改原因，注释标志：
   */
  protected boolean beforeProduceDateEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    int iRow = e.getRow();
    String sInvID = getCellValue(iRow, "cinvbasid");
    // 调出存货管理id
    String sOutInvID = getCellValue(iRow, "coutinvid");
    String sBatch = getCellValue(iRow, "vbatch");
    // 是否输入存货
    if (isNull(sInvID) || isNull(sOutInvID) || isNull(sBatch)) {
      return false;
    }

    ParamVO paramResult = InvInfo.getBatchMngInfo(sOutInvID);
    // 批次管理 AND 保值期管理存货的生产日期可编辑
    return (paramResult.bLotMngFlag.booleanValue() && paramResult.bValidateMngFlag);

  }

  /**
   * ****************************************** 功能： <|> 创建日期：(2004-3-11
   * 20:58:45)
   * 
   * @param：
   * @return： ******************************************
   */
  protected boolean beforeSendTypeEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    // 退回时发运方式不可编辑
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
   * 创建者： 功能：表体批次号录入之前的处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
   * 修改日期，修改人，修改原因，注释标志：
   */
  protected boolean beforeValidateEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    int iRow = e.getRow();
    String sInvID = getCellValue(iRow, "cinvbasid");
    // 调出存货管理id
    String sOutInvID = getCellValue(iRow, "coutinvid");
    String sBatch = getCellValue(iRow, "vbatch");
    // 是否输入存货
    if (isNull(sInvID) || isNull(sOutInvID) || isNull(sBatch)) {
      return false;
    }

    ParamVO paramResult = InvInfo.getBatchMngInfo(sOutInvID);

    // 批次管理 AND 保值期管理存货的失效日期可编辑
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
   * 函数功能:设置直运仓信息 参数: 返回值: 异常:
   */
  private void chkDirectStore() throws BusinessException {

    // 调拨类型
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
    // 直运调拨
    String sCInCbID = getHeadTailItemValue("cincbid");
    if (isNull(sCInCbID)) {
      // 重设默认值
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
                                             * @res "请您先选择调入组织"
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
                                             * @res "请确认是否定义了直运仓库"
                                             */);
    }

    // 设置表体行的调入仓库
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
   * 此处插入方法说明。 创建日期：(2004-5-8 10:10:53)
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
   * 获取参与公司的角色 创建日期：(2001-10-24 16:33:58)
   * 
   * @return 例出货公司:"ctakeout";
   * @param 例sColumnName="ctakeoutwhname";
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
   * 此处插入方法说明。 创建日期：(2004-5-9 19:09:18)
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
                         * @res "归调出方"
                         */);
      case ITransrelaConst.ONWAY_OWER_FLAF_IN:
        return nc.ui.ml.NCLangRes.getInstance().getStrByID("topub",
            "UPP-000140"/*
                         * @res "归调入方"
                         */);

    }
    return null;
  }

  /**
   * 获取参与调拨的公司id 创建日期：(2004-5-9 19:09:18)
   * 
   * @return
   */
  private HashMap getTransCorpID() {

    HashMap hmID = new HashMap();
    // 调入corp ID
    String sInCorpID = null;
    String sInCbID = null;
    String sInWhID = null;

    String sOutCorpID = null;
    String sOutCbID = null;
    String sOutWhID = null;

    // 出货corp ID
    String sTakeOutCorpID = null;
    String sTakeOutCbID = null;
    String sTakeOutWhID = null;

    sInWhID = getHeadTailItemValue("cinwhid");
    sOutWhID = getHeadTailItemValue("coutwhid");
    sTakeOutWhID = getHeadTailItemValue("ctakeoutwhid");

    // 调入组织
    sInCbID = getHeadTailItemValue("cincbid");
    // 调出组织
    sOutCbID = getHeadTailItemValue("coutcbid");
    // 出货组织
    sTakeOutCbID = getHeadTailItemValue("ctakeoutcbid");
    // 调入公司
    sInCorpID = getHeadTailItemValue("cincorpid");
    // 调出公司
    sOutCorpID = getHeadTailItemValue("coutcorpid");
    // 出货公司
    sTakeOutCorpID = getHeadTailItemValue("ctakeoutcorpid");

    // 出货方信息如果为空，默认为调出方信息
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
      //不清空调出调入在途归属，5E、5I不询调拨关系，取手工录入在途归属
      TOBillTool.clearHeadTailValue(m_card, new String[] {
          "fotwastpartflag",
          "fotwastpartname"
      });
    }
  }

  /**
   * 此处插入方法说明:获取调拨关系 创建日期：(2004-5-9 19:00:29)
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
      // 查找调出调入方的调拨关系表体
      alParam[0] = getParam4QryTransItem(iaRow, false);
      // 查找调出调入方的调拨关系表头
      // alParam[1] = getParam4QryTransHead(false);
      // 查找出货调出方的表体调拨关系
      alParam[1] = getParam4QryTransItem(iaRow, true);
      // 查找出货调出方的表头调拨关系
      // alParam[3] = getParam4QryTransHead(true);
    }
    else {
      alParam = new ArrayList[1];
      // 查找调出调入方的调拨关系表体
      alParam[0] = getParam4QryTransItem(iaRow, false);
      // 查找调出调入方的调拨关系表头
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
               * @res "第i行未找到调拨关系定义"
               */);
      }
      hmValue.put("crelationid", voaTransItem[i].getPk_transrela());// 调拨关系主表pk
      hmValue.put("crelation_bid", voaTransItem[i].getPk_transrela_b());// 调拨关系子表pk

      TOBillTool.setRowValue(m_card, hmValue, iaRow[i]);
    }
    // 给表头的在途归属字段赋值(因为在途归属的属性在调拨关系表头，取第一个就可以了)
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
    // 公司id
    String sInCorpID = hmCorp.get("cincorpid").toString();
    String sInCbID = hmCorp.get("cincbid").toString();
    String sInWhID = hmCorp.get("cinwhid").toString();
    String sOutCorpID = hmCorp.get("coutcorpid").toString();
    String sOutCbID = hmCorp.get("coutcbid").toString();
    String sOutWhID = hmCorp.get("coutwhid").toString();
    String sTakeOutCorpID = hmCorp.get("ctakeoutcorpid").toString();
    String sTakeOutCbID = hmCorp.get("ctakeoutcbid").toString();
    String sTakeoutWhID = hmCorp.get("ctakeoutwhid").toString();

    // 调拨类型
    String sFallocFlag = getHeadTailItemValue("bdrictflag");
    if (sFallocFlag.equals("false")) {
      sFallocFlag = "1";
    }
    else {
      sFallocFlag = "0";
    }
    Integer iFallocflag = Integer.valueOf(sFallocFlag);

    // 业务类型
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
    // 公司id
    String sInCorpID = hmCorp.get("cincorpid").toString();
    String sInCbID = hmCorp.get("cincbid").toString();
    String sOutCorpID = hmCorp.get("coutcorpid").toString();
    String sOutCbID = hmCorp.get("coutcbid").toString();
    String sTakeOutCorpID = hmCorp.get("ctakeoutcorpid").toString();
    String sTakeOutCbID = hmCorp.get("ctakeoutcbid").toString();

    // 调拨类型
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
   * ****************************************** 功能： <|>查询出货公司的可用量、现存量
   * 创建日期：(2004-3-11 20:58:45)
   * 
   * @param：
   * @return： ******************************************
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
   * 创建者： 功能：表体换算率录入之前的处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
   * 修改日期，修改人，修改原因，注释标志：
   */
  protected boolean beforeTaxRateEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {
    if (!isCanUpdatePrice(e.getRow())) {
      return false;
    }
    // 调拨关系
    QryTransrelaVO[] voTransItems = getTransItem(new int[] {
      e.getRow()
    });
    if (isNull(voTransItems)) {
      //5I、5E不匹配调拨关系，这时要求税率可编辑
      return true;
    }
    QryTransrelaVO voTransItem = voTransItems[0];
    Integer iTemp = voTransItem.getTaxtypeflag();
    boolean bEdit = false;

    if (iTemp.intValue() == ConstVO.TAX_TYPE_NULL)
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", ConstVO201.sINF_TRAN_TAX_NULL_SID));

    else {
      // 税率可编辑
      bEdit = true;
    }

    return bEdit;

  }

  protected boolean beforePriceEdit(BillEditEvent e) throws Exception {
    if (!isCanUpdatePrice(e.getRow())) {
      return false;
    }
    // 如果报价计量单位单价显示,则不允许修改主单位单价
    if (e.getKey().indexOf("price") >= 0) {
      BillItem bt = getBodyItem("norgqttaxnetprc");
      if (bt != null && bt.isShow()) {
        return false;
      }
    }
    return getBodyItem(e.getKey()).isEdit();
  }

  /**
   * 此处插入方法说明。 创建日期：(2004-5-18 11:10:39)
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
            "UPP40093009-000261")/* @res "未能获得数据精度！" */);
        return;
      }
      for (int i = 0; i < daPrices.length; i++) {
        if (daPrices[i] != null)
          daPrices[i].setScale(iaDataPrecision[1], UFDouble.ROUND_HALF_UP);
      }
      // 报价单位含税单价
      hmValue.put("norgqttaxnetprc", isNull(daPrices[0]) ? "" : daPrices[0]
          .toString());
      // 报价单位非含税单价
      hmValue.put("norgqtnetprc", isNull(daPrices[1]) ? "" : daPrices[1]
          .toString());
      // 主单位含税单价
      hmValue.put("nprice", isNull(daPrices[2]) ? "" : daPrices[2].toString());
      // 主单位非含税单价
      hmValue.put("nnotaxprice", isNull(daPrices[3]) ? "" : daPrices[3]
          .toString());
      // 税率
      hmValue
          .put("ntaxrate", isNull(daPrices[4]) ? "" : daPrices[4].toString());
      // 含税金额
      hmValue.put("nmny", isNull(daPrices[2]) || isNull(sNum) ? ""
          : daPrices[2].multiply(new UFDouble(sNum)).toString());
      // 非含税金额
      hmValue.put("nnotaxmny", isNull(daPrices[3]) || isNull(sNum) ? ""
          : daPrices[3].multiply(new UFDouble(sNum)).toString());
      // 加价率
      hmValue.put("naddpricerate", isNull(daPrices[5]) ? "" : daPrices[5]
          .toString());
      // 询价含税单价
      // hmValue.put("naskprice",
      // isNull(daPrices) || isNull(daPrices[2]) ? "" : daPrices[2]
      // .toString());
      // 询价非含税单价
      // hmValue.put("nasknotaxprice", isNull(daPrices)
      // || isNull(daPrices[3]) ? "" : daPrices[3].toString());
      TOBillTool.setRowValue(m_card, hmValue, iRow);

    }
    else {
      // 清空价格
      TOBillTool.clearRowValues(m_card, iRow, new String[] {
          "norgqttaxnetprc", "norgqtnetprc", "nprice", "nnotaxprice",
          "ntaxrate", "nmny", "nnotaxmny", "naddpricerate"
      });
    }
  }

  /*
   * 清空指定行的价格信息
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
   * 方法功能描述：全部表体行询价方法。调用此方法肯定会询价。如需过滤需要询价的表体行，请调用setPrice()或setPrice(int[]
   * iaRow) <b>参数说明</b>
   * 
   * @throws BusinessException
   * @author sunwei
   * @time 2008-11-28 上午09:39:48
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
    // 取价
    getPrice(iaRow);
  }

  /**
   * 方法功能描述：表头询价条件是否存在
   * 判断表头的公司、组织、币种是否非空
   * @return
   * @author sunwei
   * @time 2009-1-13 下午07:00:38
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
   * ****************************************** 功能： <|> 调用此方法肯定会询价
   * 通常情况下调用setPrice(int[] iaRow)方法，先过滤出需要询价的行, 创建日期：(2004-3-15 21:57:54)
   * 
   * @param：
   * @return： ******************************************
   */
  protected void getPrice(int[] iaRow) throws BusinessException {
    //5E、5I不触发询价
    if (isInSameCorp()) {
      return;
    }
    //如果询价条件不完整，不触发询价
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
      // 如果存货为赠品，则不取价
      String sInvbasid = getCellValue(iaRow[i], "cinvbasid");
      String sFlargess = getCellValue(iaRow[i], "flargess");
      if (isNull(sInvbasid) || Boolean.valueOf(sFlargess).booleanValue()) {
        k++;
        continue;
      }

      for (int j = 0; j < keys.length; j++) {
    	  // modify by zhw 2011-04-11  如果存在计划项目出库方式为合同价  就不在设置价格
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
      
      //右键询价不会触发行状态变化，手动触发行状态为“修改”.
      if(BillModel.NORMAL==m_card.getBillModel().getRowState(iaRow[i])){//避免新增行状态由NEW 变为MODIFICATION
        m_card.getBillModel().setRowState(iaRow[i], BillModel.MODIFICATION);
      }
    }
    String[] keys_h = new String[]{"foiwastpartflag", "fotwastpartflag", "foiwastpartname","fotwastpartname"};
    for(int i=0;i<keys_h.length;i++){
      m_card.setHeadItem(keys_h[i], vo4Query.getHeaderVO().getAttributeValue(keys_h[i]));
    }
    
    String sHintMsg = (String) alResult.get(1);
    if (sHintMsg != null && sHintMsg.length() > 0) {
      // 出错，则清空指定行的价格信息
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
    // 是否赠品标志
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
      // 没有存货，或者赠品行存货不参加询价
      if (isNull(sInvBasID)
          || (!isNull(sFlargess) && Boolean.valueOf(sFlargess).booleanValue())) {
        continue;
      }
      
      //V56销售价格需要传整个VO做为参数
      BillItemVO itemTemp = (BillItemVO) m_card.getBillModel().getBodyValueRowVO(iaRow[i],NodeInfo.NAME_BODYVO);

      alItem4Query.add(itemTemp);
    }
    BillVO vo4Query = new BillVO();
    
    //V56销售价格需要传整个VO做为参数
    BillHeaderVO head4Query = (BillHeaderVO) m_card.getBillData().getHeaderValueVO(NodeInfo.NAME_HEADVO);

    BillItemVO[] items4Query = new BillItemVO[alItem4Query.size()];
    items4Query = (BillItemVO[]) alItem4Query.toArray(items4Query);
    vo4Query.setParentVO(head4Query);
    vo4Query.setChildrenVO(items4Query);
    
    if(bAllFlargess&&items4Query.length==0&&ui.getIsComeFromOther()){
      //所有表体行都为赠品行，且为5A参照新增时需要询调拨关系
      getTransItem(iaRow);
    }
    return vo4Query;
  }

  /**
   * ****************************************** 功能： <|> 创建日期：(2004-3-18
   * 15:04:02)
   * 
   * @param：
   * @return： ******************************************
   */
  private void afterCalbodyEidt(String sKey) throws Exception {
    // 清除库存组织下的仓库（表头和表体）、货位信息
    String sRole = getCorpRole(sKey);
    TOBillTool.clearHeadTailValue(m_card, new String[] {
        sRole + "whname", sRole + "whid"
    });
    TOBillTool.clearAllRowValues(m_card, new String[] {
        sRole + "whname", sRole + "whid", sRole + "spacename",
        sRole + "spaceid"
    });
    // 带出库存组织所属公司
    String sPk = (String) m_card.getHeadItem(sKey).getValueObject();
    Object[] oValue = null;
    String oldcorp = null;
    if (sPk != null && sPk.length() > 0) {
       oValue = (Object[]) nc.ui.scm.pub.cache.CacheTool.getCellValue(
          "bd_calbody", "pk_calbody", "pk_corp", sPk);
    }
    
    oldcorp = (String)m_card.getHeadItem(sRole + "corpid").getValueObject();
    // 公司确实变化了才调用afterCorpChanged
    if (oValue != null && !isSameString(oldcorp, (String) oValue[0])) {
      
      m_card.setHeadItem(sRole + "corpid", (String) oValue[0]);
      // 改变公司引起的变化：变更表体公司和本位币
      afterCorpChanged(sRole + "corpid");
    }
    afterCalbodyChanged(sKey);
    
    //判断在途归属是否可以编辑
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
   * 方法功能描述：设置在途归属是否可以编辑。因为5E、5I不询调拨关系，所以调出调入在途归属可以编辑
   * <b>参数说明</b>
   * @author sunwei
   * @time 2009-3-16 上午10:01:07
   */
  private void setEditableOfWastpart() {
    BillItem bt = null;
    bt = m_card.getHeadItem("foiwastpartflag");
    String typecode = "";
    try{
      typecode = m_NodeInfo.getOrderTypeCode(m_card);
    }
    catch(BusinessException ex){
      //无法判断单据类型时，在途归属不可编辑
    }
    if(typecode.equals("5E")||typecode.equals("5I"))
      bt.setEdit(true);
    else
      bt.setEdit(false);
  }
  
  /*
   * 处理库存组织的值改变以后，引起的变化
   */
  private void afterCalbodyChanged(String sKey) throws Exception {
    String sRole = getCorpRole(sKey);
    BillItem bt = m_card.getHeadItem(sKey);
    UIRefPane ref = (UIRefPane) bt.getComponent();
    m_card.setHeadItem(sRole + "cbname", ref.getRefName());

    // 先清除历史信息
    TOBillTool.clearAllRowValues(m_card, new String[] {
        "nprice", "nnotaxprice", "ntaxrate", "nmny", "nnotaxmny",
        "naddpricerate", "naskprice", "nasknotaxprice"
    });

    // 如果编辑调出组织
    if (sRole.equals("cout")) {
      // 重新刷新atp
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
    // 如果编辑调入组织
    else if (sRole.equals("cin")) {
      // v5 不允许新增直运调拨订单，且外系统传入的直运调拨订单不允许修改调入方信息，故不可能进入下面的方法
      // chkDirectStore();
      setWhInfo(null);
    }

    // 调拨关系变了，需要重新取表体行的价格
    int iCount = m_card.getBillModel().getRowCount();
    ArrayList<Integer> alRow = new ArrayList<Integer>(iCount);
    for (int i = 0; i < iCount; i++) {
      m_card.getBillModel().setValueAt(ref.getRefPK(), i, sRole + "cbid");
      // 如果修改了表头，也认为修改了表体
      if (m_card.getBillModel().getRowState(i) == BillModel.NORMAL) {
        m_card.getBillModel().setRowState(i, BillModel.MODIFICATION);
      }
      // 表体是否输入了存货
      if (!isNull(getCellValue(i, "cinvcode"))) {
        alRow.add(i);
      }
    }
    if (alRow.size() > 0) {
      int[] iaRow = new int[alRow.size()];
      for (int i = 0; i < iaRow.length; i++) {
        iaRow[i] = ((Integer) alRow.get(i)).intValue();
      }

      // 如果有表体行，检查是否分配到库存组织，刷新表体存货信息
      checkInvInfo(iaRow);

      // 再取价
      setPrice(iaRow);
    }
  }

  /**
   * 检查是否分配到公司，刷新表体存货信息
   * <b>参数说明</b>
   * @param iaRow
   * @throws BusinessException
   * @author sunwei
   * @time 2009-1-19 上午11:07:13
   */
  protected void checkInvInfo(int[] iaRow) throws BusinessException {
    // 存货基本id
    String[] saInvBasID = new String[iaRow.length];
    for (int i = 0; i < iaRow.length; i++) {
      saInvBasID[i] = (String) m_card.getBillModel().getValueAt(iaRow[i],
          "cinvbasid");
    }
    // 公司id、库存组织id
    HashMap hmCorp = getTransCorpID();
    String sInCorpID = hmCorp.get("cincorpid").toString();
    String sInCbID = hmCorp.get("cincbid").toString();
    String sOutCorpID = hmCorp.get("coutcorpid").toString();
    String sOutCbID = hmCorp.get("coutcbid").toString();
    String sTakeOutCorpID = hmCorp.get("ctakeoutcorpid").toString();
    String sTakeOutCbID = hmCorp.get("ctakeoutcbid").toString();
    
    //库存组织为空，不进行检查
    if(VOChecker.isEmpty(sInCbID)||VOChecker.isEmpty(sOutCbID)||VOChecker.isEmpty(sTakeOutCbID)){
      return;
    }
    
    // 检查存货是否分配到相应的公司
    HashMap hmInvManID = BusinessCheck.invManDocChk(saInvBasID, new String[] {
        sInCorpID, sOutCorpID, sTakeOutCorpID
    });
    String[] saOutInvID = new String[iaRow.length];
    for (int i = 0; i < iaRow.length; i++) {
      saOutInvID[i] = (String) hmInvManID.get(saInvBasID[i] + sOutCorpID);
    }
    // 存货信息改成批取
    HashMap hmParam = InvInfo.getMngInfo(saInvBasID, saOutInvID);
    // time.showExecTime("取存货信息");
    StringBuffer sbKey = new StringBuffer();
    ParamVO param = null;

    for (int ctr = 0; ctr < iaRow.length; ctr++) {
      // 存货管理id
      String sOutInvID = saOutInvID[ctr];
      // 存货基本id
      String sInvBasID = saInvBasID[ctr];
      sbKey.setLength(0);
      sbKey = sbKey.append(sInvBasID).append(sOutInvID);
      param = (ParamVO) hmParam.get(sbKey.toString());
      if (param.bLaborFlag || param.bDiscountFlag)
        throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40093010", "UPP40093010-000023")/*
                                                           * @res
                                                           * "不可选取为劳务或价格折扣的存货"
                                                           */);

      // 存货管理id
      String sInInvID = hmInvManID.get(sInvBasID + sInCorpID).toString();
      String sTakeOutInvID = hmInvManID.get(sInvBasID + sTakeOutCorpID)
          .toString();

      // 设置本行的存货信息
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
      
      //如果已有辅单位、换算率等，则不从新设值.因为辅单位等信息是集团级的，跟公司、组织无关，无需变动
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

      hmValue.put("bquotefixrate", param.bFixRate);// 设置是否报价计量单位固定换算率，数量单价金额公共方法使用
      // 往界面上放数
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
    // 自动带出对应部门信息
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
   * ****************************************** 功能： <|> 创建日期：(2004-3-11
   * 20:58:45)
   * 
   * @param：
   * @return： ******************************************
   */
  public boolean beforeCustEdit(BillEditEvent e) throws Exception {

    String sKey = e.getKey();
    boolean bEditFlag = false;

    // 调拨类型
    String sAllocFlag = getHeadTailItemValue("bdrictflag");
    if (sAllocFlag.equals("false")) {
      sAllocFlag = "1";
    }
    else {
      sAllocFlag = "0";
    }
    // 入库调拨：收货地址、收货地区应不编辑
    if (getSourceType(e) == null
        && Integer.parseInt(sAllocFlag) == ConstVO.ITransferType_DIRECT) {
      bEditFlag = true;
    }
    // 过滤收货客户
    if (sKey.equals("creceievename")) {

      // 收货客户为调入公司的客户
      if (Integer.parseInt(sAllocFlag) == ConstVO.ITransferType_DIRECT) {
        String sPk_corp = (String) getHeadTailItemValue("cincorpid");
        String sCondition = "(  frozenflag  =  'N'  or  frozenflag  is  null  ) AND  (  bd_cumandoc.custflag  =  '0'  or  bd_cumandoc.custflag  =  '2'  ) ";
        BillItem btCurrent = getBodyItem("creceievename");
        CardPanelTool.filterRef(btCurrent, sCondition, sPk_corp);
      }
    }
    return bEditFlag;
  }

  // 根据调出公司过滤供应商
  protected boolean beforeVendorEdit(BillEditEvent e) throws Exception {

    BillItem btCurrent = getBodyItem(e.getKey());
    String sPk_corp = getHeadTailItemValue("coutcorpid");
    String sCondition = " bd_cumandoc.custflag in('1','3') ";
    CardPanelTool.filterRef(btCurrent, sCondition, sPk_corp);

    return getBodyItem(e.getKey()).isEdit();
  }

  /**
   * ****************************************** 功能： <|>来源单据类型 创建日期：(2004-3-11
   * 20:58:45)
   * 
   * @param：
   * @return： ******************************************
   */
  private String getSourceType(BillEditEvent e) throws BusinessException {

    Object oTemp = getCellValue(e.getRow(), "csourcetypecode");
    return oTemp == null ? null : oTemp.toString();

  }

  /**
   * 设置下拉自由项值 创建日期：(01-2-26 13:29:17)
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
   * 设置汇率 创建日期：(2004-5-9 16:57:38)
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
    // 折本汇率
    m_card.setHeadItem("nexchangeotobrate", null);

    UFDouble daRate = null;

    // 是否主辅币核算
    if (!bBD302) {
      // 如果单主币核算,原币折本币
      UFDouble dRate = currArith.getRate(sCurrType, sBasCurrType, sDate);
      if (isNull(dRate)) {
        sendErrMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
            "UPP40093010-000204")/* "没有获得折本汇率！" */);
      }
      daRate = dRate;
    }
    m_card.setHeadItem("nexchangeotobrate", daRate);

    // 是否可编辑
    setRateEdit(false, sOutCorp, sCurrType);
  }

  /**
   * 此处插入方法说明:输入正负数的检查 创建日期：(2004-6-9 11:47:17)
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
                                                           * @res "汇率请输入正数"
                                                           */);
      }
      if (isRetBill(iRow) && sKey.indexOf("num") != -1 && oValue != null
          && oValue.toString().length() > 0
          && Double.parseDouble(e.getValue().toString()) >= 0d) {
        throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40093010", "UPP40093010-000052")/*
                                                           * @res
                                                           * "调拨订单退回时，请输入负数"
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
   * 设置卡片界面的精度 sOutCorpID 调出公司 bNewCurrType :是否为调出公司的本位币） 创建日期：(2004-6-14
   * 19:43:36) int[0] ----- 数量的精度 int[1] ----- 单价的精度 int[2] ----- 金额的精度 int[3]
   * ----- 辅计量数量的精度 int[4] ----- 换算率的精度 int[5] -----
   * 折本汇率的精度（此处的汇率实际是错误的，因为汇率要知道两个币种，这里默认返回5位） int[6] -----
   * 折辅汇率的精度（此处的汇率实际是错误的，因为汇率要知道两个币种，这里默认返回5位
   */
  public void freshScale(String sOutCorpID, String sCurrType) {

    if (isNull(sCurrType)) {
      return;
    }

    try {
      Integer[] iaDataPrecision = m_NodeInfo.getDataPrecision();

      BusinessCurrencyRateUtil currArith = BusinessCheck
          .getCurrArith(sOutCorpID);

      String sBasCurrType = m_NodeInfo.getCurrType(sOutCorpID);// 本位币

      // 设置金额精度
      // 先取折本汇率(如果主辅币核算，则源币种是辅币，目的币种是本币；否则是原币折本币)
      CurrinfoVO voCurr = null;

      voCurr = currArith.getCurrinfoVO(sCurrType, sBasCurrType);

      // 获得折本汇率的精度
      /*
       * 谢阳曰： 当没有定义汇率方案时，折本汇率的默认小数位现在供应链各模块处理不一致，这样有可能会导致单据跨模块转换时折本汇率被改变的问题。
       * 建议统一供应链各模块的折本汇率默认小数位，都统一处理成5位，因为现在财务处理成5位，咱们保持与财务的一致。
       */
      iaDataPrecision[5] = isNull(voCurr) || isNull(voCurr.getRatedigit()) ? 5
          : voCurr.getRatedigit();

      Integer currBusiDigit = CurrtypeQuery.getInstance().getCurrtypeVO(
          sCurrType).getCurrbusidigit();

      iaDataPrecision[2] = isNull(currBusiDigit) ? iaDataPrecision[2]
          : currBusiDigit;

      // 获得数量单价精度,调出公司有可能为其他公司
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
   * 创建者： 功能：项目信息编辑以后触发 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
   * 修改日期，修改人，修改原因，注释标志：
   */
  private void afterProjectEdit(nc.ui.pub.bill.BillEditEvent e) {
    Object oTemp = e.getValue();
    int iRow = e.getRow();

    TOBillTool.clearRowValues(m_card, iRow, new String[] {
        "cprojectphasename", "cprojectphase"
    });
  }

  /**
   * 创建者： 功能：项目阶段信息编辑以后触发 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
   * 修改日期，修改人，修改原因，注释标志：
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

  // 编辑表头仓库触发
  private void afterHeadWhEdit(BillEditEvent e) throws Exception {
    String sKey = e.getKey();
    String sRole = getCorpRole(sKey);
    String sPk = ((nc.ui.pub.beans.UIRefPane) e.getSource()).getRefPK();
    String sName = ((nc.ui.pub.beans.UIRefPane) e.getSource()).getRefName();
    m_card.setHeadItem(sRole + "whname", sName);
    // 带入表体
    int iRowCount = m_card.getBillModel().getRowCount();
    if (iRowCount > 0) {
      // 清除货位
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

    // 仓库为多公司参照，有可能所选仓库所属公司、组织会改变。所以一定要刷相应的公司、组织
    if (sPk != null) {
      // 根据仓库带出库存组织和公司
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
          // 改变公司引起的变化：变更表体公司和本位币
          afterCorpChanged(sRole + "corpid");
        }
      }
      if (btcb != null) {

        String oldCB = (String) btcb.getValueObject();
        if (!isSameString(pk_calbody, oldCB))

          m_card.setHeadItem(sRole + "cbid", pk_calbody);

        // 改变库存组织引起的变化：变更表体库存组织和重新询价
        afterCalbodyChanged(sRole + "cbid");
      }
    }
  }

  /*
   * 公司改变以后引起的变化
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
      // 表体的公司也要随之改变
      if (m_card.getBillModel().getRowState(i) == BillModel.NORMAL) {
        m_card.getBillModel().setRowState(i, BillModel.MODIFICATION);
      }
    }

    String sTakeOutCorpid = getHeadTailItemValue("ctakeoutcorpid");
    String sOutCorpid = getHeadTailItemValue("coutcorpid");

    // 编辑调出公司，变更相应的本位币
    if (sRole.equals("cout")) {
      String sCurrType = m_NodeInfo.getCurrType(sPK);
      m_card.setHeadItem("coutcurrtype", sCurrType);
      freshScale(sPK, sCurrType);
      setRate(sCurrType, sPK);

      // 出货公司为空或出货公司与调出公司相同，连带给出货公司赋值
      if (isNull(getHeadTailItemValue("ctakeoutcorpid"))||isSameOutTakeOut) {
        m_card.setHeadItem("ctakeoutcorpid", sPK);
        // 清除公司下的库存组织
        TOBillTool.clearHeadTailValue(m_card, new String[] {
            "ctakeoutcbid", "ctakeoutcbname"
        });
        // 清除公司下的仓库
        TOBillTool.clearHeadTailValue(m_card, new String[] {
            "ctakeoutwhid", "ctakeoutwhname"
        });

        // 清除公司下的部门、人员、仓库、货位信息
        TOBillTool.clearAllRowValues(m_card, new String[] {
            "ctakeoutdeptname", "ctakeoutdeptid", "ctakeoutpsnname",
            "ctakeoutpsnid", "ctakeoutwhname", "ctakeoutwhid", "ctakeoutspacename",
            "ctakeoutspaceid"
        });
      }
      /*
       * 重新取表体行的价格，因为库存组织为必输项，所以此处不重新查找调拨关系，而是 等输入组织以后再重新查找
       */

      TOBillTool.clearAllRowValues(m_card, new String[] {
          "cprojectid", "cprojectname", "cprojectphase", "cprojectphasename",
          "nprice", "nnotaxprice", "ntaxrate", "nmny", "nnotaxmny",
          "naddpricerate", "naskprice", "nasknotaxprice"
      });

    }

    String sInCorpid = getHeadTailItemValue("cincorpid");
    // 询默认结算路径
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
        // 三方调拨不设置结算路径
        m_card.setHeadItem("csettlepathid", null);
        m_card.setHeadItem("csettlepathname", null);
      }
    }
    else {
      m_card.setHeadItem("csettlepathid", null);
      m_card.setHeadItem("csettlepathname", null);
    }

    //当公司改变时才判断是否控制调入方权限
    if (sRole.equals("cin")||sRole.equals("cout")) {
      setRefUserPower();
    }
  }
  
  /*
   * 设置参照的数据权限
   * 当公司改变时才判断是否控制调入方权限
   * */
  private void setRefUserPower()
  {
    String sInCorpid = getHeadTailItemValue("cincorpid");
    String sOutCorpid = getHeadTailItemValue("coutcorpid");
    if(!VOChecker.isEmpty(sInCorpid)
        &&!VOChecker.isEmpty(sOutCorpid)
        &&!sOutCorpid.equals(sInCorpid)){
      //不控制调入方的数据权限
      //--表头调入库存组织
      CardPanelTool.setRefUserPower(m_card,IBillItem.HEAD, "cincbid", false);
      //--表头调入仓库
      CardPanelTool.setRefUserPower(m_card,IBillItem.HEAD, "cinwhid", false);
      //--表体调入仓库
      CardPanelTool.setRefUserPower(m_card,IBillItem.BODY, "cinwhname", false);
      //--表体调入部门
      CardPanelTool.setRefUserPower(m_card,IBillItem.BODY, "cindeptname", false);
      //--表体调入部门业务员
      CardPanelTool.setRefUserPower(m_card,IBillItem.BODY, "cinpsnname", false);
    }
    else{
      //控制调入方的数据权限
      //--表头调入库存组织
      CardPanelTool.setRefUserPower(m_card,IBillItem.HEAD, "cincbid", true);
      //--表头调入仓库
      CardPanelTool.setRefUserPower(m_card,IBillItem.HEAD, "cinwhid", true);
      //--表体调入仓库
      CardPanelTool.setRefUserPower(m_card,IBillItem.BODY, "cinwhname", true);
      //--表体调入部门
      CardPanelTool.setRefUserPower(m_card,IBillItem.BODY, "cindeptname", true);
      //--表体调入部门业务员
      CardPanelTool.setRefUserPower(m_card,IBillItem.BODY, "cinpsnname", true);
    }
  }

  /**
   * ****************************************** 功能： <|> 创建日期：(2004-3-15
   * 21:57:54)
   * 
   * @param：
   * @return： ******************************************
   */
  private void afterCorpUnitEdit(nc.ui.pub.bill.BillEditEvent e)
      throws Exception {

    // 输入的公司
    String sKey = e.getKey().trim();
    String sRole = getCorpRole(sKey);

    // 清除公司下的库存组织
    TOBillTool.clearHeadTailValue(m_card, new String[] {
        sRole + "cbid", sRole + "cbname"
    });
    // 清除公司下的仓库
    TOBillTool.clearHeadTailValue(m_card, new String[] {
        sRole + "whid", sRole + "whname"
    });

    // 清除公司下的部门、人员、仓库、货位信息
    TOBillTool.clearAllRowValues(m_card, new String[] {
        sRole + "deptname", sRole + "deptid", sRole + "psnname",
        sRole + "psnid", sRole + "whname", sRole + "whid", sRole + "spacename",
        sRole + "spaceid"
    });

    afterCorpChanged(sKey);
  }

  /**
   * 此处插入方法说明: 创建日期：(2004-6-24 10:27:21)
   * 
   * @param eBillEditEvent
   */
  private void afterBodyWhEdit(BillEditEvent e) throws Exception {
    String sKey = e.getKey();
    String sRole = getCorpRole(sKey);
    int iRow = e.getRow();

    // 清除货位
    TOBillTool.clearRowValues(m_card, iRow, new String[] {
        sRole + "spacename", sRole + "spaceid"
    });

    // 调出或出货仓库
    if (sKey.endsWith("outwhname")) {
      // 非三方调拨，出货仓库＝调出仓库
      if (!m_NodeInfo.getOrderTypeCode(m_card).equals(ConstVO.m_sBillSFDBDD)) {
        m_card.getBillModel().setValueAt(getCellValue(iRow, "coutwhid"), iRow,
            "ctakeoutwhid");
        m_card.getBillModel().execEditFormulasByKey(iRow, "ctakeoutwhname");
      }
      // 查询可用量
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
    // 编辑仓库后，组织内调拨要重新询调拨关系和价格（因为非组织内调拨，仓库不作为匹配调拨关系的条件）
    if (ConstVO.m_sBillZZNDBDD.equals(m_NodeInfo.getOrderTypeCode(m_card))) {
      // 表体是否输入了存货
      if (!isNull(getCellValue(iRow, "cinvcode"))) {
        // 再取价
        setPrice(new int[] {
          iRow
        });
      }
    }
  }

  /**
   * 创建者： 功能：表体项目阶段录入之前的处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
   * 修改日期，修改人，修改原因，注释标志：
   */
  protected boolean beforeProjectEdit(nc.ui.pub.bill.BillEditEvent e)
      throws BusinessException {

    String sOutCorpID = getHeadTailItemValue("coutcorpid");
    if (isNull(sOutCorpID))
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPP40093010-000053")/*
                                             * @res "请先选择调出公司"
                                             */);

    BillItem bt = getBodyItem(e.getKey());
    UIRefPane ref = (UIRefPane) bt.getComponent();
    ref.setRefModel(new nc.ui.bd.b39.JobRefTreeModel(ConstVO201.sGROUPID,
        sOutCorpID, null));
    return true;
  }

  /**
   * 创建者： 功能：表体项目阶段录入之前的处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
   * 修改日期，修改人，修改原因，注释标志：
   */
  protected boolean beforeProjectPhaseEdit(nc.ui.pub.bill.BillEditEvent e) {
    String sProjectID = getCellValue(e.getRow(), "cprojectid");
    if (isNull(sProjectID)) {
      sendHintMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
          "UPP40093010-000054")/* @res "请先输入项目信息" */);
      return false;
    }

    UIRefPane cprojectphasename = (UIRefPane) getBodyItem(e.getKey())
        .getComponent();
    cprojectphasename.setRefModel(new nc.ui.bd.b39.PhaseRefModel(sProjectID));

    return true;
  }

  /**
   * 此处插入方法说明:清空前次的存货信息 创建日期：(2004-6-21 19:25:50)
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
   * 读取该行存货的自由项信息 创建日期：(2004-6-17 9:13:20)
   * 
   * @return java.lang.String[]
   */
  private String[] getFreeItem(int iRow) {
    String[] saFreeItem = new String[5];
    // 自由项是否全为空
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
   * 此处插入方法说明。 创建日期：(2004-6-17 9:13:20)
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
   * 此处插入方法说明:是否为退回调拨订单 创建日期：(2004-6-14 16:05:30)
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
   * 此处插入方法说明:是否自制单据行或推式生成的单据行 创建日期：(2004-6-18 15:05:31)
   * 
   * @param row
   * @return : true:自制单据; false:推式生成或退回的单据
   */
  private boolean isSelfMadeBill(int row) {
    return isNull(getCellValue(row, "vsourcecode"));
  }

  /**
   * 此处插入方法说明:根据调拨关系上的询价后价格是否可改判断 是否可以编辑调拨订单上的价格，税率等
   * 
   * @return :
   */
  private boolean isCanUpdatePrice(int iRow) throws Exception {

    boolean bEdit = false;
    // 如果存货为赠品，价格不能编辑
    String sFlargess = getCellValue(iRow, "flargess");

    if (Boolean.valueOf(sFlargess).booleanValue()) {
      return bEdit;
    }
    // 根据调拨关系上的价格是否可改规则决定是否可以编辑
    QryTransrelaVO[] voTransItem = getTransItem(new int[] {
      iRow
    });

    if (voTransItem != null && voTransItem[0] != null) {
      // 价格规则
      String priceruleflag = voTransItem[0].getPriceruleflag();
      // 如果价格规则为不取价，则允许编辑价格
      if (priceruleflag == null
          || priceruleflag.equals(String.valueOf(ConstVO.PRICE_RULE_NULL))) {
        bEdit = true;
      }
      else {
        // 如果询价，根据调拨关系上的参数：询价后价格是否可改
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
   * 创建日期：(2003-6-9 14:49:40)
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
    if (isRetBill(0))// 调拨退回单据
    {
      Arrays.fill(bEditArray, bEditFlag);
      CardPanelTool.setItemEdit(m_card, bEditArray, iPosArray, saKey);
      return;
    }

    // 折本、辅汇率是否可编辑
    if (!TOBillTool.isNull(voCurSel)) {
      BillHeaderVO voHead = voCurSel.getHeaderVO();
      BusinessCurrencyRateUtil currArith = getBizCheck().getCurrArith(
          voHead.getCoutcorpid());
      setRateEdit(false, voHead.getCoutcorpid(), voHead.getCoutcurrtype());
    }
    final int iPosCanEdit = 5;
    Arrays.fill(bEditArray, 0, iPosCanEdit, true);

    // 分货单形成的调拨订单可以修改调入方信息,其他单据形成的调拨订单不能修改调入方信息、调拨类型
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
      // 直运调拨调入仓库不能编辑
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
   * 此处插入方法说明。 创建日期：(2004-6-11 11:13:02)
   */
  public void initMultiNewLine(int[] iaRow,boolean bFillWhByHead) {
    if (iaRow == null || iaRow.length <= 0) {
      return;
    }
    // 设置要求到货日期
    UFDate dtTemp = new UFDate(m_NodeInfo.getLogDate(), false);
    for (int i = 0; i < iaRow.length; i++) {
      m_card.getBillModel().setValueAt(dtTemp, iaRow[i], "dplanoutdate");
      m_card.getBillModel().setValueAt(dtTemp, iaRow[i], "dplanarrivedate");
    }
    if(bFillWhByHead){
      // 设置调出仓库（由表头带入）
      TOBillTool.copyItemValueFromHead(m_card, "coutwhname", "coutwhid", iaRow);
      // 设置调入仓库（由表头带入）
      TOBillTool.copyItemValueFromHead(m_card, "cinwhname", "cinwhid", iaRow);
      // 设置出货仓库（由表头带入）
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
    // 设置仓库相关信息
    setWhInfo(iaRow);
    // 根据操作员带出业务员
    setDefaultPsnValue(iaRow);
  }

  /*
   * 根据操作员带出业务员
   */
  private void setDefaultPsnValue(int[] iaRow) {
    // 模板上如果设置了业务员的默认值，则不进行下面的处理
    if (getBodyItem("coutpsnid") != null
        && getBodyItem("coutpsnid").getDefaultValue() != null) {
      return;
    }
    IUserManageQuery iQuery = (IUserManageQuery) NCLocator.getInstance()
        .lookup(IUserManageQuery.class.getName());
    String sOutCorpid = getHeadTailItemValue("coutcorpid");
    // 获得业务员信息vo
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
      // 获得部门名称
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
        // 根据业务员带出所属部门
        m_card.getBillModel().setValueAt(pk_deptdoc, iaRow[i], "coutdeptid");
        m_card.getBillModel().setValueAt(deptdocname, iaRow[i], "coutdeptname");
      }
    }
  }

  /**
   * 设置每行的价格。 创建日期：(2004-7-22 14:20:29)
   * 
   * @param iRow
   *          行号数组
   */
  protected void setPrice(int[] iaRow) throws BusinessException {
    try {
      int[] iaNewRow = getNeedAskPriceLine(iaRow);

      if (iaNewRow.length > 0) {
        getPrice(iaNewRow);
        SetColor.resetColor(m_card);
        // 设置自由项字段的编辑器,SetColor.resetColor()重置着色器，自由项着色器被覆盖了，这里需要重置自由项着色器
        new InvAttrCellRenderer().setFreeItemRenderer(m_card);
      }
    }
    catch (BusinessException e) {
      sendErrMsg(e.getMessage());
    }
  }

  /**
   * 在询价之前，先调用此方法过滤掉经过手工修改过的表体行，得到需要询价的表体行
   * 
   * @param iaRow
   *          选择的表体行
   * @return 需要询价的表体行
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
    // 取价
    setPrice(iaRow);
  }

  /**
   * 设置折本折辅是否可编辑 创建日期：(2004-5-9 16:57:38) bBD302:是否主辅币核算 sOutCorp:调出公司
   * sCurrType:币种
   */
  protected void setRateEdit(boolean bBD302, String sOutCorp, String sCurrType)
      throws BusinessException {

    BusinessCurrencyRateUtil currArith = getBizCheck().getCurrArith(sOutCorp);

    boolean bZBrateEdit = false;

    bBD302 = false;
    // 是否主辅币核算
    if (!bBD302) {
      // 如果单主币核算
      // 如果币种等于本位币种则折本汇率不能修改
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
   * 入库调拨时,自动带出调入仓库的地址,地区,地点，没有则带出调入组织的 创建日期：(2004-8-31 10:58:44)
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
    // 查询调入仓库的地点，地址
    Object[][] objsStor = null;
    // 查询调入仓库的地点，地址
    if (!isNull(saInWhID)) {
      objsStor = nc.ui.scm.pub.CacheTool.getMultiColValue("bd_stordoc",
          "pk_stordoc", new String[] {
              "pk_address", "storaddr"
          }, (String[]) alInWhID.toArray(new String[iRows.length]));
    }
    // 调入组织
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
    String sPk_areacl = ""; // 地点
    String sAddress = ""; // 地址
    ArrayList alPk_areal = new ArrayList();
    ArrayList alArrivearea = new ArrayList();
    // 表头调拨类型
    String sFallocFlag = getHeadTailItemValue("bdrictflag");
    if (sFallocFlag.equals("false")) {
      sFallocFlag = "1";
    }
    else {
      sFallocFlag = "0";
    }
    for (int i = 0; i < iRows.length; i++) {
      // 调拨类型为入库调拨时
      if (sFallocFlag != null
          && Integer.parseInt(sFallocFlag) == ConstVO.ITransferType_INSTORE) {
        sPk_areacl = "";
        sAddress = "";
        if (objsStor != null && objsStor[i] != null) {
          if (objsStor[i][0] != null)
            sPk_areacl = objsStor[i][0].toString();
          // 地址
          if (objsStor[i][1] != null)
            sAddress = objsStor[i][1].toString();
        }

        // 调入仓库的地址地点信息为空，取调入组织的
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
        // 直运调拨时,保留用户输入
      }
    }
    // 取地区地点的名称信息
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
    // 调拨类型为入库调拨时
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
   * 点击现存量面板触发事件
   */
  public void onOnHandClicked(ActionEvent e, OnHandResultVO[] vos) {
  }

  /*
   * 存量面板双击的时候触发
   */
  public void onOnHandClicked(BillMouseEnent e, OnHandResultVO vo) {
    // TODO 自动生成方法存根
    if (e == null || vo == null) {
      return;
    }
    int iRow = e.getRow();
    if (vo.getCcalbodyid() != null
        && vo.getCcalbodyid().equals(getCellValue(iRow, "ctakeoutcbid"))) {
      // 把仓库信息带入
      m_card.getBillModel().setValueAt(vo.getCwhid(), iRow, "ctakeoutwhid");
      m_card.getBillModel().setValueAt(vo.getAttributeValue("warehousename"),
          iRow, "ctakeoutwhname");
      // 把批次信息带入
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
      // 把货位信息带入
      m_card.getBillModel().setValueAt(vo.getAttributeValue("cspaceid"), iRow,
          "ctakeoutspaceid");
      m_card.getBillModel().setValueAt(vo.getAttributeValue("vspacename"),
          iRow, "ctakeoutspacename");
      // 如果不是三方调拨订单，需要把仓库和货位信息复制到调出方
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
package nc.ui.to.pubtransfer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import nc.ui.hg.to.pub.StockNumParaHelper;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.ui.pub.msg.PfLinkData;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.scm.extend.IFuncExtend;
import nc.ui.scm.print.BillPrintTool;
import nc.ui.scm.print.PrintLogClient;
import nc.ui.scm.print.SplitParams;
import nc.ui.scm.print.SplitPrintParamDlg;
import nc.ui.scm.pub.CollectSettingDlg;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.ctrl.GenEditConst;
import nc.ui.scm.pub.panel.SetColor;
import nc.ui.scm.pub.sourceref.SourceRefDlg;
import nc.ui.to.outer.ClientOuterHelper;
import nc.ui.to.outer.SettlePathDlgForIC;
import nc.ui.to.pub.ConstHintMsg;
import nc.ui.to.pub.ExceptionUITools;
import nc.ui.to.pub.RemoteCall;
import nc.ui.to.pub.TOBillBusinessTool;
import nc.ui.to.pub.TOBillTool;
import nc.ui.uap.sf.SFClientUtil;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.ATPVO;
import nc.vo.scm.ic.exp.ATPNotEnoughException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.ctrl.GenMsgCtrl;
import nc.vo.scm.pub.ctrl.GenTimer;
import nc.vo.so.soreceive.SaleReceiveVO;
import nc.vo.to.pub.ATPWhenDeleteException;
import nc.vo.to.pub.BillHeaderVO;
import nc.vo.to.pub.BillItemVO;
import nc.vo.to.pub.BillVO;
import nc.vo.to.pub.ConstVO;
import nc.vo.to.pub.Log;
import nc.vo.to.pub.tools.ExceptionUtils;
import nc.vo.to.to002.SettlePathVO;
import nc.vo.to.to201.ConstVO201;
import nc.vo.trade.checkrule.VOChecker;

public class ButtonEventHandler {
  private TransferClientUI toftpanel = null;

  // added by songhy, 2008-08-29, start
  ArrayList<Integer> errrows = null; // ���桰���������㡱���кţ����ڷ���saveBill��

  // added by songhy, 2008-08-29, end

  private CardPanelCtrl getCardPanelCtrl() {
    return toftpanel.m_CardCtrl;
  }

  private ListPanelCtrl getListPanelCtrl() {
    return toftpanel.m_ListCtrl;
  }

  private Model getModel() {
    return toftpanel.m_Model;
  }

  private ButtonCtrl getButtonCtrl() {
    return toftpanel.m_ButtonCtrl;
  }

  /**
   * ****************************************** ���ܣ�<|> �������ڣ�(2004-3-18 8:47:35)
   * 
   * @return�� ******************************************
   */
  private void onCopy() throws BusinessException {
    try {
      startEdit();
      //�ѵ�ǰѡ��ҵ���������õ����ƺ�ĵ�����
      String cbiztypeid = getButtonCtrl().m_boBusiType.getSelectedChildButton()[0]
          .getTag();
      getCardPanelCtrl().copyBill(getModel().getCurRow(), cbiztypeid);
      // ���ñ�ͷҵ������
      /*getCardPanelCtrl().setBusiTypeId(
       getButtonCtrl().m_boBusiType.getSelectedChildButton()[0].getTag());*/

      toftpanel.setButtonStatusEdit();
      rightButtonRightControl();
    }
    catch (Exception ex) {
      // ȡ������
      onCancel();
      ExceptionUtils.wrappException(ex);
    }
  }

  /**
   * �����޸ģ����ƣ��˻ز�����Щ�༭״̬ʱ��һЩ��ͬ���� ����������(��Ϊ�����Ƚ�����) �������ڣ�(2005-11-22 10:18:12)
   */
  private void startEdit() throws BusinessException {
    toftpanel.getDefaultCalbodyID();
    // ��ǰ���б���ʽʱ�������л�������ʽ
    if (getCurPanel() == GenEditConst.LIST) {
      onSwitch();
    }
    getCardPanelCtrl().startEdit();
    
  }

  private int getCurPanel() {
    return toftpanel.getCurPanel();
  }

  /**
   * �������ڣ�(2004-2-10 17:09:40) ���ߣ����˾� ������ ���أ� ˵���������д���
   */
  private void onCopyLine() {
    getCardPanelCtrl().getCardPanel().getBillCardPanel().stopEditing();
    getCardPanelCtrl().copyLine();
  }

  /**
   * �������ڣ�(2004-2-10 17:09:40) ���ߣ����˾� ������ ���أ� ˵����ɾ������
   */
  private void onDelete() {
    GenTimer timer = new GenTimer();
    timer.start("ɾ����ʼ");/*-=notranslate=-*/

    ArrayList alVOs = new ArrayList();
    nc.vo.to.pub.BillVO[] voaDelete = null;
    int[] iaSelCount = null;
    String sWarningMsg = null;
    
    try {
    
      // ɾ��ǰ�����ݼ��
      try {
        checkBeforeUpdate(Const.ACTION_DELETE, alVOs);
      }
      catch (BusinessException be) {
        sWarningMsg = be.getMessage();
      }
      finally {
        voaDelete = (nc.vo.to.pub.BillVO[]) alVOs.get(0);
        iaSelCount = (int[]) alVOs.get(1);
        if (voaDelete == null || voaDelete.length <= 0) {
          if (sWarningMsg != null && sWarningMsg.length() > 0) {
            showWarningMessage(sWarningMsg);
          }
          return;
        }
      }
      //���ο�����չ
      toftpanel.getPluginProxy().beforeAction(nc.vo.scm.plugin.Action.DELETE, voaDelete);
      switch (nc.ui.pub.beans.MessageDialog
          .showOkCancelDlg(toftpanel, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("SCMCOMMON", "UPPSCMCommon-000219")/* @res "ȷ��" */,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                  "UPPSCMCommon-000178")/* @res "��ȷ��ɾ����ǰ������" */)) {

        case nc.ui.pub.beans.MessageDialog.ID_OK:
          break;
        case nc.ui.pub.beans.MessageDialog.ID_CANCEL:
          return;
      }
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000318")/* @res "����ɾ�������Ժ�..." */);
    
      // ɾ������
      deleteBills(voaDelete);
      timer.showExecTime("ɾ��");/*-=notranslate=-*/
      //���ο�����չ
      toftpanel.getPluginProxy().afterAction(nc.vo.scm.plugin.Action.DELETE, voaDelete);
      // ɾ�������ʾ
      if (sWarningMsg != null && sWarningMsg.length() > 0) {
        showWarningMessage(sWarningMsg);
      }
      // ��model��ɾ��
      getModel().removeData(iaSelCount);

      // ɾ������洦��
      if (getModel().getCount() <= 0) {
        // ɾ����û�е�����
        getModel().setCurRow(-1);
      }
      else {
        // ���ʣ��ĵ��������Ѿ�С�ڸղ�ɾ������ʼλ�ã�����ʾ��һ��
        if (getModel().getCount() - 1 < iaSelCount[0]) {
          getModel().setCurRow(0);
        }
        else// ��ʾ��һ��(�ղ�ɾ����λ�õ���һ��)
        {
          getModel().setCurRow(iaSelCount[0]);
        }
      }
      getBillCtrl().displayBill(getModel().getCurRow());

      // ���谴ť״̬
      setButtonStatusBrowse(getModel().getCurRow());

      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
          "UPP40093010-000066")/* @res "ɾ���ɹ���" */);
    }
    catch (Exception e) {
      ExceptionUITools.showMessage(e, toftpanel);
    }
  }

  private void deleteBills(BillVO[] voaDelete) throws Exception {
    for (int i = 0; i < voaDelete.length; i++) {
      voaDelete[i].setOperator(getNodeInfo().getUserID());
      // ����ͻ���IP��ַ,дҵ����־��
      voaDelete[i].setIPAdress(java.net.InetAddress.getLocalHost()
          .getHostAddress());
    }
    final String[] saType = new String[] {
      ConstVO.m_sBillDBDD
    };
    ArrayList<BillVO>[] list = new ArrayList[saType.length];
    for (int i = 0; i < saType.length; i++) {
      list[i] = new ArrayList<BillVO>();
    }
    for (int i = 0; i < voaDelete.length; i++) {
      String sTypeCode = voaDelete[i].getBillTypeCode();
      for (int j = 0; j < saType.length; j++) {
        if (sTypeCode.equals(saType[j]))
          list[j].add(voaDelete[i]);
      }
    }

    Object[] objAry = new Object[voaDelete.length];
    objAry[0] = toftpanel.m_clientLink;
    // ���ö���ִ�нű�ִ��ɾ��
    for (int i = 0; i < saType.length; i++) {
      if (list[i].size() > 0) {
        BillVO[] voaTemp = new BillVO[list[i].size()];
        voaTemp = (BillVO[]) list[i].toArray(voaTemp);
        try {

          nc.ui.pub.pf.PfUtilClient.processBatch("DELETE", saType[i],
              getNodeInfo().getLogDate(), voaTemp, objAry);

        }
        catch (ATPWhenDeleteException atde) {
          if (atde.getHint() == null) {
            String msg = atde.getMessage();
            throw new BusinessException(msg);
          }
          else {

            int iFlag = toftpanel.showYesNoMessage(atde.getMessage()
                + " \r\n"
                + nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009",
                    "UPP40093009-000203")/*
                 * @res "�Ƿ����ɾ������������"
                 */);
            if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
              // �Ƿ���п��������,��Ϊ����
              voaDelete[atde.getIndex()].getHeaderVO().setBatpcheck(
                  new UFBoolean(false));
              nc.ui.pub.pf.PfUtilClient.processBatch("DELETE", saType[i],
                  getNodeInfo().getLogDate(), voaTemp, objAry);
            }
            else// ȡ������ɾ��
            {
              throw new BusinessException("");
            }
          }
        }
      }
    }
  }

  private void showWarningMessage(String smsg) {
    toftpanel.showWarningMessage(smsg);
  }

  /*
   * ���ܣ� �޸�ɾ������ǰ����Ƿ����������޸ģ��Ƿ񱣴�ԭʼ�Ƶ��˵� ���أ�ArrayList:
   * 1��BillVO[]:���ͨ����VO���飻2��ͨ����VO������3��String:��ͨ����VO������Ϣ
   */
  private void checkBeforeUpdate(String sAction, ArrayList alData)
      throws BusinessException {
    BillVO[] checkVOs = null;

    int[] iaSelCount;
    if (getCurPanel() == GenEditConst.LIST) {
      iaSelCount = getBillCtrl().getCurSelectIndex();
      // ������VO
      ArrayList alRet = getSelectedBills(false);
      checkVOs = new BillVO[iaSelCount.length];
      checkVOs = (BillVO[]) alRet.toArray(checkVOs);
    }
    else {
      iaSelCount = new int[] {
        getModel().getCurRow()
      };
      checkVOs = new BillVO[] {
        (BillVO) getModel().getCurVO()
      };
    }
    if (iaSelCount == null || iaSelCount.length <= 0) {
      showHintMessage(ConstHintMsg.S_WARNING_CHOSE/* @res "��ѡ��Ҫ��������ݣ�" */);
      return;
    }
    // ȡ������
    String pk_corp = getNodeInfo().getCorpID();
    // ��ǰ����Ա
    String sOperator = getNodeInfo().getUserID().trim();

    // ���ͨ����VOs
    BillVO[] passVOs = null;
    int[] ipassCount = null;
    UFBoolean isPermit;

    if (getNodeInfo().getBPerModify() == null) {
      // �Ƿ����������޸ĵ���
      String sPermitUpdate = ConstVO.m_sPk_Para[ConstVO.m_iPara_SFYXSGFBR];

      String[] sPara = new String[] {
        sPermitUpdate
      };
      Hashtable htParas = nc.ui.to.pub.ClientCommonDataHelper.getParaValues(
          pk_corp, sPara);
      isPermit = new UFBoolean(htParas.get(sPermitUpdate).toString().trim());
      // ���浽NodeInfo��
      getNodeInfo().setBPerModify(isPermit);
    }
    else {
      isPermit = getNodeInfo().getBPerModify();
    }
    StringBuffer sErrorMsg = new StringBuffer();
    StringBuffer sErrorMsg2 = new StringBuffer();
    // ��鵱ǰVO �Ƿ����Ҫ��
    // �������������VO
    ArrayList alVOs = new ArrayList();
    // ��������VO������
    ArrayList alIndex = new ArrayList();
    for (int i = 0; i < checkVOs.length; i++) {
      // ԭʼ�Ƶ���
      String sOriBillMaker = checkVOs[i].getHeaderVO().getCoperatorid().trim();
      // ������
      String sAuditorid = checkVOs[i].getHeaderVO().getCauditorid();
      Integer iFstatus = checkVOs[i].getHeaderVO().getFstatusflag();
      if (!sOriBillMaker.equals(sOperator) && !isPermit.booleanValue()) {
        sErrorMsg.append(checkVOs[i].getHeaderVO().getVcode()).append("\n");
      }
      else if (!(iFstatus.intValue()==ConstVO.IBillStatus_FREE
          ||iFstatus.intValue()==ConstVO.IBillStatus_UNCHECKED
          || (VOChecker.isEmpty(sAuditorid)&&iFstatus.intValue()==ConstVO.IBillStatus_CHECKING))) {
        // ֻ�����ɺ�����δͨ���ĵ��ݲ����޸ģ�ɾ��
        sErrorMsg2.append(checkVOs[i].getHeaderVO().getVcode()).append("\n");

      }
      else {
        alVOs.add(checkVOs[i]);
        alIndex.add(new Integer(iaSelCount[i]));
      }
    }

    // ��ȫûͨ��
    if (alVOs.size() == 0) {
      passVOs = null;
      ipassCount = null;
    }
    else if (checkVOs.length == alVOs.size()) {
      passVOs = checkVOs;
      ipassCount = iaSelCount;
    }
    else {
      passVOs = new BillVO[alVOs.size()];
      ipassCount = new int[alIndex.size()];
      passVOs = (BillVO[]) alVOs.toArray(passVOs);
      for (int i = 0; i < alIndex.size(); i++) {
        ipassCount[i] = ((Integer) alIndex.get(i)).intValue();
      }
    }
    if (alData != null) {
      alData.add(passVOs);
      alData.add(ipassCount);
    }

    if (sErrorMsg.toString().trim().length() > 0) {
      String[] value = new String[] {
        String.valueOf(sErrorMsg)
      };
      if (Const.ACTION_DELETE.equals(sAction)) {
        throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40093010", "UPP40093010-000120", null, value)/* "����ɾ�����˵��ݵ��ݺţ�" */);
      }
      else if (Const.ACTION_UPDATE.equals(sAction)) {
        throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40093010", "UPP40093010-000123", null, value)/* "�˵��ݲ��ܱ������޸�\n���ݺ�: */);
      }
    }
    if (sErrorMsg2.toString().trim().length() > 0) {
      String[] value = new String[] {
        String.valueOf(sErrorMsg2)
      };
      if (Const.ACTION_DELETE.equals(sAction)) {
        throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("topub", "UPPtopub-000114", null, value)/* "���ڲ���ɾ�������ɻ������δͨ��״̬�ĵ��ݣ����ݺ�:{0} */);
      }
      else {
        throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("topub", "UPPtopub-000144", null, value)/* "���ڲ����޸ķ����ɻ������δͨ��״̬�ĵ��ݣ����ݺ�:{0} */);
      }
    }

  }

  private void afterSaveBill(Object[] objRet, BillVO voBill, String sActionName)
      throws BusinessException {
    BillHeaderVO voHeader = voBill.getHeaderVO();
    BillItemVO[] voaItem = voBill.getItemVOs();
    int iOldEditMode = getCardPanelCtrl().getEditMode();
    getCardPanelCtrl().endUpdate();

    ArrayList alRetData = (ArrayList) objRet[objRet.length - 1];
    // =========================================================
    // ���°ѷ��ص����ݷ���model��
    if (alRetData != null) {
      // ����PK
      String sBillid = (String) alRetData.get(0);
      // ���ݺ�
      String sBillcode = (String) alRetData.get(1);
      if (iOldEditMode == GenEditConst.NEW) {
        voHeader.setCbillid(sBillid);
        voHeader.setVcode(sBillcode);
      }
      else if (iOldEditMode == GenEditConst.UPDATE) {
        voHeader.setStatus(VOStatus.UNCHANGED);
        voHeader.setVcode(sBillcode);
        ArrayList alTemp = new ArrayList(voaItem.length);
        for (int item = 0; item < voaItem.length; item++) {
          if (voaItem[item].getStatus() == VOStatus.DELETED) {
            continue;
          }
          voaItem[item].setStatus(VOStatus.UNCHANGED);
          alTemp.add(voaItem[item]);
        }
        voaItem = (BillItemVO[]) alTemp.toArray(new BillItemVO[alTemp.size()]);
      }
      // ���ص�������Ϣ
      HashMap hmRetData = (HashMap) alRetData.get(alRetData.size() - 2);
      // ��ͷts
      voHeader.setTs((UFDateTime) hmRetData.get(sBillid
          + ConstVO201.sPostfix_CTS));
      // ��atpcheck�ı�־��Ϊ��
      voHeader.setBatpcheck(null);
      // ������������
      voHeader.setAttributeValue("billtypename", TOBillTool
          .getOrderNameByCode(voHeader.getCtypecode()));
      String sBillbid = "";
      int iCounter = 2; // start from 2!!!
      Integer headStatus = voHeader.getFstatusflag();
      for (int i = 0; i < voaItem.length; i++) {
        sBillbid = (String) alRetData.get(iCounter + i);
        if (voaItem[i].getCbill_bid() == null) {
          voaItem[i].setAttributeValue("vcode", sBillcode);
          voaItem[i].setAttributeValue("cbillid", sBillid);
          voaItem[i].setCbill_bid(sBillbid);
          voaItem[i].setFrowstatuflag(headStatus);
        }
        // ֻ���±������
        if (hmRetData.get(sBillbid + ConstVO201.sPostfix_CBTS) != null) {
          voaItem[i].setTs((UFDateTime) hmRetData.get(sBillbid
              + ConstVO201.sPostfix_CBTS));
        }
      }
      voBill.setChildrenVO(voaItem);

      // ˢ��model��card ��list
      if (iOldEditMode == GenEditConst.NEW) {
        if (toftpanel.getIsComeFromOther()) {
          // �������ݣ�ȫ���������ֵ��m_model,չʾ��������
          int iCount = toftpanel.getModelFrom5A().getCount();
          toftpanel.getModelFrom5A().insertData(iCount, voBill);
          onCancel();
          return;
        }
        else {
          int iCount = getModel().getCount();
          getModel().insertData(iCount, voBill);
          getModel().setCurRow(iCount);
        }
      }
      else if (iOldEditMode == GenEditConst.UPDATE) {
        getModel().setData(getModel().getCurRow(), voBill);
      }

      if (sActionName.equals(ConstVO.sActionSENDAFTERSAVE)) // ����ɹ�
      {
        getModel().freshTsAndStatus(getModel().getCurRow());
        voBill.getHeaderVO().setCauditorid(null);
        voBill.getHeaderVO().setFstatusflag(ConstVO.IBillStatus_CHECKING);
        getModel().setData(getModel().getCurRow(), voBill);
        // ����һ�±�ͷ
        getListPanelCtrl().setListHeadData(
            new CircularlyAccessibleValueObject[] {
              voBill.getHeaderVO()
            });

        showHintMessage(ConstHintMsg.S_ON_SUCCESS_SENDAUDIT);
      }
      // ============================================
      // ֻ��ʾ��Ƭ����,Ϊ���Ч��,��ͬ���б�������ʾ
      getBillCtrl().displayBill(getModel().getCurRow());

      // ͬ���б����������ʾ����
      getListPanelCtrl().setListHeadData(new CircularlyAccessibleValueObject[] {
        voBill.getHeaderVO()
      });

      if (sActionName.equals(ConstVO.sActionCOMMIT)) // ����ɹ�
      {
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
            "UPP40093010-000076")/*
         * @res "����ɹ�"
         */);
      }

      setButtonStatusBrowse(getModel().getCurRow());

    }
    else {
      setButtonStatusBrowse(-1);
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPP40093010-000077")/*
       * @res "���浥�ݳ���!"
       */);
    }
  }

  private void setButtonStatusBrowse(int iNum) {
    toftpanel.setButtonStatusBrowse(iNum);
  }

  /**
   * ���������󴫻ص�VO��ȡ�����������п��ܸı����Ϣ�� ����ˢ��ǰ̨����
   * 
   * @param voAudit
   */
  private void processDataAfterApprove(List<BillVO> lstOld, List<BillVO> lstNew) {
    final int len = lstOld.size();
    BillHeaderVO[] voOldHeads = new BillHeaderVO[len];
    BillHeaderVO[] voNewHeads = new BillHeaderVO[len];
    List<BillItemVO> lstOldBody = new ArrayList<BillItemVO>();
    List<BillItemVO> lstNewBody = new ArrayList<BillItemVO>();
    HashMap<String, BillItemVO> hmData = new HashMap<String, BillItemVO>();
    for (int i = 0; i < len; i++) {
      voOldHeads[i] = lstOld.get(i).getHeaderVO();
      voNewHeads[i] = lstNew.get(i).getHeaderVO();
      lstOldBody.addAll(Arrays.asList(lstOld.get(i).getItemVOs()));
      lstNewBody.addAll(Arrays.asList(lstNew.get(i).getItemVOs()));
      for (int j = 0, len2 = lstNew.get(i).getItemVOs().length; j < len2; j++) {
        hmData.put(lstNew.get(i).getItemVOs()[j].getCbill_bid(), lstNew.get(i)
            .getItemVOs()[j]);
      }
    }

    // 1.��Ҫˢ�µı�ͷ����
    final String[] saHeadField = new String[] {
        "taudittime", "dauditdate", "ts", "fstatusflag","cauditorid"
    };

    for (int i = 0; i < len; i++) {
      for (int j = 0; j < saHeadField.length; j++) {
        voOldHeads[i].setAttributeValue(saHeadField[j], voNewHeads[i]
            .getAttributeValue(saHeadField[j]));
      }
      // ����������
      if(VOChecker.isEmpty(voOldHeads[i].getCauditorid())){
        voOldHeads[i].setAttributeValue("cauditorname", null);
      }
      else{
        voOldHeads[i].setAttributeValue("cauditorname", getNodeInfo().getUserName());
      }
    }
    // ��������״̬����
    TOBillTool.setNameValueByFlag(new String[] {
      "fstatusflag"
    }, voOldHeads);

    // 2.��Ҫˢ�µı�������
    final String[] saBodyField = new String[] {
        "ts",
        "frowstatuflag",
        // ��д����
        "noutsumnum", "ntranponum", "norderoutnum", "norderoutassnum",
        "nordershouldoutnum", "norderbacknum", "norderbackassnum",
        "norderinnum", "norderinassnum", "nordersendnum", "nordersendassnum",
        "nordersignnum", "nordersignassnum", "norderinoutnum",
        "norderinoutmny", "nordertakenum", "nordertakemny",
        "norderwaylossassnum", "norderwaylossnum", "ntranpoassnum",
        "nnotinhandastnum", "nnotinhandnum",
        // ��ʶλ
        "bsendendflag", "boutendflag", "bsettleendflag", "bsettleendflag",
        "btakesettleendflag"
    };
    for (int i = 0; i < saBodyField.length; i++) {
      for (int j = 0, ilen = lstOldBody.size(); j < ilen; j++) {
        lstOldBody.get(j).setAttributeValue(
            saBodyField[i],
            hmData.get(lstOldBody.get(j).getCbill_bid()).getAttributeValue(
                saBodyField[i]));
      }
    }
    // ����������״̬����
    BillItemVO[] items = new BillItemVO[lstOldBody.size()];
    items = (BillItemVO[]) lstOldBody.toArray(items);
    TOBillTool.setNameValueByFlag(new String[] {
      "frowstatuflag"
    }, items);
  }

  /**
   * ����������ʱ��������pk���ɣ� ���ص��ͻ���
   */
  private String tempHeaderID = null;

  /**
   * ������������������������ʱ��������pk���ɣ� ���ص��ͻ��ˡ�
   * <b>����˵��</b>
   * @param pk_corp
   * @throws Exception
   * @author sunwei
   * @time 2009-11-20 ����09:06:47
   */
  private void setOID( String pk_corp ) throws Exception {
    if ( tempHeaderID == null ) { 
      Class[] parameterType = new Class[] {String.class};
      Object[] parameterValues = new Object[]{pk_corp};
      try {
        tempHeaderID = (String)RemoteCall.callEJBService("nc.bs.to.pub.CommonDataDMO","getOID",parameterType,parameterValues);
      }
      catch (Exception e) {
        ExceptionUtils.marsh(e);
      }

    }
  }

  /**
   * ��������������������ɵ�����pk��
   * <b>����˵��</b>
   * @return
   * @author sunwei
   * @time 2009-11-20 ����09:07:28
   */
  private String getOID() { 
    return tempHeaderID;
  }

  /**
   * ��������������������ص��ͻ��˵�����pk������ɹ��󡢶�����ʾ��ȡ������á�
   * <b>����˵��</b>
   * @author sunwei
   * @time 2009-11-20 ����09:07:46
   */
  public void clearOID() {
    tempHeaderID = null;
  }

  /**
   * �����������������ú�̨�õ�����������pk����ͷ��ʱ�ֶΣ���̨����ʱʹ�á�
   * ������ݱ���������жϣ�ǰ̨���������ٴε㱣�棬��������жϺ��̨�Ѿ����棬��ʱ�ٴα��滹������һ�ŵ���������ͬ�ĵ���
   * <b>����˵��</b>
   * @param headerVO
   * @throws Exception
   * @author sunwei
   * @time 2009-11-19 ����08:10:18
   */
  private void setPrimeryKey(BillHeaderVO headerVO) throws Exception {
    setOID(headerVO.getCoutcorpid());

    String headpkname = headerVO.getVOMeta().getPkColName();
    headerVO.setAttributeValue(headpkname+"_temp", getOID());
  }

  /**
   * �����ߣ����˾� ���ܣ������޸ĵĵ��� �д�����Ҫ���쳣�׳���Ӱ�������� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  protected void saveBill(String sActionName) throws Exception {
    GenTimer timer = new GenTimer();
    GenTimer timer2 = new GenTimer();
    timer.start();
    timer2.start();
    
    //ִ����֤��ʽ
    if(!getCardPanelCtrl().getCardPanel().getBillCardPanel().getBillData().execValidateFormulas())
      return;

    BillVO voNew = getSaveBill(sActionName);
    
    boolean flag = false;//�ж��Ƿ�����������  zhw  2011-05-10
    if(PuPubVO.getString_TrimZeroLenAsNull(voNew.getParentVO().getPrimaryKey())==null)
    	flag = true;
    
    //��ʽ���ɵĵ���������ʽ���ɱ�־����̨�ж��Ƿ���ϵͳ��ʽ����ʱ������Ϊ����
    if (toftpanel.getIsComeFromOther()) {
      BillHeaderVO header = voNew.getHeaderVO();
      header.setAttributeValue("bpullby5Aflag", true);
    }
    
    //���ο�����չ
    toftpanel.getPluginProxy().beforeAction(nc.vo.scm.plugin.Action.SAVE, new BillVO[]{voNew});
    ArrayList alParam = getSaveParam(voNew);

    Object[] objRet = null;

    BillScrollPane bsp = toftpanel.m_CardCtrl.getCardPanel().getBillCardPanel()
        .getBodyPanel();
    try {
      //���õõ��ı�ͷpk
      setPrimeryKey(voNew.getHeaderVO());
      
      objRet = nc.ui.pub.pf.PfUtilClient.processBatch(toftpanel, sActionName,
          voNew.getBillTypeCode(), getNodeInfo().getLogDate(), new BillVO[] {
            voNew
          }, new Object[] {
            alParam
          });
   
    }
    catch (ATPNotEnoughException ane) {
      // add by songhy, 2008-08-06, start
      // ʵ�ֹ��ܣ������������浥��ʱ��������Ϣ�����б���ɫ���
      ArrayList<ATPVO> atpVOs = ane.getAtpVoList();
      if ((atpVOs != null) && (atpVOs.size() > 0)) {
        // �� ArrayList ��ʽ��ñ��������������������³���������е��к�
        errrows = getErrorLines(voNew, atpVOs);
        SetColor.setRowColor(bsp, errrows, Color.YELLOW);
      }
      // add by songhy, 2008-08-06, end

      if (ane.getHint() == null) {
        showErrorMessage(ane.getMessage());
        return;
      }
      else {

        int iFlag = toftpanel.showYesNoMessage(ane.getMessage()
            + " \r\n"
            + nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
                "UPP40093010-000130")/*
             * @res "�Ƿ�����������������"
             */);
        if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
          // �Ƿ���п��������,��Ϊ����
          voNew.getHeaderVO().setBatpcheck(UFBoolean.FALSE);
          objRet = nc.ui.pub.pf.PfUtilClient.processBatch(null, sActionName,
              voNew.getBillTypeCode(), getNodeInfo().getLogDate(),
              new BillVO[] {
                voNew
              }, new Object[] {
                alParam
              });
        }
        else// ȡ�����ݱ���
        {
          return;
        }
      }
    }
    
    clearOID();
    
    timer.showExecTime("@@��ƽ̨���棺��");/*-=notranslate=-*/
    toftpanel.getPluginProxy().afterAction(nc.vo.scm.plugin.Action.SAVE, new BillVO[]{voNew});
    // ========================================================
    // [[��ʾ��Ϣ][new_PK_body1,new_PK_body2,....]]
    // ���浱ǰ���ݵ�OID
    // �������ݴ��� ------------------- EXIT --------------------------
    if (objRet == null || objRet.length <= 0
        || objRet[objRet.length - 1] == null) {
      setButtonStatusBrowse(-1);
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "SCMCOMMON", "UPPSCMCommon-000300")/*
       * @res "�Ѿ����棬������ֵ���������²�ѯ���ݡ�"
       */);
    }
    // �������
    afterSaveBill(objRet, voNew, sActionName);
    timer2.showExecTime("@@�����������棺��");/*-=notranslate=-*/
    
    if(flag)// add by  zhw  ����������� ˢ��ʱֻˢ�µ�ǰ��
    	toftpanel.querysql=" cbillid  = '"+getModel().getCurVO().getParentVO().getPrimaryKey()+"'";
    	
  }


  /**
   * @author songhy
   * @param billVO
   *          ��Ҫ����ĵ��� billVO
   * @param atpVOs
   *          �����������VO
   * @return ��Ҫ������ʾ�ı������к� ArrayList
   */
  private ArrayList<Integer> getErrorLines(BillVO billVO,
      ArrayList<ATPVO> atpVOs) {
    ArrayList<Integer> errrows = new ArrayList<Integer>();

    // ������������Ĵ��Id ����Set��
    Set<String> atpVOIds = new java.util.HashSet<String>();
    for (int i = 0, len = atpVOs.size(); i < len; i++) {
      atpVOIds.add(atpVOs.get(i).getCinventoryid());
    }

    // ��¼����������Ĵ��Id���ڵı������к�
    BillItemVO[] billItemVOs = billVO.getItemVOs();
    for (int j = 0, m = billItemVOs.length; j < m; j++) {
      if (atpVOIds.contains(billItemVOs[j].getCoutinvid())) {
        errrows.add(j);
      }
    }

    return errrows;
  }

  private AbstractBillCtrl getBillCtrl() {
    return toftpanel.getBillCtrl();
  }

  protected void onDeleteLine() throws BusinessException {
    // ��ɾ���е���Դid�ӻ�����������Ա��������ʱ�����ٴβ�ѯ��
    int[] selRow = getCardPanelCtrl().getCardPanel().getBillCardPanel()
        .getBillTable().getSelectedRows();
    String csourcebid = null;
    for (int i = 0; i < selRow.length; i++) {
      csourcebid = (String) getCardPanelCtrl().getCardPanel()
          .getBillCardPanel().getBillModel()
          .getValueAt(selRow[i], "csourcebid");
      toftpanel.getRefBillIds().remove(csourcebid);
    }

    getCardPanelCtrl().deleteLine();
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2004-2-26 11:05:58)
   */
  private void onDocument() {
    try {
      BillHeaderVO voHead = (BillHeaderVO) getModel().getHeaderVO(
          getModel().getCurRow());
      String sBillid = voHead.getPrimaryKey();
      String sBillType = voHead.getCbilltype();
      // String sVCode = voHead.getVcode();
      nc.ui.scm.file.DocumentManager.showDM(toftpanel, sBillType, sBillid);
    }
    catch (Exception e) {
      nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint(e.getMessage());
    }

  }

  public void onBtnClick(nc.ui.pub.ButtonObject bo) throws Exception {
    GenTimer timer = null;
    // ����
    if (bo.getParent() == getButtonCtrl().m_boBusiType) {
      onBusiType(bo);
    }
    else if (bo.getParent() == getButtonCtrl().m_boNew) {
      onNew(bo);
    }
    else if (bo == getButtonCtrl().m_boAddLine) {
      onAddLine();
      try {
        //���ο�����չ
        toftpanel.getPluginProxy().onAddLine();
      }
      catch (Exception ex) {
        ExceptionUITools.showMessage( ex,toftpanel);
      }
    }
    else if (bo == getButtonCtrl().m_boCopyLine) {
      onCopyLine();
    }
    else if (bo == getButtonCtrl().m_boPasteLine) {
      onPasteLine();    
      try {
        //���ο�����չ
        toftpanel.getPluginProxy().onPastLine();
      }
      catch (Exception ex) {
        ExceptionUITools.showMessage( ex,toftpanel);
      }
    }
    else if (bo == getButtonCtrl().m_boDeleteLine) {
      onDeleteLine();
    }
    else if (bo == getButtonCtrl().m_boInsertLine) {
      onInsertLine();
    }
    else if (bo == getButtonCtrl().m_boPasteLineToTail) { // ճ���е���β
      onPasteLineToTail();
    }
    else if (bo == getButtonCtrl().m_boCardEdit) { // ��Ƭ�༭
      onCardEdit();
    }
    else if (bo == getButtonCtrl().m_boNewRowNo) { // �����к�
      onNewRowNo();
    }
    else if (bo == getButtonCtrl().m_boAskPrice) { // ѯ��
      this.toftpanel.m_CardCtrl.askPrice();
    }
    else if (bo == getButtonCtrl().m_boDRSQ) { // ���յ�����������
      onRefDRSQAddLine(bo);
    }
    else if (bo == getButtonCtrl().m_boSwitch) {
      onSwitch();
    }
    else if (bo == getButtonCtrl().m_boQuery) {
      onQuery();
    }
    else if(bo == getButtonCtrl().m_boRefresh){//ˢ��
      onRefresh();
    }
    else if (bo == getButtonCtrl().m_boUpdate) {
      onUpdate();
    }
    else if (bo == getButtonCtrl().m_boCancel) {
      onCancel();
    }
    else if (bo == getButtonCtrl().m_boSave) {
      timer = new GenTimer();
      timer.start("����");/*-=notranslate=-*/
      onSave();
      timer.stopAndShow("����");/*-=notranslate=-*/
    }
    // ����
    else if (bo == getButtonCtrl().m_boAudit) {
      timer = new GenTimer();
      timer.start("����");/*-=notranslate=-*/
      onApprove();
      timer.stopAndShow("����");/*-=notranslate=-*/
    }
    else if (bo == getButtonCtrl().m_boCancelAudit) {
      timer = new GenTimer();
      timer.start("����");/*-=notranslate=-*/
      onUnApprove();
      timer.stopAndShow("����");/*-=notranslate=-*/
    }
    else if (bo == getButtonCtrl().m_boDelete) {
      onDelete();
    }
    else if (bo == getButtonCtrl().m_boCopy) {
      onCopy();
    }
    // �˻�
    else if (bo == getButtonCtrl().m_boReturn) {
      onReturn();
    }
    // �ر�
    else if (bo == getButtonCtrl().m_boClose) {
      setBillStatus(ConstVO.IBillStatus_CLOSED, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40093010", "UPT40093010-000028")/*
       * @res
       * "�ر�"
       */);
    }
    // �򿪺󵥾�״̬Ϊ����
    else if (bo == getButtonCtrl().m_boOpen) {
      setBillStatus(ConstVO.IBillStatus_PASSCHECK, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40093010", "UPT40093010-000029")/*
       * @res
       * "��"
       */);
    }
    // �ⶳ�󵥾�״̬Ϊ����
    else if (bo == getButtonCtrl().m_boFresh) {
      setBillStatus(ConstVO.IBillStatus_PASSCHECK, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("common", "UC001-0000031")/*
       * @res "�ⶳ"
       */);
    }
    // ����
    else if (bo == getButtonCtrl().m_boFreeze) {
      setBillStatus(ConstVO.IBillStatus_FREEZED, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("common", "UC001-0000030")/*
       * @res "����"
       */);
    }
    else if (bo == getButtonCtrl().m_boSendAudit) {
      onSendAudit();
    }
    else if (bo == getButtonCtrl().m_boJointCheck) {
      onJointCheck();
    }
    else if (bo == getButtonCtrl().m_boDocument) {
      onDocument();
    }
    else if (bo == getButtonCtrl().m_boLocate) {
      onLocate();
    }
    else if (bo == getButtonCtrl().m_boOnHandShowHidden) {
      onOnHandShowHidden();
    }
    else if (bo == getButtonCtrl().m_boAllATP) {
      onAllATP();
    }
    else if (bo == getButtonCtrl().m_boAllSP) {
      onAllSP();
    }
    else if (bo == getButtonCtrl().m_PageBtnCtrl.getFirst()) {
      onFirst();
    }
    else if (bo == getButtonCtrl().m_PageBtnCtrl.getPre()) {
      onPrevious();
    }
    else if (bo == getButtonCtrl().m_PageBtnCtrl.getNext()) {
      onNext();
    }
    else if (bo == getButtonCtrl().m_PageBtnCtrl.getLast()) {
      onLast();
    }
    else if (bo == getButtonCtrl().m_boPreview) {
      onPreview();
    }
    else if (bo == getButtonCtrl().m_boPrint) {
      onPrint();
    }
    else if (bo == getButtonCtrl().m_boAuditStatus) {
      onQueryAudit();
    }
    else if (bo == getButtonCtrl().m_boStockLock) {
      onStockLock();
    }
    // ��������
    else if (bo == getButtonCtrl().m_boSendPlan) {
      onSendPlan();
    }
    // ��������
    else if (bo == getButtonCtrl().m_boFillPlan) {
      onFillPlan();
    }
    // ָ������·��
    else if (bo == getButtonCtrl().m_boSelSettlePath) {
      onSelSettlePath();
    }
    // ����"�ֵ�Ԥ��"�ķ���
    else  if (bo == getButtonCtrl().m_splitPrint) {
        onSplitPreview();
      }
    // ȡ������·��
    else if (bo == getButtonCtrl().m_boDelSettlePath) {
      onDelSettlePath();
    }
    else if (bo == getButtonCtrl().m_boBillCombin) {
      onBillCombin();
    }
    else if (bo == getButtonCtrl().m_boCancelRef) {
      onCancelRef();
    } // GenEditConst.SOURCE������ֱ�˽���
    else if (toftpanel.getCurPanel() == GenEditConst.SOURCE) {
      toftpanel.onBHZYBtnClick(bo);
    }else if(bo == getButtonCtrl().m_stockNumPara){
    	return;
    }
    else {
      // ��չ��ť
      toftpanel.onExtendBtnsClick(bo);
      // ֧�ֹ�����չ
      if (getCurPanel() == GenEditConst.CARD)
        ((IFuncExtend) toftpanel).doAction(bo, toftpanel, getCardPanelCtrl()
            .getCardPanel().getBillCardPanel(), getListPanelCtrl()
            .getListPanel().getBillListPanel(),
            nc.ui.scm.extend.IFuncExtend.CARD);
      else if (getCurPanel() == GenEditConst.LIST)
        ((IFuncExtend) toftpanel).doAction(bo, toftpanel, getCardPanelCtrl()
            .getCardPanel().getBillCardPanel(), getListPanelCtrl()
            .getListPanel().getBillListPanel(),
            nc.ui.scm.extend.IFuncExtend.LIST);
    }

    // ���ݸ��ƻ��������˻�
    if (getCardPanelCtrl().getEditMode() == GenEditConst.NEW
        && !toftpanel.getIsComeFromOther()) {
      getCardPanelCtrl().setItemEdit(-1);
    }
    // �����޸�
    else if (getCardPanelCtrl().getEditMode() == GenEditConst.UPDATE) {
      getCardPanelCtrl().setItemEdit(getModel().getCurRow());
    }
  }

  private void onRefresh() {
    toftpanel.refresh();
}

private void onRefDRSQAddLine(ButtonObject bo) throws Exception {
    // ��ǰ���ݵ�ҵ������id
    String cbiztypeid = (String) getCardPanelCtrl().getCardPanel()
        .getBillCardPanel().getHeadItem("cbiztypeid").getValueObject();
    // 1 ���������У���������������5A��ʽ���ɡ�
    if (!TOBillBusinessTool.isHaveBizType(ConstVO.m_sBillDRSQ,
        ConstVO.m_sBillDBDD, cbiztypeid)) {
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093009", "UPP40093009-000239")/* @res "��ǰ����δ�������ε���Ϊ�������룡" */);
    }
    bo.setTag("5A:" + cbiztypeid);
    
    SourceRefDlg.getCacheToMapAdapter().clear();//��ת�����滺�棬���¹���ת������
    SourceRefDlg.childButtonClicked(bo, getNodeInfo().getCorpID(), null,
        getNodeInfo().getUserID(), ConstVO.m_sBillDBDD, toftpanel);
    if (SourceRefDlg.isCloseOK()) {
      // ������Դ���շ��ص��ݾۺ�VO������
      BillVO[] vos = (BillVO[]) SourceRefDlg.getRetsVos();
      if (vos != null && vos.length > 0) {
        // ���ò��յ��ĵ��ݵ���Ƭ����
        getCardPanelCtrl().setRefBillToCard(vos,toftpanel);
      }
    }
  }

  private void onCancelRef() {
    // ��ջ�����Ѳ��յ��ݱ���VO���Ա��´β��տ��Բ�ѯ��
    toftpanel.getRefBillIds().clear();

    // ���÷ǲ����ⲿ����
    toftpanel.setIsComeFromOther(false);
    // �ѱ������Դ��5A�ĵ����������Ƶ�Model��
    getModel().setData(toftpanel.getModelFrom5A().getData());
    // ��ջ���
    toftpanel.getModelFrom5A().setData(null);
    // ɾ����û�е�����
    if (getModel().getCount() > 0) {
      getModel().setCurRow(0);
    }
    else {
      getModel().setCurRow(-1);
    }

    // ȡ���޸ģ������޸�״̬��
    getCardPanelCtrl().cancelUpdate();

    // ���ҳ������
    getBillCtrl().displayBill(getModel().getCurRow());

    // ���谴ť״̬
    setButtonStatusBrowse(getModel().getCurRow());

    // �����л���ť״̬
    toftpanel.showBtnSwitch();

    showHintMessage(ConstHintMsg.S_ON_SUCCESS_CANCEL);
  }

  // �������š����������˾�ǵ�ǰ��½��˾����������ͨ��ʱ ��ť������
  private void onFillPlan() throws Exception {
    BillVO vo = (BillVO) getModel().getCurVO();

    BillHeaderVO header = vo.getHeaderVO();
    BillItemVO[] items = vo.getItemVOs();

    ArrayList list = new ArrayList();
    for (int i = 0; i < items.length; i++) {
      // ��δ����-ֱ�˰��Ž����ĵ�������
      if ((items[i].getBarrangedflag() == null 
          || !items[i].getBarrangedflag().booleanValue())) {
        list.add(items[i]);
      }
    }

    if (list.size() == 0) {
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093009", "UPP40093009-000240")/*
       * @res
       * "���ܱ�������ȫ���������Ž�����û�б����п������������ţ�"
       */);
    }

    BillItemVO[] newItems = new BillItemVO[list.size()];
    newItems = (BillItemVO[]) list.toArray(newItems);

    BillVO newVO = new BillVO();
    newVO.setParentVO(header);
    newVO.setChildrenVO(newItems);

    // ��toftpanel�м��ز���ֱ�˽���
    toftpanel.addBHZYCardUI(newVO);
  }

  // �������š����������˾�ǵ�ǰ��½��˾����������ͨ�� ��ť������
  private void onSendPlan() throws BusinessException {
    BillVO vo = (BillVO) getModel().getCurVO();
    BillHeaderVO header = vo.getHeaderVO();
    BillItemVO[] items = vo.getItemVOs();
    //1�����ʱ�����������û��ˢ�½��棬������������ʱ�ű�����
    Class[] parameterType = new Class[] {BillVO.class};
    Object[] parameterValues = new Object[]{vo};
    try {
      RemoteCall.callEJBService("nc.bs.to.pub.CommonDataDMO","checkTimeStamp",parameterType,parameterValues);
    }
    catch (Exception e) {
      ExceptionUtils.marsh(e);
    }
    
    // 2 ���������У���������Դ����������
    if (!TOBillBusinessTool.isHaveBizType(ConstVO.m_sBillDBDD,
        ConstVO.SO_Receive, header.getCbiztypeid())) {
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093009", "UPP40093009-000241")/* @res "��ǰ����δ�������ε���Ϊ��������" */);
    }
    // 1 �����رգ�������������
    // 3 ���ֵ�������������������
    ArrayList list = new ArrayList();
    for (int i = 0; i < items.length; i++) {
      boolean bsendendflag = items[i].getBsendendflag().booleanValue();
      boolean bretractflag = items[i].getNnum().compareTo(new UFDouble(0)) < 0 ? true
          : false;
      if (!bsendendflag && !bretractflag) {
        list.add(items[i]);
      }
    }
    if (list.size() == 0) {
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093009", "UPP40093009-000242")/*
       * @res
       * "���ܱ������ѷ����رջ�Ϊ���֣�û�б����п��Խ��з������ţ�"
       */);
    }

    BillItemVO[] newItems = new BillItemVO[list.size()];
    newItems = (BillItemVO[]) list.toArray(newItems);

    BillVO newVO = new BillVO();
    newVO.setParentVO(header);
    newVO.setChildrenVO(newItems);

    // VO����
    SaleReceiveVO sendVO = (SaleReceiveVO) PfChangeBO_Client
        .pfChangeBillToBill(newVO, "5X", "4331");

    PfLinkData linkData = new PfLinkData();
    linkData.setUserObject(sendVO);

    SFClientUtil.openLinkedADDDialog("40060401", toftpanel, linkData);
  }

  private void onNew(ButtonObject bo) throws Exception {
    SourceRefDlg.getCacheToMapAdapter().clear();//��ת�����滺�棬���¹���ת������
    SourceRefDlg.childButtonClicked(bo, getNodeInfo().getCorpID(), null,
        getNodeInfo().getUserID(), ConstVO.m_sBillDBDD, toftpanel);
    
//    zhf add ���õ�ǰѡ��ҳǩΪ ��һҳǩ
    getBillCardPanel().getBodyTabbedPane().setSelectedIndex(0);
//    zhf end  
    
    
    if (nc.ui.pub.pf.PfUtilClient.makeFlag) {
      // ���Ƶ��ݴ���
      onNew(getButtonCtrl().m_boBusiType.getSelectedChildButton()[0].getTag());
    }
    else {
      if (SourceRefDlg.isCloseOK()) {
        // ������Դ���շ��ص��ݾۺ�VO������
        BillVO[] vo = (BillVO[]) SourceRefDlg.getRetsVos();

        String tag = bo.getTag();
        int index = tag.indexOf(":");
        String bizType = tag.substring(index + 1, tag.length());

        // ����ҵ������
        setBizType(vo, bizType);

        // Ϊ���յ�������Ĭ��ֵ
        setDefaultData(vo);

        // ���浥��
        getModel().setData(new ArrayList(Arrays.asList(vo)));

        toftpanel.setReftoListPanel();

        // getCardPanelCtrl().getCardPanel().getBillCardPanel().setCardData(vo);
        // //������
      }
    }

  }

  /**
   * Ϊ���յ�������Ĭ��ֵ
   * 
   * @param vo
   */
  private void setDefaultData(BillVO[] vo) {
    if (vo != null && vo.length > 0) {
      UFDate date = new UFDate(getNodeInfo().getLogDate());
      for (int i = 0; i < vo.length; i++) {
        // ���üƻ��������ƻ���������
        for (int j = 0; j < vo[i].getItemVOs().length; j++) {
          if (vo[i].getItemVOs()[j].getDplanarrivedate() == null) {
            vo[i].getItemVOs()[j].setDplanarrivedate(date);
          }
          if (vo[i].getItemVOs()[j].getDplanoutdate() == null) {
            vo[i].getItemVOs()[j].setDplanoutdate(date);
          }
        }
      }
    }
  }

  /**
   * Ϊ���յ�������ҵ������
   * 
   * @param vo
   * @param bizType
   */
  private void setBizType(BillVO[] vo, String bizType) {
    if (vo != null && vo.length > 0) {
      for (int i = 0; i < vo.length; i++) {
        vo[i].getHeaderVO().setCbiztypeid(bizType);
      }
    }
  }

  private void onBusiType(ButtonObject bo) {
    // �̻���������,ϵͳ�Զ�ˢ��������Դ���ݰ�ť
    PfUtilClient.retAddBtn(getButtonCtrl().m_boNew, getNodeInfo().getCorpID(),
        ConstVO.m_sBillDBDD, bo);
  }

  private void onDelSettlePath() throws Exception {
    BillVO bill = (BillVO) getModel().getCurVO();
    if (bill == null) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon",
          "UPPSCMCommon-000167")/* @res "û��ѡ�񵥾�" */);
      return;
    }
    String oldpathid = bill.getHeaderVO().getCsettlepathid();
    if (oldpathid.length() == 0) {
      return;
    }

    bill.setOperator(toftpanel.m_clientLink.getUser());
    bill.getHeaderVO().setCsettlepathid(null);
    bill.getHeaderVO().setCsettlepathname(null);
    bill.getHeaderVO().setStatus(VOStatus.UPDATED);

    BillVO ret = ClientOuterHelper.modifySettlePath(toftpanel.m_clientLink,
        bill);
    getModel().setData(getModel().getCurRow(), ret);

    getBillCtrl().displayBill(getModel().getCurRow());

    if (getCurPanel() == GenEditConst.CARD) {
      getCardPanelCtrl().getCardPanel().getBillCardPanel()
          .execHeadLoadFormulas();
    }
    else {
      getListPanelCtrl().getListPanel().getBillListPanel().getHeadBillModel()
          .execLoadFormula();
    }
    setButtonStatusBrowse(getModel().getCurRow());
  }

  private void onSelSettlePath() throws Exception {
    BillVO bill = (BillVO) getModel().getCurVO();
    if (bill == null) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon",
          "UPPSCMCommon-000167")/* @res "û��ѡ�񵥾�" */);
      return;
    }
    if (bill.getHeaderVO().getCtypecode().equals(ConstVO.m_sBillSFDBDD)) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009",
          "UPP40093009-000265")/* @res "����������������ָ������·����" */);
      return;
    }
    String cinCorp = bill.getHeaderVO().getCincorpid(); // ���빫˾
    String coutCorp = bill.getHeaderVO().getCoutcorpid(); // ������˾

    SettlePathDlgForIC dlgModifySettlePath = new SettlePathDlgForIC(cinCorp,
        coutCorp, toftpanel, "ָ������·��");

    if (dlgModifySettlePath == null)
      return;

    dlgModifySettlePath.show();
    if (dlgModifySettlePath.getResult() != nc.ui.pub.beans.UIDialog.ID_OK)
      return;

    SettlePathVO[] spvos = dlgModifySettlePath.getReturnVOs();
    String cpathid = dlgModifySettlePath.getSelectedSettlePathID();
    if (spvos == null || spvos.length == 0 || cpathid == null) {
      ExceptionUtils
          .wrappBusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "40093009", "UPP40093009-000243")/* @res "ѡ��Ľ���·��Ϊ��!" */);
      return;
    }

    SettlePathVO spvo = null;
    for (int i = 0; i < spvos.length; i++) {
      if (spvos[i].getHeadVO().getPrimaryKey().equals(cpathid)) {
        spvo = spvos[i];
        break;
      }
    }
    String cpathname = spvo.getHeadVO().getCpathname();

    if (cpathid == null) {
      SCMEnv.out(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009",
          "UPP40093009-000244")/* @res "·���գ�" */);
      return;
    }

    String oldpathid = bill.getHeaderVO().getCsettlepathid();
    if (cpathid.equalsIgnoreCase(oldpathid)) {
      int ok = showOkCancelMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40094030", "UPP40094030-000244")/*
       * @res "����·��û�з����ı䣡�Ƿ�������½���·����"
       */);
      if (ok == 2)
        return;
    }

    bill.setOperator(toftpanel.m_clientLink.getUser());
    bill.getHeaderVO().setCsettlepathid(cpathid);
    bill.getHeaderVO().setCsettlepathname(cpathname);
    bill.getHeaderVO().setStatus(VOStatus.UPDATED);

    BillVO ret = ClientOuterHelper.modifySettlePath(toftpanel.m_clientLink,
        bill);
    getModel().setData(getModel().getCurRow(), ret);

    getBillCtrl().displayBill(getModel().getCurRow());

    setButtonStatusBrowse(getModel().getCurRow());
  }

  /**
   * ��ʾ������Ϣ
   * 
   * @param err
   *          java.lang.String
   */
  public int showOkCancelMessage(String msg) {
    return MessageDialog.showOkCancelDlg(toftpanel, null, msg);
  }

  /**
   * ��������:���õ���������״̬ ����: int iChangeStatus ----- Ҫ�޸�Ϊ��״̬ String sOperationName
   * ----- �������ƣ���رա������� ����ֵ: �쳣:
   */
  private void setBillStatus(int iChangeStatus, String sOperationName) {
    BillVO voSel = (BillVO) getModel().getCurVO();
    if (voSel == null || voSel.getParentVO() == null) {
      String[] value = new String[] {
        sOperationName
      };
      showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPP40093010-000125", null, value));// "��ǰѡ�еĵ�����û�п��Խ���"
      // +
      // sOperationName
      // +
      // "�����ģ������");
      return;
    }
    BillHeaderVO voHeader = (BillHeaderVO) voSel.getParentVO();
    // ���ò���Ա,����������
    voSel.setOperator(getNodeInfo().getUserID());
    voSel.getHeaderVO().setBatpcheck(new UFBoolean(true));
    // ���ùرա��򿪡����ᡢ�ⶳ�Ķ���
    for (int i = 0; i < voSel.getChildrenVO().length; i++) {
      int iStatu = -1;
      if (sOperationName.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPT40093010-000028")/* @res "�ر�" */)) {
        iStatu = ConstVO.A_CLOSE;
      }
      else if (sOperationName.equals(nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40093010", "UPT40093010-000029")/* @res "��" */)) {
        iStatu = ConstVO.A_OPEN;
      }
      else if (sOperationName.equals(nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("common", "UC001-0000030")/* @res "����" */)) {
        iStatu = ConstVO.A_FREEZE;
      }
      else if (sOperationName.equals(nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("common", "UC001-0000031")/* @res "�ⶳ" */)) {
        iStatu = ConstVO.A_UNFREEZE;
      }
      ((BillItemVO) voSel.getChildrenVO()[i]).setAction(iStatu);
    }
    String[] sBillIDs = new String[] {
      voHeader.getCbillid()
    };
    try {
      // ���ýӿ��޸�״̬
      try {
        nc.ui.to.outer.ClientOuterHelper.setBillStatusWithLock("to_bill",
            sBillIDs, iChangeStatus, new BillVO[] {
              voSel
            });
      }
      catch (ATPNotEnoughException ane) {
        if (ane.getHint() == null) {
          showErrorMessage(ane.getMessage());
          return;
        }
        else {

          int iFlag = toftpanel.showYesNoMessage(ane.getMessage()
              + " \r\n"
              + nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
                  "UPP40093010-000206", null, new String[] {
                    sOperationName
                  })/*
               * @res "�Ƿ�����ر�/��/����/�ⶳ����������"
               */);
          if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
            // �Ƿ���п��������,��Ϊ����
            voSel.getHeaderVO().setBatpcheck(new UFBoolean(false));
            nc.ui.to.outer.ClientOuterHelper.setBillStatusWithLock("to_bill",
                sBillIDs, iChangeStatus, new BillVO[] {
                  voSel
                });
          }
          else// ȡ������ɾ��
          {
            return;
          }
        }
      }

      voHeader.setFstatusflag(iChangeStatus);

      // ˢ��mode
      int index = getModel().getCurRow();
      getModel().setData(index, voSel);
      getModel().freshTsAndStatus(index);
      // ����ˢ��
      TOBillTool.setNameValueByFlag(new String[] {
        "fstatusflag"
      }, new CircularlyAccessibleValueObject[] {
        getModel().getHeaderVO(index)
      });
      getBillCtrl().displayBill(index);

      setButtonStatusBrowse(index);
      String[] value = new String[] {
        sOperationName
      };
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
          "UPP40093010-000126", null, value));// sOperationName
      // + "�ɹ�");
    }
    catch (Exception e) {
      Log.error(e);
      String[] value = new String[] {
          sOperationName, e.getMessage()
      };
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
          "UPP40093010-000127", null, value));// sOperationName
      // +
      // "����״̬ʱ����"
      // +
      // e.getMessage());
    }
  }

  /**
   * �������ڣ�(2004-2-10 17:09:40) ���ߣ����˾� ������ ���أ� ˵���������д���
   */
  protected void onAddLine() {
    getCardPanelCtrl().addLine();
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
        "UPP40093010-000064")/* @res "����һ�з�¼" */);
  }

  /**
   * �������ڣ�(2004-2-10 17:09:40) ���ߣ����˾� ������ ���أ� ˵���������д���
   */
  private void onInsertLine() throws Exception {
    getCardPanelCtrl().insertLine();
  }

  /**
   * ��� ����Ƭ�༭�� ��ť����Ӧ����
   */
  protected void onCardEdit() {

    showHintMessage(ConstHintMsg.S_ON_CARD_EDIT_START); // �����п�Ƭ�༭��ʼ
    getCardPanelCtrl().getCardPanel().getBillCardPanel().startRowCardEdit();
    showHintMessage(ConstHintMsg.S_ON_CARD_EDIT_END); // �����п�Ƭ�༭���
  }

  /**
   * ��� �������кš� ��ť����Ӧ����
   */
  protected void onNewRowNo() {

    nc.ui.scm.pub.report.BillRowNo.addNewRowNo(getCardPanelCtrl()
        .getCardPanel().getBillCardPanel(), toftpanel.getNodeInfo()
        .getBillTypeCode(), "crowno");
    showHintMessage(ConstHintMsg.S_ON_NEW_ROW_NO); // �����к����
  }

  /**
   * �������ڣ�(2004-2-10 17:09:40) ���ߣ����˾� ������ ���أ� ˵���������д���
   */
  protected void onPasteLine() {
    getCardPanelCtrl().pasteLine();   
  }

  /**
   * ��ť��ճ���е���β������Ӧ����
   */
  protected void onPasteLineToTail() {

    getCardPanelCtrl().pasteLineToTail();
    showHintMessage(ConstHintMsg.S_ON_PASTE_LINE_TO_TAIL); // ��ʾճ���е���β���
  }

  /**
   * �˴����뷽��˵���� ��������:�ڿ�Ƭģʽ��ת���һ�š� ���ߣ������� �������: ����ֵ: �쳣����: ����:(2003-5-27 14:47:24)
   */
  private void onFirst() throws BusinessException {
    doPageChanged(0);
    getButtonCtrl().m_PageBtnCtrl.first(getModel().getCount());
  }

  private void doPageChanged(int cur) {
    int iAll = getModel().getCount();
    if (iAll > 0) {
      getModel().setCurRow(cur);
      getBillCtrl().displayBill(cur);
      setButtonStatusBrowse(cur);
    }
  }

  /**
   * �˴����뷽��˵���� ��������:�ڿ�Ƭģʽ��ָ�����һ�� ���ߣ������� �������: ����ֵ: �쳣����: ����:(2003-5-27 14:48:54)
   */
  private void onLast() {
    int count = getModel().getCount();
    doPageChanged(count - 1);
    getButtonCtrl().m_PageBtnCtrl.last(count);
  }

  /**
   * �˴����뷽��˵���� ��������:�ڿ�Ƭģʽ��ָ����һ�� ���ߣ������� �������: ����ֵ: �쳣����: ����:(2003-5-27 14:48:31)
   */
  private void onNext() {
    int cur = getModel().getCurRow();
    doPageChanged(cur + 1);
    getButtonCtrl().m_PageBtnCtrl.next(getModel().getCount(), cur + 1);
  }

  /**
   * �˴����뷽��˵���� ��������:��ӡԤ�� �������: ����ֵ: �쳣����: ����:(2003-5-27 14:48:02)
   */
  protected void onPreview() {
    // ������ӡ����
    // ����ǰ���б��Ǳ�������ӡ����
    if (getCardPanelCtrl().getEditMode() == GenEditConst.BROWSE) {
      if (getCurPanel() == GenEditConst.CARD) {
        showHintMessage(PrintLogClient.getBeforePrintMsg(true, false));
        // ׼������
        BillVO voPrint = (BillVO) getModel().getCurVO();

        if (null == voPrint) {
          voPrint = new BillVO();
        }
        if (null == voPrint.getParentVO()) {
          voPrint.setParentVO(new BillHeaderVO());
        }
        if ((null == voPrint.getChildrenVO())
            || (voPrint.getChildrenVO().length == 0)
            || (voPrint.getChildrenVO()[0] == null)) {
          BillItemVO[] ivo = new BillItemVO[1];
          ivo[0] = new BillItemVO();
          voPrint.setChildrenVO(ivo);
        }

        ArrayList alPrintVO = new ArrayList(1);
        alPrintVO.add(voPrint);
        try {
          BillPrintTool printTool = new BillPrintTool(this.toftpanel,
              getNodeInfo().getNodeCode(), alPrintVO, getCardPanelCtrl()
                  .getCardPanel().getBillCardPanel().getBillData(), null,
              getNodeInfo().getCorpID(), getNodeInfo().getUserID(), "vcode",
              "cbillid");

          printTool.onCardPrintPreview(getCardPanelCtrl().getCardPanel()
              .getBillCardPanel(), getListPanelCtrl().getListPanel()
              .getBillListPanel(), getNodeInfo().getBillTypeCode());
          showHintMessage(printTool.getPrintMessage());
        }
        catch (Exception e) {
          GenMsgCtrl.printErr(e.getMessage());
        }

      }
      else { // �б�
        showHintMessage(PrintLogClient.getBeforePrintMsg(true, true));
        if (getModel().getCount() <= 0) {
          return;
        }

        ArrayList alPrintVO = getSelectedBills(true);
        if (alPrintVO == null)
          return;
        BillPrintTool printTool = null;
        try {
          printTool = new BillPrintTool(this.toftpanel, getNodeInfo()
              .getNodeCode(), alPrintVO, getCardPanelCtrl().getCardPanel()
              .getBillCardPanel().getBillData(), null, getNodeInfo()
              .getCorpID(), getNodeInfo().getUserID(), "vcode", "cbillid");


          printTool.onBatchPrintPreview(getListPanelCtrl().getListPanel()
              .getBillListPanel(), getNodeInfo().getBillTypeCode());
          showHintMessage(printTool.getPrintMessage());
        }
        catch (Exception e) {
          GenMsgCtrl.printErr(e.getMessage());
        }
      }
    }
  }

  /**
   * ����˵����ȡ��ѡ�еĵ��� �������ڣ�(2003-03-13 12:59:54) �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
   * 
   * @return java.util.ArrayList
   */
  protected ArrayList getSelectedBills(boolean bParse) {

    int[] iaSelListIndex = getBillCtrl().getCurSelectIndex();
    int iSelCount = iaSelListIndex.length;
    if (iSelCount <= 0) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
          "UPP40093010-000065")/* @res "����ѡ�е��ݣ�" */);
      return null;
    }

    BillVO[] voaBill = new BillVO[iSelCount];
    // ��ͷPK
    ArrayList<String> alHeadPK = new ArrayList<String>();
    ArrayList<Integer> alIndex = new ArrayList<Integer>();
    for (int i = 0; i < iSelCount; i++) {
      voaBill[i] = (BillVO) getModel().getData(iaSelListIndex[i]);
      // ���û�б��壬����Ҫ��ѯ
      if (voaBill[i].getItemVOs() == null) {
        alHeadPK.add(voaBill[i].getHeaderVO().getCbillid());
        alIndex.add(i);
      }
    }

    // ��ѯ��������
    try {
      if(alHeadPK.size()>0){
        BillVO[] voaQuery = nc.ui.to.pub.ClientBillHelper.queryBillByIDs(alHeadPK);
        for (int i = 0, len = alIndex.size(); i < len; i++) {
          //ֻ�������,��ʱ����ı���û�н�����ʽ
          voaBill[alIndex.get(i)].setChildrenVO(voaQuery[i].getItemVOs());
        }
      }
      ArrayList alRet = new ArrayList(Arrays.asList(voaBill));
      if (bParse) {
        // ��Ҫ�������еı�ͷ����
        getListPanelCtrl().setListHeadData(alRet);
        // ��Ҫ�������еı�������
        getListPanelCtrl().setlistBodyData(voaBill.length, alRet);
      }

      return alRet;
    }
    catch (Exception e) {
      nc.vo.scm.pub.ctrl.GenMsgCtrl.printErr(e.getMessage());
    }
    return null;
  }

  private void processDataBeforeUnApprove(List<BillVO> lstUnAudit)
      throws BusinessException {
    StringBuffer sbErr = new StringBuffer("");
    for (int i = 0, len = lstUnAudit.size(); i < len; i++) {
      BillHeaderVO voHead = lstUnAudit.get(i).getHeaderVO();
      int iBillStatus = voHead.getFstatusflag().intValue();

      // V55��������ģʽΪ��������ʱ��������״̬�ĵ���Ҳ��������
      // ����ģʽΪ��һ�����ס�ʱ��ֻ���������״̬�ĵ��ݲ�������
      // modifier: songhy
      if ((iBillStatus != ConstVO.IBillStatus_CHECKING)
          && (iBillStatus != ConstVO.IBillStatus_PASSCHECK)) {
        // ��¼�²���Ҫ����ĵ��ݺ�
        sbErr.append(i+1).append(
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009",
                "UPP40093009-000245")/* @res "��" */);
        continue;
      }
    }
    if (sbErr.toString().trim().length() > 0) {
      sbErr.delete(sbErr.length() - 1, sbErr.length());
      String[] value = new String[] {
        sbErr.toString()
      };
      String error = NCLangRes.getInstance().getStrByID("40093009",
          "UPP40093009-000272", null, value);
      throw new BusinessException(error);
    }
    BillVO[] vos = new BillVO[lstUnAudit.size()];
    vos = lstUnAudit.toArray(vos);
    // ��������˺��������Ƿ���ͬ
    ArrayList alMsg = TOBillTool.checkApprover(vos, getNodeInfo().getCorpID(),
        getNodeInfo().getUserID(), "cauditorid", "vcode");
    String sMsg = (String) alMsg.get(1);
    if (sMsg != null) {
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "topub", "UPPtopub-000126")/*
       * @res "�����˺������˱�����ͬ"
       */);
    }
    for (int i = 0, len = lstUnAudit.size(); i < len; i++) {
      BillHeaderVO voHead = lstUnAudit.get(i).getHeaderVO();
      BillItemVO[] voaItem = lstUnAudit.get(i).getItemVOs();
      UFBoolean bTemp = null;

      for (int j = 0; j < voaItem.length; j++) {
        // ����ѵ���/��������
        if ((voaItem[j].getNorderinnum() != null && voaItem[j].getNorderinnum()
            .doubleValue() != 0.0)
            || (voaItem[j].getNorderoutnum() != null && voaItem[j]
                .getNorderoutnum().doubleValue() != 0.0))
          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40093010", "UPP40093010-000091")/*
           * @res
           * "�ѵ���������������Ϊ��,���ܽ�������!"
           */);
        bTemp = voaItem[j].getBoutendflag();
        if (bTemp != null && bTemp.toString().equals("Y")) {
          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40093010", "UPP40093010-000092")/*
           * @res
           * "�����ѽ���,���ܽ�������!"
           */);
        }
        bTemp = voaItem[j].getBsettleendflag();
        if (bTemp != null && bTemp.toString().equals("Y")) {
          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40093010", "UPP40093010-000093")/*
           * @res
           * "���������,���ܽ�������!"
           */);
        }

        voaItem[j].setStatus(VOStatus.UPDATED);
      }

      // ���뵱ǰ����Ա
      lstUnAudit.get(i).setOperator(getNodeInfo().getUserID());
      // ����ͻ���IP��ַ,дҵ����־��
      try {
        lstUnAudit.get(i).setIPAdress(
            java.net.InetAddress.getLocalHost().getHostAddress());
      }
      catch (Exception ex) {
        ExceptionUITools.showMessage(ex, toftpanel);
      }
      // ��������:����������ǵ�ǰ����Ա�������ǵ��ݵ������ˡ������������������뵥���ϵ�����˱Ƚϣ��������ȣ��ͻᱨ����û������Ȩ�ޡ�
      voHead.setCauditorid(getNodeInfo().getUserID());

      // ���뵱ǰ����
      voHead.setDauditdate(new UFDate(getNodeInfo().getLogDate(), false));
      // �ѵ���״̬��Ϊ�޸�
      voHead.setStatus(VOStatus.UPDATED);

    }
  }

  private void showHintMessage(String hint) {
    toftpanel.showHintMessage(hint);
  }

  /**
   * �Ե�ǰ���ݽ��кϲ���ʾ�����ɴ�ӡ ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-9-25 13:51:28) �޸�����:2008-07-22
   * �޸���:������ �޸�ԭ��:�޸ĺϲ���ʾ���췽�� �� v5.5 ���󣬺ϲ���ӡ���յ���ģ���ӡ ע�ͱ�־:
   */
  private void onBillCombin() {
    // CollectSettingDlg dlg = new
    // CollectSettingDlg(toftpanel,nc.ui.ml.NCLangRes.getInstance().getStrByID("topub","UPT4009-000007")/*@res
    // "�ϲ���ʾ"*/);
    CollectSettingDlg dlg = new CollectSettingDlg(this.toftpanel, getNodeInfo()
        .getBillTypeCode(), getNodeInfo().getNodeCode(), getNodeInfo()
        .getCorpID(), getNodeInfo().getUserID(), BillVO.class.getName(),
        BillHeaderVO.class.getName(), BillItemVO.class.getName());
    // dlg.setBilltype(toftpanel.getNodeInfo().getBillTypeCode());
    BillCardPanel bcp = getCardPanelCtrl().getCardPanel().getBillCardPanel();
    dlg.initData(bcp, new String[] {
        "cinvcode", "cinvname", "cinvspec", "cinvtype"
    }, // �̶�������
        null, // new String[]{"dplanarrvdate"},ȱʡ������
        new String[] {
            "nnum", "nmny", "nnotaxmny"
        }, // �����
        null,// ��ƽ����
        new String[] {
            "nprice", "nnotaxprice"
        },// ��Ȩƽ��
        "nnum",// ����
        new String[] {
          "base"
        });
    dlg.showModal();
  }

  /**
   * �˴����뷽��˵���� ��������:���ݱ����еĵ���ID�͵����������������ε��ݡ� ���ߣ������� �������: ����ֵ: �쳣����:
   * ����:(2003-4-22 16:09:14)
   */
  private void onJointCheck() {
    String sBillPK = null;
    BillHeaderVO voHead = (BillHeaderVO) getModel().getHeaderVO(
        getModel().getCurRow());
    if (voHead == null) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
          "UPP40093010-000067")/* @res "û��ѡ�񵥾ݣ�" */);
      return;
    }
    String sBillTypeCode = voHead.getCbilltype();
    String vBillCode = voHead.getVcode();
    sBillPK = (String) (getModel().getHeaderVO(getModel().getCurRow())
        .getAttributeValue("cbillid"));
    if (sBillPK == null || sBillTypeCode == null) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
          "UPP40093010-000068")/* @res "����û�ж�Ӧ���ݣ�" */);
      return;
    }
    nc.ui.scm.sourcebill.SourceBillFlowDlg soureDlg = new nc.ui.scm.sourcebill.SourceBillFlowDlg(
        toftpanel, sBillTypeCode, sBillPK, null, getNodeInfo().getUserID(),
        vBillCode);

    soureDlg.showModal();
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2004-2-26 11:06:14)
   */
  private void onLocate() {
    getBillCtrl().onLocate();
  }

  /**
   * �������ڣ�(2004-2-10 17:09:40) ���ߣ����˾� ������ ���أ� ˵������������
   */
  private void onNew(String cbusitype) throws Exception {
    try {
      if (getCurPanel() == GenEditConst.LIST)
        onSwitch();
    
      getCardPanelCtrl().newBlankBill(cbusitype);

      getCardPanelCtrl().setDefaultCorpID();
      toftpanel.setButtonStatusEdit();
      
      rightButtonRightControl();
      getCardPanelCtrl().getCardPanel().getBillCardPanel().transferFocusTo(
          IBillItem.HEAD);
    }
    catch (BusinessException be) {
      // ȡ������
      onCancel();
      throw be;
    }
  }

 private void rightButtonRightControl() {
//    String[] saTableCodes = new String[] { "base", "relation", "exec2",
//        "free", "produce" };
//    for (int j = 0; j < saTableCodes.length; j++) {
//    UIMenuItem[] bodyMenuItems = getBillCardPanel().getBodyMenuItems(saTableCodes[j]);
//
//    for (int i = 0; i < bodyMenuItems.length; i++) {
//
//      String sButtonName = bodyMenuItems[i].getText().toString();
//      if (ButtonTree.BUTTON_NULL != toftpanel.m_boBtnTree.getButton(sButtonName))
//        if(!toftpanel.m_boBtnTree.getButton(sButtonName).getParent().isPower()
//            || (toftpanel.m_boBtnTree.getButton(sButtonName).getParent().isPower()
//                && !(toftpanel.m_boBtnTree.getButton(sButtonName).isPower()))){
//          bodyMenuItems[i].setEnabled(false);
//      }
//    }
//
//    }
  }

  private BillCardPanel getBillCardPanel(){
    return toftpanel.m_CardCtrl.getCardPanel().getBillCardPanel();
  }

  /**
   * ��������:������ѯ ����: ����ֵ: �쳣:
   */
  protected void onOnHandShowHidden() {
    // ������ѯ����ֻ�ڿ�Ƭ������ʾ
    if (getCurPanel() == GenEditConst.LIST)
      onSwitch();
    getCardPanelCtrl().setOnhandShowHiddenChanged();
    toftpanel.updateUI();
  }

  /**
   * �˴����뷽��˵���� ��������:�ڿ�Ƭģʽ��ָ��ǰһ�� ���ߣ������� �������: ����ֵ: �쳣����: ����:(2003-5-27 14:48:02)
   */
  protected void onPrevious() {
    int iCur = getModel().getCurRow();
    doPageChanged(iCur - 1);
    getButtonCtrl().m_PageBtnCtrl.previous(getModel().getCount(), iCur);
  }

  /**
   * ��������:��ӡ ����: ����ֵ: �쳣:
   */
  protected void onPrint() {
    // ������ӡ����
    // ����ǰ���б��Ǳ�������ӡ����
	  
//	  // ��ӡ֮ǰִ��ˢ��  modify by zhw  �׸���Ŀ  2011-04-14
	 int currrow = getModel().getCurRow();
	 int cardpanel = getCurPanel();
	 if(currrow>=0){
		 onRefresh();
		 getModel().setCurRow(currrow);
		
		 if (getCurPanel() == GenEditConst.CARD) {
			 getBillCardPanel().setBillValueVO(getModel().getCurVO());
			 getBillCardPanel().execHeadTailLoadFormulas();
			 getBillCardPanel().getBillModel().execLoadFormula();
		 }else{
			 getListPanelCtrl().getListPanel().getBillListPanel().getHeadTable().setRowSelectionInterval(currrow, currrow);
//			 getbill
		 }
		 
		 if(cardpanel!=getCurPanel())
			 onSwitch();
	 }
		
	 
//	  zhw end
    if (getCardPanelCtrl().getEditMode() == GenEditConst.BROWSE) {
      if (getCurPanel() == GenEditConst.CARD) {
        showHintMessage(PrintLogClient.getBeforePrintMsg(false, false));
        // ׼������
        BillVO voPrint = (BillVO) getModel().getCurVO();
       
        
        if (null == voPrint) {
          return;
        }

        ArrayList alPrintVO = new ArrayList(1);
        alPrintVO.add(voPrint);
        try {
          BillPrintTool printTool = new BillPrintTool(this.toftpanel,
              getNodeInfo().getNodeCode(), alPrintVO, getCardPanelCtrl()
                  .getCardPanel().getBillCardPanel().getBillData(), null,
              getNodeInfo().getCorpID(), getNodeInfo().getUserID(), "vcode",
              "cbillid");

          printTool.onCardPrint(getCardPanelCtrl().getCardPanel()
              .getBillCardPanel(), getListPanelCtrl().getListPanel()
              .getBillListPanel(), getNodeInfo().getBillTypeCode());
          showHintMessage(printTool.getPrintMessage());
        }
        catch (Exception e) {
          GenMsgCtrl.printErr(e.getMessage());
        }

      }
      else { // �б�
        showHintMessage(PrintLogClient.getBeforePrintMsg(false, true));
        if (getModel().getCount() <= 0) {
          return;
        }

        ArrayList alPrintVO = getSelectedBills(true);
        if (alPrintVO == null)
          return;

        try {
          BillPrintTool printTool = new BillPrintTool(this.toftpanel,
              getNodeInfo().getNodeCode(), alPrintVO, getCardPanelCtrl()
                  .getCardPanel().getBillCardPanel().getBillData(), null,
              getNodeInfo().getCorpID(), getNodeInfo().getUserID(), "vcode",
              "cbillid");

          printTool.onBatchPrint(getListPanelCtrl().getListPanel()
              .getBillListPanel(), getNodeInfo().getBillTypeCode());
          showHintMessage(printTool.getPrintMessage());
        }
        catch (Exception e) {
          Debug.debug(e.getMessage(), e);
        }
      }
    }
  }

  /**
   * �������ڣ�(2004-2-10 18:38:37) ���ߣ����˾� ������ ���أ� ˵������ѯ
   */
  protected void onQuery() {
    toftpanel.query();
  }

  /**
   * ****************************************** ���ܣ�����״̬��ѯ(����������ƽ̨�Ĳ�ѯ�Ի���)
   * �������ڣ�(2004-3-11 14:19:33)
   * 
   * @param��
   * @return�� ******************************************
   */
  private void onQueryAudit() throws Exception {

    if (getModel().getCurRow() < 0) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
          "UPP40093010-000088")/* @res "��ѡ��һ�ŵ���!" */);
      return;
    }
    BillVO vo = (BillVO) getModel().getCurVO();
    nc.ui.pub.workflownote.FlowStateDlg dlgAuditStatus = new nc.ui.pub.workflownote.FlowStateDlg(
        toftpanel, vo.getBillTypeCode(), vo.getParentVO().getPrimaryKey());
    dlgAuditStatus.showModal();

  }

  /**
   * �㡰�˻ء���ťʱ���� �������ڣ�(2005-11-22 9:28:41)
   */
  protected void onReturn() throws Exception {
    try {
      startEdit();
      //�ѵ�ǰѡ��ҵ���������õ����ƺ�ĵ�����
      String cbiztypeid = getButtonCtrl().m_boBusiType.getSelectedChildButton()[0]
          .getTag();
      getCardPanelCtrl().returnBill(getModel().getCurRow(), cbiztypeid);
      toftpanel.setButtonStatusEdit();
    }
    catch (Exception be) {
      // ȡ���˻�
      onCancel();
      throw be;

    }
  }

  boolean b = false;

  /**
   * �������ڣ�(2004-2-10 18:38:37) ���ߣ����˾� ������ ���أ�
   * ˵�������棬����Ϊ����������޸ı��档Ҳ����ͳһΪEDIT״̬��ͨ����������־�������������޸ġ�
   */
  protected void onSave() throws Exception {
    saveBill(ConstVO.sActionCOMMIT);
  }

  /**
   * ��������:��������󡱰�ťʱ���� ����: ����ֵ: �쳣:
   */
  protected void onSendAudit() throws Exception {

    // �༭״̬��,���ñ��沢����Ķ���
    if (getCardPanelCtrl().getEditMode() == GenEditConst.NEW
        || getCardPanelCtrl().getEditMode() == GenEditConst.UPDATE) {
      saveBill(ConstVO.sActionSENDAFTERSAVE);
    }
    // ���״̬��,ֻ���õ�����������
    else {
      BillVO voBill = (BillVO) getModel().getCurVO();
      if (voBill == null || voBill.getParentVO() == null) {
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "SCMCOMMON", "UPPSCMCommon-000199")/* @res "��ѡ�񵥾�" */);
        return;
      }
      if (!getNodeInfo().getUserID().equals(
          voBill.getHeaderVO().getCoperatorid())) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("scmcommon",
            "UPPSCMCommon-000080")/* @res "�����Ǵ˵����Ƶ��ˣ���������" */);
        return;
      }

      voBill = toftpanel.sendAudit(voBill);

      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
          "UPP40093010-000074")/* @res "����ɹ�" */);
      int iNewStatus = voBill.getHeaderVO().getFstatusflag().intValue();
      if (iNewStatus == ConstVO.IBillStatus_CHECKING) {
        getModel().setData(getModel().getCurRow(), voBill);
        // ����һ�±�ͷ
        getListPanelCtrl().setListHeadData(
            new CircularlyAccessibleValueObject[] {
              voBill.getHeaderVO()
            });
      }

      getBillCtrl().displayBill(getModel().getCurRow());
      setButtonStatusBrowse(getModel().getCurRow());
    }

  }

  private void showErrorMessage(String smsg) {
    toftpanel.showErrorMessage(smsg);
  }

  private void showUnknownErrorMessage(Exception e) {
    MessageDialog.showUnknownErrorDlg(toftpanel, e);
  }

  /**
   * �˴����뷽��˵���� ��������:�ڿ�Ƭģʽ��ת���һ�š� ���ߣ������� �������: ����ֵ: �쳣����: ����:(2003-5-27 14:47:24)
   */
  protected void onStockLock() throws Exception {
    try {
      // ȡ��Ҫ��˵ĵ���
      // ���Ӳ����ֻ������һ�Ρ���������
      BillVO voBill = (BillVO) getModel().getCurVO();
      voBill.setOperator(getNodeInfo().getUserID());
      nc.ui.pub.pf.PfUtilClient.processActionNoSendMessage(toftpanel,
          "STOCKLOCK", voBill.getBillTypeCode(), getNodeInfo().getLogDate(),
          voBill, null, null, null);

      //ˢ�±���ʱ���
      StringBuffer sql = new StringBuffer(" ");
      sql.append(" cbillid = '");
      sql.append(voBill.getHeaderVO().getPrimaryKey());
      sql.append("' and dr=0 ");
      BillItemVO[] itemVOs = nc.ui.to.pub.ClientBillHelper.queryBodyBySQL(
          new String[] {
              "cbill_bid", "ts"
          }, sql.toString());
      HashMap<String, UFDateTime> mapTS = new HashMap<String, UFDateTime>();
      for (BillItemVO item : itemVOs) {
        mapTS.put(item.getCbill_bid(), item.getTs());
      }

      BillItemVO[] oldItemVOs = voBill.getItemVOs();
      for (BillItemVO item : oldItemVOs) {
        item.setTs(mapTS.get(item.getCbill_bid()));
      }
      getModel().setData(getModel().getCurRow(), voBill);
      getBillCtrl().displayBill(getModel().getCurRow());
      // showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
      // "40093010", "UPP40093010-000082")/* @res "�����Ѿ��ɹ�������" */);
      //
    }
    catch (Exception e) {
      throw e;
    }

  }

  /**
   * �������ڣ�(2004-2-10 17:09:40) ���ߣ����˾� ������ ���أ� ˵����Panel�л�����
   */
  protected void onSwitch() {
    toftpanel.switchPanel();
  }

  /**
   * ****************************************** ���ܣ���˵��� �������ڣ�(2004-3-10
   * 13:10:37)
   * 
   * @param��
   * @return�� ******************************************
   */
  protected void onUnApprove() {

    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
        "UPP40093010-000089")/* @res "��������" */);
    // ȡ��Ҫ����ĵ��ݣ�����Ϊfalse����������ͷ���幫ʽ����С���������Կ����л���ͷʱ�������幫ʽ
    ArrayList lstUnAudit = getSelectedBills(false);
    List<BillVO> lstRet = null;
    try {
      processDataBeforeUnApprove(lstUnAudit);
      lstRet = toftpanel.unApproveBill(lstUnAudit);
    }
    catch (BusinessException ex) {
      ExceptionUITools.showMessage(ex, toftpanel);
    }

    // ˢ�������иı����Ϣ
    if (lstRet != null && lstRet.size() > 0 && lstUnAudit != null) {
      processDataAfterApprove(lstUnAudit, lstRet);

      int[] iaSelListIndex = getBillCtrl().getCurSelectIndex();
      for (int i = 0; i < iaSelListIndex.length; i++) {
        getModel().setData(iaSelListIndex[i], (BillVO) lstUnAudit.get(i));
      }
      getBillCtrl().displayBill(getModel().getCurRow());
      setButtonStatusBrowse(getModel().getCurRow());

      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
          "UPP40093010-000096")/* @res "����ɹ�" */);
    }
    else {
      showHintMessage("");
    }
  }

  /**
   * �������ڣ�(2004-2-10 17:09:40) ���ߣ����˾� ������ ���أ� ˵�����޸Ķ�������
   */
  protected void onUpdate() throws Exception {
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
        "UPPSCMCommon-000315")/* @res "���ڼ�����Ҫ�����ݣ����Ժ�..." */);
   
    if (!toftpanel.getIsComeFromOther()) {
      // 1��BillVO[]:���ͨ����VO���飻2��ͨ����VO������3��String:��ͨ����VO������Ϣ
      checkBeforeUpdate(Const.ACTION_UPDATE, null);
    
      startEdit();
      // ---
      getCardPanelCtrl().startUpdate();
      // ʹ������Ч,���룡
      toftpanel.setButtonStatusEdit();
      rightButtonRightControl();
      getCardPanelCtrl().getCardPanel().getBillCardPanel().transferFocusTo(
          IBillItem.HEAD);
    }
    else {
    
      toftpanel.getDefaultCalbodyID();
      // ��ǰ���б���ʽʱ�������л�������ʽ
      if (getCurPanel() == GenEditConst.LIST) {
        // ����ѡ���ĵ��ݵ�������
        toftpanel.onSwitchForRef();
      }
      getCardPanelCtrl().startEdit();
      rightButtonRightControl();

      // ��Ƭͨ������������������
      getCardPanelCtrl().newBillByRef();
      // ʹ������Ч,���룡
      toftpanel.setButtonStatusEdit();
      getCardPanelCtrl().getCardPanel().getBillCardPanel().transferFocusTo(
          IBillItem.HEAD);
    }

    setModifiedRowsColor();

  //zhf add  �޸�ʱ ����������ݴ��� �����ʽ���Ϣ Ϊ�丳ֵ   
    setFundValue();

    
    
    //zhf add  �޸�ʱ ����������ݴ��� �����ʽ���Ϣ Ϊ�丳ֵ   
    StockNumParaHelper.setFundValue(getBillCardPanel(), (BillVO)getModel().getCurVO());

    //zhf end
    
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
        "UPPSCMCommon-000133")/* @res "����" */);

  }

  /**
   * ��������:ATP��ѯ ����: ����ֵ: �쳣:
   */
  private void onAllATP() {
    getBillCtrl().onAllATP();
  }

  /**
   * ��������:�ִ�����ѯ ����: ����ֵ: �쳣:
   */
  private void onAllSP() {
    // ��ѯ�ִ��������+��˾+�����֯+�ֿ�
    getBillCtrl().onAllSP();
  }

  /**
   * ****************************************** ���ܣ���˵��� �������ڣ�(2004-3-10
   * 13:10:37)
   * 
   * @param��
   * @return�� ******************************************
   */
  private void onApprove() {
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093010",
        "UPP40093010-000081")/* @res "��������" */);

    // ȡ��Ҫ��˵ĵ��ݣ�����Ϊfalse����������ͷ���幫ʽ����С���������Կ����л���ͷʱ�������幫ʽ
    ArrayList<BillVO> lstAudit = getSelectedBills(false);
    List<BillVO> lstRet = null;
    try {
      for (int i = 0, len = lstAudit.size(); i < len; i++) {
        BillHeaderVO voHead = lstAudit.get(i).getHeaderVO();
        String coperatorId = voHead.getCoperatorid(); // �Ƶ���
        String currUserId = toftpanel.m_NodeInfo.getUserID(); // ��ǰ��½�ˣ��������
        String corpId = toftpanel.m_NodeInfo.getCorpID(); // ��ǰ��½��˾  
        
        String isAllowSame = toftpanel.m_NodeInfo.getM_paravalue().get(ConstVO.m_sPk_Para[ConstVO.m_iPara_ZDSPSFXT]).toString();
        if (isAllowSame.equals("N")) { // �������Ƶ��˺���������ͬһ��
          if (coperatorId.equals(currUserId)) { // ����ʱ�Ƶ��˺�������ȴ��ͬ������ false
            //�������Ƶ��˺���������ͬһ�ˣ����ʧ�ܣ�
            showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("topub",
                "UCH4009-000011"));
            return;
          }
        }
      }

      processDataBeforeApprove(lstAudit);
      lstRet = toftpanel.approveBill(lstAudit);
    }
    catch (BusinessException be) {
      ExceptionUITools.showMessage(be, toftpanel);
    }

    // ˢ�������иı����Ϣ
    if (lstRet != null && lstAudit != null) {
      processDataAfterApprove(lstAudit, lstRet);

      int[] iaSelListIndex = getBillCtrl().getCurSelectIndex();
      for (int i = 0; i < iaSelListIndex.length; i++) {
        getModel().setData(iaSelListIndex[i], lstAudit.get(i));
      }
      getBillCtrl().displayBill(getModel().getCurRow());
      setButtonStatusBrowse(getModel().getCurRow());

      String hint = ConstHintMsg.S_ON_SUCCESS_AUDIT;
      if (lstRet != null && lstRet.size() > 0) {
        Integer fStatusFlag = lstAudit.get(0).getHeaderVO().getFstatusflag();
        if (fStatusFlag.equals(ConstVO.IBillStatus_PASSCHECK))
          hint += nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009",
              "UPP40093009-000246")/* @res " ���Ϊ:ͨ��" */;
        else if (fStatusFlag.equals(ConstVO.IBillStatus_CHECKING))
          hint += nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009",
              "UPP40093009-000247")/* @res " ���Ϊ:����������" */;
        else if (fStatusFlag.equals(ConstVO.IBillStatus_UNCHECKED))
          hint += nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009",
              "UPP40093009-000248")/* @res " ���Ϊ:��ͨ��" */;
        else if (fStatusFlag.equals(ConstVO.IBillStatus_FREE))
          hint += nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009",
              "UPP40093009-000249")/* @res " ���Ϊ:����" */;
        else
          hint += nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009",
              "UPP40093009-000250")/* @res " ���Ϊ:δ֪" */;

        showHintMessage(hint);
      }
      else {
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009",
            "UPP40093009-000251")/* @res "����ʧ��" */);
      }
    }
    else {
      showHintMessage("");
    }
  }

  private void processDataBeforeApprove(List<BillVO> lstAudit)
      throws BusinessException {
    StringBuffer sbErr = new StringBuffer("");
    for (int i = 0, len = lstAudit.size(); i < len; i++) {
      BillVO voAudit = lstAudit.get(i);
      BillHeaderVO voHead = voAudit.getHeaderVO();
      BillItemVO[] voaItem = voAudit.getItemVOs();
      int iBillStatus = voHead.getFstatusflag();

      if (iBillStatus == ConstVO.IBillStatus_FREE
          || iBillStatus == ConstVO.IBillStatus_CHECKING) // ������
      {
        // ����ǰ�ļ��
        BusinessCheck.checkForSendAudit(voAudit);
        // ���뵱ǰ����Ա
        voAudit.setOperator(getNodeInfo().getUserID());
        voHead.setCauditorid(getNodeInfo().getUserID());
        // ����ͻ���IP��ַ,дҵ����־��
        try {
          voAudit.setIPAdress(java.net.InetAddress.getLocalHost()
              .getHostAddress());
        }
        catch (Exception ex) {
          ExceptionUITools.showMessage(ex, toftpanel);
        }
        // ���뵱ǰ����
        voHead.setDauditdate(new UFDate(getNodeInfo().getLogDate(), false));

        if (voHead.getDbilldate().after(voHead.getDauditdate())) {
          throw new BusinessException(NCLangRes.getInstance().getStrByID(
              "40093010", "UPP40093010-000005")/*
           * @res "�Ƶ����ڲ��ܴ�����������"
           */);
        }
        // �ѵ���״̬��Ϊ�޸�
        voHead.setStatus(VOStatus.UPDATED);
        for (int j = 0; j < voaItem.length; j++) {
          voaItem[j].setStatus(VOStatus.UPDATED);
        }
        // ���ø���˾�ı�λ��
        BusinessCheck.setCurrencyType(voAudit);
      }
      else {
        // ��¼�²���Ҫ�����ĵ��ݺ�
        sbErr.append(voHead.getVcode()).append(
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40093009",
                "UPP40093009-000245")/* @res "��" */);
      }
    }
    if (sbErr.toString().trim().length() > 0) {
      sbErr.delete(sbErr.length() - 1, sbErr.length());
      String[] value = new String[] {
        sbErr.toString()
      };
      String error = NCLangRes.getInstance().getStrByID("40093009",
          "UPP40093009-000209", null, value);
      throw new BusinessException(error);
    }
  }

  /**
   * �������ڣ�(2004-2-10 18:38:37) ���ߣ����˾� ������ ���أ� ˵����ȡ�� ����
   */
  private void onCancel() {

    clearOID();
    
    if (toftpanel.getIsComeFromOther()) {
      getModel().removeData(new int[] {
        getModel().getCurRow()
      });
      if (getModel().getCount() == 0) {
        onCancelRef();
      }
      else {
        //�������ı�����յ����ε���id
        clearRefLineFromHashWhenCancel();
        
        toftpanel.setReftoListPanelForCancel();
      }
    }
    else {
      //�������ı�����յ����ε���id
      clearRefLineFromHashWhenCancel();
      
      getCardPanelCtrl().cancelUpdate();
      getBillCtrl().displayBill(getModel().getCurRow());
      setButtonStatusBrowse(getModel().getCurRow());
      showHintMessage(ConstHintMsg.S_ON_SUCCESS_CANCEL);
    }
  }

  /**
   * ȡ���༭ʱ�������յ����ε���id�ӻ�����������Ա��������ʱ�����ٴβ�ѯ��
   * <b>����˵��</b>
   * @author sunwei
   * @time 2009-1-9 ����02:59:19
   */
  private void clearRefLineFromHashWhenCancel() {
    int count = getCardPanelCtrl().getCardPanel().getBillCardPanel().getBillTable().getRowCount();
    for (int i = 0; i < count; i++) {
      Object csourcebid = null;
      Object rowId = null;
      rowId = getCardPanelCtrl().getCardPanel().getBillCardPanel().getBillModel().getValueAt(i, "cbill_bid");
      //idΪ�յ�Ϊ�����У������ѱ�����ı����д���csourcebid����������
      if(rowId==null||rowId.toString().length()==0){
        csourcebid = getCardPanelCtrl().getCardPanel().getBillCardPanel().getBillModel().getValueAt(i, "csourcebid");
      }
      
      toftpanel.getRefBillIds().remove(csourcebid);
    }
  }

  private BillVO getSaveBill(String sActionName) throws Exception {
    BillVO voNew = (BillVO) getCardPanelCtrl().getVO();
    if (voNew == null || voNew.getParentVO() == null
        || voNew.getChildrenVO() == null) {
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "40093010", "UPP40093010-000073")/*
       * @res "����Ϊ�գ�"
       */);
    }
    // ����ͻ���IP��ַ,дҵ����־��
    voNew.setIPAdress(java.net.InetAddress.getLocalHost().getHostAddress());
    // ���뵱ǰ����Ա
    if (sActionName.equals(ConstVO.sActionSENDAFTERSAVE)) {
      // ����������id(��������ʹ��:�����ò�������������ʱ,�����������������ֱ�ӵ��������ű�)
      ((BillVO) voNew).getHeaderVO().setCauditorid(getNodeInfo().getUserID());

    }
    voNew.setOperator(getNodeInfo().getUserID());

    BillHeaderVO voHeader = voNew.getHeaderVO();

    voHeader.setBatpcheck(new UFBoolean(true));
    voHeader.setClastmodifierid(getNodeInfo().getUserID());
    voHeader.setClastmodiname(getNodeInfo().getUserName());
    voHeader.setTlastmodifytime(ClientEnvironment.getServerTime());
    if (voNew.getHeaderVO().getCbillid() == null
        || voNew.getHeaderVO().getCbillid().toString().trim().length() == 0) {
      // �Ƶ�ʱ��
      voNew.getHeaderVO().setTmaketime(ClientEnvironment.getServerTime());
    }

    // �к��ظ����
    if (!nc.ui.scm.pub.report.BillRowNo.verifyRowNosCorrect(getCardPanelCtrl()
        .getCardPanel().getBillCardPanel(), "crowno")) {
      return null;
    }
    BusinessCheck.inputChk(voNew);
    BusinessCheck.checkCardPanel(getCardPanelCtrl().getCardPanel()
        .getBillCardPanel());
    if (sActionName.equals(ConstVO.sActionSENDAFTERSAVE)) {
      // �������ĵ���
      BusinessCheck.checkForSendAudit(voNew);
    }
    return voNew;
  }

  private NodeInfo getNodeInfo() {
    return toftpanel.getNodeInfo();
  }

  /*
   * �õ�����ʱ�����ű���Ҫ�Ĳ���
   */
  private ArrayList getSaveParam(BillVO voBill) {
    BillHeaderVO voHeader = voBill.getHeaderVO();
    BillItemVO[] voaItem = voBill.getItemVOs();

    // 1.����״̬�£���Ϊ�޸�ǰ�ĵ���Ϊnull
    BillVO voOriginalBill = (BillVO) getModel().getCurVO();
    // 2.�޸�״̬�£�ȡ�޸�ǰ�ĵ���
    // �ж��ǵ��������˻ػ����ֹ������˻ص�������
    String sSourceid = voaItem[0].getCsourceid();
    boolean bRetBillFlag = false;
    if (voOriginalBill != null)
      bRetBillFlag = voaItem[0].getBretractflag() != null
          && voaItem[0].getBretractflag().booleanValue() && sSourceid != null
          && sSourceid.equals(voOriginalBill.getItemVOs()[0].getCbillid());

    ArrayList alParam = new ArrayList();
    if (getCardPanelCtrl().getEditMode() == GenEditConst.NEW) {
      // ����˻ص���Դ���������Ƿ��޸�ʱʹ��
      alParam.add(bRetBillFlag ? new BillVO[] {
        voOriginalBill
      } : null);
      alParam.add(toftpanel.m_clientLink);
    }
    else if (getCardPanelCtrl().getEditMode() == GenEditConst.UPDATE) {
      alParam.add(new BillVO[] {
        (BillVO) voOriginalBill
      });
      alParam.add(toftpanel.m_clientLink);
    }
    return alParam;
  }

  public ButtonEventHandler(
      TransferClientUI toftpanel) {
    super();
    this.toftpanel = toftpanel;
  }

  private void setModifiedRowsColor() {
    BillCardPanel billCardPanel = getCardPanelCtrl().getCardPanel()
        .getBillCardPanel();
    BillModel billModel = billCardPanel.getBillModel();
    ArrayList<Integer> selectedRows = new ArrayList<Integer>();
    for (int i = 0, len = billModel.getRowCount(); i < len; i++) {
      UFDouble nprice = (UFDouble) billModel.getValueAt(i, "nprice");
      UFDouble naskprice = (UFDouble) billModel.getValueAt(i, "naskprice");
      if ((nprice != null) && (naskprice != null)
          && (!nprice.equals(naskprice))) {
        selectedRows.add(i);

      }
    }
    SetColor.setRowColor(billCardPanel.getBodyPanel(), selectedRows,
        Color.YELLOW);
  }
  
  /**
   * �����������������ӵ��������ֵ���ӡԤ�����ܡ�
   * <b>����˵��</b>
   * @author liyu
   * @time 2009-2-19 ����03:46:53
   */
  private void onSplitPreview(){
    if (getCardPanelCtrl().getEditMode() == GenEditConst.BROWSE) {
      // ������װ�ֵ�����vo,�������ֿ�����������зֵ�
      SplitParams[] paramvos = new SplitParams[1];
      paramvos[0] = new SplitParams("ctakeoutwhid",
          nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("common", "UC000-0000525")/* @res "�����ֿ�" */,
          SplitParams.NoInput, null, true);// �����ֿ�
      SplitPrintParamDlg splitDlg = new SplitPrintParamDlg(this.toftpanel, paramvos);
      splitDlg.showModal();
      // ��ȡ�µĲ���vo
      SplitParams[] paramvonew = null;
      if (splitDlg.isCloseByBtnOK()) {// ��ȡȷ����ť
        paramvonew = splitDlg.getSplitParams();
      }
      else{
        return;
      }
      if (getCurPanel() == GenEditConst.LIST) {
        // �б�
        showHintMessage(PrintLogClient.getBeforePrintMsg(true, true));
        if (getModel().getCount() <= 0) {
          return;
        }

        ArrayList alPrintVO = getSelectedBills(true);
        if (alPrintVO == null)
          return;
        BillPrintTool printTool = null;
        try {
          printTool = new BillPrintTool(this.toftpanel, getNodeInfo()
              .getNodeCode(), alPrintVO, getCardPanelCtrl().getCardPanel()
              .getBillCardPanel().getBillData(), null, getNodeInfo()
              .getCorpID(), getNodeInfo().getUserID(), "vcode", "cbillid");

          printTool.onBatchSplitPrintPreview(getListPanelCtrl().getListPanel()
              .getBillListPanel(), getNodeInfo().getBillTypeCode(),paramvonew);
          showHintMessage(printTool.getPrintMessage());
        }
        catch (Exception e) {
          GenMsgCtrl.printErr(e.getMessage());
        }
      
        
      }
      else if(getCurPanel() == GenEditConst.CARD){ 
        showHintMessage(PrintLogClient.getBeforePrintMsg(true, false));
      // ׼������
      BillVO voPrint = (BillVO) getModel().getCurVO();

      if (null == voPrint) {
        voPrint = new BillVO();
      }
      if (null == voPrint.getParentVO()) {
        voPrint.setParentVO(new BillHeaderVO());
      }
      if ((null == voPrint.getChildrenVO())
          || (voPrint.getChildrenVO().length == 0)
          || (voPrint.getChildrenVO()[0] == null)) {
        BillItemVO[] ivo = new BillItemVO[1];
        ivo[0] = new BillItemVO();
        voPrint.setChildrenVO(ivo);
      }

      ArrayList alPrintVO = new ArrayList(1);
      alPrintVO.add(voPrint);
      try {
        BillPrintTool printTool = new BillPrintTool(this.toftpanel,
            getNodeInfo().getNodeCode(), alPrintVO, getCardPanelCtrl()
                .getCardPanel().getBillCardPanel().getBillData(), null,
            getNodeInfo().getCorpID(), getNodeInfo().getUserID(), "vcode",
            "cbillid");

        printTool.onSplitCardPrintPreview(getCardPanelCtrl().getCardPanel()
            .getBillCardPanel(), getListPanelCtrl().getListPanel()
            .getBillListPanel(), getNodeInfo().getBillTypeCode(),paramvonew);
        showHintMessage(printTool.getPrintMessage());
      }
      catch (Exception e) {
        GenMsgCtrl.printErr(e.getMessage());
      }}
      
      
    }
  }

  /**
   * 
   * @author zhf
   * @˵�������׸ڿ�ҵ��zhf add
   * 2011-4-25����04:24:43
   */
  private void setFundValue(){
	  BillVO bill = (BillVO)getModel().getCurVO();
	  getBillCardPanel().getHeadItem("nfund").setValue(bill.getNfund());
	  getBillCardPanel().getHeadItem("nallfund").setValue(bill.getNallfund());
	  getBillCardPanel().getHeadItem("nmny").setValue(bill.getNmny());
	  getBillCardPanel().getHeadItem("nallmny").setValue(bill.getNallmny());
  }

}
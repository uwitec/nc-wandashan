package nc.ui.ps.estimate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.ListSelectionModel;

import nc.itf.scm.cenpur.service.ChgDataUtil;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.ic.service.IICPub_LotNumbRefPane;
import nc.ui.ml.NCLangRes;
import nc.ui.po.pub.PoQueryCondition;
import nc.ui.ps.estimate.hg.InitialPartEditField;
import nc.ui.ps.pub.SpeBiztypeDef;
import nc.ui.ps.pub.WarehouseRef;
import nc.ui.pu.pub.POPubSetUI2;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.bill.DefaultCurrTypeBizDecimalListener;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.print.IDataSource;
import nc.ui.scm.ic.freeitem.DefHelper;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.print.ISetBillVO;
import nc.ui.scm.pub.print.ScmPrintTool;
import nc.ui.scm.pub.report.BillRowNo;
import nc.vo.ps.estimate.EstimateVO;
import nc.vo.ps.estimate.GeneralBb3VO;
import nc.vo.ps.estimate.GeneralHHeaderVO;
import nc.vo.ps.estimate.GeneralHItemVO;
import nc.vo.ps.estimate.GeneralHVO;
import nc.vo.ps.settle.OorderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.para.SysInitVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.scm.cenpur.service.ChgDocPkVO;
import nc.vo.scm.cenpur.service.ChgPriceMnyVO;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.excp.RwtIcToPoException;
import nc.vo.scm.service.ServcallVO;

/**
 * ��������:�ڳ��ݹ���ⵥ ����: ��������: �޸ģ�2004-09-10 ԬҰ
 * 
 * @modify: since v502, ֧�ַ��ռ���ģʽ�µ��ڳ����������ڳ��ݹ���ⵥ��������ⵥ�����ɹ���˾
 */
public class InitialUI extends nc.ui.pub.ToftPanel implements ActionListener,
    BillEditListener, BillEditListener2, BillTableMouseListener, IDataSource,
    javax.swing.event.ListSelectionListener, IBillRelaSortListener2,
    ILinkMaintain,// �����޸�
    ILinkAdd,// ��������
    ILinkApprove,// ������
    ILinkQuery,// ������
    ISetBillVO

{
  // since v502, ֧�ֶ�����������
  class IBillRelaSortListener2Impl implements IBillRelaSortListener2 {

    public Object[] getRelaSortObjectArray() {
      return m_orderVOs;
    }

  }

  // since v53,֧���б�����
  ButtonObject m_btnListLinkQuery = new ButtonObject(NCLangRes.getInstance()
      .getStrByID("scmcommon", "UPPSCMCommon-000145")/*
                                                       * @res "����"
                                                       */, NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000145")/*
                                                                               * @res
                                                                               * "����"
                                                                               */, "����");

  // ������ư�ť
  private ButtonObject m_buttons1[] = null;

  private ButtonObject m_buttons2[] = null;

  private ButtonObject m_buttons3[] = null;

  // ��ť״̬��0 ������1 �ûң�2 ������
  private int m_nButtonState[] = null;

  // ����״̬
  private int m_nUIState = 1;

  // ����
  private BillCardPanel m_billPanel1 = null;

  private BillCardPanel m_billPanel2 = null;

  private BillListPanel m_listPanel = null;

  // ��λ���룬ϵͳӦ�ṩ������ȡ
  private String m_sUnitCode = getCorpPrimaryKey();

  // ����
  private int m_nPresentRecord = 0;

  // �ڳ���ⵥ
  private GeneralHVO m_VOs[] = null;

  private OorderVO m_orderVOs[] = null;

  // �ֵ���ʽ
  private int m_nMode = 0;

  private UIDialog m_dialog = null;

  private UIRadioButton m_rb1 = null;

  private UIRadioButton m_rb2 = null;

  private UIRadioButton m_rb3 = null;

  private UIRadioButton m_rb4 = null;

  private UIButton m_bnOK = null;

  private UIButton m_bnCancel = null;

  // �Ƿ������ڳ��ݹ���ⵥ
  private boolean m_bAdd = false;

  // �ڳ��ݹ���ⵥ�ӱ����ӱ��Ƿ��ѯ
  // private UFBoolean m_bBodyQuery[] = null;
  // �Ƿ񶩵�������ⵥ
  private boolean m_bGenerate = false;

  // ����Ƿ�����
  private boolean m_bICStartUp = false;

  // �Ѽ���
  private boolean m_bSettling = false;

  // ֻ�������ð�ť {this.setButtons()}
  private ButtonObject m_buttons11[] = null;

  private ButtonObject m_buttons31[] = null;

  // ��ѯ����
  private InitialUIQryDlg m_condClient1 = null;

  private PoQueryCondition m_condClient2 = null;
  
  //�²�ѯģ��
  //V55 by zhaoyha at 2008.10.15
  private InitialQueryOrderDlg m_condQrderDlg = null;

  // ������Խ���
  private nc.ui.scm.pub.InvoInfoBYFormula m_formula = new nc.ui.scm.pub.InvoInfoBYFormula();

  // ������
  private FreeItemRefPane m_freeItem = null;

  // �������ɵ��ڳ���ⵥ
  private GeneralHVO m_gVOs[] = null;

  private SysInitVO m_initVO = null;

  // �ɹ����С��λ(��λ��С��λ)
  private int m_moneyDecimal = 2;

  private int m_nRestoreState[] = null;

  // �ɹ�����С��λ
  private int m_priceDecimal = 2;

  // �б��ӡ
  private ScmPrintTool m_printList = null;

  private ScmPrintTool m_printCard = null;

  private UIRadioButton m_rb5 = null;

  // ��Դ�Ƕ�������ⵥ��ҵ�����ͼ���
  private String m_sBiztypeID[] = null;

  // ����ID����
  private String m_sDeptID = null;

  // �ݹ�������Դ
  private String m_sEstPriceSource = null;

  // ������С��λ
  private int m_unitDecimal = 2;

  // ������С��λ
  private int m_unitDecimalAssist = 2;

  // ϵͳ��ʼ������ "BD501","BD505","BD301"
  private int measure[] = null;

  // �ɹ�ϵͳ��������
  private UFDate m_dSystem = null;

  // �Ƿ��ݹ�Ӧ��
  private UFBoolean m_bZGYF = new UFBoolean(false);

  // zhf add
  private POPubSetUI2 m_cardPoPubSetUI2 = null;

  // ���ұ���
  private String m_sCurrTypeID = null;

  // end

  /**
   * OrderUI ������ע�⡣
   */
  public InitialUI() {
    super();
    init();
  }

  /**
   * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����:
   * 
   * @param event
   *          nc.ui.pub.beans.UIDialogEvent
   */
  public void actionPerformed(ActionEvent event) {
    UIButton button = (UIButton) event.getSource();

    if (button == m_bnOK) {
      if (m_rb1.isSelected()) {
        m_nMode = 0;
        m_initVO.setValue("��Ӧ��");
      }
      if (m_rb2.isSelected()) {
        m_nMode = 1;
        m_initVO.setValue("���+��Ӧ��");
      }
      if (m_rb3.isSelected()) {
        m_nMode = 2;
        m_initVO.setValue("����");
      }
      if (m_rb4.isSelected()) {
        m_nMode = 3;
        m_initVO.setValue("�ֿ�+��Ӧ��");
      }
      if (m_rb5.isSelected()) {
        m_nMode = 4;
        m_initVO.setValue("����+�ֿ�");
      }

      m_dialog.closeOK();
    }
    else {
      m_dialog.closeCancel();
      return;
    }

    // ��������ϵͳ�ķֵ���ʽ
    SysInitVO tempVOs[] = new SysInitVO[1];
    tempVOs[0] = new SysInitVO();
    tempVOs[0] = (SysInitVO) m_initVO.clone();
    tempVOs[0].setPk_corpid(m_sUnitCode);
    try {
      SysInitBO_Client.saveSysInitVOs(tempVOs);
    }
    catch (Exception e) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000040")/* @res "�������÷ֵ���ʽ" */,
          e.toString());
      return;
    }
    return;
  }

  private POPubSetUI2 getCardPoPubSetUI2() {
    if (m_cardPoPubSetUI2 == null) {
      m_cardPoPubSetUI2 = new POPubSetUI2();
    }
    return m_cardPoPubSetUI2;
  }

  /**
   * Ϊ�����������û��ʾ��� ԭ�ҽ��ҵ�񾫶�
   * 
   * @author zhanghongfang
   * @time 2008-7-29 ����07:03:39
   */

  private void setBodyDigits() {
    OorderVO[] VOs = null;
    VOs = (OorderVO[]) getBillCardPanel2().getBillData().getBodyValueVOs(
        "nc.vo.ps.settle.OorderVO");
    int row = 0;

    ArrayList<String> currIdList = new ArrayList<String>();
    String sCurrId = null;

    for (OorderVO vo : VOs) {
      sCurrId = PuPubVO.getString_TrimZeroLenAsNull(vo.getCurrencytypeid());
      if (sCurrId == null) {
        continue;
      }
      if (!currIdList.contains(sCurrId)
          && !sCurrId.equalsIgnoreCase(m_sCurrTypeID)) {
        currIdList.add(sCurrId);
      }
    }
    // ��ȡ���ֶ�Ӧҵ�񾫶�
    HashMap<String, Integer> mnyDigitMap = new HashMap<String, Integer>();
    if (currIdList != null && currIdList.size() > 0) {
      String[] currIds = currIdList.toArray(new String[0]);
      int[] iMnyDigits = getCardPoPubSetUI2().getMoneyDigitByCurr_Busi_Batch(
          currIds);
      if (currIds.length != iMnyDigits.length)
        return;
      for (int i = 0; i < currIds.length; i++) {
        mnyDigitMap.put(currIds[i], new Integer(iMnyDigits[i]));
      }
    }

    for (OorderVO vo : VOs) {
      sCurrId = vo.getCurrencytypeid() == null ? "" : vo.getCurrencytypeid()
          .trim();
      int iaExchRateDigit = getDigits_ExchangeRate(sCurrId) == null ? 5
          : getDigits_ExchangeRate(sCurrId)[0];
      getBillCardPanel2().getBillModel().getItemByKey("nexchangeotobrate")
          .setDecimalDigits(iaExchRateDigit);
      getBillCardPanel2().getBillModel().setValueAt(
          m_orderVOs[row].getNexchangeotobrate(), row, "nexchangeotobrate");

      int iMnyDigit = PuPubVO.getInteger_NullAs(
          mnyDigitMap.get(vo.getCurrencytypeid()), new Integer(m_moneyDecimal))
          .intValue();

      getBillCardPanel2().getBillModel().getItemByKey("noriginaltaxpricemny")
          .setDecimalDigits(iMnyDigit);

      getBillCardPanel2().getBillModel().getItemByKey("noriginalcurmny")
          .setDecimalDigits(iMnyDigit);
      getBillCardPanel2().getBillModel().getItemByKey("norgtaxmoney")
          .setDecimalDigits(iMnyDigit);

      getBillCardPanel2().getBillModel().setValueAt(
          m_orderVOs[row].getNoriginaltaxpricemny(), row,
          "noriginaltaxpricemny");
      getBillCardPanel2().getBillModel().setValueAt(
          m_orderVOs[row].getNoriginalcurmny(), row, "noriginalcurmny");
      getBillCardPanel2().getBillModel().setValueAt(
          m_orderVOs[row].getNorgtaxmoney(), row, "norgtaxmoney");

      row++;
    }
  }

  /**
   * ���ݱ�������ʵʱ�۱����� zhf v55
   */
  private void setExchangeRate(int row, String sCurrId) {
    // ����������ʾ����
    int[] iaExchRateDigit = getCardPoPubSetUI2().getBothExchRateDigit(
        getCorpPrimaryKey(), sCurrId);
    int iMnyDigit = getCardPoPubSetUI2().getMoneyDigitByCurr_Busi(sCurrId);

    getBillCardPanel2().getBodyItem("nexchangeotobrate").setDecimalDigits(
        iaExchRateDigit[0]);
 
    // ����ֵ
    UFDouble[] daRate = getCardPoPubSetUI2().getBothExchRateValue(
        getCorpPrimaryKey(), sCurrId,
        nc.ui.po.pub.PoPublicUIClass.getLoginDate());// ��ǰ����ȡ��ʵʱ����
    getBillCardPanel2().setBodyValueAt(daRate[0], row, "nexchangeotobrate");

    getBillCardPanel2().getBillModel().getItemByKey("noriginaltaxpricemny")
        .setDecimalDigits(iMnyDigit);
    getBillCardPanel2().getBillModel().getItemByKey("noriginalcurmny")
        .setDecimalDigits(iMnyDigit);
    getBillCardPanel2().getBillModel().getItemByKey("norgtaxmoney")
        .setDecimalDigits(iMnyDigit);

    // ���¸�ֵ ������Ч
    getBillCardPanel2().getBillModel().setValueAt(
        getBillCardPanel2().getBodyValueAt(row, "noriginaltaxpricemny"), row,
        "noriginaltaxpricemny");
    getBillCardPanel2().getBillModel().setValueAt(
        getBillCardPanel2().getBodyValueAt(row, "noriginalcurmny"), row,
        "noriginalcurmny");
    getBillCardPanel2().getBillModel().setValueAt(
        getBillCardPanel2().getBodyValueAt(row, "norgtaxmoney"), row,
        "norgtaxmoney");
  }

  /**
   * ���ָ�����ֵĻ��ʾ��ȣ��۱�+�۸��� zhf v55
   */
  private int[] getDigits_ExchangeRate(String sCurrId) {
    int[] iaExchRateDigit = null;
    // �õ��۱����۸����ʾ���
    iaExchRateDigit = getCardPoPubSetUI2().getBothExchRateDigit(
        getCorpPrimaryKey(), sCurrId);
    return iaExchRateDigit;
  }

  // /**
  // * @���� zhf
  // * @����ʱ�䣺2008-6-17����01:56:12
  // * @param iRow
  // * @return void
  // * @˵����(for v5.5)���ñ����л��ʾ���
  // * @�޸��ߣ�
  // * @�޸�ʱ�䣺
  // * @�޸�˵����
  // */
  //
  // protected void setRowDigits_ExchangeRate(int iRow) {
  // // ȡ�ñ���
  // String sCurrId = (String) getBillCardPanel2().getBillModel().getValueAt(
  // iRow, "currencytypeid");
  // int[] iaExchRateDigit = getDigits_ExchangeRate(sCurrId);
  // // �õ��۱����۸����ʾ���
  // if (iaExchRateDigit == null || iaExchRateDigit.length == 0) {
  // getBillCardPanel2().getBillModel().getItemByKey("nexchangeotobrate")
  // .setDecimalDigits(2);
  // }
  // else {
  // getBillCardPanel2().getBillModel().getItemByKey("nexchangeotobrate")
  // .setDecimalDigits(iaExchRateDigit[0]);
  // }
  // }

  /**
   * �༭�۱����ʺ��� zhf
   */
  private void afterEditWhenNexchRate(int row) {

    // �۱�����
    Object bRate = getBillCardPanel2().getBodyValueAt(row, "nexchangeotobrate");
    if (bRate == null || bRate.toString().trim().length() == 0) {
      return;
    }
    UFDouble ufBRate = new UFDouble(bRate.toString());
    EstimateUI.setLocalPriceMnyFrmOrg(getBillCardPanel2(), getCorpPrimaryKey(),
        row, ufBRate, m_priceDecimal,m_moneyDecimal, "ngaugenum");
  }

  /**
   * �޸�ԭ�ұ����޸ĺ��������� zhf
   */
  private void afterEditWhenCurrency(int row) throws Exception {

    Object oCurrId = getBillCardPanel2().getBodyValueAt(row, "currencytypeid");
    if (PuPubVO.getString_TrimZeroLenAsNull(oCurrId) == null) {
      MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID(
          "40040503", "UPP40040503-000033"/* @res "�ݹ����� */), NCLangRes
          .getInstance().getStrByID("4004050301", "UPP4004050301-000011")/*
                                                                           * @res
                                                                           * ԭ�ұ��ֲ���Ϊ�գ���¼��ԭ�ұ���
                                                                           */);
      return;
    }
    String sCurrId = oCurrId.toString().trim();

    setExchangeRate(row, sCurrId);
    if (sCurrId.equalsIgnoreCase(m_sCurrTypeID))// �жϵ�ǰ�����Ƿ�˾��λ��
      // �����۱����ʱ༭����
      getBillCardPanel2().getBillModel().setCellEditable(row,
          "nexchangeotobrate", false);
    else
      getBillCardPanel2().getBillModel().setCellEditable(row,
          "nexchangeotobrate", true);

    // �۱�����
    Object bRate = getBillCardPanel2().getBodyValueAt(row, "nexchangeotobrate");
    if (bRate == null || bRate.toString().trim().length() == 0) {
      return;
    }
    UFDouble ufBRate = new UFDouble(bRate.toString());
    EstimateUI.setLocalPriceMnyFrmOrg(getBillCardPanel2(), getCorpPrimaryKey(),
        row, ufBRate, m_priceDecimal, m_moneyDecimal, "ngaugenum");
  }

  private void updateOorderVO(Integer[] rows) {
    OorderVO[] vos = (OorderVO[]) getBillCardPanel2().getBillModel()
        .getBodyValueVOs("nc.vo.ps.settle.OorderVO");
    for (int i = 0; i < rows.length; i++) {
      m_orderVOs[rows[i].intValue()].setNoriginalcurmny(vos[rows[i].intValue()]
          .getNoriginalcurmny());
      m_orderVOs[rows[i].intValue()].setNorgtaxmoney(vos[rows[i].intValue()]
          .getNorgtaxmoney());
      m_orderVOs[rows[i].intValue()].setNoriginaltaxpricemny(vos[rows[i]
          .intValue()].getNoriginaltaxpricemny());

      m_orderVOs[rows[i].intValue()].setNorgnettaxprice(vos[rows[i].intValue()]
          .getNorgnettaxprice());
      m_orderVOs[rows[i].intValue()].setNoriginalnetprice(vos[rows[i]
          .intValue()].getNoriginalnetprice());

      m_orderVOs[rows[i].intValue()].setNtaxmoney(vos[rows[i].intValue()]
          .getNtaxmoney());
      m_orderVOs[rows[i].intValue()].setNmoney(vos[rows[i].intValue()]
          .getNmoney());
      m_orderVOs[rows[i].intValue()].setNtotalmoney(vos[rows[i].intValue()]
          .getNtotalmoney());

      m_orderVOs[rows[i].intValue()].setNprice(vos[rows[i].intValue()]
          .getNprice());
      m_orderVOs[rows[i].intValue()].setNtaxprice(vos[rows[i].intValue()]
          .getNtaxprice());

      m_orderVOs[rows[i].intValue()].setCurrencytypeid(vos[rows[i].intValue()]
          .getCurrencytypeid());
      //�ٴ�ģ����ȡһ�λ���,���򾫶ȿ��ܴ�������
      m_orderVOs[rows[i].intValue()].setNexchangeotobrate((UFDouble)getBillCardPanel2().
          getBodyValueAt(i, "nexchangeotobrate"));
      // by zhaoyha at 2008.10.29
      m_orderVOs[rows[i].intValue()].setNzgyfnotaxmoney(vos[rows[i].intValue()].getNzgyfnotaxmoney());
      m_orderVOs[rows[i].intValue()].setNzgyfnotaxprice(vos[rows[i].intValue()].getNzgyfnotaxprice());
      // by zhaoyha end
    }
  }

  /**
   * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����:
   */
  public void afterEdit(BillEditEvent event) {
    // �ݹ�����(ѡ�񶩵�����)�༭�¼�

    if (getBillCardPanel2().isShowing()) {

      String strItemKey = event.getKey();
      int row = event.getRow();

      // �޸ı���
      if (strItemKey.equals("currencytypename")) {
        try {
          afterEditWhenCurrency(row);
          // // �ѽ���任������ݸ��µ�����
          // updateOorderVO(row);
          return;
        }
        catch (Exception e) {
          SCMEnv.out(e);
        }
      }
      if (strItemKey.equals("nexchangeotobrate")) {// �޸Ļ���
        afterEditWhenNexchRate(row);
        // // �ѽ���任������ݸ��µ�����
        // updateOorderVO(row);
        return;
      }
      String strDisCntName = "Ӧ˰�ں�";/*-=notranslate=-*/
      if (m_orderVOs[row].getIdiscounttaxtype().intValue() == 1)
        strDisCntName = "Ӧ˰���";
      if (m_orderVOs[row].getIdiscounttaxtype().intValue() == 2)
        strDisCntName = "����˰";
      //
      // �޸ı��Һ���������
      UFDouble ufRate = getBillCardPanel2().getBodyValueAt(row,
          "nexchangeotobrate") == null ? new UFDouble(1.0)
          : (UFDouble) getBillCardPanel2().getBodyValueAt(row,
              "nexchangeotobrate");
      if ("nprice".equalsIgnoreCase(strItemKey)
          || "nmoney".equalsIgnoreCase(strItemKey)
          || "ntaxprice".equalsIgnoreCase(strItemKey)
          || "ntotalmoney".equalsIgnoreCase(strItemKey)
          || "ngaugenum".equalsIgnoreCase(strItemKey)) {
              
        EstimateUI.computeBodyData(m_orderVOs, strDisCntName,
            getBillCardPanel2(), event, this);
        // ����ֵ�������������ԭ������
        String sCurrId = PuPubVO
            .getString_TrimZeroLenAsNull(getBillCardPanel2().getBodyValueAt(
                row, "currencytypeid"));
        //�޸��ݹ�������ԭ��ҲҪ���¼��㣬�������ݹ��ɱ������� V55 by zhaoyha at 2008.9.27
        if("ngaugenum".equalsIgnoreCase(strItemKey)){
          EstimateUI.computeOrgBodyData(m_orderVOs, strDisCntName,
              getBillCardPanel2(), event, this);
          EstimateUI.setYFLocalPriceMnyFrmOrg(getBillCardPanel2(),
              getCorpPrimaryKey(), row, ufRate, m_priceDecimal, m_moneyDecimal,
              "ngaugenum");
        }
        return;
      }
      //�޸�ԭ�Һ���Ϣ��,��������,���򱾱�����
      else if("noriginalnetprice".equalsIgnoreCase(strItemKey) || "noriginalcurmny".equalsIgnoreCase(strItemKey) 
          || "norgnettaxprice".equalsIgnoreCase(strItemKey) || "noriginaltaxpricemny".equalsIgnoreCase(strItemKey)){
        // �޸�ԭ�Һ���������
        EstimateUI.computeOrgBodyData(m_orderVOs, strDisCntName,
            getBillCardPanel2(), event, this);
        // ԭ��ֵ������������㱾������
        EstimateUI.setLocalPriceMnyFrmOrg(getBillCardPanel2(),
            getCorpPrimaryKey(), row, ufRate, m_priceDecimal, m_moneyDecimal,
            "ngaugenum");
      }
      //�޸�˰�ʺ�,�ݹ��ɱ����ݹ�Ӧ���ı��Ҷ�Ҫ����,���ݹ�Ӧ����ԭ�Ҳ����ݹ��ɱ��ı�������
      else if("ntaxrate".equalsIgnoreCase(strItemKey)){
        // �ݹ��ɱ���������
        EstimateUI.computeBodyData(m_orderVOs, strDisCntName,
            getBillCardPanel2(), event, this);
        // �ݹ�Ӧ��ԭ����������
        EstimateUI.computeOrgBodyData(m_orderVOs, strDisCntName,
            getBillCardPanel2(), event, this);
        // �ݹ�Ӧ��ԭ�����ݹ�Ӧ����������
        EstimateUI.setYFLocalPriceMnyFrmOrg(getBillCardPanel2(),
            getCorpPrimaryKey(), row, ufRate, m_priceDecimal, m_moneyDecimal,
            "ngaugenum");
       }
      return;
    }
    // ��ⵥ����༭�¼�
    if (m_nUIState == 1 && event.getKey().trim().equals("cdptid")) {
      int nRow = getBillCardPanel1().getRowCount();
      GeneralHVO VO = new GeneralHVO(nRow);
      getBillCardPanel1().getBillValueVO(VO);

      String sKey = VO.getHeadVO().getCdptid();
      UIRefPane nRefPanel = (UIRefPane) getBillCardPanel1().getHeadItem(
          "cbizid").getComponent();
      String sWhere = " bd_psndoc.pk_deptdoc = '" + sKey + "'";

      if (sKey != null && sKey.length() > 0) {
        nRefPanel.setWhereString(sWhere);

        if (!sKey.equals(m_sDeptID)) {
          // ���ҵ��Ա�����ڲ���,���ҵ��Ա
          UIRefPane nRefPanel0 = (UIRefPane) getBillCardPanel1().getHeadItem(
              "cbizid").getComponent();
          nRefPanel0.setValue(null);
          nRefPanel0.setPK(null);
        }
      }
      else {
        nRefPanel.setWhereString("bd_deptdoc.pk_corp = '" + m_sUnitCode
            + "' and bd_deptdoc.pk_deptdoc = bd_psndoc.pk_deptdoc");
      }
    }
    else if (m_nUIState == 1 && event.getKey().trim().equals("cbizid")) {
      
      String sPsnID = ((UIRefPane) getBillCardPanel1().getHeadItem("cbizid").getComponent()).getRefPK();
      if (sPsnID != null && sPsnID.length() > 0) {
        try {
          String sDeptName = EstimateHelper.getRefDeptName(sPsnID);
          UIRefPane nRefPanel = (UIRefPane) getBillCardPanel1().getHeadItem(
              "cdptid").getComponent();
          nRefPanel.setValue(sDeptName);
          nRefPanel.setPK(EstimateHelper.getRefDeptKey(sPsnID));

          m_sDeptID = nRefPanel.getRefPK();
        }
        catch (Exception e) {
          SCMEnv.out(e);
          MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000041")/* @res "���ò���" */,
              e.getMessage());
          return;
        }
      }
    }
    else if (m_nUIState == 1 && event.getKey().trim().equals("dbilldate")) {
      UIRefPane nRefPanel = (UIRefPane) getBillCardPanel1().getHeadItem(
          "dbilldate").getComponent();
      if (nRefPanel.getText().length() == 0) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000042")/* @res "�޸��������" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
                "UPP40040503-000043")/* @res "������ڲ���Ϊ�գ�" */);
        nRefPanel.setValue(m_dSystem.toString());
        return;
      }

      UFDate dBillDate = new UFDate(nRefPanel.getText());
      if (dBillDate.compareTo(m_dSystem) > 0) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID(
            "40040503", "UPP40040503-000042")/* @res "�޸��������" */, NCLangRes
            .getInstance().getStrByID("40040503", "UPP40040503-000044", null,
                new String[] {
                    m_dSystem.toString(), m_dSystem.getDateAfter(1).toString()
                })
        /* @res "�ڳ��ݹ���ⵥ��������ڲ�������{0},ϵͳ��������{1}" */);
        nRefPanel.setValue(m_dSystem.toString());
        return;
      }
      // ����ͷ���������������ñ����ҵ�����ڼ��ݹ����ڣ������ݹ���ͳ�Ʊ�����ȷ��FENGPING��2008-01-08
      else {
        int iRowCnt = getBillCardPanel1().getRowCount();
        for (int i = 0; i < iRowCnt; i++) {
          getBillCardPanel1().getBillModel().setValueAt(dBillDate, i,
              "dbizdate");
          getBillCardPanel1().getBillModel()
              .setValueAt(dBillDate, i, "dzgdate");
          getBillCardPanel1().getBillModel().setRowState(i,
              BillModel.MODIFICATION);
        }
      }
    }
    else if (m_nUIState == 1
        && (event.getKey().trim().equals("ninnum")
            || event.getKey().trim().equals("nprice") || event.getKey().trim()
            .equals("nmny"))) {
      computeBBodyData(event);

    }
    else if (m_nUIState == 1 && event.getKey().trim().equals("vfree")) {
      // �����������ֵ
      FreeVO freeVO = m_freeItem.getFreeVO();
      getBillCardPanel1().setBodyValueAt(freeVO.getVfree1(), event.getRow(),
          "vfree1");
      getBillCardPanel1().setBodyValueAt(freeVO.getVfree2(), event.getRow(),
          "vfree2");
      getBillCardPanel1().setBodyValueAt(freeVO.getVfree3(), event.getRow(),
          "vfree3");
      getBillCardPanel1().setBodyValueAt(freeVO.getVfree4(), event.getRow(),
          "vfree4");
      getBillCardPanel1().setBodyValueAt(freeVO.getVfree5(), event.getRow(),
          "vfree5");

    }
    else if (m_nUIState == 1 && event.getKey().trim().endsWith("crowno")) {
      // �к�
      nc.ui.scm.pub.report.BillRowNo.afterEditWhenRowNo(getBillCardPanel1(),
          event, "4T");

    }
    else if (m_nUIState == 1 && event.getKey().trim().endsWith("cwarehouseid")) {
      // �ֿ�
      setCalbodyValue();
    }
    // since v53�ع����ݹ����������ۡ����������ݹ�����(ѡ�񶩵�����)�༭
    if (m_nUIState == 2) {
      // computeBodyData(event);
    }
    // since v53,֧�ֵ���ģ���Զ�����ά��
    if (getBillCardPanel1().isShowing()
        && event.getKey().startsWith("vuserdef")) {
      if (event.getPos() == BillItem.HEAD) {
        DefSetTool.afterEditHead(getBillCardPanel1().getBillData(), event
            .getKey(), "pk_defdoc"
            + event.getKey().substring("vuserdef".length(),
                event.getKey().length()));
      }
      else if (event.getPos() == BillItem.BODY) {
        DefSetTool.afterEditBody(getBillCardPanel1().getBillModel(), event
            .getRow(), event.getKey(), "pk_defdoc"
            + event.getKey().substring("vuserdef".length(),
                event.getKey().length()));
      }
    }
  }

  /**
   * �Ƿ�δ��ѯ������
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * 
   * @param voCur
   *          <p>
   * @author czp
   * @time 2007-7-16 ����01:27:18
   */
  private boolean isNoBodys(GeneralHVO voCur) {
    boolean bNoLoaded = true;
    try {
      bNoLoaded = (voCur == null || voCur.getChildrenVO() == null
          || voCur.getChildrenVO().length == 0 || PuPubVO
          .getString_TrimZeroLenAsNull(voCur.getChildrenVO()[0].getPrimaryKey()) == null);
    }
    catch (BusinessException e) {
      // �����쳣ʱ������������²�ѯ
      SCMEnv.out(e);
    }
    return bNoLoaded;
  }

  /**
   * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����:
   */
  public void bodyRowChange(BillEditEvent event) {
    if ((UITable) event.getSource() == getBillListPanel().getHeadTable()) {
      // ��ⵥ�б�
      m_nPresentRecord = event.getRow();

      if (m_nPresentRecord >= 0) {
        // ��ø��ŵ��ݵı���,sice v502 �ع�
        // if((!m_bGenerate) && m_bBodyQuery != null &&
        // m_bBodyQuery.length > 0 &&
        // (!m_bBodyQuery[m_nPresentRecord].booleanValue())){
        if (!m_bGenerate && isNoBodys(m_VOs[m_nPresentRecord])) {
          try {
            ArrayList list = EstimateHelper.queryInitialBody(
                m_VOs[m_nPresentRecord].getHeadVO().getCgeneralhid(),
                m_VOs[m_nPresentRecord].getHeadVO().getTs());
            if (list == null || list.size() == 0)
              throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
                  .getStrByID("4004050302", "UPP4004050302-000004")/*
                                                                     * @res
                                                                     * "����������������ˢ�½�������ԣ�"
                                                                     */);
            GeneralHItemVO tempBodyVO[] = (GeneralHItemVO[]) list.get(0);
            GeneralBb3VO tempBb3VO[] = (GeneralBb3VO[]) list.get(1);
            m_VOs[m_nPresentRecord].setChildrenVO(tempBodyVO);
            m_VOs[m_nPresentRecord].setGrandVO(tempBb3VO);
          }
          catch (BusinessException e) {
            SCMEnv.out(e);
            MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
                .getStrByID("40040503", "UPP40040503-000045")/*
                                                               * @res "��������"
                                                               */, e.getMessage());
            return;
          }
          catch (Exception e) {
            SCMEnv.out(e);
          }
          // m_bBodyQuery[m_nPresentRecord] = new UFBoolean(true);
        }

        GeneralHItemVO bodyVO[] = m_VOs[m_nPresentRecord].getBodyVO();
        getBillListPanel().getBodyBillModel().setBodyDataVO(bodyVO);
        getBillListPanel().getBodyBillModel().execLoadFormula();
        getBillListPanel().getBodyBillModel().updateValue();
        // ��ʾ��Դ������Ϣ
        nc.ui.pu.pub.PuTool.loadSourceBillData(getBillListPanel(), "4T");

        // ���ÿ����֯
        Object o = getBillListPanel().getHeadBillModel().getValueAt(
            event.getRow(), "cstoreorganization");
        if (o != null)
          m_VOs[m_nPresentRecord].getHeadVO().setCstoreorganization(
              o.toString());
        GeneralHItemVO tempbodyVO[] = m_VOs[m_nPresentRecord].getBodyVO();
        // ������Դ������������
        for (int j = 0; j < tempbodyVO.length; j++) {
          Object oTemp = getBillListPanel().getBodyBillModel().getValueAt(j,
              "csourcebillname");
          if (oTemp != null)
            tempbodyVO[j].setCsourcebillname(oTemp.toString());
          oTemp = getBillListPanel().getBodyBillModel().getValueAt(j,
              "csourcebillcode");
          if (oTemp != null)
            tempbodyVO[j].setCsourcebillcode(oTemp.toString());
        }
        //

      }
    }
  }

  /**
   * �˴����뷽��˵���� ��������:�ı���水ť״̬ �������: ����ֵ: �쳣����:
   */
  private void changeButtonState() {
    if (m_nUIState == 1) {
      for (int i = 0; i < m_nButtonState.length; i++) {
        if (m_nButtonState[i] == 0) {
          m_buttons1[i].setVisible(true);
          m_buttons1[i].setEnabled(true);
        }
        if (m_nButtonState[i] == 1) {
          m_buttons1[i].setVisible(true);
          m_buttons1[i].setEnabled(false);
        }
        if (m_nButtonState[i] == 2) {
          m_buttons1[i].setVisible(false);
        }

        this.updateButton(m_buttons1[i]);
      }
    }

    else if (m_nUIState == 2) {
      for (int i = 0; i < m_nButtonState.length; i++) {
        if (m_nButtonState[i] == 0) {
          m_buttons2[i].setVisible(true);
          m_buttons2[i].setEnabled(true);
        }
        if (m_nButtonState[i] == 1) {
          m_buttons2[i].setVisible(true);
          m_buttons2[i].setEnabled(false);
        }
        if (m_nButtonState[i] == 2) {
          m_buttons2[i].setVisible(false);
        }

        this.updateButton(m_buttons2[i]);
      }
    }

    else if (m_nUIState == 3) {
      for (int i = 0; i < m_nButtonState.length; i++) {
        if (m_nButtonState[i] == 0) {
          m_buttons3[i].setVisible(true);
          m_buttons3[i].setEnabled(true);
        }
        if (m_nButtonState[i] == 1) {
          m_buttons3[i].setVisible(true);
          m_buttons3[i].setEnabled(false);
        }
        if (m_nButtonState[i] == 2) {
          m_buttons3[i].setVisible(false);
        }

        this.updateButton(m_buttons3[i]);
      }
    }
  }

  /**
   * �ڳ���ⵥ,��Ƭģ��
   */
  private BillCardPanel getBillCardPanel1() {
    if (m_billPanel1 == null) {
      try {
        m_billPanel1 = new BillCardPanel();
        //
        m_billPanel1.loadTemplet("4T", null, getClientEnvironment().getUser()
            .getPrimaryKey(), getClientEnvironment().getCorporation()
            .getPk_corp());

        // ����ģ��
        // BillData bd = new
        // BillData(m_billPanel1.getTempletData("40040503020100000000"));
        BillData bd = m_billPanel1.getBillData();

        bd = initDecimal1(bd);

        // ���κŲ���
        LotNumbRefPane lotRef = new LotNumbRefPane();
        if (lotRef != null) {
          lotRef.setMaxLength(bd.getBodyItem("vbatchcode").getLength());
        }
        bd.getBodyItem("vbatchcode").setComponent((JComponent) lotRef);

        // ������
        m_freeItem = new FreeItemRefPane();
        m_freeItem.setMaxLength(bd.getBodyItem("vfree").getLength());
        bd.getBodyItem("vfree").setComponent(m_freeItem);

        m_billPanel1.setBillData(bd);
        m_billPanel1.setTatolRowShow(true);
        //m_billPanel1.setShowThMark(true);

        // ���ӵ��ݱ༭����
        m_billPanel1.addEditListener(this);
        m_billPanel1.addBodyEditListener2(this);
        m_billPanel1.setBodyMenuShow(false);

        // ��ʼ���к�
        nc.ui.scm.pub.report.BillRowNo.loadRowNoItem(m_billPanel1, "crowno");

        // �޸��Զ�����
        DefSetTool.updateBillCardPanelUserDef(m_billPanel1,
            getClientEnvironment().getCorporation().getPk_corp(), "4T", // ��������
            "vuserdef", "vuserdef");
        // Ϊʹ�Զ�����������ʾ��ģ����,���������趨һ������
        m_billPanel1.setBillData(m_billPanel1.getBillData());
        // V55ѡ��ģʽ
        PuTool.setLineSelected(m_billPanel1);
      }
      catch (java.lang.Throwable ivjExc) {
        SCMEnv.out(ivjExc);
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000024")/* @res "����ģ��" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
                "UPP40040503-000025")/* @res "ģ�岻����!" */);
        return null;
      }
    }
    return m_billPanel1;
  }

  /**
   * ������Ƭģ��--ѡ��Ҫ�����ڳ��ݹ���ⵥ�Ĳɹ�����
   */
  private BillCardPanel getBillCardPanel2() {
    if (m_billPanel2 == null) {
      try {
        m_billPanel2 = new BillCardPanel();

        // ����ģ��
        BillData bd = new BillData(m_billPanel2
            .getTempletData("40040503020200000000"));

        // m_billPanel2.loadTemplet("40040503020200000000");

        //��δ�ݹ�Ӧ������ʾ�ݹ�Ӧ��ҳǩ
        if(!m_bZGYF.booleanValue())
          bd.removeTabItems(BillData.BODY, "zgyf_table");
        
        bd = initDecimal2(bd);

        m_billPanel2.setBillData(bd);

        // ���ӵ��ݱ༭����
        //m_billPanel2.setShowThMark(true);
        m_billPanel2.setTatolRowShow(true);
        m_billPanel2.setTatolRowShow("zgyf_table", true);
        m_billPanel2.addEditListener(this);
        m_billPanel2.addBodyEditListener2(this);
        //��������ҳǩ���Ҽ��˵� V55 by zhaoyha at 2008.9.26 
        for(String strTableCode : m_billPanel2.getBillData().getTableCodes(BillItem.BODY)){
          m_billPanel2.setBodyMenuShow(strTableCode,false);
        }   

        // ����ѡ�����
        m_billPanel2.getBillTable().setCellSelectionEnabled(false);
        m_billPanel2.getBillTable().setSelectionMode(
            javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        m_billPanel2.getBillTable().getSelectionModel()
            .addListSelectionListener(this);
        // m_billPanel2.setBillBeforeEditListenerHeadTail(this);
        m_billPanel2.getBillModel().addSortRelaObjectListener2(
            new IBillRelaSortListener2Impl());

        // ��˰��� 0Ӧ˰�ں� 1Ӧ˰��� 2����˰
        UIComboBox comItem = (UIComboBox) m_billPanel2.getBodyItem(
            "idiscounttaxtype").getComponent();
        m_billPanel2.getBodyItem("idiscounttaxtype").setWithIndex(true);
        comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
            "UPP40040401-000105")/* @res "Ӧ˰�ں�" */);
        comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
            "UPP40040401-000106")/* @res "Ӧ˰���" */);
        comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
            "UPP40040401-000107")/* @res "����˰" */);
        //
        m_billPanel2.setBodyMultiSelect(true);
        //
        m_billPanel2.getBillTable().setRowSelectionAllowed(true);
        m_billPanel2.getBillTable().setColumnSelectionAllowed(false);
        m_billPanel2.getBillTable().setSelectionMode(
            javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        new DefaultCurrTypeBizDecimalListener(m_billPanel2.getBillModel(),
            "currencytypeid", new String[] {
                "noriginaltaxpricemny", "noriginalcurmny", "norgtaxmoney"
            });
        //
        PuTool.setLineSelected(m_billPanel2);
      }
      catch (java.lang.Throwable ivjExc) {
        SCMEnv.out(ivjExc);
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000024")/* @res "����ģ��" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
                "UPP40040503-000025")/* @res "ģ�岻����!" */);
        return null;

      }
    }
    return m_billPanel2;
  }

  /**
   * �ڳ��ݹ���ⵥ���б����
   */
  private BillListPanel getBillListPanel() {
    if (m_listPanel == null) {
      try {
        m_listPanel = new BillListPanel(false);
        m_listPanel.setName("ListPanel");
        try {
          m_listPanel.loadTemplet("4T", null, getClientEnvironment().getUser()
              .getPrimaryKey(), getClientEnvironment().getCorporation()
              .getPk_corp());
        }
        catch (Exception e) {
          SCMEnv.out(e);
          m_listPanel.loadTemplet("40040503020100000000");
        }
        BillListData bd = m_listPanel.getBillListData();
        bd = initListDecimal(bd);
        m_listPanel.setListData(bd);
        //
        DefSetTool.updateBillListPanelUserDef(m_listPanel,
            getClientEnvironment().getCorporation().getPk_corp(), "4T", // ��������
            "vuserdef", "vuserdef");
        //
        //m_listPanel.getParentListPanel().setShowThMark(true);
        //m_listPanel.getChildListPanel().setShowThMark(true);
        m_listPanel.getParentListPanel().setTotalRowShow(true);
        m_listPanel.getChildListPanel().setTotalRowShow(true);
        m_listPanel.addMouseListener(this);
        m_listPanel.addEditListener(this);
        // ��ͷѡ�����
        m_listPanel.getHeadBillModel().addSortRelaObjectListener2(this);
        m_listPanel.getHeadTable().setCellSelectionEnabled(false);
        //
        m_listPanel.getHeadTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // Ϊʹ�Զ�����������ʾ��ģ����,���������趨һ������[1]
        m_listPanel.setListData(m_listPanel.getBillListData());
        // V55ѡ��ģʽ
        PuTool.setLineSelectedList(m_listPanel);
        //�������������[1]����
        m_listPanel.getHeadTable().getSelectionModel().addListSelectionListener(this);
        //
        m_listPanel.updateUI();
      }
      catch (java.lang.Throwable e) {
        SCMEnv.out(e);
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000024")/* @res "����ģ��" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
                "UPP40040503-000025")/* @res "ģ�岻����!" */);
        return null;
      }
    }
    return m_listPanel;
  }

  /**
   * ����ʵ�ָ÷���������ҵ�����ı��⡣
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  public String getTitle() {
    return nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050302",
        "UPP4004050302-000005")/* @res "ά���ڳ��ݹ�" */;
  }
  
  private int buttons1_len = 17;//zhf add

  /**
   * ��������:��ʼ�� �޸ģ�2004-09-10 ԬҰ
   */
  public void init() {
    initpara();
    // ��ʾ��ť
//    m_buttons1 = new ButtonObject[16];   zhf �޸�  ����   �޶�����
    m_buttons1 = new ButtonObject[buttons1_len]; 
    m_buttons1[0] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000002")/*
                                               * @res "����"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000002")/*
                                                                                 * @res
                                                                                 * "����"
                                                                                 */, "����");
    m_buttons1[1] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000045")/*
                                               * @res "�޸�"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000045")/*
                                                                                 * @res
                                                                                 * "�޸�"
                                                                                 */, "�޸�");
    m_buttons1[2] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000001")/*
                                               * @res "����"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000001")/*
                                                                                 * @res
                                                                                 * "����"
                                                                                 */, "����");
    m_buttons1[3] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000013")/*
                                               * @res "ɾ��"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000013")/*
                                                                                 * @res
                                                                                 * "ɾ��"
                                                                                 */, "ɾ��");
    m_buttons1[4] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000008")/*
                                               * @res "ȡ��"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000008")/*
                                                                                 * @res
                                                                                 * "ȡ��"
                                                                                 */, "ȡ��");
    m_buttons1[5] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000005")/*
                                               * @res "����"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000005")/*
                                                                                 * @res
                                                                                 * "����"
                                                                                 */, "ɾ��");
    m_buttons1[6] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000006")/*
                                               * @res "��ѯ"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000006")/*
                                                                                 * @res
                                                                                 * "��ѯ"
                                                                                 */, "��ѯ");
    m_buttons1[7] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UCH031")/*
                                         * @res "��ҳ"
                                         */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH031")/*
                                                                         * @res
                                                                         * "��ҳ"
                                                                         */, "��ҳ");
    m_buttons1[8] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UCH033")/*
                                         * @res "��һҳ"
                                         */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH033")/*
                                                                         * @res
                                                                         * "��һҳ"
                                                                         */, "��һҳ");
    m_buttons1[9] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UCH034")/*
                                         * @res "��һҳ"
                                         */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH034")/*
                                                                         * @res
                                                                         * "��һҳ"
                                                                         */, "��һҳ");
    m_buttons1[10] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UCH032")/*
                                         * @res "ĩҳ"
                                         */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH032")/*
                                                                         * @res
                                                                         * "ĩҳ"
                                                                         */, "ĩҳ");
    m_buttons1[11] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("scmcommon", "UPPSCMCommon-000464")/* @res "�б���ʾ" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon",
            "UPPSCMCommon-000464")/* @res "�б���ʾ" */, "�б���ʾ");
    m_buttons1[12] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000007")/*
                                               * @res "��ӡ"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000007")/*
                                                                                 * @res
                                                                                 * "��ӡ"
                                                                                 */, 2, "��ӡ"); /*-=notranslate=-*/
    m_buttons1[13] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("4004050302", "UPT4004050302-000025")/* @res "��ӡԤ��" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050302",
            "UPT4004050302-000025")/* @res "��ӡԤ��" */, 2, "��ӡԤ��"); /*-=notranslate=-*/
    m_buttons1[14] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("4004050302", "UPT4004050302-000034")/* @res "�ĵ�����" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050302",
            "UPT4004050302-000034")/* @res "�ĵ�����" */, "�ĵ�����");
    m_buttons1[15] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("scmcommon", "UPPSCMCommon-000145")/* @res "����" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon",
            "UPPSCMCommon-000145")/* @res "����" */, "����");
    
    //zhf add
    m_buttons1[16] = new ButtonObject("�޶�","�޶�",2,"�޶�");
//    ahf  end
    
    // ����ά��
    ButtonObject btnOprCombin = new ButtonObject(
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050302",
            "UPT4004050302-000026")/* @res "����ά��" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050302",
            "UPT4004050302-000026")/* @res "����ά��" */, "����ά��");
    btnOprCombin.setChildButtonGroup(new ButtonObject[] {
        m_buttons1[1], m_buttons1[2], m_buttons1[3], m_buttons1[4],
        m_buttons1[5], m_buttons1[16]
    });
    // ���
    ButtonObject btnLookCombin = new ButtonObject(nc.ui.ml.NCLangRes
        .getInstance().getStrByID("common", "UC001-0000021")/*
                                                             * @res "���"
                                                             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000021")/*
                                                                                 * @res
                                                                                 * "���"
                                                                                 */, "���");
    btnLookCombin.setChildButtonGroup(new ButtonObject[] {
        m_buttons1[6], m_buttons1[7], m_buttons1[8], m_buttons1[9],
        m_buttons1[10]
    });
    // ����
    ButtonObject btnOtherCombin11 = new ButtonObject(nc.ui.ml.NCLangRes
        .getInstance().getStrByID("common", "UC001-0000036")/*
                                                             * @res "����"
                                                             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000036")/*
                                                                                 * @res
                                                                                 * "����"
                                                                                 */, "����");
    btnOtherCombin11.setChildButtonGroup(new ButtonObject[] {
        m_buttons1[14], m_buttons1[15], m_buttons1[12], m_buttons1[13]
    });
    m_buttons11 = new ButtonObject[] {
        m_buttons1[0], btnOprCombin, btnLookCombin, m_buttons1[11],
        btnOtherCombin11
    };
    //
    m_buttons3 = new ButtonObject[8];
    m_buttons3[0] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000041")/*
                                               * @res "ȫѡ"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000041")/*
                                                                                 * @res
                                                                                 * "ȫѡ"
                                                                                 */, "ȫѡ");
    m_buttons3[1] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000042")/*
                                               * @res "ȫ��"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000042")/*
                                                                                 * @res
                                                                                 * "ȫ��"
                                                                                 */, "ȫ��");
    m_buttons3[2] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000005")/*
                                               * @res "����"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000005")/*
                                                                                 * @res
                                                                                 * "����"
                                                                                 */, "����");
    m_buttons3[3] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000006")/*
                                               * @res "��ѯ"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000006")/*
                                                                                 * @res
                                                                                 * "��ѯ"
                                                                                 */, "��ѯ");
    m_buttons3[4] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("scmcommon", "UPPSCMCommon-000463")/* @res "��Ƭ��ʾ" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon",
            "UPPSCMCommon-000463")/* @res "��Ƭ��ʾ" */, "��Ƭ��ʾ");
    m_buttons3[5] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("4004050302", "UPT4004050302-000034")/* @res "�ĵ�����" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050302",
            "UPT4004050302-000034")/* @res "�ĵ�����" */, "�ĵ�����");
    m_buttons3[6] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000007")/*
                                               * @res "��ӡ"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000007")/*
                                                                                 * @res
                                                                                 * "��ӡ"
                                                                                 */, 2, "��ӡ"); /*-=notranslate=-*/
    m_buttons3[7] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("4004050302", "UPT4004050302-000025")/* @res "��ӡԤ��" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050302",
            "UPT4004050302-000025")/* @res "��ӡԤ��" */, 2, "��ӡԤ��"); /*-=notranslate=-*/
    // ����
    ButtonObject btnOtherCombin31 = new ButtonObject(nc.ui.ml.NCLangRes
        .getInstance().getStrByID("common", "UC001-0000036")/*
                                                             * @res "����"
                                                             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000036")/*
                                                                                 * @res
                                                                                 * "����"
                                                                                 */, "����");
    btnOtherCombin31.setChildButtonGroup(new ButtonObject[] {
        m_buttons3[5], m_buttons3[6], m_buttons3[7]
    });

    /* since v53,֧���б����� */
    m_buttons31 = new ButtonObject[] {
        m_buttons3[0], m_buttons3[1], m_buttons3[2], m_buttons3[3],
        m_buttons3[4], m_btnListLinkQuery, btnOtherCombin31
    };
    //
    m_buttons2 = new ButtonObject[6];
    m_buttons2[0] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000041")/*
                                               * @res "ȫѡ"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000041")/*
                                                                                 * @res
                                                                                 * "ȫѡ"
                                                                                 */, "ȫѡ");
    m_buttons2[1] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000042")/*
                                               * @res "ȫ��"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000042")/*
                                                                                 * @res
                                                                                 * "ȫ��"
                                                                                 */, "ȫ��");
    m_buttons2[2] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("4004050302", "UPT4004050302-000032")/* @res "�ֵ���ʽ" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050302",
            "UPT4004050302-000032")/* @res "�ֵ���ʽ" */, "�ֵ���ʽ");
    m_buttons2[3] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("4004050302", "UPT4004050302-000033")/* @res "�ݹ�" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050302",
            "UPT4004050302-000033")/* @res "�ݹ�" */, "�ݹ�");
    m_buttons2[4] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000006")/*
                                               * @res "��ѯ"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000006")/*
                                                                                 * @res
                                                                                 * "��ѯ"
                                                                                 */, "��ѯ");
    m_buttons2[5] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000038")/*
                                               * @res "����"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000038")/*
                                                                                 * @res
                                                                                 * "����"
                                                                                 */, "����");

    this.setButtons(m_buttons11);

    // ��ʾ����
    setLayout(new java.awt.BorderLayout());
    add(getBillCardPanel1(), java.awt.BorderLayout.CENTER);
    getBillCardPanel1().setEnabled(false);

    initConfigure();

    // ��ʼ��ť״̬
    m_nButtonState = new int[buttons1_len];
    m_nRestoreState = new int[m_buttons1.length];

    // ���ó�����,��ӡ����ѯ���б�������а�ťΪ��
    for (int i = 0; i < buttons1_len; i++) {
      m_nButtonState[i] = 1;
    }
    m_nButtonState[0] = 0;
    m_nButtonState[6] = 0;
    m_nButtonState[11] = 0;
    m_nButtonState[12] = 0;
    m_nButtonState[13] = 0;
    
//    zhf
    m_nButtonState[16] = 1;

    changeButtonState();

  }

  /**
   * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����:
   */
  public void mouse_doubleclick(BillMouseEnent event) {
    if (event.getPos() == BillItem.HEAD) {
      m_nPresentRecord = event.getRow();

      // �б���������򣬻������ǰ�Ļ����¼ָ��
      m_nPresentRecord = nc.ui.pu.pub.PuTool.getIndexBeforeSort(m_listPanel,
          m_nPresentRecord);

      if (m_nPresentRecord >= 0) {
        this.remove(getBillListPanel());
        getBillCardPanel1().setVisible(true);
        this.setButtons(m_buttons11);
        m_nButtonState = new int[m_buttons1.length];

        if (m_bGenerate && 
            PuPubVO.getString_TrimZeroLenAsNull(m_VOs[m_nPresentRecord].getHeadVO().getCgeneralhid())==null) {
          getBillCardPanel1().setEnabled(false);
          m_bAdd = true;
          // ����ҵ��Ա�Ĳ�������
          String sKey = m_VOs[m_nPresentRecord].getHeadVO().getCdptid();
          UIRefPane nRefPanel = (UIRefPane) getBillCardPanel1().getHeadItem(
              "cbizid").getComponent();
          String sWhere = " bd_psndoc.pk_deptdoc = '" + sKey + "'";
          if (sKey != null && sKey.length() > 0)
            nRefPanel.setWhereString(sWhere);
          else
            nRefPanel.setWhereString("bd_deptdoc.pk_corp = '" + m_sUnitCode
                + "' and bd_deptdoc.pk_deptdoc = bd_psndoc.pk_deptdoc");

          // ���ó����棬ɾ��, ��ӡ�����������а�ťΪ��
          for (int i = 0; i < 16; i++) {
            m_nButtonState[i] = 1;
          }
          m_nButtonState[2] = 0;
          m_nButtonState[3] = 0;
          m_nButtonState[4] = 0;
          m_nButtonState[12] = 0;
          m_nButtonState[13] = 0;
        }
        else {
          // ���ó�����,ȡ��,ɾ�������а�ťΪ����
          for (int i = 0; i < 16; i++) {
            m_nButtonState[i] = 0;
          }
          for (int i = 2; i < 5; i++)
            m_nButtonState[i] = 1;
          if (m_nPresentRecord == 0) {
            m_nButtonState[7] = 1;
            m_nButtonState[8] = 1;
          }
          if (m_nPresentRecord == m_VOs.length - 1) {
            m_nButtonState[9] = 1;
            m_nButtonState[10] = 1;
          }
          if (m_VOs.length == 1) {
            for (int i = 7; i < 11; i++)
              m_nButtonState[i] = 1;
          }
          // �ݹ�Ӧ��ʱ,�������޸ģ�Ҳ�Ͳ������в���
          if (m_bZGYF.booleanValue()) {
            m_nButtonState[1] = 1;
          }
        }
        m_nUIState = 1;
        changeButtonState();

        // ��ʾ��Ƭ����
        getBillCardPanel1().setBillValueVO(m_VOs[m_nPresentRecord]);
        getBillCardPanel1().getBillModel().execLoadFormula();
        // ��ʾ��Դ������Ϣ
        nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");
        getBillCardPanel1().updateValue();
        getBillCardPanel1().updateUI();

        if (m_bGenerate){
          if(PuPubVO.getString_TrimZeroLenAsNull(m_VOs[m_nPresentRecord].getHeadVO().getCgeneralhid())!=null){
            getBillCardPanel1().setEnabled(false);
          }
          else 
            setPartNoEditable();
        }
        // ��ʾ��ע
        UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem(
            "vnote").getComponent();
        nRefPanel1.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVnote());

        // ��ʾ�����֯
        setCalbodyValue();

        if (m_bGenerate) {
          // �ۼ�����ⵥ��
          getBillCardPanel1().transferFocusTo(BillCardPanel.HEAD);
        }

      }
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
    if (bo == m_buttons1[0]) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH028")/* @res "��������" */);
      onAppend();
    }

    if (bo == m_buttons1[1]) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH027")/* @res "�����޸�" */);
      onModify();
    }

    if (bo == m_buttons1[2]) {
      onSave();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH005")/* @res "����ɹ�" */);
    }

    if (bo == m_buttons1[3]) {
      onDeleteLine();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH037")/* @res "ɾ�гɹ�" */);
    }

    if (bo == m_buttons1[4]) {
      onCancelling();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH008")/* @res "ȡ���ɹ�" */);
    }

    if (bo == m_buttons1[5]) {
      onDiscard();
    }

    if (bo == m_buttons1[6]) {
      onCardQuery();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH009")/* @res "��ѯ���" */);
    }

    if (bo == m_buttons1[7])
      onFirst();

    if (bo == m_buttons1[8])
      onPrevious();

    if (bo == m_buttons1[9])
      onNext();

    if (bo == m_buttons1[10])
      onLast();

    if (bo == m_buttons1[11])
      onList();

    if (bo == m_buttons1[12]) {
      onPrint();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH041")/* @res "��ӡ�ɹ�" */);
    }

    if (bo == m_buttons1[13]) {
      onPreview();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "4004COMMON000000021")/* @res "��ӡԤ�����" */);
    }

    if (bo == m_buttons1[14]) {
      onFileManage();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "4004COMMON000000025")/* @res "�ĵ�����ɹ�" */);
    }

    if (bo == m_buttons1[15] || bo == m_btnListLinkQuery) {
      onBillRelateQuery();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "4004COMMON000000019")/* @res "����ɹ�" */);
    }

    if (bo == m_buttons3[0]) {
      onSelectAll();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "4004COMMON000000033")/* @res "ȫѡ�ɹ�" */);
    }

    if (bo == m_buttons3[1]) {
      onSelectNo();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "4004COMMON000000034")/* @res "ȫ���ɹ�" */);
    }

    if (bo == m_buttons3[2]) {
      onDiscard();
    }

    if (bo == m_buttons3[3]) {
      onQueryStock();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH009")/* @res "��ѯ���" */);
    }

    if (bo == m_buttons3[4])
      onSwitch();

    if (bo == m_buttons3[5]) {
      onFileManage();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "4004COMMON000000025")/* @res "�ĵ�����ɹ�" */);
    }

    if (bo == m_buttons3[6]) {
      onListPrint();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH041")/* @res "��ӡ�ɹ�" */);
    }

    if (bo == m_buttons3[7]) {
      onListPreview();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "4004COMMON000000021")/* @res "��ӡԤ�����" */);
    }

    if (bo == m_buttons2[0]) {
      onSelectAll();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "4004COMMON000000033")/* @res "ȫѡ�ɹ�" */);
    }

    if (bo == m_buttons2[1]) {
      onSelectNo();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "4004COMMON000000034")/* @res "ȫ���ɹ�" */);
    }

    if (bo == m_buttons2[2])
      onSelectMode();

    if (bo == m_buttons2[3])
      onEstimate();

    if (bo == m_buttons2[4]) {
      onQueryOrder();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH009")/* @res "��ѯ���" */);
    }

    if (bo == m_buttons2[5]) {
      onReturn();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "4004COMMON000000053")/* @res "���سɹ�" */);
    }
//    zhf �޶�
    if(bo  == m_buttons1[16]){
    	onEdit();
    }
  }
  
  private void onEdit(){

	    this.showHintMessage("");
//	    if (isSettled(m_VOs[m_nPresentRecord])) {
//	      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
//	          .getStrByID("40040503", "UPP40040503-000075")/* @res "�޸��ڳ��ݹ���ⵥ" */,
//	          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
//	              "UPP40040503-000076")/* @res "��ⵥ�Ѿ����㣬�����޸ģ�" */);
//	      return;
//	    }

	    getBillCardPanel1().setEnabled(false);
	    setPartNoEditable2_zhf();

//	    // ��ⵥ�Ų��ɱ༭
//	    BillItem item = getBillCardPanel1().getHeadItem("vbillcode");
//	    item.setEnabled(false);

	    m_bAdd = false;
	    // ���水ť״̬
	    for (int i = 0; i < m_nButtonState.length; i++)
	      m_nRestoreState[i] = m_nButtonState[i];

	    // ���ó����棬ɾ��,����,��ӡ�����а�ťΪ��
	    for (int i = 0; i < buttons1_len; i++) {
	      m_nButtonState[i] = 1;
	    }
	    for (int i = 2; i < 5; i++)
	      m_nButtonState[i] = 0;
	    m_nButtonState[12] = 0;
	    m_nButtonState[13] = 0;
	    m_nButtonState[3] = 1;
	    m_nUIState = 1;
	    changeButtonState();
	  
  }
  
  /**
   * 
   * @author zhf
   * @˵�������׸ڿ�ҵ��zhf add for �׸�
   * 2011-6-17����04:30:35
   */
  private void setPartNoEditable2_zhf() {
	    getBillCardPanel1().setEnabled(true);
	    BillItem items[] = getBillCardPanel1().getHeadItems();
	    for (int i = 0; i < items.length; i++) {
	      String sKey = items[i].getKey().trim();
//	      if (sKey.equals("cbiztype") || sKey.equals("cstoreorganization"))
	      for(String name2:InitialPartEditField.head_edit_names){
	    	  if(sKey.equalsIgnoreCase(name2))
	    		  items[i].setEdit(true);
	    	  else
	    		  items[i].setEdit(false);
	      }
	       
	    }
	    // since V53,���������ۡ�������ݹ�ʱȷ�ϣ��������޸ģ�Ҫ�޸���Ҫɾ���ڳ��ݹ���ⵥ�����ݹ�(���ֹ��ݹ�����һ��)
	    items = getBillCardPanel1().getBodyItems();
	    
	    for(BillItem item:items){
	    	 String sKey = item.getKey().trim();
	    	 for(String name:InitialPartEditField.body_edit_names){
	    		 if(sKey.equalsIgnoreCase(name))
	    			 item.setEdit(true);
	    		 else
		    		 item.setEdit(false);
	    	 }	    	
	    }
	    
//	    for (int i = 0; i < items.length; i++) {
//	      String sKey = items[i].getKey().trim();
//	      if (sKey.equals("vfree"))
//	        items[i].setEnabled(items[i].isEdit());
//	      else if (sKey.equals("vbatchcode"))
//	        items[i].setEnabled(items[i].isEdit());
//	      else if (sKey.endsWith("crowno"))
//	        items[i].setEnabled(items[i].isEdit());
//	      else if (sKey.equals("ninnum"))
//	        items[i].setEnabled(false);
//	      else if (sKey.equals("nprice"))
//	        items[i].setEnabled(false);
//	      else if (sKey.equals("nmny"))
//	        items[i].setEnabled(false);
//	      else if (sKey.startsWith("vuserdef"))
//	        items[i].setEnabled(items[i].isEdit());
//	      else
//	        items[i].setEnabled(false);
//	    }
//
//	    // ����Ϊ������۱����ʲ����޸� zhf add
//	    int rows = getBillCardPanel2().getBillModel().getRowCount();
//	    for (int i = 0; i < rows; i++) {
//	      Object curTypeId = getBillCardPanel2()
//	          .getBodyValueAt(i, "currencytypeid");
//	      if (curTypeId == null)
//	        continue;
//
//	      if (curTypeId.toString().equalsIgnoreCase(m_sCurrTypeID))// �жϵ�ǰ�����Ƿ�˾��λ��
//	        // �����۱����ʱ༭����
//	        getBillCardPanel2().getBillModel().setCellEditable(i,
//	            "nexchangeotobrate", false);
//	      else
//	        getBillCardPanel2().getBillModel().setCellEditable(i,
//	            "nexchangeotobrate", true);
//	    }
//	    // zhf end
//
//	    //
//	    UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1()
//	        .getBodyItem("ninnum").getComponent();
//	    UITextField nStockNumUI = (UITextField) nRefPanel1.getUITextField();
//	    // nStockNumUI.setMaxLength(13);
//	    nStockNumUI.setMaxLength(16);
//
//	    UIRefPane nRefPanel2 = (UIRefPane) getBillCardPanel1()
//	        .getBodyItem("nprice").getComponent();
//	    UITextField nPriceUI = (UITextField) nRefPanel2.getUITextField();
//	    // nPriceUI.setMaxLength(9);
//	    nPriceUI.setMaxLength(16);
//	    nPriceUI.setDelStr("-");
//
//	    UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel1().getBodyItem("nmny")
//	        .getComponent();
//	    UITextField nMoneyUI = (UITextField) nRefPanel3.getUITextField();
//	    // nMoneyUI.setMaxLength(9);
//	    nMoneyUI.setMaxLength(16);
//
//	    UIRefPane nRefPanel4 = (UIRefPane) getBillCardPanel1().getBodyItem(
//	        "cinventorycode").getComponent();
//	    String sWhere = " upper(bd_invmandoc.sealflag) = 'N' and bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc and bd_invmandoc.pk_corp = '"
//	        + m_sUnitCode + "'";
//	    nRefPanel4.setWhereString(sWhere);
//
//	    UIRefPane nRefPanel5 = (UIRefPane) getBillCardPanel1().getHeadItem("vnote")
//	        .getComponent();
//	    nRefPanel5.setReturnCode(false);
//	    nRefPanel5.setAutoCheck(false);
//
//	    UIRefPane nRefPanel6 = (UIRefPane) getBillCardPanel1().getHeadItem(
//	        "cwarehouseid").getComponent();
//	    nRefPanel6.setWhereString(" bd_stordoc.pk_corp = '" + m_sUnitCode
//	        + "' and upper(gubflag) = 'N'");
//
//	    UIRefPane nRefPanel7 = (UIRefPane) getBillCardPanel1().getHeadItem(
//	        "cproviderid").getComponent();
//	    nRefPanel7
//	        .setWhereString(" bd_cumandoc.pk_corp = '"
//	            + m_sUnitCode
//	            + "' and upper(frozenflag) = 'N' and (custflag = '1' or custflag = '3') and bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc");
//
//	    // ���λ�ô������:������Ƿ����ι���
//	    int nRow = m_VOs[m_nPresentRecord].getBodyVO().length;
//	    InvVO invVO[] = new InvVO[nRow];
//	    for (int i = 0; i < nRow; i++) {
//	      invVO[i] = new InvVO();
//	      invVO[i].setCinventoryid(m_VOs[m_nPresentRecord].getBodyVO()[i]
//	          .getCinventoryid());
//	    }
//	    invVO = m_formula.getQuryInvVOs(invVO, false, false, 2);
//	    for (int i = 0; i < nRow; i++) {
//	      Integer j = invVO[i].getIsFreeItemMgt();
//	      if (j != null) {
//	        if (j.intValue() != 1)
//	          getBillCardPanel1().setCellEditable(i, "vfree", false);
//	        else
//	          getBillCardPanel1().setCellEditable(i, "vfree", true);
//	      }
//	      else {
//	        getBillCardPanel1().setCellEditable(i, "vfree", false);
//	      }
//	      j = invVO[i].getIsLotMgt();
//	      if (j != null) {
//	        if (j.intValue() != 1)
//	          getBillCardPanel1().setCellEditable(i, "vbatchcode", false);
//	        else
//	          getBillCardPanel1().setCellEditable(i, "vbatchcode", true);
//	      }
//	      else {
//	        getBillCardPanel1().setCellEditable(i, "vbatchcode", false);
//	      }
//	    }
	    //
	  }

  /**
   * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����: ����:
   */
  public boolean beforeEdit(BillEditEvent event) {
    int row=event.getRow();
    String key=event.getKey();
    //������ݹ�ά������
    if (getBillCardPanel2().isShowing()) {
       if("nexchangeotobrate".equalsIgnoreCase(key)){
          // �������!!!!!!!!
          getBillCardPanel2().stopEditing();
          // �õ�����
          String sCurrId = (String) getBillCardPanel2().getBodyValueAt(row,
                  "currencytypeid");
          getBillCardPanel2().getBodyItem("nexchangeotobrate")
                  .setDecimalDigits(getDigits_ExchangeRate(sCurrId)[0]);
        }
       
       return true;
    }
   
    //�������ⵥά������
    if (event.getPos() == BillItem.BODY) {

      if ("vbatchcode".equalsIgnoreCase(key)) {
        beforeEditWhenBodyBatch(getBillCardPanel1(), m_sUnitCode, event,
            new String[] {
                "cinventoryid", "cinventorycode", "cinventoryname",
                "cinventoryspec", "cinventorytype", "cinventoryunit", null,
                null, "cwarehouseid"
            }, "vfree");
      }
      else if ("vfree".equalsIgnoreCase(key)) {
        // �û�ģ�嶨����޸�����µĴ���
        return PuTool.beforeEditInvBillBodyFree(getBillCardPanel1(), event,
            new String[] {
                "cinventoryid", "cinventorycode", "cinventoryname",
                "cinventoryspec", "cinventorytype"
            }, new String[] {
                "vfree", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5"
            });
        // // �����������Զ������
        // InvVO invVO = new InvVO();
        // String sBaseId = (String) getBillCardPanel1().getBodyValueAt(
        // event.getRow(), "cbaseid");
        // if (sBaseId == null)
        // return true;
        //
        // // ��ô�����룬������ƣ������񣬴���ͺż��������ID
        // invVO.setCinventorycode((String) getBillCardPanel1()
        // .getBodyValueAt(event.getRow(), "cinventorycode"));
        // invVO.setInvname((String) getBillCardPanel1().getBodyValueAt(
        // event.getRow(), "cinventoryname"));
        // invVO.setInvspec((String) getBillCardPanel1().getBodyValueAt(
        // event.getRow(), "cinventoryspec"));
        // invVO.setInvtype((String) getBillCardPanel1().getBodyValueAt(
        // event.getRow(), "cinventorytype"));
        // invVO.setCinventoryid((String) getBillCardPanel1()
        // .getBodyValueAt(event.getRow(), "cbaseid"));
        // try {
        // FreeVO freeVO = nc.ui.pr.pray.PraybillHelper
        // .queryVOForFreeItem(invVO.getCinventoryid());
        // invVO.setFreeItemVO(freeVO);
        // } catch (Exception e) {
        // SCMEnv.out(e);
        // MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
        // .getInstance().getStrByID("40040503",
        // "UPP40040503-000047")/*
        // * @res "�����������Զ������"
        // */, e.getMessage());
        // }
        //
        // invVO.setIsFreeItemMgt(new Integer(1));
        // m_freeItem.setFreeItemParam(invVO);
      }
    }
    return true;
  }

  /**
   * �˴����뷽��˵���� ��������: �������: BillCardPanle panle ����ģ�� String pk_corp ��˾����
   * BillEditEvent e �¼� String[] invFieldNames
   * �����Ϣ�ֶ����ƣ��������ID/����/����/���/�ͺ�/��������λ����/��������λID/�Ƿ񸨼������� + �ֿ��ֶ����ƣ��ֿ�ID�� String
   * freePrefix ��������Ϣ�ֶ�ǰ׺ ����ֵ: �쳣����: ����:
   */
  private void beforeEditWhenBodyBatch(BillCardPanel panel, String pk_corp,
      BillEditEvent event, String[] invFieldNames, String freePrefix) {

    // �Ϸ����ж�
    if (panel == null || pk_corp == null || pk_corp.length() <= 0
        || invFieldNames == null || invFieldNames.length != 9
        || freePrefix == null || freePrefix.length() <= 0) {
      SCMEnv.out("���������������");
    }

    panel.stopEditing();
    int row = event.getRow();
    Object omangid = panel.getBodyValueAt(row, invFieldNames[0]);
    if (omangid == null || omangid.toString().length() <= 0)
      return;
    String cmangid = omangid.toString();

    InvVO invVO = new InvVO();

    // ��֯�ֿ���Ϣ
    Object owhid = panel.getHeadItem(invFieldNames[8]).getValue();
    WhVO whvo = new WhVO();
    if (owhid != null && owhid.toString().length() > 0) {
      String cwhid = owhid.toString();
      UIRefPane refPane = new UIRefPane();
      refPane.setRefNodeName("�ֿ⵵��");
      refPane.setValue(cwhid);
      refPane.setPK(cwhid);
      // refPane.getRefModel().reloadData();
      whvo.setCwarehouseid(cwhid);
      whvo.setCwarehousecode(refPane.getRefCode());
      whvo.setCwarehousename(refPane.getRefName());
      whvo.setIsWasteWh(new Integer(0));
      whvo.setPk_corp(pk_corp);
    }

    // ��֯�����Ϣ
    ArrayList listI = new ArrayList();
    listI.add(cmangid);
    ArrayList retList = null;
    try {
      ArrayList allList = new ArrayList();
      allList.add(listI);
      retList = DefHelper.queryFreeVOByInvIDsGroupByBills(allList);
    }
    catch (Exception exception) {
      SCMEnv.out(exception);
    }

    if (retList != null && retList.size() > 0) {
      ArrayList freeList = (ArrayList) (retList.get(0));
      FreeVO freeVO = (FreeVO) freeList.get(0);

      // �Ƿ����������
      if (freeVO == null
          || ((freeVO.getVfreename1() == null || freeVO.getVfreename1()
              .length() < 1)
              && (freeVO.getVfreename2() == null || freeVO.getVfreename2()
                  .length() < 1)
              && (freeVO.getVfreename3() == null || freeVO.getVfreename3()
                  .length() < 1)
              && (freeVO.getVfreename4() == null || freeVO.getVfreename4()
                  .length() < 1) && (freeVO.getVfreename5() == null || freeVO
              .getVfreename5().length() < 1))) {

        // �����������
        invVO.setIsFreeItemMgt(new Integer(0));
      }
      else {
        // ���������
        invVO.setIsFreeItemMgt(new Integer(1));
        // ������
        freeVO.setCinventoryid(cmangid);
        for (int i = 1; i < 6; i++) {
          Object oTemp = panel.getBodyValueAt(row, freePrefix + i);
          freeVO.setAttributeValue("vfree" + i, oTemp == null ? null : oTemp
              .toString());
        }
        // ����FreeVO
        invVO.setFreeItemVO(freeVO);

      }
    }
    else {
      // �����������
      invVO.setIsFreeItemMgt(new Integer(0));
    }
    // �������ID
    invVO.setCinventoryid(cmangid);

    // �������
    Object strTemp = panel.getBodyValueAt(row, invFieldNames[1]);
    invVO.setCinventorycode(strTemp == null ? null : strTemp.toString());
    // �������
    strTemp = panel.getBodyValueAt(row, invFieldNames[2]);
    invVO.setInvname(strTemp == null ? null : strTemp.toString());
    // ���
    strTemp = panel.getBodyValueAt(row, invFieldNames[3]);
    invVO.setInvspec(strTemp == null ? null : strTemp.toString());
    // �ͺ�
    strTemp = panel.getBodyValueAt(row, invFieldNames[4]);
    invVO.setInvtype(strTemp == null ? null : strTemp.toString());
    // ��������λ����
    strTemp = panel.getBodyValueAt(row, invFieldNames[5]);
    invVO.setMeasdocname(strTemp == null ? null : strTemp.toString());
    // �Ƿ񸨼�������
    // strTemp = panel.getBodyValueAt(row, invFieldNames[7]);
    // if(strTemp != null){
    // strTemp = (strTemp.toString().equalsIgnoreCase("Y") ? new Integer(1):
    // new Integer(0));
    // }
    // else strTemp = new Integer(0);
    // invVO.setIsAstUOMmgt((Integer)strTemp);
    // ��������λID
    // strTemp = panel.getBodyValueAt(row, invFieldNames[6]);
    // invVO.setCastunitid(strTemp == null ? null : strTemp.toString()) ;

    // nc.ui.ic.pub.lot.LotNumbRefPane refPane =
    // (nc.ui.ic.pub.lot.LotNumbRefPane)panel.getBodyItem("vbatchcode").getComponent();
    IICPub_LotNumbRefPane refPane = (IICPub_LotNumbRefPane) panel.getBodyItem(
        "vbatchcode").getComponent();
    refPane.setParameter(whvo, invVO);

  }

  /**
   * �˴����뷽��˵���� ��������:������κ��Ƿ����� �������: ����ֵ: �쳣����:
   * 
   * @return boolean
   * @param VO
   *          nc.vo.pr.pray.PraybillVO
   */
  private String checkBatchCode() {
    String sErr = "";
    int nRow = getBillCardPanel1().getRowCount();
    int nCol = getBillCardPanel1().getBodyColByKey("vbatchcode");

    for (int i = 0; i < nRow; i++) {
      if (getBillCardPanel1().getBillModel().isCellEditable(i, nCol)) {
        Object oTemp = getBillCardPanel1().getBodyValueAt(i, "vbatchcode");
        if (oTemp == null || oTemp.toString().trim().length() == 0) {
          String[] value = new String[] {
            String.valueOf(i + 1)
          };
          sErr += nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000048", null, value)/*
                                                 * @res "��" +
                                                 * CommonConstant.BEGIN_MARK +
                                                 * (i + 1) +
                                                 * CommonConstant.END_MARK +
                                                 * "δ¼�����κţ�\n"
                                                 */;
        }
      }
    }

    return sErr;
  }

  /**
   * �˴����뷽��˵���� ��������:�����ⵥ���Ƿ��ظ� �������: ����ֵ: �쳣����:
   * 
   * @return boolean
   * @param VO
   *          nc.vo.pr.pray.PraybillVO
   *
   *@modified by zhaoyha at 2009.9 �ƶ�����̨���
   *@deprecated
   */
  private boolean checkBeforeSave(GeneralHVO VO, boolean bAdd) {
    boolean b = false;

    try {
      if (bAdd)
        b = EstimateHelper.isCodeDuplicate(m_sUnitCode, VO.getHeadVO()
            .getVbillcode(), null);
      else
        b = EstimateHelper.isCodeDuplicate(m_sUnitCode, VO.getHeadVO()
            .getVbillcode(), m_VOs[m_nPresentRecord].getHeadVO()
            .getCgeneralhid().trim());
    }
    catch (Exception e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000049")/* @res "�����ⵥ���Ƿ��ظ�" */, e.toString());
      SCMEnv.out(e);
      return true;
    }

    return b;
  }

  /**
   * �˴����뷽��˵���� ��������:����������Ƿ����� �������: ����ֵ: �쳣����:
   * 
   * @return boolean
   * @param VO
   *          nc.vo.pr.pray.PraybillVO
   */
  private String checkFreeItem() {
    String sErr = "";
    int nRow = getBillCardPanel1().getRowCount();
    int nCol = getBillCardPanel1().getBodyColByKey("vfree");

    for (int i = 0; i < nRow; i++) {
      if (getBillCardPanel1().getBillModel().isCellEditable(i, nCol)) {
        Object oTemp = getBillCardPanel1().getBodyValueAt(i, "vfree");
        if (oTemp == null || oTemp.toString().trim().length() == 0) {
          String[] value = new String[] {
            String.valueOf(i + 1)
          };
          sErr += nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000050", null, value)/*
                                                 * @res "��" +
                                                 * CommonConstant.BEGIN_MARK +
                                                 * (i + 1) +
                                                 * CommonConstant.END_MARK +
                                                 * "δ¼�������\n"
                                                 */;
        }
      }
    }

    return sErr;
  }

  /**
   * �˴����뷽��˵���� ��������:��Ƭ���棬�޸�����ʱ,����Զ��仯;�޸ĵ���ʱ������Զ��仯���޸Ľ��ʱ�������Զ��仯 �������: ����ֵ: �쳣����:
   */
  private void computeBBodyData(BillEditEvent event) {
    // ��Ҫ����ı�����
    int n = event.getRow();

    UFDouble nInNum = null;
    Object oTemp = getBillCardPanel1().getBodyValueAt(n, "ninnum");
    if (oTemp != null && oTemp.toString().length() > 0)
      nInNum = (UFDouble) oTemp;
    UFDouble nPrice = null;
    oTemp = getBillCardPanel1().getBodyValueAt(n, "nprice");
    if (oTemp != null && oTemp.toString().length() > 0)
      nPrice = (UFDouble) oTemp;
    UFDouble nMny = null;
    oTemp = getBillCardPanel1().getBodyValueAt(n, "nmny");
    if (oTemp != null && oTemp.toString().length() > 0)
      nMny = (UFDouble) oTemp;

    if (event.getKey().trim().equals("ninnum")) {
      // �����仯������Զ��仯
      if (nInNum != null) {
        UFDouble ninnum = m_VOs[m_nPresentRecord].getBodyVO()[n].getNinnum();
        if (ninnum != null && nInNum.doubleValue() * ninnum.doubleValue() < 0.0) {
          MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000051")/*
                                                             * @res "�޸�����"
                                                             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000052")/*
                                     * @res "�޸��������ܸı���ţ�"
                                     */);
          getBillCardPanel1().setBodyValueAt(ninnum, n, "ninnum");
          return;
        }
        if (nInNum.doubleValue() == 0.0) {
          MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000051")/*
                                                             * @res "�޸�����"
                                                             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000053")/*
                                     * @res "�ݹ���������Ϊ�㣡"
                                     */);
          getBillCardPanel1().setBodyValueAt(ninnum, n, "ninnum");
          return;
        }

        if (nPrice != null && nPrice.doubleValue() != 0.0) {
          double d = nPrice.doubleValue() * nInNum.doubleValue();
          getBillCardPanel1().setBodyValueAt(new UFDouble(d), n, "nmny");
        }
      }
      else
        getBillCardPanel1().setBodyValueAt(null, n, "nmny");

    }
    else if (event.getKey().trim().equals("nprice")) {
      // ���۱仯������Զ��仯
      if (nPrice != null) {
        if (nInNum != null) {
          double d = nInNum.doubleValue() * nPrice.doubleValue();
          getBillCardPanel1().setBodyValueAt(new UFDouble(d), n, "nmny");
        }
      }
      else
        getBillCardPanel1().setBodyValueAt(null, n, "nmny");

    }
    else if (event.getKey().trim().equals("nmny")) {
      // ���仯�������Զ��仯
      if (nMny != null) {
        UFDouble nmny = m_VOs[m_nPresentRecord].getBodyVO()[n].getNmny();
        if (nmny != null && nInNum != null
            && nMny.doubleValue() * nInNum.doubleValue() < 0.0) {
          MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000022")/*
                                                             * @res "�޸Ľ��"
                                                             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000023")/*
                                     * @res "�޸Ľ��ܸı���ţ�"
                                     */);
          getBillCardPanel1().setBodyValueAt(nmny, n, "nmny");
          return;
        }

        if (nInNum != null && nInNum.doubleValue() != 0.0) {
          double d = nMny.doubleValue() / nInNum.doubleValue();
          getBillCardPanel1().setBodyValueAt(new UFDouble(d), n, "nprice");
        }
      }
      else
        getBillCardPanel1().setBodyValueAt(null, n, "nprice");
    }
  }

  /**
   * �˴����뷽��˵���� ��������:������ѯ���棬�޸��ݹ�����ʱ���ݹ�����Զ��仯�� �޸��ݹ�����ʱ���ݹ�����Զ��仯���޸��ݹ����ݹ������Զ��仯
   * �������: ����ֵ: �쳣����:
   */
  private void computeBodyData(BillEditEvent event) {
    // ��Ҫ����ı�����
    int n = event.getRow();

    UFDouble nGaugeNum = null;
    Object oTemp = getBillCardPanel2().getBodyValueAt(n, "ngaugenum");
    if (oTemp != null && oTemp.toString().length() > 0)
      nGaugeNum = (UFDouble) oTemp;
    UFDouble nGaugePrice = null;
    oTemp = getBillCardPanel2().getBodyValueAt(n, "nprice");
    if (oTemp != null && oTemp.toString().length() > 0)
      nGaugePrice = (UFDouble) oTemp;
    UFDouble nGaugeMny = null;
    oTemp = getBillCardPanel2().getBodyValueAt(n, "nmoney");
    if (oTemp != null && oTemp.toString().length() > 0)
      nGaugeMny = (UFDouble) oTemp;

    if (event.getKey().trim().equals("ngaugenum")) {
      // �ݹ������仯������Զ��仯
      if (nGaugeNum != null && nGaugeNum.toString().length() > 0) {
        if (m_orderVOs[n].getNgaugenum() != null
            && nGaugeNum.doubleValue()
                * m_orderVOs[n].getNgaugenum().doubleValue() < 0.0) {
          MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000054")/*
                                                             * @res "�޸��ݹ�����"
                                                             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000055")/*
                                     * @res "�޸��ݹ��������ܸı���ţ�"
                                     */);
          getBillCardPanel2().setBodyValueAt(m_orderVOs[n].getNgaugenum(), n,
              "ngaugenum");
          return;
        }
        if (nGaugeNum.doubleValue() == 0.0) {
          MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000054")/*
                                                             * @res "�޸��ݹ�����"
                                                             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000056")/*
                                     * @res "�޸��ݹ��������ܵ����㣡"
                                     */);
          getBillCardPanel2().setBodyValueAt(m_orderVOs[n].getNgaugenum(), n,
              "ngaugenum");
          return;
        }
        if (nGaugePrice != null) {
          double d = nGaugeNum.doubleValue() * nGaugePrice.doubleValue();
          getBillCardPanel2().setBodyValueAt(new UFDouble(d), n, "nmoney");
        }
      }
      else
        getBillCardPanel2().setBodyValueAt(null, n, "nmoney");

      m_orderVOs[n].setNgaugenum(nGaugeNum);
      m_orderVOs[n].setNmoney((UFDouble) getBillCardPanel2().getBodyValueAt(n,
          "nmoney"));
    }
    else if (event.getKey().trim().equals("nprice")) {
      // �ݹ����۱仯������Զ��仯
      if (nGaugePrice != null && nGaugePrice.toString().length() > 0) {
        if (nGaugeNum != null) {
          double d = nGaugeNum.doubleValue() * nGaugePrice.doubleValue();
          getBillCardPanel2().setBodyValueAt(new UFDouble(d), n, "nmoney");
        }
      }
      else
        getBillCardPanel2().setBodyValueAt(null, n, "nmoney");

      m_orderVOs[n].setNprice(nGaugePrice);
      m_orderVOs[n].setNmoney((UFDouble) getBillCardPanel2().getBodyValueAt(n,
          "nmoney"));
    }
    else if (event.getKey().trim().equals("nmoney")) {
      // �ݹ����仯�������Զ��仯
      if (nGaugeMny != null && nGaugeMny.toString().length() > 0) {
        if (nGaugeNum != null
            && nGaugeMny.doubleValue() * nGaugeNum.doubleValue() < 0.0) {
          MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000057")/*
                                                             * @res "�޸��ݹ����"
                                                             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000058")/*
                                     * @res "�޸��ݹ����ܸı���ţ�"
                                     */);
          getBillCardPanel2().setBodyValueAt(m_orderVOs[n].getNmoney(), n,
              "nmoney");
          return;
        }
        if (nGaugeNum != null && nGaugeNum.doubleValue() != 0.0) {
          double d = nGaugeMny.doubleValue() / nGaugeNum.doubleValue();
          getBillCardPanel2().setBodyValueAt(new UFDouble(d), n, "nprice");
        }
      }
      else
        getBillCardPanel2().setBodyValueAt(null, n, "nprice");

      m_orderVOs[n].setNmoney(nGaugeMny);
      m_orderVOs[n].setNprice((UFDouble) getBillCardPanel2().getBodyValueAt(n,
          "nprice"));

    }
  }

  /**
   * �˴����뷽��˵���� ��������:���ݶ��������������+��Ӧ��������ⵥ ע�⣺�����֯��ҵ��Ա�����š�ҵ�����͡��ɹ���˾��ϵͳȱʡ�ķֵ�������
   * �������: ����ֵ:��ⵥVO �쳣����:
   */
  private GeneralHVO[] doGenerationByInv(OorderVO tempVOs[], Vector vMixture) {
    if (tempVOs == null || tempVOs.length == 0)
      return null;
    // �Ż�Ч�ʣ��ṩ���β�ѯ�շ���ʽ
    String sRSMode[] = new String[tempVOs.length];
    for (int i = 0; i < tempVOs.length; i++)
      sRSMode[i] = tempVOs[i].getCbiztype();
    try {
      sRSMode = EstimateHelper.getRSModeBatch(sRSMode);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000059")/* @res "����շ���ʽ" */, e
          .getMessage());
      return null;
    }
    //

    Vector vv = new Vector();
    String sTemp = null, s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, s7 = null, sID = null, s8 = null;
    boolean b = false;

    for (int i = 0; i < vMixture.size(); i++) {
      GeneralHHeaderVO headVO = new GeneralHHeaderVO();
      Vector v = new Vector();
      Vector v0 = new Vector();

      sTemp = (String) vMixture.elementAt(i);
      b = false;
      for (int j = 0; j < tempVOs.length; j++) {
        s1 = tempVOs[j].getCmangid();
        s2 = tempVOs[j].getCvendormangid();
        s3 = tempVOs[j].getCstoreorganization();
        s4 = tempVOs[j].getCemployeeid();
        s5 = tempVOs[j].getCdeptid();
        s6 = tempVOs[j].getCbiztype();
        s7 = tempVOs[j].getPk_corp();
        s8 = tempVOs[j].getPk_arrvcorp();
        if (s1 == null || s1.trim().length() == 0)
          s1 = "null";
        if (s2 == null || s2.trim().length() == 0)
          s2 = "null";
        if (s3 == null || s3.trim().length() == 0)
          s3 = "null";
        if (s4 == null || s4.trim().length() == 0)
          s4 = "null";
        if (s5 == null || s5.trim().length() == 0)
          s5 = "null";
        if (s6 == null || s6.trim().length() == 0)
          s6 = "null";
        if (s7 == null || s7.trim().length() == 0)
          s7 = "null";
        if (s8 == null || s8.trim().length() == 0)
          s8 = "null";
        sID = s1 + s2 + s3 + s4 + s5 + s6 + s7 + s8;
        if (sTemp.equals(sID)) {
          if (!b) {
            // ������ⵥ��ͷ
            headVO.setDbilldate(m_dSystem);
            headVO.setCbiztype(tempVOs[j].getCbiztype());
            headVO.setCproviderid(tempVOs[j].getCvendormangid());
            headVO.setPk_cubasdoc(tempVOs[j].getCvendorbaseid());
            headVO.setCdptid(tempVOs[j].getCdeptid());
            headVO.setCbizid(tempVOs[j].getCemployeeid());
            headVO.setPk_corp(tempVOs[j].getPk_arrvcorp());// �ջ���˾
            headVO.setPk_purcorp(tempVOs[j].getPk_corp());// �ɹ���˾
            headVO.setCstoreorganization(tempVOs[j].getPk_arrvstoorg());// �ջ������֯
            headVO.setCwarehouseid(tempVOs[j].getCwarehouseid());
            headVO.setCbilltypecode("4T");
            headVO.setCregister(getClientEnvironment().getUser()
                .getPrimaryKey());
            headVO.setCdispatcherid(sRSMode[j]);
            headVO.setCoperatorid(getClientEnvironment().getUser()
                .getPrimaryKey());

            headVO.setVuserdef1(tempVOs[j].getHvdef1());
            headVO.setVuserdef2(tempVOs[j].getHvdef2());
            headVO.setVuserdef3(tempVOs[j].getHvdef3());
            headVO.setVuserdef4(tempVOs[j].getHvdef4());
            headVO.setVuserdef5(tempVOs[j].getHvdef5());
            headVO.setVuserdef6(tempVOs[j].getHvdef6());
            headVO.setVuserdef7(tempVOs[j].getHvdef7());
            headVO.setVuserdef8(tempVOs[j].getHvdef8());
            headVO.setVuserdef9(tempVOs[j].getHvdef9());
            headVO.setVuserdef10(tempVOs[j].getHvdef10());
            headVO.setVuserdef11(tempVOs[j].getHvdef11());
            headVO.setVuserdef12(tempVOs[j].getHvdef12());
            headVO.setVuserdef13(tempVOs[j].getHvdef13());
            headVO.setVuserdef14(tempVOs[j].getHvdef14());
            headVO.setVuserdef15(tempVOs[j].getHvdef15());
            headVO.setVuserdef16(tempVOs[j].getHvdef16());
            headVO.setVuserdef17(tempVOs[j].getHvdef17());
            headVO.setVuserdef18(tempVOs[j].getHvdef18());
            headVO.setVuserdef19(tempVOs[j].getHvdef19());
            headVO.setVuserdef20(tempVOs[j].getHvdef20());

            headVO.setPk_defdoc1(tempVOs[j].getHpkdef1());
            headVO.setPk_defdoc2(tempVOs[j].getHpkdef2());
            headVO.setPk_defdoc3(tempVOs[j].getHpkdef3());
            headVO.setPk_defdoc4(tempVOs[j].getHpkdef4());
            headVO.setPk_defdoc5(tempVOs[j].getHpkdef5());
            headVO.setPk_defdoc6(tempVOs[j].getHpkdef6());
            headVO.setPk_defdoc7(tempVOs[j].getHpkdef7());
            headVO.setPk_defdoc8(tempVOs[j].getHpkdef8());
            headVO.setPk_defdoc9(tempVOs[j].getHpkdef9());
            headVO.setPk_defdoc10(tempVOs[j].getHpkdef10());
            headVO.setPk_defdoc11(tempVOs[j].getHpkdef11());
            headVO.setPk_defdoc12(tempVOs[j].getHpkdef12());
            headVO.setPk_defdoc13(tempVOs[j].getHpkdef13());
            headVO.setPk_defdoc14(tempVOs[j].getHpkdef14());
            headVO.setPk_defdoc15(tempVOs[j].getHpkdef15());
            headVO.setPk_defdoc16(tempVOs[j].getHpkdef16());
            headVO.setPk_defdoc17(tempVOs[j].getHpkdef17());
            headVO.setPk_defdoc18(tempVOs[j].getHpkdef18());
            headVO.setPk_defdoc19(tempVOs[j].getHpkdef19());
            headVO.setPk_defdoc20(tempVOs[j].getHpkdef20());
            headVO.setVnote(tempVOs[j].getVmemoHead());
          }

          // ������ⵥ����
          GeneralHItemVO bodyVO = new GeneralHItemVO();
          // since v502
          bodyVO.setBLargess(tempVOs[j].getBLargess());
          //
          bodyVO.setCsourcebillhid(tempVOs[j].getCorderid());
          bodyVO.setCsourcebillbid(tempVOs[j].getCorder_bid());
          bodyVO.setVsourcebillcode(tempVOs[j].getVordercode());
          bodyVO.setCsourcetype(ScmConst.PO_Order);
          bodyVO.setBzgflag(new UFBoolean(true));
          bodyVO.setIsok(new UFBoolean(false));
          bodyVO.setCinventoryid(tempVOs[j].getCmangid());
          bodyVO.setNinnum(tempVOs[j].getNgaugenum());
          bodyVO.setNprice(tempVOs[j].getNprice());
          bodyVO.setNmny(tempVOs[j].getNmoney());

          bodyVO.setVfree1(tempVOs[j].getVfree1());
          bodyVO.setVfree2(tempVOs[j].getVfree2());
          bodyVO.setVfree3(tempVOs[j].getVfree3());
          bodyVO.setVfree4(tempVOs[j].getVfree4());
          bodyVO.setVfree5(tempVOs[j].getVfree5());

          bodyVO.setCprojectid(tempVOs[j].getCprojectid());
          bodyVO.setCprojectphaseid(tempVOs[j].getCprojectphaseid());

          bodyVO.setCfirstbillhid(tempVOs[j].getCorderid());
          bodyVO.setCfirstbillbid(tempVOs[j].getCorder_bid());
          bodyVO.setCfirsttype(ScmConst.PO_Order);
          // ���κ�
          bodyVO.setVbatchcode(tempVOs[j].getVbatchcode());
          bodyVO.setFlargess(new UFBoolean(false));
          // �����ƻ���ID
          bodyVO.setCorder_bb1id(tempVOs[j].getCorder_bb1id());
          bodyVO.setVuserdef1(tempVOs[j].getBvdef1());
          bodyVO.setVuserdef2(tempVOs[j].getBvdef2());
          bodyVO.setVuserdef3(tempVOs[j].getBvdef3());
          bodyVO.setVuserdef4(tempVOs[j].getBvdef4());
          bodyVO.setVuserdef5(tempVOs[j].getBvdef5());
          bodyVO.setVuserdef6(tempVOs[j].getBvdef6());
          bodyVO.setVuserdef7(tempVOs[j].getBvdef7());
          bodyVO.setVuserdef8(tempVOs[j].getBvdef8());
          bodyVO.setVuserdef9(tempVOs[j].getBvdef9());
          bodyVO.setVuserdef10(tempVOs[j].getBvdef10());
          bodyVO.setVuserdef11(tempVOs[j].getBvdef11());
          bodyVO.setVuserdef12(tempVOs[j].getBvdef12());
          bodyVO.setVuserdef13(tempVOs[j].getBvdef13());
          bodyVO.setVuserdef14(tempVOs[j].getBvdef14());
          bodyVO.setVuserdef15(tempVOs[j].getBvdef15());
          bodyVO.setVuserdef16(tempVOs[j].getBvdef16());
          bodyVO.setVuserdef17(tempVOs[j].getBvdef17());
          bodyVO.setVuserdef18(tempVOs[j].getBvdef18());
          bodyVO.setVuserdef19(tempVOs[j].getBvdef19());
          bodyVO.setVuserdef20(tempVOs[j].getBvdef20());

          bodyVO.setPk_defdoc1(tempVOs[j].getBpkdef1());
          bodyVO.setPk_defdoc2(tempVOs[j].getBpkdef2());
          bodyVO.setPk_defdoc3(tempVOs[j].getBpkdef3());
          bodyVO.setPk_defdoc4(tempVOs[j].getBpkdef4());
          bodyVO.setPk_defdoc5(tempVOs[j].getBpkdef5());
          bodyVO.setPk_defdoc6(tempVOs[j].getBpkdef6());
          bodyVO.setPk_defdoc7(tempVOs[j].getBpkdef7());
          bodyVO.setPk_defdoc8(tempVOs[j].getBpkdef8());
          bodyVO.setPk_defdoc9(tempVOs[j].getBpkdef9());
          bodyVO.setPk_defdoc10(tempVOs[j].getBpkdef10());
          bodyVO.setPk_defdoc11(tempVOs[j].getBpkdef11());
          bodyVO.setPk_defdoc12(tempVOs[j].getBpkdef12());
          bodyVO.setPk_defdoc13(tempVOs[j].getBpkdef13());
          bodyVO.setPk_defdoc14(tempVOs[j].getBpkdef14());
          bodyVO.setPk_defdoc15(tempVOs[j].getBpkdef15());
          bodyVO.setPk_defdoc16(tempVOs[j].getBpkdef16());
          bodyVO.setPk_defdoc17(tempVOs[j].getBpkdef17());
          bodyVO.setPk_defdoc18(tempVOs[j].getBpkdef18());
          bodyVO.setPk_defdoc19(tempVOs[j].getBpkdef19());
          bodyVO.setPk_defdoc20(tempVOs[j].getBpkdef20());
          bodyVO.setVnote(tempVOs[j].getVmemoBody());

          // �ݹ�����
          // bodyVO.setDzgdate(m_dSystem);
          // bodyVO.setDbizdate(m_dSystem);

          bodyVO.setDzgdate(headVO.getDbilldate());
          bodyVO.setDbizdate(headVO.getDbilldate());

          //
          bodyVO.setPk_reqcorp(tempVOs[j].getPk_reqcorp());// ����˾
          bodyVO.setPk_invoicecorp(tempVOs[j].getPk_invoicecorp());// ��Ʊ��˾
          bodyVO.setPk_reqstoorg(tempVOs[j].getPk_reqstoorg());// ��������֯
          bodyVO.setPk_creqwareid(tempVOs[j].getPk_creqwareid());// ����ֿ�
          bodyVO.setPk_corp(headVO.getPk_corp());

          bodyVO.setCbaseid(tempVOs[j].getCbaseid());
          bodyVO.setCvendorid(tempVOs[j].getCvendormangid());

          // ������ⵥ���ӱ�
          GeneralBb3VO bb3VO = new GeneralBb3VO();
          bb3VO.setNaccountnum1(new UFDouble(0.0));
          bb3VO.setNaccountmny(new UFDouble(0.0));
          bb3VO.setNpprice(tempVOs[j].getNprice());
          bb3VO.setNpmoney(tempVOs[j].getNmoney());

          b = true;
          v.addElement(bodyVO);
          v0.addElement(bb3VO);
        }
      }

      // ������ⵥ
      if (v.size() > 0) {
        GeneralHItemVO bodyVOs[] = new GeneralHItemVO[v.size()];
        v.copyInto(bodyVOs);

        GeneralBb3VO bb3VOs[] = new GeneralBb3VO[v0.size()];
        v0.copyInto(bb3VOs);

        GeneralHVO vo = new GeneralHVO(v.size());
        vo.setParentVO(headVO);
        vo.setChildrenVO(bodyVOs);
        vo.setGrandVO(bb3VOs);

        vv.addElement(vo);
      }

    }

    // ����
    if (vv.size() > 0) {
      GeneralHVO returnVOs[] = new GeneralHVO[vv.size()];
      vv.copyInto(returnVOs);
      return returnVOs;
    }

    return null;
  }

  /**
   * �˴����뷽��˵���� ��������:���ݶ��������ֿ�ID+������������ⵥ ע�⣺�����֯��ҵ��Ա�����š�ҵ�����͡��ɹ���˾��ϵͳȱʡ�ķֵ�������
   * �������: ����ֵ:��ⵥVO �쳣����:
   */
  private GeneralHVO[] doGenerationByMixture(OorderVO tempVOs[], Vector vMixture) {
    if (tempVOs == null || tempVOs.length == 0)
      return null;
    // �Ż�Ч�ʣ��ṩ���β�ѯ�շ���ʽ
    String sRSMode[] = new String[tempVOs.length];
    for (int i = 0; i < tempVOs.length; i++)
      sRSMode[i] = tempVOs[i].getCbiztype();
    try {
      sRSMode = EstimateHelper.getRSModeBatch(sRSMode);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000059")/* @res "����շ���ʽ" */, e
          .getMessage());
      return null;
    }
    //

    Vector vv = new Vector();
    String sTemp = null, s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, s7 = null, sID = null, s8 = null;
    boolean b = false;

    for (int i = 0; i < vMixture.size(); i++) {
      GeneralHHeaderVO headVO = new GeneralHHeaderVO();
      Vector v = new Vector();
      Vector v0 = new Vector();

      sTemp = (String) vMixture.elementAt(i);
      b = false;
      for (int j = 0; j < tempVOs.length; j++) {
        s1 = tempVOs[j].getCwarehouseid();
        s2 = tempVOs[j].getVordercode();
        s3 = tempVOs[j].getCstoreorganization();
        s4 = tempVOs[j].getCemployeeid();
        s5 = tempVOs[j].getCdeptid();
        s6 = tempVOs[j].getCbiztype();
        s7 = tempVOs[j].getPk_corp();
        s8 = tempVOs[j].getPk_arrvcorp();
        if (s1 == null || s1.trim().length() == 0)
          s1 = "null";
        if (s2 == null || s2.trim().length() == 0)
          s2 = "null";
        if (s3 == null || s3.trim().length() == 0)
          s3 = "null";
        if (s4 == null || s4.trim().length() == 0)
          s4 = "null";
        if (s5 == null || s5.trim().length() == 0)
          s5 = "null";
        if (s6 == null || s6.trim().length() == 0)
          s6 = "null";
        if (s7 == null || s7.trim().length() == 0)
          s7 = "null";
        if (s8 == null || s8.trim().length() == 0)
          s8 = "null";
        sID = s1 + s2 + s3 + s4 + s5 + s6 + s7 + s8;
        if (sTemp.equals(sID)) {
          if (!b) {
            // ������ⵥ��ͷ
            headVO.setDbilldate(m_dSystem);
            headVO.setCbiztype(tempVOs[j].getCbiztype());
            headVO.setCproviderid(tempVOs[j].getCvendormangid());
            headVO.setPk_cubasdoc(tempVOs[j].getCvendorbaseid());
            headVO.setCdptid(tempVOs[j].getCdeptid());
            headVO.setCbizid(tempVOs[j].getCemployeeid());
            headVO.setPk_corp(tempVOs[j].getPk_arrvcorp());// �ջ���˾
            headVO.setPk_purcorp(tempVOs[j].getPk_corp());// �ɹ���˾
            headVO.setCstoreorganization(tempVOs[j].getPk_arrvstoorg());// �ջ������֯
            headVO.setCwarehouseid(tempVOs[j].getCwarehouseid());
            headVO.setCbilltypecode("4T");
            headVO.setCregister(getClientEnvironment().getUser()
                .getPrimaryKey());
            headVO.setCdispatcherid(sRSMode[j]);
            headVO.setCoperatorid(getClientEnvironment().getUser()
                .getPrimaryKey());
            
            headVO.setVuserdef1(tempVOs[j].getHvdef1());
            headVO.setVuserdef2(tempVOs[j].getHvdef2());
            headVO.setVuserdef3(tempVOs[j].getHvdef3());
            headVO.setVuserdef4(tempVOs[j].getHvdef4());
            headVO.setVuserdef5(tempVOs[j].getHvdef5());
            headVO.setVuserdef6(tempVOs[j].getHvdef6());
            headVO.setVuserdef7(tempVOs[j].getHvdef7());
            headVO.setVuserdef8(tempVOs[j].getHvdef8());
            headVO.setVuserdef9(tempVOs[j].getHvdef9());
            headVO.setVuserdef10(tempVOs[j].getHvdef10());
            headVO.setVuserdef11(tempVOs[j].getHvdef11());
            headVO.setVuserdef12(tempVOs[j].getHvdef12());
            headVO.setVuserdef13(tempVOs[j].getHvdef13());
            headVO.setVuserdef14(tempVOs[j].getHvdef14());
            headVO.setVuserdef15(tempVOs[j].getHvdef15());
            headVO.setVuserdef16(tempVOs[j].getHvdef16());
            headVO.setVuserdef17(tempVOs[j].getHvdef17());
            headVO.setVuserdef18(tempVOs[j].getHvdef18());
            headVO.setVuserdef19(tempVOs[j].getHvdef19());
            headVO.setVuserdef20(tempVOs[j].getHvdef20());

            headVO.setPk_defdoc1(tempVOs[j].getHpkdef1());
            headVO.setPk_defdoc2(tempVOs[j].getHpkdef2());
            headVO.setPk_defdoc3(tempVOs[j].getHpkdef3());
            headVO.setPk_defdoc4(tempVOs[j].getHpkdef4());
            headVO.setPk_defdoc5(tempVOs[j].getHpkdef5());
            headVO.setPk_defdoc6(tempVOs[j].getHpkdef6());
            headVO.setPk_defdoc7(tempVOs[j].getHpkdef7());
            headVO.setPk_defdoc8(tempVOs[j].getHpkdef8());
            headVO.setPk_defdoc9(tempVOs[j].getHpkdef9());
            headVO.setPk_defdoc10(tempVOs[j].getHpkdef10());
            headVO.setPk_defdoc11(tempVOs[j].getHpkdef11());
            headVO.setPk_defdoc12(tempVOs[j].getHpkdef12());
            headVO.setPk_defdoc13(tempVOs[j].getHpkdef13());
            headVO.setPk_defdoc14(tempVOs[j].getHpkdef14());
            headVO.setPk_defdoc15(tempVOs[j].getHpkdef15());
            headVO.setPk_defdoc16(tempVOs[j].getHpkdef16());
            headVO.setPk_defdoc17(tempVOs[j].getHpkdef17());
            headVO.setPk_defdoc18(tempVOs[j].getHpkdef18());
            headVO.setPk_defdoc19(tempVOs[j].getHpkdef19());
            headVO.setPk_defdoc20(tempVOs[j].getHpkdef20());
            headVO.setVnote(tempVOs[j].getVmemoHead());
          }

          // ������ⵥ����
          GeneralHItemVO bodyVO = new GeneralHItemVO();
          // since v502
          bodyVO.setBLargess(tempVOs[j].getBLargess());
          //
          bodyVO.setCsourcebillhid(tempVOs[j].getCorderid());
          bodyVO.setCsourcebillbid(tempVOs[j].getCorder_bid());
          bodyVO.setVsourcebillcode(tempVOs[j].getVordercode());
          bodyVO.setCsourcetype(ScmConst.PO_Order);
          bodyVO.setBzgflag(new UFBoolean(true));
          bodyVO.setIsok(new UFBoolean(false));
          bodyVO.setCinventoryid(tempVOs[j].getCmangid());
          bodyVO.setNinnum(tempVOs[j].getNgaugenum());
          bodyVO.setNprice(tempVOs[j].getNprice());
          bodyVO.setNmny(tempVOs[j].getNmoney());

          bodyVO.setVfree1(tempVOs[j].getVfree1());
          bodyVO.setVfree2(tempVOs[j].getVfree2());
          bodyVO.setVfree3(tempVOs[j].getVfree3());
          bodyVO.setVfree4(tempVOs[j].getVfree4());
          bodyVO.setVfree5(tempVOs[j].getVfree5());
          bodyVO.setCprojectid(tempVOs[j].getCprojectid());
          bodyVO.setCprojectphaseid(tempVOs[j].getCprojectphaseid());

          bodyVO.setCfirstbillhid(tempVOs[j].getCorderid());
          bodyVO.setCfirstbillbid(tempVOs[j].getCorder_bid());
          bodyVO.setCfirsttype(ScmConst.PO_Order);
          // ���κ�
          bodyVO.setVbatchcode(tempVOs[j].getVbatchcode());
          bodyVO.setFlargess(new UFBoolean(false));
          // �ݹ�����
          // bodyVO.setDzgdate(m_dSystem);
          // bodyVO.setDbizdate(m_dSystem);

          bodyVO.setDzgdate(headVO.getDbilldate());
          bodyVO.setDbizdate(headVO.getDbilldate());

          bodyVO.setPk_reqcorp(tempVOs[j].getPk_reqcorp());// ����˾
          bodyVO.setPk_invoicecorp(tempVOs[j].getPk_invoicecorp());// ��Ʊ��˾
          bodyVO.setPk_reqstoorg(tempVOs[j].getPk_reqstoorg());// ��������֯
          bodyVO.setPk_creqwareid(tempVOs[j].getPk_creqwareid());// ����ֿ�
          bodyVO.setPk_corp(headVO.getPk_corp());

          bodyVO.setCbaseid(tempVOs[j].getCbaseid());
          bodyVO.setCvendorid(tempVOs[j].getCvendormangid());

          // �����ƻ���ID
          bodyVO.setCorder_bb1id(tempVOs[j].getCorder_bb1id());
          bodyVO.setVuserdef1(tempVOs[j].getBvdef1());
          bodyVO.setVuserdef2(tempVOs[j].getBvdef2());
          bodyVO.setVuserdef3(tempVOs[j].getBvdef3());
          bodyVO.setVuserdef4(tempVOs[j].getBvdef4());
          bodyVO.setVuserdef5(tempVOs[j].getBvdef5());
          bodyVO.setVuserdef6(tempVOs[j].getBvdef6());
          bodyVO.setVuserdef7(tempVOs[j].getBvdef7());
          bodyVO.setVuserdef8(tempVOs[j].getBvdef8());
          bodyVO.setVuserdef9(tempVOs[j].getBvdef9());
          bodyVO.setVuserdef10(tempVOs[j].getBvdef10());
          bodyVO.setVuserdef11(tempVOs[j].getBvdef11());
          bodyVO.setVuserdef12(tempVOs[j].getBvdef12());
          bodyVO.setVuserdef13(tempVOs[j].getBvdef13());
          bodyVO.setVuserdef14(tempVOs[j].getBvdef14());
          bodyVO.setVuserdef15(tempVOs[j].getBvdef15());
          bodyVO.setVuserdef16(tempVOs[j].getBvdef16());
          bodyVO.setVuserdef17(tempVOs[j].getBvdef17());
          bodyVO.setVuserdef18(tempVOs[j].getBvdef18());
          bodyVO.setVuserdef19(tempVOs[j].getBvdef19());
          bodyVO.setVuserdef20(tempVOs[j].getBvdef20());

          bodyVO.setPk_defdoc1(tempVOs[j].getBpkdef1());
          bodyVO.setPk_defdoc2(tempVOs[j].getBpkdef2());
          bodyVO.setPk_defdoc3(tempVOs[j].getBpkdef3());
          bodyVO.setPk_defdoc4(tempVOs[j].getBpkdef4());
          bodyVO.setPk_defdoc5(tempVOs[j].getBpkdef5());
          bodyVO.setPk_defdoc6(tempVOs[j].getBpkdef6());
          bodyVO.setPk_defdoc7(tempVOs[j].getBpkdef7());
          bodyVO.setPk_defdoc8(tempVOs[j].getBpkdef8());
          bodyVO.setPk_defdoc9(tempVOs[j].getBpkdef9());
          bodyVO.setPk_defdoc10(tempVOs[j].getBpkdef10());
          bodyVO.setPk_defdoc11(tempVOs[j].getBpkdef11());
          bodyVO.setPk_defdoc12(tempVOs[j].getBpkdef12());
          bodyVO.setPk_defdoc13(tempVOs[j].getBpkdef13());
          bodyVO.setPk_defdoc14(tempVOs[j].getBpkdef14());
          bodyVO.setPk_defdoc15(tempVOs[j].getBpkdef15());
          bodyVO.setPk_defdoc16(tempVOs[j].getBpkdef16());
          bodyVO.setPk_defdoc17(tempVOs[j].getBpkdef17());
          bodyVO.setPk_defdoc18(tempVOs[j].getBpkdef18());
          bodyVO.setPk_defdoc19(tempVOs[j].getBpkdef19());
          bodyVO.setPk_defdoc20(tempVOs[j].getBpkdef20());
          bodyVO.setVnote(tempVOs[j].getVmemoBody());

          // ������ⵥ���ӱ�
          GeneralBb3VO bb3VO = new GeneralBb3VO();
          bb3VO.setNaccountnum1(new UFDouble(0.0));
          bb3VO.setNaccountmny(new UFDouble(0.0));
          bb3VO.setNpprice(tempVOs[j].getNprice());
          bb3VO.setNpmoney(tempVOs[j].getNmoney());

          b = true;
          v.addElement(bodyVO);
          v0.addElement(bb3VO);
        }
      }

      // ������ⵥ
      if (v.size() > 0) {
        GeneralHItemVO bodyVOs[] = new GeneralHItemVO[v.size()];
        v.copyInto(bodyVOs);

        GeneralBb3VO bb3VOs[] = new GeneralBb3VO[v0.size()];
        v0.copyInto(bb3VOs);

        GeneralHVO vo = new GeneralHVO(v.size());
        vo.setParentVO(headVO);
        vo.setChildrenVO(bodyVOs);
        vo.setGrandVO(bb3VOs);

        vv.addElement(vo);
      }

    }

    // ����
    if (vv.size() > 0) {
      GeneralHVO returnVOs[] = new GeneralHVO[vv.size()];
      vv.copyInto(returnVOs);
      return returnVOs;
    }

    return null;
  }

  /**
   * �˴����뷽��˵���� ��������:���ݶ�������������������ⵥ ע�⣺�����֯��ҵ��Ա�����š�ҵ�����͡��ɹ���˾��ϵͳȱʡ�ķֵ������� �������:
   * ����ֵ:��ⵥVO �쳣����:
   */
  private GeneralHVO[] doGenerationByOrder(OorderVO tempVOs[], Vector vMixture) {
    if (tempVOs == null || tempVOs.length == 0)
      return null;
    // �Ż�Ч�ʣ��ṩ���β�ѯ�շ���ʽ
    String sRSMode[] = new String[tempVOs.length];
    for (int i = 0; i < tempVOs.length; i++)
      sRSMode[i] = tempVOs[i].getCbiztype();
    try {
      sRSMode = EstimateHelper.getRSModeBatch(sRSMode);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000059")/* @res "����շ���ʽ" */, e
          .getMessage());
      return null;
    }
    //
    Vector vv = new Vector();
    String sTemp = null, s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, sID = null, s7 = null;
    boolean b = false;

    for (int i = 0; i < vMixture.size(); i++) {
      GeneralHHeaderVO headVO = new GeneralHHeaderVO();
      Vector v = new Vector();
      Vector v0 = new Vector();

      sTemp = (String) vMixture.elementAt(i);
      b = false;
      for (int j = 0; j < tempVOs.length; j++) {
        s1 = tempVOs[j].getVordercode();
        s2 = tempVOs[j].getCstoreorganization();
        s3 = tempVOs[j].getCemployeeid();
        s4 = tempVOs[j].getCdeptid();
        s5 = tempVOs[j].getCbiztype();
        s6 = tempVOs[j].getPk_corp();
        s7 = tempVOs[j].getPk_arrvcorp();
        if (s1 == null || s1.trim().length() == 0)
          s1 = "null";
        if (s2 == null || s2.trim().length() == 0)
          s2 = "null";
        if (s3 == null || s3.trim().length() == 0)
          s3 = "null";
        if (s4 == null || s4.trim().length() == 0)
          s4 = "null";
        if (s5 == null || s5.trim().length() == 0)
          s5 = "null";
        if (s6 == null || s6.trim().length() == 0)
          s6 = "null";
        if (s7 == null || s7.trim().length() == 0)
          s7 = "null";
        sID = s1 + s2 + s3 + s4 + s5 + s6 + s7;
        if (sTemp.equals(sID)) {
          if (!b) {
            // ������ⵥ��ͷ
            headVO.setDbilldate(m_dSystem);
            headVO.setCbiztype(tempVOs[j].getCbiztype());
            headVO.setCproviderid(tempVOs[j].getCvendormangid());
            headVO.setPk_cubasdoc(tempVOs[j].getCvendorbaseid());
            headVO.setCdptid(tempVOs[j].getCdeptid());
            headVO.setCbizid(tempVOs[j].getCemployeeid());
            headVO.setPk_corp(tempVOs[j].getPk_arrvcorp());// �ջ���˾
            headVO.setPk_purcorp(tempVOs[j].getPk_corp());// �ɹ���˾
            headVO.setCstoreorganization(tempVOs[j].getPk_arrvstoorg());// �ջ������֯
            headVO.setCwarehouseid(tempVOs[j].getCwarehouseid());
            headVO.setCbilltypecode("4T");
            headVO.setCregister(getClientEnvironment().getUser()
                .getPrimaryKey());
            headVO.setCdispatcherid(sRSMode[j]);
            headVO.setCoperatorid(getClientEnvironment().getUser()
                .getPrimaryKey());

//            headVO.setVuserdef1(tempVOs[j].getHvdef1());
//            headVO.setVuserdef2(tempVOs[j].getHvdef2());
//            headVO.setVuserdef3(tempVOs[j].getHvdef3());
//            headVO.setVuserdef4(tempVOs[j].getHvdef4());
//            headVO.setVuserdef5(tempVOs[j].getHvdef5());
//            headVO.setVuserdef6(tempVOs[j].getHvdef6());
//            headVO.setVuserdef7(tempVOs[j].getHvdef7());
//            headVO.setVuserdef8(tempVOs[j].getHvdef8());
//            headVO.setVuserdef9(tempVOs[j].getHvdef9());
//            headVO.setVuserdef10(tempVOs[j].getHvdef10());
//            headVO.setVuserdef11(tempVOs[j].getHvdef11());
//            headVO.setVuserdef12(tempVOs[j].getHvdef12());
//            headVO.setVuserdef13(tempVOs[j].getHvdef13());
//            headVO.setVuserdef14(tempVOs[j].getHvdef14());
//            headVO.setVuserdef15(tempVOs[j].getHvdef15());
//            headVO.setVuserdef16(tempVOs[j].getHvdef16());
//            headVO.setVuserdef17(tempVOs[j].getHvdef17());
//            headVO.setVuserdef18(tempVOs[j].getHvdef18());
//            headVO.setVuserdef19(tempVOs[j].getHvdef19());
//            headVO.setVuserdef20(tempVOs[j].getHvdef20());
//
//            headVO.setPk_defdoc1(tempVOs[j].getHpkdef1());
//            headVO.setPk_defdoc2(tempVOs[j].getHpkdef2());
//            headVO.setPk_defdoc3(tempVOs[j].getHpkdef3());
//            headVO.setPk_defdoc4(tempVOs[j].getHpkdef4());
//            headVO.setPk_defdoc5(tempVOs[j].getHpkdef5());
//            headVO.setPk_defdoc6(tempVOs[j].getHpkdef6());
//            headVO.setPk_defdoc7(tempVOs[j].getHpkdef7());
//            headVO.setPk_defdoc8(tempVOs[j].getHpkdef8());
//            headVO.setPk_defdoc9(tempVOs[j].getHpkdef9());
//            headVO.setPk_defdoc10(tempVOs[j].getHpkdef10());
//            headVO.setPk_defdoc11(tempVOs[j].getHpkdef11());
//            headVO.setPk_defdoc12(tempVOs[j].getHpkdef12());
//            headVO.setPk_defdoc13(tempVOs[j].getHpkdef13());
//            headVO.setPk_defdoc14(tempVOs[j].getHpkdef14());
//            headVO.setPk_defdoc15(tempVOs[j].getHpkdef15());
//            headVO.setPk_defdoc16(tempVOs[j].getHpkdef16());
//            headVO.setPk_defdoc17(tempVOs[j].getHpkdef17());
//            headVO.setPk_defdoc18(tempVOs[j].getHpkdef18());
//            headVO.setPk_defdoc19(tempVOs[j].getHpkdef19());
//            headVO.setPk_defdoc20(tempVOs[j].getHpkdef20());
            headVO.setVnote(tempVOs[j].getVmemoHead());
          }

          // ������ⵥ����
          GeneralHItemVO bodyVO = new GeneralHItemVO();
          // since v502
          bodyVO.setBLargess(tempVOs[j].getBLargess());
          //
          bodyVO.setCsourcebillhid(tempVOs[j].getCorderid());
          bodyVO.setCsourcebillbid(tempVOs[j].getCorder_bid());
          bodyVO.setVsourcebillcode(tempVOs[j].getVordercode());
          bodyVO.setCsourcetype(ScmConst.PO_Order);
          bodyVO.setBzgflag(new UFBoolean(true));
          bodyVO.setIsok(new UFBoolean(false));
          bodyVO.setCinventoryid(tempVOs[j].getCmangid());
          bodyVO.setNinnum(tempVOs[j].getNgaugenum());
          bodyVO.setNprice(tempVOs[j].getNprice());
          bodyVO.setNmny(tempVOs[j].getNmoney());

          bodyVO.setVfree1(tempVOs[j].getVfree1());
          bodyVO.setVfree2(tempVOs[j].getVfree2());
          bodyVO.setVfree3(tempVOs[j].getVfree3());
          bodyVO.setVfree4(tempVOs[j].getVfree4());
          bodyVO.setVfree5(tempVOs[j].getVfree5());
          bodyVO.setCprojectid(tempVOs[j].getCprojectid());
          bodyVO.setCprojectphaseid(tempVOs[j].getCprojectphaseid());

          bodyVO.setCfirstbillhid(tempVOs[j].getCorderid());
          bodyVO.setCfirstbillbid(tempVOs[j].getCorder_bid());
          bodyVO.setCfirsttype(ScmConst.PO_Order);
          // ���κ�
          bodyVO.setVbatchcode(tempVOs[j].getVbatchcode());
          bodyVO.setFlargess(new UFBoolean(false));
          // �ݹ�����
          // bodyVO.setDzgdate(m_dSystem);
          // bodyVO.setDbizdate(m_dSystem);

          bodyVO.setDzgdate(headVO.getDbilldate());
          bodyVO.setDbizdate(headVO.getDbilldate());

          bodyVO.setPk_reqcorp(tempVOs[j].getPk_reqcorp());// ����˾
          bodyVO.setPk_invoicecorp(tempVOs[j].getPk_invoicecorp());// ��Ʊ��˾
          bodyVO.setPk_reqstoorg(tempVOs[j].getPk_reqstoorg());// ��������֯
          bodyVO.setPk_creqwareid(tempVOs[j].getPk_creqwareid());// ����ֿ�
          bodyVO.setPk_corp(headVO.getPk_corp());

          bodyVO.setCbaseid(tempVOs[j].getCbaseid());
          bodyVO.setCvendorid(tempVOs[j].getCvendormangid());

          // �����ƻ���ID
          bodyVO.setCorder_bb1id(tempVOs[j].getCorder_bb1id());
//          bodyVO.setVuserdef1(tempVOs[j].getBvdef1());
//          bodyVO.setVuserdef2(tempVOs[j].getBvdef2());
//          bodyVO.setVuserdef3(tempVOs[j].getBvdef3());
//          bodyVO.setVuserdef4(tempVOs[j].getBvdef4());
//          bodyVO.setVuserdef5(tempVOs[j].getBvdef5());
//          bodyVO.setVuserdef6(tempVOs[j].getBvdef6());
//          bodyVO.setVuserdef7(tempVOs[j].getBvdef7());
//          bodyVO.setVuserdef8(tempVOs[j].getBvdef8());
//          bodyVO.setVuserdef9(tempVOs[j].getBvdef9());
//          bodyVO.setVuserdef10(tempVOs[j].getBvdef10());
//          bodyVO.setVuserdef11(tempVOs[j].getBvdef11());
//          bodyVO.setVuserdef12(tempVOs[j].getBvdef12());
//          bodyVO.setVuserdef13(tempVOs[j].getBvdef13());
//          bodyVO.setVuserdef14(tempVOs[j].getBvdef14());
//          bodyVO.setVuserdef15(tempVOs[j].getBvdef15());
//          bodyVO.setVuserdef16(tempVOs[j].getBvdef16());
//          bodyVO.setVuserdef17(tempVOs[j].getBvdef17());
//          bodyVO.setVuserdef18(tempVOs[j].getBvdef18());
//          bodyVO.setVuserdef19(tempVOs[j].getBvdef19());
//          bodyVO.setVuserdef20(tempVOs[j].getBvdef20());
//
//          bodyVO.setPk_defdoc1(tempVOs[j].getBpkdef1());
//          bodyVO.setPk_defdoc2(tempVOs[j].getBpkdef2());
//          bodyVO.setPk_defdoc3(tempVOs[j].getBpkdef3());
//          bodyVO.setPk_defdoc4(tempVOs[j].getBpkdef4());
//          bodyVO.setPk_defdoc5(tempVOs[j].getBpkdef5());
//          bodyVO.setPk_defdoc6(tempVOs[j].getBpkdef6());
//          bodyVO.setPk_defdoc7(tempVOs[j].getBpkdef7());
//          bodyVO.setPk_defdoc8(tempVOs[j].getBpkdef8());
//          bodyVO.setPk_defdoc9(tempVOs[j].getBpkdef9());
//          bodyVO.setPk_defdoc10(tempVOs[j].getBpkdef10());
//          bodyVO.setPk_defdoc11(tempVOs[j].getBpkdef11());
//          bodyVO.setPk_defdoc12(tempVOs[j].getBpkdef12());
//          bodyVO.setPk_defdoc13(tempVOs[j].getBpkdef13());
//          bodyVO.setPk_defdoc14(tempVOs[j].getBpkdef14());
//          bodyVO.setPk_defdoc15(tempVOs[j].getBpkdef15());
//          bodyVO.setPk_defdoc16(tempVOs[j].getBpkdef16());
//          bodyVO.setPk_defdoc17(tempVOs[j].getBpkdef17());
//          bodyVO.setPk_defdoc18(tempVOs[j].getBpkdef18());
//          bodyVO.setPk_defdoc19(tempVOs[j].getBpkdef19());
//          bodyVO.setPk_defdoc20(tempVOs[j].getBpkdef20());
          bodyVO.setVnote(tempVOs[j].getVmemoBody());

          // ������ⵥ���ӱ�
          GeneralBb3VO bb3VO = new GeneralBb3VO();
          bb3VO.setNaccountnum1(new UFDouble(0.0));
          bb3VO.setNaccountmny(new UFDouble(0.0));
          bb3VO.setNpprice(tempVOs[j].getNprice());
          bb3VO.setNpmoney(tempVOs[j].getNmoney());

          b = true;
          v.addElement(bodyVO);
          v0.addElement(bb3VO);
        }
      }

      // ������ⵥ
      if (v.size() > 0) {
        GeneralHItemVO bodyVOs[] = new GeneralHItemVO[v.size()];
        v.copyInto(bodyVOs);

        GeneralBb3VO bb3VOs[] = new GeneralBb3VO[v0.size()];
        v0.copyInto(bb3VOs);

        GeneralHVO vo = new GeneralHVO(v.size());
        vo.setParentVO(headVO);
        vo.setChildrenVO(bodyVOs);
        vo.setGrandVO(bb3VOs);

        vv.addElement(vo);
      }

    }

    // ����
    if (vv.size() > 0) {
      GeneralHVO returnVOs[] = new GeneralHVO[vv.size()];
      vv.copyInto(returnVOs);
      return returnVOs;
    }

    return null;
  }

  /**
   * �˴����뷽��˵���� ��������:���ݶ���������Ӧ�̱���������ⵥ ע�⣺�����֯��ҵ��Ա�����š�ҵ�����͡��ɹ���˾��ϵͳȱʡ�ķֵ������� �������:
   * ����ֵ:��ⵥVO �쳣����:
   */
  /**
   * ���ݶ���������Ӧ�̱���������ⵥ
   * <p>
   * ע�⣺�����֯��ҵ��Ա�����š�ҵ�����͡��ɹ���˾��ϵͳȱʡ�ķֵ����� <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * 
   * @param tempVOs
   * @param vMixture
   * @return
   *          <p>
   * @author xhq
   * @modify 2007-8-22 ����11:01:25, by czp, ����Ĭ�Ϸֵ��������ջ���˾
   */
  private GeneralHVO[] doGenerationByVendor(OorderVO tempVOs[], Vector vMixture) {
    if (tempVOs == null || tempVOs.length == 0)
      return null;
    // �Ż�Ч�ʣ��ṩ���β�ѯ�շ���ʽ
    String sRSMode[] = new String[tempVOs.length];
    for (int i = 0; i < tempVOs.length; i++)
      sRSMode[i] = tempVOs[i].getCbiztype();
    try {
      sRSMode = EstimateHelper.getRSModeBatch(sRSMode);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000059")/* @res "����շ���ʽ" */, e
          .getMessage());
      return null;
    }
    //
    Vector vv = new Vector();
    String sTemp = null, s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, sID = null, s7 = null;
    boolean b = false;

    for (int i = 0; i < vMixture.size(); i++) {
      GeneralHHeaderVO headVO = new GeneralHHeaderVO();
      Vector v = new Vector();
      Vector v0 = new Vector();

      sTemp = (String) vMixture.elementAt(i);
      b = false;
      for (int j = 0; j < tempVOs.length; j++) {
        s1 = tempVOs[j].getCvendormangid();
        s2 = tempVOs[j].getCstoreorganization();
        s3 = tempVOs[j].getCemployeeid();
        s4 = tempVOs[j].getCdeptid();
        s5 = tempVOs[j].getCbiztype();
        s6 = tempVOs[j].getPk_corp();
        s7 = tempVOs[j].getPk_arrvcorp();
        if (s1 == null || s1.trim().length() == 0)
          s1 = "null";
        if (s2 == null || s2.trim().length() == 0)
          s2 = "null";
        if (s3 == null || s3.trim().length() == 0)
          s3 = "null";
        if (s4 == null || s4.trim().length() == 0)
          s4 = "null";
        if (s5 == null || s5.trim().length() == 0)
          s5 = "null";
        if (s6 == null || s6.trim().length() == 0)
          s6 = "null";
        if (s7 == null || s7.trim().length() == 0)
          s7 = "null";
        sID = s1 + s2 + s3 + s4 + s5 + s6 + s7;
        if (sTemp.equals(sID)) {
          if (!b) {
            // ������ⵥ��ͷ
            headVO.setDbilldate(m_dSystem);
            headVO.setCbiztype(tempVOs[j].getCbiztype());
            headVO.setCproviderid(tempVOs[j].getCvendormangid());
            headVO.setPk_cubasdoc(tempVOs[j].getCvendorbaseid());
            headVO.setCdptid(tempVOs[j].getCdeptid());
            headVO.setCbizid(tempVOs[j].getCemployeeid());
            headVO.setPk_corp(tempVOs[j].getPk_arrvcorp());// �ջ���˾
            headVO.setPk_purcorp(tempVOs[j].getPk_corp());// �ɹ���˾
            headVO.setCstoreorganization(tempVOs[j].getPk_arrvstoorg());// �ջ������֯
            headVO.setCwarehouseid(tempVOs[j].getCwarehouseid());
            headVO.setCbilltypecode("4T");
            headVO.setCregister(getClientEnvironment().getUser()
                .getPrimaryKey());
            headVO.setCdispatcherid(sRSMode[j]);
            headVO.setCoperatorid(getClientEnvironment().getUser()
                .getPrimaryKey());

//            headVO.setVuserdef1(tempVOs[j].getHvdef1());
//            headVO.setVuserdef2(tempVOs[j].getHvdef2());
//            headVO.setVuserdef3(tempVOs[j].getHvdef3());
//            headVO.setVuserdef4(tempVOs[j].getHvdef4());
//            headVO.setVuserdef5(tempVOs[j].getHvdef5());
//            headVO.setVuserdef6(tempVOs[j].getHvdef6());
//            headVO.setVuserdef7(tempVOs[j].getHvdef7());
//            headVO.setVuserdef8(tempVOs[j].getHvdef8());
//            headVO.setVuserdef9(tempVOs[j].getHvdef9());
//            headVO.setVuserdef10(tempVOs[j].getHvdef10());
//            headVO.setVuserdef11(tempVOs[j].getHvdef11());
//            headVO.setVuserdef12(tempVOs[j].getHvdef12());
//            headVO.setVuserdef13(tempVOs[j].getHvdef13());
//            headVO.setVuserdef14(tempVOs[j].getHvdef14());
//            headVO.setVuserdef15(tempVOs[j].getHvdef15());
//            headVO.setVuserdef16(tempVOs[j].getHvdef16());
//            headVO.setVuserdef17(tempVOs[j].getHvdef17());
//            headVO.setVuserdef18(tempVOs[j].getHvdef18());
//            headVO.setVuserdef19(tempVOs[j].getHvdef19());
//            headVO.setVuserdef20(tempVOs[j].getHvdef20());
//
//            headVO.setPk_defdoc1(tempVOs[j].getHpkdef1());
//            headVO.setPk_defdoc2(tempVOs[j].getHpkdef2());
//            headVO.setPk_defdoc3(tempVOs[j].getHpkdef3());
//            headVO.setPk_defdoc4(tempVOs[j].getHpkdef4());
//            headVO.setPk_defdoc5(tempVOs[j].getHpkdef5());
//            headVO.setPk_defdoc6(tempVOs[j].getHpkdef6());
//            headVO.setPk_defdoc7(tempVOs[j].getHpkdef7());
//            headVO.setPk_defdoc8(tempVOs[j].getHpkdef8());
//            headVO.setPk_defdoc9(tempVOs[j].getHpkdef9());
//            headVO.setPk_defdoc10(tempVOs[j].getHpkdef10());
//            headVO.setPk_defdoc11(tempVOs[j].getHpkdef11());
//            headVO.setPk_defdoc12(tempVOs[j].getHpkdef12());
//            headVO.setPk_defdoc13(tempVOs[j].getHpkdef13());
//            headVO.setPk_defdoc14(tempVOs[j].getHpkdef14());
//            headVO.setPk_defdoc15(tempVOs[j].getHpkdef15());
//            headVO.setPk_defdoc16(tempVOs[j].getHpkdef16());
//            headVO.setPk_defdoc17(tempVOs[j].getHpkdef17());
//            headVO.setPk_defdoc18(tempVOs[j].getHpkdef18());
//            headVO.setPk_defdoc19(tempVOs[j].getHpkdef19());
//            headVO.setPk_defdoc20(tempVOs[j].getHpkdef20());
            headVO.setVnote(tempVOs[j].getVmemoHead());
          }

          // ������ⵥ����
          GeneralHItemVO bodyVO = new GeneralHItemVO();
          // since v502
          bodyVO.setBLargess(tempVOs[j].getBLargess());
          //
          bodyVO.setCsourcebillhid(tempVOs[j].getCorderid());
          bodyVO.setCsourcebillbid(tempVOs[j].getCorder_bid());
          bodyVO.setVsourcebillcode(tempVOs[j].getVordercode());
          bodyVO.setCsourcetype(ScmConst.PO_Order);
          bodyVO.setBzgflag(new UFBoolean(true));
          bodyVO.setIsok(new UFBoolean(false));
          bodyVO.setCinventoryid(tempVOs[j].getCmangid());
          bodyVO.setNinnum(tempVOs[j].getNgaugenum());
          bodyVO.setNprice(tempVOs[j].getNprice());
          bodyVO.setNmny(tempVOs[j].getNmoney());

          bodyVO.setVfree1(tempVOs[j].getVfree1());
          bodyVO.setVfree2(tempVOs[j].getVfree2());
          bodyVO.setVfree3(tempVOs[j].getVfree3());
          bodyVO.setVfree4(tempVOs[j].getVfree4());
          bodyVO.setVfree5(tempVOs[j].getVfree5());
          bodyVO.setCprojectid(tempVOs[j].getCprojectid());
          bodyVO.setCprojectphaseid(tempVOs[j].getCprojectphaseid());

          bodyVO.setCfirstbillhid(tempVOs[j].getCorderid());
          bodyVO.setCfirstbillbid(tempVOs[j].getCorder_bid());
          bodyVO.setCfirsttype(ScmConst.PO_Order);
          // ���κ�
          bodyVO.setVbatchcode(tempVOs[j].getVbatchcode());
          bodyVO.setFlargess(new UFBoolean(false));
          // �ݹ�����
          // bodyVO.setDzgdate(m_dSystem);
          // bodyVO.setDbizdate(m_dSystem);

          bodyVO.setDzgdate(headVO.getDbilldate());
          bodyVO.setDbizdate(headVO.getDbilldate());

          bodyVO.setPk_reqcorp(tempVOs[j].getPk_reqcorp());// ����˾
          bodyVO.setPk_invoicecorp(tempVOs[j].getPk_invoicecorp());// ��Ʊ��˾
          bodyVO.setPk_reqstoorg(tempVOs[j].getPk_reqstoorg());// ��������֯
          bodyVO.setPk_creqwareid(tempVOs[j].getPk_creqwareid());// ����ֿ�
          bodyVO.setPk_corp(headVO.getPk_corp());

          bodyVO.setCbaseid(tempVOs[j].getCbaseid());
          bodyVO.setCvendorid(tempVOs[j].getCvendormangid());

          // �����ƻ���ID
          bodyVO.setCorder_bb1id(tempVOs[j].getCorder_bb1id());
//          bodyVO.setVuserdef1(tempVOs[j].getBvdef1());
//          bodyVO.setVuserdef2(tempVOs[j].getBvdef2());
//          bodyVO.setVuserdef3(tempVOs[j].getBvdef3());
//          bodyVO.setVuserdef4(tempVOs[j].getBvdef4());
//          bodyVO.setVuserdef5(tempVOs[j].getBvdef5());
//          bodyVO.setVuserdef6(tempVOs[j].getBvdef6());
//          bodyVO.setVuserdef7(tempVOs[j].getBvdef7());
//          bodyVO.setVuserdef8(tempVOs[j].getBvdef8());
//          bodyVO.setVuserdef9(tempVOs[j].getBvdef9());
//          bodyVO.setVuserdef10(tempVOs[j].getBvdef10());
//          bodyVO.setVuserdef11(tempVOs[j].getBvdef11());
//          bodyVO.setVuserdef12(tempVOs[j].getBvdef12());
//          bodyVO.setVuserdef13(tempVOs[j].getBvdef13());
//          bodyVO.setVuserdef14(tempVOs[j].getBvdef14());
//          bodyVO.setVuserdef15(tempVOs[j].getBvdef15());
//          bodyVO.setVuserdef16(tempVOs[j].getBvdef16());
//          bodyVO.setVuserdef17(tempVOs[j].getBvdef17());
//          bodyVO.setVuserdef18(tempVOs[j].getBvdef18());
//          bodyVO.setVuserdef19(tempVOs[j].getBvdef19());
//          bodyVO.setVuserdef20(tempVOs[j].getBvdef20());
//
//          bodyVO.setPk_defdoc1(tempVOs[j].getBpkdef1());
//          bodyVO.setPk_defdoc2(tempVOs[j].getBpkdef2());
//          bodyVO.setPk_defdoc3(tempVOs[j].getBpkdef3());
//          bodyVO.setPk_defdoc4(tempVOs[j].getBpkdef4());
//          bodyVO.setPk_defdoc5(tempVOs[j].getBpkdef5());
//          bodyVO.setPk_defdoc6(tempVOs[j].getBpkdef6());
//          bodyVO.setPk_defdoc7(tempVOs[j].getBpkdef7());
//          bodyVO.setPk_defdoc8(tempVOs[j].getBpkdef8());
//          bodyVO.setPk_defdoc9(tempVOs[j].getBpkdef9());
//          bodyVO.setPk_defdoc10(tempVOs[j].getBpkdef10());
//          bodyVO.setPk_defdoc11(tempVOs[j].getBpkdef11());
//          bodyVO.setPk_defdoc12(tempVOs[j].getBpkdef12());
//          bodyVO.setPk_defdoc13(tempVOs[j].getBpkdef13());
//          bodyVO.setPk_defdoc14(tempVOs[j].getBpkdef14());
//          bodyVO.setPk_defdoc15(tempVOs[j].getBpkdef15());
//          bodyVO.setPk_defdoc16(tempVOs[j].getBpkdef16());
//          bodyVO.setPk_defdoc17(tempVOs[j].getBpkdef17());
//          bodyVO.setPk_defdoc18(tempVOs[j].getBpkdef18());
//          bodyVO.setPk_defdoc19(tempVOs[j].getBpkdef19());
//          bodyVO.setPk_defdoc20(tempVOs[j].getBpkdef20());
          bodyVO.setVnote(tempVOs[j].getVmemoBody());

          // ������ⵥ���ӱ�
          GeneralBb3VO bb3VO = new GeneralBb3VO();
          bb3VO.setNaccountnum1(new UFDouble(0.0));
          bb3VO.setNaccountmny(new UFDouble(0.0));
          bb3VO.setNpprice(tempVOs[j].getNprice());
          bb3VO.setNpmoney(tempVOs[j].getNmoney());

          b = true;
          v.addElement(bodyVO);
          v0.addElement(bb3VO);
        }
      }

      // ������ⵥ
      if (v.size() > 0) {
        GeneralHItemVO bodyVOs[] = new GeneralHItemVO[v.size()];
        v.copyInto(bodyVOs);

        GeneralBb3VO bb3VOs[] = new GeneralBb3VO[v0.size()];
        v0.copyInto(bb3VOs);

        GeneralHVO vo = new GeneralHVO(v.size());
        vo.setParentVO(headVO);
        vo.setChildrenVO(bodyVOs);
        vo.setGrandVO(bb3VOs);

        vv.addElement(vo);
      }

    }

    // ����
    if (vv.size() > 0) {
      GeneralHVO returnVOs[] = new GeneralHVO[vv.size()];
      vv.copyInto(returnVOs);
      return returnVOs;
    }

    return null;
  }

  /**
   * �˴����뷽��˵���� ��������:���ݶ��������ֿ�ID+��Ӧ��ID������ⵥ ע�⣺�����֯��ҵ��Ա�����š�ҵ�����͡��ɹ���˾��ϵͳȱʡ�ķֵ�������
   * �������: ����ֵ:��ⵥVO �쳣����:
   */
  private GeneralHVO[] doGenerationByWarehouse(OorderVO tempVOs[],
      Vector vMixture) {
    if (tempVOs == null || tempVOs.length == 0)
      return null;
    // �Ż�Ч�ʣ��ṩ���β�ѯ�շ���ʽ
    String sRSMode[] = new String[tempVOs.length];
    for (int i = 0; i < tempVOs.length; i++)
      sRSMode[i] = tempVOs[i].getCbiztype();
    try {
      sRSMode = EstimateHelper.getRSModeBatch(sRSMode);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000059")/* @res "����շ���ʽ" */, e
          .getMessage());
      return null;
    }
    //
    Vector vv = new Vector();
    String sTemp = null, s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, s7 = null, sID = null, s8;
    boolean b = false;

    for (int i = 0; i < vMixture.size(); i++) {
      GeneralHHeaderVO headVO = new GeneralHHeaderVO();
      Vector v = new Vector();
      Vector v0 = new Vector();

      sTemp = (String) vMixture.elementAt(i);
      b = false;
      for (int j = 0; j < tempVOs.length; j++) {
        s1 = tempVOs[j].getCwarehouseid();
        s2 = tempVOs[j].getCvendormangid();
        s3 = tempVOs[j].getCstoreorganization();
        s4 = tempVOs[j].getCemployeeid();
        s5 = tempVOs[j].getCdeptid();
        s6 = tempVOs[j].getCbiztype();
        s7 = tempVOs[j].getPk_corp();
        s8 = tempVOs[j].getPk_arrvcorp();
        if (s1 == null || s1.trim().length() == 0)
          s1 = "null";
        if (s2 == null || s2.trim().length() == 0)
          s2 = "null";
        if (s3 == null || s3.trim().length() == 0)
          s3 = "null";
        if (s4 == null || s4.trim().length() == 0)
          s4 = "null";
        if (s5 == null || s5.trim().length() == 0)
          s5 = "null";
        if (s6 == null || s6.trim().length() == 0)
          s6 = "null";
        if (s7 == null || s7.trim().length() == 0)
          s7 = "null";
        if (s8 == null || s8.trim().length() == 0)
          s8 = "null";
        sID = s1 + s2 + s3 + s4 + s5 + s6 + s7 + s8;
        if (sTemp.equals(sID)) {
          if (!b) {
            // ������ⵥ��ͷ
            headVO.setDbilldate(m_dSystem);
            headVO.setCbiztype(tempVOs[j].getCbiztype());
            headVO.setCproviderid(tempVOs[j].getCvendormangid());
            headVO.setPk_cubasdoc(tempVOs[j].getCvendorbaseid());
            headVO.setCdptid(tempVOs[j].getCdeptid());
            headVO.setCbizid(tempVOs[j].getCemployeeid());
            headVO.setPk_corp(tempVOs[j].getPk_arrvcorp());// �ջ���˾
            headVO.setPk_purcorp(tempVOs[j].getPk_corp());// �ɹ���˾
            headVO.setCstoreorganization(tempVOs[j].getPk_arrvstoorg());// �ջ������֯
            headVO.setCwarehouseid(tempVOs[j].getCwarehouseid());
            headVO.setCbilltypecode("4T");
            headVO.setCregister(getClientEnvironment().getUser()
                .getPrimaryKey());
            headVO.setCdispatcherid(sRSMode[j]);
            headVO.setCoperatorid(getClientEnvironment().getUser()
                .getPrimaryKey());

//            headVO.setVuserdef1(tempVOs[j].getHvdef1());
//            headVO.setVuserdef2(tempVOs[j].getHvdef2());
//            headVO.setVuserdef3(tempVOs[j].getHvdef3());
//            headVO.setVuserdef4(tempVOs[j].getHvdef4());
//            headVO.setVuserdef5(tempVOs[j].getHvdef5());
//            headVO.setVuserdef6(tempVOs[j].getHvdef6());
//            headVO.setVuserdef7(tempVOs[j].getHvdef7());
//            headVO.setVuserdef8(tempVOs[j].getHvdef8());
//            headVO.setVuserdef9(tempVOs[j].getHvdef9());
//            headVO.setVuserdef10(tempVOs[j].getHvdef10());
//            headVO.setVuserdef11(tempVOs[j].getHvdef11());
//            headVO.setVuserdef12(tempVOs[j].getHvdef12());
//            headVO.setVuserdef13(tempVOs[j].getHvdef13());
//            headVO.setVuserdef14(tempVOs[j].getHvdef14());
//            headVO.setVuserdef15(tempVOs[j].getHvdef15());
//            headVO.setVuserdef16(tempVOs[j].getHvdef16());
//            headVO.setVuserdef17(tempVOs[j].getHvdef17());
//            headVO.setVuserdef18(tempVOs[j].getHvdef18());
//            headVO.setVuserdef19(tempVOs[j].getHvdef19());
//            headVO.setVuserdef20(tempVOs[j].getHvdef20());
//
//            headVO.setPk_defdoc1(tempVOs[j].getHpkdef1());
//            headVO.setPk_defdoc2(tempVOs[j].getHpkdef2());
//            headVO.setPk_defdoc3(tempVOs[j].getHpkdef3());
//            headVO.setPk_defdoc4(tempVOs[j].getHpkdef4());
//            headVO.setPk_defdoc5(tempVOs[j].getHpkdef5());
//            headVO.setPk_defdoc6(tempVOs[j].getHpkdef6());
//            headVO.setPk_defdoc7(tempVOs[j].getHpkdef7());
//            headVO.setPk_defdoc8(tempVOs[j].getHpkdef8());
//            headVO.setPk_defdoc9(tempVOs[j].getHpkdef9());
//            headVO.setPk_defdoc10(tempVOs[j].getHpkdef10());
//            headVO.setPk_defdoc11(tempVOs[j].getHpkdef11());
//            headVO.setPk_defdoc12(tempVOs[j].getHpkdef12());
//            headVO.setPk_defdoc13(tempVOs[j].getHpkdef13());
//            headVO.setPk_defdoc14(tempVOs[j].getHpkdef14());
//            headVO.setPk_defdoc15(tempVOs[j].getHpkdef15());
//            headVO.setPk_defdoc16(tempVOs[j].getHpkdef16());
//            headVO.setPk_defdoc17(tempVOs[j].getHpkdef17());
//            headVO.setPk_defdoc18(tempVOs[j].getHpkdef18());
//            headVO.setPk_defdoc19(tempVOs[j].getHpkdef19());
//            headVO.setPk_defdoc20(tempVOs[j].getHpkdef20());
            headVO.setVnote(tempVOs[j].getVmemoHead());
          }

          // ������ⵥ����
          GeneralHItemVO bodyVO = new GeneralHItemVO();
          // since v502
          bodyVO.setBLargess(tempVOs[j].getBLargess());
          //
          bodyVO.setCsourcebillhid(tempVOs[j].getCorderid());
          bodyVO.setCsourcebillbid(tempVOs[j].getCorder_bid());
          bodyVO.setVsourcebillcode(tempVOs[j].getVordercode());
          bodyVO.setCsourcetype(ScmConst.PO_Order);
          bodyVO.setBzgflag(new UFBoolean(true));
          bodyVO.setIsok(new UFBoolean(false));
          bodyVO.setCinventoryid(tempVOs[j].getCmangid());
          bodyVO.setNinnum(tempVOs[j].getNgaugenum());
          bodyVO.setNprice(tempVOs[j].getNprice());
          bodyVO.setNmny(tempVOs[j].getNmoney());

          bodyVO.setVfree1(tempVOs[j].getVfree1());
          bodyVO.setVfree2(tempVOs[j].getVfree2());
          bodyVO.setVfree3(tempVOs[j].getVfree3());
          bodyVO.setVfree4(tempVOs[j].getVfree4());
          bodyVO.setVfree5(tempVOs[j].getVfree5());
          bodyVO.setCprojectid(tempVOs[j].getCprojectid());
          bodyVO.setCprojectphaseid(tempVOs[j].getCprojectphaseid());

          bodyVO.setCfirstbillhid(tempVOs[j].getCorderid());
          bodyVO.setCfirstbillbid(tempVOs[j].getCorder_bid());
          bodyVO.setCfirsttype(ScmConst.PO_Order);
          // ���κ�
          bodyVO.setVbatchcode(tempVOs[j].getVbatchcode());
          bodyVO.setFlargess(new UFBoolean(false));
          // �ݹ�����
          // bodyVO.setDzgdate(m_dSystem);
          // bodyVO.setDbizdate(m_dSystem);

          bodyVO.setDzgdate(headVO.getDbilldate());
          bodyVO.setDbizdate(headVO.getDbilldate());

          bodyVO.setPk_reqcorp(tempVOs[j].getPk_reqcorp());// ����˾
          bodyVO.setPk_invoicecorp(tempVOs[j].getPk_invoicecorp());// ��Ʊ��˾
          bodyVO.setPk_reqstoorg(tempVOs[j].getPk_reqstoorg());// ��������֯
          bodyVO.setPk_creqwareid(tempVOs[j].getPk_creqwareid());// ����ֿ�
          bodyVO.setPk_corp(headVO.getPk_corp());

          bodyVO.setCbaseid(tempVOs[j].getCbaseid());
          bodyVO.setCvendorid(tempVOs[j].getCvendormangid());

          // �����ƻ���ID
          bodyVO.setCorder_bb1id(tempVOs[j].getCorder_bb1id());
//          bodyVO.setVuserdef1(tempVOs[j].getBvdef1());
//          bodyVO.setVuserdef2(tempVOs[j].getBvdef2());
//          bodyVO.setVuserdef3(tempVOs[j].getBvdef3());
//          bodyVO.setVuserdef4(tempVOs[j].getBvdef4());
//          bodyVO.setVuserdef5(tempVOs[j].getBvdef5());
//          bodyVO.setVuserdef6(tempVOs[j].getBvdef6());
//          bodyVO.setVuserdef7(tempVOs[j].getBvdef7());
//          bodyVO.setVuserdef8(tempVOs[j].getBvdef8());
//          bodyVO.setVuserdef9(tempVOs[j].getBvdef9());
//          bodyVO.setVuserdef10(tempVOs[j].getBvdef10());
//          bodyVO.setVuserdef11(tempVOs[j].getBvdef11());
//          bodyVO.setVuserdef12(tempVOs[j].getBvdef12());
//          bodyVO.setVuserdef13(tempVOs[j].getBvdef13());
//          bodyVO.setVuserdef14(tempVOs[j].getBvdef14());
//          bodyVO.setVuserdef15(tempVOs[j].getBvdef15());
//          bodyVO.setVuserdef16(tempVOs[j].getBvdef16());
//          bodyVO.setVuserdef17(tempVOs[j].getBvdef17());
//          bodyVO.setVuserdef18(tempVOs[j].getBvdef18());
//          bodyVO.setVuserdef19(tempVOs[j].getBvdef19());
//          bodyVO.setVuserdef20(tempVOs[j].getBvdef20());
//
//          bodyVO.setPk_defdoc1(tempVOs[j].getBpkdef1());
//          bodyVO.setPk_defdoc2(tempVOs[j].getBpkdef2());
//          bodyVO.setPk_defdoc3(tempVOs[j].getBpkdef3());
//          bodyVO.setPk_defdoc4(tempVOs[j].getBpkdef4());
//          bodyVO.setPk_defdoc5(tempVOs[j].getBpkdef5());
//          bodyVO.setPk_defdoc6(tempVOs[j].getBpkdef6());
//          bodyVO.setPk_defdoc7(tempVOs[j].getBpkdef7());
//          bodyVO.setPk_defdoc8(tempVOs[j].getBpkdef8());
//          bodyVO.setPk_defdoc9(tempVOs[j].getBpkdef9());
//          bodyVO.setPk_defdoc10(tempVOs[j].getBpkdef10());
//          bodyVO.setPk_defdoc11(tempVOs[j].getBpkdef11());
//          bodyVO.setPk_defdoc12(tempVOs[j].getBpkdef12());
//          bodyVO.setPk_defdoc13(tempVOs[j].getBpkdef13());
//          bodyVO.setPk_defdoc14(tempVOs[j].getBpkdef14());
//          bodyVO.setPk_defdoc15(tempVOs[j].getBpkdef15());
//          bodyVO.setPk_defdoc16(tempVOs[j].getBpkdef16());
//          bodyVO.setPk_defdoc17(tempVOs[j].getBpkdef17());
//          bodyVO.setPk_defdoc18(tempVOs[j].getBpkdef18());
//          bodyVO.setPk_defdoc19(tempVOs[j].getBpkdef19());
//          bodyVO.setPk_defdoc20(tempVOs[j].getBpkdef20());
          bodyVO.setVnote(tempVOs[j].getVmemoBody());       

          // ������ⵥ���ӱ�
          GeneralBb3VO bb3VO = new GeneralBb3VO();
          bb3VO.setNaccountnum1(new UFDouble(0.0));
          bb3VO.setNaccountmny(new UFDouble(0.0));
          bb3VO.setNpprice(tempVOs[j].getNprice());
          bb3VO.setNpmoney(tempVOs[j].getNmoney());

          b = true;
          v.addElement(bodyVO);
          v0.addElement(bb3VO);
        }
      }

      // ������ⵥ
      if (v.size() > 0) {
        GeneralHItemVO bodyVOs[] = new GeneralHItemVO[v.size()];
        v.copyInto(bodyVOs);

        GeneralBb3VO bb3VOs[] = new GeneralBb3VO[v0.size()];
        v0.copyInto(bb3VOs);

        GeneralHVO vo = new GeneralHVO(v.size());
        vo.setParentVO(headVO);
        vo.setChildrenVO(bodyVOs);
        vo.setGrandVO(bb3VOs);

        vv.addElement(vo);
      }

    }

    // ����
    if (vv.size() > 0) {
      GeneralHVO returnVOs[] = new GeneralHVO[vv.size()];
      vv.copyInto(returnVOs);
      return returnVOs;
    }

    return null;
  }

  /**
   * �˴����뷽��˵���� ��������:�����ˢ�½��� �������: ����ֵ: �쳣����:
   */
  private void doRefresh(int n) {
    // ��ʾ�б�
    getBillCardPanel1().setVisible(false);

    add(getBillListPanel(), java.awt.BorderLayout.CENTER);
    this.setButtons(m_buttons31);
    m_nButtonState = new int[m_buttons3.length];

    // �������ݹ���ⵥ������ʾ
    Vector v1 = new Vector();
    for (int i = 0; i < m_VOs.length; i++) {
      //if (i != n)
        v1.addElement(m_VOs[i]);
    }
    if (v1.size() == 0) {
      // ���������ɵ��ݹ���ⵥ�ѱ������
      m_bAdd = false;
      m_bGenerate = false;
      m_VOs = null;
      getBillListPanel().getHeadBillModel().clearBodyData();
      getBillListPanel().getBodyBillModel().clearBodyData();
      getBillListPanel().updateUI();
      //
      setListBtnsStateEditable();
      return;
    }

    // ���������ɵ��ݹ���ⵥ�ѱ������
    m_VOs = new GeneralHVO[v1.size()];
    v1.copyInto(m_VOs);

    Vector v2 = new Vector();
    for (int i = 0; i < m_VOs.length; i++)
      v2.addElement(m_VOs[i].getHeadVO());
    GeneralHHeaderVO head[] = new GeneralHHeaderVO[v2.size()];
    v2.copyInto(head);

    GeneralHItemVO body[] = m_VOs[0].getBodyVO();
    // ����cbiztype,cproviderid,cdptid,cbizid,cwarehouseid,cwhsmanagerid
    getBillListPanel().hideHeadTableCol("cbiztype");
    getBillListPanel().hideHeadTableCol("cproviderid");
    getBillListPanel().hideHeadTableCol("cdptid");
    getBillListPanel().hideHeadTableCol("cbizid");
    getBillListPanel().hideHeadTableCol("cwarehouseid");
    getBillListPanel().hideHeadTableCol("cwhsmanagerid");

    getBillListPanel().getHeadBillModel().setBodyDataVO(head);
    getBillListPanel().getHeadBillModel().execLoadFormula();
    getBillListPanel().getBodyBillModel().setBodyDataVO(body);
    getBillListPanel().getBodyBillModel().execLoadFormula();
    getBillListPanel().getHeadBillModel().updateValue();
    getBillListPanel().getBodyBillModel().updateValue();
    getBillListPanel().updateUI();

    // ��ʾ��ע
    for (int i = 0; i < head.length; i++) {
      getBillListPanel().getHeadBillModel().setValueAt(head[i].getVnote(), i,
          "vnote");
    }
    //
    setListBtnsStateEditable();
  }

  /**
   * �˴����뷽��˵���� ��������:���ݶ���������ⵥ����ѡ���ķֵ���ʽ���С� ע�⣺�����֯��ҵ��Ա�����š�ҵ�����͡��ɹ���˾��ϵͳȱʡ�ķֵ�������
   * �������:����VO ����ֵ: �쳣����:
   */
  private void generateStock(OorderVO tempVOs[]) {
    try {
      for (int i = 0; i < tempVOs.length; i++)
        tempVOs[i].validate();
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000060")/* @res "�������" */, e
          .getMessage());
      return;
    }

    GeneralHVO vo[] = null;

    if (m_nMode == 0) {
      // ����Ӧ��
      // ��Ӧ�̱���+�����֯+ҵ��Ա+����+ҵ������+�ɹ���˾Ψһ���
      Vector vMixture = new Vector();
      String sID = null, s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, s7 = null;
      for (int i = 0; i < tempVOs.length; i++) {
        s1 = tempVOs[i].getCvendormangid();
        s2 = tempVOs[i].getCstoreorganization();
        s3 = tempVOs[i].getCemployeeid();
        s4 = tempVOs[i].getCdeptid();
        s5 = tempVOs[i].getCbiztype();
        s6 = tempVOs[i].getPk_corp();
        s7 = tempVOs[i].getPk_arrvcorp();
        if (s1 == null || s1.trim().length() == 0)
          s1 = "null";
        if (s2 == null || s2.trim().length() == 0)
          s2 = "null";
        if (s3 == null || s3.trim().length() == 0)
          s3 = "null";
        if (s4 == null || s4.trim().length() == 0)
          s4 = "null";
        if (s5 == null || s5.trim().length() == 0)
          s5 = "null";
        if (s6 == null || s6.trim().length() == 0)
          s6 = "null";
        if (s7 == null || s7.trim().length() == 0)
          s7 = "null";
        sID = s1 + s2 + s3 + s4 + s5 + s6 + s7;
        if (!vMixture.contains(sID))
          vMixture.addElement(sID);
      }
      vo = doGenerationByVendor(tempVOs, vMixture);

    }
    else if (m_nMode == 1) {
      // �����+��Ӧ��
      // �������+��Ӧ��ID+�����֯+ҵ��Ա+����+ҵ������+�ɹ���˾Ψһ���
      Vector vMixture = new Vector();
      String sID = null, s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, s7 = null, s8 = null;
      for (int i = 0; i < tempVOs.length; i++) {
        s1 = tempVOs[i].getCmangid();
        s2 = tempVOs[i].getCvendormangid();
        s3 = tempVOs[i].getCstoreorganization();
        s4 = tempVOs[i].getCemployeeid();
        s5 = tempVOs[i].getCdeptid();
        s6 = tempVOs[i].getCbiztype();
        s7 = tempVOs[i].getPk_corp();
        s8 = tempVOs[i].getPk_arrvcorp();
        if (s1 == null || s1.trim().length() == 0)
          s1 = "null";
        if (s2 == null || s2.trim().length() == 0)
          s2 = "null";
        if (s3 == null || s3.trim().length() == 0)
          s3 = "null";
        if (s4 == null || s4.trim().length() == 0)
          s4 = "null";
        if (s5 == null || s5.trim().length() == 0)
          s5 = "null";
        if (s6 == null || s6.trim().length() == 0)
          s6 = "null";
        if (s7 == null || s7.trim().length() == 0)
          s7 = "null";
        if (s8 == null || s8.trim().length() == 0)
          s8 = "null";
        sID = s1 + s2 + s3 + s4 + s5 + s6 + s7 + s8;
        if (!vMixture.contains(sID))
          vMixture.addElement(sID);
      }
      vo = doGenerationByInv(tempVOs, vMixture);

    }
    else if (m_nMode == 2) {
      // ��������
      // ������+�����֯+ҵ��Ա+����+ҵ������+�ɹ���˾Ψһ���
      Vector vMixture = new Vector();
      String sID = null, s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, s7 = null;
      for (int i = 0; i < tempVOs.length; i++) {
        s1 = tempVOs[i].getVordercode();
        s2 = tempVOs[i].getCstoreorganization();
        s3 = tempVOs[i].getCemployeeid();
        s4 = tempVOs[i].getCdeptid();
        s5 = tempVOs[i].getCbiztype();
        s6 = tempVOs[i].getPk_corp();
        s7 = tempVOs[i].getPk_arrvcorp();
        if (s1 == null || s1.trim().length() == 0)
          s1 = "null";
        if (s2 == null || s2.trim().length() == 0)
          s2 = "null";
        if (s3 == null || s3.trim().length() == 0)
          s3 = "null";
        if (s4 == null || s4.trim().length() == 0)
          s4 = "null";
        if (s5 == null || s5.trim().length() == 0)
          s5 = "null";
        if (s6 == null || s6.trim().length() == 0)
          s6 = "null";
        if (s7 == null || s7.trim().length() == 0)
          s7 = "null";
        sID = s1 + s2 + s3 + s4 + s5 + s6 + s7;
        if (!vMixture.contains(sID))
          vMixture.addElement(sID);
      }
      vo = doGenerationByOrder(tempVOs, vMixture);

    }
    else if (m_nMode == 3) {
      // ���ֿ�+��Ӧ��
      // �ֿ�ID+��Ӧ��ID+�����֯+ҵ��Ա+����+ҵ������+�ɹ���˾Ψһ���
      Vector vMixture = new Vector();
      String sID = null, s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, s7 = null, s8 = null;
      for (int i = 0; i < tempVOs.length; i++) {
        s1 = tempVOs[i].getCwarehouseid();
        s2 = tempVOs[i].getCvendormangid();
        s3 = tempVOs[i].getCstoreorganization();
        s4 = tempVOs[i].getCemployeeid();
        s5 = tempVOs[i].getCdeptid();
        s6 = tempVOs[i].getCbiztype();
        s7 = tempVOs[i].getPk_corp();
        s8 = tempVOs[i].getPk_arrvcorp();
        if (s1 == null || s1.trim().length() == 0)
          s1 = "null";
        if (s2 == null || s2.trim().length() == 0)
          s2 = "null";
        if (s3 == null || s3.trim().length() == 0)
          s3 = "null";
        if (s4 == null || s4.trim().length() == 0)
          s4 = "null";
        if (s5 == null || s5.trim().length() == 0)
          s5 = "null";
        if (s6 == null || s6.trim().length() == 0)
          s6 = "null";
        if (s7 == null || s7.trim().length() == 0)
          s7 = "null";
        if (s8 == null || s8.trim().length() == 0)
          s8 = "null";
        sID = s1 + s2 + s3 + s4 + s5 + s6 + s7 + s8;
        if (!vMixture.contains(sID))
          vMixture.addElement(sID);
      }
      vo = doGenerationByWarehouse(tempVOs, vMixture);

    }
    else if (m_nMode == 4) {
      // ��������+�ֿ�
      // ������+�ֿ�ID+�����֯+ҵ��Ա+����+ҵ������+�ɹ���˾Ψһ���
      Vector vMixture = new Vector();
      String sID = null, s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, s7 = null, s8 = null;
      for (int i = 0; i < tempVOs.length; i++) {
        s1 = tempVOs[i].getCwarehouseid();
        s2 = tempVOs[i].getVordercode();
        s3 = tempVOs[i].getCstoreorganization();
        s4 = tempVOs[i].getCemployeeid();
        s5 = tempVOs[i].getCdeptid();
        s6 = tempVOs[i].getCbiztype();
        s7 = tempVOs[i].getPk_corp();
        s8 = tempVOs[i].getPk_arrvcorp();
        if (s1 == null || s1.trim().length() == 0)
          s1 = "null";
        if (s2 == null || s2.trim().length() == 0)
          s2 = "null";
        if (s3 == null || s3.trim().length() == 0)
          s3 = "null";
        if (s4 == null || s4.trim().length() == 0)
          s4 = "null";
        if (s5 == null || s5.trim().length() == 0)
          s5 = "null";
        if (s6 == null || s6.trim().length() == 0)
          s6 = "null";
        if (s7 == null || s7.trim().length() == 0)
          s7 = "null";
        if (s8 == null || s8.trim().length() == 0)
          s8 = "null";
        sID = s1 + s2 + s3 + s4 + s5 + s6 + s7 + s8;
        if (!vMixture.contains(sID))
          vMixture.addElement(sID);
      }
      vo = doGenerationByMixture(tempVOs, vMixture);
    }

    // ���ɵ���ⵥ���뵽������
    if (vo == null || vo.length == 0)
      return;

    // ������
    try {
      vo = switchVOForDifferentCorp(vo);
      if (vo == null || vo.length == 0)
        return;

    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000061")/* @res "��ȡ��������Ϣ" */, e
          .getMessage());
    }
    //
    try {
      String vfree[][] = EstimateHelper.getFreeItem0(vo);
      if (vfree != null && vfree.length > 0) {
        for (int i = 0; i < vo.length; i++) {
          GeneralHItemVO itemVO[] = vo[i].getBodyVO();
          if (itemVO != null && itemVO.length > 0) {
            for (int j = 0; j < itemVO.length; j++) {
              if (vfree[i] != null) {
                itemVO[j].setVfree(vfree[i][j]);
              }
            }
          }
        }
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000061")/* @res "��ȡ��������Ϣ" */, e
          .getMessage());
    }
    //

    Vector v = new Vector();
    // if(m_gVOs!=null && m_gVOs.length>0){
    // for(int i=0;i<m_gVOs.length;i++) v.addElement(m_gVOs[i]);
    // }
    for (int i = 0; i < vo.length; i++)
      v.addElement(vo[i]);

    m_gVOs = new GeneralHVO[v.size()];
    v.copyInto(m_gVOs);

    m_bGenerate = true;
  }

  /**
   * �˴����뷽��˵���� ��������:���Ҫ��ӡ���ֶ����� �������: ����ֵ: �쳣����:
   */
  public String[] getAllDataItemExpress() {
    BillItem headItems[] = getBillCardPanel1().getHeadShowItems();
    BillItem bodyItems[] = getBillCardPanel1().getBodyShowItems();

    Vector v = new Vector();
    for (int i = 0; i < headItems.length; i++)
      v.addElement(headItems[i].getKey());
    for (int i = 0; i < bodyItems.length; i++)
      v.addElement(bodyItems[i].getKey());

    if (v.size() > 0) {
      String sKey[] = new String[v.size()];
      v.copyInto(sKey);
      return sKey;
    }
    return null;
  }

  /**
   * �˴����뷽��˵���� ��������:���Ҫ��ӡ���ֶ����� �������: ����ֵ: �쳣����:
   */
  public String[] getAllDataItemNames() {
    BillItem headItems[] = getBillCardPanel1().getHeadShowItems();
    BillItem bodyItems[] = getBillCardPanel1().getBodyShowItems();

    Vector v = new Vector();
    for (int i = 0; i < headItems.length; i++)
      v.addElement(headItems[i].getName());
    for (int i = 0; i < bodyItems.length; i++)
      v.addElement(bodyItems[i].getName());

    if (v.size() > 0) {
      String sName[] = new String[v.size()];
      v.copyInto(sName);
      return sName;
    }
    return null;
  }

  /**
   * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����:
   */
  public String[] getDependentItemExpressByExpress(String itemName) {
    return null;
  }

  /**
   * �˴����뷽��˵���� ��������:���Ҫ��ӡ���ֶε�ֵ �������:���� ����ֵ: �쳣����:
   */
  public String[] getItemValuesByExpress(String itemKey) {
    BillItem headItem[] = getBillCardPanel1().getHeadShowItems();
    BillItem bodyItem[] = getBillCardPanel1().getBodyShowItems();

    itemKey = itemKey.trim();
    int nPos = -1;
    for (int i = 0; i < headItem.length; i++) {
      String s = headItem[i].getKey().trim();
      if (itemKey.equals("h_" + s)) {
        nPos = 0;
        break;
      }
    }

    if (nPos == -1) {
      for (int i = 0; i < bodyItem.length; i++) {
        String s = bodyItem[i].getKey().trim();
        if (itemKey.equals(s)) {
          nPos = 1;
          break;
        }
      }
    }

    // ��Ƭ
    int nRow = getBillCardPanel1().getRowCount();
    if (nPos == 0) {
      // ��ͷ
      Object o = null;
      if (itemKey.equals("h_vbillcode") || itemKey.equals("h_dbilldate")) {
        GeneralHVO VO = new GeneralHVO(nRow);
        getBillCardPanel1().getBillValueVO(VO);
        GeneralHHeaderVO headVO = VO.getHeadVO();
        o = headVO.getAttributeValue(itemKey.substring(2));
      }
      else {
        UIRefPane nRefPanel = (UIRefPane) getBillCardPanel1().getHeadItem(
            itemKey.substring(2)).getComponent();
        o = nRefPanel.getRefName();
      }

      String sValues[] = new String[1];
      if (o != null)
        sValues[0] = o.toString();
      else
        sValues[0] = "";
      return sValues;
    }
    else {
      // ����
      // ����ϼ���
      int nTotal = 0;
      if (getBillCardPanel1().getBodyPanel().isTatolRow())
        nTotal = 1;
      String sValues[] = new String[nRow + nTotal];
      for (int i = 0; i < nRow; i++) {
        Object o = getBillCardPanel1().getBodyValueAt(i, itemKey);
        if (o != null)
          sValues[i] = o.toString();
        else
          sValues[0] = "";
      }
      if (nTotal > 0) {
        Object o = getBillCardPanel1().getTotalTableModel().getValueAt(0,
            getBillCardPanel1().getBillModel().getBodyColByKey(itemKey));
        if (o != null && o.toString().trim().length() > 0)
          sValues[nRow] = o.toString();
        else
          sValues[nRow] = "";
      }

      return sValues;
    }
  }

  /**
   * ����: ����ڳ��ݹ���ⵥ������ֵ{����ֵ=�޸ĺ��ֵ-�޸�ǰ��ֵ������������޸�ǰ��ֵ=0��} ���أ�ArrayList{boolean[]
   * �Ƿ񵽻��ƻ�[]��String[] ������(�����ƻ�)ID[]��UFDouble[] ������ID��Ӧ����������仯��} ���ߣ��ܺ��� �޸ģ���־ƽ
   * FOR V30 ���ӵ����ƻ�����
   */
  private ArrayList getModifiedOrderStockNum() {
    // ��VO
    GeneralHItemVO itemVO1[] = m_VOs[m_nPresentRecord].getBodyVO();
    int iLen = itemVO1.length;
    boolean[] isPlanLines = new boolean[iLen];
    String[] saKey = new String[iLen];
    UFDouble[] uaNumAdded = new UFDouble[iLen];
    // ��VO
    int nRow = getBillCardPanel1().getRowCount();
    GeneralHVO vo = new GeneralHVO(nRow);
    getBillCardPanel1().getBillValueVO(vo);
    GeneralHItemVO itemVO2[] = vo.getBodyVO();
    int iLenNew = itemVO2.length;

    if (!m_bAdd) {
      saKey = new String[iLen];
      isPlanLines = new boolean[iLen];
      uaNumAdded = new UFDouble[iLen];
      Hashtable hNewNum = new Hashtable();
      for (int i = 0; i < iLenNew; i++) {
        hNewNum.put(itemVO2[i].getCgeneralbid(), itemVO2[i].getNinnum());
      }
      String strOldKey = null;
      UFDouble uNewNum = null;
      for (int i = 0; i < iLen; i++) {
        isPlanLines[i] = (itemVO1[i].getCorder_bb1id() != null && itemVO1[i]
            .getCorder_bb1id().trim().length() > 0);
        strOldKey = itemVO1[i].getCgeneralbid();
        if (isPlanLines[i]) {
          saKey[i] = itemVO1[i].getCorder_bb1id();
        }
        else {
          saKey[i] = itemVO1[i].getCsourcebillbid();
        }
        uNewNum = (UFDouble) hNewNum.get(strOldKey);
        if (!hNewNum.containsKey(strOldKey) || uNewNum == null) {
          uaNumAdded[i] = itemVO1[i].getNinnum().multiply(-1.0);
          continue;
        }
        uaNumAdded[i] = uNewNum.sub(itemVO1[i].getNinnum());
      }
    }
    else {
      saKey = new String[iLenNew];
      isPlanLines = new boolean[iLenNew];
      uaNumAdded = new UFDouble[iLenNew];
      for (int i = 0; i < iLenNew; i++) {
        isPlanLines[i] = (itemVO2[i].getCorder_bb1id() != null && itemVO2[i]
            .getCorder_bb1id().trim().length() > 0);
        saKey[i] = isPlanLines[i] ? itemVO2[i].getCorder_bb1id() : itemVO2[i]
            .getCsourcebillbid();
        uaNumAdded[i] = itemVO2[i].getNinnum();
      }
    }

    ArrayList list = new ArrayList();
    list.add(isPlanLines);
    list.add(saKey);
    list.add(uaNumAdded);

    return list;
  }

  /**
   * �˴����뷽��˵���� ��������:��ô�ӡģ������ �������: ����ֵ: �쳣����:
   */
  public String getModuleName() {
    return "4004050302";
  }

  /**
   * ��������:��ʼ�� �޸ģ�2004-09-10 ԬҰ
   */
  private void initConfigure() {

    if (m_initVO != null) {
      String sMode = m_initVO.getValue().trim();
      if (sMode.equals("��Ӧ��"))
        m_nMode = 0;
      else if (sMode.equals("���+��Ӧ��"))
        m_nMode = 1;
      else if (sMode.equals("����"))
        m_nMode = 2;
      else if (sMode.equals("�ֿ�+��Ӧ��"))
        m_nMode = 3;
      else
        m_nMode = 4;
    }

    // �ֿ�
    UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem(
        "cwarehouseid").getComponent();
    nRefPanel1.setIsCustomDefined(true);
    nRefPanel1.setRefModel(new nc.ui.ps.pub.WarehouseRef(m_sUnitCode));
    AbstractRefModel refModel = nRefPanel1.getRefModel();
    ((WarehouseRef) refModel).setVMIFlag(true);
    // ����
    UIRefPane nRefPanel2 = (UIRefPane) getBillCardPanel1()
        .getHeadItem("cdptid").getComponent();
    String strWherePart = " AND (deptattr IN ('2','4')) ";
    nRefPanel2.getRefModel().addWherePart(strWherePart);

    return;
  }

  /**
   * �˴����뷽��˵���� ��������:��ʼ��С��λ �޸ģ�2004-09-10 ԬҰ
   */
  private BillData initDecimal1(BillData bd) {
    // ���ϵͳ��ʼ������
    // int measure[] = nc.ui.pu.pub.PuTool.getDigitBatch(m_sUnitCode, new
    // String[]{"BD501","BD505","BD301"});

    if (measure == null || measure.length == 0) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000026")/* @res "���ϵͳ��ʼ������" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000027")/* @res "�޷����ϵͳ��ʼ��������" */);
      return null;
    }

    // ���������С��λ
    m_unitDecimal = measure[0];

    // ��òɹ�����С��λ
    m_priceDecimal = measure[1];

    // ��òɹ����С��λ
    m_moneyDecimal = measure[2];

    // ��ø�����С��λ
    m_unitDecimalAssist = measure[3];

    // ����ϵͳ��ʼ������
    int nMeasDecimal = m_unitDecimal;
    int nPriceDecimal = m_priceDecimal;
    int nMoneyDecimal = m_moneyDecimal;

    // ��õ���Ԫ�ض�Ӧ�Ŀؼ�,���޸Ŀؼ�������
    if (bd.getBodyItem("ninnum") != null) {
      bd.getBodyItem("ninnum").setDecimalDigits(nMeasDecimal);
    }
    if (bd.getBodyItem("nshouldoutnum") != null) {
      bd.getBodyItem("nshouldoutnum").setDecimalDigits(nMeasDecimal);
    }
    if (bd.getBodyItem("noutnum") != null) {
      bd.getBodyItem("noutnum").setDecimalDigits(nMeasDecimal);
    }
    if (bd.getBodyItem("nshouldinnum") != null) {
      bd.getBodyItem("nshouldinnum").setDecimalDigits(nMeasDecimal);
    }
    if (bd.getBodyItem("ntranoutnum") != null) {
      bd.getBodyItem("ntranoutnum").setDecimalDigits(nMeasDecimal);
    }
    if (bd.getBodyItem("nshouldoutassistnum") != null) {
      bd.getBodyItem("nshouldoutassistnum").setDecimalDigits(
          m_unitDecimalAssist);
    }
    if (bd.getBodyItem("noutassistnum") != null) {
      bd.getBodyItem("noutassistnum").setDecimalDigits(m_unitDecimalAssist);
    }
    if (bd.getBodyItem("nneedinassistnum") != null) {
      bd.getBodyItem("nneedinassistnum").setDecimalDigits(m_unitDecimalAssist);
    }
    if (bd.getBodyItem("ninassistnum") != null) {
      bd.getBodyItem("ninassistnum").setDecimalDigits(m_unitDecimalAssist);
    }
    if (bd.getBodyItem("nprice") != null) {
      bd.getBodyItem("nprice").setDecimalDigits(nPriceDecimal);
    }
    if (bd.getBodyItem("nmny") != null) {
      bd.getBodyItem("nmny").setDecimalDigits(nMoneyDecimal);
    }
    if (bd.getBodyItem("nplannedmny") != null) {
      bd.getBodyItem("nplannedmny").setDecimalDigits(nMoneyDecimal);
    }

    return bd;
  }

  /**
   * �˴����뷽��˵���� ��������:��ʼ��С��λ �������: ����ֵ: �쳣����:
   */
  private BillData initDecimal2(BillData bd) {

    // ����ϵͳ��ʼ������
    int nMeasDecimal = m_unitDecimal;
    int nPriceDecimal = m_priceDecimal;
    int nMoneyDecimal = m_moneyDecimal;

    // ��õ���Ԫ�ض�Ӧ�Ŀؼ�,���޸Ŀؼ�������
    if (bd.getBodyItem("nordernum") != null) {
      bd.getBodyItem("nordernum").setDecimalDigits(nMeasDecimal);
    }
    if (bd.getBodyItem("ngaugenum") != null) {
      bd.getBodyItem("ngaugenum").setDecimalDigits(nMeasDecimal);
    }
    if (bd.getBodyItem("nprice") != null) {
      bd.getBodyItem("nprice").setDecimalDigits(nPriceDecimal);
    }
    if (bd.getBodyItem("ntaxprice") != null) {
      bd.getBodyItem("ntaxprice").setDecimalDigits(nPriceDecimal);
    }
    if (bd.getBodyItem("nmoney") != null) {
      bd.getBodyItem("nmoney").setDecimalDigits(nMoneyDecimal);
    }
    if (bd.getBodyItem("ntaxmoney") != null) {
      bd.getBodyItem("ntaxmoney").setDecimalDigits(nMoneyDecimal);
    }
    if (bd.getBodyItem("ntotalmoney") != null) {
      bd.getBodyItem("ntotalmoney").setDecimalDigits(nMoneyDecimal);
    }
    //

    // �ݹ�Ӧ��֧����� zhf 2008-06-27
    BillItem item1 = bd.getBodyItem("noriginalnetprice");
    item1.setDecimalDigits(nPriceDecimal);

    item1 = bd.getBodyItem("norgnettaxprice");
    item1.setDecimalDigits(nPriceDecimal);

    BillItem item2 = bd.getBodyItem("noriginalcurmny");
    item2.setDecimalDigits(nMoneyDecimal);

    item2 = bd.getBodyItem("noriginaltaxpricemny");
    item2.setDecimalDigits(nMoneyDecimal);
    // end

    // ------ by zhaoyha at 2008.10.23 -----------------------
    //֧���ݹ�Ӧ��ҳǩ�ı���
    BillItem item3 = bd.getBodyItem("nzgyfnotaxprice"); //Ӧ��������˰����
    if (item3 != null) {
      item3.setDecimalDigits(nPriceDecimal);
    }
    item3=bd.getBodyItem("nzgyfnotaxmoney"); //Ӧ��������˰���
    if (item3 != null) {
      item3.setDecimalDigits(nMoneyDecimal);
    }
    item3 = bd.getBodyItem("nzgyfprice"); //Ӧ�����Һ�˰����
    if (item3 != null) {
      item3.setDecimalDigits(nPriceDecimal);
    }
    item3=bd.getBodyItem("nzgyfmoney"); //Ӧ�����Һ�˰���
    if (item3 != null) {
      item3.setDecimalDigits(nMoneyDecimal);
    }
    
    return bd;
  }

  /**
   * �˴����뷽��˵���� ��������:��ʼ��С��λ �������: ����ֵ: �쳣����:
   */
  private BillListData initListDecimal(BillListData bd) {
    // ����ϵͳ��ʼ������
    int nMeasDecimal = m_unitDecimal;
    int nPriceDecimal = m_priceDecimal;
    int nMoneyDecimal = m_moneyDecimal;

    // ��õ���Ԫ�ض�Ӧ�Ŀؼ�,���޸Ŀؼ�������
    BillItem item1 = bd.getBodyItem("ninnum");
    item1.setDecimalDigits(nMeasDecimal);

    BillItem item2 = bd.getBodyItem("nprice");
    item2.setDecimalDigits(nPriceDecimal);

    BillItem item3 = bd.getBodyItem("nmny");
    item3.setDecimalDigits(nMoneyDecimal);

    return bd;
  }

  /**
   * Ϊ�˼��ٳ�ʼ��ʱǰ��̨�����Ĵ�����һ���Ի�ȡ���ϵͳ���� ����:ԬҰ ���ڣ�2004-09-09
   */
  public void initpara() {
    try {

      Object[] objs = null;
      ServcallVO[] scds = new ServcallVO[3];

      // ���ϵͳ��ʼ������
      scds[0] = new ServcallVO();
      scds[0].setBeanName("nc.itf.pu.pub.IPub");
      scds[0].setMethodName("getDigitBatch");
      scds[0].setParameter(new Object[] {
          m_sUnitCode, new String[] {
              "BD501", "BD505", "BD301", "BD502"
          }
      });
      scds[0].setParameterTypes(new Class[] {
          String.class, String[].class
      });

      // ����Ƿ�����
      scds[1] = new ServcallVO();
      scds[1].setBeanName("nc.itf.pu.pub.IPub");
      scds[1].setMethodName("isEnabled");
      scds[1].setParameter(new Object[] {
          m_sUnitCode, "IC"
      });
      scds[1].setParameterTypes(new Class[] {
          String.class, String.class
      });

      // ���ϵͳ��������
      scds[2] = new ServcallVO();
      scds[2].setBeanName("nc.itf.uap.sf.ICreateCorpQueryService");
      scds[2].setMethodName("queryEnabledPeriod");
      scds[2].setParameter(new Object[] {
          m_sUnitCode, "PO"
      });
      scds[2].setParameterTypes(new Class[] {
          String.class, String.class
      });
      //
      objs = nc.ui.scm.service.LocalCallService.callService(scds);
      //
      measure = (int[]) objs[0];// ���ϵͳ��ʼ������
      m_bICStartUp = ((UFBoolean) objs[1]).booleanValue();// ����Ƿ�����

      /*
       * ϵͳ��������,�ַ������飬����Ϊ5����Ԫ�����£�<br> <code>0</code> - �����ꣻ<br> <code>1</code> -
       * �����£�<br> <code>2</code> - ʱ���(char 19)��<br> <code>3</code> -
       * ��Ӧ��Ȼ���µ����죻<br> <code>4</code> - ��Ӧ��Ȼ���µ�ĩ�졣
       */
      String[] sysDate = (String[]) objs[2];
      m_dSystem = PuPubVO.getUFDate(sysDate[3]);
      // ϵͳ�������������һ�����ڳ������������󵥾�����
      m_dSystem = m_dSystem.getDateBefore(1);
      // �ݹ�Ӧ��
      m_bZGYF = SysInitBO_Client.getParaBoolean(m_sUnitCode, "PO52");

      // ���ϵͳ���õķֵ���ʽ
      m_initVO = SysInitBO_Client.queryByParaCode(m_sUnitCode, "PO21");

      // ���ϵͳ���õ��ݹ���ʽ�Ͳ���ת�뷽ʽ
      Hashtable t = SysInitBO_Client.queryBatchParaValues(m_sUnitCode,
          new String[] {
              "PO27", "BD301"
          });
      if (t == null || t.size() == 0)
        return;

      if (t.get("PO27") != null)
        m_sEstPriceSource = t.get("PO27").toString().trim();
      if (t.get("BD301") != null) {
        Object temp = t.get("BD301");
        m_sCurrTypeID = temp.toString();
      }
      else {
        SCMEnv.out("δ��ȡ��ʼ����������λ��[BD301], ����...");
      }

    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000028")/* @res "��ȡϵͳ��ʼ����������" */, e.getMessage());
      return;
    }
  }

  /**
   * ��ѯģ���ʼ�� ����:ԬҰ ���ڣ�2004-09-10
   */
  public InitialUIQryDlg initQueryModel1(InitialUIQryDlg condClient,
      String pk_corp, boolean bLinkQuery) {

    // ��ʼ����ѯģ��
    condClient = new InitialUIQryDlg(this);
    condClient.setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "4004050302", "UPP4004050302-000006")/* @res "�ڳ��ݹ���ѯ" */);
    condClient.setTempletID(pk_corp, "400405020201", getClientEnvironment()
        .getUser().getPrimaryKey(), null, "40040502001");

    condClient.setValueRef("dbilldate", "����");
    condClient.setValueRef("cinvclassid", "�������");
    condClient.setValueRef("coperatorid", "����Ա");
    condClient.setValueRef("cwarehouseid", "�ֿ⵵��");

    UIRefPane vendorRef = new UIRefPane();
    vendorRef.setRefNodeName("��Ӧ�̵���");
    condClient.setValueRef("cvendorbaseid", vendorRef);

    UIRefPane inventoryRef = new UIRefPane();
    inventoryRef.setRefNodeName("�������");
    condClient.setValueRef("cbaseid", inventoryRef);
    /*
     * since v502,�����ջ���˾��ѯ��������Ϊ, ���ռ���ģʽ�£���ⵥ������˾Ҳ��Ϊ�ɹ���˾��
     * �������ڳ��ݹ���ⵥʵ�����ǵ�����˾���ɹ���˾�����ݣ��òɹ��������ջ���˾�ǲ�ѯ�����ġ�
     */
    // UIRefPane corpPane = new UIRefPane();
    // corpPane.setRefNodeName("��˾Ŀ¼");
    // condClient.setValueRef("pk_stockcorp",corpPane);
    //    
    if (!bLinkQuery) {
      condClient.setDefaultValue("dbilldate", "dbilldate",
          getClientEnvironment().getDate().toString());
      condClient.setIsWarningWithNoInput(true);

      // �����Զ���������
      nc.ui.scm.pub.def.DefSetTool.updateQueryConditionClientUserDef(
          condClient, pk_corp, "icbill", "vuserdef", "ic_general_b.vuserdef");

      /* ���Ļ��������ܱ����� */
      condClient.setSealedDataShow(true);
      condClient.setShowPrintStatusPanel(true);
    }

    // ����Ȩ�޿���
    condClient.setRefsDataPowerConVOs(nc.ui.pub.ClientEnvironment.getInstance()
        .getUser().getPrimaryKey(), new String[] {
      nc.ui.pub.ClientEnvironment.getInstance().getCorporation()
          .getPrimaryKey()
    }, new String[] {
        "��Ӧ�̵���", "�ֿ⵵��", "�������", "�������"
    }, new String[] {
        "cvendorbaseid", "cwarehouseid", "cbaseid", "cinvclassid"
    }, new int[] {
        0, 2, 0, 0
    });

    return condClient;
  }

  /**
   * ��ѯģ���ʼ�� ����:ԬҰ ���ڣ�2004-09-09
   */
  public void initQueryModel2() {
    if (m_condClient2 == null) {
      m_condClient2 = new PoQueryCondition(this, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("4004050302", "UPP4004050302-000006")/*
                                                                           * @res
                                                                           * "�ڳ��ݹ���ѯ"
                                                                           */);
      m_condClient2.setTempletID(m_sUnitCode, "4004050302",
          getClientEnvironment().getUser().getPrimaryKey(), null, "PS04");

      m_condClient2.setValueRef("dorderdate", "����");
      m_condClient2.setValueRef("cinvclassid", "�������");

      UIRefPane vendorRef = new UIRefPane();
      vendorRef.setRefNodeName("��Ӧ�̵���");
      vendorRef.getRefModel().addWherePart(" and frozenflag = 'N' ");
      m_condClient2.setValueRef("cvendorbaseid", vendorRef);

      UIRefPane inventoryRef = new UIRefPane();
      inventoryRef.setRefNodeName("�������");
      inventoryRef.getRefModel().addWherePart(
          " and upper(bd_invmandoc.sealflag) = 'N' ");
      m_condClient2.setValueRef("cbaseid", inventoryRef);

      m_condClient2.setDefaultValue("dorderdate", "dorderdate",
          getClientEnvironment().getDate().toString());

      m_condClient2.setIsWarningWithNoInput(true);
      /* ���Ļ��������ܱ����� */
      m_condClient2.setSealedDataShow(true);

      // ����Ȩ�޿���
      m_condClient2.setRefsDataPowerConVOs(nc.ui.pub.ClientEnvironment
          .getInstance().getUser().getPrimaryKey(), new String[] {
        nc.ui.pub.ClientEnvironment.getInstance().getCorporation()
            .getPrimaryKey()
      }, new String[] {
          "��Ӧ�̵���", "�������", "�������"
      }, new String[] {
          "cvendorbaseid", "cbaseid", "cinvclassid"
      }, new int[] {
          0, 0, 0
      });

    }
  }

  /**
   * �˴����뷽��˵���� ��������:���Ҫ��ӡ���ֶ��Ƿ�Ϊ���� �������:���� ����ֵ: �쳣����:
   */
  public boolean isNumber(String itemKey) {
    itemKey = itemKey.trim();
    itemKey = itemKey.substring(0, 1);
    if (itemKey.equals("n"))
      return true;
    else
      return false;
  }

  /**
   * �˴����뷽��˵���� ��������:�ж��ڳ��ݹ���ⵥ�Ƿ��Ѿ����㣨����ȫ������Ͳ��ֽ��㣩 �������: ����ֵ: �쳣����:
   */
  private boolean isSettled(GeneralHVO VO) {
    boolean b = false;
    GeneralBb3VO bb3VO[] = VO.getGrandVO();

    for (int i = 0; i < bb3VO.length; i++) {
      UFDouble nAccumNum = bb3VO[i].getNaccountnum1();
      if (nAccumNum != null && Math.abs(nAccumNum.doubleValue()) > 0.0) {
        b = true;
        break;
      }
    }
    return b;
  }

  /**
   * ��������:����(����������ⵥ)
   */
  public void onAppend() {
    this.showHintMessage("");
    // ��ѯ��Դ�Ƕ�������ⵥ��ҵ������
    try {
      m_sBiztypeID = EstimateHelper.querySpeBiztypeID(m_sUnitCode);
    }
    catch (Exception e) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000063")/*
                                                         * @res
                                                         * "��ѯ��Դ�Ƕ�������ⵥ��ҵ������"
                                                         */, e.getMessage());
      return;
    }
    if (m_sBiztypeID == null || m_sBiztypeID.length == 0) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000063")/*
                                                         * @res
                                                         * "��ѯ��Դ�Ƕ�������ⵥ��ҵ������"
                                                         */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
          "UPP40040503-000064")/*
                                 * @res "��Դ�Ƕ�������ⵥ��ҵ�����Ͳ����ڣ�����ͨ�����������ڳ��ݹ���ⵥ��"
                                 */);
      return;
    }

    getBillCardPanel1().setVisible(false);
    setLayout(new java.awt.BorderLayout());
    add(getBillCardPanel2(), java.awt.BorderLayout.CENTER);
    getBillCardPanel2().getBillData().clearViewData();
    getBillCardPanel2().setEnabled(false);
    getBillCardPanel2().setVisible(true);
    this.setButtons(m_buttons2);
    m_nButtonState = new int[m_buttons2.length];

    // ���ó���ѯ������а�ťΪ��
    for (int i = 0; i < 5; i++) {
      m_nButtonState[i] = 1;
    }
    m_nButtonState[4] = 0;
    m_nUIState = 2;
    changeButtonState();

    m_bAdd = true;

    // �Զ�����������ѯ�Ի���
    onQueryOrder();
  }

  /**
   * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����: ����:
   */
  private void onBillRelateQuery() {
    if (m_VOs == null || m_VOs.length == 0)
      return;

    GeneralHVO VO = m_VOs[m_nPresentRecord];
    nc.ui.scm.sourcebill.SourceBillFlowDlg soureDlg = new nc.ui.scm.sourcebill.SourceBillFlowDlg(
        this, "4T", /* ��ǰ�������� */
        VO.getHeadVO().getCgeneralhid(), /* ��ǰ����ID */
        null, /* ��ǰҵ������ */
        getClientEnvironment().getUser().getPrimaryKey(), /* ��ǰ�û�ID */
        VO.getHeadVO().getVbillcode()); /* ���ݱ��� */
        
    soureDlg.showModal();
  }

  /**
   * �˴����뷽��˵���� ��������:ȡ�� �������: ����ֵ: �쳣����:
   */
  public void onCancelling() {
    if (m_bGenerate) {
      // �����б�
      getBillCardPanel1().setVisible(false);

      add(getBillListPanel(), java.awt.BorderLayout.CENTER);
      this.setButtons(m_buttons31);
      m_nButtonState = new int[m_buttons3.length];

      // ����cbiztype,cproviderid,cdptid,cbizid,cwarehouseid,cwhsmanagerid
      getBillListPanel().hideHeadTableCol("cbiztype");
      getBillListPanel().hideHeadTableCol("cproviderid");
      getBillListPanel().hideHeadTableCol("cdptid");
      getBillListPanel().hideHeadTableCol("cbizid");
      getBillListPanel().hideHeadTableCol("cwarehouseid");
      getBillListPanel().hideHeadTableCol("cwhsmanagerid");

      // δ������ݹ���ⵥ������ʾ
      Vector v1 = new Vector();
      for (int i = 0; i < m_VOs.length; i++) {
        if (i != m_nPresentRecord || 
            PuPubVO.getString_TrimZeroLenAsNull(m_VOs[m_nPresentRecord].getHeadVO().getCgeneralhid())!=null)
          v1.addElement(m_VOs[i]);
      }
      
      if (v1 == null || v1.size() == 0) {
        // �������ݹ���ⵥ
        getBillListPanel().getHeadBillModel().clearBodyData();
        getBillListPanel().getBodyBillModel().clearBodyData();
        getBillListPanel().updateUI();

        // ����ѯ,�л���������ťΪ��
        for (int i = 0; i < 6; i++)
          m_nButtonState[i] = 1;
        m_nButtonState[3] = 0;
        m_nButtonState[4] = 0;
        m_nUIState = 3;
        changeButtonState();
        //������VO���
        m_VOs=null;
        m_bGenerate=false;
        m_bAdd=false;
        m_bSettling=false;
        getBillCardPanel1().setEnabled(false);
        return;
      }

      // �����ݹ���ⵥ
      m_VOs = new GeneralHVO[v1.size()];
      v1.copyInto(m_VOs);

      Vector v2 = new Vector();
      for (int i = 0; i < m_VOs.length; i++)
        v2.addElement(m_VOs[i].getHeadVO());
      GeneralHHeaderVO head[] = new GeneralHHeaderVO[v2.size()];
      v2.copyInto(head);

      GeneralHItemVO body[] = m_VOs[0].getBodyVO();

      getBillListPanel().getHeadBillModel().setBodyDataVO(head);
      getBillListPanel().getHeadBillModel().execLoadFormula();
      getBillListPanel().getBodyBillModel().setBodyDataVO(body);
      getBillListPanel().getBodyBillModel().execLoadFormula();
      getBillListPanel().getHeadBillModel().updateValue();
      getBillListPanel().getBodyBillModel().updateValue();
      getBillListPanel().updateUI();
      //

      // ��ʾ��ע
      for (int i = 0; i < head.length; i++) {
        getBillListPanel().getHeadBillModel().setValueAt(head[i].getVnote(), i,
            "vnote");
      }
      //
      setListBtnsStateEditable();
      // // ��ȫ��,����,�л���������ťΪ����
      // for (int i = 0; i < 8; i++) {
      // m_nButtonState[i] = 0;
      // }
      // m_nButtonState[1] = 1;
      // m_nButtonState[2] = 1;
      // m_nButtonState[4] = 1;
      // m_nButtonState[5] = 1;
      // m_nButtonState[6] = 1;
      // m_nButtonState[7] = 1;
      //
      // m_nUIState = 3;
      // changeButtonState();

    }
    else {
      m_bAdd = false;
      getBillCardPanel1().setEnabled(false);
      getBillCardPanel1().setBillValueVO(m_VOs[m_nPresentRecord]);
      getBillCardPanel1().getBillModel().execLoadFormula();
      getBillCardPanel1().updateValue();
      getBillCardPanel1().updateUI();

      // ��ʾ��ע
      UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem(
          "vnote").getComponent();
      nRefPanel1.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVnote());
      // ��ʾ��Դ������Ϣ
      nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");
      // �ָ���ť״̬
      for (int i = 0; i < m_nButtonState.length; i++)
        m_nButtonState[i] = m_nRestoreState[i];
      // �ݹ�Ӧ��ʱ,�������޸ģ�Ҳ�Ͳ������в���
      if (m_bZGYF.booleanValue()) {
        m_nButtonState[1] = 1;
      }
      m_nUIState = 1;
//    zhf  
      m_nButtonState[buttons1_len-1] = 0;
      changeButtonState();
    }
  }

  /**
   * �˴����뷽��˵���� ��������:�ڳ��ݹ���ⵥ��Ƭ��ѯ �������: ����ֵ: �쳣����:
   */
  public void onCardQuery() {
    this.showHintMessage("");

    if (!m_bICStartUp) {
      // ���δ����
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000065")/* @res "�ڳ��ݹ���ⵥ��ѯ" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000036")/* @res "���δ���ã���������ⵥ��" */);
      return;
    }

    if (m_condClient1 == null)
      m_condClient1 = initQueryModel1(m_condClient1, m_sUnitCode, false);

    m_condClient1.hideNormal();
    m_condClient1.showModal();

    if (m_condClient1.isCloseOK()) {
      // ��ȡ�ڳ��ݹ���ⵥ��ѯ����
      ConditionVO conditionVO[] = m_condClient1.getConditionVO();

      // ��ѯ
      try {
        long tTime = System.currentTimeMillis();
        m_bGenerate = false;
        m_VOs = EstimateHelper.queryStockForEstimate(m_sUnitCode, conditionVO);
        if (m_VOs == null || m_VOs.length == 0) {
          // m_bBodyQuery = null;
          MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000065")/*
                                                             * @res "�ڳ��ݹ���ⵥ��ѯ"
                                                             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000066")/*
                                     * @res "û�з����������ڳ��ݹ���ⵥ��"
                                     */);

          // �������
          getBillCardPanel1().getBillData().clearViewData();
          getBillCardPanel1().updateValue();
          getBillCardPanel1().updateUI();
          // ���ó�����,��ӡ����ѯ���б�������а�ťΪ��
          for (int i = 0; i < 16; i++) {
            m_nButtonState[i] = 1;
          }
          m_nButtonState[0] = 0;
          m_nButtonState[6] = 0;
          m_nButtonState[11] = 0;
          m_nButtonState[12] = 0;
          m_nButtonState[13] = 0;

          m_nUIState = 1;
          changeButtonState();
          return;
        }
        tTime = System.currentTimeMillis() - tTime;
        SCMEnv.out("�ڳ��ݹ���ⵥ��ѯʱ�䣺" + tTime + " ms!");

        // ��ʾ��ѯ���
        m_nPresentRecord = 0;

        // m_bBodyQuery = new UFBoolean[m_VOs.length];
        // for(int i = 0; i < m_VOs.length; i++){
        // if(i == 0) m_bBodyQuery[i] = new UFBoolean(true);
        // else m_bBodyQuery[i] = new UFBoolean(false);
        // }

        getBillCardPanel1().setBillValueVO(m_VOs[0]);
        getBillCardPanel1().getBillModel().execLoadFormula();
        
        // ������Դ���ݺ�
        nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");
        getBillCardPanel1().updateValue();
        getBillCardPanel1().updateUI();
        // ��ʾ��ע
        UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem(
            "vnote").getComponent();
        // ��ֹ���ɱ༭ʱ,����������ʧ
        nRefPanel1.setAutoCheck(false);
        nRefPanel1.setValue(m_VOs[0].getHeadVO().getVnote());
        
        // ��ʾ�����֯
        setCalbodyValue();

        // ���ó�����,ȡ��,ɾ�������а�ťΪ����
        for (int i = 0; i < 16; i++) {
          m_nButtonState[i] = 0;
        }
        for (int i = 2; i < 5; i++)
          m_nButtonState[i] = 1;
        if (m_nPresentRecord == 0) {
          for (int i = 7; i < 9; i++)
            m_nButtonState[i] = 1;
        }
        if (m_nPresentRecord == m_VOs.length - 1) {
          for (int i = 9; i < 11; i++)
            m_nButtonState[i] = 1;
        }
        if (m_VOs.length == 1) {
          for (int i = 7; i < 11; i++)
            m_nButtonState[i] = 1;
        }
        // �ݹ�Ӧ��ʱ,�������޸ģ�Ҳ�Ͳ������в���
        if (m_bZGYF.booleanValue()) {
          m_nButtonState[1] = 1;
        }
        m_nUIState = 1;
        
//        zhf add
        m_nButtonState[buttons1_len-1] = 0;
        changeButtonState();
      }
      catch (java.sql.SQLException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000065")/*
                                                           * @res "�ڳ��ݹ���ⵥ��ѯ"
                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
            "UPPSCMCommon-000412")/*
                                   * @res "SQL������"
                                   */);
        SCMEnv.out(e);
        return;
      }
      catch (ArrayIndexOutOfBoundsException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000065")/*
                                                           * @res "�ڳ��ݹ���ⵥ��ѯ"
                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
            "UPPSCMCommon-000426")/*
                                   * @res "����Խ�����"
                                   */);
        SCMEnv.out(e);
        return;
      }
      catch (NullPointerException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000065")/*
                                                           * @res "�ڳ��ݹ���ⵥ��ѯ"
                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
            "UPPSCMCommon-000427")/*
                                   * @res "��ָ�����"
                                   */);
        SCMEnv.out(e);
        return;
      }
      catch (Exception e) {
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
                "UPP40040503-000065")/* @res "�ڳ��ݹ���ⵥ��ѯ" */, e.getMessage());
        SCMEnv.out(e);
        return;
      }
    }
  }

  /**
   * �˴����뷽��˵���� ��������:�˳�ϵͳ �������: ����ֵ: �쳣����:
   * 
   * @return boolean
   */
  public boolean onClosing() {
    if (m_bSettling) {
      // �����Ѽ���
      int nReturn = MessageDialog.showYesNoCancelDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050302",
              "UPP4004050302-000008")/* @res "�˳��ڳ��ݹ�����" */, nc.ui.ml.NCLangRes
              .getInstance().getStrByID("common", "UCH001")/*
                                                             * @res
                                                             * "�Ƿ񱣴����޸ĵ����ݣ�"
                                                             */);
      if (nReturn == MessageDialog.ID_YES) {
        boolean bExistSucc = onSave();
        if (m_bSettling) {
          m_bSettling = false;
        }
        return bExistSucc;
      }
      else if (nReturn == MessageDialog.ID_NO) {
        m_bSettling = false;
        return true;
      }
      else {
        return false;
      }
    }
    m_bSettling = false;
    return true;
  }

  /**
   * �˴����뷽��˵���� ��������:ɾ�� �������: ����ֵ: �쳣����:
   */
  public void onDeleteLine() {
    if (!getBillCardPanel1().delLine()) {
      MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("common", "UC001-0000013")/* @res "ɾ��" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000067")/* @res "û��ѡ���ڳ��ݹ��У�" */);
    }

    // ���水ť״̬
    // for(int i=0;i<m_nButtonState.length;i++)
    // m_nRestoreState[i]=m_nButtonState[i];
  }

  /**
   * ��������:�ڳ��ݹ����� ������ ���أ� ���ߣ��ܺ��� ������2001-6-22 14:41:50 �޸ģ���־ƽ FOR V30
   */
  public void onDiscard() {
    this.showHintMessage("");
    Vector vecNoSelected = new Vector();
    Integer nSelected[] = null;
    boolean b = getBillCardPanel1().isShowing();
    if (!b) {
      Vector v = new Vector();
      int nRow = getBillListPanel().getHeadBillModel().getRowCount();
      for (int i = 0; i < nRow; i++) {
        int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
        if (nStatus == BillModel.SELECTED)
          v.addElement(new Integer(i));
        else
          vecNoSelected.addElement(new Integer(i));
      }
      nSelected = new Integer[v.size()];
      v.copyInto(nSelected);
    }
    else {
      nSelected = new Integer[1];
      nSelected[0] = new Integer(m_nPresentRecord);
    }

    if (nSelected == null || nSelected.length == 0) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000068")/* @res "�ڳ��ݹ�����" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000069")/* @res "δѡ���ڳ��ݹ���ⵥ��" */);
      return;
    }

    if (MessageDialog.showYesNoDlg(this, nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("4004050302", "UPP4004050302-000010")/*
                                                           * @res "�ڳ��ݹ�����"
                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH002")/*
                                                                         * @res
                                                                         * "ȷ�������ڳ��ݹ���"
                                                                         */) != MessageDialog.ID_YES)
      return;

    try {
      // ��õ��ݱ���
      // if (m_bBodyQuery != null && m_bBodyQuery.length > 0) {
      int iCnt = nSelected.length;
      Vector v1 = new Vector();
      Vector v2 = new Vector();
      for (int i = 0; i < iCnt; i++) {
        // int nStatus =
        // getBillListPanel().getHeadBillModel().getRowState(i);
        if (isNoBodys(m_VOs[nSelected[i].intValue()])) {
          v1.addElement(m_VOs[nSelected[i].intValue()].getHeadVO()
              .getCgeneralhid());
          v2.addElement(m_VOs[nSelected[i].intValue()].getHeadVO().getTs());
        }
      }
      if (!m_bGenerate && v1.size() > 0 && v2.size() > 0) {
        String headKey[] = new String[v1.size()];
        v1.copyInto(headKey);
        String ts[] = new String[v2.size()];
        v2.copyInto(ts);

        ArrayList list = EstimateHelper.queryInitialBodyBatch(headKey, ts);
        if (list == null || list.size() == 0)
          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000070")/*
                                                             * @res
                                                             * "����������������ˢ�½�������ԣ�"
                                                             */);
        int j = 0;
        for (int i = 0; i < iCnt; i++) {
          // int nStatus =
          // getBillListPanel().getHeadBillModel().getRowState(i);
          if (isNoBodys(m_VOs[nSelected[i].intValue()])) {
            ArrayList list0 = (ArrayList) list.get(j);
            GeneralHItemVO tempBodyVO[] = (GeneralHItemVO[]) list0.get(0);
            GeneralBb3VO tempBb3VO[] = (GeneralBb3VO[]) list0.get(1);
            m_VOs[nSelected[i].intValue()].setChildrenVO(tempBodyVO);
            m_VOs[nSelected[i].intValue()].setGrandVO(tempBb3VO);
            // m_bBodyQuery[i] = new UFBoolean(true);
            j++;
          }
        }
      }
      // }
    }
    catch (BusinessException e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000045")/* @res "��������" */, e
          .getMessage());
      return;
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }

    long tTime = System.currentTimeMillis();
    Vector vTemp = new Vector();
    StringBuffer sMessage = new StringBuffer();
    for (int i = 0; i < nSelected.length; i++) {
      GeneralHVO vo = m_VOs[nSelected[i].intValue()];
      vTemp.addElement(vo);

      // int nBillStatus=vo.getHeadVO().getDr().intValue();
      // if(nBillStatus>0) sMessage.append(vo.getHeadVO().getVbillcode()+"
      // ���ڳ��ݹ���ⵥ�Ѿ����ϣ�\n");
      // if(isSettled(vo)) sMessage.append(vo.getHeadVO().getVbillcode()+"
      // ���ڳ��ݹ���ⵥ�Ѿ����㣡\n");
    }

    // ��������Ѿ����ϣ���������
    if (sMessage.length() > 0) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000068")/* @res "�ڳ��ݹ�����" */,
          sMessage.toString());
      return;
    }

    GeneralHVO VOs[] = new GeneralHVO[vTemp.size()];
    vTemp.copyInto(VOs);

    try {
      java.util.ArrayList listPara = new java.util.ArrayList();
      listPara.add(VOs);
      listPara.add(getClientEnvironment().getUser().getPrimaryKey());
      listPara.add(getCorpPrimaryKey());

      EstimateHelper.discard(listPara);

      // ���ϳɹ����޸ĵ���״̬
      for (int i = 0; i < nSelected.length; i++) {
        m_VOs[nSelected[i].intValue()].getHeadVO().setDr(new Integer(1));
        GeneralHItemVO bodyVO[] = m_VOs[nSelected[i].intValue()].getBodyVO();
        for (int j = 0; j < bodyVO.length; j++)
          bodyVO[j].setDr(new Integer(1));
      }
    }
    catch (nc.vo.pub.BusinessException e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000068")/* @res "�ڳ��ݹ�����" */, e
          .getMessage());
      return;
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000068")/* @res "�ڳ��ݹ�����" */, e
          .getMessage());
      return;
    }

    tTime = System.currentTimeMillis() - tTime;
    SCMEnv.out("�ڳ��ݹ�����ʱ�䣺" + tTime + " ms!");
    this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "40040503", "UPP40040503-000071")/* @res "���ϳɹ���" */);

    // ���Ϻ���ڳ��ݹ���ⵥ������ʾ�ڽ���
    if (!b) {
      // �б�
      Vector v0 = new Vector();
      Vector vTemp1 = new Vector();
      for (int i = 0; i < vecNoSelected.size(); i++) {
        int n = ((Integer) vecNoSelected.elementAt(i)).intValue();
        v0.addElement(m_VOs[n]);
        // vTemp1.addElement(m_bBodyQuery[n]);
      }

      if (v0 == null || v0.size() == 0) {
        // ���е������������
        m_VOs = null;
        // m_bBodyQuery = null;
        getBillListPanel().getHeadBillModel().clearBodyData();
        getBillListPanel().getBodyBillModel().clearBodyData();
        getBillListPanel().updateUI();
        // ����ѯ,�л�������Ϊ��
        for (int i = 0; i < 8; i++)
          m_nButtonState[i] = 1;
        for (int i = 3; i < 5; i++)
          m_nButtonState[i] = 0;
        m_nUIState = 3;
        changeButtonState();
        return;
      }

      // ���ֵ������������
      m_VOs = new GeneralHVO[v0.size()];
      v0.copyInto(m_VOs);
      // m_bBodyQuery = new UFBoolean[vTemp1.size()];
      // vTemp1.copyInto(m_bBodyQuery);

      //
      // ��ø��ŵ��ݵı���
      // if (m_bBodyQuery != null && m_bBodyQuery.length > 0 &&
      // (!m_bBodyQuery[0].booleanValue())) {
      if (isNoBodys(m_VOs[0])) {
        try {
          ArrayList list = EstimateHelper.queryInitialBody(m_VOs[0].getHeadVO()
              .getCgeneralhid(), m_VOs[0].getHeadVO().getTs());
          if (list == null || list.size() == 0)
            throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
                .getStrByID("40040503", "UPP40040503-000070")/*
                                                               * @res
                                                               * "����������������ˢ�½�������ԣ�"
                                                               */);
          GeneralHItemVO tempBodyVO[] = (GeneralHItemVO[]) list.get(0);
          GeneralBb3VO tempBb3VO[] = (GeneralBb3VO[]) list.get(1);
          m_VOs[0].setChildrenVO(tempBodyVO);
          m_VOs[0].setGrandVO(tempBb3VO);
        }
        catch (BusinessException e) {
          SCMEnv.out(e);
          MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000045")/* @res "��������" */,
              e.getMessage());
          return;
        }
        catch (Exception e) {
          SCMEnv.out(e);
        }
        // m_bBodyQuery[0] = new UFBoolean(true);
      }

      Vector v1 = new Vector();
      for (int i = 0; i < m_VOs.length; i++)
        v1.addElement(m_VOs[i].getHeadVO());
      GeneralHHeaderVO head[] = new GeneralHHeaderVO[v1.size()];
      v1.copyInto(head);

      GeneralHItemVO body[] = m_VOs[0].getBodyVO();

      getBillListPanel().getHeadBillModel().setBodyDataVO(head);
      getBillListPanel().getHeadBillModel().execLoadFormula();
      getBillListPanel().getBodyBillModel().setBodyDataVO(body);
      getBillListPanel().getBodyBillModel().execLoadFormula();
      getBillListPanel().getHeadBillModel().updateValue();
      getBillListPanel().getBodyBillModel().updateValue();
      getBillListPanel().updateUI();

      // ��ʾ��ע
      for (int i = 0; i < head.length; i++) {
        getBillListPanel().getHeadBillModel().setValueAt(head[i].getVnote(), i,
            "vnote");
      }

      // ��ȫ��,���Ϻ��л�����������
      for (int i = 0; i < 8; i++)
        m_nButtonState[i] = 0;
      m_nButtonState[1] = 1;
      m_nButtonState[2] = 1;
      m_nButtonState[4] = 1;
      m_nButtonState[6] = 1;
      m_nButtonState[7] = 1;

      m_nUIState = 3;
      changeButtonState();
    }
    else {
      // ��Ƭ
      Vector v0 = new Vector();
      Vector vTemp1 = new Vector();
      for (int i = 0; i < m_VOs.length; i++) {
        if (i != m_nPresentRecord) {
          v0.addElement(m_VOs[i]);
          // vTemp1.addElement(m_bBodyQuery[i]);
        }
      }
      if (v0.size() > 0) {
        // ���ֵ������������
        if (m_nPresentRecord == m_VOs.length - 1)
          m_nPresentRecord--;
        m_VOs = new GeneralHVO[v0.size()];
        v0.copyInto(m_VOs);
        // m_bBodyQuery = new UFBoolean[vTemp1.size()];
        // vTemp1.copyInto(m_bBodyQuery);

        // ��ø��ŵ��ݵı���
        // if (m_bBodyQuery != null && m_bBodyQuery.length > 0 &&
        // (!m_bBodyQuery[m_nPresentRecord].booleanValue())) {
        if (!m_bGenerate && isNoBodys(m_VOs[m_nPresentRecord])) {
          // m_bBodyQuery[m_nPresentRecord] = new UFBoolean(true);
          try {
            ArrayList list = EstimateHelper.queryInitialBody(
                m_VOs[m_nPresentRecord].getHeadVO().getCgeneralhid(),
                m_VOs[m_nPresentRecord].getHeadVO().getTs());
            if (list == null || list.size() == 0)
              throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
                  .getStrByID("40040503", "UPP40040503-000070")/*
                                                                 * @res
                                                                 * "����������������ˢ�½�������ԣ�"
                                                                 */);
            GeneralHItemVO tempBodyVO[] = (GeneralHItemVO[]) list.get(0);
            GeneralBb3VO tempBb3VO[] = (GeneralBb3VO[]) list.get(1);
            m_VOs[m_nPresentRecord].setChildrenVO(tempBodyVO);
            m_VOs[m_nPresentRecord].setGrandVO(tempBb3VO);
          }
          catch (BusinessException e) {
            SCMEnv.out(e);
            MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
                .getStrByID("40040503", "UPP40040503-000045")/*
                                                               * @res "��������"
                                                               */, e.getMessage());
            return;
          }
          catch (Exception e) {
            SCMEnv.out(e);
          }
        }

        getBillCardPanel1().setBillValueVO(m_VOs[m_nPresentRecord]);
        getBillCardPanel1().getBillModel().execLoadFormula();
        // ��ʾ��ע
        UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem(
            "vnote").getComponent();
        nRefPanel1.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVnote());

        // ���ó�����,ȡ��,ɾ�������а�ťΪ����
        for (int i = 0; i < 16; i++) {
          m_nButtonState[i] = 0;
        }
        for (int i = 2; i < 5; i++)
          m_nButtonState[i] = 1;
        if (m_nPresentRecord == 0) {
          m_nButtonState[7] = 1;
          m_nButtonState[8] = 1;
        }
        if (m_nPresentRecord == m_VOs.length - 1) {
          m_nButtonState[9] = 1;
          m_nButtonState[10] = 1;
        }
        if (m_VOs.length == 1) {
          for (int i = 7; i < 11; i++)
            m_nButtonState[i] = 1;
        }
        // �ݹ�Ӧ��ʱ,�������޸ģ�Ҳ�Ͳ������в���
        if (m_bZGYF.booleanValue()) {
          m_nButtonState[1] = 1;
        }
        m_nUIState = 1;
        changeButtonState();
      }
      else {
        // ���е������������
        m_VOs = null;
        // m_bBodyQuery = null;
        getBillCardPanel1().getBillData().clearViewData();
        getBillCardPanel1().updateUI();
        // ������,��ӡ����ѯ���б�������а�ťΪ��
        for (int i = 0; i < 16; i++)
          m_nButtonState[i] = 1;
        m_nButtonState[0] = 0;
        m_nButtonState[6] = 0;
        m_nButtonState[11] = 0;
        m_nButtonState[12] = 0;
        m_nButtonState[13] = 0;
        changeButtonState();
        return;
      }

      getBillCardPanel1().updateValue();
      getBillCardPanel1().updateUI();
      if (m_bGenerate) {
        doRefresh(m_nPresentRecord);
      }
    }
  }

  /**
   * �˴����뷽��˵���� ��������:�ݹ� �������: ����ֵ: �쳣����:
   */
  public void onEstimate() {
    // ���ݶ���������ⵥ
    Integer nSelected[] = null;
    Vector v = new Vector();
    int nRow = getBillCardPanel2().getRowCount();
    for (int i = 0; i < nRow; i++) {
      int nStatus = getBillCardPanel2().getBillModel().getRowState(i);
      if (nStatus == BillModel.SELECTED)
        v.addElement(new Integer(i));
    }

    if (v.size() > 0) {
      nSelected = new Integer[v.size()];
      v.copyInto(nSelected);
    }
    else {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000072")/* @res "����������ⵥ" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000073")/* @res "δѡ�ж����У�" */);
      return;
    }

    // zhf
    updateOorderVO(nSelected);
    // end

    Vector vv = new Vector();
    // // for (int i = 0; i < nSelected.length; i++)
    // // vv.addElement(m_orderVOs[nSelected[i].intValue()]);
    // // m_orderVOs = new OorderVO[vv.size()];
    // // vv.copyInto(m_orderVOs);
    //
    String strErrMnyLines = "";

    for (int i = 0; i < nSelected.length; i++) {
      OorderVO vo = m_orderVOs[nSelected[i].intValue()];
      UFDouble nMoney = vo.getNmoney();
      if (nMoney == null || nMoney.doubleValue() == 0.0) {
        strErrMnyLines += (nSelected[i].intValue() + 1) + "��";
      }
      vv.add(vo);
    }
    // ȥ�����һ����������
    if (strErrMnyLines.length() > 0) {
      strErrMnyLines = strErrMnyLines.substring(0, strErrMnyLines.length() - 1);
    }

    // �����ⵥ���������Ϊ�ջ�Ϊ�㣬��ⵥ���ܽ����ݹ�����
    if (strErrMnyLines.length() > 0) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID(
          "40040503", "UPP40040503-000033"/* @res "�ݹ����� */), NCLangRes
          .getInstance().getStrByID("40040503", "UPP40040503-000032"
          /* @res "������ⵥ�е��ݹ����Ϊ0�������ݹ��������Ϊ��{0}" */, null, new String[] {
            strErrMnyLines
          }));
      return;
    }
    //
    m_bSettling = true;

    OorderVO[] tempOrderVOs = new OorderVO[vv.size()];
    if (vv != null && vv.size() > 0)
      vv.copyInto(tempOrderVOs);
    generateStock(tempOrderVOs);

    // �ݹ������������б��ݣ��б���ʾ���������ɵ���ⵥ
    this.remove(getBillCardPanel2());
    setLayout(new java.awt.BorderLayout());
    add(getBillListPanel(), java.awt.BorderLayout.CENTER);
    this.setButtons(m_buttons31);
    m_nButtonState = new int[m_buttons3.length];

    // ����cbiztype,cproviderid,cdptid,cbizid,cwarehouseid,cwhsmanagerid,cdispatcherid
    getBillListPanel().hideHeadTableCol("cbiztype");
    getBillListPanel().hideHeadTableCol("cproviderid");
    getBillListPanel().hideHeadTableCol("cdptid");
    getBillListPanel().hideHeadTableCol("cbizid");
    getBillListPanel().hideHeadTableCol("cwarehouseid");
    getBillListPanel().hideHeadTableCol("cwhsmanagerid");
    getBillListPanel().hideHeadTableCol("cstoreorganization");
    getBillListPanel().hideHeadTableCol("pk_purcorp");
    getBillListPanel().hideHeadTableCol("cdispatcherid");

    // // û�������ڳ��ݹ���ⵥ
    if (m_gVOs == null || m_gVOs.length == 0) {
      // // ���ó���ѯ���л������а�ťΪ��
      // for (int i = 0; i < 8; i++) {
      // m_nButtonState[i] = 1;
      // }
      // m_nButtonState[3] = 0;
      // m_nButtonState[4] = 0;
      // m_nUIState = 3;
      // changeButtonState();
      //
      setListBtnsStateEditable();
      return;
    }

    // �����ڳ��ݹ���ⵥ
    // �����ɵ��ݹ���ⵥ�����к�
    BillRowNo.setVOsRowNoByRule(m_gVOs, "4T", "crowno");

    // �����н���ԭ������ⵥ�������ɵ���ⵥ���Ա�༭�����ɵ���ⵥ
    Vector v1 = new Vector();
    if (m_VOs != null && m_VOs.length > 0) {
      for (int i = 0; i < m_VOs.length; i++)
        v1.addElement(m_VOs[i]);
    }
    Vector v2 = new Vector();
    for (int i = 0; i < m_gVOs.length; i++)
      v2.addElement(m_gVOs[i]);

    m_VOs = new GeneralHVO[v2.size()];
    v2.copyInto(m_VOs);
    m_gVOs = new GeneralHVO[v1.size()];
    v1.copyInto(m_gVOs);
    m_nPresentRecord = 0;

    // ��ʾ�б�
    Vector v0 = new Vector();
    for (int i = 0; i < m_VOs.length; i++)
      v0.addElement(m_VOs[i].getHeadVO());
    GeneralHHeaderVO head[] = new GeneralHHeaderVO[v0.size()];
    v0.copyInto(head);

    GeneralHItemVO body[] = m_VOs[0].getBodyVO();

    m_nUIState = 3;

    getBillListPanel().getHeadBillModel().setBodyDataVO(head);
    getBillListPanel().getHeadBillModel().execLoadFormula();
    getBillListPanel().getBodyBillModel().setBodyDataVO(body);
    getBillListPanel().getBodyBillModel().execLoadFormula();
    getBillListPanel().getHeadBillModel().updateValue();
    getBillListPanel().getBodyBillModel().updateValue();
    getBillListPanel().updateUI();
    // ��ʾ��Դ������Ϣ
    nc.ui.pu.pub.PuTool.loadSourceBillData(getBillListPanel(), "4T");
    //Ĭ��ѡ���һ��
    getBillListPanel().getHeadBillModel().setRowState(0, BillModel.SELECTED);
    //
    setListBtnsStateEditable();
  }

  /**
   * �˴����뷽��˵���� ��������:�ļ����� �������: ����ֵ: �쳣����: ����:2003/03/07
   */
  private void onFileManage() {
    Integer nSelected[] = null;
    boolean b = getBillCardPanel1().isVisible();
    if (!b) {
      Vector v = new Vector();
      int nRow = getBillListPanel().getHeadBillModel().getRowCount();
      for (int i = 0; i < nRow; i++) {
        int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
        if (nStatus == BillModel.SELECTED)
          v.addElement(new Integer(i));
      }
      nSelected = new Integer[v.size()];
      v.copyInto(nSelected);
    }
    else {
      nSelected = new Integer[1];
      nSelected[0] = new Integer(m_nPresentRecord);
    }

    if (nSelected == null || nSelected.length == 0)
      return;

    String sBillID[] = new String[nSelected.length];
    String sBillCode[] = new String[nSelected.length];
    for (int i = 0; i < sBillID.length; i++) {
      int j = nSelected[i].intValue();
      if (j >= 0 && j < m_VOs.length) {
        sBillID[i] = m_VOs[j].getHeadVO().getCgeneralhid();
        sBillCode[i] = m_VOs[j].getHeadVO().getVbillcode();
      }
    }

    if (sBillID != null && sBillID.length > 0) {
      nc.ui.scm.file.DocumentManager.showDM(this,"4T", sBillID);
    }
  }

  /**
   * �˴����뷽��˵���� ��������:��ҳ �������: ����ֵ: �쳣����:
   */
  public void onFirst() {
    this.showHintMessage("");
    //getBillCardPanel1().getBillModel().clearBodyData();
    //m_nPresentRecord = 0;
    m_nButtonState[7] = 1;
    m_nButtonState[8] = 1;
    m_nButtonState[9] = 0;
    m_nButtonState[10] = 0;
    int tempInx=m_nPresentRecord;
    int firstInx=0;
    while(--tempInx>=0){
        if(PuPubVO.getString_TrimZeroLenAsNull(m_VOs[tempInx].getHeadVO().getCgeneralhid())!=null)
          firstInx=tempInx;
    }
    if(tempInx>=0)
      m_nPresentRecord=firstInx;
    else
      return;
    changeButtonState();
    getBillCardPanel1().getBillModel().clearBodyData();


    // ��ø��ŵ��ݵı���
    // if(m_bBodyQuery != null && m_bBodyQuery.length > 0 &&
    // (!m_bBodyQuery[m_nPresentRecord].booleanValue())){
    if (isNoBodys(m_VOs[m_nPresentRecord])) {
      try {
        ArrayList list = EstimateHelper.queryInitialBody(
            m_VOs[m_nPresentRecord].getHeadVO().getCgeneralhid(),
            m_VOs[m_nPresentRecord].getHeadVO().getTs());
        if (list == null || list.size() == 0)
          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("4004050302", "UPP4004050302-000004")/*
                                                                 * @res
                                                                 * "����������������ˢ�½�������ԣ�"
                                                                 */);
        GeneralHItemVO tempBodyVO[] = (GeneralHItemVO[]) list.get(0);
        GeneralBb3VO tempBb3VO[] = (GeneralBb3VO[]) list.get(1);
        m_VOs[m_nPresentRecord].setChildrenVO(tempBodyVO);
        m_VOs[m_nPresentRecord].setGrandVO(tempBb3VO);
      }
      catch (BusinessException e) {
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000045")/* @res "��������" */, e
            .getMessage());
        return;
      }
      catch (Exception e) {
        SCMEnv.out(e);
      }
      // m_bBodyQuery[m_nPresentRecord] = new UFBoolean(true);
    }

    getBillCardPanel1().setBillValueVO(m_VOs[0]);
    getBillCardPanel1().getBillModel().execLoadFormula();
    getBillCardPanel1().updateValue();
    getBillCardPanel1().updateUI();
    // ��ʾ��Դ������Ϣ
    nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");

    // ��ʾ��ע
    UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem("vnote")
        .getComponent();
    nRefPanel1.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVnote());

    // ��ʾ�����֯
    setCalbodyValue();

  }

  /**
   * �˴����뷽��˵���� ��������:ĩҳ �������: ����ֵ: �쳣����:
   */
  public void onLast() {
    this.showHintMessage("");
    //getBillCardPanel1().getBillModel().clearBodyData();
    //m_nPresentRecord = m_VOs.length - 1;
    m_nButtonState[7] = 0;
    m_nButtonState[8] = 0;
    m_nButtonState[9] = 1;
    m_nButtonState[10] = 1;

    int tempInx=m_nPresentRecord;
    int lastInx=m_VOs.length;
    while(++tempInx<m_VOs.length){ 
        if(PuPubVO.getString_TrimZeroLenAsNull(m_VOs[tempInx].getHeadVO().getCgeneralhid())!=null)
          lastInx=tempInx;
    }
    if(tempInx<m_VOs.length)
      m_nPresentRecord=lastInx;
    else
      return;
    
    changeButtonState();
    getBillCardPanel1().getBillModel().clearBodyData();


    // ��ø��ŵ��ݵı���
    // if(m_bBodyQuery != null && m_bBodyQuery.length > 0 &&
    // (!m_bBodyQuery[m_nPresentRecord].booleanValue())){
    if (isNoBodys(m_VOs[m_nPresentRecord])) {
      try {
        ArrayList list = EstimateHelper.queryInitialBody(
            m_VOs[m_nPresentRecord].getHeadVO().getCgeneralhid(),
            m_VOs[m_nPresentRecord].getHeadVO().getTs());
        if (list == null || list.size() == 0)
          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("4004050302", "UPP4004050302-000004")/*
                                                                 * @res
                                                                 * "����������������ˢ�½�������ԣ�"
                                                                 */);
        GeneralHItemVO tempBodyVO[] = (GeneralHItemVO[]) list.get(0);
        GeneralBb3VO tempBb3VO[] = (GeneralBb3VO[]) list.get(1);
        m_VOs[m_nPresentRecord].setChildrenVO(tempBodyVO);
        m_VOs[m_nPresentRecord].setGrandVO(tempBb3VO);
      }
      catch (BusinessException e) {
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000045")/* @res "��������" */, e
            .getMessage());
        return;
      }
      catch (Exception e) {
        SCMEnv.out(e);
      }
      // m_bBodyQuery[m_nPresentRecord] = new UFBoolean(true);
    }

    getBillCardPanel1().setBillValueVO(m_VOs[m_nPresentRecord]);
    getBillCardPanel1().getBillModel().execLoadFormula();
    getBillCardPanel1().updateValue();
    getBillCardPanel1().updateUI();
    // ��ʾ��Դ������Ϣ
    nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");

    // ��ʾ��ע
    UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem("vnote")
        .getComponent();
    nRefPanel1.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVnote());

    // ��ʾ�����֯
    setCalbodyValue();

  }

  /**
   * �˴����뷽��˵���� ��������:��Ƭ���б� �������: ����ֵ: �쳣����:
   */
  public void onList() {
    this.showHintMessage("");
    getBillCardPanel1().setVisible(false);
    getBillCardPanel2().setVisible(false);
    setLayout(new java.awt.BorderLayout());
    add(getBillListPanel(), java.awt.BorderLayout.CENTER);
    this.setButtons(m_buttons31);
    m_nButtonState = new int[m_buttons3.length];

    // ����cbiztype,cproviderid,cdptid,cbizid,cwarehouseid,cwhsmanagerid,cdispatcherid
    getBillListPanel().hideHeadTableCol("cbiztype");
    getBillListPanel().hideHeadTableCol("cproviderid");
    getBillListPanel().hideHeadTableCol("cdptid");
    getBillListPanel().hideHeadTableCol("cbizid");
    getBillListPanel().hideHeadTableCol("cwarehouseid");
    getBillListPanel().hideHeadTableCol("cwhsmanagerid");
    getBillListPanel().hideHeadTableCol("cstoreorganization");
    getBillListPanel().hideHeadTableCol("pk_purcorp");
    getBillListPanel().hideHeadTableCol("cdispatcherid");

    if (m_VOs != null && m_VOs.length > 0) {
      // ��ʾ��Ƭ״̬�ĵ�ǰ����
      Vector v1 = new Vector();
      for (int i = 0; i < m_VOs.length; i++)
        v1.addElement(m_VOs[i].getHeadVO());
      GeneralHHeaderVO head[] = new GeneralHHeaderVO[v1.size()];
      v1.copyInto(head);

      GeneralHItemVO body[] = m_VOs[m_nPresentRecord].getBodyVO();

      getBillListPanel().getHeadBillModel().setBodyDataVO(head);
      getBillListPanel().getHeadBillModel().execLoadFormula();
      getBillListPanel().getBodyBillModel().setBodyDataVO(body);
      getBillListPanel().getBodyBillModel().execLoadFormula();
      getBillListPanel().getHeadBillModel().updateValue();
      getBillListPanel().getBodyBillModel().updateValue();
      // ��ʾ��Դ������Ϣ
      nc.ui.pu.pub.PuTool.loadSourceBillData(getBillListPanel(), "4T");
      getBillListPanel().updateUI();

      getBillListPanel().getHeadBillModel().setRowState(m_nPresentRecord,
          BillModel.SELECTED);

      // ��ʾ��ע
      for (int i = 0; i < head.length; i++) {
        getBillListPanel().getHeadBillModel().setValueAt(head[i].getVnote(), i,
            "vnote");
      }

      // �������а�ťΪ����
      for (int i = 0; i < 8; i++) {
        m_nButtonState[i] = 0;
      }
    }
    else {
      // ���ó���ѯ,�л�������а�ťΪ��
      getBillListPanel().getHeadBillModel().clearBodyData();
      getBillListPanel().getBodyBillModel().clearBodyData();
      getBillListPanel().updateUI();

      for (int i = 0; i < 8; i++) {
        m_nButtonState[i] = 1;
      }
      m_nButtonState[3] = 0;
      m_nButtonState[4] = 0;
    }

    m_nUIState = 3;
    changeButtonState();

    // since v53
    if (m_bGenerate) {
      setListBtnsStateEditable();
    }
    else {
      setListBtnsState();
    }
  }

  /**
   * �˴����뷽��˵���� ��������:�б��ӡ �������: ����ֵ: �쳣����: 2003/11/04
   */
  private void onListPreview() {
    //
    this.showHintMessage("");
    Vector vv = new Vector();
    Integer nSelected[] = null;
    Vector v = new Vector();
    int nRow = getBillListPanel().getHeadBillModel().getRowCount();
    for (int i = 0; i < nRow; i++) {
      //
      Object oTemp = getBillListPanel().getHeadBillModel().getValueAt(i,
          "cstoreorganization");
      if (oTemp != null)
        m_VOs[i].getHeadVO().setCstoreorganization(oTemp.toString());
      //
      int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
      if (nStatus == BillModel.SELECTED )
        v.addElement(new Integer(i));
      else
        vv.addElement(new Integer(i));
    }
    nSelected = new Integer[v.size()];
    v.copyInto(nSelected);

    if (nSelected == null || nSelected.length == 0) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000074")/* @res "���δ�ӡ�ڳ��ݹ�" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000069")/* @res "δѡ���ڳ��ݹ���ⵥ��" */);
      return;
    }
    //

    try {
      // ��õ��ݱ���
      // if(m_bBodyQuery != null && m_bBodyQuery.length > 0){
      Vector v1 = new Vector();
      Vector v2 = new Vector();
      for (int i = 0; i < nSelected.length; i++) {
        // int
        // nStatus=getBillListPanel().getHeadBillModel().getRowState(i);
        if (isNoBodys(m_VOs[nSelected[i].intValue()])) {
          v1.addElement(m_VOs[nSelected[i].intValue()].getHeadVO()
              .getCgeneralhid());
          v2.addElement(m_VOs[nSelected[i].intValue()].getHeadVO().getTs());
        }
      }
      if (v1.size() > 0 && v2.size() > 0) {
        String headKey[] = new String[v1.size()];
        v1.copyInto(headKey);
        String ts[] = new String[v2.size()];
        v2.copyInto(ts);

        ArrayList list = EstimateHelper.queryInitialBodyBatch(headKey, ts);
        if (list == null || list.size() == 0)
          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("4004050302", "UPP4004050302-000004")/*
                                                                 * @res
                                                                 * "����������������ˢ�½�������ԣ�"
                                                                 */);
        int j = 0;
        for (int i = 0; i < nSelected.length; i++) {
          // int
          // nStatus=getBillListPanel().getHeadBillModel().getRowState(i);
          if (isNoBodys(m_VOs[nSelected[i].intValue()])) {
            ArrayList list0 = (ArrayList) list.get(j);
            GeneralHItemVO tempBodyVO[] = (GeneralHItemVO[]) list0.get(0);
            GeneralBb3VO tempBb3VO[] = (GeneralBb3VO[]) list0.get(1);
            m_VOs[nSelected[i].intValue()].setChildrenVO(tempBodyVO);
            m_VOs[nSelected[i].intValue()].setGrandVO(tempBb3VO);
            // m_bBodyQuery[i] = new UFBoolean(true);
            j++;
          }
        }
      }
      // }
    }
    catch (BusinessException e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000045")/* @res "��������" */, e
          .getMessage());
      return;
    }
    catch (Exception e) {
      SCMEnv.out(e);
      return;
    }

    java.util.ArrayList list = new java.util.ArrayList();
    for (int i = 0; i < nSelected.length; i++) {
      GeneralHVO vo = m_VOs[nSelected[i].intValue()];
      list.add(vo);
    }

    if (list.size() > 0) {
      // if(m_printList == null){
      // m_printList = new ScmPrintTool(getBillCardPanel1());
      // }
      // m_printList.setData(list);
      // m_printList.preview();

      if (m_printList == null) {
        m_printList = new ScmPrintTool(this, getBillCardPanel1(), list,
            getModuleCode());
      }
      else {
        try {
          m_printList.setData(list);
        }
        catch (Exception e1) {
          SCMEnv.out(e1);
          return;
        }
      }
      try {
        m_printList.onBatchPrintPreview(getBillListPanel(),
            getBillCardPanel1(), "4T");
        if (PuPubVO.getString_TrimZeroLenAsNull(m_printList.getPrintMessage()) != null) {
          MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */,
              m_printList.getPrintMessage());
        }
      }
      catch (Exception e) {
        SCMEnv.out(e);
      }
    }
  }

  /**
   * �˴����뷽��˵���� ��������:�б��ӡ �������: ����ֵ: �쳣����: 2003/11/04
   */
  private void onListPrint() {
    //
    this.showHintMessage("");
    Vector vv = new Vector();
    Integer nSelected[] = null;
    Vector v = new Vector();
    int nRow = getBillListPanel().getHeadBillModel().getRowCount();
    for (int i = 0; i < nRow; i++) {
      //
      Object oTemp = getBillListPanel().getHeadBillModel().getValueAt(i,
          "cstoreorganization");
      if (oTemp != null)
        m_VOs[i].getHeadVO().setCstoreorganization(oTemp.toString());
      //
      int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
      if (nStatus == BillModel.SELECTED)
        v.addElement(new Integer(i));
      else
        vv.addElement(new Integer(i));
    }
    nSelected = new Integer[v.size()];
    v.copyInto(nSelected);

    if (nSelected == null || nSelected.length == 0) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000074")/* @res "���δ�ӡ�ڳ��ݹ�" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000069")/* @res "δѡ���ڳ��ݹ���ⵥ��" */);
      return;
    }
    //

    try {
      // ��õ��ݱ���
      // if(m_bBodyQuery != null && m_bBodyQuery.length > 0){
      Vector v1 = new Vector();
      Vector v2 = new Vector();
      for (int i = 0; i < nSelected.length; i++) {
        // int
        // nStatus=getBillListPanel().getHeadBillModel().getRowState(i);
        if (isNoBodys(m_VOs[nSelected[i].intValue()])) {
          v1.addElement(m_VOs[nSelected[i].intValue()].getHeadVO()
              .getCgeneralhid());
          v2.addElement(m_VOs[nSelected[i].intValue()].getHeadVO().getTs());
        }
      }
      if (v1.size() > 0 && v2.size() > 0) {
        String headKey[] = new String[v1.size()];
        v1.copyInto(headKey);
        String ts[] = new String[v2.size()];
        v2.copyInto(ts);

        ArrayList list = EstimateHelper.queryInitialBodyBatch(headKey, ts);
        if (list == null || list.size() == 0)
          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("4004050302", "UPP4004050302-000004")/*
                                                                 * @res
                                                                 * "����������������ˢ�½�������ԣ�"
                                                                 */);
        int j = 0;
        for (int i = 0; i < nSelected.length; i++) {
          // int
          // nStatus=getBillListPanel().getHeadBillModel().getRowState(i);
          if (isNoBodys(m_VOs[nSelected[i].intValue()])) {
            ArrayList list0 = (ArrayList) list.get(j);
            GeneralHItemVO tempBodyVO[] = (GeneralHItemVO[]) list0.get(0);
            GeneralBb3VO tempBb3VO[] = (GeneralBb3VO[]) list0.get(1);
            m_VOs[nSelected[i].intValue()].setChildrenVO(tempBodyVO);
            m_VOs[nSelected[i].intValue()].setGrandVO(tempBb3VO);
            // m_bBodyQuery[i] = new UFBoolean(true);
            j++;
          }
        }
      }
      // }
    }
    catch (BusinessException e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000045")/* @res "��������" */, e
          .getMessage());
      return;
    }
    catch (Exception e) {
      SCMEnv.out(e);
      return;
    }

    java.util.ArrayList list = new java.util.ArrayList();
    for (int i = 0; i < nSelected.length; i++) {
      GeneralHVO vo = m_VOs[nSelected[i].intValue()];
      list.add(vo);
    }

    if (list.size() > 0) {
      // if(m_printList == null){
      // m_printList = new ScmPrintTool(getBillCardPanel1());
      // }
      // m_printList.setData(list);
      // m_printList.print();

      if (m_printList == null) {
        m_printList = new ScmPrintTool(null, getBillCardPanel1(), list,
            getModuleCode());
      }
      else {
        try {
          m_printList.setData(list);
        }
        catch (Exception e1) {
          SCMEnv.out(e1);
          return;
        }
      }
      try {
        m_printList.onBatchPrint(getBillListPanel(), getBillCardPanel1(), "4T");
        if (PuPubVO.getString_TrimZeroLenAsNull(m_printList.getPrintMessage()) != null) {
          MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */,
              m_printList.getPrintMessage());
        }
      }
      catch (Exception e) {
        SCMEnv.out(e);
      }
    }
  }

  /**
   * �˴����뷽��˵���� ��������:�޸� �������: ����ֵ: �쳣����:
   */
  public void onModify() {
    this.showHintMessage("");
    if (isSettled(m_VOs[m_nPresentRecord])) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000075")/* @res "�޸��ڳ��ݹ���ⵥ" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000076")/* @res "��ⵥ�Ѿ����㣬�����޸ģ�" */);
      return;
    }

    getBillCardPanel1().setEnabled(false);
    setPartNoEditable();

    // ��ⵥ�Ų��ɱ༭
    BillItem item = getBillCardPanel1().getHeadItem("vbillcode");
    item.setEnabled(false);

    m_bAdd = false;
    // ���水ť״̬
    for (int i = 0; i < m_nButtonState.length; i++)
      m_nRestoreState[i] = m_nButtonState[i];

    // ���ó����棬ɾ��,����,��ӡ�����а�ťΪ��
    for (int i = 0; i < 16; i++) {
      m_nButtonState[i] = 1;
    }
    for (int i = 2; i < 5; i++)
      m_nButtonState[i] = 0;
    m_nButtonState[12] = 0;
    m_nButtonState[13] = 0;
    m_nUIState = 1;
    changeButtonState();
  }

  /**
   * �˴����뷽��˵���� ��������:��ҳ �������: ����ֵ: �쳣����:
   */
  public void onNext() {
    this.showHintMessage("");

    if (m_nPresentRecord == m_VOs.length - 2) {
      m_nButtonState[9] = 1;
      m_nButtonState[10] = 1;
    }
    m_nButtonState[7] = 0;
    m_nButtonState[8] = 0;
    //m_nPresentRecord++;
    int tempInx=m_nPresentRecord;
    while(++tempInx<m_VOs.length &&
        PuPubVO.getString_TrimZeroLenAsNull(m_VOs[tempInx].getHeadVO().getCgeneralhid())==null);
    if(tempInx<m_VOs.length)
      m_nPresentRecord=tempInx;
    else
      return;
    
//  zhf  
    m_nButtonState[buttons1_len-1] = 1;
    
    changeButtonState();
    getBillCardPanel1().getBillModel().clearBodyData();

    // ��ø��ŵ��ݵı���
    // if(m_bBodyQuery != null && m_bBodyQuery.length > 0 &&
    // (!m_bBodyQuery[m_nPresentRecord].booleanValue())){
    if (isNoBodys(m_VOs[m_nPresentRecord])) {
      try {
        ArrayList list = EstimateHelper.queryInitialBody(
            m_VOs[m_nPresentRecord].getHeadVO().getCgeneralhid(),
            m_VOs[m_nPresentRecord].getHeadVO().getTs());
        if (list == null || list.size() == 0)
          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("4004050302", "UPP4004050302-000004")/*
                                                                 * @res
                                                                 * "����������������ˢ�½�������ԣ�"
                                                                 */);
        GeneralHItemVO tempBodyVO[] = (GeneralHItemVO[]) list.get(0);
        GeneralBb3VO tempBb3VO[] = (GeneralBb3VO[]) list.get(1);
        m_VOs[m_nPresentRecord].setChildrenVO(tempBodyVO);
        m_VOs[m_nPresentRecord].setGrandVO(tempBb3VO);
      }
      catch (BusinessException e) {
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000045")/* @res "��������" */, e
            .getMessage());
        return;
      }
      catch (Exception e) {
        SCMEnv.out(e);
      }
      // m_bBodyQuery[m_nPresentRecord] = new UFBoolean(true);
    }

    getBillCardPanel1().setBillValueVO(m_VOs[m_nPresentRecord]);
    getBillCardPanel1().getBillModel().execLoadFormula();
    getBillCardPanel1().updateValue();
    getBillCardPanel1().updateUI();
    // ��ʾ��Դ������Ϣ
    nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");

    // ��ʾ��ע
    UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem("vnote")
        .getComponent();
    nRefPanel1.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVnote());

    // ��ʾ�����֯
    setCalbodyValue();

  }

  /**
   * �˴����뷽��˵���� ��������:��ӡ �������: ����ֵ: �쳣����:
   */
  public void onPreview() {
    this.showHintMessage("");

    // PrintEntry print = new PrintEntry(null,null);
    // print.setTemplateID(m_sUnitCode,"4004050302",getClientEnvironment().getUser().getPrimaryKey(),null);
    // int ret = print.selectTemplate() ;
    // if(ret>0){
    // print.setDataSource(this);
    // print.preview() ;
    // }

    java.util.ArrayList list = new java.util.ArrayList();
    if (m_VOs == null || m_VOs.length == 0)// || m_bGenerate)
      return;
    list.add(m_VOs[m_nPresentRecord]);
    if (m_printCard == null) {
      m_printCard = new ScmPrintTool(this, getBillCardPanel1(), list,
          getModuleCode());
    }
    else {
      try {
        m_printCard.setData(list);
      }
      catch (Exception e1) {
        SCMEnv.out(e1);
        return;
      }
    }

    try {
      m_printCard.onCardPrintPreview(getBillCardPanel1(), getBillListPanel(),
          "4T");
      if (PuPubVO.getString_TrimZeroLenAsNull(m_printCard.getPrintMessage()) != null) {
        MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
            "SCMCOMMON", "UPPSCMCommon-000270")
        /* @res "��ʾ" */, m_printCard.getPrintMessage());
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }

  }

  /**
   * �˴����뷽��˵���� ��������:��ҳ �������: ����ֵ: �쳣����:
   */
  public void onPrevious() {
    this.showHintMessage("");
    //getBillCardPanel1().getBillModel().clearBodyData();
    if (m_nPresentRecord == 1) {
      m_nButtonState[7] = 1;
      m_nButtonState[8] = 1;
    }
    m_nButtonState[9] = 0;
    m_nButtonState[10] = 0;
    //m_nPresentRecord--;
    int tempInx=m_nPresentRecord;
    while(--tempInx>=0 &&
        PuPubVO.getString_TrimZeroLenAsNull(m_VOs[tempInx].getHeadVO().getCgeneralhid())==null);
    if(tempInx>=0)
      m_nPresentRecord=tempInx;
    else
      return;
    changeButtonState();
    getBillCardPanel1().getBillModel().clearBodyData();


    // ��ø��ŵ��ݵı���
    // if(m_bBodyQuery != null && m_bBodyQuery.length > 0 &&
    // (!m_bBodyQuery[m_nPresentRecord].booleanValue())){
    if (isNoBodys(m_VOs[m_nPresentRecord])) {
      try {
        ArrayList list = EstimateHelper.queryInitialBody(
            m_VOs[m_nPresentRecord].getHeadVO().getCgeneralhid(),
            m_VOs[m_nPresentRecord].getHeadVO().getTs());
        if (list == null || list.size() == 0)
          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("4004050302", "UPP4004050302-000004")/*
                                                                 * @res
                                                                 * "����������������ˢ�½�������ԣ�"
                                                                 */);
        GeneralHItemVO tempBodyVO[] = (GeneralHItemVO[]) list.get(0);
        GeneralBb3VO tempBb3VO[] = (GeneralBb3VO[]) list.get(1);
        m_VOs[m_nPresentRecord].setChildrenVO(tempBodyVO);
        m_VOs[m_nPresentRecord].setGrandVO(tempBb3VO);
      }
      catch (BusinessException e) {
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000045")/* @res "��������" */, e
            .getMessage());
        return;
      }
      catch (Exception e) {
        SCMEnv.out(e);
      }
      // m_bBodyQuery[m_nPresentRecord] = new UFBoolean(true);
    }

    getBillCardPanel1().setBillValueVO(m_VOs[m_nPresentRecord]);
    getBillCardPanel1().getBillModel().execLoadFormula();
    // ��ʾ��Դ������Ϣ
    nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");
    getBillCardPanel1().updateValue();
    getBillCardPanel1().updateUI();

    // ��ʾ��ע
    UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem("vnote")
        .getComponent();
    nRefPanel1.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVnote());

    // ��ʾ�����֯
    setCalbodyValue();

  }

  /**
   * �˴����뷽��˵���� ��������:��ӡ �������: ����ֵ: �쳣����:
   */
  public void onPrint() {
    this.showHintMessage("");

    // PrintEntry print = new PrintEntry(null,null);
    // print.setTemplateID(m_sUnitCode,"4004050302",getClientEnvironment().getUser().getPrimaryKey(),null);
    // //print.print();
    // int ret = print.selectTemplate() ;
    // if(ret>0){
    // print.setDataSource(this);
    // print.print() ;
    // }

    java.util.ArrayList list = new java.util.ArrayList();
    if (m_VOs == null || m_VOs.length == 0)// || m_bGenerate)
      return;
    list.add(m_VOs[m_nPresentRecord]);
    if (m_printCard == null) {
      m_printCard = new ScmPrintTool(getBillCardPanel1(), list, getModuleCode());
    }
    else {
      try {
        m_printCard.setData(list);
      }
      catch (Exception e1) {
        SCMEnv.out(e1);
        return;
      }
    }

    try {
      m_printCard.onCardPrint(getBillCardPanel1(), getBillListPanel(), "4T");
      if (PuPubVO.getString_TrimZeroLenAsNull(m_printCard.getPrintMessage()) != null) {
        MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
            "SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */, m_printCard
            .getPrintMessage());
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }
    
  }

  /**
   * ��������:�ڳ��ݹ���ⵥ�б��ѯ �޸ģ�2004-09-10 ԬҰ
   * 
   * @since v53, 2008-04-17, ֧���Զ���� czp
   */
  public void onQueryStock() {
    if (!m_bICStartUp) {
      // ���δ����
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000065")/* @res "�ڳ��ݹ���ⵥ��ѯ" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
          "UPP40040503-000036")/* @res "���δ���ã���������ⵥ��" */);
      return;
    }

    if (m_condClient1 == null) {
      m_condClient1 = initQueryModel1(m_condClient1, m_sUnitCode, false);
    }

    m_condClient1.hideNormal();
    m_condClient1.showModal();

    if (m_condClient1.isCloseOK()) {
      // ��ȡ�ڳ��ݹ���ⵥ��ѯ����
      ConditionVO conditionVO[] = m_condClient1.getConditionVO();

      // ��ѯ
      try {
        long tTime = System.currentTimeMillis();
        m_bGenerate = false;
        m_VOs = EstimateHelper.queryStockForEstimate(m_sUnitCode, conditionVO);
        if (m_VOs == null || m_VOs.length == 0) {
          // m_bBodyQuery = null;
          MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000065")/*
               * @res "�ڳ��ݹ���ⵥ��ѯ"
               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
               "UPP40040503-000066")/*
                * @res "û�з����������ڳ��ݹ���ⵥ��"
                */);

          // �������
          getBillListPanel().getHeadBillModel().clearBodyData();
          getBillListPanel().getBodyBillModel().clearBodyData();
          getBillListPanel().updateUI();
          // ���ð�ť״̬������ѯ,�л���ȫ��Ϊ��
          for (int i = 0; i < 8; i++) {
            m_nButtonState[i] = 1;
          }
          m_nButtonState[3] = 0;
          m_nButtonState[4] = 0;
          m_nUIState = 3;
          changeButtonState();
          return;
        }
        tTime = System.currentTimeMillis() - tTime;
        SCMEnv.out("�ڳ��ݹ���ⵥ��ѯʱ�䣺" + tTime + " ms!");

        // ��ʾ��ѯ���
        // m_bBodyQuery = new UFBoolean[m_VOs.length];
        // for(int i = 0; i < m_VOs.length; i++){
        // if(i == 0) m_bBodyQuery[i] = new UFBoolean(true);
        // else m_bBodyQuery[i] = new UFBoolean(false);
        // }

        Vector v = new Vector();
        for (int i = 0; i < m_VOs.length; i++)
          v.addElement(m_VOs[i].getHeadVO());
        GeneralHHeaderVO head[] = new GeneralHHeaderVO[v.size()];
        v.copyInto(head);

        GeneralHItemVO body[] = m_VOs[0].getBodyVO();

        getBillListPanel().getHeadBillModel().setBodyDataVO(head);
        getBillListPanel().getHeadBillModel().execLoadFormula();
        getBillListPanel().getBodyBillModel().setBodyDataVO(body);
        getBillListPanel().getBodyBillModel().execLoadFormula();
        getBillListPanel().getHeadBillModel().updateValue();
        getBillListPanel().getBodyBillModel().updateValue();
        nc.ui.pu.pub.PuTool.loadSourceBillData(getBillListPanel(), "4T");
        getBillListPanel().updateUI();

        // ��ʾ��ע
        for (int i = 0; i < head.length; i++) {
          getBillListPanel().getHeadBillModel().setValueAt(head[i].getVnote(),
              i, "vnote");
        }       
        setListBtnsState();
//        // ���ð�ť״̬����ȫ��,����,�л���ȫ��Ϊ����
//        for (int i = 0; i < 8; i++) {
//          m_nButtonState[i] = 1;
//        }
//        m_nButtonState[3] = 0;
//        m_nButtonState[4] = 0;
//        m_nButtonState[4] = 1;
//        m_nButtonState[6] = 1;
//        m_nButtonState[7] = 1;
//
//        m_nUIState = 3;
//        changeButtonState();
      }
      catch (java.sql.SQLException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000065")/*
             * @res "�ڳ��ݹ���ⵥ��ѯ"
             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
             "UPPSCMCommon-000412")/*
              * @res "SQL������"
              */);
        SCMEnv.out(e);
        return;
      }
      catch (ArrayIndexOutOfBoundsException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000065")/*
             * @res "�ڳ��ݹ���ⵥ��ѯ"
             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
             "UPPSCMCommon-000426")/*
              * @res "����Խ�����"
              */);
        SCMEnv.out(e);
        return;
      }
      catch (NullPointerException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000065")/*
             * @res "�ڳ��ݹ���ⵥ��ѯ"
             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
             "UPPSCMCommon-000427")/*
              * @res "��ָ�����"
              */);
        SCMEnv.out(e);
        return;
      }
      catch (Exception e) {
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
            "UPP40040503-000065")/* @res "�ڳ��ݹ���ⵥ��ѯ" */, e.getMessage());
        SCMEnv.out(e);
        return;
      }
    }
  }
  
  /**
   * �˴����뷽��˵���� ��������:������ѯ �������: ����ֵ: �쳣����:

   */
  public void onQueryOrder() {
    if (!m_bICStartUp) {
      // ���δ����
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000077")/*
                                                         * @res "������ѯ"
                                                         */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
          "UPP40040503-000078")/*
                                 * @res "���δ���ã�����ͨ���ɹ����������ڳ��ݹ���ⵥ��"
                                 */);
      return;
    }

    initQueryModel2();

    UIRefPane biztypeRef = new UIRefPane();
    biztypeRef.setIsCustomDefined(true);
    biztypeRef.setRefModel(new nc.ui.ps.pub.SpeBiztypeDef(m_sBiztypeID));
    m_condClient2.setValueRef("cbiztype", biztypeRef);

    m_condClient2.hideNormal();
    m_condClient2.hideCorp();
    m_condClient2.showModal();

    if (m_condClient2.isCloseOK()) {
      // ��ȡ������ѯ����
      ConditionVO conditionVO[] = m_condClient2.getConditionVO();

      // ��ѯ
      try {
        long tTime = System.currentTimeMillis();
        m_orderVOs = EstimateHelper.queryOrder(m_sUnitCode, conditionVO,
            m_sBiztypeID, m_sEstPriceSource);
        if (m_orderVOs == null || m_orderVOs.length == 0) {
          MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000077")/*
                                                             * @res "������ѯ"
                                                             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000079")/*
                                     * @res "û�з��������Ķ�����"
                                     */);

          // �������
          getBillCardPanel2().getBillModel().clearBodyData();
          getBillCardPanel2().updateUI();
          // ���ó���ѯ,����������а�ťΪ��
          for (int i = 0; i < 6; i++) {
            m_nButtonState[i] = 1;
          }
          m_nButtonState[4] = 0;
          m_nButtonState[5] = 0;
          m_nUIState = 2;
          changeButtonState();
          return;
        }
        tTime = System.currentTimeMillis() - tTime;
        SCMEnv.out("������ѯʱ�䣺" + tTime + " ms!");

        // ����Ƿ��ռ��ᣬ���ջ���˾����Ϊ��¼��˾,since v502
        for (OorderVO curVo : m_orderVOs) {
          if (EstimateVO.PURCHASE.equals(EstimateVO.getEstType(curVo
              .getPk_corp(), curVo.getPk_arrvcorp(), curVo.getPk_invoicecorp(),
              m_sUnitCode))) {
            curVo.setPk_arrvcorp(m_sUnitCode);
          }
        }

        // ��ʾ��ѯ���
        getBillCardPanel2().getBillData().setBodyValueVO(m_orderVOs);
        getBillCardPanel2().getBillModel().execLoadFormula();
        getBillCardPanel2().updateValue();
        getBillCardPanel2().updateUI();

        // zhf �����۱�����
        setBodyDigits();

        // setAddPartNoEditable();
        getBillCardPanel2().setEnabled(true);

        // ����Ϊ�����۱����ʲ����޸� zhf add
        int rows = getBillCardPanel2().getBillModel().getRowCount();
        for (int i = 0; i < rows; i++) {
          Object curTypeId = getBillCardPanel2().getBodyValueAt(i,
              "currencytypeid");
          if (curTypeId == null)
            continue;

          if (curTypeId.toString().equalsIgnoreCase(m_sCurrTypeID))// �жϵ�ǰ�����Ƿ�˾��λ��
            // �����۱����ʱ༭����
            getBillCardPanel2().getBillModel().setCellEditable(i,
                "nexchangeotobrate", false);
          else
            getBillCardPanel2().getBillModel().setCellEditable(i,
                "nexchangeotobrate", true);
        }
        // zhf end

        // ���ð�ť״̬����ȫ��,�ݹ�������Ϊ����
        for (int i = 0; i < 6; i++) {
          m_nButtonState[i] = 0;
        }
        m_nButtonState[1] = 1;
        m_nButtonState[3] = 1;
        m_nUIState = 2;
        changeButtonState();
      }
      catch (java.sql.SQLException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000077")/*
                                                           * @res "������ѯ"
                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
            "UPPSCMCommon-000412")/*
                                   * @res "SQL������"
                                   */);
        SCMEnv.out(e);
        return;
      }
      catch (ArrayIndexOutOfBoundsException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000077")/*
                                                           * @res "������ѯ"
                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
            "UPPSCMCommon-000426")/*
                                   * @res "����Խ�����"
                                   */);
        SCMEnv.out(e);
        return;
      }
      catch (NullPointerException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000077")/*
                                                           * @res "������ѯ"
                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
            "UPPSCMCommon-000427")/*
                                   * @res "��ָ�����"
                                   */);
        SCMEnv.out(e);
        return;
      }
      catch (Exception e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000077")/* @res "������ѯ" */, e
            .getMessage());
        SCMEnv.out(e);
        return;
      }
    }
  }
  
  
  /**
   * �˴����뷽��˵���� ��������:������ѯ �������: ����ֵ: �쳣����:
   * modify by zhaoyha at 2008.10.15 ʹ���²�ѯģ��
   */
  public void onQueryOrderForV55() {
    if (!m_bICStartUp) {
      // ���δ����
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000077")/*
           * @res "������ѯ"
           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
           "UPP40040503-000078")/*
            * @res "���δ���ã�����ͨ���ɹ����������ڳ��ݹ���ⵥ��"
            */);
      return;
    }

    getNewQueryDlg().showModal();
    if(getNewQueryDlg().isCloseOK()){

      // ��ѯ
      try {
        //ҵ������Ȩ�޿���
        String sInit = " and po_order.cbiztype in (";
        String s=sInit;
        for (int i = 0; i < m_sBiztypeID.length; i++) {
          if (i != m_sBiztypeID.length - 1)
            s += "'" + m_sBiztypeID[i] + "',";
          else
            s += "'" + m_sBiztypeID[i] + "')";
        }
        //�õ���ѯ�Ի����е�����
        String wherePart=getNewQueryDlg().getQueryWhereSql();
        if(!sInit.equals(s)) wherePart+=s;
        long tTime = System.currentTimeMillis();

        m_orderVOs = EstimateHelper.queryOrder(m_sUnitCode, wherePart);
        if (m_orderVOs == null || m_orderVOs.length == 0) {
          MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000077")/*
               * @res "������ѯ"
               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
               "UPP40040503-000079")/*
                * @res "û�з��������Ķ�����"
                */);

          // �������
          getBillCardPanel2().getBillModel().clearBodyData();
          getBillCardPanel2().updateUI();
          // ���ó���ѯ,����������а�ťΪ��
          for (int i = 0; i < 6; i++) {
            m_nButtonState[i] = 1;
          }
          m_nButtonState[4] = 0;
          m_nButtonState[5] = 0;
          m_nUIState = 2;
          changeButtonState();
          return;
        }
        tTime = System.currentTimeMillis() - tTime;
        SCMEnv.out("������ѯʱ�䣺" + tTime + " ms!");

        // ����Ƿ��ռ��ᣬ���ջ���˾����Ϊ��¼��˾,since v502
        for (OorderVO curVo : m_orderVOs) {
          if (EstimateVO.PURCHASE.equals(EstimateVO.getEstType(curVo
              .getPk_corp(), curVo.getPk_arrvcorp(), curVo.getPk_invoicecorp(),
              m_sUnitCode))) {
            curVo.setPk_arrvcorp(m_sUnitCode);
          }
        }

        // ��ʾ��ѯ���
        getBillCardPanel2().getBillData().setBodyValueVO(m_orderVOs);
        getBillCardPanel2().getBillModel().execLoadFormula();
        getBillCardPanel2().updateValue();
        getBillCardPanel2().updateUI();

        // zhf �����۱�����
        setBodyDigits();

        // setAddPartNoEditable();
        getBillCardPanel2().setEnabled(true);

        // ����Ϊ�����۱����ʲ����޸� zhf add
        int rows = getBillCardPanel2().getBillModel().getRowCount();
        for (int i = 0; i < rows; i++) {
          Object curTypeId = getBillCardPanel2().getBodyValueAt(i,
              "currencytypeid");
          if (curTypeId == null)
            continue;

          if (curTypeId.toString().equalsIgnoreCase(m_sCurrTypeID))// �жϵ�ǰ�����Ƿ�˾��λ��
            // �����۱����ʱ༭����
            getBillCardPanel2().getBillModel().setCellEditable(i,
                "nexchangeotobrate", false);
          else
            getBillCardPanel2().getBillModel().setCellEditable(i,
                "nexchangeotobrate", true);
        }
        // zhf end

        // ���ð�ť״̬����ȫ��,�ݹ�������Ϊ����
        for (int i = 0; i < 6; i++) {
          m_nButtonState[i] = 0;
        }
        m_nButtonState[1] = 1;
        m_nButtonState[3] = 1;
        m_nUIState = 2;
        changeButtonState();
      }
      catch (ArrayIndexOutOfBoundsException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000077")/*
             * @res "������ѯ"
             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
             "UPPSCMCommon-000426")/*
              * @res "����Խ�����"
              */);
        SCMEnv.out(e);
        return;
      }
      catch (NullPointerException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000077")/*
             * @res "������ѯ"
             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
             "UPPSCMCommon-000427")/*
              * @res "��ָ�����"
              */);
        SCMEnv.out(e);
        return;
      }
      catch (Exception e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000077")/* @res "������ѯ" */, e
            .getMessage());
        SCMEnv.out(e);
        return;
      }
    }
  }

  /**
   * ��������:����
   */
  public boolean onSave() {
    getBillCardPanel1().stopEditing();

    // updateOorderVO();// zhf add

    ArrayList lModify = null;

    int nRow = getBillCardPanel1().getRowCount();
    GeneralHVO VO = new GeneralHVO(nRow);
    getBillCardPanel1().getBillValueVO(VO);

    GeneralHHeaderVO hheadVO = VO.getHeadVO();

    GeneralHItemVO bbodyVO[] = VO.getBodyVO();
    if (bbodyVO == null || bbodyVO.length == 0) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000082")/* @res "�Ϸ��Լ��" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000093")/* @res"���岻��Ϊ�գ�" */);
      return false;
    }
    //
    GeneralHItemVO tempVO[] = m_VOs[m_nPresentRecord].getBodyVO();
    for (int i = 0; i < bbodyVO.length; i++) {
      // ����ģ����û��ccfirsttype,cfirstbillhid,cfirstbillbid
      // ��˱���ʱ��Ҫ��������3���ֶε�ֵ����ͷ,�����Զ������ֵ
      bbodyVO[i].setCfirsttype(tempVO[i].getCfirsttype());
      bbodyVO[i].setVfirstbillcode(tempVO[i].getVsourcebillcode());// since
      // v53,
      // ���ո�����
      bbodyVO[i].setCfirstbillhid(tempVO[i].getCfirstbillhid());
      bbodyVO[i].setCfirstbillbid(tempVO[i].getCfirstbillbid());
      // since v502
      bbodyVO[i].setBLargess(tempVO[i].getBLargess());
      bbodyVO[i].setCvendorid(hheadVO.getCproviderid());
    }
    // �Ϸ��Լ��
    int nError = -1;
    try {
      VO.getHeadVO().validate();
      GeneralHItemVO bodyVO[] = VO.getBodyVO();
      for (nError = 0; nError < bodyVO.length; nError++)
        bodyVO[nError].validate();
    }
    catch (ValidationException e) {
      String sError = "";
      if (nError == -1) {
        sError = e.getMessage();
      }
      else {
        String[] value = new String[] {
          String.valueOf(nError + 1)
        };
        sError = nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
            "UPP40040503-000081", null, value)/*
                                               * @res "������" +
                                               * CommonConstant.BEGIN_MARK +
                                               * (nError + 1) +
                                               * CommonConstant.END_MARK +
                                               * "�����ֶβ���Ϊ�գ�\n"
                                               */
            + e.getMessage();
      }
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000082")/* @res "�Ϸ��Լ��" */,
          sError);
      return false;
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000082")/* @res "�Ϸ��Լ��" */, e
          .getMessage());
      return false;
    }

    // �кż��
    if (!nc.ui.scm.pub.report.BillRowNo.verifyRowNosCorrect(
        getBillCardPanel1(), "crowno"))
      return false;

    // ��������κż��
    String sError = checkFreeItem() + checkBatchCode();
    if (sError != null && sError.trim().length() > 0) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000082")/* @res "�Ϸ��Լ��" */,
          sError);
      return false;
    }

//    if (checkBeforeSave(VO, m_bAdd)) {
//      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
//          .getStrByID("40040503", "UPP40040503-000082")/* @res "�Ϸ��Լ��" */,
//          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
//              "UPP40040503-000083")/* @res "��ⵥ���ظ������ܱ��棡" */);
//      return false;
//    }

    if (!m_bAdd) {
      // ��������ⵥ
      GeneralHHeaderVO headVO = VO.getHeadVO();
      // ��ͷ����
      headVO.setPk_corp(m_sUnitCode);
      headVO.setFbillflag(m_VOs[m_nPresentRecord].getHeadVO().getFbillflag());
      headVO.setPrimaryKey((String) m_VOs[m_nPresentRecord].getHeadVO()
          .getAttributeValue("cgeneralhid"));
      headVO.setHeadEditStatus(2);

      UIRefPane nRefPanel = (UIRefPane) getBillCardPanel1()
          .getHeadItem("vnote").getComponent();
      UITextField vMemoField = nRefPanel.getUITextField();
      headVO.setVnote(vMemoField.getText());

      GeneralHItemVO bodyVO[] = VO.getBodyVO();
      for (int i = 0; i < bodyVO.length; i++) {
        bodyVO[i].setBodyEditStatus(getBillCardPanel1().getBillData()
            .getBillModel().getRowState(i));
        // bodyVO[i].setDzgdate(m_dSystem);

        bodyVO[i].setDbizdate(headVO.getDbilldate());
        bodyVO[i].setDzgdate(headVO.getDbilldate());

        bodyVO[i].setPk_corp(m_sUnitCode);
      }

      // ���Ʊ��弰�ӱ�
      Vector vData = new Vector();
      // Vector vDataData = new Vector();//
      for (int i = 0; i < bodyVO.length; i++) {
        vData.addElement(bodyVO[i]);
        // vDataData.addElement(bb3VO[i]);//
      }

      // ����ɾ���ı���
      Vector vDelData = getBillCardPanel1().getBillModel().getDeleteRow();
      GeneralHItemVO bVO[] = m_VOs[m_nPresentRecord].getBodyVO();
      if (vDelData != null) {
        for (int i = 0; i < vDelData.size(); i++) {
          Vector vTemp = (Vector) vDelData.elementAt(i);
          GeneralHItemVO bvoTemp = new GeneralHItemVO();
          bvoTemp.setCgeneralbid((String) vTemp.elementAt(1));
          for (int j = 0; j < bVO.length; j++) {
            if (bvoTemp.getCgeneralbid().equals(bVO[j].getCgeneralbid())) {
              bvoTemp = (GeneralHItemVO) bVO[j].clone();
              vData.addElement(bvoTemp);
              break;
            }
          }
        }
      }

      GeneralHItemVO v[] = new GeneralHItemVO[vData.size()];
      vData.copyInto(v);

      Vector vTemp = new Vector();
      GeneralBb3VO bb3VOs[] = m_VOs[m_nPresentRecord].getGrandVO();
      for (int i = 0; i < v.length; i++) {
        String s1 = v[i].getCgeneralbid().trim();
        for (int j = 0; j < bb3VOs.length; j++) {
          String s2 = bb3VOs[j].getCgeneralbid().trim();
          if (s1.equals(s2)) {
            bb3VOs[j].setNpprice(v[i].getNprice());
            bb3VOs[j].setNpmoney(v[i].getNmny());
            vTemp.addElement(bb3VOs[j]);
            break;
          }
        }
      }
      GeneralBb3VO vv[] = new GeneralBb3VO[vTemp.size()];
      vTemp.copyInto(vv);

      for (int i = vData.size() - 1; i >= nRow; i--) {
        // ����ɾ��״̬
        v[i].setBodyEditStatus(3);
      }
      // ����ɾ�������б�������Ϊ��ǰ�ı���
      VO.setChildrenVO(v);
      VO.setGrandVO(vv);

      // �ڳ��ݹ���ⵥ�޸�ʱ�������ⵥ���޸ĵ��ݹ��������޸�ֵ=�޸ĺ��ֵ-�޸�ǰ��ֵ
      lModify = getModifiedOrderStockNum();

      // ����dr��ts
      VO.getHeadVO().setDr(m_VOs[m_nPresentRecord].getHeadVO().getDr());
      VO.getHeadVO().setTs(m_VOs[m_nPresentRecord].getHeadVO().getTs());
      GeneralHItemVO temp1VO[] = VO.getBodyVO();
      GeneralBb3VO bb31VO[] = VO.getGrandVO();
      GeneralHItemVO temp2VO[] = m_VOs[m_nPresentRecord].getBodyVO();
      GeneralBb3VO bb32VO[] = m_VOs[m_nPresentRecord].getGrandVO();
      for (int i = 0; i < temp1VO.length; i++) {
        String s1 = temp1VO[i].getCgeneralbid();
        if (s1 == null || s1.trim().length() == 0)
          continue;
        for (int j = 0; j < temp2VO.length; j++) {
          String s2 = temp2VO[j].getCgeneralbid().trim();
          if (s1.equals(s2)) {
            temp1VO[i].setDr(temp2VO[j].getDr());
            temp1VO[i].setTs(temp2VO[j].getTs());
            bb31VO[i].setDr(bb32VO[j].getDr());
            bb31VO[i].setTs(bb32VO[j].getTs());
            break;
          }
        }
      }
    }
    else {
      // ������ⵥ
      GeneralHHeaderVO headVO = VO.getHeadVO();
      headVO.setFbillflag(new Integer(3));

      GeneralHItemVO bodyVO[] = VO.getBodyVO();
      if (bodyVO != null && bodyVO.length > 0) {
        for (int i = 0; i < bodyVO.length; i++) {
          // bodyVO[i].setDzgdate(m_dSystem);
          bodyVO[i].setDbizdate(headVO.getDbilldate());
          bodyVO[i].setDzgdate(headVO.getDbilldate());
          bodyVO[i].setPk_corp(headVO.getPk_corp());
        }
      }

      try {
        if (headVO.getCoperatorid() == null)
          headVO.setCoperatorid(EstimateHelper
              .getRefOperatorKey(getClientEnvironment().getUser()
                  .getPrimaryKey()));
        if (headVO.getCdptid() == null)
          headVO.setCdptid(EstimateHelper
              .getRefDeptKey(headVO.getCoperatorid()));
      }
      catch (Exception e) {
        SCMEnv.out(e);
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("SCMCOMMON", "UPPSCMCommon-000355")/* @res "��ͷ����" */,
            e.getMessage());
        return false;
      }

      UIRefPane nRefPanel = (UIRefPane) getBillCardPanel1()
          .getHeadItem("vnote").getComponent();
      UITextField vMemoField = nRefPanel.getUITextField();
      headVO.setVnote(vMemoField.getText());

      // ���������ڳ��ݹ���ⵥʱ�������ⵥ�е��ݹ�����,�޸Ķ������ۼ��������
      lModify = getModifiedOrderStockNum();
    }

    boolean bContinue = true;
    while (bContinue) {
      try {
        // ����
        long tTime = System.currentTimeMillis();
        // �����ⵥVO����Դ����VO
        Vector vv = new Vector();
        GeneralHItemVO itemVO[] = VO.getBodyVO();
        if (m_orderVOs != null && m_orderVOs.length > 0) {
          for (int i = 0; i < m_orderVOs.length; i++) {
            String s1 = m_orderVOs[i].getCorder_bid().trim();
            for (int j = 0; j < itemVO.length; j++) {
              String s2 = itemVO[j].getCsourcebillbid().trim();
              if (s1.equals(s2)) {
                vv.addElement(m_orderVOs[i]);
                break;
              }
            }
          }
        }
        OorderVO orderVOs[] = null;
        if (vv.size() > 0) {
          orderVOs = new OorderVO[vv.size()];
          vv.copyInto(orderVOs);
        }
        //

        lModify.add(getCorpPrimaryKey());

        ArrayList list = EstimateHelper.doSave(VO, m_bAdd, lModify,
            getClientEnvironment().getUser().getPrimaryKey(), orderVOs);

        ArrayList list1 = null;
        ArrayList list2 = null;
        if (list != null && list.size() > 0) {
          list1 = (ArrayList) list.get(0);
          list2 = (ArrayList) list.get(1);
        }

        tTime = System.currentTimeMillis() - tTime;
        SCMEnv.out("����ʱ�䣺" + tTime + " ms!");

        // ����ɹ����������ñ��壨ȥ��ɾ���ı��壩
        GeneralHItemVO bodyVO[] = VO.getBodyVO();
        GeneralBb3VO bb3VO[] = VO.getGrandVO();
        Vector temp = new Vector();
        Vector tempp = new Vector();
        for (int i = 0; i < bodyVO.length; i++) {
          if (bodyVO[i].getBodyEditStatus() != 3) {
            temp.addElement(bodyVO[i]);
            tempp.addElement(bb3VO[i]);
          }
        }
        GeneralHItemVO tempBVO[] = new GeneralHItemVO[temp.size()];
        temp.copyInto(tempBVO);
        VO.setChildrenVO(tempBVO);
        GeneralBb3VO tempBb3VO[] = new GeneralBb3VO[tempp.size()];
        tempp.copyInto(tempBb3VO);
        VO.setGrandVO(tempBb3VO);

        // ���ó�����,ȡ��,ɾ�������а�ťΪ����
        getBillCardPanel1().setEnabled(false);
        for (int i = 0; i < 16; i++) {
          m_nButtonState[i] = 0;
        }
        for (int i = 2; i < 5; i++)
          m_nButtonState[i] = 1;
        // ���������ӣ�������Ϊ��
        if (m_bGenerate)
          m_nButtonState[5] = 1;

        // �����������ݵ��ӱ�����ӱ��ѯΪTRUE
        // if (m_bAdd) {
        // Vector vTemp1 = new Vector();
        // if (m_bBodyQuery != null && m_bBodyQuery.length > 0) {
        // for (int i = 0; i < m_bBodyQuery.length; i++)
        // vTemp1.addElement(m_bBodyQuery[i]);
        // }
        // vTemp1.addElement(new UFBoolean(true));
        // m_bBodyQuery = new UFBoolean[vTemp1.size()];
        // vTemp1.copyInto(m_bBodyQuery);
        // }

        // ���ӵı�ͷ�����б�����������
        if (m_bAdd) {
          if (list1 != null && list1.size() > 0) {
            // ��������ⵥ�л������ֵ
            VO.getHeadVO().setAttributeValue("cgeneralhid", list1.get(0));
            int j = 1;
            for (int i = 0; i < nRow; i++) {
              tempBVO[i].setAttributeValue("cgeneralhid", list1.get(0));
              tempBVO[i].setAttributeValue("cgeneralbid", list1.get(j));
              tempBb3VO[i].setAttributeValue("cgeneralhid", list1.get(0));
              tempBb3VO[i].setAttributeValue("cgeneralbid", list1.get(j));
              tempBb3VO[i].setAttributeValue("cgeneralbb3", list1.get(j+nRow));
              j++;
            }

            // ��������ⵥ��õ��ݺ�
            VO.getHeadVO().setAttributeValue("vbillcode",
                list1.get(list1.size() - 1));
            //
          }

          if (list2 != null && list2.size() > 0) {
            // ��������ⵥͷ����������ⵥ�弰�ӱ���ʱ���
            VO.getHeadVO().setTs((String) list2.get(0));
            int j = 1;
            for (int i = 0; i < nRow; i++) {
              tempBVO[i].setTs((String) list2.get(j));
              j++;
              tempBb3VO[i].setTs((String) list2.get(j));
              j++;
            }
          }
          //

          Vector vTemp = new Vector();
          if (m_VOs != null) {
            // �Ѿ�������ⵥ
            if (!m_bGenerate) {
              for (int i = 0; i < m_VOs.length; i++) {
                vTemp.addElement(m_VOs[i]);
              }
              vTemp.addElement(VO);
              m_VOs = new GeneralHVO[vTemp.size()];
              vTemp.copyInto(m_VOs);
              m_nPresentRecord = m_VOs.length - 1;
            }
            else
              m_VOs[m_nPresentRecord] = VO;
          }
          else {
            // ��������ⵥ
            m_VOs = new GeneralHVO[1];
            m_VOs[0] = VO;
            m_nPresentRecord = 0;
          }
        }
        else {
          // ��������ⵥ�л������ֵ
          if (list1 != null && list1.size() > 0) {
            int j = 1;
            for (int i = 0; i < nRow; i++) {
              if (tempBVO[i].getBodyEditStatus() == 1) {
                tempBVO[i].setAttributeValue("cgeneralhid", list1.get(0));
                tempBVO[i].setAttributeValue("cgeneralbid", list1.get(j));
                tempBb3VO[i].setAttributeValue("cgeneralhid", list1.get(0));
                tempBb3VO[i].setAttributeValue("cgeneralbid", list1.get(j));
                j++;
                tempBVO[i].setBodyEditStatus(0);
              }
            }
          }

          // ��ⵥͷ�����µ���ⵥ�弰�ӱ���ʱ���
          if (list2 != null && list2.size() > 0) {
            VO.getHeadVO().setTs((String) list2.get(0));
            int j = 1;
            for (int i = 0; i < nRow; i++) {
              if (tempBVO[i].getBodyEditStatus() == 2) {
                tempBVO[i].setTs((String) list2.get(j));
                j++;
                tempBb3VO[i].setTs((String) list2.get(j));
                j++;
                tempBVO[i].setBodyEditStatus(0);
              }
            }
          }
          //

          m_VOs[m_nPresentRecord] = VO;
        }

        // ��������װ�����ݣ������½���
        getBillCardPanel1().setBillValueVO(m_VOs[m_nPresentRecord]);
        getBillCardPanel1().getBillModel().execLoadFormula();
        // ��ʾ��Դ������Ϣ
        nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");
        getBillCardPanel1().updateValue();
        if (!m_bAdd) {
        }

        this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "40040503", "UPP40040503-000084")/*
                                               * @res "����ɹ���"
                                               */);
        if (m_nPresentRecord == 0) {
          m_nButtonState[7] = 1;
          m_nButtonState[8] = 1;
        }
        if (m_nPresentRecord == m_VOs.length - 1) {
          m_nButtonState[9] = 1;
          m_nButtonState[10] = 1;
        }
        if (m_VOs.length == 1) {
          for (int i = 7; i < 11; i++)
            m_nButtonState[i] = 1;
        }
        // �ݹ�Ӧ��ʱ,�������޸ģ�Ҳ�Ͳ������в���
        if (m_bZGYF.booleanValue()) {
          m_nButtonState[1] = 1;
        }
        m_nUIState = 1;
        
//        zhf  
        m_nButtonState[buttons1_len-1] = 0;
        changeButtonState();

        // ��ʾ��ע
        UIRefPane nRefPanel = (UIRefPane) getBillCardPanel1().getHeadItem(
            "vnote").getComponent();
        nRefPanel.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVnote());

        // ��ʾ�б�����ұ������ڳ��ݹ���ⵥ������ʾ�ڽ���
        if (m_bGenerate) {
          doRefresh(m_nPresentRecord);
        }

        bContinue = false;
      }
      catch (java.sql.SQLException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000085")/* @res "��ⵥ����" */, e
            .getMessage());
        SCMEnv.out(e);
        return false;
      }
      catch (ArrayIndexOutOfBoundsException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000085")/*
                                                           * @res "��ⵥ����"
                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
            "UPPSCMCommon-000426")/*
                                   * @res "����Խ�����"
                                   */);
        SCMEnv.out(e);
        return false;
      }
      catch (NullPointerException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000085")/*
                                                           * @res "��ⵥ����"
                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
            "UPPSCMCommon-000427")/*
                                   * @res "��ָ�����"
                                   */);
        SCMEnv.out(e);
        return false;
      }
      // since v502, ֧����ʾ����
      catch (RwtIcToPoException e) {
        // ��ⵥ�ۼ�����������ʾ
        int iRet = showYesNoMessage(e.getMessage());
        if (iRet == MessageDialog.ID_YES) {
          // ����ѭ��
          bContinue = true;
          // �û�ȷ�Ϲ������Ա���
          VO.setUserConfirmed(true);
        }
        else {
          return false;
        }
      }
      catch (nc.vo.pub.BusinessException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000085")/* @res "��ⵥ����" */, e
            .getMessage());
        SCMEnv.out(e);
        return false;
      }
      catch (Exception e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000085")/* @res "��ⵥ����" */, e
            .getMessage());
        SCMEnv.out(e);
        return false;
      }
    }
    //���Ƿ����б�־
    m_bSettling = false;
    return true;
  }

  /**
   * �˴����뷽��˵���� ��������:ȫѡ �������: ����ֵ: �쳣����:
   */
  public void onSelectAll() {
    if (m_nUIState == 2) {
      // ��ʾ������ѯ�б�
      int nRow = getBillCardPanel2().getBillModel().getRowCount();
      //
      for (int i = 0; i < nRow; i++)
        getBillCardPanel2().getBillModel().setRowState(i, BillModel.SELECTED);

      getBillCardPanel2().updateUI();
      // ��ȫѡ��������ť����
      for (int i = 0; i < 5; i++)
        m_nButtonState[i] = 0;
      m_nButtonState[0] = 1;
      m_nUIState = 2;
      changeButtonState();
    }

    if (m_nUIState == 3) {
      // ��ʾ��ⵥ�б�
      int nRow = getBillListPanel().getHeadBillModel().getRowCount();
      //
      getBillListPanel().getHeadTable().setRowSelectionInterval(0, nRow - 1);
      //
      for (int i = 0; i < nRow; i++) {
        getBillListPanel().getHeadBillModel()
            .setRowState(i, BillModel.SELECTED);
      }
      getBillListPanel().updateUI();
      if (!m_bAdd) {
        // �Ƕ������ɲɹ���ⵥ����ȫѡ,�л���������ť����
        for (int i = 0; i < 8; i++)
          m_nButtonState[i] = 0;
        m_nButtonState[0] = 1;
      }
      else {
        // �������ɲɹ���ⵥ����ȫ������ӡ������Ϊ��
        for (int i = 0; i < 8; i++)
          m_nButtonState[i] = 1;
        m_nButtonState[1] = 0;
      }
      if (nRow != 1)
        m_nButtonState[4] = 1;
      else
        m_nButtonState[4] = 0;
      m_nUIState = 3;
      changeButtonState();
    }
  }

  /**
   * �˴����뷽��˵���� ��������:ѡ��ֵ���ʽ �������: ����ֵ: �쳣����:
   */
  public void onSelectMode() {
    if (m_dialog == null) {
      m_dialog = new UIDialog(this);
      m_dialog.setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "4004050302", "UPP4004050302-000012")/* @res "ѡ��ֵ���ʽ" */);

      m_rb1 = new UIRadioButton();
      m_rb1.setEnabled(true);
      UILabel label1 = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "4004050302", "UPP4004050302-000013")/*
                                                 * @res "����Ӧ��"
                                                 */);
      m_rb1.setBounds(80, 10, 16, 16);
      label1.setBounds(96, 7, 100, 25);

      m_rb2 = new UIRadioButton();
      m_rb2.setEnabled(true);
      UILabel label2 = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "4004050302", "UPP4004050302-000014")/*
                                                 * @res "�����+��Ӧ��"
                                                 */);
      m_rb2.setBounds(80, 45, 16, 16);
      label2.setBounds(96, 42, 100, 25);

      m_rb3 = new UIRadioButton();
      m_rb3.setEnabled(true);
      UILabel label3 = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "4004050302", "UPP4004050302-000015")/*
                                                 * @res "������"
                                                 */);
      m_rb3.setBounds(80, 80, 16, 16);
      label3.setBounds(96, 77, 100, 25);

      m_rb4 = new UIRadioButton();
      m_rb4.setEnabled(true);
      UILabel label4 = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "4004050302", "UPP4004050302-000016")/*
                                                 * @res "���ֿ�+��Ӧ��"
                                                 */);
      m_rb4.setBounds(80, 115, 16, 16);
      label4.setBounds(96, 112, 100, 25);

      m_rb5 = new UIRadioButton();
      m_rb5.setEnabled(true);
      UILabel label5 = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "4004050302", "UPP4004050302-000017")/*
                                                 * @res "������+�ֿ�"
                                                 */);
      m_rb5.setBounds(80, 150, 16, 16);
      label5.setBounds(96, 147, 100, 25);

      // ���ó�ʼֵ
      switch (m_nMode) {
        case 0:
          m_rb1.setSelected(true);
        case 1:
          m_rb2.setSelected(true);
        case 2:
          m_rb3.setSelected(true);
        case 3:
          m_rb4.setSelected(true);
        case 4:
          m_rb5.setSelected(true);
      }

      javax.swing.ButtonGroup group = new javax.swing.ButtonGroup();
      group.add(m_rb1);
      group.add(m_rb2);
      group.add(m_rb3);
      group.add(m_rb4);
      group.add(m_rb5);

      m_bnOK = new UIButton();
      m_bnOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UC001-0000044")/* @res "ȷ��" */);
      m_bnOK.addActionListener(this);
      m_bnCancel = new UIButton();
      m_bnCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UC001-0000008")/* @res "ȡ��" */);
      m_bnCancel.addActionListener(this);
      m_bnOK.setBounds(100, 180, 70, 22);
      m_bnCancel.setBounds(250, 180, 70, 22);

      m_dialog.getContentPane().setLayout(null);
      m_dialog.getContentPane().add(m_rb1);
      m_dialog.getContentPane().add(m_rb2);
      m_dialog.getContentPane().add(m_rb3);
      m_dialog.getContentPane().add(m_rb4);
      m_dialog.getContentPane().add(m_rb5);
      m_dialog.getContentPane().add(label1);
      m_dialog.getContentPane().add(label2);
      m_dialog.getContentPane().add(label3);
      m_dialog.getContentPane().add(label4);
      m_dialog.getContentPane().add(label5);
      m_dialog.getContentPane().add(m_bnOK);
      m_dialog.getContentPane().add(m_bnCancel);
    }

    m_dialog.showModal();
  }

  /**
   * �˴����뷽��˵���� ��������:ȫ�� �������: ����ֵ: �쳣����:
   */
  public void onSelectNo() {
    if (m_nUIState == 2) {
      // ��ʾ������ѯ�б�
      int nRow = getBillCardPanel2().getBillModel().getRowCount();
      if (nRow <= 0) {
        showHintMessage(NCLangRes.getInstance().getStrByID("40040101",
            "UPP40040101-000541")/* @res "������ѡ��" */);
        return;
      }
      getBillCardPanel2().getBillTable()
          .removeRowSelectionInterval(0, nRow - 1);
      //
      for (int i = 0; i < nRow; i++)
        getBillCardPanel2().getBillModel().setRowState(i, BillModel.UNSTATE);

      nRow = getBillCardPanel2().getBillModel().getRowCount();
      for (int i = 0; i < nRow; i++)
        getBillCardPanel2().getBillModel().setRowState(i, BillModel.UNSTATE);

      getBillCardPanel2().updateUI();
      // ��ȫ��,�ֵ�,�ݹ�����������
      for (int i = 0; i < 5; i++)
        m_nButtonState[i] = 0;
      m_nButtonState[1] = 1;
      m_nButtonState[2] = 1;
      m_nButtonState[3] = 1;
      m_nUIState = 2;
      changeButtonState();
    }

    if (m_nUIState == 3) {
      int nRow = getBillListPanel().getHeadBillModel().getRowCount();
      getBillListPanel().getHeadTable().removeRowSelectionInterval(0, nRow - 1);
      // ��ʾ��ⵥ�б�
      for (int i = 0; i < nRow; i++)
        getBillListPanel().getHeadBillModel().setRowState(i, BillModel.UNSTATE);

      nRow = getBillListPanel().getHeadBillModel().getRowCount();
      for (int i = 0; i < nRow; i++)
        getBillListPanel().getHeadBillModel().setRowState(i, BillModel.UNSTATE);

      getBillListPanel().updateUI();
      if (!m_bAdd) {
        // �Ƕ���������ⵥʱ����ȫѡ,��ѯ��������ťΪ��
        for (int i = 0; i < 8; i++)
          m_nButtonState[i] = 1;
        m_nButtonState[0] = 0;
        m_nButtonState[3] = 0;
      }
      else {
        // ����������ⵥʱ����ȫѡ��������ťΪ��
        for (int i = 0; i < 8; i++)
          m_nButtonState[i] = 1;
        m_nButtonState[0] = 0;
      }
      m_nUIState = 3;
      changeButtonState();
    }
  }

  /**
   * �˴����뷽��˵���� ��������:�б�ת������Ƭ �������: ����ֵ: �쳣����:
   */
  public void onSwitch() {
    // �б���������򣬻������ǰ�Ļ����¼ָ��
    m_nPresentRecord = nc.ui.pu.pub.PuTool.getIndexBeforeSort(m_listPanel,
        m_nPresentRecord);

    this.remove(getBillListPanel());
    getBillCardPanel1().setVisible(true);
    this.setButtons(m_buttons11);
    m_nButtonState = new int[m_buttons1.length];

    // �������ڳ���ⵥ
    if (getBillListPanel().getHeadBillModel().getRowCount() == 0) {
      getBillCardPanel1().getBillData().clearViewData();
      getBillCardPanel1().updateValue();
      getBillCardPanel1().updateUI();
      getBillCardPanel1().setEnabled(false);
      if (m_bGenerate) {
        m_bAdd = false;
      }

      // ���ó�����,��ӡ����ѯ���б�������а�ťΪ��
      for (int i = 0; i < 16; i++) {
        m_nButtonState[i] = 1;
      }
      m_nButtonState[0] = 0;
      m_nButtonState[6] = 0;
      m_nButtonState[11] = 0;
      m_nButtonState[12] = 0;
      m_nButtonState[13] = 0;
      m_nUIState = 1;
      changeButtonState();
      return;
    }

    // �����ڳ���ⵥ
    if(m_nPresentRecord<0){
      for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
        if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
          m_nPresentRecord = i;
          break;
        }
      }
    }

    if (m_nPresentRecord >= 0) {
      if (m_bGenerate && 
          PuPubVO.getString_TrimZeroLenAsNull(m_VOs[m_nPresentRecord].getHeadVO().getCgeneralhid())==null) {
        getBillCardPanel1().setEnabled(false);
        m_bAdd = true;
        // ����ҵ��Ա�Ĳ�������
        String sKey = m_VOs[m_nPresentRecord].getHeadVO().getCdptid();
        UIRefPane nRefPanel = (UIRefPane) getBillCardPanel1().getHeadItem(
            "cbizid").getComponent();
        String sWhere = " bd_psndoc.pk_deptdoc = '" + sKey + "'";
        if (sKey != null && sKey.length() > 0)
          nRefPanel.setWhereString(sWhere);
        else
          nRefPanel.setWhereString("bd_deptdoc.pk_corp = '" + m_sUnitCode
              + "' and bd_deptdoc.pk_deptdoc = bd_psndoc.pk_deptdoc");

        // ���ó����棬ɾ��, ��ӡ�����������а�ťΪ��
        for (int i = 0; i < 16; i++) {
          m_nButtonState[i] = 1;
        }
        m_nButtonState[2] = 0;
        m_nButtonState[3] = 0;
        m_nButtonState[4] = 0;
        m_nButtonState[12] = 0;
        m_nButtonState[13] = 0;
      }
      else {
        // ���ó�����,ȡ��,ɾ�������а�ťΪ����
        for (int i = 0; i < 16; i++) {
          m_nButtonState[i] = 0;
        }
        for (int i = 2; i < 5; i++)
          m_nButtonState[i] = 1;
        if (m_nPresentRecord == 0) {
          for (int i = 7; i < 9; i++)
            m_nButtonState[i] = 1;
        }
        if (m_nPresentRecord == m_VOs.length - 1) {
          for (int i = 9; i < 11; i++)
            m_nButtonState[i] = 1;
        }
        if (m_VOs.length == 1) {
          for (int i = 7; i < 11; i++)
            m_nButtonState[i] = 1;
        }
        // �ݹ�Ӧ��ʱ,�������޸ģ�Ҳ�Ͳ������в���
        if (m_bZGYF.booleanValue()) {
          m_nButtonState[1] = 1;
        }
      }
      m_nUIState = 1;
      changeButtonState();

      // ��ʾ��Ƭ����
      getBillCardPanel1().setBillValueVO(m_VOs[m_nPresentRecord]);
      getBillCardPanel1().getBillModel().execLoadFormula();
      // ��ʾ��Դ������Ϣ
      nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");
      getBillCardPanel1().updateValue();
      getBillCardPanel1().updateUI();

      if (m_bGenerate){
          if(PuPubVO.getString_TrimZeroLenAsNull(m_VOs[m_nPresentRecord].getHeadVO().getCgeneralhid())!=null){
            getBillCardPanel1().setEnabled(false);
          }
          else 
            setPartNoEditable();
      }
      // ��ʾ��ע
      UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem(
          "vnote").getComponent();
      nRefPanel1.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVnote());

      // ��ʾ�����֯
      setCalbodyValue();

      if (m_bGenerate) {
        // �ۼ�����ⵥ��
        getBillCardPanel1().transferFocusTo(BillCardPanel.HEAD);
      }

    }
  }

  /**
   * �˴����뷽��˵���� ��������:����״̬��ѯ��Ϻ����ñ�����ݹ��������ݹ�����,�ݹ����������ɱ༭ �������: ����ֵ: �쳣����:
   */
  private void setAddPartNoEditable() {
    BillItem items[] = getBillCardPanel2().getBodyItems();
    for (int i = 0; i < items.length; i++)
      items[i].setEnabled(false);

    for (int i = 0; i < items.length; i++) {
      String sKey = items[i].getKey().trim();
      if (sKey.equals("ngaugenum"))
        items[i].setEnabled(true);
      if (sKey.equals("nprice"))
        items[i].setEnabled(true);
      if (sKey.equals("nmoney"))
        items[i].setEnabled(true);
    }

    UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel2().getBodyItem(
        "ngaugenum").getComponent();
    UITextField nStockNumUI = (UITextField) nRefPanel1.getUITextField();
    // nStockNumUI.setMaxLength(13);
    nStockNumUI.setMaxLength(16);

    UIRefPane nRefPanel2 = (UIRefPane) getBillCardPanel2()
        .getBodyItem("nprice").getComponent();
    UITextField nPriceUI = (UITextField) nRefPanel2.getUITextField();
    // nPriceUI.setMaxLength(9);
    nPriceUI.setMaxLength(16);
    nPriceUI.setDelStr("-");

    UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel2()
        .getBodyItem("nmoney").getComponent();
    UITextField nMoneyUI = (UITextField) nRefPanel3.getUITextField();
    // nMoneyUI.setMaxLength(9);
    nMoneyUI.setMaxLength(16);

  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2003-11-18 16:57:10)
   */
  private void setCalbodyValue() {
    UIRefPane nRefPanel = (UIRefPane) getBillCardPanel1().getHeadItem(
        "cwarehouseid").getComponent();
    String sWarehouseid = nRefPanel.getRefPK();
    ((UIRefPane) getBillCardPanel1().getHeadItem("cwarehouse").getComponent())
        .setValue(sWarehouseid);
    int nRow = getBillCardPanel1().getRowCount();
    if (sWarehouseid == null || sWarehouseid.trim().length() == 0) {
      // nRefPanel =
      // (UIRefPane)getBillCardPanel1().getHeadItem("cstoreorganization").getComponent();
      // nRefPanel.setValue(null);
      // nRefPanel.setPK(null);
      //    
      for (int i = 0; i < nRow; i++) {
        getBillCardPanel1().setBodyValueAt(null, i, "pk_reqstoorg");
        getBillCardPanel1().setBodyValueAt(null, i, "pk_creqwareid");
      }
    }
    else {
      
      Object oTemp = getBillCardPanel1().execHeadFormula(
          "cstoreorganization->getColValue(bd_stordoc,pk_calbody,pk_stordoc,cwarehouse)");
      if (oTemp != null && oTemp.toString().trim().length() > 0) {
        nRefPanel = (UIRefPane) getBillCardPanel1().getHeadItem(
            "cstoreorganization").getComponent();
        nRefPanel.setPK(oTemp.toString());
      }
      for (int i = 0; i < nRow; i++) {
        getBillCardPanel1().setBodyValueAt(oTemp.toString(), i, "pk_reqstoorg");
        getBillCardPanel1().setBodyValueAt(sWarehouseid, i, "pk_creqwareid");
      }
    }

  }

  /**
   * @since v53, ֧���Զ�����ɱ༭��֧�ֿ�����ģ�壬czp, 2008-04-17
   *        <p>
   *        ע�⣬��������ϸ˵����
   *        <p>
   *        A������ԭ��(V502����ǰ)�Ƿ�ɱ༭���߼���
   *        <p>
   *        1����ͷ��ҵ�����͡������֯���ɱ༭��������Ŀ�ɱ༭
   *        <p>
   *        2�����壺��������κš��к�����ɱ༭��������Ŀ���ɱ༭
   *        <p>
   *        B��V53�������߼���
   *        <p>
   *        1����ͷ�������˱�ͷ�Զ�����1-20�����óɿɱ༭
   *        <p>
   *        2�����壺
   *        <p>
   *        B-2-1)�������˱����Զ�����1-20�����óɿɱ༭
   *        <p>
   *        B-2-2)�����������ۡ�������ݹ�ʱȷ�ϣ��������޸ģ�Ҫ�޸���Ҫɾ���ڳ��ݹ���ⵥ�����ݹ�(���ֹ��ݹ�����һ��)
   */
  private void setPartNoEditable() {
    getBillCardPanel1().setEnabled(true);
    BillItem items[] = getBillCardPanel1().getHeadItems();
    for (int i = 0; i < items.length; i++) {
      String sKey = items[i].getKey().trim();
      if (sKey.equals("cbiztype") || sKey.equals("cstoreorganization"))
        items[i].setEnabled(false);
    }
    // since V53,���������ۡ�������ݹ�ʱȷ�ϣ��������޸ģ�Ҫ�޸���Ҫɾ���ڳ��ݹ���ⵥ�����ݹ�(���ֹ��ݹ�����һ��)
    items = getBillCardPanel1().getBodyItems();
    for (int i = 0; i < items.length; i++) {
      String sKey = items[i].getKey().trim();
      if (sKey.equals("vfree"))
        items[i].setEnabled(items[i].isEdit());
      else if (sKey.equals("vbatchcode"))
        items[i].setEnabled(items[i].isEdit());
      else if (sKey.endsWith("crowno"))
        items[i].setEnabled(items[i].isEdit());
      else if (sKey.equals("ninnum"))
        items[i].setEnabled(false);
      else if (sKey.equals("nprice"))
        items[i].setEnabled(false);
      else if (sKey.equals("nmny"))
        items[i].setEnabled(false);
      else if (sKey.startsWith("vuserdef"))
        items[i].setEnabled(items[i].isEdit());
      else
        items[i].setEnabled(false);
    }

    // ����Ϊ������۱����ʲ����޸� zhf add
    int rows = getBillCardPanel2().getBillModel().getRowCount();
    for (int i = 0; i < rows; i++) {
      Object curTypeId = getBillCardPanel2()
          .getBodyValueAt(i, "currencytypeid");
      if (curTypeId == null)
        continue;

      if (curTypeId.toString().equalsIgnoreCase(m_sCurrTypeID))// �жϵ�ǰ�����Ƿ�˾��λ��
        // �����۱����ʱ༭����
        getBillCardPanel2().getBillModel().setCellEditable(i,
            "nexchangeotobrate", false);
      else
        getBillCardPanel2().getBillModel().setCellEditable(i,
            "nexchangeotobrate", true);
    }
    // zhf end

    //
    UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1()
        .getBodyItem("ninnum").getComponent();
    UITextField nStockNumUI = (UITextField) nRefPanel1.getUITextField();
    // nStockNumUI.setMaxLength(13);
    nStockNumUI.setMaxLength(16);

    UIRefPane nRefPanel2 = (UIRefPane) getBillCardPanel1()
        .getBodyItem("nprice").getComponent();
    UITextField nPriceUI = (UITextField) nRefPanel2.getUITextField();
    // nPriceUI.setMaxLength(9);
    nPriceUI.setMaxLength(16);
    nPriceUI.setDelStr("-");

    UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel1().getBodyItem("nmny")
        .getComponent();
    UITextField nMoneyUI = (UITextField) nRefPanel3.getUITextField();
    // nMoneyUI.setMaxLength(9);
    nMoneyUI.setMaxLength(16);

    UIRefPane nRefPanel4 = (UIRefPane) getBillCardPanel1().getBodyItem(
        "cinventorycode").getComponent();
    String sWhere = " upper(bd_invmandoc.sealflag) = 'N' and bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc and bd_invmandoc.pk_corp = '"
        + m_sUnitCode + "'";
    nRefPanel4.setWhereString(sWhere);

    UIRefPane nRefPanel5 = (UIRefPane) getBillCardPanel1().getHeadItem("vnote")
        .getComponent();
    nRefPanel5.setReturnCode(false);
    nRefPanel5.setAutoCheck(false);

    UIRefPane nRefPanel6 = (UIRefPane) getBillCardPanel1().getHeadItem(
        "cwarehouseid").getComponent();
    nRefPanel6.setWhereString(" bd_stordoc.pk_corp = '" + m_sUnitCode
        + "' and upper(gubflag) = 'N'");

    UIRefPane nRefPanel7 = (UIRefPane) getBillCardPanel1().getHeadItem(
        "cproviderid").getComponent();
    nRefPanel7
        .setWhereString(" bd_cumandoc.pk_corp = '"
            + m_sUnitCode
            + "' and upper(frozenflag) = 'N' and (custflag = '1' or custflag = '3') and bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc");

    // ���λ�ô������:������Ƿ����ι���
    int nRow = m_VOs[m_nPresentRecord].getBodyVO().length;
    InvVO invVO[] = new InvVO[nRow];
    for (int i = 0; i < nRow; i++) {
      invVO[i] = new InvVO();
      invVO[i].setCinventoryid(m_VOs[m_nPresentRecord].getBodyVO()[i]
          .getCinventoryid());
    }
    invVO = m_formula.getQuryInvVOs(invVO, false, false, 2);
    for (int i = 0; i < nRow; i++) {
      Integer j = invVO[i].getIsFreeItemMgt();
      if (j != null) {
        if (j.intValue() != 1)
          getBillCardPanel1().setCellEditable(i, "vfree", false);
        else
          getBillCardPanel1().setCellEditable(i, "vfree", true);
      }
      else {
        getBillCardPanel1().setCellEditable(i, "vfree", false);
      }
      j = invVO[i].getIsLotMgt();
      if (j != null) {
        if (j.intValue() != 1)
          getBillCardPanel1().setCellEditable(i, "vbatchcode", false);
        else
          getBillCardPanel1().setCellEditable(i, "vbatchcode", true);
      }
      else {
        getBillCardPanel1().setCellEditable(i, "vbatchcode", false);
      }
    }
    //
  }

  /*
   * ������ѯ���������"���ذ�ť"����
   */
  private void onReturn() {
    getBillCardPanel2().setVisible(false);
    getBillCardPanel1().setVisible(true);
    this.setButtons(m_buttons11);
    m_nButtonState = new int[m_buttons1.length];

    // �������ڳ���ⵥ
    if (getBillListPanel().getHeadBillModel().getRowCount() == 0) {
      getBillCardPanel1().getBillData().clearViewData();
      getBillCardPanel1().updateValue();
      getBillCardPanel1().updateUI();
      getBillCardPanel1().setEnabled(false);
      if (m_bGenerate) {
        m_bAdd = false;
      }

      // ���ó�����,��ӡ����ѯ���б�������а�ťΪ��
      for (int i = 0; i < 16; i++) {
        m_nButtonState[i] = 1;
      }
      m_nButtonState[0] = 0;
      m_nButtonState[6] = 0;
      m_nButtonState[11] = 0;
      m_nButtonState[12] = 0;
      m_nButtonState[13] = 0;
      m_nUIState = 1;
      changeButtonState();
      return;
    }

    // �����ڳ���ⵥ
    for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
      if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
        m_nPresentRecord = i;
        break;
      }
    }

    if (m_nPresentRecord >= 0) {
      if (m_bGenerate) {
        getBillCardPanel1().setEnabled(false);
        m_bAdd = true;
        // ����ҵ��Ա�Ĳ�������
        String sKey = m_VOs[m_nPresentRecord].getHeadVO().getCdptid();
        UIRefPane nRefPanel = (UIRefPane) getBillCardPanel1().getHeadItem(
            "cbizid").getComponent();
        String sWhere = " bd_psndoc.pk_deptdoc = '" + sKey + "'";
        if (sKey != null && sKey.length() > 0)
          nRefPanel.setWhereString(sWhere);
        else
          nRefPanel.setWhereString("bd_deptdoc.pk_corp = '" + m_sUnitCode
              + "' and bd_deptdoc.pk_deptdoc = bd_psndoc.pk_deptdoc");

        // ���ó����棬��ӡ�����������а�ťΪ��
        for (int i = 0; i < 16; i++) {
          m_nButtonState[i] = 1;
        }
        m_nButtonState[2] = 0;
        m_nButtonState[4] = 0;
        m_nButtonState[12] = 0;
        m_nButtonState[13] = 0;
      }
      else {
        // ���ó�����,ȡ��,ɾ�������а�ťΪ����
        for (int i = 0; i < 16; i++) {
          m_nButtonState[i] = 0;
        }
        for (int i = 2; i < 5; i++)
          m_nButtonState[i] = 1;
        if (m_nPresentRecord == 0) {
          for (int i = 7; i < 9; i++)
            m_nButtonState[i] = 1;
        }
        if (m_nPresentRecord == m_VOs.length - 1) {
          for (int i = 9; i < 11; i++)
            m_nButtonState[i] = 1;
        }
        if (m_VOs.length == 1) {
          for (int i = 7; i < 11; i++)
            m_nButtonState[i] = 1;
        }
        // �ݹ�Ӧ��ʱ,�������޸ģ�Ҳ�Ͳ������в���
        if (m_bZGYF.booleanValue()) {
          m_nButtonState[1] = 1;
        }
      }
      m_nUIState = 1;
      changeButtonState();

      // ��ʾ��Ƭ����
      getBillCardPanel1().setBillValueVO(m_VOs[m_nPresentRecord]);
      getBillCardPanel1().getBillModel().execLoadFormula();
      // ��ʾ��Դ������Ϣ
      nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");
      getBillCardPanel1().updateValue();
      getBillCardPanel1().updateUI();

      if (m_bGenerate)
        setPartNoEditable();
      // ��ʾ��ע
      UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem(
          "vnote").getComponent();
      nRefPanel1.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVnote());

      // ��ʾ�����֯
      setCalbodyValue();

      if (m_bGenerate) {
        // �ۼ�����ⵥ��
        getBillCardPanel1().transferFocusTo(BillCardPanel.HEAD);
      }

    }
  }

  /**
   * �˴����뷽��˵���� ��������:�����б�ѡ�� �������: ����ֵ: �쳣����: ����:2002/09/26
   */
  public void valueChanged(javax.swing.event.ListSelectionEvent event) {
    if ((ListSelectionModel) event.getSource() == getBillCardPanel2()
        .getBillTable().getSelectionModel()) {
      int nRow = getBillCardPanel2().getBillModel().getRowCount();

      // // ���������лָ���������ѡ��
      // for (int i = 0; i < nRow; i++) {
      // getBillCardPanel2().getBillModel().setRowState(i,
      // BillModel.UNSTATE);
      // }

      // ��ñ���ѡ������
      int nSelected[] = getBillCardPanel2().getBillTable().getSelectedRows();
      if (nSelected != null && nSelected.length > 0) {
        for (int i = 0; i < nSelected.length; i++) {
          int j = nSelected[i];
          if (j < nRow)
            getBillCardPanel2().getBillModel().setRowState(j,
                BillModel.SELECTED);
        }
      }

      int m = 0;
      for (int i = 0; i < getBillCardPanel2().getBillModel().getRowCount(); i++) {
        if (getBillCardPanel2().getBillModel().getRowState(i) == BillModel.SELECTED)
          m++;
      }
      if (m > 0) {
        // ȫ������
        m_nButtonState[0] = 0;
        m_nButtonState[1] = 0;
        m_nButtonState[2] = 0;
        m_nButtonState[3] = 0;
        m_nButtonState[4] = 0;
      }
      else {
        // ȫ��,�ݹ�Ϊ��
        m_nButtonState[1] = 1;
        m_nButtonState[3] = 1;
      }
      changeButtonState();

    }
    else if ((ListSelectionModel) event.getSource() == getBillListPanel()
        .getHeadTable().getSelectionModel()) {
      int nRow = getBillListPanel().getHeadBillModel().getRowCount();

      // ��ͷ�����лָ���������ѡ��
      for (int i = 0; i < nRow; i++) {
        getBillListPanel().getHeadBillModel().setRowState(i, BillModel.UNSTATE);
      }

      // ��ñ�ͷѡ������
      int nSelected[] = getBillListPanel().getHeadTable().getSelectedRows();
      if (nSelected != null && nSelected.length > 0) {
        for (int i = 0; i < nSelected.length; i++) {
          int j = nSelected[i];
          if (j < nRow)
            getBillListPanel().getHeadBillModel().setRowState(j,
                BillModel.SELECTED);
        }
      }
      // since v53
      setListBtnsState();
      //
    }
  }

  /*
   * since v53, �б�ť��ʾ�߼�ͳһ�㷨--�ݹ�����б�ѡ��״̬
   */
  private void setListBtnsStateEditable() {
    int nRow = getBillListPanel().getHeadBillModel().getRowCount();
    int n = 0;
    for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
      if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED)
        n++;
    }
    // m_buttons3[4] = "��Ƭ��ʾ";
//    if (n == 1 || nRow == 0) {
      m_nButtonState[4] = 0;
//   }
//    else {
//      m_nButtonState[4] = 1;
//    }
    // m_buttons3[3] = "��ѯ";
    // m_buttons3[0] = "ȫѡ";
    // m_buttons3[1] = "ȫ��";
    // m_buttons3[2] = "����";
    // m_buttons3[5] = "�ĵ�����";
    // m_buttons3[6] = "��ӡ";
    // m_buttons3[7] = "��ӡԤ��";
    m_nButtonState[3] = 1;
    m_nButtonState[0] = 1;
    m_nButtonState[1] = 1;
    m_nButtonState[2] = 1;
    m_nButtonState[5] = 1;
    m_nButtonState[6] = 1;
    m_nButtonState[7] = 1;
    //
    changeButtonState();
    //
    m_btnListLinkQuery.setEnabled(false);
    updateButton(m_btnListLinkQuery);
    //
  }

  /*
   * since v53, �б�ť��ʾ�߼�ͳһ�㷨
   */
  private void setListBtnsState() {
    // �����ڳ��ɹ����
    if (m_bAdd) {
      setListBtnsStateEditable();
      return;
    }
    // ����ڳ��ɹ����
    int nRow = getBillListPanel().getHeadBillModel().getRowCount();
    int n = 0;
    for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
      if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED)
        n++;
    }
    // m_buttons3[3] = "��ѯ";
    m_nButtonState[3] = 0;
    // m_buttons3[4] = "��Ƭ��ʾ";
    if (n == 1 || nRow == 0) {
      m_nButtonState[4] = 0;
    }
    else {
      m_nButtonState[4] = 1;
    }
    // m_buttons3[0] = "ȫѡ";
    if (nRow==0 || nRow > 0 && n == nRow) {
      m_nButtonState[0] = 1;
    }
    else {
      m_nButtonState[0] = 0;
    }
    // m_buttons3[1] = "ȫ��";
    // m_buttons3[2] = "����";
    // m_buttons3[5] = "�ĵ�����";
    // m_buttons3[6] = "��ӡ";
    // m_buttons3[7] = "��ӡԤ��";
    if (nRow > 0 && n != 0) {
      m_nButtonState[1] = 0;
      m_nButtonState[2] = 0;
      m_nButtonState[5] = 0;
      m_nButtonState[6] = 0;
      m_nButtonState[7] = 0;
    }
    else {
      m_nButtonState[1] = 1;
      m_nButtonState[2] = 1;
      m_nButtonState[5] = 1;
      m_nButtonState[6] = 1;
      m_nButtonState[7] = 1;
    }
    
    //
    changeButtonState();
    //
    m_btnListLinkQuery.setEnabled(nRow > 0 && m_buttons3[4].isEnabled());
    updateButton(m_btnListLinkQuery);
    //

  }

  /*
   * �ɹ���˾���ջ���˾��ͬ,��Ҫת��: ���, ��Ӧ��, ����, ��� 2006-07-20 xhq
   */
  private GeneralHVO[] switchVOForDifferentCorp(GeneralHVO billVO[])
      throws Exception {
    Vector v1 = new Vector(), v2 = new Vector();
    for (int i = 0; i < billVO.length; i++) {
      GeneralHHeaderVO headVO = billVO[i].getHeadVO();
      if (!headVO.getPk_corp().equals(headVO.getPk_purcorp()))
        v1.addElement(billVO[i]);
      else
        v2.addElement(billVO[i]);
    }

    // 1.ת����Ӧ�̹���ID
    Vector v3 = new Vector();
    for (int i = 0; i < v1.size(); i++) {
      GeneralHVO VO = (GeneralHVO) v1.elementAt(i);
      GeneralHHeaderVO headVO = VO.getHeadVO();
      if (headVO.getCproviderid() == null
          || headVO.getCproviderid().trim().length() == 0)
        continue;

      ChgDocPkVO switchVO = new ChgDocPkVO();
      switchVO.setDstCorpId(headVO.getPk_corp());
      switchVO.setSrcCorpId(headVO.getPk_purcorp());
      switchVO.setSrcManId(headVO.getCproviderid());
      v3.addElement(switchVO);
    }
    if (v3.size() > 0) {
      ChgDocPkVO switchVO[] = new ChgDocPkVO[v3.size()];
      v3.copyInto(switchVO);
      switchVO = ChgDataUtil.chgPkCuByCorp(switchVO);
      int j = 0;
      for (int i = 0; i < v1.size(); i++) {
        GeneralHVO VO = (GeneralHVO) v1.elementAt(i);
        GeneralHHeaderVO headVO = VO.getHeadVO();
        GeneralHItemVO bodyVO[] = VO.getBodyVO();
        if (headVO.getCproviderid() == null
            || headVO.getCproviderid().trim().length() == 0)
          continue;
        headVO.setCproviderid(switchVO[j].getDstManId());
        for (int k = 0; k < bodyVO.length; k++)
          bodyVO[k].setCvendorid(switchVO[j].getDstManId());
        j++;
      }
    }

    // 2.ת���������ID
    v3 = new Vector();
    // 3.ת������,���
    Vector v4 = new Vector(), v5 = new Vector();
    Hashtable tCurrTypeID = new Hashtable();// ��˾����
    for (int i = 0; i < v1.size(); i++) {
      GeneralHVO VO = (GeneralHVO) v1.elementAt(i);
      GeneralHHeaderVO headVO = VO.getHeadVO();
      GeneralHItemVO bodyVO[] = VO.getBodyVO();
      for (int j = 0; j < bodyVO.length; j++) {
        ChgDocPkVO switchVO = new ChgDocPkVO();
        switchVO.setDstCorpId(headVO.getPk_corp());
        switchVO.setSrcCorpId(headVO.getPk_purcorp());
        switchVO.setSrcBasId(bodyVO[j].getCbaseid());
        v3.addElement(switchVO);

        ChgPriceMnyVO switchVOs1 = new ChgPriceMnyVO();
        switchVOs1.setSrcCorpId(headVO.getPk_purcorp());
        Object oTemp = tCurrTypeID.get(headVO.getPk_purcorp());
        if (oTemp == null) {
          oTemp = SysInitBO_Client.getParaString(headVO.getPk_purcorp(),
              "BD301");
          tCurrTypeID.put(headVO.getPk_purcorp(), oTemp);
        }
        switchVOs1.setSrcCurrId(oTemp.toString());
        switchVOs1.setSrcVal(bodyVO[j].getNprice());
        switchVOs1.setDstCorpId(headVO.getPk_corp());
        switchVOs1.setDRateDate(getClientEnvironment().getDate());
        oTemp = tCurrTypeID.get(headVO.getPk_corp());
        if (oTemp == null) {
          oTemp = SysInitBO_Client.getParaString(headVO.getPk_corp(), "BD301");
          tCurrTypeID.put(headVO.getPk_corp(), oTemp);
        }
        switchVOs1.setDstCurrId(oTemp.toString());
        v4.addElement(switchVOs1);

        ChgPriceMnyVO switchVOs2 = new ChgPriceMnyVO();
        switchVOs2.setSrcCorpId(headVO.getPk_purcorp());
        oTemp = tCurrTypeID.get(headVO.getPk_purcorp());
        if (oTemp == null) {
          oTemp = SysInitBO_Client.getParaString(headVO.getPk_purcorp(),
              "BD301");
          tCurrTypeID.put(headVO.getPk_purcorp(), oTemp);
        }
        switchVOs2.setSrcCurrId(oTemp.toString());
        switchVOs2.setSrcVal(bodyVO[j].getNmny());
        switchVOs2.setDstCorpId(headVO.getPk_corp());
        oTemp = tCurrTypeID.get(headVO.getPk_corp());
        if (oTemp == null) {
          oTemp = SysInitBO_Client.getParaString(headVO.getPk_corp(), "BD301");
          tCurrTypeID.put(headVO.getPk_corp(), oTemp);
        }
        switchVOs2.setDstCurrId(oTemp.toString());
        switchVOs2.setDRateDate(getClientEnvironment().getDate());
        v5.addElement(switchVOs2);
      }
    }

    if (v3.size() == 0) {
      GeneralHVO VO[] = new GeneralHVO[v2.size()];
      v2.copyInto(VO);
      return VO;
    }
    ChgDocPkVO switchVO[] = new ChgDocPkVO[v3.size()];
    v3.copyInto(switchVO);
    switchVO = ChgDataUtil.chgPkInvByCorpBase(switchVO);

    if (v4.size() == 0 || v5.size() == 0) {
      GeneralHVO VO[] = new GeneralHVO[v2.size()];
      v2.copyInto(VO);
      return VO;
    }
    ChgPriceMnyVO switchVO1[] = new ChgPriceMnyVO[v4.size()];
    v4.copyInto(switchVO1);
    ChgPriceMnyVO switchVO2[] = new ChgPriceMnyVO[v5.size()];
    v5.copyInto(switchVO2);
    switchVO1 = ChgDataUtil.chgPriceByCorp(switchVO1);
    switchVO2 = ChgDataUtil.chgPriceByCorp(switchVO2);

    for (int i = 0; i < v1.size(); i++) {
      GeneralHVO VO = (GeneralHVO) v1.elementAt(i);
      GeneralHItemVO bodyVO[] = VO.getBodyVO();
      GeneralBb3VO bb3VO[] = VO.getGrandVO();
      for (int j = 0; j < bodyVO.length; j++) {
        bodyVO[j].setCinventoryid(switchVO[j].getDstManId());
        bodyVO[j].setNprice(switchVO1[j].getDstVal());
        bodyVO[j].setNmny(switchVO2[j].getDstVal());

        bb3VO[j].setNpprice(bodyVO[j].getNprice());
        bb3VO[j].setNpmoney(bodyVO[j].getNmny());
      }
    }

    // ����
    v3 = new Vector();
    for (int i = 0; i < v1.size(); i++)
      v3.addElement(v1.elementAt(i));
    for (int i = 0; i < v2.size(); i++)
      v3.addElement(v2.elementAt(i));
    GeneralHVO VO[] = new GeneralHVO[v3.size()];
    v3.copyInto(VO);

    return VO;
  }

  public Object[] getRelaSortObjectArray() {
    // TODO �Զ����ɷ������
    return m_VOs;
  }

  public void doMaintainAction(ILinkMaintainData maintaindata) {
    // TODO �Զ����ɷ������

  }

  public void doAddAction(ILinkAddData adddata) {
    // TODO �Զ����ɷ������

  }

  public void doApproveAction(ILinkApproveData approvedata) {
    // TODO �Զ����ɷ������

  }

  public void doQueryAction(ILinkQueryData querydata) {
    // TODO �Զ����ɷ������
    init();

    GeneralHVO tempVO = null;
    try {
      tempVO = EstimateHelper.queryStockByHeadKey(null, querydata.getBillID());
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000046")/* @res "����������" */, e
          .getMessage());
      return;
    }
    if (tempVO == null)
      return;

    String pk_corp = tempVO.getHeadVO().getPk_corp();
    InitialUIQryDlg dlg = null;
    dlg = initQueryModel1(dlg, pk_corp, true);
    ConditionVO conditionVO[] = dlg.getConditionVO();

    Vector vTemp = new Vector();
    ConditionVO condVO = new ConditionVO();
    condVO.setFieldCode("cgeneralhid");
    condVO.setOperaCode("=");
    condVO.setValue(tempVO.getHeadVO().getCgeneralhid());
    vTemp.addElement(condVO);
    if (conditionVO != null && conditionVO.length > 0) {
      for (int i = 0; i < conditionVO.length; i++)
        vTemp.addElement(conditionVO[i]);
    }
    conditionVO = new ConditionVO[vTemp.size()];
    vTemp.copyInto(conditionVO);

    try {
      GeneralHVO VOs[] = EstimateHelper.queryStockForEstimate(pk_corp,
          conditionVO);
      if (VOs != null && VOs.length > 0)
        tempVO = VOs[0];
      else
        tempVO = null;
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000046")/* @res "����������" */, e
          .getMessage());
      return;
    }

    getBillCardPanel1().setBillValueVO(tempVO);
    getBillCardPanel1().getBillModel().execLoadFormula();
    // ������Դ���ݺ�
    nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");
    getBillCardPanel1().updateValue();
    getBillCardPanel1().updateUI();
    // ��ʾ��ע
    UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem("vnote")
        .getComponent();
    nRefPanel1.setValue(tempVO.getHeadVO().getVnote());

    // ��ʾ�����֯
    setCalbodyValue();

    if (tempVO != null){
      m_VOs = new GeneralHVO[] {
        tempVO
      };
      //���鰴ťҲ����
      m_nButtonState[15]=0;
      changeButtonState();
    }
    else
      m_VOs = null;
  }

  public void setBillVO(AggregatedValueObject vo) {
    // TODO �Զ����ɷ������
    if (vo == null)
      return;

    GeneralHVO VO = (GeneralHVO) vo;
    getBillCardPanel1().setBillValueVO(VO);
    getBillCardPanel1().getBillModel().execLoadFormula();
    // ������Դ���ݺ�
    nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");
    getBillCardPanel1().updateValue();
    getBillCardPanel1().updateUI();
    // ��ʾ��ע
    UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem("vnote")
        .getComponent();
    nRefPanel1.setValue(VO.getHeadVO().getVnote());
    // ��ʾ�����֯
    setCalbodyValue();

  }
  
  /**
   * 
   * ��������������
   * <p>��ʼ���²�ѯ�Ի������Ϣ��
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * @author zhaoyha
   * @time 2008-10-15 ����07:57:41
   * @since 5.5
   */
  protected InitialQueryOrderDlg getNewQueryDlg(){
    
    if(m_condQrderDlg==null ){

      TemplateInfo tempinfo=new TemplateInfo();
      tempinfo.setPk_Org(m_sUnitCode);
      tempinfo.setCurrentCorpPk(m_sUnitCode);
      tempinfo.setFunNode(getModuleCode());
      tempinfo.setUserid(ClientEnvironment.getInstance().getUser().getPrimaryKey());

     //�����µĲ�ѯ�Ի���
      m_condQrderDlg=new InitialQueryOrderDlg(this,tempinfo,
          NCLangRes.getInstance().getStrByID("4004050302", "UPP4004050302-000006")
          /*
           * @res
           * "�ڳ��ݹ���ѯ"
           */);
      //�����Զ������
      m_condQrderDlg.setRefModel("po_order.cbiztype", SpeBiztypeDef.class, new Object[]{m_sBiztypeID});
    }
    return m_condQrderDlg;
  }
  
  private int nextBill(){
    int tempInx=m_nPresentRecord;
    while(++tempInx<m_VOs.length &&
        PuPubVO.getString_TrimZeroLenAsNull(m_VOs[tempInx].getHeadVO().getCgeneralhid())==null);
    if(tempInx<m_VOs.length)
      return tempInx;
    else
      return -1;
  }
  
  private int previousBill(){
    int tempInx=m_nPresentRecord;
    while(--tempInx>=0 &&
        PuPubVO.getString_TrimZeroLenAsNull(m_VOs[tempInx].getHeadVO().getCgeneralhid())==null);
    if(tempInx>=0)
      return tempInx;
    else
      return -1;
  }
  
  
}
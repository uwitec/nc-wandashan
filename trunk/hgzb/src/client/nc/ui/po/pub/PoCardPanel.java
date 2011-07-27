package nc.ui.po.pub;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.framework.common.NCLocator;
import nc.itf.ct.service.ICtToPo_QueryCt;
import nc.itf.dm.service.delivery.IDeliveryStatusUI;
import nc.itf.po.IOrder;
import nc.itf.po.rp.IPoReceivePlan;
import nc.itf.scm.cenpur.service.CentrPurchaseUtil;
import nc.itf.scm.coopwith.ICoopwithOut;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.ui.bd.b21.CurrParamQuery;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.busi.ComAbstrDefaultRefModel;
import nc.ui.bd.ref.busi.JobmngfilDefaultRefModel;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.ml.NCLangRes;
import nc.ui.po.OrderAfterBillHelper;
import nc.ui.po.OrderHelper;
import nc.ui.po.oper.OrderUI;
import nc.ui.po.oper.PoToftPanel;
import nc.ui.po.oper.ReplenishUI;
import nc.ui.po.oper.RevisionUI;
import nc.ui.po.ref.OtherRefModel;
import nc.ui.po.ref.PoDeliverAddressRefModel;
import nc.ui.po.ref.PoFreeCustBankRefModel;
import nc.ui.po.ref.PoInputInvRefModel;
import nc.ui.po.ref.PoReceiveAddrRefModel;
import nc.ui.pu.print.BillCardPanelTool;
import nc.ui.pu.pub.POPubSetUI;
import nc.ui.pu.pub.POPubSetUI2;
import nc.ui.pu.pub.PuGetUIValueTool;
import nc.ui.pu.pub.PuProjectPhaseRefModel;
import nc.ui.pu.pub.PuTool;
import nc.ui.pu.pub.PubHelper;
import nc.ui.pu.pub.PurDeptRefModel;
import nc.ui.pu.pub.PurPsnRefModel;
import nc.ui.pu.ref.PuBizTypeRefModel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillActionListener;
import nc.ui.pub.bill.BillBodyMenuListener;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillSortListener;
import nc.ui.pub.bill.BillTotalListener;
import nc.ui.pub.bill.IBillModelSortPrepareListener;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.scm.freecust.UFRefGridUI;
import nc.ui.scm.hqhp.QPSchmRefModel;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.inv.InvTool;
import nc.ui.scm.pu.ParaVOForBatch;
import nc.ui.scm.pub.BillTools;
import nc.ui.scm.pub.CacheTool;
import nc.ui.scm.pub.ct.CntSelDlg;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.ref.WarehouseRefModel;
import nc.ui.scm.service.LocalCallService;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.dm.service.delivery.SourceBillDeliveryStatus;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.po.OrderHeaderVO;
import nc.vo.po.OrderItemVO;
import nc.vo.po.OrderVO;
import nc.vo.po.OrderstatusVO;
import nc.vo.po.afterbill.OrderAfterBillVO;
import nc.vo.po.pub.OrderPubVO;
import nc.vo.po.pub.PoSaveCheckParaVO;
import nc.vo.po.pub.PoVendorVO;
import nc.vo.po.pub.RetPoVrmAndParaPriceVO;
import nc.vo.po.rp.OrderReceivePlanVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ProductCode;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.coopwith.ForStockStruct;
import nc.vo.scm.ctpo.RetCtToPoQueryVO;
import nc.vo.scm.field.pu.FieldMaxLength;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.RelationsCalVO;
import nc.vo.scm.pu.VariableConst;
import nc.vo.scm.pub.CustomerConfigVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.service.ServcallVO;
import nc.vo.sp.service.SalePriceVO;

public class PoCardPanel extends BillCardPanel implements BillEditListener,
    BillEditListener2, ActionListener, BillBodyMenuListener,
    IBillModelSortPrepareListener, BillCardBeforeEditListener,
    BillSortListener, BillActionListener, ChangeListener
    // since v55, ��Ʒ���ƺϼ�(�����۶�������һ��)
    , BillTotalListener {
  // since v53,�����к�
  private Map<String, UIMenuItem> m_mapReSortRowNo = null;
  private boolean isAfterEditWhenHeadVendor = false;
  private boolean isSetCnt = true;
  private Map<String, UIMenuItem> m_mapCardEdit = null;

  // since v55,�����кż���
  class IMenuItemListener implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      PoCardPanel.this.onMenuItemClick(event);
    };
  }
//  private static String sCurrId2="";
  private String m_strCbiztype = null;

  private POPubSetUI2 m_cardPoPubSetUI2 = null;

  private CntSelDlg cntSelDlg = null;

  // ��ѯ����״̬�������ĺ���״̬
  private HashMap h_mapAfterBill = new HashMap();

  // �����ߣ��õ�������ʵ�ֽӿ�nc.ui.po.pub.PoCardPanelInterface
  private Container m_conInvoker = null;

  private String[] m_saBodyRefItemKey = null;

  // ��ͷ���������в��յ�ITEMKEY
  private String[] m_saHeadRefItemKey = null;

  private static String NO_AFTERBILL = "";

  // ���������ۿ�
  private int iChange = 7;

  //
  private boolean addOneMore = false;

  //
  private static ArrayList m_listFixedFields = new ArrayList();

  private boolean bIsSoZ = false;

  boolean binv = false;
  
  HashMap<String ,Boolean> m_hmProdEnable = new HashMap<String, Boolean>();

//  private PsndocVO m_voPsnDoc = null;
  // ����״̬�Ի���, duy, �ṩ����״̬UI,���PUֱ������DM�����򵥲�Ʒ����������
  private IDeliveryStatusUI sbdsUI = null;
  
  // �к����������� �����޸ĵ��
  // �кš���ͬ�š��������������κš���˰���˰�ʡ����֡�ʹ�ò��š���Ŀ����Ŀ�׶Ρ��ջ��ֿ⡢�շ�����ַ���۱����ʡ��۸����ʡ���Ʒ
  // V5�����ջ���˾���ջ������֯
  private static String[] saFixedItem = new String[] {
      "crowno", "ccontractcode", "cinventorycode", "vfree", "vproducenum",
      "idiscounttaxtype", "ntaxrate", "ccurrencytype", "cusedept", "cproject",
      "cprojectphase", "cwarehouse", "vreceiveaddress", "nexchangeotobrate",
      "blargess", "arrvcorpname", "arrvstoorgname"
  };
  /* V5ע�⣺��Ʊ��˾ҵ���߼�������ͬ����Ҫ�����μ�beforeEdit() */
  static {
    for (int i = 0; i < saFixedItem.length; i++) {
      m_listFixedFields.add(saFixedItem[i]);
    }
  }

  public UIMenuItem[] getCardEditMenu() {
    if (m_mapCardEdit == null || m_mapCardEdit.size() == 0) {
      return null;
    }
    String[] saTableCode = getBillData().getBodyTableCodes();
    if (saTableCode == null || saTableCode.length == 0) {
      return null;
    }
    UIMenuItem[] cardEditMenu = new UIMenuItem[saTableCode.length];
    for (int i = 0; i < saTableCode.length; i++) {
      if (saTableCode[i] == null) {
        continue;
      }
      cardEditMenu[i] = m_mapCardEdit.get(saTableCode[i]);
    }
    return cardEditMenu;
  }

  public void setSoZ(boolean bisSoZ) {
    bIsSoZ = bisSoZ;
  }

  /**
   * ��ȡ���ҳǩ���Ҽ������˵��еġ������кš�
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * 
   * @return <p>
   * @author czp
   * @time 2008-8-19 ����07:25:20
   */
  public UIMenuItem[] getMiReSortRowNos() {
    if (m_mapReSortRowNo == null || m_mapReSortRowNo.size() == 0) {
      return null;
    }
    String[] saTableCode = getBillData().getBodyTableCodes();
    if (saTableCode == null || saTableCode.length == 0) {
      return null;
    }
    UIMenuItem[] miReSortRowNos = new UIMenuItem[saTableCode.length];
    for (int i = 0; i < saTableCode.length; i++) {
      if (saTableCode[i] == null) {
        continue;
      }
      miReSortRowNos[i] = m_mapReSortRowNo.get(saTableCode[i]);
    }
    return miReSortRowNos;
  }

  /**
   * ���ߣ����� ���ܣ����ݸı�ʱ�������¼� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-10-21 WYF ���뺬˰�����޸ĺ󴥷��Ĵ���
   */
  public void afterEdit(BillEditEvent e) {
    if(m_conInvoker instanceof PoToftPanel)
      ((PoToftPanel) m_conInvoker).getInvokeEventProxy().afterEdit(e);
    // ��ͷ
    if (e.getPos() == BillItem.HEAD) {
      // ���Ǳ༭״̬��������༭���¼�
      if (!getBillData().getHeadItem(e.getKey()).isEnabled()) {
        return;
      }
      if (e.getSource() == getHeadItem("ccurrencytypeid").getComponent()) {
        // ����
        afterEditWhenHeadCurrency(e);
      }
      if (e.getSource() == getHeadItem("idiscounttaxtype").getComponent()) {
        // ��˰���
        afterEditWhenHeadDiscountTaxType(e);
      }
      else if (e.getSource() == getHeadItem("ntaxrate").getComponent()) {
        // ˰��
        afterEditWhenHeadTaxRate(e);
      }
      else if (e.getSource() == getHeadItem("nexchangeotobrate").getComponent()) {
        // ����
        afterEditWhenHeadExchangeRate(e);
      }
      else if (e.getSource() == getHeadItem("cvendormangid").getComponent()) {
        afterEditWhenHeadVendor(e);
      }
      else if (e.getSource() == getHeadItem("cfreecustid").getComponent()) {
        // ɢ��
        afterEditWhenHeadFreecust(e);
      }
      else if (e.getSource() == getHeadItem("cdeptid").getComponent()) {
        // ����
        afterEditWhenHeadDept(e);
      }
      else if (e.getSource() == getHeadItem("cemployeeid").getComponent()) {
        // ҵ��Ա
        afterEditWhenHeadEmployee(e);
      }
      else if (e.getSource() == getHeadItem("creciever").getComponent()) {
        // �ջ���
        afterEditWhenHeadReciever(e);
      }
      else if (e.getSource() == getHeadItem("dorderdate").getComponent()) {
        // ��������
        afterEditWhenHeadOrderDate(e);
      }
      else if (e.getSource() == getHeadItem("cbiztype").getComponent()) {
        m_strCbiztype = getHeadItem("cbiztype").getValue();
        afterEditWhenHeadBiztype(e);
      }
      else if (e.getSource() == getHeadItem("ctransmodeid").getComponent()) {
        int iCount = getRowCount();
        if (iCount > 0) {
//          for (int i = 0; i < iCount; i++) {
//            getBillModel().setValueAt(null, i, "ccontractid");
//            getBillModel().setValueAt(null, i, "ccontractrowid");
//            getBillModel().setValueAt(null, i, "ccontractcode");
//          }
//          setRelated_Cnt(0, iCount - 1, false, false);

          setRelated_MaxPrice(0, iCount - 1);
        }
      }
      else if (e.getSource() == getHeadItem("cprojectid").getComponent()) {
        // ��Ŀ
        afterEditWhenHeadProject(e);
      }
      /*
       * V5 Del: else if (e.getSource() == getHeadItem("cstoreorganization")
       * .getComponent()) { //�����֯
       * afterEditWhenHeadStoreOrg(e,getHeadItem("cstoreorganization"
       * ).getValue()); }
       */
      else if (e.getSource() == getHeadItem("vmemo").getComponent()) {
        // ��ע
        afterEditWhenHeadMemo(e);
      }
      // else if (e.getSource() ==
      // getHeadItem("cdeliveraddress").getComponent()) {
      // //��Ӧ���շ�����ַ
      // afterEditWhenHeadAddr(e);
      // }
      // else if (e.getSource() ==
      // getHeadItem("bdeliver").getComponent()){
      // afterEditWhenHeadDeliver(e);
      // }
      // ��ͷ�Զ�����
      else if (e.getKey().startsWith("vdef")) {
        DefSetTool.afterEditHead(getBillData(), e.getKey(), "pk_defdoc"
            + e.getKey().substring(e.getKey().indexOf("vdef") + 4));
      }
      // �ޱ�����
      if (PuTool.isLastCom(this, e) && getBillModel().getRowCount() <= 0) {
        onActionAppendLine();
      }
      PuTool.setFocusOnLastCom(this, e);
      if (getHeadItem(e.getKey()).getEditFormulas() != null) {
        execHeadFormulas(getHeadItem(e.getKey()).getEditFormulas());
      }
    }
    // ����
    else if (e.getPos() == BillItem.BODY) {
      int iRow = e.getRow();

      // �뵥������ЩIF�������ж��븨�����Ĺ�ϵ
      if (e.getKey().equals("nordernum")) {
        /*
         * V5:ȡ�����������������������븨�����Ŀ��� afterEditWhenBodyNum(e);
         */
        calDplanarrvdate(e.getRow(), e.getRow());
      }
      if (e.getKey().equals("crowno")) {
        // �к�
        BillRowNo.afterEditWhenRowNo(this, e, "crowno");
      }
      else if (e.getKey().equals("cinventorycode")) {
        // �޸Ĵ��
        afterEditWhenBodyInventory(e);
      }
      else if (e.getKey().equals("ccurrencytype")) {
        // ����
        afterEditWhenBodyCurrency(e,true);
      }
      else if (e.getKey().equals("idiscounttaxtype")
          || e.getKey().equals("nordernum")
          || e.getKey().equals("noriginalcurprice")
          || e.getKey().equals("noriginalnetprice")
          || e.getKey().equals("ntaxrate")
          || e.getKey().equals("noriginalcurmny")
          || e.getKey().equals("noriginaltaxmny")
          || e.getKey().equals("noriginaltaxpricemny")
          || e.getKey().equals("ndiscountrate")
          || e.getKey().equals("nconvertrate")
          || e.getKey().equals("nassistnum")
          || e.getKey().equals("norgtaxprice")
          || e.getKey().equals("norgnettaxprice")) {
        // ���������ۡ����
        afterEditWhenBodyRelationsCal(e);
      }
      else if (e.getKey().equals("nexchangeotobrate")) {
        afterEditWhenBodyExchangeRate(e);
      }
      else if (e.getKey().equals("cassistunitname")) {
        // ��������λ
        afterEditWhenBodyAssistunit(e);
      }
      else if (e.getKey().equals("vfree")) {
        // ������
        PoEditTool.afterEditWhenBodyFree(getBillModel(), iRow, "vfree");
      }
      else if (e.getKey().equals("cproject")) {
        // ��Ŀ
        afterEditWhenBodyProject(e);
      }
      else if (e.getKey().equals("vmemo")) {
        // ��ע
        afterEditWhenBodyMemo(e);
      }
      else if (e.getKey().equals("ccontractcode")) {
        // ��ͬ
        afterEditWhenBodyCntCode(e);
      }
      else if (e.getKey().equals("vreceiveaddress")) {
        // �շ�����ַ
        afterEditWhenBodyAddr(e);
      }
      else if (e.getKey().equals("vvenddevaddr")) {
        // ��Ӧ���շ�����ַ
        afterEditWhenBodyAddrVendor(e);
      }
      else if (e.getKey().startsWith("vdef")) {
        // �����Զ�����
        DefSetTool.afterEditBody(getBillModel(), e.getRow(), e.getKey(),
            "pk_defdoc" + e.getKey().substring(e.getKey().indexOf("vdef") + 4));
      }
      else if (e.getKey().startsWith("arrvcorpname")) {
        // �ջ���˾
        afterEditBodyArrCorp(e);
      }
      else if (e.getKey().startsWith("arrvstoorgname")) {
        // �ջ������֯
        afterEditWhenBodyArrStoOrg(e);
      }
      else if (e.getKey().equals("cwarehouse")) {
        // �ջ��ֿ�
        afterEditWhenBodyArrWare(e);
      }
      else if (e.getKey().equals("reqcorpname")) {
        // ����˾
        afterEditWhenBodyReqCorp(e);
      }
      else if (e.getKey().equals("reqstoorgname")) {
        // ��������֯
        afterEditWhenBodyReqStoOrg(e);
      }
      else if (e.getKey().equals("reqwarename")) {
        // ����ֿ�
        afterEditWhenBodyReqWare(e);
      }
      else if (e.getKey().equals("blargess")) {
        // �Ƿ���Ʒ
        afterEditWhenBodyBlargess(e);
      }
      else if (e.getKey().equals("cdevareaid")) {
        afterEditWhenBodyInventory(e);
      }
    }

  }
  
  /**
   * 
   * ����������������ͷ��˰���༭�����������˰���
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param event
   * <p>
   * @author tianft
   * @time 2010-8-12 ����08:53:19
   */
  private void afterEditWhenHeadDiscountTaxType(BillEditEvent event) {
  	if (getHeadTailItem("idiscounttaxtype") != null) {
    	Object value = getHeadTailItem("idiscounttaxtype").getValueObject();
      if(value != null && !"".equals(value)){
      	//2010-08-12  tianft ��ͷ��˰���Ķ������������˰��𣬲��������۽���
      	changeBodyDiscountTaxType();
      }
    }
  }
  
  /**
   * 
   * ����������������ͷ��˰���Ķ������������˰��𣬲��������۽��㡣
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * @author tianft
   * @time 2010-8-12 ����07:47:31
   */
  private void changeBodyDiscountTaxType(){
  	BillEditEvent e = null;
  	int rows = getRowCount();
  	if(rows == 0){
  		return;
  	}
  	Object headTaxType = getHeadItem("idiscounttaxtype").getValueObject();
  	if(headTaxType == null){
  		return;
  	}
  	Object bodyTaxType = null;
  	for(int i = 0; i < rows; i++){
  		bodyTaxType = getBodyValueAt(i, "idiscounttaxtype");
  		// �����˰������ͷ��˰�����ȣ�������
  		if(bodyTaxType != null 
  				&& headTaxType.toString().equals(bodyTaxType.toString())){
  			continue;
  		}
  		// 1.�����˰���ȡ��ͷ��˰���
  		setBodyValueAt(headTaxType, i, "idiscounttaxtype");
      //���ֶ��޸���״̬
      getBillModel().setRowState(i, BillModel.MODIFICATION);
      
      e = new BillEditEvent(getBodyItem("idiscounttaxtype").getComponent(), bodyTaxType, headTaxType, "idiscounttaxtype", i, BillItem.BODY);
      // 2.�����˰���仯�����¼���
      afterEditWhenBodyRelationsCal(e);
  	}
  }
  
  private void afterEditWhenHeadBiztype(BillEditEvent event) {
  	// ���ڷ��ռ����ҵ�����̣���Ʊ��˾ȡ��ǰ��˾
  	String pk_corp = (String)getHeadTailItem("pk_corp").getValueObject();
  	if (pk_corp == null)
  		return;
    try{
	    Object[] oaTemp = (Object[]) CacheTool.getCellValue("bd_busitype","pk_busitype","defaultgather",m_strCbiztype);
			if(oaTemp != null && oaTemp.length > 0 && oaTemp[0] != null){
				// ���ռ���
				if (((Integer)oaTemp[0]).intValue() != 2)
					return;
				
				int rowcount = getRowCount();
				for (int i = 0; i < rowcount; i++) {
					String pk_invcorp = (String)getBodyValueAt(i, "pk_invoicecorp");
					if (!pk_corp.equals(pk_invcorp)) {
						setBodyValueAt(pk_corp, i, "pk_invoicecorp");
						execBodyFormula(i, "invoicecorpname");
					}
				}
			}
    }catch(Exception ex){
    	ex.printStackTrace();
    }
  	
  }

  /**
   * �༭���¼�����Ʒ
   */
  private void afterEditWhenBodyBlargess(BillEditEvent event) {
    if (getBodyValueAt(event.getRow(), "blargess") != null
        && getBodyValueAt(event.getRow(), "blargess").toString()
            .equals("false")) {
      if (getBodyValueAt(event.getRow(), "noriginalnetprice") != null) {
        UFDouble dd = (UFDouble) getBodyValueAt(event.getRow(),
            "noriginalnetprice");
        if (dd.intValue() == 0) {
          setBodyValueAt(null, event.getRow(), "noriginalnetprice");
          setBodyValueAt(null, event.getRow(), "norgnettaxprice");
        }
      }
    }
    getBillModel().reCalcurateAll();
  }

  /**
   * �༭���¼�������ֿ�
   */
  private void afterEditWhenBodyReqWare(BillEditEvent e) {

    String strPkCalBody = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e
        .getRow(), "pk_reqstoorg"));
    String strPkWare = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e
        .getRow(), "pk_creqwareid"));
    if (strPkWare == null) {
      SCMEnv.out("�޸�����ֿ�ʱ��������������֯������˾Ĭ��ֵ,�ֿ�Ϊ�գ�ֱ�ӷ��ء�");
      return;
    }
    Object[] oaRet = null;
    try {
      // ������������֯
      if (strPkCalBody == null) {
        oaRet = (Object[]) CacheTool.getCellValue("bd_stordoc", "pk_stordoc",
            "pk_calbody", strPkWare);
        if (oaRet == null || oaRet.length == 0 || oaRet[0] == null
            || oaRet[0].toString().trim().length() == 0) {
          SCMEnv.out("���ݲֿ⵵��ID[IDֵ����" + strPkWare + "��]���ܻ�ȡ���������֯ID!");
        }
        else {
          strPkCalBody = oaRet[0].toString().trim();
          // -----------------
          setBodyValueAt(strPkCalBody, e.getRow(), "pk_reqstoorg");
          oaRet = (Object[]) CacheTool.getCellValue("bd_calbody", "pk_calbody",
              "bodyname", strPkCalBody);
          if (oaRet == null || oaRet.length == 0 || oaRet[0] == null
              || oaRet[0].toString().trim().length() == 0) {
            SCMEnv.out("�������������֯ID[IDֵ����" + strPkCalBody + "��]���ܻ�ȡ���������֯����!");
          }
          else {
            // --------------
            setBodyValueAt(oaRet[0], e.getRow(), "reqstoorgname");
          }
        }
      }
      /*
       * //��������˾ if(strReqCorp == null){ oaRet = (Object[])
       * CacheTool.getCellValue("bd_stordoc","pk_stordoc","pk_corp",strPkWare);
       * if(oaRet == null || oaRet.length == 0 || oaRet[0] == null ||
       * oaRet[0].toString().trim().length() == 0){
       * SCMEnv.out("��������ֿ⵵��ID[IDֵ����"+strPkWare+"��]���ܻ�ȡ������˾ID!"); }else{
       * strReqCorp = oaRet[0].toString().trim(); //----------------
       * setBodyValueAt(strReqCorp,e.getRow(),"pk_reqcorp"); oaRet = (Object[])
       * CacheTool.getCellValue("bd_corp","pk_corp","unitname",strReqCorp);
       * if(oaRet == null || oaRet.length == 0 || oaRet[0] == null ||
       * oaRet[0].toString().trim().length() == 0){
       * SCMEnv.out("����������˾ID[IDֵ����"+strReqCorp+"��]���ܻ�ȡ������˾����!"); }else{
       * //------------------ setBodyValueAt(oaRet[0],e.getRow(),"reqcorpname");
       * } } }
       */
    }
    catch (BusinessException be) {
      SCMEnv.out(be.getMessage());
    }
  }

  /**
   * �༭���¼�����������֯
   */
  private void afterEditWhenBodyReqStoOrg(BillEditEvent e) {
    /*
     * String strReqCorp =
     * PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e.getRow
     * (),"pk_reqcorp")); String strPkCalBody =
     * PuPubVO.getString_TrimZeroLenAsNull
     * (getBodyValueAt(e.getRow(),"pk_reqstoorg")); //��������˾ if(strReqCorp ==
     * null && strPkCalBody != null){ Object[] oaRet = null; try{ oaRet =
     * (Object[])
     * CacheTool.getCellValue("bd_calbody","pk_calbody","pk_corp",strPkCalBody);
     * }catch(BusinessException be){ SCMEnv.out(be.getMessage()); } if(oaRet ==
     * null || oaRet.length == 0 || oaRet[0] == null ||
     * oaRet[0].toString().trim().length() == 0){
     * SCMEnv.out("������������֯ID[IDֵ����"+strPkCalBody+"��]���ܻ�ȡ������˾ID!"); return; }
     * setBodyValueAt(oaRet[0],e.getRow(),"pk_reqcorp"); try{ oaRet = (Object[])
     * CacheTool
     * .getCellValue("bd_corp","pk_corp","unitname",oaRet[0].toString().trim());
     * }catch(BusinessException be){ SCMEnv.out(be.getMessage()); } if(oaRet ==
     * null || oaRet.length == 0 || oaRet[0] == null ||
     * oaRet[0].toString().trim().length() == 0){
     * SCMEnv.out("���ݹ�˾ID[IDֵ����"+oaRet[0]+"��]���ܻ�ȡ��˾����!"); return; }
     * setBodyValueAt(oaRet[0],e.getRow(),"reqcorpname"); }
     */
    // �������ֿ�
    setBodyValueAt(null, e.getRow(), "reqwarename");
    setBodyValueAt(null, e.getRow(), "pk_creqwareid");
  }

  /**
   * �༭���¼�������˾
   */
  private void afterEditWhenBodyReqCorp(BillEditEvent e) {
    // �����������֯
    setBodyValueAt(null, e.getRow(), "reqstoorgname");
    setBodyValueAt(null, e.getRow(), "pk_reqstoorg");
    // �������ֿ�
    setBodyValueAt(null, e.getRow(), "reqwarename");
    setBodyValueAt(null, e.getRow(), "pk_creqwareid");
  }

  /**
   * <p>
   * ���ܣ��༭���¼����ջ��ֿ�
   * <p>
   * ���ߣ���־ƽ
   * <p>
   * ���ڣ�2006-03-08
   * <p>
   * 
   * @since V31SP1
   */
  private void afterEditWhenBodyArrWare(BillEditEvent e) {

    // �޸Ĳֿ�ʱ��������ַ���ܱ仯
    String sRevPk = getHeadItem("creciever").getValue();
    int iRow = e.getRow();

    // ���ͷ���ջ���Ϊ�գ���Ĭ��Ϊ�ֿ�ĵ�ַ���ɱ༭
    if (PuPubVO.getString_TrimZeroLenAsNull(sRevPk) == null) {

      Object sWareId = getBillModel().getValueAt(iRow, "cwarehouseid");
      Object[][] oaVal = null;
      // ����ֿ�ĵ�ַΪ�գ���Ĭ��Ϊ��ͷ�����֯Ĭ�ϵ�ַ���������ص�
      if (PuPubVO.getString_TrimZeroLenAsNull(sWareId) == null) {
        getBillModel().setValueAt(null, iRow, "vreceiveaddress");
        getBillModel().setValueAt(null, iRow, "cdevareaid");
        getBillModel().setValueAt(null, iRow, "cdevaddrid");
        getBillModel().setValueAt(null, iRow, "cdevareaname");
        getBillModel().setValueAt(null, iRow, "cdevaddrname");
        /*
         * V5��ɾ��cstoreorganization����ص��� String sStorOrgId =
         * getHeadItem("cstoreorganization").getValue(); oaVal =
         * CacheTool.getMultiColValue("bd_calbody", "pk_calbody", new
         * String[]{"area","pk_areacl","pk_address"}, new String[]{sStorOrgId});
         * String sStorOrgAddr = null; String sStorOrgAreaId = null; String
         * sStorOrgAddrId = null; if(oaVal != null && oaVal.length > 0 &&
         * oaVal[0] != null){ sStorOrgAddr = (String) oaVal[0][0];
         * sStorOrgAreaId = (String) oaVal[0][1]; sStorOrgAddrId = (String)
         * oaVal[0][2]; } getBillModel().setValueAt(sStorOrgAddr, iRow,
         * "vreceiveaddress"); getBillModel().setValueAt(sStorOrgAreaId, iRow,
         * "cdevareaid"); getBillModel().setValueAt(sStorOrgAddrId, iRow,
         * "cdevaddrid"); oaVal = CacheTool.getMultiColValue("bd_areacl",
         * "pk_areacl", new String[]{"areaclname"}, new
         * String[]{sStorOrgAreaId}); String sAreaName = null; if(oaVal != null
         * && oaVal.length > 0 && oaVal[0] != null){ sAreaName = (String)
         * oaVal[0][0]; } getBillModel().setValueAt(sAreaName, iRow,
         * "cdevareaname"); oaVal = CacheTool.getMultiColValue("bd_address",
         * "pk_address", new String[]{"addrname"}, new
         * String[]{sStorOrgAddrId}); String sAddrName = null; if(oaVal != null
         * && oaVal.length > 0 && oaVal[0] != null){ sAddrName = (String)
         * oaVal[0][0]; } getBillModel().setValueAt(sAddrName, iRow,
         * "cdevaddrname");
         */
      }
      else {
        oaVal = CacheTool.getMultiColValue("bd_stordoc", "pk_stordoc",
            new String[] {
                "storaddr", "pk_address"
            }, new String[] {
              sWareId.toString()
            });
        String sWareAddr = null;
        String sWareAddrId = null;
        if (oaVal != null && oaVal.length > 0 && oaVal[0] != null) {
          sWareAddr = (String) oaVal[0][0];
          sWareAddrId = (String) oaVal[0][1];
        }
        // �ص�����
        oaVal = CacheTool.getMultiColValue("bd_address", "pk_address",
            new String[] {
              "addrname"
            }, new String[] {
              sWareAddrId
            });
        String sWareAddrName = null;
        if (oaVal != null && oaVal.length > 0 && oaVal[0] != null) {
          sWareAddrName = (String) oaVal[0][0];
        }
        // �ֿ����������ɲֿ������ص����
        oaVal = CacheTool.getMultiColValue("bd_address", "pk_address",
            new String[] {
              "pk_areacl"
            }, new String[] {
              sWareAddrId
            });
        String sWareAreaId = null;
        if (oaVal != null && oaVal.length > 0 && oaVal[0] != null) {
          sWareAreaId = (String) oaVal[0][0];
        }
        // ��������
        oaVal = CacheTool.getMultiColValue("bd_areacl", "pk_areacl",
            new String[] {
              "areaclname"
            }, new String[] {
              sWareAreaId
            });
        String sWareAreaName = null;
        if (oaVal != null && oaVal.length > 0 && oaVal[0] != null) {
          sWareAreaName = (String) oaVal[0][0];
        }
        if (PuPubVO.getString_TrimZeroLenAsNull(sWareAddr) != null) {
          getBillModel().setValueAt(sWareAddr, iRow, "vreceiveaddress");
        }
        if (PuPubVO.getString_TrimZeroLenAsNull(sWareAreaId) != null) {
          getBillModel().setValueAt(sWareAreaId, iRow, "cdevareaid");
        }
        if (PuPubVO.getString_TrimZeroLenAsNull(sWareAddrId) != null) {
          getBillModel().setValueAt(sWareAddrId, iRow, "cdevaddrid");
        }
        if (PuPubVO.getString_TrimZeroLenAsNull(sWareAreaName) != null) {
          getBillModel().setValueAt(sWareAreaName, iRow, "cdevareaname");
        }
        if (PuPubVO.getString_TrimZeroLenAsNull(sWareAddrName) != null) {
          getBillModel().setValueAt(sWareAddrName, iRow, "cdevaddrname");
        }
      }

    }
    // ��ͷ�ջ������գ���ȡ��ͷ�ջ������Ĭ�ϵ�ַ
    else {
      setDefaultValueWhenHeadRecieverChged(new BillEditEvent(getBodyItem(
          "cwarehouse").getComponent(), null, "cwarehouse", e.getRow(),
          BillItem.BODY), sRevPk);
    }
    /*
     * V5��ɾ��cstoreorganization�����ص��� //���ֿ��Ƿ�������֯����
     * if(getHeadItem("cstoreorganization").getValue() != null){
     * PuTool.afterEditWareValidWithStoreOrg(getBillModel(), e, getHeadItem(
     * "cstoreorganization").getValue(), "cwarehouseid", new String[] {
     * "cwarehouse" }); }
     */

    // ---V5:�޸��ջ��ֿ�ʱ�������ջ������֯���ջ���˾Ĭ��ֵ
    String strPkCalBody = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e
        .getRow(), "pk_arrvstoorg"));

    //��������֯��Ϊ�գ��򲻽�������Ĵ���  by zhaoyha at 2009.12.1
    if(!StringUtil.isEmptyWithTrim(strPkCalBody)){
      afterBatch(e, e.getKey());
      return;
    } 
    
    String strPkWare = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e
        .getRow(), "cwarehouseid"));
    if (strPkWare == null) {
      SCMEnv.out("�޸��ջ��ֿ�ʱ�������ջ������֯���ջ���˾Ĭ��ֵ,�ֿ�Ϊ�գ�ֱ�ӷ��ء�");
      return;
    }

    Object[] oaRet = null;
    try {
      // �����ջ������֯

      oaRet = (Object[]) CacheTool.getCellValue("bd_stordoc", "pk_stordoc",
          "pk_calbody", strPkWare);
      if (oaRet == null || oaRet.length == 0 || oaRet[0] == null
          || oaRet[0].toString().trim().length() == 0) {
        SCMEnv.out("���ݲֿ⵵��ID[IDֵ����" + strPkWare + "��]���ܻ�ȡ���������֯ID!");
      }
      else {
        strPkCalBody = oaRet[0].toString().trim();
        // -----------------
        setBodyValueAt(strPkCalBody, e.getRow(), "pk_arrvstoorg");
        oaRet = (Object[]) CacheTool.getCellValue("bd_calbody", "pk_calbody",
            "bodyname", strPkCalBody);
        if (oaRet == null || oaRet.length == 0 || oaRet[0] == null
            || oaRet[0].toString().trim().length() == 0) {
          SCMEnv.out("�������������֯ID[IDֵ����" + strPkCalBody + "��]���ܻ�ȡ���������֯����!");
        }
        else {
          // --------------
          setBodyValueAt(oaRet[0], e.getRow(), "arrvstoorgname");
        }
      }
      ((UIRefPane) getBodyItem("arrvstoorgname").getComponent())
          .setPK(strPkCalBody);

      if (((UIRefPane) getBodyItem("arrvstoorgname").getComponent())
          .getRefModel().getPkValue() == null) {
        setBodyValueAt(null, e.getRow(), "pk_arrvstoorg");
        setBodyValueAt(null, e.getRow(), "arrvstoorgname");
      }
      /*
       * //�����ջ���˾ if(strArrCorp == null){ oaRet = (Object[])
       * CacheTool.getCellValue("bd_stordoc","pk_stordoc","pk_corp",strPkWare);
       * if(oaRet == null || oaRet.length == 0 || oaRet[0] == null ||
       * oaRet[0].toString().trim().length() == 0){
       * SCMEnv.out("���ݲֿ⵵��ID[IDֵ����"+strPkWare+"��]���ܻ�ȡ������˾ID!"); }else{
       * strArrCorp = oaRet[0].toString().trim(); //----------------
       * setBodyValueAt(strArrCorp,e.getRow(),"pk_arrvcorp"); oaRet = (Object[])
       * CacheTool.getCellValue("bd_corp","pk_corp","unitname",strArrCorp);
       * if(oaRet == null || oaRet.length == 0 || oaRet[0] == null ||
       * oaRet[0].toString().trim().length() == 0){
       * SCMEnv.out("����������˾ID[IDֵ����"+strArrCorp+"��]���ܻ�ȡ������˾����!"); }else{
       * //------------------
       * setBodyValueAt(oaRet[0],e.getRow(),"arrvcorpname"); } } }
       */
      afterBatch(e, e.getKey());
    }
    catch (BusinessException be) {
      SCMEnv.out(be.getMessage());
    }
  }

  /**
   * <p>
   * �༭���¼����ջ������֯
   * <p>
   * i.����ջ��ֿ�
   * <p>
   * ii.����ƻ���������
   */
  private void afterEditWhenBodyArrStoOrg(BillEditEvent e) {
    int iRow = e.getRow();
    afterBatch(e, e.getKey());
    // ����ջ��ֿ�
    setBodyValueAt(null, iRow, "cwarehouse");
    setBodyValueAt(null, iRow, "cwarehouseid");
    // ����ƻ���������
    calDplanarrvdate(iRow, iRow);

  }

  /**
   * ������������
   */
  private void afterBatch(BillEditEvent e, String key) {
    if (e.getValue() instanceof DefaultConstEnum) {
      String value = (String) ((DefaultConstEnum) e.getValue()).getValue();
      Object[] obj = null;
      String tablename = null;
      String pkname = null;
      if ("arrvstoorgname".equals(key)) {
        tablename = "bd_calbody";
        pkname = "pk_calbody";
      }
      else if ("cwarehouse".equals(key)) {
        tablename = "bd_stordoc";
        pkname = "pk_stordoc";
      }
      try {
        obj = (Object[]) CacheTool.getCellValue(tablename, pkname, "pk_corp",
            value);
        if (obj[0] != null) {
          setBodyValueAt(obj[0], e.getRow(), "pk_arrvcorp");
        }
        execBodyFormulas(e.getRow(), getBodyItem("arrvcorpname")
            .getEditFormulas());
      }
      catch (Exception ex) {
        SCMEnv.out(ex);
      }
    }
  }

  /**
   * �༭���¼����ջ���˾
   */
  private void afterEditBodyArrCorp(BillEditEvent e) {
    // ����ջ������֯
    setBodyValueAt(null, e.getRow(), "arrvstoorgname");
    setBodyValueAt(null, e.getRow(), "pk_arrvstoorg");
    // ����ջ��ֿ�
    setBodyValueAt(null, e.getRow(), "cwarehouse");
    setBodyValueAt(null, e.getRow(), "cwarehouseid");
    // ��������ż�
    setBodyValueAt(null, e.getRow(), "cqpbaseschemeid");
    setBodyValueAt(null, e.getRow(), "cqpbaseschemename");
  }

  /**
   * ���ߣ����� ���ܣ�����ѡ���иı�ʱ�������¼� �÷���Ϊ�����¼����� ������BillEditEvent e ��׽����BillEditEvent�¼�
   * ���أ��� ���⣺�� ���ڣ�(2001-10-20 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public void bodyRowChange(BillEditEvent e) {
  }

  /**
   * PoOrderManage ������ע�⡣
   */
  public PoCardPanel(
      Container cntInvoker, String sCorp, String sBillType, POPubSetUI2 setUi) {

    if (setUi != null) {
      m_cardPoPubSetUI2 = setUi;
    }
    else {
      m_cardPoPubSetUI2 = new POPubSetUI2();
    }
    setContainer(cntInvoker);
    setBillType(sBillType);
    setCorp(sCorp);
    initi();
  }

  /**
   * ���ߣ����� ���ܣ�������ӦActionListener�¼��ĺ��� ������ActionEvent e �¼� ���أ��� ���⣺��
   * ���ڣ�(2002-4-22 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public void actionPerformed(ActionEvent e) {

    // HEAD
    if (e.getSource() == ((UIRefPane) getHeadItem("cemployeeid").getComponent())
        .getUIButton()) {
      setRefPane_Head("cemployeeid");
    }
    else if (e.getSource() == ((UIRefPane) getHeadItem("cdeliveraddress")
        .getComponent()).getUIButton()) {
      setRefPane_Head("cdeliveraddress");
    }
    else if (e.getSource() == ((UIRefPane) getHeadItem("cbiztype")
        .getComponent()).getUIButton()) {
      setRefPane_Head("cbiztype");
    }
    else if (e.getSource() == ((UIRefPane) getHeadItem("caccountbankid")
        .getComponent()).getUIButton()) {
      setRefPane_Head("caccountbankid");
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��޸ı��屸ע�����Ӧ�仯 ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void afterEditWhenBodyAddr(BillEditEvent e) {

    String str = ((UIRefPane) getBodyItem("vreceiveaddress").getComponent())
        .getUITextField().getText();
    if (PuPubVO.getString_TrimZeroLenAsNull(str) != null) {
      setBodyValueAt(str, e.getRow(), "vreceiveaddress");
    }
    else {
      // �����ַ��գ��������ص������ص�Ҳ���
      setBodyValueAt("", e.getRow(), "vreceiveaddress");
      setBodyValueAt(null, e.getRow(), "cdevareaid");
      setBodyValueAt(null, e.getRow(), "cdevaddrid");
      setBodyValueAt(null, e.getRow(), "cdevareaname");
      setBodyValueAt(null, e.getRow(), "cdevaddrname");
      return;
    }
    // �շ�����ַ�仯��Ե������ص�Ĭ��ֵ��Ӱ��
    Object oVal = ((UIRefPane) getBodyItem("vreceiveaddress").getComponent())
        .getRefValue("bd_custaddr.pk_custaddr");
    String sAddrId = null;
    if (oVal != null && oVal.toString().trim().length() > 0) {
      sAddrId = oVal.toString().trim();
    }
    // ����Ĭ��ֵ��ע���ֹ��޸������ã�
    if (PuPubVO.getString_TrimZeroLenAsNull(sAddrId) != null) {
      Object[][] objs = CacheTool.getMultiColValue("bd_custaddr",
          "pk_custaddr", new String[] {
              "pk_areacl", "pk_address"
          }, new String[] {
            sAddrId
          });
      if (objs != null && objs.length == 1) {
        setBodyValueAt(objs[0][0], e.getRow(), "cdevareaid");
        setBodyValueAt(objs[0][1], e.getRow(), "cdevaddrid");
        String[] aryAddr = new String[] {
            "cdevareaname->getColValue(bd_areacl,areaclname,pk_areacl,cdevareaid)",
            "cdevaddrname->getColValue(bd_address,addrname,pk_address,cdevaddrid)"
        };
        getBillModel().execFormulas(e.getRow(), aryAddr);
      }
    }
  }

  /**
   * ���ߣ���־ƽ ���ܣ��޸ı��幩Ӧ���շ�����ַ�����Ӧ�仯 ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ���
   * ���⣺�� ���ڣ�(2004-11-17 13:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void afterEditWhenBodyAddrVendor(BillEditEvent e) {

    String str = ((UIRefPane) getBodyItem("vvenddevaddr").getComponent())
        .getUITextField().getText();
    if (PuPubVO.getString_TrimZeroLenAsNull(str) != null) {
      setBodyValueAt(str, e.getRow(), "vvenddevaddr");
    }
    else {
      // �����ַ��գ��������ص������ص�Ҳ���
      setBodyValueAt(null, e.getRow(), "vvenddevaddr");
      setBodyValueAt(null, e.getRow(), "cvenddevareaid");
      setBodyValueAt(null, e.getRow(), "cvenddevaddrid");
      setBodyValueAt(null, e.getRow(), "cvenddevareaname");
      setBodyValueAt(null, e.getRow(), "cvenddevaddrname");
      return;
    }
    // ��Ӧ���շ�����ַ�仯��Ե������ص�Ĭ��ֵ��Ӱ��
    Object oVal = ((UIRefPane) getBodyItem("vvenddevaddr").getComponent())
        .getRefValue("bd_custaddr.pk_custaddr");
    String sAddrId = null;
    if (oVal != null && oVal.toString().trim().length() > 0) {
      sAddrId = oVal.toString().trim();
    }
    // ����Ĭ��ֵ��ע���ֹ��޸������ã�
    if (PuPubVO.getString_TrimZeroLenAsNull(sAddrId) != null) {
      Object[][] objs = CacheTool.getMultiColValue("bd_custaddr",
          "pk_custaddr", new String[] {
              "pk_areacl", "pk_address"
          }, new String[] {
            sAddrId
          });
      if (objs != null && objs.length == 1) {
        setBodyValueAt(objs[0][0], e.getRow(), "cvenddevareaid");
        setBodyValueAt(objs[0][1], e.getRow(), "cvenddevaddrid");
        String[] aryAddr = new String[] {
            "cvenddevareaname->getColValue(bd_areacl,areaclname,pk_areacl,cvenddevareaid)",
            "cvenddevaddrname->getColValue(bd_address,addrname,pk_address,cvenddevaddrid)"
        };
        getBillModel().execFormulas(e.getRow(), aryAddr);
      }
    }
  }

  /**
   * ���ߣ����� ���ܣ��޸ĸ�������λ���� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-05-16 wyf
   * ����calRelation()ȥ���Ƿ�̶������ʵĲ���
   */
  private void afterEditWhenBodyAssistunit(BillEditEvent e) {

    // ��������λ
    PoEditTool.afterEditWhenBodyAssistunit(getBillModel(), e, new String[] {
        "cbaseid", "cmessureunit", "cassistunit", "cassistunitname",
        "nconvertrate"
    });

    // �����ʸı䣬���㣨���������ۣ���˰���ۣ���˰�ʣ�˰���˰�ϼ�,���ʣ����ʵ��ۣ�֮��Ĺ�ϵ
    BillEditEvent tempE = new BillEditEvent(e.getSource(), e.getValue(),
        "nconvertrate", e.getRow());
    calRelation(tempE);
    
    // ���ÿɱ༭��
    setEnabled_Body(e.getRow(),true);



  }

  /**
   * ���ߣ���ӡ�� ���ܣ��޸ĺ�ͬ�����Ӧ�仯 ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-05-27 ��ӡ�� ��ͬ�Ų��ոı�󣬼��봦��
   * 2002-07-02 ��ӡ�� ��Ժ�ͬ�Ų��յ��޸ģ���Ӧ�޸�ȡgetValue�Ĳ��� 2009-07-27 ��ӡ��
   * ������룬�Է�ֹ˫����ͬ�Ų��յ�TEXT����ʱ����ͬ����յ�����
   */
  private void afterEditWhenBodyCntCode(BillEditEvent e) {

    int iRow = e.getRow();
    if (e.getValue() == null || e.getValue().toString().trim().length() < 1) {
      // �Ƿ�ɱ༭
      setCellEditable(iRow, "ccontractcode",
          getBodyItem("ccontractcode") != null
              && getBodyItem("ccontractcode").isEdit());
      setCellEditable(iRow, "ccurrencytype",
          getBodyItem("ccurrencytype") != null
              && getBodyItem("ccurrencytype").isEdit());
      setCellEditable(iRow, "noriginalcurprice",
          getBodyItem("noriginalcurprice") != null
              && getBodyItem("noriginalcurprice").isEdit());
      setCellEditable(iRow, "norgtaxprice", getBodyItem("norgtaxprice") != null
          && getBodyItem("norgtaxprice").isEdit());
      setCellEditable(iRow, "noriginalnetprice",
          getBodyItem("noriginalnetprice") != null
              && getBodyItem("noriginalnetprice").isEdit());
      setCellEditable(iRow, "noriginalcurmny",
          getBodyItem("noriginalcurmny") != null
              && getBodyItem("noriginalcurmny").isEdit());
      // ��ͬID ��ͬ��ID ��ͬ�� ����
      setBodyValueAt(null, iRow, "ccontractid");
      setBodyValueAt(null, iRow, "ccontractrowid");
      setBodyValueAt(null, iRow, "ccontractcode");
    }
    else {
      try {
        AbstractRefModel refmodelCt = ((UIRefPane) getBodyItem("ccontractcode")
            .getComponent()).getRefModel();
        Object[] oRet = new Object[7];

        oRet[0] = (String) refmodelCt.getValue("ct_b.pk_ct_manage");
        oRet[1] = (String) refmodelCt.getValue("ct_b.pk_ct_manage_b");
        oRet[2] = (String) refmodelCt.getValue("ct.ct_code");
        oRet[3] = (String) refmodelCt.getValue("ct.currid");
        oRet[4] = (String) refmodelCt.getValue("ct_b.oriprice");
        oRet[5] = refmodelCt.getValue("ct_b.taxration");
        oRet[6] = (String) refmodelCt.getValue("ct_b.oritaxprice");
        // ����ֵ
        setBodyValueAt(oRet[0], iRow, "ccontractid");
        setBodyValueAt(oRet[1], iRow, "ccontractrowid");
        setBodyValueAt(oRet[2], iRow, "ccontractcode");
        // ���ñ��֡�˰�ʡ�����
        setBodyValueAt(oRet[3], iRow, "ccurrencytypeid");
        getBillModel().execLoadFormulaByKey("ccurrencytype");
        setBodyValueAt(oRet[5], iRow, "ntaxrate");
        // ��ͬ���ֲ��ɱ༭
        setCellEditable(iRow, "ccurrencytype", false);
        // ���ñ������:���ʡ����ȵ�
        setExchangeRateBody(iRow, true);
     // �۸���������
        int iPricePolicy = PuTool.getPricePriorPolicy(getCorp());
        UFDouble dPrice = null,dOldPrice = null;
        String sChangedKey = "";
        RetCtToPoQueryVO  voCtInfo = null;
     // �۸�
        if (oRet[4] != null || oRet[6] != null) {
          dPrice = iPricePolicy==RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE ? 
              new UFDouble((String)oRet[6]): new UFDouble((String)oRet[4]);
          sChangedKey = OrderItemVO.getPriceFieldByPricePolicy(iPricePolicy);
          if (dPrice != null) {
            dOldPrice = PuPubVO.getUFDouble_ValueAsValue(getBodyValueAt(iRow,
                sChangedKey));
            setBodyValueAt(dPrice, iRow, sChangedKey);
          }
          voCtInfo = new RetCtToPoQueryVO();
          voCtInfo.setDOrgPrice(new UFDouble((String)oRet[4]));
          voCtInfo.setDOrgTaxPrice(new UFDouble((String)oRet[6]));
        }

        if (voCtInfo != null && dPrice != null) {
          // ���ü۸�Ŀɱ༭��
          PoPublicUIClass.setPriceEditableByPricePolicy(getBillModel(), iRow,
              voCtInfo, iPricePolicy);

          // ���¼���������ϵ��ֻ��ֵ��ͬ��ԭֵʱ�����¼���
          if (dOldPrice == null || dPrice.compareTo(dOldPrice) != 0) {
            // ѯ���۸�ʱ�������¼���
            if (sChangedKey == null) {
              sChangedKey = OrderItemVO.getPriceFieldByPricePolicy(iPricePolicy);
            }
            calRelation(new BillEditEvent(
                getBodyItem(sChangedKey).getComponent(), getBodyValueAt(iRow,
                    sChangedKey), sChangedKey, iRow));
          }
        }
        else {
          setCellEditable(iRow, "noriginalcurprice",
              getBodyItem("noriginalcurprice") != null
                  && getBodyItem("noriginalcurprice").isEdit());
          setCellEditable(iRow, "norgtaxprice",
              getBodyItem("norgtaxprice") != null
                  && getBodyItem("norgtaxprice").isEdit());
        }
      }
      catch (Exception ee) {
        SCMEnv.out(ee);
      }
      // ���ý���������ɱ༭��
      setRelated_Cnt(iRow, iRow, true, false);
    }
    // �öδ����ֹ˫����ͬ�Ų��յ�TEXT����ʱ����ͬ����յ�����
    UIRefPane pane = ((UIRefPane) getBodyItem("ccontractcode").getComponent());
    pane.setPK(getBodyValueAt(iRow, "ccontractrowid"));
  }

  /**
   * ���ߣ����� ���ܣ��޸ı�����ֺ��� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-05-15 wyf
   * ��ȡ���뵽��������setBodyCurrRelated()�� wyf add/modify/delete 2002-05-15 begin/end
   * 2002-07-12 wyf ԭ�ȱ��ֱ仯��ȡ��ΪĬ�ϼۣ���ȡΪ��ͬ�� 2002-10-18 wyf �޸�ǰ�������������¼����KEYΪ������
   * �޸ĺ�Ϊԭ�ҵ��ۣ��øı�Ϊ�������еĸı䶼��Ӱ��ƻ��������ڵ��޸�
   */
  private void afterEditWhenBodyCurrency(BillEditEvent e,boolean isSetDefaultPrice) {

    int iRow = e.getRow();
    // ���û��ʡ����ȵľ���
    // setBodyCurrRelated(iRow,iRow,true) ;
    setExchangeRateBody(iRow, true);

    if (e.getValue() == null || e.getValue().toString().trim().equals("")) {
      setBodyValueAt(null, iRow, "nexchangeotobrate");      
      return;
    }
    if(isSetDefaultPrice){
      setDefaultPrice(iRow, iRow);
    }

    String sCurrTypeId = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(
        iRow, "ccurrencytypeid"));
    if (sCurrTypeId == null) {
      setBodyValueAt(null, iRow, "nexchangeotobrate");      
      return;
    }
    // ������ͬ
    int iCount = getRowCount();
    if(isAfterEditWhenHeadVendor){
    	setRelated_Cnt(0, iCount - 1, false, false);
    }else if(isSetDefaultPrice){
      setRelated_Cnt(e.getRow(), e.getRow(), false, false);
    }
    // ����ֵӦ���´���������ܱ���λ����ͬ�������¼���
    // ���޷��õ��ɱ���ID����˲��ܱ��־����Ƿ�ı䣬�����¼��㣬��Ȼ�п��ܵ������ݸı䡣
    // �޸ĺ����ҵ�񾫶����޸�ǰû�仯���򲻼���
    String sChangedKey = "nordernum";
    afterEditWhenBodyRelationsCal(new BillEditEvent(getBodyItem(sChangedKey),
        getBodyValueAt(iRow, sChangedKey), sChangedKey, iRow, BillItem.BODY));
    /**
    Object oOldCur = e.getOldValue();
    String sOldCur = null;
    if(oOldCur != null){
      sOldCur = e.getOldValue() instanceof String ? (String) e.getOldValue()
          : (String) ((DefaultConstEnum) e.getOldValue()).getValue();
    }
    try {
      if(sOldCur == null)
      //����
        sOldCur = CurrParamQuery.getInstance().getLocalCurrPK(getCorp());
    } catch (Exception  e2) {
      sOldCur = "";
    }
    String sNewCur = e.getValue() instanceof String ? (String) e.getValue()
        : (String) ((DefaultConstEnum) e.getValue()).getValue();
    if(!sOldCur.equals(sNewCur)){
      int iCurDec = POPubSetUI.getMoneyDigitByCurr_Busi((String) getBodyValueAt(e.getRow(), "ccurrencytypeid"));
      int iLocalCurDec = m_cardPoPubSetUI2.getCCurrDecimal();
      if(iCurDec != iLocalCurDec){
        String sChangedKey = "nordernum";
        afterEditWhenBodyRelationsCal(new BillEditEvent(getBodyItem(sChangedKey),
            getBodyValueAt(iRow, sChangedKey), sChangedKey, iRow, BillItem.BODY));
      }
    }*/
    updateUI();
  }

  /**
   * ���ߣ����� ���ܣ��޸ı�����ʺ����Ӧ�仯 ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-04-22 wyf ���ӶԺ�ͬ��֧�� wyf
   * add/modify/delete 2002-04-22 begin/end
   */
  private void afterEditWhenBodyExchangeRate(BillEditEvent e) {

    // //����
    // if ((e.getValue() != null) &&
    // (!(e.getValue().toString().trim().equals("")))){
    // UFDouble nexchangeotobrate = new
    // UFDouble(e.getValue().toString().trim());
    // if (nexchangeotobrate.doubleValue() < 0){
    // MessageDialog.showWarningDlg(this,"��ʾ","���ʲ���С��0");
    // getBillModel().setValueAt(null,e.getRow(),"nexchangeotobrate");
    // return ;
    // }
    // }

    // WYF add 2002-11-11 begin
    // ����޼�
    setRelated_MaxPrice(e.getRow(), e.getRow());
    // WYF add 2002-11-11 end

  }

  /**
   * ���ߣ����� ���ܣ��޸Ĵ�������Ӧ�仯 ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void afterEditWhenBodyInventory(BillEditEvent e) {

    // �رպϼƿ���
    boolean bOldNeedCalc = getBillModel().isNeedCalculate();
    getBillModel().setNeedCalculate(false);

    UIRefPane refpane = (UIRefPane) getBodyItem("cinventorycode")
        .getComponent();
    String[] saMangId = refpane.getRefPKs();

    nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
    timer.start();

    // Ϊ��ѡ��׼��
    // ��ǰ������
    int iOperationRow = e.getRow();
    // �������������
    int[] iaRow = PuTool.getRowsAfterMultiSelect(iOperationRow,
        saMangId == null ? 1 : saMangId.length);

    // ����(������)����
    int iNewLen = iaRow.length - 1;
    if (iOperationRow == getRowCount() - 1) {
      // ����
      onActionAppendLine(iNewLen,iOperationRow);
      // ����״̬Ϊ�޸�״̬
      getBillModel().setRowState(iOperationRow, BillModel.MODIFICATION);
    }
    else {
      // ����
      onActionInsertLines(iOperationRow, iNewLen,iOperationRow);
      // ����״̬Ϊ�޸�״̬
      getBillModel().setRowState(
          iOperationRow + (saMangId == null ? 1 : saMangId.length) - 1,
          BillModel.MODIFICATION);
    }
    timer.addExecutePhase("���л�����");/* -=notranslate=- */

    // ������
    int iEndRow = iaRow[iNewLen];

    // ִ�б��幫ʽ(��ȡ�ڴ������Ϣ)
    InvInfoParse
        .setInvEditFormulaManInfo(this, refpane, iOperationRow, iEndRow);
    timer.addExecutePhase("������ȡ���롢���ơ�ID��");/* -=notranslate=- */

    // ������
    setRelated_AssistUnit(iOperationRow, iEndRow);
    timer.addExecutePhase("������");/* -=notranslate=- */

    // ������
    for (int i = iOperationRow; i <= iEndRow; i++) {
      setBodyValueAt(null, i, "vfree");
      setBodyValueAt(null, i, "vfree1");
      setBodyValueAt(null, i, "vfree2");
      setBodyValueAt(null, i, "vfree3");
      setBodyValueAt(null, i, "vfree4");
      setBodyValueAt(null, i, "vfree5");
      setBodyValueAt(null, i, "vproducenum");

    }
    // setEnabled_BodyFree(iOperationRow,iEndRow) ;
    timer.addExecutePhase("��������κ�");/* -=notranslate=- */

    // ˰�� ���˰��Ϊ�գ����ô������������Ĭ��˰��
    boolean bUseGroupTaxRate = true;
    try {
      // �Ϻ�������Ŀ�ù�˾˰��(ͨ���Զ��幫ʽʵ��),since V502 ,modified by czp, 2007-08-07
      bUseGroupTaxRate = !CustomerConfigVO.NAME_SHANGHAILONGQI
          .equalsIgnoreCase(CustomerConfigVO.getCustomerName());
    }
    catch (Exception ee) {
      SCMEnv.out(ee);
    }
    //modify by zhw 2011-03-23  //�����ͷ��˰�� ��Ĭ��Ϊ��ͷ˰��  �����ͷû˰��  ��������˰��
    UFDouble rate = PuPubVO.getUFDouble_NullAsZero(getHeadItem("ntaxrate").getValueObject());
    if(UFDouble.ZERO_DBL.compareTo(rate)==0){
    	 if (bUseGroupTaxRate) {
    	      setRelated_Taxrate(iOperationRow, iEndRow);
    	      timer.addExecutePhase("˰��");/* -=notranslate=- */
    	    }
    }else{
    	 setBodyValueAt(rate, e.getRow(), "ntaxrate");
         // ˰�ʸı䣬���㣨���������ۣ���˰���ۣ���˰�ʣ�˰���˰�ϼ�,���ʣ����ʵ��ۣ�֮��Ĺ�ϵ
         BillEditEvent tempE = new BillEditEvent(getBodyItem("ntaxrate"),
        		 rate, "ntaxrate", e.getRow());
         calRelation(tempE);
    }

    // �ƻ���������
    calDplanarrvdate(iOperationRow, iEndRow);
    timer.addExecutePhase("�ƻ���������");/* -=notranslate=- */

    // �ϲ���Դ����

    // ��ͬID
    ArrayList listHaveCntRowId = new ArrayList();
    ArrayList listHaveCntIndex = new ArrayList();
    ArrayList listNotCntIndex = new ArrayList();
    String sUpSourceType = null;
    boolean isFromCTInv = false;//��һ���Ƿ���Դ��ͬ���������߿�
    for (int i = iOperationRow; i <= iEndRow; i++) {
      sUpSourceType = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(i,
          "cupsourcebilltype"));
      // ������غ�ͬ��Ϣ
      if (sUpSourceType != null
          && (sUpSourceType.equals(nc.vo.scm.pu.BillTypeConst.CT_BEFOREDATE) || sUpSourceType
              .equals(nc.vo.scm.pu.BillTypeConst.CT_ORDINARY))) {
        String sCntRowId = (String) getBodyValueAt(i, "ccontractrowid");
        listHaveCntRowId.add(sCntRowId);
        listHaveCntIndex.add(new Integer(i));
        if(!isFromCTInv){
        	RetCtToPoQueryVO voCntInfo = PoPublicUIClass.getCntInfo(sCntRowId);
        	if (voCntInfo != null && (voCntInfo.getIinvCtl() == RetCtToPoQueryVO.INVNULLCTL 
        			|| voCntInfo.getIinvCtl() == RetCtToPoQueryVO.INVCLASSCTL)) {
        		isFromCTInv = true;	
        	}
        }
        if(isFromCTInv){
  	        getBillModel().setValueAt(getBodyValueAt(iOperationRow, "cancestorbillcode"), i, "cancestorbillcode");
  	        getBillModel().setValueAt(getBodyValueAt(iOperationRow, "cancestorbillname"), i, "cancestorbillname");
  	        getBillModel().setValueAt(getBodyValueAt(iOperationRow, "cancestorbillrowno"), i, "cancestorbillrowno");
            getBillModel().setValueAt(getBodyValueAt(iOperationRow, "cupsourcebillid"), i, "cupsourcebillid");
            getBillModel().setValueAt(getBodyValueAt(iOperationRow, "cupsourcebillrowid"), i, "cupsourcebillrowid");
            getBillModel().setValueAt(getBodyValueAt(iOperationRow, "cupsourcebilltype"), i, "cupsourcebilltype");
    	  }
      }
      else {
	        // �ϲ���Դ���Ǻ�ͬ
	        getBillModel().setValueAt(null, i, "ccontractid");
	        getBillModel().setValueAt(null, i, "ccontractrowid");
	        getBillModel().setValueAt(null, i, "ccontractcode");
	        listNotCntIndex.add(new Integer(i));
    	  
      }
    }

    // ���к�ͬ����
    int iHaveCntLen = listHaveCntIndex.size();
    ArrayList listNoPriceIndex = new ArrayList();
    if (iHaveCntLen > 0) {
      RetCtToPoQueryVO[] voaCntInfo = PoPublicUIClass
          .getCntInfoArray((String[]) listHaveCntRowId
              .toArray(new String[iHaveCntLen]));

      for (int i = 0; i < iHaveCntLen; i++) {
        UFDouble dPrice = OrderPubVO.getPriceValueByPricePolicy(voaCntInfo[i],
            PuTool.getPricePriorPolicy(getCorp()));
        if (dPrice == null) {
          listNoPriceIndex.add(listHaveCntIndex.get(i));
        }
      }
      // ������Ѱ��Ĭ�ϼ۵���
      int iNotPriceLen = listNoPriceIndex.size();
      if (iNotPriceLen > 0) {
        setDefaultPrice((Integer[]) listNoPriceIndex
            .toArray(new Integer[iNotPriceLen]));
      }
    }
    // ������Ѱ�Һ�ͬ����(��Ʒ��������ͬ��setRelated_Cnt(Integer[],boolean)�����д���)
    int iNotCntLen = listNotCntIndex.size();
    if (iNotCntLen > 0) {
      setRelated_CntNew4V56((Integer[]) listNotCntIndex
          .toArray(new Integer[iNotCntLen]), false, true);
    }
    timer.addExecutePhase("ȡ��");/* -=notranslate=- */

    // ����޼�
    setRelated_MaxPrice(iOperationRow, iEndRow);
    timer.addExecutePhase("����޼�");/* -=notranslate=- */

    // ������ʾ
    if (getBillType().equalsIgnoreCase(BillTypeConst.PO_ORDER)
        || getBillType().equalsIgnoreCase(BillTypeConst.PO_REPLENISH)) {
      OrderVO voCurr = (OrderVO) getBillValueVO(OrderVO.class.getName(),
          OrderHeaderVO.class.getName(), OrderItemVO.class.getName());
      InvAttrCellRenderer ficr = new InvAttrCellRenderer();
      ficr.setFreeItemRenderer(this, voCurr);
    }
    timer.addExecutePhase("������ʾ");/* -=notranslate=- */

    /*
     * ����ɱ༭�����趨 ԭ���縨������ԭ���ǿ��У�������BeforeEdit�¼����趨Ϊ���ɱ༭
     * ��������Ӵ��д�����и���������ģ���ô�����������趨�������д���ศ�����Բ��ɱ༭
     */
    if (getIContainer() != null
        && getIContainer().getOperState() == IPoCardPanel.OPER_STATE_EDIT) {
      // ���õ��ݵĿɱ༭��
      int iRow = e.getRow();
      // ���ÿɱ༭��
      setEnabled_Body(iRow,true);
    }
    timer.addExecutePhase("����ɱ༭�����趨");/* -=notranslate=- */

    // ֧���û�����ı༭��ʽִ��
    if (getIContainer() != null
        && getIContainer().getOperState() == IPoCardPanel.OPER_STATE_EDIT) {
      BillItem it = getBodyItem(e.getKey());
      if (it.getEditFormulas() != null && it.getEditFormulas().length > 0) {
        getBillModel().execFormulas(it.getEditFormulas(), iOperationRow,
            iEndRow);
      }
    }
    timer.addExecutePhase("����༭��˾ִ��");/* -=notranslate=- */

    // ��ʾ��Ӧ������롢����
    String strVendorId = getHeadItem("cvendormangid").getValue();
    PuTool.loadVendorInvInfos(strVendorId, saMangId, getBillModel(),
        iOperationRow, iEndRow);
    timer.addExecutePhase("��ʾ��Ӧ������롢����");/* -=notranslate=- */

    // v50:���ӿ��д���
    if ("cinventorycode".equalsIgnoreCase(e.getKey())
        && getBillModel().getValueAt(getBillModel().getRowCount() - 1,
            "cinventorycode") != null) {
      onActionAppendLine(1,-1);
    }
    timer.addExecutePhase("����һ����");/* -=notranslate=- */

    // �򿪺ϼƿ���
    getBillModel().setNeedCalculate(bOldNeedCalc);
    // �����ͬ���ֶεĿɱ༭��
    for (int i = iOperationRow; i <= iEndRow; i++) {
      setEnabled_BodyCntRelated(i);
    }
    timer.addExecutePhase("�򿪺ϼƿ���");/* -=notranslate=- */

    timer.showAllExecutePhase("�����ѡ����");/* -=notranslate=- */

  }

  /**
   * ���ߣ���ӡ�� ���ܣ��޸ı��屸ע�����Ӧ�仯 ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void afterEditWhenBodyMemo(BillEditEvent e) {

    String sValue = ((UIRefPane) getBodyItem("vmemo").getComponent())
        .getUITextField().getText();
    if (PuPubVO.getString_TrimZeroLenAsNull(sValue) != null) {
      setBodyValueAt(sValue, e.getRow(), "vmemo");
    }
    else {
      setBodyValueAt(((UIRefPane) getBodyItem("vmemo").getComponent())
          .getText(), e.getRow(), "vmemo");
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��޸ı�����Ŀ�����Ӧ�仯 ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void afterEditWhenBodyProject(BillEditEvent e) {
    int iRow = e.getRow();
    // ��Ŀ
    getBillModel().setValueAt(null, iRow, "cprojectphase");
    getBillModel().setValueAt(null, iRow, "cprojectphaseid");
    getBillModel().setValueAt(null, iRow, "cprojectphasebaseid");
    // �趨�ɱ༭��
    setEnabled_BodyProjectPhase(iRow);
  }

  /**
   * ���ߣ����� ���ܣ��޸����������ۡ������� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-10-18 wyf
   * �޸�ǰ�������������¼����KEYΪ������ �޸ĺ�Ϊԭ�ҵ��ۣ��øı�Ϊ��Ӧ��ֻ�������ı��Ӱ��ƻ��������ڵ��޸� 2003-01-20 wyf
   * �޸�ǰ�������仯���¼ƻ��������ڱ仯�� �޸ĺ���������ɿջ��Ϊ��ֵ�����¼ƻ��������ڱ仯�����򲻱�
   */

  private void afterEditWhenBodyRelationsCal(BillEditEvent e) {

    int iRow = e.getRow();
    if(e.getKey().equals("nordernum") && e.getValue() != null && (new UFDouble(e.getValue().toString().trim())).doubleValue() > 0
        && PuPubVO.getUFBoolean_NullAs(getHeadItem("breturn").getValueObject(), UFBoolean.FALSE).booleanValue()){
      MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                          * @res "��ʾ"
                                                          */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200",
              "UPP40040200-000070")/*
                                    * @res "�˻�����������������Ϊ��"
                                    */);
      getBillModel().setValueAt(e.getOldValue(), iRow, "nordernum");
      return;
    }else if (e.getKey().equals("nordernum") && e.getValue() != null && (new UFDouble(e.getValue().toString().trim())).doubleValue() < 0
        && !PuPubVO.getUFBoolean_NullAs(getHeadItem("breturn").getValueObject(), UFBoolean.FALSE).booleanValue()){
      MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                          * @res "��ʾ"
                                                          */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200",
              "UPP40040200-000072")/*
                                    * @res "���˻�����������������Ϊ��"
                                    */);
      getBillModel().setValueAt(e.getOldValue(), iRow, "nordernum");
      return;
    }
    if ((!(e.getKey().equals("idiscounttaxtype"))) && (e.getValue() != null)
        && (!(e.getValue().toString().trim().equals("")))) {
      UFDouble ndata = new UFDouble(e.getValue().toString().trim());
      if (ndata.doubleValue() < 0) {
        if (e.getKey().equals("ntaxrate")) {
          MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                              * @res "��ʾ"
                                                              */,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200",
                  "UPP40040200-000025")/*
                                        * @res "˰�ʲ���С��0"
                                        */);
          getBillModel().setValueAt(e.getOldValue(), iRow, "ntaxrate");
          return;
        }
        else if (e.getKey().equals("noriginalnetprice")) {
          MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                              * @res "��ʾ"
                                                              */,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200",
                  "UPP40040200-000026")/*
                                        * @res "�����۲���С��0"
                                        */);
          getBillModel().setValueAt(e.getOldValue(), iRow, "noriginalnetprice");
          return;
        }
        else if (e.getKey().equals("noriginalcurprice")) {
          MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                              * @res "��ʾ"
                                                              */,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200",
                  "UPP40040200-000027")/*
                                        * @res "���۲���С��0"
                                        */);
          getBillModel().setValueAt(e.getOldValue(), iRow, "noriginalcurprice");
          return;
        }
      }
      if (e.getKey().equals("ndiscountrate")) {
        // if ((ndata.doubleValue() < 0) || (ndata.doubleValue() > 100))
        // {
        if (ndata.compareTo(VariableConst.ZERO) < 0) {
          // MessageDialog.showWarningDlg(this,"��ʾ","���ʱ������0С��100��");
          MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                              * @res "��ʾ"
                                                              */,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200",
                  "UPP40040200-000028")/*
                                        * @res "���ʱ������0"
                                        */);
          getBillModel().setValueAt(e.getOldValue(), iRow, "ndiscountrate");
          return;
        }
      }
    }

    // wyf 2002-10-21 add begin
    // ԭ�еĶ�������
    Object oNum = null;
    if (e.getKey().equals("nordernum")) {
      oNum = e.getOldValue();
    }
    else {
      oNum = getBodyValueAt(iRow, "nordernum");
    }
    UFDouble dOldNum = (oNum == null || oNum.toString().trim().equals("")) ? VariableConst.ZERO
        : new UFDouble(oNum.toString());
    // wyf 2002-10-21 add end

    // ����������ϵ
    calRelation(e);
    // UFDouble ndata = null;
    if (!e.getKey().equals("idiscounttaxtype")) {
      // ��Ʒ�У������ۿ���Ϊ0
      UFDouble ndata = new UFDouble(PuPubVO.getString_TrimZeroLenAsNull(e
          .getValue()));
      String strInvBaseID = getBillModel().getValueAt(e.getRow(), "cbaseid") == null ? null
          : getBillModel().getValueAt(e.getRow(), "cbaseid").toString();
      if (ndata.doubleValue() == 0
          && (e.getKey().equals("noriginalnetprice")
              || e.getKey().equals("norgtaxprice")
              || e.getKey().equals("noriginalcurprice")
              || e.getKey().equals("norgnettaxprice") || e.getKey().equals(
              "noriginalcurmny"))) {
        if (getBodyValueAt(iRow, "blargess") != null
            && getBodyValueAt(iRow, "blargess").toString().equalsIgnoreCase(
                "true") || PuTool.isDiscount(strInvBaseID)
            || PuTool.isLabor(strInvBaseID)) {
          setBodyValueAt(ndata, iRow, "noriginalnetprice");
          setBodyValueAt(ndata, iRow, "norgtaxprice");
          setBodyValueAt(ndata, iRow, "noriginalcurprice");
          setBodyValueAt(ndata, iRow, "norgnettaxprice");
          setBodyValueAt(ndata, iRow, "noriginalcurmny");
          setBodyValueAt(ndata, iRow, "noriginaltaxpricemny");
          setBodyValueAt(ndata, iRow, "nprice");
        }
        else {
          setBodyValueAt(null, iRow, "noriginalnetprice");
          setBodyValueAt(null, iRow, "norgtaxprice");
          setBodyValueAt(null, iRow, "noriginalcurprice");
          setBodyValueAt(null, iRow, "norgnettaxprice");
          setBodyValueAt(null, iRow, "noriginalcurmny");
          setBodyValueAt(null, iRow, "noriginaltaxpricemny");
          setBodyValueAt(null, iRow, "nprice");
        }
      }
    }

    // wyf 2003-01-20 modify begin
    // �����仯����ƻ��������ڱ仯
    oNum = getBodyValueAt(iRow, "nordernum");
    UFDouble dCurNum = (oNum == null || oNum.toString().trim().equals("")) ? VariableConst.ZERO
        : new UFDouble(oNum.toString());
    // if ( dOldNum.compareTo(dCurNum)!=0 ) {
    if (dOldNum.compareTo(VariableConst.ZERO) == 0
        && dCurNum.compareTo(VariableConst.ZERO) != 0) {
      // calDplanarrvdate(iRow,e.getValue()) ;
      calDplanarrvdate(iRow, iRow);
    }
    // wyf 2003-01-20 modify end

  }

  /**
   * ���ߣ����� ���ܣ��޸ı�ͷ���ֺ��� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2001-12-11 ljq
   * ���ݲ�ͬ�ı��֣����ñ�ͷ�������۸����ʡ��۱����ʵľ��� 2002-06-07 wyf �޸���״̬���Ա㱣��ʱ���� 2002-11-11 WYF
   * ���������޼۵Ĵ���
   */
  private void afterEditWhenHeadCurrency(BillEditEvent e) {

    // �رպϼƿ���
    boolean bOldNeedCalc = getBillModel().isNeedCalculate();
    getBillModel().setNeedCalculate(false);

    try {
      String sCurrId = getHeadItem("ccurrencytypeid").getValue();
      String dOrderDate = getHeadItem("dorderdate").getValue();
//      sCurrId2 = sCurrId;
//    =================��ͷ
      setExchangeRateHead(dOrderDate, sCurrId);
      
      //=================����
      if (sCurrId == null || sCurrId.trim().equals("") || getRowCount() == 0) {
        //
        getBillModel().setNeedCalculate(bOldNeedCalc);
        return;
      }
      ArrayList listAllCurrId = new ArrayList();
      listAllCurrId.add(sCurrId);
      BusinessCurrencyRateUtil bca = new BusinessCurrencyRateUtil(getCorp());
      if (CurrParamQuery.getInstance().getLocalCurrPK(getCorp()) != null
          && !listAllCurrId.contains(CurrParamQuery.getInstance()
              .getLocalCurrPK(getCorp()))) {
        listAllCurrId.add(CurrParamQuery.getInstance()
            .getLocalCurrPK(getCorp()));
      }
      // if(bca.getFracCurrPK() != null &&
      // !listAllCurrId.contains(bca.getFracCurrPK())){
      // listAllCurrId.add(bca.getFracCurrPK());
      // }
      // String[] saCurrId = (String[]) listAllCurrId.toArray(new
      // String[listAllCurrId.size()]);
      // HashMap mapRateMny =
      // m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(saCurrId);
      // HashMap mapRate =
      // m_cardPoPubSetUI2.getBothExchRateDigit(getCorp(),saCurrId);
      // HashMap mapRateEditable =
      // m_cardPoPubSetUI2.getBothExchRateEditableBatch(getCorp(),saCurrId);
      // HashMap mapRateVal =
      // m_cardPoPubSetUI2.getBothExchRateValue(getCorp(),saCurrId,dOrderDate);
      // �ı����û�б��ֵ���
      String sBodyCurrId = null;
      int iLen = getRowCount();
      for (int i = 0; i < iLen; i++) {
        sBodyCurrId = (String) getBillModel().getValueAt(i, "ccurrencytypeid");
        if (sBodyCurrId == null || sBodyCurrId.equals("")) {
          getBillModel().setValueAt(sCurrId, i, "ccurrencytypeid");
          // ��ȡ��������
          // execBodyFormula(i, "ccurrencytype");//�Ż� V31
          // ����������ر��ֵĸĶ�
          afterEditWhenBodyCurrency(new BillEditEvent(getBodyItem(
              "ccurrencytypeid").getComponent(), sCurrId, "ccurrencytypeid", i),false);
          // �����޸ı�־
          getBillModel().setRowState(i, BillModel.MODIFICATION);
        }
      }
      syncurrency();
      setRelated_Cnt(0, getRowCount() - 1, false, false);
      //����Ĭ�ϼ۸�
      setDefaultPrice(0, iLen -1);
      getBillModel().execEditFormulaByKey(-1, "ccurrencytype");// �Ż�
      // V31
    }
    catch (Exception exp) {
      PuTool.outException(this, exp);
      // �򿪺ϼƿ���
      getBillModel().setNeedCalculate(bOldNeedCalc);
      return;
    }
    // �򿪺ϼƿ���
    getBillModel().setNeedCalculate(bOldNeedCalc);
  }

  /**
   * ���ߣ����� ���ܣ��޸ı�ͷ���ֺ��� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2001-12-11 ljq
   * ���ݲ�ͬ�ı��֣����ñ�ͷ�������۸����ʡ��۱����ʵľ��� 2002-06-07 wyf �޸���״̬���Ա㱣��ʱ���� 2002-11-11 WYF
   * ���������޼۵Ĵ���
   */
  private void afterEditWhenHeadProject(BillEditEvent e) {

    try {
      String cprojectid = getHeadItem("cprojectid").getValue();
      if (cprojectid == null) {
        return;
      }
      String sBodyProjectid = null;
      int iLen = getRowCount();
      for (int i = 0; i < iLen; i++) {
        sBodyProjectid = (String) getBillModel().getValueAt(i, "cproject");
        if (sBodyProjectid == null || sBodyProjectid.equals("cproject")) {
          getBillModel().setValueAt(cprojectid, i, "cprojectbasid");

          getBillModel()
              .execFormulas(
                  new String[] {
                      "cprojectid->getColValue(bd_jobmngfil,pk_jobbasfil,pk_jobmngfil,cprojectbasid)",
                      "cproject->getColValue(bd_jobbasfil,jobname,pk_jobbasfil,cprojectid)"
                  });
          // �����޸ı�־
          getBillModel().setRowState(i, BillModel.MODIFICATION);
        }
      }
    }
    catch (Exception exp) {
      PuTool.outException(this, exp);
      return;
    }
  }

  /**
   * ���ߣ����� ���ܣ��޸ı�ͷ���ź��� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 
   * @�����¼�� <p>
   *        2006-06-21, Czp ,V5, �ſ��ɹ�Ա�ܲɹ�����Լ��������
   */
  private void afterEditWhenHeadDept(BillEditEvent e) {
    /*
     * UIRefPane paneDept = (UIRefPane) getHeadItem("cdeptid").getComponent();
     * String sDeptId = PuPubVO.getString_TrimZeroLenAsNull(paneDept
     * .getRefPK()); if (sDeptId == null) { return; } UIRefPane panePsn =
     * (UIRefPane) getHeadItem("cemployeeid") .getComponent(); String sPsnDeptId
     * = PuPubVO.getString_TrimZeroLenAsNull(panePsn
     * .getRefModel().getValue("bd_psndoc.pk_deptdoc")); if
     * (!sDeptId.equals(sPsnDeptId)) { panePsn.setPK(null); }
     */
  }

  /**
   * ���ߣ���־ƽ ���ܣ��޸ı�ͷ�Ƿ��˺��� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2004-12-13 9:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void afterEditWhenHeadDeliver(BillEditEvent e) {
    /*
     * String strDeliver = getHeadItem("bdeliver").getValue();
     * if("false".equalsIgnoreCase(strDeliver)){
     * getHeadItem("ctransmodeid").setValue(null);
     * getHeadItem("ctransmodeid").setEnabled(false); }else if
     * ("true".equalsIgnoreCase(strDeliver)){
     * getHeadItem("ctransmodeid").setEnabled(true); }
     */
  }

  /**
   * ���ߣ����� ���ܣ��޸ı�ͷҵ��Ա���� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void afterEditWhenHeadEmployee(BillEditEvent e) {

    String sPsnId = ((UIRefPane) getHeadItem("cemployeeid").getComponent())
        .getRefPK();
    if (PuPubVO.getString_TrimZeroLenAsNull(sPsnId) == null) {
      return;
    }

    // ��ҵ��Ա����Ĭ�ϲ���
    UIRefPane ref = (UIRefPane) (getHeadItem("cemployeeid").getComponent());
    ref.setPK(sPsnId);
    // ҵ��Ա��������
    Object sDeptId = ref.getRefModel().getValue("bd_psndoc.pk_deptdoc");
    getHeadItem("cdeptid").setValue(sDeptId);

    // ����ҵ��ԱĬ�ϲɹ���֯
    setPurOrg(sPsnId);
  }

  /***************************************************************************
   * ����ҵ��Ա�����ɹ���֯ ��������������� �� 1)��ԭ���ɹ���֯����ֵ 2)����ҵ��Ա���䵽����ɹ���֯
   **************************************************************************/
  private void setPurOrg(String sPsnId) {
    // Ŀǰ���淽����֧��һ���������ض��ֵ�����
    String strPurId = PuTool.getPurIdByPsnId(sPsnId);
    if (strPurId != null && getHeadItem("cpurorganization") != null
        && getHeadItem("cpurorganization").getValue() == null) {
      getHeadItem("cpurorganization").setValue(strPurId);
    }
  }

  /**
   * ���ߣ����� ���ܣ��޸ı�ͷ���ʺ��� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void afterEditWhenHeadExchangeRate(BillEditEvent e) {

  	if ((e.getValue() != null)
        && (!(e.getValue().toString().trim().equals("")))) {
      UFDouble nexchangeotobrate = new UFDouble(e.getValue().toString().trim());
      if (nexchangeotobrate.doubleValue() < 0) {
        MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200",
                "UPP40040200-000029")/* @res "���ʲ���С��0" */);
        getHeadItem(e.getKey()).setValue(null);
        return;
      }
      // �رպϼƿ���
      boolean bOldNeedCalc = getBillModel().isNeedCalculate();
      getBillModel().setNeedCalculate(false);
      for (int i = 0; i < getRowCount(); i++) {
        Object o = getBillModel().getValueAt(i, e.getKey());
        String bodyValue = PuPubVO.getString_TrimZeroLenAsNull(o);
        if (!e.getValue().equals(bodyValue)) {
          getBillModel().setValueAt(e.getValue(), i, e.getKey());
          getBillModel().setRowState(i, BillModel.MODIFICATION);
        }
      }
      // �򿪺ϼƿ���
      getBillModel().setNeedCalculate(bOldNeedCalc);
    }
  }

  /**
   * ���ߣ����� ���ܣ��޸ı�ͷɢ������ ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2003-02-25 wyf
   * �޸Ĺ�Ӧ��Ĭ����ϢVOΪȡ�Թ������е�VO
   */
  private void afterEditWhenHeadFreecust(BillEditEvent e) {

    String cfreecustid = ((UIRefPane) (getHeadItem("cfreecustid")
        .getComponent())).getRefPK();
    String cvendormangid = ((UIRefPane) (getHeadItem("cvendormangid")
        .getComponent())).getRefPK();
    // setRefPaneBank("cfreecustid", cvendorbaseid, cfreecustid);
    setRefPane_Head("caccountbankid");

    if ((e.getValue() != null)
        && (!(e.getValue().toString().trim().equals("")))) {
      String[] aryBanks = getFreeCustBank(cfreecustid);
      if (aryBanks != null && aryBanks.length != 0) {
        getHeadItem("caccountbankid").setValue(aryBanks[0]);
        getHeadItem("account").setValue(aryBanks[1]);
      }
    }
    else {
      PoVendorVO vendorVO = null;
      // if ((cvendormangid != null) && (getHashtableVendor() != null) &&
      // (getHashtableVendor().containsKey(cvendormangid.trim()))){
      if ((cvendormangid != null)) {
        vendorVO = PoPublicUIClass.getVendDefaultInfo(cvendormangid);
      }
      if (vendorVO != null) {
        getHeadItem("caccountbankid").setValue(vendorVO.getCcustbank());
        getHeadItem("account").setValue(vendorVO.getCaccount());
      }
    }
  }

  /**
   * ���ߣ����� ���ܣ��޸ı�ͷ��ע���� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void afterEditWhenHeadMemo(BillEditEvent e) {
    // setHeadItem("vmemo",e.getValue());
    setHeadItem("vmomo", ((UIRefPane) getHeadItem("vmemo").getComponent())
        .getRefName());

  }

  /**
   * ���ߣ����� ���ܣ��޸Ķ������ں����Ӧ�仯 ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-05-27 wyf �����ͬ�����Ϣ��յĴ���
   * 2002-06-07 wyf �޸���״̬���Ա㱣��ʱ���� 2002-11-11 WYF ���������޼۵Ĵ���
   */
  private void afterEditWhenHeadOrderDate(BillEditEvent e) {

    // �رպϼƿ���
    boolean bOldNeedCalc = getBillModel().isNeedCalculate();
    getBillModel().setNeedCalculate(false);

    // �������ڱ仯�������ļƻ��������ڱ仯
    if (e.getValue() != null && !e.getValue().toString().trim().equals("")) {
      // �ı��ͷ����
      String dorderdate = (String) e.getValue();
      setExchangeRateHead(dorderdate, getHeadItem("ccurrencytypeid").getValue());

      int iCount = getRowCount();
      // �ƻ���������
      calDplanarrvdate(0, iCount - 1);
      // String[] saCurrId = new String[iCount];
      // for (int i = 0; i < iCount; i++) {
      // //�����������¼���������
      // saCurrId[i] = (String)
      // getBillModel().getValueAt(i,"ccurrencytypeid");
      // }
      // saCurrId = m_cardPoPubSetUI2.getDistinctStrArray(saCurrId);
      // int iLen = (saCurrId == null) ? 0 : saCurrId.length;
      // if(iLen > 0){
      // HashMap mapRate =
      // m_cardPoPubSetUI2.getBothExchRateDigit(getCorp(),saCurrId);
      // HashMap mapRateEditable =
      // m_cardPoPubSetUI2.getBothExchRateEditableBatch(getCorp(),saCurrId);
      // String dOrderDate = getHeadItem("dorderdate").getValue();
      // HashMap mapRateVal =
      // m_cardPoPubSetUI2.getBothExchRateValue(getCorp(),saCurrId,dOrderDate);
      for (int i = 0; i < iCount; i++) {
        // �����������¼���������
        String sCurrId = (String) getBillModel().getValueAt(i,
            "ccurrencytypeid");
        if (PuPubVO.getString_TrimZeroLenAsNull(sCurrId) != null) {
          // �����޸ı�־
          getBillModel().setRowState(i, BillModel.MODIFICATION);
          setExchangeRateBody(i, true);
        }
      }
      // }
    }
    // ��ͬ
    int iCount = getRowCount();
    if (iCount > 0) {
      for (int i = 0; i < iCount; i++) {
        getBillModel().setValueAt(null, i, "ccontractid");
        getBillModel().setValueAt(null, i, "ccontractrowid");
        getBillModel().setValueAt(null, i, "ccontractcode");
      }
      setRelated_Cnt(0, iCount - 1, false, false);

      setRelated_MaxPrice(0, iCount - 1);
    }

    // �򿪺ϼƿ���
    getBillModel().setNeedCalculate(bOldNeedCalc);

  }

  /**
   * ���ߣ����� ���ܣ��޸ı�ͷ�ջ������� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void afterEditWhenHeadReciever(BillEditEvent e) {
    String sReveivePk = ((UIRefPane) (getHeadItem("creciever").getComponent()))
        .getRefPK();
    setDefaultValueWhenHeadRecieverChged(e, sReveivePk);
  }

  private void setDefaultValueWhenHeadRecieverChged(BillEditEvent e,
      String sReveivePk) {
    UIRefPane paneRef = ((UIRefPane) (getBodyItem("vreceiveaddress")
        .getComponent()));
    // ��ͷ�ջ���Ϊ��
    if (PuPubVO.getString_TrimZeroLenAsNull(sReveivePk) == null) {
      // �����ͷ�ջ�����ֵ��������շ�����ַ���߲���(�ֹ����롢�ֿ�����������֯����)
      paneRef.setButtonVisible(false);
      for (int i = 0; i < getRowCount(); i++) {
        afterEditWhenBodyArrWare(new BillEditEvent(getBodyItem("cwarehouse")
            .getComponent(), null, "cwarehouse", i, BillItem.HEAD));
        // �ɱ༭
        getBillModel().setRowState(i, BillModel.MODIFICATION);
      }
    }
    // ��ͷ�ջ����ǿ� : Ĭ��Ϊ��ͷ�ջ����ĵ�����ַ�����տͻ����շ�����ַ
    else {
      paneRef.setButtonVisible(true);
      setRefPane_Body("vreceiveaddress");
      // ȡĬ�ϵ�ַ
      Object[] oaRet = null;
      try {
        oaRet = (Object[]) nc.ui.scm.pub.CacheTool.getCellValue("bd_cumandoc",
            "pk_cumandoc", "pk_cubasdoc", sReveivePk);
      }
      catch (Exception ee) {
        /** �����׳� */
        SCMEnv.out(e);
      }
      String sRevBaseId = (String) oaRet[0];
      Object[][] oa2Ret = null;
      try {
        oa2Ret = PubHelper.queryResultsFromAnyTable("bd_custaddr",
            new String[] {
                "addrname", "pk_custaddr"
            }, "pk_cubasdoc='" + sRevBaseId
                + "' AND ISNULL(defaddrflag,'N')='Y'");
      }
      catch (Exception ex) {
      }
      String sAddr = (oa2Ret == null || oa2Ret[0] == null || oa2Ret[0].length == 0) ? null
          : PuPubVO.getString_TrimZeroLenAsNull(oa2Ret[0][0]);
      String sAddrId = (oa2Ret == null || oa2Ret[0] == null || oa2Ret[0].length == 0) ? null
          : PuPubVO.getString_TrimZeroLenAsNull(oa2Ret[0][1]);
      Object[][] objs = CacheTool.getMultiColValue("bd_custaddr",
          "pk_custaddr", new String[] {
              "pk_areacl", "pk_address"
          }, new String[] {
            sAddrId
          });
      String strAreaId = null, strAreaName = null, strAddrId = null, strAddrName = null;
      if (objs != null && objs.length == 1) {
        strAreaId = (objs[0] == null || objs[0].length == 0) ? null : PuPubVO
            .getString_TrimZeroLenAsNull(objs[0][0]);
        strAddrId = (objs[0] == null || objs[0].length < 1) ? null : PuPubVO
            .getString_TrimZeroLenAsNull(objs[0][1]);
        if (strAreaId != null) {
          oa2Ret = CacheTool.getMultiColValue("bd_areacl", "pk_areacl",
              new String[] {
                "areaclname"
              }, new String[] {
                strAreaId
              });
          strAreaName = (oa2Ret == null || oa2Ret[0] == null || oa2Ret[0].length == 0) ? null
              : PuPubVO.getString_TrimZeroLenAsNull(oa2Ret[0][0]);
        }
        if (strAddrId != null) {
          oa2Ret = CacheTool.getMultiColValue("bd_address", "pk_address",
              new String[] {
                "addrname"
              }, new String[] {
                strAddrId
              });
          strAddrName = (oa2Ret == null || oa2Ret[0] == null || oa2Ret[0].length == 0) ? null
              : PuPubVO.getString_TrimZeroLenAsNull(oa2Ret[0][0]);
        }
      }
      int iCnt = getRowCount();
      // 2006-02-23 Czp V5�޸ģ������Զ���ģ���еĿɱ༭������
      boolean bEditUserDef = getBillModel().getItemByKey("vreceiveaddress")
          .isEdit();
      if (e.getPos() == BillItem.HEAD) {
        for (int i = 0; i < iCnt; i++) {
          if (sAddr == null) {
            getBillModel().setValueAt("", i, "vreceiveaddress");
          }
          else {
            getBillModel().setValueAt(sAddr, i, "vreceiveaddress");
          }
          getBillModel().setValueAt(strAddrId, i, "cdevaddrid");
          getBillModel().setValueAt(strAreaId, i, "cdevareaid");
          getBillModel().setValueAt(strAddrName, i, "cdevaddrname");
          getBillModel().setValueAt(strAreaName, i, "cdevareaname");
          getBillModel().setCellEditable(i, "vreceiveaddress", bEditUserDef);
          getBillModel().setRowState(i, BillModel.MODIFICATION);
        }
      }
      else if (e.getPos() == BillItem.BODY) {
        if (sAddr == null) {
          getBillModel().setValueAt("", e.getRow(), "vreceiveaddress");
        }
        else {
          getBillModel().setValueAt(sAddr, e.getRow(), "vreceiveaddress");
        }
        getBillModel().setValueAt(strAddrId, e.getRow(), "cdevaddrid");
        getBillModel().setValueAt(strAreaId, e.getRow(), "cdevareaid");
        getBillModel().setValueAt(strAddrName, e.getRow(), "cdevaddrname");
        getBillModel().setValueAt(strAreaName, e.getRow(), "cdevareaname");
        getBillModel().setCellEditable(e.getRow(), "vreceiveaddress",
            bEditUserDef);
        getBillModel().setRowState(e.getRow(), BillModel.MODIFICATION);
      }
    }
  }

  /**
   * ���ߣ����� ���ܣ��޸ı�ͷ˰�ʺ��� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-06-07 wyf �޸���״̬���Ա㱣��ʱ����
   * 2002-10-21 wyf �޸������ȵĹ�ϵ���㣬���ù��÷���
   */
  private void afterEditWhenHeadTaxRate(BillEditEvent e) {

    // �رպϼƿ���
    boolean bOldNeedCalc = getBillModel().isNeedCalculate();
    getBillModel().setNeedCalculate(false);

    if ((e.getValue() != null)
        && (!(e.getValue().toString().trim().equals("")))) {
      UFDouble ntaxrate = new UFDouble(e.getValue().toString().trim());
      if (ntaxrate.doubleValue() < 0) {
        MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200",
                "UPP40040200-000025")/* @res "˰�ʲ���С��0" */);
        getHeadItem("ntaxrate").setValue(null);
        return;
      }
      //2010-05-18 tianft ֻ�б�ͷ˰�ʲ�Ϊ��ʱ��������
      for (int i = 0; i < getRowCount(); i++) {
        Object o = getBillModel().getValueAt(i, "ntaxrate");
        if ((o == null) || (o.toString().trim().equals(""))
        		|| !o.toString().equals(e.getValue().toString())
        		) {
          getBillModel().setValueAt(e.getValue(), i, "ntaxrate");

          // ���㣨���������ۣ���˰���ۣ���˰�ʣ�˰���˰�ϼ�,���ʣ����ʵ��ۣ�֮��Ĺ�ϵ
          BillEditEvent tempe = new BillEditEvent(e.getSource(), e.getValue(),
              "ntaxrate", i);
          calRelation(tempe);
          getBillModel().setRowState(i, BillModel.MODIFICATION);
        }
      }
    }
    // HashMap mapRateMny =
    //m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(getAllCurrIdFromCard(getRowCount
    // (),getCorp(),getHeadItem("ccurrencytypeid").getValue(),getBillModel()));
    // BusinessCurrencyRateUtil bca =
    // m_cardPoPubSetUI2.getCurrArith_Busi(getCorp());

    // �򿪺ϼƿ���
    getBillModel().setNeedCalculate(bOldNeedCalc);
  }

  /**
   * ���ߣ����� ���ܣ��޸ı�ͷ��Ӧ�̺����Ӧ�仯 ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2001-10-20 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-04-25 wyf �޸�ȡ�۷�ʽ wyf
   * add/modify/delete 2002-04-22 begin/end 2002-05-27 wyf �����ͬ�����Ϣ��յĴ���
   * 2002-06-07 wyf �޸���״̬���Ա㱣��ʱ����
   */
  private void afterEditWhenHeadVendor(BillEditEvent e) {

    // �رպϼƿ���
    boolean bOldNeedCalc = getBillModel().isNeedCalculate();
    isAfterEditWhenHeadVendor = true;
    getBillModel().setNeedCalculate(false);

    /*
     * V5 ֧�ִ����ݿ��ж�ȡ��ʽִ�� //��Ӧ�� String formula =
     * "cvendorbaseid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cvendormangid)"
     * ; execHeadFormula(formula); //��Ӧ��ȫ�� formula =
     * "ccustname->getColValue(bd_cubasdoc,custname,pk_cubasdoc,cvendorbaseid)";
     * execHeadFormula(formula);
     */
    // V5 ֧�����ݿ��ж���Ĺ�ʽ
    execHeadFormulas(getHeadItem("cvendormangid").getEditFormulas());

    // ����ɢ���Ƿ�ɱ༭
    setEnabled_HeadFreeCust(isRevise());
    setEnabled_HeadAccount(isRevise());
    isSetCnt = false;
    // ���ñ�ͷĬ������
    setDefaultHeadafterVendor(e);
    isSetCnt = true;
    // ��ͬ
    int iCount = getRowCount();
    String[] saMangId = new String[iCount];
    if (iCount > 0) {
      for (int i = 0; i < iCount; i++) {
        saMangId[i] = (String) getBodyValueAt(i, "cmangid");
        getBillModel().setValueAt(null, i, "ccontractid");
        getBillModel().setValueAt(null, i, "ccontractrowid");
        getBillModel().setValueAt(null, i, "ccontractcode");
        getBillModel().setRowState(i, BillModel.MODIFICATION);
      }
      setRelated_Cnt(0, iCount - 1, false, false);
    }
    // ��Ӧ���շ�����ַ���������ص�
    String sRevBaseId = getHeadItem("cvendorbaseid").getValue();

    if (getRowCount() > 0) {
      setDefaultValueToBodyVendorAddr(sRevBaseId, 0, getRowCount() - 1);
    }
    boolean bVrmEnabled = false;
    try {
      if(m_hmProdEnable.containsKey(getCorp()+ScmConst.m_sModuleCodeVRM)){
        bVrmEnabled = m_hmProdEnable.get(getCorp()+ScmConst.m_sModuleCodeVRM).booleanValue();
      }else{
        bVrmEnabled = PuTool.isProductEnabled(getCorp(), ScmConst.m_sModuleCodeVRM);
      }
    }
    catch (Exception ex) {
      nc.ui.pu.pub.PuTool.outException(m_conInvoker, ex);
      return;
    }
    if (bVrmEnabled) {
      // ��ʾ��Ӧ�̵Ĵ�����뼰����
      String strVendorId = getHeadItem("cvendormangid").getValue();
      String[][] saCodeNames = PuTool.getVendorInvInfos(strVendorId, saMangId);
      if (saCodeNames != null && saCodeNames.length == saMangId.length) {
        for (int i = 0; i < iCount; i++) {
          setBodyValueAt(saCodeNames[i][0], i, "vvendinventorycode");
          setBodyValueAt(saCodeNames[i][1], i, "vvendinventoryname");
        }
      }
      else {
        for (int i = 0; i < iCount; i++) {
          setBodyValueAt(null, i, "vvendinventorycode");
          setBodyValueAt(null, i, "vvendinventoryname");
        }
      }
    }
    
    if(getHeadItem("ccurrencytypeid") == null){

    	//��������б���Ϊ�գ�����Ϊ��λ��
    	String sLocalCurr  = null ;
    	try {
    		//����
    		sLocalCurr = CurrParamQuery.getInstance().getLocalCurrPK(getHeadItem("pk_corp").getValue());
    	} catch (Exception	e2) {
    	}

    	//���ñ������
    	getHeadItem("ccurrencytypeid").setValue(sLocalCurr);
    }
    syncurrency();
    // �򿪺ϼƿ���
    getBillModel().setNeedCalculate(bOldNeedCalc);
    isAfterEditWhenHeadVendor = false;
  }

  /**
   * ���ݿ��̻�������ID������Ĭ�ϵ�ַ��Ϣ���ñ��幩Ӧ�̵ĵ�ַ���������ص�
   */
  private void setDefaultValueToBodyVendorAddr(String sRevBaseId,
      int iBeginRow, int iEndRow) {

    Object[][] oa2Ret = null;
    if(PuPubVO.getString_TrimZeroLenAsNull(sRevBaseId) == null){
      SCMEnv.out("��Ĭ�ϵ�ַ���������壬ֱ�ӷ���!");/* -=notranslate=- */
      return;
    }
    try {
      oa2Ret = PubHelper.queryResultsFromAnyTable("bd_custaddr", new String[] {
          "addrname", "pk_custaddr"
      }, "pk_cubasdoc='" + sRevBaseId + "' AND ISNULL(defaddrflag,'N')='Y'");
    }
    catch (Exception ex) {
    }
    if (oa2Ret == null || oa2Ret.length == 0) {
      SCMEnv.out("��Ĭ�ϵ�ַ���������壬ֱ�ӷ���!");/* -=notranslate=- */
      return;
    }
    String sAddr = null;
    String sAddrId = null;

    if (oa2Ret != null && oa2Ret.length == 1 && oa2Ret[0] != null) {
      sAddr = PuPubVO.getString_TrimZeroLenAsNull(oa2Ret[0][0]);
      sAddrId = PuPubVO.getString_TrimZeroLenAsNull(oa2Ret[0][1]);
    }
    Object[][] objs = CacheTool.getMultiColValue("bd_custaddr", "pk_custaddr",
        new String[] {
            "pk_areacl", "pk_address"
        }, new String[] {
          sAddrId
        });
    String strAreaId = null, strAreaName = null, strAddrId = null, strAddrName = null;
    if (objs != null && objs.length == 1 && objs[0] != null) {
      strAreaId = PuPubVO.getString_TrimZeroLenAsNull(objs[0][0]);
      strAddrId = PuPubVO.getString_TrimZeroLenAsNull(objs[0][1]);
      if (strAreaId != null) {
        oa2Ret = CacheTool.getMultiColValue("bd_areacl", "pk_areacl",
            new String[] {
              "areaclname"
            }, new String[] {
              strAreaId
            });
        if (oa2Ret != null && oa2Ret.length == 1 && oa2Ret[0] != null) {
          strAreaName = PuPubVO.getString_TrimZeroLenAsNull(oa2Ret[0][0]);
        }
      }
      if (strAddrId != null) {
        oa2Ret = CacheTool.getMultiColValue("bd_address", "pk_address",
            new String[] {
              "addrname"
            }, new String[] {
              strAddrId
            });
        if (oa2Ret != null && oa2Ret.length == 1 && oa2Ret[0] != null) {
          strAddrName = PuPubVO.getString_TrimZeroLenAsNull(oa2Ret[0][0]);
        }
      }
    }
    for (int i = iBeginRow; i <= iEndRow; i++) {
      if (sAddr == null) {
        getBillModel().setValueAt("", i, "vvenddevaddr");
      }
      else {
        getBillModel().setValueAt(sAddr, i, "vvenddevaddr");
      }
      getBillModel().setValueAt(strAddrId, i, "cvenddevaddrid");
      getBillModel().setValueAt(strAreaId, i, "cvenddevareaid");
      getBillModel().setValueAt(strAddrName, i, "cvenddevaddrname");
      getBillModel().setValueAt(strAreaName, i, "cvenddevareaname");
      getBillModel().setRowState(i, BillModel.MODIFICATION);
    }
  }

  /**
   * ���ߣ���־ƽ ���ܣ���ͷ��β�༭ǰ���� ������BillItemEvent e ��׽����BillItemEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-7-22 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־
   */
  public boolean beforeEdit(BillItemEvent e) {
    // ��ͷ
    /*
     * if (e.getSource().equals(getHeadItem("ctransmodeid"))) {
     * //���˷�ʽ���ڡ��Ƿ��ˡ���ѡʱ��Ч if
     * ("false".equalsIgnoreCase(getHeadItem("bdeliver").getValue())){
     * getHeadItem("ctransmodeid").setValue(null);
     * getHeadItem("ctransmodeid").setEnabled(false); }else if
     * ("true".equalsIgnoreCase(getHeadItem("bdeliver").getValue())){
     * getHeadItem("ctransmodeid").setEnabled(true); } }else
     */
    // ����Ϊ�ִ����Կ����֯(20050310-ע�⣺Ŀǰ�˴������Ѿ�����showmodal())
    /*
     * V5 Del: if (e.getSource().equals(getHeadItem("cstoreorganization"))){
     * PuTool
     * .restrictStoreOrg(getHeadItem("cstoreorganization").getComponent(),true);
     * }
     */
    return true;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��༭ǰ���� ������ActionEvent e ��׽����ActionEvent�¼� ���أ��� ���⣺�� ���ڣ�(2002-7-22
   * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2003-10-08 wyf ������Զ����еĴ���
   */
  public boolean beforeEdit(BillEditEvent e) {
    Boolean bret = ((PoToftPanel) m_conInvoker).getInvokeEventProxy()
        .beforeEdit(e);
    if (bret == null || !bret.booleanValue()) {
      return Boolean.FALSE;
    }
    bret = beforeEditPlugin(e);
  //�������������ʵĿɱ༭�ԣ���ҪΪ��Ƭ�༭����  by zhaoyha at 2009.12.7
    boolean editable=true;
    if("nconvertrate".equals(e.getKey()))
        editable=getBillModel().isCellEditable(e.getRow(),
            getBodyColByKey("nconvertrate"));
    return bret && editable;

  }

  private boolean beforeEditPlugin(BillEditEvent e) {
    if (e.getPos() == BillItem.BODY) {
      // У����˰�����������������ʾ��һ��
      if (!e.getKey().equals("idiscounttaxtype")) {
        initDisBeforeEdit(e.getRow());
      }

      // V31SP1
      if (PuPubVO.getString_TrimZeroLenAsNull(getBillModel().getValueAt(
          e.getRow(), "ntaxrate")) == null) {
        getBillModel().setValueAt(new UFDouble(0.0), e.getRow(), "ntaxrate");
      }
      // V31��׼��ģ�����-�ۼ�ִ��������������ؽ�������ؽ��
      if (OrderPubVO.isNaccNumKey(e.getKey())
          || OrderPubVO.isLocalMnyKey(e.getKey())) {
        return false;
      }
      //
      if (getIContainer() != null
          && getIContainer().getOperState() == IPoCardPanel.OPER_STATE_EDIT) {
        // ���õ��ݵĿɱ༭��
        int iRow = e.getRow();
        // ���ÿɱ༭��
        if (isRevise()) {
          setEnabled_Body(iRow,false);
          //���ú�ͬ���
          setEnabled_BodyCntRelated(iRow);
          // ���޶��Ĳ����趨:����������к������ɡ��ر��У���̶���Ŀ����Ϊ�������޸�
          setReviseEnable_Body(iRow);
        }
        else{
          setEnabled_Body(iRow,true);
        }
        // ���ɱ༭�������½���
        String sKey = e.getKey();
        if (!getBillModel().isCellEditable(iRow, getBodyColByKey(sKey))) {
          return true;
        }

        // ���
        if (sKey.equals("cinventorycode")) {
          beforeEditBodyInventory(e);
        }
        // ��ͬ
        else if (sKey.equals("ccontractcode")) {
          try {
            // ���ù�˾ID����Ӧ��ID�����ID���������ú�ͬ�Ų���
            UIRefPane pane = ((UIRefPane) getBodyItem("ccontractcode")
                .getComponent());
            nc.ui.ct.ref.ValiCtRefModel newCtRefModel = (nc.ui.ct.ref.ValiCtRefModel) pane
                .getRefModel();
            if (newCtRefModel == null) {
              nc.ui.ct.ref.ValiCtRefModel newCtRefModel1 = new nc.ui.ct.ref.ValiCtRefModel(
                  getHeadItem("pk_corp").getValue(), getHeadItem(
                      "cvendorbaseid").getValue(), (String) getBodyValueAt(
                      iRow, "cbaseid"),
                  (String) getBodyValueAt(iRow, "cmangid"), new UFDate(
                      getHeadItem("dorderdate").getValue()));
              pane.setRefModel(newCtRefModel1);
            }
            else {
              String invmangid = newCtRefModel.getM_sInvManId();
              if (invmangid == null
                  || !invmangid.equals(PuPubVO
                      .getString_TrimZeroLenAsNull(getBodyValueAt(iRow,
                          "cmangid")))) {
                nc.ui.ct.ref.ValiCtRefModel newCtRefModel1 = new nc.ui.ct.ref.ValiCtRefModel(
                    getHeadItem("pk_corp").getValue(), getHeadItem(
                        "cvendorbaseid").getValue(), (String) getBodyValueAt(
                        iRow, "cbaseid"), (String) getBodyValueAt(iRow,
                        "cmangid"), new UFDate(getHeadItem("dorderdate")
                        .getValue()));
                pane.setRefModel(newCtRefModel1);
              }
              else {
                newCtRefModel.setM_sPk_corp(getHeadItem("pk_corp").getValue());
                newCtRefModel.setM_sCustBaseID(getHeadItem("cvendorbaseid")
                    .getValue());
                newCtRefModel.setM_sInvBaseID((String) getBodyValueAt(iRow,
                    "cbaseid"));
                newCtRefModel.setM_sInvManId((String) getBodyValueAt(iRow,
                    "cmangid"));
                newCtRefModel.setM_date(new UFDate(getHeadItem("dorderdate")
                    .getValue()));
              }
            }
            pane.getRefModel().reloadData();
            String sUpSourceType = (String) getBodyValueAt(iRow,
                "cupsourcebilltype");
            // �ϲ���Դ���Ǻ�ͬ
            if (sUpSourceType != null
                && (sUpSourceType.equals(BillTypeConst.CT_ORDINARY) || sUpSourceType
                    .equals(BillTypeConst.CT_BEFOREDATE))) {
              setCellEditable(iRow, "ccontractcode", false);
            }
          }
          catch (Exception ee) {
            SCMEnv.out(ee);
          }
          // ����������
        }
        else if (sKey.equals("vfree")) {
          return PuTool.beforeEditInvBillBodyFree(this, e, new String[] {
              "cmangid", "cinventorycode", "cinventoryname", "cspecification",
              "ctype"
          }, new String[] {
              "vfree", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5"
          });
        }
        // ���屸ע
        else if (sKey.equals("vmemo")) {
          PuTool.beforeEditBillBodyMemo(this, e);
        }
        // �����շ�����ַ
        else if (sKey.equals("vreceiveaddress")) {
          //
          stopEditing();

          Object ob = getBodyValueAt(e.getRow(), "vreceiveaddress");
          if (ob == null) {
            ((UIRefPane) getBodyItem("vreceiveaddress").getComponent())
                .setText("");
          }
          else {
            ((UIRefPane) getBodyItem("vreceiveaddress").getComponent())
                .setText((String) ob);
          }
          // ���ͷ���ջ���Ϊ�գ���Ĭ��Ϊ�ջ��ֿ�ĵ�ַ���ɱ༭
          setRefPane_Body("vreceiveaddress");
        }
        // ���幩Ӧ���շ�����ַ
        else if (sKey.equals("vvenddevaddr")) {
          //
          stopEditing();

          Object ob = getBodyValueAt(e.getRow(), "vvenddevaddr");
          if (ob == null) {
            ((UIRefPane) getBodyItem("vvenddevaddr").getComponent())
                .setText("");
          }
          else {
            ((UIRefPane) getBodyItem("vvenddevaddr").getComponent())
                .setText((String) ob);
          }
          // ���ͷ���ջ���Ϊ�գ���Ĭ��Ϊ�ջ��ֿ�ĵ�ַ���ɱ༭
          setRefPane_Body("vvenddevaddr");
        }
        // ���κ�
        else if (sKey.equals("vproducenum")) {
          beforeEditBodyProduceNum(e);
        }
        else if (sKey.equals("cdevaddrname")) {
          setRefPane_Body("cdevaddrname");
        }
        // ����λ
        else if (sKey.equals("cassistunitname")) {
          PoEditTool
              .beforeEditBodyAssistUnit(this, e, "cbaseid", "cassistunit");
          // ���������
          PoEditTool.setCellEditable_AssistUnitRelated(getBillModel(), iRow,
              new String[] {
                  "cbaseid", "cassistunit", "cassistunitname", "nassistnum",
                  "nconvertrate"
              });
        }
        // ��Ŀ�׶�
        else if (sKey.equals("cprojectphase")) {
          // ֹͣ�༭!!!
          stopEditing();
          String sPK = (String) getBodyValueAt(iRow, "cprojectphaseid");
          // ��ĿID
          String sProjectId = (String) getBodyValueAt(iRow, "cprojectid");
          // ��Ŀ�׶β���
          UIRefPane refPane = (UIRefPane) getBodyItem("cprojectphase")
              .getComponent();

          refPane.setRefInputType(0);

          refPane.updateUI();
          PuProjectPhaseRefModel refmodelJobPhase = (PuProjectPhaseRefModel) (refPane)
              .getRefModel();
          // refmodelJobPhase.setWherePart(refmodelJobPhase.getWherePart());
          refmodelJobPhase.getTableName();
          refmodelJobPhase.setProjectid(sProjectId);

          setBodyValueAt(sPK, iRow, "cprojectphaseid");
          refPane.setPK(sPK);
        }
        // �۱�
        else if (sKey.equals("nexchangeotobrate")) {
          // �������!!!!!!!!
          stopEditing();
          //
          String sCurrId = (String) getBodyValueAt(iRow, "ccurrencytypeid");
          getBodyItem("nexchangeotobrate").setDecimalDigits(
              m_cardPoPubSetUI2.getBothExchRateDigit(getCorp(), sCurrId)[0]);
          if(isRevise()){
            //�޶����棬��λ�Ҳ����޶��۱�����
            boolean[] bEdit = m_cardPoPubSetUI2.getBothExchRateEditable(getCorp(), sCurrId);
            boolean bRevise = getBillModel().getItemByKey("nexchangeotobrate").isM_bReviseFlag();
            if (bEdit != null) {
              getBillModel().setCellEditable(iRow, "nexchangeotobrate",
                  bEdit[0] & bRevise);
            }
          }
        }// �۸�
        else if (sKey.equals("noriginalcurmny")) {
          stopEditing();
          String sCurrId = (String) getBodyValueAt(iRow, "ccurrencytypeid");
          getBodyItem("noriginalcurmny").setDecimalDigits(
              m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(sCurrId));
        }// ˰��
        else if (sKey.equals("noriginaltaxmny")) {
          stopEditing();
          String sCurrId = (String) getBodyValueAt(iRow, "ccurrencytypeid");
          getBodyItem("noriginaltaxmny").setDecimalDigits(
              m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(sCurrId));
        }// ��˰�ϼ�
        else if (sKey.equals("noriginaltaxpricemny")) {
          stopEditing();
          String sCurrId = (String) getBodyValueAt(iRow, "ccurrencytypeid");
          getBodyItem("noriginaltaxpricemny").setDecimalDigits(
              m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(sCurrId));
        }// �Ƿ���Ʒ
        else if (sKey.equals("blargess")) {
          return beforeEditBodyBlargess(e);
        }

        // ----V5������

        // �����ż۷���
        else if (sKey.equals("cqpbaseschemename")) {
          return beforeEditBodyCQP(e);
        }
        // �ջ���˾
        else if (sKey.equals("arrvcorpname")) {
          return beforeEditBodyArrCorp(e);
        }
        // �ջ������֯
        else if (sKey.equals("arrvstoorgname")) {
          return beforeEditBodyArrStoOrg(e);
        }
        // �ջ��ֿ�
        else if (sKey.equals("cwarehouse")) {
          return beforeEditBodyArrWare(e);
        }
        // ��Ʊ��˾
        else if (sKey.equals("invoicecorpname")) {
          return beforeEditBodyInvoiceCorp(e);
        }
        // ����˾
        else if (sKey.equals("reqcorpname")) {
          // return false;
          return beforeEditBodyReqCorp(e);
        }
        // ��������֯
        else if (sKey.equals("reqstoorgname")) {
          // return false;
          return beforeEditBodyReqStoOrg(e);
        }
        // ����ֿ�
        else if (sKey.equals("reqwarename")) {
          // return false;
          return beforeEditBodyReqWare(e);
        }
        // ������
        else if (sKey.startsWith("vfree")) {
          PoEditTool.setCellEditable_Vfree(getBillModel(), iRow, "cmangid",
              "vfree");
        }
        // 
        else if (sKey.equals("norgtaxprice")
            || sKey.equals("noriginalcurprice") || sKey.equals("ccurrencytype")) {
//          if (getBodyValueAt(iRow, "ccontractcode") != null 
//              && getBodyValueAt(iRow, sKey) != null ) {
//            return false;
//          }
          return true;
        }
        else if (sKey.startsWith("vdef")) {
          if (getBodyItem(e.getKey()).getComponent() instanceof UIRefPane) {
            ((UIRefPane) getBodyItem(e.getKey()).getComponent())
                .getUITextField().setEditable(true);
            ((UIRefPane) getBodyItem(e.getKey()).getComponent()).requestFocus();
            ((UIRefPane) getBodyItem(e.getKey()).getComponent())
                .getUITextField().requestFocus();
            ((UIRefPane) getBodyItem(e.getKey()).getComponent())
                .getUITextField().requestDefaultFocus();
            ((UIRefPane) getBodyItem(e.getKey()).getComponent())
                .getUITextField().requestFocus(true);
          }
        }
      }
    }
    return true;
  }

  /**
   * <p>
   * ��ȡ��ǰ��ȷ�Ĺ�˾���ϣ�������ʽ��('1001','1002')
   * 
   * @param iCurrCorp
   *          0��¼��˾��1����˾��2�ջ���˾��3��Ʊ��˾
   * @param isLimittedBySelfCorp
   *          �Ƿ��ܱ���˾����,��ʱδ����
   * @return ��˾�������ϣ�����ֵ�㷨����(�������󣺲�֧�ֵ������ջ�)���£�
   *         <p>
   *         i.�����ͬ��˾ֻ��һ��������(������)ʱ������null��
   *         <p>
   *         ii.�����ͬ��˾�ж���ʱ������ʽ��('1001','1002')���ַ���
   * @author czp
   * @date 2006-03-08
   */
  public static String getCorpSet(BillCardPanel card, int iCurrCorp,
      int iRowPos, boolean isLimittedBySelfCorp) {
    ArrayList listCorp = new ArrayList();
    String strLoginCorp = PoPublicUIClass.getLoginPk_corp();
    listCorp.add(strLoginCorp);
    String strReqCorp = PuPubVO.getString_TrimZeroLenAsNull(card
        .getBodyValueAt(iRowPos, "pk_reqcorp"));
    String strArrCorp = PuPubVO.getString_TrimZeroLenAsNull(card
        .getBodyValueAt(iRowPos, "pk_arrvcorp"));
    String strInvoiceCorp = PuPubVO.getString_TrimZeroLenAsNull(card
        .getBodyValueAt(iRowPos, "pk_invoicecorp"));
    switch (iCurrCorp) {
      case 1:// ����˾
        if (isLimittedBySelfCorp && strReqCorp != null
            && !listCorp.contains(strReqCorp)) {
          listCorp.add(strReqCorp);
        }
        if (strArrCorp != null && !listCorp.contains(strArrCorp)) {
          listCorp.add(strArrCorp);
        }
        if (strInvoiceCorp != null && !listCorp.contains(strInvoiceCorp)) {
          listCorp.add(strInvoiceCorp);
        }
        break;
      case 2:// �ջ���˾
        if (strReqCorp != null && !listCorp.contains(strReqCorp)) {
          listCorp.add(strReqCorp);
        }
        if (isLimittedBySelfCorp && strArrCorp != null
            && !listCorp.contains(strArrCorp)) {
          listCorp.add(strArrCorp);
        }
        if (strInvoiceCorp != null && !listCorp.contains(strInvoiceCorp)) {
          listCorp.add(strInvoiceCorp);
        }
        break;
      case 3:// ��Ʊ��˾
        if (strReqCorp != null && !listCorp.contains(strReqCorp)) {
          listCorp.add(strReqCorp);
        }
        if (strArrCorp != null && !listCorp.contains(strArrCorp)) {
          listCorp.add(strArrCorp);
        }
        if (isLimittedBySelfCorp && strInvoiceCorp != null
            && !listCorp.contains(strInvoiceCorp)) {
          listCorp.add(strInvoiceCorp);
        }
        // Ϊ��֤���������ջ���˾����¼��˾��ʱ����Ʊ��˾ֻ��Ϊ��¼��˾������V5��֧�֡����вɹ��������ջ�����ɢ���㡱ģʽ
        if (strLoginCorp.equals(strArrCorp)) {
          listCorp = new ArrayList();
          listCorp.add(strLoginCorp);
          listCorp.add(strLoginCorp);
        }
        break;

      default:
        break;
    }
    if (listCorp.size() == 2) {
      return " ('" + listCorp.get(0) + "','" + listCorp.get(1) + "') ";
    }
    return null;
  }

  /**
   * �༭ǰ�¼�������ֿ� i. ���ҵ������Ϊ��˾ҵ�����ͣ����ɱ༭ ii. ����ϲ���Դ����ID�ǿգ��򲻿ɱ༭ iii. �������˾��ֵ�����ɱ༭
   * iv. �������˾��ֵ����������֯��ֵ�����������Ϊ����˾�����п����֯�µĲֿ⵵�� v.
   * �������˾����������֯��ֵ�����������Ϊ��������֯�µĲֿ⵵��
   */
  private boolean beforeEditBodyReqWare(BillEditEvent e) {
    String strReqCorp = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e
        .getRow(), "pk_reqcorp"));
    if (strReqCorp == null) {
      SCMEnv.out("�ɹ�����������˾δ¼��,���塰��������֯����Ŀ���ɱ༭");
      return false;
    }
    String strUpSrcBillId = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(
        e.getRow(), "cupsourcebillid"));
    if (strUpSrcBillId != null) {
      SCMEnv.out("�ɹ�������������Դ�ĵ�����,���塰����ֿ⡱��Ŀ���ɱ༭");
      return false;
    }
    // ����ǹ�˾ҵ�����ͣ��򲻿ɱ༭(ԭ������˾רΪ���вɹ����)
    String strBusiType = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
        "cbiztype").getValue());
    if (strBusiType == null) {
      SCMEnv.out("δ��ȡҵ������������������ֿ⡱��Ŀ���ɱ༭");
      return false;
    }
    String strReqStoOrg = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e
        .getRow(), "pk_reqstoorg"));
    PuTool.restrictWarehouseRefByStoreOrg(this, strReqCorp, strReqStoOrg,
        "reqwarename");
    return true;
  }

  /**
   * �༭ǰ�¼�����������֯ i. �������˾�գ����ɱ༭ ii. ����ϲ���Դ����ID�ǿգ����ɱ༭ iii.
   * ���ҵ������Ϊ��˾ҵ������(ԭ������˾רΪ���вɹ����)�����ɱ༭ iv. �������˾�Ѿ���ֵ�������ò�������Ϊ����˾�µ�������������֯
   * v. �������˾û��ֵ��<1>�ջ���˾����Ʊ��˾����ֵ ��
   * ��ֵ�����¼��˾һ�£����ò���Ϊȫ���ſ����֯������<2>��������������·�Χ{��¼��˾���ջ���˾����Ʊ��˾}
   */
  private boolean beforeEditBodyReqStoOrg(BillEditEvent e) {
    String strReqCorp = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e
        .getRow(), "pk_reqcorp"));
    if (strReqCorp == null) {
      SCMEnv.out("�ɹ�����������˾δ¼��,���塰��������֯����Ŀ���ɱ༭");
      return false;
    }
    String strUpSrcBillId = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(
        e.getRow(), "cupsourcebillid"));
    if (strUpSrcBillId != null) {
      SCMEnv.out("�ɹ�������������Դ�ĵ�����,���塰��������֯����Ŀ���ɱ༭");
      return false;
    }
    // ����ǹ�˾ҵ�����ͣ��򲻿ɱ༭(ԭ������˾רΪ���вɹ����)
    String strBusiType = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
        "cbiztype").getValue());
    if (strBusiType == null) {
      SCMEnv.out("δ��ȡҵ����������������������֯����Ŀ���ɱ༭");
      return false;
    }
    UIRefPane paneReqStoOrg = ((UIRefPane) getBodyItem("reqstoorgname")
        .getComponent());
    AbstractRefModel rmArrStoOrg = paneReqStoOrg.getRefModel();
    rmArrStoOrg.setSealedDataShow(false);// ����ʾ������ݣ��μ�AbstractRefModel::
                                         // addSealCondition(whereClause);
    rmArrStoOrg.setWherePart(" bd_calbody.pk_corp = '" + strReqCorp + "' ");
    rmArrStoOrg.setPk_corp(strReqCorp);
    return true;
  }

  /**
   * <p>
   * �༭ǰ�¼�������˾
   * <p>
   * i.��������Դʱ(����ϲ���Դ����ID�ǿ�)�����ɱ༭
   * <p>
   * ii.����ǹ�˾ҵ������(ԭ������˾רΪ���вɹ����)�����ɱ༭
   * <p>
   * iii.���ҵ�������Ǽ���ҵ�����ͣ�<1>�ջ���˾����Ʊ��˾����ֵ ��
   * ��ֵ�����¼��˾һ�£����ò���Ϊ���Ź�˾������<2>��������������·�Χ{��¼��˾���ջ���˾����Ʊ��˾}
   */
  private boolean beforeEditBodyReqCorp(BillEditEvent e) {

    String strUpSrcBillId = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(
        e.getRow(), "cupsourcebillid"));
    if (strUpSrcBillId != null) {
      SCMEnv.out("�ɹ�������������Դ�ĵ�����,���塰����˾����Ŀ���ɱ༭");
      return false;
    }
    String strRpFlag = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e
        .getRow(), "breceiveplan"));
    if (PuPubVO.getUFBoolean_NullAs(strRpFlag, UFBoolean.FALSE).booleanValue()) {
      SCMEnv.out("�ɹ��������Ѿ��е����ƻ���,���塰����˾����Ŀ���ɱ༭");
      return false;
    }
    // ����ǹ�˾ҵ�����ͣ��򲻿ɱ༭(ԭ������˾רΪ���вɹ����)
    String strBusiType = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
        "cbiztype").getValue());
    if (strBusiType == null) {
      SCMEnv.out("δ��ȡҵ������������������˾����Ŀ���ɱ༭");
      return false;
    }
    boolean isCentrPur = false;
    try {
      isCentrPur = CentrPurchaseUtil.isGroupBusiType(strBusiType);
    }
    catch (Exception ex) {
      SCMEnv.out("�����Ƿ���ҵ�����͹��߷���ʱ����" + ex.getMessage());
    }
    UIRefPane paneArrCorp = ((UIRefPane) getBodyItem("reqcorpname")
        .getComponent());
//    paneArrCorp.setRefNodeName("Ȩ�޹�˾Ŀ¼");
    AbstractRefModel rmArrCorp = paneArrCorp.getRefModel();
    if (!isCentrPur) {
      rmArrCorp.addWherePart("and bd_corp.pk_corp = '"
          + PoPublicUIClass.getLoginPk_corp() + "' ");
      return true;
    }
    String strCorpSet = getCorpSet(this, 1, e.getRow(), false);
    if (strCorpSet != null) {
      rmArrCorp.addWherePart("and bd_corp.pk_corp in " + strCorpSet + " ");
    }
    return true;
  }

  /**
   * �༭ǰ�¼�����Ʊ��˾
   * <p>
   * i.���ҵ������Ϊ��˾ҵ�����ͣ����ɱ༭
   * <p>
   * ii.���ҵ������Ϊ����ҵ�����ͣ�<1>����˾���ջ���˾����ֵ ��
   * ��ֵ�����¼��˾һ�£����ò���Ϊ���Ź�˾������<2>��������������·�Χ{��¼��˾������˾���ջ���˾}
   * 
   * @author czp
   * @date 2006-03-08
   */
  private boolean beforeEditBodyInvoiceCorp(BillEditEvent e) {

    // ����ǹ�˾ҵ�����ͣ��򲻿ɱ༭
    String strBusiType = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
        "cbiztype").getValue());
    if (strBusiType == null) {
      SCMEnv.out("δ��ȡҵ����������������Ʊ��˾����Ŀ���ɱ༭");
      return false;
    }
    boolean isCentrPur = false;
    try {
      isCentrPur = CentrPurchaseUtil.isGroupBusiType(strBusiType);
    }
    catch (Exception ex) {
      SCMEnv.out("�����Ƿ���ҵ�����͹��߷���ʱ����" + ex.getMessage());
    }
    if (!isCentrPur) {
      SCMEnv.out("���Ǽ���ҵ������,���塰��Ʊ��˾����Ŀ���ɱ༭");
      return false;
    }
    String sHId = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(
        e.getRow(), "corderid"));
    String sBId = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(
        e.getRow(), "corder_bid"));
    if (sHId != null && sBId != null) {
      if (!NO_AFTERBILL.equals(h_mapAfterBill.get(sHId))) {
        OrderAfterBillVO voAfter = (OrderAfterBillVO) h_mapAfterBill.get(sHId);
        boolean bInvoice = false;
        if (voAfter != null) {
          bInvoice = voAfter
              .isHaveAfterBill(sBId, OrderstatusVO.STATUS_INVOICE);
        }
        // �����߼�������������ۼƷ�Ʊ������Ϊ�㣬Ҳ��Ϊ�����˷�Ʊ
        UFDouble ufdNaccInvoiceNum = PuPubVO
            .getUFDouble_NullAsZero(getBodyValueAt(e.getRow(),
                "naccuminvoicenum"));
        bInvoice = bInvoice || ufdNaccInvoiceNum.doubleValue() != 0.0;
        // �������к�����Ʊ���ɣ��򲻿����޸�
        if (bInvoice) {
          SCMEnv.out("�������к�����Ʊ���ɣ��������޸���Ʊ��˾");/* -=notranslate=- */
          return false;
        }
        // ������������Ѿ��ݹ������ɽ��㵥���������޸���Ʊ��˾
        boolean bPs = false;
        if (voAfter != null) {
          bPs = voAfter.isHaveAfterBill(sBId, OrderstatusVO.STATUS_SETTLE);
        }
        if (bPs) {
          SCMEnv.out("������������Ѿ��ݹ������ɽ��㵥���������޸���Ʊ��˾");/* -=notranslate=- */
          return false;
        }
      }
    }
    /*
     * ����ҵ�����ͣ��������ò���:
     */
    UIRefPane paneInvoiceCorp = ((UIRefPane) getBodyItem("invoicecorpname")
        .getComponent());
    paneInvoiceCorp.setRefNodeName("��˾Ŀ¼");
    AbstractRefModel rmInvoiceCorp = paneInvoiceCorp.getRefModel();
    String strCorpSet = getCorpSet(this, 3, e.getRow(), false);
    if (strCorpSet != null) {
      rmInvoiceCorp.addWherePart("and bd_corp.pk_corp in " + strCorpSet + " ");
    }
    return true;
  }

  /**
   * <p>
   * �༭ǰ�¼����ջ��ֿ�
   * <p>
   * i.����ջ���˾��ֵ�����ɱ༭
   * <p>
   * ii.����ջ���˾��ֵ���ջ������֯��ֵ��������ջ���˾�����вֿ⵵��
   * <p>
   * iii.����ջ���˾���ջ������֯����ֵ��������ջ������֯�µ����вֿ⵵��
   */
  private boolean beforeEditBodyArrWare(BillEditEvent e) {
    boolean bRight = false;
    String strArrCorp = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e
        .getRow(), "pk_arrvcorp"));
    if (strArrCorp == null) {
      return false;
    }
    UIRefPane paneBodyStor = ((UIRefPane) getBodyItem("cwarehouse")
        .getComponent());
    UIRefPane refpane = (UIRefPane) getHeadItem("cbiztype").getComponent();
    String sBiztype = refpane.getRefPK();
    SCMEnv.out("------" + sBiztype);
    Object[] oRule = null;
    try {
      oRule = (Object[]) CacheTool.getCellValue("bd_busitype", "pk_busitype",
          "verifyrule", sBiztype);
    }
    catch (Exception ee) {
      SCMEnv.out(ee);
    }
    WarehouseRefModel model = (WarehouseRefModel) paneBodyStor.getRefModel();
    model.setM_pk_corp(getCorp());
    model.setPk_calbody(strArrCorp);
    // ҵ������ֱ��
    if (oRule != null && oRule.length > 0
        && "Z".equalsIgnoreCase(oRule[0].toString())) {
      bRight = true;
      // paneBodyStor.setRefModel(new
      // WarehouseRefModel(getCorp(),strArrCorp,true));
      model.setBDirect(true);
    }
    else {
      bRight = false;
      // paneBodyStor.setRefModel(new
      // WarehouseRefModel(getCorp(),strArrCorp,false));
      model.setBDirect(false);
    }

    String strArrStoOrg = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e
        .getRow(), "pk_arrvstoorg"));
    PuTool.restrictNotZYWarehouseRefByStoreOrg(this, strArrCorp, strArrStoOrg,
        "cwarehouse", bRight);

    return true;
  }

  /**
   * <p>
   * �༭ǰ�¼����ջ������֯
   * <p>
   * i.�ջ���˾δ¼�룬���ɱ༭
   * <p>
   * ii.���ò�������Ϊ�ջ���˾�µĿ����֯����
   */
  private boolean beforeEditBodyArrStoOrg(BillEditEvent e) {
    String strArrCorp = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e
        .getRow(), "pk_arrvcorp"));
    if (strArrCorp == null) {
      return false;
    }
    UIRefPane paneArrStoOrg = ((UIRefPane) getBodyItem("arrvstoorgname")
        .getComponent());
    AbstractRefModel rmArrStoOrg = paneArrStoOrg.getRefModel();
    rmArrStoOrg.setSealedDataShow(false);// ����ʾ������ݣ��μ�AbstractRefModel::
                                         // addSealCondition(whereClause);
    rmArrStoOrg.setPk_corp(strArrCorp);
    rmArrStoOrg.setWherePart(" bd_calbody.pk_corp = '" + strArrCorp + "' ");
    return true;
  }

  /**
   * <p>
   * �༭ǰ�¼����ջ���˾ i.���ҵ�������ǹ�˾ҵ�����ͣ����ɱ༭ ii.���ҵ�������Ǽ���ҵ�����ͣ�<1>����˾����Ʊ��˾����ֵ ��
   * ��ֵ�����¼��˾һ�£����ò���Ϊ���Ź�˾������<2>��������������·�Χ{��¼��˾������˾����Ʊ��˾}
   */
  private boolean beforeEditBodyArrCorp(BillEditEvent e) {

    // ����ǹ�˾ҵ�����ͣ��򲻿ɱ༭
    String strBusiType = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
        "cbiztype").getValue());
    if (strBusiType == null) {
      SCMEnv.out("δ��ȡҵ���������������ջ���˾����Ŀ���ɱ༭");
      return false;
    }
    boolean isCentrPur = false;
    try {
      isCentrPur = CentrPurchaseUtil.isGroupBusiType(strBusiType);
    }
    catch (Exception ex) {
      SCMEnv.out("�����Ƿ���ҵ�����͹��߷���ʱ����" + ex.getMessage());
    }
    if (!isCentrPur) {
      SCMEnv.out("���Ǽ���ҵ������,���塰�ջ���˾����Ŀ���ɱ༭");
      return false;
    }
    /*
     * ����ҵ�����ͣ��������ò���:
     */
    UIRefPane paneArrCorp = ((UIRefPane) getBodyItem("arrvcorpname")
        .getComponent());
    // paneArrCorp.setRefNodeName("��˾Ŀ¼");
    AbstractRefModel rmArrCorp = paneArrCorp.getRefModel();
    String strCorpSet = getCorpSet(this, 2, e.getRow(), false);
    if (strCorpSet != null) {
      rmArrCorp.addWherePart("and bd_corp.pk_corp in " + strCorpSet + " ");
    }
    return true;
  }

  /**
   * �༭ǰ�¼��������ż۷���<br>
   * <p>
   * �ɱ༭��Ҫ��ͬʱ���㣬<br>
   * <p>
   * 1)�����¼��<br>
   * 2)���ջ���˾�ǿ�<br>
   * 3)���ջ���˾=�ɹ���˾<br>
   */
  private boolean beforeEditBodyCQP(BillEditEvent e) {

    // �����ż۷�������ģ�ͣ�nc.ui.scm.hqhp.QPSchmRefModel

    // ����������˾Ϊ�գ��򲻿ɱ༭
    String strArrvCorp = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e
        .getRow(), "pk_arrvcorp"));
    if (strArrvCorp == null) {
      SCMEnv.out("�ջ���˾δȷ�����������ż۷�������Ŀ���ɱ༭");
      return false;
    }
    String strCbaseid = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e
        .getRow(), "cbaseid"));
    if (strCbaseid == null) {
      SCMEnv.out("���δȷ�����������ż۷�������Ŀ���ɱ༭");
      return false;
    }
    if (!strArrvCorp.equals(PoPublicUIClass.getLoginPk_corp())) {
      SCMEnv.out("�ջ���˾��ɹ���˾����ͬ���������ż۷�������Ŀ���ɱ༭");
      return false;
    }
    // �����������ż۷����Ѿ����ɼ۸���㵥���������޸�
    String sHId = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem("corderid")
        .getValue());
    if (sHId != null) {
      String sBId = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e
          .getRow(), "corder_bid"));
      if (sBId != null) {
        OrderAfterBillVO voAfter = (OrderAfterBillVO) h_mapAfterBill.get(sHId);
        boolean bPs = false;
        if (voAfter != null) {
          bPs = voAfter.isHaveAfterBill(sBId, OrderstatusVO.STATUS_PS);
        }
        if (bPs) {
          SCMEnv.out("�����������ż۷����Ѿ����ɼ۸���㵥���������޸�");/* -=notranslate=- */
          return false;
        }
      }
    }
    // �������ò���
    UIRefPane paneBodyCQP = ((UIRefPane) getBodyItem("cqpbaseschemename")
        .getComponent());
    paneBodyCQP.setRefModel(new QPSchmRefModel(strArrvCorp, strCbaseid));
    paneBodyCQP.setButtonVisible(true);
    paneBodyCQP.setReturnCode(false);
    paneBodyCQP.setRefInputType(1);
    //
    return true;
  }

  /**
   * ���ߣ���־ƽ ���ܣ��Ƿ���Ʒ�༭ǰ���� ������BillEditEvent e ���أ��� ���⣺�� ���ڣ�(2004-10-29 10:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public boolean beforeEditBodyBlargess(BillEditEvent e) {
    stopEditing();
    // �Ƿ����ɵ����ƻ�
    String sRcPlan = getBodyValueAt(e.getRow(), "breceiveplan").toString();
    if (sRcPlan != null && "Y".equalsIgnoreCase(sRcPlan.trim())) {
      return false;
    }
    return true;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�����༭ǰ���� ������BillEditEvent e ���أ��� ���⣺�� ���ڣ�(2002-7-22 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public boolean beforeEditBodyInventory(BillEditEvent e) {
      
      //ֹͣ�༭!!!
      stopEditing();

      int iRow = e.getRow();
      String sClassId = null;
      //�ϲ���ԴΪ��ͬ
      String sUpSourceType = (String) getBodyValueAt(iRow, "cupsourcebilltype");
      if (sUpSourceType != null
              && (sUpSourceType.equals(BillTypeConst.CT_BEFOREDATE) 
                      || sUpSourceType.equals(BillTypeConst.CT_ORDINARY))) {
    	    //setCellEditable(iRow, "cinventorycode", false);
          String sCntRowId = (String) getBodyValueAt(iRow, "ccontractrowid");
          RetCtToPoQueryVO voCntInfo = PoPublicUIClass.getCntInfo(sCntRowId);
          if (voCntInfo != null && (voCntInfo.getIinvCtl() == RetCtToPoQueryVO.INVNULLCTL || voCntInfo.getIinvCtl() == RetCtToPoQueryVO.INVCLASSCTL)) {
              //��������ͬ�Ĵ��ֻ���ڸ�����
              sClassId = voCntInfo.getCInvClass();
              //setCellEditable(iRow, "cinventorycode", true);
          }
      }
      //�����Ƿ��������ͬ�ò���
      UIRefPane pane = ((UIRefPane) getBodyItem("cinventorycode").getComponent());      
      pane.setPk_corp(getCorp());      
      PoInputInvRefModel refModel = (PoInputInvRefModel)pane.getRefModel();      
      refModel.setChangeTableSeq(PuPubVO.getString_TrimZeroLenAsNull(sClassId) == null); 
      refModel.setPk_corp(getCorp());
      refModel.setInvClassId(sClassId);
      
      try{
		    boolean checker = false;
		    Object[] oaTemp = (Object[]) CacheTool.getCellValue("bd_busitype","pk_busitype","verifyrule",m_strCbiztype);
  			if(oaTemp != null && oaTemp.length > 0 && oaTemp[0] != null){
  				checker = "S".equalsIgnoreCase(oaTemp[0].toString().trim());
  			}
		    if(checker){
		        refModel.addWherePart(" and (sellproxyflag = 'Y') ");
		    }
      }catch(Exception ex){
      	ex.printStackTrace();
      }
      return true;
  }




  /**
   * ���ߣ���ӡ�� ���ܣ����κű༭ǰ���� ������ActionEvent e ��׽����ActionEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-7-22 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private boolean beforeEditBodyProduceNum(BillEditEvent e) {

    int iRow = e.getRow();
    ParaVOForBatch vo = new ParaVOForBatch();
    // ����FieldName
    vo.setMangIdField("cmangid");
    vo.setInvCodeField("cinventorycode");
    vo.setInvNameField("cinventoryname");
    vo.setSpecificationField("cspecification");
    vo.setInvTypeField("ctype");
    vo.setMainMeasureNameField("cmessureunitname");
    vo.setAssistUnitIDField("cassistunit");
    vo.setIsAstMg(new UFBoolean(PuTool
        .isAssUnitManaged((String) getBodyValueAt(iRow, "cbaseid"))));
    vo.setWarehouseIDField("cwarehouseid");
    vo.setFreePrefix("vfree");
    // ���ÿ�Ƭģ��,��˾��
    vo.setCardPanel(this);
    vo.setPk_corp(getCorp());
    vo.setEvent(e);
    try {
      nc.ui.pu.pub.PuTool.beforeEditWhenBodyBatch(vo);
    }
    catch (Exception exp) {
      PuTool.outException(this, exp);
    }

    return true;
  }

  /**
   * ���ߣ����� ���ܣ���������е�Ԥ�Ƶ������� ������int iCurRow ������ Object oReason ���¼ƻ��������������¼����ԭ��
   * Ϊ���������ڡ�����������֯ ֵ֮ ���أ��� ���⣺�� ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 2002-04-22 wyf wyf add/modify/delete 2002-03-21 begin/end 2003-01-20 wyf
   * �޸�Ϊ������޸ļƻ��������ڵ�ԭ��Ϊ�գ����޸�ԭֵ ���δ�õ��ɹ���ǰ�ڣ������������üƻ��������� 2003-02-26 wyf �޸�һ����ת������
   */
  private void calDplanarrvdate(int iBeginRow, int iEndRow) {

    // ��������
    String sOrderDate = getHeadItem("dorderdate").getValue();
    if (PuPubVO.getUFDate(sOrderDate) == null) {
      return;
    }
    /*
     * V5 Del: String sStoreId = getHeadItem("cstoreorganization").getValue();
     * if (PuPubVO.getString_TrimZeroLenAsNull(sStoreId) == null) { return; }
     */
    // Ԥ�Ƶ�������
    UFDate dOrderDate = new UFDate(sOrderDate.trim());
    Vector vecRow = new Vector();
    Vector vecBaseId = new Vector();
    Vector vecStorId = new Vector();
    Vector vecOrderNum = new Vector();
    for (int i = iBeginRow; i <= iEndRow; i++) {
      // ���ԭ����ֵ���򲻸ı�
      Object oPlanArrvDate = getBodyValueAt(i, "dplanarrvdate");
      if (PuPubVO.getString_TrimZeroLenAsNull(oPlanArrvDate) != null) {
        continue;
      }
      // ����ID
      String sBaseId = PuPubVO
          .getString_TrimZeroLenAsNull((String) getBodyValueAt(i, "cbaseid"));
      if (sBaseId == null) {
        continue;
      }
      String sStoreId = PuPubVO
          .getString_TrimZeroLenAsNull((String) getBodyValueAt(i,
              "pk_arrvstoorg"));
      if (sStoreId == null) {
        continue;
      }
      // ������Ϊ0
      UFDouble dOrderNum = PuPubVO.getUFDouble_ZeroAsNull(getBodyValueAt(i,
          "nordernum"));
      if (dOrderNum == null) {
        continue;
      }
      vecRow.add(new Integer(i));
      vecBaseId.add(sBaseId);
      vecOrderNum.add(dOrderNum);
      vecStorId.add(sStoreId);
    }

    int iReSetLen = vecBaseId.size();
    if (iReSetLen == 0) {
      return;
    }

    // ����Ԥ�Ƶ�������
    UFDate[] daPlanDate = PoPublicUIClass.getDPlanArrvDateArray(getCorp(),
        (String[]) vecStorId.toArray(new String[iReSetLen]), dOrderDate,
        (String[]) vecBaseId.toArray(new String[iReSetLen]),
        (UFDouble[]) vecOrderNum.toArray(new UFDouble[iReSetLen]));

    // ������ֵ
    for (int i = 0; i < iReSetLen; i++) {
      setBodyValueAt(daPlanDate[i], ((Integer) vecRow.get(i)).intValue(),
          "dplanarrvdate");
    }
  }

  /**
   * ���ߣ����� ���ܣ���������е�������ϵ ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2001-10-20 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-05-16 wyf ��������������۸����ȵķ�ʽ
   * 2002-10-21 wyf ����Ժ�˰���۵ļ���
   */
  private void calRelation(BillEditEvent e) {
    int iRow = e.getRow();
    // ���þ���
    setRowDigits_Mny(getCorp(), iRow, getBillModel(), m_cardPoPubSetUI2);
    // ����
    int[] iaDescription = {
        RelationsCal.DISCOUNT_TAX_TYPE_NAME,
        RelationsCal.DISCOUNT_TAX_TYPE_KEY, RelationsCal.TAXRATE,
        RelationsCal.MONEY_ORIGINAL, RelationsCal.TAX_ORIGINAL,
        RelationsCal.SUMMNY_ORIGINAL, RelationsCal.NUM,
        RelationsCal.NET_PRICE_ORIGINAL, RelationsCal.PRICE_ORIGINAL,

        RelationsCal.NET_TAXPRICE_ORIGINAL,// ����˰����
        RelationsCal.TAXPRICE_ORIGINAL,// ��˰����

        RelationsCal.DISCOUNT_RATE, RelationsCal.CONVERT_RATE,
        RelationsCal.NUM_ASSIST, RelationsCal.IS_FIXED_CONVERT
    };

    // �Ƿ�̶�������
    String sBaseId = (String) getBillModel().getValueAt(iRow, "cbaseid");
    String sAssistUnit = (String) getBillModel()
        .getValueAt(iRow, "cassistunit");
    String sFixFlag = PuTool.isFixedConvertRate(sBaseId, sAssistUnit) ? "Y"
        : "N";
    // ��ȡ��˰��𣬲�����
    String strDisType = VariableConst.IDISCOUNTTAXTYPE_NAME_OUTTER_NO_TRANS;// Ӧ˰�ں�
    if (getBillModel().getValueAt(iRow, "idiscounttaxtype") != null) {
      strDisType = getBillModel().getValueAt(iRow, "idiscounttaxtype").toString();
      if (strDisType.equals("0")) {
        strDisType = VariableConst.IDISCOUNTTAXTYPE_NAME_INNER_NO_TRANS;// Ӧ˰�ں�
      }
      else if (strDisType.equals("1")) {
        strDisType = VariableConst.IDISCOUNTTAXTYPE_NAME_OUTTER_NO_TRANS;// Ӧ˰���
      }
      else if (strDisType.equals("2")) {
        strDisType = VariableConst.IDISCOUNTTAXTYPE_NAME_NOCOUNT_NO_TRANS;// ����˰
      }
    }
    // int index = getBillModel().getBodyColByKey("idiscounttaxtype");
    // if (index >= 0) {
    // if (getBillModel().getBodyItems()[getBillModel().getBodyColByKey(
    // "idiscounttaxtype")] != null) {
    // BillItem bi = getBillModel().getBodyItems()[getBillModel()
    // .getBodyColByKey("idiscounttaxtype")];
    // if (bi.getComponent() instanceof UIComboBox) {
    // index = ((UIComboBox) bi.getComponent()).getSelectedIndex();
    // if (index == 0) {
    // strDisType = VariableConst.IDISCOUNTTAXTYPE_NAME_INNER_NO_TRANS;// Ӧ˰�ں�
    // } else if (index == 1) {
    // strDisType = VariableConst.IDISCOUNTTAXTYPE_NAME_OUTTER_NO_TRANS;// Ӧ˰���
    // } else if (index == 2) {
    // strDisType = VariableConst.IDISCOUNTTAXTYPE_NAME_NOCOUNT_NO_TRANS;// ����˰
    // }
    // }
    // }
    // }
    String[] saKeys = {
        strDisType, "idiscounttaxtype", "ntaxrate", "noriginalcurmny",
        "noriginaltaxmny", "noriginaltaxpricemny", "nordernum",
        "noriginalnetprice", "noriginalcurprice", "norgnettaxprice",
        "norgtaxprice", "ndiscountrate", "nconvertrate", "nassistnum", sFixFlag
    };

    // ��������
    RelationsCal.calculate(this, e, this.getBillModel(), new int[] {
        PuTool.getPricePriorPolicy(getCorp()), iChange
    }, iaDescription, saKeys, OrderItemVO.class.getName());
  }

  /**
   * ���ߣ�WYF ���ܣ����һ������VO�ĺ���������Ϣ ��ʹ�����������øú�������ʹ�����������Ϣͬ���� ������OrderVO voOrder ����VO
   * ���أ��� ���⣺�� ���ڣ�(2004-05-25 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public void clearAfterBillInfo(String sOrderId) {

    if (PuPubVO.getString_TrimZeroLenAsNull(sOrderId) == null) {
      return;
    }
    // ���
    h_mapAfterBill.remove(sOrderId);

  }

  /**
   * ���ߣ�WYF ���ܣ�ʵ����ʾָ��λ�õĲɹ���������ȫ������ʾ ������OrderVO voShow ���� ���أ��� ���⣺��
   * ���ڣ�(2001-10-20 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public void displayCurVO(OrderVO voViewCur, boolean bLoadVendCodeName) {

    nc.vo.scm.pu.Timer timeDebug = new nc.vo.scm.pu.Timer();
    timeDebug.start();

    // ��һ�ŵ��ݵĹ�˾
    String sOldPk_corp = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
        "pk_corp").getValue());

    // �����Ǽ��Ųɹ����漰�൥λ�����Ҫ���ò���
    setCorp(voViewCur.getHeadVO().getPk_corp());

    // �������ԭ���ݺ���ʾ������
    addNew();
    getBillModel().clearBodyData();

    // ���ò���
    setRefPane();

    timeDebug.addExecutePhase("���ò���");/* -=notranslate=- */

    // �����������۸񾫶�
    setHeadDigits(getCorp());
    setBodyDigits_CorpRelated(getCorp(), getBillModel());
    timeDebug.addExecutePhase("�����������۸񾫶�");/* -=notranslate=- */

    // �رպϼƿ���
    boolean bOldNeedCalc = getBillModel().isNeedCalculate();
    getBillModel().setNeedCalculate(false);
    // ����VO
    setBillValueVO(voViewCur);
    timeDebug.addExecutePhase("����VO");/* -=notranslate=- */
    // ������� : ¼�붩����Ĭ�ϴ�����ǰ����Ա��Ӧ��ҵ��Ա�Ͳ��ţ�������һ��������Ƭ���沿�ű����
    if (getHeadItem("cdeptid").getValue() == null
        || (getHeadItem("cdeptid").getValue() != null && getHeadItem("cdeptid")
            .getValue().trim().length() == 0)) {
      getHeadItem("cdeptid").setValue(voViewCur.getHeadVO().getCdeptid());
    }

    // �������ñ�ͷ����
    // if (!voViewCur.getHeadVO().getPk_corp().equals(sOldPk_corp)) {
    // setRefPane_Head();
    // getHeadItem("cvendorbaseid").getValue();
    // }
    // timeDebug.addExecutePhase("�������ñ�ͷ����");/*-=notranslate=-*/

    // ���ù�Ӧ��ȫ��
    String formula = "ccustname->getColValue(bd_cubasdoc,custname,pk_cubasdoc,cvendorbaseid)";
    execHeadFormula(formula);

    // ���ñ�ͷ���¼�����ʾ��ȡ��һ�е�ֵ
    OrderItemVO voFirstItem = voViewCur.getBodyVO()[0];
    getHeadItem("ccurrencytypeid").setValue(voFirstItem.getCcurrencytypeid());
    // ���ñ�ͷ���־���
    resetHeadCurrDigits();
    //
    getHeadItem("nexchangeotobrate").setValue(
        voFirstItem.getNexchangeotobrate());
    //
    getHeadItem("cprojectid").setValue(voFirstItem.getCprojectid());
    getHeadItem("idiscounttaxtype").setValue(voFirstItem.getIdiscounttaxtype());
    getHeadItem("ntaxrate").setValue(voFirstItem.getNtaxrate());

    // ���ñ����������ص���ֵ
    try {
      resetBodyValueRelated_Curr(getCorp(), voFirstItem.getCcurrencytypeid(),
          getBillModel(), new BusinessCurrencyRateUtil(getCorp()),
          getRowCount(), m_cardPoPubSetUI2);
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }
    timeDebug.addExecutePhase("�������ñ�����ص���ֵ");/* -=notranslate=- */

    // ��ͷ��ע
    ((UIRefPane) getHeadItem("vmemo").getComponent()).setText(voViewCur
        .getHeadVO().getVmemo());

    // ��ͷ��Ӧ���շ�����ַ
    ((UIRefPane) getHeadItem("cdeliveraddress").getComponent())
        .setText(voViewCur.getHeadVO().getCdeliveraddress());

    // ���幫ʽ
    getBillModel().execLoadFormula();
    // getBillModel().execFormulas( new String[]{
    // "cprojectid->getColValue(bd_jobmanfil,pk_jobbasfil,pk_jobmanfil,cprojectid)"
    // ,"cproject->getColValue(bd_jobbasfil,jobname,pk_jobbasfil,cprojectid)"});
    timeDebug.addExecutePhase("���幫ʽ");/* -=notranslate=- */


    // ����������
    PoPublicUIClass.setFreeColValue(getBillModel(), "vfree");
    // ���㲢���û�����
    PuTool.setBillModelConvertRate(getBillModel(), new String[] {
        "cbaseid", "cassistunit", "nordernum", "nassistnum", "nconvertrate"
    });
    timeDebug.addExecutePhase("���㲢���û�����");/* -=notranslate=- */

    // ������Դ�������͡���Դ���ݺ�
    PuTool.loadSourceInfoAll(this, BillTypeConst.PO_ORDER);
    timeDebug.addExecutePhase("������Դ�������͡���Դ���ݺ�");/* -=notranslate=- */

    // ����޼�
    PoPublicUIClass.loadCardMaxPrice(this, getCorp(), m_cardPoPubSetUI2);
    timeDebug.addExecutePhase("����޼�");/* -=notranslate=- */

    // ��ʾ��Ӧ�̵Ĵ�����뼰����
    /*
     * String strVendorId = voShow.getHeadVO().getCvendormangid(); String[]
     * saMangId = new String[voShow.getBodyVO().length]; for (int i = 0; i <
     * saMangId.length; i++) { saMangId[i] = voShow.getBodyVO()[i].getCmangid();
     * }
     * PuTool.loadVendorInvInfos(strVendorId,saMangId,getBillModel(),0,saMangId
     * .length -1);
     */
    // ��ʾ��Ӧ�̵Ĵ�����뼰����
    if (bLoadVendCodeName) {
      String strVendorId = voViewCur.getHeadVO().getCvendormangid();
      String[] saMangId = new String[voViewCur.getBodyVO().length];
      for (int i = 0; i < saMangId.length; i++) {
        saMangId[i] = voViewCur.getBodyVO()[i].getCmangid();
      }
      PuTool.loadVendorInvInfos(strVendorId, saMangId, getBillModel(), 0,
          saMangId.length - 1);
    }

    // ִ�б�ͷ���ع�ʽ
    execHeadLoadFormulas();

    Object[] obj = null;
    try {
      obj = (Object[]) CacheTool.getCellValue("bd_bankaccbas", "pk_bankaccbas",
          "accountname", voViewCur.getHeadVO().getCaccountbankid());
    }
    catch (Exception e) {
      PuTool.outException(this, e);
    }
    if (obj != null) {
      ((UIRefPane) getHeadItem("caccountbankid").getComponent())
          .setText((String) obj[0]);
    }
    timeDebug.addExecutePhase("ִ�б�ͷ���ع�ʽ");/* -=notranslate=- */
    //
    updateValue();
    //
    updateUI();

    timeDebug.showAllExecutePhase("�ɹ�������Ƭ��ʾ");/* -=notranslate=- */
    // ֧���û��Զ��幫ʽ���±�����ʧ
    if (getHeadItem("ccurrencytypeid").getValue() == null) {
      getHeadItem("ccurrencytypeid").setValue(voFirstItem.getCcurrencytypeid());
    }
    // �﷫�Ĳ�����֧�ֱ�ͷ˰��Ϊ��������
    if (getHeadItem("ntaxrate") != null
        && getHeadItem("ntaxrate").getComponent() instanceof UIComboBox) {
      ((UIComboBox) getHeadItem("ntaxrate").getComponent()).setSelectedIndex(0);
    }
    // ���ʣ�Ĭ��Я�����ֱ��еĻ��ʣ��������ݣ���Ĭ��Ϊ1�����޸�
    // getHeadItem("ccurrencytypeid").setValue(
    // voFirstItem.getCcurrencytypeid());
    ((UIRefPane) getHeadItem("cprojectid").getComponent())
        .setText((String) getBillModel().getValueAt(0, "cproject"));
//    // �򿪺ϼƿ���
    getBillModel().setNeedCalculate(bOldNeedCalc);
  }

  /**
   * ���ߣ�WYF ���ܣ�ʵ����ʾָ��λ�õĲɹ���������ȫ������ʾ ��������ר�� ������OrderVO voShow ���� ���أ��� ���⣺��
   * ���ڣ�(2001-10-20 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public void displayCurVOForLinkQry(OrderVO voViewCur,
      boolean bLoadVendCodeName) {

    nc.vo.scm.pu.Timer timeDebug = new nc.vo.scm.pu.Timer();
    timeDebug.start();

    // ��һ�ŵ��ݵĹ�˾
    String sOldPk_corp = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
        "pk_corp").getValue());

    // �����Ǽ��Ųɹ����漰�൥λ�����Ҫ���ò���
    setCorp(voViewCur.getHeadVO().getPk_corp());

    // �������ԭ���ݺ���ʾ������
    addNew();
    getBillModel().clearBodyData();

    // ���ò���
    // if (!voViewCur.getHeadVO().getPk_corp().equals(sOldPk_corp)) {
    // setRefPane();
    // }
    // timeDebug.addExecutePhase("���ò���");/*-=notranslate=-*/

    // �����������۸񾫶�
    setHeadDigits(getCorp());
    setBodyDigits_CorpRelated(getCorp(), getBillModel());
    timeDebug.addExecutePhase("�����������۸񾫶�");/* -=notranslate=- */

    setRefPane();

    // ����VO
    setBillValueVO(voViewCur);
    timeDebug.addExecutePhase("����VO");/* -=notranslate=- */

    // �������ñ�ͷ����
    // if (!voViewCur.getHeadVO().getPk_corp().equals(sOldPk_corp)) {
    // setRefPane_Head();
    // getHeadItem("cvendorbaseid").getValue();
    // }
    // timeDebug.addExecutePhase("�������ñ�ͷ����");/*-=notranslate=-*/

    // ���ù�Ӧ��ȫ��
    String formula = "ccustname->getColValue(bd_cubasdoc,custname,pk_cubasdoc,cvendorbaseid)";
    execHeadFormula(formula);

    // ���ñ�ͷ���¼�����ʾ��ȡ��һ�е�ֵ
    OrderItemVO voFirstItem = voViewCur.getBodyVO()[0];
    getHeadItem("ccurrencytypeid").setValue(voFirstItem.getCcurrencytypeid());
    // ���ñ�ͷ���־���
    resetHeadCurrDigits();
    //
    getHeadItem("nexchangeotobrate").setValue(
        voFirstItem.getNexchangeotobrate());
    //
    getHeadItem("cprojectid").setValue(voFirstItem.getCprojectid());
    getHeadItem("idiscounttaxtype").setValue(voFirstItem.getIdiscounttaxtype());
    getHeadItem("ntaxrate").setValue(voFirstItem.getNtaxrate());
    // ���ñ����������ص���ֵ
    try {
      resetBodyValueRelated_Curr(getCorp(), voFirstItem.getCcurrencytypeid(),
          getBillModel(), new BusinessCurrencyRateUtil(getCorp()),
          getRowCount(), m_cardPoPubSetUI2);
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }
    timeDebug.addExecutePhase("�������ñ�����ص���ֵ");/* -=notranslate=- */

    // ��ͷ��ע
    ((UIRefPane) getHeadItem("vmemo").getComponent()).setText(voViewCur
        .getHeadVO().getVmemo());

    // ��ͷ��Ӧ���շ�����ַ
    ((UIRefPane) getHeadItem("cdeliveraddress").getComponent())
        .setText(voViewCur.getHeadVO().getCdeliveraddress());

    // �رպϼƿ���
    boolean bOldNeedCalc = getBillModel().isNeedCalculate();
    getBillModel().setNeedCalculate(false);

    // ���幫ʽ
    getBillModel().execLoadFormula();
    timeDebug.addExecutePhase("���幫ʽ");/* -=notranslate=- */

    // �򿪺ϼƿ���
    getBillModel().setNeedCalculate(bOldNeedCalc);

    // ����������
    PoPublicUIClass.setFreeColValue(getBillModel(), "vfree");
    // ���㲢���û�����
    PuTool.setBillModelConvertRate(getBillModel(), new String[] {
        "cbaseid", "cassistunit", "nordernum", "nassistnum", "nconvertrate"
    });
    timeDebug.addExecutePhase("���㲢���û�����");/* -=notranslate=- */

    // ������Դ�������͡���Դ���ݺ�
    PuTool.loadSourceInfoAll(this, BillTypeConst.PO_ORDER);
    timeDebug.addExecutePhase("������Դ�������͡���Դ���ݺ�");/* -=notranslate=- */

    // ����޼�
    PoPublicUIClass.loadCardMaxPrice(this, getCorp(), m_cardPoPubSetUI2);
    timeDebug.addExecutePhase("����޼�");/* -=notranslate=- */

    // ��ʾ��Ӧ�̵Ĵ�����뼰����
    /*
     * String strVendorId = voShow.getHeadVO().getCvendormangid(); String[]
     * saMangId = new String[voShow.getBodyVO().length]; for (int i = 0; i <
     * saMangId.length; i++) { saMangId[i] = voShow.getBodyVO()[i].getCmangid();
     * }
     * PuTool.loadVendorInvInfos(strVendorId,saMangId,getBillModel(),0,saMangId
     * .length -1);
     */
    // ��ʾ��Ӧ�̵Ĵ�����뼰����
    if (bLoadVendCodeName) {
      String strVendorId = voViewCur.getHeadVO().getCvendormangid();
      String[] saMangId = new String[voViewCur.getBodyVO().length];
      for (int i = 0; i < saMangId.length; i++) {
        saMangId[i] = voViewCur.getBodyVO()[i].getCmangid();
      }
      PuTool.loadVendorInvInfos(strVendorId, saMangId, getBillModel(), 0,
          saMangId.length - 1);
    }

    // ִ�б�ͷ���ع�ʽ
    execHeadLoadFormulas();
    timeDebug.addExecutePhase("ִ�б�ͷ���ع�ʽ");/* -=notranslate=- */
    //
    updateValue();
    //
    updateUI();

    timeDebug.showAllExecutePhase("�ɹ�������Ƭ��ʾ");/* -=notranslate=- */

    // �﷫�Ĳ�����֧�ֱ�ͷ˰��Ϊ��������
    if (getHeadItem("ntaxrate") != null
        && getHeadItem("ntaxrate").getComponent() instanceof UIComboBox) {
      ((UIComboBox) getHeadItem("ntaxrate").getComponent()).setSelectedIndex(0);
    }
    // ���ʣ�Ĭ��Я�����ֱ��еĻ��ʣ��������ݣ���Ĭ��Ϊ1�����޸�
    // getHeadItem("ccurrencytypeid").setValue(
    // voFirstItem.getCcurrencytypeid());
  }

  /**
   * ���ߣ�WYF ���ܣ��ڸոձ�����Ϻ��޸Ĵ˷�����ʾ������Ϊ���Ч�����
   * ����ǰ������д�����Ķ�������ʱֻ������һЩ���ܻᷢ���仯����Ϣ���ٰ��к����򼴿ɡ� ������OrderVO voJustSaved
   * ������ϵĶ��������б���ͷ��ID��ͷ��TS�������ˡ�����ʱ�� ���أ��� ���⣺�� ���ڣ�(2004-06-07 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public void displayCurVOAfterJustSave(OrderVO voJustSaved) {

    nc.vo.scm.pu.Timer timeDebug = new nc.vo.scm.pu.Timer();
    timeDebug.start();

    if (voJustSaved == null) {
      return;
    }

    // ɾ�����õ���
    int[] iaInvNullRow = PuTool.getRowsWhenAttrNull(getBillModel(),
        OrderItemVO.FILTERKEY_WHENSAVE);
    if (iaInvNullRow != null) {
      getBillModel().delLine(iaInvNullRow);
    }

    // �����к����顢����VO����
    String[] saUIRowNo = (String[]) PuGetUIValueTool.getArray(getBillModel(),
        "crowno", String.class);
    OrderVO voCheck = voJustSaved.getCheckVO();
    OrderItemVO[] voaItem = voCheck.getBodyVOsByRowNos(saUIRowNo);

    int iUILen = getRowCount();
    String sRowNo = null;
    for (int i = 0; i < iUILen; i++) {
      sRowNo = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(i, "crowno"));
      if (sRowNo == null) {
        continue;
      }
      if (voaItem[i] == null) {
        continue;
      }
      // ���кŶ�Ӧ��VO
      setBodyValueAt(voaItem[i].getCorderid(), i, "corderid");
      setBodyValueAt(voaItem[i].getCorder_bid(), i, "corder_bid");
      setBodyValueAt(voaItem[i].getTs(), i, "ts");
      setBodyValueAt(voaItem[i].getVproducenum(), i, "vproducenum");
      setBodyValueAt(voaItem[i].getNmoney(), i, "nmoney");
      setBodyValueAt(voaItem[i].getNtaxmny(), i, "ntaxmny");
      setBodyValueAt(voaItem[i].getNtaxpricemny(), i, "ntaxpricemny");
      // since v55, ��������빺������ɹ���ֱ���޸ĵ����ٱ���ͱ���������
      setBodyValueAt(voaItem[i].getCupsourcebts(), i, "cupsourcebts");
      setBodyValueAt(voaItem[i].getCupsourcehts(), i, "cupsourcehts");
      // �����÷���!!!!!
      getBillModel().setRowState(i, BillModel.NORMAL);
    }

    setHeadItem("corderid", voCheck.getHeadVO().getCorderid());
    setHeadItem("vordercode", voCheck.getHeadVO().getVordercode());
    setHeadItem("forderstatus", voCheck.getHeadVO().getForderstatus());
    setHeadItem("ts", voCheck.getHeadVO().getTs());

    setTailItem("cauditpsn", voCheck.getHeadVO().getCauditpsn());
    setTailItem("dauditdate", voCheck.getHeadVO().getDauditdate());
    setTailItem("tmaketime", voCheck.getHeadVO().getTmaketime());
    setTailItem("taudittime", voCheck.getHeadVO().getTaudittime());
    setTailItem("tlastmaketime", voCheck.getHeadVO().getTlastmaketime());
    // since v55
    setTailItem("trevisiontime", voCheck.getHeadVO().getTrevisiontime());

    timeDebug.addExecutePhase("��ֵ");/* -=notranslate=- */

    // ֧���û��Զ��幫ʽ���±�����ʧ

    // ���к�����
    getBillModel().sortByColumn("crowno", true);
    timeDebug.addExecutePhase("���к�����");/* -=notranslate=- */
    // ִ�б�ͷ���ع�ʽ
//    execHeadLoadFormulas();

    if (getHeadItem("ccurrencytypeid").getValue() == null) {
      getHeadItem("ccurrencytypeid").setValue(
          voJustSaved.getBodyVO()[0].getCcurrencytypeid());      
    }
    if (getHeadItem("nexchangeotobrate").getValue() == null || 
    		getHeadItem("nexchangeotobrate").getValue().trim().length()==0) {
        getHeadItem("nexchangeotobrate").setValue(
            voJustSaved.getBodyVO()[0].getNexchangeotobrate());
      }
    timeDebug.addExecutePhase("ִ�б�ͷ���ع�ʽ");/* -=notranslate=- */
    //
    updateUI();

    timeDebug.showAllExecutePhase("�ɹ�������Ƭ��ʾ");/* -=notranslate=- */

  }

  /**
   * ���ߣ���ӡ�� ���ܣ�Ѱ��ĳЩ�еĺ�ͬ ������Integer[] iaRow ���ѯ��Ϣ���� ���أ�HashMap KEY: Integer
   * VALUE: RetCtToPoQueryVO ���ܷ���NULL ���⣺�� ���ڣ�(2003-10-30 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  
  private Vector<RetCtToPoQueryVO> findCntInfoForRow(Integer[] iaRow) {

    // ������ȷ���ж�
    if (iaRow == null) {
      return null;
    }

    // ��˾
    String pk_corp = getHeadItem("pk_corp").getValue();
    // ��Ӧ�̹���ID
    String sVendBaseId = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
        "cvendorbaseid").getValue());
    // ����
    String sDate = PuPubVO
        .getString_TrimZeroLenAsNull(getHeadItem("dorderdate").getValue());

    String sTransMode = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
        "ctransmodeid").getValue());

    // �й�Ӧ�̼�����ʱ��������Ѱ�Һ�ͬ
    if (pk_corp == null || sVendBaseId == null || sDate == null) {
      return null;
    }

    // ���ܳ���
    int iTotalLen = iaRow.length;
    // KEY:�к� VALUE:RetCtToPoQueryVO ��ͬVO
    HashMap mapRowAndCnt = new HashMap();

    // ���к�ͬ��ID���ֶ�
    Vector vecHaveCntIndex = new Vector();
    Vector vecCntRowId = new Vector();
    // ���ѯ��ͬ����
    Vector vecNoCntIndex = new Vector();
    Vector vecNoCntBaseId = new Vector();
    Vector vecDevareaname = new Vector();
    Vector vecCurrtypeid = new Vector();
    Vector vecRowNo = new Vector();

    // ��ʱ����
    int iRow = 0;
    String sBaseId = null;
    String sDevarea = null;
    String sCurrentypeid = null;
    String sRowno = null;
    // �������к�ͬ���޺�ͬ����
    for (int i = 0; i < iTotalLen; i++) {
      iRow = iaRow[i].intValue();

      String sCntRowId = (String) getBodyValueAt(iRow, "ccontractrowid");
      if (PuPubVO.getString_TrimZeroLenAsNull(sCntRowId) != null) {
        vecHaveCntIndex.add(iaRow[i]);
        vecCntRowId.add(sCntRowId);
      }
      else {
        sBaseId = (String) getBodyValueAt(iRow, "cbaseid");
        sDevarea = (String) getBodyValueAt(iRow, "cdevareaid");
        sCurrentypeid = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(
            iRow, "ccurrencytypeid"));
        sRowno = (String) getBodyValueAt(iRow, "crowno");
        if (PuPubVO.getString_TrimZeroLenAsNull(sBaseId) != null) {
          vecNoCntIndex.add(iaRow[i]);
          vecNoCntBaseId.add(sBaseId);
          vecDevareaname.add(sDevarea);
          vecCurrtypeid.add(sCurrentypeid);
          vecRowNo.add(sRowno);
        }
      }
    }
    // �洢���к�ͬ���ص�VO
    Vector<RetCtToPoQueryVO> vAllCTinfo = new Vector<RetCtToPoQueryVO>();

    // ���к�ͬ����
    int iHaveCntLen = vecHaveCntIndex.size();
    if (iHaveCntLen > 0) {
      RetCtToPoQueryVO[] voaCtInfo = PoPublicUIClass
          .getCntInfoArray((String[]) vecCntRowId
              .toArray(new String[iHaveCntLen]));
      for (int i = 0; i < iHaveCntLen; i++) {
        iRow = ((Integer) vecHaveCntIndex.get(i)).intValue();
        if (voaCtInfo == null || voaCtInfo[i] == null) {
          sBaseId = (String) getBodyValueAt(iRow, "cbaseid");
          sCurrentypeid = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(
              iRow, "ccurrencytypeid"));
          sRowno = (String) getBodyValueAt(iRow, "crowno");
          vecNoCntIndex.add(vecHaveCntIndex.get(i));
          vecNoCntBaseId.add(sBaseId);
          vecCurrtypeid.add(sCurrentypeid);
          vecRowNo.add(sRowno);
        }
        else {
          // �����к�
          voaCtInfo[i].setAttributeValue("crowno", getBodyValueAt(iRow,
              "crowno"));
          vAllCTinfo.add(voaCtInfo[i]);
        }
      }
    }
    // �޺�ͬ����
    // ���ú�ͬ�ӿڣ���ѯ������һά���飬����ֵ�а����ˡ��кš���
    int iNoCntLen = vecNoCntIndex.size();
    ICtToPo_QueryCt ctservice = (ICtToPo_QueryCt) nc.bs.framework.common.NCLocator
        .getInstance().lookup(ICtToPo_QueryCt.class.getName());
    RetCtToPoQueryVO[] retFromCT = null;
    try {
      retFromCT = ctservice.queryForCntAll((String[]) vecRowNo
          .toArray(new String[0]), pk_corp, (String[]) vecNoCntBaseId
          .toArray(new String[iNoCntLen]), (String[]) PoPublicUIClass
          .getSameValueArray(sVendBaseId, iNoCntLen), (String[]) vecCurrtypeid
          .toArray(new String[iNoCntLen]),  new UFDate(sDate));
    }
    catch (BusinessException e) {
      // TODO �Զ����� catch ��
      e.printStackTrace();
    }
    /**********/
    if (retFromCT != null) {
      for (RetCtToPoQueryVO retCtVO : retFromCT) {
        vAllCTinfo.add(retCtVO);
      }
    }
    return vAllCTinfo;
    /**********/

    // return mapRowAndCnt.size() == 0 ? null : mapRowAndCnt;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��õ������� �������� ���أ��� ���⣺�� ���ڣ�(2003-10-09 13:24:16) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private Container getContainer() {
    return m_conInvoker;
  }

  private String[] getFreeCustBank(String cfreecustid) {
    String[] sbanks = null;
    try {
      sbanks = OrderHelper.getFreecustBank(cfreecustid);
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }

    return sbanks;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��õ������� �������� ���أ��� ���⣺�� ���ڣ�(2003-10-09 13:24:16) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private IPoCardPanel getIContainer() {
    if (getContainer() == null || !(getContainer() instanceof IPoCardPanel)) {
      return null;
    }

    return (IPoCardPanel) getContainer();
  }

  /**
   * ���ߣ�WYF ���ܣ��õ����б�ͷ����ITEMKEY �������� ���أ��� ���⣺�� ���ڣ�(2003-10-14 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private String[] getRefItemKeysBody() {
    if (m_saBodyRefItemKey == null) {
      m_saBodyRefItemKey = new String[] {
          "cinventorycode", "cassistunitname", "ccontractcode",
          "ccurrencytype", "cusedept", "cproject", "cprojectphase",
          "cwarehouse", "cdevaddrname", "cdevareaname", "coperatorname",
          "coperator", "vmemo", "cvenddevareaname", "cvenddevaddrname",
          "vreceiveaddress", "vvenddevaddr"
      };
    }
    return m_saBodyRefItemKey;
  }

  /**
   * ���ߣ�WYF ���ܣ��õ����б�ͷ����ITEMKEY �������� ���أ��� ���⣺�� ���ڣ�(2003-10-14 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private String[] getRefItemKeysHead() {
    if (m_saHeadRefItemKey == null) {
      m_saHeadRefItemKey = new String[] {
          "ccurrencytypeid", "cunfreeze", "pk_corp", "cbiztype",
          "cpurorganization", "cstoreorganization", /* "cvendormangid", */
          "caccountbankid", "cdeliveraddress", "cemployeeid", "cdeptid",
          "creciever", "cgiveinvoicevendor", "ctransmodeid", "cfreecustid",
          "ctermprotocolid", "cprojectid", "vmemo"
      };
    }
    return m_saHeadRefItemKey;
  }

  /**
   * ���ߣ�WYF ���ܣ���ȡ��Ҫ����ĵ���VO�� ������PoCardPanel pnlCard �������ݿ�Ƭ OrderVO voOld
   * �ɵĶ���VO������ޣ�ΪNULL������У��������ĵ���VO ���أ�OrderVO �豣��ĵ���VO ���ݼ��е�״̬����������� ���⣺
   * ���ڣ�(2003-9-9 11:37:13) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2003-10-08 wyf �޸ĶԼ���Ŀ��еĴ���
   * 2004-03-02 wyf �޸�ɾ���ĵ����оɺ�ͬ��Ϊ�գ��������ݴ��������
   */
  public OrderVO getSaveVO(OrderVO voOld) {

    // ֹͣ�༭���Ա�֤���������ȡ�������һ����ֵ
    stopEditing();
    /*
     * OrderVO vo = (OrderVO)
     * getBillValueChangeVO(OrderVO.class.getName(),OrderHeaderVO
     * .class.getName(),OrderItemVO.class.getName()); if(vo == null ||
     * vo.getHeadVO() == null || vo.getBodyVO() == null || vo.getBodyVO().length
     * == 0){ SCMEnv.out("δ�޸��κ���Ŀ�����������Ч"); return null; }
     */
    // ʹ������ʱ�п��ܹ�Ӧ�̻���ID��ֵ���˴�����������
    String sVendMangId = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
        "cvendormangid").getValue());
    if (sVendMangId != null
        && PuPubVO.getString_TrimZeroLenAsNull(getHeadItem("cvendorbaseid")) == null) {
      Object[] oaRet = null;
      try {
        oaRet = (Object[]) nc.ui.scm.pub.CacheTool.getCellValue("bd_cumandoc",
            "pk_cumandoc", "pk_cubasdoc", sVendMangId);
      }
      catch (Exception ee) {
        /** �����׳� */
        SCMEnv.out(ee);
      }
      String sVendBaseId = (String) oaRet[0];
      setHeadItem("cvendorbaseid", sVendBaseId);
    }

    // ģ��ǿ����ж�
    try {
      PuTool.validateNotNullField(this);
    }
    catch (Exception e) {
      PuTool.outException(this, e);
      return null;
    }

    // �õ�����VO
    int iRowCount = getRowCount();
    // �ֱ�õ���ͷ���д���ı���
    OrderHeaderVO voUIHead = (OrderHeaderVO) getBillData().getHeaderValueVO(
        "nc.vo.po.OrderHeaderVO");
    // ��ͷ��ע
    String sHeadMemo = ((UIRefPane) getHeadItem("vmemo").getComponent())
        .getUITextField().getText();
    voUIHead.setVmemo(sHeadMemo);
    // ��ͷ��Ӧ���շ�����ַ
    String sHeadAddr = ((UIRefPane) getHeadItem("cdeliveraddress")
        .getComponent()).getUITextField().getText();
    voUIHead.setCdeliveraddress(sHeadAddr);
    BillItem exchangeItem = getBillData().getBodyItem("nexchangeotobrate");
    exchangeItem.setConverter(new nc.ui.scm.pub.SCMUFDoubleConverter());
    Vector vecUIBody = new Vector();
    for (int i = 0; i < iRowCount; i++) {
      if (PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(i,
          OrderItemVO.FILTERKEY_WHENSAVE)) != null) {
        OrderItemVO voItem = (OrderItemVO) getBillModel().getBodyValueRowVO(i,
            "nc.vo.po.OrderItemVO");
        vecUIBody.addElement(voItem);
      }
    }

    OrderVO voSaved = new OrderVO();
    voSaved.setParentVO(voUIHead);
    voSaved.setChildrenVO((OrderItemVO[]) vecUIBody
        .toArray(new OrderItemVO[vecUIBody.size()]));

    // V5 ���Ӽ��
    // V56 ���ú�̨��У�飬����Զ�̵��ô���
//    try {
//      voSaved.validateCentrPur();
//    }
//    catch (ValidationException e) {
//      PuTool.outException(this, e);
//      return null;
//    }
    // ����ID
    String sOrderId = voSaved.getHeadVO().getCorderid();
    // �Ƿ��������ݣ������޸�
    boolean bNewBill = (PuPubVO.getString_TrimZeroLenAsNull(sOrderId) == null) ? true
        : false;

    // �ı�ĵ�������ԭ�������Ƚϴ���
    if (!bNewBill) {
      // ����ı���VO������
      OrderItemVO[] voaNewItem = voSaved.getBodyVO();
      int iNewLen = voaNewItem.length;
      // �õ������贫����̨����
      Vector vecAllItem = new Vector();
      for (int i = 0; i < iNewLen; i++) {
        vecAllItem.addElement(voaNewItem[i]);
      }

      // ԭ�еı���VO
      OrderItemVO[] voaOldItem = voOld.getBodyVO();
      int iOldLen = voaOldItem.length;
      // ��ԭ�е��ݱ���ѭ��
      // DELETED �����µı�����
      // UPDATED ���µı����У����µı���ı�
      // NEW �±����IDΪ��
      for (int i = 0; i < iOldLen; i++) {
        // ��ǰ��ID
        String sOldBId = voaOldItem[i].getCorder_bid();
        // �Ƿ����¶������Ծɴ���
        boolean bExisted = false;

        // Ѱ����֮ƥ�����
        for (int j = 0; j < iNewLen; j++) {
          String sNewBId = voaNewItem[j].getCorder_bid();
          if (PuPubVO.isEqual(sOldBId, sNewBId)) {
            bExisted = true;
            break;
          }
        }
        // �������б�ɾ��
        if (!bExisted) {
          OrderItemVO voDeletedCloneItem = (OrderItemVO) voaOldItem[i].clone();
          voDeletedCloneItem.setStatus(VOStatus.DELETED);
          // voaOldItem[i].setStatus(VOStatus.DELETED) ;
          vecAllItem.addElement(voDeletedCloneItem);
        }
      }
      // ���¹���VO
      OrderItemVO[] voaAllBody = new OrderItemVO[vecAllItem.size()];
      vecAllItem.copyInto(voaAllBody);
      voSaved.setChildrenVO(voaAllBody);
    }

    // -----------��VOStatus״̬
    if (bNewBill) {
      // ���ò������͡�����״̬������Ա
      voSaved.setStatus(VOStatus.NEW);
      voSaved.getHeadVO().setStatus(VOStatus.NEW);
      voSaved.getHeadVO().setForderstatus(nc.vo.scm.pu.BillStatus.FREE);
    }
    else {
      // ���ò�������
      voSaved.setStatus(VOStatus.UPDATED);
      voSaved.getHeadVO().setStatus(VOStatus.UPDATED);
      // �޸ģ���Ϊ��һ�棺�޶����޸�״̬
      if (voSaved.getHeadVO().getNversion().equals(OrderHeaderVO.NVERSION_FIRST)
          && (voSaved.getHeadVO().getForderstatus() == null || voSaved.getHeadVO().getForderstatus().intValue() != 2)) {
        voSaved.getHeadVO().setForderstatus(nc.vo.scm.pu.BillStatus.FREE);
      }
    }
    // ����
    int iBodyLen = voSaved.getBodyVO().length;
    for (int i = 0; i < iBodyLen; i++) {
      String sOrderBId = voSaved.getBodyVO()[i].getCorder_bid();
      if (PuPubVO.getString_TrimZeroLenAsNull(sOrderBId) == null) {
        voSaved.getBodyVO()[i].setStatus(VOStatus.NEW);
      }
    }

    // �����Զ�����
    OrderHeaderVO voHead = voSaved.getHeadVO();
    // String[] saVdefKey = new String[] { "vdef1", "vdef2", "vdef3",
    // "vdef4",
    // "vdef5", "vdef6", "vdef7", "vdef8", "vdef9",
    // "vdef10","vdef11","vdef12","vdef13"
    // ,"vdef14","vdef15","vdef16","vdef17","vdef18","vdef19","vdef20" };
    // int iVdefLen = saVdefKey.length;
    // for (int i = 0; i < iVdefLen; i++) {
    // JComponent component = getHeadItem(saVdefKey[i]).getComponent();
    // if (component instanceof UIRefPane) {
    // voHead.setAttributeValue(saVdefKey[i], ((UIRefPane) component)
    // .getRefName());
    // }
    // }
    // ����VO���
    String sOrderCorp = getHeadItem("pk_corp").getValue();
    // ����������
    String sExpMsg = PoChangeUI.setVONativeAndAssistCurrValue(voSaved);
    if (sExpMsg != null) {
      MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                          * @res "��ʾ"
                                                          */, sExpMsg);
      return null;
    }

    // ���ö���VO��ǰ������˾
    if (!sOrderCorp.equals(PoPublicUIClass.getLoginPk_corp())) {
      voSaved.setGroupPkCorp(PoPublicUIClass.getLoginPk_corp());

      // ȡ�õ�½��˾�뵥�ݹ�˾��Ӧ��ҵ������
      Object o = voSaved.getParentVO().getAttributeValue("cbiztype");
      String pk_busitype = (o == null ? null : o.toString());
      try {
        if (pk_busitype != null) {
          String whereStr = " businame = (select businame from bd_busitype where pk_busitype = '"
              + pk_busitype
              + "') and pk_corp = '"
              + PoPublicUIClass.getLoginPk_corp() + "'";
          Object[][] objs = PubHelper.queryResultsFromAnyTable("bd_busitype",
              new String[] {
                "pk_busitype"
              }, whereStr);
          // VO��½��˾ҵ�����͸�ֵ
          if (objs != null && objs[0] != null && objs[0][0] != null) {
            voSaved.setPkBusinessType(objs[0][0].toString());
          }
        }
      }
      catch (Exception e) {
      }
    }
    ArrayList<String> alBaseids = new ArrayList<String>();
    // ����ǲ������������ñ����Ϊ����
    iBodyLen = voSaved.getBodyVO().length;
    for (int i = 0; i < iBodyLen; i++) {
      if (voHead.getBisreplenish() != null
          && voHead.getBisreplenish().booleanValue()) {
        voSaved.getBodyVO()[i].setIisreplenish(OrderItemVO.IISREPLENISH_YES);
      }
      else {
        voSaved.getBodyVO()[i].setIisreplenish(OrderItemVO.IISREPLENISH_NO);
      }
      if(!alBaseids.contains(voSaved.getBodyVO()[i].getCbaseid())){
        alBaseids.add(voSaved.getBodyVO()[i].getCbaseid());
      }
    }

    // ���þ�VO
    if (bNewBill) {
      voSaved.setOldVO(null);
    }
    else {
      voSaved.setOldVO(voOld);
    }

    // ����VO���
    try {
      // ��֯���Ĳ���VO
      PoSaveCheckParaVO voPara = new PoSaveCheckParaVO();
      voPara.iDigit_Curr_FinanceLocal = m_cardPoPubSetUI2
          .getMoneyDigitByCurr_Finance(CurrParamQuery.getInstance()
              .getLocalCurrPK(sOrderCorp));
      validateNum(voSaved,alBaseids.toArray(new String[alBaseids.size()]));
      voSaved.validate(voPara);
    }
    catch (Exception e) {
      PuTool.outException(this, e);
      return null;
    }

    // Ϊ���������������ò���Ա��
    PoPublicUIClass.setCuserId(new OrderVO[] {
      voSaved
    });

    return voSaved;
  }
  private void validateNum(OrderVO order,String[] sbaseids ) throws BusinessException{
    int iUIBodyLen = order.getBodyLen();
    OrderItemVO[] items = order.getBodyVO();
    InvTool.loadBatchLabor(sbaseids);
    InvTool.loadBatchDiscount(sbaseids);
    for (int i = 0; i < iUIBodyLen; i++){

      if (items[i] == null){
        continue;
      }

      //���м��
      items[i].validate();

      //��������������飺�����ۿ۳���
      if (!(InvTool.isLaborNew(items[i].getCbaseid()) || InvTool.isDiscountNew(items[i].getCbaseid()))){
        //���������������븺��
        if (order.getHeadVO().getBisreplenish() != null && order.getHeadVO().getBisreplenish().booleanValue()) {
          if ( PuPubVO.getUFDouble_NullAsZero( items[i].getNordernum() ).compareTo(VariableConst.ZERO)<0 ) {
            throw new ValidationException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004020201","UPP4004020201-000175",null,new String[]{items[i].getCrowno()})/*@res "���壺�к�{0}������������������Ϊ��"*/) ;
          }
        }
        //�˻�����������
        if (order.getHeadVO().getBreturn() != null && order.getHeadVO().getBreturn().booleanValue()) {
          if ( PuPubVO.getUFDouble_NullAsZero( items[i].getNordernum() ).compareTo(VariableConst.ZERO)>0 ) {
            throw new ValidationException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004020201","UPP4004020201-000176",null,new String[]{items[i].getCrowno()})/*@res "���壺�к�{0}���˻������Ĵ�����������ۿۣ�����Ӧ�����븺��"*/);
          }
        }else{
          if ( PuPubVO.getUFDouble_NullAsZero( items[i].getNordernum() ).compareTo(VariableConst.ZERO)<0 ) {
            throw new ValidationException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004020201","UPP4004020201-000177",null,new String[]{items[i].getCrowno()})/*@res "���壺�к�{0}�����ɹ������Ĵ��(�������ۿ�)����Ӧ����������"*/) ;
          }
        }
      }
    }
  }
  /**
   * ���ߣ�WYF ���ܣ��ӿ�IBillModelSortPrepareListener ��ʵ�ַ��� ������String sItemKey ITEMKEY
   * ���أ��� ���⣺�� ���ڣ�(2004-03-24 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public int getSortTypeByBillItemKey(String sItemKey) {

    if ("crowno".equals(sItemKey) || "csourcebillrowno".equals(sItemKey)
        || "cancestorbillrowno".equals(sItemKey)) {
      return BillItem.DECIMAL;
    }

    return getBillModel().getItemByKey(sItemKey).getDataType();
  }

  private void initPara() {
    try {
      // ��ʼ������
      String para = SysInitBO_Client.getParaString(PoPublicUIClass
          .getLoginPk_corp(), "PO84");
      // ��ò���PO84,����Ƶ��˺������Ƿ�һ��
      if (para != null && para.trim().length() > 0) {
        iChange = para.equals("�����ۿ�") ? 7 : 8;
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000280")/*��ȡϵͳ��ʼ����������*/, e.getMessage());
    }
    return;
  }

  /**
   * ���ߣ����� ���ܣ���ʼ��������Ƭ �������� ���أ��� ���⣺�� ���ڣ�(2001-04-21 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void initi() {

    initPara();

    setBillBeforeEditListenerHeadTail(this);

    nc.vo.scm.pu.Timer timeDebug = new nc.vo.scm.pu.Timer();
    timeDebug.start();

    BillData bd = null;
    try {
      if (m_conInvoker instanceof PoToftPanel) {
        bd = new BillData(((PoToftPanel) m_conInvoker).getBillTempletVo());
      }
      else {
        bd = new BillData(getDefaultTemplet("4004020201", null, PoPublicUIClass
            .getLoginUser(), getCorp()));
      }

    }
    catch (java.lang.Exception e) {
      PuTool.outException(this, e);
      return;
    }
    if (bd == null) {
      return;
    }
    timeDebug.addExecutePhase("����ģ��");/* -=notranslate=- */

    // ���ñ�ͷ�����пɼ�
    initiHideItems(bd);
    timeDebug.addExecutePhase("���ñ�ͷ�����пɼ�");/* -=notranslate=- */

    // ���ñ�ͷ�������ComboBox
//    initiComboBox(bd);
    timeDebug.addExecutePhase("���ñ�ͷ�������ComboBox");/* -=notranslate=- */

    // �����Сֵ
    initiMinMaxValue(bd);
    timeDebug.addExecutePhase("�����Сֵ");/* -=notranslate=- */

    // ���ò��շ��
    initiRefPane(bd);
    timeDebug.addExecutePhase("���ò��շ��");/* -=notranslate=- */

    // ITEM�Ŀɱ༭��
    initiEnabledItems(bd);
    timeDebug.addExecutePhase("ITEM�Ŀɱ༭��");/* -=notranslate=- */

    try {
      // �Զ�����Զ�̵���������
      // ServletCallDiscription[] scdsDef = nc.ui.scm.pub.def.DefSetTool
      // .getTwoSCDs(getCorp());
      // ȫ��Զ�̵���������
      ServcallVO[] scdsAll = new ServcallVO[3];
      // scdsAll[0] = scdsDef[0];
      // scdsAll[1] = scdsDef[1];
      scdsAll[0] = new ServcallVO();
      scdsAll[0].setBeanName("nc.itf.po.IOrder");
      scdsAll[0].setMethodName("initSatus");
      scdsAll[0].setParameter(new Object[] {
        getCorp()
      });
      scdsAll[0].setParameterTypes(new Class[] {
        String.class
      });
      
      scdsAll[1] = new ServcallVO();
      scdsAll[1].setBeanName("nc.itf.uap.sf.ICreateCorpQueryService");
      scdsAll[1].setMethodName("isEnabled");
      scdsAll[1].setParameter(new Object[] {
        getCorp(),ProductCode.PROD_VRM
      });
      scdsAll[1].setParameterTypes(new Class[] {
        String.class,String.class
      });
      
      scdsAll[2] = new ServcallVO();
      scdsAll[2].setBeanName("nc.itf.uap.sf.ICreateCorpQueryService");
      scdsAll[2].setMethodName("isEnabled");
      scdsAll[2].setParameter(new Object[] {
        getCorp(),ProductCode.PROD_CT
      });
      scdsAll[2].setParameterTypes(new Class[] {
        String.class,String.class
      });
      
      // ����Զ�̵���
      Object[] oParaValue = LocalCallService.callService(scdsAll);
      if (oParaValue != null && oParaValue.length == scdsAll.length) {
        m_hmProdEnable.put(getCorp()+ScmConst.m_sModuleCodeVRM, (Boolean)oParaValue[1]);
        m_hmProdEnable.put(getCorp()+ScmConst.m_sModuleCodeCT, (Boolean)oParaValue[2]);
      }
    }
    catch (Exception e) {
      PuTool.outException(this, e);
    }

    // �����Զ�����
    nc.ui.scm.pub.def.DefSetTool.updateBillCardPanelUserDef(new BillCardPanel(
        bd), // ��ǰģ��DATA
        getCorp(), // ��˾����
        ScmConst.PO_Order, // ��������
        "vdef", // ����ģ���е���ͷ���Զ�����ǰ׺
        "vdef" // ����ģ���е�������Զ�����ǰ׺
    );
    timeDebug.addExecutePhase("�����Զ�����");/* -=notranslate=- */

    // ---------------��������
    setBillData(bd);
    timeDebug.addExecutePhase("��������");/* -=notranslate=- */

    // ---------------�Ƕ�MODEL�������õı�����ں���
    // ��ʼ��ҵ����������
    // initiStatus();
    // timeDebug.addExecutePhase("��ʼ��ҵ����������");

    // �кŵ�����
    BillRowNo.loadRowNoItem(this, "crowno");
    timeDebug.addExecutePhase("�кŵ�����");/* -=notranslate=- */

    // ��������Ŀ�����
    getBodyPanel().addTableSortListener();
    getBillModel().setRowSort(true);
    timeDebug.addExecutePhase("��������Ŀ�����");/* -=notranslate=- */

    // ǧ��λ��ʾ
    setBodyShowThMark(true);
    // �Ƿ���ʾ�ϼ���
    setTatolRowShow(true);
    // ��ʼ�������շ�����ַ��ʾ���
    // setColType( getCorp() );
    // ����
    PuTool.setTranslateRender(this);

    // since v55 , ֧�ֱ��嵯���˵��м��롰�����кš�
    if (getContainer() instanceof OrderUI
        || getContainer() instanceof ReplenishUI) {
      m_mapReSortRowNo = BillTools.addReSortRowNoToPopMenuMultiTabs(this, null);
    }
    if (getContainer() instanceof OrderUI) {
      m_mapCardEdit = BillTools.addMultiTableBodyMenuItem(this,
          new ButtonObject(NCLangRes.getInstance().getStrByID("common",
              "SCMCOMMON000000267")/* @res"��Ƭ�༭" */), null);
    }

    // ����
    initiListener();

    // ---------------���ý���ɱ༭��
    setEnabled(false);
    timeDebug.addExecutePhase("����");/* -=notranslate=- */

    // V55��֧������ѡ��
    PuTool.setLineSelected(this);

    //since v56, ���ò��������ĵ���Ŀ(ԭ���ǣ�������Ҫ����Ӧ�á������Լ���������Ϊ Ŀǰ������������֧�ֲ�������)
    PuTool.setBatchModifyForbid(this, new String[]{"vfree"});
    
    timeDebug.showAllExecutePhase("������Ƭ����");/* -=notranslate=- */
  }

  /**
   * ���ñ�ͷ�������е�ComboBox
   * 
   * @param
   * @return
   * @exception
   * @see
   * @since 2001-05-20
   */
  private void initiComboBox(BillData bd) {

    // ��ͷ��˰���
    UIComboBox cbbHType = (UIComboBox) (bd.getHeadItem("idiscounttaxtype")
        .getComponent());
    cbbHType.setFont(this.getFont());
    cbbHType.setBackground(this.getBackground());
    cbbHType.addItem(VariableConst.IDISCOUNTTAXTYPE_NAME_INNER);
    cbbHType.addItem(VariableConst.IDISCOUNTTAXTYPE_NAME_OUTTER);
    cbbHType.addItem(VariableConst.IDISCOUNTTAXTYPE_NAME_NOCOUNT);
    cbbHType.setSelectedIndex(1);

    cbbHType.setTranslate(true);

    bd.getHeadItem("idiscounttaxtype").setWithIndex(true);
    // �����˰���
    UIComboBox cbbBType = null;
    cbbBType = (UIComboBox) (bd.getBodyItem("idiscounttaxtype").getComponent());
    cbbBType.setFont(this.getFont());
    cbbBType.setBackground(this.getBackground());

    cbbBType.addItem(VariableConst.IDISCOUNTTAXTYPE_NAME_INNER);
    cbbBType.addItem(VariableConst.IDISCOUNTTAXTYPE_NAME_OUTTER);
    cbbBType.addItem(VariableConst.IDISCOUNTTAXTYPE_NAME_NOCOUNT);
    cbbBType.setSelectedIndex(1);

    cbbBType.setTranslate(true);

    bd.getBodyItem("idiscounttaxtype").setWithIndex(true);
  }

  /**
   * ���ߣ�WYF ���ܣ����ò��ɱ༭���ֶ� �������� ���أ��� ���⣺�� ���ڣ�(2003-10-08 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void initiEnabledItems(BillData bd) {

    // ��ͷ��ҵ�����Ͳ��ܱ༭
    bd.getHeadItem("cbiztype").setEnabled(false);
  }

  /**
   * ���ߣ����� ���ܣ����ñ�ͷ�еĿɼ��� ������ boolean true �Ƿ�ɼ� ���أ��� ���⣺�� ���ڣ�(2002-3-13 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void initiHideItems(BillData bd) {
    if (bd.getHeadItem("pk_corp") != null) {
      bd.getHeadItem("pk_corp").setShow(true);
    }
    if (bd.getHeadItem("cbiztype") != null) {
      bd.getHeadItem("cbiztype").setShow(true);
    }
    if (bd.getHeadItem("cpurorganization") != null) {
      bd.getHeadItem("cpurorganization").setShow(true);
    }
    // bd.getHeadItem("cstoreorganization").setShow(true);
    if (bd.getHeadItem("cvendormangid") != null) {
      bd.getHeadItem("cvendormangid").setShow(true);
    }
    // bd.getHeadItem("cfreecustid").setShow(true);
    // bd.getHeadItem("caccountbankid").setShow(true);
    // bd.getHeadItem("cdeliveraddress").setShow(true);
    if (bd.getHeadItem("cemployeeid") != null) {
      bd.getHeadItem("cemployeeid").setShow(true);
    }
    if (bd.getHeadItem("cdeptid") != null) {
      bd.getHeadItem("cdeptid").setShow(true);
    }
    // bd.getHeadItem("creciever").setShow(true);
    // bd.getHeadItem("cgiveinvoicevendor").setShow(true);
    // bd.getHeadItem("ctransmodeid").setShow(true);
    // bd.getHeadItem("ctermprotocolid").setShow(true);
  }

  /**
   * ���ߣ���ӡ�� ���ܣ����ӿ�Ƭ������� �������� ���أ��� ���⣺�� ���ڣ�(2002-4-22 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-07-22 wyf ����������༭ǰ���� 2002-08-20 wyf �������κż���
   * 2002-09-17 ljq ȥ�����κż��� 2002-09-18 wyf ����ֿ����
   */
  private void initiListener() {
    // since v55,���ӺϼƼ���
    addBodyTotalListener(this);
    // ���ݼӼ���
    addEditListener(this);
    addBodyEditListener2(this);
    addBodyMenuListener(this);
    getBodyTabbedPane().addChangeListener(this);
    // HEAD
    ((UIRefPane) getHeadItem("cemployeeid").getComponent()).getUIButton()
        .addActionListener(this);
    //
    ((UIRefPane) getHeadItem("cdeliveraddress").getComponent()).getUIButton()
        .addActionListener(this);
    // ҵ������
    ((UIRefPane) getHeadItem("cbiztype").getComponent()).getUIButton()
        .addActionListener(this);
    // ��������
    ((UIRefPane) getHeadItem("caccountbankid").getComponent()).getUIButton()
        .addActionListener(this);
    ((UIRefPane) getHeadItem("cprojectid").getComponent()).getUITextField()
        .addActionListener(this);
    // �к��������
    getBillModel().setSortPrepareListener(this);
    // ������¼�
    getBillModel().addSortListener(this);
    // �����Ҽ������˵�
    UIMenuItem[] items = getMiReSortRowNos();
    UIMenuItem[] cardeditItems = getCardEditMenu();
    if (items != null && items.length > 0) {
      for (int i = 0; i < items.length; i++) {
        if (items[i] == null) {
          continue;
        }
        items[i].addActionListener(new IMenuItemListener());
      }
    }
    // �Ҽ���Ƭ�༭����
    if (cardeditItems != null && cardeditItems.length > 0) {
      for (int i = 0; i < cardeditItems.length; i++) {
        if (cardeditItems[i] == null) {
          continue;
        }
        cardeditItems[i].addActionListener(new IMenuItemListener());
      }
    }
    /** *****��ÿ��ҳǩ���ӱ���˵�����****** */
    addActionListener(this);
    addActionListener("table_arr", this);
    addActionListener("table_pol", this);
    addActionListener("table_exe", this);
    /** ******************************** */
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�����С���Ŀؼ���������������Сֵ �������� ���أ��� ���⣺�� ���ڣ�(2002-8-26 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void initiMinMaxValue(BillData bd) {

    // ======================��ͷ
    // ˰�� �۱����� �۸�����
    String[] saHeadItem = new String[] {
        "ntaxrate", "nexchangeotobrate", "nprepaymny", "nprepaymaxmny"
    };
    int iHeadLen = saHeadItem.length;
    for (int i = 0; i < iHeadLen; i++) {
      BillItem hItem = bd.getHeadItem(saHeadItem[i]);
      if (hItem != null) {
        // �﷫�Ĳ�����ϣ��˰������������
        if (!(hItem.getComponent() instanceof UIRefPane)) {
          continue;
        }
        UIRefPane refPanel = (UIRefPane) hItem.getComponent();
        UITextField textField = (UITextField) refPanel.getUITextField();
        textField.setMinValue(0.0);
      }
    }

    // ======================����
    // ˰�� �۱����� �۸����� ���� ��˰���� ��˰����
    String[] saBodyItem = new String[] {
        "ntaxrate", "nexchangeotobrate", "noriginalcurprice",
        "noriginalnetprice", "norgtaxprice", "norgnettaxprice"
    };
    int iBodyLen = saBodyItem.length;
    for (int i = 0; i < iBodyLen; i++) {
      BillItem bItem = bd.getBodyItem(saBodyItem[i]);
      if (bItem != null) {
        UIRefPane refPanel = (UIRefPane) bItem.getComponent();
        UITextField textField = (UITextField) refPanel.getUITextField();
        textField.setMinValue(0.0);
      }
    }

    return;

  }

  /**
   * ���ߣ����� ���ܣ����ò��յĿ����ԡ� �������� ���أ��� ���⣺�� ���ڣ�(2001-10-20 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void initiRefPane(BillData bd) {
    try {
      // -------------------��ͷ
      // ��ע
      ((UIRefPane) bd.getHeadItem("vmemo").getComponent()).setReturnCode(false);
      ((UIRefPane) bd.getHeadItem("vmemo").getComponent()).setAutoCheck(false);
      // ����
      ((UIRefPane) bd.getHeadItem("caccountbankid").getComponent()).setReturnCode(true);
      // ��Ӧ���շ�����ַ
      ((UIRefPane) bd.getHeadItem("cdeliveraddress").getComponent()).setReturnCode(false);
      ((UIRefPane) bd.getHeadItem("cdeliveraddress").getComponent()).setAutoCheck(false);
      ((UIRefPane) bd.getHeadItem("cdeliveraddress").getComponent()).setButtonVisible(true);
      // ɢ������ɢ���Ƶ�PUB�����ֱ������ WYF
      ((UIRefPane) bd.getHeadItem("cfreecustid").getComponent()).getRef().setRefUI(new UFRefGridUI(this));

      // -------------------����
      // ������
      FreeItemRefPane firpFreeItemRefPane = new FreeItemRefPane();
      firpFreeItemRefPane.setMaxLength(bd.getBodyItem("vfree").getLength());
      bd.getBodyItem("vfree").setComponent(firpFreeItemRefPane);
      // ���κ�
      if (bd.getBodyItem("vproducenum").isShow()) {
        LotNumbRefPane lotRef = new LotNumbRefPane();
        if (lotRef != null) {
          lotRef.setMaxLength(bd.getBodyItem("vproducenum").getLength());
        }
        bd.getBodyItem("vproducenum").setComponent((JComponent) lotRef);
      }
      // ������
      ((UIRefPane) bd.getBodyItem("cassistunitname").getComponent()).setReturnCode(true);
      ((UIRefPane) bd.getBodyItem("cassistunitname").getComponent()).setRefInputType(1);
      ((UIRefPane) bd.getBodyItem("cassistunitname").getComponent()).setCacheEnabled(true);
      // ��ע
      UIRefPane paneBodyMemo = ((UIRefPane) bd.getBodyItem("vmemo").getComponent());
      paneBodyMemo.setButtonVisible(true);
      paneBodyMemo.setReturnCode(false);
      paneBodyMemo.setAutoCheck(false);
      paneBodyMemo.setRefInputType(1);
      // �շ�����ַ
      UIRefPane paneBodyAddr = ((UIRefPane) bd.getBodyItem("vreceiveaddress").getComponent());
      paneBodyAddr.setRefModel(new PoReceiveAddrRefModel(getCorp(), null));
      paneBodyAddr.setButtonVisible(true);
      paneBodyAddr.setReturnCode(false);
      paneBodyAddr.setAutoCheck(false);
      paneBodyAddr.setRefInputType(1);
      // ��Ӧ���շ�����ַ
      UIRefPane paneBodyAddrVendor = ((UIRefPane) bd.getBodyItem("vvenddevaddr").getComponent());
      paneBodyAddrVendor.setRefModel(new PoReceiveAddrRefModel(getCorp(), null));
      paneBodyAddrVendor.setButtonVisible(true);
      paneBodyAddrVendor.setReturnCode(false);
      paneBodyAddrVendor.setAutoCheck(false);
      paneBodyAddrVendor.setRefInputType(1);
      // �ֿ�
      UIRefPane paneBodyStor = ((UIRefPane) bd.getBodyItem("cwarehouse").getComponent());
      paneBodyStor.setRefModel(new WarehouseRefModel(getCorp()));

      // ����ֿ�
      UIRefPane paneReqStor = ((UIRefPane) bd.getBodyItem("reqwarename").getComponent());
      paneReqStor.setRefModel(new WarehouseRefModel(getCorp()));
      paneReqStor.getRefModel().addWherePart(
              " and UPPER(bd_stordoc.gubflag) <> 'Y' and UPPER(bd_stordoc.sealflag) <> 'Y' ");

    }
    catch (Exception e) {
      PuTool.outException(e);
    }
  }

  /**
   * ���ߣ�WYF ���ܣ��޶���ɺ���պ���������Ϣ �������� ���أ��� ���⣺�� ���ڣ�(2003-11-11 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private boolean isHaveAfterBill(String sOrderId) {

    if (PuPubVO.getString_TrimZeroLenAsNull(sOrderId) == null) {
      return true;
    }
    Object oValue = h_mapAfterBill.get(sOrderId);

    if (oValue == null || oValue instanceof String) {
      return false;
    }

    return true;

  }

  /**
   * ���ߣ���ӡ�� ���ܣ����޶������޸� �������� ���أ�boolean �޶�����true���޸ķ���false ���⣺�� ���ڣ�(2004-5-27
   * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private boolean isRevise() {

    UFDouble dVersion = PuPubVO.getUFDouble_NullAsZero(getHeadItem("nversion")
        .getValue());
    Integer iVersion = dVersion.intValue() == 0 ? OrderHeaderVO.NVERSION_FIRST
        : new Integer(dVersion.intValue());
    return !(iVersion.compareTo(OrderHeaderVO.NVERSION_FIRST) == 0);
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��õ�����ĳ���Ƿ�ر� ������int iRow ָ���� ���أ�boolean ���з���true������Ϊfalse ���⣺
   * ���ڣ�(2004-5-27 13:24:16) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private boolean isRowActive(int iRow) {

    Integer iActive = PuPubVO.getInteger_NullAs(getBodyValueAt(iRow,
        "iisactive"), OrderItemVO.IISACTIVE_ACTIVE);

    return iActive.compareTo(OrderItemVO.IISACTIVE_ACTIVE) == 0;
  }

  /**
   * ���ߣ�WYF ���ܣ��޶���ɺ���պ���������Ϣ �������� ���أ��� ���⣺�� ���ڣ�(2003-11-11 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private boolean isRowHaveAfterBill(int iRow) {
    String sHId = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(iRow,
        "corderid"));
    String sBId = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(iRow,
        "corder_bid"));

    if (sHId == null || sBId == null) {
      return false;
    }

    if (isHaveAfterBill(sHId)) {
      Object oValue = h_mapAfterBill.get(sHId);
      OrderAfterBillVO voAfter = (OrderAfterBillVO) oValue;
      return voAfter.isHaveAfterBill(sBId) ? true : false;
    }
    return false;
  }

  /**
   * ���ߣ�WYF ���ܣ��޶���ɺ���պ���������Ϣ �������� ���أ��� ���⣺�� ���ڣ�(2003-11-11 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public boolean loadAfterBillInfo(OrderVO voOrder) {

    if (h_mapAfterBill.containsKey(voOrder.getHeadVO().getCorderid())) {
      return true;
    }

    OrderAfterBillVO voAfterBill = null;
    try {
      voAfterBill = OrderAfterBillHelper.queryAnyAfterBill(voOrder);
    }
    catch (Exception e) {
      PuTool.outException(this, e);
      return false;
    }

    if (voAfterBill == null) {
      h_mapAfterBill.put(voOrder.getHeadVO().getCorderid(), NO_AFTERBILL);
    }
    else {
      h_mapAfterBill.put(voOrder.getHeadVO().getCorderid(), voAfterBill);
    }

    return true;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�����һ�е����� �������� ���أ��� ���⣺�� ���ڣ�(2001-4-8 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public void onActionAppendLine() {

    onActionAppendLine(1,-1);
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�����N�е����� ������int iAppendCount ���ӵ����� ���أ��� ���⣺�� ���ڣ�(2001-4-8
   * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * �����Ҫ���ú�ͬ��ص���Ϣ����ôiSourceRowNoΪԴ�кţ�����iSourceRowNo = -1 since 5.6 modify by donggq
   */
  public void onActionAppendLine(int iAppendCount,int iSourceRowNo) {

    // showHintMessage( CommonConstant.SPACE_MARK + "���Ӷ�����" +
    // CommonConstant.SPACE_MARK ) ;

    if (iAppendCount <= 0) {
      return;
    }
    int iRow = getBillModel().getRowCount() - 1;
    // ����
//    if (iAppendCount == 1) {
//      getBillModel().addLine();
//    }
//    else {
    addOneMore = true;
    getBodyPanel().addLine(iAppendCount);
//    }

    // �账�����
    int[] iaRow = PuTool.getRowsAfterMultiSelect(iRow, iAppendCount + 1);

    // ��ʼ��������
    int iLen = iaRow.length;
    int iBeginRow = iaRow[1];
    int iEndRow = iaRow[iLen - 1];
    boolean isWaitSetAll = iAppendCount > 1;
    for (int i = iBeginRow; i <= iEndRow; i++) {
      // ���������е�Ĭ��ֵ
      setDefaultBody(i,iSourceRowNo,true);
      // ��ͬ�Ų��ɱ༭
      setCellEditable(i, "ccontractcode", false);
    }
    if(isWaitSetAll){
      String[] falmulars =  getBodyItem("ccurrencytype").getEditFormulas();
      ArrayList<String> alFomulars = new ArrayList<String>();
      if(falmulars != null ){
        for (String fomular : falmulars) {
          alFomulars.add(fomular);
        }
      }
      alFomulars.add( "coperator->getColValue(sm_user,cUserid,cUserId,coperator)" );
      alFomulars.add("coperatorname->getColValue(sm_user,user_name,cUserId,coperator)");
      getBillModel().execFormulas(alFomulars.toArray(new String[alFomulars.size()]));
    }else{
      getBillModel().execEditFormulaByKey(-1, "ccurrencytype"); // V31�Ż���ѭ������
    }
    // �����к�
    BillRowNo.addLineRowNos(this, BillTypeConst.PO_ORDER, "crowno",
        iAppendCount);

    // ������Ŀ���Զ�Я��
    PuTool.setBodyProjectByHeadProject(this, "cprojectid", "cprojectid",
        "cproject", iBeginRow, iEndRow);

    // ��ע
//    setDefaultValueToVmemo();
    // ���ñ��塰�շ�����ַ���������ص㡱�͡���Ӧ���շ�����ַ���������ص㡱Ĭ��ֵ
    setDefaultValueWhenAppInsLines(iBeginRow, iEndRow);

  }

  /**
   * ��ʼ�������˰���,�������ʱ�����˰����԰�������һ�α༭�Ŀ�˰����δ����ͷ��˰������
   */
  private void initDisBeforeEdit(int iRow) {
    BillItem bi = getBillModel().getBodyItems()[getBillModel().getBodyColByKey(
        "idiscounttaxtype")];
    UIComboBox cmbBody = (UIComboBox) bi.getComponent();
    int iRealIndex = cmbBody.getItemIndexByValue(getBodyValueAt(iRow,
        "idiscounttaxtype"));
    cmbBody.setSelectedIndex(iRealIndex);
    // BillEditEvent eventInit = new BillEditEvent(
    // cmbBody,
    // getBodyValueAt(iRow,"idiscounttaxtype"),
    // "idiscounttaxtype",
    // iRow,
    // BillItem.BODY
    // );
    // afterEdit(eventInit);
  }

  /**
   * ���ñ��塰�շ�����ַ���������ص㡱�͡���Ӧ���շ�����ַ���������ص㡱Ĭ��ֵ
   */
  private void setDefaultValueWhenAppInsLines(int iBeginRow, int iEndRow) {
    // ���շ�����ַ��
    for (int i = iBeginRow; i <= iEndRow; i++) {
      afterEditWhenBodyArrWare(new BillEditEvent(getBodyItem("cwarehouse")
          .getComponent(), null, "cwarehouse", i, BillItem.HEAD));
    }
    // �������շ�����ַ��
    String strVendorId = getHeadItem("cvendorbaseid").getValue();
    setDefaultValueToBodyVendorAddr(strVendorId, iBeginRow, iEndRow);
    // ��ַ����ȡ��ͷ�������շ�����ַ��
    String strVendorAddr = getHeadItem("cdeliveraddress").getValue();
    strVendorAddr = PuPubVO.getString_TrimZeroLenAsNull(strVendorAddr);
    if (strVendorAddr != null) {
      for (int i = iBeginRow; i <= iEndRow; i++) {
        getBillModel().setValueAt(strVendorAddr, i, "vvenddevaddr");
      }
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ����Ƶ�ǰ���� �������� ���أ��� ���⣺�� ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 20050407 czp �Ż���1����ͷ�������ñ�Ҫֵ(��Ĭ��ֵ����ֵ����ֵ)��2��ȥ����ͷ���幫ʽִ��
   */
  public void onActionCopyBill(OrderVO voOld) {
    //

    // �رպϼƿ���
    boolean bOldNeedCalc = getBillModel().isNeedCalculate();
    getBillModel().setNeedCalculate(false);

    nc.vo.scm.pu.Timer timeDebug = new nc.vo.scm.pu.Timer();
    timeDebug.start();

    // ����׼��
    OrderVO voNew = (OrderVO) voOld.clone();

    // -------------��ͷ
    OrderHeaderVO voHead = (OrderHeaderVO) voNew.getParentVO();

    // Эͬ��ʱ����һ��һ��,���ԣ������Эͬ�����ĵ��ݣ�����Ҫ�����ϲ���Դ��Ϣ��
    UFBoolean ufbFromSOFlag = voHead.getBsocooptome();
    if (ufbFromSOFlag == null) {
      ufbFromSOFlag = UFBoolean.FALSE;
    }
    voHead.setBcooptoso(OrderHeaderVO.BISREPLENISH_NO);
    getHeadItem("bsocooptome").setValue(OrderHeaderVO.BISREPLENISH_NO);

    // ��������������ƣ���beforEditBodyInventory()��
    m_strCbiztype = voHead.getCbiztype();
    // ��������
    voHead.setPrimaryKey(null);
    getHeadItem("corderid").setValue(null);
    // ��������
    voHead.setVordercode(null);
    getHeadItem("vordercode").setValue(null);
    // ������
    voHead.setCauditpsn(null);
    getTailItem("cauditpsn").setValue(null);
    // ��������
    voHead.setDauditdate(null);
    getTailItem("dauditdate").setValue(null);
    // �޶�����
    voHead.setDrevisiondate(null);
    getHeadItem("drevisiondate").setValue(null);
    // Ԥ�����޶�
    voHead.setNprepaymaxmny(null);
    getHeadItem("nprepaymaxmny").setValue(null);
    // Ԥ����
    voHead.setNprepaymny(null);
    getHeadItem("nprepaymny").setValue(null);
    // Эͬ��־
    voHead.setBcooptoso(UFBoolean.FALSE);
    getHeadItem("bcooptoso").setValue(null);
    voHead.setBsocooptome(UFBoolean.FALSE);
    getHeadItem("bsocooptome").setValue(null);
    voHead.setCoopsohid(null);
    getHeadItem("vcoopordercode").setValue(null);

    // ��������
    voHead.setDorderdate(PoPublicUIClass.getLoginDate());
    getHeadItem("dorderdate").setValue(PoPublicUIClass.getLoginDate());
    // ��������(������Դ����)--�������û�� �����м���
    voHead.setDorderdateOld(voOld.getHeadVO().getDorderdate());
    // ����״̬
    voHead.setForderstatus(nc.vo.scm.pu.BillStatus.FREE);
    getHeadItem("forderstatus").setValue(nc.vo.scm.pu.BillStatus.FREE);
    // �Ƶ���
    voHead.setCoperator(PoPublicUIClass.getLoginUser());
    getTailItem("coperator").setValue(PoPublicUIClass.getLoginUser());
    // ������
    voHead.setCaccountyear(ClientEnvironment.getInstance().getAccountYear());
    getHeadItem("caccountyear").setValue(
        ClientEnvironment.getInstance().getAccountYear());
    // TS
    voHead.setTs(null);
    getHeadItem("ts").setValue(null);
    // �Ƶ�ʱ��
    voHead.setTmaketime(null);
    getTailItem("tmaketime").setValue(null);
    // ����޸�ʱ��
    voHead.setTlastmaketime(null);
    getTailItem("tlastmaketime").setValue(null);
    // �޶���
    voHead.setCrevisepsn(null);
    getTailItem("crevisepsn").setValue(null);
    // �޶�ʱ��
    voHead.setTrevisiontime(null);
    getTailItem("trevisiontime").setValue(null);
    // ����ʱ��
    voHead.setTaudittime(null);
    getTailItem("taudittime").setValue(null);
    // �汾
    voHead.setNversion(OrderHeaderVO.NVERSION_FIRST);
    getHeadItem("nversion").setValue(OrderHeaderVO.NVERSION_FIRST);
    // �Ƿ����°汾
    voHead.setBislatest(OrderHeaderVO.BISLATEST_YES);
    getHeadItem("bislatest").setValue(OrderHeaderVO.BISLATEST_YES);
    // �Ƿ񲹻�������ֻ�ܲ��� 20050407 XY
    voHead.setBisreplenish(OrderHeaderVO.BISREPLENISH_NO);
    getHeadItem("bisreplenish").setValue(OrderHeaderVO.BISREPLENISH_NO);
    // ��ӡ����
    voHead.setIprintcount(null);
    if (getTailItem("iprintcount") != null) {
      getTailItem("iprintcount").setValue(null);
    }
    // since v50 ֧��Ȩ��
    ((UIRefPane) getHeadItem("cvendormangid").getComponent()).getRefModel()
        .setisRefEnable(true);
    getHeadItem("cvendormangid").setValue(voHead.getCvendormangid());
    if (((UIRefPane) getHeadItem("cvendormangid").getComponent()).getRefPK() == null) {
      voHead.setCvendormangid(null);
      voHead.setCvendorbaseid(null);
      getHeadItem("cvendorbaseid").setValue(null);
      BillEditEvent bee = new BillEditEvent(getHeadItem("cvendormangid")
          .getComponent(), "", "cvendormangid", -1, BillItem.HEAD);
      afterEditWhenHeadVendor(bee);
    }

    // -------------����
    OrderItemVO[] voaItem = voNew.getBodyVO();
    int iLen = voaItem.length;

    // �ƻ�������������׼��
    UFDate[] dplandates = new UFDate[iLen];
    try {
      dplandates = PoPublicUIClass.getDPlanArrvDateArray(
          voHead.getPk_corp(),
          // ---V5 modify:------
          // voHead.getCstoreorganization(),
          (String[]) OrderPubVO.getFieldArrayIncludeNull(new OrderVO[] {
            voOld
          }, OrderItemVO.class, "pk_arrvstoorg", String.class),
          // -------------------
          voHead.getDorderdate(), (String[]) OrderPubVO
              .getFieldArrayIncludeNull(new OrderVO[] {
                voOld
              }, OrderItemVO.class, "cbaseid", String.class),
          (UFDouble[]) OrderPubVO.getFieldArrayIncludeNull(new OrderVO[] {
            voOld
          }, OrderItemVO.class, "nordernum", UFDouble.class));
      if (dplandates == null || dplandates.length != iLen) {
        dplandates = new UFDate[iLen];
        SCMEnv.out("����BUG�������������ݳ��ֳ��Ȳ�ƥ��");
      }
    }
    catch (Exception e) {
      dplandates = new UFDate[iLen];
      PuTool.outException(this, e);
      SCMEnv.out("����ƻ��������ڳ�����Ӱ������");
    }

    timeDebug.addExecutePhase("�ƻ�������������׼��");/* -=notranslate=- */
    // �����ֶθ�ֵ
    for (int i = 0; i < voaItem.length; i++) {
      // ������ر�
      voaItem[i].setBtransclosed(UFBoolean.FALSE);
      setBodyValueAt(UFBoolean.FALSE, i, "btransclosed");
      // �ƻ���������
      voaItem[i].setDplanarrvdate(dplandates[i]);
      setBodyValueAt(dplandates[i], i, "dplanarrvdate");

      // ��������
      voaItem[i].setCorder_bid(null);
      setBodyValueAt(null, i, "corder_bid");
      // ��ͷ����
      voaItem[i].setCorderid(null);
      setBodyValueAt(null, i, "corderid");

      // �ۼƵ�������
      voaItem[i].setNaccumarrvnum(null);
      setBodyValueAt(null, i, "naccumarrvnum");
      // �ۼƷ�Ʊ����
      voaItem[i].setNaccuminvoicenum(null);
      setBodyValueAt(null, i, "naccuminvoicenum");
      // �ۼƵ����ƻ�����
      voaItem[i].setNaccumrpnum(null);
      setBodyValueAt(null, i, "naccumrpnum");
      // �ۼ��������
      voaItem[i].setNaccumstorenum(null);
      setBodyValueAt(null, i, "naccumstorenum");
      // �ۼ�;������
      voaItem[i].setNaccumwastnum(null);
      setBodyValueAt(null, i, "naccumwastnum");
      // �ۼ��˻�����
      voaItem[i].setNbackarrvnum(null);
      setBodyValueAt(null, i, "nbackarrvnum");
      // �ۼ��˿�����
      voaItem[i].setNbackstorenum(null);
      setBodyValueAt(null, i, "nbackstorenum");
      // ���ۼ���������
      voaItem[i].setNaccumdevnum(null);
      setBodyValueAt(null, i, "naccumdevnum");
      // ȷ������
      voaItem[i].setNconfirmnum(null);
      setBodyValueAt(null, i, "nconfirmnum");
      // ȷ������
      voaItem[i].setDconfirmdate(null);
      setBodyValueAt(null, i, "dconfirmdate");
      // ��������
      voaItem[i].setDcorrectdate(null);
      setBodyValueAt(null, i, "dcorrectdate");
      // �Է�������
      voaItem[i].setVvendorordercode(null);
      setBodyValueAt(null, i, "vvendorordercode");
      // �Է������к�
      voaItem[i].setVvendororderrow(null);
      setBodyValueAt(null, i, "vvendororderrow");
      // ����Ա
      voaItem[i].setCoperator(null);
      setBodyValueAt(null, i, "coperator");
      // ������
      voaItem[i].setCcorrectrowid(null);
      setBodyValueAt(null, i, "ccorrectrowid");

      /*
       * V31 �������������Ʊ�����Դ //��Դ����ID voaItem[i].setCsourcebillid(null); //��Դ��������
       * voaItem[i].setCsourcebilltype(null); //��Դ������ID
       * voaItem[i].setCsourcerowid(null); //�ϲ���Դ����ID
       * voaItem[i].setCupsourcebillid(null); //�ϲ���Դ������ID
       * voaItem[i].setCupsourcebillrowid(null); //�ϲ���Դ��������
       * voaItem[i].setCupsourcebilltype(null);
       */
      voaItem[i].setCsourcebilltype(null); // ��Դ������ID
      voaItem[i].setCsourcerowid(null); // �ϲ���Դ����ID
      voaItem[i].setCupsourcebillid(null); // �ϲ���Դ������ID
      voaItem[i].setCupsourcebillrowid(null); // �ϲ���Դ��������
      voaItem[i].setCupsourcebilltype(null);
      // since v55, ������μ۸������������Ϣ

      // �۸���������
      voaItem[i].setVpriceauditcode(null);
      setBodyValueAt(null, i, "vpriceauditcode");
      // �۸�������id
      voaItem[i].setCpriceauditid(null);
      setBodyValueAt(null, i, "cpriceauditid");
      // �۸�����������ӱ�id
      voaItem[i].setCpriceaudit_bid(null);
      setBodyValueAt(null, i, "cpriceaudit_bid");
      // �۸�������������ӱ�id
      voaItem[i].setCpriceaudit_bb1id(null);
      setBodyValueAt(null, i, "cpriceaudit_bb1id");

      // since v55, ������κ�ͬ�����Ϣ

      // ��ͬid
      voaItem[i].setCcontractid(null);
      setBodyValueAt(null, i, "ccontractid");
      // ��ͬ��id
      voaItem[i].setccontractrowid(null);
      setBodyValueAt(null, i, "ccontractrowid");

      // �����ĸ���Ҫ�����Դ
      // since v55,Эͬ��ʱ����һ��һ��,���ԣ������Эͬ�����ĵ��ݣ�����Ҫ�����ϲ���Դ��Ϣ
      // if (voOld.isReplenish() || ufbFromSOFlag.booleanValue()) {
      // ��Դ����ID
      voaItem[i].setCsourcebillid(null);
      setBodyValueAt(null, i, "csourcebillid");
      // ��Դ��������
      voaItem[i].setCsourcebilltype(null);
      setBodyValueAt(null, i, "csourcebilltype");
      // ��Դ������ID
      voaItem[i].setCsourcerowid(null);
      setBodyValueAt(null, i, "csourcerowid");
      // �ϲ���Դ����ID
      voaItem[i].setCupsourcebillid(null);
      setBodyValueAt(null, i, "cupsourcebillid");
      // �ϲ���Դ������ID
      voaItem[i].setCupsourcebillrowid(null);
      setBodyValueAt(null, i, "cupsourcebillrowid");
      // �ϲ���Դ��������
      voaItem[i].setCupsourcebilltype(null);
      setBodyValueAt(null, i, "cupsourcebilltype");

      setBodyValueAt(null, i, "csourcebillname");
      setBodyValueAt(null, i, "csourcebillcode");
      setBodyValueAt(null, i, "csourcebillrowno");
      setBodyValueAt(null, i, "cancestorbillname");
      setBodyValueAt(null, i, "cancestorbillcode");
      setBodyValueAt(null, i, "cancestorbillrowno");
      // }
      // TS
      voaItem[i].setTs(null);
      setBodyValueAt(null, i, "ts");

      // ����Ч�ʿ��ǣ�����ʱ����ÿ�{������Ч�����Ǹ�����Ĵ����������ݿ�����ʱ���}
      voaItem[i].setCupsourcehts(null);
      setBodyValueAt(null, i, "cupsourcehts");
      voaItem[i].setCupsourcebts(null);
//      setBodyValueAt(null, i, "upsourcebts");

      // �����ֶ���ȱʡֵ
      voaItem[i].setForderrowstatus(OrderItemVO.FORDERROWSTATUS_FREE);
      setBodyValueAt(OrderItemVO.FORDERROWSTATUS_FREE, i, "forderrowstatus");
      voaItem[i].setIisactive(OrderItemVO.IISACTIVE_ACTIVE);
      setBodyValueAt(OrderItemVO.IISACTIVE_ACTIVE, i, "iisactive");
      voaItem[i].setIisreplenish(OrderItemVO.IISREPLENISH_NO);
      setBodyValueAt(OrderItemVO.IISREPLENISH_NO, i, "iisreplenish");

      // V3����
      voaItem[i].setBreceiveplan(OrderItemVO.BRECEIVEPLAN_NO);
      setBodyValueAt(OrderItemVO.BRECEIVEPLAN_NO, i, "breceiveplan");

      voaItem[i].setCCloseUserId(null);
      voaItem[i].setDCloseDate(null);

      setBodyValueAt(null, i, "ccloseuserid");
      setBodyValueAt(null, i, "ccloseusername");
      setBodyValueAt(null, i, "dclosedate");
      setBodyValueAt(null, i, "bcloserow");
      // -----------V31����

      // �ۼ������ռƻ�����
      // voaItem[i].setNaccumdayplnum(null);
      // setBodyValueAt(null,i,"naccumdayplnum");
      // �ۼƷ�������
      voaItem[i].setNaccumdevnum(null);
      setBodyValueAt(null, i, "naccumdevnum");
      // �ۼƷ��ý��
      setBodyValueAt(null, i, "nfeemny");
    }

    timeDebug.addExecutePhase("����Ĭ��ֵ");/* -=notranslate=- */

    // �к�(�����кŲ���Ҫ������Ѱ�Һ�֮ͬǰ����Ϊ�õ��к�)
    BillRowNo.setVORowNoByRule(voNew, BillTypeConst.PO_ORDER, "crowno");
    timeDebug.addExecutePhase("�к�");/* -=notranslate=- */

   
//    PoChangeUI.setCloneVOCntRelated(this, voNew);
    timeDebug.addExecutePhase("���б�Ҫ������Ѱ�Һ�ͬ");/* -=notranslate=- */

    // ���õ���ҳ��ɱ༭
    setEnabled(true);

    //V31����ͬV30��ʾVO�����洦��--------------------------------------------------------
    // ------------------

    // ��һ�ŵ��ݵĹ�˾
    String sOldPk_corp = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
        "pk_corp").getValue());

    // �����Ǽ��Ųɹ����漰�൥λ�����Ҫ���ò���
    setCorp(voNew.getHeadVO().getPk_corp());

    // ���ò���
    if (!voNew.getHeadVO().getPk_corp().equals(sOldPk_corp)) {
      setRefPane();
    }
    timeDebug.addExecutePhase("���ò���");/* -=notranslate=- */

    // �����������۸񾫶�
    setHeadDigits(getCorp());
    setBodyDigits_CorpRelated(getCorp(), getBillModel());
    timeDebug.addExecutePhase("�����������۸񾫶�");/* -=notranslate=- */

    // ����VO
    // setBillValueVO(voNew);
    // timeDebug.addExecutePhase("����VO");/*-=notranslate=-*/

    // ������ֵ��ֵ���ַ��Ͳ�����
//    String[] saDecimalKeyBody = new String[] {
//        "nassistnum",
//        "ndiscountrate",
//
//        "nexchangeotobrate", "nmaxprice", "nmoney", "nnotarrvnum",
//        "nnotstorenum", "nordernum", "norgnettaxprice", "norgtaxprice",
//        "noriginalcurmny", "noriginalcurprice", "noriginalnetprice",
//        "noriginaltaxmny", "noriginaltaxpricemny",
//        // "nprice",
//        "ntaxmny",
//        // "ntaxprice",
//        "ntaxpricemny", "ntaxrate"
//    };
//    int jLen = saDecimalKeyBody.length;
//    for (int i = 0; i < iLen; i++) {
//      for (int j = 0; j < jLen; j++) {
//        setBodyValueAt(voaItem[i].getAttributeValue(saDecimalKeyBody[j]), i,
//            saDecimalKeyBody[j]);
//      }
//    }
    
    timeDebug.addExecutePhase("������ֵ��ֵ����ͬ��Ϣ�����֡��к�");/* -=notranslate=- */
    getBillModel().execLoadFormulaByKey("ccontractcode");// ccontractcode->
                                                         // getColValue
                                                         // (ct_manage
                                                         // ,ct_code,pk_ct_manage
                                                         // ,ccontractid)
    timeDebug.addExecutePhase("ִ�к�ͬ�ż��ڹ�ʽ");/* -=notranslate=- */
    // ���ñ�ͷ���¼�����ʾ��ȡ��һ�е�ֵ
    OrderItemVO voFirstItem = voNew.getBodyVO()[0];
    getHeadItem("ccurrencytypeid").setValue(voFirstItem.getCcurrencytypeid());
    // ���ñ�ͷ���־���
    resetHeadCurrDigits();
    timeDebug.addExecutePhase("���ñ�ͷ���־���");/* -=notranslate=- */
    getHeadItem("nexchangeotobrate").setValue(
        voFirstItem.getNexchangeotobrate());
    getHeadItem("cprojectid").setValue(voFirstItem.getCprojectid());
    getHeadItem("idiscounttaxtype").setValue(voFirstItem.getIdiscounttaxtype());
    getHeadItem("ntaxrate").setValue(voFirstItem.getNtaxrate());
    // ���ñ����������ص���ֵ
    try {
      resetBodyValueRelated_Curr(getCorp(), voFirstItem.getCcurrencytypeid(),
          getBillModel(), new BusinessCurrencyRateUtil(getCorp()),
          getRowCount(), m_cardPoPubSetUI2);
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }
    timeDebug.addExecutePhase("�������ñ�����ص���ֵ");/* -=notranslate=- */
    // ���б�Ҫ������Ѱ�Һ�ͬ
    setRelated_Cnt(0, getRowCount() - 1, false, false);
    getBillModel().getValueAt(0, "ccontractcode");

    // ����޼�
    PoPublicUIClass.loadCardMaxPrice(this, getCorp(), m_cardPoPubSetUI2);
    timeDebug.addExecutePhase("����޼�");/* -=notranslate=- */

    // timeDebug.showAllExecutePhase("�ɹ�������Ƭ��ʾ");/*-=notranslate=-*/

    //--------------------------------------------------------------------------
    // ----------------------------

    // ���ñ�ͷ�Ŀɱ༭��
    setEnabled_Head(false);
    timeDebug.addExecutePhase("���ñ�ͷ�Ŀɱ༭��");/* -=notranslate=- */

    // ��ע
//    setDefaultValueToVmemo();
//    timeDebug.addExecutePhase("��ע");/* -=notranslate=- */

    // �շ�����ַ
//    setDefaultValueToAddr();
//    timeDebug.addExecutePhase("�շ�����ַ");/* -=notranslate=- */

    // 20050310����ʾ���ñ�ͷ�༭ǰ����
    /*
     * V5 Del: if(getHeadItem("cstoreorganization") != null){ beforeEdit(new
     * BillItemEvent(getHeadItem("cstoreorganization"))); }
     */
    updateValue();
    updateUI();

    // �򿪺ϼƿ���
    getBillModel().setNeedCalculate(bOldNeedCalc);
    timeDebug.addExecutePhase("�򿪺ϼƿ���");/* -=notranslate=- */

    timeDebug.showAllExecutePhase("���ƶ���");/* -=notranslate=- */

  }

  /**
   * ����һ�е�����
   * 
   * @param
   * @return
   * @exception
   * @see
   * @since 2001-04-28
   */
  public void onActionCopyLine() {

    // showHintMessage( CommonConstant.SPACE_MARK + "���ƶ�����" +
    // CommonConstant.SPACE_MARK ) ;
    if (getRowCount() > 0) {
      if (getBillTable().getSelectedRowCount() <= 0) {
        MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200",
                "UPP40040200-000030")/* @res "������ǰ����ѡ�������" */);
        return;
      }
      copyLine();
    }

    // showHintMessage( CommonConstant.SPACE_MARK + "�༭����" +
    // CommonConstant.SPACE_MARK ) ;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�ɾ��һ�е����� ������ ���أ� ���⣺ ���ڣ�(2002-1-4 13:24:16) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 2002-07-08 wyf ɾ��һ�к��Զ����У�ȥ���жϣ���Ϊ��������ʱ��ɾ�в����� 2004-05-27 wyf ������޶����ݵ��ж�
   */
  public void onActionDeleteLine() {

    // showHintMessage( CommonConstant.SPACE_MARK + "ɾ��������" +
    // CommonConstant.SPACE_MARK ) ;

    // -------------�к����������в���ɾ���������������ӷ�����ʼ��
    // ��ҪΪ�޶��ṩ
    int[] iaSelectedRow = getBillTable().getSelectedRows();
    if (iaSelectedRow == null) {
      return;
    }
    int iSelectedCount = iaSelectedRow.length;
    for (int i = 0; i < iSelectedCount; i++) {
      String sBId = (String) getBodyValueAt(iaSelectedRow[i], "corder_bid");
      if (PuPubVO.getString_TrimZeroLenAsNull(sBId) != null) {
        if (!isRowActive(iaSelectedRow[i])) {
          // �رյ��в���ɾ��
          MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                              * @res "��ʾ"
                                                              */,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200",
                  "UPP40040200-000031")/*
                                        * @res "��ѡ�д����ѹرյ��У�����ɾ��"
                                        */);
          return;
        }
        if (isRowHaveAfterBill(iaSelectedRow[i])) {
          // �к����������в���ɾ��
          MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                              * @res "��ʾ"
                                                              */,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200",
                  "UPP40040200-000032")/*
                                        * @res "��ѡ�д����к����������У�����ɾ��"
                                        */);
          return;
        }
      }
      String sUoBid = (String) getBodyValueAt(iaSelectedRow[i],
          "cupsourcebillrowid");
      OrderVO vo = ((PoToftPanel) m_conInvoker)
          .getOrderDataVOAt(((PoToftPanel) m_conInvoker).getBufferVOManager()
              .getVOPos());
      Vector vecData = new Vector();
      if (vo != null && sUoBid != null) {
        // for (int j = 0; j < vo.getBodyVO().length; j++) {
        // if (vo.getBodyVO()[j].getCupsourcebillrowid() != null
        // && !vo.getBodyVO()[j].getCupsourcebillrowid()
        // .equals(sUoBid)) {
        // vecData.add(vo.getBodyVO()[j]);
        // }
        // }
        // OrderItemVO[] itemvo = new OrderItemVO[vecData.size()];
        // vecData.copyInto(itemvo);
        // vo.setChildrenVO(itemvo);
        ((PoToftPanel) m_conInvoker).hPraybillVO.remove(sUoBid);
      }
    }

    // ɾ��
    delLine();

    // ������ʾ
    if (getBillType().equalsIgnoreCase(BillTypeConst.PO_ORDER)
        || getBillType().equalsIgnoreCase(BillTypeConst.PO_REPLENISH)) {
      OrderVO voCurr = (OrderVO) getBillValueVO(OrderVO.class.getName(),
          OrderHeaderVO.class.getName(), OrderItemVO.class.getName());
      InvAttrCellRenderer ficr = new InvAttrCellRenderer();
      ficr.setFreeItemRenderer((nc.ui.pub.bill.BillCardPanel) this, voCurr);
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�����һ�е����� ������ ���أ� ���⣺ ���ڣ�(2003-7-28 13:24:16) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public void onActionInsertLine() {

    // showHintMessage( CommonConstant.SPACE_MARK + "���붩����" +
    // CommonConstant.SPACE_MARK ) ;

    if (getBillTable().getSelectedRowCount() <= 0) {
      MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200",
              "UPP40040200-000033")/* @res "����ǰ����ѡ�������" */);
      return;
    }
    onActionInsertLines(getBillTable().getSelectedRow(), 1,-1);

    // ������ʾ
    if (getBillType().equalsIgnoreCase(BillTypeConst.PO_ORDER)
        || getBillType().equalsIgnoreCase(BillTypeConst.PO_REPLENISH)) {
      OrderVO voCurr = (OrderVO) getBillValueVO(OrderVO.class.getName(),
          OrderHeaderVO.class.getName(), OrderItemVO.class.getName());
      InvAttrCellRenderer ficr = new InvAttrCellRenderer();
      ficr.setFreeItemRenderer((nc.ui.pub.bill.BillCardPanel) this, voCurr);
    }

  }

  /**
   * ���ߣ���ӡ�� ���ܣ�������е����� ������int iCurRow �ڴ����ϲ��� int iInsertCount ��������� ���أ� ���⣺
   * ���ڣ�(2003-10-28 13:24:16) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * �����Ҫ���ú�ͬ��ص���Ϣ����ôiSourceRowNoΪԴ�кţ�����iSourceRowNo = -1 since 5.6 modify by donggq
   */
  private void onActionInsertLines(int iCurRow, int iInsertCount , int iSourceRowNo) {

    if (iInsertCount == 0) {
      return;
    }

    // ����
    getBodyPanel().insertLine(iCurRow, iInsertCount);

    int iLen = iInsertCount + 1;
    // ���ӵ���
    int[] iaRow = PuTool.getRowsAfterMultiSelect(iCurRow, iLen);

    // ��ʼ��������
    int iBeginRow = iaRow[0];
    int iEndRow = iaRow[iLen - 2];

    // ��ֵ
    // String[] saCurrid =
    //getAllCurrIdFromCard(getRowCount(),getCorp(),getHeadItem("ccurrencytypeid"
    // ).getValue(),getBillModel());
    // String[] saCurridDis =
    // m_cardPoPubSetUI2.getDistinctStrArray(saCurrid);
    // HashMap mapRate =
    // m_cardPoPubSetUI2.getBothExchRateDigit(getCorp(),saCurridDis);
    // HashMap mapRateEditable =
    // m_cardPoPubSetUI2.getBothExchRateEditableBatch(getCorp(),saCurridDis);
    // HashMap mapRateVal =
    // m_cardPoPubSetUI2.getBothExchRateValue(getCorp(),saCurridDis,getHeadItem(
    // "dorderdate").getValue());
    boolean isWaitSetAll = iInsertCount > 1;
    for (int i = iBeginRow; i <= iEndRow; i++) {
      if(iSourceRowNo != -1 ){
        // �����²��е�Ĭ��ֵ
        setDefaultBody(i,iCurRow + iInsertCount,isWaitSetAll);
      }else{
     // �����²��е�Ĭ��ֵ
        setDefaultBody(i,-1,isWaitSetAll);
      }
      // ��ͬ�Ų��ɱ༭
      setCellEditable(i, "ccontractcode", false);
    }
    if(isWaitSetAll){
      String[] falmulars =  getBodyItem("ccurrencytype").getEditFormulas();
      ArrayList<String> alFomulars = new ArrayList<String>();
      if(falmulars != null ){
        for (String fomular : falmulars) {
          alFomulars.add(fomular);
        }
      }
      alFomulars.add( "coperator->getColValue(sm_user,cUserid,cUserId,coperator)" );
      alFomulars.add("coperatorname->getColValue(sm_user,user_name,cUserId,coperator)");
      getBillModel().execFormulas(alFomulars.toArray(new String[alFomulars.size()]));
    }else{
      getBillModel().execEditFormulaByKey(-1, "ccurrencytype"); // V31�Ż���ѭ������
    }
    // ������Ŀ���Զ�Я��
    PuTool.setBodyProjectByHeadProject(this, "cprojectid", "cprojectid",
        "cproject", iBeginRow, iEndRow);

    // �����к�
    BillRowNo.insertLineRowNos(this, BillTypeConst.PO_ORDER, "crowno",
        iaRow[iLen - 1], iInsertCount);

    // ���ñ��塰�շ�����ַ���������ص㡱�͡���Ӧ���շ�����ַ���������ص㡱Ĭ��ֵ
    setDefaultValueWhenAppInsLines(iBeginRow, iEndRow);

  }

  /**
   * ���ߣ���ӡ�� ���ܣ��޸ĵ�ǰ���� �������� ���أ��� ���⣺�� ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public void onActionModify() {

    // //����ҵ�����ͶԴ�����յ�����(��beforEditBodyInventory())
    // m_strCbiztype = getHeadItem("cbiztype").getValue();
    // if(m_strCbiztype == null || m_strCbiztype.trim().length() == 0){
    // //���Ƽ۸����������ɲɹ�����ҵ�����Ϳ�ѡ
    // if(getBodyValueAt(0, "cupsourcebilltype") != null
    // && getBodyValueAt(0, "cupsourcebilltype").equals("28")){
    // m_strCbiztype = ((PoToftPanel)getContainer()).getCurBizeType();
    // getHeadItem("cbiztype").setValue(m_strCbiztype);
    // }
    // }
    getHeadItem("nexchangeotobrate").getValue();

    // ���õ���ҳ��ɱ༭
    setEnabled(true);

    // ���ñ�ͷ����Ŀɱ༭��
    setEnabled_Head(false);

    // ���屸ע
//    setDefaultValueToVmemo();

    // �շ�����ַ
    setDefaultValueToAddrForCHG();

    /**
     * *************************************************************************
     * ***********
     */
    getBillTable().clearSelection();

    // ȫ����
    // transferFocusTo(BillCardPanel.HEAD) ;
    String statue = getHeadItem("forderstatus").getValue();
    if (statue != null && statue.equals("0")) {
      // ���ݲ��������Ƶ���
      setOperatorValue();
    }
    // 20050310����ʾ���ñ�ͷ�༭ǰ����
    /*
     * V5 Del: if(getHeadItem("cstoreorganization") != null){ beforeEdit(new
     * BillItemEvent(getHeadItem("cstoreorganization"))); }
     */
    // �̶�ֵ����:����V31��������
    if (!(getContainer() instanceof RevisionUI)) {
      PuTool.setBillCardPanelDefaultValue(this, this);
    }

    // ����������������ʾ
    if (getBillType().equalsIgnoreCase(BillTypeConst.PO_ORDER)
        || getBillType().equalsIgnoreCase(BillTypeConst.PO_REPLENISH)) {
      OrderVO voCurr = (OrderVO) getBillValueVO(OrderVO.class.getName(),
          OrderHeaderVO.class.getName(), OrderItemVO.class.getName());
      InvAttrCellRenderer ficr = new InvAttrCellRenderer();
      ficr.setFreeItemRenderer(this, voCurr);
      // updateUI();
    }

    // since v51, ���ݲ���Ա���òɹ�Ա���ɹ�����
    setDefaultValueByUser();
    // �����˻����γɲ���������ͷ����Ϊ��
    if (getHeadItem("ccurrencytypeid").getValue() == null
        || (getHeadItem("ccurrencytypeid").getValue() != null && getHeadItem(
            "ccurrencytypeid").getValue().trim().length() == 0)) {
      getHeadItem("ccurrencytypeid").setValue(
          getBodyValueAt(0, "ccurrencytypeid"));
    }
    int[] iaExchRateDigit = m_cardPoPubSetUI2.getBothExchRateDigit(getCorp(),
        getHeadItem("ccurrencytypeid").getValue());

    // ��������ˣ�����ʱ��
    if (getTailItem("cauditpsn").getValue() != null) {
      getTailItem("cauditpsn").setValue(null);
      getTailItem("dauditdate").setValue(null);
      getTailItem("taudittime").setValue(null);
    }
  }

  /**
   * ���ݲ���Ա���òɹ�Ա���ɹ����š�
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author czp
   * @time 2007-3-26 ����04:49:35
   */
  private void setDefaultValueByUser() {
    // ȡ����Ա��Ӧҵ��Ա�����òɹ�Ա(�ɹ�Ա��ֵʱ������)
    if (PuPubVO.getString_TrimZeroLenAsNull(getHeadItem("cemployeeid")
        .getValueObject()) == null) {
      IUserManageQuery iSrvUser = (IUserManageQuery) NCLocator.getInstance()
          .lookup(IUserManageQuery.class.getName());
      PsndocVO voPsndoc = PuTool.getPsnByUser(PoPublicUIClass.getLoginUser(),PoPublicUIClass.getLoginPk_corp());
      if (voPsndoc != null 
          && PuPubVO.getString_TrimZeroLenAsNull(voPsndoc.getPk_psndoc()) != null) {
        UIRefPane refPanePrayPsn = (UIRefPane) getHeadItem("cemployeeid")
            .getComponent();
        refPanePrayPsn.setPK(voPsndoc.getPk_psndoc());
        // �ɲɹ�Ա�����ɹ�����(����ɹ�Ա������ֵʱ�Ŵ���)
        if (PuPubVO.getString_TrimZeroLenAsNull(getHeadItem("cdeptid")
            .getValueObject()) == null) {
          afterEditWhenHeadEmployee(null);
        }
      }
    }
    
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�����һ�Ųɹ����� �������� ���أ��� ���⣺�� ���ڣ�(2002-3-13 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-05-27 ��ӡ�� ������������ݣ������ð�ť״̬������ᵼ�°�ť״̬����ȷ
   */
  public void onActionNew(String sBizType) {

    // ����ҵ�����ͶԴ�����յ�����(��beforEditBodyInventory())
    m_strCbiztype = sBizType;

    String sOldPk_corp = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
        "pk_corp").getValue());
    setCorp(PoPublicUIClass.getLoginPk_corp());

    // ���ò��յĹ�˾����(���Ƶ���Ϊ����˾����)
    // ֧�ֵ���ģ��Ĭ��ֵ������ֵ
    if (!PoPublicUIClass.getLoginPk_corp().equals(sOldPk_corp)) {
      setRefPane();
    }

    // ���õ���ҳ��ɱ༭
    setEnabled(true);

    // ���ӱ�ͷ
    addNew();

    // ���ñ�ͷĬ������
    setDefaultHead(sBizType);

    // ���ñ�ͷ�ı༭��
    setEnabled_Head(false);

    // ���þ���
    setBodyDigits_CorpRelated(getCorp(), getBillModel());

    // ��ע
//    setDefaultValueToVmemo();

    // �շ�����ַ
//    setDefaultValueToAddr();
    String sOldPk_corp1 = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
        "pk_corp").getValue());
    // �﷫�Ĳ�����֧�ֱ�ͷ˰��Ϊ��������
    if (getHeadItem("ntaxrate") != null
        && getHeadItem("ntaxrate").getComponent() instanceof UIComboBox) {
      ((UIComboBox) getHeadItem("ntaxrate").getComponent()).setSelectedIndex(0);
    }

    // ������������Ҫ�Զ����й���
    try {
      if (!nc.vo.scm.pub.CustomerConfigVO.getCustomerName().equalsIgnoreCase(
          nc.vo.scm.pub.CustomerConfigVO.NAME_FENGLITINGLI)) {
        // v50:���ӿ��д���
        onActionAppendLine(1,-1);
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ���ճ�� �������� ���أ��� ���⣺�� ���ڣ�(2001-5-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 2002-09-20 wyf �ָ��ԣ��ϲ㣩��ԴID����Դ��ID�ĸ��� 2002-11-25 wyf ��TS���� 2003-02-27 wyf
   * �޸�Ϊ�������и��Ƶ��н�����Ŀ��ռ��༭�Կ���
   */
  public void onActionPasteLine() {

    // showHintMessage( CommonConstant.SPACE_MARK + "ճ��������" +
    // CommonConstant.SPACE_MARK ) ;
    // ճ��ǰ������
    int iOrgRowCount = getRowCount();
    pasteLine();
    // ���ӵ�����
    int iPastedRowCount = getRowCount() - iOrgRowCount;
    // �����к�
    BillRowNo.pasteLineRowNo(this, BillTypeConst.PO_ORDER, "crowno",
        iPastedRowCount);

    int iEndRow = getBillTable().getSelectedRow() - 1;
    int iBeginRow = iEndRow - iPastedRowCount + 1;

    // ����Ĭ��ֵ
    setValueToPastedLines(iBeginRow, iEndRow);

    // showHintMessage( CommonConstant.SPACE_MARK + "�༭����" +
    // CommonConstant.SPACE_MARK ) ;

    // ������ʾ
    if (getBillType().equalsIgnoreCase(BillTypeConst.PO_ORDER)
        || getBillType().equalsIgnoreCase(BillTypeConst.PO_REPLENISH)) {
      OrderVO voCurr = (OrderVO) getBillValueVO(OrderVO.class.getName(),
          OrderHeaderVO.class.getName(), OrderItemVO.class.getName());
      InvAttrCellRenderer ficr = new InvAttrCellRenderer();
      ficr.setFreeItemRenderer((nc.ui.pub.bill.BillCardPanel) this, voCurr);
    }

  }

  /**
   * ���ߣ���ӡ�� ���ܣ���ճ�� �������� ���أ��� ���⣺�� ���ڣ�(2001-5-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 2002-09-20 wyf �ָ��ԣ��ϲ㣩��ԴID����Դ��ID�ĸ��� 2002-11-25 wyf ��TS���� 2003-02-27 wyf
   * �޸�Ϊ�������и��Ƶ��н�����Ŀ��ռ��༭�Կ���
   */
  public void onActionPasteLineToTail() {

    // showHintMessage( CommonConstant.SPACE_MARK + "ճ��������" +
    // CommonConstant.SPACE_MARK ) ;
    // ճ��ǰ������
    int iOrgRowCount = getRowCount();
    pasteLineToTail();
    // ���ӵ�����
    int iPastedRowCount = getRowCount() - iOrgRowCount;
    // �����к�
    BillRowNo.addLineRowNos(this, BillTypeConst.PO_ORDER, "crowno",
        iPastedRowCount);

    int iEndRow = getRowCount() - 1;
    int iBeginRow = iOrgRowCount;

    // ����Ĭ��ֵ
    setValueToPastedLines(iBeginRow, iEndRow);

    // ������ʾ
    if (getBillType().equalsIgnoreCase(BillTypeConst.PO_ORDER)
        || getBillType().equalsIgnoreCase(BillTypeConst.PO_REPLENISH)) {
      OrderVO voCurr = (OrderVO) getBillValueVO(OrderVO.class.getName(),
          OrderHeaderVO.class.getName(), OrderItemVO.class.getName());
      InvAttrCellRenderer ficr = new InvAttrCellRenderer();
      ficr.setFreeItemRenderer((nc.ui.pub.bill.BillCardPanel) this, voCurr);
    }

  }

  /**
   * ���ߣ���ӡ�� ���ܣ��޶���ǰ���� �������� ���أ��� ���⣺�� ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public void onActionRevise(OrderVO voOrder) {

    // ���õ���ҳ��ɱ༭
    setEnabled(true);

    // �����޶�������Ĭ��ֵ
    setDefaultReviseItems(voOrder);

    // ���ñ�ͷ�Ŀɱ༭��
    setEnabled_Head(true);

    // �����޶���Ŀɱ༭��
    setReviseEnabledCells_Head(voOrder);

    // ���屸ע
//    setDefaultValueToVmemo();

    // �շ�����ַ
//    setDefaultValueToAddr();

    /**
     * *************************************************************************
     * ***********
     */
    getBillTable().clearSelection();

    // ȫ����
    // transferFocusTo(BillCardPanel.HEAD) ;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�������帡���˵� �������� ���أ��� ���⣺�� ���ڣ�(2001-4-18 13:24:16)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public void onMenuItemClick(java.awt.event.ActionEvent event) {

    // �õ������˵�
    UIMenuItem menuItem = (UIMenuItem) event.getSource();

    if (menuItem.equals(getAddLineMenuItem())) {
      onActionAppendLine();
      if (getContainer() instanceof PoToftPanel) {
        ((PoToftPanel) getContainer()).showHintMessage(nc.ui.ml.NCLangRes
            .getInstance().getStrByID("common", "UCH036")/*
                                                          * @res "���гɹ�"
                                                          */);
      }
    }
    else if (menuItem.equals(getCopyLineMenuItem())) {
      onActionCopyLine();
      if (getContainer() instanceof PoToftPanel) {
        ((PoToftPanel) getContainer()).showHintMessage(nc.ui.ml.NCLangRes
            .getInstance().getStrByID("common", "UCH039")/*
                                                          * @res "�����гɹ�"
                                                          */);
      }
    }
    else if (menuItem.equals(getDelLineMenuItem())) {
      onActionDeleteLine();
      if (getContainer() instanceof PoToftPanel) {
        ((PoToftPanel) getContainer()).showHintMessage(nc.ui.ml.NCLangRes
            .getInstance().getStrByID("common", "UCH037")/*
                                                          * @res "ɾ�гɹ�"
                                                          */);
      }
    }
    else if (menuItem.equals(getInsertLineMenuItem())) {
      onActionInsertLine();
      if (getContainer() instanceof PoToftPanel) {
        ((PoToftPanel) getContainer()).showHintMessage(nc.ui.ml.NCLangRes
            .getInstance().getStrByID("common", "UCH038")/*
                                                          * @res "�����гɹ�"
                                                          */);
      }
    }
    else if (menuItem.equals(getPasteLineMenuItem())) {
      onActionPasteLine();
      if (getContainer() instanceof PoToftPanel) {
        ((PoToftPanel) getContainer()).showHintMessage(nc.ui.ml.NCLangRes
            .getInstance().getStrByID("common", "UCH040")/*
                                                          * @res "ճ���гɹ�"
                                                          */);
      }
    }
    else if (menuItem.equals(getPasteLineToTailMenuItem())) {
      onActionPasteLineToTail();
      if (getContainer() instanceof PoToftPanel) {
        ((PoToftPanel) getContainer()).showHintMessage(nc.ui.ml.NCLangRes
            .getInstance().getStrByID("common", "UCH040")/*
                                                          * @res "ճ���гɹ�"
                                                          */);
      }
    }
    // �����к�,֧�ֶ��ҳǩ�ġ������кš�
    else if (NCLangRes.getInstance().getStrByID("common", "SCMCOMMON000000283")
        .equals(menuItem.getText())) {
      onActionReSortRowNo();
    }
    else if (NCLangRes.getInstance().getStrByID("common", "SCMCOMMON000000267")
        /* @res"��Ƭ�༭" */.equals(menuItem.getText())) {
      onCardEdit();
    }
  }

  /*
   * �����к�
   */
  public void onActionReSortRowNo() {
    PuTool.resortRowNo(this, ScmConst.PO_Order, "crowno");
    ((PoToftPanel) getContainer()).showHintMessage(NCLangRes.getInstance()
        .getStrByID("common", "SCMCOMMON000000284")/*
                                                    * @res "�����кųɹ�"
                                                    */);
  }

  /**
   * ��Ƭ�༭
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author donggq
   * @time 2008-8-25 ����02:44:46
   */
  public void onCardEdit() {
    startRowCardEdit();
  }

  /**
   * ���ߣ�WYF ���ܣ����ñ���������йص�ֵ�ľ��ȣ��˷������б����ͬʱʹ�� ������String pk_corp ��˾ID BillModel
   * billModel BillModel ���أ��� ���⣺�� ���ڣ�(2004-6-11 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public static void resetBodyValueRelated_Curr(String pk_corp,
      String strHeadCurId, BillModel billModel, BusinessCurrencyRateUtil bca,
      int iLen, POPubSetUI2 setUi) {
    // =====����
    // �������ϼ�
    String[] saMnyItem = new String[] {
        "noriginalcurmny", "noriginaltaxmny", "noriginaltaxpricemny"
    };
    int iMnyLen = saMnyItem.length;
    // �������λ��
    int iMaxMnyDigit = 0;

    // ��Ϊ���ϼƣ�����ռ�÷ǳ����ʱ��
    boolean bOldNeedCalculate = billModel.isNeedCalculate();
    billModel.setNeedCalculate(false);

    // ��ֵ
    // String[] saCurrId =
    // getAllCurrIdFromCard(iLen,pk_corp,strHeadCurId,billModel);
    // int iCnt = saCurrId == null ? 0 : saCurrId.length;
    // String[] saPk_corp = new String[iCnt];
    // for (int i = 0; i < iCnt; i++) {
    // saPk_corp[i] = pk_corp;
    // }
    // HashMap mapRate =
    // m_cardPoPubSetUI2.getBothExchRateDigit(saPk_corp,saCurrId);
    // HashMap mapRateMny =
    // m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(saCurrId);

    for (int i = 0; i < iLen; i++) {
      // �۱����۸�����
      setRowDigits_ExchangeRate(pk_corp, i, billModel, setUi);
      billModel.setValueAt(billModel.getValueAt(i, "nexchangeotobrate"), i,
          "nexchangeotobrate");
      // ��˰���˰�ϼ�
      setRowDigits_Mny(pk_corp, i, billModel, setUi);
      // ���ҽ����
      int jLenMny = OrderItemVO.getDbMnyFields_Local_Busi().length;
      for (int j = 0; j < jLenMny; j++) {
        billModel.setValueAt(billModel.getValueAt(i, OrderItemVO
            .getDbMnyFields_Local_Busi()[j]), i, OrderItemVO
            .getDbMnyFields_Local_Busi()[j]);
      }
      for (int j = 0; j < iMnyLen; j++) {
        billModel.setValueAt(billModel.getValueAt(i, saMnyItem[j]), i,
            saMnyItem[j]);
      }
      if (billModel.getItemByKey(saMnyItem[0]).getDecimalDigits() > iMaxMnyDigit) {
        iMaxMnyDigit = billModel.getItemByKey(saMnyItem[0]).getDecimalDigits();
      }
    }

    // ����ϼ�
    for (int i = 0; i < iMnyLen; i++) {
      billModel.getItemByKey(saMnyItem[0]).setDecimalDigits(iMaxMnyDigit);
      billModel.reCalcurate(billModel.getBodyColByKey(saMnyItem[i]));
    }
    billModel.setNeedCalculate(bOldNeedCalculate);
  }

  /**
   * ���ߣ���־ƽ ���ܣ������۱����۸�����С��λ �������� ���أ��� ���⣺�� ���ڣ�2005-6-17 14:39:21
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־
   */
  private void resetHeadCurrDigits() {
    // =====��ͷ
    String sCurrId = getHeadItem("ccurrencytypeid").getValue();
    if (sCurrId != null && sCurrId.trim().length() > 0) {
      // �õ����ʾ���
      int[] iaExchRateDigit = m_cardPoPubSetUI2.getBothExchRateDigit(getCorp(),
          sCurrId);
      // ���þ���
      getHeadItem("nexchangeotobrate").setDecimalDigits(iaExchRateDigit[0]);
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ������빫˾�йصľ��ȣ��˷������б����ͬʱʹ�� ������ActionEvent e �¼� ���أ��� ���⣺��
   * ���ڣ�(2002-4-22 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-05-16 wyf
   * getDecimalDigits()�޸�Ϊ���ù������� 2002-07-05 wyf ����һ�����󣬸ñ������б���滻���ʾ��Ȳ���ȷ
   * 2002-10-28 wyf ���������޼ۼ���˰���۵ľ��ȿ���
   */
  protected static void setBodyDigits_CorpRelated(String pk_corp, BillModel bm) {

    int[] iaDigit = (int[]) PoPublicUIClass.getShowDigits(pk_corp);
    // ������
    int iLen = OrderItemVO.getDbMainNumFields().length;
    for (int i = 0; i < iLen; i++) {
      BillItem item = bm.getItemByKey(OrderItemVO.getDbMainNumFields()[i]);
      if (item != null && iaDigit != null && iaDigit.length > 0) {
        item.setDecimalDigits(iaDigit[0]);
      }
    }
    // ��������
    iLen = OrderItemVO.getDbAssistNumFields().length;
    for (int i = 0; i < iLen; i++) {
      BillItem item = bm.getItemByKey(OrderItemVO.getDbAssistNumFields()[i]);
      if (item != null) {
        item.setDecimalDigits(iaDigit[1]);
      }
    }
    // ������
    iLen = OrderItemVO.getDbPriceFields().length;
    for (int i = 0; i < iLen; i++) {
      BillItem item = bm.getItemByKey(OrderItemVO.getDbPriceFields()[i]);
      if (item != null) {
        item.setDecimalDigits(iaDigit[2]);
      }
    }
    bm.getItemByKey("nmaxprice").setDecimalDigits(iaDigit[2]);
    // ������
    bm.getItemByKey("nconvertrate").setDecimalDigits(iaDigit[3]);

    // ���������ص�����Ϊ���λ�������ⱻ��λ
    // �۱����۸�
    bm.getItemByKey("nexchangeotobrate").setDecimalDigits(
        FieldMaxLength.DECIMALDIGIT_TOBRATE);
    // ���
    bm.getItemByKey("noriginalcurmny").setDecimalDigits(
        FieldMaxLength.DECIMALDIGIT_MONEY);
    bm.getItemByKey("noriginaltaxmny").setDecimalDigits(
        FieldMaxLength.DECIMALDIGIT_MONEY);
    bm.getItemByKey("noriginaltaxpricemny").setDecimalDigits(
        FieldMaxLength.DECIMALDIGIT_MONEY);
    // ���ҽ����
    int jLenMny = OrderItemVO.getDbMnyFields_Local_Busi().length;
    for (int j = 0; j < jLenMny; j++) {
      bm.getItemByKey(OrderItemVO.getDbMnyFields_Local_Busi()[j])
          .setDecimalDigits(FieldMaxLength.DECIMALDIGIT_MONEY);
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ����õ�ǰ������ �������� ���أ��� ���⣺�� ���ڣ�(2001-05-22 13:24:16)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setContainer(Container newCon) {
    m_conInvoker = newCon;
  }

  /**
   * ���ߣ����� ���ܣ����ӱ�����ʱ������Ĭ������ ������int Row �ڼ��� ���أ��� ���⣺�� ���ڣ�(2002-3-13 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-06-07 WYF ɾ��Ĭ�����õ�ǰ�����˵Ĵ��� 2002-06-18 WYF
   * ɾ��Ĭ�����õ�ǰIDΪһ�����Ĵ��� 2003-01-20 WYF �޸ĶԼƻ��������ڵļ���
   * isWaitSetAll �ȴ���������
   */
  private void setDefaultBody(int iCurRow,int iSourceRowNo,boolean isWaitSetAll) {

    // ID
    setBodyValueAt(null, iCurRow, "corder_bid");
    // ��˾����
    setBodyValueAt(getCorp(), iCurRow, "pk_corp");
    // ����״̬
    setBodyValueAt(OrderItemVO.FORDERROWSTATUS_FREE, iCurRow, "forderrowstatus");
    setBodyValueAt(OrderItemVO.IISREPLENISH_NO, iCurRow, "iisreplenish");
    setBodyValueAt(OrderItemVO.IISACTIVE_ACTIVE, iCurRow, "iisactive");
    // V3����
    setBodyValueAt(OrderItemVO.BRECEIVEPLAN_NO, iCurRow, "breceiveplan");
    // �Ƶ���
    setBodyValueAt(PoPublicUIClass.getLoginUser(), iCurRow, "coperator");
    if(!isWaitSetAll){
      execBodyFormulas(iCurRow, new String[] {
          "coperator->getColValue(sm_user,cUserid,cUserId,coperator)",
          "coperatorname->getColValue(sm_user,user_name,cUserId,coperator)"
      });
    }
    // ����ƻ���������
    calDplanarrvdate(iCurRow, iCurRow);
    // ��˰���
    String text = null;
    // if (getHeadItem("idiscounttaxtype").getDataType() == 6) {
    JComboBox cbbHType = (JComboBox) (getHeadItem("idiscounttaxtype")
        .getComponent());
    if (cbbHType.getSelectedItem() != null) {
      text = cbbHType.getSelectedItem().toString();
    }
    // }
    if (text != null) {
      setBodyValueAt(text, iCurRow, "idiscounttaxtype");
    }
    else {
      setBodyValueAt(VariableConst.IDISCOUNTTAXTYPE_NAME_OUTTER, iCurRow,
          "idiscounttaxtype");
    }

    if (getHeadItem("ccurrencytypeid").getValue() != null && getHeadItem("ccurrencytypeid").getValue().trim().length() > 0) {
      setBodyValueAt(getHeadItem("ccurrencytypeid").getValue(), iCurRow,"ccurrencytypeid");
    }
    else {
      if (iCurRow > 0) {
        setBodyValueAt(getBodyValueAt(0, "ccurrencytypeid"), iCurRow,
        "ccurrencytypeid");
      }
    }
    // ���֡�����
    // ���þ��ȼ��ɱ༭�Ե�
    setExchangeRateBody(iCurRow, false);
    if (getHeadItem("ccurrencytypeid").getValue() != null && getHeadItem("ccurrencytypeid").getValue().trim().length() > 0) {
      setBodyValueAt(PuPubVO.getUFDouble_NullAsZero(getHeadItem("nexchangeotobrate").getValueObject()), 
          iCurRow,"nexchangeotobrate");
    }
    // execBodyFormula(iCurRow, "ccurrencytype"); //V31�Ż���ѭ������
    // setBodyValueAt(PuPubVO.getUFDouble_ValueAsValue(getHeadItem(
    // "nexchangeotobrate").getValue()), iCurRow, "nexchangeotobrate");
    // ˰��
    Object o = getHeadItem("ntaxrate").getValue();

    // �﷫�Ĳ���Ŀ����֧��˰����Ŀ����Ϊ��������
    if (getHeadItem("ntaxrate").getComponent() instanceof UIComboBox) {
      o = ((UIComboBox) getHeadItem("ntaxrate").getComponent())
          .getSelectdItemValue();
    }

    setBodyValueAt(PuPubVO.getUFDouble_ValueAsValue(o), iCurRow, "ntaxrate");
    // ����
    setBodyValueAt(VariableConst.HUNDRED_UFDOUBLE, iCurRow, "ndiscountrate");
    // ������Ŀ��Ϣ
    if (iCurRow - 1 > 0) {
      setProjectInfoAs(iCurRow, iCurRow - 1);
    }
    // ��ע
//    setDefaultValueToVmemo();
    // �շ�����ַ(��ͷ����ġ���Ӧ���շ�����ַ������Ҫ��ġ��շ�����ַ��)
//    setDefaultValueToAddr();

    // ---V5����

    // �ջ���˾��Ĭ��Ϊ��ǰ��¼��˾
    setBodyValueAt(ClientEnvironment.getInstance().getCorporation()
        .getPrimaryKey(), iCurRow, "pk_arrvcorp");
    setBodyValueAt(ClientEnvironment.getInstance().getCorporation()
        .getUnitname(), iCurRow, "arrvcorpname");
    // ��Ʊ��˾��Ĭ��Ϊ��ǰ��¼��˾
    setBodyValueAt(ClientEnvironment.getInstance().getCorporation()
        .getPrimaryKey(), iCurRow, "pk_invoicecorp");
    setBodyValueAt(ClientEnvironment.getInstance().getCorporation()
        .getUnitname(), iCurRow, "invoicecorpname");

    // ����˾: Ĭ��Ϊ��ǰ��¼��˾ v5.02 fengping 07 11 09
    setBodyValueAt(ClientEnvironment.getInstance().getCorporation()
        .getPrimaryKey(), iCurRow, "pk_reqcorp");
    setBodyValueAt(ClientEnvironment.getInstance().getCorporation()
        .getUnitname(), iCurRow, "reqcorpname");
    //�����Դ�кţ���ô���ú�ͬ�����Ϣ��
    if(iSourceRowNo != -1 && iSourceRowNo != iCurRow){
      setBodyValueAt(getBodyValueAt(iSourceRowNo, "cupsourcebillid"), iCurRow, "cupsourcebillid");
      setBodyValueAt(getBodyValueAt(iSourceRowNo, "cupsourcebillrowid"), iCurRow, "cupsourcebillrowid");
      setBodyValueAt(getBodyValueAt(iSourceRowNo, "cupsourcebilltype"), iCurRow, "cupsourcebilltype");
      setBodyValueAt(getBodyValueAt(iSourceRowNo, "ccontractrowid"), iCurRow, "ccontractrowid");
      setBodyValueAt(getBodyValueAt(iSourceRowNo, "ccontractid"), iCurRow, "ccontractid");
      setBodyValueAt(getBodyValueAt(iSourceRowNo, "ccontractcode"), iCurRow, "ccontractcode");
      setBodyValueAt(getBodyValueAt(iSourceRowNo, "noriginalcurprice"), iCurRow, "noriginalcurprice");
      setBodyValueAt(getBodyValueAt(iSourceRowNo, "norgtaxprice"), iCurRow, "norgtaxprice");
      //setBodyValueAt(getBodyValueAt(iSourceRowNo, "ccurrencytypeid"), iCurRow, "ccurrencytypeid");
      setBodyValueAt(getBodyValueAt(iSourceRowNo, "csourcebillname"), iCurRow, "csourcebillname");
      setBodyValueAt(getBodyValueAt(iSourceRowNo, "csourcebillcode"), iCurRow, "csourcebillcode");
      setBodyValueAt(getBodyValueAt(iSourceRowNo, "csourcebillrowno"), iCurRow, "csourcebillrowno");
      //���ñ������
      
      setExchangeRateBody(iCurRow, false);
      // �۸���������
      int iPricePolicy = PuTool.getPricePriorPolicy(getCorp());
      RetCtToPoQueryVO voCtInfo = new RetCtToPoQueryVO();
      voCtInfo.setDOrgPrice((UFDouble)getBodyValueAt(iSourceRowNo, "noriginalcurprice"));
      voCtInfo.setDOrgTaxPrice((UFDouble)getBodyValueAt(iSourceRowNo, "norgtaxprice"));
      UFDouble dPrice = OrderPubVO.getPriceValueByPricePolicy(voCtInfo,
              iPricePolicy);
      String sChangedKey = OrderItemVO.getPriceFieldByPricePolicy(iPricePolicy);
      PoPublicUIClass.setPriceEditableByPricePolicy(getBillModel(), iCurRow,
              voCtInfo, iPricePolicy);
      // ���¼���������ϵ
      if (PuPubVO.getString_TrimZeroLenAsNull(sChangedKey) != null) {
        calRelation(new BillEditEvent(
            getBodyItem(sChangedKey).getComponent(), getBodyValueAt(iCurRow,
                sChangedKey), sChangedKey, iCurRow));
      }
    }
    updateUI();
  }

  /**
   * ���ߣ����� ���ܣ������¶���ʱ�����ñ�ͷĬ������ ������int Row �ڼ��� ���أ��� ���⣺�� ���ڣ�(2002-3-13 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-06-07 WYF ɾ��Ĭ�����õ�ǰ�����˵Ĵ��� 2003-10-08 WYF
   * �޸ı��־��Ȳ���ȷ������
   */
  private void setDefaultHead(String sBizType) {

    try {
      // ��˾����
      setHeadItem("pk_corp", getCorp());
      // ҵ������
      setHeadItem("cbiztype", sBizType);
      // ״̬
      setHeadItem("forderstatus", nc.vo.scm.pu.BillStatus.FREE);
      // �Ƶ���
      setTailItem("coperator", PoPublicUIClass.getLoginUser());
      // �������ڣ�ϵͳע������
      setHeadItem("dorderdate", PoPublicUIClass.getLoginDate());
      // ���ʣ�Ĭ��Я�����ֱ��еĻ��ʣ��������ݣ���Ĭ��Ϊ1�����޸�
      String sLocalCurrId = CurrParamQuery.getInstance().getLocalCurrPK(
          getCorp());
      // �����ԸĽ���֧��ģ��̶�ֵ,����Զ�����ʱ���û���Ҫ�����޸ı��ֵĲ���
      if (PuPubVO.getString_TrimZeroLenAsNull(getHeadItem("ccurrencytypeid")
          .getValueObject()) != null) {
        sLocalCurrId = (String) getHeadItem("ccurrencytypeid").getValueObject();
      }
      // getHeadItem("ccurrencytypeid").setValue(sLocalCurrId);

      setHeadItem("nexchangeotobrate", m_cardPoPubSetUI2.getBothExchRateValue(
          getCorp(), sLocalCurrId, PoPublicUIClass.getLoginDate())[0]);
      setExchangeRateHead(getHeadItem("dorderdate").getValue(), sLocalCurrId);
      // ˰�ʣ�Ĭ��Ϊ0�����޸�
      // setHeadItem("ntaxrate", VariableConst.ZERO);
      // ��˰���
//      setHeadItem("idiscounttaxtype", VariableConst.IDISCOUNTTAXTYPE_OUTTER);
      // �汾
      setHeadItem("nversion", OrderHeaderVO.NVERSION_FIRST);
      // �Ƿ����°汾
      setHeadItem("bislatest", OrderHeaderVO.BISLATEST_YES);
      // �Ƿ񲹻�
      setHeadItem("bisreplenish", OrderHeaderVO.BISREPLENISH_NO);
      // ���ݲ���Ա���òɹ�Ա���ɹ�����
      setDefaultValueByUser();
    }
    catch (Exception e) {
      PuTool.outException(this, e);
    }
  }

  /**
   * ���ߣ����� ���ܣ��ı�������ݺ���ݹ�Ӧ������OrderVO��һЩĬ��ֵ�� ������int Row �ڼ��� ���أ��� ���⣺��
   * ���ڣ�(2001-10-16 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2003-10-20 wyf
   * �޸Ĺ�Ӧ����պ�ҵ��Ա����յĴ���
   */
  private void setDefaultHeadafterVendor(BillEditEvent e) {

    // ��Ӧ�̲���
    UIRefPane ref = (UIRefPane) (getHeadItem("cvendormangid").getComponent());
    String sVendorMangId = ref.getRefPK();
    if (PuPubVO.getString_TrimZeroLenAsNull(sVendorMangId) == null) {
      UIRefPane psn = (UIRefPane) (getHeadItem("cemployeeid").getComponent());
      String whereString = "bd_psndoc.pk_corp = '"
          + getHeadItem("pk_corp").getValueObject() + "'";

      psn.setWhereString(whereString);
      getHeadItem("cgiveinvoicevendor").setValue(null);
      getHeadItem("ctransmodeid").setValue(null);
      getHeadItem("ctermprotocolid").setValue(null);
      getHeadItem("caccountbankid").setValue(null);
      getHeadItem("account").setValue(null);
      getHeadItem("cdeliveraddress").setValue(null);
      return;
    }
    getHeadItem("caccountbankid").setValue(null);
    getHeadItem("account").setValue(null);
    getHeadItem("cdeliveraddress").setValue(null);
    setRefPane_Head("caccountbankid");
    setRefPane_Head("cdeliveraddress");

    PoVendorVO vendorVO = null;
    vendorVO = PoPublicUIClass.getVendDefaultInfo(sVendorMangId);
    if (vendorVO == null) {
      getHeadItem("cdeptid").setValue(null);
      getHeadItem("cemployeeid").setValue(null);
      getHeadItem("creciever").setValue(null);
      // �ջ����������ñ��塰�շ�����ַ���������ص㡱(���ñ�ͷ�ջ����༭���¼�)
      afterEditWhenHeadReciever(new BillEditEvent(getHeadItem("creciever")
          .getComponent(), null, "creciever", 0, BillCardPanel.HEAD));
      getHeadItem("ctransmodeid").setValue(null);
      getHeadItem("ctermprotocolid").setValue(null);
      getHeadItem("ccurrencytypeid").setValue(null);
      getHeadItem("caccountbankid").setValue(null);
      getHeadItem("account").setValue(null);
      getHeadItem("cdeliveraddress").setValue(null);
      // ��Ʊ��
      getHeadItem("cgiveinvoicevendor").setValue(sVendorMangId.trim());
      return;
    }
    // ����
    if (vendorVO.getCrespdept1() != null) {
      getHeadItem("cdeptid").setValue(vendorVO.getCrespdept1().trim());
      /*
       * UIRefPane psn = (UIRefPane) (getHeadItem("cemployeeid")
       * .getComponent()); String whereString = null; if
       * ((vendorVO.getCrespdept1() != null) &&
       * (!(vendorVO.getCrespdept1().trim().equals("")))) { whereString =
       * "bd_psndoc.pk_deptdoc = '" + vendorVO.getCrespdept1().trim() + "'"; }
       * else { whereString = "bd_psndoc.pk_corp = '" +
       * getHeadItem("pk_corp").getValue().trim() + "'"; }
       * psn.setWhereString(whereString);
       */
    }
    // ҵ��Ա
    if (vendorVO.getCresppsn1() != null) {
      getHeadItem("cemployeeid").setValue(vendorVO.getCresppsn1().trim());
      // ҵ��Ա��������
      if (PuPubVO.getString_TrimZeroLenAsNull(vendorVO.getCrespdept1()) == null) {
        // ��ҵ��Ա����Ĭ�ϲ���
        UIRefPane ref1 = (UIRefPane) (getHeadItem("cemployeeid").getComponent());
        ref1.setPK(vendorVO.getCresppsn1().trim());
        // ҵ��Ա��������
        Object sDeptId = ref1.getRefModel().getValue("bd_psndoc.pk_deptdoc");
        getHeadItem("cdeptid").setValue(sDeptId);
      }
      // ҵ��Ա����Ĭ�ϲɹ���֯
      setPurOrg(vendorVO.getCresppsn1().trim());
    }
    // ��Ʊ��
    if (vendorVO.getCcusmandoc2() != null) {
      getHeadItem("cgiveinvoicevendor").setValue(
          vendorVO.getCcusmandoc2().trim());
    }
    else {
      getHeadItem("cgiveinvoicevendor")
          .setValue(vendorVO.getCcumandoc().trim());
    }
    /*
     * ������20041207 �ջ���ֻ�뱾��˾�йأ��빩Ӧ���޹أ��� //�ջ��� if (vendorVO.getCcusmandoc3() !=
     * null) {
     * ((UIRefPane)getHeadItem("creciever").getComponent()).setPK(vendorVO
     * .getCcusmandoc3()); }else{
     * ((UIRefPane)getHeadItem("creciever").getComponent()).setPK(null); }
     * //�ջ����������ñ��塰�շ�����ַ���������ص㡱(���ñ�ͷ�ջ����༭���¼�)
     * setDefaultValueWhenHeadRecieverChged(new
     * BillEditEvent(getHeadItem("creciever"
     * ).getComponent(),null,"creciever",0,BillCardPanel
     * .HEAD),vendorVO.getCcusmandoc3());
     */
    // ���˷�ʽ
    if (vendorVO.getCsendtype() != null /* && isBdeliver() */) {
      getHeadItem("ctransmodeid").setValue(vendorVO.getCsendtype().trim());
    }
    // ����Э��
    if (vendorVO.getCpayterm() != null) {
      getHeadItem("ctermprotocolid").setValue(vendorVO.getCpayterm().trim());
    }
    try {
      // ���� �����Ӧ��Ĭ�Ͻ��ױ���û��,�����ñ���
      if (vendorVO.getCcurrtype1() != null) {
        getHeadItem("ccurrencytypeid").setValue(vendorVO.getCcurrtype1());
        afterEditWhenHeadCurrency(e);
      }
      else {
        getHeadItem("ccurrencytypeid").setValue(
            CurrParamQuery.getInstance().getLocalCurrPK(getCorp()));
        afterEditWhenHeadCurrency(e);
      }
    }
    catch (BusinessException be) {
      SCMEnv.out(e);
    }
    // ��������
    if (vendorVO.getCcustbank() != null) {
      getHeadItem("caccountbankid").setValue(vendorVO.getCcustbank().trim());
    }
    // �ʺ�
    if (vendorVO.getCaccount() != null) {
      getHeadItem("account").setValue(vendorVO.getCaccount().trim());
    }
    // ������ַ
    if (vendorVO.getCcustaddr() != null) {
      ((UIRefPane) getHeadItem("cdeliveraddress").getComponent())
          .setPK(vendorVO.getCcustaddr());
      setHeadItem("cdeliveraddress",
          ((UIRefPane) getHeadItem("cdeliveraddress").getComponent())
              .getRefName());
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ���Ĭ�ϵļ۸�·�� �ú�����setCntRelated(Integer[])���� ������ Integer[] iaRow
   * �����Ĭ�ϼ۸���Ϣ�ı������������� ���أ��� ���⣺�� ���ڣ�(2003-10-29 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setDefaultPrice(Integer[] iaRow) {

    if (iaRow == null) {
      return;
    }
    int iTotalLen = iaRow.length;

    // ��˾
    String pk_corp = getHeadItem("pk_corp").getValue();
    // ��Ӧ�̹���ID
    String sVendId = getHeadItem("cvendormangid").getValue();
    // ����
    String sDate = getHeadItem("dorderdate").getValue();
    // �ɹ���֯
    String sPuorg = getHeadItem("cpurorganization").getValue();

    // ȡ��Ĭ�ϼ۸����¼���������ϵ
    Vector vecStoOrgId = new Vector();
    Vector vecInvId = new Vector();
    Vector vecInvBaseId = new Vector();
    Vector vecCurrId = new Vector();
    Vector vecBRate = new Vector();
    Vector vecARate = new Vector();
    Vector vecAddress = new Vector();
    Vector vecIndex = new Vector();
    Vector vecStore = new Vector();
    Vector vecInvoice = new Vector();

    int iRow = 0;
    for (int i = 0; i < iTotalLen; i++) {
      iRow = iaRow[i].intValue();
      String sInvId = (String) getBillModel().getValueAt(iRow, "cmangid");
      String sInvBaseId = (String) getBillModel().getValueAt(iRow, "cbaseid");
      String sStoreCorp = (String) getBillModel().getValueAt(iRow,
          "pk_arrvcorp");
      String sInvoiceCorp = (String) getBillModel().getValueAt(iRow,
          "pk_invoicecorp");
      if (sInvId == null || sInvId.trim().equals("")) {
        continue;
      }
      String sCurrId = (String) getBillModel().getValueAt(iRow,
          "ccurrencytypeid");
      Object oBRate = getBodyValueAt(iRow, "nexchangeotobrate");

      // V5 add:��Ԫ���ɺ�̨����
      vecStoOrgId.addElement((String) PuPubVO
          .getString_TrimZeroLenAsNull(getBillModel().getValueAt(iRow,
              "pk_arrvstoorg")));

      vecInvId.addElement(sInvId);
      vecInvBaseId.addElement(sInvBaseId);
      vecCurrId.addElement(sCurrId);
      vecBRate.addElement((UFDouble) oBRate);
      vecARate.addElement((UFDouble) null);
      vecAddress.add(getBodyValueAt(iRow, "cdevareaid") == null ? null
          : getBodyValueAt(iRow, "cdevareaid").toString());
      vecIndex.addElement(iaRow[i]);
      vecStore.addElement(sStoreCorp);
      vecInvoice.addElement(sInvoiceCorp);
    }

    int iQueryLen = vecIndex.size();
    if (iQueryLen == 0) {
      return;
    }

    // ��� ���� �۱����� �۸�����
    // ȡĬ�ϼ۸�
    RetPoVrmAndParaPriceVO voPara = new RetPoVrmAndParaPriceVO(1);
    voPara.setPk_corp(pk_corp);
    voPara.setPuorg(sPuorg);
    // V5 modify:
    // voPara.setStoOrgId(getHeadItem("cstoreorganization").getValue());
    voPara.setStoOrgIds((String[]) vecStoOrgId.toArray(new String[vecStoOrgId
        .size()]));
    voPara.setVendMangId(sVendId);
    voPara.setSaInvMangId((String[]) vecInvId.toArray(new String[iQueryLen]));
    voPara.setSaInvBaseId((String[]) vecInvBaseId
        .toArray(new String[iQueryLen]));
    voPara.setSaCurrId((String[]) vecCurrId.toArray(new String[iQueryLen]));
    voPara.setDaBRate((UFDouble[]) vecBRate.toArray(new UFDouble[iQueryLen]));
    voPara.setDaARate((UFDouble[]) vecARate.toArray(new UFDouble[iQueryLen]));
    voPara.setAddress((String[]) vecAddress.toArray(new String[iQueryLen]));
    voPara
        .setDOrderDate(PuPubVO.getString_TrimZeroLenAsNull(sDate) == null ? null
            : new UFDate(sDate));
    voPara.setTransmode(getHeadItem("ctransmodeid").getValue());

    // ҵ������ID:XY+CZP��ҵ�������Ǽ���ҵ������ʱ���������ջ������֯ȡ�ۣ�������ҵ�������£�ֻȡ�������ļ۸�(�ƻ��ۡ��ο��ɱ�)
    String sBizTypeId = getHeadItem("cbiztype").getValue();
    voPara.setBizTypeId(sBizTypeId);
    voPara.setStorecorp((String[]) vecStore.toArray(new String[iQueryLen]));
    voPara.setInvoicecorp((String[]) vecInvoice.toArray(new String[iQueryLen]));
    // �۸񷵻�
    RetPoVrmAndParaPriceVO voRetPrice = PoPublicUIClass
        .getVrmOrParaPricesVO(voPara);

    int iPricePolicy = PuTool.getPricePriorPolicy(pk_corp);
    // ȡ��Ĭ�ϼ۸����¼���������ϵ
    UFDouble dOldPrice = null;
    String sChangedKey = null;

    // String[] saCurrId =
    //getAllCurrIdFromCard(getRowCount(),getCorp(),getHeadItem("ccurrencytypeid"
    // ).getValue(),getBillModel());
    // HashMap mapRateMny =
    // m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(saCurrId);
    // BusinessCurrencyRateUtil bca =
    // m_cardPoPubSetUI2.getCurrArith_Busi(getCorp());
    for (int i = 0; i < iQueryLen; i++) {
      iRow = ((Integer) vecIndex.elementAt(i)).intValue();
      if (voRetPrice == null || voRetPrice.getPriceAt(i) == null) {
        // ��������ۿ۶�����ѯ�����������ú�˰����Ϊ1����ϸ�μ�����֧���н�����Ϊ0�������ۿ������Ĳɹ������������ɲɹ���Ʊ��
        sChangedKey = "norgtaxprice";
        if (PuPubVO.getUFDouble_ZeroAsNull(getBodyValueAt(iRow, sChangedKey)) == null) {
          String strBaseId = PuPubVO
              .getString_TrimZeroLenAsNull(getBodyValueAt(iRow, "cbaseid"));
          if (strBaseId != null
              && (PuTool.isLabor(strBaseId) || PuTool.isDiscount(strBaseId))) {
            setBodyValueAt(new UFDouble(1.0), iRow, sChangedKey);
            calRelation(new BillEditEvent(getBodyItem(sChangedKey)
                .getComponent(), getBodyValueAt(iRow, sChangedKey),
                sChangedKey, iRow));
          }
        }
        continue;
      }
      UFDouble dPrice = voRetPrice.getPriceAt(i);
      UFDouble dTaxRate = voRetPrice.getTaxRateAt(i);
      if (PuPubVO.getUFDouble_NullAsZero(dTaxRate).compareTo(new UFDouble(0)) > 0) {
        setBodyValueAt(dTaxRate, iRow, "ntaxrate");
      }
      if (voRetPrice.isSetPriceNoTaxAt(i)) {
        sChangedKey = "noriginalcurprice";
      }
      else {
        sChangedKey = OrderItemVO.getPriceFieldByPricePolicy(iPricePolicy);
      }

      // ��ԭ��ֵ��ͬ�����¼���
      dOldPrice = PuPubVO.getUFDouble_ValueAsValue(getBodyValueAt(iRow,
          sChangedKey));
      if (dOldPrice == null) {
        dOldPrice = new UFDouble(0);
      }
      //begin ncm zhaoypb �۸����������ɶ����޸�״̬ʱ���Ѵ��ڼ۸���ȡ���µļ۸�
      //PID:201105111551493083 CUS:�׸�ú��
      if (PuPubVO.getUFDouble_ZeroAsNull(dPrice) != null
//          && dPrice.compareTo(PuPubVO.getUFDouble_ValueAsValue(dOldPrice)) != 0) {
    		  && PuPubVO.getUFDouble_ZeroAsNull(dOldPrice) == null) {
        setBodyValueAt(dPrice, iRow, sChangedKey);
      }
      if ((PuPubVO.getUFDouble_NullAsZero(dTaxRate).compareTo(new UFDouble(0)) > 0)
          || (dPrice != null && 
        		  //dPrice.compareTo(PuPubVO.getUFDouble_ValueAsValue(dOldPrice)) != 0
        		  PuPubVO.getUFDouble_ZeroAsNull(dOldPrice) == null)) {
    	  //end ncm zhaoypb 2011-05-16
        // ���¼���������ϵ
        calRelation(new BillEditEvent(getBodyItem(sChangedKey).getComponent(),
            getBodyValueAt(iRow, sChangedKey), sChangedKey, iRow));
      }
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ���Ĭ�ϵļ۸�·�� �ú�����setCntRelated(int,int)���� ������ int iBeginRow
   * �����Ĭ�ϼ۸���Ϣ�ı��忪ʼ�� int iEndRow �����Ĭ�ϼ۸���Ϣ�ı�������� ���أ��� ���⣺�� ���ڣ�(2002-5-15
   * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-06-19 WYF ȥ������ 2002-11-19 WYF
   * ����һ�����󣬸ô�����iBeginRow==iEndRowʱ����ѭ��
   */
  private void setDefaultPrice(int iBeginRow, int iEndRow) {

    Vector vecRow = new Vector();
    for (int i = iBeginRow; i <= iEndRow; i++) {
      vecRow.add(new Integer(i));
    }
    setDefaultPrice((Integer[]) vecRow.toArray(new Integer[vecRow.size()]));
  }

  /**
   * ���ߣ�WYF ���ܣ������޶�������Ĭ��ѡ�� �������� ���أ� ���⣺ ���ڣ�(2003-9-9 19:42:01)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setDefaultReviseItems(OrderVO voOrder) {

    // �汾���Զ���1
    setHeadItem("nversion", new Integer(voOrder.getHeadVO().getNversion()
        .intValue() + 1));
    // �޶�������Ϊ��ǰ����
    setHeadItem("drevisiondate", PoPublicUIClass.getLoginDate());
    // �����޶���
    setTailItem("crevisepsn", ClientEnvironment.getInstance().getUser().getPrimaryKey());
    //
    setHeadItem("ccurrencytypeid", voOrder.getHeadVO().getCcurrencytypeid());
  }

  /**
   * ���ߣ���ӡ�� ���ܣ���ճ�� �������� ���أ��� ���⣺�� ���ڣ�(2001-5-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 2002-09-20 wyf �ָ��ԣ��ϲ㣩��ԴID����Դ��ID�ĸ��� 2002-11-25 wyf ��TS���� 2003-02-27 wyf
   * �޸�Ϊ�������и��Ƶ��н�����Ŀ��ռ��༭�Կ���
   */
  private void setDefaultValueToPastedLines(int iBeginRow, int iEndRow) {

    for (int iRow = iBeginRow; iRow <= iEndRow; iRow++) {
      // ��������ȱʡֵ
      setBodyValueAt(null, iRow, "corderid");
      setBodyValueAt(null, iRow, "corder_bid");

      /*
       * //���ݿ���Դ setBodyValueAt(null, iRow, "csourcebilltype");
       * setBodyValueAt(null, iRow, "csourcebillid"); setBodyValueAt(null, iRow,
       * "csourcerowid"); setBodyValueAt(null, iRow, "cupsourcebilltype");
       * setBodyValueAt(null, iRow, "cupsourcebillid"); setBodyValueAt(null,
       * iRow, "cupsourcebillrowid"); //������Դ setBodyValueAt(null, iRow,
       * "csourcebillname") ; setBodyValueAt(null, iRow, "csourcebillcode") ;
       * setBodyValueAt(null, iRow, "csourcebillrowno") ; setBodyValueAt(null,
       * iRow, "cancestorbillname") ; setBodyValueAt(null, iRow,
       * "cancestorbillcode") ; setBodyValueAt(null, iRow, "cancestorbillrowno")
       * ; //����������Դ setBodyValueAt(null, iRow, "cupsourcehts");
       * setBodyValueAt(null, iRow, "cupsourcebts");
       */
      // �ۼ�����
      setBodyValueAt(null, iRow, "naccumarrvnum");
      setBodyValueAt(null, iRow, "naccumstorenum");
      setBodyValueAt(null, iRow, "naccumwastnum");
      setBodyValueAt(null, iRow, "naccuminvoicenum");
      setBodyValueAt(null, iRow, "nconfirmnum");
      setBodyValueAt(null, iRow, "nbackarrvnum");
      setBodyValueAt(null, iRow, "nbackstorenum");
      setBodyValueAt(null, iRow, "naccumrpnum");

      setBodyValueAt(null, iRow, "ccorrectrowid");
      setBodyValueAt(null, iRow, "dconfirmdate");
      setBodyValueAt(null, iRow, "dcorrectdate");
      setBodyValueAt(null, iRow, "vvendorordercode");
      setBodyValueAt(null, iRow, "vvendororderrow");

      setBodyValueAt(null, iRow, "coperator");

      // �����ֶ���ȱʡֵ
      setBodyValueAt(OrderItemVO.FORDERROWSTATUS_FREE, iRow, "forderrowstatus");
      setBodyValueAt(OrderItemVO.IISACTIVE_ACTIVE, iRow, "iisactive");

      // setBodyValueAt(OrderItemVO.IISREPLENISH_NO,iRow,"iisreplenish");
      UFBoolean bReplenish = PuPubVO.getUFBoolean_NullAs(getHeadItem(
          "bisreplenish").getValue(), OrderHeaderVO.BISREPLENISH_NO);
      setBodyValueAt(
          bReplenish.equals(OrderHeaderVO.BISREPLENISH_NO) ? OrderItemVO.IISREPLENISH_NO
              : OrderItemVO.IISREPLENISH_YES, iRow, "iisreplenish");

      // V3����
      setBodyValueAt(OrderItemVO.BRECEIVEPLAN_NO, iRow, "breceiveplan");

      setBodyValueAt(null, iRow, "ts");

    }

  }

  /**
   * ���ߣ���ӡ�� ���ܣ��Ա�ע�ǿյ����ÿմ� �������� ���أ��� ���⣺�� ���ڣ�(2003-10-15 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 
   * ��ϵͳ��Ҫ���ÿմ����ܱ༭����ϵͳ����Ҫ����ֵ��since 5.6 modify by donggq
   */
//  private void setDefaultValueToVmemo() {
//    if (getHeadItem("vmemo").getComponent() instanceof UIRefPane) {
//      if (((UIRefPane) getHeadItem("vmemo").getComponent()).getText() == null) {
//        ((UIRefPane) getHeadItem("vmemo").getComponent()).setText("");
//      }
//    }
//    int iRowCount = getRowCount();
//    for (int i = 0; i < iRowCount; i++) {
//      if (getBodyValueAt(i, "vmemo") == null) {
//        setBodyValueAt("  ", i, "vmemo");
//      }
//    }
//  }

  /**
   * ���ߣ���־ƽ ���ܣ��Ա�ͷ����ġ���Ӧ���շ�����ַ�������塰�շ�����ַ���ǿյ����ÿմ� �������� ���أ��� ���⣺�� ���ڣ�(2004-11-17
   * 15:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 
   * ��ϵͳ��Ҫ���ÿմ����ܱ༭����ϵͳ����Ҫ����ֵ��since 5.6 modify by donggq
   */
//  private void setDefaultValueToAddr() {
//    if (((UIRefPane) getHeadItem("cdeliveraddress").getComponent()).getText() == null) {
//      ((UIRefPane) getHeadItem("cdeliveraddress").getComponent()).setText("");
//    }
//
//    int iRowCount = getRowCount();
//    for (int i = 0; i < iRowCount; i++) {
//      if (getBodyValueAt(i, "vvenddevaddr") == null) {
//        setBodyValueAt("  ", i, "vvenddevaddr");
//      }
//      if (getBodyValueAt(i, "vreceiveaddress") == null) {
//        setBodyValueAt("  ", i, "vreceiveaddress");
//      }
//    }
//  }

  /**
   * ���ߣ���־ƽ ���ܣ��Ա���ġ��շ�����ַ���ǿյ����ÿմ�,���ݱ�ͷ"��Ӧ���շ�����ַ"������"��Ӧ���շ�����ַ" �������� ���أ��� ���⣺��
   * ���ڣ�(2004-11-17 15:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setDefaultValueToAddrForCHG() {
    if (((UIRefPane) getHeadItem("cdeliveraddress").getComponent()).getText() == null) {
      ((UIRefPane) getHeadItem("cdeliveraddress").getComponent()).setText("");
    }

    int iRowCount = getRowCount();
    for (int i = 0; i < iRowCount; i++) {
//      if (getBodyValueAt(i, "vvenddevaddr") == null) {
//        setBodyValueAt("  ", i, "vvenddevaddr");
//      }
      if (getBodyValueAt(i, "vreceiveaddress") == null) {
        setBodyValueAt(((UIRefPane) getHeadItem("cdeliveraddress")
            .getComponent()).getText(), i, "vreceiveaddress");
      }
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��趨��ͬ��������Ƿ���޸� �������� ���أ��� ���⣺�� ���ڣ�(2002-5-13 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-05-17 wyf �Ӻ�ͬ�����ĵ��ݶ������ڲ����޸� 2002-05-29 wyf
   * �������ͬ�����Ϣ�ı���ı༭��������
   */
  private void setEnabled_Body(int iRow,boolean bCntRelated) {
    // =========�趨�ɱ༭��
    int iRowCount = getRowCount();
    if (iRowCount == 0) {
      return;
    }
    // ��ͬ��أ���ͬ���롢���ۡ����֣�
    if(bCntRelated){
      setEnabled_BodyCntRelated(iRow);
    }
    // ����
    setEnabled_BodyExchangeRate(iRow);
    // ���κ�
    PoEditTool.setCellEditable_VproduceNum(getBillModel(), iRow, "cmangid",
        "vproducenum");
    // ���������
    PoEditTool.setCellEditable_AssistUnitRelated(getBillModel(), iRow,
        new String[] {
            "cbaseid", "cassistunit", "cassistunitname", "nassistnum",
            "nconvertrate"
        });
    // ������
    // PoEditTool.setCellEditable_Vfree(getBillModel(), iRow,
    // "cmangid","vfree");
    // ��Ŀ�׶�
    setEnabled_BodyProjectPhase(iRow);
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��趨��ͬ��������Ƿ���޸� ������int iRow ���趨�ɱ༭�Ե��� ���أ��� ���⣺�� ���ڣ�(2002-5-13
   * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-05-17 wyf �Ӻ�ͬ�����ĵ��ݶ������ڲ����޸� 2002-05-29
   * wyf �������ͬ�����Ϣ�ı���ı༭�������� 2003-04-14 wyf �޸�û�д��ʱ�Ĵ���
   */
  private void setEnabled_BodyCntRelated(int iRow) {

    // �۸���������
    int iPricePolicy = PuTool.getPricePriorPolicy(getCorp());
    // �Ƿ�Ӻ�ͬ��
    String sUpSourceType = (String) getBodyValueAt(iRow, "cupsourcebilltype");
    String sCntRowId = (String) getBodyValueAt(iRow, "ccontractrowid");
    // �ϲ���Դ���Ǻ�ͬ
    if (sUpSourceType != null
        && (sUpSourceType.equals(BillTypeConst.CT_ORDINARY) || sUpSourceType
            .equals(BillTypeConst.CT_BEFOREDATE))) {

      setCellEditable(iRow, "ccontractcode", false);
      RetCtToPoQueryVO voCntInfo = PoPublicUIClass.getCntInfo(sCntRowId);
      // ���
      PoPublicUIClass.setInvEditableByPricePolicy(this, iRow, voCntInfo);
      // ����
      PoPublicUIClass.setPriceEditableByPricePolicy(getBillModel(), iRow,
          voCntInfo, iPricePolicy);
      // ����
      setCellEditable(iRow, "ccurrencytype", false);
    }
    else {
      // û�д��
      String sMangId = (String) getBodyValueAt(iRow, "cmangid");
      if (PuPubVO.getString_TrimZeroLenAsNull(sMangId) == null) {
        setCellEditable(iRow, "cinventorycode",
            getBodyItem("cinventorycode") != null
                && getBodyItem("cinventorycode").isEdit());
        setCellEditable(iRow, "ccontractcode", false);
        setCellEditable(iRow, "ccurrencytype",
            getBodyItem("ccurrencytype") != null
                && getBodyItem("ccurrencytype").isEdit());
        setCellEditable(iRow, "noriginalcurprice",
            getBodyItem("noriginalcurprice") != null
                && getBodyItem("noriginalcurprice").isEdit());
        setCellEditable(iRow, "norgtaxprice",
            getBodyItem("norgtaxprice") != null
                && getBodyItem("norgtaxprice").isEdit());
        return;
      }

      // =====����ɱ༭
      setCellEditable(iRow, "cinventorycode",
          getBodyItem("cinventorycode") != null
              && getBodyItem("cinventorycode").isEdit());

      // =====��ͬ���
      // �����ͬ���
      RetCtToPoQueryVO voCntInfo = PoPublicUIClass.getCntInfo(sCntRowId);
      if (voCntInfo == null) {
        setCellEditable(iRow, "ccontractcode",
            getBodyItem("ccontractcode") != null
                && getBodyItem("ccontractcode").isEdit());
        setCellEditable(iRow, "ccurrencytype",
            getBodyItem("ccurrencytype") != null
                && getBodyItem("ccurrencytype").isEdit());
        setCellEditable(iRow, "noriginalcurprice",
            getBodyItem("noriginalcurprice") != null
                && getBodyItem("noriginalcurprice").isEdit());
        setCellEditable(iRow, "norgtaxprice",
            getBodyItem("norgtaxprice") != null
                && getBodyItem("norgtaxprice").isEdit());
      }
      else {
        // ��ͬ����
        setCellEditable(iRow, "ccontractcode",
            getBodyItem("ccontractcode") != null
                && getBodyItem("ccontractcode").isEdit());
        // ����
        setCellEditable(iRow, "ccurrencytype", false);
        // ����
        PoPublicUIClass.setPriceEditableByPricePolicy(getBillModel(), iRow,
            voCntInfo, iPricePolicy);
      }
    }

  }

  /**
   * ���ߣ���ӡ�� ���ܣ��趨���ʵĿɱ༭�� ������int indRow ���趨�ɱ༭�Ե��� ���أ��� ���⣺�� ���ڣ�(2002-6-25
   * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setEnabled_BodyExchangeRate(int iRow) {

    // =========����
    String pk_corp = (String) getHeadItem("pk_corp").getValue();
    String sCurrId = (String) getBodyValueAt(iRow, "ccurrencytypeid");
    boolean[] baEditable = m_cardPoPubSetUI2.getBothExchRateEditable(pk_corp,
        sCurrId);

    // 2006-02-23 Czp V5�޸ģ������Զ���ģ���еĿɱ༭������
    boolean bEditUserDef0 = getBillModel().getItemByKey("nexchangeotobrate")
        .isEdit();

    if (baEditable != null) {
      getBillModel().setCellEditable(iRow, "nexchangeotobrate",
          baEditable[0] & bEditUserDef0);
    }
  }

  /**
   * ���ߣ�WYF ���ܣ�������Ŀ�׶εĿɱ༭�� ������int iRow ������ ���أ��� ���⣺�� ���ڣ�(2004-6-22 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setEnabled_BodyProjectPhase(int iRow) {

    // 2006-02-23 Czp V5�޸ģ������Զ���ģ���еĿɱ༭������
    boolean bEditUserDef = getBillModel().getItemByKey("cprojectphase")
        .isEdit();
    // ��Ŀ�׶�
    String sProjectId = (String) getBodyValueAt(iRow, "cprojectid");
    if (sProjectId == null || sProjectId.trim().equals("")) {
      getBillModel().setCellEditable(iRow, "cprojectphase", false);
    }
    else {
      getBillModel().setCellEditable(iRow, "cprojectphase", bEditUserDef);
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��趨��ͷ�Ŀɱ༭�� Ŀǰ�ú���ֻ����һ��������Ϊ�˱�����չ�� ���뺯��setEnabled_BodyCells���ֶԳ� ��������
   * ���أ��� ���⣺�� ���ڣ�(2002-6-25 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * <p>
   * 2006-05-22,Czp,V5֧���û����嵥��ģ���ϵġ��Ƿ���޶������ԵĶ���
   */
  private void setEnabled_Head(boolean bRev) {
      // �Ƿ�Ӻ�ͬ��
      boolean bFrmCnt = false;
      for (int i = 0; i < getRowCount(); i++) {
        String sUpSourceType = (String) getBodyValueAt(i, "cupsourcebilltype");
        // �ϲ���Դ���Ǻ�ͬ
        if (sUpSourceType != null
            && (sUpSourceType.equals(BillTypeConst.CT_ORDINARY) || sUpSourceType
                .equals(BillTypeConst.CT_BEFOREDATE))) {
          bFrmCnt = true;
          break;
        }
      }
    // since v501, ֧������״̬�ĵ��ݺ��޸�ʱ�ɱ༭(���û�����)
    if (bRev) {
      setEnabled_HeadOrderCode();
    }
    setEnabled_HeadCntRelated(bRev);
    setEnabled_HeadFreeCust(bRev);
    setEnabled_HeadExchangeRate(bRev);
    setEnabled_HeadAccount(bRev);
    setEnabled_HeadBizType();

    // ---------V31����:
    // �����������˻�
    getHeadItem("breturn").setEnabled(
        !isBisreplenish() && getHeadItem("breturn").isEdit());
    // Эͬ��־�����޸�
    if (getHeadItem("bcooptoso") != null) {
      getHeadItem("bcooptoso").setEnabled(false);
    }
    // Эͬ,��Ӧ�̲������޸�
    if (getHeadItem("vcoopordercode") != null
        && PuPubVO.getString_TrimZeroLenAsNull(getHeadItem("vcoopordercode")
            .getValue()) != null) {
      getHeadItem("cvendormangid").setEnabled(false);
    }
    else {
        getHeadItem("cvendormangid").setEnabled(!bFrmCnt);
      }
      if(bFrmCnt){
        getHeadItem("ccurrencytypeid").setEnabled(false);
        getHeadItem("nexchangeotobrate").setEnabled(false);
      }
    if (getHeadItem("bsocooptome") != null) {
      getHeadItem("bsocooptome").setEnabled(false);
    }
    if (getHeadItem("ntaxrate") != null) {
        getHeadItem("ntaxrate").setEnabled(true);
      }

  }

  /**
   * ���ߣ���ӡ�� ���ܣ��趨ɢ���Ŀɱ༭�� �������� ���أ��� ���⣺�� ���ڣ�(2002-6-25 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setEnabled_HeadAccount(boolean bRev) {
    // ��Ӧ�̼�ɢ�����ٴ���һ��
    String sVendId = ((UIRefPane) (getHeadItem("cvendormangid").getComponent()))
        .getRefPK();
    String sFreeCustId = ((UIRefPane) (getHeadItem("cfreecustid")
        .getComponent())).getRefPK();
    if (PuPubVO.getString_TrimZeroLenAsNull(sVendId) == null
        && PuPubVO.getString_TrimZeroLenAsNull(sFreeCustId) == null) {
      getHeadItem("caccountbankid").setEnabled(false);
    }
    else {
      if (bRev) {
        getHeadItem("caccountbankid").setEnabled(
            getHeadItem("caccountbankid").isM_bReviseFlag()
                && getHeadItem("caccountbankid").isEdit());
      }
      else {
        getHeadItem("caccountbankid").setEnabled(
            getHeadItem("caccountbankid").isEdit());
      }
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��趨ҵ�����͵Ŀɱ༭�� �������� ���أ��� ���⣺�� ���ڣ�(2003-10-17 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setEnabled_HeadBizType() {

    if (PuPubVO.getString_TrimZeroLenAsNull(getHeadItem("cbiztype").getValue()) == null
        || getHeadItem("cbiztype").isEdit()) {
      getHeadItem("cbiztype").setEnabled(true);
    }
    else {
      getHeadItem("cbiztype").setEnabled(false);
    }
  }

  /**
   * ���ߣ���־ƽ ���ܣ��жϡ��Ƿ񲹻��� �������� ���أ��� ���⣺�� ���ڣ�(2004-12-16 12:09:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private boolean isBisreplenish() {

    String strVal = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
        "bisreplenish").getValue());

    return (strVal != null && "true".equalsIgnoreCase(strVal));
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��趨��ͷ�������ڡ���Ӧ�̵Ŀɱ༭�� �������� ���أ��� ���⣺�� ���ڣ�(2002-6-25 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setEnabled_HeadCntRelated(boolean bRev) {

    // ����״̬    �Ƿ�Ӻ�ͬ��
    boolean bFrmCnt = false;
    boolean bRelatedCnt = false;
    for (int i = 0; i < getRowCount(); i++) {
      boolean bRowFrmCnt = false;
      boolean bRowRelatedCnt = false;
      String sUpSourceType = (String) getBodyValueAt(i, "cupsourcebilltype");
      String sCntrowid = (String) getBodyValueAt(i, "ccontractrowid");
      // �ϲ���Դ���Ǻ�ͬ
      if (!bFrmCnt && sUpSourceType != null
          && (sUpSourceType.equals(BillTypeConst.CT_ORDINARY) || sUpSourceType
              .equals(BillTypeConst.CT_BEFOREDATE))) {
        bFrmCnt = true;
        bRowFrmCnt = true;
      }else if (sUpSourceType != null
          && (sUpSourceType.equals(BillTypeConst.CT_ORDINARY) || sUpSourceType
              .equals(BillTypeConst.CT_BEFOREDATE))){
        bRowFrmCnt = true;
      }
      
      if (!bRelatedCnt && PuPubVO.getString_TrimZeroLenAsNull(sCntrowid) != null ) {
        bRelatedCnt = true;
        bRowRelatedCnt = true;
      }else if(PuPubVO.getString_TrimZeroLenAsNull(sCntrowid) != null){
        bRowRelatedCnt = true;
      }
      //�޶�״̬���к�ͬ�����������ñ��ֲ��ɱ༭ since 5.6 modify by donggq
      if((bRowFrmCnt || bRowRelatedCnt ) && bRev){
        setCellEditable(i, "ccurrencytype", false);
      }
      //�޶�״̬���к�ͬ�����������ú�ͬ�Ų��ɱ༭ since 5.6 modify by donggq
      if (bRowRelatedCnt && bRev){
        setCellEditable(i, "ccontractcode", false);
      }
    }
    // =========��ͷ ��Ӧ�̲��ɱ༭
    if (bRev) {
      getHeadItem("cvendormangid").setEnabled(
          !bRelatedCnt && !bFrmCnt && getHeadItem("cvendormangid").isM_bReviseFlag()
              && getHeadItem("cvendormangid").isEdit());
      getHeadItem("dorderdate").setEnabled(
          !bRelatedCnt && !bFrmCnt && getHeadItem("dorderdate").isM_bReviseFlag()
              && getHeadItem("dorderdate").isEdit());
    }
    else {
      getHeadItem("cvendormangid").setEnabled(
          !bFrmCnt && getHeadItem("cvendormangid").isEdit());
      getHeadItem("dorderdate").setEnabled(
          !bFrmCnt && getHeadItem("dorderdate").isEdit());
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��趨��ͷ���ʵĿɱ༭�� �������� ���أ��� ���⣺�� ���ڣ�(2002-6-25 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setEnabled_HeadExchangeRate(boolean bRev) {

    // =========����
    String pk_corp = (String) getHeadItem("pk_corp").getValue();
    String sCurrId = (String) getHeadItem("ccurrencytypeid").getValue();
    boolean[] baEditable = m_cardPoPubSetUI2.getBothExchRateEditable(pk_corp,
        sCurrId);
    if (baEditable != null) {
      if (bRev) {
        getHeadItem("nexchangeotobrate").setEnabled(
            baEditable[0] && getHeadItem("nexchangeotobrate").isM_bReviseFlag()
                && getHeadItem("nexchangeotobrate").isEdit());
      }
      else {
        getHeadItem("nexchangeotobrate").setEnabled(
            baEditable[0] && getHeadItem("nexchangeotobrate").isEdit());
      }
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��趨�������еĿɱ༭�� �������� ���أ��� ���⣺�� ���ڣ�(2003-10-15 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setEnabled_HeadFreeCust(boolean bRev) {
    // ��Ӧ�̲���
    UIRefPane ref = (UIRefPane) (getHeadItem("cvendormangid").getComponent());
    String cvendormangid = ref.getRefPK();
    // ɢ��
    String cfreecustid = getHeadItem("cfreecustid").getValue();
    if ((cvendormangid == null) || (cvendormangid.toString().trim().equals(""))) {
      ((UIRefPane) (getHeadItem("cfreecustid").getComponent())).setPK(null);
      getHeadItem("cfreecustid").setEnabled(false);
      if (cfreecustid != null) {
        getHeadItem("caccountbankid").setValue(null);
      }
    }
    else {
      try {
        PoVendorVO vendorVO = null;
        vendorVO = PoPublicUIClass.getVendDefaultInfo(cvendormangid);
        if (vendorVO == null) {
          ((UIRefPane) (getHeadItem("cfreecustid").getComponent())).setPK(null);
          getHeadItem("cfreecustid").setEnabled(false);
          if (cfreecustid != null) {
            getHeadItem("caccountbankid").setValue(null);
          }
        }
        else {
          String freecustflag = vendorVO.getFreecustflag();
          if ((freecustflag == null) || (freecustflag.trim().equals(""))
              || (freecustflag.trim().toUpperCase().equals("N"))) {
            ((UIRefPane) (getHeadItem("cfreecustid").getComponent()))
                .setPK(null);
            getHeadItem("cfreecustid").setEnabled(false);
            if (cfreecustid != null) {
              getHeadItem("caccountbankid").setValue(null);
            }
          }
          else {
            if (bRev) {
              getHeadItem("cfreecustid").setEnabled(
                  getHeadItem("cfreecustid").isM_bReviseFlag());
            }
            else {
              getHeadItem("cfreecustid").setEnabled(
                  getHeadItem("cfreecustid").isEdit());
            }
          }
        }
      }
      catch (Exception e) {
        ((UIRefPane) (getHeadItem("cfreecustid").getComponent())).setPK(null);
        getHeadItem("cfreecustid").setEnabled(false);
        if (cfreecustid != null) {
          getHeadItem("caccountbankid").setValue(null);
        }
      }
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��趨������ŵĿɱ༭�� �������� ���أ��� ���⣺�� ���ڣ�(2003-9-9 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setEnabled_HeadOrderCode() {
    String sId = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem("corderid")
        .getValue());
    boolean bEditFlag = getHeadItem("vordercode").isEdit();
    getHeadItem("vordercode").setEnabled(sId == null ? bEditFlag : false);
  }

  /**
   * ���ߣ���ӡ�� ���ܣ����ñ����۱����۸����ʵ�ֵ��������ɱ༭�� ������ int iRow boolean bResetExchValue
   * �Ƿ���Ҫ�������ñ����еĻ���ֵ ���أ��� ���⣺�� ���ڣ�(2002-5-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setExchangeRateBody(int iRow, boolean bResetExchValue) {

    String dOrderDate = getHeadItem("dorderdate").getValue();
    String sCurrId = (String) getBodyValueAt(iRow, "ccurrencytypeid");
    if (sCurrId == null || sCurrId.trim().length() == 0) {
      sCurrId = getHeadItem("ccurrencytypeid").getValue();
    }
    // ����������ʾ����
    setRowDigits_ExchangeRate(getCorp(), iRow, getBillModel(),
        m_cardPoPubSetUI2);
    // ֵ
//    UFDouble oldExchRate=PuPubVO.getUFDouble_NullAsZero(getBodyValueAt(iRow, 
//        "nexchangeotobrate"));
//    if ((bResetExchValue || 0.0==oldExchRate.doubleValue())
      if(bResetExchValue && dOrderDate != null && dOrderDate.trim().length() > 0) {
      UFDouble[] daRate = null;
      String strCurrDate = dOrderDate;
      if (strCurrDate == null || strCurrDate.trim().length() == 0) {
        strCurrDate = PoPublicUIClass.getLoginDate() + "";
      }
      daRate = m_cardPoPubSetUI2.getBothExchRateValue(getCorp(), sCurrId,
          new UFDate(dOrderDate));
      setBodyValueAt(daRate[0], iRow, "nexchangeotobrate");
    }
    // �ɱ༭��
    boolean[] baEditable = null;

    baEditable = m_cardPoPubSetUI2.getBothExchRateEditable(getCorp(), sCurrId);

    // 2006-02-23 Czp V5�޸ģ������Զ���ģ���еĿɱ༭������
    boolean bEditUserDef0 = getBillModel().getItemByKey("nexchangeotobrate")
        .isEdit();
    getBillModel().setCellEditable(iRow, "nexchangeotobrate",
        baEditable[0] & bEditUserDef0);

    // �����޸ı�־
    getBillModel().setRowState(iRow, BillModel.MODIFICATION);
  }

  /**
   * ���ߣ���ӡ�� ���ܣ����ñ�ͷ���ּ����ʵĿɱ༭�� �������� ���أ��� ���⣺�� ���ڣ�(2002-5-13 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setExchangeRateHead(String dorderdate, String sCurrId) {

    sCurrId = (sCurrId == null || sCurrId.trim().length() == 0) ? null
        : sCurrId;
    // ����������ʾ����
    int[] iaExchRateDigit = m_cardPoPubSetUI2.getBothExchRateDigit(getCorp(),
        sCurrId);
    
    if(!StringUtil.isEmptyWithTrim(sCurrId)){
      getHeadItem("nexchangeotobrate").setDecimalDigits(iaExchRateDigit[0]);
    }
    
    // ����ֵ
    UFDouble[] daRate = m_cardPoPubSetUI2.getBothExchRateValue(getCorp(),
        sCurrId, new UFDate(dorderdate));
    getHeadItem("nexchangeotobrate").setValue(daRate[0]);
    // �ɱ༭��
    boolean[] iaEditable = m_cardPoPubSetUI2.getBothExchRateEditable(getCorp(),
        sCurrId);
    getHeadItem("nexchangeotobrate").setEnabled(
        iaEditable[0] && getHeadItem("nexchangeotobrate").isEdit());


  }

  /**
   * ���ߣ���ӡ�� ���ܣ����ñ�ͷ���� ������String pk_corp ��˾ID ���أ��� ���⣺�� ���ڣ�(2003-9-4 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setHeadDigits(String pk_corp) {
    // Ԥ�����޶Ԥ����
    int iDigit = 2;
    try {
      iDigit = m_cardPoPubSetUI2.getMoneyDigitByCurr_Finance(CurrParamQuery
          .getInstance().getLocalCurrPK(pk_corp));
    }
    catch (Exception e) {
      PuTool.outException(this, e);
    }

    // ���Ҳ��񾫶Ƚ����
    int iLen = OrderHeaderVO.getDbMnyFields_Local_Finance().length;
    for (int i = 0; i < iLen; i++) {
      BillItem item = getHeadItem(OrderHeaderVO.getDbMnyFields_Local_Finance()[i]);
      if (item != null) {
        item.setDecimalDigits(iDigit);
      }
    }

    // �汾
    BillItem item3 = getHeadItem("nversion");
    if (item3 != null) {
      item3.setDecimalDigits(1);
    }
  }

  /***************************************************************************
   * �����Ƿ�������Ƶ��˲��������Ϊ��Y�����޸ģ������޸��Ƶ���Ϊ��ǰ��¼�Ĳ���Ա
   **************************************************************************/
  private void setOperatorValue() {
    if (!BillTypeConst.PO_ORDER.equals(getBillType())) {
      SCMEnv.out("��ά�����������޸��Ƶ���!");/* -=notranslate=- */
      return;
    }
    String strParaVal = null;
    try {
      strParaVal = SysInitBO_Client.getParaString(PoPublicUIClass
          .getLoginPk_corp(), "PO060");
    }
    catch (Exception e) {
      SCMEnv.out("��ȡ����ʱ�����쳣�����޸��Ƶ���!");/* -=notranslate=- */
      return;
    }
    if ("Y".equalsIgnoreCase(strParaVal)) {
      SCMEnv.out("�Ƿ�������Ƶ��˲���Ϊ��Y�������޸��Ƶ���!");/* -=notranslate=- */
      return;
    }
    // �޸��Ƶ���
    setTailItem("coperator", PoPublicUIClass.getLoginUser());
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�����ĳ�е���Ŀ��Ϣ����һ������ ������int iCurRow ��������Ŀ��Ϣ���� int iRefRow �ο��� ���أ���
   * ���⣺�� ���ڣ�(2003-05-03 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setProjectInfoAs(int iCurRow, int iRefRow) {

    // if (iCurRow<0 || iRefRow<0 ) {
    // return ;
    // }

    String sPreviousProject = (String) getBillModel().getValueAt(iRefRow,
        "cprojectid");
    String sCurProject = (String) getBillModel().getValueAt(iCurRow,
        "cprojectid");
    sPreviousProject = (sPreviousProject == null
        || sPreviousProject.trim().equals("") ? sPreviousProject = "NULL"
        : sPreviousProject);
    sCurProject = (sCurProject == null || sCurProject.trim().equals("") ? sCurProject = "NULL"
        : sCurProject);
    if (sCurProject.equals(sPreviousProject)) {
      getBillModel().setValueAt(
          getBillModel().getValueAt(iRefRow, "cprojectphaseid"), iCurRow,
          "cprojectphaseid");
      getBillModel().setValueAt(getBillModel().getValueAt(iRefRow, "cproject"),
          iCurRow, "cproject");
      getBillModel().setValueAt(
          getBillModel().getValueAt(iRefRow, "cprojectphase"), iCurRow,
          "cprojectphase");
    }
  }

  /**
   * ���ߣ�WYF ���ܣ����ò��յĿ����ԡ� �������� ���أ��� ���⣺�� ���ڣ�(2003-10-08 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setRefPane() {

    setRefPane_Head();
    setRefPane_Body();
    setRefPane_Tail();
  }

  /**
   * ���ߣ�WYF ���ܣ����ò��յĿ����ԡ� �������� ���أ��� ���⣺�� ���ڣ�(2003-10-08 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setRefPane_Body() {

    // ==================��ͷ
    int iLen = getRefItemKeysBody().length;
    for (int i = 0; i < iLen; i++) {
      setRefPane_Body(getRefItemKeysBody()[i]);
    }
  }

  /**
   * ���ߣ�WYF ���ܣ����ò��յĿ����ԡ� �������� ���أ��� ���⣺�� ���ڣ�(2003-10-08 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setRefPane_Body(String sItemKey) {

    // ==================����
    UIRefPane refpane = null;

    if ("cinventorycode".equals(sItemKey)) {
      // ���
      refpane = (UIRefPane) getBodyItem("cinventorycode").getComponent();
      refpane.setTreeGridNodeMultiSelected(true);
      refpane.setMultiSelectedEnabled(true);
      refpane.setRefModel(new PoInputInvRefModel(getCorp(), null));
    }
    else if ("cassistunitname".equals(sItemKey)) {
      refpane = (UIRefPane) getBodyItem("cassistunitname").getComponent();
      refpane.setRefModel(new OtherRefModel("��������λ"));/* -=notranslate=- */
    }
    else if ("ccurrencytype".equals(sItemKey)) {
      refpane = (UIRefPane) getBodyItem("ccurrencytype").getComponent();
      AbstractRefModel refModel = refpane.getRefModel();
      refModel.setPk_corp(getCorp());
    }
    else if ("cusedept".equals(sItemKey)) {
      // ����
      refpane = (UIRefPane) getBodyItem("cusedept").getComponent();
      AbstractRefModel refModel = refpane.getRefModel();
      refModel.setPk_corp(getCorp());
    }
    else if ("cproject".equals(sItemKey)) {
      // ��Ŀ
      JobmngfilDefaultRefModel refModel = new JobmngfilDefaultRefModel("��Ŀ������");
      refModel.setPk_corp(getCorp());
      refModel
          .addWherePart(" AND UPPER(ISNULL(bd_jobbasfil.sealflag,'N')) = 'N'");
      refpane = (UIRefPane) getBodyItem("cproject").getComponent();
      refpane.setRefModel(refModel);
    }
    else if ("cprojectphase".equals(sItemKey)) {
      refpane = (UIRefPane) getBodyItem("cprojectphase").getComponent();
      refpane.setRefModel(new PuProjectPhaseRefModel(getCorp()));
      //refpane.getRefModel().setWherePart(refpane.getRefModel().getWherePart())
      // ;
    }
    /*
     * V5���ű༭ǰ���� else if ("cwarehouse".equals(sItemKey)) { //���òֿ���գ����˵���Ʒ��
     * PuTool.restrictWarehouseRefByStoreOrg(this, getCorp(), getHeadItem(
     * "cstoreorganization").getValue(), "cwarehouse"); }
     */
    else if ("vmemo".equals(sItemKey)) {
      // ��ע
      ComAbstrDefaultRefModel refModelCom = new ComAbstrDefaultRefModel("����ժҪ");
      refModelCom.setPk_corp(getCorp());
      // refModel.setRefNodeName("����ժҪ");/*-=notranslate=-*/

      refpane = (UIRefPane) getBodyItem("vmemo").getComponent();
      refpane.setRefModel(refModelCom);

      // //��ע
      // DefaultRefModel refModel = new DefaultRefModel();
      // refModel.setPk_corp(getCorp());
      // refModel.setRefNodeName("����ժҪ");/*-=notranslate=-*/
      //
      // refpane = (UIRefPane) getBodyItem("vmemo").getComponent();
      // refpane.setRefModel(refModel);
    }
    // �շ�����ַ:�������ջ���
    else if ("vreceiveaddress".equals(sItemKey)) {
      String sReveivePk = ((UIRefPane) (getHeadItem("creciever").getComponent()))
          .getRefPK();
      refpane = (UIRefPane) getBodyItem("vreceiveaddress").getComponent();
      refpane.setRefModel(new PoReceiveAddrRefModel(getCorp(), PuPubVO
          .getString_TrimZeroLenAsNull(sReveivePk)));
    }
    // �����շ�����ַ:�����ڹ�Ӧ��
    else if ("vvenddevaddr".equals(sItemKey)) {
      String sVendorMangPk = ((UIRefPane) (getHeadItem("cvendormangid")
          .getComponent())).getRefPK();
      refpane = (UIRefPane) getBodyItem("vvenddevaddr").getComponent();
      refpane.setRefModel(new PoReceiveAddrRefModel(getCorp(), PuPubVO
          .getString_TrimZeroLenAsNull(sVendorMangPk)));
    }
    // else if ("cdevaddrname".equals(sItemKey)) {
    // String sVendorMangPk = ((UIRefPane) (getHeadItem("cvendormangid")
    // .getComponent())).getRefPK();
    // refpane = (UIRefPane) getBodyItem("cdevaddrname").getComponent();
    // refpane.setRefModel(new PodevaddressRefModel( PuPubVO
    // .getString_TrimZeroLenAsNull(sVendorMangPk)));
    // }
    if (refpane != null) {
      refpane.setPk_corp(getCorp());
    }
  }

  /**
   * ���ߣ�WYF ���ܣ����ñ�ͷ���� �������� ���أ��� ���⣺�� ���ڣ�(2003-10-08 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setRefPane_Head() {

    // ==================��ͷ
    int iLen = getRefItemKeysHead().length;
    for (int i = 0; i < iLen; i++) {
      setRefPane_Head(getRefItemKeysHead()[i]);
    }
  }

  /**
   * ���ߣ�WYF ���ܣ����ñ�ͷ���� �������� ���أ��� ���⣺�� ���ڣ�(2003-10-08 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setRefPane_Head(String sItemKey) {

    // ==================��ͷ
    UIRefPane refpane = null;
    AbstractRefModel refModel = null;
    if ("cprojectid".equals(sItemKey)) {
      // ��Ŀ cprojectid
      refpane = (UIRefPane) getHeadItem("cprojectid").getComponent();
      refModel = refpane.getRefModel();
      refModel.setPk_corp(getCorp());
      refModel
          .addWherePart(" AND UPPER(ISNULL(bd_jobbasfil.sealflag,'N')) = 'N'");

    }
    else if ("ctermprotocolid".equals(sItemKey)) {
      // ����Э�� ctermprotocolid
      refpane = (UIRefPane) getHeadItem("ctermprotocolid").getComponent();
      refModel = refpane.getRefModel();
      refModel.setPk_corp(getCorp());
    }
    else if ("ctransmodeid".equals(sItemKey)) {
      // ���˷�ʽ ctransmodeid
      refpane = (UIRefPane) getHeadItem("ctransmodeid").getComponent();
      refModel = refpane.getRefModel();
      if (refModel != null) {
        refModel.setPk_corp(getCorp());
        refModel.setRefNodeName("���䷽ʽ");/* -=notranslate=- */
      }
    }
    /* V5 Del: */
    // else if ("cstoreorganization".equals(sItemKey)) {
    // //�����֯ cstoreorganization
    // DefaultRefModel refModel = new DefaultRefModel();
    // refModel.setPk_corp(getCorp());
    // refModel.setRefNodeName("�����֯");/*-=notranslate=-*/
    //
    // refpane = (UIRefPane) getHeadItem("cstoreorganization")
    // .getComponent();
    // refpane.setRefModel(refModel);
    // }
    else if ("cpurorganization".equals(sItemKey)) {
      // �ɹ���֯ cpurorganization
      refpane = (UIRefPane) getHeadItem("cpurorganization").getComponent();
      refpane.setAutoCheck(true);
      refModel = refpane.getRefModel();
      refModel.setPk_corp(getCorp());
      refModel.addWherePart(" and ownercorp = '"
          + PoPublicUIClass.getLoginPk_corp() + "' ");
    }
    else if ("cbiztype".equals(sItemKey)) {
      // ҵ������ pk_corp
      refpane = (UIRefPane) getHeadItem("cbiztype").getComponent();
      // refpane.setRefModel(new PuBizTypeRefModel(getCorp(),
      // BillTypeConst.PO_ORDER));
      Object[] oRule = null;
      String sBiztype = refpane.getRefPK();
      if (sBiztype == null) {
        refpane.setRefModel(new PuBizTypeRefModel(PoPublicUIClass
            .getLoginPk_corp(), BillTypeConst.PO_ORDER));
        return;
      }
      try {
        oRule = (Object[]) CacheTool.getCellValue("bd_busitype", "pk_busitype",
            "verifyrule", sBiztype);
      }
      catch (Exception ee) {
        SCMEnv.out(ee);
      }
      // ҵ������ֱ��
      if (oRule != null && oRule.length > 0
          && "Z".equalsIgnoreCase(oRule[0].toString())) {
        if ((refpane.getRefModel() instanceof PuBizTypeRefModel)) {
          // refpane.setRefModel(new PuBizTypeRefModel(PoPublicUIClass
          // .getLoginPk_corp(), BillTypeConst.PO_ORDER, true));
          ((PuBizTypeRefModel) refpane.getRefModel()).setM_bDirectly(true);
        }
      }
      else {
        // ҵ�����ͷ�ֱ��
        if ((refpane.getRefModel() instanceof PuBizTypeRefModel)) {
          // refpane.setRefModel(new PuBizTypeRefModel(PoPublicUIClass
          // .getLoginPk_corp(), BillTypeConst.PO_ORDER, false));
          ((PuBizTypeRefModel) refpane.getRefModel()).setM_bDirectly(false);
        }
      }
    }
    else if ("ccurrencytypeid".equals(sItemKey)) {
      // ����

      refpane = (UIRefPane) getHeadItem("ccurrencytypeid").getComponent();
      refModel = refpane.getRefModel();
      refModel.setPk_corp(getCorp());
    }
    else if ("cunfreeze".equals(sItemKey)) {
      // �ⶳ�� cunfreeze
      refpane = (UIRefPane) getHeadItem("cunfreeze").getComponent();
      refModel = refpane.getRefModel();
      refModel.setPk_corp(getCorp());
    }
    else if ("pk_corp".equals(sItemKey)) {
      // ��˾ pk_corp
      refpane = (UIRefPane) getHeadItem("pk_corp").getComponent();
      getHeadItem("pk_corp").setLength(100);
      refpane.setAutoCheck(true);
      refModel = refpane.getRefModel();
      refModel.setPk_corp(getCorp());
    }
    else if ("caccountbankid".equals(sItemKey)) {
      // ����
      refpane = (UIRefPane) getHeadItem("caccountbankid").getComponent();
      if (PuPubVO.getString_TrimZeroLenAsNull(getHeadItem("cfreecustid")
          .getValue()) != null) {
        refpane.setRefModel(new PoFreeCustBankRefModel(getHeadItem(
            "cfreecustid").getValue()));
      }
      else {
        String pkcorp = getCorp();
        String vendorbaseid = (String) getHeadItem("cvendorbaseid").getValue();
        if (refpane.getRefModel() != null) {
          refpane
              .getRefModel()
              .addWherePart(
                  " and bd_bankaccbas.pk_bankaccbas in (select  k.pk_accbank from bd_custbank k,bd_cumandoc m   where   m.pk_corp ='"
                      + pkcorp
                      + "'  and k.pk_cubasdoc=m.pk_cubasdoc and k.pk_cubasdoc='"
                      + vendorbaseid + "')");
        }
      }
    }
    else if ("cfreecustid".equals(sItemKey)) {
      // //ɢ��
    }
    else if ("cgiveinvoicevendor".equals(sItemKey)) {
      // ��Ʊ��
      refpane = (UIRefPane) getHeadItem("cgiveinvoicevendor").getComponent();
      refModel = refpane.getRefModel();
      refModel.setPk_corp(getCorp());
      refModel.setRefNameField("bd_cubasdoc.custshortname");
    }
    else if ("cvendormangid".equals(sItemKey)) {
      // ��Ӧ��
      refpane = (UIRefPane) getHeadItem("cgiveinvoicevendor").getComponent();
      refModel = refpane.getRefModel();
      refModel.setPk_corp(getCorp());
      refModel.setRefNameField("bd_cubasdoc.custshortname");
    }
    else if ("creciever".equals(sItemKey)) {
      // �ջ���
      refpane = (UIRefPane) (getHeadItem("creciever").getComponent());
      refModel = refpane.getRefModel();
      refModel.setPk_corp(getCorp());
      refModel.setRefNameField("bd_cubasdoc.custshortname");
    }
    else if ("cdeptid".equals(sItemKey)) {
      // ����
      refpane = (UIRefPane) getHeadItem("cdeptid").getComponent();
      refpane.setRefModel(new PurDeptRefModel(getCorp()));
    }
    else if ("cemployeeid".equals(sItemKey)) {
      // ҵ��Ա
      /*
       * �������õ������⣺¼��Ǳ�����ҵ��Աʱ��������Ӧ���� refpane = (UIRefPane)
       * getHeadItem("cemployeeid").getComponent(); refpane.setRefModel(new
       * PurPsnRefModel(getCorp(), getHeadItem( "cdeptid").getValue()));
       */
      refpane = (UIRefPane) getHeadItem("cemployeeid").getComponent();
      String cemployeeid = refpane.getRefPK();
      refpane.setRefModel(new PurPsnRefModel(getCorp(), null));
      refpane.setPK(cemployeeid);

    }
    else if ("vmemo".equals(sItemKey)) {
      // ��ע
      ComAbstrDefaultRefModel refModelCom = new ComAbstrDefaultRefModel("����ժҪ");
      refModelCom.setPk_corp(getCorp());
      // refModel.setRefNodeName("����ժҪ");/*-=notranslate=-*/
      if ((getHeadItem("vmemo").getComponent() instanceof UIRefPane)) {
        refpane = (UIRefPane) getHeadItem("vmemo").getComponent();
        refpane.setRefModel(refModelCom);
        refpane.setButtonVisible(true);
      }
      // //��ע
      // refpane = new UIRefPane();
      // refpane.setRefNodeName("����ժҪ");/*-=notranslate=-*/
      // refModel = refpane.getRefModel();
      // refModel.setPk_corp(getCorp());
      // getHeadItem("vmemo").setComponent(refpane);
      // refpane.setButtonVisible(true);
    }
    else if ("cdeliveraddress".equals(sItemKey)) {
      // ��Ӧ���շ�����ַ
      refpane = (UIRefPane) getHeadItem("cdeliveraddress").getComponent();
      refpane.setRefModel(new PoDeliverAddressRefModel(getHeadItem(
          "cvendorbaseid").getValue()));
    }
    if (refpane != null && refpane.getRefModel() != null) {
      refpane.getRefModel().setPk_corp(getCorp());
    }
  }

  /**
   * ���ߣ�WYF ���ܣ����ñ�β���� �������� ���أ��� ���⣺�� ���ڣ�(2003-10-14 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setRefPane_Tail() {

    // ==================��ͷ
    UIRefPane refpane = null;

    // if ("cauditpsn".equals(sItemKey)) {
    // ������ coperator
    refpane = (UIRefPane) getTailItem("cauditpsn").getComponent();
    // DefaultRefModel refModelAuditPsn = new DefaultRefModel();
    AbstractRefModel refModelAuditPsn = refpane.getRefModel();
    refModelAuditPsn.setPk_corp(getCorp());
    // refModelAuditPsn.setRefNodeName("����Ա");/*-=notranslate=-*/
    // refpane.setRefModel(refModelAuditPsn);
    // }else if ("coperator".equals(sItemKey)) {
    // �Ƶ��� coperator
    // DefaultRefModel refModelOper = new DefaultRefModel();
    // refModelOper.setRefNodeName("����Ա");/*-=notranslate=-*/

    refpane = (UIRefPane) getTailItem("coperator").getComponent();
    AbstractRefModel refModelOper = refpane.getRefModel();
    refModelOper.setPk_corp(getCorp());
    // refpane.setRefModel(refModelOper);
    // }

    if (refpane != null) {
      refpane.setPk_corp(getCorp());
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�������������ڡ������޸ĺ���Ӧ������޼۱仯 �ú�����afterEdit������������ڡ����ָı����á� ������ int
   * iBeginRow ������ͬ�����Ϣ�ı��忪ʼ�� int iEndRow ������ͬ�����Ϣ�ı�������� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2003-11-14 wyf ��������
   */
  private void setRelated_AssistUnit(int iBeginRow, int iEndRow) {

    // ��������
    String[] saBaseId = (String[]) PuGetUIValueTool.getArrayNotNull(
        getBillModel(), "cbaseid", String.class, iBeginRow, iEndRow);
    PuTool.loadBatchAssistManaged(saBaseId);

    // ����������
    Vector vecAssistUnitIndex = new Vector();
    Vector vecBaseId = new Vector();
    Vector vecAssistId = new Vector();

    // ����ֵ

    // ����Ĭ�ϸ�����
    String[] aryAssistunit = new String[] {
        // "<formulaset><cachetype>FOREDBCACHE</cachetype></formulaset>"
        // +
        "cassistunit->getColValue(bd_invbasdoc,pk_measdoc2,pk_invbasdoc,cbaseid)",
        "cassistunitname->getColValue(bd_measdoc,measname,pk_measdoc,cassistunit)"
    };
    getBillModel().execFormulas(aryAssistunit, iBeginRow, iEndRow);
    //
    String sBaseId = null;
    String sAssistUnit = null;
    for (int iRow = iBeginRow; iRow <= iEndRow; iRow++) {
      sBaseId = (String) getBodyValueAt(iRow, "cbaseid");
      if (PuTool.isAssUnitManaged(sBaseId)) {
        sAssistUnit = (String) getBodyValueAt(iRow, "cassistunit");
        // Ϊ����������׼��
        if (PuPubVO.getString_TrimZeroLenAsNull(sAssistUnit) != null) {
          vecAssistUnitIndex.add(new Integer(iRow));
          vecBaseId.add(sBaseId);
          vecAssistId.add(sAssistUnit);
        }
      }
      else {
        getBillModel().setValueAt(null, iRow, "cassistunitname");
        getBillModel().setValueAt(null, iRow, "cassistunit");
        getBillModel().setValueAt(null, iRow, "nassistnum");
        getBillModel().setValueAt(null, iRow, "nconvertrate");
      }
    }

    // �������ø���������
    int iAssistUnitLen = vecAssistUnitIndex.size();
    if (iAssistUnitLen > 0) {

      // ��������
      PuTool.loadBatchInvConvRateInfo((String[]) vecBaseId
          .toArray(new String[iAssistUnitLen]), (String[]) vecAssistId
          .toArray(new String[iAssistUnitLen]));

      // String[] saCurrId =
      // getAllCurrIdFromCard(getRowCount(),getCorp(),getHeadItem(
      // "ccurrencytypeid").getValue(),getBillModel());
      // HashMap mapRateMny =
      // m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(saCurrId);
      // BusinessCurrencyRateUtil bca =
      // m_cardPoPubSetUI2.getCurrArith_Busi(getCorp());

      // ѭ��ִ��
      int iRow = 0;
      for (int i = 0; i < iAssistUnitLen; i++) {
        iRow = ((Integer) vecAssistUnitIndex.get(i)).intValue();

        Object[] oConvRate = PuTool.getInvConvRateInfo((String) vecBaseId
            .get(i), (String) vecAssistId.get(i));
        if (oConvRate == null) {
          getBillModel().setValueAt(null, iRow, "nconvertrate");
        }
        else {
          getBillModel().setValueAt((UFDouble) oConvRate[0], iRow,
              "nconvertrate");
        }

        // �����ʸı䣬���¼���
        BillEditEvent tempE = new BillEditEvent(getBodyItem("nconvertrate"),
            getBodyValueAt(iRow, "nconvertrate"), "nconvertrate", iRow);
        calRelation(tempE);
      }
    }

    // ���ÿɱ༭��
    // setEnabled_BodyAssistUnitRelated(iBeginRow,iEndRow) ;

  }

  /**
   * ���ߣ���ӡ�� ���ܣ���Ӧ�̡���������������޸ĺ���Ӧ�ĺ�ͬ�仯 �ú�����afterEdit��Ӧ�̡�������������ڸı����á� ������Integer[]
   * iaRow �����õ����� boolean bMustSetCnt �Ƿ����ȡ��ֵͬ ���ô˷��������ñ�ֵ���磺����ѡ��
   * �˺�ͬ���գ���Ϊtrue������Ϊfalse ���أ��� ���⣺�� ���ڣ�(2003-10-30 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setRelated_CntNew4V56(Integer[] iaRow, boolean bMustSetCnt,
      boolean bPasteFlag) {

    String pk_corp = getHeadItem("pk_corp").getValue();
    boolean isCTenable = false;
    // ��ͬδ����,��Ĭ��
    if(m_hmProdEnable.containsKey(pk_corp+ScmConst.m_sModuleCodeCT)){
      isCTenable = m_hmProdEnable.get(pk_corp+ScmConst.m_sModuleCodeCT).booleanValue();
    }else{
      isCTenable = PuTool.isProductEnabled(pk_corp, ScmConst.m_sModuleCodeCT);
    }
    if (!isCTenable) {
      setDefaultPrice(iaRow);
      return;
    }
    // ��Ʒ��������ͬ���ֹ����պ�ͬ���⣩
    if (!bMustSetCnt) {
      iaRow = setDefaultPriceAndGetLargessLines(iaRow);
    }
    if (iaRow == null || iaRow.length == 0) {
      SCMEnv.out("������Ʒ�о�Ϊ��Ʒ��ֱ�ӷ��أ�����������ͬ����");/* -=notranslate=- */
      return;
    }
    // ������и��ƺ��ճ������ѯ��ͬ
    if (bPasteFlag) {
      iaRow = getNoSourceCtLines(iaRow);
    }
    if (iaRow == null || iaRow.length == 0) {
      SCMEnv.out("�����о�ΪZ2���գ�ֱ�ӷ��أ�����������ͬ����");/* -=notranslate=- */
      return;
    }
    // Ѱ����غ�ͬ��Ϣ
    Vector<RetCtToPoQueryVO> vRetCTInfo = findCntInfoForRow(iaRow);
//    RetCtToPoQueryVO[] voaCnt = null;
    if (!bMustSetCnt && vRetCTInfo != null) {
      int iLen = vRetCTInfo.size();
//      voaCnt = vRetCTInfo.toArray(new RetCtToPoQueryVO[iLen]);
      if (iLen > 0) {
          int iRet = MessageDialog.showYesNoDlg(this, nc.ui.ml.NCLangRes
              .getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                                            * @res
                                                                            * "��ʾ"
                                                                            */,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200",
                  "UPP40040200-000034")/*
                                        * @res "Ѱ�ҵ���ͬ���Ƿ������"
                                        */);
          if (iRet == MessageDialog.ID_NO) {
          vRetCTInfo = null;
        }
      }
    }

    // ==============��ͬ
    // ��Ӧ�̹���ID
    String sVendId = getHeadItem("cvendormangid").getValue();
    sVendId = PuPubVO.getString_TrimZeroLenAsNull(sVendId);
    // ����
    String sDate = getHeadItem("dorderdate").getValue();
    sDate = PuPubVO.getString_TrimZeroLenAsNull(sDate);

    // ��ʱ����
    int iTotalLen = iaRow.length;
    int iRow = 0;
    String sInvId = null;
    String sChangedKey = null;
    UFDouble dPrice = null;
    UFDouble dOldPrice = null;

    // ����ȡ�Ǻ�ͬ�۵��м��ı��KEY
    Vector vecPoPriceRow = new Vector();

    // �۸���������
    int iPricePolicy = PuTool.getPricePriorPolicy(pk_corp);
    RetCtToPoQueryVO[] cntListVO = null;
    RetCtToPoQueryVO[] vos = null;
    RetCtToPoQueryVO[] voCtInfo = null;
    String sBaseid = null;
    HashMap hHeadData = null;
    String sRowNo = null;
    ArrayList listRow = new ArrayList();
    ArrayList listId = new ArrayList();
    ArrayList listRowQP = new ArrayList();
    ArrayList listIdQP = new ArrayList();

    boolean bOpenDialog = false;
    cntListVO = null;
//    Vector<RetCtToPoQueryVO> vecData = new Vector<RetCtToPoQueryVO>();
//    HashMap<String, RetCtToPoQueryVO> hData = new HashMap<String, RetCtToPoQueryVO>();
    if (vRetCTInfo != null) {
      Vector<String> vRowno = new Vector<String>();
      for (RetCtToPoQueryVO ctToPoVO : vRetCTInfo) {
        if(vRowno.contains(ctToPoVO.getAttributeValue("crowno"))){
            bOpenDialog = true;
        }else{
          vRowno.add((String)ctToPoVO.getAttributeValue("crowno"));
          }
//        vecData.add(ctToPoVO);
          }
        }
    if (vRetCTInfo != null && vRetCTInfo.size() > 1) {
      if (bOpenDialog) {
        RetCtToPoQueryVO[] cntListVOs = new RetCtToPoQueryVO[vRetCTInfo.size()];
        vRetCTInfo.copyInto(cntListVOs);
        if (cntSelDlg == null) {
          cntSelDlg = new CntSelDlg(this);
        }
        // ÿ��ˢ�¶�λ����
        cntSelDlg.resetLocateHash();
        cntSelDlg.setOnOK(false);
        cntSelDlg.setCntDatas(cntListVOs);
        cntSelDlg.showModal();
        if (cntSelDlg.isON_OK())
          voCtInfo = cntSelDlg.getRetVOs();
        else
          voCtInfo = null;
      }
      else {
        voCtInfo = new RetCtToPoQueryVO[vRetCTInfo.size()];
        vRetCTInfo.copyInto(voCtInfo);
      }
    }
    else if(vRetCTInfo != null) {
      voCtInfo = new RetCtToPoQueryVO[vRetCTInfo.size()];
      vRetCTInfo.copyInto(voCtInfo);
    }
    RetCtToPoQueryVO[] voCtInfos = new RetCtToPoQueryVO[iTotalLen];
    if ((voCtInfo == null || voCtInfo.length == 0)) {
      for (int i = 0; i < iaRow.length; i++) {
        setCellEditable(iaRow[i], "noriginalcurprice", true);
        setCellEditable(iaRow[i], "norgtaxprice", true);
        setCellEditable(iaRow[i], "ccurrencytype", true);
        setCellEditable(iaRow[i], "noriginalcurprice", true);
        setCellEditable(iaRow[i], "norgtaxprice", true);
      }
      ((PoToftPanel) m_conInvoker).showHintMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000281")/*û��ѡ�������ͬ����*/);
    }

    if (voCtInfo != null && voCtInfo.length > 0 && vRetCTInfo.size() > 1) {
      HashMap<String,RetCtToPoQueryVO> hmCT = new HashMap<String, RetCtToPoQueryVO>();
      for (int k = 0; k < voCtInfo.length; k++) {
        hmCT.put((String)voCtInfo[k].getAttributeValue("crowno"), voCtInfo[k]);
      }
        for (int i = 0; i < iaRow.length; i++) {
          String strRowNo = (String) getBodyValueAt(iaRow[i].intValue(), "crowno");
        voCtInfos[i] = hmCT.get(strRowNo);
          }
        }
//    }
    else if (voCtInfo != null && voCtInfo.length > 0 && vRetCTInfo.size() > 0) {
      voCtInfos[0] = voCtInfo[0];
    }
    int iRowCount = getRowCount();
    for (int i = 0; i < iTotalLen; i++) {

      iRow = -1;
      for (int ii = 0; ii < iRowCount; ii++) {
        String sRowNoOnPanel = PuPubVO
            .getString_TrimZeroLenAsNull(getBodyValueAt(ii, "crowno"));
        if (sRowNoOnPanel != null
            && voCtInfos[i] != null
            && sRowNoOnPanel.equalsIgnoreCase(PuPubVO
                .getString_TrimZeroLenAsNull(voCtInfos[i]
                    .getAttributeValue("crowno")))) {
          iRow = ii;
          break;
        }

      }
      if (iRow == -1) {
        vecPoPriceRow.add(iaRow[i]);
        continue;
      }

      // �������ID
      sInvId = PuPubVO.getString_TrimZeroLenAsNull((String) getBodyValueAt(
          iRow, "cmangid"));

      if (sInvId == null || sDate == null) {
        // �Ƿ�ɱ༭
        setCellEditable(iRow, "ccontractcode", false);
        setCellEditable(iRow, "ccurrencytype",
            getBodyItem("ccurrencytype") != null
                && getBodyItem("ccurrencytype").isEdit());
        setCellEditable(iRow, "noriginalcurprice",
            getBodyItem("noriginalcurprice") != null
                && getBodyItem("noriginalcurprice").isEdit());
        setCellEditable(iRow, "norgtaxprice",
            getBodyItem("norgtaxprice") != null
                && getBodyItem("norgtaxprice").isEdit());
        // ��ͬID ��ͬ��ID ��ͬ�� ����
        setBodyValueAt(null, iRow, "ccontractid");
        setBodyValueAt(null, iRow, "ccontractrowid");
        setBodyValueAt(null, iRow, "ccontractcode");

        continue;
      }

      // �ĸ�����ITEM���޸�
      sChangedKey = null;
      dPrice = null;
      dOldPrice = null;

      // ��ͬ��ʼ�տɱ༭
      setCellEditable(iRow, "ccontractcode",
          getBodyItem("ccontractcode") != null
              && getBodyItem("ccontractcode").isEdit());


      // ��ͬID ��ͬ��
      if (voCtInfos[i] == null) {
        setBodyValueAt(null, iRow, "ccontractrowid");
        setBodyValueAt(null, iRow, "ccontractid");
        setBodyValueAt(null, iRow, "ccontractcode");
      }
      else {
        setBodyValueAt(voCtInfos[i].getCContractID(), iRow, "ccontractid");
        setBodyValueAt(voCtInfos[i].getCContractRowId(), iRow, "ccontractrowid");
        setBodyValueAt(voCtInfos[i].getCContractCode(), iRow, "ccontractcode");

        // since v50, xy,ljf���Ӵ��������ż۷���,�繫˾��������룬����ȡֵ����ѭ��������ִ��
        if (PuPubVO.getString_TrimZeroLenAsNull(voCtInfos[i]
            .getQPBaseSchemeID()) != null
            && PoPublicUIClass.getLoginPk_corp().equals(
                voCtInfos[i].getPk_Corp())) {
          setBodyValueAt(voCtInfos[i].getQPBaseSchemeID(), iRow,
              "cqpbaseschemeid");
          listRowQP.add(new Integer(iRow));
          listIdQP.add(voCtInfos[i].getQPBaseSchemeID());
        }
      }
UFDouble oldTaxRate = null;
      // ����ID
      if (voCtInfos[i] == null) {
        // �Ǻ�ͬ���ֿɱ༭
        setCellEditable(iRow, "ccurrencytype",
            getBodyItem("ccurrencytype") != null
                && getBodyItem("ccurrencytype").isEdit());
      }
      else {
        setBodyValueAt(voCtInfos[i].getCCurrencyId(), iRow, "ccurrencytypeid");
      //begin ncm linsf 201008261327513739 ��Ӫ����_˰�ʱ仯����ѯ��
        oldTaxRate = getBodyValueAt(iRow, "ntaxrate")==null?null:(UFDouble)getBodyValueAt(iRow, "ntaxrate");
      //end ncm linsf 201008261327513739 ��Ӫ����_˰�ʱ仯����ѯ��
        setBodyValueAt(voCtInfos[i].getTaxration(), iRow, "ntaxrate");
        // execBodyFormula(iRow, "ccurrencytype"); v31Ч���Ż� �ŵ�ѭ����ִ��
        listRow.add(new Integer(iRow));
        if (!listId.contains(voCtInfos[i].getCCurrencyId())) {
          listId.add(voCtInfos[i].getCCurrencyId());
        }
        // ��ͬ���ֲ��ɱ༭
        setCellEditable(iRow, "ccurrencytype", false);
        // ���ñ������:���ʡ����ȵ�
        setExchangeRateBody(iRow, true);
      }

      // �۸�
      if (voCtInfos[i] != null) {
        dPrice = OrderPubVO.getPriceValueByPricePolicy(voCtInfos[i],
            iPricePolicy);
        sChangedKey = OrderItemVO.getPriceFieldByPricePolicy(iPricePolicy);
        if (dPrice != null) {
          dOldPrice = PuPubVO.getUFDouble_ValueAsValue(getBodyValueAt(iRow,
              sChangedKey));
          setBodyValueAt(dPrice, iRow, sChangedKey);
        }
      }

      if (voCtInfos[i] != null && dPrice != null) {
        // ���ü۸�Ŀɱ༭��
        PoPublicUIClass.setPriceEditableByPricePolicy(getBillModel(), iRow,
            voCtInfos[i], iPricePolicy);

        // ���¼���������ϵ��ֻ��ֵ��ͬ��ԭֵʱ�����¼���
        if (dOldPrice == null || dPrice.compareTo(dOldPrice) != 0
        //begin ncm linsf 201008261327513739 ��Ӫ����_˰�ʱ仯����ѯ��
        		|| (oldTaxRate!=null&&oldTaxRate.compareTo(voCtInfos[i].getTaxration())!=0)) {
        	//end ncm linsf 201008261327513739 ��Ӫ����_˰�ʱ仯����ѯ��
          // ѯ���۸�ʱ�������¼���
          if (sChangedKey == null) {
            sChangedKey = OrderItemVO.getPriceFieldByPricePolicy(iPricePolicy);
          }
          calRelation(new BillEditEvent(
              getBodyItem(sChangedKey).getComponent(), getBodyValueAt(iRow,
                  sChangedKey), sChangedKey, iRow));
        }
      }
      else {
        setCellEditable(iRow, "noriginalcurprice",
            getBodyItem("noriginalcurprice") != null
                && getBodyItem("noriginalcurprice").isEdit());
        setCellEditable(iRow, "norgtaxprice",
            getBodyItem("norgtaxprice") != null
                && getBodyItem("norgtaxprice").isEdit());

        // ���뵽������ѯ��
        String sCurrId = PuPubVO
            .getString_TrimZeroLenAsNull((String) getBodyValueAt(iRow,
                "ccurrencytypeid"));
        Object oBRate = getBodyValueAt(iRow, "nexchangeotobrate");
        if (sCurrId == null && oBRate == null) {
          continue;
        }
        if (!vecPoPriceRow.contains(iaRow[i])) {
          // ������Ѱ��Ĭ�ϼ۵���
          vecPoPriceRow.add(iaRow[i]);
        }
        continue;
      }
      // �öδ����ֹ˫����ͬ�Ų��յ�TEXT����ʱ����ͬ����յ�����
      UIRefPane pane = ((UIRefPane) getBodyItem("ccontractcode").getComponent());
      pane.setPK(getBodyValueAt(iRow, "ccontractrowid"));
    }
    // ����ִ����ʾ�������ƹ�ʽ
    // ccurrencytype->getColValue(bd_currtype,currtypename,pk_currtype,
    // ccurrencytypeid)
    if (listRow.size() > 0 && listId.size() > 0) {
      Hashtable hashIdName = new Hashtable();
      int iSizeId = listId.size();
      try {
        Object[][] objs = PubHelper.queryArrayValue("bd_currtype",
            "pk_currtype", new String[] {
              "currtypename"
            }, (String[]) listId.toArray(new String[listId.size()]));
        if (objs != null && objs.length == iSizeId) {
          for (int i = 0; i < iSizeId; i++) {
            if (objs[i] == null || objs[i].length == 0) {
              continue;
            }
            hashIdName.put(listId.get(i), objs[i][0]);
          }
          int iSizeRow = listRow.size();
          Object oValId = null;
          Object oValName = null;
          int iPos = 0;
          for (int i = 0; i < iSizeRow; i++) {
            iPos = ((Integer) listRow.get(i)).intValue();
            oValId = getBillModel().getValueAt(iPos, "ccurrencytypeid");
            oValName = (String) hashIdName.get(oValId);
            getBillModel().setValueAt(oValName, iPos, "ccurrencytype");
          }
        }
      }
      catch (Exception e) {
        SCMEnv.out("��ȡ��������ʱ����!" + e.getMessage());
      }
    }

    // since v50, xy,ljf���Ӵ��������ż۷���,����ȡֵ����ѭ��������ִ��
    //cqpbaseschemename->getColValue(scm_qpbasescheme,vschemename,cqpbaseschemeid
    // ,cqpbaseschemeid)
    if (listRowQP.size() > 0 && listIdQP.size() > 0) {
      Hashtable hashIdName = new Hashtable();
      int iSizeId = listIdQP.size();
      try {
        Object[][] objs = PubHelper.queryArrayValue("scm_qpbasescheme",
            "cqpbaseschemeid", new String[] {
              "vschemename"
            }, (String[]) listIdQP.toArray(new String[listIdQP.size()]));
        if (objs != null && objs.length == iSizeId) {
          for (int i = 0; i < iSizeId; i++) {
            if (objs[i] == null || objs[i].length == 0) {
              continue;
            }
            hashIdName.put(listIdQP.get(i), objs[i][0]);
          }
          int iSizeRow = listRowQP.size();
          Object oValId = null;
          Object oValName = null;
          int iPos = 0;
          for (int i = 0; i < iSizeRow; i++) {
            iPos = ((Integer) listRowQP.get(i)).intValue();
            oValId = getBillModel().getValueAt(iPos, "cqpbaseschemeid");
            oValName = (String) hashIdName.get(oValId);
            getBillModel().setValueAt(oValName, iPos, "cqpbaseschemename");
          }
        }
      }
      catch (Exception e) {
        SCMEnv.out("��ȡ�����ż۷�������ʱ����!" + e.getMessage());
      }
    }

    int iPoPriceLen = vecPoPriceRow.size();
    if (iPoPriceLen == 0) {
      return;
    }
    // ����Ĭ�ϼ�
    setDefaultPrice((Integer[]) vecPoPriceRow.toArray(new Integer[iPoPriceLen]));
  }
  /**
   * ���ߣ���ӡ�� ���ܣ���Ӧ�̡���������������޸ĺ���Ӧ�ĺ�ͬ�仯 �ú�����afterEdit��Ӧ�̡�������������ڸı����á� ������ int
   * iBeginRow ������ͬ�����Ϣ�ı��忪ʼ�� int iEndRow ������ͬ�����Ϣ�ı�������� boolean bMustSetCnt
   * �Ƿ����ȡ��ֵͬ ���ô˷��������ñ�ֵ���磺����ѡ�� �˺�ͬ���գ���Ϊtrue������Ϊfalse ���أ��� ���⣺�� ���ڣ�(2002-3-13
   * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-05-27 ��ӡ�� �����������غ�ͬ���еĴ�������֧�ֲ��յĶ�ѡ
   * 2002-05-28 ��ӡ�� �޸�PoPublicUIClass.addCntInfo(String,Object[])�Ŀ�ָ�����
   * 2002-06-20 ��ӡ�� �ú�ͬ��Ϊʼ�տɱ༭
   */
  public void setRelated_Cnt(int iBeginRow, int iEndRow, boolean bMustSetCnt,
      boolean bPasteFlag) {
    if(!isSetCnt){
      return;
    }
    Vector vecRow = new Vector();
    for (int i = iBeginRow; i <= iEndRow; i++) {
        String sCntRowId = (String) getBodyValueAt(i, "ccontractrowid");
        if (PuPubVO.getString_TrimZeroLenAsNull(sCntRowId) == null) {
        	vecRow.add(new Integer(i));
        }
    }
    setRelated_CntNew4V56((Integer[]) vecRow.toArray(new Integer[vecRow.size()]),
        bMustSetCnt, bPasteFlag);
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�������������ڡ������޸ĺ���Ӧ������޼۱仯 �ú�����afterEdit������������ڡ����ָı����á� ������ int
   * iBeginRow ������ͬ�����Ϣ�ı��忪ʼ�� int iEndRow ������ͬ�����Ϣ�ı�������� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setRelated_MaxPrice(int iBeginRow, int iEndRow) {

    PoPublicUIClass.loadCardMaxPrice(this, getHeadItem("pk_corp").getValue(),
        iBeginRow, iEndRow, m_cardPoPubSetUI2);
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�����޸ĺ���Ӧ��˰�ʱ仯 �ú�����afterEdit������������ڡ����ָı����á� ������ int iBeginRow
   * �����˰�������Ϣ�ı��忪ʼ�� int iEndRow �����˰�������Ϣ�ı�������� ���أ��� ���⣺�� ���ڣ�(2003-11-3
   * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setRelated_Taxrate(int iBeginRow, int iEndRow) {

    nc.vo.scm.pu.Timer timeDebug = new nc.vo.scm.pu.Timer();
    timeDebug.start();

    HashMap mapId = new HashMap();
    Vector vecRow = new Vector();
    for (int i = iBeginRow; i <= iEndRow; i++) {
      String sBaseId = PuPubVO.getString_TrimZeroLenAsNull((String) getBodyValueAt(i, "cbaseid"));
      String sCcontractcode = PuPubVO.getString_TrimZeroLenAsNull((String) getBodyValueAt(i, "ccontractcode"));
      
      if (sBaseId != null && sCcontractcode == null) {
        mapId.put(sBaseId, "");
        vecRow.add(new Integer(i));
      }
    }
    int iSize = vecRow.size();
    if (iSize == 0) {
      return;
    }
    timeDebug.addExecutePhase("�õ�����ѯ��ID");/* -=notranslate=- */

    // ��������˰��
    PuTool.loadBatchTaxrate((String[]) mapId.keySet()
        .toArray(new String[iSize]), getHeadItem("pk_corp").getValue());

    timeDebug.addExecutePhase("��������˰��");/* -=notranslate=- */

    // ����
    int iRow = 0;
    String sBaseId = null;
    UFDouble dCurTaxRate = null;

    // String[] saCurrId =
    //getAllCurrIdFromCard(getRowCount(),getCorp(),getHeadItem("ccurrencytypeid"
    // ).getValue(),getBillModel());
    // HashMap mapRateMny =
    // m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(saCurrId);
    // BusinessCurrencyRateUtil bca =
    // m_cardPoPubSetUI2.getCurrArith_Busi(getCorp());
    for (int i = 0; i < iSize; i++) {
      iRow = ((Integer) vecRow.get(i)).intValue();

      sBaseId = (String) getBodyValueAt(iRow, "cbaseid");

      // UFDouble dOldTaxRate =
      //PuPubVO.getUFDouble_NullAsZero(getBillModel().getValueAt(iRow,"ntaxrate"
      // ));
      dCurTaxRate = PuTool.getInvTaxRate(sBaseId);
      if (dCurTaxRate != null) {
        setBodyValueAt(dCurTaxRate, iRow, "ntaxrate");
        // ˰�ʸı䣬���㣨���������ۣ���˰���ۣ���˰�ʣ�˰���˰�ϼ�,���ʣ����ʵ��ۣ�֮��Ĺ�ϵ
        BillEditEvent tempE = new BillEditEvent(getBodyItem("ntaxrate"),
            dCurTaxRate, "ntaxrate", iRow);
        calRelation(tempE);
      }
    }
    timeDebug.addExecutePhase("���¼����ϵ");/* -=notranslate=- */
    timeDebug.showAllExecutePhase("����˰��");/* -=notranslate=- */

  }

  /**
   * ���ߣ�WYF ���ܣ������޶�����Ŀɱ༭�� �������� ���أ��� ���⣺�� ���ڣ�(2004-5-27 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * <p>
   * 2006-05-22,Czp,V5,֧�ֵ���ģ��ġ��Ƿ���޶�������
   */
  private void setReviseEnable_Body(int iRow) {

    // OrderItemVO[] voaItem = voOrder.getBodyVO() ;
    BillItem[] bitemaBody = getBodyItems();
    int iBodyItemLen = bitemaBody.length;
    // �رյ���
    if (!isRowActive(iRow)) {
      for (int i = 0; i < iBodyItemLen; i++) {
        setCellEditable(iRow, bitemaBody[i].getKey(), false);
      }
    }
    // δ�ر�-�к�����������
    else if (isRowHaveAfterBill(iRow)) {
      for (int i = 0; i < iBodyItemLen; i++) {
        if (m_listFixedFields.contains(bitemaBody[i].getKey())) {
          setCellEditable(iRow, bitemaBody[i].getKey(), false);
        }
        else {
          setCellEditable(iRow, bitemaBody[i].getKey(), bitemaBody[i]
              .isM_bReviseFlag());
        }
      }
    }
    // δ�ر�-�޺�����������
    else {
      for (int i = 0; i < iBodyItemLen; i++) {
        setCellEditable(iRow, bitemaBody[i].getKey(), bitemaBody[i]
            .isM_bReviseFlag());
      }
    }
  }

  /**
   * ���ߣ�WYF ���ܣ������޶��ı�ͷ�Ŀɱ༭�� �������� ���أ��� ���⣺�� ���ڣ�(2003-9-8 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setReviseEnabledCells_Head(OrderVO voOrder) {

    // -------------��������һ����ִ��ʱ����ͷ��Ŀ�����޸ġ����˴�ִ�дӷ�����ʼ��
    // boolean bBodyExeced =
    // voOrder.isHaveOperationFrom(OrderstatusVO.STATUS_SENDOUT) ;
    boolean bBodyExeced = isHaveAfterBill(voOrder.getHeadVO().getCorderid());

    // ��ͷ������
    BillItem[] baHeadItem = getHeadItems();
    int iHLen = baHeadItem.length;

    if (bBodyExeced) {
      // ��������һ����ִ��ʱ����ͷ��Ŀ�����޸ġ����˴�ִ�дӷ�����ʼ��
      for (int i = 0; i < iHLen; i++) {
        baHeadItem[i].setEnabled(false);
      }
    }
    else {
      // -------------���
      // ���ǰ���������š��������ڡ�ҵ�����͡���˾��Ĭ�����⣬������ɸ�
      // ����󣬳�Ĭ�������Ӧ�̡�ɢ�����⣬������ɸġ�

      // //��ͷ�ɱ༭
      for (int i = 0; i < iHLen; i++) {
        baHeadItem[i].setEnabled(baHeadItem[i].isM_bReviseFlag()
            && baHeadItem[i].isEdit());
      }

      // boolean bOutputed =
      // voOrder.isHaveOperationFrom(OrderstatusVO.STATUS_OUTPUT) ;
      boolean bOutputed = false;
      if (bBodyExeced
          || voOrder.getHeadVO().getForderstatus().equals(
              nc.vo.scm.pu.BillStatus.OUTPUT)) {
        bOutputed = true;
      }
      // ����������Ƿ�ı����
      String[] saDecidedItem = new String[] {
          "cvendormangid", "cfreecustid"
      };
      int iDecidedLen = saDecidedItem.length;
      for (int i = 0; i < iDecidedLen; i++) {
        getHeadItem(saDecidedItem[i]).setEnabled(
            !bOutputed && getHeadItem(saDecidedItem[i]).isM_bReviseFlag()
                && getHeadItem(saDecidedItem[i]).isEdit());
      }

      // �������ñ�ͷ�Ŀɱ༭��
      // �˴���ǰ��������г�ͻ���ɷ��ٶԽṹ�����Ż�
      setEnabled_Head(true);
    }

    // �޶�����ʼ�տ���+�û�����
    getHeadItem("drevisiondate").setEnabled(
        getHeadItem("drevisiondate").isM_bReviseFlag()
            && getHeadItem("drevisiondate").isEdit());
    // ʼ�ղ��ı����
    String[] saUnChangedItem = new String[] {
        "bisreplenish", "bislatest", "vordercode", "dorderdate", "pk_corp",
        "nversion", "cbiztype", "nprepaymny", "breturn"
    };
    int iUnChangedLen = saUnChangedItem.length;
    for (int i = 0; i < iUnChangedLen; i++) {
      getHeadItem(saUnChangedItem[i]).setEnabled(false);
    }

  }

  // /**
  // * ���ߣ����� ���ܣ������۱����۸�����С��λ ������String pk_corp ��˾ID int iflag ���أ��� ���⣺��
  // * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-05-20 wyf
  // * �޸�ԭ��ȡ��󾫶�Ϊȡ�����־��� wyf add/modify/delete 2002-05-20 begin/end
  // */
  // protected static void setRowDigits_ExchangeRate(String sPk_corp, int
  // iRow,
  // BillModel billModel,HashMap mapRate) {
  // setRowDigits_ExchangeRate(sPk_corp,iRow,billModel,mapRate);
  // }
  /**
   * ���ߣ����� ���ܣ������۱����۸�����С��λ ������String pk_corp ��˾ID int iflag ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-05-20 wyf �޸�ԭ��ȡ��󾫶�Ϊȡ�����־���
   * wyf add/modify/delete 2002-05-20 begin/end
   */
  protected static void setRowDigits_ExchangeRate(String sPk_corp, int iRow,
      BillModel billModel, POPubSetUI2 setUi) {
    // ȡ�ñ���
    String sCurrId = (String) billModel.getValueAt(iRow, "ccurrencytypeid");
	//��������б���Ϊ�գ�����Ϊ��λ��
	String sLocalCurr  = null ;
	try {
		//����
		sLocalCurr = CurrParamQuery.getInstance().getLocalCurrPK(sPk_corp);
	} catch (Exception	e2) {
	}

    if(sCurrId==null)sCurrId = sLocalCurr;
    int[] iaExchRateDigit = null;
    // ��˰���˰�ϼ�
    iaExchRateDigit = setUi.getBothExchRateDigit(sPk_corp, sCurrId);
    if (iaExchRateDigit == null || iaExchRateDigit.length == 0) {
      billModel.getItemByKey("nexchangeotobrate").setDecimalDigits(2);
    }
    else {
      billModel.getItemByKey("nexchangeotobrate").setDecimalDigits(
          iaExchRateDigit[0]);
    }
  }

  /**
   * ���ߣ�WYF ���ܣ������еĽ��С��λ ������String pk_corp ��˾ID ���أ��� ���⣺�� ���ڣ�(2005-5-16
   * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  protected static void setRowDigits_Mny(String sPk_corp, int iRow,
      BillModel billModel, POPubSetUI2 setUi) {

    int iMnyDigit = 2;
    int iMnyDigitLocal = 2;
    int iMnyDigitAssi = 2;

    // ȡ�ñ���
    String sCurrId = (String) billModel.getValueAt(iRow, "ccurrencytypeid");
    int[] iaDigits = null;

    try {
      // if(setUi.getCurrArith_Busi(sPk_corp).isBlnLocalFrac()){
      // iaDigits = setUi.getMoneyDigitByCurr_Busi_Batch(new
      //String[]{sCurrId,setUi.getCurrArith_Busi(sPk_corp).getLocalCurrPK(),setUi
      // .getCurrArith_Busi(sPk_corp).getFracCurrPK()});
      // if(iaDigits != null && iaDigits.length >= 3){
      // iMnyDigit = iaDigits[0];
      // iMnyDigitLocal = iaDigits[1];
      // iMnyDigitAssi = iaDigits[2];
      // }
      // }else{
      iaDigits = setUi.getMoneyDigitByCurr_Busi_Batch(new String[] {
          sCurrId, CurrParamQuery.getInstance().getLocalCurrPK(sPk_corp)
      });
      if (iaDigits != null && iaDigits.length >= 2) {
        iMnyDigit = iaDigits[0];
        iMnyDigitLocal = iaDigits[1];
      }
      // }
    }
    catch (Exception e) {
      PuTool.outException(e);
      return;
    }
    // ԭ�ҽ����
    int iLen = OrderItemVO.getDbMnyFields_Org_Busi().length;
    for (int i = 0; i < iLen; i++) {
      BillItem item = billModel.getItemByKey(OrderItemVO
          .getDbMnyFields_Org_Busi()[i]);
      if (item != null) {
        item.setDecimalDigits(iMnyDigit);
      }
    }
    // ���ҽ����
    iLen = OrderItemVO.getDbMnyFields_Local_Busi().length;
    for (int i = 0; i < iLen; i++) {
      BillItem item = billModel.getItemByKey(OrderItemVO
          .getDbMnyFields_Local_Busi()[i]);
      if (item != null) {
        item.setDecimalDigits(iMnyDigitLocal);
      }
    }
    // ���ҽ����
    // try {
    // if (setUi.getCurrArith_Busi(sPk_corp).isBlnLocalFrac()) {
    // iLen = OrderItemVO.getDbMnyFields_Assist_Busi().length;
    // for (int i = 0; i < iLen; i++) {
    // BillItem item =
    // billModel.getItemByKey(OrderItemVO.getDbMnyFields_Assist_Busi()[i]);
    // if (item != null) {
    // item.setDecimalDigits(iMnyDigitAssi);
    // }
    // }
    // }
    // } catch (Exception e) {
    // PuTool.outException(e);
    // return;
    // }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�����ճ���е�ֵ ������int iBeginRow ��ʼ�� int iEndRow ������ ���أ��� ���⣺��
   * ���ڣ�(2004-06-10 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setValueToPastedLines(int iBeginRow, int iEndRow) {

    // ����Ĭ��ֵ
    setDefaultValueToPastedLines(iBeginRow, iEndRow);

    // ѯ��
    setRelated_Cnt(iBeginRow, iEndRow, false, true);

    // ��ע
//    setDefaultValueToVmemo();
//
//    // �շ�����ַ
//    setDefaultValueToAddr();

  }

  /**
   * @���ܣ�������Դ��Ϊ��ͬ��������,��Ҫ���Ǹ����и�����Դ���������ͬ��һ����
   * @���ߣ���־ƽ
   * @since v50
   */
  private Integer[] getNoSourceCtLines(Integer[] iaRow) {
    Object objUpSrcBillType = null;
    int iLen = iaRow == null ? 0 : iaRow.length;
    ArrayList listNotCntIndex = new ArrayList();
    for (int i = 0; i < iLen; i++) {
      if (iaRow[i] == null) {
        SCMEnv.out(new Exception("���ù�����Դ��Ϊ��ͬ������������ʱ����������ڿ�Ԫ��!"));/*
                                                                  * -=notranslate
                                                                  * =-
                                                                  */
        continue;
      }
      objUpSrcBillType = getBodyValueAt(iaRow[i].intValue(),
          "cupsourcebilltype");
      //
      if (objUpSrcBillType != null
          && "Z2".equalsIgnoreCase(objUpSrcBillType.toString().trim())) {
        continue;
      }
      listNotCntIndex.add(iaRow[i]);
    }
    if (listNotCntIndex.size() == 0) {
      return null;
    }
    return (Integer[]) listNotCntIndex.toArray(new Integer[listNotCntIndex
        .size()]);
  }

  /**
   * ���ܣ�������Ʒ������ ���ߣ���־ƽ
   */
  private Integer[] setDefaultPriceAndGetLargessLines(Integer[] iaRow) {
    Object objLargess = null;
    Object objPrice = null;
    int iLen = iaRow == null ? 0 : iaRow.length;
    ArrayList listNotCntIndex = new ArrayList();
    for (int i = 0; i < iLen; i++) {
      if (iaRow[i] == null) {
        SCMEnv.out(new Exception("���ù�����Ʒ����������ʱ����������ڿ�Ԫ��!"));/* -=notranslate=- */
        continue;
      }
      objLargess = getBodyValueAt(iaRow[i].intValue(), "blargess");
      if (objLargess == null || "false".equalsIgnoreCase(objLargess.toString())) {
        listNotCntIndex.add(iaRow[i]);
      }
      else {
        // ����
        objPrice = getBodyValueAt(iaRow[i].intValue(), "noriginalcurprice");
        if (objPrice == null || objPrice.toString().trim().length() == 0) {
          setBodyValueAt(new UFDouble(0.0), iaRow[i].intValue(),
              "noriginalcurprice");
        }
        // ������
        objPrice = getBodyValueAt(iaRow[i].intValue(), "noriginalnetprice");
        if (objPrice == null || objPrice.toString().trim().length() == 0) {
          setBodyValueAt(new UFDouble(0.0), iaRow[i].intValue(),
              "noriginalnetprice");
        }
        // ������˰������
        objPrice = getBodyValueAt(iaRow[i].intValue(), "nprice");
        if (objPrice == null || objPrice.toString().trim().length() == 0) {
          setBodyValueAt(new UFDouble(0.0), iaRow[i].intValue(), "nprice");
        }
        // ���Һ�˰������
        objPrice = getBodyValueAt(iaRow[i].intValue(), "ntaxprice");
        if (objPrice == null || objPrice.toString().trim().length() == 0) {
          setBodyValueAt(new UFDouble(0.0), iaRow[i].intValue(), "ntaxprice");
        }
        // ��˰����
        objPrice = getBodyValueAt(iaRow[i].intValue(), "norgtaxprice");
        if (objPrice == null || objPrice.toString().trim().length() == 0) {
          setBodyValueAt(new UFDouble(0.0), iaRow[i].intValue(), "norgtaxprice");
        }
        // ����˰����
        objPrice = getBodyValueAt(iaRow[i].intValue(), "norgnettaxprice");
        if (objPrice == null || objPrice.toString().trim().length() == 0) {
          setBodyValueAt(new UFDouble(0.0), iaRow[i].intValue(),
              "norgnettaxprice");
        }
      }
    }
    if (listNotCntIndex.size() == 0) {
      return null;
    }
    return (Integer[]) listNotCntIndex.toArray(new Integer[listNotCntIndex
        .size()]);
  }

  /**
   * ���ߣ���־ƽ ���ܣ�������ͬ������VO�����ݿ�Ƭ���� ������OrderVO voReloading, ���������VO(����)��OrderVO
   * voCurr ���л�����VO(��ˢ��) ���أ��� ���⣺�� ���ڣ�2005-4-14 10:24:16 �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public void reloadBillCardAfterAudit(OrderVO voReloading, OrderVO voCurr) {

    // ͬ����ͷ��{ʱ�����״̬�������ˡ���������}

    // ��ǰVO
    if (voCurr == null || voCurr.getHeadVO() == null
        || voCurr.getBodyVO() == null) {
      SCMEnv.out("����ͬ���ĵ�ǰ����VOΪ�ջ�VO��ͷΪ�ջ����Ϊ��!������Ϣ������Ա�ο���\n");
      SCMEnv.out(new Exception());
      MessageDialog.showHintDlg(getContainer(), NCLangRes.getInstance().getStrByID("4004020201", "UPPSCMCommon-000270")/*��ʾ*//* notranslate */,
          NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000282")/*����ͬ�������ݴ������⣬���ֹ����ˢ�µ�ǰ����*//* notranslate */);
      return;
    }
    if (voReloading.getHeadVO() != null) {
      OrderHeaderVO headCurr = voCurr.getHeadVO();
      OrderHeaderVO headReloading = voReloading.getHeadVO();
      // VO��ͷ
      headCurr.setTs(headReloading.getTs());
      headCurr.setForderstatus(headReloading.getForderstatus());
      headCurr.setCauditpsn(headReloading.getCauditpsn());
      headCurr.setDauditdate(headReloading.getDauditdate());
      // ģ���ͷ������
      getHeadItem("ts").setValue(headReloading.getTs());
      getHeadItem("forderstatus").setValue(headReloading.getForderstatus());
      getTailItem("cauditpsn").setValue(headReloading.getCauditpsn());
      getTailItem("dauditdate").setValue(headReloading.getDauditdate());
      getTailItem("tmaketime").setValue(headReloading.getTmaketime());
      getTailItem("taudittime").setValue(headReloading.getTaudittime());
      getTailItem("tlastmaketime").setValue(headReloading.getTlastmaketime());
    }
    // ���±��壺{ʱ������ۼ��ռƻ��������ۼ��������}
    HashMap mapIdItemReLoad = getHashMapIdItem(voReloading);
    if (mapIdItemReLoad == null || mapIdItemReLoad.size() == 0) {
      SCMEnv.out("����ͬ���Ĵ��д�ˢ�����ݵĵ���VO(����)����Ϊ��!������Ϣ������Ա�ο���\n");
      SCMEnv.out(new Exception());
      MessageDialog.showHintDlg(getContainer(), NCLangRes.getInstance().getStrByID("4004020201", "UPPSCMCommon-000270")/*��ʾ*//* notranslate */,
          NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000282")/*����ͬ�������ݴ������⣬���ֹ����ˢ�µ�ǰ����*//* notranslate */);
      return;
    }
    HashMap mapIdItemCurr = getHashMapIdItem(voCurr);
    int iLen = getRowCount();
    String strCorder_bid = null;
    BillModel bm = getBillModel();
    OrderItemVO itemReload = null;
    OrderItemVO itemCurr = null;
    for (int i = 0; i < iLen; i++) {
      strCorder_bid = (String) bm.getValueAt(i, "corder_bid");
      if (strCorder_bid == null) {
        continue;
      }
      itemReload = (OrderItemVO) mapIdItemReLoad.get(strCorder_bid);
      if (itemReload == null) {
        continue;
      }
      // VO����
      itemCurr = (OrderItemVO) mapIdItemCurr.get(strCorder_bid);
      if (itemCurr != null) {
        itemCurr.setTs(itemReload.getTs());
        // itemCurr.setNaccumdayplnum(itemReload.getNaccumdayplnum());
        itemCurr.setNaccumstorenum(itemReload.getNaccumstorenum());
      }
      // ģ�����
      bm.setValueAt(itemReload.getTs(), i, "ts");
      // bm.setValueAt(itemReload.getNaccumdayplnum(),i,"naccumdayplnum");
      bm.setValueAt(itemReload.getNaccumstorenum(), i, "naccumstorenum");
    }

  }

  /**
   * ���ߣ���־ƽ ���ܣ�����HashMap:{corder_bid=OrderItemVO} ������OrderVO ���أ��� ���⣺��
   * ���ڣ�2005-4-14 10:24:16 �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private HashMap getHashMapIdItem(OrderVO vo) {
    HashMap mapRet = new HashMap();
    if (vo == null || vo.getBodyVO() == null) {
      return mapRet;
    }
    OrderItemVO[] items = vo.getBodyVO();
    int iLen = items.length;
    for (int i = 0; i < iLen; i++) {
      if (items[i] == null || items[i].getPrimaryKey() == null) {
        continue;
      }
      mapRet.put(items[i].getPrimaryKey(), items[i]);
    }
    return mapRet;
  }

  /**
   * ��ȡ���ʾ������ù���
   */
  public POPubSetUI2 getPoPubSetUi2() {
    if (m_cardPoPubSetUI2 == null) {
      m_cardPoPubSetUI2 = new POPubSetUI2();
    }
    return m_cardPoPubSetUI2;
  }

  /**
   * ������¼�,��Ҫ��Ϊ�˴����BillModel���ݵ��������Զ���Ļ������ؼ�
   * 
   * @author czp
   * @since v50
   */
  public void afterSort(String key) {
    // ������ʾ
    if (this.getBillData().getEnabled()
        && (getBillType().equalsIgnoreCase(BillTypeConst.PO_ORDER) || getBillType()
            .equalsIgnoreCase(BillTypeConst.PO_REPLENISH))) {
      OrderVO voCurr = (OrderVO) getBillValueVO(OrderVO.class.getName(),
          OrderHeaderVO.class.getName(), OrderItemVO.class.getName());
      InvAttrCellRenderer ficr = new InvAttrCellRenderer();
      ficr.setFreeItemRenderer(this, voCurr);
    }
    // TODO �Զ����ɷ������
    HashMap hData = ((PoToftPanel) m_conInvoker).getMaxPrice();
    HashMap hNewDate = new HashMap();
    if (hData.size() > 0) {
      String[] sValue = (String[]) hData.values().toArray(
          new String[hData.size()]);
      for (int i = 0; i < getBillModel().getRowCount(); i++) {
        String rowNo = (String) getBillModel().getValueAt(i, "crowno");
        if (rowNo != null) {
          for (int j = 0; j < sValue.length; j++) {
            if (rowNo.equals(sValue[j])) {
              hNewDate.put(String.valueOf(i), sValue[j]);
              break;
            }
          }
        }
      }
    }
    if (hNewDate.size() > 0) {
      ((PoToftPanel) m_conInvoker).getRowRender().setRight(true);
      ((PoToftPanel) m_conInvoker).getRowRender().setRowRender(getBillTable(),
          hNewDate);
      getBillTable().repaint();
    }
  }

  /**
   * ���д���
   * 
   * @see nc.ui.pub.bill.BillActionListener#onEditAction(int)
   */
  public boolean onEditAction(int action) {
    if (action == BillScrollPane.ADDLINE) {
      if (addOneMore) {
        addOneMore = false;
        return true;
      }
      else {
        onActionAppendLine(1,-1);
        return false;
      }
    }
    return true;
  }

  /**
   * ѯЭͬ��˾��
   */
  public void LookSoPrice() {
    // ȡ��û�д����Ŀհ���
    int iCount = getRowCount();
    for (int i = 0; i < iCount; i++) {
      String sMangid = (String) getBillModel().getValueAt(i, "cbaseid");
      if (sMangid == null) {
        getBodyPanel().delLine(new int[] {
          i
        });
      }
    }

    String sVendor = getHeadItem("cvendormangid").getValue();
    if (sVendor == null) {
      ((PoToftPanel) m_conInvoker).showErrorMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000283")/*ѯЭͬ�۸�������빩Ӧ��*/);
      return;
    }

    String sCorp = getCorp();
    iCount = getRowCount();
    if (iCount < 0) {
      ((PoToftPanel) m_conInvoker).showErrorMessage(NCLangRes.getInstance().getStrByID("4004020201", "UPP4004020201-000284")/*û�б�������*/);
      return;
    }
    String[] sMangids = new String[iCount];
    String[] sCurrTypeid = new String[iCount];
    String[] sCvenddevareaid = new String[iCount];
    UFDouble[] sOrderNum = new UFDouble[iCount];
    String[] sCmessureunitid = new String[iCount];
    for (int i = 0; i < iCount; i++) {
      sMangids[i] = (String) getBillModel().getValueAt(i, "cbaseid");
      sCurrTypeid[i] = (String) getBillModel().getValueAt(i, "ccurrencytypeid");
      sCvenddevareaid[i] = (String) getBillModel().getValueAt(i,
          "cvenddevareaid");
      sOrderNum[i] = (UFDouble) getBillModel().getValueAt(i, "nordernum");
      sCmessureunitid[i] = (String) getBillModel()
          .getValueAt(i, "cmessureunit");
    }
    // ����Эͬ����ƥ��Эͬ��˾����
    ICoopwithOut bo = (ICoopwithOut) nc.bs.framework.common.NCLocator
        .getInstance().lookup(ICoopwithOut.class.getName());
    ForStockStruct fss = null;
    try {
      // ѯЭͬ��˾���ۼ۸�ϵͳ
      fss = bo.forStock(sCorp, sVendor, sMangids);
    }
    catch (Exception e) {
      ((PoToftPanel) m_conInvoker).showErrorMessage(e.getMessage());
      return;
    }
    String sSoCorp = fss.getCorpID();
    String sSoVendorid = fss.getCumanID();
    String[] sSoMangid = new String[iCount];
    for (int i = 0; i < iCount; i++) {
      sSoMangid[i] = fss.getInvodocIDS().get(sMangids[i]);
    }
    Vector vecData = new Vector();
    for (int i = 0; i < iCount; i++) {
      if (sSoMangid[i] != null) {
        SalePriceVO soVO = new SalePriceVO();
        soVO.setCropID(sSoCorp);
        soVO.setCustomerID(sSoVendorid);
        soVO.setCurrencyID(sCurrTypeid[i]);
        soVO.setNumber(sOrderNum[i]);
        soVO.setInventoryBaseID(sMangids[i]);
        soVO.setInventoryID(sSoMangid[i]);
        soVO.setSystemData(new UFDate((String) getHeadItem("dorderdate")
            .getValue()));
        soVO.setMeasdocid(sCmessureunitid[i]);
        vecData.add(soVO);

      }

    }
    nc.itf.scm.sp.pub.IFindSalePriceQuery so = (nc.itf.scm.sp.pub.IFindSalePriceQuery) nc.bs.framework.common.NCLocator
        .getInstance().lookup(
            nc.itf.scm.sp.pub.IFindSalePriceQuery.class.getName());
    nc.vo.sp.service.PriceAskResultVO[] priceVOs = null;
    if (vecData.size() > 0) {
      SalePriceVO[] sovos = new SalePriceVO[vecData.size()];
      vecData.copyInto(sovos);
      try {
        priceVOs = so.findPrices(sovos);
      }
      catch (Exception e) {
        ((PoToftPanel) m_conInvoker).showErrorMessage(e.getMessage());
        return;
      }
      if (priceVOs != null && priceVOs.length > 0) {
        for (int i = 0; i < priceVOs.length; i++) {
          if (priceVOs[i].getNetNum() != null) {
            String changeKey = null;
            if (priceVOs[i].getIsIncludeTax().booleanValue()) {
              changeKey = "norgtaxprice";
            }
            else {
              changeKey = "noriginalcurprice";
            }
            setBodyValueAt(priceVOs[i].getNetNum(), i, changeKey);
            // ���¼���������ϵ
            calRelation(new BillEditEvent(
                getBodyItem(changeKey).getComponent(), getBodyValueAt(i,
                    changeKey), changeKey, i));
          }
        }
      }
    }
  }

  // ������״̬�Ի���
  public void showDeliveryStatusUI(OrderVO voUI) {
    boolean bTransportEnable = false;
//    ICreateCorpQueryService corpDmo = (ICreateCorpQueryService) NCLocator
//        .getInstance().lookup(ICreateCorpQueryService.class.getName());
    IPoReceivePlan bo = (IPoReceivePlan) nc.bs.framework.common.NCLocator
        .getInstance().lookup(IPoReceivePlan.class.getName());
    try {
      Vector vecData = new Vector();

      // ��ѯ�Ƿ��е����ƻ�
      OrderReceivePlanVO[] planVOs = bo.queryPlanVOsByHId(voUI.getHeadVO()
          .getCorderid());
      HashMap hItem = new HashMap();
      HashMap hPlan = new HashMap();
      if (planVOs != null) {
        OrderItemVO voBody = null;
        for (int i = 0; i < voUI.getBodyVO().length; i++) {
          hItem.put(voUI.getBodyVO()[i].getCorder_bid(), voUI.getBodyVO()[i]);
        }
        for (int i = 0; i < planVOs.length; i++) {
          hPlan.put(planVOs[i].getCorder_bid(), planVOs[i]);
          voBody = (OrderItemVO) hItem.get(planVOs[i].getCorder_bid());
          if(voBody == null){
        	  continue;
          }
          
          SourceBillDeliveryStatus transport = new SourceBillDeliveryStatus();
          transport = new SourceBillDeliveryStatus();
          transport.setCbillid(planVOs[i].getCorderid());
          transport.setCbill_bid(planVOs[i].getCorder_bid());
          transport.setCpuorder_bb1id(planVOs[i].getPrimaryKey());
          transport.setVpuplancode(planVOs[i].getVplancode());
          transport.setCbilltypecode("21");
          transport.setCinvbasid(planVOs[i].getCbaseid());
          Object[] obj = null;
          Object[] oaRet = null;
          Object[] objname = null;
          try {
            obj = (Object[]) CacheTool.getCellValue("bd_invbasdoc",
                "pk_invbasdoc", "invname", planVOs[i].getCbaseid());
            oaRet = (Object[]) CacheTool.getCellValue("bd_invbasdoc",
                "pk_invbasdoc", "pk_measdoc", planVOs[i].getCbaseid());
            objname = (Object[]) CacheTool.getCellValue("bd_measdoc",
                "pk_measdoc", "measname", (String) oaRet[0]);

          }
          catch (Exception e) {
            ((PoToftPanel) m_conInvoker).showErrorMessage(e.getMessage());
          }
          transport.setCinvname((String) obj[0]);
          transport.setPk_corp(getCorp());
          transport.setCrowno(voBody.getCrowno());
          transport.setCunitid((String) oaRet[0]);
          transport.setCunitname((String) objname[0]);
          transport.setNnumber(PuPubVO.getUFDouble_NullAsZero(planVOs[i]
              .getNordernum()));
          transport.setNcansendnum(PuPubVO.getUFDouble_NullAsZero((planVOs[i]
              .getNordernum()).sub(PuPubVO.getUFDouble_NullAsZero(planVOs[i]
              .getNaccumdevnum()))));
          transport.setNsendnum(PuPubVO.getUFDouble_NullAsZero(planVOs[i]
              .getNaccumdevnum()));
          transport.setBsendendflag(planVOs[i].getBtransclosed());
          vecData.add(transport);
        }
      }
      IOrder iorder = (IOrder) NCLocator.getInstance().lookup(
          IOrder.class.getName());
      OrderVO vo = iorder.queryOrderVOByKey(voUI.getHeadVO().getCorderid());
      if(vo == null){
          MessageDialog.showWarningDlg(this, "", NCLangRes.getInstance().getStrByID(
                  "4004020201", "UPP4004020201-000077")/*
                   * @res
                   * "û�з��������Ĳɹ�����"
                   */);
          return;
      }
      Vector vct = new Vector();
      for (int i = 0; i < vo.getBodyVO().length; i++) {
          OrderItemVO bodyVO = vo.getBodyVO()[i];
          if(!bodyVO.isActive()){
          	continue;
          }
          vct.add(bodyVO);
      }
      
      OrderItemVO[] item = new OrderItemVO[vct.size()];
      vct.copyInto(item);
      vo.setChildrenVO(item);
      
      for (int i = 0; i < vo.getBodyVO().length; i++) {
        OrderItemVO bodyVO = vo.getBodyVO()[i];
        if (!hPlan.containsKey(bodyVO.getCorder_bid())) {
          SourceBillDeliveryStatus transport = new SourceBillDeliveryStatus();
          transport.setCbillid(bodyVO.getCorderid());
          transport.setCbill_bid(bodyVO.getCorder_bid());
          transport.setCpuorder_bb1id(bodyVO.getOrderbbid());
          transport.setVpuplancode(bodyVO.getOrderbbcode());
          transport.setCbilltypecode("21");
          transport.setCinvbasid(bodyVO.getCbaseid());
          Object[] obj = null;
          Object[] oaRet = null;
          Object[] objname = null;
          try {
            obj = (Object[]) CacheTool.getCellValue("bd_invbasdoc",
                "pk_invbasdoc", "invname", bodyVO.getCbaseid());
            oaRet = (Object[]) CacheTool.getCellValue("bd_invbasdoc",
                "pk_invbasdoc", "pk_measdoc", bodyVO.getCbaseid());
            objname = (Object[]) CacheTool.getCellValue("bd_measdoc",
                "pk_measdoc", "measname", (String) oaRet[0]);

          }
          catch (Exception e) {
            ((PoToftPanel) m_conInvoker).showErrorMessage(e.getMessage());
          }
          transport.setCinvname((String) obj[0]);
          transport.setPk_corp(getCorp());
          transport.setCrowno(bodyVO.getCrowno());
          transport.setCunitid((String) oaRet[0]);
          transport.setCunitname((String) objname[0]);
          transport.setNnumber(PuPubVO.getUFDouble_NullAsZero(bodyVO
              .getNordernum()));
          transport.setNcansendnum(PuPubVO.getUFDouble_NullAsZero((bodyVO
              .getNordernum()).sub(PuPubVO.getUFDouble_NullAsZero(bodyVO
              .getNaccumdevnum()))));
          transport.setNsendnum(PuPubVO.getUFDouble_NullAsZero(bodyVO
              .getNaccumdevnum()));
          transport.setBsendendflag(bodyVO.getBtransclosed());
          vecData.add(transport);
        }
      }
      if(getDeliveryStatusUI()  == null){
        return;
      }
      boolean isChange = getDeliveryStatusUI().show(
          (SourceBillDeliveryStatus[]) vecData
              .toArray(new SourceBillDeliveryStatus[0]));
      if (isChange) {
          //ʹ��������VO��ˢ�£�ֻˢ���˱�ͷ\����ʱ������ۼ������������Ƿ�����رձ�־
        OrderVO[] ordervos = OrderHelper.queryOrderVOsLight(
                new String[] {voUI.getHeadVO().getCorderid()}, "TRANSCLOSE");
        if(ordervos != null && ordervos.length == 1 && ordervos[0] != null){
            OrderVO voLight = ordervos[0];
            voUI.setTransClosedVo(voLight);
            displayCurVO(voUI, true);
            updateUI();
        }
      }
    }
    catch (Exception e) {
      ((PoToftPanel) m_conInvoker).showErrorMessage(e.getMessage());
      return;
    }
  }

  /**
   * �������״̬UI
   */
  private IDeliveryStatusUI getDeliveryStatusUI() {
    if (sbdsUI == null){
      sbdsUI = IDeliveryStatusUI.Utility.getDeliveryStatusUI((PoToftPanel) m_conInvoker,
    		  ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
    }
    return sbdsUI;
  }

  public void stateChanged(ChangeEvent e) {
    // ����������ť��Ƭ�༭
    int index = getBodyTabbedPane().getSelectedIndex();
    Component[] menuItems = getBodyMenu().getComponents();
    if (index == 0) {
      for (Component menuItem : menuItems) {
        UIMenuItem Item = (UIMenuItem) menuItem;
        if (NCLangRes.getInstance().getStrByID("common", "SCMCOMMON000000267")
            /* @res"��Ƭ�༭" */.equals(Item.getText())) {
          Item.setEnabled(true);
        }
      }
    }
    else {
      for (Component menuItem : menuItems) {
        UIMenuItem Item = (UIMenuItem) menuItem;
        if (NCLangRes.getInstance().getStrByID("common", "SCMCOMMON000000267")
            /* @res"��Ƭ�༭" */.equals(Item.getText())) {
          Item.setEnabled(false);
        }
      }
    }
  }

  // since v55, ��Ʒ���ƺϼ�(�����۶�������һ��)
  public UFDouble calcurateTotal(String key) {
    UFDouble total = new UFDouble(0.0);
    BillCardPanelTool cardTool = new BillCardPanelTool(this.getBillData());
    // ��Ʒ��־
    UFBoolean blargessflag = null;
    for (int i = 0; i < getRowCount(); i++) {
      blargessflag = cardTool.getBodyUFBooleanValue(i, "blargess");
      if (OrderItemVO.isPriceOrMny(key) && blargessflag != null
          && blargessflag.booleanValue()) {
        continue;
      }
      total = total.add(PuPubVO.getUFDouble_NullAsZero(getBodyValueAt(i, key)));
    }
    return total;
  }
  /**
   * begin ncm linsf 201007142027273523_Timelink_ͬ����ͷ���ֵ�����
   *@author ncm linsf 
   * @see Timelink��Ŀ��ͷ����ͬ��������
   * ͬ����ͷ������Ϣ������
   */
  private void syncurrency(){
	 String headCurrency = getHeadItem("ccurrencytypeid").getValue();
	 if(headCurrency==null || headCurrency.trim().length()==0)
		 return;
	 int rows = getRowCount();
	 Object rowCurrency;
	 for(int row=0; row<rows; row++){
		 rowCurrency = getBodyValueAt(row, "ccurrencytypeid");
		//���������ͬ��������
		 if(rowCurrency!=null && rowCurrency.toString().trim().length()>0
				 && rowCurrency.toString().equals(headCurrency)){
			 continue;		 
		 }
		 setBodyValueAt(headCurrency, row, "ccurrencytypeid");
		 //ͬʱ����������ֱ༭�¼�
		 afterEditWhenBodyCurrency(new BillEditEvent(getBodyItem(
         	"ccurrencytypeid").getComponent(), headCurrency, "ccurrencytypeid", row),false);
     // �����޸ı�־
		 getBillModel().setRowState(row, BillModel.MODIFICATION);
		 //afterEditWhenBodyCurrency((BillEditEvent e,boolean isSetDefaultPrice)); 
	 }
	  //end ncm linsf 201007142027273523_Timelink_ͬ����ͷ���ֵ�����
	  
  }
  
}
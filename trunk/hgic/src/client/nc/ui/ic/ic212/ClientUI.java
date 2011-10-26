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
 * ���ϳ��ⵥ
 *
 *
 * �������ڣ�(2001-11-23 15:39:43)
 * @author������
 */
public class ClientUI extends nc.ui.ic.pub.bill.GeneralBillClientUI {
//	private ButtonObject m_boRatioOut ;
//	private ButtonObject m_boRefMR ;
	//��ȳ���Ի���
	private RatioOUTStructureDlg ivjRatioOUTStructureDlg = null;

	private IButtonManager m_buttonManager;

/**
 * ClientUI2 ������ע�⡣
 */
public ClientUI() {
	super();
	initialize();
	//initRefWork();
}

/**
 * ClientUI ������ע�⡣
 * add by liuzy 2007-12-18 ���ݽڵ�����ʼ������ģ��
 */
public ClientUI(FramePanel fp) {
 super(fp);
 initialize();
}

/**
 * ClientUI ������ע�⡣
 * nc 2.2 �ṩ�ĵ������鹦�ܹ����ӡ�
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
 * �
 * ���˹������ĺ�Ͷ�ϵ����
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
 * �����ߣ����˾�
 * ���ܣ����ݱ༭����
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
protected void afterBillEdit(nc.ui.pub.bill.BillEditEvent e) {
//	nc.vo.scm.pub.SCMEnv.out("haha,bill edit/.");
}
/**
 * �����ߣ����˾�
 * ���ܣ���������ѡ��ı�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
protected void afterBillItemSelChg(int iRow, int iCol) {
	//nc.vo.scm.pub.SCMEnv.out("haha,sel chged!");

}
/**
 * �����ߣ����˾�
 * ���ܣ����ݱ༭�¼�����
 * ������e���ݱ༭�¼�
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-8 19:08:05)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */

public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
	//�У�ѡ�б�ͷ�ֶ�ʱΪ-1
	int row = e.getRow();
	//�ֶ�itemkey
	String sItemKey = e.getKey();
	//�ֶΣ�λ�� 0: head     1:table
	int pos = e.getPos();

	if (pos == nc.ui.pub.bill.BillItem.BODY
		&& row < 0
		|| sItemKey == null
		|| sItemKey.length() == 0)
		return;
	//�ɱ�����
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

		//�������������б���ʽ����ʾ��
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

		//�������������б���ʽ����ʾ��
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

		//�������������б���ʽ����ʾ��
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
	//���˱��幤������
	if (sItemKey.equals("pk_calbody")||sItemKey.equals("cwarehouseid")){
		initRefWork();
	}
}
/**
 * �����ߣ����˾�
 * ���ܣ����ݱ༭�¼�����
 * ������e���ݱ༭�¼�
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-8 19:08:05)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
public boolean beforeBillItemEdit(nc.ui.pub.bill.BillEditEvent e) {
	return true;
}
/**
 * �����ߣ����˾�
 * ���ܣ���������ѡ��ı�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
protected void beforeBillItemSelChg(int iRow, int iCol) {
	//nc.vo.scm.pub.SCMEnv.out("haha,before sel");

}
/**
  * �����ߣ����˾�
  * ���ܣ����󷽷�������ǰ��VO���
  * �����������浥��
  * ���أ�
  * ���⣺
  * ���ڣ�(2001-5-24 ���� 5:17)
  * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
  */
protected boolean checkVO(nc.vo.ic.pub.bill.GeneralBillVO voBill) {
	return super.checkVO();
}
/**
 * ���� BillCardPanel1 ����ֵ��
 * @return nc.ui.pub.bill.BillCardPanel
 */
/* ���棺�˷������������ɡ� */
protected nc.ui.pub.bill.BillCardPanel getBillCardPanel() {
	if (ivjBillCardPanel == null) {
		try {
			ivjBillCardPanel=super.getBillCardPanel();
			BillData bd=ivjBillCardPanel.getBillData();
				//���幤�����Ĳ���
			if (bd.getBodyItem("cworkcentername") != null) {
				nc.ui.pub.beans.UIRefPane ref1 = new nc.ui.pub.beans.UIRefPane();
				ref1.setIsCustomDefined(true);
//				�޸��ˣ������� �޸�ʱ�䣺2008-6-3 ����11:07:21 �޸�ԭ�򣺸��ݹ�˾���˹������ġ�
				//WkRefModel model1 = new WkRefModel();
				WkRefModel model1 = new WkRefModel(getEnvironment().getCorpID());

				ref1.setRefModel(model1);


				bd.getBodyItem("cworkcentername").setComponent(ref1); //

			}
			//����Ͷ�ϵ����
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
 * �˴����뷽��˵����
 * ��������:
 * ���ߣ����˾�
 * �������:
 * ����ֵ:
 * �쳣����:
 * ����:(2003-6-25 20:43:17)
 * @return java.util.ArrayList
 */
protected ArrayList getFormulaItemBody() {
	ArrayList arylistItemField = super.getFormulaItemBody();
	if (arylistItemField != null) {
		//�������� new added by zhx
		String[] aryItemField20 = new String[] { "gzzxmc", "cworkcentername", "cworkcenterid" };
		arylistItemField.add(aryItemField20);
		//Ͷ�ϵ� new added by zhx
		String[] aryItemField21 = new String[] { "tldmc", "cworksitename", "cworksiteid" };
		arylistItemField.add(aryItemField21);
	}

	//��Ӧ��ⵥ���� ccorrespondtype
	//String[] aryItemField20 =
	//new String[] { "billtypename", "cfirsttypeName", "cfirsttype" };
	//arylistItemField.add(aryItemField20);
	return arylistItemField;
}
/**
 * ���� FormMemoDlg1 ����ֵ��
 * @return nc.ui.ic.ic211.FormMemoDlg
 */
/* ���棺�˷������������ɡ� */
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
 * �����ߣ����˾�
 * ���ܣ���ʼ��ϵͳ����
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
protected void initPanel() {
	//��Ҫ���ݲ���
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
	 * ����ʵ�ָ÷�������Ӧ��ť�¼���
	 * @version (00-6-1 10:32:59)
	 *
	 * @param bo ButtonObject
	 */
	public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
		if (bo == getButtonManager().getButton(ICButtonConst.BTN_BILL_RATIO_OUT)){
			//����"�γɴ���"
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
	 * ��ȳ��ⰴť����¼�����Ӧ����
	 * �����ߣ�����
	 * ���ܣ�
	 * ������Void
	 * ���أ�Void
	 * ���⣺
	 * ���ڣ�(2001-5-10 11:04:00)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 *
	 */
	private void onRatioOut() {

		if (getEnvironment().getCorpID() == null || getEnvironment().getUserID() == null || getEnvironment().getLogDate() == null) {

			nc.vo.scm.pub.SCMEnv.out("��˾ID������ԱID����½����Ϊ�գ�");
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
//      �޸��ˣ������� �޸����ڣ�2007-04-29 �޸�ԭ�򣺲����������������ɲ��ϳ��ⵥ�󣬱�״̬���ԣ�Ӧ�ð��Ƿ�������ɶ��ŵ������ó���
        setRefBillsFlag(true);
				setBillRefMultiVOs(null,(GeneralBillVO[])vos);
			}
		} catch (Exception e) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000297")/*@res "��������:"*/ + e.getMessage());
		}
	}
}

public IButtonManager getButtonManager() {
	if (m_buttonManager == null) {
		try {
			m_buttonManager = new ButtonManager212(this);
		} catch (BusinessException e) {
			//��־�쳣
			nc.vo.scm.pub.SCMEnv.error(e);
			showErrorMessage(e.getMessage());
		}
	}
	return m_buttonManager;
}

/**
 * �
 * ���ܣ���ѯһ����ȳ���ĵ���
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2005-1-4 17:56:45)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 */
public void qryRationOutBill(ArrayList alHIDs) {
	try {
		nc.vo.ic.pub.bill.Timer timer = new nc.vo.ic.pub.bill.Timer();
		timer.start(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000277")/*@res "@@��ѯ��ʼ��"*/);
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
		//���qrycontionVO�Ĺ���
		QryConditionVO voCond = new QryConditionVO(sbSql.toString());
		if (m_bIsByFormula) {
			voCond.setIntParam(0, GeneralBillVO.QRY_HEAD_ONLY_PURE);
		} else {
			voCond.setIntParam(0, GeneralBillVO.QRY_HEAD_ONLY);
		}
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000250")/*@res "���ڲ�ѯ�����Ժ�..."*/);
		timer.showExecuteTime("Before ��ѯ����");/*-=notranslate=-*/
		ArrayList<GeneralBillVO> alListData = GeneralBillHelper.queryBills(getBillType(),voCond);
		timer.showExecuteTime("��ѯʱ�䣺");/*-=notranslate=-*/

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
			//���õ�ǰ�ĵ�������/��ţ����ڰ�ť����
			setM_iLastSelListHeadRow(0);
			//��ʼ����ǰ������ţ��л�ʱ�õ������������������ñ������ݡ�
			m_iCurDispBillNum = -1;
			//��ǰ������
			m_iBillQty = getM_alListData().size();

			if (m_iBillQty > 0)
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000290",null,new String[]{(new Integer(m_iBillQty)).toString()})/*@res "���鵽{0}�ŵ��ݣ�"*/);
			else
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000243")/*@res "δ�鵽���������ĵ��ݡ�"*/);

		} else {
			dealNoData();
		}
		setM_iMode(BillMode.Browse);
		setButtonStatus(true);
	} catch (Exception e) {
		handleException(e);
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000251")/*@res "��ѯ����"*/);
		showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000252")/*@res "��ѯ����"*/ + e.getMessage());
	}

}
/**
 * �����ߣ����˾�
 * ���ܣ����б�ʽ��ѡ��һ�ŵ���
 * ������ ������alListData�е�����
 * ���أ���
 * ���⣺
 * ���ڣ�(2001-11-23 18:11:18)
 *  �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
protected void selectBillOnListPanel(int iBillIndex) {}
/**
 * �����ߣ����˾�
 * ���ܣ����󷽷������ð�ť״̬����setButtonStatus�е��á�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
protected void setButtonsStatus(int iBillMode) {
  //modified by liuzy 2007-11-23 ����ȳ��⡱��ť�ڱ༭״̬�²�����
  switch(iBillMode){
    case BillMode.New:
    case BillMode.Update:
      getButtonManager().getButton(ICButtonConst.BTN_BILL_RATIO_OUT).setEnabled(false);
      break;
      default:
        getButtonManager().getButton(ICButtonConst.BTN_BILL_RATIO_OUT).setEnabled(true);
  }
//modified by liuzy 2007-11-23 ����ȳ��⡱��ť�ڱ༭״̬�²�����
//	//���ģʽ�£��е��ݲ����Ѿ�ǩ�ֲſ���
//  getButtonManager().getButton(ICButtonConst.BTN_BILL_RATIO_OUT).setEnabled(true);
	//��Ҫ�������ð�ť��ˢ�����ఴť��״̬��
	//super.initButtonsData();
	//m_vBillMngMenu.addElement(m_boRatioOut);
	//super.setButtons();
}
}
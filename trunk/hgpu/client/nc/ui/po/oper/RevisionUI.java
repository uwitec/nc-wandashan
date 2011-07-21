package nc.ui.po.oper;

/**
 * �ɹ������޶�����
 * ���ߣ�WYF
 * @version		2004/06/14
 * 
 * Modify : V5,czp 
 */
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ListSelectionModel;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ml.NCLangRes;
import nc.ui.po.OrderHelper;
import nc.ui.po.pub.PoCardPanel;
import nc.ui.po.pub.PoListPanel;
import nc.ui.po.pub.PoPublicUIClass;
import nc.ui.pu.pub.POPubSetUI2;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.pf.PfUtilClient;
import nc.vo.po.OrderHeaderVO;
import nc.vo.po.OrderItemVO;
import nc.vo.po.OrderVO;
import nc.vo.pu.exception.MaxPriceException;
import nc.vo.pu.exception.PoReviseException;
import nc.vo.pu.exception.RwtPoToPrException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;

public class RevisionUI extends PoToftPanel {
	//��ѯ��������ģ��
	private OrderUIQueDlg m_condition = null;

/**
 * RevisionUI_Test ������ע�⡣
 * @param panel nc.ui.pub.FramePanel
 */
public RevisionUI(nc.ui.pub.FramePanel panel) {
	super(panel);
}
/**
 * ���ߣ���ӡ��
 * ���ܣ� �õ���ť������
 * ��������
 * ���أ�PoToftPanelButton
 * ���⣺��
 * ���ڣ�(2004-4-01 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
protected PoToftPanelButton getBtnManager() {
	if (m_btnManager==null) {
		m_btnManager = new	RevisionUIButton() ;
	}
	return	 m_btnManager;
}
/**
 * ���ߣ�WYF
 * ���ܣ���ȡ����Panel��
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2001-10-20 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
protected nc.ui.po.pub.PoCardPanel getInitPoCardPanel(POPubSetUI2 setUi) {
	return new PoCardPanel(
		this,
		PoPublicUIClass.getLoginPk_corp(),
		nc.vo.scm.pu.BillTypeConst.PO_REVISE,
		setUi);

}
/**
 * ���ߣ�WYF
 * ���ܣ���ȡ�б�Panel��
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2001-10-20 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
protected nc.ui.po.pub.PoListPanel getInitPoListPanel(POPubSetUI2 setUi) {
	// ����ģ��
	PoListPanel	listBill =
		new PoListPanel(this, PoPublicUIClass.getLoginPk_corp(), nc.vo.scm.pu.BillTypeConst.PO_REVISE,setUi);
	//ֻ����ѡ����
	listBill.getHeadTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	//listBill.addMouseListener(this);

	return	listBill ;
}
/**
 * ���ߣ�WYF
 * ���ܣ���ȡ��ѯ��������ʵ��
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2001-10-20 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * 2002-06-21	wyf		�޸Ĳ�ѯģ����ҵ��������ص����
 */
protected nc.ui.po.pub.PoQueryCondition getPoQueryCondition() {
	/*V5֧�ּ��вɹ�����*/
//	if (m_condition==null) {
//		m_condition = new PoQueryCondition(
//		    this,
//		    nc.ui.ml.NCLangRes.getInstance().getStrByID("4004020201","UPP4004020201-000093")/*@res "�ɹ������޶���ѯ"*/,
//		    "4004020202",
//		    PoPublicUIClass.getLoginPk_corp(),
//			PoPublicUIClass.getLoginUser());
//		m_condition.hideNormal();
//		m_condition.hideCorp();
//	}
	/*V5֧�ּ��вɹ�����*/
	if (m_condition==null) {
		m_condition = new OrderUIQueDlg(
			this,
			"4004020202",
			PoPublicUIClass.getLoginPk_corp(),
			PoPublicUIClass.getLoginUser()) ;		
		m_condition.hideNormal();
		m_condition.setShowPrintStatusPanel(false);
		m_condition.setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004020201","UPP4004020201-000093")/*@res "�ɹ������޶���ѯ"*/);
	}
	return	m_condition ;
}
/**
 * ���ߣ�WYF
 * ���ܣ��õ���ѯ��VO
 * ��������
 * ���أ�OrderVO[]		��ѯ����VO���飬����ֻ�е�һ�ź��壬����ֻ��ͷ
 * ���⣺��
 * ���ڣ�(2003-11-05 13:24:16)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
protected nc.vo.po.OrderVO[] getQueriedVOHead() throws	Exception{
	//ͨ��Clientȡ�����ݣ���λ����������
	return OrderHelper.queryForRevision(
		((OrderUIQueDlg)getPoQueryCondition()).getNormalVOs(true),
		getPoQueryCondition().getConditionVO());
}
/**
 * ���ߣ�WYF
 * ���ܣ���ȡ��ť������
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2004-10-20 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
private RevisionUIButton	getThisBtnManager(){

	return	((RevisionUIButton)getBtnManager()) ;
}
/**
 * ����ʵ�ָ÷���������ҵ�����ı��⡣
 * @version (00-6-6 13:33:25)
 *
 * @return java.lang.String
 */
public String getTitle() {
	return getPoListPanel().getBillListData().getTitle() ;
}
/**
 * ���ߣ�WYF
 * ���ܣ���ʼ���ǿ�Ƭ�����б�
 * ��������
 * ���أ�boolean		��Ƭ����true���б���false
 *		�˷���Ĭ�Ϸ���true����������ʼ��Ϊ�б����д�÷���
 * ���⣺��
 * ���ڣ�(2004-06-23 13:24:16)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
protected	boolean	isInitStateBill(){
	return	false ;
}
/**
 * ��Ӧ���˫��
 * @param
 * @return
 * @exception   Exception
 * @see
 * @since		2001-04-21
*/
public void mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent e) {
	if (e.getPos() == BillItem.HEAD) {

		showHintMessage(  nc.ui.ml.NCLangRes.getInstance().getStrByID("4004020201","UPP4004020201-000074")/*@res "ת����Ƭ"*/  ) ;
		// modify by zhw 20110302
		Object cmangid =getPoListPanel().getHeadBillModel().getValueAt(e.getRow(), "cvendormangid");
		if(!checkOrderState(cmangid)){
			return;
		}
		// zhw end
		//
		getBufferVOManager().setVOPos(getPoListPanel().getVOPos(e.getRow()));
		//

		//��ʾ��Ƭ
		remove(getPoListPanel());
		add(getPoCardPanel(),java.awt.BorderLayout.CENTER);
		//��ť
		setCurOperState(STATE_BILL_EDIT) ;
		setButtons(getThisBtnManager().getBtnaBill(this));

		//��ʾVO
		displayCurVOEntirelyReset(true) ;

		//�޶�
		onBillRevise() ;
	}
}
/**
 * �����б����
 * @param
 * @return
 * @exception   Exception
 * @see
 * @since		2001-04-21
*/
private void onBillRevise() {
	showHintMessage(  nc.ui.ml.NCLangRes.getInstance().getStrByID("4004020201","UPP4004020201-000041")/*@res "�༭����"*/  ) ;

	OrderVO		voOrder = getOrderViewVOAt(getBufferVOManager().getVOPos()) ;
	//modify by zhw 201100302 
	OrderHeaderVO head = (OrderHeaderVO) voOrder.getParentVO();
	Object cmangid = head.getAttributeValue("cvendormangid");
		if(!checkOrderState(cmangid)){
			return;
		}
		//zhw end
	if(voOrder.getHeadVO().getPk_corp().equals(ClientEnvironment.getInstance().getCorporation().getPk_corp())){
	String  ccurtypeid = ((OrderHeaderVO)voOrder.getParentVO()).getCcurrencytypeid();
	if(ccurtypeid == null || (ccurtypeid != null && ccurtypeid.trim().length() == 0)){
		((OrderHeaderVO)voOrder.getParentVO()).setCcurrencytypeid(((OrderItemVO[])voOrder.getChildrenVO())[0].getCcurrencytypeid());
	}
	//�޸�ԭ�к�����Ϣ
	getPoCardPanel().clearAfterBillInfo(voOrder.getHeadVO().getCorderid()) ;
	if ( !getPoCardPanel().loadAfterBillInfo(voOrder) ) {
		//���ð�ť
		setButtonsStateBrowse() ;
		updateUI();
		return	;
	}

	//����״̬
	setCurOperState(STATE_BILL_EDIT) ;
	//���ð�ť
	setButtonsStateEdit() ;
	//���ñ༭��
	getPoCardPanel().onActionRevise(voOrder) ;
  //���÷��ʱ��ͷ�ֶ�Ҳ����ʾ����
	//����ع����޹�˾��Ŀ����v502 modify 2007 11 06
  try{//��ʱ����,֧�ֿ������е��������ȥ��
    setHeadRefPK(voOrder);
  }catch(Exception e){
    SCMEnv.out(e);
  }
  //�޶���
  getPoCardPanel().setTailItem("crevisepsn", PoPublicUIClass.getLoginUser());
	//ˢ��
	updateUI();
	//ȫ����
	getPoCardPanel().transferFocusTo(BillCardPanel.HEAD) ;

	showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000030")/*@res "�����޸�"*/);
	}
}
/**
 * @param voOrder
 */
private void setHeadRefPK(OrderVO voOrder) {
	//�ɹ�Ա
	UIRefPane refpnl = (UIRefPane) getPoCardPanel().getHeadItem("cemployeeid").getComponent();
	refpnl.getRefModel().setSealedDataShow(true);
	String strVarValue = getPoCardPanel().getHeadItem("cemployeeid").getValue();
	if(strVarValue == null ||( strVarValue != null && strVarValue.trim().length() == 0)){
		refpnl.setPK(voOrder.getHeadVO().getCemployeeid());
		String strFormula = "cemployeename->getColValue(bd_psndoc,psnname,pk_psndoc,cemployeeid)";
		Object ob = getPoCardPanel().execHeadFormula(strFormula);
		refpnl.getUITextField().setText((String) ob);
	}
	//�ɹ�����
	refpnl = (UIRefPane) getPoCardPanel().getHeadItem("cdeptid").getComponent();
	refpnl.getRefModel().setSealedDataShow(true);
	strVarValue = getPoCardPanel().getHeadItem("cdeptid").getValue();
	if(strVarValue == null ||( strVarValue != null && strVarValue.trim().length() == 0)){
		refpnl.setPK(voOrder.getHeadVO().getCdeptid());
		String strFormula = "cdeptname->getColValue(bd_deptdoc,deptname,pk_deptdoc,cdeptid)";
		Object ob = getPoCardPanel().execHeadFormula(strFormula);
		refpnl.getUITextField().setText((String) ob);
	}
    //ҵ������
	refpnl = (UIRefPane) getPoCardPanel().getHeadItem("cbiztype").getComponent();
	refpnl.getRefModel().setSealedDataShow(true);
	strVarValue = getPoCardPanel().getHeadItem("cbiztype").getValue();
	if(strVarValue == null ||( strVarValue != null && strVarValue.trim().length() == 0)){
		refpnl.setPK(voOrder.getHeadVO().getCbiztype());
		String strFormula = "cbiztype->getColValue(bd_busitype,businame,pk_busitype,cbiztype)";
		Object ob = getPoCardPanel().execHeadFormula(strFormula);
		refpnl.getUITextField().setText((String) ob);
	}
    //�ɹ���֯
	refpnl = (UIRefPane) getPoCardPanel().getHeadItem("cpurorganization").getComponent();
	refpnl.getRefModel().setSealedDataShow(true);
	strVarValue = getPoCardPanel().getHeadItem("cpurorganization").getValue();
	if(strVarValue == null ||( strVarValue != null && strVarValue.trim().length() == 0)){
		refpnl.setPK(voOrder.getHeadVO().getCpurorganization());
		String strFormula = "cpurorganizationname->getColValue(bd_purorg,name,pk_purorg,cpurorganization)";
		Object ob = getPoCardPanel().execHeadFormula(strFormula);
		refpnl.getUITextField().setText((String) ob);
	}
    //��Ӧ��
	refpnl = (UIRefPane) getPoCardPanel().getHeadItem("cvendormangid").getComponent();
	refpnl.getRefModel().setSealedDataShow(true);
	strVarValue = getPoCardPanel().getHeadItem("cvendorbaseid").getValue();
	if(strVarValue == null ||( strVarValue != null && strVarValue.trim().length() == 0)){
		refpnl.setPK(voOrder.getHeadVO().getCvendorbaseid());
		String strFormula = "cvendormangname->getColValue(bd_cubasdoc,custshortname,pk_cubasdoc,cvendorbaseid)";
		Object ob = getPoCardPanel().execHeadFormula(strFormula);
		refpnl.getUITextField().setText((String) ob);
	}
    //��������
	refpnl = (UIRefPane) getPoCardPanel().getHeadItem("cfreecustid").getComponent();
	refpnl.getRefModel().setSealedDataShow(true);
	strVarValue = getPoCardPanel().getHeadItem("cfreecustid").getValue();
	if(strVarValue == null ||( strVarValue != null && strVarValue.trim().length() == 0)){
		refpnl.setPK(voOrder.getHeadVO().getCfreecustid());
		//String strFormula = "caccountbankname->iif(cfreecustid=null||cfreecustid="+""+",getColValue(bd_custbank,accname,pk_custbank,caccountbankid),getColValue(so_freecust,vaccname,cfreecustid,cfreecustid))";
    //since v55
    String strFormula = "caccountbankname->getColValue(bd_bankaccbas,accountname,pk_bankaccbas,caccountbankid)";
		Object ob = getPoCardPanel().execHeadFormula(strFormula);
		refpnl.getUITextField().setText((String) ob);
	}
    //�ջ���
	refpnl = (UIRefPane) getPoCardPanel().getHeadItem("creciever").getComponent();
	refpnl.getRefModel().setSealedDataShow(true);
	strVarValue = getPoCardPanel().getHeadItem("creciever").getValue();
	if(strVarValue == null ||( strVarValue != null && strVarValue.trim().length() == 0)){
		refpnl.setPK(voOrder.getHeadVO().getCreciever());
		String strFormula = "crecievername->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creciever);crecievername->getColValue(bd_cubasdoc,custshortname,pk_cubasdoc,crecievername)";
		Object ob = getPoCardPanel().execHeadFormula(strFormula);
		refpnl.getUITextField().setText((String) ob);
	}
    //��Ʊ��
	refpnl = (UIRefPane) getPoCardPanel().getHeadItem("cgiveinvoicevendor").getComponent();
	refpnl.getRefModel().setSealedDataShow(true);
	strVarValue = getPoCardPanel().getHeadItem("cgiveinvoicevendor").getValue();
	if(strVarValue == null ||( strVarValue != null && strVarValue.trim().length() == 0)){
		refpnl.setPK(voOrder.getHeadVO().getCgiveinvoicevendor());
		String strFormula = "cgiveinvoicevendorname->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cgiveinvoicevendor);cgiveinvoicevendorname->getColValue(bd_cubasdoc,custshortname,pk_cubasdoc,cgiveinvoicevendorname)";
		Object ob = getPoCardPanel().execHeadFormula(strFormula);
		refpnl.getUITextField().setText((String) ob);
	}
    //���˷�ʽ
	refpnl = (UIRefPane) getPoCardPanel().getHeadItem("ctransmodeid").getComponent();
	refpnl.getRefModel().setSealedDataShow(true);
	strVarValue = getPoCardPanel().getHeadItem("ctransmodeid").getValue();
	if(strVarValue == null ||( strVarValue != null && strVarValue.trim().length() == 0)){
		refpnl.setPK(voOrder.getHeadVO().getCtransmodeid());
		String strFormula = "ctransmodename->getColValue(bd_sendtype,sendname,pk_sendtype,ctransmodeid)";
		Object ob = getPoCardPanel().execHeadFormula(strFormula);
		refpnl.getUITextField().setText((String) ob);
	}
    //ɢ��
	refpnl = (UIRefPane) getPoCardPanel().getHeadItem("cfreecustid").getComponent();
	refpnl.getRefModel().setSealedDataShow(true);
	strVarValue = getPoCardPanel().getHeadItem("cfreecustid").getValue();
	if(strVarValue == null ||( strVarValue != null && strVarValue.trim().length() == 0)){
		refpnl.setPK(voOrder.getHeadVO().getCfreecustid());
		String strFormula = "cfreecustname->getColValue(so_freecust,vcustname,cfreecustid,cfreecustid)";
		Object ob = getPoCardPanel().execHeadFormula(strFormula);
		refpnl.getUITextField().setText((String) ob);
	}
    //����Э��
	refpnl = (UIRefPane) getPoCardPanel().getHeadItem("ctermprotocolid").getComponent();
	refpnl.getRefModel().setSealedDataShow(true);
	strVarValue = getPoCardPanel().getHeadItem("ctermprotocolid").getValue();
	if(strVarValue == null ||( strVarValue != null && strVarValue.trim().length() == 0)){
		refpnl.setPK(voOrder.getHeadVO().getCtermprotocolid());
		String strFormula = "ctermprotocolname->getColValue(bd_payterm,termname,pk_payterm,ctermprotocolid)";
		Object ob = getPoCardPanel().execHeadFormula(strFormula);
		refpnl.getUITextField().setText((String) ob);
	}
    //����
	refpnl = (UIRefPane) getPoCardPanel().getHeadItem("ccurrencytypeid").getComponent();
	refpnl.getRefModel().setSealedDataShow(true);
	strVarValue = getPoCardPanel().getHeadItem("ccurrencytypeid").getValue();
	if(strVarValue == null ||( strVarValue != null && strVarValue.trim().length() == 0)){
		refpnl.setPK(voOrder.getHeadVO().getCcurrencytypeid());
		String strFormula = "ccurrencytypeid->getColValue(bd_currtype,currtypename,pk_currtype,ccurrencytypeid)";
		Object ob = getPoCardPanel().execHeadFormula(strFormula);
		refpnl.getUITextField().setText((String) ob);
	}
    //�ⶳ��
	refpnl = (UIRefPane) getPoCardPanel().getHeadItem("cunfreeze").getComponent();
	refpnl.getRefModel().setSealedDataShow(true);
	strVarValue = getPoCardPanel().getHeadItem("cunfreeze").getValue();
	if(strVarValue == null ||( strVarValue != null && strVarValue.trim().length() == 0)){
		refpnl.setPK(voOrder.getHeadVO().getCunfreeze());
		String strFormula = "cunfreeze->getColValue(sm_user,user_name,cUserId,cunfreeze)";
		Object ob = getPoCardPanel().execHeadFormula(strFormula);
		refpnl.getUITextField().setText((String) ob);
	}
    //�����ʺ�
//	refpnl = (UIRefPane) getPoCardPanel().getHeadItem("cfreecustid").getComponent();
//	refpnl.getRefModel().setSealedDataShow(true);
//	strVarValue = getPoCardPanel().getHeadItem("cfreecustid").getValue();
//	if(strVarValue == null ||( strVarValue != null && strVarValue.trim().length() == 0)){
//		refpnl.setPK(voOrder.getHeadVO().getCfreecustid());
//		String strFormula = "account->iif(cfreecustid=null||cfreecustid=+"+""+",getColValue(bd_custbank,account,pk_custbank,caccountbankid),getColValue(so_freecust,vaccount,cfreecustid,cfreecustid))";
//		Object ob = getPoCardPanel().execHeadFormula(strFormula);
//		refpnl.getUITextField().setText((String) ob);
//	}
}

/**
 * ���ߣ�WYF
 * ���ܣ�����
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2001-04-21  11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
protected boolean onBillSave() {

	//�õ������VO��ֻ����ʾ��VO���б���
	OrderVO voSaved = getPoCardPanel().getSaveVO(getOrderDataVOAt(getBufferVOManager().getVOPos()));
	if (voSaved == null) {
		//δ���޸�
	    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004020601","UPP4004020601-000073")/*@res ""δ�޸��κ��Ŀ����������oЧ""*/);
		return false;
	}
	if(voSaved.getHeadVO().getNprepaymny()!=null&&PuPubVO.getUFDouble_NullAsZero(voSaved.getHeadVO().getNprepaymny()).compareTo(new UFDouble(0))>0){
		UFDouble dPrePayMNY = UFDouble.ZERO_DBL;
		for(int i = 0 ; i < voSaved.getBodyLen() ; i ++){
			dPrePayMNY = dPrePayMNY.add(PuPubVO.getUFDouble_NullAsZero(voSaved.getBodyVO()[i].getNtaxpricemny()));
		}
		if(PuPubVO.getUFDouble_NullAsZero(voSaved.getHeadVO().getNprepaymny()).compareTo(dPrePayMNY)>0){
			showHintMessage(NCLangRes.getInstance().getStrByID("4004020601", "RevisionUI-000000")/*���������˰�ϼ�֮���Ѿ�С�ڶ���Ԥ�����*/);
			return false;
		}
	}

	//��������ʷVO
	OrderVO voHistory = getOrderDataVOAt(getBufferVOManager().getVOPos()).getHistoryVO(PoPublicUIClass.getLoginUser());

	//-------����
	boolean bContinue = true;
	while (bContinue) {
		try {
			PfUtilClient.processActionNoSendMessage(
				this,
				"REVISE",
				nc.vo.scm.pu.BillTypeConst.PO_ORDER,
				getClientEnvironment().getDate().toString(),
				voSaved,
				voHistory,
				null,
				null);
			bContinue = false;
		} catch (Exception e) {
			setCurOperState(STATE_BILL_EDIT);
			if (e instanceof RwtPoToPrException) {
				//�빺��ʾ
				int iRet = showYesNoMessage(e.getMessage());
				if (iRet == MessageDialog.ID_YES) {
					//����ѭ��
					bContinue = true;
					//�Ƿ��һ��
					voSaved.setFirstTime(false);
				} else {
					showHintMessage( nc.ui.ml.NCLangRes.getInstance().getStrByID("4004020201","UPP4004020201-000041")/*@res "�༭����"*/ );
					return false;
				}
			} else
			if (e instanceof nc.vo.scm.pub.SaveHintException) {
				//��ͬ��ʾ
				int iRet = showYesNoMessage(e.getMessage());
				if (iRet == MessageDialog.ID_YES) {
					//����ѭ��
					bContinue = true;
					//�Ƿ��һ��
					voSaved.setFirstTime(false);
				} else {
					showHintMessage( nc.ui.ml.NCLangRes.getInstance().getStrByID("4004020201","UPP4004020201-000041")/*@res "�༭����"*/ );
					return false;
				}
			} else if (e instanceof nc.vo.scm.pub.StockPresentException) {
				//���ִ�����ʾ
				int iRet = showYesNoMessage(e.getMessage());
				if (iRet == MessageDialog.ID_YES) {
					//����ѭ��
					bContinue = true;
					//�Ƿ��һ��
					voSaved.setFirstTimeSP(false);
				} else {
					showHintMessage( nc.ui.ml.NCLangRes.getInstance().getStrByID("4004020201","UPP4004020201-000041")/*@res "�༭����"*/ );
					return false;
				}
			}  else if (e instanceof PoReviseException) {
				//�к������ݵ���ʾ
				int iRet = showYesNoMessage(e.getMessage());
				if (iRet == MessageDialog.ID_YES) {
					//����ѭ��
					bContinue = true;
					//�Ƿ��һ��
					voSaved.setFirstTimeSP(false);
				} else {
					showHintMessage( nc.ui.ml.NCLangRes.getInstance().getStrByID("4004020201","UPP4004020201-000041")/*@res "�༭����"*/ );
					return false;
				}
			} else if (e instanceof MaxPriceException) {
        HashMap hData = ((MaxPriceException) e).getIRows();
        m_renderYellowAlarmLine.setRight(true);
        m_renderYellowAlarmLine.setRowRender(getPoCardPanel().getBillTable(), hData);
        // pnlBill.getBillTable().gett
        // ����޼۳�����ʾ
        getPoCardPanel().getBillTable().repaint();
        String[] sWarn = (String[]) hData.values().toArray(new String[hData.size()]);
        String sShow = new String();
        for (int i = 0; i < sWarn.length; i++) {
          sShow += sWarn[i] + ",";
        }
        int iRet = showYesNoMessage(NCLangRes.getInstance().getStrByID("4004020601", "RevisionUI-000001", null, new String[]{sShow})/*��ţ�{0}���ڳ�������޼۵��У��Ƿ������*/);

        if (iRet == MessageDialog.ID_YES) {
          // ����ѭ��
          bContinue = true;
          // �Ƿ��һ��
          voSaved.setFirstTimePrice(false);
          m_renderYellowAlarmLine.setRight(false);
          getPoCardPanel().getBillTable().repaint();
        } else {
          showHintMessage(nc.ui.ml.NCLangRes
              .getInstance().getStrByID("SCMCOMMON",
                  "UPPSCMCommon-000010")/*
                               * @res "����ʧ��"
                               */);
          return false;
        }
      } else if (e instanceof BusinessException || e instanceof ValidationException) {
				showHintMessage( nc.ui.ml.NCLangRes.getInstance().getStrByID("4004020201","UPP4004020201-000041")/*@res "�༭����"*/ );
				PuTool.outException(this, e);
				return false;
			} else {
				PuTool.outException(this, e);
				showHintMessage( nc.ui.ml.NCLangRes.getInstance().getStrByID("4004020201","UPP4004020201-000041")/*@res "�༭����"*/ );
				return false;
			}
		}
	}

	showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004020201","UPP4004020201-000094")/*@res "�����޶����"*/);

//	String sId = getOrderDataVOAt(getBufferVOManager().getVOPos()).getHeadVO().getCorderid();
	//�������������޸�{0,������1���޸�}
  int iCurOperType = PuPubVO.getString_TrimZeroLenAsNull(voSaved
      .getHeadVO().getPrimaryKey()) == null ? 0 : 1;
	OrderVO[] voLightRefreshed = null;
	//���²�ѯ���滻��ǰVO
	try {
		//==================�������	���²�ѯ
	  voLightRefreshed = OrderHelper.queryOrderVOsLight(
        new String[] { voSaved.getHeadVO().getCorderid() }, "SAVE");
	} catch (Exception e) {
		PuTool.outException(this, e);
		showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004020201","UPP4004020201-000095")/*@res "��������ɹ��������ֲ���������������ˢ�²鿴�õ���"*/);
	}
	
	voSaved.setLastestVo(voLightRefreshed[0],iCurOperType);
	getBufferVOManager().setVOAt(getBufferVOManager().getVOPos(), voSaved);

	//��ʾ��ǰVO
	displayCurVOEntirelyReset(false);

	//���ý����밴ť״̬
	getPoCardPanel().setEnabled(false);
	setCurOperState(STATE_BILL_BROWSE);
	setButtonsStateBrowse();

	updateUI();

	showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH005")/*@res "����ɹ�"*/);
	return true;

}
/**
 * ����ʵ�ָ÷�������Ӧ��ť�¼���
 * @version (00-6-1 10:32:59)
 *
 * @param bo ButtonObject
 */
protected void onButtonClickedBill(nc.ui.pub.ButtonObject bo) {
	if (bo == getThisBtnManager().btnBillRevise ) {
		onBillRevise();
	} else {
		super.onButtonClickedBill(bo);
	}
}
/**
 * ����ʵ�ָ÷�������Ӧ��ť�¼���
 * @version (00-6-1 10:32:59)
 *
 * @param bo ButtonObject
 */
protected void onButtonClickedList(nc.ui.pub.ButtonObject bo) {
	if (bo == getThisBtnManager().btnListRevise) {
		
		onListRevise();
	}  else {
		super.onButtonClickedList(bo);
	}
}


	/**
	 * liuys add for 2010-12-23 У���Խ��ͬ�������޶�
	 */
	private Boolean checkOrderState(Object cmangid) {
		
			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			String sql = "select bas.custname from bd_cumandoc man  join bd_cubasdoc bas on bas.pk_cubasdoc= man.pk_cubasdoc  " +
					" where  man.pk_cumandoc = '" + cmangid + "' and man.pk_corp = '"+PoPublicUIClass.getLoginPk_corp()+"' and isnull(man.dr,0)=0 and isnull(bas.dr,0)=0";
			Object object = null;
			try {
				object =  query.executeQuery(sql, new ColumnProcessor());
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
			if (object != null ){
					if(!"��������ú��ҵ���Źɷ����޹�˾".equalsIgnoreCase(PuPubVO.getString_TrimZeroLenAsNull(object))){
						showErrorMessage("��Ӧ�̲�����ú�Ĳɹ���ͬ�������޶�");
						return false;
					}
				}
		return true;
	}
	
	
	
/**
 * ��ʾ�빺���б����
 */
private void onListRevise() {

	//onDoubleClick(getPoListPanel().getHeadSelectedRow());
	// modify by zhw 20110302
	Object cmangid =getPoListPanel().getHeadBillModel().getValueAt(getPoListPanel().getHeadSelectedRow(), "cvendormangid");
	if(!checkOrderState(cmangid)){
		return;
	}
	// zhw end
	BillMouseEnent	mEvent = new	BillMouseEnent(
		getPoListPanel(),
		getPoListPanel().getHeadSelectedRow(),
		BillItem.HEAD
		 ) ;
	mouse_doubleclick(mEvent) ;
	
	showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000030")/*@res "�����޸�"*/);
}
}
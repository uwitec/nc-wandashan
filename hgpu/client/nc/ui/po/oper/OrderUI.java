package nc.ui.po.oper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import nc.ui.hg.pu.pub.PlanPubHelper;
import nc.ui.ml.NCLangRes;
import nc.ui.po.OrderHelper;
import nc.ui.po.pub.ContractClassParse;
import nc.ui.po.pub.PoCardPanel;
import nc.ui.po.pub.PoListPanel;
import nc.ui.po.pub.PoPublicUIClass;
import nc.ui.po.pub.PoQueryCondition;
import nc.ui.pr.pray.PraybillHelper;
import nc.ui.pu.pub.POPubSetUI2;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.scm.pub.bill.IBillExtendFun;
import nc.vo.hg.pu.pact.PactItemVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pi.NormalCondVO;
import nc.vo.po.OrderHeaderVO;
import nc.vo.po.OrderVO;
import nc.vo.po.pub.OrderPubVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.BillStatus;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.scm.pu.PUMessageVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;

public class OrderUI	extends PoToftPanel
	implements
		nc.ui.pf.query.ICheckRetVO,//getVo()
		IBillExtendFun,//֧�ֲ�ҵ��������չ::getExtendBtns()/onExtendBtnsClick()/setExtendBtnsStat()
		ILinkMaintain,//�����޸�
		ILinkAdd,//��������
		ILinkApprove,//������
		ILinkQuery,//������
		ChangeListener
		{
/**
 * ���ߣ���ӡ��
 * ���ܣ�Ĭ�Ϲ�����
 * �������ޡ�
 * ���أ���
 * ���⣺��
 * ���ڣ�(2001-5-22 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public OrderUI() {
	super();
	init();
}

/**
 * 
 * @author zhf
 * @˵�������׸ڿ�ҵ��
 * 2012-2-23����12:34:55
 */
private void init(){
	getPoCardPanel().getBodyTabbedPane().addChangeListener(this);//zhf
	getPoListPanel().getBodyTabbedPane().addChangeListener(this);//zhf
	m_btnLinePact.addChildButton(m_btnAddLinePact);
	m_btnLinePact.addChildButton(m_btnDelLinePact);
}

/**
 * ���õ��ݱ���
 * @param
 * @return		String
 * @exception
 * @see
 * @since		2001-04-26
*/
public String getTitle(){
	return	getPoCardPanel().getTitle() ;
}
	//��ѯ��������ģ��
	private OrderUIQueDlg m_condition = null;

/**
 * ���ߣ���ӡ��
 * ���ܣ�����������Ϣ����˫����Ϣʱ�����Ķ�����������
 * ������	String	pk_corp				��ǰ��½��˾ID
 * 			String	billType			�������ͣ��˴�ӦΪBillTypeConst.PO_ORDER
 * 			String	businessType		���ݵ�ҵ������
 * 			String	operator			��ǰ����ԱID
 * 			String	billID				��Ҫ�����ĵ���ID
 * ���أ���
 * ���⣺��
 * ���ڣ�(2002-4-22 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public OrderUI(
	String	pk_corp,
	String	billType,
	String	businessType,
	String	operator,
	String	billID
	) {

	super();
	initMsgCenter() ;

	//���ÿ�Ƭ���������ֶ���ʾ��
	//BillData data = getPoCardPanel().getBillData();
	getPoCardPanel().getBodyItem("naccumarrvnum").setShow(true);
	getPoCardPanel().getBodyItem("naccumstorenum").setShow(true);
	getPoCardPanel().getBodyItem("naccuminvoicenum").setShow(true);

	//setCauditid(billID);

	//û�з��������Ķ���
	try {
		getBufferVOManager().resetVOs(new OrderVO[]{OrderHelper.queryOrderVOByKey(billID)}) ;
	} catch (Exception	e) {
		SCMEnv.out(e);
		MessageDialog.showHintDlg(this,NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/,OrderPubVO.returnHintMsgWhenNull(e.getMessage())) ;
	}

	if(getBufferLength()==0){
		MessageDialog.showHintDlg(this,NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/,NCLangRes.getInstance().getStrByID("4004020201","UPP4004020201-000023")/*@res "û�з��ϲ�ѯ�����Ķ���"*/) ;
		getPoCardPanel().addNew() ;
		return ;
	}

	//���ö���VO����
	//setVOPos(0);
	getBufferVOManager().setVOPos(0) ;

	//���ÿ�Ƭ��������
	getPoCardPanel().displayCurVO( getOrderViewVOAt( getBufferVOManager().getVOPos() ), false ) ;

	getPoCardPanel().setEnabled(false) ;
	//���ò���Ա
	PoPublicUIClass.setCuserId(
		new	OrderVO[]{
			getOrderViewVOAt(getBufferVOManager().getVOPos())
		}
	) ;
	init();
}

/**
 * ���ߣ���ӡ��
 * ���ܣ�Ĭ�Ϲ�����
 * �������ޡ�
 * ���أ���
 * ���⣺��
 * ���ڣ�(2001-5-22 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public OrderUI(nc.ui.pub.FramePanel panel) {
	super(panel);
	init();
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
protected PoToftPanelButton getBtnManager(){
	if (m_btnManager==null) {
		m_btnManager = new	OrderUIButton(this) ;


		//��Ŀ������ο���
		try {
			ContractClassParse.resetButton(
				getParameter(ContractClassParse.getParaName()),
				m_btnManager.btnBillContractClass) ;
        } catch (Exception e) {
        	SCMEnv.out("��Ŀ������ο���BUG��"+e.getMessage());
        }
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
protected PoCardPanel getInitPoCardPanel(POPubSetUI2 setUi){
	return	new PoCardPanel(
			this,
			PoPublicUIClass.getLoginPk_corp(),
			BillTypeConst.PO_ORDER,
			setUi
		) ;
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
protected PoListPanel getInitPoListPanel(POPubSetUI2 setUi){
	return	new PoListPanel(
			this,
			PoPublicUIClass.getLoginPk_corp(),
			BillTypeConst.PO_ORDER,
			setUi
		) ;
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
public PoQueryCondition getPoQueryCondition(){
	if (m_condition==null) {
		m_condition = new OrderUIQueDlg(
			this,
			"4004020201",
			PoPublicUIClass.getLoginPk_corp(),
			PoPublicUIClass.getLoginUser()) ;
		//֧�ִ�ӡ����
		m_condition.setShowPrintStatusPanel(true);
		m_condition.addCurToCurDate("po_order.dorderdate");
	}
	return	m_condition ;
}

/**
 * ���ߣ���ӡ��
 * ���ܣ��õ���ť
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2003-9-12 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
protected OrderVO[] getQueriedVOHead() throws Exception {

    NormalCondVO[] normalVos = ((OrderUIQueDlg) getPoQueryCondition()).getNormalVOs(false);
    ConditionVO[] voaUserDefined = getPoQueryCondition().getConditionVO();
    if(voaUserDefined  == null || (voaUserDefined != null && voaUserDefined.length == 0)){
    	return getBufferVOManager().getVOs();
    }
    ArrayList listVos = new ArrayList();
    if(normalVos != null && normalVos.length >0){
        int iLen = normalVos.length;
        for (int i = 0; i < iLen; i++) {
            if (normalVos[i] == null) {
                continue;
            }
            listVos.add(normalVos[i]);
        }
        NormalCondVO voTmp = new NormalCondVO();
        voTmp.setKey("ConstrictClosedLines");
        voTmp.setValue("Y");
        listVos.add(voTmp);
		/*����ר������ά���������ɼ��ر���(��ȫ�������о��رյĶ���)
		  �������
		  1)��δ��ѡ�رգ���ѯ�������ݲ������رյĶ����м������رյĶ���
		  2)��ֻ��ѡ�رգ�ֻ��ѯ���رյĶ�����
		  3)����ѡ�ر�ͬʱ��ѡ��������ѯ�������ݰ����ر��м������رյĶ���
		*/
//		if(nc.vo.scm.pub.CustomerConfigVO.getCustomerName().equalsIgnoreCase(nc.vo.scm.pub.CustomerConfigVO.NAME_TIANYIN)){
        if(true){//20050406 xy ghl czp ��������ݵ�V31
			//1)��δ��ѡ�رգ���ѯ�������ݲ������رյĶ����м������رյĶ���
			if(!((OrderUIQueDlg)getPoQueryCondition()).isClosedSelected()){
				voTmp.setValue("Y");
			}
//			// 3)����ѡ�ر�ͬʱ��ѡ��������ѯ�������ݰ����ر��м������رյĶ���
			else 
        if(!((OrderUIQueDlg)getPoQueryCondition()).isOnlyClosed()){
				voTmp.setValue("N");
			}
			//2)��ֻ��ѡ�رգ�ֻ��ѯ���رյĶ�����
			if(((OrderUIQueDlg)getPoQueryCondition()).isOnlyClosed()){
				voTmp.setValue("N");
				NormalCondVO voTmp1 = new NormalCondVO();
				voTmp1.setKey("OnlyClosedLines");
				voTmp1.setValue("Y");
				listVos.add(voTmp1);
			}
		}
		//���Ӳ���Ա����
        voTmp = new NormalCondVO();
        voTmp.setKey(OrderPubVO.NORMALVO_COPERATOR);
        voTmp.setValue(PoPublicUIClass.getLoginUser());
        listVos.add(voTmp);
        
        normalVos = (NormalCondVO[]) listVos.toArray(new NormalCondVO[listVos.size()]);
    }

 
  
	return OrderHelper.queryOrderArrayOnlyHead(normalVos, voaUserDefined,getClientEnvironment().getUser().getPrimaryKey());
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
private OrderUIButton getThisBtnManager(){

	return	((OrderUIButton)getBtnManager()) ;
}

/**
 * ���ߣ������
 * ���ܣ��÷�����ʵ�ֽӿ�ICheckRetVO�ķ������빹�췽��nc.ui.po.oper.OrderUI_bak(String, String, String, String, String)���ʹ��
 *		�ýӿ�Ϊ��������ƣ���ʵ������ʱ�õ�����VO
 *		�벻Ҫ����ɾ�����޸ĸ÷������Ա������
 * ��������
 * ���أ�nc.vo.pub.AggregatedValueObject	Ϊ����VO
 * ���⣺��
 * ���ڣ�(2001-5-13 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public nc.vo.pub.AggregatedValueObject getVo() throws Exception {

	return	getOrderViewVOAt( getBufferVOManager().getVOPos() ) ;

}

/**
 * ���ߣ�����
 * ���ܣ���Ϣ���ĳ�ʼ��
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2001-04-21  11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * 2002-07-25	wyf		ע�͵����չ�˾ID������
 */
private void initMsgCenter() {

	setCurPk_corp( PoPublicUIClass.getLoginPk_corp() );
	//���ؿ�Ƭ
	setLayout(new java.awt.BorderLayout());
	add(getPoCardPanel(),java.awt.BorderLayout.CENTER);
	//���ð�ť��
	setButtons(getThisBtnManager().getBtnaMsgCenter());
	//��ť
	ContractClassParse.resetButton(
		getParameter(ContractClassParse.getParaName()),
		getThisBtnManager().btnBillContractClass) ;

}

/**
 * ���ߣ�WYF
 * ���ܣ���ϰ�ť����
 * ������nc.ui.pub.ButtonObject bo		��ť
 * ���أ���
 * ���⣺��
 * ���ڣ�(2003-9-12 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
protected void onButtonClickedBill(nc.ui.pub.ButtonObject bo) {
  
	if(bo == getThisBtnManager().btnMsgCenterAudit){
		onMsgCenterAudit();
	}else if(bo == getThisBtnManager().btnMsgCenterUnAudit){
		onMsgCenterUnAudit();
		//�������޶�
	}else if(bo == getThisBtnManager().btnBillMaintainRevise){
		onMsgRevise();
	}
	//֧�ֲ�ҵ��������չ
	else if(PuTool.isExist(getExtendBtns(),bo)){
		  onExtendBtnsClick(bo);
    }
	else{
		super.onButtonClickedBill(bo) ;
	}
}
/**
 * �������޶�
 */
private void onMsgRevise(){
	bRevise = true;
	bAddNew = false; 
	// ���ð�ť
	setButtonsStateEdit() ;
	OrderVO vo = getOrderViewVOAt(getBufferVOManager().getVOPos());
	// ���ñ༭��
	getPoCardPanel().onActionRevise(vo) ;
	// ˢ��
	updateUI();
}
/**
 * ���ܣ��÷���Ϊ�������ṩ�����������޸ļ�ɾ��
 *		������˫����Ϣ�󣬵���OrderUI���棬���ϵİ�ť��������ť����Ӧ�˺���
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2002-10-15 16:15:35)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * 2003-03-05	wyf		�޸�������Ե��ݵ���ʾ
 */
private void onMsgCenterAudit() {

	OrderVO vo = getOrderViewVOAt(getBufferVOManager().getVOPos());
	PoPublicUIClass.setCuserId(new	OrderVO[]{vo}) ;

	OrderVO VOs[] = new OrderVO[1];
	VOs[0] = new OrderVO();
	VOs[0].getHeadVO().setCorderid(vo.getHeadVO().getCorderid());
	VOs[0].getHeadVO().setTs(vo.getHeadVO().getTs());
  //���������˼��������ڹ�ϣ������ʧ��ʱ�õ�
  HashMap<String, ArrayList<Object>> mapAuditInfo = new HashMap<String, ArrayList<Object>>();
  ArrayList<Object> listAuditInfo = null;
  OrderHeaderVO headVO = null;
  for (int i = 0; i < VOs.length; i++) {
    headVO = VOs[i].getHeadVO();
    //����ʧ��ʱ�õ������������˼��������ڹ�ϣ��
    if(PuPubVO.getString_TrimZeroLenAsNull(headVO.getCauditpsn()) != null){
      listAuditInfo = new ArrayList<Object>();
      listAuditInfo.add(headVO.getCauditpsn());
      listAuditInfo.add(headVO.getDauditdate());
      mapAuditInfo.put(headVO.getPrimaryKey(),listAuditInfo);
    }
    headVO.setCauditpsn(getClientEnvironment().getUser().getPrimaryKey());
    headVO.setDauditdate(getClientEnvironment().getDate());
  }
	try {
		String strErr = PuTool.getAuditLessThanMakeMsg(VOs,"dorderdate","vordercode", getClientEnvironment().getDate(),ScmConst.PO_Order);
		if (strErr != null) {
			throw new BusinessException(strErr);
		}
		/*
		//�����вɹ�����������Ա
		String strOpr = getClientEnvironment().getUser().getPrimaryKey();
		for (int i = 0; i < VOs.length; i++) {
			OrderHeaderVO headVO = VOs[i].getHeadVO();
			headVO.setCoperator(strOpr);
		}
		*/
	    Object[] objs = new Object[1];
	    objs[0] = new ClientLink(getClientEnvironment());

	    HashMap hmPfExParams = new HashMap();
	    hmPfExParams.put(PfUtilBaseTools.PARAM_RELOAD_VO, PfUtilBaseTools.PARAM_RELOAD_VO);

			PfUtilClient.runBatch(
				this,
				"APPROVE"+nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				ScmConst.PO_Order,
				getClientEnvironment().getDate().toString(),
				VOs,
				objs,null,hmPfExParams);
		if (!PfUtilClient.isSuccess()) {
			//���������˼���������
			if(mapAuditInfo.size() > 0){
				for (int i = 0; i < VOs.length; i++) {
					headVO = VOs[i].getHeadVO();
					if(!mapAuditInfo.containsKey(headVO.getPrimaryKey())){
						headVO.setCauditpsn(null);
						headVO.setDauditdate(null);
						continue;
					}          
					listAuditInfo = (ArrayList<Object>)mapAuditInfo.get(headVO.getPrimaryKey());
					headVO.setCauditpsn((String)listAuditInfo.get(0));
					headVO.setDauditdate((UFDate)listAuditInfo.get(1));
				}
			}
			//�������ɹ�����
			return;
		} else {
			String billID = vo.getParentVO().getPrimaryKey();
      OrderVO[] voNewOrder = OrderHelper.queryOrderVOsLight(
          new String[] {billID}, "AUDIT");
		/************��¼ҵ����־*************/
      //since 56 ��̨����־����
//      Operlog operlog=new Operlog();
//      voNewOrder.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
//      voNewOrder.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
//      voNewOrder.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
//      operlog.insertBusinessExceptionlog(voNewOrder, "����", "����", nc.vo.scm.funcs.Businesslog.MSGMESSAGE, nc.vo.scm.pu.BillTypeConst.PO_ORDER,
//				nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
      /************��¼ҵ����־* end ************/
//    ˢ�±�ͷVO
      if(voNewOrder == null){
        return;
      }
      getBufferVOManager().refreshByHeaderVo(voNewOrder[0].getHeadVO());

      //��������
      getPoCardPanel().reloadBillCardAfterAudit(getOrderDataVOAt(
          getBufferVOManager().getVOPos())
          ,getBufferVOManager().getVOAt_LoadItemNo(getBufferVOManager().getVOPos()));

			//ˢ�½������ݣ���ʾ�����ˡ���������
			//ע���˴����ɵ���displayCurVOToBillPanel
//			getPoCardPanel().displayCurVO( getOrderViewVOAt(getBufferVOManager().getVOPos()) , false);
		}
    //
    setButtonsStateBrowse();
    //
		showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000236")/*@res "��˳ɹ�"*/);
	} catch (Exception e) {
		nc.ui.pu.pub.PuTool.outException(this,e) ;
	}
}
/**
 * ���ߣ���־ƽ
 * ���ܣ��÷���Ϊ�������ṩ�����������޸ļ�ɾ��
 *		������˫����Ϣ�󣬵���OrderUI���棬���ϵİ�ť�� ���� ��ť����Ӧ�˺���
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2005-06-27 15:49:35)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
private void onMsgCenterUnAudit() {

	OrderVO vo = getOrderViewVOAt(getBufferVOManager().getVOPos());
	PoPublicUIClass.setCuserId(new	OrderVO[]{vo}) ;

	OrderVO VOs[] = new OrderVO[1];
	VOs[0] = vo;
  //���������˼��������ڹ�ϣ������ʧ��ʱ�õ�
  HashMap<String, ArrayList<Object>> mapAuditInfo = new HashMap<String, ArrayList<Object>>();
  ArrayList<Object> listAuditInfo = null;
  OrderHeaderVO headVO = null;
	for (int i = 0; i < VOs.length; i++) {
		headVO = VOs[i].getHeadVO();
    //����ʧ��ʱ�õ������������˼��������ڹ�ϣ��
    if(PuPubVO.getString_TrimZeroLenAsNull(headVO.getCauditpsn()) != null){
      listAuditInfo = new ArrayList<Object>();
      listAuditInfo.add(headVO.getCauditpsn());
      listAuditInfo.add(headVO.getDauditdate());
      mapAuditInfo.put(headVO.getPrimaryKey(),listAuditInfo);
    }
    	vo.getHeadVO().setAttributeValue("cauditpsnold",vo.getHeadVO().getCauditpsn());//Ϊ�ж��Ƿ������������˵ĵ���
		headVO.setCauditpsn(getClientEnvironment().getUser().getPrimaryKey());
		headVO.setDauditdate(getClientEnvironment().getDate());
	}
	if(headVO.getBcooptoso()!=null&&headVO.getBcooptoso().booleanValue()){
		MessageDialog.showHintDlg(this,
				NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/* @res "��ʾ" */,
				NCLangRes.getInstance().getStrByID("4004020201","UPP4004020201-V56001")/* @res "�ɹ������Ѿ�Эͬ�������۶���,��������" */);
		return ;
	}
  OrderVO[] voNewOrder = null;
	try {
		String strOpr = getClientEnvironment().getUser().getPrimaryKey();
		/*
		for (int i = 0; i < VOs.length; i++) {
			OrderHeaderVO headVO = VOs[i].getHeadVO();
			headVO.setCoperator(strOpr);
		}
		*/
			PfUtilClient.processBatchFlow(
				this,
				"UNAPPROVE"+strOpr,
        ScmConst.PO_Order,
				getClientEnvironment().getDate().toString(),
				VOs,
				null);    
		if (!PfUtilClient.isSuccess()) {
      //���������˼���������
      if(mapAuditInfo.size() > 0){
        for (int i = 0; i < VOs.length; i++) {
          headVO = VOs[i].getHeadVO();
          if(!mapAuditInfo.containsKey(headVO.getPrimaryKey())){
            headVO.setCauditpsn(null);
            headVO.setDauditdate(null);
            continue;
          }          
          listAuditInfo = (ArrayList<Object>)mapAuditInfo.get(headVO.getPrimaryKey());
          headVO.setCauditpsn((String)listAuditInfo.get(0));
          headVO.setDauditdate((UFDate)listAuditInfo.get(1));
        }
      }
			//�������ɹ�����
			return;
		} else {
			String billID = vo.getParentVO().getPrimaryKey();
//			getBufferVOManager().resetVOs(
//				new	OrderVO[]{OrderHelper.queryOrderVOByKey(billID)} ) ;
//      
      voNewOrder = OrderHelper.queryOrderVOsLight(new String[]{billID},"UNAUDIT");
//
//			getBufferVOManager().setVOPos(0);
//
//			//ˢ�½������ݣ���ʾ�����ˡ���������
//			//ע���˴����ɵ���displayCurVOToBillPanel
//			getPoCardPanel().displayCurVO( getOrderViewVOAt(getBufferVOManager().getVOPos()), false );
//			updateButtons();
		}
		/************��¼ҵ����־*************/
//	      Operlog operlog=new Operlog();
//	      for (OrderVO orderVO : VOs) {
//	    	  orderVO.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
//	    	  orderVO.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
//	    	  orderVO.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
//	    	  operlog.insertBusinessExceptionlog(orderVO, "����", "����", nc.vo.scm.funcs.Businesslog.MSGMESSAGE, nc.vo.scm.pu.BillTypeConst.PO_ORDER,
//						nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
//		}
	      /************��¼ҵ����־* end ************/
		//ˢ�±�ͷVO
    getBufferVOManager().refreshByHeaderVo(voNewOrder[0].getHeadVO());
    //��������
    getPoCardPanel().reloadBillCardAfterAudit(voNewOrder[0],getBufferVOManager().getVOAt_LoadItemNo(getBufferVOManager().getVOPos()));
    //
    setButtonsStateBrowse();
    //
		showHintMessage(NCLangRes.getInstance().getStrByID("common","UCH011")/*@res "����ɹ�"*/);
	} catch (Exception e) {
	      //���������˼���������
	      if(mapAuditInfo.size() > 0){
	        for (int i = 0; i < VOs.length; i++) {
	          headVO = VOs[i].getHeadVO();
	          if(!mapAuditInfo.containsKey(headVO.getPrimaryKey())){
	            headVO.setCauditpsn(null);
	            headVO.setDauditdate(null);
	            continue;
	          }          
	          listAuditInfo = (ArrayList<Object>)mapAuditInfo.get(headVO.getPrimaryKey());
	          headVO.setCauditpsn((String)listAuditInfo.get(0));
	          headVO.setDauditdate((UFDate)listAuditInfo.get(1));
	        }
	      }
		nc.ui.pu.pub.PuTool.outException(this,e) ;
	}
}
/**
 * ���ο���������չ��ť��Ҫ����ο��������������ʵ��
 * 
 * @see nc.ui.scm.pub.bill.IBillExtendFun#getExtendBtns()
 *
 */
public ButtonObject[] getExtendBtns() {
	return null;
}
/**
 * ������ο�����ť�����Ӧ����Ҫ����ο��������������ʵ��
 * 
 * @see nc.ui.scm.pub.bill.IBillExtendFun#onExtendBtnsClick(nc.ui.pub.ButtonObject)
 * 
 */
public void onExtendBtnsClick(ButtonObject bo) {
	
}
/**
 * ���ο���״̬��ԭ�н���״̬������Ҫ����ο��������������ʵ��
 * 
 * @see nc.ui.scm.pub.bill.IBillExtendFun#setExtendBtnsStat(int)
 * 
 *  ״̬��ֵ���ձ�
 * 
 *  0����ʼ��
 *  1�������Ƭ
 *  2���޸�
 *  3������б�
 *  4��ת���б�
 */
public void setExtendBtnsStat(int iState) {
		
}
/**
 * ��������ӿڷ���ʵ�� -- ά��
 **/
public void doMaintainAction(ILinkMaintainData maintaindata) {
	SCMEnv.out("���붩��ά���ӿ�...");
	
	String billID = maintaindata.getBillID(); 
	
	setCurPk_corp( PoPublicUIClass.getLoginPk_corp() );
	//���ؿ�Ƭ
	setLayout(new java.awt.BorderLayout());
	add(getPoCardPanel(),java.awt.BorderLayout.CENTER);

	//���ÿ�Ƭ���������ֶ���ʾ��
	//BillData data = getPoCardPanel().getBillData();
	getPoCardPanel().getBodyItem("naccumarrvnum").setShow(true);
	getPoCardPanel().getBodyItem("naccumstorenum").setShow(true);
	getPoCardPanel().getBodyItem("naccuminvoicenum").setShow(true);

	//setCauditid(billID);

	//û�з��������Ķ���
	try {
		
		getPoListPanel().getHeadBillModel().clearBodyData();
		getPoListPanel().getBodyBillModel().clearBodyData();
		getBufferVOManager().resetVOs(new OrderVO[]{OrderHelper.queryOrderVOByKey(billID)}) ;
	} catch (Exception	e) {
		SCMEnv.out(e);
		MessageDialog.showHintDlg(this,NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/,OrderPubVO.returnHintMsgWhenNull(e.getMessage())) ;
	}
	//�����ǰ��¼��˾���ǲ���Ա�Ƶ����ڹ�˾��������޲�����ť�����ṩ������ܣ�by chao , xy , 2006-11-07
	String strLoginCorpId = PoPublicUIClass.getLoginPk_corp();
	String strPrayCorpId = getOrderViewVOAt(0) == null ? null : getOrderViewVOAt(0).getHeadVO().getPk_corp();
	boolean bSameCorpFlag = strLoginCorpId.equals(strPrayCorpId);
	
	if(getBufferLength()==0){
		MessageDialog.showHintDlg(this,NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/,NCLangRes.getInstance().getStrByID("4004020201","UPP4004020201-000023")/*@res "û�з��ϲ�ѯ�����Ķ���"*/) ;
		if(bSameCorpFlag){
			getPoCardPanel().addNew() ;
		}else{
			setButtonsNull();
		}
		return ;
	}

	//���ö���VO����
	getBufferVOManager().setVOPos(0) ;

	//���ÿ�Ƭ��������
	getPoCardPanel().displayCurVO( getOrderViewVOAt( getBufferVOManager().getVOPos() ) , false) ;

	//���ò���Ա
	PoPublicUIClass.setCuserId(
		new	OrderVO[]{
			getOrderViewVOAt(getBufferVOManager().getVOPos())
		}
	) ;

  //����ͬһ��˾�����а�ť���ɼ�
  if(!bSameCorpFlag){
    setButtonsNull();
    return;
  }
  //�����ͬһ��˾
  OrderVO voOrder = getOrderViewVOAt( getBufferVOManager().getVOPos() );
  if(voOrder.getHeadVO().isAuditted()){
    setButtonsStateBrowse();
    return;
  }
  setButtonsStateBrowse();
}
/**
 * ��������ӿڷ���ʵ�� -- ����
 **/
public void doAddAction(ILinkAddData adddata) {
	SCMEnv.out("���붩�������ӿ�...");
	if(adddata == null){
		SCMEnv.out("���ε��òɹ�������������ʱδ��ȷ����������ݣ�ֱ�ӷ���!");
		return;
	}
	//
	String strUpBillType = adddata.getSourceBillType();
	AggregatedValueObject[] vos = null;
	//��Դ�ڼ۸�������
	if(adddata.getUserObject() != null
			&& adddata.getUserObject() instanceof PUMessageVO){
		
		PUMessageVO msgVo =  (PUMessageVO) adddata.getUserObject();
		
		//since v55:ֻ�м۸�������ʱ���������ã�Ϊ֧����ҵ�������������ɶ�������������
		if(msgVo.getAskvo() instanceof nc.vo.pp.ask.PriceauditMergeVO){
			strUpBillType = BillTypeConst.PO_PRICEAUDIT;
		}
		//
		vos = new AggregatedValueObject[]{msgVo.getAskvo()};
	}
	//��Դ���빺��:V5�ݲ�֧��
	else if(false && BillTypeConst.PO_PRAY.equals(strUpBillType)){
		//
		String strLoginCorp =  ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		String strOperator =  ClientEnvironment.getInstance().getUser().getPrimaryKey();
		//		
		Vector<NormalCondVO> m_vecNormalVO = new Vector<NormalCondVO>();
        NormalCondVO voNormalLoginCorp = new NormalCondVO();
        voNormalLoginCorp.setKey(OrderPubVO.NORMALVO_PK_CORP_LOGIN);
        voNormalLoginCorp.setValue(strLoginCorp);
        m_vecNormalVO.addElement(voNormalLoginCorp);

        NormalCondVO voNormalOper = new NormalCondVO();
        voNormalOper.setKey(OrderPubVO.NORMALVO_COPERATOR);
        voNormalOper.setValue(strOperator);
        m_vecNormalVO.addElement(voNormalOper);

        NormalCondVO voNormalSplitFlag = new NormalCondVO();
        voNormalSplitFlag.setKey(OrderPubVO.NORMALVO_SPLIT_FLAG);/*-=�Ƿ�ֵ�=-*/
        voNormalSplitFlag.setValue(UFBoolean.TRUE);
        m_vecNormalVO.addElement(voNormalSplitFlag);
        
        NormalCondVO[] voaNormal = m_vecNormalVO.toArray(new NormalCondVO[m_vecNormalVO.size()]);
                     
		ConditionVO voCond = new ConditionVO();
		voCond.setLogic(true);
		voCond.setFieldCode("po_praybill.cpraybillid");
		voCond.setFieldName("�뵥��ͷID");/*-=notranslate=-*/
		voCond.setOperaCode("=");
		voCond.setOperaName("����");/*-=notranslate=-*/
		voCond.setDataType(0);
		voCond.setValue(adddata.getSourceBillID());
		
		UFDate dateLogin = ClientEnvironment.getInstance().getBusinessDate();
		//
		try{
			vos = PraybillHelper.queryForOrderBill(voaNormal,new ConditionVO[]{voCond},dateLogin,null);
		}catch(Exception e){			
			SCMEnv.out(e);
			return;
		}
	}
	//��Դ����������
	
	//����ͳһ����	
	processAfterChange(strUpBillType, vos);
}
/**
 * ��������ӿڷ���ʵ�� -- ����
 **/
public void doApproveAction(ILinkApproveData approvedata) {
	SCMEnv.out("���붩�������ӿ�...");
	//-------------��Ϣ����
	//��ʼ��
//	initMsgCenter() ;

	//û�з��������Ķ���
	try {
		OrderVO	voOrder = OrderHelper.queryOrderVOByKey( approvedata.getBillID() )  ;
		getBufferVOManager().resetVOs( voOrder==null ? null : new	OrderVO[]{voOrder});
	} catch (Exception	e) {
		int		iBtnLen = getThisBtnManager().getBtnaMsgCenter().length ;
		for (int i = 0; i < iBtnLen; i++){
			getThisBtnManager().getBtnaMsgCenter()[i].setEnabled(false) ;
		}
		updateButtons();

		PuTool.outException(this,e) ;
		return ;
	}
	//���ð�ť״̬
	if (getBufferLength()==0) {
		MessageDialog.showHintDlg(this,NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/,NCLangRes.getInstance().getStrByID("4004020201","UPP4004020201-000023")/*@res "û�з��ϲ�ѯ�����Ķ���"*/) ;
		getPoCardPanel().addNew() ;

		int		iBtnLen = getThisBtnManager().getBtnaMsgCenter().length ;
		for (int i = 0; i < iBtnLen; i++){
			getThisBtnManager().getBtnaMsgCenter()[i].setEnabled(false) ;
		}
		updateButtons();
		return ;
	}
  
	//���ö���VO����
	getBufferVOManager().setVOPos(0) ;
	//���ÿ�Ƭ��������
	OrderVO	voCur = getOrderViewVOAt( getBufferVOManager().getVOPos() ) ;
	//���ò���Ա
	PoPublicUIClass.setCuserId(new	OrderVO[]{voCur}) ;

	getPoCardPanel().displayCurVO(voCur, false) ;
	getPoCardPanel().setEnabled(false) ;

  //�����Ƿ��Ѿ�����,���а�ť����ʾ��ֱ�ӷ���
  boolean bFrozenFlag = false ;
  if ( voCur.getHeadVO().getForderstatus().compareTo(BillStatus.FREEZE)==0 ) {
    MessageDialog.showHintDlg(this,NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/,NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000384")/*@res "�õ����Ѷ��ᣡ"*/) ;
    bFrozenFlag = true ;
  }
  if(bFrozenFlag){
    setButtonsNull();
    return;
  }
  //�Ƿ���ͬ��˾
  boolean bCorpSameFlag = getCorpPrimaryKey().equals(voCur.getPk_corp()) ;
  
  //���ð�ť�鼰״̬
  
  //��ͬ��˾���ߵ��������ť�߼�
  if(bCorpSameFlag){
    setButtons(getThisBtnManager().getBtnTree(this).getButtonArray());
    
  }
  //��ͬ��˾������ԭ�д���
  else{
    setButtons(getThisBtnManager().getBtnaMsgCenter());
    getThisBtnManager().getBtnaMsgCenter()[0].setEnabled( voCur.isCanBeAudited() ) ;
    getThisBtnManager().getBtnaMsgCenter()[1].setEnabled( voCur.isCanBeUnAudited() ) ;
    getBtnManager().btnBillLnkQry.setEnabled(true);
    getBtnManager().btnListDocManage.setEnabled(true);
    getBtnManager().btnBillStatusQry.setEnabled(true);
    
  }
  getBtnManager().btnBillMaintainRevise.setVisible(true);
  //��ͬ��˾������ť������
  setButtonsStateBrowse();
  getThisBtnManager().getBtnaMsgCenter()[1].setEnabled( voCur.isCanBeUnAudited() ) ;
  updateButtonsAll();
}
/**
 * ��������ӿڷ���ʵ�� -- ������ 
 **/
public void doQueryAction(ILinkQueryData querydata) {
	SCMEnv.out("���붩��������ӿ�...");

	String billID = querydata.getBillID(); 
	
	setCurPk_corp( PoPublicUIClass.getLoginPk_corp() );
	//���ؿ�Ƭ
	setLayout(new java.awt.BorderLayout());
	add(getPoCardPanel(),java.awt.BorderLayout.CENTER);

	//���ÿ�Ƭ���������ֶ���ʾ��
	//BillData data = getPoCardPanel().getBillData();
	getPoCardPanel().getBodyItem("naccumarrvnum").setShow(true);
	getPoCardPanel().getBodyItem("naccumstorenum").setShow(true);
	getPoCardPanel().getBodyItem("naccuminvoicenum").setShow(true);

	//setCauditid(billID);

	//û�з��������Ķ���
	try {
		getBufferVOManager().resetVOs(new OrderVO[]{OrderHelper.queryOrderVOByKey(billID)}) ;
	} catch (Exception	e) {
		SCMEnv.out(e);
		MessageDialog.showHintDlg(this,NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/,OrderPubVO.returnHintMsgWhenNull(e.getMessage())) ;
	}

	if(getBufferLength()==0){
		MessageDialog.showHintDlg(this,NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/,NCLangRes.getInstance().getStrByID("4004020201","UPP4004020201-000023")/*@res "û�з��ϲ�ѯ�����Ķ���"*/) ;
		getPoCardPanel().addNew() ;
		return ;
	}
  //
  getBufferVOManager().setVOPos(0) ;
  //
  OrderVO vo = getOrderViewVOAt( getBufferVOManager().getVOPos() );
  //
  String strPkCorp = vo.getPk_corp();
  //���յ���������˾���ز�ѯģ�� 
  OrderUIQueDlg queryDlg = new OrderUIQueDlg(this, 
      getModuleCode(), 
      strPkCorp,
      getClientEnvironment().getUser().getPrimaryKey());
  
  //��ѯģ����û�й�˾ʱ��Ҫ�������⹫˾
  queryDlg.initCorpRefsDataPower(OrderUIQueDlg.CORPKEY, OrderUIQueDlg.getPowerCodes(), new String[]{strPkCorp} );
  
  //���ù���������ȡ�ù�˾�п���Ȩ�޵ĵ�������VO����
  ConditionVO[] voaPower = queryDlg.getDataPowerConVOs(strPkCorp,PoQueryCondition.getPowerCodesOrderUI());
  
  //��֯��ѯ����VO
  ConditionVO[] voaUserDefined = null;
  ConditionVO voHid = new ConditionVO();
  voHid.setFieldCode("po_order.corderid");
  voHid.setFieldName("�ɹ�����ͷID");/*-=notranslate=-*/
  voHid.setOperaCode("=");
  voHid.setOperaName("����");
  voHid.setValue(billID);
  if(voaPower == null || voaPower.length == 0){
    voaUserDefined = new ConditionVO[1];
    voaUserDefined[0] = voHid;
  }else{
    voaUserDefined = new ConditionVO[voaPower.length + 1];
    for(int i=0; i<voaPower.length; i++){
      voaUserDefined[i] = voaPower[i];
    }
    voaUserDefined[voaPower.length] = voHid;
  }
  //��֯�ڶ��β�ѯ���ݣ�����Ȩ�޺͵���PK����  
  NormalCondVO voNormal = new NormalCondVO();
  voNormal.setKey(OrderPubVO.NORMALVO_PK_CORP_QUERY);
  voNormal.setValue(strPkCorp);
  
  NormalCondVO voNormalClosed = new NormalCondVO();
  voNormalClosed.setKey(OrderPubVO.NORMALVO_INCLUDING_CLOSED);
  voNormalClosed.setValue("Y");
  
  OrderVO[] voaRet = null;
  try{
    voaRet = OrderHelper.queryOrderArrayOnlyHead(new NormalCondVO[] {voNormal, voNormalClosed}, voaUserDefined,getClientEnvironment().getUser().getPrimaryKey());
  }catch(Exception e){
    MessageDialog.showHintDlg(this, 
        NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/* "��ʾ" */, 
        e.getMessage());
    setButtonsNull();
    return;
  }
  if(voaRet == null || voaRet.length <= 0 || voaRet[0] == null){
    MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/* "��ʾ" */, 
        NCLangRes.getInstance().getStrByID("common", "SCMCOMMON000000161")/* "û�в쿴���ݵ�Ȩ��" */);
    return;
  }
	//���ÿ�Ƭ��������
	getPoCardPanel().displayCurVOForLinkQry( vo , false) ;

	getPoCardPanel().setEnabled(false) ;

	//���ò���Ա
	PoPublicUIClass.setCuserId(
		new	OrderVO[]{
			getOrderViewVOAt(getBufferVOManager().getVOPos())
		}
	) ;
  //�����Ƿ��Ѿ�����,���а�ť����ʾ��ֱ�ӷ���
  boolean bFrozenFlag = false ;
  if ( vo.getHeadVO().getForderstatus().compareTo(BillStatus.FREEZE)==0 ) {
    MessageDialog.showHintDlg(this,NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/,NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000384")/*@res "�õ����Ѷ��ᣡ"*/) ;
    bFrozenFlag = true ;
  }
  if(bFrozenFlag){
    setButtonsNull();
    return;
  }
  boolean bCorpSameFlag = getCorpPrimaryKey().equals(vo.getPk_corp()) ;
  //���ð�ť��
  if(bCorpSameFlag){
    setButtons(getThisBtnManager().getBtnTree(this).getButtonArray());
    setButtonsStateBrowse();
  }else{
    setButtonsNull();
  }
}
/**
 * ��յ�ǰ���水ť
 */
private void setButtonsNull(){
	ButtonObject[] objs = getButtons();
	int iLen = objs == null ? 0 : objs.length;
	for (int i = 0; i < iLen; i++) {
		if (objs[i] == null) {
			continue;
		}
		objs[i].setVisible(false);
		updateButton(objs[i]);
	}
}
//zhf add---------------------------------------------------------------------------------
public ButtonObject m_btnModifyPact = new ButtonObject("�޸�","�޸�",2,"�޸�");
public ButtonObject m_btnSavePact = new ButtonObject("����","����",2,"����");
public ButtonObject m_btnCancelPact = new ButtonObject("ȡ��","ȡ��",2,"ȡ��");
private ButtonObject m_btnLinePact = new ButtonObject("�в���","�в���",2,"�в���");
public ButtonObject m_btnAddLinePact = new ButtonObject("����","����",2,"����");
public ButtonObject m_btnDelLinePact = new ButtonObject("ɾ��","ɾ��",2,"ɾ��");
private ButtonObject[] m_pactbuttons = new ButtonObject[]{m_btnModifyPact,m_btnLinePact,m_btnSavePact,m_btnCancelPact};

private Map<String,PactItemVO[]> m_pactItemInfor = null;
public Map<String,PactItemVO[]> getPactItemInfor(){
 if(m_pactItemInfor == null){
	 m_pactItemInfor = new HashMap<String, PactItemVO[]>();
 }
 return m_pactItemInfor;
}
private void switchButton(boolean flag){
	if(flag){
		setButtons(m_pactbuttons);
	}else
		setButtons(getThisBtnManager().getBtnTree(this).getButtonArray());
	updateUI();
}
public void stateChanged(ChangeEvent e) {
	// TODO Auto-generated method stub
	if(getCurOperState()!=STATE_LIST_BROWSE&&getCurOperState() != STATE_BILL_BROWSE){
		return;
	}
	
	if(getPoCardPanel().getBodyTabbedPane().getSelectedIndex()==HgPubConst.PO_PACT_TABLECODE_INDEX){
		refreshPactItems();
		switchButton(true);
		
	}else{
		switchButton(false);
	}	
}

private void refreshPactItems(){

	if(getBufferVOManager().getLength()==0)
		return;
	OrderHeaderVO head = getBufferVOManager().getHeadVOAt(getBufferVOManager().getVOPos());
	String headid = PuPubVO.getString_TrimZeroLenAsNull(head.getPrimaryKey());
	if(headid == null)
		return;
	
	PactItemVO[] pactvos = getPactItemInfor().get(headid);
	
	if(pactvos==null||pactvos.length==0){
		try{
			pactvos = PlanPubHelper.getPactItemsForPO(headid);
			getPactItemInfor().put(headid, pactvos);
		}catch(Exception ee){
			ee.printStackTrace();
			showErrorMessage(HgPubTool.getString_NullAsTrimZeroLen(ee.getMessage()));
			return;
		}		
	}
	getPoCardPanel().getBillModel(HgPubConst.PU_PACT_ITEM_TABLECODE).setBodyDataVO(pactvos);
	getPoCardPanel().getBillModel(HgPubConst.PU_PACT_ITEM_TABLECODE).execLoadFormula();	
}

}
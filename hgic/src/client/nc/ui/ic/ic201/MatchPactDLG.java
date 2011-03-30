package nc.ui.ic.ic201;

import java.util.HashMap;
import java.util.Map;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.vo.hg.ic.ic201.PactVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;

/**
 * ��ͬ��¼�Ի���   zhw  ����Զ�ƥ�䰴ť ʵ�� �Զ�ƥ���㷨
 * @author zhf
 * 
 * ��ͷΪ ��Ҫƥ���ͬ�� �����    ����Ϊ��ͬ��   �����һ��һ��ƥ��  ����ƥ��Ŀ�ȡ��ƥ��
 * 
 * �Զ�ƥ��  ϵͳ�Զ�ƥ���ͬ����=������� ƥ������˳  ��ͬ��+�к�  ��С����
 * 
 *
 */

public class MatchPactDLG extends UIDialog  implements BillEditListener,IBillRelaSortListener2 {
	private static final long serialVersionUID = -39986234234258916L;
	
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	private BillListPanel listPane = null;
	
	private nc.ui.pub.beans.UIPanel ivjPnButton = null;
	private nc.ui.pub.beans.UIButton ivjBnCancel = null;
	private nc.ui.pub.beans.UIButton ivjBnOk = null;
	
	private nc.ui.pub.beans.UIButton ivjBnCanMatch = null;
	private nc.ui.pub.beans.UIButton ivjBnMatch = null;
	private nc.ui.pub.beans.UIButton ivjBnAutoMatch = null;
	
	private MatchPactEventHandler m_handler = null;
	
	private ToftPanel parent = null;
//	private  ClientEnvironment ce=null;
	
	
	//----------------------------����
	private GeneralBillItemVO[] m_gitems = null;
	private GeneralBillHeaderVO m_ghead = null;
	private int m_currow = -1;
	
	private Map<String,PactVO[]> m_pactInfor = null;//��ͬ��Ϣ invbasid---packvo
	
	private Map<String,PactVO[]> m_matchInfor = null;//ƥ����Ϣ  icbid  ---- packvo
	
	public GeneralBillVO getNewGbillvo(){
		return m_handler.getNewGbillvo();
	}
	
	
	public MatchPactDLG(ToftPanel tp) {
		super(tp);
		this.parent = tp;
//		this.ce=ce;
		init();
	}
	
	protected Map<String,PactVO[]> getMatchInfor(){
		if(m_matchInfor == null)
			m_matchInfor = new HashMap<String, PactVO[]>();
		return m_matchInfor;
	}
	
	protected Map<String, PactVO[]> getPactInfor(){
		if(m_pactInfor == null){
			m_pactInfor = new HashMap<String, PactVO[]>();
		}
		return m_pactInfor;
	}
	
	public boolean initData(GeneralBillVO gbillvo) throws Exception{
		//У��items  ����û�к�ͬ��Ϣ
		getMatchInfor().clear();
		getPactInfor().clear();
		
		getBillListPanel().getBodyBillModel().clearBodyData();
		
		m_ghead = gbillvo.getHeaderVO();
		m_gitems = gbillvo.getItemVOs();
		boolean flag = true;
		for(GeneralBillItemVO item:m_gitems){
			if(PuPubVO.getString_TrimZeroLenAsNull(item.getCsourcetype())==null){
				item.setAttributeValue("ismatch", UFBoolean.FALSE);
				flag = false;
			}				
			else
				item.setAttributeValue("isoldmatch", UFBoolean.TRUE);
		}
		
		if(flag)
			return flag;//��Դ��ͬ����ⵥ����Ҫƥ���ͬ
		
		m_pactInfor = m_handler.loadPactInfor(m_ghead.getCproviderid(),m_gitems);

		getBillListPanel().getHeadBillModel().setBodyDataVO(m_gitems);
		getBillListPanel().getHeadBillModel().execLoadFormula();
		return flag;
	}
	
	
	
	private void init() {
		setSize(1000,500);
		setTitle("��ͬƥ��");
		setContentPane(getUIDialogContentPane());
		
		m_handler = new MatchPactEventHandler(parent,this);
		
		initListener();
		
	}
	
	private void initListener(){
		getBnOk().addActionListener(m_handler);
		getBnCancel().addActionListener(m_handler);
		getBnMatch().addActionListener(m_handler);
		getBnCanMatch().addActionListener(m_handler);
		getBnAutoMatch().addActionListener(m_handler);
		
		getBillListPanel().getParentListPanel().addEditListener(this);
		getBillListPanel().getHeadBillModel().addSortRelaObjectListener2(this);
	}
	
	
	private javax.swing.JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new javax.swing.JPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new java.awt.BorderLayout());
				getUIDialogContentPane().add(getPnButton(), "South");
				getUIDialogContentPane().add(getBillListPanel(), "Center");
			} catch (java.lang.Throwable ivjExc) {
			}
		}
		return ivjUIDialogContentPane;
	}
	
	public BillListPanel getBillListPanel() {
		if (listPane == null) {
			listPane = new BillListPanel();
			listPane.loadTemplet(HgPubConst.SUPPLY_PACT_DLG_TEMPLET_ID);
			listPane.setEnabled(true);
		}
		return listPane;
	}
	
	
	
	/**
	 * ���� PnButton ����ֵ.
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ����:�˷�������������. */
	private nc.ui.pub.beans.UIPanel getPnButton() {
		if (ivjPnButton == null) {
			try {
				ivjPnButton = new nc.ui.pub.beans.UIPanel();
				ivjPnButton.setName("PnButton");
				getPnButton().add(getBnOk(), getBnOk().getName());	
				getPnButton().add(getBnCancel(), getBnCancel().getName());
				getPnButton().add(getBnMatch(), getBnMatch().getName());	
				getPnButton().add(getBnCanMatch(), getBnCanMatch().getName());
				getPnButton().add(getBnAutoMatch(),getBnAutoMatch().getName());

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjPnButton;
	}
	
	/**
	 * ���� BnOk ����ֵ.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ����:�˷�������������. */
	nc.ui.pub.beans.UIButton getBnAutoMatch() {
		if (ivjBnAutoMatch == null) {
			try {
				ivjBnAutoMatch = new nc.ui.pub.beans.UIButton();
				ivjBnAutoMatch.setName("ivjBnAutoMatch");
				ivjBnAutoMatch.setText("�Զ�ƥ��");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnAutoMatch;
	}

	/**
	 * ���� BnOk ����ֵ.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ����:�˷�������������. */
	nc.ui.pub.beans.UIButton getBnOk() {
		if (ivjBnOk == null) {
			try {
				ivjBnOk = new nc.ui.pub.beans.UIButton();
				ivjBnOk.setName("BnOk");
				ivjBnOk.setText("ȷ��");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnOk;
	}
	/**
	 * ���� BnOk ����ֵ.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ����:�˷�������������. */
	nc.ui.pub.beans.UIButton getBnMatch() {
		if (ivjBnMatch == null) {
			try {
				ivjBnMatch = new nc.ui.pub.beans.UIButton();
				ivjBnMatch.setName("match");
				ivjBnMatch.setText("ƥ��");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnMatch;
	}
	/**
	 * ���� BnOk ����ֵ.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ����:�˷�������������. */
	nc.ui.pub.beans.UIButton getBnCanMatch() {
		if (ivjBnCanMatch == null) {
			try {
				ivjBnCanMatch = new nc.ui.pub.beans.UIButton();
				ivjBnCanMatch.setName("canmatch");
				ivjBnCanMatch.setText("ȡ��ƥ��");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnCanMatch;
	}

	/**
	 * ���� BnCancel ����ֵ.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ����:�˷�������������. */
	nc.ui.pub.beans.UIButton getBnCancel() {
		if (ivjBnCancel == null) {
			try {
				ivjBnCancel = new nc.ui.pub.beans.UIButton();
				ivjBnCancel.setName("BnCancel");
				ivjBnCancel.setText("ȡ��");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnCancel;
	}	

	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		
		//zhw  ʵ��    ��ͷ �б任��  �����ͬͬ���任   �任ǰ  ѯ���Ƿ�  δ����ƥ���ͬ  �Ƿ�ƥ�䣿
		
		//���л���������ݼ���˳��  ���� ��  matchinfor �� �����id ����   �粻����  �� pactinfor  �������ͬ����
		
		m_currow = e.getRow();
		
		//���ȼ����� ��ƥ�� ��Ϣ
		String citemid = m_gitems[m_currow].getPrimaryKey();
		String cinvid = m_gitems[m_currow].getCinvbasid();		
		
		PactVO[] vos = getMatchInfor().get(citemid);
		if(vos==null||vos.length==0){
			vos = getPactInfor().get(cinvid);
		}		
		
		getBillListPanel().getBodyBillModel().clearBodyData();
		getBillListPanel().getBodyBillModel().setBodyDataVO(vos);
		getBillListPanel().getBodyBillModel().execLoadFormula();
		boolean ismatch = PuPubVO.getUFBoolean_NullAs(m_gitems[m_currow].getAttributeValue("ismatch"),UFBoolean.FALSE).booleanValue();
		boolean isoldmatch = PuPubVO.getUFBoolean_NullAs(m_gitems[m_currow].getAttributeValue("isoldmatch"),UFBoolean.FALSE).booleanValue();
		setMatchNumEditEnable(!(ismatch||isoldmatch));
		setButtonEdit(ismatch,isoldmatch);
	}
	protected void setButtonEdit(boolean ismatch,boolean isoldmatch){
		if(isoldmatch){
			getBnMatch().setEnabled(false);
			getBnCanMatch().setEnabled(false);
		}else{
			if(ismatch){
				getBnMatch().setEnabled(false);
				getBnCanMatch().setEnabled(true);
				getBnAutoMatch().setEnabled(false);
			}else{
				getBnMatch().setEnabled(true);
				getBnAutoMatch().setEnabled(true);
				getBnCanMatch().setEnabled(false);
			}
		}
	}
	
	protected GeneralBillItemVO getCurGitem(){
		if(m_currow<0||m_gitems==null||m_gitems.length==0)
			return null;
		
		return m_gitems[m_currow];
	}
	
	protected GeneralBillHeaderVO getCurGHeader(){
		return m_ghead;
	}
	
	protected GeneralBillItemVO[] getAllGitems(){
		return m_gitems;
	}
	public void setMatchNumEditEnable(boolean flag){
		getBillListPanel().getBodyBillModel().getItemByKey("nnum").setEdit(flag);
	}

	public Object[] getRelaSortObjectArray() {
		// TODO Auto-generated method stub
		return m_gitems;
	}

	void showErrorMessage(String msg){
		parent.showErrorMessage(msg);
	}
	void showWarningMessage(String msg){
		parent.showWarningMessage(msg);
	}
	void showHintMessage(String msg){
		parent.showHintMessage(msg);
	}
}

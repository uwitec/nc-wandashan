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
 * 合同补录对话框   zhw  添加自动匹配按钮 实现 自动匹配算法
 * @author zhf
 * 
 * 表头为 需要匹配合同的 入库行    表体为合同行   入库行一行一行匹配  本次匹配的可取消匹配
 * 
 * 自动匹配  系统自动匹配合同数量=入库数量 匹配优先顺  合同号+行号  有小到大
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
	
	
	//----------------------------缓存
	private GeneralBillItemVO[] m_gitems = null;
	private GeneralBillHeaderVO m_ghead = null;
	private int m_currow = -1;
	
	private Map<String,PactVO[]> m_pactInfor = null;//合同信息 invbasid---packvo
	
	private Map<String,PactVO[]> m_matchInfor = null;//匹配信息  icbid  ---- packvo
	
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
		//校验items  必须没有合同信息
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
			return flag;//来源合同的入库单不需要匹配合同
		
		m_pactInfor = m_handler.loadPactInfor(m_ghead.getCproviderid(),m_gitems);

		getBillListPanel().getHeadBillModel().setBodyDataVO(m_gitems);
		getBillListPanel().getHeadBillModel().execLoadFormula();
		return flag;
	}
	
	
	
	private void init() {
		setSize(1000,500);
		setTitle("合同匹配");
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
	 * 返回 PnButton 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告:此方法将重新生成. */
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
	 * 返回 BnOk 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告:此方法将重新生成. */
	nc.ui.pub.beans.UIButton getBnAutoMatch() {
		if (ivjBnAutoMatch == null) {
			try {
				ivjBnAutoMatch = new nc.ui.pub.beans.UIButton();
				ivjBnAutoMatch.setName("ivjBnAutoMatch");
				ivjBnAutoMatch.setText("自动匹配");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnAutoMatch;
	}

	/**
	 * 返回 BnOk 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告:此方法将重新生成. */
	nc.ui.pub.beans.UIButton getBnOk() {
		if (ivjBnOk == null) {
			try {
				ivjBnOk = new nc.ui.pub.beans.UIButton();
				ivjBnOk.setName("BnOk");
				ivjBnOk.setText("确定");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnOk;
	}
	/**
	 * 返回 BnOk 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告:此方法将重新生成. */
	nc.ui.pub.beans.UIButton getBnMatch() {
		if (ivjBnMatch == null) {
			try {
				ivjBnMatch = new nc.ui.pub.beans.UIButton();
				ivjBnMatch.setName("match");
				ivjBnMatch.setText("匹配");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnMatch;
	}
	/**
	 * 返回 BnOk 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告:此方法将重新生成. */
	nc.ui.pub.beans.UIButton getBnCanMatch() {
		if (ivjBnCanMatch == null) {
			try {
				ivjBnCanMatch = new nc.ui.pub.beans.UIButton();
				ivjBnCanMatch.setName("canmatch");
				ivjBnCanMatch.setText("取消匹配");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnCanMatch;
	}

	/**
	 * 返回 BnCancel 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告:此方法将重新生成. */
	nc.ui.pub.beans.UIButton getBnCancel() {
		if (ivjBnCancel == null) {
			try {
				ivjBnCancel = new nc.ui.pub.beans.UIButton();
				ivjBnCancel.setName("BnCancel");
				ivjBnCancel.setText("取消");

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
		
		//zhw  实现    表头 行变换后  表体合同同步变换   变换前  询问是否  未进行匹配合同  是否匹配？
		
		//行切换后表体数据加载顺序  首先 从  matchinfor 按 入库行id 加载   如不存在  从 pactinfor  按存货相同加载
		
		m_currow = e.getRow();
		
		//首先检测加载 已匹配 信息
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

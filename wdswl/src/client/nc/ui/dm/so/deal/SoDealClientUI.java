package nc.ui.dm.so.deal;

import javax.swing.JComponent;

import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillModelRowStateChangeEventListener;
import nc.ui.pub.bill.RowStateChangeEvent;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.wl.pub.LoginInforVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * ���ۼƻ�����
 * 
 * @author Administrator
 * 
 */
public class SoDealClientUI extends ToftPanel implements BillEditListener,BillEditListener2 {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// ���尴ť
	private ButtonObject m_btnQry = new ButtonObject(
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY, 2,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY);
	private ButtonObject m_btnDeal = new ButtonObject(
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL, 2,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL);
	private ButtonObject m_btnSelAll = new ButtonObject(
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL, 2,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL);
	private ButtonObject m_btnSelno = new ButtonObject(
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO, 2,
			WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO);
//	private ButtonObject m_btnXnDeal = new ButtonObject(
//			WdsWlPubConst.DM_PLANDEAL_BTNTAG_XNDEAL,
//			WdsWlPubConst.DM_PLANDEAL_BTNTAG_XNDEAL, 2,
//			WdsWlPubConst.DM_PLANDEAL_BTNTAG_XNDEAL);

	protected ClientEnvironment m_ce = null;
	protected ClientLink cl = null;
	private SoDealEventHandler event = null;

	// �������ģ��

	private BillListPanel m_panel = null;

	// ��ť�¼�����

	private LoginInforHelper helper = null;
	

	private String cwhid;//��ǰ��¼�ͻ������ֿ�

	public String getWhid(){
		return cwhid;
	}

	public LoginInforHelper getLoginInforHelper() {
		if (helper == null) {
			helper = new LoginInforHelper();
		}
		return helper;
	}

	public SoDealClientUI() {
		super();
		m_ce = ClientEnvironment.getInstance();
		cl = new ClientLink(m_ce);
		init();
	}

	public SoDealClientUI(String pk_corp, String pk_billType,
			String pk_busitype, String operater, String billId) {
		super();
		m_ce = ClientEnvironment.getInstance();
		cl = new ClientLink(m_ce);
		init();
		loadData(billId);
	}
	private void init() {
		setLayout(new java.awt.CardLayout());
		add(getPanel(), "a");
		createEventHandler();
		setButton();
		initListener();
		try {
			cwhid  = new LoginInforHelper().getLogInfor(m_ce.getUser().getPrimaryKey()).getWhid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cwhid = null;
		}
	}

	private void initListener() {
		//��ͷ�༭ǰ�����
		getPanel().addEditListener(this);
		getPanel().getParentListPanel().addEditListener2(this);
		//����༭ǰ�����
		BodyEditListener bodyEditListener = new BodyEditListener(); 
		getPanel().addBodyEditListener(bodyEditListener);
		getPanel().getBodyScrollPane("body").addEditListener2(bodyEditListener);
		getPanel().getHeadBillModel().addRowStateChangeEventListener(new HeadRowStateListener());
	}
	/**
	 * lyf:����༭����
	 * @author
	 *
	 */
	private class BodyEditListener  implements BillEditListener,BillEditListener2{
		public void afterEdit(BillEditEvent e) {
			String key  = e.getKey();//���������븺��
			Object value = e.getValue();
			if("nassnum".equalsIgnoreCase(key)){
				UFDouble num = PuPubVO.getUFDouble_NullAsZero(value);
				if(num.doubleValue() <0){
					showWarningMessage("�������Ÿ���");
					getPanel().getBodyBillModel().setValueAt(e.getOldValue(), e.getRow(), key);
					return;
				}
			}else if("nnum".equalsIgnoreCase(key)){
				UFDouble num = PuPubVO.getUFDouble_NullAsZero(value);
				if(num.doubleValue() <0){
					showWarningMessage("�������Ÿ���");
					getPanel().getBodyBillModel().setValueAt(e.getOldValue(), e.getRow(), key);
					return;
				}
			}
		}

		public void bodyRowChange(BillEditEvent e) {
		}

		public boolean beforeEdit(BillEditEvent e) {
			String key  = e.getKey();
			if("nassnum".equalsIgnoreCase(key)){
				if(isGift()){
					return false;
				}else{
					return true;
				}
			}else if("nnum".equalsIgnoreCase(key)){
				if(isGift()){
					return false;
				}else{
					return true;
				}
			}else if("disdate".equalsIgnoreCase(key)){
			
					return true;
				
			}
			return false;
		}
		/**
		 * 
		 * @���ߣ�lyf:�ж��Ƿ���Ʒ��
		 * @˵�������ɽ������Ŀ 
		 * @ʱ�䣺2011-11-17����09:41:46
		 * @return
		 */
		public boolean isGift(){
			boolean isGift = false;
			int count = getPanel().getBodyBillModel().getRowCount();
			for(int row =0;row<count;row++){
				Object value = getPanel().getBodyBillModel().getValueAt(row, "blargessflag");
				isGift = PuPubVO.getUFBoolean_NullAs(value, UFBoolean.FALSE).booleanValue();
				if(isGift){
					return isGift;
				}
			}
			return isGift;
		}
	}
	
	protected BillListPanel getPanel() {
		if (m_panel == null) {
			m_panel = new BillListPanel();
			m_panel.loadTemplet(WdsWlPubConst.WDS4, null, m_ce.getUser()
					.getPrimaryKey(), m_ce.getCorporation().getPrimaryKey());
			m_panel.setEnabled(true);
			m_panel.getParentListPanel().setTotalRowShow(true);
			m_panel.setMultiSelect(true);
			m_panel.getHeadTable().removeSortListener();
		}
		return m_panel;
	}

	
	public void headRowChange(int iNewRow) {
		if (!getPanel().setBodyModelData(iNewRow)) {
			//1.���������������
			loadBodyData(iNewRow);
			//2.���ݵ�ģ����
			getPanel().setBodyModelDataCopy(iNewRow);
		}
		getPanel().repaint();
	}
	
	private void loadBodyData(int row){
		getPanel().getBodyBillModel().clearBodyData();
		String key = (String)getPanel().getHeadBillModel().getValueAt(row, "vreceiptcode");		
		getPanel().getBodyBillModel().setBodyDataVO( event.getSelectBufferData(key));//���ñ���
		getPanel().getBodyBillModel().execLoadFormula();
	}


	private class HeadRowStateListener implements IBillModelRowStateChangeEventListener {
		public void valueChanged(RowStateChangeEvent e) {
			if (e.getRow() != getPanel().getHeadTable().getSelectedRow()) {
				headRowChange(e.getRow());
			}
			BillModel model = getPanel().getBodyBillModel();
			IBillModelRowStateChangeEventListener l = model.getRowStateChangeEventListener();
			model.removeRowStateChangeEventListener();
			if (e.isSelectState()) {
				getPanel().getChildListPanel().selectAllTableRow();
			} else {
				getPanel().getChildListPanel().cancelSelectAllTableRow();
			}
			model.addRowStateChangeEventListener(l);
			getPanel().updateUI();
		}

	}
	

	private void setButton() {
		ButtonObject[] m_objs = new ButtonObject[] { 
				m_btnQry,m_btnSelAll,m_btnSelno, m_btnDeal};
		this.setButtons(m_objs);
	}

	private void createEventHandler() {
		event = new SoDealEventHandler(this);
	}

	public void loadData(String billId) {
		try {
			SoDealVO[] billdatas = SoDealHealper.doQuery(" h.CSALEID = '"
					+ billId + "' ");
			if (billdatas == null || billdatas.length == 0) {
				showHintMessage("��ѯ��ɣ�û����������������");
				return;
			}
			// �����ѯ���ļƻ� ���� ����
			getPanel().getHeadBillModel().setBodyDataVO(billdatas);
			getPanel().getHeadBillModel().execLoadFormula();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onButtonClicked(ButtonObject btn) {
		// TODO Auto-generated method stub
		event.onButtonClicked(btn.getCode());
	}
	

	public void updateButtonStatus(String btnTag, boolean flag) {
		if (btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL)) {
			m_btnDeal.setEnabled(flag);
		} else if (btnTag
				.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY)) {
			m_btnQry.setEnabled(flag);
		}
		updateButtons();
	}
	//��ͷ���л��¼�
	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		if(e.getRow()<0)
			return;
		e.getValue();
		headRowChange(e.getRow());
		
	}
	//��ͷ�༭ǰ�¼�
	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		int row = e.getRow();
		if(e.getPos() == BillItem.HEAD){
			if ("warehousename".equalsIgnoreCase(key)) {
				try {
					LoginInforVO login = getLoginInforHelper().getLogInfor(
							m_ce.getUser().getPrimaryKey());
					if (login.getBistp() == null) {
						return false;
					}
					// ����Ȩ�޵Ĺ��ˣ�ֻ�о�������Ȩ�޵ı���Ա�����ܱ༭����վ
					if (login.getBistp().booleanValue() == true) {
						getPanel().getHeadItem("warehousename").setEnabled(true);
						// ����ֱ�������� �Ĳֿ�
						JComponent c = getPanel().getHeadItem("warehousename")
								.getComponent();
						if (c instanceof UIRefPane) {
							UIRefPane ref = (UIRefPane) c;
							ref.getRefModel().addWherePart(
									" and def1 = '1' and isnull(dr,0) = 0");
						}
						return true;
					} else {
						getPanel().getHeadItem("warehousename").setEnabled(false);
						return false;
					}
				} catch (Exception e1) {
					Logger.error(e1);
				}
			}
		}else{//����༭
			if("nassnum".equalsIgnoreCase(key) || "nnum".equalsIgnoreCase(key)){//������Ʒ�����Ա����
				Object value = getPanel().getBodyBillModel().getValueAt(row, "blargessflag");
				if(PuPubVO.getUFBoolean_NullAs(value, UFBoolean.FALSE).booleanValue()){
					return false;
				}
			}
		}
	
		return true;
	}
	//��ͷ�༭���¼�
	public void afterEdit(BillEditEvent e) {
		String key = e.getKey();
		if("warehousename".equalsIgnoreCase(key)){
			
		}
	}
	public ClientLink getCl() {
		return cl;
	}

	public void setCl(ClientLink cl) {
		this.cl = cl;
	}
}

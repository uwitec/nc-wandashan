package nc.ui.dm.so.deal2;

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
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.wl.pub.LoginInforVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * ���ۼƻ�����2
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
	private SoDealEventHandler m_handler = null;
	private String cwhid;//��ǰ��¼�ͻ������ֿ�
	public String getWhid(){
		return cwhid;
	}
	// �������ģ��
	private BillListPanel m_panel = null;
	// ��ť�¼�����
	private LoginInforHelper helper = null;
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

	protected BillListPanel getPanel() {
		if (m_panel == null) {
			m_panel = new BillListPanel();
			m_panel.loadTemplet(WdsWlPubConst.WDS4_2, null, m_ce.getUser()
					.getPrimaryKey(), m_ce.getCorporation().getPrimaryKey());
			m_panel.setEnabled(true);
//			m_panel.getParentListPanel().setTotalRowShow(true);
			m_panel.getChildListPanel().setTotalRowShow(true);
			m_panel.setMultiSelect(true);
			m_panel.getHeadTable().removeSortListener();
		}
		return m_panel;
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
		//��ͷ����
		getPanel().addEditListener(this);
		getPanel().getHeadBillModel().addRowStateChangeEventListener(new HeadRowStateListener());
		//�������
		BodyEditListener bodyEditListener = new BodyEditListener(); 
		getPanel().getChildListPanel().addEditListener(bodyEditListener);
		getPanel().getChildListPanel().addEditListener2(bodyEditListener);

	}
	/**
	 * lyf:����༭����
	 * @author
	 *
	 */
	private class BodyEditListener  implements BillEditListener,BillEditListener2{
		public void afterEdit(BillEditEvent e) {
			String key = e.getKey();
		}

		public void bodyRowChange(BillEditEvent e) {
			int row = e.getOldRow();
			if(row <0){
				return ;
			}
			int state = getPanel().getBodyBillModel().getRowState(row);
			String csaleid = PuPubVO.getString_TrimZeroLenAsNull(getPanel().getBodyBillModel().getValueAt(row, "csaleid"));
			if(isGift(csaleid)){
				updateGiftState(csaleid,state);
				updateUI();
			}
		}
		/**
		 * 
		 * @���ߣ�������Ʒ��״̬
		 * @˵�������ɽ������Ŀ 
		 * @ʱ�䣺2011-11-24����10:31:16
		 * @param csaleid
		 */
		public void updateGiftState(String csaleid,int state){

			if(csaleid == null || "".equalsIgnoreCase(csaleid)){
				return ;
			}
			int count = getPanel().getBodyBillModel().getRowCount();
			for(int row =0;row<count;row++){
				String csourcebillhid = PuPubVO.getString_TrimZeroLenAsNull(getPanel().getBodyBillModel().getValueAt(row, "csaleid"));
				if(csaleid.equalsIgnoreCase(csourcebillhid)){
					getPanel().getBodyBillModel().setRowState(row, state);
				}
			}
			return ;
		
		}
		/**
		 * 
		 * @���ߣ�lyf:�ж��Ƿ���Ʒ��
		 * @˵�������ɽ������Ŀ 
		 * @ʱ�䣺2011-11-17����09:41:46
		 * @return
		 */
		public boolean isGift(String csaleid){
			if(csaleid == null || "".equalsIgnoreCase(csaleid)){
				return false;
			}
			boolean isGift = false;
			int count = getPanel().getBodyBillModel().getRowCount();
			for(int row =0;row<count;row++){
				String csourcebillhid = PuPubVO.getString_TrimZeroLenAsNull(getPanel().getBodyBillModel().getValueAt(row, "csaleid"));
				if(csaleid.equalsIgnoreCase(csourcebillhid)){
					Object value = getPanel().getBodyBillModel().getValueAt(row, "blargessflag");
					isGift = PuPubVO.getUFBoolean_NullAs(value, UFBoolean.FALSE).booleanValue();
					if(isGift){
						return isGift;
					}
				}
			}
			return isGift;
		}
		public boolean beforeEdit(BillEditEvent e) {
			return false;
		}
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
		getPanel().getBodyBillModel().setBodyDataVO(m_handler.getDataBuffer()[row].getBodyVos());
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
				m_btnSelAll, m_btnSelno,
				m_btnQry, m_btnDeal};
		this.setButtons(m_objs);
	}

	private void createEventHandler() {

		m_handler = new SoDealEventHandler(this);

	}
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onButtonClicked(ButtonObject btn) {
		// TODO Auto-generated method stub

		m_handler.onButtonClicked(btn.getCode());
	}

	public void updateButtonStatus(String btnTag, boolean flag) {
		if (btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL)) {
			m_btnDeal.setEnabled(flag);
		} else if (btnTag
				.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO)) {
			m_btnSelno.setEnabled(flag);
		} else if (btnTag
				.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL)) {
			m_btnSelAll.setEnabled(flag);
		} else if (btnTag
				.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY)) {
			m_btnQry.setEnabled(flag);
		}else if(btnTag
				.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_XNDEAL)){
			//			m_btnXnDeal.setEnabled(flag);
		}
		updateButtons();
	}

	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		int row  = e.getRow();
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
		}else{
			if("nassnum".equalsIgnoreCase(key) || "nnum".equalsIgnoreCase(key)){//������Ʒ�����Ա����
				Object value = getPanel().getBodyBillModel().getValueAt(row, "blargessflag");
				if(PuPubVO.getUFBoolean_NullAs(value, UFBoolean.FALSE).booleanValue()){
					return false;
				}
			}
		}
		
		return true;
	}

	public ClientLink getCl() {
		return cl;
	}

	public void setCl(ClientLink cl) {
		this.cl = cl;
	}

	public void afterEdit(BillEditEvent e) {
		String key = e.getKey();
		
	}

	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		if(e.getRow()<0)
			return;
		headRowChange(e.getRow());
		getPanel().getBodyBillModel().reCalcurateAll();	
	}
	
}
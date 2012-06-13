package nc.ui.wds.ic.inv;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import nc.bs.logging.Logger;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.wds.tray.lock.LockTrayHelper;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.xn.XnRelationVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class ViewLockDLG  extends UIDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel ivjUIDialogContentPane = null;
	protected BillCardPanel ivjbillListPanel = null;
	//	// ��˾Id
	private String m_pkcorp = null;
	//	// ������id
	private String m_operator = null;
	private UIPanel ivjPanlCmd = null;
	private UIButton ivjbtnReLock = null;
	private UIButton ivjbtnOk = null;
	private UIButton ivjbtnCancel = null;
	
	private ToftPanel parent;

	private String cxntrayid;//���鿴����������ID
	
	private Map<String, XnRelationVO[]> dataInfor = null;//�󶨵�ʵ����������

	public void setID(String xntrayid){
		cxntrayid = xntrayid;
	}

	public ViewLockDLG(ToftPanel parent, String m_operator,
			String m_pkcorp) {
		super(parent);
		this.parent = parent;
		this.m_operator = m_operator;
		this.m_pkcorp = m_pkcorp;
		init();
	}

	private void init() {
		setName("BillSourceUI");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(600, 300);
		setTitle("�󶨹�ϵ�鿴");
		setContentPane(getUIDialogContentPane());
		addListenerEvent();
//		//���ر�ͷ����
//		try {
//			loadHeadData();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	protected JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			ivjUIDialogContentPane = new JPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(new BorderLayout());
			ivjUIDialogContentPane.add(getbillCardPanel(), BorderLayout.CENTER);
			ivjUIDialogContentPane.add(getPanlCmd(), BorderLayout.SOUTH);
		}
		return ivjUIDialogContentPane;
	}

	protected BillCardPanel getbillCardPanel() {
		if (ivjbillListPanel == null) {
			try {
				ivjbillListPanel = new BillCardPanel();
				ivjbillListPanel.setName("billListPanel");
				ivjbillListPanel.loadTemplet(WdsWlPubConst.INV_VIEW_LOCK_TEMPLET_TYPE, null,m_operator, m_pkcorp);
				//				ivjbillListPanel.getHeadTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);//��ѡ
				//				ivjbillListPanel.getBodyTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);//��ѡ
				ivjbillListPanel.getBodyPanel().setTotalRowShow(true);
				ivjbillListPanel.setEnabled(false);
			} catch (java.lang.Throwable e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return ivjbillListPanel;
	}

	private UIPanel getPanlCmd() {
		if (ivjPanlCmd == null) {
			ivjPanlCmd = new UIPanel();
			ivjPanlCmd.setName("PanlCmd");
			ivjPanlCmd.setPreferredSize(new Dimension(0, 40));
			ivjPanlCmd.setLayout(new FlowLayout());
			ivjPanlCmd.add(getbtnOk(), getbtnOk().getName());
			ivjPanlCmd.add(getbtnCancel(), getbtnCancel().getName());
			ivjPanlCmd.add(getBtnReLock(),getBtnReLock().getName());
		}
		return ivjPanlCmd;
	}

	private UIButton getBtnReLock() {
		if (ivjbtnReLock == null) {
			ivjbtnReLock = new UIButton();
			ivjbtnReLock.setName("relock");
			ivjbtnReLock.setText("����");
		}
		return ivjbtnReLock;
	}
	//
	// ���ȷ����ť
	private UIButton getbtnOk() {
		if (ivjbtnOk == null) {
			ivjbtnOk = new UIButton();
			ivjbtnOk.setName("btnOk");
			ivjbtnOk.setText("ȷ��");
		}
		return ivjbtnOk;
	}

	// ���ȡ����ť
	private UIButton getbtnCancel() {
		if (ivjbtnCancel == null) {
			ivjbtnCancel = new UIButton();
			ivjbtnCancel.setName("btnCancel");
			ivjbtnCancel.setText("ȡ��");
		}
		return ivjbtnCancel;
	}

	private Map<String,XnRelationVO[]> getDataInfor(){
		if(dataInfor == null)
			dataInfor = new HashMap<String, XnRelationVO[]>();
			return dataInfor;
	}
	private BillModel getDataPanel(){
		return getbillCardPanel().getBillModel();
	}
	public void loadHeadData() throws Exception{
		getDataPanel().clearBodyData();
		if(PuPubVO.getString_TrimZeroLenAsNull(cxntrayid)==null)
			return;
		if(!getDataInfor().containsKey(cxntrayid)){
			String where = " isnull(dr,0)=0 and cxntrayid = '"+cxntrayid+"'";
			XnRelationVO[] vos = (XnRelationVO[])HYPubBO_Client.queryByCondition(XnRelationVO.class, where);
			if(vos == null || vos.length == 0)
				return;
			getDataInfor().put(cxntrayid, vos);
		}
		XnRelationVO[]  vos = getDataInfor().get(cxntrayid);	
		getDataPanel().setBodyDataVO(vos);
		getDataPanel().execLoadFormula();
	}
//	private static String[] sort_fields = new String[]{"","",""};
	// ����
	public void addListenerEvent() {
		getbtnOk().addActionListener(this);
		getbtnCancel().addActionListener(this);
		getBtnReLock().addActionListener(this);
	}

	public UIPanel getIvjPanlCmd() {
		return ivjPanlCmd;
	}
	// �����ť��ļ����¼�
	public void actionPerformed(ActionEvent e) {
		//		// �ж��Ƿ�Ϊȡ����ť
		if (e.getSource().equals(getbtnCancel())) {
			// �رմ���
			this.closeCancel();
		}
		// �ж��Ƿ�Ϊȷ����ť
		if (e.getSource().equals(getbtnOk())) {
			this.closeOK();
		}
		else if (e.getSource().equals(getBtnReLock())) {
			try {
				onReLock();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				parent.showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e1.getMessage()));
			}
		}
	}

	private void onReLock() throws Exception{
		if(PuPubVO.getString_TrimZeroLenAsNull(cxntrayid)==null)
			throw new BusinessException("�����쳣");
		LockTrayHelper.reLockTray(parent, getDataInfor().get(cxntrayid));
//		�����ɹ���  �رնԻ���
		this.closeOK();
	}
}

package nc.ui.dm.so.deal2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.WindowConstants;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.vo.dm.so.deal2.SoDealBillVO;
import nc.vo.dm.so.deal2.SoDealVO;
import nc.vo.dm.so.deal2.StoreInvNumVO;

/**
 * 
 * @author zhf  ���ۼƻ�����  �ֹ����Ž���
 *
 */
public class HandDealDLG  extends UIDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected SoDealClientUI ui = null;
	private JPanel ivjUIDialogContentPane = null;
	private HandDealDataPanel m_dataPane = null;	
	private HandDataBuffer m_buffer = null;
	private UIButton ivjbtnOk =  null;
	private UIButton ivjbtnCancel = null;
	

	private void createHandDataBuffer(){
		m_buffer = new HandDataBuffer();
	}
	public HandDataBuffer getBuffer(){
		return m_buffer;
	}

	public void clear(){
		getBuffer().clear();
	}	

	public void setLcust(List<SoDealBillVO> lcust) {
		getBuffer().setLcust(lcust);
	}

	public void setLnum(List<StoreInvNumVO> lnum) {
		getBuffer().setLnum(lnum);
	}

	private UIPanel ivjPanlCmd = null;
	
	public HandDealDLG(String m_billType, String m_operator,
			String m_pkcorp, SoDealClientUI parent,List<SoDealVO> vos) {
		super(parent);
		this.ui = parent;
		init();
	}

	public HandDealDLG(SoDealClientUI parent) {
		super(parent);
		this.ui = parent;
		init();
	}

	private void init() {
		setName("BillSourceUI");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(1000, 500);
		setTitle("��������");
		setContentPane(getUIDialogContentPane());
		createHandDataBuffer();	
		getbtnCancel().addActionListener(this);
		getbtnOk().addActionListener(this);
	}
	protected JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			ivjUIDialogContentPane = new JPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(new BorderLayout());
			ivjUIDialogContentPane.add(getDataPanel(), BorderLayout.CENTER);
			ivjUIDialogContentPane.add(getPanlCmd(), BorderLayout.SOUTH);
		}
		return ivjUIDialogContentPane;
	}

	public HandDealDataPanel getDataPanel(){
		if(m_dataPane == null){
			m_dataPane = new HandDealDataPanel(this);
		}
		return m_dataPane;
	}

	private UIPanel getPanlCmd() {
		if (ivjPanlCmd == null) {
			ivjPanlCmd = new UIPanel();
			ivjPanlCmd.setName("PanlCmd");
			ivjPanlCmd.setPreferredSize(new Dimension(0, 40));
			ivjPanlCmd.setLayout(new FlowLayout());
			ivjPanlCmd.add(getbtnOk(), getbtnOk().getName());
			ivjPanlCmd.add(getbtnCancel(), getbtnCancel().getName());
		}
		return ivjPanlCmd;
	}

//	public UIPanel getIvjPanlCmd() {
//		return ivjPanlCmd;
//	}
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
	}	

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
}

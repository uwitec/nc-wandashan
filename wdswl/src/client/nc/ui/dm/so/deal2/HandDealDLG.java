package nc.ui.dm.so.deal2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.dm.so.deal2.SoDealBillVO;
import nc.vo.dm.so.deal2.StoreInvNumVO;

/**
 * 
 * @author zhf  销售计划安排  手工安排界面
 *
 */
public class HandDealDLG  extends UIDialog implements
ActionListener, BillEditListener,BillEditListener2{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected SoDealClientUI ui = null;

	private JPanel ivjUIDialogContentPane = null;

	private HandDealDataPanel m_dataPane = null;
	
	private HandDataBuffer m_buffer = null;
	

	private void createHandDataBuffer(){
		m_buffer = new HandDataBuffer();
	}
	public HandDataBuffer getBuffer(){
		return m_buffer;
	}
	
	
	
	public void clear(){
		getBuffer().clear();
	}	

	//	// 公司Id
	//	private String m_pkcorp = null;
	//
	//	// 操作人id
	//	private String m_operator = null;
	//
	//	// 单据类型
	//	private String m_billType = null;

	public void setLcust(List<SoDealBillVO> lcust) {
		getBuffer().setLcust(lcust);
	}

	public void setLnum(List<StoreInvNumVO> lnum) {
		getBuffer().setLnum(lnum);
	}

	private UIPanel ivjPanlCmd = null;

	@SuppressWarnings("deprecation")
	public HandDealDLG(String m_billType, String m_operator,
			String m_pkcorp, SoDealClientUI parent,List<SoDealVO> vos) {
		super(parent);
		//		this.m_billType = m_billType;
		//		this.m_operator = m_operator;
		//		this.m_pkcorp = m_pkcorp;
		this.ui = parent;
		//		this.list = vos;
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
		setTitle("发货安排");
		setContentPane(getUIDialogContentPane());
		createHandDataBuffer();
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
		}
		return ivjPanlCmd;
	}

	public UIPanel getIvjPanlCmd() {
		return ivjPanlCmd;
	}
	// 点击按钮后的监听事件
	public void actionPerformed(ActionEvent e) {
		//		// 判断是否为取消按钮
		//		if (e.getSource().equals(getbtnCancel())) {
		//			// 关闭窗体
		//			this.closeCancel();
		//		}
		//		// 判断是否为确定按钮
		//		if (e.getSource().equals(getbtnOk())) {
		//			try{
		//				saveCurrentData(getHeadCurrentRow());
		//				chekcNumBody();
		//			}catch(Exception e1){
		//				MessageDialog.showErrorDlg(this, "警告", e1.getMessage());
		//				return;
		//			}
		//			this.closeOK();
		//		}
		//		else if (e.getSource().equals(getAddLine())) {
		//			onLineAdd();
		//		}else if (e.getSource().equals(getDeline())) {
		//			onLineDel();
		//		}
	}	
	// 编辑后事件
	public void afterEdit(BillEditEvent e) {

	}

	public void bodyRowChange(BillEditEvent e) {
	}

	public boolean beforeEdit(BillEditEvent e) {
		return true;
	}

}

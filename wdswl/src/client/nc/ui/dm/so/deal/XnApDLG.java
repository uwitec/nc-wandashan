package nc.ui.dm.so.deal;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillListPanel;
import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.dm.so.order.XnapVo;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public class XnApDLG  extends UIDialog implements
ActionListener, BillEditListener,BillEditListener2{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected SoDealClientUI myClientUI = null;

	private JPanel ivjUIDialogContentPane = null;

	protected BillListPanel ivjbillListPanel = null;

	// 公司Id
	private String m_pkcorp = null;

	// 操作人id
	private String m_operator = null;

	// 单据类型
	private String m_billType = null;

	private UIPanel ivjPanlCmd = null;
	
	private List<SoDealVO> list = null;
	
	
	

	@SuppressWarnings("deprecation")
	public XnApDLG(String m_billType, String m_operator,
			String m_pkcorp, SoDealClientUI parent,List<SoDealVO> vos) {
		super(parent);
		this.m_billType = m_billType;
		this.m_operator = m_operator;
		this.m_pkcorp = m_pkcorp;
		this.myClientUI = parent;
		this.list = vos;
		init();
	}

	private void init() {
		setName("BillSourceUI");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(1000, 500);
		setTitle("模拟安排");
		setContentPane(getUIDialogContentPane());
		setEdit();
//		addListenerEvent();
		//加载表头数据
		loadHeadData();
	}
	protected JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			ivjUIDialogContentPane = new JPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(new BorderLayout());
			ivjUIDialogContentPane.add(getbillListPanel(), BorderLayout.CENTER);
			ivjUIDialogContentPane.add(getPanlCmd(), BorderLayout.SOUTH);
		}
		return ivjUIDialogContentPane;
	}
	
	public void setEdit(){
		getbillListPanel().setEnabled(true);

	}
	
	protected BillListPanel getbillListPanel() {
		if (ivjbillListPanel == null) {
			try {
				ivjbillListPanel = new BillListPanel();
				ivjbillListPanel.setName("billListPanel");
				ivjbillListPanel.loadTemplet(m_billType, null,m_operator, m_pkcorp);
				ivjbillListPanel.getHeadTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);//单选
				ivjbillListPanel.getBodyTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);//单选
				ivjbillListPanel.getChildListPanel().setTotalRowShow(true);
				ivjbillListPanel.setEnabled(true);
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
//			ivjPanlCmd.add(getAddLine(), getAddLine().getName());
//			ivjPanlCmd.add(getDeline(), getDeline().getName());
//			ivjPanlCmd.add(getbtnOk(), getbtnOk().getName());
//			ivjPanlCmd.add(getbtnCancel(), getbtnCancel().getName());
		}
		return ivjPanlCmd;
	}
	
//	private UIButton getAddLine() {
//		if (btn_addline == null) {
//			btn_addline = new UIButton();
//			btn_addline.setName("addline");
//			btn_addline.setText("增行");
//		}
//		return btn_addline;
//	}
//	private UIButton getDeline() {
//		if (btn_deline == null) {
//			btn_deline = new UIButton();
//			btn_deline.setName("deline");
//			btn_deline.setText("删行");
//		}
//		return btn_deline;
//	}
//
//	// 添加确定按钮
//	private UIButton getbtnOk() {
//		if (ivjbtnOk == null) {
//			ivjbtnOk = new UIButton();
//			ivjbtnOk.setName("btnOk");
//			ivjbtnOk.setText(NCLangRes.getInstance().getStrByID("common",
//					"UC001-0000044")/* @res "确定" */);
//		}
//		return ivjbtnOk;
//	}
//
//	// 添加取消按钮
//	private UIButton getbtnCancel() {
//		if (ivjbtnCancel == null) {
//
//			ivjbtnCancel = new UIButton();
//			ivjbtnCancel.setName("btnCancel");
//			ivjbtnCancel.setText(NCLangRes.getInstance().getStrByID("common",
//					"UC001-0000008")/* @res "取消" */);
//		}
//		return ivjbtnCancel;
//	}

	public void loadHeadData() {
		try{
			if(list!=null){
				Map<String, XnapVo> map = new HashMap<String, XnapVo>();
				for(int i=0;i<list.size();i++){
					SoDealVO  vo= list.get(i);
					String pk_outwhouse = vo.getCbodywarehouseid()==null?"":vo.getCbodywarehouseid();
					String ccustomerid = vo.getCcustomerid()==null?"":vo.getCcustomerid();
					String cinventoryid = vo.getCinventoryid()==null?"":vo.getCinventoryid();
					String key = pk_outwhouse+ccustomerid+cinventoryid;
					if(map.containsKey(key)){
						XnapVo xnvo = map.get(key);
						UFDouble oldNum = PuPubVO.getUFDouble_NullAsZero(xnvo.getNnum());
						UFDouble oldAssNum = PuPubVO.getUFDouble_NullAsZero(xnvo.getNassnum());
						UFDouble newNum = PuPubVO.getUFDouble_NullAsZero(vo.getNnum());
						UFDouble newAssNum = PuPubVO.getUFDouble_NullAsZero(vo.getNassnum());
						oldNum = oldNum.add(newNum);
						oldAssNum = oldNum.add(newAssNum);
						xnvo.setNnum(oldNum);
						xnvo.setNassnum(oldAssNum);
						map.put(key, xnvo);
					}else{
						XnapVo xnvo = new XnapVo();
						xnvo.setPk_outwhouse(pk_outwhouse);
						xnvo.setPk_inwhouse(null);
						xnvo.setPk_cumandoc(ccustomerid);
						xnvo.setPk_invmandoc(cinventoryid);
						xnvo.setPk_invbasdoc(vo.getCinvbasdocid());
						xnvo.setNnum(vo.getNnum());
						xnvo.setNassnum(vo.getNassnum());
						xnvo.setCunitid(vo.getCunitid());
						xnvo.setCassunitid(vo.getCpackunitid());
						map.put(key, xnvo);
						
					}
				}
				Collection<XnapVo> list =map.values();
				getbillListPanel().setHeaderValueVO(list.toArray(new XnapVo[0]));
				getbillListPanel().getHeadBillModel().execLoadFormula();
				getbillListPanel().getHeadTable().setColumnSelectionInterval(0, 1);
			}
		}catch(Exception e){
			Logger.error(e);
		}
	}
	// 监听
	public void addListenerEvent() {
//		getbtnOk().addActionListener(this);
//		getbtnCancel().addActionListener(this);
//		getAddLine().addActionListener(this);
//		getDeline().addActionListener(this);
		getbillListPanel().addHeadEditListener(this);
		getbillListPanel().addEditListener(this);
		getbillListPanel().addBodyEditListener(this); // 表体编辑后事件监听
		getbillListPanel().getBodyScrollPane("xnap").addEditListener2(this);//表头编辑前监听
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

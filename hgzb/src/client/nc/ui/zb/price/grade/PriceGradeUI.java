package nc.ui.zb.price.grade;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.vo.scm.pu.PuPubVO;
/**
 * 
 * @author zhf  ���۷�ά��
 *
 */
public class PriceGradeUI extends ToftPanel {
	private UISplitPane m_splitPane = null;
	private TreeUI m_tree = null;//��������
	private DataUI m_dataPane = null;
	PriceGradeDataBuffer m_buffer = null;
	
	private ButtonObject m_btnOK = new ButtonObject("ȷ��","ȷ��",2,"ȷ��");
	private ButtonObject m_btnCol = new ButtonObject("����","����",2,"����");
	private ButtonObject m_btnCancel = new ButtonObject("ȡ��","ȡ��",2,"ȡ��");
	private ButtonObject m_btnAdjust = new ButtonObject("����","����",2,"����");
	private ButtonObject m_btnFresh = new ButtonObject("ˢ��","ˢ��",2,"ˢ��");
	
	private PriceGradeEventHandler m_handler = null;
	
	public PriceGradeUI(){
		super();
		initialize();
	}
	
	public UISplitPane getSplitPane() {
		if (m_splitPane == null)
			m_splitPane = new UISplitPane(1);
		return m_splitPane;
	}

	public TreeUI getTreeUI(){
		if(m_tree == null){
			m_tree = new TreeUI(this);
		}
		return m_tree;
	}
	public DataUI getDataUI(){
		if(m_dataPane == null){
			m_dataPane = new DataUI(this,ClientEnvironment.getInstance().getUser().getPrimaryKey()
					,ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
		}
		return m_dataPane;
	}
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void onButtonClicked(ButtonObject bo) {
		if(bo == m_btnOK){
			getHandler().onOk();
		}else  if(bo == m_btnCol){
			getHandler().onCol();
		}else if(bo == m_btnCancel){
			getHandler().onCancel();
		}else if(bo == m_btnFresh){
			onFreesh();
		}else if(bo == m_btnAdjust){
			getHandler().onAdjust();
		}
		setButtonState();
	}
	
	private void initialize() {
		createDataBuffer();
		this.add(getSplitPane());
		getSplitPane().setLeftComponent(getTreeUI());
		getSplitPane().setRightComponent(getDataUI());

		getDataUI().setPreferredSize(
				new java.awt.Dimension(298, 469));

		createEventhandler();
		getDataUI().addEditListener(getHandler());
		getDataUI().getBodyScrollPane("zb_bidding_b").addEditListener(new boydAfterEditor());
		setButtons(new ButtonObject[]{m_btnCol,m_btnAdjust,m_btnOK,m_btnCancel,m_btnFresh});
		setButtonState();
	}
	class boydAfterEditor implements BillEditListener{

		public void afterEdit(BillEditEvent e) {
			// TODO Auto-generated method stub
			getHandler().bodyAfterEdit(e);
		}

		public void bodyRowChange(BillEditEvent e) {
			// TODO Auto-generated method stub
			getHandler().itemRowChange(e);
		}
		
	}
	private void createEventhandler(){
		m_handler = new PriceGradeEventHandler(this);
	}
	public PriceGradeEventHandler getHandler(){
		return m_handler;
	}
	private void createDataBuffer(){
		m_buffer = new PriceGradeDataBuffer(this);
	}
	public PriceGradeDataBuffer getDataBuffer(){
		return m_buffer;
	}

	public void onTreeSelectSetButtonState(TableTreeNode selectnode){
		setButtonState();
	}
	
	public void setButtonState(){
		if(getDataBuffer().getVendorSelRow()<0){
			m_btnAdjust.setEnabled(false);
			m_btnCancel.setEnabled(false);
			m_btnCol.setEnabled(false);
			m_btnFresh.setEnabled(true);
			m_btnOK.setEnabled(false);
		}else{
			if(getDataBuffer().isAdjust()){
				m_btnAdjust.setEnabled(false);
				m_btnCancel.setEnabled(true);
				m_btnCol.setEnabled(false);
				m_btnFresh.setEnabled(false);
				m_btnOK.setEnabled(true);
			}else{
				m_btnAdjust.setEnabled(true);
				m_btnCancel.setEnabled(false);
				m_btnCol.setEnabled(true);
				m_btnFresh.setEnabled(true);
				m_btnOK.setEnabled(false);
			}
		}
		updateButtons();	
	}
	
	private void onFreesh(){
//		���¹�����  ˢ������
		
		getDataUI().clearDataOnTreeSel();
		getDataBuffer().clear();
		
		getTreeUI().initData();
	}
	
	public void freeshData(){
//		���¹�����  ˢ������
		
		getDataUI().clearDataOnTreeSel();
		getDataBuffer().clearOnCol();
		
//		getTreeUI().initData();
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�����ڵ�ѡ���л�
	 * 2011-5-5����09:53:02
	 */
	public void onTreeSelect(VOTreeNode node){
		if(node==null)
			return;
		String cbiddingid = PuPubVO.getString_TrimZeroLenAsNull(node.getNodeID());
		getDataBuffer().setKey(cbiddingid);
		getDataBuffer().setSelNode(node);
		
		getDataUI().clearDataOnTreeSel();
		getDataBuffer().clearOnTreeSel();
		getDataUI().setDataToUI();
	}
}

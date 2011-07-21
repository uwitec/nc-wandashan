package nc.ui.zb.bill.deal;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.bidding.BiddingHeaderVO;

public class BillDealUI extends ToftPanel {
	private UISplitPane m_splitPane = null;
	private TreeUI m_tree = null;//左侧标书树
	private DataUI m_dataPane = null;
	BillDataBuffer m_buffer = null;
	
	private ButtonObject m_btnOK = new ButtonObject("确定","确定",2,"确定");
	private ButtonObject m_btnAll = new ButtonObject("全选","全选",2,"全选");
	private ButtonObject m_btnNo = new ButtonObject("全消","全消",2,"全消");
	private ButtonObject m_btnFresh = new ButtonObject("刷新","刷新",2,"刷新");
	
	private BillDealEventHandler m_handler = null;
	
	public BillDealUI(){
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
			m_dataPane = new DataUI(this);
		}
		return m_dataPane;
	}
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onButtonClicked(ButtonObject bo) {
		if(bo == m_btnOK){
			getHandler().onOk();
		}else  if(bo == m_btnAll){
			getHandler().onAllSel();
		}else if(bo == m_btnNo){
			getHandler().onNoSel();
		}else if(bo == m_btnFresh){
			onFreesh();
		}
	}
	
	private void initialize() {
		createDataBuffer();
		this.add(getSplitPane());
		getSplitPane().setLeftComponent(getTreeUI());
		getSplitPane().setRightComponent(getDataUI());

		getDataUI().setPreferredSize(
				new java.awt.Dimension(298, 469));

		createEventhandler();
		getDataUI().getVendorPane().addEditListener(getHandler());
		setButtons(new ButtonObject[]{m_btnAll,m_btnNo,m_btnOK,m_btnFresh});
	}
	private void createEventhandler(){
		m_handler = new BillDealEventHandler(this);
	}
	public BillDealEventHandler getHandler(){
		return m_handler;
	}
	private void createDataBuffer(){
		m_buffer = new BillDataBuffer(this);
	}
	public BillDataBuffer getDataBuffer(){
		return m_buffer;
	}

	public void onTreeSelectSetButtonState(TableTreeNode selectnode){
		
	}
	
	private void onFreesh(){
//		重新构造树  刷新数据
		
		getDataUI().clearDataOnTreeSel();
		getDataBuffer().clear();
		
		getTreeUI().initData();
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）树节点选中切换
	 * 2011-5-5上午09:53:02
	 */
	public void onTreeSelect(VOTreeNode node){
		if(node==null)
			return;
		BiddingHeaderVO biddVo = (BiddingHeaderVO)node.getData();
		String cbiddingid = PuPubVO.getString_TrimZeroLenAsNull(node.getNodeID());
		UFBoolean isinv = PuPubVO.getUFBoolean_NullAs(biddVo.getFisinvcl(),UFBoolean.FALSE);
		getDataBuffer().setKey(cbiddingid);
		getDataBuffer().setSelNode(node);
		getDataBuffer().setIsinv(isinv.booleanValue()?UFBoolean.FALSE:UFBoolean.TRUE);
		
		getDataUI().clearDataOnTreeSel();
		getDataBuffer().clearOnTreeSel();
		getDataUI().setDataToUI();
	}
}

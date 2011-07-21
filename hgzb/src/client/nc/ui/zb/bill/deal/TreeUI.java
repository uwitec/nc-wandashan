package nc.ui.zb.bill.deal;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITree;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.TreeCreateTool;
import nc.ui.trade.pub.VOTreeNode;
import nc.vo.zb.bidding.BiddingHeaderVO;

public class TreeUI extends UIScrollPane implements TreeSelectionListener{
	private UITree m_tree = null;
	private BillDealUI ui = null;
	
	public TreeUI(BillDealUI ui){
		super();
		this.ui = ui;
		init();
	}
	
	private void init(){
		setAutoscrolls(true);
		setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setMinimumSize(new java.awt.Dimension(3, 3));
		setPreferredSize(new java.awt.Dimension(200, 469));
		setViewportView(getBillTree());
		
		initData();
	}
	
	public void initData(){
		BiddingHeaderVO[] datas = null;
		try {
			datas = BillDealHelper.initData(ui.getDataBuffer().getCorp());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			datas = null;
		}
		createBillTree(datas);
	}
	
	protected UITree getBillTree() {
		if (m_tree == null) {
			m_tree = new UITree();
			m_tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			m_tree.addTreeSelectionListener(this);
		}
		return m_tree;
	}
	
	private TreeCreateTool m_treedata = null;
	public TreeCreateTool getBillTreeData() {

		if (m_treedata == null)
			m_treedata = new TreeCreateTool();

		return m_treedata;
	}
	public void createBillTree(BiddingHeaderVO[] dataVO) {
		getBillTree().setModel(getBillTreeData().createTree(dataVO, "cbiddingid", null, "vbillno,cname"));
		getBillTreeData().modifyRootNodeShowName("招标标书");
	}
	
	public void deleteNodeFromTree() {
		TableTreeNode node = ui.getDataBuffer().getSelNode();
		boolean b = getBillTreeData().deleteNodeFromTree(node);
		if(!b){
			ui.showErrorMessage("删除树节点出错，请刷新数据");
			return;
		}
	}

	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub
		TableTreeNode selectnode = (TableTreeNode) e.getPath().getLastPathComponent();


		if (selectnode.isRoot()){
			ui.getDataUI().clearDataOnTreeSel();
			ui.getDataBuffer().clearOnTreeSelRoot();			
		}
		if (selectnode instanceof VOTreeNode) {

			VOTreeNode node = (VOTreeNode) selectnode;
			BiddingHeaderVO biddVo = (BiddingHeaderVO)node.getData();
			if(biddVo == null){
				ui.showHintMessage("选中标段数据为空");
				return;
			}
			

			//选中树节点变换   data  数据 发生变动  获取 该节点对应标书的数据  设置到缓存  设置到界面
			ui.onTreeSelect(node);
//			node.getNodeID()
		}

		ui.onTreeSelectSetButtonState(selectnode);

	
	}

}

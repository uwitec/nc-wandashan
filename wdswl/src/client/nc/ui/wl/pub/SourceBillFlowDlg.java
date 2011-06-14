package nc.ui.wl.pub;


import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.pf.IinitData2;
import nc.ui.trade.billgraph.BillFlowViewer;
import nc.ui.trade.billgraph.BillNodePanel;
import nc.vo.trade.billsource.LightBillVO;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 联查对话框公共类
 * 
 * @author zpm
 * 
 */
public class SourceBillFlowDlg extends nc.ui.pub.beans.UIDialog implements ActionListener, IinitData2 {
	
	private String billFinderClassname = "nc.bs.wds.finder.WdsBillFinder";
	
	/**
	 * 启动应用程序。
	 * @param args 命令行自变量数组
	 */
	public static void main(java.lang.String[] args)
	{
		//在此处插入用来启动应用程序的代码。
		try
		{
			SourceBillFlowDlg sd = new SourceBillFlowDlg("2K", "1001AA10000000000H32", null, "1001AA10000000000734", "1001");
			sd.showModal();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	String billID = null;
	String billType = null;
	String userID = null;
	;
	String pk_corp = null;
	String bizType = null;
	BillFlowViewer m_panelBillFlowView = null;
	nc.ui.pub.beans.UIMenuBar mainMenuBar = new nc.ui.pub.beans.UIMenuBar();
	nc.ui.pub.beans.UIMenu opMenu = new nc.ui.pub.beans.UIMenu(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000087")/*@res "操作"*/);
	nc.ui.pub.beans.UIMenuItem exitMenu = new nc.ui.pub.beans.UIMenuItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000088")/*@res "退出"*/);
	nc.ui.pub.beans.UIMenuItem sourceMenu = new nc.ui.pub.beans.UIMenuItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UC000-0002745")/*@res "来源"*/);
	nc.ui.pub.beans.UIMenuItem resetMenu = new nc.ui.pub.beans.UIMenuItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000089")/*@res "重新排列"*/);
	{
		mainMenuBar.add(opMenu);
		opMenu.insert(resetMenu, 0);
		opMenu.insert(sourceMenu, 1);
		opMenu.insertSeparator(2);
		opMenu.insert(exitMenu, 3);
		resetMenu.addActionListener(this);
		exitMenu.addActionListener(this);
		sourceMenu.addActionListener(this);
		//mainMenuBar.add(refreshMenu);
	}

/**
 * SoureBillDialog 构造子注解。
 */
public SourceBillFlowDlg()
{
	super(null);
}
	/**
	 * SoureBillDialog 构造子注解。
	 */
	public SourceBillFlowDlg(java.awt.Container parent, String billType, String billID, String bizType, String userID, String pk_corp)
	{
		super(parent);
		this.billID = billID;
		this.billType = billType;
		this.bizType = bizType;
		this.userID = userID;
		this.pk_corp = pk_corp;
	}
	/**
	 * SoureBillDialog 构造子注解。
	 */
	public SourceBillFlowDlg(String billType, String billID, String bizType, String userID, String pk_corp)
	{
		super(null);
		this.billID = billID;
		this.billType = billType;
		this.bizType = bizType;
		this.userID = userID;
		this.pk_corp = pk_corp;
	}
	/**
	 * 此处插入方法说明。
	 * 功能描述:
	 * 输入参数:
	 * 返回值:
	 * 异常处理:
	 * 日期:
	 */
	public void actionPerformed(java.awt.event.ActionEvent e)
	{
		if (e.getSource() == exitMenu)
		{
			this.dispose();
			this.setVisible(false);
			this.closeOK();
		}
		else if (e.getSource() == resetMenu)
		{
			m_panelBillFlowView.initBillNodes();
		}
		else if (e.getSource() == sourceMenu)
		{
			BillNodePanel node = m_panelBillFlowView.getSelectedNode();
			if (node != null && node.getModel() != null)
			{
				SourceBillFlowDlg f = new SourceBillFlowDlg(this, node.getModel().getType(), node.getModel().getID(), bizType, userID, pk_corp);
				f.showModal();
			}
		}
	}
	/**
	 * 此处插入方法说明。
	 * 功能描述:
	 * 输入参数:
	 * 返回值:
	 * 异常处理:
	 * 日期:
	 */
	public void init()
	{
		//菜单初始化
		this.setJMenuBar(mainMenuBar);
		this.setModal(true);
		java.awt.Color color = this.getContentPane().getBackground();
		java.awt.Font font = new java.awt.Font("宋体", java.awt.Font.PLAIN, 12);
		mainMenuBar.setBackground(color);
		mainMenuBar.setFont(font);
		opMenu.setBackground(color);
		opMenu.setFont(font);
		exitMenu.setBackground(color);
		exitMenu.setFont(font);
		resetMenu.setBackground(color);
		resetMenu.setFont(font);
		sourceMenu.setBackground(color);
		sourceMenu.setFont(font);
		setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000090")/*@res "单据来源图表"*/);
		setSize(700, 520);
		getContentPane().setLayout(new java.awt.BorderLayout());
		LightBillVO vo = querySourceBillVO(billID, billType);
		m_panelBillFlowView = new BillFlowViewer(vo, bizType, userID, pk_corp){
			@Override
			public void mouseClicked(MouseEvent e) {
				BillNodePanel node = (BillNodePanel)e.getSource();
				if (e.getModifiers() == MouseEvent.BUTTON1_MASK && e.getClickCount() == 2)
				{
					LightBillVO vo = node.getModel();
					try
					{
						new HYBillQry().showBillInfo(this.getParent(), vo.getType(), vo.getID());
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
				}
			}
		};
		nc.ui.pub.beans.UIScrollPane scrollPane = new nc.ui.pub.beans.UIScrollPane(m_panelBillFlowView);
		//,nc.ui.pub.beans.UIScrollPane.VERTICAL_SCROLLBAR_ALWAYS,nc.ui.pub.beans.UIScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);
		nc.ui.pub.beans.UIPanel bottomPanel = new nc.ui.pub.beans.UIPanel();
		bottomPanel.setPreferredSize(new java.awt.Dimension(1, 20));
		getContentPane().add(bottomPanel, java.awt.BorderLayout.SOUTH);
	}
	/**
	 * 此处插入方法说明。
	 * 功能描述:
	 * 输入参数:
	 * 返回值:
	 * 异常处理:
	 * 日期:
	 */
	public LightBillVO querySourceBillVO(String id, String type)
	{
		LightBillVO vo = null;
		try
		{
			//查询
			
			Class[] ParameterTypes = new Class[]{String.class,String.class,String.class};
			Object[] ParameterValues = new Object[]{getBillFinderClassname(), id, type};
			vo = (LightBillVO)LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, "nc.bs.wl.pub.WdsWlPubDMO", "queryBillGraph", ParameterTypes, ParameterValues, 2);

//			vo = nc.ui.trade.business.HYPubBO_Client.queryBillGraph(getBillFinderClassname(), id, type);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return vo;
	}


/**
 * @param string
 * 创建时间：2004-6-22 9:21:13
 */
public void setBillFinderClassname(String string)
{
	billFinderClassname = string;
}

protected String getBillFinderClassname() {
	return billFinderClassname;
}
	/**
	 * SoureBillDialog 构造子注解。
	 */
	public SourceBillFlowDlg(java.awt.Container parent)
	{
		super(parent);
	}

/**
 * 初始化dlg和panle界面
 * 创建日期：(2001-10-18 14:27:41)
 */
public void initData(java.lang.Object userObj)
{
	if (userObj instanceof String[])
	{
		String[] paraAry = (String[]) userObj;
		if (paraAry.length < 2)
			System.out.println("传入参数错误!");

		this.billType = paraAry[0];
		this.billID = paraAry[1];
		if (paraAry.length > 2)
			this.bizType = paraAry[2];
		this.userID = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		this.pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	}
}

	public int showModal() {
		init();
		return super.showModal();}
}
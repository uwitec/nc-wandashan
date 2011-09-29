package nc.ui.wl.pub;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIMenu;
import nc.ui.pub.beans.UIMenuBar;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.scm.sourcebill.BillFlowViewer;
import nc.ui.scm.sourcebill.BillNodePanel;
import nc.vo.scm.sourcebill.LightBillVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class SourceBillFlowDlg extends UIDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String billID = null;

	String billType = null;

	String userID = null;;

	String pk_corp = null;

	String bizType = null;

	BillFlowViewer m_panelBillFlowView = null;
	
	private String billFinderClassname = "nc.bs.wds.finder.WdsBillFinder";

	UIMenuBar mainMenuBar = new UIMenuBar();

	UIMenu opMenu = new UIMenu(NCLangRes.getInstance().getStrByID("scmpub",
			"UPPscmpub-000741")/* @res "操作" */);

	UIMenuItem exitMenu = new UIMenuItem(NCLangRes.getInstance().getStrByID(
			"scmcommon", "UPPSCMCommon-000272")/* @res "退出" */);

	UIMenuItem sourceMenu = new UIMenuItem(NCLangRes.getInstance().getStrByID(
			"common", "UC000-0002745")/* @res "来源" */);

	nc.ui.pub.beans.UIMenuItem resetMenu = new nc.ui.pub.beans.UIMenuItem(
			NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000742"));
	/* @res"重新排列" */
	{
		mainMenuBar.add(opMenu);
		opMenu.insert(resetMenu, 0);
		opMenu.insert(sourceMenu, 1);
		opMenu.insertSeparator(2);
		opMenu.insert(exitMenu, 3);
		resetMenu.addActionListener(this);
		exitMenu.addActionListener(this);
		sourceMenu.addActionListener(this);
		// mainMenuBar.add(refreshMenu);
	}

	/**
	 * SoureBillDialog 构造子注解。
	 */
	public SourceBillFlowDlg(Container parent, String billType, String billID,
			String bizType, String userID, String pk_corp) {
		super(parent);
		this.billID = billID;
		this.billType = billType;
		this.bizType = bizType;
		this.userID = userID;
		this.pk_corp = pk_corp;
		LightBillVO voBillInfo = new LightBillVO();
		voBillInfo.setID(billID);
		voBillInfo.setType(billType);
		voBillInfo.setBizType(bizType);
		voBillInfo.setUserID(userID);
		voBillInfo.setPk_corp(pk_corp);
		init(voBillInfo);
	}

	/**
	 * SoureBillDialog 构造子注解。from 3.1 ,改为传入LightBillVO
	 */
	public SourceBillFlowDlg(Container parent, LightBillVO voBillInfo) {
		super(parent);
		this.billID = voBillInfo.getID();
		this.billType = voBillInfo.getType();
		this.bizType = voBillInfo.getBizType();
		this.userID = voBillInfo.getUserID();
		this.pk_corp = voBillInfo.getPk_corp();
		init(voBillInfo);
	}

	/**
	 * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
	 */
	public void actionPerformed(java.awt.event.ActionEvent e) {
		if (e.getSource() == exitMenu) {
			this.dispose();
			this.setVisible(false);
			this.closeOK();
		} else if (e.getSource() == resetMenu) {
			m_panelBillFlowView.initBillNodes();
		} else if (e.getSource() == sourceMenu) {
			BillNodePanel node = m_panelBillFlowView.getSelectedNode();
			if (node != null && node.getModel() != null) {
				SourceBillFlowDlg f = new SourceBillFlowDlg(this, node
						.getModel().getType(), node.getModel().getID(),
						bizType, userID, pk_corp);
				f.showModal();
			}
		}
	}

	/**
	 * 此处插入方法说明。 功能描述:初始化界面 输入参数: 返回值: 异常处理: 日期:
	 */
	private void init(LightBillVO voBillInfo) {
		// 菜单初始化
		this.setJMenuBar(mainMenuBar);
		this.setModal(true);
		Color color = this.getContentPane().getBackground();
		Font font = new Font(NCLangRes.getInstance().getStrByID("scmpub",
				"UPPscmpub-000735")/* @res "宋体" */, Font.PLAIN, 12);
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
		setTitle(NCLangRes.getInstance().getStrByID("scmpub",
				"UPPscmpub-000743")/* @res "单据来源图表" */);
		setSize(700, 520);
		getContentPane().setLayout(new BorderLayout());
		// query source bill data
		LightBillVO voRet = querySourceBillVO(voBillInfo);
		if (voRet == null) {
			System.out.println("query bill graph ERROR.");
			return;
		}
		m_panelBillFlowView = new BillFlowViewer(voRet,
				voBillInfo.getBizType(), voBillInfo.getUserID(), voBillInfo
						.getPk_corp());
		UIScrollPane scrollPane = new UIScrollPane(m_panelBillFlowView);// ,nc.ui.pub.beans.UIScrollPane.VERTICAL_SCROLLBAR_ALWAYS,nc.ui.pub.beans.UIScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		UIPanel bottomPanel = new UIPanel();
		bottomPanel.setPreferredSize(new Dimension(1, 20));
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
	}

	/**
	 * 此处插入方法说明。
	 * 
	 * 功能描述: 查询 单据关联 图。 FROM 3.1
	 * 
	 * 输入参数:LightBillVO
	 * 
	 * 返回值: LightBillVO 结果，带回关联信息。
	 * 
	 * 异常处理: 日期:
	 */
//	private LightBillVO querySourceBillVO(LightBillVO voBillInfo) {
//		LightBillVO voRet = null;
//		try {
//      // ServcallVO[] scd = new ServcallVO[1];
//      // scd[0] = new ServcallVO();
//      // scd[0].setBeanName("nc.itf.scm.sourcebill.ISourceBill");
//      // scd[0].setMethodName("querySourceBillGraph");
//      // scd[0].setParameterTypes(new Class[] { LightBillVO.class });
//      // scd[0].setParameter(new Object[] { voBillInfo });
//      // Object[] rerObjs = LocalCallService.callService(scd);
//      // if (rerObjs != null && rerObjs[0] != null)
//      // voRet = (LightBillVO) rerObjs[0];
//      
//      voRet=SourceBillHelper.querySourceBillGraph(voBillInfo);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//    
//		return voRet;
//	}
	public LightBillVO querySourceBillVO(LightBillVO voBillInfo){
		LightBillVO vo = null;
		try
		{
			//查询
			String id = voBillInfo.getID();
			String type = voBillInfo.getType();
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
	
	public void setBillFinderClassname(String string)
	{
		billFinderClassname = string;
	}

	protected String getBillFinderClassname() {
		return billFinderClassname;
	}
}
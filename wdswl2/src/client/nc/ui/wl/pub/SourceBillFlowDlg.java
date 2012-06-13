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
/**
 * 联查对话框
 * @author mlr
 */
public class SourceBillFlowDlg extends UIDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	String billID = null;
	
	String billType = null;
	
	String userID = null;;
	
	String pk_corp = null;
	
	String bizType = null;
	
	private BillFlowViewer m_panelBillFlowView = null;	
	
	private String billFinderClassname = "nc.bs.wds.finder.LinkQueryFinder";
	
	UIMenuBar mainMenuBar = new UIMenuBar();
	
	UIMenu opMenu = new UIMenu(NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000741")/* @res "操作" */);

	UIMenuItem exitMenu = new UIMenuItem(NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000272")/* @res "退出" */);
	
	UIMenuItem sourceMenu = new UIMenuItem(NCLangRes.getInstance().getStrByID("common", "UC000-0002745")/* @res "来源" */);

	nc.ui.pub.beans.UIMenuItem resetMenu = new nc.ui.pub.beans.UIMenuItem(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000742"));
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
	}
	
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

	public SourceBillFlowDlg(Container parent, LightBillVO voBillInfo) {
		super(parent);
		this.billID = voBillInfo.getID();
		this.billType = voBillInfo.getType();
		this.bizType = voBillInfo.getBizType();
		this.userID = voBillInfo.getUserID();
		this.pk_corp = voBillInfo.getPk_corp();
		init(voBillInfo);
	}
	
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
				SourceBillFlowDlg f = new SourceBillFlowDlg(this, node.getModel().getType(), node.getModel().getID(),bizType, userID, pk_corp);				
				f.showModal();
			}
		}
	}

	/**
	 * 点击联查按钮后 进入此方法
	 */
	private void init(LightBillVO voBillInfo) {
		// 菜单初始化
		this.setJMenuBar(mainMenuBar);
		this.setModal(true);
		Color color = this.getContentPane().getBackground();
		Font font = new Font(NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000735")/* @res "宋体" */, Font.PLAIN, 12);
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
		setTitle(NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000743")/* @res "单据来源图表" */);
		setSize(700, 520);
		getContentPane().setLayout(new BorderLayout());
		LightBillVO voRet = querySourceBillVO(voBillInfo);//查询来源和下游单据信息
		if (voRet == null) {
			System.out.println("query bill graph ERROR.");
			return;
		}
		//流程图显示 panel
		m_panelBillFlowView = new BillFlowViewer(voRet,voBillInfo.getBizType(), voBillInfo.getUserID(), voBillInfo.getPk_corp());
		UIScrollPane scrollPane = new UIScrollPane(m_panelBillFlowView);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		UIPanel bottomPanel = new UIPanel();
		bottomPanel.setPreferredSize(new Dimension(1, 20));
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
	}
	/**
	 * 查询来源或者下游单据信息
	 * @作者：mlr
	 * @时间：2011-9-29下午03:44:53
	 * @param voBillInfo
	 * @return
	 */
	public LightBillVO querySourceBillVO(LightBillVO voBillInfo){
		LightBillVO vo=null;
		try
		{
			String id = voBillInfo.getID();
			String type = voBillInfo.getType();
			Class[] ParameterTypes = new Class[]{String.class,String.class,String.class};
			Object[] ParameterValues = new Object[]{getBillFinderClassname(), id, type};
			vo = (LightBillVO)LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, "nc.bs.wl.pub.LinkQueryDMO", "queryBillGraph", ParameterTypes, ParameterValues, 2);
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
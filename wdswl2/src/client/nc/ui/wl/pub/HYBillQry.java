package nc.ui.wl.pub;


import java.awt.Dimension;
import java.lang.reflect.Constructor;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.sf.IFuncRegisterQueryService;
import nc.ui.pf.pub.DapCall;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.sm.funcreg.FuncRegisterVO;

public class HYBillQry {
	private nc.ui.pub.beans.UIDialog m_uiDlg=null;
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	private UIPanel ivjTopPanel = null;
	private UIPanel ivjTitlePanel = null;
	private UILabel m_titleLbl=null;

	private String m_billType=null;
	private String m_billPk=null;
/**
 * HYBillQry 构造子注解。
 */
public HYBillQry() {
	super();
}
/**
 * 返回 UIPanel1 特性值。
 * @return nc.ui.pub.beans.UIPanel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getTitleLbl() {
	if (m_titleLbl == null) {
		try {
			m_titleLbl=new UILabel();
			m_titleLbl.setILabelType(3);
			m_titleLbl.setPreferredSize(new Dimension(200,30));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return m_titleLbl;
}
/**
 * 返回 UIPanel1 特性值。
 * @return nc.ui.pub.beans.UIPanel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIPanel getTitlePanel() {
	if (ivjTitlePanel == null) {
		try {
			ivjTitlePanel = new nc.ui.pub.beans.UIPanel();
			ivjTitlePanel.setName("BottomPanel");
			ivjTitlePanel.setPreferredSize(new Dimension(200,30));
			ivjTitlePanel.add(getTitleLbl(),"Center");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTitlePanel;
}
/**
 * 返回 TopPanel 特性值。
 * @return nc.ui.pub.beans.UIPanel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIPanel getTopPanel()
{
	if (ivjTopPanel == null)
	{
		try
		{
			ivjTopPanel = new nc.ui.pub.beans.UIPanel();
			ivjTopPanel.setName("TopPanel");
			ivjTopPanel.setPreferredSize(new java.awt.Dimension(10, 350));
			ivjTopPanel.setBorder(new javax.swing.border.EtchedBorder());
			ivjTopPanel.setLayout(new java.awt.BorderLayout());
			// user code begin {1}
			BilltypeVO resultVO = nc.ui.pf.pub.PfUIDataCache.getBillType(m_billType);
			
			
			String nodecode = resultVO.getNodecode();
			IFuncRegisterQueryService iIFuncRegisterQueryService = (IFuncRegisterQueryService) NCLocator.getInstance().lookup(
					IFuncRegisterQueryService.class.getName());
			FuncRegisterVO[] vos = iIFuncRegisterQueryService.queryFuncWhere(
							" fun_code='" + nodecode
									+ "' and isnull(dr,0)=0 ");
			
			
			String uiClassName = vos[0].getClassName();
			
			
			if (uiClassName == null || uiClassName.trim().length() ==  3)
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000091")/*@res "单据类型注册中未注册UI类！！！"*/);
			Class c = Class.forName(uiClassName);
			Class[] ArgsClass =
				new Class[] {
					String.class,
					String.class,
					String.class,
					String.class,
					String.class };
			Object[] Arguments =
				new Object[] {
					DapCall.getPkcorp(),
					m_billType,
					null,
					DapCall.getOperator(),
					m_billPk };
			Constructor ArgsConstructor = c.getConstructor(ArgsClass);
			Object retObj = (Object) ArgsConstructor.newInstance(Arguments);
			nc.ui.pub.ToftPanel tp=(nc.ui.pub.ToftPanel)retObj;
			ivjTopPanel.add(tp, "Center");
			getTitleLbl().setText(tp.getTitle());
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTopPanel;
}
/**
 * 获得界面。
 * 创建日期：(2004-4-29 17:37:24)
 * @return nc.ui.pub.beans.UIDialog
 */
private nc.ui.pub.beans.UIDialog getUIDialog(java.awt.Container parent)
{
	if (m_uiDlg == null)
	{
		try
		{
			m_uiDlg=new nc.ui.pub.beans.UIDialog(parent);
			// user code end
			m_uiDlg.setName("BillQuery");
			m_uiDlg.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			m_uiDlg.setSize(774, 532);
			m_uiDlg.setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000092")/*@res "联查单据"*/);
			m_uiDlg.setContentPane(getUIDialogContentPane());
			m_uiDlg.setResizable(false);
		}
		catch (java.lang.Throwable ivjExc)
		{
			handleException(ivjExc);
		}
	}
	return m_uiDlg;
}
/**
 * 返回 UIDialogContentPane 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JPanel getUIDialogContentPane()
{
	if (ivjUIDialogContentPane == null)
	{
		try
		{
			ivjUIDialogContentPane = new javax.swing.JPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(new java.awt.BorderLayout());
			getUIDialogContentPane().add(getTitlePanel(),"North");
			getUIDialogContentPane().add(getTopPanel(), "Center");
			// user code begin {1}
			// user code end
		}
		catch (java.lang.Throwable ivjExc)
		{
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIDialogContentPane;
}
/**
 * 每当部件抛出异常时被调用
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {
	System.out.println("--------- 异常 ---------");
	exception.printStackTrace(System.out);
}
/**
 * 显示单据信息
 * 创建日期：(2002-11-8 11:08:05)
 * @param param billType,billPk
 */
public void showBillInfo(
	java.awt.Container parent,
	String billType,
	String billPk)
	throws Exception {
	m_billType=billType;
	m_billPk=billPk;
	getUIDialog(parent).showModal();
	getUIDialog(parent).repaint();
	getUIDialog(parent).destroy();
}
}
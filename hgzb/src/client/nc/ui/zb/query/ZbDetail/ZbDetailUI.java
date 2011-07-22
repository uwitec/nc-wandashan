package nc.ui.zb.query.ZbDetail;

import java.awt.CardLayout;

import javax.swing.ListSelectionModel;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.scm.pub.print.ScmPrintTool;
import nc.ui.trade.pub.CardPanelPRTS;
import nc.ui.zb.pub.LongTimeTask;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;
import nc.vo.zb.query.ZbDetail.ZbDetailVO;

/**
 * 中标金额报表
 * @author zhw
 * 
 * 鹤岗项目  2011-07-08
 */


public class ZbDetailUI extends ToftPanel {

	//	按钮  查询   打印

	private ButtonObject m_btnQry = new ButtonObject("查询","查询",2,"查询");
	private ButtonObject m_btnPrint = new ButtonObject("导出","导出",2,"导出");
	private ButtonObject m_btnPrint1 = new ButtonObject("打印","打印",2,"打印");

	//	查询条件 定义： 标书  供应商  存货 
    private ZbDetailQueryDlg m_qryDlg = null;

	//	数据模板
	private BillCardPanel m_dataPanel1 = null;



	public ZbDetailUI(){
		super();
		init();
	}
	
	public BillCardPanel getBillCardPanel1(){
		if(m_dataPanel1 == null){
			m_dataPanel1 = new BillCardPanel();
			m_dataPanel1.loadTemplet(ZbDetailVO.templet_data_ID1);
			m_dataPanel1.setTatolRowShow(true);
			m_dataPanel1.setEnabled(false);
			m_dataPanel1.getBillTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		}
		return m_dataPanel1;
	}

	private CardLayout getLayOut(){
		return (CardLayout)getLayout();
	}
	private void init(){
		setLayout(new CardLayout());
		add(ZbPubConst.ZB_TYPE_ZBDETAIL,getBillCardPanel1());
		setButtons(new ButtonObject[]{m_btnQry,m_btnPrint,m_btnPrint1});
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onButtonClicked(ButtonObject bo) {
		// TODO Auto-generated method stub
		if(bo == m_btnQry){
			doQry();
		}else if(bo == m_btnPrint)
			onPrint();
		else if(bo == m_btnPrint1)
			onPrint1();

	}

	private ZbDetailQueryDlg getQryDlg(){
		if(m_qryDlg == null){
			m_qryDlg = new ZbDetailQueryDlg();
			m_qryDlg.setTempletID(ZbDetailVO.templet_query_ID);
			m_qryDlg.hideUnitButton();
			m_qryDlg.hideNormal();
		}
		return m_qryDlg;
	}

	private void doQry(){
		if(getQryDlg().showModal()!= UIDialog.ID_OK)
			return;
		String sql = getQryDlg().getWhereSQL();

		getLayOut().show(this,ZbPubConst.ZB_TYPE_ZBDETAIL);
			
		updateUI();
		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{sql};
		Object o = null;
		try {
			o = LongTimeTask.
			calllongTimeService("pu", this, "正在获取数据...", 1, "nc.bs.zb.query.pub.ZbQueryPubBO", null, "queryDatas1", ParameterTypes, ParameterValues);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
		ZbDetailVO[] datas = (ZbDetailVO[])o;
		getBillCardPanel1().getBillModel().clearBodyData();
		if(datas == null || datas.length == 0)
			return;
	
		getBillCardPanel1().getBillModel().setBodyDataVO(datas);
		getBillCardPanel1().getBillModel().execLoadFormula();
	}

	private void onPrint() {
		
    nc.ui.pub.print.IDataSource dataSource = new CardPanelPRTS(ZbDetailVO.templet_modoulecode,getBillCardPanel1());
		
		nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,dataSource);
		//String 
		
		print.setTemplateID(ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),
				ZbDetailVO.templet_modoulecode,
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),null,ZbPubConst.ZB_TYPE_ZBDETAIL);
		
		
		if (print.selectTemplate() == 1)
			try {
				ScmPrintTool.exportExcelByPrintTemplet(print,dataSource);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public void onPrint1(){
		  nc.ui.pub.print.IDataSource dataSource = new CardPanelPRTS(ZbDetailVO.templet_modoulecode,getBillCardPanel1());
			
			nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,dataSource);
			//String 
			
			print.setTemplateID(ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),
					ZbDetailVO.templet_modoulecode,
					ClientEnvironment.getInstance().getUser().getPrimaryKey(),null,ZbPubConst.ZB_TYPE_ZBDETAIL);
			
		if (print.selectTemplate() == 1)
			print.preview();
	}
}

package nc.ui.hg.ia.report;

import java.awt.CardLayout;

import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.scm.pub.print.ScmPrintTool;
import nc.ui.trade.pub.BillDirectPrint;
import nc.ui.trade.pub.CardPanelPRTS;
import nc.ui.trade.pub.ReportTreeTableModelAdapter;
import nc.vo.hg.ia.report.IaInvInOutReportVO;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.query.ConditionVO;

/**
 * 
 * @author zhf
 * 存货核算收发存汇总明细表
 * 鹤岗项目  2011-06-18
 */


public class IAInOutSumClientUI extends ToftPanel {

	//	按钮  查询   打印

	private ButtonObject m_btnQry = new ButtonObject("查询","查询",2,"查询");
	private ButtonObject m_btnView = new ButtonObject("导出","导出",2,"导出");
	private ButtonObject m_btnPrint = new ButtonObject("打印","打印",2,"打印");

	//	查询条件 定义：  会计月      存货分类（大类）   存货   库存组织    仓库    批次号 
    private InOutSumQueryDlg m_qryDlg = null;

	//	数据模板
	private BillCardPanel m_dataPanel = null;
	private BillCardPanel m_dataPanel2 = null;
	
	private boolean flag= true;


	public IAInOutSumClientUI(){
		super();
		init();
		getBillCardPanel().setTatolRowShow(true);//
		getBillCardPanel2().setTatolRowShow(true);//
	}

	public BillCardPanel getBillCardPanel(){
		if(m_dataPanel == null){
			m_dataPanel = new BillCardPanel();
			m_dataPanel.loadTemplet(IaInvInOutReportVO.templet_data_ID);
			m_dataPanel.setTatolRowShow(true);
			m_dataPanel.setEnabled(false);
			m_dataPanel.getBillTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			setCardPanelColumn();
		}
		return m_dataPanel;
	}
	public BillCardPanel getBillCardPanel2(){
		if(m_dataPanel2 == null){
			m_dataPanel2 = new BillCardPanel();
			m_dataPanel2.loadTemplet(IaInvInOutReportVO.templet_data_ID2);
			m_dataPanel2.setTatolRowShow(true);
			m_dataPanel2.setEnabled(false);
			m_dataPanel2.getBillTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			setCardPanelColumn2();
		}
		return m_dataPanel2;
	}

	private String invpanel = "inv";
	private String invclpanel = "invcl";
	private CardLayout getLayOut(){
		return (CardLayout)getLayout();
	}
	private void init(){
		setLayout(new CardLayout());
		add(invpanel,getBillCardPanel());
		add(invclpanel,getBillCardPanel2());
		setButtons(new ButtonObject[]{m_btnQry,m_btnView,m_btnPrint});
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
		}else if( bo == m_btnView){
			onPrintView();
		}else if(bo == m_btnPrint)
			onPrint();

	}

	private InOutSumQueryDlg getQryDlg(){
		if(m_qryDlg == null){
			m_qryDlg = new InOutSumQueryDlg();
			m_qryDlg.setTempletID(IaInvInOutReportVO.templet_query_ID);
			m_qryDlg.hideUnitButton();
//			m_qryDlg.hideNormal();
		}
		return m_qryDlg;
	}

	private void doQry(){
		if(getQryDlg().showModal()!= UIDialog.ID_OK)
			return;
		ConditionVO[] cons = getQryDlg().getConditionVO();

		UFBoolean isinvcl = getQryDlg().isinvcl();
		if(isinvcl.booleanValue()){
			getLayOut().show(this, invclpanel);
			flag=false;
		}
		else{
			getLayOut().show(this, invpanel);
			flag= true;
		}
			
		updateUI();
		Class[] ParameterTypes = new Class[]{ConditionVO[].class,UFBoolean.class};
		Object[] ParameterValues = new Object[]{cons,isinvcl};
		Object o = null;
		try {
			o = nc.ui.hg.pu.pub.LongTimeTask.
			calllongTimeService("pu", this, "正在获取数据...", 1, "nc.bs.ia.hg.report.InOutSubBO", null, "queryInOutData", ParameterTypes, ParameterValues);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showErrorMessage(HgPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
		IaInvInOutReportVO[] datas = (IaInvInOutReportVO[])o;
		getBillCardPanel().getBillModel().clearBodyData();
		if(datas == null || datas.length == 0)
			return;
		if(isinvcl.booleanValue()){
			getBillCardPanel2().getBillModel().setBodyDataVO(datas);
			getBillCardPanel2().getBillModel().execLoadFormula();
		}else{
		getBillCardPanel().getBillModel().setBodyDataVO(datas);
		getBillCardPanel().getBillModel().execLoadFormula();
		}
	}
	private void onPrintView(){
		BillCardPanel billCardPanel= null;
		String nodeKey=null;
		if(flag){
			billCardPanel =getBillCardPanel();
			nodeKey="detail";
		}
		else{
			billCardPanel=getBillCardPanel2();
			nodeKey="class";
		}
			
		nc.ui.pub.print.IDataSource dataSource = new CardPanelPRTS(IaInvInOutReportVO.templet_modoulecode,billCardPanel);
		
		nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,dataSource);
		//String 
		
		print.setTemplateID(ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),
				IaInvInOutReportVO.templet_modoulecode,
				ClientEnvironment.getInstance().getUser().getPrimaryKey(), 
				null,nodeKey);
		
		
		if (print.selectTemplate() == 1)
			try {
				ScmPrintTool.exportExcelByPrintTemplet(print,dataSource);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private void onPrint() {
		BillCardPanel billCardPanel= null;
		String nodeKey=null;
		if(flag){
			billCardPanel =getBillCardPanel();
			nodeKey="detail";
		}
		else{
			billCardPanel=getBillCardPanel2();
			nodeKey="class";
		}
			
		nc.ui.pub.print.IDataSource dataSource = new CardPanelPRTS(IaInvInOutReportVO.templet_modoulecode,billCardPanel);
		
		nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,dataSource);
		//String 
		
		print.setTemplateID(ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),
				IaInvInOutReportVO.templet_modoulecode,
				ClientEnvironment.getInstance().getUser().getPrimaryKey(), 
				null,nodeKey);
		
		
		if (print.selectTemplate() == 1)
			  print.preview();
	}
	private void setCardPanelColumn() {
		UITable cardTable = getBillCardPanel().getBillTable();
		GroupableTableHeader cardHeader = (GroupableTableHeader) cardTable
				.getTableHeader();
		TableColumnModel cardTcm = cardTable.getColumnModel();
		ColumnGroup[] card = new ColumnGroup[4];// 二级，12个月和最后的合计
		
		card[0] = new ColumnGroup("期初");
		for(int i=3;i<5;i++){
			card[0].add(cardTcm.getColumn(i));
		}
		cardHeader.addColumnGroup(card[0]);
		
		card[1] = new ColumnGroup("本期收入");
		for(int i=6;i<14;i++){
			card[1].add(cardTcm.getColumn(i));
		}
		cardHeader.addColumnGroup(card[1]);
		
		card[2] = new ColumnGroup("本期发出");
		for(int i=16;i<22;i++){
			card[2].add(cardTcm.getColumn(i));
		}
		cardHeader.addColumnGroup(card[2]);
		
		card[3] = new ColumnGroup("期末");
		for(int i=24;i<26;i++){
			card[3].add(cardTcm.getColumn(i));
		}
		cardHeader.addColumnGroup(card[3]);
		
		getBillCardPanel().getBillModel().updateValue();
	}
	
	private void setCardPanelColumn2() {
		UITable cardTable = getBillCardPanel2().getBillTable();
		GroupableTableHeader cardHeader = (GroupableTableHeader) cardTable
				.getTableHeader();
		TableColumnModel cardTcm = cardTable.getColumnModel();
		ColumnGroup[] card = new ColumnGroup[2];// 二级，
		
		card[0] = new ColumnGroup("本期收入");
		for(int i=3;i<7;i++){
			card[0].add(cardTcm.getColumn(i));
		}
		cardHeader.addColumnGroup(card[0]);
		
		card[1] = new ColumnGroup("本期发出");
		for(int i=8;i<11;i++){
			card[1].add(cardTcm.getColumn(i));
		}
		cardHeader.addColumnGroup(card[1]);
		
		getBillCardPanel2().getBillModel().updateValue();
	}
}

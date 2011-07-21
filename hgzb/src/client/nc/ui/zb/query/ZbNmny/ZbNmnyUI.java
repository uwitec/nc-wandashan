package nc.ui.zb.query.ZbNmny;

import java.awt.CardLayout;

import javax.swing.ListSelectionModel;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.scm.pub.print.ScmPrintTool;
import nc.ui.trade.pub.CardPanelPRTS;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;
import nc.vo.zb.query.ZbNmny.ZbNmnyVO;
import nc.vo.zb.query.ZbSitua.ZbSituaVO;

/**
 * 中标金额报表
 * @author zhw
 * 
 * 鹤岗项目  2011-07-08
 */


public class ZbNmnyUI extends ToftPanel {

	//	按钮  查询   打印

	private ButtonObject m_btnQry = new ButtonObject("查询","查询",2,"查询");
	private ButtonObject m_btnPrint = new ButtonObject("打印","打印",2,"打印");
	private ButtonObject m_btnPrint1 = new ButtonObject("导出","导出",2,"导出");

	//	查询条件 定义：  部门  供应商  存货 
    private ZbNmnyQueryDlg m_qryDlg = null;

	//	数据模板
	private BillCardPanel m_dataPanel1 = null;
	private BillCardPanel m_dataPanel2 = null;
	private BillCardPanel m_dataPanel3 = null;


	public ZbNmnyUI(){
		super();
		init();
	}

	private String getZbType(){
		if(getQryDlg().m_one.isSelected()){
			return ZbPubConst.ZB_TYPE_VENDOR;
		}else if(getQryDlg().m_two.isSelected()){
			return ZbPubConst.ZB_TYPE_CORP;
		}else if(getQryDlg().m_three.isSelected()){
			return ZbPubConst.ZB_TYPE_DETAIL;
		}
		else
			return null;
	}
	public BillCardPanel getBillCardPanel1(){
		if(m_dataPanel1 == null){
			m_dataPanel1 = new BillCardPanel();
			m_dataPanel1.loadTemplet(ZbNmnyVO.templet_data_ID1);
			m_dataPanel1.setTatolRowShow(true);
			m_dataPanel1.setEnabled(false);
			m_dataPanel1.getBillTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		}
		return m_dataPanel1;
	}
	public BillCardPanel getBillCardPanel2(){
		if(m_dataPanel2 == null){
			m_dataPanel2 = new BillCardPanel();
			m_dataPanel2.loadTemplet(ZbNmnyVO.templet_data_ID2);
			m_dataPanel2.setTatolRowShow(true);
			m_dataPanel2.setEnabled(false);
			m_dataPanel2.getBillTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			
		}
		return m_dataPanel2;
	}
	
	public BillCardPanel getBillCardPanel3(){
		if(m_dataPanel3 == null){
			m_dataPanel3 = new BillCardPanel();
			m_dataPanel3.loadTemplet(ZbNmnyVO.templet_data_ID3);
			m_dataPanel3.setTatolRowShow(true);
			m_dataPanel3.setEnabled(false);
			m_dataPanel3.getBillTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			
		}
		return m_dataPanel3;
	}

	private CardLayout getLayOut(){
		return (CardLayout)getLayout();
	}
	private void init(){
		setLayout(new CardLayout());
		add(ZbPubConst.ZB_TYPE_DETAIL,getBillCardPanel3());
		add(ZbPubConst.ZB_TYPE_VENDOR,getBillCardPanel1());
		add(ZbPubConst.ZB_TYPE_CORP,getBillCardPanel2());
		setButtons(new ButtonObject[]{m_btnQry,m_btnPrint1,m_btnPrint});
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
		}else if(bo == m_btnPrint1)
			onPrint();
		else if(bo == m_btnPrint)
			onPrint1();

	}

	private ZbNmnyQueryDlg getQryDlg(){
		if(m_qryDlg == null){
			m_qryDlg = new ZbNmnyQueryDlg();
			m_qryDlg.setTempletID(ZbNmnyVO.templet_query_ID);
			m_qryDlg.hideUnitButton();
//			m_qryDlg.hideNormal();
		}
		return m_qryDlg;
	}

	private void doQry(){
		if(getQryDlg().showModal()!= UIDialog.ID_OK)
			return;
		String sql = getQryDlg().getWhereSQL();

		getLayOut().show(this,getZbType());
			
		updateUI();
		Class[] ParameterTypes = new Class[]{String.class,String.class};
		Object[] ParameterValues = new Object[]{sql,getZbType()};
		Object o = null;
		try {
			o = nc.ui.hg.pu.pub.LongTimeTask.
			calllongTimeService("pu", this, "正在获取数据...", 1, "nc.bs.zb.query.pub.ZbQueryPubBO", null, "queryDatas", ParameterTypes, ParameterValues);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
		ZbNmnyVO[] datas = (ZbNmnyVO[])o;
		getBillCardPanel().getBillModel().clearBodyData();
		if(datas == null || datas.length == 0)
			return;
	
		getBillCardPanel().getBillModel().setBodyDataVO(datas);
		int len = datas.length;
		for(int i=0;i<len;i++){
			if(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(i, "ccustbasid"))==null){
				String[] formulas = new String[]{
						"gysname->getColvalue(bd_cubasdochg,custname,ccubasdochgid,ccustmanid)",
						"gyscode->getColvalue(bd_cubasdochg,vbillno,ccubasdochgid,ccustmanid)"
						};
				getBillCardPanel().getBillModel().execFormulas(i, formulas);
			}else{
				String[] formulas = new String[]{
						"gysname->getColvalue(bd_cubasdoc,custname,pk_cubasdoc,ccustbasid)",
						"gyscode->getColvalue(bd_cubasdoc,custcode,pk_cubasdoc,ccustbasid)"
						};
				getBillCardPanel().getBillModel().execFormulas(i, formulas);
			}
		}
		getBillCardPanel().getBillModel().execLoadFormula();
	}

	private void onPrint() {
		
    nc.ui.pub.print.IDataSource dataSource = new CardPanelPRTS(ZbNmnyVO.templet_modoulecode,getBillCardPanel());
		
		nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,dataSource);
		//String 
		
		print.setTemplateID(ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),
				ZbNmnyVO.templet_modoulecode,
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),null,getZbType());
		
		
		if (print.selectTemplate() == 1)
			try {
				ScmPrintTool.exportExcelByPrintTemplet(print,dataSource);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public BillCardPanel getBillCardPanel(){
		BillCardPanel billCardPanel= null;
		if(ZbPubConst.ZB_TYPE_VENDOR.endsWith(getZbType())){
			 billCardPanel= getBillCardPanel1();
		}else if(ZbPubConst.ZB_TYPE_CORP.endsWith(getZbType())){
			 billCardPanel= getBillCardPanel2();
		}else if(ZbPubConst.ZB_TYPE_DETAIL.endsWith(getZbType())){
			 billCardPanel= getBillCardPanel3();
		}
		return billCardPanel;
	}
	
	public void onPrint1(){
		 nc.ui.pub.print.IDataSource dataSource = new CardPanelPRTS(ZbNmnyVO.templet_modoulecode,getBillCardPanel());
			
			nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,dataSource);
			//String 
			
			print.setTemplateID(ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),
					ZbNmnyVO.templet_modoulecode,
					ClientEnvironment.getInstance().getUser().getPrimaryKey(),null,getZbType());
			
			
		if (print.selectTemplate() == 1)
			print.preview();
	}
}

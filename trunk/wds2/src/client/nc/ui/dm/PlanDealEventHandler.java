package nc.ui.dm;

import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillModel;
import nc.vo.dm.PlanDealVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class PlanDealEventHandler implements BillEditListener{


	private PlanDealClientUI ui = null;

	private PlanDealQryDlg m_qrypanel = null;
	//数据缓存
	private PlanDealVO[] m_billdatas = null;
	private List<PlanDealVO> lseldata = new ArrayList<PlanDealVO>();

//	private Map<String, UFDateTime> tsInfor = new HashMap<String, UFDateTime>();

//	private MonthNumAdjustDlg m_monNumDlg = null;

//	private PlanDealVO[] m_combinDatas = null;

//	private String planType= null;

	public PlanDealEventHandler(PlanDealClientUI parent){
		super();
		ui = parent;
		
		
	}

	private BillModel getDataPane(){
		return ui.getPanel().getHeadBillModel();
	}

	public void onButtonClicked(String btnTag){
		if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL)){
			onDeal();
		}else if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO)){
			onNoSel();
		}else if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL)){
			onAllSel();
		}else if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY)){
			onQuery();
		}
	}
	
	public void onNoSel(){
//		int rowcount = getDataPanelSel().getRowCount();
//		if(rowcount <= 0)
//			return;
//		for(int i=0;i<rowcount;i++){
//			getDataPanelSel().setValueAt(UFBoolean.FALSE, i, "bsel");
//		}
//		clearCache();
	}

	public void onAllSel(){
//		if(getDataBuffer() == null||getDataBuffer().length == 0)
//			return;
//		PlanDealVO[] datas = getDataBuffer();
//		clearCache();
//		for(PlanDealVO data:datas){
//			lseldata.add(data);
//			tsInfor.put(data.getPk_plan_b(),data.getTs());
//		}
//		
//		int rowcount = getDataPanelSel().getRowCount();
//		for(int i=0;i<rowcount;i++){
//			getDataPanelSel().setValueAt(UFBoolean.TRUE, i, "bsel");
//		}
//	}
//	public void onNoSel(){
//		int rowcount = getDataPanelSel().getRowCount();
//		if(rowcount <= 0)
//			return;
//		for(int i=0;i<rowcount;i++){
//			getDataPanelSel().setValueAt(UFBoolean.FALSE, i, "bsel");
//		}
//		clearCache();
	}
	
	private PlanDealQryDlg getQryDlg(){
		if(m_qrypanel == null){
			m_qrypanel = new PlanDealQryDlg();
			
			m_qrypanel.hideUnitButton();
			//			m_qrypanel.setConditionEditable("h.pk_corp",true);
			//			m_qrypanel.setValueRef("h.pk_corp", new UIRefPane("公司目录"));
			//			m_qrypanel.changeValueRef("h.pk_corp", new UIRefPane("公司目录"));
		}
		return m_qrypanel;
	}
	
	private void clearData(){
		m_billdatas = null;
		lseldata.clear();
		getDataPane().clearBodyData();
	}
	public void onQuery(){
		getQryDlg().showModal();
		if(!getQryDlg().isCloseOK())
			return;
		String whereSql = getQryDlg().getWhereSQL();
		
		
		PlanDealVO[] billdatas = null; 
		try{
			billdatas = PlanDealHealper.doQuery(whereSql);
		}catch(Exception e){
			e.printStackTrace();
			showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}		
		
		if(billdatas == null||billdatas.length == 0){
            clearData();
			showHintMessage("查询完成：没有满足条件的数据");
			return;
		}
		
		//处理查询出的计划  缓存  界面
		getDataPane().setBodyDataVO(billdatas);
		getDataPane().execLoadFormula();
		setDataBuffer(billdatas);
		
		showHintMessage("查询完成");
	}
	
	private void setDataBuffer(PlanDealVO[] billdatas){
		this.m_billdatas = billdatas;
	}
	private void onDeal(){

	}

	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private void showErrorMessage(String msg){
		ui.showErrorMessage(msg);
	}
	private void showWarnMessage(String msg){
		ui.showWarningMessage(msg);
	}
	private void showHintMessage(String msg){
		ui.showHintMessage(msg);
	}

}

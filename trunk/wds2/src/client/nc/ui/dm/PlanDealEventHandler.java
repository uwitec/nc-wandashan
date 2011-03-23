package nc.ui.dm;

import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.dm.PlanDealVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class PlanDealEventHandler implements BillEditListener,IBillRelaSortListener2{


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
		getDataPane().addSortRelaObjectListener2(this);
		
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
	
	private PlanDealVO[] getDataBuffer(){
		return m_billdatas;
	}
	
	public void onNoSel(){
		int rowcount = getDataPane().getRowCount();
		if(rowcount <= 0)
			return;
		for(int i=0;i<rowcount;i++){
			getDataPane().setValueAt(UFBoolean.FALSE, i, "bsel");
		}
		clearCache();
	}
	
	private void clearCache(){
		lseldata.clear();
//		tsInfor.clear();
	}

	public void onAllSel(){
		if(getDataBuffer() == null||getDataBuffer().length == 0)
			return;
		PlanDealVO[] datas = getDataBuffer();
		clearCache();
		for(PlanDealVO data:datas){
			lseldata.add(data);
//			tsInfor.put(data.getPk_plan_b(),data.getTs());
		}
		
		int rowcount = getDataPane().getRowCount();
		for(int i=0;i<rowcount;i++){
			getDataPane().setValueAt(UFBoolean.TRUE, i, "bsel");
		}
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
		
		
		//查询计划进行处理
		
		/**
		 * 满足什么条件的计划呢？人员和仓库已经绑定了   登陆人只能查询他的权限仓库  总仓的人可以安排分仓的
		 * 校验登录人是否为总仓库德人 如果是可以安排任何仓库的  转分仓  计划 
		 * 如果是分仓的人 只能 安排  本分仓内部的  发运计划
		 * 
		 */
		
		
		getQryDlg().showModal();
		if(!getQryDlg().isCloseOK())
			return;
		String whereSql = getQryDlg().getWhereSQL();
		
		//追加默认条件
		String cwhid = null;
		try{
			cwhid = LoginInforHelper.getLogInfor(ui.m_ce.getUser().getPrimaryKey()).getWhid();
		}catch(Exception ee){
			ee.printStackTrace();
			showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(ee.getMessage()));
			return;
		}
		
		if(!WdsWlPubTool.isZc(cwhid)){//非总仓人员登陆  只能查询 发货仓库为自身的发运计划
			whereSql = whereSql+" and pk_outwhouse = '"+cwhid+"' ";
		}
		
		
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
		int row = e.getRow();
		if(row < 0)
			return;
		if(e.getKey().equalsIgnoreCase("bsel")){
			UFBoolean bsel = PuPubVO.getUFBoolean_NullAs(getDataPane().getValueAt(row, "bsel"), UFBoolean.FALSE);
			if(bsel.booleanValue()){
			     lseldata.add(getDataBuffer()[row]);
//			     tsInfor.put(getDataBuffer()[row].getPk_plan_b(), getDataBuffer()[row].getTs());
			}else{
				lseldata.remove(getDataBuffer()[row]);
//				tsInfor.remove(getDataBuffer()[row].getPk_plan_b());
			}   
		}
//		else if(e.getKey().equalsIgnoreCase("dreqdate")){//zhf  到货日期编辑后
//			UFDate udate = (UFDate)getDataPanelSel().getValueAt(row, "dreqdate");
//			getDataBuffer()[row].setCsupplydate(udate);
//		}
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
	
	public Object[] getRelaSortObjectArray() {
		// TODO Auto-generated method stub
		return getDataBuffer();
	}

}

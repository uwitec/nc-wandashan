package nc.ui.hg.pu.plan.deal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillModel;
import nc.vo.hg.pu.plan.deal.PlanDealVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scm.pu.PuPubVO;

public class DealPlanEventHandler implements BillEditListener,BillEditListener2{

	private DealPlanUI ui = null;
	
	private DealPlanQueryDlg m_qrypanel = null;
	
	private List<PlanDealVO> lseldata = new ArrayList<PlanDealVO>();
	
	private Map<String, UFDateTime> tsInfor = new HashMap<String, UFDateTime>();
	
	private MonthNumAdjustDlg m_monNumDlg = null;
	
	private PlanDealVO[] m_combinDatas = null;
	
	private String planType= null;
	
	private String getPlanType(){
		if(getQryDlg().m_rbyear.isSelected()){
			planType= HgPubConst.PLAN_YEAR_BILLTYPE;
			return HgPubConst.PLAN_YEAR_BILLTYPE;
		}else if(getQryDlg().m_rbtemp.isSelected()){
			planType = HgPubConst.PLAN_TEMP_BILLTYPE;
			return HgPubConst.PLAN_TEMP_BILLTYPE;
		}else if(getQryDlg().m_rbmny.isSelected()){
			planType = HgPubConst.PLAN_MNY_BILLTYPE;
			return HgPubConst.PLAN_MNY_BILLTYPE;
		}
		else
			return null;
	}
	
	private DealPlanQueryDlg getQryDlg(){
		if(m_qrypanel == null){
			m_qrypanel = new DealPlanQueryDlg();
			if(ui.cl.getCorp().equals(ui.m_appInfor.getM_pocorp()))
				m_qrypanel.setTempletID(ui.cl.getCorp(), HgPubConst.PLAN_DEAL_FUNCODE, ui.cl.getUser(), null);
			else
				m_qrypanel.setTempletID(HgPubConst.PLAN_DEAL_FUNCODE2);
			m_qrypanel.hideUnitButton();
			//			m_qrypanel.setConditionEditable("h.pk_corp",true);
			//			m_qrypanel.setValueRef("h.pk_corp", new UIRefPane("公司目录"));
			//			m_qrypanel.changeValueRef("h.pk_corp", new UIRefPane("公司目录"));
		}
		return m_qrypanel;
	}
	public DealPlanEventHandler(DealPlanUI ui){
		super();
		this.ui = ui;
	}	
	
	private void clearCache(){
		lseldata.clear();
		tsInfor.clear();
	}
	
	public void onAllSel(){
		if(getDataBuffer() == null||getDataBuffer().length == 0)
			return;
		PlanDealVO[] datas = getDataBuffer();
		clearCache();
		for(PlanDealVO data:datas){
			lseldata.add(data);
			tsInfor.put(data.getPk_plan_b(),data.getTs());
		}
		
		int rowcount = getDataPanelSel().getRowCount();
		for(int i=0;i<rowcount;i++){
			getDataPanelSel().setValueAt(UFBoolean.TRUE, i, "bsel");
			// modify by zhw 2011-02-24  设置到货日期的可编辑性
			if(!HgPubConst.PLAN_YEAR_BILLTYPE.equalsIgnoreCase(getPlanType()))
				 getDataPanelSel().setCellEditable(i,"dreqdate",true);
		}
	}
	public void onNoSel(){
		int rowcount = getDataPanelSel().getRowCount();
		if(rowcount <= 0)
			return;
		for(int i=0;i<rowcount;i++){
			getDataPanelSel().setValueAt(UFBoolean.FALSE, i, "bsel");
			if(!HgPubConst.PLAN_YEAR_BILLTYPE.equalsIgnoreCase(getPlanType()))
				 getDataPanelSel().setCellEditable(i,"dreqdate",false);
		}
		clearCache();
	}
	
	public void onQuery(){

		//查询    查询条件是什么    查询下级单位的    计划   审批通过    区分   年计划  临时计划   专项资金计划
//		默认查询年计划   需求部门  指定查询下级具体的部门   申请人    申请物资分类   申请日期
		getQryDlg().showModal();
		if(!getQryDlg().isCloseOK())
			return;
		String whereSql = getQryDlg().getWhereSQL();
		if(PuPubVO.getString_TrimZeroLenAsNull(whereSql)==null){
			whereSql = " h.pk_corp <> '"+ui.m_appInfor.getM_pocorp()+"'";
		}else
		whereSql = whereSql + " and h.pk_corp <> '"+ui.m_appInfor.getM_pocorp()+"'";;
		
		PlanDealVO[] billdatas = null; 
		try{
			billdatas = DealPlanHealper.queryPlans(whereSql, ui.cl,getPlanType());
		}catch(Exception e){
			e.printStackTrace();
			showErrorMessage(HgPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}		
		//lyf begin 设置 回写到货日期 按钮的可编辑性
		if( planType.equalsIgnoreCase(HgPubConst.PLAN_TEMP_BILLTYPE)|| planType.equalsIgnoreCase(HgPubConst.PLAN_MNY_BILLTYPE)){
			ui.setBtnEditable(10, true);
		}else{
			ui.setBtnEditable(10, false);
		}
		//lyf end
		
		if(billdatas == null||billdatas.length == 0){
			ui.clearData();
			showHintMessage("查询完成：没有满足条件的数据");
			return;
		}
		
		for(PlanDealVO billdata:billdatas){
			if(billdata.getDreqdate()==null || "".equals(billdata.getDreqdate())){
				billdata.setDreqdate(billdata.getCsupplydate());//到货日期默认为需用日期
			}
			if(!billdata.getPk_billtype().equalsIgnoreCase(HgPubConst.PLAN_YEAR_BILLTYPE)){
				billdata.setCyear(String.valueOf(billdata.getDreqdate().getYear()));
				billdata.setCmonth(HgPubTool.getMonth(billdata.getDreqdate().getMonth()));
			}
		}
		
		ui.clearData();
		clearCache();
		//处理查询出的计划  缓存  界面
		getDataPanelSel().setBodyDataVO(billdatas);
		getDataPanelSel().execLoadFormula();
		setDataBuffer(billdatas);
		showHintMessage("查询完成");
	}
	
	private void beforCommit(PlanDealVO[] datas) throws ValidationException{
		if(datas == null||datas.length==0)
			return;
		int index = 0;
		for(PlanDealVO data:datas){
			data.setLsourceid(m_combinDatas[index].getLsourceid());
			data.validata();
			index++;
		}		
	}
	
	public void onCommit(){
//		从界面取数据     未做缓存
		//是否支持  界面数据  选择性操作     应该支持吧    全部查询出来了，但不一定这次就要全部提交过去支持用户的勾选
		ui.getBillNoSelPanel().stopEditing();
		PlanDealVO[] datas = (PlanDealVO[])getDataPanelNoSel().getBodyValueVOs(PlanDealVO.class.getName());
		if(datas == null||datas.length ==0){
			showHintMessage("没有数据");
			return;
		}
		
		try{
			beforCommit(datas);
			ui.m_appInfor.setM_useObj(tsInfor);
			DealPlanHealper.commitPlans(ui,datas, ui.m_appInfor,getPlanType(),lseldata);	
		}catch(Exception e){
			e.printStackTrace();
			showErrorMessage(HgPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
		
		//提交成功后处理    1、缓存中需要清楚选中的数据  2、 按钮切换  3、数据切换
		
		List<PlanDealVO> ldata = new ArrayList<PlanDealVO>();
		PlanDealVO[] buffers = getDataBuffer();
		for(PlanDealVO buffer:buffers){
			ldata.add(buffer);
		}
		
		ldata.removeAll(lseldata);
		lseldata.clear();
		
		if(ldata.size() == 0){
			setDataBuffer(null);				
		}else
			setDataBuffer(ldata.toArray(new PlanDealVO[0]));
		
		setNumItemEnable(false);
		ui.update();
		ui.switchUI();		
	}
	public void onBalance(){

		//从界面取数据     未做缓存
		PlanDealVO[] datas = (PlanDealVO[])getDataPanelNoSel().getBodyValueVOs(PlanDealVO.class.getName());
		if(datas == null||datas.length ==0){
			showHintMessage("没有数据");
			return;
		}
	
		try{
			datas = DealPlanHealper.balancePlans(ui, datas, ui.cl, getPlanType());
		}catch(Exception e){
			e.printStackTrace();
			showErrorMessage(HgPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
		
		getDataPanelNoSel().clearBodyData();
		getDataPanelNoSel().setBodyDataVO(datas);
		getDataPanelNoSel().execLoadFormula();
		
		setNumItemEnable(true);
	}
	public void onCombin(){
		if(lseldata.size() == 0){
			showWarnMessage("请选中要合并的计划数据");
			return;
		}		
		if(ui.m_appInfor == null){
			showErrorMessage("当前操作员未关联业务员");
			return;
		}
//		PlanDealVO[] datas = null;
		try{
			m_combinDatas = PlanCombinTool.combinPlanData(lseldata,ui.m_appInfor,true);
		}catch(Exception e){
			e.printStackTrace();
			showErrorMessage(HgPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
		
		//数据转换为新界面  
		getDataPanelNoSel().clearBodyData();
		getDataPanelNoSel().setBodyDataVO(m_combinDatas);
		getDataPanelNoSel().execLoadFormula();
//		getDataPanelNoSel().execFormulas(new String[]{"nsafestocknum->getcolvalue2(bd_produce , safetystocknum, pk_invmandoc,cinventoryid , pk_calbody, creqcalbodyid)"});
		ui.switchUI();			
	}
	private void setNumItemEnable(boolean isEnable){
		String[] names = HgPubTool.PLAN_IC_NUMS;
		for(String name:names){
//			if(name.equalsIgnoreCase("nnum")||name.equalsIgnoreCase("nretnum")||name.equalsIgnoreCase("nassistnum")
//					||name.equalsIgnoreCase("nusenum")||name.equalsIgnoreCase("nallusenum"))//毛需求净需求不能编辑
//				continue;
			if(!getPlanType().equalsIgnoreCase(HgPubConst.PLAN_YEAR_BILLTYPE)){
				if(name.equalsIgnoreCase("nreinnum")||name.equalsIgnoreCase("nreoutnum")){
					continue;
				}
			}
			getDataPanelNoSel().getItemByKey(name).setEdit(isEnable);
		}
	}
	public void onCancel(){
		getDataPanelNoSel().clearBodyData();
		m_combinDatas = null;
		getDataPanelNoSel().setBodyDataVO(null);
		lseldata.clear();
		ui.update();
		ui.switchUI();
		setNumItemEnable(false);
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）直接处理数据
	 * 2011-1-29下午04:16:20
	 */
	public void onDeal(){
		if(lseldata.size() == 0){
			showWarnMessage("请选中要处理的计划数据");
			return;
		}		
		if(ui.m_appInfor == null){
			showErrorMessage("当前操作员未关联业务员");
			return;
		}
//		PlanDealVO[] datas = null;
		try{
			m_combinDatas = PlanCombinTool.combinPlanData(lseldata,ui.m_appInfor,false);
		}catch(Exception e){
			e.printStackTrace();
			showErrorMessage(HgPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
		
		//数据转换为新界面  
		getDataPanelNoSel().clearBodyData();
		getDataPanelNoSel().setBodyDataVO(m_combinDatas);
		getDataPanelNoSel().execLoadFormula();
		ui.switchUI();	
	}
	
	public void onAdjust() {
		
		//年计划  库存平衡后 调整月份分量
		
//		判断1 是否为年计划      2月份分量和总量是否已经不相等
		if(!getPlanType().equalsIgnoreCase(HgPubConst.PLAN_YEAR_BILLTYPE)){
			showWarnMessage("非年度计划");
			return;
		}
		
		PlanDealVO[] datas = (PlanDealVO[])getDataPanelNoSel().getBodyValueVOs(PlanDealVO.class.getName());
		if(datas == null || datas.length ==0){
			showWarnMessage("数据为空");
			return;
		}
//      在次可对月份领用量的调整  但最终结果  月份总量=净需求量
		
//		需要调整       调整对话框    净需求量    月份总量    月份分量---------按什么进行匹配  顺序号
		getMonNumDlg().setDatasToUI(datas);
		if(getMonNumDlg().showModal()!=UIDialog.ID_OK)
			return;
		datas = getMonNumDlg().getDatas();
	
		getDataPanelNoSel().clearBodyData();
		getDataPanelNoSel().setBodyDataVO(datas);
		getDataPanelNoSel().execLoadFormula();
	}
	/**
	 * 回写到货日期
	 */
	public void onWriteBack(){
		ui.getBillNoSelPanel().stopEditing();
		if(lseldata.size() == 0){
			showWarnMessage("请选中计划数据");
			return;
		}		
		try{
			UFDate date = ui.cl.getLogonDate();
			for(PlanDealVO dealvo:lseldata){
				if(PuPubVO.getString_TrimZeroLenAsNull(dealvo.getDreqdate())==null){
					showErrorMessage("选中数据存在到货日期为空的计划记录");
					return;
				}
				if(dealvo.getDreqdate().compareTo(date)<0){
					showErrorMessage("到货日期不能小于今天");
					return;
				}
				// modify by zhw 2011-02-24  校验到货日期不能早于需用日期日期 回写日期时同时也回写TS  回写完成后提示
				if(dealvo.getDreqdate().compareTo(dealvo.getCsupplydate())<0)
					throw new ValidationException("到货日期不能早于需用日期日期");
			}
			DealPlanHealper.writeBack(ui,lseldata,tsInfor);	
			
			showHintMessage("回写完成");
			int rowcount = getDataPanelSel().getRowCount();
			for(int i=0;i<rowcount;i++){
				// modify by zhw 2011-02-24  设置到货日期的可编辑性
			    getDataPanelSel().setCellEditable(i, "dreqdate", false);
			}
			// zhw end  
		}catch(Exception e){
			e.printStackTrace();
			showErrorMessage(HgPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
		
	}
	
	private MonthNumAdjustDlg getMonNumDlg(){
		if(m_monNumDlg == null){
			m_monNumDlg = new MonthNumAdjustDlg(ui);
		}
		return m_monNumDlg;
	}
	public boolean beforeEdit(BillEditEvent e) {
		if(e.getKey().equalsIgnoreCase("dreqdate")){
			if( planType !=null && 
					(planType.equalsIgnoreCase(HgPubConst.PLAN_TEMP_BILLTYPE)|| planType.equalsIgnoreCase(HgPubConst.PLAN_MNY_BILLTYPE))){
				return true;
			}
		}
		return false;
	}
	
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		int row = e.getRow();
		if(row < 0)
			return;
		if(e.getKey().equalsIgnoreCase("bsel")){
			UFBoolean bsel = PuPubVO.getUFBoolean_NullAs(getDataPanelSel().getValueAt(row, "bsel"), UFBoolean.FALSE);
			if(bsel.booleanValue()){
			     lseldata.add(getDataBuffer()[row]);
			     tsInfor.put(getDataBuffer()[row].getPk_plan_b(), getDataBuffer()[row].getTs());
			  // modify by zhw 2011-02-24  设置到货日期的可编辑性
			     if(!HgPubConst.PLAN_YEAR_BILLTYPE.equalsIgnoreCase(getDataBuffer()[row].getPk_billtype()))
			        getDataPanelSel().setCellEditable(row,"dreqdate",true);
			}else{
				lseldata.remove(getDataBuffer()[row]);
				tsInfor.remove(getDataBuffer()[row].getPk_plan_b());
				if(!HgPubConst.PLAN_YEAR_BILLTYPE.equalsIgnoreCase(getDataBuffer()[row].getPk_billtype()))
				    getDataPanelSel().setCellEditable(row,"dreqdate",false);
			}   
		}else if(e.getKey().equalsIgnoreCase("dreqdate")){//zhf  到货日期编辑后
			UFDate udate = (UFDate)getDataPanelSel().getValueAt(row, "dreqdate");
			getDataBuffer()[row].setDreqdate(udate);
		}
		
	}
	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		
	}
	private BillModel getDataPanelSel(){
		return ui.getBillPanel().getBillModel();
	}
	
	private BillModel getDataPanelNoSel(){
		return ui.getBillNoSelPanel().getBillModel();
	}
	
	private PlanDealVO[] getDataBuffer(){
		return ui.getDataBuffer();
	}
	
	private void setDataBuffer(PlanDealVO[] datas){
		ui.setDataBuffer(datas);
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

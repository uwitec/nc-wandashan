package nc.ui.hg.pu.plan.balance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.hg.pu.pub.LongTimeTask;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.hg.pu.plan.balance.PlanMonDealVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scm.pu.PuPubVO;

public class BalancePlanEventHandler implements BillEditListener{

	private BalancePlanUI ui = null;
	
	private BalancePlanQueryDlg m_qrypanel = null;
	
	private List<PlanMonDealVO> lseldata = new ArrayList<PlanMonDealVO>();
	
	private Map<String, UFDateTime> tsInfor = new HashMap<String, UFDateTime>();
	
	
//	private PlanMonDealVO[] m_combinDatas = null;

	/**
	 * 选择  false未平衡  true已经平衡
	 */
	private boolean getPlanType(){
		if(getQryDlg().m_deal.isSelected()){
			return true ;
		}else if(getQryDlg().m_undeal.isSelected()){
			return false;
		}
		return false;
	}
	/**
	 * 选择  undeal未平衡  deal 已经平衡  audit审核  unaudit未审核
	 */
	private String getType(){
		if(getQryDlg().m_deal.isSelected()){
			return  "deal" ;
		}else if(getQryDlg().m_undeal.isSelected()){
			return "undeal";
		}else if(getQryDlg().m_audit.isSelected()){
			return "audit";
		}
		return "other";
	}
	private BalancePlanQueryDlg getQryDlg(){
		if(m_qrypanel == null){
			m_qrypanel = new BalancePlanQueryDlg();
			m_qrypanel.setTempletID(HgPubConst.PLAN_Balance_ID);
		}
		return m_qrypanel;
	}
	public BalancePlanEventHandler(BalancePlanUI ui){
		super();
		this.ui = ui;
	}	
	
	private void clearCache(){
		lseldata.clear();
		tsInfor.clear();
	}
	/**
	 * 全选
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-2-18下午06:06:50
	 */
	public void onAllSel(){
		if(getDataBuffer() == null||getDataBuffer().length == 0)
			return;
		PlanMonDealVO[] datas = getDataBuffer();
		clearCache();
		for(PlanMonDealVO data:datas){
			lseldata.add(data);
			tsInfor.put(data.getPk_plan_b(),data.getTs());
		}
		
		int rowcount = getDataPanelSel().getRowCount();
		for(int i=0;i<rowcount;i++){
			getDataPanelSel().setValueAt(UFBoolean.TRUE, i, "bsel");
			if(!getPlanType())
			   getDataPanelSel().getItemByKey("nreserve10").setEdit(!getPlanType());
		}
	}
	/**
	 * 取消全选
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-2-18下午06:07:10
	 */
	public void onNoSel(){
		int rowcount = getDataPanelSel().getRowCount();
		if(rowcount <= 0)
			return;
		for(int i=0;i<rowcount;i++){
			getDataPanelSel().setValueAt(UFBoolean.FALSE, i, "bsel");
			if(!getPlanType())
				   getDataPanelSel().getItemByKey("nreserve10").setEdit(getPlanType());
		}
		clearCache();
	}
	
	public void onQuery(){

        //查询   查询下级单位的计划 
		getQryDlg().showModal();
		if(!getQryDlg().isCloseOK())
			return;
		String whereSql = getQryDlg().getWhereSQL();
		
		PlanMonDealVO[] billdatas = null; 
		try{
			billdatas = BalancePlanHealper.queryPlans(whereSql, ui.cl,getType());
		}catch(Exception e){
			e.printStackTrace();
			showErrorMessage(HgPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}		
		
		if(billdatas == null||billdatas.length == 0){
			ui.clearData();
			showHintMessage("查询完成：没有满足条件的数据");
			return;
		}
		int len= billdatas.length;
		for(int i=0;i<len;i++){
			if(billdatas[i].getNreserve10()==null)
			   billdatas[i].setNreserve10(billdatas[i].getNnum());
		}
		ui.clearData();
		clearCache();
		//处理查询出的计划  缓存  界面
		getDataPanelSel().setBodyDataVO(billdatas);
		getDataPanelSel().execLoadFormula();
		setDataBuffer(billdatas);
		ui.setButtonSatus(getType());
		  
		showHintMessage("查询完成");
	}
	
	/**
	 * 平衡月计划
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-2-21下午12:30:12
	 */
	public void onBalance(){

		ui.getBillPanel().stopEditing();
		
		if(lseldata.size() == 0){
			showWarnMessage("请选中要平衡的计划数据");
			return;
		}	
		
		int size = lseldata.size();
		for(int i=0;i<size;i++){
			PlanMonDealVO vo = (PlanMonDealVO)lseldata.get(i);
			vo.setVreserve4(ui.cl.getUser());
			vo.setVreserve5(ui.cl.getLogonDate().toString());
		}
		
		
		try{
			if(checkBalance())
			BalancePlanHealper.commitBalancePlans(ui,lseldata,true);	
		}catch(Exception e){
			e.printStackTrace();
			showErrorMessage(HgPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
		
		//平衡成功后处理    1、缓存中需要清楚选中的数据  2、 按钮切换  3、数据切换
		
		List<PlanMonDealVO> ldata = new ArrayList<PlanMonDealVO>();
		PlanMonDealVO[] buffers = getDataBuffer();
		for(PlanMonDealVO buffer:buffers){
			ldata.add(buffer);
		}
		
		ldata.removeAll(lseldata);
		lseldata.clear();
		
		if(ldata.size() == 0){
			setDataBuffer(null);				
		}else
			setDataBuffer(ldata.toArray(new PlanMonDealVO[0]));
		ui.update();	
	}
	
	public void onCancel(){
		getDataPanelSel().clearBodyData();
		getDataPanelSel().setBodyDataVO(null);
		lseldata.clear();
		ui.update();
	}
	/**
	 * 取消平衡
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-2-21下午12:30:34
	 */
	public void onUnBalance(){
		if(lseldata.size() == 0){
			showWarnMessage("请选中要取消平衡的计划数据");
			return;
		}	
		
		int size = lseldata.size();
		for(int i=0;i<size;i++){
			
			PlanMonDealVO vo = (PlanMonDealVO)lseldata.get(i);
				
			vo.setVreserve4("");
			vo.setVreserve5("");
			vo.setNreserve10(null);
		}
		try{
			BalancePlanHealper.commitBalancePlans(ui,lseldata,false);	
		}catch(Exception e){
			e.printStackTrace();
			showErrorMessage(HgPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
		
		List<PlanMonDealVO> ldata = new ArrayList<PlanMonDealVO>();
		PlanMonDealVO[] buffers = getDataBuffer();
		for(PlanMonDealVO buffer:buffers){
			ldata.add(buffer);
		}
		
		ldata.removeAll(lseldata);
		lseldata.clear();
		
		if(ldata.size() == 0){
			setDataBuffer(null);				
		}else
			setDataBuffer(ldata.toArray(new PlanMonDealVO[0]));
		ui.update();
	}
	/**
	 * 审批
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-4-6下午08:15:03
	 */
	public void onAduit(){
		if(lseldata.size() == 0){
			showWarnMessage("请选中要审批的数据");
			return;
		}	
		
		int size = lseldata.size();
		for(int i=0;i<size;i++){
			
			PlanMonDealVO vo = (PlanMonDealVO)lseldata.get(i);
			vo.setVreserve2("Y");
		}
		try{
			BalancePlanHealper.aduitPlans(ui,lseldata,true);	
		}catch(Exception e){
			e.printStackTrace();
			showErrorMessage(HgPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
		
		List<PlanMonDealVO> ldata = new ArrayList<PlanMonDealVO>();
		PlanMonDealVO[] buffers = getDataBuffer();
		for(PlanMonDealVO buffer:buffers){
			ldata.add(buffer);
		}
		
		ldata.removeAll(lseldata);
		lseldata.clear();
		
		if(ldata.size() == 0){
			setDataBuffer(null);				
		}else
			setDataBuffer(ldata.toArray(new PlanMonDealVO[0]));
		ui.update();
	}
	
	/**
	 * 取消审批
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-4-6下午08:15:18
	 */
     public void onUnAduit(){
    	 if(lseldata.size() == 0){
 			showWarnMessage("请选中要审批的数据");
 			return;
 		}	
 		
 		int size = lseldata.size();
 		for(int i=0;i<size;i++){
 			
 			PlanMonDealVO vo = (PlanMonDealVO)lseldata.get(i);
 			vo.setVreserve2("N");
 		}
 		try{
 			BalancePlanHealper.aduitPlans(ui,lseldata,false);	
 		}catch(Exception e){
 			e.printStackTrace();
 			showErrorMessage(HgPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
 			return;
 		}
 		
 		List<PlanMonDealVO> ldata = new ArrayList<PlanMonDealVO>();
 		PlanMonDealVO[] buffers = getDataBuffer();
 		for(PlanMonDealVO buffer:buffers){
 			ldata.add(buffer);
 		}
 		
 		ldata.removeAll(lseldata);
 		lseldata.clear();
 		
 		if(ldata.size() == 0){
 			setDataBuffer(null);				
 		}else
 			setDataBuffer(ldata.toArray(new PlanMonDealVO[0]));
 		ui.update();
	 }
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		int row = e.getRow();
		if(e.getKey().equalsIgnoreCase("bsel")){
			UFBoolean bsel = PuPubVO.getUFBoolean_NullAs(getDataPanelSel().getValueAt(row, "bsel"), UFBoolean.FALSE);
			if(bsel.booleanValue()){
			     lseldata.add(getDataBuffer()[row]);
			     tsInfor.put(getDataBuffer()[row].getPk_plan_b(), getDataBuffer()[row].getTs());
			     if(!getPlanType())
					   getDataPanelSel().setCellEditable(row,"nreserve10",!getPlanType());
			}else{
				lseldata.remove(getDataBuffer()[row]);
				tsInfor.remove(getDataBuffer()[row].getPk_plan_b());
				if(!getPlanType())
					  getDataPanelSel().setCellEditable(row,"nreserve10",getPlanType());
			}   
		}else if(e.getKey().equalsIgnoreCase("nreserve10")){
			PlanMonDealVO vo =getDataBuffer()[row];
			vo.setNreserve10(PuPubVO.getUFDouble_NullAsZero(getDataPanelSel().getValueAt(row,"nreserve10")));
		}
		
	}
	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		
	}
	private BillModel getDataPanelSel(){
		return ui.getBillPanel().getBillModel();
	}
	
	private PlanMonDealVO[] getDataBuffer(){
		return ui.getDataBuffer();
	}
	
	
	private void setDataBuffer(PlanMonDealVO[] datas){
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
	
	private boolean checkBalance() throws Exception{
		Class[] ParameterTypes = new Class[]{List.class};
		Object[] ParameterValues = new Object[]{lseldata};
		Object o = LongTimeTask.callRemoteService("pu","nc.bs.hg.pu.plan.balance.BalancePlanBO", "checkBalance", ParameterTypes, ParameterValues, 2);
		
		if(o==null)
			return true;
		ArrayList<String> al = (ArrayList<String>)o;
		
		if(al==null || al.size()==0)
			return true;
		int size =al.size();
		StringBuffer str = new StringBuffer();
		for(int i=0;i<size;i++){
 			String err =al.get(i);
			if(err!=null){
				String [] strs =err.split("&");
				if(strs !=null && strs.length!=0){
					Object invcode =HYPubBO_Client.findColValue("bd_invbasdoc","invcode","pk_invbasdoc='"+strs[0]+"'");
					str.append("存货"+invcode+strs[1]+"。");
				}
			}	
		}
		if(str==null ||str.length()==0)
		    return true;
		if(MessageDialog.showYesNoDlg(ui,"连锁提示校验",str.toString())==MessageDialog.ID_YES)
			return true;
		return false;
	}

	/**
	 * 审批
	 * @author zhw
	 * @throws UifException 
	 * @说明：（鹤岗矿业）
	 * 2011-4-6下午08:15:03
	 */
	public void onView() {
		if(lseldata.size() == 0){
			showWarnMessage("请选中数据");
			return;
		}	
		int size = lseldata.size();
		ArrayList<String> al = new ArrayList<String>();
		for(int i=0;i<size;i++){
			PlanMonDealVO vo =lseldata.get(i);
			String key = vo.getPk_invbasdoc();
			String st=null;
			try {
			 st = PuPubVO.getString_TrimZeroLenAsNull(HYPubBO_Client.findColValue("bd_invmandoc", "pk_invmandoc", "pk_corp ='1002' and pk_invbasdoc ='" + key+ "'"));
			} catch (UifException e) {
				PuPubVO.getString_TrimZeroLenAsNull(e.getMessage());
			}
			if(st==null)
				return;
			if(!al.contains(st)){
				al.add(st);
			}
			      
		}
		 ViewDetailDlg dlg=new ViewDetailDlg(ui,al);
		   dlg.showModal();
	}
}

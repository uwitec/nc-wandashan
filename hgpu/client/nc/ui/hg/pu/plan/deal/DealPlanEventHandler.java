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
			//			m_qrypanel.setValueRef("h.pk_corp", new UIRefPane("��˾Ŀ¼"));
			//			m_qrypanel.changeValueRef("h.pk_corp", new UIRefPane("��˾Ŀ¼"));
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
			// modify by zhw 2011-02-24  ���õ������ڵĿɱ༭��
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

		//��ѯ    ��ѯ������ʲô    ��ѯ�¼���λ��    �ƻ�   ����ͨ��    ����   ��ƻ�  ��ʱ�ƻ�   ר���ʽ�ƻ�
//		Ĭ�ϲ�ѯ��ƻ�   ������  ָ����ѯ�¼�����Ĳ���   ������    �������ʷ���   ��������
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
		//lyf begin ���� ��д�������� ��ť�Ŀɱ༭��
		if( planType.equalsIgnoreCase(HgPubConst.PLAN_TEMP_BILLTYPE)|| planType.equalsIgnoreCase(HgPubConst.PLAN_MNY_BILLTYPE)){
			ui.setBtnEditable(10, true);
		}else{
			ui.setBtnEditable(10, false);
		}
		//lyf end
		
		if(billdatas == null||billdatas.length == 0){
			ui.clearData();
			showHintMessage("��ѯ��ɣ�û����������������");
			return;
		}
		
		for(PlanDealVO billdata:billdatas){
			if(billdata.getDreqdate()==null || "".equals(billdata.getDreqdate())){
				billdata.setDreqdate(billdata.getCsupplydate());//��������Ĭ��Ϊ��������
			}
			if(!billdata.getPk_billtype().equalsIgnoreCase(HgPubConst.PLAN_YEAR_BILLTYPE)){
				billdata.setCyear(String.valueOf(billdata.getDreqdate().getYear()));
				billdata.setCmonth(HgPubTool.getMonth(billdata.getDreqdate().getMonth()));
			}
		}
		
		ui.clearData();
		clearCache();
		//�����ѯ���ļƻ�  ����  ����
		getDataPanelSel().setBodyDataVO(billdatas);
		getDataPanelSel().execLoadFormula();
		setDataBuffer(billdatas);
		showHintMessage("��ѯ���");
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
//		�ӽ���ȡ����     δ������
		//�Ƿ�֧��  ��������  ѡ���Բ���     Ӧ��֧�ְ�    ȫ����ѯ�����ˣ�����һ����ξ�Ҫȫ���ύ��ȥ֧���û��Ĺ�ѡ
		ui.getBillNoSelPanel().stopEditing();
		PlanDealVO[] datas = (PlanDealVO[])getDataPanelNoSel().getBodyValueVOs(PlanDealVO.class.getName());
		if(datas == null||datas.length ==0){
			showHintMessage("û������");
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
		
		//�ύ�ɹ�����    1����������Ҫ���ѡ�е�����  2�� ��ť�л�  3�������л�
		
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

		//�ӽ���ȡ����     δ������
		PlanDealVO[] datas = (PlanDealVO[])getDataPanelNoSel().getBodyValueVOs(PlanDealVO.class.getName());
		if(datas == null||datas.length ==0){
			showHintMessage("û������");
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
			showWarnMessage("��ѡ��Ҫ�ϲ��ļƻ�����");
			return;
		}		
		if(ui.m_appInfor == null){
			showErrorMessage("��ǰ����Աδ����ҵ��Ա");
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
		
		//����ת��Ϊ�½���  
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
//					||name.equalsIgnoreCase("nusenum")||name.equalsIgnoreCase("nallusenum"))//ë���������ܱ༭
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
	 * @˵�������׸ڿ�ҵ��ֱ�Ӵ�������
	 * 2011-1-29����04:16:20
	 */
	public void onDeal(){
		if(lseldata.size() == 0){
			showWarnMessage("��ѡ��Ҫ����ļƻ�����");
			return;
		}		
		if(ui.m_appInfor == null){
			showErrorMessage("��ǰ����Աδ����ҵ��Ա");
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
		
		//����ת��Ϊ�½���  
		getDataPanelNoSel().clearBodyData();
		getDataPanelNoSel().setBodyDataVO(m_combinDatas);
		getDataPanelNoSel().execLoadFormula();
		ui.switchUI();	
	}
	
	public void onAdjust() {
		
		//��ƻ�  ���ƽ��� �����·ݷ���
		
//		�ж�1 �Ƿ�Ϊ��ƻ�      2�·ݷ����������Ƿ��Ѿ������
		if(!getPlanType().equalsIgnoreCase(HgPubConst.PLAN_YEAR_BILLTYPE)){
			showWarnMessage("����ȼƻ�");
			return;
		}
		
		PlanDealVO[] datas = (PlanDealVO[])getDataPanelNoSel().getBodyValueVOs(PlanDealVO.class.getName());
		if(datas == null || datas.length ==0){
			showWarnMessage("����Ϊ��");
			return;
		}
//      �ڴοɶ��·��������ĵ���  �����ս��  �·�����=��������
		
//		��Ҫ����       �����Ի���    ��������    �·�����    �·ݷ���---------��ʲô����ƥ��  ˳���
		getMonNumDlg().setDatasToUI(datas);
		if(getMonNumDlg().showModal()!=UIDialog.ID_OK)
			return;
		datas = getMonNumDlg().getDatas();
	
		getDataPanelNoSel().clearBodyData();
		getDataPanelNoSel().setBodyDataVO(datas);
		getDataPanelNoSel().execLoadFormula();
	}
	/**
	 * ��д��������
	 */
	public void onWriteBack(){
		ui.getBillNoSelPanel().stopEditing();
		if(lseldata.size() == 0){
			showWarnMessage("��ѡ�мƻ�����");
			return;
		}		
		try{
			UFDate date = ui.cl.getLogonDate();
			for(PlanDealVO dealvo:lseldata){
				if(PuPubVO.getString_TrimZeroLenAsNull(dealvo.getDreqdate())==null){
					showErrorMessage("ѡ�����ݴ��ڵ�������Ϊ�յļƻ���¼");
					return;
				}
				if(dealvo.getDreqdate().compareTo(date)<0){
					showErrorMessage("�������ڲ���С�ڽ���");
					return;
				}
				// modify by zhw 2011-02-24  У�鵽�����ڲ������������������� ��д����ʱͬʱҲ��дTS  ��д��ɺ���ʾ
				if(dealvo.getDreqdate().compareTo(dealvo.getCsupplydate())<0)
					throw new ValidationException("�������ڲ�������������������");
			}
			DealPlanHealper.writeBack(ui,lseldata,tsInfor);	
			
			showHintMessage("��д���");
			int rowcount = getDataPanelSel().getRowCount();
			for(int i=0;i<rowcount;i++){
				// modify by zhw 2011-02-24  ���õ������ڵĿɱ༭��
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
			  // modify by zhw 2011-02-24  ���õ������ڵĿɱ༭��
			     if(!HgPubConst.PLAN_YEAR_BILLTYPE.equalsIgnoreCase(getDataBuffer()[row].getPk_billtype()))
			        getDataPanelSel().setCellEditable(row,"dreqdate",true);
			}else{
				lseldata.remove(getDataBuffer()[row]);
				tsInfor.remove(getDataBuffer()[row].getPk_plan_b());
				if(!HgPubConst.PLAN_YEAR_BILLTYPE.equalsIgnoreCase(getDataBuffer()[row].getPk_billtype()))
				    getDataPanelSel().setCellEditable(row,"dreqdate",false);
			}   
		}else if(e.getKey().equalsIgnoreCase("dreqdate")){//zhf  �������ڱ༭��
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

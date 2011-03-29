package nc.ui.dm;

import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.dm.PlanDealVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class PlanDealEventHandler implements BillEditListener,IBillRelaSortListener2{


	private PlanDealClientUI ui = null;

	private PlanDealQryDlg m_qrypanel = null;
	//���ݻ���
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
			m_qrypanel.setTempletID(ui.cl.getCorp(), WdsWlPubConst.DM_PLAN_LURU_NODECODE, ui.cl.getUser(), null);
			m_qrypanel.hideUnitButton();
			m_qrypanel.hideNormal();
			//			m_qrypanel.setConditionEditable("h.pk_corp",true);
			//			m_qrypanel.setValueRef("h.pk_corp", new UIRefPane("��˾Ŀ¼"));
			//			m_qrypanel.changeValueRef("h.pk_corp", new UIRefPane("��˾Ŀ¼"));
		}
		return m_qrypanel;
	}
	
	private void clearData(){
		m_billdatas = null;
		lseldata.clear();
		getDataPane().clearBodyData();
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * ��ѯ������Ӧ����
	 * @ʱ�䣺2011-3-25����09:49:04
	 */
	public void onQuery(){		
		/**
		 * ����ʲô�����ļƻ��أ���Ա�Ͳֿ��Ѿ�����   ��½��ֻ�ܲ�ѯ����Ȩ�޲ֿ�  �ֵܲ��˿��԰��ŷֲֵ�
		 * У���¼���Ƿ�Ϊ�ֿܲ���� ����ǿ��԰����κβֿ��  ת�ֲ�  �ƻ� 
		 * ����Ƿֲֵ��� ֻ�� ����  ���ֲ��ڲ���  ���˼ƻ�
		 * 
		 */	
		getQryDlg().showModal();
		if(!getQryDlg().isCloseOK())
			return;
		PlanDealVO[] billdatas = null; 
		try{
			String whereSql = getSQL();
			billdatas = PlanDealHealper.doQuery(whereSql);
		}catch(Exception e){
			e.printStackTrace();
			showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}		
		
		if(billdatas == null||billdatas.length == 0){
            clearData();
			showHintMessage("��ѯ��ɣ�û����������������");
			return;
		}
		//�����ѯ���ļƻ�  ����  ����
		getDataPane().setBodyDataVO(billdatas);
		getDataPane().execLoadFormula();
		setDataBuffer(billdatas);		
		showHintMessage("��ѯ���");
		ui.updateButtonStatus(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL,true);
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * ��� �� ���˼ƻ��Ĳ�ѯ����
	 * wds_sendplanin���˼ƻ�����
	 * wds_sendplanin_b ���˼ƻ��ӱ�
	 * @ʱ�䣺2011-3-25����09:47:50
	 * @return
	 * @throws Exception
	 */
	private String getSQL() throws Exception{
		StringBuffer whereSql = new StringBuffer();
		String where = getQryDlg().getWhereSQL();
		if( where != null && !"".equals(where)){
			whereSql.append(where+" and");
		}
		whereSql.append("  nvl(wds_sendplanin.dr,0)=0");
		whereSql.append(" and nvl(wds_sendplanin_b.dr,0)=0 ");
		whereSql.append(" and wds_sendplanin.pk_corp='"+ui.cl.getCorp()+"'");
		whereSql.append(" and wds_sendplanin.vbillstatus=1");
		whereSql.append(" and (coalesce(wds_sendplanin_b.nplannum,0) -  coalesce(wds_sendplanin_b.ndealnum,0)) > 0");
		String cwhid  = LoginInforHelper.getLogInfor(ui.m_ce.getUser().getPrimaryKey()).getWhid();
		if(!WdsWlPubTool.isZc(cwhid)){//���ܲ���Ա��½  ֻ�ܲ�ѯ �����ֿ�Ϊ����ķ��˼ƻ�
			whereSql.append(" and wds_sendplanin.pk_outwhouse = '"+cwhid+"' ");
		}
		return whereSql.toString();
	}
	
	private void setDataBuffer(PlanDealVO[] billdatas){
		this.m_billdatas = billdatas;
	}
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * ���˼ƻ�  ���Ű�ť������
	 * @ʱ�䣺2011-3-25����02:59:20
	 */
	public void onDeal(){
		//����  ����ǰ   ����У��
		/**
		 * ����У��
		 * �����ֿⲻ��Ϊ��   ����ֿⲻ��Ϊ�� �����ֿⲻ����ͬ  
		 * ���˵����ΰ�������Ϊ0����  
		 * ���ΰ����������ܴ��� �ƻ�����-�ۼư�������
		 * ����������ݴ����̨   ����ת��   ����    
		 * 
		 * �ֵ����򣺼ƻ���  ����վ �ջ�վ  ���   ��������   ��������
		 * 
		 */
		WdsWlPubTool.stopEditing(getDataPane());
		if(lseldata==null||lseldata.size()==0){
			showHintMessage("��ѡ��Ҫ���������");
			return;
		}
		List<SuperVO> ldata = WdsWlPubTool.filterVOsZeroNum(lseldata,"nnum");
		if(ldata == null||ldata.size() == 0){
			showErrorMessage("ѡ������û�а���");
			return;
		}
		try{
			for(SuperVO vo:ldata){
				((PlanDealVO)vo).validataOnDeal();
			}
			PlanDealHealper.doDeal(ldata, ui);
			ui.updateButtonStatus(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL,false);	
			clearData();
		}catch(Exception e){
			e.printStackTrace();
			if(e instanceof ValidationException){
				showErrorMessage(e.getMessage());
				return;
			}
			showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
		ui.showHintMessage("�����Ѿ����...");
	}

	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		int row = e.getRow();
		String key = e.getKey();
		if(row < 0)
			return;
		if(key.equalsIgnoreCase("bsel")){
			UFBoolean bsel = PuPubVO.getUFBoolean_NullAs(getDataPane().getValueAt(row, "bsel"), UFBoolean.FALSE);
			if(bsel.booleanValue()){
			     lseldata.add(getDataBuffer()[row]);
			}else{
				lseldata.remove(getDataBuffer()[row]);
			}   
		}else if("nnum".equalsIgnoreCase(key)){
			getDataBuffer()[row].setNnum(PuPubVO.getUFDouble_NullAsZero(e.getValue()));
		}

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

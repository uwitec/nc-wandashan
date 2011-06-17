package nc.ui.dm.so.deal;

import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class SoDealEventHandler implements BillEditListener,IBillRelaSortListener2{


	private SoDealClientUI ui = null;
	private SoDealQryDlg m_qrypanel = null;
	//���ݻ���
	private SoDealVO[] m_billdatas = null;
	private List<SoDealVO> lseldata = new ArrayList<SoDealVO>();
	
	public SoDealEventHandler(SoDealClientUI parent){
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
		}else if (btnTag
				.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_XNDEAL)) {
			onXNDeal();
		}
	}
	
	private SoDealVO[] getDataBuffer(){
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
		SoDealVO[] datas = getDataBuffer();
		clearCache();
		for(SoDealVO data:datas){
			lseldata.add(data);
//			tsInfor.put(data.getPk_plan_b(),data.getTs());
		}
		
		int rowcount = getDataPane().getRowCount();
		for(int i=0;i<rowcount;i++){
			getDataPane().setValueAt(UFBoolean.TRUE, i, "bsel");
		}
	}
	
	
	private SoDealQryDlg getQryDlg(){
		if(m_qrypanel == null){
			//parent, normalPnl, pk_corp, moduleCode, operator, busiType
			m_qrypanel = new SoDealQryDlg(ui,
					null,
					ui.cl.getCorp(),
					WdsWlPubConst.DM_SO_DEAL_NODECODE,
					ui.cl.getUser(),
					null
				);
			m_qrypanel.setTempletID(ui.cl.getCorp(), WdsWlPubConst.DM_SO_DEAL_NODECODE, ui.cl.getUser(), null);
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
		clearData();
		getQryDlg().showModal();
		if(!getQryDlg().isCloseOK())
			return;
		SoDealVO[] billdatas = null; 
		
		try{
			String whereSql = getSQL();
			billdatas = SoDealHealper.doQuery(whereSql);
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
		//������ѯ���ļƻ�  ����  ����
		getDataPane().setBodyDataVO(billdatas);
		getDataPane().execLoadFormula();
		billdatas = (SoDealVO[])getDataPane().getBodyValueVOs(SoDealVO.class.getName());
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
		whereSql.append(" h.pk_corp='"+ui.cl.getCorp());
		whereSql.append("' and (coalesce(b.nnumber,0) -  coalesce(b."+WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME+",0)) > 0 ");
		String where = getQryDlg().getWhereSQL();
		if(PuPubVO.getString_TrimZeroLenAsNull(where)!=null){
			whereSql.append(" and "+where);
		}
		whereSql.append(" and h.fstatus =2 and isnull(h.dr,0)=0");//���ͨ����
		
//		whereSql.append("  nvl(h.dr,0)=0");
//		whereSql.append(" and nvl(wds_sendplanin_b.dr,0)=0 ");
	
//		whereSql.append(" and h.vbillstatus=1");
		//liuys add for wds��Ŀ   �ܲ��ܲ�����зֲּƻ�,�ֲ�ֻ�ܲ���Լ��ֲּƻ�(���¼�˲ֿ���й�)
		String cwhid  = new LoginInforHelper().getLogInfor(ui.m_ce.getUser().getPrimaryKey()).getWhid();
		if(!WdsWlPubTool.isZc(cwhid)){//���ܲ���Ա��½  ֻ�ܲ�ѯ �����ֿ�Ϊ�����ķ��˼ƻ�
			whereSql.append(" and h.cwarehouseid = '"+cwhid+"' ");
		}
		return whereSql.toString();
	}
	
	private void setDataBuffer(SoDealVO[] billdatas){
		this.m_billdatas = billdatas;
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ ���˼ƻ� ���ⰲ�Ű�ť��������
	 * @ʱ�䣺2011-3-25����02:59:20
	 */
	public void onXNDeal() {
		if (lseldata == null || lseldata.size() == 0) {
			showWarnMessage("��ѡ��Ҫ����������");
			return;
		}
		XnApDLG  tdpDlg = new XnApDLG(WdsWlPubConst.XNAP,  ui.getCl().getUser(),
				ui.getCl().getCorp(), ui, lseldata);
		if(tdpDlg.showModal()== UIDialog.ID_OK){}
		// nc.ui.pub.print.IDataSource dataSource = new DealDataSource(
		// ui.getBillListPanel(), WdsWlPubConst.DM_PLAN_DEAL_NODECODE);
		// nc.ui.pub.print.PrintEntry print = new
		// nc.ui.pub.print.PrintEntry(null,
		// dataSource);
		// print.setTemplateID(ui.getEviment().getCorporation().getPk_corp(),WdsWlPubConst.DM_PLAN_DEAL_NODECODE,ui.getEviment().getUser().getPrimaryKey(),
		// null, null);
		// if (print.selectTemplate() == 1)
		// print.preview();

	}
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * ���˼ƻ�  ���Ű�ť��������
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
			showWarnMessage("��ѡ��Ҫ����������");
			return;
		}
		List<SuperVO> ldata = WdsWlPubTool.filterVOsZeroNum(lseldata,"nnum");
		if(ldata == null||ldata.size() == 0){
			showErrorMessage("ѡ������û�а���");
			return;
		}
		//
		
		
		try{
			for(SuperVO vo:ldata){
				((SoDealVO)vo).validataOnDeal();
			}
			SoDealHealper.doDeal(ldata, ui);
			
//			ui.updateButtonStatus(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL,false);	
//			clearData();
			
			
		  //�����Ѿ����ŵĵ�ѡ�����Զ����ŵļ�¼ �����ٴΰ���
			List<SuperVO>  sdata=WdsWlPubTool.filterVOsisAutoSell(ldata);
			if(sdata==null || sdata.size()==0){
				getLeftDate(ldata);			
				clearCache();
				return;
			}
			for(SuperVO vo:sdata){
				((SoDealVO)vo).validataOnDeal();
			}			
			SoDealHealper.doDeal(sdata, ui);
			getLeftDate(sdata);			
			clearCache();
		
			
			
			
			
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
	
	private void getLeftDate(List<SuperVO> ldata){
		List<SoDealVO> leftDate = new ArrayList<SoDealVO>();
		if(m_billdatas == null || m_billdatas.length==0){
			ui.showWarningMessage("��ȡ�������ݳ����������²�ѯ");
		}
		for(SoDealVO dealVO:m_billdatas){
			if(ldata.contains(dealVO))
				continue;
			leftDate.add(dealVO);
		}
		getDataPane().setBodyDataVO(leftDate.toArray(new SoDealVO[0]));
		getDataPane().execLoadFormula();
		setDataBuffer(leftDate.toArray(new SoDealVO[0]));	
	}

	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		int row = e.getRow();
		String key = e.getKey();
		if(row < 0)
			return;
		if(key.equalsIgnoreCase("isonsell")){
			UFBoolean onsell = PuPubVO.getUFBoolean_NullAs(getDataPane().getValueAt(row, "isonsell"), UFBoolean.FALSE);
			
			getDataBuffer()[row].setIsonsell(onsell);		
		}
		if(key.equalsIgnoreCase("bsel")){
			UFBoolean bsel = PuPubVO.getUFBoolean_NullAs(getDataPane().getValueAt(row, "bsel"), UFBoolean.FALSE);
			if(bsel.booleanValue()){
			    lseldata.add(getDataBuffer()[row]);
			}else{
				lseldata.remove(getDataBuffer()[row]);
			}   
		}else if("nnum".equalsIgnoreCase(key)){
			getDataBuffer()[row].setNnum(PuPubVO.getUFDouble_NullAsZero(e.getValue()));
			getDataBuffer()[row].setNassnum(PuPubVO.getUFDouble_NullAsZero(getDataPane().getValueAt(row, "nassnum")));
		}else if("nassnum".equalsIgnoreCase(key)){
			getDataBuffer()[row].setNassnum(PuPubVO.getUFDouble_NullAsZero(e.getValue()));
			getDataBuffer()[row].setNnum(PuPubVO.getUFDouble_NullAsZero(getDataPane().getValueAt(row, "nnum")));
		}else if("warehousename".equalsIgnoreCase(key)){
			getDataBuffer()[row].setCbodywarehouseid(PuPubVO.getString_TrimZeroLenAsNull(getDataPane().getValueAt(row, "cbodywarehouseid")));
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
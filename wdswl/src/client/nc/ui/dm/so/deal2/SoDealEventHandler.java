package nc.ui.dm.so.deal2;

import java.util.List;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillStatus;
import nc.ui.wl.pub.FilterNullBody;
import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.dm.so.deal2.SoDealBillVO;
import nc.vo.dm.so.deal2.SoDealHeaderVo;
import nc.vo.dm.so.deal2.StoreInvNumVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class SoDealEventHandler{

	private SoDealClientUI ui = null;
	private SoDealQryDlg m_qrypanel = null;	
	private SoDealBillVO[] m_buffer = null;

	
	public SoDealEventHandler(SoDealClientUI parent){
		super();
		ui = parent;		
	}

	private BillModel getDataPane(){
		return getHeadDataPane();
	}
	
	private BillModel getHeadDataPane(){
		return ui.getPanel().getHeadBillModel();
	}
	
	private BillModel getBodyDataPane(){
		return ui.getPanel().getBodyBillModel();
	}

	public void onButtonClicked(String btnTag){
		if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL)){
			onDeal();
		}else if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO)){
//			onNoSel();
		}else if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL)){
//			onAllSel();
		}else if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY)){
			onQuery();
		}else if (btnTag
				.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_XNDEAL)) {
//			onXNDeal();
		}
	}
	
	public SoDealBillVO[] getDataBuffer(){
		return m_buffer;
	}
	
//	public void onNoSel(){
//		int rowcount = getDataPane().getRowCount();
//		if(rowcount <= 0)
//			return;
//		for(int i=0;i<rowcount;i++){
//			getDataPane().setValueAt(UFBoolean.FALSE, i, "bsel");
//		}
//		clearCache();
//	}
	
//	private void clearCache(){
//		lseldata.clear();
////		tsInfor.clear();
//	}

//	public void onAllSel(){
//		if(getDataBuffer() == null||getDataBuffer().length == 0)
//			return;
//		SoDealVO[] datas = getDataBuffer();
//		clearCache();
//		for(SoDealVO data:datas){
//			lseldata.add(data);
////			tsInfor.put(data.getPk_plan_b(),data.getTs());
//		}
//		
//		int rowcount = getDataPane().getRowCount();
//		for(int i=0;i<rowcount;i++){
//			getDataPane().setValueAt(UFBoolean.TRUE, i, "bsel");
//		}
//	}
	
	
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
		m_buffer = null;
//		lseldata.clear();
		getDataPane().clearBodyData();
		getBodyDataPane().clearBodyData();
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
		 * 
		 * 
		 * �ֲֿ��̰�     
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
		
//		�����ݽ��кϲ�  ���ͻ��ϲ�  ��������ȡ��С��������
		SoDealBillVO[] billvos = SoDealHealper.combinDatas(ui.getWhid(),billdatas);
		
		//�����ѯ���ļƻ�  ����  ����
		
		getDataPane().setBodyDataVO(WdsWlPubTool.getParentVOFromAggBillVo(billvos, SoDealHeaderVo.class));
		getDataPane().execLoadFormula();
		getBodyDataPane().setBodyDataVO(billvos[0].getChildrenVO());
		getBodyDataPane().execLoadFormula();
//		billdatas = (SoDealVO[])getDataPane().getBodyValueVOs(SoDealVO.class.getName());
		setDataBuffer(billvos);		
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
		whereSql.append(" and h.fstatus ='"+BillStatus.AUDIT+"' ");//���ͨ����
		
//		���˵����������ͳ����������
		whereSql.append(" and coalesce(c.bifreceiptfinish,'N') = 'N'");
		whereSql.append(" and coalesce(c.bifinventoryfinish,'N') = 'N'");
		
//		���۶�������  �ĺ�״̬ �ֶ� ��֪�Ƿ���Ӱ��    ���� Ӱ��  ����� ֧�� 
//		frowstatus                    SMALLINT(2)         ��״̬ 
				
		/**
		 * 
		 * bifreceiptfinish              CHAR(1)             �Ƿ񷢻�����
           bifinventoryfinish            CHAR(1)             �Ƿ�������     
		 * 
		 * �� ������չ�ӱ��� ���ڱ������״̬   û�н��й��� ���������Ҫ  Ӧ��չ��  ���Ϸ��������Ŀ��� 
		 * 
		 */
		
//		whereSql.append("  nvl(h.dr,0)=0");
//		whereSql.append(" and nvl(wds_sendplanin_b.dr,0)=0 ");
	
//		whereSql.append(" and h.vbillstatus=1");
		//liuys add for wds��Ŀ   �ܲ��ܲ�����зֲּƻ�,�ֲ�ֻ�ܲ���Լ��ֲּƻ�(���¼�˲ֿ���й�)
		
//		if(!WdsWlPubTool.isZc(ui.getWhid())){//���ܲ���Ա��½  ֻ�ܲ�ѯ �����ֿ�Ϊ����ķ��˼ƻ�
			
			whereSql.append(" and tbst.pk_stordoc = '"+ui.getWhid()+"' ");
			
/**
 * �����ֿܲ���   ����  �ֲֵļƻ� �������Ϊ  �� ��ѯ������  ����  �ֿ��ѡ��
 * ����Ƿֲֵ�¼  ���������ɱ༭Ĭ��Ϊ ��¼�ֿ�
 * ������ֵܲ�¼  ������Ĭ��Ϊ�ܲ�  ����ѡ��ֲ�-----------��δʵ��
 */
			
//		}
		return whereSql.toString();
	}
	
	private void setDataBuffer(SoDealBillVO[] billvos){
		this.m_buffer = billvos;
	}
//	/**
//	 * 
//	 * @���ߣ�mlr
//	 * @˵�������ɽ������Ŀ ���˼ƻ� ���ⰲ�Ű�ť������
//	 * @ʱ�䣺2011-3-25����02:59:20
//	 */
//	public void onXNDeal() {
//		if (lseldata == null || lseldata.size() == 0) {
//			showWarnMessage("��ѡ��Ҫ���������");
//			return;
//		}
//		XnApDLG  tdpDlg = new XnApDLG(WdsWlPubConst.XNAP,  ui.getCl().getUser(),
//				ui.getCl().getCorp(), ui, lseldata);
//		if(tdpDlg.showModal()== UIDialog.ID_OK){}
//		// nc.ui.pub.print.IDataSource dataSource = new DealDataSource(
//		// ui.getBillListPanel(), WdsWlPubConst.DM_PLAN_DEAL_NODECODE);
//		// nc.ui.pub.print.PrintEntry print = new
//		// nc.ui.pub.print.PrintEntry(null,
//		// dataSource);
//		// print.setTemplateID(ui.getEviment().getCorporation().getPk_corp(),WdsWlPubConst.DM_PLAN_DEAL_NODECODE,ui.getEviment().getUser().getPrimaryKey(),
//		// null, null);
//		// if (print.selectTemplate() == 1)
//		// print.preview();
//
//	}
	
//	class filterNullBody implements IFilter{
//
//	public boolean accept(Object o) {
//		// TODO Auto-generated method stub
//		if(!(o instanceof AggregatedValueObject))
//			return true;
//		AggregatedValueObject bill = (AggregatedValueObject)o;
//		if(bill == null)
//			return false;
//		if(bill.getChildrenVO() == null || bill.getChildrenVO().length == 0)
//			return false;
//		return true;
//	}
		
//	}
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
		 * �����Ƿ����ⰲ��  ������С������   ���ǿ���ִ����Ƿ�����   ֱ�Ӱ���   �ֹ����Ž���
		 * ������־
		 */
		
//		��ȡѡ�е�����
		AggregatedValueObject[] selectVos = ui.getPanel().getMultiSelectedVOs(SoDealBillVO.class.getName(), SoDealHeaderVo.class.getName(), SoDealVO.class.getName());
		AggregatedValueObject[] newVos = (AggregatedValueObject[])VOUtil.filter(selectVos, new FilterNullBody());
		if(newVos == null || newVos.length == 0){
			showWarnMessage("δѡ������");
			return;
		}
		
//		�����ݽ���һ������У��
		try {
			SoDealBillVO.checkData((SoDealBillVO[])newVos);
		} catch (ValidationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e1.getMessage()));
			return;
		}
		
		Object o = null;
		try {
		 o = SoDealHealper.doDeal((SoDealBillVO[])newVos, ui);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ui.showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
		
		if(o == null)
			return;
		Object[] os = (Object[])o;
		if(os == null || os.length == 0)
			return;
		
//		δ���ŵĿͻ���Ϣ
		List<SoDealBillVO> lcust = (List<SoDealBillVO>)os[0];		
//		��������Ϣ
		List<StoreInvNumVO> lnum = (List<StoreInvNumVO>)os[1];
		
		if(lcust==null || lcust.size()<=0)
			return;
//		�����ֹ����Ž���  ���û� �ֹ�����  ���������Ʒ
		getHandDealDlg().setLcust(lcust);
		getHandDealDlg().setLnum(lnum);
		getHandDealDlg().getDataPanel().setDataToUI();
		int retFlag = getHandDealDlg().showModal();		
		if(retFlag != UIDialog.ID_OK){
			return;
		}
		
//		�����ֹ�������Ϣ  �����˵�
		
	}
	
	private HandDealDLG m_handDlg = null;
	private HandDealDLG getHandDealDlg(){
		if(m_handDlg == null){
			m_handDlg = new HandDealDLG(ui);
		}
		return m_handDlg;
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

package nc.ui.wds.ic.allo.in.close;

import java.util.ArrayList;
import java.util.List;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillModelRowStateChangeEventListener;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.ic.allo.in.close.AlloCloseBVO;
import nc.vo.wds.ic.allo.in.close.AlloCloseBillVO;
import nc.vo.wds.ic.allo.in.close.AlloCloseHVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class AlloCloseEventHandler implements BillEditListener{


	private AlloCloseClientUI ui = null;
	private AlloCloseQryDlg m_qrypanel = null;

	//���ݻ���
	private AlloCloseBillVO[] m_billdatas = null;
	int m_headrow = -1;
	private String whereSql = null;

	UFBoolean getOrderType(){
		if(getQryDlg().m_rbclose.isSelected()){
			return UFBoolean.TRUE;
		}else{
			return UFBoolean.FALSE;
		}
	}

	public AlloCloseEventHandler(AlloCloseClientUI parent){
		super();
		ui = parent;	
	}

	private BillModel getBodyDataPane(){
		return ui.getPanel().getBodyBillModel();
	}

	private BillModel getDataPane(){
		return ui.getPanel().getHeadBillModel();
	}

	public void onButtonClicked(String btnTag){
		if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO)){
			onNoSel();
		}else if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL)){
			onAllSel();
		}else if(btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY)){
			onQuery();
		}else if(btnTag.equalsIgnoreCase("�ر�")){
			try {
				onClose();
			} catch (Exception e) {
				e.printStackTrace();
				showErrorMessage(e.getMessage());
			}
		}else if(btnTag.equalsIgnoreCase("��")){
			try {
				onOpen();
			} catch (Exception e) {
				e.printStackTrace();
				showErrorMessage(e.getMessage());
			}
		}
	}

	public AlloCloseBillVO[] getDataBuffer(){
		return m_billdatas;
	}
	/**
	 * 
	 * @���ߣ�lyf:���ݱ�ͷ���ݺţ����ر���
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-11-10����08:10:37
	 * @param key
	 * @return
	 */
	protected AlloCloseBVO[] getCurrBodys(){
		if(m_billdatas == null || m_billdatas.length == 0)
			return null;
		if(m_headrow == -1)
			return null;
		if(m_billdatas.length<m_headrow)
			return null;
		return m_billdatas[m_headrow].getBodys();
	}

	public void onNoSel(){
		int rowcount = getDataPane().getRowCount();
		if(rowcount <= 0)
			return;
		for (int i = 0; i < ui.getPanel().getParentListPanel().getTable().getRowCount(); i++) {
			ui.getPanel().getParentListPanel().getTableModel().setRowState(i, BillModel.UNSTATE);
			ui.headRowChange(i);
			BillModel model = ui.getPanel().getBodyBillModel();
			IBillModelRowStateChangeEventListener l = model.getRowStateChangeEventListener();
			model.removeRowStateChangeEventListener();
			ui.getPanel().getChildListPanel().cancelSelectAllTableRow();
			model.addRowStateChangeEventListener(l);
			ui.getPanel().updateUI();
		}
	}


	/**
	 * 
	 * @���ߣ�ȫѡ
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-11-11����11:09:55
	 */
	public void onAllSel(){
		if(getDataBuffer() == null||getDataBuffer().length == 0)
			return;
		for (int i = 0; i < ui.getPanel().getParentListPanel().getTable().getRowCount(); i++) {
			ui.getPanel().getParentListPanel().getTableModel().setRowState(i, BillModel.SELECTED);
			ui.headRowChange(i);
			BillModel model = ui.getPanel().getBodyBillModel();
			IBillModelRowStateChangeEventListener l = model.getRowStateChangeEventListener();
			model.removeRowStateChangeEventListener();
			ui.getPanel().getChildListPanel().selectAllTableRow();
			model.addRowStateChangeEventListener(l);
			ui.getPanel().updateUI();
		}

	}
	private AlloCloseQryDlg getQryDlg(){
		if(m_qrypanel == null){
			m_qrypanel = new AlloCloseQryDlg();
			m_qrypanel.setTempletID(ui.cl.getCorp(), WdsWlPubConst.allo_in_close_node, ui.cl.getUser(), null);
			m_qrypanel.hideUnitButton();
		}
		return m_qrypanel;
	}

	private void clearData(){
		m_billdatas = null;
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
		 */	
		if(PuPubVO.getString_TrimZeroLenAsNull(ui.getWhid()) == null){
			showWarnMessage("��ǰ��¼��δ�󶨲ֿ�");
			return ;
		}
		getQryDlg().showModal();
		if(!getQryDlg().isCloseOK())
			return;
		clearData();
//		AlloCloseBillVO[] billdatas = null; 
		try{
			whereSql = getSQL();
			m_billdatas = AlloCloseHealper.doQuery(whereSql,ui.getWhid(),ui.cl.getUser(),getOrderType());
		}catch(Exception e){
			e.printStackTrace();
			showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}		
		if(m_billdatas == null||m_billdatas.length == 0){
			clearData();
			showHintMessage("��ѯ��ɣ�û����������������");
			return;
		}
		setDate(m_billdatas);
	}

	private void onRefresh() throws Exception{
		AlloCloseBillVO[] billdatas = null;
		clearData();
		if(PuPubVO.getString_TrimZeroLenAsNull(whereSql)!=null){

			try{
				billdatas = AlloCloseHealper.doQuery(whereSql,ui.getWhid(),ui.cl.getUser(),getOrderType());
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
			setDate(billdatas);
		}
		showHintMessage("�������");
	}

	/**
	 * 
	 * @���ߣ�lyf:��ѯ������ý�������
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-11-10����11:57:53
	 * @param billdatas
	 */
	void setDate(AlloCloseBillVO[] billdatas ){
		if(billdatas == null || billdatas.length ==0)
			return ;

		//�����ѯ���ļƻ�  ����  ����
		getDataPane().setBodyDataVO(WdsWlPubTool.getParentVOFromAggBillVo(billdatas, AlloCloseHVO.class));
		getDataPane().execLoadFormula();
		getBodyDataPane().setBodyDataVO(billdatas[0].getChildrenVO());
		getBodyDataPane().execLoadFormula();

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
//		whereSql.append(" h.pk_corp='"+ui.cl.getCorp());
//		whereSql.append("' and (coalesce(b.nnumber,0) -  coalesce(b."+WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME+",0)) > 0 ");
		String where = getQryDlg().getWhereSQL();
		if(PuPubVO.getString_TrimZeroLenAsNull(where)!=null){
			whereSql.append(" and "+where);
		}
		whereSql.append(" and h.cregister is not null ");//���ͨ����

		return whereSql.toString();
	}

	private AggregatedValueObject[] getSelectVos(){
		AggregatedValueObject[] selectVos = ui.getPanel().getMultiSelectedVOs(AlloCloseBillVO.class.getName(), AlloCloseHVO.class.getName(), AlloCloseBVO.class.getName());
//		AggregatedValueObject[] newVos = (AggregatedValueObject[])VOUtil.filter(selectVos, new FilterNullBody());
//		ui.getPanel().
		return selectVos;
	}

	public void bodyRowChange(BillEditEvent e) {		
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

	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
	}

	private void doCloseOrOpen(UFBoolean bclose) throws Exception {
		AggregatedValueObject[] newVos = getSelectVos();
		if(newVos == null || newVos.length == 0){
			showWarnMessage("δѡ������");
			return;
		}
		AlloCloseBillVO[] orders = (AlloCloseBillVO[])newVos;
		AlloCloseHVO head = null;
		List<String> lid = new ArrayList<String>();
//		String key = null;
		for(AlloCloseBillVO order:orders){
			head = order.getHead();
			if(head == null)
				continue;
			if(PuPubVO.getString_TrimZeroLenAsNull(head.getPrimaryKey())==null)
				continue;
			if(lid.contains(head.getPrimaryKey().trim()))
				continue;
			lid.add(head.getPrimaryKey().trim());
		}

		if(lid.size()<=0)
			return;

		//		Զ�̵���
		AlloCloseHealper.doCloseOrOpen(lid.toArray(new String[0]), ui, bclose);

		//		ˢ�½�������
		onRefresh();

	}

	private void onClose()throws Exception{
		doCloseOrOpen(UFBoolean.TRUE);
	}
	private void onOpen()throws Exception{
		doCloseOrOpen(UFBoolean.FALSE);
	}
}

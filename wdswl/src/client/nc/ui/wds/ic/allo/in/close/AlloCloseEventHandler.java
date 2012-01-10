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

	//数据缓存
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
		}else if(btnTag.equalsIgnoreCase("关闭")){
			try {
				onClose();
			} catch (Exception e) {
				e.printStackTrace();
				showErrorMessage(e.getMessage());
			}
		}else if(btnTag.equalsIgnoreCase("打开")){
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
	 * @作者：lyf:根据表头单据号，加载表体
	 * @说明：完达山物流项目 
	 * @时间：2011-11-10下午08:10:37
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
	 * @作者：全选
	 * @说明：完达山物流项目 
	 * @时间：2011-11-11上午11:09:55
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
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 查询动作相应方法
	 * @时间：2011-3-25上午09:49:04
	 */
	public void onQuery(){		
		/**
		 * 满足什么条件的计划呢？人员和仓库已经绑定了   登陆人只能查询他的权限仓库  总仓的人可以安排分仓的
		 * 校验登录人是否为总仓库德人 如果是可以安排任何仓库的  转分仓  计划 
		 * 如果是分仓的人 只能 安排  本分仓内部的  发运计划
		 * 
		 */	
		if(PuPubVO.getString_TrimZeroLenAsNull(ui.getWhid()) == null){
			showWarnMessage("当前登录人未绑定仓库");
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
			showHintMessage("查询完成：没有满足条件的数据");
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
				showHintMessage("查询完成：没有满足条件的数据");
				return;
			}
			setDate(billdatas);
		}
		showHintMessage("操作完成");
	}

	/**
	 * 
	 * @作者：lyf:查询完成设置界面数据
	 * @说明：完达山物流项目 
	 * @时间：2011-11-10下午11:57:53
	 * @param billdatas
	 */
	void setDate(AlloCloseBillVO[] billdatas ){
		if(billdatas == null || billdatas.length ==0)
			return ;

		//处理查询出的计划  缓存  界面
		getDataPane().setBodyDataVO(WdsWlPubTool.getParentVOFromAggBillVo(billdatas, AlloCloseHVO.class));
		getDataPane().execLoadFormula();
		getBodyDataPane().setBodyDataVO(billdatas[0].getChildrenVO());
		getBodyDataPane().execLoadFormula();

		showHintMessage("查询完成");
		ui.updateButtonStatus(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL,true);
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 获得 对 发运计划的查询条件
	 * wds_sendplanin发运计划主表
	 * wds_sendplanin_b 发运计划子表
	 * @时间：2011-3-25上午09:47:50
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
		whereSql.append(" and h.cregister is not null ");//审核通过的

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
			showWarnMessage("未选中数据");
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

		//		远程调用
		AlloCloseHealper.doCloseOrOpen(lid.toArray(new String[0]), ui, bclose);

		//		刷新界面数据
		onRefresh();

	}

	private void onClose()throws Exception{
		doCloseOrOpen(UFBoolean.TRUE);
	}
	private void onOpen()throws Exception{
		doCloseOrOpen(UFBoolean.FALSE);
	}
}

package nc.ui.dm.so.deal2;

import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillStatus;
import nc.ui.pub.bill.IBillModelRowStateChangeEventListener;
import nc.ui.wl.pub.FilterNullBody;
import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.dm.so.deal2.SoDealBillVO;
import nc.vo.dm.so.deal2.SoDealHeaderVo;
import nc.vo.dm.so.deal2.StoreInvNumVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.voutils.IFilter;
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
		try {
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
				//			onXNDeal();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ui.showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
		}
	}
	
	public SoDealBillVO[] getDataBuffer(){
		return m_buffer;
	}
	
	public void onNoSel(){
		int rowcount = getDataPane().getRowCount();
		if(rowcount <= 0)
			return;
		//yf add
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
			//			m_qrypanel.setValueRef("h.pk_corp", new UIRefPane("公司目录"));
			//			m_qrypanel.changeValueRef("h.pk_corp", new UIRefPane("公司目录"));
		}
		return m_qrypanel;
	}
	
	private void clearData(){
		m_buffer = null;
		getDataPane().clearBodyData();
		getBodyDataPane().clearBodyData();
	}
	
	private String whereSql;//缓存上次查询条件  zhf add
	
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 查询动作相应方法
	 * @时间：2011-3-25上午09:49:04
	 */
	public void onQuery() throws Exception{		
		/**
		 * 满足什么条件的计划呢？人员和仓库已经绑定了   登陆人只能查询他的权限仓库  总仓的人可以安排分仓的
		 * 校验登录人是否为总仓库德人 如果是可以安排任何仓库的  转分仓  计划 
		 * 如果是分仓的人 只能 安排  本分仓内部的  发运计划
		 * 分仓客商绑定
		 */	
		getQryDlg().showModal();
		if(!getQryDlg().isCloseOK())
			return;

		whereSql = getSQL();
		SoDealVO[] billdatas = SoDealHealper.doQuery(whereSql);
		if(billdatas == null||billdatas.length == 0){
			clearData();
			showHintMessage("查询完成：没有满足条件的数据");
			return;
		}
		//对数据进行合并  按客户合并  订单日期取最小订单日期
		SoDealBillVO[] billvos = SoDealHealper.combinDatas(ui.getWhid(),billdatas);
		clearData();
		//处理查询出的计划  缓存  界面
		getDataPane().setBodyDataVO(WdsWlPubTool.getParentVOFromAggBillVo(billvos, SoDealHeaderVo.class));
		getDataPane().execLoadFormula();
		getBodyDataPane().setBodyDataVO(billvos[0].getChildrenVO());
		getBodyDataPane().execLoadFormula();
		//		billdatas = (SoDealVO[])getDataPane().getBodyValueVOs(SoDealVO.class.getName());
		setDataBuffer(billvos);		
		showHintMessage("查询完成");
		ui.updateButtonStatus(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL,true);
	}
	
	private void onRefresh() throws Exception{
		SoDealVO[] billdatas = null;
		clearData();
		if(PuPubVO.getString_TrimZeroLenAsNull(whereSql)!=null){

			billdatas = SoDealHealper.doQuery(whereSql);
			if(billdatas == null||billdatas.length == 0){
				return;
			}
			//对数据进行合并  按客户合并  订单日期取最小订单日期
			SoDealBillVO[] billvos = SoDealHealper.combinDatas(ui.getWhid(),billdatas);
			//处理查询出的计划  缓存  界面
			getDataPane().setBodyDataVO(WdsWlPubTool.getParentVOFromAggBillVo(billvos, SoDealHeaderVo.class));
			getDataPane().execLoadFormula();
			getBodyDataPane().setBodyDataVO(billvos[0].getChildrenVO());
			getBodyDataPane().execLoadFormula();
			//			billdatas = (SoDealVO[])getDataPane().getBodyValueVOs(SoDealVO.class.getName());
			setDataBuffer(billvos);	
			ui.updateButtonStatus(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL,true);
		}
		showHintMessage("操作完成");
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
		whereSql.append(" h.pk_corp='"+ui.cl.getCorp());
		whereSql.append("' and (coalesce(b.nnumber,0) -  coalesce(b."+WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME+",0)) > 0 ");
		String where = getQryDlg().getWhereSQL();
		if(PuPubVO.getString_TrimZeroLenAsNull(where)!=null){
			whereSql.append(" and "+where);
		}
		whereSql.append(" and h.fstatus ='"+BillStatus.AUDIT+"' ");//审核通过的
		
//		过滤掉发货结束和出库结束的行
		whereSql.append(" and coalesce(c.bifreceiptfinish,'N') = 'N'");
		whereSql.append(" and coalesce(c.bifinventoryfinish,'N') = 'N'");	
//		销售订单表体  的航状态 字段 不知是否有影响    如有 影响  请后续 支持 
//		frowstatus                    SMALLINT(2)         行状态 			
		/**
		 * 
		 * bifreceiptfinish              CHAR(1)             是否发货结束
           bifinventoryfinish            CHAR(1)             是否出库结束     
		 * 
		 * 在 销售扩展子表上 存在表体的行状态   没有进行过滤 如果后续需要  应扩展对  以上发货结束的控制 
		 * 
		 */			
	whereSql.append(" and tbst.pk_stordoc = '"+ui.getWhid()+"' ");
			
	/**
	 * 关于总仓可以   看到  分仓的计划 解决方案为  在 查询条件出  增加  仓库的选择
	 * 如果是分仓登录  该条件不可编辑默认为 登录仓库
	 * 如果是总仓登录  该条件默认为总仓  但可选择分仓-----------暂未实现
	 */
	return whereSql.toString();
	}
	
	private void setDataBuffer(SoDealBillVO[] billvos){
		this.m_buffer = billvos;
	}
//	/**
//	 * 
//	 * @作者：mlr
//	 * @说明：完达山物流项目 发运计划 虚拟安排按钮处理方法
//	 * @时间：2011-3-25下午02:59:20
//	 */
//	public void onXNDeal() {
//		if (lseldata == null || lseldata.size() == 0) {
//			showWarnMessage("请选中要处理的数据");
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
	 * @作者：zhf
	 * @说明：完达山物流项目 
	 * 发运计划  安排按钮处理方法
	 * @时间：2011-3-25下午02:59:20
	 */
	@SuppressWarnings("unchecked")
	public void onDeal() throws Exception{
		//安排  安排前   数据校验
		/**
		 * 考虑是否特殊安排  过滤最小发货量   考虑库存现存量是否满足   直接安排   手工安排界面
		 * 安排日志
		 */
		AggregatedValueObject[] selectVos = ui.getPanel().getMultiSelectedVOs(SoDealBillVO.class.getName(), SoDealHeaderVo.class.getName(), SoDealVO.class.getName());
		AggregatedValueObject[] newVos = (AggregatedValueObject[])VOUtil.filter(selectVos, new FilterNullBody());
		if(newVos == null || newVos.length == 0){
			showWarnMessage("未选中数据");
			return;
		}
		//对数据进行一层严密校验
		SoDealBillVO.checkData((SoDealBillVO[])newVos);
		// SoDealHealper.doDeal((SoDealBillVO[])newVos, ui)返回值：
		// 1.null:所有客户的发货量都未达到最小发货量
		// 2.Object[] { isauto, null, null ,reasons}:所有客户都待安排的存货中，都包含可用量不足的存货
		// 3.Object[] { isauto, lcust, lnum,reasons}：有需要手动安排的客户
		Object o = SoDealHealper.doDeal((SoDealBillVO[])newVos, ui);
		boolean flag = false;
		UFBoolean isauto = UFBoolean.FALSE;
		if(o != null){
			Object[] os = (Object[])o;
			if(os == null || os.length == 0)
				return;
			//是否有一部分客户进行了自动安排
			isauto = PuPubVO.getUFBoolean_NullAs(os[0], UFBoolean.FALSE);
			//未安排的客户信息
			List<SoDealBillVO> lcust = (List<SoDealBillVO>)os[1];		
			//存货库存量，可用量
			List<StoreInvNumVO> lnum = (List<StoreInvNumVO>)os[2];
			//本次不能安排的客户原因
			List<String> reasons = (List<String>)os[3];
			//本站直接安排的客户
			List<String> reasons2 = (List<String>)os[4];
			if(lcust!=null && lcust.size()>0){
				flag = doHandDeal(lcust, lnum);
			}
			StringBuffer bur = new StringBuffer();
			if(reasons2 != null && reasons2.size() >0){
				bur.append("本次直接安排的客户:\n");
				for(int i=0;i<reasons2.size();i++){
					bur.append("**");
					String reason = reasons2.get(i);
					bur.append(reason+"\n");
				}
			}
			if(reasons != null && reasons.size() >0){
				bur.append("本次不能进行安排的客户:\n");
				for(int i=0;i<reasons.size();i++){
					bur.append("**");
					String reason = reasons.get(i);
					bur.append(reason+"\n");
				}
			}
			if(bur.toString().length()>0){
				showWarnMessage(bur.toString());
			}
		}else{
			showWarnMessage("本次安排的所有客户均未达到最小发货量");
		}
		onRefresh();
		ui.showHintMessage("本次安排结束");
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目  手工安排
	 * @时间：2011-7-11下午03:08:02
	 * @param lcust
	 * @param lnum
	 * @throws Exception
	 */
	private boolean doHandDeal(List<SoDealBillVO> lcust,List<StoreInvNumVO> lnum) throws Exception{
		//1.对客户按订单日期 自动 分配  发运量
		SoDealHealper.autoDealNum(lcust, lnum);	
		//2.调用手工安排界面  供用户 手工安排  存量不足货品
		getHandDealDlg().setLcust(lcust);
		getHandDealDlg().setLnum(lnum);
		getHandDealDlg().getDataPanel().setDataToUI();
		int retFlag = getHandDealDlg().showModal();		
		if(retFlag != UIDialog.ID_OK){
			return false;
		}
		//处理手工安排信息  生成运单
		SoDealVO[] bodys = null;
		List<SoDealBillVO> lcust2 = getHandDealDlg().getBuffer().getLcust();
		if(lcust2 == null || lcust2.size() == 0)
			return false;
		List<SoDealVO> ldeal = new ArrayList<SoDealVO>();
		for(SoDealBillVO cust:lcust){
			bodys = cust.getBodyVos();
			SoDealVO[] newBodys = (SoDealVO[])VOUtil.filter(bodys, new FilterNullNum("nnum"));
			for(SoDealVO body:newBodys){
				body.validataOnDeal();
				ldeal.add(body);
			}
		}
		if(ldeal.size() <= 0)
			return false;		
//		过滤最小发货量   手工安排的  不过滤最小发货量   支持 人工 最大
//		过滤零安排量得存货    
//		VOUtil.filter(ldeal, iFilter);
		SoDealHealper.doHandDeal(ldeal, ui);
		return true;
	}
	public class FilterNullNum implements IFilter{
		private String para;
		FilterNullNum(String column){
			this.para = column;
		}
		public boolean accept(Object obj) {
			if( obj instanceof SuperVO){
				SuperVO vo = (SuperVO)obj;
				UFDouble value = PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue(para));
				if(value == null || value.doubleValue() <= 0){
					return false;
				}
				return true;
			}
			return false;
		}
		
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

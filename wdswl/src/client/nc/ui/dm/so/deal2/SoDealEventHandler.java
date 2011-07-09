package nc.ui.dm.so.deal2;

import java.util.List;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
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

public class SoDealEventHandler implements BillEditListener{


	private SoDealClientUI ui = null;
	private SoDealQryDlg m_qrypanel = null;
	//数据缓存
//	private SoDealVO[] m_billdatas = null;
	
	private SoDealBillVO[] m_buffer = null;
	
//	private List<SoDealVO> lseldata = new ArrayList<SoDealVO>();
	
	public SoDealEventHandler(SoDealClientUI parent){
		super();
		ui = parent;
//		getDataPane().addSortRelaObjectListener2(this);
		
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
	
	private SoDealBillVO[] getDataBuffer(){
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
			//			m_qrypanel.setValueRef("h.pk_corp", new UIRefPane("公司目录"));
			//			m_qrypanel.changeValueRef("h.pk_corp", new UIRefPane("公司目录"));
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
		 * 
		 * 
		 * 分仓客商绑定     
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
			showHintMessage("查询完成：没有满足条件的数据");
			return;
		}
		
//		对数据进行合并  按客户合并  订单日期取最小订单日期
		SoDealBillVO[] billvos = SoDealHealper.combinDatas(ui.getWhid(),billdatas);
		
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
		
//		whereSql.append("  nvl(h.dr,0)=0");
//		whereSql.append(" and nvl(wds_sendplanin_b.dr,0)=0 ");
	
//		whereSql.append(" and h.vbillstatus=1");
		//liuys add for wds项目   总仓能查出所有分仓计划,分仓只能查出自己分仓计划(与登录人仓库绑定有关)
		
//		if(!WdsWlPubTool.isZc(ui.getWhid())){//非总仓人员登陆  只能查询 发货仓库为自身的发运计划
			
			whereSql.append(" and tbst.pk_stordoc = '"+ui.getWhid()+"' ");
			
/**
 * 关于总仓可以   看到  分仓的计划 解决方案为  在 查询条件出  增加  仓库的选择
 * 如果是分仓登录  该条件不可编辑默认为 登录仓库
 * 如果是总仓登录  该条件默认为总仓  但可选择分仓-----------暂未实现
 */
			
//		}
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
	public void onDeal(){
		//安排  安排前   数据校验
		/**
		 * 考虑是否特殊安排  过滤最小发货量   考虑库存现存量是否满足   直接安排   手工安排界面
		 * 安排日志
		 */
		
//		获取选中的数据
		AggregatedValueObject[] selectVos = ui.getPanel().getMultiSelectedVOs(SoDealBillVO.class.getName(), SoDealHeaderVo.class.getName(), SoDealVO.class.getName());
		AggregatedValueObject[] newVos = (AggregatedValueObject[])VOUtil.filter(selectVos, new FilterNullBody());
		if(newVos == null || newVos.length == 0){
			showWarnMessage("未选中数据");
			return;
		}
		
//		对数据进行一层严密校验
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
		
//		未安排的客户信息
		List<SoDealBillVO> lcust = (List<SoDealBillVO>)os[0];		
//		库存存量信息
		List<StoreInvNumVO> lnum = (List<StoreInvNumVO>)os[1];
		
		if(lcust==null || lcust.size()<=0)
			return;
//		调用手工安排界面  供用户 手工安排  存量不足货品
		getHandDealDlg().setLcust(lcust);
		getHandDealDlg().setLnum(lnum);
		int retFlag = getHandDealDlg().showModal();
		if(retFlag != UIDialog.ID_OK){
			return;
		}
		
//		处理手工安排信息  生成运单
		
	}
	
	private HandDealDLG m_handDlg = null;
	private HandDealDLG getHandDealDlg(){
		if(m_handDlg == null){
			m_handDlg = new HandDealDLG(ui);
		}
		return m_handDlg;
	}
	
//	private void getLeftDate(List<SuperVO> ldata){
//		List<SoDealVO> leftDate = new ArrayList<SoDealVO>();
//		if(m_billdatas == null || m_billdatas.length==0){
//			ui.showWarningMessage("获取缓存数据出错，请重新查询");
//		}
//		for(SoDealVO dealVO:m_billdatas){
//			if(ldata.contains(dealVO))
//				continue;
//			leftDate.add(dealVO);
//		}
//		getDataPane().setBodyDataVO(leftDate.toArray(new SoDealVO[0]));
//		getDataPane().execLoadFormula();
//		setDataBuffer(leftDate.toArray(new SoDealVO[0]));	
//	}

	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
//		int row = e.getRow();
//		String key = e.getKey();
//		if(row < 0)
//			return;
//		if(key.equalsIgnoreCase("isonsell")){
//			UFBoolean onsell = PuPubVO.getUFBoolean_NullAs(getDataPane().getValueAt(row, "isonsell"), UFBoolean.FALSE);
//			
//			getDataBuffer()[row].setIsonsell(onsell);		
//		}
//		if(key.equalsIgnoreCase("bsel")){
//			UFBoolean bsel = PuPubVO.getUFBoolean_NullAs(getDataPane().getValueAt(row, "bsel"), UFBoolean.FALSE);
//			if(bsel.booleanValue()){
//			    lseldata.add(getDataBuffer()[row]);
//			}else{
//				lseldata.remove(getDataBuffer()[row]);
//			}   
//		}else if("nnum".equalsIgnoreCase(key)){
//			getDataBuffer()[row].setNnum(PuPubVO.getUFDouble_NullAsZero(e.getValue()));
//			getDataBuffer()[row].setNassnum(PuPubVO.getUFDouble_NullAsZero(getDataPane().getValueAt(row, "nassnum")));
//		}else if("nassnum".equalsIgnoreCase(key)){
//			getDataBuffer()[row].setNassnum(PuPubVO.getUFDouble_NullAsZero(e.getValue()));
//			getDataBuffer()[row].setNnum(PuPubVO.getUFDouble_NullAsZero(getDataPane().getValueAt(row, "nnum")));
//		}else if("warehousename".equalsIgnoreCase(key)){
//			getDataBuffer()[row].setCbodywarehouseid(PuPubVO.getString_TrimZeroLenAsNull(getDataPane().getValueAt(row, "cbodywarehouseid")));
//		}
	}

	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		int row = e.getRow();
		if(row<0)
			return;
		getBodyDataPane().clearBodyData();
		getBodyDataPane().setBodyDataVO(getDataBuffer()[row].getBodyVos());
		getBodyDataPane().execLoadFormula();
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

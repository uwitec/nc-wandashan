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
	//数据缓存
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
			//			m_qrypanel.setValueRef("h.pk_corp", new UIRefPane("公司目录"));
			//			m_qrypanel.changeValueRef("h.pk_corp", new UIRefPane("公司目录"));
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
		//处理查询出的计划  缓存  界面
		getDataPane().setBodyDataVO(billdatas);
		getDataPane().execLoadFormula();
		billdatas = (SoDealVO[])getDataPane().getBodyValueVOs(SoDealVO.class.getName());
		setDataBuffer(billdatas);		
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
		whereSql.append(" and h.fstatus ='"+BillStatus.AUDIT+"' and isnull(h.dr,0)=0");//审核通过的
				
		/**
		 * 
		 * bifreceiptfinish              CHAR(1)             是否发货结束                                      NULL                
           bifinventoryfinish            CHAR(1)             是否出库结束     
		 * 
		 * 在 销售扩展子表上 存在表体的行状态   没有进行过滤 如果后续需要  应扩展对  以上发货结束的控制 
		 * 
		 */
		
//		whereSql.append("  nvl(h.dr,0)=0");
//		whereSql.append(" and nvl(wds_sendplanin_b.dr,0)=0 ");
	
//		whereSql.append(" and h.vbillstatus=1");
		//liuys add for wds项目   总仓能查出所有分仓计划,分仓只能查出自己分仓计划(与登录人仓库绑定有关)
		String cwhid  = new LoginInforHelper().getLogInfor(ui.m_ce.getUser().getPrimaryKey()).getWhid();
		if(!WdsWlPubTool.isZc(cwhid)){//非总仓人员登陆  只能查询 发货仓库为自身的发运计划
			
			whereSql.append(" and tbst.pk_stordoc = '"+cwhid+"' ");
		}
		return whereSql.toString();
	}
	
	private void setDataBuffer(SoDealVO[] billdatas){
		this.m_billdatas = billdatas;
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 发运计划 虚拟安排按钮处理方法
	 * @时间：2011-3-25下午02:59:20
	 */
	public void onXNDeal() {
		if (lseldata == null || lseldata.size() == 0) {
			showWarnMessage("请选中要处理的数据");
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
	 * @作者：zhf
	 * @说明：完达山物流项目 
	 * 发运计划  安排按钮处理方法
	 * @时间：2011-3-25下午02:59:20
	 */
	public void onDeal(){
		//安排  安排前   数据校验
		/**
		 * 数据校验
		 * 调出仓库不能为空   调入仓库不能为空 两个仓库不能相同  
		 * 过滤掉本次安排数量为0的行  
		 * 本次安排数量不能大于 计划数量-累计安排数量
		 * 将满足的数据传入后台   数据转换   保存    
		 * 
		 * 分单规则：计划号  发货站 收货站  存货   单据日期   安排日期
		 * 
		 */
		WdsWlPubTool.stopEditing(getDataPane());
		if(lseldata==null||lseldata.size()==0){
			showWarnMessage("请选中要处理的数据");
			return;
		}
		List<SuperVO> ldata = WdsWlPubTool.filterVOsZeroNum(lseldata,"nnum");
		if(ldata == null||ldata.size() == 0){
			showErrorMessage("选中数据没有安排");
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
			
			
		  //对于已经安排的单选中了自动安排的记录 进行再次安排
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
		ui.showHintMessage("安排已经完成...");
	}
	
	private void getLeftDate(List<SuperVO> ldata){
		List<SoDealVO> leftDate = new ArrayList<SoDealVO>();
		if(m_billdatas == null || m_billdatas.length==0){
			ui.showWarningMessage("获取缓存数据出错，请重新查询");
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

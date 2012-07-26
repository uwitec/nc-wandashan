package nc.ui.dm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.ui.pub.bill.BillModel;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.zmpub.pub.tool.SingleVOChangeDataUiTool;
import nc.vo.dm.PlanDealVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.wdsnew.pub.AvailNumBO;
import nc.vo.wdsnew.pub.BillStockBO1;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class PlanDealEventHandler {

	private PlanDealClientUI ui = null;

	private PlanDealQryDlg m_qrypanel = null;
	// 数据缓存
//	private PlanDealVO[] m_billdatas = null;
//	private List<PlanDealVO> lseldata = new ArrayList<PlanDealVO>();
	private String  whereSql = null;

	public PlanDealEventHandler(PlanDealClientUI parent) {
		super();
		ui = parent;
//		getDataPane().addSortRelaObjectListener2(this);

	}
    private AvailNumBO  abo=null;
    public AvailNumBO getAbo(){
    	
    	if(abo==null){
    		abo=new AvailNumBO();
    	}
    	return abo;
    }
	private BillStockBO1 stock=null;
	
	public BillStockBO1 getStock(){
		if(stock ==null){
			stock =new BillStockBO1();
		}
		return stock ;
	}
	private BillModel getDataPane() {
		return ui.getPanel().getBodyBillModel();
	}

	public void onButtonClicked(String btnTag) {
		if (btnTag.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_DEAL)) {
			onDeal();
		} else if (btnTag
				.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELNO)) {
			onNoSel();
		} else if (btnTag
				.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_SELALL)) {
			onAllSel();
		} else if (btnTag
				.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_QRY)) {
			onQuery();
		} else if (btnTag
				.equalsIgnoreCase(WdsWlPubConst.DM_PLANDEAL_BTNTAG_XNDEAL)) {
			onXNDeal();
		}
	}

//	private PlanDealVO[] getDataBuffer() {
//		return m_billdatas;
//	}

	public void onNoSel() {
		int rowcount = getDataPane().getRowCount();
	if(rowcount <= 0)
		return;
//	for (int i = 0; i < ui.getPanel().getChildListPanel().getTable().getRowCount(); i++) {
//		ui.getPanel().getParentListPanel().getTableModel().setRowState(i, BillModel.UNSTATE);
//		ui.headRowChange(i);
//		BillModel model = ui.getPanel().getBodyBillModel();
//		IBillModelRowStateChangeEventListener l = model.getRowStateChangeEventListener();
//		model.removeRowStateChangeEventListener();
		ui.getPanel().getChildListPanel().cancelSelectAllTableRow();
//		model.addRowStateChangeEventListener(l);
		ui.getPanel().updateUI();
//	}
	}

//	private void clearCache() {
//		lseldata.clear();
//		// tsInfor.clear();
//	}

	public void onAllSel() {
//		for (int i = 0; i < ui.getPanel().getParentListPanel().getTable().getRowCount(); i++) {
//			ui.getPanel().getParentListPanel().getTableModel().setRowState(i, BillModel.SELECTED);
//			ui.headRowChange(i);
//			BillModel model = ui.getPanel().getBodyBillModel();
//			IBillModelRowStateChangeEventListener l = model.getRowStateChangeEventListener();
//			model.removeRowStateChangeEventListener();
			ui.getPanel().getChildListPanel().selectAllTableRow();
//			model.addRowStateChangeEventListener(l);
			ui.getPanel().updateUI();
//		}
	}

	private PlanDealQryDlg getQryDlg() {
		if (m_qrypanel == null) {
			m_qrypanel = new PlanDealQryDlg(ui, null, ui.cl.getCorp(),
					WdsWlPubConst.DM_PLAN_DEAL_NODECODE, ui.cl.getUser(), null);
			// m_qrypanel.hideUnitButton();
			// m_qrypanel.hideNormal();
			// m_qrypanel.setConditionEditable("h.pk_corp",true);
			// m_qrypanel.setValueRef("h.pk_corp", new UIRefPane("公司目录"));
			// m_qrypanel.changeValueRef("h.pk_corp", new UIRefPane("公司目录"));
		}
		return m_qrypanel;
	}

	private void clearData() {
//		m_billdatas = null;
//		lseldata.clear();
		getDataPane().clearBodyData();
		tsInfor.clear();
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 查询动作相应方法
	 * @时间：2011-3-25上午09:49:04
	 */
	public void onQuery() {
		/**
		 * 满足什么条件的计划呢？人员和仓库已经绑定了 登陆人只能查询他的权限仓库 总仓的人可以安排分仓的 校验登录人是否为总仓库德人
		 * 如果是可以安排任何仓库的 转分仓 计划 如果是分仓的人 只能 安排 本分仓内部的 发运计划
		 * 
		 */
		getQryDlg().showModal();
		if (!getQryDlg().isCloseOK())
			return;
		PlanDealVO[] billdatas = null;
		try {
			whereSql = getSQL();
			billdatas = PlanDealHealper.doQuery(whereSql);
		} catch (Exception e) {
			e.printStackTrace();
			showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e
					.getMessage()));
			return;
		}
		try {
			setStock(billdatas);
			setAvailNum(billdatas);
		} catch (Exception e) {
			e.printStackTrace();
			showErrorMessage("设置库存量失败");
			return;
		}
		setDataToUI(billdatas);
	}
	private void setAvailNum(PlanDealVO[] billdatas) throws Exception {		
		if(billdatas==null || billdatas.length==0)
			return ;
		for(int i=0;i<billdatas.length;i++){
			billdatas[i].setVdef1(WdsWlPubConst.WDS_STORSTATE_PK_hg);
		}
		//构造现存量查询条件
		StockInvOnHandVO[] vos=(StockInvOnHandVO[]) SingleVOChangeDataUiTool.runChangeVOAry(billdatas, StockInvOnHandVO.class, "nc.ui.wds.self.changedir.CHGWDS2TOACCOUNTNUM");
		if(vos==null || vos.length==0)
			return;
		StockInvOnHandVO[] nvos=(StockInvOnHandVO[]) getAbo().getAvailNumForClient(vos);
		if(nvos==null || nvos.length==0)
			return ;
		for(int i=0;i<billdatas.length;i++){
			if(nvos[i]!=null){		
				UFDouble  uf1=nvos[i].getWhs_stocktonnage();//可用主数量
				UFDouble uf2=nvos[i].getWhs_stockpieces();//可用辅数量
				billdatas[i].setNdrqarrstorenumout(uf1);
				billdatas[i].setNdrqstorenumout(uf2);
			}
		}
	}
	/**
	 * 设置库存 量
	 * @作者：zhf
	 * @说明：完达山物流项目 
	 * @时间：2012-7-3上午10:36:27
	 * @param billdatas
	 * @throws Exception 
	 */
	private void setStock(PlanDealVO[] billdatas) throws Exception {		
		if(billdatas==null || billdatas.length==0)
			return ;
		for(int i=0;i<billdatas.length;i++){
			billdatas[i].setVdef1(WdsWlPubConst.WDS_STORSTATE_PK_hg);
		}
		//构造现存量查询条件
		StockInvOnHandVO[] vos=(StockInvOnHandVO[]) SingleVOChangeDataUiTool.runChangeVOAry(billdatas, StockInvOnHandVO.class, "nc.ui.wds.self.changedir.CHGWDS2TOACCOUNTNUM");
		if(vos==null || vos.length==0)
			return;
		//获得现存量
		StockInvOnHandVO[] nvos=(StockInvOnHandVO[]) getStock().queryStockCombinForClient(vos);
		if(nvos==null || nvos.length==0)
			return ;
		for(int i=0;i<billdatas.length;i++){
			if(nvos[i]!=null){		
				UFDouble  uf1=nvos[i].getWhs_stocktonnage();//库存主数量
				billdatas[i].setNstorenumout(uf1);
			}
		}
	}
	
	Map<String, UFDateTime> tsInfor = new HashMap<String, UFDateTime>();
	
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 :设置数据到界面和缓存
	 * @时间：2011-11-14下午07:41:04
	 * @param billdatas
	 */
	public void setDataToUI(PlanDealVO[] billdatas ){
		clearData();
		if (billdatas == null || billdatas.length == 0) {
			showHintMessage("查询完成：没有满足条件的数据");
			return;
		}	
//		处理时间戳
//		Map<String, UFDateTime> tsInfor = new HashMap<String, UFDateTime>();
		for(PlanDealVO data:billdatas){
			tsInfor.put(data.getPrimaryKey(), data.getTs());
		}	
		// 处理查询出的计划 缓存 界面
		getDataPane().setBodyDataVO(billdatas);
		getDataPane().execLoadFormula();
//		billdatas = (PlanDealVO[]) getDataPane().getBodyValueVOs(
//				PlanDealVO.class.getName());
//		for(PlanDealVO data:billdatas){
//			data.setTs(tsInfor.get(data.getPrimaryKey()));
//		}
//		setDataBuffer(billdatas);
		showHintMessage("查询完成");
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 ：刷新
	 * @时间：2011-11-14下午07:32:22
	 */
	public void onRefresh(){
		clearData();
		PlanDealVO[] billdatas = null;
		try {
			billdatas = PlanDealHealper.doQuery(whereSql);
		} catch (Exception e) {
			e.printStackTrace();
			showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e
					.getMessage()));
			return;
		}
		setDataToUI(billdatas);
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 获得 对 发运计划的查询条件 wds_sendplanin发运计划主表 wds_sendplanin_b 发运计划子表
	 * @时间：2011-3-25上午09:47:50
	 * @return
	 * @throws Exception
	 */
	private String getSQL() throws Exception {
		StringBuffer whereSql = new StringBuffer();
		String where = getQryDlg().getWhereSQL();
		if (where != null && !"".equals(where)) {
			whereSql.append(where + " and");
		}
		whereSql.append("  nvl(wds_sendplanin.dr,0)=0");
		whereSql.append(" and nvl(wds_sendplanin_b.dr,0)=0 ");
		whereSql.append(" and wds_sendplanin.pk_corp='" + ui.cl.getCorp() + "'");
		whereSql.append(" and wds_sendplanin.vbillstatus=1 ");
		whereSql.append(" and wds_sendplanin.iplantype=0 ");
		whereSql.append(" and (coalesce(wds_sendplanin_b.nplannum,0) -  coalesce(wds_sendplanin_b.ndealnum,0)) > 0");
//		zhf 追加 支持过滤关闭 计划
		whereSql.append(" and coalesce(wds_sendplanin_b.reserve14,'N')='N' and coalesce(wds_sendplanin.reserve14,'N') = 'N' ");
		String cwhid = getLoginInforHelper().getLogInfor(
				ui.m_ce.getUser().getPrimaryKey()).getWhid();
		if (!WdsWlPubTool.isZc(cwhid)) {
			whereSql.append(" and wds_sendplanin.pk_outwhouse = '" + cwhid
					+ "' ");
		}
		return whereSql.toString();
	}

	private LoginInforHelper helper = null;

	public LoginInforHelper getLoginInforHelper() {
		if (helper == null) {
			helper = new LoginInforHelper();
		}
		return helper;
	}

//	private void setDataBuffer(PlanDealVO[] billdatas) {
//		this.m_billdatas = billdatas;
//	}

	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 发运计划 虚拟安排按钮处理方法
	 * @时间：2011-3-25下午02:59:20
	 */
	public void onXNDeal() {
//		if (lseldata == null || lseldata.size() == 0) {
//			showWarnMessage("请选中要处理的数据");
//			return;
//		}
//		XnApDLG tdpDlg = new XnApDLG(WdsWlPubConst.XNAP, ui.getEviment().getUser()
//				.getPrimaryKey(), ui.getEviment().getCorporation()
//				.getPk_corp(), ui, lseldata);
//		if(tdpDlg.showModal()== UIDialog.ID_OK){}
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
	 * @说明：完达山物流项目 发运计划 安排按钮处理方法
	 * @时间：2011-3-25下午02:59:20
	 */
	public void onDeal() {
		// 安排 安排前 数据校验
		/**
		 * 数据校验 调出仓库不能为空 调入仓库不能为空 两个仓库不能相同 过滤掉本次安排数量为0的行 本次安排数量不能大于 计划数量-累计安排数量
		 * 将满足的数据传入后台 数据转换 保存
		 * 
		 * 分单规则：计划号 发货站 收货站 存货 单据日期 安排日期
		 * 
		 */
		WdsWlPubTool.stopEditing(getDataPane());
		if (getDataPane().getRowCount() <= 0) {
			showWarnMessage("请选中要处理的数据");
			return;
		}
		
		int[] rows = ui.getPanel().getBodyTable().getSelectedRows();
		if(rows == null || rows.length == 0) {
			showErrorMessage("请选中要处理的数据");
			return;
		}		
		
		PlanDealVO[] vos = (PlanDealVO[])getDataPane().getBodySelectedVOs(PlanDealVO.class.getName());
		if(vos == null || vos.length == 0){
			showErrorMessage("数据获取失败,请重新操作");
			return;
		}
		
		List<SuperVO> ldata = WdsWlPubTool.filterVOsZeroNum(Arrays.asList(vos), "nnum");
		if (ldata == null || ldata.size() == 0) {
			showErrorMessage("选中数据没有安排");
			return;
		}
		try {
			PlanDealVO tmp = null;
			for (SuperVO vo : ldata) {
				tmp = (PlanDealVO)vo;
				tmp.validataOnDeal();
				tmp.setTs(tsInfor.get(tmp.getPrimaryKey()));
			}
			PlanDealHealper.doDeal(ldata, ui);
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof ValidationException) {
				showErrorMessage(e.getMessage());
				return;
			}
			showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e
					.getMessage()));
			return;
		}
		ui.showHintMessage("安排已经完成...");
		onRefresh();
	}

//	public void afterEdit(BillEditEvent e) {
//		// TODO Auto-generated method stub
//		int row = e.getRow();
//		String key = e.getKey();
//		if (row < 0)
//			return;
//		if (key.equalsIgnoreCase("bsel")) {
//			UFBoolean bsel = PuPubVO.getUFBoolean_NullAs(getDataPane()
//					.getValueAt(row, "bsel"), UFBoolean.FALSE);
//			if (bsel.booleanValue()) {
//				lseldata.add(getDataBuffer()[row]);
//			} else {
//				lseldata.remove(getDataBuffer()[row]);
//			}
//		} else if ("nnum".equalsIgnoreCase(key)) {
//			UFDouble hsl = PuPubVO.getUFDouble_NullAsZero(getDataBuffer()[row]
//					.getHsl());
//			UFDouble num = e.getValue() == null ? new UFDouble(0)
//					: new UFDouble(e.getValue().toString());
//			if(hsl != null && hsl.doubleValue()>0){
//				getDataBuffer()[row].setNassnum(num.div(hsl));
//				ui.getPanel().getHeadBillModel().setValueAt(num.div(hsl), row, "nassnum");
//			}
//		} else if ("nassnum".equalsIgnoreCase(key)) {
//			UFDouble assnum = e.getValue() == null ? new UFDouble(0)
//			: new UFDouble(e.getValue().toString());
//			UFDouble hsl = PuPubVO.getUFDouble_NullAsZero(getDataBuffer()[row]
//					.getHsl());	
//			getDataBuffer()[row].setNnum(hsl.multiply(assnum));
//			getDataBuffer()[row].setNassnum(assnum);
//			ui.getPanel().getHeadBillModel().setValueAt(hsl.multiply(assnum), row, "nnum");
//		}
//
//	}

	private void showErrorMessage(String msg) {
		ui.showErrorMessage(msg);
	}

	private void showWarnMessage(String msg) {
		ui.showWarningMessage(msg);
	}

	private void showHintMessage(String msg) {
		ui.showHintMessage(msg);
	}

//	public Object[] getRelaSortObjectArray() {
//		// TODO Auto-generated method stub
//		return getDataBuffer();
//	}
//
//	public void bodyRowChange(BillEditEvent e) {
//		// TODO Auto-generated method stub
//
//	}

}

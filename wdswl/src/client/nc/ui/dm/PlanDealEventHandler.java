package nc.ui.dm;

import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.dm.PlanDealVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class PlanDealEventHandler implements BillEditListener,
		IBillRelaSortListener2 {

	private PlanDealClientUI ui = null;

	private PlanDealQryDlg m_qrypanel = null;
	// 数据缓存
	private PlanDealVO[] m_billdatas = null;
	private List<PlanDealVO> lseldata = new ArrayList<PlanDealVO>();

	// private Map<String, UFDateTime> tsInfor = new HashMap<String,
	// UFDateTime>();

	// private MonthNumAdjustDlg m_monNumDlg = null;

	// private PlanDealVO[] m_combinDatas = null;

	// private String planType= null;

	public PlanDealEventHandler(PlanDealClientUI parent) {
		super();
		ui = parent;
		getDataPane().addSortRelaObjectListener2(this);

	}

	private BillModel getDataPane() {
		return ui.getPanel().getHeadBillModel();
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

	private PlanDealVO[] getDataBuffer() {
		return m_billdatas;
	}

	public void onNoSel() {
		int rowcount = getDataPane().getRowCount();
		if (rowcount <= 0)
			return;
		for (int i = 0; i < rowcount; i++) {
			getDataPane().setValueAt(UFBoolean.FALSE, i, "bsel");
		}
		clearCache();
	}

	private void clearCache() {
		lseldata.clear();
		// tsInfor.clear();
	}

	public void onAllSel() {
		if (getDataBuffer() == null || getDataBuffer().length == 0)
			return;
		PlanDealVO[] datas = getDataBuffer();
		clearCache();
		for (PlanDealVO data : datas) {
			lseldata.add(data);
			// tsInfor.put(data.getPk_plan_b(),data.getTs());
		}

		int rowcount = getDataPane().getRowCount();
		for (int i = 0; i < rowcount; i++) {
			getDataPane().setValueAt(UFBoolean.TRUE, i, "bsel");
		}
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
		m_billdatas = null;
		lseldata.clear();
		getDataPane().clearBodyData();
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
		clearData();
		getQryDlg().showModal();
		if (!getQryDlg().isCloseOK())
			return;
		PlanDealVO[] billdatas = null;
		try {
			String whereSql = getSQL();
			billdatas = PlanDealHealper.doQuery(whereSql);
		} catch (Exception e) {
			e.printStackTrace();
			showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e
					.getMessage()));
			return;
		}

		if (billdatas == null || billdatas.length == 0) {
			clearData();
			showHintMessage("查询完成：没有满足条件的数据");
			return;
		}
		// 处理查询出的计划 缓存 界面
		getDataPane().setBodyDataVO(billdatas);
		getDataPane().execLoadFormula();
		billdatas = (PlanDealVO[]) getDataPane().getBodyValueVOs(
				PlanDealVO.class.getName());
		setDataBuffer(billdatas);
		showHintMessage("查询完成");
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
		whereSql
				.append(" and wds_sendplanin.pk_corp='" + ui.cl.getCorp() + "'");

		whereSql.append(" and wds_sendplanin.vbillstatus=1");
		whereSql
				.append(" and (coalesce(wds_sendplanin_b.nplannum,0) -  coalesce(wds_sendplanin_b.ndealnum,0)) > 0");
		String cwhid = getLoginInforHelper().getLogInfor(
				ui.m_ce.getUser().getPrimaryKey()).getWhid();
		if (!WdsWlPubTool.isZc(cwhid)) {//只有总仓才有发运计划
			whereSql.append(" and wds_sendplanin.pk_inwhouse = '" + cwhid
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

	private void setDataBuffer(PlanDealVO[] billdatas) {
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
		XnApDLG tdpDlg = new XnApDLG(WdsWlPubConst.XNAP, ui.getEviment().getUser()
				.getPrimaryKey(), ui.getEviment().getCorporation()
				.getPk_corp(), ui, lseldata);
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
		if (lseldata == null || lseldata.size() == 0) {
			showWarnMessage("请选中要处理的数据");
			return;
		}
		List<SuperVO> ldata = WdsWlPubTool.filterVOsZeroNum(lseldata, "nnum");
		if (ldata == null || ldata.size() == 0) {
			showErrorMessage("选中数据没有安排");
			return;
		}

		try {
			for (SuperVO vo : ldata) {
				((PlanDealVO) vo).validataOnDeal();
			}
			PlanDealHealper.doDeal(ldata, ui);
			getLeftDate(ldata);
			clearCache();
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
	}

	private void getLeftDate(List<SuperVO> ldata) {
		List<PlanDealVO> leftDate = new ArrayList<PlanDealVO>();
		if (m_billdatas == null || m_billdatas.length == 0) {
			ui.showWarningMessage("获取缓存数据出错，请重新查询");
		}
		for (PlanDealVO dealVO : m_billdatas) {
			if (ldata.contains(dealVO))
				continue;
			leftDate.add(dealVO);
		}
		getDataPane().setBodyDataVO(leftDate.toArray(new PlanDealVO[0]));
		getDataPane().execLoadFormula();
		setDataBuffer(leftDate.toArray(new PlanDealVO[0]));
	}

	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		int row = e.getRow();
		String key = e.getKey();
		if (row < 0)
			return;
		if (key.equalsIgnoreCase("bsel")) {
			UFBoolean bsel = PuPubVO.getUFBoolean_NullAs(getDataPane()
					.getValueAt(row, "bsel"), UFBoolean.FALSE);
			if (bsel.booleanValue()) {
				lseldata.add(getDataBuffer()[row]);
			} else {
				lseldata.remove(getDataBuffer()[row]);
			}
		} else if ("nnum".equalsIgnoreCase(key)) {
			UFDouble hsl = PuPubVO.getUFDouble_NullAsZero(getDataBuffer()[row]
					.getHsl());
			// UFDouble assplannum =
			// PuPubVO.getUFDouble_NullAsZero(getDataBuffer()[row].getNassplannum());
			// if(hsl.doubleValue() == 0 &&assplannum.doubleValue() != 0){
			// hsl = getDataBuffer()[row].getNplannum().div(assplannum);
			// }
			UFDouble num = e.getValue() == null ? new UFDouble(0)
					: new UFDouble(e.getValue().toString());
			getDataBuffer()[row].setNassnum(num.div(hsl));
		} else if ("nassnum".equalsIgnoreCase(key)) {
			UFDouble hsl = PuPubVO.getUFDouble_NullAsZero(getDataBuffer()[row]
					.getHsl());
			// UFDouble assplannum =
			// PuPubVO.getUFDouble_NullAsZero(getDataBuffer()[row].getNassplannum());
			// if(hsl.doubleValue() == 0 &&assplannum.doubleValue() != 0){
			// hsl = getDataBuffer()[row].getNplannum().div(assplannum);
			// }
			UFDouble assnum = e.getValue() == null ? new UFDouble(0)
					: new UFDouble(e.getValue().toString());
			getDataBuffer()[row].setNnum(hsl.multiply(assnum));
			(getDataBuffer()[row]).setNassnum(assnum);
		}

	}

	private void showErrorMessage(String msg) {
		ui.showErrorMessage(msg);
	}

	private void showWarnMessage(String msg) {
		ui.showWarningMessage(msg);
	}

	private void showHintMessage(String msg) {
		ui.showHintMessage(msg);
	}

	public Object[] getRelaSortObjectArray() {
		// TODO Auto-generated method stub
		return getDataBuffer();
	}

	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub

	}

}

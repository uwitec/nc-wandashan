package nc.vo.zmpub.pub.report2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.zmpub.pub.report.buttonaction2.CaPuBtnConst;
import nc.ui.zmpub.pub.report.buttonaction2.CrossAction;
import nc.ui.zmpub.pub.report.buttonaction2.IReportButton;
import nc.ui.zmpub.pub.report.buttonaction2.LevelSubTotalAction;
import nc.ui.zmpub.pub.tool.LongTimeTask;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zmpub.pub.report.ReportBaseVO;

/**
 * 支持分组查询的报表基类 支持数据交叉 分级次的小计 合计 过滤 排序 刷新 以及打印 支持查询动态列 和 初始化动态列
 * 
 * @author mlr
 * @mod mlr 增加报表配置缓存 支持将本次的报表配置 进行保存 下次查询时 自动读取配置 进行报表数据设置
 * 
 */
public class ZmReportBaseUI2 extends JxReportBaseUI {
	// for add mlr 增加报表配置保存按钮
	private ReportBuffer buff = null;// 报表配置缓存

	public ReportBuffer getBuff() {
		if (buff == null) {
			buff = queryBuffer(this._getModelCode());
		}
		if (buff == null) {
			buff = new ReportBuffer();
			buff.setNodecode(_getModelCode());
		}
		return buff;
	}

	/**
	 * 根据节点号从数据库中查询配置
	 * 
	 * @param modelCode
	 * @return
	 */
	public ReportBuffer queryBuffer(String modelCode) {
		ReportBuffer buffer = null;
		try {
			Class[] ParameterTypes = new Class[] { String.class };
			Object[] ParameterValues = new Object[] { modelCode };
			Object o = LongTimeTask.calllongTimeService("zmpub", this,
					"正在查询...", 1, "nc.bs.zmpub.pub.report.BufferDMO", null,
					"queryByNodeId", ParameterTypes, ParameterValues);
			if (o != null) {
				buffer = (ReportBuffer) o;
			}
		} catch (Exception e) {
			Logger.error(e);
			MessageDialog.showErrorDlg(this, "警告", e.getMessage());
		}
		return buffer;
	}

	public int[] getReportButtonAry() {
		m_buttonArray = new int[] { IReportButton.QueryBtn,
				IReportButton.LevelSubTotalBtn, IReportButton.CrossBtn,
				IReportButton.PrintBtn, CaPuBtnConst.onboRefresh,
				CaPuBtnConst.save, };
		return m_buttonArray;
	}

	/**
	 * 将缓存配置更新到数据库
	 * 
	 * @param modelCode
	 * @return
	 */
	public ReportBuffer updateBuffer(ReportBuffer buff) {
		ReportBuffer buffer = null;
		try {
			Class[] ParameterTypes = new Class[] { ReportBuffer.class };
			Object[] ParameterValues = new Object[] { buff };
			Object o = LongTimeTask.calllongTimeService("zmpub", this,
					"正在查询...", 1, "nc.bs.zmpub.pub.report.BufferDMO", null,
					"updateBuffer", ParameterTypes, ParameterValues);
			if (o != null) {
				buffer = (ReportBuffer) o;
			}
		} catch (Exception e) {
			Logger.error(e);
			MessageDialog.showErrorDlg(this, "警告", e.getMessage());
		}
		return buffer;
	}

	public void setBuff(ReportBuffer buff) {
		this.buff = buff;
	}

	public ZmReportBaseUI2() {
		super();
		// getBuff();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2436203500270302801L;

	private String[] sqls = null;

	@Override
	public Map getNewItems() throws Exception {
		return null;
	}

	@Override
	public String getQuerySQL() throws Exception {
		return null;
	}

	protected void initPrivateButton() {

		addPrivateButton("保存设置", "保存设置", CaPuBtnConst.save);
		super.initPrivateButton();
	}

	/**
     * 
     */
	protected void onBoElse(Integer intBtn) throws Exception {
		switch (intBtn) {
		case CaPuBtnConst.save: {
			onBoSave();
			break;
		}
		}
		super.onBoElse(intBtn);
	}

	/**
	 * 报表保存设置按钮
	 */
	private void onBoSave() {
		if (getBuff() == null) {
			return;
		} else {
			updateBuffer(getBuff());
		}
	}

	@Override
	public void initReportUI() {

	}

	@Override
	public void onQuery() {
		getQueryDlg().showModal();
		if (getQueryDlg().getResult() == UIDialog.ID_OK) {
			try {
				// 清空表体数据
				clearBody();
				setDynamicColumn1();
				// 得到查询结果
				setColumn();

				List<ReportBaseVO[]> list = getReportVO(getSqls());
				sqls = getSqls();
				ReportBaseVO[] vos = null;
				vos = dealBeforeSetUI(list);
				if (vos == null || vos.length == 0)
					return;
				if (vos != null) {
					super.updateBodyDigits();
					setBodyVO(vos);
					updateVOFromModel();// 重新加载初始化公式
					dealQueryAfter();// 查询后的后续处理 一般用于 默认数据交叉之类的操作
				}
			} catch (BusinessException e) {
				e.printStackTrace();
				this.showErrorMessage(e.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				this.showErrorMessage(e.getMessage());

			}
		}
	}

	/**
	 * 接收查询的组合sql
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-12-22上午10:41:05
	 * @return
	 */
	public String[] getSqls() throws Exception {
		return null;

	}

	public void onboRefresh() throws Exception {
		// 清空表体数据
		clearBody();
		// 设置动态列
		setDynamicColumn1();
		// 得到查询结果
		ReportBaseVO[] vos = null;
		// 设置基本列合并
		setColumn();
		// 设置vo
		List<ReportBaseVO[]> list = getReportVO(getSqls());
		sqls = getSqls();
		ReportBaseVO[] vos1 = null;
		vos1 = dealBeforeSetUI(list);
		if (vos1 == null || vos1.length == 0)
			return;
		if (vos1 != null) {
			super.updateBodyDigits();
			setBodyVO(vos1);
			updateVOFromModel();// 重新加载初始化公式
			dealQueryAfter();// 查询后的后续处理 一般用于 默认数据交叉之类的操作
		}
		this.showHintMessage("刷新操作结束");
	}

	/**
	 * 设置到ui界面之前 处理分组查询后的数据
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-12-22上午10:42:36
	 * @param list
	 * @return
	 */
	public ReportBaseVO[] dealBeforeSetUI(List<ReportBaseVO[]> list)
			throws Exception {
		if (list == null || list.size() == 0) {
			return null;
		}
		List<ReportBaseVO> zlist = new ArrayList<ReportBaseVO>();
		for (int i = 0; i < list.size(); i++) {
			ReportBaseVO[] vos = list.get(i);
			if (vos == null || vos.length == 0)
				continue;
			for (int j = 0; j < vos.length; j++) {
				zlist.add(vos[j]);
			}

		}
		return zlist.toArray(new ReportBaseVO[0]);
	}

	/**
	 * 查询完成 设置到ui界面之后 后续处理
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-12-22上午10:42:36
	 * @param list
	 * @return
	 */
	public void dealQueryAfter() throws Exception {
		ReportBuffer buff = getBuff();
		if (buff == null) {
			return;
		}
		String[] rows = buff.getStrRows();
		String[] cols = buff.getStrCols();
		String[] vals = buff.getStrVals();
		UFBoolean istotal = buff.getIstotal();
		Integer lel = buff.getLel();
		/**
		 * 执行数据交叉
		 */
		if (rows != null && cols != null && vals != null && rows.length != 0
				&& cols.length != 0 && vals.length != 0) {
			nc.ui.pub.ButtonObject bo = (ButtonObject) getButton_action_map()
					.getButtonMap().get(IReportButton.CrossBtn);
			CrossAction action = (CrossAction) getButton_action_map().get(bo);
			if (istotal == null)
				istotal = UFBoolean.FALSE;
			action.execute2(rows, cols, vals, istotal, lel);

		}
		/**
		 * 执行合计
		 */
		UFBoolean issub = buff.getIssub();
		UFBoolean issum = buff.getIssum();
		String[] totalfields = buff.getTotfields();
		String[] totalfieldsName = buff.getTotfieldsNames();
		if (totalfields != null && totalfieldsName != null
				&& totalfields.length != 0 && totalfieldsName.length != 0) {
			nc.ui.pub.ButtonObject bo = (ButtonObject) getButton_action_map()
					.getButtonMap().get(IReportButton.LevelSubTotalBtn);
			LevelSubTotalAction action = (LevelSubTotalAction) getButton_action_map()
					.get(bo);
			if (istotal == null)
				istotal = UFBoolean.FALSE;
			action.atuoexecute2(PuPubVO.getUFBoolean_NullAs(issub,
					UFBoolean.FALSE).booleanValue(),
					PuPubVO.getUFBoolean_NullAs(issum, UFBoolean.FALSE)
							.booleanValue(), totalfields, totalfieldsName);
		}
		return;
	}

	/**
	 * 分组查询
	 */
	public List<ReportBaseVO[]> getReportVO(String[] sqls)
			throws BusinessException {
		List<ReportBaseVO[]> reportVOs = null;
		try {
			Class[] ParameterTypes = new Class[] { String[].class };
			Object[] ParameterValues = new Object[] { sqls };
			Object o = LongTimeTask.calllongTimeService("zmpub", this,
					"正在查询...", 1, "nc.bs.zmpub.pub.report.ReportDMO", null,
					"queryVOBySql", ParameterTypes, ParameterValues);
			if (o != null) {
				reportVOs = (List<ReportBaseVO[]>) o;
			}
		} catch (Exception e) {
			Logger.error(e);
			MessageDialog.showErrorDlg(this, "警告", e.getMessage());
		}
		return reportVOs;
	}

}

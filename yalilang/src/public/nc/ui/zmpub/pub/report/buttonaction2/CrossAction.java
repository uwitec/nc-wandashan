package nc.ui.zmpub.pub.report.buttonaction2;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.report.ReportItem;
import nc.ui.scm.util.ObjectUtils;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.querymodel.RotateCrossVO;
import nc.vo.pub.rs.MemoryResultSetMetaData;
import nc.vo.zmpub.pub.report2.CrossDLG;
import nc.vo.zmpub.pub.report2.ReportBaseUI;
import nc.vo.zmpub.pub.report2.ReportBuffer;
import nc.vo.zmpub.pub.report2.ReportRowColCrossTool;
import nc.vo.zmpub.pub.report2.ZmReportBaseUI2;

/**
 * 交叉处理 原先报表自带的交叉处理不好使 现在对数据交叉对话框进行了重新构造 借鉴了查询引擎的交叉对话框
 * 
 * @author mlr
 */
public class CrossAction extends AbstractActionHasDataAvailable {
	private RotateCrossVO m_rc = new RotateCrossVO();
	private SelectFldVO[] fls = null;
	private ReportBaseUI ui = null;
	private MemoryResultSetMetaData mrsmd = null;
	private Vector vc = null;// 内存结果集
	private ReportItem[] items = null;
	private boolean istotal = false;
	private int leaval = 0;

	public CrossAction() {
		super();
	}

	public CrossAction(ReportBaseUI reportBaseUI) {
		super(reportBaseUI);
	}

	public void execute() throws Exception {
		CrossDLG dlg = new CrossDLG(getReportBaseUI());
		if (fls == null) {
			fls = getFls();
		}
		dlg.setRotateCross(m_rc, fls);
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			m_rc = dlg.getRotateCross();
			istotal = dlg.getIsTotal1();
			leaval = dlg.getComboBox().getSelectedIndex() + 1;
			// 执行交叉
			try {
				String[] rows = m_rc.getStrRows();
				String[] cols = m_rc.getStrCols();
				String[] vals = m_rc.getStrVals();
				if (istotal == true && vals.length > 1) {
					ui.showErrorMessage("只有存在一个交叉值的情况下,才能横向合计");
					throw new Exception("只有存在一个交叉值的情况下,才能横向合计");
				}
				getUI();
				ReportRowColCrossTool.onCross(getUI(), mrsmd, vc, items, rows,
						cols, vals, istotal, leaval);
				// 设置缓存
				ZmReportBaseUI2 ui = (ZmReportBaseUI2) getUI();
				ReportBuffer buf = ui.getBuff();
				buf.setStrRows(rows);
				buf.setStrCols(cols);
				buf.setStrVals(vals);
				buf.setLel(leaval);
				buf.setIstotal(new UFBoolean(istotal));
			} catch (Exception e) {
				Logger.error(e);
			}
		}
	}

	private ReportBaseUI getUI() throws Exception {
		if (ui == null) {
			ui = getReportBaseUI();
			vc = (Vector) ObjectUtils.serializableClone(ui.getReportBase()
					.getBillModel().getDataVector());
			mrsmd = (MemoryResultSetMetaData) ObjectUtils.serializableClone(ui
					.getReportBase().getReportGeneralUtil().createMeteData());
			items = ui.getReportBase().getBody_Items();
		}
		return ui;
	}

	private SelectFldVO[] getFls() {
		ReportItem[] its = getReportBaseUI().getReportBase().getBody_Items();
		List<SelectFldVO> list = new ArrayList<SelectFldVO>();
		for (int i = 0; i < its.length; i++) {
			if (its[i].isShow() == false)
				continue;
			SelectFldVO fs = new SelectFldVO();
			fs.setColtype(its[i].getDataType());
			fs.setDirty(false);
			fs.setFldalias(its[i].getKey());
			fs.setExpression(its[i].getKey());
			fs.setFldname(its[i].getName());
			// fs.setNote();
			list.add(fs);
		}
		return list.toArray(new SelectFldVO[0]);
	}

	/**
	 * 执行缓存交差 mlr
	 * 
	 * @param rows
	 * @param cols
	 * @param vals
	 * @param istotal2
	 * @param lel
	 * @throws Exception
	 */
	public void execute2(String[] rows, String[] cols, String[] vals,
			UFBoolean istotal2, Integer lel) throws Exception {
		if (fls == null) {
			fls = getFls();
		}
		if (istotal == true && vals.length > 1) {
			ui.showErrorMessage("只有存在一个交叉值的情况下,才能横向合计");
			throw new Exception("只有存在一个交叉值的情况下,才能横向合计");
		}
		getUI();
		ReportRowColCrossTool.onCross(getUI(), mrsmd, vc, items, rows, cols,
				vals, istotal2.booleanValue(), lel);
	}
}

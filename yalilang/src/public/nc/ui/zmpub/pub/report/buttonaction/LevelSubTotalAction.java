package nc.ui.zmpub.pub.report.buttonaction;

/**
 * 分级小计  事件
 * @author guanyj1
 * @date:2007年8月9日
 */
import java.util.ArrayList;
import java.util.Arrays;

import nc.bs.logging.Logger;
import nc.ui.report.base.ISubTotalConf;
import nc.ui.trade.report.subtotal.SubTotalConfDLG;
import nc.ui.zmpub.pub.report.ReportBaseUI;
import nc.vo.pub.BusinessException;
import nc.vo.pub.report.SubtotalContext;
import nc.vo.trade.report.IReportModelDataTypes;
import nc.vo.trade.report.TableField;

public class LevelSubTotalAction extends AbstractActionHasDataAvailable
		implements ISubTotalConf {

	// 小计合计分组字段
	private TableField[] subTotalCurrentUsingGroupFields = new TableField[0];

	// 小计合计汇总字段
	private TableField[] SubTotalCurrentUsingTotalFields = new TableField[0];
	// add by ycy 2009-09-07 可选择是否小计 是否合计 之前界面选择没有效果
	private boolean issub = false;

	private boolean issum = true;

	public LevelSubTotalAction(ReportBaseUI reportBaseUI) {
		super(reportBaseUI);
	}

	public LevelSubTotalAction() {
		super();
	}

	/**
	 * 产生小计合计窗口 点击[确定]调用executeSubTotal()
	 */
	public void execute() throws Exception {
		SubTotalConfDLG dlg = new SubTotalConfDLG(getReportBaseUI(), this);
		if (dlg.showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
			issub = dlg.isSub();
			issum = dlg.isSum();
			setSubTotalCurrentUsingGroupFields(dlg.getGroupFields());
			setSubTotalCurrentUsingTotalFields(dlg.getTotalFields());
			getReportBaseUI().setBodyDataForSub();// 将表体数据中的合计 小计行去掉

			executeSubTotal();

			getReportBaseUI()
					.showHintMessage(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"uifactory_report",
									"UPPuifactory_report-000022")/*
																 * @res "小计合计成功"
																 */);
		}
	}

	/**
	 * 判断条件 小计合计 2007-8-23 下午03:36:35 guanyj1
	 */
	public void executeSubTotal() throws Exception {
		// 表体为空的情况
		if (getReportBaseUI().getBodyDataVO() == null
				|| getReportBaseUI().getBodyDataVO().length == 0) {
			Logger.debug("报表中没有数据，小计合计操作取消");
			return;
		}
		// 未选择分组 合计 字段的情况
		if (getSubTotalCurrentUsingGroupFields() == null
				|| getSubTotalCurrentUsingGroupFields().length == 0
				|| getSubTotalCurrentUsingTotalFields() == null
				|| getSubTotalCurrentUsingTotalFields().length == 0)
			throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("uifactory_report",
							"UPPuifactory_report-000019")/*
														 * @res
														 * "分组字段和合计字段至少需要各选一项才能进行小计合计操作。"
														 */);

		// 正常情况 下 开始小计合计
		if (getSubTotalCurrentUsingGroupFields() != null
				&& getSubTotalCurrentUsingGroupFields().length != 0
				&& getSubTotalCurrentUsingTotalFields() != null
				&& getSubTotalCurrentUsingTotalFields().length != 0) {
			String[] gfs = new String[getSubTotalCurrentUsingGroupFields().length];
			for (int i = 0; i < gfs.length; i++) {
				gfs[i] = getSubTotalCurrentUsingGroupFields()[i].getFieldName();
				gfs[i] = gfs[i].replace('.', '_');
			}
			String[] tfs = new String[getSubTotalCurrentUsingTotalFields().length];
			for (int i = 0; i < tfs.length; i++) {
				tfs[i] = getSubTotalCurrentUsingTotalFields()[i].getFieldName();
				tfs[i] = tfs[i].replace('.', '_');
			}

			onSubtotal(gfs, tfs);

		}
	}

	public boolean isIssub() {
		return issub;
	}

	public void setIssub(boolean issub) {
		this.issub = issub;
	}

	public boolean isIssum() {
		return issum;
	}

	public void setIssum(boolean issum) {
		this.issum = issum;
	}

	/**
	 * 进行小计合计
	 * 
	 * @param gfs
	 * @param tfs
	 * @throws Exception
	 *             2007-8-23 下午04:15:33 guanyj1
	 */
	public void onSubtotal(String[] gfs, String[] tfs) throws Exception {
		SubtotalContext context = new SubtotalContext();
		context.setGrpKeys(gfs);
		context.setSubtotalCols(tfs);
		context.setTotalNameColKeys(gfs[0]);
		context.setIsSubtotal(issub);
		context.setIsSumtotal(issum);
		context.setSubtotalName("——小计——");
		context.setSumtotalName("——合计——");
		getReportBaseUI().onSubTotal(context);
	}

	/**
	 * 将TableField[]转化成String[]
	 * 
	 * @param table
	 * @return 2007-8-24 下午02:55:01 guanyj1
	 */
	public String[] totalTableFieldToString(TableField[] table) {
		String[] tfs = new String[table.length];
		for (int i = 0; i < tfs.length; i++) {
			tfs[i] = table[i].getFieldName();
			tfs[i] = tfs[i].replace('.', '_');
		}
		return tfs;
	}

	/**
	 * 将分组字段转化为TableField[]
	 * 
	 * @param gfs
	 * @return 2007-8-24 上午09:00:09 guanyj1
	 */
	public TableField[] groupStringToTableField(String[] gfs) {
		TableField[] all = getSubTotalCandidateGroupFields();
		TableField[] temp = new TableField[gfs.length];

		for (int i = 0; i < gfs.length; i++) {
			for (int j = 0; j < all.length; j++) {
				if (gfs[i].equals(all[j].getFieldName().replace('.', '_'))) {
					temp[i] = all[j];
					continue;
				}
			}
		}

		return temp;
	}

	/**
	 * 将汇总字段转化为TableField[]
	 * 
	 * @param tfs
	 * @return 2007-8-24 上午09:04:02 guanyj1
	 */
	public TableField[] totalStringToTableField(String[] tfs) {
		TableField[] all = getSubTotalCandidateTotalFields();
		TableField[] temp = new TableField[tfs.length];

		for (int i = 0; i < tfs.length; i++) {
			for (int j = 0; j < all.length; j++) {
				if (tfs[i].equals(all[j].getFieldName().replace('.', '_'))) {
					temp[i] = all[j];
					continue;
				}
			}
		}

		return temp;
	}

	/**
	 * @param newSubTotalCurrentUsingGroupFields
	 *            nc.vo.tm.report.TableField[]
	 */
	public void setSubTotalCurrentUsingGroupFields(
			TableField[] newSubTotalCurrentUsingGroupFields) {
		subTotalCurrentUsingGroupFields = newSubTotalCurrentUsingGroupFields;
	}

	/**
	 * 
	 * @param newSubTotalCurrentUsingTotalFields
	 *            nc.vo.tm.report.TableField[]
	 */
	public void setSubTotalCurrentUsingTotalFields(
			TableField[] newSubTotalCurrentUsingTotalFields) {
		SubTotalCurrentUsingTotalFields = newSubTotalCurrentUsingTotalFields;
	}

	/**
	 * 
	 * @return nc.vo.tm.report.TableField[]
	 */
	public TableField[] getSubTotalCurrentUsingGroupFields() {
		return subTotalCurrentUsingGroupFields;
	}

	/**
	 * 
	 * @return nc.vo.tm.report.TableField[]
	 */
	public TableField[] getSubTotalCurrentUsingTotalFields() {
		return SubTotalCurrentUsingTotalFields;
	}

	/**
	 * 返回可选的小计合计分组字段。 界面上所显示的所有字符型字段作为可选的小计合计分组字段
	 * 
	 * @return nc.vo.tm.report.TableField[]
	 */
	public TableField[] getSubTotalCandidateGroupFields() {
		return getReportBaseUI().getVisibleFieldsByDataType(
				IReportModelDataTypes.STRING);
	}

	/**
	 * 返回可选的小计合计汇总字段。 界面上所显示的所有数值型(包括浮点和整型)字段作为可选的小计合计汇总字段。
	 * 
	 * @return nc.vo.tm.report.TotalField[]
	 */
	public TableField[] getSubTotalCandidateTotalFields() {
		TableField[] f = getReportBaseUI().getVisibleFieldsByDataType(
				IReportModelDataTypes.FLOAT);
		TableField[] i = getReportBaseUI().getVisibleFieldsByDataType(
				IReportModelDataTypes.INTEGER);
		ArrayList<TableField> al = new ArrayList<TableField>();
		al.addAll(Arrays.asList(f));
		al.addAll(Arrays.asList(i));
		return (TableField[]) al.toArray(new TableField[0]);
	}
}

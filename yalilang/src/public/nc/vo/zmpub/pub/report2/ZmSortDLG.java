package nc.vo.zmpub.pub.report2;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JList;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.dialog.StandardUIDialog;
import nc.ui.trade.component.ListToListPanel;
import nc.ui.trade.report.sort.SortListItem;
import nc.ui.trade.report.sort.SortListRenderer;
import nc.vo.trade.report.TableField;

/**
 * mlr 添加对 交叉后的数据的 排序功能
 */
public class ZmSortDLG extends StandardUIDialog {
	public ListToListPanel centerPane = null;

	public SortListItem[] leftItems = null;

	public SortListItem[] rightItems = null;

	public AllEventHandler eventHandler = new AllEventHandler();

	public UIButton ascBt = null;

	public UIButton descBt = null;

	class AllEventHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == ascBt) {
				UIList list = (UIList) getCenterPane().getRightList()
						.getViewComponent();
				if (list.getSelectedValues() != null) {
					for (int i = 0; i < list.getSelectedValues().length; i++) {
						((SortListItem) list.getSelectedValues()[i])
								.setAsc(true);
					}
					list.repaint();
				}
			} else if (e.getSource() == descBt) {
				UIList list = (UIList) getCenterPane().getRightList()
						.getViewComponent();
				if (list.getSelectedValues() != null) {
					for (int i = 0; i < list.getSelectedValues().length; i++) {
						((SortListItem) list.getSelectedValues()[i])
								.setAsc(false);
					}
					list.repaint();
				}
			}
		}

	}

	/**
	 * @param parent
	 * @param title
	 */
	public ZmSortDLG(Container parent, String title, TableField[] leftField,
			TableField[] rightField) {
		super(parent, title);
		initData(leftField, rightField);
		initLayout();
		initListeners();
	}

	/**
	 *
	 */
	public void initListeners() {
		getDescBt().addActionListener(eventHandler);
		getAscBt().addActionListener(eventHandler);
	}

	/**
	 *
	 */
	public void initData(TableField[] leftField, TableField[] rightField) {
		if (leftField != null) {
			leftItems = new SortListItem[leftField.length];
			for (int i = 0; i < leftItems.length; i++) {
				leftItems[i] = new SortListItem();
				leftItems[i].setTableField(leftField[i]);
				leftItems[i].setAsc(true);
			}
		}
		if (rightField != null) {
			rightItems = new SortListItem[rightField.length];
			for (int i = 0; i < rightItems.length; i++) {
				rightItems[i] = new SortListItem();
				rightItems[i].setTableField(rightField[i]);
				rightItems[i].setAsc(true);
			}
		}
	}

	/**
	 *
	 */
	public void initLayout() {
		this.setSize(620, 475);

		this.themePanel
				.setTheme(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"uifactory_report", "UPPuifactory_report-000006")/*
																		 * @res
																		 * "排序设置"
																		 */);
		this.themePanel
				.setDetailTheme(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"uifactory_report", "UPPuifactory_report-000151")/*
																		 * @res
																		 * "从左侧列表中选择你要排序的字段,加入到右侧面板,并可调整顺序"
																		 */);

		Container contentPane = this.editorPanel;
		contentPane.setLayout(new BorderLayout());
		contentPane.add(getCenterPane(), BorderLayout.CENTER);
		((JList) getCenterPane().getRightList().getViewComponent())
				.setCellRenderer(new SortListRenderer());
	}

	/**
	 * @return 返回 centerPane。
	 */
	public ListToListPanel getCenterPane() {
		if (centerPane == null) {
			centerPane = new ListToListPanel(
					nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"uifactory_report", "UPPuifactory_report-000152")/*
																			 * @res
																			 * "未排序字段"
																			 */,
					leftItems,
					nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"uifactory_report", "UPPuifactory_report-000153")/*
																			 * @res
																			 * "待排序字段"
																			 */,
					rightItems);
			UIPanel panel = new UIPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.add(getAscBt());
			panel.add(Box.createVerticalStrut(ListToListPanel.VERTI_HEIGHT));
			panel.add(getDescBt());
			centerPane.setExtensionPanel(panel);
		}
		return centerPane;
	}

	/**
	 * 得到排序字段升序或降序
	 * 
	 * @return
	 */
	public int[] getAscOrDesc() {
		Object[] items = getCenterPane().getRightData();
		if (items.length == 0)
			return null;
		int[] asc = new int[items.length];
		for (int i = 0; i < items.length; i++) {
			asc[i] = ((SortListItem) items[i]).isAsc() ? 1 : -1;
		}
		return asc;
	}

	/**
	 * 得到排序字段
	 * 
	 * @return
	 */
	public String[] getSortFields() {
		Object[] items = getCenterPane().getRightData();
		if (items.length == 0)
			return null;
		String[] fields = new String[items.length];
		for (int i = 0; i < items.length; i++) {
			fields[i] = ((SortListItem) items[i]).getTableField()
					.getFieldName();
		}
		return fields;
	}

	/**
	 * @return 返回 ascBt。
	 */
	public UIButton getAscBt() {
		if (ascBt == null) {
			ascBt = new UIButton();
			ascBt
					.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"uifactory_report", "UPPuifactory_report-000100")/*
																			 * @res
																			 * "升序"
																			 */);
		}
		return ascBt;
	}

	/**
	 * @return 返回 descBt。
	 */
	public UIButton getDescBt() {
		if (descBt == null) {
			descBt = new UIButton();
			descBt
					.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"uifactory_report", "UPPuifactory_report-000101")/*
																			 * @res
																			 * "降序"
																			 */);
		}
		return descBt;
	}

	@Override
	protected void complete() throws Exception {
	}
}
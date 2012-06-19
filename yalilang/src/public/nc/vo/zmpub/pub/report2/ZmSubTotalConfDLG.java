/*
 * 创建日期 2005-9-2
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.vo.zmpub.pub.report2;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.dialog.StandardUIDialog;
import nc.ui.report.base.ISubTotalConf;
import nc.ui.trade.component.ListToListPanel;
import nc.vo.trade.report.TableField;

/**
 * @author dengjt  modify mlr支持是否显示原始行
 * 创建日期 2005-9-2
 */
public class ZmSubTotalConfDLG extends StandardUIDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7376433796357942322L;
	private ISubTotalConf istc = null;
	private ListToListPanel groupPanel = null;
	private ListToListPanel totalPanel = null;
	private UIPanel settingPane = null;
	private UICheckBox sumChooser = null;
	private UICheckBox subChooser = null;
	private UICheckBox levelChooser = null;


	public ZmSubTotalConfDLG(java.awt.Container parent, ISubTotalConf istc)
	{
		super(parent);
		this.istc = istc;
		initLayout();
	}


	/**
	 *
	 */
	private void initLayout()
	{
		setSize(650, 650);

		setErrorMsgSize(new Dimension(680,500));

		setResizable(true);

		this.themePanel.setTheme(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000156")/*@res "小计合计设置"*/);
		this.themePanel.setDetailTheme(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000157")/*@res "选择分组字段和要小计的字段,以及其他小计合计选项"*/);

		Container contentPane = this.editorPanel;

		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(getGroupPanel());
		contentPane.add(getSettingPane());
		contentPane.add(getTotalPanel());
		// zjb+
		setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000007")/*@res "分级小计"*/);
	}


	/**
	 * @return 返回 groupPanel。
	 */
	private ListToListPanel getGroupPanel()
	{
		if(groupPanel == null)
		{
			TableField[] all = istc.getSubTotalCandidateGroupFields();
			TableField[] using = istc.getSubTotalCurrentUsingGroupFields();
			java.util.ArrayList al = new java.util.ArrayList();
			for (int i = 0; i < all.length; i++)
			{
				if (!java.util.Arrays.asList(using).contains(all[i]))
					al.add(all[i]);
			}
			TableField[] nousing = (TableField[]) al.toArray(new TableField[0]);
			groupPanel = new ListToListPanel(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000158")/*@res "可选分组字段"*/, nousing, nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000159")/*@res "已选分组字段"*/,
					using);

		}
		return groupPanel;
	}
	/**
	 * @return 返回 totalPanel。
	 */
	private ListToListPanel getTotalPanel()
	{
		if(totalPanel == null)
		{
			TableField[] all = istc.getSubTotalCandidateTotalFields();
			TableField[] using = istc.getSubTotalCurrentUsingTotalFields();
			java.util.ArrayList al = new java.util.ArrayList();
			for (int i = 0; i < all.length; i++)
			{
				if (!java.util.Arrays.asList(using).contains(all[i]))
					al.add(all[i]);
			}
			TableField[] nousing = (TableField[]) al.toArray(new TableField[0]);
			totalPanel = new ListToListPanel(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000160")/*@res "可选汇总字段"*/, nousing, nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000161")/*@res "已选汇总字段"*/,
					using, null, true);
		}
		return totalPanel;
	}
	/**
	 * @return 返回 settingPane。
	 */
	private UIPanel getSettingPane()
	{
		if(settingPane == null)
		{
			settingPane = new UIPanel();
			settingPane.setBorder(BorderFactory.createTitledBorder(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000162")/*@res "小计合计选项"*/));
			settingPane.add(getSubChooser());
			settingPane.add(getSumChooser());
			settingPane.add(getLevelChooser());
		}
		return settingPane;
	}
	/**
	 * @return 返回 subChooser。
	 */
	protected UICheckBox getSubChooser()
	{
		if(subChooser == null)
		{
			subChooser = new UICheckBox();
			subChooser.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000142")/*@res "计算小计"*/);
			subChooser.setSelected(true);
		}
		return subChooser;
	}
	/**
	 * @return 返回 sumChooser。
	 */
	private UICheckBox getSumChooser()
	{
		if(sumChooser == null)
		{
			sumChooser = new UICheckBox();
			sumChooser.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000145")/*@res "计算合计"*/);
			sumChooser.setSelected(true);
		}
		return sumChooser;
	}
	/**
	 * @return 返回 levelChooser。
	 */
	private UICheckBox getLevelChooser()
	{
		if(levelChooser == null)
		{
			levelChooser = new UICheckBox();
			levelChooser.setText("显示原始行");
			levelChooser.setSelected(true);
		}
		return levelChooser;
	}

	/**
	 * 该方法得到SQL语句中Group的字段 返回数组中的顺序即是Group的顺序
	 */
	public TableField[] getGroupFields()
	{
		TableField[] fs = new TableField[getGroupPanel().getRightData().length];
		for (int i = 0; i < fs.length; i++)
		{
			fs[i] = (TableField) getGroupPanel().getRightData()[i];
		}
		return fs;
	}

	/**
	 * 该方法得到所有的汇总字段， （一般来说总是数值类型的字段） 以及需要对这些字段进行的操作，sum or avg
	 *
	 * @return nc.ui.report.base.TotalField[]
	 */
	public TableField[] getTotalFields()
	{
		TableField[] fs = new TableField[getTotalPanel().getRightData().length];
		for (int i = 0; i < fs.length; i++)
		{
			fs[i] = (TableField) getTotalPanel().getRightData()[i];
		}
		return fs;
	}
	/**
	 * 该方法得到所有的汇总字段， （一般来说总是数值类型的字段） 以及需要对这些字段进行的操作，sum or avg
	 *
	 * @return nc.ui.report.base.TotalField[]
	 */
	public TableField[] getTotalFields2()
	{
		TableField[] fs = new TableField[getTotalPanel().getLeftData().length];
		for (int i = 0; i < fs.length; i++)
		{
			fs[i] = (TableField) getTotalPanel().getLeftData()[i];
		}
		return fs;
	}
	/**
	 * 该方法得到SQL语句中Group的字段 返回数组中的顺序即是Group的顺序
	 */
	public TableField[] getGroupFields2()
	{
		TableField[] fs = new TableField[getGroupPanel().getLeftData().length];
		for (int i = 0; i < fs.length; i++)
		{
			fs[i] = (TableField) getGroupPanel().getLeftData()[i];
		}
		return fs;
	}

	public boolean isSum()
	{
		return getSumChooser().isSelected();
	}

	public boolean isSub()
	{
		return getSubChooser().isSelected();
	}

	public boolean isLevelCompute()
	{
		return getLevelChooser().isSelected();
	}


	@Override
	protected void complete() throws Exception {
	}
}
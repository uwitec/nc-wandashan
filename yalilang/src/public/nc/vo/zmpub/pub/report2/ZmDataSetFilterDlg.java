/*
 * 创建日期 2005-5-10
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.vo.zmpub.pub.report2;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.beans.layout.SpringUtilities;
import nc.ui.trade.report.cross.ListItemWrapperObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.report.TableField;

import com.borland.jbcl.layout.VerticalFlowLayout;

/**
 * @author mlr 支持数据交叉后的  数据过滤
 */
public class ZmDataSetFilterDlg extends UIDialog
{

	public UITextArea resultTextArea; //放置结果

	public UIList resultList;

	public UIComboBox comboBoxValue;

	public UIComboBox comboBoxCondition;

	public UIComboBox comboBoxFilter;

	public TableField[] modelVOs = null;

	public CircularlyAccessibleValueObject[] bodyDataVOs = null;

	//public UIRadioButton filterInOld_Y;

	//public UIRadioButton filterInOld_N;

	//public UICheckBox checkBoxIsOri;

	public DataSetFilterDlgEventHandler allEventHandler;

	public UIButton moveBt;

	public UIButton okBt;

	public UIButton cancelBt;

	public UIButton delBt;

	public UIRadioButton andBt;

	public UIRadioButton orBt;

	public UIPanel panelOkCancel;

	//最终结果
	public String fomula;

	public UIPanel panelTop;

	public UIPanel panelCombobox;

	public UIPanel panelButton;

	public UIPanel panelList;

	//public UIPanel panelSelect;

	public UIPanel panelResult;

	public UIPanel panelButtonBottom;

	//public UIPanel panelRadio;

	public UIPanel panelJoin;

	public UIPanel panelLevelOne;

	public UIPanel panelLevelTwo;

	public UIButton buttonNormal; //普通视图

	public UIButton buttonAdvanced; //高级过滤视图

	//下面是高级视图中的控件
	public UIPanel panelList_2;

	public UIList listFilterItem;

	public UITree treeFunction;

	public UIList listCondition;

	public UITextArea resultTextArea_2;

	//public UIPanel ivjPnColumns;

	public UIPanel panelButtonBottom_2;

	public UIPanel panelOkCancel_2;

	public UIButton okBt_2;

	public UIButton cancelBt_2;

	public boolean isNormalState = true; //记录状态

	public ZmDataSetFilterDlg(Container panret,TableField[] modelVOs,
			final CircularlyAccessibleValueObject[] bodyDataVOs)
	{
		super(panret);
		setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000088")/*@res "条件过滤"*/);
		this.modelVOs = modelVOs;
		this.bodyDataVOs = bodyDataVOs;
		allEventHandler = new DataSetFilterDlgEventHandler();
		this.setSize(480, 330);
		initLayout();

		initData();

	}

	/**
	 *
	 */
	public void initData()
	{

		if (modelVOs == null || modelVOs.length == 0)
		{
			return;
		}
		initComboBoxFilter();
		initListFilter();
		initComboBoxCondition();

	}

	/**
	 *
	 */
	public void initListFilter()
	{
		listFilterItem.setModel(new DefaultListModel());
		for (int i = 0; i < modelVOs.length; i++)
		{
				ListItemWrapperObject value = new ListItemWrapperObject();
				value.setShowName(modelVOs[i].getFieldShowName());
				value.setTrueName(convertVOFieldNameToReportModelFieldName(modelVOs[i].getFieldName()));
				((DefaultListModel) listFilterItem.getModel())
						.addElement(value);
		}
	}

	/**
	 *
	 */
	public void initComboBoxCondition()
	{
		String[] str = { "=", ">", "<", "<>", ">=", "<=" };
		for (int i = 0; i < str.length; i++)
		{
			comboBoxCondition.addItem(str[i]);
		}
	}

	/**
	 *
	 */
	public void initComboBoxFilter()
	{
		for (int i = 0; i < modelVOs.length; i++)
		{
				ListItemWrapperObject value = new ListItemWrapperObject();
				value.setShowName(modelVOs[i].getFieldShowName());
				value.setTrueName(convertVOFieldNameToReportModelFieldName(modelVOs[i].getFieldName()));
				comboBoxFilter.addItem(value);
		}
		if (comboBoxFilter.getModel().getSize() > 0)
		{
			comboBoxFilter.setSelectedIndex(0);
			fillComboBoxValue();
		}

	}

	/**
	 *
	 */
	public void initLayout()
	{

		getContentPane().setLayout(new CardLayout());
		getContentPane().add("one", getPanelLevelOne());
		getContentPane().add("two", getPanelLevelTwo());
		((CardLayout) getContentPane().getLayout()).show(getContentPane(),
				"one");
		//getContentPane().add(getPanelTop());
		//		//getContentPane().add(getPanelSelect());
		//		getContentPane().add(getPanelResult());
		//		getContentPane().add(getPanelButtonBottom());
		//		SpringUtilities.makeCompactGrid(getContentPane(), 4,
		//                1,
		//                6, 6, 6, 6);

	}
	public String convertVOFieldNameToReportModelFieldName(String voFieldName)
	{
		if (voFieldName.indexOf('.') == -1)
			return voFieldName;
		else
			return voFieldName.substring(0, voFieldName.indexOf('.'))
					+ "_"
					+ voFieldName.substring(voFieldName.indexOf('.') + 1,
							voFieldName.length());
	}
	public UIPanel getPanelLevelOne()
	{
		if (panelLevelOne == null)
		{
			panelLevelOne = new UIPanel();
			//			panelLevelOne.setLayout(
			//					new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
			panelLevelOne.add(getPanelTop());
			//getContentPane().add(getPanelSelect());
			panelLevelOne.add(getPanelResult());
			panelLevelOne.add(getPanelButtonBottom());
		}
		return panelLevelOne;
	}

	public UIPanel getPanelLevelTwo()
	{
		if (panelLevelTwo == null)
		{
			panelLevelTwo = new UIPanel();
			panelLevelTwo = new nc.ui.pub.beans.UIPanel();
			//			panelLevelTwo.setLayout(new BorderLayout());
			//			panelLevelTwo.add(getPnAdvNorth(), "North");
			//panelLevelTwo.add(getPnAdvCenter(), "Center");
			panelLevelTwo.add(getPanelList_2());
			resultTextArea_2 = new UITextArea();
			resultTextArea_2.setPreferredSize(new Dimension(380, 93));
			resultTextArea_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			panelLevelTwo.add(resultTextArea_2);
			panelLevelTwo.add(getPanelButtonBottom_2());
			//			panelLevelTwo.add(getUIPanel3(), "West");
			//			panelLevelTwo.add(getUIPanel4(), "East");
			//			panelLevelTwo.add(getUIPanel5(), "South");
			// user code begin {1}
		}
		return panelLevelTwo;
	}

	/**
	 * @return
	 */
	public UIPanel getPanelButtonBottom_2()
	{
		if (panelButtonBottom_2 == null)
		{
			panelButtonBottom_2 = new UIPanel();
			panelButtonBottom_2.add(getPanelOkCancel_2());

			buttonNormal = new UIButton();
			buttonNormal.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000089")/*@res "普通过滤"*/);
			panelButtonBottom_2.add(buttonNormal);
			buttonNormal.addActionListener(allEventHandler);
		}
		return panelButtonBottom_2;
	}

	public UIPanel getPanelList_2()
	{
		if (panelList_2 == null)
		{
			panelList_2 = new UIPanel();
			listCondition = new nc.ui.pub.beans.UIList();
			listCondition.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			UIScrollPane pane1 = new UIScrollPane();
			pane1.setViewportView(listCondition);
			pane1.setPreferredSize(new Dimension(120, 150));
			listCondition.addMouseListener(allEventHandler);

			DefaultTreeSelectionModel localSelectionModel = new DefaultTreeSelectionModel();
			localSelectionModel.setSelectionMode(1);
			treeFunction = new nc.ui.pub.beans.UITree();
			treeFunction.setSelectionModel(localSelectionModel);
			UIScrollPane pane2 = new UIScrollPane();
			pane2.setViewportView(treeFunction);
			pane2.setPreferredSize(new Dimension(120, 150));
			treeFunction.addMouseListener(allEventHandler);

			listFilterItem = new nc.ui.pub.beans.UIList();
			listFilterItem
					.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			UIScrollPane pane3 = new UIScrollPane();
			pane3.setViewportView(listFilterItem);
			pane3.setPreferredSize(new Dimension(120, 150));
			listFilterItem.addMouseListener(allEventHandler);

			panelList_2.add(pane3);
			panelList_2.add(pane1);
			panelList_2.add(pane2);
			initListAndTree();
		}
		return panelList_2;
	}

	/**
	 *
	 */
	public void initListAndTree()
	{
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000283")/* @res "函数" */);
		DefaultMutableTreeNode parent, child;

		//初始化函数树
		parent = new DefaultMutableTreeNode(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("10241201", "UPP10241201-000284")/* @res "数学函数" */);
		child = new DefaultMutableTreeNode("abs()");
		parent.add(child);
		child = new DefaultMutableTreeNode("sgn()");
		parent.add(child);
		//child = new DefaultMutableTreeNode("floor()");
		//parent.add(child);
		//child = new DefaultMutableTreeNode("ceiling()");
		//parent.add(child);
		//child = new DefaultMutableTreeNode("round()");
		//parent.add(child);
		//child = new DefaultMutableTreeNode("square()");
		//parent.add(child);
		child = new DefaultMutableTreeNode("sqrt()");
		parent.add(child);
		child = new DefaultMutableTreeNode("exp()");
		parent.add(child);
		child = new DefaultMutableTreeNode("log()");
		parent.add(child);
		child = new DefaultMutableTreeNode("sin()");
		parent.add(child);
		child = new DefaultMutableTreeNode("cos()");
		parent.add(child);
		child = new DefaultMutableTreeNode("tg()");
		parent.add(child);
		
		// V502
		child = new DefaultMutableTreeNode("zeroifnull()");
		parent.add(child);
		
		root.add(parent);

		parent = new DefaultMutableTreeNode(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("10241201", "UPP10241201-000285")/* @res "文本函数" */);
		child = new DefaultMutableTreeNode("length()");
		parent.add(child);
		child = new DefaultMutableTreeNode("toLowerCase()");
		parent.add(child);
		child = new DefaultMutableTreeNode("toUpperCase()");
		parent.add(child);
		child = new DefaultMutableTreeNode("toNumber()");
		parent.add(child);
		child = new DefaultMutableTreeNode("toString()");
		parent.add(child);
		//child = new DefaultMutableTreeNode("str()"); //zjb
		//parent.add(child); //zjb
		//child = new DefaultMutableTreeNode("format()");
		//parent.add(child);
		root.add(parent);

		treeFunction.setModel(new DefaultTreeModel(root));

		//初始化操作符列表
		DefaultListModel lm = new DefaultListModel();
		//if (m_iMode == 0) {
		lm.addElement("()");
		lm.addElement("+");
		lm.addElement("-");
		lm.addElement("*");
		lm.addElement("/");
		//} else {
		lm.addElement("=");
		lm.addElement(">");
		lm.addElement("<");
		lm.addElement("<=");
		lm.addElement(">=");
		lm.addElement("<>");
		//}
		lm.addElement("!");
		lm.addElement("||");
		lm.addElement("&&");
		listCondition.setModel(lm);
	}

	class DataSetFilterDlgEventHandler implements ActionListener, ItemListener,
			MouseListener
	{

		/*
		 * （非 Javadoc）
		 *
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == moveBt)
			{
				MiddleResult tmp = getMiddleResult();
				addToResultListAndArea(tmp);
			}
			else if (e.getSource() == delBt)
			{
				if (resultList.getSelectedValue() != null)
				{
					Object[] selectObjects = resultList.getSelectedValues();
					if( selectObjects != null ){
						for (int i = 0; i < selectObjects.length; i++) {
							((DefaultListModel) resultList.getModel())
							.removeElement(selectObjects[i]);
						}
					}

					refreshArea();
				}
			}
			else if (e.getSource() == okBt || e.getSource() == okBt_2)
			{
				if (isNormalState)
					setFomula(reCombineResult());
				else
					setFomula(resultTextArea_2.getText());
				closeOK();
			}
			else if (e.getSource() == cancelBt || e.getSource() == cancelBt_2)
			{
				closeCancel();
			}
			else if (e.getSource() == buttonNormal)
			{
				isNormalState = true;
				((CardLayout) getContentPane().getLayout()).show(
						getContentPane(), "one");
			}

			else if (e.getSource() == buttonAdvanced)
			{
				isNormalState = false;
				((CardLayout) getContentPane().getLayout()).show(
						getContentPane(), "two");
			}

		}

		/*
		 * （非 Javadoc）
		 *
		 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
		 */
		public void itemStateChanged(ItemEvent e)
		{
			if (e.getSource() == comboBoxFilter
					&& e.getStateChange() == ItemEvent.SELECTED)
			{
				fillComboBoxValue();
			}

		}

		/*
		 * （非 Javadoc）
		 *
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		public void mouseClicked(MouseEvent e)
		{
			if (e.getClickCount() == 2)
			{
				if (e.getSource() == treeFunction)
				{
					TreePath selPath = treeFunction.getSelectionPath();
					if (selPath != null)
					{
						TreeNode selNode = (TreeNode) selPath
								.getLastPathComponent();
						if (selNode.isLeaf())
						{
							String fun = selNode.toString();
							int pos = resultTextArea_2.getSelectionStart();
							resultTextArea_2.insert(fun, pos);
							resultTextArea_2.setSelectionStart(pos
									+ fun.length() - 1);
							resultTextArea_2.setSelectionEnd(pos + fun.length()
									- 1);
							resultTextArea_2.requestFocus();
						}
					}
				}
				else if (e.getSource() == listFilterItem)
				{
					if (listFilterItem.getSelectedValue() != null)
					{
						String name = ((ListItemWrapperObject) listFilterItem
								.getSelectedValue()).getTrueName();
						int pos = resultTextArea_2.getSelectionStart();
						resultTextArea_2.insert(name, pos);
						resultTextArea_2.requestFocus();
						resultTextArea_2.setSelectionStart(pos + name.length());
						resultTextArea_2.setSelectionEnd(pos + name.length());
					}
				}
				else if (e.getSource() == listCondition)
				{
					if (listFilterItem.getSelectedValue() != null)
					{
						String str = listCondition.getSelectedValue()
								.toString();
						int pos = resultTextArea_2.getSelectionStart();
						resultTextArea_2.insert(str, pos);
						int posmove = str.length();
						if (str.equals("()"))
						{
							posmove -= 1;
						}
						resultTextArea_2.requestFocus();
						resultTextArea_2.setSelectionStart(pos + posmove);
						resultTextArea_2.setSelectionEnd(pos + posmove);
					}
				}
			}
		}

		/*
		 * （非 Javadoc）
		 *
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		public void mouseEntered(MouseEvent e)
		{
		}

		/*
		 * （非 Javadoc）
		 *
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		public void mouseExited(MouseEvent e)
		{
		}

		/*
		 * （非 Javadoc）
		 *
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		public void mousePressed(MouseEvent e)
		{
		}

		/*
		 * （非 Javadoc）
		 *
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		public void mouseReleased(MouseEvent e)
		{
		}

	}

	/**
	 * 根据所选过滤项添加comboboxvalue值
	 */
	public void fillComboBoxValue()
	{
		if (comboBoxFilter.getSelectedItem() != null)
		{
			Set ls = getValueByFilterItem(comboBoxFilter.getSelectedItem());
			comboBoxValue.removeAllItems();
			if (ls != null)
			{
				Iterator it = ls.iterator();
				while (it.hasNext())
				{
					comboBoxValue.addItem(it.next());
				}
			}

		}

	}

	/**
	 * 按照传入的列取得所有列数据
	 *
	 * @param selectedItem
	 */
	public Set getValueByFilterItem(Object selectedItem)
	{
		ListItemWrapperObject item = (ListItemWrapperObject) selectedItem;
		//		int colIndex = m_report.getBodyColByKey(item.getTrueName());
		//		Vector vec = m_report.getBillModel().getDataVector();
		if (bodyDataVOs == null || bodyDataVOs.length == 0)
			return null;
		Set al = new HashSet();
		for (int i = 0; i < bodyDataVOs.length; i++)
		{
			al.add(bodyDataVOs[i].getAttributeValue(item.getTrueName()));
		}
		return al;
	}

	public void addToResultListAndArea(MiddleResult result)
	{
		if (result == null)
		{
			return;
		}
		if (isListContainsResult(result))
		{
			return;
		}
		((DefaultListModel) resultList.getModel()).addElement(result);
		refreshArea();
	}

	/**
	 * @return
	 */
	public boolean isListContainsResult(MiddleResult result)
	{
		int size = resultList.getModel().getSize();
		for (int i = 0; i < size; i++)
		{
			if (resultList.getModel().getElementAt(i).equals(result))
				return true;
		}
		return false;
	}

	//更新结果框
	public void refreshArea()
	{
		try
		{
			resultTextArea.getDocument().remove(0,
					resultTextArea.getDocument().getLength());
			resultTextArea.getDocument().insertString(0, reCombineResult(),
					null);
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	public String reCombineResult()
	{
		StringBuffer tmpFomula = new StringBuffer();
		int size = resultList.getModel().getSize();
		for (int i = 0; i < size; i++)
		{
			MiddleResult r = (MiddleResult) resultList.getModel().getElementAt(
					i);
			if (i != 0)
			{
				tmpFomula.append(" ");
				tmpFomula.append(r.getJoin());
				tmpFomula.append(" ");
			}
			tmpFomula.append(r.getName().getTrueName());
			tmpFomula.append(r.getCondition());
			if( !r.isNumber() )
				tmpFomula.append("\"");
			tmpFomula.append(r.getValue());
			if( !r.isNumber() )
			tmpFomula.append("\"");

		}
		return tmpFomula.toString();
	}

	/**
	 * @return
	 */
	public MiddleResult getMiddleResult()
	{
		Object filter = comboBoxFilter.getSelectedItem();
		Object condition = comboBoxCondition.getSelectedItem();
		Object value = comboBoxValue.getSelectedItem();
		String join = "&&";
		if (orBt.isSelected())
		{
			join = "||";
		}
		if (filter != null && condition != null && value != null)
		{
			return new MiddleResult((ListItemWrapperObject) filter, condition
					.toString(), value.toString(), join,modelVOs[comboBoxFilter.getSelectedIndex()].isNumber());
		}
		return null;
	}

	//暂存中间结果,放置于resultList中
	class MiddleResult
	{
		public ListItemWrapperObject name;

		public String condition;

		public String value;

		public String join;

		public boolean isNumber = false;

		public MiddleResult(ListItemWrapperObject o_name, String s_condition,
				String s_value, String s_join,boolean isNum)
		{
			name = o_name;
			condition = s_condition;
			value = s_value;
			join = s_join;
			isNumber = isNum;
		}

		public ListItemWrapperObject getName()
		{
			return name;
		}

		public String getValue()
		{
			return value;
		}

		public String getCondition()
		{
			return condition;
		}

		public String getJoin()
		{
			return join;
		}

		public String toString()
		{
			return name + " " + condition + " " + value;
		}

		public boolean equals(Object obj)
		{
			return obj.toString().equals(this.toString());
		}

		public boolean isNumber() {
			return isNumber;
		}

	}

	/**
	 * @return
	 */
	public String getFomula()
	{
		return fomula;
	}

	/**
	 * @param fomula
	 *            要设置的 fomula。
	 */
	public void setFomula(String fomula)
	{
		this.fomula = fomula;
	}

	/**
	 * @return 返回 panelButton。
	 */
	public UIPanel getPanelButton()
	{
		if (panelButton == null)
		{
			panelButton = new UIPanel();
            VerticalFlowLayout vf = new VerticalFlowLayout();
            vf.setAlignment(VerticalFlowLayout.MIDDLE);
            vf.setVgap(15);
            panelButton.setLayout(vf);
			moveBt = new UIButton();
			moveBt.addActionListener(allEventHandler);
			moveBt.setText(">");
            moveBt.setMaximumSize(moveBt.getPreferredSize());
			panelButton.add(moveBt);
			delBt = new UIButton();
			delBt.setText("<");
            delBt.setMaximumSize(delBt.getPreferredSize());
			panelButton.add(delBt);
			delBt.addActionListener(allEventHandler);
		}
		return panelButton;
	}

	public UIPanel getPanelOkCancel()
	{
		if (panelOkCancel == null)
		{
			panelOkCancel = new UIPanel();
			okBt = new UIButton();

			okBt.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000044")/*@res "确定"*/);
			panelOkCancel.add(okBt);
			okBt.addActionListener(allEventHandler);

			cancelBt = new UIButton();
			cancelBt.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000008")/*@res "取消"*/);
			panelOkCancel.add(cancelBt);
			cancelBt.addActionListener(allEventHandler);
		}
		return panelOkCancel;
	}

	public UIPanel getPanelOkCancel_2()
	{
		if (panelOkCancel_2 == null)
		{
			panelOkCancel_2 = new UIPanel();
			okBt_2 = new UIButton();

			okBt_2.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000044")/*@res "确定"*/);
			panelOkCancel_2.add(okBt_2);
			okBt_2.addActionListener(allEventHandler);

			cancelBt_2 = new UIButton();
			cancelBt_2.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000008")/*@res "取消"*/);
			panelOkCancel_2.add(cancelBt_2);
			cancelBt_2.addActionListener(allEventHandler);
		}
		return panelOkCancel_2;
	}

	/**
	 * @return 返回 panelButtonBottom。
	 */
	public UIPanel getPanelButtonBottom()
	{
		if (panelButtonBottom == null)
		{
			panelButtonBottom = new UIPanel();
			panelButtonBottom.add(getPanelOkCancel());

			buttonAdvanced = new UIButton();
			buttonAdvanced.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000090")/*@res "高级过滤"*/);
			panelButtonBottom.add(buttonAdvanced);
			buttonAdvanced.addActionListener(allEventHandler);
		}
		return panelButtonBottom;
	}

	/**
	 * @return 返回 panelCombobox。
	 */
	public UIPanel getPanelCombobox()
	{
		if (panelCombobox == null)
		{
			panelCombobox = new UIPanel();
			panelCombobox.setLayout(new SpringLayout());
			final UILabel labelFilter = new UILabel();
			labelFilter.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000091")/*@res "过滤项"*/);
			panelCombobox.add(labelFilter);

			comboBoxFilter = new UIComboBox();
			panelCombobox.add(comboBoxFilter);
			comboBoxFilter.addItemListener(allEventHandler);

			final UILabel labelCond = new UILabel();
			labelCond.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000092")/*@res "过滤条件"*/);
			panelCombobox.add(labelCond);

			comboBoxCondition = new UIComboBox();
			panelCombobox.add(comboBoxCondition);

			final UILabel labelValue = new UILabel();
			labelValue.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000093")/*@res "过滤值"*/);
			panelCombobox.add(labelValue);

			comboBoxValue = new UIComboBox();
			panelCombobox.add(comboBoxValue);
			comboBoxValue.setEditable(true);
			SpringUtilities.makeCompactGrid(panelCombobox, 3, 2, 6, 6, 6, 3);

		}
		return panelCombobox;
	}

	/**
	 * @return 返回 panelList。
	 */
	public UIPanel getPanelList()
	{
		if (panelList == null)
		{
			panelList = new UIPanel();
			UIScrollPane scrollPane = new UIScrollPane();
			scrollPane.setPreferredSize(new Dimension(110, 140));
			panelList.add(scrollPane);

			resultList = new UIList();
			resultList.setModel(new DefaultListModel());
			scrollPane.setViewportView(resultList);
		}
		return panelList;
	}

	/**
	 * @return 返回 panelResult。
	 */
	public UIPanel getPanelResult()
	{
		if (panelResult == null)
		{
			panelResult = new UIPanel();
			panelResult.setLayout(new BorderLayout());
			Box box = Box.createVerticalBox();
			final JLabel label = new UILabel();
			label.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000094")/*@res "组合公式"*/);
			box.add(label);

			resultTextArea = new UITextArea();
			resultTextArea.setPreferredSize(new Dimension(380, 80));
			resultTextArea.setBackground(this.getBackground());
			resultTextArea.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			resultTextArea.setEditable(false);
			box.add(resultTextArea);

			panelResult.add(box);

		}

		return panelResult;
	}

	/**
	 * @return 返回 panelTop。
	 */
	public UIPanel getPanelTop()
	{
		if (panelTop == null)
		{
			panelTop = new UIPanel();
			//			panelTop.setPreferredSize(new Dimension(430, 150));
			panelTop.setLayout(new SpringLayout());
			Box box = Box.createVerticalBox();
			box.add(getPanelCombobox());
			box.add(getPanelJoin());
			panelTop.add(box);
			panelTop.add(getPanelButton());
			panelTop.add(getPanelList());
			SpringUtilities.makeCompactGrid(panelTop, 1, 3, 6, 6, 16, 0);
		}
		return panelTop;
	}

	public UIPanel getPanelJoin()
	{
		if (panelJoin == null)
		{
			panelJoin = new UIPanel();
			panelJoin.setLayout(new BorderLayout());
			Box box = Box.createVerticalBox();

			ButtonGroup group = new ButtonGroup();
			andBt = new UIRadioButton();
			andBt.setText("and");
			andBt.setSelected(true);
			orBt = new UIRadioButton();
			orBt.setText("or");
			group.add(andBt);
			group.add(orBt);
			box.add(andBt);
			box.add(orBt);
			panelJoin.add(box);
			panelJoin.setBorder(BorderFactory.createTitledBorder(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000095")/*@res "连接条件"*/));
		}
		return panelJoin;
	}
}
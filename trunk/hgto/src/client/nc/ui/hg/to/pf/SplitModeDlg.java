package nc.ui.hg.to.pf;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.ScrollPaneLayout;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.beans.layout.TableLayout;
import nc.vo.to.pub.ConstVO;

public class SplitModeDlg extends UIDialog implements
		java.awt.event.ActionListener {
	private UIPanel m_contentpanel = null;
	private UIPanel m_dataPanel = null;
	private UIPanel m_btnPanel = null;
	private UICheckBox m_chkBillCode = null;// 调入申请单号
	private UICheckBox m_chkBillDate = null;// 按调入申请日期
	private UICheckBox m_chkInDept = null;// 按调入部门
	private UICheckBox m_chkOutDept = null;// 按调出部门
	private UILabel m_lbBillCode = null;// 调入申请单号
	private UILabel m_lbBillDate = null;// 按调入申请日期
	private UILabel m_lbInDept = null;// 按调入部门
	private UILabel m_lbOutDept = null;// 按调出部门
	private UITextArea m_textNote = null;
	private UIScrollPane m_textScroll = null;
	private UIButton m_btnOK = null;// 确定
	private UIButton m_btnCancel = null;// 取消
	public SplitModeDlg(Container parent) {
		super(parent);
		initialize();
	}

	private void initialize() {
		setName("SplitModeDlg");
		setSize(485, 220);
		setResizable(true);
		setContentPane(getContentpanel());
		setTitle("");
	}

	private UIPanel getContentpanel() {
		if (m_contentpanel == null) {
			m_contentpanel = new UIPanel();
			m_contentpanel.setName("Contentpanel");
			m_contentpanel.setLayout(new BorderLayout());
			m_contentpanel.setAutoscrolls(true);
			m_contentpanel.add(getDataPanel(), "Center");
			m_contentpanel.add(getBtnPanel(), "South");

		}
		return m_contentpanel;
	}

	private UIPanel getDataPanel() {
		if (m_dataPanel == null) {
			m_dataPanel = new UIPanel();
			double b = 20;
			double p = TableLayout.PREFERRED;
			double f = TableLayout.FILL;
			double size[][] = { { b, p, p, f, p, p, b },
					{ b, p, p, b, p, f, b } };
			m_dataPanel.setLayout(new TableLayout(size));
			m_dataPanel.add(getUIChkBoxBillCode(), "1,1");
			m_dataPanel.add(getLableBillCode(), "2,1");
			m_dataPanel.add(getUIChkBoxBillDate(), "4,1");
			m_dataPanel.add(getLabelBillDate(), "5,1");
			m_dataPanel.add(getUIChkBoxInDept(), "1,2");
			m_dataPanel.add(getLabelInDept(), "2,2");
			m_dataPanel.add(getUIChkBoxOutDept(), "4,2");
			m_dataPanel.add(getLabelOutDept(), "5,2");
			m_dataPanel.add(gettextScollPanel(), "1,3,5,6");

		}
		return m_dataPanel;
	}

	private UIPanel getBtnPanel() {
		if (m_btnPanel == null) {
			m_btnPanel = new UIPanel();
			m_btnPanel.setName("BtnPanel");
			m_btnPanel.setLayout(new FlowLayout());
			m_btnPanel.add(getUIBtnOk());
			m_btnPanel.add(getUIBtnCancel());

		}
		return m_btnPanel;
	}

	private UIButton getUIBtnOk() {
		if (m_btnOK == null) {
			m_btnOK = new UIButton();
			m_btnOK.setName("OK");
			m_btnOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000044")/*@res "确定"*/);
			m_btnOK.addActionListener(this);
		}
		return m_btnOK;
	}

	private UIButton getUIBtnCancel() {
		if (m_btnCancel == null) {
			m_btnCancel = new UIButton();
			m_btnCancel.setName("Cancel");
			m_btnCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000008")/*@res "取消"*/);
			m_btnCancel.addActionListener(this);
		}
		return m_btnCancel;
	}

	private UICheckBox getUIChkBoxBillCode() {
		if (m_chkBillCode == null) {
			m_chkBillCode = new UICheckBox();
			m_chkBillCode.setSize(10, 10);
			m_chkBillCode.setPreferredSize(new Dimension(20, 20));
			m_chkBillCode.setName("UIChkBoxBillCode");
			m_chkBillCode.setSelected(false);
		}
		return m_chkBillCode;
	}

	public void actionPerformed(java.awt.event.ActionEvent e) {
		if (e.getSource() == this.getUIBtnCancel()) {
			this.closeCancel();
		} else if (e.getSource() == this.getUIBtnOk()) {
			this.closeOK();
		}
	}

	/**
	 * 返回结果
	 *
	 * @return
	 */
	public String getReturnValue() {
		StringBuffer sbValue = new StringBuffer();
		if (getUIChkBoxBillCode().isSelected()) {
			sbValue.append(ConstVO.sbillcode);
		}
		if (getUIChkBoxBillDate().isSelected()) {
			sbValue.append(ConstVO.sBilldate);
		}
		if (getUIChkBoxInDept().isSelected()) {
			sbValue.append(ConstVO.sInDept);
		}
		if (getUIChkBoxOutDept().isSelected()) {
			sbValue.append(ConstVO.sOutDept);
		}
		return sbValue.toString();
	}

	public void setBillCodeSelected(boolean bselected) {
		getUIChkBoxBillCode().setSelected(bselected);
	}

	public void setBillDateSelected(boolean bselected) {
		getUIChkBoxBillDate().setSelected(bselected);
	}

	public void setIndeptSelected(boolean bselected) {
		getUIChkBoxInDept().setSelected(bselected);
	}

	public void setOutDeptSelected(boolean bselected) {
		getUIChkBoxOutDept().setSelected(bselected);
	}

	private UICheckBox getUIChkBoxBillDate() {
		if (m_chkBillDate == null) {
			m_chkBillDate = new UICheckBox();
			// m_chkBillDate.setBounds(x, y, width, height)
			m_chkBillDate.setName("UIChkBoxBillDate");
			m_chkBillDate.setPreferredSize(new Dimension(20, 20));
			m_chkBillDate.setSelected(false);
		}
		return m_chkBillDate;
	}

	private UICheckBox getUIChkBoxInDept() {
		if (m_chkInDept == null) {
			m_chkInDept = new UICheckBox();
			m_chkInDept.setName("UIChkBoxInDept");
			m_chkInDept.setPreferredSize(new Dimension(20, 20));
			m_chkInDept.setSelected(false);
		}
		return m_chkInDept;
	}

	private UICheckBox getUIChkBoxOutDept() {
		if (m_chkOutDept == null) {
			m_chkOutDept = new UICheckBox();
			m_chkOutDept.setName("UIChkBoxOutDept");
			m_chkOutDept.setPreferredSize(new Dimension(20, 20));
			m_chkOutDept.setSelected(false);
		}
		return m_chkOutDept;
	}

	private UILabel getLableBillCode() {
		if (m_lbBillCode == null) {
			m_lbBillCode = new UILabel();
			m_lbBillCode.setName("LableBillCode");
			m_lbBillCode.setText(nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40092030","UPP40092030-000200")/*@res "按调入申请单号"*/);
			// m_lbBillCode.setBounds(39, 233, 101, 22);
		}
		return m_lbBillCode;
	}

	private UILabel getLabelBillDate() {
		if (m_lbBillDate == null) {
			m_lbBillDate = new UILabel();
			m_lbBillDate.setName("LabelBillDate");
			m_lbBillDate.setText(nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40092030","UPP40092030-000201")/*@res "按调入申请日期"*/);
			// m_lbBillDate.setBounds(39, 233, 101, 22);
		}
		return m_lbBillDate;
	}

	private UIScrollPane gettextScollPanel() {
		if (m_textScroll == null) {
			m_textScroll = new UIScrollPane();
			m_textScroll.setAutoscrolls(true);
			m_textScroll.setLayout(new ScrollPaneLayout());
			m_textScroll.setViewportView(gettextNote());
			// m_textScroll.setEnabled(false)
		}
		return m_textScroll;
	}

	private UITextArea gettextNote() {
		if (m_textNote == null) {
			m_textNote = new UITextArea();
			m_textNote.setName("Note");
			m_textNote.setAutoscrolls(true);
			m_textNote.setEditable(false);
			m_textNote.setLayout(new FlowLayout());
			String stext = nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40092030","UPP40092030-000202")/*@res "公司间/组织间/三方调拨订单有固定的分单项：\n"*/+"\n"
					+ nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40092030","UPP40092030-000203")/*@res "调出公司+调出组织+调入公司+调入组织+出货公司+出货组织+调拨类型+业务类型\n"*/+"\n"
					+ nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40092030","UPP40092030-000204")/*@res "组织内调拨订单有固定的分单项：\n"*/+"\n"
					+ nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40092030","UPP40092030-000205")/*@res "调出公司+调出组织+调出仓库+调入公司+调入组织+调入仓库+调拨类型+业务类型"*/;
			m_textNote.setText(stext);

		}
		return m_textNote;
	}

	private UILabel getLabelInDept() {
		if (m_lbInDept == null) {
			m_lbInDept = new UILabel();
			m_lbInDept.setName("LabelInDept");
			m_lbInDept.setText(nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40092030","UPP40092030-000206")/*@res "按调入部门"*/);
			// m_lbInDept.setBounds(39, 233, 101, 22);
		}
		return m_lbInDept;
	}

	private UILabel getLabelOutDept() {
		if (m_lbOutDept == null) {
			m_lbOutDept = new UILabel();
			m_lbOutDept.setName("LabelOutDept");
			m_lbOutDept.setText(nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40092030","UPP40092030-000207")/*@res "按调出部门"*/);
			// m_lbOutDept.setBounds(39, 233, 101, 22);
		}
		return m_lbOutDept;
	}

}
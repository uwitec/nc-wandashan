package nc.ui.wl.pub.report;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.trade.report.query.QueryDLG;
import nc.vo.pub.query.ConditionVO;
import nc.vo.trade.field.BillField;

/**
 * 公共查询对话框。
 */
public class HYPubQueryDlg extends QueryDLG {

	public HYPubQueryDlg() {
		super();
	}

	public HYPubQueryDlg(java.awt.Container parent) {
		super(parent);
	}

	public HYPubQueryDlg(java.awt.Container parent, String title) {
		super(parent, title);
	}

	public HYPubQueryDlg(java.awt.Frame parent) {
		super(parent);
	}

	public HYPubQueryDlg(java.awt.Frame parent, String title) {
		super(parent, title);
	}

	public HYPubQueryDlg(boolean isFixedSet) {
		super(isFixedSet);
	}

	/**
	 * 在查询模板中出现的主表名（或别名）。
	 * @return
	 * twh (2006-6-14 上午10:58:55)<br>
	 */
	protected String getHeadTable() {
		return null;
	}

	/**
	 * 单据状态列名
	 * @return
	 * twh (2006-6-14 上午11:01:28)<br>
	 */
	protected String getBillStatusFieldName() {
		return BillField.getInstance().getField_BillStatus();
	}

	/**
	 * 获得由ConditionVO[]组合成的where子句
	 */
	public String getWhereSQL(ConditionVO[] conditions) {
		return super.getWhereSQL(conditions);
	}

	protected String getPkCorp() {
		return ClientEnvironment.getInstance().getCorporation().getPk_corp();
	}

	/**
	 * 根据fieldcode返回取值处的参照或固定选项对象(不包括选择的结果).
	 */
	public Object getValueRefObjectByFieldCode(String fieldcode) {
		Object o = super.getValueRefObjectByFieldCode(fieldcode);
		if (o == null) {
			return new UIRefPane();
		}
		return o;
	}
}

package nc.ui.wl.pub;

import java.awt.Component;
import java.awt.Container;

import javax.swing.table.TableCellEditor;

import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.trade.query.HYQueryDLG;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubTool;

public class WdsQueryDlg extends HYQueryDLG {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String cwhid = null;// 仓库
	protected String ccargdoc = null;// 货位
	protected String cwh_fieldname;// 仓库字段名称
	protected String ccarg_fieldname;// 货位字段名称

	public WdsQueryDlg(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType,
			String whfield, String cargfield) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType);
		cwh_fieldname = whfield;
		ccarg_fieldname = cargfield;
		LoginInforHelper login = new LoginInforHelper();
		try {
			cwhid = login.getCwhid(operator);
			ccargdoc = login.getLogInfor(operator).getSpaceid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cwhid = null;
			ccargdoc = null;
		}
	}

	private void init() {
		//  add by  zhw 第一次加载报空指针异常
		if (PuPubVO.getString_TrimZeroLenAsNull(cwhid) != null
				&& PuPubVO.getString_TrimZeroLenAsNull(cwh_fieldname) != null) {
			if (!WdsWlPubTool.isZc(cwhid)) {
				setConditionEditable(cwh_fieldname, false);
				//getComponent(cwh_fieldname).setEnabled(false);
			}
			if (PuPubVO.getString_TrimZeroLenAsNull(ccargdoc) != null
					&& PuPubVO.getString_TrimZeroLenAsNull(ccargdoc) != null) {
				try {
					Object o = getValueRefObjectByFieldCode(ccarg_fieldname);
					if (o == null)
						return;
					if (o instanceof UIRefPane) {
						UIRefPane ref = (UIRefPane) o;
						ref.getRefModel().addWherePart(" and bd_cargdoc.pk_stordoc = '" + cwhid
											+ "'");
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void initData() {
		if (PuPubVO.getString_TrimZeroLenAsNull(cwhid) != null
				&& PuPubVO.getString_TrimZeroLenAsNull(cwh_fieldname) != null) {
			setDefaultValue(cwh_fieldname, cwhid, cwhid);
			if (PuPubVO.getString_TrimZeroLenAsNull(ccargdoc) != null
					&& PuPubVO.getString_TrimZeroLenAsNull(ccargdoc) != null) {
				setDefaultValue(ccarg_fieldname, ccargdoc, ccargdoc);
			}
		}
		super.initData();
		init();
	}

	protected void afterEdit(TableCellEditor editor, int row, int col) {
		super.afterEdit(editor, row, col);
		String fieldcode = getFieldCodeByRow(row);
		if (PuPubVO.getString_TrimZeroLenAsNull(fieldcode) == null)
			return;
		if (fieldcode.equalsIgnoreCase(cwh_fieldname)) {
			ConditionVO[] cons = getConditionVOsByFieldCode(fieldcode);
			if (cons == null || cons.length == 0)
				return;
			String value = PuPubVO.getString_TrimZeroLenAsNull(cons[0]
					.getRefResult().getRefPK());
			if (PuPubVO.getString_TrimZeroLenAsNull(value) == null)
				return;
			Object o = getValueRefObjectByFieldCode(ccarg_fieldname);
			if (o == null)
				return;
			if (o instanceof UIRefPane) {
				UIRefPane ref = (UIRefPane) o;
				ref.getRefModel().addWherePart(
						" and bd_cargdoc.pk_stordoc = '" + value + "'");
			}
		}
	}

	protected Component getComponent(String filedcode) {
		Object o = getValueRefObjectByFieldCode(filedcode);
		Component jb = null;
		if (o instanceof UIRefCellEditor) {
			jb = ((UIRefCellEditor) o).getComponent();// getUITabInput().getCellEditor(1,
														// 4);
		} else {
			jb = (Component) o;
		}

		return jb;
	}

}

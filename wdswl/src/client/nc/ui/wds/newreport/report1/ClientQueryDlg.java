package nc.ui.wds.newreport.report1;
import java.awt.Component;

import javax.swing.table.TableCellEditor;

import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.trade.report.query.QueryDLG;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zmpub.pub.report2.ZmReportBaseQueryDlg;
/**
 * 库存报表查询模版
 * @author mlr
 */
public class ClientQueryDlg extends ZmReportBaseQueryDlg{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String cwhid = null;// 仓库
	protected String ccargdoc = null;// 货位
	protected String cwh_fieldname;// 仓库字段名称
	protected String ccarg_fieldname;// 货位字段名称

	public ClientQueryDlg(java.awt.Container parent,String nodecode,String storname,String cargname) {
		super(parent,nodecode);
		cwh_fieldname=storname;
		ccarg_fieldname = cargname;
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

	

}

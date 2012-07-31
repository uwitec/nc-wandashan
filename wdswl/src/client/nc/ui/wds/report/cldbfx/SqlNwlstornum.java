package nc.ui.wds.report.cldbfx;

import nc.ui.trade.report.query.QueryDLG;

/**
 * 物流库存数量
 * 
 * @author Administrator
 * 
 */
/*
 * select h.pk_corp, h.pk_customize1, h.pk_cargdoc, h.pk_invmandoc,
 * sum(coalesce(h.whs_stocktonnage, 0.0)) from tb_warehousestock h group by
 * h.pk_corp, h.pk_customize1, h.pk_cargdoc, h.pk_invmandoc having
 * sum(coalesce(h.whs_stocktonnage, 0.0)) > 0
 */
public class SqlNwlstornum extends SqlQueryConditionVO {

	public SqlNwlstornum(QueryDLG queryDlg) {
		this.queryDlg = queryDlg;
	}

	public String getCargFieldName() {
		return "b.pk_cargdoc";
	}

	public String getCorpFieldName() {
		return "b.pk_corp";
	}

	public String getInvclFieldName() {
		return "invcl.pk_invcl";
	}

	public String getInvmandocFieldName() {
		return "b.pk_invmandoc";
	}

	public String getSql() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql.append(getCorpFieldName() + " " + pk_corp + ", ");
		sql.append(getStorFieldName() + " " + pk_stordoc + ", ");
//		sql.append(getCargFieldName() + " " + pk_cargdoc + ", ");
		sql.append(getInvclFieldName() + " " + pk_invcl + ", ");
		sql.append(getInvmandocFieldName() + " " + pk_invmandoc + ", ");
		sql.append(getInvbasdocFieldName() + " " + pk_invbasdoc + ", ");
		sql.append(getNnum() + " " + nwlstornum + " ");
		sql.append(" from ");
		sql.append(" tb_warehousestock b ");
		sql
				.append(" join bd_invbasdoc bas on b.pk_invbasdoc = bas.pk_invbasdoc and isnull(bas.dr,0) = 0 ");
		sql
				.append(" join bd_invcl invcl on bas.pk_invcl = invcl.pk_invcl and isnull(invcl.dr,0) = 0 ");
		sql.append(" where ");
		sql.append(" isnull(b.dr,0) = 0 ");
		if (getQueryConditon() != null && getQueryConditon().length() > 0) {
			sql.append(" and " + getQueryConditon());
		}
		sql.append(" group by ");
		sql.append(getCorpFieldName() + ", ");
		sql.append(getStorFieldName() + ", ");
		sql.append(getCargFieldName() + ", ");
		sql.append(getInvclFieldName() + ", ");
		sql.append(getInvmandocFieldName() + ", ");
		sql.append(getInvbasdocFieldName() + " ");
		sql.append(" having " + getNnum() + " > 0 ");
		return sql.toString();
	}

	public String getNnum() {
		return "sum(coalesce(b.whs_stocktonnage, 0.0))";
	}

	public String getStorFieldName() {
		return "b.pk_customize1";
	}

	public String getInvbasdocFieldName() {
		return "b.pk_invbasdoc";
	}

}

package nc.ui.wds.report.cldbfx;

import nc.ui.trade.report.query.QueryDLG;

/**
 * erp采购入库数量
 * @author Administrator
 *
 */
public class SqlNerpcgrknum extends SqlQueryConditionVO {

	public SqlNerpcgrknum(QueryDLG queryDlg) {
		this.queryDlg = queryDlg;
	}

	public String getCargFieldName() {
		return "";
	}

	public String getCorpFieldName() {
		return null;
	}

	public String getInvclFieldName() {
		return null;
	}

	public String getInvmandocFieldName() {
		return null;
	}

	public String getSql() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql.append(getStorFieldName() + " " + pk_stordoc + ", ");
		sql.append(getCargFieldName() + " " + pk_cargdoc + ", ");
		sql.append(getInvclFieldName() + " " + pk_invcl + ", ");
		sql.append(getInvmandocFieldName() + " " + pk_invmandoc + ", ");
		sql.append(getNnum() + " " + nerpstornum + " ");
		sql.append(" from ");
		sql.append("");
		sql.append(" where ");
		sql.append(" 1=1 ");
		if (getQueryConditon() != null && getQueryConditon().length() > 0) {
			sql.append(" and " + getQueryConditon());
		}
		sql.append("");
		return sql.toString();
	}

	public String getNnum() {
		return null;
	}

	public String getStorFieldName() {
		return null;
	}

	public String getInvbasdocFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

}

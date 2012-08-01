package nc.ui.wds.report.cldbfx;

import nc.bs.ic.pub.bill.GeneralSqlString;
import nc.ui.trade.report.query.QueryDLG;

/**
 * erp¿â´æÊýÁ¿
 * 
 * @author Administrator
 * 
 */
/*
 * 
 * select h.nonhandnum, h.pk_onhandnum, b.nnum, b.cspaceid, b.pk_corp,
 * b.cwarehouseidb, b.cinventoryidb, b.cinvbasid, invcl.invclasscode from
 * ic_onhandnum h join ic_onhandnum_b b on h.pk_onhandnum = b.pk_onhandnum join
 * bd_invbasdoc bas on h.cinvbasid = bas.pk_invbasdoc join bd_invcl invcl on
 * bas.pk_invcl = invcl.pk_invcl where invcl.invclasscode like '301010202' and
 * h.pk_corp = '1021' -- and h.cwarehouseid = '1021A91000000004UX5N' and
 * bas.invcode = '3010002'
 */
public class SqlNerpstornum extends SqlQueryConditionVO {

	public SqlNerpstornum(QueryDLG queryDlg) {
		this.queryDlg = queryDlg;
	}

	public String getCargFieldName() {
		return "b.cspaceid";
	}

	public String getCorpFieldName() {
		return "b.pk_corp";
	}

	public String getInvclFieldName() {
		return "invcl.pk_invcl";
	}

	public String getInvmandocFieldName() {
		return "b.cinventoryidb";
	}

	// SELECT SUM(COALESCE(v1.nonhandnum,0.0)) from
	// "+GeneralSqlString.V_IC_ONHANDNUM+" v1 where v1.cinventoryid=?
	public String getSql() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql.append(getCorpFieldName() + " " + pk_corp + ", ");
		sql.append(getStorFieldName() + " " + pk_stordoc + ", ");
//		sql.append(getCargFieldName() + " " + pk_cargdoc + ", ");
		sql.append(getInvclFieldName() + " " + pk_invcl + ", ");
		sql.append(getInvmandocFieldName() + " " + pk_invmandoc + ", ");
		sql.append(getInvbasdocFieldName() + " " + pk_invbasdoc + ", ");
		sql.append(getNnum() + " " + nerpstornum + " ");
		sql.append(" from ");
		sql.append(GeneralSqlString.V_IC_ONHANDNUM_B + " b ");
		sql
				.append(" join bd_invbasdoc bas on b.cinvbasid = bas.pk_invbasdoc and isnull(bas.dr,0) = 0 ");
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
//		sql.append(getCargFieldName() + ", ");
		sql.append(getInvclFieldName() + ", ");
		sql.append(getInvmandocFieldName() + ", ");
		sql.append(getInvbasdocFieldName() + " ");
		sql.append(" having " + getNnum() + " > 0 ");
		return sql.toString();
	}

	public String getNnum() {
		return "SUM(COALESCE(b.nnum,0.0))";
	}

	public String getStorFieldName() {
		return "b.cwarehouseidb";
	}

	public String getInvbasdocFieldName() {
		// TODO Auto-generated method stub
		return "b.cinvbasid";
	}

}

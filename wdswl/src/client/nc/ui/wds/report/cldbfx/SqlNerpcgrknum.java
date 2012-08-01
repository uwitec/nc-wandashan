package nc.ui.wds.report.cldbfx;

import nc.ui.trade.report.query.QueryDLG;
import nc.vo.scm.constant.ScmConst;

/**
 * erp采购入库数量
 * 
 * @author Administrator
 * 
 */
/*
 * select h.pk_corp, h.cwarehouseid, b.cinventoryid, b.cinvbasid,
 * sum(coalesce(b.ninnum, 0.0)) from ic_general_h h join ic_general_b b on
 * h.cgeneralhid = b.cgeneralhid and nvl(b.dr, 0) = 0
 * 
 * where nvl(h.dr, 0) = 0
 * 
 * and h.cbilltypecode = '45' and h.pk_corp = '1021' group by h.pk_corp,
 * h.cwarehouseid, b.cinventoryid, b.cinvbasid
 */
public class SqlNerpcgrknum extends SqlQueryConditionVO {

	public SqlNerpcgrknum(QueryDLG queryDlg) {
		this.queryDlg = queryDlg;
	}

	public String getCargFieldName() {
		return "";
	}

	public String getCorpFieldName() {
		return "h.pk_corp";
	}

	public String getInvclFieldName() {
		return "invcl.pk_invcl";
	}

	public String getInvmandocFieldName() {
		return "b.cinventoryid";
	}

	public String getSql() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql.append(getCorpFieldName() + " " + pk_corp + ", ");
		sql.append(getStorFieldName() + " " + pk_stordoc + ", ");
		// sql.append(getCargFieldName() + " " + pk_cargdoc + ", ");
		sql.append(getInvclFieldName() + " " + pk_invcl + ", ");
		sql.append(getInvmandocFieldName() + " " + pk_invmandoc + ", ");
		sql.append(getInvbasdocFieldName() + " " + pk_invbasdoc + ", ");
		sql.append(getNnum() + " " + nerpcgrknum + " ");
		sql.append(" from ");
		sql.append(" ic_general_h h ");
		sql
				.append(" join ic_general_b b on h.cgeneralhid = b.cgeneralhid and nvl(b.dr, 0) = 0 ");
		sql
				.append(" join bd_invbasdoc bas on b.cinvbasid = bas.pk_invbasdoc and isnull(bas.dr,0) = 0 ");
		sql
				.append(" join bd_invcl invcl on bas.pk_invcl = invcl.pk_invcl and isnull(invcl.dr,0) = 0 ");
		sql.append(" where ");
		sql.append(" nvl(h.dr, 0) = 0 ");
		sql.append(" and h.cbilltypecode = '" + ScmConst.m_purchaseIn + "' ");
		if (getQueryConditon() != null && getQueryConditon().length() > 0) {
			sql.append(" and " + getQueryConditon());
		}
		sql.append(" group by ");
		sql.append(getCorpFieldName() + ", ");
		sql.append(getStorFieldName() + ", ");
		// sql.append(getCargFieldName() + ", ");
		sql.append(getInvclFieldName() + ", ");
		sql.append(getInvmandocFieldName() + ", ");
		sql.append(getInvbasdocFieldName() + " ");
		sql.append(" having " + getNnum() + " > 0 ");
		return sql.toString();
	}

	public String getNnum() {
		return "sum(coalesce(b.ninnum, 0.0))";
	}

	public String getStorFieldName() {
		return "h.cwarehouseid";
	}

	public String getInvbasdocFieldName() {
		return "b.cinvbasid";
	}

}

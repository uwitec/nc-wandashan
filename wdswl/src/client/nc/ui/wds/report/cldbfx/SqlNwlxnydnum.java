package nc.ui.wds.report.cldbfx;

import nc.ui.trade.report.query.QueryDLG;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 物流虚拟运单剩余量 发运计划
 * 
 * @author Administrator
 * 
 */
/*
 * select h.pk_corp, h.pk_inwhouse, invcl.pk_invcl, b.pk_invmandoc,
 * b.pk_invbasdoc, sum(coalesce(b.nplannum, 0.0) - coalesce(b.ndealnum, 0.0))
 * from wds_sendplanin h join wds_sendplanin_b b on h.pk_sendplanin =
 * b.pk_sendplanin join bd_invbasdoc bas on b.pk_invbasdoc = bas.pk_invbasdoc
 * join bd_invcl invcl on invcl.pk_invcl = bas.pk_invcl where nvl(h.dr, 0) = 0
 * and h.pk_billtype = 'WDS1' and h.reserve16 = 'Y'
 * 
 * group by h.pk_corp, h.pk_inwhouse, invcl.pk_invcl, b.pk_invmandoc,
 * b.pk_invbasdoc having sum(coalesce(b.nplannum, 0.0) - coalesce(b.ndealnum,
 * 0.0)) > 0
 */
public class SqlNwlxnydnum extends SqlQueryConditionVO {

	public SqlNwlxnydnum(QueryDLG queryDlg) {
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
		return "b.pk_invmandoc";
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
		sql.append(getNnum() + " " + nerpstornum + " ");
		sql.append(" from ");
		sql
				.append(" wds_sendplanin h join wds_sendplanin_b b on h.pk_sendplanin =  b.pk_sendplanin and isnull(b.dr,0) = 0 ");
		sql
				.append(" join bd_invbasdoc bas on b.pk_invbasdoc = bas.pk_invbasdoc and isnull(bas.dr,0) = 0 ");
		sql
				.append(" join bd_invcl invcl on bas.pk_invcl = invcl.pk_invcl and isnull(invcl.dr,0) = 0 ");

		sql.append(" where ");
		sql.append(" nvl(h.dr, 0) = 0 ");
		sql.append(" and h.pk_billtype = '" + WdsWlPubConst.WDS1 + "' ");
		sql.append(" and h." + WdsWlPubConst.dmplan_xn + " = 'Y' ");
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

	public String getStorFieldName() {
		return "h.pk_inwhouse";
	}

	public String getInvbasdocFieldName() {
		// TODO Auto-generated method stub
		return "b.pk_invbasdoc";
	}

	public String getNnum() {
		// TODO Auto-generated method stub
		return "sum(coalesce(b.nplannum, 0.0) - coalesce(b.ndealnum, 0.0))";
	}

}

package nc.ui.wds.report.cldbfx;

import nc.ui.scm.util.ObjectUtils;
import nc.ui.trade.report.query.QueryDLG;
import nc.vo.pub.query.ConditionVO;

public class SqlNerpstornum implements ISqlforcldbfx {
	private QueryDLG queryDlg = null;

	public SqlNerpstornum(QueryDLG queryDlg) {
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

	public String getQueryConditon() throws Exception {
		if (queryDlg == null) {
			return null;
		}
		ConditionVO[] vos1 = (ConditionVO[]) ObjectUtils
				.serializableClone(queryDlg.getConditionVO());// 获取已被用户填写的查询条件
		if (vos1 == null || vos1.length == 0)
			return null;
		for (int i = 0; i < vos1.length; i++) {

		}
		String sql = queryDlg.getWhereSQL(vos1);

		return sql;
	}

	public String getSql() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql.append(getStorFieldName() + " pk_stordoc, ");
		sql.append(getStorFieldName() + " pk_cargdoc, ");
		sql.append(getStorFieldName() + " pk_invcl, ");
		sql.append(getStorFieldName() + " pk_invmandoc, ");
		sql.append(getNerpstornum() + " nerpstornum ");
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

	private String getNerpstornum() {
		return null;
	}

	public String getStorFieldName() {
		return null;
	}

}

package nc.ui.wds.report.cldbfx;

import nc.ui.trade.report.query.QueryDLG;

/**
 * ���ⶩ��ʣ����
 * erp���۶���
 * nc.vo.wl.pub.Wds2WlPubConst�Ƿ�����
 * @author Administrator
 *
 */
public class SqlNwlxnddnum extends SqlQueryConditionVO {

	public SqlNwlxnddnum(QueryDLG queryDlg) {
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
		sql.append(getNerpstornum() + " " + nerpstornum + " ");
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

	public String getInvbasdocFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getNnum() {
		// TODO Auto-generated method stub
		return null;
	}

}

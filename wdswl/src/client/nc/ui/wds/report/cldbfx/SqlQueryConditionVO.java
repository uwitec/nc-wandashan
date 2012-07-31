package nc.ui.wds.report.cldbfx;

import nc.ui.scm.util.ObjectUtils;
import nc.ui.trade.report.query.QueryDLG;
import nc.vo.pub.query.ConditionVO;

public abstract class SqlQueryConditionVO implements ISqlforcldbfx {
	public QueryDLG queryDlg = null;
	//
	public static String pk_corp = "pk_corp";// ��˾
	public static String pk_stordoc = "pk_stordoc";// �ֿ�
	public static String pk_cargdoc = "pk_cargdoc";// ��λ
	public static String pk_invcl = "pk_invcl";// �������
	public static String pk_invmandoc = "pk_invmandoc";// ���������
	public static String pk_invbasdoc = "pk_invbasdoc";//�����������
	// erp���nerpstornum���������nwlstornum�����ⶩ��ʣ����nwlxnddnum�������˵�ʣ����nwlxnydnum
	public static String nerpstornum = "nerpstornum";//
	public static String nwlstornum = "nwlstornum";
	public static String nwlxnddnum = "nwlxnddnum";
	public static String nwlxnydnum = "nwlxnydnum";
	public static String nerpcgrknum = "nerpcgrknum";

	public String getQueryConditon() throws Exception {

		if (queryDlg == null) {
			return null;
		}
		ConditionVO[] vos1 = (ConditionVO[]) ObjectUtils
				.serializableClone(queryDlg.getConditionVO());// ��ȡ�ѱ��û���д�Ĳ�ѯ����
		if (vos1 == null || vos1.length == 0)
			return null;
		String code = "";
		for (int i = 0; i < vos1.length; i++) {
			code = vos1[i].getFieldCode();
			if (pk_stordoc.equalsIgnoreCase(code)) {
				vos1[i].setFieldCode(getStorFieldName());
			} else if (pk_cargdoc.equalsIgnoreCase(code)) {
				vos1[i].setFieldCode(getCargFieldName());
			} else if (pk_invcl.equalsIgnoreCase(code)) {
				vos1[i].setFieldCode(getInvclFieldName());
			} else if (pk_invmandoc.equalsIgnoreCase(code)) {
				vos1[i].setFieldCode(getInvmandocFieldName());
			} else if (pk_corp.equalsIgnoreCase(code)) {
				vos1[i].setFieldCode(getCorpFieldName());
			}

		}
		String sql = queryDlg.getWhereSQL(vos1);

		return sql;
	}
}

package nc.ui.wds.report.cldbfx;

public interface ISqlforcldbfx {

	/*
	 * ����sql
	 */
	public String getSql() throws Exception;

	/*
	 * ����wheresql
	 */
	public String getQueryConditon() throws Exception;

	/*
	 * ���ع�˾�ֶ�����
	 */
	public String getCorpFieldName();

	/*
	 * ���زֿ��ֶ�����
	 */
	public String getStorFieldName();

	/*
	 * ���ػ�λ�ֶ�����
	 */
	public String getCargFieldName();

	/*
	 * ���ش�������ֶ�����
	 */
	public String getInvclFieldName();

	/*
	 * ���ش������id�ֶ�����
	 */
	public String getInvmandocFieldName();

	/*
	 * ���ش������id�ֶ�����
	 */
	public String getInvbasdocFieldName();

	/*
	 * ������������
	 */
	public String getNnum();
}

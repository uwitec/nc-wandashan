package nc.ui.wds.report.cldbfx;

public interface ISqlforcldbfx {

	/*
	 * 返回sql
	 */
	public String getSql() throws Exception;

	/*
	 * 返回wheresql
	 */
	public String getQueryConditon() throws Exception;

	/*
	 * 返回公司字段名称
	 */
	public String getCorpFieldName();

	/*
	 * 返回仓库字段名称
	 */
	public String getStorFieldName();

	/*
	 * 返回货位字段名称
	 */
	public String getCargFieldName();

	/*
	 * 返回存货分类字段名称
	 */
	public String getInvclFieldName();

	/*
	 * 返回存货管理id字段名称
	 */
	public String getInvmandocFieldName();

	/*
	 * 返回存货基本id字段名称
	 */
	public String getInvbasdocFieldName();

	/*
	 * 返回主数量和
	 */
	public String getNnum();
}

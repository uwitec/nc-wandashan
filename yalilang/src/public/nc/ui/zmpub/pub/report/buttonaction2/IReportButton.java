package nc.ui.zmpub.pub.report.buttonaction2;

/**
 * 记录报表按钮
 * 
 * @author saf 创建日期:2007-8-22下午03:11:12
 */
public class IReportButton {

	public final static Integer QueryBtn = new Integer(0);// 查询

	public final static Integer ColumnFilterBtn = new Integer(1);// 栏目设置

	public final static Integer SortBtn = new Integer(2);// 排序

	public final static Integer SubTotalBtn = new Integer(3);// 小计合计
	public final static Integer LevelSubTotalBtn = new Integer(4);// 分级小计
	public final static Integer CodeLevelSubTotalBtn = new Integer(5);// 编码级次小计

	public final static Integer PrintBtn = new Integer(6);// 打印
	public final static Integer PrintPreviewBtn = new Integer(7);// 打印预览
	public final static Integer PrintDirectBtn = new Integer(8);// 直接打印
	public final static Integer PrintTempletBtn = new Integer(9);// 模板打印

	public final static Integer CrossBtn = new Integer(10); // 交叉设置
	public final static Integer FilterBtn = new Integer(11); // 过滤
	public final static Integer RefreshBtn = new Integer(12); // 刷新
	public final static Integer PenerateBtn = new Integer(13); // 联查
}

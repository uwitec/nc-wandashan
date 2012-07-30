package nc.ui.zmpub.pub.report;

/**
 *功能:对一个节点访问的对此节点的数据调整操作。 可能的调整为：小计，合计，节点VO.
 * 
 * 创建日期：(2004-1-15 19:32:30)
 * 
 * @author：owind
 */
public interface IAdjustNodeTool {
	/* 调整分级统计的总计VO. */
	public void adjustAllTotalVO(nc.vo.pub.CircularlyAccessibleValueObject vo);

	/* 调整每一个节点VO. */
	public void adjustNodeVO(ExTreeNode node);

	/* 调整小计VO */
	public void adjustSubtotalVO(ExTreeNode node, String strObjcode);

	/* 调整合计VO. */
	public void adjustTotalVO(ExTreeNode node);
}

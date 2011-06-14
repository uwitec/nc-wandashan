package nc.ui.wl.pub.report;

/**
 * 功能：数据合并后的数据调整操作。
 * 		也可以对任何一个VO进行调整操作。
 * 创建日期：(2004-1-17 15:28:57)
 * @author：刘建波
 */
public interface IAdjustCombinVOTool {
/*对合并之后的VO进行处理。*/
public void adjustCombinedVO(
	nc.vo.pub.CircularlyAccessibleValueObject vo,
	Object oMsg);
/*对参加合并得VO进行处理。*/	
public void adjustDetailVO(nc.vo.pub.CircularlyAccessibleValueObject vo) ;
}

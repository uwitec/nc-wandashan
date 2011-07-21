package nc.vo.zb.pub.freeitem;


import nc.vo.pub.*;

/**
 * Defdef的聚合VO类。
 *
 * 创建日期：(2001-11-8)
 * @author：仲瑞庆
 */
public class DefdefVO extends AggregatedValueObject {

	private DefdefHeaderVO header = null;
	private DefdefItemVO[] items = null;

	// 时间戳标示，现在暂未使用：
	long currentTimestamp; // 当前时间戳
	long initialTimestamp; // 从数据库读入时获得的时间戳
/**
 * DefdefVO 构造子注解。
 */
public DefdefVO() {
	super();
}
/**
 * <p>获得子表的VO数组。
 * <p>
 * 创建日期：(2001-11-8)
 * @return nc.vo.pub.CircularlyAccessibleValueObject[]
 */
public CircularlyAccessibleValueObject[] getChildrenVO() {

	return items;
}
/**
 * <p>获得母表的VO。
 * <p>
 * 创建日期：(2001-11-8)
 * @return nc.vo.pub.CircularlyAccessibleValueObject
 */
public CircularlyAccessibleValueObject getParentVO() {

	return header;
}
/**
 * <p>设置子表的VO数组。
 * <p>
 * 创建日期：(2001-11-8)
 * @param children nc.vo.pub.CircularlyAccessibleValueObject[]
 */
public void setChildrenVO(CircularlyAccessibleValueObject[] children) {

	items = (DefdefItemVO[]) children;
}
/**
 * <p>设置母表的VO。
 * <p>
 * 创建日期：(2001-11-8)
 * @param parent nc.vo.pub.CircularlyAccessibleValueObject
 */
public void setParentVO(CircularlyAccessibleValueObject parent) {

	header = (DefdefHeaderVO) parent;
}
}
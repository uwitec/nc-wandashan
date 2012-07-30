package nc.vo.zmpub.pub.report;

/**
 * 穿透相关的扩展信息，如有需要请实现该接口
 * 
 * @author xkf
 * 
 */

public interface IPeneExtendInfo {

	/**
	 * 获得需要单独设置的列名（itemkey，针对公共列返回commoncolumn），主要是针对需要根据前台条件判断打开不同节点 或者是添加列
	 * 的前台条件（这些条件无法在后台初始化）
	 * 
	 * @return
	 */
	public String[][] getColumKeys();

	/**
	 * 针对列分组 返回对应的扩展条件，长度需要与getColumKeys()相同，且顺序对应
	 * 
	 * @return
	 */
	public String[] getExtendsWhereStrings();

	/**
	 * 返回针对列分组 对应的节点号，长度需要与getColumKeys()相同，且顺序对应
	 * 
	 * @return
	 */
	public String[] getNodeCodes();
}

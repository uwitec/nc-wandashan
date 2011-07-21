package nc.ui.zb.pub;

/**
 * <p>多子表界面VO信息。
 * <p>设置多子表界面有关的信息，如VO名称、各表体编码，以及编码-VO类名对照等。
 * @version V5.0
 * @修改历史：
 */
public interface IMultiChildVOInfo extends IMultiChild {
	/**
	 * 返回聚合VO,主表和各个子表的vo类名。
	 */
	public abstract String[] getVONames();

	/**
	 * 返回子表代码对应的VO类名。
	 * @param tblCode
	 * @return
	 * twh (2007-1-22 上午09:52:57)<br>
	 */
	public abstract String getVoClassNameByTableCode(String tblCode);
}

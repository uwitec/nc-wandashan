package nc.ui.wds.pub.report2;

import javax.swing.tree.DefaultTreeModel;
/**
 * 创建日期：(2004-1-14 14:48:24)
 * @author：刘建波
 */
public interface ITreeModelFactory {
	public DefaultTreeModel getModel(String strCode);
}

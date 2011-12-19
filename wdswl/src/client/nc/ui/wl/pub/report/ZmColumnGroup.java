package nc.ui.wl.pub.report;
import nc.ui.pub.beans.table.ColumnGroup;
/**
 * 能够记录父类的  报表组合表头
 * @author mlr
 */
public class ZmColumnGroup extends ColumnGroup{
    private String parentName=null;//记录父类的名字
    private String name=null;//自己的名字
	public ZmColumnGroup(String text) {
		super(text);
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}

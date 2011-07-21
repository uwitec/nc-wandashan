package nc.vo.hg.pu.pub;

import nc.ui.pub.beans.constenum.IConstEnum;

 public class ItemsEnum implements IConstEnum{
	 
	int index;
	
	String name;
	
	public void setIndex(int index) {
		this.index = index;
	}
	public Object getValue() {
		return index;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	@Override
	public String toString() {
		return getName();
	}
}

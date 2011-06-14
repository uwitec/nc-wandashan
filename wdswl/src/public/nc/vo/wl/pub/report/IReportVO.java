package nc.vo.wl.pub.report;

/**
 * 创建日期：(2004-1-15 14:47:48)
 * @author：刘建波 
 */
public interface IReportVO {
	public java.util.Vector getDetailvo();
	public boolean isCombined();
	public boolean isSubtotal();
	public boolean isTotal();
	public void setCombined(boolean b);
	public void setDetailVO(java.util.Vector v);
	public void setSubtotal(boolean b);
	public void setTotal(boolean b);
}

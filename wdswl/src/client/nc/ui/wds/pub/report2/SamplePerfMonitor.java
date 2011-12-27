package nc.ui.wds.pub.report2;


import java.util.Calendar;
import java.util.Hashtable;
/**
 * 创建日期：(2004-7-28 13:42:29)
 * @author：刘建波
 * UI端简单的性能监视工具.
 * 主要的性能指标:响应时间,内存耗用.
 */
public class SamplePerfMonitor {
	private static SamplePerfMonitor m_monitor = null;
	//测试项目.
	private java.util.Hashtable m_hTestItem = new Hashtable();
	public class PerfContext {
		//开始时间.
		private long bgtime = 0;
		//开始内存.
		private long bgmem = 0;
		//上次打印时间.
		private long lasttime=0;
		//上次打印内存.
		private long lastmem=0;
		public void setLastTime(long l){
			lasttime=l;
		}
		public long getLastTime(){
			return lasttime;
		}
		public void setLastMem(long l){
			lastmem=l;
		}
		public long getLastMem(){
			return lastmem;
		}
		//项目名称.
		private String itemname;
		//项目主键.
		private String itemKey;
		/**
		 * PerfContext 构造子注解。
		 */
		public PerfContext()
	{
			super();
		}
		public long getBgMem()
	{
			return bgmem;
		}
		public long getBgTime()
	{
			return bgtime;
		}
		public String getItemKey()
	{
			return itemKey;
		}
		public String getItemName()
	{
			return itemname;
		}
		public void setBgMem(long l)
	{
			bgmem = l;
		}
		public void setBgTime(long l)
	{
			bgtime = l;
		}
		public void setItemKey(String key)
	{
			itemKey = key;
		}
		public void setItemname(String name)
	{
			itemname = name;
		}
	}
/**
 * SampleUIPerfMonitor 构造子注解。
 */
private SamplePerfMonitor() {
	super();
}
private PerfContext getContextByKey(String key)
{
	Object o = m_hTestItem.get(key);
	if (o == null)
		return null;
	return (PerfContext) o;

}
private String getCurUFtime()
{
	nc.vo.pub.lang.UFDateTime time =
		new nc.vo.pub.lang.UFDateTime(Calendar.getInstance().getTime());
	return time.toString();
}
	public static synchronized SamplePerfMonitor getInstance()
{
		if (m_monitor == null)
			m_monitor = new SamplePerfMonitor();
		return m_monitor;
	}
private Runtime getRuntime()
{
	return Runtime.getRuntime();
}
/**
* 打印测试数据.
**/
public void printCurData(String key, String remark)
{
	PerfContext context = getContextByKey(key);
	if (context == null)
		return;
	long curtime = System.currentTimeMillis();
	long costtime = curtime - context.getBgTime();
	long diffcosttime = curtime - context.getLastTime();
	long curmem = getRuntime().totalMemory() - getRuntime().freeMemory();
	long costmem = curmem - context.getBgMem();
	long diffmem = curmem - context.getLastMem();
	context.setLastMem(curmem);
	context.setLastTime(curtime);
	remark = (remark == null ? "" : "---" + remark);
	System.out.println("#####测试项目:" + context.getItemName() + remark);
	System.out.println("#####与上次打印比较耗时	 :" + Long.toString(diffcosttime) + "毫秒.");
	System.out.println("#####与上次打印比较耗内存   :" + (float) diffmem / 1024 + "K.");
	System.out.println("#####总耗时	 :" + Long.toString(costtime) + "毫秒.");
	System.out.println("#####总耗内存   :" + (float) costmem / 1024 + "K.");
}
/**
*注册一个测试项目.
*/
public void registItem(String key, String itemname)
{
	PerfContext context = new PerfContext();
	context.setBgTime(System.currentTimeMillis());
	context.setBgMem(getRuntime().totalMemory() - getRuntime().freeMemory());
	context.setLastMem(getRuntime().totalMemory() - getRuntime().freeMemory());
	context.setLastTime(System.currentTimeMillis());
	context.setItemKey(key);
	context.setItemname(itemname);
	m_hTestItem.put(key, context);
	System.out.println(
		"#####测试项目:" + itemname + "\n######开始测试时间:" + getCurUFtime());
}
/**
* 结束测试项目.
*/
public void stopTest(String key)
{
	PerfContext context = getContextByKey(key);
	if(context==null) return;
	System.out.println("#####测试项目:" + context.getItemName() + "结束测试.");
	m_hTestItem.remove(key);
}
}

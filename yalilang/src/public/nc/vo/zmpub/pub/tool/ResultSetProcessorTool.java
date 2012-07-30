package nc.vo.zmpub.pub.tool;

import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;

/**
 * 常用结果集处理器定义
 * 
 * @author zhf
 */
public class ResultSetProcessorTool {
	public static final ColumnProcessor COLUMNPROCESSOR = new ColumnProcessor();
	public static final ColumnListProcessor COLUMNLISTPROCESSOR = new ColumnListProcessor();
	public static final ArrayListProcessor ARRAYLISTPROCESSOR = new ArrayListProcessor();
	public static final ArrayProcessor ARRAYPROCESSOR = new ArrayProcessor();
	public static final MapProcessor MAPPROCESSOR = new MapProcessor();
	public static final MapListProcessor MAPLISTPROCESSOR = new MapListProcessor();
}

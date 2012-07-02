package nc.ui.zmpub.pub.tool.stock;
import java.util.ArrayList;
import nc.bs.logging.Logger;
import nc.ui.zmpub.pub.tool.LongTimeTask;
import nc.vo.pub.SuperVO;
/**
 * 现存量操作前台 工具类
 * @author mlr
 */
public class StockTool {
	/**
	 * 根据传入的现存量vo  取出维度 查询现存量   
	 * ArrayList<SuperVO[]> 存放每个查询维度查询出来的现存量
	 * @throws Exception 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-7-2下午12:25:52
	 *
	 */
    public static ArrayList<SuperVO[]> queryStockDetail(SuperVO[] vos) throws Exception{
		 ArrayList<SuperVO[]> list=null;
    	try {
			Class[] ParameterTypes = new Class[] { SuperVO[].class };
			Object[] ParameterValues = new Object[] { vos };
			Object o = LongTimeTask.calllongTimeService(
					"zmpub", null, "正在查询...", 1,
					"nc.bs.zmpub.pub.tool.stock.BillStockBO", null, "queryStockDetail",
					ParameterTypes, ParameterValues);
			if (o != null) {
				list = (ArrayList<SuperVO[]>) o;
			}
		} catch (Exception e) {
			Logger.error(e);
			throw new Exception(e.getMessage());
			
		}		
      return list;	
    }
    /**
     * 根据传入的现存量vo  取出维度 查询现存量   
 	 * SuperVO[]  存放每个查询维度查询出来的现存量(按查询维度合并后)
  * @throws Exception 
     * @作者：mlr
     * @说明：完达山物流项目 
     * @时间：2012-7-2下午12:27:29
     *
     */
 	public static SuperVO[] queryStockCombin(SuperVO[] vos) throws Exception {
		 SuperVO[] nvos=null;
	    	try {
				Class[] ParameterTypes = new Class[] { SuperVO[].class };
				Object[] ParameterValues = new Object[] { vos };
				Object o = LongTimeTask.calllongTimeService(
						"zmpub", null, "正在查询...", 1,
						"nc.bs.zmpub.pub.tool.stock.BillStockBO", null, "queryStockCombin",
						ParameterTypes, ParameterValues);
				if (o != null) {
					nvos = (SuperVO[]) o;
				}
			} catch (Exception e) {
				Logger.error(e);
				throw new Exception(e.getMessage());
				
			}		
	      return nvos;	
	    }
 	
	/**
	 * 通过where条件查询现存量
	 * @param whereSql
	 * @return
	 */
	public static  SuperVO[] queryStock(String whereSql)throws Exception{
		 SuperVO[] nvos=null;
	    	try {
				Class[] ParameterTypes = new Class[] { String.class };
				Object[] ParameterValues = new Object[] { whereSql };
				Object o = LongTimeTask.calllongTimeService(
						"zmpub", null, "正在查询...", 1,
						"nc.bs.zmpub.pub.tool.stock.BillStockBO", null, "queryStock",
						ParameterTypes, ParameterValues);
				if (o != null) {
					nvos = (SuperVO[]) o;
				}
			} catch (Exception e) {
				Logger.error(e);
				throw new Exception(e.getMessage());
				
			}		
	      return nvos;	
	    }
}

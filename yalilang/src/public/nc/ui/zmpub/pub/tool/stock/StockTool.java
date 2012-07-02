package nc.ui.zmpub.pub.tool.stock;
import java.util.ArrayList;
import nc.bs.logging.Logger;
import nc.ui.zmpub.pub.tool.LongTimeTask;
import nc.vo.pub.SuperVO;
/**
 * �ִ�������ǰ̨ ������
 * @author mlr
 */
public class StockTool {
	/**
	 * ���ݴ�����ִ���vo  ȡ��ά�� ��ѯ�ִ���   
	 * ArrayList<SuperVO[]> ���ÿ����ѯά�Ȳ�ѯ�������ִ���
	 * @throws Exception 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-7-2����12:25:52
	 *
	 */
    public static ArrayList<SuperVO[]> queryStockDetail(SuperVO[] vos) throws Exception{
		 ArrayList<SuperVO[]> list=null;
    	try {
			Class[] ParameterTypes = new Class[] { SuperVO[].class };
			Object[] ParameterValues = new Object[] { vos };
			Object o = LongTimeTask.calllongTimeService(
					"zmpub", null, "���ڲ�ѯ...", 1,
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
     * ���ݴ�����ִ���vo  ȡ��ά�� ��ѯ�ִ���   
 	 * SuperVO[]  ���ÿ����ѯά�Ȳ�ѯ�������ִ���(����ѯά�Ⱥϲ���)
  * @throws Exception 
     * @���ߣ�mlr
     * @˵�������ɽ������Ŀ 
     * @ʱ�䣺2012-7-2����12:27:29
     *
     */
 	public static SuperVO[] queryStockCombin(SuperVO[] vos) throws Exception {
		 SuperVO[] nvos=null;
	    	try {
				Class[] ParameterTypes = new Class[] { SuperVO[].class };
				Object[] ParameterValues = new Object[] { vos };
				Object o = LongTimeTask.calllongTimeService(
						"zmpub", null, "���ڲ�ѯ...", 1,
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
	 * ͨ��where������ѯ�ִ���
	 * @param whereSql
	 * @return
	 */
	public static  SuperVO[] queryStock(String whereSql)throws Exception{
		 SuperVO[] nvos=null;
	    	try {
				Class[] ParameterTypes = new Class[] { String.class };
				Object[] ParameterValues = new Object[] { whereSql };
				Object o = LongTimeTask.calllongTimeService(
						"zmpub", null, "���ڲ�ѯ...", 1,
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

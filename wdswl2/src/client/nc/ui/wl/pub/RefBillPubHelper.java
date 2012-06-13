package nc.ui.wl.pub;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.wl.pub.WdsWlPubConst;
/**
  参照查询 对限制条件的创建临时表查询(主要针对in限制语句)
*/
public class RefBillPubHelper {
	
	private static String bo = "nc.bs.wl.pub.RefBillPubBO";
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *        参照查询 查询表头数据
	 * @时间：2011-7-5上午09:35:36
	 * @param billType 
	 * @param businessType 
	 * @param tmpWhere
	 * @param oUserObj 数组in中的参数
	 * @return
	 * @throws Exception
	 */
	public static nc.vo.pub.CircularlyAccessibleValueObject[] queryHeadAllData(String billType,String businessType,String tmpWhere,Object oUserObj) throws Exception {
		Class[] ParameterTypes = new Class[]{String.class,String.class,String.class,Object.class};
		Object[] ParameterValues = new Object[]{billType,businessType,tmpWhere,oUserObj};
		Object os = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "queryByClassCondtion", ParameterTypes, ParameterValues, 2);
		return (CircularlyAccessibleValueObject[]) os;
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *        参照查询 查询表体数据
	 * @时间：2011-7-5上午09:35:36
	 * @param billType 
	 * @param businessType 
	 * @param tmpWhere
	 * @param oUserObj 数组in中的参数
	 * @return
	 * @throws Exception
	 */
	public static CircularlyAccessibleValueObject[] queryBodyAllData(String billType, String id, String bodyCondition,Object oUserObj)throws Exception {
		Class[] ParameterTypes = new Class[]{String.class,String.class,String.class,Object.class};
		Object[] ParameterValues = new Object[]{billType,id,bodyCondition,oUserObj};
		Object os = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "queryBodyByClassCondtion", ParameterTypes, ParameterValues, 2);
		return (CircularlyAccessibleValueObject[]) os;
	}

}

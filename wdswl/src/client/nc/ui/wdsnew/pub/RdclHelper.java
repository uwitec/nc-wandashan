package nc.ui.wdsnew.pub;

import nc.ui.wl.pub.LongTimeTask;
import nc.vo.wds.rdcl.RdclVO;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 
 * @author yf
 *
 */
public class RdclHelper {

	private static String bo = "nc.bs.wds.rdcl.RdclDMO";
	/**
	 * 按公司查询
	 */
	public static RdclVO[] queryAll(String unitCode) throws Exception{
		Class[] ParameterTypes = new Class[] { String.class };
		Object[] ParameterValues = new Object[] { unitCode};
		Object os = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "queryAll", ParameterTypes, ParameterValues, 2);
		return os==null?null:(RdclVO[]) os;
		
	}
	/**
	 * 根据VO中所设定的条件返回所有符合条件的VO数组
	 */
	public static RdclVO[] queryByVO(RdclVO condRdclVO)
	throws Exception {
		Class[] ParameterTypes = new Class[] { RdclVO.class };
		Object[] ParameterValues = new Object[] { condRdclVO};
		Object os = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "queryByVO", ParameterTypes, ParameterValues, 2);
		return os==null?null:(RdclVO[]) os;
	}
}

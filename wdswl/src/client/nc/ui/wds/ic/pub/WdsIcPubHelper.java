package nc.ui.wds.ic.pub;

import nc.ui.wl.pub.LongTimeTask;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class WdsIcPubHelper {


	private  static String bo = "nc.bs.ic.pub.WdsIcPubBO";

	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 自动拣货入库
	 * @时间：2011-3-31下午08:20:29
	 * @param bodys
	 * @param cwhid
	 * @param cspaceid
	 * @return
	 * @throws Exception
	 */
	public static  Object autoInStore(TbGeneralBVO[] bodys, String cwhid, String cspaceid) throws Exception{
		if(bodys == null || bodys.length == 0)
			return null;
		Class[] ParameterTypes = new Class[]{TbGeneralBVO[].class,String.class,String.class};
		Object[] ParameterValues = new Object[]{bodys,cwhid,cspaceid};
		Object os = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "autoInTray", ParameterTypes, ParameterValues, 2);

		return os;
	}

	/**
	 * queryBodyVOs 方法注解。
	 */
	public static nc.vo.pub.CircularlyAccessibleValueObject[] queryIcInBillBodyAllData(
			String key, String strWhere)
			throws Exception {
		if(PuPubVO.getString_TrimZeroLenAsNull(strWhere)==null)
			strWhere = strWhere +=" geh_pk = '"+key+"'";
		else
			strWhere = strWhere +=" and geh_pk = '"+key+"'";
		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{strWhere};
		SuperVO[] os = (SuperVO[])LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, "nc.bs.ic.pub.WdsIcPubBO", "queryIcInBillBodyAllData", ParameterTypes, ParameterValues, 2);
		
		return os;
	}

}

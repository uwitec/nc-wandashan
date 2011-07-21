package nc.ui.hg.ia.vendor;

import java.util.Map;

import nc.ui.hg.pu.pub.LongTimeTask;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public class VendorHelper {
	private static String bo ="nc.bs.hg.ia.vendor.VendorBo";
	public static BalFreezeVO[] getBySueprVO(Map<String,SuperVO> map) throws Exception{
		Class[] ParameterTypes = new Class[]{SuperVO.class};
		Object[] ParameterValues = new Object[]{map};
		Object o = LongTimeTask.callRemoteService("pu",bo, "getBySueprVO", ParameterTypes, ParameterValues, 2);
		return o==null?null:(BalFreezeVO[])o;
	}
	
	public static UFDouble getByString(String ccustbasid,String szxmid,String pk_corp) throws Exception{
		Class[] ParameterTypes = new Class[]{String.class,String.class,String.class};
		Object[] ParameterValues = new Object[]{ccustbasid,szxmid,pk_corp};
		Object o = LongTimeTask.callRemoteService("pu",bo, "getByString", ParameterTypes, ParameterValues, 2);
		return PuPubVO.getUFDouble_NullAsZero(o);
	}

}

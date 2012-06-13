package nc.ui.wds.tray.lock;

import nc.ui.pub.ToftPanel;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.xn.XnRelationVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class LockTrayHelper {
	
	private static String bo = "nc.bs.wds.tray.lock.LockTrayBO";
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 锁定托盘
	 * @时间：2011-7-5下午07:02:34
	 * @param chid  库存单据主表ID
	 * @param lockTrayInfor 虚拟托盘实际托盘对应关系   key：托盘id，存货id    value：实际托盘信息数组
	 * @throws Exception
	 */
	public static void lockTray(ToftPanel tp,String chid,String cwareid,String gebbid,java.util.Map lockTrayInfor) throws Exception{
		if(PuPubVO.getString_TrimZeroLenAsNull(chid)==null)
			return;
		if(lockTrayInfor == null || lockTrayInfor.size() == 0)
			return;
		Class[] ParameterTypes = new Class[]{String.class,String.class,String.class,java.util.Map.class};
		Object[] ParameterValues = new Object[]{chid,cwareid,gebbid,lockTrayInfor};
		LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "lockTray", ParameterTypes, ParameterValues, 2);

	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 存货状态处调用的解锁处理
	 * @时间：2011-7-19下午12:18:08
	 * @param tp
	 * @param revos
	 * @throws Exception
	 */
	public static void reLockTray(ToftPanel tp,XnRelationVO[] revos) throws Exception{
		if(revos == null || revos.length == 0)
			return;
		Class[] ParameterTypes = new Class[]{XnRelationVO[].class};
		Object[] ParameterValues = new Object[]{revos};
		LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "reLockTray2", ParameterTypes, ParameterValues, 2);
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 
	 * @时间：2011-7-6下午03:03:12
	 * @param ctrayid 虚拟托盘id
	 * @param pk_invmanid 存货id
	 * @param vbatchcode 批次
	 * @return
	 * @throws Exception
	 */
	public static UFBoolean isLock(String ctrayid,String pk_invmanid,String vbatchcode) throws Exception{
		Class[] ParameterTypes = new Class[]{String.class,String.class,String.class};
		Object[] ParameterValues = new Object[]{ctrayid,pk_invmanid,vbatchcode};
		Object os = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "isLock", ParameterTypes, ParameterValues, 2);
		return PuPubVO.getUFBoolean_NullAs(os, UFBoolean.FALSE);
	}

}

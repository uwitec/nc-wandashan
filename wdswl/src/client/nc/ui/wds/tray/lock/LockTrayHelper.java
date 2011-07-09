package nc.ui.wds.tray.lock;

import nc.ui.pub.ToftPanel;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class LockTrayHelper {
	
	private static String bo = "nc.bs.wds.tray.lock.LockTrayBO";
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ��������
	 * @ʱ�䣺2011-7-5����07:02:34
	 * @param chid  ��浥������ID
	 * @param lockTrayInfor ��������ʵ�����̶�Ӧ��ϵ   key������id�����id    value��ʵ��������Ϣ����
	 * @throws Exception
	 */
	public static void lockTray(ToftPanel tp,String chid,String cwareid,java.util.Map lockTrayInfor) throws Exception{
		if(PuPubVO.getString_TrimZeroLenAsNull(chid)==null)
			return;
		if(lockTrayInfor == null || lockTrayInfor.size() == 0)
			return;
		Class[] ParameterTypes = new Class[]{String.class,String.class,java.util.Map.class};
		Object[] ParameterValues = new Object[]{chid,cwareid,lockTrayInfor};
		LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "lockTray", ParameterTypes, ParameterValues, 2);

	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-7-6����03:03:12
	 * @param ctrayid ��������id
	 * @param pk_invmanid ���id
	 * @param vbatchcode ����
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
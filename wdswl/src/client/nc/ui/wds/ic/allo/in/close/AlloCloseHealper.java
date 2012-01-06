package nc.ui.wds.ic.allo.in.close;

import nc.ui.pub.ToftPanel;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.wds.ic.allo.in.close.AlloCloseBillVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * ���˰��ŵ��ú�̨��
 * @author Administrator
 *
 */
public class AlloCloseHealper {
	
	private static String bo = "nc.bs.wds.ic.allo.in.close.AlloCloseBO";
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * ��ѯ����վ�ǵ�ǰ��¼��Ա�󶨵Ĳֿ�� ���˰���
	 * �����ǰ��¼�����ֵܲ� ���Բ�ѯ���е� ���˰���
	 * @ʱ�䣺2011-3-25����09:16:20
	 * @param wheresql
	 * @param pk_stordoc :�ֿ�
	 * @return
	 * @throws Exception
	 */

	public static AlloCloseBillVO[] doQuery(String wheresql,String pk_stordoc,String userid,UFBoolean isclose) throws Exception{
		AlloCloseBillVO[] dealVos=null;
		Class[] ParameterTypes = new Class[] { String.class,String.class,String.class,UFBoolean.class };
		Object[] ParameterValues = new Object[] { wheresql,pk_stordoc ,userid,isclose};
		Object o = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "doQuery", ParameterTypes, ParameterValues, 2);
		if(o != null){
			dealVos = (AlloCloseBillVO[])o;
		}
		return dealVos;
	}
	
	public static void doCloseOrOpen(String[] ids, ToftPanel tp,UFBoolean isclose) throws Exception {
		if (ids == null || ids.length == 0)
			return;
		Class[] ParameterTypes = new Class[] {String[].class,UFBoolean.class};
		Object[] ParameterValues = new Object[] {ids,isclose};
		LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, tp,
				"���ڴ���...", 2, bo, null, "doCloseOrOpen", ParameterTypes,
				ParameterValues);
	}
}

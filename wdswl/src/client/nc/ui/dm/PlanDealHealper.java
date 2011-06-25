package nc.ui.dm;

import java.util.ArrayList;
import java.util.List;
import nc.ui.pub.ToftPanel;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.dm.PlanDealVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * ���˰��ŵ��ú�̨��
 * @author Administrator
 *
 */
public class PlanDealHealper {
	
	private static String bo = "nc.bs.wl.dm.PlanDealBO";
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * ��ѯ����վ�ǵ�ǰ��¼��Ա�󶨵Ĳֿ�� ���˰���
	 * �����ǰ��¼�����ֵܲ� ���Բ�ѯ���е� ���˰���
	 * @ʱ�䣺2011-3-25����09:16:20
	 * @param wheresql
	 * @return
	 * @throws Exception
	 */

	public static PlanDealVO[] doQuery(String wheresql) throws Exception{
		PlanDealVO[] dealVos=null;
		Class[] ParameterTypes = new Class[] { String.class };
		Object[] ParameterValues = new Object[] { wheresql };
		Object o = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "doQuery", ParameterTypes, ParameterValues, 2);
		if(o != null){
			dealVos = (PlanDealVO[])o;
		}
		return dealVos;
	}
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * ���˼ƻ�  ���Ű�ť������
	 * @ʱ�䣺2011-3-25����02:59:20
	 */
	public static void doDeal(List ldata, ToftPanel tp) throws Exception {
		if (ldata == null || ldata.size() == 0)
			return;
		if(tp instanceof PlanDealClientUI){
			PlanDealClientUI ui = (PlanDealClientUI)tp;
			List<String> infor = new ArrayList<String>();
			infor.add(ui.cl.getUser());
			infor.add(ui.cl.getCorp());
			infor.add(ui.cl.getLogonDate().toString());
			Class[] ParameterTypes = new Class[] { List.class,List.class };
			Object[] ParameterValues = new Object[] { ldata,infor};
			LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, tp,
					"���ڴ���...", 2, bo, null, "doDeal", ParameterTypes,
					ParameterValues);
		}
	
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ���˼ƻ�¼�� �����ر�  ��δʵ��
	 * @ʱ�䣺2011-6-25����09:29:06
	 * @param billid
	 * @return
	 * @throws Exception
	 */
	public static HYBillVO closeBill(String billid) throws Exception{
//		if(lpara == null || lpara.size() ==0)
//			return null;
//		HYBillVO newbill = null;
//		Class[] ParameterTypes = new Class[] { java.util.List.class };
//		Object[] ParameterValues = new Object[] { lpara };
//		Object o = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "closeRows", ParameterTypes, ParameterValues, 2);
//		if(o != null){
//			newbill  = (HYBillVO)o;
//		}
		return null;
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ �ƻ�¼���йر�
	 * @ʱ�䣺2011-6-25����09:12:02
	 * @param lpara
	 * @return
	 * @throws Exception
	 */
	public static HYBillVO closeRows(List lpara) throws Exception{
		if(lpara == null || lpara.size() ==0)
			return null;
		HYBillVO newbill = null;
		Class[] ParameterTypes = new Class[] { java.util.List.class };
		Object[] ParameterValues = new Object[] { lpara };
		Object o = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "closeRows", ParameterTypes, ParameterValues, 2);
		if(o != null){
			newbill  = (HYBillVO)o;
		}
		return newbill;
	}
}

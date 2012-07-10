package nc.ui.dm.db.deal;
import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.ToftPanel;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.dm.db.deal.DbDealVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 调拨订单安排 后台调用
 * @author mlr
 */
public class DbDealHealper {
	
	private static String bo = "nc.bs.dm.db.DbDealBO";
	
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 查询到货站是当前登录人员绑定的仓库的 发运安排
	 * 如果当前登录人是总仓的 可以查询所有的 发运安排
	 * @时间：2011-3-25上午09:16:20
	 * @param wheresql
	 * @param pk_stordoc :仓库
	 * @return
	 * @throws Exception
	 */

	public static DbDealVO[] doQuery(String wheresql,String pk_stordoc,UFBoolean isclose) throws Exception{
		DbDealVO[] dealVos=null;
		Class[] ParameterTypes = new Class[] { String.class,String.class,UFBoolean.class };
		Object[] ParameterValues = new Object[] { wheresql,pk_stordoc ,isclose};
		Object o = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "doQuery", ParameterTypes, ParameterValues, 2);
		if(o != null){
			dealVos = (DbDealVO[])o;
		}
		return dealVos;
	}
	
	public static void doCloseOrOpen(String[] ids, ToftPanel tp,UFBoolean isclose) throws Exception {
		if (ids == null || ids.length == 0)
			return;
		Class[] ParameterTypes = new Class[] {String[].class,UFBoolean.class};
		Object[] ParameterValues = new Object[] {ids,isclose};
		LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, tp,
				"正在处理...", 2, bo, null, "doCloseOrOpen", ParameterTypes,
				ParameterValues);
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 
	 * 发运计划  安排按钮处理方法
	 * @时间：2011-3-25下午02:59:20
	 */
	public static void doDeal(List ldata, ToftPanel tp) throws Exception {
		if (ldata == null || ldata.size() == 0)
			return;
		DbDealClientUI ui = (DbDealClientUI)tp;
		List<String> infor = new ArrayList<String>();
		infor.add(ui.cl.getUser());
		infor.add(ui.cl.getCorp());
		infor.add(ui.cl.getLogonDate().toString());
		Class[] ParameterTypes = new Class[] { List.class,List.class };
		Object[] ParameterValues = new Object[] { ldata,infor};
		LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, tp,
				"正在处理...", 2, bo, null, "doDeal", ParameterTypes,
				ParameterValues);


	}
}

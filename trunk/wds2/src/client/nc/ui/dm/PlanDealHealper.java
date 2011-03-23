package nc.ui.dm;

import java.util.List;

import nc.ui.pub.ToftPanel;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.dm.PlanDealVO;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubConst;

public class PlanDealHealper {
	
	private static String bo = "nc.bs.wl.dm.PlanDealBO";

	public static PlanDealVO[] doQuery(String wheresql) throws BusinessException{
		return null;
	}
	
	public static void doDeal(List ldata, ToftPanel tp) throws Exception {
		if (ldata == null || ldata.size() == 0)
			return;
		Class[] ParameterTypes = new Class[] { List.class };
		Object[] ParameterValues = new Object[] { ldata };
		LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, tp,
				"正在处理...", 2, bo, null, "doDeal", ParameterTypes,
				ParameterValues);
	}
}

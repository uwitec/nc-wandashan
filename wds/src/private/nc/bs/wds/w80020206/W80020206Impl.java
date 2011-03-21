package nc.bs.wds.w80020206;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilBO;
import nc.itf.uap.IVOPersistence;
import nc.itf.wds.w80020206.Iw80020206;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
import nc.vo.wds.w80060604.SoSaleVO;

public class W80020206Impl implements Iw80020206 {

	public Object whs_processAction80020206(String actionName, String actionName2,
			String billType, String currentDate, AggregatedValueObject vo,
			Object outgeneralVo) throws Exception {
		if (null != outgeneralVo)
			getIvo().updateVO((SoSaleVO) outgeneralVo);
		nc.bs.pub.pf.PfUtilBO pfutilbo = new PfUtilBO();
		// 保存ERP中销售出库
		Object o = pfutilbo.processAction(actionName, billType, currentDate,
				null, vo, null);
		if (actionName.equals("CANCELSIGN")) {
			boolean oper = Boolean.parseBoolean(((ArrayList) o).get(0)
					.toString());
			if (!oper)
				return null;
			o = pfutilbo.processAction(actionName2, billType, currentDate,
					null, vo, null);
			return o;
		}
		if (actionName.equals("SAVEPICKSIGN")) {
			return o;
		}
		AggregatedValueObject billVO = null;
		Object[] arrayO = (Object[]) o;
		billVO = (AggregatedValueObject) arrayO[0];
		// 销售出库签字
		o = pfutilbo.processAction(actionName2, billType, currentDate, null,
				billVO, null);
		return o;
	}

	public IVOPersistence getIvo() {
		return (IVOPersistence) NCLocator.getInstance().lookup(
				IVOPersistence.class.getName());
	}
}

package nc.ui.dm.confirm;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.bs.wds.w80060210.W80060210Impl;
import nc.itf.wds.w80060210.Iw80060210;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pub.pf.IUIBeforeProcAction;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.businessaction.BdBusinessAction;
import nc.ui.trade.businessaction.IPFACTION;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.trade.pub.IBDGetCheckClass2;

public class W80060210Action extends BdBusinessAction {
	private AbstractBillUI billUI = null;

	public W80060210Action(AbstractBillUI billUI) {
		super();
		this.billUI = billUI;
	}

	/**
	 * save 方法注解。
	 */
	public AggregatedValueObject save(AggregatedValueObject billVO,
			String billType, String billDate, Object userObj,
			AggregatedValueObject checkVo) throws Exception {
		// ArrayList param=(ArrayList)userObj;
		if (userObj instanceof IBDGetCheckClass2) {
			RunUIBeforeCheck(((IBDGetCheckClass2) userObj).getUICheckClass(),
					billType, IPFACTION.SAVE, userObj, checkVo);
		} else {
			// 如果数据库注册，按照数据库执行，如果未注册，按照以前机制运行
			BilltypeVO billVo = PfUIDataCache.getBillType(billType);
			// 动作执行前的事前处理
			String strClassName = billVo == null ? null : billVo.getDef3();
			RunUIBeforeCheck(strClassName, billType, IPFACTION.SAVE, userObj,
					checkVo);

		}
		Iw80060210 iw = (Iw80060210) NCLocator.getInstance().lookup(
				Iw80060210.class.getName());
		AggregatedValueObject retVo = iw.saveAndCommit80060210(billVO, userObj);
		// AggregatedValueObject retVo = nc.ui.trade.business.HYPubBO_Client
		// .saveBD(billVO, (userObj));
		if (userObj instanceof nc.vo.trade.pub.IRetCurrentDataAfterSave) {
			fillUITotalVO(checkVo.getChildrenVO(), retVo.getChildrenVO());
			checkVo.setParentVO(retVo.getParentVO());
			retVo = checkVo;
		}
		return retVo;
	}

	/**
	 * delete 方法注解。
	 */
	// public nc.vo.pub.AggregatedValueObject delete(
	// nc.vo.pub.AggregatedValueObject billVO, String billType,
	// String billDate, Object userObj) throws Exception {
	// ArrayList param=(ArrayList)userObj;
	// if (param.get(0) instanceof IBDGetCheckClass2) {
	// RunUIBeforeCheck(((IBDGetCheckClass2) param.get(0)).getUICheckClass(),
	// billType, IPFACTION.DELETE, param.get(0), billVO);
	// } else {
	// //如果数据库注册，按照数据库执行，如果未注册，按照以前机制运行
	// BilltypeVO billVo = PfUIDataCache.getBillType(billType);
	// //动作执行前的事前处理
	// String strClassName =billVo==null?null:billVo.getDef3();
	// RunUIBeforeCheck(strClassName, billType, IPFACTION.DELETE, param.get(0),
	// billVO);
	// }
	// Iw80060208 iw = (Iw80060208) NCLocator.getInstance().lookup(
	// Iw80060208.class.getName());
	// AggregatedValueObject retVo = iw.deleteBD80060208(billVO, userObj);
	// return null;
	// }
	private void RunUIBeforeCheck(String checkClsName, String billType,
			String actionName, Object userObj, AggregatedValueObject checkVo)
			throws Exception {
		if (checkClsName == null || checkClsName.length() == 0)
			return;
		Class c = Class.forName(checkClsName);
		Object o = c.newInstance();
		if (o instanceof IUIBeforeProcAction) {
			((IUIBeforeProcAction) o).runClass(billUI, billType, actionName,
					checkVo, userObj);
		}
	}

}

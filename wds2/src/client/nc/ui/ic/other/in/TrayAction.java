package nc.ui.ic.other.in;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.wds.w8004040210.Iw8004040210;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pub.pf.IUIBeforeProcAction;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.businessaction.BdBusinessAction;
import nc.ui.trade.businessaction.IPFACTION;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.trade.pub.IBDGetCheckClass2;

public class TrayAction extends BdBusinessAction {

	private AbstractBillUI billUI = null;

	public TrayAction(AbstractBillUI billUI) {
		super();
		this.billUI = billUI;
	}

	public AggregatedValueObject save(AggregatedValueObject billVO,
			String billType, String billDate, Object userObj,
			AggregatedValueObject checkVo) throws Exception {

		ArrayList param = (ArrayList) userObj;

		if (param.get(0) instanceof IBDGetCheckClass2) {
			RunUIBeforeCheck(((IBDGetCheckClass2) param.get(0))
					.getUICheckClass(), billType, IPFACTION.SAVE, param.get(0),
					checkVo);
		} else {
			// 如果数据库注册，按照数据库执行，如果未注册，按照以前机制运行
			BilltypeVO billVo = PfUIDataCache.getBillType(billType);
			// 动作执行前的事前处理
			String strClassName = billVo == null ? null : billVo.getDef3();
			RunUIBeforeCheck(strClassName, billType, IPFACTION.SAVE, param
					.get(0), checkVo);

		}
		if ("1".equals(param.get(4).toString())) {
			Iw8004040210 iw = (Iw8004040210) NCLocator.getInstance().lookup(
					Iw8004040210.class.getName());
			AggregatedValueObject retVo = iw.saveBD(billVO, userObj);
			if (((ArrayList) userObj).get(0) instanceof nc.vo.trade.pub.IRetCurrentDataAfterSave) {
				fillUITotalVO(checkVo.getChildrenVO(), retVo.getChildrenVO());
				checkVo.setParentVO(retVo.getParentVO());
				retVo = checkVo;
			}
			return retVo;
		} else {
			AggregatedValueObject retVo = nc.ui.trade.business.HYPubBO_Client
					.saveBD(billVO, param.get(0));
			if (((ArrayList) userObj).get(0) instanceof nc.vo.trade.pub.IRetCurrentDataAfterSave) {
				fillUITotalVO(checkVo.getChildrenVO(), retVo.getChildrenVO());
				checkVo.setParentVO(retVo.getParentVO());
				retVo = checkVo;
			}
			return retVo;
		}
		// AggregatedValueObject retVo = nc.ui.trade.business.HYPubBO_Client
		// .saveBD(billVO, ((ArrayList)userObj).get(0));

	}

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

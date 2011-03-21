package nc.ui.wds.w80020202;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.wds.w80020202.Iw80020202;
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
import nc.vo.wds.w80020202.TbHandlecostsVO;

public class W80020202Action extends BdBusinessAction {

	private AbstractBillUI billUI = null;

	public W80020202Action(AbstractBillUI billUI) {
		super();
		this.billUI = billUI;
	}

	public AggregatedValueObject save(AggregatedValueObject billVO,
			String billType, String billDate, Object userObj,
			AggregatedValueObject checkVo) throws Exception {
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
		AggregatedValueObject retVo = null;
		if (null != billVO) {
			TbHandlecostsVO tbHandlecostsVO = (TbHandlecostsVO) billVO
					.getParentVO();
			if (null != tbHandlecostsVO) {
				if (null != tbHandlecostsVO.getDbilltype()) {
					if (tbHandlecostsVO.getDbilltype().intValue() == 0) {
						Iw80020202 iw = (Iw80020202) NCLocator.getInstance()
								.lookup(Iw80020202.class.getName());
						retVo = iw.saveBD80020202(billVO, userObj);
					} else {
						retVo = nc.ui.trade.business.HYPubBO_Client.saveBD(
								billVO, userObj);
					}
				}
			}
		}
		if (userObj instanceof nc.vo.trade.pub.IRetCurrentDataAfterSave) {
			fillUITotalVO(checkVo.getChildrenVO(), retVo.getChildrenVO());
			checkVo.setParentVO(retVo.getParentVO());
			retVo = checkVo;
		}
		return retVo;
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

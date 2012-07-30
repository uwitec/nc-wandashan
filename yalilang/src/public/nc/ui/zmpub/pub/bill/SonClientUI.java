package nc.ui.zmpub.pub.bill;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.itf.zmpub.pub.ISonVO;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;

/**
 * 支持孙表保存的ui
 * 
 * @author mlr
 */
public abstract class SonClientUI extends BillManageUI {
	private static final long serialVersionUID = 8902467752873715641L;

	public abstract Map<String, List<SuperVO>> getSonMap();

	public AggregatedValueObject getChangedVOFromUI()
			throws java.lang.Exception {
		HYBillVO billvo = (HYBillVO) this.getBillCardWrapper()
				.getChangedVOFromUI();
		HYBillVO billvo2 = (HYBillVO) this.getBillCardWrapper()
				.getBillVOFromUI();
		HYBillVO newbillvo = new HYBillVO();
		// 存放没有变化的vo
		List<SuperVO> listu = new ArrayList<SuperVO>();
		if (billvo2 != null) {
			SuperVO[] vos = (SuperVO[]) billvo2.getChildrenVO();
			if (vos != null && vos.length != 0) {
				for (int i = 0; i < vos.length; i++) {
					if (vos[i].getStatus() == VOStatus.UNCHANGED) {
						listu.add(vos[i]);
					}
				}
			}
		}
		// 存放已经变化的vo
		List<SuperVO> listc = new ArrayList<SuperVO>();
		if (billvo != null) {
			SuperVO[] vos = (SuperVO[]) billvo.getChildrenVO();
			if (vos != null && vos.length != 0) {
				for (int i = 0; i < vos.length; i++) {
					listc.add(vos[i]);
				}
			}
		}
		listc.addAll(listu);
		String cls = getUIControl().getBillVoName()[1];
		Class c = Class.forName(cls);
		newbillvo
				.setChildrenVO(listc
						.toArray((SuperVO[]) java.lang.reflect.Array
								.newInstance(c, 0)));
		if (billvo != null && billvo.getParentVO() != null) {
			newbillvo.setParentVO(billvo.getParentVO());
		} else {
			newbillvo.setParentVO(billvo2.getParentVO());
		}
		SuperVO[] bodys = (SuperVO[]) newbillvo.getChildrenVO();
		if (bodys == null || bodys.length == 0)
			return newbillvo;
		if (getSonMap() == null)
			return newbillvo;
		String key = null;
		for (SuperVO body : bodys) {
			ISonVO isvo = (ISonVO) body;
			key = PuPubVO.getString_TrimZeroLenAsNull(body
					.getAttributeValue(isvo.getRowNumName()));
			String pk = body.getPrimaryKey();
			if (getSonMap().containsKey(key + pk)) {
				isvo.setSonVOS((getSonMap().get(key + pk)));
			}
		}
		return newbillvo;
	}

	@Override
	protected AbstractManageController createController() {
		return null;
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	@Override
	protected void initSelfData() {

	}

	@Override
	public void setDefaultData() throws Exception {

	}
}

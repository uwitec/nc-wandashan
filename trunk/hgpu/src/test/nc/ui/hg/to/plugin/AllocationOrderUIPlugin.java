package nc.ui.hg.to.plugin;

/**
 * zhw 2011-03-20 调拨订单二次开发ui扩展插件
 */
import java.awt.event.ActionEvent;

import nc.bs.hg.scm.pub.HgScmPubBO;
import nc.ui.hg.pu.pub.LongTimeTask;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.scm.plugin.IScmUIPlugin;
import nc.ui.scm.plugin.SCMUIContext;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.vo.hg.pu.plan.year.PlanYearBVO;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.plugin.Action;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.to.pub.BillItemVO;

public class AllocationOrderUIPlugin implements IScmUIPlugin {

	HgScmPubBO bo = null;

	private HgScmPubBO getScmPubBO() {
		if (bo == null) {
			bo = new HgScmPubBO();
		}
		return bo;
	}

	public void afterAction(Action action, AggregatedValueObject[] billvos,
			SCMUIContext conx) throws BusinessException {
		// TODO Auto-generated method stub

	}

	public void afterButtonClicked(ButtonObject bo, SCMUIContext ctx)
			throws BusinessException {
		// TODO Auto-generated method stub

	}

	public void afterEdit(BillEditEvent e, SCMUIContext ctx) {
		// TODO Auto-generated method stub
		if (e.getPos() == BillItem.HEAD)
			return;

		// 表体编辑后事件处理

		if (e.getKey().equals("vbatch")) {// 批次
			BillModel bm = ctx.getBillCardPanel().getBillModel();
			int row = e.getRow();
			String pk_planproject = PuPubVO.getString_TrimZeroLenAsNull(bm
					.getValueAt(row, "vbdef11"));
			if (pk_planproject != null) {
				String way = null;
				try {
					way = getIOutWay(pk_planproject);
				} catch (Exception ee) {
					ee.printStackTrace();
					ctx.getToftPanel().showErrorMessage(HgPubTool.getString_NullAsTrimZeroLen(ee.getMessage()));
					return;
				}
				if ("合同价".equalsIgnoreCase(way)) {
					String invcode = PuPubVO.getString_TrimZeroLenAsNull(bm.getValueAt(row, "cinvcode"));
					String vbatch = PuPubVO.getString_TrimZeroLenAsNull(bm.getValueAt(row, "vbatch"));
					String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
					UFDouble nnum =PuPubVO.getUFDouble_NullAsZero(bm.getValueAt(row,"nnum"));
					if (invcode == null || vbatch == null || pk_corp==null ) 
						return;
				
					UFDouble pricedouble = null;
					try {
						pricedouble = calFormAllocationOrderOrder(invcode,pk_corp,vbatch);
					} catch (Exception ee) {
						ee.printStackTrace();
						ctx.getToftPanel().showErrorMessage(
								HgPubTool.getString_NullAsTrimZeroLen(ee
										.getMessage()));
						return;
					}
					if (pricedouble == null)
						return;

					setCardPanelBodyValue(bm, "nnotaxprice", pricedouble,
							row); // 原币无税单价
				}
			}
		}
	}

	private void setCardPanelBodyValue(BillModel bm, String itemkey,
			Object oValue, int row) {
		bm.setValueAt(oValue, row, itemkey);
	}

	public void afterSetBillVOToCard(AggregatedValueObject billvo,
			SCMUIContext conx) {
		// TODO Auto-generated method stub

	}

	public void afterSetBillVOsToListBody(
			CircularlyAccessibleValueObject[] bodyvos, SCMUIContext conx) {
		// TODO Auto-generated method stub

	}

	public void afterSetBillVOsToListHead(
			CircularlyAccessibleValueObject[] headvos, SCMUIContext conx) {
		// TODO Auto-generated method stub

	}

	public void beforeAction(Action action, AggregatedValueObject[] billvos,
			SCMUIContext conx) throws BusinessException {
		// TODO Auto-generated method stub

	}

	public void beforeButtonClicked(ButtonObject bo, SCMUIContext ctx)
			throws BusinessException {
		// TODO Auto-generated method stub
//		if(bo ==)

	}

	public boolean beforeEdit(BillEditEvent e, SCMUIContext ctx) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean beforeEdit(BillItemEvent e, SCMUIContext conx) {
		// TODO Auto-generated method stub
		return false;
	}

	public AggregatedValueObject[] beforePrint(
			AggregatedValueObject[] printVOs, SCMUIContext conx) {
		// TODO Auto-generated method stub
		return null;
	}

	public void beforeSetBillVOToCard(AggregatedValueObject billvo,
			SCMUIContext conx) {
		// TODO Auto-generated method stub

	}

	public void beforeSetBillVOsToListBody(
			CircularlyAccessibleValueObject[] bodyvos, SCMUIContext conx) {
		// TODO Auto-generated method stub

	}

	public void beforeSetBillVOsToListHead(
			CircularlyAccessibleValueObject[] headvos, SCMUIContext conx) {
		// TODO Auto-generated method stub

	}

	public void bodyRowChange(BillEditEvent e, SCMUIContext ctx) {
		// TODO Auto-generated method stub

	}

	public boolean init(SCMUIContext ctx) {
		// TODO Auto-generated method stub
		return false;
	}

	public void mouse_doubleclick(BillMouseEnent e, SCMUIContext ctx) {
		// TODO Auto-generated method stub

	}

	public void onAddLine(SCMUIContext conx) throws BusinessException {
		// TODO Auto-generated method stub

	}

	public void onMenuItemClick(ActionEvent e, SCMUIContext ctx) {
		// TODO Auto-generated method stub

	}

	public void onPastLine(SCMUIContext conx) throws BusinessException {
		// TODO Auto-generated method stub

	}

	public String onQuery(String swhere, SCMUIContext conx)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object[] retBillToBillRefVOs(
			CircularlyAccessibleValueObject[] headVos,
			CircularlyAccessibleValueObject[] bodyVos) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public AggregatedValueObject[] retChangeBusiVOs(
			AggregatedValueObject[] preVo, AggregatedValueObject[] nowVo)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setButtonStatus(SCMUIContext conx) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业）采购订单订单询采购合同价销售 2012-2-21下午02:27:22
	 * @param cinvbasid
	 * @param cbatchid
	 * @throws BusinessException
	 */
	public static UFDouble calFormAllocationOrderOrder(String invcode, String pk_corp,String vbatch) throws Exception {
		UFDouble npriceinfor = null;
		if (PuPubVO.getString_TrimZeroLenAsNull(invcode) == null
				|| PuPubVO.getString_TrimZeroLenAsNull(pk_corp) == null || PuPubVO.getString_TrimZeroLenAsNull(vbatch)==null)
			return null;

		Class[] ParameterTypes = new Class[] { String.class, String.class,String.class };
		Object[] ParameterValues = new Object[] { invcode, pk_corp ,vbatch};
		Object o = LongTimeTask.callRemoteService("scmpub",
				"nc.bs.hg.scm.pub.HgScmPubBO", "callPurchasePactPrice",
				ParameterTypes, ParameterValues, 2);

		if (o == null || !(o instanceof UFDouble))
			return null;

		npriceinfor = (UFDouble) o;
		return npriceinfor;
	}

	/**
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业）采根据计划项目找出库方式 2012-2-21下午02:27:22
	 * @param cinvbasid
	 * @param cbatchid
	 * @throws BusinessException
	 */
	public static String getIOutWay(String pk_planproject) throws Exception {
		String str = null;
		if (PuPubVO.getString_TrimZeroLenAsNull(pk_planproject) == null)
			return null;

		Class[] ParameterTypes = new Class[] { String.class };
		Object[] ParameterValues = new Object[] { pk_planproject };
		Object o = LongTimeTask.callRemoteService("scmpub",
				"nc.bs.hg.scm.pub.HgScmPubBO", "getIOutWay", ParameterTypes,
				ParameterValues, 1);

		if (o == null)
			return null;

		str = (String) o;
		return str;
	}

}

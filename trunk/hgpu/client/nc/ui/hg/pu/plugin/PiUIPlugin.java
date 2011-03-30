package nc.ui.hg.pu.plugin;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import nc.ui.hg.pu.pub.LongTimeTask;
import nc.ui.pi.invoice.IButtonConstInv;
import nc.ui.pi.invoice.InvoiceUI;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.scm.plugin.IScmUIPlugin;
import nc.ui.scm.plugin.SCMUIContext;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pi.InvoiceHeaderVO;
import nc.vo.pi.InvoiceItemVO;
import nc.vo.pi.InvoiceVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.plugin.Action;
import nc.vo.scm.pu.PuPubVO;

/**
 * 
 * @author liuys 根据出库单上的存货加批次追溯上游采购合同价格 调用nc.bs.hg.so.pub.HgBsPubBO取价算法
 * @date 2010-12-21
 */
public class PiUIPlugin implements IScmUIPlugin {
	private SCMUIContext ctx = null;

	private static String bo = "nc.bs.hg.so.pub.HgSoPubBO";

	private static Map<String, UFDouble[]> m_pactPriceInfor = null;

	private static Map<String, UFDouble[]> getPactPriceInfor() {
		if (m_pactPriceInfor == null)
			m_pactPriceInfor = new HashMap<String, UFDouble[]>();
		return m_pactPriceInfor;
	}

	public void afterAction(Action action, AggregatedValueObject[] billvos,
			SCMUIContext conx) throws BusinessException {
		// TODO Auto-generated method stub

	}

	private InvoiceUI getUI() {
		return (InvoiceUI) ctx.getToftPanel();
	}

	public void afterButtonClicked(ButtonObject bo, SCMUIContext ctx)
			throws BusinessException {
		// TODO Auto-generated method stub
		this.ctx = ctx;
		String tag = bo.getTag();
		if (tag == null)
			return;
		int index = tag.indexOf(":");
		if (index == -1)
			return;
		String strUpBillType = tag.substring(0, index);// 来源单据类型

		// 如果来源单据类型是：“50”(根据消耗汇总记录生成采购发票时)，则作如下处理
		if ("50".equalsIgnoreCase(strUpBillType)
				&& ctx.getCbilltype().equals(ScmConst.PO_Invoice)) {
			InvoiceItemVO[] bodyvos = (InvoiceItemVO[]) ctx.getBillCardPanel()
					.getBillModel().getBodyValueVOs(
							InvoiceItemVO.class.getName());
			for (int i = 0; i < bodyvos.length; i++) {
				String cmangid = bodyvos[i].getCmangid();
				String vbatchcode = bodyvos[i].getVproducenum();
				if (cmangid == null || vbatchcode == null)
					return;
				UFDouble[] pricedouble = null;
				try {
					pricedouble = callPurchasePactPrice(cmangid, vbatchcode);
				} catch (Exception ee) {
					ee.printStackTrace();
					ctx.getToftPanel().showErrorMessage(
							HgPubTool.getString_NullAsTrimZeroLen(ee
									.getMessage()));
					return;
				}
				if (pricedouble == null || pricedouble.length == 0
						|| pricedouble[1] == null)
					return;
				// modify by zhw 2011-01-24 取出的无税单价放到对应的无税单价上 也取出税率
				ctx.getBillCardPanel().setBodyValueAt(pricedouble[0], i,
						"ntaxrate");
				ctx.getBillCardPanel().setBodyValueAt(pricedouble[1], i,
						"noriginalcurprice");
				// 调用afteredit事件联算其他单价
				BillEditEvent be = new BillEditEvent(ctx.getBillCardPanel()
						.getBodyItem("noriginalcurprice"), null,
						pricedouble[1], "noriginalcurprice", i, BillItem.BODY);
				getUI().afterEdit(be);
			}
		}

	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）销售订单询采购合同价销售 2012-2-21下午02:27:22
	 * @param cinvbasid
	 * @param cbatchid
	 * @throws BusinessException
	 */
	public static UFDouble[] callPurchasePactPrice(String cinvmanid,
			String cbatchid) throws Exception {
		UFDouble[] npriceinfor = null;
		if (PuPubVO.getString_TrimZeroLenAsNull(cbatchid) == null
				|| PuPubVO.getString_TrimZeroLenAsNull(cinvmanid) == null)
			return null;
		// String key = cinvmanid.trim()+cbatchid.trim();
		// if(getPactPriceInfor().containsKey(key)){
		// return getPactPriceInfor().get(key);
		// }

		Class[] ParameterTypes = new Class[] { String.class, String.class };
		Object[] ParameterValues = new Object[] { cinvmanid, cbatchid };
		Object o = LongTimeTask.callRemoteService("so", bo,
				"callPurchasePactPrice", ParameterTypes, ParameterValues, 2);

		if (o == null || !(o instanceof UFDouble[]))
			return null;

		npriceinfor = (UFDouble[]) o;
		// getPactPriceInfor().put(key, npriceinfor);
		return npriceinfor;
	}

	public void afterEdit(BillEditEvent e, SCMUIContext ctx) {
		// TODO Auto-generated method stub

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
		this.ctx = ctx;
		if (bo.getCode().equals(IButtonConstInv.BTN_SAVE)) {
			InvoiceItemVO[] bodyvos = (InvoiceItemVO[]) ctx.getBillCardPanel().getBillModel().getBodyValueVOs(
							InvoiceItemVO.class.getName());
			String strBillType = PuPubVO.getString_TrimZeroLenAsNull(ctx.getBillCardPanel().getHeadItem("cbiztype").getValueObject());
			for (int i = 0; i < bodyvos.length; i++) {
				String cmangid = bodyvos[i].getCmangid();
				String vbatchcode = bodyvos[i].getVproducenum();
				String rowid = bodyvos[i].getCsourcebillrowid();
				UFDouble uicgfp = (UFDouble) ctx.getBillCardPanel()
						.getBodyValueAt(i, "noriginalcurprice");
				UFDouble[] pricedouble = null;
				UFDouble pricedoubleo = null;
				BigDecimal pricedouble1 = null;
				try {
					// 如果来源单据类型是：“50”(根据消耗汇总记录生成采购发票时)，则作如下处理 modify by
					// zhw 2011-03-04  0001Q110000000000PHN  代储代销主键
					if ("0001Q110000000000PHN".equalsIgnoreCase(strBillType)&& ctx.getCbilltype().equals(ScmConst.PO_Invoice)) {
						if (cmangid == null || vbatchcode == null)
							return;
						pricedouble = callPurchasePactPrice(cmangid,vbatchcode);
						pricedouble1 = pricedouble[1].toBigDecimal().setScale(2, RoundingMode.DOWN);
					} else {
						if (rowid == null)
							return;
						pricedoubleo = calFormPoOrder(rowid);
						pricedouble1 = pricedoubleo.toBigDecimal().setScale(2,
								RoundingMode.DOWN);
					}
				} catch (Exception ee) {
					ee.printStackTrace();
					ctx.getToftPanel().showErrorMessage(
							HgPubTool.getString_NullAsTrimZeroLen(ee
									.getMessage()));
					return;
				}
				BigDecimal noriginalcurprice = uicgfp.toBigDecimal().setScale(
						2, RoundingMode.DOWN);
				if ((noriginalcurprice.doubleValue() - (pricedouble1
						.add(BigDecimal.valueOf(0.01)).doubleValue())) > 0) {
					throw new BusinessException("第" + (i + 1)
							+ "行采购发票单价不能超采购合同单价");
				}
			}
			// add by zhw 2011-03-11  校验表体  表头 税率是否一致
			validntaxrate();
		}else if(bo.getCode().equals(IButtonConstInv.BTN_BILL_EDIT)){// add by zhw 2011-03-11 修改时校验是否生成报账单
			 Object oc1 =ctx.getBillCardPanel().getHeadItem("vdef20").getValueObject();
			 if(PuPubVO.getString_TrimZeroLenAsNull(oc1)!=null){
				 throw new BusinessException("该发票有报账单,不能修改");
			 }
		}else if(bo.getCode().equals(IButtonConstInv.BTN_BILL_DELETE)){// add by zhw 2011-03-11 删除时校验是否生成报账单
			 Object oc1 =ctx.getBillCardPanel().getHeadItem("vdef20").getValueObject();
			 if(PuPubVO.getString_TrimZeroLenAsNull(oc1)!=null){
				 throw new BusinessException("该发票有报账单,不能删除");
			 }
		}
		else if(bo.getCode().equals(IButtonConstInv.BTN_AUDIT)){// add by zhw 2011-03-11 审核时校验是否生成报账单
			 Object oc1 =ctx.getBillCardPanel().getHeadItem("vdef20").getValueObject();
			 if(PuPubVO.getString_TrimZeroLenAsNull(oc1)==null){
				 throw new BusinessException("该发票没有报账单,不能审核");
			 }
		}
	}

	public void validntaxrate() throws BusinessException{
		UFDouble hrate = PuPubVO.getUFDouble_NullAsZero(ctx.getBillCardPanel().getHeadItem("ntaxrate").getValue());
		int rowcount = ctx.getBillCardPanel().getRowCount();
		for(int i=0;i<rowcount;i++){
			UFDouble brate = PuPubVO.getUFDouble_NullAsZero(ctx.getBillCardPanel().getBodyValueAt(i,"ntaxrate"));
			if(!hrate.equals(brate)){
				throw new BusinessException("第"+(i+1)+"行的表体税率与表头不同");
			}
				
		}
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
	 * @author zhf
	 * @说明：（鹤岗矿业）销售订单询采购合同价销售 2012-2-21下午02:27:22
	 * @param cinvbasid
	 * @param cbatchid
	 * @throws BusinessException
	 */
	public static UFDouble calFormPoOrder(String orderbid) throws Exception {
		UFDouble npriceinfor = null;
		if (PuPubVO.getString_TrimZeroLenAsNull(orderbid) == null)
			return null;

		Class[] ParameterTypes = new Class[] { String.class };
		Object[] ParameterValues = new Object[] { orderbid };
		Object o = LongTimeTask.callRemoteService("so", bo, "calFormPoOrder",
				ParameterTypes, ParameterValues, 2);

		if (o == null || !(o instanceof UFDouble))
			return null;

		npriceinfor = (UFDouble) o;
		return npriceinfor;
	}
}

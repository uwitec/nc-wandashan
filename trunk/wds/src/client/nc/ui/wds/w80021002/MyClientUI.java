package nc.ui.wds.w80021002;

import java.util.ArrayList;

import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */
public class MyClientUI extends AbstractMyClientUI implements
		BillCardBeforeEditListener {

	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}

	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void initSelfData() {
	}

	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}

	public void setDefaultData() throws Exception {
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		if (e.getKey().equals("hfp_boxt")) {
			getBillCardPanel().execHeadEditFormulas();
		}
		if (e.getKey().equals("hfp_packpricet")) {
			getBillCardPanel().execHeadEditFormulas();
		}
		if (e.getKey().equals("htp_unboxpricet")) {
			getBillCardPanel().execHeadEditFormulas();
		}
		super.afterEdit(e);
	}

	public boolean beforeEdit(BillItemEvent e) {
		// TODO Auto-generated method stub
		if ("hfp_specification".equals(e.getItem().getKey())) {
			// this.showErrorMessage("1");
			BillItem billItem = getBillCardPanel().getHeadItem(
					"hfp_specification");

			String sWhere = " select distinct  invspec from bd_invbasdoc "
					+ " where  (def14=0 or def14=1) and dr=0 ";
			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			ArrayList os = new ArrayList();
			try {
				os = (ArrayList) query.executeQuery(sWhere.toString(),
						new ArrayListProcessor());
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (null != os && os.size() > 0) {
				Object[] myValues = new Object[os.size()];
				for (int i = 0; i < os.size(); i++) {
					Object[] gvo = (Object[]) os.get(i);
					if (null != gvo[0]) {
						myValues[i] = gvo[0];
					}
				}
				initComboBox(billItem, myValues, false);

			}

		}
		return true;
	}

	/**
	 * 设置下拉框 values 是值,isWhithIndex 表示是否带下拉索引(即返回的是数字)
	 */

	private void initComboBox(BillItem billItem, Object[] values,
			boolean isWhithIndex) {
		if (billItem != null && billItem.getDataType() == BillItem.COMBO) {
			nc.ui.pub.beans.UIComboBox cmb = (nc.ui.pub.beans.UIComboBox) billItem
					.getComponent();
			cmb.removeAllItems();
			for (int i = 0; i < values.length; i++) {
				cmb.addItem(values[i]);
			}
			billItem.setWithIndex(isWhithIndex);
		}
	}

}

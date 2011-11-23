package nc.ui.dm.so.order;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.print.IExDataSource;
import nc.ui.trade.pub.CardPanelPRTS;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.wl.pub.WdsWlPubConst;

public class MyCardDateSource extends CardPanelPRTS implements IExDataSource {
	private BillCardPanel m_billcardpanel = null;

	public MyCardDateSource(String moduleName, BillCardPanel billcardpanel) {
		super(moduleName, billcardpanel);
		this.m_billcardpanel = billcardpanel;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object[] getObjectByExpress(String itemExpress) {
		if (itemExpress.equals("h_reserve7")) {
			Object[] imageIcon = new Object[1];
			String pk_cumandoc = (String) m_billcardpanel.getHeadItem(
					"pk_cumandoc").getValueObject();
			String classname = "nc.bs.wl.so.order.SoorderBO";
			String methodname = "getCorpImag";
			Class[] ParameterTypes = new Class[] { String.class };
			Object[] ParameterValues = new Object[] { pk_cumandoc };
			try {
				Object obj = LongTimeTask.callRemoteService(
						WdsWlPubConst.WDS_WL_MODULENAME, classname, methodname,
						ParameterTypes, ParameterValues, 2);
				imageIcon[0]=obj;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return imageIcon;
		}
		return null;
	}

	public int getObjectTypeByExpress(String itemExpress) {
		if (itemExpress.equals("h_reserve7"))
			return IExDataSource.IMAGE_TYPE;
		return 0;
	}

}

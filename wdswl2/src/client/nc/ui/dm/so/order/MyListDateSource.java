package nc.ui.dm.so.order;

import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.print.IExDataSource;
import nc.ui.trade.pub.ListPanelPRTS;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.wl.pub.WdsWlPubConst;

public class MyListDateSource extends ListPanelPRTS implements IExDataSource {
	private BillListPanel listpanel;
	public MyListDateSource(String modulecode, BillListPanel bp) {
		super(modulecode, bp);
		this.listpanel = bp;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Object[] getObjectByExpress(String itemExpress){
		if (itemExpress.equals("h_reserve7")) {
			Object[] imageIcon = new Object[0];
			String pk_cumandoc = (String) listpanel.getHeadItem("pk_cumandoc").getValueObject();
			String classname = "nc.bs.wl.so.order.SoorderBO";
			String methodname = "getCorpImag";
			Class[] ParameterTypes = new Class[] { String.class };
			Object[] ParameterValues = new Object[] { pk_cumandoc };
			try {
				Object o = LongTimeTask.callRemoteService(
						WdsWlPubConst.WDS_WL_MODULENAME, classname, methodname,
						ParameterTypes, ParameterValues, 2);
				if("".equals(o)||o.toString().length()==0){
					return null;
				}
				imageIcon[0]=o;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return imageIcon;
		}
		return null;
	}

	public int getObjectTypeByExpress(String itemExpress) {
		if(itemExpress.equals("h_reserve7"))
			return IExDataSource.IMAGE_TYPE;
		
		return 0;
}


}

package nc.ui.wds.w8006080808;

import java.util.ArrayList;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;


/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
 public class MyClientUI extends AbstractMyClientUI{
       
       protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
       
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

	protected void initSelfData() {	}

	public void setDefaultData() throws Exception {
	}
	
@Override
public void afterEdit(BillEditEvent e) {
	// TODO Auto-generated method stub
	if (e.getKey().equals("area")) {
		
		/*Object oo = getBillCardPanel().getBodyValueAt(
				getBillCardPanel().getBillTable().getSelectedRow(),
				"pk_wlxx");*/
		
		//卡片下获得表体的总行数
		ArrayList arr = new ArrayList();
		int rowNum = getBillCardPanel().getBillTable().getRowCount();
		
		
		for(int i=0;i<rowNum;i++){
			String pk_wlxx = (String)getBillCardPanel().getBillModel()
			.getValueAt(i, "pk_wlxx");
			if (!arr.contains(pk_wlxx)) {
				
				arr.add(pk_wlxx);
			} else {
				this.showErrorMessage("此到货站已存在");
				getBillCardPanel().getBillModel().setValueAt(
						"",
						getBillCardPanel().getBillTable()
								.getSelectedRow(), "area");
				getBillCardPanel().getBillModel().setValueAt(
						"",
						getBillCardPanel().getBillTable()
								.getSelectedRow(), "cdhz");
			}}}
	if (e.getKey().equals("qy")) {
		getBillCardPanel().execHeadEditFormulas();
	}
	
	super.afterEdit(e);
}

}

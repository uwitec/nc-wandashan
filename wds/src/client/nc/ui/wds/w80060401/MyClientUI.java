package nc.ui.wds.w80060401;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
 public class MyClientUI extends AbstractMyClientUI implements ListSelectionListener{
       
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
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();	//初始化添加监听
		getBillCardPanel().getBillTable().getSelectionModel().addListSelectionListener(this);
//		getBillListPanel().getHeadTable()
//        .setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getKey().equals("seb_plannum")){
			//获取所选择行的计划数
			Object plannum = getBillCardPanel().getBodyValueAt(getBillCardPanel().getBillTable().getSelectedRow(),"seb_plannum" );
			if(null!=plannum&&!"".equals(plannum)){
				double num = Double.parseDouble(plannum.toString());
				//如果计划数不是null  判断 如果为 0
				if(num==0){
					//清空所选择行的计划数
					getBillCardPanel().setBodyValueAt(null,getBillCardPanel().getBillTable().getSelectedRow(), "seb_plannum");
				}
			}
		}
		
		super.afterEdit(e);
	}
	
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
	}
	


}

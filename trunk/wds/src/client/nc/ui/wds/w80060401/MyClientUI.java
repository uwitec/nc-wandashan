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
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 *
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
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
		super.initEventListener();	//��ʼ����Ӽ���
		getBillCardPanel().getBillTable().getSelectionModel().addListSelectionListener(this);
//		getBillListPanel().getHeadTable()
//        .setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getKey().equals("seb_plannum")){
			//��ȡ��ѡ���еļƻ���
			Object plannum = getBillCardPanel().getBodyValueAt(getBillCardPanel().getBillTable().getSelectedRow(),"seb_plannum" );
			if(null!=plannum&&!"".equals(plannum)){
				double num = Double.parseDouble(plannum.toString());
				//����ƻ�������null  �ж� ���Ϊ 0
				if(num==0){
					//�����ѡ���еļƻ���
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

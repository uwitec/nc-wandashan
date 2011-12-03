package nc.ui.wds.dm.storebing;

import javax.swing.JComponent;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * 
 * <p>
 * �ֲֿ��̰�
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */
public class MyClientUI extends AbstractMyClientUI {

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

	@Override
	protected void initSelfData() {
		//
		ButtonObject btn = getButtonManager().getButton(IBillButton.Line);
		if (btn != null) {
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.CopyLine));
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.PasteLine));
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.InsLine));
		}
	}

	public void setDefaultData() throws Exception {
	}
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		//ֻ�ܿ��������Լ�����ĵ���
		if("areacode".equalsIgnoreCase(e.getKey())){
			JComponent jc = getBillCardPanel().getBodyItem("areacode").getComponent();
			if(jc instanceof UIRefPane){
				UIRefPane ref = (UIRefPane)jc;
				ref.getRefModel().addWherePart("and areaclcode like '00%'");
			}
		}
		return super.beforeEdit(e);
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		String key = e.getKey();
		Object value =e.getValue();
		if ("custcode".equals(key)) {
			//ѡ�����̺�����ֲ���Ϣ
			if(value != null && !"".equals(value)){
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(), "pk_stordoc1");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(), "storname");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(), "storcode");
			}
		}else if ("storcode".equals(key)) {
			//ѡ���ֲֺ����������Ϣ
			if(value != null && !"".equals(value)){
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(), "pk_cumandoc ");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(), "pk_cubasdoc");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(), "custname");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(), "custcode");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(), "conaddr");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(), "areaclname");
			}
		}
	}
	@Override
	protected BusinessDelegator createBusinessDelegator() {
		// TODO Auto-generated method stub
		return new MyDelegator();
	}

	
	
}

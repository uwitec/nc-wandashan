package nc.ui.wds.w8004040208;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;

/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * 
 * <p>
 * ��������
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */
public class MyClientUI extends AbstractMyClientUI implements
		ListSelectionListener {

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

	public void setDefaultData() throws Exception {
	}

	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();
		getBillCardPanel().getBillTable().getSelectionModel()
				.addListSelectionListener(this);
		getBillListPanel().getHeadTable().getSelectionModel()
				.addListSelectionListener(this);
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		int row = e.getRow();
		if (e.getKey().equals("ccunhuobianma")) {
			String code = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(
					row,
					"ccunhuobianma"));
			if (null == code) {
				getBillCardPanel().setBodyValueAt(null,
						row,
						"vbatchcode");
				getBillCardPanel().setBodyValueAt(null,
						row,
						"nshouldoutassistnum");
				getBillCardPanel().setBodyValueAt(null,
						row,
						"nshouldoutnum");
				getBillCardPanel().setBodyValueAt(null,
						row,
						"noutnum");
				getBillCardPanel().setBodyValueAt(null,
						row,
						"noutassistnum");
			}

		}

		super.afterEdit(e);

	}
//zhf  ע�� ��ť״̬�Ϳ����ԵĿ���ͨ��Ȩ�޷�����ʵ��
	public void valueChanged(ListSelectionEvent arg0) {	
		int index = 0;
		TbOutgeneralHVO generalhvo = null;
		if (getBufferData().getVOBufferSize() <= 0) 
		    return;
		index = getBillListPanel().getHeadTable().getSelectedRow();
			if (index == -1) {
				index = 0;
			}
			AggregatedValueObject billvo = getBufferData().getVOByRowNo(index);
			generalhvo = (TbOutgeneralHVO) billvo.getParentVO(); //
		
			int vbillstatus = PuPubVO.getInteger_NullAs(generalhvo.getVbillstatus(), -1);
//			// ǩ�ֺ�
			if (vbillstatus == 0) {
				this.setButtonEnabled(false);
				getButtonManager().getButton(IBillButton.Edit)
						.setEnabled(false);
			} else{ // ǩ��ǰ
				this.setButtonEnabled(true);
				getButtonManager().getButton(IBillButton.Edit)
						.setEnabled(true);
			} 
	}

	/**
	 * ����ǩ�֣�ȡ��ǩ�ֺ��޸İ�ť״̬
	 */
	private void setButtonEnabled(boolean value) {
		getButtonManager().getButton(
				nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr)
				.setEnabled(value);
		getButtonManager().getButton(
				nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz).setEnabled(
				!value);
//		getButtonManager().getButton(IBillButton.Edit).setEnabled(value);
	}

}

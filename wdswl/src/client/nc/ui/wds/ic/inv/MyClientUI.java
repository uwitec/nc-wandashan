package nc.ui.wds.ic.inv;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.WdsSelfButton;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;


/**
 * ���״̬
 * @author Administrator
 *
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
		super.initEventListener();
		getBillListPanel().getHeadTable().getSelectionModel().addListSelectionListener(this);
	}
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		 
	}
	
	protected void initPrivateButton() {
		super.initPrivateButton();
		
		ButtonVO view = new ButtonVO();
		view.setBtnNo(WdsSelfButton.view_lock);
		view.setBtnCode(null);
		view.setBtnName("�󶨲鿴");
		view.setBtnChinaName("�󶨲鿴");
		view.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
		addPrivateButton(view);
		
		
		//���� ��Ч���
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(WdsSelfButton.clean_zero);
		btnVo.setBtnCode(null);
		btnVo.setBtnName("��������");
		btnVo.setBtnChinaName("����");
		btnVo.setBtnName("������");
		btnVo.setHintStr("������Ч���");
		btnVo.setOperateStatus(new 
				int[]{IBillOperate.OP_NOTEDIT,
						IBillOperate.OP_INIT});
		btnVo.setBusinessStatus(new 
				int[]{IBillStatus.FREE});
		addPrivateButton(btnVo);
		
	}

}

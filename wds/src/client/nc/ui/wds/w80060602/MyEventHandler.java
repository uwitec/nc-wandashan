package nc.ui.wds.w80060602;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.SuperVO;

/**
 * 
 * ������AbstractMyEventHandler�������ʵ���࣬ ��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {
	
	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		
		super(billUI, control);
	}
	
	
	
}
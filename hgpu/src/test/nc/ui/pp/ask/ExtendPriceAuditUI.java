package nc.ui.pp.ask;

import nc.ui.trade.manage.ManageEventHandler;

//add by  zhw  2011-06-17  �׸���Ŀ    У��۸��������Ƿ��Ѿ����ɺ�ͬ
public class ExtendPriceAuditUI extends PriceAuditUI{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//�¼�������ʵ��
	ExtendPriceAuditEventHandler eventHandler = null;
	protected ManageEventHandler createEventHandler() {
		if(eventHandler == null){
			eventHandler =  new ExtendPriceAuditEventHandler(this, getUIControl());
		}
		return eventHandler;
	}

}

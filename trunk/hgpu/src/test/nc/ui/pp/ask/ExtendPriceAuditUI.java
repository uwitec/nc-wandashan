package nc.ui.pp.ask;

import nc.ui.trade.manage.ManageEventHandler;

//add by  zhw  2011-06-17  鹤岗项目    校验价格审批单是否已经生成合同
public class ExtendPriceAuditUI extends PriceAuditUI{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//事件处理类实例
	ExtendPriceAuditEventHandler eventHandler = null;
	protected ManageEventHandler createEventHandler() {
		if(eventHandler == null){
			eventHandler =  new ExtendPriceAuditEventHandler(this, getUIControl());
		}
		return eventHandler;
	}

}

package nc.ui.deal.dataset;
/**
 * 发运运计划安排 合格状态期间设置
 */
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.uif.pub.exception.UifException;
import nc.vo.deal.dataset.PlanSetVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds2.set.OutInSetVO;
public class MyClient extends BillManageUI {
	
	private static final long serialVersionUID = 8036305571063747481L;
	public MyClient() {
		super();
		//initialize();
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void init() throws Exception{
		SuperVO[] vos = null;
		try {
			vos = HYPubBO_Client.queryByCondition(PlanSetVO.class, 
					" pk_corp = '"+_getCorp().getPrimaryKey()+"'and isnull(dr,0)=0 ");
		} catch (UifException e) {
			e.printStackTrace();
		}
	//    getBillListPanel().setHeaderValueVO(vos);
		getBufferData().clear();
		// 增加数据到Buffer
		((MyEventHandler)getManageEventHandler()).addDataToBuffer(vos);

		if (getBufferData().getVOBufferSize() != 0) {

			this.setListHeadData(
					getBufferData().getAllHeadVOsFromBuffer());
			this.setBillOperate(IBillOperate.OP_NOTEDIT);
			getBufferData().setCurrentRow(0);
		} else {
			this.setListHeadData(null);
			this.setBillOperate(IBillOperate.OP_INIT);
			getBufferData().setCurrentRow(-1);
		}
	    
	}
	
	@Override
	protected AbstractManageController createController() {		
		return new MyClientController();
	}
	
	@Override
	protected ManageEventHandler createEventHandler() {		
		return new MyEventHandler(this, getUIControl());
	}
	
	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {			
	}
	
	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {	
	}
	
	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {		
	}
	
	@Override
	protected void initSelfData() {	
		
	}
	
	@Override
	public void setDefaultData() throws Exception {
	 getBillCardWrapper().getBillCardPanel().getHeadItem("pk_corp").setValue(getCorpPrimaryKey());			
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		getBillCardPanel().execHeadEditFormulas();
	}
}

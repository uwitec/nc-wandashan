package nc.ui.zb.price.local;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.busi.CustmandocDefaultRefModel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.zb.price.pub.AbstractPricePubUI;
import nc.ui.zb.price.pub.PricePubEventHandler;
import nc.ui.zb.pub.refmodel.BiddingRefModelAll2;
import nc.ui.zb.pub.refmodel.CustManRefModelSelf;
import nc.ui.zb.pub.refmodel.TempCustManRefModel;
import nc.ui.zb.pub.refmodel.TempCustManRefModelAll;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.pub.ZbPubConst;

public class LocalSubmintPriceUI extends AbstractPricePubUI implements BillCardBeforeEditListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LocalSubmintPriceUI(){
		super();
	}
	@Override
	public void init() {
		super.init();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
		initCustManModel();
	}
	@Override
	public String getBillType() {
		// TODO Auto-generated method stub
		return ZbPubConst.ZB_LOCAL_BILLTYPE;
	}
	
	public void bodyRowChange(BillEditEvent e) {

		m_handler.bodyRowChange(e);
	}

	@Override
	public PricePubEventHandler getEventHandler() {
		if(m_handler == null){
			m_handler = new LocalPriceEventHandler(this);
		}
		return m_handler;
	}
	
	@Override
	public void createDataBuffer() {
		m_dataBuffer = new LocalPriceDataBuffer();
	}
	@Override
	public ButtonObject[] getButtonAry() {
		// TODO Auto-generated method stub
		return new ButtonObject[]{m_btnCommit,m_btnCancel,m_btnRefresh};
	}
	/**
	 * 表头编辑前事件处理    
	 */
	public boolean beforeEdit(BillItemEvent e) {
		if("cbiddingid".equalsIgnoreCase(e.getItem().getKey())){
			return beforeBiddingEdit(e);
		}else if("cvendorid".equalsIgnoreCase(e.getItem().getKey())){
			return beforeVendorEdit(e);
		}
		return false;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）编辑标书时考虑供应商是否已存在如存在根据供应商过滤标书
	 * 2011-5-25下午02:00:33
	 * @param e
	 * @return
	 */
	private boolean beforeBiddingEdit(BillItemEvent e){
//		获取供应商 
		String cvendorid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("cvendorid"));
//		if(cvendorid !=  null)
//			return true;
		UIRefPane refpane = (UIRefPane)getBillCardPanel().getHeadItem("cbiddingid").getComponent();
		BiddingRefModelAll2 model = (BiddingRefModelAll2)refpane.getRefModel();
		model.setVendor(cvendorid);
		return true;
	}
	
	private boolean beforeVendorEdit(BillItemEvent e){
//		获取供应商 
		String cbiddingid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("cbiddingid"));
		String cvendorid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("cvendorid"));
		if(cvendorid!=null)
			return true;
		UFBoolean btemp = PuPubVO.getUFBoolean_NullAs(getHeadValue("bistemp"), UFBoolean.FALSE);
		UIRefPane refpane = (UIRefPane)getBillCardPanel().getHeadItem("cvendorid").getComponent();
		AbstractRefModel model = null;
		if(btemp.booleanValue()){//使用临时供应商参照
			if(cbiddingid == null){//使用自定义参照参照
				model = getCustManModelTempAll();
			}else{
			  model = getCustManModelTemp();
			  ((TempCustManRefModel) getCustManModelTemp()).setBiddingid(cbiddingid);
			}
		}else{
			if(cbiddingid == null){//使用系统参照
				model = getCustManModel();
			}else{//使用自定义供应商参照
				((CustManRefModelSelf)getCustManModelSelf()).setBiddingid(cbiddingid);
				model = getCustManModelSelf();
			}
		}
		
		refpane.setRefModel(model);
		return true;
	}
	
	private AbstractRefModel custManModel = null;//系统供应商档案
	private AbstractRefModel custManModelSelf = null;//自定义标书供应商档案
	private AbstractRefModel custManModelTemp = null;//临时供应商档案
	private AbstractRefModel custManModelTempAll = null;//临时供应商档案（ 全部）
	private AbstractRefModel getCustManModel(){
		if(custManModel == null){
			custManModel = new CustmandocDefaultRefModel("供应商档案");
		}
		return custManModel;
	}
	private AbstractRefModel getCustManModelTemp(){
		if(custManModelTemp == null){
			custManModelTemp = new TempCustManRefModel();
		}
		return custManModelTemp;
	}
	private AbstractRefModel getCustManModelSelf(){
		if(custManModelSelf == null){
			custManModelSelf = new CustManRefModelSelf();
		}
		return custManModelSelf;
	}
	
	private AbstractRefModel getCustManModelTempAll(){
		if(custManModelTempAll == null){
			custManModelTempAll = new TempCustManRefModelAll();
		}
		return custManModelTempAll;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）初始化供应商参照   考虑数据注册model
	 * 2011-5-25下午03:03:40
	 */
	private void initCustManModel(){
		UIRefPane refpane = (UIRefPane)getBillCardPanel().getHeadItem("cvendorid").getComponent();
		AbstractRefModel model = refpane.getRefModel();
		if(model instanceof CustmandocDefaultRefModel){
			if(model.getRefNodeName().equalsIgnoreCase("供应商档案")){
				custManModel = model;
			}else 
				custManModel = getCustManModel();
		}else if(model instanceof TempCustManRefModel){
			custManModelTemp = model;
		}else if(model instanceof CustManRefModelSelf){
			custManModelSelf = model;
		}else if(model instanceof TempCustManRefModelAll)
			custManModelTempAll =model;
	}
	
	private Object getHeadValue(String fieldname){
		return getBillCardPanel().getHeadItem(fieldname).getValueObject();
	}
	
	public void clearHeadData(){
		BillItem[] headitmes = getBillCardPanel().getHeadItems();
		for(BillItem item:headitmes){
			if("cbiddingid".equalsIgnoreCase(item.getKey()))
				continue;
			item.setValue(null);
		}
	}
}

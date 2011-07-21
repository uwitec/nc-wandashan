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
	 * ��ͷ�༭ǰ�¼�����    
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
	 * @˵�������׸ڿ�ҵ���༭����ʱ���ǹ�Ӧ���Ƿ��Ѵ�������ڸ��ݹ�Ӧ�̹��˱���
	 * 2011-5-25����02:00:33
	 * @param e
	 * @return
	 */
	private boolean beforeBiddingEdit(BillItemEvent e){
//		��ȡ��Ӧ�� 
		String cvendorid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("cvendorid"));
//		if(cvendorid !=  null)
//			return true;
		UIRefPane refpane = (UIRefPane)getBillCardPanel().getHeadItem("cbiddingid").getComponent();
		BiddingRefModelAll2 model = (BiddingRefModelAll2)refpane.getRefModel();
		model.setVendor(cvendorid);
		return true;
	}
	
	private boolean beforeVendorEdit(BillItemEvent e){
//		��ȡ��Ӧ�� 
		String cbiddingid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("cbiddingid"));
		String cvendorid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("cvendorid"));
		if(cvendorid!=null)
			return true;
		UFBoolean btemp = PuPubVO.getUFBoolean_NullAs(getHeadValue("bistemp"), UFBoolean.FALSE);
		UIRefPane refpane = (UIRefPane)getBillCardPanel().getHeadItem("cvendorid").getComponent();
		AbstractRefModel model = null;
		if(btemp.booleanValue()){//ʹ����ʱ��Ӧ�̲���
			if(cbiddingid == null){//ʹ���Զ�����ղ���
				model = getCustManModelTempAll();
			}else{
			  model = getCustManModelTemp();
			  ((TempCustManRefModel) getCustManModelTemp()).setBiddingid(cbiddingid);
			}
		}else{
			if(cbiddingid == null){//ʹ��ϵͳ����
				model = getCustManModel();
			}else{//ʹ���Զ��幩Ӧ�̲���
				((CustManRefModelSelf)getCustManModelSelf()).setBiddingid(cbiddingid);
				model = getCustManModelSelf();
			}
		}
		
		refpane.setRefModel(model);
		return true;
	}
	
	private AbstractRefModel custManModel = null;//ϵͳ��Ӧ�̵���
	private AbstractRefModel custManModelSelf = null;//�Զ�����鹩Ӧ�̵���
	private AbstractRefModel custManModelTemp = null;//��ʱ��Ӧ�̵���
	private AbstractRefModel custManModelTempAll = null;//��ʱ��Ӧ�̵����� ȫ����
	private AbstractRefModel getCustManModel(){
		if(custManModel == null){
			custManModel = new CustmandocDefaultRefModel("��Ӧ�̵���");
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
	 * @˵�������׸ڿ�ҵ����ʼ����Ӧ�̲���   ��������ע��model
	 * 2011-5-25����03:03:40
	 */
	private void initCustManModel(){
		UIRefPane refpane = (UIRefPane)getBillCardPanel().getHeadItem("cvendorid").getComponent();
		AbstractRefModel model = refpane.getRefModel();
		if(model instanceof CustmandocDefaultRefModel){
			if(model.getRefNodeName().equalsIgnoreCase("��Ӧ�̵���")){
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

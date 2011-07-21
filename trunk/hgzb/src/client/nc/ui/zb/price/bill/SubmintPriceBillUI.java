package nc.ui.zb.price.bill;

import nc.bs.logging.Logger;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.busi.CustmandocDefaultRefModel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.zb.pub.ZbPubHelper;
import nc.ui.zb.pub.refmodel.BiddingRefModelAll2;
import nc.ui.zb.pub.refmodel.CustManRefModelSelf;
import nc.ui.zb.pub.refmodel.TempCustManRefModel;
import nc.ui.zb.pub.refmodel.TempCustManRefModelAll;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.zb.price.bill.SubmitPriceBodyVO;
import nc.vo.zb.pub.ZbPuBtnConst;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class SubmintPriceBillUI extends BillManageUI implements BillCardBeforeEditListener,ILinkQuery{
	
	private Class bodyVOClass;
	
	private Class bodyVO1Class;
	
	private Class aggBillVOClass;
	
	
	public SubmintPriceBillUI() {
		super();
	initLogInfor();
}

public Object[] m_logInfor = null;// 

private void initLogInfor() {
	try {
		m_logInfor = ZbPubHelper.getLogInfor(_getCorp().getPrimaryKey(),
				_getOperator());
	} catch (Exception e) {
		e.printStackTrace();// ��ȡ������Ϣʧ�� ����Ӱ��������
		m_logInfor = null;
	}
}
	@Override
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		return new ClientController();
	}
	
	/**
	 * ʵ��������༭ǰ���¼�����, ��������¼�������Ҫ���ظ÷��� �������ڣ�(2004-1-3 18:13:36)
	 */
	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub
	}
	
	// ����Զ��尴ť
	public void initPrivateButton() {
		// ����
		ButtonVO btnvo4 = new ButtonVO();
		btnvo4.setBtnNo(ZbPuBtnConst.LINKQUERY);
		btnvo4.setBtnName("����");
		btnvo4.setBtnChinaName("����");
		btnvo4.setBtnCode(null);// code�������Ϊ��
		btnvo4.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
				IBillOperate.OP_NO_ADDANDEDIT });
		addPrivateButton(btnvo4);
		
		// ������ѯ
		ButtonVO btnvo8 = new ButtonVO();
		btnvo8.setBtnNo(ZbPuBtnConst.ASSQUERY);
		btnvo8.setBtnName("������ѯ");
		btnvo8.setBtnChinaName("������ѯ");
		btnvo8.setBtnCode(null);// code�������Ϊ��
		btnvo8.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		btnvo8.setChildAry(new int[] { ZbPuBtnConst.LINKQUERY,IBillButton.ApproveInfo});
		addPrivateButton(btnvo8);
		
		// ��ӡ����
		ButtonVO btnvo9 = new ButtonVO();
		btnvo9.setBtnNo(ZbPuBtnConst.ASSPRINT);
		btnvo9.setBtnName("��ӡ����");
		btnvo9.setBtnChinaName("��ӡ����");
		btnvo9.setBtnCode(null);// code�������Ϊ��
		btnvo9.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		btnvo9.setChildAry(new int[] { IBillButton.Print,IBillButton.DirectPrint});
		addPrivateButton(btnvo9);

		//�޸�
		ButtonVO btnvo10 = new ButtonVO();
		btnvo10.setBtnNo(ZbPuBtnConst.Editor);
		btnvo10.setBtnName("�޸�");
		btnvo10.setBtnChinaName("�޸�");
		btnvo10.setBtnCode(null);// code�������Ϊ��
		btnvo10.setOperateStatus(new int[] { IBillOperate.OP_INIT,IBillOperate.OP_NOTEDIT});
		btnvo10.setBusinessStatus(new int[]{IBillStatus.FREE});
		addPrivateButton(btnvo10);
		
		ButtonVO btnvo6 = new ButtonVO();
		btnvo6.setBtnNo(ZbPuBtnConst.REVISED);
		btnvo6.setBtnName("�޶�");
		btnvo6.setBtnChinaName("�޶�");
		btnvo6.setBtnCode(null);// code�������Ϊ��
		btnvo6.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		btnvo6.setBusinessStatus( new int[]{IBillStatus.CHECKPASS});
		addPrivateButton(btnvo6);
		super.initPrivateButton();
	}

	@Override
	protected void initSelfData() {
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
		initCustManModel();
	}

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("vbillstatus", IBillStatus.FREE);// ����״̬
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPrimaryKey());
		getBillCardPanel().setHeadItem("pk_billtype", ZbPubConst.ZB_SUBMIT_BILL);
		getBillCardPanel().setTailItem("voperatorid", _getOperator());// �Ƶ���
		getBillCardPanel().setTailItem("dmakedate", _getDate());// �Ƶ�����
		getBillCardPanel().setHeadItem("dbilldate", _getDate());	
		
		if (m_logInfor==null || m_logInfor.length==0 ) {
			showHintMessage("��ǰ�û�δ���ù���ҵ��Ա");
			return;
		}
		getBillCardPanel().setHeadItem("pk_deptdoc", m_logInfor[1]);
		getBillCardPanel().setHeadItem("vemployeeid", m_logInfor[0]);
	}
	
	/**
	 * ʵ����ǰ̨�����ҵ��ί����
	 * ��������¼�������Ҫ���ظ÷���
	 * �������ڣ�(2004-1-3 18:13:36)
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new SubmitPriceDelegator();
	}
	
	/**
	 * �༭���¼��� �������ڣ�(2001-3-23 2:02:27)
	 *
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
		String key = e.getKey();
		if(e.getPos() == BillItem.HEAD){
			if(key.equalsIgnoreCase("cbiddingid")){//����ѡ���
				afterEditWhenBidding(e);
			}else if("venname".equalsIgnoreCase(key)){//��Ӧ��ѡ���
				afterEditWhenVendor(e);
				getBillCardPanel().execHeadFormulas(new String[]
				         {"ccustbasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,venname)",
						  "cvendorid->venname"});
			}else if("temp".equalsIgnoreCase(key)){//��ʱ��Ӧ��ѡ���
				afterEditWhenTemp(e);
				getBillCardPanel().execHeadFormulas(new String[]{"cvendorid->temp"});
			}else if("bistemp".equalsIgnoreCase(key)){
				setHeadItemValue(null);
				getHeadItem("venname").setEnabled(!PuPubVO.getUFBoolean_NullAs(getHeadValue("bistemp"),UFBoolean.FALSE).booleanValue());
				getHeadItem("temp").setEnabled(PuPubVO.getUFBoolean_NullAs(getHeadValue("bistemp"),UFBoolean.FALSE).booleanValue());
			}else if("pk_deptdoc".equalsIgnoreCase(key)){//����  ���ҵ��Ա
				getBillCardPanel().setHeadItem("vemployeeid",null);
			}else if ("vemployeeid".equalsIgnoreCase(key)) {// ҵ��Ա  ��������
				String deptdoc = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("pk_deptdoc").getValueObject());
				if(deptdoc ==null)
				    getBillCardPanel().execHeadTailEditFormulas(getBillCardPanel().getHeadItem("vemployeeid"));
			}
		}
		super.afterEdit(e);
	}
	
	private void clearData(){
		int count = getBillCardPanel().getBillModel().getRowCount();
		int[] array = new int[count];
		for (int j = 0; j < count; j++) {
			array[j] = j;
		}
		getBillCardPanel().getBillData().getBillModel().delLine(array);
	}
	
	private Object getHeadValue(String fieldname){
		return getBillCardPanel().getHeadItem(fieldname).getValueObject();
	}
	
	private SubmitPriceDelegator getDelegator(){
		return (SubmitPriceDelegator)getBusiDelegator();
	}
	private void afterEditWhenBidding(BillEditEvent e){
//      У������  				
		String cbiddingid = PuPubVO.getString_TrimZeroLenAsNull(e.getValue());
		if(cbiddingid == null){//��ձ���
			clearData();//�������
			setHeadItemValue(null);
			getHeadItem("bistemp").setValue(null);
			getHeadItem("temp").setEnabled(false);
			getHeadItem("venname").setEnabled(true);
			return;
		}
		UIRefPane refpane = (UIRefPane)getBillCardPanel().getHeadItem("cbiddingid").getComponent();
		cbiddingid = refpane.getRefPK();

		try{
			loadBiddingInvInfor(cbiddingid);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			getBillCardPanel().getHeadItem("cbiddingid").setValue(null);
			showErrorMessage("���ر��Ʒ�����ݳ���,"+ZbPubTool.getString_NullAsTrimZeroLen(e1.getMessage()));
		}
		
	}
	
	public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
		if(e.getKey().equalsIgnoreCase("nprice")){
			String cvendorid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("cvendorid"));
			String cbiddingid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("cbiddingid"));
//			if(cvendorid == null){
//				showHintMessage("��ѡ��Ӧ��");
//				return false;
//			}
			if(cbiddingid == null){
				showHintMessage("��ѡ����");
				return false;
			}				
		}
		return true;
	}
	
	private void afterEditWhenVendor(BillEditEvent e){
//		��Ӧ��ѡ���   ˢ�±���
		UIRefPane refpane = (UIRefPane)getBillCardPanel().getHeadItem("venname").getComponent();
		String cvendorid = PuPubVO.getString_TrimZeroLenAsNull(refpane.getRefPK());
		if(cvendorid == null){
			//��ձ���
			clearData();//�������
			return;
		}
		
		String cbiddingid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("cbiddingid"));
		
		if(cbiddingid == null){
			showHintMessage("��ѡ�����");
			return;		
		}
		
//		loadBiddingInvInfor(cbiddingid, cvendorid);
	}
	
	private void afterEditWhenTemp(BillEditEvent e){
//		��Ӧ��ѡ���   ˢ�±���
		UIRefPane refpane = (UIRefPane)getBillCardPanel().getHeadItem("temp").getComponent();
		String cvendorid = PuPubVO.getString_TrimZeroLenAsNull(refpane.getRefPK());
		if(cvendorid == null){
			//��ձ���
			clearData();//�������
			return;
		}
		
		String cbiddingid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("cbiddingid"));
		
		if(cbiddingid == null){
			showHintMessage("��ѡ�����");
			return;		
		}
		
//		loadBiddingInvInfor(cbiddingid, cvendorid);
	}
	
	private void loadBiddingInvInfor(String cbiddingid) throws Exception{
//		try {
			SubmitPriceBodyVO[] bodys = getDelegator().loadBiddingInvInfor(cbiddingid,this);
			ZbPubTool.setVOsRowNoByRule(bodys, "crowno");
			getBillCardPanel().getBillModel().setBodyDataVO(bodys);
			getBillCardPanel().getBillModel().execLoadFormula();
		
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
		UIRefPane refpane = (UIRefPane)getBillCardPanel().getHeadItem("cbiddingid").getComponent();
		BiddingRefModelAll2 model = (BiddingRefModelAll2)refpane.getRefModel();
		model.addWherePart(" and zb_bidding_h.ibusstatus in ("+ZbPubConst.BIDDING_BUSINESS_STATUE_INIT+","+ZbPubConst.BIDDING_BUSINESS_STATUE_SUBMIT+")" );
		model.setVendor(cvendorid);
		return true;
	}
	
	private boolean beforeTempEdit(BillItemEvent e){
//		��ȡ��Ӧ�� 
		String cbiddingid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("cbiddingid"));
		String cvendorid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("temp"));
		if(cvendorid!=null)
			return true;
		UIRefPane refpane = (UIRefPane)getBillCardPanel().getHeadItem("temp").getComponent();
		AbstractRefModel model = null;
		if(cbiddingid == null){//ʹ���Զ������ʱ��Ӧ��
			model = getCustManModelTempAll();
		}else{//ʹ���Զ��幩Ӧ�̲��գ��������飩
			((TempCustManRefModel) getCustManModelTemp()).setBiddingid(cbiddingid);
			model = getCustManModelTemp();
		}
		refpane.setRefModel(model);
		return true;
	}
	
	private AbstractRefModel custManModel = null;//ϵͳ��Ӧ�̵���
	private AbstractRefModel custManModelSelf = null;//�Զ�����鹩Ӧ�̵���
	private AbstractRefModel custManModelTempBid = null;//��ʱ��Ӧ�̵������������飩
	private AbstractRefModel custManModelTempAll = null;//��ʱ��Ӧ�̵����� ȫ����
	private AbstractRefModel getCustManModel(){
		if(custManModel == null){
			custManModel = new CustmandocDefaultRefModel("��Ӧ�̵���");
		}
		return custManModel;
	}
	private AbstractRefModel getCustManModelTemp(){
		if(custManModelTempBid == null){
			custManModelTempBid = new TempCustManRefModel();
		}
		return custManModelTempBid;
	}
	
	private AbstractRefModel getCustManModelTempAll(){
		if(custManModelTempAll == null){
			custManModelTempAll = new TempCustManRefModelAll();
		}
		return custManModelTempAll;
	}
	
	private AbstractRefModel getCustManModelSelf(){
		if(custManModelSelf == null){
			custManModelSelf = new CustManRefModelSelf();
		}
		return custManModelSelf;
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
			custManModelTempBid = model;
		}else if(model instanceof CustManRefModelSelf){
			custManModelSelf = model;
		}else if(model instanceof TempCustManRefModelAll)
			custManModelTempAll =model;
			
	}
	
	

	/**
	 * ��ͷ�༭ǰ�¼�����    
	 */
	public boolean beforeEdit(BillItemEvent e) {
		if("cbiddingid".equalsIgnoreCase(e.getItem().getKey())){//����
			return beforeBiddingEdit(e);
		}else if("venname".equalsIgnoreCase(e.getItem().getKey())){//��Ӧ��
			return beforeVendorEdit(e);
		}else if("temp".equalsIgnoreCase(e.getItem().getKey())){//��ʱ��Ӧ��
			return beforeTempEdit(e);
		}else if ("vemployeeid".equalsIgnoreCase(e.getItem().getKey())) {//ҵ��Ա    ���ݲ��Ź���ҵ��Ա
			String deptdoc = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("pk_deptdoc").getValueObject());
			UIRefPane invRefPane = (UIRefPane) e.getItem().getComponent();
			AbstractRefModel refModel = (AbstractRefModel) invRefPane.getRefModel();
			if (deptdoc == null) {
				refModel.addWherePart(" and 1=1");
				return true;
			}
			refModel.addWherePart("  and bd_psndoc.pk_deptdoc = '" + deptdoc + "'");
		}
		return false;
	}
	
	private void setHeadItemValue(Object value){
		getHeadItem("cvendorid").setValue(value);
		getHeadItem("ccustbasid").setValue(value);
		getHeadItem("venname").setValue(value);
		getHeadItem("vname").setValue(value);
		getHeadItem("temp").setValue(value);
		getHeadItem("temp1").setValue(value);
	}
	
	private BillItem getHeadItem(String key){
		return getBillCardPanel().getHeadItem(key);
	}

	private boolean beforeVendorEdit(BillItemEvent e){
//		��ȡ��Ӧ�� 
		String cbiddingid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("cbiddingid"));
		String cvendorid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("venname"));
		if(cvendorid!=null)
			return true;
		UIRefPane refpane = (UIRefPane)getBillCardPanel().getHeadItem("venname").getComponent();
		AbstractRefModel model = null;
			if(cbiddingid == null){//ʹ��ϵͳ����
				model = getCustManModel();
			}else{//ʹ���Զ��幩Ӧ�̲���
				((CustManRefModelSelf)getCustManModelSelf()).setBiddingid(cbiddingid);
				model = getCustManModelSelf();
			}
		refpane.setRefModel(model);
		return true;
	}
	/**
	 * ���鶯��
	 */
	public void doQueryAction(ILinkQueryData querydata) {
		try {
			if(querydata == null)
				return;
			String id = querydata.getBillID();
			if(id == null || "".equals(id))
				return;
			SuperVO headvo = (SuperVO)getBodyB1Class().newInstance();
			//��ѯ��������
			SuperVO[] queryVos = getBusiDelegator().queryHeadAllData(
					getBodyB1Class(),
					getUIControl().getBillType(), " "+headvo.getTableName()+"."+getUIControl().getPkField()+" = '"+id+"' ");
			//��ѯ�ӱ�����
			if(queryVos != null && queryVos.length > 0){
					setCurrentPanel(BillTemplateWrapper.CARDPANEL);
					AggregatedValueObject aggvo = (AggregatedValueObject)getAggVOClass().newInstance();
					aggvo.setParentVO(queryVos[0]);
					getBusiDelegator().setChildData(
								aggvo,
								getBodyVOClass(),
								getUIControl().getBillType(),
								queryVos[0].getPrimaryKey(),null);
					getBufferData().addVOToBuffer(aggvo);
					getBufferData().setCurrentRow(getBufferData().getCurrentRow());
			}
			//
			ButtonObject[] btns = getButtons();
			for (ButtonObject btn : btns) {
				if (("" + (IBillButton.Card)).equals(btn.getTag()) || ("" + (IBillButton.Return)).equals(btn.getTag())) {
					btn.setEnabled(true);
					btn.setVisible(true);
				} else {
					btn.setEnabled(false);
					btn.setVisible(false);
				}
			}
			updateButtons();
		}  catch (Exception e) {
			Logger.error(e);
		}
		//
	}
	
	
	private Class getBodyVOClass() throws Exception{
		if(bodyVOClass==null)
			bodyVOClass = Class.forName(getUIControl().getBillVoName()[2]);
		return bodyVOClass;
	}
	
	private Class getAggVOClass()  throws Exception{
		if(aggBillVOClass == null)
				aggBillVOClass = Class.forName(getUIControl().getBillVoName()[0]);
			return aggBillVOClass;
	}
	private Class getBodyB1Class()  throws Exception{
		if( bodyVO1Class == null)
			bodyVO1Class = Class.forName(getUIControl().getBillVoName()[1]);
			return bodyVO1Class ;
	}
}

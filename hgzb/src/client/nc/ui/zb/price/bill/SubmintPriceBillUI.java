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
		e.printStackTrace();// 获取申请信息失败 并不影响界面加载
		m_logInfor = null;
	}
}
	@Override
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		return new ClientController();
	}
	
	/**
	 * 实例化界面编辑前后事件处理, 如果进行事件处理需要重载该方法 创建日期：(2004-1-3 18:13:36)
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
	
	// 添加自定义按钮
	public void initPrivateButton() {
		// 联查
		ButtonVO btnvo4 = new ButtonVO();
		btnvo4.setBtnNo(ZbPuBtnConst.LINKQUERY);
		btnvo4.setBtnName("联查");
		btnvo4.setBtnChinaName("联查");
		btnvo4.setBtnCode(null);// code最好设置为空
		btnvo4.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
				IBillOperate.OP_NO_ADDANDEDIT });
		addPrivateButton(btnvo4);
		
		// 辅助查询
		ButtonVO btnvo8 = new ButtonVO();
		btnvo8.setBtnNo(ZbPuBtnConst.ASSQUERY);
		btnvo8.setBtnName("辅助查询");
		btnvo8.setBtnChinaName("辅助查询");
		btnvo8.setBtnCode(null);// code最好设置为空
		btnvo8.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		btnvo8.setChildAry(new int[] { ZbPuBtnConst.LINKQUERY,IBillButton.ApproveInfo});
		addPrivateButton(btnvo8);
		
		// 打印管理
		ButtonVO btnvo9 = new ButtonVO();
		btnvo9.setBtnNo(ZbPuBtnConst.ASSPRINT);
		btnvo9.setBtnName("打印管理");
		btnvo9.setBtnChinaName("打印管理");
		btnvo9.setBtnCode(null);// code最好设置为空
		btnvo9.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		btnvo9.setChildAry(new int[] { IBillButton.Print,IBillButton.DirectPrint});
		addPrivateButton(btnvo9);

		//修改
		ButtonVO btnvo10 = new ButtonVO();
		btnvo10.setBtnNo(ZbPuBtnConst.Editor);
		btnvo10.setBtnName("修改");
		btnvo10.setBtnChinaName("修改");
		btnvo10.setBtnCode(null);// code最好设置为空
		btnvo10.setOperateStatus(new int[] { IBillOperate.OP_INIT,IBillOperate.OP_NOTEDIT});
		btnvo10.setBusinessStatus(new int[]{IBillStatus.FREE});
		addPrivateButton(btnvo10);
		
		ButtonVO btnvo6 = new ButtonVO();
		btnvo6.setBtnNo(ZbPuBtnConst.REVISED);
		btnvo6.setBtnName("修订");
		btnvo6.setBtnChinaName("修订");
		btnvo6.setBtnCode(null);// code最好设置为空
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
		getBillCardPanel().setHeadItem("vbillstatus", IBillStatus.FREE);// 单据状态
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPrimaryKey());
		getBillCardPanel().setHeadItem("pk_billtype", ZbPubConst.ZB_SUBMIT_BILL);
		getBillCardPanel().setTailItem("voperatorid", _getOperator());// 制单人
		getBillCardPanel().setTailItem("dmakedate", _getDate());// 制单日期
		getBillCardPanel().setHeadItem("dbilldate", _getDate());	
		
		if (m_logInfor==null || m_logInfor.length==0 ) {
			showHintMessage("当前用户未设置关联业务员");
			return;
		}
		getBillCardPanel().setHeadItem("pk_deptdoc", m_logInfor[1]);
		getBillCardPanel().setHeadItem("vemployeeid", m_logInfor[0]);
	}
	
	/**
	 * 实例化前台界面的业务委托类
	 * 如果进行事件处理需要重载该方法
	 * 创建日期：(2004-1-3 18:13:36)
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new SubmitPriceDelegator();
	}
	
	/**
	 * 编辑后事件。 创建日期：(2001-3-23 2:02:27)
	 *
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
		String key = e.getKey();
		if(e.getPos() == BillItem.HEAD){
			if(key.equalsIgnoreCase("cbiddingid")){//标书选择后
				afterEditWhenBidding(e);
			}else if("venname".equalsIgnoreCase(key)){//供应商选择后
				afterEditWhenVendor(e);
				getBillCardPanel().execHeadFormulas(new String[]
				         {"ccustbasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,venname)",
						  "cvendorid->venname"});
			}else if("temp".equalsIgnoreCase(key)){//临时供应商选择后
				afterEditWhenTemp(e);
				getBillCardPanel().execHeadFormulas(new String[]{"cvendorid->temp"});
			}else if("bistemp".equalsIgnoreCase(key)){
				setHeadItemValue(null);
				getHeadItem("venname").setEnabled(!PuPubVO.getUFBoolean_NullAs(getHeadValue("bistemp"),UFBoolean.FALSE).booleanValue());
				getHeadItem("temp").setEnabled(PuPubVO.getUFBoolean_NullAs(getHeadValue("bistemp"),UFBoolean.FALSE).booleanValue());
			}else if("pk_deptdoc".equalsIgnoreCase(key)){//部门  清空业务员
				getBillCardPanel().setHeadItem("vemployeeid",null);
			}else if ("vemployeeid".equalsIgnoreCase(key)) {// 业务员  带出部门
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
//      校验数据  				
		String cbiddingid = PuPubVO.getString_TrimZeroLenAsNull(e.getValue());
		if(cbiddingid == null){//清空标书
			clearData();//清空数据
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
			showErrorMessage("加载标段品种数据出错,"+ZbPubTool.getString_NullAsTrimZeroLen(e1.getMessage()));
		}
		
	}
	
	public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
		if(e.getKey().equalsIgnoreCase("nprice")){
			String cvendorid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("cvendorid"));
			String cbiddingid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("cbiddingid"));
//			if(cvendorid == null){
//				showHintMessage("请选择供应商");
//				return false;
//			}
			if(cbiddingid == null){
				showHintMessage("请选择标段");
				return false;
			}				
		}
		return true;
	}
	
	private void afterEditWhenVendor(BillEditEvent e){
//		供应商选择后   刷新表体
		UIRefPane refpane = (UIRefPane)getBillCardPanel().getHeadItem("venname").getComponent();
		String cvendorid = PuPubVO.getString_TrimZeroLenAsNull(refpane.getRefPK());
		if(cvendorid == null){
			//清空标书
			clearData();//清空数据
			return;
		}
		
		String cbiddingid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("cbiddingid"));
		
		if(cbiddingid == null){
			showHintMessage("请选择标书");
			return;		
		}
		
//		loadBiddingInvInfor(cbiddingid, cvendorid);
	}
	
	private void afterEditWhenTemp(BillEditEvent e){
//		供应商选择后   刷新表体
		UIRefPane refpane = (UIRefPane)getBillCardPanel().getHeadItem("temp").getComponent();
		String cvendorid = PuPubVO.getString_TrimZeroLenAsNull(refpane.getRefPK());
		if(cvendorid == null){
			//清空标书
			clearData();//清空数据
			return;
		}
		
		String cbiddingid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("cbiddingid"));
		
		if(cbiddingid == null){
			showHintMessage("请选择标书");
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
	 * @说明：（鹤岗矿业）编辑标书时考虑供应商是否已存在如存在根据供应商过滤标书
	 * 2011-5-25下午02:00:33
	 * @param e
	 * @return
	 */
	private boolean beforeBiddingEdit(BillItemEvent e){
//		获取供应商 
		String cvendorid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("cvendorid"));
		UIRefPane refpane = (UIRefPane)getBillCardPanel().getHeadItem("cbiddingid").getComponent();
		BiddingRefModelAll2 model = (BiddingRefModelAll2)refpane.getRefModel();
		model.addWherePart(" and zb_bidding_h.ibusstatus in ("+ZbPubConst.BIDDING_BUSINESS_STATUE_INIT+","+ZbPubConst.BIDDING_BUSINESS_STATUE_SUBMIT+")" );
		model.setVendor(cvendorid);
		return true;
	}
	
	private boolean beforeTempEdit(BillItemEvent e){
//		获取供应商 
		String cbiddingid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("cbiddingid"));
		String cvendorid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("temp"));
		if(cvendorid!=null)
			return true;
		UIRefPane refpane = (UIRefPane)getBillCardPanel().getHeadItem("temp").getComponent();
		AbstractRefModel model = null;
		if(cbiddingid == null){//使用自定义的临时供应商
			model = getCustManModelTempAll();
		}else{//使用自定义供应商参照（关联标书）
			((TempCustManRefModel) getCustManModelTemp()).setBiddingid(cbiddingid);
			model = getCustManModelTemp();
		}
		refpane.setRefModel(model);
		return true;
	}
	
	private AbstractRefModel custManModel = null;//系统供应商档案
	private AbstractRefModel custManModelSelf = null;//自定义标书供应商档案
	private AbstractRefModel custManModelTempBid = null;//临时供应商档案（关联标书）
	private AbstractRefModel custManModelTempAll = null;//临时供应商档案（ 全部）
	private AbstractRefModel getCustManModel(){
		if(custManModel == null){
			custManModel = new CustmandocDefaultRefModel("供应商档案");
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
			custManModelTempBid = model;
		}else if(model instanceof CustManRefModelSelf){
			custManModelSelf = model;
		}else if(model instanceof TempCustManRefModelAll)
			custManModelTempAll =model;
			
	}
	
	

	/**
	 * 表头编辑前事件处理    
	 */
	public boolean beforeEdit(BillItemEvent e) {
		if("cbiddingid".equalsIgnoreCase(e.getItem().getKey())){//标书
			return beforeBiddingEdit(e);
		}else if("venname".equalsIgnoreCase(e.getItem().getKey())){//供应商
			return beforeVendorEdit(e);
		}else if("temp".equalsIgnoreCase(e.getItem().getKey())){//临时供应商
			return beforeTempEdit(e);
		}else if ("vemployeeid".equalsIgnoreCase(e.getItem().getKey())) {//业务员    根据部门过滤业务员
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
//		获取供应商 
		String cbiddingid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("cbiddingid"));
		String cvendorid = PuPubVO.getString_TrimZeroLenAsNull(getHeadValue("venname"));
		if(cvendorid!=null)
			return true;
		UIRefPane refpane = (UIRefPane)getBillCardPanel().getHeadItem("venname").getComponent();
		AbstractRefModel model = null;
			if(cbiddingid == null){//使用系统参照
				model = getCustManModel();
			}else{//使用自定义供应商参照
				((CustManRefModelSelf)getCustManModelSelf()).setBiddingid(cbiddingid);
				model = getCustManModelSelf();
			}
		refpane.setRefModel(model);
		return true;
	}
	/**
	 * 联查动作
	 */
	public void doQueryAction(ILinkQueryData querydata) {
		try {
			if(querydata == null)
				return;
			String id = querydata.getBillID();
			if(id == null || "".equals(id))
				return;
			SuperVO headvo = (SuperVO)getBodyB1Class().newInstance();
			//查询主表数据
			SuperVO[] queryVos = getBusiDelegator().queryHeadAllData(
					getBodyB1Class(),
					getUIControl().getBillType(), " "+headvo.getTableName()+"."+getUIControl().getPkField()+" = '"+id+"' ");
			//查询子表数据
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

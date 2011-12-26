package nc.ui.wds.ic.so.out;
import javax.swing.JComponent;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeListener;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.ButtonVOFactory;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.ic.pub.OutPubClientUI;
import nc.ui.wds.w80020206.buttun0206.QxqzBtn;
import nc.ui.wds.w80020206.buttun0206.QzqrBtn;
import nc.ui.wds.w8004040204.ssButtun.ckmxBtn;
import nc.ui.wds.w8004040204.ssButtun.fzgnBtn;
import nc.ui.wds.w8004040204.ssButtun.tpzdBtn;
import nc.ui.wds.w8004040204.ssButtun.zdqhBtn;
import nc.ui.wds.w80060206.buttun0206.ISsButtun;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.field.IBillField;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * <b> 销售出库 </b>
 */
public class MyClientUI extends OutPubClientUI implements BillCardBeforeEditListener, ChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6680473320457515722L;
	
	private String curRefBilltype=null;
	
	public MyClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}

	public MyClientUI() {
		super();
	}
//	private Map<String,List<TbOutgeneralTVO>> trayInfor = null;//缓存下  保存后更新到 buffer
	
	protected ManageEventHandler createEventHandler() {
		return new MySaleEventHandler(this, getUIControl());
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
	
	protected BusinessDelegator createBusinessDelegator() {
		return new MyDelegator();
	}

	protected void initSelfData() {
		getBillListPanel().getHeadTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);//单选
		getBillListPanel().getBodyTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		getBillCardPanel().getBillTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		// 增加页签切换监听
		UITabbedPane m_CardUITabbedPane = getBillCardPanel().getBodyTabbedPane();
		m_CardUITabbedPane.addChangeListener( this);
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
		

	}

	
	
	@Override
	public boolean beforeEdit(BillEditEvent e) {
//		String key=e.getKey();
//		if(key==null){
//			return false;
//		}
//		//过滤存货分类只属于粉类 的存货
//		if(key.equalsIgnoreCase("invcode")){
//			JComponent c =getBillCardPanel().getBodyItem("invcode").getComponent();
//			if( c instanceof UIRefPane){
//				UIRefPane ref = (UIRefPane)c;
//				
//				ref.getRefModel().addWherePart(" and bd_invbasdoc.pk_invcl in " +
//						"(select bd_invcl.pk_invcl from bd_invcl where bd_invcl.invclasscode like '30101%')" +
//						"    and isnull(bd_invmandoc.dr,0) = 0");
//			}		
//		}
		return true;
	}

	@Override
	protected String getBillNo() throws Exception {
		String billno = HYPubBO_Client.getBillNo(WdsWlPubConst.BILLTYPE_SALE_OUT, _getCorp().getPrimaryKey(), null, null);
		return billno;
	}
	
	public void setDefaultData() {
		
		getBillCardPanel().setTailItem("tmaketime", _getServerTime().toString());
		
		try {
			getBillCardPanel().setHeadItem("vbillcode", getBillNo());
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		getBillCardPanel().setTailItem("tlastmoditime", _getServerTime().toString());
		
		getBillCardPanel().setTailItem("clastmodiid", _getOperator());
		// 设置库管员
		getBillCardPanel().getHeadItem("cwhsmanagerid").setValue(_getOperator());
		// 设置制单人
		getBillCardPanel().getHeadTailItem("coperatorid").setValue(_getOperator());
		// 单据日期
		getBillCardPanel().getHeadTailItem("dbilldate").setValue(_getDate().toString());
		//单据类型
		getBillCardPanel().getHeadTailItem("vbilltype").setValue(WdsWlPubConst.BILLTYPE_SALE_OUT);
		//公司
		getBillCardPanel().getHeadTailItem("pk_corp").setValue(_getCorp().getPrimaryKey());
		//单据状态
		
		getBillCardPanel().getHeadTailItem("vbillstatus").setValue(IBillStatus.FREE); //自由状态
	}
	@Override
	protected IBillField createBillField() {
		return new nc.ui.wds.ic.other.out.BillField();
	}
	
	protected AbstractManageController createController() {
		return new MyClientUICtrl();
	}
	/**
	 * 注册自定义按钮
	 */
	protected void initPrivateButton() {
		super.initPrivateButton();
		fzgnBtn customizeButton1 = new fzgnBtn();//辅助功能
		addPrivateButton(customizeButton1.getButtonVO());
		tpzdBtn customizeButton2 = new tpzdBtn();//手动拣货
		addPrivateButton(customizeButton2.getButtonVO());
		zdqhBtn customizeButton3 = new zdqhBtn();//自动拣货
		addPrivateButton(customizeButton3.getButtonVO());
		ckmxBtn customizeButton4 = new ckmxBtn();//查看明细
		addPrivateButton(customizeButton4.getButtonVO());

		QxqzBtn customizeButton5 = new QxqzBtn();//取消签字
		addPrivateButton(customizeButton5.getButtonVO());
		QzqrBtn customizeButton6 = new QzqrBtn();//签字确认
		addPrivateButton(customizeButton6.getButtonVO());
		getButtonManager().getButtonAry(new int[]{ISsButtun.Qzqr,ISsButtun.Qxqz});//取消签字,签字确认
		
		ButtonVO soOrder = new ButtonVO();
		soOrder.setBtnNo(nc.ui.wds.w80020206.buttun0206.ISsButtun.RefSoOrder);
		soOrder.setBtnCode(null);
		soOrder.setBtnName("销售运单");
		soOrder.setBtnChinaName("销售运单");
		addPrivateButton(soOrder);
//		ButtonVO redSoorder = new ButtonVO();
//		redSoorder.setBtnNo(nc.ui.wds.w80020206.buttun0206.ISsButtun.RefRedSoOrder);
//		redSoorder.setBtnCode(null);
//		redSoorder.setBtnName("退货入库");
//		redSoorder.setBtnChinaName("退货入库");
//		addPrivateButton(redSoorder);
		ButtonVO refbill =ButtonVOFactory.getInstance().build(IBillButton.Refbill);
		refbill.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT, IBillOperate.OP_INIT });
//		refbill.setChildAry(new int[]{soOrder.getBtnNo(),redSoorder.getBtnNo()});
		addPrivateButton(refbill);
	}
	/**
	 * 注册前台校验类
	 */
	public Object getUserObject() {
		return  null;
	}
	@Override
	protected void initEventListener() {
		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}
	
	public boolean beforeEdit(BillItemEvent e) {
		String key=e.getItem().getKey();
		if("pk_cargdoc".equals(key)){//出库货位
			//仓库id
			Object a = getBillCardPanel().getHeadItem("srl_pk").getValueObject();
			if(a==null){
				showWarningMessage("请选择仓库");
				return false;
			}
			UIRefPane panel = (UIRefPane) this.getBillCardPanel().getHeadItem("pk_cargdoc").getComponent();
			if (null != a && !"".equals(a)) {
				//修改参照 条件 增加条件 指定仓库id
				panel.getRefModel().addWherePart(" and bd_stordoc.pk_stordoc = '"+a+"' ");
			}
		}
		//
		
		if(e.getItem().getPos()==BillItem.HEAD){
			if("srl_pk".equalsIgnoreCase(key)){
				UIRefPane panel=(UIRefPane) getBillCardPanel().getHeadItem("srl_pk").getComponent();
				//参照过滤
				panel.getRefModel().addWherePart(" and def1 = '1' and  isnull(dr,0) = 0");
			}			
		}

		// 对当前货位下的管理员进行过滤
		if ("cwhsmanagerid".equalsIgnoreCase(key)) {
			String pk_cargdoc = (String) getBillCardPanel().getHeadItem(
					"pk_cargdoc").getValueObject();
			if (null == pk_cargdoc || "".equalsIgnoreCase(pk_cargdoc)) {
				showWarningMessage("前选择入库货位");
				return false;
			}
			JComponent c = getBillCardPanel().getHeadItem

			("cwhsmanagerid").getComponent();
			if (c instanceof UIRefPane) {
				UIRefPane ref = (UIRefPane) c;
				ref.getRefModel().addWherePart(
						"  and tb_stockstaff.pk_cargdoc='" + pk_cargdoc + "' ");
			}
			return true;
		}
		return true;
	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
	   String key=e.getKey();
	   //修改仓库 清空货位
	   if("srl_pk".equalsIgnoreCase(key)){
		   //仓库 为空 则 货位禁止编辑；反之 货位可编辑
		   boolean isEditable = true;
		   if(PuPubVO.getString_TrimZeroLenAsNull(e.getValue()) == null){
			   isEditable = false;
		   }
		   getBillCardPanel().getHeadItem("pk_cargdoc").setEnabled(isEditable);
		   getBillCardPanel().getHeadItem("pk_cargdoc").setValue(null);
	   }
	   if("cdptid".equalsIgnoreCase(key)){
		  getBillCardPanel().getHeadItem("cbizid").setValue(null);
	   }
	   if("cbizid".equalsIgnoreCase(key)){
		   getBillCardPanel().execHeadLoadFormulas();
	   }
		
	}

	public void afterUpdate() {
		if (!getBufferData().isVOBufferEmpty()){
			int row = getBufferData().getCurrentRow();
			if(row < 0){
				return;
			}
			BillItem[] body=	getBillCardPanel().getBillModel().getBodyItems();
			
			Object o = getBufferData().getCurrentVO().getParentVO().getAttributeValue(getBillField().getField_BillStatus());
			if(o.equals(IBillStatus.FREE)){//自由
				getButtonManager().getButton(ISsButtun.Qxqz).setEnabled(false);
				getButtonManager().getButton(ISsButtun.Qzqr).setEnabled(true);
			}else{//签字
				getButtonManager().getButton(ISsButtun.Qzqr).setEnabled(false);
				getButtonManager().getButton(ISsButtun.Qxqz).setEnabled(true);
			}
			updateButtons();
		}
	}
	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return curRefBilltype;
	}
	
	protected void setRefBillType(String curRefBilltype){
		this.curRefBilltype =curRefBilltype;
	}

//	public void setTrayInfor(Map<String,List<TbOutgeneralTVO>> trayInfor2){
//		trayInfor = trayInfor2;
//	}
//	public Map<String,List<TbOutgeneralTVO>> getTrayInfor(){
//		return trayInfor;
//	}
	
	/**
	 * 获取所有的TableCode 多字表
	 */
	public String[] getTableCodes() {
		return new String[]{"tb_outgeneral_b","tb_outgeneral_b2"};
	}
	
	public  void stateChanged(javax.swing.event.ChangeEvent arg0){
		Object sourece = arg0.getSource();
		if("tb_outgeneral_b".equals(getBillCardPanel().getBodyTabbedPane().getSelectedTableCode())){
			getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
			getButtonManager().getButton(IBillButton.DelLine).setEnabled(true);
		}else{
			getButtonManager().getButton(IBillButton.AddLine).setEnabled(true);
			getButtonManager().getButton(IBillButton.DelLine).setEnabled(true);
		}
		updateButtons();
	}

}

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
 * <b> ���۳��� </b>
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
//	private Map<String,List<TbOutgeneralTVO>> trayInfor = null;//������  �������µ� buffer
	
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
		getBillListPanel().getHeadTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);//��ѡ
		getBillListPanel().getBodyTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		getBillCardPanel().getBillTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		// ����ҳǩ�л�����
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
//		//���˴������ֻ���ڷ��� �Ĵ��
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
		// ���ÿ��Ա
		getBillCardPanel().getHeadItem("cwhsmanagerid").setValue(_getOperator());
		// �����Ƶ���
		getBillCardPanel().getHeadTailItem("coperatorid").setValue(_getOperator());
		// ��������
		getBillCardPanel().getHeadTailItem("dbilldate").setValue(_getDate().toString());
		//��������
		getBillCardPanel().getHeadTailItem("vbilltype").setValue(WdsWlPubConst.BILLTYPE_SALE_OUT);
		//��˾
		getBillCardPanel().getHeadTailItem("pk_corp").setValue(_getCorp().getPrimaryKey());
		//����״̬
		
		getBillCardPanel().getHeadTailItem("vbillstatus").setValue(IBillStatus.FREE); //����״̬
	}
	@Override
	protected IBillField createBillField() {
		return new nc.ui.wds.ic.other.out.BillField();
	}
	
	protected AbstractManageController createController() {
		return new MyClientUICtrl();
	}
	/**
	 * ע���Զ��尴ť
	 */
	protected void initPrivateButton() {
		super.initPrivateButton();
		fzgnBtn customizeButton1 = new fzgnBtn();//��������
		addPrivateButton(customizeButton1.getButtonVO());
		tpzdBtn customizeButton2 = new tpzdBtn();//�ֶ����
		addPrivateButton(customizeButton2.getButtonVO());
		zdqhBtn customizeButton3 = new zdqhBtn();//�Զ����
		addPrivateButton(customizeButton3.getButtonVO());
		ckmxBtn customizeButton4 = new ckmxBtn();//�鿴��ϸ
		addPrivateButton(customizeButton4.getButtonVO());

		QxqzBtn customizeButton5 = new QxqzBtn();//ȡ��ǩ��
		addPrivateButton(customizeButton5.getButtonVO());
		QzqrBtn customizeButton6 = new QzqrBtn();//ǩ��ȷ��
		addPrivateButton(customizeButton6.getButtonVO());
		getButtonManager().getButtonAry(new int[]{ISsButtun.Qzqr,ISsButtun.Qxqz});//ȡ��ǩ��,ǩ��ȷ��
		
		ButtonVO soOrder = new ButtonVO();
		soOrder.setBtnNo(nc.ui.wds.w80020206.buttun0206.ISsButtun.RefSoOrder);
		soOrder.setBtnCode(null);
		soOrder.setBtnName("�����˵�");
		soOrder.setBtnChinaName("�����˵�");
		addPrivateButton(soOrder);
//		ButtonVO redSoorder = new ButtonVO();
//		redSoorder.setBtnNo(nc.ui.wds.w80020206.buttun0206.ISsButtun.RefRedSoOrder);
//		redSoorder.setBtnCode(null);
//		redSoorder.setBtnName("�˻����");
//		redSoorder.setBtnChinaName("�˻����");
//		addPrivateButton(redSoorder);
		ButtonVO refbill =ButtonVOFactory.getInstance().build(IBillButton.Refbill);
		refbill.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT, IBillOperate.OP_INIT });
//		refbill.setChildAry(new int[]{soOrder.getBtnNo(),redSoorder.getBtnNo()});
		addPrivateButton(refbill);
	}
	/**
	 * ע��ǰ̨У����
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
		if("pk_cargdoc".equals(key)){//�����λ
			//�ֿ�id
			Object a = getBillCardPanel().getHeadItem("srl_pk").getValueObject();
			if(a==null){
				showWarningMessage("��ѡ��ֿ�");
				return false;
			}
			UIRefPane panel = (UIRefPane) this.getBillCardPanel().getHeadItem("pk_cargdoc").getComponent();
			if (null != a && !"".equals(a)) {
				//�޸Ĳ��� ���� �������� ָ���ֿ�id
				panel.getRefModel().addWherePart(" and bd_stordoc.pk_stordoc = '"+a+"' ");
			}
		}
		//
		
		if(e.getItem().getPos()==BillItem.HEAD){
			if("srl_pk".equalsIgnoreCase(key)){
				UIRefPane panel=(UIRefPane) getBillCardPanel().getHeadItem("srl_pk").getComponent();
				//���չ���
				panel.getRefModel().addWherePart(" and def1 = '1' and  isnull(dr,0) = 0");
			}			
		}

		// �Ե�ǰ��λ�µĹ���Ա���й���
		if ("cwhsmanagerid".equalsIgnoreCase(key)) {
			String pk_cargdoc = (String) getBillCardPanel().getHeadItem(
					"pk_cargdoc").getValueObject();
			if (null == pk_cargdoc || "".equalsIgnoreCase(pk_cargdoc)) {
				showWarningMessage("ǰѡ������λ");
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
	   //�޸Ĳֿ� ��ջ�λ
	   if("srl_pk".equalsIgnoreCase(key)){
		   //�ֿ� Ϊ�� �� ��λ��ֹ�༭����֮ ��λ�ɱ༭
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
			if(o.equals(IBillStatus.FREE)){//����
				getButtonManager().getButton(ISsButtun.Qxqz).setEnabled(false);
				getButtonManager().getButton(ISsButtun.Qzqr).setEnabled(true);
			}else{//ǩ��
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
	 * ��ȡ���е�TableCode ���ֱ�
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

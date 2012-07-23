package nc.ui.wds.ie.cgqy;
import javax.swing.JComponent;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.WdsBillManagUI;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 *  �ɹ�ȡ��
 * @author Administrator
 * 
 */
public class ClientUI extends WdsBillManagUI implements  BillCardBeforeEditListener{

	private static final long serialVersionUID = -3998675844592858916L;
	
	public ClientUI() {
		super();
	}

	public ClientUI(Boolean useBillSource) {
		super(useBillSource);
	}

	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}
	
	
	
	
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		return super.beforeEdit(e);
	}

	/**
	 * @author yf
	 * �ֿ��޸Ĺ��� ��λ
	 */
	public boolean beforeEdit(BillItemEvent e) {
		String key=e.getItem().getKey();
		//�Բֿ���ˣ�����ֻ���ڵ�ǰ��˾�Ĳֿ�
		if("reserve3".equalsIgnoreCase(key)){
			JComponent c =getBillCardPanel().getHeadItem("reserve3").getComponent();
			if( c instanceof UIRefPane){
				UIRefPane ref = (UIRefPane)c;
				ref.getRefModel().addWherePart("  and def1 = '1' and isnull(dr,0) = 0");
			}
			return true;
		}
		
		//������λ���ˣ�����ֻ���ڶ�Ӧ�����ֿ�����Ļ�λ
		if("reserve2".equalsIgnoreCase(key)){
			String pk_store=(String) getBillCardPanel().getHeadItem("reserve3").getValueObject();
			if(null==pk_store || "".equalsIgnoreCase(pk_store)){
				showWarningMessage("��ѡ�����ֿ�");
				return false;
			}			
			JComponent c =getBillCardPanel().getHeadItem("reserve2").getComponent();
			if( c instanceof UIRefPane){
				UIRefPane ref = (UIRefPane)c;
				ref.getRefModel().addWherePart("  and bd_cargdoc.pk_stordoc='"+pk_store+"' and isnull(bd_cargdoc.dr,0) = 0");
			}
			return true;			
		}
		return true;
	}

	/**
	 * @author yf
	 * �����ջ��޸ĺ󣬸������ݳ�ʼ��
	 */
	@Override
	public void afterEdit(BillEditEvent e) {
		//���� invcode�ֶ��޸�ʱ�Ž����ж�
		//ͨ�� �к� �� �ֶ��� ��������Ϊ��
		if("invcode".equals(e.getKey())){
			int row = e.getRow();
			getBillCardPanel().setBodyValueAt(null, row, "nassplannum");
			getBillCardPanel().setBodyValueAt(null, row, "nplannum");
		}
		
		super.afterEdit(e);
	}

	@Override
	protected AbstractManageController createController() {
		return new ClientController();
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
		//
		ButtonObject btn = getButtonManager().getButton(IBillButton.Line);
		if (btn != null) {
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.CopyLine));
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.PasteLine));
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.InsLine));
		}
	}

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("vbillstatus", IBillStatus.FREE);
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
		getBillCardPanel().setTailItem("voperatorid", _getOperator());
		getBillCardPanel().setHeadItem("pk_billtype", WdsWlPubConst.WDSC);
		getBillCardPanel().setTailItem("dmakedate", _getDate());	
		
//		Ϊ �ֿ�  �� ��λ ��ֵ
		String cwhid = PuPubVO.getString_TrimZeroLenAsNull(getLoginInforHelper().getCwhid(_getOperator()));
		getBillCardPanel().setHeadItem("reserve3", cwhid);
		String cspaceid = PuPubVO.getString_TrimZeroLenAsNull(getLoginInforHelper().getLogInfor(_getOperator()).getSpaceid());
		getBillCardPanel().setHeadItem("reserve2", cspaceid);
	}

	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

	// ���ݺ�
	public String getBillNo() throws java.lang.Exception {
		return HYPubBO_Client.getBillNo(getUIControl().getBillType(),
				_getCorp().getPrimaryKey(), null, null);
	}
	/**
	 * ���Ӻ�̨У��
	 */
	public Object getUserObject() {
		return null;
	}

	@Override
	public boolean isSaveAndCommitTogether() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initEventListener() {
		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}

	@Override
	public String getBillType() {
		// TODO Auto-generated method stub
		return WdsWlPubConst.WDSC;
	}

	@Override
	public boolean isLinkQueryEnable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getAssNumFieldName() {
		// TODO Auto-generated method stub
		return "nassplannum";
	}

	@Override
	public String getHslFieldName() {
		// TODO Auto-generated method stub
		return "nhsl";
	}


}
package nc.ui.wds.ic.tpyd;
import javax.swing.JComponent;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 *  �����ƶ�
 * @author Administrator
 * 
 */
public class ClientUI extends BillManageUI implements BillCardBeforeEditListener{

	private static final long serialVersionUID = -3998675844592858916L;
	
	private String cwhid;//��ǰ��¼�����ڲֿ�
	private String cspaceid;//��ǰ��¼�����ڵĻ�λ
	
	public ClientUI() {
		super();
		init();
	}

	public ClientUI(Boolean useBillSource) {
		super(useBillSource);
		init();
	}

	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
		init();
	}
	private void init(){
		LoginInforHelper login = new LoginInforHelper();
		try {
			cwhid = login.getCwhid(_getOperator());
			cspaceid = login.getLogInfor(_getOperator()).getSpaceid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cwhid = null;
			cspaceid = null;
		}
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
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("vbillstatus", IBillStatus.FREE);
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
		getBillCardPanel().setTailItem("voperatorid", _getOperator());
		getBillCardPanel().setHeadItem("pk_billtype", WdsWlPubConst.WDSD);
		getBillCardPanel().setTailItem("dmakedate", _getDate());	
//		���õ�ǰ��¼�����ڵĲֿ�  �� ��λ
		if(PuPubVO.getString_TrimZeroLenAsNull(cwhid)!=null)
			getBillCardPanel().setHeadItem("pk_stordoc", cwhid);
		if(PuPubVO.getString_TrimZeroLenAsNull(cspaceid)!=null)
			getBillCardPanel().setHeadItem("pk_cargedoc", cspaceid);
	}

	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

	// ���ݺ�
	public String getBillNo() throws java.lang.Exception {
		return HYPubBO_Client.getBillNo(getUIControl().getBillType(),
				_getCorp().getPrimaryKey(), null, null);
	}
	public boolean beforeEdit(BillItemEvent e) {
		String key=e.getItem().getKey();
		if(e.getItem().getPos() ==BillItem.HEAD){
			if("pk_stordoc".equalsIgnoreCase(key)){//�ֿ���ˣ�ֻ��������ϵͳ��
				JComponent c =getBillCardPanel().getHeadItem("pk_stordoc").getComponent();
				if( c instanceof UIRefPane){
					UIRefPane ref = (UIRefPane)c;
					ref.getRefModel().addWherePart("  and def1 = '1' and isnull(dr,0) = 0");
				}
				return true;
			}else if("pk_cargedoc".equalsIgnoreCase(key)){//��λ������ѡ��ֿ��µĻ�λ
				Object a = getBillCardPanel().getHeadItem("pk_stordoc").getValueObject();
				if (null != a && !"".equals(a)) {
					UIRefPane panel = (UIRefPane) this.getBillCardPanel().getHeadItem("pk_cargedoc").getComponent();
					panel.getRefModel().addWherePart(" and bd_cargdoc.pk_stordoc='" + a + "'");
				} else {
					showErrorMessage("����ѡ��ֿ�");
					return false;
				}
			}
		}
	
		return true;
	}
	
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		String key=e.getKey();
		int row = e.getRow();
		if(e.getPos() ==BillItem.BODY){
			if("outtraycode".equalsIgnoreCase(key)){//�Ƴ�����
				Object a = getBillCardPanel().getHeadItem("pk_cargedoc").getValueObject();
				if(null != a && !"".equals(a)){
					JComponent jc = getBillCardPanel().getBodyItem("outtraycode").getComponent();
					if( jc instanceof UIRefPane){
						UIRefPane ref = (UIRefPane)jc;
						ref.getRefModel().addWherePart(" and isnull(bd_cargdoc_tray.cdt_traystatus,0)="+StockInvOnHandVO.stock_state_use+"" +
								" and tb_warehousestock.pk_cargdoc='"+a+"'" +
										" and bd_cargdoc_tray.cdt_traycode not like '"+WdsWlPubConst.XN_CARGDOC_TRAY_NAME+"%' ");//�л�
					}
				}else{
					showErrorMessage("����ѡ���ͷ��λ��Ϣ!");
					return false;
				}
			}
			if("intarycode".equalsIgnoreCase(key)){//��������
				Object a = getBillCardPanel().getHeadItem("pk_cargedoc").getValueObject();
				if(null == a || "".equals(a)){
					showErrorMessage("����ѡ���ͷ��λ��Ϣ!");
					return false;
				}
				Object outtraycode = getBillCardPanel().getBodyValueAt(row, "outtraycode");
				if(null == outtraycode || "".equals(outtraycode)){
					showErrorMessage("����ѡ�� �Ƴ�����");
					return false;
				}
				Object pk_invmandoc = getBillCardPanel().getBodyValueAt(row, "pk_invmandoc");
				if(null == pk_invmandoc ||"".equals(pk_invmandoc)){
					showErrorMessage("�Ƴ����̰󶨴������ȡ������ά��");
					return false;
				}
				JComponent jc = getBillCardPanel().getBodyItem("intarycode").getComponent();
				if( jc instanceof UIRefPane){
					UIRefPane ref = (UIRefPane)jc;
					
					ref.getRefModel().addWherePart(" and wds_cargdoc.pk_cargdoc='"+a
							+"' and isnull(bd_cargdoc_tray.cdt_traystatus,0)= " +StockInvOnHandVO.stock_state_null+
							" and bd_invmandoc.pk_invmandoc='"+pk_invmandoc+"'" +
									" and bd_cargdoc_tray.cdt_traycode not like '"+WdsWlPubConst.XN_CARGDOC_TRAY_NAME+"%' ");//��ǰ��λ������Ϊ���Һ��Ƴ�����һ���Ĵ��
				}
			}
			
		}
		return super.beforeEdit(e);
	}
	@Override
	public void afterEdit(BillEditEvent e) {
		String key=e.getKey();
		int row = e.getRow();
		if(e.getPos() == BillItem.HEAD){
			//�ֹܸ��ģ������λ
			//�ֿ���߻�λ���� ��ձ���
		}else if(e.getPos() == BillItem.BODY){
			if("outtraycode".equalsIgnoreCase(key)){//�Ƴ�����
				JComponent jc = getBillCardPanel().getBodyItem("outtraycode").getComponent();
				if( jc instanceof UIRefPane){
					UIRefPane ref = (UIRefPane)jc;
					getBillCardPanel().setBodyValueAt(ref.getRefModel().getValue("tb_warehousestock.whs_stocktonnage"), row, "noutnum");
					getBillCardPanel().setBodyValueAt(ref.getRefModel().getValue("tb_warehousestock.whs_stockpieces"), row, "noutassnum");
					getBillCardPanel().setBodyValueAt(ref.getRefModel().getValue("tb_warehousestock.whs_stocktonnage"), row, "nmovenum");
					getBillCardPanel().setBodyValueAt(ref.getRefModel().getValue("tb_warehousestock.whs_stockpieces"), row, "nmoveassnum");
					getBillCardPanel().setBodyValueAt(ref.getRefModel().getValue("tb_warehousestock.whs_batchcode"), row, "vbanchcode");

					getBillCardPanel().setBodyValueAt(ref.getRefModel().getValue("tb_warehousestock.pk_invmandoc"), row, "pk_invmandoc");
					getBillCardPanel().getBillModel().execLoadFormulaByKey("pk_invmandoc");
					getBillCardPanel().setBodyValueAt(null, row, "pk_trayin");
					getBillCardPanel().getBillModel().execLoadFormulaByKey("pk_trayin");
					}
				
			}
			if("nmovenum".equalsIgnoreCase(key)){
				double noutnum =PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt(row, "noutnum")).doubleValue();
				double value = PuPubVO.getUFDouble_NullAsZero(e.getValue()).doubleValue();
				if(value>noutnum){
					showWarningMessage("���ܳ���������ƶ�");
					getBillCardPanel().setBodyValueAt(null, row, "nmovenum");
					getBillCardPanel().setBodyValueAt(null, row, "nmoveassnum");
					return;
				}else{
					//���㻻����
					//�Ƴ�����������
				     UFDouble num= PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt(row, "noutnum"));
					//�Ƴ����̸�����
				     UFDouble bnum= PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt(row, "noutassnum"));
					
				     UFDouble hsl=new UFDouble(0); 
				     if(num.doubleValue()<=0 && bnum.doubleValue()<=0){
				    	return; 
				     }
				     if(num.doubleValue()<=0 && bnum.doubleValue()>0){
				    	showWarningMessage("�Ƴ���������������һ��");
					 }
				     if(bnum.doubleValue()<=0){
				    	 return;
				     }
				     hsl=num.div(bnum);
				     if(hsl.doubleValue()>0){
				    	 getBillCardPanel().setBodyValueAt(value/hsl.doubleValue(), row, "nmoveassnum");
				     }				
				}
				
				
			}
			if("nmoveassnum".equalsIgnoreCase(key)){
				double nmoveassnum =PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt(row, "nmoveassnum")).doubleValue();
				double value = PuPubVO.getUFDouble_NullAsZero(e.getValue()).doubleValue();
				if(value>nmoveassnum){
					showWarningMessage("���ܳ���������ƶ�");
					getBillCardPanel().setBodyValueAt(null, row, "nmovenum");
					getBillCardPanel().setBodyValueAt(null, row, "nmoveassnum");
					return;
				}else{
					//���㻻����
					//�Ƴ�����������
				     UFDouble num= PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt(row, "noutnum"));
					//�Ƴ����̸�����
				     UFDouble bnum= PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt(row, "noutassnum"));
					
				     UFDouble hsl=new UFDouble(0); 
				     if(num.doubleValue()<=0 && bnum.doubleValue()<=0){
				    	return; 
				     }
				     if(num.doubleValue()<=0 && bnum.doubleValue()>0){
				    	showWarningMessage("�Ƴ���������������һ��");
					 }
				     if(bnum.doubleValue()<=0){
				    	 return;
				     }
				     hsl=num.div(bnum);
				     if(hsl.doubleValue()>0){
				    	 getBillCardPanel().setBodyValueAt(value*hsl.doubleValue(), row, "nmovenum");
				     }				
				}
			}
		
		}
	
		super.afterEdit(e);
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

}
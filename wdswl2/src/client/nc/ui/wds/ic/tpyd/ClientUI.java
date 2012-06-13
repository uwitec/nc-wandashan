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
	/**
	 * ��ͷ�༭ǰ�¼�
	 */
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
				if(null == a || "".equals(a)){
					showErrorMessage("����ѡ���ͷ��λ��Ϣ!");
					return false;
				}
				JComponent jc = getBillCardPanel().getBodyItem("outtraycode").getComponent();
				if( jc instanceof UIRefPane){
					UIRefPane ref = (UIRefPane)jc;
					StringBuffer strWhere = new StringBuffer();
					strWhere.append(" and isnull(bd_cargdoc_tray.cdt_traystatus,0)=1 ");//����״̬Ϊռ��
					strWhere.append(" and tb_warehousestock.pk_cargdoc='"+a+"'");//��ǰ��λ
					strWhere.append(" and bd_cargdoc_tray.cdt_traycode not like '"+WdsWlPubConst.XN_CARGDOC_TRAY_NAME+"%' ");//����������
					ref.getRefModel().addWherePart(strWhere.toString());
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
					StringBuffer strWhere = new StringBuffer();
					strWhere.append("and wds_cargdoc.pk_cargdoc='"+a+"'");//��λ
					strWhere.append(" and bd_invmandoc.pk_invmandoc='"+pk_invmandoc+"'");
					strWhere.append(" and bd_cargdoc_tray.cdt_traycode not like '"+WdsWlPubConst.XN_CARGDOC_TRAY_NAME+"%' ");
					strWhere.append(" and ((isnull(bd_cargdoc_tray.cdt_traystatus,0)= 0) ");//������
					strWhere.append(" or (isnull(bd_cargdoc_tray.cdt_traystatus,0)= 1 and coalesce (wds_invbasdoc.tray_volume,0) - coalesce (tb_warehousestock.whs_stockpieces,0) > 0))");//����δ����
					ref.getRefModel().addWherePart(strWhere.toString());
				}
			}
			if("nmoveassnum".equalsIgnoreCase(key)){
				UFDouble noutassnum = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt(row, "noutassnum"));
				if(noutassnum.doubleValue() <=0){
					return false;
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
			if("pk_stordoc".equalsIgnoreCase(key)){
				getBillCardPanel().setHeadItem("pk_cargedoc", null);
				getBillCardPanel().getBillModel().setBodyDataVO(null);
			}
			if("pk_cargedoc".equalsIgnoreCase(key)){
				getBillCardPanel().getBillModel().setBodyDataVO(null);
			}
			//�ֹܸ��ģ������λ
			//�ֿ���߻�λ���� ��ձ���
		}else if(e.getPos() == BillItem.BODY){
			if("outtraycode".equalsIgnoreCase(key)){//�Ƴ�����
				JComponent jc = getBillCardPanel().getBodyItem("outtraycode").getComponent();
				if( jc instanceof UIRefPane){
					UIRefPane ref = (UIRefPane)jc;
					UFDouble whs_stocktonnage = PuPubVO.getUFDouble_NullAsZero(ref.getRefModel().getValue("tb_warehousestock.whs_stocktonnage"));
					getBillCardPanel().setBodyValueAt(whs_stocktonnage, row, "noutnum");					
					UFDouble whs_stockpieces = PuPubVO.getUFDouble_NullAsZero(ref.getRefModel().getValue("tb_warehousestock.whs_stockpieces"));
					getBillCardPanel().setBodyValueAt(whs_stockpieces, row, "noutassnum");
					if(whs_stockpieces.doubleValue() >0){
						getBillCardPanel().setBodyValueAt(whs_stocktonnage.div(whs_stockpieces, 8), row, "nhsl");
					}
					getBillCardPanel().setBodyValueAt(null, row, "nmovenum");
					getBillCardPanel().setBodyValueAt(null, row, "nmoveassnum");
					getBillCardPanel().setBodyValueAt(ref.getRefModel().getValue("tb_warehousestock.whs_batchcode"), row, "vbanchcode");
					getBillCardPanel().setBodyValueAt(ref.getRefModel().getValue("tb_warehousestock.pk_invmandoc"), row, "pk_invmandoc");
					getBillCardPanel().setBodyValueAt(ref.getRefModel().getValue("tb_warehousestock.whs_pk"), row, "whs_pkout");
					getBillCardPanel().setBodyValueAt(ref.getRefModel().getValue("wds_invbasdoc.tray_volume"), row, "tray_volume");//��������					
					getBillCardPanel().getBillModel().execLoadFormulaByKey("pk_invmandoc");
					getBillCardPanel().setBodyValueAt(null, row, "pk_trayin");
					getBillCardPanel().getBillModel().execLoadFormulaByKey("pk_trayin");

				}
			}
			if("intarycode".equalsIgnoreCase(key)){//��������
				JComponent jc = getBillCardPanel().getBodyItem("intarycode").getComponent();
				if( jc instanceof UIRefPane){
					UIRefPane ref = (UIRefPane)jc;
					getBillCardPanel().setBodyValueAt(ref.getRefModel().getValue("tb_warehousestock.whs_stocktonnage"), row, "ninnum");//�������
					getBillCardPanel().setBodyValueAt(ref.getRefModel().getValue("tb_warehousestock.whs_stockpieces"), row, "ninassnum");//���������
					getBillCardPanel().setBodyValueAt(ref.getRefModel().getValue("tb_warehousestock.whs_pk"), row, "whs_pkin");//�������̴��״̬id

				}
			}
			if("nmovenum".equalsIgnoreCase(key)){
				if(!afterEditNmovenum(e)){
					getBillCardPanel().setBodyValueAt(e.getOldValue(), e.getRow(), e.getKey());
					getBillCardPanel().getBillModel().execEditFormulaByKey(e.getRow(), e.getKey());
				}
				
				return;
			}
			if("nmoveassnum".equalsIgnoreCase(key)){
				if(!afterEditNmoveassnum(e)){
					getBillCardPanel().setBodyValueAt(e.getOldValue(), e.getRow(), e.getKey());
					getBillCardPanel().getBillModel().execEditFormulaByKey(e.getRow(), e.getKey());
				}
				
				return;
			}
		}
		super.afterEdit(e);
	}
	/**
	 * 
	 * @���ߣ�lyf:�ƶ��������༭���¼�
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-27����03:59:32
	 */
	private boolean afterEditNmovenum(BillEditEvent e){
		String pk_taryin = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(e.getRow(), "pk_trayin"));
		if(pk_taryin == null){
			showWarningMessage("����ѡ����������");
			return false;
		}
		//�ƶ�����
		double noutnum =PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt(e.getRow(), "noutnum")).doubleValue();
		double value = PuPubVO.getUFDouble_NullAsZero(e.getValue()).doubleValue();
		if(value>noutnum){
			showWarningMessage("���ܳ���������ƶ�");
			return false;
		}else{
			//�����Ƴ����̿�������������㻻����
		     UFDouble num= PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt(e.getRow(), "noutnum"));//�Ƴ�����������
		     UFDouble bnum= PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt(e.getRow(), "noutassnum"));//�Ƴ����̸�����
		     UFDouble hsl=new UFDouble(0); 
		     if(num.doubleValue()<=0 && bnum.doubleValue()<=0){
		    	return false; 
		     }
		     if(num.doubleValue()<=0 && bnum.doubleValue()>0){
		    	showWarningMessage("�Ƴ���������������һ��");
		    	return false; 
			 }
		     if(bnum.doubleValue()<=0){
		    	 return false; 
		     	}
		     hsl=num.div(bnum);
		     UFDouble nmoveassnum = PuPubVO.getUFDouble_NullAsZero(value/hsl.doubleValue());
		     UFDouble ninassnum= PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt(e.getRow(), "ninassnum"));//�������̸�����
		     UFDouble tray_volume= PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt(e.getRow(), "tray_volume"));//�����������Ը�������Ϊ��׼��
		     if(nmoveassnum.add(ninassnum).sub(tray_volume).doubleValue()>0){
		    	showWarningMessage("����������������");
		    	return false;
		     }
		     if(hsl.doubleValue()>0){
		    	 getBillCardPanel().setBodyValueAt(nmoveassnum, e.getRow(), "nmoveassnum");
		     }				
		}
		return true;
	
	}
	/**
	 * 
	 * @���ߣ�lyf:�ƶ��������༭���¼�
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-27����04:10:50
	 * @param e
	 */
	private boolean afterEditNmoveassnum(BillEditEvent e){
		String pk_taryin = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(e.getRow(), "pk_trayin"));
		if(pk_taryin == null){
			showWarningMessage("����ѡ����������");
			return false;
		}
		//�ƶ�������
		double noutassnum =PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt(e.getRow(), "noutassnum")).doubleValue();
		double value = PuPubVO.getUFDouble_NullAsZero(e.getValue()).doubleValue();
		if(value>noutassnum){
			showWarningMessage("���ܳ���������ƶ�");
			return false;
		}else{
			//�����Ƴ����̿�������������㻻����
		     UFDouble num= PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt(e.getRow(), "noutnum"));//�Ƴ�����������
		     UFDouble bnum= PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt(e.getRow(), "noutassnum"));//�Ƴ����̸�����
		     UFDouble hsl=new UFDouble(0); 
		     if(num.doubleValue()<=0 && bnum.doubleValue()<=0){
		    	 return false;
		     }
		     if(num.doubleValue()<=0 && bnum.doubleValue()>0){
		    	showWarningMessage("�Ƴ���������������һ��");
			 }
		     if(bnum.doubleValue()<=0){
		    	 return false;
		     }
		     UFDouble ninassnum= PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt(e.getRow(), "ninassnum"));//�������̸�����
		     UFDouble tray_volume= PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt(e.getRow(), "tray_volume"));//�����������Ը�������Ϊ��׼��
		     if(PuPubVO.getUFDouble_NullAsZero(e.getValue()).add(ninassnum).sub(tray_volume).doubleValue()>0){
		    	showWarningMessage("����������������");
		    	return false;
		     }
		     hsl=num.div(bnum);
		     if(hsl.doubleValue()>0){
		    	 getBillCardPanel().setBodyValueAt(value*hsl.doubleValue(), e.getRow(), "nmovenum");
		     }				
		}
		return true;
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
package nc.ui.wds.tranprice.fencang;
import javax.swing.JComponent;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.LoginInforVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 *  分仓运价表
 * @author Administrator
 * 
 */
public class ClientUI extends BillManageUI  implements  BillCardBeforeEditListener{

	private static final long serialVersionUID = -3998675844592858916L;
	protected LoginInforVO m_loginInfor = null;

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
	@Override
	protected void initEventListener() {	
		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}
	private void init() {
		// 初始当前登录人 角色
		LoginInforHelper login = new LoginInforHelper();
		try {
			m_loginInfor = login.getLogInfor(_getOperator());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			m_loginInfor = null;
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
	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		//过滤该仓库  承运商 下的收获地区
		if("reareaclcode".equalsIgnoreCase(key)){			
			Object pk_stordoc = getBillCardPanel().getHeadItem("reserve1").getValueObject();		
			if(pk_stordoc == null || pk_stordoc.equals("") ){
				showWarningMessage("仓库未 绑定");
				return false;
			}
			Object carriersid= getBillCardPanel().getHeadItem("carriersid").getValueObject();
			if(carriersid == null || carriersid.equals("") ){
				showWarningMessage("请选择承运商");
				return false;
			}
			JComponent jc = getBillCardPanel().getBodyItem("reareaclcode").getComponent();
			if(jc instanceof UIRefPane){
				UIRefPane ref =(UIRefPane)jc;
				ref.getRefModel().addWherePart(
				" and bd_areacl.pk_areacl in (select b.careaid from bd_stordoc h join wds_stortranscorp_b b" +
				"   on h.pk_stordoc=b.pk_stordoc where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 " +
				"   and h.pk_stordoc='"+pk_stordoc+"' and b.careaid  is not null " +
				"   and b.pk_wds_tanscorp_h='"+carriersid+"' and h.pk_corp='"+_getCorp().getPrimaryKey()+"') "			
				);
			}
		}		
		return true;
	}
    
	@Override
	protected void initSelfData() {
		
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
		getBillCardPanel().setHeadItem("pk_billtype", WdsWlPubConst.WDSK);
		getBillCardPanel().setTailItem("dmakedate", _getDate());
		getBillCardPanel().setHeadItem("dbilldate", _getDate());
		// zhf add 初始化 默认 发货仓库
		getBillCardPanel().setHeadItem("reserve1", m_loginInfor.getWhid());

	}

	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

//	// 单据号a
	public String getBillNo() throws java.lang.Exception {
		return HYPubBO_Client.getBillNo(getUIControl().getBillType(),
				_getCorp().getPrimaryKey(), null, null);
	}
	
	public Object getUserObject() {
		return null;
	}
	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		
		
	}

public boolean beforeEdit(BillItemEvent e) {
	String key = e.getItem().getKey();
	//过滤该仓库下的承运商
	if("carriersid".equalsIgnoreCase(key)){			
		Object pk_stordoc = getBillCardPanel().getHeadItem("reserve1").getValueObject();		
		if(pk_stordoc == null || pk_stordoc.equals("") ){
			showWarningMessage("仓库未 绑定");
			return false;
		}			
		JComponent jc = getBillCardPanel().getHeadItem("carriersid").getComponent();
		if(jc instanceof UIRefPane){
			UIRefPane ref =(UIRefPane)jc;
			ref.getRefModel().addWherePart(
			"and wds_tanscorp_h.pk_wds_tanscorp_h in (select b.pk_wds_tanscorp_h from bd_stordoc h join wds_stortranscorp_b b" +
			"   on h.pk_stordoc=b.pk_stordoc where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 " +
			"   and h.pk_stordoc='"+pk_stordoc+"' and b.pk_wds_tanscorp_h  is not null  " +
			"   and h.pk_corp='"+_getCorp().getPrimaryKey()+"') "			
			);
		}
	}
	return false;
}

}
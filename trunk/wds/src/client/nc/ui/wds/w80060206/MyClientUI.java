 package nc.ui.wds.w80060206;

import java.util.ArrayList;

import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;

import nc.bs.framework.common.NCLocator;
import nc.itf.ic.pub.IGeneralBill;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */
public class MyClientUI extends AbstractMyClientUI implements
		BillCardBeforeEditListener {

	public MyClientUI() {
		super();
		setscale();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
		

		// getBillCardPanel().addBillEditListenerHeadTail(billEditListener)

	}
	
	protected ManageEventHandler createEventHandler() {
		
		return new MyEventHandler(this, getUIControl());

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

	protected void initSelfData() {
	}

	public void setDefaultData() throws Exception {
	}
	public void setscale(){
		int[] iScale=null;
		try {
			iScale = getSysParm(ClientEnvironment
					.getInstance().getCorporation().getPrimaryKey(), ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
		} catch (BusinessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.getBillCardPanel().getBodyItem("pwbb_snum").setDecimalDigits(iScale[0]);
		this.getBillCardPanel().getBodyItem("pwbb_anum").setDecimalDigits(iScale[0]);
		this.getBillCardPanel().getBodyItem("pwbb_bsnum").setDecimalDigits(iScale[1]);
		this.getBillCardPanel().getBodyItem("pwbb_banum").setDecimalDigits(iScale[1]);
		this.getBillCardPanel().getBodyItem("pwbb_hsl").setDecimalDigits(iScale[2]);
		this.getBillListPanel().getBodyItem("pwbb_snum").setDecimalDigits(iScale[0]);
		this.getBillListPanel().getBodyItem("pwbb_anum").setDecimalDigits(iScale[0]);
		this.getBillListPanel().getBodyItem("pwbb_bsnum").setDecimalDigits(iScale[1]);
		this.getBillListPanel().getBodyItem("pwbb_banum").setDecimalDigits(iScale[1]);
		this.getBillListPanel().getBodyItem("pwbb_hsl").setDecimalDigits(iScale[2]);
		
		
	}
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		if (e.getKey().equals("tc_pk")) {
			getBillCardPanel().execHeadEditFormulas();
		}
		// String tc_pk = (String) this.getBillCardPanel()
		// .getHeadItem("tc_pk").getValueObject();
		// if (tc_pk != null && tc_pk.length() > 0) {
		// getBillCardPanel().getHeadItem("cif_pk").setEnabled(true);
		// // 得到合同参照
		// UIRefPane panel = (UIRefPane) this.getBillCardPanel()
		// .getHeadItem("cif_pk").getComponent();
		// //加上客户做为条件去过滤
		// panel.getRefModel().setWherePart(" tc_pk = '" + tc_pk + "' ");
		// } else {
		// getBillCardPanel().getHeadItem("cif_pk").setEnabled(false);
		// }
		// }
		
		super.afterEdit(e);
	}

	public boolean beforeEdit(BillItemEvent e) {
		if (getBillCardPanel().isEnabled() == false) {
			return false;
		}
		// 这里如果单据上选择了客户，参选合同时，需要按客户过滤经销合同
		if ("cif_pk".equals(e.getItem().getKey())) {
			String tc_pk = (String) this.getBillCardPanel()
					.getHeadItem("tc_pk").getValueObject();
			if (tc_pk != null && tc_pk.length() > 0) {
				// 得到合同参照
				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
						.getHeadItem("cif_pk").getComponent();
//				this.getBillCardPanel()
//				.getHeadItem("cif_pk").setDecimalDigits(iDecimalDigits);
				// 加上客户做为条件去过滤
				panel.getRefModel().setWherePart(
						" tc_pk = '" + tc_pk + "' and dr=0 ");
			} else {

				this.showErrorMessage("您没有选择运输公司，将不显示任何车辆信息");
				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
						.getHeadItem("cif_pk").getComponent();
				// 加上客户做为条件去过滤
				panel.getRefModel().setWherePart(" dr=0 and 1=2  ");
			}
		}
		return true;
	}

	private Exception Exception() {
		// TODO Auto-generated method stub
		return null;
	}

	private int[] getSysParm(String sPk_corp, String suserid)
			throws BusinessException {

		String[] saParam = new String[] { "BD501", "BD502","BD503" };
		// 传入的参数
		ArrayList alAllParam = new ArrayList();
		// 查参数的必须数据
		ArrayList alParam = new ArrayList();
		alParam.add(sPk_corp); // 第一个是公司
		alParam.add(saParam); // 待查的参数
		alAllParam.add(alParam);
		// //查用户对应公司的必须参数
		alAllParam.add(suserid);

		IGeneralBill IGeneralBill = (IGeneralBill) NCLocator.getInstance()
				.lookup(IGeneralBill.class.getName());

		IUAPQueryBS IUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());

		ArrayList alRetData = null;
		try {
			alRetData = (ArrayList) IGeneralBill.queryInfo(
					nc.vo.ic.pub.bill.QryInfoConst.INIT_PARAM, alAllParam);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		int[] iScale = new int[3];
		String[] saParamValue = (String[]) alRetData.get(0);
		if(null!=saParamValue[0]){
			iScale[0] = Integer.parseInt(saParamValue[0]);
		}else{
			iScale[0] = 8;
		}
		if(null!=saParamValue[1]){
			iScale[1] = Integer.parseInt(saParamValue[1]);
		}else{
			iScale[1] = 8;
		}
		if(null!=saParamValue[2]){
			iScale[2] = Integer.parseInt(saParamValue[2]);
		}else{
			iScale[2] = 8;
		}
		return iScale;
	}

}

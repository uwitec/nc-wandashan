package nc.ui.hg.pu.check.specialfund;

import javax.swing.ListSelectionModel;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.hg.pu.pub.PlanPubHelper;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.hg.pu.check.fund.CritiCHK;
import nc.vo.hg.pu.pub.PlanApplyInforVO;
import nc.vo.scm.pu.PuPubVO;

/**
 * 专项资金设置
 * 
 * @author zhw
 * 
 */
public class ClientUI extends BillCardUI {

	private static final long serialVersionUID = 859146761348083688L;

	public ClientUI() {
		super();
		initAppInfor();
	}

	public PlanApplyInforVO m_appInfor = null;// 系统集采公司

	public void initAppInfor() {
		try {
			m_appInfor = PlanPubHelper.getAppInfor(_getCorp().getPrimaryKey(),
					_getOperator());
		} catch (Exception e) {
			e.printStackTrace();// 获取申请信息失败 并不影响界面加载
			m_appInfor = null;
		}
	}

	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}

	/**
	 * 
	 */
	@Override
	protected ICardController createController() {
		return new ClientUICtrl(_getCorp().getPrimaryKey());
	}

	protected CardEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

	@Override
	public String getRefBillType() {
		return null;
	}

	@Override
	protected void initSelfData() {
		//
		getBillCardPanel().getBillTable().setRowSelectionAllowed(true);
		getBillCardPanel().getBillTable().setSelectionMode(
				ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		// //年
		// getBillCardWrapper().initBodyComboBox("cyear", getComBoxYearValues(),
		// false);
	}

	/**
	 * 
	 * @return
	 * 
	 */
	// public String[] getComBoxYearValues(){
	// UFDate billdate = new UFDate(System.currentTimeMillis());
	// int year=billdate.getYear();
	// String[] str= new String[5];
	// for(int i=-2;i<=2;i++){
	// str[i+2]= String.valueOf(year+i);
	// }
	// return str;
	// }
	@Override
	public void setDefaultData() throws Exception {

	}

	@Override
	public Object getUserObject() {
		return new CritiCHK();
	}

	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
		super.afterEdit(e);
		String key = e.getKey();
		int row = e.getRow();
		if (e.getPos() == BillItem.BODY) {
			if ("corp".equalsIgnoreCase(key)) {
				String corp = PuPubVO
						.getString_TrimZeroLenAsNull(getBillCardPanel()
								.getBillModel().getValueAt(row, "pk_corp"));
				if (corp != null) {
					getBillCardPanel().getBillData().getBodyItem("deptname")
							.setEdit(false);
				} else {
					getBillCardPanel().getBillData().getBodyItem("deptname")
							.setEdit(true);
				}
			} else if ("deptname".equalsIgnoreCase(key)) {
				String deptname = PuPubVO
						.getString_TrimZeroLenAsNull(getBillCardPanel()
								.getBillModel().getValueAt(row, "deptname"));
				if (deptname != null) {
					getBillCardPanel().getBillData().getBodyItem("corp")
							.setEdit(false);
				} else {
					getBillCardPanel().getBillData().getBodyItem("corp")
							.setEdit(true);
				}
			} else if ("cyear".equalsIgnoreCase(key)) {
				getBillCardPanel().getBillModel().setValueAt(null, row,
						"imonth");
				getBillCardPanel().getBillModel().setValueAt(null, row,
						"pk_month");
			} else if ("imonth".equalsIgnoreCase(key)) {
				String[] aryAssistunit = new String[] {
						"pk_year->getColValue(bd_accperiodmonth,pk_accperiod,pk_accperiodmonth,pk_month)",
						"cyear->getColValue(bd_accperiod,periodyear,pk_accperiod,pk_year)" };
				getBillCardPanel().getBillModel().execFormulas(e.getRow(),
						aryAssistunit);
			}
		}
	}

	public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
		String key = e.getKey();
		int row = e.getRow();
		if (m_appInfor == null) {
			showWarningMessage("未获取到系统采购公司");
			return false;
		}
		if ("corp".equalsIgnoreCase(key) || "deptname".equalsIgnoreCase(key)) {
			if (m_appInfor.getM_pocorp().equalsIgnoreCase(_getCorp().getPrimaryKey())) {// 过滤掉当前公司 物资供应不下有效
				UIRefPane invRefPane = (UIRefPane) getBillCardPanel()
						.getBodyItem("corp").getComponent();
				AbstractRefModel refModel = (AbstractRefModel) invRefPane
						.getRefModel();
				refModel.addWherePart(" and pk_corp <> '"
						+ _getCorp().getPrimaryKey() + "'");
			}else{
				
			}
			String customer = PuPubVO
					.getString_TrimZeroLenAsNull(getBillCardPanel()
							.getBillModel().getValueAt(row, "customer"));
			if (customer == null)
				return true;
			return false;
		} else if ("customer".equalsIgnoreCase(key)) {
			String corp = PuPubVO
					.getString_TrimZeroLenAsNull(getBillCardPanel()
							.getBillModel().getValueAt(row, "corp"));
			String deptname = PuPubVO
					.getString_TrimZeroLenAsNull(getBillCardPanel()
							.getBillModel().getValueAt(row, "deptname"));
			if (corp == null && deptname == null)
				return true;
			return false;
		} else if ("imonth".equalsIgnoreCase(key)) {
			String pk_year = PuPubVO
					.getString_TrimZeroLenAsNull(getBillCardPanel()
							.getBillModel().getValueAt(row, "pk_year"));
			UIRefPane invRefPane = (UIRefPane) getBillCardPanel().getBodyItem(
					"imonth").getComponent();
			AbstractRefModel refModel = (AbstractRefModel) invRefPane
					.getRefModel();
			if (pk_year == null) {
				refModel.addWherePart(" and 1=1 ");
			} else {
				refModel.addWherePart(" and bd_accperiod.pk_accperiod = '"
						+ pk_year + "'");
			}

			return true;
		}
		return super.beforeEdit(e);
	}
}

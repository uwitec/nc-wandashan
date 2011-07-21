package nc.ui.hg.pu.plan.mondeal;

import javax.swing.table.TableColumnModel;

import nc.bd.accperiod.AccountCalendar;
import nc.ui.hg.pu.pub.DefBillManageUI;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.bd.period3.AccperiodquarterVO;
import nc.vo.hg.pu.pub.HgPuBtnConst;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.button.ButtonVO;

/**
 * 月计划调整(支持自定义项)
 * 
 * @author zhw
 * 
 */
public class ClientUI extends DefBillManageUI {
	AccountCalendar calendar = AccountCalendar.getInstance();
	AccperiodquarterVO quarter = calendar.getQuarterVO();
	
	public ClientUI() {
		super();
	}

	@Override
	protected void initSelfData() {
		// 除去审批按钮
		ButtonObject btnobj = getButtonManager().getButton(IBillButton.Action);
		if (btnobj != null) {
			btnobj.removeChildButton(getButtonManager().getButton(
					IBillButton.CancelAudit));
		}
		// 除去行操作多余按钮
		ButtonObject btnobj1 = getButtonManager().getButton(IBillButton.Line);
		if (btnobj1 != null) {
			btnobj1.removeChildButton(getButtonManager().getButton(
					IBillButton.AddLine));
			btnobj1.removeChildButton(getButtonManager().getButton(
					IBillButton.CopyLine));
			btnobj1.removeChildButton(getButtonManager().getButton(
					IBillButton.PasteLine));
			btnobj1.removeChildButton(getButtonManager().getButton(
					IBillButton.InsLine));
			btnobj1.removeChildButton(getButtonManager().getButton(
					IBillButton.PasteLinetoTail));
		}

		setCardPanelColumn();
		setListPanelColumn();
	}

	private void setListPanelColumn() {
		UITable listTable = getBillListPanel().getBodyTable();
		GroupableTableHeader cardHeader = (GroupableTableHeader) listTable
				.getTableHeader();
		TableColumnModel listTcm = listTable.getColumnModel();
		ColumnGroup[] list2 = new ColumnGroup[12];// 二级，12个月和最后的合计

		for (int i = 0; i < 12; i++) {
			list2[i] = new ColumnGroup(String.valueOf(i + 1) + "月份");
		}

		int index = 0;
		for (int i = 6; i < 41; i = i + 3) {
			index = (i - 6) / 3;
			list2[index].add(listTcm.getColumn(i));
			list2[index].add(listTcm.getColumn(i + 1));
			list2[index].add(listTcm.getColumn(i + 2));
			cardHeader.addColumnGroup(list2[index]);
		}
		getBillCardPanel().getBillModel().updateValue();
	}

	private void setCardPanelColumn() {
		UITable cardTable = getBillCardPanel().getBillTable();
		GroupableTableHeader cardHeader = (GroupableTableHeader) cardTable
				.getTableHeader();
		TableColumnModel cardTcm = cardTable.getColumnModel();
		ColumnGroup[] card2 = new ColumnGroup[12];// 二级，12个月和最后的合计

		for (int i = 0; i < 12; i++) {
			card2[i] = new ColumnGroup(String.valueOf(i + 1) + "月份");
		}

		int index = 0;
		for (int i = 6; i < 41; i = i + 3) {
			index = (i - 6) / 3;
			card2[index].add(cardTcm.getColumn(i));
			card2[index].add(cardTcm.getColumn(i + 1));
			card2[index].add(cardTcm.getColumn(i + 2));
			cardHeader.addColumnGroup(card2[index]);
		}
		getBillCardPanel().getBillModel().updateValue();
	}

	@Override
	public void setDefaultData() throws Exception {

	}

	// 添加自定义按钮
	public void initPrivateButton() {
		// 加载
		ButtonVO btnvo1 = new ButtonVO();
		btnvo1.setBtnNo(HgPuBtnConst.LOAD);
		btnvo1.setBtnName("加载");
		btnvo1.setBtnChinaName("加载");
		btnvo1.setBtnCode(null);// code最好设置为空
		btnvo1.setOperateStatus(new int[] { IBillOperate.OP_INIT,
				IBillOperate.OP_NOTEDIT, IBillOperate.OP_NO_ADDANDEDIT });
		addPrivateButton(btnvo1);
		super.initPrivateButton();
	}

	@Override
	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

	@Override
	protected AbstractManageController createController() {
		return new ClientController();
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {

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
	protected String getBillNo() throws Exception {
		return HYPubBO_Client.getBillNo(HgPubConst.PLAN_MONDEAL_BILLTYPE,
				_getCorp().getPrimaryKey(), null, null);
	}

	@Override
	protected BusinessDelegator createBusinessDelegator() {
		return new ClientBusinessDelegator(this);
	}

	
	/**
	 * liuys add for 鹤岗矿业
	 * 
	 * 根据季度调整月计划调剂量
	 * 如果是1月,则123月能调整,如果是2月,则2,3月能调整,如果是3月则第二季度能调整,以此类推,12月则都无法调整
	 */
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		Object kjmouth = Integer.parseInt(calendar.getMonthVO().getMonth());
		if(kjmouth == null)
			return false;
		String key = e.getKey();
		int row = e.getRow();
		//如果是1月,则1,2,3月均能编辑调剂量
		if(kjmouth.equals(1))
			for (int i = 0; i < 3; i++) {
				if (key.equalsIgnoreCase(HgPubConst.NADJUSTNUM[i])) {
					if (!PuPubVO.getUFDouble_NullAsZero(
							getBillCardPanel().getBodyValueAt(row,
									HgPubConst.NTOTAILNUM[i])).equals(UFDouble.ZERO_DBL)) {
						return false;
					}
					return true;
				}
			}
		else
		//如果是2月,则2,3月均能编辑调剂量
		if(kjmouth.equals(2))
			for (int i = 1; i < 3; i++) {
				if (key.equalsIgnoreCase(HgPubConst.NADJUSTNUM[i])) {
					if (!PuPubVO.getUFDouble_NullAsZero(
							getBillCardPanel().getBodyValueAt(row,
									HgPubConst.NTOTAILNUM[i])).equals(UFDouble.ZERO_DBL)) {
						return false;
					}
					return true;
				}
			}
		else
		//如果是3月或4月,则4,5,6月均能编辑调剂量
		if(kjmouth.equals(3) || kjmouth.equals(4))
			for (int i = 3; i < 6; i++) {
				if (key.equalsIgnoreCase(HgPubConst.NADJUSTNUM[i])) {
					if (!PuPubVO.getUFDouble_NullAsZero(
							getBillCardPanel().getBodyValueAt(row,
									HgPubConst.NTOTAILNUM[i])).equals(UFDouble.ZERO_DBL)) {
						return false;
					}
					return true;
				}
			}
		else
		//如果是5月,则5,6月均能编辑调剂量
		if(kjmouth.equals(5))
			for (int i = 4; i < 6; i++) {
				if (key.equalsIgnoreCase(HgPubConst.NADJUSTNUM[i])) {
					if (!PuPubVO.getUFDouble_NullAsZero(
							getBillCardPanel().getBodyValueAt(row,
									HgPubConst.NTOTAILNUM[i])).equals(UFDouble.ZERO_DBL)) {
						return false;
					}
					return true;
				}
			}
		else
		//如果是6月或者7,则7,8,9月均能编辑调剂量
		if(kjmouth.equals(6)||kjmouth.equals(7))
			for (int i = 6; i < 9; i++) {
				if (key.equalsIgnoreCase(HgPubConst.NADJUSTNUM[i])) {
					if (!PuPubVO.getUFDouble_NullAsZero(
							getBillCardPanel().getBodyValueAt(row,
									HgPubConst.NTOTAILNUM[i])).equals(UFDouble.ZERO_DBL)) {
						return false;
					}
					return true;
				}
			}
		else
		//如果是8月,则8,9月均能编辑调剂量
		if(kjmouth.equals(8))
			for (int i = 7; i < 9; i++) {
				if (key.equalsIgnoreCase(HgPubConst.NADJUSTNUM[i])) {
					if (!PuPubVO.getUFDouble_NullAsZero(
							getBillCardPanel().getBodyValueAt(row,
									HgPubConst.NTOTAILNUM[i])).equals(UFDouble.ZERO_DBL)) {
						return false;
					}
					return true;
				}
			}
		else
		//如果是9月或10,则10,11,12月均能编辑调剂量
		if(kjmouth.equals(9)||kjmouth.equals(10))
			for (int i = 9; i < 12; i++) {
				if (key.equalsIgnoreCase(HgPubConst.NADJUSTNUM[i])) {
					if (!PuPubVO.getUFDouble_NullAsZero(
							getBillCardPanel().getBodyValueAt(row,
									HgPubConst.NTOTAILNUM[i])).equals(UFDouble.ZERO_DBL)) {
						return false;
					}
					return true;
				}
			}
		else
		//如果是11月,则11,12月均能编辑调剂量
		if(kjmouth.equals(11))
			for (int i = 10; i < 12; i++) {
				if (key.equalsIgnoreCase(HgPubConst.NADJUSTNUM[i])) {
					if (!PuPubVO.getUFDouble_NullAsZero(
							getBillCardPanel().getBodyValueAt(row,
									HgPubConst.NTOTAILNUM[i])).equals(UFDouble.ZERO_DBL)) {
						return false;
					}
					return true;
				}
			}
		
		return false;
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
	}

	/**
	 * 保存即提交
	 */
//	@Override
//	public boolean isSaveAndCommitTogether() {
//		return true;
//	}
}

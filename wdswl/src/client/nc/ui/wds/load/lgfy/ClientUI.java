package nc.ui.wds.load.lgfy;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wds.load.account.LoadpirceTVO;
import nc.vo.wl.pub.ButtonCommon;

/**
 * 零工费用单
 * 
 * @author yf
 * 
 */
public class ClientUI extends BillManageUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = -558525831456996131L;

	@Override
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		return new ClientController();
	}

	@Override
	protected ManageEventHandler createEventHandler() {
		// TODO Auto-generated method stub
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

	// 单据号
	public String getBillNo() throws java.lang.Exception {
		return HYPubBO_Client.getBillNo(getUIControl().getBillType(),
				_getCorp().getPrimaryKey(), null, null);
	}

	@Override
	public boolean isSaveAndCommitTogether() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("vbillstatus", IBillStatus.FREE);
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
		getBillCardPanel().setHeadItem("pk_billtype",
				getUIControl().getBillType());
		getBillCardPanel().setHeadItem("dbilldate", _getDate());
		getBillCardPanel().setTailItem("voperatorid", _getOperator());
		getBillCardPanel().setTailItem("dmakedate", _getDate());
	}

	@Override
	protected void initPrivateButton() {
		super.initPrivateButton();
		ButtonVO button = new ButtonVO();
		button.setBtnNo(ButtonCommon.WDSVYGXX);
		button.setBtnCode(null);
		button.setBtnName("编辑员工信息");
		button.setBtnChinaName("编辑员工信息");
		button.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });
		addPrivateButton(button);
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		if ("teamcode".equalsIgnoreCase(key)) {
			return beforeEditTeamcode(e);
		}
		return super.beforeEdit(e);
	}

	private boolean beforeEditTeamcode(BillEditEvent e) {
		String pk = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
				.getBodyValueAt(e.getRow(), "pk_loadprice_b2"));
		if (pk == null) {
			return true;
		}
		if (hasWDSVYGXX(pk)) {
			return false;
		}
		return true;
	}

	private boolean hasWDSVYGXX(String pk) {
		try {
			SuperVO[] tvos = HYPubBO_Client.queryByCondition(
					LoadpirceTVO.class,
					" isnull(dr,0) = 0 and pk_loadprice_b2 = '" + pk + "' ");
			if (tvos != null && tvos.length > 0) {
				return true;
			}
		} catch (UifException e) {
			e.printStackTrace();
			return true;
		}
		return false;
	}
}

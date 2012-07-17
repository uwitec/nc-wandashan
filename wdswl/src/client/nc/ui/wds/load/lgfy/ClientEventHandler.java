package nc.ui.wds.load.lgfy;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.load.account.LoadpriceB2VO;
import nc.vo.wl.pub.ButtonCommon;

public class ClientEventHandler extends ManageEventHandler {

	public ClientUIQueryDlg queryDialog = null;

	private WDSVEditDlg m_quickDlg = null;

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	protected String getHeadCondition() {
		if (super.getHeadCondition() == null) {
			return " and pk_billtype = '" + getUIController().getBillType()
					+ "' ";
		}
		return super.getHeadCondition() + " and pk_billtype = '"
				+ getUIController().getBillType() + "' ";
	}

	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
			queryDialog = new ClientUIQueryDlg(getBillUI(), null, _getCorp()
					.getPrimaryKey(), getBillUI()._getModuleCode(),
					_getOperator(), getBillUI().getBusinessType(), getBillUI()
							.getNodeKey());
		}
		return queryDialog;
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		if (ButtonCommon.WDSVYGXX == intBtn) {
			onBoWDSVYGXX();
		}
		super.onBoElse(intBtn);
	}

	/**
	 * 
	 * @作者：yf
	 * @说明：完达山物流项目 
	 * 编辑人员信息
	 * @时间：2012-7-17下午05:17:30
	 * @throws BusinessException
	 */
	private void onBoWDSVYGXX() throws BusinessException {
		int i = getBillCardPanelWrapper().getBillCardPanel().getBillTable()
				.getSelectedRow();
		if (i < 0) {
			throw new BusinessException("请先选择要编辑的班组");
		}
		String pk = PuPubVO
				.getString_TrimZeroLenAsNull(getBillCardPanelWrapper()
						.getBillCardPanel()
						.getBodyValueAt(i, "pk_loadprice_b2"));
		String teampk = PuPubVO
				.getString_TrimZeroLenAsNull(getBillCardPanelWrapper()
						.getBillCardPanel().getBodyValueAt(i,
								"pk_wds_teamdoc_h"));
		UFDouble nloadprice = PuPubVO
				.getUFDouble_NullAsZero(getBillCardPanelWrapper()
						.getBillCardPanel().getBodyValueAt(i, "nloadprice"));
		LoadpriceB2VO vo = new LoadpriceB2VO();
		vo.setPk_loadprice_b2(pk);
		vo.setPk_wds_teamdoc_h(teampk);
		vo.setNloadprice(nloadprice);
		checkLoadpriceB2VO(vo);
		getQuickDlg().setParentVO(vo);
		getQuickDlg().setEnabled(true);
		getQuickDlg().showModal();
	}

	private WDSVEditDlg getQuickDlg() {
		if (null == m_quickDlg) {
			m_quickDlg = new WDSVEditDlg(getUI());
		}
		return m_quickDlg;
	}

	private ClientUI getUI() {
		return (ClientUI) this.getBillUI();
	}

	private void checkLoadpriceB2VO(LoadpriceB2VO vo) throws BusinessException {
		String error = null;
		if (vo.getPk_loadprice_b2() == null
				|| vo.getPk_loadprice_b2().length() == 0) {
			error = "请先保存当前的班组信息";
		} else if (vo.getPk_wds_teamdoc_h() == null
				|| vo.getPk_wds_teamdoc_h().length() == 0) {
			error = "请先录入当前的班组信息";
		} else if (vo.getNloadprice() == null
				|| vo.getNloadprice().doubleValue() == 0) {
			error = "请先录入当前的班组费用";
		}
		if (null != error && error.length() > 0) {
			throw new BusinessException(error);
		}
	}
}

package nc.ui.wds.ic.transfer;

import javax.swing.JComponent;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.ic.pub.OutPubClientUI;
import nc.ui.wds.w8004040204.ssButtun.fzgnBtn;
import nc.ui.wds.w8004040204.ssButtun.zdqhBtn;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.field.BillField;
import nc.vo.trade.field.IBillField;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wds.transfer.TransferVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientUI extends OutPubClientUI implements
		BillCardBeforeEditListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7678509534612908946L;

	public ClientUI() {
		super();
		initSelfData();
		init();
	}

	private void init() {
	}

	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}

	public String getBillType() {
		return "HWTZ";
	}

	protected void initSelfData() {
		// ��ȥ�в������ఴť
		ButtonObject btnobj = getButtonManager().getButton(IBillButton.Line);
		if (btnobj != null) {
			btnobj.removeChildButton(getButtonManager().getButton(
					IBillButton.CopyLine));
			btnobj.removeChildButton(getButtonManager().getButton(
					IBillButton.PasteLine));
			btnobj.removeChildButton(getButtonManager().getButton(
					IBillButton.InsLine));
		}
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);

	}

	@Override
	protected AbstractManageController createController() {
		return new ClientUICtrl(getBillType());
	}

	@Override
	protected ManageEventHandler createEventHandler() {
		return new EventHandler(this, getUIControl());
	}
	@Override
	protected IBillField createBillField() {
		return BillField.getInstance();
	}
	protected String getBillNo() throws java.lang.Exception {
		return HYPubBO_Client.getBillNo(getBillType(), _getOperator(), null,
				null);
	}

	public boolean isSaveAndCommitTogether() {
		return true;
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		int row = e.getRow();
		if (e.getPos() == BillItem.BODY) {
			if ("ntagnum".equalsIgnoreCase(key)) {
				UFBoolean fistag = PuPubVO.getUFBoolean_NullAs(
						getBillCardPanel().getBodyValueAt(row, "fistag"),
						UFBoolean.FALSE);
				if (fistag.booleanValue()) {
					return true;
				}
				return false;
			}
			String csourcetype = PuPubVO
					.getString_TrimZeroLenAsNull(getBillCardPanel()
							.getBodyValueAt(row, "csourcetype"));

			// �ڰ�����Ϣ�� ͨ����ͷ�Ĳֿ�԰�����в��չ���
			if ("teamcode".equals(key)) {// ����
				// �ֿ�id
				Object a = getBillCardPanel().getHeadItem("srl_pk")
						.getValueObject();
				if (a == null) {
					showWarningMessage("��ѡ��ֿ�");
					return false;
				}
				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
						.getBodyItem("teamcode").getComponent();
				if (null != a && !"".equals(a)) {
					// �޸Ĳ��� ���� �������� ָ���ֿ�id
					panel.getRefModel().addWherePart(
							" and wds_teamdoc_h.vdef1 = '" + a + "' ");
				}
			}

			// ����ǲ��չ����Ĵ�������Ա༭ ����������Ƶ��ݿ��Ա༭
			if ("ccunhuobianma".equalsIgnoreCase(key)) {
				if (getBillOperate() == IBillOperate.OP_REFADD)
					return false;
				if (csourcetype != null) {
					return false;
				} else {
					String pk_cargdoc = (String) getBillCardPanel()
							.getHeadItem("pk_cargdoc").getValueObject();
					if (null == pk_cargdoc || "".equalsIgnoreCase(pk_cargdoc)) {
						showWarningMessage("ǰѡ������λ");
						return false;
					}
					JComponent c = getBillCardPanel().getBodyItem(
							"ccunhuobianma").getComponent();
					if (c instanceof UIRefPane) {
						UIRefPane ref = (UIRefPane) c;
						ref.getRefModel().addWherePart(
								"  and tb_spacegoods.pk_cargdoc='" + pk_cargdoc
										+ "' ");
					}
					return true;
				}
			} else if ("nshouldoutnum".equalsIgnoreCase(key)) {
				// zhf add
				if (getBillOperate() == IBillOperate.OP_REFADD)
					return false;
				if (csourcetype != null) {

					return false;
				} else {
					return true;
				}
			} else if ("nshouldoutassistnum".equalsIgnoreCase(key)) {
				// zhf add
				if (getBillOperate() == IBillOperate.OP_REFADD)
					return false;
				if (csourcetype != null) {
					return false;
				} else {
					return true;
				}
			}

		}
		return super.beforeEdit(e);
	}

	public void setDefaultData() throws Exception {
		// ��ǰ��˾ ��ǰ�����֯ ��ǰ�ֿ� ��ǰ��λ
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPrimaryKey());
		getBillCardPanel().setHeadItem("pk_calbody",
				WdsWlPubConst.DEFAULT_CALBODY);
		if (getBillOperate() != IBillOperate.OP_REFADD) {
			try {
				getBillCardPanel().setHeadItem("srl_pk",
						getLoginInforHelper().getCwhid(_getOperator()));
				getBillCardPanel().setHeadItem(
						"pk_cargdoc",
						getLoginInforHelper().getSpaceByLogUserForStore(
								_getOperator()));
				// getBillCardPanel().setHeadItem("",
				// LoginInforHelper.getLogInfor(userid));
			} catch (Exception e) {
				e.printStackTrace();// zhf �쳣������
			}
		}
		// �Ƶ��� �Ƶ�����
		getBillCardPanel().setTailItem("tmaketime", _getServerTime());
		getBillCardPanel().setHeadItem("dbilldate", _getDate());
		getBillCardPanel().setTailItem("voperatorid", _getOperator());
		getBillCardPanel().setHeadItem("pk_billtype", getBillType());
		// getBillCardPanel().setHeadItem("is_yundan", UFBoolean.TRUE);
		// ����״̬
		getBillCardPanel().getHeadTailItem("vbillstatus").setValue(
				IBillStatus.FREE); // ����״̬
		try {
			getBillCardPanel().setHeadItem("vbillno", getBillNo());
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		String key = e.getKey();
		if (e.getPos() == BillItem.HEAD) {
			if ("srl_pk".equalsIgnoreCase(key)) {
				// �ֿ� Ϊ�� �� ��λ��ֹ�༭����֮ ��λ�ɱ༭
				boolean isEditable = true;
				if (PuPubVO.getString_TrimZeroLenAsNull(e.getValue()) == null) {
					isEditable = false;
				}
				getBillCardPanel().getHeadItem("pk_cargdoc").setEnabled(
						isEditable);
				getBillCardPanel().getHeadItem("pk_cargdoc").setValue(null);
			}
			if ("cdptid".equalsIgnoreCase(key)) {
				getBillCardPanel().getHeadItem("cbizid").setValue(null);
			} else if (key.equalsIgnoreCase("cbizid")) {
				getBillCardPanel().execHeadTailLoadFormulas();
			} else if (key.equalsIgnoreCase("pk_cargdoc2")) {// ��ͷ�����λ
				String cargdoc = PuPubVO
						.getString_TrimZeroLenAsNull(getBillCardPanel()
								.getHeadItem("pk_cargdoc2").getValueObject());
				if (cargdoc != null) {
					int rowcount = getBillCardPanel().getBillTable()
							.getRowCount();
					for (int i = 0; i < rowcount; i++) {
						if (PuPubVO
								.getString_TrimZeroLenAsNull(getBillCardPanel()
										.getBodyValueAt(i, "pk_cargdoc2")) == null) {
							getBillCardPanel().setBodyValueAt(cargdoc, i,
									"pk_cargdoc2");
							getBillCardPanel().execBodyFormula(i,
									"pk_cargdoc2");
						}
					}
				}
			}
		} else {
			if ("fistag".equalsIgnoreCase(key)) {// ����ѡ��ǩ��ʱ����ǩ�������
				int row = e.getRow();
				Object o = e.getValue();
				if (UFBoolean.FALSE.equals(o)) {
					getBillCardPanel().setBodyValueAt(null, row, "ntagnum");
				}
			} else if ("ccunhuobianma".equalsIgnoreCase(key)) {// ����ѡ��ǩ��ʱ����ǩ�������
				int row = e.getRow();
				if (null == PuPubVO
						.getString_TrimZeroLenAsNull(getBillCardPanel()
								.getBodyValueAt(row, "pk_cargdoc2"))) {
					String cargdoc = PuPubVO
							.getString_TrimZeroLenAsNull(getBillCardPanel()
									.getHeadItem("pk_cargdoc2")
									.getValueObject());
					if (cargdoc != null) {
						getBillCardPanel().setBodyValueAt(cargdoc, row,
								"pk_cargdoc2");
						getBillCardPanel().execBodyFormula(row, "pk_cargdoc2");
					}
				}
			}
		}
	}

	@Override
	protected void initEventListener() {

		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);

	}

	/**
	 * ��ͷ�ı༭ǰ�¼�
	 */
	public boolean beforeEdit(BillItemEvent e) {
		String key = e.getItem().getKey();
		if (e.getItem().getPos() == BillItem.HEAD) {
			// �ֿ���ˣ�ֻ��������ϵͳ��
			// srl_pkr Ϊ���ֿ�
			if ("srl_pk".equalsIgnoreCase(key)) {
				JComponent c = getBillCardPanel().getHeadItem("srl_pk")
						.getComponent();
				if (c instanceof UIRefPane) {
					UIRefPane ref = (UIRefPane) c;
					ref.getRefModel().addWherePart(
							" and def1 = '1' and isnull(dr,0) = 0");
				}
			}
			// ���������Ĳֿ�
			if ("srl_pkr".equalsIgnoreCase(key)) {
				JComponent c = getBillCardPanel().getHeadItem("srl_pkr")
						.getComponent();
				if (c instanceof UIRefPane) {
					UIRefPane ref = (UIRefPane) c;
					ref.getRefModel().addWherePart(
							" and def1 = '1' and isnull(dr,0) = 0");
				}
			}
			// ���ݲֿ���˻�λ
			if ("pk_cargdoc".equals(key)) {// �����λ
				// �ֿ�id
				Object a = getBillCardPanel().getHeadItem("srl_pk")
						.getValueObject();
				if (a == null) {
					showWarningMessage("��ѡ��ֿ�");
					return false;
				}
				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
						.getHeadItem("pk_cargdoc").getComponent();
				if (null != a && !"".equals(a)) {
					// �޸Ĳ��� ���� �������� ָ���ֿ�id
					panel.getRefModel().addWherePart(
							" and bd_stordoc.pk_stordoc = '" + a + "' ");
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
				JComponent c = getBillCardPanel().getHeadItem("cwhsmanagerid")
						.getComponent();
				if (c instanceof UIRefPane) {
					UIRefPane ref = (UIRefPane) c;
					ref.getRefModel().addWherePart(
							"  and tb_stockstaff.pk_cargdoc='" + pk_cargdoc
									+ "' ");
				}
				return true;
			}
		}
		return true;
	}

	@Override
	protected void initPrivateButton() {
		super.initPrivateButton();
		fzgnBtn customizeButton5 = new fzgnBtn();
		addPrivateButton(customizeButton5.getButtonVO());
		zdqhBtn customizeButton7 = new zdqhBtn();
		addPrivateButton(customizeButton7.getButtonVO());
	}

	public void afterUpdate() {

	}

	protected BusinessDelegator createBusinessDelegator() {
		return new Delegator();
	}

}

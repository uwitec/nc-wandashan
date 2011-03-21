package nc.ui.wds.w8004040218;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.wds.w8004040204.Iw8004040204;
import nc.itf.wds.w8004040210.Iw8004040210;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.BusinessAction;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.businessaction.IBusinessController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.INormalQuery;
import nc.ui.wds.w8000.CommonUnit;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;
import nc.ui.wds.w8004040210.TrayAction;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.to.pub.ConstVO;
import nc.vo.wds.pub.WDSTools;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
import nc.vo.wds.w8004040210.TbGeneralBBVO;
import nc.vo.wds.w8004040212.TbWarehousestockVO;
import nc.vo.wds.w8004040218.MyBillVO;
import nc.vo.wds.w8004061002.BdCargdocTrayVO;
import nc.vo.wds.w80060604.SoSaleVO;
import nc.vo.wds.w80060604.SoSaleorderBVO;
import nc.vo.wds.w80060608.IcGeneralBVO;
import nc.vo.wds.w80060608.IcGeneralHVO;
import nc.vo.wds.w8006080206.TbStorcubasdocVO;

/**
 * 
 * ���۳���
 * 
 * @author author xzs
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	MyClientUI myClientUI = null;

	// ���ýӿ�
	private Iw8004040204 iw = null;

	private SaleOrderDlg saleOrderDlg = null; // ��ѯ��������

	boolean isAdd = false;

	boolean opType = false; // �Ƿ���Ʒ
	private boolean isedit = true;// �Ƿ����޸�
	private String st_type = "";// ���

	// private List hiddenList = null;
	private String pk_stock = ""; // ��ǰ��¼�߶�Ӧ�Ĳֿ�����
	IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());

	private Integer isControl = 0; // �Ƿ���Ȩ�޲�����ǰ���� 1Ϊ����ǩ�ֵ��û� 0 ���ܽ����κβ��� 2 ���Խ��г���
	private boolean isStock = false; // �Ƿ�Ϊ�ܲ� true �ܲ� false �ֲ�
	// ���ݵ�ǰ��¼�߲�ѯ�����ֿ����ֿ����洢�Ĳ�Ʒ
	private List pkList = null;

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;
		// ��ʼ���ӿ�
		this.setIw((Iw8004040204) NCLocator.getInstance().lookup(
				Iw8004040204.class.getName()));
		// ����ʲô��ɫ��һ�μ�������ָ����ť���Զ�ȡ�������ò�����
		changeButton(false);
		try {
			// ��ȡ��ǰ��¼�߲�ѯ��Ա�󶨱� �������ĸ��ֿ⣬
			pk_stock = CommonUnit.getStordocName(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
			// ��ȡ�û�����
			String isType = CommonUnit.getUserType(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
			st_type = isType;
			if (null != isType && isType.equals("0")) {
				isStock = CommonUnit.getSotckIsTotal(pk_stock);
				pkList = CommonUnit.getInvbasdoc_Pk(ClientEnvironment
						.getInstance().getUser().getPrimaryKey());
				isControl = 2;
			} else if (null != isType && isType.equals("1")) {
				isControl = 1;
				// getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				// myClientUI.updateButtons();
			} else {
				isControl = 0;
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// // �ж��û����
		// String st_type = "";
		// try {
		// st_type = nc.ui.wds.w8000.CommonUnit.getUserType(ClientEnvironment
		// .getInstance().getUser().getPrimaryKey());
		// } catch (BusinessException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// if (null != st_type && !"".equals(st_type)) {
		// if (!"0".equals(st_type)) {
		// getButtonManager().getButton(IBillButton.Add).setEnabled(false);
		// }
		// }

		// getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
	}

	protected IBusinessController createBusinessAction() {
		// TODO Auto-generated method stub
		switch (getUIController().getBusinessActionType()) {
		case IBusinessActionType.PLATFORM:

			return new BusinessAction(getBillUI());
		case IBusinessActionType.BD:

			return new TrayAction(getBillUI());

		default:

			return new BusinessAction(getBillUI());
		}

	}

	// �����ɫ
	private void noColor(TbOutgeneralBVO[] generalb) {
		if (null != generalb && generalb.length > 0) {
			for (int i = 0; i < generalb.length; i++) {
				getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
						.setCellBackGround(i, "ccunhuobianma", Color.white);
			}
		}
	}

	@Override
	protected void onBoEdit() throws Exception {
		// �ֶܲ����ǿ��Ա����ʱ�ģ�
		if (!"3".equals(st_type)) {

			if (isControl == 2) {
				super.onBoEdit();
				// ��������ָ����ť���ã��޸�״̬
				isAdd = false;
				getButtonManager().getButton(ISsButtun.tpzd).setEnabled(true);
				myClientUI.updateButtons();
			} else {
				myClientUI.showErrorMessage("��û��Ȩ��");
			}
		}

	}

	/**
	 * ���ݲ����ı�����ָ�����Զ�ȡ����ť�Ƿ����
	 * 
	 * @param type
	 */
	private void changeButton(boolean type) {
		getButtonManager().getButton(ISsButtun.zdqh).setEnabled(type);
		getButtonManager().getButton(ISsButtun.tpzd).setEnabled(type);
		getButtonManager().getButton(
				nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr).setEnabled(type);
		getButtonManager().getButton(
				nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz).setEnabled(type);
		myClientUI.updateButtons();
	}

	// ˢ��
	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
	}

	@Override
	protected void onBoQuery() throws Exception {
		String st_type = "";
		try {
			st_type = nc.ui.wds.w8000.CommonUnit.getUserType(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ
		strWhere.append(" and vsourcebillcode like '%��%' and dr=0 ");

		if ("0".equals(st_type)) {
			String pk_stordoc = nc.ui.wds.w8000.CommonUnit
					.getStordocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());

			strWhere.append(" and srl_pk='" + pk_stordoc + "' ");

		}
		if ("3".equals(st_type)) {
			if (!isStock) {
				String pk_stordoc = nc.ui.wds.w8000.CommonUnit
						.getStordocName(ClientEnvironment.getInstance()
								.getUser().getPrimaryKey());

				strWhere.append(" and srl_pk='" + pk_stordoc + "' ");
			}

		}

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
//		super.onBoReturn();
	}

	/**
	 * ��д�÷������޸�(isnull(dr,0)=0) ������ѯ�Ի������û�ѯ�ʲ�ѯ������
	 * ����û��ڶԻ������ˡ�ȷ��������ô����true,���򷵻�false ��ѯ����ͨ�������StringBuffer���ظ�������
	 * 
	 * @param sqlWhereBuf
	 *            �����ѯ������StringBuffer
	 * @return �û�ѡȷ������true���򷵻�false
	 */
	protected boolean askForQueryCondition(StringBuffer sqlWhereBuf)
			throws Exception {
		if (sqlWhereBuf == null)
			throw new IllegalArgumentException(
					"askForQueryCondition().sqlWhereBuf cann't be null");
		UIDialog querydialog = getQueryUI();

		if (querydialog.showModal() != UIDialog.ID_OK)
			return false;
		INormalQuery query = (INormalQuery) querydialog;

		String strWhere = query.getWhereSql();
		if (strWhere == null)
			strWhere = "1=1";

		if (getButtonManager().getButton(IBillButton.Busitype) != null) {
			if (getBillIsUseBusiCode().booleanValue())
				// ҵ�����ͱ���
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_BusiCode() + "='"
						+ getBillUI().getBusicode() + "'";

			else
				// ҵ������
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_Busitype() + "='"
						+ getBillUI().getBusinessType() + "'";

		}

		strWhere = "(" + strWhere + ") and (isnull(tb_outgeneral_h.dr,0)=0)";

		if (getHeadCondition() != null)
			strWhere = strWhere + " and " + getHeadCondition();
		// ��������ֱ�Ӱ����ƴ�õĴ��ŵ�StringBuffer�ж���ȥ�Ż�ƴ���Ĺ���
		sqlWhereBuf.append(strWhere);
		return true;
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub

		// �ж��û����
		String st_type = "";
		try {
			st_type = nc.ui.wds.w8000.CommonUnit.getUserType(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null != st_type && !"".equals(st_type)) {
			if (!"0".equals(st_type) && !"3".equals(st_type)) {
				getBillUI().showErrorMessage("��û��Ȩ�޲����˵��ݣ�");
				return;
			}
		} else {
			getBillUI().showErrorMessage("��û��Ȩ�޲����˵��ݣ�");
			return;
		}
		if ("0".equals(st_type)) {
			String pk_stordoc = nc.ui.wds.w8000.CommonUnit
					.getStordocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
			String pk_stordocnow = "";
			if (null != getBillCardPanelWrapper().getBillCardPanel()
					.getHeadTailItem("srl_pk")) {
				pk_stordocnow = getBillCardPanelWrapper().getBillCardPanel()
						.getHeadTailItem("srl_pk").getValue();
			}

			if (!pk_stordoc.equals(pk_stordocnow)) {
				getBillUI().showErrorMessage("���ֿ�ͱ���Ա�ֿⲻ�������ܱ��棡");
				return;
			}
		}
		if ("3".equals(st_type)) {
			String pk_stordoc = nc.ui.wds.w8000.CommonUnit
					.getStordocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
			String pk_stordocnow = "";
			if (null != getBillCardPanelWrapper().getBillCardPanel()
					.getHeadTailItem("srl_pk")) {
				pk_stordocnow = getBillCardPanelWrapper().getBillCardPanel()
						.getHeadTailItem("srl_pk").getValue();
			}

			if (!pk_stordoc.equals(pk_stordocnow)) {
				getBillUI().showErrorMessage("���ֿ�ͱ���Ա�ֿⲻ�������ܱ��棡");
				return;
			}
		}
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());

		int bodyrownum = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getRowCount();
		if (bodyrownum == 0) {
			getBillUI().showErrorMessage("�ӱ���Ϊ�գ�");
			return;
		}
		// ��֤��Ʒ����
		for (int i = 0; i < bodyrownum; i++) {
			String geb_cinvbasidy = (String) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getValueAt(i,
							"cinventoryid");
			if (null == geb_cinvbasidy || "".equals(geb_cinvbasidy)) {
				getBillUI().showErrorMessage("��Ʒ����Ϊ��!");
				return;
			}
			// ��֤���κ��Ƿ�Ϊ��
			String geb_vbatchcodey = (String) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getValueAt(i,
							"vbatchcode");
			if (null == geb_vbatchcodey || "".equals(geb_vbatchcodey)) {
				getBillUI().showErrorMessage("���κŲ���Ϊ��!");
				return;
			}
			// ��֤Ӧ������
			UFDouble geb_bsnumy = (UFDouble) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getValueAt(i,
							"nshouldoutassistnum");
			if (null == geb_bsnumy) {
				getBillUI().showErrorMessage("Ӧ����������Ϊ��!");
				return;
			}
			// ��֤���κ��Ƿ���ȷ
			if (geb_vbatchcodey.trim().length() < 8) {
				getBillUI().showErrorMessage("���κŲ���С��8λ!");
				return;
			}

			Pattern p = Pattern
					.compile(
							"^((((1[6-9]|[2-9]\\d)\\d{2})(0?[13578]|1[02])(0?[1-9]|[12]\\d|3[01]))|"
									+ "(((1[6-9]|[2-9]\\d)\\d{2})(0?[13456789]|1[012])(0?[1-9]|[12]\\d|30))|"
									+ "(((1[6-9]|[2-9]\\d)\\d{2})0?2(0?[1-9]|1\\d|2[0-8]))|"
									+ "(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))0?229))$",
							Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			Matcher m = p.matcher(geb_vbatchcodey.trim().substring(0, 8));
			if (!m.find()) {
				getBillUI().showErrorMessage(
						"���κ�����Ĳ���ȷ,����������ȷ������!�磺20100101XXXXXX");
				return;
			}

		}
		// �Ƿ������

		// �жϵ�������Ƿ����
		if ("0".equals(st_type)) {
			int rowNum = getBillCardPanelWrapper().getBillCardPanel()
					.getBillTable().getRowCount();
			for (int i = 0; i < rowNum; i++) {
				double geb_bsnum = 0;
				double geb_snum = 0;
				double geb_banum = 0;
				double geb_anum = 0;
				// Ӧ�븨����
				if (null != getBillCardPanelWrapper().getBillCardPanel()
						.getBillModel().getValueAt(i, "nshouldoutassistnum")) {
					geb_bsnum = Double.parseDouble(getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"nshouldoutassistnum").toString());
				}
				// Ӧ������
				if (null != getBillCardPanelWrapper().getBillCardPanel()
						.getBillModel().getValueAt(i, "nshouldoutnum")) {
					geb_snum = Double.parseDouble(getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"nshouldoutnum").toString());
				}
				// ʵ�븨����
				if (null != getBillCardPanelWrapper().getBillCardPanel()
						.getBillModel().getValueAt(i, "noutassistnum")) {
					geb_banum = Double.parseDouble(getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"noutassistnum").toString());
				}
				// ʵ������
				if (null != getBillCardPanelWrapper().getBillCardPanel()
						.getBillModel().getValueAt(i, "noutnum")) {
					geb_anum = Double.parseDouble(getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"noutnum").toString());
				}
				if (geb_bsnum != geb_banum || geb_snum != geb_anum) {
					getBillUI().showErrorMessage("Ӧ��⡢ʵ����������ȣ����ܱ��棡");
					return;
				}
			}
		}
		if ("3".equals(st_type)) {
			int rowNum = getBillCardPanelWrapper().getBillCardPanel()
					.getBillTable().getRowCount();
			for (int i = 0; i < rowNum; i++) {
				double geb_bsnum = 0;
				double geb_snum = 0;
				double geb_banum = 0;
				double geb_anum = 0;
				// Ӧ�븨����
				if (null != getBillCardPanelWrapper().getBillCardPanel()
						.getBillModel().getValueAt(i, "nshouldoutassistnum")) {
					geb_bsnum = Double.parseDouble(getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"nshouldoutassistnum").toString());
				}
				// Ӧ������
				if (null != getBillCardPanelWrapper().getBillCardPanel()
						.getBillModel().getValueAt(i, "nshouldoutnum")) {
					geb_snum = Double.parseDouble(getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"nshouldoutnum").toString());
				}
				// ʵ�븨����
				if (null != getBillCardPanelWrapper().getBillCardPanel()
						.getBillModel().getValueAt(i, "noutassistnum")) {
					geb_banum = Double.parseDouble(getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"noutassistnum").toString());
				}
				// ʵ������
				if (null != getBillCardPanelWrapper().getBillCardPanel()
						.getBillModel().getValueAt(i, "noutnum")) {
					geb_anum = Double.parseDouble(getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"noutnum").toString());
				}
				if (geb_bsnum != geb_banum || geb_snum != geb_anum) {
					getBillUI().showErrorMessage("Ӧ��⡢ʵ����������ȣ����ܱ��棡");
					return;
				}
			}
		}

		// �õ�ȫ������VO

		TbOutgeneralBVO[] tbGeneralBVOss = null;
		if (getBillManageUI().isListPanelSelected()) {
			// if (getBufferData().getVOBufferSize() != 0) {
			tbGeneralBVOss = (TbOutgeneralBVO[]) getBillManageUI()
					.getBillListWrapper().getVOFromUI().getChildrenVO();
			// }
		} else {
			// if (getBufferData().getVOBufferSize() != 0) {
			tbGeneralBVOss = (TbOutgeneralBVO[]) getBillCardPanelWrapper()
					.getBillVOFromUI().getChildrenVO();
			// }
		}
		// �жϱ����Ƿ�������
		if (null == tbGeneralBVOss || tbGeneralBVOss.length == 0) {
			getBillUI().showErrorMessage("������û�����ݣ����ܽ��в�������Ȩ������");
			return;
		}

		// if (null != st_type && !"".equals(st_type)) {
		// if ("1".equals(st_type)) {
		// super.onBoSave();
		// return;
		// }
		// }

		// getBillCardPanelWrapper().getBillCardPanel()
		// .getHeadItem("geh_billtype").setValue(new Integer(0));
		AggregatedValueObject billVO = getBillUI().getVOFromUI();

		// if (!addoredit) {
		// for (int i = 0; i < billVO.getChildrenVO().length; i++) {
		// billVO.getChildrenVO()[i].setStatus(VOStatus.UPDATED);
		// }
		// } else {
		// for (int i = 0; i < billVO.getChildrenVO().length; i++) {
		// billVO.getChildrenVO()[i].setStatus(VOStatus.NEW);
		//
		// }
		// }

		billVO.getChildrenVO()[0].setStatus(1);
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);

		//
		TbOutgeneralBVO[] tbGeneralBVOas = (TbOutgeneralBVO[]) billVO
				.getChildrenVO();
		// ��֤�����Ƿ�������
		if (null == tbGeneralBVOas || tbGeneralBVOas.length < 0) {
			myClientUI.showErrorMessage("����ʧ��,����Ȩ����");
			return;
		}
		// ѭ����֤�Ƿ���û�г���Ĳ�Ʒ

		for (int i = 0; i < tbGeneralBVOas.length; i++) {
			TbOutgeneralBVO genb = tbGeneralBVOas[i];
			if (null != genb.getNshouldoutassistnum()
					&& !"".equals(genb.getNshouldoutassistnum())) {
				if (null != genb.getNoutassistnum()
						&& !"".equals(genb.getNoutassistnum())) {
					if (genb.getNshouldoutassistnum().doubleValue() != genb
							.getNoutassistnum().doubleValue()) {

						getBillUI().showErrorMessage("���в�Ʒû��ָ�����");
						return;
					}
				}
			}
		}

		// �ֺܲͷֲֵ�
		boolean addoredit = true;

		TbOutgeneralHVO generalh = (TbOutgeneralHVO) billVO.getParentVO();
		if (isStock) {

			// ���ݱ���Ա��Ʒ��������޸��Լ���Ʒ�ӱ�
			List invLisk = nc.ui.wds.w8000.CommonUnit
					.getInvbasdoc_Pk(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());

			TbOutgeneralHVO tmpgeneralh = null;
			// ������Դ���ݺŲ�ѯ�Ƿ�����������
			String strWhere = " vbillcode='" + generalh.getVbillcode()
					+ "' and csourcebillhid='" + generalh.getCsourcebillhid()
					+ "' and dr=0 ";
			ArrayList tmpList = (ArrayList) query.retrieveByClause(
					TbOutgeneralHVO.class, strWhere);
			if (null != tmpList && tmpList.size() > 0) {
				tmpgeneralh = (TbOutgeneralHVO) tmpList.get(0);
			}
			// boolean myisadd=true;
			// �õ����й������¼
			if (null != tmpgeneralh) {
				// �ѱ�ͷ�滻
				generalh = tmpgeneralh;
				String gebbvosql = " dr=0 and general_pk ='"
						+ generalh.getGeneral_pk() + "' and cinventoryid in ('";
				for (int k = 0; k < invLisk.size(); k++) {
					if (null != invLisk && invLisk.size() > 0
							&& null != invLisk.get(k)
							&& !"".equals(invLisk.get(k))) {
						gebbvosql += invLisk.get(k) + "','";
					}
				}
				gebbvosql += "') ";
				ArrayList gebbvos = (ArrayList) query.retrieveByClause(
						TbOutgeneralBVO.class, gebbvosql);
				if (null != gebbvos && gebbvos.size() > 0) {
					addoredit = false;
				} else {
					addoredit = true;
				}

			} else {
				addoredit = true;
				// myisadd=true;
				// �Ƶ�ʱ��
				generalh.setTmaketime(myClientUI._getServerTime().toString());
				// �Ƶ����ڣ���������
				generalh.setDbilldate(new UFDate(new Date()));
				// generalh.setGeh_dbilldate(new UFDate(new Date()));
				// ���õ��ݺ�
				generalh.setVbillcode(CommonUnit.getBillCode("4A",
						ClientEnvironment.getInstance().getCorporation()
								.getPk_corp(), "", ""));
			}

			// ������������ñ���״̬��������
			if (addoredit) {
				// ѭ���������״̬
				for (int i = 0; i < tbGeneralBVOas.length; i++) {
					tbGeneralBVOas[i].setStatus(VOStatus.NEW);
				}
			} else { // ���ñ���״̬���޸ġ�
				// ѭ���������״̬
				for (int i = 0; i < tbGeneralBVOas.length; i++) {
					tbGeneralBVOas[i].setStatus(VOStatus.UPDATED);
				}
			}
		} else {
			// ���ݱ���Ա��Ʒ��������޸��Լ���Ʒ�ӱ�
			List invLisk = nc.ui.wds.w8000.CommonUnit
					.getInvbasdoc_Pk(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());

			TbOutgeneralHVO tmpgeneralh = null;
			// ������Դ���ݺŲ�ѯ�Ƿ�����������
			String strWhere = " vbillcode='" + generalh.getVbillcode()
					+ "' and csourcebillhid='" + generalh.getCsourcebillhid()
					+ "' and dr=0 ";
			ArrayList tmpList = (ArrayList) query.retrieveByClause(
					TbOutgeneralHVO.class, strWhere);
			if (null != tmpList && tmpList.size() > 0) {
				tmpgeneralh = (TbOutgeneralHVO) tmpList.get(0);
				addoredit = false;
			} else {
				addoredit = true;
				// �Ƶ�ʱ��
				generalh.setTmaketime(myClientUI._getServerTime().toString());
				// �Ƶ����ڣ���������
				generalh.setDbilldate(new UFDate(new Date()));
				// generalh.setGeh_dbilldate(new UFDate(new Date()));
				// ���õ��ݺ�
				generalh.setVbillcode(CommonUnit.getBillCode("4A",
						ClientEnvironment.getInstance().getCorporation()
								.getPk_corp(), "", ""));
			}
			// ������������ñ���״̬��������
			if (addoredit) {
				// ѭ���������״̬
				for (int i = 0; i < tbGeneralBVOas.length; i++) {
					tbGeneralBVOas[i].setStatus(VOStatus.NEW);
				}
			} else { // ���ñ���״̬���޸ġ�
				// ѭ���������״̬
				for (int i = 0; i < tbGeneralBVOas.length; i++) {
					tbGeneralBVOas[i].setStatus(VOStatus.UPDATED);
				}
			}

		}
		// ����޸�ʱ��
		generalh.setTlastmoditime(myClientUI._getServerTime().toString());
		// �����޸���
		generalh.setClastmodiid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey());
		// ����״̬
		generalh.setVbillstatus(1);
		// ���ۺ�VO�б�ͷ��ֵ
		billVO.setParentVO(generalh);
		// ���帳ֵ
		billVO.setChildrenVO(tbGeneralBVOas);

		// // �����Ƿ�ر�
		// TbGeneralBVO[] tbGenBVOs = (TbGeneralBVO[]) billVO.getChildrenVO();
		// // ��;�����ӱ�
		// ArrayList vgebvos = new ArrayList();
		// // �ڿ��洢
		// double geb_virtualbnumy = 0;
		// double geb_virtualnumy = 0;
		//
		// for (int i = 0; i < tbGenBVOs.length; i++) {
		// if (null != tbGenBVOs[i].getGeb_banum()
		// && null != tbGenBVOs[i].getGeb_anum()) {
		// if (tbGenBVOs[i].getGeb_bsnum().toDouble().doubleValue() ==
		// tbGenBVOs[i]
		// .getGeb_banum().toDouble().doubleValue()
		// && tbGenBVOs[i].getGeb_anum().toDouble().doubleValue() ==
		// tbGenBVOs[i]
		// .getGeb_snum().toDouble().doubleValue()) {
		// ((TbGeneralBVO[]) billVO.getChildrenVO())[i]
		// .setGeb_isclose(new UFBoolean("Y"));
		// } else {
		// ((TbGeneralBVO[]) billVO.getChildrenVO())[i]
		// .setGeb_isclose(new UFBoolean("N"));
		// }
		// } else {
		// ((TbGeneralBVO[]) billVO.getChildrenVO())[i]
		// .setGeb_isclose(new UFBoolean("N"));
		// }
		// }

		for (int i = 0; i < billVO.getChildrenVO().length; i++) {
			if (null == ((TbOutgeneralBVO[]) billVO.getChildrenVO())[i]
					.getLvbatchcode()
					|| ""
							.equals(((TbOutgeneralBVO[]) billVO.getChildrenVO())[i]
									.getLvbatchcode().trim())) {
				((TbOutgeneralBVO[]) billVO.getChildrenVO())[i]
						.setLvbatchcode("2009");
			}

		}
		// �����������
		Object o = null;
		ISingleController sCtrl = null;
		if (getUIController() instanceof ISingleController) {
			sCtrl = (ISingleController) getUIController();
			if (sCtrl.isSingleDetail()) {
				o = billVO.getParentVO();
				billVO.setParentVO(null);
			} else {
				o = billVO.getChildrenVO();
				billVO.setChildrenVO(null);
			}
		}

		boolean isSave = true;

		// �ж��Ƿ��д�������
		if (billVO.getParentVO() == null
				&& (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0)) {
			isSave = false;
		} else {
			ArrayList params = new ArrayList();

			// ���Ҫ��д�ļ���VO
			// GeneralBillVO voTempBill = getVoTempBill();
			// ���淽��

			params.add(getBillUI().getUserObject());
			// ��д����
			params.add(null);
			// ��������
			TbOutgeneralBVO[] tbGeneralBVOs = (TbOutgeneralBVO[]) getBillUI()
					.getVOFromUI().getChildrenVO();
			//
			// TbGeneralHVO abs = (TbGeneralHVO) getBillUI().getVOFromUI()
			// .getParentVO();

			// ͨ�����ⵥ�ӱ������õ��������
			StringBuffer tgbbsql = new StringBuffer(" geb_pk in ('");
			if (null != tbGeneralBVOs && tbGeneralBVOs.length > 0) {
				for (int i = 0; i < tbGeneralBVOs.length; i++) {
					if (null != tbGeneralBVOs[i].getCsourcebillbid()
							&& !"".equals(tbGeneralBVOs[i].getCsourcebillbid())) {
						tgbbsql.append(tbGeneralBVOs[i].getCsourcebillbid());
						tgbbsql.append("','");
					}

				}
				tgbbsql.append("') and dr=0 ");
			}
			// �û�Ȩ���ж�

			// ���̻����

			ArrayList tbGeneralBBVOs = (ArrayList) query.retrieveByClause(
					TbGeneralBBVO.class, tgbbsql.toString());
			if (null == tbGeneralBBVOs || tbGeneralBBVOs.size() == 0) {
				getBillUI().showErrorMessage("��ָ�����̺��ٱ��棡");
				return;
			}
			// ���VO
			TbWarehousestockVO[] tbWarehousestockVO = new TbWarehousestockVO[tbGeneralBBVOs
					.size()];
			ArrayList bdCargdocTrayVOs = new ArrayList();
			String Cdt_pk = "";
			for (int i = 0; i < tbGeneralBBVOs.size(); i++) {
				if (null != tbGeneralBBVOs.get(i)) {
					TbGeneralBBVO tbbbvo = (TbGeneralBBVO) tbGeneralBBVOs
							.get(i);
					tbWarehousestockVO[i] = new TbWarehousestockVO();
					// ��������
					if (null != tbbbvo.getCdt_pk()
							&& !"".equals(tbbbvo.getCdt_pk())) {
						tbWarehousestockVO[i].setPplpt_pk(tbbbvo.getCdt_pk());
					}
					// dr
					tbWarehousestockVO[i].setDr(0);
					// ������
					if (null != tbbbvo.gebb_num) {
						tbWarehousestockVO[i].setWhs_stockpieces(tbbbvo
								.getGebb_num());
						tbWarehousestockVO[i].setWhs_oanum(tbbbvo.gebb_num);
					}
					// ������
					if (null != tbbbvo.getGebb_hsl()) {
						tbWarehousestockVO[i].setWhs_hsl(tbbbvo.getGebb_hsl());
					}
					// ����
					if (null != tbbbvo.getGebb_nprice()) {
						tbWarehousestockVO[i].setWhs_nprice(tbbbvo
								.getGebb_nprice());
					}
					// ���
					if (null != tbbbvo.getGebb_nmny()) {
						tbWarehousestockVO[i]
								.setWhs_nmny(tbbbvo.getGebb_nmny());
					}
					// ������
					if (null != tbbbvo.getGebb_num()
							&& null != tbbbvo.getGebb_hsl()) {
						tbWarehousestockVO[i].setWhs_stocktonnage(new UFDouble(
								tbbbvo.getGebb_num().toDouble()
										* tbbbvo.getGebb_hsl().toDouble()));
						tbWarehousestockVO[i].setWhs_omnum(new UFDouble(tbbbvo
								.getGebb_num().toDouble()
								* tbbbvo.getGebb_hsl().toDouble()));

					}

					// �����״̬(Ĭ�Ϻ���)
					tbWarehousestockVO[i].setSs_pk("0001AA100000000B1TYK");
					// ����״̬
					tbWarehousestockVO[i].setWhs_status(0);
					// ����
					tbWarehousestockVO[i].setWhs_type(1);
					//
					if (null != tbbbvo.getPwb_pk()
							&& !"".equals(tbbbvo.getPwb_pk())) {
						tbWarehousestockVO[i].setPk_bodysource(tbbbvo
								.getPwb_pk());
					}
					// �����������
					if (null != tbbbvo.getPk_invbasdoc()
							&& !"".equals(tbbbvo.getPk_invbasdoc())) {
						tbWarehousestockVO[i].setPk_invbasdoc(tbbbvo
								.getPk_invbasdoc());
					}
					// ���κ�
					if (null != tbbbvo.getGebb_vbatchcode()
							&& !"".equals(tbbbvo.getGebb_vbatchcode())) {
						tbWarehousestockVO[i].setWhs_batchcode(tbbbvo
								.getGebb_vbatchcode());
					}
					// ��д���κ�
					if (null != tbbbvo.getGebb_lvbatchcode()
							&& !"".equals(tbbbvo.getGebb_lvbatchcode())) {
						tbWarehousestockVO[i].setWhs_lbatchcode(tbbbvo
								.getGebb_lvbatchcode());
					} else {
						tbWarehousestockVO[i].setWhs_lbatchcode("2009");
					}
					// ����ʱ��
					tbWarehousestockVO[i].setOperatetime(new UFDateTime(
							new Date()));
					// ��λ
					String cargdocPK = getCargdocPK(ClientEnvironment
							.getInstance().getUser().getPrimaryKey());
					tbWarehousestockVO[i].setPk_cargdoc(cargdocPK);
					// // ��Դ���ݱ��������� ���������
					// tbWarehousestockVO[i].setPk_bodysource(tbbbvo
					// .getGebb_pk());
					//
					// if (!addoredit) {
					//						
					// }
					//
					// ����״̬

					if (null != tbbbvo.getCdt_pk()
							&& !"".equals(tbbbvo.getCdt_pk())) {
						Cdt_pk = tbbbvo.getCdt_pk();
					}
					BdCargdocTrayVO bdCargdocTrayVO = (BdCargdocTrayVO) query
							.retrieveByPK(BdCargdocTrayVO.class, Cdt_pk);
					bdCargdocTrayVO.setCdt_traystatus(new Integer(1));
					bdCargdocTrayVOs.add(bdCargdocTrayVO);

				}

			}
			params.add(tbWarehousestockVO);
			params.add(bdCargdocTrayVOs);
			// �Ƶ�ʱ��
			// if (addoredit) {
			// ((TbGeneralHVO) billVO.getParentVO())
			// .setTmaketime(getBillUI()._getServerTime()
			// .toString());
			// }

			//
			if (getBillUI().isSaveAndCommitTogether()) {
				billVO = getBusinessAction().saveAndCommit(billVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), checkVO);
			} else {

				// write to database

				billVO = getBusinessAction().save(billVO,
						getUIController().getBillType(), _getDate().toString(),
						params, checkVO);

			}

		}

		// �������ݻָ�����
		if (sCtrl != null) {
			if (sCtrl.isSingleDetail())
				billVO.setParentVO((CircularlyAccessibleValueObject) o);
		}
		int nCurrentRow = -1;
		if (isSave) {
			if (isEditing()) {
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(billVO);
					nCurrentRow = 0;

				} else {
					getBufferData().setCurrentVO(billVO);
					nCurrentRow = getBufferData().getCurrentRow();
				}
			}
			// �������������
			setAddNewOperate(isAdding(), billVO);
		}
		// ���ñ����״̬
		setSaveOperateState();
		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRow(nCurrentRow);
		}
		showZeroLikeNull(false);

		getButtonManager().getButton(
				nc.ui.wds.w8004040204.ssButtun.ISsButtun.zdqh)
				.setEnabled(false);
		getButtonManager().getButton(
				nc.ui.wds.w8004040204.ssButtun.ISsButtun.tpzd)
				.setEnabled(false);

		getButtonManager().getButton(IBillButton.Add).setEnabled(true);

		getBillUI().updateButtonUI();
		// ҳ���ǩ
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_corp")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_calbody")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "geh_cwarehouseid").setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "geh_cwhsmanagerid").setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_cbizid")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "geh_cdispatcherid").setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "geh_cothercorpid").setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "geh_cothercalbodyid").setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "geh_cotherwhid").setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fallocname")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vnote")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_crowno")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("invcode")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
		// "geb_vbatchcode").setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_bsnum")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_snum")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_banum")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_anum")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vnote")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
		// "geb_virtualbnum").setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
		// "geb_customize2").setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_nprice")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_nmny")
		// .setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
		// "geb_virtualbnum").setShow(false);

		// if (null != st_type && st_type.equals("1")
		// && getBufferData().getVOBufferSize() > 0) {
		//
		// AggregatedValueObject billvo = getBillUI().getVOFromUI();
		// TbGeneralHVO generalhvo = null;
		// if (null != billvo.getParentVO()) {
		// generalhvo = (TbGeneralHVO) billvo.getParentVO();
		// }
		//
		// //
		//
		// // ǩ�ֺ�
		// if (null != generalhvo.getPwb_fbillflag()
		// && generalhvo.getPwb_fbillflag() == 3) {
		// getButtonManager().getButton(
		// nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
		// .setEnabled(false);
		// getButtonManager().getButton(
		// nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
		// .setEnabled(true);
		// getBillUI().updateButtonUI();
		// } else { // ǩ��ǰ
		// getButtonManager().getButton(
		// nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
		// .setEnabled(true);
		// getButtonManager().getButton(
		// nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
		// .setEnabled(false);
		// getBillUI().updateButtonUI();
		// }
		// }
	}

	/**
	 * ���ݵ�¼��Ա������ѯ����Ӧ�Ļ�λ����
	 * 
	 * @param pk
	 *            ��Ա����
	 * @return �ֿ�����
	 * @throws BusinessException
	 */
	public static String getCargdocPK(String pk) throws BusinessException {
		String tmp = null;
		IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		String sql = "select pk_cargdoc from tb_stockstaff where dr = 0 and cuserid='"
				+ pk + "'";
		ArrayList list = (ArrayList) iuap.executeQuery(sql,
				new ArrayListProcessor());
		if (null != list && list.size() > 0) {
			Object[] a = (Object[]) list.get(0);
			if (null != a && a.length > 0 && null != a[0]) {
				tmp = a[0].toString();
			}
		}

		return tmp;
	}

	public void showZeroLikeNull(boolean bShowZeroLikeNull) {
		BillRendererVO vo = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getRendererVO();
		vo.setShowZeroLikeNull(bShowZeroLikeNull);
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
				.setShowFlags(vo);
	}

	// ��֤����
	public boolean validateItem(TbOutgeneralBVO[] generalBVO) throws Exception {
		// ��������
		Object bdilldate = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadTailItem("dbilldate").getValueObject();
		if (null == bdilldate || "".equals(bdilldate)) {
			getBillUI().showWarningMessage("�����õ�������");
			return false;
		}
		/***********************************************************************
		 * / �շ���� Object cdispatcherid =
		 * getBillCardPanelWrapper().getBillCardPanel()
		 * .getHeadTailItem("cdispatcherid").getValueObject(); if (null ==
		 * cdispatcherid || "".equals(cdispatcherid)) {
		 * getBillUI().showWarningMessage("�������շ����"); return false; }
		 **********************************************************************/
		if (null != generalBVO && generalBVO.length > 0) {
			TbOutgeneralBVO generalbvo = null;
			boolean resultCount = true;
			for (int i = 0; i < generalBVO.length; i++) {
				generalbvo = generalBVO[i];
				if (null == generalbvo.getNoutassistnum()
						|| "".equals(generalbvo.getNoutassistnum())) {
					resultCount = false;
					break;
				}
			}
			int result = getBillUI().showOkCancelMessage("���п���Ʒû��ָ������,�Ƿ񱣴�?");
			if (result == 1)
				return true;
			else
				return false;
		}
		return true;
	}

	/**
	 * ���ݵ�ǰҳ���ϵ�Ӧ��������ʵ���������бȽ�����ʾ��ɫ
	 * 
	 * @throws Exception
	 */
	public void chaneColor() throws Exception {
		TbOutgeneralBVO[] generalbVO = (TbOutgeneralBVO[]) getBillUI()
				.getVOFromUI().getChildrenVO();

		// ��ȡ���жϱ����Ƿ���ֵ������ѭ��
		if (null != generalbVO && generalbVO.length > 0) {
			for (int i = 0; i < generalbVO.length; i++) {
				// ��ȡ��ǰ�����е�Ӧ����������ʵ�����������бȽϣ����ݱȽϽ��������ɫ��ʾ
				// ��ɫ��û��ʵ����������ɫ����ʵ����������������������ɫ��ʵ��������Ӧ���������
				Object num = getBillCardPanelWrapper().getBillCardPanel()
						.getBodyValueAt(i, "nshouldoutassistnum");// Ӧ��������
				Object tatonum = getBillCardPanelWrapper().getBillCardPanel()
						.getBodyValueAt(i, "noutassistnum");// ʵ��������
				if (null != num && !"".equals(num) && null != tatonum
						&& !"".equals(tatonum)) {
					if (Double.parseDouble(num.toString().trim()) != Double
							.parseDouble(tatonum.toString().trim()))
						getBillCardPanelWrapper().getBillCardPanel()
								.getBodyPanel().setCellBackGround(i,
										"ccunhuobianma", Color.gray);
					else
						getBillCardPanelWrapper().getBillCardPanel()
								.getBodyPanel().setCellBackGround(i,
										"ccunhuobianma", Color.white);
				} else
					getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
							.setCellBackGround(i, "ccunhuobianma", Color.red);
			}

		}

	}

	// ��������
	protected void onfzgn() {

	}

	/*
	 * ����ָ��(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w8004040204.AbstractMyEventHandler#ontpzd()
	 */
	protected void ontpzd() throws Exception {
		int bodyrownum = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getRowCount();
		if (bodyrownum == 0) {
			getBillUI().showErrorMessage("�ӱ���Ϊ�գ�");
			return;
		}
		// ��֤��Ʒ����

		String geb_cinvbasidy = (String) getBillCardPanelWrapper()
				.getBillCardPanel().getBillModel().getValueAt(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBillTable().getSelectedRow(),
						"cinventoryid");
		if (null == geb_cinvbasidy || "".equals(geb_cinvbasidy)) {
			getBillUI().showErrorMessage("��Ʒ����Ϊ��!");
			return;
		}
		// ��֤���κ��Ƿ�Ϊ��
		String geb_vbatchcodey = (String) getBillCardPanelWrapper()
				.getBillCardPanel().getBillModel().getValueAt(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBillTable().getSelectedRow(), "vbatchcode");
		if (null == geb_vbatchcodey || "".equals(geb_vbatchcodey)) {
			getBillUI().showErrorMessage("���κŲ���Ϊ��!");
			return;
		}
		// ��֤Ӧ������
		UFDouble geb_bsnumy = (UFDouble) getBillCardPanelWrapper()
				.getBillCardPanel().getBillModel().getValueAt(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBillTable().getSelectedRow(),
						"nshouldoutassistnum");
		if (null == geb_bsnumy) {
			getBillUI().showErrorMessage("Ӧ����������Ϊ��!");
			return;
		}
		// ��֤���κ��Ƿ���ȷ
		if (geb_vbatchcodey.trim().length() < 8) {
			getBillUI().showErrorMessage("���κŲ���С��8λ!");
			return;
		}

		Pattern p = Pattern
				.compile(
						"^((((1[6-9]|[2-9]\\d)\\d{2})(0?[13578]|1[02])(0?[1-9]|[12]\\d|3[01]))|"
								+ "(((1[6-9]|[2-9]\\d)\\d{2})(0?[13456789]|1[012])(0?[1-9]|[12]\\d|30))|"
								+ "(((1[6-9]|[2-9]\\d)\\d{2})0?2(0?[1-9]|1\\d|2[0-8]))|"
								+ "(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))0?229))$",
						Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		Matcher m = p.matcher(geb_vbatchcodey.trim().substring(0, 8));
		if (!m.find()) {
			getBillUI()
					.showErrorMessage("���κ�����Ĳ���ȷ,����������ȷ������!�磺20100101XXXXXX");
			return;
		}

		// ���ѡ���к�
		int[] b = getBillCardPanelWrapper().getBillCardPanel().getBillTable()
				.getSelectedRows();
		// �ж��Ƿ�ѡ�е���
		if (b.length > 1) {
			getBillUI().showErrorMessage("���ܽ��ж��в�������ѡ��һ������!");
			return;
		}
		if (null == b || b.length == 0) {
			getBillUI().showErrorMessage("��û��ѡ����ѡ��һ������!");
			return;
		}
		// ��ȡ��ѡ����к�
		int index = getBillCardPanelWrapper().getBillCardPanel().getBillTable()
				.getSelectedRow();
		if (index > -1) {
			// ��ȡ�����ѡ���VO����
			TbOutgeneralBVO item = (TbOutgeneralBVO) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getBodyValueRowVO(index,
							TbOutgeneralBVO.class.getName());

			if (null != item) {
				Object pk_stordoc = getBillCardPanelWrapper()
						.getBillCardPanel().getHeadItem("srl_pk")
						.getValueObject();
				if (null == pk_stordoc || "".equals(pk_stordoc)) {
					myClientUI.showErrorMessage("����ʧ��,����Ȩ����");
					return;
				} else {
					if (!pk_stordoc.equals(pk_stock)) {
						myClientUI.showErrorMessage("����ʧ��,����Ȩ����");
						return;
					}
				}
				// TrayDisposeDlg tdpDlg = new TrayDisposeDlg("4203",
				// ClientEnvironment.getInstance().getUser()
				// .getPrimaryKey(),
				// ClientEnvironment.getInstance().getCorporation()
				// .getPrimaryKey(), "8004040204", myClientUI);
				// tdpDlg.getReturnVOs(myClientUI, item, index, pk_stordoc
				// .toString(), opType);
				TrayDisposeDlg tdpDlg = new TrayDisposeDlg("80EE",
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey(),
						ClientEnvironment.getInstance().getCorporation()
								.getPrimaryKey(), "8004040218", item,
						myClientUI);
				TbOutgeneralBVO vos = tdpDlg.getReturnVOs(item);

				if (null == vos) {
					return;
				}

				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
						vos.getNoutassistnum(), index, "noutassistnum");
				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
						vos.getNoutnum(), index, "noutnum");

				chaneColor();
			}
		} else {
			getBillUI().showWarningMessage("��ѡ������н��в���");
		}
	}

	/*
	 * �Զ�ȡ��(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w8004040204.AbstractMyEventHandler#onzdqh()
	 */
	protected void onzdqh() throws Exception {
		int results = myClientUI.showOkCancelMessage("ȷ���Զ����?");
		if (results == 1) {
			TbOutgeneralBVO[] generalbVO = (TbOutgeneralBVO[]) getBillUI()
					.getVOFromUI().getChildrenVO();
			TbOutgeneralBVO[] tmpbVO = null;
			if (null != generalbVO && generalbVO.length > 0) {
				Object pk_stordoc = getBillCardPanelWrapper()
						.getBillCardPanel().getHeadItem("srl_pk")
						.getValueObject();
				if (null == pk_stordoc || "".equals(pk_stordoc)) {
					myClientUI.showErrorMessage("����ʧ��,����Ȩ����");
					return;
				} else {
					if (!pk_stordoc.equals(pk_stock)) {
						myClientUI.showErrorMessage("����ʧ��,����Ȩ����");
						return;
					}
				}
				// ��λ
				String pk_cargdoc = "";
				try {
					pk_cargdoc = nc.ui.wds.w8000.CommonUnit
							.getCargDocName(ClientEnvironment.getInstance()
									.getUser().getPrimaryKey());
				} catch (BusinessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance()
						.lookup(IUAPQueryBS.class.getName());
				int bodyrownum = getBillCardPanelWrapper().getBillCardPanel()
						.getBillTable().getRowCount();
				if (bodyrownum == 0) {
					getBillUI().showErrorMessage("�ӱ���Ϊ�գ�");
					return;
				}
				// ��֤��Ʒ����
				for (int i = 0; i < bodyrownum; i++) {
					String geb_cinvbasidy = (String) getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"cinventoryid");
					if (null == geb_cinvbasidy || "".equals(geb_cinvbasidy)) {
						getBillUI().showErrorMessage("��Ʒ����Ϊ��!");
						return;
					}
					// ��֤���κ��Ƿ�Ϊ��
					String geb_vbatchcodey = (String) getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"vbatchcode");
					if (null == geb_vbatchcodey || "".equals(geb_vbatchcodey)) {
						getBillUI().showErrorMessage("���κŲ���Ϊ��!");
						return;
					}
					// ��֤Ӧ������
					UFDouble geb_bsnumy = (UFDouble) getBillCardPanelWrapper()
							.getBillCardPanel().getBillModel().getValueAt(i,
									"nshouldoutassistnum");
					if (null == geb_bsnumy) {
						getBillUI().showErrorMessage("Ӧ����������Ϊ��!");
						return;
					}
					// ��֤���κ��Ƿ���ȷ
					if (geb_vbatchcodey.trim().length() < 8) {
						getBillUI().showErrorMessage("���κŲ���С��8λ!");
						return;
					}

					Pattern p = Pattern
							.compile(
									"^((((1[6-9]|[2-9]\\d)\\d{2})(0?[13578]|1[02])(0?[1-9]|[12]\\d|3[01]))|"
											+ "(((1[6-9]|[2-9]\\d)\\d{2})(0?[13456789]|1[012])(0?[1-9]|[12]\\d|30))|"
											+ "(((1[6-9]|[2-9]\\d)\\d{2})0?2(0?[1-9]|1\\d|2[0-8]))|"
											+ "(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))0?229))$",
									Pattern.CASE_INSENSITIVE
											| Pattern.UNICODE_CASE);
					Matcher m = p.matcher(geb_vbatchcodey.trim()
							.substring(0, 8));
					if (!m.find()) {
						getBillUI().showErrorMessage(
								"���κ�����Ĳ���ȷ,����������ȷ������!�磺20100101XXXXXX");
						return;
					}

				}
				// �õ�ȫ������VO

				// ��ͷ

				TbOutgeneralBVO[] tbOutgeneralBVOs = null;
				if (getBillManageUI().isListPanelSelected()) {

					tbOutgeneralBVOs = (TbOutgeneralBVO[]) getBillManageUI()
							.getBillListWrapper().getVOFromUI().getChildrenVO();

				} else {

					tbOutgeneralBVOs = (TbOutgeneralBVO[]) getBillCardPanelWrapper()
							.getBillVOFromUI().getChildrenVO();

				}

				// �жϱ����Ƿ�������
				if (null == tbOutgeneralBVOs || tbOutgeneralBVOs.length == 0) {
					getBillUI().showErrorMessage("������û�����ݣ����ܽ��в�����");
					return;
				}
				// �ж������ݻ��Ƿ��ܴ�Ż���
				if (isStock) {
					for (int i = 0; i < tbOutgeneralBVOs.length; i++) {
						// ��ñ���
						StringBuffer sql = new StringBuffer(
								" select cdt_pk from bd_cargdoc_tray where cdt_invbasdoc='"
										+ tbOutgeneralBVOs[i].getCinventoryid()
										+ "' and cdt_traystatus=0 and dr=0 and  pk_cargdoc='"
										+ pk_cargdoc + "'  ");

						// ����������ȫ������
						ArrayList cdts = new ArrayList();
						try {
							cdts = (ArrayList) query.executeQuery(sql
									.toString(), new ArrayListProcessor());

						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// �����ݻ�
						StringBuffer sqlVolume = new StringBuffer(
								"select def20 from bd_invbasdoc where pk_invbasdoc='");
						sqlVolume.append(tbOutgeneralBVOs[i].getCinventoryid());
						sqlVolume.append("'");
						ArrayList cdtsVolume = new ArrayList();
						cdtsVolume = (ArrayList) query.executeQuery(sqlVolume
								.toString(), new ArrayListProcessor());
						// ��Ʒ��ǰ���ݻ�
						double invVolume = 0;
						if (null != cdts
								&& null != cdtsVolume
								&& null != cdtsVolume.get(0)
								&& null != ((Object[]) cdtsVolume.get(0))[0]
								&& !"".equals(((Object[]) cdtsVolume.get(0))[0]
										.toString())) {
							invVolume = Double
									.parseDouble(((Object[]) cdtsVolume.get(0))[0]
											.toString())
									* cdts.size();
						} else {
							getBillUI().showErrorMessage("����д���̻������ݻ���");
							return;
						}
						// ��⸨����
						String geb_bsnum = "";
						if (null != tbOutgeneralBVOs[i]
								.getNshouldoutassistnum()) {
							geb_bsnum = tbOutgeneralBVOs[i]
									.getNshouldoutassistnum().toString();
						}
						// ��⸨����
						double geb_bsnumd = 0;
						if (null != geb_bsnum && !"".equals(geb_bsnum)) {
							geb_bsnumd = Double.parseDouble(geb_bsnum);
						}

						if (Math.abs(geb_bsnumd) > invVolume) {
							getBillUI().showErrorMessage("����������ڵ�ǰ����ݻ���");
							return;
						}
					}

				} else {

					StringBuffer sql_cargtrays = new StringBuffer(
							" dr=0 and  pk_cargdoc='" + pk_cargdoc + "'  ");

					ArrayList cargtrays = (ArrayList) query.retrieveByClause(
							BdCargdocTrayVO.class, sql_cargtrays.toString());
					if (null == cargtrays || cargtrays.size() == 0
							|| null == cargtrays.get(0)) {
						getBillUI().showErrorMessage("�ֲֲֿ�û���������̣�");
						return;
					}

				}

				// ���������Ϣ
				for (int i = 0; i < tbOutgeneralBVOs.length; i++) {
					// ��ñ���
					StringBuffer sql = new StringBuffer();
					if (isStock) {
						sql
								.append("select cdt_pk from bd_cargdoc_tray where cdt_invbasdoc='"
										+ tbOutgeneralBVOs[i].getCinventoryid()
										+ "' and cdt_traystatus=0 and dr=0 and  pk_cargdoc='"
										+ pk_cargdoc + "' ");
						String mySql = " select distinct cdt_pk from tb_general_b_b where pk_invbasdoc='"
								+ tbOutgeneralBVOs[i].getCinventoryid()
								+ "' and geb_pk !='"
								+ tbOutgeneralBVOs[i].getCsourcebillbid()
								+ "' and dr=0 and geb_pk in (' ";

						boolean myBoolean = false;
						for (int j = 0; j < tbOutgeneralBVOs.length; j++) {
							if (null != tbOutgeneralBVOs[i].getCinventoryid()
									&& null != tbOutgeneralBVOs[j]
											.getCinventoryid()
									&& null != tbOutgeneralBVOs[i]
											.getCsourcebillbid()
									&& null != tbOutgeneralBVOs[j]
											.getCsourcebillbid()) {
								if (tbOutgeneralBVOs[i].getCinventoryid()
										.equals(
												tbOutgeneralBVOs[j]
														.getCinventoryid())
										&& !tbOutgeneralBVOs[i]
												.getCsourcebillbid()
												.equals(
														tbOutgeneralBVOs[j]
																.getCsourcebillbid())) {
									mySql += tbOutgeneralBVOs[j]
											.getCsourcebillbid()
											+ "','";
									myBoolean = true;
								}
							}
						}
						mySql += "') ";
						if (myBoolean) {
							sql.append(" and cdt_pk not in (" + mySql + ")");
						}
					} else {
						sql
								.append("select cdt_pk from bd_cargdoc_tray where  dr=0 and  pk_cargdoc='"
										+ pk_cargdoc + "' ");
					}

					// ����������ȫ������
					ArrayList cdts = new ArrayList();
					try {
						cdts = (ArrayList) query.executeQuery(sql.toString(),
								new ArrayListProcessor());

					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// �����ݻ�
					StringBuffer sqlVolume = new StringBuffer(
							"select def20 from bd_invbasdoc where pk_invbasdoc='");
					sqlVolume.append(tbOutgeneralBVOs[i].getCinventoryid());
					sqlVolume.append("' and dr=0 ");
					ArrayList cdtsVolume = new ArrayList();
					cdtsVolume = (ArrayList) query.executeQuery(sqlVolume
							.toString(), new ArrayListProcessor());

					double invTrayVolume = 0;
					if (isStock) {
						invTrayVolume = Double
								.parseDouble(((Object[]) cdtsVolume.get(0))[0]
										.toString());
					} else {
						invTrayVolume = -1;
					}
					// ��⸨����
					String geb_bsnum = "";
					if (null != tbOutgeneralBVOs[i].getNshouldoutassistnum()) {
						geb_bsnum = tbOutgeneralBVOs[i]
								.getNshouldoutassistnum().toString();

					}
					// ��ǰ��⸨����
					double geb_bsnumd = 0;
					if (null != geb_bsnum && !"".equals(geb_bsnum)) {
						geb_bsnumd = Double.parseDouble(geb_bsnum);
					}
					// ������̸���

					if (null != cdts) {

					}

					// ���Ҫɾ����VO
					StringBuffer tbbsql = new StringBuffer("geb_pk='");
					tbbsql.append(tbOutgeneralBVOs[i].getCsourcebillbid());
					tbbsql.append("' and pk_invbasdoc='");
					tbbsql.append(tbOutgeneralBVOs[i].getCinventoryid());
					tbbsql.append("' and dr = 0");

					ArrayList dtbbvos = new ArrayList();
					try {

						dtbbvos = (ArrayList) query.retrieveByClause(
								TbGeneralBBVO.class, tbbsql.toString());
					} catch (BusinessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// 
					ArrayList tbGeneralBBVOs = new ArrayList();
					// ArrayList bdCargdocTrayVOs = new ArrayList();
					String Cdt_pk = "";
					geb_bsnumd = Math.abs(geb_bsnumd);
					int k = 0;
					if (-1 != invTrayVolume) {

						while (geb_bsnumd > invTrayVolume) {
							TbGeneralBBVO tbgbbvo = new TbGeneralBBVO();
							// ����
							tbgbbvo.setGebb_vbatchcode(tbOutgeneralBVOs[i]
									.getVbatchcode());
							// ��д����
							tbgbbvo.setGebb_lvbatchcode(tbOutgeneralBVOs[i]
									.getLvbatchcode());
							// �к�
							tbgbbvo.setGebb_rowno(k + 1 + "0");
							// ���ⵥ��������
							tbgbbvo.setGeb_pk(tbOutgeneralBVOs[i]
									.getCsourcebillbid());
							// ������
							tbgbbvo.setGebb_hsl(tbOutgeneralBVOs[i].getHsl());
							// �˻���������
							tbgbbvo.setPk_invbasdoc(tbOutgeneralBVOs[i]
									.getCinventoryid());
							// ��ⵥ�ӱ�����
							// tbgbbvo.setGeb_pk(tbGeneralBVOs[i].getGeb_pk());
							// ����
							tbgbbvo.setGebb_nprice(tbOutgeneralBVOs[i]
									.getNprice());
							// ���
							tbgbbvo.setGebb_nmny(tbOutgeneralBVOs[i].getNmny());
							// ����ʵ�ʴ������
							tbgbbvo.setGebb_num(new UFDouble(invTrayVolume));
							geb_bsnumd = geb_bsnumd - invTrayVolume;
							// ��������
							if (null != ((Object[]) cdts.get(k))[0]) {
								tbgbbvo.setCdt_pk(((Object[]) cdts.get(k))[0]
										.toString());
							}
							// DR
							tbgbbvo.setDr(0);
							// tbgbbvo.setGeb_pk(tbGeneralBVOs[i].getGeb_pk());
							tbGeneralBBVOs.add(tbgbbvo);
							// ����״̬

							k++;
						}
					}

					TbGeneralBBVO tbgbbvo1 = new TbGeneralBBVO();
					// ����
					tbgbbvo1.setGebb_vbatchcode(tbOutgeneralBVOs[i]
							.getVbatchcode());
					// ��д����
					tbgbbvo1.setGebb_lvbatchcode(tbOutgeneralBVOs[i]
							.getLvbatchcode());
					// �к�
					tbgbbvo1.setGebb_rowno(k + 1 + "0");
					// ���ⵥ��������
					tbgbbvo1.setGeb_pk(tbOutgeneralBVOs[i].getCsourcebillbid());
					// ������
					tbgbbvo1.setGebb_hsl(tbOutgeneralBVOs[i].getHsl());
					// �˻���������
					tbgbbvo1.setPk_invbasdoc(tbOutgeneralBVOs[i]
							.getCinventoryid());
					// ��ⵥ�ӱ�����
					// tbgbbvo1.setGeb_pk(tbGeneralBVOs[i].getGeb_pk());
					// ����
					tbgbbvo1.setGebb_nprice(tbOutgeneralBVOs[i].getNprice());
					// ���
					tbgbbvo1.setGebb_nmny(tbOutgeneralBVOs[i].getNmny());
					// ����ʵ�ʴ������
					tbgbbvo1.setGebb_num(new UFDouble(geb_bsnumd));
					// ��������
					if (null != ((Object[]) cdts.get(k))[0]) {
						tbgbbvo1.setCdt_pk(((Object[]) cdts.get(k))[0]
								.toString());
					}
					// DR
					tbgbbvo1.setDr(0);
					tbGeneralBBVOs.add(tbgbbvo1);
					// ����״̬

					//
					TbGeneralBBVO[] tbGeneralBBVO = new TbGeneralBBVO[tbGeneralBBVOs
							.size()];
					tbGeneralBBVOs.toArray(tbGeneralBBVO);

					// ����Զ���ӿ�
					Iw8004040210 iw = (Iw8004040210) NCLocator.getInstance()
							.lookup(Iw8004040210.class.getName());
					try {
						iw.delAndInsertTbGeneralBBVO(dtbbvos, tbGeneralBBVO);
					} catch (BusinessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				TrayDisposeDetailDlg tdpDlg = new TrayDisposeDetailDlg("80FF",
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey(),
						ClientEnvironment.getInstance().getCorporation()
								.getPrimaryKey(), "8004040218",
						tbOutgeneralBVOs);
				AggregatedValueObject[] vos = tdpDlg
						.getReturnVOs(tbOutgeneralBVOs);
				int rownum = getBillCardPanelWrapper().getBillCardPanel()
						.getBillTable().getRowCount();
				for (int i = 0; i < rownum; i++) {
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(
									getBillCardPanelWrapper()
											.getBillCardPanel().getBodyValueAt(
													i, "nshouldoutassistnum"),
									i, "noutassistnum");
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(
									getBillCardPanelWrapper()
											.getBillCardPanel().getBodyValueAt(
													i, "nshouldoutnum"), i,
									"noutnum");

				}
				if (null == vos || vos.length == 0) {

					return;
				}
				chaneColor();

			} else {
				myClientUI.showErrorMessage("����ʧ��,����Ȩ����");
				return;
			}
		}

	}

	/*
	 * ��ѯ��ϸ(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w8004040204.AbstractMyEventHandler#onckmx()
	 */
	protected void onckmx() throws Exception {

		if (getBufferData().getVOBufferSize() <= 0) {
			getBillUI().showErrorMessage("������û�����ݻ���û��ѡ����嵥�ݣ����ܽ��в���!");
			return;
		}
		// �õ�ȫ������VO
		TbOutgeneralBVO[] tbOutgeneralBVOs = null;
		int selectRow = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getSelectedRow();

		// if (-1 == selectRow) {
		// getBillUI().showErrorMessage("������û�����ݻ���û��ѡ����嵥�ݣ����ܽ��в�����");
		// return;
		// }
		if (getBillManageUI().isListPanelSelected()) {
			// if (getBufferData().getVOBufferSize() != 0) {

			tbOutgeneralBVOs = (TbOutgeneralBVO[]) getBillManageUI()
					.getBillListWrapper().getVOFromUI().getChildrenVO();
			// }

		} else {

			// if (getBufferData().getVOBufferSize() != 0) {
			tbOutgeneralBVOs = (TbOutgeneralBVO[]) getBillCardPanelWrapper()
					.getBillVOFromUI().getChildrenVO();
			// }

		}
		// �жϱ����Ƿ�������
		if (null == tbOutgeneralBVOs || tbOutgeneralBVOs.length == 0) {
			getBillUI().showErrorMessage("������û�����ݣ����ܽ��в�����");
			return;
		}
		TrayDisposeDetailDlg tdpDlg = new TrayDisposeDetailDlg("80FF",
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey(), "8004040218", tbOutgeneralBVOs);
		AggregatedValueObject[] vos = tdpDlg.getReturnVOs(tbOutgeneralBVOs);

	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		if (isControl == 2 || "3".equals(st_type)) {
			isAdd = true;
			// ʵ����ѯ��
			saleOrderDlg = new SaleOrderDlg(myClientUI);
			// ���÷��� ��ȡ��ѯ��ľۺ�VO
			// AggregatedValueObject[] vos = saleOrderDlg
			// .getReturnVOs(ClientEnvironment.getInstance()
			// .getCorporation().getPrimaryKey(),
			// ClientEnvironment.getInstance().getUser()
			// .getPrimaryKey(), "4202",
			// ConstVO.m_sBillDRSQ, "8004040204", "8004040294",
			// myClientUI);
			AggregatedValueObject[] vos = saleOrderDlg
					.getReturnVOs(ClientEnvironment.getInstance()
							.getCorporation().getPrimaryKey(),
							ClientEnvironment.getInstance().getUser()
									.getPrimaryKey(), "0208",
							ConstVO.m_sBillDRSQ, "8004040218", "8004040298",
							myClientUI);
			// �ж��Ƿ�Բ�ѯģ���н��в���
			if (null == vos || vos.length == 0) {
				return;
			}
			// ����ת���� ��ģ���л�ȡ�Ķ���ת�����Լ��ĵ�ǰ��ʾ�Ķ��󣬵��÷���
			MyBillVO voForSave = changeReqFydtoOutgeneral(vos);// �÷����ǵ�һ�����ӽ�������ת��
			opType = false;
			TbOutgeneralHVO generalh = (TbOutgeneralHVO) voForSave
					.getParentVO();
			if (null != generalh && generalh.getVsourcebillcode().length() > 0) {
				// �ж�Դ�������Ƿ����M��ĸ ��M��ĸ������ҵ������ҵ�����������Ʒ�ж�����¼û���Զ�����������Զ������ť
				int index = generalh.getVsourcebillcode().toLowerCase()
						.indexOf("m");
				if (index > -1) {
					opType = true;
					getButtonManager().getButton(ISsButtun.zdqh).setEnabled(
							false);
				}
			}
			// ����������� �Ͱ�ť��ʼ��
			getBufferData().clear();
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
			// �������
			getBufferData().addVOToBuffer(voForSave);
			// ��������
			updateBuffer();
			super.onBoEdit();
			// ִ�й�ʽ��ͷ��ʽ
			getBillCardPanelWrapper().getBillCardPanel().execHeadEditFormulas();
			getBillCardPanelWrapper().getBillCardPanel()
					.execHeadTailEditFormulas();
			// ���ÿ��Ա
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"cwhsmanagerid").setValue(
					ClientEnvironment.getInstance().getUser().getPrimaryKey());
			// �����Ƶ���
			getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
					"coperatorid").setValue(
					ClientEnvironment.getInstance().getUser().getPrimaryKey());
			// ��������
			getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
					"dbilldate").setValue(_getDate().toString());

		} else {
			myClientUI.showErrorMessage("����ʧ��,����Ȩ����");
			return;
		}
	}

	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		getBillUI().initUI();
		getBufferData().clear();
		changeButton(false);
	}

	public void saveGeneral(AggregatedValueObject billvo) throws Exception {
		// ͨ��ת��������ȡERP�г��ⵥ�ۺ�VO
		this.getBusinessAction().save(billvo, getUIController().getBillType(),
				_getDate().toString(), getBillUI().getUserObject(), billvo);
	}

	// ȡ��ǩ��
	protected void onQxqz() throws Exception {
		myClientUI.showHintMessage("����ִ��ȡ��ǩ��...");
		if (getBufferData().getCurrentRow() >= 0) {
			int retu = myClientUI.showOkCancelMessage("ȷ��ȡ��ǩ��?");
			if (retu == 1) {
				TbOutgeneralHVO generalh = (TbOutgeneralHVO) getBufferData()
						.getCurrentVO().getParentVO();
				Object result = iuap.retrieveByPK(TbOutgeneralHVO.class,
						generalh.getGeneral_pk());
				if (null != result) {
					generalh = (TbOutgeneralHVO) result;
					if (generalh.getVbillstatus() == 0) {
						// ״̬
						generalh.setVbillstatus(new Integer(1));
						// ǩ��������
						generalh.setCregister(null);
						// ǩ��ʱ��
						generalh.setTaccounttime(null);
						// ǩ������
						generalh.setQianzidate(null);

						String strWhere = " dr = 0 and vbillcode = '"
								+ generalh.getVbillcode() + "'";

						ArrayList list = (ArrayList) iuap.retrieveByClause(
								IcGeneralHVO.class, strWhere);
						if (null != list && list.size() > 0) {
							IcGeneralHVO header = (IcGeneralHVO) list.get(0);
							if (null != header) {

								strWhere = " dr = 0 and cgeneralhid = '"
										+ header.getCgeneralhid() + "'";
								list = (ArrayList) iuap.retrieveByClause(
										IcGeneralBVO.class, strWhere);
								if (null != list && list.size() > 0) {
									IcGeneralBVO[] itemvo = new IcGeneralBVO[list
											.size()];
									GeneralBillItemVO[] generalbItem = new GeneralBillItemVO[itemvo.length];
									itemvo = (IcGeneralBVO[]) list
											.toArray(itemvo);
									AggregatedValueObject billvo = new GeneralBillVO();
									billvo.setParentVO(CommonUnit
											.setGeneralHVO(header));
									for (int i = 0; i < itemvo.length; i++) {
										generalbItem[i] = CommonUnit
												.setGeneralItemVO(itemvo[i]);
									}
									billvo.setChildrenVO(generalbItem);
									this.getIw().whs_processAction(
											"CANCELSIGN", "DELETE", "4C",
											_getDate().toString(), billvo,
											generalh);

									myClientUI.showHintMessage("�����ɹ�");
									super.onBoRefresh();
									getButtonManager()
											.getButton(
													nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr)
											.setEnabled(true);
									getButtonManager()
											.getButton(
													nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz)
											.setEnabled(false);
									myClientUI.updateButtons();
									return;
								}
							}
						}
						myClientUI.showErrorMessage("ǩ��ʧ��,���۳��ⵥ�ѱ�ɾ��");
						return;

					}
				}
			} else {
				myClientUI.showHintMessage(null);
			}
		} else {
			myClientUI.showErrorMessage("��ѡ��һ�����ݽ���ǩ��");
		}

	}

	// ǩ��ȷ��
	protected void onQzqr() throws Exception {
		myClientUI.showHintMessage("����ִ��ǩ��...");
		if (getBufferData().getCurrentRow() >= 0) {
			int retu = myClientUI.showOkCancelMessage("ȷ��ǩ��?");
			if (retu == 1) {
				TbOutgeneralHVO generalh = (TbOutgeneralHVO) getBufferData()
						.getCurrentVO().getParentVO();
				Object result = iuap.retrieveByPK(TbOutgeneralHVO.class,
						generalh.getGeneral_pk());
				if (null != result) {
					generalh = (TbOutgeneralHVO) result;
					if (null != generalh.getVbillstatus()
							&& generalh.getVbillstatus() == 0) {
						myClientUI.showErrorMessage("�õ����Ѿ�ǩ��");
						return;
					} else if (null != generalh.getVbillstatus()
							&& generalh.getVbillstatus() == 1) {
						// �����������Ҫ�ģ����ڵ��˵�ȷ�ϻ�û�����������������������Ҫ���óɱ���ȷ�Ϻ�Ĳ���ǩ��

						// ״̬
						generalh.setVbillstatus(new Integer(0));
						// ǩ��������
						generalh.setCregister(ClientEnvironment.getInstance()
								.getUser().getPrimaryKey());
						// ǩ��ʱ��
						generalh.setTaccounttime(getBillUI()._getServerTime()
								.toString());
						// ǩ������
						generalh.setQianzidate(_getDate());
						AggregatedValueObject billvo = changeReqOutgeneraltoGeneral();
						this.getIw().whs_processAction("PUSHSAVE", "SIGN",
								"4C", _getDate().toString(), billvo, generalh);

						myClientUI.showHintMessage("ǩ�ֳɹ�");
						super.onBoRefresh();
						getButtonManager().getButton(
								nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr)
								.setEnabled(false);
						getButtonManager().getButton(
								nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz)
								.setEnabled(true);
						myClientUI.updateButtons();
					} else {
						myClientUI.showErrorMessage("�õ��ݻ�û�н����˵�ȷ��");
					}
				}
			} else {
				myClientUI.showHintMessage(null);
			}
		} else {
			myClientUI.showErrorMessage("��ѡ��һ�����ݽ���ǩ��");
		}

		// Object o = PfUtilClient.processAction("PUSHSAVE", "4C", _getDate()
		// .toString(), billvo);
		// Object[] arrayO = (Object[]) o;
		// billvo = (AggregatedValueObject) arrayO[0];
		// o = PfUtilClient.processAction("SIGN", "4C", _getDate()
		// .toString(), billvo);

	}

	/**
	 * ת�� �ѵ�ǰҳ���е�VOת����ERP�еĳ��ⵥ�ۺ�OV ���÷��� ���л�дERP�г��ⵥ
	 * 
	 * @return ERP�г��ⵥ�ۺ�VO
	 * @throws Exception
	 */
	public GeneralBillVO changeReqOutgeneraltoGeneral() throws Exception {
		if (getBufferData().getCurrentRow() < 0) {
			myClientUI.showErrorMessage("��ѡ���ͷ����ǩ��");
			return null;
		}
		// ���س���� ��ͷ
		TbOutgeneralHVO outhvo = (TbOutgeneralHVO) getBufferData()
				.getCurrentVO().getParentVO();
		// ��Ҫ��ѯ�������ݣ����ȷ������----------Ϊ���
		// Object result = iuap.retrieveByPK(TbOutgeneralHVO.class, outhvo
		// .getGeneral_pk());
		//		
		// if(null!=result){
		// outhvo = (TbOutgeneralHVO) result;
		// if(outhvo.getVbillstatus())
		// }

		// ���س���� ����
		TbOutgeneralBVO[] outbvo = (TbOutgeneralBVO[]) getBufferData()
				.getCurrentVO().getChildrenVO();

		// ����ۺ�VO
		GeneralBillVO gBillVO = new GeneralBillVO();
		// �����ͷVO
		GeneralBillHeaderVO generalhvo = null;
		// �����ӱ���
		List<GeneralBillItemVO> generalBVOList = new ArrayList<GeneralBillItemVO>();
		// �����ӱ�VO����
		GeneralBillItemVO[] generalBVO = null;
		// Object o = myClientUI.getBillCardPanel().getBodyValueAt(0,
		// "cfirstbillhid");
		if (null != outbvo[0].getCfirstbillhid()
				&& !"".equals(outbvo[0].getCfirstbillhid())) {
			String sWhere = " dr = 0 and csaleid = '"
					+ outbvo[0].getCfirstbillhid() + "'";
			ArrayList list = (ArrayList) iuap.retrieveByClause(SoSaleVO.class,
					sWhere);
			if (null != list && list.size() > 0) {
				SoSaleVO salehvo = (SoSaleVO) list.get(0);
				generalhvo = new GeneralBillHeaderVO();
				opType = false;
				if (null != salehvo) {
					// �����ⵥ��ͷ��ֵ
					// �ж���Ʒ����
					int index = salehvo.getVreceiptcode().toLowerCase()
							.indexOf("m");
					if (index > -1) {
						opType = true;
					}
					generalhvo.setPk_corp(salehvo.getPk_corp());// ��˾����
					generalhvo.setCbiztypeid(salehvo.getCbiztype());// ҵ������
					generalhvo.setCbilltypecode("4C");// ��浥�����ͱ���
					generalhvo.setVbillcode(outhvo.getVbillcode());// ���ݺ�
					generalhvo.setDbilldate(outhvo.getDbilldate());// ��������
					generalhvo.setCwarehouseid(outhvo.getSrl_pk());// �ֿ�ID
					generalhvo.setCdispatcherid(outhvo.getCdispatcherid());// �շ�����ID
					generalhvo.setCdptid(salehvo.getCdeptid());// ����ID
					generalhvo.setCwhsmanagerid(outhvo.getCwhsmanagerid());// ���ԱID
					generalhvo.setCoperatorid(ClientEnvironment.getInstance()
							.getUser().getPrimaryKey());// �Ƶ���
					generalhvo.setAttributeValue("coperatoridnow",
							ClientEnvironment.getInstance().getUser()
									.getPrimaryKey());// ������
					generalhvo.setCinventoryid(outbvo[0].getCinventoryid());// ���ID
					generalhvo.setAttributeValue("csalecorpid", salehvo
							.getCsalecorpid());// ������֯
					generalhvo.setCcustomerid(salehvo.getCcustomerid());// �ͻ�ID
					generalhvo.setVdiliveraddress(salehvo.getVreceiveaddress());// ���˵�ַ
					generalhvo.setCbizid(salehvo.getCemployeeid());// ҵ��ԱID
					generalhvo.setVnote(salehvo.getVnote());// ��ע
					generalhvo.setFbillflag(2);// ����״̬
					generalhvo.setPk_calbody(salehvo.getCcalbodyid());// �����֯PK
					generalhvo.setAttributeValue("clastmodiid", outhvo
							.getClastmodiid());// ����޸���
					generalhvo.setAttributeValue("tlastmoditime", outhvo
							.getTlastmoditime());// ����޸�ʱ��
					generalhvo.setAttributeValue("tmaketime", outhvo
							.getTmaketime());// �Ƶ�ʱ��
					generalhvo.setPk_cubasdocC(outhvo.getPk_cubasdocc());// �ͻ���������ID
					// �����帳ֵ
					for (int i = 0; i < outbvo.length; i++) {
						// ���ݱ��帽��--��λ
						LocatorVO locatorvo = new LocatorVO();
						locatorvo.setPk_corp(salehvo.getPk_corp());
						boolean isBatch = false;
						if (null != outbvo[i].getCfirstbillhid()
								&& !"".equals(outbvo[i].getCfirstbillhid())) {
							sWhere = " dr = 0 and corder_bid = '"
									+ outbvo[i].getCfirstbillbid() + "'";
							ArrayList saleblist = (ArrayList) iuap
									.retrieveByClause(SoSaleorderBVO.class,
											sWhere);
							if (null != saleblist && saleblist.size() > 0) {
								SoSaleorderBVO salebvo = (SoSaleorderBVO) saleblist
										.get(0);
								GeneralBillItemVO generalb = new GeneralBillItemVO();
								generalb.setAttributeValue("pk_corp", salebvo
										.getPk_corp());// ��˾
								generalb
										.setCinvbasid(salebvo.getCinvbasdocid());// �������ID
								if (opType) {
									// �������۸���ID��ѯ ����ִ�б� �е���������hid �� �������ID
									// �����������Զ���10 ��11 ���л�д
									// ����Ϊ�����ڻ�д���۷�Ʊ��ʱ�򱻶��ο����������Ա���Ҫ��д�Զ���11�ֶ�(����ҵ��)
									String sellSql = " select  vdef10,vdef11  from so_saleexecute where dr = 0 and csale_bid = '"
											+ salebvo.getCorder_bid() + "'";
									ArrayList sellList = (ArrayList) iuap
											.executeQuery(sellSql,
													new ArrayListProcessor());
									if (null != sellList && sellList.size() > 0) {
										Object[] tmpsell = (Object[]) sellList
												.get(0);
										generalb
												.setVuserdef10(WDSTools
														.getString_TrimZeroLenAsNull(tmpsell[0]));
										generalb
												.setAttributeValue(
														"vuserdef11",
														WDSTools
																.getString_TrimZeroLenAsNull(tmpsell[1]));
									}
								}
								generalb.setCinventoryid(salebvo
										.getCinventoryid());// ���ID
								generalb.setVbatchcode(outbvo[i]
										.getLvbatchcode());// ���κ�
								// ��ѯ�������ں�ʧЧ����
								String sql = "select dproducedate ,dvalidate  from scm_batchcode where pk_invbasdoc='"
										+ salebvo.getCinvbasdocid()
										+ "' and vbatchcode='"
										+ outbvo[i].getLvbatchcode()
										+ "' and dr=0";
								ArrayList batchList = (ArrayList) iuap
										.executeQuery(sql,
												new ArrayListProcessor());
								if (null != batchList && batchList.size() > 0) {
									Object[] batch = (Object[]) batchList
											.get(0);
									// ��������
									if (null != batch[0]
											&& !"".equals(batch[0]))
										generalb.setScrq(new UFDate(batch[0]
												.toString()));
									// ʧЧ����
									if (null != batch[0]
											&& !"".equals(batch[0]))
										generalb.setDvalidate(new UFDate(
												batch[1].toString()));
									isBatch = true;
								}
								generalb.setDbizdate(outhvo.getDbilldate());// ҵ������
								generalb.setNshouldoutnum(salebvo.getNnumber());// Ӧ������
								generalb.setNshouldoutassistnum(salebvo
										.getNpacknumber());// Ӧ��������
								generalb.setNoutnum(outbvo[i].getNoutnum());// ʵ������
								locatorvo.setNoutspacenum(outbvo[i]
										.getNoutnum());
								generalb.setNoutassistnum(outbvo[i]
										.getNoutassistnum());// ʵ��������
								locatorvo.setNoutspaceassistnum(outbvo[i]
										.getNoutassistnum());
								locatorvo.setCspaceid(outbvo[i].getCspaceid());// ��λID
								generalb.setCastunitid(outbvo[i]
										.getCastunitid());// ��������λID
								generalb.setNprice(outbvo[i].getNprice());// ����
								generalb.setNmny(outbvo[i].getNmny());// ���
								generalb
										.setCsourcebillhid(salehvo.getCsaleid());// ��Դ���ݱ�ͷ���к�
								generalb.setCfirstbillhid(salehvo.getCsaleid());// Դͷ���ݱ�ͷID
								generalb.setCfreezeid(salebvo.getCorder_bid());// ������Դ
								generalb.setCsourcebillbid(salebvo
										.getCorder_bid());// ��Դ���ݱ������к�
								generalb.setCfirstbillbid(salebvo
										.getCorder_bid());// Դͷ���ݱ���ID
								generalb.setCsourcetype(salehvo
										.getCreceipttype());// ��Դ��������
								generalb.setCfirsttype(salehvo
										.getCreceipttype());// Դͷ��������
								generalb.setVsourcebillcode(salehvo
										.getVreceiptcode());// ��Դ���ݺ�
								generalb.setVfirstbillcode(salehvo
										.getVreceiptcode());// Դͷ���ݺ�
								generalb.setVsourcerowno(salebvo.getCrowno());// ��Դ�����к�
								generalb.setVfirstrowno(salebvo.getCrowno());// Դͷ�����к�
								generalb.setFlargess(salebvo.getBlargessflag());// �Ƿ���Ʒ
								generalb.setDfirstbilldate(salehvo
										.getDmakedate());// Դͷ�����Ƶ�����
								generalb.setCreceieveid(salebvo
										.getCreceiptcorpid());// �ջ���λ
								generalb.setCrowno(outbvo[i].getCrowno());// �к�
								generalb.setHsl(outbvo[i].getHsl());// ������
								generalb.setNsaleprice(salebvo.getNnetprice());// ���ۼ۸�
								generalb.setNtaxprice(salebvo.getNtaxprice());// ��˰����
								generalb.setNtaxmny(salebvo.getNtaxmny());// ��˰���
								generalb.setAttributeValue("cquoteunitid",
										salebvo.getCquoteunitid());// ���ۼ�����λ
								generalb.setNsalemny(salebvo.getNmny());// ����˰���
								generalb.setAttributeValue("cquotecurrency",
										salebvo.getCcurrencytypeid());// ���ñ���
								LocatorVO[] locatorVO = new LocatorVO[] { locatorvo };
								generalb.setLocator(locatorVO);
								if (isBatch)
									// �������ӱ�����Ӷ���
									generalBVOList.add(generalb);
							}
						}
					}
					// ת������
					generalBVO = new GeneralBillItemVO[generalBVOList.size()];
					generalBVO = generalBVOList.toArray(generalBVO);
					// �ۺ�VO��ͷ��ֵ
					gBillVO.setParentVO(generalhvo);
					// �ۺ�VO���帳ֵ
					gBillVO.setChildrenVO(generalBVO);
				}
			}

		}

		return gBillVO;
	}

	/**
	 * ��ģ���е�ѡ�е�VO ����ת�������۳����VO
	 * 
	 * @param vos
	 *            ҳ��ѡ�еľۺ�VO
	 * @return ���۵ľۺ�VO
	 * @throws BusinessException
	 */
	private MyBillVO changeReqFydtoOutgeneral(AggregatedValueObject[] vos)
			throws BusinessException {
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// hiddenList = new ArrayList();
		MyBillVO myBillVO = new MyBillVO();
		TbOutgeneralHVO generalHVO = new TbOutgeneralHVO();
		// �ӱ���Ϣ���鼯��
		List<TbOutgeneralBVO> generalBList = new ArrayList<TbOutgeneralBVO>();

		SaleorderHVO salehVO = (SaleorderHVO) vos[0].getParentVO();
		if (null != salehVO.getCwarehouseid()
				&& !"".endsWith(salehVO.getCwarehouseid())) {
			generalHVO.setSrl_pk(salehVO.getCwarehouseid()); // ����ֿ�

		} else {
			if (null != salehVO.getCcustomerid()
					&& !"".equals(salehVO.getCcustomerid())) {
				String sql_stor = " pk_cubasdoc in "
						+ " (select pk_cubasdoc from bd_cumandoc where pk_cumandoc='"
						+ salehVO.getCcustomerid() + "')";
				ArrayList srl_pks = new ArrayList();
				try {
					srl_pks = (ArrayList) query.retrieveByClause(
							TbStorcubasdocVO.class, sql_stor.toString());

				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (null != srl_pks && srl_pks.size() > 0) {
					if (null != srl_pks.get(0)) {
						TbStorcubasdocVO tbStorcubasdocVO = (TbStorcubasdocVO) srl_pks
								.get(0);
						if (null != tbStorcubasdocVO.getPk_stordoc()
								&& !"".equals(tbStorcubasdocVO.getPk_stordoc())) {
							generalHVO.setSrl_pk(tbStorcubasdocVO
									.getPk_stordoc());
						}
					}
				}

			}
		}
		generalHVO.setCbiztype(salehVO.getCbiztype()); // ҵ����������
		generalHVO.setCdptid(salehVO.getCdeptid()); // ����
		generalHVO.setCbizid(salehVO.getCemployeeid()); // ҵ��Ա
		generalHVO.setCcustomerid(salehVO.getCcustomerid()); // �ͻ�
		generalHVO.setVdiliveraddress(salehVO.getVreceiveaddress()); // �ջ���ַ
		generalHVO.setVnote(salehVO.getVnote()); // ��ע
		generalHVO.setVsourcebillcode(salehVO.getVreceiptcode()); // ��Դ���ݺ�
		generalHVO.setDauditdate(salehVO.getDapprovedate()); // �������
		generalHVO.setCsourcebillhid(salehVO.getCsaleid()); // ��Դ���ݱ�ͷ����
		generalHVO.setVbilltype(salehVO.getCreceipttype().toString()); // ��������

		// ѭ�������е��ӱ���Ϣ
		for (int i = 0; i < saleOrderDlg.getSaleVO().length; i++) {
			if (salehVO.getCsaleid().equals(
					saleOrderDlg.getSaleVO()[i].getHeadVO().getCsaleid())) {
				vos[0].setChildrenVO(saleOrderDlg.getSaleVO()[i].getBodyVOs());
				break;
			}
		}
		myBillVO.setParentVO(generalHVO);
		// ѭ�������е��ӱ���Ϣ
		if (null != vos[0].getChildrenVO() && vos[0].getChildrenVO().length > 0) {
			SaleorderBVO[] salebVOs = (SaleorderBVO[]) vos[0].getChildrenVO();
			// ��ȡ���� ת���ӱ���������Ϣ
			for (int i = 0; i < salebVOs.length; i++) {
				SaleorderBVO salebVO = salebVOs[i];
				if (salehVO.getCsaleid().equals(salebVO.getCsaleid())) {
					TbOutgeneralBVO generalBVO = new TbOutgeneralBVO();
					setGeneralbVO(salehVO, salebVO, generalBVO);
					// generalBVO.setIsoper(new UFBoolean("Y"));
					generalBList.add(generalBVO);
				}
			}
			if (null != generalBList && generalBList.size() > 0) {
				TbOutgeneralBVO[] generalBVO = new TbOutgeneralBVO[generalBList
						.size()];
				generalBVO = generalBList.toArray(generalBVO);
				myBillVO.setChildrenVO(generalBVO);
			}
		}
		return myBillVO;
	}

	/**
	 * �����˵��ӱ���һЩ���Ը�ֵ VOת��
	 * 
	 * @param fydnew
	 *            ���˵�����
	 * @param fydmxnewvo
	 *            ���˵��ӱ�
	 * @param generalBVO
	 *            ���ⵥ�ӱ�
	 */
	private void setGeneralbVO(SaleorderHVO salehVO, SaleorderBVO salebVO,
			TbOutgeneralBVO generalBVO) {
		generalBVO.setCsourcebillhid(salehVO.getCsaleid()); // ��Դ���ݱ�ͷ����
		generalBVO.setVsourcebillcode(salehVO.getVreceiptcode()); // ��Դ���ݺ�
		generalBVO.setCsourcetype(salehVO.getCreceipttype().toString()); // ��Դ��������
		generalBVO.setCsourcebillbid(salebVO.getCorder_bid()); // ��Դ���ݱ�������
		generalBVO.setCfirstbillhid(salebVO.getCsaleid()); // Դͷ���ݱ�ͷ����
		generalBVO.setCfirstbillbid(salebVO.getCorder_bid()); // Դͷ���ݱ�������
		generalBVO.setCrowno(salebVO.getCrowno()); // �к�
		generalBVO.setNshouldoutnum(salebVO.getNnumber()); // Ӧ������
		generalBVO.setNshouldoutassistnum(salebVO.getNpacknumber()); // Ӧ��������
		generalBVO.setCinventoryid(salebVO.getCinvbasdocid()); // �������
		generalBVO.setFlargess(salebVO.getBlargessflag()); // �Ƿ���Ʒ
		generalBVO.setLvbatchcode("2009");// ��д����
	}

	public Iw8004040204 getIw() {
		return iw;
	}

	public void setIw(Iw8004040204 iw) {
		this.iw = iw;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-9 8:37:34)
	 * 
	 * @return nc.ui.trade.pub.BillCardUI
	 */
	private BillManageUI getBillManageUI() {
		return (BillManageUI) getBillUI();
	}
}
package nc.ui.wds.w8004040210;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nc.bs.framework.common.NCLocator;
import nc.bs.ic.mm.pub.GenMethod;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.sfapp.IBillcodeRuleService;
import nc.itf.wds.w8004040210.Iw8004040210;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.to.to103.BatchDisposeDlg;
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
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import nc.vo.wds.w8004040210.MyBillVO;
import nc.vo.wds.w8004040210.TbGeneralBBVO;
import nc.vo.wds.w8004040210.TbGeneralBVO;
import nc.vo.wds.w8004040210.TbGeneralHVO;
import nc.vo.wds.w8004040212.TbWarehousestockVO;
import nc.vo.wds.w8004061002.BdCargdocTrayVO;
import nc.vo.wds.w80060608.IcGeneralBVO;
import nc.vo.wds.w80060608.IcGeneralHVO;

/**
 * 
 * ������AbstractMyEventHandler�������ʵ���࣬ ��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	private String billCode = null; // ���ݺ�
	private boolean dbbool = true;
	private boolean isadd = true;
	// �ж��û����
	private String st_type = "";
	// �ж����ֻܲ��Ƿֲ�
	private boolean sotckIsTotal = true;
	private boolean isedit = true;

	protected IBusinessController createBusinessAction() {
		// TODO Auto-generated method stub
		switch (getUIController().getBusinessActionType()) {
		case IBusinessActionType.PLATFORM:
			dbbool = false;
			return new BusinessAction(getBillUI());
		case IBusinessActionType.BD:
			dbbool = true;
			// if (isControl1) {
			// return new BdBusinessAction(getBillUI());
			// } else {
			// if ("1".equals(st_type)) {
			// return new BdBusinessAction(getBillUI());
			// }
			return new TrayAction(getBillUI());
			// }
		default:
			dbbool = false;
			return new BusinessAction(getBillUI());
		}
	}

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;
		getButtonManager().getButton(
				nc.ui.wds.w8004040210.ssButtun.ISsButtun.Td).setEnabled(false);
		getButtonManager().getButton(
				nc.ui.wds.w8004040210.ssButtun.ISsButtun.Ai).setEnabled(false);
		//
		List InvLisk = new ArrayList();
		try {
			InvLisk = nc.ui.wds.w8000.CommonUnit
					.getInvbasdoc_Pk(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean isControl = true;
		if (null != InvLisk && InvLisk.size() > 0) {
			isControl = false;
			isControl1 = false;
		} else {
			isControl = true;
			isControl1 = true;
		}
		try {
			st_type = nc.ui.wds.w8000.CommonUnit.getUserType(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// �ֿ�����
		String stordocName = "";
		try {
			stordocName = nc.ui.wds.w8000.CommonUnit
					.getStordocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null != stordocName && !"".equals(stordocName)) {
			try {
				sotckIsTotal = nc.ui.wds.w8000.CommonUnit
						.getSotckIsTotal(stordocName);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (null != st_type) {
			if ("1".equals(st_type)) {
				getButtonManager().getButton(
						nc.ui.wds.w8004040210.ssButtun.ISsButtun.SsAdd)
						.setEnabled(false);
				isControl1 = true;
			} else if ("0".equals(st_type)) {
				getButtonManager().getButton(
						nc.ui.wds.w8004040210.ssButtun.ISsButtun.SsAdd)
						.setEnabled(true);
				isControl1 = false;
				try {
					stordocName = nc.ui.wds.w8000.CommonUnit
							.getStordocName(ClientEnvironment.getInstance()
									.getUser().getPrimaryKey());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (null != stordocName && !"".equals(stordocName)) {
					try {
						sotckIsTotal = nc.ui.wds.w8000.CommonUnit
								.getSotckIsTotal(stordocName);

					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} else if ("3".equals(st_type)) {
				getButtonManager().getButton(
						nc.ui.wds.w8004040210.ssButtun.ISsButtun.SsAdd)
						.setEnabled(true);
				// isControl1 = true;
			}
		}
		if (isControl1) {
			getButtonManager().getButton(
					nc.ui.wds.w8004040210.ssButtun.ISsButtun.SsAdd).setEnabled(
					false);
		} else {
			getButtonManager().getButton(
					nc.ui.wds.w8004040210.ssButtun.ISsButtun.SsAdd).setEnabled(
					true);
		}
		// getButtonManager().getButton(
		// nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
		// .setEnabled(false);
		// getButtonManager().getButton(
		// nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
		// .setEnabled(false);
		getButtonManager().getButton(
				nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
				.setEnabled(false);
		getButtonManager().getButton(
				nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
				.setEnabled(false);
//		showZeroLikeNull(false);
	
	}

	private boolean isControl1 = true;
	// �ж�����ӻ����޸�
	private boolean addoredit = true;
	private MyClientUI myClientUI;
	private BatchDisposeDlg m_BatchDisDlg = null;

	/**
	 * �������ָ����ť�¼�
	 */
	@Override
	protected void onTd() throws Exception {
		// TODO Auto-generated method stub
		// ((BillManageUI)getBillUI()).getBillListPanel()
		//
		int selectRow = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getSelectedRow();
		if (-1 == selectRow) {
			getBillUI().showErrorMessage("������û�����ݻ���û��ѡ����嵥�ݣ����ܽ��в�����");
			return;
		}
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
								.getBillTable().getSelectedRow(), "invcode");
		if (null == geb_cinvbasidy || "".equals(geb_cinvbasidy)) {
			getBillUI().showErrorMessage("��Ʒ����Ϊ��!");
			return;
		}
		// ��֤���κ��Ƿ�Ϊ��
		String geb_vbatchcodey = (String) getBillCardPanelWrapper()
				.getBillCardPanel().getBillModel().getValueAt(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBillTable().getSelectedRow(),
						"geb_vbatchcode");
		if (null == geb_vbatchcodey || "".equals(geb_vbatchcodey)) {
			getBillUI().showErrorMessage("���κŲ���Ϊ��!");
			return;
		}
		// ��֤Ӧ������
		UFDouble geb_bsnumy = (UFDouble) getBillCardPanelWrapper()
				.getBillCardPanel().getBillModel().getValueAt(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBillTable().getSelectedRow(), "geb_bsnum");
		if (null == geb_bsnumy) {
			getBillUI().showErrorMessage("Ӧ����������Ϊ��!");
			return;
		}
		// ��֤���κ��Ƿ���ȷ
		if (geb_vbatchcodey.trim().length() < 8) {
			getBillUI().showErrorMessage("���κŲ���С��8λ!");
			return;
		}
		// String geb_vbatchcodey1 = geb_vbatchcodey.trim().substring(4, 6);
		// String geb_vbatchcodey2 = geb_vbatchcodey.trim().substring(6, 8);
		// if (Integer.parseInt(geb_vbatchcodey1) < 1
		// || Integer.parseInt(geb_vbatchcodey1) > 12
		// || Integer.parseInt(geb_vbatchcodey2) < 1
		// || Integer.parseInt(geb_vbatchcodey2) > 31) {
		// getBillUI()
		// .showErrorMessage("���κ�����Ĳ���ȷ,����������ȷ������!�磺20100101XXXXXX");
		// return;
		// }
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
			getBillUI().showErrorMessage("��û��ѡ��������Ȩ����!");
			return;
		}
		// ���ѡ�еı���VO
		TbGeneralBVO tbGeneralBVO = (TbGeneralBVO) getBillCardPanelWrapper()
				.getBillCardPanel().getBillModel().getBodyValueRowVO(b[0],
						TbGeneralBVO.class.getName());
		TrayDisposeDlg tdpDlg = new TrayDisposeDlg("80AA", ClientEnvironment
				.getInstance().getUser().getPrimaryKey(), ClientEnvironment
				.getInstance().getCorporation().getPrimaryKey(), "8004040210",
				tbGeneralBVO, myClientUI);
		TbGeneralBVO vos = tdpDlg.getReturnVOs(tbGeneralBVO);
		if (null == vos) {
			return;
		}
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
				vos.getGeb_banum(), b[0], "geb_banum");
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
				vos.getGeb_anum(), b[0], "geb_anum");
		m_geb_banum = vos.getGeb_banum().toDouble();
		brow = b[0];
		// �õ�ȫ������VO
		TbGeneralBVO[] tbGeneralBVOs = null;
		if (getBillManageUI().isListPanelSelected()) {
			if (getBufferData().getVOBufferSize() != 0) {
				tbGeneralBVOs = (TbGeneralBVO[]) getBillManageUI()
						.getBillListWrapper().getVOFromUI().getChildrenVO();
			}
		} else {
			if (getBufferData().getVOBufferSize() != 0) {
				tbGeneralBVOs = (TbGeneralBVO[]) getBillCardPanelWrapper()
						.getBillVOFromUI().getChildrenVO();
			}
		}
		// ��ȡ���жϱ����Ƿ���ֵ������ѭ��
		if (null != tbGeneralBVOs && tbGeneralBVOs.length > 0) {
			for (int i = 0; i < tbGeneralBVOs.length; i++) {
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
				// ��ñ���
				StringBuffer sql = new StringBuffer(
						"select cdt_pk from bd_cargdoc_tray where cdt_invbasdoc='"
								+ tbGeneralBVOs[i].getGeb_cinvbasid()
								+ "' and cdt_traystatus=0 and dr=0 and  pk_cargdoc='"
								+ pk_cargdoc + "' ");
				IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance()
						.lookup(IUAPQueryBS.class.getName());
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
				sqlVolume.append(tbGeneralBVOs[i].getGeb_cinvbasid());
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
					invVolume = Double.parseDouble(((Object[]) cdtsVolume
							.get(0))[0].toString())
							* cdts.size();
				} else {
					getBillUI().showErrorMessage("����д�����ݻ���");
					return;
				}
				// ��ȡ��ǰ�����е�Ӧ����������ʵ�����������бȽϣ����ݱȽϽ��������ɫ��ʾ
				// ��ɫ��û��ʵ����������ɫ����ʵ����������������������ɫ��ʵ��������Ӧ���������
				Object num = getBillCardPanelWrapper().getBillCardPanel()
						.getBodyValueAt(i, "geb_bsnum");// Ӧ�ո�����

				if (null != num && !"".equals(num)) {
					if (Double.parseDouble(num.toString().trim()) > invVolume)
						getBillCardPanelWrapper().getBillCardPanel()
								.getBodyPanel().setCellBackGround(i, "invcode",
										Color.red);
					else
						getBillCardPanelWrapper().getBillCardPanel()
								.getBodyPanel().setCellBackGround(i, "invcode",
										Color.white);
				} else
					getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
							.setCellBackGround(i, "invcode", Color.red);
			}
		}
	}

	private int brow = 0;
	private double m_geb_banum;

	/**
	 * ����Զ������ť�¼�
	 */
	@Override
	protected void onAi() throws Exception {
		// TODO Auto-generated method stub
		// ��λ
		String pk_cargdoc = "";
		try {
			pk_cargdoc = nc.ui.wds.w8000.CommonUnit
					.getCargDocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
		} catch (BusinessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//
		int bodyrownum = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getRowCount();
		if (bodyrownum == 0) {
			getBillUI().showErrorMessage("�ӱ���Ϊ�գ�");
			return;
		}
		// ��֤��Ʒ����
		for (int i = 0; i < bodyrownum; i++) {
			String geb_cinvbasidy = (String) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getValueAt(i, "invcode");
			if (null == geb_cinvbasidy || "".equals(geb_cinvbasidy)) {
				getBillUI().showErrorMessage("��Ʒ����Ϊ��!");
				return;
			}
			// ��֤���κ��Ƿ�Ϊ��
			String geb_vbatchcodey = (String) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getValueAt(i,
							"geb_vbatchcode");
			if (null == geb_vbatchcodey || "".equals(geb_vbatchcodey)) {
				getBillUI().showErrorMessage("���κŲ���Ϊ��!");
				return;
			}
			// ��֤Ӧ������
			UFDouble geb_bsnumy = (UFDouble) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getValueAt(i,
							"geb_bsnum");
			if (null == geb_bsnumy) {
				getBillUI().showErrorMessage("Ӧ����������Ϊ��!");
				return;
			}
			// ��֤���κ��Ƿ���ȷ
			if (geb_vbatchcodey.trim().length() < 8) {
				getBillUI().showErrorMessage("���κŲ���С��8λ!");
				return;
			}
			// String geb_vbatchcodey1 = geb_vbatchcodey.trim().substring(4, 6);
			// String geb_vbatchcodey2 = geb_vbatchcodey.trim().substring(6, 8);
			// if (Integer.parseInt(geb_vbatchcodey1) < 1
			// || Integer.parseInt(geb_vbatchcodey1) > 12
			// || Integer.parseInt(geb_vbatchcodey2) < 1
			// || Integer.parseInt(geb_vbatchcodey2) > 31) {
			// getBillUI()
			// .showErrorMessage("���κ�����Ĳ���ȷ,����������ȷ������!�磺20100101XXXXXX");
			// return;
			// }
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
		// �õ�ȫ������VO
		TbGeneralBVO[] tbGeneralBVOs = null;
		if (getBillManageUI().isListPanelSelected()) {
			if (getBufferData().getVOBufferSize() != 0) {
				tbGeneralBVOs = (TbGeneralBVO[]) getBillManageUI()
						.getBillListWrapper().getVOFromUI().getChildrenVO();

			}

		} else {
			if (getBufferData().getVOBufferSize() != 0) {
				tbGeneralBVOs = (TbGeneralBVO[]) getBillCardPanelWrapper()
						.getBillVOFromUI().getChildrenVO();

			}

		}
		// for (int i = 0; i < tbGeneralBVOs.length; i++) {
		// if (null != tbGeneralBVOs[i].getGeb_banum()) {
		// String geb_banum = tbGeneralBVOs[i].getGeb_banum().toString();
		// if ("".equals(geb_banum)) {
		// getBillUI().showErrorMessage("�Զ��������дʵ��������");
		// return;
		// }
		// } else {
		// getBillUI().showErrorMessage("�Զ��������дʵ��������");
		// return;
		// }
		//
		// }
		// �жϱ����Ƿ�������
		if (null == tbGeneralBVOs || tbGeneralBVOs.length == 0) {
			getBillUI().showErrorMessage("������û�����ݣ����ܽ��в�������Ȩ������");
			return;
		}
		// ��ȡ���жϱ����Ƿ���ֵ������ѭ��
		if (null != tbGeneralBVOs && tbGeneralBVOs.length > 0) {
			for (int i = 0; i < tbGeneralBVOs.length; i++) {

				// ��ñ���
				StringBuffer sql = new StringBuffer(
						"select cdt_pk from bd_cargdoc_tray where cdt_invbasdoc='"
								+ tbGeneralBVOs[i].getGeb_cinvbasid()
								+ "' and cdt_traystatus=0 and dr=0 and  pk_cargdoc='"
								+ pk_cargdoc + "' ");
				IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance()
						.lookup(IUAPQueryBS.class.getName());
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
				sqlVolume.append(tbGeneralBVOs[i].getGeb_cinvbasid());
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
					invVolume = Double.parseDouble(((Object[]) cdtsVolume
							.get(0))[0].toString())
							* cdts.size();
				} else {
					getBillUI().showErrorMessage("����д�����ݻ���");
					return;
				}
				// ��ȡ��ǰ�����е�Ӧ����������ʵ�����������бȽϣ����ݱȽϽ��������ɫ��ʾ
				// ��ɫ��û��ʵ����������ɫ����ʵ����������������������ɫ��ʵ��������Ӧ���������
				Object num = getBillCardPanelWrapper().getBillCardPanel()
						.getBodyValueAt(i, "geb_bsnum");// Ӧ�ո�����

				if (null != num && !"".equals(num)) {
					if (Double.parseDouble(num.toString().trim()) > invVolume)
						getBillCardPanelWrapper().getBillCardPanel()
								.getBodyPanel().setCellBackGround(i, "invcode",
										Color.red);
					else
						getBillCardPanelWrapper().getBillCardPanel()
								.getBodyPanel().setCellBackGround(i, "invcode",
										Color.white);
				} else
					getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
							.setCellBackGround(i, "invcode", Color.red);
			}
		}
		// �ж������ݻ��Ƿ��ܴ�Ż���
		for (int i = 0; i < tbGeneralBVOs.length; i++) {
			// ��ñ���
			StringBuffer sql = new StringBuffer(
					"select cdt_pk from bd_cargdoc_tray where cdt_invbasdoc='"
							+ tbGeneralBVOs[i].getGeb_cinvbasid()
							+ "' and cdt_traystatus=0 and dr=0 and  pk_cargdoc='"
							+ pk_cargdoc + "' ");
			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
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
			sqlVolume.append(tbGeneralBVOs[i].getGeb_cinvbasid());
			sqlVolume.append("'");
			ArrayList cdtsVolume = new ArrayList();
			cdtsVolume = (ArrayList) query.executeQuery(sqlVolume.toString(),
					new ArrayListProcessor());
			// ��Ʒ��ǰ���ݻ�
			double invVolume = 0;
			if (null != cdts && null != cdtsVolume && null != cdtsVolume.get(0)
					&& null != ((Object[]) cdtsVolume.get(0))[0]
					&& !"".equals(((Object[]) cdtsVolume.get(0))[0].toString())) {
				invVolume = Double
						.parseDouble(((Object[]) cdtsVolume.get(0))[0]
								.toString())
						* cdts.size();
			} else {
				getBillUI().showErrorMessage("����д�����ݻ���");
				return;
			}
			// ��⸨����
			String geb_bsnum = "";
			if (null != tbGeneralBVOs[i].getGeb_bsnum()) {
				geb_bsnum = tbGeneralBVOs[i].getGeb_bsnum().toString();
			}
			// ��⸨����
			double geb_bsnumd = 0;
			if (null != geb_bsnum && !"".equals(geb_bsnum)) {
				geb_bsnumd = Double.parseDouble(geb_bsnum);
			}

			if (geb_bsnumd > invVolume) {
				getBillUI().showErrorMessage("����������ڵ�ǰ����ݻ���(�������Ϊ��ɫ�����̲���) ");
				return;
			}
		}
		// ���������Ϣ
		for (int i = 0; i < tbGeneralBVOs.length; i++) {
			// ��ñ���
			StringBuffer sql = new StringBuffer(
					"select cdt_pk from bd_cargdoc_tray where cdt_invbasdoc='"
							+ tbGeneralBVOs[i].getGeb_cinvbasid()
							+ "' and cdt_traystatus=0 and dr=0 and  pk_cargdoc='"
							+ pk_cargdoc + "' ");
			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
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
			sqlVolume.append(tbGeneralBVOs[i].getGeb_cinvbasid());
			sqlVolume.append("' and dr=0 ");
			ArrayList cdtsVolume = new ArrayList();
			cdtsVolume = (ArrayList) query.executeQuery(sqlVolume.toString(),
					new ArrayListProcessor());
			double invTrayVolume = Double.parseDouble(((Object[]) cdtsVolume
					.get(0))[0].toString());
			// ��⸨����
			String geb_bsnum = "";
			if (null != tbGeneralBVOs[i].getGeb_bsnum()) {
				geb_bsnum = tbGeneralBVOs[i].getGeb_bsnum().toString();
			}
			// ��ǰ��⸨����
			double geb_bsnumd = 0;
			if (null != geb_bsnum && !"".equals(geb_bsnum)) {
				geb_bsnumd = Double.parseDouble(geb_bsnum);
			}
			// ������̸���
			// int traynum=0;
			// if(geb_bsnumd!=0&&invTrayVolume!=0){
			// traynum=
			// }
			//
			if (null != cdts) {

			}
			// TbGeneralBBVO[] tbGeneralBBVO = null;
			// ���Ҫɾ����VO
			StringBuffer tbbsql = new StringBuffer("pwb_pk='");
			tbbsql.append(tbGeneralBVOs[i].getGeb_cgeneralbid());
			tbbsql.append("' and pk_invbasdoc='");
			tbbsql.append(tbGeneralBVOs[i].getGeb_cinvbasid());
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
			int k = 0;
			while (geb_bsnumd > invTrayVolume) {
				TbGeneralBBVO tbgbbvo = new TbGeneralBBVO();
				// ����
				tbgbbvo
						.setGebb_vbatchcode(tbGeneralBVOs[i]
								.getGeb_vbatchcode());
				// ��д����
				tbgbbvo.setGebb_lvbatchcode(tbGeneralBVOs[i]
						.getGeb_backvbatchcode());
				// �к�
				tbgbbvo.setGebb_rowno(k + 1 + "0");
				// ���ⵥ��������
				tbgbbvo.setPwb_pk(tbGeneralBVOs[i].getGeb_cgeneralbid());
				// ������
				tbgbbvo.setGebb_hsl(tbGeneralBVOs[i].getGeb_hsl());
				// �˻���������
				tbgbbvo.setPk_invbasdoc(tbGeneralBVOs[i].getGeb_cinvbasid());
				// ��ⵥ�ӱ�����
				tbgbbvo.setGeb_pk(tbGeneralBVOs[i].getGeb_pk());
				// ����
				tbgbbvo.setGebb_nprice(tbGeneralBVOs[i].getGeb_nprice());
				// ���
				tbgbbvo.setGebb_nmny(tbGeneralBVOs[i].getGeb_nmny());
				// ����ʵ�ʴ������
				tbgbbvo.setGebb_num(new UFDouble(invTrayVolume));
				geb_bsnumd = geb_bsnumd - invTrayVolume;
				// ��������
				if (null != ((Object[]) cdts.get(k))[0]) {
					tbgbbvo.setCdt_pk(((Object[]) cdts.get(k))[0].toString());
				}
				// DR
				tbgbbvo.setDr(0);
				tbgbbvo.setGeb_pk(tbGeneralBVOs[i].getGeb_pk());
				tbGeneralBBVOs.add(tbgbbvo);
				// // ����״̬
				//
				// if (null != ((Object[]) cdts.get(k))[0]) {
				// Cdt_pk = ((Object[]) cdts.get(k))[0].toString();
				// }
				// BdCargdocTrayVO bdCargdocTrayVO = (BdCargdocTrayVO) query
				// .retrieveByPK(BdCargdocTrayVO.class, Cdt_pk);
				// bdCargdocTrayVO.setCdt_traystatus(new Integer(1));
				// bdCargdocTrayVOs.add(bdCargdocTrayVO);
				k++;
			}
			TbGeneralBBVO tbgbbvo1 = new TbGeneralBBVO();
			// ����
			tbgbbvo1.setGebb_vbatchcode(tbGeneralBVOs[i].getGeb_vbatchcode());
			// ��д����
			tbgbbvo1.setGebb_lvbatchcode(tbGeneralBVOs[i]
					.getGeb_backvbatchcode());
			// �к�
			tbgbbvo1.setGebb_rowno(k + 1 + "0");
			// ���ⵥ��������
			tbgbbvo1.setPwb_pk(tbGeneralBVOs[i].getGeb_cgeneralbid());
			// ������
			tbgbbvo1.setGebb_hsl(tbGeneralBVOs[i].getGeb_hsl());
			// �˻���������
			tbgbbvo1.setPk_invbasdoc(tbGeneralBVOs[i].getGeb_cinvbasid());
			// ��ⵥ�ӱ�����
			tbgbbvo1.setGeb_pk(tbGeneralBVOs[i].getGeb_pk());
			// ����
			tbgbbvo1.setGebb_nprice(tbGeneralBVOs[i].getGeb_nprice());
			// ���
			tbgbbvo1.setGebb_nmny(tbGeneralBVOs[i].getGeb_nmny());
			// ����ʵ�ʴ������
			tbgbbvo1.setGebb_num(new UFDouble(geb_bsnumd));
			// ��������
			if (null != ((Object[]) cdts.get(k))[0]) {
				tbgbbvo1.setCdt_pk(((Object[]) cdts.get(k))[0].toString());
			}
			// DR
			tbgbbvo1.setDr(0);
			tbGeneralBBVOs.add(tbgbbvo1);
			// // ����״̬
			//
			// if (null != ((Object[]) cdts.get(k))[0]) {
			// Cdt_pk = ((Object[]) cdts.get(k))[0].toString();
			// }
			// BdCargdocTrayVO bdCargdocTrayVO = (BdCargdocTrayVO) query
			// .retrieveByPK(BdCargdocTrayVO.class, Cdt_pk);
			// bdCargdocTrayVO.setCdt_traystatus(new Integer(1));
			// bdCargdocTrayVOs.add(bdCargdocTrayVO);
			//
			TbGeneralBBVO[] tbGeneralBBVO = new TbGeneralBBVO[tbGeneralBBVOs
					.size()];
			tbGeneralBBVOs.toArray(tbGeneralBBVO);
			// ����Զ���ӿ�
			Iw8004040210 iw = (Iw8004040210) NCLocator.getInstance().lookup(
					Iw8004040210.class.getName());
			try {
				iw.delAndInsertTbGeneralBBVO(dtbbvos, tbGeneralBBVO);
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		TrayDisposeDetailDlg tdpDlg = new TrayDisposeDetailDlg("80BB",
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey(), "8004040210", tbGeneralBVOs);
		AggregatedValueObject[] vos = tdpDlg.getReturnVOs(tbGeneralBVOs);

		int rownum = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getRowCount();
		for (int i = 0; i < rownum; i++) {
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
					getBillCardPanelWrapper().getBillCardPanel()
							.getBodyValueAt(i, "geb_bsnum"), i, "geb_banum");
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
					getBillCardPanelWrapper().getBillCardPanel()
							.getBodyValueAt(i, "geb_snum"), i, "geb_anum");
		}
		if (null == vos || vos.length == 0) {
			return;
		}
	}

	/**
	 * ��Ӳ鿴��ϸ��ť�¼�
	 */
	@Override
	protected void onVd() throws Exception {
		// TODO Auto-generated method stub
		if (getBufferData().getVOBufferSize() <= 0) {
			getBillUI().showErrorMessage("������û�����ݻ���û��ѡ����嵥�ݣ����ܽ��в���!");
			return;
		}
		// �õ�ȫ������VO
		TbGeneralBVO[] tbGeneralBVOs = null;
		if (getBillManageUI().isListPanelSelected()) {
			if (getBufferData().getVOBufferSize() != 0) {
				tbGeneralBVOs = (TbGeneralBVO[]) getBillManageUI()
						.getBillListWrapper().getVOFromUI().getChildrenVO();
			}
		} else {
			if (getBufferData().getVOBufferSize() != 0) {
				tbGeneralBVOs = (TbGeneralBVO[]) getBillCardPanelWrapper()
						.getBillVOFromUI().getChildrenVO();
			}
		}
		// �жϱ����Ƿ�������
		if (null == tbGeneralBVOs || tbGeneralBVOs.length == 0) {
			getBillUI().showErrorMessage("������û�����ݣ����ܽ��в�����");
			return;
		}
		TrayDisposeDetailDlg tdpDlg = new TrayDisposeDetailDlg("80BB",
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey(), "8004040210", tbGeneralBVOs);
		AggregatedValueObject[] vos = tdpDlg.getReturnVOs(tbGeneralBVOs);
	}

	// �б�UI
	private BillManageUI getBillManageUI() {
		return (BillManageUI) getBillUI();
	}

	protected void onCtAdd() {
		// TODO Auto-generated method stub
		// getBillUI().showErrorMessage("12345");
	}

	/**
	 * ��������
	 */
	protected void onSsAdd() {
		// TODO ��ʵ�ִ˰�ť�¼����߼�
		// TbGeneralhDlg tghQuery = new TbGeneralhDlg(myClientUI);
		// AggregatedValueObject[] vos = tghQuery.getReturnVOs(ClientEnvironment
		// .getInstance().getCorporation().getPrimaryKey(),
		// ClientEnvironment.getInstance().getUser().getPrimaryKey(),
		// "0210", "8004040210", "8004040210", "02100282", myClientUI);
		if (!sotckIsTotal) {
			getBillUI().showErrorMessage("���ֱֲ���Ա������Ȩ������");
			return;
		}
		// ��ǰ��¼�ı���Ա�ܹ���Ļ�Ʒ
		try {
			List invLisk = nc.ui.wds.w8000.CommonUnit
					.getInvbasdoc_Pk(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
			if (null == invLisk || invLisk.size() == 0) {
				getBillUI().showErrorMessage("����Ȩ������");
				return;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ProdWaybillDlg prodWaybillDlg = new ProdWaybillDlg(myClientUI);
		AggregatedValueObject[] vos = prodWaybillDlg.getReturnVOs(
				ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey(), ClientEnvironment.getInstance()
						.getUser().getPrimaryKey(), "0206", "4Y", "8004040210",
				"02100282", myClientUI);
		try {
			if (null == vos || vos.length == 0) {
				return;
			}
			if (vos.length > 2) {
				getBillUI().showErrorMessage("һ��ֻ��ѡ��һ�ų��ⵥ��");
				return;
			}
			// TbProdwaybillVO genh = (TbProdwaybillVO) vos[0].getParentVO();
			// if (((BillManageUI)getBillUI()).isListPanelSelected())
			// ((BillManageUI)getBillUI()).setCurrentPanel(BillTemplateWrapper.CARDPANEL);
			// MyBillVO voForSave = queryLocaData(vos);
			// if (null == voForSave) {
			MyBillVO voForSave = changeTbpwbtoTbgen(vos);
			// }
			getBufferData().clear();
			getBufferData().addVOToBuffer(voForSave);
			updateBuffer();
			// getBillUI().setBillOperate(IBillOperate.OP_EDIT);
			super.onBoEdit();
			getButtonManager().getButton(IBillButton.Edit).setEnabled(false);

			getButtonManager().getButton(IBillButton.Save).setEnabled(true);
			getButtonManager().getButton(IBillButton.Cancel).setEnabled(true);

			getButtonManager().getButton(IBillButton.Refresh).setEnabled(false);
			getButtonManager().getButton(IBillButton.Query).setEnabled(false);
			getButtonManager().getButton(
					nc.ui.wds.w8004040210.ssButtun.ISsButtun.Td).setEnabled(
					true);
			getButtonManager().getButton(
					nc.ui.wds.w8004040210.ssButtun.ISsButtun.Ai).setEnabled(
					true);
			getButtonManager().getButton(
					nc.ui.wds.w8004040210.ssButtun.ISsButtun.SsAdd).setEnabled(
					false);
			getBillUI().updateButtonUI();

		} catch (Exception e) {
			getBillUI().showErrorMessage(e.getMessage());
		}
		// addoredit = true;
		showZeroLikeNull(false);
		isedit = false;
	}

	// ��ӷ��˼ƻ��˵���ť�¼�
	protected void onsp() {
		// TODO ��ʵ�ִ˰�ť�¼����߼�
		throw new BusinessRuntimeException("δʵ�ִ˰�ť���¼������߼�,�뵽"
				+ this.getClass().getName() + "��onsp�������޸�");
	}

	// ������������˵���ť�¼�
	protected void onpd() {
		// TODO ��ʵ�ִ˰�ť�¼����߼�
		// TbGeneralhDlg tghQuery = new TbGeneralhDlg(myClientUI);
		// AggregatedValueObject[] vos = tghQuery.getReturnVOs(ClientEnvironment
		// .getInstance().getCorporation().getPrimaryKey(),
		// ClientEnvironment.getInstance().getUser().getPrimaryKey(),
		// "0210", "8004040210", "8004040210", "02100282", myClientUI);
		// ProdWaybillDlg prodWaybillDlg = new ProdWaybillDlg(myClientUI);
		// AggregatedValueObject[] vos = prodWaybillDlg.getReturnVOs(
		// ClientEnvironment.getInstance().getCorporation()
		// .getPrimaryKey(), ClientEnvironment.getInstance()
		// .getUser().getPrimaryKey(), "0206", "4Y", "8004040210",
		// "02100282", myClientUI);
		// try {
		//
		// if (null == vos || vos.length == 0) {
		//
		// return;
		// }
		// if (vos.length > 2) {
		// getBillUI().showErrorMessage("һ��ֻ��ѡ��һ�ų��ⵥ��");
		// return;
		// }
		// // TbProdwaybillVO genh = (TbProdwaybillVO) vos[0].getParentVO();
		// // if (((BillManageUI)getBillUI()).isListPanelSelected())
		// //
		// ((BillManageUI)getBillUI()).setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		//
		// MyBillVO voForSave = changeTbpwbtoTbgen(vos);
		// getBufferData().clear();
		// getBufferData().addVOToBuffer(voForSave);
		// updateBuffer();
		// // getBillUI().setBillOperate(IBillOperate.OP_EDIT);
		// super.onBoEdit();
		// getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
		//
		// getButtonManager().getButton(IBillButton.Save).setEnabled(true);
		// getButtonManager().getButton(IBillButton.Cancel).setEnabled(true);
		//
		// getButtonManager().getButton(IBillButton.Print).setEnabled(false);
		// getButtonManager().getButton(IBillButton.Refresh).setEnabled(false);
		// getButtonManager().getButton(IBillButton.Query).setEnabled(false);
		// getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
		// getBillUI().updateButtonUI();
		//
		// } catch (Exception e) {
		// getBillUI().showErrorMessage(e.getMessage());
		// }
		// addoredit = true;
		// showZeroLikeNull(false);
	}

	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		// ��ǰ��¼�ı���Ա�ܹ���Ļ�Ʒ
		List InvLisk = nc.ui.wds.w8000.CommonUnit
				.getInvbasdoc_Pk(ClientEnvironment.getInstance().getUser()
						.getPrimaryKey());
		boolean isControl = true;
		if ("0".equals(st_type)) {
			isControl = false;
		}
		// ����Ҫ�л��б�ģʽ��һ�Σ������������
		super.onBoReturn();
		if (!isControl) {
			// �ж��Ƿ�Ϊ�գ����Ϊ�� ˵����ǰ��¼�߲��ǲֿⱣ��Ա
			if (null != InvLisk && InvLisk.size() > 0) {
				List list = new ArrayList();
				TbGeneralBVO[] generalbvo = null;
				// ��ȡ��ǰѡ����
				AggregatedValueObject billvo = getBufferData().getCurrentVO();
				for (int i = 0; i < InvLisk.size(); i++) {
					// �ж�ѡ�����Ƿ�������
					if (null != billvo && null != billvo.getParentVO()
							&& null != billvo.getChildrenVO()
							&& billvo.getChildrenVO().length > 0) {
						// ȡ���ӱ�����
						generalbvo = (TbGeneralBVO[]) billvo.getChildrenVO();
						// �����ж��Ƿ�Ϊ��Ȼ�����ѭ��
						if (null != generalbvo && generalbvo.length > 0) {
							for (int j = 0; j < generalbvo.length; j++) {
								// �����ǰ��¼�ֿ߲��еĵ�Ʒ�����͵����еĵ�Ʒ������ȷŵ�һ���������� ȥ Ȼ�����ҳ��
								if (InvLisk.get(i).equals(
										generalbvo[j].getGeb_cinvbasid())) {
									list.add(generalbvo[j]);
								}
							}
						}
					}
				}
				if (null != list && list.size() > 0) {
					// ��������ת��
					generalbvo = new TbGeneralBVO[list.size()];
					generalbvo = (TbGeneralBVO[]) list.toArray(generalbvo);
					billvo.setChildrenVO(generalbvo);
					// �ڸ��µ���ǰѡ������
					getBufferData().setCurrentVO(billvo);
					// ����ָ����ť���Զ�ȡ����ť����
					// changeButton(true);
				} else {
					// �����������û������ ��ǰ��¼���������ֿ�ı���Ա
					// �Ѹ������ܰ�ť��Ϊ������
					// getButtonManager().getButton(ISsButtun.fzgn).setEnabled(
					// false);
					billvo.setChildrenVO(null);
					getBufferData().setCurrentVO(billvo);
				}
			}
		}
		// super.onBoEdit();
		if (isControl) {
			// �����շ����ͱ�ע�ɱ༭
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"geh_cdispatcherid").setEdit(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"geh_cwarehouseid").setEdit(true);
			getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("geh_corp").setEdit(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"geh_calbody").setEdit(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"geh_cwhsmanagerid").setEdit(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vnote")
					.setEdit(true);
		} else {
			getButtonManager().getButton(
					nc.ui.wds.w8004040210.ssButtun.ISsButtun.Td).setEnabled(
					true);
			getButtonManager().getButton(
					nc.ui.wds.w8004040210.ssButtun.ISsButtun.Ai).setEnabled(
					true);
			getButtonManager().getButton(
					nc.ui.wds.w8004040210.ssButtun.ISsButtun.SsAdd).setEnabled(
					false);
			getBillUI().updateButtonUI();
		}
		if ("3".equals(st_type)) {
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"geh_cdispatcherid").setEdit(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"geh_cwarehouseid").setEdit(true);
			getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("geh_corp").setEdit(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"geh_calbody").setEdit(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"geh_cwhsmanagerid").setEdit(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vnote")
					.setEdit(true);
			getButtonManager().getButton(
					nc.ui.wds.w8004040210.ssButtun.ISsButtun.Td).setEnabled(
					true);
			getButtonManager().getButton(
					nc.ui.wds.w8004040210.ssButtun.ISsButtun.Ai).setEnabled(
					true);
			getButtonManager().getButton(
					nc.ui.wds.w8004040210.ssButtun.ISsButtun.SsAdd).setEnabled(
					false);
			getBillUI().updateButtonUI();
		}
		super.onBoEdit();
		getButtonManager().getButton(
				nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
				.setEnabled(false);
		getButtonManager().getButton(
				nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
				.setEnabled(false);
		addoredit = false;
		isedit = true;
	}

	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		// ��������
		TbGeneralBVO[] tbGeneralBVOs = (TbGeneralBVO[]) getBillUI()
				.getVOFromUI().getChildrenVO();
		super.onBoCancel();
		if (addoredit) {
			getBufferData().clear();
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
			getBillUI().initUI();
			// super.onBoRefresh();
		}
		IVOPersistence perse = (IVOPersistence) NCLocator.getInstance().lookup(
				IVOPersistence.class.getName());
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"geh_cdispatcherid").setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"geh_cwarehouseid").setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_corp")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_calbody")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"geh_cwhsmanagerid").setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vnote")
				.setEdit(false);
		getButtonManager().getButton(
				nc.ui.wds.w8004040210.ssButtun.ISsButtun.Td).setEnabled(false);
		getButtonManager().getButton(
				nc.ui.wds.w8004040210.ssButtun.ISsButtun.Ai).setEnabled(false);
		if (!isControl1) {
			getButtonManager().getButton(
					nc.ui.wds.w8004040210.ssButtun.ISsButtun.SsAdd).setEnabled(
					true);
		}
		if (!isControl1) {
			getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
		}
		getBillUI().updateButtonUI();
		// // ͨ�����ⵥ�ӱ������õ��������
		// StringBuffer tgbbsql = new StringBuffer(" pwb_pk in ('");
		// if (null != tbGeneralBVOs && tbGeneralBVOs.length > 0) {
		// for (int i = 0; i < tbGeneralBVOs.length; i++) {
		// if (null != tbGeneralBVOs[i].getGeb_cgeneralbid()
		// && !""
		// .equals(tbGeneralBVOs[i]
		// .getGeb_cgeneralbid())) {
		// tgbbsql.append(tbGeneralBVOs[i].getGeb_cgeneralbid());
		// tgbbsql.append("','");
		// }
		//
		// }
		// tgbbsql.append("') and dr=0 ");
		// }
		// // ���̻����
		// IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
		// IUAPQueryBS.class.getName());
		// ArrayList tbGeneralBBVOs = (ArrayList) query.retrieveByClause(
		// TbGeneralBBVO.class, tgbbsql.toString());
		//		
		// //
		// Iw8004040210 iw = (Iw8004040210) NCLocator.getInstance().lookup(
		// Iw8004040210.class.getName());
		// iw.delTbGeneralBBVO(tbGeneralBBVOs);
		if (null != st_type && st_type.equals("1")
				&& getBufferData().getVOBufferSize() > 0) {
			AggregatedValueObject billvo = getBillUI().getVOFromUI();
			TbGeneralHVO generalhvo = null;
			if (null != billvo.getParentVO()) {
				generalhvo = (TbGeneralHVO) billvo.getParentVO();
			}
			// ǩ�ֺ�
			if (null != generalhvo.getPwb_fbillflag()
					&& generalhvo.getPwb_fbillflag() == 3) {
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
						.setEnabled(false);
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
						.setEnabled(true);
				getBillUI().updateButtonUI();
			} else { // ǩ��ǰ
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
						.setEnabled(true);
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
						.setEnabled(false);
				getBillUI().updateButtonUI();
			}
		}
		if (null != st_type && st_type.equals("3")
				&& getBufferData().getVOBufferSize() > 0) {

			AggregatedValueObject billvo = getBillUI().getVOFromUI();
			TbGeneralHVO generalhvo = null;
			if (null != billvo.getParentVO()) {
				generalhvo = (TbGeneralHVO) billvo.getParentVO();
			}
			// ǩ�ֺ�
			if (null != generalhvo.getPwb_fbillflag()
					&& generalhvo.getPwb_fbillflag() == 3) {
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
						.setEnabled(false);
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
						.setEnabled(true);
				getBillUI().updateButtonUI();
			} else { // ǩ��ǰ
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
						.setEnabled(true);
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
						.setEnabled(false);
				getBillUI().updateButtonUI();
			}
		}
		if (null != st_type && st_type.equals("3")) {
			getButtonManager().getButton(
					nc.ui.wds.w8004040210.ssButtun.ISsButtun.SsAdd).setEnabled(
					true);
			getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
			getBillUI().updateButtonUI();
		}
		isedit = true;
	}

	private List myTempall;

	private MyBillVO changeTbpwbtoTbgen(AggregatedValueObject[] vos) {
		MyBillVO myBillVO = new MyBillVO();
		// ������ⵥ����VO
		TbGeneralHVO tbGeneralHVO = new TbGeneralHVO();
		// �������ⵥ����VO
		GeneralBillHeaderVO firstVO = (GeneralBillHeaderVO) vos[0]
				.getParentVO();
		// ����˵����ݺ�
		// ��ӵ������ⵥ���ݺ�
		tbGeneralHVO.setGeh_vbillcode(firstVO.getVbillcode());
		// �˵���ͷ����
		// tbGeneralHVO.setPwb_pk(tbProdwaybillVO.getPwb_pk());
		// �������ⵥ��ͷ����
		tbGeneralHVO.setGeh_cgeneralhid(firstVO.getCgeneralhid());
		// ��ⵥ���ݺ�
		// try {
		// billCode = getBillCode("4E", "", "", "");
		// tbGeneralHVO.setGeh_billcode(billCode);
		// } catch (BusinessException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// ���⹫˾����
		tbGeneralHVO.setGeh_cothercorpid(firstVO.getPk_corp());
		// ��������֯����
		tbGeneralHVO.setGeh_cothercalbodyid(firstVO.getPk_calbody());
		// ����ֿ�����
		tbGeneralHVO.setGeh_cotherwhid(firstVO.getCwarehouseid());
		// ҵ��Ա����
		tbGeneralHVO.setGeh_cbizid(firstVO.getCbizid());
		// �շ�����ID
		tbGeneralHVO.setGeh_cdispatcherid(firstVO.getCdispatcherid());
		// ��⹫˾����
		if (null != firstVO.getAttributeValue("cothercorpid")) {
			tbGeneralHVO.setGeh_corp(firstVO.getAttributeValue("cothercorpid")
					.toString());
		}
		// �������֯����
		if (null != firstVO.getAttributeValue("cothercalbodyid")) {
			tbGeneralHVO.setGeh_calbody(firstVO.getAttributeValue(
					"cothercalbodyid").toString());
		}
		// ���ֿ�����
		if (null != firstVO.getAttributeValue("cotherwhid")) {
			tbGeneralHVO.setGeh_cwarehouseid("1021A91000000004YZ0P");
		}
		// ������Ա����
		tbGeneralHVO.setGeh_cwhsmanagerid(firstVO.getCwhsmanagerid());
		// �������ͱ�־
		tbGeneralHVO.setGeh_fallocflag(firstVO.getFallocflag());
		// �Ƿ�����˻�
		// tbGeneralHVO.setGeh_freplenishflag(firstVO.getFreplenishflag());
		// ҵ������
		tbGeneralHVO.setGeh_cbiztype(firstVO.getCbiztypeid());
		// �Ƶ����ڣ���������
		tbGeneralHVO.setCopetadate(new UFDate(new Date()));
		tbGeneralHVO.setGeh_dbilldate(new UFDate(new Date()));
		// �Ƶ���
		tbGeneralHVO.setCoperatorid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey());
		// ��ⵥ����
		tbGeneralHVO.setGeh_billtype(0);
		// �������VO
		myBillVO.setParentVO(tbGeneralHVO);
		// ��ȡ�ӱ�vo
		String pk = firstVO.getCgeneralhid().toString();
		String sql = "select cinvbasid ,cinventoryid,crowno,vbatchcode "
				+ ",dvalidate,noutnum,ntranoutnum ,nprice"
				+ ",cgeneralbid ,castunitid,noutassistnum,ntranoutastnum"
				+ ",hsl,flargess from ic_general_b where cgeneralhid='"
				+ firstVO.getCgeneralhid().toString()
				+ "' and ((coalesce(noutnum, 0) > 0 "
				+ " and coalesce(noutnum, 0) - coalesce(ntranoutnum, 0) - coalesce(naccumwastnum, 0) > 0)"
				+ " or (coalesce(noutnum, 0) < 0"
				+ " and coalesce(noutnum, 0) - coalesce(ntranoutnum, 0) - coalesce(naccumwastnum, 0) < 0))"
				+ " and dr=0";
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// ȫ���ӱ�VO
		List myTemp = new ArrayList();
		// ǰ̨��ʾ��VO
		List myTempShow = new ArrayList();

		myTempall = new ArrayList();
		try {
			ArrayList ttcs = (ArrayList) query.executeQuery(sql.toString(),
					new ArrayListProcessor());
			// ��ǰ��¼�ı���Ա�ܹ���Ļ�Ʒ
			List InvLisk = nc.ui.wds.w8000.CommonUnit
					.getInvbasdoc_Pk(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
			for (int i = 0; i < ttcs.size(); i++) {
				Object[] gbvo = (Object[]) ttcs.get(i);
				TbGeneralBVO tbGeneralBVO = new TbGeneralBVO();
				// ����к�
				if (null != gbvo[2]) {
					tbGeneralBVO.setGeb_crowno(gbvo[2].toString());
				}
				// ���������������
				if (null != gbvo[0]) {
					tbGeneralBVO.setGeb_cinvbasid(gbvo[0].toString());
				}

				// ����˵���ͷ����
				// tbGeneralBVO.setPwb_pk(tbProdwaybillVO.getPwb_pk());
				// �˵���������
				// tbGeneralBVO.setPwbb_pk(ttc.getPwbb_pk());
				// �������ⵥ��ͷ����
				tbGeneralBVO.setGeb_cgeneralhid(firstVO.getCgeneralhid());
				// �������ⵥ��������
				if (null != gbvo[8]) {
					tbGeneralBVO.setGeb_cgeneralbid(gbvo[8].toString());
				}
				// ������
				if (null != gbvo[12]) {
					tbGeneralBVO.setGeb_hsl(new UFDouble(gbvo[12].toString()));
				}
				// ��λ
				// if (null != ttc.getPk_measdoc()) {
				// tbGeneralBVO.setPk_measdoc(ttc.getPk_measdoc());
				// }
				// ���κ�
				if (null != gbvo[3]) {
					tbGeneralBVO.setGeb_vbatchcode(gbvo[3].toString());
					// ��д����
					tbGeneralBVO.setGeb_backvbatchcode(gbvo[3].toString());
				}
				// �������Σ���ѯ�������ڣ�ʧЧ����
				String vbcsql = "select dproducedate ,dvalidate  from scm_batchcode where pk_invbasdoc='"
						+ tbGeneralBVO.getGeb_cinvbasid()
						+ "' and vbatchcode='"
						+ tbGeneralBVO.getGeb_vbatchcode() + "' and dr=0";
				ArrayList vbc = (ArrayList) query.executeQuery(vbcsql,
						new ArrayListProcessor());

				if (null != vbc && vbc.size() > 0 && null != vbc.get(0)) {
					// ��������
					if (null != ((Object[]) vbc.get(0))[0]
							&& !""
									.equals(((Object[]) vbc.get(0))[0]
											.toString())) {
						tbGeneralBVO.setGeb_proddate(new UFDate(((Object[]) vbc
								.get(0))[0].toString()));
					}
					// ʧЧ����
					if (null != ((Object[]) vbc.get(0))[1]
							&& !""
									.equals(((Object[]) vbc.get(0))[1]
											.toString())) {
						tbGeneralBVO.setGeb_dvalidate(new UFDate(
								((Object[]) vbc.get(0))[1].toString()));
					}
				}

				// ���Ӧ�ո�������ʵ�ո�����
				double noutassistnum = 0.00;
				double ntranoutastnum = 0.00;
				if (null != gbvo[10]) {
					noutassistnum = Double.parseDouble(gbvo[10].toString());
				}
				if (null != gbvo[11]) {
					ntranoutastnum = Double.parseDouble(gbvo[11].toString());
				}
				// Ӧ�ո�����
				tbGeneralBVO.setGeb_bsnum(new UFDouble(noutassistnum
						- ntranoutastnum));
				// ʵ�ո�����
				// tbGeneralBVO.setGeb_banum(tbGeneralBVO.getGeb_bsnum());
				// ���ʵ��������Ӧ������
				double noutnum = 0.00;
				double ntranoutnum = 0.00;
				if (null != gbvo[5]) {
					noutnum = Double.parseDouble(gbvo[5].toString());
				}
				if (null != gbvo[6]) {
					ntranoutnum = Double.parseDouble(gbvo[6].toString());
				}
				tbGeneralBVO.setGeb_snum(new UFDouble(noutnum - ntranoutnum));

				// tbGeneralBVO.setGeb_anum(tbGeneralBVO.getGeb_snum());
				// ����
				if (null != gbvo[7] && !"".equals(gbvo[7].toString())) {
					tbGeneralBVO
							.setGeb_nprice(new UFDouble(gbvo[7].toString()));
				}
				// ���
				if (null != gbvo[7] && !"".equals(gbvo[7].toString())
						&& null != tbGeneralBVO.getGeb_nprice()) {
					tbGeneralBVO.setGeb_nmny(new UFDouble(Double
							.parseDouble(gbvo[7].toString())
							* (noutnum - ntranoutnum)));
				}
				// �Ƿ���Ʒ
				if (null != gbvo[13] && !"".equals(gbvo[13].toString())) {
					tbGeneralBVO.setGeb_flargess(new UFBoolean(gbvo[13]
							.toString()));
				}
				// ��λ
				String geb_space = nc.ui.wds.w8000.CommonUnit
						.getCargDocName(ClientEnvironment.getInstance()
								.getUser().getPrimaryKey());
				if (null != geb_space && !"".equals(geb_space)) {
					tbGeneralBVO.setGeb_space(geb_space);
				}

				// �ı�״̬
				tbGeneralBVO.setStatus(VOStatus.NEW);

				// gbvo[0].toString();
				//				
				for (int j = 0; j < InvLisk.size(); j++) {
					if (null != gbvo[0]) {
						if (gbvo[0].equals(InvLisk.get(j))) {
							myTempShow.add(tbGeneralBVO);
							break;
						}
					}
				}
				myTempall.add(tbGeneralBVO);
				// addoredit = true;
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TbGeneralBVO[] tbGeneralBVOs = new TbGeneralBVO[myTempShow.size()];
		myTempShow.toArray(tbGeneralBVOs);
		myBillVO.setChildrenVO(tbGeneralBVOs);

		return myBillVO;

	}

	/**
	 * ��ѯ�����Ƿ��Ѿ����ڸ����ݣ�����з��ظ�VO
	 * 
	 * @param vos
	 *            ��ʽ��������ѡ�еľۺ�VO
	 * @return
	 * @throws BusinessException
	 */
	private MyBillVO queryLocaData(AggregatedValueObject[] vos)
			throws BusinessException {
		// �������ⵥ����VO
		GeneralBillHeaderVO firstVO = (GeneralBillHeaderVO) vos[0]
				.getParentVO();
		if (null != firstVO && null != firstVO.getCgeneralhid()
				&& firstVO.getCgeneralhid().length() > 0) {
			TbGeneralHVO tbGeneralHVO = new TbGeneralHVO();
			TbGeneralBVO[] tbGeneralBVOs = null;
			// TbOutgeneralHVO generalh = null;
			// TbOutgeneralBVO[] generalb = null;
			String strWhere = " dr = 0 and geh_cgeneralhid = '"
					+ firstVO.getCgeneralhid() + "'";
			IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			List tmpList = new ArrayList();
			// ����������ѯ���������Ƿ�������
			ArrayList hList = (ArrayList) iuap.retrieveByClause(
					TbGeneralHVO.class, strWhere);
			if (null != hList && hList.size() > 0) {
				tbGeneralHVO = (TbGeneralHVO) hList.get(0);
			}
			if (null != tbGeneralHVO) {
				// ���ݱ�������������ѯ�ӱ�����
				strWhere = " dr = 0 and geh_pk = '" + tbGeneralHVO.getGeh_pk()
						+ "'";
				ArrayList list = (ArrayList) iuap.retrieveByClause(
						TbGeneralBVO.class, strWhere);
				if (null != list && list.size() > 0) {
					// ��ǰ��¼�ı���Ա�ܹ���Ļ�Ʒ
					List invLisk = nc.ui.wds.w8000.CommonUnit
							.getInvbasdoc_Pk(ClientEnvironment.getInstance()
									.getUser().getPrimaryKey());
					// �����ӱ�����
					if (null != invLisk && invLisk.size() > 0) {
						for (int i = 0; i < invLisk.size(); i++) {
							for (int j = 0; j < list.size(); j++) {
								TbGeneralBVO outb = (TbGeneralBVO) list.get(j);
								if (invLisk.get(i).equals(
										outb.getGeb_cinvbasid())) {
									outb.setStatus(VOStatus.UPDATED);
									tmpList.add(outb);
								}
							}
						}
						// myTempall = list;
					}
					if (tmpList.size() > 0) {
						tbGeneralBVOs = new TbGeneralBVO[tmpList.size()];
						tbGeneralBVOs = (TbGeneralBVO[]) tmpList
								.toArray(tbGeneralBVOs);
						MyBillVO mybillvo = new MyBillVO();
						mybillvo.setParentVO(tbGeneralHVO);
						mybillvo.setChildrenVO(tbGeneralBVOs);
						// ���������״̬�Ļ� �޸�
						// addoredit = false;
						return mybillvo;
					}
				}
			}
		}
		return null;
	}

	public void showZeroLikeNull(boolean bShowZeroLikeNull) {
		BillRendererVO vo = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getRendererVO();
		vo.setShowZeroLikeNull(bShowZeroLikeNull);
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
				.setShowFlags(vo);
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		// 1����Ա 2��Ϣ��
		String islast = "1";
		if ("1".equals(st_type)) {
			islast = "2";
		}
		if ("3".equals(st_type) && isedit) {
			islast = "2";
		}
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		//
		int bodyrownum = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getRowCount();
		if (bodyrownum == 0) {
			getBillUI().showErrorMessage("�ӱ���Ϊ�գ�");
			return;
		}
		// ��֤��Ʒ����
		for (int i = 0; i < bodyrownum; i++) {
			String geb_cinvbasidy = (String) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getValueAt(i, "invcode");
			if (null == geb_cinvbasidy || "".equals(geb_cinvbasidy)) {
				getBillUI().showErrorMessage("��Ʒ����Ϊ��!");
				return;
			}
			// ��֤���κ��Ƿ�Ϊ��
			String geb_vbatchcodey = (String) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getValueAt(i,
							"geb_vbatchcode");
			if (null == geb_vbatchcodey || "".equals(geb_vbatchcodey)) {
				getBillUI().showErrorMessage("���κŲ���Ϊ��!");
				return;
			}
			// ��֤Ӧ������
			UFDouble geb_bsnumy = (UFDouble) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getValueAt(i,
							"geb_bsnum");
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
		// �жϵ�������Ƿ����
		int rowNum = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getRowCount();
		for (int i = 0; i < rowNum; i++) {
			double geb_bsnum = 0;
			double geb_snum = 0;
			double geb_banum = 0;
			double geb_anum = 0;
			// Ӧ�븨����
			if (null != getBillCardPanelWrapper().getBillCardPanel()
					.getBillModel().getValueAt(i, "geb_bsnum")) {
				geb_bsnum = Double.parseDouble(getBillCardPanelWrapper()
						.getBillCardPanel().getBillModel().getValueAt(i,
								"geb_bsnum").toString());
			}
			// Ӧ������
			if (null != getBillCardPanelWrapper().getBillCardPanel()
					.getBillModel().getValueAt(i, "geb_snum")) {
				geb_snum = Double.parseDouble(getBillCardPanelWrapper()
						.getBillCardPanel().getBillModel().getValueAt(i,
								"geb_snum").toString());
			}
			// ʵ�븨����
			if (null != getBillCardPanelWrapper().getBillCardPanel()
					.getBillModel().getValueAt(i, "geb_banum")) {
				geb_banum = Double.parseDouble(getBillCardPanelWrapper()
						.getBillCardPanel().getBillModel().getValueAt(i,
								"geb_banum").toString());
			}
			// ʵ������
			if (null != getBillCardPanelWrapper().getBillCardPanel()
					.getBillModel().getValueAt(i, "geb_anum")) {
				geb_anum = Double.parseDouble(getBillCardPanelWrapper()
						.getBillCardPanel().getBillModel().getValueAt(i,
								"geb_anum").toString());
			}
			if (geb_bsnum != geb_banum || geb_snum != geb_anum) {
				getBillUI().showErrorMessage("Ӧ��⡢ʵ����������ȣ����ܱ��棡");
				return;
			}
		}

		// �õ�ȫ������VO

		TbGeneralBVO[] tbGeneralBVOss = null;
		if (getBillManageUI().isListPanelSelected()) {
			if (getBufferData().getVOBufferSize() != 0) {
				tbGeneralBVOss = (TbGeneralBVO[]) getBillManageUI()
						.getBillListWrapper().getVOFromUI().getChildrenVO();
			}

		} else {
			if (getBufferData().getVOBufferSize() != 0) {
				tbGeneralBVOss = (TbGeneralBVO[]) getBillCardPanelWrapper()
						.getBillVOFromUI().getChildrenVO();
			}

		}

		// �жϱ����Ƿ�������
		if (null == tbGeneralBVOss || tbGeneralBVOss.length == 0) {
			getBillUI().showErrorMessage("������û�����ݣ����ܽ��в�������Ȩ������");
			return;
		}
		if (null != st_type && !"".equals(st_type)) {
			if ("1".equals(st_type)) {
				// super.onBoSave();
				if (null != st_type && st_type.equals("1")
						&& getBufferData().getVOBufferSize() > 0) {

					AggregatedValueObject billvo = getBillUI().getVOFromUI();
					TbGeneralHVO generalhvo = null;
					if (null != billvo.getParentVO()) {
						generalhvo = (TbGeneralHVO) billvo.getParentVO();
					}

					//

					// ǩ�ֺ�
					if (null != generalhvo.getPwb_fbillflag()
							&& generalhvo.getPwb_fbillflag() == 3) {
						getButtonManager().getButton(
								nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
								.setEnabled(false);
						getButtonManager().getButton(
								nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
								.setEnabled(true);
						getBillUI().updateButtonUI();
					} else { // ǩ��ǰ
						getButtonManager().getButton(
								nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
								.setEnabled(true);
						getButtonManager().getButton(
								nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
								.setEnabled(false);
						getBillUI().updateButtonUI();
					}
				}
				// return;
			}
			if ("3".equals(st_type) && isedit) {
				// super.onBoSave();
				if (null != st_type && st_type.equals("3")
						&& getBufferData().getVOBufferSize() > 0) {

					AggregatedValueObject billvo = getBillUI().getVOFromUI();
					TbGeneralHVO generalhvo = null;
					if (null != billvo.getParentVO()) {
						generalhvo = (TbGeneralHVO) billvo.getParentVO();
					}

					//

					// ǩ�ֺ�
					if (null != generalhvo.getPwb_fbillflag()
							&& generalhvo.getPwb_fbillflag() == 3) {
						getButtonManager().getButton(
								nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
								.setEnabled(false);
						getButtonManager().getButton(
								nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
								.setEnabled(true);
						getBillUI().updateButtonUI();
					} else { // ǩ��ǰ
						getButtonManager().getButton(
								nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
								.setEnabled(true);
						getButtonManager().getButton(
								nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
								.setEnabled(false);
						getBillUI().updateButtonUI();
					}
				}
				// return;
			}
		}
		// ��ⵥ����
		getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("geh_billtype").setValue(new Integer(0));
		AggregatedValueObject billVO = getBillUI().getVOFromUI();

		billVO.getChildrenVO()[0].setStatus(1);
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);

		//
		TbGeneralBVO[] tbGeneralBVOas = (TbGeneralBVO[]) billVO.getChildrenVO();
		// ��֤�����Ƿ�������
		if (null == tbGeneralBVOas || tbGeneralBVOas.length < 0) {
			myClientUI.showErrorMessage("����ʧ��,����Ȩ����");
			return;
		}
		// ѭ����֤�Ƿ���û�����Ĳ�Ʒ
		for (int i = 0; i < tbGeneralBVOas.length; i++) {
			TbGeneralBVO genb = tbGeneralBVOas[i];
			if (null != genb.getGeb_banum() && !"".equals(genb.getGeb_banum())) {
				if (null != genb.getGeb_bsnum()
						&& !"".equals(genb.getGeb_bsnum())) {
					if (genb.getGeb_banum().toDouble().doubleValue() != genb
							.getGeb_bsnum().toDouble().doubleValue()) {
						getBillUI().showErrorMessage("���в�Ʒû��ָ�����,���ܱ�����⣡");
						// if (result != 1)
						return;
						// else
						// break;
					}
				}
			}
		}

		// ���ݱ���Ա��Ʒ��������޸��Լ���Ʒ�ӱ�
		List invLisk = nc.ui.wds.w8000.CommonUnit
				.getInvbasdoc_Pk(ClientEnvironment.getInstance().getUser()
						.getPrimaryKey());
		TbGeneralHVO tmpgeneralh = null;
		TbGeneralHVO generalh = (TbGeneralHVO) billVO.getParentVO();
		// ������Դ���ݺŲ�ѯ�Ƿ�����������
		String strWhere = " geh_vbillcode='" + generalh.getGeh_vbillcode()
				+ "' and geh_cgeneralhid='" + generalh.getGeh_cgeneralhid()
				+ "' and dr=0";
		ArrayList tmpList = (ArrayList) query.retrieveByClause(
				TbGeneralHVO.class, strWhere);
		if (null != tmpList && tmpList.size() > 0) {
			tmpgeneralh = (TbGeneralHVO) tmpList.get(0);
		}
		// boolean myisadd=true;
		// �õ����й������¼
		if (null != tmpgeneralh) {
			// �ѱ�ͷ�滻
			generalh = tmpgeneralh;
			String gebbvosql = " dr=0 and geh_pk ='" + generalh.getGeh_pk()
					+ "' and geb_cinvbasid in ('";
			for (int k = 0; k < invLisk.size(); k++) {
				if (null != invLisk && invLisk.size() > 0
						&& null != invLisk.get(k) && !"".equals(invLisk.get(k))) {
					gebbvosql += invLisk.get(k) + "','";
				}
			}
			gebbvosql += "') ";
			ArrayList gebbvos = (ArrayList) query.retrieveByClause(
					TbGeneralBVO.class, gebbvosql);
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
			generalh.setCopetadate(new UFDate(new Date()));
			generalh.setGeh_dbilldate(new UFDate(new Date()));
			// ���õ��ݺ�
			generalh.setGeh_billcode(CommonUnit.getBillCode("4E",
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
		// ����޸�ʱ��
		generalh.setClastmodetime(myClientUI._getServerTime().toString());
		// �����޸���
		generalh.setClastmodiid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey());
		// ����״̬
		generalh.setPwb_fbillflag(2);
		// ���ۺ�VO�б�ͷ��ֵ
		billVO.setParentVO(generalh);
		// ���帳ֵ
		billVO.setChildrenVO(tbGeneralBVOas);
		// �����������

		// �����Ƿ�ر�
		TbGeneralBVO[] tbGenBVOs = (TbGeneralBVO[]) billVO.getChildrenVO();
		for (int i = 0; i < tbGenBVOs.length; i++) {
			if (null != tbGenBVOs[i].getGeb_banum()
					&& null != tbGenBVOs[i].getGeb_anum()) {
				if (tbGenBVOs[i].getGeb_bsnum().toDouble().doubleValue() == tbGenBVOs[i]
						.getGeb_banum().toDouble().doubleValue()
						&& tbGenBVOs[i].getGeb_anum().toDouble().doubleValue() == tbGenBVOs[i]
								.getGeb_snum().toDouble().doubleValue()) {
					((TbGeneralBVO[]) billVO.getChildrenVO())[i]
							.setGeb_isclose(new UFBoolean("Y"));
				} else {
					((TbGeneralBVO[]) billVO.getChildrenVO())[i]
							.setGeb_isclose(new UFBoolean("N"));
				}
			} else {
				((TbGeneralBVO[]) billVO.getChildrenVO())[i]
						.setGeb_isclose(new UFBoolean("N"));
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
			// if (getBillUI().isSaveAndCommitTogether())
			// billVO = getBusinessAction().saveAndCommit(billVO,
			// getUIController().getBillType(), _getDate().toString(),
			// getBillUI().getUserObject(), checkVO);
			// else
			// write to database
			// ���Ҫ��д�ļ���VO
			GeneralBillVO voTempBill = getVoTempBill();
			// ���淽��
			ArrayList params = new ArrayList();

			params.add(getBillUI().getUserObject());
			// ��д����
			params.add(voTempBill);
			// ��������
			TbGeneralBVO[] tbGeneralBVOs = (TbGeneralBVO[]) getBillUI()
					.getVOFromUI().getChildrenVO();
			// ͨ�����ⵥ�ӱ������õ��������
			StringBuffer tgbbsql = new StringBuffer(" pwb_pk in ('");
			if (null != tbGeneralBVOs && tbGeneralBVOs.length > 0) {
				for (int i = 0; i < tbGeneralBVOs.length; i++) {
					if (null != tbGeneralBVOs[i].getGeb_cgeneralbid()
							&& !""
									.equals(tbGeneralBVOs[i]
											.getGeb_cgeneralbid())) {
						tgbbsql.append(tbGeneralBVOs[i].getGeb_cgeneralbid());
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
					if (null != tbbbvo.getGebb_num()) {
						tbWarehousestockVO[i].setWhs_stockpieces(tbbbvo
								.getGebb_num());
						tbWarehousestockVO[i]
								.setWhs_oanum(tbbbvo.getGebb_num());

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
					tbWarehousestockVO[i].setWhs_type(0);
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
					// ��Դ���ݱ��������� ���������
					tbWarehousestockVO[i].setPk_bodysource(tbbbvo.getGebb_pk());
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
			// ((TbGeneralHVO) billVO.getParentVO()).setTmaketime(getBillUI()
			// ._getServerTime().toString());
			// }
			//
			params.add(islast);
			if ("3".equals(st_type) && isedit) {
				for (int i = 0; i < billVO.getChildrenVO().length; i++) {
					billVO.getChildrenVO()[i].setStatus(VOStatus.UPDATED);
				}
			} else if ("3".equals(st_type) && (!isedit)) {
				for (int i = 0; i < billVO.getChildrenVO().length; i++) {
					billVO.getChildrenVO()[i].setStatus(VOStatus.NEW);
				}
			}
			if (getBillUI().isSaveAndCommitTogether())
				billVO = getBusinessAction().saveAndCommit(billVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), checkVO);
			else

				// write to database
				// if (isControl1) {// ��Ϣ�Ʊ��棬ֻ���浱ǰ����
				// // billVO.getParentVO().setStatus(1);
				// billVO = getBusinessAction().save(billVO,
				// getUIController().getBillType(), _getDate().toString(),
				// getBillUI().getUserObject(), checkVO);
				// } else {// ����Ա���棬��Ҫ������Ϣ�ĸĶ�
				billVO = getBusinessAction().save(billVO,
						getUIController().getBillType(), _getDate().toString(),
						params, checkVO);
			// }

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
				nc.ui.wds.w8004040210.ssButtun.ISsButtun.Td).setEnabled(false);
		getButtonManager().getButton(
				nc.ui.wds.w8004040210.ssButtun.ISsButtun.Ai).setEnabled(false);
		if (!isControl1) {
			getButtonManager().getButton(
					nc.ui.wds.w8004040210.ssButtun.ISsButtun.SsAdd).setEnabled(
					true);
			getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
		}
		if (null != st_type && st_type.equals("1")
				&& getBufferData().getVOBufferSize() > 0) {

			AggregatedValueObject billvo = getBillUI().getVOFromUI();
			TbGeneralHVO generalhvo = null;
			if (null != billvo.getParentVO()) {
				generalhvo = (TbGeneralHVO) billvo.getParentVO();
			}

			//

			// ǩ�ֺ�
			if (null != generalhvo.getPwb_fbillflag()
					&& generalhvo.getPwb_fbillflag() == 3) {
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
						.setEnabled(false);
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
						.setEnabled(true);
				getBillUI().updateButtonUI();
			} else { // ǩ��ǰ
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
						.setEnabled(true);
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
						.setEnabled(false);
				getBillUI().updateButtonUI();
			}
		}
		if (null != st_type && st_type.equals("3")
				&& getBufferData().getVOBufferSize() > 0) {

			AggregatedValueObject billvo = getBillUI().getVOFromUI();
			TbGeneralHVO generalhvo = null;
			if (null != billvo.getParentVO()) {
				generalhvo = (TbGeneralHVO) billvo.getParentVO();
			}

			//

			// ǩ�ֺ�
			if (null != generalhvo.getPwb_fbillflag()
					&& generalhvo.getPwb_fbillflag() == 3) {
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
						.setEnabled(false);
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
						.setEnabled(true);
				getBillUI().updateButtonUI();
			} else { // ǩ��ǰ
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
						.setEnabled(true);
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
						.setEnabled(false);
				getBillUI().updateButtonUI();
			}
		}
		if (null != st_type && st_type.equals("3")) {
			getButtonManager().getButton(
					nc.ui.wds.w8004040210.ssButtun.ISsButtun.SsAdd).setEnabled(
					true);
			getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
		}
		getBillUI().updateButtonUI();
		isedit = true;
		showZeroLikeNull(false);
	}

	@Override
	protected void onBoLineAdd() throws Exception {
		// TODO Auto-generated method stub
		super.onBoLineAdd();
	}

	/**
	 * ��û�д����VO�ķ���
	 * 
	 * @return
	 * @throws Exception
	 */
	protected GeneralBillVO getVoTempBill() throws Exception {
		GeneralBillVO voTempBill = new GeneralBillVO();
		TbGeneralHVO tbGeneralHVOss = null;
		TbGeneralBVO[] tbGeneralBVOss = null;
		if (getBillManageUI().isListPanelSelected()) {
			if (getBufferData().getVOBufferSize() != 0) {
				tbGeneralHVOss = (TbGeneralHVO) getBillManageUI()
						.getBillListWrapper().getVOFromUI().getParentVO();
				tbGeneralBVOss = (TbGeneralBVO[]) getBillManageUI()
						.getBillListWrapper().getVOFromUI().getChildrenVO();
			}
		} else {
			if (getBufferData().getVOBufferSize() != 0) {
				tbGeneralHVOss = (TbGeneralHVO) getBillCardPanelWrapper()
						.getBillVOFromUI().getParentVO();
				tbGeneralBVOss = (TbGeneralBVO[]) getBillCardPanelWrapper()
						.getBillVOFromUI().getChildrenVO();
			}
		}

		if (null == tbGeneralHVOss) {
			return null;
		}
		// ��õ������ⵥ��ͷ����
		String h_cgeneralhid = tbGeneralHVOss.getGeh_cgeneralhid();

		// ��õ������ⵥ��ͷVO
		StringBuffer hsql = new StringBuffer(
				"select fallocflag,PK_CALBODY,PK_CORP,CWAREHOUSEID"
						+ ",CPROVIDERID,FALLOCFLAG,PK_CUBASDOC,vbillcode"
						+ ",cgeneralhid "
						+ " from ic_general_h where cgeneralhid='");
		hsql.append(h_cgeneralhid);
		hsql.append("' and dr=0");
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());

		// �������ⵥ��ͷVO����
		ArrayList firstVOs = (ArrayList) query.executeQuery(hsql.toString(),
				new ArrayListProcessor());
		if (null != firstVOs && firstVOs.size() != 0) {

			Object[] firstVO = (Object[]) firstVOs.get(0);
			// Ҫ��д�ĵ�����ⵥ��ͷVO
			GeneralBillHeaderVO gbillhVO = new GeneralBillHeaderVO();

			// ��浥�����ͱ���
			gbillhVO.setCbilltypecode("4E");
			// �������ͱ�־
			if (null != firstVO[0]) {
				gbillhVO.setFallocflag(Integer.parseInt(firstVO[0].toString()));
			}
			// ҵ��ԱID
			if (null != tbGeneralHVOss.getGeh_cbizid()) {
				gbillhVO.setCbizid(tbGeneralHVOss.getGeh_cbizid());
			}
			// �շ�����ID
			if (null != tbGeneralHVOss.getGeh_cdispatcherid()) {
				gbillhVO
						.setCdispatcherid(tbGeneralHVOss.getGeh_cdispatcherid());
			}
			// ����ID
			if (null != tbGeneralHVOss.getGeh_cdptid()) {
				gbillhVO.setCdptid(tbGeneralHVOss.getGeh_cdptid());
			}
			// ����޸���
			gbillhVO.setAttributeValue("clastmodiid", ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
			// �Ƶ���
			gbillhVO.setCoperatorid(ClientEnvironment.getInstance().getUser()
					.getPrimaryKey());
			gbillhVO.setAttributeValue("coperatoridnow", ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
			// �Է������֯PK
			if (null != firstVO[1]) {
				gbillhVO.setAttributeValue("cothercalbodyid", firstVO[1]
						.toString());
			}
			// �Է���˾ID
			if (null != firstVO[2]) {
				gbillhVO.setAttributeValue("cothercorpid", firstVO[2]
						.toString());
			}
			// �����ֿ�ID
			if (null != firstVO[3]) {
				gbillhVO.setAttributeValue("cotherwhid", firstVO[3].toString());
			}
			// ���������֯ID
			if (null != firstVO[1]) {
				gbillhVO.setAttributeValue("cothercalbodyid", firstVO[1]
						.toString());
			}
			// ������˾ID
			if (null != firstVO[2]) {
				gbillhVO.setAttributeValue("coutcorpid", firstVO[2].toString());

			}

			// ��Ӧ��ID
			if (null != firstVO[4]) {
				gbillhVO.setCproviderid(firstVO[4].toString());
			}
			// �ⷿǩ����
			// gbillhVO.setCregister(ClientEnvironment.getInstance().getUser()
			// .getPrimaryKey());
			// �ֿ�ID
			if (null != tbGeneralHVOss.getGeh_cwarehouseid()) {
				gbillhVO.setCwarehouseid(tbGeneralHVOss.getGeh_cwarehouseid());
			}

			// �ⷿǩ������
			// gbillhVO.setDaccountdate(new UFDate(new Date()));
			// ��������
			gbillhVO.setDbilldate(tbGeneralHVOss.getGeh_dbilldate());
			// �������ͱ�־
			if (null != firstVO[5]) {
				gbillhVO.setFallocflag(Integer.parseInt(firstVO[5].toString()));

			}
			// ����״̬
			gbillhVO.setFbillflag(2);
			// �˻���־
			gbillhVO.setFreplenishflag(new UFBoolean(false));
			// �����֯PK
			if (null != tbGeneralHVOss.getGeh_calbody()) {
				gbillhVO.setPk_calbody(tbGeneralHVOss.getGeh_calbody());
			}
			// ��˾ID
			if (null != tbGeneralHVOss.getGeh_corp()) {
				gbillhVO.setPk_corp(tbGeneralHVOss.getGeh_corp());
			} else {
				gbillhVO.setPk_corp(ClientEnvironment.getInstance().getUser()
						.getCorpId());
			}
			// ��Ӧ�̻�������ID
			if (null != firstVO[6]) {
				// gbillhVO.setPk_cubasdoc(firstVO[6].toString());
			}
			// �ⷿǩ��ʱ��
			// gbillhVO.setAttributeValue("taccounttime", new UFTime(
			// new Date()));
			// ����޸�ʱ��
			gbillhVO.setAttributeValue("tlastmoditime", new UFTime(new Date()));
			// �Ƶ�ʱ��
			gbillhVO.setAttributeValue("tmaketime", tbGeneralHVOss
					.getTmaketime());
			// ���ݺ�
			if (null != tbGeneralHVOss.getGeh_billcode()) {
				gbillhVO.setVbillcode(tbGeneralHVOss.getGeh_billcode());
			}

			// ���û�д��ͷVO

			voTempBill.setParentVO(gbillhVO);

			// ��������
			// int b_rows = getBillCardPanelWrapper().getBillCardPanel()
			// .getBillModel().getRowCount();
			// ����VO����
			GeneralBillItemVO[] gbillbVOs = new GeneralBillItemVO[tbGeneralBVOss.length];
			// ���ñ���VO
			String b_cgeneralbid = "";
			for (int i = 0; i < tbGeneralBVOss.length; i++) {
				// ����VO
				gbillbVOs[i] = new GeneralBillItemVO();
				GeneralBillItemVO gbillbVO = gbillbVOs[i];
				// ��ǰ���ݱ���
				TbGeneralBVO tbGeneralBVO = tbGeneralBVOss[i];
				// ��õ������ⵥ��������
				b_cgeneralbid = tbGeneralBVO.getGeb_cgeneralbid();
				// ��õ������ⵥ����VO
				StringBuffer bsql = new StringBuffer(
						"select BTOINZGFLAG,CASTUNITID,CFIRSTBILLBID,CFIRSTBILLHID"
								+ ",CFIRSTTYPE,CINVBASID,CINVENTORYID,CQUOTEUNITID,"
								+ " CGENERALBID,CGENERALHID,FLARGESS,HSL,"
								+ " ISOK,NQUOTEUNITNUM,NQUOTEUNITRATE,VBATCHCODE,"
								+ " VFIRSTBILLCODE,crowno,VUSERDEF5 "
								+ " from ic_general_b where cgeneralbid='");
				bsql.append(b_cgeneralbid);
				bsql.append("' and dr=0");
				// �������ⵥ����VO����
				ArrayList childVOs = (ArrayList) query.executeQuery(bsql
						.toString(), new ArrayListProcessor());
				// �������ⵥ����VO
				Object[] childVO = (Object[]) childVOs.get(0);

				// ��������λID
				if (null != childVO[1]) {
					gbillbVO.setCastunitid(childVO[1].toString());
				}
				// Դͷ���ݱ���ID
				if (null != childVO[2]) {
					gbillbVO.setCfirstbillbid(childVO[2].toString());
				}
				// Դͷ���ݱ�ͷID
				if (null != childVO[3]) {
					gbillbVO.setCfirstbillhid(childVO[3].toString());
				}
				// Դͷ��������
				if (null != childVO[4]) {
					gbillbVO.setCfirsttype(childVO[4].toString());
				}
				// �������ID
				if (null != childVO[5]) {
					gbillbVO.setCinvbasid(childVO[5].toString());
				}
				// ͨ����˾�ʹ����������������ѯ�������������
				String pk_invmandoc = "select pk_invmandoc from bd_invmandoc ";
				if (null != childVO[5] && null != tbGeneralHVOss.getGeh_corp()) {
					pk_invmandoc += " where pk_invbasdoc='"
							+ childVO[5].toString().trim() + "' and pk_corp='"
							+ tbGeneralHVOss.getGeh_corp().toString().trim()
							+ "' and dr=0 ";
				} else {
					pk_invmandoc += " where 1=2 ";
				}
				ArrayList invmandocvos = (ArrayList) query.executeQuery(
						pk_invmandoc.toString(), new ArrayListProcessor());
				Object[] invmandocvo = (Object[]) invmandocvos.get(0);

				// ���ID
				if (null != invmandocvo[0]) {
					// gbillbVO.setCinventoryid(childVO[6].toString());
					gbillbVO.setCinventoryid(invmandocvo[0].toString().trim());
				}
				// ������˾
				// // ���ۼ�����λID
				// if (null != childVO[7]) {
				// gbillbVO.setAttributeValue("CQUOTEUNITID", childVO[7]
				// .toString());
				// }
				// �к�
				gbillbVO.setCrowno(i + 1 + "0");
				// ��Դ���ݱ������к�
				if (null != childVO[8]) {
					gbillbVO.setCsourcebillbid(childVO[8].toString());
				}
				// ��Դ���ݱ�ͷ���к�
				if (null != childVO[9]) {
					gbillbVO.setCsourcebillhid(childVO[9].toString());
				}
				// ��Դ��������
				gbillbVO.setCsourcetype("4Y");
				// ҵ������
				gbillbVO.setDbizdate(tbGeneralHVOss.getGeh_dbilldate());

				// �Ƿ���Ʒ
				if (null != childVO[10]) {
					gbillbVO.setFlargess(new UFBoolean(childVO[10].toString()));
				}
				// ������
				if (null != childVO[11]) {
					gbillbVO.setHsl(new UFDouble(Double.parseDouble(childVO[11]
							.toString())));
				}

				// ʵ�븨����
				if (null != tbGeneralBVO.getGeb_banum()) {
					gbillbVO.setNinassistnum(new UFDouble(tbGeneralBVO
							.getGeb_banum().toString()));
				}
				// ʵ������
				if (null != tbGeneralBVO.getGeb_anum()) {
					gbillbVO.setNinnum(new UFDouble(tbGeneralBVO.getGeb_anum()
							.toString()));
				}
				// ���
				if (null != tbGeneralBVO.getGeb_nmny()) {
					gbillbVO.setNmny(new UFDouble(tbGeneralBVO.getGeb_nmny()
							.toString()));
				}
				// Ӧ�븨����
				if (null != tbGeneralBVO.getGeb_bsnum()) {
					gbillbVO.setNneedinassistnum(new UFDouble(tbGeneralBVO
							.getGeb_bsnum().toString()));
				}
				// ����
				if (null != tbGeneralBVO.getGeb_nprice()) {
					gbillbVO.setNprice(new UFDouble(tbGeneralBVO
							.getGeb_nprice().toString()));
				}

				// Ӧ������
				if (null != tbGeneralBVO.getGeb_snum()) {
					gbillbVO.setNshouldinnum(new UFDouble(tbGeneralBVO
							.getGeb_snum().toString()));
				}
				// ��˾
				if (null != tbGeneralHVOss.getGeh_corp()) {
					gbillbVO.setAttributeValue("PK_CORP", tbGeneralHVOss
							.getGeh_corp().toString());
				}
				// ���κ�
				if (null != childVO[15]) {
					gbillbVO.setVbatchcode(childVO[15].toString());
				}
				// Դͷ���ݺ�
				if (null != childVO[16]) {
					gbillbVO.setVfirstbillcode(childVO[16].toString());
				}
				// ��Դ���ݺ�
				if (null != firstVO[7]) {
					gbillbVO.setVsourcebillcode(firstVO[7].toString());
				}
				// ��Դ�����к�
				if (null != childVO[17]) {
					gbillbVO.setVsourcerowno(childVO[17].toString());
				}

				// ��������
				if (null != tbGeneralBVO.getGeb_proddate()) {
					gbillbVO.setScrq(new UFDate(tbGeneralBVO.getGeb_proddate()
							.toString()));
				}
				// ʧЧ����
				if (null != tbGeneralBVO.getGeb_dvalidate()) {
					gbillbVO.setDvalidate(new UFDate(tbGeneralBVO
							.getGeb_dvalidate().toString()));
				}
				// ƾ֤����
				LocatorVO locatorvo = new LocatorVO();
				if (null != tbGeneralBVO.getGeb_space()) {
					locatorvo.setCspaceid(tbGeneralBVO.getGeb_space()
							.toString());
				} else {
					// getBillUI().showErrorMessage("û������λ��������⣡");
					return null;
				}
				// locatorvo.setCspaceid("1021A91000000004YZ0Q");
				locatorvo.setPk_corp(gbillhVO.getPk_corp());
				// ʵ��
				locatorvo.setNinspacenum(gbillbVO.getNinnum());
				// ʵ�븨
				locatorvo.setNinspaceassistnum(gbillbVO.getNneedinassistnum());
				LocatorVO[] locatorvos = new LocatorVO[1];
				locatorvos[0] = locatorvo;
				gbillbVO.setLocator(locatorvos);

			}

			voTempBill.setChildrenVO(gbillbVOs);

		}
		return voTempBill;
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ

		strWhere.append(" and geh_billtype =0");

		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		List invLisk = nc.ui.wds.w8000.CommonUnit
				.getInvbasdoc_Pk(ClientEnvironment.getInstance().getUser()
						.getPrimaryKey());

		if ("0".equals(st_type)) {

			String gebbvosql = " dr=0 " + " and geb_cinvbasid in ('";
			for (int k = 0; k < invLisk.size(); k++) {
				if (null != invLisk && invLisk.size() > 0
						&& null != invLisk.get(k) && !"".equals(invLisk.get(k))) {
					gebbvosql += invLisk.get(k) + "','";
				}
			}
			gebbvosql += "') ";
			ArrayList gebbvos = (ArrayList) query.retrieveByClause(
					TbGeneralBVO.class, gebbvosql);
			if (null != gebbvos && gebbvos.size() > 0) {
				strWhere.append(" and geh_pk in ('");
				for (int i = 0; i < gebbvos.size(); i++) {
					strWhere
							.append(((TbGeneralBVO) gebbvos.get(i)).getGeh_pk());
					strWhere.append("','");
				}
				strWhere.append("')");
			} else {
				strWhere.append(" and 1=2 ");
			}

		}

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
		if (!isControl1) {
			getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
			getBillUI().updateButtonUI();
		}

		// if (null != st_type && st_type.equals("3")
		// && getBufferData().getVOBufferSize() > 0) {
		// getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
		// getBillUI().updateButtonUI();
		// }
		super.onBoReturn();
		showZeroLikeNull(false);
	}

	// ��õ��ݺ�
	public String getBillCode(String billtype, String pkcorp, String gcbm,
			String operator) throws BusinessException {
		String scddh = null;
		try {
			BillCodeObjValueVO vo = new BillCodeObjValueVO();
			String[] names = { "�����֯", "����Ա" };
			String[] values = new String[] { gcbm, operator };
			vo.setAttributeValue(names, values);
			scddh = getBillCode(billtype, pkcorp, vo);
		} catch (Exception e) {
			GenMethod.throwBusiException(e);
		}
		return scddh;
	}

	private String getBillCode(String billtype, String pkcorp,
			nc.vo.pub.billcodemanage.BillCodeObjValueVO billVO)
			throws BusinessException {
		String djh = null;
		try {
			IBillcodeRuleService bo = (IBillcodeRuleService) NCLocator
					.getInstance().lookup(IBillcodeRuleService.class.getName());
			djh = bo.getBillCode_RequiresNew(billtype, pkcorp, null, billVO);

		} catch (Exception e) {
			GenMethod.throwBusiException(e);
		}
		return djh;
	}

	@Override
	protected void onBoReturn() throws Exception {
		// TODO Auto-generated method stub

		super.onBoReturn();
		getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
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

	/**
	 * ������ѯ�Ի������û�ѯ�ʲ�ѯ������ ����û��ڶԻ������ˡ�ȷ��������ô����true,���򷵻�false
	 * ��ѯ����ͨ�������StringBuffer���ظ�������
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

		strWhere = "(" + strWhere + ") and (isnull(dr,0)=0)";

		// if (getHeadCondition() != null)
		// strWhere = strWhere + " and " + getHeadCondition();
		// ��������ֱ�Ӱ����ƴ�õĴ��ŵ�StringBuffer�ж���ȥ�Ż�ƴ���Ĺ���
		sqlWhereBuf.append(strWhere);
		return true;
	}

	@Override
	/**
	 * ȡ��ǩ��
	 */
	protected void onQxqz() throws Exception {
		// TODO Auto-generated method stub
		// Ҫ��д�ľۺ�VO
		GeneralBillVO voTempBill = getVoTempBillCancel();

		if (null == voTempBill) {
			getBillUI().showErrorMessage("û�е��ݻ򵥾�û���ƶ���λ��������⣡");
			return;
		}

		TbGeneralHVO tbGeneralHVOss = null;
		if (getBillManageUI().isListPanelSelected()) {
			if (getBufferData().getVOBufferSize() != 0) {
				tbGeneralHVOss = (TbGeneralHVO) getBillManageUI()
						.getBillListWrapper().getVOFromUI().getParentVO();

			}
		} else {
			if (getBufferData().getVOBufferSize() != 0) {
				tbGeneralHVOss = (TbGeneralHVO) getBillCardPanelWrapper()
						.getBillVOFromUI().getParentVO();

			}
		}
		// �ⷿǩ����
		tbGeneralHVOss.setGeh_storname(null);
		// ǩ��ʱ��
		tbGeneralHVOss.setTaccounttime(null);

		// ����޸���
		tbGeneralHVOss.setClastmodiid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey());
		// ����޸�������
		tbGeneralHVOss.setClastmodedate(new UFDate(new Date()));
		// ����޸�ʱ��
		tbGeneralHVOss.setClastmodetime(myClientUI._getServerTime().toString());
		// ����״̬
		tbGeneralHVOss.setPwb_fbillflag(2);
		// Ҫ��д�ķ���
		// Object o = nc.ui.pub.pf.PfUtilClient.processAction(
		// "CANCELSIGN"/* ��д�ű����� */, "4E"/* �������� */, _getDate()
		// .toString(), voTempBill);
		Iw8004040210 iw = (Iw8004040210) NCLocator.getInstance().lookup(
				Iw8004040210.class.getName());
		iw.canceldelete8004040210("CANCELSIGN", "DELETE"/* ��д�ű����� */,
				"4E"/* �������� */, _getDate().toString(), voTempBill,
				tbGeneralHVOss);

		// Object o = nc.ui.pub.pf.PfUtilClient.processAction(
		// "DELETE"/* ��д�ű����� */, "4E"/* �������� */, _getDate().toString(),
		// voTempBill);
		getBillUI().showHintMessage("����ȡ��ǩ�ֳɹ�");
		getButtonManager().getButton(
				nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr).setEnabled(true);
		getButtonManager().getButton(
				nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
				.setEnabled(false);
		getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
		super.onBoRefresh();
		return;
	}

	private GeneralBillVO voTempBillall;

	@Override
	/**
	 * ǩ��ȷ��
	 */
	//
	protected void onQzqr() throws Exception {
		// TODO Auto-generated method stub
		// Ҫ��д�ľۺ�VO
		GeneralBillVO voTempBill = getVoTempBill();
		// LocatorVO locatorvo = new LocatorVO();

		if (null == voTempBill) {
			getBillUI().showErrorMessage("û�е��ݻ򵥾�û���ƶ���λ��������⣡");
			return;
		}
		TbGeneralHVO tbGeneralHVOss = null;
		if (getBillManageUI().isListPanelSelected()) {
			if (getBufferData().getVOBufferSize() != 0) {
				tbGeneralHVOss = (TbGeneralHVO) getBillManageUI()
						.getBillListWrapper().getVOFromUI().getParentVO();

			}
		} else {
			if (getBufferData().getVOBufferSize() != 0) {
				tbGeneralHVOss = (TbGeneralHVO) getBillCardPanelWrapper()
						.getBillVOFromUI().getParentVO();

			}
		}
		// �ⷿǩ����
		tbGeneralHVOss.setGeh_storname(ClientEnvironment.getInstance()
				.getUser().getPrimaryKey());
		// ǩ��ʱ��
		tbGeneralHVOss.setTaccounttime(myClientUI._getServerTime().toString());

		// ����޸���
		tbGeneralHVOss.setClastmodiid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey());
		// ����޸�������
		tbGeneralHVOss.setClastmodedate(new UFDate(new Date()));
		// ����޸�ʱ��
		tbGeneralHVOss.setClastmodetime(myClientUI._getServerTime().toString());
		// ����״̬
		tbGeneralHVOss.setPwb_fbillflag(3);
		// Ҫ��д�ķ���

		Iw8004040210 iw = (Iw8004040210) NCLocator.getInstance().lookup(
				Iw8004040210.class.getName());
		iw.pushsavesign8004040210("PUSHSAVESIGN"/* ��д�ű����� */, "4E"/* �������� */,
				_getDate().toString(), voTempBill, tbGeneralHVOss);
		// Object o = nc.ui.pub.pf.PfUtilClient.processAction(
		// "PUSHSAVESIGN"/* ��д�ű����� */, "4E"/* �������� */, _getDate()
		// .toString(), voTempBill);

		getBillUI().showHintMessage("ǩ�ֳɹ�");
		getButtonManager().getButton(
				nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
				.setEnabled(false);
		getButtonManager().getButton(
				nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz).setEnabled(true);
		getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
		super.onBoRefresh();
		return;
		//
		// // Ҫ��дƾ֤�ķ���
		// nc.ui.pub.pf.PfUtilClient.processAction("SIGN"/* ��д�ű����� */,
		// "4E"/* �������� */, _getDate().toString(), null);
		// getBillUI().showErrorMessage("112");

	}

	// ȡ��ǩ�ֻ�д
	protected GeneralBillVO getVoTempBillCancel() throws Exception {
		GeneralBillVO voTempBill = new GeneralBillVO();
		TbGeneralHVO tbGeneralHVOss = null;
		TbGeneralBVO[] tbGeneralBVOss = null;
		if (getBillManageUI().isListPanelSelected()) {
			if (getBufferData().getVOBufferSize() != 0) {
				tbGeneralHVOss = (TbGeneralHVO) getBillManageUI()
						.getBillListWrapper().getVOFromUI().getParentVO();
				tbGeneralBVOss = (TbGeneralBVO[]) getBillManageUI()
						.getBillListWrapper().getVOFromUI().getChildrenVO();
			}
		} else {
			if (getBufferData().getVOBufferSize() != 0) {
				tbGeneralHVOss = (TbGeneralHVO) getBillCardPanelWrapper()
						.getBillVOFromUI().getParentVO();
				tbGeneralBVOss = (TbGeneralBVO[]) getBillCardPanelWrapper()
						.getBillVOFromUI().getChildrenVO();
			}
		}
		if (null == tbGeneralHVOss) {
			return null;
		}
		// ��õ������ⵥ��ͷ����
		String h_cgeneralhid = tbGeneralHVOss.getGeh_billcode();

		// ��õ������ⵥ��ͷVO
		StringBuffer hsql = new StringBuffer(" vbillcode='");
		hsql.append(h_cgeneralhid);
		hsql.append("' and dr=0");
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());

		// �������ⵥ��ͷVO����
		ArrayList firstVOs = (ArrayList) query.retrieveByClause(
				IcGeneralHVO.class, hsql.toString());
		if (null != firstVOs && firstVOs.size() != 0) {

			IcGeneralHVO firstVO = (IcGeneralHVO) firstVOs.get(0);

			// Ҫ��д�ĵ�����ⵥ��ͷVO
			GeneralBillHeaderVO gbillhVO = new GeneralBillHeaderVO();

			// ��ͷ����
			gbillhVO.setCgeneralhid(firstVO.getCgeneralhid());
			// ��浥�����ͱ���
			gbillhVO.setCbilltypecode("4E");
			// �������ͱ�־
			gbillhVO.setFallocflag(firstVO.getFallocflag());

			// ҵ��ԱID
			gbillhVO.setCbizid(firstVO.getCbizid());

			// �շ�����ID
			gbillhVO.setCdispatcherid(firstVO.getCdispatcherid());

			// ����ID
			gbillhVO.setCdptid(firstVO.getCdptid());

			// ����޸���
			gbillhVO.setAttributeValue("clastmodiid", ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
			// �Ƶ���
			gbillhVO.setCoperatorid(firstVO.getCoperatorid());
			gbillhVO.setAttributeValue("coperatoridnow", ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
			// �Է������֯PK
			gbillhVO.setAttributeValue("cothercalbodyid", firstVO
					.getCothercalbodyid());
			// �Է���˾ID
			gbillhVO.setAttributeValue("cothercorpid", firstVO
					.getCothercorpid());
			// �����ֿ�ID
			gbillhVO.setAttributeValue("cotherwhid", firstVO.getCotherwhid());

			// ���������֯ID
			gbillhVO.setAttributeValue("cothercalbodyid", firstVO
					.getCothercalbodyid());
			// ������˾ID
			gbillhVO.setAttributeValue("coutcorpid", firstVO.getCoutcorpid());

			// ��Ӧ��ID
			gbillhVO.setCproviderid(firstVO.getCproviderid());
			// �ⷿǩ����
			gbillhVO.setCregister(firstVO.getCregister());
			// �ֿ�ID
			gbillhVO.setCwarehouseid(firstVO.getCwarehouseid());

			// �ⷿǩ������
			gbillhVO.setDaccountdate(firstVO.getDaccountdate());
			// ��������
			gbillhVO.setDbilldate(firstVO.getDbilldate());
			// �������ͱ�־
			gbillhVO.setFallocflag(firstVO.getFallocflag());
			// ����״̬
			gbillhVO.setFbillflag(2);
			// �˻���־
			gbillhVO.setFreplenishflag(firstVO.getFreplenishflag());
			// �����֯PK
			gbillhVO.setPk_calbody(firstVO.getPk_calbody());
			// ��˾ID
			gbillhVO.setPk_corp(firstVO.getPk_corp());
			// ��Ӧ�̻�������ID
			// if (null != firstVO[6]) {
			// // gbillhVO.setPk_cubasdoc(firstVO[6].toString());
			// }
			// �ⷿǩ��ʱ��
			gbillhVO.setAttributeValue("taccounttime", firstVO
					.getTaccounttime());
			// ����޸�ʱ��
			gbillhVO.setAttributeValue("tlastmoditime", new UFTime(myClientUI
					._getServerTime().toString()));
			// �Ƶ�ʱ��
			gbillhVO.setAttributeValue("tmaketime", firstVO.getTmaketime());
			// ���ݺ�
			gbillhVO.setVbillcode(firstVO.getVbillcode());
			// ���û�д��ͷVO

			voTempBill.setParentVO(gbillhVO);

			// ��������
			// int b_rows = getBillCardPanelWrapper().getBillCardPanel()
			// .getBillModel().getRowCount();
			// IcGeneralBVO
			// ��õ������ⵥ��ͷ����
			String h_cgeneralhid1 = firstVO.getCgeneralhid();

			// ��õ������ⵥ��ͷVO
			StringBuffer bsql = new StringBuffer(" cgeneralhid='");
			bsql.append(h_cgeneralhid1);
			bsql.append("' and dr=0");

			ArrayList icGeneralBVOs = (ArrayList) query.retrieveByClause(
					IcGeneralBVO.class, bsql.toString());
			// ����VO����
			GeneralBillItemVO[] gbillbVOs = new GeneralBillItemVO[icGeneralBVOs
					.size()];
			// ���ñ���VO
			String b_cgeneralbid = "";
			for (int i = 0; i < icGeneralBVOs.size(); i++) {
				// ����VO
				gbillbVOs[i] = new GeneralBillItemVO();
				GeneralBillItemVO gbillbVO = gbillbVOs[i];
				// ��ǰ���ݱ���
				IcGeneralBVO icGeneralBVO = (IcGeneralBVO) icGeneralBVOs.get(i);

				// ��ͷ����
				gbillbVO.setCgeneralhid(icGeneralBVO.getCgeneralhid());
				// ��������
				gbillbVO.setCgeneralbid(icGeneralBVO.getCgeneralbid());
				// ��������λID
				gbillbVO.setCastunitid(icGeneralBVO.getCastunitid());
				// Դͷ���ݱ���ID
				gbillbVO.setCfirstbillbid(icGeneralBVO.getCfirstbillbid());
				// Դͷ���ݱ�ͷID
				gbillbVO.setCfirstbillhid(icGeneralBVO.getCfirstbillhid());
				// Դͷ��������
				gbillbVO.setCfirsttype(icGeneralBVO.getCfirsttype());
				// �������ID
				gbillbVO.setCinvbasid(icGeneralBVO.getCinvbasid());
				// ���ID
				gbillbVO.setCinventoryid(icGeneralBVO.getCinventoryid());
				// ������˾
				// // ���ۼ�����λID
				// if (null != childVO[7]) {
				// gbillbVO.setAttributeValue("CQUOTEUNITID", childVO[7]
				// .toString());
				// }
				// �к�
				gbillbVO.setCrowno(icGeneralBVO.getCrowno());
				// ��Դ���ݱ������к�
				gbillbVO.setCsourcebillbid(icGeneralBVO.getCsourcebillbid());
				// ��Դ���ݱ�ͷ���к�
				gbillbVO.setCsourcebillhid(icGeneralBVO.getCsourcebillhid());
				// ��Դ��������
				gbillbVO.setCsourcetype("4Y");
				// ҵ������
				gbillbVO.setDbizdate(icGeneralBVO.getDbizdate());

				// �Ƿ���Ʒ
				gbillbVO.setFlargess(icGeneralBVO.getFlargess());
				// ������
				gbillbVO.setHsl(icGeneralBVO.getHsl());

				// ʵ�븨����
				gbillbVO.setNinassistnum(icGeneralBVO.getNinassistnum());
				// ʵ������
				gbillbVO.setNinnum(icGeneralBVO.getNinnum());
				// ���
				gbillbVO.setNmny(icGeneralBVO.getNmny());
				// Ӧ�븨����
				gbillbVO
						.setNneedinassistnum(icGeneralBVO.getNneedinassistnum());
				// ����
				gbillbVO.setNprice(icGeneralBVO.getNprice());

				// Ӧ������
				gbillbVO.setNshouldinnum(icGeneralBVO.getNshouldinnum());
				// ��˾
				gbillbVO
						.setAttributeValue("PK_CORP", icGeneralBVO.getPk_corp());
				// ���κ�
				gbillbVO.setVbatchcode(icGeneralBVO.getVbatchcode());
				// Դͷ���ݺ�
				gbillbVO.setVfirstbillcode(icGeneralBVO.getVfirstbillcode());
				// ��Դ���ݺ�
				gbillbVO.setVsourcebillcode(icGeneralBVO.getVsourcebillcode());
				// ��Դ�����к�
				gbillbVO.setVsourcerowno(icGeneralBVO.getVsourcerowno());

				// ��������
				// gbillbVO.setScrq(new UFDate(tbGeneralBVO.getGeb_proddate()
				// .toString()));
				// }
				// ʧЧ����
				gbillbVO.setDvalidate(icGeneralBVO.getDvalidate());
				// ƾ֤����
				// LocatorVO locatorvo = new LocatorVO();
				// if (null != tbGeneralBVO.getGeb_space()) {
				// locatorvo.setCspaceid(tbGeneralBVO.getGeb_space()
				// .toString());
				// } else {
				// // getBillUI().showErrorMessage("û������λ��������⣡");
				// return null;
				// }
				// // locatorvo.setCspaceid("1021A91000000004YZ0Q");
				// locatorvo.setPk_corp(gbillhVO.getPk_corp());
				// // ʵ��
				// locatorvo.setNinspacenum(gbillbVO.getNinnum());
				// // ʵ�븨
				// locatorvo.setNinspaceassistnum(gbillbVO.getNneedinassistnum());
				// LocatorVO[] locatorvos = new LocatorVO[1];
				// locatorvos[0] = locatorvo;
				// gbillbVO.setLocator(icGeneralBVO.getloc);

			}

			voTempBill.setChildrenVO(gbillbVOs);

		}
		return voTempBill;
	}

}
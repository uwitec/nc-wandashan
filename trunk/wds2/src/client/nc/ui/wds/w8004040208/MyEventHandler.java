package nc.ui.wds.w8004040208;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.wds.w8004040204.Iw8004040204;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
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
import nc.ui.wds.w8000.W8004040204Action;
import nc.ui.wds.w8004040204.TrayDisposeDetailDlg;
import nc.ui.wds.w8004040204.TrayDisposeDlg;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.dm.order.SendorderBVO;
import nc.vo.dm.order.SendorderVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.to.pub.ConstVO;
import nc.vo.wds.pub.WDSTools;
import nc.vo.wds.pub.WdsWlPubConsts;
import nc.vo.wds.w80021030.TbQycgjh2VO;
import nc.vo.wds.w80021030.TbQycgjhVO;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
import nc.vo.wds.w8004040208.MyBillVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;
import nc.vo.wds.w80060608.IcGeneralBVO;
import nc.vo.wds.w80060608.IcGeneralHVO;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * 
 * ��������
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	MyClientUI myClientUI = null;
	private String m_logUser = null;

	private Iw8004040204 iw04 = null;
	private Integer isControl = -1; // �Ƿ���Ȩ�޲�����ǰ���� 1Ϊ����ǩ�ֵ��û� 0 �޸Ĳ��� 2 ���Խ��г���
	// 3����Ȩ��

	// ���ݵ�ǰ��¼�߲�ѯ�����ֿ����ֿ����洢�Ĳ�Ʒ
	private List pkList = null;

	private FydnewDlg fydnewdlg = null; // ת���ѯ��������

	private CgqyDlg cgqydlg = null; // ���������вɹ�ȡ����ѯ��������

	boolean isAdd = false;

	int isTypes = 0; // ��¼�������� 0Ĭ��ֵ��1ת�⣬2ȡ���ɹ���3���Ƶ���

	// private List hiddenList = null;
	private boolean isStock = false; // �Ƿ�Ϊ�ܲ� true �ܲ� false �ֲ�
	private IUAPQueryBS iuap = null;

	private String pk_stock = ""; // ��ǰ��¼�߶�Ӧ�Ĳֿ�����
	String billType = null; // 0 ת�� 9�ɹ�ȡ�� 8���Ƶ���
	
	private Iw8004040204 getIwBO(){
		if(iw04 == null){
			iw04 = (Iw8004040204) NCLocator.getInstance().getInstance().lookup(
					Iw8004040204.class.getName());
		}
		return iw04;
	}
	
	private IUAPQueryBS getQueryBO(){
		if(iuap == null){
			iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
		}
		return iuap;
	}


	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;
		m_logUser = myClientUI._getOperator();
		// ����ʲô��ɫ��һ�μ�������ָ����ť���Զ�ȡ�������ò�����
		changeButton(false);
		try {
			// ��ȡ��ǰ��¼�߲�ѯ��Ա�󶨱� �������ĸ��ֿ⣬
			pk_stock = LoginInforHelper.getWhidByUser(m_logUser);
			int iType = LoginInforHelper.getITypeByUser(m_logUser);
			if (iType == 0) {
				isStock = WdsWlPubTool.isZc(LoginInforHelper.getCwhid(m_logUser));
				pkList = Arrays.asList(LoginInforHelper.getInvBasDocIDsByUserID(m_logUser));
				isControl = 2;
			} else if (iType==1) {
				isControl = 1;
				getButtonManager().getButton(ISsButtun.zj).setEnabled(false);
				myClientUI.updateButtons();
			} else if (iType==2) {
				isControl = 0;

			} else if (iType==3) {
				isControl = 3;
				isStock = WdsWlPubTool.isZc(LoginInforHelper.getCwhid(m_logUser));
				pkList = Arrays.asList(LoginInforHelper.getInvBasDocIDsByUserID(m_logUser));
			}
			if (isControl != 1) {
				getButtonManager().getButton(
						nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr)
						.setEnabled(false);
				getButtonManager().getButton(
						nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz)
						.setEnabled(false);
				myClientUI.updateButtons();
			}
		} catch (Exception e) {
			e.printStackTrace();
			myClientUI.showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
		}

	}

	private int lineNo = -1;

	@Override
	protected void onBoLinePaste() throws Exception {
		super.onBoLinePaste();
		if (lineNo != -1) {
			lineNo = lineNo + 10;
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
					lineNo + "",
					getBillCardPanelWrapper().getBillCardPanel().getBillTable()
							.getSelectedRow(), "crowno");
			// ��Դ��������
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
					"8",
					getBillCardPanelWrapper().getBillCardPanel().getBillTable()
							.getSelectedRow(), "csourcetype");
		}

	}

	// ������
	@Override
	protected void onBoLineIns() throws Exception {
		super.onBoLineIns();
		if (lineNo != -1) {
			lineNo = lineNo + 10;
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
					lineNo + "",
					getBillCardPanelWrapper().getBillCardPanel().getBillTable()
							.getSelectedRow(), "crowno");
			// ��Դ��������
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
					"8",
					getBillCardPanelWrapper().getBillCardPanel().getBillTable()
							.getSelectedRow(), "csourcetype");
		}
	}

	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
		if (lineNo == -1)
			lineNo = 10;
		else
			lineNo = lineNo + 10;
		// �к�
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
				lineNo + "",
				getBillCardPanelWrapper().getBillCardPanel().getBillTable()
						.getSelectedRow(), "crowno");
		// ��Դ��������
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
				"8",
				getBillCardPanelWrapper().getBillCardPanel().getBillTable()
						.getSelectedRow(), "csourcetype");
	}

	protected IBusinessController createBusinessAction() {
		switch (getUIController().getBusinessActionType()) {
		case IBusinessActionType.PLATFORM:
			return new BusinessAction(getBillUI());
		case IBusinessActionType.BD:
			return new W8004040204Action(getBillUI());
		default:
			return new BusinessAction(getBillUI());
		}
	}

	// ȡ��ǩ��
	protected void onQxqz() throws Exception {
		myClientUI.showHintMessage("����ִ��ȡ��ǩ��...");
		if (getBufferData().getCurrentRow() >= 0) {
			int retu = myClientUI.showOkCancelMessage("ȷ��ȡ��ǩ��?");
			if (retu == 1) {
				TbOutgeneralHVO generalh = (TbOutgeneralHVO) getBufferData()
						.getCurrentVO().getParentVO();
				Object result = getQueryBO().retrieveByPK(TbOutgeneralHVO.class,
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

						ArrayList list = (ArrayList) getQueryBO().retrieveByClause(
								IcGeneralHVO.class, strWhere);
						if (null != list && list.size() > 0) {
							IcGeneralHVO header = (IcGeneralHVO) list.get(0);
							if (null != header) {
								strWhere = " dr = 0 and cgeneralhid = '"
										+ header.getCgeneralhid() + "'";
								list = (ArrayList) getQueryBO().retrieveByClause(
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
									getIwBO().whs_processAction("CANCELSIGN",
											"DELETE", "4I", _getDate()
													.toString(), billvo,
											generalh);

									myClientUI.showHintMessage("�����ɹ�");
									// getBufferData().getCurrentVO().setParentVO(
									// generalh);
									// updateBuffer();
									super.onBoRefresh();
									getButtonManager()
											.getButton(
													nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr)
											.setEnabled(true);
									getButtonManager()
											.getButton(
													nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz)
											.setEnabled(false);
									getButtonManager().getButton(
											IBillButton.Edit).setEnabled(true);
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
				Object result = getQueryBO().retrieveByPK(TbOutgeneralHVO.class,
						generalh.getGeneral_pk());
				if (null != result) {
					generalh = (TbOutgeneralHVO) result;
					if (null != generalh.getVbillstatus()
							&& generalh.getVbillstatus() == 0) {
						myClientUI.showErrorMessage("�õ����Ѿ�ǩ��");
						return;
					}
					// ����ȷ�Ϻ�Ĳ���ǩ��
					else {
						if (generalh.getVbilltype().equals("0")
								&& (null == generalh.getVbillstatus() || generalh
										.getVbillstatus() != 1)) {
							myClientUI.showErrorMessage("�õ��ݻ�û�н����˵�ȷ��");
							return;
						}
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
						getIwBO().whs_processAction("SAVEPICKSIGN", null, "4I",
								_getDate().toString(), billvo, generalh);

						myClientUI.showHintMessage("ǩ�ֳɹ�");
						// getBufferData().getCurrentVO().setParentVO(generalh);
						// updateBuffer();
						super.onBoRefresh();
						getButtonManager().getButton(
								nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr)
								.setEnabled(false);
						getButtonManager().getButton(
								nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz)
								.setEnabled(true);
						getButtonManager().getButton(IBillButton.Edit)
								.setEnabled(false);
						myClientUI.updateButtons();
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

	/*
	 * ת��(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w8004040208.AbstractMyEventHandler#onzk()
	 */
	protected void onzk() throws Exception {
		lineNo = -1;
		if (isControl == 2 || isControl == 3) {
			isAdd = true;
			if(fydnewdlg == null){
				fydnewdlg = new FydnewDlg(null, ClientEnvironment.getInstance()
							.getCorporation().getPrimaryKey(),
							ClientEnvironment.getInstance().getUser()
							.getPrimaryKey(), WdsWlPubConsts.DM_ORDER_NODECODE, "1=1", 
							WdsWlPubConsts.WDS3, null, null,"4C", myClientUI, pkList, isStock, pk_stock);
			}
			// ���÷��� ��ȡ��ѯ��ľۺ�VO
			AggregatedValueObject[] vos = fydnewdlg
					.getReturnVOs(ClientEnvironment.getInstance()
							.getCorporation().getPrimaryKey(),
							ClientEnvironment.getInstance().getUser()
									.getPrimaryKey(), WdsWlPubConsts.WDS3,
							ConstVO.m_sBillDRSQ,WdsWlPubConsts.OTHER_OUT_FUNCODE, 
							WdsWlPubConsts.OTHER_OUT_REFWDS3_NODECODE,fydnewdlg);
			// �ж��Ƿ�Բ�ѯģ���н��в���
			if (null == vos || vos.length == 0) {
				return;
			}
			// ����ת���� ��ģ���л�ȡ�Ķ���ת�����Լ��ĵ�ǰ��ʾ�Ķ��󣬵��÷���
			// queryLocaData�����Ȳ�ѯ���ر��Ƿ������ݣ�������˵���õ��ݲ��ǵ�һ�����ӡ�
			MyBillVO voForSave = changeReqFydtoOutgeneral(vos);// �÷����ǵ�һ�����ӽ�������ת��

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
			setViewPro();
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"ccunhuobianma").setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"nshouldoutassistnum").setEnabled(false);
			billType = "0";
			changeButton(true);
			isTypes = 1;
		} else {
			myClientUI.showErrorMessage("����ʧ��,����Ȩ����");
			return;
		}
	}

	/**
	 * ��������һЩ���Ը�ֵ
	 * 
	 * @throws BusinessException
	 */
	private void setViewPro() throws BusinessException {
		// �����Ƶ���
		getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
				"coperatorid").setValue(
				ClientEnvironment.getInstance().getUser().getPrimaryKey());
		// �����޸���
		getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
				"clastmodiid").setValue(
				ClientEnvironment.getInstance().getUser().getPrimaryKey());
		// ��������
		getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
				"dbilldate").setValue(_getDate().toString());
		if (isControl == 3) {
			// �����շ����ͱ�ע�ɱ༭
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"cdispatcherid").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vnote")
					.setEnabled(true);
			// ����
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cdptid")
					.setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cdptid")
					.setValue("1021B110000000000BN9");
			// ���Ա
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"cwhsmanagerid").setEnabled(true);
		}
		getButtonManager().getButton(
				nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr)
				.setEnabled(false);
		myClientUI.updateButtons();
	}

	/*
	 * �ɹ�ȡ��(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w8004040208.AbstractMyEventHandler#oncgqy()
	 */
	protected void oncgqy() throws Exception {
		lineNo = -1;
		if (isControl == 2 || isControl == 3) {
			isAdd = true;

			cgqydlg = new CgqyDlg(myClientUI, pkList, isStock);

			// ���÷��� ��ȡ��ѯ��ľۺ�VO
			AggregatedValueObject[] vos = cgqydlg
					.getReturnVOs(ClientEnvironment.getInstance()
							.getCorporation().getPrimaryKey(),
							ClientEnvironment.getInstance().getUser()
									.getPrimaryKey(), "4278",
							ConstVO.m_sBillDRSQ, "8004040208", "8004040278",
							myClientUI);
			// �ж��Ƿ�Բ�ѯģ���н��в���
			if (null == vos || vos.length == 0) {
				return;
			}
			// ����ת���� ��ģ���л�ȡ�Ķ���ת�����Լ��ĵ�ǰ��ʾ�Ķ��󣬵��÷���
			// queryLocaDateByCgqy�����Ȳ�ѯ���ر��Ƿ������ݣ�������˵���õ��ݲ��ǵ�һ�����ӡ�
			MyBillVO voForSave = changeReqQycgtoOutgeneral(vos);// �÷����ǵ�һ�����ӽ�������ת��

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
			setViewPro();

			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"ccunhuobianma").setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"nshouldoutassistnum").setEnabled(false);
			billType = "9";
			changeButton();
			isTypes = 2;
		} else {
			myClientUI.showErrorMessage("����ʧ��,����Ȩ����");
			return;
		}
	}

	private void changeButton() {
		getButtonManager().getButton(ISsButtun.zdqh).setEnabled(false);
		getButtonManager().getButton(ISsButtun.tpzd).setEnabled(true);
		getButtonManager().getButton(ISsButtun.zj).setEnabled(false);
		myClientUI.updateButtons();
	}

	/**
	 * ��֤�������ڣ������Ƿ��п�����
	 * 
	 * @param generalb
	 * @return
	 */
	private boolean validate(TbOutgeneralBVO[] generalb) {
		String billdate = (String) getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("dbilldate").getValue();
		if (null == billdate || "".equals(billdate)) {
			myClientUI.showErrorMessage("��ѡ�񵥾�����");
			return false;
		}
		for (int i = 0; i < generalb.length; i++) {
			TbOutgeneralBVO genb = generalb[i];
			if (null == genb.getCinventoryid()
					|| "".equals(genb.getCinventoryid())) {
				myClientUI.showErrorMessage("����д��������");
				return false;
			}
			for (int j = 0; j < generalb.length; j++) {
				if (j == i)
					continue;
				if ((genb.getCinventoryid() + "1").equals(generalb[j]
						.getCinventoryid()
						+ "1")) {
					myClientUI.showErrorMessage("�����а�����ͬ��Ʒ,��ȥ��");
					return false;
				}
			}
		}
		return true;
	}

	private boolean validate(TbOutgeneralHVO generalh) {
		// �����жϵ����Ի�����ʾ
		if (generalh.getCdispatcherid() == null
				|| "".equals(generalh.getCdispatcherid().trim())) {
			getBillUI().showWarningMessage("��ѡ���շ����");
		} else if (generalh.getCwhsmanagerid() == null
				|| "".equals(generalh.getCwhsmanagerid())) {
			getBillUI().showWarningMessage("��ѡ����Ա");
		} else if (generalh.getCdptid() == null
				|| "".equals(generalh.getCdptid())) {
			getBillUI().showWarningMessage("��ѡ����");
		} else {
			return true;
		}
		return false;
	}

	@Override
	protected void onBoSave() throws Exception {
		
		// ��ȡ��ǰҳ����VO
		AggregatedValueObject myBillVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(myBillVO);
		TbOutgeneralBVO[] generalb = (TbOutgeneralBVO[]) myBillVO
				.getChildrenVO();
		TbOutgeneralHVO tmpgeneralh = null;
		TbOutgeneralHVO generalh = (TbOutgeneralHVO) myBillVO.getParentVO();
		// ��֤�����Ƿ�������
		if (null == generalb || generalb.length < 0) {
			myClientUI.showErrorMessage("����ʧ��,���������ݲ��ܱ���");
			return;
		}
		// �˵���Object,���湲����ֵ��[0]true or false falseΪ�����˵�ʧ��
		// [1]�˵�������󼯺�List [2]�˵��ӱ���󼯺�List
		Object[] obj = null;
		// ������������Ƶ��ݽ�����֤
		if (null != billType && billType.equals("8") && isAdd) {
			// ��֤������Ϣ
			if (!validate(generalb))
				return;
			// �Ƿ������˵�
			if (generalh.getIs_yundan().booleanValue())
				obj = insertFyd(generalh, generalb);
		} else {
			obj = new Object[1];
			obj[0] = false;
		}
		// ������Ա
		if (isControl == 3) {
			// �����ת��
			if (null != billType && billType.equals("0")) {
				// ������Դ���ݺŲ�ѯ�Ƿ����������⣬������Ѿ��������������������еĳ��ⵥ��ȥ
				String strWhere = " dr = 0 and vsourcebillcode = '"
						+ generalh.getVsourcebillcode()
						+ "' and csourcebillhid = '"
						+ generalh.getCsourcebillhid() + "'";
				ArrayList tmpList = (ArrayList) iuap.retrieveByClause(
						TbOutgeneralHVO.class, strWhere);
				if (null != tmpList && tmpList.size() > 0) {
					tmpgeneralh = (TbOutgeneralHVO) tmpList.get(0);
				}
//				// �õ����й������¼
//				if (null != tmpgeneralh) {
//					tmpgeneralh.setCdispatcherid(generalh.getCdispatcherid());// �շ����
//					tmpgeneralh.setCwhsmanagerid(generalh.getCwhsmanagerid());// ���Ա
//					tmpgeneralh.setCdptid(generalh.getCdptid());// ����
//					tmpgeneralh.setVnote(generalh.getVnote());// ��ע
//					// �ѱ�ͷ�滻
//					generalh = tmpgeneralh;
//				} else 
				 if(tmpgeneralh ==null){
					isAdd = true;
					// �Ƶ�ʱ��
					generalh.setTmaketime(myClientUI._getServerTime()
							.toString());
					// ���õ��ݺ�
					generalh.setVbillcode(CommonUnit.getBillCode("4I",
							ClientEnvironment.getInstance().getCorporation()
									.getPk_corp(), "", ""));
					// �������
					generalh.setVbilltype(billType);
					// ��˾
					generalh.setComp(ClientEnvironment.getInstance()
							.getCorporation().getPk_corp());
				}
				// ������������ñ���״̬��������
				if (isAdd) {
					// ѭ���������״̬
					for (int i = 0; i < generalb.length; i++) {
						generalb[i].setStatus(VOStatus.NEW);
					}
				} else { // ���ñ���״̬���޸ġ�
					// ѭ���������״̬
					for (int i = 0; i < generalb.length; i++) {
						generalb[i].setStatus(VOStatus.UPDATED);
					}
				}

			} else {
				// ����״̬
				if (isAdd) {

					// ѭ���������״̬
					for (int i = 0; i < generalb.length; i++) {
						generalb[i].setStatus(VOStatus.NEW);
					}
					// �Ƶ�ʱ��
					generalh.setTmaketime(myClientUI._getServerTime()
							.toString());
					// ���õ��ݺ�
					generalh.setVbillcode(CommonUnit.getBillCode("4I",
							ClientEnvironment.getInstance().getCorporation()
									.getPk_corp(), "", ""));
					// �������
					generalh.setVbilltype(billType);
					// ��˾
					generalh.setComp(ClientEnvironment.getInstance()
							.getCorporation().getPk_corp());
				} else {
					// ѭ���������״̬
					for (int i = 0; i < generalb.length; i++) {
						generalb[i].setStatus(VOStatus.UPDATED);
					}

				}
			}
		} else if (isControl == 1) { // �������Ϣ�ƽ��б���
			// ��֤��ͷ��Ϣ
			if (!validate(generalh))
				return;

		} else {
			// ������Դ���ݺŲ�ѯ�Ƿ�����������
			String strWhere = " dr = 0 and vsourcebillcode = '"
					+ generalh.getVsourcebillcode()
					+ "' and csourcebillhid = '" + generalh.getCsourcebillhid()
					+ "'";
			ArrayList tmpList = (ArrayList)getQueryBO().retrieveByClause(
					TbOutgeneralHVO.class, strWhere);
			if (null != tmpList && tmpList.size() > 0) {
				tmpgeneralh = (TbOutgeneralHVO) tmpList.get(0);
			}
			// �õ����й������¼
			if (null != tmpgeneralh && !billType.equals("8")) {
				// �ѱ�ͷ�滻
				generalh = tmpgeneralh;
			} else if (!billType.equals("8")) {
				isAdd = true;
				// �Ƶ�ʱ��
				generalh.setTmaketime(myClientUI._getServerTime().toString());
				// ���õ��ݺ�
				generalh.setVbillcode(CommonUnit.getBillCode("4I",
						ClientEnvironment.getInstance().getCorporation()
								.getPk_corp(), "", ""));
			} else if (isAdd) {
				// �Ƶ�ʱ��
				generalh.setTmaketime(myClientUI._getServerTime().toString());
			}
			// ������������ñ���״̬��������
			if (isAdd) {
				// ѭ���������״̬
				for (int i = 0; i < generalb.length; i++) {
					generalb[i].setStatus(VOStatus.NEW);
				}
			} else { // ���ñ���״̬���޸ġ�
				// ѭ���������״̬
				for (int i = 0; i < generalb.length; i++) {
					generalb[i].setStatus(VOStatus.UPDATED);
				}
			}
			// �������
			generalh.setVbilltype(billType);
			// ����״̬
			// generalh.setVbillstatus(new Integer(1));// û��ǩ��
			// ��˾
			generalh.setComp(ClientEnvironment.getInstance().getCorporation()
					.getPk_corp());
		}
		// ����޸�ʱ��
		generalh.setTlastmoditime(myClientUI._getServerTime().toString());
		// �����޸���
		generalh.setClastmodiid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey());

		myBillVO.setParentVO(generalh);
		myBillVO.setChildrenVO(generalb);
		// �����������
		Object o = null;
		ISingleController sCtrl = null;
		if (getUIController() instanceof ISingleController) {
			sCtrl = (ISingleController) getUIController();
			if (sCtrl.isSingleDetail()) {
				o = myBillVO.getParentVO();
				myBillVO.setParentVO(null);
			} else {
				o = myBillVO.getChildrenVO();
				myBillVO.setChildrenVO(null);
			}
		}

		boolean isSave = true;

		// �ж��Ƿ��д�������
		if (myBillVO.getParentVO() == null
				&& (myBillVO.getChildrenVO() == null || myBillVO
						.getChildrenVO().length == 0)) {
			isSave = false;
		} else {
			if (getBillUI().isSaveAndCommitTogether())
				myBillVO = getBusinessAction().saveAndCommit(myBillVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), myBillVO);
			
			else {

				List objUser = new ArrayList();
				objUser.add(getBillUI().getUserObject());
				objUser.add(generalb);
				if (isAdd)
					objUser.add(true);
				else
					objUser.add(false);
				objUser.add(true);

				objUser.add(isStock);

				objUser.add(obj);
				// write to database
				myBillVO = getBusinessAction().save(myBillVO,
						getUIController().getBillType(), _getDate().toString(),
						objUser, myBillVO);
				// myBillVO.setChildrenVO(generalb);
				if (null == myBillVO) {
					myClientUI.showErrorMessage("����ʧ��,�����²����õ���");
					return;
				}
				// �����ɫ
				noColor((TbOutgeneralBVO[]) myBillVO.getChildrenVO());
				// ����ָ����ť���Զ�ȡ����ť������
				changeButton(false);
				// �ж���������Ƶ������� ���а�ť�����ñ�����պ�Ӧ��������������
				if (null != billType && billType.equals("8")) {
					getButtonManager().getButton(IBillButton.Line).setVisible(
							false);

					getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
							"ccunhuobianma").setEnabled(false);
					getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
							"nshouldoutassistnum").setEnabled(false);
				}
				if (isControl == 1 || isControl == 3) {
					getButtonManager().getButton(
							nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr)
							.setEnabled(true);
					myClientUI.updateButtons();
				}
			}
		}

		// �������ݻָ�����
		if (sCtrl != null) {
			if (sCtrl.isSingleDetail())
				myBillVO.setParentVO((CircularlyAccessibleValueObject) o);
		}
		int nCurrentRow = -1;
		if (isSave) {
			if (isEditing()) {
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(myBillVO);
					nCurrentRow = 0;

				} else {
					getBufferData().setCurrentVO(myBillVO);
					nCurrentRow = getBufferData().getCurrentRow();
				}
			}
			// �������������
			setAddNewOperate(isAdding(), myBillVO);
		}
		// ���ñ����״̬
		setSaveOperateState();
		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRow(nCurrentRow);
		}
		myClientUI.updateButtons();	
		}

	/**
	 * ���ɷ��˵�
	 * 
	 * @throws Exception
	 */
	private Object[] insertFyd(TbOutgeneralHVO generalh,
			TbOutgeneralBVO[] generalb) throws Exception {
		Object[] o = new Object[3];
		o[0] = false;
		List<TbFydnewVO> fydList = new ArrayList<TbFydnewVO>();
		List<TbFydmxnewVO[]> fydmxList = new ArrayList<TbFydmxnewVO[]>();

		// ����VOת��/////////////////////////////////////////////

		// ------------ת����ͷ����-----------------//
		TbFydnewVO fydvo = new TbFydnewVO();
		if (null != generalh && null != generalb && generalb.length > 0) {
			if (null != generalh.getVdiliveraddress()
					&& !"".equals(generalh.getVdiliveraddress())) {
				fydvo.setFyd_shdz(generalh.getVdiliveraddress()); // �ջ���ַ
			}
			if (null != generalh.getVnote() && !"".equals(generalh.getVnote())) {
				fydvo.setFyd_bz(generalh.getVnote()); // ��ע
			}
			if (null != generalh.getCdptid()
					&& !"".equals(generalh.getCdptid())) {
				fydvo.setCdeptid(generalh.getCdptid()); // ����
			}
			// �����˻���ʽ
			fydvo.setFyd_yhfs("����");
			// �������� 0 �����Ƶ� 1 ���۶��� 2 �ֳ�ֱ�� 3��ֶ���4 �ϲ����� 8 �������Ƶ������ɵ��˵�
			fydvo.setBilltype(8);
			fydvo.setVbillstatus(1);
			// ���ݺ�
			fydvo.setVbillno(generalh.getVbillcode());
			// �Ƶ�����
			fydvo.setDmakedate(generalh.getDbilldate());
			fydvo.setVoperatorid(ClientEnvironment.getInstance().getUser()
					.getPrimaryKey()); // �����Ƶ���
			// ���÷���վ
			fydvo.setSrl_pk(generalh.getSrl_pk());
			// ����վ
			fydvo.setSrl_pkr(generalh.getSrl_pkr());
			fydList.add(fydvo);
			// --------------ת����ͷ����---------------//
			// --------------ת������----------------//
			List<TbFydmxnewVO> tbfydmxList = new ArrayList<TbFydmxnewVO>();
			for (int j = 0; j < generalb.length; j++) {
				TbFydmxnewVO fydmxnewvo = new TbFydmxnewVO();
				TbOutgeneralBVO genb = generalb[j];
				if (null != genb.getCinventoryid()
						&& !"".equals(genb.getCinventoryid())) {
					fydmxnewvo.setPk_invbasdoc(genb.getCinventoryid()); // ��Ʒ����
				}
				if (null != genb.getNshouldoutnum()
						&& !"".equals(genb.getNshouldoutnum())) {
					fydmxnewvo.setCfd_yfsl(genb.getNshouldoutnum()); // Ӧ������
				}
				if (null != genb.getNshouldoutassistnum()
						&& !"".equals(genb.getNshouldoutassistnum())) {
					fydmxnewvo.setCfd_xs(genb.getNshouldoutassistnum()); // ����
				}
				if (null != genb.getNoutnum() && !"".equals(genb.getNoutnum())) {
					fydmxnewvo.setCfd_sfsl(genb.getNoutnum()); // ʵ������
				}
				if (null != genb.getNoutassistnum()
						&& !"".equals(genb.getNoutassistnum())) {
					fydmxnewvo.setCfd_sffsl(genb.getNoutassistnum()); // ʵ��������
				}
				if (null != genb.getCrowno() && !"".equals(genb.getCrowno())) {
					fydmxnewvo.setCrowno(genb.getCrowno()); // �к�
				}
				if (null != genb.getUnitid() && !"".equals(genb.getUnitid())) {
					fydmxnewvo.setCfd_dw(genb.getUnitid()); // ��λ
				}
				fydmxnewvo.setCfd_pc(genb.getVbatchcode()); // ����
				tbfydmxList.add(fydmxnewvo);
			}
			// ----------------ת���������---------------------//
			if (tbfydmxList.size() > 0) {
				TbFydmxnewVO[] fydmxVO = new TbFydmxnewVO[tbfydmxList.size()];
				tbfydmxList.toArray(fydmxVO);
				fydmxList.add(fydmxVO);
				o[0] = true;
			} else {
				fydmxList.add(null);
			}
		}
		o[1] = fydList;
		o[2] = fydmxList;
		return o;
	}

	@Override
	protected void onBoEdit() throws Exception {
		billType = ((TbOutgeneralHVO) getBufferData().getCurrentVO()
				.getParentVO()).getVbilltype();
		if (isControl == 3) {
			super.onBoEdit();
			// ��������ָ����ť���ã��޸�״̬
			isAdd = false;

			// ���޸�Ȩ��
			// �����շ����ͱ�ע�ɱ༭
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"cdispatcherid").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vnote")
					.setEnabled(true);
			// ����
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cdptid")
					.setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cdptid")
					.setValue("1021B110000000000BN9");
			// ���Ա
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"cwhsmanagerid").setEnabled(true);
			getButtonManager().getButton(ISsButtun.tpzd).setEnabled(true);// ����ָ��
			getButtonManager().getButton(ISsButtun.zj).setEnabled(false);// ���Ӱ�ť
			getButtonManager().getButton(IBillButton.Line).setEnabled(false);// ����
			// ǩ�ְ�ť������
			getButtonManager().getButton(
					nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr).setEnabled(
					false);
			myClientUI.updateButtons();
			return;
		}

		// ���˿�
		if (isControl == 0) {
			super.onBoEdit();
			// ��������ָ����ť���ã��޸�״̬
			isAdd = false;
			getButtonManager().getButton(ISsButtun.tpzd).setEnabled(true);
			getButtonManager().getButton(ISsButtun.zj).setEnabled(false);// ���Ӱ�ť
			getButtonManager().getButton(IBillButton.Line).setEnabled(false);// ����
			myClientUI.updateButtons();

		} else if (isControl == 1) {
			super.onBoEdit();
			// ��������ָ����ť���ã��޸�״̬
			isAdd = false;
			// ���޸�Ȩ��
			// �����շ����ͱ�ע�ɱ༭
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"cdispatcherid").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vnote")
					.setEnabled(true);
			// ����
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cdptid")
					.setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cdptid")
					.setValue("1021B110000000000BN9");
			// ���Ա
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"cwhsmanagerid").setEnabled(true);
			getButtonManager().getButton(
					nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr).setEnabled(
					false);
			getButtonManager().getButton(ISsButtun.tpzd).setEnabled(false);// ����ָ��

			getButtonManager().getButton(IBillButton.Line).setEnabled(false);// ����
			getButtonManager().getButton(ISsButtun.zj).setEnabled(false);// ���Ӱ�ť

			getBillUI().updateButtons();

		} else {
			myClientUI.showErrorMessage("��û��Ȩ���޸�");
			return;
		}
	}

	/*
	 * ���Ƶ���(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w8004040208.AbstractMyEventHandler#onzzdj()
	 */
	protected void onzzdj(ButtonObject bo) throws Exception {
		if (isControl == 2 || isControl == 3) {
			lineNo = -1;
			getButtonManager().getButton(IBillButton.Line).setVisible(true);
			super.onBoAdd(bo);
			UIRefPane panel = (UIRefPane) getBillCardPanelWrapper()
					.getBillCardPanel().getBodyItem("ccunhuobianma")
					.getComponent();
			StringBuffer strWhere = new StringBuffer();
			// �����ֻܲ��߷ֲֽ��й��˵�Ʒ��ʾ����
			if (isStock) {
				if (null != pkList && pkList.size() > 0) {

					strWhere.append(" pk_invbasdoc in (");
					for (int i = 0; i < pkList.size(); i++) {
						if (i == pkList.size() - 1)
							strWhere.append("'" + pkList.get(i) + "')");
						else {
							strWhere.append("'" + pkList.get(i) + "'").append(
									" , ");
						}
					}
					panel.getRefModel().setWherePart(strWhere.toString());
				} else {
					panel.getRefModel().setWherePart(" 1=2");
				}
			} else {
				strWhere.append(" dr = 0 and ( def14 ='0' or def14 = '1') ");
				panel.getRefModel().setWherePart(strWhere.toString());
			}
			for (int i = 0; i < columnName.length; i++) {
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
						columnName[i]).setEnabled(true);
			}
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"ccunhuobianma").setEnabled(true);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"nshouldoutassistnum").setEnabled(true);
			String pk_stock = CommonUnit.getStordocName(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("srl_pk")
					.setValue(pk_stock);
			setViewPro();
			// ���õ��ݺ�
			getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
					"vbillcode").setValue(
					CommonUnit.getBillCode("4I", ClientEnvironment
							.getInstance().getCorporation().getPk_corp(), "",
							""));
			// getButtonManager().getButton(ISsButtun.zzdj).setEnabled(false);

			billType = "8";
			changeButton();
			isTypes = 3;
			isAdd = true;
		} else {
			myClientUI.showErrorMessage("����ʧ��,����Ȩ����");
			return;
		}
	}

	// �����Ƶ���״̬����Щ�ֶο��Ա༭
	String[] columnName = { "dbilldate", "srl_pkr", "vdiliveraddress", "vnote",
			"is_yundan" };

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {

	}

	/**
	 * ���ݲ����ı�����ָ�����Զ�ȡ����ť�Ƿ����
	 * 
	 * @param type
	 */
	private void changeButton(boolean type) {
		getButtonManager().getButton(ISsButtun.zdqh).setEnabled(type);
		getButtonManager().getButton(ISsButtun.tpzd).setEnabled(type);
		getButtonManager().getButton(ISsButtun.zj).setEnabled(!type);

		myClientUI.updateButtons();
	}

	@Override
	protected void onBoCancel() throws Exception {
		getBillUI().initUI();
		getBufferData().clear();
		changeButton(false);
		if (isControl == 1) {
			getButtonManager().getButton(ISsButtun.zj).setEnabled(false);
			myClientUI.updateButtons();
		} else if (isControl == 2) {
			getButtonManager().getButton(ISsButtun.zj).setEnabled(true);
			myClientUI.updateButtons();
		}
	}

	/*
	 * ����ָ��(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w8004040208.AbstractMyEventHandler#ontpzd()
	 */
	protected void ontpzd() throws Exception {

		// ��ȡ��ѡ����к�
		int index = getBillCardPanelWrapper().getBillCardPanel().getBillTable()
				.getSelectedRow();
		if (index > -1) {
			// ��ȡ�����ѡ���VO����
			TbOutgeneralBVO item = (TbOutgeneralBVO) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getBodyValueRowVO(index,
							TbOutgeneralBVO.class.getName());
			if (null != item) {
				String sNum = getRandomNum();
				if (null == item.getCinventoryid()
						|| "".equals(item.getCinventoryid())) {
					myClientUI.showErrorMessage("��ѡ��Ʒ");
					return;
				}
				if (null == item.getCsourcebillhid()
						|| "".equals(item.getCsourcebillhid())) {
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(sNum, index, "csourcebillhid");
					item.setCsourcebillhid(sNum);
				}
				if (null == item.getCsourcebillbid()
						|| "".equals(item.getCsourcebillbid())) {
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(sNum, index, "csourcebillbid");
					item.setCsourcebillbid(sNum);
				}
				Object pk_stordoc = getBillCardPanelWrapper()
						.getBillCardPanel().getHeadItem("srl_pk")
						.getValueObject();
				if (null == pk_stordoc || "".equals(pk_stordoc)) {
					myClientUI.showErrorMessage("����ʧ��,����Ȩ����");
					return;
				}
				TrayDisposeDlg tdpDlg = new TrayDisposeDlg("4209",
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey(),
						ClientEnvironment.getInstance().getCorporation()
								.getPrimaryKey(), "8004040208", myClientUI);
				tdpDlg.getReturnVOs(myClientUI, item, index, pk_stordoc
						.toString(), false);
				chaneColor();
			}
		} else {
			getBillUI().showWarningMessage("��ѡ������н��в���");
		}
	}

	/*
	 * �Զ�ȡ��(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w8004040208.AbstractMyEventHandler#onzdqh()
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
				}
				String msg = getIwBO().autoPickAction(ClientEnvironment
						.getInstance().getUser().getPrimaryKey(), generalbVO,
						pk_stordoc.toString(), "def16");
				if (null != msg) {
					myClientUI.showErrorMessage(msg);
					return;
				} else {
					tmpbVO = new TbOutgeneralBVO[generalbVO.length];
					for (int i = 0; i < generalbVO.length; i++) {
						getBillCardPanelWrapper().getBillCardPanel()
								.setBodyValueAt(null, i, "noutnum");
						getBillCardPanelWrapper().getBillCardPanel()
								.setBodyValueAt(null, i, "noutassistnum");
						getBillCardPanelWrapper().getBillCardPanel()
								.setBodyValueAt(null, i, "vbatchcode");
						// ���ýӿڲ�ѯ������е�ʵ����������
						Object[] a = getIwBO().getNoutNum(generalbVO[i]
								.getCsourcebillbid(), generalbVO[i]
								.getCinventoryid().trim());
						if (null != a && a.length > 0) {
							// ʵ������
							if (null != a[0])
								getBillCardPanelWrapper().getBillCardPanel()
										.setBodyValueAt(a[0], i, "noutnum");
							// ʵ��������
							if (null != a[1]) {
								getBillCardPanelWrapper().getBillCardPanel()
										.setBodyValueAt(a[1], i,
												"noutassistnum");
							}
							// ����
							if (null != a[2]
									&& a[2].toString().trim().length() > 0) {
								getBillCardPanelWrapper().getBillCardPanel()
										.setBodyValueAt(a[2], i, "vbatchcode");
							}
							// ����
							if (null != a[3]) {
								getBillCardPanelWrapper().getBillCardPanel()
										.setBodyValueAt(a[3], i, "nprice");
							}
							// ���
							if (null != a[4]) {
								getBillCardPanelWrapper().getBillCardPanel()
										.setBodyValueAt(a[4], i, "nmny");
							}
							// ��Դ����
							if (null != a[5]
									&& a[5].toString().trim().length() > 0) {
								getBillCardPanelWrapper().getBillCardPanel()
										.setBodyValueAt(a[5], i, "lvbatchcode");
							}
							// ��λID
							getBillCardPanelWrapper()
									.getBillCardPanel()
									.setBodyValueAt(
											CommonUnit
													.getCargDocName(ClientEnvironment
															.getInstance()
															.getUser()
															.getPrimaryKey()),
											i, "cspaceid");
						}
					}
					chaneColor();
					this.onckmx();
				}
			} else {
				myClientUI.showErrorMessage("����ʧ��,����Ȩ����");
				return;
			}
		}

	}

	/*
	 * ��ѯ��ϸ(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w8004040208.AbstractMyEventHandler#onckmx()
	 */
	protected void onckmx() throws Exception {
		TbOutgeneralBVO[] item = null;
		if (null != billType && billType.equals("8")) {
			// ��ȡ��ǰҳ����VO
			AggregatedValueObject myBillVO = getBillUI().getVOFromUI();
			item = (TbOutgeneralBVO[]) myBillVO.getChildrenVO();
			// ��֤�����Ƿ�������
			if (null == item || item.length < 0) {
				myClientUI.showErrorMessage("����ʧ��,����Ȩ����");
				return;
			}
		} else {
			if (getBufferData().getVOBufferSize() <= 0) {

				myClientUI.showErrorMessage("����ʧ��,����Ȩ����");
				return;
			}
			item = (TbOutgeneralBVO[]) getBufferData().getCurrentVO()
					.getChildrenVO();
		}
		TrayDisposeDetailDlg tdpDlg = new TrayDisposeDetailDlg("4209",
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey(), "8004040208", myClientUI);

		tdpDlg.getReturnVOs(myClientUI, item);

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
	protected void onBoQuery() throws Exception {
		if (isControl == -1) {
			myClientUI.showErrorMessage("��û��Ȩ�޲���");
			return;
		}
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ
		int tmp = strWhere.indexOf("tb_outgeneral_h.vbilltype");
		StringBuffer strtmp = new StringBuffer(strWhere.substring(tmp, strWhere
				.length()));
		StringBuffer a = new StringBuffer(strWhere.toString().toLowerCase()
				.substring(tmp, strWhere.length()));

		int tmpnum = a.indexOf("and");

		StringBuffer b = new StringBuffer(a.substring(0, tmpnum));
		for (int i = 28; i < b.length(); i++) {
			String stra = b.substring(i, i + 1);
			if (stra.equals("1"))
				b.replace(i, i + 1, "9");
			if (stra.equals("2"))
				b.replace(i, i + 1, "8");
		}
		strtmp.replace(0, tmpnum, b.toString());
		strWhere.replace(tmp, strWhere.length(), strtmp.toString());
		// strWhere.append(" and srl_pk = '" + pk_stock + "' ");
		SuperVO[] queryVos = null;
		if (isControl == 2 || isControl == 0) {
			// ��ȡ��ʼ����
			tmp = strWhere.indexOf("tb_outgeneral_h.srl_pk");
			// ����Ա��̨���ֿ⸳ֵ������ǰ̨��ѡ��ֿ�Ҳ��Ĭ�ϵ�ǰ��¼�˵ġ�
			if (tmp > -1) {
				// �ӿ�ʼ�����������������ַ���
				strtmp = new StringBuffer(strWhere.substring(tmp, strWhere
						.length()));
				strtmp.replace(strtmp.indexOf("'"), strtmp.indexOf("'") + 21,
						"'" + pk_stock);
				strWhere.replace(tmp, strWhere.length(), strtmp.toString());
			} else
				strWhere.append(" and srl_pk = '" +pk_stock + "' ");
			if (isControl == 0) // ���˿� ��ѯȫ�����ֿ�
				queryVos = queryHeadVOs(strWhere.toString());
			else {
				if (isStock) // �ֲܲ����Ǳ���Ա��ѯ�б���λ�µ�Ʒ�ļ�¼
					queryVos = CommonUnit.queryTbOutGeneral(pkList, strWhere);
				else
					// �ֱֲ���Ա��ѯȫ�����ֵ�Ʒ
					queryVos = queryHeadVOs(strWhere.toString());
			}
		} else
			queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();

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

	/**
	 * ȡ���ɹ��ƻ� ��ģ���е�ѡ�е�VO ����ת����ȡ���ɹ������VO
	 * 
	 * @param vos
	 *            ҳ��ѡ�еľۺ�VO
	 * @return �ۺ�VO
	 */
	private MyBillVO changeReqQycgtoOutgeneral(AggregatedValueObject[] vos) {
		MyBillVO myBillVO = new MyBillVO();
		// hiddenList = new ArrayList();
//		int num = 0; // Ϊ�˼�������ĳ���
		TbOutgeneralHVO generalHVO = new TbOutgeneralHVO();
		// �ӱ���Ϣ���鼯��
		List<TbOutgeneralBVO> generalBList = new ArrayList<TbOutgeneralBVO>();

		TbQycgjhVO qycg = (TbQycgjhVO) vos[0].getParentVO();
		if (null != qycg.getQycgjh_ck() && !"".equals(qycg.getQycgjh_ck())) {
			generalHVO.setSrl_pk(qycg.getQycgjh_ck()); // �ֿ�
		}
		if (null != qycg.getQycgjh_ddh() && !"".equals(qycg.getQycgjh_ddh())) {
			generalHVO.setVsourcebillcode(qycg.getQycgjh_ddh()); // ��Դ���ݺ�
		}
		if (null != qycg.getPk_qycgjh() && !"".equals(qycg.getPk_qycgjh())) {
			generalHVO.setCsourcebillhid(qycg.getPk_qycgjh()); // ��Դ���ݱ�ͷ����
		}
		myBillVO.setParentVO(generalHVO);
		// ѭ�������е��ӱ���Ϣ
		if (null != cgqydlg.getQmxVO() && cgqydlg.getQmxVO().length > 0) {
			// ��ȡ���� ת���ӱ���������Ϣ
			for (int i = 0; i < cgqydlg.getQmxVO().length; i++) {
				String invpk = null;
				if (null != pkList && pkList.size() > 0) {
					TbQycgjh2VO qmxvo = cgqydlg.getQmxVO()[i];
					if (qycg.getPk_qycgjh().equals(qmxvo.getPk_qycgjh())) {
						TbOutgeneralBVO generalBVO = new TbOutgeneralBVO();
						if (null != qycg.getPk_qycgjh()
								&& !"".equals(qycg.getPk_qycgjh())) {
							generalBVO.setCsourcebillhid(qycg.getPk_qycgjh()); // ��Դ���ݱ�ͷ����
							generalBVO.setCfirstbillhid(qycg.getPk_qycgjh()); // Դͷ���ݱ�ͷ����
						}
						if (null != qycg.getQycgjh_ddh()
								&& !"".equals(qycg.getQycgjh_ddh())) {
							generalBVO.setVsourcebillcode(qycg.getQycgjh_ddh()); // ��Դ���ݺ�
						}
						if (null != qmxvo.getPk_qycgjh()
								&& !"".equals(qmxvo.getPk_qycgjh())) {
							generalBVO.setCsourcebillbid(qmxvo.getPk_qycgjh2()); // ��Դ���ݱ�������
							generalBVO.setCfirstbillbid(qmxvo.getPk_qycgjh2()); // Դͷ���ݱ�������
						}
						if (null != qmxvo.getQycgjh2_fsl()
								&& !"".equals(qmxvo.getQycgjh2_fsl())) {
							generalBVO.setNshouldoutassistnum(qmxvo
									.getQycgjh2_fsl()); // Ӧ��������
						}
						if (null != qmxvo.getQycgjh2_zsl()
								&& !"".equals(qmxvo.getQycgjh2_zsl())) {
							generalBVO.setNshouldoutnum(qmxvo.getQycgjh2_zsl()); // Ӧ��������
						}
						if (null != qmxvo.getQycgjh2_chzj()
								&& !"".equals(qmxvo.getQycgjh2_chzj())) {
							generalBVO.setCinventoryid(qmxvo.getQycgjh2_chzj()); // �������
						}
						// �к�
						generalBVO.setIsoper(new UFBoolean("Y"));
						generalBVO.setCrowno(qmxvo.getQycgjh2_hh());
						generalBList.add(generalBVO);
						// for (int j = 0; j < pkList.size(); j++) {
						// invpk = pkList.get(j).toString();
						// if (invpk.equals(qmxvo.getQycgjh2_chzj())) {
						// // ����Ҫ��ʾ�ı����������Ƿ�����ֶθ�ֵ
						// generalBVO.setIsoper(new UFBoolean("Y"));
						// generalBList.add(generalBVO);
						// break;
						// }
						// }
						// hiddenList.add(generalBVO); // �������ؼ����б�
					}
				}
			}
			TbOutgeneralBVO[] generalBVO = new TbOutgeneralBVO[generalBList
					.size()];
			generalBVO = generalBList.toArray(generalBVO);
			myBillVO.setChildrenVO(generalBVO);
		}

		return myBillVO;
	}

	/**
	 * ��ģ���е�ѡ�е�VO ����ת����ת������VO
	 * 
	 * @param vos
	 *            ҳ��ѡ�еľۺ�VO
	 * @return �ۺ�VO
	 */
	private MyBillVO changeReqFydtoOutgeneral(AggregatedValueObject[] vos) {
		// hiddenList = new ArrayList();
		MyBillVO myBillVO = new MyBillVO();
//		int num = 0; // Ϊ�˼�������ĳ���
		TbOutgeneralHVO generalHVO = new TbOutgeneralHVO();
		// �ӱ���Ϣ���鼯��
		List<TbOutgeneralBVO> generalBList = new ArrayList<TbOutgeneralBVO>();

		SendorderVO fydnew = (SendorderVO) vos[0].getParentVO();
		if (null != fydnew.getPk_outwhouse() && !"".equals(fydnew.getPk_outwhouse())) {
			generalHVO.setSrl_pk(fydnew.getPk_outwhouse()); // ����ֿ�
		}
		if (null != fydnew.getPk_inwhouse() && !"".equals(fydnew.getPk_inwhouse())) {
			generalHVO.setSrl_pkr(fydnew.getPk_inwhouse()); // ���ֿ�
		}
		if (null != fydnew.getVinaddress() && !"".equals(fydnew.getVinaddress())) {
			generalHVO.setVdiliveraddress(fydnew.getVinaddress()); // �ջ���ַ
		}
		if (null != fydnew.getVmemo() && !"".equals(fydnew.getVmemo())) {
			generalHVO.setVnote(fydnew.getVmemo()); // ��ע
		}
		if (null != fydnew.getDapprovedate()
				&& !"".equals(fydnew.getDapprovedate())) {
			generalHVO.setDauditdate(fydnew.getDapprovedate()); // �������
		}
		if (null != fydnew.getVbillno() && !"".equals(fydnew.getVbillno())) {
			generalHVO.setVsourcebillcode(fydnew.getVbillno()); // ��Դ���ݺ�
		}
		if (null != fydnew.getPk_sendorder() && !"".equals(fydnew.getPk_sendorder())) {
			generalHVO.setCsourcebillhid(fydnew.getPk_sendorder()); // ��Դ���ݱ�ͷ����
		}

		myBillVO.setParentVO(generalHVO);
		// ѭ�������е��ӱ���Ϣ
		if (null != fydnewdlg.getFydmxVO() && fydnewdlg.getFydmxVO().length > 0) {

			// ��ȡ���� ת���ӱ���������Ϣ
			for (int i = 0; i < fydnewdlg.getFydmxVO().length; i++) {
//				String invpk = null;
				if (null != pkList && pkList.size() > 0) {
					SendorderBVO fydmxnewvo = fydnewdlg.getFydmxVO()[i];
					if (fydnew.getPk_sendorder().equals(fydmxnewvo.getPk_sendorder())) {

						TbOutgeneralBVO generalBVO = new TbOutgeneralBVO();
						if (null != fydnew.getPk_sendorder()
								&& !"".equals(fydnew.getPk_sendorder())) {
							generalBVO.setCsourcebillhid(fydnew.getPk_sendorder()); // ��Դ���ݱ�ͷ����
							generalBVO.setCfirstbillhid(fydmxnewvo.getPk_sendorder()); // Դͷ���ݱ�ͷ����
						}
						if (null != fydnew.getVbillno()
								&& !"".equals(fydnew.getVbillno())) {
							generalBVO.setVsourcebillcode(fydnew.getVbillno()); // ��Դ���ݺ�
						}
						if (null != fydnew.getPk_billtype()
								&& !"".equals(fydnew.getPk_billtype())) {
							generalBVO.setCsourcetype(fydnew.getPk_billtype()
									.toString()); // ��Դ��������
						}
						if (null != fydmxnewvo.getPk_sendorder_b()
								&& !"".equals(fydmxnewvo.getPk_sendorder_b())) {
							generalBVO
									.setCsourcebillbid(fydmxnewvo.getPk_sendorder_b()); // ��Դ���ݱ�������
							generalBVO.setCfirstbillbid(fydmxnewvo.getPk_sendorder_b()); // Դͷ���ݱ�������
						}
						if (null != fydmxnewvo.getNdealnum()
								&& !"".equals(fydmxnewvo.getNdealnum())) {
							generalBVO.setNshouldoutnum(fydmxnewvo
									.getNdealnum()); // ��������
						}
						if (null != fydmxnewvo.getNassdealnum()
								&& !"".equals(fydmxnewvo.getNassdealnum())) {
							generalBVO.setNshouldoutassistnum(fydmxnewvo
									.getNassdealnum()); // ���Ÿ�����
						}
						if (null != fydmxnewvo.getPk_invbasdoc()
								&& !"".equals(fydmxnewvo.getPk_invbasdoc())) {
							generalBVO.setCinventoryid(fydmxnewvo
									.getPk_invbasdoc()); // �������
						}
						generalBVO.setIsoper(new UFBoolean("Y"));
						generalBVO.setCrowno(""+(i+1)*10);
						// �к�
						// generalBVO.setCrowno(getRowNo() + "");
						generalBList.add(generalBVO);
						// for (int j = 0; j < pkList.size(); j++) {
						// invpk = pkList.get(j).toString();
						// if (invpk.equals(fydmxnewvo.getPk_invbasdoc())) {
						// // ����Ҫ��ʾ�ı����������Ƿ�����ֶθ�ֵ
						// generalBVO.setIsoper(new UFBoolean("Y"));
						// generalBList.add(generalBVO);
						// break;
						// }
						// }
						// hiddenList.add(generalBVO); // �������ؼ����б�
					}
				}
			}
			TbOutgeneralBVO[] generalBVO = new TbOutgeneralBVO[generalBList
					.size()];
			generalBVO = generalBList.toArray(generalBVO);
			myBillVO.setChildrenVO(generalBVO);
		}

		return myBillVO;
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
		TbOutgeneralHVO tmphvo = (TbOutgeneralHVO) getBufferData()
				.getCurrentVO().getParentVO();
		// ���س���� ����
		TbOutgeneralBVO[] tmpbvo = null;
		// ����ۺ�VO
		GeneralBillVO gBillVO = new GeneralBillVO();
		// �����ͷVO
		GeneralBillHeaderVO generalhvo = null;
		// �����ӱ���
		List<GeneralBillItemVO> generalBVOList = new ArrayList<GeneralBillItemVO>();
		// �����ӱ�VO����
		GeneralBillItemVO[] generalBVO = null;
		String sWhere = " dr = 0 and general_pk = '" + tmphvo.getGeneral_pk()
				+ "'";
		ArrayList list = (ArrayList) getQueryBO().retrieveByClause(
				TbOutgeneralHVO.class, sWhere);
		if (null != list && list.size() > 0) {
			TbOutgeneralHVO outhvo = (TbOutgeneralHVO) list.get(0);
			generalhvo = new GeneralBillHeaderVO();

			if (null != outhvo) {
				// �����ⵥ��ͷ��ֵ

				generalhvo.setPk_corp(outhvo.getComp());// ��˾����
				generalhvo.setAttributeValue("cothercorpid", outhvo.getComp());// �Է���˾
				// generalhvo.setCbiztypeid(outhvo.getCbiztype());// ҵ������
				generalhvo.setCbilltypecode("4I");// ��浥�����ͱ���
				generalhvo.setVbillcode(outhvo.getVbillcode());// ���ݺ�
				generalhvo.setDbilldate(outhvo.getDbilldate());// ��������
				generalhvo.setCwarehouseid(outhvo.getSrl_pk());// �ֿ�ID
				generalhvo.setAttributeValue("cotherwhid", outhvo.getSrl_pkr());// �����ֿ�
				generalhvo.setCdispatcherid(outhvo.getCdispatcherid());// �շ�����outhvo.getCdispatcherid()
				generalhvo.setCdptid(outhvo.getCdptid());// ����ID1021B110000000000BN9
				generalhvo.setCwhsmanagerid(outhvo.getCwhsmanagerid());// ���ԱID
				generalhvo.setCoperatorid(ClientEnvironment.getInstance()
						.getUser().getPrimaryKey());// �Ƶ���
				generalhvo.setAttributeValue("coperatoridnow",
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey());// ������
				generalhvo.setVdiliveraddress(outhvo.getVdiliveraddress());// ���˵�ַ
				generalhvo.setCbizid(outhvo.getCbizid());// ҵ��ԱID
				generalhvo.setVnote(outhvo.getVnote());// ��ע
				generalhvo.setFbillflag(2);// ����״̬
				generalhvo.setAttributeValue("clastmodiid", outhvo
						.getClastmodiid());// ����޸���
				generalhvo.setAttributeValue("tlastmoditime", outhvo
						.getTlastmoditime());// ����޸�ʱ��
				generalhvo
						.setAttributeValue("tmaketime", outhvo.getTmaketime());// �Ƶ�ʱ��
				sWhere = " dr = 0 and general_pk = '" + tmphvo.getGeneral_pk()
						+ "'";
				list = (ArrayList) getQueryBO().retrieveByClause(TbOutgeneralBVO.class,
						sWhere);
				if (null != list && list.size() > 0) {
					tmpbvo = new TbOutgeneralBVO[list.size()];
					tmpbvo = (TbOutgeneralBVO[]) list.toArray(tmpbvo);
					// �����帳ֵ
					for (int i = 0; i < tmpbvo.length; i++) {
						// ���ݱ��帽��--��λ
						LocatorVO locatorvo = new LocatorVO();
						locatorvo.setPk_corp(tmpbvo[i].getComp());
						boolean isBatch = false;
						GeneralBillItemVO generalb = new GeneralBillItemVO();
						generalb.setAttributeValue("pk_corp", outhvo.getComp());// ��˾

						generalb.setCinvbasid(tmpbvo[i].getCinventoryid());// ���ID
						generalb.setVbatchcode(tmpbvo[i].getLvbatchcode());// ���κ�
						// ��ѯ�������ں�ʧЧ����
						String sql = "select dproducedate ,dvalidate  from scm_batchcode where pk_invbasdoc='"
								+ tmpbvo[i].getCinventoryid()
								+ "' and vbatchcode='"
								+ tmpbvo[i].getLvbatchcode() + "' and dr=0";
						ArrayList batchList = (ArrayList) getQueryBO().executeQuery(
								sql, new ArrayListProcessor());
						if (null != batchList && batchList.size() > 0) {
							Object[] batch = (Object[]) batchList.get(0);
							// ��������
							if (null != batch[0] && !"".equals(batch[0]))
								generalb
										.setScrq(new UFDate(batch[0].toString()));
							// ʧЧ����
							if (null != batch[0] && !"".equals(batch[0]))
								generalb.setDvalidate(new UFDate(batch[1]
										.toString()));
							isBatch = true;
						}
						String pk_invmandoc = "select pk_invmandoc from bd_invmandoc  where pk_invbasdoc='"
								+ tmpbvo[i].getCinventoryid().trim()
								+ "' and pk_corp='"
								+ outhvo.getComp()
								+ "' and dr=0 ";
						ArrayList tmpList = (ArrayList) getQueryBO().executeQuery(
								pk_invmandoc, new ArrayListProcessor());
						if (null != tmpList && tmpList.size() > 0) {
							Object[] a = (Object[]) tmpList.get(0);
							generalb.setCinventoryid(WDSTools
									.getString_TrimZeroLenAsNull(a[0]));// �������ID
						} else {
							isBatch = false;
						}
						generalb.setDbizdate(outhvo.getDbilldate());// ҵ������
						generalb.setNshouldoutnum(tmpbvo[i].getNshouldoutnum());// Ӧ������
						generalb.setNshouldoutassistnum(tmpbvo[i]
								.getNshouldoutassistnum());// Ӧ��������
						generalb.setNoutnum(tmpbvo[i].getNoutnum());// ʵ������
						locatorvo.setNoutspacenum(tmpbvo[i].getNoutnum());
						generalb.setNoutassistnum(tmpbvo[i].getNoutassistnum());// ʵ��������
						locatorvo.setNoutspaceassistnum(tmpbvo[i]
								.getNoutassistnum());
						locatorvo.setCspaceid(tmpbvo[i].getCspaceid());// ��λID
						generalb.setCastunitid(tmpbvo[i].getCastunitid());// ��������λID
						generalb.setNprice(tmpbvo[i].getNprice());// ����
						generalb.setNmny(tmpbvo[i].getNmny());// ���
						generalb.setCsourcebillhid(tmpbvo[i]
								.getCsourcebillhid());// ��Դ���ݱ�ͷ���к�
						generalb.setCfirstbillhid(tmpbvo[i].getCfirstbillhid());// Դͷ���ݱ�ͷID
						generalb.setCfreezeid(tmpbvo[i].getCsourcebillhid());// ������Դ
						generalb.setCsourcebillbid(tmpbvo[i]
								.getCsourcebillbid());// ��Դ���ݱ������к�
						generalb.setCfirstbillbid(tmpbvo[i].getCfirstbillbid());// Դͷ���ݱ���ID
						// generalb.setCsourcetype();// ��Դ��������
						// generalb.setCfirsttype();// Դͷ��������
						generalb.setVsourcebillcode(tmpbvo[i]
								.getVsourcebillcode());// ��Դ���ݺ�
						generalb.setVfirstbillcode(tmpbvo[i]
								.getVsourcebillcode());// Դͷ���ݺ�
						generalb.setVsourcerowno(tmpbvo[i].getCrowno());// ��Դ�����к�
						generalb.setVfirstrowno(tmpbvo[i].getCrowno());// Դͷ�����к�
						generalb.setFlargess(tmpbvo[i].getFlargess());// �Ƿ���Ʒ
						generalb.setDfirstbilldate(tmpbvo[i]
								.getDfirstbilldate());// Դͷ�����Ƶ�����
						generalb.setCrowno(tmpbvo[i].getCrowno());// �к�
						generalb.setHsl(tmpbvo[i].getHsl());// ������
						LocatorVO[] locatorVO = new LocatorVO[] { locatorvo };
						generalb.setLocator(locatorVO);
						if (isBatch)
							// �������ӱ�����Ӷ���
							generalBVOList.add(generalb);
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

//	private int getRowNo() {
//		if (lineNo == -1)
//			lineNo = 10;
//		else
//			lineNo = lineNo + 10;
//		return lineNo;
//	}

	/**
	 * ���������20λ ������Сʱ����������4λ�����
	 * 
	 * @return
	 */
	private String getRandomNum() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssms");
		String tmp = format.format(new Date());
		tmp = tmp + Math.round((Math.random() * 1000000));
		tmp = tmp.substring(0, 20);
		return tmp;
	}

}
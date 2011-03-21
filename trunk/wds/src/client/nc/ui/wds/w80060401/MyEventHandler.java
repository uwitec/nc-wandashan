package nc.ui.wds.w80060401;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.wds.w80060401.Iw80060401;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.BusinessAction;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.businessaction.IBusinessController;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wds.w8000.CommonUnit;
import nc.ui.wds.w8000.W80060401Action;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.wds.w80060401.MyBillVO;
import nc.vo.wds.w80060401.TbShipentryBVO;
import nc.vo.wds.w80060401.TbShipentryVO;

/**
 * 
 *����¼�� 
 *
 * @author author xzs
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	protected boolean isAdd = false;
	MyClientUI myClientUI = null;
	Iw80060401 iw = null;
	// ���ݿ��ѯ����
	IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());
	private boolean isControl = false; // �Ƿ���Ȩ�޲�����ǰ����

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;
		String isType;
		try {
			isType = CommonUnit.getUserType(ClientEnvironment.getInstance()
					.getUser().getPrimaryKey());
			if (null != isType && isType.equals("2")) {
				isControl = true;
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected IBusinessController createBusinessAction() {
		// TODO Auto-generated method stub
		switch (getUIController().getBusinessActionType()) {
		case IBusinessActionType.PLATFORM:
			return new BusinessAction(getBillUI());
		case IBusinessActionType.BD:
			return new W80060401Action(getBillUI());
		default:
			return new BusinessAction(getBillUI());
		}
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		super.onBoQuery();
		showZeroLikeNull(false);
	}

	// ��֤����
	private boolean validate(AggregatedValueObject billVO) throws Exception {
		Object srl_pk = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("srl_pk").getValueObject();
		// �ֿ��Ƿ�Ϊ��
		if (null == srl_pk || "".equals(srl_pk)) {
			myClientUI.showWarningMessage("��ѡ��ֿ�");
			return false;
		}
		// �ƻ����
		Object se_type = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("se_type").getValueObject();
		if (null == se_type || "".equals(se_type)) {
			myClientUI.showWarningMessage("��ѡ��ƻ�����");
			return false;
		}
		// �Ƶ�����
		Object doperatordate = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("doperatordate").getValueObject();
		if (null == doperatordate || "".equals(doperatordate)) {
			myClientUI.showWarningMessage("��ѡ���Ƶ�����");
			return false;
		}
		// �Ƿ�ѡ��Ʒ
		TbShipentryBVO[] shbVO = (TbShipentryBVO[]) billVO.getChildrenVO();
		boolean isPlannum = false;
		// ѭ���ӱ�����
		if (null != shbVO && shbVO.length > 0) {
			for (int i = 0; i < shbVO.length; i++) {
				TbShipentryBVO shbvo = shbVO[i];
				// �ж��ӱ����Ƿ�������ƻ����ĵ�Ʒ
				if (null != shbvo.getSeb_plannum()
						&& !"".equals(shbvo.getSeb_plannum())) {
					isPlannum = true;
					break;
				}
			}
			if (!isPlannum) {
				myClientUI.showWarningMessage("��ѡ��һ�Ʒ����ƻ���");
				return false;
			}
		}
		// ����where����
		Date date = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		// ����ת��
		try {
			date = format.parse(doperatordate.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			myClientUI.showWarningMessage("�������ڲ��Ϸ�,����������");
			return false;
		}

		if (date != null) {
			// ת����ʼʱ��ͽ���ʱ��
			String[] dateformat = this.dateFormat(date);

			// ת��
			String begindate = dateformat[0];
			String enddate = dateformat[1];
			String sWhere = "";
			Object se_pk = getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("se_pk").getValueObject();
			// �жϲ������ �޸�
			if (null != se_pk && !"".equals(se_pk)) {
				sWhere = " dr = 0 and se_type = 0 and doperatordate between '"
						+ begindate + "' and '" + enddate + "' and se_pk <> '"
						+ se_pk + "' and srl_pk = '" + srl_pk + "'";
			} else { // ����
				sWhere = " dr = 0 and se_type = 0 and doperatordate between '"
						+ begindate + "' and '" + enddate + "' and srl_pk = '"
						+ srl_pk + "'";
			}
			ArrayList list = (ArrayList) iuap.retrieveByClause(
					TbShipentryVO.class, sWhere);

			// �ж�����ļƻ����
			if (Integer.parseInt(se_type.toString()) == 0) {
				// �ж� �����ǰ����ļƻ�������¼ƻ� �Ͳ�����¼���¼ƻ���
				if (null != list && list.size() >= 1) {
					myClientUI.showWarningMessage("���������¼ƻ�,���޸�¼������");
					return false;
				} else {
					List pman = new ArrayList();
					pman.add(getBillUI().getUserObject());
					pman.add(begindate);
					pman.add(enddate);
					pman.add(list);
					billVO = getBusinessAction().save(billVO,
							getUIController().getBillType(),
							_getDate().toString(), pman, billVO);
					return true;
				}
			} else {
				// ����ƻ����Ϊ׷�Ӽƻ����ͱ��������¼ƻ�
				if (null != list && list.size() >= 1) {
					List pman = new ArrayList();
					pman.add(getBillUI().getUserObject());
					pman.add(begindate);
					pman.add(enddate);
					pman.add(list);
					billVO = getBusinessAction().save(billVO,
							getUIController().getBillType(),
							_getDate().toString(), pman, billVO);
					// iw.insertShipertryVO(billVO, begindate, enddate, list);
					return true;
				} else {
					myClientUI.showWarningMessage("���»�û���¼ƻ�,����¼���¼ƻ�");
					return false;
				}
			}
		}
		return false;
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);
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
			if (getBillUI().isSaveAndCommitTogether())
				billVO = getBusinessAction().saveAndCommit(billVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), checkVO);
			else {

				// write to database
				Object srl_pk = getBillCardPanelWrapper().getBillCardPanel()
						.getHeadItem("srl_pk").getValueObject();
				// �ֿ��Ƿ�Ϊ��
				if (null == srl_pk || "".equals(srl_pk)) {
					myClientUI.showWarningMessage("��ѡ��ֿ�");
					return;
				}
				// �ƻ����
				Object se_type = getBillCardPanelWrapper().getBillCardPanel()
						.getHeadItem("se_type").getValueObject();
				if (null == se_type || "".equals(se_type)) {
					myClientUI.showWarningMessage("��ѡ��ƻ�����");
					return;
				}
				// �Ƶ�����
				Object doperatordate = getBillCardPanelWrapper()
						.getBillCardPanel().getHeadItem("doperatordate")
						.getValueObject();
				if (null == doperatordate || "".equals(doperatordate)) {
					myClientUI.showWarningMessage("��ѡ���Ƶ�����");
					return;
				}
				// �Ƿ�ѡ��Ʒ
				TbShipentryBVO[] shbVO = (TbShipentryBVO[]) billVO
						.getChildrenVO();
				boolean isPlannum = false;
				// ѭ���ӱ�����
				if (null != shbVO && shbVO.length > 0) {
					for (int i = 0; i < shbVO.length; i++) {
						TbShipentryBVO shbvo = shbVO[i];
						// �ж��ӱ����Ƿ�������ƻ����ĵ�Ʒ
						if (null != shbvo.getSeb_plannum()
								&& !"".equals(shbvo.getSeb_plannum())) {
							isPlannum = true;
							break;
						}
					}
					if (!isPlannum) {
						myClientUI.showWarningMessage("��ѡ��һ�Ʒ����ƻ���");
						return;
					}
				}

				// ����where����
				Date date = null;
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

				// ����ת��
				try {
					date = format.parse(doperatordate.toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					myClientUI.showWarningMessage("�������ڲ��Ϸ�,����������");
					return;
				}

				if (date != null) {
					// ת����ʼʱ��ͽ���ʱ��
					String[] dateformat = this.dateFormat(date);

					// ת��
					String begindate = dateformat[0];
					String enddate = dateformat[1];
					String sWhere = "";
					Object se_pk = getBillCardPanelWrapper().getBillCardPanel()
							.getHeadItem("se_pk").getValueObject();
					// �жϲ������ �޸�
					if (null != se_pk && !"".equals(se_pk)) {
						sWhere = " dr = 0 and se_type = 0 and doperatordate between '"
								+ begindate
								+ "' and '"
								+ enddate
								+ "' and se_pk <> '"
								+ se_pk
								+ "' and srl_pk = '" + srl_pk + "'";
					} else { // ����
						sWhere = " dr = 0 and se_type = 0 and doperatordate between '"
								+ begindate
								+ "' and '"
								+ enddate
								+ "' and srl_pk = '" + srl_pk + "'";
					}
					ArrayList list = (ArrayList) iuap.retrieveByClause(
							TbShipentryVO.class, sWhere);

					// �ж�����ļƻ����
					if (Integer.parseInt(se_type.toString()) == 0) {
						// �ж� �����ǰ����ļƻ�������¼ƻ� �Ͳ�����¼���¼ƻ���
						if (null != list && list.size() >= 1) {
							myClientUI.showWarningMessage("���������¼ƻ�,���޸�¼������");
							return;
						} else {
							List pman = new ArrayList();
							pman.add(getBillUI().getUserObject());
							pman.add(begindate);
							pman.add(enddate);
							pman.add(list);
							billVO = getBusinessAction().save(billVO,
									getUIController().getBillType(),
									_getDate().toString(), pman, billVO);
						}
					} else {
						// ����ƻ����Ϊ׷�Ӽƻ����ͱ��������¼ƻ�
						if (null != list && list.size() >= 1) {
							List pman = new ArrayList();
							pman.add(getBillUI().getUserObject());
							pman.add(begindate);
							pman.add(enddate);
							pman.add(list);
							billVO = getBusinessAction().save(billVO,
									getUIController().getBillType(),
									_getDate().toString(), pman, billVO);
							// iw.insertShipertryVO(billVO, begindate, enddate,
							// list);
						} else {
							myClientUI.showWarningMessage("���»�û���¼ƻ�,����¼���¼ƻ�");
							return;
						}
					}
				}
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
	}

	/**
	 * ����ʱ��ת�� ��ʼʱ��ͽ���ʱ��
	 * 
	 * @param date
	 *            ʱ�����
	 * @return ���鿪ʼʱ��ͽ���ʱ��
	 * @throws ParseException
	 */
	private String[] dateFormat(Date date) throws ParseException {
		String[] dateformat = new String[2];
		SimpleDateFormat format = new SimpleDateFormat("dd");
		int day = Integer.parseInt(format.format(date)); // ��ǰ��
		format = new SimpleDateFormat("MM");
		int month = Integer.parseInt(format.format(date)); // ��ǰ��
		format = new SimpleDateFormat("yyyy");
		int year = Integer.parseInt(format.format(date)); // ��ǰ��
		// �ж� �����ǰ����С��21
		if (day < 21) {
			// �·ݼ�һ
			month = month - 1;
		}
		format = new SimpleDateFormat("yyyy-MM-dd");
		// ƴװ��ʼʱ��
		if (month == 0) {
			month = 12;
			year = year - 1;
		}
		String begindate = year + "-" + month + "-21";
		// ����ʱ��
		if (month + 1 == 13) {
			month = 1;
			year = year + 1;
		} else {
			month = month + 1;
		}
		String enddate = year + "-" + month + "-20";
		format = new SimpleDateFormat("yyyy-MM-dd");
		dateformat[0] = format.format(format.parse(begindate));
		dateformat[1] = format.format(format.parse(enddate));
		return dateformat;
	}

	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		if (isControl) {
			// ����û�����ݻ��������ݵ���û��ѡ���κ���
			if (getBufferData().getCurrentVO() == null)
				return;

			if (MessageDialog.showOkCancelDlg(getBillUI(), nc.ui.ml.NCLangRes
					.getInstance().getStrByID("uifactory",
							"UPPuifactory-000064")/* @res "����ɾ��" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000065")/* @res "�Ƿ�ȷ��ɾ���û�������?" */
					, MessageDialog.ID_CANCEL) != UIDialog.ID_OK)
				return;

			AggregatedValueObject modelVo = getBufferData().getCurrentVO();

			AggregatedValueObject item = getBillUI().getBufferData()
					.getCurrentVO();

			String pk = ((TbShipentryVO) item.getParentVO()).getSe_pk();
			String sql = "select count(se_pk) from tb_fydnew where dr = 0 and se_pk = '"
					+ pk + "'";

			ArrayList list = (ArrayList) iuap.executeQuery(sql,
					new ArrayListProcessor());
			Object[] results = (Object[]) list.get(0);
			if (null != results[0] && !"".equals(results[0])
					&& Integer.parseInt(results[0].toString()) > 0) {
				myClientUI.showErrorMessage("ɾ��ʧ��!�õ����ѽ��в��,����ɾ����ֵ���");
				return;
			}
			boolean isupdate = false;
			ArrayList<TbShipentryBVO> shibList = new ArrayList<TbShipentryBVO>();
			// ¼������
			int type = ((TbShipentryVO) item.getParentVO()).getSe_type();
			// ����վ
			String srl_pk = ((TbShipentryVO) item.getParentVO()).getSrl_pk();
			// ¼������
			UFDate datets = ((TbShipentryVO) item.getParentVO())
					.getDoperatordate();
			String[] dateformat = this.dateFormat(datets.toDate());
			// ������¼ƻ� ���в�ѯ����û��׷�Ӽƻ�
			if (type == 0) {
				sql = "select count(se_pk) from tb_shipentry where dr = 0 and se_type = 1 and  doperatordate between '"
						+ dateformat[0]
						+ "' and '"
						+ dateformat[1]
						+ "' and srl_pk = '" + srl_pk + "'";
				list = (ArrayList) iuap.executeQuery(sql,
						new ArrayListProcessor());
				results = (Object[]) list.get(0);
				if (null != results[0] && !"".equals(results[0])
						&& Integer.parseInt(results[0].toString()) > 0) {
					myClientUI.showErrorMessage("ɾ��ʧ��!������׷�Ӽƻ�,����ɾ��׷�Ӽƻ�");
					return;
				}
			} else if (type == 1) { // ׷�Ӽƻ� ���ж��Ƿ��й�һ�β�֣��й�һ�ζ�������ɾ��
				String strWhere = " dr = 0  and se_type = 0 and "
						+ " doperatordate between '" + dateformat[0]
						+ "' and '" + dateformat[1] + "'";
				// ��ѯ����ǰ׷�Ӽƻ����¼ƻ�
				list = (ArrayList) iuap.retrieveByClause(TbShipentryVO.class,
						strWhere);
				if (null != list && list.size() > 0) {
					// ���ݲ�ѯ�������¼ƻ�������ѯ�Ƿ��й���֣��й��Ͳ�����ɾ���ˡ���Ϊ����ɾ������׷�Ӽƻ�����̨��������ݶԲ��ϣ���ֹ��ֺ���ɾ��
					String se_pk = ((TbShipentryVO) list.get(0)).getSe_pk();
					sql = "select count(se_pk) from tb_fydnew where dr = 0 and se_pk = '"
							+ se_pk + "'";
					list = (ArrayList) iuap.executeQuery(sql,
							new ArrayListProcessor());
					results = (Object[]) list.get(0);
					if (null != results[0] && !"".equals(results[0])
							&& Integer.parseInt(results[0].toString()) > 0) {
						myClientUI.showErrorMessage("ɾ��ʧ��!�õ����ѽ��в��,����ɾ����ֵ���");
						return;
					}
					strWhere = " dr = 0 and se_pk = '" + se_pk + "'";
					list = (ArrayList) iuap.retrieveByClause(
							TbShipentryBVO.class, strWhere);

					if (null != list && list.size() > 0) {
						TbShipentryBVO[] shibVO = (TbShipentryBVO[]) item
								.getChildrenVO();
						if (null != shibVO && shibVO.length > 0) {
							for (int i = 0; i < shibVO.length; i++) {
								for (int j = 0; j < list.size(); j++) {
									TbShipentryBVO shib = (TbShipentryBVO) list
											.get(j);
									// ���������Ʒ���
									if (shibVO[i].getPk_invbasdoc().equals(
											shib.getPk_invbasdoc())) {
										if (null != shibVO[i].getSeb_plannum()
												&& !"".equals(shibVO[i]
														.getSeb_plannum())) {
											// ����ʣ���� = ��ǰʣ���� - ��ѡ��ɾ���ļƻ���
											shib
													.setSeb_surplus(new UFDouble(
															shib
																	.getSeb_surplus()
																	.toDouble()
																	- shibVO[i]
																			.getSeb_plannum()
																			.toDouble()));
											shib.setDr(1); // ����ɾ��
											shibList.add(shib);// �������Ҫ���µļ��ϸ�ֵ
											isupdate = true;
										}
									}
								}
							}
						}
					}
				}
			}
			// // ���ýӿ�ɾ������
			// iw.deleteShipertryVO(item, shibList, isupdate);
			List pman = new ArrayList();
			pman.add(getBillUI().getUserObject());
			pman.add(shibList);
			pman.add(isupdate);
			getBusinessAction().delete(modelVo,
					getUIController().getBillType(),
					getBillUI()._getDate().toString(), pman);

			if (PfUtilClient.isSuccess()) {
				// �����������
				getBillUI().removeListHeadData(getBufferData().getCurrentRow());
				if (getUIController() instanceof ISingleController) {
					ISingleController sctl = (ISingleController) getUIController();
					if (!sctl.isSingleDetail())
						getBufferData().removeCurrentRow();
				} else {
					getBufferData().removeCurrentRow();
				}

			}
			if (getBufferData().getVOBufferSize() == 0)
				getBillUI().setBillOperate(IBillOperate.OP_INIT);
			else
				getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		} else {
			getBillUI().showErrorMessage("����ʧ��,��ǰ��¼��û�н�����Ա��");
			return;
		}
	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		if (isControl) {
			if (CommonUnit.getSotckIsTotal(CommonUnit
					.getStordocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey()))) {
				super.onBoAdd(bo);
				getBufferData().clear();
				Object[] values = new Object[] { ClientEnvironment
						.getInstance().getUser().getPrimaryKey() };
				getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
						"voperatorid").setValue(values[0]);
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
						"doperatordate").setValue(_getDate());

				Iw80060401 iw = (Iw80060401) NCLocator.getInstance().lookup(
						Iw80060401.class.getName()); // ���ýӿ�
				TbShipentryBVO[] shvo = null;// ������������
				try {
					shvo = iw.queryShipentryBVO(""); // ͨ���ӿڻ�ȡ����
					if (shvo != null) {
						MyBillVO myBillVO = (MyBillVO) this
								// ��ȡ�����е�UI
								.getBillCardPanelWrapper().getBillCardPanel()
								.getBillValueChangeVO(MyBillVO.class.getName(),
										TbShipentryVO.class.getName(),
										TbShipentryBVO.class.getName());
						// myBillVO.setParentVO(new TbShipentryVO());
						myBillVO.setChildrenVO(shvo); // Ϊ���帳ֵ ���ı�������
						getBufferData().addVOToBuffer(myBillVO); // Ϊ����UI�������
						updateBuffer(); // ����

						getBillUI().setBillOperate( // ��Ϊ����Զ��尴ť��ͻ��е�������ťģʽ
								// ��������״̬
								nc.ui.trade.base.IBillOperate.OP_EDIT);
						showZeroLikeNull(false);
					}

				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				isAdd = true;
			} else {
				getBillUI().showErrorMessage("����ʧ��,��ǰ��¼��û��Ȩ�޽��в���");
				return;
			}
		} else {
			getBillUI().showErrorMessage("����ʧ��,��ǰ��¼��û�н�����Ա��");
			return;
		}

	}

	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		if (isAdd) { // �ж��Ƿ�Ϊ���Ӻ� ����� �������
			getBufferData().clear();
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
			getBillUI().initUI();
			isAdd = false;
		} else {
			super.onBoCancel();
		}
	}

	// 0��ʾ
	public void showZeroLikeNull(boolean bShowZeroLikeNull) {
		BillRendererVO vo = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getRendererVO();
		vo.setShowZeroLikeNull(bShowZeroLikeNull);
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
				.setShowFlags(vo);
	}

	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		super.onBoEdit();
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("srl_pk")
				.setEnabled(false);
		showZeroLikeNull(false);
	}
}
package nc.ui.wds.w80060406;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.wds.w80060406.Iw80060406;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.query.INormalQuery;
import nc.ui.wds.w8000.CommonUnit;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.wds.w80060406.MyBillVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;

/**
 * 
 *  ���˲�� 
 * @author author xzs
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	MyClientUI myClientUI = null;
	boolean isEdit = false;
	private String dhzPK = null;
	private boolean isControl = false; // �Ƿ���Ȩ�޲�����ǰ����

	public String getDhzPK() {
		return dhzPK;
	}

	public void setDhzPK(String dhzPK) {
		this.dhzPK = dhzPK;
	}

	public MyEventHandler(MyClientUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = billUI;
		String isType;
		try {
			isType = CommonUnit.getUserType(ClientEnvironment.getInstance()
					.getUser().getPrimaryKey());
			if ((null != isType && isType.equals("2")) ||(null != isType && isType.equals("3"))) {
				isControl = true;
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean validate() {
		// ��ȡ��ͷ����
		String stratts = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("fyd_begints").getValueObject().toString();// ��ȡ��ʼʱ��
		String endts = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("fyd_endts").getValueObject().toString(); // ��ȡ����ʱ��
		Object fhc = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"srl_pk").getValueObject(); // ��ȡ����վ
		Object dhc = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"srl_pkr").getValueObject(); // ��ȡ����վ
		// �����жϵ����Ի�����ʾ
		if (stratts == null || "".equals(stratts)) {
			getBillUI().showWarningMessage("��ѡ��ʼʱ��");
		} else if (endts == null || "".equals(endts)) {
			getBillUI().showWarningMessage("��ѡ�����ʱ��");
		} else if (fhc == null || "".equals(fhc)) {
			getBillUI().showWarningMessage("��ѡ�񷢻�վ");
		} else if (dhc == null || "".equals(dhc)) {
			getBillUI().showWarningMessage("��ѡ�񵽻�վ");
		} else if (fhc.toString().equals(dhc)) {
			getBillUI().showWarningMessage("����վ�뵽��վ��ͬ,������ѡ��");
		} else {
			return true;
		}
		return false;
	}

	// �Զ��尴ť ��ѯ��ϸ ����¼�
	protected void oncxmx() throws Exception {
		getBufferData().clear();
		if (this.validate()) {
			// ��ȡ��ͷ����
			String stratts = getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("fyd_begints").getValueObject().toString();// ��ȡ��ʼʱ��
			String endts = getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("fyd_endts").getValueObject().toString(); // ��ȡ����ʱ��
			Object fhc = getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("srl_pk").getValueObject(); // ��ȡ����վ
			Object dhc = getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("srl_pkr").getValueObject(); // ��ȡ����վ
			this.setDhzPK(dhc.toString());
			String strWhere = " and b.srl_pk= '"
					+ dhc.toString() // ƴװWhere����
					+ "' and b.doperatordate between  '" + stratts + "' and '"
					+ endts + "'";
			Iw80060406 iw = (Iw80060406) NCLocator.getInstance().lookup(
					Iw80060406.class.getName()); // ���ýӿ�
			TbFydmxnewVO[] shvo = null;// ������������
			getBillCardPanelWrapper().getBillCardPanel().getBillData()
					.setBodyValueVO(new TbFydmxnewVO[0]);
			try {
				shvo = iw.queryFydmxnewVO(strWhere, dhc.toString(), stratts,
						endts, fhc.toString()); // ͨ���ӿڻ�ȡ����
				if (shvo != null) {
					MyBillVO myBillVO = (MyBillVO) this
							// ��ȡ�����е�UI
							.getBillCardPanelWrapper().getBillCardPanel()
							.getBillValueVO(MyBillVO.class.getName(),
									TbFydnewVO.class.getName(),
									TbFydmxnewVO.class.getName());
					TbFydnewVO fydVO = (TbFydnewVO) myBillVO.getParentVO();
					fydVO.setSe_pk(shvo[0].getCfd_dw());
					myBillVO.setParentVO(fydVO); // Ϊ��ͷ��ֵ
					myBillVO.setChildrenVO(shvo); // Ϊ���帳ֵ ���ı�����������
					getBufferData().addVOToBuffer(myBillVO); // Ϊ����UI�������
					updateBuffer(); // ����
					getBillUI().setBillOperate( // ��Ϊ����Զ��尴ť��ͻ��е�������ťģʽ ��������״̬
							nc.ui.trade.base.IBillOperate.OP_EDIT);
				} else {
					getBillUI().showWarningMessage("�õ���վ��ǰ��û�мƻ�");
					return;
				}

			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// ����ִ�й�ʽ
			getBillCardPanelWrapper().getBillCardPanel().execTailLoadFormulas();
			getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
		}
	}

	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		if (isControl) {
			super.onBoEdit();

			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("srl_pk")
					.setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("srl_pkr")
					.setEnabled(false);

			getButtonManager().getButton(
					nc.ui.wds.w800604.tcButtun.ITcButtun.cxmx)
					.setEnabled(false);

			// ����ִ�й�ʽ
			getBillCardPanelWrapper().getBillCardPanel().execTailLoadFormulas();
			getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
			// �����޸�״̬
			isEdit = true;
		} else {
			getBillUI().showErrorMessage("����ʧ��,��ǰ��¼��û�н�����Ա��");
			return;
		}
	}

	/*
	 * ���ɶ�����
	 */
	private void getOrderNo() throws Exception {

		Object fhz = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"srl_pk").getValueObject(); // ��ȡ����վ
		Object dhz = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"srl_pkr").getValueObject(); // ��ȡ����վ

		String fhzCode = ""; // ����վ����
		String dhzCode = ""; // ����վ����
		String top = ""; // ������ͷ��λ
		String ddh = null; // ������ɵĶ�����
		String d = ""; // �������м�
		int num = 0; // �����ź�λ

		// ��ȡ�������ݿ����
		IUAPQueryBS IUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		//
		// // �жϷ���վ�͵���վ�Ƿ�Ϊ��
		// if ((fhz != null && !"".equals(fhz))
		// && (dhz != null && !"".equals(dhz))) {
		// // ��ѯ���ݿ��еĽ�� �������ObjectΪ���������
		// ArrayList list = (ArrayList) IUAPQueryBS
		// .executeQuery(
		// "select fyd_ddh from (select fyd_ddh from tb_fydnew where dr = 0 and
		// srl_pkr ='"
		// + dhz
		// + "' and srl_pk ='"
		// + fhz
		// + "' order by fyd_ddh desc) where rownum = 1 ",
		// new ArrayListProcessor());
		// // �жϽ���Ƿ�Ϊ��
		// if (list != null && list.size() > 0) {
		// Object a[] = (Object[]) list.get(0);
		// if (a != null && a.length > 0 && a[0] != null) {
		// // �����Ϊ�ս��и�ֵ
		// ddh = a[0].toString();
		// }
		// }
		// }
		//
		// // �жϻ�õĶ������Ƿ�Ϊ��
		// if (ddh != null && !"".equals(ddh)) {
		// // �����Ϊ�ս����ַ�����ȡ����ȡ��������"-"������ֵ
		// String n = ddh.substring(ddh.lastIndexOf("-") + 1, ddh.length())
		// .toString();
		// num = Integer.parseInt(n.trim());
		// }
		// // Ϊ�����ź�λ����1
		// num = num + 1;

		// ��ѯ����վCode ���� �ɶ� "CD"
		if (fhz != null && !fhz.equals("")) {
			ArrayList list = (ArrayList) IUAPQueryBS
					.executeQuery(
							"select distinct pk_stordoc,storcode  from tb_storareacl where dr = 0 and pk_stordoc = '"
									+ fhz.toString() + "'",
							new ArrayListProcessor());
			if (list != null && list.size() > 0) {
				// �ж��Ƿ�Ϊ��
				Object a[] = (Object[]) list.get(0);
				if (a != null && a.length > 0 && a[0] != null) {
					// �����Ϊ�ս��и�ֵ
					fhzCode = a[1].toString();
					// ���н�ȡ ����"CD" ��ȡ��һλ"C"ת����д
					fhzCode = fhzCode.substring(0, 1).toUpperCase();
				}

			}
		}
		// ��ѯ����վCode
		if (dhz != null && !dhz.equals("")) {
			ArrayList list = (ArrayList) IUAPQueryBS
					.executeQuery(
							"select distinct pk_stordoc,storcode  from tb_storareacl where dr = 0 and  pk_stordoc = '"
									+ dhz.toString() + "'",
							new ArrayListProcessor());
			if (list != null && list.size() > 0) {
				Object a[] = (Object[]) list.get(0);
				if (a != null && a.length > 0 && a[0] != null) {
					// �����Ϊ�ս��и�ֵ
					dhzCode = a[1].toString();
					dhzCode = dhzCode.substring(0, 1).toUpperCase();
				}
			}
		}
		String tmpddh = CommonUnit.getBillCode("4206", ClientEnvironment
				.getInstance().getUser().getPrimaryKey(), "", "");
		// ������װ ������ͷ��λ
		top = fhzCode + "Y" + dhzCode;
		Object isbig = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isbig").getValueObject();
		if(null!=isbig){
			if(Boolean.parseBoolean(isbig.toString())){
				ddh = top + "-G" + tmpddh.substring(0, 6) + "-"
				+ tmpddh.substring(6, tmpddh.length());
			}else{
				ddh = top + "-" + tmpddh.substring(0, 6) + "-"
				+ tmpddh.substring(6, tmpddh.length());
			}
		} 
		
		// SimpleDateFormat sdFormat = new SimpleDateFormat("yy");
		// // ��װ��
		// d = sdFormat.format(new Date());
		// // ��װ���C
		// // d = d + "C";
		// sdFormat = new SimpleDateFormat("MM");
		// // ��װ�·�
		// d = d + sdFormat.format(new Date());
		// sdFormat = new SimpleDateFormat("dd");
		// // ��װ��
		// d = d + sdFormat.format(new Date());
		// // �����װ������
		// ddh = top + "-" + d + "-" + num;
		// ���ö�����
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fyd_ddh")
				.setValue(ddh);
		// ���õ��ݺ�
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno")
				.setValue(ddh);
		getBillCardPanelWrapper().getBillCardPanel().setTailItem(
				"fyd_approstate", 0); // ��������״̬
	}

	@Override
	protected void onBoSave() throws Exception {
		this.onSave();
	}

	private void onSave() throws Exception {
		if (this.validate()) {
			
			AggregatedValueObject mybillVO = getBillUI().getVOFromUI();
			TbFydmxnewVO[] fydmxVO = (TbFydmxnewVO[]) mybillVO.getChildrenVO();
			List<TbFydmxnewVO> fydmxVOList = new ArrayList<TbFydmxnewVO>();
			boolean isPlannum = false;
			// ѭ���ӱ�����
			if (null != fydmxVO && fydmxVO.length > 0) {
				for (int i = 0; i < fydmxVO.length; i++) {
					TbFydmxnewVO fydmxvo = fydmxVO[i];
					// �ж��ӱ����Ƿ�������ƻ����ĵ�Ʒ
					if (null != fydmxvo.getCfd_xs()
							&& !"".equals(fydmxvo.getCfd_xs())) {
						if (fydmxvo.getCfd_xs().toDouble() > 0) {
							isPlannum = true;
							fydmxVOList.add(fydmxvo);
						}
					}
				}
				if (!isPlannum) {
					myClientUI.showWarningMessage("��ѡ��һ�Ʒ��������");
					return;
				}
			} else {
				myClientUI.showWarningMessage("��û�в�ѯ��Ʒ,����������������ѯ��ϸ");
				return;
			}
			// �����ж��Ƿ�Ϊ�޸�״̬
			if (!isEdit) {
				getOrderNo();
			}
			// ���õ�������
			getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("billtype").setValue(new Integer(0));
			TbFydmxnewVO[] fydmxvo = null;
			if (fydmxVOList.size() > 0) {
				fydmxvo = new TbFydmxnewVO[fydmxVOList.size()];
				fydmxvo = fydmxVOList.toArray(fydmxvo);
			}
			AggregatedValueObject billVO = getBillUI().getChangedVOFromUI();
			// �����ж��Ƿ�Ϊ�޸�״̬
			if (!isEdit) {
				if (!this.getDhzPK().equals(
						((TbFydnewVO) billVO.getParentVO()).getSrl_pkr())) {
					myClientUI.showWarningMessage("���޸��˵���վ,�����²�ѯ");
					return;
				}
			}

			billVO.setChildrenVO(fydmxvo);
			setTSFormBufferToVO(billVO);
			AggregatedValueObject checkVO = billVO;
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
					&& (billVO.getChildrenVO() == null || billVO
							.getChildrenVO().length == 0)) {
				isSave = false;
			} else {
				if (getBillUI().isSaveAndCommitTogether())
					billVO = getBusinessAction().saveAndCommit(billVO,
							getUIController().getBillType(),
							_getDate().toString(), getBillUI().getUserObject(),
							checkVO);
				else

					// write to database
					billVO = getBusinessAction().save(billVO,
							getUIController().getBillType(),
							_getDate().toString(), getBillUI().getUserObject(),
							checkVO);
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
			// ����ִ�й�ʽ
			getBillCardPanelWrapper().getBillCardPanel().execTailLoadFormulas();
			getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
			// ���ð�ť״̬
			getButtonManager().getButton(
					nc.ui.wds.w800604.tcButtun.ITcButtun.cxmx)
					.setEnabled(false);
			// ���÷��޸�״̬
			isEdit = false;
			this.onBoRefresh();
		}
	}

	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		if (isControl) {
			super.onBoDelete();
			if (null != getBufferData().getCurrentVO()) {
				TbFydnewVO fydnewvo = (TbFydnewVO) getBufferData()
						.getCurrentVO().getParentVO();
				if (null != fydnewvo) {
					int tmp = fydnewvo.getFyd_approstate();
					// �ж��Ƿ�Ϊ 1 �� 0 ������ 1 ����ͨ�� 2 ��ͨ��
					if (tmp == 1) {
						// ���ð�ť״̬
						getButtonManager().getButton(IBillButton.Edit)
								.setEnabled(false);
						getButtonManager().getButton(IBillButton.Delete)
								.setEnabled(false);
					} else {
						getButtonManager().getButton(IBillButton.Edit)
								.setEnabled(true);
						getButtonManager().getButton(IBillButton.Delete)
								.setEnabled(true);
					}
				}
			}
		} else {
			getBillUI().showErrorMessage("����ʧ��,��ǰ��¼��û�н�����Ա��");
			return;
		}
	}

	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		isEdit = false;
		// ���ð�ť״̬
		getButtonManager().getButton(nc.ui.wds.w800604.tcButtun.ITcButtun.cxmx)
				.setEnabled(false);
		super.onBoCancel();
		this.getBillUI().initUI();
		// ����ִ�й�ʽ
		getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
		getBillCardPanelWrapper().getBillCardPanel().execTailLoadFormulas();
	}

	/**
	 * ��ȡ��ʼ���ںͽ������� 21�ŵ��¸��µ�20��
	 * 
	 * @param type
	 *            ���� true ��ʼ���� false ��������
	 * @return ����
	 * @throws ParseException
	 */
	private String getStarOrEndDate(boolean type) throws ParseException {
		Calendar calen = Calendar.getInstance();

		int intDay = calen.get(Calendar.DATE);
		int intMonth = calen.get(Calendar.MONTH) + 1;
		int intYear = calen.get(Calendar.YEAR);
		String tmp = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (type) {
			if (intDay >= 21) {
				tmp = intYear + "-" + intMonth + "-" + 21;
				return format.format(format.parse(tmp));
			} else {
				intMonth = intMonth - 1;
				if (intMonth == 0) {
					intYear = intYear - 1;
					intMonth = 12;
				}
				tmp = intYear + "-" + intMonth + "-" + 21;
				return format.format(format.parse(tmp));
			}
		} else {
			if (intDay >= 21) {
				intMonth = intMonth + 1;
				if (intMonth == 13) {
					intYear = intYear + 1;
					intMonth = 1;
				}
				tmp = intYear + "-" + intMonth + "-" + 20;
				return format.format(format.parse(tmp));

			} else {
				tmp = intYear + "-" + intMonth + "-" + 20;
				return format.format(format.parse(tmp));
			}
		}

	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		if (isControl) {
			// ���ð�ť״̬
			getButtonManager().getButton(
					nc.ui.wds.w800604.tcButtun.ITcButtun.cxmx).setEnabled(true);
			super.onBoAdd(bo);
			String strDate = this.getStarOrEndDate(true);
			String endDate = this.getStarOrEndDate(false);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"fyd_begints").setValue(strDate);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"fyd_endts").setValue(endDate);
			// �����Ƶ�ʱ��
			getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
					"dmakedate").setValue(_getDate());
			String pk_stockdoc = CommonUnit.getStordocName(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());

			// ���÷���վ
			getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
					"srl_pk").setValue(pk_stockdoc);
			// getBillUI().setCardUIState();
			// ����ִ�й�ʽ
			getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
			getBillCardPanelWrapper().getBillCardPanel().execTailLoadFormulas();
		} else {
			getBillUI().showErrorMessage("����ʧ��,��ǰ��¼��û�н�����Ա��");
			return;
		}
	}

	@Override
	protected void onBoCard() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCard();
		// ����ִ�й�ʽ
		getBillCardPanelWrapper().getBillCardPanel().execTailLoadFormulas();
		getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
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

		if (getHeadCondition() != null)
			strWhere = strWhere + " and " + getHeadCondition();
		// ��������ֱ�Ӱ����ƴ�õĴ��ŵ�StringBuffer�ж���ȥ�Ż�ƴ���Ĺ���
		sqlWhereBuf.append(strWhere);
		return true;
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ

		// strWhere.append(" tb_fydnew.billtype = 0 and ");

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();

		// ����ִ�й�ʽ
		getBillCardPanelWrapper().getBillCardPanel().execTailLoadFormulas();
	}

	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
		// ����ִ�й�ʽ
		getBillCardPanelWrapper().getBillCardPanel().execTailLoadFormulas();
	}

}
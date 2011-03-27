package nc.ui.wds.w8004040204;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.wds.w8004040204.Iw8004040204;
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
import nc.ui.wds.w8000.W8004040204Action;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;
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
import nc.vo.to.pub.ConstVO;
import nc.vo.wds.pub.WDSTools;
import nc.vo.wds.w8004040204.MyBillVO;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;
import nc.vo.wds.w80060604.SoSaleVO;
import nc.vo.wds.w80060604.SoSaleorderBVO;
import nc.vo.wds.w80060608.IcGeneralBVO;
import nc.vo.wds.w80060608.IcGeneralHVO;

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

	private FydnewDlg fydnewdlg = null; // ��ѯ��������

	boolean isAdd = false;

	boolean opType = false; // �Ƿ���Ʒ

	private List hiddenList = null;
	private String pk_stock = ""; // ��ǰ��¼�߶�Ӧ�Ĳֿ�����
	IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());

	private Integer isControl = -1; // �Ƿ���Ȩ�޲�����ǰ���� 1Ϊ����ǩ�ֵ��û� 0 �޸Ĳ��� 2 ���Խ��г���
									// 3����Ȩ��

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
			if (null != isType && isType.equals("0")) {
				isStock = CommonUnit.getSotckIsTotal(pk_stock);
				pkList = CommonUnit.getInvbasdoc_Pk(ClientEnvironment
						.getInstance().getUser().getPrimaryKey());
				isControl = 2;
			} else if (null != isType && isType.equals("1")) {
				isControl = 1;
				// getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				// myClientUI.updateButtons();
			} else if (null != isType && isType.equals("2")) {
				isControl = 0;
			} else if (null != isType && isType.equals("3")) {
				isControl = 3;
				isStock = CommonUnit.getSotckIsTotal(pk_stock);
				pkList = CommonUnit.getInvbasdoc_Pk(ClientEnvironment
						.getInstance().getUser().getPrimaryKey());
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
	}

	protected IBusinessController createBusinessAction() {
		// TODO Auto-generated method stub
		switch (getUIController().getBusinessActionType()) {
		case IBusinessActionType.PLATFORM:
			return new BusinessAction(getBillUI());
		case IBusinessActionType.BD:
			return new W8004040204Action(getBillUI());
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
		// ���˿ƻ�������
		if (isControl == 0 || isControl == 3) {
			super.onBoEdit();
			// ��������ָ����ť���ã��޸�״̬
			isAdd = false;
			getButtonManager().getButton(ISsButtun.tpzd).setEnabled(true);
			myClientUI.updateButtons();
		} else {
			myClientUI.showErrorMessage("��û��Ȩ��");
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
		StringBuffer strWhere = new StringBuffer();
		if (isControl == -1) {
			myClientUI.showErrorMessage("��û��Ȩ�޲���");
			return;
		}
		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ

		int tmp = strWhere.indexOf("tb_outgeneral_h.vbilltype");
		StringBuffer strtmp = new StringBuffer(strWhere.substring(tmp, strWhere
				.length()));
		StringBuffer a = new StringBuffer(strWhere.toString().toLowerCase()
				.substring(tmp, strWhere.length()));

		int tmpnum = a.indexOf("and");
		// ���е�������ת��
		StringBuffer b = new StringBuffer(a.substring(0, tmpnum));
		for (int i = 28; i < b.length(); i++) {
			String stra = b.substring(i, i + 1);
			if (stra.equals("0"))
				b.replace(i, i + 1, "1");
			if (stra.equals("1"))
				b.replace(i, i + 1, "3");
			if (stra.equals("2"))
				b.replace(i, i + 1, "4");
		}
		strtmp.replace(0, tmpnum, b.toString());
		strWhere.replace(tmp, strWhere.length(), strtmp.toString());

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
				strWhere.append(" and srl_pk = '" + pk_stock + "' ");
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
			// ��Ϣ�Ʋ�ѯȫ��
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

	@Override
	protected void onBoSave() throws Exception {
		// ��ȡ��ǰҳ����VO
		AggregatedValueObject myBillVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(myBillVO);
		TbOutgeneralHVO generalh = (TbOutgeneralHVO) myBillVO.getParentVO();
		TbOutgeneralBVO[] generalb = (TbOutgeneralBVO[]) myBillVO
				.getChildrenVO();
		// ��֤�����Ƿ�������
		if (null == generalb || generalb.length < 0) {
			myClientUI.showErrorMessage("����ʧ��,����Ȩ����");
			return;
		}
		// �˵���Object,���湲����ֵ��[0]true or false falseΪ�����˵�ʧ��
		// [1]�˵�������󼯺�List [2]�˵��ӱ���󼯺�List
		Object[] obj = null;
		if (!validate(generalb)){
				return;
		}
		//�����˵�
		obj = insertFyd(generalh, generalb);
		TbOutgeneralHVO tmpgeneralh = null;
		// ������Դ���ݺŲ�ѯ�Ƿ�����������
		String strWhere = " dr = 0 and vsourcebillcode = '"
				+ generalh.getVsourcebillcode() + "' and csourcebillhid = '"
				+ generalh.getCsourcebillhid() + "'";
		ArrayList tmpList = (ArrayList) iuap.retrieveByClause(
				TbOutgeneralHVO.class, strWhere);
		if (null != tmpList && tmpList.size() > 0) {
			tmpgeneralh = (TbOutgeneralHVO) tmpList.get(0);
		}
		// �õ����й������¼
		if (null != tmpgeneralh) {
			// �ѱ�ͷ�滻
			generalh = tmpgeneralh;
		} else {
			isAdd = true;
			// �Ƶ�ʱ��
			generalh.setTmaketime(myClientUI._getServerTime().toString());
			// ���õ��ݺ�
			generalh.setVbillcode(CommonUnit.getBillCode("4C",
					ClientEnvironment.getInstance().getCorporation()
							.getPk_corp(), "", ""));
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
		// ����޸�ʱ��
		generalh.setTlastmoditime(myClientUI._getServerTime().toString());
		// �����޸���
		generalh.setClastmodiid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey());
		// ���ۺ�VO�б�ͷ��ֵ
		myBillVO.setParentVO(generalh);
		// ���帳ֵ
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
			if (getBillUI().isSaveAndCommitTogether()){
				myBillVO = getBusinessAction().saveAndCommit(myBillVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), myBillVO);
			}else {
				List<Object> objUser = new ArrayList<Object>();
				objUser.add(getBillUI().getUserObject());
				objUser.add(generalb);
				if (isAdd){
					objUser.add(true);
				}else{
					objUser.add(false);
				}
				objUser.add(false);
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
		// ��ȡ��ѡ����к�
		int index = getBillCardPanelWrapper().getBillCardPanel().getBillTable()
				.getSelectedRow();
		if (index > -1) {
			// ��ȡ����ѡ���VO����
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
				}
				TrayDisposeDlg tdpDlg = new TrayDisposeDlg("4203",
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey(),
						ClientEnvironment.getInstance().getCorporation()
								.getPrimaryKey(), "8004040204", myClientUI);
				tdpDlg.getReturnVOs(myClientUI, item, index, pk_stordoc
						.toString(), opType);
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
				}
				String msg = iw.autoPickAction(ClientEnvironment.getInstance()
						.getUser().getPrimaryKey(), generalbVO, pk_stordoc
						.toString(), "def19");
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
						Object[] a = iw.getNoutNum(generalbVO[i]
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
				}
				chaneColor();
				this.onckmx();

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
			myClientUI.showErrorMessage("����ʧ��,����Ȩ����");
			return;
		}
		TrayDisposeDetailDlg tdpDlg = new TrayDisposeDetailDlg("4203",
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey(), "8004040204", myClientUI);

		TbOutgeneralBVO[] item = (TbOutgeneralBVO[]) getBufferData()
				.getCurrentVO().getChildrenVO();

		tdpDlg.getReturnVOs(myClientUI, item);

	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		if (isControl == 2 || isControl == 3) {
			isAdd = true;
			// ʵ����ѯ��
			fydnewdlg = new FydnewDlg(myClientUI, pkList, isStock);
			// ���÷��� ��ȡ��ѯ��ľۺ�VO
			AggregatedValueObject[] vos = fydnewdlg
					.getReturnVOs(ClientEnvironment.getInstance()
							.getCorporation().getPrimaryKey(),
							ClientEnvironment.getInstance().getUser()
									.getPrimaryKey(), "4202",
							ConstVO.m_sBillDRSQ, "8004040204", "8004040294",
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
									iw.whs_processAction("CANCELSIGN",
											"DELETE", "4C", _getDate()
													.toString(), billvo,
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
				int[] index = myClientUI.getBillListPanel().getHeadTable()
						.getSelectedRows();
				if (null != index && index.length > 0) {

					for (int i = 0; i < index.length; i++) {
						AggregatedValueObject value = getBufferData()
								.getVOByRowNo(index[i]);
						TbOutgeneralHVO generalh = (TbOutgeneralHVO) value
								.getParentVO();
						// TbOutgeneralBVO[] bvo = (TbOutgeneralBVO[]) value
						// .getChildrenVO();

						Object result = iuap
								.retrieveByPK(TbOutgeneralHVO.class, generalh
										.getGeneral_pk());
						if (null != result) {
							generalh = (TbOutgeneralHVO) result;
							if (null != generalh.getVbillstatus()
									&& generalh.getVbillstatus() == 0) {
								myClientUI.showErrorMessage("�õ����Ѿ�ǩ��");
								return;
							} else if (null != generalh.getVbillstatus()
									&& generalh.getVbillstatus() == 1) {
								// ״̬
								generalh.setVbillstatus(new Integer(0));
								// ǩ��������
								generalh.setCregister(ClientEnvironment
										.getInstance().getUser()
										.getPrimaryKey());
								// ǩ��ʱ��
								generalh.setTaccounttime(getBillUI()
										._getServerTime().toString());
								// ǩ������
								generalh.setQianzidate(_getDate());
								AggregatedValueObject billvo = changeReqOutgeneraltoGeneral(value);
								iw
										.whs_processAction("PUSHSAVE", "SIGN",
												"4C", _getDate().toString(),
												billvo, generalh);

								// super.onBoRefresh();

							} else {
								myClientUI.showHintMessage(null);
								myClientUI.showErrorMessage("�õ��ݻ�û�н����˵�ȷ��");
								return;
							}
						}
					}
					myClientUI.showHintMessage("����ˢ������");
					for (int i = 0; i < index.length; i++) {
						getBufferData().setCurrentRow(index[i]);
						super.onBoRefresh();
					}
					myClientUI.showHintMessage("ǩ�ֳɹ�");
					getButtonManager().getButton(
							nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr)
							.setEnabled(false);
					getButtonManager().getButton(
							nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz)
							.setEnabled(true);
					myClientUI.updateButtons();
				}
			} else {
				myClientUI.showHintMessage(null);
			}

		} else {
			myClientUI.showHintMessage(null);
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
	public GeneralBillVO changeReqOutgeneraltoGeneral(
			AggregatedValueObject value) throws Exception {
		if (getBufferData().getCurrentRow() < 0) {
			myClientUI.showErrorMessage("��ѡ���ͷ����ǩ��");
			return null;
		}
		// ���س���� ��ͷ
		TbOutgeneralHVO outhvo = (TbOutgeneralHVO) value.getParentVO();
		// ��Ҫ��ѯ�������ݣ����ȷ������----------Ϊ���
		// Object result = iuap.retrieveByPK(TbOutgeneralHVO.class, outhvo
		// .getGeneral_pk());
		//		
		// if(null!=result){
		// outhvo = (TbOutgeneralHVO) result;
		// if(outhvo.getVbillstatus())
		// }

		// ���س���� ����
		TbOutgeneralBVO[] outbvo = (TbOutgeneralBVO[]) value.getChildrenVO();

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
		// hiddenList = new ArrayList();
		MyBillVO myBillVO = new MyBillVO();
		TbOutgeneralHVO generalHVO = new TbOutgeneralHVO();
		// �ӱ���Ϣ���鼯��
		List<TbOutgeneralBVO> generalBList = new ArrayList<TbOutgeneralBVO>();

		TbFydnewVO fydnew = (TbFydnewVO) vos[0].getParentVO();
		generalHVO.setSrl_pk(fydnew.getSrl_pk()); // ����ֿ�
		generalHVO.setCbiztype(fydnew.getPk_busitype()); // ҵ����������
		generalHVO.setCdptid(fydnew.getCdeptid()); // ����
		generalHVO.setCbizid(fydnew.getPk_psndoc()); // ҵ��Ա
		generalHVO.setCcustomerid(fydnew.getPk_kh()); // �ͻ�
		generalHVO.setVdiliveraddress(fydnew.getFyd_shdz()); // �ջ���ַ
		generalHVO.setVnote(fydnew.getFyd_bz()); // ��ע
		generalHVO.setVsourcebillcode(fydnew.getVbillno()); // ��Դ���ݺ�
		generalHVO.setDauditdate(fydnew.getDapprovedate()); // �������
		generalHVO.setCsourcebillhid(fydnew.getFyd_pk()); // ��Դ���ݱ�ͷ����
		generalHVO.setVbilltype(fydnew.getBilltype().toString()); // ��������

		myBillVO.setParentVO(generalHVO);
		// ѭ�������е��ӱ���Ϣ
		if (null != fydnewdlg.getFydmxVO() && fydnewdlg.getFydmxVO().length > 0) {
			// ��ȡ���� ת���ӱ���������Ϣ
			for (int i = 0; i < fydnewdlg.getFydmxVO().length; i++) {
				TbFydmxnewVO fydmxnewvo = fydnewdlg.getFydmxVO()[i];
				if (fydnew.getFyd_pk().equals(fydmxnewvo.getFyd_pk())) {
					TbOutgeneralBVO generalBVO = new TbOutgeneralBVO();
					setGeneralbVO(fydnew, fydmxnewvo, generalBVO);
					generalBVO.setIsoper(new UFBoolean("Y"));
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
	private void setGeneralbVO(TbFydnewVO fydnew, TbFydmxnewVO fydmxnewvo,
			TbOutgeneralBVO generalBVO) {
		generalBVO.setCsourcebillhid(fydnew.getFyd_pk()); // ��Դ���ݱ�ͷ����
		generalBVO.setVsourcebillcode(fydnew.getVbillno()); // ��Դ���ݺ�
		generalBVO.setCsourcetype(fydnew.getBilltype().toString()); // ��Դ��������
		generalBVO.setCsourcebillbid(fydmxnewvo.getCfd_pk()); // ��Դ���ݱ�������
		generalBVO.setCfirstbillhid(fydmxnewvo.getCsaleid()); // Դͷ���ݱ�ͷ����
		generalBVO.setCfirstbillbid(fydmxnewvo.getCorder_bid()); // Դͷ���ݱ�������
		generalBVO.setCrowno(fydmxnewvo.getCrowno()); // �к�
		generalBVO.setNshouldoutnum(fydmxnewvo.getCfd_yfsl()); // Ӧ������
		generalBVO.setNshouldoutassistnum(fydmxnewvo.getCfd_xs()); // Ӧ��������
		generalBVO.setCinventoryid(fydmxnewvo.getPk_invbasdoc()); // �������
		generalBVO.setFlargess(fydmxnewvo.getBlargessflag()); // �Ƿ���Ʒ
	}

	public Iw8004040204 getIw() {
		return iw;
	}

	public void setIw(Iw8004040204 iw) {
		this.iw = iw;
	}

}
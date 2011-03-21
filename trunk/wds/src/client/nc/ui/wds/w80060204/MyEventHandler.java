package nc.ui.wds.w80060204;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.wds.w80060204.Iw80060204;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.ic.ic009.CardPanelCtrl;
import nc.ui.ic.ic009.ListPanelCtrl;
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
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.to.pub.ConstVO;
import nc.vo.wds.w80060406.MyBillVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;

/**
 * 
 * ���۶����ϲ�
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	MyClientUI myClientUI = null;
	private boolean isControl = false; // �Ƿ���Ȩ�޲�����ǰ����
	private SaleOrderDlg saleOrderDlg = null; // ��ѯ��������

	boolean isAdd = false;

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;
		String isType;
		try {
			isType = CommonUnit.getUserType(ClientEnvironment.getInstance()
					.getUser().getPrimaryKey());
			if (null != isType && isType.equals("2")||(null != isType && isType.equals("3"))) {
				isControl = true;
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub

		getButtonManager().getButton(nc.ui.wds.w80060204.cfButtun.ICfButtun.zj)
				.setEnabled(false); // �������Ӱ�ť״̬
		super.onBoEdit();
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ

		strWhere
				.append(" and billtype = 4 and vbillstatus = 1  and mergelogo is null");

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}

	/**
	 * ���Ӱ�ť�ĵ����¼� ������ѯ�� ���в�ѯ����ʾ
	 */
	protected void onzj() throws Exception {
		if (isControl) {
			// ʵ����ѯ��
			saleOrderDlg = new SaleOrderDlg(myClientUI);

			// ���÷��� ��ȡ��ѯ��ľۺ�VO
			AggregatedValueObject[] vos = saleOrderDlg.getReturnVOs(
					ClientEnvironment.getInstance().getCorporation()
							.getPrimaryKey(), ClientEnvironment.getInstance()
							.getUser().getPrimaryKey(), "0204",
					ConstVO.m_sBillDRSQ, "80060204", "8006020401", myClientUI);

			// �ж��Ƿ�Բ�ѯģ���н��в���
			if (null == vos || vos.length == 0) {
				return;
			}

			// ����ת���� ��ģ���л�ȡ�Ķ���ת�����Լ��ĵ�ǰ��ʾ�Ķ��󣬵��÷���
			MyBillVO voForSave = changeReqSaleOrderYtoFyd(vos);
			// ����������� �Ͱ�ť��ʼ��
			getBufferData().clear();
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
			// �������
			getBufferData().addVOToBuffer(voForSave);
			// ��������
			updateBuffer();
			// �������Ӱ�ť״̬
			getButtonManager().getButton(
					nc.ui.wds.w80060204.cfButtun.ICfButtun.zj)
					.setEnabled(false);
			super.onBoEdit();
			// getBillUI().setBillOperate( // ��Ϊ����Զ��尴ť��ͻ��е�������ťģʽ ��������״̬
			// nc.ui.trade.base.IBillOperate.OP_EDIT);
			// ִ�й�ʽ��ͷ��ʽ
			getBillCardPanelWrapper().getBillCardPanel().execHeadEditFormulas();
			isAdd = true;
		} else {
			getBillUI().showErrorMessage("����ʧ��,��ǰ��¼��û�н�����Ա��");
			return;
		}
	}

	@Override
	protected void onBoSave() throws Exception {

		// ��ʾ
		int result = getBillUI().showYesNoMessage("�Ƿ��Ƶ���ɲ���ӡ?");
		// ���ѡ��Yes
		if (result == 4) {
			// ������Ϊyes ���ã� �����Ǻϲ�����������״̬����ɣ�����״̬�Ǵ�����
			getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("billtype").setValue(new Integer(4));
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"vbillstatus").setValue(new Integer(1));
			getBillCardPanelWrapper().getBillCardPanel()
					.getHeadItem("fyd_fyzt").setValue(new Integer(0));
			this.onSave(getBillUI().getVOFromUI());
		}
		/*
		 * /////////////���´����������������� ����û�� ֻ�кϲ�����������////////////////// // else { // //
		 * ��ʾ // int result = getBillUI().showYesNoCancelMessage("�Ƿ�����Ƶ�?"); // //
		 * No // if (result == 8) { // // �����NO ���������۵�������״̬��δ��� //
		 * getBillCardPanelWrapper().getBillCardPanel().getHeadItem( //
		 * "billtype").setValue(new Integer(1)); //
		 * getBillCardPanelWrapper().getBillCardPanel().getHeadItem( //
		 * "vbillstatus").setValue(new Integer(0)); // onSave(); // } // // Yes //
		 * if (result == 4) { // // ������Ϊ�� ���������۵�������״̬����� //
		 * getBillCardPanelWrapper().getBillCardPanel().getHeadItem( //
		 * "billtype").setValue(new Integer(1)); //
		 * getBillCardPanelWrapper().getBillCardPanel().getHeadItem( //
		 * "vbillstatus").setValue(new Integer(1)); //
		 * getBillCardPanelWrapper().getBillCardPanel().getHeadItem( //
		 * "fyd_fyzt").setValue(new Integer(0)); // onSave(); // } // }
		 * ///////////////////////////////////////////////////////////
		 */
	}

	IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());

	// �ѱ��淽������������޸�getBillUI().getVOFromUI();
	private void onSave(AggregatedValueObject billVO) throws Exception {
		getButtonManager().getButton(nc.ui.wds.w80060204.cfButtun.ICfButtun.zj)
				.setEnabled(true);
		// ���ýӿڽ��б���ͻ�д
		Iw80060204 iw = (Iw80060204) NCLocator.getInstance().lookup(
				Iw80060204.class.getName());
		iw.saveFyd(billVO);

		// ��ӡ
		onBoPrint();
		getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
		// onBoRefresh();

	}

	/*
	 * ɾ������ @Override protected void onBoDelete() throws Exception { // TODO
	 * Auto-generated method stub // ����û�����ݻ��������ݵ���û��ѡ���κ��� if
	 * (getBufferData().getCurrentVO() == null) return;
	 * 
	 * if (MessageDialog.showOkCancelDlg(getBillUI(),
	 * nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
	 * "UPPuifactory-000064")/* @res "����ɾ��"
	 */
	/*
	 * , nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
	 * "UPPuifactory-000065")/* @res "�Ƿ�ȷ��ɾ���û�������?"
	 */

	/*
	 * * , MessageDialog.ID_CANCEL) != UIDialog.ID_OK) return;
	 * 
	 * AggregatedValueObject modelVo = getBufferData().getCurrentVO();
	 * 
	 * TbFydnewVO fydvo = (TbFydnewVO) modelVo.getParentVO(); // ��pk�ϲ���ʶ��ȡ������
	 * String[] pk = fydvo.getPk_mergelogo().split(",");
	 * 
	 * if (null != pk && pk.length > 0) { for (int i = 0; i < pk.length; i++) {
	 * String sql = "select count(csourcebillhid) from tb_outgeneral_b where
	 * csourcebillhid = '" + pk[i] + "'"; // ����������ѯ�Ƿ������ɳ��ⵥ ArrayList list =
	 * (ArrayList) iuap.executeQuery(sql, new ArrayListProcessor()); Object[]
	 * results = (Object[]) list.get(0); if (null != results[0] &&
	 * !"".equals(results[0]) && Integer.parseInt(results[0].toString()) > 0) {
	 * myClientUI.showErrorMessage("ɾ��ʧ��!�õ��������ɳ��ⵥ��,����ɾ�����ⵥ��"); return; } } }
	 * List pman = new ArrayList(); pman.add(getBillUI().getUserObject());
	 * getBusinessAction().delete(modelVo, getUIController().getBillType(),
	 * getBillUI()._getDate().toString(), pman); if (PfUtilClient.isSuccess()) { //
	 * ����������� getBillUI().removeListHeadData(getBufferData().getCurrentRow());
	 * if (getUIController() instanceof ISingleController) { ISingleController
	 * sctl = (ISingleController) getUIController(); if (!sctl.isSingleDetail())
	 * getBufferData().removeCurrentRow(); } else {
	 * getBufferData().removeCurrentRow(); } } if
	 * (getBufferData().getVOBufferSize() == 0)
	 * getBillUI().setBillOperate(IBillOperate.OP_INIT); else
	 * getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
	 * getBufferData().setCurrentRow(getBufferData().getCurrentRow()); }
	 */

	@Override
	protected void onBoCancel() throws Exception {
		getButtonManager().getButton(nc.ui.wds.w80060204.cfButtun.ICfButtun.zj)
				.setEnabled(true);
		// �ж��Ƿ�Ϊ������� ȡ��
		if (isAdd) {
			getBufferData().clear();
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
			getBillUI().initUI();
			isAdd = false;
		} else {
			super.onBoCancel();
		}

	}

	/**
	 * ��ģ����ѡ�е�VO ����ת���ɷ��˵���VO
	 * 
	 * @param vos
	 *            ҳ��ѡ�еľۺ�VO
	 * @return ���˵��ľۺ�VO
	 * @throws BusinessException
	 */
	private MyBillVO changeReqSaleOrderYtoFyd(AggregatedValueObject[] vos)
			throws BusinessException {
		MyBillVO myBillVO = new MyBillVO();
		// ��ȡ��ѯ���ݿ����
		IUAPQueryBS iQuery = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		int num = 0; // Ϊ�˼�������ĳ���
		SaleorderHVO salehVO = null;
		TbFydnewVO fydnewVO = new TbFydnewVO();
		// �ӱ���Ϣ���鼯��
		List<SaleorderBVO[]> salevoList = new ArrayList<SaleorderBVO[]>();
		// ����ۺ�VO�ĳ��ȴ���1 ˵���Ǻϲ���������
		if (vos.length > 1) {
			String vercode = null; // ������
			String saleid = null; // �ϲ�������۶�������
			String bustype = null; // ҵ������ ���� ������
			// ��ȡ��һ�β�ѯ������ݻ���
			SaleOrderVO[] salevo = saleOrderDlg.getSaleVO();
			// ѭ���ۺ�VO�ĳ���
			for (int i = 0; i < vos.length; i++) {
				// �ѵ�һ��������ȡ����
				salehVO = (SaleorderHVO) vos[i].getParentVO();
				if (null != salehVO.getCsaleid() // �ж����������Ƿ�Ϊ��
						&& !"".equals(salehVO.getCsaleid())) {
					if (null == saleid || "".equals(saleid)) { // �ж������Ƿ�Ϊ�գ����Ϊ��˵���ǵ�һ�θ�ֵ
						saleid = salehVO.getCsaleid();
					} else { // �����ֵ �����ۼӣ��м��ö��������֣�������������
						saleid = saleid + "," + salehVO.getCsaleid();
					}
				}
				// �ж϶������Ƿ�Ϊ�գ�ͬ��
				if (null != salehVO.getVreceiptcode()
						&& !"".equals(salehVO.getVreceiptcode())) {
					if (null == vercode || "".equals(vercode)) {
						vercode = salehVO.getVreceiptcode();
					} else {
						vercode = vercode + "," + salehVO.getVreceiptcode();
					}
				}
				// ��ȡ��ǰ�����е���������(�����ۼӺ������)
				String csaleid = salehVO.getCsaleid();
				// ѭ������
				for (int j = 0; j < salevo.length; j++) {
					// ��ȡ�����е���������
					String salehid = salevo[j].getHeadVO().getCsaleid();
					// �жϵ�ǰ�����������ͻ����е����������Ƿ���ͬ��
					if (csaleid.equals(salehid)) {
						// �������������ͬ�Ͱѻ����ж�Ӧ�������ӱ���Ϣ������ȡ�����ŵ� �ӱ���Ϣ������
						salevoList.add((SaleorderBVO[]) salevo[j]
								.getChildrenVO());
						// ��ȡ��ǰ�ӱ���Ϣ�ĳ��� ����ж�� �����ۼ� Ϊ������������ʼ��
						num = num + salevo[j].getChildrenVO().length;
						break;
					}
				}
				// ��ȡҵ�������Ƿ�Ϊ��
				if (null != salehVO.getCbiztype()
						&& !"".equals(salehVO.getCbiztype())) {
					String sql = "select businame from bd_busitype where pk_busitype = '"
							+ salehVO.getCbiztype() + "'";
					ArrayList list = (ArrayList) iQuery.executeQuery(sql,
							new ArrayListProcessor());
					// �жϽ���Ƿ�Ϊ��
					if (list != null && list.size() > 0) {
						Object a[] = (Object[]) list.get(0);
						if (a != null && a.length > 0 && a[0] != null) {
							if (null == bustype)// �����һ��Ϊ�ս��и�ֵ
								bustype = a[0].toString();
							else
								// ��Ϊ�� ��Ӷ��Ž�������
								bustype = bustype + "," + a[0].toString();
						}
					}
				}
			}
			// ���ۼӺ�Ķ����ź�������ֵ����ǰ�Ķ���
			fydnewVO.setVbillno(vercode); // ���ݺ�
			fydnewVO.setCsaleid(saleid); // ������������
			fydnewVO.setFyd_busitype(bustype); // ҵ������
			// ����վ
			fydnewVO.setSrl_pk(CommonUnit.getStordocName(ClientEnvironment
					.getInstance().getUser().getPrimaryKey()));
			// �ӱ���Ϣ����
			List<SaleorderBVO> salelist = new ArrayList<SaleorderBVO>();
			// ѭ���ӱ���Ϣ���鼯�ϣ��Ѽ����е������ѭ���������ŵ��ӱ���Ϣ�����У��������ת����������
			for (int i = 0; i < salevoList.size(); i++) {
				SaleorderBVO[] tmp = salevoList.get(i);
				for (int j = 0; j < tmp.length; j++) {
					salelist.add(tmp[j]);
				}
			}
			SaleorderBVO[] saleb = new SaleorderBVO[num]; // ʵ��һ���ӱ���Ϣ���飬�䳤�Ⱦ���������ж���ۼӵ�
			salelist.toArray(saleb); // ����ת��
			// ��ת������ӱ�����ŵ���ǰ�ľۺ�VO��
			vos[0].setChildrenVO(saleb);
		}
		/*
		 * // ///////////���´��� Ϊ ԭ�ƻ� �÷��������ֲ��� һ��Ϊ�ϲ����� һ��Ϊ�������� //
		 * ����ֻ�кϲ�����һ��////////////////////// // else { // ������Ǻϲ������Ĳ��� ����������ֵ�� //
		 * salehVO = (SaleorderHVO) vos[0].getParentVO(); // if (null !=
		 * salehVO.getVreceiptcode() // &&
		 * !"".equals(salehVO.getVreceiptcode())) { //
		 * fydnewVO.setVbillno(salehVO.getVreceiptcode()); // ���ݺ� // } // if
		 * (null != salehVO.getCsaleid() // && !"".equals(salehVO.getCsaleid())) { //
		 * fydnewVO.setCsaleid(salehVO.getCsaleid()); // ���� // } // //
		 * ѭ�������е��ӱ���Ϣ // for (int i = 0; i < saleOrderDlg.getSaleVO().length;
		 * i++) { // if (salehVO.getCsaleid().equals( //
		 * saleOrderDlg.getSaleVO()[i].getHeadVO().getCsaleid())) { //
		 * vos[0].setChildrenVO(saleOrderDlg.getSaleVO()[i] // .getBodyVOs()); //
		 * break; // } // } // } //
		 * ///////////////////////////////////////////////////////////////////////////////////////////////////////
		 */
		// ����Ϊ���ò��� ��Ϊ�ںϲ��������ģ��������Ϣ�ǲ����
		if (null != salehVO.getCcustomerid()
				&& !"".equals(salehVO.getCcustomerid())) {
			fydnewVO.setPk_kh(salehVO.getCcustomerid()); // �ͻ�����
		}
		if (null != salehVO.getVreceiveaddress()
				&& !"".equals(salehVO.getVreceiveaddress())) {
			fydnewVO.setFyd_shdz(salehVO.getVreceiveaddress()); // �ջ���ַ
		}
		if (null != salehVO.getCemployeeid()
				&& !"".equals(salehVO.getCemployeeid())) {
			fydnewVO.setPk_psndoc(salehVO.getCemployeeid()); // ҵ��Ա
		}
		if (null != salehVO.getCbiztype() && !"".equals(salehVO.getCbiztype())) {
			fydnewVO.setPk_busitype(salehVO.getCbiztype()); // ҵ������
		}
		if (null != salehVO.getVnote() && !"".equals(salehVO.getVnote())) {
			fydnewVO.setFyd_bz(salehVO.getVnote()); // ��ע
		}
		if (null != salehVO.getCdeptid() && !"".equals(salehVO.getCdeptid())) {
			fydnewVO.setCdeptid(salehVO.getCdeptid()); // ����
		}
		if (null != salehVO.getAttributeValue("daudittime")
				&& !"".equals(salehVO.getAttributeValue("daudittime"))) {
			fydnewVO.setFyd_spsj(salehVO.getAttributeValue("daudittime")
					.toString()); // ����ʱ��
		}
		if (null != salehVO.getVdef18() && !"".equals(salehVO.getVdef18())) {
			UFDouble gls = null;
			try {
				gls = new UFDouble(salehVO.getVdef18());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				gls = null;
			}
			fydnewVO.setFyd_yslc(gls); // ������
		}
		if (null != salehVO.getDbilldate()
				&& !"".equals(salehVO.getDbilldate())) {
			fydnewVO.setFyd_xhsj(salehVO.getDbilldate()); // ���ʱ��
			// �������ת���������������еĵ��������ֶ�
		}
		// �����˻���ʽ
		fydnewVO.setFyd_yhfs("����");

		// �Ƶ�����
		fydnewVO.setDmakedate(_getDate());
		fydnewVO.setFyd_dby(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey()); // ���õ���Ա
		fydnewVO.setVoperatorid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey()); // �����Ƶ���
		fydnewVO.setBilltype(new Integer(1));
		myBillVO.setParentVO(fydnewVO);

		if (null != vos[0].getChildrenVO() && vos[0].getChildrenVO().length > 0) {
			SaleorderBVO[] salebVO = (SaleorderBVO[]) vos[0].getChildrenVO();
			TbFydmxnewVO[] fydmxnewVO = new TbFydmxnewVO[salebVO.length];
			// ��ȡ���� ת���ӱ���������Ϣ
			for (int i = 0; i < salebVO.length; i++) {
				SaleorderBVO salebvo = salebVO[i];
				TbFydmxnewVO fydmxnewvo = new TbFydmxnewVO();
				if (null != salebvo.getCorder_bid()
						&& !"".equals(salebvo.getCorder_bid())) {
					fydmxnewvo.setCorder_bid(salebvo.getCorder_bid()); // ���۸�������
				}

				if (null != salebvo.getNnumber()
						&& !"".equals(salebvo.getNnumber())) {
					fydmxnewvo.setCfd_yfsl(salebvo.getNnumber()); // Ӧ������
				}
				if (null != salebvo.getNpacknumber()
						&& !"".equals(salebvo.getNpacknumber())) {
					fydmxnewvo.setCfd_xs(salebvo.getNpacknumber()); // ����
				}
				if (null != salebvo.getCrowno()
						&& !"".equals(salebvo.getCrowno())) {
					fydmxnewvo.setCrowno(salebvo.getCrowno()); // �к�
				}
				if (null != salebvo.getBlargessflag()
						&& !"".equals(salebvo.getBlargessflag())) {
					fydmxnewvo.setBlargessflag(salebvo.getBlargessflag()); // �Ƿ���Ʒ
				}
				if (null != salebvo.getCunitid()
						&& !"".equals(salebvo.getCunitid())) {
					fydmxnewvo.setCfd_dw(salebvo.getCunitid()); // ��λ
				}

				fydmxnewVO[i] = fydmxnewvo;

			}
			myBillVO.setChildrenVO(fydmxnewVO);
		}

		return myBillVO;
	}
}

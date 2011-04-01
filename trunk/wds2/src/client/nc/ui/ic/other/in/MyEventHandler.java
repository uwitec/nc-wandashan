package nc.ui.ic.other.in;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.businessaction.IBusinessController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.INormalQuery;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.to.pub.ConstVO;
import nc.vo.wds.w8004040210.MyBillVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;
import nc.vo.wds.w80060608.IcGeneralBVO;
import nc.vo.wds.w80060608.IcGeneralHVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * 
 * ������AbstractMyEventHandler�������ʵ���࣬ ��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	private List myTempall;
	private MyClientUI myClientUI;
	private IUAPQueryBS iuap = null;
	// �ж��û����
	// �ж����ֻܲ��Ƿֲ�
	private boolean sotckIsTotal = true;
	private int iUserType = -1;

	protected IBusinessController createBusinessAction() {
		// TODO Auto-generated method stub
//		switch (getUIController().getBusinessActionType()) {
//		case IBusinessActionType.PLATFORM:
//			dbbool = false;
//			return new BusinessAction(getBillUI());
//		case IBusinessActionType.BD:
//			dbbool = true;
			return new TrayAction(getBillUI());
//			// }
//		default:
//			dbbool = false;
//			return new BusinessAction(getBillUI());
//		}
	}

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;
		try {
			iUserType = LoginInforHelper.getITypeByUser(myClientUI._getOperator());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			iUserType =0 ;
		}
		try {
			sotckIsTotal = WdsWlPubTool.isZc(LoginInforHelper.getCwhid(myClientUI._getOperator()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sotckIsTotal = true;
		}
	}

	/**
	 * ���Ƶ���
	 */
	public void onZzdj() throws Exception {
		if (getBillManageUI().isListPanelSelected())
			getBillManageUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		getBillUI().setBillOperate(IBillOperate.OP_ADD);
	}

	// �Ƿ�������
//	private boolean iszzdj = false;

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-9 8:37:34)
	 * 
	 * @return nc.ui.trade.pub.BillCardUI
	 */
	private BillManageUI getBillManageUI() {
		return (BillManageUI) getBillUI();
	}

	private ProdWaybillDlg m_prodWaybillDlg = null;
	private ProdWaybillDlg getProdWaybillDlg(){
		if(m_prodWaybillDlg == null){
			m_prodWaybillDlg = new ProdWaybillDlg(myClientUI);
		}
		return m_prodWaybillDlg;
	}
	/**
	 * ���˵���
	 */
	@Override
	protected void onFydj() throws Exception {
		//ֻ�п��Ա���Բ����ù���
		if(iUserType != 0){
			throw new BusinessException("���޲���Ȩ��");
		}

		// 6ģ���ʶ
		AggregatedValueObject[] vos = getProdWaybillDlg().getReturnVOs(
				myClientUI._getCorp().getPrimaryKey(), myClientUI._getOperator(), "0202",
				ConstVO.m_sBillDRSQ, "8004040214", "02140286", myClientUI);
		// �ж�ʧ�ֻܲ��Ƿֲ�
		if (null == vos || vos.length == 0) {

			return;
		}

		if (vos.length > 2) {
			getBillUI().showErrorMessage("һ��ֻ��ѡ��һ���˵���");
			return;
		}
		try {
			MyBillVO voForSave = null;
			voForSave = changeTbPwbtoTbgen(vos);
			
//			getBufferData().clear();
//			getBufferData().addVOToBuffer(voForSave);
//			updateBuffer();
			getBillUI().setBillOperate(IBillOperate.OP_ADD);
//			super.onBoEdit();
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
			"geb_virtualbnum").setEdit(true);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
			"geb_customize2").setEdit(true);
		} catch (Exception e) {
			getBillUI().showErrorMessage(e.getMessage());
		}
	}

	/**
	 * ���˵����䵥��
	 * 
	 * @param vos
	 * @return
	 */
	private MyBillVO changeTbPwbtoTbgen(AggregatedValueObject[] vos) {
		MyBillVO myBillVO = new MyBillVO();
		// ������ⵥ����VO
		TbGeneralHVO tbGeneralHVO = new TbGeneralHVO();
		// �������ⵥ����VO
		TbFydnewVO firstVO = (TbFydnewVO) vos[0].getParentVO();

		// ����˵����ݺ�

		// ��ӷ��˵����ݺ�
		tbGeneralHVO.setGeh_vbillcode(firstVO.getFyd_ddh());
		// ��ⵥ����
		tbGeneralHVO.setGeh_billtype(WdsWlPubConst.BILLTYPE_OTHER_IN);
		// �˵���ͷ����
		// tbGeneralHVO.setPwb_pk(tbProdwaybillVO.getPwb_pk());
		// ���˵���ͷ����
		tbGeneralHVO.setGeh_cgeneralhid(firstVO.getFyd_pk());
		// �������ݵ��ݺ�

		// ����ֿ�����
		if (null != firstVO.getSrl_pk()) {
			tbGeneralHVO.setGeh_cotherwhid(firstVO.getSrl_pk());
		}
		// ҵ��Ա����
		if (null != firstVO.getPk_psndoc()) {
			tbGeneralHVO.setGeh_cbizid(firstVO.getPk_psndoc());
		}
		// ��⹫˾����
//		if (null != ClientEnvironment.getInstance().getUser().getCorpId()) {
			tbGeneralHVO.setGeh_corp(ClientEnvironment.getInstance().getUser()
					.getCorpId().toString());
//		}
		// �������֯����
		// if (null != firstVO.getPk_calbodyr()) {
		// tbGeneralHVO.setGeh_calbody(firstVO.getPk_calbodyr().toString());
		// }
		// ���ֿ�����
		if (null != firstVO.getSrl_pkr()) {
			tbGeneralHVO.setGeh_cwarehouseid(firstVO.getSrl_pkr().toString());
		}

		// ������Ա����
		// if (null != firstVO.getCwhsmanagerid()) {
		// tbGeneralHVO.setGeh_cwhsmanagerid(firstVO.getCwhsmanagerid());
		// }

		// �������ͱ�־
		// tbGeneralHVO.setGeh_fallocflag(firstVO.getFallocflag());
		// �Ƿ�����˻�
		// tbGeneralHVO.setGeh_freplenishflag(firstVO.getFreplenishflag());
		// ҵ������
		if (null != firstVO.getPk_busitype()) {
			tbGeneralHVO.setGeh_cbiztype(firstVO.getPk_busitype());
		}

		// �Ƶ����ڣ���������

		tbGeneralHVO.setCopetadate(new UFDate(new Date()));
		tbGeneralHVO.setGeh_dbilldate(new UFDate(new Date()));
		// �Ƶ���
		tbGeneralHVO.setCoperatorid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey());
		// ��ⵥ����
//		tbGeneralHVO.setGeh_billtype(2);
		// �������VO
		myBillVO.setParentVO(tbGeneralHVO);

		// ��ȡ�ӱ�vo
		String pk = firstVO.getFyd_pk().toString();
		String sql = " fyd_pk='" + pk + "' and dr=0 ";
//		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
//				IUAPQueryBS.class.getName());
		// ȫ���ӱ�VO
		List myTemp = new ArrayList();
		// ǰ̨��ʾ��VO
		List myTempShow = new ArrayList();

		myTempall = new ArrayList();
		try {
			ArrayList ttcs = (ArrayList) getQueryBO().retrieveByClause(
					TbFydmxnewVO.class, sql.toString());
			// ��ǰ��¼�ı���Ա�ܹ���Ļ�Ʒ
//			String[] InvLisk =LoginInforHelper.getInvBasDocIDsByUserID(ClientEnvironment.getInstance().getUser().getPrimaryKey());
			for (int i = 0; i < ttcs.size(); i++) {
				TbFydmxnewVO gbvo = (TbFydmxnewVO) ttcs.get(i);
				TbGeneralBVO tbGeneralBVO = new TbGeneralBVO();
				// ����к�
				// if (null != gbvo.getCrowno()) {
				// tbGeneralBVO.setGeb_crowno(gbvo.getCrowno());
				// }
				tbGeneralBVO.setGeb_crowno(i + 1 + "0");
				// ���������������
				if (null != gbvo.getPk_invbasdoc()) {
					tbGeneralBVO.setGeb_cinvbasid(gbvo.getPk_invbasdoc());
				}

				// ����˵���ͷ����
				// tbGeneralBVO.setPwb_pk(tbProdwaybillVO.getPwb_pk());
				// �˵���������
				// tbGeneralBVO.setPwbb_pk(ttc.getPwbb_pk());
				// ���˵���ͷ����
				if (null != firstVO.getFyd_pk()) {
					tbGeneralBVO.setGeb_cgeneralhid(firstVO.getFyd_pk());
				}

				// ���˵���������
				if (null != gbvo.getCfd_pk()) {
					tbGeneralBVO.setGeb_cgeneralbid(gbvo.getCfd_pk());
				}
				// ���κ�
				if (null != gbvo.getCfd_pc()) {
					tbGeneralBVO.setGeb_vbatchcode(gbvo.getCfd_pc());
				}

				// ��λ
				if (null != nc.ui.wds.w8000.CommonUnit
						.getCargDocName(ClientEnvironment.getInstance()
								.getUser().getPrimaryKey())) {
					tbGeneralBVO.setGeb_space(nc.ui.wds.w8000.CommonUnit
							.getCargDocName(ClientEnvironment.getInstance()
									.getUser().getPrimaryKey()));
				}

				// Ҫ��д�����κ�
				if (null != gbvo.getCfd_lpc()) {
					tbGeneralBVO.setGeb_backvbatchcode(gbvo.getCfd_lpc());
				}
				// �������Σ���ѯ�������ڣ�ʧЧ����
				String vbcsql = "select dproducedate ,dvalidate  from scm_batchcode where pk_invbasdoc='"
						+ tbGeneralBVO.getGeb_cinvbasid()
						+ "' and vbatchcode='"
						+ tbGeneralBVO.getGeb_backvbatchcode() + "' and dr=0";
				ArrayList vbc = (ArrayList) getQueryBO().executeQuery(vbcsql,
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

				
				// �Ƿ���Ʒ
				if (null != gbvo.getBlargessflag()) {
					tbGeneralBVO.setGeb_flargess(gbvo.getBlargessflag());
				}
				// Ӧ�ո�����
				if (null != gbvo.getCfd_sffsl()) {
					tbGeneralBVO.setGeb_bsnum(gbvo.getCfd_sffsl());
				}
				
				if (null != gbvo.getCfd_sfsl()) {
					tbGeneralBVO.setGeb_snum(gbvo.getCfd_sfsl());
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
//				if (null != InvLisk && InvLisk.size() > 0) {
//					for (int j = 0; j < InvLisk.size(); j++) {
//						if (null != gbvo.getPk_invbasdoc()) {
//							if (gbvo.getPk_invbasdoc().equals(InvLisk.get(j))) {
//								if (null != tbGeneralBVO.getGeb_snum()
//										&& 0 != tbGeneralBVO.getGeb_snum()
//												.doubleValue()) {
//									myTempShow.add(tbGeneralBVO);
//								}
//
//								break;
//							}
//						}
//					}
//				}

				if (null != tbGeneralBVO.getGeb_snum()
						&& 0 != tbGeneralBVO.getGeb_snum().doubleValue()) {
					myTempall.add(tbGeneralBVO);
				}

				addoredit = true;
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (sotckIsTotal) {
			TbGeneralBVO[] tbGeneralBVOs = new TbGeneralBVO[myTempShow.size()];
			myTempShow.toArray(tbGeneralBVOs);
			myBillVO.setChildrenVO(tbGeneralBVOs);
		} else {
			TbGeneralBVO[] tbGeneralBVOs = new TbGeneralBVO[myTempall.size()];
			myTempall.toArray(tbGeneralBVOs);
			myBillVO.setChildrenVO(tbGeneralBVOs);
		}

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
	private MyBillVO queryLocaDataPwb(AggregatedValueObject[] vos)
			throws BusinessException {
		// �������ⵥ����VO
		TbFydnewVO tbFydnewVO = (TbFydnewVO) vos[0].getParentVO();
		if (null != tbFydnewVO && null != tbFydnewVO.getFyd_pk()
				&& tbFydnewVO.getFyd_pk().length() > 0) {
			TbGeneralHVO tbGeneralHVO = new TbGeneralHVO();
			TbGeneralBVO[] tbGeneralBVOs = null;
			// TbOutgeneralHVO generalh = null;
			// TbOutgeneralBVO[] generalb = null;
			String strWhere = " dr = 0 and geh_cgeneralhid = '"
					+ tbFydnewVO.getFyd_pk() + "'";
//			IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
//					IUAPQueryBS.class.getName());
			// Ҫ��ʾ���ӱ�VO
			List tmpList = new ArrayList();
			// ����������ѯ���������Ƿ�������
			ArrayList hList = (ArrayList) getQueryBO().retrieveByClause(
					TbGeneralHVO.class, strWhere);
			if (null != hList && hList.size() > 0) {
				tbGeneralHVO = (TbGeneralHVO) hList.get(0);
			}
			if (null != tbGeneralHVO) {
				// ���ݱ�������������ѯ�ӱ�����
				strWhere = " dr = 0 and geh_pk = '" + tbGeneralHVO.getGeh_pk()
						+ "'";
				ArrayList list = (ArrayList) getQueryBO().retrieveByClause(
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
						addoredit = false;
						return mybillvo;
					}
				}
			}
		}
		return null;
	}

	/**
	 * ��ѯ�����Ƿ��Ѿ����ڸ����ݣ�����з��ظ�VO(�ֲ�)
	 * 
	 * @param vos
	 *            ��ʽ��������ѡ�еľۺ�VO
	 * @return
	 * @throws BusinessException
	 */
	private MyBillVO queryLocaDataPwbf(AggregatedValueObject[] vos)
			throws BusinessException {
		// �������ⵥ����VO
		TbFydnewVO tbFydnewVO = (TbFydnewVO) vos[0].getParentVO();
		if (null != tbFydnewVO && null != tbFydnewVO.getFyd_pk()
				&& tbFydnewVO.getFyd_pk().length() > 0) {
			TbGeneralHVO tbGeneralHVO = new TbGeneralHVO();
			TbGeneralBVO[] tbGeneralBVOs = null;
			// TbOutgeneralHVO generalh = null;
			// TbOutgeneralBVO[] generalb = null;
			String strWhere = " dr = 0 and geh_cgeneralhid = '"
					+ tbFydnewVO.getFyd_pk() + "'";
			IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
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
					// �ı��ӱ��״̬
					for (int i = 0; i < list.size(); i++) {
						((TbGeneralBVO) list.get(i))
								.setStatus(VOStatus.UPDATED);
					}
					if (list.size() > 0) {
						tbGeneralBVOs = new TbGeneralBVO[list.size()];
						tbGeneralBVOs = (TbGeneralBVO[]) list
								.toArray(tbGeneralBVOs);
						MyBillVO mybillvo = new MyBillVO();
						mybillvo.setParentVO(tbGeneralHVO);
						mybillvo.setChildrenVO(tbGeneralBVOs);
						// ���������״̬�Ļ� �޸�
						addoredit = false;
						return mybillvo;
					}
				}
			}
		}
		return null;
	}

	/**
	 * ��λ����
	 */
	@Override
	protected void onHwtz() throws Exception {
		// TODO Auto-generated method stub
		try {
			// List invLisk = nc.ui.wds.w8000.CommonUnit
			// .getInvbasdoc_Pk(ClientEnvironment.getInstance().getUser()
			// .getPrimaryKey());
			// if (null == invLisk || invLisk.size() == 0) {
			// getBillUI().showErrorMessage("����Ȩ������");
			// return;
			// }
			// List invLisk = nc.ui.wds.w8000.CommonUnit
			// .getInvbasdoc_Pk(ClientEnvironment.getInstance().getUser()
			// .getPrimaryKey());
			String st_type = nc.ui.wds.w8000.CommonUnit
					.getUserType(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
			if (null == st_type || "".equals(st_type)
					|| (!"0".equals(st_type) && !"3".equals(st_type))) {
				getBillUI().showErrorMessage("����Ȩ������");
				return;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// ConstVO.m_sBillDRSQ

		TbOutgeneralbillDlg tbOutgeneralbillDlg = new TbOutgeneralbillDlg(
				myClientUI);
		AggregatedValueObject[] vos = tbOutgeneralbillDlg.getReturnVOs(
				ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey(), ClientEnvironment.getInstance()
						.getUser().getPrimaryKey(), "0214",
				ConstVO.m_sBillDRSQ, "8004040214", "02140288", myClientUI);
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
			MyBillVO voForSave = queryLocaData(vos);
			if (null == voForSave) {
				voForSave = changeTbOuttoTbgen(vos);
			}
			getBufferData().clear();
			getBufferData().addVOToBuffer(voForSave);
			updateBuffer();
			// getBillUI().setBillOperate(IBillOperate.OP_EDIT);
			super.onBoEdit();
			getButtonManager().getButton(IBillButton.Edit).setEnabled(false);

			getButtonManager().getButton(IBillButton.Save).setEnabled(true);
			getButtonManager().getButton(IBillButton.Cancel).setEnabled(true);
			getButtonManager().getButton(IBillButton.Line).setEnabled(false);
			getButtonManager().getButton(IBillButton.Refresh).setEnabled(false);
			getButtonManager().getButton(IBillButton.Query).setEnabled(false);
			getButtonManager().getButton(
					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zdrk)
					.setEnabled(true);
			getButtonManager().getButton(
					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zdtp)
					.setEnabled(true);
			getButtonManager().getButton(
					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zj).setEnabled(
					false);
			// ��;�����༭����
			getBillUI().updateButtonUI();

		} catch (Exception e) {
			getBillUI().showErrorMessage(e.getMessage());
		}
		// addoredit = true;
		showZeroLikeNull(false);
		isedit = false;
		// createBusinessAction();
	}
	
	private TrayDisposeDlg m_trayDisDlg = null;
	private TrayDisposeDlg getTrayDisDlg(){
		if(m_trayDisDlg == null){
			m_trayDisDlg = new TrayDisposeDlg("80CC", ClientEnvironment
					.getInstance().getUser().getPrimaryKey(), ClientEnvironment
					.getInstance().getCorporation().getPrimaryKey(), "8004040214",
					 myClientUI);
		}
		return m_trayDisDlg;
	}

	/**
	 * ָ������
	 */
	@Override
	protected void onZdtp() throws Exception {
		// TODO Auto-generated method stub
		//
		String[] cargdocs = LoginInforHelper.getSpaceByLogUser(myClientUI._getOperator());
		if(cargdocs== null||cargdocs.length==0||cargdocs.length>1)
			throw new BusinessException("��ȡ��λ��Ϣ�쳣");



		//��֤  ��Ϣ����Ϊ��
		// ��ͷ

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
		// ���ѡ�еı���VO
		TbGeneralBVO tbGeneralBVO = (TbGeneralBVO) getBillCardPanelWrapper()
		.getBillCardPanel().getBillModel().getBodyValueRowVO(b[0],
				TbGeneralBVO.class.getName());

		if(tbGeneralBVO == null){
			throw new BusinessException("�ӽ����ȡ�����쳣");
		}
		tbGeneralBVO.validateOnZdrk();


		TrayDisposeDlg tdpDlg = getTrayDisDlg();
		tdpDlg.setChlid(tbGeneralBVO);//zhf modify

		TbGeneralBVO vos = tdpDlg.getReturnVOs(tbGeneralBVO);
		if (null == vos) {
			return;
		}

		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
				vos.getGeb_banum(), b[0], "geb_banum");
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
				vos.getGeb_anum(), b[0], "geb_anum");
		//		m_geb_banum = vos.getGeb_banum().toDouble();
		//		brow = b[0];

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
				String pk_cargdoc = LoginInforHelper.getSpaceByLogUser(myClientUI._getOperator())[0];

				//				// ��ñ���

				String[] cdts = LoginInforHelper.getTrayInfor(pk_cargdoc, tbGeneralBVOs[i].getGeb_cinvbasid());

				// �����ݻ�
				// ��Ʒ��ǰ���ݻ�
				int iVolumn = LoginInforHelper.getTrayVolumeByInvbasid(tbGeneralBVOs[i].getGeb_cinvbasid());
				double invVolume = cdts.length*iVolumn;

				// ��ȡ��ǰ�����е�Ӧ����������ʵ�����������бȽϣ����ݱȽϽ��������ɫ��ʾ
				// ��ɫ��û��ʵ����������ɫ����ʵ����������������������ɫ��ʵ��������Ӧ���������
				UFDouble num = PuPubVO.getUFDouble_NullAsZero(getBillCardPanelWrapper().getBillCardPanel()
						.getBodyValueAt(i, "geb_bsnum"));// Ӧ�ո�����

				if (num.equals(WdsWlPubTool.DOUBLE_ZERO)) {
					getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
					.setCellBackGround(i, "invcode", Color.red);					
				} else{
					if (Double.parseDouble(num.toString().trim()) > invVolume)
						getBillCardPanelWrapper().getBillCardPanel()
						.getBodyPanel().setCellBackGround(i, "invcode",
								Color.red);
					else
						getBillCardPanelWrapper().getBillCardPanel()
						.getBodyPanel().setCellBackGround(i, "invcode",
								Color.white);
				}
			}
		}
	}
//
//	private int brow = 0;
//	private double m_geb_banum;

	/**
	 * �鿴��ϸ
	 */
	@Override
	protected void onCkmx() throws Exception {
		// TODO Auto-generated method stub
		if (getBufferData().getVOBufferSize() <= 0) {
			getBillUI().showErrorMessage("������û�����ݻ���û��ѡ����嵥�ݣ����ܽ��в���!");
			return;
		}
		// �õ�ȫ������VO
		TbGeneralBVO[] tbGeneralBVOs = null;

		if (getBillManageUI().isListPanelSelected()) {
			// if (getBufferData().getVOBufferSize() != 0) {

			tbGeneralBVOs = (TbGeneralBVO[]) getBillManageUI()
					.getBillListWrapper().getVOFromUI().getChildrenVO();
			// }

		} else {

			// if (getBufferData().getVOBufferSize() != 0) {
			tbGeneralBVOs = (TbGeneralBVO[]) getBillCardPanelWrapper()
					.getBillVOFromUI().getChildrenVO();
			// }

		}
		// �жϱ����Ƿ�������
		if (null == tbGeneralBVOs || tbGeneralBVOs.length == 0) {
			getBillUI().showErrorMessage("������û�����ݣ����ܽ��в�����");
			return;
		}

		TrayDisposeDetailDlg tdpDlg = new TrayDisposeDetailDlg("80DD",
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey(), "8004040214", tbGeneralBVOs);
		AggregatedValueObject[] vos = tdpDlg.getReturnVOs(tbGeneralBVOs);
	}
	
	private IUAPQueryBS getQueryBO(){
		if(iuap == null){
			iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
		}
		return iuap;
	}
	
	private MyDelegator getDelegator(){
		return  (MyDelegator)getBusiDelegator();
	}

	private Object getCardPanelBodyValue(int rowIndex, String strKey){
		return getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(rowIndex, strKey);
	}
	
	private void setCardPanelBodyValue(int row,String key,Object oValue){
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(oValue, row, key);
	}

	/**
	 * �Զ����
	 */
	@Override
	protected void onZdrk() throws Exception {
		// ��λ
		String[] cargdocs = LoginInforHelper.getSpaceByLogUser(myClientUI._getOperator());
		if(cargdocs== null||cargdocs.length==0||cargdocs.length>1)
			throw new BusinessException("��ȡ��λ��Ϣ�쳣");

		
		
	//��֤  ��Ϣ����Ϊ��
		// ��ͷ
		TbGeneralBVO[] tbGeneralBVOs = (TbGeneralBVO[])getBillCardPanelWrapper().getBillCardPanel()
		.getBillModel().getBodyValueVOs(TbGeneralBVO.class.getName());

		if(tbGeneralBVOs == null||tbGeneralBVOs.length ==0){
			throw new BusinessException("��������Ϊ��");
		}
		// ��֤��Ʒ����
		for (TbGeneralBVO body:tbGeneralBVOs) {
			body.validateOnZdrk();
		}
		
		Object o = getDelegator().autoInStore(tbGeneralBVOs, 
				LoginInforHelper.getCwhid(myClientUI._getOperator()), cargdocs[0]);
		if(o == null){
			myClientUI.showHintMessage("�������");
			return;
		}
			
		
		// ��ȡ��ǰ�����е�Ӧ����������ʵ�����������бȽϣ����ݱȽϽ��������ɫ��ʾ
		// ��ɫ��û��ʵ������-1����ɫ����ʵ������������������1����ɫ��ʵ��������Ӧ���������0
		
		Map<String, Integer> retInfor = (Map<String, Integer>)o;
		
		int row = 0;
		for(TbGeneralBVO body:tbGeneralBVOs){
			int flag = PuPubVO.getInteger_NullAs(retInfor.get(body.getGeb_cinvbasid()), 0);
			if(flag == -1){
				getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().setCellBackGround(row,
						"invcode", Color.red);
			}else if(flag == 0){
				getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().setCellBackGround(row,
						"invcode", Color.white);
				setCardPanelBodyValue(row, "geb_banum", getCardPanelBodyValue(row, "geb_bsnum"));
				setCardPanelBodyValue(row, "geb_anum", getCardPanelBodyValue(row, "geb_snum"));
			}else{
				getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().setCellBackGround(row,
						"invcode", Color.white);
			}
			row ++;
		}
		
		
//		if (Double.parseDouble(num.toString().trim()) > invVolume)
//			getBillCardPanelWrapper().getBillCardPanel()
//					.getBodyPanel().setCellBackGround(i,
//							"invcode", Color.red);
//		else
//			getBillCardPanelWrapper().getBillCardPanel()
//					.getBodyPanel().setCellBackGround(i,
//							"invcode", Color.white);
//	} else
//		getBillCardPanelWrapper().getBillCardPanel()
//				.getBodyPanel().setCellBackGround(i, "invcode",
//						Color.red);
		
//
//		// ------------------------------------------�ж������ݻ��Ƿ��ܴ�Ż���
//		if (sotckIsTotal) {
//			// ��ȡ���жϱ����Ƿ���ֵ������ѭ��
//			if (null != tbGeneralBVOs && tbGeneralBVOs.length > 0) {
//				for (int i = 0; i < tbGeneralBVOs.length; i++) {
//
//					// ��ñ���
//					StringBuffer sql = new StringBuffer(
//							"select cdt_pk from bd_cargdoc_tray where cdt_invbasdoc='"
//									+ tbGeneralBVOs[i].getGeb_cinvbasid()
//									+ "' and cdt_traystatus=0 and dr=0 and  pk_cargdoc='"
//									+ cargdocs[0] + "' ");
//
//					// ����������ȫ������
//					ArrayList cdts = new ArrayList();
//					try {
//						cdts = (ArrayList) getQueryBO().executeQuery(sql.toString(),
//								new ArrayListProcessor());
//
//					} catch (BusinessException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//					
//					// �����ݻ�
//					StringBuffer sqlVolume = new StringBuffer(
//							"select def20 from bd_invbasdoc where pk_invbasdoc='");
//					sqlVolume.append(tbGeneralBVOs[i].getGeb_cinvbasid());
//					sqlVolume.append("'");
//					ArrayList cdtsVolume = new ArrayList();
//					cdtsVolume = (ArrayList) getQueryBO().executeQuery(sqlVolume
//							.toString(), new ArrayListProcessor());
//					// ��Ʒ��ǰ���ݻ�
//					double invVolume = 0;
//					if (null != cdts
//							&& null != cdtsVolume
//							&& null != cdtsVolume.get(0)
//							&& null != ((Object[]) cdtsVolume.get(0))[0]
//							&& !"".equals(((Object[]) cdtsVolume.get(0))[0]
//									.toString())) {
//						invVolume = Double.parseDouble(((Object[]) cdtsVolume
//								.get(0))[0].toString())
//								* cdts.size();
//					} else {
//						getBillUI().showErrorMessage("����д�����ݻ���");
//						return;
//					}
//
//					// ��ȡ��ǰ�����е�Ӧ����������ʵ�����������бȽϣ����ݱȽϽ��������ɫ��ʾ
//					// ��ɫ��û��ʵ����������ɫ����ʵ����������������������ɫ��ʵ��������Ӧ���������
//					Object num = getBillCardPanelWrapper().getBillCardPanel()
//							.getBodyValueAt(i, "geb_bsnum");// Ӧ�ո�����
//
//					if (null != num && !"".equals(num)) {
//						if (Double.parseDouble(num.toString().trim()) > invVolume)
//							getBillCardPanelWrapper().getBillCardPanel()
//									.getBodyPanel().setCellBackGround(i,
//											"invcode", Color.red);
//						else
//							getBillCardPanelWrapper().getBillCardPanel()
//									.getBodyPanel().setCellBackGround(i,
//											"invcode", Color.white);
//					} else
//						getBillCardPanelWrapper().getBillCardPanel()
//								.getBodyPanel().setCellBackGround(i, "invcode",
//										Color.red);
//				}
//
//			}
//
//			for (int i = 0; i < tbGeneralBVOs.length; i++) {
//				// ��ñ���
//				StringBuffer sql = new StringBuffer(
//						" select cdt_pk from bd_cargdoc_tray where cdt_invbasdoc='"
//								+ tbGeneralBVOs[i].getGeb_cinvbasid()
//								+ "' and cdt_traystatus=0 and dr=0 and  pk_cargdoc='"
//								+ cargdocs[0] + "'  ");
//				// IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance()
//				// .lookup(IUAPQueryBS.class.getName());
//				// ����������ȫ������
//				ArrayList cdts = new ArrayList();
//				try {
//					cdts = (ArrayList) getQueryBO().executeQuery(sql.toString(),
//							new ArrayListProcessor());
//
//				} catch (BusinessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				// �����ݻ�
//				StringBuffer sqlVolume = new StringBuffer(
//						"select def20 from bd_invbasdoc where pk_invbasdoc='");
//				sqlVolume.append(tbGeneralBVOs[i].getGeb_cinvbasid());
//				sqlVolume.append("'");
//				ArrayList cdtsVolume = new ArrayList();
//				cdtsVolume = (ArrayList) getQueryBO().executeQuery(sqlVolume
//						.toString(), new ArrayListProcessor());
//				// ��Ʒ��ǰ���ݻ�
//				double invVolume = 0;
//				if (null != cdts
//						&& null != cdtsVolume
//						&& null != cdtsVolume.get(0)
//						&& null != ((Object[]) cdtsVolume.get(0))[0]
//						&& !"".equals(((Object[]) cdtsVolume.get(0))[0]
//								.toString())) {
//					invVolume = Double.parseDouble(((Object[]) cdtsVolume
//							.get(0))[0].toString())
//							* cdts.size();
//				} else {
//					getBillUI().showErrorMessage("����д���̻������ݻ���");
//					return;
//				}
//				// ��⸨����
//				String geb_bsnum = "";
//				if (null != tbGeneralBVOs[i].getGeb_bsnum()) {
//					geb_bsnum = tbGeneralBVOs[i].getGeb_bsnum().toString();
//				}
//				// ��⸨����
//				double geb_bsnumd = 0;
//				if (null != geb_bsnum && !"".equals(geb_bsnum)) {
//					geb_bsnumd = Double.parseDouble(geb_bsnum);
//				}
//
//				if (geb_bsnumd > invVolume) {
//					getBillUI()
//							.showErrorMessage("����������ڵ�ǰ����ݻ���(�������Ϊ��ɫ�����̲���) ");
//					return;
//				}
//			}
//			
//
//		} else {
//
//			StringBuffer sql_cargtrays = new StringBuffer(
//					" dr=0 and  pk_cargdoc='" + cargdocs[0] + "'  ");
//
//			ArrayList cargtrays = (ArrayList) getQueryBO().retrieveByClause(
//					BdCargdocTrayVO.class, sql_cargtrays.toString());
//			if (null == cargtrays || cargtrays.size() == 0
//					|| null == cargtrays.get(0)) {
//				getBillUI().showErrorMessage("�ֲֲֿ�û���������̣�");
//				return;
//			}
//
//			
//		}
		
		// ------------------------------------------�ж������ݻ��Ƿ��ܴ�Ż���
		
		
		
		
		
//		
//		
//
//		// ���������Ϣ
//		for (int i = 0; i < tbGeneralBVOs.length; i++) {
//			// ��ñ���
//			StringBuffer sql = new StringBuffer();
//			if (sotckIsTotal) {
//				sql
//						.append("select cdt_pk from bd_cargdoc_tray where cdt_invbasdoc='"
//								+ tbGeneralBVOs[i].getGeb_cinvbasid()
//								+ "' and cdt_traystatus=0 and dr=0 and  pk_cargdoc='"
//								+ cargdocs[0] + "' ");
//				String mySql = " select distinct cdt_pk from tb_general_b_b where pk_invbasdoc='"
//						+ tbGeneralBVOs[i].getGeb_cinvbasid()
//						+ "' and pwb_pk !='"
//						+ tbGeneralBVOs[i].getGeb_cgeneralbid()
//						+ "' and dr=0 and pwb_pk in (' ";
//
//				boolean myBoolean = false;
//				for (int j = 0; j < tbGeneralBVOs.length; j++) {
//					if (null != tbGeneralBVOs[i].getGeb_cinvbasid()
//							&& null != tbGeneralBVOs[j].getGeb_cinvbasid()
//							&& null != tbGeneralBVOs[i].getGeb_cgeneralbid()
//							&& null != tbGeneralBVOs[j].getGeb_cgeneralbid()) {
//						if (tbGeneralBVOs[i].getGeb_cinvbasid().equals(
//								tbGeneralBVOs[j].getGeb_cinvbasid())
//								&& !tbGeneralBVOs[i].getGeb_cgeneralbid()
//										.equals(
//												tbGeneralBVOs[j]
//														.getGeb_cgeneralbid())) {
//							mySql += tbGeneralBVOs[j].getGeb_cgeneralbid()
//									+ "','";
//							myBoolean = true;
//						}
//					}
//				}
//				mySql += "') ";
//				if (myBoolean) {
//					sql.append(" and cdt_pk not in (" + mySql + ")");
//				}
//			} else {
//				sql
//						.append("select cdt_pk from bd_cargdoc_tray where  dr=0 and  pk_cargdoc='"
//								+ cargdocs[0] + "' ");
//			}
//
//			// ����������ȫ������
//			ArrayList cdts = new ArrayList();
//			try {
//				cdts = (ArrayList) getQueryBO().executeQuery(sql.toString(),
//						new ArrayListProcessor());
//
//			} catch (BusinessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			// �����ݻ�
//			StringBuffer sqlVolume = new StringBuffer(
//					"select def20 from bd_invbasdoc where pk_invbasdoc='");
//			sqlVolume.append(tbGeneralBVOs[i].getGeb_cinvbasid());
//			sqlVolume.append("' and dr=0 ");
//			ArrayList cdtsVolume = new ArrayList();
//			cdtsVolume = (ArrayList) getQueryBO().executeQuery(sqlVolume.toString(),
//					new ArrayListProcessor());
//
//			double invTrayVolume = 0;
//			if (sotckIsTotal) {
//				invTrayVolume = Double.parseDouble(((Object[]) cdtsVolume
//						.get(0))[0].toString());
//			} else {
//				invTrayVolume = -1;
//			}
//			// ��⸨����
//			String geb_bsnum = "";
//			if (null != tbGeneralBVOs[i].getGeb_bsnum()) {
//				geb_bsnum = tbGeneralBVOs[i].getGeb_bsnum().toString();
//			}
//			// ��ǰ��⸨����
//			double geb_bsnumd = 0;
//			if (null != geb_bsnum && !"".equals(geb_bsnum)) {
//				geb_bsnumd = Double.parseDouble(geb_bsnum);
//			}
//			// ������̸���
//			// int traynum=0;
//			// if(geb_bsnumd!=0&&invTrayVolume!=0){
//			// traynum=
//			// }
//			//
//			if (null != cdts) {
//
//			}
//
//			// TbGeneralBBVO[] tbGeneralBBVO = null;
//
//			// ���Ҫɾ����VO
//			StringBuffer tbbsql = new StringBuffer("pwb_pk='");
//			tbbsql.append(tbGeneralBVOs[i].getGeb_cgeneralbid());
//			tbbsql.append("' and pk_invbasdoc='");
//			tbbsql.append(tbGeneralBVOs[i].getGeb_cinvbasid());
//			tbbsql.append("' and dr = 0");
//
//			ArrayList dtbbvos = new ArrayList();
//			try {
//
//				dtbbvos = (ArrayList) getQueryBO().retrieveByClause(
//						TbGeneralBBVO.class, tbbsql.toString());
//			} catch (BusinessException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			// 
//			ArrayList tbGeneralBBVOs = new ArrayList();
//			// ArrayList bdCargdocTrayVOs = new ArrayList();
//			String Cdt_pk = "";
//
//			int k = 0;
//			if (-1 != invTrayVolume) {
//
//				while (geb_bsnumd > invTrayVolume) {
//					TbGeneralBBVO tbgbbvo = new TbGeneralBBVO();
//					// ����
//					tbgbbvo.setGebb_vbatchcode(tbGeneralBVOs[i]
//							.getGeb_vbatchcode());
//					// ��д����
//					tbgbbvo.setGebb_lvbatchcode(tbGeneralBVOs[i]
//							.getGeb_backvbatchcode());
//					// �к�
//					tbgbbvo.setGebb_rowno(k + 1 + "0");
//					// ���ⵥ��������
//					tbgbbvo.setPwb_pk(tbGeneralBVOs[i].getGeb_cgeneralbid());
//					// ������
//					tbgbbvo.setGebb_hsl(tbGeneralBVOs[i].getGeb_hsl());
//					// �˻���������
//					tbgbbvo
//							.setPk_invbasdoc(tbGeneralBVOs[i]
//									.getGeb_cinvbasid());
//					// ��ⵥ�ӱ�����
//					tbgbbvo.setGeb_pk(tbGeneralBVOs[i].getGeb_pk());
//					// ����
//					tbgbbvo.setGebb_nprice(tbGeneralBVOs[i].getGeb_nprice());
//					// ���
//					tbgbbvo.setGebb_nmny(tbGeneralBVOs[i].getGeb_nmny());
//					// ����ʵ�ʴ������
//					tbgbbvo.setGebb_num(new UFDouble(invTrayVolume));
//					geb_bsnumd = geb_bsnumd - invTrayVolume;
//					// ��������
//					if (null != ((Object[]) cdts.get(k))[0]) {
//						tbgbbvo.setCdt_pk(((Object[]) cdts.get(k))[0]
//								.toString());
//					}
//					// DR
//					tbgbbvo.setDr(0);
//					tbgbbvo.setGeb_pk(tbGeneralBVOs[i].getGeb_pk());
//					tbGeneralBBVOs.add(tbgbbvo);
//					// // ����״̬
//					//
//					// if (null != ((Object[]) cdts.get(k))[0]) {
//					// Cdt_pk = ((Object[]) cdts.get(k))[0].toString();
//					// }
//					// BdCargdocTrayVO bdCargdocTrayVO = (BdCargdocTrayVO) query
//					// .retrieveByPK(BdCargdocTrayVO.class, Cdt_pk);
//					// bdCargdocTrayVO.setCdt_traystatus(new Integer(1));
//					// bdCargdocTrayVOs.add(bdCargdocTrayVO);
//					k++;
//				}
//			}
//
//			TbGeneralBBVO tbgbbvo1 = new TbGeneralBBVO();
//			// ����
//			tbgbbvo1.setGebb_vbatchcode(tbGeneralBVOs[i].getGeb_vbatchcode());
//			// ��д����
//			tbgbbvo1.setGebb_lvbatchcode(tbGeneralBVOs[i]
//					.getGeb_backvbatchcode());
//			// �к�
//			tbgbbvo1.setGebb_rowno(k + 1 + "0");
//			// ���ⵥ��������
//			tbgbbvo1.setPwb_pk(tbGeneralBVOs[i].getGeb_cgeneralbid());
//			// ������
//			tbgbbvo1.setGebb_hsl(tbGeneralBVOs[i].getGeb_hsl());
//			// �˻���������
//			tbgbbvo1.setPk_invbasdoc(tbGeneralBVOs[i].getGeb_cinvbasid());
//			// ��ⵥ�ӱ�����
//			tbgbbvo1.setGeb_pk(tbGeneralBVOs[i].getGeb_pk());
//			// ����
//			tbgbbvo1.setGebb_nprice(tbGeneralBVOs[i].getGeb_nprice());
//			// ���
//			tbgbbvo1.setGebb_nmny(tbGeneralBVOs[i].getGeb_nmny());
//			// ����ʵ�ʴ������
//			tbgbbvo1.setGebb_num(new UFDouble(geb_bsnumd));
//			// ��������
//			if (null != ((Object[]) cdts.get(k))[0]) {
//				tbgbbvo1.setCdt_pk(((Object[]) cdts.get(k))[0].toString());
//			}
//			// DR
//			tbgbbvo1.setDr(0);
//			tbGeneralBBVOs.add(tbgbbvo1);
//			// // ����״̬
//			//
//			// if (null != ((Object[]) cdts.get(k))[0]) {
//			// Cdt_pk = ((Object[]) cdts.get(k))[0].toString();
//			// }
//			// BdCargdocTrayVO bdCargdocTrayVO = (BdCargdocTrayVO) query
//			// .retrieveByPK(BdCargdocTrayVO.class, Cdt_pk);
//			// bdCargdocTrayVO.setCdt_traystatus(new Integer(1));
//			// bdCargdocTrayVOs.add(bdCargdocTrayVO);
//
//			//
//			TbGeneralBBVO[] tbGeneralBBVO = new TbGeneralBBVO[tbGeneralBBVOs
//					.size()];
//			tbGeneralBBVOs.toArray(tbGeneralBBVO);
//
//			// ����Զ���ӿ�
//			Iw8004040210 iw = (Iw8004040210) NCLocator.getInstance().lookup(
//					Iw8004040210.class.getName());
//			try {
//				iw.delAndInsertTbGeneralBBVO(dtbbvos, tbGeneralBBVO);
//			} catch (BusinessException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

//		TrayDisposeDetailDlg tdpDlg = new TrayDisposeDetailDlg("80DD",
//				myClientUI._getOperator(),
//				myClientUI._getCorp().getPrimaryKey(), "8004040214", tbGeneralBVOs);
//		
//		AggregatedValueObject[] vos = tdpDlg.getReturnVOs(tbGeneralBVOs);
//
//		int rownum = getBillCardPanelWrapper().getBillCardPanel()
//				.getBillTable().getRowCount();
//		for (int i = 0; i < rownum; i++) {
//			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
//					getBillCardPanelWrapper().getBillCardPanel()
//							.getBodyValueAt(i, "geb_bsnum"), i, "geb_banum");
//			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
//					getBillCardPanelWrapper().getBillCardPanel()
//							.getBodyValueAt(i, "geb_snum"), i, "geb_anum");
//		}
//		if (null == vos || vos.length == 0) {
//
//			return;
//		}

	}

	// �к�
	private int allRowNO = -1;

	@Override
	protected void onBoLineAdd() throws Exception {
		// TODO Auto-generated method stub
		super.onBoLineAdd();
		int selectRow = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getSelectedRow();

		// �к�

		if (-1 == allRowNO) {
			allRowNO = selectRow;
		} else {
			allRowNO++;
		}
		String rowno = allRowNO + 1 + "0";
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
				rowno,
				getBillCardPanelWrapper().getBillCardPanel().getBillTable()
						.getSelectedRow(), "geb_crowno");
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
				getRandomNum(),
				getBillCardPanelWrapper().getBillCardPanel().getBillTable()
						.getSelectedRow(), "geb_cgeneralbid");

		// ��λ
		String geb_space = nc.ui.wds.w8000.CommonUnit
				.getCargDocName(ClientEnvironment.getInstance().getUser()
						.getPrimaryKey());
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
				geb_space,
				getBillCardPanelWrapper().getBillCardPanel().getBillTable()
						.getSelectedRow(), "geb_space");

	}

	@Override
	protected void onBoLineIns() throws Exception {
		// TODO Auto-generated method stub
		super.onBoLineIns();
		int selectRow = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getSelectedRow();

		// �к�

		if (-1 == allRowNO) {
			allRowNO = selectRow;
		} else {
			allRowNO++;
		}
		String rowno = allRowNO + 1 + "0";
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
				rowno,
				getBillCardPanelWrapper().getBillCardPanel().getBillTable()
						.getSelectedRow(), "geb_crowno");
	}

	// �ж�����ӻ����޸�
	private boolean addoredit = true;
	// �Ƿ����޸�
	private boolean isedit = true;

	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		// ��ǰ��¼�ı���Ա�ܹ���Ļ�Ʒ
		List InvLisk = nc.ui.wds.w8000.CommonUnit
				.getInvbasdoc_Pk(ClientEnvironment.getInstance().getUser()
						.getPrimaryKey());
		boolean isControl = false;
//		if ("1".equals(st_type)) {
//			isControl = true;
//		}
		// ����Ҫ�л��б�ģʽ��һ�Σ������������
		super.onBoReturn();
//		if (!isControl) {
//			// �ж��Ƿ�Ϊ�գ����Ϊ�� ˵����ǰ��¼�߲��ǲֿⱣ��Ա
//			if (sotckIsTotal) {
//				if (null != InvLisk && InvLisk.size() > 0) {
//					List list = new ArrayList();
//					TbGeneralBVO[] generalbvo = null;
//					// ��ȡ��ǰѡ����
//					AggregatedValueObject billvo = getBufferData()
//							.getCurrentVO();
//					for (int i = 0; i < InvLisk.size(); i++) {
//						// �ж�ѡ�����Ƿ�������
//						if (null != billvo && null != billvo.getParentVO()
//								&& null != billvo.getChildrenVO()
//								&& billvo.getChildrenVO().length > 0) {
//							// ȡ���ӱ�����
//							generalbvo = (TbGeneralBVO[]) billvo
//									.getChildrenVO();
//							// �����ж��Ƿ�Ϊ��Ȼ�����ѭ��
//							if (null != generalbvo && generalbvo.length > 0) {
//								for (int j = 0; j < generalbvo.length; j++) {
//									// �����ǰ��¼�ֿ߲��еĵ�Ʒ�����͵����еĵ�Ʒ������ȷŵ�һ���������� ȥ
//									// Ȼ�����ҳ��
//									if (InvLisk.get(i).equals(
//											generalbvo[j].getGeb_cinvbasid())) {
//										list.add(generalbvo[j]);
//									}
//								}
//							}
//						}
//					}
//
//					if (null != list && list.size() > 0) {
//						// ��������ת��
//						generalbvo = new TbGeneralBVO[list.size()];
//						generalbvo = (TbGeneralBVO[]) list.toArray(generalbvo);
//						billvo.setChildrenVO(generalbvo);
//						// �ڸ��µ���ǰѡ������
//						getBufferData().setCurrentVO(billvo);
//						// ����ָ����ť���Զ�ȡ����ť����
//						// changeButton(true);
//					} else {
//						// �����������û������ ��ǰ��¼���������ֿ�ı���Ա
//						// �Ѹ������ܰ�ť��Ϊ������
//						// getButtonManager().getButton(ISsButtun.fzgn).setEnabled(
//						// false);
//						billvo.setChildrenVO(null);
//						getBufferData().setCurrentVO(billvo);
//					}
//				}
//			}
//		}
		// super.onBoEdit();

//		if (isControl) {
//			// �����շ����ͱ�ע�ɱ༭
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//					"geh_cdispatcherid").setEdit(true);
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//					"geh_cwarehouseid").setEdit(true);
//			getBillCardPanelWrapper().getBillCardPanel()
//					.getHeadItem("geh_corp").setEdit(true);
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//					"geh_calbody").setEdit(true);
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//					"geh_cwhsmanagerid").setEdit(true);
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//					"geh_cdptid").setEdit(true);
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//					"geh_cothercorpid").setEdit(true);
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//					"geh_cothercalbodyid").setEdit(true);
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//					"geh_cotherwhid").setEdit(true);
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//					"geh_cbizid").setEdit(true);
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vnote")
//					.setEdit(true);
//			getButtonManager().getButton(IBillButton.Line).setEnabled(false);
//
//			getBillUI().updateButtonUI();
//
//		} else {
//			getButtonManager().getButton(
//					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zdtp)
//					.setEnabled(true);
//			getButtonManager().getButton(
//					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zdrk)
//					.setEnabled(true);
//			getButtonManager().getButton(
//					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zj).setEnabled(
//					false);
//			getButtonManager().getButton(IBillButton.Line).setEnabled(false);
//			getBillUI().updateButtonUI();
//		}
//
//		if ("3".equals(st_type)) {
//			// �����շ����ͱ�ע�ɱ༭
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//					"geh_cdispatcherid").setEdit(true);
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//					"geh_cwarehouseid").setEdit(true);
//			getBillCardPanelWrapper().getBillCardPanel()
//					.getHeadItem("geh_corp").setEdit(true);
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//					"geh_calbody").setEdit(true);
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//					"geh_cwhsmanagerid").setEdit(true);
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//					"geh_cdptid").setEdit(true);
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//					"geh_cothercorpid").setEdit(true);
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//					"geh_cothercalbodyid").setEdit(true);
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//					"geh_cotherwhid").setEdit(true);
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//					"geh_cbizid").setEdit(true);
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vnote")
//					.setEdit(true);
//			getButtonManager().getButton(IBillButton.Line).setEnabled(false);
//
//			getButtonManager().getButton(
//					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zdtp)
//					.setEnabled(true);
//			getButtonManager().getButton(
//					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zdrk)
//					.setEnabled(true);
//			getButtonManager().getButton(
//					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zj).setEnabled(
//					false);
//			// getButtonManager().getButton(IBillButton.Line).setEnabled(false);
//			getBillUI().updateButtonUI();
//		}
//		getButtonManager().getButton(
//				nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
//				.setEnabled(false);
//		getButtonManager().getButton(
//				nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
//				.setEnabled(false);
//		super.onBoEdit();
//		getButtonManager().getButton(IBillButton.Line).setEnabled(false);
//		getBillUI().updateButtonUI();
		addoredit = false;
		isedit = true;
		// createBusinessAction();
	}

	public void showZeroLikeNull(boolean bShowZeroLikeNull) {
		BillRendererVO vo = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getRendererVO();
		vo.setShowZeroLikeNull(bShowZeroLikeNull);
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
				.setShowFlags(vo);
	}

	/**
	 * ��λ�����䵥��
	 * 
	 * @param vos
	 * @return
	 */
	private MyBillVO changeTbOuttoTbgen(AggregatedValueObject[] vos) {
		MyBillVO myBillVO = new MyBillVO();
		// ������ⵥ����VO
		TbGeneralHVO tbGeneralHVO = new TbGeneralHVO();
		// �������ⵥ����VO
		TbOutgeneralHVO firstVO = (TbOutgeneralHVO) vos[0].getParentVO();

		// ����˵����ݺ�

		// ��ӵ������ⵥ���ݺ�
		tbGeneralHVO.setGeh_vbillcode(firstVO.getVbillcode());
		// ��ⵥ����
		tbGeneralHVO.setGeh_billtype(WdsWlPubConst.BILLTYPE_OTHER_IN);
		// �˵���ͷ����
		// tbGeneralHVO.setPwb_pk(tbProdwaybillVO.getPwb_pk());
		// ���˳��ⵥ��ͷ����
		tbGeneralHVO.setGeh_cgeneralhid(firstVO.getGeneral_pk());
		// �������ݵ��ݺ�
		// try {
		// billCode = getBillCode("0214", ClientEnvironment.getInstance()
		// .getUser().getCorpId(), "", "");
		// tbGeneralHVO.setGeh_billcode(billCode);
		// } catch (BusinessException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// ���⹫˾����
		if (null != firstVO.getPk_fcorp()) {
			tbGeneralHVO.setGeh_cothercorpid(firstVO.getPk_fcorp());
		}
		// ��������֯����
		if (null != firstVO.getPk_calbody()) {
			tbGeneralHVO.setGeh_cothercalbodyid(firstVO.getPk_calbody());
		}
		// ����ֿ�����
		if (null != firstVO.getSrl_pkr()) {
			tbGeneralHVO.setGeh_cotherwhid(firstVO.getSrl_pkr());
		}
		// ҵ��Ա����
		if (null != firstVO.getCbizid()) {
			tbGeneralHVO.setGeh_cbizid(firstVO.getCbizid());
		}

		// �շ�����ID
		if (null != firstVO.getCdispatcherid()) {
			tbGeneralHVO.setGeh_cdispatcherid(firstVO.getCdispatcherid());
		}

		// ��⹫˾����
		if (null != ClientEnvironment.getInstance().getUser().getCorpId()) {
			tbGeneralHVO.setGeh_corp(ClientEnvironment.getInstance().getUser()
					.getCorpId().toString());
		}
		// �������֯����
		if (null != firstVO.getPk_calbodyr()) {
			tbGeneralHVO.setGeh_calbody(firstVO.getPk_calbodyr().toString());
		}
		// ���ֿ�����
		if (null != firstVO.getSrl_pk()) {
			tbGeneralHVO.setGeh_cwarehouseid(firstVO.getSrl_pk().toString());
		}

		// ������Ա����
		if (null != firstVO.getCwhsmanagerid()) {
			tbGeneralHVO.setGeh_cwhsmanagerid(firstVO.getCwhsmanagerid());
		}

		// �������ͱ�־
		// tbGeneralHVO.setGeh_fallocflag(firstVO.getFallocflag());
		// �Ƿ�����˻�
		// tbGeneralHVO.setGeh_freplenishflag(firstVO.getFreplenishflag());
		// ҵ������
		if (null != firstVO.getCbiztype()) {
			tbGeneralHVO.setGeh_cbiztype(firstVO.getCbiztype());
		}

		// �Ƶ����ڣ���������

		tbGeneralHVO.setCopetadate(new UFDate(new Date()));
		tbGeneralHVO.setGeh_dbilldate(new UFDate(new Date()));
		// �Ƶ���
		tbGeneralHVO.setCoperatorid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey());
		// ��ⵥ����
//		tbGeneralHVO.setGeh_billtype(3);
		// �������VO
		myBillVO.setParentVO(tbGeneralHVO);

		// ��ȡ�ӱ�vo
		String pk = firstVO.getGeneral_pk().toString();
		String sql = " general_pk='" + pk + "' and dr=0 ";
//		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
//				IUAPQueryBS.class.getName());
		// ȫ���ӱ�VO
		List myTemp = new ArrayList();
		// ǰ̨��ʾ��VO
		List myTempShow = new ArrayList();

		myTempall = new ArrayList();
		try {
			ArrayList ttcs = (ArrayList) getQueryBO().retrieveByClause(
					TbOutgeneralBVO.class, sql.toString());
			// ��ǰ��¼�ı���Ա�ܹ���Ļ�Ʒ
			List InvLisk = nc.ui.wds.w8000.CommonUnit
					.getInvbasdoc_Pk(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
			for (int i = 0; i < ttcs.size(); i++) {
				TbOutgeneralBVO gbvo = (TbOutgeneralBVO) ttcs.get(i);
				TbGeneralBVO tbGeneralBVO = new TbGeneralBVO();
				// ����к�
				// if (null != gbvo.getCrowno()) {
				// tbGeneralBVO.setGeb_crowno(gbvo.getCrowno());
				// }
				tbGeneralBVO.setGeb_crowno(i + 1 + "0");
				// ���������������
				if (null != gbvo.getCinventoryid()) {
					tbGeneralBVO.setGeb_cinvbasid(gbvo.getCinventoryid());
				}

				// ����˵���ͷ����
				// tbGeneralBVO.setPwb_pk(tbProdwaybillVO.getPwb_pk());
				// �˵���������
				// tbGeneralBVO.setPwbb_pk(ttc.getPwbb_pk());
				// ���˳��ⵥ��ͷ����
				if (null != firstVO.getGeneral_pk()) {
					tbGeneralBVO.setGeb_cgeneralhid(firstVO.getGeneral_pk());
				}

				// ���˳��ⵥ��������
				if (null != gbvo.getGeneral_b_pk()) {
					tbGeneralBVO.setGeb_cgeneralbid(gbvo.getGeneral_b_pk());
				}
				// ������
				if (null != gbvo.getHsl()) {
					tbGeneralBVO.setGeb_hsl(new UFDouble(gbvo.getHsl()
							.toString()));
				}
				// ��λ
				// if (null != ttc.getPk_measdoc()) {
				// tbGeneralBVO.setPk_measdoc(ttc.getPk_measdoc());
				// }
				// ���κ�
				if (null != gbvo.getVbatchcode()) {
					// ʹ�õ����κ�
					tbGeneralBVO.setGeb_vbatchcode(gbvo.getVbatchcode());

				}
				// Ҫ��д�����κ�
				if (null != gbvo.getLvbatchcode()) {
					// Ҫ��д�����κ�
					tbGeneralBVO.setGeb_backvbatchcode(gbvo.getLvbatchcode());
				}
				// �������Σ���ѯ�������ڣ�ʧЧ����
				String vbcsql = "select dproducedate ,dvalidate  from scm_batchcode where pk_invbasdoc='"
						+ tbGeneralBVO.getGeb_cinvbasid()
						+ "' and vbatchcode='"
						+ tbGeneralBVO.getGeb_vbatchcode() + "' and dr=0";
				ArrayList vbc = (ArrayList) getQueryBO().executeQuery(vbcsql,
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
				// double noutassistnum = 0.00;
				// double ntranoutastnum = 0.00;
				// if (null != gbvo[10]) {
				// noutassistnum = Double.parseDouble(gbvo[10].toString());
				// }
				// if (null != gbvo[11]) {
				// ntranoutastnum = Double.parseDouble(gbvo[11].toString());
				// }
				// Ӧ�ո�����
				if (null != gbvo.getNoutassistnum()) {
					tbGeneralBVO.setGeb_bsnum(gbvo.getNoutassistnum());
				}
				// ʵ�ո�����
				// tbGeneralBVO.setGeb_banum(tbGeneralBVO.getGeb_bsnum());
				// ���ʵ��������Ӧ������
				// double noutnum = 0.00;
				// double ntranoutnum = 0.00;
				// if (null != gbvo[5]) {
				// noutnum = Double.parseDouble(gbvo[5].toString());
				// }
				// if (null != gbvo[6]) {
				// ntranoutnum = Double.parseDouble(gbvo[6].toString());
				// }
				if (null != gbvo.getNoutnum()) {
					tbGeneralBVO.setGeb_snum(gbvo.getNoutnum());
				}
				// tbGeneralBVO.setGeb_anum(tbGeneralBVO.getGeb_snum());
				// ����
				if (null != gbvo.getNprice()
						&& !"".equals(gbvo.getNprice().toString())) {
					tbGeneralBVO.setGeb_nprice(new UFDouble(gbvo.getNprice()
							.toString()));
				}
				// ���
				if (null != gbvo.getNmny()
						&& !"".equals(gbvo.getNmny().toString())) {
					tbGeneralBVO.setGeb_nmny(gbvo.getNmny());
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
					if (null != gbvo.getCinventoryid()) {
						if (gbvo.getCinventoryid().equals(InvLisk.get(j))) {
							myTempShow.add(tbGeneralBVO);
							break;
						}
					}
				}
				myTempall.add(tbGeneralBVO);
				addoredit = true;
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
		TbOutgeneralHVO firstVO = (TbOutgeneralHVO) vos[0].getParentVO();
		if (null != firstVO && null != firstVO.getGeneral_pk()
				&& firstVO.getGeneral_pk().length() > 0) {
			TbGeneralHVO tbGeneralHVO = new TbGeneralHVO();
			TbGeneralBVO[] tbGeneralBVOs = null;
			// TbOutgeneralHVO generalh = null;
			// TbOutgeneralBVO[] generalb = null;
			String strWhere = " dr = 0 and geh_cgeneralhid = '"
					+ firstVO.getGeneral_pk() + "'";
//			IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
//					IUAPQueryBS.class.getName());
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
					if (sotckIsTotal) {

						if (null != invLisk && invLisk.size() > 0) {
							for (int i = 0; i < invLisk.size(); i++) {
								for (int j = 0; j < list.size(); j++) {
									TbGeneralBVO outb = (TbGeneralBVO) list
											.get(j);
									if (invLisk.get(i).equals(
											outb.getGeb_cinvbasid())) {
										outb.setStatus(VOStatus.UPDATED);
										tmpList.add(outb);
									}
								}
							}
							// myTempall = list;
						}
					} else {
						for (int j = 0; j < list.size(); j++) {
							TbGeneralBVO outb = (TbGeneralBVO) list.get(j);

							outb.setStatus(VOStatus.UPDATED);
							tmpList.add(outb);

						}
					}
					if (tmpList.size() > 0) {
						tbGeneralBVOs = new TbGeneralBVO[tmpList.size()];
						tbGeneralBVOs = (TbGeneralBVO[]) tmpList
								.toArray(tbGeneralBVOs);
						MyBillVO mybillvo = new MyBillVO();
						mybillvo.setParentVO(tbGeneralHVO);
						mybillvo.setChildrenVO(tbGeneralBVOs);
						// ���������״̬�Ļ� �޸�
						addoredit = false;
						return mybillvo;
					}
				}
			}
		}
		return null;
	}

	@Override
	protected void onBoSave() throws Exception {
		OtherInBillVO billvo = (OtherInBillVO)getBillCardPanelWrapper().getBillVOFromUI();
		if(billvo == null || billvo.getChildrenVO()==null || billvo.getChildrenVO().length ==0){
			throw new BusinessException("�ӽ����ȡ����Ϊ��");
		}
//		У������
		TbGeneralBVO[] bodys = (TbGeneralBVO[])billvo.getChildrenVO();
		for(TbGeneralBVO body:bodys){
			body.validateOnSave();
		}
		super.onBoSave();
	}



//	@Override
//	protected void onBoCancel() throws Exception {
//		// TODO Auto-generated method stub
//		super.onBoCancel();
//		if (addoredit) {
//			getBufferData().clear();
//			getBillUI().setBillOperate(IBillOperate.OP_INIT);
//			getBillUI().initUI();
//			// super.onBoRefresh();
//		}
//		IVOPersistence perse = (IVOPersistence) NCLocator.getInstance().lookup(
//				IVOPersistence.class.getName());
//		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//				"geh_cdispatcherid").setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//				"geh_cwarehouseid").setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_corp")
//				.setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_calbody")
//				.setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//				"geh_cwhsmanagerid").setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vnote")
//				.setEdit(false);
//		getButtonManager().getButton(
//				nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zdtp).setEnabled(
//				false);
//		getButtonManager().getButton(
//				nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zdrk).setEnabled(
//				false);
//		if (!isControl1) {
//			getButtonManager().getButton(
//					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zj).setEnabled(
//					true);
//		}
//		if (!isControl1) {
//			getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
//
//		}
//		getBillUI().updateButtonUI();
//		// ҳ���ǩ
//		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_corp")
//				.setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_calbody")
//				.setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//				"geh_cwarehouseid").setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//				"geh_cwhsmanagerid").setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_cbizid")
//				.setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//				"geh_cdispatcherid").setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//				"geh_cothercorpid").setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//				"geh_cothercalbodyid").setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
//				"geh_cotherwhid").setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fallocname")
//				.setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vnote")
//				.setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_crowno")
//				.setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("invcode")
//				.setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
//				"geb_vbatchcode").setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_bsnum")
//				.setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_snum")
//				.setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_banum")
//				.setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_anum")
//				.setEdit(false);
//		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vnote")
//		// .setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
//				"geb_virtualbnum").setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
//				"geb_customize2").setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_nprice")
//				.setEdit(false);
//		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_nmny")
//				.setEdit(false);
//
//		isfydj = false;
//		isfirst = true;
//		if (null != st_type && st_type.equals("1")
//				&& getBufferData().getVOBufferSize() > 0) {
//
//			AggregatedValueObject billvo = getBillUI().getVOFromUI();
//			TbGeneralHVO generalhvo = null;
//			if (null != billvo.getParentVO()) {
//				generalhvo = (TbGeneralHVO) billvo.getParentVO();
//			}
//
//			//
//
//			// ǩ�ֺ�
//			if (null != generalhvo.getPwb_fbillflag()
//					&& generalhvo.getPwb_fbillflag() == 3) {
//				getButtonManager().getButton(
//						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
//						.setEnabled(false);
//				getButtonManager().getButton(
//						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
//						.setEnabled(true);
//				getBillUI().updateButtonUI();
//			} else { // ǩ��ǰ
//				getButtonManager().getButton(
//						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
//						.setEnabled(true);
//				getButtonManager().getButton(
//						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
//						.setEnabled(false);
//				getBillUI().updateButtonUI();
//			}
//		}
//		if (null != st_type && st_type.equals("3")
//				&& getBufferData().getVOBufferSize() > 0) {
//
//			AggregatedValueObject billvo = getBillUI().getVOFromUI();
//			TbGeneralHVO generalhvo = null;
//			if (null != billvo.getParentVO()) {
//				generalhvo = (TbGeneralHVO) billvo.getParentVO();
//			}
//
//			//
//
//			// ǩ�ֺ�
//			if (null != generalhvo.getPwb_fbillflag()
//					&& generalhvo.getPwb_fbillflag() == 3) {
//				getButtonManager().getButton(
//						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
//						.setEnabled(false);
//				getButtonManager().getButton(
//						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
//						.setEnabled(true);
//				getBillUI().updateButtonUI();
//			} else { // ǩ��ǰ
//				getButtonManager().getButton(
//						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
//						.setEnabled(true);
//				getButtonManager().getButton(
//						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
//						.setEnabled(false);
//				getBillUI().updateButtonUI();
//			}
//
//		}
//		if (null != st_type && st_type.equals("3")) {
//			getButtonManager().getButton(
//					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zj).setEnabled(
//					true);
//			getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
//			getBillUI().updateButtonUI();
//		}
//		isedit = true;
//		// createBusinessAction();
//	}

	/**
	 * ��ǰ��¼�ı���Աֻ�ܲ�ѯ�Լ���λ����ⵥ
	 */
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();
		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ
		strWhere.append(" and geh_billtype ='"+WdsWlPubConst.BILLTYPE_OTHER_IN+"' ");
		String pk_stordoc = LoginInforHelper.getCwhid(_getOperator());
		strWhere.append(" and geh_cwarehouseid='" + pk_stordoc + "' ");
		String cargdocid = LoginInforHelper.getSpaceByLogUserForStore(myClientUI._getOperator());
		if(cargdocid != null){//���Ǳ���Ա��¼ ���Բ鿴������ⵥ
			strWhere.append(" and pk_cargdoc = '"+cargdocid+"'");
		}
		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());
		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);
		updateBuffer();
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

	private String getRandomNum() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssms");
		String tmp = format.format(new Date());
		tmp = tmp + Math.round((Math.random() * 1000000));
		tmp = tmp.substring(0, 20);
		return tmp;
	}

	/**
	 * ȡ��ǩ��
	 */

	@Override
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
//		Iw8004040210 iw = (Iw8004040210) NCLocator.getInstance().lookup(
//				Iw8004040210.class.getName());
//		iw.canceldelete8004040210("CANCELSIGN", "DELETE"/* ��д�ű����� */,
//				"4A"/* �������� */, _getDate().toString(), voTempBill,
//				tbGeneralHVOss);

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

	/**
	 * ǩ��ȷ��
	 */

	@Override
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
		TbGeneralBVO[] tbGeneralBVOsss = null;
		if (getBillManageUI().isListPanelSelected()) {
			if (getBufferData().getVOBufferSize() != 0) {
				tbGeneralHVOss = (TbGeneralHVO) getBillManageUI()
						.getBillListWrapper().getVOFromUI().getParentVO();
				tbGeneralBVOsss = (TbGeneralBVO[]) getBillManageUI()
						.getBillListWrapper().getVOFromUI().getChildrenVO();

			}
		} else {
			if (getBufferData().getVOBufferSize() != 0) {
				tbGeneralHVOss = (TbGeneralHVO) getBillCardPanelWrapper()
						.getBillVOFromUI().getParentVO();
				tbGeneralBVOsss = (TbGeneralBVO[]) getBillCardPanelWrapper()
						.getBillVOFromUI().getChildrenVO();

			}
		}
		if (null == tbGeneralBVOsss) {
			getBillUI().showErrorMessage("����Ϊ�ղ���ǩ�֣�");
			return;
		} else {
			for (int i = 0; i < tbGeneralBVOsss.length; i++) {
				if (null != tbGeneralBVOsss[i].getGeb_isclose()) {
					if (!tbGeneralBVOsss[i].getGeb_isclose().booleanValue()) {
						getBillUI().showErrorMessage("����û��ȫ���رղ���ǩ�֣�");
						return;
					}
				}
			}
		}
		if (null == tbGeneralHVOss.getGeh_corp()
				|| "".equals(tbGeneralHVOss.getGeh_corp())) {
			getBillUI().showErrorMessage("��⹫˾Ϊ�ղ���ǩ�֣�");
			return;
		}
		if (null == tbGeneralHVOss.getGeh_calbody()
				|| "".equals(tbGeneralHVOss.getGeh_calbody())) {
			getBillUI().showErrorMessage("�������֯Ϊ�ղ���ǩ�֣�");
			return;
		}
		if (null == tbGeneralHVOss.getGeh_cdptid()
				|| "".equals(tbGeneralHVOss.getGeh_cdptid())) {
			getBillUI().showErrorMessage("����Ϊ�ղ���ǩ�֣�");
			return;
		}
		if (null == tbGeneralHVOss.getGeh_cwhsmanagerid()
				|| "".equals(tbGeneralHVOss.getGeh_cwhsmanagerid())) {
			getBillUI().showErrorMessage("������ԱΪ�ղ���ǩ�֣�");
			return;
		}
		if (null == tbGeneralHVOss.getGeh_cdispatcherid()
				|| "".equals(tbGeneralHVOss.getGeh_cdispatcherid())) {
			getBillUI().showErrorMessage("�շ�����Ϊ�ղ���ǩ�֣�");
			return;
		}

		if (null != tbGeneralHVOss.getGeh_cotherwhid()
				&& !"".equals(tbGeneralHVOss.getGeh_cotherwhid())) {
			if (null == tbGeneralHVOss.getGeh_cothercorpid()
					|| "".equals(tbGeneralHVOss.getGeh_cothercorpid())) {
				getBillUI().showErrorMessage("���⹫˾�ͳ���ֿ����ͬʱΪ�ջ�ǿգ�������ǩ�֣�");
				return;
			}
		} else {
			if (null != tbGeneralHVOss.getGeh_cothercorpid()
					&& !"".equals(tbGeneralHVOss.getGeh_cothercorpid())) {
				getBillUI().showErrorMessage("���⹫˾�ͳ���ֿ����ͬʱΪ�ջ�ǿգ�������ǩ�֣�");
				return;
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

//		Iw8004040210 iw = (Iw8004040210) NCLocator.getInstance().lookup(
//				Iw8004040210.class.getName());
//		iw.pushsavesign8004040210("PUSHSAVESIGN"/* ��д�ű����� */, "4A"/* �������� */,
//				_getDate().toString(), voTempBill, tbGeneralHVOss);
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
	
		// Ҫ��д�ĵ�����ⵥ��ͷVO
		GeneralBillHeaderVO gbillhVO = new GeneralBillHeaderVO();

		// ��浥�����ͱ���
		gbillhVO.setCbilltypecode("4A");
		
		// �շ�����ID
		if (null != tbGeneralHVOss.getGeh_cdispatcherid()) {
			gbillhVO.setCdispatcherid(tbGeneralHVOss.getGeh_cdispatcherid());
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
		// ����Ա
		gbillhVO.setAttributeValue("coperatoridnow", ClientEnvironment
				.getInstance().getUser().getPrimaryKey());
		
		// �Է���˾ID
		if (null != tbGeneralHVOss) {
			gbillhVO.setAttributeValue("cothercorpid", tbGeneralHVOss
					.getGeh_cothercorpid());
		}
		// �����ֿ�ID
		if (null != tbGeneralHVOss) {
			gbillhVO.setAttributeValue("cotherwhid", tbGeneralHVOss
					.getGeh_cotherwhid());
		}
		
		// �ֿ�ID
		if (null != tbGeneralHVOss.getGeh_cwarehouseid()) {
			gbillhVO.setCwarehouseid(tbGeneralHVOss.getGeh_cwarehouseid());
		}
		//
		// // �ⷿǩ������
		// // gbillhVO.setDaccountdate(new UFDate(new Date()));
		// ��������
		gbillhVO.setDbilldate(tbGeneralHVOss.getGeh_dbilldate());

		// ������Ա
		if (null != tbGeneralHVOss.getGeh_cwhsmanagerid()) {
			gbillhVO.setCwhsmanagerid(tbGeneralHVOss.getGeh_cwhsmanagerid());
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

		// ����޸�ʱ��
		gbillhVO.setAttributeValue("tlastmoditime", getBillUI()
				._getServerTime());
		// �Ƶ�ʱ��
		gbillhVO.setAttributeValue("tmaketime", new UFDateTime(tbGeneralHVOss
				.getTmaketime()));
		// ���ݺ�
		if (null != tbGeneralHVOss.getGeh_billcode()) {
			gbillhVO.setVbillcode(tbGeneralHVOss.getGeh_billcode());
		}

		// ���û�д��ͷVO

		voTempBill.setParentVO(gbillhVO);

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

			// ��������λID
			if (null != tbGeneralBVO.getBmeasdocname()) {
				gbillbVO.setCastunitid(tbGeneralBVO.getCastunitid());
			}

			// �������ID_
			if (null != tbGeneralBVO.getGeb_cinvbasid()) {
				gbillbVO.setCinvbasid(tbGeneralBVO.getGeb_cinvbasid());
			}
			// ͨ����˾�ʹ����������������ѯ�������������
			String pk_invmandoc = "select pk_invmandoc from bd_invmandoc ";
			if (null != tbGeneralBVO.getGeb_cinvbasid()
					&& null != tbGeneralHVOss.getGeh_corp()) {
				pk_invmandoc += " where pk_invbasdoc='"
						+ tbGeneralBVO.getGeb_cinvbasid().toString().trim()
						+ "' and pk_corp='"
						+ tbGeneralHVOss.getGeh_corp().toString().trim()
						+ "' and dr=0 ";
			} else {
				pk_invmandoc += " where 1=2 ";
			}
			ArrayList invmandocvos = (ArrayList) getQueryBO().executeQuery(
					pk_invmandoc.toString(), new ArrayListProcessor());
			Object[] invmandocvo = (Object[]) invmandocvos.get(0);

			// ���ID
			if (null != invmandocvo[0]) {
				gbillbVO.setCinventoryid(invmandocvo[0].toString().trim());
			}

			// �к�
			gbillbVO.setCrowno(i + 1 + "0");

			// ҵ������
			gbillbVO.setDbizdate(tbGeneralHVOss.getGeh_dbilldate());
			//
			// �Ƿ���Ʒ
			if (null != tbGeneralBVO.getGeb_flargess()) {
				gbillbVO.setFlargess(tbGeneralBVO.getGeb_flargess());
			}
			// ������
			if (null != tbGeneralBVO.getGeb_hsl()) {
				gbillbVO.setHsl(tbGeneralBVO.getGeb_hsl());
			}
			//
			// ��;������
			double geb_virtualbnum = 0;
			if (null != tbGeneralBVO.getGeb_virtualbnum()
					&& !"".equals(tbGeneralBVO.getGeb_virtualbnum())) {
				geb_virtualbnum = tbGeneralBVO.getGeb_virtualbnum()
						.doubleValue();
			}
			// ʵ�븨����
			if (null != tbGeneralBVO.getGeb_banum()) {
				gbillbVO.setNinassistnum(new UFDouble(tbGeneralBVO
						.getGeb_banum().doubleValue()
						+ geb_virtualbnum));

			}
			// ��;����
			double geb_virtualnum = 0;
			if (null != tbGeneralBVO.getGeb_virtualnum()
					&& !"".equals(tbGeneralBVO.getGeb_virtualnum())) {
				geb_virtualnum = tbGeneralBVO.getGeb_virtualnum().doubleValue();
			}
			// ʵ������
			if (null != tbGeneralBVO.getGeb_anum()) {
				gbillbVO.setNinnum(new UFDouble(tbGeneralBVO.getGeb_anum()
						.doubleValue()
						+ geb_virtualnum));
			}

			// ���
			if (null != tbGeneralBVO.getGeb_nmny()) {
				gbillbVO.setNmny(tbGeneralBVO.getGeb_nmny());
			}

			// Ӧ�븨����
			if (null != tbGeneralBVO.getGeb_bsnum()) {
				gbillbVO.setNneedinassistnum(new UFDouble(tbGeneralBVO
						.getGeb_bsnum().doubleValue()));
			}
			// ����
			if (null != tbGeneralBVO.getGeb_nprice()) {
				// gbillbVO.setNprice(new UFDouble(1));
				gbillbVO.setNprice(tbGeneralBVO.getGeb_nprice());
			}

			// Ӧ������
			if (null != tbGeneralBVO.getGeb_snum()) {
				gbillbVO.setNshouldinnum(new UFDouble(tbGeneralBVO
						.getGeb_snum().doubleValue()));
			}
			// ��˾
			if (null != tbGeneralHVOss.getGeh_corp()) {
				gbillbVO.setAttributeValue("PK_CORP", tbGeneralHVOss
						.getGeh_corp().toString());
			}
			// ���κ�
			if (null != tbGeneralBVO.getGeb_backvbatchcode()) {
				gbillbVO.setVbatchcode(tbGeneralBVO.getGeb_backvbatchcode());
			}
			// �������Σ���ѯ�������ڣ�ʧЧ����
			String vbcsql = "select dproducedate ,dvalidate  from scm_batchcode where pk_invbasdoc='"
					+ tbGeneralBVO.getGeb_cinvbasid()
					+ "' and vbatchcode='"
					+ tbGeneralBVO.getGeb_backvbatchcode() + "' and dr=0";
			ArrayList vbc = (ArrayList) getQueryBO().executeQuery(vbcsql,
					new ArrayListProcessor());

			if (null != vbc && vbc.size() > 0 && null != vbc.get(0)) {
				// ��������
				if (null != ((Object[]) vbc.get(0))[0]
						&& !"".equals(((Object[]) vbc.get(0))[0].toString())) {
					gbillbVO.setScrq(new UFDate(((Object[]) vbc.get(0))[0]
							.toString()));
				}
				// ʧЧ����
				if (null != ((Object[]) vbc.get(0))[1]
						&& !"".equals(((Object[]) vbc.get(0))[1].toString())) {
					gbillbVO.setDvalidate(new UFDate(((Object[]) vbc.get(0))[1]
							.toString()));
				}
			}
			// // ��������
			// if (null != tbGeneralBVO.getGeb_proddate()) {
			// gbillbVO.setScrq(new UFDate(tbGeneralBVO.getGeb_proddate()
			// .toString()));
			// }
			// // ʧЧ����
			// if (null != tbGeneralBVO.getGeb_dvalidate()) {
			// gbillbVO.setDvalidate(new UFDate(tbGeneralBVO
			// .getGeb_dvalidate().toString()));
			// }
			// ƾ֤����
			LocatorVO locatorvo = new LocatorVO();
			if (null != tbGeneralBVO.getGeb_space()) {
				locatorvo.setCspaceid(tbGeneralBVO.getGeb_space().toString());
			} else {
				// getBillUI().showErrorMessage("û������λ��������⣡");
				return null;
			}
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

		// }
		return voTempBill;
	}

	/**
	 * ���ȡ��ǩ�ֻ�д����VO�ķ���
	 * 
	 * @return
	 * @throws Exception
	 */
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
			gbillhVO.setCbilltypecode("4A");
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
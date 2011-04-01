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
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	private List myTempall;
	private MyClientUI myClientUI;
	private IUAPQueryBS iuap = null;
	// 判断用户身份
	// 判断是总仓还是分仓
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
	 * 自制单据
	 */
	public void onZzdj() throws Exception {
		if (getBillManageUI().isListPanelSelected())
			getBillManageUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		getBillUI().setBillOperate(IBillOperate.OP_ADD);
	}

	// 是否是自制
//	private boolean iszzdj = false;

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-9 8:37:34)
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
	 * 发运单据
	 */
	@Override
	protected void onFydj() throws Exception {
		//只有库管员可以操作该功能
		if(iUserType != 0){
			throw new BusinessException("你无操作权限");
		}

		// 6模版标识
		AggregatedValueObject[] vos = getProdWaybillDlg().getReturnVOs(
				myClientUI._getCorp().getPrimaryKey(), myClientUI._getOperator(), "0202",
				ConstVO.m_sBillDRSQ, "8004040214", "02140286", myClientUI);
		// 判断失总仓还是分仓
		if (null == vos || vos.length == 0) {

			return;
		}

		if (vos.length > 2) {
			getBillUI().showErrorMessage("一次只能选择一张运单！");
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
	 * 发运单据落单据
	 * 
	 * @param vos
	 * @return
	 */
	private MyBillVO changeTbPwbtoTbgen(AggregatedValueObject[] vos) {
		MyBillVO myBillVO = new MyBillVO();
		// 调拨入库单主表VO
		TbGeneralHVO tbGeneralHVO = new TbGeneralHVO();
		// 调拨出库单主表VO
		TbFydnewVO firstVO = (TbFydnewVO) vos[0].getParentVO();

		// 添加运单单据号

		// 添加发运单单据号
		tbGeneralHVO.setGeh_vbillcode(firstVO.getFyd_ddh());
		// 入库单类型
		tbGeneralHVO.setGeh_billtype(WdsWlPubConst.BILLTYPE_OTHER_IN);
		// 运单表头主键
		// tbGeneralHVO.setPwb_pk(tbProdwaybillVO.getPwb_pk());
		// 发运单表头主键
		tbGeneralHVO.setGeh_cgeneralhid(firstVO.getFyd_pk());
		// 其他单据单据号

		// 出库仓库主键
		if (null != firstVO.getSrl_pk()) {
			tbGeneralHVO.setGeh_cotherwhid(firstVO.getSrl_pk());
		}
		// 业务员主键
		if (null != firstVO.getPk_psndoc()) {
			tbGeneralHVO.setGeh_cbizid(firstVO.getPk_psndoc());
		}
		// 入库公司主键
//		if (null != ClientEnvironment.getInstance().getUser().getCorpId()) {
			tbGeneralHVO.setGeh_corp(ClientEnvironment.getInstance().getUser()
					.getCorpId().toString());
//		}
		// 入库库存组织主键
		// if (null != firstVO.getPk_calbodyr()) {
		// tbGeneralHVO.setGeh_calbody(firstVO.getPk_calbodyr().toString());
		// }
		// 入库仓库主键
		if (null != firstVO.getSrl_pkr()) {
			tbGeneralHVO.setGeh_cwarehouseid(firstVO.getSrl_pkr().toString());
		}

		// 入库管理员主键
		// if (null != firstVO.getCwhsmanagerid()) {
		// tbGeneralHVO.setGeh_cwhsmanagerid(firstVO.getCwhsmanagerid());
		// }

		// 调拨类型标志
		// tbGeneralHVO.setGeh_fallocflag(firstVO.getFallocflag());
		// 是否调拨退回
		// tbGeneralHVO.setGeh_freplenishflag(firstVO.getFreplenishflag());
		// 业务类型
		if (null != firstVO.getPk_busitype()) {
			tbGeneralHVO.setGeh_cbiztype(firstVO.getPk_busitype());
		}

		// 制单日期，单据日期

		tbGeneralHVO.setCopetadate(new UFDate(new Date()));
		tbGeneralHVO.setGeh_dbilldate(new UFDate(new Date()));
		// 制单人
		tbGeneralHVO.setCoperatorid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey());
		// 入库单类型
//		tbGeneralHVO.setGeh_billtype(2);
		// 添加主表VO
		myBillVO.setParentVO(tbGeneralHVO);

		// 获取子表vo
		String pk = firstVO.getFyd_pk().toString();
		String sql = " fyd_pk='" + pk + "' and dr=0 ";
//		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
//				IUAPQueryBS.class.getName());
		// 全部子表VO
		List myTemp = new ArrayList();
		// 前台显示的VO
		List myTempShow = new ArrayList();

		myTempall = new ArrayList();
		try {
			ArrayList ttcs = (ArrayList) getQueryBO().retrieveByClause(
					TbFydmxnewVO.class, sql.toString());
			// 当前登录的保管员能管理的货品
//			String[] InvLisk =LoginInforHelper.getInvBasDocIDsByUserID(ClientEnvironment.getInstance().getUser().getPrimaryKey());
			for (int i = 0; i < ttcs.size(); i++) {
				TbFydmxnewVO gbvo = (TbFydmxnewVO) ttcs.get(i);
				TbGeneralBVO tbGeneralBVO = new TbGeneralBVO();
				// 添加行号
				// if (null != gbvo.getCrowno()) {
				// tbGeneralBVO.setGeb_crowno(gbvo.getCrowno());
				// }
				tbGeneralBVO.setGeb_crowno(i + 1 + "0");
				// 存货基本档案主键
				if (null != gbvo.getPk_invbasdoc()) {
					tbGeneralBVO.setGeb_cinvbasid(gbvo.getPk_invbasdoc());
				}

				// 获得运单表头主键
				// tbGeneralBVO.setPwb_pk(tbProdwaybillVO.getPwb_pk());
				// 运单表体主键
				// tbGeneralBVO.setPwbb_pk(ttc.getPwbb_pk());
				// 发运单表头主键
				if (null != firstVO.getFyd_pk()) {
					tbGeneralBVO.setGeb_cgeneralhid(firstVO.getFyd_pk());
				}

				// 发运单表体主键
				if (null != gbvo.getCfd_pk()) {
					tbGeneralBVO.setGeb_cgeneralbid(gbvo.getCfd_pk());
				}
				// 批次号
				if (null != gbvo.getCfd_pc()) {
					tbGeneralBVO.setGeb_vbatchcode(gbvo.getCfd_pc());
				}

				// 货位
				if (null != nc.ui.wds.w8000.CommonUnit
						.getCargDocName(ClientEnvironment.getInstance()
								.getUser().getPrimaryKey())) {
					tbGeneralBVO.setGeb_space(nc.ui.wds.w8000.CommonUnit
							.getCargDocName(ClientEnvironment.getInstance()
									.getUser().getPrimaryKey()));
				}

				// 要回写的批次号
				if (null != gbvo.getCfd_lpc()) {
					tbGeneralBVO.setGeb_backvbatchcode(gbvo.getCfd_lpc());
				}
				// 根据批次，查询生产日期，失效日期
				String vbcsql = "select dproducedate ,dvalidate  from scm_batchcode where pk_invbasdoc='"
						+ tbGeneralBVO.getGeb_cinvbasid()
						+ "' and vbatchcode='"
						+ tbGeneralBVO.getGeb_backvbatchcode() + "' and dr=0";
				ArrayList vbc = (ArrayList) getQueryBO().executeQuery(vbcsql,
						new ArrayListProcessor());

				if (null != vbc && vbc.size() > 0 && null != vbc.get(0)) {
					// 生产日期
					if (null != ((Object[]) vbc.get(0))[0]
							&& !""
									.equals(((Object[]) vbc.get(0))[0]
											.toString())) {
						tbGeneralBVO.setGeb_proddate(new UFDate(((Object[]) vbc
								.get(0))[0].toString()));
					}
					// 失效日期
					if (null != ((Object[]) vbc.get(0))[1]
							&& !""
									.equals(((Object[]) vbc.get(0))[1]
											.toString())) {
						tbGeneralBVO.setGeb_dvalidate(new UFDate(
								((Object[]) vbc.get(0))[1].toString()));
					}
				}

				
				// 是否赠品
				if (null != gbvo.getBlargessflag()) {
					tbGeneralBVO.setGeb_flargess(gbvo.getBlargessflag());
				}
				// 应收辅数量
				if (null != gbvo.getCfd_sffsl()) {
					tbGeneralBVO.setGeb_bsnum(gbvo.getCfd_sffsl());
				}
				
				if (null != gbvo.getCfd_sfsl()) {
					tbGeneralBVO.setGeb_snum(gbvo.getCfd_sfsl());
				}
				

				// 货位
				String geb_space = nc.ui.wds.w8000.CommonUnit
						.getCargDocName(ClientEnvironment.getInstance()
								.getUser().getPrimaryKey());
				if (null != geb_space && !"".equals(geb_space)) {
					tbGeneralBVO.setGeb_space(geb_space);
				}

				// 改变状态
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
	 * 查询本地是否已经存在改数据，如果有返回改VO
	 * 
	 * @param vos
	 *            拉式生成中所选中的聚合VO
	 * @return
	 * @throws BusinessException
	 */
	private MyBillVO queryLocaDataPwb(AggregatedValueObject[] vos)
			throws BusinessException {
		// 调拨出库单主表VO
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
			// 要显示的子表VO
			List tmpList = new ArrayList();
			// 根据主键查询本地主表是否有数据
			ArrayList hList = (ArrayList) getQueryBO().retrieveByClause(
					TbGeneralHVO.class, strWhere);
			if (null != hList && hList.size() > 0) {
				tbGeneralHVO = (TbGeneralHVO) hList.get(0);
			}
			if (null != tbGeneralHVO) {
				// 根据本地主表主键查询子表数据
				strWhere = " dr = 0 and geh_pk = '" + tbGeneralHVO.getGeh_pk()
						+ "'";
				ArrayList list = (ArrayList) getQueryBO().retrieveByClause(
						TbGeneralBVO.class, strWhere);
				if (null != list && list.size() > 0) {
					// 当前登录的保管员能管理的货品
					List invLisk = nc.ui.wds.w8000.CommonUnit
							.getInvbasdoc_Pk(ClientEnvironment.getInstance()
									.getUser().getPrimaryKey());
					// 过滤子表数据
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
						// 这里把增加状态改回 修改
						addoredit = false;
						return mybillvo;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 查询本地是否已经存在改数据，如果有返回改VO(分仓)
	 * 
	 * @param vos
	 *            拉式生成中所选中的聚合VO
	 * @return
	 * @throws BusinessException
	 */
	private MyBillVO queryLocaDataPwbf(AggregatedValueObject[] vos)
			throws BusinessException {
		// 调拨出库单主表VO
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
			// 根据主键查询本地主表是否有数据
			ArrayList hList = (ArrayList) iuap.retrieveByClause(
					TbGeneralHVO.class, strWhere);
			if (null != hList && hList.size() > 0) {
				tbGeneralHVO = (TbGeneralHVO) hList.get(0);
			}
			if (null != tbGeneralHVO) {
				// 根据本地主表主键查询子表数据
				strWhere = " dr = 0 and geh_pk = '" + tbGeneralHVO.getGeh_pk()
						+ "'";
				ArrayList list = (ArrayList) iuap.retrieveByClause(
						TbGeneralBVO.class, strWhere);
				if (null != list && list.size() > 0) {
					// 改变子表的状态
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
						// 这里把增加状态改回 修改
						addoredit = false;
						return mybillvo;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 货位调整
	 */
	@Override
	protected void onHwtz() throws Exception {
		// TODO Auto-generated method stub
		try {
			// List invLisk = nc.ui.wds.w8000.CommonUnit
			// .getInvbasdoc_Pk(ClientEnvironment.getInstance().getUser()
			// .getPrimaryKey());
			// if (null == invLisk || invLisk.size() == 0) {
			// getBillUI().showErrorMessage("您无权操作！");
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
				getBillUI().showErrorMessage("您无权操作！");
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
				getBillUI().showErrorMessage("一次只能选择一张出库单！");
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
			// 在途数量编辑属性
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
	 * 指定托盘
	 */
	@Override
	protected void onZdtp() throws Exception {
		// TODO Auto-generated method stub
		//
		String[] cargdocs = LoginInforHelper.getSpaceByLogUser(myClientUI._getOperator());
		if(cargdocs== null||cargdocs.length==0||cargdocs.length>1)
			throw new BusinessException("获取货位信息异常");



		//验证  信息不能为空
		// 表头

		// 获得选中行号
		int[] b = getBillCardPanelWrapper().getBillCardPanel().getBillTable()
		.getSelectedRows();
		// 判断是否选中单行
		if (b.length > 1) {
			getBillUI().showErrorMessage("不能进行多行操作，请选择一行数据!");
			return;
		}
		if (null == b || b.length == 0) {
			getBillUI().showErrorMessage("您没有选择，请选择一行数据!");
			return;
		}
		// 获得选中的表体VO
		TbGeneralBVO tbGeneralBVO = (TbGeneralBVO) getBillCardPanelWrapper()
		.getBillCardPanel().getBillModel().getBodyValueRowVO(b[0],
				TbGeneralBVO.class.getName());

		if(tbGeneralBVO == null){
			throw new BusinessException("从界面获取数据异常");
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

		// 得到全部表体VO

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
		// 获取并判断表体是否有值，进行循环
		if (null != tbGeneralBVOs && tbGeneralBVOs.length > 0) {
			for (int i = 0; i < tbGeneralBVOs.length; i++) {
				// 货位
				String pk_cargdoc = LoginInforHelper.getSpaceByLogUser(myClientUI._getOperator())[0];

				//				// 获得表体

				String[] cdts = LoginInforHelper.getTrayInfor(pk_cargdoc, tbGeneralBVOs[i].getGeb_cinvbasid());

				// 托盘容积
				// 单品当前总容积
				int iVolumn = LoginInforHelper.getTrayVolumeByInvbasid(tbGeneralBVOs[i].getGeb_cinvbasid());
				double invVolume = cdts.length*iVolumn;

				// 获取当前表体行的应发辅数量和实发辅数量进行比较，根据比较结果进行颜色显示
				// 红色：没有实发数量；灰色：有实发数量但是数量不够；白色：实发数量与应发数量相等
				UFDouble num = PuPubVO.getUFDouble_NullAsZero(getBillCardPanelWrapper().getBillCardPanel()
						.getBodyValueAt(i, "geb_bsnum"));// 应收辅数量

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
	 * 查看明细
	 */
	@Override
	protected void onCkmx() throws Exception {
		// TODO Auto-generated method stub
		if (getBufferData().getVOBufferSize() <= 0) {
			getBillUI().showErrorMessage("表体中没有数据或您没有选择表体单据，不能进行操作!");
			return;
		}
		// 得到全部表体VO
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
		// 判断表体是否有数据
		if (null == tbGeneralBVOs || tbGeneralBVOs.length == 0) {
			getBillUI().showErrorMessage("表体中没有数据，不能进行操作！");
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
	 * 自动入库
	 */
	@Override
	protected void onZdrk() throws Exception {
		// 货位
		String[] cargdocs = LoginInforHelper.getSpaceByLogUser(myClientUI._getOperator());
		if(cargdocs== null||cargdocs.length==0||cargdocs.length>1)
			throw new BusinessException("获取货位信息异常");

		
		
	//验证  信息不能为空
		// 表头
		TbGeneralBVO[] tbGeneralBVOs = (TbGeneralBVO[])getBillCardPanelWrapper().getBillCardPanel()
		.getBillModel().getBodyValueVOs(TbGeneralBVO.class.getName());

		if(tbGeneralBVOs == null||tbGeneralBVOs.length ==0){
			throw new BusinessException("表体数据为空");
		}
		// 验证货品主键
		for (TbGeneralBVO body:tbGeneralBVOs) {
			body.validateOnZdrk();
		}
		
		Object o = getDelegator().autoInStore(tbGeneralBVOs, 
				LoginInforHelper.getCwhid(myClientUI._getOperator()), cargdocs[0]);
		if(o == null){
			myClientUI.showHintMessage("处理完成");
			return;
		}
			
		
		// 获取当前表体行的应发辅数量和实发辅数量进行比较，根据比较结果进行颜色显示
		// 红色：没有实发数量-1；灰色：有实发数量但是数量不够1；白色：实发数量与应发数量相等0
		
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
//		// ------------------------------------------判断托盘容积是否能存放货物
//		if (sotckIsTotal) {
//			// 获取并判断表体是否有值，进行循环
//			if (null != tbGeneralBVOs && tbGeneralBVOs.length > 0) {
//				for (int i = 0; i < tbGeneralBVOs.length; i++) {
//
//					// 获得表体
//					StringBuffer sql = new StringBuffer(
//							"select cdt_pk from bd_cargdoc_tray where cdt_invbasdoc='"
//									+ tbGeneralBVOs[i].getGeb_cinvbasid()
//									+ "' and cdt_traystatus=0 and dr=0 and  pk_cargdoc='"
//									+ cargdocs[0] + "' ");
//
//					// 符合条件的全部托盘
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
//					// 托盘容积
//					StringBuffer sqlVolume = new StringBuffer(
//							"select def20 from bd_invbasdoc where pk_invbasdoc='");
//					sqlVolume.append(tbGeneralBVOs[i].getGeb_cinvbasid());
//					sqlVolume.append("'");
//					ArrayList cdtsVolume = new ArrayList();
//					cdtsVolume = (ArrayList) getQueryBO().executeQuery(sqlVolume
//							.toString(), new ArrayListProcessor());
//					// 单品当前总容积
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
//						getBillUI().showErrorMessage("请填写托盘容积！");
//						return;
//					}
//
//					// 获取当前表体行的应发辅数量和实发辅数量进行比较，根据比较结果进行颜色显示
//					// 红色：没有实发数量；灰色：有实发数量但是数量不够；白色：实发数量与应发数量相等
//					Object num = getBillCardPanelWrapper().getBillCardPanel()
//							.getBodyValueAt(i, "geb_bsnum");// 应收辅数量
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
//				// 获得表体
//				StringBuffer sql = new StringBuffer(
//						" select cdt_pk from bd_cargdoc_tray where cdt_invbasdoc='"
//								+ tbGeneralBVOs[i].getGeb_cinvbasid()
//								+ "' and cdt_traystatus=0 and dr=0 and  pk_cargdoc='"
//								+ cargdocs[0] + "'  ");
//				// IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance()
//				// .lookup(IUAPQueryBS.class.getName());
//				// 符合条件的全部托盘
//				ArrayList cdts = new ArrayList();
//				try {
//					cdts = (ArrayList) getQueryBO().executeQuery(sql.toString(),
//							new ArrayListProcessor());
//
//				} catch (BusinessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				// 托盘容积
//				StringBuffer sqlVolume = new StringBuffer(
//						"select def20 from bd_invbasdoc where pk_invbasdoc='");
//				sqlVolume.append(tbGeneralBVOs[i].getGeb_cinvbasid());
//				sqlVolume.append("'");
//				ArrayList cdtsVolume = new ArrayList();
//				cdtsVolume = (ArrayList) getQueryBO().executeQuery(sqlVolume
//						.toString(), new ArrayListProcessor());
//				// 单品当前总容积
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
//					getBillUI().showErrorMessage("请填写托盘或托盘容积！");
//					return;
//				}
//				// 入库辅数量
//				String geb_bsnum = "";
//				if (null != tbGeneralBVOs[i].getGeb_bsnum()) {
//					geb_bsnum = tbGeneralBVOs[i].getGeb_bsnum().toString();
//				}
//				// 入库辅数量
//				double geb_bsnumd = 0;
//				if (null != geb_bsnum && !"".equals(geb_bsnum)) {
//					geb_bsnumd = Double.parseDouble(geb_bsnum);
//				}
//
//				if (geb_bsnumd > invVolume) {
//					getBillUI()
//							.showErrorMessage("入库数量大于当前库存容积！(存货编码为红色的托盘不足) ");
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
//				getBillUI().showErrorMessage("分仓仓库没有虚拟托盘！");
//				return;
//			}
//
//			
//		}
		
		// ------------------------------------------判断托盘容积是否能存放货物
		
		
		
		
		
//		
//		
//
//		// 添加托盘信息
//		for (int i = 0; i < tbGeneralBVOs.length; i++) {
//			// 获得表体
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
//			// 符合条件的全部托盘
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
//			// 托盘容积
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
//			// 入库辅数量
//			String geb_bsnum = "";
//			if (null != tbGeneralBVOs[i].getGeb_bsnum()) {
//				geb_bsnum = tbGeneralBVOs[i].getGeb_bsnum().toString();
//			}
//			// 当前入库辅数量
//			double geb_bsnumd = 0;
//			if (null != geb_bsnum && !"".equals(geb_bsnum)) {
//				geb_bsnumd = Double.parseDouble(geb_bsnum);
//			}
//			// 存货托盘个数
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
//			// 获得要删除的VO
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
//					// 批次
//					tbgbbvo.setGebb_vbatchcode(tbGeneralBVOs[i]
//							.getGeb_vbatchcode());
//					// 回写批次
//					tbgbbvo.setGebb_lvbatchcode(tbGeneralBVOs[i]
//							.getGeb_backvbatchcode());
//					// 行号
//					tbgbbvo.setGebb_rowno(k + 1 + "0");
//					// 出库单表体主键
//					tbgbbvo.setPwb_pk(tbGeneralBVOs[i].getGeb_cgeneralbid());
//					// 换算率
//					tbgbbvo.setGebb_hsl(tbGeneralBVOs[i].getGeb_hsl());
//					// 运货档案主键
//					tbgbbvo
//							.setPk_invbasdoc(tbGeneralBVOs[i]
//									.getGeb_cinvbasid());
//					// 入库单子表主键
//					tbgbbvo.setGeb_pk(tbGeneralBVOs[i].getGeb_pk());
//					// 单价
//					tbgbbvo.setGebb_nprice(tbGeneralBVOs[i].getGeb_nprice());
//					// 金额
//					tbgbbvo.setGebb_nmny(tbGeneralBVOs[i].getGeb_nmny());
//					// 托盘实际存放数量
//					tbgbbvo.setGebb_num(new UFDouble(invTrayVolume));
//					geb_bsnumd = geb_bsnumd - invTrayVolume;
//					// 托盘主键
//					if (null != ((Object[]) cdts.get(k))[0]) {
//						tbgbbvo.setCdt_pk(((Object[]) cdts.get(k))[0]
//								.toString());
//					}
//					// DR
//					tbgbbvo.setDr(0);
//					tbgbbvo.setGeb_pk(tbGeneralBVOs[i].getGeb_pk());
//					tbGeneralBBVOs.add(tbgbbvo);
//					// // 托盘状态
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
//			// 批次
//			tbgbbvo1.setGebb_vbatchcode(tbGeneralBVOs[i].getGeb_vbatchcode());
//			// 回写批次
//			tbgbbvo1.setGebb_lvbatchcode(tbGeneralBVOs[i]
//					.getGeb_backvbatchcode());
//			// 行号
//			tbgbbvo1.setGebb_rowno(k + 1 + "0");
//			// 出库单表体主键
//			tbgbbvo1.setPwb_pk(tbGeneralBVOs[i].getGeb_cgeneralbid());
//			// 换算率
//			tbgbbvo1.setGebb_hsl(tbGeneralBVOs[i].getGeb_hsl());
//			// 运货档案主键
//			tbgbbvo1.setPk_invbasdoc(tbGeneralBVOs[i].getGeb_cinvbasid());
//			// 入库单子表主键
//			tbgbbvo1.setGeb_pk(tbGeneralBVOs[i].getGeb_pk());
//			// 单价
//			tbgbbvo1.setGebb_nprice(tbGeneralBVOs[i].getGeb_nprice());
//			// 金额
//			tbgbbvo1.setGebb_nmny(tbGeneralBVOs[i].getGeb_nmny());
//			// 托盘实际存放数量
//			tbgbbvo1.setGebb_num(new UFDouble(geb_bsnumd));
//			// 托盘主键
//			if (null != ((Object[]) cdts.get(k))[0]) {
//				tbgbbvo1.setCdt_pk(((Object[]) cdts.get(k))[0].toString());
//			}
//			// DR
//			tbgbbvo1.setDr(0);
//			tbGeneralBBVOs.add(tbgbbvo1);
//			// // 托盘状态
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
//			// 获得自定义接口
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

	// 行号
	private int allRowNO = -1;

	@Override
	protected void onBoLineAdd() throws Exception {
		// TODO Auto-generated method stub
		super.onBoLineAdd();
		int selectRow = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getSelectedRow();

		// 行号

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

		// 货位
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

		// 行号

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

	// 判断是添加还是修改
	private boolean addoredit = true;
	// 是否是修改
	private boolean isedit = true;

	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		// 当前登录的保管员能管理的货品
		List InvLisk = nc.ui.wds.w8000.CommonUnit
				.getInvbasdoc_Pk(ClientEnvironment.getInstance().getUser()
						.getPrimaryKey());
		boolean isControl = false;
//		if ("1".equals(st_type)) {
//			isControl = true;
//		}
		// 先需要切换列表模式下一次，方便过滤数据
		super.onBoReturn();
//		if (!isControl) {
//			// 判断是否为空，如果为空 说明当前登录者不是仓库保管员
//			if (sotckIsTotal) {
//				if (null != InvLisk && InvLisk.size() > 0) {
//					List list = new ArrayList();
//					TbGeneralBVO[] generalbvo = null;
//					// 获取当前选中行
//					AggregatedValueObject billvo = getBufferData()
//							.getCurrentVO();
//					for (int i = 0; i < InvLisk.size(); i++) {
//						// 判断选中行是否有数据
//						if (null != billvo && null != billvo.getParentVO()
//								&& null != billvo.getChildrenVO()
//								&& billvo.getChildrenVO().length > 0) {
//							// 取出子表数据
//							generalbvo = (TbGeneralBVO[]) billvo
//									.getChildrenVO();
//							// 进行判断是否为空然后进行循环
//							if (null != generalbvo && generalbvo.length > 0) {
//								for (int j = 0; j < generalbvo.length; j++) {
//									// 如果当前登录者仓库中的单品主键和单据中的单品主键相等放到一个集合里面 去
//									// 然后填充页面
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
//						// 进行数组转换
//						generalbvo = new TbGeneralBVO[list.size()];
//						generalbvo = (TbGeneralBVO[]) list.toArray(generalbvo);
//						billvo.setChildrenVO(generalbvo);
//						// 在更新到当前选中行中
//						getBufferData().setCurrentVO(billvo);
//						// 托盘指定按钮和自动取货按钮可用
//						// changeButton(true);
//					} else {
//						// 如果集合里面没有数据 当前登录者是其他仓库的保管员
//						// 把辅助功能按钮置为不可用
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
//			// 设置收发类别和备注可编辑
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
//			// 设置收发类别和备注可编辑
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
	 * 货位调整落单据
	 * 
	 * @param vos
	 * @return
	 */
	private MyBillVO changeTbOuttoTbgen(AggregatedValueObject[] vos) {
		MyBillVO myBillVO = new MyBillVO();
		// 调拨入库单主表VO
		TbGeneralHVO tbGeneralHVO = new TbGeneralHVO();
		// 调拨出库单主表VO
		TbOutgeneralHVO firstVO = (TbOutgeneralHVO) vos[0].getParentVO();

		// 添加运单单据号

		// 添加调拨出库单单据号
		tbGeneralHVO.setGeh_vbillcode(firstVO.getVbillcode());
		// 入库单类型
		tbGeneralHVO.setGeh_billtype(WdsWlPubConst.BILLTYPE_OTHER_IN);
		// 运单表头主键
		// tbGeneralHVO.setPwb_pk(tbProdwaybillVO.getPwb_pk());
		// 发运出库单表头主键
		tbGeneralHVO.setGeh_cgeneralhid(firstVO.getGeneral_pk());
		// 其他单据单据号
		// try {
		// billCode = getBillCode("0214", ClientEnvironment.getInstance()
		// .getUser().getCorpId(), "", "");
		// tbGeneralHVO.setGeh_billcode(billCode);
		// } catch (BusinessException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// 出库公司主键
		if (null != firstVO.getPk_fcorp()) {
			tbGeneralHVO.setGeh_cothercorpid(firstVO.getPk_fcorp());
		}
		// 出库库存组织主键
		if (null != firstVO.getPk_calbody()) {
			tbGeneralHVO.setGeh_cothercalbodyid(firstVO.getPk_calbody());
		}
		// 出库仓库主键
		if (null != firstVO.getSrl_pkr()) {
			tbGeneralHVO.setGeh_cotherwhid(firstVO.getSrl_pkr());
		}
		// 业务员主键
		if (null != firstVO.getCbizid()) {
			tbGeneralHVO.setGeh_cbizid(firstVO.getCbizid());
		}

		// 收发类型ID
		if (null != firstVO.getCdispatcherid()) {
			tbGeneralHVO.setGeh_cdispatcherid(firstVO.getCdispatcherid());
		}

		// 入库公司主键
		if (null != ClientEnvironment.getInstance().getUser().getCorpId()) {
			tbGeneralHVO.setGeh_corp(ClientEnvironment.getInstance().getUser()
					.getCorpId().toString());
		}
		// 入库库存组织主键
		if (null != firstVO.getPk_calbodyr()) {
			tbGeneralHVO.setGeh_calbody(firstVO.getPk_calbodyr().toString());
		}
		// 入库仓库主键
		if (null != firstVO.getSrl_pk()) {
			tbGeneralHVO.setGeh_cwarehouseid(firstVO.getSrl_pk().toString());
		}

		// 入库管理员主键
		if (null != firstVO.getCwhsmanagerid()) {
			tbGeneralHVO.setGeh_cwhsmanagerid(firstVO.getCwhsmanagerid());
		}

		// 调拨类型标志
		// tbGeneralHVO.setGeh_fallocflag(firstVO.getFallocflag());
		// 是否调拨退回
		// tbGeneralHVO.setGeh_freplenishflag(firstVO.getFreplenishflag());
		// 业务类型
		if (null != firstVO.getCbiztype()) {
			tbGeneralHVO.setGeh_cbiztype(firstVO.getCbiztype());
		}

		// 制单日期，单据日期

		tbGeneralHVO.setCopetadate(new UFDate(new Date()));
		tbGeneralHVO.setGeh_dbilldate(new UFDate(new Date()));
		// 制单人
		tbGeneralHVO.setCoperatorid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey());
		// 入库单类型
//		tbGeneralHVO.setGeh_billtype(3);
		// 添加主表VO
		myBillVO.setParentVO(tbGeneralHVO);

		// 获取子表vo
		String pk = firstVO.getGeneral_pk().toString();
		String sql = " general_pk='" + pk + "' and dr=0 ";
//		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
//				IUAPQueryBS.class.getName());
		// 全部子表VO
		List myTemp = new ArrayList();
		// 前台显示的VO
		List myTempShow = new ArrayList();

		myTempall = new ArrayList();
		try {
			ArrayList ttcs = (ArrayList) getQueryBO().retrieveByClause(
					TbOutgeneralBVO.class, sql.toString());
			// 当前登录的保管员能管理的货品
			List InvLisk = nc.ui.wds.w8000.CommonUnit
					.getInvbasdoc_Pk(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
			for (int i = 0; i < ttcs.size(); i++) {
				TbOutgeneralBVO gbvo = (TbOutgeneralBVO) ttcs.get(i);
				TbGeneralBVO tbGeneralBVO = new TbGeneralBVO();
				// 添加行号
				// if (null != gbvo.getCrowno()) {
				// tbGeneralBVO.setGeb_crowno(gbvo.getCrowno());
				// }
				tbGeneralBVO.setGeb_crowno(i + 1 + "0");
				// 存货基本档案主键
				if (null != gbvo.getCinventoryid()) {
					tbGeneralBVO.setGeb_cinvbasid(gbvo.getCinventoryid());
				}

				// 获得运单表头主键
				// tbGeneralBVO.setPwb_pk(tbProdwaybillVO.getPwb_pk());
				// 运单表体主键
				// tbGeneralBVO.setPwbb_pk(ttc.getPwbb_pk());
				// 发运出库单表头主键
				if (null != firstVO.getGeneral_pk()) {
					tbGeneralBVO.setGeb_cgeneralhid(firstVO.getGeneral_pk());
				}

				// 发运出库单表体主键
				if (null != gbvo.getGeneral_b_pk()) {
					tbGeneralBVO.setGeb_cgeneralbid(gbvo.getGeneral_b_pk());
				}
				// 换算率
				if (null != gbvo.getHsl()) {
					tbGeneralBVO.setGeb_hsl(new UFDouble(gbvo.getHsl()
							.toString()));
				}
				// 单位
				// if (null != ttc.getPk_measdoc()) {
				// tbGeneralBVO.setPk_measdoc(ttc.getPk_measdoc());
				// }
				// 批次号
				if (null != gbvo.getVbatchcode()) {
					// 使用的批次号
					tbGeneralBVO.setGeb_vbatchcode(gbvo.getVbatchcode());

				}
				// 要回写的批次号
				if (null != gbvo.getLvbatchcode()) {
					// 要回写的批次号
					tbGeneralBVO.setGeb_backvbatchcode(gbvo.getLvbatchcode());
				}
				// 根据批次，查询生产日期，失效日期
				String vbcsql = "select dproducedate ,dvalidate  from scm_batchcode where pk_invbasdoc='"
						+ tbGeneralBVO.getGeb_cinvbasid()
						+ "' and vbatchcode='"
						+ tbGeneralBVO.getGeb_vbatchcode() + "' and dr=0";
				ArrayList vbc = (ArrayList) getQueryBO().executeQuery(vbcsql,
						new ArrayListProcessor());

				if (null != vbc && vbc.size() > 0 && null != vbc.get(0)) {
					// 生产日期
					if (null != ((Object[]) vbc.get(0))[0]
							&& !""
									.equals(((Object[]) vbc.get(0))[0]
											.toString())) {
						tbGeneralBVO.setGeb_proddate(new UFDate(((Object[]) vbc
								.get(0))[0].toString()));
					}
					// 失效日期
					if (null != ((Object[]) vbc.get(0))[1]
							&& !""
									.equals(((Object[]) vbc.get(0))[1]
											.toString())) {
						tbGeneralBVO.setGeb_dvalidate(new UFDate(
								((Object[]) vbc.get(0))[1].toString()));
					}
				}

				// 获得应收辅数量，实收辅数量
				// double noutassistnum = 0.00;
				// double ntranoutastnum = 0.00;
				// if (null != gbvo[10]) {
				// noutassistnum = Double.parseDouble(gbvo[10].toString());
				// }
				// if (null != gbvo[11]) {
				// ntranoutastnum = Double.parseDouble(gbvo[11].toString());
				// }
				// 应收辅数量
				if (null != gbvo.getNoutassistnum()) {
					tbGeneralBVO.setGeb_bsnum(gbvo.getNoutassistnum());
				}
				// 实收辅数量
				// tbGeneralBVO.setGeb_banum(tbGeneralBVO.getGeb_bsnum());
				// 获得实收数量，应收数量
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
				// 单价
				if (null != gbvo.getNprice()
						&& !"".equals(gbvo.getNprice().toString())) {
					tbGeneralBVO.setGeb_nprice(new UFDouble(gbvo.getNprice()
							.toString()));
				}
				// 金额
				if (null != gbvo.getNmny()
						&& !"".equals(gbvo.getNmny().toString())) {
					tbGeneralBVO.setGeb_nmny(gbvo.getNmny());
				}

				// 货位
				String geb_space = nc.ui.wds.w8000.CommonUnit
						.getCargDocName(ClientEnvironment.getInstance()
								.getUser().getPrimaryKey());
				if (null != geb_space && !"".equals(geb_space)) {
					tbGeneralBVO.setGeb_space(geb_space);
				}

				// 改变状态
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
	 * 查询本地是否已经存在改数据，如果有返回改VO
	 * 
	 * @param vos
	 *            拉式生成中所选中的聚合VO
	 * @return
	 * @throws BusinessException
	 */
	private MyBillVO queryLocaData(AggregatedValueObject[] vos)
			throws BusinessException {
		// 调拨出库单主表VO
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
			// 根据主键查询本地主表是否有数据
			ArrayList hList = (ArrayList) iuap.retrieveByClause(
					TbGeneralHVO.class, strWhere);
			if (null != hList && hList.size() > 0) {
				tbGeneralHVO = (TbGeneralHVO) hList.get(0);
			}
			if (null != tbGeneralHVO) {
				// 根据本地主表主键查询子表数据
				strWhere = " dr = 0 and geh_pk = '" + tbGeneralHVO.getGeh_pk()
						+ "'";
				ArrayList list = (ArrayList) iuap.retrieveByClause(
						TbGeneralBVO.class, strWhere);
				if (null != list && list.size() > 0) {
					// 当前登录的保管员能管理的货品
					List invLisk = nc.ui.wds.w8000.CommonUnit
							.getInvbasdoc_Pk(ClientEnvironment.getInstance()
									.getUser().getPrimaryKey());
					// 过滤子表数据
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
						// 这里把增加状态改回 修改
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
			throw new BusinessException("从界面获取数据为空");
		}
//		校验数据
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
//		// 页面标签
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
//			// 签字后
//			if (null != generalhvo.getPwb_fbillflag()
//					&& generalhvo.getPwb_fbillflag() == 3) {
//				getButtonManager().getButton(
//						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
//						.setEnabled(false);
//				getButtonManager().getButton(
//						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
//						.setEnabled(true);
//				getBillUI().updateButtonUI();
//			} else { // 签字前
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
//			// 签字后
//			if (null != generalhvo.getPwb_fbillflag()
//					&& generalhvo.getPwb_fbillflag() == 3) {
//				getButtonManager().getButton(
//						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
//						.setEnabled(false);
//				getButtonManager().getButton(
//						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
//						.setEnabled(true);
//				getBillUI().updateButtonUI();
//			} else { // 签字前
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
	 * 当前登录的保管员只能查询自己货位的入库单
	 */
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();
		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		strWhere.append(" and geh_billtype ='"+WdsWlPubConst.BILLTYPE_OTHER_IN+"' ");
		String pk_stordoc = LoginInforHelper.getCwhid(_getOperator());
		strWhere.append(" and geh_cwarehouseid='" + pk_stordoc + "' ");
		String cargdocid = LoginInforHelper.getSpaceByLogUserForStore(myClientUI._getOperator());
		if(cargdocid != null){//不是保管员登录 可以查看所有入库单
			strWhere.append(" and pk_cargdoc = '"+cargdocid+"'");
		}
		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());
		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);
		updateBuffer();
	}

	/**
	 * 弹出查询对话框向用户询问查询条件。 如果用户在对话框点击了“确定”，那么返回true,否则返回false
	 * 查询条件通过传入的StringBuffer返回给调用者
	 * 
	 * @param sqlWhereBuf
	 *            保存查询条件的StringBuffer
	 * @return 用户选确定返回true否则返回false
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
				// 业务类型编码
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_BusiCode() + "='"
						+ getBillUI().getBusicode() + "'";

			else
				// 业务类型
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_Busitype() + "='"
						+ getBillUI().getBusinessType() + "'";

		}

		strWhere = "(" + strWhere + ") and (isnull(dr,0)=0)";

		// if (getHeadCondition() != null)
		// strWhere = strWhere + " and " + getHeadCondition();
		// 现在我先直接把这个拼好的串放到StringBuffer中而不去优化拼串的过程
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
	 * 取消签字
	 */

	@Override
	protected void onQxqz() throws Exception {
		// TODO Auto-generated method stub
		// 要回写的聚合VO
		GeneralBillVO voTempBill = getVoTempBillCancel();

		if (null == voTempBill) {
			getBillUI().showErrorMessage("没有单据或单据没有制定货位，不能入库！");
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
		// 库房签字人
		tbGeneralHVOss.setGeh_storname(null);
		// 签字时间
		tbGeneralHVOss.setTaccounttime(null);

		// 最后修改人
		tbGeneralHVOss.setClastmodiid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey());
		// 最后修改人日期
		tbGeneralHVOss.setClastmodedate(new UFDate(new Date()));
		// 最后修改时间
		tbGeneralHVOss.setClastmodetime(myClientUI._getServerTime().toString());
		// 单据状态
		tbGeneralHVOss.setPwb_fbillflag(2);
		// 要回写的方法
		// Object o = nc.ui.pub.pf.PfUtilClient.processAction(
		// "CANCELSIGN"/* 回写脚本名称 */, "4E"/* 单据类型 */, _getDate()
		// .toString(), voTempBill);
//		Iw8004040210 iw = (Iw8004040210) NCLocator.getInstance().lookup(
//				Iw8004040210.class.getName());
//		iw.canceldelete8004040210("CANCELSIGN", "DELETE"/* 回写脚本名称 */,
//				"4A"/* 单据类型 */, _getDate().toString(), voTempBill,
//				tbGeneralHVOss);

		// Object o = nc.ui.pub.pf.PfUtilClient.processAction(
		// "DELETE"/* 回写脚本名称 */, "4E"/* 单据类型 */, _getDate().toString(),
		// voTempBill);
		getBillUI().showHintMessage("单据取消签字成功");
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
	 * 签字确认
	 */

	@Override
	protected void onQzqr() throws Exception {
		// TODO Auto-generated method stub
		// 要回写的聚合VO
		GeneralBillVO voTempBill = getVoTempBill();
		// LocatorVO locatorvo = new LocatorVO();

		if (null == voTempBill) {
			getBillUI().showErrorMessage("没有单据或单据没有制定货位，不能入库！");
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
			getBillUI().showErrorMessage("表体为空不能签字！");
			return;
		} else {
			for (int i = 0; i < tbGeneralBVOsss.length; i++) {
				if (null != tbGeneralBVOsss[i].getGeb_isclose()) {
					if (!tbGeneralBVOsss[i].getGeb_isclose().booleanValue()) {
						getBillUI().showErrorMessage("表体没有全部关闭不能签字！");
						return;
					}
				}
			}
		}
		if (null == tbGeneralHVOss.getGeh_corp()
				|| "".equals(tbGeneralHVOss.getGeh_corp())) {
			getBillUI().showErrorMessage("入库公司为空不能签字！");
			return;
		}
		if (null == tbGeneralHVOss.getGeh_calbody()
				|| "".equals(tbGeneralHVOss.getGeh_calbody())) {
			getBillUI().showErrorMessage("入库库存组织为空不能签字！");
			return;
		}
		if (null == tbGeneralHVOss.getGeh_cdptid()
				|| "".equals(tbGeneralHVOss.getGeh_cdptid())) {
			getBillUI().showErrorMessage("部门为空不能签字！");
			return;
		}
		if (null == tbGeneralHVOss.getGeh_cwhsmanagerid()
				|| "".equals(tbGeneralHVOss.getGeh_cwhsmanagerid())) {
			getBillUI().showErrorMessage("入库管理员为空不能签字！");
			return;
		}
		if (null == tbGeneralHVOss.getGeh_cdispatcherid()
				|| "".equals(tbGeneralHVOss.getGeh_cdispatcherid())) {
			getBillUI().showErrorMessage("收发类型为空不能签字！");
			return;
		}

		if (null != tbGeneralHVOss.getGeh_cotherwhid()
				&& !"".equals(tbGeneralHVOss.getGeh_cotherwhid())) {
			if (null == tbGeneralHVOss.getGeh_cothercorpid()
					|| "".equals(tbGeneralHVOss.getGeh_cothercorpid())) {
				getBillUI().showErrorMessage("出库公司和出库仓库必须同时为空或非空，否则不能签字！");
				return;
			}
		} else {
			if (null != tbGeneralHVOss.getGeh_cothercorpid()
					&& !"".equals(tbGeneralHVOss.getGeh_cothercorpid())) {
				getBillUI().showErrorMessage("出库公司和出库仓库必须同时为空或非空，否则不能签字！");
				return;
			}
		}

		// 库房签字人
		tbGeneralHVOss.setGeh_storname(ClientEnvironment.getInstance()
				.getUser().getPrimaryKey());
		// 签字时间
		tbGeneralHVOss.setTaccounttime(myClientUI._getServerTime().toString());

		// 最后修改人
		tbGeneralHVOss.setClastmodiid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey());
		// 最后修改人日期
		tbGeneralHVOss.setClastmodedate(new UFDate(new Date()));
		// 最后修改时间
		tbGeneralHVOss.setClastmodetime(myClientUI._getServerTime().toString());
		// 单据状态
		tbGeneralHVOss.setPwb_fbillflag(3);
		// 要回写的方法

//		Iw8004040210 iw = (Iw8004040210) NCLocator.getInstance().lookup(
//				Iw8004040210.class.getName());
//		iw.pushsavesign8004040210("PUSHSAVESIGN"/* 回写脚本名称 */, "4A"/* 单据类型 */,
//				_getDate().toString(), voTempBill, tbGeneralHVOss);
		// Object o = nc.ui.pub.pf.PfUtilClient.processAction(
		// "PUSHSAVESIGN"/* 回写脚本名称 */, "4E"/* 单据类型 */, _getDate()
		// .toString(), voTempBill);

		getBillUI().showHintMessage("签字成功");
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
	 * 获得回写集合VO的方法
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
	
		// 要回写的调拨入库单表头VO
		GeneralBillHeaderVO gbillhVO = new GeneralBillHeaderVO();

		// 库存单据类型编码
		gbillhVO.setCbilltypecode("4A");
		
		// 收发类型ID
		if (null != tbGeneralHVOss.getGeh_cdispatcherid()) {
			gbillhVO.setCdispatcherid(tbGeneralHVOss.getGeh_cdispatcherid());
		}
		// 部门ID
		if (null != tbGeneralHVOss.getGeh_cdptid()) {
			gbillhVO.setCdptid(tbGeneralHVOss.getGeh_cdptid());
		}
		// 最后修改人
		gbillhVO.setAttributeValue("clastmodiid", ClientEnvironment
				.getInstance().getUser().getPrimaryKey());
		// 制单人
		gbillhVO.setCoperatorid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey());
		// 操作员
		gbillhVO.setAttributeValue("coperatoridnow", ClientEnvironment
				.getInstance().getUser().getPrimaryKey());
		
		// 对方公司ID
		if (null != tbGeneralHVOss) {
			gbillhVO.setAttributeValue("cothercorpid", tbGeneralHVOss
					.getGeh_cothercorpid());
		}
		// 其它仓库ID
		if (null != tbGeneralHVOss) {
			gbillhVO.setAttributeValue("cotherwhid", tbGeneralHVOss
					.getGeh_cotherwhid());
		}
		
		// 仓库ID
		if (null != tbGeneralHVOss.getGeh_cwarehouseid()) {
			gbillhVO.setCwarehouseid(tbGeneralHVOss.getGeh_cwarehouseid());
		}
		//
		// // 库房签字日期
		// // gbillhVO.setDaccountdate(new UFDate(new Date()));
		// 单据日期
		gbillhVO.setDbilldate(tbGeneralHVOss.getGeh_dbilldate());

		// 入库管理员
		if (null != tbGeneralHVOss.getGeh_cwhsmanagerid()) {
			gbillhVO.setCwhsmanagerid(tbGeneralHVOss.getGeh_cwhsmanagerid());
		}
		// 单据状态
		gbillhVO.setFbillflag(2);
		// 退货标志
		gbillhVO.setFreplenishflag(new UFBoolean(false));
		// 库存组织PK
		if (null != tbGeneralHVOss.getGeh_calbody()) {
			gbillhVO.setPk_calbody(tbGeneralHVOss.getGeh_calbody());
		}

		// 公司ID
		if (null != tbGeneralHVOss.getGeh_corp()) {
			gbillhVO.setPk_corp(tbGeneralHVOss.getGeh_corp());
		} else {
			gbillhVO.setPk_corp(ClientEnvironment.getInstance().getUser()
					.getCorpId());
		}

		// 最后修改时间
		gbillhVO.setAttributeValue("tlastmoditime", getBillUI()
				._getServerTime());
		// 制单时间
		gbillhVO.setAttributeValue("tmaketime", new UFDateTime(tbGeneralHVOss
				.getTmaketime()));
		// 单据号
		if (null != tbGeneralHVOss.getGeh_billcode()) {
			gbillhVO.setVbillcode(tbGeneralHVOss.getGeh_billcode());
		}

		// 设置回写表头VO

		voTempBill.setParentVO(gbillhVO);

		// 表体VO集合
		GeneralBillItemVO[] gbillbVOs = new GeneralBillItemVO[tbGeneralBVOss.length];
		// 设置表体VO
		String b_cgeneralbid = "";
		for (int i = 0; i < tbGeneralBVOss.length; i++) {
			// 表体VO
			gbillbVOs[i] = new GeneralBillItemVO();
			GeneralBillItemVO gbillbVO = gbillbVOs[i];
			// 当前单据表体
			TbGeneralBVO tbGeneralBVO = tbGeneralBVOss[i];

			// 辅计量单位ID
			if (null != tbGeneralBVO.getBmeasdocname()) {
				gbillbVO.setCastunitid(tbGeneralBVO.getCastunitid());
			}

			// 存货基本ID_
			if (null != tbGeneralBVO.getGeb_cinvbasid()) {
				gbillbVO.setCinvbasid(tbGeneralBVO.getGeb_cinvbasid());
			}
			// 通过公司和存货基本档案主键查询存货管理档案主键
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

			// 存货ID
			if (null != invmandocvo[0]) {
				gbillbVO.setCinventoryid(invmandocvo[0].toString().trim());
			}

			// 行号
			gbillbVO.setCrowno(i + 1 + "0");

			// 业务日期
			gbillbVO.setDbizdate(tbGeneralHVOss.getGeh_dbilldate());
			//
			// 是否赠品
			if (null != tbGeneralBVO.getGeb_flargess()) {
				gbillbVO.setFlargess(tbGeneralBVO.getGeb_flargess());
			}
			// 换算率
			if (null != tbGeneralBVO.getGeb_hsl()) {
				gbillbVO.setHsl(tbGeneralBVO.getGeb_hsl());
			}
			//
			// 在途辅数量
			double geb_virtualbnum = 0;
			if (null != tbGeneralBVO.getGeb_virtualbnum()
					&& !"".equals(tbGeneralBVO.getGeb_virtualbnum())) {
				geb_virtualbnum = tbGeneralBVO.getGeb_virtualbnum()
						.doubleValue();
			}
			// 实入辅数量
			if (null != tbGeneralBVO.getGeb_banum()) {
				gbillbVO.setNinassistnum(new UFDouble(tbGeneralBVO
						.getGeb_banum().doubleValue()
						+ geb_virtualbnum));

			}
			// 在途数量
			double geb_virtualnum = 0;
			if (null != tbGeneralBVO.getGeb_virtualnum()
					&& !"".equals(tbGeneralBVO.getGeb_virtualnum())) {
				geb_virtualnum = tbGeneralBVO.getGeb_virtualnum().doubleValue();
			}
			// 实入数量
			if (null != tbGeneralBVO.getGeb_anum()) {
				gbillbVO.setNinnum(new UFDouble(tbGeneralBVO.getGeb_anum()
						.doubleValue()
						+ geb_virtualnum));
			}

			// 金额
			if (null != tbGeneralBVO.getGeb_nmny()) {
				gbillbVO.setNmny(tbGeneralBVO.getGeb_nmny());
			}

			// 应入辅数量
			if (null != tbGeneralBVO.getGeb_bsnum()) {
				gbillbVO.setNneedinassistnum(new UFDouble(tbGeneralBVO
						.getGeb_bsnum().doubleValue()));
			}
			// 单价
			if (null != tbGeneralBVO.getGeb_nprice()) {
				// gbillbVO.setNprice(new UFDouble(1));
				gbillbVO.setNprice(tbGeneralBVO.getGeb_nprice());
			}

			// 应入数量
			if (null != tbGeneralBVO.getGeb_snum()) {
				gbillbVO.setNshouldinnum(new UFDouble(tbGeneralBVO
						.getGeb_snum().doubleValue()));
			}
			// 公司
			if (null != tbGeneralHVOss.getGeh_corp()) {
				gbillbVO.setAttributeValue("PK_CORP", tbGeneralHVOss
						.getGeh_corp().toString());
			}
			// 批次号
			if (null != tbGeneralBVO.getGeb_backvbatchcode()) {
				gbillbVO.setVbatchcode(tbGeneralBVO.getGeb_backvbatchcode());
			}
			// 根据批次，查询生产日期，失效日期
			String vbcsql = "select dproducedate ,dvalidate  from scm_batchcode where pk_invbasdoc='"
					+ tbGeneralBVO.getGeb_cinvbasid()
					+ "' and vbatchcode='"
					+ tbGeneralBVO.getGeb_backvbatchcode() + "' and dr=0";
			ArrayList vbc = (ArrayList) getQueryBO().executeQuery(vbcsql,
					new ArrayListProcessor());

			if (null != vbc && vbc.size() > 0 && null != vbc.get(0)) {
				// 生产日期
				if (null != ((Object[]) vbc.get(0))[0]
						&& !"".equals(((Object[]) vbc.get(0))[0].toString())) {
					gbillbVO.setScrq(new UFDate(((Object[]) vbc.get(0))[0]
							.toString()));
				}
				// 失效日期
				if (null != ((Object[]) vbc.get(0))[1]
						&& !"".equals(((Object[]) vbc.get(0))[1].toString())) {
					gbillbVO.setDvalidate(new UFDate(((Object[]) vbc.get(0))[1]
							.toString()));
				}
			}
			// // 生产日期
			// if (null != tbGeneralBVO.getGeb_proddate()) {
			// gbillbVO.setScrq(new UFDate(tbGeneralBVO.getGeb_proddate()
			// .toString()));
			// }
			// // 失效日期
			// if (null != tbGeneralBVO.getGeb_dvalidate()) {
			// gbillbVO.setDvalidate(new UFDate(tbGeneralBVO
			// .getGeb_dvalidate().toString()));
			// }
			// 凭证部分
			LocatorVO locatorvo = new LocatorVO();
			if (null != tbGeneralBVO.getGeb_space()) {
				locatorvo.setCspaceid(tbGeneralBVO.getGeb_space().toString());
			} else {
				// getBillUI().showErrorMessage("没有入库货位，不能入库！");
				return null;
			}
			locatorvo.setPk_corp(gbillhVO.getPk_corp());
			// 实入
			locatorvo.setNinspacenum(gbillbVO.getNinnum());
			// 实入辅
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
	 * 获得取消签字回写集合VO的方法
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
		// 获得调拨出库单表头主键
		String h_cgeneralhid = tbGeneralHVOss.getGeh_billcode();

		// 获得调拨出库单表头VO
		StringBuffer hsql = new StringBuffer(" vbillcode='");
		hsql.append(h_cgeneralhid);
		hsql.append("' and dr=0");
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());

		// 调拨出库单表头VO集合
		ArrayList firstVOs = (ArrayList) query.retrieveByClause(
				IcGeneralHVO.class, hsql.toString());
		if (null != firstVOs && firstVOs.size() != 0) {

			IcGeneralHVO firstVO = (IcGeneralHVO) firstVOs.get(0);

			// 要回写的调拨入库单表头VO
			GeneralBillHeaderVO gbillhVO = new GeneralBillHeaderVO();

			// 表头主键
			gbillhVO.setCgeneralhid(firstVO.getCgeneralhid());
			// 库存单据类型编码
			gbillhVO.setCbilltypecode("4A");
			// 调拨类型标志
			gbillhVO.setFallocflag(firstVO.getFallocflag());

			// 业务员ID
			gbillhVO.setCbizid(firstVO.getCbizid());

			// 收发类型ID
			gbillhVO.setCdispatcherid(firstVO.getCdispatcherid());

			// 部门ID
			gbillhVO.setCdptid(firstVO.getCdptid());

			// 最后修改人
			gbillhVO.setAttributeValue("clastmodiid", ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
			// 制单人
			gbillhVO.setCoperatorid(firstVO.getCoperatorid());
			gbillhVO.setAttributeValue("coperatoridnow", ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
			// 对方库存组织PK
			gbillhVO.setAttributeValue("cothercalbodyid", firstVO
					.getCothercalbodyid());
			// 对方公司ID
			gbillhVO.setAttributeValue("cothercorpid", firstVO
					.getCothercorpid());
			// 其它仓库ID
			gbillhVO.setAttributeValue("cotherwhid", firstVO.getCotherwhid());

			// 调出库存组织ID
			gbillhVO.setAttributeValue("cothercalbodyid", firstVO
					.getCothercalbodyid());
			// 调出公司ID
			gbillhVO.setAttributeValue("coutcorpid", firstVO.getCoutcorpid());

			// 供应商ID
			gbillhVO.setCproviderid(firstVO.getCproviderid());
			// 库房签字人
			gbillhVO.setCregister(firstVO.getCregister());
			// 仓库ID
			gbillhVO.setCwarehouseid(firstVO.getCwarehouseid());

			// 库房签字日期
			gbillhVO.setDaccountdate(firstVO.getDaccountdate());
			// 单据日期
			gbillhVO.setDbilldate(firstVO.getDbilldate());
			// 调拨类型标志
			gbillhVO.setFallocflag(firstVO.getFallocflag());
			// 单据状态
			gbillhVO.setFbillflag(2);
			// 退货标志
			gbillhVO.setFreplenishflag(firstVO.getFreplenishflag());
			// 库存组织PK
			gbillhVO.setPk_calbody(firstVO.getPk_calbody());
			// 公司ID
			gbillhVO.setPk_corp(firstVO.getPk_corp());
			// 供应商基本档案ID
			// if (null != firstVO[6]) {
			// // gbillhVO.setPk_cubasdoc(firstVO[6].toString());
			// }
			// 库房签字时间
			gbillhVO.setAttributeValue("taccounttime", firstVO
					.getTaccounttime());
			// 最后修改时间
			gbillhVO.setAttributeValue("tlastmoditime", new UFTime(myClientUI
					._getServerTime().toString()));
			// 制单时间
			gbillhVO.setAttributeValue("tmaketime", firstVO.getTmaketime());
			// 单据号
			gbillhVO.setVbillcode(firstVO.getVbillcode());
			// 设置回写表头VO

			voTempBill.setParentVO(gbillhVO);

			// 表体行数
			// int b_rows = getBillCardPanelWrapper().getBillCardPanel()
			// .getBillModel().getRowCount();
			// IcGeneralBVO
			// 获得调拨出库单表头主键
			String h_cgeneralhid1 = firstVO.getCgeneralhid();

			// 获得调拨出库单表头VO
			StringBuffer bsql = new StringBuffer(" cgeneralhid='");
			bsql.append(h_cgeneralhid1);
			bsql.append("' and dr=0");

			ArrayList icGeneralBVOs = (ArrayList) query.retrieveByClause(
					IcGeneralBVO.class, bsql.toString());
			// 表体VO集合
			GeneralBillItemVO[] gbillbVOs = new GeneralBillItemVO[icGeneralBVOs
					.size()];
			// 设置表体VO
			String b_cgeneralbid = "";
			for (int i = 0; i < icGeneralBVOs.size(); i++) {
				// 表体VO
				gbillbVOs[i] = new GeneralBillItemVO();
				GeneralBillItemVO gbillbVO = gbillbVOs[i];
				// 当前单据表体
				IcGeneralBVO icGeneralBVO = (IcGeneralBVO) icGeneralBVOs.get(i);

				// 表头主键
				gbillbVO.setCgeneralhid(icGeneralBVO.getCgeneralhid());
				// 表体主键
				gbillbVO.setCgeneralbid(icGeneralBVO.getCgeneralbid());
				// 辅计量单位ID
				gbillbVO.setCastunitid(icGeneralBVO.getCastunitid());
				// 源头单据表体ID
				gbillbVO.setCfirstbillbid(icGeneralBVO.getCfirstbillbid());
				// 源头单据表头ID
				gbillbVO.setCfirstbillhid(icGeneralBVO.getCfirstbillhid());
				// 源头单据类型
				gbillbVO.setCfirsttype(icGeneralBVO.getCfirsttype());
				// 存货基本ID
				gbillbVO.setCinvbasid(icGeneralBVO.getCinvbasid());
				// 存货ID
				gbillbVO.setCinventoryid(icGeneralBVO.getCinventoryid());
				// 调出公司
				// // 报价计量单位ID
				// if (null != childVO[7]) {
				// gbillbVO.setAttributeValue("CQUOTEUNITID", childVO[7]
				// .toString());
				// }
				// 行号
				gbillbVO.setCrowno(icGeneralBVO.getCrowno());
				// 来源单据表体序列号
				gbillbVO.setCsourcebillbid(icGeneralBVO.getCsourcebillbid());
				// 来源单据表头序列号
				gbillbVO.setCsourcebillhid(icGeneralBVO.getCsourcebillhid());
				// 来源单据类型
				gbillbVO.setCsourcetype("4Y");
				// 业务日期
				gbillbVO.setDbizdate(icGeneralBVO.getDbizdate());

				// 是否赠品
				gbillbVO.setFlargess(icGeneralBVO.getFlargess());
				// 换算率
				gbillbVO.setHsl(icGeneralBVO.getHsl());

				// 实入辅数量
				gbillbVO.setNinassistnum(icGeneralBVO.getNinassistnum());
				// 实入数量
				gbillbVO.setNinnum(icGeneralBVO.getNinnum());
				// 金额
				gbillbVO.setNmny(icGeneralBVO.getNmny());
				// 应入辅数量
				gbillbVO
						.setNneedinassistnum(icGeneralBVO.getNneedinassistnum());
				// 单价
				gbillbVO.setNprice(icGeneralBVO.getNprice());

				// 应入数量
				gbillbVO.setNshouldinnum(icGeneralBVO.getNshouldinnum());
				// 公司
				gbillbVO
						.setAttributeValue("PK_CORP", icGeneralBVO.getPk_corp());
				// 批次号
				gbillbVO.setVbatchcode(icGeneralBVO.getVbatchcode());
				// 源头单据号
				gbillbVO.setVfirstbillcode(icGeneralBVO.getVfirstbillcode());
				// 来源单据号
				gbillbVO.setVsourcebillcode(icGeneralBVO.getVsourcebillcode());
				// 来源单据行号
				gbillbVO.setVsourcerowno(icGeneralBVO.getVsourcerowno());

				// 生产日期
				// gbillbVO.setScrq(new UFDate(tbGeneralBVO.getGeb_proddate()
				// .toString()));
				// }
				// 失效日期
				gbillbVO.setDvalidate(icGeneralBVO.getDvalidate());
				// 凭证部分
				// LocatorVO locatorvo = new LocatorVO();
				// if (null != tbGeneralBVO.getGeb_space()) {
				// locatorvo.setCspaceid(tbGeneralBVO.getGeb_space()
				// .toString());
				// } else {
				// // getBillUI().showErrorMessage("没有入库货位，不能入库！");
				// return null;
				// }
				// // locatorvo.setCspaceid("1021A91000000004YZ0Q");
				// locatorvo.setPk_corp(gbillhVO.getPk_corp());
				// // 实入
				// locatorvo.setNinspacenum(gbillbVO.getNinnum());
				// // 实入辅
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
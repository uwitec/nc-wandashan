package nc.ui.wds.w8004040214;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;

import nc.bs.framework.common.NCLocator;
import nc.bs.ic.mm.pub.GenMethod;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.sfapp.IBillcodeRuleService;
import nc.itf.wds.w8004040210.Iw8004040210;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.buffer.BillUIBuffer;
import nc.ui.trade.businessaction.BdBusinessAction;
import nc.ui.trade.businessaction.BusinessAction;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.businessaction.IBusinessController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.INormalQuery;
import nc.ui.wds.w8000.CommonUnit;
import nc.ui.wds.w8004040210.TrayAction;
import nc.ui.wds.w8004040214.TrayDisposeDetailDlg;
import nc.ui.wds.w8004040214.TrayDisposeDlg;
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
import nc.vo.to.pub.ConstVO;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
import nc.vo.wds.w8004040210.MyBillVO;
import nc.vo.wds.w8004040210.TbGeneralBBVO;
import nc.vo.wds.w8004040210.TbGeneralBVO;
import nc.vo.wds.w8004040210.TbGeneralHVO;
import nc.vo.wds.w8004040212.TbWarehousestockVO;
import nc.vo.wds.w8004061002.BdCargdocTrayVO;
import nc.vo.wds.w8004061002.BdCargdocVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;
import nc.vo.wds.w80060608.IcGeneralBVO;
import nc.vo.wds.w80060608.IcGeneralHVO;

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
	private String billCode = null;
	private boolean dbbool = true;
	private boolean isControl1 = true;
	// 判断用户身份
	private String st_type = "";
	// 判断是总仓还是分仓
	private boolean sotckIsTotal = true;

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
			// if ("3".equals(st_type) && isedit) {
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
				nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zdtp).setEnabled(
				false);
		getButtonManager().getButton(
				nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zdrk).setEnabled(
				false);
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
		// boolean isControl = true;
		// if (null != InvLisk && InvLisk.size() > 0) {
		// isControl = false;
		// isControl1 = false;
		// } else {
		// isControl = true;
		// isControl1 = true;
		// }

		try {
			st_type = nc.ui.wds.w8000.CommonUnit.getUserType(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 仓库主键
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
						nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zj)
						.setEnabled(false);
				isControl1 = true;
			} else if ("0".equals(st_type)) {
				getButtonManager().getButton(
						nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zj)
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
						nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zj)
						.setEnabled(true);
				// isControl1 = false;
			} else {
				isControl1 = false;
			}
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
	}

	/**
	 * 自制单据
	 */
	public void onZzdj() throws Exception {
		if (getBillManageUI().isListPanelSelected())
			getBillManageUI().setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		getBillUI().setBillOperate(IBillOperate.OP_ADD);
		// 入库单类型
		getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("geh_billtype").setValue(new Integer(1));
		// 制单人
		getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
				"coperatorid").setValue(
				ClientEnvironment.getInstance().getUser().getPrimaryKey());
		// 添加仓库
		if (null != st_type && "0".equals(st_type)) {
			// 根据登录人默认仓库
			String pk_stordoc = nc.ui.wds.w8000.CommonUnit
					.getStordocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
			getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
					"geh_cwarehouseid").setValue(pk_stordoc);
			getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
					"geh_corp").setValue("1021");
			getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
					"geh_calbody").setValue("1021B1100000000001JL");
		} else if (null != st_type && "3".equals(st_type)) {
			// 根据登录人默认仓库
			String pk_stordoc = nc.ui.wds.w8000.CommonUnit
					.getStordocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
			getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
					"geh_cwarehouseid").setValue(pk_stordoc);
			getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
					"geh_corp").setValue("1021");
			getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
					"geh_calbody").setValue("1021B1100000000001JL");
		}

		// 自制单据号
		// try {
		// billCode = getBillCode("0214", ClientEnvironment.getInstance()
		// .getUser().getCorpId(), "", "");
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "geh_billcode").setValue(billCode);
		// } catch (BusinessException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "geh_cgeneralhid").setValue(getRandomNum());
		// 源头单据

		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_corp")
				.setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_calbody")
				.setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"geh_cwarehouseid").setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"geh_cwhsmanagerid").setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_cbizid")
				.setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"geh_cdispatcherid").setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"geh_cothercorpid").setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"geh_cothercalbodyid").setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"geh_cotherwhid").setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_cdptid")
				.setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fallocname")
				.setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vnote")
				.setEdit(true);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_crowno")
		// .setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("invcode")
				.setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
				"geb_vbatchcode").setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_bsnum")
				.setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_snum")
				.setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel()
				.getBodyItem("geb_proddate").setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
				"geb_dvalidate").setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_nprice")
				.setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_nmny")
				.setEdit(true);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_banum")
		// .setEdit(true);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_anum")
		// .setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vnote")
				.setEdit(true);
		getButtonManager().getButton(
				nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zdrk).setEnabled(
				true);
		getButtonManager().getButton(
				nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zdtp).setEnabled(
				true);
		getButtonManager().getButton(
				nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zj)
				.setEnabled(false);
		getBillUI().updateButtonUI();
		isedit = false;
		allRowNO = -1;
		// createBusinessAction();
	}

	// 是否是自制
	private boolean iszzdj = false;

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-9 8:37:34)
	 * 
	 * @return nc.ui.trade.pub.BillCardUI
	 */
	private BillManageUI getBillManageUI() {
		return (BillManageUI) getBillUI();
	}

	/**
	 * 发运单据
	 */
	@Override
	protected void onFydj() throws Exception {
		// TODO Auto-generated method stub
		try {
			List invLisk = nc.ui.wds.w8000.CommonUnit
					.getInvbasdoc_Pk(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
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

		// TbOutgeneralbillDlg tbOutgeneralbillDlg = new TbOutgeneralbillDlg(
		// myClientUI);

		// 6模版标识
		ProdWaybillDlg prodWaybillDlg = new ProdWaybillDlg(myClientUI);
		AggregatedValueObject[] vos = prodWaybillDlg.getReturnVOs(
				ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey(), ClientEnvironment.getInstance()
						.getUser().getPrimaryKey(), "0202",
				ConstVO.m_sBillDRSQ, "8004040214", "02140286", myClientUI);
		// 判断失总仓还是分仓

		try {

			if (null == vos || vos.length == 0) {

				return;
			}
			if (vos.length > 2) {
				getBillUI().showErrorMessage("一次只能选择一张运单！");
				return;
			}
			// TbProdwaybillVO genh = (TbProdwaybillVO) vos[0].getParentVO();
			// if (((BillManageUI)getBillUI()).isListPanelSelected())
			// ((BillManageUI)getBillUI()).setCurrentPanel(BillTemplateWrapper.CARDPANEL);
			MyBillVO voForSave = null;
			if (sotckIsTotal) {
				voForSave = queryLocaDataPwb(vos);
			} else {
				voForSave = queryLocaDataPwbf(vos);
			}

			if (null == voForSave) {

				voForSave = changeTbPwbtoTbgen(vos);
				isfirst = true;
			} else {
				isfirst = false;
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
			// if (sotckIsTotal) {
			if (isfirst) {
				getButtonManager().getButton(
						nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zdrk)
						.setEnabled(true);
			}
			// }

			getButtonManager().getButton(
					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zdtp)
					.setEnabled(true);
			getButtonManager().getButton(
					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zj).setEnabled(
					false);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"geb_virtualbnum").setEdit(true);
			getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
					"geb_customize2").setEdit(true);

			getBillUI().updateButtonUI();

		} catch (Exception e) {
			getBillUI().showErrorMessage(e.getMessage());
		}
		// addoredit = true;
		showZeroLikeNull(false);
		isfydj = true;
		isedit = false;
		// createBusinessAction();
	}

	private boolean isfydj = false;
	private boolean isfirst = true;

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
		tbGeneralHVO.setGeh_billtype(2);
		// 运单表头主键
		// tbGeneralHVO.setPwb_pk(tbProdwaybillVO.getPwb_pk());
		// 发运单表头主键
		tbGeneralHVO.setGeh_cgeneralhid(firstVO.getFyd_pk());
		// 其他单据单据号
		// try {
		// billCode = getBillCode("0202", ClientEnvironment.getInstance()
		// .getUser().getCorpId(), "", "");
		// tbGeneralHVO.setGeh_billcode(billCode);
		// } catch (BusinessException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// 出库公司主键
		// if (null != firstVO.getPk_fcorp()) {
		// tbGeneralHVO.setGeh_cothercorpid(firstVO.getPk_fcorp());
		// }
		// 出库库存组织主键
		// if (null != firstVO.getPk_calbody()) {
		// tbGeneralHVO.setGeh_cothercalbodyid(firstVO.getPk_calbody());
		// }
		// 出库仓库主键
		if (null != firstVO.getSrl_pk()) {
			tbGeneralHVO.setGeh_cotherwhid(firstVO.getSrl_pk());
		}
		// 业务员主键
		if (null != firstVO.getPk_psndoc()) {
			tbGeneralHVO.setGeh_cbizid(firstVO.getPk_psndoc());
		}

		// 收发类型ID
		// if (null != firstVO.getCdispatcherid()) {
		// tbGeneralHVO.setGeh_cdispatcherid(firstVO.getCdispatcherid());
		// }

		// 入库公司主键
		if (null != ClientEnvironment.getInstance().getUser().getCorpId()) {
			tbGeneralHVO.setGeh_corp(ClientEnvironment.getInstance().getUser()
					.getCorpId().toString());
		}
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
		tbGeneralHVO.setGeh_billtype(2);
		// 添加主表VO
		myBillVO.setParentVO(tbGeneralHVO);

		// 获取子表vo
		String pk = firstVO.getFyd_pk().toString();
		String sql = " fyd_pk='" + pk + "' and dr=0 ";
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// 全部子表VO
		List myTemp = new ArrayList();
		// 前台显示的VO
		List myTempShow = new ArrayList();

		myTempall = new ArrayList();
		try {
			ArrayList ttcs = (ArrayList) query.retrieveByClause(
					TbFydmxnewVO.class, sql.toString());
			// 当前登录的保管员能管理的货品
			List InvLisk = nc.ui.wds.w8000.CommonUnit
					.getInvbasdoc_Pk(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
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
				// 换算率
				// if (null != gbvo.getHsl()) {
				// tbGeneralBVO.setGeb_hsl(new UFDouble(gbvo.getHsl()
				// .toString()));
				// }
				// 单位
				// if (null != ttc.getPk_measdoc()) {
				// tbGeneralBVO.setPk_measdoc(ttc.getPk_measdoc());
				// }
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
				ArrayList vbc = (ArrayList) query.executeQuery(vbcsql,
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
				// 是否赠品
				if (null != gbvo.getBlargessflag()) {
					tbGeneralBVO.setGeb_flargess(gbvo.getBlargessflag());
				}
				// 应收辅数量
				if (null != gbvo.getCfd_sffsl()) {
					tbGeneralBVO.setGeb_bsnum(gbvo.getCfd_sffsl());
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
				if (null != gbvo.getCfd_sfsl()) {
					tbGeneralBVO.setGeb_snum(gbvo.getCfd_sfsl());
				}
				// tbGeneralBVO.setGeb_anum(tbGeneralBVO.getGeb_snum());
				// 单价
				// if (null != gbvo.getNprice()
				// && !"".equals(gbvo.getNprice().toString())) {
				// tbGeneralBVO.setGeb_nprice(new UFDouble(gbvo.getNprice()
				// .toString()));
				// }
				// 金额
				// if (null != gbvo.getNmny()
				// && !"".equals(gbvo.getNmny().toString())) {
				// tbGeneralBVO.setGeb_nmny(gbvo.getNmny());
				// }

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
				if (null != InvLisk && InvLisk.size() > 0) {
					for (int j = 0; j < InvLisk.size(); j++) {
						if (null != gbvo.getPk_invbasdoc()) {
							if (gbvo.getPk_invbasdoc().equals(InvLisk.get(j))) {
								if (null != tbGeneralBVO.getGeb_snum()
										&& 0 != tbGeneralBVO.getGeb_snum()
												.doubleValue()) {
									myTempShow.add(tbGeneralBVO);
								}

								break;
							}
						}
					}
				}

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
			IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			// 要显示的子表VO
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

	/**
	 * 指定托盘
	 */
	@Override
	protected void onZdtp() throws Exception {
		// TODO Auto-generated method stub
		//
		int bodyrownum = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getRowCount();
		if (bodyrownum == 0) {
			getBillUI().showErrorMessage("子表不能为空！");
			return;
		}
		// 验证货品主键

		String geb_cinvbasidy = (String) getBillCardPanelWrapper()
				.getBillCardPanel().getBillModel().getValueAt(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBillTable().getSelectedRow(), "invcode");
		if (null == geb_cinvbasidy || "".equals(geb_cinvbasidy)) {
			getBillUI().showErrorMessage("货品不能为空!");
			return;
		}
		// 验证批次号是否为空
		String geb_vbatchcodey = (String) getBillCardPanelWrapper()
				.getBillCardPanel().getBillModel().getValueAt(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBillTable().getSelectedRow(),
						"geb_vbatchcode");
		if (null == geb_vbatchcodey || "".equals(geb_vbatchcodey)) {
			getBillUI().showErrorMessage("批次号不能为空!");
			return;
		}
		// 验证应入数量
		UFDouble geb_bsnumy = (UFDouble) getBillCardPanelWrapper()
				.getBillCardPanel().getBillModel().getValueAt(
						getBillCardPanelWrapper().getBillCardPanel()
								.getBillTable().getSelectedRow(), "geb_bsnum");
		if (null == geb_bsnumy) {
			getBillUI().showErrorMessage("应入数量不能为空!");
			return;
		}
		// 验证批次号是否正确
		if (geb_vbatchcodey.trim().length() < 8) {
			getBillUI().showErrorMessage("批次号不能小于8位!");
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
					.showErrorMessage("批次号输入的不正确,请您输入正确的日期!如：20100101XXXXXX");
			return;
		}

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
		TrayDisposeDlg tdpDlg = new TrayDisposeDlg("80CC", ClientEnvironment
				.getInstance().getUser().getPrimaryKey(), ClientEnvironment
				.getInstance().getCorporation().getPrimaryKey(), "8004040214",
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
				String pk_cargdoc = "";
				try {
					pk_cargdoc = nc.ui.wds.w8000.CommonUnit
							.getCargDocName(ClientEnvironment.getInstance()
									.getUser().getPrimaryKey());
				} catch (BusinessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// 获得表体
				StringBuffer sql = new StringBuffer(
						"select cdt_pk from bd_cargdoc_tray where cdt_invbasdoc='"
								+ tbGeneralBVOs[i].getGeb_cinvbasid()
								+ "' and cdt_traystatus=0 and dr=0 and  pk_cargdoc='"
								+ pk_cargdoc + "' ");
				IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance()
						.lookup(IUAPQueryBS.class.getName());
				// 符合条件的全部托盘
				ArrayList cdts = new ArrayList();
				try {
					cdts = (ArrayList) query.executeQuery(sql.toString(),
							new ArrayListProcessor());

				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 托盘容积
				StringBuffer sqlVolume = new StringBuffer(
						"select def20 from bd_invbasdoc where pk_invbasdoc='");
				sqlVolume.append(tbGeneralBVOs[i].getGeb_cinvbasid());
				sqlVolume.append("'");
				ArrayList cdtsVolume = new ArrayList();
				cdtsVolume = (ArrayList) query.executeQuery(sqlVolume
						.toString(), new ArrayListProcessor());
				// 单品当前总容积
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
					getBillUI().showErrorMessage("请填写托盘容积！");
					return;
				}

				// 获取当前表体行的应发辅数量和实发辅数量进行比较，根据比较结果进行颜色显示
				// 红色：没有实发数量；灰色：有实发数量但是数量不够；白色：实发数量与应发数量相等
				Object num = getBillCardPanelWrapper().getBillCardPanel()
						.getBodyValueAt(i, "geb_bsnum");// 应收辅数量

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
		int selectRow = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getSelectedRow();

		// if (-1 == selectRow) {
		// getBillUI().showErrorMessage("表体中没有数据或您没有选择表体单据，不能进行操作！");
		// return;
		// }
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

	/**
	 * 自动入库
	 */
	@Override
	protected void onZdrk() throws Exception {
		// TODO Auto-generated method stub
		//
		// 货位
		String pk_cargdoc = "";
		try {
			pk_cargdoc = nc.ui.wds.w8000.CommonUnit
					.getCargDocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
		} catch (BusinessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		int bodyrownum = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getRowCount();
		if (bodyrownum == 0) {
			getBillUI().showErrorMessage("子表不能为空！");
			return;
		}
		// 验证货品主键
		for (int i = 0; i < bodyrownum; i++) {
			String geb_cinvbasidy = (String) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getValueAt(i, "invcode");
			if (null == geb_cinvbasidy || "".equals(geb_cinvbasidy)) {
				getBillUI().showErrorMessage("货品不能为空!");
				return;
			}
			// 验证批次号是否为空
			String geb_vbatchcodey = (String) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getValueAt(i,
							"geb_vbatchcode");
			if (null == geb_vbatchcodey || "".equals(geb_vbatchcodey)) {
				getBillUI().showErrorMessage("批次号不能为空!");
				return;
			}
			// 验证应入数量
			UFDouble geb_bsnumy = (UFDouble) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getValueAt(i,
							"geb_bsnum");
			if (null == geb_bsnumy) {
				getBillUI().showErrorMessage("应入数量不能为空!");
				return;
			}
			// 验证批次号是否正确
			if (geb_vbatchcodey.trim().length() < 8) {
				getBillUI().showErrorMessage("批次号不能小于8位!");
				return;
			}
			// String geb_vbatchcodey1 = geb_vbatchcodey.trim().substring(4, 6);
			// String geb_vbatchcodey2 = geb_vbatchcodey.trim().substring(6, 8);
			// if (Integer.parseInt(geb_vbatchcodey1) < 1
			// || Integer.parseInt(geb_vbatchcodey1) > 12
			// || Integer.parseInt(geb_vbatchcodey2) < 1
			// || Integer.parseInt(geb_vbatchcodey2) > 31) {
			// getBillUI()
			// .showErrorMessage("批次号输入的不正确,请您输入正确的日期!如：20100101XXXXXX");
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
						"批次号输入的不正确,请您输入正确的日期!如：20100101XXXXXX");
				return;
			}

		}
		// 得到全部表体VO

		// 表头
		// TbGeneralHVO mytbGeneralHVO = null;
		TbGeneralBVO[] tbGeneralBVOs = null;
		if (getBillManageUI().isListPanelSelected()) {
			// if (getBufferData().getVOBufferSize() != 0) {
			tbGeneralBVOs = (TbGeneralBVO[]) getBillManageUI()
					.getBillListWrapper().getVOFromUI().getChildrenVO();
			// mytbGeneralHVO = (TbGeneralHVO) getBillManageUI()
			// .getBillListWrapper().getVOFromUI().getParentVO();
			// }

		} else {
			// if (getBufferData().getVOBufferSize() != 0) {
			tbGeneralBVOs = (TbGeneralBVO[]) getBillCardPanelWrapper()
					.getBillVOFromUI().getChildrenVO();
			// mytbGeneralHVO = (TbGeneralHVO) getBillCardPanelWrapper()
			// .getBillVOFromUI().getParentVO();

			// }

		}

		// for (int i = 0; i < tbGeneralBVOs.length; i++) {
		// if (null != tbGeneralBVOs[i].getGeb_banum()) {
		// String geb_banum = tbGeneralBVOs[i].getGeb_banum().toString();
		// if ("".equals(geb_banum)) {
		// getBillUI().showErrorMessage("自动入库请填写实入数量！");
		// return;
		// }
		// } else {
		// getBillUI().showErrorMessage("自动入库请填写实入数量！");
		// return;
		// }
		//
		// }
		// 判断表体是否有数据
		if (null == tbGeneralBVOs || tbGeneralBVOs.length == 0) {
			getBillUI().showErrorMessage("表体中没有数据，不能进行操作！");
			return;
		}
		// 判断托盘容积是否能存放货物
		if (sotckIsTotal) {
			// 获取并判断表体是否有值，进行循环
			if (null != tbGeneralBVOs && tbGeneralBVOs.length > 0) {
				for (int i = 0; i < tbGeneralBVOs.length; i++) {

					// 获得表体
					StringBuffer sql = new StringBuffer(
							"select cdt_pk from bd_cargdoc_tray where cdt_invbasdoc='"
									+ tbGeneralBVOs[i].getGeb_cinvbasid()
									+ "' and cdt_traystatus=0 and dr=0 and  pk_cargdoc='"
									+ pk_cargdoc + "' ");

					// 符合条件的全部托盘
					ArrayList cdts = new ArrayList();
					try {
						cdts = (ArrayList) query.executeQuery(sql.toString(),
								new ArrayListProcessor());

					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// 托盘容积
					StringBuffer sqlVolume = new StringBuffer(
							"select def20 from bd_invbasdoc where pk_invbasdoc='");
					sqlVolume.append(tbGeneralBVOs[i].getGeb_cinvbasid());
					sqlVolume.append("'");
					ArrayList cdtsVolume = new ArrayList();
					cdtsVolume = (ArrayList) query.executeQuery(sqlVolume
							.toString(), new ArrayListProcessor());
					// 单品当前总容积
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
						getBillUI().showErrorMessage("请填写托盘容积！");
						return;
					}

					// 获取当前表体行的应发辅数量和实发辅数量进行比较，根据比较结果进行颜色显示
					// 红色：没有实发数量；灰色：有实发数量但是数量不够；白色：实发数量与应发数量相等
					Object num = getBillCardPanelWrapper().getBillCardPanel()
							.getBodyValueAt(i, "geb_bsnum");// 应收辅数量

					if (null != num && !"".equals(num)) {
						if (Double.parseDouble(num.toString().trim()) > invVolume)
							getBillCardPanelWrapper().getBillCardPanel()
									.getBodyPanel().setCellBackGround(i,
											"invcode", Color.red);
						else
							getBillCardPanelWrapper().getBillCardPanel()
									.getBodyPanel().setCellBackGround(i,
											"invcode", Color.white);
					} else
						getBillCardPanelWrapper().getBillCardPanel()
								.getBodyPanel().setCellBackGround(i, "invcode",
										Color.red);
				}

			}

			for (int i = 0; i < tbGeneralBVOs.length; i++) {
				// 获得表体
				StringBuffer sql = new StringBuffer(
						" select cdt_pk from bd_cargdoc_tray where cdt_invbasdoc='"
								+ tbGeneralBVOs[i].getGeb_cinvbasid()
								+ "' and cdt_traystatus=0 and dr=0 and  pk_cargdoc='"
								+ pk_cargdoc + "'  ");
				// IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance()
				// .lookup(IUAPQueryBS.class.getName());
				// 符合条件的全部托盘
				ArrayList cdts = new ArrayList();
				try {
					cdts = (ArrayList) query.executeQuery(sql.toString(),
							new ArrayListProcessor());

				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 托盘容积
				StringBuffer sqlVolume = new StringBuffer(
						"select def20 from bd_invbasdoc where pk_invbasdoc='");
				sqlVolume.append(tbGeneralBVOs[i].getGeb_cinvbasid());
				sqlVolume.append("'");
				ArrayList cdtsVolume = new ArrayList();
				cdtsVolume = (ArrayList) query.executeQuery(sqlVolume
						.toString(), new ArrayListProcessor());
				// 单品当前总容积
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
					getBillUI().showErrorMessage("请填写托盘或托盘容积！");
					return;
				}
				// 入库辅数量
				String geb_bsnum = "";
				if (null != tbGeneralBVOs[i].getGeb_bsnum()) {
					geb_bsnum = tbGeneralBVOs[i].getGeb_bsnum().toString();
				}
				// 入库辅数量
				double geb_bsnumd = 0;
				if (null != geb_bsnum && !"".equals(geb_bsnum)) {
					geb_bsnumd = Double.parseDouble(geb_bsnum);
				}

				if (geb_bsnumd > invVolume) {
					getBillUI()
							.showErrorMessage("入库数量大于当前库存容积！(存货编码为红色的托盘不足) ");
					return;
				}
			}
			// if (isfydj) {
			// String stordocName = nc.ui.wds.w8000.CommonUnit
			// .getStordocName(ClientEnvironment.getInstance()
			// .getUser().getPrimaryKey());
			// StringBuffer sql_cargdoc = new StringBuffer();
			// if (null != stordocName) {
			// sql_cargdoc.append(" cscode='11' and pk_stordoc='"
			// + stordocName.toString() + "' and dr=0 ");
			// }
			// ArrayList cargdocpks = new ArrayList();
			// cargdocpks = (ArrayList) query.retrieveByClause(
			// BdCargdocVO.class, sql_cargdoc.toString());
			// if (null != cargdocpks && cargdocpks.size() > 0
			// && null != cargdocpks.get(0)) {
			// BdCargdocVO cargVO = (BdCargdocVO) cargdocpks.get(0);
			// if (null != cargVO.getPk_cargdoc()) {
			// StringBuffer sql_cargdoc_tray = new StringBuffer(
			// " pk_cargdoc='" + cargVO.getPk_cargdoc()
			// + "' and dr=0");
			// ArrayList cargdoctrays = new ArrayList();
			// cargdoctrays = (ArrayList) query.retrieveByClause(
			// BdCargdocTrayVO.class, sql_cargdoc_tray
			// .toString());
			// if (null == cargdoctrays || cargdoctrays.size() == 0
			// || null == cargdoctrays.get(0)) {
			// getBillUI().showErrorMessage("仓库在途库没有虚拟托盘！");
			// return;
			// }
			// } else {
			// getBillUI().showErrorMessage("仓库没有在途库！");
			// return;
			// }
			// } else {
			// getBillUI().showErrorMessage("仓库没有在途库！");
			// return;
			// }
			// }

		} else {

			StringBuffer sql_cargtrays = new StringBuffer(
					" dr=0 and  pk_cargdoc='" + pk_cargdoc + "'  ");

			ArrayList cargtrays = (ArrayList) query.retrieveByClause(
					BdCargdocTrayVO.class, sql_cargtrays.toString());
			if (null == cargtrays || cargtrays.size() == 0
					|| null == cargtrays.get(0)) {
				getBillUI().showErrorMessage("分仓仓库没有虚拟托盘！");
				return;
			}

			// if (isfydj) {
			// String stordocName = nc.ui.wds.w8000.CommonUnit
			// .getStordocName(ClientEnvironment.getInstance()
			// .getUser().getPrimaryKey());
			// StringBuffer sql_cargdoc = new StringBuffer();
			// if (null != stordocName) {
			// sql_cargdoc.append(" cscode='03' and pk_stordoc='"
			// + stordocName.toString() + "' and dr=0 ");
			// }
			// ArrayList cargdocpks = new ArrayList();
			// cargdocpks = (ArrayList) query.retrieveByClause(
			// BdCargdocVO.class, sql_cargdoc.toString());
			// if (null != cargdocpks && cargdocpks.size() > 0
			// && null != cargdocpks.get(0)) {
			// BdCargdocVO cargVO = (BdCargdocVO) cargdocpks.get(0);
			// if (null != cargVO.getPk_cargdoc()) {
			// StringBuffer sql_cargdoc_tray = new StringBuffer(
			// " pk_cargdoc='" + cargVO.getPk_cargdoc()
			// + "' and dr=0");
			// ArrayList cargdoctrays = new ArrayList();
			// cargdoctrays = (ArrayList) query.retrieveByClause(
			// BdCargdocTrayVO.class, sql_cargdoc_tray
			// .toString());
			// if (null == cargdoctrays || cargdoctrays.size() == 0
			// || null == cargdoctrays.get(0)) {
			// getBillUI().showErrorMessage("仓库在途库没有虚拟托盘！");
			// return;
			// }
			// } else {
			// getBillUI().showErrorMessage("仓库没有在途库！");
			// return;
			// }
			// } else {
			// getBillUI().showErrorMessage("仓库没有在途库！");
			// return;
			// }
			// }
		}

		// 添加托盘信息
		for (int i = 0; i < tbGeneralBVOs.length; i++) {
			// 获得表体
			StringBuffer sql = new StringBuffer();
			if (sotckIsTotal) {
				sql
						.append("select cdt_pk from bd_cargdoc_tray where cdt_invbasdoc='"
								+ tbGeneralBVOs[i].getGeb_cinvbasid()
								+ "' and cdt_traystatus=0 and dr=0 and  pk_cargdoc='"
								+ pk_cargdoc + "' ");
				String mySql = " select distinct cdt_pk from tb_general_b_b where pk_invbasdoc='"
						+ tbGeneralBVOs[i].getGeb_cinvbasid()
						+ "' and pwb_pk !='"
						+ tbGeneralBVOs[i].getGeb_cgeneralbid()
						+ "' and dr=0 and pwb_pk in (' ";

				boolean myBoolean = false;
				for (int j = 0; j < tbGeneralBVOs.length; j++) {
					if (null != tbGeneralBVOs[i].getGeb_cinvbasid()
							&& null != tbGeneralBVOs[j].getGeb_cinvbasid()
							&& null != tbGeneralBVOs[i].getGeb_cgeneralbid()
							&& null != tbGeneralBVOs[j].getGeb_cgeneralbid()) {
						if (tbGeneralBVOs[i].getGeb_cinvbasid().equals(
								tbGeneralBVOs[j].getGeb_cinvbasid())
								&& !tbGeneralBVOs[i].getGeb_cgeneralbid()
										.equals(
												tbGeneralBVOs[j]
														.getGeb_cgeneralbid())) {
							mySql += tbGeneralBVOs[j].getGeb_cgeneralbid()
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

			// 符合条件的全部托盘
			ArrayList cdts = new ArrayList();
			try {
				cdts = (ArrayList) query.executeQuery(sql.toString(),
						new ArrayListProcessor());

			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 托盘容积
			StringBuffer sqlVolume = new StringBuffer(
					"select def20 from bd_invbasdoc where pk_invbasdoc='");
			sqlVolume.append(tbGeneralBVOs[i].getGeb_cinvbasid());
			sqlVolume.append("' and dr=0 ");
			ArrayList cdtsVolume = new ArrayList();
			cdtsVolume = (ArrayList) query.executeQuery(sqlVolume.toString(),
					new ArrayListProcessor());

			double invTrayVolume = 0;
			if (sotckIsTotal) {
				invTrayVolume = Double.parseDouble(((Object[]) cdtsVolume
						.get(0))[0].toString());
			} else {
				invTrayVolume = -1;
			}
			// 入库辅数量
			String geb_bsnum = "";
			if (null != tbGeneralBVOs[i].getGeb_bsnum()) {
				geb_bsnum = tbGeneralBVOs[i].getGeb_bsnum().toString();
			}
			// 当前入库辅数量
			double geb_bsnumd = 0;
			if (null != geb_bsnum && !"".equals(geb_bsnum)) {
				geb_bsnumd = Double.parseDouble(geb_bsnum);
			}
			// 存货托盘个数
			// int traynum=0;
			// if(geb_bsnumd!=0&&invTrayVolume!=0){
			// traynum=
			// }
			//
			if (null != cdts) {

			}

			// TbGeneralBBVO[] tbGeneralBBVO = null;

			// 获得要删除的VO
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
			if (-1 != invTrayVolume) {

				while (geb_bsnumd > invTrayVolume) {
					TbGeneralBBVO tbgbbvo = new TbGeneralBBVO();
					// 批次
					tbgbbvo.setGebb_vbatchcode(tbGeneralBVOs[i]
							.getGeb_vbatchcode());
					// 回写批次
					tbgbbvo.setGebb_lvbatchcode(tbGeneralBVOs[i]
							.getGeb_backvbatchcode());
					// 行号
					tbgbbvo.setGebb_rowno(k + 1 + "0");
					// 出库单表体主键
					tbgbbvo.setPwb_pk(tbGeneralBVOs[i].getGeb_cgeneralbid());
					// 换算率
					tbgbbvo.setGebb_hsl(tbGeneralBVOs[i].getGeb_hsl());
					// 运货档案主键
					tbgbbvo
							.setPk_invbasdoc(tbGeneralBVOs[i]
									.getGeb_cinvbasid());
					// 入库单子表主键
					tbgbbvo.setGeb_pk(tbGeneralBVOs[i].getGeb_pk());
					// 单价
					tbgbbvo.setGebb_nprice(tbGeneralBVOs[i].getGeb_nprice());
					// 金额
					tbgbbvo.setGebb_nmny(tbGeneralBVOs[i].getGeb_nmny());
					// 托盘实际存放数量
					tbgbbvo.setGebb_num(new UFDouble(invTrayVolume));
					geb_bsnumd = geb_bsnumd - invTrayVolume;
					// 托盘主键
					if (null != ((Object[]) cdts.get(k))[0]) {
						tbgbbvo.setCdt_pk(((Object[]) cdts.get(k))[0]
								.toString());
					}
					// DR
					tbgbbvo.setDr(0);
					tbgbbvo.setGeb_pk(tbGeneralBVOs[i].getGeb_pk());
					tbGeneralBBVOs.add(tbgbbvo);
					// // 托盘状态
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
			}

			TbGeneralBBVO tbgbbvo1 = new TbGeneralBBVO();
			// 批次
			tbgbbvo1.setGebb_vbatchcode(tbGeneralBVOs[i].getGeb_vbatchcode());
			// 回写批次
			tbgbbvo1.setGebb_lvbatchcode(tbGeneralBVOs[i]
					.getGeb_backvbatchcode());
			// 行号
			tbgbbvo1.setGebb_rowno(k + 1 + "0");
			// 出库单表体主键
			tbgbbvo1.setPwb_pk(tbGeneralBVOs[i].getGeb_cgeneralbid());
			// 换算率
			tbgbbvo1.setGebb_hsl(tbGeneralBVOs[i].getGeb_hsl());
			// 运货档案主键
			tbgbbvo1.setPk_invbasdoc(tbGeneralBVOs[i].getGeb_cinvbasid());
			// 入库单子表主键
			tbgbbvo1.setGeb_pk(tbGeneralBVOs[i].getGeb_pk());
			// 单价
			tbgbbvo1.setGebb_nprice(tbGeneralBVOs[i].getGeb_nprice());
			// 金额
			tbgbbvo1.setGebb_nmny(tbGeneralBVOs[i].getGeb_nmny());
			// 托盘实际存放数量
			tbgbbvo1.setGebb_num(new UFDouble(geb_bsnumd));
			// 托盘主键
			if (null != ((Object[]) cdts.get(k))[0]) {
				tbgbbvo1.setCdt_pk(((Object[]) cdts.get(k))[0].toString());
			}
			// DR
			tbgbbvo1.setDr(0);
			tbGeneralBBVOs.add(tbgbbvo1);
			// // 托盘状态
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

			// 获得自定义接口
			Iw8004040210 iw = (Iw8004040210) NCLocator.getInstance().lookup(
					Iw8004040210.class.getName());
			try {
				iw.delAndInsertTbGeneralBBVO(dtbbvos, tbGeneralBBVO);
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		TrayDisposeDetailDlg tdpDlg = new TrayDisposeDetailDlg("80DD",
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey(), "8004040214", tbGeneralBVOs);
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
		if ("1".equals(st_type)) {
			isControl = true;
		}
		// 先需要切换列表模式下一次，方便过滤数据
		super.onBoReturn();
		if (!isControl) {
			// 判断是否为空，如果为空 说明当前登录者不是仓库保管员
			if (sotckIsTotal) {
				if (null != InvLisk && InvLisk.size() > 0) {
					List list = new ArrayList();
					TbGeneralBVO[] generalbvo = null;
					// 获取当前选中行
					AggregatedValueObject billvo = getBufferData()
							.getCurrentVO();
					for (int i = 0; i < InvLisk.size(); i++) {
						// 判断选中行是否有数据
						if (null != billvo && null != billvo.getParentVO()
								&& null != billvo.getChildrenVO()
								&& billvo.getChildrenVO().length > 0) {
							// 取出子表数据
							generalbvo = (TbGeneralBVO[]) billvo
									.getChildrenVO();
							// 进行判断是否为空然后进行循环
							if (null != generalbvo && generalbvo.length > 0) {
								for (int j = 0; j < generalbvo.length; j++) {
									// 如果当前登录者仓库中的单品主键和单据中的单品主键相等放到一个集合里面 去
									// 然后填充页面
									if (InvLisk.get(i).equals(
											generalbvo[j].getGeb_cinvbasid())) {
										list.add(generalbvo[j]);
									}
								}
							}
						}
					}

					if (null != list && list.size() > 0) {
						// 进行数组转换
						generalbvo = new TbGeneralBVO[list.size()];
						generalbvo = (TbGeneralBVO[]) list.toArray(generalbvo);
						billvo.setChildrenVO(generalbvo);
						// 在更新到当前选中行中
						getBufferData().setCurrentVO(billvo);
						// 托盘指定按钮和自动取货按钮可用
						// changeButton(true);
					} else {
						// 如果集合里面没有数据 当前登录者是其他仓库的保管员
						// 把辅助功能按钮置为不可用
						// getButtonManager().getButton(ISsButtun.fzgn).setEnabled(
						// false);
						billvo.setChildrenVO(null);
						getBufferData().setCurrentVO(billvo);
					}
				}
			}
		}
		// super.onBoEdit();

		if (isControl) {
			// 设置收发类别和备注可编辑
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
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"geh_cdptid").setEdit(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"geh_cothercorpid").setEdit(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"geh_cothercalbodyid").setEdit(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"geh_cotherwhid").setEdit(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"geh_cbizid").setEdit(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vnote")
					.setEdit(true);
			getButtonManager().getButton(IBillButton.Line).setEnabled(false);

			getBillUI().updateButtonUI();

		} else {
			getButtonManager().getButton(
					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zdtp)
					.setEnabled(true);
			getButtonManager().getButton(
					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zdrk)
					.setEnabled(true);
			getButtonManager().getButton(
					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zj).setEnabled(
					false);
			getButtonManager().getButton(IBillButton.Line).setEnabled(false);
			getBillUI().updateButtonUI();
		}

		if ("3".equals(st_type)) {
			// 设置收发类别和备注可编辑
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
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"geh_cdptid").setEdit(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"geh_cothercorpid").setEdit(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"geh_cothercalbodyid").setEdit(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"geh_cotherwhid").setEdit(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
					"geh_cbizid").setEdit(true);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vnote")
					.setEdit(true);
			getButtonManager().getButton(IBillButton.Line).setEnabled(false);

			getButtonManager().getButton(
					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zdtp)
					.setEnabled(true);
			getButtonManager().getButton(
					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zdrk)
					.setEnabled(true);
			getButtonManager().getButton(
					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zj).setEnabled(
					false);
			// getButtonManager().getButton(IBillButton.Line).setEnabled(false);
			getBillUI().updateButtonUI();
		}
		getButtonManager().getButton(
				nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
				.setEnabled(false);
		getButtonManager().getButton(
				nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
				.setEnabled(false);
		super.onBoEdit();
		getButtonManager().getButton(IBillButton.Line).setEnabled(false);
		getBillUI().updateButtonUI();
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
		tbGeneralHVO.setGeh_billtype(3);
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
		tbGeneralHVO.setGeh_billtype(3);
		// 添加主表VO
		myBillVO.setParentVO(tbGeneralHVO);

		// 获取子表vo
		String pk = firstVO.getGeneral_pk().toString();
		String sql = " general_pk='" + pk + "' and dr=0 ";
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// 全部子表VO
		List myTemp = new ArrayList();
		// 前台显示的VO
		List myTempShow = new ArrayList();

		myTempall = new ArrayList();
		try {
			ArrayList ttcs = (ArrayList) query.retrieveByClause(
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
				ArrayList vbc = (ArrayList) query.executeQuery(vbcsql,
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
			IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
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
		// TODO Auto-generated method stub
		// 判断入库仓库和登录保管员是否是同一仓库
		// if ("1".equals(st_type)) {
		// super.onBoSave();
		// return;
		//
		// }
		// 1保管员 2信息科
		String islast = "1";
		if ("1".equals(st_type)) {
			islast = "2";
		}
		if ("3".equals(st_type) && isedit) {
			islast = "2";
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
					} // 
					// 签字后
					if (null != generalhvo.getPwb_fbillflag()
							&& generalhvo.getPwb_fbillflag() == 3) {
						getButtonManager().getButton(
								nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
								.setEnabled(false);
						getButtonManager().getButton(
								nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
								.setEnabled(true);
						getBillUI().updateButtonUI();
					} else { // 签字前
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
					} //
					// 签字后
					if (null != generalhvo.getPwb_fbillflag()
							&& generalhvo.getPwb_fbillflag() == 3) {
						getButtonManager().getButton(
								nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
								.setEnabled(false);
						getButtonManager().getButton(
								nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
								.setEnabled(true);
						getBillUI().updateButtonUI();
					} else { // 签字前
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
							nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zj)
							.setEnabled(true);
					getButtonManager().getButton(IBillButton.Edit).setEnabled(
							true);
					getBillUI().updateButtonUI();
				}
				// return;
			}
		}

		if (null != st_type && !"".equals(st_type)) {

		} else {
			getBillUI().showErrorMessage("你没有权限！");
			return;
		}
		if ("0".equals(st_type)) {
			String pk_stordoc = nc.ui.wds.w8000.CommonUnit
					.getStordocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
			String pk_stordocnow = "";
			if (null != getBillCardPanelWrapper().getBillCardPanel()
					.getHeadTailItem("geh_cwarehouseid")) {
				pk_stordocnow = getBillCardPanelWrapper().getBillCardPanel()
						.getHeadTailItem("geh_cwarehouseid").getValue();
			}

			if (!pk_stordoc.equals(pk_stordocnow)) {
				getBillUI().showErrorMessage("入库仓库和保管员仓库不符，不能保存！");
				return;
			}
		}
		if ("3".equals(st_type) && !isedit) {
			String pk_stordoc = nc.ui.wds.w8000.CommonUnit
					.getStordocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
			String pk_stordocnow = "";
			if (null != getBillCardPanelWrapper().getBillCardPanel()
					.getHeadTailItem("geh_cwarehouseid")) {
				pk_stordocnow = getBillCardPanelWrapper().getBillCardPanel()
						.getHeadTailItem("geh_cwarehouseid").getValue();
			}

			if (!pk_stordoc.equals(pk_stordocnow)) {
				getBillUI().showErrorMessage("入库仓库和保管员仓库不符，不能保存！");
				return;
			}
		}
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());

		int bodyrownum = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getRowCount();
		if (bodyrownum == 0) {
			getBillUI().showErrorMessage("子表不能为空！");
			return;
		}
		// 验证货品主键
		for (int i = 0; i < bodyrownum; i++) {
			String geb_cinvbasidy = (String) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getValueAt(i, "invcode");
			if (null == geb_cinvbasidy || "".equals(geb_cinvbasidy)) {
				getBillUI().showErrorMessage("货品不能为空!");
				return;
			}
			// 验证批次号是否为空
			String geb_vbatchcodey = (String) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getValueAt(i,
							"geb_vbatchcode");
			if (null == geb_vbatchcodey || "".equals(geb_vbatchcodey)) {
				getBillUI().showErrorMessage("批次号不能为空!");
				return;
			}
			// 验证应入数量
			UFDouble geb_bsnumy = (UFDouble) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getValueAt(i,
							"geb_bsnum");
			if (null == geb_bsnumy) {
				getBillUI().showErrorMessage("应入数量不能为空!");
				return;
			}
			// 验证批次号是否正确
			if (geb_vbatchcodey.trim().length() < 8) {
				getBillUI().showErrorMessage("批次号不能小于8位!");
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
						"批次号输入的不正确,请您输入正确的日期!如：20100101XXXXXX");
				return;
			}

		}
		// 非发运入库
		if (!isfydj) {
			// 判断调拨入库是否完成
			if ("0".equals(st_type) || "3".equals(st_type)) {
				int rowNum = getBillCardPanelWrapper().getBillCardPanel()
						.getBillTable().getRowCount();
				for (int i = 0; i < rowNum; i++) {
					double geb_bsnum = 0;
					double geb_snum = 0;
					double geb_banum = 0;
					double geb_anum = 0;
					// 应入辅数量
					if (null != getBillCardPanelWrapper().getBillCardPanel()
							.getBillModel().getValueAt(i, "geb_bsnum")) {
						geb_bsnum = Double
								.parseDouble(getBillCardPanelWrapper()
										.getBillCardPanel().getBillModel()
										.getValueAt(i, "geb_bsnum").toString());
					}
					// 应入数量
					if (null != getBillCardPanelWrapper().getBillCardPanel()
							.getBillModel().getValueAt(i, "geb_snum")) {
						geb_snum = Double.parseDouble(getBillCardPanelWrapper()
								.getBillCardPanel().getBillModel().getValueAt(
										i, "geb_snum").toString());
					}
					// 实入辅数量
					if (null != getBillCardPanelWrapper().getBillCardPanel()
							.getBillModel().getValueAt(i, "geb_banum")) {
						geb_banum = Double
								.parseDouble(getBillCardPanelWrapper()
										.getBillCardPanel().getBillModel()
										.getValueAt(i, "geb_banum").toString());
					}
					// 实入数量
					if (null != getBillCardPanelWrapper().getBillCardPanel()
							.getBillModel().getValueAt(i, "geb_anum")) {
						geb_anum = Double.parseDouble(getBillCardPanelWrapper()
								.getBillCardPanel().getBillModel().getValueAt(
										i, "geb_anum").toString());
					}
					if (geb_bsnum != geb_banum || geb_snum != geb_anum) {
						getBillUI().showErrorMessage("应入库、实入库数量不等，不能保存！");
						return;
					}
				}
			}

		}
		// 得到全部表体VO

		TbGeneralBVO[] tbGeneralBVOss = null;
		if (getBillManageUI().isListPanelSelected()) {
			// if (getBufferData().getVOBufferSize() != 0) {
			tbGeneralBVOss = (TbGeneralBVO[]) getBillManageUI()
					.getBillListWrapper().getVOFromUI().getChildrenVO();
			// }
		} else {
			// if (getBufferData().getVOBufferSize() != 0) {
			tbGeneralBVOss = (TbGeneralBVO[]) getBillCardPanelWrapper()
					.getBillVOFromUI().getChildrenVO();
			// }
		}
		// 判断表体是否有数据
		if (null == tbGeneralBVOss || tbGeneralBVOss.length == 0) {
			getBillUI().showErrorMessage("表体中没有数据，不能进行操作或无权操作！");
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

		if (!addoredit) {
			for (int i = 0; i < billVO.getChildrenVO().length; i++) {
				billVO.getChildrenVO()[i].setStatus(VOStatus.UPDATED);
			}
		} else {
			for (int i = 0; i < billVO.getChildrenVO().length; i++) {
				billVO.getChildrenVO()[i].setStatus(VOStatus.NEW);

			}
		}
		if ("3".equals(st_type) && isedit) {
			for (int i = 0; i < billVO.getChildrenVO().length; i++) {
				billVO.getChildrenVO()[i].setStatus(VOStatus.UPDATED);
			}
		} else if ("3".equals(st_type) && (!isedit)) {
			for (int i = 0; i < billVO.getChildrenVO().length; i++) {
				billVO.getChildrenVO()[i].setStatus(VOStatus.NEW);

			}
		}

		billVO.getChildrenVO()[0].setStatus(1);
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);

		//
		TbGeneralBVO[] tbGeneralBVOas = (TbGeneralBVO[]) billVO.getChildrenVO();
		// 验证表体是否有数据
		if (null == tbGeneralBVOas || tbGeneralBVOas.length < 0) {
			myClientUI.showErrorMessage("操作失败,您无权操作");
			return;
		}
		// 循环验证是否有没有出库的产品
		if (!isfydj) {
			for (int i = 0; i < tbGeneralBVOas.length; i++) {
				TbGeneralBVO genb = tbGeneralBVOas[i];
				if (null != genb.getGeb_banum()
						&& !"".equals(genb.getGeb_banum())) {
					if (null != genb.getGeb_bsnum()
							&& !"".equals(genb.getGeb_bsnum())) {
						if (genb.getGeb_banum().doubleValue() != genb
								.getGeb_bsnum().doubleValue()) {
							// int result = getBillUI().showOkCancelMessage(
							// "您有产品没有指定入库,是否保存?");
							// if (result != 1)
							// return;
							// else
							// break;
							getBillUI().showErrorMessage("您有产品没有指定入库");
							return;
						}
					}
				}
			}
		} else {
			for (int i = 0; i < tbGeneralBVOas.length; i++) {
				TbGeneralBVO genb = tbGeneralBVOas[i];
				if (null != genb.getGeb_banum()
						&& !"".equals(genb.getGeb_banum())) {
					if (null != genb.getGeb_bsnum()
							&& !"".equals(genb.getGeb_bsnum())) {
						if (null != genb.getGeb_virtualbnum()
								&& !"".equals(genb.getGeb_virtualbnum())) {

							if (genb.getGeb_banum().doubleValue()
									+ genb.getGeb_virtualbnum().doubleValue() != genb
									.getGeb_bsnum().doubleValue()) {
								// int result = getBillUI().showOkCancelMessage(
								// "您有产品没有指定入库,是否保存?");
								// if (result != 1)
								// return;
								// else
								// break;
								getBillUI().showErrorMessage(
										"您有产品没有指定入库或实入数量加在途数量大于应入数量！");
								return;
							}
						} else {
							if (genb.getGeb_banum().doubleValue() != genb
									.getGeb_bsnum().doubleValue()) {
								// int result = getBillUI().showOkCancelMessage(
								// "您有产品没有指定入库,是否保存?");
								// if (result != 1)
								// return;
								// else
								// break;
								getBillUI().showErrorMessage(
										"您有产品没有指定入库或实入数量加在途数量大于应入数量！");
								return;
							}
						}
					} else {
						getBillUI().showErrorMessage(
								"您有产品没有指定入库或实入数量加在途数量大于应入数量！");
						return;
					}
				} else {

					if (null != genb.getGeb_bsnum()
							&& !"".equals(genb.getGeb_bsnum())) {
						if (null != genb.getGeb_virtualbnum()
								&& !"".equals(genb.getGeb_virtualbnum())) {

							if (genb.getGeb_virtualbnum().doubleValue() != genb
									.getGeb_bsnum().doubleValue()) {
								// int result = getBillUI().showOkCancelMessage(
								// "您有产品没有指定入库,是否保存?");
								// if (result != 1)
								// return;
								// else
								// break;
								getBillUI().showErrorMessage(
										"您有产品没有指定入库或实入数量加在途数量大于应入数量！");
								return;
							}
						} else {
							getBillUI().showErrorMessage(
									"您有产品没有指定入库或实入数量加在途数量大于应入数量！");
							return;
						}
					} else {
						getBillUI().showErrorMessage(
								"您有产品没有指定入库或实入数量加在途数量大于应入数量！");
						return;
					}

				}
			}
		}
		//

		// if (null != myTempall && myTempall.size() > 0) {
		// // 把页面中显示的数据和后台隐藏的数据进行合并
		// TbGeneralBVO[] tbGeneralBVObs = new TbGeneralBVO[myTempall.size()];
		// myTempall.toArray(tbGeneralBVObs);
		// if (null != tbGeneralBVOas && tbGeneralBVOas.length > 0) {
		// for (int j = 0; j < tbGeneralBVObs.length; j++) {
		// TbGeneralBVO tmplistb = tbGeneralBVObs[j];
		// for (int i = 0; i < tbGeneralBVOas.length; i++) {
		// if (tmplistb.getGeb_cinvbasid().equals(
		// tbGeneralBVOas[i].getGeb_cinvbasid())) {
		// tbGeneralBVObs[j] = tbGeneralBVOas[i];
		// break;
		// }
		// }
		// }
		//
		// billVO.setChildrenVO(tbGeneralBVObs);
		// }
		//
		// }

		// 总仓和分仓的

		TbGeneralHVO generalh = (TbGeneralHVO) billVO.getParentVO();

		if (sotckIsTotal) {
			if ("0".equals(st_type)) {
				// 根据保管员货品，保存或修改自己货品子表
				List invLisk = nc.ui.wds.w8000.CommonUnit
						.getInvbasdoc_Pk(ClientEnvironment.getInstance()
								.getUser().getPrimaryKey());

				TbGeneralHVO tmpgeneralh = null;
				// 根据来源单据号查询是否有做过出库
				String strWhere = " geh_vbillcode='"
						+ generalh.getGeh_vbillcode()
						+ "' and geh_cgeneralhid='"
						+ generalh.getGeh_cgeneralhid() + "' and dr=0 ";
				ArrayList tmpList = (ArrayList) query.retrieveByClause(
						TbGeneralHVO.class, strWhere);
				if (null != tmpList && tmpList.size() > 0) {
					tmpgeneralh = (TbGeneralHVO) tmpList.get(0);
				}
				// boolean myisadd=true;
				// 该单据有过出库记录
				if (null != tmpgeneralh) {
					// 把表头替换
					generalh = tmpgeneralh;
					String gebbvosql = " dr=0 and geh_pk ='"
							+ generalh.getGeh_pk()
							+ "' and geb_cinvbasid in ('";
					for (int k = 0; k < invLisk.size(); k++) {
						if (null != invLisk && invLisk.size() > 0
								&& null != invLisk.get(k)
								&& !"".equals(invLisk.get(k))) {
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
					// 制单时间
					generalh.setTmaketime(myClientUI._getServerTime()
							.toString());
					// 制单日期，单据日期
					generalh.setCopetadate(new UFDate(new Date()));
					generalh.setGeh_dbilldate(new UFDate(new Date()));
					// 设置单据号
					generalh.setGeh_billcode(CommonUnit.getBillCode("4A",
							ClientEnvironment.getInstance().getCorporation()
									.getPk_corp(), "", ""));
				}

				// 如果是新增设置表体状态“新增”
				if (addoredit) {
					// 循环表体更改状态
					for (int i = 0; i < tbGeneralBVOas.length; i++) {
						tbGeneralBVOas[i].setStatus(VOStatus.NEW);
					}
				} else { // 设置表体状态“修改”
					// 循环表体更改状态
					for (int i = 0; i < tbGeneralBVOas.length; i++) {
						tbGeneralBVOas[i].setStatus(VOStatus.UPDATED);
					}
				}
			}
		} else {
			if ("0".equals(st_type) || "3".equals(st_type)) {
				// 根据保管员货品，保存或修改自己货品子表
				List invLisk = nc.ui.wds.w8000.CommonUnit
						.getInvbasdoc_Pk(ClientEnvironment.getInstance()
								.getUser().getPrimaryKey());

				TbGeneralHVO tmpgeneralh = null;
				// 根据来源单据号查询是否有做过出库
				String strWhere = " geh_vbillcode='"
						+ generalh.getGeh_vbillcode()
						+ "' and geh_cgeneralhid='"
						+ generalh.getGeh_cgeneralhid() + "' and dr=0 ";
				ArrayList tmpList = (ArrayList) query.retrieveByClause(
						TbGeneralHVO.class, strWhere);
				if (null != tmpList && tmpList.size() > 0) {
					tmpgeneralh = (TbGeneralHVO) tmpList.get(0);
					addoredit = false;
				} else {
					addoredit = true;
					// 制单时间
					generalh.setTmaketime(myClientUI._getServerTime()
							.toString());
					// 制单日期，单据日期
					generalh.setCopetadate(new UFDate(new Date()));
					generalh.setGeh_dbilldate(new UFDate(new Date()));
					// 设置单据号
					generalh.setGeh_billcode(CommonUnit.getBillCode("4A",
							ClientEnvironment.getInstance().getCorporation()
									.getPk_corp(), "", ""));
				}
				// 如果是新增设置表体状态“新增”
				if (addoredit) {
					// 循环表体更改状态
					for (int i = 0; i < tbGeneralBVOas.length; i++) {
						tbGeneralBVOas[i].setStatus(VOStatus.NEW);
					}
				} else { // 设置表体状态“修改”
					// 循环表体更改状态
					for (int i = 0; i < tbGeneralBVOas.length; i++) {
						tbGeneralBVOas[i].setStatus(VOStatus.UPDATED);
					}
				}

			}
		}
		// 最后修改时间
		generalh.setClastmodetime(myClientUI._getServerTime().toString());
		// 设置修改人
		generalh.setClastmodiid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey());
		// 单据状态
		generalh.setPwb_fbillflag(2);
		// 给聚合VO中表头赋值
		billVO.setParentVO(generalh);
		// 表体赋值
		billVO.setChildrenVO(tbGeneralBVOas);

		// 单据是否关闭
		TbGeneralBVO[] tbGenBVOs = (TbGeneralBVO[]) billVO.getChildrenVO();
		// 在途集合子表
		ArrayList vgebvos = new ArrayList();
		// 在库存存储
		double geb_virtualbnumy = 0;
		double geb_virtualnumy = 0;
		if (isfydj) {
			for (int i = 0; i < tbGenBVOs.length; i++) {
				double geb_virtualbnum = 0;
				double geb_virtualnum = 0;
				double geb_banum = 0;
				double geb_anum = 0;
				if (null != tbGenBVOs[i].getGeb_virtualbnum()
						&& tbGenBVOs[i].getGeb_virtualbnum().doubleValue() != 0) {
					geb_virtualbnum = tbGenBVOs[i].getGeb_virtualbnum()
							.doubleValue();
					geb_virtualbnumy = tbGenBVOs[i].getGeb_virtualbnum()
							.doubleValue();
					vgebvos.add(tbGenBVOs[i]);
				}
				if (null != tbGenBVOs[i].getGeb_virtualnum()) {
					geb_virtualnum = tbGenBVOs[i].getGeb_virtualnum()
							.doubleValue();
					geb_virtualnumy = tbGenBVOs[i].getGeb_virtualnum()
							.doubleValue();
				}

				if (null != tbGenBVOs[i].getGeb_banum()
						&& null != tbGenBVOs[i].getGeb_anum()) {
					geb_banum = tbGenBVOs[i].getGeb_banum().doubleValue();
					geb_anum = tbGenBVOs[i].getGeb_anum().doubleValue();
				}
				// DecimalFormat df=new DecimalFormat("0.00");
				//				
				// double x=(geb_anum + geb_virtualnum);
				// df.format(x);
				if (tbGenBVOs[i].getGeb_bsnum().toDouble().doubleValue() < geb_banum
						+ geb_virtualbnum
				// || geb_anum + geb_virtualnum > tbGenBVOs[i]
				// .getGeb_snum().toDouble().doubleValue()
				) {
					getBillUI().showErrorMessage("实际入库数量大于应入库数量！");
					return;

				}
				if (tbGenBVOs[i].getGeb_bsnum().toDouble().doubleValue() == geb_banum
						+ geb_virtualbnum
				// || geb_anum + geb_virtualnum == tbGenBVOs[i]
				// .getGeb_snum().toDouble().doubleValue()
				) {
					((TbGeneralBVO[]) billVO.getChildrenVO())[i]
							.setGeb_isclose(new UFBoolean("Y"));
				} else {
					((TbGeneralBVO[]) billVO.getChildrenVO())[i]
							.setGeb_isclose(new UFBoolean("N"));
				}
				// tbGenBVOs[i].setGeb_banum(new UFDouble(tbGenBVOs[i]
				// .getGeb_banum().toDouble().doubleValue()
				// + geb_virtualbnum));
				// tbGenBVOs[i].setGeb_anum(new UFDouble(tbGenBVOs[i]
				// .getGeb_anum().toDouble().doubleValue()
				// + geb_virtualnum));
				// tbGenBVOs[i].setGeb_virtualbnum(new UFDouble(0));
				// tbGenBVOs[i].setGeb_virtualnum(new UFDouble(0));
				if (!isfirst) {
					tbGenBVOs[i].setStatus(VOStatus.UPDATED);
				}

			}
		} else {

			for (int i = 0; i < tbGenBVOs.length; i++) {
				if (null != tbGenBVOs[i].getGeb_banum()
						&& null != tbGenBVOs[i].getGeb_anum()) {
					if (tbGenBVOs[i].getGeb_bsnum().toDouble().doubleValue() == tbGenBVOs[i]
							.getGeb_banum().toDouble().doubleValue()
							&& tbGenBVOs[i].getGeb_anum().toDouble()
									.doubleValue() == tbGenBVOs[i]
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
		}
		for (int i = 0; i < billVO.getChildrenVO().length; i++) {
			if (null == ((TbGeneralBVO[]) billVO.getChildrenVO())[i]
					.getGeb_backvbatchcode()
					|| "".equals(((TbGeneralBVO[]) billVO.getChildrenVO())[i]
							.getGeb_backvbatchcode().trim())) {
				((TbGeneralBVO[]) billVO.getChildrenVO())[i]
						.setGeb_backvbatchcode("2009");
			}

		}
		// 进行数据晴空
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

		// 判断是否有存盘数据
		if (billVO.getParentVO() == null
				&& (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0)) {
			isSave = false;
		} else {
			ArrayList params = new ArrayList();

			if (isfydj) {
				// if(null!=tbs[0].getGeb_pk()&&!"".equals(tbs[0].getGeb_pk())){
				// String tbssql=" geb_pk='"+tbs[0].getGeb_pk()+"' and dr=0 ";
				// ArrayList dtbvos = new ArrayList();
				// try {
				//
				// dtbvos = (ArrayList) query.retrieveByClause(
				// TbGeneralBVO.class, tbssql.toString());
				// } catch (BusinessException e1) {
				// // TODO Auto-generated catch block
				// e1.printStackTrace();
				// }
				//					
				//					
				// }

				// 获得要回写的集合VO
				// GeneralBillVO voTempBill = getVoTempBill();
				// 保存方法

				params.add(getBillUI().getUserObject());
				// 回写数据
				params.add(null);
				// 库存表数据
				TbGeneralBVO[] tbGeneralBVOs = (TbGeneralBVO[]) getBillUI()
						.getVOFromUI().getChildrenVO();
				// //
				// TbGeneralHVO abs = (TbGeneralHVO) getBillUI().getVOFromUI()
				// .getParentVO();
				// 通过出库单子表主键得到存库数据
				StringBuffer tgbbsql = new StringBuffer(" pwb_pk in ('");
				if (null != tbGeneralBVOs && tbGeneralBVOs.length > 0) {

					for (int i = 0; i < tbGeneralBVOs.length; i++) {
						TbGeneralBVO tbgenvo = null;
						if (null != tbGeneralBVOs[i].getGeb_pk()) {
							tbgenvo = (TbGeneralBVO) query.retrieveByPK(
									TbGeneralBVO.class, tbGeneralBVOs[i]
											.getGeb_pk().toString().trim());
						}
						if (isfirst) {
							if (null != tbGeneralBVOs[i].getGeb_cgeneralbid()
									&& !"".equals(tbGeneralBVOs[i]
											.getGeb_cgeneralbid())) {
								tgbbsql.append(tbGeneralBVOs[i]
										.getGeb_cgeneralbid());
								tgbbsql.append("','");
							}

						} else {
							if (null != tbgenvo) {
								if (tbGeneralBVOs[i].getGeb_banum()
										.doubleValue() != tbgenvo
										.getGeb_banum().doubleValue()
										&& tbGeneralBVOs[i].getGeb_anum()
												.doubleValue() != tbgenvo
												.getGeb_anum().doubleValue()) {

									if (null != tbGeneralBVOs[i]
											.getGeb_cgeneralbid()
											&& !"".equals(tbGeneralBVOs[i]
													.getGeb_cgeneralbid())) {
										tgbbsql.append(tbGeneralBVOs[i]
												.getGeb_cgeneralbid());
										tgbbsql.append("','");
									}
								}
							}
						}

					}
					tgbbsql.append("') and dr=0 ");
				}
				// 用户权限判断

				// 托盘缓存表

				ArrayList tbGeneralBBVOs = new ArrayList();

				// if (isfirst) {
				tbGeneralBBVOs = (ArrayList) query.retrieveByClause(
						TbGeneralBBVO.class, tgbbsql.toString());
				if (!isfydj) {
					if (null == tbGeneralBBVOs || tbGeneralBBVOs.size() == 0) {
						getBillUI().showErrorMessage("请指定托盘后再保存！");
						return;
					}
					// }
				}
				// 库存VO
				TbWarehousestockVO[] tbWarehousestockVO = new TbWarehousestockVO[tbGeneralBBVOs
						.size()
						+ vgebvos.size()];
				// 库存VO集合
				ArrayList twhsVOs = new ArrayList();
				// 托盘集合
				ArrayList bdCargdocTrayVOs = new ArrayList();
				String Cdt_pk = "";
				int k = 0;
				// 实际入库
				for (int i = 0; i < tbGeneralBBVOs.size(); i++) {
					if (null != tbGeneralBBVOs.get(i)) {
						TbGeneralBBVO tbbbvo = (TbGeneralBBVO) tbGeneralBBVOs
								.get(i);
						TbWarehousestockVO tbWarehousestockVO1 = new TbWarehousestockVO();
						// 托盘主键
						if (null != tbbbvo.getCdt_pk()
								&& !"".equals(tbbbvo.getCdt_pk())) {
							tbWarehousestockVO1.setPplpt_pk(tbbbvo.getCdt_pk());
						}
						// dr
						tbWarehousestockVO1.setDr(0);
						// 辅数量
						if (null != tbbbvo.gebb_num) {
							tbWarehousestockVO1.setWhs_stockpieces(tbbbvo
									.getGebb_num());
							tbWarehousestockVO1.setWhs_oanum(tbbbvo.gebb_num);
						}
						// 换算率
						if (null != tbbbvo.getGebb_hsl()) {
							tbWarehousestockVO1
									.setWhs_hsl(tbbbvo.getGebb_hsl());
						}
						// 单价
						if (null != tbbbvo.getGebb_nprice()) {
							tbWarehousestockVO1.setWhs_nprice(tbbbvo
									.getGebb_nprice());
						}
						// 金额
						if (null != tbbbvo.getGebb_nmny()) {
							tbWarehousestockVO1.setWhs_nmny(tbbbvo
									.getGebb_nmny());
						}
						// 主数量
						if (null != tbbbvo.getGebb_num()
								&& null != tbbbvo.getGebb_hsl()) {
							tbWarehousestockVO1
									.setWhs_stocktonnage(new UFDouble(tbbbvo
											.getGebb_num().doubleValue()
											* tbbbvo.getGebb_hsl()
													.doubleValue()));
							tbWarehousestockVO1.setWhs_omnum(new UFDouble(
									tbbbvo.getGebb_num().doubleValue()
											* tbbbvo.getGebb_hsl()
													.doubleValue()));

						}

						// 库存检查状态(默认合适)
						tbWarehousestockVO1.setSs_pk("0001AA100000000B1TYK");
						// 库存表状态
						tbWarehousestockVO1.setWhs_status(0);
						// 类型
						tbWarehousestockVO1.setWhs_type(1);
						//
						if (null != tbbbvo.getPwb_pk()
								&& !"".equals(tbbbvo.getPwb_pk())) {
							tbWarehousestockVO1.setPk_bodysource(tbbbvo
									.getPwb_pk());
						}
						// 存货档案主键
						if (null != tbbbvo.getPk_invbasdoc()
								&& !"".equals(tbbbvo.getPk_invbasdoc())) {
							tbWarehousestockVO1.setPk_invbasdoc(tbbbvo
									.getPk_invbasdoc());
						}
						// 批次号
						if (null != tbbbvo.getGebb_vbatchcode()
								&& !"".equals(tbbbvo.getGebb_vbatchcode())) {
							tbWarehousestockVO1.setWhs_batchcode(tbbbvo
									.getGebb_vbatchcode());
						}
						// 回写批次号
						if (null != tbbbvo.getGebb_lvbatchcode()
								&& !"".equals(tbbbvo.getGebb_lvbatchcode())) {
							tbWarehousestockVO1.setWhs_lbatchcode(tbbbvo
									.getGebb_lvbatchcode());
						} else {
							tbWarehousestockVO1.setWhs_lbatchcode("2009");
						}
						// 操作时间
						tbWarehousestockVO1.setOperatetime(new UFDateTime(
								new Date()));
						// 货位
						String cargdocPK = getCargdocPK(ClientEnvironment
								.getInstance().getUser().getPrimaryKey());
						tbWarehousestockVO1.setPk_cargdoc(cargdocPK);
						// 来源单据表体主键， 缓存表主键
						tbWarehousestockVO1.setPk_bodysource(tbbbvo
								.getGebb_pk());
						//
						// if (!addoredit) {
						//						
						// }
						//
						// 托盘状态

						if (null != tbbbvo.getCdt_pk()
								&& !"".equals(tbbbvo.getCdt_pk())) {
							Cdt_pk = tbbbvo.getCdt_pk();
						}
						BdCargdocTrayVO bdCargdocTrayVO = (BdCargdocTrayVO) query
								.retrieveByPK(BdCargdocTrayVO.class, Cdt_pk);
						bdCargdocTrayVO.setCdt_traystatus(new Integer(1));
						bdCargdocTrayVOs.add(bdCargdocTrayVO);
						twhsVOs.add(tbWarehousestockVO1);

					}
					// k = i + 1;
				}
				// 在途入库
				for (int i = 0; i < vgebvos.size(); i++) {
					if (null != vgebvos.get(i)) {
						TbGeneralBVO vgebvo = (TbGeneralBVO) vgebvos.get(i);
						TbWarehousestockVO tbWarehousestockVO1 = new TbWarehousestockVO();
						// 登录人所属仓库
						String pk_stordoc = nc.ui.wds.w8000.CommonUnit
								.getStordocName(ClientEnvironment.getInstance()
										.getUser().getPrimaryKey());
						// 查询在途库
						String stordoc_sql = "";
						if (sotckIsTotal) {
							stordoc_sql = "pk_stordoc='" + pk_stordoc
									+ "' and cscode='11' and dr=0 ";
						} else {
							stordoc_sql = "pk_stordoc='" + pk_stordoc
									+ "' and cscode='03' and dr=0 ";
						}
						ArrayList bdCargdocVOs = (ArrayList) query
								.retrieveByClause(BdCargdocVO.class,
										stordoc_sql);
						// 在途货位
						String bdCargdocname = "";
						if (null != vgebvo.getPk_customize2()
								&& !"".equals(vgebvo.getPk_customize2())) {
							bdCargdocname = vgebvo.getPk_customize2();

						} else {
							getBillUI().showErrorMessage("请您选择在途库！");
							return;
						}

						// 查询在途库托盘
						String bdcargtray_sql = "";
						if (null != bdCargdocname && !"".equals(bdCargdocname)) {
							bdcargtray_sql = "pk_cargdoc='" + bdCargdocname
									+ "' and dr=0 ";
							// 货位
							// String cargdocPK = getCargdocPK(ClientEnvironment
							// .getInstance().getUser().getPrimaryKey());
							tbWarehousestockVO1.setPk_cargdoc(bdCargdocname);
						} else {
							getBillUI().showErrorMessage("请您选择在途库！");
							return;
						}
						ArrayList bdcargdoctrays = new ArrayList();
						if (!"".equals(bdcargtray_sql)) {
							bdcargdoctrays = (ArrayList) query
									.retrieveByClause(BdCargdocTrayVO.class,
											bdcargtray_sql);
						} else {
							getBillUI().showErrorMessage("没有在途库或在途库错误!");
							return;
						}

						// 托盘主键
						if (null != bdcargdoctrays && bdcargdoctrays.size() > 0
								&& null != bdcargdoctrays.get(0)) {
							tbWarehousestockVO1
									.setPplpt_pk(((BdCargdocTrayVO) bdcargdoctrays
											.get(0)).getCdt_pk());
							Cdt_pk = ((BdCargdocTrayVO) bdcargdoctrays.get(0))
									.getCdt_pk();
						}
						// tbWarehousestockVO1.setPplpt_pk("0001RT100000000CJ81N");
						// dr
						tbWarehousestockVO1.setDr(0);
						// 辅数量
						if (null != vgebvo.getGeb_virtualbnum()) {
							tbWarehousestockVO1
									.setWhs_stockpieces(new UFDouble(vgebvo
											.getGeb_virtualbnum()));
							tbWarehousestockVO1.setWhs_oanum(new UFDouble(
									vgebvo.getGeb_virtualbnum()));
						}
						// 换算率
						if (null != vgebvo.getGeb_hsl()) {
							tbWarehousestockVO1.setWhs_hsl(vgebvo.getGeb_hsl());
						}
						// 单价
						if (null != vgebvo.getGeb_nprice()) {
							tbWarehousestockVO1.setWhs_nprice(vgebvo
									.getGeb_nprice());
						}
						// 金额
						if (null != vgebvo.getGeb_nmny()) {
							tbWarehousestockVO1.setWhs_nmny(vgebvo
									.getGeb_nmny());
						}
						// 主数量
						if (null != vgebvo.getGeb_virtualnum()) {
							tbWarehousestockVO1
									.setWhs_stocktonnage(new UFDouble(vgebvo
											.getGeb_virtualnum()));
							tbWarehousestockVO1.setWhs_omnum(new UFDouble(
									vgebvo.getGeb_virtualnum()));

						}

						// 库存检查状态(默认合适)
						tbWarehousestockVO1.setSs_pk("0001AA100000000B1TYK");
						// 库存表状态
						tbWarehousestockVO1.setWhs_status(0);
						// 类型
						tbWarehousestockVO1.setWhs_type(1);
						//
						if (null != vgebvo.getGeb_cgeneralbid()
								&& !"".equals(vgebvo.getGeb_cgeneralbid())) {
							tbWarehousestockVO1.setPk_bodysource(vgebvo
									.getGeb_cgeneralbid());
						}
						// 存货档案主键
						if (null != vgebvo.getGeb_cinvbasid()
								&& !"".equals(vgebvo.getGeb_cinvbasid())) {
							tbWarehousestockVO1.setPk_invbasdoc(vgebvo
									.getGeb_cinvbasid());
						}
						// 批次号
						if (null != vgebvo.getGeb_vbatchcode()
								&& !"".equals(vgebvo.getGeb_vbatchcode())) {
							tbWarehousestockVO1.setWhs_batchcode(vgebvo
									.getGeb_vbatchcode());
						}
						// 回写批次号
						if (null != vgebvo.getGeb_backvbatchcode()
								&& !"".equals(vgebvo.getGeb_backvbatchcode())) {
							tbWarehousestockVO1.setWhs_lbatchcode(vgebvo
									.getGeb_backvbatchcode());
						} else {
							tbWarehousestockVO1.setWhs_lbatchcode("2009");
						}
						// 操作时间
						tbWarehousestockVO1.setOperatetime(new UFDateTime(
								new Date()));

						// 来源单据表体主键， 缓存表主键
						// tbWarehousestockVO1.setPk_bodysource(vgebvo
						// .getGebb_pk());
						//
						// if (!addoredit) {
						//						
						// }
						//
						// 托盘状态
						// Cdt_pk = "0001RT100000000CJ81N";

						BdCargdocTrayVO bdCargdocTrayVO = (BdCargdocTrayVO) query
								.retrieveByPK(BdCargdocTrayVO.class, Cdt_pk);
						bdCargdocTrayVO.setCdt_traystatus(new Integer(1));
						bdCargdocTrayVOs.add(bdCargdocTrayVO);
						twhsVOs.add(tbWarehousestockVO1);

					}
					// k = i + 1;
				}
				twhsVOs.toArray(tbWarehousestockVO);
				params.add(tbWarehousestockVO);
				params.add(bdCargdocTrayVOs);
				// 制单时间
				// if (addoredit) {
				// ((TbGeneralHVO) billVO.getParentVO())
				// .setTmaketime(getBillUI()._getServerTime()
				// .toString());
				// }
			} else {

				// 获得要回写的集合VO
				// GeneralBillVO voTempBill = getVoTempBill();
				// 保存方法

				params.add(getBillUI().getUserObject());
				// 回写数据
				params.add(null);
				// 库存表数据
				TbGeneralBVO[] tbGeneralBVOs = (TbGeneralBVO[]) getBillUI()
						.getVOFromUI().getChildrenVO();
				//
				// TbGeneralHVO abs = (TbGeneralHVO) getBillUI().getVOFromUI()
				// .getParentVO();

				// 通过出库单子表主键得到存库数据
				StringBuffer tgbbsql = new StringBuffer(" pwb_pk in ('");
				if (null != tbGeneralBVOs && tbGeneralBVOs.length > 0) {
					for (int i = 0; i < tbGeneralBVOs.length; i++) {
						if (null != tbGeneralBVOs[i].getGeb_cgeneralbid()
								&& !"".equals(tbGeneralBVOs[i]
										.getGeb_cgeneralbid())) {
							tgbbsql.append(tbGeneralBVOs[i]
									.getGeb_cgeneralbid());
							tgbbsql.append("','");
						}

					}
					tgbbsql.append("') and dr=0 ");
				}
				// 用户权限判断

				// 托盘缓存表

				ArrayList tbGeneralBBVOs = (ArrayList) query.retrieveByClause(
						TbGeneralBBVO.class, tgbbsql.toString());
				if (null == tbGeneralBBVOs || tbGeneralBBVOs.size() == 0) {
					getBillUI().showErrorMessage("请指定托盘后再保存！");
					return;
				}
				// 库存VO
				TbWarehousestockVO[] tbWarehousestockVO = new TbWarehousestockVO[tbGeneralBBVOs
						.size()];
				ArrayList bdCargdocTrayVOs = new ArrayList();
				String Cdt_pk = "";
				for (int i = 0; i < tbGeneralBBVOs.size(); i++) {
					if (null != tbGeneralBBVOs.get(i)) {
						TbGeneralBBVO tbbbvo = (TbGeneralBBVO) tbGeneralBBVOs
								.get(i);
						tbWarehousestockVO[i] = new TbWarehousestockVO();
						// 托盘主键
						if (null != tbbbvo.getCdt_pk()
								&& !"".equals(tbbbvo.getCdt_pk())) {
							tbWarehousestockVO[i].setPplpt_pk(tbbbvo
									.getCdt_pk());
						}
						// dr
						tbWarehousestockVO[i].setDr(0);
						// 辅数量
						if (null != tbbbvo.gebb_num) {
							tbWarehousestockVO[i].setWhs_stockpieces(tbbbvo
									.getGebb_num());
							tbWarehousestockVO[i].setWhs_oanum(tbbbvo.gebb_num);
						}
						// 换算率
						if (null != tbbbvo.getGebb_hsl()) {
							tbWarehousestockVO[i].setWhs_hsl(tbbbvo
									.getGebb_hsl());
						}
						// 单价
						if (null != tbbbvo.getGebb_nprice()) {
							tbWarehousestockVO[i].setWhs_nprice(tbbbvo
									.getGebb_nprice());
						}
						// 金额
						if (null != tbbbvo.getGebb_nmny()) {
							tbWarehousestockVO[i].setWhs_nmny(tbbbvo
									.getGebb_nmny());
						}
						// 主数量
						if (null != tbbbvo.getGebb_num()
								&& null != tbbbvo.getGebb_hsl()) {
							tbWarehousestockVO[i]
									.setWhs_stocktonnage(new UFDouble(tbbbvo
											.getGebb_num().toDouble()
											* tbbbvo.getGebb_hsl().toDouble()));
							tbWarehousestockVO[i].setWhs_omnum(new UFDouble(
									tbbbvo.getGebb_num().toDouble()
											* tbbbvo.getGebb_hsl().toDouble()));

						}

						// 库存检查状态(默认合适)
						tbWarehousestockVO[i].setSs_pk("0001AA100000000B1TYK");
						// 库存表状态
						tbWarehousestockVO[i].setWhs_status(0);
						// 类型
						tbWarehousestockVO[i].setWhs_type(1);
						//
						if (null != tbbbvo.getPwb_pk()
								&& !"".equals(tbbbvo.getPwb_pk())) {
							tbWarehousestockVO[i].setPk_bodysource(tbbbvo
									.getPwb_pk());
						}
						// 存货档案主键
						if (null != tbbbvo.getPk_invbasdoc()
								&& !"".equals(tbbbvo.getPk_invbasdoc())) {
							tbWarehousestockVO[i].setPk_invbasdoc(tbbbvo
									.getPk_invbasdoc());
						}
						// 批次号
						if (null != tbbbvo.getGebb_vbatchcode()
								&& !"".equals(tbbbvo.getGebb_vbatchcode())) {
							tbWarehousestockVO[i].setWhs_batchcode(tbbbvo
									.getGebb_vbatchcode());
						}
						// 回写批次号
						if (null != tbbbvo.getGebb_lvbatchcode()
								&& !"".equals(tbbbvo.getGebb_lvbatchcode())) {
							tbWarehousestockVO[i].setWhs_lbatchcode(tbbbvo
									.getGebb_lvbatchcode());
						} else {
							tbWarehousestockVO[i].setWhs_lbatchcode("2009");
						}
						// 操作时间
						tbWarehousestockVO[i].setOperatetime(new UFDateTime(
								new Date()));
						// 货位
						String cargdocPK = getCargdocPK(ClientEnvironment
								.getInstance().getUser().getPrimaryKey());
						tbWarehousestockVO[i].setPk_cargdoc(cargdocPK);
						// // 来源单据表体主键， 缓存表主键
						// tbWarehousestockVO[i].setPk_bodysource(tbbbvo
						// .getGebb_pk());
						//
						// if (!addoredit) {
						//						
						// }
						//
						// 托盘状态

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
				// 制单时间
				// if (addoredit) {
				// ((TbGeneralHVO) billVO.getParentVO())
				// .setTmaketime(getBillUI()._getServerTime()
				// .toString());
				// }
			}
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
			//
			if (getBillUI().isSaveAndCommitTogether()) {
				billVO = getBusinessAction().saveAndCommit(billVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), checkVO);
			} else {

				// write to database
				// if (isControl1) {// 信息科保存，只保存当前单据
				// billVO = getBusinessAction().save(billVO,
				// getUIController().getBillType(),
				// _getDate().toString(), getBillUI().getUserObject(),
				// checkVO);
				// } else {// 保管员保存，需要其他信息的改动
				billVO = getBusinessAction().save(billVO,
						getUIController().getBillType(), _getDate().toString(),
						params, checkVO);
				// }
			}

		}

		// 进行数据恢复处理
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
			// 新增后操作处理
			setAddNewOperate(isAdding(), billVO);
		}
		// 设置保存后状态
		setSaveOperateState();
		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRow(nCurrentRow);
		}
		showZeroLikeNull(false);
		getButtonManager().getButton(
				nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zdtp).setEnabled(
				false);
		getButtonManager().getButton(
				nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zdrk).setEnabled(
				false);
		if (!isControl1) {
			getButtonManager().getButton(
					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zj).setEnabled(
					true);
		}
		if (!isControl1) {
			getButtonManager().getButton(IBillButton.Edit).setEnabled(false);

		}
		getBillUI().updateButtonUI();
		// 页面标签
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_corp")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_calbody")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"geh_cwarehouseid").setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"geh_cwhsmanagerid").setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_cbizid")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"geh_cdispatcherid").setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"geh_cothercorpid").setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"geh_cothercalbodyid").setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"geh_cotherwhid").setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fallocname")
				.setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vnote")
		// .setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_crowno")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("invcode")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
				"geb_vbatchcode").setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_bsnum")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_snum")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_banum")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_anum")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vnote")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
				"geb_virtualbnum").setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
				"geb_customize2").setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_nprice")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_nmny")
				.setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
		// "geb_virtualbnum").setShow(false);

		isfydj = false;
		isfirst = true;
		if (null != st_type && st_type.equals("1")
				&& getBufferData().getVOBufferSize() > 0) {

			AggregatedValueObject billvo = getBillUI().getVOFromUI();
			TbGeneralHVO generalhvo = null;
			if (null != billvo.getParentVO()) {
				generalhvo = (TbGeneralHVO) billvo.getParentVO();
			}

			// 签字后
			if (null != generalhvo.getPwb_fbillflag()
					&& generalhvo.getPwb_fbillflag() == 3) {
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
						.setEnabled(false);
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
						.setEnabled(true);
				getBillUI().updateButtonUI();
			} else { // 签字前
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

			// 签字后
			if (null != generalhvo.getPwb_fbillflag()
					&& generalhvo.getPwb_fbillflag() == 3) {
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
						.setEnabled(false);
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
						.setEnabled(true);
				getBillUI().updateButtonUI();
			} else { // 签字前
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
					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zj).setEnabled(
					true);
			getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
			getBillUI().updateButtonUI();
		}
		showZeroLikeNull(false);
	}

	/**
	 * 根据登录人员主键查询出对应的货位主键
	 * 
	 * @param pk
	 *            人员主键
	 * @return 仓库主键
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

	// 获得单据号
	public String getBillCode(String billtype, String pkcorp, String gcbm,
			String operator) throws BusinessException {
		String scddh = null;
		try {
			BillCodeObjValueVO vo = new BillCodeObjValueVO();
			String[] names = { "库存组织", "操作员" };
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
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
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
				nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zdtp).setEnabled(
				false);
		getButtonManager().getButton(
				nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zdrk).setEnabled(
				false);
		if (!isControl1) {
			getButtonManager().getButton(
					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zj).setEnabled(
					true);
		}
		if (!isControl1) {
			getButtonManager().getButton(IBillButton.Edit).setEnabled(false);

		}
		getBillUI().updateButtonUI();
		// 页面标签
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_corp")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_calbody")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"geh_cwarehouseid").setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"geh_cwhsmanagerid").setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_cbizid")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"geh_cdispatcherid").setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"geh_cothercorpid").setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"geh_cothercalbodyid").setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"geh_cotherwhid").setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fallocname")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vnote")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_crowno")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("invcode")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
				"geb_vbatchcode").setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_bsnum")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_snum")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_banum")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_anum")
				.setEdit(false);
		// getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vnote")
		// .setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
				"geb_virtualbnum").setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem(
				"geb_customize2").setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_nprice")
				.setEdit(false);
		getBillCardPanelWrapper().getBillCardPanel().getBodyItem("geb_nmny")
				.setEdit(false);

		isfydj = false;
		isfirst = true;
		if (null != st_type && st_type.equals("1")
				&& getBufferData().getVOBufferSize() > 0) {

			AggregatedValueObject billvo = getBillUI().getVOFromUI();
			TbGeneralHVO generalhvo = null;
			if (null != billvo.getParentVO()) {
				generalhvo = (TbGeneralHVO) billvo.getParentVO();
			}

			//

			// 签字后
			if (null != generalhvo.getPwb_fbillflag()
					&& generalhvo.getPwb_fbillflag() == 3) {
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
						.setEnabled(false);
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
						.setEnabled(true);
				getBillUI().updateButtonUI();
			} else { // 签字前
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

			// 签字后
			if (null != generalhvo.getPwb_fbillflag()
					&& generalhvo.getPwb_fbillflag() == 3) {
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
						.setEnabled(false);
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
						.setEnabled(true);
				getBillUI().updateButtonUI();
			} else { // 签字前
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
					nc.ui.wds.w8004040214.buttun0214.ISsButtun.Zj).setEnabled(
					true);
			getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
			getBillUI().updateButtonUI();
		}
		isedit = true;
		// createBusinessAction();
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		strWhere.append(" and geh_billtype !=0 ");

		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		List invLisk = nc.ui.wds.w8000.CommonUnit
				.getInvbasdoc_Pk(ClientEnvironment.getInstance().getUser()
						.getPrimaryKey());

		if ("0".equals(st_type)) {
			String pk_stordoc = nc.ui.wds.w8000.CommonUnit
					.getStordocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
			if (sotckIsTotal) {
				String gebbvosql = " dr=0 " + " and geb_cinvbasid in ('";
				for (int k = 0; k < invLisk.size(); k++) {
					if (null != invLisk && invLisk.size() > 0
							&& null != invLisk.get(k)
							&& !"".equals(invLisk.get(k))) {
						gebbvosql += invLisk.get(k) + "','";
					}
				}
				gebbvosql += "') ";
				ArrayList gebbvos = (ArrayList) query.retrieveByClause(
						TbGeneralBVO.class, gebbvosql);
				if (null != gebbvos && gebbvos.size() > 0) {
					strWhere.append(" and geh_cwarehouseid='" + pk_stordoc
							+ "' and geh_pk in ('");
					for (int i = 0; i < gebbvos.size(); i++) {
						strWhere.append(((TbGeneralBVO) gebbvos.get(i))
								.getGeh_pk());
						strWhere.append("','");
					}
					strWhere.append("')");
				} else {
					strWhere.append(" and 1=2 ");
				}
			} else {
				strWhere.append(" and geh_cwarehouseid='" + pk_stordoc + "' ");
			}
		} else if ("3".equals(st_type)) {
			if (!sotckIsTotal) {
				String pk_stordoc = nc.ui.wds.w8000.CommonUnit
						.getStordocName(ClientEnvironment.getInstance()
								.getUser().getPrimaryKey());
				strWhere.append(" and geh_cwarehouseid='" + pk_stordoc + "' ");
			}
		}

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
		if (!isControl1) {
			getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
			getBillUI().updateButtonUI();
		}
		super.onBoReturn();
		// if ("3".equals(st_type) && getBufferData().getVOBufferSize() > 0) {
		// getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
		// getBillUI().updateButtonUI();
		// }
		showZeroLikeNull(false);
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
		Iw8004040210 iw = (Iw8004040210) NCLocator.getInstance().lookup(
				Iw8004040210.class.getName());
		iw.canceldelete8004040210("CANCELSIGN", "DELETE"/* 回写脚本名称 */,
				"4A"/* 单据类型 */, _getDate().toString(), voTempBill,
				tbGeneralHVOss);

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

		Iw8004040210 iw = (Iw8004040210) NCLocator.getInstance().lookup(
				Iw8004040210.class.getName());
		iw.pushsavesign8004040210("PUSHSAVESIGN"/* 回写脚本名称 */, "4A"/* 单据类型 */,
				_getDate().toString(), voTempBill, tbGeneralHVOss);
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
		// // 获得调拨出库单表头主键
		// String h_cgeneralhid = tbGeneralHVOss.getGeh_cgeneralhid();
		//
		// // 获得调拨出库单表头VO
		// StringBuffer hsql = new StringBuffer(
		// "select fallocflag,PK_CALBODY,PK_CORP,CWAREHOUSEID"
		// + ",CPROVIDERID,FALLOCFLAG,PK_CUBASDOC,vbillcode"
		// + ",cgeneralhid "
		// + " from ic_general_h where cgeneralhid='");
		// hsql.append(h_cgeneralhid);
		// hsql.append("' and dr=0");
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		//
		// // 调拨出库单表头VO集合
		// ArrayList firstVOs = (ArrayList) query.executeQuery(hsql.toString(),
		// new ArrayListProcessor());
		// if (null != firstVOs && firstVOs.size() != 0) {

		// Object[] firstVO = (Object[]) firstVOs.get(0);
		// 要回写的调拨入库单表头VO
		GeneralBillHeaderVO gbillhVO = new GeneralBillHeaderVO();

		// 库存单据类型编码
		gbillhVO.setCbilltypecode("4A");
		// 调拨类型标志
		// if (null != firstVO[0]) {
		// gbillhVO.setFallocflag(Integer.parseInt(firstVO[0].toString()));
		// }
		// 业务员ID
		// if (null != tbGeneralHVOss.getGeh_cbizid()) {
		// gbillhVO.setCbizid(tbGeneralHVOss.getGeh_cbizid());
		// }
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
		// // // 对方库存组织PK
		// // if (null != tbGeneralHVOss) {
		// // gbillhVO.setAttributeValue("cothercalbodyid", tbGeneralHVOss
		// // .getGeh_cothercalbodyid());
		// // }
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
		// // // 调出库存组织ID
		// // if (null != tbGeneralHVOss) {
		// // gbillhVO.setAttributeValue("cothercalbodyid", tbGeneralHVOss
		// // .getGeh_cothercalbodyid());
		// // }
		// 调出公司ID
		// if (null != tbGeneralHVOss) {
		// gbillhVO.setAttributeValue("coutcorpid", tbGeneralHVOss
		// .getGeh_cothercorpid());
		//
		// }
		//
		// // 供应商ID
		// // if (null != firstVO[4]) {
		// // gbillhVO.setCproviderid(firstVO[4].toString());
		// // }
		// // 库房签字人
		// // gbillhVO.setCregister(ClientEnvironment.getInstance().getUser()
		// // .getPrimaryKey());
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
			ArrayList invmandocvos = (ArrayList) query.executeQuery(
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
			ArrayList vbc = (ArrayList) query.executeQuery(vbcsql,
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
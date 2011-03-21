package nc.ui.wds.w80060212;

import java.util.Iterator;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.to.pub.ConstVO;
import nc.vo.wds.w80060212.MyBillVO;
import nc.vo.wds.w80060212.TbCarmanagerVO;
import nc.vo.wds.w80060406.TbFydnewVO;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	private MyClientUI myClientUI;

	// 查询接口
	IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());
	// 保存接口
	IVOPersistence ivo = (IVOPersistence) NCLocator.getInstance().lookup(
			IVOPersistence.class.getName());

	boolean isAdd = false; // 是否增加或修改 true 增加 false 修改

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;
	}

	public void showZeroLikeNull(boolean bShowZeroLikeNull) {
		BillRendererVO vo = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getRendererVO();
		vo.setShowZeroLikeNull(bShowZeroLikeNull);
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
				.setShowFlags(vo);
	}

	@Override
	protected void onAdd() {
		// TODO Auto-generated method stub
		// param1 登录人公司主键 2登录人的主键 3弹出单据 5单据号 6查询模板
		CarTrackingbillDlg prodWaybillDlg = new CarTrackingbillDlg(myClientUI);
		AggregatedValueObject[] vos = prodWaybillDlg.getReturnVOs(
				ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey(), ClientEnvironment.getInstance()
						.getUser().getPrimaryKey(), "02020400",
				ConstVO.m_sBillDRSQ, "80060212", "02121", myClientUI);

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

			MyBillVO voForSave = changeTbPwbtoTbgen(vos);

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

			getButtonManager().getButton(nc.ui.wds.w80060212.ISsButtun.Adbtn)
					.setEnabled(false);

			getBillUI().updateButtonUI();
			isAdd = true;
		} catch (Exception e) {
			getBillUI().showErrorMessage(e.getMessage());
		}
		showZeroLikeNull(false);
		getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
	}

	private MyBillVO changeTbPwbtoTbgen(AggregatedValueObject[] vos) {

		MyBillVO myBillVO = new MyBillVO();
		try {

			// 车辆跟踪主表VO
			TbCarmanagerVO car = new TbCarmanagerVO();
			// 发运单主表VO
			TbFydnewVO firstVO = (TbFydnewVO) vos[0].getParentVO();

			// 添加发运单主键
			car.setPk_fydnew(firstVO.getFyd_pk());

			// 添加发运单单据号
			car.setCm_ddh(firstVO.getVbillno());
			// 添加经销商名称
			car.setKh_pk(firstVO.getPk_kh());
			// 添加订单派车
			car.setCm_ddpc(firstVO.getDmakedate());
			// 物流公司来车日期
			car
					.setCm_wlgslc(new UFDate(firstVO.getDmaketime().substring(
							0, 10)));
			// 添加发运单物流公司主键
			car.setPk_wlgs(firstVO.getTc_pk());
			// 添加发运单单据号接订单时间
			// TBCARTRACKINGVO.setJddate(firstVO.getDmakedate());
			// 添加发运单收货电话
			// car.setcm_(firstVO.getFyd_lxdh());
			// 添加发运单收货地址
			// TBCARTRACKINGVO.setFyd_shdz(firstVO.getFyd_shdz());
			// 得到子表数量和重量的和 并添加发运单数量和重量
			List list = (List) query
					.executeQuery(
							"select sum(tb_fydmxnew.cfd_sfsl),sum(tb_fydmxnew.cfd_sffsl) from tb_fydmxnew where tb_fydmxnew.fyd_pk ='"
									+ firstVO.getFyd_pk() + "' ",
							new ArrayListProcessor());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] o = (Object[]) it.next();
				car.setCm_zl(new UFDouble(Double.parseDouble(o[0].toString())));
				car.setCm_sl(new UFDouble(Double.parseDouble(o[1].toString())));
			}

			// 添加主表VO
			myBillVO.setParentVO(car);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return myBillVO;
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub

		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		super.onBoSave();

		getButtonManager().getButton(nc.ui.wds.w80060212.ISsButtun.Adbtn)
				.setEnabled(true);

		getBillUI().updateButtonUI();
		getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
	}

	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		if (isAdd) {
			getBillUI().initUI();
			getBufferData().clear();
		} else {
			super.onBoCancel();
			getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
		}
		getButtonManager().getButton(nc.ui.wds.w80060212.ISsButtun.Adbtn)
				.setEnabled(true);

		getBillUI().updateButtonUI();
	}

	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		super.onBoEdit();
		getButtonManager().getButton(nc.ui.wds.w80060212.ISsButtun.Adbtn)
				.setEnabled(false);

		getBillUI().updateButtonUI();
		isAdd = false;
	}

}
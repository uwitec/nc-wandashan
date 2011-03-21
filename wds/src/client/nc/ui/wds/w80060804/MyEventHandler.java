package nc.ui.wds.w80060804;

import java.lang.reflect.Array;
import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.HYQueryConditionDLG;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.SuperVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.wds.w80060804.TbTranscompanyVO;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	private boolean myaddbuttun = true;

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	protected void onTcOpen() throws Exception {
		// TODO 请实现此按钮事件的逻辑
		if (getBufferData().getCurrentVO() == null) {
			return;
		}
		super.onBoCard();
		String tc_pk = (String) getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("tc_pk").getValue();
		// 根据查询条件获得运输公司
		TbTranscompanyVO[] ttcVO = (TbTranscompanyVO[]) HYPubBO_Client
				.queryByCondition(TbTranscompanyVO.class, " tc_pk='" + tc_pk
						+ "'");
		ttcVO[0].setTc_archive(0);
		HYPubBO_Client.update(ttcVO[0]);
		// 重新显示数据
		StringBuffer strWhere = new StringBuffer(
				"tb_transcompany.tc_archive=1 and dr=0 ");
		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
		// 修改按钮属性
		getButtonManager().getButton(IBillButton.Add).setEnabled(false);
		myaddbuttun = false;
		// 转到卡片面，实现按钮的属性转换
		getBillUI().setCardUIState();
		// 在转回列表显示界面，显示数据
		super.onBoReturn();

	}

	protected void onTcStop() throws Exception {
		// TODO 请实现此按钮事件的逻辑
		if (getBufferData().getCurrentVO() == null) {
			return;
		}
		super.onBoCard();
		String tc_pk = (String) getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("tc_pk").getValue();
		// 根据查询条件获得运输公司
		TbTranscompanyVO[] ttcVO = (TbTranscompanyVO[]) HYPubBO_Client
				.queryByCondition(TbTranscompanyVO.class, " tc_pk='" + tc_pk
						+ "'");
		ttcVO[0].setTc_archive(1);
		HYPubBO_Client.update(ttcVO[0]);
		// 重新显示数据
		StringBuffer strWhere = new StringBuffer(
				" (tb_transcompany.tc_archive=0 or tb_transcompany.tc_archive is null) and dr=0 ");
		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
		// 修改按钮属性
		getButtonManager().getButton(IBillButton.Add).setEnabled(true);
		myaddbuttun = true;
		// 转到卡片面，实现按钮的属性转换
		getBillUI().setCardUIState();
		super.onBoReturn();
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		UIDialog querydialog = getQueryUI();
		if (querydialog.showModal() != UIDialog.ID_OK) {
			return;
		}
		// 得到查询条件
		String strWhere = ((HYQueryConditionDLG) querydialog).getWhereSql();
		if (strWhere == null)
			strWhere = " 1=1 ";
		// 得到所有的查询标签
		ConditionVO[] ttc = ((HYQueryConditionDLG) querydialog)
				.getQryCondEditor().getGeneralCondtionVOs();
		// ConditionVO[] vos =
		// ((HYQueryConditionDLG)querydialog).getQryCondEditor().getGeneralCondtionVOs();
		String sendsignState = "";
		for (ConditionVO vo : ttc) {
			if ("tb_transcompany.tc_archive".equals(vo.getFieldCode())) {
				sendsignState = vo.getValue();
				if ("Y".equals(sendsignState)) {
					strWhere = StringUtil.replaceIgnoreCase(strWhere,
							"tb_transcompany.tc_archive = 'Y'",
							"tb_transcompany.tc_archive=1 and dr=0 ");
					getButtonManager().getButton(IBillButton.Edit).setEnabled(
							true);
					getButtonManager().getButton(IBillButton.Delete)
							.setEnabled(true);

					getButtonManager().getButton(IBillButton.Refresh)
							.setEnabled(true);

					
					getButtonManager().getButton(
							nc.ui.wds.w80060804.tcButtun.ITcButtun.TcStop)
							.setEnabled(false);
					getButtonManager().getButton(
							nc.ui.wds.w80060804.tcButtun.ITcButtun.TcOpen)
							.setEnabled(true);
					getBillUI().updateButtons();
				} else {
					strWhere = StringUtil
							.replaceIgnoreCase(
									strWhere,
									"tb_transcompany.tc_archive = 'N'",
									" (tb_transcompany.tc_archive=0 or tb_transcompany.tc_archive is null) and dr=0 ");
					getButtonManager().getButton(IBillButton.Edit).setEnabled(
							true);
					getButtonManager().getButton(IBillButton.Delete)
							.setEnabled(true);

					getButtonManager().getButton(IBillButton.Refresh)
							.setEnabled(true);

					
					getButtonManager().getButton(
							nc.ui.wds.w80060804.tcButtun.ITcButtun.TcStop)
							.setEnabled(true);
					getButtonManager().getButton(
							nc.ui.wds.w80060804.tcButtun.ITcButtun.TcOpen)
							.setEnabled(false);
					getBillUI().updateButtons();
				}
			}

		}

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
		// 修改按钮属性
		if ("Y".equals(sendsignState)) {
			getButtonManager().getButton(IBillButton.Add).setEnabled(false);
			myaddbuttun = false;
		} else {
			getButtonManager().getButton(IBillButton.Add).setEnabled(true);
			myaddbuttun = true;
		}
		// 转到卡片面，实现按钮的属性转换
		getBillUI().setCardUIState();
		// 在转回列表显示界面，显示数据
		super.onBoReturn();
	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		//当点击增加时，封存按钮和解封按钮为不可编辑
		getButtonManager().getButton(
				nc.ui.wds.w80060804.tcButtun.ITcButtun.TcStop)
				.setEnabled(false);
		getButtonManager().getButton(
				nc.ui.wds.w80060804.tcButtun.ITcButtun.TcOpen)
				.setEnabled(false);
		super.onBoAdd(bo);

		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("tc_archive",
				0);
		// 根据查询条件获得运输公司
		String sql="tc_comcode is not null order by tc_comcode desc";
		IUAPQueryBS query=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		ArrayList ttcs=(ArrayList) query.retrieveByClause(TbTranscompanyVO.class, sql);
		TbTranscompanyVO[] ttcVO = new TbTranscompanyVO[ttcs.size()];
		ttcs.toArray(ttcVO);
		String tc_comcode="0";
		if(null!=ttcVO&&ttcs.size()!=0){
			tc_comcode = ttcVO[0].tc_comcode;
		}
		int comcode = Integer.parseInt(tc_comcode) + 1;
		// 自动补位
		tc_comcode = comcode + "";
		while (tc_comcode.length() < 3) {
			tc_comcode = "0" + tc_comcode;
		}
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("tc_comcode",
				tc_comcode);
		

	}
	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		//当点击修改时，封存按钮和解封按钮为不可编辑
		getButtonManager().getButton(
				nc.ui.wds.w80060804.tcButtun.ITcButtun.TcStop)
				.setEnabled(false);
		getButtonManager().getButton(
				nc.ui.wds.w80060804.tcButtun.ITcButtun.TcOpen)
				.setEnabled(false);
		
		
		super.onBoEdit();
		if(!myaddbuttun){
			getButtonManager().getButton(IBillButton.Line).setEnabled(false);
			// 转到卡片面，实现按钮的属性转换
			getBillUI().setCardUIState();
		}
		
		
	}
	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub

		super.onBoSave();
		// 修改按钮属性
		if (myaddbuttun) {
			getButtonManager().getButton(IBillButton.Add).setEnabled(true);
			getButtonManager().getButton(
					nc.ui.wds.w80060804.tcButtun.ITcButtun.TcStop).setEnabled(
					true);
			getButtonManager().getButton(
					nc.ui.wds.w80060804.tcButtun.ITcButtun.TcOpen).setEnabled(
					false);
			myaddbuttun = true;
			// 转到卡片面，实现按钮的属性转换
			getBillUI().setCardUIState();
			super.onBoReturn();
			super.onBoCard();
		} else {
			getButtonManager().getButton(IBillButton.Add).setEnabled(false);
			getButtonManager().getButton(
					nc.ui.wds.w80060804.tcButtun.ITcButtun.TcOpen).setEnabled(
					true);
			getButtonManager().getButton(
					nc.ui.wds.w80060804.tcButtun.ITcButtun.TcStop).setEnabled(
					false);
			myaddbuttun = false;
			// 转到卡片面，实现按钮的属性转换
			getBillUI().setCardUIState();
			super.onBoReturn();
			super.onBoCard();
		}
		super.onBoRefresh();
		
	}

	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCancel();
		// 修改按钮属性
		if (myaddbuttun) {
			getButtonManager().getButton(IBillButton.Add).setEnabled(true);
			getButtonManager().getButton(
					nc.ui.wds.w80060804.tcButtun.ITcButtun.TcStop).setEnabled(
					true);
			getButtonManager().getButton(
					nc.ui.wds.w80060804.tcButtun.ITcButtun.TcOpen).setEnabled(
					false);
			myaddbuttun = true;
			// 转到卡片面，实现按钮的属性转换
			getBillUI().setCardUIState();
			super.onBoReturn();
		} else {
			getButtonManager().getButton(IBillButton.Add).setEnabled(false);
			getButtonManager().getButton(
					nc.ui.wds.w80060804.tcButtun.ITcButtun.TcOpen).setEnabled(
					true);
			getButtonManager().getButton(
					nc.ui.wds.w80060804.tcButtun.ITcButtun.TcStop).setEnabled(
					false);
			myaddbuttun = false;
			// 转到卡片面，实现按钮的属性转换
			getBillUI().setCardUIState();
			super.onBoReturn();
		}
		super.onBoRefresh();
	}

	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		super.onBoDelete();
		// 修改按钮属性
		if (myaddbuttun) {
			getButtonManager().getButton(IBillButton.Add).setEnabled(true);
			myaddbuttun = true;
			// 转到卡片面，实现按钮的属性转换
			getBillUI().setCardUIState();
			super.onBoReturn();
		} else {
			getButtonManager().getButton(IBillButton.Add).setEnabled(false);
			myaddbuttun = false;
			// 转到卡片面，实现按钮的属性转换
			getBillUI().setCardUIState();
			super.onBoReturn();
		}
		super.onBoRefresh();
	}
	
	
}
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
 * ������AbstractMyEventHandler�������ʵ���࣬ ��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
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
		// TODO ��ʵ�ִ˰�ť�¼����߼�
		if (getBufferData().getCurrentVO() == null) {
			return;
		}
		super.onBoCard();
		String tc_pk = (String) getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("tc_pk").getValue();
		// ���ݲ�ѯ����������乫˾
		TbTranscompanyVO[] ttcVO = (TbTranscompanyVO[]) HYPubBO_Client
				.queryByCondition(TbTranscompanyVO.class, " tc_pk='" + tc_pk
						+ "'");
		ttcVO[0].setTc_archive(0);
		HYPubBO_Client.update(ttcVO[0]);
		// ������ʾ����
		StringBuffer strWhere = new StringBuffer(
				"tb_transcompany.tc_archive=1 and dr=0 ");
		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
		// �޸İ�ť����
		getButtonManager().getButton(IBillButton.Add).setEnabled(false);
		myaddbuttun = false;
		// ת����Ƭ�棬ʵ�ְ�ť������ת��
		getBillUI().setCardUIState();
		// ��ת���б���ʾ���棬��ʾ����
		super.onBoReturn();

	}

	protected void onTcStop() throws Exception {
		// TODO ��ʵ�ִ˰�ť�¼����߼�
		if (getBufferData().getCurrentVO() == null) {
			return;
		}
		super.onBoCard();
		String tc_pk = (String) getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("tc_pk").getValue();
		// ���ݲ�ѯ����������乫˾
		TbTranscompanyVO[] ttcVO = (TbTranscompanyVO[]) HYPubBO_Client
				.queryByCondition(TbTranscompanyVO.class, " tc_pk='" + tc_pk
						+ "'");
		ttcVO[0].setTc_archive(1);
		HYPubBO_Client.update(ttcVO[0]);
		// ������ʾ����
		StringBuffer strWhere = new StringBuffer(
				" (tb_transcompany.tc_archive=0 or tb_transcompany.tc_archive is null) and dr=0 ");
		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
		// �޸İ�ť����
		getButtonManager().getButton(IBillButton.Add).setEnabled(true);
		myaddbuttun = true;
		// ת����Ƭ�棬ʵ�ְ�ť������ת��
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
		// �õ���ѯ����
		String strWhere = ((HYQueryConditionDLG) querydialog).getWhereSql();
		if (strWhere == null)
			strWhere = " 1=1 ";
		// �õ����еĲ�ѯ��ǩ
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
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
		// �޸İ�ť����
		if ("Y".equals(sendsignState)) {
			getButtonManager().getButton(IBillButton.Add).setEnabled(false);
			myaddbuttun = false;
		} else {
			getButtonManager().getButton(IBillButton.Add).setEnabled(true);
			myaddbuttun = true;
		}
		// ת����Ƭ�棬ʵ�ְ�ť������ת��
		getBillUI().setCardUIState();
		// ��ת���б���ʾ���棬��ʾ����
		super.onBoReturn();
	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		//���������ʱ����水ť�ͽ�ⰴťΪ���ɱ༭
		getButtonManager().getButton(
				nc.ui.wds.w80060804.tcButtun.ITcButtun.TcStop)
				.setEnabled(false);
		getButtonManager().getButton(
				nc.ui.wds.w80060804.tcButtun.ITcButtun.TcOpen)
				.setEnabled(false);
		super.onBoAdd(bo);

		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("tc_archive",
				0);
		// ���ݲ�ѯ����������乫˾
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
		// �Զ���λ
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
		//������޸�ʱ����水ť�ͽ�ⰴťΪ���ɱ༭
		getButtonManager().getButton(
				nc.ui.wds.w80060804.tcButtun.ITcButtun.TcStop)
				.setEnabled(false);
		getButtonManager().getButton(
				nc.ui.wds.w80060804.tcButtun.ITcButtun.TcOpen)
				.setEnabled(false);
		
		
		super.onBoEdit();
		if(!myaddbuttun){
			getButtonManager().getButton(IBillButton.Line).setEnabled(false);
			// ת����Ƭ�棬ʵ�ְ�ť������ת��
			getBillUI().setCardUIState();
		}
		
		
	}
	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub

		super.onBoSave();
		// �޸İ�ť����
		if (myaddbuttun) {
			getButtonManager().getButton(IBillButton.Add).setEnabled(true);
			getButtonManager().getButton(
					nc.ui.wds.w80060804.tcButtun.ITcButtun.TcStop).setEnabled(
					true);
			getButtonManager().getButton(
					nc.ui.wds.w80060804.tcButtun.ITcButtun.TcOpen).setEnabled(
					false);
			myaddbuttun = true;
			// ת����Ƭ�棬ʵ�ְ�ť������ת��
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
			// ת����Ƭ�棬ʵ�ְ�ť������ת��
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
		// �޸İ�ť����
		if (myaddbuttun) {
			getButtonManager().getButton(IBillButton.Add).setEnabled(true);
			getButtonManager().getButton(
					nc.ui.wds.w80060804.tcButtun.ITcButtun.TcStop).setEnabled(
					true);
			getButtonManager().getButton(
					nc.ui.wds.w80060804.tcButtun.ITcButtun.TcOpen).setEnabled(
					false);
			myaddbuttun = true;
			// ת����Ƭ�棬ʵ�ְ�ť������ת��
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
			// ת����Ƭ�棬ʵ�ְ�ť������ת��
			getBillUI().setCardUIState();
			super.onBoReturn();
		}
		super.onBoRefresh();
	}

	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		super.onBoDelete();
		// �޸İ�ť����
		if (myaddbuttun) {
			getButtonManager().getButton(IBillButton.Add).setEnabled(true);
			myaddbuttun = true;
			// ת����Ƭ�棬ʵ�ְ�ť������ת��
			getBillUI().setCardUIState();
			super.onBoReturn();
		} else {
			getButtonManager().getButton(IBillButton.Add).setEnabled(false);
			myaddbuttun = false;
			// ת����Ƭ�棬ʵ�ְ�ť������ת��
			getBillUI().setCardUIState();
			super.onBoReturn();
		}
		super.onBoRefresh();
	}
	
	
}
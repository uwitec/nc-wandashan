package nc.ui.wds.w8006080602;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.HYQueryConditionDLG;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.wds.w80060804.TbTranscompanyVO;
import nc.vo.wds.w8006080602.TbCarinfVO;

/**
 *
 *������AbstractMyEventHandler�������ʵ���࣬
 *��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 *@author author
 *@version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	private boolean myaddbuttun=true;
	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	protected void onCfStop() throws Exception {
		// TODO ��ʵ�ִ˰�ť�¼����߼�
		if (getBufferData().getCurrentVO() == null) {
			return;
		}
		super.onBoCard();
		String cif_pk = (String) getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("cif_pk").getValue();
		// ���ݲ�ѯ������ó���
		TbCarinfVO[] ciVO = (TbCarinfVO[]) HYPubBO_Client
				.queryByCondition(TbCarinfVO.class, " cif_pk='" + cif_pk
						+ "'");
		ciVO[0].setCif_archive(1);
		HYPubBO_Client.update(ciVO[0]);
		// ������ʾ����
		StringBuffer strWhere = new StringBuffer(
				"(tb_carinf.cif_archive=0 or tb_carinf.cif_archive is null) and dr=0 ");
		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());
		
		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
		super.onBoReturn();

	}

	protected void onCfOpen() throws Exception {
		// TODO ��ʵ�ִ˰�ť�¼����߼�
		if (getBufferData().getCurrentVO() == null) {
			return;
		}
		super.onBoCard();
		String cif_pk = (String) getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("cif_pk").getValue();
		// ���ݲ�ѯ������ó���
		TbCarinfVO[] ciVO = (TbCarinfVO[]) HYPubBO_Client
				.queryByCondition(TbCarinfVO.class, " cif_pk='" + cif_pk
						+ "'");
		ciVO[0].setCif_archive(0);
		HYPubBO_Client.update(ciVO[0]);
		// ������ʾ����
		StringBuffer strWhere = new StringBuffer(
				"tb_carinf.cif_archive=1 and dr=0 ");
		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
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
		// �õ����еĲ�ѯ��ǩ
		ConditionVO[] ttc = ((HYQueryConditionDLG) querydialog)
				.getQryCondEditor().getGeneralCondtionVOs();
		
		String sendsignState = "";
		for (ConditionVO vo : ttc) {
			if ("tb_carinf.cif_archive".equals(vo.getFieldCode())) {
				sendsignState = vo.getValue();
				if ("Y".equals(sendsignState)) {
					strWhere = StringUtil.replaceIgnoreCase(strWhere,
							"tb_carinf.cif_archive = 'Y'",
							"tb_carinf.cif_archive=1 and dr=0 ");
					getButtonManager().getButton(IBillButton.Edit).setEnabled(
							true);
					getButtonManager().getButton(IBillButton.Add).setEnabled(
							false);
					getButtonManager().getButton(IBillButton.Delete)
							.setEnabled(true);

					getButtonManager().getButton(IBillButton.Refresh)
							.setEnabled(true);

					
					getButtonManager().getButton(
							nc.ui.wds.w8006080602.cfButtun.ICfButtun.CfStop)
							.setEnabled(false);
					getButtonManager().getButton(
							nc.ui.wds.w8006080602.cfButtun.ICfButtun.CfOpen)
							.setEnabled(true);
					getBillUI().updateButtons();
				} else {
					strWhere = StringUtil.replaceIgnoreCase(strWhere,
							"tb_carinf.cif_archive = 'N'",
							" (tb_carinf.cif_archive=0 or tb_carinf.cif_archive is null) and dr=0 ");
					getButtonManager().getButton(IBillButton.Edit).setEnabled(
							true);
					getButtonManager().getButton(IBillButton.Delete)
							.setEnabled(true);

					getButtonManager().getButton(IBillButton.Refresh)
							.setEnabled(true);

					
					getButtonManager().getButton(
							nc.ui.wds.w8006080602.cfButtun.ICfButtun.CfStop)
							.setEnabled(true);
					getButtonManager().getButton(
							nc.ui.wds.w8006080602.cfButtun.ICfButtun.CfOpen)
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
			myaddbuttun=false;
		} else {
			getButtonManager().getButton(IBillButton.Add).setEnabled(true);
			myaddbuttun=true;
		}
		// ת����Ƭ�棬ʵ�ְ�ť������ת��
		getBillUI().setCardUIState();
		// ��ת���б���ʾ���棬��ʾ����
		super.onBoReturn();

	}
	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		
		super.onBoSave();
		// �޸İ�ť����
		if(myaddbuttun){
			getButtonManager().getButton(IBillButton.Add).setEnabled(true);
			getButtonManager().getButton(
					nc.ui.wds.w8006080602.cfButtun.ICfButtun.CfStop)
					.setEnabled(true);
			getButtonManager().getButton(
					nc.ui.wds.w8006080602.cfButtun.ICfButtun.CfOpen)
					.setEnabled(false);
			myaddbuttun=true;
			// ת����Ƭ�棬ʵ�ְ�ť������ת��
			getBillUI().setCardUIState();
			super.onBoReturn();
		}else{
			getButtonManager().getButton(IBillButton.Add).setEnabled(false);
			getButtonManager().getButton(
					nc.ui.wds.w8006080602.cfButtun.ICfButtun.CfStop)
					.setEnabled(false);
			getButtonManager().getButton(
					nc.ui.wds.w8006080602.cfButtun.ICfButtun.CfOpen)
					.setEnabled(true);
			myaddbuttun=false;
			// ת����Ƭ�棬ʵ�ְ�ť������ת��
			getBillUI().setCardUIState();
			super.onBoReturn();
		}
	}
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		getButtonManager().getButton(
				nc.ui.wds.w8006080602.cfButtun.ICfButtun.CfStop)
				.setEnabled(false);
		getButtonManager().getButton(
				nc.ui.wds.w8006080602.cfButtun.ICfButtun.CfOpen)
				.setEnabled(false);
		super.onBoAdd(bo);
		
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("cif_archive", 0);
	}
	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		getButtonManager().getButton(
				nc.ui.wds.w8006080602.cfButtun.ICfButtun.CfStop)
				.setEnabled(false);
		getButtonManager().getButton(
				nc.ui.wds.w8006080602.cfButtun.ICfButtun.CfOpen)
				.setEnabled(false);
		super.onBoEdit();
		
	}
	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCancel();
		// �޸İ�ť����
		if(myaddbuttun){
			getButtonManager().getButton(IBillButton.Add).setEnabled(true);
			getButtonManager().getButton(
					nc.ui.wds.w8006080602.cfButtun.ICfButtun.CfStop)
					.setEnabled(true);
			getButtonManager().getButton(
					nc.ui.wds.w8006080602.cfButtun.ICfButtun.CfOpen)
					.setEnabled(false);
			myaddbuttun=true;
			// ת����Ƭ�棬ʵ�ְ�ť������ת��
			getBillUI().setCardUIState();
			super.onBoReturn();
		}else{
			getButtonManager().getButton(IBillButton.Add).setEnabled(false);
			getButtonManager().getButton(
					nc.ui.wds.w8006080602.cfButtun.ICfButtun.CfStop)
					.setEnabled(false);
			getButtonManager().getButton(
					nc.ui.wds.w8006080602.cfButtun.ICfButtun.CfOpen)
					.setEnabled(true);
			myaddbuttun=false;
			// ת����Ƭ�棬ʵ�ְ�ť������ת��
			getBillUI().setCardUIState();
			super.onBoReturn();
		}
	}
	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		super.onBoDelete();
		// �޸İ�ť����
		if(myaddbuttun){
			getButtonManager().getButton(IBillButton.Add).setEnabled(true);
			myaddbuttun=true;
			// ת����Ƭ�棬ʵ�ְ�ť������ת��
			getBillUI().setCardUIState();
			super.onBoReturn();
		}else{
			getButtonManager().getButton(IBillButton.Add).setEnabled(false);
			myaddbuttun=false;
			// ת����Ƭ�棬ʵ�ְ�ť������ת��
			getBillUI().setCardUIState();
			super.onBoReturn();
		}
	}

}
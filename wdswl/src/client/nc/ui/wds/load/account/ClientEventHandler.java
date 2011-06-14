package nc.ui.wds.load.account;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.wds.load.account.ExaggLoadPricVO;
import nc.vo.wl.pub.ButtonCommon;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientEventHandler extends WdsPubEnventHandler {

	public ClientUIQueryDlg queryDialog = null;

	public ClientEventHandler(ClientUI clientUI, IControllerBase control) {
		super(clientUI, control);
	}

	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
			queryDialog=new ClientUIQueryDlg(	getBillUI(),
					null,
					_getCorp().getPrimaryKey(),
					getBillUI()._getModuleCode(),
					_getOperator(),
					getBillUI().getBusinessType(),
					getBillUI().getNodeKey());
			//queryDialog = new ClientUIQueryDlg(getBillUI(), null, tempinfo);
		}
		return queryDialog;
	}
	
	@Override
	protected void onBoSave() throws Exception {
		super.onBoSave();
	}
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		// TODO Auto-generated method stub
		super.onBoElse(intBtn);
		switch (intBtn){
			case ButtonCommon.REFWDS6:
				((ClientUI)getBillUI()).setRefBillType(WdsWlPubConst.BILLTYPE_OTHER_OUT);
				onBillRef();
				break;
			case ButtonCommon.REFWDS7:
				((ClientUI)getBillUI()).setRefBillType(WdsWlPubConst.BILLTYPE_OTHER_IN);
				onBillRef();
				break;
			case ButtonCommon.REFWDS8:
				((ClientUI)getBillUI()).setRefBillType(WdsWlPubConst.BILLTYPE_SALE_OUT);
				onBillRef();
				break;
			case ButtonCommon.REFWDS9:
				((ClientUI)getBillUI()).setRefBillType(WdsWlPubConst.BILLTYPE_ALLO_IN);
				onBillRef();
				break;
		}
	}
	@Override
	protected boolean isDataChange() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected void setRefData(AggregatedValueObject[] vos) throws Exception {
		// ���õ���״̬
		getBillUI().setCardUIState();
		AggregatedValueObject vo = refVOChange(vos);
		if (vo == null)
			throw new BusinessException("δѡ����յ���");
		Class[] ParameterTypes = new Class[]{AggregatedValueObject[].class,String.class};
		Object[] ParameterValues = new Object[]{vos,_getCorp().getPk_corp()};
		Object o =LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, "nc.bs.wds.load.account.LoadAccountBS", "accoutLoadPrice", ParameterTypes, ParameterValues, 2);
		ExaggLoadPricVO billVo =(ExaggLoadPricVO) o;
		// ����Ϊ��������
		getBillUI().setBillOperate(IBillOperate.OP_REFADD);
		// ������
		getBillCardPanelWrapper().setCardData(billVo);
		//���ý���Ĭ��ֵ
		((ClientUI)getBillUI()).setRefDefalutData();
	}
	

}

package nc.ui.wds.dm.storebing;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.INormalQuery;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.ui.zmpub.pub.excel.UpLoadCtrlTool;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.wds.dm.storebing.TbStorcubasdocVO;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * 
 * ������AbstractMyEventHandler�������ʵ���࣬ ��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends WdsPubEnventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	protected UIDialog createQueryUI() {
		// TODO Auto-generated method stub
		return new MyQueryDIG(
				getBillUI(), null, 
				_getCorp().getPk_corp(), getBillUI().getModuleCode()
				
				, getBillUI()._getOperator(), null		
		);
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		AggregatedValueObject mybillVO1 = getBillUI().getVOFromUI();
		TbStorcubasdocVO[] tbStorcubasdocFormVOs = (TbStorcubasdocVO[]) mybillVO1
				.getChildrenVO();
		if (null != tbStorcubasdocFormVOs) {
			for (int i = 0; i < tbStorcubasdocFormVOs.length; i++) {
				String pk_cumandoc = tbStorcubasdocFormVOs[i].getPk_cumandoc();
				String pk_stordoc1 = tbStorcubasdocFormVOs[i].getPk_stordoc1();
				if (null == pk_cumandoc || "".equals(pk_cumandoc)) {
					if(null == pk_stordoc1 || "".equals(pk_stordoc1)){
						getBillUI().showErrorMessage("���̺ͷֲֲ���ͬʱΪ�գ�");
						return;
					}
					
				}
			}
			}

		WdsWlPubTool.beforeSaveBodyUnique(getBillCardPanelWrapper().getBillCardPanel().getBillTable(), 
				getBillCardPanelWrapper().getBillCardPanel().getBillModel(), new String[]{"pk_cumandoc","pk_stordoc1"}, new String[]{"����","�ֲ�"});

		super.onBoSave();
		UpLoadCtrlTool tool = new UpLoadCtrlTool();
		tool.setExcelReadCtrlClass("nc.ui.wds.dm.storebing.StoreBingERCtrl");
		tool.onExcelBill(getBillUI());
	}
	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ

	//	strWhere.append(" and def1='1' ");
		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();

	}
	
	@Override
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

		strWhere = "(" + strWhere + ")";

		if (getHeadCondition() != null)
			strWhere = strWhere + " and " + getHeadCondition();
		// ��������ֱ�Ӱ����ƴ�õĴ��ŵ�StringBuffer�ж���ȥ�Ż�ƴ���Ĺ���
		sqlWhereBuf.append(strWhere);
		return true;
	}

}
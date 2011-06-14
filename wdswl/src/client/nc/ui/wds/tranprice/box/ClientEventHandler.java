package nc.ui.wds.tranprice.box;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wl.pub.BeforeSaveValudate;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientEventHandler extends WdsPubEnventHandler {

	public ClientUIQueryDlg queryDialog = null;

	public ClientEventHandler(ClientUI clientUI, IControllerBase control) {
		super(clientUI, control);
	}

	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
			queryDialog = new ClientUIQueryDlg(getBillUI(), null, _getCorp()
					.getPrimaryKey(), getBillUI()._getModuleCode(),
					_getOperator(), getBillUI().getBusinessType(), getBillUI()
							.getNodeKey());
			// queryDialog = new ClientUIQueryDlg(getBillUI(), null, tempinfo);
		}
		return queryDialog;
	}

	@Override
	protected String getHeadCondition() {
		String strWhere = super.getHeadCondition();
		if (strWhere == null || "".equals(strWhere)) {
			return " and pk_billtype='" + WdsWlPubConst.WDSJ + "'";
		} else {
			return strWhere + " and pk_billtype='" + WdsWlPubConst.WDSJ + "'";
		}
	}

	@Override
	protected void onBoSave() throws Exception {
		beforeSaveValidate();
		super.onBoSave();
	}

	protected void beforeSaveValidate() throws Exception {
		// 对 最小箱数 和 最大箱数 的校验
		String o1 = (String) getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("nmincase").getValueObject();
		String o2 = (String) getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("nmaxcase").getValueObject();
		if (Double.valueOf(o1).compareTo(Double.valueOf(o2)) > 0) {
			throw new Exception("[最小箱数] 不能大于  [最大箱数]");
		}
		//
		// 表体不为空
		BeforeSaveValudate.BodyNotNULL(getBillCardPanelWrapper()
				.getBillCardPanel().getBillTable());
		// 最小距离 必须大于等于 最大距离
		int rowCount = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getRowCount();
		for (int i = 0; i < rowCount; i++) {
			UFDouble o11 = (UFDouble) getBillCardPanelWrapper()
					.getBillCardPanel().getBodyValueAt(i, "nmindistance");
			UFDouble o22 = (UFDouble) getBillCardPanelWrapper()
					.getBillCardPanel().getBodyValueAt(i, "nmaxdistance");
			if (o11.compareTo(o22) > 0) {
				throw new Exception(i+"行,[最小距离] 不能大于  [最大距离]");
			}
		}
	}
	
	@Override
	protected void onBoLineAdd() throws Exception {
		// TODO Auto-generated method stub
		super.onBoLineAdd();
		int count = getBillCardPanelWrapper().getBillCardPanel().getBillData().getBillModel().getRowCount();
		if(count>1){
			UFDouble nendnum = PuPubVO.getUFDouble_NullAsZero(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt((count-2), "nmaxdistance"));
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(nendnum.add(1), (count-1), "nmindistance");
		}
	}
	@Override
	protected void onBoCopy() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCopy();
		getBillUI().setDefaultData();
		getBillCardPanelWrapper().getBillCardPanel().setTailItem("vapproveid", null);
		getBillCardPanelWrapper().getBillCardPanel().setTailItem("dapprovedate", null);
	}

}

package nc.bs.wds.transfer;

import nc.bs.trade.business.HYSuperDMO;
import nc.vo.wds.transfer.BillField;
import nc.vo.trade.field.IBillField;

public class TransferDMO extends HYSuperDMO {
	@Override
	protected IBillField createBillField() throws Exception {
		return BillField.getInstance();
	}
}

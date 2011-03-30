package nc.ui.ld;

import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.treemanage.BillTreeManageUI;
import nc.vo.pub.CircularlyAccessibleValueObject;


public class ClientUI extends BillTreeManageUI{

	@Override
	protected IVOTreeData createTableTreeData() {
		
		return null;
	}

	@Override
	protected IVOTreeData createTreeData() {
		
		return new ClientManageUIData();
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	
		
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		
		
	}

	@Override
	protected void initSelfData() {
	
		
	}

	

}

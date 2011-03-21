package nc.ui.wds.w8004040220;

import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.SuperVO;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		String st_type = "";
		String pk_cargdoc = "";
		try {
			pk_cargdoc = nc.ui.wds.w8000.CommonUnit
					.getCargDocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
		} catch (BusinessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			st_type = nc.ui.wds.w8000.CommonUnit.getUserType(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
		} catch (BusinessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		if (null != st_type && "0".equals(st_type)) {
			if (null != pk_cargdoc && !"".equals(pk_cargdoc)) {
				strWhere.append(" and pk_customize='");
				strWhere.append(pk_cargdoc);
				strWhere.append("' ");
			}
		}
		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}
}
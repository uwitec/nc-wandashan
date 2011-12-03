package nc.ui.wds.dm.storebing;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.INormalQuery;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.wds.dm.storebing.TbStorcubasdocVO;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
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
						getBillUI().showErrorMessage("客商和分仓不能同时为空！");
						return;
					}
					
				}
			}
			}

		WdsWlPubTool.beforeSaveBodyUnique(getBillCardPanelWrapper().getBillCardPanel().getBillTable(), 
				getBillCardPanelWrapper().getBillCardPanel().getBillModel(), new String[]{"pk_cumandoc","pk_stordoc1"}, new String[]{"客商","分仓"});


//		// 除了本仓外其他仓库的绑定客商
//		StringBuffer sql_srcbd = new StringBuffer();
//		sql_srcbd.append(" dr=0 ");
//		if (null != bdStordocVO && null != bdStordocVO.getPk_stordoc()) {
//			sql_srcbd.append("and  pk_stordoc!='");
//			sql_srcbd.append(bdStordocVO.getPk_stordoc());
//			sql_srcbd.append("' ");
//		}
//		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
//				IUAPQueryBS.class.getName());
//		ArrayList tbStorcubasdocVOs = (ArrayList) query.retrieveByClause(
//				TbStorcubasdocVO.class, sql_srcbd.toString());
//		AggregatedValueObject mybillVO = getBillUI().getChangedVOFromUI();
//		TbStorcubasdocVO[] tbStorcubasdocChangeVOs = (TbStorcubasdocVO[]) mybillVO
//				.getChildrenVO();
//		if (null != tbStorcubasdocVOs && null != tbStorcubasdocChangeVOs) {
//			for (int i = 0; i < tbStorcubasdocVOs.size(); i++) {
//				TbStorcubasdocVO tbStorcubasdocVO = (TbStorcubasdocVO) tbStorcubasdocVOs
//						.get(i);
//				for (int j = 0; j < tbStorcubasdocChangeVOs.length; j++) {
//					TbStorcubasdocVO tbStorcubasdocChangeVO = tbStorcubasdocChangeVOs[j];
//					if (null != tbStorcubasdocVO.getPk_cubasdoc()
//							&& null != tbStorcubasdocChangeVO.getPk_cubasdoc()) {
//						if (tbStorcubasdocVO.getPk_cubasdoc().trim().equals(
//								tbStorcubasdocChangeVO.getPk_cubasdoc().trim())) {
//							getBillUI().showErrorMessage(
//									"添加的客商已经和其它分仓绑定，请您先解除绑定关系！");
//							return;
//						}
//					}
//				}
//			}
//		}

		super.onBoSave();
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		strWhere.append(" and def1='1' ");

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// 增加数据到Buffer
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
				// 业务类型编码
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_BusiCode() + "='"
						+ getBillUI().getBusicode() + "'";

			else
				// 业务类型
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_Busitype() + "='"
						+ getBillUI().getBusinessType() + "'";

		}

		strWhere = "(" + strWhere + ")";

		if (getHeadCondition() != null)
			strWhere = strWhere + " and " + getHeadCondition();
		// 现在我先直接把这个拼好的串放到StringBuffer中而不去优化拼串的过程
		sqlWhereBuf.append(strWhere);
		return true;
	}

}
package nc.ui.wds.w8006080206;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.SuperVO;
import nc.vo.wds.w8006080206.BdStordocVO;
import nc.vo.wds.w8006080206.TbStorcubasdocVO;

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
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		AggregatedValueObject mybillVO1 = getBillUI().getVOFromUI();
		BdStordocVO bdStordocVO = (BdStordocVO) mybillVO1.getParentVO();
		TbStorcubasdocVO[] tbStorcubasdocFormVOs = (TbStorcubasdocVO[]) mybillVO1
				.getChildrenVO();
		if (null != tbStorcubasdocFormVOs) {
			for (int i = 0; i < tbStorcubasdocFormVOs.length; i++) {
				String pk_cubasdoc = tbStorcubasdocFormVOs[i].getPk_cubasdoc();
				if (null == pk_cubasdoc || "".equals(pk_cubasdoc)) {
					getBillUI().showErrorMessage("客商编码不能为空！");
					return;
				}
				for (int j = i + 1; j < tbStorcubasdocFormVOs.length; j++) {
					String pk_cubasdocj = tbStorcubasdocFormVOs[j]
							.getPk_cubasdoc();
					if (null != pk_cubasdoc && null != pk_cubasdocj
							&& !"".equals(pk_cubasdoc)
							&& !"".equals(pk_cubasdocj)) {
						if (pk_cubasdoc.equals(pk_cubasdocj)) {
							getBillUI().showErrorMessage(
									"添加的客商已经和本分仓绑定，请您重新选择！");
							return;
						}
					}
				}
			}
		}

		// 除了本仓外其他仓库的绑定客商
		StringBuffer sql_srcbd = new StringBuffer();
		sql_srcbd.append(" dr=0 ");
		if (null != bdStordocVO && null != bdStordocVO.getPk_stordoc()) {
			sql_srcbd.append("and  pk_stordoc!='");
			sql_srcbd.append(bdStordocVO.getPk_stordoc());
			sql_srcbd.append("' ");
		}
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		ArrayList tbStorcubasdocVOs = (ArrayList) query.retrieveByClause(
				TbStorcubasdocVO.class, sql_srcbd.toString());
		AggregatedValueObject mybillVO = getBillUI().getChangedVOFromUI();
		TbStorcubasdocVO[] tbStorcubasdocChangeVOs = (TbStorcubasdocVO[]) mybillVO
				.getChildrenVO();
		if (null != tbStorcubasdocVOs && null != tbStorcubasdocChangeVOs) {
			for (int i = 0; i < tbStorcubasdocVOs.size(); i++) {
				TbStorcubasdocVO tbStorcubasdocVO = (TbStorcubasdocVO) tbStorcubasdocVOs
						.get(i);
				for (int j = 0; j < tbStorcubasdocChangeVOs.length; j++) {
					TbStorcubasdocVO tbStorcubasdocChangeVO = tbStorcubasdocChangeVOs[j];
					if (null != tbStorcubasdocVO.getPk_cubasdoc()
							&& null != tbStorcubasdocChangeVO.getPk_cubasdoc()) {
						if (tbStorcubasdocVO.getPk_cubasdoc().trim().equals(
								tbStorcubasdocChangeVO.getPk_cubasdoc().trim())) {
							getBillUI().showErrorMessage(
									"添加的客商已经和其它分仓绑定，请您先解除绑定关系！");
							return;
						}
					}
				}
			}
		}

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

}
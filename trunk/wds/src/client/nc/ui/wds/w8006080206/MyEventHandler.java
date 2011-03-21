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
 * ������AbstractMyEventHandler�������ʵ���࣬ ��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
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
					getBillUI().showErrorMessage("���̱��벻��Ϊ�գ�");
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
									"��ӵĿ����Ѿ��ͱ��ְֲ󶨣���������ѡ��");
							return;
						}
					}
				}
			}
		}

		// ���˱����������ֿ�İ󶨿���
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
									"��ӵĿ����Ѿ��������ְֲ󶨣������Ƚ���󶨹�ϵ��");
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
			return;// �û������˲�ѯ

		strWhere.append(" and def1='1' ");

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();

	}

}
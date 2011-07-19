package nc.ui.wds.dm.storebing;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wl.pub.WdsPubEnventHandler;
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


//		// ���˱����������ֿ�İ󶨿���
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
//									"��ӵĿ����Ѿ��������ְֲ󶨣������Ƚ���󶨹�ϵ��");
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
			return;// �û������˲�ѯ

		strWhere.append(" and def1='1' ");

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();

	}

}
package nc.ui.wds.w80021002;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.wds.w80021002.MyBillVO;
import nc.vo.wds.w80021002.TbHandlingfeepriceVO;
import nc.vo.wds.w80060406.TbFydnewVO;

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
		// ��øı�billvo
		MyBillVO billvo = (MyBillVO) getBillCardPanelWrapper()
				.getChangedVOFromUI();
		TbHandlingfeepriceVO fvo = (TbHandlingfeepriceVO) billvo.getParentVO();
		if (null != fvo) {
			fvo.validate();
		}
		// ���
		String hfp_specification = (String) getBillCardPanelWrapper()
				.getBillCardPanel().getHeadItem("hfp_specification")
				.getValueObject();
		// �ֿ�
		String pk_stordoc = (String) getBillCardPanelWrapper()
				.getBillCardPanel().getHeadItem("pk_stordoc").getValueObject();
		// ��ѯͬһ�ֿ����Ƿ�����ͬ���
		String sWhere = " hfp_specification='" + hfp_specification
				+ "' and pk_stordoc='" + pk_stordoc + "' and dr=0 ";

		if (null != getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"hfp_specification").getValueObject()) {
			// ����
			String hfp_pk = (String) getBillCardPanelWrapper()
					.getBillCardPanel().getHeadItem("hfp_pk").getValueObject();
			if (!"".equals(hfp_pk)) {
				sWhere += " and hfp_pk!='" + hfp_pk + "' ";
			}

		}
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());

		ArrayList os = (ArrayList) query.retrieveByClause(
				TbHandlingfeepriceVO.class, sWhere.toString());
		if (null != os && os.size() > 0) {
			getBillUI().showErrorMessage("ͬһ�ֿ���������ͬ���");
			return;
		}
		super.onBoSave();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (null == obj || "".equals(obj)) {
			return true;
		}
		return false;
	}

}
package nc.ui.wds.w80021008;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wds.w8000.CommonUnit;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.wds.w80021008.TbStockstaffVO;

/**
 * 
 * 仓库人员绑定
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	MyClientUI myClientUI = null;
	private String Pk = null;
	private boolean isAdd = false;

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;
	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		super.onBoAdd(bo);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("st_date")
				.setValue(_getDate());
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("st_pot")
				.setValue(
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey());
		Pk = null;
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub

		Object pk_stordoc = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("pk_stordoc").getValueObject();
		if (null == pk_stordoc || "".equals(pk_stordoc)) {
			myClientUI.showErrorMessage("请选择仓库信息");
			return;
		}
		Object pk_cargdoc = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("pk_cargdoc").getValueObject();
		if (null == pk_cargdoc || "".equals(pk_cargdoc)) {
			myClientUI.showErrorMessage("请选择货位信息");
			return;
		}
		Object cuserid = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("cuserid").getValueObject();
		if (null == cuserid || "".equals(cuserid)) {
			myClientUI.showErrorMessage("请选择人员信息");
			return;
		}
		// 获取人员类型
		Object temp = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadTailItem("crenyuanleixing").getValueObject();
		if (null != temp && !"".equals(temp)) {
			if (temp.toString().equals("3")) {
				if (CommonUnit.getSotckIsTotal(pk_stordoc.toString())) {
					myClientUI.showErrorMessage("操作失败,只有分仓有改人员类别");
					return;
				}
			}

			Integer type = Integer.parseInt(temp.toString());
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem("st_type",
					type);
		} else {
			myClientUI.showErrorMessage("请选择人员类型");
			return;
		}
		if (!isAdd || null == Pk || !Pk.equals(cuserid.toString())) {
			String strWhere = " dr = 0 and cuserid = '" + cuserid.toString()
					+ "'";

			IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			ArrayList list = (ArrayList) iuap.retrieveByClause(
					TbStockstaffVO.class, strWhere);
			if (null != list && list.size() > 0) {
				myClientUI.showErrorMessage("操作失败,该人员已经添加过记录");
				return;
			}
		}
		super.onBoSave();
		getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		super.onBoQuery();
		getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
	}

	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		super.onBoEdit();
		getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
		isAdd = true;
		Object a = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"cuserid").getValueObject();
		if (null != a && !"".equals(a)) {
			Pk = a.toString();
		}
	}

	@Override
	protected void onBoCard() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCard();
		getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
	}

	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCancel();
		getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
	}

	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
		getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();
	}

}
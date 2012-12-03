package nc.ui.wds.ic.cargtray;

import java.util.ArrayList;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.zmpub.pub.tool.LongTimeTask;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.wds.ic.cargtray.CargdocVO;
import nc.vo.wl.pub.LoginInforVO;

public class MyEventHandler extends ManageEventHandler {
	private LoginInforHelper helper = null;

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	protected UIDialog createQueryUI() {
		return new MyQueryDIG(getBillUI(), null, _getCorp().getPk_corp(),
				getBillUI().getModuleCode(), getBillUI()._getOperator(), null);
	}

	protected void onBoDelete() throws Exception {
		// 界面没有数据或者有数据但是没有选中任何行
		if (getBufferData().getCurrentVO() == null)
			return;
		valuteIndex1();
		super.onBoDelete();

	}

	/**
	 * 删除时 校验存货档案是否被引用
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目
	 * @时间：2012-9-22下午04:08:55
	 * @throws Exception
	 */
	private void valuteIndex1() throws Exception {

		AggregatedValueObject billVO =getBufferData().getCurrentVO();
		if (billVO == null || billVO.getParentVO() == null) {
			return;
		}
		Class[] ParameterTypes = new Class[] { AggregatedValueObject.class };
		Object[] ParameterValues = new Object[] { billVO };
		Object o = LongTimeTask.calllongTimeService("wds", null, "正在查询...", 1,
				"nc.vo.wdsnew.pub.BaseDocValuteTool", null, "valuteCdtDelete",
				ParameterTypes, ParameterValues);

	}

	// @Override
	// protected String getHeadCondition() {
	//		
	// // StringBuffer strWhere = new StringBuffer();
	// // String cargdocPK = null;
	// // try {
	// // cargdocPK = getCargdocPK(_getOperator());
	// // } catch (BusinessException e) {
	// // // TODO Auto-generated catch block
	// // e.printStackTrace();
	// // }
	// // strWhere.append(" pk_cargdoc='"+cargdocPK+"'");
	// return null;
	// }

	public LoginInforHelper getLoginInforHelper() {
		if (helper == null) {
			helper = new LoginInforHelper();
		}
		return helper;
	}

	@Override
	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
		String value = (String) getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("pk_cargdoc").getValueObject();
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable()
				.getSelectedRow();
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(value, row,
				"pk_cargdoc");// 货位
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("1", row,
				"cdt_traystatus");// 托盘状态默认占用

		// 托盘是否占用赋默认值

		int row1 = getBillCardPanelWrapper().getBillCardPanel().getBillTable()
				.getSelectedRow();
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(0, row,
				"cdt_traystatus");

	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {

		LoginInforVO login = getLoginInforHelper().getLogInfor(_getOperator());

		// add by zhw  增加仓库维度
		SuperVO[] vos = HYPubBO_Client.queryByCondition(CargdocVO.class,
				" wds_cargdoc.pk_cargdoc='" + login.getSpaceid()
						+ "' and isnull(wds_cargdoc.dr,0)=0 and pk_corp = '"
						+ _getCorp().getPrimaryKey() + "' and pk_stordoc = '"+login.getWhid()+"'");
		if (vos != null && vos.length > 0) {
			throw new Exception("该货位已经存在，请执行查询操作");
		}

		super.onBoAdd(bo);
	}

	/**
	 * 根据登录人员主键查询出对应的货位主键
	 * 
	 * @param pk
	 *            人员主键
	 * @return 仓库主键
	 * @throws BusinessException
	 */
	public static String getCargdocPK(String pk) throws BusinessException {
		String tmp = null;
		IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		String sql = "select pk_cargdoc from tb_stockstaff where dr = 0 and cuserid='"
				+ pk + "'";
		ArrayList list = (ArrayList) iuap.executeQuery(sql,
				new ArrayListProcessor());
		if (null != list && list.size() > 0) {
			Object[] a = (Object[]) list.get(0);
			if (null != a && a.length > 0 && null != a[0]) {
				tmp = a[0].toString();
			}
		}

		return tmp;
	}

	@Override
	protected void onBoSave() throws Exception {
		valuteIndex();
		super.onBoSave();
	}

	/**
	 * 保存时 校验货架是否被引用
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目
	 * @时间：2012-9-22下午04:14:47
	 * @throws Exception
	 */
	private void valuteIndex() throws Exception {
		AggregatedValueObject billVO = getBillUI().getChangedVOFromUI();
		if (billVO == null || billVO.getParentVO() == null) {
			return;
		}
		Class[] ParameterTypes = new Class[] { AggregatedValueObject.class };
		Object[] ParameterValues = new Object[] { billVO };
		Object o = LongTimeTask.calllongTimeService("wds", null, "正在查询...", 1,
				"nc.vo.wdsnew.pub.BaseDocValuteTool", null, "valuteCdtEdit",
				ParameterTypes, ParameterValues);

	}
}
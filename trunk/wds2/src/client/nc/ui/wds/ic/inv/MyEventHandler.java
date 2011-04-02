package nc.ui.wds.ic.inv;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.BusinessAction;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.businessaction.IBusinessController;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.wds.pub.WDSTools;
import nc.vo.wl.pub.CommonUnit;

/**
 * 
 * 存货状态
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	MyClientUI myClientUI = null;

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;
	}

	IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());
	IVOPersistence ivo = (IVOPersistence) NCLocator.getInstance().lookup(
			IVOPersistence.class.getName());

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub

		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		// 根据登录者查询对应货位
		String pk_cargdoc = CommonUnit.getCargDocName(ClientEnvironment
				.getInstance().getUser().getPrimaryKey());
		if (null == pk_cargdoc) {
			getBillUI().showErrorMessage("操作失败,当前用户没有进行人员绑定");
			return;
		}
		// 分组查询 所以后台手写语句并查询
		StringBuffer sql = new StringBuffer(
				" select pk_cargdoc,pk_invbasdoc,whs_batchcode,ss_pk from tb_warehousestock  "
						+ "where  dr = 0 and whs_status = 0 and pk_cargdoc = '"
						+ pk_cargdoc + "' ");

		// 如果有where条件 相加
		if (!"".equals(strWhere) && strWhere.length() > 0) {
			sql.append(" and " + strWhere);
		}

		sql.append(" group by pk_cargdoc,pk_invbasdoc ,whs_batchcode ,ss_pk");

		ArrayList list = (ArrayList) iuap.executeQuery(sql.toString(),
				new ArrayListProcessor());
		List numList = new ArrayList();
		SuperVO[] queryVos = null;
		if (null != list && list.size() > 0) {
			queryVos = new SuperVO[list.size()];
			// 把获取出来的值转换成数组
			for (int i = 0; i < list.size(); i++) {
				Object[] a = (Object[]) list.get(i);
				StockInvOnHandVO ware = new StockInvOnHandVO();
				ware.setPk_cargdoc(WDSTools.getString_TrimZeroLenAsNull(a[0])); // 货位主键
				ware
						.setPk_invbasdoc(WDSTools
								.getString_TrimZeroLenAsNull(a[1])); // 产品主键
				ware.setWhs_batchcode(WDSTools
						.getString_TrimZeroLenAsNull(a[2])); // 批次
				ware.setSs_pk(WDSTools.getString_TrimZeroLenAsNull(a[3])); // 状态

				if (null != a[2] && !"".equals(a[2]) && null != a[1]
						&& !"".equals(a[1])) {
					// 根据单品主键和批次查询出该单品的销售预警天数，如果当前日期减去批次大于或等于销售预警天数记录i
					sql = new StringBuffer(
							"select "
									+ " case"
									+ " when ((to_date((to_char(sysdate, 'yyyy-MM-dd')),'yyyy-MM-dd')) -"
									+ " (to_date(substr('"
									+ a[2]
									+ "', 0, 8),"
									+ " 'yyyy-MM-dd'))) >= bd_invbasdoc.def18 then "
									+ " 0 else 1"
									+ " end from bd_invbasdoc where pk_invbasdoc = '"
									+ a[1] + "'");
					ArrayList tmplist = (ArrayList) iuap.executeQuery(sql
							.toString(), new ArrayListProcessor());
					if (null != tmplist && tmplist.size() > 0) {
						a = (Object[]) tmplist.get(0);
						if (a[0].toString().equals("0")) {
							numList.add(i);
						}
					}

				}
				queryVos[i] = ware;
			}

		}
		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
		//变更颜色
		if (numList.size() > 0) {
			for (int i = 0; i < numList.size(); i++) {
				myClientUI.getBillListPanel().getParentListPanel()
						.setCellBackGround(
								Integer.parseInt(numList.get(i).toString()),
								"ccunhuobianma", Color.red);
			}
		}
	}

	protected IBusinessController createBusinessAction() {
		// TODO Auto-generated method stub
		switch (getUIController().getBusinessActionType()) {
		case IBusinessActionType.PLATFORM:
			return new BusinessAction(getBillUI());
		case IBusinessActionType.BD:
			return new W8004040602Action(getBillUI());
		default:
			return new BusinessAction(getBillUI());
		}
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub

		AggregatedValueObject billVO = getBillUI().getChangedVOFromUI();
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);
		// 进行数据晴空
		Object o = null;
		ISingleController sCtrl = null;
		if (getUIController() instanceof ISingleController) {
			sCtrl = (ISingleController) getUIController();
			if (sCtrl.isSingleDetail()) {
				o = billVO.getParentVO();
				billVO.setParentVO(null);
			} else {
				o = billVO.getChildrenVO();
				billVO.setChildrenVO(null);
			}
		}

		boolean isSave = true;

		// 判断是否有存盘数据
		if (billVO.getParentVO() == null
				&& (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0)) {
			isSave = false;
		} else {
			if (getBillUI().isSaveAndCommitTogether())
				billVO = getBusinessAction().saveAndCommit(billVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), checkVO);
			else

				// write to database
				billVO = getBusinessAction().save(billVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), checkVO);
		}

		// 进行数据恢复处理
		if (sCtrl != null) {
			if (sCtrl.isSingleDetail())
				billVO.setParentVO((CircularlyAccessibleValueObject) o);
		}
		int nCurrentRow = -1;
		if (isSave) {
			if (isEditing()) {
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(billVO);
					nCurrentRow = 0;

				} else {
					getBufferData().setCurrentVO(billVO);
					nCurrentRow = getBufferData().getCurrentRow();
				}
			}
			// 新增后操作处理
			setAddNewOperate(isAdding(), billVO);
		}
		// 设置保存后状态
		setSaveOperateState();
		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRow(nCurrentRow);
		}
	}
}
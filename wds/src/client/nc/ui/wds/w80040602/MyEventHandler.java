package nc.ui.wds.w80040602;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.wds.w80040602.TbSpacegoodsVO;
import nc.vo.wds.w8004061002.BdCargdocTrayVO;

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
		StringBuffer strWhere = new StringBuffer();
		strWhere
				.append(" pk_stordoc in (select pk_stordoc from bd_stordoc where def1='1' and dr=0) and sealflag='N' and ");
		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		strWhere.append(" order by pk_stordoc desc,cscode asc ");	
		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		// 获得表体行数
		int rowNum = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getRowCount();
		// 非空验证
		for (int i = 0; i < rowNum; i++) {
			String sgcode = (String) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getValueAt(i, "sgcode");
			String sgname = (String) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getValueAt(i, "sgname");
			if (null == sgcode || "".equals(sgcode)) {
				getBillUI().showErrorMessage("存货编码不能为空，请填写存货编码！");
				return;
			}
			if (null == sgname || "".equals(sgname)) {
				getBillUI().showErrorMessage("存货名称不能为空，请填写存货编码！");
				return;
			}
		}
		TbSpacegoodsVO[] tbSpacegoodsVO = null;
		try {
			if (null != getBillCardPanelWrapper().getBillVOFromUI()
					.getChildrenVO()) {
				tbSpacegoodsVO = (TbSpacegoodsVO[]) getBillCardPanelWrapper()
						.getBillVOFromUI().getChildrenVO();
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (null != tbSpacegoodsVO) {
			for (int i = 0; i < tbSpacegoodsVO.length; i++) {
				String pk_invbasdoc = tbSpacegoodsVO[i].getPk_invbasdoc();
				for (int j = i + 1; j < tbSpacegoodsVO.length; j++) {
					String pk_invbasdocj = tbSpacegoodsVO[j].getPk_invbasdoc();
					if (null != pk_invbasdoc && null != pk_invbasdocj
							&& !"".equals(pk_invbasdoc)
							&& !"".equals(pk_invbasdoc)) {
						if (pk_invbasdoc.equals(pk_invbasdocj)) {
							getBillUI().showErrorMessage("货物种类重复，请重新选择！");
							return;
						}
					}
				}
			}
		}

		AggregatedValueObject billVO = getBillUI().getChangedVOFromUI();
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);

		// IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
		// IUAPQueryBS.class.getName());
		// String pk_cargdoc = getBillCardPanelWrapper().getBillCardPanel()
		// .getHeadItem("pk_cargdoc").getValue();
		// String sgdssql = "";
		// if (null != pk_cargdoc) {
		// sgdssql = " pk_cargdoc='" + pk_cargdoc + "' and dr=0 ";
		// }
		//
		// ArrayList sfds = (ArrayList) query.retrieveByClause(
		// TbSpacegoodsVO.class, sgdssql);
		// if (null != sfds && sfds.size() > 0) {
		// for (int j = 0; j < sfds.size(); j++) {
		// TbSpacegoodsVO tbSpacegoodsVOl = (TbSpacegoodsVO) sfds.get(j);
		// boolean isdel = true;
		// for (int i = 0; i < billVO.getChildrenVO().length; i++) {
		// TbSpacegoodsVO tbSpacegoodsVOg = (TbSpacegoodsVO) billVO
		// .getChildrenVO()[i];
		// if (null != tbSpacegoodsVOl
		// && null != tbSpacegoodsVOl.getSg_pk()
		// & null != tbSpacegoodsVOg
		// && null != tbSpacegoodsVOg.getSg_pk()) {
		// if (tbSpacegoodsVOl.getSg_pk().equals(
		// tbSpacegoodsVOg.getSg_pk())) {
		// isdel = false;
		// }
		// }
		//
		// }
		// String sgdsql = "select count(*) from bd_cargdoc_tray where
		// pk_cargdoc='"
		// + tbSpacegoodsVOl.getPk_cargdoc()
		// + "' and cdt_invbasdoc='"
		// + tbSpacegoodsVOl.getPk_cargdoc() + "' and dr=0 ";
		// ArrayList sgd_count = (ArrayList) query.executeQuery(sgdsql,
		// new ArrayListProcessor());
		// int countnum = Integer
		// .parseInt((((Object[]) sgd_count.get(0))[0]).toString());
		// if (countnum != 0) {
		// getBillUI().showErrorMessage("货物已和托盘绑定，请先删除托盘！");
		// return;
		// }
		// }
		// }

		// 判断是否可以删除
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		if (null != billVO.getChildrenVO()) {
			for (int i = 0; i < billVO.getChildrenVO().length; i++) {
				TbSpacegoodsVO tbSpacegoodsVO1 = (TbSpacegoodsVO) billVO
						.getChildrenVO()[i];

				if (null != tbSpacegoodsVO1
						&& !"".equals(tbSpacegoodsVO1.getStatus())
						&& tbSpacegoodsVO1.getStatus() == 3) {

					String sgdsql = "select count(*) from bd_cargdoc_tray where pk_cargdoc='"
							+ tbSpacegoodsVO1.getPk_cargdoc()
							+ "' and cdt_invbasdoc='"
							+ tbSpacegoodsVO1.getPk_invbasdoc() + "' and dr=0 ";
					ArrayList sgd_count = (ArrayList) query.executeQuery(
							sgdsql, new ArrayListProcessor());
					int countnum = Integer.parseInt((((Object[]) sgd_count
							.get(0))[0]).toString());
					if (countnum != 0) {
						getBillUI().showErrorMessage("货物已和托盘绑定，请先删除托盘！");
						return;
					}
				}

			}
		}

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

	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		// 界面没有数据或者有数据但是没有选中任何行
		if (getBufferData().getCurrentVO() == null)
			return;

		if (MessageDialog.showOkCancelDlg(getBillUI(),
				nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
						"UPPuifactory-000064")/* @res "档案删除" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
						"UPPuifactory-000065")/* @res "是否确认删除该基本档案?" */
				, MessageDialog.ID_CANCEL) != UIDialog.ID_OK)
			return;

		AggregatedValueObject modelVo = getBufferData().getCurrentVO();
		// 判断是否可以删除
		// IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
		// IUAPQueryBS.class.getName());
		// for (int i = 0; i < modelVo.getChildrenVO().length; i++) {
		// TbSpacegoodsVO tbSpacegoodsVO = (TbSpacegoodsVO) modelVo
		// .getChildrenVO()[i];
		//
		// String sgdsql = "select count(*) from bd_cargdoc_tray where
		// pk_cargdoc='"
		// + tbSpacegoodsVO.getPk_cargdoc()
		// + "' and cdt_invbasdoc='"
		// + tbSpacegoodsVO.getPk_cargdoc() + "' and dr=0 ";
		// ArrayList sgd_count = (ArrayList) query.executeQuery(sgdsql,
		// new ArrayListProcessor());
		// int countnum = Integer
		// .parseInt((((Object[]) sgd_count.get(0))[0]).toString());
		// if (countnum != 0) {
		// getBillUI().showErrorMessage("货物已和托盘绑定，请先删除托盘！");
		// return;
		// }
		//
		// }
		// TbSpacegoodsVO
		getBusinessAction().delete(modelVo, getUIController().getBillType(),
				getBillUI()._getDate().toString(), getBillUI().getUserObject());
		if (PfUtilClient.isSuccess()) {
			// 清除界面数据
			getBillUI().removeListHeadData(getBufferData().getCurrentRow());
			if (getUIController() instanceof ISingleController) {
				ISingleController sctl = (ISingleController) getUIController();
				if (!sctl.isSingleDetail())
					getBufferData().removeCurrentRow();
			} else {
				getBufferData().removeCurrentRow();
			}

		}
		if (getBufferData().getVOBufferSize() == 0)
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
		else
			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
		getBufferData().setCurrentRow(getBufferData().getCurrentRow());

	}
}
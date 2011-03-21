package nc.ui.wds.w8004061002;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
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
		// 判断用户身份
		String st_type = "";
		try {
			st_type = nc.ui.wds.w8000.CommonUnit.getUserType(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if ("0".equals(st_type)) {
			// 货位
			String cargdocPK = getCargdocPK(ClientEnvironment.getInstance()
					.getUser().getPrimaryKey());
			strWhere.append(" pk_cargdoc='");
			strWhere.append(cargdocPK);
			strWhere.append("' and ");
		}
		if ("3".equals(st_type)) {
			// 货位
			String cargdocPK = getCargdocPK(ClientEnvironment.getInstance()
					.getUser().getPrimaryKey());
			strWhere.append(" pk_cargdoc='");
			strWhere.append(cargdocPK);
			strWhere.append("' and ");
		}

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		strWhere.append(" order by pk_stordoc desc,cscode asc ");
		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
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
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		super.onBoAdd(bo);

	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub

		// 获得表体行数
		int rowNum = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getRowCount();
		// 非空验证
		for (int i = 0; i < rowNum; i++) {
			String cdt_trayno = (String) getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel().getValueAt(i,
							"cdt_traycode");
			if (null == getBillCardPanelWrapper().getBillCardPanel()
					.getBillModel().getValueAt(i, "cdt_traystatus")) {
				getBillCardPanelWrapper().getBillCardPanel().getBillModel()
						.setValueAt(0, i, "cdt_traystatus");
			}
			if (null == cdt_trayno || "".equals(cdt_trayno)) {
				getBillUI().showErrorMessage("托盘位置编码不能为空，请填写存货编码！");
				return;
			}
		}
		BdCargdocTrayVO[] bdCargdocTrayVO = null;
		try {
			if (null != getBillCardPanelWrapper().getBillVOFromUI()
					.getChildrenVO()) {
				bdCargdocTrayVO = (BdCargdocTrayVO[]) getBillCardPanelWrapper()
						.getBillVOFromUI().getChildrenVO();
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (null != bdCargdocTrayVO) {
			for (int i = 0; i < bdCargdocTrayVO.length; i++) {
				String pk_invbasdoc = bdCargdocTrayVO[i].getCdt_traycode();
				for (int j = i + 1; j < bdCargdocTrayVO.length; j++) {
					String pk_invbasdocj = bdCargdocTrayVO[j].getCdt_traycode();
					if (null != pk_invbasdoc && null != pk_invbasdocj
							&& !"".equals(pk_invbasdoc)
							&& !"".equals(pk_invbasdoc)) {
						if (pk_invbasdoc.equals(pk_invbasdocj)) {
							getBillUI().showErrorMessage("托盘位置编码重复，请重新选择！");
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
		bdCargdocTrayVO = (BdCargdocTrayVO[]) billVO.getChildrenVO();
		if (null != bdCargdocTrayVO) {
			for (int i = 0; i < bdCargdocTrayVO.length; i++) {
				if (null != bdCargdocTrayVO[i].getCdt_traycode()) {
					((BdCargdocTrayVO[]) billVO.getChildrenVO())[i]
							.setCdt_customize8(bdCargdocTrayVO[i]
									.getCdt_traycode());
				}
			}
		}
		// 判断托盘是否在使用
		if (null != billVO.getChildrenVO() && billVO.getChildrenVO().length > 0) {
			for (int i = 0; i < billVO.getChildrenVO().length; i++) {
				BdCargdocTrayVO bdCargdocTrayVOd = ((BdCargdocTrayVO[]) (billVO
						.getChildrenVO()))[i];
				if (null != bdCargdocTrayVOd
						&& null != bdCargdocTrayVOd.getCdt_traystatus()) {
					if (bdCargdocTrayVOd.getCdt_traystatus() == 1) {
						getBillUI().showErrorMessage("托盘正在使用，不能删除或修改！");
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
}
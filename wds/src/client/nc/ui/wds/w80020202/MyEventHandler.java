package nc.ui.wds.w80020202;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pflock.VOConsistenceCheck;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.ui.pub.ClientEnvironment;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.BdBusinessAction;
import nc.ui.trade.businessaction.BusinessAction;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.businessaction.IBusinessController;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI; //import nc.ui.wds.w80060208.MyQueryTemplate;
//import nc.ui.wds.w8004040210.TrayAction;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.wds.w80020202.MultiBillVO;
import nc.vo.wds.w80020202.TbHandlecostsVO;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
import nc.vo.wds.w8004040210.TbGeneralHVO;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	MyClientUI myClientUI = null;

	protected IBusinessController createBusinessAction() {
		// TODO Auto-generated method stub
		switch (getUIController().getBusinessActionType()) {
		case IBusinessActionType.PLATFORM:
			return new BusinessAction(getBillUI());
		case IBusinessActionType.BD:
			return new W80020202Action(getBillUI());
		default:
			return new BusinessAction(getBillUI());
		}
	}

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;
		
	}

	@Override
	protected void onZxfzj() throws Exception {
		// TODO Auto-generated method stub

		MyQueryTemplate myQuery = new MyQueryTemplate(myClientUI);
		SCMQueryConditionDlg query = myQuery.getQueryDlg(ClientEnvironment
				.getInstance().getCorporation().getPrimaryKey(), "80020202",
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				"8002020202");

		if (query.showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
			// 获取查询条件
			ConditionVO[] voCons = query.getConditionVO();

			if (null != voCons && voCons.length > 0) {
				TbHandlecostsVO voForSave = queryDocuments(voCons);
			} else {

			}

		}
		showZeroLikeNull(false);
		// getBillUI().showErrorMessage("11");
	}

	// 有查询条件
	public TbHandlecostsVO queryDocuments(ConditionVO[] voCons)
			throws Exception {
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// 入库单查询语句
		String inSql = "";
		// 出库单查询语句
		String outSql = "";
		// 装卸单查询语句
		String handlingSql = " and dr=0 ";
		// 本地所有单据
		ArrayList hcVos = (ArrayList) query.retrieveByClause(
				TbHandlecostsVO.class, handlingSql);

		for (int i = 0; i < voCons.length; i++) {
			if (null != voCons[i].getOperaCode()) {
				if ("my.vbillcode".equals(voCons[i].getOperaCode())) {
					inSql = " ";
				}
			}
		}
		return null;
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub

		// 装卸人数
		if (null != getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"hc_mannum").getValueObject()) {
			String hc_mannum = (String) getBillCardPanelWrapper()
					.getBillCardPanel().getHeadItem("hc_mannum")
					.getValueObject();
			if ("".equals(hc_mannum)) {
				getBillUI().showErrorMessage("装卸人数不能为空！");
				return;
			}

		} else {
			getBillUI().showErrorMessage("装卸人数不能为空！");
			return;
		}

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
		getBillCardPanelWrapper().getBillCardPanel().execHeadLoadFormulas();

		super.onBoRefresh();
		// 显示0
		showZeroLikeNull(false);
	}

	public void showZeroLikeNull(boolean bShowZeroLikeNull) {
		BillRendererVO vo = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getRendererVO();
		vo.setShowZeroLikeNull(bShowZeroLikeNull);
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
				.setShowFlags(vo);
	}

	@Override
	protected void onBoPrint() throws Exception {
		// TODO Auto-generated method stub
		// String
		if (getBillManageUI().isListPanelSelected()) {

			nc.ui.pub.print.IDataSource dataSource = new MyListPanelPRTS(
					getBillUI()._getModuleCode(), ((BillManageUI) getBillUI())
							.getBillListPanel());
			nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(
					null, dataSource);
			print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(),
					getBillUI()._getModuleCode(), getBillUI()._getOperator(),
					getBillUI().getBusinessType(), getBillUI().getNodeKey());
			if (print.selectTemplate() == 1)
				print.preview();
		}
		// 如果是卡片界面，使用CardPanelPRTS数据源
		else {

			nc.ui.pub.print.IDataSource dataSource = new MyCardPanelPRTS(
					getBillUI()._getModuleCode(), getBillCardPanelWrapper()
							.getBillCardPanel());
			nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(
					null, dataSource);
			print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(),
					getBillUI()._getModuleCode(), getBillUI()._getOperator(),
					getBillUI().getBusinessType(), getBillUI().getNodeKey());
			if (print.selectTemplate() == 1)
				print.preview();

		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-1-9 8:37:34)
	 * 
	 * @return nc.ui.trade.pub.BillCardUI
	 */
	private BillManageUI getBillManageUI() {
		return (BillManageUI) getBillUI();
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// 入库单查询语句
		String inSql = "  dr=0 ";
		// 出库单查询语句
		String outSql = "  dr=0 ";
		// 装卸单查询语句
		String handlingSql = " dr=0 ";
		// 本地所有单据
		ArrayList hcVos = (ArrayList) query.retrieveByClause(
				TbHandlecostsVO.class, handlingSql);
		// 入库单单据
		ArrayList inVos = (ArrayList) query.retrieveByClause(
				TbGeneralHVO.class, inSql);
		// 出库单单据
		ArrayList outVos = (ArrayList) query.retrieveByClause(
				TbOutgeneralHVO.class, outSql);
		// 新增本地单据
		ArrayList hcNewVos = new ArrayList();
		// 添加本地单据
		// 入库单
		for (int i = 0; i < inVos.size(); i++) {
			TbHandlecostsVO tbHandlecostsVO = new TbHandlecostsVO();
			boolean isadd = true;
			if (null != inVos.get(i)) {
				TbGeneralHVO tbGeneralHVO = (TbGeneralHVO) inVos.get(i);
				if (null != tbGeneralHVO.getGeh_billcode()) {
					for (int j = 0; j < hcVos.size(); j++) {
						if (null != ((TbHandlecostsVO) hcVos.get(j))
								.getWd_document()) {
							if (((TbHandlecostsVO) hcVos.get(j))
									.getWd_document().equals(
											tbGeneralHVO.getGeh_billcode())) {
								isadd = false;
								break;
							}
						}
					}
					if (isadd) {
						tbHandlecostsVO.setCsourcebillhid(tbGeneralHVO
								.getGeh_pk());
						tbHandlecostsVO.setWd_document(tbGeneralHVO
								.getGeh_billcode());
						tbHandlecostsVO.setDbilldate(tbGeneralHVO
								.getGeh_dbilldate());
						tbHandlecostsVO.setDbilltype(0);
						tbHandlecostsVO.setHandtype(1);
						tbHandlecostsVO.setDr(0);
						hcNewVos.add(tbHandlecostsVO);
					}

				}
			}
		}
		// 出库单
		for (int i = 0; i < outVos.size(); i++) {
			TbHandlecostsVO tbHandlecostsVO = new TbHandlecostsVO();
			boolean isadd = true;
			if (null != outVos.get(i)) {
				TbOutgeneralHVO tbOutgeneralHVO = (TbOutgeneralHVO) outVos
						.get(i);
				if (null != tbOutgeneralHVO.getVbillcode()) {
					for (int j = 0; j < hcVos.size(); j++) {
						if (null != ((TbHandlecostsVO) hcVos.get(j))
								.getWd_document()) {
							if (((TbHandlecostsVO) hcVos.get(j))
									.getWd_document().equals(
											tbOutgeneralHVO.getVbillcode())) {
								isadd = false;
								break;
							}
						}
					}
					if (isadd) {
						tbHandlecostsVO.setCsourcebillhid(tbOutgeneralHVO
								.getGeneral_pk());
						tbHandlecostsVO.setWd_document(tbOutgeneralHVO
								.getVbillcode());
						tbHandlecostsVO.setDbilldate(tbOutgeneralHVO
								.getDbilldate());
						tbHandlecostsVO.setDbilltype(0);
						tbHandlecostsVO.setHandtype(0);
						tbHandlecostsVO.setDr(0);
						hcNewVos.add(tbHandlecostsVO);
					}

				}
			}
		}

		IVOPersistence perse = (IVOPersistence) NCLocator.getInstance().lookup(
				IVOPersistence.class.getName());
		if (null != hcNewVos && hcNewVos.size() > 0) {
			perse.insertVOList(hcNewVos);
		}

		super.onBoQuery();
		showZeroLikeNull(false);
	}

}
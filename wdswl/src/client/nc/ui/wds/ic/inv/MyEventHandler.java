package nc.ui.wds.ic.inv;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;


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



	@Override
	protected UIDialog createQueryUI() {
		
		return new MyQueryDIG(getBillUI() , null,getBillUI()._getCorp().getPrimaryKey(), getBillUI()._getModuleCode(),getBillUI()._getOperator(),null);
	}
	
	

//	IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
//			IUAPQueryBS.class.getName());
//	IVOPersistence ivo = (IVOPersistence) NCLocator.getInstance().lookup(
//			IVOPersistence.class.getName());

//	@Override
//	protected void onBoQuery() throws Exception {
//		
//
//		StringBuffer strWhere = new StringBuffer();
//
//		if (askForQueryCondition(strWhere) == false)
//			return;// 用户放弃了查询
//		// 根据登录者查询对应货位
//		String pk_cargdoc = CommonUnit.getCargDocName(ClientEnvironment
//				.getInstance().getUser().getPrimaryKey());
//		if (null == pk_cargdoc) {
//			getBillUI().showErrorMessage("操作失败,当前用户没有进行人员绑定");
//			return;
//		}
//
//		StringBuffer sql = new StringBuffer(
//				" select pk_cargdoc,pk_invbasdoc,whs_batchcode,sum(whs_stocktonnage) whs_stocktonnage," +
//				"sum(whs_stockpieces) whs_stockpieces,sum(whs_oanum) whs_oanum," +
//				"sum(whs_omnum) whs_omnum," +
//				"ss_pk from tb_warehousestock  "
//				+ "where  dr = 0 and whs_status = 0 and pk_cargdoc = '"
//				+ pk_cargdoc + "' ");
//
//		// 如果有where条件 相加
//		if (!"".equals(strWhere) && strWhere.length() > 0) {
//			sql.append(" and " + strWhere);
//		}
//
//		//分组查询
//		sql.append(" group by pk_cargdoc,pk_invbasdoc ,whs_batchcode ,ss_pk");
//
//
//		//获取查询
//		ArrayList listVOs=(ArrayList) iuap.executeQuery(sql.toString(), new BeanListProcessor(StockInvOnHandVO.class));
//
//
//		//用来记录超出预警天数的记录的对应行号
//
////		ArrayList waringdayrows=new ArrayList();
//		//用来记录所查询出的记录中哪一行的记录对应的货物超过预警天数 
////		if(listVOs !=null){
////			if(listVOs.size()>0){
//
//				SuperVO[] supers=new SuperVO[listVOs.size()];
//				for(int i=0;i<supers.length;i++){
//					supers[i]=(SuperVO)listVOs.get(i);
//
//				}
////				for(int i=0;i<supers.length;i++){
////					StockInvOnHandVO st=(StockInvOnHandVO)supers[i];
////					if (null != st.getWhs_batchcode() && ! "".equals((st.getWhs_batchcode()))&& null != st.getPk_cargdoc() && !"".equals((st.getPk_cargdoc())) ){
////
////						sql = new StringBuffer(
////								"select "
////								+ " case"
////								+ " when ((to_date((to_char(sysdate, 'yyyy-MM-dd')),'yyyy-MM-dd')) -"
////								+ " (to_date(substr('"
////								+ ((StockInvOnHandVO)supers[i]).getWhs_batchcode()
////								+ "', 0, 8),"
////								+ " 'yyyy-MM-dd'))) >= bd_invbasdoc.def18 then "
////								+ " 0 else 1"
////								+ " end from bd_invbasdoc where pk_invbasdoc = '"
////								+ ((StockInvOnHandVO)supers[i]).getPk_invbasdoc() + "'");
////						Object col = iuap.executeQuery(sql
////								.toString(), new ColumnProcessor());
////						if (null != col ) {
////							if(col.toString().equals("0"))
////								waringdayrows.add(i);
////						}
////					}
////				}	
//				getBufferData().clear();
//				// 增加数据到Buffer
//				addDataToBuffer(supers);
//				updateBuffer();
////				//变更颜色
////				if (waringdayrows.size() > 0) {
////					for (int i = 0; i < waringdayrows.size(); i++) {
////						myClientUI.getBillListPanel().getParentListPanel()
////						.setCellBackGround(
////								(Integer)waringdayrows.get(i),
////								"ccunhuobianma", Color.red);
////					}		
////				}
////			}
////		}
//	}
//
//	protected IBusinessController createBusinessAction() {
//		// TODO Auto-generated method stub
//		switch (getUIController().getBusinessActionType()) {
//		case IBusinessActionType.PLATFORM:
//			return new BusinessAction(getBillUI());
//		case IBusinessActionType.BD:
//			return new W8004040602Action(getBillUI());
//		default:
//			return new BusinessAction(getBillUI());
//		}
//	}
//
//	
//	
//	@Override
//	protected UIDialog createQueryUI() {
//		// TODO Auto-generated method stub
//		return new MyQueryDIG(
//				getBillUI(), null, 
//				
//				_getCorp().getPk_corp(), getBillUI().getModuleCode()
//				
//				, getBillUI()._getOperator(), null		
//		);
//	}
//
//	@Override
//	protected void onBoSave() throws Exception {
//		// TODO Auto-generated method stub
//
//		AggregatedValueObject billVO = getBillUI().getChangedVOFromUI();
//		setTSFormBufferToVO(billVO);
//		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
//		setTSFormBufferToVO(checkVO);
//		// 进行数据晴空
//		Object o = null;
//		ISingleController sCtrl = null;
//		if (getUIController() instanceof ISingleController) {
//			sCtrl = (ISingleController) getUIController();
//			if (sCtrl.isSingleDetail()) {
//				o = billVO.getParentVO();
//				billVO.setParentVO(null);
//			} else {
//				o = billVO.getChildrenVO();
//				billVO.setChildrenVO(null);
//			}
//		}
//
//		boolean isSave = true;
//
//		// 判断是否有存盘数据
//		if (billVO.getParentVO() == null
//				&& (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0)) {
//			isSave = false;
//		} else {
//			if (getBillUI().isSaveAndCommitTogether())
//				billVO = getBusinessAction().saveAndCommit(billVO,
//						getUIController().getBillType(), _getDate().toString(),
//						getBillUI().getUserObject(), checkVO);
//			else
//
//				// write to database
//				billVO = getBusinessAction().save(billVO,
//						getUIController().getBillType(), _getDate().toString(),
//						getBillUI().getUserObject(), checkVO);
//		}
//
//		// 进行数据恢复处理
//		if (sCtrl != null) {
//			if (sCtrl.isSingleDetail())
//				billVO.setParentVO((CircularlyAccessibleValueObject) o);
//		}
//		int nCurrentRow = -1;
//		if (isSave) {
//			if (isEditing()) {
//				if (getBufferData().isVOBufferEmpty()) {
//					getBufferData().addVOToBuffer(billVO);
//					nCurrentRow = 0;
//
//				} else {
//					getBufferData().setCurrentVO(billVO);
//					nCurrentRow = getBufferData().getCurrentRow();
//				}
//			}
//			// 新增后操作处理
//			setAddNewOperate(isAdding(), billVO);
//		}
//		// 设置保存后状态
//		setSaveOperateState();
//		if (nCurrentRow >= 0) {
//			getBufferData().setCurrentRow(nCurrentRow);
//		}
//	}
}
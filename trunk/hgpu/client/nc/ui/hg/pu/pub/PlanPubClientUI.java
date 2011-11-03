package nc.ui.hg.pu.pub;

import javax.swing.ListSelectionModel;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.po.ref.OtherRefModel;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.uif.pub.exception.UifException;
import nc.vo.hg.pu.plan.year.PlanYearBVO;
import nc.vo.hg.pu.pub.HgPuBtnConst;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.hg.pu.pub.PlanApplyInforVO;
import nc.vo.hg.pu.pub.PlanBVO;
import nc.vo.hg.pu.pub.PlanVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;

/**
 * 计划(支持自定义项)
 * 
 * @author zhw
 * 
 */
public abstract class PlanPubClientUI extends DefBillManageUI implements
		BillCardBeforeEditListener {

	public PlanPubClientUI() {
		super();
		initAppInfor();
	}

	public PlanApplyInforVO m_appInfor = null;// 系统集采公司

	private void initAppInfor() {
		try {
			m_appInfor = PlanPubHelper.getAppInfor(_getCorp().getPrimaryKey(),
					_getOperator());
		} catch (Exception e) {
			e.printStackTrace();// 获取申请信息失败 并不影响界面加载
			m_appInfor = null;
		}
	}

	private PlanPubEventHandler getEventHandler() {
		return (PlanPubEventHandler) getManageEventHandler();
	}

	@Override
	protected void initSelfData() {
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);// BillItemEvent
		// e
		// 捕捉到的BillItemEvent事件
		// 设置多选
		getBillListPanel().getHeadTable().setRowSelectionAllowed(true); // true那一行内容能够全部选中,false只能选中一个单元格
		// getBillListPanel().setParentMultiSelect(true);//设置表头多选,带复选框
		getBillListPanel().getHeadTable().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);// 往下拖拽，选中表头所有行

		// 除去行操作多余按钮
		ButtonObject btnobj = getButtonManager().getButton(IBillButton.Line);
		if (btnobj != null) {
			btnobj.removeChildButton(getButtonManager().getButton(
					IBillButton.CopyLine));
			btnobj.removeChildButton(getButtonManager().getButton(
					IBillButton.PasteLine));
			btnobj.removeChildButton(getButtonManager().getButton(
					IBillButton.InsLine));
			btnobj.removeChildButton(getButtonManager().getButton(
					IBillButton.PasteLinetoTail));
		}
		initRefPanel();// 初始化辅计量档案
	}

	private void initRefPanel() {
		BillData bd = getBillCardPanel().getBillData();
		if (bd.getBodyItem("castname") != null) {
			((UIRefPane) (bd.getBodyItem("castname").getComponent()))
					.setIsCustomDefined(true);
			((UIRefPane) (bd.getBodyItem("castname").getComponent()))
					.setRefModel(new OtherRefModel("辅计量单位"));
			((UIRefPane) (bd.getBodyItem("castname").getComponent()))
					.setReturnCode(false);
			((UIRefPane) (bd.getBodyItem("castname").getComponent()))
					.setRefInputType(UIRefPane.REFINPUTTYPE_CODE);
			((UIRefPane) (bd.getBodyItem("castname").getComponent()))
					.setCacheEnabled(false);
		}
	}

	@Override
	public void setDefaultData() throws Exception {
		setHeadItemValue("vbillstatus", IBillStatus.FREE);// 单据状态
		setTailItemValue("voperatorid", _getOperator());// 在表尾了
		setHeadItemValue("dbilldate", _getDate());
		setHeadItemValue("pk_corp", _getCorp().getPrimaryKey());
//		 setHeadItemValue("pk_billtype", HgPubConst.PLAN_YEAR_BILLTYPE);
		setTailItemValue("dmakedate", _getDate());
		// UFDate billdate = new UFDate(System.currentTimeMillis());
		// String year = PuPubVO
		// .getString_TrimZeroLenAsNull(billdate.getYear() + 1);
		// setHeadItemValue("cyear", year);// 年度赋值
		setHeadItemValue("caccperiodschemeid", HgPubTool
				.getStandAccperiodschemeID());// 会计期间
		
		setHeadItemValue("fisself", UFBoolean.TRUE);

		if (PuPubVO.getString_TrimZeroLenAsNull(m_appInfor.getCapplypsnid()) == null) {
			showWarningMessage("当前用户未设置关联业务员");
			return;
		}
	
		setHeadItemValue("capplypsnid", m_appInfor.getCapplypsnid());// 申请人
		setHeadItemValue("capplydeptid", m_appInfor.getCapplydeptid());// 部门
		setHeadItemValue("creqcalbodyid", m_appInfor.getCreqcalbodyid());// 申请组织
//		setHeadItemValue("csupplycorpid", m_appInfor.getCsupplycorpid());// 供货单位
//		setHeadItemValue("csupplydeptid", m_appInfor.getCsupplydeptid());// 供货部门
		String[] aryAssistunit = new String[] { "creqcalbodyid->getColValue(bd_deptdoc,pk_calbody,pk_deptdoc,capplydeptid)" };
		getBillCardPanel().execHeadFormulas(aryAssistunit);
		// setHeadItemValue("csupplydeptid",m_appInfor.getCsupplycalbodyid());//供货组织

	}
	
	public void setDefaultDataForCopy() throws Exception {
		setHeadItemValue("vbillstatus", IBillStatus.FREE);// 单据状态
		setTailItemValue("voperatorid", _getOperator());// 在表尾了
		setHeadItemValue("dbilldate", _getDate());
		setHeadItemValue("pk_corp", _getCorp().getPrimaryKey());
		setTailItemValue("dmakedate", _getDate());		
		setHeadItemValue("fisself", UFBoolean.TRUE);
	}

	protected void setHeadItemValue(String item, Object value) {
		getBillCardPanel().setHeadItem(item, value);
	}

	@Override
	public Object getUserObject() {
		// TODO Auto-generated method stub
		if (getBillOperate() == IBillOperate.OP_EDIT) {
			return getEventHandler().getOldNumMap();
		}
		return m_appInfor == null ? _getCorp().getPrimaryKey() : m_appInfor
				.getM_pocorp();
	}

	protected void setTailItemValue(String item, Object value) {
		getBillCardPanel().setTailItem(item, value);
	}

	// 添加自定义按钮
	public void initPrivateButton() {

		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnNo(HgPuBtnConst.FZGN);
		btnvo.setBtnName("辅助功能");
		btnvo.setBtnChinaName("辅助功能");
		btnvo.setBtnCode(null);// code最好设置为空
		btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });
		btnvo.setChildAry(new int[] { HgPuBtnConst.TZJXQ, HgPuBtnConst.CKJXQ });
		addPrivateButton(btnvo);

//		// 新物资申请
//		ButtonVO btnvo1 = new ButtonVO();
//		btnvo1.setBtnNo(HgPuBtnConst.XWZSQ);
//		btnvo1.setBtnName("新物资申请");
//		btnvo1.setBtnCode(null);// code最好设置为空
//		btnvo1.setOperateStatus(new int[] { IBillOperate.OP_EDIT,
//				IBillOperate.OP_ADD });
//		addPrivateButton(btnvo1);
//		// 调整净需求
		ButtonVO btnvo2 = new ButtonVO();
		btnvo2.setBtnNo(HgPuBtnConst.TZJXQ);
		btnvo2.setBtnName("调整净需求");
		btnvo2.setBtnChinaName("调整净需求");
		btnvo2.setBtnCode(null);// code最好设置为空
		btnvo2.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
				IBillOperate.OP_NO_ADDANDEDIT });
		btnvo2.setBusinessStatus(new int[]{IBillStatus.FREE});
		addPrivateButton(btnvo2);
		
		// 调整净需求
		ButtonVO btnvo3 = new ButtonVO();
		btnvo3.setBtnNo(HgPuBtnConst.CKJXQ);
		btnvo3.setBtnName("查看调整明细");
		btnvo3.setBtnChinaName("查看调整明细");
		btnvo3.setBtnCode(null);// code最好设置为空
		btnvo3.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
				IBillOperate.OP_NO_ADDANDEDIT });
		addPrivateButton(btnvo3);
		
		// 联查
		ButtonVO btnvo4 = new ButtonVO();
		btnvo4.setBtnNo(HgPuBtnConst.LINKQUERY);
		btnvo4.setBtnName("联查");
		btnvo4.setBtnChinaName("联查");
		btnvo4.setBtnCode(null);// code最好设置为空
		btnvo4.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
				IBillOperate.OP_NO_ADDANDEDIT });
		addPrivateButton(btnvo4);
		
		// 辅助查询
		ButtonVO btnvo5 = new ButtonVO();
		btnvo5.setBtnNo(HgPuBtnConst.ASSQUERY);
		btnvo5.setBtnName("辅助查询");
		btnvo5.setBtnChinaName("辅助查询");
		btnvo5.setBtnCode(null);// code最好设置为空
		btnvo5.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		btnvo5.setChildAry(new int[] { HgPuBtnConst.LINKQUERY,IBillButton.ApproveInfo});
		addPrivateButton(btnvo5);
		// 打印管理
		ButtonVO btnvo6 = new ButtonVO();
		btnvo6.setBtnNo(HgPuBtnConst.ASSPRINT);
		btnvo6.setBtnName("打印管理");
		btnvo6.setBtnChinaName("打印管理");
		btnvo6.setBtnCode(null);// code最好设置为空
		btnvo6.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		btnvo6.setChildAry(new int[] { IBillButton.Print,IBillButton.DirectPrint});
		addPrivateButton(btnvo6);

		super.initPrivateButton();
	}

	// @Override
	// protected ManageEventHandler createEventHandler() {
	// return new ClientEventHandler(this, getUIControl());
	// }

	// @Override
	// protected AbstractManageController createController() {
	// return new ClientController();
	// }

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {

	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {

	}

	// @Override
	// protected String getBillNo() throws Exception {
	// return HYPubBO_Client.getBillNo(HgPubConst.PLAN_YEAR_BILLTYPE,
	// _getCorp().getPrimaryKey(), null, null);
	// }

	// @Override
	// protected BusinessDelegator createBusinessDelegator() {
	// return new ClientBusinessDelegator(this);
	// }

	/**
	 * 表头编辑前
	 */
	public boolean beforeEdit(BillItemEvent e) {
		// TODO Auto-generated method stub
		String key = e.getItem().getKey();
		if ("creqcalbodyid".equalsIgnoreCase(key)) {// 需求组织
			String corpid = PuPubVO
					.getString_TrimZeroLenAsNull(getBillCardPanel()
							.getHeadItem("pk_corp").getValueObject());
			if (corpid == null) {
				e.getItem().setValue(null);
//				showWarningMessage("请录入公司");
				return false;
			}
			// return false;
			else {
				UIRefPane invRefPane = (UIRefPane) e.getItem().getComponent();
				AbstractRefModel refModel = (AbstractRefModel) invRefPane
						.getRefModel();
				refModel
						.setWherePart(" pk_corp = '"
								+ corpid
								+ "' and ( bd_calbody.sealflag='N' or bd_calbody.sealflag is null )");
			}
		} else if ("capplypsnid".equalsIgnoreCase(key)) {// 申请人
			String deptdoc = PuPubVO
					.getString_TrimZeroLenAsNull(getBillCardPanel()
							.getHeadItem("capplydeptid").getValueObject());
			UIRefPane invRefPane = (UIRefPane) e.getItem().getComponent();
			AbstractRefModel refModel = (AbstractRefModel) invRefPane
					.getRefModel();
			if (deptdoc == null) {
				refModel.addWherePart(" and 1=1");
				return true;
			}
			refModel.addWherePart("  and bd_psndoc.pk_deptdoc = '" + deptdoc
					+ "'");
			return true;
		} else if ("creqwarehouseid".equalsIgnoreCase(key)) {// 需求仓库
			String creqcalbodyid = PuPubVO
					.getString_TrimZeroLenAsNull(getBillCardPanel()
							.getHeadItem("creqcalbodyid").getValueObject());
			UIRefPane invRefPane = (UIRefPane) e.getItem().getComponent();
			AbstractRefModel refModel = (AbstractRefModel) invRefPane
					.getRefModel();
			if (creqcalbodyid == null) {
				refModel.addWherePart(" and 1=1");
				return true;
			}
			refModel.addWherePart("  and bd_stordoc.pk_calbody = '"
					+ creqcalbodyid + "'");
			return true;
		} else if ("csupplydeptid".equalsIgnoreCase(key)) {// 供货部门
			String csupplycorpid = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
							.getHeadItem("csupplycorpid").getValueObject());
			UIRefPane invRefPane = (UIRefPane) e.getItem().getComponent();
			AbstractRefModel refModel = (AbstractRefModel) invRefPane
					.getRefModel();
			
			if (csupplycorpid == null) {
				refModel.setPk_corp(_getCorp().getPrimaryKey());
				refModel.addWherePart(" and 1=1");
				return true;
			}
			refModel.setPk_corp(csupplycorpid);
		} else if ("capplydeptid".equalsIgnoreCase(key)) {// 申请部门
			String pk_corp = PuPubVO
					.getString_TrimZeroLenAsNull(getBillCardPanel()
							.getHeadItem("pk_corp").getValueObject());
			UIRefPane invRefPane = (UIRefPane) e.getItem().getComponent();
			AbstractRefModel refModel = (AbstractRefModel) invRefPane
					.getRefModel();
			if (pk_corp == null) {
				refModel.addWherePart(" and 1=1");
				return true;
			}
			refModel.addWherePart("  and bd_deptdoc.pk_corp = '" + pk_corp
					+ "'");
			return true;
		}
		return false;
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		int row = e.getRow();
		//提报的不可编辑
		if(getBillOperate()==IBillOperate.OP_EDIT){
			PlanVO head = (PlanVO)getBufferData().getCurrentVO().getParentVO();
			if(head == null)
				return true;
			if(!PuPubVO.getUFBoolean_NullAs(head.getFisself(), UFBoolean.TRUE).booleanValue())
				if(HgPubTool.getString_NullAsTrimZeroLen(getBillCardPanel().getBillModel().getValueAt(row,"irowstatus")).equals(HgPubConst.PLAN_VBILLSTATUS_SELT)){
					return false;
				}
		}
         
		if (getEventHandler().m_isAdjust) {// 调整净需求
			if (key.equalsIgnoreCase("nnetnum"))
				return true;
			else
				return false;
		}
		if (key.equalsIgnoreCase("nnetnum")){
			return false;
		}
		if ("invcode".equalsIgnoreCase(key)) {// 存货
			 String cinvclassid = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getHeadItem("cinvclassid").getValueObject());
			 String invcode =PuPubVO.getString_TrimZeroLenAsNull(HgPubTool.getInvclasscode(cinvclassid));//存货分类编码
	         UIRefPane invRefPane = (UIRefPane) getBillCardPanel().getBodyItem("invcode").getComponent();
	         AbstractRefModel refModel = (AbstractRefModel) invRefPane.getRefModel();
	         if(cinvclassid==null){
		            refModel.addWherePart("  and 1=1 ");
		         return true;
	           }
	         if(invcode !=null){
	  	        refModel.addWherePart(" and bd_invbasdoc.pk_invcl in (select pk_invcl from bd_invcl where invclasscode like '"+ invcode + "%')"); 
	         }
			return !PuPubVO.getUFBoolean_NullAs(getBillCardPanel().getBillModel().getValueAt(row, "bisuse"), UFBoolean.FALSE).booleanValue();
		}else if("cinvcode".equalsIgnoreCase(key)){
			 String cinvclassid = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
						.getHeadItem("cinvclassid").getValueObject());
				 String invcode =PuPubVO.getString_TrimZeroLenAsNull(HgPubTool.getInvclasscode(cinvclassid));//存货分类编码
		         UIRefPane invRefPane = (UIRefPane) getBillCardPanel().getBodyItem("cinvcode").getComponent();
		         AbstractRefModel refModel = (AbstractRefModel) invRefPane.getRefModel();
		         if(cinvclassid==null){
			            refModel.addWherePart("  and 1=1 ");
			         return true;
		           }
		         if(invcode !=null){
		  	        refModel.addWherePart(" and hg_new_materials.pk_invcl in (select pk_invcl from bd_invcl where invclasscode like '"+ invcode + "%')"); 
		         }
			return PuPubVO.getUFBoolean_NullAs(getBillCardPanel().getBillModel().getValueAt(row, "bisuse"), UFBoolean.FALSE).booleanValue();
		}else if ("castname".equalsIgnoreCase(key)) {// 辅计量
			// 存货ID
			String sBaseID = PuPubVO
					.getString_TrimZeroLenAsNull(getBillCardPanel()
							.getBillModel().getValueAt(e.getRow(),
									"pk_invbasdoc"));
			if (sBaseID == null)
				return false;
			if (!nc.ui.pu.pub.PuTool.isAssUnitManaged(sBaseID)) {
				return false;
			}
			setRefPaneAssistunit(e.getRow());
			return true;
		} else if ("nassistnum".equalsIgnoreCase(key)) {
			String sBaseID = PuPubVO
					.getString_TrimZeroLenAsNull(getBillCardPanel()
							.getBillModel().getValueAt(e.getRow(), "castname"));
			if (sBaseID == null)
				return false;
		} else if ("calname".equalsIgnoreCase(key)) {// 需求组织
			String pk_corp = PuPubVO
					.getString_TrimZeroLenAsNull(getBillCardPanel()
							.getHeadItem("pk_corp").getValueObject());
			UIRefPane invRefPane = (UIRefPane) getBillCardPanel().getBodyItem(
					"calname").getComponent();
			AbstractRefModel refModel = (AbstractRefModel) invRefPane
					.getRefModel();
			if (pk_corp == null) {
				refModel.addWherePart(" and 1=1");
				return true;
			}
			refModel.addWherePart("  and bd_calbody.pk_corp = '" + pk_corp
					+ "'");
			return true;
		} else if ("rename".equalsIgnoreCase(key)) {// 需求仓库
			String creqcalbodyid = PuPubVO
			.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBodyValueAt(row, "creqcalbodyid"));
			UIRefPane invRefPane = (UIRefPane) getBillCardPanel().getBodyItem(
					"rename").getComponent();
			AbstractRefModel refModel = (AbstractRefModel) invRefPane
					.getRefModel();
			if (creqcalbodyid == null) {
				refModel.addWherePart(" and 1=1");
				return true;
			}
			refModel.addWherePart("  and  bd_stordoc.pk_calbody = '" + creqcalbodyid
					+ "'");
			return true;
		}	else if ("sucalname".equalsIgnoreCase(key)) {// 供货组织
			String csupplycorpid = PuPubVO
			.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getHeadItem("csupplycorpid").getValueObject());
			UIRefPane invRefPane = (UIRefPane) getBillCardPanel().getBodyItem(
					"sucalname").getComponent();
			AbstractRefModel refModel = (AbstractRefModel) invRefPane
					.getRefModel();
			if (csupplycorpid == null) {
				showErrorMessage("请先选择表头供货单位");
				return false;
			}
			 refModel.setPk_corp(csupplycorpid);
			return true;
		} 
		else if ("suware".equalsIgnoreCase(key)) {// 供货仓库
			String csupplycorpid = PuPubVO
			.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getHeadItem("csupplycorpid").getValueObject());
			String csupplycalbodyid = PuPubVO
					.getString_TrimZeroLenAsNull(getBillCardPanel()
							.getBodyValueAt(row, "csupplycalbodyid"));
			UIRefPane invRefPane = (UIRefPane) getBillCardPanel().getBodyItem(
					"suware").getComponent();
			AbstractRefModel refModel = (AbstractRefModel) invRefPane
					.getRefModel();
			if (csupplycalbodyid == null){
				if(csupplycorpid!=null)
				 refModel.setPk_corp(csupplycorpid);
				refModel.addWherePart(" and 1=1");
				return true;
			}
			if(csupplycorpid!=null)
				 refModel.setPk_corp(csupplycorpid);
			 refModel.setPk_corp(csupplycorpid);
			refModel.addWherePart("  and  bd_stordoc.pk_calbody = '" + csupplycalbodyid
					+ "'");
			return true;
		}
		return true;
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		String key = e.getKey();
		int rowCount= getBillCardPanel().getBillTable().getRowCount();
		if (e.getPos() == BillItem.HEAD) {
			if ("creqcalbodyid".equalsIgnoreCase(key)) {// 需求组织
				int row = getBillCardPanel().getRowCount();
				String creqcalbodyid = PuPubVO
				.getString_TrimZeroLenAsNull(getHeadItemValue("creqcalbodyid"));// 需求组织
				setBodyCelValue(row, "creqcalbodyid", creqcalbodyid);
				getBillCardPanel().getHeadItem("creqwarehouseid")
				.setValue(null);
				getBillCardPanel().getBillModel().execLoadFormulaByKey(
				"creqcalbodyid");
				if(creqcalbodyid==null){
					setBodyCelValue(rowCount,"creqcalbodyid",null);
					setBodyCelValue(rowCount,"calname",null);
					setBodyCelValue(rowCount,"creqwarehouseid",null);
					setBodyCelValue(rowCount,"rename",null);
				}
			} else if ("creqwarehouseid".equals(key)) { // 需求仓库
				int row = getBillCardPanel().getRowCount();
				String pk_channelpsn = PuPubVO
				.getString_TrimZeroLenAsNull(getHeadItemValue("creqwarehouseid"));// 需求仓库
				setBodyCelValue(row, "creqwarehouseid", pk_channelpsn);

				String[] aryAssistunit = new String[] { "creqcalbodyid->getColValue(bd_stordoc,pk_calbody,pk_stordoc,creqwarehouseid)" };
				getBillCardPanel().execHeadFormulas(aryAssistunit);
				String creqcalbodyid = PuPubVO.getString_TrimZeroLenAsNull(getHeadItemValue("creqcalbodyid"));// 需求组织
				setBodyCelValue(row, "creqcalbodyid", creqcalbodyid);
				getBillCardPanel().getBillModel().execLoadFormulaByKey("creqcalbodyid");
				getBillCardPanel().getBillModel().execLoadFormulaByKey("creqwarehouseid");
			} 
//			else if ("capplydeptid".equalsIgnoreCase(key)) {// 申请部门
//				String[] aryAssistunit = new String[] { "creqcalbodyid->getColValue(bd_deptdoc,pk_calbody,pk_deptdoc,capplydeptid)" };
//				getBillCardPanel().execHeadFormulas(aryAssistunit);
//				getBillCardPanel().getHeadItem("capplypsnid").setValue(null);
//				getBillCardPanel().getHeadItem("creqwarehouseid").setValue(null);
//				int row = getBillCardPanel().getRowCount();
//				String creqcalbodyid = PuPubVO
//				.getString_TrimZeroLenAsNull(getHeadItemValue("creqcalbodyid"));// 需求组织
//				setBodyCelValue(row, "creqcalbodyid", creqcalbodyid);
//				if(creqcalbodyid==null){
//					setBodyCelValue(rowCount,"creqcalbodyid",null);
//					setBodyCelValue(rowCount,"calname",null);
//				}
//				setBodyCelValue(rowCount,"creqwarehouseid",null);
//				setBodyCelValue(rowCount,"rename",null);
//				getBillCardPanel().getBillModel().execLoadFormulaByKey(
//				"creqcalbodyid");
//			} 
			else if ("capplypsnid".equalsIgnoreCase(key)) {// 申请人
				getBillCardPanel().execHeadTailEditFormulas(
						getBillCardPanel().getHeadItem("capplypsnid"));
			} else if ("csupplycorpid".equalsIgnoreCase(key)) {// 供货单位
				String csupplycorpid = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("csupplycorpid").getValue());
				getBillCardPanel().getHeadItem("csupplydeptid").setValue(null);
				Object supplycalbodyid = null;
				Object supplycalbodyname = null;
				try {
                    supplycalbodyid = HYPubBO_Client.findColValue("bd_calbody","pk_calbody","isnull(dr,0)=0 and pk_corp ='"+csupplycorpid+"'");
                    supplycalbodyname= HYPubBO_Client.findColValue("bd_calbody","bodyname"," isnull(dr,0)=0 and pk_calbody ='"+supplycalbodyid+"'");
                } catch (UifException e1) {
                    showHintMessage(e1.getMessage());
                    supplycalbodyid = null;
                    supplycalbodyname = null;
                }
				
//				String supplycalbodyid =PuPubVO.getString_TrimZeroLenAsNull(m_appInfor==null?null:m_appInfor.getCsupplycalbodyid());//供货组织
//				setBodyCelValue(rowCount,"csupplycalbodyid",supplycalbodyid);
				if(csupplycorpid == null){
					setBodyCelValue(rowCount,"csupplycalbodyid",null);
					setBodyCelValue(rowCount,"sucalname",null);
				}
				setBodyCelValue(rowCount,"suware",null);
				setBodyCelValue(rowCount,"csupplywarehouseid",null);
				setBodyCelValue(rowCount,"csupplycalbodyid",supplycalbodyid);
                setBodyCelValue(rowCount,"sucalname",supplycalbodyname);
//			    getBillCardPanel().setBodyValueAt(supplycalbodyid,e.getRow(),"csupplycalbodyid");
				getBillCardPanel().getBillModel().execLoadFormulaByKey("csupplycalbodyid");
			} else if ("csupplydeptid".equalsIgnoreCase(key)) {// 供货部门
//				String pk_corp =PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("csupplycorpid").getValue());
//				if(pk_corp == null){
				getBillCardPanel().execHeadTailEditFormulas(
				        getBillCardPanel().getHeadItem("csupplydeptid"));
//				}else{
//					getBillCardPanel().getHeadItem("csupplycorpid").setValue(pk_corp);
//				}
			} 
			else if ("cinvclassid".equalsIgnoreCase(key)) {// 存货分类
				String[] aryAssistunit = new String[] { "invclcode->getColValue(bd_invcl, invclasscode,pk_invcl,cinvclassid)" };
				getBillCardPanel().execHeadFormulas(aryAssistunit);
				String[] tablecodes = new String[] { "hg_planyear_b" };
				clearTable(tablecodes);
			}
		} else if (e.getPos() == BillItem.BODY) {
			int bodyRow = e.getRow();
			if("bisuse".equalsIgnoreCase(key)){
				afterEditWhenIsUsed(e, bodyRow);
				
			}else if ("invcode".equalsIgnoreCase(key)|| "cinvcoe".equalsIgnoreCase(key)) {
				// 存货
				afterEditWhenInv(e, bodyRow);
			} else if ("castname".equalsIgnoreCase(key)) {
				// 辅计量
				afterEditWhenAssistUnit(e);
			} else if ("nassistnum".equalsIgnoreCase(key)|| "nnum".equalsIgnoreCase(key)) {
				UFBoolean isfixed = PuPubVO.getUFBoolean_NullAs(getBillCardPanel().getBillModel().getValueAt(
						e.getRow(), "fixedflag"), UFBoolean.TRUE);
				if (isfixed.booleanValue())
					HgPubTool.m_saKey[0] = "Y";
				else
					HgPubTool.m_saKey[0] = "N";
				RelationsCal.calculate(getBillCardPanel(), e,getBillCardPanel().getBillModel(), HgPubTool.m_iDescriptions,HgPubTool.m_saKey, PlanBVO.class.getName());
				getBillCardPanel().getBillModel().execEditFormulaByKey(e.getRow(),"nnum");
			}else if ("calname".equalsIgnoreCase(key)) {// 需求组织
				getBillCardPanel().setBodyValueAt(null, bodyRow,
				"creqwarehouseid");
				getBillCardPanel().setBodyValueAt(null, bodyRow, "rename");
			} else if ("rename".equals(key)) { // 需求仓库
				String[] aryAssistunit = new String[] {
						"creqcalbodyid->getColValue(bd_stordoc,pk_calbody,pk_stordoc,creqwarehouseid)",
				"calname->getColValue(bd_calbody,bodyname,pk_calbody,creqcalbodyid)" };
				getBillCardPanel().getBillModel().execFormulas(e.getRow(),
						aryAssistunit);
			}else if ("sucalname".equalsIgnoreCase(key)) {// 供货组织
				getBillCardPanel().setBodyValueAt(null, bodyRow,"csupplywarehouseid");
				getBillCardPanel().setBodyValueAt(null, bodyRow, "suware");
			}else if ("suware".equals(key)) { // 供货仓库
				String[] aryAssistunit = new String[] {
						"csupplycalbodyid->getColValue(bd_stordoc,pk_calbody,pk_stordoc,csupplywarehouseid)",
				"sucalname->getColValue(bd_calbody,bodyname,pk_calbody,csupplycalbodyid)" };
				getBillCardPanel().getBillModel().execFormulas(e.getRow(),
						aryAssistunit);
			}
		}
		super.afterEdit(e);
	}

	private void afterEditWhenIsUsed(BillEditEvent e, int row){
		//如果是临时物资 参照为临时物资参照  如果不是  为存货档案参照
		boolean isuse = PuPubVO.getUFBoolean_NullAs(getBillCardPanel().getBillModel().getValueAt(row, "bisuse"), UFBoolean.FALSE).booleanValue();
		String invid;
		BillEditEvent e2 = null;
		if(isuse){//清空数据
			invid = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc"));
			if(invid != null){
				setBodyRowValue(row, "pk_invbasdoc", null);
				setBodyRowValue(row, "invcode", null);
				e2 = new BillEditEvent(getBillCardPanel().getBodyItem("invcode"),null,null,"invcode",row,BillItem.BODY);
				afterEditWhenInv(e2, row);
			}
		}else{
			invid = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(row, "vreserve1"));
			if(invid != null){
				setBodyRowValue(row, "vreserve1", null);
				setBodyRowValue(row, "cinvcode", null);
				e2 = new BillEditEvent(getBillCardPanel().getBodyItem("cinvcode"),null,null,"cinvcode",row,BillItem.BODY);
				afterEditWhenInv(e2, row);
			}
		}
	}
	
//	private AbstractRefModel  abs =null;
//	private AbstractRefModel getAbstractRefModel(){//存货档案MODEL
//		if(abs == null){
//			abs = new UIRefPane("存货档案").getRefModel();
//		}
//		return abs;
//	}
//	
//	private InvbasdocRefModel  inv= null;
//	private InvbasdocRefModel getInvbasdocRefModel(){//临时物资参照MODEL
//		if(inv == null){
//			inv = new InvbasdocRefModel();
//		}
//		return inv;
//	}
	private void setBodyCelValue(int rowCount, String filedname, Object oValue) {
		for (int i = 0; i < rowCount; i++) {
			getBillCardPanel().setBodyValueAt(oValue, i, filedname);
		}
	}

	private void setBodyRowValue(int row, String filedname, Object oValue) {
		getBillCardPanel().setBodyValueAt(oValue, row, filedname);
	}

	private Object getHeadItemValue(String sitemname) {
		return getBillCardPanel().getHeadItem(sitemname).getValueObject();
	}


	/**
	 * 保存即提交
	 */
//	@Override
//	public boolean isSaveAndCommitTogether() {
//		return false;
//	}

	/**
	 * @说明 根据表体 tableCode,清空页签数据
	 * @时间 2010-9-14下午02:06:02
	 * @param tableCodes
	 */
	protected void clearTable(String[] tableCodes) {
		if (tableCodes != null && tableCodes.length > 0) {
			for (int i = 0; i < tableCodes.length; i++) {
			    if(getBillCardPanel().getBillModel(tableCodes[i])==null)
			        return;
				int count = getBillCardPanel().getBillModel(tableCodes[i])
						.getRowCount();
				int[] array = new int[count];
				for (int j = 0; j < count; j++) {
					array[j] = j;
				}
				getBillCardPanel().getBillData().getBillModel(tableCodes[i])
						.delLine(array);
			}
		}
	}

	private void setRefPaneAssistunit(int row) {
		// 存货基本ID与主计量ID
		String cbaseid = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
				.getBillModel().getValueAt(row, "pk_invbasdoc"));
		if (cbaseid != null && nc.ui.pu.pub.PuTool.isAssUnitManaged(cbaseid)) {

			// 设置辅计量单位参照
			UIRefPane ref = (UIRefPane) getBillCardPanel().getBodyItem(
					"castname").getComponent();
			String wherePart = "bd_convert.pk_invbasdoc='" + cbaseid + "' ";
			ref.setWhereString(wherePart);
			String unionPart = " union all \n";
			unionPart += "(select bd_measdoc.shortname,bd_measdoc.measname,bd_invbasdoc.pk_measdoc \n";
			unionPart += "from bd_invbasdoc \n";
			unionPart += "left join bd_measdoc  \n";
			unionPart += "on bd_invbasdoc.pk_measdoc=bd_measdoc.pk_measdoc \n";
			unionPart += "where bd_invbasdoc.pk_invbasdoc='" + cbaseid
					+ "') \n";
			ref.getRefModel().setGroupPart(unionPart);
		}
	}

	/**
	 * 辅计量编辑事件 处理： # 辅计量主键编辑时触发处理 ==>> 换算率 | # 选取的计量ID是主计量ID：变换率置为1，固定换算率 | #
	 * 由“换算率”驱动公式计算 > 步完成均处理合格数量是否可编辑 # 同步更新单据模板中的换算率和是否固定换算率属性 | # 更新换算率属性列可编辑性
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	private void afterEditWhenAssistUnit(BillEditEvent e) {
		// 是否固定变化率改变时要同步更改：strKeys[0]的值
		boolean isfixed = true;
		UFDouble convert = new UFDouble(0);
		// 存货ID
		String sBaseID = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
				.getBillModel().getValueAt(e.getRow(), "pk_invbasdoc"));
		// 辅计量主键
		String sCassId = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
				.getBillModel().getValueAt(e.getRow(), "castunitid"));

		if (sCassId == null) {
			getBillCardPanel().setCellEditable(e.getRow(), "hsl", false);
			getBillCardPanel().setCellEditable(e.getRow(), "nassistnum", false);
			getBillCardPanel().setCellEditable(e.getRow(), "castname", false);
			getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
					"hsl");
			getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
					"nassistnum");
			getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
					"castname");
			getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
					"castunitid");
			return;
		}
		// 获取换算率
		convert = nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID, sCassId);
		// 是否固定换算率
		isfixed = PuTool.isFixedConvertRate(sBaseID, sCassId);
		// 设置辅信息 到界面
		getBillCardPanel().setBodyValueAt(isfixed, e.getRow(), "fixedflag");// 是否固定换算率
		getBillCardPanel().getBillModel()
				.setValueAt(convert, e.getRow(), "hsl");
		if (isfixed)
			HgPubTool.m_saKey[0] = "Y";
		else
			HgPubTool.m_saKey[0] = "N";
		// //用换算率驱动计算：到货数量，辅数量，合格数量，不合格数量，单价，金额
		e.setKey("hsl");
		RelationsCal.calculate(getBillCardPanel(), e, getBillCardPanel()
				.getBillModel(), "hsl", HgPubTool.m_iDescriptions,
				HgPubTool.m_saKey, PlanYearBVO.class.getName());
	}

	private void afterEditWhenInv(BillEditEvent e, int row) {
		String invbasid;
		if(e.getKey().equalsIgnoreCase("invcode")){
			invbasid = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(e.getRow(), "pk_invbasdoc"));			
		}else{
			invbasid = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(e.getRow(), "vreserve1"));
		}
			clearBodyItem(row,invbasid);
	}

	
////	@Override
//	public void afterUpdate() {
//		// TODO Auto-generated method stub
//		String[] formul = new String[]{
//				
//				
//
//				"invname->\"aaaa\""
////				"invtype->iif(bisuse==\"Y\",getColValue(hg_new_materials,vinvtype,pk_materials,vreserve1),getColValue(bd_invbasdoc,invtype,pk_invbasdoc, pk_invbasdoc))",
////				"invspec->iif(bisuse==\"Y\",getColValue(hg_new_materials,vinvspec,pk_materials,vreserve1),getColValue(bd_invbasdoc,invspec,pk_invbasdoc,pk_invbasdoc))"
//};
//		
//		getBillCardPanel().getBillModel("hg_planyear_b").execFormulas(formul);
//		super.afterUpdate();
//	}

	@Override
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		return null;
	}

	// 存货编辑后 清空该行元素
	private void clearBodyItem(int row, String invbasdoc) {
		
//		if(invbasdoc == null){
//			setBodyRowValue(row, "vreserve1", null);//临时存货ID
//			setBodyRowValue(row, "pk_invbasdoc", null);//存货基本ID
//			setBodyRowValue(row, "cinventoryid", null);//存货管理ID
//			setBodyRowValue(row, "invname", null);//名称
//			setBodyRowValue(row, "invtype", null);//型号
//			setBodyRowValue(row, "cinvcode", null);//临时存货编码
//			setBodyRowValue(row, "invspec", null);//型号
//			setBodyRowValue(row, "nprice", null);//价格
//			setBodyRowValue(row, "castunitid", null);//辅单位主键
//			setBodyRowValue(row, "hsl", null);//换算率
//			setBodyRowValue(row, "fixedflag", null);//是否辅计量管理
//			setBodyRowValue(row, "castname", null);//辅单位名称
//		}
		
		setBodyRowValue(row, "vbatchcode", null);//批号
		setBodyRowValue(row, "nmny", null);//金额
		
		for (int i = 0; i < 12; i++) {
			setBodyRowValue(row, HgPubConst.NMONTHNUM[i], null);//12月分量
		}
		setBodyRowValue(row, "nnetnum", null);//净需求
		setBodyRowValue(row, "nnum", null);//毛需求
		setBodyRowValue(row, "nassistnum", null);//辅数量
		setBodyRowValue(row, "nallusenum", null);//上年度消耗
		setBodyRowValue(row, "nnonum", null);//库存不合用
		setBodyRowValue(row, "nreoutnum", null);//余月预耗
		setBodyRowValue(row, "nreinnum", null);//剩余资源
		setBodyRowValue(row, "nusenum", null);//当前库存
		setBodyRowValue(row, "nonhandnum", null);//1-N月消耗
		setBodyRowValue(row, "nsafestocknum", null);//安全库存
	}
	
}

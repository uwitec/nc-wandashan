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
 * �ƻ�(֧���Զ�����)
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

	public PlanApplyInforVO m_appInfor = null;// ϵͳ���ɹ�˾

	private void initAppInfor() {
		try {
			m_appInfor = PlanPubHelper.getAppInfor(_getCorp().getPrimaryKey(),
					_getOperator());
		} catch (Exception e) {
			e.printStackTrace();// ��ȡ������Ϣʧ�� ����Ӱ��������
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
		// ��׽����BillItemEvent�¼�
		// ���ö�ѡ
		getBillListPanel().getHeadTable().setRowSelectionAllowed(true); // true��һ�������ܹ�ȫ��ѡ��,falseֻ��ѡ��һ����Ԫ��
		// getBillListPanel().setParentMultiSelect(true);//���ñ�ͷ��ѡ,����ѡ��
		getBillListPanel().getHeadTable().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);// ������ק��ѡ�б�ͷ������

		// ��ȥ�в������ఴť
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
		initRefPanel();// ��ʼ������������
	}

	private void initRefPanel() {
		BillData bd = getBillCardPanel().getBillData();
		if (bd.getBodyItem("castname") != null) {
			((UIRefPane) (bd.getBodyItem("castname").getComponent()))
					.setIsCustomDefined(true);
			((UIRefPane) (bd.getBodyItem("castname").getComponent()))
					.setRefModel(new OtherRefModel("��������λ"));
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
		setHeadItemValue("vbillstatus", IBillStatus.FREE);// ����״̬
		setTailItemValue("voperatorid", _getOperator());// �ڱ�β��
		setHeadItemValue("dbilldate", _getDate());
		setHeadItemValue("pk_corp", _getCorp().getPrimaryKey());
//		 setHeadItemValue("pk_billtype", HgPubConst.PLAN_YEAR_BILLTYPE);
		setTailItemValue("dmakedate", _getDate());
		// UFDate billdate = new UFDate(System.currentTimeMillis());
		// String year = PuPubVO
		// .getString_TrimZeroLenAsNull(billdate.getYear() + 1);
		// setHeadItemValue("cyear", year);// ��ȸ�ֵ
		setHeadItemValue("caccperiodschemeid", HgPubTool
				.getStandAccperiodschemeID());// ����ڼ�
		
		setHeadItemValue("fisself", UFBoolean.TRUE);

		if (PuPubVO.getString_TrimZeroLenAsNull(m_appInfor.getCapplypsnid()) == null) {
			showWarningMessage("��ǰ�û�δ���ù���ҵ��Ա");
			return;
		}
	
		setHeadItemValue("capplypsnid", m_appInfor.getCapplypsnid());// ������
		setHeadItemValue("capplydeptid", m_appInfor.getCapplydeptid());// ����
		setHeadItemValue("creqcalbodyid", m_appInfor.getCreqcalbodyid());// ������֯
//		setHeadItemValue("csupplycorpid", m_appInfor.getCsupplycorpid());// ������λ
//		setHeadItemValue("csupplydeptid", m_appInfor.getCsupplydeptid());// ��������
		String[] aryAssistunit = new String[] { "creqcalbodyid->getColValue(bd_deptdoc,pk_calbody,pk_deptdoc,capplydeptid)" };
		getBillCardPanel().execHeadFormulas(aryAssistunit);
		// setHeadItemValue("csupplydeptid",m_appInfor.getCsupplycalbodyid());//������֯

	}
	
	public void setDefaultDataForCopy() throws Exception {
		setHeadItemValue("vbillstatus", IBillStatus.FREE);// ����״̬
		setTailItemValue("voperatorid", _getOperator());// �ڱ�β��
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

	// ����Զ��尴ť
	public void initPrivateButton() {

		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnNo(HgPuBtnConst.FZGN);
		btnvo.setBtnName("��������");
		btnvo.setBtnChinaName("��������");
		btnvo.setBtnCode(null);// code�������Ϊ��
		btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });
		btnvo.setChildAry(new int[] { HgPuBtnConst.TZJXQ, HgPuBtnConst.CKJXQ });
		addPrivateButton(btnvo);

//		// ����������
//		ButtonVO btnvo1 = new ButtonVO();
//		btnvo1.setBtnNo(HgPuBtnConst.XWZSQ);
//		btnvo1.setBtnName("����������");
//		btnvo1.setBtnCode(null);// code�������Ϊ��
//		btnvo1.setOperateStatus(new int[] { IBillOperate.OP_EDIT,
//				IBillOperate.OP_ADD });
//		addPrivateButton(btnvo1);
//		// ����������
		ButtonVO btnvo2 = new ButtonVO();
		btnvo2.setBtnNo(HgPuBtnConst.TZJXQ);
		btnvo2.setBtnName("����������");
		btnvo2.setBtnChinaName("����������");
		btnvo2.setBtnCode(null);// code�������Ϊ��
		btnvo2.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
				IBillOperate.OP_NO_ADDANDEDIT });
		btnvo2.setBusinessStatus(new int[]{IBillStatus.FREE});
		addPrivateButton(btnvo2);
		
		// ����������
		ButtonVO btnvo3 = new ButtonVO();
		btnvo3.setBtnNo(HgPuBtnConst.CKJXQ);
		btnvo3.setBtnName("�鿴������ϸ");
		btnvo3.setBtnChinaName("�鿴������ϸ");
		btnvo3.setBtnCode(null);// code�������Ϊ��
		btnvo3.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
				IBillOperate.OP_NO_ADDANDEDIT });
		addPrivateButton(btnvo3);
		
		// ����
		ButtonVO btnvo4 = new ButtonVO();
		btnvo4.setBtnNo(HgPuBtnConst.LINKQUERY);
		btnvo4.setBtnName("����");
		btnvo4.setBtnChinaName("����");
		btnvo4.setBtnCode(null);// code�������Ϊ��
		btnvo4.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
				IBillOperate.OP_NO_ADDANDEDIT });
		addPrivateButton(btnvo4);
		
		// ������ѯ
		ButtonVO btnvo5 = new ButtonVO();
		btnvo5.setBtnNo(HgPuBtnConst.ASSQUERY);
		btnvo5.setBtnName("������ѯ");
		btnvo5.setBtnChinaName("������ѯ");
		btnvo5.setBtnCode(null);// code�������Ϊ��
		btnvo5.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		btnvo5.setChildAry(new int[] { HgPuBtnConst.LINKQUERY,IBillButton.ApproveInfo});
		addPrivateButton(btnvo5);
		// ��ӡ����
		ButtonVO btnvo6 = new ButtonVO();
		btnvo6.setBtnNo(HgPuBtnConst.ASSPRINT);
		btnvo6.setBtnName("��ӡ����");
		btnvo6.setBtnChinaName("��ӡ����");
		btnvo6.setBtnCode(null);// code�������Ϊ��
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
	 * ��ͷ�༭ǰ
	 */
	public boolean beforeEdit(BillItemEvent e) {
		// TODO Auto-generated method stub
		String key = e.getItem().getKey();
		if ("creqcalbodyid".equalsIgnoreCase(key)) {// ������֯
			String corpid = PuPubVO
					.getString_TrimZeroLenAsNull(getBillCardPanel()
							.getHeadItem("pk_corp").getValueObject());
			if (corpid == null) {
				e.getItem().setValue(null);
//				showWarningMessage("��¼�빫˾");
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
		} else if ("capplypsnid".equalsIgnoreCase(key)) {// ������
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
		} else if ("creqwarehouseid".equalsIgnoreCase(key)) {// ����ֿ�
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
		} else if ("csupplydeptid".equalsIgnoreCase(key)) {// ��������
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
		} else if ("capplydeptid".equalsIgnoreCase(key)) {// ���벿��
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
		//�ᱨ�Ĳ��ɱ༭
		if(getBillOperate()==IBillOperate.OP_EDIT){
			PlanVO head = (PlanVO)getBufferData().getCurrentVO().getParentVO();
			if(head == null)
				return true;
			if(!PuPubVO.getUFBoolean_NullAs(head.getFisself(), UFBoolean.TRUE).booleanValue())
				if(HgPubTool.getString_NullAsTrimZeroLen(getBillCardPanel().getBillModel().getValueAt(row,"irowstatus")).equals(HgPubConst.PLAN_VBILLSTATUS_SELT)){
					return false;
				}
		}
         
		if (getEventHandler().m_isAdjust) {// ����������
			if (key.equalsIgnoreCase("nnetnum"))
				return true;
			else
				return false;
		}
		if (key.equalsIgnoreCase("nnetnum")){
			return false;
		}
		if ("invcode".equalsIgnoreCase(key)) {// ���
			 String cinvclassid = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getHeadItem("cinvclassid").getValueObject());
			 String invcode =PuPubVO.getString_TrimZeroLenAsNull(HgPubTool.getInvclasscode(cinvclassid));//����������
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
				 String invcode =PuPubVO.getString_TrimZeroLenAsNull(HgPubTool.getInvclasscode(cinvclassid));//����������
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
		}else if ("castname".equalsIgnoreCase(key)) {// ������
			// ���ID
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
		} else if ("calname".equalsIgnoreCase(key)) {// ������֯
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
		} else if ("rename".equalsIgnoreCase(key)) {// ����ֿ�
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
		}	else if ("sucalname".equalsIgnoreCase(key)) {// ������֯
			String csupplycorpid = PuPubVO
			.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getHeadItem("csupplycorpid").getValueObject());
			UIRefPane invRefPane = (UIRefPane) getBillCardPanel().getBodyItem(
					"sucalname").getComponent();
			AbstractRefModel refModel = (AbstractRefModel) invRefPane
					.getRefModel();
			if (csupplycorpid == null) {
				showErrorMessage("����ѡ���ͷ������λ");
				return false;
			}
			 refModel.setPk_corp(csupplycorpid);
			return true;
		} 
		else if ("suware".equalsIgnoreCase(key)) {// �����ֿ�
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
			if ("creqcalbodyid".equalsIgnoreCase(key)) {// ������֯
				int row = getBillCardPanel().getRowCount();
				String creqcalbodyid = PuPubVO
				.getString_TrimZeroLenAsNull(getHeadItemValue("creqcalbodyid"));// ������֯
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
			} else if ("creqwarehouseid".equals(key)) { // ����ֿ�
				int row = getBillCardPanel().getRowCount();
				String pk_channelpsn = PuPubVO
				.getString_TrimZeroLenAsNull(getHeadItemValue("creqwarehouseid"));// ����ֿ�
				setBodyCelValue(row, "creqwarehouseid", pk_channelpsn);

				String[] aryAssistunit = new String[] { "creqcalbodyid->getColValue(bd_stordoc,pk_calbody,pk_stordoc,creqwarehouseid)" };
				getBillCardPanel().execHeadFormulas(aryAssistunit);
				String creqcalbodyid = PuPubVO.getString_TrimZeroLenAsNull(getHeadItemValue("creqcalbodyid"));// ������֯
				setBodyCelValue(row, "creqcalbodyid", creqcalbodyid);
				getBillCardPanel().getBillModel().execLoadFormulaByKey("creqcalbodyid");
				getBillCardPanel().getBillModel().execLoadFormulaByKey("creqwarehouseid");
			} 
//			else if ("capplydeptid".equalsIgnoreCase(key)) {// ���벿��
//				String[] aryAssistunit = new String[] { "creqcalbodyid->getColValue(bd_deptdoc,pk_calbody,pk_deptdoc,capplydeptid)" };
//				getBillCardPanel().execHeadFormulas(aryAssistunit);
//				getBillCardPanel().getHeadItem("capplypsnid").setValue(null);
//				getBillCardPanel().getHeadItem("creqwarehouseid").setValue(null);
//				int row = getBillCardPanel().getRowCount();
//				String creqcalbodyid = PuPubVO
//				.getString_TrimZeroLenAsNull(getHeadItemValue("creqcalbodyid"));// ������֯
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
			else if ("capplypsnid".equalsIgnoreCase(key)) {// ������
				getBillCardPanel().execHeadTailEditFormulas(
						getBillCardPanel().getHeadItem("capplypsnid"));
			} else if ("csupplycorpid".equalsIgnoreCase(key)) {// ������λ
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
				
//				String supplycalbodyid =PuPubVO.getString_TrimZeroLenAsNull(m_appInfor==null?null:m_appInfor.getCsupplycalbodyid());//������֯
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
			} else if ("csupplydeptid".equalsIgnoreCase(key)) {// ��������
//				String pk_corp =PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("csupplycorpid").getValue());
//				if(pk_corp == null){
				getBillCardPanel().execHeadTailEditFormulas(
				        getBillCardPanel().getHeadItem("csupplydeptid"));
//				}else{
//					getBillCardPanel().getHeadItem("csupplycorpid").setValue(pk_corp);
//				}
			} 
			else if ("cinvclassid".equalsIgnoreCase(key)) {// �������
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
				// ���
				afterEditWhenInv(e, bodyRow);
			} else if ("castname".equalsIgnoreCase(key)) {
				// ������
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
			}else if ("calname".equalsIgnoreCase(key)) {// ������֯
				getBillCardPanel().setBodyValueAt(null, bodyRow,
				"creqwarehouseid");
				getBillCardPanel().setBodyValueAt(null, bodyRow, "rename");
			} else if ("rename".equals(key)) { // ����ֿ�
				String[] aryAssistunit = new String[] {
						"creqcalbodyid->getColValue(bd_stordoc,pk_calbody,pk_stordoc,creqwarehouseid)",
				"calname->getColValue(bd_calbody,bodyname,pk_calbody,creqcalbodyid)" };
				getBillCardPanel().getBillModel().execFormulas(e.getRow(),
						aryAssistunit);
			}else if ("sucalname".equalsIgnoreCase(key)) {// ������֯
				getBillCardPanel().setBodyValueAt(null, bodyRow,"csupplywarehouseid");
				getBillCardPanel().setBodyValueAt(null, bodyRow, "suware");
			}else if ("suware".equals(key)) { // �����ֿ�
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
		//�������ʱ���� ����Ϊ��ʱ���ʲ���  �������  Ϊ�����������
		boolean isuse = PuPubVO.getUFBoolean_NullAs(getBillCardPanel().getBillModel().getValueAt(row, "bisuse"), UFBoolean.FALSE).booleanValue();
		String invid;
		BillEditEvent e2 = null;
		if(isuse){//�������
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
//	private AbstractRefModel getAbstractRefModel(){//�������MODEL
//		if(abs == null){
//			abs = new UIRefPane("�������").getRefModel();
//		}
//		return abs;
//	}
//	
//	private InvbasdocRefModel  inv= null;
//	private InvbasdocRefModel getInvbasdocRefModel(){//��ʱ���ʲ���MODEL
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
	 * ���漴�ύ
	 */
//	@Override
//	public boolean isSaveAndCommitTogether() {
//		return false;
//	}

	/**
	 * @˵�� ���ݱ��� tableCode,���ҳǩ����
	 * @ʱ�� 2010-9-14����02:06:02
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
		// �������ID��������ID
		String cbaseid = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
				.getBillModel().getValueAt(row, "pk_invbasdoc"));
		if (cbaseid != null && nc.ui.pu.pub.PuTool.isAssUnitManaged(cbaseid)) {

			// ���ø�������λ����
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
	 * �������༭�¼� ���� # �����������༭ʱ�������� ==>> ������ | # ѡȡ�ļ���ID��������ID���任����Ϊ1���̶������� | #
	 * �ɡ������ʡ�������ʽ���� > ����ɾ�����ϸ������Ƿ�ɱ༭ # ͬ�����µ���ģ���еĻ����ʺ��Ƿ�̶����������� | # ���»����������пɱ༭��
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	private void afterEditWhenAssistUnit(BillEditEvent e) {
		// �Ƿ�̶��仯�ʸı�ʱҪͬ�����ģ�strKeys[0]��ֵ
		boolean isfixed = true;
		UFDouble convert = new UFDouble(0);
		// ���ID
		String sBaseID = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
				.getBillModel().getValueAt(e.getRow(), "pk_invbasdoc"));
		// ����������
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
		// ��ȡ������
		convert = nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID, sCassId);
		// �Ƿ�̶�������
		isfixed = PuTool.isFixedConvertRate(sBaseID, sCassId);
		// ���ø���Ϣ ������
		getBillCardPanel().setBodyValueAt(isfixed, e.getRow(), "fixedflag");// �Ƿ�̶�������
		getBillCardPanel().getBillModel()
				.setValueAt(convert, e.getRow(), "hsl");
		if (isfixed)
			HgPubTool.m_saKey[0] = "Y";
		else
			HgPubTool.m_saKey[0] = "N";
		// //�û������������㣺�������������������ϸ����������ϸ����������ۣ����
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

	// ����༭�� ��ո���Ԫ��
	private void clearBodyItem(int row, String invbasdoc) {
		
//		if(invbasdoc == null){
//			setBodyRowValue(row, "vreserve1", null);//��ʱ���ID
//			setBodyRowValue(row, "pk_invbasdoc", null);//�������ID
//			setBodyRowValue(row, "cinventoryid", null);//�������ID
//			setBodyRowValue(row, "invname", null);//����
//			setBodyRowValue(row, "invtype", null);//�ͺ�
//			setBodyRowValue(row, "cinvcode", null);//��ʱ�������
//			setBodyRowValue(row, "invspec", null);//�ͺ�
//			setBodyRowValue(row, "nprice", null);//�۸�
//			setBodyRowValue(row, "castunitid", null);//����λ����
//			setBodyRowValue(row, "hsl", null);//������
//			setBodyRowValue(row, "fixedflag", null);//�Ƿ񸨼�������
//			setBodyRowValue(row, "castname", null);//����λ����
//		}
		
		setBodyRowValue(row, "vbatchcode", null);//����
		setBodyRowValue(row, "nmny", null);//���
		
		for (int i = 0; i < 12; i++) {
			setBodyRowValue(row, HgPubConst.NMONTHNUM[i], null);//12�·���
		}
		setBodyRowValue(row, "nnetnum", null);//������
		setBodyRowValue(row, "nnum", null);//ë����
		setBodyRowValue(row, "nassistnum", null);//������
		setBodyRowValue(row, "nallusenum", null);//���������
		setBodyRowValue(row, "nnonum", null);//��治����
		setBodyRowValue(row, "nreoutnum", null);//����Ԥ��
		setBodyRowValue(row, "nreinnum", null);//ʣ����Դ
		setBodyRowValue(row, "nusenum", null);//��ǰ���
		setBodyRowValue(row, "nonhandnum", null);//1-N������
		setBodyRowValue(row, "nsafestocknum", null);//��ȫ���
	}
	
}

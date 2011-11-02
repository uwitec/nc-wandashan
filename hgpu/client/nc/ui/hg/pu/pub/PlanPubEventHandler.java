package nc.ui.hg.pu.pub;

import java.util.HashMap;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.hg.pu.plan.month.PlanOtherBVO;
import nc.vo.hg.pu.plan.year.PlanYearBVO;
import nc.vo.hg.pu.pub.HgPuBtnConst;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.hg.pu.pub.PlanApplyInforVO;
import nc.vo.hg.pu.pub.PlanBVO;
import nc.vo.hg.pu.pub.PlanVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public abstract class PlanPubEventHandler extends FlowManageEventHandler {


	private SourceBillFlowDlg soureDlg = null;
	
	public PlanPubEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
    private ModifyNnetNumDlg m_ModifyNnetNumDlg = null;
	@Override
	protected void onBoLineAdd() throws Exception {
		String cfhzcid = PuPubVO.getString_TrimZeroLenAsNull(
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cinvclassid").getValueObject());
				if(cfhzcid == null){
					getBillUI().showErrorMessage("����ѡ��[�������]");
					return;
		}
		super.onBoLineAdd();
		BillRowNo.addLineRowNo(getBillCardPanelWrapper().getBillCardPanel(),
				HgPubConst.PLAN_YEAR_BILLTYPE, "crowno");
		setLineDefData(-1);
	}
	
	private PlanPubClientUI getPlanUI(){
		return (PlanPubClientUI)getBillManageUI();
	}
	
	protected void onBoLineDel() throws Exception {
		BillCardPanel bcp=getBillCardPanelWrapper().getBillCardPanel();
		int row =bcp.getBillTable().getSelectedRow();
		if(getBillUI().getBillOperate()==IBillOperate.OP_EDIT){
			PlanVO head = (PlanVO)getBufferData().getCurrentVO().getParentVO();
			if(head != null){
				if(!PuPubVO.getUFBoolean_NullAs(head.getFisself(), UFBoolean.TRUE).booleanValue()){
					if(HgPubTool.getString_NullAsTrimZeroLen(bcp.getBillModel().getValueAt(row,"irowstatus")).equals(HgPubConst.PLAN_VBILLSTATUS_SELT)){
				    	  throw new BusinessException("��ǰ�����ϱ��ģ�����ɾ��");
				    }
				}
			}	
		}
	    super.onBoLineDel();
	}
	
	private void setLineDefData(int rowno){
		int row = rowno==-1?getBillCardPanelWrapper().getBillCardPanel().getRowCount()-1:rowno;
		String calbodyid = PuPubVO.getString_TrimZeroLenAsNull(getHeadItemValue("creqcalbodyid"));//������֯
		String warehouseid = PuPubVO.getString_TrimZeroLenAsNull(getHeadItemValue("creqwarehouseid"));//����ֿ�
		setBodyCelValue(row, "creqcalbodyid", calbodyid);
		setBodyCelValue(row, "creqwarehouseid", warehouseid);
//		setBodyCelValue(row, "dreqdate",_getDate());
		setBodyCelValue(row, "csupplydate",_getDate());
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormulaByKey("creqcalbodyid");
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormulaByKey("creqwarehouseid");
		PlanApplyInforVO appinfor = ((PlanPubClientUI)getBillUI()).m_appInfor;
		if(appinfor!=null){
			 String supplycalbodyid =PuPubVO.getString_TrimZeroLenAsNull(appinfor.getCsupplycalbodyid());//������֯
			 if(supplycalbodyid==null&&appinfor.getM_pocorp().equalsIgnoreCase(appinfor.getCsupplycorpid()))
				 supplycalbodyid = calbodyid;//zhf add �ڹ�Ӧ��  ������֯=������֯   ��֯ʼ�ղ���Ϊ�շ�������İ�ȫ����޷����
			 getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(supplycalbodyid,row,"csupplycalbodyid");
		}
	    getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormulaByKey("csupplycalbodyid");
	    if(PuPubVO.getUFBoolean_NullAs(getHeadItemValue("fisself"),UFBoolean.TRUE).booleanValue()){//����
	    	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(0,row,"irowstatus");
	    }else {
	    	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(2,row,"irowstatus");
	    }
	    
	}
	
	private void setBodyCelValue(int row,String filedname,Object oValue){
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(oValue, row, filedname);
	}
	
	private Object getHeadItemValue(String sitemname){
		return getBillCardPanelWrapper().getBillCardPanel().getHeadItem(sitemname).getValueObject();
	}
	
	@Override
	protected void onBoSave() throws Exception {
		beforeSaveCheckHead();
		if(!m_isAdjust){
			beforeSave();
		}
		
		super.onBoSave();
		setAdjustFlag(false);
		if(getOldNumMap()!=null)
			getOldNumMap().clear();
	}
	
	
	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCancel();
		setAdjustFlag(false);
		if(getOldNumMap()!=null)
			getOldNumMap().clear();
	}

	private void setAdjustFlag(boolean flag){
		m_isAdjust = flag;
	}
	public boolean m_isAdjust = false;//��������ɾ�в���ʹ��
	private Map<String, UFDouble> oldNumMap = null;
	public Map<String,UFDouble> getOldNumMap(){
		if(oldNumMap==null){
			oldNumMap = new HashMap<String, UFDouble>();
		}
		return oldNumMap;
	}
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		// TODO Auto-generated method stub
		if(intBtn == HgPuBtnConst.TZJXQ){//����������
		   onBoAdjust();
		}else if(intBtn == HgPuBtnConst.XWZSQ){//����������
			
		}else if(intBtn == HgPuBtnConst.CKJXQ){//�鿴������
			onBoView();
		}else if(intBtn == HgPuBtnConst.LINKQUERY){//����
			onJoinQuery();
		}else if(intBtn == HgPuBtnConst.Editor){//�޸�
			onBoEdit();
		}else{
			super.onBoElse(intBtn);
		}
	}
	
	
	  /**
	   * ����
	   */
	public void onJoinQuery()throws BusinessException {
		getBillManageUI().showHintMessage("����");
		if(getBufferData().getCurrentVO() == null ){
			return;
		}
		getSourceDlg().showModal();
	}
	/**
	 * ����Ի���
	 */
	public SourceBillFlowDlg getSourceDlg() throws BusinessException{
			try {
				soureDlg = new SourceBillFlowDlg(getBillManageUI(), getUIController().getBillType(),/* ��ǰ�������� */
						getBufferData().getCurrentVO().getParentVO().getPrimaryKey(), /* ��ǰ����ID */
						null, /* ��ǰҵ������ */
						_getOperator(), /* ��ǰ�û�ID */
						(String)getBufferData().getCurrentVO().getParentVO().getAttributeValue("vbillno") /* ���ݺ� */);
			} catch (BusinessException e) {
				Logger.error(e);
				throw new BusinessException("��ȡ����Ի������! ");
			}
			return soureDlg;
	}
	
	private void onBoView() throws Exception{
		
		if(getBufferData().isVOBufferEmpty()){
			return;
		}
		if(getBufferData().getCurrentRow()<0){
			getBillUI().showWarningMessage("��ѡ��Ҫ�����ĵ���");
			return;
		}
		
		PlanVO head =(PlanVO)getBufferData().getCurrentVO().getParentVO();
		String billType=PuPubVO.getString_TrimZeroLenAsNull(head.getPk_billtype());
		int row=-2;
		if (!getBillManageUI().isListPanelSelected()) {// ��Ƭ��ʾ
			row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		}else{
			row=getBillListPanel().getBodyTable().getSelectedRow();
		}
		
		if(row<0){
			getBillUI().showWarningMessage("��ѡ��Ҫ�鿴�ı�������");
		return;
		}
		String pk=null;
		if(billType !=null){
			if("HG01".equalsIgnoreCase(billType)){
				PlanYearBVO body =(PlanYearBVO) getBillCardPanelWrapper().getBillCardPanel().
				getBillModel().getBodyValueRowVO(row,PlanYearBVO.class.getName());
				if(body!=null){
					pk = body.getPrimaryKey();
				}
			}else{
				PlanOtherBVO body =(PlanOtherBVO) getBillCardPanelWrapper().getBillCardPanel().
				getBillModel().getBodyValueRowVO(row,PlanOtherBVO.class.getName());
				if(body!=null){
					pk = body.getPrimaryKey();
				}
			}
		}
		
		getModifyNnetNumDlg().setPk(pk);
		getModifyNnetNumDlg().initBillListPanel();
		
		if (getModifyNnetNumDlg().showModal() == UIDialog.ID_OK) {
			return;
		}
	}
	
	private ModifyNnetNumDlg getModifyNnetNumDlg() throws Exception {
		if (m_ModifyNnetNumDlg == null) {
			m_ModifyNnetNumDlg = new ModifyNnetNumDlg(getBillUI());
		}
		return m_ModifyNnetNumDlg;
	}
//
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		super.onBoAdd(bo);
		//setHeadEditEnableWhenAdjustNum(true);
	}

	@Override
	protected void onBoEdit() throws Exception {
		if(getBufferData().isVOBufferEmpty()){
			return;
		}
		if(getBufferData().getCurrentRow()<0){
			getBillUI().showWarningMessage("��ѡ��Ҫ�޸ĵĵ���");
			return;
		}
		super.onBoEdit();
		//setHeadEditEnableWhenAdjustNum(true);
		
	}
	
	private void onBoAdjust() throws Exception{
		if(PuPubVO.getUFBoolean_NullAs(getHeadItemValue("fisself"),UFBoolean.TRUE).booleanValue())
			throw new BusinessException("���Ƶ��ݲ���Ҫ����������,���޸�");
		if(getBufferData().isVOBufferEmpty()){
			return;
		}
		if(getBufferData().getCurrentRow()<0){
			getBillUI().showWarningMessage("��ѡ��Ҫ�����ĵ���");
			return;
		}
		super.onBoEdit();

		getButtonManager().getButton(IBillButton.Line).setEnabled(false);
		getBillUI().updateButton(getButtonManager().getButton(IBillButton.Line));//�����в�����ť������
		
		PlanBVO[] bodys = (PlanBVO[])getBufferData().getCurrentVO().getChildrenVO();
		getOldNumMap().clear();
		for(PlanBVO body:bodys){
			getOldNumMap().put(body.getPrimaryKey(), PuPubVO.getUFDouble_NullAsZero(body.getNnetnum()));
		}
		setAdjustFlag(true);
		//����ֻ�о�������Ա༭
		//setHeadEditEnableWhenAdjustNum(false);
	}
	
//	private void setHeadEditEnableWhenAdjustNum(boolean flag){
//		String[] keys = HgPubConst.Plan_Head_EditItems;
//		BillCardPanel card = getBillCardPanelWrapper().getBillCardPanel();
//		for(String key:keys){
//			card.getHeadItem(key).setEdit(flag);
//		}
//	}

	protected abstract void beforeSave() throws Exception;
	
	private void beforeSaveCheckHead() throws Exception{
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		PlanVO head=(PlanVO)checkVO.getParentVO();
		head.validata();
	}
	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-9-12 15:03:31)
	 *
	 * @return java.lang.String
	 */
	   
//	//��ǰ��ʾ��Panel,LIST OR CARD
	protected String m_CurrentPanel = BillTemplateWrapper.LISTPANEL;
	
	public boolean isListPanelSelected() {
		return m_CurrentPanel.equals(BillTemplateWrapper.LISTPANEL);
	}
	
	protected void onBoCopy() throws Exception {
		super.onBoCopy();
		getPlanUI().setDefaultDataForCopy();
		//��ձ�����ƽ����   ��״̬Ϊ����̬  
		BillModel bm  = getBillCardPanelWrapper().getBillCardPanel().getBillModel();
		int rowcount = bm.getRowCount();
		if(rowcount<=0)
			return;
		for(int i=0;i<rowcount;i++){
			for(String name:HgPubTool.PLAN_IC_NUMS){
				bm.setValueAt(null, i, name);
			}
			bm.setValueAt(bm.getValueAt(i, "nnum"), i, "nnetnum");
			setLineDefData(i);
		}
	}
}

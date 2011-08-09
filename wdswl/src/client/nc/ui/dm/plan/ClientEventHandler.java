package nc.ui.dm.plan;

import java.util.ArrayList;

import nc.ui.dm.PlanDealHealper;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wl.pub.BeforeSaveValudate;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.dm.SendplaninBVO;
import nc.vo.dm.SendplaninVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.dm.sendinvdoc.SendinvdocVO;
import nc.vo.wl.pub.ButtonCommon;
import nc.vo.wl.pub.WdsWlPubTool;

public class ClientEventHandler extends WdsPubEnventHandler {

	public ClientUIQueryDlg queryDialog = null;

	public ClientEventHandler(ClientUI clientUI, IControllerBase control) {
		super(clientUI, control);
	}

	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
		
			
			queryDialog=new ClientUIQueryDlg(	getBillUI(),
					null,
					_getCorp().getPrimaryKey(),
					getBillUI()._getModuleCode(),
					_getOperator(),
					getBillUI().getBusinessType(),
					getBillUI().getNodeKey());
			//queryDialog = new ClientUIQueryDlg(getBillUI(), null, tempinfo);
		}
		return queryDialog;
	}

	@Override
	protected String getHeadCondition() {
//		String whereSql = null;
//		try{
//			String cwhid = LoginInforHelper.getLogInfor(_getOperator()).getWhid();
//			if(!WdsWlPubTool.isZc(cwhid)){//���ܲ���Ա��½  ֻ�ܲ�ѯ �����ֿ�Ϊ����ķ��˼ƻ�
//				whereSql=" wds_sendplanin.pk_outwhouse = '"+cwhid+"'";
//			};
//		}catch(Exception e){
//			e.printStackTrace();
//			getBillUI().showErrorMessage(e.getMessage());
//		}
//		return whereSql;
		return " pk_corp = '"+_getCorp().getPrimaryKey()+"' and isnull(dr,0) = 0  ";
	}
	
	@Override
	protected void onBoSave() throws Exception {
		beforeSaveCheck();		
		try {
			dataNotNullValidate();
		} catch (ValidationException e) {
			MessageDialog.showErrorDlg(getBillUI(), "У��", e.getMessage());
			return;
		}
		
		AggregatedValueObject billVO = getBillUI().getChangedVOFromUI();
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);
		
//		zhf add У��
		SendplaninBVO[] bodys = (SendplaninBVO[])checkVO.getChildrenVO();
		if(bodys==null || bodys.length ==0)
			throw new BusinessException("�������ݲ���Ϊ��");
		for(SendplaninBVO body:bodys)
			body.validationOnSave();
//		zhf end
		
		// �����������
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

		// �ж��Ƿ��д�������
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

		// �������ݻָ�����
		if (sCtrl != null) {
			if (sCtrl.isSingleDetail())
				billVO.setParentVO((CircularlyAccessibleValueObject) o);
		}
		int nCurrentRow=-1;
		if (isSave) {
			if (isEditing()) {
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(billVO);
					nCurrentRow=0;
					
				} else {
					getBufferData().setCurrentVO(billVO);
					nCurrentRow=getBufferData().getCurrentRow();
				}
			}
			// �������������
			setAddNewOperate(isAdding(), billVO);
		}
		// ���ñ����״̬
		setSaveOperateState();
		if(nCurrentRow>=0){
			getBufferData().setCurrentRow(nCurrentRow);
		}
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * ���屣��ǰУ��
	 * @ʱ�䣺2011-3-23����09:05:20
	 * @throws Exception
	 */
	protected void beforeSaveCheck() throws Exception{
		if(getBillUI().getVOFromUI()!=null){
			if(getBillUI().getVOFromUI().getChildrenVO()==null||
					getBillUI().getVOFromUI().getChildrenVO().length==0	){
				throw new BusinessException("���岻����Ϊ��");
			}
		AggregatedValueObject vo=getBillUI().getVOFromUI();
		//ֻ��׷�Ӽƻ�У�� �ƻ�����������Ϊ��
		if(vo.getParentVO()==null){
			return;
		}
		Integer planType=PuPubVO.getInteger_NullAs(vo.getParentVO().getAttributeValue("iplantype"),new Integer(3));
		if(planType.intValue()==1){
		BeforeSaveValudate.checkNotAllNulls(getBillUI().getVOFromUI(),new String[]{"nplannum","nassplannum"}, new String[]{"�ƻ�����","�ƻ�������"});	
		}
//			}else{
//				super.beforeSaveBodyUnique(new String[]{"pk_invbasdoc"});
//			}
		};
	}
	
	
	
	
	@Override
	protected void onBoLineAdd() throws Exception {
		// TODO Auto-generated method stub
		super.onBoLineAdd();
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		if(intBtn == ButtonCommon.BILLCLOSE){
			closeBill();
		}else if(intBtn == ButtonCommon.ROWCLOSE){
			closeRow();
		}else
		super.onBoElse(intBtn);
	}
	
/**
 * 
 * @���ߣ�zhf
 * @˵�������ɽ������Ŀ �����ر�  ��֧���б�������
 * @ʱ�䣺2011-6-25����06:19:49
 */
	private void closeBill(){
		if(getBufferData().isVOBufferEmpty()){
			getBillUI().showWarningMessage("������");
			return;
		}
		if(getBufferData().getCurrentRow()<0){
			getBillUI().showWarningMessage("������");
			return;
		}
		
		int flag = MessageDialog.showOkCancelDlg(getBillUI(), "ѯ��", "ȷ�Ϲرյ�ǰ���˼ƻ���?"); 
		if(flag != UIDialog.ID_OK){
			getBillUI().showHintMessage("ȡ���˹رղ���");
			return;
		}
		
		HYBillVO bill = (HYBillVO)getBufferData().getCurrentVO();
		
		HYBillVO newbill = null;
//		��̨�رռƻ�
	   try {
		newbill = PlanDealHealper.closeBill(bill.getParentVO().getPrimaryKey());
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		getBillUI().showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
		return;
	}
		
//		�رճɹ�ǰ̨���ݴ���   ���»���  ���� ��Ƭ����
		if(newbill == null){
			getBillUI().showWarningMessage("�رղ����ɹ�,ˢ��ǰ̨����ʧ��");
			return;
		}
		
		getBufferData().setCurrentVO(newbill);
		getBillCardPanelWrapper().getBillCardPanel().getBillData().setBillValueVO(newbill);
		
//		��̨�رռƻ�
		
//		�رճɹ�ǰ̨���ݴ���   ���»���  ���� ��Ƭ����
		
		
//		int[] rows = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRows();
//		if(rows == null || rows.length ==0){
//			getBillUI().showWarningMessage("");
//		}
			
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ �йر�  ��Ƭ��  ��ѡ�ж��н��йر�
	 * @ʱ�䣺2011-6-25����06:20:15
	 */
	private void closeRow(){
		if(getBufferData().isVOBufferEmpty()){
			getBillUI().showWarningMessage("������");
			return;
		}
		if(getBufferData().getCurrentRow()<0){
			getBillUI().showWarningMessage("������");
			return;
		}
		
		int[] rows = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRows();
		if(rows == null || rows.length ==0){
			getBillUI().showWarningMessage("��ѡ��Ҫ�رյ���");
			return;
		}
		
		int flag = MessageDialog.showOkCancelDlg(getBillUI(), "ѯ��", "ȷ�Ϲر�ѡ������?"); 
		if(flag != UIDialog.ID_OK){
			getBillUI().showHintMessage("ȡ�����йرղ���");
			return;
		}
		
//		��ȡ����
	    java.util.List<String> ldata = new ArrayList<String>();
	    HYBillVO bill = (HYBillVO)getBufferData().getCurrentVO();
	    if(bill == null){

			getBillUI().showWarningMessage("������");
			return;
		
	    }
	    
	    SendplaninVO head = (SendplaninVO)bill.getParentVO(); 
	    
	    ldata.add(head.getPrimaryKey());
	    
	    SendplaninBVO tmpbody = null;
	    for(int row:rows){
	    	tmpbody = (SendplaninBVO)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getBodyValueRowVO(row, SendplaninBVO.class.getName());
	    	if(!PuPubVO.getUFBoolean_NullAs(tmpbody.getReserve14(), UFBoolean.FALSE).booleanValue())
	    	ldata.add(tmpbody.getPrimaryKey());
	    }
	    
	    if(ldata.size()<=0)
	    	return;
	    	
		HYBillVO newbill = null;
//		��̨�رռƻ�
	   try {
		newbill = PlanDealHealper.closeRows(ldata);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		getBillUI().showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
		return;
	}
		
//		�رճɹ�ǰ̨���ݴ���   ���»���  ���� ��Ƭ����
		if(newbill == null){
			getBillUI().showWarningMessage("�رղ����ɹ�,ˢ��ǰ̨����ʧ��");
			return;
		}
		
		getBufferData().setCurrentVO(newbill);
		getBillCardPanelWrapper().getBillCardPanel().getBillData().setBillValueVO(newbill);
		getBillCardPanelWrapper().getBillCardPanel().execHeadTailLoadFormulas();
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
	}
	
	
	@Override
	public void onBoAudit() throws Exception {
		if(getBufferData().getCurrentVO() ==null){
			getBillUI().showWarningMessage("����ѡ��һ������");
			return;
		}
		super.onBoAudit();
	}
	@Override
	public void onBoCancelAudit()throws Exception{
			if(getBufferData().getCurrentVO() ==null){
			getBillUI().showWarningMessage("����ѡ��һ������");
			return;
		}
		super.onBoCancelAudit();
	}
	
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {	
		//����pk_corp
	 SuperVO[] vos= HYPubBO_Client.queryByCondition(SendinvdocVO.class, "pk_corp = '"+_getCorp().getPrimaryKey()+"' and  isnull(dr,0)=0  order by crow");	
	 super.onBoAdd(bo);	 	 	 
	 getBillCardPanelWrapper().getBillCardPanel().getBillData().setBodyValueVO(vos);
	 getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
	}
}






















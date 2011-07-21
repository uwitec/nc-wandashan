package nc.ui.hg.pu.nmr;

import java.text.SimpleDateFormat;
import java.util.Date;

import nc.ui.hg.pu.pub.FlowManageEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.hg.pu.nmr.NewMaterialsVO;
import nc.vo.hg.pu.pub.HgPuBtnConst;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.PlanApplyInforVO;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;

public class ClientEventHandler extends FlowManageEventHandler {

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	protected UIDialog createQueryUI() {
		return new ClientUIQueryDlg(getBillUI(), null, _getCorp()
				.getPrimaryKey(), getBillUI().getModuleCode(), _getOperator(),
				null, null);
	}
	
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		// TODO Auto-generated method stub
		if(intBtn == HgPuBtnConst.CODING){//����
			onBOCoding();
		}else if(intBtn == HgPuBtnConst.OK){//ȷ��
			onBOOK();
		}else if(intBtn == HgPuBtnConst.Editor){//�޸�
			   onBoEdit();
		}else {
			super.onBoElse(intBtn);
		}
	}
	
	protected void onBOCoding() throws Exception{
		PlanApplyInforVO appinfor = ((ClientUI)getBillUI()).m_appInfor;
		if(appinfor==null){
			getBillUI().showErrorMessage("��ȡ������Ϣʧ��!");
			return;
		}
		if(!_getCorp().getPrimaryKey().equalsIgnoreCase(appinfor.getM_pocorp()))
			throw new BusinessException("���ϲ��ܱ���");
		super.onBoEdit();
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("voperatorid",_getOperator());//������
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("reserve12",_getDate());//��������
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPrimaryKey());//��˾
		setHeadEditEnableWhenCoding(true);
		BillCardPanel card = getBillCardPanelWrapper().getBillCardPanel();
		card.getHeadItem("invcode").setEdit(true);
		setButtonEnable(false,IBillButton.Save);
		setButtonEnable(true,HgPuBtnConst.OK);
		
	}
	
	protected void onBOOK() throws Exception{
		BillCardPanel card = getBillCardPanelWrapper().getBillCardPanel();
		String invcode =PuPubVO.getString_TrimZeroLenAsNull(card.getHeadItem("invcode").getValueObject()); 
		if(invcode==null)
			throw new BusinessException("���ȱ������ȷ��");
		
		Object ijjway = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ijjway").getValueObject();
		Object pk_taxitems =getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_taxitems").getValueObject();
		Object nplanprice =getBillCardPanelWrapper().getBillCardPanel().getHeadItem("nplanprice").getValueObject();
		if(PuPubVO.getString_TrimZeroLenAsNull(ijjway)==null)
			throw new BusinessException("���㷽ʽ����Ϊ��");
		if(PuPubVO.getString_TrimZeroLenAsNull(pk_taxitems)==null)
			throw new BusinessException("˰Ŀ����Ϊ��");
		if(PuPubVO.getString_TrimZeroLenAsNull(nplanprice)==null)
			throw new BusinessException("�ƻ��۲���Ϊ��");
		super.onBoSave();
		card.getHeadItem("invcode").setEdit(false);
	}

//	//���ñ��水ť�༭�� 
	private void setButtonEnable(boolean flag,int buttonName){
		getButtonManager().getButton(buttonName).setEnabled(flag);
		getBillUI().updateButton(getButtonManager().getButton(buttonName));
	}
	
	//���ñ�ͷԪ�ر༭��
	private void setHeadEditEnableWhenCoding(boolean flag){
		String[] keys = HgPubConst.MATERIALS_HEAD_EDITITEMS;
		BillCardPanel card = getBillCardPanelWrapper().getBillCardPanel();
		for(String key:keys){
			card.getHeadItem(key).setEdit(flag);
		}
	}
	
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		super.onBoAdd(bo);
		setHeadItemValue("cinvcode",getInvcode());
		setHeadEditEnableWhenCoding(false);
		setButtonEnable(true,IBillButton.Save);
		
	}
     
     public String  getInvcode(){
    	 SimpleDateFormat format = new SimpleDateFormat("dHHmmss");
 		 String date = format.format(new Date());
    	 String str= "99"+date.toString();
    	 return str;
     }
     public void setHeadItemValue(String item,String value){
    	 getBillCardPanelWrapper().getBillCardPanel().getHeadItem(item).setValue(value);
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
		
		String corp = PuPubVO.getString_TrimZeroLenAsNull(getBufferData().getCurrentVO().getParentVO().getAttributeValue("ccorpid"));
		if(!_getCorp().getPrimaryKey().equalsIgnoreCase(corp))
			throw new BusinessException("�������������ʱ���,�����޸�");
			
		String invcode =null;
		BillCardPanel card = getBillCardPanelWrapper().getBillCardPanel();
		BillListPanel list = getBillListPanelWrapper().getBillListPanel();
		if(getBillManageUI().isListPanelSelected()){//�б�״̬
			int selectRow = list.getHeadTable().getSelectedRow();
			invcode=PuPubVO.getString_TrimZeroLenAsNull(list.getHeadBillModel().getValueAt(selectRow,"invcode"));
		}else{
			invcode=PuPubVO.getString_TrimZeroLenAsNull(card.getHeadItem("invcode").getValueObject());
		}
		if(invcode!=null)
			throw new BusinessException("�Ѿ�����,�����޸�");
		super.onBoEdit();
		setHeadEditEnableWhenCoding(false);
		setButtonEnable(true,IBillButton.Save);
		setButtonEnable(false,HgPuBtnConst.OK);
		
	}
	
	public void onBoAudit() throws Exception {
		String invcode =null;
		BillCardPanel card = getBillCardPanelWrapper().getBillCardPanel();
		BillListPanel list = getBillListPanelWrapper().getBillListPanel();
		if(getBillManageUI().isListPanelSelected()){//�б�״̬
			int selectRow = list.getHeadTable().getSelectedRow();
			invcode=PuPubVO.getString_TrimZeroLenAsNull(list.getHeadBillModel().getValueAt(selectRow,"invcode"));
		}else{
			invcode=PuPubVO.getString_TrimZeroLenAsNull(card.getHeadItem("invcode").getValueObject());
		}
		if(invcode==null)
			throw new BusinessException("���ȱ����������");
		super.onBoAudit();
	}
	@Override
	protected String getHeadCondition() {
		PlanApplyInforVO appinfor = ((ClientUI)getBillUI()).m_appInfor;
		String pk_corp =_getCorp().getPrimaryKey();
		if(appinfor==null){
			getBillUI().showErrorMessage("��ȡ������Ϣʧ��!");
			return " pk_corp = '"+pk_corp+"' ";
		}
		if(pk_corp.equalsIgnoreCase(appinfor.getM_pocorp()))
				return null;
		return " pk_corp = '"+pk_corp+"' ";
	}
	
	public void onBoCommit() throws Exception{
		String invcode =null;
		BillCardPanel card = getBillCardPanelWrapper().getBillCardPanel();
		BillListPanel list = getBillListPanelWrapper().getBillListPanel();
		if(getBillManageUI().isListPanelSelected()){//�б�״̬
			int selectRow = list.getHeadTable().getSelectedRow();
			invcode=PuPubVO.getString_TrimZeroLenAsNull(list.getHeadBillModel().getValueAt(selectRow,"invcode"));
		}else{
			invcode=PuPubVO.getString_TrimZeroLenAsNull(card.getHeadItem("invcode").getValueObject());
		}
		if(invcode==null)
			throw new BusinessException("���ȱ�������ύ");
		super.onBoCommit();
	}

}

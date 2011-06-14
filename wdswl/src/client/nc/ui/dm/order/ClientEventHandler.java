package nc.ui.dm.order;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.pub.ListPanelPRTS;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.dm.order.SendorderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.ButtonCommon;
import nc.vo.wl.pub.WdsWlPubTool;

public class ClientEventHandler extends WdsPubEnventHandler {

	public ClientUIQueryDlg queryDialog = null;

	public ClientEventHandler(ClientUI clientUI, IControllerBase control) {
		super(clientUI, control);
	}

	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
			queryDialog = new ClientUIQueryDlg(
					
					getBillUI(),
					null,
					_getCorp().getPrimaryKey(),
					getBillUI()._getModuleCode(),
					_getOperator(),
					getBillUI().getBusinessType());
		}
		return queryDialog;
	}

	@Override
	protected String getHeadCondition() {
		return null;
	}
	
	@Override
	protected void onBoSave() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("itransstatus", 1);
		beforeSaveCheck();
		super.onBoSave();
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
		//mlr
		//�Է�����ʼ�ͽ������ڼ��飬�������ڲ��ܴ��ڽ�������
		//spf �޸�
		Object start=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("dbegindate").getValueObject();
		Object end=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("denddate").getValueObject();
		Object status = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("itransstatus").getValueObject();
       if(start ==null || "".equals(start)){
    	   throw new BusinessException("��ʼ���ڲ���Ϊ��");
    	   
       }
       if(status == null || "".equals(status)){
    	  throw new BusinessException("�˵�״̬����Ϊ�գ�");
       }else if("2".equals(status.toString())){
    	   if(end == null || "".equals(end)){
    		   throw new BusinessException("�˵�״̬Ϊ����!�������ڲ���Ϊ�գ�");
    	   }
    	   if(start.toString().compareTo(end.toString())>0){
        	   throw new BusinessException("��ʼ���ڲ��ܴ��ڽ�������");
           }	
       }
	}
	@Override
	protected void onBoLineAdd() throws Exception {
		// TODO Auto-generated method stub
		super.onBoLineAdd();
	}
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		if(intBtn == ButtonCommon.TRAN_COL){
			onBoCol();
		}
		if(intBtn == ButtonCommon.LOCK){
			onBoLock();
		}
		if(intBtn == ButtonCommon.UNLOCK){
			onBoUnlock();
		}
		super.onBoElse(intBtn);
	}
	@Override
	public void onBoAudit() throws Exception {
		if(getBufferData().getCurrentVO() ==null){
			getBillUI().showWarningMessage("����ѡ��һ������");
			return;
		}
		SendorderVO head = (SendorderVO)getBufferData().getCurrentVO().getParentVO();
		UFBoolean fisended = PuPubVO.getUFBoolean_NullAs(head.getFisended(), UFBoolean.FALSE);
		if(fisended == UFBoolean.FALSE ){
			getBillUI().showWarningMessage("������δ����");
			return ;
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
	
	
	/**
	 * liuys add ֧��������ӡ
	 */
	protected void onBoPrint() throws Exception {
		//��������б���棬ʹ��ListPanelPRTS����Դ
		if( getBillManageUI().isListPanelSelected() ){

			nc.ui.pub.print.IDataSource dataSource = new ListPanelPRTS(getBillUI()
					._getModuleCode(),((BillManageUI) getBillUI()).getBillListPanel());

			int[] rows = getBillManageUI().getBillListPanel().getHeadTable().getSelectedRows();
			
			if(rows == null || rows.length <= 1)
				super.onBoPrint();
			else{
				nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,
						dataSource);
				print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()
						._getModuleCode(), getBillUI()._getOperator(), getBillUI()
						.getBusinessType(), getBillUI().getNodeKey());

				BillModel bm = getBillManageUI().getBillListPanel().getBodyBillModel();
				UITable bt = getBillManageUI().getBillListPanel().getHeadTable();
				int len = rows.length;
				if (print.selectTemplate() == 1)
					for(int i = 0;i<len;i++){
						//					bm.setBodyDataVO(getBufferData().getVOByRowNo(i).getChildrenVO());
						//					bm.execLoadFormula();

						//					��������ӡ��������ʯ��  ���������仰�� ����
						bt.getSelectionModel().setSelectionInterval(i, i);
						print.print(true, false);
					}				
			}

		}
		// ����ǿ�Ƭ���棬ʹ��CardPanelPRTS����Դ
		else{
			super.onBoPrint();
		}
		Integer iprintcount =PuPubVO.getInteger_NullAs(getBufferData().getCurrentVO().getParentVO().getAttributeValue("iprintcount"), 0) ;
		iprintcount=iprintcount+1;
		getBufferData().getCurrentVO().getParentVO().setAttributeValue("iprintcount", iprintcount);
		HYPubBO_Client.update((SuperVO)getBufferData().getCurrentVO().getParentVO());
		onBoRefresh();	
	}	
	
//	@Override
//	protected void onBoPrint() throws Exception {
//		super.onBoPrint();
//		Integer iprintcount =PuPubVO.getInteger_NullAs(getBufferData().getCurrentVO().getParentVO().getAttributeValue("iprintcount"), 0) ;
//		iprintcount=iprintcount+1;
//		getBufferData().getCurrentVO().getParentVO().setAttributeValue("iprintcount", iprintcount);
//		HYPubBO_Client.update((SuperVO)getBufferData().getCurrentVO().getParentVO());
//		onBoRefresh();	
//	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ ����
	 * @ʱ�䣺2011-6-10����10:05:26
	 * @throws Exception
	 */
	private void onBoLock() throws Exception{
		AggregatedValueObject billVo = getBufferData().getCurrentVO();
		if(billVo == null){
			getBillUI().showWarningMessage("��ѡ��Ҫ����������");
		}
		SendorderVO head = (SendorderVO)billVo.getParentVO();
		UFBoolean fisended = PuPubVO.getUFBoolean_NullAs(head.getFisended(), UFBoolean.FALSE);
		if(fisended == UFBoolean.TRUE ){
			return ;
		}
		head.setFisended(UFBoolean.TRUE);
		HYPubBO_Client.update(head);
		onBoRefresh();
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ  �ⶳ
	 * @ʱ�䣺2011-6-10����10:05:39
	 * @throws Exception
	 */
	private void onBoUnlock() throws Exception{
		AggregatedValueObject billVo = getBufferData().getCurrentVO();
		if(billVo == null){
			getBillUI().showWarningMessage("��ѡ��Ҫ����������");
		}
		SendorderVO head = (SendorderVO)billVo.getParentVO();
		UFBoolean fisended = PuPubVO.getUFBoolean_NullAs(head.getFisended(), UFBoolean.FALSE);
		if(fisended == UFBoolean.FALSE ){
			return ;
		}
		head.setFisended(UFBoolean.FALSE);
		HYPubBO_Client.update(head);
		onBoRefresh();
	}
	
	/**
	 * �˷Ѽ���
	 */
	private void onBoCol(){
		if(getBufferData().isVOBufferEmpty()){
			getBillUI().showHintMessage("������");
			return;
		}
		AggregatedValueObject  billvo = getBufferData().getCurrentVO();
		if(billvo == null)
			return;
		if(billvo.getChildrenVO() == null || billvo.getChildrenVO().length == 0){
			getBillUI().showHintMessage("��������Ϊ��");
			return;
		}
		try{	
			billvo = TranColHelper.col(getBillUI(), billvo, _getDate(),_getOperator());
			if(getBillManageUI().isListPanelSelected()){
				onBoReturn();
			}

		}catch(Exception e){
			e.printStackTrace();
			getBillUI().showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}

		getBillCardPanelWrapper().getBillCardPanel().getBillData().setBillValueVO(billvo);
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
		getBufferData().setCurrentVO(billvo);		
	}

}
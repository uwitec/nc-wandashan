package nc.ui.wds2.send;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.pub.CardPanelPRTS;
import nc.ui.trade.pub.ListPanelPRTS;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.Wds2WlPubConst;

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

	
	protected String getHeadCondition() {
		// ��˾
		String whereSql=null;
		whereSql=super.getHeadCondition();
		if(whereSql==null || whereSql.length()==0){		
			whereSql=" pk_billtype='"+Wds2WlPubConst.billtype_alloinsendorder+"'";
		}else{
			whereSql=whereSql+" and pk_billtype='"+Wds2WlPubConst.billtype_alloinsendorder+"'";
		}		
		return whereSql;
	}
	@Override
	protected void onBoSave() throws Exception {
//		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("itransstatus", 1);
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
//	@Override
//	protected void onBoElse(int intBtn) throws Exception {
//		if(intBtn == ButtonCommon.TRAN_COL){
//			onBoCol();
//		}
//		if(intBtn == ButtonCommon.LOCK){
//			onBoLock();
//		}
//		if(intBtn == ButtonCommon.UNLOCK){
//			onBoUnlock();
//		}
//		super.onBoElse(intBtn);
//	}
//	@Override
//	public void onBoAudit() throws Exception {
//		if(getBufferData().getCurrentVO() ==null){
//			getBillUI().showWarningMessage("����ѡ��һ������");
//			return;
//		}
//		SendorderVO head = (SendorderVO)getBufferData().getCurrentVO().getParentVO();
//		UFBoolean fisended = PuPubVO.getUFBoolean_NullAs(head.getFisended(), UFBoolean.FALSE);
//		if(fisended == UFBoolean.FALSE ){
//			getBillUI().showWarningMessage("������δ����");
//			return ;
//		}
//	
//		super.onBoAudit();
//	}
//	@Override
//	public void onBoCancelAudit()throws Exception{
//		if(getBufferData().getCurrentVO() ==null){
//			getBillUI().showWarningMessage("����ѡ��һ������");
//			return;
//		}
//		super.onBoCancelAudit();
//	}
	
	
	/**
	 * liuys add ֧��������ӡ
	 */
	protected void onBoPrint() throws Exception {
		//��������б����棬ʹ��ListPanelPRTS����Դ
		if( getBillManageUI().isListPanelSelected() ){
			nc.ui.pub.print.IDataSource dataSource = new ListPanelPRTS(
					getBillUI()._getModuleCode(),((BillManageUI) getBillUI()).getBillListPanel());
			int[] rows = getBillManageUI().getBillListPanel().getHeadTable().getSelectedRows();
			if(rows == null || rows.length<=0){
				return;
			}
			int rowstart = getBillManageUI().getBillListPanel().getHeadTable().getSelectedRow();
				nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,
						dataSource);
				print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()
						._getModuleCode(), getBillUI()._getOperator(), getBillUI()
						.getBusinessType(), getBillUI().getNodeKey());
				UITable bt = getBillManageUI().getBillListPanel().getHeadTable();
				int len = rows.length;
				if (print.selectTemplate() == 1)
					for(int i = 0;i<len;i++){
						bt.getSelectionModel().setSelectionInterval(rowstart+i, rowstart+i);
						print.print(true, false);
						Integer iprintcount =PuPubVO.getInteger_NullAs(getBufferData().getCurrentVO().getParentVO().getAttributeValue("iprintcount"), 0) ;
						iprintcount=iprintcount+1;
						getBufferData().getCurrentVO().getParentVO().setAttributeValue("iprintcount", iprintcount);
						HYPubBO_Client.update((SuperVO)getBufferData().getCurrentVO().getParentVO());
						onBoRefresh();				
			}

		}else{
			//��������Դ��֧��ͼƬ
			nc.ui.pub.print.IDataSource dataSource = new CardPanelPRTS(getBillUI()
					._getModuleCode(), getBillCardPanelWrapper().getBillCardPanel());
			nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,
					dataSource);
			print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()
					._getModuleCode(), getBillUI()._getOperator(), getBillUI()
					.getBusinessType(), getBillUI().getNodeKey());
			if (print.selectTemplate() == 1)
				print.preview();
			//��������Դ��֧��ͼƬ
			Integer iprintcount =PuPubVO.getInteger_NullAs(getBufferData().getCurrentVO().getParentVO().getAttributeValue("iprintcount"), 0) ;
			iprintcount=iprintcount+1;
			getBufferData().getCurrentVO().getParentVO().setAttributeValue("iprintcount", iprintcount);
			HYPubBO_Client.update((SuperVO)getBufferData().getCurrentVO().getParentVO());
			onBoRefresh();
		}	
	}		

}
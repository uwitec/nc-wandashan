package nc.ui.dm.order;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.querytemplate.TemplateInfo;

public class ClientEventHandler extends WdsPubEnventHandler {

	public ClientUIQueryDlg queryDialog = null;

	public ClientEventHandler(ClientUI clientUI, IControllerBase control) {
		super(clientUI, control);
	}

	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
			TemplateInfo tempinfo = new TemplateInfo();
			tempinfo.setPk_Org(_getCorp().getPk_corp());
			tempinfo.setCurrentCorpPk(_getCorp().getPk_corp());
			tempinfo.setFunNode(getBillUI()._getModuleCode());
			tempinfo.setUserid(getBillUI()._getOperator());
			queryDialog = new ClientUIQueryDlg(getBillUI(), null, tempinfo);
		}
		return queryDialog;
	}

	@Override
	protected String getHeadCondition() {
		return null;
	}
	
	@Override
	protected void onBoSave() throws Exception {
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
		Object start=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("dbegindate").getValueObject();
		Object end=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("denddate").getValueObject();
       if(start ==null || end==null
    		   ||"".equals(start)||"".equals(end)){
    	   throw new BusinessException("��ʼ�ͽ������ڲ���Ϊ��");
    	   
       }else if(start.toString().compareTo(end.toString())>0){
    	   
    	   throw new BusinessException("��ʼ���ڲ��ܴ��ڽ�������");
       }
    		  
		
		
		//����ǿ�У��
		if(getBillUI().getVOFromUI()!=null){
			if(getBillUI().getVOFromUI().getChildrenVO()==null||
					getBillUI().getVOFromUI().getChildrenVO().length==0	){
				throw new BusinessException("���岻����Ϊ��");
			}else{
				super.beforeSaveBodyUnique(new String[]{"pk_invbasdoc"});
			}
		};
				
		
	}
	@Override
	protected void onBoLineAdd() throws Exception {
		// TODO Auto-generated method stub
		super.onBoLineAdd();
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		
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
}
package nc.ui.dm.plan;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.dm.SendplaninVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.wds.dm.sendinvdoc.SendinvdocVO;

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
		if(getBillUI().getVOFromUI()!=null){
			if(getBillUI().getVOFromUI().getChildrenVO()==null||
					getBillUI().getVOFromUI().getChildrenVO().length==0	){
				throw new BusinessException("���岻����Ϊ��");
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
		super.onBoElse(intBtn);
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
	 SuperVO[] vos= HYPubBO_Client.queryByCondition(SendinvdocVO.class, "  isnull(dr,0)=0  order by crow");	
	 super.onBoAdd(bo);	 	 	 
	 getBillCardPanelWrapper().getBillCardPanel().getBillData().setBodyValueVO(vos);
	 getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
	}
}






















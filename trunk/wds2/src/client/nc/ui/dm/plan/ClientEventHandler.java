package nc.ui.dm.plan;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.wl.pub.WdsWlPubTool;

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
		String whereSql = null;
		try{
			String cwhid = LoginInforHelper.getLogInfor(_getOperator()).getWhid();
			if(!WdsWlPubTool.isZc(cwhid)){//非总仓人员登陆  只能查询 发货仓库为自身的发运计划
				whereSql=" and wds_sendplanin.pk_outwhouse = '"+cwhid+"'";
			};
		}catch(Exception e){
			e.printStackTrace();
			getBillUI().showErrorMessage(e.getMessage());
		}
		return whereSql;
	}
	
	@Override
	protected void onBoSave() throws Exception {
		beforeSaveCheck();
		super.onBoSave();
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 表体保存前校验
	 * @时间：2011-3-23下午09:05:20
	 * @throws Exception
	 */
	protected void beforeSaveCheck() throws Exception{
		if(getBillUI().getVOFromUI()!=null){
			if(getBillUI().getVOFromUI().getChildrenVO()==null||
					getBillUI().getVOFromUI().getChildrenVO().length==0	){
				throw new BusinessException("表体不允许为空");
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
			getBillUI().showWarningMessage("请先选择一条数据");
			return;
		}
		super.onBoAudit();
	}
	@Override
	public void onBoCancelAudit()throws Exception{
		if(getBufferData().getCurrentVO() ==null){
			getBillUI().showWarningMessage("请先选择一条数据");
			return;
		}
		super.onBoCancelAudit();
	}
}
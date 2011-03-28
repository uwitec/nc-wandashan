package nc.ui.dm.order;

import java.util.Date;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
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
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 表体保存前校验
	 * @时间：2011-3-23下午09:05:20
	 * @throws Exception
	 */
	protected void beforeSaveCheck() throws Exception{
		//mlr
		//对发货开始和结束日期检验，发货日期不能大于结束日期
		Object start=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("dbegindate").getValueObject();
		Object end=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("denddate").getValueObject();
       if(start ==null || end==null
    		   ||"".equals(start)||"".equals(end)){
    	   throw new BusinessException("开始和结束日期不能为空");
    	   
       }else if(start.toString().compareTo(end.toString())>0){
    	   
    	   throw new BusinessException("开始日期不能大于结束日期");
       }
    		  
		
		
		//表体非空校验
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
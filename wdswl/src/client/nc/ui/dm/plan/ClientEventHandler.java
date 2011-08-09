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
//			if(!WdsWlPubTool.isZc(cwhid)){//非总仓人员登陆  只能查询 发货仓库为自身的发运计划
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
			MessageDialog.showErrorDlg(getBillUI(), "校验", e.getMessage());
			return;
		}
		
		AggregatedValueObject billVO = getBillUI().getChangedVOFromUI();
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);
		
//		zhf add 校验
		SendplaninBVO[] bodys = (SendplaninBVO[])checkVO.getChildrenVO();
		if(bodys==null || bodys.length ==0)
			throw new BusinessException("表体数据不能为空");
		for(SendplaninBVO body:bodys)
			body.validationOnSave();
//		zhf end
		
		// 进行数据晴空
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

		// 判断是否有存盘数据
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

		// 进行数据恢复处理
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
			// 新增后操作处理
			setAddNewOperate(isAdding(), billVO);
		}
		// 设置保存后状态
		setSaveOperateState();
		if(nCurrentRow>=0){
			getBufferData().setCurrentRow(nCurrentRow);
		}
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
			}
		AggregatedValueObject vo=getBillUI().getVOFromUI();
		//只对追加计划校验 计划数量不允许都为空
		if(vo.getParentVO()==null){
			return;
		}
		Integer planType=PuPubVO.getInteger_NullAs(vo.getParentVO().getAttributeValue("iplantype"),new Integer(3));
		if(planType.intValue()==1){
		BeforeSaveValudate.checkNotAllNulls(getBillUI().getVOFromUI(),new String[]{"nplannum","nassplannum"}, new String[]{"计划数量","计划辅数量"});	
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
 * @作者：zhf
 * @说明：完达山物流项目 整单关闭  不支持列表批操作
 * @时间：2011-6-25下午06:19:49
 */
	private void closeBill(){
		if(getBufferData().isVOBufferEmpty()){
			getBillUI().showWarningMessage("无数据");
			return;
		}
		if(getBufferData().getCurrentRow()<0){
			getBillUI().showWarningMessage("无数据");
			return;
		}
		
		int flag = MessageDialog.showOkCancelDlg(getBillUI(), "询问", "确认关闭当前发运计划吗?"); 
		if(flag != UIDialog.ID_OK){
			getBillUI().showHintMessage("取消了关闭操作");
			return;
		}
		
		HYBillVO bill = (HYBillVO)getBufferData().getCurrentVO();
		
		HYBillVO newbill = null;
//		后台关闭计划
	   try {
		newbill = PlanDealHealper.closeBill(bill.getParentVO().getPrimaryKey());
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		getBillUI().showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
		return;
	}
		
//		关闭成功前台数据处理   更新缓存  跟新 卡片界面
		if(newbill == null){
			getBillUI().showWarningMessage("关闭操作成功,刷新前台数据失败");
			return;
		}
		
		getBufferData().setCurrentVO(newbill);
		getBillCardPanelWrapper().getBillCardPanel().getBillData().setBillValueVO(newbill);
		
//		后台关闭计划
		
//		关闭成功前台数据处理   更新缓存  跟新 卡片界面
		
		
//		int[] rows = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRows();
//		if(rows == null || rows.length ==0){
//			getBillUI().showWarningMessage("");
//		}
			
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 行关闭  卡片下  的选中多行进行关闭
	 * @时间：2011-6-25下午06:20:15
	 */
	private void closeRow(){
		if(getBufferData().isVOBufferEmpty()){
			getBillUI().showWarningMessage("无数据");
			return;
		}
		if(getBufferData().getCurrentRow()<0){
			getBillUI().showWarningMessage("无数据");
			return;
		}
		
		int[] rows = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRows();
		if(rows == null || rows.length ==0){
			getBillUI().showWarningMessage("请选择要关闭的行");
			return;
		}
		
		int flag = MessageDialog.showOkCancelDlg(getBillUI(), "询问", "确认关闭选中行吗?"); 
		if(flag != UIDialog.ID_OK){
			getBillUI().showHintMessage("取消了行关闭操作");
			return;
		}
		
//		获取数据
	    java.util.List<String> ldata = new ArrayList<String>();
	    HYBillVO bill = (HYBillVO)getBufferData().getCurrentVO();
	    if(bill == null){

			getBillUI().showWarningMessage("无数据");
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
//		后台关闭计划
	   try {
		newbill = PlanDealHealper.closeRows(ldata);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		getBillUI().showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
		return;
	}
		
//		关闭成功前台数据处理   更新缓存  跟新 卡片界面
		if(newbill == null){
			getBillUI().showWarningMessage("关闭操作成功,刷新前台数据失败");
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
	
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {	
		//增加pk_corp
	 SuperVO[] vos= HYPubBO_Client.queryByCondition(SendinvdocVO.class, "pk_corp = '"+_getCorp().getPrimaryKey()+"' and  isnull(dr,0)=0  order by crow");	
	 super.onBoAdd(bo);	 	 	 
	 getBillCardPanelWrapper().getBillCardPanel().getBillData().setBodyValueVO(vos);
	 getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
	}
}






















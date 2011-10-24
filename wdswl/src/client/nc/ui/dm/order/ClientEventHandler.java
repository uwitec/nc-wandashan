package nc.ui.dm.order;


import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wl.pub.BeforeSaveValudate;
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

//	@Override
//	protected String getHeadCondition() {
//		return null;
//	}
	
	@Override
	protected void onBoSave() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("itransstatus", 1);
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
		//spf 修改
		Object start=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("dbegindate").getValueObject();
		Object end=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("denddate").getValueObject();
		Object status = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("itransstatus").getValueObject();
       if(start ==null || "".equals(start)){
    	   throw new BusinessException("开始日期不能为空");
    	   
       }
       if(status == null || "".equals(status)){
    	  throw new BusinessException("运单状态不能为空！");
       }else if("2".equals(status.toString())){
    	   if(end == null || "".equals(end)){
    		   throw new BusinessException("运单状态为到货!结束日期不能为空！");
    	   }
    	   if(start.toString().compareTo(end.toString())>0){
        	   throw new BusinessException("开始日期不能大于结束日期");
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
			getBillUI().showWarningMessage("请先选择一条数据");
			return;
		}
		SendorderVO head = (SendorderVO)getBufferData().getCurrentVO().getParentVO();
		UFBoolean fisended = PuPubVO.getUFBoolean_NullAs(head.getFisended(), UFBoolean.FALSE);
		if(fisended == UFBoolean.FALSE ){
			getBillUI().showWarningMessage("单据尚未冻结");
			return ;
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
	
	
	/**
	 * liuys add 支持批量打印
	 */
	protected void onBoPrint() throws Exception {
		//　如果是列表界面，使用ListPanelPRTS数据源
		if( getBillManageUI().isListPanelSelected() ){
			nc.ui.pub.print.IDataSource dataSource = new MyListDateSource(getBillUI()
					._getModuleCode(),((BillManageUI) getBillUI()).getBillListPanel());
			int[] rows = getBillManageUI().getBillListPanel().getHeadTable().getSelectedRows();
			int rowstart = getBillManageUI().getBillListPanel().getHeadTable().getSelectedRow();
			if(rows == null || rows.length <= 1){				
				super.onBoPrint();
				if(rows.length == 1){
				//单行打印 打印数量处理
				Integer iprintcount =PuPubVO.getInteger_NullAs(getBufferData().getCurrentVO().getParentVO().getAttributeValue("iprintcount"), 0) ;
				iprintcount=iprintcount+1;
				getBufferData().getCurrentVO().getParentVO().setAttributeValue("iprintcount", iprintcount);
				HYPubBO_Client.update((SuperVO)getBufferData().getCurrentVO().getParentVO());
				onBoRefresh();	
				}
			}		
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

						//					如果表体打印不正常玉石可  把上面两句话打开 试试
						bt.getSelectionModel().setSelectionInterval(rowstart+i, rowstart+i);
						print.print(true, false);
						Integer iprintcount =PuPubVO.getInteger_NullAs(getBufferData().getCurrentVO().getParentVO().getAttributeValue("iprintcount"), 0) ;
						iprintcount=iprintcount+1;
						getBufferData().getCurrentVO().getParentVO().setAttributeValue("iprintcount", iprintcount);
						HYPubBO_Client.update((SuperVO)getBufferData().getCurrentVO().getParentVO());
						onBoRefresh();	
					}				
			}

		}// 如果是卡片界面，使用CardPanelPRTS数据源
//		else{
//			super.onBoPrint();
//		}
		else{
			//更改数据源，支持图片
			nc.ui.pub.print.IDataSource dataSource = new MyCardDateSource(getBillUI()
					._getModuleCode(), getBillCardPanelWrapper().getBillCardPanel());
			nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,
					dataSource);
			print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()
					._getModuleCode(), getBillUI()._getOperator(), getBillUI()
					.getBusinessType(), getBillUI().getNodeKey());
			if (print.selectTemplate() == 1)
				print.preview();
			//更改数据源，支持图片
			Integer iprintcount =PuPubVO.getInteger_NullAs(getBufferData().getCurrentVO().getParentVO().getAttributeValue("iprintcount"), 0) ;
			iprintcount=iprintcount+1;
			getBufferData().getCurrentVO().getParentVO().setAttributeValue("iprintcount", iprintcount);
			HYPubBO_Client.update((SuperVO)getBufferData().getCurrentVO().getParentVO());
			onBoRefresh();
		}	
	}		
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 冻结
	 * @时间：2011-6-10下午10:05:26
	 * @throws Exception
	 */
	private void onBoLock() throws Exception{
		AggregatedValueObject billVo = getBufferData().getCurrentVO();
		if(billVo == null){
			getBillUI().showWarningMessage("请选择要操作的数据");
		}
		BeforeSaveValudate.checkNotAllNull(billVo,"noutnum","实发数量");
		SendorderVO head = (SendorderVO)billVo.getParentVO();
		UFBoolean fisended = PuPubVO.getUFBoolean_NullAs(head.getFisended(), UFBoolean.FALSE);
		if(fisended == UFBoolean.TRUE ){
			return ;
		}
		head.setFisended(UFBoolean.TRUE);	
		HYPubBO_Client.update(head);
		onBoRefresh();		
	}


	@Override
	public void onButton(ButtonObject bo){
		
	   // 冻结后 不允许单据修改的处理
		Object o = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"fisended").getValueObject();
		ButtonObject parentBtn = bo.getParent();
		if (parentBtn != null ) {
			int intParentBtn = Integer.parseInt(parentBtn.getTag());
			if(IBillButton.Action == intParentBtn){
				if (o != null && ((String) o).equalsIgnoreCase("true")) {
					getBillUI().showErrorMessage("该单据已冻结不允许操作");
					return;

				}
			}

		} else {
			Integer intbtn = Integer.valueOf(bo.getTag());
			if (IBillButton.Edit == intbtn || IBillButton.Del == intbtn
					|| IBillButton.Delete == intbtn
					
					|| ButtonCommon.TRAN_COL == intbtn) {
				if (o != null && ((String) o).equalsIgnoreCase("true")) {
					getBillUI().showErrorMessage("该单据已冻结不允许操作");
					return;

				}
			}

		}
		super.onButton(bo);
	}
	

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目  解冻
	 * @时间：2011-6-10下午10:05:39
	 * @throws Exception
	 */
	private void onBoUnlock() throws Exception{
		AggregatedValueObject billVo = getBufferData().getCurrentVO();
		if(billVo == null){
			getBillUI().showWarningMessage("请选择要操作的数据");
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
	 * 运费计算
	 */
	private void onBoCol(){
		if(getBufferData().isVOBufferEmpty()){
			getBillUI().showHintMessage("无数据");
			return;
		}
		AggregatedValueObject  billvo = getBufferData().getCurrentVO();
		if(billvo == null)
			return;
		if(billvo.getChildrenVO() == null || billvo.getChildrenVO().length == 0){
			getBillUI().showHintMessage("表体数据为空");
			return;
		}
		SendorderVO head =(SendorderVO) getBufferData().getCurrentVO().getParentVO();
		if(PuPubVO.getInteger_NullAs(head.getIcoltype(), 0)==3)//手动核算运费
			return ;
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
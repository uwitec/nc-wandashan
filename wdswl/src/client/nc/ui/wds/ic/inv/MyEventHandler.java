package nc.ui.wds.ic.inv;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wds.tray.lock.LockTrayHelper;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.WdsSelfButton;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;


/**
 * 
 * 存货状态
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	MyClientUI myClientUI = null;
	


	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;
	}



	@Override
	protected UIDialog createQueryUI() {
		
		return new MyQueryDIG(getBillUI() , null,getBillUI()._getCorp().getPrimaryKey(), getBillUI()._getModuleCode(),getBillUI()._getOperator(),null);
	}
	
	@Override
	protected void onBoSave() throws Exception {
		super.onBoSave();
		doRefresh();
	}
	protected void doRefresh() throws Exception {
		if(PuPubVO.getString_TrimZeroLenAsNull(whereSql)==null)
			getBillUI().showHintMessage("刷新操作结束");
		int row = getBufferData().getCurrentRow();
		SuperVO[] queryVos = queryHeadVOs(whereSql);

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
		if(((BillManageUI)getBillUI()).isListPanelSelected())
			return;
		onBoReturn();
		if(row<getBufferData().getVOBufferSize()){
			((BillManageUI)getBillUI()).getBillListPanel().getHeadTable().getSelectionModel().setSelectionInterval(row, row);
		}
	}
	private String whereSql;
	protected void onBoQuery() throws Exception {

		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		whereSql = strWhere.toString();
		String[] sql=whereSql.split(" and ");
		StringBuffer strWhere1 = new StringBuffer();
		int leng=sql.length;
        for(int i=0;i<sql.length;i++){
//        	leng--;
        	if("(tb_warehousestock.whs_stocktonnage = 'N'))".equals(sql[i].trim())){
        		strWhere1.append(" (tb_warehousestock.whs_stocktonnage >0)) and ");
        		continue;
        	}
        	if("(tb_warehousestock.whs_stocktonnage = 'Y'))".equals(sql[i].trim())){
        		strWhere1.append(" (tb_warehousestock.whs_stocktonnage >-100000)) and ");
        	}else{
               	strWhere1.append(sql[i]);
               	if(leng-(i+1)>0){
               		strWhere1.append(" and ");
               	}
        	}
        	
        }
		SuperVO[] queryVos = queryHeadVOs(strWhere1.toString());

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();

	}
	
	protected void onBoElse(int intBtn) throws Exception {
		if(intBtn == WdsSelfButton.view_lock){
			onViewLock();
		}
		//库存状态 清理
		if(intBtn == WdsSelfButton.clean_zero){
			onCleanZero();
		}
 	}
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 查看绑定信息
	 * @时间：2011-7-19上午10:19:37
	 */
	private void onViewLock() throws Exception{
		//		必须选中数据   非虚拟托盘  不能查看
		if(getBufferData().isVOBufferEmpty())
			getBillUI().showHintMessage("数据为空");
		AggregatedValueObject bill = getBufferData().getCurrentVO();
		if(bill == null || bill.getParentVO()==null)
			getBillUI().showHintMessage("数据为空");
		StockInvOnHandVO vo = (StockInvOnHandVO)bill.getParentVO();
		UFBoolean islock = LockTrayHelper.isLock(vo.getPplpt_pk(), vo.getPk_invmandoc(), vo.getWhs_batchcode());
		if(!islock.booleanValue()){
			throw new BusinessException("不存在绑定关系");
		}
		getViewLockDlg().setID(vo.getPplpt_pk());
		getViewLockDlg().loadHeadData();
		getViewLockDlg().showModal();
	}
	
	//库存状态 清理()
	private CleanQueryDlg m_cleanQueryDlg = null;
	
	private CleanQueryDlg getCleanQueryDlg(){
		if(m_cleanQueryDlg == null){
			//parent, normalPnl, pk_corp, moduleCode, operator, busiType
			//moduleCode = 800404060201
			m_cleanQueryDlg = new CleanQueryDlg(getBillUI());
			m_cleanQueryDlg.setTempletID(WdsWlPubConst.INV_STATE_QRY_TEMPLET_ID);
			m_cleanQueryDlg.hideNormal();
			m_cleanQueryDlg.hideUnitButton();
		}
		return m_cleanQueryDlg;
	}
	/**
	 * 
	 * @作者：yf
	 * @说明：完达山物流项目 
	 * @时间：2011-8-4下午06:09:23
	 * @throws Exception
	 */
	private void onCleanZero() throws Exception{
		//如果有无效数据，弹出查询对话框--自定义查询模板CleanQueryDlg		 
		//忽略 确定 以外的事件
		if(getCleanQueryDlg().showModal()!=UIDialog.ID_OK){
			return;
		}
		//确定获得条件语句 模板编号=800404060201	模板id=0001S3100000000L231C	
		ConditionVO[] vos =getCleanQueryDlg().getConditionVO();
		try {		
			int i = cleanZero(vos);
			getBillUI().showWarningMessage("成功清理" + i + "条无效数据");
			if(i > 0){
				doRefresh();
			}
				
		} catch (Exception e) {
			throw new BusinessException(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
		}
		
	}	
		
	
	private Integer cleanZero(ConditionVO[] vos) throws Exception {		
		
		Class[] ParameterTypes = new Class[] { ConditionVO[].class };
		Object[] ParameterValues = new Object[] { vos};
		//远程调用- 等待提示框- 后台bo类- cleanStockZero 方法 
		Object os = LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, getBillUI(),
				"正在清理无效库存...", 2, "nc.bs.wds.ic.stock.StockInvOnHandBO", null, "cleanStockZero", ParameterTypes,
				ParameterValues);
		return Integer.parseInt(os.toString());
	}
	
	private ViewLockDLG m_viewLockDlg = null;
	
	private ViewLockDLG getViewLockDlg(){
		if(m_viewLockDlg ==  null)
			m_viewLockDlg = new ViewLockDLG(getBillUI(),getBillUI()._getOperator(),getBillUI()._getCorp().getPrimaryKey());
		return m_viewLockDlg;
	}
}
package nc.ui.wds.ic.inv;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wds.tray.lock.LockTrayHelper;
import nc.ui.wl.pub.WdsSelfButton;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;


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
		SuperVO[] queryVos = queryHeadVOs(whereSql);

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();

	}
	
	protected void onBoElse(int intBtn) throws Exception {
		if(intBtn == WdsSelfButton.view_lock){
			onViewLock();
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
	
	private ViewLockDLG m_viewLockDlg = null;
	private ViewLockDLG getViewLockDlg(){
		if(m_viewLockDlg ==  null)
			m_viewLockDlg = new ViewLockDLG(getBillUI(),getBillUI()._getOperator(),getBillUI()._getCorp().getPrimaryKey());
		return m_viewLockDlg;
	}
}
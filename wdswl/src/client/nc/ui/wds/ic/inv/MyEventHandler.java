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
 * ���״̬
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
			getBillUI().showHintMessage("ˢ�²�������");
		int row = getBufferData().getCurrentRow();
		SuperVO[] queryVos = queryHeadVOs(whereSql);

		getBufferData().clear();
		// �������ݵ�Buffer
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
			return;// �û������˲�ѯ

		whereSql = strWhere.toString();
		SuperVO[] queryVos = queryHeadVOs(whereSql);

		getBufferData().clear();
		// �������ݵ�Buffer
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
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ �鿴����Ϣ
	 * @ʱ�䣺2011-7-19����10:19:37
	 */
	private void onViewLock() throws Exception{
		//		����ѡ������   ����������  ���ܲ鿴
		if(getBufferData().isVOBufferEmpty())
			getBillUI().showHintMessage("����Ϊ��");
		AggregatedValueObject bill = getBufferData().getCurrentVO();
		if(bill == null || bill.getParentVO()==null)
			getBillUI().showHintMessage("����Ϊ��");
		StockInvOnHandVO vo = (StockInvOnHandVO)bill.getParentVO();
		UFBoolean islock = LockTrayHelper.isLock(vo.getPplpt_pk(), vo.getPk_invmandoc(), vo.getWhs_batchcode());
		if(!islock.booleanValue()){
			throw new BusinessException("�����ڰ󶨹�ϵ");
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
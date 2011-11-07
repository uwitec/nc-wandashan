package nc.ui.wds.ic.write.back4c;

import java.util.ArrayList;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wds.w80020206.buttun0206.ISsButtun;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.ic.write.back4c.MultiBillVO;
import nc.vo.wds.ic.write.back4c.Writeback4cHVO;

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
		}
		return queryDialog;
	}
	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ
		AggregatedValueObject[]  bodyVOs =HYPubBO_Client.queryBillVOByCondition(getUIController().getBillVoName(), strWhere.toString());
		int count = 0;
		getBufferData().clear();
		if(bodyVOs != null ){
			count = bodyVOs.length;
			getBufferData().addVOsToBuffer(bodyVOs);
			updateBuffer();
		}
		getBillUI().showHintMessage("��ѯ��ɣ�����ѯ��"+count+"������");
	}
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		if(intBtn == ISsButtun.all_selected){
			onAllSelect();
		}else if(intBtn == ISsButtun.all_not_selected){
			onAllNoSelect();
		}
	}
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵����ȫѡ
	 * @ʱ�䣺2011-9-18����09:19:18
	 */
	protected void onAllSelect() {
		int count = getBillListPanel().getBillListData().getHeadBillModel().getRowCount();
		for(int row=0;row<count;row++){
			getBillListPanel().getBillListData().getHeadBillModel().setValueAt(UFBoolean.TRUE, row, "fselect");
		}
		ArrayList<MultiBillVO> m_VOBuffer =(ArrayList<MultiBillVO>)getBufferData().getRelaSortObject();
		if(m_VOBuffer != null){
			for(int i =0;i<m_VOBuffer.size();i++){
				MultiBillVO bill = m_VOBuffer.get(i);
				bill.getHeadVO().setFselect(UFBoolean.TRUE);
			}
		}
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵����ȫ��
	 * @ʱ�䣺2011-9-18����09:19:28
	 */
	protected void onAllNoSelect() {
		int count = getBillListPanel().getBillListData().getHeadBillModel().getRowCount();
		for(int row=0;row<count;row++){
			getBillListPanel().getBillListData().getHeadBillModel().setValueAt(null, row, "fselect");

		}
		ArrayList<MultiBillVO> m_VOBuffer =(ArrayList<MultiBillVO>)getBufferData().getRelaSortObject();
		if(m_VOBuffer != null){
			for(int i =0;i<m_VOBuffer.size();i++){
				MultiBillVO bill = m_VOBuffer.get(i);
				bill.getHeadVO().setFselect(UFBoolean.FALSE);
			}
		}
	}
	@Override
	public void onBoAudit() throws Exception {
		if(getBillManageUI().isListPanelSelected()){
			ArrayList<MultiBillVO> select = new ArrayList<MultiBillVO>();
			ArrayList<MultiBillVO> m_VOBuffer =(ArrayList<MultiBillVO>)getBufferData().getRelaSortObject();
			if(m_VOBuffer != null && m_VOBuffer.size()>0){
				for(int i =0;i<m_VOBuffer.size();i++){
					MultiBillVO bill = m_VOBuffer.get(i);
					UFBoolean fselsect = PuPubVO.getUFBoolean_NullAs(bill.getHeadVO().getFselect(), UFBoolean.FALSE);
					if(fselsect.booleanValue()){
						select.add(bill);
					}
				}
			}
			StringBuffer bur = new StringBuffer();
			for(int i=0;i<select.size();i++){
				getBufferData().setCurrentVO(select.get(i));
				Writeback4cHVO hvo = (Writeback4cHVO)getBufferData().getCurrentVO().getParentVO();
				String vbillno =hvo.getVbillno()== null?"":hvo.getVbillno();
				try{
					super.onBoAudit();
				}catch(Exception e){
					e.printStackTrace();
					bur.append(vbillno+":"+e.getMessage()+"\n");
				}
			}
			if(bur.toString() != null && !"".equalsIgnoreCase(bur.toString())){
				getBillUI().showErrorMessage("���ʧ�ܵĵ��ݣ�\n"+bur.toString());
			}
		}else{
			super.onBoAudit();
		}
		
	}
	@Override
	protected void onBoCancelAudit() throws Exception {
		ArrayList<MultiBillVO> select = new ArrayList<MultiBillVO>();
		ArrayList<MultiBillVO> m_VOBuffer =(ArrayList<MultiBillVO>)getBufferData().getRelaSortObject();
		if(m_VOBuffer != null && m_VOBuffer.size()>0){
			for(int i =0;i<m_VOBuffer.size();i++){
				MultiBillVO bill = m_VOBuffer.get(i);
				UFBoolean fselsect = PuPubVO.getUFBoolean_NullAs(bill.getHeadVO().getFselect(), UFBoolean.FALSE);
				if(fselsect.booleanValue()){
					select.add(bill);
				}
			}
		}
		StringBuffer bur = new StringBuffer();
		for(int i=0;i<select.size();i++){
			getBufferData().setCurrentVO(select.get(i));
			Writeback4cHVO hvo = (Writeback4cHVO)getBufferData().getCurrentVO().getParentVO();
			String vbillno =hvo.getVbillno()== null?"":hvo.getVbillno();
			try{
				super.onBoCancelAudit();
			}catch(Exception e){
				e.printStackTrace();
				bur.append(vbillno+":"+e.getMessage()+"\n");
			}
		}
		if(bur.toString() != null && !"".equalsIgnoreCase(bur.toString())){
			getBillUI().showErrorMessage("����ʧ�ܵĵ��ݣ�\n"+bur.toString());
		}
	}
	@Override
	protected void onBoSave() throws Exception {
		super.onBoSave();
	}
}

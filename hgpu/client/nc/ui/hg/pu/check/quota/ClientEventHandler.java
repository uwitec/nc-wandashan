package nc.ui.hg.pu.check.quota;

import nc.ui.hg.pu.pub.DBTManageEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.PlanApplyInforVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public class ClientEventHandler extends DBTManageEventHandler {

	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
		//initAppInfor();
	}
	@Override
	protected UIDialog createQueryUI() {
		return new ClientQueryDLG(getBillUI(), null,
				_getCorp().getPrimaryKey(), getBillUI().getModuleCode(),
				_getOperator(), null, null);
	}
	
		private void setBodyItemsEdit(){
		getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("corp").setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("deptname").setEdit(true);
//		getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("customer").setEdit(true);
	}
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		getBillUI().setBillOperate(IBillOperate.OP_EDIT);
        setBodyItemsEdit();
		onBoLineAdd();
		int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		BillModel model = getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().getTableModel();
		model.setRowEditState(true);
		model.setEditRow(selectRow);
		PlanApplyInforVO appinfor = ((ClientUI)getBillUI()).m_appInfor;
		if(appinfor==null)
			throw new BusinessException("��ȡ������Ϣʧ��");
		
		if(appinfor.getM_pocorp().equalsIgnoreCase(this._getCorp().getPrimaryKey())){
			getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("corp").setEdit(true);
		}else {
			getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("corp").setEdit(false);
		}
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("Y", selectRow, "fcontrol");//�Ƿ���Ƹ�ֵ
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getOperator(), selectRow, "coperatorid");//¼����
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getDate(), selectRow, "dmakedate");//¼������
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(HgPubConst.FUND_CHECK_QUATO, selectRow, "ifundtype");//��������
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(this._getCorp().getPrimaryKey(), selectRow, "vdef2");//��ֵ��ǰ��˾  ���ڲ�ѯ
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
	}
	
	@Override
	public void onBoEdit() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("deptname").setEdit(true);
		setBodyItemsEdit();
		super.onBoEdit();
		int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getOperator(), selectRow, "cmodifyman");//�޸���
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getDate(), selectRow, "dmodifydate");//�޸�����
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
		String corp = PuPubVO.getString_TrimZeroLenAsNull(getBodyCelValue(selectRow,"pk_corp"));
		String deptname = PuPubVO.getString_TrimZeroLenAsNull(getBodyCelValue(selectRow,"deptname"));
		if(!isNULL(corp)){
			if(_getCorp().getPrimaryKey().equalsIgnoreCase(corp)){//�����¼��˾����ѡ��˾����  ���Ų��ɱ༭
				getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("deptname").setEdit(true);
				getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("corp").setEdit(false);
			}else{
				getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("deptname").setEdit(false);
			}
		}else if(!isNULL(deptname)){
			getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("corp").setEdit(false);
		}
	}
	
	@Override
	protected void onBoSave() throws Exception {
		onCheckBeforeSave();
			super.onBoSave();
		
	}
	
	private void onCheckBeforeSave() throws BusinessException{
	
		int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		if(selectRow<0){
			return;
		}
		String oldid = PuPubVO.getString_TrimZeroLenAsNull(getBodyCelValue(selectRow,"corp"));
		String newid = PuPubVO.getString_TrimZeroLenAsNull(getBodyCelValue(selectRow,"customer"));
		String deptname = PuPubVO.getString_TrimZeroLenAsNull(getBodyCelValue(selectRow,"deptname"));
		if(isNULL(oldid) && isNULL(newid) && isNULL(deptname)){
			 throw new BusinessException("{��˾ ���� �ͻ�}����ͬʱΪ��");
		}else if(!isNULL(oldid) && !isNULL(newid)&& isNULL(deptname)){
			 throw new BusinessException("{��˾ ���� �ͻ�}����ͬʱ����");
		}
		UFDouble nfund =PuPubVO.getUFDouble_NullAsZero(getBodyCelValue(selectRow,"nfund"));//�ʽ�
		UFDouble nlockfund =PuPubVO.getUFDouble_NullAsZero(getBodyCelValue(selectRow,"nlockfund"));//Ԥ��
		UFDouble nactfund =PuPubVO.getUFDouble_NullAsZero(getBodyCelValue(selectRow,"nactfund"));//ʵ��
		if(nfund.compareTo(UFDouble.ZERO_DBL)< 0){
			throw new BusinessException("�ʽ���С����");
		}else{
			if(nfund.compareTo(nlockfund.add(nactfund))<0){
				throw new BusinessException("�ʽ���С��ʵ����Ԥ��֮��");
			}
		}
	}
	
	protected void onBoDelete() throws Exception {
		int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		Object nlockfund =getBodyCelValue(selectRow,"nlockfund");//Ԥ��
		Object nactfund =getBodyCelValue(selectRow,"nactfund");//ʵ��
		if(!isNULL(nlockfund) || !isNULL(nactfund) )
			throw new BusinessException("������Ԥ�ۻ�ʵ�۲���ɾ��");
		
		super.onBoDelete();
		
	}
	private Object getBodyCelValue(int row,String sitemname){
		return getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row,sitemname);
	}
	
	private boolean isNULL(Object o) {
		if (o == null || o.toString().trim().equals(""))
			return true;
		return false;
	}
}

package nc.ui.wds.ic.so.out;

import java.util.ArrayList;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.bill.BillListPanelWrapper;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wds.ic.pub.InPubClientUI;
import nc.ui.wds.ic.pub.OutPubClientUI;
import nc.ui.wds.ic.pub.OutPubEventHandler;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;
import nc.ui.wl.pub.BeforeSaveValudate;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.ic.pub.ScaleKey;
import nc.vo.ic.pub.ScaleValue;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.LoginInforVO;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 
 *  0 ����Ա 1 ��Ϣ�� 2 ���˿� 3����
 *  
 * 	�û�����0---------���Խ��г���
	�û�����1---------Ϊ����ǩ�ֵ��û�
	�û�����2---------�޸Ĳ���
	�û�����3---------����Ȩ��
 */

public class MySaleEventHandler extends OutPubEventHandler {

	private LoginInforHelper login=null;
	protected nc.ui.pub.print.PrintEntry m_print = null;
	
	protected ScaleValue m_ScaleValue=new ScaleValue();
	protected ScaleKey m_ScaleKey=new ScaleKey();
	public MySaleEventHandler(OutPubClientUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	protected UIDialog createQueryUI() {
		return new MyQueryDIG(
				getBillUI(),null,_getCorp().getPk_corp(),getBillUI().getModuleCode(),_getOperator(),null);
	}

	@Override
	protected String getHeadCondition() {
		return " pk_corp = '"+_getCorp().getPrimaryKey()+"' and isnull(dr,0) = 0 and vbilltype = '"+WdsWlPubConst.BILLTYPE_SALE_OUT+"' ";
	}


	private LoginInforHelper getLoginInfoHelper(){
		if(login==null){
			login=new LoginInforHelper();
		}
		return login;
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		switch (intBtn) {
			case ISsButtun.tpzd://����ָ��(�ֶ����)
				//��� ���ΨһУ��
				BeforeSaveValudate.beforeSaveBodyUnique(getBillCardPanelWrapper().getBillCardPanel().getBillTable(),
						getBillCardPanelWrapper().getBillCardPanel().getBillModel(),
						new String[]{"ccunhuobianma","batchcode"},
						new String[]{"�������","���κ�"});
				ontpzd();
				
				break;
			case ISsButtun.zdqh://�Զ�ȡ��
				//��� ���ΨһУ��
				BeforeSaveValudate.beforeSaveBodyUnique(getBillCardPanelWrapper().getBillCardPanel().getBillTable(),
						getBillCardPanelWrapper().getBillCardPanel().getBillModel(),
						new String[]{"ccunhuobianma","batchcode"},
						new String[]{"�������","���κ�"});
				onzdqh();
				
				break;
			case ISsButtun.ckmx://�鿴��ϸ
				onckmx();
				break;
			case ISsButtun.Qxqz://ȡ��ǩ��
				onQxqz();
				break;
			case ISsButtun.Qzqr://ǩ��ȷ��
				onQzqr();
				break;
			case nc.ui.wds.w80020206.buttun0206.ISsButtun.RefSoOrder://���������˵�
				((MyClientUI) getBillUI()).setRefBillType(WdsWlPubConst.WDS5);
					onBillRef();
					setInitByWhid(new String[]{"srl_pk","pk_cargdoc"});
					//��� ���յĳ���ֿ�Ϊ�� ����Ĭ�ϲֿ�Ϊ��ǰ����Ա�ֿ�
					setInitWarehouse("srl_pk");
				break;
			case nc.ui.wds.w80020206.buttun0206.ISsButtun.RefRedSoOrder://���������˵�
				((MyClientUI) getBillUI()).setRefBillType("30");
					onBillRef();
					setInitByWhid(new String[]{"srl_pk","pk_cargdoc"});
					//��� ���յĳ���ֿ�Ϊ�� ����Ĭ�ϲֿ�Ϊ��ǰ����Ա�ֿ�
					setInitWarehouse("srl_pk");
				break;
		}
	}

	
	@Override
	public void onBillRef() throws Exception {
		super.onBillRef();
		//��ǰ���Ա�󶨵Ļ�λ
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cargdoc").setValue(getLoginInfoHelper().getSpaceByLogUserForStore(_getOperator()));
		//�����Ա��ֵ
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cwhsmanagerid").setValue(_getOperator());	
		//������ҵ�����ڸ�ֵ
		int rowCounts=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		for(int i=0;i<rowCounts;i++){			
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getDate(), i, "dbizdate");
	    	getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(getPk_cargDoc(), i, "cspaceid");

		}	
	}

	@Override
	protected void onBoSave() throws Exception {
		
		//����ǩ����    С��    ʵ��������У��
		if( getBillUI().getVOFromUI().getChildrenVO()!=null){
			TbOutgeneralBVO[] tbs=(TbOutgeneralBVO[]) getBillUI().getVOFromUI().getChildrenVO();
			for(int i=0;i<tbs.length;i++){
				UFDouble u1=PuPubVO.getUFDouble_NullAsZero(tbs[i].getNoutassistnum());
				UFDouble u2=PuPubVO.getUFDouble_NullAsZero(tbs[i].getNtagnum());
				if(u1.sub(u2).doubleValue()<0){
					throw new BusinessException("��ǩ����   ���ܴ��� ʵ������");
				}
				
				
			}			
		}
		
		super.onBoSave();
	}

	// ȡ��ǩ��
	protected void onQxqz() throws Exception {
		try{
			if (getBufferData().getCurrentVO()!=null){
				getBillManageUI().showHintMessage("����ִ��ȡ��ǩ��...");
				int retu = getBillManageUI().showOkCancelMessage("ȷ��ȡ��ǩ��?");
				if (retu != 1) {
					return;
				}
				AggregatedValueObject aObject  = getBufferData().getCurrentVOClone();
				TbOutgeneralHVO generalh = (TbOutgeneralHVO)aObject.getParentVO();
				if(generalh.getFisload() != null &&generalh.getFisload().equals(UFBoolean.TRUE)){
					getBillUI().showWarningMessage("�Ѿ��γ�װж�Ѻ��㵥������ȡ��ǩ��");
					return ;
				}
				if(generalh.getFistran() != null &&generalh.getFistran().equals(UFBoolean.TRUE)){
					getBillUI().showWarningMessage("�Ѿ��γ��˷Ѻ��㵥������ȡ��ǩ��");
					return ;
				}
				generalh.setVbillstatus(IBillStatus.FREE);// ����״̬
				generalh.setCregister(null);// ǩ��������
				generalh.setTaccounttime(null);// ǩ��ʱ��
				generalh.setQianzidate(null);// ǩ������
				ArrayList<String> list = new ArrayList<String>();
				list.add(_getDate().toString());
				list.add(_getOperator());//��¼��
				//�����ű�
				PfUtilClient.processAction(getBillManageUI(), "CANELSIGN", getBillManageUI().getUIControl().getBillType(), _getDate().toString(), aObject,list );
				//���»���
				onBoRefresh();
			}
		}catch(Exception e){
			Logger.error(e);
			getBillManageUI().showErrorMessage("ȡ��ǩ��ʧ�ܣ�");
		}
	}
	
	/**
	 * �����б�����
	 */
	protected void updateListVo(int[] rows) throws java.lang.Exception {
		CircularlyAccessibleValueObject vo = null;
		if(rows!=null && rows.length >= 0){
			for(int i : rows){
				vo = getBufferData().getVOByRowNo(i).getParentVO();
				getBillListPanelWrapper().updateListVo(vo,i);
			}
		}
	}
	protected BillListPanelWrapper getBillListPanelWrapper() {
		return getBillManageUI().getBillListWrapper();
	} 
	// ǩ��ȷ��
	protected void onQzqr() throws Exception {
		try{
			if (getBufferData().getCurrentVO()!=null){
				getBillManageUI().showHintMessage("����ִ��ǩ��...");
				int retu = getBillManageUI().showOkCancelMessage("ȷ��ǩ��?");
				if (retu != 1) {
					return;
				}
				AggregatedValueObject aObject  = getBufferData().getCurrentVOClone();
				TbOutgeneralHVO generalh = (TbOutgeneralHVO)aObject.getParentVO();
				generalh.setVbillstatus(IBillStatus.CHECKPASS);// ����״̬
				generalh.setCregister(_getOperator());// ǩ��������
				generalh.setTaccounttime(getBillUI()._getServerTime().toString());// ǩ��ʱ��
				generalh.setQianzidate(_getDate());// ǩ������
				ArrayList<String> list = new ArrayList<String>();
				list.add(_getDate().toString());
				list.add(_getOperator());//��¼��
				//�����ű�
				PfUtilClient.processAction(getBillManageUI(), "SIGN", getBillManageUI().getUIControl().getBillType(), _getDate().toString(), aObject,list );
				//���»���
				onBoRefresh();
			}
		}catch(Exception e){
			Logger.error(e);
			getBillManageUI().showErrorMessage("ǩ��ʧ�ܣ�");
		}
	}
	@Override
	protected void onBoPrint() throws Exception {
		// TODO Auto-generated method stub
		super.onBoPrint();
		Integer iprintcount =PuPubVO.getInteger_NullAs(getBillCardPanelWrapper().getBillCardPanel().getTailItem("iprintcount").getValueObject(), 0) ;
		iprintcount=++iprintcount;
		getBillCardPanelWrapper().getBillCardPanel().getTailItem("iprintcount").setValue(iprintcount);
		getBufferData().getCurrentVO().getParentVO().setAttributeValue("iprintcount", iprintcount);
		HYPubBO_Client.update((SuperVO)getBufferData().getCurrentVO().getParentVO());
	}
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
		
		setInitByWhid(new String[]{"srl_pk","pk_cargdoc"});
	}
	
	//
	
}
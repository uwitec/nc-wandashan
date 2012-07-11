package nc.ui.wds.ic.allocation.out;

import java.util.ArrayList;

import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wds.ic.pub.OutPubClientUI;
import nc.ui.wds.ic.pub.OutPubEventHandler;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.BillRowNo;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * ��������
 * @author mlr
 */
public class OtherOutEventHandler extends OutPubEventHandler {


	
	private EventHandlerTools tools;
	
	public OtherOutEventHandler(OutPubClientUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		switch (intBtn) {
			case ISsButtun.zzdj:
				onzzdj(null);				
				break;
			case ISsButtun.zdqh:
				valudateWhereYeqian();
				onzdqh();			
				break;
			case nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz:
				onQxqz();
				break;
			case nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr:
				onQzqr();
				break;
			case nc.ui.wds.w80020206.buttun0206.ISsButtun.tsyd:
				((MyClientUI)getBillUI()).setRefBillType(WdsWlPubConst.WDSG);
				onBillRef();	
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem("ss_state").setEdit(false);
				break;	
			}
	}
	
	/**
	 * �����Ӧ��ҳǩ
	 * �����ڳ����ӱ�ҳǩ
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-9-22����02:56:45
	 */
    private void valudateWhereYeqian()throws Exception{
	   String tablecode=getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().getTableCode();
	   if(!"tb_outgeneral_b".equalsIgnoreCase(tablecode)){
		 throw new Exception("��ѡ�������ҳǩ");   
	   }
	}
	@Override
	protected void setRefData(AggregatedValueObject[] vos) throws Exception {
		// TODO Auto-generated method stub
		super.setRefData(vos);
		//���� �ֿ�ͻ�λ���Ƿ�ɱ༭���ֿܲ��ԣ��ֲֲ�����
		setInitByWhid(new String[]{"srl_pk","pk_cargdoc"});
		//���ò��ճ����г���ֿ�Ϊ�գ���ֵĬ�ϲֿ�Ϊ��ǰ����Ա�ֿ�
		setInitWarehouse("srl_pk");
		//���õ��ݺ�
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("vbillno", ((MyClientUI)getBillUI()).getBillNo());
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("is_yundan", null);
	}
	@Override
	protected UIDialog createQueryUI() {
		// TODO Auto-generated method stub
		return new MyQueryDIG(
				getBillUI(), null, 				
				_getCorp().getPk_corp(), getBillUI().getModuleCode()				
				, getBillUI()._getOperator(), null		
		);
	}
	@Override
	protected String getHeadCondition() {
		return " pk_corp = '"+_getCorp().getPrimaryKey()+"' and isnull(dr,0) = 0 and vbilltype = '"+WdsWlPubConst.BILLTYPE_ALLO_OUT+"' ";
	}

	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
	    BillRowNo.addLineRowNo(getBillCardPanelWrapper().getBillCardPanel(),WdsWlPubConst.BILLTYPE_OTHER_OUT, "crowno");
	    
	}

	
	// ǩ��ȷ��
	protected void onQzqr() throws Exception {
		try{
			if (getBufferData().getCurrentVO()!=null){
				getBillManageUI().showHintMessage("ִ��ǩ��...");
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
			getBillManageUI().showErrorMessage("ǩ��ʧ��:"+e.getMessage());
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
				int retu = getBillManageUI().showOkCancelMessage("ȡ��ǩ�ֻ�ɾ������װж�Ѻ��㵥���Ƿ�ȷ��ȡ��ǩ��?");
				if (retu != UIDialog.ID_OK) {
					return;
				}
				AggregatedValueObject aObject  = getBufferData().getCurrentVOClone();
				TbOutgeneralHVO generalh = (TbOutgeneralHVO)aObject.getParentVO();
//				if(generalh.getFisload() != null &&generalh.getFisload().equals(UFBoolean.TRUE)){
//					getBillUI().showWarningMessage("�Ѿ��γ�װж�Ѻ��㵥������ȡ��ǩ��");
//					return ;
//				}
//				if(generalh.getFistran() != null &&generalh.getFistran().equals(UFBoolean.TRUE)){
//					getBillUI().showWarningMessage("�Ѿ��γ��˷Ѻ��㵥������ȡ��ǩ��");
//					return ;
//				}
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
			getBillManageUI().showErrorMessage("ȡ��ǩ��ʧ��:"+e.getMessage());
		}
	}

	protected void onzzdj(ButtonObject bo) throws Exception {
		super.onBoAdd(bo);
	}
	
	
	
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		super.onBoAdd(bo);
		setInitByWhid(new String[]{"srl_pk","pk_cargdoc"});
	}

	private  EventHandlerTools getEventHanderTools(){
		if(tools == null){
			return new EventHandlerTools();
		}
		return tools;
	}

//	@Override
//	protected void onBoPrint() throws Exception {
//		super.onBoPrint();
//		Integer iprintcount =PuPubVO.getInteger_NullAs(getBillCardPanelWrapper().getBillCardPanel().getTailItem("iprintcount").getValueObject(), 0) ;
//		iprintcount=++iprintcount;
//		getBillCardPanelWrapper().getBillCardPanel().getTailItem("iprintcount").setValue(iprintcount);
//		getBufferData().getCurrentVO().getParentVO().setAttributeValue("iprintcount", iprintcount);
//		HYPubBO_Client.update((SuperVO)getBufferData().getCurrentVO().getParentVO());
//	
//		
//	}
	
	/**
	 * zhf add  ��֧���޸�ʱ �в���
	 */
	protected void onBoEdit() throws Exception {
		if (getBufferData().getCurrentVO() == null)
			return;
		UFBoolean isadjust = PuPubVO.getUFBoolean_NullAs(getBufferData().getCurrentVO().getParentVO().getAttributeValue("vuserdef15"), UFBoolean.FALSE);
		if(isadjust.booleanValue()){
			getBillUI().showHintMessage("���ڵ����ĳ��ⵥ�����޸�");
			return;
		}
		super.onBoEdit();
		setInitByWhid(new String[]{"srl_pk","pk_cargdoc"});
		
	}

	
	protected void onBoDel() throws Exception {
		if (getBufferData().getCurrentVO() == null)
			return;
		UFBoolean isadjust = PuPubVO.getUFBoolean_NullAs(getBufferData().getCurrentVO().getParentVO().getAttributeValue("vuserdef15"), UFBoolean.FALSE);
		if(isadjust.booleanValue()){
			getBillUI().showHintMessage("���ڵ����ĳ��ⵥ����ɾ��");
			return;
		}
		super.onBoDel();
		
	}

	
}
package nc.ui.wds.ic.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.wds.w80060206.buttun0206.ISsButtun;
import nc.ui.wl.pub.WdsBillManagUI;
import nc.vo.bd.invdoc.InvmandocVO;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wds.ic.cargtray.SmallTrayVO;

public class InPubClientUI extends WdsBillManagUI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	
	private Map<String,List<TbGeneralBBVO>> trayInfor = null;//������  �������µ� �к� + ������ˮ
	
	private Map<String,SmallTrayVO[]> lockTrayInfor = null;//�������̰󶨵�ʵ��������Ϣ zhf add
	public Map<String,SmallTrayVO[]> getLockTrayInfor(){
		return lockTrayInfor;
	}
	public void setLockTrayInfor(Map<String,SmallTrayVO[]>  newInfor){
		lockTrayInfor = newInfor;
	}
	
	public InPubClientUI() {
		super();
		initlize();
	}
	public InPubClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
		initlize();
	}
	public void setTrayInfor(Map<String,List<TbGeneralBBVO>> trayInfor2){
		trayInfor = trayInfor2;
	}
	
	public Map<String,List<TbGeneralBBVO>> getTrayInfor(){
		if(trayInfor == null)
			trayInfor = new HashMap<String, List<TbGeneralBBVO>>();
		return trayInfor;
	}
	@Override
	protected void setBillNo() throws Exception {
	
	}
	@Override
	protected AbstractManageController createController() {
		return null;
	}
	protected void setTotalUIState(int intOpType) throws Exception {
		super.setTotalUIState(intOpType);
		switch (intOpType) {
			case OP_ADD: 
			case OP_REFADD: {
				HashMap<String, List<TbGeneralBBVO>> map = 
					new HashMap<String, List<TbGeneralBBVO>>();
				setTrayInfor(map);
			}
			break;
				
		}
	}
	/**
	 * ��ʼ��
	 */
	private void initlize() {
		getBufferData().addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				if (!getBufferData().isVOBufferEmpty()){
					int row = getBufferData().getCurrentRow();
					if(row < 0){
						return;
					}
					//���»���
					AggregatedValueObject obj = getBufferData().getCurrentVO();
					if(obj != null){
						OtherInBillVO billvo = (OtherInBillVO)getBufferData().getCurrentVO();
						TbGeneralBVO[] bvo = (TbGeneralBVO[])billvo.getChildrenVO();
						setList(bvo);
					}
				}
			}		
		});
	}
	
	public void setList(TbGeneralBVO[] bvo){
		Map<String,List<TbGeneralBBVO>> m = new HashMap<String,List<TbGeneralBBVO>>();
		if(bvo!=null && bvo.length>0){
			for(TbGeneralBVO b : bvo){
				String crowno = b.getGeb_crowno();//�к�
				m.put(crowno, b.getTrayInfor());
			}
		}
		setTrayInfor(m);
	}
	
	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {

	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {

	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {

	}

	@Override
	protected void initSelfData() {

	}

	@Override
	public void setDefaultData() throws Exception {

	}

	public AggregatedValueObject getChangedVOFromUI() throws java.lang.Exception {
		OtherInBillVO billvo = (OtherInBillVO)this.getBillCardWrapper().getChangedVOFromUI();	
		OtherInBillVO billvo2 = (OtherInBillVO)this.getBillCardWrapper().getBillVOFromUI();	
		if(getBillOperate() == IBillOperate.OP_ADD){//����ʱ Ӧʹ�� ���� vo ����  ������ɾ�����
			billvo = billvo2;
		}
		if(billvo == null)
			return null;
		TbGeneralBVO[] bodys = (TbGeneralBVO[])billvo.getChildrenVO();		
		if(bodys == null || bodys.length==0)
			return billvo;
		
		if(trayInfor == null)
			return billvo;
		String key = null;
		for(TbGeneralBVO body:bodys){
			key = body.getGeb_crowno();
			if(trayInfor.containsKey(key)){
				body.setTrayInfor(trayInfor.get(key));
			}
		}
		
		billvo.setOUserObj(getLockTrayInfor());//�����������̰���Ϣ
		return billvo;
	}
	
	public AggregatedValueObject getBillVOFromUI() throws Exception {
		AggregatedValueObject billvo = getBillCardWrapper().getBillVOFromUI();
		return billvo;
	}
	
	
//	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
//		if(e.getPos() == BillItem.BODY){
//			String key = e.getKey();
//			int row = e.getRow();
//			if("geb_bsnum".equalsIgnoreCase(key)||"geb_snum".equalsIgnoreCase(key)){
//				getBillListPanel().getBodyBillModel().setValueAt(null, row, "geb_anum");
//				getBillListPanel().getBodyBillModel().setValueAt(null, row, "geb_banum");
//			}
//		}
//	}
	@Override
	public void afterEdit(BillEditEvent e) {
		try{
			String key = e.getKey();
			int row = e.getRow();
			Object value = e.getValue();
			if(e.getPos() == BillItem.HEAD){
				if("pk_cargdoc".equals(key)){//��λ
					UIRefPane u = (UIRefPane)e.getSource();
					afterHeadCargDoc(u.getRefPK());				
				}
			}else{
				if("geb_proddate".equals(key)){//��������
					String cinventoryid = (String)getBillCardPanel().getBodyValueAt(row, "geb_cinventoryid");
					if(value == null || "".equals(value)){
						getBillCardPanel().setBodyValueAt(null, row, "geb_dvalidate");//ʧЧ����
					}else{
						if(cinventoryid == null) 
							return;
						InvmandocVO vo = (InvmandocVO)HYPubBO_Client.queryByPrimaryKey(InvmandocVO.class, cinventoryid);
						UFDate date = new UFDate(value.toString());
						Integer num = vo.getQualitydaynum();//����������
						UFBoolean b = vo.getQualitymanflag();//�Ƿ�����
						if(b!=null && b.booleanValue()){
							getBillCardPanel().setBodyValueAt(date.getDateAfter(num), row, "geb_dvalidate");//ʧЧ����
						}
					}
				}else if("geb_dvalidate".equals(key)){//ʧЧ����
					String cinventoryid = (String)getBillCardPanel().getBodyValueAt(row, "geb_cinventoryid");
					if(value == null || "".equals(value)){
						getBillCardPanel().setBodyValueAt(null, row, "geb_proddate");//��������
					}else{
						if(cinventoryid == null) 
							return;
						InvmandocVO vo = (InvmandocVO)HYPubBO_Client.queryByPrimaryKey(InvmandocVO.class, cinventoryid);
						UFDate date = new UFDate(value.toString());
						Integer num = vo.getQualitydaynum();//����������
						UFBoolean b = vo.getQualitymanflag();//�Ƿ�����
						if(b!=null && b.booleanValue()){
							getBillCardPanel().setBodyValueAt(date.getDateBefore(num), row, "geb_proddate");//��������
						}
					}
			}
		}
		if("geb_vbatchcode".equalsIgnoreCase(key)){			
			// ��֤���κ��Ƿ���ȷ
			String va=(String) e.getValue();
			if(va==null ||va.equalsIgnoreCase("")){
				this.showErrorMessage("���κŲ���Ϊ��");
				return;
			}
			if (va.trim().length() < 8) {
				this.showErrorMessage("���κŲ���С��8λ");
			     return ;
			}
	
			Pattern p = Pattern
			.compile(
					"^((((1[6-9]|[2-9]\\d)\\d{2})(0?[13578]|1[02])(0?[1-9]|[12]\\d|3[01]))|"
					+ "(((1[6-9]|[2-9]\\d)\\d{2})(0?[13456789]|1[012])(0?[1-9]|[12]\\d|30))|"
					+ "(((1[6-9]|[2-9]\\d)\\d{2})0?2(0?[1-9]|1\\d|2[0-8]))|"
					+ "(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))0?229))$",
					Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			Matcher m = p.matcher(va.trim().substring(0, 8));
			if (!m.find()) {
				this.showErrorMessage(
				"���κ�����Ĳ���ȷ,����������ȷ������!�磺20100101XXXXXX");
				return;
	
			}
		    //������κ������ʽ��ȷ�͸��������ڸ�ֵ
			String year=va.substring(0,4);
			String month=va.substring(4,6);
			String day=va.substring(6,8);
			String startdate=year+"-"+month+"-"+day;
			UFDate date=new UFDate(startdate);
			getBillCardPanel().setBodyValueAt(date, row, "geb_proddate");
			
			//��ʧЧ�����ڸ�ֵ
			String cinventoryid = (String)getBillCardPanel().getBodyValueAt(row, "geb_cinventoryid");		
				if(cinventoryid == null) 
					return;
				InvmandocVO vo = (InvmandocVO)HYPubBO_Client.queryByPrimaryKey(InvmandocVO.class, cinventoryid);
				Integer num = vo.getQualitydaynum();//����������
				UFBoolean b = vo.getQualitymanflag();//�Ƿ�����
				if(b!=null && b.booleanValue()){
					getBillCardPanel().setBodyValueAt(date.getDateAfter(num), row, "geb_dvalidate");//ʧЧ����
				}			
		}	
			
			
			
		super.afterEdit(e);
	}catch(Exception e1){
		Logger.info(e1);
	}
	}
	
	public void afterHeadCargDoc(Object pk_cargdoc){
		//�������¸�ֵ[��λ]
		int row = getBillCardPanel().getBillTable().getRowCount();
		if(row < 0 ){
			return;
		}
		for(int i  = 0 ;i<row; i++){
			getBillCardPanel().getBillModel().setValueAt(pk_cargdoc, i, "geb_space");//��λ
		}
		//�����ǰ����
		getTrayInfor().clear();
		getBillCardPanel().getBillModel().execLoadFormula();
	}
	public void afterUpdate() {
		if (!getBufferData().isVOBufferEmpty()){
			int row = getBufferData().getCurrentRow();
			if(row < 0){
				return;
			}
			Object o = getBufferData().getCurrentVO().getParentVO().getAttributeValue(getBillField().getField_BillStatus());
			if(o.equals(IBillStatus.FREE)){//����
				getButtonManager().getButton(ISsButtun.Qxqz).setEnabled(false);
				getButtonManager().getButton(ISsButtun.Qzqr).setEnabled(true);
			}else{//ǩ��
				getButtonManager().getButton(ISsButtun.Qzqr).setEnabled(false);
				getButtonManager().getButton(ISsButtun.Qxqz).setEnabled(true);
			}
			updateButtons();
		}
	}
	
}

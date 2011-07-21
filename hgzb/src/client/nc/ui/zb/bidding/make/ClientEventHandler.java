package nc.ui.zb.bidding.make;

import java.util.HashMap;
import java.util.Map;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.zb.pub.BillRowNo;
import nc.ui.zb.pub.FlowManageEventHandler;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFTime;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.zb.bidding.BiddingBillVO;
import nc.vo.zb.bidding.BiddingBodyVO;
import nc.vo.zb.bidding.BiddingHeaderVO;
import nc.vo.zb.bidding.BiddingSuppliersVO;
import nc.vo.zb.bidding.BiddingTimesVO;
import nc.vo.zb.pub.ZbPuBtnConst;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class ClientEventHandler extends FlowManageEventHandler {

	public ClientUIQueryDlg queryDialog = null;
	
	private ClientLink cl = null;
	
	
	public ClientEventHandler(MakeBiddingUI clientUI, IControllerBase control) {
		super(clientUI, control);
		cl = new ClientLink(ClientEnvironment.getInstance());
	}

	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
			TemplateInfo tempinfo = new TemplateInfo();
			tempinfo.setPk_Org(_getCorp().getPk_corp());
			tempinfo.setCurrentCorpPk(_getCorp().getPk_corp());
			tempinfo.setFunNode(getBillUI()._getModuleCode());
			tempinfo.setUserid(getBillUI()._getOperator());
			queryDialog = new ClientUIQueryDlg(getBillUI(), null, tempinfo);
		}
		return queryDialog;
	}

	@Override
	protected String getHeadCondition() {
		String sql =" zb_bidding_h.pk_corp = '" + _getCorp().getPrimaryKey()
		+ "'  and  zb_bidding_h.pk_billtype = '" +ZbPubConst.ZB_BIDDING_BILLTYPE
		+ "' and  isnull(zb_bidding_h.dr,0)=0";
		String s=null;
		try {
			s = ZbPubTool.getParam();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(s!=null &&!"".equals(s))
		   sql = sql+" and zb_bidding_h.reserve1 = '"+s+"'";
		return sql;
	}


	@Override
	protected  MakeBiddingUI getBillManageUI() {
		return (MakeBiddingUI) getBillUI();
	}
	
	/**
	 * �������ӵĴ��� �������ڣ�(2002-12-23 12:43:15)
	 */
	public void onBoAdd(ButtonObject bo) throws Exception {
		
		super.onBoAdd(bo);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("fisself",UFBoolean.TRUE);
		if(PuPubVO.getInteger_NullAs(getBillCardPanel().getHeadItem("izbtype").getValueObject(),new Integer(-1))==1){
			getBillCardPanel().getBodyTabbedPane().setEnabledAt(2,false);
			getBillCardPanel().getBodyTabbedPane().setSelectedIndex(0);
		}
	}
	
	
	@Override
	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
		BillRowNo.addLineRowNo(getBillCardPanelWrapper().getBillCardPanel(),ZbPubConst.ZB_BIDDING_BILLTYPE,"crowno");
		if(2==getBillCardPanel().getBodyTabbedPane().getSelectedIndex()){
			int rowCount = getBillCardPanel().getBillModel("zb_biddingtimes").getRowCount();
			if(rowCount==1)
				getBillCardPanel().getBillModel("zb_biddingtimes").setValueAt("N",0,"bisfollow");
			else
				getBillCardPanel().getBillModel("zb_biddingtimes").setValueAt("Y",(getBillCardPanel().getBillTable("zb_biddingtimes").getSelectedRow()),"bisfollow");
		}
		
		String[] bstrs = new String[]{"fistemp","temp","gyscode"};
		for(String str:bstrs){//��Ӧ��ҵǩ���ɱ༭�ֶ�
			getBillCardPanel().getBillModel("zb_biddingsuppliers").getItemByKey(str).setEnabled(true);
		}
		
	}
	
	@Override
	protected void onBoSave() throws Exception {
	    
		doBeforeSave();
		super.onBoSave();
		getBillCardPanel().getBodyTabbedPane().setEnabledAt(0,true);
		getBillCardPanel().getBodyTabbedPane().setEnabledAt(1,true);
	}
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setEdit(true);
		if(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("izbtype").getValueObject())==null)//û���б����� Ĭ���� �ֳ��б�
		      getBillCardPanel().getHeadItem("izbtype").setValue(new Integer(1));
		if(PuPubVO.getInteger_NullAs(getBillCardPanel().getHeadItem("izbtype").getValueObject(),new Integer(-1))==1){
			getBillCardPanel().getBodyTabbedPane().setEnabledAt(2,false);
			getBillCardPanel().getBodyTabbedPane().setSelectedIndex(0);
		}
		
		if(getBillManageUI().m_logInfor==null||getBillManageUI().m_logInfor.length==0){
			getBillManageUI().showHintMessage("��ǰ�û�δ���ù���ҵ��Ա");
			return;
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("vemployeeid").getValueObject())==null){
			getBillCardPanel().getHeadItem("vemployeeid").setValue(getBillManageUI().m_logInfor[0]);
		}
		
		if(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("pk_deptdoc").getValueObject())==null){
			getBillCardPanel().getHeadItem("pk_deptdoc").setValue(getBillManageUI().m_logInfor[1]);
		}
		String[] bstrs = new String[]{"fistemp","temp","gyscode"};
		for(String str:bstrs){//��Ӧ��ҵǩ���ɱ༭�ֶ�
			getBillCardPanel().getBillModel("zb_biddingsuppliers").getItemByKey(str).setEnabled(true);
		}
		
		String para = ZbPubTool.getParam();
		if(para!=null&&!"".equalsIgnoreCase(para)&&getBillCardPanel().getHeadItem("reserve1").getValueObject()==null)
		    getBillCardPanel().setHeadItem("reserve1",para);
	}
	
	private void  doBeforeSave() throws Exception {
		BiddingBillVO billvo =(BiddingBillVO)getBillUI().getVOFromUI();
		if(billvo == null)
			return;
		BiddingHeaderVO head = (BiddingHeaderVO) billvo.getParentVO();
		if(head == null)
			return;
//		setVbillno(head);
		checkTiems(billvo,head);
		if(PuPubVO.getUFBoolean_NullAs(head.getFisself(),UFBoolean.FALSE).booleanValue())
		   checkBodys();
		
	}
	
	//У�����Ƿ���ͬһ����
	private void checkBodys() throws BusinessException{
		BillModel bm =getBillCardPanel().getBillModel(BiddingBillVO.tablecode_body);
		int rowCount = bm.getRowCount();
		Map<String,String> map = new HashMap<String,String>();
		for(int i=0;i<rowCount;i++){
			String code =PuPubVO.getString_TrimZeroLenAsNull(bm.getValueAt(i,"invcode"));
			if(code!=null){
				String str =code.substring(0,2);
				if(!map.containsKey(str)){
					map.put(str, code);
				}
			}
		}
		if(map!=null&&map.size()>1)
			throw new BusinessException("���ڲ�ͬ����Ĵ��");
	}
	
	//���õ��ݺ�
	private void  setVbillno(BiddingHeaderVO head) throws UifException{
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getVbillno())==null){
			String billno = HYPubBO_Client.getBillNo(ZbPubConst.ZB_BIDDING_BILLTYPE,_getCorp().getPrimaryKey(), null, null);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setValue(billno);
		}
	}
	
	//У���ִ�ʱ�䰲��
	private void checkTiems(BiddingBillVO billvo,BiddingHeaderVO head) throws BusinessException{
		if(PuPubVO.getInteger_NullAs(head.getIzbtype(),new Integer(-1))==0){
			BiddingTimesVO[] times = (BiddingTimesVO[])billvo.getTableVO("zb_biddingtimes");
			VOUtil.sort(times,new String[]{"crowno"},new int[]{VOUtil.ASC});
			if(times !=null && times.length>0){
				int len =times.length;
				for(int i=0;i<len;i++){
					if(PuPubVO.getString_TrimZeroLenAsNull(times[i].getVname())==null)
						throw new BusinessException("�ִ����Ʋ���Ϊ��");
					UFDate dbd =times[i].getDbegindate();
					UFDate ded =times[i].getDendate();
					UFTime dst =times[i].getTstart();
					UFTime det =times[i].getTend();
					if(dbd !=null && dst !=null ){
						UFDateTime udtb = new UFDateTime(PuPubVO.getString_TrimZeroLenAsNull(dbd)+" "+PuPubVO.getString_TrimZeroLenAsNull(dst));
						getBillCardPanelWrapper().getBillCardPanel().getBillModel("zb_biddingtimes").setValueAt(udtb, i, "tbigintime");
					}

					if(ded !=null && det !=null ){
						UFDateTime udte = new UFDateTime(PuPubVO.getString_TrimZeroLenAsNull(ded)+" "+PuPubVO.getString_TrimZeroLenAsNull(det));
						getBillCardPanelWrapper().getBillCardPanel().getBillModel("zb_biddingtimes").setValueAt(udte, i, "tendtime");
					}
					if(dbd !=null && dst !=null){
						if(dbd.after(ded))
							throw new BusinessException("��"+times[i].getCrowno()+"���ִο�ʼ�������ڱ��н�������");

						if(dbd.equals(ded))
							if(!dst.before(det))
								throw new BusinessException("��"+times[i].getCrowno()+"���ִο�ʼʱ��������ڱ��н���ʱ��");
					}
					
					if(PuPubVO.getUFBoolean_NullAs(times[i].getBisfollow(),UFBoolean.FALSE).booleanValue()){
						
						if(len>1 && i<len-1){
							UFDate dbd1 =times[i+1].getDbegindate();
							UFTime dst1 =times[i+1].getTstart();
							if(dbd1.before(ded))
								throw new BusinessException("��"+times[i+1].getCrowno()+"���ִο�ʼ�������ڵ�"+times[i].getCrowno()+"�н�������");
							if(dbd1.equals(ded))
								if(dst1.before(det))
									throw new BusinessException("��"+times[i+1].getCrowno()+"���ִο�ʼʱ�����ڵ�"+times[i].getCrowno()+"�н���ʱ��");
						}
				   }
			    }
			}	
		}
	}
	
	/**
	 * ���յ��ݽ����Ƶ��� �������ڣ�(2003-6-12 10:01:28)
	 */
	public void onBillRef() throws Exception {
//		
		BiddingBillVO[] bills = MakeBiddingHelper.refAddButtonClicked( getBillUI(),cl);
		if(bills == null || bills.length ==0)
		{
			getBillUI().showHintMessage("�������");
			return;
		}
		
		getBufferData().clear();
		
		getBufferData().addVOsToBuffer(bills);
//		getBufferData().setCurrentRow(0);
		
		updateBuffer();
	}

	protected void onBoElse(int intBtn) throws Exception {
		if(intBtn==ZbPuBtnConst.REVISED){
			onRevisedBill();
		}else if(intBtn==ZbPuBtnConst.ViewHis){
			onViewHis();
		}else if(intBtn==ZbPuBtnConst.StopVen){
			onStopVen(true);//��ֹ
		}else if(intBtn==ZbPuBtnConst.StartVen){
			onStopVen(false);
		}else if(intBtn == ZbPuBtnConst.MISS){
			onMiss();
		}else if(intBtn == ZbPuBtnConst.Editor){//�޸�
			   onBoEdit();
		}else{
			super.onBoElse(intBtn);
		}
	}
	
	private HistoryPriceDlg m_historyPriceDlg = null;
	private HistoryPriceDlg getHistoryPriceDlg(){
		if(m_historyPriceDlg == null)
			m_historyPriceDlg = new HistoryPriceDlg(getBillManageUI());
		return m_historyPriceDlg;
	}
	
	
	
	private void onViewHis(){
		if(getBufferData().isVOBufferEmpty()){
			getBillUI().showHintMessage("������");
			return;
		}
		if(getBufferData().getCurrentRow()<0){
			getBillUI().showErrorMessage("��ѡ������");
			return;
		}
		
		int selindex =0;
		UITable bt = null;
		BillModel bm = null;
		if(getBillManageUI().isCardPanelSelected()){
			selindex = getBillCardPanel().getBodyTabbedPane().getSelectedIndex();
			bt = getBillCardPanel().getBillTable(BiddingBillVO.tablecode_body);
			bm = getBillCardPanel().getBillModel(BiddingBillVO.tablecode_body);
		}else{
			selindex = getBillListPanel().getBodyTabbedPane().getSelectedIndex();
			bt = getBillListPanel().getBodyTable(BiddingBillVO.tablecode_body);
			bm = getBillListPanel().getBodyBillModel(BiddingBillVO.tablecode_body);
		}
		if(selindex!=0){
			getBillUI().showErrorMessage("��ѡ��Ʒ��ҳǩ");
			return;
		}
		int row = bt.getSelectedRow();
		if(row<0){
			getBillUI().showErrorMessage("��ѡ��һ��Ʒ��");
			return;
		}
		
		BiddingBillVO bill = (BiddingBillVO)getBufferData().getCurrentVO();
		BiddingBodyVO body = (BiddingBodyVO)bill.getChildrenVO()[row];	
		getHistoryPriceDlg().setHistoryPara(bill.getHeader().getPrimaryKey(), body.getCinvbasid(), ClientEnvironment.getInstance().getDate(), body.getNaverageprice());
		if(getHistoryPriceDlg().showModal()!=UIDialog.ID_OK){
			return;
		}
		if(getHistoryPriceDlg().isUpdate()){
			//body.setTs(new UFDateTime(getHistoryPriceDlg().getNewTs()));
			body.setNaverageprice(getHistoryPriceDlg().getNewPrice());
		}
		bm.setValueAt(getHistoryPriceDlg().getNewPrice(), row, "naverageprice");			
	}
	
	private void onRevisedBill() throws Exception{
		if(PuPubVO.getInteger_NullAs(getBillCardPanel().getHeadItem("ibusstatus").getValueObject(),new Integer(-1)).intValue()>3)
			throw new BusinessException("�����Ѿ����,�����޶�");
		super.onBoEdit();
		setItemEnable(false);
	}
	//�޶���ͬ�� ���ñ༭��
	private void setItemEnable(boolean flag){
		//ibusstatus ҵ��״̬  0 --��ʼ  1--Ͷ��  2--����  3--�б�  4--���
		
		String[] strs = new String[]{"cname","pk_deptdoc","vemployeeid","izbtype","vbillno"};
		for(String str:strs){//��ͷ���ɱ༭�ֶ�
			getBillCardPanel().getHeadItem(str).setEnabled(flag);
		}
		
		String[] bstrs = new String[]{"fistemp","temp","gyscode"};
		for(String str:bstrs){//��Ӧ��ҵǩ���ɱ༭�ֶ�
			getBillCardPanel().getBillModel("zb_biddingsuppliers").getItemByKey(str).setEnabled(flag);
		}
		
		getBillCardPanel().getBodyTabbedPane().setEnabledAt(0,flag);//����Ʒ����Ϣ������
		
		if(PuPubVO.getInteger_NullAs(getBillCardPanel().getHeadItem("ibusstatus").getValueObject(),new Integer(-1)).intValue()==3)//�б�
			getBillCardPanel().getHeadItem("nvendornum").setEnabled(flag);//��Χ��Ӧ���������ɱ༭
		
		if(PuPubVO.getInteger_NullAs(getBillCardPanel().getHeadItem("izbtype").getValueObject(),
				new Integer(-1)).intValue()==1){//�ֳ��б�
			getBillCardPanel().getBodyTabbedPane().setEnabledAt(2,flag);
			getButtonManager().getButton(IBillButton.Line).setEnabled(flag);
			getBillManageUI().updateButton(getButtonManager().getButton(IBillButton.Line));// ���ò�����ť������
			
		}
		getBillCardPanel().getBodyTabbedPane().setSelectedIndex(1);
	}
	
	private void setItemEnable1(boolean flag){
		
		getBillCardPanel().getBodyTabbedPane().setEnabledAt(2,flag);
		getButtonManager().getButton(IBillButton.Line).setEnabled(flag);
		getBillManageUI().updateButton(getButtonManager().getButton(IBillButton.Line));// ���ò�����ť������
		getBillCardPanel().getBillModel().setEnabled(flag);
			
		getBillCardPanel().getBodyTabbedPane().setEnabledAt(0,flag);
		getBillCardPanel().getBodyTabbedPane().setEnabledAt(1,flag);
		String[] strs = new String[]{"cname","pk_deptdoc","vemployeeid","izbtype","vbillno"};
		for(String str:strs){
			getBillCardPanel().getHeadItem(str).setEnabled(flag);
		}
		String[] bstrs = new String[]{"fistemp","temp","gyscode"};
		for(String str:bstrs){//��Ӧ��ҵǩ���ɱ༭�ֶ�
			getBillCardPanel().getBillModel("zb_biddingsuppliers").getItemByKey(str).setEnabled(flag);
		}
	}
	
	protected void onBoCancel() throws Exception {
		setItemEnable1(true);
		super.onBoCancel();
	}
	
	private void onStopVen(boolean flag) throws Exception{
		if(getBufferData().isVOBufferEmpty()){
			getBillUI().showHintMessage("������");
			return;
		}
		if(getBufferData().getCurrentRow()<0){
			getBillUI().showErrorMessage("��ѡ������");
			return;
		}
		int x = PuPubVO.getInteger_NullAs(getBillCardPanel().getHeadItem("ibusstatus").getValueObject(), new Integer(-1)).intValue();
		if(x == ZbPubConst.BIDDING_BUSINESS_STATUE_RESULT){
			getBillUI().showErrorMessage("���б�");
			return;
		}else if(x == ZbPubConst.BIDDING_BUSINESS_STATUE_CLOSE){
			getBillUI().showErrorMessage("�б����");
//			error.append(":"+head.getVbillno()+"�б����");
			return;
		}else if(x == ZbPubConst.BIDDING_BUSINESS_STATUE_MISS){
			getBillUI().showErrorMessage("������");
//			error.append(":"+head.getVbillno()+"������");
			return;
		}
		
		int selindex =0;
		UITable bt = null;
		BillModel bm = null;
		if(getBillManageUI().isCardPanelSelected()){
			selindex = getBillCardPanel().getBodyTabbedPane().getSelectedIndex();
			bt = getBillCardPanel().getBillTable(BiddingBillVO.tablecode_suppliers);
			bm = getBillCardPanel().getBillModel(BiddingBillVO.tablecode_suppliers);
		}else{
			selindex = getBillListPanel().getBodyTabbedPane().getSelectedIndex();
			bt = getBillListPanel().getBodyTable(BiddingBillVO.tablecode_suppliers);
			bm = getBillListPanel().getBodyBillModel(BiddingBillVO.tablecode_suppliers);
		}
		if(selindex!=1){
			getBillUI().showErrorMessage("��ѡ��Ӧ��ҵǩҳǩ");
			return;
		}
		int row = bt.getSelectedRow();
		if(row<0){
			getBillUI().showErrorMessage("��ѡ��һ�й�Ӧ��");
			return;
		}
		
		
		
		BiddingBillVO bill = (BiddingBillVO)getBufferData().getCurrentVO();
		BiddingSuppliersVO suppliers = (BiddingSuppliersVO)bill.getTableVO(BiddingBillVO.tablecode_suppliers)[row];
		
		if(!false){
			MakeBiddingHelper.isExitBadPrice(suppliers.getCcustmanid(),suppliers.getCbiddingid());
		}
		
		if(flag && PuPubVO.getUFBoolean_NullAs(suppliers.getFisclose(),UFBoolean.FALSE).booleanValue())
			throw new BusinessException("�к�Ϊ"+suppliers.getCrowno()+"�Ĺ�Ӧ���Ѿ���ֹ");
		
		
		if(!flag && !PuPubVO.getUFBoolean_NullAs(suppliers.getFisclose(),UFBoolean.FALSE).booleanValue())
			throw new BusinessException("�к�Ϊ"+suppliers.getCrowno()+"�Ĺ�Ӧ��û����ֹ");
		
		int  icloseno=0;
		suppliers.setFisclose(new UFBoolean(flag));
	
		if(flag){
			icloseno =PuPubVO.getInteger_NullAs(suppliers.getIcloseno(),new Integer(0)).intValue();
//			bm.setValueAt(icloseno+1, row, "icloseno");
			suppliers.setIcloseno(icloseno+1);
		}

		suppliers.setCcloseman(_getOperator());
		suppliers.setCclosetime(ClientEnvironment.getServerTime().toString());
		
		
		HYPubBO_Client.update(suppliers);
		bm.setValueAt(new UFBoolean(flag).toString(), row, "fisclose");	
		if(flag){
			bm.setValueAt(icloseno+1, row, "icloseno");
		}
		bm.setValueAt(ClientEnvironment.getServerTime().toString(), row, "cclosetime");
		bm.setValueAt(_getOperator(), row, "ccloseman");
		bm.execLoadFormulaByRow(row);
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ������
	 * 2011-6-14����03:53:14
	 */
	private void onMiss(){
		if(getBufferData().isVOBufferEmpty()){
			getBillUI().showHintMessage("����Ϊ��");
			return;
		}
		BiddingBillVO bill = (BiddingBillVO)getBufferData().getCurrentVO();
		Object o = null;
		if(bill == null){
			return;
		}
		BiddingHeaderVO head = bill.getHeader();
		int flag = PuPubVO.getInteger_NullAs(head.getIbusstatus(), -1);
		if(flag == ZbPubConst.BIDDING_BUSINESS_STATUE_RESULT){
			getBillUI().showErrorMessage("���б�");
			return;
		}else if(flag == ZbPubConst.BIDDING_BUSINESS_STATUE_CLOSE){
			getBillUI().showErrorMessage("�б����");
//			error.append(":"+head.getVbillno()+"�б����");
			return;
		}else if(flag == ZbPubConst.BIDDING_BUSINESS_STATUE_MISS){
			getBillUI().showErrorMessage("������");
//			error.append(":"+head.getVbillno()+"������");
			return;
		}
		
		try {
			o = MakeBiddingHelper.misBidding(getBillUI(), new String[]{bill.getHeader().getPrimaryKey()}, _getOperator(), _getDate());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			getBillUI().showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}

		Object[] os = (Object[])o;
		String error = PuPubVO.getString_TrimZeroLenAsNull(os[0]);

		Map<String,UFDateTime> tsInfor = (Map<String,UFDateTime>)os[1];
		if(tsInfor == null || tsInfor.size() ==0)
			return;
		
//		ǰ̨��δ֧�� ��������
		if(error!=null){
			getBillUI().showErrorMessage(error);
			return;
		}
		UFDateTime newts = tsInfor.values().iterator().next();
		bill.getHeader().setTs(newts);
		bill.getHeader().setIbusstatus(ZbPubConst.BIDDING_BUSINESS_STATUE_MISS);

		if(getBillManageUI().isListPanelSelected()){
			getBillListPanel().getHeadBillModel().setValueAt(ZbPubConst.BIDDING_BUSINESS_STATUE_MISS, getBufferData().getCurrentRow(), "ibusstatus");
		}else{
			getBillCardPanel().getHeadItem("ibusstatus").setValue(ZbPubConst.BIDDING_BUSINESS_STATUE_MISS);
		}
	}
}